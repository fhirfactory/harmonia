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
  evaluation is durable evidence.
- **Persistence:** evidence permanence/immutability is documented; exact repository,
  schema and collaboration with Mnemosyne are incomplete. Historical FHIR and
  operations allocations compete (CDG-13/14).
- **Collaboration/exclusions:** receives evidence from Themis, Pylai, execution,
  Iris and other governed producers. Evidence custody does not allocate
  independent assurance evaluation, work execution or subject management.
- **Standing:** high-level RADS responsibility plus ADR candidate detail; not
  rejected because the legacy subproject inventory omits its name.

## Activity, composition and task constructs

<a id="c04"></a>

### C04 — Ponos

- **Role/responsibility:** governed execution and observable progression of
  operational activity; documented as WorkEngine/worker design within Energeia.
  [A02](sources.md#a02), [L02/L01/L23](sources.md#l02).
- **Features/contained constructs:** work ingress and dispatcher, workflow/sequence
  resolution, activity invocation, execution concurrency/timeouts, retry and
  dead-letter escalation, checkpoint coordination and administrative inspection.
  Consumer loops/thread pools are documented mechanisms, not logical children.
- **Information:** consumes work/events and Pragma context; consults workflow
  definitions; produces progression, failure and destination observations.
- **Persistence:** coordinates recoverable handoff through Petasos and governed
  information change through Mneme/Mnemosyne. Does not establish durable state
  by completing an activity or writing a checkpoint to a cache.
- **Collaboration/exclusions:** uses Praxis, Erga, Themis, Mneme and Petasos.
  Does not own workflow definitions, concrete domain logic, broker lifecycle,
  external standards/transport or universal clinical-work determination.
- **Standing:** established execution responsibility, candidate detailed model.
  No Dokimasia execution allocation; CDG-03/08/16–18 retain incomplete control
  and continuation semantics. Numerical worker/retry defaults are not baselined.

<a id="c05"></a>

### C05 — Ergo / Ergon / Erga

- **Role/responsibility:** developer-defined activity behaviour in a governed
  loading → logic → unloading boundary. Legacy material uses Ergo as the
  activity-business-logic term and Ergon (plural Erga) for discrete activity units.
  [A05/U01](sources.md#a05), [L03/L19](sources.md#l03).
- **Features/contained constructs:** transformation/mapping, field validation,
  identifier/reference checking, enrichment, order routing, result processing,
  fan-out and per-resource registry change activities. `ErgonBase` is documented
  contract support, not a separate logical activity owner.
- **Information:** acts upon Pragma inputs, resource models and context; generates
  resources, outputs, checkpoints and destination observations. **No discrete
  Ergon Information Resource exists merely because the behaviour is an Ergon.**
- **Persistence:** requests governed persistence of resulting information; no
  independent raw database ownership or universal transaction boundary is established.
- **Collaboration/exclusions:** composed using Praxis, executed with Ponos,
  uses Calliope/Themis/Mneme/Petasos; not worker allocation, transport or durable
  storage infrastructure. Optional AI can be intentionally invoked without
  transferring authority or defining Ergon identity.
- **Standing:** behavioural standing adjudicated by U01; detailed contracts remain
  candidate. Atomic/pure/idempotent claims coexist with state-changing examples
  and varied branch allocation, CDG-05. Names are retained without inferring equivalence
  to a Task definition, undertaking, Information Unit or FHIR resource.

<a id="c06"></a>

### C06 — Praxis

- **Role/responsibility:** documented workflow blueprint, composition and execution
  design area. [L04/L18/L19](sources.md#l04) distinguish declarative metadata and
  runtime routes; [A04](sources.md#a04) also uses Assurance Praxis for actual activity.
- **Features/contained constructs:** definition loading/validation/seeding, ordered
  steps, dependencies/conditions, subscriptions and trigger/gateway selection,
  dynamic composition/reload and checkpoint support. TaskSequence,
  PraxisDefinition and PraxisImplementation are documented support constructs.
- **Information:** workflow definitions/configuration, activity references, topics
  and subscriptions; actual-execution information is carried through Pragma.
- **Persistence:** active definition caches and non-FHIR operations persistence
  are described. Authoritative definition lifecycle/version and cache recovery
  allocation are incomplete (CDG-06).
- **Collaboration/exclusions:** supplies composition to Ponos/Erga, uses Mneme and
  definition support. Legacy blueprint model excludes worker pools, payload logic,
  live transaction state and broker sessions.
- **Standing:** candidate with explicit CDG-01. DAG, ordered sequence, runtime
  orchestrator and actual Praxis activity are not silently made aliases. Parent
  hierarchy, composition identity and definition-to-instance semantics remain open.

<a id="c07"></a>

### C07 — Pragma

- **Role/responsibility:** candidate task/context/progression carrier construct
  associated with Energeia; model definitions also documented in Calliope.
  [L05/L01/L19](sources.md#l05), [U01](sources.md#u01).
- **Features/contained constructs:** identity/correlation/causation, inputs/outputs
  or payload references, context, state/checkpoints, parent association and
  destination-specific delivery observations.
- **Information:** PragmaCheckpoint, ErgonPayload/references, execution metadata
  and work-related information. Both ActionableTask and FulfillmentTask have
  candidate Pragma representation with WorkOrder/ToDo/Stimulus/Effector forms;
  work instance and undertaking remain distinct. Form detail is unknown.
- **Persistence:** active cache and FHIR Task/non-FHIR operations representations
  appear in legacy design. Allocation, history, carrier variants and outcomes
  are unresolved (CDG-02/03/12/13); the envelope is not authoritative merely by existing.
- **Collaboration/exclusions:** created/consumed/updated at ingress and execution,
  observed through presentation and outbound status handling. Does not execute
  business logic, own pools/consumer loops, define workflow blueprints or convey
  itself over wire transports.
- **Standing:** candidate carrier, not a complete Task/undertaking/outcome model
  or evidence repository. No universal state machine, automatic context persistence,
  TaskOutcome schema or ReportedTask meaning is approved here.

<a id="c08"></a>

### C08 — PragmaFactory

- **Role:** current candidate logical embodiment of ActionableTaskArchetype under
  [U01](sources.md#u01). The meta-concept need not be a separately realised asset.
- **Responsibilities/features:** detailed instantiation, selection, validation,
  parameterisation and policy handling are **unknown**; the name alone establishes
  no complete factory feature contract.
- **Contained constructs/information/persistence:** subcomponents, realised
  definition Data Objects, inputs/outputs and persistence responsibility unknown.
- **Collaborations/exclusions:** no consistent legacy contract or structural
  allocation to Praxis/Ponos/Calliope was found. It must not be equated to a
  TaskSequence seeder or PraxisDefinition by inference.
- **Standing:** explicitly instructed candidate solution meaning, CDG-04; not
  invented from a matching class and not rejected for missing implementation.

## Information management, preservation and middleware

<a id="c09"></a>

### C09 — Mneme

- **Role/responsibility:** application-facing managed-information access and active
  distributed representation, observation and coordination. [A01 AX-05/11](sources.md#a01),
  [A02](sources.md#a02); [L09/L10/L13](sources.md#l09) supply historical detail.
- **Features/contained constructs:** governed retrieval/use/change, distributed
  availability, active context/relationship navigation, observation and concurrency
  coordination, reconstruction/convergence; governed facades C11. Active resource,
  task, definition and telemetry cache groupings are historical structure candidates.
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

- **Role/responsibility:** internal opaque messaging and durable transfer of work
  between independently recoverable activities. [A01 AX-10](sources.md#a01),
  [L13 ADR-014–017](sources.md#l13), [L29](sources.md#l29).
- **Features/contained constructs:** producer/consumer/destination contracts,
  framing/envelope handling, correlation, duplicate detection, durable acceptance,
  consumption/rejection, redelivery/dead-letter handling, designated replay and
  bounded backlog/transport metrics. Broker adapters are implementation support.
- **Information:** PetasosMessage opaque binary/text payload plus message identity,
  source/destination, schema/type, correlation/causation and context metadata.
- **Persistence:** recoverable in-flight work at explicit transition points;
  messaging journal/queue structures are documented mechanisms. Not the durable
  repository for committed application information or permanent evidence.
- **Collaboration/exclusions:** ingress → execution, execution → publication,
  collaboration requests/events and evidence handoff where documented. Does not
  parse clinical business content, own workflow rules or clinical persistence.
- **Standing:** recoverability responsibility established; detailed handoff,
  deduplication/replay/uncertain-effect contracts incomplete, CDG-16/17/19/34.
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
- **Features/contained constructs:** clinical/FHIR and non-FHIR operational
  persistence slices, conditional commit/versioning, preservation/retrieval,
  indexing/search support and server-side registry integrity validation.
- **Information:** managed resource representations, durable versions/metadata,
  operational definitions/state/history. Evidence, Pragma and collaboration store
  allocations differ between sources; see persistence view and CDG-12/13/22.
- **Persistence:** owns authoritative durable establishment. Hybrid relational-
  document rows, per-version composite-key structures and HAPI-native storage
  appear as competing descriptions; no physical schema is chosen here.
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
  approval, durable commit and observable task progress. Seven per-resource Erga
  and a registry change sequence are documented behavioural specialisations.
- **Information:** Practitioner, PractitionerRole, Organization, Location,
  HealthcareService, Endpoint and Group; change Pragma/input/output, errors and
  resulting versions. Endpoint and Group remain independently referenced resources.
- **Persistence:** clinical/FHIR Mnemosyne slice; current-row versus history
  structures unclear. Iris is presentation-only; server-side governance remains
  with Mnemosyne Clinical, Erga and Themis under AGENTS Invariant 3.
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

## Cross-component constructs and support

<a id="c19"></a>

### C19 — Digital Twin

- **Role/features:** active entity-specific information/state/activity coordination
  across Mneme/Ponos. Entity-related operational execution SHOULD use the entity's
  Twin unless explicitly architecturally justified otherwise. [A02](sources.md#a02).
- **Information:** uses context drawn from governed entity information and
  relationships; entity/resource/Twin identity are not aliases. Historical patient,
  practitioner, wardsperson, organisation, care-team, ward and bed examples are
  illustrative archetypes, not an approved exhaustive component catalogue.
- **Persistence/contained constructs:** internal coordination-state Data Objects,
  activation/deactivation and durability allocations are not sufficiently defined
  (CDG-07). A Twin is not inherently a permanent object, table or FHIR resource.
- **Collaboration/exclusions:** obtains information via Mneme and coordinates
  execution through Ponos. Not a second engine, unmanaged persistent actor, separate
  policy/search service or authority over originating clinical facts/clinical work.
- **Standing:** established architectural construct, detailed solution realisation
  candidate; no manufactured parent/child relationship with Dokimasia or Pragma.

<a id="c20"></a>

### C20 — Dokimasia / Assurance Praxis

- **Role/features:** independent assurance framework coordinating its own governed
  assurance activity over a subject; actual Assurance Praxis evaluates available
  evidence against applicable basis and produces governed assurance information.
  [A04 §2](sources.md#a04), [L31](sources.md#l31).
- **Information:** evidence/context/basis and assurance assessment/finding/outcome
  areas are documented; detailed Data Objects, identity and persistence are
  unallocated. Candidate vocabulary is not promoted into an Information Family.
- **Contained constructs/persistence/collaboration:** detailed component composition,
  evidence-access/control contract and execution relation to Ponos remain unresolved
  (CDG-08/09). It is not placed inside Ponos or declared a new execution engine.
- **Exclusions:** evidence, workflow completion and operational status are not
  assurance; custody does not make Kleio the evaluator. Dokimasia does not manage
  operational subject escalation/remediation and is not an established Twin subtype.
  Optional AI does not confer determination authority or introduce recursive assurance.
- **Standing:** conceptual name/boundaries approved; application allocation remains
  candidate and explicit. No upstream assurance responsibility is reopened.

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
