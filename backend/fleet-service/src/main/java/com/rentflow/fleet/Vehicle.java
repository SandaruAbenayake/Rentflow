package com.rentflow.fleet;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="vehicles")
public class Vehicle {
    @Id @Column(columnDefinition="char(36)") private UUID id;
    @Column(name="company_id",columnDefinition="char(36)",nullable=false) private UUID companyId;
    @Column(name="registration_number",nullable=false) private String registrationNumber;
    @Column(nullable=false) private String make;
    @Column(nullable=false) private String model;
    private Integer year; private String color;
    @Column(name="category_id",columnDefinition="char(36)") private UUID categoryId;
    private String transmission;
    @Column(name="fuel_type") private String fuelType;
    @Column(name="seating_capacity") private Integer seatingCapacity;
    @Column(name="daily_rate",nullable=false,precision=12,scale=2) private BigDecimal dailyRate;
    private String description;
    @Column(name="image_url") private String imageUrl;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private VehicleStatus status=VehicleStatus.AVAILABLE;
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID companyId){this.companyId=companyId;}
    public String getRegistrationNumber(){return registrationNumber;} public void setRegistrationNumber(String v){registrationNumber=v;}
    public String getMake(){return make;} public void setMake(String v){make=v;}
    public String getModel(){return model;} public void setModel(String v){model=v;}
    public Integer getYear(){return year;} public void setYear(Integer v){year=v;}
    public String getColor(){return color;} public void setColor(String v){color=v;}
    public UUID getCategoryId(){return categoryId;} public void setCategoryId(UUID v){categoryId=v;}
    public String getTransmission(){return transmission;} public void setTransmission(String v){transmission=v;}
    public String getFuelType(){return fuelType;} public void setFuelType(String v){fuelType=v;}
    public Integer getSeatingCapacity(){return seatingCapacity;} public void setSeatingCapacity(Integer v){seatingCapacity=v;}
    public BigDecimal getDailyRate(){return dailyRate;} public void setDailyRate(BigDecimal v){dailyRate=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public VehicleStatus getStatus(){return status;} public void setStatus(VehicleStatus v){status=v;updatedAt=Instant.now();}
}
