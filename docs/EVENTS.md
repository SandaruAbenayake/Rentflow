# RabbitMQ Events

The planned exchange uses stable versioned event contracts with `eventId`, `eventType`, `eventVersion`, `occurredAt`, `correlationId`, `aggregateId` and `companyId`.

Examples: `CompanyCreated.v1`, `CompanyProvisioningRequested.v1`, `UserCreated.v1`, `VehicleAvailabilityChanged.v1`, `BookingConfirmed.v1`, `PaymentRecorded.v1` and `TripCompleted.v1`.

Publishers use event IDs for deduplication. Consumers persist inbox event IDs before side effects. Retryable failures return to the retry exchange; exhausted messages route to a dead-letter queue.
