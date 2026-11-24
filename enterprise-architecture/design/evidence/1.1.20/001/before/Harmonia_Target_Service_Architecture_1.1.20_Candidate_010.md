# Harmonia — End-to-End Target Service Architecture
## Candidate 010 — Detailed Service Architecture and Integrity Scope

**Date:** 4 November 2026  
**Standing:** architectural working candidate, subject to Solution Architect review; **not** migration authority  
**Source snapshot:** `fhirfactory/harmonia` · `Capability-Map-Consolidation` · `fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374`  
**Agreed scope:** [Candidate 009](Harmonia_End_to_End_Architecture_Scope_1.1.20_Candidate_009.md)  
**Target scope candidates:** [Candidate 003](Harmonia_Capability_Architecture_Clean_Sheet_1.1.20_Candidate_003.md), [Candidate 006](Harmonia_Capability_Architecture_1.1.20_Candidate_006_Proposed.json)

> **Design thesis.** Preserve the entire governed Continuum, apply our established DRP reasoning with equal seriousness, retain Stakeholders/Journeys as first-class architecture and integrity tests, and offer a **granular Service catalogue** whose individual promises can be understood and tested. No source identity is replaced simply because a newer candidate has a more convenient name.

### 1. What Candidate 010 establishes — and does not

- **147** source Service identities are preserved in an explicit inventory: **142 Business Services** and **5 Application Services**; their existing type, name, canonical ID and observed standing are retained.
- **174 granular target Service proposals**, including **5 candidate Technology Services**, span all **47 Enterprise CT2 responsibilities**. This is a candidate contract catalogue, **not** 174 authorised new elements.
- All **13** stakeholder perspectives, **11** reusable Journeys and **29** portfolio Journeys are carried forward explicitly from Candidate 003 and given scoped negative-test probes.
- A **1,999-row Feature demand register** preserves every Candidate 006 Business/Enterprise Feature as an unresolved integrity obligation, rather than falsely declaring it satisfied by nearby candidate Services.
- **26 architectural obligation assertions** provide cross-cutting tests, sourced to DRP themes, the Continuum and agreed scoping decisions.
- **No** ArchiMate canonical ID, Home, Composition, Realisation, new model relationship, software implementation, or release acceptance is created by this document. All proposed homes and associations require review.

### 2. Architectural authority and scope guardrails

1. **Source parity.** The governed Continuum and agreed DRPs are equal architectural evidence for the current design. Preserve their meaning, provenance and stated uncertainties. Reconcile apparent conflicts symmetrically.
2. **Publication immutability.** Published 1.1.19 remains immutable. The 1.1.20 branch is a candidate working state. Existing identity and standing survive until evidence-based correspondence and explicit disposition.
3. **One home.** Every Service, Function and Process ultimately has exactly one governed home under an eligible Capability/Feature or Solution Facet. Other relations may reference it. Here, proposedHome is a *single proposed assignment*, not an incorporated Home relationship.
4. **Granular consumer contract.** A Service promises one independently meaningful, testable outcome. A Process may coordinate Services, but does not conceal separate contracts.
5. **Five intentions.** `Retrieve → Check → Update → ProcessSomething → Commit` are distinguishable consumer intentions, not mandatory sequential steps or ArchiMate constructs; `ProcessSomething` is deliberately not `ArchiMate::Process`. Execution success is not authoritative commit.
6. **No artificial completeness.** A Feature or Journey requirement may demand Services, Functions, Processes, a Solution Facet contribution, a nonfunctional constraint or an external provider; a missing contribution remains a visible gap.
7. **No model-type conflation.** Existing Business Services, proposed Application Services and proposed Technology Services remain distinguishable even where they have similar names or consumer-facing intent.
8. **DRP interpretation.** Draft is incorporation status, not evidence of weak intent. Explicitly unresolved design choices remain open; neither the author nor Codex may silently decide them.

### 3. What was reviewed

- **Continuum:** existing Service directories, component directory and representative service content; the 1.1.20 review-register state, current source layout and standing metadata. Service identity inventory is complete by directory count; **full semantic body and relationship review of every element is not yet complete**.
- **DRPs:** 19 distinct current-lineage DRP files listed below, including two differently scoped Mneme↔Mnemosyne interaction treatments, with their decision and open-question sections. Earlier numbered drafts are retained in GitHub; not silently treated as unrelated designs.
- **Candidate 006:** all 24 CT1, 133 CT2, 397 CT3 and 1,999 Features are carried as candidate demand. A proposed Enterprise Service near a CT2 does not prove every associated CT3/Feature is met.
- **Candidate 003:** all 13 stakeholder perspectives, 11 reusable Journeys and 29 portfolio Journeys; all stage wording is carried from the candidate, not fabricated for this draft.

### 4. Application Service correspondence safeguards

| Existing Application Service | Existing identity | Candidate alignment to investigate | Disposition |
|---|---|---|---|
| Adhoc Search | `02bf54d699b44ed0b26fa5b8125f0f01` | Search Governed Information Units | Retain; semantic equivalence **unproven** |
| Context Load | `1ccfe420541945bb9c0756a949799d95` | Load Governed Work Context | Retain; semantic equivalence **unproven** |
| Context Save | `24d4fc42d9524d53a747f0550833f1a4` | Return Work Context Changes | Retain; semantic equivalence **unproven** |
| Adhoc Resource Access | `410f2f55bbda481b973c6c67b924f0be` | Acquire Adhoc Work Information | Retain; semantic equivalence **unproven** |
| External Interoperability Service | `43b79574792443faa57daa76b9a47a19` | Receive External Clinical Information; Submit Clinical Information Externally; Query External Exchange Status | Retain; semantic equivalence **unproven** |

**No name-only ID transfer.** `External Interoperability Service` may be architecturally broader than one new service-intent candidate. Whether it stays as a coherent exposed Service, is narrowed, or relates to several more granular Services is a distinct architectural judgement; it must not be quietly deleted.

### 5. Detailed candidate Application / Technology Services

Each row defines a **candidate independently consumable contract**. The evidence keys refer to relevant source obligations, **not to a claim that a source file contains this exact Service or name**. No architectural Home relationship is asserted.

#### E01 — Governed Information Custody

The system can hold, preserve, recover and account for governed information.

**E01.01 — Governed Information Persistence**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E01-01-01` · App | **Submit Governed Information Change** — Accept a requested information change for governed evaluation and authoritative disposition, without asserting commit merely from receipt. | Harmonia information consumer | `Facet:Mneme` | IU, MN, MM |
| `ES-E01-01-02` · App | **Commit Information Unit Version** — Return an authoritative acceptance, rejection or indeterminate outcome for a specific governed version change. | Mneme governed writer | `Facet:Mnemosyne` | IU, MM, PERSIST, NFR |
| `ES-E01-01-03` · App | **Query Authoritative Commit Outcome** — Resolve the known disposition of a previously submitted change without falsely repeating it. | Mneme or recovery coordinator | `Facet:Mnemosyne` | MM, PERSIST, RES |
| `ES-E01-01-04` · App | **Register Governed Content Custody** — Record the custody and reference of content whose managed representation is governed independently of provider internals. | Information custodian | `Facet:Mnemosyne` | IU, PERSIST |

**E01.02 — Information Version & History Management**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E01-02-01` · App | **List Information Unit Versions** — Return identified historical versions eligible for the requester and context. | Authorised information consumer | `Facet:Mneme` | IU, MN, NFR |
| `ES-E01-02-02` · App | **Retrieve Information Unit Version** — Return a specified version without substituting the latest state. | Authorised information consumer | `Facet:Mneme` | IU, MN, NFR |
| `ES-E01-02-03` · App | **Query Information Unit Lineage** — Return version and generation relations, with their governing temporal identities. | Governed information consumer | `Facet:Mneme` | IU, NFR |
| `ES-E01-02-04` · App | **Check Immutable Version Status** — Establish whether a specified version is protected by the applicable immutable-state restrictions. | Information custodian | `Facet:Mneme` | IU, NFR |

**E01.03 — Information Integrity Management**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E01-03-01` · App | **Check Information Unit Integrity** — Evaluate identity, content/reference integrity and applicable integrity evidence without changing the governed information. | Information consumer or custodian | `Facet:Mneme` | IU, NFR |
| `ES-E01-03-02` · App | **Query Information Origin Evidence** — Return origin and authority references available for a governed information version. | Authorised custodian | `Facet:Mneme` | IU, PROV |
| `ES-E01-03-03` · App | **Report Information Integrity Exception** — Record or emit a distinct integrity exception for subsequent assurance and recovery. | Integrity manager | `Facet:Mnemosyne` | IU, RES, NFR |

**E01.04 — Information Retention & Recovery**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E01-04-01` · App | **Request Information Archive** — Request a governed transition of an eligible information holding into archival custody. | Authorised information custodian | `Facet:Mnemosyne` | IU, RES, NFR |
| `ES-E01-04-02` · App | **Retrieve Archived Information Unit** — Recover an eligible archived Information Unit while preserving version, labels and provenance. | Authorised information consumer | `Facet:Mneme` | IU, RES, NFR |
| `ES-E01-04-03` · App | **Request Information Retention Disposition** — Obtain a governed decision on retention or RETIRE without implying physical deletion. | Information custodian | `Facet:Mneme` | IU, NFR |
| `ES-E01-04-04` · App | **Request Authoritative Custody Recovery** — Initiate recovery of governed persistence from a known failure or corruption state. | Operations recovery coordinator | `Facet:Mnemosyne` | RES, PERSIST, NFR |
| `ES-E01-04-05` · App | **Query Custody Recovery Outcome** — Return known recovery state, qualification and preserved-authority evidence. | Operations recovery coordinator | `Facet:Mnemosyne` | RES, NFR |

