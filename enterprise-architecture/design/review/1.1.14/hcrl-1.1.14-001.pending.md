<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Data Governance Services architecture / code reconciliation & architectural conflict exposure | Not recorded | Reconciliation completed; advisory recommendations NOT adopted; Solution Architect disposition required. |
| [DR-2](#dr-2) | Bounded supporting Minutiae, provenance limitations & repository entry state | Not recorded | Provenance limitations, auto-commit risk, and uncommitted governance entry state recorded for treatment before closure. |

# HCRL — 1.1.13 → 1.1.14: Data Governance Services architecture / code reconciliation

**Pending.** Mark Hunter, Solution Architect, issued the operative Data Governance
Services architecture/code reconciliation task in session `session-261023-191640-1n0i`,
retained in the [formal reconciliation Task][reconciliation-task]. The reconciliation
activity examined the relationship between the canonical Architecture Continuum baseline
RAD-1.1.13 and the substantial existing Java implementation across Mneme, Calliope,
Mnemosyne, and Pylai, producing the [reconciliation Report][reconciliation-report].
The subsequent governance housekeeping task in session `session-261023-205129-62l6`,
retained in the [housekeeping Task][housekeeping-task], regularised this activity into the
Harmonia Architecture Review lifecycle under RAD-1.1.14 iteration 001 and produced the
[housekeeping Report][housekeeping-report]. This pending HCRL exposes the discovered
architectural conflicts for Solution Architect adjudication. It does not infer
acceptance, adopt advisory recommendations, or modify canonical architecture or
production code.

**Review basis.** The immediate governed predecessor release is 1.1.13, whose
[completed HCRL](../../register/1.1.13/hcrl-1.1.13-002.complete.md) establishes the
accepted 1.1.13 canonical architecture and workbench projection. Entry tracked state
for this housekeeping activity was commit `ff141340fdb0ab14ca46401bef5a7058138b7aad`
on branch `Data-Governence-Services`. Mark Hunter, Solution Architect, issued the
operative reconciliation and regularisation tasks; Junie agent prepared and recorded
this review basis in session `session-261023-205129-62l6` (2026-10-23). Root release
authority records `1.1.13` (`architecture-release.json`); pending iterations allocate
no release increment. The common Architecture Governance, Architecture Review Praxis,
Architecture Change Provenance, and Architecture Release Policy govern this review.

The Architecture Continuum remains canonical authority. AX-01 through AX-17 govern
architectural interpretation. In accordance with fail-closed governance principles,
existing implementation is not, by itself, evidence of architectural intent, and
discovered conflicts must be reported rather than silently resolved. Zero production
Java source files, Architecture Continuum elements, ArchiMate published models, or
completed historical HCRLs have been modified.

<a id="dr-1"></a>
## DR-1 — Data Governance Services architecture / code reconciliation & architectural conflict exposure

**Background & Activity Context.** The Code Round-Trip reconciliation activity was
commissioned to establish a reliable baseline for Harmonia Data Governance Services
development by reconciling canonical Architecture Continuum responsibilities against
persistent implementation concepts in the Java codebase. The investigation covered:
- **Mneme**: Application-facing managed-information access, distributed active state,
  observation, and coordination (Infinispan).
- **Calliope**: Domain models, schemas, and converters (Calliope-Models library) versus
  runtime managed dataset authority (Calliope-DataSets).
- **Mnemosyne**: Authoritative durable managed-information state, state progression,
  and audit persistence (PostgreSQL / JPA / HAPI FHIR).
- **Pylai**: Interoperability ingress/egress boundaries, protocol gateways (MLLP / FHIR
  REST), and payload transformation.

**Reported Architectural Discrepancies & Invariant Observations.** The reconciliation
report formulated 9 structured Disposition Items exposing material structural
discrepancies, authority gaps, and invariant tensions between existing implementation
lineage and the canonical Architecture Continuum baseline. In accordance with neutral
reporting constraints, these findings are reported neutrally as historical observations
rather than confirmed invariant violations or adjudicated decisions:
1. *DI-1: Convergence Plan Status & Authority Contradiction (Authority Gap & Plan Inconsistency)*:
   The report records an internal contradiction between the master plan's summary status
   ("Programme status: CONVERGENCE COMPLETE"), claimed milestone completion (M2.3 / M2.4),
   and active sequence indications (M2.3 next), alongside references claiming ADR-021/022 are
   ACCEPTED while they are absent from the Decision Register.
2. *DI-2: Mnemosyne Persistence Adapter Dual-Implementation (Authority Gap & Candidate Gap)*:
   The report observes that production code uses `@Primary` `AuthoritativePersistenceService`
   (custom Spring Data repository) while `HapiJpaAuthoritativePersistenceAdapter` is marked
   experimental, without authoritative Decision Register entries for ADR-021/022.
3. *DI-3: Absent Entity Read Result Contract (404 vs 500) (Contract Inconsistency & Test Failure)*:
   The report observes divergence between `AuthoritativePersistenceService.read()` returning
   `NotCommitted` (mapped to HTTP 500, causing integration test failure) versus expectations
   of `Absent` (HTTP 404), noting contrary port Javadoc and separate UPDATE result mappings.
4. *DI-4: Mneme Four-Service Surface vs C11 Point Contracts (Candidate Gap & Unresolved Mapping)*:
   The report observes that Calliope C11 contracts provide atomic point operations
   (`GovernedReader`/`GovernedWriter`), whereas Continuum candidate assertions (F16/M05)
   specify a four-service surface (Context Load/Save, Adhoc Access/Search) with composite
   schemas deferred (CDG-31/45/47).
5. *DI-5: Inbound Ingress Exception Handling (Observed Invariant Violation — Invariant 4 / REC-001)*:
   The report observes that `DefaultCommunicationService` catches and swallows remote cache
   write exceptions with `log.warn`, resulting in `IncomingAdtMessageProcessor` emitting an
   `AA` (Application Accept) ACK rather than an `AE` NACK upon cache failure.
6. *DI-6: Outbound Egress Exception Handling (Observed Invariant Violation — Invariant 5 / REC-002)*:
   The report observes five catch-and-log blocks in `OutboundStateLifecycleManager` that swallow
   exceptions during lifecycle initialization, Task/Provenance updates, and Petasos event publishing.
7. *DI-7: Legacy `FhirRestCacheStore` & Soft-Delete Cascade (Uncertain Runtime & Candidate Conflict — ADR-020 / Invariant 8)*:
   The report records synchronous write-through cache store configuration where cache removal
   invokes REST DELETE reaching `@Delete` providers and generic soft-delete, with runtime
   reachability uncertain and posing a bounded question regarding its planned M4.3 retirement.
8. *DI-8: Pylai FHIR Registry Direct Persistence Coupling (Source-Level Boundary Conflict — Invariant 8.1 / Invariant 9)*:
   The report observes that `pylai-fhir-registry` POM imports `mnemosyne-clinical` and invokes
   `FhirStorageService` directly in-process, currently existing as an undeployed library jar.
9. *DI-9: Persistence Security Context & Default-Deny Authorization (Observed Invariant Violation — Invariant 6 / AX-07)*:
   The report observes that `FhirStorageService` defaults a `null` security principal to
   `service:mnemosyne` with self-granted permissions on internal storage paths.

**Non-Adoption Guardrail & Neutral Invariant Reporting.** The completed reconciliation
report (`20261023T094001Z-data-governance-reconciliation.md`) formulated 9 structured
Disposition Items (including reported observations regarding Invariants 4, 5, and 6),
candidate conforming paths, and an advisory first solution build increment. In accordance
with governance guardrails:
- All 9 report Disposition Items, conforming paths, and advisory recommendations are
  referenced strictly as **supporting evidence** and are **NOT adopted** as approved
  architecture decisions.
- Reported invariant observations are documented neutrally as findings of the historical
  reconciliation pass, avoiding premature confirmation or implicit adjudication.
- Code Round-Trip cannot safely proceed by mechanically forcing implementation to match
  architecture, nor by promoting existing implementation behaviour into architecture.

**Required Solution Architect Adjudication.** Explicit Solution Architect disposition
is required to:
1. Adjudicate each of the identified architectural conflicts between existing implementation
   constructs and canonical Continuum definitions.
2. Determine which legacy implementation concepts remain intended and which canonical
   Continuum specifications govern future development.
3. Authorise any required Architecture Continuum updates or code remediation increments
   through subsequent formal HCRL iterations before implementation changes commence.

<a id="dr-2"></a>
## DR-2 — Bounded supporting Minutiae, provenance limitations & repository entry state

The disclosed supporting group comprises the formal Task and Report records, attributable
temporal provenance recovery, report line-count discrepancy disclosure, uncommitted
governance modifications, git auto-commit sweep risk disclosure, and release allocation
governance.

1. **Formal Task and Report Records**:
   - Reconciliation Task: [`20261023T081653Z-data-governance-reconciliation.md`][reconciliation-task]
     retaining verbatim prompts from session `session-261023-191640-1n0i`.
   - Reconciliation Report: [`20261023T094001Z-data-governance-reconciliation.md`][reconciliation-report]
     preserving 496 lines of byte-identical report body (SHA-256 `89649cab85317a1c31cb0ef132decbb8e1fc8672e45dc8b8c89becd682705061`).
   - Housekeeping Task: [`20261023T095350Z-data-governance-reconciliation-regularisation.md`][housekeeping-task]
     retaining verbatim prompt from session `session-261023-205129-62l6`.
   - Housekeeping Report: [`20261023T102853Z-data-governance-reconciliation-regularisation.md`][housekeeping-report]
     recording housekeeping execution, validation outcomes, and disclosures.

2. **Attributable Temporal Provenance**:
   - Reconciliation Task issuance timestamp `2026-10-23T08:16:53.959Z` recovered from
     session `events.jsonl` `UserPromptEvent.prompt` (`prompt-261023-191653-co07`).
   - Reconciliation Report production timestamp `2026-10-23T09:40:01.102Z` recovered from
     the first matching `COMPLETED` write event (`FileChangesBlockUpdatedEvent` index 10239)
     in session `events.jsonl`, representing completed emission per AGENTS.md §1.4.
   - Housekeeping Task issuance timestamp `2026-10-23T09:53:50.775Z` recovered from
     session `events.jsonl` `UserPromptEvent.prompt` (`prompt-261023-205350-2q2f`).
   - Housekeeping Report production timestamp `2026-10-23T10:28:53.734Z` recovered from
     the completed write event (`FileChangesBlockUpdatedEvent` index 4064 in session `events.jsonl`),
     establishing the governed `Report-Produced-At` and `20261023T102853Z-` filename prefix.
     An initial `10:27:23Z` figure was a pre-emission clock sample taken prior to file authoring
     and validation, regularised to the attributable write event. Subsequent file updates are
     documented as traceable amendments preserving this production timestamp per AGENTS.md §1.4.

3. **Report Line-Count Discrepancy Disclosure**:
   - The parent housekeeping task referenced a 423-line report. Event log analysis of
     session `session-261023-191640-1n0i` confirmed that 423 lines matched no historical
     write state (drafts generated 405, 492, and 494 lines). The preserved report contains
     496 lines and is byte-identical to the final completed write event.

4. **Uncommitted Governance Modifications & In-Force Ambiguity**:
   - Tracked working tree modifications appeared in `architecture-governance.md` (+29 lines,
     observed between 09:54:21Z and 09:58:53Z) and `AGENTS.md` (+33 lines, observed between
     09:59:00Z and 10:01:49Z) during this housekeeping session, after Task issuance (09:53:50Z).
   - No agent write events targeted either file; author and exact modification timestamps are
     not established from session records.
   - The modifications introduce §1.5 fail-closed governance enforcement rules and a markdown
     link in AGENTS.md referencing `architecture-governance.md#governance-enforcement-and-fail-closed-authority`.
   - The binding status of these uncommitted modifications is undetermined, pending Solution
     Architect treatment. This housekeeping activity recorded the uncertainty and maintained
     conduct consistent with §1.5 without resolving the ambiguity. In accordance with
     non-destructive boundary rules, these files have been preserved untouched without
     modification or reversion.

5. **Git Auto-Commit Sweep Risk Disclosure**:
   - Because `AGENTS.md` and `architecture-governance.md` exist as unstaged working tree
     modifications, automated git workflow mechanisms (`git_init` / auto-commit / `git_pr`)
     may sweep these changes into the housekeeping commit series. This risk is explicitly
     disclosed for Solution Architect review and treatment before closure.

6. **Release Policy Invariant**:
   - Root `architecture-release.json` remains untouched at version `1.1.13`. Pending
     review iterations consume no release allocation.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

This control and DR-2's opening-table entry describe the same decision. Silence,
agent classification, preparation of this pending HCRL, and successful tooling
supply no acceptance.

[reconciliation-task]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T081653Z-data-governance-reconciliation.md
[reconciliation-report]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T094001Z-data-governance-reconciliation.md
[housekeeping-task]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T095350Z-data-governance-reconciliation-regularisation.md
[housekeeping-report]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T102853Z-data-governance-reconciliation-regularisation.md
