# Domain 02 — Strategy

## Overview & Pedagogical Guide

Domain 02 — Strategy bridges the foundational intent established in **Domain 01 (Motivation)** with the concrete architectural models defined in **Domains 03–13 (Execution & Realisation)**.

Where Domain 01 answers **why** Harmonia exists, what pressures shape it, and which enduring axioms govern it, Domain 02 defines **how Harmonia organizes its capability to respond**.

Domain 02 establishes an authoritative, technology-neutral architecture that defines:
- **What the healthcare enterprise must do**: The enterprise healthcare business capabilities that exist in the operating environment.
- **What systems must enable**: The system-enabled healthcare capabilities required across genuine clinical, operational, and administrative settings.
- **What atomic system behaviour is required**: The atomic, testable features that constitute those enabling capabilities.
- **What should be reusable**: The cross-cutting Enterprise Capabilities that recur across clinical and operational features.
- **How technical realization is evaluated**: The ICT Foundation lenses that guide technical realization without distorting functional capability boundaries.
- **How architectural responsibility is organized**: How coherent clusters of capabilities inform the strategic logical component responsibility boundaries (elaborated in Pass B).

---

## 1. What is Strategy Architecture?

In enterprise architecture, the **Strategy** domain articulates the strategic choices, capabilities, resources, and courses of action through which an enterprise fulfills its mission and achieves its goals.

Without an explicit Strategy Architecture, technical implementations suffer from two common failure modes:
1. **Direct Translation Fallacy**: Attempting to implement high-level business goals directly in software components without first understanding the required business and enabling capabilities, resulting in brittle architectures dictated by transient vendor software.
2. **Component-Capability Conflation**: Defining capabilities in terms of existing software modules or middleware platforms (e.g., equating integration capabilities with a specific message broker or workflow engine), preventing architectural evolution and concealing true enterprise responsibilities.

Strategy Architecture provides the structured progression from enterprise healthcare needs to platform capability boundaries, ensuring that every software subsystem reflects authentic operational responsibilities.

---

## 2. Why Does Harmonia Need It?

Harmonia operates as a regional Health Integration Environment (HIE) across diverse acute, primary, community, and diagnostic care environments. The operational landscape is characterized by heterogeneous electronic medical records (EMRs), patient administration systems (PAS), laboratory information systems (LIS), and national registries.

Harmonia requires an explicit Strategy Architecture to:
- **Separate Healthcare Practice from Software Enablement**: Clarify what the healthcare enterprise does (e.g., Individual Care Delivery, Diagnostic Services) versus what Harmonia provides to enable that care (e.g., longitudinal record correlation, reliable event distribution, context propagation).
- **Prevent Conflation of Reusable Functionality with Centralized Services**: Establish that reusable capabilities (such as Context Management, Policy & Control, and Provenance) are collaborative architectural capabilities realized across components, not monolithic runtime services.
- **Enforce Technology-Neutral System Boundaries**: Ensure that integration boundaries, operational progression, and state management remain stable when underlying storage products, message brokers, or frameworks are updated or replaced.
- **Govern Architectural Evolution Across Releases**: Provide clear scope sufficiency for Harmonia 1.x and 2.x while delineating roadmap candidates (such as authoritative master-patient EMPI reconciliation) for Harmonia 3.x.

---

## 3. Relevant TOGAF and ArchiMate Concepts

Harmonia adapts the core Strategy elements defined in the **The Open Group Architecture Framework (TOGAF®)** and the **ArchiMate® 3.2 Specification**:

- **Capability**: An ability that an organization, person, or system possesses. Capabilities represent *what* is done or enabled, completely independent of *how* it is implemented or *who* executes it.
- **Resource**: An asset owned or controlled by an individual or organization (physical, informational, financial, or organizational) that enables capabilities.
- **Course of Action**: An approach, strategy, or plan for configuring capabilities and resources to achieve a goal.
- **Value Stream**: A sequence of activities that creates an overall result for a customer, stakeholder, or end user.

### Harmonia Architectural Metamodel Extensions

ArchiMate 3.2 provides a single generic `Capability` element. In complex healthcare integration, treating all capabilities at a single undifferentiated level inevitably leads to confusion between clinical business practice, software enablement, atomic functional requirements, and reusable middleware functions.

Harmonia relates distinct capability constructs through the following **derivation progression**:

```text
BUSINESS CAPABILITY TIER
What the healthcare enterprise must be capable of doing
(Independent of any IT system or automation)
             │
             ▼
BUSINESS ENABLING CAPABILITY TIER
What systems must enable or provide in support of the enterprise
(Structured across 5 authentic healthcare operating contexts)
             │
             ▼
FEATURE LEVEL
The smallest useful statement of required system-enabled behaviour
(Atomic, testable, technology-neutral)
             │
             ▼
ENTERPRISE CAPABILITY TIER (EC-01 .. EC-13)
Reusable platform functionality derived across features
(Free of middleware, concurrency plumbing, or database specifics)
             │
             ▼
ICT FOUNDATION CAPABILITY LENSES
Cross-cutting technical enablement considerations
(Framed through the 18 ICT Foundation capabilities)
```

