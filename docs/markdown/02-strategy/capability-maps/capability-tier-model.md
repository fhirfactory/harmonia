# Harmonia Capability Decomposition & Derivation Progression

## Overview

This model distinguishes hierarchical Capability decomposition from the derivation progression, composition relationships, and derivation rules connecting high-level healthcare enterprise capabilities with reusable platform capabilities and downstream architectural responsibility boundaries.

Canonical terminology SHALL distinguish:

- **Layer / Architectural Layer**: architectural layering within the TOGAF / ArchiMate architecture model.
- **Capability Tier**: hierarchical decomposition within a Capability Model.
- **Derivation / Derivation Progression**: progression between different architectural constructs or models; a Derivation Stage is a stage in that progression.

`Layer ≠ Capability Tier ≠ Derivation Stage`.

Capability decomposition uses `CT1` (Capability Tier 1), `CT2` (Capability Tier 2), `CT3` (Capability Tier 3) and `FT` (Feature). `L1`, `L2` and `L3` are deprecated as canonical capability classifications. Decomposition MAY proceed `CT1 → CT2 → CT3 → FT` without requiring every branch to materialise every possible lower tier. Established higher-tier ancestry SHALL be represented when a lower Capability Tier is materialised; missing ancestry SHALL remain unresolved rather than being invented or skipped.

