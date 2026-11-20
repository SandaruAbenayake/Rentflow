# Company Provisioning Saga

1. Super Admin creates a pending company.
2. Company Service persists `ProvisioningSaga` and an idempotency key.
3. Identity provisioning creates the initial Company Admin.
4. Company Service activates the company only after Identity acknowledges success.
5. Failure schedules retry with a bounded attempt count.
6. Permanent failure compensates by leaving the company inactive and records the terminal saga state.

No company becomes active while its required initial administrator provisioning is incomplete.
