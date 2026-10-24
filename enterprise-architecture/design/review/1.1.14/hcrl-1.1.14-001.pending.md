<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Data Governance Services architecture / code reconciliation & architectural conflict exposure | Not recorded | Reconciliation completed; DI-1 disposed; remaining findings unresolved and carried forward to iteration [002](hcrl-1.1.14-002.pending.md) for substantive architecture development; advisory recommendations NOT adopted. |
| [DR-2](#dr-2) | Bounded supporting Minutiae, provenance limitations & repository entry state | Accept | Supporting minutiae accepted as an adequate and truthful record of the reconciliation and housekeeping provenance. This acceptance does not constitute acceptance or adjudication of any DR-1 architectural finding, disposition item, conforming path, or advisory recommendation. |
| [DR-3](#dr-3) | Architecture Governance finite-provenance lifecycle & recursion elimination | Accept | Accepted finite-provenance governing principle, substantive activity vs provenance maintenance distinction, conditional review lifecycle, and non-recursive maintenance rules. |

# HCRL — 1.1.13 → 1.1.14: Data Governance Services architecture / code reconciliation

**Standing.** Completed review iteration **001** for **RAD-1.1.13 → RAD-1.1.14**. Mark Hunter, Solution Architect,
issued the operative Data Governance Services architecture/code reconciliation task in session `session-261023-191640-1n0i`,
retained in the [formal reconciliation Task][reconciliation-task]. The reconciliation activity examined the relationship
between the canonical Architecture Continuum baseline RAD-1.1.13 and the substantial existing Java implementation
across Mneme, Calliope, Mnemosyne, and Pylai, producing the [reconciliation Report][reconciliation-report].
The subsequent governance housekeeping task in session `session-261023-205129-62l6`, retained in the
[housekeeping Task][housekeeping-task], regularised this activity into the Harmonia Architecture Review lifecycle under
RAD-1.1.14 iteration 001 and produced the [housekeeping Report][housekeeping-report].
Mark Hunter, Solution Architect, rendered disposition on [DI-1](#di-1) ([Task][di-1-task] | [Report][di-1-report]),
accepted [DR-2](#dr-2) (supporting minutiae and repository entry state), and accepted [DR-3](#dr-3)
(finite-provenance governance framework repair, evidenced in the [governance-repair Report][governance-repair-report]).
The remaining architectural reconciliation findings in DR-1 (DI-2 through DI-9) are intentionally not adjudicated in this
iteration and are carried forward into iteration [002](hcrl-1.1.14-002.pending.md) for substantive architecture development
and review through Design Review Proposals (beginning with the Information Unit Framework DRP). Iteration 001 is closed as
the completed historical record for this initial review boundary; closing iteration 001 closes this review iteration,
not the unresolved architectural matters in DR-1. Root release authority records `1.1.13` (`architecture-release.json`);
this review iteration consumes no release allocation.

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

   **DI-1 — Solution Architect Disposition**

   Disposition: Historical authority conflict; no current architectural conflict.

   The Convergence Plan and associated historical implementation/design material are discovery evidence only and do not constitute current architectural authority.

   Status statements, milestone completion claims, and ADR acceptance claims contained within that historical material do not establish current architectural standing. The governed Architecture Continuum and governed Decision Register remain authoritative.

   No Architecture Continuum change is required as a consequence of DI-1.

   Implementation derived from, or associated with, the historical material remains legitimate implementation evidence and may require separate reconciliation where that implementation behaviour is encountered.

   *Attribution & Evidence*: Disposed by Mark Hunter, Solution Architect (Task-Issued-At: 2026-10-23T11:37:23.387Z, session `session-261023-223648-fp6j`); recorded and reported by Junie co-executor agent. Formal evidence: [Task][di-1-task] | [Report][di-1-report].
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

**Required Solution Architect Adjudication & Iteration 001 Review Closure.**
Mark Hunter, Solution Architect, rendered disposition on DI-1, while DR-2 and DR-3 were accepted within this boundary.
The remaining architectural discrepancies and invariant observations (DI-2 through DI-9) remain intentionally unresolved
in this iteration because their resolution requires substantive architecture development through Design Review Proposals
(DRPs)—beginning with the Information Unit Framework DRP and subsequently the Authentication & Authorisation Framework,
Audit Framework, and Mneme-to-Mnemosyne governed interaction.
With this review boundary completed, those unresolved findings are carried forward into iteration [002](hcrl-1.1.14-002.pending.md)
for adjudication under those respective DRPs without being treated as accepted, rejected, resolved, or discarded.
Explicit Solution Architect disposition on those substantive matters will be rendered under the subsequent iterations.

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
Minutiae-Accepted: Yes
Comment: Supporting minutiae accepted as an adequate and truthful record of the reconciliation and housekeeping provenance. This acceptance does not constitute acceptance or adjudication of any DR-1 architectural finding, disposition item, conforming path, or advisory recommendation.
```

This control and DR-2's opening-table entry describe the same decision. Silence,
agent classification, preparation of this pending HCRL, and successful tooling
supply no acceptance.

<a id="dr-3"></a>
## DR-3 — Architecture Governance finite-provenance lifecycle & recursion elimination

**Background & Governance Problem.** Operational experience during recent Architecture Review activity (most notably DI-1 disposition reporting) exposed an unintended recursive pathology in Harmonia's Architecture Governance provenance framework. Because producing, validating, correcting, or recording a Task, Report, HCRL entry, timestamp, or validation result was capable of being interpreted as a new governed change, agents were trapped in self-documenting amendment cycles that generated unbounded administrative overhead and paralyzed execution progress. Governance processes must terminate cleanly rather than recursively documenting their own mechanical documentation.

**Governing Principle.** The fundamental governing principle formalized and adopted across Harmonia architecture contracts (`architecture-change-provenance.md`, `architecture-review-praxis.md`, `architecture-release-policy.md`, `architecture-governance.md`, and `AGENTS.md` §1.4) is:

> **Provenance records describe the governed activity, not the mechanical production history of the provenance records themselves.**

**Semantic Distinction: Substantive Governed Activity vs Provenance Maintenance.**
The framework explicitly distinguishes:
1. *Substantive Governed Activity*: Activity that analyses, changes, reconciles, proposes, or validates architectural or solution content in a manner requiring governed evidence under Architecture Review.
2. *Provenance Maintenance*: Mechanical creation, completion, cross-referencing, formatting, timestamp synchronization, or verification of the Task, Report, HCRL, and related governance records required to document an already-authorised substantive activity within an established provenance boundary.

**Finite Governance Progression & Conditional Architecture Review Lifecycle.**
The governance lifecycle follows a finite, bounded progression that terminates cleanly without recursive loops:

```text
Authorised Task
    ↓
Substantive Activity
    ↓
Report
    ↓
Architecture Review required?
    ├── No
    │     ↓
    │  TERMINAL
    │
    └── Yes
          ↓
      Architecture Review
          ↓
      Solution Architect Disposition
          ↓
      HCRL Completion
          ↓
      Resulting Git State
          ↓
      TERMINAL
```

Where mechanical Git state is established or checked for an activity terminating at Report, that verification remains provenance maintenance and does not extend the lifecycle. A Git commit is not required where the governed activity legitimately produces no repository change beyond its Report/evidence.

**Provenance Maintenance Rules & Release Allocation.**
- Provenance maintenance performed within an authorized provenance boundary SHALL NOT recursively instantiate a new Task, Report, HCRL iteration, or Architecture Release increment.
- Post-emission verification checks, Git status inspections, and mechanical synchronizations terminate at Git state without requiring recursive Report amendment passes.
- Pending review iterations and mechanical governance maintenance consume zero release allocation under Architecture Release Policy; release increments apply only to accepted substantive architectural and solution change sets.

**Fail-Closed Governance Invariant (AX-17).**
If a new material architectural issue, invariant conflict, or unintentional code modification is discovered during provenance maintenance, execution on the affected matter MUST halt immediately and surface the finding through HCRL for Solution Architect adjudication under fail-closed governance (AX-17 and `AGENTS.md` §1.5). Provenance maintenance cannot be used to bypass governance or conceal substantive architectural discoveries.

**Solution Architect Disposition — Accept.**
Mark Hunter, Solution Architect, explicitly accepts the finite-provenance governance-framework repair, the governing principle, the distinction between substantive activity and provenance maintenance, the conditional Architecture Review lifecycle, and the non-recursive maintenance rules as normative repository governance.

*Attribution & Supporting Evidence*: Formally analysed and evidenced in the governance-repair [Activity Report][governance-repair-report] (`.junie/reports/2026-10-24-architecture-governance-provenance-simplification-report.md`). This HCRL update completes the accepted governance decision within the existing provenance boundary and does not instantiate a separate Task or Report.

[reconciliation-task]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T081653Z-data-governance-reconciliation.md
[reconciliation-report]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T094001Z-data-governance-reconciliation.md
[housekeeping-task]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T095350Z-data-governance-reconciliation-regularisation.md
[housekeeping-report]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T102853Z-data-governance-reconciliation-regularisation.md
[di-1-task]: ../../evidence/1.1.14/001/before/ai-agent-tasks/20261023T113723Z-di-1-solution-architect-disposition.md
[di-1-report]: ../../evidence/1.1.14/001/after/ai-agent-reports/20261023T115132Z-di-1-solution-architect-disposition.md
[governance-repair-report]: ../../../../.junie/reports/2026-10-24-architecture-governance-provenance-simplification-report.md
[iteration-002]: hcrl-1.1.14-002.pending.md
