# Harmonia — Service Architecture Scope
## Candidate 007 — Architectural Scoping Brief

**Date:** 4 November 2026  
**Status:** Working design proposal; neither architectural acceptance nor repository incorporation  
**Authoritative repository snapshot:** `fhirfactory/harmonia`, branch `Capability-Map-Consolidation`, HEAD `fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374` (observed 4 November 2026)  
**Target reference:** Enterprise Capability Candidate 006 (9 CT1, 47 CT2, 139 CT3, 700 Features).  
**Purpose:** Set the *extent and boundaries* of the Service Architecture that must be reconciled with the existing Continuum, accepted architectural thinking in DRPs, and the Solution Facets. This is **not** the Service Catalogue, an implementation plan, a Codex task, or permission to modify the repository.

## Executive proposition

Harmonia should expose a coherent **portfolio of service offerings** that explains what information systems provide to their consumers. The portfolio must be wide enough to meet the obligations in the Enterprise Capability architecture, the governed Continuum and the DRPs. It must not reproduce a hierarchy of software components, technical operations, APIs or Features.

The scope exercise has four complementary results:

1. **Service obligations** — behaviours or functionality which somebody or something must be able to consume.
2. **Service qualities** — measurable characteristics (e.g. FastRead, survivability, policy compliance) attached to service offerings; not inherently new services.
3. **Internal responsibilities** — Functions and Processes necessary for fulfilment, appropriately at home in either a Capability/Feature or a Solution Facet. They are not automatically Services.
4. **Coverage gaps** — accepted architectural obligations for which no adequate existing/candidate Service, Function, Process, contract or Solution Facet responsibility can be located.

Every Service, Function and Process must have **one architectural home**, within a Capability/Feature or Solution Facet. Other architectural contexts may reference it. Home is not inferred from name similarity, provenance, current file placement or presence of a contract. This scope analysis does **not** propose Realisation relationships.

### Sources and their authority

- **Current governed Continuum:** authoritative recorded representation, including historical candidates, standing, provenance, native Services/Functions/Processes and Solution Facets.
- **DRPs:** authoritative evidence of agreed architectural *intent*, even if labelled Draft because incorporation was postponed. An explicitly unresolved matter inside a DRP remains unresolved. Differences between design intent and current representation become reconciliation work, not automatic grounds for rejecting the DRP.
- **Candidate 006:** proposed EC target. Its CT1/CT2/CT3/Feature hierarchy is working design input, **not accepted Continuum architecture**.
- **Solution Facets:** governed decomposition of architectural responsibility; they challenge completeness and home placement, but do not dictate the Service taxonomy.
- **NFRs and Motivation:** required outcomes and constraints to be traced through service obligations. A measurable Feature or NFR is not itself a Service.

The source inventory currently identifies **5 native Application Services, 142 Business Services, 160 Business Functions, 21 Business Processes**, and **664 Capability documents** in the branch directories. These are source-file counts, not approved target service counts. Existing Business Service names/placements may preserve historical BEC context and require identity-aware reconciliation.

## Scope domains — candidate service offerings

**Notation:** `S` = candidate recognisable service offering; `Q` = quality/assurance expectation, not ordinarily a new service; `I` = internal responsibility/function/process to examine; `?` = boundary or existence still to verify. The examples are scoping hypotheses, not accepted individual Service elements.

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
| Authentication, Passport currency and local enforcement | Policy correctness cannot be delegated to a generic shared security server | Themis DRP |
| Immutable evidence and separate audit/provenance/logging | An operational log is not an AuditEvent or evidence of provenance | Logging and audit DRPs |
| Explicit recovery semantics | Recovery cannot resurrect partial Ergon execution or silently lose accepted work | Pragmaton, Petasos and resilience DRPs |
| Information semantics independent of storage | InformationUnit definition must not collapse into persistence implementation | Information Unit Framework DRP |
| Standards-native boundary conformance | External consumers experience supported contracts, not Harmonia's internal schema | Interoperability axioms |
| Governed result-set and analytical access | Discovery, result metadata, aggregation and actual content access differ | Mneme/Mnemosyne interaction and search decisions |
| Multiple Solution Facet contributions | One service value may depend on several responsibilities without duplicating their architectural homes | Architecture-wide home rule |
| Explicit service quality | Throughput, latency, availability, concurrency and security are often qualities rather than new Service entities | Candidate 006 Features and NFRs |
| Operational administration and observability | A solution that can deliver clinical value but cannot be operated, reviewed and recovered is incomplete | E08 and NFR/OA&M |

