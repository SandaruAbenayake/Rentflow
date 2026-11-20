# Database Design

## Logical databases

- `rentflow_identity`
- `rentflow_company`
- `rentflow_fleet`
- `rentflow_rental`

Each service uses its own datasource, credentials, Flyway migrations and schema. Foreign keys and joins across service databases are prohibited.

## Cross-service identifiers

All records use UUID primary keys. Tenant-owned records include `company_id`. Cross-service references are UUID values without database foreign keys.

## Money and time

Money values use `DECIMAL`, never floating-point types. Timestamps are stored in UTC with `TIMESTAMP` or equivalent UTC-aware application types. API timestamps use ISO-8601 UTC strings.

## Planned indexes

- Tenant and status indexes on tenant-owned entities
- Company-scoped unique registration number constraints
- Customer and vehicle lookup indexes
- Booking overlap protection indexes and transaction locking
- Event inbox/outbox correlation indexes
