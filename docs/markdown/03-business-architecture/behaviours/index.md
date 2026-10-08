# Capability-Scoped Business Behaviour Overview

## 1. Structuring Principles

Harmonia structures business behaviour within established Capability or Feature responsibility contexts from **[Domain 02 (Strategy)](../../02-strategy/capabilities/business-enabling-capabilities.md)**. Unresolved Capability Tier, complete ancestry or structural Canonical ID does not prevent derivation from established responsibility and SHALL NOT be completed through inference. The five contextual views are organisational views, not identifier ancestors.

### 1.1 No Disconnected Global Catalogues
Business Functions and exposed Business Services are **never** defined in isolated, flat catalogues. Every function and service exists solely within the responsibility boundary of an owning Capability or Feature.

### 1.2 The Five Healthcare Contextual Views
To provide clear architectural alignment with authentic healthcare operations, business behaviour is organised across five contextual views:

```text
docs/markdown/03-business-architecture/behaviours/
├── index.md                             # This structuring overview
├── 01-entity-management.md              # Core master entities (Person, Provider, Org, Location, Service, Device)
├── 02-service-administration.md         # Clinical administrative lifecycles (Referrals, Encounters, Orders, Docs)
├── 03-service-delivery.md               # Clinical care enablement contexts (Acute, Primary, Inpatient, Virtual)
├── 04-health-service-operations.md      # Facility logistics & operations (Wards, Beds, Dispatch, Logistics)
└── 05-intrinsic-enablement.md           # Horizontal platform services (LHR, HIE, Control, Workflow, Collab)
```

The [bounded Health Service Assurance derivation](health-service-assurance.md) additionally establishes the approved **Service Assurance Modeller** and **Service Guardian** Roles and exactly five **Business Functions** within Assurance Design, Assurance Criteria Management and Governed Assurance. Its [Function definitions](health-service-assurance.md#3-direct-capability-scoped-responsibility-consequences), [responsibility model](health-service-assurance.md#2-governance-management-guardianship-and-clinical-authority) and [EC-02 / EC-14 context contribution](health-service-assurance.md#41-context-management-contribution) preserve capability ownership and authority boundaries. It remains a capability-scoped view outside the five-view placement model: no sixth view or contextual placement is assigned. Further decomposition, exposed Services, Processes, Interactions, Collaborations, formal information modelling and downstream allocation remain unresolved.

---

## 2. Derivation Conventions

For every documented capability and feature:
1. **Owning Capability / Feature**: Identified by the established architectural element; an unavailable full Canonical ID or unestablished Feature association remains explicit.
2. **Business Function**: The internal behaviour delivered *within* the capability boundary to discharge its mandate.
3. **Exposed Business Service**: Behaviour exposed *outside* the boundary to identifiable internal or external consumers.
4. **Governed Process (Where Justified)**: State progression lifecycles encompassing the activity.
5. **Information Responsibility**: The conceptual information assets owned and maintained by the capability.

Behaviour and Process descriptions are complementary. A scoped Process may elaborate checkpoints without reproducing the Behaviour stage list or redefining responsibility. Omission of a Process checkpoint from a Behaviour summary does not invalidate it; omission of Behaviour responsibility from a Process does not remove it. Scoped applicability, compound checkpoints and alternative dispositions remain explicit. See the [metamodel](../metamodel/business-architecture-metamodel.md#43-behaviour-and-process-consistency) and approved G1 K10/K11.

---

## 3. Preservation of Ownership and Boundary Rules

- **Exposing Capability Retains Ownership**: Consuming a Business Service does not transfer ownership of the underlying function or data to the consumer.
- **Cross-Cutting Guardrail**: Shared capabilities (such as Security Control, Presentation, or Workflow Coordination) do not acquire ownership of the business payloads, activities, or entities they govern, render, or coordinate.
