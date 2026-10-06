# Capability-Scoped Business Behaviour Overview

## 1. Structuring Principles

Harmonia structures all business behaviour strictly within the four-tier capability framework defined in **[Domain 02 (Strategy)](../../02-strategy/capabilities/business-enabling-capabilities.md)**:

$$\text{L1 Capability} \longrightarrow \text{L2 Capability} \longrightarrow \text{L3 Capability} \longrightarrow \text{Feature} \longrightarrow \text{Function} \longrightarrow \text{Exposed Service}$$

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

---

## 2. Derivation Conventions

For every documented capability and feature:
1. **Owning Capability / Feature**: Explicitly identified using its canonical Strategy name and identifier.
2. **Business Function**: The internal behaviour delivered *within* the capability boundary to discharge its mandate.
3. **Exposed Business Service**: Behaviour exposed *outside* the boundary to identifiable internal or external consumers.
4. **Governed Process (Where Justified)**: State progression lifecycles encompassing the activity.
5. **Information Responsibility**: The conceptual information assets owned and maintained by the capability.

---

## 3. Preservation of Ownership and Boundary Rules

- **Exposing Capability Retains Ownership**: Consuming a Business Service does not transfer ownership of the underlying function or data to the consumer.
- **Cross-Cutting Guardrail**: Shared capabilities (such as Security Control, Presentation, or Workflow Coordination) do not acquire ownership of the business payloads, activities, or entities they govern, render, or coordinate.