**E01.05 — Governed Information Reference Management**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E01-05-01` · App | **Resolve Governed Information Reference** — Resolve a governed reference to a stable eligible information identity and location abstraction. | Information consumer | `Facet:Mneme` | IU, REF |
| `ES-E01-05-02` · App | **Retrieve Referenced Binary Content** — Return eligible large content by managed reference without embedding it in general Information Unit persistence. | Authorised content consumer | `Capability:E01.05` | IU, REF, NFR |
| `ES-E01-05-03` · App | **Check Reference Availability** — State availability and validity of a managed reference without necessarily dereferencing payload. | Information consumer | `Facet:Mneme` | IU, RES |
| `ES-E01-05-04` · App | **Validate Information Reference Integrity** — Check reference ownership, identity and admissibility without mutating its target. | Information custodian | `Facet:Mneme` | IU, REF |


#### E02 — Information Access, Discovery & Distribution

The system can make information available to appropriate consumers at the time and scope required.

**E02.01 — Governed Information Access**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E02-01-01` · App | **Retrieve Governed Information Unit** — Return eligible governed content and status for a caller-scoped request. | Harmonia subsystem | `Facet:Mneme` | MN, IU, SEC |
| `ES-E02-01-02` · App | **Prepare Governed Information Update** — Establish a correlated candidate change in working context, distinct from authoritative acceptance. | Authorised work executor | `Facet:Mneme` | MN, PRAGMA, PRAGMATON |
| `ES-E02-01-03` · App | **Check Information Access Entitlement** — Return the applicable access disposition without exposing protected content. | Protected component PEP | `Facet:Themis` | TH, SEC |
| `ES-E02-01-04` · App | **Query Governed Access Disposition** — Return an interpretable access outcome including denied, absent, failure and uncertain cases. | Authorised subsystem | `Facet:Mneme` | MN, SEC |

**E02.02 — Information Discovery & Search**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E02-02-01` · App | **Search Governed Information Units** — Return a governed result set of Information Unit Version references, not an uncontrolled object dump. | Harmonia authorised consumer | `Facet:Mneme` | MN, IU, RESULT |
| `ES-E02-02-02` · App | **Continue Governed Search** — Continue a previously scoped search without losing authority, version or result semantics. | Harmonia authorised consumer | `Facet:Mneme` | MN, RESULT |
| `ES-E02-02-03` · App | **Retrieve Search Result Reference** — Return a selected Information Unit Version reference from a governed result set. | Ergon or viewer | `Facet:Mneme` | MN, RESULT |
| `ES-E02-02-04` · App | **Query Search Result Qualifications** — Explain partial, bounded, rejected and incomplete search outcomes where available. | Harmonia authorised consumer | `Facet:Mneme` | MN, RESULT |

**E02.03 — Distributed Information Availability**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E02-03-01` · App | **Query Distributed Information Availability** — Report reachable information access availability and eligible replica/service scope. | Information consumer or operations | `Facet:Mneme` | DIST, RES |
| `ES-E02-03-02` · App | **Check Distributed Information Currency** — Return freshness and consistency qualification of an accessible information representation. | Harmonia information consumer | `Facet:Mneme` | DIST, IU |
| `ES-E02-03-03` · App | **Verify Distributed Change Outcome** — Resolve whether a distributed write request was accepted authoritatively rather than infer from cache acknowledgement. | Governed writer | `Facet:Mneme` | DIST, MM, RES |
| `ES-E02-03-04` · App | **Resolve Distributed Information Endpoint** — Select an eligible access route without exposing persistence-provider topology as the service contract. | Harmonia information consumer | `Facet:Mneme` | DIST, PERSIST |

**E02.04 — Information Distribution & Notification**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E02-04-01` · App | **Publish Information Availability Notice** — Publish a governed notification of information availability subject to eligibility and disclosure. | Authorised information publisher | `Capability:E02.04` | IU, SEC |
| `ES-E02-04-02` · App | **Register Information Subscription** — Establish an authorised, scoped subscription to information availability events. | Authorised information consumer | `Capability:E02.04` | IU, SEC |
| `ES-E02-04-03` · App | **Cancel Information Subscription** — Remove a previously registered subscription without changing retained information. | Subscription owner | `Capability:E02.04` | IU, SEC |
| `ES-E02-04-04` · App | **Query Subscription State** — Return subscription status and its delivery/authorisation qualifications. | Subscriber or operator | `Capability:E02.04` | IU, SEC |
| `ES-E02-04-05` · App | **Notify Information Subscriber** — Deliver an eligible information reference/event and report disposition without treating delivery as content commitment. | Information subscriber | `Capability:E02.04` | IU, SEC |

**E02.05 — Information Access Continuity**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E02-05-01` · App | **Query Information Access Degradation** — Return the current access limitation and conditions that affect governed retrieval. | Information consumer or operations | `Facet:Mneme` | DIST, RES |
| `ES-E02-05-02` · App | **Request Information Access Restoration** — Request restoration of a degraded information access path without changing authoritative information semantics. | Operations coordinator | `Facet:Mneme` | RES, DIST |
| `ES-E02-05-03` · App | **Query Access Restoration Disposition** — Return whether usable governed access has been restored, including qualified degraded states. | Information consumer or operations | `Facet:Mneme` | RES, DIST |


#### E03 — Digital Interoperability & Service Interaction

The system can communicate with external/internal services using understandable, governed contracts.

**E03.01 — External Information Exchange**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E03-01-01` · App | **Receive External Clinical Information** — Accept externally submitted clinical information for standards-native exchange handling, not authoritative content acceptance by itself. | External clinical organisation | `Facet:Pylai` | EXCH, IU, NFR |
| `ES-E03-01-02` · App | **Submit Clinical Information Externally** — Initiate standards-conformant outbound exchange of selected eligible information. | Harmonia exchange caller | `Facet:Pylai` | EXCH, SEC |
| `ES-E03-01-03` · App | **Query External Exchange Status** — Return correlation, acknowledgement and outstanding exchange disposition. | Exchange participant | `Facet:Pylai` | EXCH, NFR |
| `ES-E03-01-04` · App | **Receive External Exchange Acknowledgement** — Correlate external protocol acknowledgements separately from Information Unit commit outcomes. | External exchange endpoint | `Facet:Pylai` | EXCH, NFR |

**E03.02 — Service Contract Exposure & Consumption**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E03-02-01` · App | **Discover Service Contract** — Return available governed service descriptions and version/eligibility constraints. | Service consumer | `Facet:Pylai` | EXCH |
| `ES-E03-02-02` · App | **Invoke Governed Service Contract** — Provide a bounded, authorised invocation of an exposed contract without leaking internal Mneme interfaces. | External or delegated consumer | `Facet:Pylai` | EXCH, SEC |
| `ES-E03-02-03` · App | **Check Service Contract Compatibility** — Determine whether proposed consumer/provider versions are compatible. | Service consumer | `Facet:Pylai` | EXCH, MODEL |

**E03.03 — Protocol & Format Mediation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E03-03-01` · App | **Interpret Exchange Protocol Envelope** — Normalise transport-level envelope information without altering protected native content meaning. | Exchange mediator | `Facet:Pylai` | EXCH, IU |
| `ES-E03-03-02` · App | **Mediated Protocol Submission** — Expose a supported protocol submission with applicable framing, timeout and ACK behaviour. | External interoperability partner | `Facet:Pylai` | EXCH, NFR |
| `ES-E03-03-03` · App | **Transform Exchange Representation** — Convert between authorised representations using an identified transformation model and explicit qualification. | Exchange caller | `Facet:Calliope` | MODEL, EXCH |
| `ES-E03-03-04` · App | **Retrieve Native Exchange Representation** — Return preserved original exchanged representation where governed retention permits. | Authorised exchange investigator | `Facet:Pylai` | EXCH, IU |

**E03.04 — Interaction Acknowledgement & Error Handling**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E03-04-01` · App | **Acknowledge Exchange Receipt** — Confirm receipt at the declared exchange boundary without implying durable governed acceptance. | Exchange partner | `Facet:Pylai` | EXCH, NFR |
| `ES-E03-04-02` · App | **Report Exchange Rejection** — Return a contract-specific rejection and reason safely to the invoking partner. | Exchange partner | `Facet:Pylai` | EXCH, NFR |
| `ES-E03-04-03` · App | **Query Exchange Outcome** — Return accepted, rejected, pending or indeterminate exchange status without conflating persistence outcome. | Exchange partner | `Facet:Pylai` | EXCH, MM |

**E03.05 — Exchange Compatibility & Conformance**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E03-05-01` · App | **Validate Exchange Conformance** — Check a submitted exchange against named standards/profile versions and report deviations. | Exchange operator | `Facet:Pylai` | EXCH, MODEL |
| `ES-E03-05-02` · App | **Retrieve Exchange Conformance Evidence** — Return evidence of exchange tests, validation and qualifications for a specific contract version. | Governance or QA actor | `Facet:Pylai` | EXCH, NFR |
| `ES-E03-05-03` · App | **Report Exchange Deviation** — Record an external deviation explicitly for governed investigation or interoperability feedback. | Exchange coordinator | `Facet:Pylai` | EXCH, NFR |


#### E04 — Semantic Meaning & Model Governance

The system can preserve, interpret, transform and evolve the meaning of managed information.

**E04.01 — Information Model Definition & Resolution**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E04-01-01` · App | **Resolve Information Model Definition** — Retrieve the versioned definition needed to understand Information Unit structures. | Model-dependent subsystem | `Facet:Calliope` | MODEL, IU |
| `ES-E04-01-02` · App | **Publish Governed Model Definition** — Expose a reviewed, released model definition, not an unapproved in-place mutation. | Model steward | `Facet:Calliope` | MODEL |
| `ES-E04-01-03` · App | **Query Model Version History** — Return released model versions and relevant compatibility/deprecation qualifications. | Model steward or consuming component | `Facet:Calliope` | MODEL |

