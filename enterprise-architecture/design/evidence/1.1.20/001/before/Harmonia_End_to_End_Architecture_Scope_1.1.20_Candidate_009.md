# Harmonia — End-to-End Architecture Scope
## Candidate 009 — Continuum, DRP, Journey and Granular Service Scoping Brief

**Date:** 4 November 2026  
**Status:** Working design proposal; neither architectural acceptance nor repository incorporation  
**Authoritative repository snapshot:** `fhirfactory/harmonia`, branch `Capability-Map-Consolidation`, HEAD `fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374` (observed 4 November 2026)  
**Target reference:** Business and Enterprise Capability Candidate 006 (Business: 15 CT1, 86 CT2, 258 CT3, 1,299 Features; Enterprise: 9 CT1, 47 CT2, 139 CT3, 700 Features), plus the Stakeholder/Journey catalogue in Candidate 003 (13 primary perspectives, 29 candidate Journeys, 11 reusable Journeys).  
**Purpose:** Define the *whole architectural migration scope*: preserved Continuum knowledge, agreed DRP thinking, Stakeholders and Journeys, Business and Enterprise Capability hierarchies, Features, Services, Functions, Processes, Solution Facets, and the traceability required to verify architectural and eventual solution integrity. This is **not** a completed Service Catalogue, an implementation plan, a Codex task, or permission to modify the repository.

## Executive proposition

**The deliverable is an end-to-end architectural scope, not a catalogue of broad service families.** It must preserve and reconcile the meaning of the established Continuum and agreed DRPs; express stakeholder value through reusable Journeys; account for Business and Enterprise Capability obligations; and identify Services at the granularity of a coherent, independently useful service contract. It must enable integrity checks from value-stream stage through capability, service offering and governed architectural home. It must not pre-empt implementation.

Six complementary results are required:

1. **Architectural source preservation and reconciliation** — every relevant established Continuum element and DRP decision is examined, preserved or explicitly reconciled; no silent supersession.
2. **Stakeholder/Journey scope** — the complete candidate stakeholder and Journey populations, shared Value Stream stages, specialisations, expected values, and entry points for integrity/coverage testing.
3. **Capability scope** — complete Candidate 006 Business and Enterprise CT1 → CT2 → CT3 → Feature proposals, retaining their candidate standing until reviewed.
4. **Granular Services** — independently intelligible consumable behaviours/contracts, potentially as fine-grained as a single API-style operation when its boundary is architecturally meaningful; *not* arbitrary multi-operation bundles.
5. **Architectural behaviours and homes** — Functions and Processes (including orchestration) and Services each have one clear architectural home within a Capability/Feature or Solution Facet. Shared use does not imply duplicated home.
6. **Traceability and integrity tests** — source-backed architectural coverage, explicit gaps, and verifiable future solution-conformance scenarios. Gap discovery is a successful use of Journeys, not a Journey defect.

Do not assume a fixed correspondence among Journey, capability, Feature, Service, Function, Process, API, or Solution Facet. One-to-one correspondences are permitted when justified; none is obligatory.

### Specific service-granularity rule

**A Service expresses one coherent, independently useful promise to a recognisable consumer. It may be granular.** A single operation can qualify when that operation is independently meaningful, bounded and governed. A larger service can also qualify where its outcome is genuinely cohesive; size is not the deciding test.

A name like `GetThingAndThingAndProcessIt` is a warning: it conflates distinguishable responsibilities and hides what the consumer can actually request. Disentangle its contract into separate Services **where those contracts independently make sense**, and house any genuine coordinated progression in the appropriate **Process**. Functions describe internal behaviour, not an excuse to hide independently consumable offerings.

| Bad default | Better candidate scope (illustrative, not authoritative names or new decisions) |
|---|---|
| `RetrieveAndValidateAndSaveInformation` | Governed Information Retrieval; Information Validation; Governed Information Submission. A Process coordinates them only when needed. |
| `FindAndReadAllRelevantVersions` | Information Search; Version Listing; Version-Specific Retrieval. These may be separate Services where independently consumable. |
| `RouteAndDeliverAndCompleteWork` | Work Routing; Durable Work Delivery; Work Completion Recording, preserving Artemis/Petasos/Ergon/Praxis distinctions. |
| `AuthoriseAndExportAuditEvidence` | Local Authorisation Evaluation (where exposed as a bounded contract); Audit Evidence Selection; Audit Evidence Export. Their interaction may require a Process; avoid centralising local PEP authority. |

