package com.rentflow.rental;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="customers")
public class Customer {
    @Id @Column(columnDefinition="char(36)") private UUID id;
    @Column(name="company_id",columnDefinition="char(36)",nullable=false) private UUID companyId;
    @Column(name="full_name",nullable=false) private String fullName;
    private String email; @Column(nullable=false) private String phone; private String address;
    @Column(name="identification_type") private String identificationType;
    @Column(name="identification_number") private String identificationNumber;
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
    public UUID getId(){return id;} public void setId(UUID v){id=v;}
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID v){companyId=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getIdentificationType(){return identificationType;} public void setIdentificationType(String v){identificationType=v;}
    public String getIdentificationNumber(){return identificationNumber;} public void setIdentificationNumber(String v){identificationNumber=v;}
}
