<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Pathway, Journey, Publish/Subscribe and Handoff Adjudication

**Date:** 2026-10-11. **Standing:** completion evidence for a bounded Domain05
Step A review/correction task; this report is not architectural authority.

## Task Goal

Correct and extend the Domain05 Candidate Information Systems Architecture to
faithfully incorporate human-adjudicated Pathway, Journey, Publish/Subscribe,
Mneme semantic management, Petasos persistent handoff and Ponos execution
boundaries. Preserve established Energeia/Pragma specialisations and Domain04
information meaning; retain unadjudicated details under AX-17. No whole-candidate
acceptance, Step B mapping, MVP analysis, implementation plan or source-code
change is performed.

## Task Activity

Authoritative repository context reviewed:

- Repository [AGENTS.md](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md),
  [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md)
  and [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md).
- All eight existing Domain05 views listed below, including their current
  candidate standing and prior Energeia/DigitalTwin/Dokimasia adjudication.
- Domain04 [baseline](../../docs/markdown/04-information-architecture/README.md),
  [Task / Work](../../docs/markdown/04-information-architecture/information-families/task-work.md),
  [observable information/domain meaning](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md),
  [Information Unit metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md)
  §§5–6 and relevant guardrails, [authority/custody/provenance](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md),
  and the [Dokimasia finding](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md).
- Relevant existing [Strategy responsibility guardrails and Mneme/Mnemosyne/Ponos profiles](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md).
  This was constraint preservation, not Step B capability mapping.

All supplied DesignNotes were read in full and used as primary human-adjudicated
inputs under the explicit task instruction:

| Source key | Supplied note | Incorporated matters |
| :--- | :--- | :--- |
| U02 | `DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md` | Prior execution/activity/information specialisations, reuse and terminal rules preserved. The `(1)` attachment is byte-identical. |
| U03 | `DesignNote-Pragma-Management-Responsibility-and-Energeia-Boundary.md` | Mneme Pragma Management/PragmaFactory and non-transfer of management responsibility. |
| U04 | `DesignNote-Pathways-Journeys-and-Assurance-Representation.md` | Interfaces/control, distinct progression mechanisms, canonical Journey and observation versus interpretation. |
| U05 | `DesignNote-Mneme-Publication-Subscription-and-Petasos-Handoff-Architecture.md` | Asymmetric Publishing/Subscription, Pattern/Topic, semantic selection, persistent reference handoff, separate state and partially adjudicated fan-out. |

The working/conversational labels in U04/U05 were preserved in source standing:
the task authorises incorporation of their decisions into the candidate, not
acceptance of the complete Solution Architecture baseline. Their necessary
meaning is now in RADS without requiring external attachment access.

Candidate Architecture files changed:

- [README](../../docs/markdown/05-information-systems-architecture/README.md):
  adjudication orientation and unchanged Step-A completion boundary.
- [System map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md):
  information-management hierarchy, separate construct kinds, collaboration and
  Journey evidence arrows; prior specialisation diagram preserved.
- [Component/construct register](../../docs/markdown/05-information-systems-architecture/component-register.md):
  corrected C04/C06/C07/C09/C10/C20/C23/C28 and added C29–C38. Navigation IDs
  distinguish interfaces, flow/control, observation and contained responsibilities.
- [Feature map](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md):
  corrected execution/selection/handoff/reporting features and added F36–F38.
- [Data/persistence](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md):
  corrected D06/D07/D10/D11, added D20–D25, separate state and corrected Petasos
  persistence-record meaning, with no physical schema selection.
- [Middleware](../../docs/markdown/05-information-systems-architecture/middleware.md):
  corrected semantic selection, context resolution and handoff responsibilities;
  added M13–M15 and bounded collaboration examples.
- [Gaps](../../docs/markdown/05-information-systems-architecture/gaps.md):
  reviewed/updated CDG-01/02/03/06/08/09/14/16/17/18/19/27/28;
  added CDG-36 selection contracts, CDG-37 fan-out detail and CDG-38 Journey
  contracts. Other affected Pragma/assurance/evidence/discovery entries were
  reviewed and their valid residual questions retained.
