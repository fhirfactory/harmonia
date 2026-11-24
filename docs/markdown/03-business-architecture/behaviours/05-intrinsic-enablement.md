<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../../enterprise-architecture/documentation/continuum-manifest/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

# Intrinsic / Shared Enablement — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

The named owning Capabilities and established Features retain their responsibilities. Affected Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing); document grouping and numbering do not establish architectural identity or hierarchy.

Intrinsic and Shared Enablement encompasses the horizontal platform capabilities that provide foundational security, longitudinal clinical record assembly, information exchange, workflow orchestration, collaborative spaces, and semantic governance across all healthcare contexts.

### The Cross-Cutting Boundary Guardrail
> **These capabilities are bounded architectural responsibilities whose services are widely consumed across the HIE. Delivering a shared service (such as security enforcement, message exchange, workflow coordination, or UI presentation) does not grant ownership of the clinical payloads, business activities, or decisions being processed.**

---

## 2. Intrinsic Enablement Capabilities

```text
Intrinsic / Shared Enablement Capabilities
├── 1. Patient Clinical Record
├── 2. Health Information Exchange (HIE)
├── 3. Health Information Access
├── 4. Health Information Communication
├── 5. Health Information Control
├── 6. Clinical Knowledge Services
├── 7. Clinical Collaboration
├── 8. General Collaboration
├── 9. Workflow & Activity Coordination
├── 10. Calendar Management
├── 11. Presentation Services
└── 12. Information Design Governance
```

---

