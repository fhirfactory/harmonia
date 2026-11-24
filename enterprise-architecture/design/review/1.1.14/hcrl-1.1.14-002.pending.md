<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Data Governance Services architecture / code reconciliation & unresolved architectural findings | Rejected — Review approach no longer conforms to the adopted implementation strategy. Findings remain discovery evidence; resolving them individually against existing implementation would prematurely constrain solution design and invert the architecture-to-solution relationship. | Review approach rejected; findings DI-2 through DI-9 retained as discovery evidence without mechanical forward carriage into a new HCRL. Post-result confirmation of incorporated 1.1.14 release products pending Solution Architect review. |

# HCRL — 1.1.13 → 1.1.14: Data Governance Services architecture development & reconciliation

**Standing.** Pending iteration **002** for **RAD-1.1.13 → RAD-1.1.14**. This iteration records the Solution Architect's
disposition on the carried-forward DR-1 review approach while retaining findings DI-2 through DI-9 as discovery evidence.
Mark Hunter, Solution Architect, accepted [DR-2 (supporting minutiae)](hcrl-1.1.14-001.pending.md#dr-2) and
[DR-3 (finite-provenance governance framework repair)](hcrl-1.1.14-001.pending.md#dr-3) in iteration 001, and rendered
disposition on [DI-1 (Convergence Plan historical status)](hcrl-1.1.14-001.pending.md#di-1). Those accepted decisions remain
preserved as completed history in iteration 001 and are referenced rather than re-adjudicated or duplicated here.
Mark Hunter, Solution Architect, rejected the 1.1.14-002 review approach for DR-1. Post-result confirmation of the
incorporated 1.1.14 release products remains pending final Solution Architect inspection before completion and registration.

**Review basis & predecessor.** The immediate governed predecessor release is 1.1.13, whose
[completed HCRL](../../register/1.1.13/hcrl-1.1.13-002.complete.md) establishes the accepted 1.1.13 canonical architecture
and workbench projection. Entry tracked state for this review stream was commit `ff141340fdb0ab14ca46401bef5a7058138b7aad`
on branch `Data-Governence-Services`. Root release authority records `1.1.13` (`architecture-release.json`); pending
iterations allocate no release increment. The common Architecture Governance, Architecture Review Praxis, Architecture
Change Provenance, and Architecture Release Policy govern this review.

The Architecture Continuum remains canonical authority. AX-01 through AX-17 govern architectural interpretation.
In accordance with fail-closed governance principles, existing implementation is not, by itself, evidence of architectural
intent, and discovered conflicts must be reported rather than silently resolved. Zero production Java source files,
Architecture Continuum elements, ArchiMate published models, or completed historical HCRLs have been modified.

<a id="dr-1"></a>
## DR-1 — Data Governance Services architecture / code reconciliation & unresolved architectural findings

**Background & Activity Context.** The Code Round-Trip reconciliation activity was commissioned to establish a reliable
baseline for Harmonia Data Governance Services development by reconciling canonical Architecture Continuum responsibilities
against persistent implementation concepts in the Java codebase. The investigation covered:
- **Mneme**: Application-facing managed-information access, distributed active state, observation, and coordination (Infinispan).
- **Calliope**: Domain models, schemas, and converters (Calliope-Models library) versus runtime managed dataset authority (Calliope-DataSets).
- **Mnemosyne**: Authoritative durable managed-information state, state progression, and audit persistence (PostgreSQL / JPA / HAPI FHIR).
- **Pylai**: Interoperability ingress/egress boundaries, protocol gateways (MLLP / FHIR REST), and payload transformation.

Formal originating evidence is preserved under iteration 001: [Reconciliation Task][reconciliation-task-001] |
[Reconciliation Report][reconciliation-report-001] | [Housekeeping Task][housekeeping-task-001] |
[Housekeeping Report][housekeeping-report-001].

**Reported Architectural Findings & Status:**
1. *DI-1: Convergence Plan Status & Authority Contradiction (Authority Gap & Plan Inconsistency)*:
   Disposed in iteration 001 by Mark Hunter, Solution Architect: Historical authority conflict; no current architectural
   conflict. The Convergence Plan and associated historical implementation/design material are discovery evidence only
   and do not constitute current architectural authority. Status statements, milestone completion claims, and ADR
   acceptance claims contained within that historical material do not establish current architectural standing. The
   governed Architecture Continuum and governed Decision Register remain authoritative. Formal evidence:
   [DI-1 Task][di-1-task-001] | [DI-1 Report][di-1-report-001].
2. *DI-2: Mnemosyne Persistence Adapter Dual-Implementation (Authority Gap & Candidate Gap)*:
   Production code uses `@Primary` `AuthoritativePersistenceService` (custom Spring Data repository) while
   `HapiJpaAuthoritativePersistenceAdapter` is marked experimental, without authoritative Decision Register entries for ADR-021/022.
3. *DI-3: Absent Entity Read Result Contract (404 vs 500) (Contract Inconsistency & Test Failure)*:
   Divergence between `AuthoritativePersistenceService.read()` returning `NotCommitted` (mapped to HTTP 500, causing
   integration test failure) versus expectations of `Absent` (HTTP 404), noting contrary port Javadoc and separate UPDATE result mappings.
4. *DI-4: Mneme Four-Service Surface vs C11 Point Contracts (Candidate Gap & Unresolved Mapping)*:
   Calliope C11 contracts provide atomic point operations (`GovernedReader`/`GovernedWriter`), whereas Continuum candidate
   assertions (F16/M05) specify a four-service surface (Context Load/Save, Adhoc Access/Search) with composite schemas deferred (CDG-31/45/47).
5. *DI-5: Inbound Ingress Exception Handling (Observed Invariant Violation — Invariant 4 / REC-001)*:
   `DefaultCommunicationService` catches and swallows remote cache write exceptions with `log.warn`, resulting in
   `IncomingAdtMessageProcessor` emitting an `AA` (Application Accept) ACK rather than an `AE` NACK upon cache failure.
6. *DI-6: Outbound Egress Exception Handling (Observed Invariant Violation — Invariant 5 / REC-002)*:
   Five catch-and-log blocks in `OutboundStateLifecycleManager` swallow exceptions during lifecycle initialization,
   Task/Provenance updates, and Petasos event publishing.
7. *DI-7: Legacy `FhirRestCacheStore` & Soft-Delete Cascade (Uncertain Runtime & Candidate Conflict — ADR-020 / Invariant 8)*:
   Synchronous write-through cache store configuration where cache removal invokes REST DELETE reaching `@Delete`
   providers and generic soft-delete, with runtime reachability uncertain and posing a bounded question regarding its planned M4.3 retirement.
8. *DI-8: Pylai FHIR Registry Direct Persistence Coupling (Source-Level Boundary Conflict — Invariant 8.1 / Invariant 9)*:
   `pylai-fhir-registry` POM imports `mnemosyne-clinical` and invokes `FhirStorageService` directly in-process, currently
   existing as an undeployed library jar.
9. *DI-9: Persistence Security Context & Default-Deny Authorization (Observed Invariant Violation — Invariant 6 / AX-07)*:
   `FhirStorageService` defaults a `null` security principal to `service:mnemosyne` with self-granted permissions on internal storage paths.

**Solution Architect Disposition (DR-1):**

> **Rejected — Review approach no longer conforms to the adopted implementation strategy. Findings remain discovery evidence; resolving them individually against existing implementation would prematurely constrain solution design and invert the architecture-to-solution relationship.**

**Interpretation and Discovery Evidence Retention:**
- The Solution Architect's rejection applies strictly to the **review/resolution approach** (attempting to resolve the findings individually against existing implementation), not to the factual existence of the discoveries recorded as DI-2 through DI-9.
- Findings **DI-2 through DI-9 remain retained as discovery evidence** within this review record. In accordance with the Solution Architect's directive, they **SHALL NOT** be mechanically carried forward as unresolved architectural obligations into a new HCRL.
- Resolving discoveries individually against current implementation would prematurely constrain solution design and invert the governed architecture-to-solution relationship (AX-08, AX-14, AX-17). Existing implementation is not, by itself, evidence of architectural intent.

**Adopted Forward Strategy: Architecture-Led Solution Development via DRPs:**
The adopted forward direction is architecture-led solution development using Design Review Proposals (DRPs). Each DRP will identify the Feature or Features it realises and will develop the required Motivation, Strategy, Business, and Application architecture before implementation and reconciliation proceed:
1. **Information Unit Framework DRP**: Addressing canonical resource representations, composite versus atomic point operations (DI-4),
   absent entity read contracts (DI-3), and persistence adapter alignment (DI-2).
2. **Authentication & Authorisation Framework DRP**: Addressing security principal propagation, default-deny security evaluation,
   and permission granting across storage and service layers (DI-9).
3. **Audit Framework DRP**: Addressing durable audit persistence, provenance tracking, and exception evidence (DI-6).
4. **Mneme-to-Mnemosyne Governed Interaction DRP**: Addressing active distributed state versus durable truth, write-through caching,
   soft-delete cascades (DI-7), direct persistence coupling (DI-8), and ingress/egress dual-write safety (DI-5, DI-6).

The discovery evidence represented by DI-2 through DI-9 informs future solution architecture but does not constrain it merely because it was discovered in historical or current implementation.

**Preserved Decisions from Iteration 001:**
Iteration 001 constitutes a completed historical review boundary. For full historical records and evidence, see:
- [DR-1 DI-1 Disposition](hcrl-1.1.14-001.pending.md#di-1): Historical authority conflict; no current architectural conflict; Convergence Plan is discovery evidence only.
- [DR-2 Supporting Minutiae & Entry State](hcrl-1.1.14-001.pending.md#dr-2): Accepted by Solution Architect.
- [DR-3 Finite-Provenance Governance Lifecycle](hcrl-1.1.14-001.pending.md#dr-3): Accepted by Solution Architect (governance framework repair establishing finite provenance lifecycle).
- [Governance-Repair Activity Report][governance-repair-report]

**Post-Result Confirmation Pending:**
This iteration prepares and verifies the 1.1.14 architecture release baseline and its derived ArchiMate Open Exchange and documentation publications in the working tree. Final Solution Architect post-result confirmation of the incorporated release products remains pending human inspection before registration of `hcrl-1.1.14-002.complete.md`.

[reconciliation-task-001]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T081653Z-data-governance-reconciliation.md
[reconciliation-report-001]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T094001Z-data-governance-reconciliation.md
[housekeeping-task-001]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T095350Z-data-governance-reconciliation-regularisation.md
[housekeeping-report-001]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T102853Z-data-governance-reconciliation-regularisation.md
[di-1-task-001]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T113723Z-di-1-solution-architect-disposition.md
[di-1-report-001]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T115132Z-di-1-solution-architect-disposition.md
[closure-task-002]: ../../evidence/1.1.14/002/before/ai-agent-tasks/20261025T061846Z-close-1.1.14-002-and-publish-1.1.14.md
[closure-report-002]: ../../evidence/1.1.14/002/after/ai-agent-reports/20261025T063900Z-close-1.1.14-002-and-publish-1.1.14.md
[governance-repair-report]: ../../../../.junie/reports/2026-10-24-architecture-governance-provenance-simplification-report.md
[iteration-001]: hcrl-1.1.14-001.pending.md
