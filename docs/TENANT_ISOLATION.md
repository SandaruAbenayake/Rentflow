# Tenant Isolation

Authentication establishes the user ID and role. Each business service validates the JWT issuer, audience and signature, then derives `companyId` from the `companyId` claim.

Every company-owned repository query includes `companyId`. Before update or delete, the record is fetched by both ID and tenant ID. Clients cannot supply a company ID as an authorization scope.

Cross-tenant tests must use two users and two company IDs and verify reads, updates and deletes.