**E04.02 — Terminology & Value Set Management**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E04-02-01` · App | **Resolve Terminology Concept** — Return identified concept meaning and version, with inactive or absent states distinguished. | Clinical or semantic consumer | `Facet:Calliope` | MODEL |
| `ES-E04-02-02` · App | **Retrieve Versioned Value Set** — Return the specified published value set and its exact version identity. | Terminology-dependent consumer | `Facet:Calliope` | MODEL |
| `ES-E04-02-03` · App | **Check Terminology Membership** — Assess whether a coded value is eligible in a particular named value-set version. | Semantic validator | `Facet:Calliope` | MODEL |
| `ES-E04-02-04` · App | **Query Terminology Change** — Return governed differences between released terminology versions. | Model steward | `Facet:Calliope` | MODEL |

**E04.03 — Semantic Transformation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E04-03-01` · App | **Translate Governed Information Representation** — Produce a new representation using an identified mapping, preserving qualifications and source identity. | Authorised information consumer | `Facet:Calliope` | MODEL, IU |
| `ES-E04-03-02` · App | **Retrieve Transformation Qualification** — Explain fidelity, loss, uncertainty and transformation-version provenance for a transformed result. | Information consumer | `Facet:Calliope` | MODEL, IU |
| `ES-E04-03-03` · App | **Resolve Mapping Definition** — Return the published transformation mapping applicable to specified model versions. | Transformation consumer | `Facet:Calliope` | MODEL |

**E04.04 — Semantic Validation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E04-04-01` · App | **Validate Information Against Model** — Return conformance findings against an explicitly selected model/version. | Harmonia information checker | `Facet:Calliope` | MODEL, IU |
| `ES-E04-04-02` · App | **Validate Terminology Binding** — Return terminology and binding findings for the selected model context. | Harmonia information checker | `Facet:Calliope` | MODEL |
| `ES-E04-04-03` · App | **Evaluate Semantic Compatibility** — Assess whether two governed semantic definitions are compatible for an intended interaction. | Model steward or exchange mediator | `Facet:Calliope` | MODEL, EXCH |

**E04.05 — Model Change & Release Governance**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E04-05-01` · App | **Submit Model Release Candidate** — Register a proposed controlled semantic release without publishing it. | Authorised model steward | `Facet:Calliope` | MODEL |
| `ES-E04-05-02` · App | **Check Model Release Compatibility** — Assess known dependent consumers, migration needs and regression expectations. | Model release authority | `Facet:Calliope` | MODEL, NFR |
| `ES-E04-05-03` · App | **Commit Governed Model Release** — Publish an authorised model/terminology release as a distinct governed acceptance event. | Model release authority | `Facet:Calliope` | MODEL |
| `ES-E04-05-04` · App | **Query Model Release Status** — Return scheduled, accepted, rejected or superseded release status without implying deployment everywhere. | Dependent solution component | `Facet:Calliope` | MODEL |


#### E05 — Identity, Trust & Information Protection

The system can establish, propagate and enforce appropriate digital trust and access boundaries.

**E05.01 — Digital Identity & Authentication**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E05-01-01` · App | **Authenticate User Identity** — Establish the identity authentication result and relevant claims for a human user. | Human-facing access boundary | `Facet:Themis` | TH, SEC, NFR |
| `ES-E05-01-02` · App | **Authenticate System Identity** — Establish trusted system-to-system identity with a bounded evidence context. | System interaction boundary | `Facet:Themis` | TH, SEC |
| `ES-E05-01-03` · App | **Query Authentication Evidence** — Return eligible authentication provenance/status without returning credentials. | Security assurance consumer | `Facet:Themis` | TH, SEC |

**E05.02 — Contextual Authorisation Determination**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E05-02-01` · App | **Evaluate Authorisation Criteria** — Determine whether an effective Passport satisfies the protected subject's RequiredCriteria. | Local PEP | `Facet:Themis` | TH, SEC |
| `ES-E05-02-02` · App | **Query Effective Authorisation Scope** — Return purpose/action/compartment-bound authorisation context for a requester. | Protected service boundary | `Facet:Themis` | TH, SEC |
| `ES-E05-02-03` · App | **Check Authorisation Currency** — Return whether a Passport/policy determination remains current and usable. | Local PEP | `Facet:Themis` | TH, SEC |

**E05.03 — Local Access Policy Enforcement**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E05-03-01` · App | **Enforce Local Access Decision** — Apply the protected subsystem's local PEP decision to a request, with fail-closed disposition; this is not a centralised Themis enforcement endpoint. | Every protected subsystem | `Capability:E05.03` | TH, SEC |
| `ES-E05-03-02` · App | **Report Access Denial** — Return a bounded local-PEP denial outcome and required evidence without disclosing protected information. | Protected subsystem | `Capability:E05.03` | TH, SEC |
| `ES-E05-03-03` · App | **Query Enforcement Evidence** — Provide authorised correlation of locally enforced decisions to evidence without centralising the enforcement authority. | Security assurance consumer | `Capability:E05.03` | TH, SEC, AUD |

**E05.04 — Security Context & Passport Propagation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E05-04-01` · App | **Retrieve Operational Passport** — Retrieve the applicable operational Passport by governed reference without exposing credentials. | Local PEP | `Facet:Themis` | TH, MN |
| `ES-E05-04-02` · App | **Distribute Operational Passport** — Make a refreshed Passport available to an eligible component under scoped authority. | Themis PD or protected subsystem | `Facet:Themis` | TH, MN |
| `ES-E05-04-03` · App | **Invalidate Operational Passport** — Withdraw or invalidate an outdated Passport, propagating currency constraints. | Themis policy authority | `Facet:Themis` | TH |
| `ES-E05-04-04` · App | **Query Passport Revision State** — Return version, epoch/revalidation status and expiry qualification without disclosure of secrets. | Local PEP or authorised operator | `Facet:Themis` | TH |

**E05.05 — Information Protection & Security Evidence**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E05-05-01` · App | **Check Information Protection Rules** — Evaluate encryption, information labels, isolation and release constraints for a proposed disclosure. | Protected subsystem | `Facet:Themis` | TH, SEC, IU |
| `ES-E05-05-02` · App | **Report Security Policy Exception** — Record an authorised, correlated policy exception or enforcement failure without silently relaxing control. | Security operations | `Facet:Themis` | TH, SEC, AUD |
| `ES-E05-05-03` · App | **Retrieve Security Accountability Evidence** — Return access-relevant security evidence through properly authorised reporting. | Authorised security investigator | `Facet:Kleio` | AUD, TH |


#### E06 — Digital Work, Events & Temporal Coordination

The system can carry forward and account for work, events and time-dependent execution.

**E06.01 — Task Representation & State Management**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E06-01-01` · App | **Create Actionable Task** — Establish a governed task with purpose, identity and applicable archetype semantics. | Work requester | `Capability:E06.01` | PRAGMA, TASK |
| `ES-E06-01-02` · App | **Query Task State** — Return the visible task state and coordination context, not simply parcel-delivery state. | Work participant | `Capability:E06.01` | PRAGMA, TASK |
| `ES-E06-01-03` · App | **Record Task Outcome** — Record a task's declared outcome with distinct execution and commitment correlations. | Authorised task owner | `Capability:E06.01` | PRAGMA, TASK |
| `ES-E06-01-04` · App | **Associate Task Fulfilment** — Record fulfilment-task association without merging independent task identities. | Task coordination consumer | `Capability:E06.01` | TASK |

**E06.02 — Workflow & Work Progression**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E06-02-01` · App | **Initiate Governed Workflow** — Create a workflow instance with its governed task obligations and execution scope. | Work initiator | `Facet:Ponos` | TASK, WORK |
| `ES-E06-02-02` · App | **Advance Workflow State** — Apply a valid progression event with explicit concurrency/ordering rules. | Workflow coordinator | `Facet:Ponos` | TASK, WORK, NFR |
| `ES-E06-02-03` · App | **Execute Individual Work Unit** — Invoke a bounded Ergon execution and return its outcome independently of commit. | Workflow or task controller | `Facet:Ergon` | WORK, PRAGMATON |
| `ES-E06-02-04` · App | **Query Workflow Progress** — Return progress, completion, blocked state and outstanding work obligations. | Workflow participant | `Facet:Ponos` | TASK, WORK |
| `ES-E06-02-05` · App | **Advance Digital Twin Work** — Progress an authorised Digital Twin workflow under distinct Behaviour responsibility. | Digital Twin work coordinator | `Facet:Behaviour` | TASK, WORK |

**E06.03 — Event & Parcel Routing**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E06-03-01` · App | **Route Petasos Parcel** — Select the exclusive governed Artemis destination for an inter-component parcel. | Parcel originator | `Capability:E06.03` | PARCEL |
| `ES-E06-03-02` · App | **Query Parcel Route** — Return the selected destination and applicable routing disposition without claiming delivery. | Parcel sender | `Capability:E06.03` | PARCEL |
| `ES-E06-03-03` · App | **Report Unroutable Parcel** — Expose an explicit routing rejection or exception for a parcel with no eligible route. | Parcel sender | `Capability:E06.03` | PARCEL |

**E06.04 — Delivery Reliability & Recovery**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E06-04-01` · App | **Accept Durable Work Parcel** — Durably accept work into Petasos custody before acknowledging, separately from IU authoritative persistence. | Parcel originator | `Facet:Petasos` | PARCEL, RES, NFR |
| `ES-E06-04-02` · App | **Deliver Accepted Work Parcel** — Attempt delivery of a durably accepted parcel to the designated consumer. | Parcel recipient | `Facet:Petasos` | PARCEL, RES |
| `ES-E06-04-03` · App | **Query Parcel Delivery Outcome** — Report acknowledged, rejected, retriable, expired and indeterminate delivery dispositions. | Parcel sender or coordinator | `Facet:Petasos` | PARCEL, RES |
| `ES-E06-04-04` · App | **Request Parcel Redelivery** — Request controlled redelivery subject to retry, duplication and concurrency rules. | Recovery coordinator | `Facet:Petasos` | PARCEL, RES |

**E06.05 — Temporal Execution & Scheduling**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E06-05-01` · App | **Register Scheduled Work** — Register a bounded scheduled action with temporal constraints at the central scheduler. | Authorised scheduler consumer | `Capability:E06.05` | TIME, NFR |
| `ES-E06-05-02` · App | **Cancel Scheduled Work** — Cancel eligible scheduled action without treating cancellation as reversal of prior work. | Scheduling consumer | `Capability:E06.05` | TIME, NFR |
| `ES-E06-05-03` · App | **Query Temporal Work Obligation** — Return deadline, recurrence and temporal markers for scheduled or active work. | Workflow coordinator | `Capability:E06.05` | TIME, NFR |
| `ES-E06-05-04` · App | **Resolve Governed Calendar Entry** — Return authoritative calendar interpretation for an identified scheduling context. | Scheduling consumer | `Capability:E06.05` | TIME |

