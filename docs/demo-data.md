# Local Demo Dataset

When `APP_SEED_DEMO_DATA=true`, each service idempotently adds the same fictional dataset as the previous RentFlow `DataSeeder`.

- Companies: Acme Rentals and Zenith Motors
- Users: one company admin for each company and a platform super admin
- Vehicles: ACME-001 (Toyota Corolla, 2022, 45.00/day), ACME-002 (Honda CR-V, 2023, 65.00/day), ZEN-001 (Ford Focus, 2021, 40.00/day)
- Customers: Jane Doe and John Smith using `.test` email addresses
- Rental data: one three-day confirmed Acme booking (135.00) and its pending payment

All seed user passwords are hashed with BCrypt and set via `DEMO_SEED_PASSWORD` in the ignored local `.env`. The source database volume is not mounted, queried, migrated, or modified. `UNSPECIFIED` is used as the new payment method because the old demo record did not include a payment method.
