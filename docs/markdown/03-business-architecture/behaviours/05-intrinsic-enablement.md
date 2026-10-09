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
- **Owning Capability**: `Patient Clinical Record`
- **Architectural Mandate**: Governs the canonical, longitudinal, cross-institutional health record for every patient across the healthcare region.
- **Functions & Exposed Services**:
  - **Feature: Longitudinal Health Record (LHR) Assembly**:
    - *Function*: `Assemble Longitudinal Clinical Record` — Synthesises clinical documents, encounters, diagnostic reports, and medication histories across disparate source systems into a unified chronological clinical view.
    - *Exposed Service*: `Longitudinal Clinical Record Query` — Discloses the comprehensive clinical timeline to authorised clinicians.
  - **Feature: Active Clinical Record Access**:
    - *Function*: `Maintain Active Clinical Record` — Maintains active problem lists, current medication regimens, allergies, and open clinical alerts for active care coordination.
    - *Exposed Service*: `Active Problem & Allergy Summary Query` — Exposes immediate active clinical summaries.
  - **Feature: Durable Clinical Record Preservation**:
    - *Function*: `Preserve Durable Clinical Record` — Preserves the governed longitudinal representation and durable, immutable, append-only history of what Harmonia received, knew, asserted or managed, with provenance and versioning. Corrections/supersession establish changed knowledge without retrospectively altering that history or acquiring originating clinical authority.
    - *Exposed Service*: `Historical Clinical Record Access` — Provides governed historical record retrieval for authorised clinical and legal review; preservation does not itself establish legal qualification or originating authority.
- **Information Responsibility**: Governed Longitudinal Health Record (LHR), Canonical Clinical Timeline, Active Problem List, Immutable Historical Record Store.

---

### 2.2 Health Information Exchange (HIE)
- **Owning Capability**: `Health Information Exchange`
- **Architectural Mandate**: Manages the ingestion, transformation, routing, addressed distribution, and publish-subscribe syndication of health information across organizational and jurisdictional boundaries.
- **Ownership Invariant**: Health Information Exchange owns **exchange transaction state, delivery receipts, and routing policies**. It does **not** acquire ownership of the business information transported.
- **Functions & Exposed Services**:
  - **Feature: Direct Submission Ingestion & Delivery**:
    - *Function*: `Receive Information Submission` — Validates and ingests standards-compliant clinical messages from external health providers.
    - *Exposed Service*: `HIE Submission Gateway Service` — Ingress endpoint for regional data contributions.
  - **Feature: Clinical Record Retrieval Mediation**:
    - *Function*: `Mediate Information Retrieval` — Federates queries across participating hospital and diagnostic node repositories.
    - *Exposed Service*: `Federated Health Information Query` — Resolves distributed record queries.
  - **Feature: Addressed Clinical Distribution**:
    - *Function*: `Distribute Addressed Information` — Directs point-to-point clinical documents to specific recipient practitioner endpoints.
    - *Exposed Service*: `Addressed Document Delivery Service` — Securely delivers documents to designated recipient mailboxes.
  - **Feature: Event-Driven Syndication**:
    - *Function*: `Syndicate Information Change` — Broadcasts event notifications and record updates to subscribed regional care network systems.
    - *Exposed Service*: `Clinical Event Syndication Stream` — Emits publish-subscribe clinical event feeds.
- **Information Responsibility**: Exchange Transaction Ledger, Route Resolution Map, Subscription & Syndication Matrix.

---

### 2.3 Health Information Access
- **Owning Capability**: `Health Information Access`
- **Architectural Mandate**: Provides high-performance search, retrieval, and access-qualified filtering of clinical information across the longitudinal record.
- **Ownership Invariant**: Consumes access control and consent determinations from *Health Information Control*. Search behaviour does **not** own security policy.
- **Functions & Exposed Services**:
  - **Feature: Federated Clinical Query**:
    - *Function*: `Search Health Information` — Executes structured semantic searches across diagnostic reports, clinical notes, and medication histories.
    - *Exposed Service*: `Clinical Information Search Service` — Discloses search capabilities to clinical user interfaces.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: `FEAT-ISE-09 — Access-Controlled Information Filtering` requires access-controlled disclosure/filtering; retrieval of discrete information alone does not materially realise that responsibility. No replacement Feature is assigned.
    - *Function*: `Retrieve Health Information` — Fetches discrete clinical observations and original attachments.
    - *Exposed Service*: `Clinical Resource Retrieval Service` — Delivers structured clinical information to requesting applications.
  - **Capability-scoped behaviour: Access-Qualified Result Filtering** (Feature identity not established):
    - *Function*: `Apply Access-Qualified Result Filtering` — Redacts or masks search results and clinical content based on the caller's verified security context and patient consent directives.
    - *Exposed Service*: *(Embedded within Search and Retrieval Services)*.
