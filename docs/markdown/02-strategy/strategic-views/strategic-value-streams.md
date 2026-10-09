# Strategic Value Streams

## Overview & Purpose

This document establishes the **Strategic Value Stream Model** for Harmonia R1.x/R2.x. In accordance with ArchiMate 3.2 strategy modelling semantics, a **Value Stream** represents an end-to-end sequence of value-adding stages that achieve an overall result for healthcare stakeholders—including patients, clinicians, multidisciplinary care teams, health service executives, and partner organisations.

Strategic Value Streams define **what value Harmonia creates** across enterprise healthcare boundaries. They stand between foundational motivation (**Domain 01**) and the capabilities, courses of action, and logical components that realise that value (**Domain 02** through **Domains 03–13**).

---

## 1. Value Stream Principles & Modelling Semantics

### 1.1 Stakeholder Value Transformation vs. Software Runtime Mechanics

A strategic value stream is not a technical workflow, a message integration pipeline, or a sequence of software component interactions. Value streams model the progressive transformation of healthcare and information state from a stakeholder perspective.

Every stage in a Harmonia Strategic Value Stream must answer:
> *"What is more valuable to the healthcare stakeholder after this stage than before it?"*

If the answer to that question is merely an internal technical operation—such as:
- "the message was transported across a broker queue",
- "the cache was updated",
- "an Ergon executed",
- "a database record was inserted", or
- "a FHIR JSON resource was serialized",

then the stage is defined at the wrong abstraction level. Technical mechanics, component invocations, and database transactions belong to downstream Application, Integration, and Technology Architectures (Domains 04–07).

### 1.2 Boundary Invariant: Decoupling Value Streams from Component Allocations

In alignment with Strategic Guardrails **G1** (*Reusable Capability $\neq$ Centralised Service*) and **G2** (*Component Boundaries Follow Architectural Responsibility*), value stream stages are **never** mapped directly or exclusively to individual logical components (e.g., Stage 1 = Pylai, Stage 2 = Mneme, Stage 3 = Ponos). 

Such mappings confuse stakeholder value with system execution topologies. Value streams are enabled by Business Capabilities and Enterprise Capabilities; those capabilities are in turn collaboratively realised by logical components (Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, and the Digital Twin construct) according to defined architectural seams.

---

### Traceability Standing and Re-Derivation Boundary

The superseded BC, contextual-view, Driver, Goal and axiom-meaning assertions formerly included in the four traceability blocks are retired as current architectural authority. They have not been translated into the current identifiers. The blocks below retain only existing relationships whose referenced responsibilities and meanings remain current; they do not establish replacement relationships for retired assertions.

The [current Motivation catalogues](../../01-motivation/README.md), [Business Capability model](../capabilities/business-capabilities.md), [Business Enabling catalogue](../capabilities/business-enabling-capabilities.md) and [Enterprise Capability definitions](../capabilities/enterprise-capabilities.md) supply the elements against which re-derivation was checked. They do not establish the missing stream-to-Business-Capability or stream-to-Business-Enabling relationships. A valid lower-level contribution does not supply those missing intermediate relationships. [AX-17](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty) requires these gaps to remain explicit; no candidate mapping is promoted to authority.

The stream purposes, stages and intended outcomes remain established. A missing traceability relationship does not invalidate a stream or an otherwise authoritative capability.

## 2. The Four Normative R1.x/R2.x Strategic Value Streams

Harmonia R1.x/R2.x defines exactly four principal Strategic Value Streams:

| Value Stream ID | Strategic Value Stream Name | Primary Value Transformation |
| :--- | :--- | :--- |
| **VS-01** | Multi-Source Clinical Information $\to$ Coherent Clinical Picture | Transforms fragmented clinical data from heterogeneous origins into a coherent, longitudinal, patient-centred clinical picture. |
| **VS-02** | Unqualified Information $\to$ Governed, Consumable Information | Transforms information with incomplete or unverified structure, meaning, or authority into governed information that can be safely published and consumed. |
| **VS-03** | Operational Need $\to$ Coordinated Activity $\to$ Resolved Outcome | Transforms an operational healthcare need into governed, coordinated activity aligned with entity state. |
| **VS-04** | Distributed Clinical Knowledge $\to$ Informed Clinical Collaboration | Transforms distributed clinical knowledge into contextually useful information supporting informed multidisciplinary care collaboration. |

