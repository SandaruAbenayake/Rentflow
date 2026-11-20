# Service Boundaries

## Identity Service

Owns users, password credentials, refresh tokens, roles and company memberships. It never owns company profiles or vehicle/customer records.

## Company Service

Owns company profiles, lifecycle status and provisioning workflow state. It initiates the company provisioning saga and consumes administrative identity provisioning results.

## Fleet Service

Owns vehicles, vehicle categories and drivers. Registration-number uniqueness is scoped to one company.

## Rental Service

Owns customers, bookings, payments and trips. It validates customer and vehicle ownership using tenant context and cross-service membership information. It does not directly query another service database.

## API Gateway

Owns routing and gateway-level security controls. It does not replace service-level authorization or tenant isolation.
