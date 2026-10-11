<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Energeia / DigitalTwin / Dokimasia Candidate Adjudication

**Date:** 2026-10-11. **Status: bounded Step-A correction complete; whole
Candidate Architecture acceptance remains pending.**

The supplied DesignNote's adjudications are incorporated across the eight
Domain05 RADS views. Execution, activity and information responsibilities are
now distinct; specialised constructs reuse common platform facilities;
Dokimasia terminal constraints and the AI discovery obligation are explicit.
This updates the existing report after resolving the primary-input access issue.
It does not complete Step A, accept the whole candidate or begin Step B.

## 1. Scope, input and architectural authority

The [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md#33-domain05-five-stage-completion-method)
places this task in A2 architectural review/adjudication and iterative A3
correction. A1 reconstruction was already complete; A4 acceptance remains
pending. No capability/information traceability, upstream coverage gap analysis,
excess/MVP assessment or implementation planning was performed.

Primary input: the complete user-supplied
`DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md`, dated 2026-10-11,
read from the supplied attachment at
`/home/mhunter/Downloads/DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md`.
The user clarified that the original `/DesignNotes` reference meant their
ChatGPT Library. The earlier access issue was an input-access dependency,
not an architectural gap. It is resolved and was not added to `gaps.md`.
Completed discovery was not repeated; source inspection expanded only as needed
for reconciliation and legacy section preservation.

Source SHA-256:
`3447f29389d698fe7a14025f4fb80c473891fc355e593cbcada3e5be2d2c5c01`.
The supplied file remains unchanged. [U02](../../docs/markdown/05-information-systems-architecture/sources.md#u02)
records provenance and links the incorporated RADS meaning, without making an
external Library/Downloads path a required architectural dependency.

Repository `AGENTS.md`, `docs/AGENTS.md`, the Architectural Axioms, Completion
Plan and Domain05 context were reviewed before the initial input dependency.
The existing Domain04 Task / Work model, governed Ergo information boundary
and Dokimasia finding constrain this correction. The already-established
Domain03 non-recursion boundary remains preserved. Implementation, source-code
structure and framework conventions supplied no architectural decisions.

| Material axioms | Consistency of the correction |
| :--- | :--- |
| AX-04 / AX-12 | Common execution/activity/information-management facilities are reused; no literal inheritance, new platform machinery or AI technology is required. |
| AX-05 / AX-06 | Mneme manages Pragma information; Mnemosyne establishes durable state. Execution, assessment and transport confer no information-management or originating authority. |
| AX-07 | Both specialisations retain established security context, Themis and governed execution. |
| AX-08 / AX-09 | Governed assurance record/result supplies evidence of the assurance activity; no separate Dokimasia own-execution AuditEvent. Controlled diagnostics/reporting remain distinct from durable evidence. |
| AX-10 / AX-11 | Per-Twin pending work is compatible with bounded resources and common recoverable handoff; no thread per Twin or weaker local authority. |
| AX-14 / AX-16 | Execution/activity/information axes and observable progression remain distinct; Task work, undertaking, outcome participation and output controls are preserved. |
| AX-15 / AX-17 | Candidate standing, detailed-design deferrals and operational uncertainty remain explicit. AI component responsibility is established without pretending its detailed design is complete. |
| AX-18 | Twin coordination, AI invocation and assurance confer no originating clinical/domain authority or operational management of the assured subject. |

No genuine contradiction with the consulted upstream semantic baseline was
identified. The Domain04 finding's open execution-placement question is a
prior downstream deferral, not a prohibition on the explicitly adjudicated
Domain05 Ponos specialisation. Operational independence remains mandatory:
common reuse cannot let the assured activity determine, suppress, manufacture
or retrospectively alter its assurance outcome. No upstream file was corrected
or reinterpreted to establish that candidate relationship.

## 2. Files changed

| Domain05 RADS file | Correction |
| :--- | :--- |
| [README](../../docs/markdown/05-information-systems-architecture/README.md) | Current Step-A standing, U02 decisions and common specialisation rule. |
| [System map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Corrected responsibility tree, separate three-axis specialisation view, typed relationships and collaboration. |
| [Component register](../../docs/markdown/05-information-systems-architecture/component-register.md) | Distinct Ergo/Ergon/Erga, specialised execution/activity entries, Mneme Pragma Management and terminal constraints. |
| [Feature map](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | Affected execution/activity/Pragma/Twin/assurance/evidence/reporting allocations; new candidate/discovery feature F35. |
| [Data and persistence](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | Pragma management and candidate specialisations; preserved Task meanings; no schema/persistence selection. |
| [Middleware](../../docs/markdown/05-information-systems-architecture/middleware.md) | Shared platform collaboration and management/execution/handoff boundaries for specialisations. |
| [Gaps](../../docs/markdown/05-information-systems-architecture/gaps.md) | Affected gap reclassification and CDG-35 AI discovery obligation. |
| [Sources](../../docs/markdown/05-information-systems-architecture/sources.md) | Primary review-input provenance and affected legacy dispositions. |

Six legacy documents received wrappers only, listed in section 5. This same
report was updated; no second report was created. Total task scope: eight RADS
files, six legacy files and this report.

The pre-existing modification to `docs/markdown/architecture-completion-plan.md`
and the reports `2026-10-11-domain05-completion-plan-update.md` and
`2026-10-11-domain05-step-a-status-refinement.md` were preserved and are not
changes made by this task. No implementation/code or Domain01–04 file changed.

## 3. Candidate structural corrections

Energeia is the activity/execution architectural subsystem. Ponos is the
reusable execution framework/component pattern; Ergo is the reusable conceptual
activity pattern and integration contract; Ergon is the bounded fine-grained
software activity component; Erga is a meaningful collection for executable
assembly, concurrent activity management/observation (including multiple
instances of one Ergon type) and coherent library/change/testing/version/release
management. Ponos and Ergo/Ergon remain tightly coupled peer responsibilities.

C05 now records Ergo separately. Added navigation entries are C23 Ergon,
C24 Erga, C25 Behaviour, C26 Dokimasia Ergon, C27 AI Assurance Ergon and
C28 Pragma Management. C19/C20 retain their keys for DigitalTwin/Dokimasia.
There are now 28 component/construct entries; inclusion does not imply 28
independent processes or deployment units.

| Relationship | Recorded candidate meaning |
| :--- | :--- |
| Responsibility containment | Mneme → Pragma Management → PragmaFactory and Pragma. Energeia includes distinct execution/activity responsibilities. |
| Execution specialisation/reuse | DigitalTwin and Dokimasia specialise Ponos; neither is contained by a Ponos runtime, nor does one specialise the other. |
| Activity contract/specialisation | Ergon conforms to/reuses Ergo; Behaviour specialises Ergon; Dokimasia Ergon specialises Ergon; AI Assurance Ergon specialises Dokimasia Ergon. |
| Information specialisation | Stimulus/Effector are candidate Pragma specialisations; Dokimasia requires corresponding candidate assurance request/input and result/finding semantics. |
| Execution/collaboration | Ponos executes/manages bounded Ergon; DigitalTwin executes Behaviour; Dokimasia executes assurance Ergon, including explicit AI activity where selected. Petasos provides messaging/recoverable handoff. |

PragmaFactory creates/initialises appropriate Pragma representations associated
with the ActionableTaskArchetype meta-concept, which is not itself an instantiated
Information Asset. Execution of, behaviour upon, or transport of information
does not confer its management responsibility. Domain04 ActionableTask versus
FulfillmentTask and TaskOutcome ownership, undertaking references, orthogonal
controls and remaining deferrals are preserved. Ergon and its specialised
activities acquire no discrete Information Resource merely by being activities.

DigitalTwin adds the per-Twin queue pattern: only Twins with pending work become
active, serviced by bounded execution resources. It does not require a thread
per Twin. Behaviour reuses Context Loading/Unloading, process management,
Petasos/platform integration, assurance integration, logging/observability,
reporting, checkpoint/platform integration and other Ergo/Ergon facilities.
Stimulus/Effector reuse common Pragma management/process/assurance/reporting
facilities. Detailed schemas and lifecycles remain unfinalised.

Dokimasia follows the same common-reuse principle while preserving independent
assurance determination and the assured subject's operational management.
Both terminal rules are explicit architectural constraints:

1. A Dokimasia execution SHALL NOT itself be monitored, assured or assessed by
   another Dokimasia execution.
2. It SHALL NOT generate a separate AuditEvent for its own execution; the governed
   Dokimasia assurance record/result provides authoritative evidence of that
   assurance activity.

Governance, execution integrity, controlled observability and reporting remain
available. Inherited assurance/evidence hooks respect those terminal rules.
Record representation, preservation and access remain unresolved; no repository,
state machine or final Pragma/outcome schema follows from the evidence rule.

Ordinary Ergon can internally invoke an AI service as an implementation choice.
AI Assurance Ergon instead makes AI processing an explicit governed activity
within Dokimasia workflow. Its existence/discrete responsibility is established;
its detailed design remains candidate/discovery. Not all AI use is allocated to
this construct, and no AI technology, provider, SDK or agent framework is selected.

The feature register now has 35 entries, with F35 explicitly candidate/discovery.
Affected existing features retain their purpose without manufacturing upstream
requirements. D18/D19 expose the specialised Pragma candidates; M10–M12 expose
management/Twin/assurance collaboration. These are candidate-view corrections,
not formal Step-B capability/information traceability.

## 4. Gap dispositions and discovery

| Gap | Resolved/reclassified meaning | Remaining uncertainty |
| :--- | :--- | :--- |
| CDG-04 | Mneme Pragma Management placement, archetype-associated factory embodiment and creation/initialisation established. | Detailed factory parameters, selection/validation/policy, callers, objects and persistence. |
| CDG-05 | Ergo, Ergon and Erga distinction and common facility reuse established. | Atomicity, side effects, purity/idempotence, branch/composition and converter/activity allocation. |
| CDG-07 | DigitalTwin/Behaviour specialisation, per-Twin queue responsibility and candidate Stimulus/Effector established. | Detailed scheduling/activation/control, coordination state, persistence and specialised information schemas/lifecycles. |
| CDG-08 | Dokimasia Ponos specialisation, discrete assurance Ergon, common reuse and terminal rules established. | Detailed independent control/evidence access and workflow/composition; separate Praxis adjudication. |
| CDG-09 | Need for corresponding candidate assurance Pragma semantics and authoritative assurance-activity record/result established. | Final names, schemas, cardinalities, lifecycle, exchange/access and preservation. |
| CDG-13/14 | Dokimasia evidence role and terminal own-execution AuditEvent constraint are settled. | General evidence custody/storage, significance/promotion/retention and checkpoint contracts remain unresolved. |
| CDG-35 — new discovery | AI component existence/discrete responsibility established. | Detailed Information Systems architecture requires discovery before implementation modelling is finalised. |

CDG-35 covers model/service selection and abstraction; context/input construction;
disclosure/security; provenance/versioning; confidence/uncertainty/results where
applicable; deterministic/non-deterministic behaviour; reproducibility requirements
and limitations; validation; human review/escalation; evidence representation;
workflow integration; failure/timeout/service unavailability; and reporting/
observability consistent with both terminal rules. None of these detailed matters
was resolved in this task. Other potential assurance Ergon subclasses are not
created by symmetry.

Unrelated gap rows remain unchanged. This is correction of documented candidate
meaning, not Step-C upstream coverage analysis. Unresolved detailed design does
not make the established AI component boundary unjustified.

## 5. Canonical documentation assessment and legacy preservation

The supplied DesignNote contains architectural adjudications belonging to
Domain05; their necessary meaning is incorporated in RADS. Its section 14
non-finalised matters remain explicit as unresolved candidate detail. The external
attachment was read without modification; it is not a repository legacy document
requiring a wrapper.

Existing repository legacy descriptions were reconciled only where affected by
this correction. Twelve new wrappers across six files point to specific RADS
replacement sections while preserving original content unchanged:

| Legacy file | Sections wrapped by this task |
| :--- | :--- |
| `docs/concepts/ponos.md` | Opening daemon-only definition. |
| `docs/concepts/ergon.md` | Opening Ergon/informal-plural definition; §2 Core Characteristics, with retained behavioural uncertainty referenced. |
| `docs/concepts/pragma.md` | §3 What Pragma Owns. |
| `docs/modules/energeia.md` | Opening composition paragraph; §3 What Energeia Owns, retaining Praxis uncertainty. |
| `docs/architecture/execution-model.md` | Structural introduction/§1 four-tier hierarchy; §6.2 assurance AI/authority boundaries. |
| `docs/modules/dokimasia.md` | Opening paragraph; Digital Twins and Execution; Runtime AI; Open Downstream Architecture. |

No entire legacy document was superseded. Existing wrappers and unwrapped
implementation detail, historical reasoning, canonical upstream navigation and
unrelated unresolved material were preserved. Wrapper removal restores all six
pre-task legacy files byte-for-byte. Legacy citations and historical diagrams
may still contain old vocabulary inside the explicit historical boundaries;
they are not competing current architectural authority.

## 6. Validation and architecture tests

**PASS — documentation checks:** local link/file/anchor checks and balanced
Markdown fences across the 15 task files; unique navigation identifiers;
`git diff --check`; unchanged original legacy sections and byte-for-byte full
file restoration after removing new wrappers; SHA-256 preservation of all 76
Domain01–04 files and the supplied DesignNote.

**PASS — cross-view architectural review:** distinct construct terminology;
containment separated from contract/specialisation, execution, information
management and collaboration; common capabilities reused; candidate Pragma roles
and AI detail visibly unresolved; both terminal constraints retained alongside
security/reporting/observability; Domain04 Task and Ergon boundaries preserved.
Mermaid relationship notation was reviewed textually; no renderer check is claimed.

Fresh required verification after the correction used Java 21 and cached Maven
artifacts, with a 600-second command timeout and 15-second termination grace:

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=15s 600s \
  mvn -o -B test -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS: BUILD SUCCESS; 90 tests across 11 architecture classes; 0 failures,
0 errors, 0 skips; 27.892 seconds.** ArchUnit detected Java 21.0.12.1;
no unsupported-class-version warnings. Log:
`/tmp/harmonia-domain05-adjudication-architecture-tests-java21-final.log`.
No command timed out or stalled.

Earlier input-dependency-stage diagnostics are retained as history: the exact
online Maven command stopped after 2.027 seconds because resolver metadata under
`/home/mhunter/.m2/repository` was read-only (**environment error; test behaviour
UNRESOLVED for that attempt**). An offline Java 25 run reported success after
25.861 seconds but emitted unsupported-major-version-69 ArchUnit import warnings.
A corrected Java 21 baseline run passed 90 tests in 28.909 seconds before the
architecture correction. The fresh Java 21 result above is the completion evidence;
no production code was changed in response to the environment issues.

Executable tests establish existing implementation guardrails. They do not
constitute acceptance of the Candidate Architecture, prove a new Dokimasia/AI
implementation or settle detailed independent execution/control design.

## 7. Remaining architectural questions and completion boundary

**Praxis still requires separate architectural adjudication.** Its definition,
composition, actual execution and relationships with Ergo/Ergon/Erga are not
finalised. C06 and CDG-01 retain that uncertainty; no substantive Praxis decision
was made to accommodate these corrections.

Detailed Twin control/state contracts, PragmaFactory contracts, Stimulus/Effector
schemas/lifecycles, Dokimasia control/evidence-access realisation, candidate
assurance Pragma names/schemas/cardinalities/lifecycles, assurance record/result
representation/preservation, Erga assembly/library detail, additional assurance
activity classifications and CDG-35 AI design remain unresolved as recorded.
Inherited assurance integration must preserve the two established terminal rules;
their normative meaning is not reopened by detailed hook design.

The requested candidate corrections and report update are complete. Domain05
remains explicitly candidate architecture pending completion of Step-A review
and acceptance. No Step B–E work, implementation plan, implementation change,
AI technology selection or Domain01–04 modification was performed.
