package com.rentflow.rental;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BookingRepositoryTest {
    @Autowired
    private BookingRepository repository;

    @Test
    void detectsOverlappingIntervalButAllowsAdjacentEndAndStart() {
        UUID companyId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Booking existing = booking(companyId, customerId, vehicleId,
            Instant.parse("2026-11-20T10:00:00Z"), Instant.parse("2026-11-20T12:00:00Z"));
        repository.save(existing);

        assertThat(repository.findOverlapping(companyId, vehicleId,
            Instant.parse("2026-11-20T11:00:00Z"), Instant.parse("2026-11-20T13:00:00Z"))).hasSize(1);
        assertThat(repository.findOverlapping(companyId, vehicleId,
            Instant.parse("2026-11-20T12:00:00Z"), Instant.parse("2026-11-20T14:00:00Z"))).isEmpty();
    }

    private Booking booking(UUID companyId, UUID customerId, UUID vehicleId, Instant start, Instant end) {
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setCompanyId(companyId);
        booking.setCustomerId(customerId);
        booking.setVehicleId(vehicleId);
        booking.setStartAt(start);
        booking.setEndAt(end);
        booking.setDailyRateSnapshot(BigDecimal.valueOf(100));
        booking.setTotalAmount(BigDecimal.valueOf(200));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCreatedBy(UUID.randomUUID());
        return booking;
    }
}
