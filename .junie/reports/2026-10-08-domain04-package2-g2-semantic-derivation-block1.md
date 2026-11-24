# Domain04 Package2 — G2 Semantic Derivation, Block 1

## Task Goal

Produce four bounded candidate Information Families for Referral, Appointment / Scheduling, Episode / Encounter and Order, derived forward from the reconciled authoritative Business Architecture. Make the derivation, information needs, relationships, authority, provenance, temporal meaning and uncertainty reviewable. This report is execution history and handover, not architectural authority or G2 approval.

## Task Activity

Read the repository instructions, central axioms and current G1 reconciliation/closure record before drafting. Inspected Domain02/03/04 inventories and the relevant Strategy catalogue, Functions, Services, Processes, Actors/Roles, Collaborations/Interactions, dependency matrix and information-responsibility matrix. Used current reconciled architecture as the semantic basis; historical G1 evidence quotations were not treated as current architecture.

Applied the Domain04 Metamodel, Foundation patterns, authority/provenance/lifecycle rules, Assemblies/Views and guardrails. Inspected all six Package1 families and reused their subject, practitioner, organisation, location and service meanings. Existing Order, Order Routing Directive, Order-Result Correlation Link and candidate Encounter/Order Context Assemblies were reused. No equivalent local relationship machinery was introduced.

Each family contains owner/Feature → Function/Service/Process → qualified interaction/collaboration context → Information Requirement → Concept → relationship derivation. Local requirement/question keys do not allocate Canonical IDs. Unresolved ancestry, unsupported Feature associations and Service dependencies remain unestablished. The convergence/runtime implementation programme is outside this conceptual task; no runtime milestone work was commenced.

Materially relevant axioms: AX-01/AX-04 semantic purpose, AX-02/AX-03 representation boundary, AX-06 authority, AX-08 evidence, AX-13 management boundary, AX-14 distinctions, AX-15 operational uncertainty and AX-17 architectural authority/absence. No axiom, upstream responsibility or frozen meaning was overridden. G1's AX-12/AX-16 register discrepancies remain unchanged and unused as derivation authority.

Session configuration: workspace tools do not expose session-launch or reasoning-effort settings, so fresh/xhigh configuration was not independently verifiable. No subagents were used.

## Target Outcome

Four G2 candidate families and a review artefact exist for architectural review. **G1 remains CLOSED; G2 is not CLOSED.** No candidate or suggestion is silently promoted into accepted architecture.

### Authoritative inputs used