### 2.1 Patient Clinical Record
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Patient Clinical Record](../../../../enterprise-architecture/continuum/strategy/capability/5478878e0e6e4d34a8d5023ac6ac2d54.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Patient Clinical Record`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Governs the canonical, longitudinal, cross-institutional health record for every patient across the healthcare region.
- **Functions & Exposed Services**:
  - **Feature: Longitudinal Health Record (LHR) Assembly**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Assemble Longitudinal Clinical Record](../../../../enterprise-architecture/continuum/business/business-function/65ea2d2427dd474d84f153671de79d63.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Assemble Longitudinal Clinical Record` — Synthesises clinical documents, encounters, diagnostic reports, and medication histories across disparate source systems into a unified chronological clinical view.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Longitudinal Clinical Record Query](../../../../enterprise-architecture/continuum/business/business-service/df10431b93bf47dfae2b8ec7b3e6552b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Longitudinal Clinical Record Query` — Discloses the comprehensive clinical timeline to authorised clinicians.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Active Clinical Record Access**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Active Clinical Record](../../../../enterprise-architecture/continuum/business/business-function/dbccf500cf4a44e0987f972311bab03a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Active Clinical Record` — Maintains active problem lists, current medication regimens, allergies, and open clinical alerts for active care coordination.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Active Problem & Allergy Summary Query](../../../../enterprise-architecture/continuum/business/business-service/a382bc84c1d043849b8cd8ebe8a482c1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Active Problem & Allergy Summary Query` — Exposes immediate active clinical summaries.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Durable Clinical Record Preservation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Preserve Durable Clinical Record](../../../../enterprise-architecture/continuum/business/business-function/ace0f4afad104482b7970534a45d74d7.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Preserve Durable Clinical Record` — Preserves the governed longitudinal representation and durable, immutable, append-only history of what Harmonia received, knew, asserted or managed, with provenance and versioning. Corrections/supersession establish changed knowledge without retrospectively altering that history or acquiring originating clinical authority.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Historical Clinical Record Access](../../../../enterprise-architecture/continuum/business/business-service/be4938981b43403282694631b40f0bb2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Historical Clinical Record Access` — Provides governed historical record retrieval for authorised clinical and legal review; preservation does not itself establish legal qualification or originating authority.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Governed Longitudinal Health Record (LHR), Canonical Clinical Timeline, Active Problem List, Immutable Historical Record Store.

---

### 2.2 Health Information Exchange (HIE)
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Health Information Exchange Enablement](../../../../enterprise-architecture/continuum/strategy/capability/a61a15908ed544e6acf3e9cab63a0267.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Health Information Exchange`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Manages the ingestion, transformation, routing, addressed distribution, and publish-subscribe syndication of health information across organizational and jurisdictional boundaries.
- **Ownership Invariant**: Health Information Exchange owns **exchange transaction state, delivery receipts, and routing policies**. It does **not** acquire ownership of the business information transported.
- **Functions & Exposed Services**:
  - **Feature: Direct Submission Ingestion & Delivery**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Information Submission](../../../../enterprise-architecture/continuum/business/business-function/26abb23ed9f54dcf9002eb894f1d02ec.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Information Submission` — Validates and ingests standards-compliant clinical messages from external health providers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [HIE Submission Gateway Service](../../../../enterprise-architecture/continuum/business/business-service/816c1013b609440fb772060396fd2dac.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `HIE Submission Gateway Service` — Ingress endpoint for regional data contributions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Clinical Record Retrieval Mediation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mediate Information Retrieval](../../../../enterprise-architecture/continuum/business/business-function/7667abb329724b7d894fca020acb8014.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Mediate Information Retrieval` — Federates queries across participating hospital and diagnostic node repositories.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Federated Health Information Query](../../../../enterprise-architecture/continuum/business/business-service/b4db5850f7114c968901966b4f6e36f5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Federated Health Information Query` — Resolves distributed record queries.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Addressed Clinical Distribution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Distribute Addressed Information](../../../../enterprise-architecture/continuum/business/business-function/6c9090a3d0a74a968475e05eb617e84f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Distribute Addressed Information` — Directs point-to-point clinical documents to specific recipient practitioner endpoints.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Addressed Document Delivery Service](../../../../enterprise-architecture/continuum/business/business-service/97080460bacf40ef8b6db2091cc3bb6e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Addressed Document Delivery Service` — Securely delivers documents to designated recipient mailboxes.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Event-Driven Syndication**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Syndicate Information Change](../../../../enterprise-architecture/continuum/business/business-function/86241540124a40458f7d1dc2f5df84ca.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Syndicate Information Change` — Broadcasts event notifications and record updates to subscribed regional care network systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Event Syndication Stream](../../../../enterprise-architecture/continuum/business/business-service/337f39bb62d9491bb6e41c4af32b220c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Event Syndication Stream` — Emits publish-subscribe clinical event feeds.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Exchange Transaction Ledger, Route Resolution Map, Subscription & Syndication Matrix.

---

### 2.3 Health Information Access
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Health Information Access](../../../../enterprise-architecture/continuum/strategy/capability/cc253aea3c5f489da0502df74dde1669.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Health Information Access`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Provides high-performance search, retrieval, and access-qualified filtering of clinical information across the longitudinal record.
- **Ownership Invariant**: Consumes access control and consent determinations from *Health Information Control*. Search behaviour does **not** own security policy.
- **Functions & Exposed Services**:
  - **Feature: Federated Clinical Query**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Search Health Information](../../../../enterprise-architecture/continuum/business/business-function/2e75f678b9814305b1cb2e810f4f0e9e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Search Health Information` — Executes structured semantic searches across diagnostic reports, clinical notes, and medication histories.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Information Search Service](../../../../enterprise-architecture/continuum/business/business-service/ef87ad2b419443a9989849bf18dfb385.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Information Search Service` — Discloses search capabilities to clinical user interfaces.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Retrieve Health Information](../../../../enterprise-architecture/continuum/business/business-function/0afb265973744f6f9e95ee54e6ea5b77.md)
