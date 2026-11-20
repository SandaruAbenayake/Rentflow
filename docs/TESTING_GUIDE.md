# Testing Guide

```bash
mvn test
cd frontend && npm test
cd frontend && npm run build
docker compose config --quiet
```

The rental repository test proves overlap rejection and adjacent interval behavior. Expand Testcontainers coverage for MySQL, RabbitMQ, cross-tenant authorization and concurrent booking submission before production.
