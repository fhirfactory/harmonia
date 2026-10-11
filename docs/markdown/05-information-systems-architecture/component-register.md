<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Component and Construct Register

**Standing: Harmonia Candidate Information Systems Architecture.**
[Orientation](README.md), [system map](candidate-system-map.md) and
[source dispositions](sources.md) govern this register. It includes significant
behavioural and data constructs as well as software components; inclusion is
not a claim that every row is an independent component or required release feature.
Primary responsibility, feature allocation and information/persistence are
read together with the [feature](candidate-feature-map.md) and
[data](data-and-persistence.md) views.

## Semantic, policy and evidence responsibilities

<a id="c01"></a>

### C01 — Calliope

- **Role/responsibility:** semantic definition and conformance responsibility
  centre; shared canonical models, terminology bindings and conversion support.
  [A01/A02](sources.md#a02), [L06/L07](sources.md#l06).
- **Features/contained constructs:** canonical event/payload/reason definitions,
  topic constants, schemas and HL7 ADT/MFN/ORM/ORU-to-FHIR converters. Published
  model/profile/binding/concept-map material supports local runtime consumption.
- **Information:** defines contracts for `ErgonEvent`, `ErgonPayload`, reasons,
  topics and resource mappings; definition authority does not confer authority
  over originating clinical assertions. Terminology/reference object persistence
  and runtime service allocation are unknown (CDG-24).
- **Persistence:** no durable clinical persistence responsibility. Compiled/shared
  reference artefacts are documented; a runtime reference repository is not defined.
- **Collaboration/exclusions:** definitions consumed by Themis, Mneme, execution
  and Pylai; not a synchronous mandatory mediator, workflow engine, security
  evaluator, broker or persistence connection manager.
- **Standing:** established high-level RADS responsibility; reconstructed detailed
  features remain candidate. Converter library versus invoked activity allocation
  is incomplete, CDG-05; this register does not select a terminology product.

<a id="c02"></a>

### C02 — Themis

- **Role/responsibility:** policy evaluation and default-deny authorization for
  governed operations. Trusted identity is established by authentication
  boundaries; Themis evaluates permission. [A01](sources.md#a01), [L08/L13](sources.md#l08).
- **Features/contained constructs:** policy contracts/evaluator, principal and
  service-identity models, roles/authorities/security-label definitions, domain
  policies and security-decision evidence production.
- **Information:** authorization request/decision/reason, security context,
  principal, roles/authorities and `ThemisAuditEvent`. Context is operational
  information and does not automatically become persisted resource content.
- **Persistence:** durable policy/identity administration storage is unknown.
  Produces security-significant evidence according to policy; Kleio preserves
  evidence, with durable storage support distinct from evaluation.
- **Collaboration/exclusions:** participates at information, activity, collaboration
  and publication boundaries. Does not own UI, transport, execution workers or
  relational persistence merely by authorizing them.
- **Standing:** high-level responsibility established; fixed four-gate variants
  are historical designs, not an exhaustive Domain05 topology. CDG-13–15/30
  retain evidence/context/enforcement differences.

<a id="c03"></a>

### C03 — Kleio

- **Role/responsibility:** preserves meaningful immutable audit evidence and
  governed provenance. [A01 AX-08](sources.md#a01), [L13 ADR-013/016](sources.md#l13).
- **Features/contained constructs:** acceptance/preservation of append-only
  evidence concerning significant actions, information and authority transitions,
  external dispatch and deliberate replay where policy requires it. Internal
  subcomponent hierarchy and evidence-access services are unknown.
- **Information:** AuditEvents, provenance and links to significant transitions/
  original processing attempts; not every implementation checkpoint or policy
  evaluation is durable evidence. Dokimasia's governed assurance record/result
  is authoritative evidence of its assurance activity; no separate AuditEvent
  for its own execution is generated ([terminal rules](#dokimasia-terminal-boundaries)).
- **Persistence:** evidence permanence/immutability is documented; exact repository,
  schema and collaboration with Mnemosyne are incomplete. Historical FHIR and
  operations allocations compete (CDG-13/14).
- **Collaboration/exclusions:** receives evidence from Themis, Pylai, execution,
  Iris and other governed producers. Evidence custody does not allocate
  independent assurance evaluation, work execution or subject management.
- **Standing:** high-level RADS responsibility plus ADR candidate detail; not
  rejected because the legacy subproject inventory omits its name.

## Activity and execution responsibilities within Energeia

Energeia is a **discrete Harmonia Software Component responsible for management
of Harmonia's operational activity**, under [U06](sources.md#u06). Its direct
Praxis/Journey management and operational observation/monitoring/reporting
responsibilities justify that boundary. Ponos, Ergo, Ergon, Erga and specialised
execution/activity constructs remain **associated** with Energeia. Their
Specialisation, Realisation, Aggregation and execution relationships are explicit
in the [system map](candidate-system-map.md#specialisation-and-reuse); no Composition,
programming-language containment, process or deployment follows from association.
Pragma information management remains with Mneme.

<a id="c39"></a>

### C39 — Energeia

- **Role/responsibility:** discrete Software Component owning Harmonia operational
  management: “Is Harmonia itself operating as expected?” It observes work-to-be-done,
  work-being-done and work-done, outstanding ActionableTasks, FulfillmentTask
  activity, Journey progression, stalled/incomplete activity, Praxis context,
  abnormal behaviour and operational information for reporting/assurance.
- **Candidate services/features:** **Praxis Management Services** (F11) and
  **Journey Management Services** (F38), plus operational observation, monitoring
  and reporting (F39). Detailed Feature/Service/internal-construct/subordinate-
  Component decomposition remains unresolved (CDG-39); service-set names do not
  manufacture independently deployable components.
- **Information:** owns Praxis C06 and Journey C35 at the application level.
  Mneme manages their governed information and retains ActionableTask,
  FulfillmentTask and Pragma management. Observing operational significance does
  not transfer management of that information or referenced clinical facts.
- **Persistence:** Mnemosyne C&G provides Praxis/configuration durability;
  Mnemosyne O&A provides Journey/operational-assurance durability, through Mneme
  governed management. Energeia application ownership is distinct from durable
  persistence responsibility; physical structures remain unresolved.
- **Collaboration/relationships:** associated execution/activity responsibilities
  retain U02 specialisation/reuse. Praxis aggregates Pathway [0..1] and has a
  working Aggregation relationship to Journey with cardinalities unresolved.
  Dokimasia uses Praxis as assurance basis and Journey as observed evidence
  subject, preserving independence and terminal rules. Iris presents operational
  views; presentation does not own Harmonia operational management.
- **Release boundary:** for **R1.x/R2.x, Observe → Monitor → Report**. Energeia
  SHALL NOT autonomously change execution, redistribute workload, modify Routes,
  Pathways, Pub/Sub or Praxis, intervene in resilience or adjust capacity merely
  from workload, failure, Journey, capacity, assurance or anomaly observations.
  Existing explicitly governed execution/recovery behaviour retains its own
  authority; observation grants no new adaptive authority.
- **Separate management plane:** external IT/Ops manages infrastructure, network,
  containers/Kubernetes, JVM/runtime, databases, technical health/capacity and
  platform resilience. Energeia manages/observes Harmonia as Harmonia. This
  distinction selects no deployment topology or infrastructure integration.
- **Deferred direction:** **R3 Adaptive Load and Resilience Service** (F40):
  Observe → Analyse → Decide → Adapt, with feedback to observation. R1.x/R2.x
  observability should permit later capability without embedding it now.
  Policy/control loops, algorithms, mechanisms, infrastructure integration and
  AI decision architecture are deliberately undesigned (CDG-41).
- **Standing:** U06 adjudicates these candidate responsibilities; whole-candidate
  acceptance and Step B remain pending. Possible upstream omissions require
  later review, without deriving Business Architecture or capability demand here.

<a id="c04"></a>

### C04 — Ponos

- **Role/responsibility:** reusable execution framework/component pattern within
  Energeia for governed execution and observable activity progression.
  [U02](sources.md#u02) adjudicates this role; [A02](sources.md#a02) and
  [L02/L01/L23](sources.md#l02) retain responsibility context and historical detail.
- **Features/contained constructs:** work ingress and dispatcher, workflow/sequence
  resolution where governed control is established, activity invocation, execution
  concurrency/timeouts, execution retry/re-attempts, checkpoint coordination and
  administrative inspection. Subscription matching belongs to Mneme; handoff
  redelivery/recovery and dead-letter semantics belong to Petasos.
  Consumer loops/thread pools are documented mechanisms, not logical children.
- **Information:** executes consumer undertakings using governed information/context
  resolved through Mneme; consults established control definitions and produces
  execution/progression observations contributing to Journey. Petasos handoff
  does not make an opaque payload into a Ponos-owned Pragma.
- **Persistence:** coordinates recoverable handoff through Petasos and governed
  information change through Mneme/Mnemosyne. Does not establish durable state
  by completing an activity or writing a checkpoint to a cache.
- **Collaboration/exclusions:** tightly coupled to peer Ergo/Ergon responsibilities,
  executing/managing bounded Ergon activity conforming to the Ergo contract;
  does not structurally contain Ergo or Ergon. Uses Praxis, Erga, Themis, Mneme
  Pragma Management and Petasos. DigitalTwin C19 and Dokimasia C20 specialise/
  reuse this pattern; they are not children inside a Ponos runtime. Conceptual
  supertype language does not mandate literal implementation inheritance.
  Does not own Pragma information management, Publishing/Subscription/Pattern
  semantic selection, workflow definitions, concrete domain logic, broker lifecycle,
  external standards/transport or universal clinical-work determination.
- **Standing:** specialisation relationships adjudicated for the candidate by U02;
  detailed model remains candidate. CDG-03/08/16–18 retain incomplete control
  and continuation semantics. Numerical worker/retry defaults are not baselined.

<a id="c05"></a>

### C05 — Ergo

- **Role/responsibility:** reusable conceptual activity pattern and
  platform-integration contract for conforming Ergon, within Energeia.
  [U02](sources.md#u02), preserving [A05](sources.md#a05).
- **Features/contract:** Context Loading → defined logic → Context Unloading;
  common process-management, Petasos adapters/platform integration, assurance
  integration, logging/observability, reporting, checkpoint/platform integration
  and other common activity facilities. Integration points are architectural
  contracts, not selected APIs or technologies.
- **Information/persistence:** governs the activity boundary around information
  use/production; Mneme manages information and Mnemosyne establishes durable
  state. No independent raw database or universal transaction boundary is assigned.
- **Collaboration/exclusions:** tightly coupled to Ponos execution, realised/
  reused by Ergon and its specialisations, using Calliope/Themis/Mneme/Petasos.
  Ergo is not the execution framework, an Erga collection or a synonym for Ergon.
- **Standing:** responsibility distinction adjudicated by U02; detailed contracts
  remain candidate. C23/C24 now separately record Ergon and Erga; the prior C05
  combined entry is not retained as a single component.

<a id="c23"></a>

### C23 — Ergon

- **Role/responsibility:** a single well-defined software component encapsulating
  a bounded fine-grained task-completion activity set within the Ergo contract,
  executed/managed through Ponos. [U01/U02](sources.md#u02), [L03/L19](sources.md#l03).
- **Features:** specialised transformation/mapping, validation, identifier/reference
  checking, enrichment, routing, result processing, fan-out or registry-change
  behaviour. Reuses C05 common facilities; `ErgonBase` is historical contract
  support, not an architectural inheritance requirement.
- **Information:** acts upon or generates Pragma, payload/resource information,
  outputs, checkpoints and observations through governed contracts. **No discrete
  Information Architecture representation or Ergon Information Resource exists
  merely because the component is an Ergon.** Activity is not information about it.
- **Persistence/collaboration:** requests governed information use/change through
  Mneme; does not own Pragma management or durable state. Erga collects components;
  Ponos executes them; Petasos effects persistent handoff. Every Ergon exposes
  IngressEndpoint and EgressEndpoint C29; traversal does not expose internal logic.
  Praxis relationship detail remains open.
- **Specialisations:** Behaviour C25, Dokimasia Ergon C26 and, through the latter,
  AI Assurance Ergon C27. They retain normal Ergo/Ergon facilities, constrained
  by the specialised role and Dokimasia's terminal rules where applicable.
- **Standing/exclusions:** bounded behaviour established for the candidate;
  competing atomic/pure/idempotent/branching claims remain CDG-05. An ordinary
  Ergon may intentionally invoke an AI service internally as an implementation
  choice without becoming AI Assurance Ergon or transferring authority.

<a id="c24"></a>

### C24 — Erga

- **Role/responsibility:** meaningful collections of Ergon within Energeia,
  distinct from the Ergo contract, an individual Ergon or the Ponos execution
  framework. [U02](sources.md#u02).
- **Features:** executable assembly/hubs; management, capture and observation of
  multiple concurrent Ergon, including concurrent instances of the same type;
  libraries of conceptually related Ergon with coherent change management,
  testing, versioning and release constraints. This is more than an informal
  plural or an arbitrary list of Ergon.
- **Information/persistence:** assembly/library and concurrent activity information
  are relevant concerns; detailed Data Objects, identity and lifecycle are not
  established here. Collection does not confer information-management or durable
  persistence responsibility for the information its activities use.
- **Collaboration/exclusions:** collects Ergon conforming to Ergo and collaborates
  with Ponos execution and common platform management/observation. Full Praxis
  composition relationships remain for separate adjudication; no new engine,
  deployment hub or collection-wide transaction boundary is prescribed.
- **Standing:** distinct candidate responsibility adjudicated by U02; detailed
  assembly/library contracts remain unfinalised.

<a id="c25"></a>

### C25 — Behaviour

- **Role/features:** Ergon specialisation for DigitalTwin operation. It inherits/
  reuses Context Loading and Context Unloading, process-management mechanisms,
  Petasos/platform integration, assurance integration, logging/observability,
  reporting, checkpoint/platform integration and other common Ergo/Ergon facilities.
  [U02](sources.md#u02), [common specialisation principle](README.md#specialisation-principle).
- **Information/persistence/collaboration:** operates on governed entity context
  and candidate Stimulus/Effector Pragma representations within DigitalTwin's
  entity-specific execution; Mneme manages information, Mnemosyne establishes
  durable state and Petasos handles persistent/recoverable handoff.
- **Standing/exclusions:** discrete candidate activity specialisation, not an
  independent activity framework parallel to Ergo/Ergon. DigitalTwin adds only
  genuinely Twin-specific behaviour instead of duplicating platform mechanisms;
  Behaviour acquires no standalone Information Resource merely by being activity.

<a id="c26"></a>

### C26 — Dokimasia Ergon

- **Role/features:** discrete Ergon specialisation representing a bounded assurance
  activity executed within a Dokimasia workflow. Reuses normal Ergo/Ergon
  execution, Context Loading/Unloading, process-management, integration,
  checkpoint, logging/observability and reporting contracts. [U02](sources.md#u02).
- **Information/persistence/collaboration:** uses governed assurance input,
  applicable basis and evidence, and may produce assurance information through
  Mneme Pragma Management. No detailed schema, evidence-access API, storage or
  additional Information Resource is inferred. Executed through Dokimasia's
  specialised Ponos model.
- **Standing/exclusions:** discrete candidate component responsibility established;
  subject to [Dokimasia terminal constraints](#dokimasia-terminal-boundaries).
  AI Assurance Ergon C27 specialises this component. Other potential assurance
  classifications, including deterministic/human subclasses, require adjudication.

<a id="c27"></a>

### C27 — AI Assurance Ergon

- **Role/features:** candidate Dokimasia Ergon specialisation where AI-based
  processing is itself an explicit governed participant in Dokimasia workflow.
  Its existence and discrete responsibility are established by [U02](sources.md#u02);
  its detailed Information Systems architecture is not finalised.
- **Distinction:** ordinary Ergon may call AI internally as an implementation
  choice. AI Assurance Ergon makes that AI activity an explicit governed assurance
  workflow activity. This allocates neither every use of AI to this component
  nor a mandatory AI step to every Dokimasia workflow.
- **Information/persistence/collaboration:** inherits C26/C23/C05 contracts and
  common Pragma Management, process-management, platform integration and reporting.
  AI invocation confers no originating domain or assurance-determination authority
  on the service/model. Context, disclosure, results, provenance/evidence and
  detailed workflow interactions require [CDG-35 discovery](gaps.md#dokimasia-ai-discovery).
- **Standing/exclusions:** established component boundary with candidate detailed
  design; no LLM, agent framework, SDK, model, provider or service technology
  selected. Discovery precedes final implementation modelling. Inherits both
  Dokimasia terminal rules; unresolved design does not invalidate its boundary.

<a id="c06"></a>

### C06 — Praxis

- **Role/ownership:** higher-order operational construct defining expected/permitted
  context and the operational/assurance envelope. **Energeia C39 owns Praxis**
  and provides **Praxis Management Services F11** under [U06](sources.md#u06).
  It is not another execution engine or necessarily an independent component.
- **Information/relationships:** candidate interface configuration, Topic exclusions
  and potentially inclusions, operational constraints/configuration and assurance
  rules. **Aggregation: Pathway [0..1]**; working **Aggregation: Journey** with
  cardinalities unresolved. No Composition or shared lifecycle is inferred.
  Praxis may exist without a Pathway for Publish/Subscribe activity.
- **Management/persistence:** Mneme manages governed representations; Mnemosyne
  **C&G** provides durable persistence responsibility. Ponos consumes Praxis for
  execution and Dokimasia uses it as the primary normative assurance basis;
  neither use nor persistence transfers application ownership.
- **Retained detail:** schema, lifecycle/versioning, activation/deactivation,
  replacement/retirement and in-flight effects remain CDG-01/06. Historical
  TaskSequence/PraxisDefinition/PraxisImplementation and A04 actual-assurance-
  activity descriptions do not establish equivalence or a definition-to-instance
  model. U06 supersedes the earlier wholly unresolved broader-Praxis standing.
- **Exclusions:** Praxis is not Pathway, Route, a sequence of Ergo/Ergon, a runtime
  engine or a Pub/Sub graph. Mneme semantic selection, Petasos concrete persistent
  handoff and Ponos execution remain distinct. No detailed Praxis schema is invented.

## Information management, preservation and middleware

<a id="c28"></a>

### C28 — Pragma Management

- **Role/responsibility:** Mneme-associated information-management responsibility
  area with logical Aggregation of PragmaFactory C08 and Publishing C36,
  Subscription C37, Pattern C38 responsibilities, managing Pragma C07.
  [U02/U03/U05](sources.md#u05). Aggregation/management identifies responsibility,
  not Composition or a required process/service.
- **Features/information:** creation/initialisation through PragmaFactory and
  governed management of Pragma representations and common process-management,
  assurance, reporting and platform integration associated with that information.
  Publishing/Subscription definitions, Pattern semantics/evaluation, matching,
  selection, semantic eligibility and eligible recipient/consumer relationships
  belong here; concrete persistent destination/handoff mechanics remain Petasos.
  Stimulus/Effector and candidate Dokimasia Pragma semantic roles
  reuse these facilities instead of requiring specialist equivalents.
- **Persistence/collaboration:** active use/coordination through Mneme; authoritative
  durable establishment through Mnemosyne. Ponos executes, Petasos provides
  messaging/recoverable handoff and Ergon supplies bounded activity behaviour.
- **Standing/exclusions:** candidate responsibility allocations established by
  U02/U03/U05;
  detailed management contracts and persistence structures remain unresolved.
  Execution of, behaviour upon, or handoff of managed information does not
  confer information-management responsibility for that information.

<a id="c07"></a>

### C07 — Pragma

- **Role/responsibility:** managed information / Application Data Object under
  Mneme Pragma Management C28, used for task/context/progression representation.
  It is not structurally in Energeia merely because execution operates on it.
  [U01/U02](sources.md#u02); [L05/L01/L19](sources.md#l05) retain carrier provenance.
- **Features/contained constructs:** identity/correlation/causation, inputs/outputs
  or payload references, context, state/checkpoints, parent association and
  governed observations concerning destination delivery. Operational handoff/
  delivery obligation state belongs to Petasos; exact observation relationships
  remain unresolved (CDG-18). Every Pragma contains or references a payload such
  as an Information Unit; metadata/Container/Content distinctions are described
  in the [semantic shape](pathways-journeys-and-handoff.md#pragma-and-topic).
- **Information:** PragmaCheckpoint, ErgonPayload/references, execution metadata
  and work-related information. Both ActionableTask and FulfillmentTask have
  candidate Pragma representation with WorkOrder/ToDo/Stimulus/Effector forms;
  work instance and undertaking remain distinct. Stimulus and Effector are
  candidate Pragma specialisations; their schemas/lifecycles remain unknown.
  Dokimasia also requires candidate Pragma specialisation semantics with assurance
  request/input and assurance result/finding roles; names, schemas, cardinalities
  and lifecycle models await adjudication (CDG-09). These specialisations reuse
  common Pragma process-management, assurance, reporting and other management
  facilities. They introduce no new Domain04 Information Concepts here.
- **Persistence:** relevant Pragma operational information is an O&A classification
  candidate under U06. Legacy active-cache and FHIR Task/non-FHIR structure variants
  do not settle detailed history, representation or outcome contracts
  (CDG-02/03/12/13); the envelope is not authoritative merely by existing.
- **Collaboration/exclusions:** created/consumed/updated at ingress and execution,
  observed through presentation and outbound status handling. Does not execute
  business logic, own pools/consumer loops, define workflow blueprints or convey
  itself over wire transports.
- **Standing:** candidate carrier, not a complete Task/undertaking/outcome model
  or evidence repository. No universal state machine, automatic context persistence,
  TaskOutcome schema or ReportedTask meaning is approved here.

<a id="c08"></a>

### C08 — PragmaFactory

- **Role:** candidate logical application embodiment associated with the
  ActionableTaskArchetype meta-concept, within Mneme Pragma Management C28.
  [U01/U02](sources.md#u02). The meta-concept is not itself an instantiated
  Information Asset; the factory is not a new upstream Information Resource.
- **Responsibilities/features:** creates/initialises appropriate Pragma
  representations. Detailed instantiation/selection/validation, parameters and
  policy contracts remain **unknown**; the basic responsibility does not finalise
  a factory feature contract.
- **Contained constructs/information/persistence:** subcomponents, realised
  definition Data Objects, inputs/outputs and persistence responsibility unknown.
- **Collaborations/exclusions:** management allocation is established by U02;
  detailed callers and contracts remain unresolved. It must not be equated to a
  TaskSequence seeder or PraxisDefinition by inference.
- **Standing:** explicitly instructed candidate solution meaning, CDG-04; not
  invented from a matching class and not rejected for missing implementation.

<a id="c09"></a>

### C09 — Mneme

- **Role/responsibility:** application-facing managed-information access and active
  distributed representation, observation and coordination. [A01 AX-05/11](sources.md#a01),
  [A02](sources.md#a02); [L09/L10/L13](sources.md#l09) supply historical detail.
- **Features/contained constructs:** governed retrieval/use/change, distributed
  availability, active context/relationship navigation, observation and concurrency
  coordination, reconstruction/convergence; governed facades C11. Active resource,
  task, definition and telemetry cache groupings are historical structure candidates.
  Pragma Management C28 logically aggregates PragmaFactory and manages Pragma; this
  information-management responsibility includes Publishing/Subscription/Pattern
  semantics, eligibility and selection under U05. Mneme coordinates governed
  information management, selection, publication, subscription and availability;
  Ponos coordinates execution of the activities that consume that information.
- **Information:** active representations of governed clinical/administrative/
  operational information; activity carriers and definition working sets where
  supported. Information is not intrinsically transient because accessed actively.
- **Persistence:** reconstructable active state; Mnemosyne establishes durable state.
  Cache retention or replication is not durable authority. The older general
  write-behind acceptance model is inconsistent with A01 (CDG-10/27).
- **Collaboration/exclusions:** provides access to Iris, activities, Twins and
  Pylai; coordinates with Mnemosyne under Themis. Not workflow progression,
  durable version authority, direct database exposure or a set of process-local caches.
- **Standing:** established high-level responsibility; detailed Data Object,
  access/search and cache contracts remain candidate, CDG-11/16/27/31.

<a id="c10"></a>

### C10 — Petasos

- **Role/responsibility:** shared persistent/recoverable messaging and handoff
  between Harmonia processing boundaries, without acquiring information/activity
  semantics or information-management authority. [U05](sources.md#u05) corrects
  historical work-payload language in L13/L29 while preserving AX-10 recoverability.
- **Features:** persistent handoff obligations, bounded backlog, queue/destination
  mechanics, identity/correlation and handoff metadata, delivery observation,
  handoff redelivery/recovery/dead-letter semantics and designated replay support.
  Duplicate/replay and uncertain-effect contracts remain CDG-16/17/19/34.
- **Information:** for Pragma processing, identity/reference plus metadata needed
  to resolve information through Mneme and effect/recover handoff; the managed
  Pragma/Information Unit payload remains through Mneme. Exact handoff structure
  and destination/channel naming remain unresolved. PetasosMessage/API and opaque
  binary/text mechanisms do not establish payload ownership or semantic eligibility.
- **Persistence:** persists the handoff obligation and necessary recovery state;
  not the authoritative durable repository for referenced managed information,
  which remains Mnemosyne's responsibility, or automatically permanent evidence.
- **Collaboration/exclusions:** serves ingress, execution, publication, Agora and
  other established messaging collaborations, including Communication Event and
  Notification Event forms. Their detailed semantics remain open; Petasos is not
  ActionableTask-only and no universal reference-only message format is imposed.
  For ActionableTask handoff it offers the opportunity to undertake work; consumers
  resolve through Mneme and establish distinct FulfillmentTasks before execution.
  It does not own Publishing/Subscription/Pattern selection or Ponos execution.
- **Standing:** responsibility adjudicated for the candidate; detailed handoff,
  recovery, acknowledgement, event-form and fan-out contracts remain unresolved.
  Internal middleware belongs in Domain05; external protocols remain Domain06.

<a id="c11"></a>

### C11 — Governed access and change services

- **Role/responsibility:** Mneme-facing developer/application access and change
  contract support, not an additional information-authority owner.
  [L14](sources.md#l14), constrained by [A01/A02](sources.md#a01).
- **Features/contained constructs:** governed read/create/update, active observation
  and coordination, authoritative precondition/commit, guarded convergence and
  typed outcomes. GovernedReader/Writer, coordinator and persistence-port names
  document contract intent; Java API placement is not hierarchy authority.
- **Information:** ResourceKey, GovernedRead, active-state token, expected/
  authoritative version, WriteResult and PersistenceOperationEnvelope; distinction
  from managed payload/resource content is preserved.
- **Persistence:** Mnemosyne commits; facade coordinates and reports established
  commit separately from degraded convergence or uncertain outcome.
- **Collaboration/exclusions:** Themis context, Mneme active use/coordination and
  Mnemosyne durable port. No raw-cache/database alternative, physical DELETE or
  automatic payload/context persistence; exact APIs/transport not selected.
- **Standing:** documented candidate service contracts bounded by current RADS.
  Older entry-token, convergence and absence formulations retained (CDG-16/31),
  not asserted to realise all Domain04 manifestation or search semantics.

<a id="c12"></a>

### C12 — Mnemosyne

- **Role/responsibility:** authoritative durable state/version establishment,
  management metadata, preservation, history and recovery. [A01 AX-05](sources.md#a01),
  [A02](sources.md#a02), [L09/L13/L16/L20](sources.md#l09).
- **Persistence responsibility domains:** **Clinical Information**, **Harmonia
  Configuration & Governance (C&G)** and **Harmonia Operations & Assurance (O&A)**
  under [U06](sources.md#u06); conditional commit/versioning, preservation/retrieval,
  indexing/search support and server-side registry integrity validation.
- **Information:** managed resource representations, durable versions/metadata,
  operational definitions/state/history. Evidence, Pragma and collaboration store
  allocations differ between sources; see persistence view and CDG-12/13/22.
- **Persistence:** owns authoritative durable establishment. Hybrid relational-
  document rows, per-version composite-key structures and HAPI-native storage
  appear as competing descriptions; no physical schema is chosen here.
  C&G SHALL present a single coherent governance space without semantic sharding
  into independent/divergent authority; O&A SHALL permit horizontal partitioning
  without changing identity, provenance, relationships or governed meaning.
  These domains prescribe no service/database/schema topology or shard key;
  clinical persistence is unchanged. [Domain guardrails](data-and-persistence.md#persistence-responsibility-domains)
  and CDG-40 retain downstream physical design.
- **Collaboration/exclusions:** supports Mneme access/recovery and governed changes;
  evidence preservation collaboration remains incomplete. Does not expose its
  persistence implementation as an application API, run workflows, become a Twin
  or acquire universal authority over clinical facts by storing them.
- **Standing:** established responsibility, candidate persistence decomposition
  and structures. Database technology, replication topology and retention values
  do not define information management or authority.

<a id="c13"></a>

### C13 — Provider Registry solution collaboration

- **Role/responsibility:** documented application solution for directory resources
  and governed changes across existing components, rather than a new enclosing
  platform component. [L15/L18](sources.md#l15).
- **Features/contained constructs:** synchronous read/search, request-for-change,
  per-resource validation/identifier uniqueness/reference checks, candidate automated
  approval, durable commit and observable task progress. Seven per-resource Ergon
  activities and a registry change sequence are documented behavioural specialisations;
  Erga supplies the distinct collection/assembly/library responsibility.
- **Information:** Practitioner, PractitionerRole, Organization, Location,
  HealthcareService, Endpoint and Group; change Pragma/input/output, errors and
  resulting versions. Endpoint and Group remain independently referenced resources.
- **Persistence:** clinical/FHIR Mnemosyne slice; current-row versus history
  structures unclear. Iris is presentation-only; server-side governance remains
  with Mnemosyne Clinical, Ergon activities within Erga, and Themis under
  AGENTS Invariant 3.
- **Collaboration/exclusions:** Pylai interaction, Themis, Petasos, Ponos/Praxis/Erga,
  Mneme/Mnemosyne and Iris Administration. Does not automatically own all provider,
  care-team or national-source facts, or imply an EMPI.
- **Standing:** documented candidate domain solution, CDG-03/11/12/21/32/33.
  Direct persistence search and published async response claims are not approved
  external contracts by inclusion here.

## Boundary, human interaction and collaboration

<a id="c14"></a>

### C14 — Pylai

- **Role/responsibility:** standards-conformant boundary interaction and publication
  membrane. [A01 AX-02/13](sources.md#a01), [A02 G4/Seam 4](sources.md#a02), [L30](sources.md#l30).
- **Features/contained constructs:** inbound HL7 validation/translation/accepted-work
  handoff, outbound representation/destination dispatch observation, FHIR registry
  interactions, conformance metadata and task/status projections.
- **Information:** externally exchanged HL7/FHIR representations, internal
  requests/events/Communication and Pragma/Task projections, acknowledgement and
  per-destination observation information.
- **Persistence:** generated managed information uses governed access/persistence;
  publication evidence follows policy. External copies cease Harmonia management.
- **Collaboration/exclusions:** Themis, Calliope, Mneme, execution and Petasos.
  No internal workflow authority, raw storage path or generic transport ownership.
  Socket/framing/connection products remain downstream implementation mechanisms.
- **Standing:** boundary responsibility established; functional gateway slices
  candidate. Non-destructive fail-closed projection controls inclusion. Legacy
  transport ownership and direct-storage/async claims remain CDG-11/32/34.

<a id="c15"></a>

### C15 — Iris

- **Role/responsibility:** contextual human interaction, presentation, input and
  governed requests. [A02](sources.md#a02), [L11/L21/L23](sources.md#l11).
- **Features/contained constructs:** clinical, operations and administration
  applications (C16), mediation (C17), shared shell/design system and role-tailored
  workspaces. These are documented presentation responsibilities, not product modules
  promoted solely by their directory names.
- **Information/persistence:** displays contextual resource and operational views,
  captures user intent/assertions, transient browser selections/filters/state.
  No authoritative clinical/registry or persistence ownership; authority of human
  input comes from the actor/process/source, not Iris.
- **Collaboration/exclusions:** uses defined governed information/activity interfaces
  and Themis. No raw-cache/database alternative, JPA/SQL, backend registry validation,
  autonomous workflow engine or clinical decision authority.
- **Standing:** established high-level responsibility; candidate detailed views,
  CDG-11/24/26/28. Diagnostic zero-PHI controls do not turn clinical presentation
  into a technical-identifiers-only application.

<a id="c16"></a>

### C16 — Iris clinical, console and administration applications

| Application | Candidate features / information interaction | Persistence, collaboration and standing |
| :--- | :--- | :--- |
| **Iris Clinical** | Patient lookup, multiple workspaces, longitudinal timeline, encounters, medications/results and source/provenance context; viewer first, selective authoring conditional. | Governed read/presentation through mediation; no clinical record storage. L11/L18/L23 and L13 ADR-008; proposed authoring/terminology prerequisites L27 remain CDG-24. |
| **Iris Console** | Health/dependency/instance views, queues/backlog, interfaces, worker/sequence/Pragma/checkpoint inspection, correlation event trace and alerts. | Transient telemetry/diagnostic views; evidence is separate. Aggregator/provider support documented; origin, lifecycle and completeness vary (L21, CDG-26/28). UI perspectives are not a component taxonomy. |
| **Iris Administration** | Directory search/detail, self-service profile/credential proposals, departmental change-review/work queue, data-quality and security-policy inspection. | Client of governed registry interactions; no authoritative validation state machine, referential rules or database. L11/L18/L23; backend ownership preserved. |

These applications contain views and shared presentation controls; individual
Vue components are implementation detail. They collaborate with C17 and governed
services. Details beyond the documented views, including universal workflow
approval rules and durable UI state, are unknown.

<a id="c17"></a>

### C17 — Iris backend-for-frontend mediation

- **Role/features:** decoupled presentation service mediation and clinical versus
  operations request separation; telemetry aggregation and provider contracts.
  [L11/L21/L23](sources.md#l11).
- **Information/contained constructs:** projected resources, task/sequence views,
  subsystem status, dependency/queue/gateway observations and alert diagnostics;
  operations aggregator and health-provider support, not a new information authority.
- **Persistence/collaboration:** uses Mneme-facing governed access, Themis and
  approved execution requests; backend/presentation state is not durable truth.
  Documented raw Hot Rod/cache paths remain conflicting (CDG-11).
- **Exclusions/standing:** no direct JPA/PostgreSQL or server-side registry governance.
  Candidate mediation component; ports/server frameworks/provider class catalogues
  are support details, and telemetry persistence/provenance remains CDG-28.

<a id="c18"></a>

### C18 — Agora

- **Role/responsibility:** governed collaboration capability with external protocol
  structures encapsulated; Matrix supplies collaboration, not Harmonia architecture.
  [L12](sources.md#l12), AGENTS Invariant 10 and [A01 AX-18](sources.md#a01).
- **Features/contained constructs:** identity provisioning/mapping, patient/
  practitioner/group collaboration spaces, room creation/archive, membership
  reconciliation, transaction ingress/deduplication and event/request handoff.
  Statistics/Tasks/Discussion/Diagnostics rooms are documented candidate projections,
  not required room cardinality or independent software components.
- **Information:** AgoraSpaceRequest, collaboration events/reconciliation results,
  Harmonia-to-collaboration identity/context mappings and transaction records.
  Membership/discourse/projection information differs from formal clinical records.
- **Persistence:** mapping/transaction tables plus homeserver event/membership
  storage documented; ownership, Mnemosyne collaboration and clinical submission
  path remain unclear (CDG-19/22/23). Historical retention value is not selected.
- **Collaboration/exclusions:** Themis guards room actions; workflow collaboration
  uses Petasos with no direct Ponos dependency. No unmasked PHI in room metadata,
  raw Matrix DTO leakage, clinical-work authority or universal source-of-truth claim.
- **Standing:** candidate collaboration design with governing isolation constraints;
  care-team source, projection providers and durable effect contract unresolved,
  CDG-19–23.

## Specialised execution components within Energeia

<a id="c19"></a>

### C19 — DigitalTwin

- **Role/features:** specialised Ponos-based execution component for entity-specific
  operational coordination within Energeia. [U02](sources.md#u02) establishes
  specialisation/reuse, not containment by a Ponos runtime. The Digital Twin
  responsibility in [A02](sources.md#a02) remains controlling: entity-related
  execution SHOULD use the Twin unless explicitly justified otherwise.
- **Twin-specific execution:** per-Twin queue pattern. Many Twins are compatible
  with bounded execution resources; only Twins with pending work become active
  and are serviced. No one-thread-per-Twin or permanently resident actor is implied.
  Detailed scheduling, activation and coordination contracts remain CDG-07.
- **Information:** uses context drawn from governed entity information and
  relationships; entity/resource/Twin identity are not aliases. Historical patient,
  practitioner, wardsperson, organisation, care-team, ward and bed examples are
  illustrative archetypes, not an approved exhaustive component catalogue.
- **Persistence/significant information:** definition/configuration is a C&G
  classification candidate; operational state/activity is an O&A candidate under
  U06. Detailed coordination-state Data Objects, activation/deactivation,
  preservation and transient/durable qualification remain CDG-07. A Twin is not
  inherently a permanent object, table or FHIR resource.
- **Collaboration/exclusions:** executes Behaviour C25, which specialises Ergon
  and retains all common Ergo/Ergon facilities. Uses Mneme Pragma Management and
  candidate Stimulus/Effector specialisations; Ponos supplies common execution,
  Petasos supplies messaging/recoverable handoff. Follows the
  [specialisation principle](README.md#specialisation-principle); adds genuinely
  Twin-specific semantics instead of parallel platform mechanisms.
  Not a second engine, unmanaged persistent actor, separate
  policy/search service or authority over originating clinical facts/clinical work.
- **Standing:** candidate execution/activity specialisations adjudicated by U02;
  information detail remains candidate. DigitalTwin and Dokimasia are sibling
  Ponos specialisations; neither contains the other or manages Pragma by execution.

<a id="c20"></a>

### C20 — Dokimasia

- **Role/features:** specialised Ponos-based execution component within Energeia
  coordinating independent governed assurance activity over a subject. Journey
  C35 is the common observed-progression evidence subject/substrate for Pathway
  and Pub/Sub
  assurance under U04/U05; this does not limit all assurance subjects to Journey.
  [U02](sources.md#u02) settles the candidate specialisation relationship; actual
  Assurance Praxis evaluates available
  evidence against applicable basis and produces governed assurance information.
  [A04 §2](sources.md#a04), [L31](sources.md#l31). **Praxis is the assurance basis;
  Journey is the observed evidence subject** under U06. Pathway/Route expectations
  contribute through applicable Praxis rather than constituting the general basis.
- **Information:** evidence/context/basis and assurance assessment/finding/outcome
  areas are documented. Journey observations remain distinct from inferred
  interpretation and assurance findings. Dokimasia activity/findings/results are
  O&A classification candidates under U06; detailed Data Objects, identity and
  preservation remain unresolved. Candidate vocabulary is not promoted into an
  Information Family.
- **Activity/collaboration:** executes discrete Dokimasia Ergon C26 specialisations,
  including candidate AI Assurance Ergon C27. Reuses Ponos execution, Ergo/Ergon
  activity, Mneme Pragma Management, process-management, platform integration,
  reporting and observability. This is specialisation/reuse, not runtime containment
  or a parallel assurance execution platform.
- **Information/persistence:** corresponding Pragma specialisation semantics are
  required; assurance request/input and assurance result/finding are candidate
  roles whose names, schemas, cardinalities and lifecycle remain unresolved.
  Evidence-access/control independence, detailed workflow composition and durable
  structures remain CDG-08/09. Common framework reuse does not let the assured
  activity determine, suppress, manufacture or alter its own assurance outcome.
- **Exclusions:** evidence, workflow completion and operational status are not
  assurance; custody does not make Kleio the evaluator. Dokimasia does not manage
  operational subject escalation/remediation and is not an established Twin subtype.
  AI invocation confers no authority on the model/service. Dokimasia can manage
  its own assurance work while the subject retains its operational management.
- **Standing:** conceptual independence remains established by A04; execution
  and discrete activity responsibilities are adjudicated for the candidate by U02.
  Detailed AI architecture requires CDG-35 discovery. No upstream responsibility
  is reopened; detailed Praxis/Journey and assurance contracts remain unresolved.

<a id="dokimasia-terminal-boundaries"></a>

### Dokimasia terminal-boundary constraints

These are architectural constraints on C20 and its specialised activity
components, including C26/C27, rather than optional implementation suggestions.

1. **No recursive Dokimasia:** a Dokimasia execution SHALL NOT itself be subject
   to monitoring, assurance or assessment by another Dokimasia execution.
   This is the terminal assurance boundary.
2. **No separate AuditEvent:** a Dokimasia execution SHALL NOT generate a separate
   AuditEvent for its own execution. The governed Dokimasia assurance record/result
   provides authoritative evidence of that assurance activity.

Dokimasia remains governed by established security context, Themis and normal
execution-integrity contracts. Common reporting, controlled logging/observability
and stewardship/management observation remain available; they do not create a
second Dokimasia assessment. Inherited assurance/evidence integration respects
these terminal constraints rather than recursively assuring the assurance activity
or duplicating its evidence as an own-execution AuditEvent. Record representation,
preservation and access contracts remain unresolved; the evidence rule establishes
neither a new repository nor detailed Pragma/outcome lifecycle semantics.

## Simulation and developer/operator support

<a id="c21"></a>

### C21 — Paradeigma simulation and verification support

- **Role/features/contained constructs:** isolated synthetic PAS, EMR, LMS and
  RIS-PAC simulators, seeded patient/provider/order/result generation, scenario
  orchestration, failure/security/logging probes and architecture/integration
  verification. [L22](sources.md#l22).
- **Information/persistence:** synthetic personas, encounters, orders/results,
  scenario configuration and test state; ephemeral fixtures/test lifecycle.
  No production information-management or durable clinical-authority responsibility.
- **Collaboration/exclusions:** exercises production interfaces as simulated
  external participants. Production imports/dependencies/simulation flags forbidden;
  support simulator state is not a fourth production persistence tier.
- **Standing:** documented support subsystem with established isolation guardrail;
  no MVP or production feature allocation follows from its inclusion here.

<a id="c22"></a>

### C22 — Developer and operator support constructs

- **Role/features:** documented activity and workflow authoring/loading/reload,
  schema/converter consumption, CLI inspection/administration, shared presentation
  shell/status controls, telemetry/health-provider contracts and verification support.
  [L02/L19/L21/L23](sources.md#l23).
- **Contained constructs:** Ponos administrative CLI, Mnemosyne/operations CLI,
  Pylai diagnostic CLI; Praxis loaders/seeders; Iris shared design system and
  subsystem health providers. Association follows the managed component;
  this row is a support inventory, not a newly invented common subsystem.
- **Information/persistence:** workflow definition/configuration, status/probe
  observations, command requests and synthetic diagnostic messages. Formal command
  authority, durable command evidence and telemetry lifecycle are incomplete.
- **Collaboration/exclusions:** governed component operations and observation
  boundaries apply to support tools. CLI convenience does not authorize bypassing
  Mneme or promoting tool-local state to truth (CDG-11/28).
- **Standing:** candidate support features and implementation/navigation detail;
  exact APIs, packaging and runtime platform remain downstream concerns.

## Interface, control, observational and contained logical constructs

The following IDs are navigation aids. They classify adjudicated constructs;
they do not manufacture independent components or deployment boundaries.
[U04/U05](sources.md#u04) and the [shared semantic view](pathways-journeys-and-handoff.md)
govern. Detailed control/definition contracts remain CDG-01/06/36 and Journey
contracts remain CDG-38; no final physical representation is selected.

<a id="c29"></a>

### C29 — PathwayEndpoint

Application Interface conceptually equivalent to ArchiMate ApplicationInterface.
Every Ergon exposes IngressEndpoint and EgressEndpoint. These surfaces do not
expose internal processing; endpoint identity and technical interfaces remain open.

<a id="c30"></a>

### C30 — PathwayElement

Generic representation of information/activity flow between and across component
boundaries, specialising at least as PathwaySegment and PathwayTraversal. It is a
behavioural/flow abstraction, not an Application Component or a final Journey schema.

<a id="c31"></a>

### C31 — PathwaySegment

Inter-component flow from Ergon A.EgressEndpoint to Ergon B.IngressEndpoint;
conceptually comparable to ArchiMate ApplicationInteraction between ApplicationInterfaces.
Segments provide the elements of Route's ordered routing/control sequence.

<a id="c32"></a>

### C32 — PathwayTraversal

Intra-component passage from an Ergon's IngressEndpoint to its EgressEndpoint.
It establishes component traversal while preserving internal business-logic
encapsulation; it does not become a Route element merely for reporting continuity.

<a id="c33"></a>

### C33 — Route

Routing/control construct: an ordered sequence of PathwaySegment [1..N].
Route is not redefined as generic PathwayElement or an observed Journey graph.
Representation and management/preservation contracts remain unresolved.

<a id="c34"></a>

### C34 — Pathway

Defines controlled interaction between PathwayEndpoints and available Route(s).
Praxis **aggregates Pathway [0..1]** within its operational/assurance envelope.
Publish/Subscribe
is a distinct progression mechanism and need not have a Pathway.

<a id="c35"></a>

### C35 — Journey

First-class observational construct / candidate Application Data Object: governed
observed progression reconstructed from available execution evidence, whether
Pathway-controlled, Pub/Sub or mixed. It may be graph-shaped and have no Pathway.
Primary reporting, monitoring, analysis and assurance subject; Dokimasia may
interpret it under applicable basis without replacing observations with inference.
**Energeia owns Journey and Journey Management Services F38** under U06. Mneme
manages governed information; Mnemosyne **O&A** provides durable persistence
responsibility. Journey aggregates/references observations/information from Mneme,
Petasos, Ponos, Ergon, DigitalTwin and other components/evidence mechanisms where
appropriate, retaining their ownership. Comparatively small discrete Journey
information may describe substantial activity/evidence: aggregate semantically,
without indiscriminate duplication. Detailed representation/construction, lifecycle,
retention, graph/evidence/causal/correlation/reference schema and inference contracts
remain unresolved ([CDG-38](gaps.md#journey-contracts)); Journey is not a shard key.
Journey is neither an engine, AI narrative nor automatic permanent AuditEvent.

<a id="c36"></a>

### C36 — Publishing

Contained Mneme Pragma Management semantic responsibility and governed provider
definition: Pattern-based filtering can prevent egress Pragma being made available
through publication when a Pattern matches. Governs provider eligibility, distinct
from consumer interest. Definition lifecycle/persistence and evaluation detail remain open.

<a id="c37"></a>

### C37 — Subscription

Contained Mneme Pragma Management semantic responsibility and governed consumer
definition: requests available Pragma matching Pattern be made available/forwarded
to the consumer input queue or equivalent destination. Mneme matches/selects;
Petasos effects resulting handoff. Registration/lifecycle and detailed fan-out remain open.

<a id="c38"></a>

### C38 — Pattern

Contained Mneme semantic evaluation responsibility / governed selection construct
used by Publishing and Subscription. May evaluate established Pragma, Unit-level
Container and Content metadata, Topic, source and other governed characteristics.
Topic is classification/metadata rather than a destination. Final Pattern language,
attributes/operators, precedence, query/index and technology remain unresolved.
