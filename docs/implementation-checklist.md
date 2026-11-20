# RentFlow Implementation Checklist

## Phase 1 - Complete

- [x] Verify existing project remains untouched
- [x] Verify target directory is absent
- [x] Create clean monorepo structure
- [x] Define proposed architecture
- [x] Create Maven and frontend module foundations
- [x] Create infrastructure and environment templates
- [ ] Architecture approval

## Phase 2 - Infrastructure foundation

- [ ] Compatible Spring dependency versions
- [ ] Docker Compose with MySQL, RabbitMQ and services
- [ ] Separate logical databases and credentials
- [ ] `.env.example`
- [ ] Persistent volumes and health checks

## Phase 3 - Identity

- [ ] Spring Security and JWT
- [ ] Login, refresh, logout
- [ ] User and role management
- [ ] Password reset workflow
- [ ] Membership validation

## Phase 4 - Company and provisioning Saga

- [ ] Company CRUD and lifecycle
- [ ] Outbox events
- [ ] Idempotent consumers
- [ ] Retry and dead-letter handling
- [ ] Provisioning recovery

## Phase 5 - Fleet

- [ ] Categories
- [ ] Vehicles
- [ ] Drivers
- [ ] Availability
- [ ] Tenant isolation tests

## Phase 6 - Rental

- [ ] Customers
- [ ] Booking validation and concurrency protection
- [ ] Payment tracking
- [ ] Trips
- [ ] Cross-service ownership validation

## Phase 7 - Gateway

- [ ] Route configuration
- [ ] JWT validation
- [ ] CORS
- [ ] Correlation IDs
- [ ] Rate limiting

## Phase 8 - Frontend

- [ ] React TypeScript application
- [ ] Authentication and protected routes
- [ ] Dashboards and role navigation
- [ ] Management workflows

## Phase 9 - Integration

- [ ] End-to-end service integration
- [ ] Saga integration
- [ ] Booking workflow

## Phase 10 - Testing

- [ ] Unit tests
- [ ] Testcontainers
- [ ] Cross-tenant tests
- [ ] Concurrent booking tests
- [ ] Event retry and idempotency tests

## Phase 11 - Deployment readiness

- [ ] Deployment guide
- [ ] Security review
- [ ] Readiness assessment