This progression relates different architectural constructs; it does not establish CT1 / CT2 / CT3 ancestry for the affected Business Enabling Capabilities. Capability Tier, complete ancestry and structural Canonical IDs remain unresolved where not established by architecture. This derivation progression is a Harmonia architectural modeling convention. It respects ArchiMate 3.2 semantics while providing the necessary vertical traceability required for clinical safety and software engineering.

---

## 4. The Pedagogical Reading Path

To navigate from motivation through capability derivation to architectural execution, follow the top-down pedagogical reading path:

```text
      WHY?
       │  [Domain 01: Motivation]
       ▼  Axioms, Drivers, Goals, Outcomes
WHAT VALUE DOES HARMONIA CREATE?
       │  [Domain 02: Strategic Value Streams]
       ▼  VS-01 through VS-04 Normative Healthcare Value Transformations
WHAT MUST THE ENTERPRISE DO?
       │  [Domain 02: Business Capabilities]
       ▼  16 L1 Business Capabilities across 4 Natural Regions
WHAT MUST SYSTEMS ENABLE?
       │  [Domain 02: Business Enabling Capabilities]
       ▼  5 Contextual Views: Entity, Admin, Delivery, Operations, Intrinsic
WHAT SYSTEM BEHAVIOUR IS REQUIRED?
       │  [Domain 02: Features]
       ▼  Atomic, testable system-enabled behaviours in established owning contexts
WHAT SHOULD BE REUSABLE?
       │  [Domain 02: Enterprise Capabilities]
       ▼  EC-01 through EC-13 reusable platform capabilities
WHAT STRATEGIC APPROACHES GUIDE REALISATION?
       │  [Domain 02: Courses of Action]
       ▼  COA-01 through COA-06 Strategic Approaches
WHAT ENDURING STRATEGIC ASSETS ARE REQUIRED?
       │  [Domain 02: Strategic Resources]
       ▼  SR-01 through SR-03 Normative External Resources
HOW DOES HARMONIA ORGANISE LOGICAL RESPONSIBILITY?
       │  [Domain 02: Strategic Logical Components & Views]
       ▼  Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, Digital Twin
HOW ARE THOSE RESPONSIBILITIES REALISED?
          [Domains 03–13: Architecture Realisation]
          Business, Information, Application, Integration, Technology
```

---

## 5. Relationship to Adjacent Domains

Domain 02 occupies a pivotal position in the Harmonia 13-domain taxonomy:

```text
Domain 01: Motivation  ────────────────────────┐
  (Why & What must be achieved)                │
                                               ▼
Domain 02: Strategy  ──────────────────────────► Domains 03–13: Architecture Realisation
  (How Harmonia organizes its capability:        - Domain 03: Business Architecture
   Capabilities, Resources, Courses of Action,   - Domain 04: Information Architecture
   Strategic Logical Components)                 - Domain 05: Application Architecture
                                                 - Domain 06: Integration Architecture
                                                 - Domain 07: Technology Architecture
                                                 - Domains 08–13: Security, Operations, etc.
```

- **Domain 01 (Motivation) $\to$ Domain 02 (Strategy)**: Domain 01 supplies the goals, drivers, assessments, external constraints, and foundational principles (Axioms AX-01..AX-16) that justify and bound Domain 02 capabilities and courses of action.
- **Domain 02 (Strategy) $\to$ Domain 03 (Business Architecture)**: Domain 02 identifies the Business and Business Enabling capabilities; Domain 03 models the business actors, clinical roles, business processes, and organizational structures that perform them.
- **Domain 02 (Strategy) $\to$ Domain 04 (Information Architecture)**: Domain 02 identifies managed information requirements (EC-04) and candidate resources; Domain 04 formalises conceptual information meaning, semantic relationships, responsibility traceability, terminology qualifications and lifecycle semantics. FHIR interoperability profiles are downstream in Domain 06; they do not define upstream concepts.
- **Domain 02 (Strategy) $\to$ Domains 05 & 06 (Application & Integration Architecture)**: Domain 02 establishes the strategic logical component responsibility model; Domains 05 and 06 define software components, package hierarchies, boundary adapters, and wire protocols.
- **Domain 02 (Strategy) $\to$ Domain 07 (Technology Architecture)**: Domain 02 articulates the technology-neutral Enterprise Capabilities; Domain 07 selects the concrete runtime platforms, databases, cache fabrics, and execution runtimes that realize them.

---

