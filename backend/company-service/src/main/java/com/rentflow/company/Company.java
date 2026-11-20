package com.rentflow.company;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class Company {
    @Id @Column(columnDefinition = "char(36)") private UUID id;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String code;
    private String email;
    private String phone;
    private String address;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private CompanyStatus status;
    @Column(name="created_at", nullable=false) private Instant createdAt = Instant.now();
    @Column(name="updated_at", nullable=false) private Instant updatedAt = Instant.now();

    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getCode(){return code;} public void setCode(String code){this.code=code;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    public String getAddress(){return address;} public void setAddress(String address){this.address=address;}
    public CompanyStatus getStatus(){return status;} public void setStatus(CompanyStatus status){this.status=status;this.updatedAt=Instant.now();}
}
