# Harmonia 1.1.20 — Pass 3 / Step 4 — 1.1.19 Functional Coverage Gate

**Iteration:** 001  
**Review:** 2026-11-06  
**Status:** **PROVISIONAL — NOT READY FOR COVERAGE ACCEPTANCE**  
**Purpose:** Decide whether Candidate 017 preserves distinct functional responsibilities in the immutable RAD 1.1.19 publication; no UUID/element migration audit.

## Executive judgement

**No wholesale functional disappearance was established at the responsibility-family level. Nevertheless, functional equivalence is not yet demonstrated for 31 responsibility bundles. The coverage gate therefore cannot be declared PASS.**

The review groups the legacy functional intent into **101 non-identical responsibility bundles**. **35** appear covered, **34** have equivalent intent organised differently, **31** need explicit demonstration or supplementation, and **1** is a later-release item deliberately outside the 1.1.19 regression obligation.

| Assessment | Responsibility bundles | Interpretation |
|---|---:|---|
| Covered | 35 | Candidate contains a meaningful counterpart |
| Covered differently | 34 | Reorganisation or decomposition preserves responsibility |
| Partially covered / unproven | 31 | Old behaviour has no demonstrably complete counterpart yet |
| R3.x deferred | 1 | Newly proposed, not established 1.1.19 regression |

These are counts of **semantic bundles, not historical elements or source obligations**. They must not be represented as a percentage of 1,994 elements or proof of 100% requirement compliance.

### Recommended decision

**Hold the Step 4 gate open**, but proceed only with *targeted* equivalence adjudication—not wholesale rediscovery. Resolve whether each partial obligation is (a) already adequately represented when properly interpreted, (b) genuinely missing and requiring a bounded additional Feature/capability, or (c) a source-domain business responsibility that Harmonia only enables rather than performs.

## Scope and evidence