- [Clinical Resource Retrieval Service](../../../../enterprise-architecture/continuum/business/business-service/3cdde3bb6a204c46b08468e080f2545a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: `FEAT-ISE-09 — Access-Controlled Information Filtering` requires access-controlled disclosure/filtering; retrieval of discrete information alone does not materially realise that responsibility. No replacement Feature is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Retrieve Health Information](../../../../enterprise-architecture/continuum/business/business-function/0afb265973744f6f9e95ee54e6ea5b77.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Retrieve Health Information` — Fetches discrete clinical observations and original attachments.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Resource Retrieval Service](../../../../enterprise-architecture/continuum/business/business-service/3cdde3bb6a204c46b08468e080f2545a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Resource Retrieval Service` — Delivers structured clinical information to requesting applications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Apply Access-Qualified Result Filtering](../../../../enterprise-architecture/continuum/business/business-function/598e46ab52644e70809b3039b7a4c21e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Access-Qualified Result Filtering** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Apply Access-Qualified Result Filtering](../../../../enterprise-architecture/continuum/business/business-function/598e46ab52644e70809b3039b7a4c21e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Apply Access-Qualified Result Filtering` — Redacts or masks search results and clinical content based on the caller's verified security context and patient consent directives.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Exposed Service*: *(Embedded within Search and Retrieval Services)*.
- **Information Responsibility**: Query Execution Context, Filtered Result Projection State.

---

### 2.4 Health Information Communication
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Health Information Communication](../../../../enterprise-architecture/continuum/strategy/capability/9794b2aff43443b495a19aefe0efa67b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Health Information Communication`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Encapsulates standards-based health information communication to mediate technical transmission between participating parties.
- **Ownership Invariant**: Does **not** own every business interaction communicated through it; provides protocol adaptation and secure transport mediation.
- **Functions & Exposed Services**:
  - **Feature: Standards-Based Boundary Exchange**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Mediate Standards-Based Health Information Communication](../../../../enterprise-architecture/continuum/business/business-function/6c14589215f744a2b76605a2023b04fb.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Mediate Standards-Based Health Information Communication` — Handles standards-based communication mediation, transaction coordination, and technical error handling.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Standards Communication Gateway Service](../../../../enterprise-architecture/continuum/business/business-service/80e239faf1bd476e973eb602b3730380.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Standards Communication Gateway Service` — Protocol interface for external client integrations.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Transport Protocol Binding Map, Network Session State.

---

### 2.5 Health Information Control
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Health Information Control](../../../../enterprise-architecture/continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Health Information Control`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Enforces enterprise default-deny authorization, ABAC/RBAC policy evaluation, consent restriction enforcement, immutable security context propagation, and non-repudiation audit logging.
- **Functions & Exposed Services**:
  - **Feature: Health Information Access & Consent Control**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Evaluate Information Access Authority](../../../../enterprise-architecture/continuum/business/business-function/4707b7fc77254ea68c30fc89f5119a77.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Evaluate Information Access Authority` — Evaluates user role, organisation, patient relationship, purpose of use, and break-glass overrides against default-deny policies.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Policy Evaluation & Authorisation Service](../../../../enterprise-architecture/continuum/business/business-service/a025ac002b6a461abaac07fcb87f411f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Policy Evaluation & Authorisation Service` — Evaluates access requests across the platform.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Evaluate Consent Constraint](../../../../enterprise-architecture/continuum/business/business-function/4a881488296d43d09112cb046fa836d3.md)
- [Consent Enforcement Service](../../../../enterprise-architecture/continuum/business/business-service/67bf392d2dd6464292619e259cc37f8e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: `FEAT-ISE-12 — Security Context Propagation` propagates attributable security context; evaluating consent constraints does not materially realise that responsibility. No replacement Feature is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Evaluate Consent Constraint](../../../../enterprise-architecture/continuum/business/business-function/4a881488296d43d09112cb046fa836d3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Evaluate Consent Constraint` — Cross-references access requests against client consent directives and sensitive health category restrictions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Consent Enforcement Service](../../../../enterprise-architecture/continuum/business/business-service/67bf392d2dd6464292619e259cc37f8e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Consent Enforcement Service` — Returns masking and redaction instructions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Propagate Security Context](../../../../enterprise-architecture/continuum/business/business-function/ee79679c6b5041659a2efca15190454f.md)
- [Security Context Validation Service](../../../../enterprise-architecture/continuum/business/business-service/f1ff9e6542004a7584a385062cb0ed21.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Security Context Binding** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Propagate Security Context](../../../../enterprise-architecture/continuum/business/business-function/ee79679c6b5041659a2efca15190454f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Propagate Security Context` — Generates and binds canonical, immutable security contexts to all intra-platform transactions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Security Context Validation Service](../../../../enterprise-architecture/continuum/business/business-service/f1ff9e6542004a7584a385062cb0ed21.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Security Context Validation Service` — Verifies transaction security credentials.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Propagate Security Context](../../../../enterprise-architecture/continuum/business/business-function/ee79679c6b5041659a2efca15190454f.md)
- [Security Context Validation Service](../../../../enterprise-architecture/continuum/business/business-service/f1ff9e6542004a7584a385062cb0ed21.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposure qualification*: Validation is the declared Service behaviour. Its juxtaposition with Propagate Security Context does not establish a one-to-one Function/Service relationship; the precise internal mapping remains unestablished.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Non-PHI Compliance Auditing**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Record Security-Significant Activity](../../../../enterprise-architecture/continuum/business/business-function/6177ed0d53e5407ab1bc4b721619ed44.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Record Security-Significant Activity` — Records tamper-evident audit evidence (who, what, when, why, patient MRN, policy outcome) without logging unmasked PHI.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Security Audit Ingress](../../../../enterprise-architecture/continuum/business/business-service/43c4a2aa7b4548ec8fdc2844e3d666b6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Security Audit Ingress` — Ingests audit events across all capabilities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Authorisation Policy Ruleset, Security Context Tokens, Tamper-Evident Security Audit Trail.

---

### 2.6 Clinical Knowledge Services
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinical Knowledge Services](../../../../enterprise-architecture/continuum/strategy/capability/bbd2bd21859f40e39963bdbe7a851686.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Knowledge Services`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Resolves canonical medical ontologies (SNOMED-CT, LOINC, ICD-10, AMT) and executes cross-terminology concept mappings.
- **Functions & Exposed Services**:
  - **Feature: Clinical Concept Resolution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resolve Clinical Concept](../../../../enterprise-architecture/continuum/business/business-function/8f89eacbc53b4697b5a747db2e143510.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Resolve Clinical Concept` — Validates concept identifiers and retrieves preferred clinical rubrics and hierarchy paths.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Terminology Concept Resolution Service](../../../../enterprise-architecture/continuum/business/business-service/3be22abcc49045c7a609bdf6428fe15a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Terminology Concept Resolution Service` — Exposes concept validation lookups.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Bidirectional Terminology Mapping**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Map Clinical Concept](../../../../enterprise-architecture/continuum/business/business-function/9549208d56cd47d8a9ba4e586323ef3b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Map Clinical Concept` — Translates local lab codes and legacy terms to canonical SNOMED/LOINC codes using governed translation maps.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Terminology Mapping Service](../../../../enterprise-architecture/continuum/business/business-service/27dfb51817344e17ab78aa6f33f959ba.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Terminology Mapping Service` — Translates codes across terminology systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Canonical Clinical Ontologies, Semantic Value Sets, Cross-Terminology Mapping Tables.

---

### 2.7 Clinical Collaboration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinical Collaboration](../../../../enterprise-architecture/continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Collaboration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Governs real-time multidisciplinary clinical collaboration channels, patient care-team virtual workspaces, and secure clinical discussions.
- **Key Architectural Semantics**:
  - **Discourse vs. Record Guardrail**: Real-time collaborative discussion and messaging in team channels do **not** automatically constitute authoritative clinical health records or observations. Authoritative entries require explicit formal document submission.
- **Functions & Exposed Services**:
  - **Feature: Care-Team Clinical Collaboration Coordination**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Care-Team Collaboration](../../../../enterprise-architecture/continuum/business/business-function/5443b253f0e4425f984702d396e77a3d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate Care-Team Collaboration` — Manages care-team communication spaces, participant memberships, and clinical handovers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Collaboration Space Service](../../../../enterprise-architecture/continuum/business/business-service/17579c34d3014825a878a9ff121e30a2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Collaboration Space Service` — Manages team collaboration spaces and discussions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: In-Conversation LHR Query Resolution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resolve LHR Query within Collaboration](../../../../enterprise-architecture/continuum/business/business-function/1d4ac561d963484480e6b06a09416616.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Resolve LHR Query within Collaboration` — Contextually projects active patient summaries directly into collaborative clinical discussion channels.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Collaboration Clinical Summary Resolution](../../../../enterprise-architecture/continuum/business/business-service/6a25de08f1b5461b86bd410ea21b80a6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Collaboration Clinical Summary Resolution` — Embeds clinical summaries in collaboration feeds.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Established ownership*: `Clinical Collaboration → In-Conversation LHR Query Resolution [FEAT-ISE-17] → Resolve LHR Query within Collaboration → Collaboration Clinical Summary Resolution`.
    - *Unresolved dependency*: The precise source Service/provider, direct versus mediated retrieval, formal consuming Capability/Function for the exposed Service and intended referent of the withdrawn dependency row remain unestablished. Information ownership, Function ownership, Service ownership, consumption, information use and presentation are distinct; no substitute dependency is implied.
  - **Feature: Zero-PHI Collaboration Metadata Governance**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Collaboration Metadata](../../../../enterprise-architecture/continuum/business/business-function/94a92326ef2141efb414c4602d878caa.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Collaboration Metadata` — Enforces privacy policies, participant access gates, and zero-PHI space metadata rules.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Collaboration Space Governance Service](../../../../enterprise-architecture/continuum/business/business-service/f94eef008b214ed88ff92bc64640ccb0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Collaboration Space Governance Service` — Governs space lifecycles and membership.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Collaboration Space Metadata, Care-Team Channel Ledger, Discussion Thread Index.

---

### 2.8 General Collaboration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] General Collaboration](../../../../enterprise-architecture/continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `General Collaboration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Operational Team Chat Enablement**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Operational Collaboration](../../../../enterprise-architecture/continuum/business/business-function/3b5604d966504661af2cb6654ae0de5c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate Operational Collaboration` — Provides communication spaces for facility logistics, disaster management teams, and administrative committees.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Collaboration Service](../../../../enterprise-architecture/continuum/business/business-service/4096602ad3084bc2a9ad9a06276cbcdc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Operational Collaboration Service` — Provides non-clinical operational messaging channels.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Operational Channel Index, Non-Clinical Messaging Log.

---

### 2.9 Workflow & Activity Coordination
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Workflow & Activity Coordination](../../../../enterprise-architecture/continuum/strategy/capability/94c683c7588f475e973b58cb226345db.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Workflow & Activity Coordination`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Coordinates execution state, task dispatch, timers, deadlines, and escalations across three distinct activity archetypes:
  1. **Work Order**: Assigned physical/human operational tasks (*human doing* — e.g., transport a patient, clean a bay).
  2. **To Do**: Assigned clinical review or administrative decision tasks (*human reviewing/updating/deciding* — e.g., review abnormal lab result, countersign discharge summary).
  3. **Synthetic Task**: Automated system-executable activities (*non-human executable work* — e.g., generate summary, syndicate batch, evaluate rules).
- **Ownership Invariant**: Workflow & Activity Coordination owns **generic activity coordination semantics and timers**. It does **not** acquire ownership of the business meaning or clinical outcome of the activity.
- **Clinical-work boundary**: Coordination supports externally established work requirements and authorised human review/update/approval. It does not determine clinical tasks, manage clinical handover or own EMR clinical worklists. The [clinical-work integration rule](../metamodel/business-architecture-metamodel.md#37-clinical-work-integration-boundary) preserves clinical authority and the three work-unit meanings; Task remains synthetic work.
- **Outcome uncertainty**: For materially significant managed activity outcomes, missing acknowledgement, response or observation does not establish success or failure. Preserve uncertainty pending authoritative observation/reconciliation. This is a Business obligation, not a universal new lifecycle state; assurance evidence insufficiency remains distinct from execution uncertainty.
- **Functions & Exposed Services**:
  - **Feature: Work Order Progression**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Work Order](../../../../enterprise-architecture/continuum/business/business-function/0161308bad6945b5bcfdeea5519341cd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate Work Order` — Coordinates operational task lifecycles and dispatches.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Work Order Coordination Service](../../../../enterprise-architecture/continuum/business/business-service/8a1987ed11d44598b325359287e503f0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Work Order Coordination Service` — Manages operational work progression.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Work Order](../../../../enterprise-architecture/continuum/business/business-function/0161308bad6945b5bcfdeea5519341cd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Work Order Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: To Do Decision & Approval Coordination**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate To Do](../../../../enterprise-architecture/continuum/business/business-function/a4c9a281578d493eb876eda9fb73cac1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate To Do` — Coordinates human review/update/approval items, reminders and sign-off queues within Harmonia's operational responsibility; clinical requirements and decisions remain with accountable clinical authority.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [To Do Management Service](../../../../enterprise-architecture/continuum/business/business-service/6bc69c5517024c43b1ab701aac5e39b7.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `To Do Management Service` — Provides To Do inbox and sign-off coordination without acquiring underlying clinical-work management.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate To Do](../../../../enterprise-architecture/continuum/business/business-function/a4c9a281578d493eb876eda9fb73cac1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **To Do Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Synthetic Task Orchestration**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Synthetic Task](../../../../enterprise-architecture/continuum/business/business-function/6cb88c7e689648b1bd0a4d1e26b12f9b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate Synthetic Task` — Orchestrates automated system workflows and operational progression.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Synthetic Task Execution Service](../../../../enterprise-architecture/continuum/business/business-service/57317da133ba4e29bde3997921f92e2e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Synthetic Task Execution Service` — Coordinates automated tasks.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Synthetic Task](../../../../enterprise-architecture/continuum/business/business-function/6cb88c7e689648b1bd0a4d1e26b12f9b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Synthetic Task Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Workflow Timeout & Escalation Oversight**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Supervise Activity Timeout / Escalation](../../../../enterprise-architecture/continuum/business/business-function/cbf6bfd71d5d4ea9923f10f288ff24dd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Supervise Activity Timeout / Escalation` — Monitors SLAs and triggers escalations when clinical or operational deadlines elapse.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Activity Escalation Telemetry](../../../../enterprise-architecture/continuum/business/business-service/f6c353c9349d47daadd59e57821ad03b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Activity Escalation Telemetry` — Discloses SLA breaches.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Generic Activity Instance Registry, Task State Progression Graph, SLA Deadline & Timer Ledger.

---

### 2.10 Calendar Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Calendar Management](../../../../enterprise-architecture/continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Calendar Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Projects unified chronological timelines across appointments, on-call rosters, and scheduled operational events.
- **Ownership Invariant**: Does **not** acquire authoritative ownership of rosters or appointment bookings (which remain owned by source scheduling and workforce systems).
- **Functions & Exposed Services**:
  - **Feature: Schedule Projection Integration**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Project Operational Schedule](../../../../enterprise-architecture/continuum/business/business-function/3ca2e6a5ca614df991a2e1a060f0f1d8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Project Operational Schedule` — Aggregates and overlays multidisciplinary schedules onto a unified temporal view.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Unified Calendar Projection Query](../../../../enterprise-architecture/continuum/business/business-service/c78bdb7ffc7d42fda450c024ca252322.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Unified Calendar Projection Query` — Discloses consolidated temporal schedules.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Temporal Event Correlation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Correlate Activity with Temporal Context](../../../../enterprise-architecture/continuum/business/business-function/b45fa964568843398661cc119b4fc693.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Correlate Activity with Temporal Context` — Associates clinical events with shift boundaries and appointment slots.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Temporal Context Resolution](../../../../enterprise-architecture/continuum/business/business-service/47f6491361794c2393cd3b64caec2d1a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Temporal Context Resolution` — Resolves operational shifts.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Projected Unified Timeline, Temporal Correlation Index.

---

### 2.11 Presentation Services
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Presentation Services](../../../../enterprise-architecture/continuum/strategy/capability/e9b6ba1917ba44cca7466444d0836c98.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Presentation Services`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Delivers user-facing portals, clinical dashboards, administrative consoles, and mobile views.
- **Ownership Invariant**: Presentation owns **rendering, user interaction, and layout concerns**. It does **not** own the underlying clinical or operational information being rendered.
- **Functions & Exposed Services**:
  - **Feature: Longitudinal Clinical & Operational Presentation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Present Contextual Health and Operational Information](../../../../enterprise-architecture/continuum/business/business-function/28ad1d4dc0444c7e8f98906a70e9319e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Present Contextual Health and Operational Information` — Composes clinical summaries, work queues, and patient records into responsive role-based user interfaces.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Contextual Presentation Service](../../../../enterprise-architecture/continuum/business/business-service/905d44b44f434fa4974e09dcafac016a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Contextual Presentation Service` — Delivers contextual health and operational information for presentation.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: UI View Configuration, User Preference Profile, Presentation Layout Specification.

---

### 2.12 Information Design Governance
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Information Design Governance](../../../../enterprise-architecture/continuum/strategy/capability/f39ed9ebcc724857a4bf7a2b8e045c7a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Information Design Governance`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Architectural Mandate**: Governs the enterprise canonical information model, data definitions, constraint schemas, terminology bindings, and exchange profiles.
- **Functions & Exposed Services**:
  - **Feature: Information Standards & Semantic Governance**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Information Semantics](../../../../enterprise-architecture/continuum/business/business-function/39a297b6c40c44ecba64857f6c3f3e0f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Information Semantics` — Authors and validates canonical data concepts and entity relationships.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Semantic Model Definition Service](../../../../enterprise-architecture/continuum/business/business-service/0f6b41152f294a87b84970c74ab5a014.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Semantic Model Definition Service` — Discloses canonical model definitions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Information Definition](../../../../enterprise-architecture/continuum/business/business-function/820b4d41490a4389bb864f1c4a04e881.md)