- [Sources](../../docs/markdown/05-information-systems-architecture/sources.md):
  U03–U05/A06 provenance and targeted legacy dispositions.
- New [shared semantic view](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md):
  definitions, endpoint flow, selection/handoff and Journey/assurance diagrams,
  information shape and explicit decision/uncertainty boundaries.

Final navigation registers contain 38 components/constructs, 38 features,
25 data entries, 15 middleware entries and 38 candidate-documentation gaps.
These counts are not a completeness or upstream coverage score.

### Canonical Documentation Assessment

Relevant legacy architectural meaning was found outside RADS. Eleven targeted
section-level supersession wrappers were added across six files:

| Legacy file | Wrapped sections | RADS replacement |
| :--- | :--- | :--- |
| [Pragma concept](../../docs/concepts/pragma.md) | Opening definition; §2 architectural definition; §5 Zero-PHI context-safety claims | Pragma semantic shape, state separation, C07 and retained representation uncertainty. |
| [Petasos concept](../../docs/concepts/petasos.md) | Opening definition; §3 positive ownership list | Shared persistent handoff C10 and non-transfer of information ownership. |
| [Petasos messaging](../../docs/concepts/petasos-messaging.md) | §2 universal message-envelope account | Pragma reference handoff versus unresolved broader message forms. |
| [Praxis concept](../../docs/concepts/praxis.md) | Opening definition; §2 DAG definition assertion | C06 unresolved broader meaning and established Pathway [0..1]. |
| [Execution model](../../docs/architecture/execution-model.md) | §2.1 envelope conversion/ACK coupling; §4 combined Pragma state/checkpointing | Governed Mneme resolution and separate information/handoff/undertaking/execution state with retained continuation details. |
| [LaTeX Praxis appendix](../../docs/latex-legacy/chapters/appendix-praxis-workflow.tex) | Topic Subscription Matching subsection | Mneme semantic selection, classification Topic and unresolved Pattern/fan-out contracts. |

Every enclosed original section and all earlier wrappers remain unchanged.
Removing only this task's wrappers restores all six files byte-for-byte.
No whole document is superseded and no unrelated migration is performed.
Other historical technology/schema/guarantee/tutorial descriptions retain their
previous supporting or unresolved standing; this task does not reconcile them
by copying them into current architecture. Supplied external notes remain
unchanged; provenance is recorded and adjudicated meaning incorporated in RADS.

## Architectural Decisions Incorporated

| Construct / responsibility | Incorporated decision |
| :--- | :--- |
| PathwayEndpoint | ApplicationInterface-equivalent ingress/egress boundary; every Ergon exposes both. |
| PathwayElement | Generic cross-boundary information/activity flow representation with at least Segment/Traversal specialisations. |
| PathwaySegment | Inter-component EgressEndpoint → IngressEndpoint interaction; ApplicationInteraction analogy. |
| PathwayTraversal | Intra-component ingress → egress traversal, without exposing business logic. |
| Route | Ordered PathwaySegment [1..N], principally routing/control. |
| Pathway | Controlled endpoint interaction and available Route(s); Praxis may have Pathway [0..1] only. |
| Journey | Governed observed progression from available evidence; canonical reporting/monitoring/analysis/assurance representation for Pathway, Pub/Sub and mixed execution; may be graph-shaped or have no Pathway. |
| Publishing | Provider eligibility/filtering; matching Pattern may block egress availability through publication. |
| Subscription | Consumer interest/selection; requests matching available Pragma for its input destination. |
| Pattern / Topic | Mneme-governed metadata evaluation across Pragma and Unit Container/Content levels; Topic is classification metadata, distinct from destination. |
| Mneme semantic selection | Owns definitions, evaluation, eligibility, matching/selection and eligible consumer/destination relationships; coordinates information availability, not consuming execution. |
| Petasos persistent handoff | Persists reliable handoff obligations, Pragma identity/reference and handoff metadata/recovery state; managed payload remains through Mneme. Broader Communication/Notification and other message forms retained. |
| Consumer undertaking / Ponos | Consumer resolves governed information and establishes its distinct FulfillmentTask for ActionableTask undertaking; Ponos manages execution/progression and execution re-attempts. |
| Journey assurance role | Dokimasia/reporting may interpret governed observations against applicable expectations, including Pathway/Route where present; inferred relationships/findings cannot replace observed evidence. |