**Test candidate Services individually:** Who consumes the Service? What single outcome does it promise? Can a consumer distinguish success, absence, rejection, failure or indeterminate outcome where relevant? Is the promise independently testable? What does the Service explicitly *not* do? Where is its one architectural home? Is there a source basis in the Continuum, DRPs, capabilities or Journey obligations?

Service identity is **not** inferred from endpoint count, method signature, CRUD operation, Feature count, or a Solution Facet name. One Service may expose several related API operations; several Services may be exposed through one interface. Granularity follows the consumer contract and meaningful responsibility boundary.

### Consumer-intent reference model — Retrieve → Check → Update → ProcessSomething → Commit

**Agreed working architectural intent (4 November 2026):** The Solution Architect confirmed the **five-stage service-intent reference model** and explicitly agreed that **successful execution is not equivalent to authoritative commitment**. This is a design rule for the Candidate 009 scope; it is **not** a claim that the current governed Continuum or any particular DRP already contains this five-stage terminology.

> A consumer may want to **Retrieve** information for review, **Check** its suitability or consistency, **Update** the intended working representation, **ProcessSomething** using that information, and finally **Commit** an accepted outcome with governed surety.

These are **five distinguishable consumer intentions**, rather than a mandatory five-step workflow, a prescribed API design, or five services that every solution must create. A Journey may skip, repeat, branch between or reorganise these intentions; a service may legitimately offer a coherent atomic result when its contract honestly expresses that result. The same conceptual discipline can inform Business Services and API-facing Application Services **without conflating their ArchiMate types or forcing a one-to-one mapping**.

| Consumer intention | Illustrative Service promise | What it must not be confused with | Example independent assessment |
|---|---|---|---|
| **Retrieve** | Make authorised information available for review, with the required identity, version, scope, source and currency context. | Authoritative review, validation, modification or acceptance; absence, denied access and stale information remain distinguishable outcomes. | Can the consumer obtain the requested eligible version and recognise its provenance and currency? |
| **Check** | Evaluate information or a proposed action against applicable consistency, validity or suitability criteria and return interpretable findings. | Automatically applying a change, asserting universal clinical correctness, or silently publishing an updated authoritative state. | Are findings attributed to the appropriate criteria/model version, including rejected or incomplete checks? |
| **Update** | Establish, revise or maintain the intended changes in an appropriate working context, preserving their identity, origin and correlation to accepted information. | Completing the business operation or **accepting the resulting state authoritatively**. A working revision might itself be retained durably, but durability of a draft is not authoritative commitment of the intended outcome. | Can the consumer inspect, revise, discard and distinguish the proposed state from the previously accepted state? |
| **ProcessSomething** | Perform a **specific, consumer-meaningful operation** using relevant working information, yielding its own execution outcome, findings, artefacts or effects. | **`ArchiMate::BusinessProcess` or `ArchiMate::ApplicationProcess`**, an all-purpose `ProcessSomethingService`, or a guarantee that the result has been committed. | Can the operation be invoked and its outcome or failure assessed independently of authoritative acceptance of that outcome? |
| **Commit** | Request and obtain an authoritative, governed acceptance disposition for the intended result, with the applicable durability, version, identity, security, audit and provenance obligations honoured. | Execution success, a completed internal workflow, a cache update, a transport acknowledgement, a provider-native write acknowledgement, or a low-level database transaction primitive. | Can acceptance, rejection, uncertainty and retrievable authoritative state be distinguished and correlated to the intended outcome? |

**Terminology rule — `ProcessSomething`:** `ProcessSomething` is a **deliberate placeholder for a particular consumer intention**. It prevents confusion with an ArchiMate **Process** element. It is neither the literal name of a Service nor a new architectural element type. In a specific Service contract, replace `Something` with the actual meaningful operation. Separately, an `ArchiMate::BusinessProcess` or `ArchiMate::ApplicationProcess` can coordinate one or more Services and other behaviours; the two concepts are not interchangeable.

**Critical semantic distinction — `ProcessSomething` success ≠ `Commit` success:** A correctly completed operation may produce a valid proposed outcome or evidence of execution without that result having been authoritatively accepted. A successful **Update** does not imply `ProcessSomething` ran; a successful **ProcessSomething** does not imply **Commit** succeeded; and a positive transport or persistence-provider acknowledgement does not automatically establish governed authoritative acceptance. The scope must preserve and separately test these outcomes wherever applicable.

