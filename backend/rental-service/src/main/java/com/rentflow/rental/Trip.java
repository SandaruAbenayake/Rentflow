package com.rentflow.rental;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="trips")
public class Trip {
    @Id @Column(columnDefinition="char(36)") private UUID id;
    @Column(name="company_id",columnDefinition="char(36)",nullable=false) private UUID companyId;
    @Column(name="booking_id",columnDefinition="char(36)",nullable=false) private UUID bookingId;
    @Column(name="vehicle_id",columnDefinition="char(36)",nullable=false) private UUID vehicleId;
    @Column(name="driver_id",columnDefinition="char(36)") private UUID driverId;
    @Column(name="trip_type",nullable=false) private String tripType;
    @Column(nullable=false) private String status;
    @Column(name="start_at") private Instant startAt;
    @Column(name="end_at") private Instant endAt;
    @Column(name="start_location") private String startLocation;
    @Column(name="end_location") private String endLocation;
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
    public UUID getId(){return id;} public void setId(UUID v){id=v;}
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID v){companyId=v;}
    public UUID getBookingId(){return bookingId;} public void setBookingId(UUID v){bookingId=v;}
    public UUID getVehicleId(){return vehicleId;} public void setVehicleId(UUID v){vehicleId=v;}
    public UUID getDriverId(){return driverId;} public void setDriverId(UUID v){driverId=v;}
    public String getTripType(){return tripType;} public void setTripType(String v){tripType=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;updatedAt=Instant.now();}
    public Instant getStartAt(){return startAt;} public void setStartAt(Instant v){startAt=v;}
    public Instant getEndAt(){return endAt;} public void setEndAt(Instant v){endAt=v;}
    public String getStartLocation(){return startLocation;} public void setStartLocation(String v){startLocation=v;}
    public String getEndLocation(){return endLocation;} public void setEndLocation(String v){endLocation=v;}
}
