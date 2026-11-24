# Domain04 TaskOutcome Semantic Refinement — Completion Report

**Date:** 2026-10-10

**Programme:** Harmonia R1.x/R2.x Architecture Completion; Domain04 Information
Architecture Reconciliation.

**Scope:** Bounded follow-on to the completed Task information reconciliation.

**Result:** The authorised refinement is applied to RADS. Domain04 completion,
the wider Task family and Domain05 derivation are not declared or recommended.

This is an execution record, not architectural authority. The maintained
[Task / Work model](../../docs/markdown/04-information-architecture/information-families/task-work.md)
holds the adjudicated semantics. The
[preceding reconciliation report](2026-10-10-domain04-task-information-reconciliation.md)
and commit `c661838` were inspected to locate prior changes and validation
tooling; their earlier outcome wording remains historical, not current authority.

## Authoritative Material Reviewed

| Source | Role in this refinement |
| :--- | :--- |
| [Root AGENTS.md](../../AGENTS.md) and [docs/AGENTS.md](../../docs/AGENTS.md) | Architectural authority, bounded scope/execution, required architecture verification and RADS/legacy preservation. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md) | Domain04 programme position, progressive authority loading, canonical-corpus assessment and completion gates. |
| [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), particularly AX-04, AX-14, AX-17 and AX-18 | Information semantics independent of machinery; preserved distinctions; approved decisions versus retained uncertainty; non-transfer of domain authority. |
| [Task / Work model](../../docs/markdown/04-information-architecture/information-families/task-work.md), [Domain04 metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) and [modelling guardrails](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md) | Prior Task meanings, provisional containment/ownership, conceptual representation independence and reserved matters. |
| [Observable-information/domain-meaning boundary](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md) | Opaque output content, governed developer-defined Ergo logic and optional runtime AI. |
| [Definition-to-Accountability pattern](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md), [lifecycle guidance](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md), [Domain04 index](../../docs/markdown/04-information-architecture/README.md), [family index](../../docs/markdown/04-information-architecture/information-families/README.md), [Task traceability](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md#24-bounded-task--work-responsibility-basis) and [Dokimasia finding §4.4](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md#44-domain04--information-architecture) | Immediately preceding reconciliation's explanatory statements and accountability boundary. |
| Domain03 [clinical-work boundary](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#37-clinical-work-integration-boundary), [Workflow & Activity Coordination](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md#29-workflow--activity-coordination), [information responsibilities](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) and [Work Order / To Do / Synthetic Task progressions](../../docs/markdown/03-business-architecture/processes/business-processes.md#514-work-order-progression-process) | Preserved business classifications, generic coordination ownership and deliberately unresolved progression details. No upstream semantics changed. |
| Explicit human TaskOutcome adjudication supplied for this task | Authority for outcome ownership/content, the three orthogonal controls and exclusion of recursive TaskOutcome containment. |

[Accepted ADR material](../../docs/architecture-decisions.md), especially
ADR-004, ADR-007 and ADR-013–015, was consulted in its repository-assigned
decision role. It does not supply a Task-to-Pragma mapping, a derivation
implementation or a resolution of ReportedTask. No conflict between the
supplied refinement and the applicable axioms was found.

## RADS Files Changed

All ten files are within `docs/markdown/04-information-architecture`:

| File | Bounded change |
| :--- | :--- |
| [information-families/task-work.md](../../docs/markdown/04-information-architecture/information-families/task-work.md) | Maintained outcome definition/content; explicit policy controls and modes; revised conceptual diagram and remaining uncertainty. |
| [patterns/definition-to-accountability.md](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md) | Corrected Task example, ownership, outcome content and table; removed recursive composition. Healthcare Service example unchanged. |
| [guardrails/modelling-guardrails.md](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md) | Replaced provisional Task containment exception with its exclusion; made policy orthogonality explicit. The sixteen core guardrails remain sixteen. |
| [guardrails/observable-information-and-domain-meaning.md](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md) | Applied the established opacity boundary to Task.Output and Derived rules without allocating their execution. Ergo/runtime-AI sections unchanged. |
| [metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) | Updated only the bounded Task explanation to governed ActionableTask resolution and retained detailed uncertainty. |
| [governance/information-lifecycle.md](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md) | Distinguished undertaking state/output from parent outcome resolution and its orthogonal policies. No lifecycle model invented. |
| [traceability/domain03-traceability.md](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md) | Corrected Task resolution terminology; preserved the generic coordination and domain-authority boundaries. |
| [reviews/dokimasia-assurance-architectural-finding.md](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md) | Updated only the Task row in §4.4; governed resolution does not settle assurance/accountability. |
| [Domain04 README](../../docs/markdown/04-information-architecture/README.md) | Corrected the Task highlight and navigation descriptions. |
| [Information Families README](../../docs/markdown/04-information-architecture/information-families/README.md) | Removed possible outcome encapsulation and the misleading linear ownership description; summarised the refined controls and content. |

## Superseded Semantics Corrected

- Removed the proposition that TaskOutcome may independently associate with
  either ActionableTask or FulfillmentTask as its owning outcome.
- Replaced the undertaking's “may produce / establish TaskOutcome” arrow
  with participation under the parent ActionableTask's explicit policy.
- Removed the recursive TaskOutcome self-edge, possible outcome encapsulation,
  and the guardrail treating recursive containment as an unresolved possibility.
- Removed the reserved question about collections of FulfillmentTask outcomes:
  participating undertakings have state/output, and the single governed
  TaskOutcome references them. Participation and output handling are now
  separately established policies.
- Corrected statements that implied only an incidental ActionableTask outcome
  association, or left its relationship to undertaking outputs unadjudicated.
- Retained genuinely unresolved identity, further-cardinality, lifecycle,
  qualification, assignment and representation questions. Detailed metadata
  and payload structures were not supplied by general pattern guidance.

## Final TaskOutcome Definition and Outcome Policy

> **TaskOutcome is the governed resolution of an ActionableTask according to
> that ActionableTask's explicitly defined outcome policy.**

It belongs to and resolves the ActionableTask. FulfillmentTask remains an
identifiable undertaking with its own execution state and possible output,
and does not independently own TaskOutcome.

TaskOutcome **references participating FulfillmentTask [0..*]**, **contains
Task Completion Metadata** concerning parent resolution/completion, and
**contains / represents the resulting Task.Output(s)** established by the
output policy. Encapsulated output domain content may remain opaque.

| Independent ActionableTask control | Established semantics |
| :--- | :--- |
| **ExecutionConcurrency = [1..x]** | How many undertakings may execute concurrently. It does not select outcome participants or require execution to exist. |
| **OutcomeConcurrency.Mode** | **FirstToFinish:** the applicable first finishing/qualifying undertaking participates. **AssignedToFinish:** a particular assigned undertaking has responsibility; other completions do not independently establish the outcome. **Aggregate:** multiple applicable undertakings participate. |
| **Outcome.OutputRules.Mode** | **Direct:** use the applicable participating output directly. **Collection:** preserve participating outputs collectively as the relevant collection/set, without implying new domain meaning. **Derived:** establish a new output from participating outputs according to explicitly defined rules. |

The three controls remain **orthogonal** and SHALL NOT become one lifecycle,
state or concurrency mechanism. Concurrent execution may involve more
undertakings than outcome participation; multiple participants may supply
collected or derived output. No default, universal mode combination,
terminal-state qualification or cancellation behaviour was invented.

**Recursive TaskOutcome containment was removed**, rather than retained for
compatibility. Aggregate participation does not construct outcome hierarchies.
**No Task Completion Metadata schema or detailed output payload structure was
invented. No Derived implementation mechanism was prescribed.**

ActionableTaskArchetype, work instance and undertaking retain their established
distinctions. Domain03 Work Order, To Do and Synthetic Task remain distinct
business classifications. Domain content opacity, the governed Ergo boundary
and optional adjunct runtime AI remain intact.

**ReportedTask remains explicitly unresolved**, including its necessity,
meaning, accountability/assurance/evidence/audit/history relationships,
identity, immutability and lifecycle. Outcome resolution neither establishes
ReportedTask nor constitutes independent assurance.

## Legacy-Doco and Remaining Uncertainty

The focused legacy scan found no section separately defining the newly
adjudicated generic Task concepts or recursive TaskOutcome composition. FHIR
Task/OperationOutcome documentation describes existing interface or
implementation behaviour and does not establish equivalence with these
concepts. No such implementation detail was changed or declared superseded.

The [legacy execution model](../../docs/architecture/execution-model.md),
particularly §§4–5 and the existing §§6–6.1 supersession wrapper, was inspected
as reconciliation input. State/checkpoint and thread-pool descriptions do not
establish these information-policy controls. Its existing runtime-AI wrapper
already points to the canonical boundary and requires no change for this
refinement. The preceding report remains unchanged historical evidence.

**Legacy-doco affected by this task: none.** No legacy architectural meaning
was incorporated or replaced by this refinement, so no new wrapper was needed.
All 268 tracked files under `/docs` outside RADS were verified byte-identical
to HEAD, including the existing wrapped execution-model content.

Under AX-17, TaskOutcome identity, mandatory occurrence, further cardinalities,
detailed lifecycle, metadata/output representation and task-specific explicit
rules remain open for later definition. Execution/assignment mechanisms and
finishing qualification are not defined here. Archetype-authoring and
outcome-authority ownership beyond the established generic coordination basis
remain unallocated. Domain03's existing To Do dismissal/delegation and
Synthetic Task stalled/failed ordering uncertainties remain unchanged.

The inspected legacy §4 durability/write-behind description still requires
separate reconciliation with AX-05. It cannot establish active cache state as
authoritative durable truth, determine this Task model or authorise a runtime
change. Generic Task-to-Pragma/Praxis mapping and broader ADR/execution-model
canonicalisation remain separately governed follow-ons. No new blocking
architectural conflict was discovered for the authorised refinement.

## Validation Results

The repository-prescribed architecture suite ran with cached dependencies
offline and a five-minute TERM bound with ten-second kill grace:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS — BUILD SUCCESS; command exit 0; 90 tests, 0 failures, 0 errors,
0 skipped; 11 architecture classes; Maven total time 26.735 seconds.**
Surefire XML was inspected. All eight AGENTS.md classes completed, plus
GovernedWriteCompositionArchitectureTest, GovernedWriteContractArchitectureTest
and MnemosyneAuthoritativePersistenceArchitectureTest. No timeout or stall
occurred. Log: `/tmp/harmonia-taskoutcome-architecture-tests.log`.

ArchUnit emitted unsupported Java 25 class-file major version 69 import
warnings for JDK classes; Maven/Guava emitted deprecated `sun.misc.Unsafe`
warnings. Assertions passed with this environmental import limitation. The
suite verifies existing implementation guardrails, not semantic completeness
of the documentation model.

Documentation verification follows the preceding reconciliation's available
Markdown-it-py CommonMark/table parser, HTML-anchor/GitHub-heading-slug checks,
local outgoing and incoming links, balanced fences and scope/preservation
checks. No packaged repository Markdown/link validator was located. Checker:
`/tmp/harmonia-taskoutcome-doc-check.py`, bounded by 30 seconds with five-second
kill grace. Mermaid fenced blocks were checked for nonempty content and the
changed conceptual diagram was reviewed; no Mermaid renderer was invoked.

**PASS — Markdown/local-link/incoming-link, balanced-fence, legacy-preservation
and scope checks; PASS — `git diff --check`.** The ten changed RADS documents
and this report are covered. The final semantic review establishes all eleven
requested outcomes and confirms that no affirmative recursive TaskOutcome
model statement remains in RADS. Only Domain04 documentation and this report
changed; Domain01–03, implementation code, tests and Domain05 remain unchanged.

## Canonical Documentation Assessment

1. **Relevant material outside RADS:** accepted ADRs and legacy execution-model
   context were found. No separate generic TaskOutcome definition or recursive
   TaskOutcome model was found in legacy architectural documentation.
2. **Sources:** [architecture-decisions.md](../../docs/architecture-decisions.md)
   and [architecture/execution-model.md](../../docs/architecture/execution-model.md).
   The preceding `.junie` report is historical execution evidence only.
3. **Knowledge contained:** model/security/evidence/processing responsibility
   decisions, runtime execution/checkpoint and worker-pool descriptions, and
   the already superseded general runtime-AI position. These do not determine
   the refined generic Task information semantics.
4. **Canonical ownership:** the adjudicated work/undertaking/outcome semantics
   and orthogonal information controls belong in Domain04. Broader ADR
   canonicalisation belongs to governance; execution and runtime allocation
   require separately authorised downstream architecture. Assurance and
   accountability interpretation remain a later review.
5. **Incorporated by this task:** the explicit human TaskOutcome refinement is
   fully represented in RADS and does not depend on the preceding report or
   legacy implementation descriptions. No additional legacy knowledge was
   migrated; the canonical semantic-agnosticism/Ergo/runtime-AI boundary was
   retained and applied to outputs.
6. **Future reconciliation:** remaining execution-model/ADR dependencies,
   Task-to-runtime mappings, the retained AX-05 legacy tension, detailed Task
   structures and ReportedTask require separately bounded work. This task
   does not authorise it or satisfy the Domain04/final programme completion
   gates. **Domain04 completion is not recommended on this refinement alone.**