**Failure and assurance boundary:** For relevant Journeys, independently represent (a) a prepared/updated working state, (b) a successfully or unsuccessfully executed operation, (c) a commitment request and its accepted/rejected/indeterminate disposition, and (d) a later independent check of authoritative state when the reported disposition is uncertain. Recovery or retry after failure between execution and commitment must not silently assert that committed state exists, repeat externally significant work without appropriate safeguards, or erase legitimate audit/provenance evidence. Exact atomicity, retry and side-effect policies depend on the specific contract and governed decisions; this reference model **does not prescribe an implementation transaction model**.

Where separate promises exist, **model separately meaningful Services**, with one architectural home per Service. An appropriately modelled Process may coordinate the progression, but a compound `RetrieveCheckUpdateProcessAndCommitService` must not be created merely to mirror the sequence. Conversely, do not force the creation of five Services when the relevant consumer intentions are absent or when the contract genuinely promises one cohesive outcome.

This reference model supplies a **cold service-granularity and integrity-test lens** for information correction, care coordination, work execution, administrative decisions, authoritative model publication and other applicable Journeys. A Journey may reference multiple Services spanning Solution Facets, while each Service, Function and Process retains a single governed architectural home.

### Sources, architectural authority and incorporation standing

- **Governed Continuum:** treated with the **same reverence** as the DRPs. It contains substantive, previously governed architectural decisions, definitions, identity, provenance, Services, Functions, Processes, Solution Facets and applicable relationships. Its representation is not a low-priority legacy inventory.
- **DRPs:** equally serious sources of agreed architectural thinking, irrespective of their formal Draft label, which reflects deferred incorporation while the Continuum stabilised. Explicitly unresolved alternatives *remain unresolved*; where DRPs disagree with the Continuum, reconcile the design evolution and preserve both sources' evidence rather than silently preferring either.
- **Candidate 003 and Candidate 006:** proposed target Stakeholder/Journey and Capability/Feature structures, respectively. They carry agreed modelling direction but are **not** yet incorporated into the governed Continuum; candidate status must be preserved.
- **Solution Facets:** established architectural responsibility boundaries and candidate architectural homes; they challenge completeness but do not dictate the capability hierarchy or create automatic Service identities.
- **NFRs and Motivation:** constrain the promises and qualities of Services and capabilities. A Feature or NFR may require one or more Services, Functions or Processes; it is not automatically itself a Service.
- **Accepted 1.1.19:** immutable published baseline. Current-branch working architecture and DRPs may contain later accepted thinking or deferred incorporation. Keep source version, decision authority, architectural standing and incorporation state separate.

**Reconciliation is symmetrical:** if a Continuum Service and a DRP disagree, capture both meanings, their provenance, and the interpretation needed to resolve the difference. Never delete a governed definition or DRP obligation because the new catalogue uses more accessible terminology.

The initial inventory identifies **5 native Application Services, 142 Business Services, 160 Business Functions, 21 Business Processes** and **664 Capability documents** in the inspected branch directories. These are source-file counts, not approved target Service counts. Existing Business Service names/placements may preserve historical BEC context and require identity-aware reconciliation.

## Stakeholders, Journeys and end-to-end scope

### Stakeholders are in scope as governed architectural perspectives

Retain the **13 primary stakeholder perspectives** from Candidate 003: Patient; Practitioner (including specialists); Allied Health; Nurse & Support; Diagnostics People; Healthcare Operations; Healthcare Administrators; Executive; Regulatory/Government; Researchers; Systems Operations; Systems Administrators; and Participant. **Patient and Participant remain distinct**. No universal Person, persona, role or human/AI identity hierarchy is to be introduced.

Stakeholder specialisations are included when their **value sought or contributed** and Journey commonality justify them; they do not require a full taxonomy of professions or employment arrangements. `ResponsiblePerson` remains an explicit probe, not a silently approved fourteenth stakeholder.

### Journeys are in scope as architectural content, not discovery notes

- **Journey is a reusable specialisation of Value Stream**, with its individual chevrons modelled as value-producing stages. No invented JourneyKernel construct.
- Preserve the **29 candidate Journeys (J01–J29)** and **11 reusable Journeys (RJ01–RJ11)** from Candidate 003 as the working scope, keeping candidate status, descriptions, shared/specialised stages and cross-stakeholder participation.
- Journeys may branch, recur, overlap and interoperate. Their chevrons are not automatically Business Processes or software workflow steps.
- A Journey may expose missing capability, missing Service, missing Solution Facet contribution, missing association or actual solution/organisation coverage. **Do not falsify the Journey to make the architecture appear complete.**
- Preserve expected outcomes and value qualities (timeliness, safety, accessibility, continuity, accountability, etc.) without converting each quality into another Capability or Service.