- **Information Responsibility**: Query Execution Context, Filtered Result Projection State.

---

### 2.4 Health Information Communication
- **Owning Capability**: `Health Information Communication`
- **Architectural Mandate**: Encapsulates standards-based health information communication to mediate technical transmission between participating parties.
- **Ownership Invariant**: Does **not** own every business interaction communicated through it; provides protocol adaptation and secure transport mediation.
- **Functions & Exposed Services**:
  - **Feature: Standards-Based Boundary Exchange**:
    - *Function*: `Mediate Standards-Based Health Information Communication` — Handles standards-based communication mediation, transaction coordination, and technical error handling.
    - *Exposed Service*: `Standards Communication Gateway Service` — Protocol interface for external client integrations.
- **Information Responsibility**: Transport Protocol Binding Map, Network Session State.

---

### 2.5 Health Information Control
- **Owning Capability**: `Health Information Control`
- **Architectural Mandate**: Enforces enterprise default-deny authorization, ABAC/RBAC policy evaluation, consent restriction enforcement, immutable security context propagation, and non-repudiation audit logging.
- **Functions & Exposed Services**:
  - **Feature: Health Information Access & Consent Control**:
    - *Function*: `Evaluate Information Access Authority` — Evaluates user role, organisation, patient relationship, purpose of use, and break-glass overrides against default-deny policies.
    - *Exposed Service*: `Policy Evaluation & Authorisation Service` — Evaluates access requests across the platform.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: `FEAT-ISE-12 — Security Context Propagation` propagates attributable security context; evaluating consent constraints does not materially realise that responsibility. No replacement Feature is assigned.
    - *Function*: `Evaluate Consent Constraint` — Cross-references access requests against client consent directives and sensitive health category restrictions.
    - *Exposed Service*: `Consent Enforcement Service` — Returns masking and redaction instructions.
  - **Capability-scoped behaviour: Security Context Binding** (Feature identity not established):
    - *Function*: `Propagate Security Context` — Generates and binds canonical, immutable security contexts to all intra-platform transactions.
    - *Exposed Service*: `Security Context Validation Service` — Verifies transaction security credentials.
    - *Exposure qualification*: Validation is the declared Service behaviour. Its juxtaposition with Propagate Security Context does not establish a one-to-one Function/Service relationship; the precise internal mapping remains unestablished.
  - **Feature: Non-PHI Compliance Auditing**:
    - *Function*: `Record Security-Significant Activity` — Records tamper-evident audit evidence (who, what, when, why, patient MRN, policy outcome) without logging unmasked PHI.
    - *Exposed Service*: `Security Audit Ingress` — Ingests audit events across all capabilities.
- **Information Responsibility**: Authorisation Policy Ruleset, Security Context Tokens, Tamper-Evident Security Audit Trail.

---

### 2.6 Clinical Knowledge Services
- **Owning Capability**: `Clinical Knowledge Services`
- **Architectural Mandate**: Resolves canonical medical ontologies (SNOMED-CT, LOINC, ICD-10, AMT) and executes cross-terminology concept mappings.
- **Functions & Exposed Services**:
  - **Feature: Clinical Concept Resolution**:
    - *Function*: `Resolve Clinical Concept` — Validates concept identifiers and retrieves preferred clinical rubrics and hierarchy paths.
    - *Exposed Service*: `Terminology Concept Resolution Service` — Exposes concept validation lookups.
  - **Feature: Bidirectional Terminology Mapping**:
    - *Function*: `Map Clinical Concept` — Translates local lab codes and legacy terms to canonical SNOMED/LOINC codes using governed translation maps.
    - *Exposed Service*: `Terminology Mapping Service` — Translates codes across terminology systems.
- **Information Responsibility**: Canonical Clinical Ontologies, Semantic Value Sets, Cross-Terminology Mapping Tables.

---

### 2.7 Clinical Collaboration
- **Owning Capability**: `Clinical Collaboration`
- **Architectural Mandate**: Governs real-time multidisciplinary clinical collaboration channels, patient care-team virtual workspaces, and secure clinical discussions.
- **Key Architectural Semantics**:
  - **Discourse vs. Record Guardrail**: Real-time collaborative discussion and messaging in team channels do **not** automatically constitute authoritative clinical health records or observations. Authoritative entries require explicit formal document submission.