**E06.06 — Work Context & Checkpoint Preservation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E06-06-01` · App | **Load Governed Work Context** — Construct the bounded anticipated Pragmaton information for an Ergon without reassigning persistence authority. | Ergon | `Facet:Mneme` | PRAGMATON, MN |
| `ES-E06-06-02` · App | **Acquire Adhoc Work Information** — Authorise and obtain additional needed information beyond initially loaded context. | Ergon | `Facet:Mneme` | PRAGMATON, MN, SEC |
| `ES-E06-06-03` · App | **Return Work Context Changes** — Return modified working state separately from new Pragma outputs; not itself a commit. | Ergon | `Facet:Mneme` | PRAGMA, PRAGMATON |
| `ES-E06-06-04` · App | **Record Recoverable Work Checkpoint** — Preserve an eligible recoverable execution boundary without resuming inside a failed Ergon. | Workflow recovery coordinator | `Facet:Ponos` | PRAGMATON, RES |
| `ES-E06-06-05` · App | **Reconstruct Governed Work Context** — Recreate eligible working context using accepted information and durable transitions. | Replacement work executor | `Facet:Mneme` | PRAGMATON, RES |


#### E07 — Information Presentation & Human Interaction

The system can expose governed information and work context to people in useful, controlled ways.

**E07.01 — Contextual Information Presentation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E07-01-01` · App | **Present Clinical Information Context** — Return an authorised clinical information projection scoped to an active review task. | Clinical viewer | `Facet:Iris` | PRESENT, IU, SEC |
| `ES-E07-01-02` · App | **Present Care Team Context** — Return collaborative care context within the team's authorised scope. | Care team viewer | `Facet:Iris` | PRESENT, SEC |
| `ES-E07-01-03` · App | **Present Participant Support Context** — Provide an eligible participant-goal and support view without assuming clinical treatment context. | Participant-facing viewer | `Capability:E07.01` | PRESENT, JOURNEY |
| `ES-E07-01-04` · App | **Present Service Operations Context** — Show relevant health-service operational state under applicable business authority. | Healthcare operations viewer | `Capability:E07.01` | PRESENT, JOURNEY |
| `ES-E07-01-05` · App | **Present System Administration Context** — Return system operational and governance state available to an authorised administrator. | Systems administrator | `Capability:E07.01` | PRESENT, JOURNEY |

**E07.02 — Governed User Interaction**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E07-02-01` · App | **Submit Governed User Action** — Receive an authorised UI-originated action request with scope, identity and correlation. | Human interaction client | `Facet:Iris` | PRESENT, SEC |
| `ES-E07-02-02` · App | **Check Proposed User Change** — Return specific confirmation/validation findings before applying an intended action. | Human interaction client | `Facet:Iris` | PRESENT, MODEL |
| `ES-E07-02-03` · App | **Confirm Governed User Action** — Record an explicit user confirmation distinctly from authoritative state commitment. | Human interaction client | `Facet:Iris` | PRESENT, SEC |
| `ES-E07-02-04` · App | **Query User Action Disposition** — Return acknowledged, rejected, executed and committed dispositions distinctly. | Human interaction client | `Facet:Iris` | PRESENT, MM |

**E07.03 — Longitudinal Information Viewing**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E07-03-01` · App | **Retrieve Longitudinal Health Record View** — Return eligible longitudinal clinical information organised for healthcare review. | Clinical viewer | `Facet:Iris` | PRESENT, IU |
| `ES-E07-03-02` · App | **Navigate Clinical Timeline** — Retrieve time-ordered health events preserving source and temporal qualifications. | Clinical viewer | `Facet:Iris` | PRESENT, TIME |
| `ES-E07-03-03` · App | **Retrieve Cross-Service Health View** — Assemble an attributed cross-service projection without asserting native information sources have identical certainty. | Clinical viewer | `Facet:Iris` | PRESENT, IU |

**E07.04 — User-Context Continuity**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E07-04-01` · App | **Restore User Work Context** — Restore the permitted UI navigation context without implying authoritative process recovery. | Human interaction client | `Facet:Iris` | PRESENT, PRAGMATON |
| `ES-E07-04-02` · App | **Switch User Work Context** — Change current task/person/service context safely, enforcing fresh authorisation where necessary. | Human interaction client | `Facet:Iris` | PRESENT, SEC |
| `ES-E07-04-03` · App | **Query User Context Currency** — Return whether the displayed work state is current or requires refresh/revalidation. | Human interaction client | `Facet:Iris` | PRESENT, DIST |

**E07.05 — Information Presentation Accountability**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E07-05-01` · App | **Retrieve Presentation Source Attribution** — Return underlying source, version and origin details supporting a displayed fact. | Human interaction client | `Facet:Iris` | PRESENT, IU |
| `ES-E07-05-02` · App | **Check Display Currency** — Return age, temporal consistency and qualified freshness of displayed content. | Human interaction client | `Facet:Iris` | PRESENT, TIME |
| `ES-E07-05-03` · App | **Explain Presentation Restriction** — Return a safe description of partial, hidden or restricted data without leaking protected content. | Human interaction client | `Facet:Iris` | PRESENT, SEC |
| `ES-E07-05-04` · App | **Report Incomplete Clinical Context** — Report known display incompleteness as a distinct user-visible state. | Clinical viewer | `Facet:Iris` | PRESENT, RES |


#### E08 — Operational Observability, Assurance & Resilience

The system can reveal its operational state, preserve evidence, support controlled response and recover service.

**E08.01 — Operational Observability**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E08-01-01` · App | **Query Component Health** — Return monitored component state and relevant dependency readiness. | Systems operations | `Capability:E08.01` | OPS, NFR |
| `ES-E08-01-02` · App | **Query Service Performance** — Return bounded service latency, throughput and capacity observations. | Systems operations | `Capability:E08.01` | OPS, NFR |
| `ES-E08-01-03` · App | **Query Dependency Health** — Return readiness and fault state of required upstream/downstream dependencies. | Systems operations | `Capability:E08.01` | OPS, NFR |

**E08.02 — Operational Logging & Evidence Collection**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E08-02-01` · Tech | **Accept Log Event** — Acknowledge Log-Server receipt of a transmitted LogEvent without asserting durable Log-Persistence. | In-JVM Log-Satellite | `Capability:E08.02` | LOG |
| `ES-E08-02-02` · Tech | **Forward Buffered Log Events** — Deliver eligible buffered events using selected logging transport and backpressure handling. | Log-Satellite | `Capability:E08.02` | LOG |
| `ES-E08-02-03` · Tech | **Retrieve Operational Log Records** — Return authorised persisted logging records by bounded selection without masquerading as audit records. | Systems operations | `Capability:E08.02` | LOG |
| `ES-E08-02-04` · Tech | **Export Operational Log Records** — Provide bounded raw log record export under authorised disclosure rules. | Operational investigator | `Capability:E08.02` | LOG |
| `ES-E08-02-05` · Tech | **Query Log Delivery State** — Report satellite/server acceptance state including known buffering or loss qualification. | Systems operations | `Capability:E08.02` | LOG |

**E08.03 — Audit & Provenance Reporting**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E08-03-01` · App | **Record Governed Audit Event** — Accept an immutable AuditEvent with required correlation and locally attributed activity context. | Audited subsystem | `Facet:Kleio` | AUD, MN, NFR |
| `ES-E08-03-02` · App | **Retrieve Audit Event** — Return an eligible immutable AuditEvent without creating another AuditEvent for the read itself. | Authorised auditor | `Facet:Kleio` | AUD |
| `ES-E08-03-03` · App | **Export Audit Event Records** — Export selected non-dereferenced AuditEvent records with permitted temporal filters. | Authorised audit investigator | `Facet:Kleio` | AUD |
| `ES-E08-03-04` · App | **Export Dereferenced Audit Evidence** — Produce authorised all-or-nothing assembled AuditEvent evidence with optional recursive evidence resolution. | Privileged auditor | `Facet:Kleio` | AUD, PROV |
| `ES-E08-03-05` · App | **Record Governed Provenance** — Accept immutable ordered Provenance evidence with correct temporal semantics and source attribution. | Provenance contributor | `Facet:Dokimasia` | PROV |
| `ES-E08-03-06` · App | **Retrieve Provenance Record** — Return eligible provenance evidence and ordered references without unwarranted dereferencing. | Authorised provenance viewer | `Facet:Dokimasia` | PROV |
| `ES-E08-03-07` · App | **Export Dereferenced Provenance Evidence** — Prepare authorised all-or-nothing provenance assembly and evidence dereferencing. | Privileged assurance investigator | `Facet:Dokimasia` | PROV, AUD |

**E08.04 — Conformance & Assurance Support**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E08-04-01` · App | **Query Release Conformance Evidence** — Return test/harness evidence associated with a governed release and its qualifications. | Architectural governance | `Facet:Dokimasia` | ASSURE, NFR |
| `ES-E08-04-02` · App | **Report Assurance Observation** — Record an observation or advisory recommendation without assuming execution or decision authority. | Assurance agent | `Facet:Dokimasia` | ASSURE |
| `ES-E08-04-03` · App | **Query Assurance Exception** — Return unresolved assurance findings and the evidence relevant to their disposition. | Governance or systems operations | `Facet:Dokimasia` | ASSURE |
| `ES-E08-04-04` · App | **Verify Architecture Conformance Scope** — Report whether required architectural contracts and test evidence are present for the declared scope. | Architecture review | `Facet:Dokimasia` | ASSURE, NFR |