### Journey-to-architecture integrity path

The architecture must be interrogable in both directions:

```text
Stakeholder perspective
    → Journey (specialised Value Stream)
        → value increment / Journey stage
            → required Business Capability / Feature
            → required Enterprise Capability / Feature
                ↔ granular Services and their coherent contracts
                   (each Service has a single architectural home)
                ↔ Functions / Processes with single architectural homes
                ↔ applicable Solution Facet contexts
            → governed DRP / Continuum / Motivation obligations
            → explicit coverage and exception evidence
```

Arrows here mean **scope and traceability questions**, not proposed ArchiMate Realisation or Composition relationships. Actual governed relationships will be chosen in a separate architectural-authoring step. This exercise is limited to meaning, home and integrity coverage.

### Integrity testing — three different assertions

| Test class | What we must be able to establish | What the test must not falsely claim |
|---|---|---|
| **Model integrity** | Each candidate stakeholder, Journey, stage, capability, Feature and behaviour has stable meaning; required homes and references are coherent; no unexplained orphan or semantic collision. | That the implementation exists or is satisfactory. |
| **Architectural scope/coverage** | Each Journey stage's required abilities and relevant DRP/Continuum obligations are represented or recorded as an explicit gap, including service contracts and boundaries. | That every capability or Feature is fully provided by Harmonia. |
| **Solution integrity / conformance** | When software is built, a test can exercise the scoped Journey scenario, inspect the relevant Service promises, exceptions and evidence, and compare results with capability/Feature expectations. | That passing structural validation alone proves the software supports real-world healthcare outcomes. |

**Test fixtures should be generated from representative Journey paths and variants**, including different stakeholders, explicit exceptions, handoffs, failure and recovery, temporal conditions, authorisation and incomplete information. The validation architecture must allow an observed shortfall to be attributed to a missing or defective capability, Service, Function, Process, Solution Facet responsibility, or implementation—not automatically to the Journey design.

**Five-intent integrity assertion:** Where the reference model applies, test `Retrieve`, `Check`, `Update`, `ProcessSomething` and `Commit` as **separately observable consumer expectations**. Distinguish a failed check, a discarded update, an operation that succeeds but whose result is not committed, a failed operation, an explicitly rejected commitment, and an indeterminate commitment acknowledgement. Verify that successful `ProcessSomething` execution is never used as the sole evidence that authoritative commitment occurred. Include a recovery/consistency scenario at the execution-to-commit boundary; do not presume that recovering an Ergon resumes it midway.

## Scope domains — candidate service offerings

**Notation:** `S` = candidate service-contract or service-family *discovery area*, which should be further split into meaningful granular Services where independent consumer outcomes exist; `Q` = quality/assurance expectation, not inherently a Service; `I` = internal responsibility/Function/Process; `?` = boundary still to verify. Labels here are **scope prompts, not final individual Service identities**. The source inventory is expected to expose much finer-grained Services than these headings.

### E01 — Governed Information Custody

**Candidate offerings (S):** Durable Information Acceptance; Authoritative Information Retrieval; Historic Version Access; Information Integrity Verification; Retention & Archival Access; Governed Recovery/Restoration; Information Reference Resolution.  
**Candidate responsibilities/qualities (I/Q):** authoritative outcome correlation; identity/version custody; integrity anomaly reporting; retention enforcement; recovery with state preservation; distributed custody; persistence-service adaptation.  
**Boundary conditions:** Mneme is the governed access boundary; Mnemosyne comprises GovernedPersistenceFramework plus PersistenceService instances. An acknowledged cache write is not authoritative persistence. PersistenceService implementation and instance are different choices. Provider-native IDs and representations do not become canonical identifiers. Media/content larger than the governed threshold belongs behind the separate media boundary, not in conventional large binary database storage. Sharding must not alter semantic identity or security.

### E02 — Information Access, Discovery & Distribution

