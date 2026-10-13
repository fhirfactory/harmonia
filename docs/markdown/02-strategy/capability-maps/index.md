# Harmonia Capability Maps & Derivation Views

## Overview

Capability mapping in Domain 02 Strategy provides visual and conceptual models that articulate how healthcare enterprise needs translate into platform capabilities, system-enabled features, and architectural responsibility boundaries.

In traditional enterprise architecture, teams often attempt to map capabilities using exhaustive $N \times M$ traceability matrices. In regional health integration, such matrices become brittle, unmaintainable, and misleading: they imply rigid, direct mechanical mappings where real-world healthcare demands dynamic composition and cross-cutting enablement.

Harmonia documents **fixed Capability decomposition** separately from the
**five-stage derivation progression** and representative derivation patterns.
`Layer ≠ Capability Tier ≠ Derivation Stage`. The fixed model is
**CT1 → CT2 → CT3 → FT**; its complete ancestry requirement does not allocate
tiers to existing unresolved elements. These models do not require exhaustive
traceability matrices:

```text
┌────────────────────────────────────────────────────────┐
│             Capability Tier Model                      │
│  - Fixed CT1 / CT2 / CT3 / FT decomposition             │
│  - Composition formula                                 │
│  - Separate five-stage derivation progression          │
│  - Representative multi-capability compositions        │
│    (Person Identifiers, Orders, Work Allocation)       │
└────────────────────────────────────────────────────────┘
```

---

## Documents in this Section

- [Strategic Value Streams](../strategic-views/strategic-value-streams.md): Normative R1.x/R2.x model defining the four principal value transformations (VS-01..VS-04), value transformation stages, and representative traceability.
- [Capability Decomposition & Derivation Model](capability-tier-model.md): Fixed CT1 → CT2 → CT3 → FT decomposition, semantic tier roles and mandatory Feature ancestry, separately from the five-stage derivation progression; composition rules, representative derivations and rationale for omitting exhaustive $N \times M$ matrices.
- [Fixed Capability Decomposition Decision](../reviews/fixed-capability-decomposition-decision.md): Approved target metamodel, rationale, bounded CT3 scope, supersession and preserved unresolved catalogue consequences.
- [Health Service Assurance Derivation](health-service-assurance-derivation.md): Approved REQ-FND-005 → BC-18 → Assurance Design, Assurance Criteria Management and Governed Assurance → collaborative Enterprise Capability realisation, including EC-14 Service Guardian; bounded first-pass contribution matrix with no component or downstream solution allocation.
- [Strategic Logical Component Responsibilities](../strategic-views/logical-component-responsibilities.md): Capability compositions describing six strategic logical components and the Digital Twin coordination construct and the canonical Strategic Responsibility View.

---

## Navigation

- [Strategy Domain Overview](../README.md)
- [Strategic Value Streams](../strategic-views/strategic-value-streams.md)
- [Capability Framework Index](../capabilities/index.md)
- [Business Capabilities](../capabilities/business-capabilities.md)
- [Business Enabling Capabilities](../capabilities/business-enabling-capabilities.md)
- [Enterprise Capabilities](../capabilities/enterprise-capabilities.md)
- [ICT Foundation Lenses](../capabilities/ict-foundation-lenses.md)
- [Strategic Logical Component Responsibilities](../strategic-views/logical-component-responsibilities.md)
- [Strategic Resources](../resources/index.md)
- [Strategic Courses of Action](../courses-of-action/index.md)