The [Domain03 Business Architecture metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#8-architectural-element-identity-and-canonical-identification) governs typed Canonical IDs, identity, aliases and behavioural anchoring. The five-stage derivation progression below SHALL NOT be reinterpreted as CT1 / CT2 / CT3 / FT or used to assign Capability Tiers or identifier codes to existing elements. [Approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing) establishes that affected legacy owner assignments are insufficiently evidenced; they SHALL NOT be mechanically translated to CT1 / CT2 / CT3. Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved where not established.

Unresolved Capability Tier or ancestry SHALL NOT prevent downstream derivation where the owning Capability or Feature and its relevant responsibility are established. Traceability SHALL reference that element without manufacturing ancestry, as required by [AX-17](../../governance/architectural-axioms.md#ax-17). Document structure, grouping, numbering, indentation and decomposition presentation SHALL NOT by themselves establish Architectural Element identity.

The model ensures that:
1. Enterprise healthcare practice is clearly demarcated from software enablement.
2. System-enabled capabilities reflect authentic clinical, administrative, and operational contexts rather than integration software mechanics.
3. System behaviours are decomposed into atomic, testable features.
4. Reusable capabilities (EC-01 .. EC-14) are derived systematically without creating centralized bottleneck services.
5. Technical realisations are guided by ICT Foundation lenses without allowing physical software products to define architectural capabilities.

---

## 1. The Five-Stage Derivation Progression

The framework progresses through five derivation stages relating different architectural constructs and models. Their stage numbers SHALL NOT denote Capability Tiers or Architectural Layers:

```text
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 1: BUSINESS CAPABILITY                                           │
│ What the enterprise must be capable of doing                           │
│ (18 catalogued capabilities across 5 natural healthcare regions)       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ requires enablement by
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 2: BUSINESS ENABLING CAPABILITY                                   │
│ What systems must enable or provide in support of the business         │
│ (Structured across 5 authentic healthcare contextual views)            │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ specifies atomic behaviour as
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 3: FEATURE (FT)                                                  │
│ Smallest useful statement of required system-enabled behaviour         │
│ (Independently understandable, testable, technology-neutral)           │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ derived into reusable
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 4: ENTERPRISE CAPABILITY (EC-01 .. EC-14)                          │
│ Reusable ICT functionality recurring across multiple domains           │
│ (Decoupled from concurrency, message brokers, and storage DDL)         │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ viewed through
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 5: ICT FOUNDATION CAPABILITY LENSES (18 Lenses)                   │
│ Cross-cutting technical enablement considerations                      │
│ (Evaluates technical realization via technology-substitution test)     │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ realised by
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ ARCHITECTURE REALISATION                                               │
│ Domains 03–07: Business, Information, Application, Integration, Tech   │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Derivation Stage Definitions

### Stage 1: Business Capability
- **Definition**: What the healthcare enterprise must be capable of doing to deliver healthcare services, govern operations, and ensure patient safety.
- **Independence**: Exists independently of IT systems, software products, commercial organizational hierarchies, or automation.
- **Structure**: 18 catalogued Business Capabilities grouped into five natural regions; this derivation-stage position does not assign Capability Tier or Canonical ID:
  - *Care & Health Delivery* (01–08)
  - *Health Information & Digital Health* (09–12)
  - *Research & Innovation* (13)
  - *Health Service Management* (14–16)
  - *Governance & Assurance* (17–18)
- **Harmonia Principle**: Harmonia provides core technical enablement for Health Information and Connected Health capabilities (09–12) and materially enables delivery and workforce capabilities (01, 02, 04, 06, 15) without asserting platform ownership over healthcare business practice.

Natural regions are contextual groupings, not capability tiers, organisational structures, application boundaries or ownership hierarchies, and are distinct from Business Enabling contextual views. The appended 17 Health Service Governance and 18 Health Service Assurance retain unresolved relevance classification; 17's enabling derivation remains unresolved. The [approved Health Service Assurance derivation](health-service-assurance-derivation.md) establishes only REQ-FND-005 → BC-18 → the three named Business Enabling Capabilities → collaborative Enterprise Capability realisation. Stage diagrams and existing examples do not establish additional relationships.

### Stage 2: Business Enabling Capability
- **Definition**: What software systems and information infrastructure must enable or provide to support the enterprise healthcare capabilities.
- **Structuring Axis**: Organized into five contextual views reflecting genuine healthcare operating environments:
  1. *Entity Management*: Governance of practitioners, organizations, locations, services, products, and devices.
  2. *Service Administration*: Administrative progression of encounters, referrals, orders, scheduling, and billing.
  3. *Service Delivery*: Systems enablement across clinical delivery settings (primary, acute, emergency, diagnostic, virtual).
  4. *Health Service Operations*: Healthcare facility logistics, bed management, work allocation, dispatch, and discharge coordination.
  5. *Intrinsic / Shared Enablement*: Longitudinal clinical records, health information exchange, clinical collaboration, and workflow coordination.
- **Independence**: Independent of commercial software product boundaries (PAS, EMR, LIS, RIS) and internal middleware engines.

The approved [Assurance Design, Assurance Criteria Management and Governed Assurance](../capabilities/business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities) additionally support BC-18. Their contextual-view placement, relevance classification, Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved. Their direct Strategy derivation does not create intermediate Features, a sixth view or inferred hierarchy.

### Stage 3: Feature (FT)
- **Definition**: The **smallest useful statement of required system-enabled behaviour** within its owning Business Enabling Capability context. FT is a Feature classification; derivation stage 3 SHALL NOT classify it as CT3.
- **Criteria**:
  - *Independently understandable*: Conveys a self-contained operational meaning.
  - *Testable in principle*: Allows clear verification that the behavior occurred or was enabled.
  - *Technology-independent*: Contains no product names, protocols, or storage mechanisms.
  - *Component-independent*: Does not prescribe which software module executes it.
- **Scope Demarcation**: Formally authored for Harmonia-Relevant and Harmonia-Core capabilities in the established catalogue. Reference and Adjacent capabilities are intentionally not decomposed into features. This scope statement establishes no new Capability Tier assignment or mandatory intermediate hierarchy.

### Stage 4: Enterprise Capability (EC-01 .. EC-14)
- **Definition**: Reusable architectural functionality derived from recurring system-enabled features across multiple healthcare domains.
- **Derivation Logic**: Derived by analyzing features and asking: *"How is this function delivered?"* Functions that recur vertically across multiple distinct healthcare domains are abstracted into reusable Enterprise Capabilities.
- **Catalogue**: The established EC-01 Managed Entity & Relationship through EC-13 Semantic Governance & Conformance, plus approved EC-14 Service Guardian.
- **Collaborative Nature**: Enterprise Capabilities represent bounded reusable responsibilities. Their realisation may require participation, consumption or enforcement across multiple architectural elements, but this does not imply shared semantic ownership or prohibit establishment of a responsibility centre. **Distributed participation does not imply distributed responsibility. Cross-cutting concern does not imply cross-cutting responsibility.**

EC-14 supplies the assurance-specific semantics needed to bind existing reusable capabilities into governed assurance. The [approved first-pass contribution matrix](health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix) establishes collaborative realisation without extending EC-12's semantics or allocating EC-14 to a component. The generic progression through Features describes the existing derivation convention; it does not manufacture Feature relationships for this explicitly approved assurance derivation.

### Stage 5: ICT Foundation Capability Lenses
- **Definition**: Technical enablement lenses that provide cross-cutting engineering criteria, architectural considerations, and technology-substitution testing for realizing Enterprise Capabilities.
- **Role**: They are technical lenses, NOT peers of the Business Enabling capabilities.
- **Test**: Governed by the *Technology-Substitution Test*: *"If the named product, protocol, programming language, or deployment mechanism were replaced tomorrow, would the capability still exist?"*

---

## 3. The Capability Composition Formula

Harmonia capabilities are composed according to the following foundational relationship:

$$\text{\bf Harmonia Capability} = \text{domain-specific enabling behaviour} + \text{reusable Enterprise Capabilities}$$

Where:
- **Domain-Specific Enabling Behaviour**: The specific clinical or operational logic, context rules, and progression semantics unique to a healthcare domain (e.g., healthcare identifier system rules, diagnostic order workflows, bed turnover readiness).
- **Reusable Enterprise Capabilities**: The horizontal, platform-wide capabilities (EC-01 through EC-14) that provide state management, context propagation, policy evaluation, provenance capture, interoperable transport and the approved assurance-specific semantics.

### Informing Strategic Logical Component Boundaries

In Harmonia Strategy:
- **Capabilities describe *what* the platform achieves**.
- **Strategic Logical Components describe *how responsibility is organized***.

When specific combinations of Enterprise Capabilities and domain-specific behaviours recur coherently across multiple features, they inform candidate **Strategic Logical Component responsibility boundaries**:

```text
┌────────────────────────────────────────────────────────┐
│  RECURRING COMPOSITION PATTERN                         │
│  Domain-specific enabling behaviour                    │
│        + Reusable Enterprise Capabilities              │
└───────────────────────────┬────────────────────────────┘
                            │ informs clustering for
                            ▼
┌────────────────────────────────────────────────────────┐
│  STRATEGIC LOGICAL RESPONSIBILITY MODEL                │
│  - Mneme (Managed Information & Active State)          │
│  - Mnemosyne (Durable Preservation & Recovery)         │
│  - Ponos (Managed Operational Activity Execution)      │
│  - Pylai (Standards-Conformant Ingress/Egress Membrane)│
│  - Calliope (Semantic Authority & Conformance)         │
│  - Iris (Contextual Human Interaction & Presentation)  │
│  - Digital Twin (Entity Coordination Archetype)        │
└────────────────────────────────────────────────────────┘
```

*Architectural Principle*: This composition informs component boundaries; it does **not** imply a rigid, mathematical one-to-one derivation. Components represent cohesive responsibility centres, not one-to-one wrappings of individual capabilities.

The displayed component-clustering examples remain the established EC-01 through EC-13 derivations. This convention does not allocate EC-14 or the three assurance Business Enabling Capabilities to a strategic logical component or establish a relationship to an existing implementation construct.

---

## 4. Representative Derivation Examples

To demonstrate how Enterprise Capabilities recur across distinct Business Enabling features without constructing an exhaustive matrix, three representative derivation examples are detailed below.

### Example 1: Person Identifier Resolution

In healthcare integration, identifying an individual across disparate clinics, hospitals, and diagnostic centres requires resolving identifiers (MRNs, national healthcare identifiers, local practice IDs) issued by multiple independent authorities.

- **Domain-Specific Enabling Behaviour**:
  - Healthcare identifier system semantics (e.g., verifying issuing authority jurisdiction, assigning authority namespaces).
  - Scope of identifier validity (temporary vs. permanent, national vs. regional).
  - Client privacy and consent flags associated with identifier disclosure.
- **Reusable Enterprise Capability Contributions**:
  - **EC-01 Managed Entity & Relationship**: Modeling the Person entity and linking multiple scoped identifiers and alias relationships.
  - **EC-05 Search & Discovery**: Locating existing entity records matching supplied identifier sets and resolving candidate matches.
  - **EC-03 Managed State & Lifecycle**: Governing the lifecycle status (active, superseded, suspended) of identifier records.
  - **EC-07 Provenance & Traceability**: Recording the authoritative origin, verification timestamp, and issuing system attribution for each identifier.
  - **EC-06 Policy & Control**: Enforcing default-deny access policies governing which requesting systems may view or resolve specific identifier authorities.
  - **EC-12 Operational Assurance**: Ensuring deterministic matching behavior and transactional consistency during concurrent resolution requests.

---

### Example 2: Order Closed Loop

A clinical diagnostic or procedure order requires governed progression from initial request to final diagnostic report.

- **Domain Progression Semantics**:
  $$\text{Destination} \longrightarrow \text{Route} \longrightarrow \text{Deliver} \longrightarrow \text{Acknowledge} \longrightarrow \text{Progress} \longrightarrow \text{Complete} \longrightarrow \text{Outcome}$$
- **Critical Healthcare Distinction**:
  $$\text{\bf Technical Delivery Acknowledgement} \neq \text{\bf Business Acknowledgement}$$
  A technical acknowledgement establishes only the acceptance state defined by the applicable interaction contract. It SHALL NOT be interpreted as durable acceptance, committed business state, successful processing or successful business disposition unless that meaning is explicitly established by the governing contract. HTTP 200/202 and MLLP acknowledgements are interpreted under their applicable contracts. A handoff acknowledgement alone does not establish the eventual downstream business outcome or the receiving laboratory's clinical responsibility. [REQ-FND-001](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-001-durable-ingress-acceptance-boundary) retains its explicit Harmonia durable-ingress requirement.
- **Reusable Enterprise Capability Contributions**:
  - **EC-02 Context Management**: Establishing and propagating the clinical encounter, ordering provider, and subject-of-care context across all order lifecycle events.
  - **EC-05 Search & Discovery**: Resolving the target fulfillment service endpoint and receiving facility capabilities.
  - **EC-01 Managed Entity & Relationship**: Associating the Order entity with the Subject, Ordering Practitioner, Performing Organization, and Target Service.
  - **EC-08 Interoperability & Exchange**: Standards-compliant boundary protocol adaptation, message transformation, delivery dispatch, and transport acknowledgement capture.
  - **EC-03 Managed State & Lifecycle**: Tracking the formal Order state progression (`PLACED` $\to$ `RECEIVED` $\to$ `ACCEPTED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`).
  - **EC-10 Activity & Execution**: Instantiating and executing operational units of work, tracking timeouts, managing escalations, and supervising fulfillment deadlines.
  - **EC-07 Provenance & Traceability**: Capturing complete chain-of-custody, transformation history, and delivery timestamps for clinical auditability.
  - **EC-12 Operational Assurance**: Guaranteeing dual-write safety, duplicate suppression, and exception recovery across asynchronous boundary handoffs.

---

### Example 3: Work Allocation & Dispatch

Operational healthcare activities (e.g., patient transport, ward bed preparation, discharge coordination, emergency triage assessment) require assigning and dispatching work to qualified clinical or operational staff.

- **Domain Progression Semantics**:
  $$\text{Event} \longrightarrow \text{Entity Context} \longrightarrow \text{Required Activity} \longrightarrow \text{Assignment} \longrightarrow \text{Dispatch} \longrightarrow \text{ACK} \longrightarrow \text{Progress} \longrightarrow \text{Outcome}$$
- **Healthcare Service Context Rule**: Where operational activity occurs in the context of a Healthcare Service, that service context must be explicitly associated with the activity/state rather than inferred solely from practitioner, organization, or location.
- **Cross-Domain Recurrence**: This operational progression pattern recurs across:
  - *Discharge Progress*: Coordinating multidisciplinary discharge milestones and readiness oversight.
  - *Patient Transport*: Allocating transfer tasks to portering staff based on mobility requirements.
  - *Mobile Work*: Dispatching community health visits to roving practitioners.
  - *ED Operational Coordination*: Progressing triage queue assessments.
  - *Bed Readiness*: Dispatching environmental cleaning staff upon patient discharge.
- **Reusable Enterprise Capability Contributions**:
  - **EC-02 Context Management**: Binding the patient, physical bed/room, care team, and healthcare service context to the operational work item.
  - **EC-03 Managed State & Lifecycle**: Managing the lifecycle of the operational activity (`REQUESTED` $\to$ `ASSIGNED` $\to$ `ACCEPTED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`).
  - **EC-05 Search & Discovery**: Discovering available, rostered, and credentialed practitioners or operational personnel matching task criteria.
  - **EC-01 Managed Entity & Relationship**: Linking the assigned worker, patient, location, and equipment entities.
  - **EC-10 Activity & Execution**: Governing the assignment logic, dispatch mechanism, human notification, timeout monitoring, and escalation triggers.
  - **EC-08 Interoperability & Exchange**: Delivering work orders to mobile devices or external task management systems where external dispatch is required.
  - **EC-12 Operational Assurance**: Preventing duplicate assignment and ensuring resilient recovery if a dispatched worker does not acknowledge within defined thresholds.
  - **EC-07 Provenance & Traceability**: Recording assignment timestamps, worker acceptance, and completion evidence.

---

## 5. Architectural Modelling Position: Omission of Exhaustive $N \times M$ Matrices

Harmonia explicitly rejects the generation of exhaustive $N \times M$ traceability matrices between Business Capabilities and Business Enabling Capabilities, or between Features and Enterprise Capabilities.

### Formal Rationale

> **The Business Enabling Capability model was derived with explicit consideration of the Business Capability model and is intended to provide the system-enabled capabilities necessary to support that business landscape. A formal many-to-many mapping between Business Capabilities and Business Enabling Capabilities has not been produced, as such traceability is not required for the current Harmonia architecture and strategy objectives. Such mapping may be developed subsequently where required for benefits realisation, investment analysis, business-case development or other value-traceability purposes.**

This statement records the existing derivation. BC-17 enabling derivation and relationships beyond the approved BC-18 derivation remain unresolved under [AX-17](../../governance/architectural-axioms.md#ax-17). The [Health Service Assurance view](health-service-assurance-derivation.md) adds only the approved three Business Enabling Capabilities and bounded fourteen-row EC contribution matrix. That explicit matrix is not an exhaustive Business Capability-to-Business Enabling or Feature-to-Enterprise mapping, does not imply other coverage, and does not change EC-12's established operational semantics.

### Utilitarian Justification
1. **Avoidance of False Precision**: In a complex regional HIE, a single Business Capability (e.g., *Care Access & Coordination*) draws upon dozens of enabling capabilities across Entity Management, Service Administration, and Shared Enablement. An exhaustive matrix suggests mechanical one-to-one connections where real systems operate through dynamic, contextual composition.
2. **Maintenance Overhead vs. Value**: Maintaining a static matrix of hundreds of capabilities across thousands of cells creates substantial documentation drift without providing actionable guidance to software engineers or architects.
3. **Clarity of Representative Derivations**: As shown in Section 4, representative derivation patterns provide clear, verifiable architectural rationale for how enterprise needs compose reusable capabilities, without the clutter of exhaustive combinatorial tables.

---

## Topic Navigation

- [Domain 02 Strategy Overview](../README.md)
- [Capability Framework Index](../capabilities/index.md)
- [Business Capabilities](../capabilities/business-capabilities.md)
- [Business Enabling Capabilities](../capabilities/business-enabling-capabilities.md)
- [Enterprise Capabilities](../capabilities/enterprise-capabilities.md)
- [Health Service Assurance Strategy Derivation](health-service-assurance-derivation.md)
- [ICT Foundation Lenses](../capabilities/ict-foundation-lenses.md)
