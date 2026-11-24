---
applyTo:
  - "common-events/**"
  - "**/event/**"
  - "**/events/**"
  - "**/kafka/**"
---

# Kafka & Common Events Guidelines

- Keep event schemas backward-compatible when evolving existing Kafka events.
- Always include unique event IDs and timestamps (e.g., `Instant` / `OffsetDateTime` in UTC) on domain events.
- Design consumers to be idempotent so duplicate message delivery does not cause inconsistent state.
- Account for compensating transactions and saga flows (e.g., releasing or resetting room availability if a reservation fails or is cancelled).
- Keep event payloads focused on domain facts rather than internal database entity details.