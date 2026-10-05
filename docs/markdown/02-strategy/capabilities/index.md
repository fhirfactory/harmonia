# Harmonia Capability Framework

## Overview & Architecture

The Harmonia Capability Framework establishes the canonical capability model for the Harmonia Health Integration Environment (HIE). In complex regional health ecosystems, an undifferentiated, single-tier capability model is insufficient: it inevitably blurs clinical business practice with system enablement, atomic software behavior, and reusable platform engineering.

Harmonia structures its capability architecture into a **five-stage vertical progression**, bridging high-level clinical governance with concrete technology realization:

```text
┌────────────────────────────────────────────────────────────────────────┐
│ 1. BUSINESS CAPABILITY TIER (16 L1s)                                   │
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
│ 4. ENTERPRISE CAPABILITY TIER (EC-01 .. EC-13)                         │
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

## The Four Tiers Explained

### 1. Business Capability Tier
Business Capabilities represent what the healthcare enterprise must be capable of doing to fulfill its healthcare mission. They exist entirely independently of IT systems, software products, or organizational structures.
- **Scope**: 16 L1 Business Capabilities organized across four natural regions: *Care & Health Delivery*, *Health Information & Digital Health*, *Research & Innovation*, and *Enterprise Management*.
- **Quality Rules**: Governed by rules CM-R01 through CM-R05 (Coverage, Orthogonality, Semantic Clarity, No Junk Drawers, Global Name Uniqueness).
- **Documentation**: [Business Capabilities](business-capabilities.md).

### 2. Business Enabling Capability Tier
Business Enabling Capabilities define what software and information systems must enable or provide to support the enterprise healthcare capabilities. They reflect authentic healthcare operating environments rather than integration software mechanics.
- **Structure**: Organized into five contextual views:
  1. *Entity Management*: Governance and administration of practitioners, organizations, locations, services, products, and devices.
  2. *Service Administration*: Administrative progression of encounters, referrals, orders, scheduling, and billing.
  3. *Service Delivery*: Systems enablement across acute, primary, inpatient, emergency, surgical, diagnostic, and virtual care settings.
  4. *Health Service Operations*: Healthcare facility logistics, bed management, work allocation, dispatch, and discharge coordination.
  5. *Intrinsic / Shared Enablement*: Longitudinal clinical records, health information exchange, clinical collaboration, and workflow coordination.
- **Documentation**: [Business Enabling Capabilities](business-enabling-capabilities.md).

### 3. Feature Level
Features represent the atomic target of the Business Enabling model. A Feature is the **smallest useful statement of required system-enabled behaviour** beneath an L3 Business Enabling Capability.
- **Characteristics**: Independently understandable, testable in principle, technology-independent, component-independent, and implementation-independent.
- **Scope Boundary**: Defined for all Harmonia-Relevant and Harmonia-Core L3 capabilities. Reference and Adjacent capabilities are not mechanically decomposed into features merely for cosmetic symmetry.
- **Documentation**: Integrated within [Business Enabling Capabilities](business-enabling-capabilities.md).

### 4. Enterprise Capability Tier (EC-01 .. EC-13)
Enterprise Capabilities represent reusable architectural functionality derived from recurring features across multiple healthcare domains by asking: *"How is this function delivered?"*
- **Scope**: 13 definitive capabilities (EC-01 Managed Entity & Relationship through EC-13 Semantic Governance & Conformance).
- **Technology-Neutral**: Free of concurrency mechanisms (threads, pools), message broker constructs (topics, queues), and storage engines (PostgreSQL, Infinispan).
- **Collaborative Nature**: Cross-cutting capabilities (EC-02 Context Management, EC-06 Policy & Control, EC-07 Provenance & Traceability, EC-12 Operational Assurance) are collaborative capabilities realized across multiple components rather than centralized bottlenecks.
- **Documentation**: [Enterprise Capabilities](enterprise-capabilities.md).

### 5. ICT Foundation Capability Lenses
The 18 ICT Foundation Capabilities function as cross-cutting technical enablement lenses rather than peers of the business enabling model. They provide the technical questions and criteria through which Enterprise Capabilities are realized.
- **Technology-Substitution Test**: Applied to distinguish enduring capabilities from transient technology choices.
- **Documentation**: [ICT Foundation Lenses](ict-foundation-lenses.md).

---

## Harmonia Relevance Classification

Every capability in the model is assigned an explicit Harmonia relevance tier:

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

---

## Topic Navigation

- [Strategic Value Streams](../strategic-views/strategic-value-streams.md)
- [Business Capabilities Catalogue](business-capabilities.md)
- [Business Enabling Capabilities Catalogue](business-enabling-capabilities.md)
- [Enterprise Capabilities Catalogue (EC-01 .. EC-13)](enterprise-capabilities.md)
- [ICT Foundation Capability Lenses](ict-foundation-lenses.md)
- [Capability Maps & Tier Progression Model](../capability-maps/capability-tier-model.md)
- [Strategic Logical Component Responsibilities](../strategic-views/logical-component-responsibilities.md)
- [Strategic Resources Catalogue](../resources/index.md)
- [Strategic Courses of Action](../courses-of-action/index.md)
- [Domain 02 Strategy Overview](../README.md)
