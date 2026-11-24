# Domain 04 — Information Architecture Foundation Completion Report

**Date**: 2026-10-06  
**Status**: Completed  
**Domain**: Domain 04 (Information Architecture)

---

## 1. Overview & Purpose

This task established the canonical foundation, metamodel, reusable modelling patterns, information governance framework, and architectural guardrails for **Domain 04 — Information Architecture** within the Harmonia Health Integration Environment (HIE).

Domain 04 answers the core architectural question:
> **What information does Harmonia need to understand, govern, and manage in order to discharge the responsibilities established by the Business Architecture, and what are the semantic relationships between those information concepts?**

It bridges the business semantics established in Domain 03 with subsequent technical architectures (Domain 05 Application Architecture, Domain 06 Integration Architecture, and physical storage implementations).

---

## 2. Files Reviewed

Authoritative upstream baselines reviewed (all treated as CLOSED and FROZEN):
- `docs/markdown/01-motivation/README.md`
- `docs/markdown/01-motivation/principles/architectural-axioms.md`
- `docs/markdown/02-strategy/README.md`
- `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`
- `docs/markdown/03-business-architecture/README.md`
- `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`
- `docs/markdown/03-business-architecture/actors-roles/actors.md`
- `docs/markdown/03-business-architecture/actors-roles/roles.md`
- `docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md`
- `docs/markdown/03-business-architecture/collaborations-interactions/interactions.md`
- `docs/markdown/03-business-architecture/behaviours/01-entity-management.md`
- `docs/markdown/03-business-architecture/behaviours/02-service-administration.md`
- `docs/markdown/03-business-architecture/behaviours/03-service-delivery.md`
- `docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md`
- `docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md`
- `docs/markdown/03-business-architecture/processes/business-processes.md`
- `docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md`

---

## 3. Files Created

The canonical documentation structure was authored under `docs/markdown/04-information-architecture/`:

1. `docs/markdown/04-information-architecture/README.md`: Domain overview, governing question, pedagogical roadmap, and architectural scope.
2. `docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md`: Core metamodel, semantic categories, characteristics, and the six-way independence boundary.
3. `docs/markdown/04-information-architecture/patterns/information-relationships.md`: Reified relationship pattern, fundamental elements vs available characteristics, forward authoring, and strict Relationship Role vs Business Role separation.
4. `docs/markdown/04-information-architecture/patterns/containment-and-collections.md`: Qualified forward containment (`Object.contains(Object)`), recursive containment boundaries, and collection membership semantics (`Containment ≠ Membership`).
5. `docs/markdown/04-information-architecture/patterns/definition-to-accountability.md`: The 5-stage progression (`Definition → Contextualisation / Binding → Fulfilment → Outcome → Accountability`) with Healthcare Service and Task/Work reference models and Mermaid diagrams.
6. `docs/markdown/04-information-architecture/governance/authority-custody-provenance.md`: Information Assertion, the four pillars (Responsibility, Authority, Custody, Consumption), granular assertion/relationship-level authority, and provenance.
7. `docs/markdown/04-information-architecture/governance/information-lifecycle.md`: Rejection of universal lifecycles, concept-specific lifecycles, and transition governance.
8. `docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md`: Information Assemblies vs Information Views, authority preservation, and canonical Context Assemblies.
9. `docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md`: The 16 canonical Information Architecture modelling guardrails.
10. `docs/markdown/04-information-architecture/traceability/domain03-traceability.md`: The 4-tier derivation framework and representative references.
11. `.junie/reports/2026-10-06-domain04-information-architecture-foundation.md`: This completion report.

---

## 4. Files Modified

- `.junie/plans/domain04-information-architecture-foundation.md`: Execution plan step status updates.

---

## 5. Modelling Decisions Established