- [Central Architectural Axioms](../../docs/architectural-axioms.md), especially AX-17, and repository/documentation AGENTS instructions.
- [Strategy overview](../../docs/markdown/02-strategy/README.md), capability index/taxonomy and relevant [Business Enabling Capability/Feature catalogue](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-2-service-administration); established owners and FEAT-SA-03–14 only where association is evidenced.
- [Business Architecture metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md), [Service Administration](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md), relevant [Service Delivery](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md) and [Patient Clinical Record](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md#21-patient-clinical-record) behaviour.
- [Processes](../../docs/markdown/03-business-architecture/processes/business-processes.md), [Interactions](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md), [Collaborations](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md), [Actors](../../docs/markdown/03-business-architecture/actors-roles/actors.md), [Roles](../../docs/markdown/03-business-architecture/actors-roles/roles.md), [responsibility](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) and [dependency](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md) matrices.
- [Domain04 Metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md), Foundation patterns/governance/Assemblies/Views/guardrails/traceability linked from the [overview](../../docs/markdown/04-information-architecture/README.md), all six [Package1 families](../../docs/markdown/04-information-architecture/information-families/README.md), and [G1 closure/uncertainty record](../../docs/markdown/04-information-architecture/reviews/package2-g1-review.md#16-controlled-g1-reconciliation-and-closure).

### Files created / modified

Created:

- [Referral](../../docs/markdown/04-information-architecture/information-families/referral.md).
- [Appointment / Scheduling](../../docs/markdown/04-information-architecture/information-families/appointment-scheduling.md).
- [Episode / Encounter](../../docs/markdown/04-information-architecture/information-families/episode-encounter.md).
- [Order](../../docs/markdown/04-information-architecture/information-families/order.md).
- [G2 Block 1 review](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md).
- This execution report and the [complete task-specific diff](2026-10-08-domain04-package2-g2-semantic-derivation-block1.diff).

Modified only the [Information Families index](../../docs/markdown/04-information-architecture/information-families/README.md) to add explicitly candidate navigation. Central axioms, G1, Domain01–03, Foundation and the six Package1 family bodies remain unchanged. No code, build configuration or runtime files changed. No staging or commit was performed.

### Principal candidate Concepts

| Family | Principal Concepts / compositions | Review standing |
| :--- | :--- | :--- |
| Referral | Referral; Referral Disposition; Referral Progression Event; Referral Context Assembly | 2 A; 2 B |
| Appointment / Scheduling | Appointment; Scheduling Change Event; Schedule Synchronisation State; Patient Appointment Schedule View | 2 A; 2 B |
| Episode / Encounter | Encounter; Encounter Progression Event; Encounter Care-Place Association; Encounter Context Assembly | 2 A; 2 B; independent Episode remains C/D |
| Order | Order; Order Routing Directive; Order Progression Event; Order Change Request; Order-Result Correlation Link; Order Context Assembly | 5 A; 1 B; existing Concepts/Assemblies reused where identified |

There are 18 principal candidate/reused Concepts or compositions, 11 A and 7 B. A is directly derived standing, not package approval. No new Concept is introduced merely for a relationship qualifier, status, date, participant or familiar healthcare representation.

### Important semantic relationships

Conditional/contextual relationships: Referral–DeliverableHealthcareService; Referral–Appointment in the specialist booking pathway; Appointment–Encounter for identified planned context; Order–Appointment in imaging scheduling; Order–DeliverableHealthcareService; Order–HealthcareServiceDelivery; Order–ServiceOutcome. Fulfilment/outcome associations require evidence and do not imply successful delivery.

Referral–delivery/outcome and Encounter–delivery contextual associations remain B for precise qualification/responsibility review. Direct Referral–Encounter, Referral–Order, Appointment–actual-delivery, precise Order–Encounter and Episode–Encounter grouping semantics remain unestablished. No association is filled transitively from another one. Encounter care-place association and operational readiness have distinct owners/meanings.

### Authority findings

Referral intent originates with the referring participant; triage/recipient decisions retain their own sources. Scheduler/PAS systems remain authoritative for bookings. Harmonia owns synchronisation knowledge and governs representations/contextual provision without becoming the scheduler.

Encounter Administration governs the tracking context, progression and location associations, while source administrative/clinical participants retain assertion authority. Order Administration governs intake/routing/tracking/change coordination/correlation; requesters, performers and outcome originators retain their substantive authority. Request validation, acknowledgement, review, custody and presentation confer no clinical performance responsibility or originating authority. Assemblies/Views acquire none of their constituents' originating authority.

### G2 questions and unresolved matters

The [review questions](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md#6-architectural-review-questions) are consistently numbered:

1. G2-Q01: Independent Referral Disposition boundary versus qualified progression information.
2. G2-Q02: Appointment meaning/classification and minimum source/context qualification.
3. G2-Q03: Encounter activity/context boundary versus tracking and contextual assembly.
4. G2-Q04: Whether independent Episode meaning/ownership/association is established.
5. G2-Q05: Meaning/responsibility of Referral and Encounter delivery/outcome associations.
6. G2-Q06: Inclusion and temporal boundaries of the four justified candidate compositions.
7. G2-Q07: Authority, response and established effect for Order modification/cancellation.

G1 U1–U13 are explicitly preserved in the review. Material absences include care-responsibility effects, signature/finality/legal criteria, broader outcome-kind/authority traces, business-acceptance criteria, Actor eligibility, ancestry/Feature associations, ON_LEAVE correspondence, discharge publication timing, Episode meaning, detailed requested/proposed scheduling and Order change-effect criteria. No new upstream contradiction requiring modification was identified; no frozen source was reopened.

D suggestions only: Episode Context; Responsibility Assumption Assertion. Neither is in the principal inventory, an approved resolution or an instruction to implement. No unrelated suggestion was promoted to make the architecture appear complete.

### Verification

Architecture tests: **PASS**. Executed the repository-required invocation with a 180-second command bound:

```bash
timeout --signal=TERM --kill-after=15s 180s mvn test -pl paradeigma/paradeigma-test -am -Dtest='*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Maven completed successfully in **17.301 seconds: 90 tests, zero failures, errors or skips**, across 11 ArchitectureTest classes. No timeout or stall occurred. The suite checks implemented repository architecture; it does not approve candidate conceptual semantics.

- **PASS — links:** 418 Markdown documents scanned across docs and Junie artefacts; 1,190 baseline links and 1,328 final links checked, including file/anchor targets and inbound references. Zero new broken links; zero broken links in task files. Seven pre-existing historical Junie link defects remain unchanged.
- **PASS — identifiers/references:** 18 unique local Information Requirement keys resolve; 18 Concept/composition definitions have necessity, classification and authority/provenance coverage; all G2-Q01–07 references resolve. FEAT-SA-03–14 references match existing Strategy definitions; all 137 Strategy Feature definitions remain unchanged and unique. Thirteen canonical Function/Service pairs and four ownership-matrix anchors resolve; Order Outcome remains Capability-scoped with no assigned Feature. No structural Canonical ID or alias allocated.
- **PASS — preservation:** G1 closure, all K1–K13 approved decisions, U1–U13 and central AX-17 are byte-identical to the pre-task baseline. Domain01–03, 10 Foundation files and six Package1 family bodies are unchanged. The index change adds candidate navigation only; original Package1/metamodel/authority sections remain identical.
- **PASS — scope:** only seven content files and the companion diff are changed/created; 2,247 out-of-scope tracked files remain byte-identical. Initial worktree was clean, staging/index state is unchanged and no commit was made. No production/downstream/runtime source changed.
- **PASS — whitespace:** git diff --check; added/new-file whitespace checks; companion patch checked with git apply --check --whitespace=error. Existing unchanged index formatting is preserved.
- **PASS — complete diff:** replay against saved pre-task content recreates all seven content files byte-for-byte. The diff includes every new file's complete content, excludes only its own generated bytes and has no unrelated changes.

Semantic inspection preserved the G1 distinctions, source-specific authority, conditional/contextual associations, candidate composition standing and explicitly unresolved relationships. None of the verification results constitutes architectural acceptance of G2.

### Stop boundary and handover

Architectural review of the seven questions is the next activity; this task answers none on behalf of the architect. **G1 remains CLOSED. G2 is candidate and not CLOSED. G3 and later Package2 family derivation were not commenced.** Diagnostics, Medication, Procedure, Clinical Document, Problem / Condition / Care Need, Care Plan and Clinical Communication families were not derived. No downstream representation modelling, FHIR mapping, implementation allocation or runtime work occurred.

The companion diff contains all seven content-file changes (four families, review, index and report), including new-file contents. As a generated patch it excludes only its own bytes; patch replay is verified against the saved pre-task content. Diagnostic logs, validator and baseline snapshots reside under `/tmp/harmonia-g2-block1/` and are verification artefacts, not architectural authority.
