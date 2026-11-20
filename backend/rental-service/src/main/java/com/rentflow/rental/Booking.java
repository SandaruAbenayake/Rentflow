package com.rentflow.rental;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="bookings")
public class Booking {
    @Id @Column(columnDefinition="char(36)") private UUID id;
    @Column(name="company_id",columnDefinition="char(36)",nullable=false) private UUID companyId;
    @Column(name="customer_id",columnDefinition="char(36)",nullable=false) private UUID customerId;
    @Column(name="vehicle_id",columnDefinition="char(36)",nullable=false) private UUID vehicleId;
    @Column(name="driver_id",columnDefinition="char(36)") private UUID driverId;
    @Column(name="start_at",nullable=false) private Instant startAt;
    @Column(name="end_at",nullable=false) private Instant endAt;
    @Column(name="daily_rate_snapshot",precision=12,scale=2,nullable=false) private BigDecimal dailyRateSnapshot;
    @Column(name="total_amount",precision=12,scale=2,nullable=false) private BigDecimal totalAmount;
    @Column(name="deposit_amount",precision=12,scale=2) private BigDecimal depositAmount;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private BookingStatus status;
    private String notes;
    @Column(name="created_by",columnDefinition="char(36)",nullable=false) private UUID createdBy;
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
    public UUID getId(){return id;} public void setId(UUID v){id=v;}
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID v){companyId=v;}
    public UUID getCustomerId(){return customerId;} public void setCustomerId(UUID v){customerId=v;}
    public UUID getVehicleId(){return vehicleId;} public void setVehicleId(UUID v){vehicleId=v;}
    public UUID getDriverId(){return driverId;} public void setDriverId(UUID v){driverId=v;}
    public Instant getStartAt(){return startAt;} public void setStartAt(Instant v){startAt=v;}
    public Instant getEndAt(){return endAt;} public void setEndAt(Instant v){endAt=v;}
    public BigDecimal getDailyRateSnapshot(){return dailyRateSnapshot;} public void setDailyRateSnapshot(BigDecimal v){dailyRateSnapshot=v;}
    public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
    public BigDecimal getDepositAmount(){return depositAmount;} public void setDepositAmount(BigDecimal v){depositAmount=v;}
    public BookingStatus getStatus(){return status;} public void setStatus(BookingStatus v){status=v;updatedAt=Instant.now();}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public UUID getCreatedBy(){return createdBy;} public void setCreatedBy(UUID v){createdBy=v;}
}