1. **Information Concept as Fundamental Unit**: Information Concepts are organized into non-hierarchical Semantic Categories (*Entity, Activity, Event, Assertion, Relationship, Definition, Collection, Assembly*) with core semantic characteristics applied where meaningful.
2. **Six-Way Semantic Boundary**: Formalized `Business Information Concept ≠ Application Data Object ≠ FHIR Resource ≠ Persistence Entity ≠ Java Class ≠ Database Table ≠ API Payload`.
3. **Reified Relationships**: Relationships are structured with fundamental elements (`Source`, `Target`, `Type`) and available characteristics (`Roles`, `Qualification`, `Effective Period`, `Authority / Evidence`, `Primacy`, `Validity / Status`, `Provenance`) used where meaningful.
4. **Relationship Role Separation**: Codified that a `Relationship Role` (e.g. *Parent, Child, Attending*) SHALL NOT create, imply, or be inferred as a Domain 03 `Business Role`.
5. **Forward Qualified Containment**: Containment is authored authoritatively forward as `Object.contains(Object)`. Recursive containment is permitted for `Organisation`, `Healthcare Location`, and `Healthcare Service`, and strictly prohibited for Person concepts.
6. **Containment vs Membership**: Documented that `Containment` and `Collection Membership` are distinct relationship semantics represented through the common Information Relationship pattern. Membership does not imply containment, hierarchy, composition, or ownership.
7. **Definition-to-Accountability Independence**: Established that Definition, Contextualisation, Fulfilment, Outcome, and Accountability are independent semantic concepts with distinct identities, authorities, and lifecycles—not state transitions on one record.
8. **Healthcare Service & Order Semantics**: Modeled `Order` as an authoritative request/direction mechanism rather than a mandatory intermediate stage in Healthcare Service delivery.
9. **Task & Work Accounting**: Defined `ReportedTask` as the formal accountable audit and reporting projection of both `FulfillmentTask` and `TaskOutcome` (`FulfillmentTask.state = Completed ≠ ReportedTask`).
10. **Granular Authority**: Established that Information Authority binds to individual assertions and relationships, enabling multi-author composite health records.
11. **Assembly Authority Preservation**: Assemblies and Views aggregate and project information without acquiring originating authority over constituent facts.
12. **Concept-Specific Lifecycles**: Rejected universal state machines in favor of concept-specific lifecycles with transition provenance.

---

## 6. Domain 03 Traceability Examples Added

Documented the canonical 4-tier derivation pattern:
$$\text{Capability / Feature} \longrightarrow \text{Function / Process} \longrightarrow \text{Information Responsibility} \longrightarrow \text{Domain 04 Concept / Relationship / Assembly}$$

Representative reference examples include:
- **Health Service Administration** $\to$ *Maintain Service / Location / Provider Map* $\to$ *Service / Location / Provider Mapping* $\to$ `OfferedHealthcareService`, `DeliverableHealthcareService`, `Healthcare Organisation`, `Healthcare Location`, `Practitioner`, and governing relationships.
- **Person Identity Administration** $\to$ *Establish / Link Person Master Identity* $\to$ *Person Identity & Identifier Correlation Graph* $\to$ `Person Master`, `Identifier Namespace Binding`, `Identity Assertion`, `Correlation Linkage Relationship`.
- **Order Administration** $\to$ *Process & Route Closed-Loop Order* $\to$ *Clinical Order & Closed-Loop Matrix* $\to$ `Order`, `Order Requisition`, `Order Routing Directive`, `Closed-Loop Correlation Relationship`.

---

## 7. Guardrails Established

Codified all 16 canonical Information Architecture guardrails in `guardrails/modelling-guardrails.md`:
1. *Business Meaning Precedes Representation*
2. *FHIR Independence*
3. *No Implementation Class / Schema Conflation*
4. *Shared Physical Mapping*
5. *Structural Similarity ≠ Semantic Identity*
6. *Responsibility Derivation*
7. *Non-Transfer of Responsibility*
8. *Role Boundary (Relationship Role ≠ Business Role)*
9. *Containment vs. Membership Semantics*
10. *Forward Authoritative Semantics*
11. *Semantic Stages ≠ Lifecycle States*
12. *Granular Assertion-Level and Relationship-Level Authority*
13. *Concept-Specific Lifecycles*
14. *Authority Preservation in Assemblies*
15. *No Forced Binary Simplification*
16. *No Speculative Concepts*

---

## 8. Issues or Ambiguities Identified

- No upstream ambiguities or blocking issues were identified. Upstream baselines in Domains 01, 02, and 03 provided clear, authoritative conceptual boundaries.

---

## 9. Explicit Confirmations

- **Upstream Immutability**: Confirmed that **Domain 01 (Motivation)**, **Domain 02 (Strategy)**, and **Domain 03 (Business Architecture)** were **NOT modified** in any way.
- **Scope Discipline**: Confirmed that no Application Architecture (Domain 05), Integration Architecture (Domain 06), FHIR resource profiles, Java classes, database schemas, or API payloads were introduced.
- **Subsequent Work**: Confirmed that detailed population of the complete Domain 04 information families/catalogues across all 110 business enabling capabilities remains reserved for subsequent bounded tasks.