- **Functions & Exposed Services**:
  - **Feature: Care-Team Clinical Collaboration Coordination**:
    - *Function*: `Coordinate Care-Team Collaboration` — Manages care-team communication spaces, participant memberships, and clinical handovers.
    - *Exposed Service*: `Clinical Collaboration Space Service` — Manages team collaboration spaces and discussions.
  - **Feature: In-Conversation LHR Query Resolution**:
    - *Function*: `Resolve LHR Query within Collaboration` — Contextually projects active patient summaries directly into collaborative clinical discussion channels.
    - *Exposed Service*: `Collaboration Clinical Summary Resolution` — Embeds clinical summaries in collaboration feeds.
    - *Established ownership*: `Clinical Collaboration → In-Conversation LHR Query Resolution [FEAT-ISE-17] → Resolve LHR Query within Collaboration → Collaboration Clinical Summary Resolution`.
    - *Unresolved dependency*: The precise source Service/provider, direct versus mediated retrieval, formal consuming Capability/Function for the exposed Service and intended referent of the withdrawn dependency row remain unestablished. Information ownership, Function ownership, Service ownership, consumption, information use and presentation are distinct; no substitute dependency is implied.
  - **Feature: Zero-PHI Collaboration Metadata Governance**:
    - *Function*: `Govern Collaboration Metadata` — Enforces privacy policies, participant access gates, and zero-PHI space metadata rules.
    - *Exposed Service*: `Collaboration Space Governance Service` — Governs space lifecycles and membership.
- **Information Responsibility**: Collaboration Space Metadata, Care-Team Channel Ledger, Discussion Thread Index.

---

### 2.8 General Collaboration
- **Owning Capability**: `General Collaboration`
- **Functions & Exposed Services**:
  - **Feature: Operational Team Chat Enablement**:
    - *Function*: `Coordinate Operational Collaboration` — Provides communication spaces for facility logistics, disaster management teams, and administrative committees.
    - *Exposed Service*: `Operational Collaboration Service` — Provides non-clinical operational messaging channels.
- **Information Responsibility**: Operational Channel Index, Non-Clinical Messaging Log.

---

### 2.9 Workflow & Activity Coordination
- **Owning Capability**: `Workflow & Activity Coordination`
- **Architectural Mandate**: Coordinates execution state, task dispatch, timers, deadlines, and escalations across three distinct activity archetypes:
  1. **Work Order**: Assigned physical/human operational tasks (*human doing* — e.g., transport a patient, clean a bay).
  2. **To Do**: Assigned clinical review or administrative decision tasks (*human reviewing/updating/deciding* — e.g., review abnormal lab result, countersign discharge summary).
  3. **Synthetic Task**: Automated system-executable activities (*non-human executable work* — e.g., generate summary, syndicate batch, evaluate rules).