**E08.05 — Operational Fault Management**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E08-05-01` · App | **Report Operational Fault** — Record an operational fault with safe correlation and handling classification. | Subsystem health emitter | `Capability:E08.05` | OPS, NFR |
| `ES-E08-05-02` · App | **Query Incident State** — Return operational incident state and impact without conflating it with clinical incident records. | Systems operations | `Capability:E08.05` | OPS, NFR |
| `ES-E08-05-03` · App | **Notify Service Degradation** — Send a bounded notification of impacted service availability or capacity. | Service operator | `Capability:E08.05` | OPS |

**E08.06 — Service Continuity & Recovery**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E08-06-01` · App | **Request Service Recovery** — Initiate an authorised recovery action without claiming completion or information acceptance. | Systems operations | `Capability:E08.06` | RES, NFR |
| `ES-E08-06-02` · App | **Query Recovery Verification** — Report whether governed state, accepted work and dependent services satisfy declared recovery conditions. | Systems operations | `Capability:E08.06` | RES, NFR |
| `ES-E08-06-03` · App | **Check Accepted Work Preservation** — Demonstrate whether durably accepted work and its recoverable transitions remain intact. | Recovery assurance | `Capability:E08.06` | RES, NFR |
| `ES-E08-06-04` · App | **Request Controlled Shutdown** — Coordinate controlled drain, checkpoint and shutdown while retaining accepted work obligations. | Systems operations | `Capability:E08.06` | RES, NFR |


#### E09 — Information Insight & Evidence Use

The system can organise information for queries, analysis and decision-supporting evidence without acquiring the decision authority itself.

**E09.01 — Information Set & Result Organisation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E09-01-01` · App | **Store Version-Referenced Result Set** — Persist a governed result-set reference collection in the appropriate Ergon context. | Ergon | `Facet:Ergon` | RESULT, PRAGMATON |
| `ES-E09-01-02` · App | **Retrieve Version-Referenced Result Set** — Return the same version-specific reference collection and its governing context. | Ergon or authorised investigator | `Facet:Ergon` | RESULT |
| `ES-E09-01-03` · App | **Query Result Set Provenance** — Explain selection basis, source versions and temporal context of a retained result set. | Authorised investigator | `Facet:Ergon` | RESULT, PROV |

**E09.02 — Information Aggregation & Analysis**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E09-02-01` · App | **Aggregate Governed Information** — Produce an authorised summary from eligible information references with explicit selection and interpretation context. | Authorised analysis consumer | `Capability:E09.02` | INSIGHT, SEC |
| `ES-E09-02-02` · App | **Execute Governed Analysis** — Return analytical findings and their data/model/version basis without automatic operational authority. | Authorised analysis consumer | `Capability:E09.02` | INSIGHT, SEC |
| `ES-E09-02-03` · App | **Query Analysis Reproducibility Context** — Return inputs, versions, rules and qualifications needed to re-evaluate an analytical result. | Authorised investigator | `Capability:E09.02` | INSIGHT, PROV |

**E09.03 — Operational & Business Information Reporting**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E09-03-01` · App | **Generate Operational Report** — Produce a scoped operations report from authorised operational evidence. | Systems operations or executive | `Capability:E09.03` | INSIGHT, OPS |
| `ES-E09-03-02` · App | **Generate Business Information Report** — Produce a business information report from authorised information and approved measure definitions. | Healthcare management | `Capability:E09.03` | INSIGHT, JOURNEY |
| `ES-E09-03-03` · App | **Export Governed Information Selection** — Export a bounded eligible information set without implying unrestricted disclosure rights. | Authorised data recipient | `Capability:E09.03` | INSIGHT, SEC |

**E09.04 — Evidence & Insight Presentation**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E09-04-01` · App | **Present Evidence-Linked Finding** — Return interpretable finding with visible evidence references, qualification and appropriate source context. | Authorised decision supporter | `Capability:E09.04` | INSIGHT, PROV |
| `ES-E09-04-02` · App | **Query Trend Comparison** — Return a time- or cohort-bounded comparative interpretation, with explicit selection and uncertainty. | Authorised analysis consumer | `Capability:E09.04` | INSIGHT |

**E09.05 — Governed Analytical Access**

| Review ref / Type | Proposed Service / independently testable promise | Consumer | Proposed single home | Sources |
|---|---|---|---|---|
| `ES-E09-05-01` · App | **Check Analytical Purpose Authorisation** — Determine whether a stated analytical use is permitted for the requested information selection. | Local PEP or analytical boundary | `Facet:Themis` | TH, INSIGHT |
| `ES-E09-05-02` · App | **Release Governed Analytical Result** — Disclose an authorised analysis result with retained audit and provenance attribution. | Authorised analysis consumer | `Capability:E09.05` | INSIGHT, SEC |
| `ES-E09-05-03` · App | **Flag Advisory-Only Finding** — Mark analytical recommendations as non-authoritative, without task execution rights in R1/R2. | Assurance or AI insight consumer | `Facet:Dokimasia` | ASSURE, INSIGHT |


### 6. Existing Continuum Service protection register

The full **147-element ID/name/standing/source-path register** is in `Harmonia_Continuum_Services_1.1.20_Candidate_010.csv`, and in the structured JSON package. **Every** listed ID remains protected from silent replacement. Source standing is preserved verbatim; a provisional title-derived review area is only a navigational hint.

Existing Business Services are *not* obsolete merely because their former BEC hierarchy is being consolidated. Their contracts might serve new Business CT3/Features, remain shared, be specialised, or require adjudication. Identity and provenance must survive whichever treatment is accepted.

Selected existing source examples, illustrating fine-grained intent rather than universal target mappings:
- **[Order Cancellation Service](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/1c8467ef0b6047329bdb899a52c23efc.md)** — `BusinessService`, `1c8467ef0b6047329bdb899a52c23efc`. **Preserve**; Business capability placement and target correspondence require full semantic review.
- **[Diagnostic Publication Service](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/600ca5384b4643b0b35579a02c307dcf.md)** — `BusinessService`, `600ca5384b4643b0b35579a02c307dcf`. **Preserve**; Business capability placement and target correspondence require full semantic review.
- **[Terminology Concept Resolution Service](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/3be22abcc49045c7a609bdf6428fe15a.md)** — `BusinessService`, `3be22abcc49045c7a609bdf6428fe15a`. **Preserve**; Business capability placement and target correspondence require full semantic review.
- **[Service Eligibility Evaluation](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/7e005ce374f347088ae854442fc3edef.md)** — `BusinessService`, `7e005ce374f347088ae854442fc3edef`. **Preserve**; Business capability placement and target correspondence require full semantic review.
- **[Clinical Privilege Verification](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/f6599b4283194577a977bfc86ea854c2.md)** — `BusinessService`, `f6599b4283194577a977bfc86ea854c2`. **Preserve**; Business capability placement and target correspondence require full semantic review.
- **[Longitudinal Clinical Record Query](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/df10431b93bf47dfae2b8ec7b3e6552b.md)** — `BusinessService`, `df10431b93bf47dfae2b8ec7b3e6552b`. **Preserve**; Business capability placement and target correspondence require full semantic review.
- **[Consent Decision Service](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/e1bebefbdbef4a1981ec31341eb71dfd.md)** — `BusinessService`, `e1bebefbdbef4a1981ec31341eb71dfd`. **Preserve**; Business capability placement and target correspondence require full semantic review.
- **[Clinical Information Search Service](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/business/business-service/ef87ad2b419443a9989849bf18dfb385.md)** — `BusinessService`, `ef87ad2b419443a9989849bf18dfb385`. **Preserve**; Business capability placement and target correspondence require full semantic review.

### 7. DRP design-evidence register

| Key | Source | Design purpose (not literal Service identity) |
|---|---|---|
| `IU` | [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) | Information Unit primacy, identity, metadata, versioning, disclosure and custody |
| `REF` | [DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md) | Topic and InformationCoordinates, reference semantics and secure evaluation |
| `MN` | [DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md) | GovernedReader, GovernedWriter, GovernedSearch and interaction semantics |
| `MM` | [DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md) | Governed read/write/search, authoritative persistence boundary and uncertain outcomes |
| `PERSIST` | [DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md) | GovernedPersistenceFramework and independently provided persistence authority |
| `RES` | [DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md) | Authoritative persistence recovery, availability and failure qualification |
| `DIST` | [DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md) | Distributed and partitioned persistence without semantic identity loss |
| `SEC` | [DRP-Mneme-Security.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Security.draft.002.md) | Governed access, contextualisation as disclosure, local authority |
| `TH` | [DRP-Themis-Policy-Passport-Enforcement.draft.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md) | Policy definition, RequiredCriteria, Passport lifecycle and local PEP |
| `PROV` | [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md) | Immutable provenance, chronological evidence, dereferenced export |
| `AUD` | [DRP-Mneme-Audit.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Audit.draft.002.md) | Audit contribution distinct from authoritative AuditEvent ownership |
| `LOG` | [DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md) | In-JVM satellite, rsyslog Log-Server, separate Log-Persistence, receipt ACK semantics |
| `PRAGMA` | [DRP-Mneme-Pragma-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragma-Model.draft.002.md) | Pragma identity, Input/Output and progression separate from parcel state |
| `PRAGMATON` | [DRP-Mneme-Pragmaton-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Model.draft.002.md) | Active work context distinguishable from authoritative durable information |
| `WORK` | [DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md) | Work execution receives and returns context without owning persistence |
| `RES-W` | [DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md) | Work reconstruction; never resume inside a failed Ergon |
| `PARCEL` | [DRP-Interaction-Petasos-to-Mneme.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md) | Durable work acceptance separate from authoritative IU persistence |
| `RESULT` | [DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md) | Adhoc governed information acquisition and result handling |
| `ASSURE` | [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md) | Assurance and evidence boundaries; dereferencing owned by Kleio/Dokimasia |
| `EXCH` | [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) | Exchange use of governed native and mapped information representations |
| `MODEL` | [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) | Interpretation and model-independent IU semantics; Calliope separation |
| `OPS` | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md) | Operations, observability and dependency health constraints |
| `NFR` | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md) | NFR-derived Motivation obligations and quality constraints |
| `TIME` | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md) | Temporal, scheduling and time-coordinate obligations |
| `PRESENT` | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md) | User experience, source, currency and presentation constraints |
| `INSIGHT` | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived Architetural Elements - Iteration 001.md) | Governed information, reporting and assurance constraints |
| `JOURNEY` | Candidate 003 | Candidate 003 Stakeholder and Journey demand (candidate; not an accepted DRP) |
| `TASK` | [DRP-Mneme-Pragma-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragma-Model.draft.002.md) | Task envelope and work semantics, without a complete task service specification |

