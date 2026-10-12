<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Logical Data, Information Responsibility and Persistence

**Standing: reconstructed and Step-A-adjudicated Harmonia Candidate Information Systems Architecture.**
[Scope](README.md) and [source dispositions](sources.md) apply. This view records
data already coupled to documented solution features/components. It does not
catalogue every Domain04 concept, test Information Architecture coverage, invent
Information Resources or approve technology-specific schemas.

## Distinct concerns

```text
Logical Data meaning described by candidate sources
  → Application Data Object / representation
  → Information Management Responsibility
  → Persistence Responsibility
  → Persistence Data Structure
  → Technology Platform (documented historical mechanism, not selected here)
```

The arrows describe levels of explanation, not a required one-to-one realisation
or an upstream traceability chain. A native FHIR resource may be an application
Data Object; it is not automatically a component, Information Unit, Twin,
authoritative assertion, durable row or external contract. A schema/table does
not define the managed information's meaning or authority.

## Coupled logical/application Data Objects

| ID | Logical data / candidate application object | Information management / production / use | State and persistence responsibility | Evidence and unresolved allocation |
| :--- | :--- | :--- | :--- | :--- |
| D01 | Trigger/event: **ErgonEvent**, reason and correlation metadata | Calliope defines envelope/reason vocabulary; Pylai and activities produce/use events; Ponos consumes | Operational event/context; recoverable transfer through Petasos where designated, not an automatic permanent audit record | L06/L07/L23; envelope relations to Pragma/PetasosMessage not completely specified, CDG-02/16. |
| D02 | Activity input/output: **ErgonPayload**, native resource/Bundle, raw HL7/JSON or references | Calliope contract support; Ergon conforming to Ergo acts upon/generates; Pragma carries or references | Mneme active use; Mnemosyne durable resulting managed information. Agora owns native collaboration media; Sylloge manages F31/D15 formal binary content without requiring Harmonia physical custody | L06/L03/L19; U02 separates activity contract/component; U16/U17 distinguish native media, formal binary management and physical custody. Reference/payload variants CDG-02; no storage product selected. |
| D03 | Themis Authorisation Model: Criterion, nested collections, AuthorisationRole, direct/role assignments, temporal validity and distinct revocation; authenticated Identity, PractitionerCapacity, PEP-Criteria / PEP-Context and decisions | External IdP authenticates; Calliope governs nomenclature. Themis PAP/PRP/PDP owns model, resolution/compilation and derivation provenance; Themis local PEP enforces. Ergo/Praxis governs administration, Iris presents | PRP persisted through Mneme → Mnemosyne C&G; model ownership remains Themis. PEP-Criteria is precomputed and Identity-specific; operational context is not automatically persisted resource content. Significant occurrences use Kleio under policy | U08/U12, AX-04/05/07/14/17. Criterion dimensions AND; grants UNION; no explicit negative Criteria. No historical carrier or final schema inferred. Temporal validity/revocation distinct; forwarding preserves originating/acting Identity. Themis deferrals [CDG-42](gaps.md#themis-detailed-contracts). |
| D04 | Supporting governed access/change carriers: ResourceKey, GovernedRead, active token, authoritative version, WriteResult, PersistenceOperationEnvelope | Mneme supporting contracts C11, constrained by its four-service surface; Mnemosyne establishes/reports persistence; Themis governs | Active generation and durable version differ; Context version/resource version also distinct; commit/degraded convergence/uncertain outcome differ | AX-05/14/15 and U10/U11. Historical carrier shapes are not additional services or Context schema; exact mappings CDG-31/45/47. |
| D05 | Pragma task/activity information envelope: inputs/outputs, checkpoint, parent/correlation, payload/reference | Mneme Pragma Management C28 manages; factory initialises; Ergon reads/acts/produces **new Content as Pragma Output**, distinct from changed Context values | Mneme management distinct from Mnemosyne durable establishment; work/undertaking information remains O&A classification candidate; no automatic persistence of every field | U10 extends U01/U02/U06. Context is working value environment; Pragma expresses inputs/outputs. Exact Output/mapping contracts CDG-45; existing Task/representation CDG-02/03/10/12/13 preserved. |
| D06 | Praxis expected/permitted operational context and control configuration; historical TaskSequence / PraxisDefinition representations | **Energeia owns Praxis / Praxis Management Services**; Mneme provides governed Context/Pragma access; Ponos uses context/control; Dokimasia uses Praxis assurance basis | Praxis and applicable control definitions are **C&G classification candidates**; detailed representation/lifecycle/version binding unresolved | U06 establishes higher-order envelope and working Aggregation: Praxis → Pathway [0..1], with Journey cardinalities unresolved. Historical schemas are provenance; CDG-01/06. |
| D07 | Topic metadata; distinct build-time Topic/Filter definitions and runtime Publishing/Subscription/Pattern constructs | Calliope-Models defines Topics, Filter Types and Filter Elements; Mneme manages runtime Pattern evaluation/matching, publication and selection; Petasos effects handoff | Topic metadata on Pragma/Content is not destination; runtime selection definitions retain C&G candidate standing | U05/U13. Definition ownership is not runtime operational ownership; Pattern language/lifecycle and fan-out CDG-36/37 remain. |
| D08 | Directory: **Practitioner, PractitionerRole, Organization, Location, HealthcareService, Endpoint, Group** | Pylai interaction, registry Ergon validation/change, Themis policy, Iris presentation; Mneme active access | Mnemosyne clinical/FHIR preservation; independently referenced Endpoint and Group; Group is not an IAM/security group | L15/L18 and U12. FHIR PractitionerRole is a formal role/service relationship, distinct from selected PractitionerCapacity and Themis AuthorisationRole; Capacity mapping deferred. Candidate topology is not Domain04 coverage or universal authority; detail CDG-11/12/21/33. |
| D09 | Clinical exchange/content: **Patient, Encounter, Location, ServiceRequest, Observation, DiagnosticReport, Communication, Consent** and resource Bundles | Documented converter/Ergon inputs/outputs; Pylai boundary; Iris context; Mneme access | Clinical/FHIR Mnemosyne slice described; exact supported inventories vary. Persisted assertion retains its own source authority | L03/L06/L09/L10/L18. No Master Patient or originating clinical-work authority inferred, CDG-20/29. |
| D10 | Governed destination delivery observations; checkpoints / Task output information | Distribution activity and Petasos/Pylai contribute observations; Mneme manages recorded information; Iris/Journey use it | Petasos owns operational handoff/delivery obligation state. Governed observations in Pragma or Task do not own that obligation | U05 preserves REC-002; exact observation relationships, durable recording and parent progression remain CDG-18/34/38. No task resolution algorithm. |
| D11 | **PetasosParcel**; purpose-defined durable queue and handoff operational state | Petasos manages common queued communication/event datatype and queue creation/operation/status; referenced information remains with its authoritative component, including Mneme-managed ActionableTask/Pragma | Parcel passed by value; associated information predominantly by reference. Durable queue/handoff recovery belongs to Petasos; Mnemosyne separately establishes referenced managed-information state | U05/U09. Conceptual FHIR Communication analogy, no literal equivalence/schema. Consumers establish distinct FulfillmentTasks; no transported other-consumer undertaking. Event purposes remain distinct; detailed representation/delivery/state CDG-16/17/19/37/44. |
| D12 | Authoritative FHIR **AuditEvent**; provenance/transition references; supporting indexes/correlation/prepared historical context; governed Dokimasia assurance record/result | Kleio owns authoritative Audit Capture/management/history; clients report occurrences, Kleio constructs AuditEvent; derived/prepared views remain distinguishable | Kleio → Mneme → Mnemosyne O&A. AuditEvents immutable: new linked event corrects/qualifies, earlier unchanged. Reference source history by default; replicate necessary evidence only when historical retrieval cannot be guaranteed | U07, AX-08/09 and U02 terminal rules. Journey participation not required; no separate Dokimasia own-execution AuditEvent. Required authoritative durable capture essential; one capable independent audit target suffices, cross-target harmonisation SEP. Mechanisms CDG-43. |
| D13 | Operational support: **OperationResourceEntity**, task/sequence metadata, module/queue/health/alert/trace observations | Component producers; Ponos execution observations; Energeia Praxis/Journey operational management; Iris aggregator/providers present; Mneme active access | C&G classification for governed configuration; O&A for governed activity observations/state where persistence is required; historical non-FHIR structures remain variants; transient diagnostics not all durable | L16/L21/L23. Energeia operational-management ownership is resolved; technical telemetry sources, freshness, projection and retention remain detailed contracts CDG-13/27/28. |
| D14 | Native collaboration dialogue/actions, Room/Space/membership state, media, references/activity, identity/context mappings and transaction information | **Agora owns/manages native collaboration information**; Omilia.Server performs underlying operations through Application Services API; Themis governs; Petasos mediates execution collaboration | Agora application ownership differs from AX-05 managed access/durable establishment. Persistence/storage allocation and historical mapping/transaction structures deferred; Omilia mechanics confer no independent durable authority | U16 resolves CDG-21/22 ownership. Roster SEP contributes Shift Start/End context only. Conveyed source information retains its owner; capture derives new Communication Information Units and/or Pragma, not a transferred dialogue/automatic clinical record. Detail CDG-19/23/52. |
| D15 | Formally managed clinical/operational binary content; binary-content metadata, Information Unit associations and location/resolution/custody knowledge; distinct from D14 native collaboration media | **Harmonia.Sylloge C49** owns Binary Content Management: governed association, resolution, retrieval and on-demand assembly from one or more payloads. Originating logical information authority remains distinct | Physical custody may remain external; Sylloge management does not require copying every payload into Harmonia. Where Harmonia persistence is required, Mnemosyne may provide applicable services under AX-05; application ownership remains Sylloge | U17 closes [CDG-25 responsibility](gaps.md#binary-custody-question). Historical L10/L17/L19/L23 carrier/DocumentReference examples are supporting evidence, not representation choices. Storage, APIs, assembly algorithms, lifecycle/versioning and FHIR/DICOM representations remain deferred. |
| D16 | Governed Models/Metadata Types/Criteria/Topic/Filter definitions; ValueSets, CodeSets, Structured Metadata and applicable ontology content | Calliope-Models owns structural/build-time definitions and development representations; Calliope-DataSets owns mutable governed content, including U12 four authorisation ValueSets. Themis owns policy association/resolution/enforcement | Models follows build/release/deploy; DataSets uses established Mneme/Mnemosyne persistence. Build-time values do not establish current membership | U12/U13; detailed release contents, ontology boundary, version/history/workflow/representation CDG-24/49. No dedicated datastore, universal terminology API or runtime structural bootstrap. |
| D17 | Twin coordination context and assurance context/basis/evidence/assessment/finding/outcome candidate areas | DigitalTwin uses governed entity information; Dokimasia coordinates independent assurance. Evidence retains originating responsibility; execution/assessment does not confer information-management authority | Detailed coordination-state, evidence-access/control and persistence contracts remain unresolved; no Twin table or assurance Information Family fabricated | A02/A04 and U02 establish responsibility/specialisation independently of resemblance. CDG-07–09 retain detail; AI participation requires CDG-35 discovery. |
| D18 | **Stimulus / Effector** — candidate Pragma specialisations | Mneme / Pragma Management manages; DigitalTwin Behaviour uses/produces as specialised activity information | Reuse common Pragma process-management, assurance, reporting and management facilities; Mnemosyne durable establishment where applicable | U02. Specialisation is candidate Application Data Object reuse, not a new Domain04 Information Resource or schema/lifecycle decision. |
| D19 | Dokimasia Pragma candidate semantic roles: **assurance request/input; assurance result/finding** | Mneme / Pragma Management manages; Dokimasia Ergon, including AI Assurance Ergon where selected, uses/produces within governed assurance workflow | Corresponding Pragma specialisation semantics required; reuse common management facilities. Detailed representation/preservation allocation unresolved | U02; CDG-09/35. Semantic roles are established as candidates; names, schemas, cardinalities and lifecycle models are not final. |
| D20 | PathwayEndpoint / PathwayElement / Segment / Traversal / Route / Pathway control representations | Ergon exposes interface boundaries; Pathway defines control with Route = ordered Segment [1..N]; traversal preserves encapsulation | Detailed definition management, lifecycle, identity and preservation allocation unresolved | U04; C29–34/F36. Control representations are not automatically separate Application Components or Domain04 Information Concepts; CDG-01/06. |
| D21 | Journey — governed observed progression | **Energeia owns Journey / Journey Management Services**; Mneme provides governed Context/Pragma access; contributors retain referenced information ownership; Dokimasia consumes Journey as evidence subject against Praxis | **Mnemosyne O&A** provides durable persistence responsibility; small discrete representation aggregates/references substantial activity/evidence. Efficient reconstruction/use is an objective, not a shard key | U04/U05/U06; C35/F38/CDG-38. Representation/construction, reference/causal/correlation schema, retention and inference contracts remain unresolved. May have no Pathway; observations are distinct from findings. |
| D22 | Publishing definition | Mneme / Pragma Management governs provider-side Pattern filtering/eligibility | C&G semantic classification candidate; detailed lifecycle/governance/representation unresolved | U05/U06; C36/F37/CDG-36. Matching filter may block egress availability; not generic producer mechanics. |
| D23 | Subscription definition | Mneme / Pragma Management governs consumer Pattern selection and eligible recipient/consumer relationships; Petasos owns concrete destination handoff | C&G semantic classification candidate; detailed registration/lifecycle/representation unresolved; concrete handoff obligation separately Petasos | U05; C37/F37/CDG-36/37. Selection does not itself establish an undertaking or mandate payload copies. |
| D24 | Pattern and evaluation observations | Mneme owns semantics/evaluation against established metadata, including Topic/source at appropriate Pragma/Container/Content levels | Definition is a C&G classification candidate; governed evaluation observations are O&A candidates where durability is required. Representation/precedence/operators unresolved | U05; C38/F37/CDG-36. No final language, query/index mechanism or technology selection. |
| D25 | Information Unit payload: Unit-level Container and Content metadata | Managed through Mneme; Pragma contains or references payload; Pattern may evaluate governed characteristics | Domain04 Identity/Metadata/Content/Context and durable-version/active-generation distinctions preserved; exact application partition representation unresolved | U05/A06; semantic shape is not a schema or revival of retired Information Container. Container Topic need not equal Content Topic; source need not equal origin. |
| D26 | **Context — versioned temporal load-snapshot / Application Data Object** | Mneme materialises authoritative resource **values** for activity requirements; **Composition** into resource bucket; Ergo Loader exposes already-loaded Context to Ergon | Loaded constituent values record authoritative state/version bases; Context version distinct; no globally transactionally consistent distributed snapshot; working values not durable authority | U10. Ergon working environment, not closed universe; detailed Information Unit Management Context mapping/schema/version/concurrency CDG-45. |
| D27 | **Expected / Likely Resource Set** in Ergo definition | Ergo development/definition identifies most likely accessed resources; Ergo framework works with Mneme to load efficiently before business logic | Definition information; deliberately non-exhaustive; no universal preloading, schema or durable definition storage choice | U10; C05/F07/16. Detailed declaration/mapping/version-binding contracts CDG-45. |
| D28 | **Pragma Output expressing new Content** | Ergon generates; Mneme manages Output and subsequent information consequences; Unloader returns it across bounded logic boundary | Semantically distinct from changed working Context values; authoritative persistence where required remains Mnemosyne, not Unloader/originating Ergon | U10; no new upstream Information Resource or TaskOutcome/output-rule change. Exact Output representation/routing CDG-45. |
| D29 | **Harmonia-level persistence search metadata** | Mnemosyne Search Management may manage persistence-domain, shard, persistent-service and search/index capability metadata | Supports search against authoritative managed persistence; transient cache not assumed complete; physical index/query representation unresolved | Explicit U11 task decisions; C12/F43/CDG-48. No technology choice or inferred semantic persistence-domain allocation. |
| D30 | **Research Metadata** | Appropriate Harmonia behavioural/execution construct derives AI meaning/relationships across content sets based on proximity; Mnemosyne captures/persists/manages/uses for search | Derived metadata distinguishable from unchanged authoritative source Content/metadata; durable capture does not confer source-content authority; AI executes outside Mnemosyne | Explicit U11 task decisions; C12/F44/CDG-48. Provenance/confidence/schema and specific deriving construct unresolved; no AI/vector/embedding/index choice. |

### Collaboration information responsibility

[U16 ownership/capture](agora-omilia-collaboration.md#information-ownership)
distinguishes Agora-native record/state from source information conveyed through
Rooms and newly derived governed information/activity. Dialogue is not automatically
clinical record content or AuditEvent; authoritative audit remains Kleio. Omilia's
operations do not override AX-05. [Binary custody](agora-omilia-collaboration.md#binary-and-media-custody)
separates native collaboration media from F31/D15 formal binary content now
managed by Sylloge under U17. Persistence mechanics and retention remain deferred.

### Binary-content information responsibility

[Sylloge](sylloge-binary-content-management.md) manages binary-content metadata,
associations with Information Units, location/resolution knowledge, retrieval,
on-demand assembly and knowledge of Harmonia versus external management. Logical
clinical information differs from its binary payload; application metadata and
associations do not prescribe an Information Unit partition, FHIR/DICOM object,
persistence structure or storage technology. No new Domain04 Information Concept
or universal payload representation is inferred.

Sylloge management differs from physical storage ownership. A particular payload
may remain externally held; where Harmonia persistence is required, Mnemosyne may
supply applicable services without becoming the Binary Content Manager. Mneme's
Context/Pragma boundary remains. Agora owns its collaboration record/reference,
not the formal binary merely because a Room conveys it; Omilia remains operational.

### Task and Pragma candidate meaning

The following records [U01/U02](sources.md#u02), preserving [A03](sources.md#a03):

```text
ActionableTaskArchetype (meta-concept; not an instantiated Information Asset)
  -- logically embodied by --> PragmaFactory (candidate)

ActionableTask (work instance)       FulfillmentTask (undertaking)
  -- candidate representation --> Pragma <-- candidate representation --
       WorkOrder / ToDo / Stimulus / Effector forms
```

Shared representation does not imply shared identity, lifecycle, execution
state or outcome ownership. ActionableTask concurrency, outcome participation
and output rules remain orthogonal. TaskOutcome resolves the ActionableTask;
FulfillmentTask does not independently own it. Candidate form discriminators,
factory inputs/outputs and concrete relationships to carrier state are unknown
(CDG-03/04). No exhaustive IA-to-object mapping has been performed. ReportedTask
identity, necessity and meaning remain deferred; no accountable report object
or automatic assurance output is created.

### Pragma management and candidate specialisations

Logical Aggregation and information-management relationships identify responsibility,
without asserting Composition:

```text
Mneme
  Pragma Management
    PragmaFactory — creates/initialises appropriate Pragma representations
    Pragma — managed Application Data Object
    Publishing / Subscription / Pattern — semantic management and evaluation
```

Specialisation/reuse is a separate relationship, directed here from candidate
specialisation to base; it denotes no containment or mandatory implementation
inheritance:

```text
Stimulus (candidate) -- specialises / reuses --> Pragma
Effector (candidate) -- specialises / reuses --> Pragma
Assurance request/input role (candidate) -- requires specialisation semantics --> Pragma
Assurance result/finding role (candidate) -- requires specialisation semantics --> Pragma
```

Stimulus/Effector retain common Pragma process-management, assurance, reporting
and other information-management facilities, instead of DigitalTwin-specific
equivalents. Dokimasia follows the same reuse principle. No detailed schemas,
required cardinalities, lifecycle/state models or final assurance-specialisation
names follow from this view. WorkOrder/ToDo candidate forms remain recorded
without newly deciding their specialisation structure.

Execution of, behaviour upon, or handoff of managed information does not
confer information-management responsibility for that information. Mneme /
Pragma Management manages; Ponos and its specialisations execute; Petasos
provides messaging/recoverable handoff; Ergon and its specialisations provide
bounded behaviour. The authoritative evidence standing of the governed Dokimasia
record/result does not finalise its representation or confer authoritative
durable-state establishment on execution; that remains Mnemosyne responsibility.

Ordinary Ergon can invoke AI internally as an implementation choice. AI Assurance
Ergon instead represents AI processing as an explicit governed assurance activity;
the component's existence/responsibility is established while its inputs/results,
disclosure, provenance and evidence representations remain for CDG-35 discovery.
No additional upstream Information Resource is invented for either activity.

### Semantic shape and state separation

The [Pragma / Topic semantic view](pathways-journeys-and-handoff.md#pragma-and-topic)
preserves Pragma metadata versus Information Unit Container/Content metadata,
multi-level Topic, and source/origin/targetDestination distinctions without a
final schema or changing Domain04's conceptual partition.

```text
Pragma / managed-information state
  ≠ Petasos handoff/delivery obligation state
  ≠ FulfillmentTask undertaking state
  ≠ Ponos runtime execution/progression state
```

Pragma may record governed observations about delivery; operational delivery
ownership remains Petasos. Ponos execution retry and Petasos handoff redelivery
remain distinct. U05 establishes common Mneme-managed Pragma/payload and distinct
consumer FulfillmentTasks for ActionableTask undertakings, while detailed fan-out
reference/copy/version, identity, destination/state/concurrency and lineage
contracts remain [CDG-37](gaps.md#pubsub-fanout). No shared mutable processing-state
or copy-per-subscriber model is established.

Journey D21 is the common observational/assurance representation, owned at the
application level by Energeia, managed as governed information through Mneme and
durably supported by Mnemosyne O&A. Its detailed preservation/construction
contracts remain unresolved. It does not replace TaskOutcome, ReportedTask,
AuditEvent or underlying observed information. A Journey may relate to many
AuditEvents; audit may occur without Journey. Journey is an audit source, not
the Audit Capture boundary ([Kleio history](component-register.md#audit-history-and-evidence)).

## Calliope content and Pylai interaction boundaries

[Calliope](calliope-models-and-datasets.md) distinguishes build-time structural
contracts from current runtime governed content. Dataset application ownership
confers no persistence management; exact domain/structure allocation remains
unselected. [Pylai](pylai-external-interoperability.md) distinguishes the transient
Diadromí from its Communication Information Unit, managed Pragma/Task and durable
PetasosParcel handoff. Neither Diadromí nor Controller has recoverable interaction
state. Retained audit/Journey/operational evidence does not resume the interaction.
The Communication Information Unit schema and Domain04 mapping remain unresolved.
No new Information Family or persistent execution object is inferred.

## Responsibility and state boundaries

| Concern | Current governing meaning / reconstructed candidate intent | What remains incomplete in candidate documentation |
| :--- | :--- | :--- |
| Application-facing information use | Mneme manages activity Context/Pragma through Context Load/Save and Adhoc Resource Access/Search, with active observation/coordination; activities use governed contracts | Legacy raw cache/direct storage paths conflict; complete service/data contracts not reconstructed from code. A01/A02, CDG-11/31. |
| Durable establishment and recovery | Mnemosyne atomically establishes durable state/versions/management metadata; Mneme converges after commit | Cache write-behind cannot establish authority; structure/history/storage slices compete. A01, L13, CDG-10/12. |
| Activity and entity progression | Ponos provides reusable execution; DigitalTwin specialises it for entity activity, with per-Twin pending-work queues and bounded execution resources; Ergon/Behaviour supplies bounded activity | Detailed coordination/control/activation Data Objects and persistence remain unknown. A02/U02, CDG-03/07. |
| Definitions and configuration | Energeia owns Praxis Management Services; Calliope provides semantic reference support; Mneme provides governed Context/Pragma access and Publishing/Subscription/Pattern definitions; Mnemosyne C&G provides semantic persistence responsibility | Detailed representation, lifecycle/release/version binding and physical persistence remain unresolved. U06, CDG-01/06/24/36/40. |
| Information authority | Source assertion credibility/domain authority independent of storage or transport; Calliope governs authority semantics | Historical universal truth/clinical workflow claims remain rejected under AX-06/18, CDG-20/29. U16 resolves roster authority as SEP and assigns Agora membership intent; CDG-21 detail remains. |
| Security/evidence/assurance | Dokimasia reuses Ponos/Ergo/Ergon/Pragma Management for independent governed assurance; no recursive Dokimasia assessment/monitoring and no separate own-execution AuditEvent; governed record/result is assurance-activity evidence | Detailed independence/evidence-access, assurance Pragma representation and evidence-store allocations remain unresolved. A01/A04/U02, CDG-08/09/13–15/35. Governance and controlled observability remain. |

Known commit with degraded convergence is not a failed commit. An unknown commit
effect remains unknown until established; availability failure or denial is not
resource absence. Active generation must represent successfully established
active state, not merely a cache entry token or attempted convergence. These
existing AX-05/14/15 constraints govern candidate contract interpretations even
where legacy APIs or algorithms simplify them.

<a id="persistence-responsibility-domains"></a>

## Mnemosyne persistence responsibility domains

[U06](sources.md#u06) replaces the insufficient Clinical/Operational partition
with three **semantic persistence responsibility domains**:

```text
Mnemosyne — authoritative durable state/version establishment
  ├── Clinical Information
  ├── Harmonia Configuration & Governance (C&G)
  └── Harmonia Operations & Assurance (O&A)
```

These domains do not prescribe three databases, deployed Mnemosyne services,
workloads, schemas or technology products. Clinical Information retains its
established responsibility; this correction does not redesign clinical
persistence or move clinical information into C&G/O&A. A representation's FHIR
or non-FHIR format does not, by itself, determine its semantic responsibility
domain. Candidate classification does not manufacture persistent objects.

| Domain | Semantic classification test | Characteristics and candidate examples |
| :--- | :--- | :--- |
| **Clinical Information** | Established clinical-information persistence responsibility | Existing clinical scope retained; no clinical structure/history/index design selected or changed. |
| **Harmonia Configuration & Governance** | Does the information define how Harmonia is intended, permitted or governed to operate? | Relatively static, strongly governed: Praxis; Pathway/Route, Publishing/Subscription/Pattern definitions; assurance rules/expectations; DigitalTwin definitions/configuration; Ergon/Erga definitions/configuration where appropriate; the **Themis Authorisation Model**, with model ownership/repository semantics retained by Themis; other governed operational configuration. |
| **Harmonia Operations & Assurance** | Does the information represent Harmonia actually operating, observing or assuring activity? | Highly transactional and potentially large: Journey; ActionableTask; FulfillmentTask; relevant Pragma operational information; DigitalTwin operational state/activity; execution/progression observations; handoff observations where governed persistence is required; Dokimasia activity and findings/results; **Kleio Audit Information**, with audit application ownership retained by Kleio; other governed operational evidence/state. |

Themis Authorisation Model → C&G and Kleio Audit Information → O&A are
adjudicated alignments under U08/U07. Other examples retain their existing
classification-candidate standing. No example, by itself, creates an independent
persisted object. Ergon remains behaviour with no discrete Information
Resource by virtue of being Ergon. Operational state remains ephemeral by default
under AX-09; inclusion in O&A does not require all diagnostics, queue mechanics,
runtime fields or observations to be persisted. Meaningful evidence follows
AX-08/09 and Kleio's boundary, including Dokimasia's terminal constraints. Petasos
continues to persist its concrete handoff/recovery obligation state; governed
handoff observations may belong to O&A without transferring that obligation.

### Configuration versus activity pattern

| Configuration & Governance | Operations & Assurance |
| :--- | :--- |
| Praxis | Journey |
| Pathway/Route definition | Observed progression/traversal |
| Publishing/Subscription/Pattern definition | Selection/matching observation |
| Assurance rule | Assurance execution/finding/result |
| DigitalTwin definition/configuration | DigitalTwin operational state/activity |
| Operational policy / Themis Authorisation Model | Observed operational behaviour / Kleio Audit Information |

This recurring semantic split is an architectural classification pattern. It
neither mandates a separate object for each entry nor duplicates the information
referenced by an observation.

<a id="cg-governance-space"></a>

### C&G coherent governance space

> **Harmonia Configuration & Governance SHALL present a single coherent governed
> information space and SHALL NOT be partitioned in a manner that creates
> independent or divergent configuration/governance authority.**

This is a semantic non-sharding guardrail. Replication, caching, clustering,
high availability, read replicas and distributed storage technology remain
permitted where they preserve coherent governance authority. No technology,
replication or consistency mechanism is selected. “Single” describes governed
meaning, not one machine, database or process.

<a id="oa-partitionability"></a>

### O&A horizontal partitionability and Journey locality

> **Harmonia Operations & Assurance SHALL permit horizontal partitioning of its
> persistent information without changing the semantic identity, provenance,
> relationships or governed meaning of that information.**

Partitionability is an architectural property; it does not require multiple
shards in R1.x/R2.x. Physical partitioning/shard model, shard key, replication
and consistency mechanisms remain downstream decisions. Journey, DigitalTwin
identity, organisation, tenant, time, operational domain, geography and workload
are not selected partition keys.

Journey offers a potentially useful locality concept, without being an
established shard key. The persistence architecture should permit efficient
reconstruction and use of Journey's comparatively small discrete information
and its references to potentially substantial distributed activity/evidence.
**Aggregate semantically; do not duplicate governed information indiscriminately.**
This objective establishes neither a physical schema nor a partitioning,
correlation or evidence-reconstruction algorithm.

### Audit persistence alignment and release boundary

**Kleio Audit Information → Mneme → Mnemosyne Harmonia O&A** aligns audit
information with operational/assurance persistence without making audit itself
assurance. Authoritative AuditEvents remain immutable; supporting/prepared views
remain distinguishable. The [longitudinal evidence rule](component-register.md#audit-history-and-evidence)
references authoritative source history with identity/version/provenance/temporal
detail, permitting necessary replication only when that historical state cannot
be retrieved reliably.

R1.x/R2.x permits **independent authoritative audit persistence targets** through
Mneme/Mnemosyne. At least one configured target capable of durably establishing
required audit information suffices for continued operation; none means Harmonia
DOWN/fail closed. Active cache, logs or Journey cannot substitute for durable capture.
Targets are not assumed replicas, identical/complete histories or a unified corpus.
**Cross-target audit harmonisation is SEP for R1.x/R2.x**: no automatic convergence,
reconnection reconciliation or unified longitudinal audit-history guarantee.
No target count, selection, one/some/all write strategy or future reconciliation
mechanism is prescribed. This is compatible with O&A partitionability and establishes
no general O&A topology ([CDG-43](gaps.md#kleio-detailed-contracts)).

### Application ownership versus persistence responsibility

```text
Energeia owns Praxis Management Services     Energeia owns Journey Management Services
                   |                                           |
                   v                                           v
       Mneme — Context and Pragma Management / active use
                   |                                           |
                   v                                           v
       Mnemosyne — Harmonia C&G                    Mnemosyne — Harmonia O&A
                  durable persistence responsibility
```

Themis owns its Authorisation Model and PRP authoritative repository semantics;
durable persistence may use **Themis PRP → Mneme → Mnemosyne C&G**, without a
dedicated Themis database. Kleio owns Audit Information Management; durability
uses **Kleio → Mneme → Mnemosyne O&A**. Neither delegation transfers application
ownership to Mneme/Mnemosyne or changes C&G coherent governance authority.

Application ownership does not confer persistence ownership. Persistence does
not confer Praxis/Journey application semantics on Mnemosyne. Mneme remains
the Context/Pragma management layer; Energeia operational observation does not
transfer ActionableTask, FulfillmentTask or Pragma information management from
Mneme. AX-05 durable truth, distinct active generation/version and originating
information authority remain controlling across all three domains.

Sylloge likewise owns Binary Content Management while Mnemosyne may supply
applicable Harmonia persistence services for content/metadata. This does not
require local custody of externally managed payloads, add a persistence domain
or alter the Clinical/C&G/O&A distinctions.

## Context, persistence outcomes and search responsibilities

[The canonical management view](context-and-persistence-management.md) establishes
Context's temporal value snapshot and bucket Composition, distinguishes Loader/
Unloader from Mneme Load/Save, and preserves changed values versus new Pragma
Outputs. Activity Context does not redefine Domain04 Information Unit Management
Context; detailed mapping remains CDG-45.

Mnemosyne is the **Persistence Manager**: transaction/service oversight, shard
management, compartment validation/reapplication, Themis-owned PEP hosting,
persistence logging and authoritative outcome reporting. API success is not itself
an authoritative Harmonia outcome. Logging is distinct from Kleio audit; the
[Kleio mandatory capture authority](component-register.md#kleio-capture-authority)
is governed intrinsic platform authority, separate from the audited Identity's
PEP-Criteria. Mnemosyne hosting/enforcement remains Themis-owned; no equivalent
intrinsic authority is granted to persistence components.

**Search Management** performs authoritative persistence search behind Mneme
Adhoc Search, with optional D29 capability metadata. **Research Metadata Management**
handles distinct D30 derived metadata usable by search; source Content remains
unchanged and AI derivation executes outside Mnemosyne. Detailed provenance,
confidence, schemas/query/outcome/compartment contracts and technology remain
[CDG-47/48](gaps.md#persistence-management-contracts). No physical structures are
selected for Context, search metadata or Research Metadata.

## Historical candidate persistence structures

These are source-described structures, not a selected schema or technology
architecture. Structure names are retained to make competing designs reviewable.
The historical combined operational structures do not implement or supersede
the C&G/O&A semantic split established above. Reconciliation into physical
structures remains downstream ([CDG-40](gaps.md#persistence-realisation)).

| Structure / variant | Documented information and persistence responsibility | Standing / conflict |
| :--- | :--- | :--- |
| **Clinical/FHIR hybrid relational-document row** | `hie_fhir_resources`: surrogate `id`, type/logical ID, durable `version_id`, full `resource_json`, lifecycle flag and update timestamp; unique `(resource_type, fhir_id)`. Mnemosyne clinical slice. | L16 database-schema §§1–2 and L15 persistence. Describes one current row per resource; does not establish where complete historical versions are preserved. CDG-12. |
| **Per-version clinical row** | Same named table described with `(res_type, res_id, res_version)` or `(resource_type, resource_id, version_id)` key, resource text/JSON and security/update fields. | L16 persistence-lifecycle §4.1, L20 register. Incompatible key/history description retained; no “newest document wins” selection, CDG-12. |
| **HAPI-native FHIR persistence/index structures** | Clinical persistence/index/history responsibility described through HAPI JPA by L09/L18; L17 explicitly avoids default `HFJ_RESOURCE`/index tables in favour of custom rows. | A01 §4 prefers engine capabilities where suitable but does not settle these candidate structure variants. Technology detail remains downstream; candidate history/index architecture is unresolved, CDG-12. |
| **Non-FHIR operational current-row store** | `hie_operations_resources`: surrogate `id`, `(object_type, object_id)` uniqueness, version, full `data_json`, lifecycle/created/update fields. | L16 database-schema §2.2. Payload categories include telemetry/configuration; not all operational state merits durable retention. CDG-13/28. |
| **Operational history/sequence variants** | Same named table described with `(resource_type, resource_id, resource_version)` key (L16 lifecycle), or `(operation_id, sequence_id)` (L20 register). `hie_task_sequences`, TaskSequenceEntity and PragmaAuditEntity also appear in L09. | Candidate durable definition/progression/history structures conflict or lack field/lifecycle contracts. CDG-06/12/13; no separate component inferred from an entity name. |
| **Historical Kleio evidence structures** | Legacy FHIR AuditEvent/Provenance in clinical cache/store versus operational audit/lineage rows/streams are retained descriptions. | U07 now establishes Kleio authoritative audit management, immutable AuditEvent and O&A semantic durability through Mneme/Mnemosyne. Earlier allocation variants do not select current structures; supporting representations need not be AuditEvents. Detailed structure/query/retention CDG-13/43; no audit database or target topology chosen. |
| **Agora mapping/transaction records** | `agora_resource_mappings`: identity/context mappings; `agora_as_transactions`: processing markers/timestamps, as historical structure names only. | U16 establishes Agora native collaboration ownership and Omilia operational mechanics; AX-05 remains. Legacy Mnemosyne/Core JPA/Synapse allocations are not selected storage architecture; exact structures/partial effects CDG-19/22/52. |
| **Homeserver discourse/room/membership store** | Historical room/state/event/membership storage; now distinguished from Agora application ownership and Omilia.Server operations. | U16 replaces discourse-purge/clinical-submission assumptions with capture/derivation. Storage allocation, persistence mechanics and retention deferred CDG-22/23/52; no originating clinical authority or alternative authoritative store. |
| **Petasos durable queues / Parcels / handoff recovery records** | All purpose-defined queues carry PetasosParcel by value; associated information predominantly by reference. ActionableTask handoff may reference Mneme-managed Pragma/payload and maintains necessary obligation/recovery state. | U05/U09 correct historical L13/L29 work-payload language. No universal reference-only transport, literal FHIR equivalence, Parcel/journal schema or topology selected. Queue purpose/delivery/operational state detail CDG-16/17/19/37/44. |
| **Mneme active working structures** | Resource-keyed active payloads, task/Pragma/checkpoint working state, sequence definitions, observation/coordination records and telemetry working sets. | A01 governs reconstructability/non-authority. Legacy cache names/distribution/TTL/dedup/store lists differ (L09/L10/L17/L19/L20), CDG-27/31. Cache product is not the persistence architecture. |
| **Reference/terminology and binary/media structures** | U13 Models build artefacts and DataSets runtime content through Mneme/Mnemosyne; U16 Agora native media; U17 Sylloge formal Binary Content Management, with physical custody potentially external and applicable persistence services through established responsibilities. | DataSet structures/version/history and ontology detail CDG-24/49; Sylloge detailed storage/representation/access/lifecycle design CDG-25; collaboration persistence mechanics CDG-52. No ValueSet database, object store or Media Server selected. |
| **Simulation state** | Seeded fixtures and ephemeral simulator/scenario data outside production dependencies. | L22. Kept as support context, not a production persistence tier. |

### Lifecycle and search limits

L13 ADR-020 distinguishes domain lifecycle updates from Mneme eviction, message
compaction, archival and physical disposal. Governed information has no normal
physical DELETE path; a relational `is_deleted` column is an existing schema
artefact, not a universal lifecycle semantic. Archival/purge/disposal are not
designed by this task.

L15 describes persisted-resource search/filtering and external FHIR searchset
Bundles; L09/L17 describe indexing support. Those descriptions do not establish
an internal managed search-result component, object identity, persistence schema,
ownership or retention contract. That candidate-documentation boundary remains
CDG-11/12/31; this reconstruction has not assessed Domain04 search coverage.

Historical technology names (Infinispan, PostgreSQL, HAPI FHIR, Artemis and
Synapse) explain source mechanisms. They neither create logical components nor
settle persistence responsibility, information authority, storage topology or
future product selection.
