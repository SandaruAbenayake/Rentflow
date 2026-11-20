package com.rentflow.rental;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
@RestController @RequestMapping("/api/v1")
public class RentalController {
    private final CustomerRepository customers;private final BookingRepository bookings;
    private final BookingLockRepository locks;private final PaymentRepository payments;private final TripRepository trips;
    public RentalController(CustomerRepository customers,BookingRepository bookings,BookingLockRepository locks,PaymentRepository payments,TripRepository trips){
        this.customers=customers;this.bookings=bookings;this.locks=locks;this.payments=payments;this.trips=trips;
    }
    private UUID company(Authentication auth){String value=(String)auth.getDetails();if(value==null||value.isBlank())throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Company membership required");return UUID.fromString(value);}
    public record CustomerRequest(@NotBlank String fullName,@Email String email,@NotBlank String phone,String address,String identificationType,String identificationNumber){}
    @GetMapping("/customers") public List<Customer> customers(Authentication auth){return customers.findByCompanyId(company(auth));}
    @PostMapping("/customers") public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequest r,Authentication auth){
        UUID companyId=company(auth);Customer c=new Customer();c.setId(UUID.randomUUID());c.setCompanyId(companyId);
        c.setFullName(r.fullName());c.setEmail(r.email());c.setPhone(r.phone());c.setAddress(r.address());
        c.setIdentificationType(r.identificationType());c.setIdentificationNumber(r.identificationNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(customers.save(c));
    }
    @PutMapping("/customers/{id}") public Customer updateCustomer(@PathVariable UUID id,@Valid @RequestBody CustomerRequest r,Authentication auth){
        UUID companyId=company(auth);Customer c=customers.findByIdAndCompanyId(id,companyId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found"));
        c.setFullName(r.fullName());c.setEmail(r.email());c.setPhone(r.phone());c.setAddress(r.address());c.setIdentificationType(r.identificationType());c.setIdentificationNumber(r.identificationNumber());
        return customers.save(c);
    }
    public record BookingRequest(@NotNull UUID customerId,@NotNull UUID vehicleId,UUID driverId,@NotNull Instant startAt,@NotNull Instant endAt,
        @DecimalMin("0.01") BigDecimal dailyRateSnapshot,BigDecimal depositAmount,String notes){}
    @GetMapping("/bookings") public List<Booking> bookings(Authentication auth){return bookings.findByCompanyId(company(auth));}
    @PostMapping("/bookings") @Transactional
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest r,Authentication auth){
        UUID companyId=company(auth);
        if(!r.startAt.isBefore(r.endAt))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"End time must be after start time");
        if(customers.findByIdAndCompanyId(r.customerId,companyId).isEmpty())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer does not belong to this company");
        BookingLock.Key key=new BookingLock.Key(companyId,r.vehicleId);
        BookingLock lock=locks.findForUpdate(companyId,r.vehicleId).orElseGet(()->{BookingLock l=new BookingLock();l.setCompanyId(companyId);l.setVehicleId(r.vehicleId);return locks.save(l);});
        if(!bookings.findOverlapping(companyId,r.vehicleId,r.startAt,r.endAt).isEmpty())throw new ResponseStatusException(HttpStatus.CONFLICT,"Vehicle is already booked for an overlapping period");
        Booking b=new Booking();b.setId(UUID.randomUUID());b.setCompanyId(companyId);b.setCustomerId(r.customerId);b.setVehicleId(r.vehicleId);b.setDriverId(r.driverId);
        b.setStartAt(r.startAt);b.setEndAt(r.endAt);b.setDailyRateSnapshot(r.dailyRateSnapshot==null?BigDecimal.ZERO:r.dailyRateSnapshot);
        long days=Math.max(1,(r.endAt.toEpochMilli()-r.startAt.toEpochMilli()+86_399_999)/(86_400_000));
        b.setTotalAmount(b.getDailyRateSnapshot().multiply(BigDecimal.valueOf(days)));b.setDepositAmount(r.depositAmount);
        b.setStatus(BookingStatus.PENDING);b.setNotes(r.notes);b.setCreatedBy(UUID.fromString(auth.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookings.save(b));
    }
    @PatchMapping("/bookings/{id}/status") @Transactional
    public Booking setBookingStatus(@PathVariable UUID id,@RequestBody Map<String,String> body,Authentication auth){
        Booking b=bookings.findByIdAndCompanyId(id,company(auth)).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found"));
        try{b.setStatus(BookingStatus.valueOf(body.getOrDefault("status","")));}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid booking status");}
        return bookings.save(b);
    }
    public record PaymentRequest(@NotNull UUID bookingId,@DecimalMin("0.01") BigDecimal amount,@NotBlank String paymentMethod,@NotNull PaymentStatus status,String transactionReference,Instant paymentDate,String notes){}
    @GetMapping("/payments") public List<Payment> payments(Authentication auth){return payments.findByCompanyId(company(auth));}
    @PostMapping("/payments") public ResponseEntity<Payment> recordPayment(@Valid @RequestBody PaymentRequest r,Authentication auth){
        UUID companyId=company(auth);if(bookings.findByIdAndCompanyId(r.bookingId,companyId).isEmpty())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Payment booking does not belong to this company");
        Payment p=new Payment();p.setId(UUID.randomUUID());p.setCompanyId(companyId);p.setBookingId(r.bookingId);p.setAmount(r.amount);
        p.setPaymentMethod(r.paymentMethod);p.setStatus(r.status);p.setTransactionReference(r.transactionReference);
        p.setPaymentDate(r.paymentDate==null?Instant.now():r.paymentDate);p.setNotes(r.notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(payments.save(p));
    }
    public record TripRequest(@NotNull UUID bookingId,@NotNull UUID vehicleId,UUID driverId,@NotBlank String tripType,String status,Instant startAt,Instant endAt,String startLocation,String endLocation){}
    @GetMapping("/trips") public List<Trip> trips(Authentication auth){return trips.findByCompanyId(company(auth));}
    @PostMapping("/trips") public ResponseEntity<Trip> createTrip(@Valid @RequestBody TripRequest r,Authentication auth){
        UUID companyId=company(auth);if(bookings.findByIdAndCompanyId(r.bookingId,companyId).isEmpty())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Trip booking does not belong to this company");
        Trip t=new Trip();t.setId(UUID.randomUUID());t.setCompanyId(companyId);t.setBookingId(r.bookingId);t.setVehicleId(r.vehicleId);t.setDriverId(r.driverId);
        t.setTripType(r.tripType);t.setStatus(r.status==null?"PLANNED":r.status);t.setStartAt(r.startAt);t.setEndAt(r.endAt);t.setStartLocation(r.startLocation);t.setEndLocation(r.endLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(trips.save(t));
    }
}
