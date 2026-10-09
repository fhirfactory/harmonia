# Domain04 — Bounded Task Information Reconciliation

**Date:** 2026-10-10

**Status:** The authorised bounded reconciliation is complete; Domain04 and
the wider Task / Work family are not declared complete or frozen/refrozen.

**Authority:** This is an execution/completion report, not architectural
authority. The supplied human adjudications govern the bounded change; the
maintained semantics are in RADS.

## Authority and Context Reviewed

| Authoritative source reviewed | Relevant input |
| :--- | :--- |
| [Repository AGENTS.md](../../AGENTS.md) and [docs/AGENTS.md](../../docs/AGENTS.md) | Axiom authority, progressive context loading, RADS/legacy reconciliation and preservation, bounded verification and mandatory architecture suite. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md) | Programme Domain04 reconciliation boundary, semantic migration, canonical-corpus assessment and completion gates; no Domain04 completion or Domain05 authorisation. |
| [Canonical Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md) | AX-01, AX-04, AX-06–08, AX-12, AX-14–18 materially govern health-information purpose, platform semantics, authority/security/evidence, developer information access, distinctions, operational/architectural uncertainty, observability and domain authority. AX-05 also governs the residual legacy persistence tension below. |
| [Foundational requirements](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md) | Relevant REQ-FND-002 observable progression, REQ-FND-004 indeterminate outcomes and REQ-FND-003 non-MPI boundary. No requirement was changed. |
| [Strategy logical component responsibilities](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md) | Existing Calliope semantic governance and Digital Twin identity/coordination boundary; no allocation or Twin redesign. |
| [Domain03 metamodel §3.7](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#37-clinical-work-integration-boundary) | Work Order = human doing, To Do = human review/update/approval, business Task = synthetic work; clinical-work authority remains external unless explicitly assigned. |
| [Domain03 Workflow & Activity Coordination](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md#29-workflow--activity-coordination) | Three Business classifications, coordination Functions/Services, generic progression/timer responsibilities and outcome uncertainty. |
| [Domain03 Processes §§5.14–5.16](../../docs/markdown/03-business-architecture/processes/business-processes.md#514-work-order-progression-process) | Work Order, To Do and Synthetic Task progression meanings; retained To Do dismissal/delegation and Synthetic Task stalled/failed uncertainties. |
| [Domain03 information responsibility](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) | Generic Activity Instance Registry & Timers; coordination does not own business meaning or clinical outcome. |
| [Domain04 overview](../../docs/markdown/04-information-architecture/README.md), [metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) and [family index](../../docs/markdown/04-information-architecture/information-families/README.md) | Activity/Definition categories, conceptual/representation separation and existing domain/family standing. |
| [Definition-to-Accountability pattern](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md), [modelling guardrails](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md), [containment pattern](../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md) and [lifecycle guidance](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md) | Earlier Task definitions, ReportedTask placement, stage/identity/lifecycle/containment assumptions requiring bounded qualification. |
| [Domain03 traceability](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md), [assemblies/views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md) and [Dokimasia finding §4.4](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md#44-domain04--information-architecture) | Responsibility derivation, the existing illustrative Operational Work Context and the earlier accountable-report assumption. |
| Explicit Task adjudications supplied for this task | Authority for the four Task concepts, established relationships, semantic-agnosticism/Ergo/AI boundaries, reserved TaskOutcome questions and unresolved ReportedTask. |

[Accepted ADR material](../../docs/architecture-decisions.md), especially
ADR-004, ADR-007 and ADR-013–015, was also consulted in its repository-assigned
decision role. It provides no generic Task-to-Pragma mapping and was not used
to reverse-derive the model. Its external location remains a separate
canonicalisation dependency; this task does not migrate those decisions.
Previous execution reports were consulted only to locate documentation-check
tooling and the bounded offline architecture command, not as architectural
authority. No implementation classes or framework structures supplied the
Task semantics.

## RADS Files Changed

| File | Change |
| :--- | :--- |
| [information-families/task-work.md](../../docs/markdown/04-information-architecture/information-families/task-work.md) — new | Maintained bounded Task meanings, agreed relationship diagram, Domain03 responsibility basis and full reserved-question list. |
| [guardrails/observable-information-and-domain-meaning.md](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md) — new | Maintained semantic-agnosticism, conceptual Ergo execution boundary, optional runtime-AI position and development/runtime distinction. |
| [patterns/definition-to-accountability.md](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md) | Replaces the ambiguous Task example; retains ReportedTask as unresolved and qualifies general pattern assumptions. Healthcare Service example unchanged. |
| [metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) | Activity is information representing an undertaking; Definition example is ActionableTaskArchetype; links the bounded Task and comprehension boundaries. |
| [guardrails/modelling-guardrails.md](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md) | Preserves Business classifications without Information subclasses; qualifies Task containment, stage identity/lifecycle and undertaking-completion assumptions. The sixteen guardrails remain sixteen. |
| [governance/information-lifecycle.md](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md) | Execution illustration concerns FulfillmentTask, does not impose a universal transition model or settle work satisfaction/outcome/ReportedTask lifecycle. |
| [traceability/domain03-traceability.md](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md) | Records existing generic coordination responsibility basis without allocating unestablished definition/outcome authority. |
| [reviews/dokimasia-assurance-architectural-finding.md](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md) | Updates only the Task row in §4.4 so its earlier ReportedTask interpretation is not presented as reaffirmed. |
| [Domain04 README](../../docs/markdown/04-information-architecture/README.md) | Navigates the new model/boundary and records bounded standing. |
| [Information Families README](../../docs/markdown/04-information-architecture/information-families/README.md) | Records this separate bounded reconciliation without commencing/closing the wider G3 or later family packages. |

## Exact Task Semantic Changes

1. **ActionableTaskArchetype:** introduces the reusable work definition,
   including applicable nature, parameters, constraints, prerequisites,
   performer requirements, satisfaction expectations and content expectations.
   It is not work instantiated. Content expectations require neither a
   structural representation nor Platform domain comprehension.
2. **ActionableTask:** replaces the previous definition/template meaning with
   an identifiable particular thing-to-be-done, instantiated from / governed
   by an applicable archetype. It is not undertaking/execution.
3. **FulfillmentTask:** replaces “instantiation of the definition” with an
   identifiable undertaking of an ActionableTask. Applicable execution
   information includes actor, context, timing, progression, state,
   completion/failure/suspension and execution scaffolding. An ActionableTask
   may have zero, one or multiple undertakings, including concurrent ones
   where work semantics permit/require. Execution state is not collapsed into
   the work instance; one undertaking's completion does not inherently satisfy
   its parent work.
4. **TaskOutcome:** replaces the previous “actual physical/clinical result”
   wording with information produced or established as an outcome of work.
   Association may be with ActionableTask or FulfillmentTask; TaskOutcome may
   contain/encapsulate other TaskOutcomes; encapsulated content may be opaque.
5. **Relationships:** the diagram records archetype → work instance → zero or
   more undertakings → possible outcome information, the additional
   ActionableTask–TaskOutcome association, and zero or more contained outcomes.
   It adds no other mandatory multiplicity, storage, inheritance or output
   obligation.

**TaskOutcome was not elaborated beyond the authorised boundary.** Detailed
composition; composition versus aggregation/reference/other containment;
identity; further cardinalities; the relationship between work-level outcomes
and collections of undertaking outcomes; mandatory outcomes; and detailed
outcome lifecycle/state all remain expressly reserved in the maintained model.
General metamodel, pattern, containment and lifecycle guidance cannot settle
them by inference.

**ReportedTask remains unresolved.** Its existing Definition →
Contextualisation → Fulfilment → Outcome → Accountability intersection is
retained as an uncertainty. Its necessity, meaning and potential overlap with
accountability, assurance, audit, history and evidence were not adjudicated,
removed or normalised. Earlier immutability/accountable-report language is
identified as unconfirmed; no ReportedTask identity, lifecycle or relationship
is established by this task.

## Business Classifications and Semantic Boundary

Domain03 Work Order, To Do and Synthetic Task retain their distinct meanings
and progression authority. The generic Task information semantics must
represent work arising from each; they do not broaden Domain03's business
“Task = synthetic task” term. Coordination owns its existing generic
progression/timer responsibility, not clinical decisions, originating facts or
the business meaning/outcome of the work. No specialised archetypes,
FulfillmentTask subclasses or TaskOutcome subclasses were introduced.
Domains01–03 were unchanged.

The maintained [semantic-agnosticism boundary §2](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md#2-observability-and-encapsulated-domain-meaning)
bounds Harmonia's concept of reality by available observation. It permits
observing, carrying, associating, applying, appending, transforming, persisting,
routing, forwarding and responding to information through established models,
rules, Behaviours, Praxis and defined logic without intrinsic inference or
comprehension of all encapsulated domain meaning. Harmonia retains its own
governed architectural semantics.

[§3](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md#3-governed-ergo-logic-boundary)
records Context Loading → Ergo Logic Process → Context Unloading solely as a
conceptual governed boundary. Defined logic may intentionally inspect or
interpret known content, apply rules, transform/map/append, invoke domain or AI
services, calculate and produce outputs. It creates no new execution machinery.

[§§4–4.1](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md#4-runtime-ai-as-an-optional-adjunct)
semantically incorporate the existing runtime-AI position: AI is an optional
adjunct intentionally invoked by developer-defined Ergo logic. It alters no
Ergo/Praxis/Twin responsibility, grants no architectural authority, defines no
Twin identity and makes neither AI nor semantic comprehension intrinsic to
Harmonia. Runtime invocation remains distinct from AI-assisted development.
No AI orchestration, governance mechanism, capability allocation or
implementation was created.

## Legacy-Doco Reviewed and Supersession

| Legacy source / inspected scope | Disposition |
| :--- | :--- |
| [Execution model](../../docs/architecture/execution-model.md), including hierarchy, state/checkpointing and runtime-AI §§6–6.2 | General §§6–6.1 AI meaning incorporated and wrapped; hierarchy/state/checkpoint implementation detail and assurance-specific §6.2 unchanged. |
| [Pragma](../../docs/concepts/pragma.md), [Praxis](../../docs/concepts/praxis.md), [Ergon](../../docs/concepts/ergon.md), [Energeia workflow](../../docs/concepts/energeia-workflow.md), and relevant [Ponos](../../docs/concepts/ponos.md) definition/execution description | Execution envelopes, workflow blueprints, actor logic and runtime machinery are reconciliation input. No equality with the new Task concepts is established; these sources remain unchanged. |
| Relevant Task/Pragma/Praxis model and module references in [Calliope](../../docs/modules/calliope.md) and [Energeia](../../docs/modules/energeia.md) | Implementation/reference detail remains supporting material; no generic Task semantics inferred or implementation material superseded. |
| [Dokimasia orientation](../../docs/modules/dokimasia.md), especially Runtime AI | Corroborates adjunct/optional AI and contains assurance-specific consumer expectations. The general position is now canonical; assurance-specific meaning/allocation is not migrated or endorsed by this task. Original document unchanged. |
| Relevant [LaTeX workflow chapter](../../docs/latex/chapters/06-workflow-energeia.tex) and [Ergon appendix](../../docs/latex/chapters/appendix-ergon-module.tex) excerpts | Legacy Pragma instance/payload/runtime descriptions remain implementation input. No definition-versus-instance mapping was imported or marked superseded. |
| [Architecture decisions](../../docs/architecture-decisions.md), relevant ADR-004/007/013–015 | Retain their defined repository decision role and separate canonicalisation dependency; no ADR migration or execution redesign. |

One wrapper surrounds exactly **execution-model §6's original heading/body
and §6.1**, ending before §6.2. Its specific authoritative replacements are:

- [Domain04 §4 — Runtime AI as an Optional Adjunct](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md#4-runtime-ai-as-an-optional-adjunct).
- [Domain04 §4.1 — Runtime AI and AI-Assisted Development](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md#41-runtime-ai-and-ai-assisted-development).

The established Start / Superseded by / Finish mechanism is explicit. The
wrapped **3,185 original bytes** are identical to the task baseline. Removing
the wrapper and added authority/navigation notice exactly reconstructs the
whole original document, including all implementation sections and §6.2.

No legacy section separately defining the four newly adjudicated generic Task
concepts was located. Pragma/Praxis definitions are different execution
concerns, so their architectural meaning was not claimed as migrated merely
through lexical similarity. No Task-to-Pragma/Praxis mapping or broader legacy
supersession was invented.

## Conflicts and Retained Uncertainty

- The prior RADS Task-definition/instance conflation, “actual result” outcome
  wording and assumed ReportedTask accountable-report meaning were addressed
  through the explicit authorised decisions, rather than inferred repair.
- All deferred TaskOutcome and ReportedTask questions above remain under
  AX-17. Archetype-authoring/outcome-authority ownership beyond the established
  coordination responsibility basis remains unallocated.
- Domain03 To Do dismissal/delegation and Synthetic Task stalled/failed
  progression ordering remain unresolved at source; no transitions were
  invented to make the generic information model appear complete.
- Legacy Pragma/Praxis/task-state descriptions do not establish generic Task
  equivalence. Their mapping and broader execution-model reconciliation remain
  future work. Assurance-specific AI expectations in execution-model §6.2 and
  Dokimasia remain outside this adjudication, as does Dokimasia/Ponos execution
  allocation.
- The legacy execution model's §4 “Durability” description of Mneme writes and
  asynchronous Mnemosyne write-behind requires separate reconciliation with
  AX-05; it cannot establish cache state as authoritative durable truth here.
  The inspected LaTeX workflow chapter's EMPI/master-identity example conflicts
  with REQ-FND-003's non-MPI boundary. Both original legacy statements were
  preserved; no implementation behaviour was inspected or inferred from them.
  These residual legacy issues do not determine the four authorised concepts.

## Validation

The mandatory architecture command ran offline using cached dependencies,
with a five-minute TERM bound and ten-second forced-termination grace:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS — BUILD SUCCESS; 90 tests; 0 failures; 0 errors; 0 skipped; 11 classes;
command exit 0; Maven total time 27.425 seconds.** No timeout/stall occurred.
All eight architecture classes named by AGENTS.md completed, together with
GovernedWriteContractArchitectureTest, GovernedWriteCompositionArchitectureTest
and MnemosyneAuthoritativePersistenceArchitectureTest. Surefire XML results
were inspected; the execution log is `/tmp/harmonia-task-architecture-tests.log`.

ArchUnit reports unsupported Java 25 JDK class-file major version 69 and falls
back when importing affected JDK classes; Maven/Guava also emits deprecated
`sun.misc.Unsafe` warnings. Assertions passed with that environmental import
limitation. The suite checks existing implementation guardrails, not the
information model's semantic sufficiency.

Documentation verification uses the available Markdown-it-py CommonMark
parser with tables, rendered HTML anchors, GitHub heading slugs and local
target/incoming-link checks. No packaged repository Markdown/link validator
was found. The task checker at `/tmp/harmonia-task-doc-check.py` runs with a
30-second TERM bound and five-second kill grace. It verifies all changed/new
documentation, balanced fences, the exact legacy preservation described above,
and changed-file scope. Existing whitespace outside the change is retained;
`git diff --check` checks introduced whitespace. Semantic review compares the
final model to every supplied adjudication and reserved question.

**PASS — documentation/link, legacy-preservation, semantic-scope and whitespace
checks.** The final check covered 12 documentation files, 176 local links
(including 95 new/changed links) and 165 incoming links. No broken file/anchor
link was found in that scope; all fences were balanced. Both worktree and
staged whitespace checks passed. The 53 protected Domain01–03/governance/user-modified baseline files
remain byte-identical. The five pre-existing modified files (root/docs agent
instructions, docs README, Completion Plan and governance README) were
preserved. Only the ten RADS files, one legacy file and this report belong to
this task; no production code, test implementation, Domain05 allocation,
Praxis/Pragma/Ponos/Ergo/Twin redesign or domain baseline/status change occurred.

## Canonical Documentation Assessment

1. **Relevant architecture outside RADS was found.** Sources are the legacy
   execution model, concept/module references, Dokimasia orientation, accepted
   ADR material and LaTeX excerpts listed above.
2. **Knowledge they contain:** optional adjunct runtime AI and its
   development/runtime distinction; workflow-blueprint/execution-envelope/
   activity-logic/runtime descriptions; assurance-specific AI expectations;
   processing authority/durable-transition decisions and implementation detail.
3. **Canonical ownership:** the general information-comprehension and
   developer-logic/AI responsibility boundary belongs in Domain04's modelling
   guardrails. Further execution/application allocation belongs to separately
   governed downstream architecture and ADR/governance reconciliation; detailed
   assurance interpretation requires its own authorised adjudication.
4. **Incorporated by this authorised task:** the general runtime-AI position
   and development/runtime distinction are now sufficiently represented in
   Domain04 §§4–4.1, integrated with the newly authorised semantic-agnosticism
   and conceptual Ergo boundary. The four Task meanings are canonical through
   explicit human decisions, not reverse-derived from execution documentation.
5. **Not incorporated:** full Praxis/Pragma/Ponos/Ergon execution architecture,
   their generic Task mapping, assurance-specific AI expectations/allocation,
   remaining ADR knowledge and conflicting legacy examples. Useful
   implementation detail is deliberately retained as supporting material and
   does not acquire RADS authority.
6. **Future reconciliation:** separately adjudicate ReportedTask and the
   reserved TaskOutcome questions; reconcile remaining execution/assurance/ADR
   knowledge and the recorded legacy tensions into the appropriate canonical
   domain when authorised. This task neither commences that work nor
   recommends Domain04 completion. The programme's final canonical-corpus and
   domain-completion gates remain outstanding.
