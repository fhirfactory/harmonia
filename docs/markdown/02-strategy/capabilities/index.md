# Harmonia Capability Framework

## Overview & Architecture

The Harmonia Capability Framework establishes the canonical capability model for the Harmonia Health Integration Environment (HIE). In complex regional health ecosystems, an undifferentiated, single-tier capability model is insufficient: it inevitably blurs clinical business practice with system enablement, atomic software behavior, and reusable platform engineering.

Harmonia structures its capability architecture into a **five-stage vertical progression**, bridging high-level clinical governance with concrete technology realization:

```text
┌────────────────────────────────────────────────────────────────────────┐
│ 1. BUSINESS CAPABILITY TIER (18 L1s)                                   │
│    What the healthcare enterprise must be capable of doing             │
│    (Independent of systems or automation; e.g., Individual Care Delivery)│
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ requires enablement by
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ 2. BUSINESS ENABLING CAPABILITY TIER (5 Contextual Views)              │
│    What systems must enable or provide in support of the enterprise    │
│    (Authentic healthcare contexts; e.g., Patient Clinical Record)      │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ specifies atomic behaviour as
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ 3. FEATURE LEVEL                                                       │
│    Smallest useful statement of required system-enabled behaviour      │
│    (Atomic, testable, technology-neutral)                              │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ derived into reusable
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ 4. ENTERPRISE CAPABILITY TIER (EC-01 .. EC-14)                          │
│    Reusable platform capabilities used across multiple domains         │
│    (Free of middleware, concurrency plumbing, or database specifics)   │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ viewed through
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ 5. ICT FOUNDATION CAPABILITY LENSES (18 Lenses)                        │
│    Cross-cutting technical enablement considerations                   │
│    (Guides realization without becoming enterprise capabilities)       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ realized by
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ 6. TECHNOLOGY REALISATION                                              │
│    Concrete technology choices (Databases, brokers, frameworks)        │
│    (Relegated to Technology Architecture — Domain 07)                  │
└────────────────────────────────────────────────────────────────────────┘
```

---

## The Derivation Stages Explained

### 1. Business Capability Tier
Business Capabilities represent what the healthcare enterprise must be capable of doing to fulfill its healthcare mission. They exist entirely independently of IT systems, software products, or organizational structures.
- **Scope**: 18 L1 Business Capabilities organized across five natural regions: *Care & Health Delivery* (01–08), *Health Information & Digital Health* (09–12), *Research & Innovation* (13), *Health Service Management* (14–16), and *Governance & Assurance* (17–18). These contextual groupings are not capability tiers, organisational structures, application boundaries or ownership hierarchies; they are distinct from the five Business Enabling contextual views.
- **Quality Rules**: Governed by rules CM-R01 through CM-R05 (Coverage, Orthogonality, Semantic Clarity, No Junk Drawers, Global Name Uniqueness).
- **Documentation**: [Business Capabilities](business-capabilities.md).

The approved reconciliation appends 17 Health Service Governance and 18 Health Service Assurance, clarifies 06's operational quality/safety/improvement boundary, and renames 14 Health Service Direction & Stewardship. Existing unrelated semantics are preserved. Relevance classification for 17–18 and enabling derivation for 17 remain unresolved. The separately approved [Health Service Assurance derivation](../capability-maps/health-service-assurance-derivation.md) establishes only the specified BC-18 enabling and collaborative EC relationships; the aggregate progression diagram does not establish additional relationships.

### 2. Business Enabling Capability Tier
Business Enabling Capabilities define what software and information systems must enable or provide to support the enterprise healthcare capabilities. They reflect authentic healthcare operating environments rather than integration software mechanics.
- **Structure**: Organized into five contextual views:
  1. *Entity Management*: Governance and administration of practitioners, organizations, locations, services, products, and devices.
  2. *Service Administration*: Administrative progression of encounters, referrals, orders, scheduling, and billing.
  3. *Service Delivery*: Systems enablement across acute, primary, inpatient, emergency, surgical, diagnostic, and virtual care settings.
  4. *Health Service Operations*: Healthcare facility logistics, bed management, work allocation, dispatch, and discharge coordination.
  5. *Intrinsic / Shared Enablement*: Longitudinal clinical records, health information exchange, clinical collaboration, and workflow coordination.
- **Documentation**: [Business Enabling Capabilities](business-enabling-capabilities.md).

The approved [Assurance Design, Assurance Criteria Management and Governed Assurance](business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities) additionally support BC-18. Their placement within contextual views, relevance classification, Capability Tier, complete ancestry and structural Canonical IDs remain unresolved. No sixth view or additional Feature catalogue is established by this derivation.

### 3. Feature Level
Features represent the atomic target of the Business Enabling model. A Feature is the **smallest useful statement of required system-enabled behaviour** within an established owning Business Enabling Capability context. Derivation-stage position does not establish CT1 / CT2 / CT3 ancestry; affected tiers, complete ancestry and structural Canonical IDs remain unresolved.
- **Characteristics**: Independently understandable, testable in principle, technology-independent, component-independent, and implementation-independent.
- **Scope Boundary**: Defined within established Harmonia-Relevant and Harmonia-Core Capability contexts. Reference and Adjacent capabilities are not mechanically decomposed into features merely for cosmetic symmetry.
- **Documentation**: Integrated within [Business Enabling Capabilities](business-enabling-capabilities.md).

### 4. Enterprise Capability Tier (EC-01 .. EC-14)
Enterprise Capabilities represent reusable architectural functionality derived from recurring features across multiple healthcare domains by asking: *"How is this function delivered?"*
- **Scope**: 14 capabilities: the established EC-01 Managed Entity & Relationship through EC-13 Semantic Governance & Conformance, plus approved EC-14 Service Guardian.
- **Technology-Neutral**: Free of concurrency mechanisms (threads, pools), message broker constructs (topics, queues), and storage engines (PostgreSQL, Infinispan).
- **Collaborative Nature**: Cross-cutting capabilities (EC-02 Context Management, EC-06 Policy & Control, EC-07 Provenance & Traceability, EC-12 Operational Assurance) are collaborative capabilities realized across multiple components rather than centralized bottlenecks.
- **Documentation**: [Enterprise Capabilities](enterprise-capabilities.md).

EC-14 supplies the assurance-specific semantics in the [approved collaborative contribution model](../capability-maps/health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix). EC-12 retains its established operational responsibilities and limited generic operational/processing assurance contribution. No component allocation follows from EC-14's addition.

### 5. ICT Foundation Capability Lenses
The 18 ICT Foundation Capabilities function as cross-cutting technical enablement lenses rather than peers of the business enabling model. They provide the technical questions and criteria through which Enterprise Capabilities are realized.
- **Technology-Substitution Test**: Applied to distinguish enduring capabilities from transient technology choices.
- **Documentation**: [ICT Foundation Lenses](ict-foundation-lenses.md).

---

## Harmonia Relevance Classification

Established relevance assignments use the following unchanged taxonomy. Business Capabilities 01–16 retain their assignments; 17–18 have **unresolved** classification, and 17 has unresolved enabling derivation. BC-18's enabling role is established only within the approved Health Service Assurance derivation. Unresolved status is not a fifth relevance tier.

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
