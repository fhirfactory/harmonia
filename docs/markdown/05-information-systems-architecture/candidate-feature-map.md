<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Solution Feature Map

**Standing: features attributed to the Harmonia Candidate Information Systems
Architecture, not an upstream-derived required-feature map.** [Scope](README.md)
and [source standing](sources.md) apply. No R1.x/R2.x necessity, release allocation,
coverage score, implementation readiness or MVP decision is established here.

“Documented” denotes a source-described candidate behaviour. “Constrained” means
existing RADS governs the high-level responsibility while detailed candidate
behaviour remains subject to review. “Proposal/unallocated” preserves intent
without inventing a component owner. Feature IDs are local navigation keys.

“Adjudicated candidate” records the bounded [U02–U17 review decisions](sources.md#u02) and [U18](sources.md#u18)
within Step A; those corrections did not themselves establish whole-candidate
acceptance or Step-B upstream traceability. The explicit B1 task now confirms
**A4 — PASS**, accepting the candidate as the Step-B baseline. The
[B1 method](traceability/step-b-method.md) governs later traceability; this
feature map adds no B2 mappings or component allocations for the worked example.
Common features below are reused by specialised
components under the [specialisation principle](README.md#specialisation-principle).

| ID | Candidate feature / behaviour | Documented allocation and collaboration | Significant information interaction | Standing, evidence and incomplete meaning |
| :--- | :--- | :--- | :--- | :--- |
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope-Models](../../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F01 | Design/build-time models, Metadata Types, Criteria Definitions, Topics, Filter Types/Elements and core element libraries | [Calliope-Models C41](component-register.md#c41); Themis and Mneme own applicable runtime use | Structural contracts, security/assurance definitions and useful human/IDE/AI development representations of known runtime datasets | U13/U15; normal development/build-time library dependency, Design → Code → Build → Test → Release → Deploy. Build knowledge is not current membership authority; no runtime structural bootstrap. Mechanisms CDG-49. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope](../../../enterprise-architecture/continuum/application/application-component/466ad8f75a954c0581b5bba090443849.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F02 | Canonical conversion support and interface-specific semantic processing | Calliope reusable support; PylaiPathway Terma-paired developer Ergo.BusinessLogic interprets/maps/transforms Communication Information into Task.output | Raw communication in Task.input; native resources and applicable outputs | U14 settles ingress Loader versus BusinessLogic separation. General converter/activity allocation remains CDG-05; no selected implementation library/route. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Themis](../../../enterprise-architecture/continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F03 | Identity authentication integration; Authorisation Model administration, resolution and distributed enforcement | [Authentication SEP](component-register.md#identity-authentication), external OIDC/OAuth IdP; [Themis C02](component-register.md#c02) owns PAP/PRP/PDP/PEP, governed management/approval through Ergo/Praxis, human interaction through Iris | Conjunctive Criterion; additive collections/AuthorisationRoles/direct grants; PractitionerCapacity context; Identity-specific precomputed PEP-Criteria through PEP-Context | U08/U12, AX-04/07/14/17/18. Keycloak + PostgreSQL default development/test/POC IdP, no product-specific dependency. Deterministic local PEP; no matching effective Criterion means DENY; no negative Criteria. Valid-context PDP-loss behaviour retained; forwarding subject to FORWARD with originating/acting provenance. Themis deferrals CDG-42; U15 resolves Kleio mandatory capture under governed intrinsic platform authority. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ponos](../../../enterprise-architecture/continuum/application/application-component/b521762c1ea444b48cd5f66be3752d30.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F04 | Governed execution ingress and activity dispatch | [Ponos C04](component-register.md#c04) with consumer undertaking, Mneme context resolution, Petasos handoff and Themis; DigitalTwin/Dokimasia reuse common execution | Handoff identity/reference, governed Pragma/context, undertaking and Praxis expected/permitted operational context | U02/U05/U06. Mneme selects information; Petasos establishes concrete handoff; Ponos executes using Praxis without owning it. Detailed control contracts CDG-01/03/16 remain open. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ponos](../../../enterprise-architecture/continuum/application/application-component/b521762c1ea444b48cd5f66be3752d30.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F05 | Execution concurrency, timeout, failure control and execution retry | Ponos execution support reused by DigitalTwin/Dokimasia, subject to specialised contracts; Ergo/Ergon activity contracts | Interest fan-out across Ergon types differs from ActionableTask.ExecutionConcurrency across instances of the same type, each through its own FulfillmentTask | U02/U05/U19. Execution retry differs from Petasos redelivery. No intra-Twin Behaviour concurrency or automatic stopped-Twin resumption follows from common facilities; F32 governs. Detailed mechanisms CDG-03/07/08/16/17/54. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Energeia](../../../enterprise-architecture/continuum/application/application-component/fb3f37a6ac614415ac486a6de9d35994.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F06 | Checkpoint, reporting and destination progression observation | Ponos/Ergon/Erga contribute execution observations; Petasos owns handoff state; Mneme manages recorded information; Energeia owns Journey/operational reporting; Iris presents | Checkpoints, destination observations and references; Journey for Pathway, Pub/Sub and mixed activity | U02/U04–U06; REC-002 retained. Observations do not transfer Petasos obligation ownership. Exact information/evidence relationships CDG-14/18/38; Dokimasia terminal rules remain. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ergon](../../../enterprise-architecture/continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F07 | Reusable activity contract, bounded transformation/validation/enrichment and meaningful collections | Praxis manages concrete R1.x/R2.x Context definitions; Ergo C05 declares likely resource set; Mneme Context Load materialises; Ergo Loader exposes Context; Ergon C23 acts; Unloader returns results to Mneme Context Save; Erga C24 assembles/manages/observes | Loaded resource values, changed working values and new Content as Pragma Output remain distinct; output processing may yield multiple independent/continuation Pragma; bounded Adhoc acquisition supported | U10/U18 refine U01/U02/A05. Resource set non-exhaustive; Unloader is not persistence; originating Ergon owns no shard/transaction/generic conflict resolution mechanics. CDG-05/45/53; R3.x dynamic Context and dynamically configured reusable/generic Ergo deferred. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ergon](../../../enterprise-architecture/continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F08 | ADT patient identity/demographic and encounter processing | Documented ingestion/identity/demographic Ergon activity; Praxis composition detail remains open | Source identifiers, Patient/Encounter/Location and progression | L03/L18/L23; candidate. Legacy EMPI/master-ID interpretation retained as conflict CDG-29; no new MPI component. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ergon](../../../enterprise-architecture/continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F09 | Order routing and diagnostic-result processing | Documented ORM-routing and ORU-processing Ergon activity, Petasos handoff and Pylai external dispatch | Order ServiceRequest; result Observation/DiagnosticReport; destination metadata | L03/L23/L28. Concrete workflow/branch allocation and effect recovery incomplete, CDG-05/16. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ergon](../../../enterprise-architecture/continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F10 | Multi-destination distribution and delivery observation | Distribution Ergon, Mneme Context/Pragma management, Petasos persistent obligations, Pylai external observation and Iris/Journey use | Destination-specific initiation/ACK/error/time observations; Task output information | U05 preserves REC-002 granularity and separates observation from Petasos operational obligation. Distribution examples do not define Pub/Sub copy semantics. Parent progression/resolution CDG-18; Pub/Sub detail CDG-37. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Energeia](../../../enterprise-architecture/continuum/application/application-component/fb3f37a6ac614415ac486a6de9d35994.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F11 | **Praxis Management Services** — candidate responsibility/service set | **[Energeia C39](component-register.md#c39) owns Praxis C06**; governed representations through Mneme; Mnemosyne C&G durable responsibility | Definition/registration, discovery/retrieval, operational/interface configuration, Topic policy, Pathway Aggregation [0..1], applicable Context definitions, segment criteria/outcomes, continuation semantics/housekeeping, assurance rules and Praxis-wide constraints | U06/U18 adjudicated candidate. Praxis defines fulfilment; the framework processes/routes, without Praxis orchestration/choreography. Detailed representation/lifecycle/in-flight effects CDG-01/06; decomposition CDG-39. Topic inclusion necessity unresolved. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Praxis](../../../enterprise-architecture/continuum/application/data-object/0f961462340348b290fd844357d54d67.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F12 | Historical workflow composition, ordering and dependency/condition evaluation | Historical Praxis/Ponos/Erga representations retained as provenance; Energeia now owns Praxis envelope; Ponos executes; F36 records Pathway/Route control | Competing definition/composition/execution representations | U06 resolves broader Praxis meaning without adopting historical schemas or engine equivalence. Detailed Praxis-to-activity relationships and definition-to-instance model remain CDG-01/06; no Composition inferred. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Petasos](../../../enterprise-architecture/continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F13 | Durable message/event queue creation, operation and status; persistent handoff/backlog/recovery | [Petasos C10](component-register.md#c10), first-class shared messaging/event capability for architecturally defined purposes | Every queue carries PetasosParcel by value; associated information predominantly by reference. ActionableTask Parcel references Mneme-managed work; each undertaking establishes its own FulfillmentTask | U05/U09, AX-10. Inter-Ergon, communication/notification and other established event sharing; no final taxonomy or broker topology. Petasos owns queue state; Energeia observes/reports. Governed FORWARD uses local Themis PEP/PEP-Criteria through PEP-Context. Detail CDG-16/19/34/37/44. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Petasos](../../../enterprise-architecture/continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F14 | Designated deliberate replay | Petasos transition anchor and governed executing activity; Kleio evidence where policy requires | New linked processing attempt; original transition remains unchanged | L13 ADR-015. Replay/redelivery/reprocessing/result redelivery distinct; operational tool terminology CDG-17. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Kleio](../../../enterprise-architecture/continuum/application/application-component/fefda110e2274884ad58906733606efd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F15 | Authoritative Audit Capture, immutable audit history and historical context preparation/retrieval | [Kleio C03](component-register.md#c03); clients report significant occurrences through Kleio contract; Kleio constructs FHIR AuditEvent; Mneme/O&A supplies management/durability | AuditEvent distinct from Journey, Provenance and derived/prepared historical context. Reference authoritative source history by default; necessary replication only when audited historical state cannot be retrieved | U07/U15; AX-07/08/09. Mandatory capture uses Kleio-specific governed intrinsic platform authority; originating Identity is evidence, not capture authority. Required durable capture is a precondition of Harmonia operation; one capable independent configured audit target suffices, none means DOWN/fail closed. Harmonisation SEP. Audit is neither Governance nor Assurance; no UI clickstream/security assurance. Dokimasia terminal rules remain; detail CDG-43. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mneme](../../../enterprise-architecture/continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F16 | **Context and Pragma Management — four principal exposed services** | **Mneme C09: Context Load, Context Save, Adhoc Resource Access, Adhoc Search**; supporting contracts C11 and Pragma Management C28 | Versioned temporal Context composes authoritative resource values; Context version differs from resource state/version; Pragma expresses inputs/outputs and new Content | U10, AX-05/11/12/14. Context Load preferred/performance-optimised; bounded runtime acquisition permitted, potentially more expensive. No fifth service or exhaustive preload; detail CDG-45. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mneme](../../../enterprise-architecture/continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F17 | Supporting authoritative state progression and guarded convergence | Mneme handles resulting Context/Pragma information consequences; Mnemosyne establishes/reports durable state; C11 retains supporting contract intent | Changed Context values and Pragma Outputs; proposed state/expected version; authoritative result, uncertainty and active convergence | U10/U11 and AX-05/14/15; internal supporting behaviour, not additional exposed Mneme services. Ergon/Unloader is not persistence; physical DELETE not lifecycle; old write-behind CDG-10, detail CDG-45/47. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mneme](../../../enterprise-architecture/continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F18 | **Adhoc Resource Access / Adhoc Search** for bounded runtime information needs | Mneme exposes both activity/context-facing services; Mnemosyne Search Management performs underlying authoritative persistence search | Dynamic external-system information, deep interrogation outside reasonably known boundaries and emerging requirements; resource values/search results | U10/U11. Supplementary to preferred Context Load, potentially materially more expensive; transient cache not complete searchable space. Registry/clinical discovery and history intent remains L13 ADR-010/L15/L23; detailed APIs/query/results/history CDG-31/45/48; Pylai external boundary retained. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mnemosyne](../../../enterprise-architecture/continuum/application/application-component/7985e647bf9e4ca3ba70faf9e3e4ae02.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F19 | Durable Clinical Information / Harmonia C&G / Harmonia O&A preservation | [Mnemosyne C12](component-register.md#c12) provides three semantic persistence domains; Mneme governed Context/Pragma access; Energeia Praxis/Journey, Themis Authorisation Model and Kleio Audit Information retain application ownership | Clinical information; C&G expected/permitted configuration and Authorisation Model; O&A observed operational/assurance information and authoritative audit history | U06–U08 and AX-05. C&G coherent governance authority; O&A horizontally partitionable without selected key/topology. Kleio independent audit targets have no R1.x/R2.x harmonisation guarantee and imply no general O&A topology. Clinical unchanged; historical structures CDG-12/13 and physical implementation CDG-40. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Provider Registry solution collaboration](../../../enterprise-architecture/continuum/application/application-collaboration/9305dc8d55b34b70950190e28b5464cf.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F20 | Governed provider-directory request-for-change processing | [Registry C13](component-register.md#c13): Pylai/Themis/Petasos/Ponos/Praxis/Erga/Mneme/Mnemosyne; Iris initiates/views | Seven directory resource kinds; change Pragma, validation errors, resulting resource/version | L15/L18/L23. Automated approval is documented candidate behaviour, not universal policy; detailed lifecycle/external status CDG-03/32. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Pylai](../../../enterprise-architecture/continuum/application/application-component/e3e4147fea3640198dc251ed41ee854d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F21 | External interoperability across messaging, synchronous/asynchronous RMI, APIs, files, polling and other styles | [Pylai C14](component-register.md#c14), Terma/Diadromí; PylaiPathway / PylaiBroker / PylaiServer patterns | Communication Information, external identity carriage and externally permitted representations/results | U14; AX-02/13 non-destructive fail-closed publication. Diadromí transient/non-recoverable; durable acceptance distinct from protocol ACK; communication-local retry only while interaction/Diadromí active; application meaning remains with applicable execution. Detail CDG-32/34/50; Strategy conflict CDG-51 closed by U15. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Agora](../../../enterprise-architecture/continuum/application/application-component/d1c4053e68384080b8eb79d2008632ba.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F22 | Native collaboration and controlled managed Rooms/Spaces/membership | [Agora C18](component-register.md#c18) controls semantics, lifecycle/membership intent and Harmonia publication; [Omilia.Server C47](component-register.md#c47) performs operations; Themis governs | Agora-native dialogue, collaboration state/references/media; other Harmonia information retains its originating owner | U16 replaces projection-only/roster-driven authority. Roster Management SEP: applicable Shift Start/End only for R1.x/R2.x; PractitionerCapacity is not a roster/access grant. Exact taxonomy/derivation and optional projections deferred CDG-21/23/52. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Harmonia.Omilia.Server](../../../enterprise-architecture/continuum/application/application-component/369907025d06492bb414a72cea834a77.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F23 | Bidirectional collaboration operations/activity boundary; historical transaction deduplication intent | Omilia.Server Application Services API: Agora requests operations and receives dialogue/activity in all managed Rooms; Petasos mediates wider execution collaboration | Operation intent/results, Room activity and collaboration transaction information | U16 settles owners and direction, not exact API or Matrix endpoint mapping. Historical AS transaction ID/status mechanisms remain L12; partial publication, marker failure and uncertain effects CDG-19; storage/API detail CDG-22/52. No exactly-once claim. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Agora](../../../enterprise-architecture/continuum/application/application-component/d1c4053e68384080b8eb79d2008632ba.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F24 | Defined managed-Room interaction capture and collaboration-record retention | Agora captures/derives applicable Communication Information Units and/or Pragma; established governed execution/information management handles new information/activity; Kleio handles audit-worthy occurrences | Original Agora dialogue/action retained as collaboration information; derived information is new and does not transfer source-record ownership | U16 replaces generic clinical-submission/purge handoff. Not every message becomes Pragma or clinical record content; dialogue/logging are not AuditEvent. Representation, lifecycle, retention and audit catalogue deferred CDG-23/52. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope-DataSets](../../../enterprise-architecture/continuum/application/application-component/53bf6815c7a54ed0abde9b1aad311843.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F25 | Proposed clinical terminology lookup/search, ValueSet expansion/validation and selective authoring | Calliope-DataSets owns governed mutable content; detailed service allocation and Iris authoring remain unestablished | Governed terminology content versus proposed query/expansion/validation results | U13 establishes runtime repository/management, not every L27 proposed service or authoring feature. Ontology boundary and detailed contracts CDG-24/49; no terminology product selected. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Iris](../../../enterprise-architecture/continuum/application/application-component/e927c1af96c64fcd979be659bee2cbe3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F26 | Contextual clinical/admin interaction and user requests | [Iris C15/C16](component-register.md#c16), mediation and governed services | Clinical timeline/context, directory views, user assertions/proposals/work queue | L11/L18/L23; A02 preserves non-authority. Authoring maturity and backend clinical/diagnostic distinction CDG-24/26. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Energeia](../../../enterprise-architecture/continuum/application/application-component/fb3f37a6ac614415ac486a6de9d35994.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F27 | Operations presentation, health/interface inspection and Journey reporting views | Iris Console/mediation presents; **Energeia owns Harmonia operational observation/reporting F39 and Journey Management F38**; contributors retain observation roles; external IT/Ops manages technical substrate | Technical health/backlog diagnostics and governed Journey/Praxis operational context | U06 distinguishes two management planes. Iris owns presentation, not Harmonia operational management; detailed projection/provider/lifecycle contracts CDG-28/38. R1.x/R2.x grants no autonomous adaptation; Dokimasia terminal rules remain. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

| F28 | Developer/operator authoring, inspection, diagnostic control and coherent activity-library management | [Support C22](component-register.md#c22) associated with Ponos/Praxis/Hestia/Pylai/Iris; Erga collections support related Ergon change management/testing/versioning/release | Configuration, definitions, library/assembly concerns, reload/inspection requests and transient observations | U02 adds explicit Erga collection purpose; L02/L19/L21/L23. Detailed library contracts remain candidate; tool permission/evidence and direct-store access CDG-11/28; no implementation plan. |
| F29 | Synthetic scenarios, failure/security/diagnostic verification | [Paradeigma C21](component-register.md#c21) outside production dependencies | Seeded synthetic patient/provider/order/result data, scenario/failure fixtures | L22; established isolation. Simulation state is not production persistence. |
| F30 | Local-directory synchronisation, reconciliation and enrichment | Documented shared-component proposal; precise runtime allocation unestablished | External/local directory facts, source/provenance, endpoint capability and sync state | L24 candidate intent retained; CDG-33. No national mechanism, upstream requirement mapping or implementation waves adopted. |
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Harmonia.Sylloge](../../../enterprise-architecture/continuum/application/application-component/feea742463b84021b66853d287d8916f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F31 | **Binary Content Management** for formally managed clinical/operational binary content | [Harmonia.Sylloge C49](component-register.md#c49) owns governed metadata association, resolution, retrieval and assembly; Mnemosyne may supply applicable persistence services. Agora retains native collaboration media and references/activity; Omilia retains collaboration operations | Binary-content metadata; Information Unit associations; location/resolution and custody knowledge; retrieval and on-demand assembly from one or more payloads. Logical information differs from binary payload | U17 resolves [CDG-25 responsibility](gaps.md#binary-custody-question). Physical custody may remain external; no mandatory copying into Harmonia. Storage, representations (including historical DocumentReference examples), APIs, algorithms and lifecycle mechanisms remain deferred. L19/L10/L17 are supporting evidence, not selected design. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [DigitalTwin](../../../enterprise-architecture/continuum/application/application-component/3f64d339648246e99e8866c8089f8960.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F32 | Entity-relative Behaviour execution and single-input serialisation | [DigitalTwin C19](component-register.md#c19) specialises Ponos; Behaviour [1..*], each through Praxis → Pathway → exactly one Ergo; Mneme manages Context/Pragma | One Stimulus/Effector queue; identifiable BehaviourStimulus/BehaviourEffector per interested Behaviour; sequential execution with fresh Context after predecessor completion/save on the normal path; open governed outputs | U02/U19 preserve A02 entity boundary. Developer sequencing policy through framework API; unspecified order without configuration. Applicable sequence stable for each input; changes apply to subsequent inputs. ContinueBehaviourOnFailure contains failure to the current execution, leaving the Behaviour active and other interested Behaviours and queue processing unaffected. StopBehaviourOnFailure stops the failing Behaviour immediately, lets remaining interested Behaviours complete the current input, then halts the Twin. StopDigitalTwinOnFailure terminates the failing Behaviour and halts the Twin immediately, preventing remaining current-input and subsequent queued processing. Queued inputs remain pending authorised Administrative Function/Ergon resume; stopped-Behaviour recovery deferred. Concurrency across independent Twins, no thread per Twin. [DesignNote](design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md); detail CDG-07/54/55. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Dokimasia](../../../enterprise-architecture/continuum/application/application-component/d739c75dffe2453081735710b285f03b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F33 | Independent governed assurance on common execution/activity platform | [Dokimasia C20](component-register.md#c20) specialises Ponos and executes Dokimasia Ergon / AI Assurance Ergon where selected; Mneme manages Context/Pragma | **Praxis is assurance basis; Journey is observed evidence subject**; applicable Pathway/Route expectations contribute through Praxis; findings/results and candidate Pragma roles | U02/U04–U06 preserve A04 independence. Inference cannot replace observations; no mandatory algorithms or new assurance Information Family. Terminal rules retained; detail CDG-08/09/35/38. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Pragma Management](../../../enterprise-architecture/continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F34 | Archetype embodiment, Pragma management and distinct work/undertaking representations | [Mneme Pragma Management C28](component-register.md#c28) logically aggregates [PragmaFactory C08](component-register.md#c08) and manages [Pragma C07](component-register.md#c07) | Factory creates/initialises representations associated with ActionableTaskArchetype meta-concept; distinct ActionableTask/FulfillmentTask forms; candidate Stimulus/Effector specialisations | U01/U02/A03. Meta-concept is not an instantiated Information Asset. Common Pragma management/process/assurance/reporting reused; factory/form details CDG-03/04/09. No TaskOutcome/ReportedTask adjudication. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [AI Assurance Ergon](../../../enterprise-architecture/continuum/application/application-component/8fe50975372b4d399a4bcb9f3d9348f7.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F35 | Explicit governed AI participation within Dokimasia workflow | [AI Assurance Ergon C27](component-register.md#c27) candidate specialisation of Dokimasia Ergon, executed by Dokimasia with common platform contracts | AI activity's context/input, result and assurance evidence interactions require discovery | **Existence/discrete responsibility established by U02; detailed architecture candidate/discovery.** Distinct from ordinary Ergon invoking AI internally; not all AI use allocated here. CDG-35 discovery precedes final implementation modelling; no technology/provider selected; terminal rules apply. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

| F36 | Controlled endpoint interaction and routing | Pathway defines conceptual Ergo sequence through endpoint interaction/Route(s); Route orders PathwaySegments; Traversal preserves encapsulation; framework performs Praxis-defined continuation processing/routing | Continuation Pragma role progresses current Praxis; independent resulting Pragma need not advance it; meaningful observations contribute to Journey | U04/U06/U18. Praxis Aggregation: Pathway [0..1]. Detailed control/definition contracts CDG-01/06; continuation representation CDG-53. No generic-PathwayElement Route or new component/type. |
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Pragma Management](../../../enterprise-architecture/continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F37 | Provider publication filtering and consumer subscription selection | Mneme / Pragma Management provides Publishing, Subscription and Pattern semantics; Petasos establishes concrete persistent handoff obligation to destination | Governed Pragma/Unit metadata, Topic/source, semantic eligibility and eligible recipient/consumer relationships; independent output-derived Pragma may participate without continuing the originating Pathway | U05/U06/U18 preserve asymmetric meanings and independent governed use. Mneme does not own queue binding/destination mechanics; Ponos executes. Pattern/lifecycle CDG-36 and detailed fan-out CDG-37 retained. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Journey](../../../enterprise-architecture/continuum/application/data-object/359b901df27f4b43b5a2ff14a8484e34.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F38 | **Journey Management Services** — candidate responsibility/service set | **Energeia C39 owns Journey C35**; Mneme governed Context/Pragma access; Mnemosyne O&A durability; established contributors retain observations/references | Meaningful observed Praxis progression; continuation lineage is an important thread, not complete Pragma provenance. Small discrete representation references relevant activity/evidence for reporting/assurance | U06/U18 preserve U04/U05 factual observation and Pub/Sub-only Journeys. Independent outputs may establish other Journeys; Mneme may manage richer provenance. Construction/reference/correlation/retention CDG-38, decomposition CDG-39; no indiscriminate duplication or Journey shard key. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Energeia](../../../enterprise-architecture/continuum/application/application-component/fb3f37a6ac614415ac486a6de9d35994.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F39 | **Harmonia Operational Observation and Reporting** | Energeia C39 manages/observes Harmonia as Harmonia; Mneme supplies governed information; contributors retain operational responsibilities; Iris presents; external IT/Ops manages platform | Work-to-be-done/being-done/done, ActionableTask obligations, FulfillmentTask activity, Journey progression, stalled/incomplete activity, Praxis context, abnormal behaviour and information for reporting/assurance | U06: R1.x/R2.x **Observe → Report**; observations confer no autonomous workload/route/Pathway/PubSub/resilience/capacity/Praxis adaptation. Service decomposition and detailed reporting contracts unresolved CDG-28/39. No upstream capability invented. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Energeia](../../../enterprise-architecture/continuum/application/application-component/fb3f37a6ac614415ac486a6de9d35994.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F40 | **Adaptive Load and Resilience Service — R3 future/deferred direction** | Future Energeia operational-management capability; no subordinate component or detailed service architecture selected | Governed operational observations may support **Observe → Analyse → Decide → Adapt** with feedback | U06 direction only, outside R1.x/R2.x behaviour. Policy model, algorithms, control architecture, resilience/load mechanisms, infrastructure integration and AI decision mechanism deliberately deferred CDG-41; not missing R1.x/R2.x architecture. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resource Harmonisation Manager](../../../enterprise-architecture/continuum/application/application-function/a6580e0b9f0f40c9a33d731d20110057.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F41 | **Resource Harmonisation Manager — internal exceptional delivery capability** | Mneme C40 identifies/manages exceptional conditions; appropriate Harmonisation Ergon uses established work/execution contract and common Ponos | Relevant condition/state/Context and new governed work; resulting information returns to Mneme | U10: approximately 5% or less at worst is design expectation, not SLA; efficient normal path; no domain harmonisation by manager, resumed originating Ergon or duplicate execution framework. Candidate Ergo Library only; CDG-46. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mnemosyne](../../../enterprise-architecture/continuum/application/application-component/7985e647bf9e4ca3ba70faf9e3e4ae02.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F42 | **Persistence oversight and authoritative outcome reporting** | Mnemosyne C12: transaction/service oversight, shard management, reporting/logging, compartment validation/reapplication and Themis-owned PEP hosting | Persistent-service capability, operation/outcome, applicable compartments; logging distinct from Kleio evidence | U11, AX-05/07/08/14/15. API success not automatically Harmonia outcome; no independent semantic compartment/sharding decision or infrastructure management. Detail CDG-47/40; Kleio mandatory capture uses governed intrinsic platform authority under AX-07; no equivalent authority for Mnemosyne. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mnemosyne](../../../enterprise-architecture/continuum/application/application-component/7985e647bf9e4ca3ba70faf9e3e4ae02.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F43 | **Search Management** | First-class Mnemosyne responsibility; Mneme Adhoc Search is context/activity-facing surface | Authoritative managed-persistence results; persistence-domain/shard/service/search-index capability metadata; Research Metadata may be used | Explicit U11 task decisions. Search not based on assumed complete transient cache; metadata may be managed without selecting physical index/query technology. Detail CDG-48/31. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mnemosyne](../../../enterprise-architecture/continuum/application/application-component/7985e647bf9e4ca3ba70faf9e3e4ae02.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F44 | **Research Metadata Management** | First-class Mnemosyne capture/persistence/management/search utilisation; AI derivation outside Mnemosyne through appropriate Harmonia behavioural/execution construct | AI-derived meaning/relationships across content sets based on proximity, distinguishable from unchanged authoritative source Content/metadata | Explicit U11 task decisions. Distinct from Search Management; source authority unchanged. Provenance/confidence/schema unresolved CDG-48; no AI framework/provider/vector/embedding/proximity/index choice or Dokimasia allocation. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope-DataSets](../../../enterprise-architecture/continuum/application/application-component/53bf6815c7a54ed0abde9b1aad311843.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F45 | Runtime governed DataSet management and authoritative ontology review for Harmonia use | [Calliope-DataSets C42](component-register.md#c42); Mneme/Mnemosyne access/persistence; existing Ponos/Ergo/Praxis workflow where required | ValueSets, CodeSets, Structured Metadata and applicable ontology content | U13. No persistence/workflow engine or originating authority over external ontology; lifecycle/UI/version/ontology boundary CDG-49. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Harmonia.Omilia.Server](../../../enterprise-architecture/continuum/application/application-component/369907025d06492bb414a72cea834a77.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F46 | Omilia collaboration operations and client experience | [Harmonia.Omilia.Server C47](component-register.md#c47) and [Harmonia.Omilia.Client C48](component-register.md#c48); distinct from Agora control | Server Room/Space, user, membership, message/event/media operations; Client collaboration experience over Matrix protocol | U16 adjudicated candidate. Server derived from Matrix Synapse; Client from Element. Lineage is provenance; API, modifications, storage and Client/Iris detail deferred CDG-52. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope](../../../enterprise-architecture/continuum/application/application-component/466ad8f75a954c0581b5bba090443849.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F46 | Shared Security and Assurance Nomenclature | Calliope-Models definitions; Calliope-DataSets controlled content; Themis decides/enforces, Dokimasia assures, Kleio audits | Includes U12 four release-bounded authorisation ValueSets; policy associations/grants remain Themis-owned | U12/U13. Complete release contents and detailed change/version rules deferred. Definition ownership is not operational ownership. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Pylai](../../../enterprise-architecture/continuum/application/application-component/e3e4147fea3640198dc251ed41ee854d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

| F47 | Pylai interaction progression and logical Praxis boundary handover | Pathway uses IngressHoros/EgressHoros and Ergo; Broker serves existing BusinessLogic; Server changes use Pathway, reads/searches use Diadromí-Controller and existing Mneme Adhoc services | Transient interaction, initial Context/Task input, mapped output, egress state/outcome before Unloader completion | U14. No fabricated READ/SEARCH work, recoverable Controller state or universal Controller. Exact schemas/representation/runtime handover CDG-50; U15 resolves Kleio mandatory capture authority separately from the audited Identity. |


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


The register deliberately preserves proposal/unallocated features alongside
better documented ones. A feature shared across components is a collaboration,
not proof of duplicated responsibility. Names such as “PatientIdentityUpdateErgon”
or “Tasks room” do not confer master-patient or clinical-work determination
authority; corresponding conflicting source meanings remain visible in CDG.
