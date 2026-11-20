package com.rentflow.rental;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface BookingLockRepository extends JpaRepository<BookingLock,BookingLock.Key> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from BookingLock l where l.companyId=:companyId and l.vehicleId=:vehicleId")
    Optional<BookingLock> findForUpdate(@Param("companyId") UUID companyId,@Param("vehicleId") UUID vehicleId);
}