**Candidate offerings (S):** Governed Information Read; Governed Information Change; Governed Search; Search-Result-Set Access; Information Reference Dereference; Information Distribution/Subscription; Information Availability Access.  
**I/Q:** authorised filtering, consistent shared access, fast reads, concurrency handling, search pagination/continuation, result-set provenance, distribution recovery, controlled access under failures.  
**Boundary conditions:** the Mneme–Mnemosyne interaction distinguishes Reader, Writer and Search; Search is **not** an implicit CRUD operation. Search returns governed Information Unit Version references and result sets are governed in Ergon context. Specific service/contract correspondence must be verified. DistributedAccess Features do not require one new Service per quality.

### E03 — Digital Interoperability & Service Interaction

**Candidate offerings (S):** External Information Exchange; Inbound Message Acceptance; Outbound Exchange Delivery; Contractual Service Exposure; Protocol/Format Mediation; Exchange Conformance Feedback; Exchange Fault Reporting.  
**I/Q:** MLLP protocol acknowledgements, timeout/retry, exact external contract adherence and explicit deviation reporting; interaction correlation; trusted mediation across boundaries.  
**Boundary conditions:** Pylai is the external exposure/interoperability concern; no separate Pyrai Facet is assumed. Internal Mneme is not externally exposed. An external acknowledgement and a durable acceptance acknowledgement may have different semantics. Do not flatten transport, exchange and clinical business meaning into one service.

### E04 — Semantic Meaning & Model Governance

**Candidate offerings (S):** Information Model Resolution; Terminology/Value Set Resolution; Semantic Transformation; Semantic Validation; Governed Model Publication; Model Compatibility Information.  
**I/Q:** model lifecycle, release acceptance, mapping version control, preservation of native information semantics, stable terminology release, semantic conformance evidence.  
**Boundary conditions:** Calliope's models and contracts are distinct from operational services using them. A model definition is not inherently a runtime Service. Dynamic terminology/value-set changes without release governance are out of agreed design intent. Distinguish translation from validation and publication.

### E05 — Identity, Trust & Information Protection

**Candidate offerings (S):** Digital Authentication; Policy Definition Management; Authorisation Determination; Policy Enforcement Access; Effective Passport Access; Passport Currency/Revocation; Security Administration & Review.  
**I/Q:** local Policy Enforcement Points, subject RequiredCriteria assessment, compartment handling, fail-closed behaviour, authoritative policy/passport construction, operational distribution, invalidation/revalidation, key/certificate lifecycle, security evidence.  
**Boundary conditions:** Themis's policy authority is distinct from Calliope's models, Mneme's operational custody and each protected component's local PEP. Propagated Passport is security context, **not credentials**. An Identity's authorisation and permission to execute an Ergon are not unrestricted information authority. Whether policy administration is a separate exposed service must be grounded in actual consumer needs.

### E06 — Digital Work, Events & Temporal Coordination

**Candidate offerings (S):** Work Registration & Status; Governed Work-Context Establishment; Adhoc Work Information Access; Work-Context Return; Workflow Progression; Work Assignment & Completion Tracking; Parcel Routing; Durable Work Acceptance & Delivery; Work Recovery/Reconstruction; Time-based Scheduling.  
**I/Q:** Pragma/Pragmaton distinction, checkpoint correlation, per-Ergon bounded work, Praxis workflow semantics, Artemis exclusive PetasosParcel routing, Petasos reliable receipt/delivery, centrally governed scheduler/calendar, response modes and completion states.  
**Boundary conditions:** transport acceptance != authoritative IU persistence; workflow progression != individual Ergon work; routing != delivery durability; active working state != authoritative durable information. Recovery **does not resume inside an Ergon**. Candidate Service offerings must respect distinct Facet homes and avoid inventing a generic ‘TaskService’ that erases these responsibilities.

### E07 — Information Presentation & Human Interaction

**Candidate offerings (S):** Contextual Information Presentation; Longitudinal Information Viewing; Governed Interaction Submission; User Work-Context Access; Presentation Attribution & Currency; UI Scope Definition/Access; Consumer Experience Status/Feedback.  
**I/Q:** navigational continuity, accessible presentation, history comparison, freshness/source display, authorised action, contextual information minimisation, UI Scoping and consumer context.  
**Boundary conditions:** E07 is a potentially large but legitimate domain; its CT3/UI-scoping depth may be large even when Harmonia itself exposes a modest set of interfaces. Do not equate a screen, widget, app route or UI Scoping with a Service automatically. Distinguish user-recognisable offerings from rendering mechanisms. No new CT1 split is required merely by volume.

