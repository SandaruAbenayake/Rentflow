package com.rentflow.rental;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
@Entity @Table(name="booking_locks")
@IdClass(BookingLock.Key.class)
public class BookingLock {
    @Id @Column(name="company_id",columnDefinition="char(36)") private UUID companyId;
    @Id @Column(name="vehicle_id",columnDefinition="char(36)") private UUID vehicleId;
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID v){companyId=v;}
    public UUID getVehicleId(){return vehicleId;} public void setVehicleId(UUID v){vehicleId=v;}
    public static class Key implements Serializable {
        public UUID companyId; public UUID vehicleId;
        public Key(){}
        public Key(UUID companyId,UUID vehicleId){this.companyId=companyId;this.vehicleId=vehicleId;}
        public boolean equals(Object o){if(!(o instanceof Key k))return false;return companyId.equals(k.companyId)&&vehicleId.equals(k.vehicleId);}
        public int hashCode(){return java.util.Objects.hash(companyId,vehicleId);}
    }
}
