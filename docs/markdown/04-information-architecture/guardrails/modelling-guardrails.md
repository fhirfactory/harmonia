# Canonical Information Architecture Modelling Guardrails

This document codifies the sixteen authoritative, repository-wide modelling guardrails for **Domain 04 — Information Architecture**. All domain models, information concepts, relationship structures, and subsequent technical designs must conform strictly to these rules.

---

### Guardrail 1: Business Meaning Precedes Representation
> **Business meaning and responsibility define the Information Concept; downstream syntax, wire payloads, or storage structures do not.**
>
> The conceptual model captures authentic healthcare and operational semantics. It must never be bent or compromised to accommodate the quirks, convenience, or limitations of a downstream serialization format or database engine.

---

### Guardrail 2: FHIR Independence
> **FHIR resources, profiles, and extensions DO NOT define or constrain Harmonia Information Architecture.**
>
> While Harmonia supports HL7® FHIR® R4/R5 as a primary external interoperability standard at its gateways (Pylai), internal Information Concepts are independent of FHIR. A FHIR resource model must never be used as a substitute for domain analysis, nor should FHIR resource boundaries dictate conceptual information boundaries.

---

### Guardrail 3: No Implementation Class or Schema Conflation
> **Information Concepts DO NOT imply Java classes, Spring beans, JPA entities, database tables, or DDL schemas.**
>
> Domain 04 defines pure semantic models. An Information Concept must not include programming language annotations (`@Entity`, `@Table`), data-type specificities (`VARCHAR(255)`, `BIGINT`), or implementation-level getters/setters.

---

### Guardrail 4: Shared Physical Mapping
> **Distinct Information Concepts MAY map to shared physical storage downstream without forfeiting their conceptual distinction.**
>
> For technical efficiency, multiple distinct conceptual entities (e.g. `Work Order`, `To Do`, `Synthetic Task`) may downstream be persisted into a unified table or processed via a common runtime envelope. Such physical consolidation is an Application/Technology Architecture decision (Domains 05/07) and does not merge or erase their conceptual separation in Domain 04.

---

### Guardrail 5: Structural Similarity ≠ Semantic Identity
> **Two Information Concepts with identical or overlapping attributes remain semantically distinct if their business meanings, lifecycles, or responsibilities differ.**
>
> For example, a `Practitioner` and a `Person` share demographic traits (names, addresses, dates of birth), but represent fundamentally distinct concepts: `Person` represents physical human identity, while `Practitioner` represents registered professional accreditation and clinical authority. They must not be conflated into a single compromised entity.

---

### Guardrail 6: Responsibility Derivation & Bounded Ownership
> **Information Concepts for which Harmonia claims architectural responsibility SHALL trace to an owning Domain 03 Information Responsibility and the Functions/Processes through which it is discharged.**
>
> Referenced, consumed, externally authoritative or contextual Information Concepts may be represented where required to discharge a traced Harmonia responsibility, but SHALL NOT thereby acquire Harmonia ownership or authority. Architectural information responsibility is a capability property and is distinct from the governance role of `Information Steward`.

---

### Guardrail 7: Non-Transfer of Responsibility
> **Accessing, querying, transporting, caching, indexing, coordinating, transforming, presenting, or persisting information NEVER transfers architectural ownership or authority.**
>
> Subsystems that intermediate data (such as Petasos messaging, Pylai gateways, Hestia active caches, or Iris dashboards) act as custodians, routers, or presenters. Semantic responsibility remains permanently with the originating capability.

---

### Guardrail 8: Role Boundary (Relationship Role ≠ Business Role)
> **A `Relationship Role` SHALL NOT create or imply a Domain 03 `Business Role`. A `Business Role` SHALL NOT be inferred merely because an entity occupies a similarly named `Relationship Role`.**
>
> Roles like *Parent*, *Child*, *Spouse*, *Emergency Contact*, or *Supervising Clinician* are local semantic roles within specific relationships. They do not represent enterprise functional business roles in Harmonia's Business Architecture.

---

### Guardrail 9: Containment vs. Membership Semantics
> **`Containment` (`Object.contains(Object)`) and `Collection Membership` are distinct relationship semantics represented through the common Information Relationship pattern. Collection Membership does not imply containment, hierarchy, composition, or ownership.**
>
> A patient belonging to a *Diabetic Cohort Collection* is not "contained" by the cohort; removing the member does not alter the patient's identity or existence. Conversely, a physical bed is structurally contained within a ward. Furthermore, Person-oriented concepts SHALL NOT use recursive containment merely to represent familial, social, care, representation or authority relationships. Those semantics SHALL use appropriate Information Relationships or Collections.

---

### Guardrail 10: Forward Authoritative Semantics
> **Relationships SHALL be authored and validated in the natural forward direction (`Source.action(Target)`); inverse navigation is a derived downstream query concern.**
>
> Relationships must not define redundant, competing reverse links that risk dual-source divergence. Forward semantics preserve clear responsibility and authority.

---

### Guardrail 11: Semantic Stages ≠ Lifecycle States
> **`Definition`, `Contextualisation`, `Fulfilment`, `Outcome`, and `Accountability` are distinct semantic concepts with independent identities, authorities, and lifecycles—NOT state transitions of a single record.**
>
> An `OfferedHealthcareService` (catalogue definition) is not the same entity as a `HealthcareServiceDelivery` (fulfilment) or an `AssuredHealthcareService` (accountability audit). They must not be collapsed into status enum values on one table.

---

### Guardrail 12: Granular Assertion-Level and Relationship-Level Authority
> **Information Authority applies to individual assertions and relationships, enabling multi-author composite health records.**
>
> Clinical records in an HIE aggregate statements from multiple independent healthcare providers, laboratories, and systems. Authority must not be restricted to whole composite documents.

---

### Guardrail 13: Concept-Specific Lifecycles
> **Harmonia SHALL NOT impose a universal lifecycle across heterogeneous Information Concepts.**
>
> Concept lifecycles must reflect their authentic business and clinical progression. Definitions version; tasks execute; documents amend; assertions verify.

---

### Guardrail 14: Authority Preservation in Assemblies
> **Assemblies and Views SHALL NOT acquire originating authority over their constituent information merely through composition, aggregation, or presentation.**
>
> An assembly owns only the rules and metadata of the composition itself; constituent facts retain their originating author, authority, and provenance bindings.

---

### Guardrail 15: No Forced Binary Simplification
> **Inherently contextual, multi-party, or N-ary relationships SHALL NOT be artificially collapsed into lossy binary links.**
>
> Complex associations involving multiple participants, specific roles, effective periods, and governing evidence must be reified as explicit association concepts rather than disconnected foreign key pairs.

---

### Guardrail 16: No Speculative Concepts
> **An Information Concept SHALL NOT be introduced merely because an external technology, standard, or database schema provides an equivalent structure.**
>
> Every concept in Domain 04 must be justified by an authentic requirement, capability, function, or information responsibility established in the authoritative baselines (Domains 01–03).