### E08 — Operational Observability, Assurance & Resilience

**Candidate offerings (S):** Operational Health & Status; Operational Telemetry Access; Log Intake/Collection; Log Retrieval/Export; AuditEvent Contribution & Access; Audit Evidence Export; Provenance Evidence Export; Conformance Evidence Reporting; Assurance Assessment/Reporting; Fault/Degradation Status; Service Recovery Control.  
**I/Q:** Log-Satellite receiver/buffer/forwarder; Log-Server receipt; backpressure; rsyslog-based logging foundation; security and PHI constraints; audit near source; immutable AuditEvent/Provenance; evidence ordering; non-dereferenced and dereferenced exports; controlled disclosure; resilience and OA&M evidence.  
**Boundary conditions:** LogEvent, AuditEvent, Provenance and assurance are **separate architectural responsibilities**. Log-Persistence is outside Mnemosyne, even if storage technology is reused. Kleio manages AuditEvents; Dokimasia is the Assurance Framework and carries the current provenance/evidence export responsibilities where defined. Successful Log-Server receipt does not prove durable logging persistence. Dereferenced exports must enforce authorisation and all-or-nothing handling where established.

### E09 — Information Insight & Evidence Use

**Candidate offerings (S):** Governed Information Set Access; Result Set Organisation; Aggregation & Derived Information Access; Governed Reporting; Analytical Evidence Access; Insight Publication/Export.  
**I/Q:** result-set membership and version references, analytic provenance, aggregation/derivation records, report interpretation, disclosure control and evidence availability.  
**Boundary conditions:** avoid turning analytical insight into unrestricted access to clinical content. Search-result-set metadata is not itself the searched Information Unit content. Operational observability/reporting (E08) and business/analytical insight (E09) may share information and service concerns while retaining coherent homes. Do not presume an advanced AI analytics capability or decision authority for R1/R2.

## Critical cross-domain service obligations

These matter more than a tidy CT1 subdivision and must be traced across all nine domains:

| Obligation | Why it must survive scope definition | Example source |
|---|---|---|
| Durable acknowledgement and outcome uncertainty | Prevent false promises of persistence or successful work | Mnemosyne persistence DRPs |
| Separate operation execution and authoritative commitment | An independently successful `ProcessSomething` can produce an outcome without a corresponding governed Commit; testing must distinguish them | Agreed five-intent model (4 November 2026); cross-check against Mneme/Mnemosyne and work DRPs |
| Authentication, Passport currency and local enforcement | Policy correctness cannot be delegated to a generic shared security server | Themis DRP |
| Immutable evidence and separate audit/provenance/logging | An operational log is not an AuditEvent or evidence of provenance | Logging and audit DRPs |
| Explicit recovery semantics | Recovery cannot resurrect partial Ergon execution or silently lose accepted work | Pragmaton, Petasos and resilience DRPs |
| Information semantics independent of storage | InformationUnit definition must not collapse into persistence implementation | Information Unit Framework DRP |
| Standards-native boundary conformance | External consumers experience supported contracts, not Harmonia's internal schema | Interoperability axioms |
| Governed result-set and analytical access | Discovery, result metadata, aggregation and actual content access differ | Mneme/Mnemosyne interaction and search decisions |
| Multiple Solution Facet contributions | One service value may depend on several responsibilities without duplicating their architectural homes | Architecture-wide home rule |
| Explicit service quality | Throughput, latency, availability, concurrency and security are often qualities rather than new Service entities | Candidate 006 Features and NFRs |
| Operational administration and observability | A solution that can deliver clinical value but cannot be operated, reviewed and recovered is incomplete | E08 and NFR/OA&M |

## The cold-question test — one part of Journey-based integrity verification

The target architecture should permit an analyst to ask a question in ordinary language, locate the relevant stakeholder Journey and value-stage scenario, and identify (a) the Business/Enterprise abilities needed; (b) the **granular** Services and their distinct consumer promises; (c) the CT3s/Features they address; (d) the single architectural home of each Service/Function/Process; (e) applicable Continuum and DRP obligations; and (f) explicit missing coverage, without inventing architecture.

Illustrative tests:

