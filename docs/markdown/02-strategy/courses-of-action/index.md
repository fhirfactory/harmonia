# Strategic Courses of Action

## Overview & Metamodel Reference

In enterprise architecture, **Courses of Action** define the strategic approaches, directions, or plans chosen by an organisation to configure its capabilities and resources in order to achieve its goals and outcomes.

Harmonia aligns with the Course of Action concept defined in **ArchiMate® 3.2** while maintaining strict technology neutrality and motivational traceability.

---

## 1. ArchiMate 3.2 Semantics & Harmonia Qualification

### ArchiMate 3.2 Course of Action Definition
According to the ArchiMate 3.2 specification:
> *"A course of action represents an approach or plan for configuring some capabilities and resources of the enterprise, in order to achieve a goal."*

Courses of action bridge the gap between **Motivation** (drivers, goals, and outcomes) and **Strategy Architecture** (capabilities and resources). They describe **how** the enterprise intends to organize its capability to meet its goals without drifting into concrete implementation designs.

### Harmonia Strategic Qualification
In Harmonia, a Course of Action is not an implementation task, a project delivery phase, or a software configuration recipe. 

Harmonia enforces that:
- Courses of Action describe **technology-neutral architectural approaches**.
- They answer fundamental strategic questions about how capability is organized and bounded across the healthcare integration environment.
- They remain enduring even if underlying software frameworks, storage products, or execution platforms are replaced.

---

## 2. Derivation Methodology & The 5-Point Quality Test

Harmonia derives its Courses of Action top-down by synthesising foundational Drivers, Strategic Goals, Business Outcomes, and Enduring Architectural Axioms from **Domain 01 (Motivation)** with the reusable Enterprise Capabilities from **Domain 02 (Strategy)**.

```text
DOMAIN 01: MOTIVATION
Drivers ──► Strategic Goals ──► Current Architectural Axiom Register (including AX-17)
                    │
                    ▼
DOMAIN 02: STRATEGY
       Courses of Action (COA-01 .. COA-06)
                    │
                    ▼
       Enterprise Capabilities (EC-01 .. EC-14)
                    │
                    ▼
       Strategic Logical Component Responsibilities
```

### The 5-Point Quality Test
Every proposed Course of Action must satisfy five mandatory quality criteria:

1. **Motivational Traceability**: Explicitly addresses one or more strategic Drivers, Goals, Business Outcomes, or Architectural Axioms in the [current authoritative register](../../../architectural-axioms.md), including AX-17 as the authority and explicit-uncertainty guardrail.
2. **Capability Influence**: Materially shapes how Enterprise Capabilities (`EC-01` through `EC-14`) are configured, scoped, and delivered.
3. **Technology Invariance**: Remains valid, meaningful, and binding even if underlying software libraries, databases, or runtime platforms are replaced.
4. **Architectural Breadth**: Spans more than a single component implementation decision, establishing broad platform-wide architectural direction.
5. **Downstream Direction**: Provides actionable architectural constraints and guidance for downstream execution domains (Domains 03 through 13).

### Negative Filter (Rejection of Implementation Mechanisms)
Candidates that describe specific technologies, software frameworks, or runtime mechanisms are immediately rejected. The following are examples of rejected candidates:
- *“Use PostgreSQL for relational storage”* — Relegated to Domain 07 (Technology Architecture).
- *“Deploy Infinispan for distributed caching”* — Relegated to Domain 07 (Technology Architecture).
- *“Use virtual threads or thread pools for concurrency”* — Relegated to Domain 05 (Application Architecture).
- *“Create a REST endpoint or MLLP listener”* — Relegated to Domain 06 (Integration Architecture).
- *“Deploy Ponos task workers”* — Ponos is a logical component; deployment belongs downstream.

---

## 3. Core Strategic Courses of Action Catalogue

Harmonia establishes six core technology-neutral Courses of Action. Their specific capability relationships remain bounded to the established EC-01 through EC-13 mappings. Generic eligibility does not establish a COA-to-EC-14 relationship; those relationships remain unresolved:

| ID | Course of Action | Summary Strategic Approach | Primary Axiom Grounding |
| :--- | :--- | :--- | :--- |
| **COA-01** | **Boundary Membrane Sovereignty** | Preserve strict separation between external standards-compliant exchange representations and internal Harmonia-governed operational semantics. | `AX-02`, `AX-03`, `AX-13`, `AX-14` |
| **COA-02** | **Distinct Management and Durable Preservation of Information and State** | Maintain a clear separation between the governed management of Harmonia information and state and its durable preservation and recovery, allowing runtime management and persistence concerns to evolve independently without compromising information integrity. | `AX-05`, `AX-09`, `AX-11`, `AX-14` |
| **COA-03** | **Meaning-Centric Provenance and Traceability** | Preserve provenance, authority, attribution and traceability for information-significant and business-significant actions and state changes, while avoiding unnecessary elevation of transient operational mechanics into enduring business evidence. | `AX-06`, `AX-07`, `AX-08`, `AX-14` |
| **COA-04** | **Governed Asynchronous Activity Progression** | Progress multi-stage operational activities through explicit, observable unit-of-work transitions coordinated with entity state, separating execution from boundary exchange. | `AX-10`, `AX-15`, `AX-16` |
| **COA-05** | **Entity-Centred Operational Coordination** | Coordinate governed information, state and operational activity around real-world healthcare entities where those entities are operationally significant in their own right and require active management, without requiring every managed entity to maintain a permanently active execution construct. | `AX-01`, `AX-11`, `AX-16` |
| **COA-06** | **Collaborative Reusable Capability Realisation** | Realize bounded reusable platform capabilities (context, policy, provenance, EC-12 Operational Assurance) collaboratively across participating components rather than through centralized, bottlenecked runtime services. | `AX-04`, `AX-07`, `AX-11` |

---

## 4. Directory Structure & Navigation

The complete specification of each Course of Action, including motivational traceability and capability mappings, is documented in:

- [Strategic Courses of Action Catalogue](strategic-courses-of-action.md): In-depth specification of COA-01 through COA-06.

To navigate across adjacent Domain 02 Strategy areas:
- [Domain 02 Strategy Overview](../README.md)
- [Strategic Resources](../resources/index.md)
- [Enterprise Capabilities](../capabilities/enterprise-capabilities.md)
- [Strategic Logical Component Responsibilities](../strategic-views/logical-component-responsibilities.md)
