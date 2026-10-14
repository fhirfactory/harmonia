# Harmonia Capability Framework

## Overview & Architecture

The Harmonia Capability Framework establishes the canonical capability model for the Harmonia Health Integration Environment (HIE). In complex regional health ecosystems, an undifferentiated, single-tier capability model is insufficient: it inevitably blurs clinical business practice with system enablement, atomic software behavior, and reusable platform engineering.

Business Capability decomposition is **CT1 → CT2 → CT3**. Business Enabling Capability decomposition is **CT1 → CT2 → CT3 → FT**; the mandatory CT3 Feature frame applies to the enabling hierarchy. BC↔BEC enablement/support is distinct from decomposition or implementation equivalence and establishes no individual mapping here. The fixed models are governed by the [Business Architecture metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#11-capability-decomposition-and-terminology). CT1 establishes the broad frame, CT2 its constituent Capabilities, CT3 the finest Capability frame required by Harmonia, and FT required system-enabled behaviour within CT3. Every Harmonia Feature SHALL have a Business Enabling CT3 frame with same-category CT2 and CT1 ancestry; Business Capability CT3s do not own Harmonia Features; Capability tiers SHALL NOT be skipped. CT3 is the intentional boundary for deriving and governing Harmonia, rather than exhaustive healthcare-enterprise architecture; CT4, CT5 and deeper tiers SHALL NOT be defined. A Feature cannot compensate for missing Capability decomposition, and meaningful CT3 frames SHALL NOT be manufactured for structural symmetry. Existing Features and unresolved ancestry remain preserved pending separate reconciliation.

`Layer ≠ Capability Tier ≠ Derivation Stage`. `L1`, `L2` and `L3` are deprecated as canonical classifications; retained catalogue labels do not allocate CT tiers or Canonical IDs. The [approved fixed-decomposition decision](../reviews/fixed-capability-decomposition-decision.md) records the supersession and reconciliation boundary.

Separately, Harmonia uses a **five-stage derivation progression**, bridging high-level clinical governance with concrete technology realization. These stages SHALL NOT be interpreted as Capability Tiers:

```text
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 1: BUSINESS CAPABILITY (18 catalogued Capabilities)               │
│    What the healthcare enterprise must be capable of doing             │
│    (Independent of systems or automation; e.g., Individual Care Delivery)│
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ requires enablement by
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 2: BUSINESS ENABLING CAPABILITY (5 Contextual Views)               │
│    What systems must enable or provide in support of the enterprise    │
│    (Authentic healthcare contexts; e.g., Patient Clinical Record)      │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ specifies atomic behaviour as
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 3: FEATURE (FT)                                                   │
│    Smallest useful statement of required system-enabled behaviour      │
│    (Atomic, testable, technology-neutral)                              │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ derived into reusable
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 4: ENTERPRISE CAPABILITY (EC-01 .. EC-14)                          │
│    Reusable platform capabilities used across multiple domains         │
│    (Free of middleware, concurrency plumbing, or database specifics)   │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ viewed through
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STAGE 5: ICT FOUNDATION CAPABILITY LENSES (18 Lenses)                   │
│    Cross-cutting technical enablement considerations                   │
│    (Guides realization without becoming enterprise capabilities)       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ realized by
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ TECHNOLOGY REALISATION                                                 │
│    Concrete technology choices (Databases, brokers, frameworks)        │
│    (Relegated to Technology Architecture — Domain 07)                  │
└────────────────────────────────────────────────────────────────────────┘
```

---

## The Derivation Stages Explained

### 1. Business Capability
Business Capabilities represent what the healthcare enterprise must be capable of doing to fulfill its healthcare mission. They exist entirely independently of IT systems, software products, or organizational structures.
- **Scope**: 18 explicitly established CT1 Business Capability domains with adjudicated CT2/CT3 branches and deliberate stopping points, organized across five natural regions: *Care & Health Delivery* (01–08), *Health Information & Digital Health* (09–12), *Research & Innovation* (13), *Health Service Management* (14–16), and *Governance & Assurance* (17–18). These contextual groupings are not capability tiers, organisational structures, application boundaries or ownership hierarchies; they are distinct from the five Business Enabling contextual views.
- **Quality Rules**: Governed by rules CM-R01 through CM-R05 (Coverage, Orthogonality, Semantic Clarity, No Junk Drawers, Global Name Uniqueness).
- **Documentation**: [Business Capabilities](business-capabilities.md).

The [canonical R1/R2 BC catalogue](business-capabilities.md) establishes the eighteen CT1 roots and its explicit CT2/CT3 parents, including the six Individual Care Delivery CT2s. Clinical Determination, Individual Care Planning and the provisional Clinical Intervention taxonomy remain without an adjudicated CT3 set; missing detail is not completed for symmetry. Care contexts and explanatory activities do not themselves create Capabilities. OI-BC-001 remains undecided; OI-BC-002 retains semantic identity with unresolved ancestry.

The current BC catalogue supersedes the historical five-region review where definitions, classifications or tier standing have subsequently changed. BC-17 classification and enabling derivation remain unresolved; BC-18 is Harmonia-Relevant, preserving its previously approved independent-assurance support. This reconciliation performs no BEC hierarchy, Feature framing, new BC↔BEC mapping or Canonical ID allocation.

### 2. Business Enabling Capability
Business Enabling Capabilities define what software and information systems must enable or provide to support the enterprise healthcare capabilities. They reflect authentic healthcare operating environments rather than integration software mechanics.
- **Structure**: Organized into five contextual views:
  1. *Entity Management*: Governance and administration of practitioners, organizations, locations, services, products, and devices.
  2. *Service Administration*: Administrative progression of encounters, referrals, orders, scheduling, and billing.
  3. *Service Delivery*: Systems enablement across acute, primary, inpatient, emergency, surgical, diagnostic, and virtual care settings.
  4. *Health Service Operations*: Healthcare facility logistics, bed management, work allocation, dispatch, and discharge coordination.
  5. *Intrinsic / Shared Enablement*: Longitudinal clinical records, health information exchange, clinical collaboration, and workflow coordination.
- **Documentation**: [Business Enabling Capabilities](business-enabling-capabilities.md).

The approved [Assurance Design, Assurance Criteria Management and Governed Assurance](business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities) additionally support BC-18. Their placement within contextual views, relevance classification, Capability Tier, complete ancestry and structural Canonical IDs remain unresolved. No sixth view or additional Feature catalogue is established by this derivation.

### 3. Feature (FT)
Features represent the atomic target of the Business Enabling model. A Feature is the **smallest useful statement of required system-enabled behaviour** within its owning CT3 Business Enabling Capability context. Derivation-stage position does not establish CT1 / CT2 / CT3 ancestry; existing Features with unresolved frames retain their established ownership evidence, and affected tiers, complete ancestry and structural Canonical IDs remain unresolved pending reconciliation.
- **Characteristics**: Independently understandable, testable in principle, technology-independent, component-independent, and implementation-independent.
- **Scope Boundary**: Defined within established Harmonia-Relevant and Harmonia-Core Capability contexts. Reference and Adjacent capabilities are not mechanically decomposed into features merely for cosmetic symmetry.
- **Documentation**: Integrated within [Business Enabling Capabilities](business-enabling-capabilities.md).

### 4. Enterprise Capability (EC-01 .. EC-14)
Enterprise Capabilities represent reusable architectural functionality derived from recurring features across multiple healthcare domains by asking: *"How is this function delivered?"*
- **Scope**: 14 capabilities: the established EC-01 Managed Entity & Relationship through EC-13 Semantic Governance & Conformance, plus approved EC-14 Service Guardian.
- **Technology-Neutral**: Free of concurrency mechanisms (threads, pools), message broker constructs (topics, queues), and storage engines (PostgreSQL, Infinispan).
- **Collaborative Nature**: Enterprise Capabilities represent bounded reusable responsibilities. Their realisation may require participation, consumption or enforcement across multiple architectural elements, but this does not imply shared semantic ownership or prohibit establishment of a responsibility centre. **Distributed participation does not imply distributed responsibility. Cross-cutting concern does not imply cross-cutting responsibility.**
- **Documentation**: [Enterprise Capabilities](enterprise-capabilities.md).

EC-14 supplies the assurance-specific semantics in the [approved collaborative contribution model](../capability-maps/health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix). EC-12 retains its established operational responsibilities and limited generic operational/processing assurance contribution. No component allocation follows from EC-14's addition.

### 5. ICT Foundation Capability Lenses
The 18 ICT Foundation Capabilities function as cross-cutting technical enablement lenses rather than peers of the business enabling model. They provide the technical questions and criteria through which Enterprise Capabilities are realized.
- **Technology-Substitution Test**: Applied to distinguish enduring capabilities from transient technology choices.
- **Documentation**: [ICT Foundation Lenses](ict-foundation-lenses.md).

---

## Harmonia Relevance Classification

The taxonomy is unchanged. Current BC classifications and their semantic rationale are recorded in the [reconciled catalogue](business-capabilities.md#classification-reconciliation-and-rationale); BC-17 classification/enablement remain unresolved. BC-18 is Harmonia-Relevant within the previously approved assurance derivation. Unresolved is a status, not a fifth relevance tier; BC classification does not reclassify BEC contexts or every BC descendant.

| Relevance Tier | Definition & Architectural Meaning |
| :--- | :--- |
| **Reference** | Exists in Harmonia's operating environment but does not materially affect Harmonia platform responsibility. |
| **Adjacent** | Harmonia consumes, represents, or supplies information around the capability but does not materially participate in its execution. |
| **Harmonia-Relevant** | Harmonia materially enables, connects, routes, coordinates, or supplies information used in performing the capability. |
| **Harmonia-Core** | Harmonia itself provides substantive, direct responsibility for the capability. |

### Preserved Architectural Principle
> **A Harmonia-Core capability may enable a Harmonia-Relevant business capability without Harmonia owning or asserting platform authority over that healthcare business domain.**

Harmonia supports the clinical and operational service; Harmonia does not perform the medicine or clinical practice.

---

## Capability Composition Formula

Harmonia capabilities are composed according to the foundational relationship:

$$\text{\bf Harmonia Capability} = \text{domain-specific enabling behaviour} + \text{reusable Enterprise Capabilities}$$

When specific combinations of Enterprise Capabilities and domain-specific behaviours recur coherently across multiple features, they indicate candidate **Strategic Logical Component boundaries** (Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, and the Digital Twin construct), as formally articulated in the [Strategic Logical Component Responsibility Model](../strategic-views/logical-component-responsibilities.md).

Detailed derivation examples (Person Identifier Resolution, Order Closed Loop, Work Allocation & Dispatch) and tier interaction dynamics are documented in the [Capability Tier Model](../capability-maps/capability-tier-model.md).

This composition convention does not allocate EC-14 or the three assurance Business Enabling Capabilities to strategic logical components. Their approved derivation stops at collaborative Enterprise Capability realisation and preserves unresolved downstream architecture.

---

## Topic Navigation

- [Strategic Value Streams](../strategic-views/strategic-value-streams.md)
- [Business Capabilities Catalogue](business-capabilities.md)
- [Five-Region Business Capability Reconciliation](../reviews/business-capability-five-region-reconciliation.md)
- [Business Enabling Capabilities Catalogue](business-enabling-capabilities.md)
- [Enterprise Capabilities Catalogue (EC-01 .. EC-14)](enterprise-capabilities.md)
- [Health Service Assurance Strategy Derivation](../capability-maps/health-service-assurance-derivation.md)
- [ICT Foundation Capability Lenses](ict-foundation-lenses.md)
- [Capability Maps & Tier Progression Model](../capability-maps/capability-tier-model.md)
- [Strategic Logical Component Responsibilities](../strategic-views/logical-component-responsibilities.md)
- [Strategic Resources Catalogue](../resources/index.md)
- [Strategic Courses of Action](../courses-of-action/index.md)
- [Domain 02 Strategy Overview](../README.md)