1. *Can a consumer retrieve the exact previously accepted clinical information version if a cache node has failed?* (E01, E02, E05, E08)
2. *Can an external source send a non-conformant HL7 v2 message and receive the contractually correct acknowledgement or error?* (E03, E04, E08)
3. *Can a previously valid Passport be invalidated fast enough that it no longer authorises a distributed operation?* (E05, E02, E08)
4. *Can a unit of work be recovered after a crashed node without resuming in the middle of an Ergon or losing its governed context?* (E06, E01, E02)
5. *Can an appropriately authorised operator export AuditEvents and their evidence while the system rejects any unauthorised referenced information?* (E08, E01, E02, E05)
6. *Can an authorised clinical reader understand the source, freshness and longitudinal context of displayed information?* (E07, E01, E02, E05)
7. *Can a participant's information be discovered for legitimate care coordination without disclosing unrelated compartment-protected content?* (E02, E05, E09)
8. *Can a clinician Retrieve a known version, Check consistency, Update a proposed correction, ProcessSomething clinically relevant with that proposal, and Commit an accepted result—without mistaking any earlier successful stage for authoritative acceptance?* (E01, E02, E04, E05, E06, E07)
9. *If ProcessSomething completes successfully but Commit is rejected, fails, or returns an indeterminate acknowledgement, can the stakeholder and the architecture distinguish the execution outcome from the authoritative state, prevent false-success reporting, and recover or reconcile safely?* (E01, E02, E05, E06, E08)

Passing these tests does not imply the solution has been implemented or verified. It establishes **architectural scope coverage and traceability**.

## Initial source register (reviewed in this scoping pass)

The current pass directly inspected the relevant Continuum top-level directories and example existing Services/Functions, plus the following DRPs on the indicated branch:

- `DRP-Information-Unit-Framework.draft.003.md`
- `DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md`
- `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md`
- `DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md`
- `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md`
- `DRP-Interaction-Petasos-to-Mneme.draft.002.md`
- `DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md`
- `DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md`
- `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md`
- `DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md`
- `DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md`
- `DRP-Mneme-Pragma-Model.draft.002.md`
- `DRP-Mneme-Pragmaton-Model.draft.002.md`
- `DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md`
- `DRP-Mneme-Security.draft.002.md`
- `DRP-Mneme-Audit.draft.002.md`
- `DRP-Themis-Policy-Passport-Enforcement.draft.001.md`

