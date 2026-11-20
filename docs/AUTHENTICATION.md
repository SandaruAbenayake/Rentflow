# Authentication

Users authenticate with email and password. Passwords are hashed with BCrypt cost 12. Access tokens expire after 15 minutes and contain user ID, role, issuer, audience and trusted company membership.

Refresh tokens are random 48-byte values stored only as SHA-256 hashes. Refresh rotates the token and revokes the consumed token. Logout revokes the supplied refresh token.

The local bootstrap super-admin is supplied through environment variables. Browser refresh tokens must be moved to HttpOnly cookies before production deployment.
