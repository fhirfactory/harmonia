# Harmonia Task 9 Backlog

## Mneme

### BL-09-01 — Contain Mneme authoritative-version provenance at the cache/read boundary.

Ensure Harmonia's Mneme authoritative-version metadata is treated as internal active-state/cache-management provenance. It must not be persisted back into Mnemosyne as clinical content or exposed through external FHIR interfaces unless explicitly required by an interoperability contract.