- [Information Dictionary Query](../../../../enterprise-architecture/continuum/business/business-service/ea491740e6cd40c69f65ad7d9d4f81fe.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Information Definition & Schema Governance** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Information Definition](../../../../enterprise-architecture/continuum/business/business-function/820b4d41490a4389bb864f1c4a04e881.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Information Definition` — Maintains governed data element dictionaries, data types, and structural rules.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Dictionary Query](../../../../enterprise-architecture/continuum/business/business-service/ea491740e6cd40c69f65ad7d9d4f81fe.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Information Dictionary Query` — Provides data dictionary specifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Terminology Binding Specification Lookup](../../../../enterprise-architecture/continuum/business/business-service/1b9e7c961a054a6bbdb7876d08a82ef3.md)
- [Govern Terminology Binding](../../../../enterprise-architecture/continuum/business/business-function/9e96ce12b7494594a6129f79f7491223.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Terminology Binding Governance** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Terminology Binding](../../../../enterprise-architecture/continuum/business/business-function/9e96ce12b7494594a6129f79f7491223.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Terminology Binding` — Binds canonical data elements to authoritative terminology value sets.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Terminology Binding Specification Lookup](../../../../enterprise-architecture/continuum/business/business-service/1b9e7c961a054a6bbdb7876d08a82ef3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Terminology Binding Specification Lookup` — Discloses value set bindings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Exchange Profile](../../../../enterprise-architecture/continuum/business/business-function/935f9a62438944599b913f2614c9e595.md)
- [Exchange Profile Specification Query](../../../../enterprise-architecture/continuum/business/business-service/98bc4e8f57f5493ebeb88f8bfc8104a8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Exchange Profile Governance** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Exchange Profile](../../../../enterprise-architecture/continuum/business/business-function/935f9a62438944599b913f2614c9e595.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Exchange Profile` — Defines standards-based interchange profiles and message constraints.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Exchange Profile Specification Query](../../../../enterprise-architecture/continuum/business/business-service/98bc4e8f57f5493ebeb88f8bfc8104a8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Exchange Profile Specification Query` — Discloses interoperability schemas.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Semantic Conformance Verification**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Verify Semantic Conformance](../../../../enterprise-architecture/continuum/business/business-function/5445184252c94c05874c52350ff13d2b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Verify Semantic Conformance` — Validates candidate messages against canonical governance schemas.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Semantic Conformance Verification Service](../../../../enterprise-architecture/continuum/business/business-service/3d492f97eb3f4d50a1c448477c61bf2b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Semantic Conformance Verification Service` — Validates representations for compliance.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Enterprise Canonical Semantic Model, Governed Data Dictionary, Terminology Binding Matrix, Exchange Profile Catalogue, Semantic Conformance Ruleset.