- **Ownership Invariant**: Workflow & Activity Coordination owns **generic activity coordination semantics and timers**. It does **not** acquire ownership of the business meaning or clinical outcome of the activity.
- **Clinical-work boundary**: Coordination supports externally established work requirements and authorised human review/update/approval. It does not determine clinical tasks, manage clinical handover or own EMR clinical worklists. The [clinical-work integration rule](../metamodel/business-architecture-metamodel.md#37-clinical-work-integration-boundary) preserves clinical authority and the three work-unit meanings; Task remains synthetic work.
- **Outcome uncertainty**: For materially significant managed activity outcomes, missing acknowledgement, response or observation does not establish success or failure. Preserve uncertainty pending authoritative observation/reconciliation. This is a Business obligation, not a universal new lifecycle state; assurance evidence insufficiency remains distinct from execution uncertainty.
- **Functions & Exposed Services**:
  - **Feature: Work Order Progression**:
    - *Function*: `Coordinate Work Order` — Coordinates operational task lifecycles and dispatches.
    - *Exposed Service*: `Work Order Coordination Service` — Manages operational work progression.
    - *Governed Process*: **Work Order Progression Process**.
  - **Feature: To Do Decision & Approval Coordination**:
    - *Function*: `Coordinate To Do` — Coordinates human review/update/approval items, reminders and sign-off queues within Harmonia's operational responsibility; clinical requirements and decisions remain with accountable clinical authority.
    - *Exposed Service*: `To Do Management Service` — Provides To Do inbox and sign-off coordination without acquiring underlying clinical-work management.
    - *Governed Process*: **To Do Progression Process**.
  - **Feature: Synthetic Task Orchestration**:
    - *Function*: `Coordinate Synthetic Task` — Orchestrates automated system workflows and operational progression.
    - *Exposed Service*: `Synthetic Task Execution Service` — Coordinates automated tasks.
    - *Governed Process*: **Synthetic Task Progression Process**.
  - **Feature: Workflow Timeout & Escalation Oversight**:
    - *Function*: `Supervise Activity Timeout / Escalation` — Monitors SLAs and triggers escalations when clinical or operational deadlines elapse.
    - *Exposed Service*: `Activity Escalation Telemetry` — Discloses SLA breaches.
- **Information Responsibility**: Generic Activity Instance Registry, Task State Progression Graph, SLA Deadline & Timer Ledger.

---

### 2.10 Calendar Management
- **Owning Capability**: `Calendar Management`
- **Architectural Mandate**: Projects unified chronological timelines across appointments, on-call rosters, and scheduled operational events.
- **Ownership Invariant**: Does **not** acquire authoritative ownership of rosters or appointment bookings (which remain owned by source scheduling and workforce systems).
- **Functions & Exposed Services**:
  - **Feature: Schedule Projection Integration**:
    - *Function*: `Project Operational Schedule` — Aggregates and overlays multidisciplinary schedules onto a unified temporal view.
    - *Exposed Service*: `Unified Calendar Projection Query` — Discloses consolidated temporal schedules.
  - **Feature: Temporal Event Correlation**:
    - *Function*: `Correlate Activity with Temporal Context` — Associates clinical events with shift boundaries and appointment slots.
    - *Exposed Service*: `Temporal Context Resolution` — Resolves operational shifts.
- **Information Responsibility**: Projected Unified Timeline, Temporal Correlation Index.

---

### 2.11 Presentation Services
- **Owning Capability**: `Presentation Services`
- **Architectural Mandate**: Delivers user-facing portals, clinical dashboards, administrative consoles, and mobile views.
- **Ownership Invariant**: Presentation owns **rendering, user interaction, and layout concerns**. It does **not** own the underlying clinical or operational information being rendered.
- **Functions & Exposed Services**:
  - **Feature: Longitudinal Clinical & Operational Presentation**:
    - *Function*: `Present Contextual Health and Operational Information` — Composes clinical summaries, work queues, and patient records into responsive role-based user interfaces.
    - *Exposed Service*: `Contextual Presentation Service` — Delivers contextual health and operational information for presentation.
- **Information Responsibility**: UI View Configuration, User Preference Profile, Presentation Layout Specification.

---

### 2.12 Information Design Governance
- **Owning Capability**: `Information Design Governance`
- **Architectural Mandate**: Governs the enterprise canonical information model, data definitions, constraint schemas, terminology bindings, and exchange profiles.
- **Functions & Exposed Services**:
  - **Feature: Information Standards & Semantic Governance**:
    - *Function*: `Govern Information Semantics` — Authors and validates canonical data concepts and entity relationships.
    - *Exposed Service*: `Semantic Model Definition Service` — Discloses canonical model definitions.
  - **Capability-scoped behaviour: Information Definition & Schema Governance** (Feature identity not established):
    - *Function*: `Govern Information Definition` — Maintains governed data element dictionaries, data types, and structural rules.
    - *Exposed Service*: `Information Dictionary Query` — Provides data dictionary specifications.
  - **Capability-scoped behaviour: Terminology Binding Governance** (Feature identity not established):
    - *Function*: `Govern Terminology Binding` — Binds canonical data elements to authoritative terminology value sets.
    - *Exposed Service*: `Terminology Binding Specification Lookup` — Discloses value set bindings.
  - **Capability-scoped behaviour: Exchange Profile Governance** (Feature identity not established):
    - *Function*: `Govern Exchange Profile` — Defines standards-based interchange profiles and message constraints.
    - *Exposed Service*: `Exchange Profile Specification Query` — Discloses interoperability schemas.
  - **Feature: Semantic Conformance Verification**:
    - *Function*: `Verify Semantic Conformance` — Validates candidate messages against canonical governance schemas.
    - *Exposed Service*: `Semantic Conformance Verification Service` — Validates representations for compliance.
- **Information Responsibility**: Enterprise Canonical Semantic Model, Governed Data Dictionary, Terminology Binding Matrix, Exchange Profile Catalogue, Semantic Conformance Ruleset.