The Energeia execution/activity/information axes, Mneme PragmaFactory/Pragma
management, DigitalTwin/Behaviour/Stimulus/Effector reuse, Dokimasia Ergon/AI
Assurance Ergon and both Dokimasia terminal rules remain intact. No assurance
subclasses, mandatory algorithms or new deployed services are inferred.

Material axiom consistency:

| Applicable axioms | How the correction preserves them |
| :--- | :--- |
| AX-01/04/12 | Governs Harmonia information semantics and reusable activity contracts before machinery; no broker, data-grid, language/framework or topology choice. |
| AX-05/06/11/18 | Mneme manages governed active information; Mnemosyne alone establishes authoritative durable managed state. Selection, handoff, execution, evidence use and assurance confer no originating information/domain authority. |
| AX-07/13 | Governed selection/handoff/activity uses established context/Themis. Internal Publishing does not terminate information management; Pylai external publication remains non-destructive and fail-closed. |
| AX-08/09 | Journey observations, operational diagnostics, AuditEvents and assurance results remain distinct; no permanent audit record required for every observation. |
| AX-10/14/15/16 | Persistent recoverable obligations, granular observed progression and separate information/handoff/undertaking/runtime state; execution retry differs from redelivery; uncertain effects are not assumed to be success/failure. |
| AX-17 | Candidate standing, broader Praxis, detailed fan-out/Pattern/handoff/Journey and technology uncertainty remain explicit; implementation and symmetry supply no missing facts. |

The previous Candidate Architecture wording conflicted with these responsibility
distinctions and is explicitly corrected. No axiom amendment or Domain04
semantic change was required. Existing REC-002 destination tracking is read as
governed observation information, without transferring Petasos operational
handoff ownership into Pragma. Production implementation semantics were not
reverse-derived or changed.

## Retained Uncertainty

- Broader Praxis definition, composition and instance semantics beyond
  Pathway [0..1], and detailed Pathway/Route control/definition management.
- Pub/Sub fan-out reference/copy/version binding and lifecycle detail. U05
  **partially settles** common Mneme-managed Pragma/payload, potential multiple
  Petasos obligations and distinct consumer FulfillmentTasks for ActionableTask
  undertakings. It does not settle Pragma undertaking identity, targetDestination,
  processing/concurrency/isolation, morph mechanics, lineage or Journey correlation.
- Detailed Pragma and Container/Content metadata definitions, including the
  application Container notation's precise relation to Domain04's partition.
  Topic/source/origin/destination are not silently made aliases.
- Pattern language, supported attributes/operators, precedence, evaluation/error
  contracts, query/index machinery, and Publishing/Subscription lifecycle,
  registration/discovery and preservation.
- Detailed Petasos handoff representation, destination/channel vocabulary,
  acknowledgement/recovery/duplicate/redelivery/replay/continuation contracts and
  other event-form semantics. No universal reference-only format.
- Journey construction and information-management ownership; detailed graph,
  evidence/reference/causal/correlation schema, relation to PathwayElement,
  lifecycle/persistence/retention and reporting/Dokimasia consumption contracts.
- Detailed representation of inference versus observed evidence, uncertainty
  and assurance results. Missing evidence is not proof of missing activity.
- Previously retained Dokimasia independence/control, assurance Pragma and AI
  discovery details, TaskOutcome/ReportedTask deferrals and persistence questions.
- Technology implementation, broker/data-grid mechanisms, physical schemas,
  queue/cache topology and AI provider/model/framework. None is selected.

## Corrections to Previous Candidate Meaning

- C10/F13/D11/M01 and the Petasos persistence row described opaque work/envelope
  transfer as though Petasos held the managed work payload. They now describe
  persistent obligations with identity/reference resolution through Mneme for
  Pragma processing and retain Petasos's broader messaging role.