---

### VS-01: Multi-Source Clinical Information → Coherent Clinical Picture

#### Strategic Purpose & Value Focus

Modern healthcare delivery is chronically fragmented across acute facilities, primary care practices, diagnostic laboratories, imaging centres, and community services. Clinical information arrives across disjointed standards (HL7 v2, CDA, FHIR), disparate identifier namespaces, and isolated episodes of care.

The strategic value created by **VS-01** is **not** merely the technical transit of electronic messages between systems. The authentic clinical and enterprise value is that **clinically relevant information from heterogeneous sources is unified into a coherent, longitudinal, patient-centred clinical picture**, enabling clinicians to make timely, well-informed, and safer care decisions.

```mermaid
graph LR
    S1["S1: Clinically Relevant Info Available"] --> S2["S2: Identity & Context Established"]
    S2 --> S3["S3: Meaning Established & Preserved"]
    S3 --> S4["S4: Information Integrated Longitudinally"]
    S4 --> S5["S5: Coherent Understanding Available"]
```

#### Value Transformation Stages

1. **S1 — Clinically Relevant Information Available**:
   - *Stakeholder State*: Clinical observations, pathology reports, diagnostic imaging results, medication orders, discharge summaries, or encounter notes occur across distributed care settings and become accessible for integration.
   - *Value Generated*: Clinical events are no longer trapped within isolated source silos; they enter the integration sphere where their clinical significance can be recognized.
2. **S2 — Identity & Clinical Context Established**:
   - *Stakeholder State*: Subject-of-care identity is established to the level required for safe association of the information, preserving source identifiers, provenance, confidence, and ambiguity where applicable; relevant encounter, episode, service, and care-delivery context is associated.
   - *Value Generated*: Clinical facts are safely bound to the correct individual and operational care context without masking identity ambiguity or making premature, unsafe identity conflations.
3. **S3 — Meaning Established & Preserved**:
   - *Stakeholder State*: Clinical concepts, classifications, and terminologies are mapped to canonical standards (e.g., SNOMED CT-AU, AMT) while strictly preserving original source clinical intent, qualifiers, and provenance.
   - *Value Generated*: Clinical data is semantically unambiguous across organisational boundaries while ensuring that the originating practitioner's original meaning and expression remain unaltered.
4. **S4 — Information Integrated Longitudinally**:
   - *Stakeholder State*: Disparate episodic facts, temporal observations, diagnostic series, and encounter histories are synthesized into a cumulative, longitudinal patient-centred record.
   - *Value Generated*: Clinical interactions are connected across time and specialty, moving beyond episodic snapshots to reveal patient trajectories, trends, and clinical history.
5. **S5 — Coherent Patient-Centred Understanding Available**:
   - *Stakeholder State*: The longitudinal clinical picture is made accessible in a contextual, consumable representation, structured for clinical review, clinical decision support, or standards-compliant query.
   - *Value Generated*: Treating clinicians and multidisciplinary care teams gain holistic, trusted situational awareness, reducing diagnostic errors, preventing duplicate testing, and improving patient outcomes.

#### Clinical Authority Boundary Guardrail

Harmonia provides **governed, vendor-neutral longitudinal clinical representation and preservation**. Harmonia does **not** claim originating clinical authority for source clinical facts. The originating EMR, laboratory information system, or practitioner retains source clinical authority; Harmonia is authoritative for its governed longitudinal aggregation, contextual relationships, and verifiable provenance trail.

#### Representative Strategic Traceability

