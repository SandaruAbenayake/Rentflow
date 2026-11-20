package com.rentflow.company;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="provisioning_sagas")
public class ProvisioningSaga {
    @Id @Column(columnDefinition="char(36)") private UUID id;
    @Column(name="company_id", columnDefinition="char(36)", nullable=false) private UUID companyId;
    @Column(nullable=false) private String state;
    @Column(nullable=false) private int attempts;
    @Column(name="last_error") private String lastError;
    @Column(name="created_at", nullable=false) private Instant createdAt=Instant.now();
    @Column(name="updated_at", nullable=false) private Instant updatedAt=Instant.now();
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID companyId){this.companyId=companyId;}
    public String getState(){return state;} public void setState(String state){this.state=state;this.updatedAt=Instant.now();}
    public int getAttempts(){return attempts;} public void setAttempts(int attempts){this.attempts=attempts;}
    public String getLastError(){return lastError;} public void setLastError(String lastError){this.lastError=lastError;}
}