- F04/M03 and system-map arrows previously assigned metadata/subscription-based
  workflow selection and opaque-envelope-to-Pragma conversion to Ponos. Mneme
  now owns semantic selection; consumer information resolution and undertaking
  precede Ponos execution.
- D07 and historical Praxis/topic matching descriptions grouped Topic,
  subscriptions and destinations under routing. Topic now has explicit governed
  classification/metadata meaning, with independent Container/Content levels;
  destination mechanics and semantic selection remain distinct.
- C07/D10/F06/F10 combined delivery observations and processing state without a
  clear operational owner. Governed delivery observation information remains
  possible, while Petasos owns the handoff obligation/state; detailed relationships
  and parent progression remain unresolved.
- C04/F05/M03 previously combined execution retry and dead-letter escalation.
  Ponos execution re-attempts are separated from Petasos handoff redelivery/
  recovery/dead-letter semantics.
- Journey was absent as a first-class construct while reporting views primarily
  described checkpoints/telemetry. C35/D21/F38/M15 now establish its canonical
  observational and assurance role across both progression mechanisms, without
  treating AI interpretation as observed fact.
- C06/F11/F12/D06 and the structural map no longer present historical Praxis
  composition/runtime descriptions as settled architecture. Pathway cardinality
  is incorporated while broader Praxis remains explicitly unresolved.

## Validation

**PASS — required architecture suite.** Java 21 with cached dependencies and a
600-second command bound (15-second termination grace):

```bash
env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=15s 600s \
  mvn -o -B test -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**BUILD SUCCESS; 90 tests across 11 architecture classes; 0 failures, 0 errors,
0 skips; 27.370 seconds.** ArchUnit detected Java 21.0.12.1. No timeout or stall.
Log: `/tmp/domain05-pathway-journey-20261011/architecture-tests.log`.
These tests establish existing executable guardrails; they neither approve the
Candidate Architecture nor implement/test the newly documented constructs.

**PASS — documentation and preservation checks.** CommonMark/table parsing,
local Markdown links and anchors, wrapper replacement links including the LaTeX
wrapper, balanced fences, table widths, unique register IDs and `git diff --check`.
All 11 original legacy sections preserved; wrapper removal restores all six
files byte-for-byte. SHA-256 comparison preserves all 76 Domain01–04 files and
all five supplied attachment files. Tracked/untracked scope verification confirms
only nine Domain05 RADS files, six legacy wrapper files and this report changed;
no implementation, source code, build configuration or upstream architecture changed.

Validation harness/results:
`/tmp/domain05-pathway-journey-20261011/validate_docs.py` and
`/tmp/domain05-pathway-journey-20261011/validation.json`.
The first whitespace check identified an extra blank line at the end of one new
wrapper; only wrapper whitespace was corrected and preservation revalidated.
Final required checks pass. Mermaid relationships were reviewed textually;
no rendered-diagram or LaTeX publication-build check is claimed.

**PASS — semantic cross-view review.** Praxis remains unresolved beyond
Pathway [0..1]; fan-out detail remains open despite U05's bounded principle;
Mneme owns Publishing/Subscription/Pattern; Petasos retains general messaging
and persistent handoff; Ponos execution is separate; Journey covers Pathway,
Pub/Sub and mixed execution; observations/inferred interpretation/findings remain
distinct. No new product, technology, algorithm, final schema, upstream traceability,
MVP, implementation plan or whole-candidate acceptance is introduced.

## Target Outcome

Domain05 now coherently records the bounded adjudicated candidate:

> Pathway defines controlled progression; Publish/Subscribe defines dynamic
> information selection; Mneme manages information eligibility and selection;
> Petasos persistently manages reliable handoff; Ponos manages execution;
> Journey represents what Harmonia observed to occur; and Dokimasia/reporting
> may interpret that Journey without replacing observed evidence with inferred
> meaning.

The result remains **Domain05 Step A Candidate Architecture**. Acceptance as
sufficiently faithful (A4), Step B and Domain05 completion remain pending and
require separately authorised work.
