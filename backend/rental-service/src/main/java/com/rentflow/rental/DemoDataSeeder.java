package com.rentflow.rental;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Component
public class DemoDataSeeder implements CommandLineRunner {
    private final CustomerRepository customers;
    private final BookingRepository bookings;
    private final PaymentRepository payments;
    @Value("${app.seed.demo-data:false}") private boolean enabled;

    public DemoDataSeeder(CustomerRepository customers, BookingRepository bookings, PaymentRepository payments) {
        this.customers = customers;
        this.bookings = bookings;
        this.payments = payments;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) return;
        UUID acmeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID acmeCustomerId = UUID.fromString("00000000-0000-0000-0000-000000000201");
        UUID zenithId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        UUID zenithCustomerId = UUID.fromString("00000000-0000-0000-0000-000000000202");

        seedCustomer(acmeCustomerId, acmeId, "Jane Doe", "jane.doe@example.test",
            "+1-555-0111", "DL-ACME-1");
        seedCustomer(zenithCustomerId, zenithId, "John Smith", "john.smith@example.test",
            "+1-555-0211", "DL-ZEN-1");

        UUID bookingId = UUID.fromString("00000000-0000-0000-0000-000000000301");
        Booking booking = bookings.findById(bookingId).orElseGet(() -> createBooking(
            bookingId, acmeId, acmeCustomerId));
        UUID paymentId = UUID.fromString("00000000-0000-0000-0000-000000000401");
        if (!payments.existsById(paymentId)) {
            Payment payment = new Payment();
            payment.setId(paymentId);
            payment.setCompanyId(acmeId);
            payment.setBookingId(booking.getId());
            payment.setAmount(booking.getTotalAmount());
            payment.setPaymentMethod("UNSPECIFIED");
            payment.setStatus(PaymentStatus.PENDING);
            payment.setPaymentDate(Instant.now());
            payments.save(payment);
        }
    }

    private void seedCustomer(UUID id, UUID companyId, String name, String email,
                              String phone, String licenseNumber) {
        if (customers.existsById(id)) return;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setCompanyId(companyId);
        customer.setFullName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setIdentificationType("DRIVER_LICENSE");
        customer.setIdentificationNumber(licenseNumber);
        customers.save(customer);
    }

    private Booking createBooking(UUID id, UUID companyId, UUID customerId) {
        Instant start = Instant.now().plusSeconds(86_400);
        Booking booking = new Booking();
        booking.setId(id);
        booking.setCompanyId(companyId);
        booking.setCustomerId(customerId);
        booking.setVehicleId(UUID.fromString("00000000-0000-0000-0000-000000000101"));
        booking.setStartAt(start);
        booking.setEndAt(start.plusSeconds(3 * 86_400L));
        booking.setDailyRateSnapshot(new BigDecimal("45.00"));
        booking.setTotalAmount(new BigDecimal("135.00"));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCreatedBy(UUID.fromString("00000000-0000-0000-0000-000000000003"));
        return bookings.save(booking);
    }
}
