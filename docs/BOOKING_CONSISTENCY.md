# Booking Consistency

Booking intervals use the half-open convention `[startAt, endAt)`. Adjacent intervals do not overlap.

The Rental Service serializes requests for each tenant/vehicle pair using a pessimistic database lock. It then rejects any active booking whose interval overlaps the request. This protects the first concurrent request and subsequent concurrent requests.

Fleet availability is not used as the sole consistency mechanism. Booking ownership remains in Rental Service and availability events are eventual synchronization only.