1. Accepted reference: [RAD 1.1.19 published navigation](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/enterprise-architecture/documentation/continuum-manifest/published/1.1.19/README.md) and [publication integrity statement](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/enterprise-architecture/documentation/continuum-manifest/published/1.1.19/publication.json). Publication declares **1,994 elements / 2,235 relationships**, SHA256 of published README `1b0d1eda18661b79192bdf115fea314a8a7ac6148d3caf923028c5a87bd451c8`.
2. New candidate: `Harmonia_1.1.20_Candidate_017_Complete_Capability_Catalogue.md`; local SHA256 `50b7877a471d71f35e1444a693339da271ddd7dd7dfb3a9cca7df59f0ff72ae2`; **586 CT3** and **2944 Features**, no governed identities yet.
3. Design intent cross-check: [Business/Application architecture](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/docs/markdown/05-information-systems-architecture/README.md), [existing Business Services and Functions](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/enterprise-architecture/documentation/continuum-manifest/published/1.1.19/README.md), [Identity Design Note](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md), [Pylai Design Note](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Pylai-External-Interoperability-Architecture.md), [Energeia Design Note](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Energeia-Operational-Management-and-Praxis-Responsibility.md).
4. Assurance/export and related requirements: [Audit, Provenance and Export DRP](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md), [NFR architectural elements](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md), and [ICT Foundation lenses](https://github.com/fhirfactory/harmonia/blob/Capability-Map-Consolidation/docs/markdown/02-strategy/capabilities/ict-foundation-lenses.md).

The accepted 1.1.19 **Markdown publication** was used as the authority-facing navigation index. The large ArchiMate XML was **not** parsed, loaded into chat or used as authority. Published navigation includes historical/superseded identities, which are not treated as independent obligations. Nonfunctional requirements remain in Motivation; the coverage gate checks their supporting capability, not duplication of every threshold into a Feature.

## Coverage method

1. Identify distinct **functional outcomes**, not historical IDs, from the published strategy capability tree, relevant business functions/services, application services/responsibilities and selected NFR families.
2. Merge overlapping source descriptions into **semantic bundles** so that multiple old elements can map to one new responsibility and vice versa; do not assume 1:1 cardinality or inherited structural placement.
3. Read the Candidate 017 target CT2/CT3/Feature descriptions and map the candidate responsibilities that jointly fulfil each bundle. Separate a generic enabling platform function from a domain-specific service contract.
4. Classify `COVERED`, `COVERED_DIFFERENTLY`, `PARTIAL`, or `DEFERRED_R3` (the last being outside historical equivalence); no positive `NOT_COVERED` assertion was established at this level, but PARTIAL does **not** imply success.
5. If the source is a healthcare **Business** capability, preserve its business meaning without claiming Harmonia is an EMR, pathology service, pharmacy, hospital management system or clinical decision authority.
6. Preserve independent *reconciled design note / DRP* constraints as interpretation evidence, without automatically promoting draft implementation specifics into approved new Capabilities.

### Important evidence limitation

This is a **functional responsibility-family assessment**, not exhaustive adjudication of every Business Function, every service operation or every NFR. The published navigation was inspected for all current major functional domains; source function/service names were sampled systematically as stress tests. Several family-level positives are still conditional on later semantic refinement. The source snapshots represent the published 1.1.19 edition on the connected `Capability-Map-Consolidation` branch; no independent local snapshot of the whole 1.1.19 Markdown tree was reconstructed.

## High-priority adjudication queue

| Item | Priority | Subject | Evidence and recommended action |
|---|---|---|---|
| **P01** | HIGH | Independent assurance / Service Guardian | 1.1.19 EC-14 explicitly owns assurance disposition, criteria applicability, evidence assessment and independent adjudication; E08 currently leans toward observation and evidence. **Action:** Expand/reclassify existing E08 assurance semantics, not generic monitoring; preserve subject/assessor independence. |
| **P02** | HIGH | Clinical orders, medication events and controlled outcomes | 1.1.19 functions include receive/order modification/cancellation/dispatch and dispense/admin event correlation. Generic E06 task handling is not proof of closed-loop clinical order interactions. **Action:** Demonstrate equivalent bounded integration and progression semantics without turning Harmonia into EMR/pharmacy. |
| **P03** | HIGH | Clinical diagnostics modality worklist / requisition lifecycle | 1.1.19 explicitly includes modality worklist distribution, diagnostic order/result correlation and publication. No corresponding precise Enterprise Feature currently apparent. **Action:** Confirm bounded HIE diagnostic workflow support; identify target E03/E06 Feature contributions. |
| **P04** | HIGH | Emergency/acute fast-path and clinical summary access | 1.1.19 includes ED triage ingress, emergency fast-path LHR query and acute alert dispatch; generic E07 views are insufficient as proof. **Action:** Confirm early-release required vs informative service; preserve priority and emergency availability if required. |
| **P05** | HIGH | Application-specific collaboration semantics | 1.1.19 Agora/Omilia room, membership, clinical context and governed capture may be reduced to generic UI/collaboration words. **Action:** Check native collaboration responsibilities without dictating Matrix deployment. |
| **P06** | HIGH | Operational and platform integration bridge | Management Of / Integration With technical middleware not explicit enough despite existing ICT foundation lenses. **Action:** Carry forward existing enablement lens requirements to E08/E03/E01 interfaces and technology Service boundary; do not transfer IT/Ops to Energeia. |

## Other significant partial coverage

| Item | Relevant bundles | What remains to prove |
|---|---|---|
| P07 — Clinical workforce rostering, presence and specialist availability | FC-063;FC-066 | 1.1.19 on-call lookup, staff presence, roster capture and external roster source are not equivalent to a generic central scheduling capability. |
| P08 — Preventive screening and immunisation exchange | FC-044 | 1.1.19 immunisation history query, screening status and record syndication are not explicitly traceable from B13.02 alone. |
| P09 — Clinical device lifecycle, identity and endpoint availability | FC-031;FC-059 | Business device lifecycle and BEC device connectivity/metadata are not all represented by clinical equipment readiness. |
| P10 — Care-place state, ward/theatre/clinic telemetry | FC-061;FC-062;FC-078 | 1.1.19 bed turnover, ED and theatre status, ward rounds and clinic session progression are more specific than B09 readiness. |
| P11 — Outreach, primary care, virtual care and remote telemetry | FC-042;FC-069 | 1.1.19 operational service examples have specific ingestion, visit/batch and remote telemetry semantics. |
| P12 — HIE endpoint management and source profile governance | FC-075 | 1.1.19 HIE Endpoint State Management and implementation/requirements management may not be fully covered by generic interface visibility. |
| P13 — Clinical service guardian vs generic release assurance | FC-016;FC-032 | Avoid accidental merging of independent assurance with Operational Assurance during rationalisation. |
| P14 — Surgical/procedural booking and discharge dossier | FC-053;FC-064 | Procedure booking, theatre case progression and discharge dossier are present in 1.1.19 Services but not named in EC Feature semantics. |
| P15 — External provision & consumption service scope | FC-090 | Candidate 017 has generic E03 capability but recent explicit FHIR resource subset and purpose-specific operation lists have not been fully reconciled. |
| P16 — Source authority and competing assertions | FC-012 | E01/E05 account for information quality, but 1.1.19 source authority, unresolved conflicts and non-destructive reconciliation need precise confirmation. |

## Coverage by established responsibility source

### 1.1.19 Enterprise capability responsibility

| Bundle | Legacy functional intent | Candidate 017 coverage | Status |
|---|---|---|---|
| FC-001 | Governed activity execution, entity-relative progression and fulfilment definition | E06.01; E06.02; E06.09 | Covered Differently |
| FC-002 | Event publication, subscription, matching and handoff | E06.03; E06.04; E06.08 | Covered Differently |
| FC-003 | Human interaction and usable contextual experience | E07.01; E07.02; E07.03; E07.06 | Covered |
| FC-004 | External interoperability, information exchange and protocol mediation | E03.01; E03.02; E03.03; E03.04; E03.05; E03.06 | Covered Differently |
| FC-005 | Context loading, use, checkpointing and operational continuity | E06.06; E02.01; E01.02 | Covered Differently |
| FC-006 | Managed information, active accessibility and durable authoritative state | E01.01; E01.03; E01.04; E02.03 | Covered |
| FC-007 | Binary-content association, external custody, resolution and assembly | E01.05; E01.06 | Covered Differently |
| FC-008 | Managed entities, healthcare relationships and identity association | E05.07; E02.06; E01.03 | Covered |
| FC-009 | Managed state, state history and lifecycle transitions | E01.02; E06.01; E06.07 | Covered |
| FC-010 | Governed search, discovery and directory lookup | E02.02; E02.06 | Covered |
| FC-011 | Information models, terminology, datasets and semantic conformance | E04.01; E04.02; E04.03; E04.04; E04.05 | Covered |
| FC-012 | Source authority qualification and conflicting assertion handling | E01.03; E05.07; E02.01 | Partial |
| FC-013 | Operational monitoring, health and generic conformance evidence | E08.01; E08.04; E08.05; E08.06 | Covered Differently |
| FC-014 | Themis policy evaluation, effective authority and local PEP enforcement | E05.01; E05.02; E05.03; E05.04; E05.06 | Covered |
| FC-015 | Audit evidence, provenance, lineage and traceability | E08.03; E08.07; E08.08 | Covered Differently |
| FC-016 | Service Guardian independent assurance: criteria applicability, assessment, adjudication, findings and conclusions | E08.04; E08.07; E08.08 | Partial |

### 1.1.19 Business capability responsibility

| Bundle | Legacy functional intent | Candidate 017 coverage | Status |
|---|---|---|---|
| FC-017 | Care entry, eligibility, intake, referral and access arrangements | B01.01; B01.02; B01.03; B05.02 | Covered |
| FC-018 | Care coordination, responsibilities, transfer, discharge and continuity | B05.01; B05.02; B05.03; B05.04; B05.05; B05.06 | Covered |
| FC-019 | Appointments, capacity, clinical places and service scheduling | B07.03; B09.01; B07.04 | Covered Differently |
| FC-020 | Clinical logistics, patient movement and care support | B09.04; B09.05; B07.04 | Covered |
| FC-021 | Health service readiness and preparedness | B12.05; B07.01; B09.06 | Covered |
| FC-022 | Clinical quality, safety, criteria review and risk response | B11.01; B11.03; B11.04; B11.05 | Covered |
| FC-023 | Preventive health, health promotion and surveillance | B13.01; B13.02; B13.03 | Covered |
| FC-024 | Digital healthcare communication and HIE sharing programmes | B10.03; B05.06; E03.01 | Covered Differently |
| FC-025 | Corporate support services, IT operations, people and facilities | B12.03; B12.04; B07.01; E08.01 | Partial |
| FC-026 | Health service financial activity, charging and funding | B12.02; B15.02; B15.04 | Covered |
| FC-027 | Diagnostic investigation, specimens, findings and results delivery | B04.01; B04.02; B04.03; B04.04 | Covered |
| FC-028 | Medication management, dispensing, preparation and administration | B06.01; B06.02; B03.01 | Partial |
| FC-029 | Clinical knowledge context, publication, stewardship and use | B10.05; B13.06; E04.02 | Covered Differently |
| FC-030 | Health information catalogue, retrieval, correlation, lifecycle and compartmentalisation | B10.01; B10.02; B10.03; B10.06; B10.07 | Covered |
| FC-031 | Clinical device management and device integration | B09.02; B09.06; E03.03 | Partial |
| FC-032 | Health innovation and research information provision | B13.04; B13.05; B13.06 | Covered |
| FC-033 | Independent health service assurance definition, evaluation and conclusions | B11.03; B11.04; E08.04 | Partial |
| FC-034 | Health-service strategic alignment, needs modelling and implementation | B12.01; B07.01; B12.05 | Covered Differently |
| FC-035 | Business compliance, policy and governance reporting | B11.03; B11.05; B12.03 | Covered |
| FC-036 | Clinical decision, intervention, planning, assessment and monitoring | B02.01; B02.02; B02.04; B02.06; B03.01; B03.03 | Covered |
| FC-037 | Population health information and regional planning | B13.01; B13.03; B12.01 | Covered |
| FC-038 | Digital identity, access control, privacy and accountability | B10.04; B10.06; E05.01; E05.02; E05.05 | Covered |
| FC-039 | Terminology, standards adoption and conformance governance | B10.05; E04.01; E04.02; E04.04; E04.05 | Covered Differently |
| FC-040 | Practitioner workforce planning, qualification, privilege and service coverage | B08.01; B08.02; B08.03; B08.04 | Covered |
| FC-041 | Healthcare organisation, provider, service and location structure | B07.01; B07.02; B09.01; E02.06 | Covered |

### 1.1.19 Business-enabling function/service responsibility

| Bundle | Legacy functional intent | Candidate 017 coverage | Status |
|---|---|---|---|
| FC-042 | Community and outreach care context, visit synchronisation and batch reconciliation | B05.04; B05.06; E03.01; E06.07 | Partial |
| FC-043 | Care plan intake, change dissemination, context query and coordination | B02.06; B05.01; B05.04; E03.01; E02.04 | Covered Differently |
| FC-044 | Screening and immunisation: historical query, eligibility timeline and record syndication | B13.02; B02.03; E02.02; E03.01 | Partial |
| FC-045 | Demographic intake, correction, client/subject identity and person identifier correlation | B10.07; E05.07; E02.06; E03.01 | Covered Differently |
| FC-046 | Secure clinical communication, delivery acknowledgements and message tracking | E03.01; E03.04; E02.04; B10.03 | Covered Differently |
| FC-047 | Clinical document lifecycle, metadata registry, publication and disclosure | B10.01; B10.06; E01.02; E02.04; E01.06 | Covered Differently |
| FC-048 | Diagnostic worklist, requisition correlation, result publication and delivery | B04.01; B04.03; E03.01; E06.01 | Partial |
| FC-049 | Emergency/acute episode: triage stream ingress, fast-path clinical summaries and alerts | B03.04; B05.03; E03.01; E07.03 | Partial |
| FC-050 | Encounter lifecycle, location history, context resolution and notifications | B05.04; E06.01; E02.01; E02.04 | Partial |
| FC-051 | Medication order, administration, dispense, allergy and adverse-reaction correlation | B06.01; B06.02; E03.01; E02.02 | Partial |
| FC-052 | Order request, cancellation, routing, dispatch, result association and tracking | B04.01; E06.01; E06.02; E06.03 | Partial |
| FC-053 | Procedure and theatre bookings, surgical documentation and capacity progression | B03.05; B09.01; B07.03; E06.01 | Partial |
| FC-054 | Referral submission, progression, outcome and context query | B05.02; B05.03; E06.01; E03.01 | Covered Differently |
| FC-055 | Appointment status, schedule query and notification | B07.03; B01.02; E06.05; E02.04 | Covered Differently |
| FC-056 | Consent and confidentiality restrictions, relationship/representative authority | B10.04; E05.02; E05.05; E05.07 | Covered Differently |
| FC-057 | Provider, practitioner, organisation, location and service directory queries | B07.01; B07.02; B08.02; E02.06; E05.07 | Covered Differently |
| FC-058 | Professional privilege and credential validity for work dispatch | B08.02; B08.03; E05.02; E06.08 | Partial |
| FC-059 | Clinical device connectivity, identifiers, endpoints and OA&M metadata | B09.02; E02.06; E03.03; E08.01 | Partial |
| FC-060 | Specimen logistics, custody and transport progression | B04.02; B09.04; B09.05; E06.02 | Covered Differently |
| FC-061 | Bed and care-place state/turnover, room/ward readiness and capacity | B09.01; B07.03; E06.01; E08.01 | Partial |
| FC-062 | Clinics, outpatient queues, session status and follow-up coordination | B07.03; B07.04; B05.05; E08.01 | Partial |
| FC-063 | Staff presence, on-call availability and roster source integration | B08.01; B08.06; E06.05; E02.06 | Partial |
| FC-064 | Discharge dossier packaging, handover and care-transition reporting | B05.03; B05.05; E07.03; E02.04 | Partial |
| FC-065 | Mobile worker dispatch, operational work orders and external work status | B07.04; E06.08; E06.09; E07.06 | Covered Differently |
| FC-066 | Clinical calendar, temporal event correlation and shift/booking projections | B07.03; E06.05; E07.01 | Partial |
| FC-067 | Health service knowledge datasets, concept mapping, dictionary and standards profiles | B10.05; E04.01; E04.02; E04.03; E04.04 | Covered |
| FC-068 | Patient communication, patient task notification and self-service access | B01.04; B01.06; E07.02; E02.04 | Covered Differently |
| FC-069 | Remote patient telemetry, primary care and virtual encounter information ingress | B03.03; B05.06; E03.01; E03.03 | Partial |
| FC-070 | Longitudinal record, query, active-state, historical access and contextual display | B10.01; E07.03; E02.01; E01.02 | Covered Differently |
| FC-071 | Information authority, result filtering, data governance and reference access | B10.02; B10.03; E02.01; E05.02; E01.05 | Covered Differently |
| FC-072 | Audit capture, significant lineage, secure logging and accountable handling | B10.06; E08.02; E08.07; E08.08 | Covered Differently |
| FC-073 | Research information release and curated purpose-qualified extracts | B13.04; B13.05; E09.05; E03.07 | Covered Differently |
| FC-074 | Independent assurance design/criteria, evidence assessment and conclusion | B11.03; E08.04; E08.07 | Partial |
| FC-075 | HIE endpoint registration, active state, programme requirements and implementation | B07.01; E03.02; E03.05; E08.01 | Partial |
| FC-076 | Clinical collaboration space, operational collaboration and communication metadata | B05.01; E07.02; E02.04 | Partial |
| FC-077 | Information asset stewardship, classifications, tags, lifecycle and access qualifications | B10.01; B10.02; E01.03; E05.05 | Covered Differently |
| FC-078 | Business service operations: shift, clinical roster, emergency department, ward and theatre | B07.03; B07.04; B09.01; E08.01 | Partial |
| FC-079 | Governed collaboration, activity timeout, escalation and indeterminate effects | E06.01; E06.02; E06.04; E06.07; E07.06 | Covered Differently |
| FC-080 | Clinical information standards exchange, search and reference datasets | E02.02; E03.06; E04.02; E04.04 | Covered |

### 1.1.19 Application responsibility

| Bundle | Legacy functional intent | Candidate 017 coverage | Status |
|---|---|---|---|
| FC-081 | Pylai external Terma protocol boundary, transient interaction and durable acceptance | E03.01; E03.03; E03.04; E06.04 | Covered Differently |
| FC-082 | Mneme governed information access, Context/Pragma and Mnemosyne durable services | E01.01; E02.01; E02.02; E06.06 | Covered Differently |
| FC-083 | Petasos/Artemis parcel routing, durable backlog and task handoff | E06.03; E06.04; E06.08 | Covered Differently |
| FC-084 | Themis authorisation with external IdP and distribution to local PEPs | E05.01; E05.02; E05.03; E05.04; E05.06 | Covered Differently |
| FC-085 | Kleio audit, Dokimasia assurance and provenance export | E08.04; E08.07; E08.08 | Partial |
| FC-086 | Sylloge binary reference, remote custody and media resolution | E01.05; E01.06 | Covered Differently |
| FC-087 | Energeia Praxis, Journey and Harmonia operational reporting | E06.02; E08.01; E08.04 | Partial |
| FC-088 | Agora/Omilia native collaboration and mediated clinical dialogue | E07.02; E02.04; E07.06 | Partial |
| FC-089 | Platform integration, deployment manageability, middleware and technical OA&M | E08.01; E08.05; E08.06; E03.02 | Partial |
| FC-090 | Provider directory, HIE, LHR and identity association as externally offered capabilities | E02.06; E03.02; E03.06; E05.07; E07.03 | Partial |

### 1.1.19 Motivation / NFR obligation

| Bundle | Legacy functional intent | Candidate 017 coverage | Status |
|---|---|---|---|
| FC-091 | Immutable and durable acceptance before positive ACK, no information loss | E01.01; E03.04; E06.04; E06.07 | Covered |
| FC-092 | Versioned info and historical retrieval, archival, integrity, migration | E01.02; E01.03; E01.04 | Covered |
| FC-093 | Access restrictions, isolation, authorisation, revocation and credential rotation | E05.02; E05.03; E05.04; E05.05; E05.06 | Covered |
| FC-094 | MLLP timeouts, original/enhanced ACK and HL7 v2 tolerance | E03.04; E03.06 | Covered |
| FC-095 | High-concurrency workload, backpressure and deterministic scheduling | E06.04; E06.05; E08.05; E08.06 | Covered |
| FC-096 | Availability, disaster recovery and restart without lost governed work | E01.04; E06.04; E08.06 | Covered |
| FC-097 | Observability, protected logs, audit, provenance and release evidence | E08.01; E08.02; E08.04; E08.07; E08.08 | Covered |
| FC-098 | Large-content fit-for-purpose storage and pass-by-reference | E01.05; E01.06 | Covered |
| FC-099 | No silent external system failures, indeterminate effect reporting | E03.04; E06.07; E08.01; E08.05 | Covered |
| FC-100 | Temporal provenance, timezone and calendar consistency | E01.02; E06.05; E08.07 | Covered |
| FC-101 | External system-to-information availability impact Data Monitor | E08.01; E02.03 | Deferred R3 |

## Examples requiring careful semantic handling

**Clinical order integration is not clinical order ownership.** The accepted 1.1.19 Business Functions include receiving orders, managing cancellation/modification, correlating outcomes and tracking dispense/administration events. A generic E06 task is not automatically equivalent to these information-flow semantics. We should show the *integration and correlation* responsibility without accidentally making Harmonia an EMR or CPOE system.

**Service Guardian is not generic monitoring.** The accepted EC-14 defines independent assurance criteria applicability, contextual evidence assessment, adjudication and conclusions. Candidate E08 currently overemphasises obtaining evidence and conformance observation. Preserve EC-14 meaning explicitly even if split across several new CT3s.

**Business operations are not necessarily Harmonia-delivered services.** Bed turnover, pharmacy compounding, theatre operations, staffing and emergency triage can remain healthcare Business Capabilities. Harmonia only requires a directly implemented Enterprise Capability where the accepted HIE scope calls for accessing, exchanging, coordinating or reporting those operational facts.

**Application components are not capability identities.** Preserve the externally observable responsibilities of Sylloge, Pylai, Energeia, Agora/Omilia, Themis, Kleio, Mneme, Petasos and other relevant designs, but do not recreate the components as one-for-one CT3s.

**New decisions since 1.1.19 remain separate.** Selected FHIR API exposure, six purpose-specific provided operations, consumed enterprise services, the Management Of/Integration With technical bridge and the R3.x Data Monitor do not automatically inherit accepted 1.1.19 standing just because they were discussed in Candidate 017.

## Proposed gate and continuation

The logical next action is **bounded coverage adjudication of the PARTIAL rows**. For each, either demonstrate existing coverage in a concrete new Feature/CT3, identify a deliberate non-Harmonia ownership boundary, or author a narrow corrective candidate. A bare word match is not sufficient. Once these dispositions are reviewed, run the same family-level gate again after rationalisation.

**No acceptance, historical identity translation, repository modification, ArchiMate editing, Codex task, commit or push is authorised by this report.**
