package com.rentflow.rental;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.*;
public interface BookingRepository extends JpaRepository<Booking,UUID> {
    List<Booking> findByCompanyId(UUID companyId);
    Optional<Booking> findByIdAndCompanyId(UUID id,UUID companyId);
    @Query("""
      select b from Booking b where b.companyId=:companyId and b.vehicleId=:vehicleId
      and b.status in (com.rentflow.rental.BookingStatus.PENDING,
                       com.rentflow.rental.BookingStatus.CONFIRMED,
                       com.rentflow.rental.BookingStatus.ACTIVE)
      and b.startAt < :endAt and b.endAt > :startAt
    """)
    List<Booking> findOverlapping(@Param("companyId") UUID companyId,@Param("vehicleId") UUID vehicleId,
                                  @Param("startAt") Instant startAt,@Param("endAt") Instant endAt);
}