## The cold-question test — evidence of useful scope

The target Service Architecture should permit an analyst to ask a question in ordinary language and identify (a) the service offerings needed; (b) their business or system consumers; (c) the EC CT3s/Features they address; (d) the single architectural home of each Service/Function/Process; (e) any applicable DRP obligations; and (f) unresolved coverage, without having to invent missing architecture.

Illustrative tests:

1. *Can a consumer retrieve the exact previously accepted clinical information version if a cache node has failed?* (E01, E02, E05, E08)
2. *Can an external source send a non-conformant HL7 v2 message and receive the contractually correct acknowledgement or error?* (E03, E04, E08)
3. *Can a previously valid Passport be invalidated fast enough that it no longer authorises a distributed operation?* (E05, E02, E08)
4. *Can a unit of work be recovered after a crashed node without resuming in the middle of an Ergon or losing its governed context?* (E06, E01, E02)
5. *Can an appropriately authorised operator export AuditEvents and their evidence while the system rejects any unauthorised referenced information?* (E08, E01, E02, E05)
6. *Can an authorised clinical reader understand the source, freshness and longitudinal context of displayed information?* (E07, E01, E02, E05)
7. *Can a participant's information be discovered for legitimate care coordination without disclosing unrelated compartment-protected content?* (E02, E05, E09)

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

**Completeness qualification:** This is an initial domain/service-family scoping pass, **not** a full semantic audit of every Continuum Service/Function/Process, Requirement, Principle and DRP paragraph. Its proposed service offering labels are hypotheses. An authoritative individual-service target cannot honestly be claimed before the source-by-source and EC Feature coverage register is completed.

## Scope boundaries and exclusions

**In scope:** all nine EC domains, all 47 CT2s and 139 CT3s, all 700 candidate Features as coverage questions; existing Service/Function/Process identity/provenance; source DRP obligations; Solution Facet boundary review; NFR and requirement traceability; missing-service and missing-Facet findings; proposed homes; semantic collisions; operational service obligations; unresolved assumptions and explicit exceptions.

**Not an automatic outcome of this exercise:** one Service per CT3/Feature; one CT1 per Facet; assigning every historical Service to a new home without evidence; introducing canonical identities; modelling platform frameworks or implementation technology as offerings; specifying detailed APIs, data schemas or deployment topology; closing DRP open questions by guesswork; changing the repository; creating migration instructions before target review.

**Beyond initial target unless evidence demands otherwise:** exhaustive UI screens and controls, all possible healthcare-sector enterprise applications, speculative AI decision services, implementation-specific persistence adapters, detailed class/method inventories, and generic infrastructure tooling with no identifiable architectural service value. These exclusions are not exclusions of applicable NFR obligations.

## Candidate-to-target promotion conditions

Before Codex receives this as a migration target, the following must be demonstrable:

1. Every existing Service/Function/Process has been evaluated for **retain / reposition / refine / consolidate / retire / unresolved** while preserving original canonical identities and provenance evidence where semantically appropriate. Those are review classifications, not new formal governance dispositions.
2. Every new proposed Service has an independently understandable service promise, consumer, source rationale, proposed *single architectural home* and scope exclusions.
3. Every Enterprise CT3 and Feature has a coverage outcome: service offering(s), quality attached to a service, internal Function/Process responsibility, or uncovered requirement; **no mandatory one-to-one mapping**.
4. Every DRP obligation and applicable NFR has a source trace with its standing and any outstanding question recorded.
5. Existing and proposed Service boundaries do not erase the distinctions among Mneme/Mnemosyne, Themis/Calliope/local PEPs, Ergon/Praxis/Artemis/Petasos, and LogEvent/AuditEvent/Provenance/Assurance.
6. The target has been challenged with cold business and system questions, including failure, security, information history, cross-Facet work and operational support.
7. The migration dossier explicitly preserves the immutable accepted 1.1.19 publication. Codex's eventual execution freedom will not constitute permission to invent unresolved architectural meaning.

## Scope thesis to challenge

> **A complete Harmonia Service Architecture is not one with a Service for every Feature. It is one in which every Enterprise Feature and accepted architectural obligation has an understandable, unambiguous place in the Service/Function/Process architecture, every behaviour has an architectural home, and gaps can be explained without inventing implementation details.**

**Decision status:** Submitted for Solution Architect discussion as scope framing only. No Service, home or naming decision is accepted by this document.
