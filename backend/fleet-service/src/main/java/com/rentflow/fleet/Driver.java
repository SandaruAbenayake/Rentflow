package com.rentflow.fleet;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="drivers")
public class Driver {
    @Id @Column(columnDefinition="char(36)") private UUID id;
    @Column(name="company_id",columnDefinition="char(36)",nullable=false) private UUID companyId;
    @Column(name="full_name",nullable=false) private String fullName;
    @Column(nullable=false) private String phone;
    private String email;
    @Column(name="license_number",nullable=false) private String licenseNumber;
    @Column(nullable=false) private String status="AVAILABLE";
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID companyId){this.companyId=companyId;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getLicenseNumber(){return licenseNumber;} public void setLicenseNumber(String v){licenseNumber=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
