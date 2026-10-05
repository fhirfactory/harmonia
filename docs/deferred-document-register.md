# Harmonia Deferred Document Register

This register records documentation tasks, wording adjustments, and architectural refinements that have been identified during architecture reviews but deferred to designated downstream passes or domains.

The register ensures governance traceability, prevents premature implementation during earlier passes, and provides an authoritative backlog of deferred documentation items across Harmonia documentation domains.

---

## 1. Active Deferred Items

*(No active deferred items for Domain 02 Strategy. All identified items resolved in Pass C).*

---

## 2. Resolved Items

| Item # | Item Description | Arose From | Resolved In | Status | Canonical Files Modified & Resolution Summary |
|---|---|---|---|---|---|
| **01** | Remove residual implementation-prescriptive wording such as `cryptographic`, `tamper-proof`, and `immutable` from Business Enabling Features where the required behaviour can be stated independently of mechanism. | Strategy Pass A.1 review | Domain 02 Strategy Pass C (2026-10-06) | **Resolved** | Modified `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` (features `FEAT-EM-05`, `FEAT-SA-16`, `FEAT-SA-29`, `FEAT-ISE-03`, `FEAT-ISE-12`, `FEAT-ISE-13`) and `enterprise-capabilities.md` (`EC-02`, `EC-07`). Replaced mechanism-specific terms with durable architectural qualities (`enduring`, `content integrity`, `tamper-evident`, `verifiable audit records`, `attributable security context`). |
| **02** | Replace wording describing Harmonia as providing the “authoritative historical clinical record” with wording reflecting Harmonia's governed, vendor-neutral longitudinal clinical representation and preservation responsibilities. | Strategy Pass A.1 review | Domain 02 Strategy Pass C (2026-10-06) | **Resolved** | Modified `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` (`FEAT-ISE-03`). Explicitly established that Harmonia provides "governed, vendor-neutral longitudinal clinical representation and preservation with verifiable integrity controls" without claiming originating clinical authority for source medical assertions. |
| **03** | Review Business Capability “Harmonia Enabling Role” statements and remove residual solution-mechanism wording so they describe strategic enablement rather than premature solution design. | Strategy Pass A.1 review | Domain 02 Strategy Pass C (2026-10-06) | **Resolved** | Modified `docs/markdown/02-strategy/capabilities/business-capabilities.md` (Capabilities 03, 04, 06, 08, 12 and summary table). Removed solution-specific messaging protocols (HL7 v2 ORU/ORM, bulk FHIR API) and prescriptive terms (immutable audit, deterministic identity resolution), replacing them with strategic enablement and correlation descriptions. |

---

## 3. Maintenance & Lifecycle

- **Deferred**: Identified during review; intentionally held for a future authoring pass or domain.
- **In Progress**: Actively being addressed within the target pass.
- **Resolved**: Reconciled and verified against applicable Architectural Axioms and quality rules.
- **Superseded**: Rendered obsolete by an accepted Architecture Decision Record (ADR) or axiom update.