## 6. Scope: Inclusions and Exclusions

To preserve architectural clarity, Domain 02 enforces strict boundary rules:

### What Belongs in Domain 02
- The 16 canonical L1 Business Capabilities and their Harmonia relevance classifications.
- The Business Enabling Capability model across five authentic healthcare operating contexts (Entity Management, Service Administration, Service Delivery, Health Service Operations, Intrinsic / Shared Enablement).
- Atomic Features within established Harmonia-Relevant and Harmonia-Core Capability contexts.
- The 13 technology-neutral Enterprise Capabilities (EC-01 through EC-13).
- The 18 ICT Foundation capabilities acting as cross-cutting technical enablement lenses.
- The multi-tier capability progression model and representative derivation examples.
- Strategic logical component responsibility boundaries (Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, and Digital Twin).
- Strategic Resources (SR-01 through SR-03) and formal candidate adjudications.
- Strategic Courses of Action (COA-01 through COA-06).
- Strategic Value Streams (VS-01 through VS-04) defining normative stakeholder value progression for R1.x/R2.x.

### What Is Explicitly Excluded from Domain 02
- **Motivational Foundations**: Stakeholders, drivers, operational risk assessments, goals, and architectural axioms (governed exclusively in Domain 01).
- **Physical Software Products**: Specific software libraries, engines, or products (HAPI FHIR, Infinispan, PostgreSQL, ActiveMQ Artemis, Apache Camel, Netty, Vue, Spring) must never define capabilities.
- **Runtime Concurrency Plumbing**: Thread pools, worker threads, queues, topics, socket ports, and network CIDRs (relegated to Domains 05, 06, and 07).
- **Physical Schemas & DDL**: Database table schemas, JPA entity annotations, JSON schemas, and MLLP framing bytes (application logical/software representation belongs downstream in Domain 05, exchange contracts and wire payloads in Domain 06, and physical realisation in Domain 07; persistence realisation may involve Domains 05 and 07 without a more precise allocation being established).
- **Runtime Component Topologies**: Physical container deployments, clustering configurations, and execution topologies (relegated to Domains 05 and 07).
- **Exhaustive Many-to-Many Matrices**: Large, brittle NxM mapping matrices between tiers that create maintenance overhead without architectural value.

---

## 7. Domain Navigation

The documentation for Domain 02 is organized as follows:

1. **Strategic Views & Value Streams**
   - [Strategic Views Index](strategic-views/index.md): Architectural views overview and conceptual guidelines.
   - [Strategic Value Streams](strategic-views/strategic-value-streams.md): Normative R1.x/R2.x model defining VS-01 through VS-04, value transformation stages, sufficiency boundaries, and representative traceability.
   - [Strategic Logical Component Responsibilities](strategic-views/logical-component-responsibilities.md): Comprehensive 7-component responsibility model, 6-point boundary tests, 6 critical seam validations, capability compositions, and Strategic Responsibility View.
2. **Capability Model & Catalogues**
   - [Capability Framework Index](capabilities/index.md): Multi-tier capability progression framework and directory guide.
   - [Business Capabilities](capabilities/business-capabilities.md): Complete catalogue of the 16 L1 Business Capabilities across four natural regions, with quality rules and Harmonia relevance tiers.
   - [Business Enabling Capabilities](capabilities/business-enabling-capabilities.md): Complete catalogue across 5 contextual views (Entity Management, Service Administration, Service Delivery, Health Service Operations, Intrinsic / Shared Enablement) and atomic Features.
   - [Enterprise Capabilities](capabilities/enterprise-capabilities.md): Technology-neutral specifications for EC-01 through EC-13 and multi-capability derivation methodology.
   - [ICT Foundation Lenses](capabilities/ict-foundation-lenses.md): The 18 ICT Foundation capabilities framed as cross-cutting technical enablement lenses.
3. **Capability Maps & Derivations**
   - [Capability Maps Index](capability-maps/index.md): Conceptual overview of capability mapping and tier derivation.
   - [Capability Tier Model](capability-maps/capability-tier-model.md): Detailed vertical progression definitions, composition formulas, and representative derivation examples (Person Identifier Resolution, Order Closed Loop, Work Allocation & Dispatch).
4. **Strategic Resources & Courses of Action**
   - [Strategic Resources Index](resources/index.md): ArchiMate 3.2 resource semantics, Harmonia qualification, and Strategic Significance Test.
   - [Strategic Resources Catalogue](resources/strategic-resources.md): Full evaluation of admitted resources (SR-01..SR-03) and formal candidate dispositions.
   - [Courses of Action Index](courses-of-action/index.md): Derivation methodology, 5-point quality test, and strategic approach index.
   - [Strategic Courses of Action Catalogue](courses-of-action/strategic-courses-of-action.md): Detailed specifications for COA-01 through COA-06.