| Traceability level | Current architectural standing |
| :--- | :--- |
| Motivation | Unresolved: the retired block does not establish a replacement stream-specific axiom set or current named Driver/Goal relationship. |
| Current Business Capability contribution | **Unresolved / not presently established.** The superseded BC relationships are retired; no relationship to BC-01 through BC-18 is supplied by identifier position or similarity. |
| Current Business Enabling contribution and contextual view | **Unresolved / not presently established.** Old view ordinals and meanings are retired; no enabling-capability or Feature relationship is inferred. |
| Existing current Enterprise Capability contributions | **Established, partial:** [EC-02 — Context Management](../capabilities/enterprise-capabilities.md#ec-02-context-management); [EC-07 — Provenance & Traceability](../capabilities/enterprise-capabilities.md#ec-07-provenance--traceability). These previously documented contributions retain their canonical meanings. Other current contributions are unresolved; the retired EC labels do not establish replacements. |

- **Strategic Courses of Action**: `COA-01` (Boundary Membrane Sovereignty), `COA-02` (Distinct Management and Durable Preservation), `COA-03` (Meaning-Centric Provenance and Traceability).

---

### VS-02: Unqualified Information → Governed, Consumable Information

#### Strategic Purpose & Value Focus

Information arriving at the healthcare enterprise boundary is frequently incomplete, syntactically inconsistent, ambiguous in authority, or lacking explicit privacy controls. Consuming or publishing unverified information risks clinical misinterpretation, privacy violations, and regulatory breach.

The strategic value created by **VS-02** is the progressive **qualification, governance, policy attachment, and durable integrity assurance of information**, transforming raw external data into trusted, policy-governed assets that can be safely published across enterprise boundaries.

```mermaid
graph LR
    S1["S1: Unqualified Information"] --> S2["S2: Qualified / Structured Info"]
    S2 --> S3["S3: Governed / Controlled Info"]
    S3 --> S4["S4: Durably Available Info"]
    S4 --> S5["S5: Published / Consumable Info"]
```

#### Value Transformation Stages

1. **S1 — Unqualified Information**:
   - *Stakeholder State*: Raw, unvalidated data streams, documents, or transactions arrive at the enterprise membrane with unverified structural conformance, incomplete provenance, and unverified authority.
   - *Value Generated*: Ingress boundaries accept external input safely without allowing malformed or unauthorized data to compromise internal environments.
2. **S2 — Qualified / Structured Information**:
   - *Stakeholder State*: Data structures are validated against canonical schemas, syntactically parsed, and reconciled into well-defined, verifiable information models.
   - *Value Generated*: Syntactic and structural ambiguity is eliminated; information adheres to predictable, conformant architectural structures.
3. **S3 — Governed / Controlled Information**:
   - *Stakeholder State*: Information is evaluated against security policies, organizational authority boundaries, consent directives, and confidentiality classifications under default-deny governance (`Themis`).
   - *Value Generated*: Information carries explicit, enforceable access policies and privacy guardrails, ensuring that sensitive healthcare data cannot be disclosed without legitimate authority.
4. **S4 — Durably Available Information**:
   - *Stakeholder State*: Information is preserved with verifiable integrity, non-destructive versioning, and comprehensive provenance, remaining resiliently queryable and recoverable.
   - *Value Generated*: Stakeholders are assured that information is protected against unauthorised alteration or loss, establishing an enduring, verifiable and auditable history of care information.
5. **S5 — Published / Consumable Information**:
   - *Stakeholder State*: Governed information is projected into standards-compliant external representations (such as FHIR R5 Core and Australian profiles) across explicit egress boundaries.
   - *Value Generated*: External consumers and presentation layers receive trusted, standards-conformant information tailored to their authorised scope without exposing internal platform mechanics.

#### Enabling Behavioural Pattern (Subordinate to Value Stream)

The progression through VS-02 is supported internally by an established architectural behavioural pattern:

$$\text{Aggregation} \longrightarrow \text{Processing} \longrightarrow \text{Persistence} \longrightarrow \text{Publishing}$$

**Crucial Distinction**: This behavioural pattern describes the internal activity contributing to the transformation. It must **not** be substituted for the value stream stage names. The value stream expresses the *evolving state and value of the information*; the behavioural pattern describes the underlying operational mechanics that support it.

#### Representative Strategic Traceability

| Traceability level | Current architectural standing |
| :--- | :--- |
| Motivation | Established existing constraints: [AX-05 — Active State and Authoritative Durable State Are Distinct](../../../architectural-axioms.md#ax-05-----active-state-and-authoritative-durable-state-are-distinct) and [AX-07 — Security Is Intrinsic to Managed Operations](../../../architectural-axioms.md#ax-07-----security-is-intrinsic-to-managed-operations). Current named Driver/Goal relationships and a replacement for the retired axiom meanings remain unresolved. |
| Current Business Capability contribution | **Unresolved / not presently established.** The superseded BC relationships are retired; no relationship to BC-01 through BC-18 is supplied by identifier position or similarity. |
| Current Business Enabling contribution and contextual view | **Unresolved / not presently established.** Old view ordinals and meanings are retired; no enabling-capability or Feature relationship is inferred. |
| Existing current Enterprise Capability contributions | **Established, partial:** [EC-02 — Context Management](../capabilities/enterprise-capabilities.md#ec-02-context-management); [EC-06 — Policy & Control](../capabilities/enterprise-capabilities.md#ec-06-policy--control); [EC-07 — Provenance & Traceability](../capabilities/enterprise-capabilities.md#ec-07-provenance--traceability). These previously documented contributions retain their canonical meanings. Other current contributions are unresolved; the retired EC labels do not establish replacements. |

- **Strategic Courses of Action**: `COA-01` (Boundary Membrane Sovereignty), `COA-02` (Distinct Management and Durable Preservation), `COA-03` (Meaning-Centric Provenance and Traceability), `COA-06` (Collaborative Reusable Capability Realisation).

---

### VS-03: Operational Need → Coordinated Activity → Resolved Outcome

#### Strategic Purpose & Value Focus

Healthcare delivery involves dynamic, distributed operational coordination—from processing an urgent laboratory order, to coordinating an inpatient bed transfer, managing clinical task escalation, or fulfilling an outpatient referral. When operational activity is disconnected from the current state of affected patients, beds, and practitioners, clinical delays and administrative failures occur.

The strategic value created by **VS-03** is the **governed, coordinated progression of operational activity together with the corresponding state of affected real-world healthcare entities** (`AX-16`). It bridges operational demand and clinical execution without premature coupling to software execution threads or queueing systems.

```mermaid
graph LR
    S1["S1: Operational Need Identified"] --> S2["S2: Required Activity Established"]
    S2 --> S3["S3: Activity Coordinated"]
    S3 --> S4["S4: Work Progressed"]
    S4 --> S5["S5: Outcome Established"]
    S5 --> S6["S6: Affected Entity State Progressed"]
```

#### Value Transformation Stages

1. **S1 — Operational Need Identified**:
   - *Stakeholder State*: A healthcare operational trigger occurs (e.g., patient admission, clinical order placed, diagnostic test requested, bed turnover required, specialist referral initiated).
   - *Value Generated*: Healthcare demands are captured systematically rather than relying on informal or uncoordinated communication.
2. **S2 — Required Activity Established**:
   - *Stakeholder State*: The activity required to address the operational need is established together with its relevant context, expected outcome, and completion conditions.
   - *Value Generated*: Vague operational requirements are translated into clear, governed, and accountable objectives with explicit success criteria.
3. **S3 — Activity Coordinated**:
   - *Stakeholder State*: Required activity is coordinated with the appropriate people, services, resources, and affected real-world entities.
   - *Value Generated*: Work is matched to capable, authorized actors and resources in harmony with real-time operational availability and clinical priorities.
4. **S4 — Work Progressed**:
   - *Stakeholder State*: Execution of the activity advances through observable, auditable lifecycle states with active oversight, milestone tracking, and escalation management.
   - *Value Generated*: Healthcare operations gain end-to-end transparency; bottlenecks, delays, and stalled activities are immediately visible and actionable.
5. **S5 — Outcome Established**:
   - *Stakeholder State*: Clinical or operational results are achieved, verified, and recorded with full attribution and provenance.
   - *Value Generated*: Activity resolution is verified against the original intent, establishing reliable evidence of care delivery.
6. **S6 — Affected Entity State Progressed**:
   - *Stakeholder State*: The state of affected real-world entities (e.g., patient clinical journey, bed occupancy status, practitioner assignment, care team schedule) advances to its next governed operational state in alignment with the activity outcome (`AX-16`).
   - *Value Generated*: The physical and organizational reality of the healthcare facility stays synchronized with system records, eliminating state drift and coordination failures.

#### Decoupling Note: Execution Constructs Below the Value Stream

Digital Twin activity is demand-driven and coordinated through the established activity-execution architecture; Digital Twin identity does not imply a dedicated or permanently active execution thread, process or runtime engine. The Twin remains the [entity-centred coordination construct](logical-component-responsibilities.md#component-7-digital-twin-entity-centred-operational-coordination-construct).

Activity execution constructs and downstream mechanisms (illustratively, worker pools, messaging queues and FHIR Tasks) remain **below** the Value Stream. Their names are not stage names or technology choices made by this Strategy view.

#### Representative Strategic Traceability

| Traceability level | Current architectural standing |
| :--- | :--- |
| Motivation | Established: [AX-15 — Uncertainty Is Preserved Until Resolved](../../../architectural-axioms.md#ax-15--uncertainty-is-preserved-until-resolved) and [AX-16 — Operational Activity & Entity State Progress Together](../../../architectural-axioms.md#ax-16--operational-activity--entity-state-progress-together). AX-16 is explicitly applied by this stream’s purpose and entity-state progression stage. Current named Driver/Goal relationships and replacement grounding for the other retired axiom meanings remain unresolved. |
| Current Business Capability contribution | **Unresolved / not presently established.** The superseded BC relationships are retired; no relationship to BC-01 through BC-18 is supplied by identifier position or similarity. |
| Current Business Enabling contribution and contextual view | **Unresolved / not presently established.** Old view ordinals and meanings are retired; no enabling-capability or Feature relationship is inferred. |
| Existing current Enterprise Capability contributions | **Established, partial:** [EC-02 — Context Management](../capabilities/enterprise-capabilities.md#ec-02-context-management); [EC-12 — Operational Assurance](../capabilities/enterprise-capabilities.md#ec-12-operational-assurance). These previously documented contributions retain their canonical meanings. Other current contributions are unresolved; the retired EC labels do not establish replacements. |

- **Strategic Courses of Action**: `COA-04` (Governed Asynchronous Activity Progression), `COA-05` (Entity-Centred Operational Coordination), `COA-06` (Collaborative Reusable Capability Realisation).

---

### VS-04: Distributed Clinical Knowledge → Informed Clinical Collaboration

#### Strategic Purpose & Value Focus

Healthcare is inherently collaborative. Complex patient care requires continuous multidisciplinary interaction among medical specialists, nursing staff, allied health professionals, pharmacists, and community care providers. However, clinical communication is frequently disconnected from the longitudinal patient record, leading to ungrounded decisions, fragmented care plans, and clinical risk.

The strategic value created by **VS-04** is the **synthesis of distributed clinical knowledge into contextually relevant information supporting informed multidisciplinary collaboration**. It enables care teams to communicate, consult, and coordinate in the direct presence of verified longitudinal clinical facts without altering source clinical truth.

```mermaid
graph LR
    S1["S1: Distributed Clinical Knowledge"] --> S2["S2: Relevant Info Discovered"]
    S2 --> S3["S3: Access Appropriately Governed"]
    S3 --> S4["S4: Clinical Context Established"]
    S4 --> S5["S5: Info Presented in Context"]
    S5 --> S6["S6: Informed Clinical Collaboration"]
```

#### Value Transformation Stages

1. **S1 — Distributed Clinical Knowledge**:
   - *Stakeholder State*: Clinically relevant information, historical observations, diagnostic findings, care history, and other governed clinical knowledge exist across the healthcare ecosystem.
   - *Value Generated*: Ecosystem knowledge is recognized as an active collective resource rather than isolated data fragments.
2. **S2 — Relevant Information Discovered**:
   - *Stakeholder State*: Authorized queries, alerts, and subscriptions discover clinical facts pertinent to an active patient problem, clinical event, or multidisciplinary consultation.
   - *Value Generated*: Practitioners are spared manual cross-system searching; relevant clinical facts are brought forward based on active clinical need.
3. **S3 — Access Appropriately Governed**:
   - *Stakeholder State*: Participant identities, clinical roles, care team relationships, consent policies, and default-deny governance rules are evaluated prior to disclosing clinical information.
   - *Value Generated*: Patient privacy and sensitive clinical data are rigorously safeguarded; collaboration proceeds within legitimate clinical relationships.
4. **S4 — Clinical Context Established**:
   - *Stakeholder State*: Longitudinal patient timeline context, active problems, current medication regimens, allergies, and relevant diagnostic findings are bound to the collaborative space.
   - *Value Generated*: Clinical discussions are anchored directly in objective patient history rather than fragmented recollection or unverified hearsay.
5. **S5 — Information Presented in Context**:
   - *Stakeholder State*: Verified clinical summaries, trends, and diagnostic reports are rendered seamlessly alongside collaborative communication channels without exposing internal platform mechanics.
   - *Value Generated*: Care team members share an identical, high-fidelity clinical picture, eliminating misunderstandings and information asymmetry.
6. **S6 — Informed Clinical Collaboration**:
   - *Stakeholder State*: Treating clinicians and multidisciplinary care team members engage in timely, context-rich deliberation, arriving at informed, coordinated clinical care decisions.
   - *Value Generated*: Care plans are coordinated rapidly, multidisciplinary consultation is streamlined, and patient safety is significantly enhanced.

#### Boundary Guardrail: Clinical Authority & Expertise in Collaboration

Harmonia establishes the governed information context in which clinical collaboration occurs. Harmonia explicitly maintains the following boundaries:
1. **Harmonia is not an Electronic Medical Record (EMR)**: It does not replace source departmental or enterprise EMRs.
2. **Harmonia is not the clinical decision-maker**: Clinical responsibility and decision-making authority rest entirely with the attending clinicians and care teams.
3. **Harmonia does not originate clinical assertions during collaboration**: Collaborative dialogue (e.g., in Matrix/Agora channels) does not automatically become an authoritative clinical record. Clinical practitioners bring their expertise to the collaboration; Harmonia provides the governed clinical context in which that expertise is exercised.
4. **Clinical statements require source attribution**: Any clinical order or diagnostic finding resulting from collaboration must be explicitly committed through governed clinical workflows with source practitioner attribution.

#### Representative Strategic Traceability

| Traceability level | Current architectural standing |
| :--- | :--- |
| Motivation | Established existing constraint: [AX-07 — Security Is Intrinsic to Managed Operations](../../../architectural-axioms.md#ax-07-----security-is-intrinsic-to-managed-operations). Current named Driver/Goal relationships and a replacement for the retired axiom meanings remain unresolved. |
| Current Business Capability contribution | **Unresolved / not presently established.** The superseded BC relationships are retired; no relationship to BC-01 through BC-18 is supplied by identifier position or similarity. |
| Current Business Enabling contribution and contextual view | **Unresolved / not presently established.** Old view ordinals and meanings are retired; no enabling-capability or Feature relationship is inferred. |
| Existing current Enterprise Capability contributions | **Established, partial:** [EC-02 — Context Management](../capabilities/enterprise-capabilities.md#ec-02-context-management); [EC-06 — Policy & Control](../capabilities/enterprise-capabilities.md#ec-06-policy--control). These previously documented contributions retain their canonical meanings. Other current contributions are unresolved; the retired EC labels do not establish replacements. |

- **Strategic Courses of Action**: `COA-01` (Boundary Membrane Sovereignty), `COA-03` (Meaning-Centric Provenance and Traceability), `COA-06` (Collaborative Reusable Capability Realisation).

---

## 3. Value Stream Sufficiency Boundary & Runtime Pipeline Rejection

### 3.1 Normative Sufficiency Boundary

> **The Harmonia R1.x/R2.x Strategic Value Stream model is intentionally sufficient rather than exhaustive.** It identifies the principal transformations through which Harmonia creates value within the currently defined release scope. Additional Value Streams may be introduced where future Harmonia capabilities or release objectives create materially different forms of stakeholder value.

Harmonia R1.x/R2.x restricts its strategic value streams strictly to VS-01 through VS-04. The model does not attempt to map every granular workflow across the global healthcare industry. Future releases (e.g., R3.x population health analytics or automated clinical trial matching) may introduce additional value streams through deliberate architectural evolution.

### 3.2 Formal Rejection of Historical Runtime Pipelines as Value Streams

Historical documentation and legacy architectural sketches in some integration projects have described technical pipelines resembling:

$$\text{Ingest Trigger} \longrightarrow \text{Transport \& Deduplicate} \longrightarrow \text{Orchestrate Erga} \longrightarrow \text{Update Clinical State} \longrightarrow \text{Dispatch Egress / Serve FHIR APIs}$$

**Architectural Adjudication**:
1. This sequence is **rejected** as a Strategic Value Stream.
2. It represents internal runtime component sequencing, message mechanics, and database persistence operations.
3. Presenting software runtime mechanics as strategic value streams violates ArchiMate 3.2 principles, confuses technical execution with stakeholder utility, and artificially locks architecture to transient implementation choices.
4. To the extent that this sequence has continuing relevance, its proper home is within **Domain 05 Application Architecture** and **Domain 06 Integration Architecture** as an internal message processing pipeline.

---

## 4. End-to-End Strategic Traceability Model

The intended direction for truthful re-derivation is shown below. Dashed edges describe the derivation method; they do not assert that every level is populated or that a specific catalogue element is already connected to a stream.

```mermaid
graph TD
    MOT["Current Motivation<br/>Drivers, Goals and current Axiom Register including AX-17"] -.-> VS["Current Strategic Value Stream<br/>VS-01 .. VS-04"]
    VS -.-> BC["Current Business Capability contribution<br/>Only where established"]
    BC -.-> BEC["Current Business Enabling Capability contribution<br/>Only where established"]
    BEC -.-> EC["Current Enterprise Capability contribution<br/>Only where established"]
```

The [five-region/eighteen-capability model](../capabilities/business-capabilities.md) establishes the current Business Capabilities, not their stream membership. The [Health Service Assurance derivation](../capability-maps/health-service-assurance-derivation.md) independently establishes REQ-FND-005 → BC-18 → the three assurance Business Enabling Capabilities → the approved collaborative EC contribution model, including EC-14. It establishes no Value Stream or stage relationship. BC-17, BC-18 and EC-14 stream relationships remain unresolved; no Course of Action, resource or strategic logical component allocation follows from the diagram.

### Traceability Principles

1. **Lightweight & Representative**: Traceability is maintained through clear, representative relationships rather than brittle, exhaustive $N \times M$ matrices.
2. **Truthful Contribution Traceability**: Where a Harmonia capability contributes to delivery of a Strategic Value Stream, that contribution SHOULD be explicitly traceable. Absence of an established Value Stream relationship does not invalidate an otherwise authoritative capability and SHALL NOT be completed through inference.
3. **Decoupled Evolution**: Capabilities and Courses of Action may be enhanced, refactored, or substituted over time without destabilizing the overarching stakeholder value streams.
