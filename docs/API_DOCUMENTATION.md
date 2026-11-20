# API Documentation

All public traffic is routed through `http://localhost:8080`. Responses use JSON and errors use the centralized error shape.

| Area | Routes |
|---|---|
| Authentication | `POST /api/v1/auth/login`, `POST /api/v1/auth/refresh`, `POST /api/v1/auth/logout` |
| Companies | `GET/POST /api/v1/companies`, `PATCH /api/v1/companies/{id}/status` |
| Vehicles | `GET/POST/PUT/DELETE /api/v1/vehicles`, `GET/POST /api/v1/drivers` |
| Rentals | `GET/POST /api/v1/customers`, `GET/POST /api/v1/bookings`, `PATCH /api/v1/bookings/{id}/status` |
| Finance | `GET/POST /api/v1/payments` |
| Trips | `GET/POST /api/v1/trips` |

Protected routes require `Authorization: Bearer <access-token>`. Company-scoped routes derive the company ID from validated JWT claims and never accept a client company ID.