#### Distinct DRPs and lifecycle qualification

- [DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Interaction-Petasos-to-Mneme.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mneme-Audit.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Audit.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mneme-Pragma-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragma-Model.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mneme-Pragmaton-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Model.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mneme-Security.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Security.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Themis-Policy-Passport-Enforcement.draft.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.
- [DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md) — authoritative *thinking* for this analysis; incorporation/open decisions retained.

**Important:** The current `DRP-INDEX.md` lists an earlier 13-item programme and does not by itself describe every later draft. Source discovery used the actual `offline-design` directory, not the index as an exhaustive catalogue.

### 8. Cross-cutting architectural obligations and integrity assertions

| Ref | Obligation retained or inferred from governed source/DRP | Candidate E domains | Integrity proof demanded |
|---|---|---|---|
| `OB-001` | InformationUnit has stable governed identity, version and metadata semantics independent of provider IDs | E01, E02, E04 | Identify and fetch specified historical version after provider change |
| `OB-002` | Authoritative durable acceptance distinct from active/cache/provider acknowledgement | E01, E02, E06 | Successful ProcessSomething followed by uncertain Commit returns uncertainty, not success |
| `OB-003` | Mneme governed read, write and search facade does not leak Mnemosyne provider semantics | E01, E02 | No subsystem uses provider-native persistence as its contract |
| `OB-004` | Search distinct from CRUD, result set is IU Version references and context is preservable | E02, E09 | Same resolved result set references exact versions across work recovery |
| `OB-005` | Mnemosyne is framework plus one or more separately configured PersistenceServices | E01, E02 | Different provider instances retain same information governance semantics |
| `OB-006` | Large binary payloads pass by reference and do not enter general PostgreSQL persistence | E01, E02 | Large content is resolved by eligible reference, not embedded in general IU provider |
| `OB-007` | Partitioning/sharding must not change governed identity, labels, lineage or outcome | E01, E02 | Cross-partition read/write retains governance despite rebalance |
| `OB-008` | Pragma, Pragmaton, IU and Petasos custody are independent states and obligations | E01, E06 | Parcel acknowledgement does not imply IU acceptance or task completion |
| `OB-009` | Work recovery reconstructs context; cannot resume inside a failed Ergon | E06, E08 | After interruption recreate unit of work at a recoverable boundary |
| `OB-010` | Local PEP enforces RequiredCriteria against current Passport and fails closed | E05 | Revocation during a Journey prohibits stale authority from granting access |
| `OB-011` | Passport propagates contextual authorisation snapshot, never credentials | E05, E06 | Forwarded work receives scoped security context but no user secret |
| `OB-012` | External exchange contract and authoritative IU acceptance remain distinct | E03, E01 | Protocol receipt ACK is not interpreted as governed commit |
| `OB-013` | Calliope governs released models, valuesets, semantic translation and regression | E04 | Translation is attributable to exact mapping/model and its qualifications |
| `OB-014` | Kleio AuditEvent and Dokimasia Provenance remain immutable and distinct from logs | E08 | Reading an AuditEvent does not create an AuditEvent; access tracked separately |
| `OB-015` | Dereferenced forensic exports require full authority and all-or-nothing release integrity | E08 | Unauthorised evidence prevents release rather than silently omitting |
| `OB-016` | Operational logging uses in-JVM satellite, rsyslog server and separate log persistence | E08 | Server receipt ACK is not durable-persistence ACK; loss window observable |
| `OB-017` | Non-dereref raw record export distinct from domain-specific Kleio/Dokimasia deref export | E08, E09 | Raw export keeps references; deref export assembles authorised evidence |
| `OB-018` | Accepted work durable before acknowledgement, controlled restart preserves it | E06, E08 | Restart after ACK preserves accepted obligations |
| `OB-019` | Observability includes component and dependency health, bounded OA&M disclosure | E08 | Failure of required dependency surfaces as explicit service degraded state |
| `OB-020` | Stakeholder Journey stages remain governed value increments, not software workflows | E02, E06, E07, E09 | Journey integrity fails with missing service/capability trace; journey not rewritten |
| `OB-021` | Retrieve, Check, Update, ProcessSomething and Commit distinguish five service intentions | E01, E02, E04, E06 | Independent processing result and authoritative acceptance verified |
| `OB-022` | AI assurance in R1/R2 observes and recommends, never acts authoritatively | E08, E09 | Advisory insight does not commit or authorise a task |
| `OB-023` | Retention/RETIRE, archival, recovery and immutable preservation maintain semantic distinctions | E01 | RETIRE request cannot mutate an immutable historical record |
| `OB-024` | System services may be deliberately out of project solution scope | E07, E09 | External responsibilities identified without claiming Harmonia implements them |
| `OB-025` | One governed home per Service, Function and Process; numerous references permitted | E01, E02, E03, E04, E05, E06, E07, E08, E09 | Each target Service has exactly one proposed home; final home review pending |
| `OB-026` | Every existing Continuum Service identity retained pending semantic equivalence determination | E01, E02, E03, E04, E05, E06, E07, E08, E09 | 147 baseline identities accounted for without name-only merging |

### 9. Stakeholders and reusable Journeys — complete scope

**All 13 primary perspectives (Candidate 003):**

| Perspective | Description/context |
|---|---|
| `S01` Patient | Emergency patient, person receiving ongoing clinical care, surgical patient |
| `S02` Practitioner (including specialists) | GP, intensivist, surgeon, visiting specialist |
| `S03` Allied Health | Occupational therapist, physiotherapist, speech pathologist, psychologist, social worker where relevant |
| `S04` Nurse & Support | Registered/enrolled nurse, midwife, nurse practitioner, care assistant |
| `S05` Diagnostics People | Pathology scientist, diagnostic technician, sonographer, radiologist |
| `S06` Operations — healthcare | Ward operations, health-service manager, clinic operations, deployment support |
| `S07` Administrators — healthcare | Scheduling, referrals, admissions, medical records, service billing |
| `S08` Executive | Portfolio executive, CEO, governing body, Defence command |
| `S09` Regulatory / Government | Public-health authority, accreditation body, policy authority, pharmaceutical regulator; potential commissioning context |
| `S10` Researchers | Clinical trial team, data custodian in research context, evidence synthesis specialist |
| `S11` Operations — systems | ICT operations, reliability, incident management, service recovery |
| `S12` Administrators — systems | Access administration, controlled configuration, semantic-release administration |
| `S13` Participant | Disability support participant, aged-care resident, wellbeing programme participant, community support recipient |

`ResponsiblePerson` remains a **probe**, not an authorised fourteenth primary Stakeholder. Patient and Participant remain distinct; no underlying Person/persona/role ontology is introduced.

**All 11 reusable Journeys are first-class specialised Value Streams:**

| Reusable Journey | Value stages |
|---|---|
| `RJ01` Access & Participate in Care | Need or intent → find/access → engage → understand/participate → continue |
| `RJ02` Determine & Direct Clinical Care | Establish concern/context → assess → decide/plan → direct care → evaluate/handover |
| `RJ03` Deliver & Observe Care | Accept care context → provide care/support → observe → respond/escalate → continue |
| `RJ04` Produce Diagnostic Evidence | Qualify request → investigate → check validity → report → close loop |
| `RJ05` Provide a Health Service | Establish service demand → organise access/capacity → provide → assess → adapt |
| `RJ06` Steward & Assure | Understand obligations → set direction → authorise/commit → evaluate → respond/improve |
| `RJ07` Produce & Apply Knowledge | Define purpose → establish authority → investigate → verify → disseminate/apply |
| `RJ08` Operate an Information Service | Establish readiness → provide/observe → manage incident → recover → improve |
| `RJ09` Administer Governed Change | Establish authority → specify change → assess → apply → verify → revise/revoke |
| `RJ10` Pursue Participation & Support | Express aspirations/needs → establish choices → arrange support → participate → evaluate/adapt |
| `RJ11` Commission & Fund Health Services | Identify population/service need → select/fund services → establish arrangements → account for activity/outcomes → review investment |

### 10. All 29 portfolio Journeys and initial integrity probes

The original candidate value-stage sequences and stakeholder participation are retained. B/E domain pointers and negative-test probes are **review hypotheses**, not approved ArchiMate relationships. Each Journey requires subsequent **stage-by-stage** contract and evidence assessment.

#### J01 — Maintain Health & Wellbeing

- **Stakeholder(s):** Patient
- **Value stages:** Understand health → identify risk/need → adopt protective action → access support → review wellbeing
- **Intended value:** Informed and supported ongoing health; RJ01, RJ07 feedback
- **Business / Enterprise candidate review areas:** B01, B13 / E02, E07, E09
- **Adverse integrity probe:** Unexpected service advice or stale guidance
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J02 — Seek, Receive & Continue Care

- **Stakeholder(s):** Patient
- **Value stages:** Recognise need → find/access care → establish care context → receive assessment/plan → participate in intervention → understand outcome → continue/transition
- **Intended value:** Care accessed and continued with informed participation; RJ01 + contributions from RJ02/3
- **Business / Enterprise candidate review areas:** B01, B02, B03, B05 / E02, E03, E06, E07
- **Adverse integrity probe:** Care transition loses source or responsibility
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J03 — Represent & Support a Patient

- **Stakeholder(s):** Representing/supporting stakeholder
- **Value stages:** Establish relationship/authority → understand need/preferences → help access → support decisions/communication → coordinate follow-up
- **Intended value:** Patient supported appropriately within representative's authority; shares RJ01, not all Patient stages
- **Business / Enterprise candidate review areas:** B01, B10 / E02, E05, E07
- **Adverse integrity probe:** Representative authority invalid or expires mid-journey
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J04 — Evaluate & Direct Clinical Care

- **Stakeholder(s):** Practitioner
- **Value stages:** Accept concern → obtain context → assess/determine → decide/plan → direct/provide care → evaluate → communicate/transition
- **Intended value:** Appropriate clinical decisions and continuity; RJ02
- **Business / Enterprise candidate review areas:** B02, B05 / E01, E02, E04, E07
- **Adverse integrity probe:** Clinical decision based on an unqualified historical version
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J05 — Manage Complex / Critical Clinical Care

- **Stakeholder(s):** Specialist practitioner
- **Value stages:** Recognise deterioration/complexity → synthesise evidence → prioritise decision → coordinate intervention → monitor response → revise plan → transition
- **Intended value:** Specialised clinical decision/response; RJ02 + RJ03 with specialty constraints
- **Business / Enterprise candidate review areas:** B02, B03, B05 / E02, E06, E07, E08
- **Adverse integrity probe:** Critical deterioration during node failure
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J06 — Restore Function & Participation

- **Stakeholder(s):** Allied Health
- **Value stages:** Understand functional need → assess capacity/environment → agree goals → provide interventions/support → evaluate progress → enable ongoing participation
- **Intended value:** Functional and psychosocial improvement; shared RJ02/3 elements
- **Business / Enterprise candidate review areas:** B06, B14 / E02, E07, E09
- **Adverse integrity probe:** Support outcome is mistaken for a medical treatment order
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J07 — Provide Continuous Nursing & Support

- **Stakeholder(s):** Nurse and Support
- **Value stages:** Accept responsibility → establish care needs → organise care → deliver/assist → monitor and escalate → hand over
- **Intended value:** Safe, continuous direct care; RJ03
- **Business / Enterprise candidate review areas:** B03, B05 / E02, E06, E07
- **Adverse integrity probe:** Handover accepted while outstanding obligation is lost
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J08 — Investigate & Report Clinical Evidence

- **Stakeholder(s):** Diagnostics People
- **Value stages:** Receive/qualify request → prepare subject/materials → acquire evidence → analyse/verify → release findings → escalate critical results → reconcile completion
- **Intended value:** Reliable, timely diagnostic evidence; RJ04
- **Business / Enterprise candidate review areas:** B04, B05 / E02, E03, E04, E06
- **Adverse integrity probe:** Critical finding received but not communicated and committed
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J09 — Deliver Reliable Healthcare Services

- **Stakeholder(s):** Healthcare Operations
- **Value stages:** Understand demand → determine service capacity → deploy workforce/resources → coordinate delivery → manage exceptions → review results → adapt
- **Intended value:** Required service available and delivered; RJ05
- **Business / Enterprise candidate review areas:** B07, B08, B09 / E06, E08, E09
- **Adverse integrity probe:** Resource reported available though required dependency is down
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J10 — Maintain Healthcare Readiness

- **Stakeholder(s):** Healthcare Operations
- **Value stages:** Establish required capability → evaluate readiness → identify shortfalls → mobilise resources → test readiness → sustain/restore
- **Intended value:** Clinically and operationally ready service; RJ05/RJ06
- **Business / Enterprise candidate review areas:** B07, B09, B12 / E06, E08, E09
- **Adverse integrity probe:** Declared readiness lacks recovered-state evidence
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J11 — Arrange & Administer Healthcare Access

- **Stakeholder(s):** Healthcare Administrators
- **Value stages:** Receive request → establish person/service context → validate authority/eligibility → book/route → communicate arrangements → record attendance/outcome → complete administration
- **Intended value:** Access coordinated and administration accurate; RJ01/RJ05
- **Business / Enterprise candidate review areas:** B01, B07 / E02, E06, E07
- **Adverse integrity probe:** Appointment notification precedes actual booking acceptance
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J12 — Administer Healthcare Records & Obligations

- **Stakeholder(s):** Healthcare Administrators
- **Value stages:** Receive information/obligation → verify scope/authority → register/update → make appropriate access available → resolve discrepancy → retain/disclose/close
- **Intended value:** Reliable business stewardship of records and entitlements; RJ05/RJ06
- **Business / Enterprise candidate review areas:** B10 / E01, E02, E05, E08
- **Adverse integrity probe:** Disclosure is authorised at request but not at retrieval
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J13 — Direct & Govern the Enterprise

- **Stakeholder(s):** Executive
- **Value stages:** Understand position → determine strategic outcomes → decide priorities → allocate authority/resources → monitor service and risk → intervene → evaluate direction
- **Intended value:** Enterprise achieves sustainable outcomes; RJ06
- **Business / Enterprise candidate review areas:** B12 / E08, E09
- **Adverse integrity probe:** Executive summary cannot be traced to authoritative evidence
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J14 — Regulate & Oversee Healthcare

- **Stakeholder(s):** Regulatory / Government
- **Value stages:** Identify public interest/obligation → establish policy/standard → obtain evidence → assess conformance and outcomes → intervene/authorise → learn/revise
- **Intended value:** Accountable, safe and equitable health system; RJ06
- **Business / Enterprise candidate review areas:** B11, B12 / E05, E08, E09
- **Adverse integrity probe:** Audit export dereferences evidence the requester cannot access
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J15 — Protect & Improve Population Health

- **Stakeholder(s):** Regulatory / Government
- **Value stages:** Observe population → identify threat/inequity → determine response → coordinate intervention → monitor impact → adjust public action
- **Intended value:** Population health protected and improved; RJ06/RJ07
- **Business / Enterprise candidate review areas:** B13 / E03, E07, E09
- **Adverse integrity probe:** Time-limited surveillance input omitted without qualification
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J16 — Produce Trustworthy Health Research

- **Stakeholder(s):** Researchers
- **Value stages:** Define question → obtain ethics/authority → design method → obtain appropriate data/evidence → analyse/verify → report/share → translate into practice
- **Intended value:** Valid, ethically produced knowledge; RJ07
- **Business / Enterprise candidate review areas:** B10, B13 / E02, E05, E09
- **Adverse integrity probe:** Research purpose changes after data access granted
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J17 — Translate Evidence into Practice

- **Stakeholder(s):** Researchers + practitioners
- **Value stages:** Appraise findings → assess applicability → agree change → update practice guidance → apply → measure effect → refine
- **Intended value:** Knowledge changes practice appropriately; RJ07 + RJ02
- **Business / Enterprise candidate review areas:** B08, B13 / E04, E07, E09
- **Adverse integrity probe:** Updated guidance published without model-version governance
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J18 — Operate & Restore Information Services

- **Stakeholder(s):** Systems Operations
- **Value stages:** Establish service readiness → operate → monitor → investigate degradation → recover/restore → verify integrity/availability → learn/improve
- **Intended value:** Reliable, recoverable Information Systems services; RJ08
- **Business / Enterprise candidate review areas:** B12 / E01, E06, E08
- **Adverse integrity probe:** Processing succeeds but commit outcome is uncertain during failure
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J19 — Sustain Information Services

- **Stakeholder(s):** Systems Operations
- **Value stages:** Understand demand/health → plan capacity and continuity → maintain service → exercise recovery → verify service level → improve
- **Intended value:** Sustainable information-service delivery; RJ08
- **Business / Enterprise candidate review areas:** B12 / E06, E08, E09
- **Adverse integrity probe:** Planned restart loses accepted work
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J20 — Govern Systems Access & Configuration

- **Stakeholder(s):** Systems Administrators
- **Value stages:** Establish authorised request → determine scope → provision/configure → validate → monitor/review → amend/revoke → preserve evidence
- **Intended value:** Controlled and accountable access/configuration; RJ09
- **Business / Enterprise candidate review areas:** B10, B12 / E05, E08
- **Adverse integrity probe:** Revoked Passport still used to authorise a change
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J21 — Govern Models, Releases & Information Changes

- **Stakeholder(s):** Systems Administrators
- **Value stages:** Identify controlled change → assess compatibility/impact → authorise release → publish/deploy → verify consumers → monitor/recover
- **Intended value:** Governed information-system evolution; RJ09
- **Business / Enterprise candidate review areas:** B10, B12 / E04, E08
- **Adverse integrity probe:** Model release breaks a dependent consumer without recorded qualification
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J22 — Pursue Wellbeing & Chosen Life Goals

- **Stakeholder(s):** S13 Participant
- **Value stages:** Express aspirations → explore choices → agree desired support → participate in activities → review outcomes → adapt preferences
- **Intended value:** Self-directed wellbeing and participation; RJ10
- **Business / Enterprise candidate review areas:** B14 / E02, E07
- **Adverse integrity probe:** Participant goals inferred as clinical problems
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J23 — Obtain & Sustain Everyday Supports

- **Stakeholder(s):** S13 Participant + Healthcare Administrators/Operations where appropriate
- **Value stages:** Understand support need → choose/arrange service → obtain support → assess fit/continuity → adjust/renew/finish
- **Intended value:** Support is available and appropriate without assuming treatment; RJ10/RJ05
- **Business / Enterprise candidate review areas:** B07, B14 / E06, E07
- **Adverse integrity probe:** Support provider handover leaves unmet obligation
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J24 — Maintain Independence & Community Participation

- **Stakeholder(s):** S13 Participant, Allied Health where appropriate
- **Value stages:** Establish desired participation → understand barriers → arrange aids/assistance → take part → evaluate independence/outcomes → adjust
- **Intended value:** Agency, access, independence and inclusion; RJ10
- **Business / Enterprise candidate review areas:** B06, B14 / E02, E07
- **Adverse integrity probe:** Assistive support unavailable or inconsistent with chosen participation
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J25 — Act for Another Within a Recognised Responsibility

- **Stakeholder(s):** Provisional ResponsiblePerson cross-context perspective
- **Value stages:** Establish scope of responsibility → understand wishes/obligations → arrange or contribute → communicate decisions/actions → review/hand over
- **Intended value:** Supported person or enterprise is properly represented/cared for; shared RJ01/RJ06 parts; no parent-role ontology
- **Business / Enterprise candidate review areas:** B01, B14 / E05, E07
- **Adverse integrity probe:** ResponsiblePerson lacks authority for requested scope
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J26 — Fund & Arrange Eligible Care or Support

- **Stakeholder(s):** Payer/insurer/funder — specialisation/participating perspective TBD
- **Value stages:** Identify entitlement or service need → assess coverage/conditions → authorise or arrange payment → reconcile provision/claims → review fairness/outcome
- **Intended value:** Funding supports eligible access; RJ11
- **Business / Enterprise candidate review areas:** B01, B15 / E05, E09
- **Adverse integrity probe:** Coverage decision based on obsolete entitlement
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J27 — Commission & Review a Provider Network

- **Stakeholder(s):** Commissioner/purchaser — specialisation/participating perspective TBD
- **Value stages:** Assess population need → specify intended outcomes → select/contract providers → monitor service performance → renew/change arrangements
- **Intended value:** Accessible, accountable service supply; RJ11/RJ05
- **Business / Enterprise candidate review areas:** B07, B15 / E08, E09
- **Adverse integrity probe:** Provider contract outcome cannot be traced to evidence
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J28 — Educate & Maintain Professional Practice

- **Stakeholder(s):** Educator/trainer and Practitioner/Allied/Nursing perspectives
- **Value stages:** Identify learning need → design teaching/supervision → provide learning → assess competence → maintain credentials → renew
- **Intended value:** Safe and competent practice; RJ07/RJ06
- **Business / Enterprise candidate review areas:** B08 / E04, E09
- **Adverse integrity probe:** Competence assessment is mistaken for clinical privilege approval
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

#### J29 — Oversee Therapeutic Goods and Public Protection

- **Stakeholder(s):** S09 Regulatory/Government, with provider/diagnostics contribution
- **Value stages:** Set standards → assess product/service evidence → authorise/monitor → identify risk → issue intervention → review protection
- **Intended value:** Regulated health products and associated activity are appropriately controlled; RJ06
- **Business / Enterprise candidate review areas:** B11, B13 / E03, E08, E09
- **Adverse integrity probe:** Product safety finding not connected to a corrective action
- **Acceptance principle:** show the *separate* execution, resulting information and governed commitment dispositions if this Journey involves their combination.

### 11. The five-intention integrity reference scenario

**Retrieve → Check → Update → ProcessSomething → Commit**

| Intention | Contract interpretation | Independent failure/uncertainty test |
|---|---|---|
| Retrieve | Read specified governed information version and source/currency context | Missing, denied and unavailable are distinct; no default substitution of latest |
| Check | Assess selected information or proposed operation against named rules/version | Uncertain or incomplete check cannot imply approval |
| Update | Record intended working-state changes and their lineage | A successful local update does not mean a task ran or durable authoritative state changed |
| ProcessSomething | Execute a *specific* consumer-meaningful operation, recording its outcome | A completed Ergon or clinical action has an outcome even if later Commit fails |
| Commit | Request governed acceptance and recover the authoritative disposition | Transport ACK or persistence-provider ACK is not proof of committed authoritative state |

**Two-state proof:** For a proposed result with a known successful `ProcessSomething` outcome, force failure before `Commit`, during `Commit`, and after provider acknowledgement but before governed outcome confirmation. At every point, verify that execution success, commitment status, authoritative retrievability, event/audit/provenance lineage and safe retry semantics remain individually interpretable. This is a *contract assurance scenario*, not a prescribed distributed transaction design.

### 12. Feature-level demand and why coverage is not yet certified

The `Harmonia_Feature_Trace_Obligations_1.1.20_Candidate_010.csv` register includes **all 1,299 Business Features and all 700 Enterprise Features**, with parent CT1/CT2/CT3 and measurement-family data. Enterprise rows carry the nearby CT2 Service candidate references **as an inspection queue only**. Every row explicitly says `UNVERIFIED`.

**Why:** Service presence is not Feature fulfilment. Some Features impose a measurable NFR or evidence obligation rather than an independently offered Service. Some Business Features rely on organisations, people or external partners, not wholly on Harmonia. The next architectural pass must determine accountable behaviour/constraints and explicit gaps, not manufacture satisfaction.

### 13. Known design tensions, absences and exclusions

1. **Artemis and Praxis:** these exact names were **not** present among the 25 observed native `ApplicationComponent` names at the pinned snapshot. Our agreed routing/workflow meaning survives; establish whether each has another current representation or needs explicit component/facet articulation before creating a home.
2. **Logging topology:** in-JVM Log-Satellite, rsyslog-based Log-Server and Log-Persistence are intended boundaries in DRP evidence. This candidate proposes Technology Service offerings; the transport/fabric and detailed instance topology remain unresolved. Log persistence stays outside Mnemosyne.
3. **Themis:** RequiredCriteria versus Passport, policy definition versus determination and enforcement, Passport currency/revalidation and model identity remain separately governed. The exact Passport/AuthorisationMap relationship is an open design question in the DRP.
3a. **Local PEP home:** E05.03 enforcement-related entries are housed provisionally under the *Capability*, not under a single central Themis endpoint. They are service obligations/contract patterns used in each protected subsystem; Themis supplies reusable definitions and policy support, but enforcement authority remains local.
4. **Mnemosyne:** `GovernedPersistenceFramework + PersistenceService[1..X]`; common implementation does not imply shared instance. PostgreSQL may be used by different problem-domain instances; large media payloads are not a general PostgreSQL storage obligation.
5. **DRP agreement is not blanket resolution:** explicit open decisions on persistence technology-specific recovery, sharding semantics, temporal export selection, identity and evidence ordering remain open.
6. **Service homes unresolved:** services provisionally housed under `Capability:E##.##` lack an established, verified Solution Facet boundary. Do not silently create one. Other home proposals use existing Facet *names*, but that does not yet accept the specific Service ownership.
7. **Business-to-application separation:** no Business Service is automatically rewritten as Application Service, nor vice versa, merely because both expose independently meaningful operations.
8. **Service granularity is not the same as endpoint count:** `ProcessSomething` is a contextual consumer intention; `ArchiMate::Process` remains separate, and no generic `ProcessSomethingService` is introduced.
9. **Incomplete source semantics:** identity/standing for 147 source Services is inventoried; full definitions, relationships, Functions and Processes are not yet completely reconciled. This deliverable is an extensive first target iteration, not an assertion of exhaustive source fidelity.
10. **No final completeness assertion:** all 47 Enterprise CT2s have nearby service scope, but 139 CT3s and 700 Enterprise Features require explicit obligation-by-obligation validation. 258 Business CT3s and 1,299 Features likewise need service/behaviour/external contribution evidence.

### 14. Controlled path to an architecturally complete target

**Follow-on architectural work, *not* Codex implementation instructions:**

1. Review the candidate Service names, independent promises and proposed homes by Enterprise domain. Identify genuinely larger cohesive contracts versus hidden multiple independently consumable operations.
2. Fully read/reconcile all 147 existing Service bodies and their relationships, and the relevant 160 Business Functions, 21 Business Processes, 2 Application Functions, current App interfaces and Solution Facet boundaries. Classify preserve, reposition, refine, explicit semantic correspondence, or unresolved—not name-only replacement.
3. Reconcile all source DRP obligations—including those without current Service candidates—and retained Motivations, NFRs, standards and proof requirements. Respect unresolved and explicit exclusions.
4. Perform two-way Journey tracing: every value-stage question gets capability/Service/behaviour/Facet coverage or a labelled gap; every new Service can point back to a consumer and reason for existence.
5. Complete Feature-level semantic assessment: all 1,999 feature obligations have validated coverage disposition, and all 29 Journeys have scenario/variant integrity tests.
6. Review governance-eligible canonical Home relationships, identity correspondence and incorporation standing. Only after Solution Architect acceptance does an architectural migration package become an executable target.

### 15. Validation and source-integrity report

| Check | Result | Interpretation |
|---|---:|---|
| Existing Business Services | 142 | All directory identities independently checked against the pinned listing (count and FNV checksum) |
| Existing Application Services | 5 | All directory identities independently checked against the pinned listing |
| Existing Application Components | 25 | Native component-name inventory checked |
| Proposed Service candidates | 174 | Unique review reference and name; no UUID allocations |
| Enterprise CT2s with service proposals | 47 | Every CT2 has at least one candidate; feature satisfaction not claimed |
| Business Features carried | 1299 | Every Feature present in demand register; unverified |
| Enterprise Features carried | 700 | Every Feature present in demand register; unverified |
| Stakeholders | 13 | Candidate 003 population preserved |
| Reusable Journeys | 11 | Candidate 003 population preserved |
| Portfolio Journeys | 29 | Candidate 003 population preserved |
| Cross-domain integrity assertions | 26 | Evidence-backed/scoped tests; contract execution pending |

**Authority and provenance boundary:** Source content was inspected from a pinned GitHub repository snapshot; the Service catalogue is the assistant’s architectural proposal. The candidate references are navigation aids only, not canonical IDs; no release is accepted or repository modified. This is the **detailed first Service target** built within the accepted Scope 009, deliberately exposing everything still requiring semantic adjudication.
