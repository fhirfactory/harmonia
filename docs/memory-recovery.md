| # | Chat / Discussion I Remember | Very brief description |
|---:|---|---|
| 1 | **Harmonia — Original Concept / Purpose** | Why Harmonia exists; vendor-neutral healthcare interoperability/operational platform; architectural rather than product-first thinking. |
| 2 | **Architecture Documentation Restructure** | Markdown as canonical architecture; LaTeX/ODT as derived 1:1 publication forms; restructure documentation around architecture domains. |
| 3 | **Why We Are Doing Architecture Properly** | Architecture as engineering input, AI-development control framework, educational/demonstrative artefact; derivation itself is part of the deliverable. |
| 4 | **TOGAF / ArchiMate Rigour** | Use TOGAF separation/progression and ArchiMate semantics pragmatically rather than mechanically; architecture must drive implementation. |
| 5 | **AI as Architecture / Development Peer** | AI should reason from documented architecture, challenge it when warranted, but never silently substitute familiar patterns or inferred destination. |
| 6 | **Junie / Codex / Claude Roles** ⚠️ | Discussion/experimentation around which agent is best for architecture reconciliation vs implementation; I retain only part of the Claude discussion. |
| 7 | **Junie Task / Report Discipline** | Task Goal / Task Activity / Target Outcome format; bounded tasks; `.junie/reports`; reports as execution history, not architectural authority. |
| 8 | **Architecture Axioms** | Development of the architectural axioms as normative guardrails, including provenance, uncertainty, state, authority, etc. |
| 9 | **AX-17 — Architectural Authority & Explicit Uncertainty** | Documented architecture is authoritative; unknowns remain unknown; AI/humans must not fill gaps by convention or inference. |
| 10 | **Motivation Architecture** | Stakeholders → Drivers → Assessments → Goals → Outcomes → Principles → Requirements/Constraints; ensuring Motivation actually drives downstream design. |
| 11 | **Independent Assurance Requirement** | Discovery of missing Motivation requirement; REQ-FND-005; independence, evidence sufficiency and separation from operational management. |
| 12 | **Strategy / Capability Model Restructure** | Strategy as bridge from Motivation to downstream architecture; capability/resource/course-of-action model. |
| 13 | **Business Capability Model** | Large discussion/refactoring of healthcare capability domains, boundaries and semantic quality. |
| 14 | **Capability Model Quality Rules** | Coverage, Orthogonality, Semantic Clarity/"Idiot Test", No Junk Drawers, Global Capability Name Uniqueness. |
| 15 | **Capability Tiers / Capability Metamodel** ⚠️ | CT1/CT2/CT3 as nested capability sets; much of this is the conversation we're currently recovering. |
| 16 | **Capability Identifier Nomenclature** ⚠️ | `BL.CT1-x.CT2-y.CT3-z.FT-a`, namespace prefixes, compact/context references, aliases, retirement/non-reuse after moves. |
| 17 | **Capability Features / Services / Functions / Processes** ⚠️ | FT/SN/FN/PS beneath capability sets, including Strategy-level articulation where appropriate. |
| 18 | **Cross-Cutting Capabilities Rejected** ⚠️ | No special cross-cutting capability concept; apparent cross-cutting behaviour means poor boundaries or consumption of another capability's features/functions/services. |
| 19 | **Capability Ownership vs Consumption** ⚠️ | Capability A can consume capability B's Feature/Function/Service without changing its ownership, membership or canonical identity. |
| 20 | **Information / Data Object Ownership** ⚠️ | Information Objects defined/owned in capability context; others may consume them; downstream Data Objects realise/represent information rather than redefine it. |
| 21 | **Business Enabling Capabilities** | Five natural/contextual views: Entity Management, Service Administration, Service Delivery, Health Service Operations, Intrinsic/Shared Enablement; views are not capability tiers. |
| 22 | **Enterprise Capabilities** | EC-01…EC-13 reusable enterprise capabilities; reusable capability does not imply centralised service. |
| 23 | **Governance vs Management** | Governance establishes what must be true/constraints/checkpoints; Management determines and progresses what must operationally happen. |
| 24 | **Management Monitoring vs Assurance** | Monitoring observes state to progress work; Assurance evaluates against criteria to establish a conclusion; cadence doesn't distinguish them. |
| 25 | **Governance & Assurance Region 5** | Recognition of missing operational perspective; addition of Health Service Governance and Health Service Assurance without raiding existing capabilities. |
| 26 | **Health Service Direction & Stewardship** | Extraction of governance concerns from old Capability 14 while retaining strategic direction/executive stewardship. |
| 27 | **Clinical Quality, Safety & Improvement Boundary** | Operational clinical quality/safety remains capability 06; independent assurance extracted; assurance is not clinical-care responsibility. |
| 28 | **Risk and Assurance** | Rejected creating a Risk capability absent requirement; risk can inform Governance, Management and Assurance; risk register permissible, not mandated. |
| 29 | **Assurance Design** | Capability to decide how a requirement/constraint/behaviour will be assured; assurance disposition as design obligation. |
| 30 | **Assurance Criteria Management** | Governed, reusable, temporally identifiable criteria; criteria operationalise but cannot silently alter governing requirements. |
| 31 | **Governed Assurance** | Independently evaluate a governed subject using criteria and trustworthy evidence to establish findings/conclusions. |
| 32 | **Assurance Evidence** | “Being evidence” is contextual, not intrinsic; underlying information remains owned by source capability; Assurance Evidence Assembly associates it with assurance purpose. |
| 33 | **Assessment vs Adjudication** | Assessment asks what evidence demonstrates against criteria; adjudication establishes what assurance conclusion follows. |
| 34 | **Assurance Disposition** | Generic assurance sufficient / existing specific / extend / new specific / not assurable / not worth specifically assuring. |
| 35 | **Generic Processing Assurance** | Business-level watchdog over workflow progression, completion, timeliness, pathway, failure/recovery/outcome/patterns—not infrastructure monitoring. |
| 36 | **Operational Health vs Processing Assurance** | Kubernetes/DB/CPU/queues being healthy does not mean business workflows are behaving correctly. |
| 37 | **Service Guardian / EC-14** | New Enterprise Capability supplying assurance-specific semantics; originally explored as “Assurance Policing”; ultimately provisionally named Service Guardian. |
| 38 | **Dokimasia** | Independent assurance framework coordinating governed assurance activity; QA service rather than Job Foreman; deliberately downstream of Strategy derivation. |
| 39 | **Dokimasia vs Ponos / Digital Twin** | Assurance activity independent from assured activity; exact execution/component relationship deliberately unresolved. |
| 40 | **Business Architecture Method** | WHO / WHAT BUSINESS BEHAVIOUR / WHAT VALUE; actors, roles, collaborations, interactions, processes/functions/services; no premature Application Architecture. |
| 41 | **Business Architecture Actor-Centric Workflows** | Workflows are actor-centric—the actor does something; workflow doesn't “act on” the actor. |
| 42 | **Information Architecture Method** | Meaning/information concepts established before application representation; don't jump from FHIR resources to business/information concepts. |
| 43 | **FHIR Is Not the Architecture** | FHIR is an interoperability representation/standard, not the source of Harmonia's business or information model. |
| 44 | **Digital Twin Concept** | Twin is an active management construct associated with a real-world entity coordinating information and operational activity. |
| 45 | **Twin ≠ FHIR Resource** | Practitioner Twin may manage Practitioner/PractitionerRole/Endpoint etc.; Twin isn't any one of those resources. |
| 46 | **Twin as Construct vs Component** | Digital Twin passes as architectural construct but not as independent platform component; Ponos executes, Twin coordinates. |
| 47 | **Practitioner / Bed / Patient Twins** | Operational twins in hospital context; Patient may touch clinical information but EMR is not the Patient Twin. |
| 48 | **Ponos Execution Model** | Per-Twin queues, few threads servicing many Twins, only Twins with pending Effectors active, Kubernetes concurrency/scaling. |
| 49 | **Ergo / Ergon / Praxis / Behaviour** | Praxis as sequence of Ergo; Behaviour as Twin Praxis; Ergon discrete business transaction; Effectors/Stimuli as Task specialisations. |
| 50 | **Ergon Processing Structure** | Receiver → Context Loader → checkpoint → business logic → checkpoint → unloader → task builder → publisher. |
| 51 | **Ergon Constraints** | Never resume inside Ergon; no assumed idempotency; business logic only in Ergo BL; checkpoints ingress/egress; audit at checkpoints. |
| 52 | **Task / Routing Model** | Task input → outputs → downstream tasks; topics, subscription/pathway routing; human Work Order/To Do vs synthetic Task. |
| 53 | **Failure / Retry / Recovery** | Petasos handles retry; avoid compensation/event sourcing by default; failure/recovery is expected behavioural space. |
| 54 | **Concurrency / Sharding / Eventual Consistency** | Exploration of sharding Mneme/databases and eventual consistency to outperform Rhapsody/Mirth-like DB-heavy integration engines. |
| 55 | **Ponos Release 1 Scope** | Ponos::Twin plus Non-Twin Ergo; pragmatic initial execution boundary. |
| 56 | **Mneme / Mnemosyne Separation** | Mneme manages application-facing/active distributed use; Mnemosyne establishes authoritative durable state/truth. |
| 57 | **AX-05 Active vs Durable State** | Formalised “Ponos progresses activity; Mneme manages active use; Mnemosyne establishes durable truth.” |
| 58 | **Mneme API / Search Architecture** | Client → Mneme → Mnemosyne → persistence engine; search semantics, cache boundaries and durable search. |
| 59 | **Search Result Semantics** | Whether results become Harmonia-managed resources or remain atomic result sets; intentionally explored rather than casually assumed. |
| 60 | **Harmonia Is Not Big Data** | Bounded/indexed results, multi-parameter search, security overlays and reasoning to constrain search scope. |
| 61 | **Mneme Cache vs Authoritative Search** | Identified cache-only search defect after restart; discussion of delegating search to Mnemosyne vs authoritative-backed cache etc. |
| 62 | **Pylai / Mneme Standards API Boundary** | Pylai as standards-based FHIR server/interoperability service; Mneme as Harmonia API/model layer; separate Harmonia-originated APIs from FHIR exposure. |
| 63 | **Calliope** | Governance of canonical models, profiles, terminology bindings, semantics; resource conflict/design responsibility. |
| 64 | **Clinical Information Architecture** | Vendor-neutral longitudinal clinical representation; source systems retain authority for originating facts while Harmonia governs interoperability representation. |
| 65 | **Iris Clinical UI** | Read-oriented longitudinal record viewer; patient tabs/navigation; possible later lightweight authoring. |
| 66 | **Clinical FHIR Resource Set** | Patient, Flag, AllergyIntolerance, EpisodeOfCare, Encounter, Condition, Medication-related resources, Observation, diagnostics, referrals etc. |
| 67 | **Person vs Patient / Practitioner** | Person as identity concept, Patient as clinical subject; Practitioner vs PractitionerRole; provider/entity modelling boundaries. |
| 68 | **Provider Directory / Provider Management** | FHIR-based provider capability, use cases, external/provider-store integrations, Epic not assumed as default answer. |
| 69 | **Clinical Terminology Architecture** | ADHA → IMO/EMR and Ontoserver syndication/dump model; local terminology/value-set governance and approval/testing. |
| 70 | **Information Management Committee** | Governance body spanning clinical informatics, HIMS, release of information, patient safety, reporting/analytics, etc. |
| 71 | **FHIR IG / FSH / Publisher** | FSH/SUSHI/FHIR Publisher/package dependencies/canonical URLs/validation/release; StructureDefinition-first preference discussion. |
| 72 | **FHIRPath** | Where FHIRPath is used, invariants, examples and validation; distinction from Java Fluent APIs. |
| 73 | **Harmonia Development Environment** | Threadripper/Linux server, Docker/Kubernetes/MicroK8s, remote development from laptops, registry/execution topology. |
| 74 | **Architecture → Software Solution Derivation** | Recent explicit discussion about how Business Architecture/Strategy constrains software solution while still allowing multiple implementation choices. |
| 75 | **Guardrails vs Prescription** | Architecture should establish invariants/responsibilities/boundaries without prematurely dictating mechanisms. |
| 76 | **Deferred Document Register** | Explicit backlog for architectural/documentation refinements intentionally deferred to later passes; the mechanism we're now fixing our use of. |
| 77 | **Domain-by-Domain Reconciliation / Freeze** | Incremental passes, human approval, freeze/refreeze, preservation checks, no silent modification of upstream architecture. |
| 78 | **Architecture Validation / Tests** | Automated architecture/link tests, preservation checks, byte-identical unaffected files, quality gates around documentation changes. |
| 79 | **Navigable Architecture / Pictographs** | L1→L2→L3 and capability→service/function maps; visual inspection to expose overlap/gaps and make architecture understandable. |
| 80 | **Documentation as Learning / Reconstruction** | Documentation intended not only for delivery but so a human can return later and reconstruct why Harmonia looks the way it does. |