Source material: [Continuum](https://github.com/fhirfactory/harmonia/tree/Capability-Map-Consolidation/enterprise-architecture/continuum) · [DRP collection](https://github.com/fhirfactory/harmonia/tree/Capability-Map-Consolidation/enterprise-architecture/design/offline-design).

**Completeness qualification:** This revises the **scope and test contract** for subsequent detailed service-catalogue derivation; it is **not** a complete semantic audit of all Continuum elements, DRP paragraphs or Journey-stage coverage. Example service-family labels must be decomposed where appropriate. Do not describe Candidate 008 as an approved or exhaustive service target until the identity-aware source reconciliation and Journey/Capability/Feature coverage register are complete.

## Scope boundaries and exclusions

**In scope:** the complete Candidate 003 stakeholder/Journey scope, including 13 stakeholder perspectives, 29 Journeys, 11 reusable Journey definitions, stage outcomes, variants and qualifying gaps; Candidate 006 Business (15 CT1/86 CT2/258 CT3/1,299 Features) and Enterprise (9 CT1/47 CT2/139 CT3/700 Features) capability structures; existing and candidate granular Business and Application Services as required; existing Functions, Processes, Solution Facets and their single homes; governing Continuum and DRP semantics; relevant NFRs, principles, contracts and motivation; traceability, service-contract distinctions, exceptions, non-coverage and eventual Journey-based solution integrity tests.

**Not an automatic outcome:** one Service per Feature, one Service per API method, or one Service per Facet; forced coarse service grouping; assigning a home by lexical proximity; issuing new canonical identities before reconciled decisions; introducing technology-specific implementations into the Service catalogue; filling genuinely missing architecture by guessing; changing repositories; issuing migration authorisation.

**Service scope may be fine-grained:** a separately useful retrieval, search, validation, update of working state, specific `ProcessSomething` operation, governed commitment, acknowledgement, export, status or controlled-operation contract should be retained/proposed as an individual Service when the consumer-facing responsibility is independently coherent. Treat cross-service sequencing as a Process where that is the meaningful architectural responsibility. Do **not** ban an individual Service simply because it corresponds to one API operation.

**Detailed interface signatures, schema, deployment topology and class/method catalogues** remain outside this scoping pass, except where a governed external or DRP contract makes a distinction necessary to define an honest Service boundary. This is a scope boundary, not permission to omit those obligations from later design.

## Candidate-to-target promotion conditions

Before Codex receives a migration target, demonstrate all of the following:

1. **Continuum/DRP parity:** existing governed definitions, decisions and identities and the agreed intent of every relevant DRP have been examined with equal care. Reconciliation records what was preserved, re-expressed, merged or left unresolved, citing both sides. The immutable accepted 1.1.19 publication remains untouched.
2. **Stakeholder/Journey coverage:** all Candidate 003 stakeholder and Journey records (including the Patient/Participant distinction and provisional ResponsiblePerson investigation) are retained and traceable; every applicable value-stage obligation has an explicit coverage/gap result.
3. **Full capability scope:** all Candidate 006 Business and Enterprise CT1/CT2/CT3/Feature candidates are accounted for, without automatically promoting them to accepted Continuum content.
4. **Granular Services:** each proposed Service has one coherent consumer-facing promise, consumer(s), expected result/exception semantics, source rationale, specific exclusions, architectural home and evidence of why it is one Service rather than an arbitrarily bundled Process or a chopped-up method inventory.
5. **Existing behaviour preservation:** every governed Service/Function/Process has a source identity, definition and provenance mapped to **retain / reposition / refine / consolidate / retire / unresolved** as an analysis category, never silently deleted. The candidate home is explicit and singular; other uses are references.
6. **Coverage and obligation register:** every EC Feature, relevant Business Feature, DRP decision, requirement/NFR and Journey value-stage expectation has source-backed mapping to Services, Functions, Processes and/or Facet responsibilities, with partial coverage and non-coverage explicit. One-to-many and many-to-one associations are legitimate.
7. **Boundary tests:** DRP/Continuum distinctions survive for Mneme/Mnemosyne, Themis/Calliope/local PEP, Ergon/Praxis/Artemis/Petasos, and LogEvent/AuditEvent/Provenance/Assurance, alongside the Information Unit and temporal/governance contracts.
8. **Integrity tests:** test scenarios cover representative and specialised Journey paths, cross-stakeholder handoffs, refusals, failure/recovery, information versions, audits, authorisation, incomplete evidence and uncertainty. Each test distinguishes architectural representation from actual implemented behaviour. **Include the Retrieve → Check → Update → ProcessSomething → Commit scenario**, explicitly testing a failed Check, a discarded working Update, successful ProcessSomething execution without Commit, failed execution, rejected Commit, an uncertain commitment acknowledgement, and recovery or state reconciliation between execution and authoritative acceptance.
9. **Identity and governance discipline:** canonical identity, standing, provenance, one definition/many references, and unresolved semantics are preserved; Codex auto-approval, if separately authorised later, will be execution freedom **not** authority to invent architectural decisions.

### Expected scope artefacts for eventual migration

- **Preservation/reconciliation register** for existing Continuum and DRP evidence.
- **Stakeholder/Journey catalogue** with value-stream stages, reuse/specialisation, expected values and provisional statuses.
- **Complete Business and Enterprise Capability/Feature catalogue**, with per-stage coverage and non-coverage.
- **Granular Service catalogue**, distinguishing established identities, proposed additions and unresolved contract boundaries.
- **Service/Function/Process architectural-home register** and Solution Facet responsibility index.
- **End-to-end traceability and scenario-based integrity-test catalogue** suitable for subsequent architectural validation and implementation conformance evidence.

These outputs should be machine-readable as well as human-navigable, so that completeness can be checked programmatically rather than assumed from the size of the document.

## Scope thesis to challenge

> **A usable Harmonia target architecture preserves the meaning of the existing Continuum and DRPs, explains stakeholder value through reusable Journeys, accounts for required Business and Enterprise abilities, and exposes sufficiently granular, coherent Services with unambiguous architectural homes. Its integrity can be tested by tracing Journey outcomes through capabilities, Services and other governed behaviour, including explicit gaps.**

**Decision status:** Candidate 009 retains Candidate 008’s Continuum/DRP parity, Stakeholder/Journey-led integrity coverage and granular-Service discipline; it adds the **agreed five-intent reference model** (`Retrieve → Check → Update → ProcessSomething → Commit`), explicit separation from `ArchiMate::Process`, and independently testable **execution success versus authoritative commitment**. The semantic distinction is agreed architectural thinking, not a claim of previous Continuum incorporation. This document remains a reviewable scoping proposal. No new Service, home, canonical identity or migration authorisation is approved by this document.
