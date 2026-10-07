# Domain04 Package 2 — Step 1 Evidence Baseline and Completion Report

**Date:** 2026-10-07 (Australia/Sydney)

**Scope:** Step 1 — Establish the evidence baseline only.

**Outcome:** Evidence capture complete; stopped at G1 — evidence review pending. Step 2 and all later work are NOT STARTED.

**Review navigation:** [Eleven area baselines](#5-eleven-investigation-area-evidence-baselines); [supporting excerpts](#6-supporting-context-evidence-and-source-comparisons); [open source issues](#7-unresolved-issues-for-g1-architectural-review); [evidence limits](#8-evidence-limits-and-deferred-questions); [G1 handover and verification](#9-completion-verification-and-g1-handover).

**Governing plan:** [Approved Package 2 delivery plan](../plans/revise-domain04-package2-plan.md), Step 1 / G1. The user's current instruction approves this step and authorises only the capability-typing correction described below. The plan's earlier proposal-status wording is retained because no other rewrite was authorised.

## 1. Completion and architectural boundary

This report records what the frozen architecture actually states, where it states it, and the questions/ambiguities that evidence leaves for review. It is a reproducible evidence register and completion record, not a new source of architectural authority. Its citations point to the frozen sources; neither this report nor the approved working plan overrides them.

Domains01–03, Domain04 Foundation and Package 1 are unchanged. There are no new Package 2 Information Concepts, concept definitions, classifications, semantic relationships, cardinalities, lifecycle decisions, pattern-applicability decisions, standards mappings or realised representations. Existing concepts and relationship examples quoted from frozen sources retain their source status; quoting them does not establish a Package 2 model.

The reasoning boundary is Business Capability / Feature → Business Function / Process / Service → Business Collaboration / Interaction → Information Requirement → Information Concept / Relationship. This step captures evidence through the business elements and identifies questions; it does not derive the latter elements. The path does not require every intermediate element.

Evidence was read from documentation, not source code or anticipated implementation. Standard/product references inside exact frozen excerpts are preserved as source wording; they supply no independent derivation evidence. Missing traceability has not been filled from healthcare convention.

### 1.1 Changes made

- Replaced seven `L1: … Administration` labels in the approved plan's capability rows with `Business Enabling Capability: … Administration`: Referral, Episode & Encounter, Order, Procedure, Clinical Record, Care Plan and Clinical Communication. The other relevant capability labels already omit an invented level. No other plan text or structure changed.
- Created this completion report containing the eleven-area evidence baseline, shared catalogue evidence, frozen constraints, unresolved issues and source fingerprints.

No frozen document, original superseded plan, navigation page or family document was edited. The runtime convergence programme was not advanced.

### 1.2 Evidence status and terminology

**Direct** means a source explicitly assigns behaviour, process or information responsibility to a named capability. **Catalogue/context** means the source defines an interaction/collaboration or relevant use context but does not explicitly map it to the subject's function/service. **Insufficient** means the inspected evidence does not establish the requested boundary/trace. **Tension** preserves differing source statements without deciding equivalence or precedence by convention.

Source codes and issue labels here are report navigation references, not invented upstream architectural identifiers. Exact names, original spellings, shortened names and source element types are retained. A Business Interaction named `Clinical Collaboration` is distinct in metamodel type from the Business Enabling Capability with the same label. `Service Outcome` in I is an Interaction, not automatically the Package 1 Information Concept `ServiceOutcome`.

## 2. Source baseline and capability typing

Repository reference: `808e73a1d871050f29c64c6780032ec29d4b7834`. Source quotations refer to the working files captured on 2026-10-07; line locations and full-file SHA-256 fingerprints allow this baseline to be checked independently. All 64 protected/source files remained unchanged against the pre-step manifest.

| Code | Authoritative source |
| :--- | :--- |
| AX | [docs/architectural-axioms.md](../../docs/architectural-axioms.md) |
| BC | [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../docs/markdown/02-strategy/capabilities/business-capabilities.md) |
| CT | [docs/markdown/02-strategy/capability-maps/capability-tier-model.md](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md) |
| T | [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) |
| B-INDEX | [docs/markdown/03-business-architecture/README.md](../../docs/markdown/03-business-architecture/README.md) |
| M | [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md) |
| B | [docs/markdown/03-business-architecture/behaviours/02-service-administration.md](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md) |
| D | [docs/markdown/03-business-architecture/behaviours/03-service-delivery.md](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md) |
| H | [docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md) |
| E | [docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md) |
| P | [docs/markdown/03-business-architecture/processes/business-processes.md](../../docs/markdown/03-business-architecture/processes/business-processes.md) |
| C | [docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md) |
| I | [docs/markdown/03-business-architecture/collaborations-interactions/interactions.md](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md) |
| O | [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) |
| X | [docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md) |
| R | [docs/markdown/03-business-architecture/actors-roles/roles.md](../../docs/markdown/03-business-architecture/actors-roles/roles.md) |
| F-M | [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) |
| F-R | [docs/markdown/04-information-architecture/patterns/information-relationships.md](../../docs/markdown/04-information-architecture/patterns/information-relationships.md) |
| F-C | [docs/markdown/04-information-architecture/patterns/containment-and-collections.md](../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md) |
| F-P | [docs/markdown/04-information-architecture/patterns/definition-to-accountability.md](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md) |
| F-A | [docs/markdown/04-information-architecture/governance/authority-custody-provenance.md](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md) |
| F-L | [docs/markdown/04-information-architecture/governance/information-lifecycle.md](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md) |
| F-V | [docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md) |
| F-G | [docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md) |
| F-T | [docs/markdown/04-information-architecture/traceability/domain03-traceability.md](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md) |
| P1-P | [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md) |
| P1-R | [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md) |
| P1-O | [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md) |
| P1-L | [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md) |
| P1-S | [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md) |
| P1-D | [docs/markdown/04-information-architecture/information-families/device.md](../../docs/markdown/04-information-architecture/information-families/device.md) |

### 2.1 Typing evidence and bounded correction

BC defines the frozen set of 16 L1 **Business Capabilities**. CT separately places **Business Enabling Capability** at conceptual Tier 2; T places the named administration capabilities in View 2, Service Administration. Conceptual Tier 2 does not mean an individually established L2 hierarchy classification. Neither catalogue numbering nor a generic statement that Features occur beneath L3 capabilities assigns a named Service Administration capability an individual L1/L2/L3 level.

No explicit named hierarchy-level assignment was found for the corrected seven capabilities in the frozen Strategy documentation. Accordingly the plan uses `Business Enabling Capability`. Harmonia-Core/Harmonia-Relevant classify relevance, not hierarchy level. No mapping from these capabilities to members of the 16-L1 set is manufactured: CT §5 explicitly states that a formal many-to-many Business Capability/Business Enabling Capability mapping has not been produced.

[BC, line 50](../../docs/markdown/02-strategy/capabilities/business-capabilities.md)

> The 16 L1 Business Capabilities are organized into four natural operational regions:

[CT: Tier 2: Business Enabling Capability, lines 76–84](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md)

> - **Definition**: What software systems and information infrastructure must enable or provide to support the enterprise healthcare capabilities.
> - **Structuring Axis**: Organized into five contextual views reflecting genuine healthcare operating environments:
>   1. *Entity Management*: Governance of practitioners, organizations, locations, services, products, and devices.
>   2. *Service Administration*: Administrative progression of encounters, referrals, orders, scheduling, and billing.
>   3. *Service Delivery*: Systems enablement across clinical delivery settings (primary, acute, emergency, diagnostic, virtual).
>   4. *Health Service Operations*: Healthcare facility logistics, bed management, work allocation, dispatch, and discharge coordination.
>   5. *Intrinsic / Shared Enablement*: Longitudinal clinical records, health information exchange, clinical collaboration, and workflow coordination.
> - **Independence**: Independent of commercial software product boundaries (PAS, EMR, LIS, RIS) and internal middleware engines.

[CT, line 224](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md)

> **The Business Enabling Capability model was derived with explicit consideration of the Business Capability model and is intended to provide the system-enabled capabilities necessary to support that business landscape. A formal many-to-many mapping between Business Capabilities and Business Enabling Capabilities has not been produced, as such traceability is not required for the current Harmonia architecture and strategy objectives. Such mapping may be developed subsequently where required for benefits realisation, investment analysis, business-case development or other value-traceability purposes.**

**Unresolved frozen-source typing tension (K9):** B, D, E, H and P retain owner labels such as `L1: Referral Administration`; M §1.1 even gives `Order Administration` as an L1 example. This conflicts with the current instruction and Strategy's separation of the 16 L1 Business Capabilities from Business Enabling Capabilities. Their raw labels appear below only as faithful quotations. They are not adopted as report-level capability classifications. Frozen documents were not corrected. The authorised plan correction does not resolve this upstream metamodel inconsistency.

## 3. Applicable frozen Information Architecture constraints

These constraints apply to all eleven areas before derivation; no family-specific pattern applicability or new semantic category has been decided.

- AX-01/04: business health-information meaning and Harmonia semantics constrain machinery.
- AX-02/03: standards boundary/native-model rules concern representations; abstract analysis is not permission to invent parallel implementation models.
- AX-05/06: managed active/durable state and information authority are distinct; projection/receipt/persistence do not establish assertion credibility or originating authority.
- AX-07/08/13: governed operations, meaningful evidence and management boundaries constrain qualification and communication outcome claims.
- AX-14/15: preserve significant distinctions and uncertainty. Missing evidence remains insufficient evidence.

The relevant Foundation and Package 1 source texts are recorded below; their constraints are reused without semantic redefinition.

### 3.1 The sixteen Foundation guardrails (exact rule statements)

**Guardrail 1: Business Meaning Precedes Representation** — [F-G, lines 7–8](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Business meaning and responsibility define the Information Concept; downstream syntax, wire payloads, or storage structures do not.**

**Guardrail 2: FHIR Independence** — [F-G, lines 14–15](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **FHIR resources, profiles, and extensions DO NOT define or constrain Harmonia Information Architecture.**

**Guardrail 3: No Implementation Class or Schema Conflation** — [F-G, lines 21–22](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Information Concepts DO NOT imply Java classes, Spring beans, JPA entities, database tables, or DDL schemas.**

**Guardrail 4: Shared Physical Mapping** — [F-G, lines 28–29](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Distinct Information Concepts MAY map to shared physical storage downstream without forfeiting their conceptual distinction.**

**Guardrail 5: Structural Similarity ≠ Semantic Identity** — [F-G, lines 35–36](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Two Information Concepts with identical or overlapping attributes remain semantically distinct if their business meanings, lifecycles, or responsibilities differ.**

**Guardrail 6: Responsibility Derivation & Bounded Ownership** — [F-G, lines 42–43](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Information Concepts for which Harmonia claims architectural responsibility SHALL trace to an owning Domain 03 Information Responsibility and the Functions/Processes through which it is discharged.**

**Guardrail 7: Non-Transfer of Responsibility** — [F-G, lines 49–50](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Accessing, querying, transporting, caching, indexing, coordinating, transforming, presenting, or persisting information NEVER transfers architectural ownership or authority.**

**Guardrail 8: Role Boundary (Relationship Role ≠ Business Role)** — [F-G, lines 56–57](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **A `Relationship Role` SHALL NOT create or imply a Domain 03 `Business Role`. A `Business Role` SHALL NOT be inferred merely because an entity occupies a similarly named `Relationship Role`.**

**Guardrail 9: Containment vs. Membership Semantics** — [F-G, lines 63–64](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **`Containment` (`Object.contains(Object)`) and `Collection Membership` are distinct relationship semantics represented through the common Information Relationship pattern. Collection Membership does not imply containment, hierarchy, composition, or ownership.**

**Guardrail 10: Forward Authoritative Semantics** — [F-G, lines 70–71](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Relationships SHALL be authored and validated in the natural forward direction (`Source.action(Target)`); inverse navigation is a derived downstream query concern.**

**Guardrail 11: Semantic Stages ≠ Lifecycle States** — [F-G, lines 77–78](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **`Definition`, `Contextualisation`, `Fulfilment`, `Outcome`, and `Accountability` are distinct semantic concepts with independent identities, authorities, and lifecycles—NOT state transitions of a single record.**

**Guardrail 12: Granular Assertion-Level and Relationship-Level Authority** — [F-G, lines 84–85](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Information Authority applies to individual assertions and relationships, enabling multi-author composite health records.**

**Guardrail 13: Concept-Specific Lifecycles** — [F-G, lines 91–92](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Harmonia SHALL NOT impose a universal lifecycle across heterogeneous Information Concepts.**

**Guardrail 14: Authority Preservation in Assemblies** — [F-G, lines 98–99](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Assemblies and Views SHALL NOT acquire originating authority over their constituent information merely through composition, aggregation, or presentation.**

**Guardrail 15: No Forced Binary Simplification** — [F-G, lines 105–106](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **Inherently contextual, multi-party, or N-ary relationships SHALL NOT be artificially collapsed into lossy binary links.**

**Guardrail 16: No Speculative Concepts** — [F-G, lines 112–113](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md)

> **An Information Concept SHALL NOT be introduced merely because an external technology, standard, or database schema provides an equivalent structure.**

### 3.2 Governance, relationship, lifecycle and pattern source evidence

[F-M, line 6](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md)

> **Information architecture is derived from business meaning and responsibility. Representation does not define meaning.**

[F-A, line 7](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md)

> **Information Assertion**: A formal statement about an entity, relationship, activity, event, or circumstance made by an identifiable source within a qualified context.

[F-A, line 67](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md)

> **Information Authority SHALL be capable of attaching directly to individual assertions and relationships rather than requiring an entire composite object to share a single monolithic authority.**

[F-L, line 7](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md)

> **Harmonia does NOT impose a single universal lifecycle across Information Concepts. Lifecycles are concept-specific and governed by the business meaning and responsibilities of the owning Capability.**

[F-V, line 45](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md)

> **Assemblies and Views SHALL NOT acquire originating authority over their constituent information merely through composition, aggregation, or presentation.**

[F-P, line 40](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md)

> 2. **Pattern, Not Rigid Requirement**: The pattern provides semantic guidance. Individual domains and information families are **not required to materialise all five stages as separate Information Concepts** if business semantics in that domain do not justify them. Stages may be collapsed where appropriate.

[F-C, line 51](../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md)

> **Containment and Collection Membership are distinct relationship semantics represented through the common Information Relationship pattern.**

[F-R, line 78](../../docs/markdown/04-information-architecture/patterns/information-relationships.md)

> **A Relationship Role SHALL NOT create or imply a Business Role. A Business Role SHALL NOT be inferred merely because an entity occupies a similarly named Relationship Role.**

F-R §1.1 makes source, target and relationship type fundamental; roles, qualification, effective period, authority/evidence, primacy, validity/status and provenance are available where meaningful. F-R §§3–4 require forward authoring and preservation of meaningful multi-party associations. F-A §§2–5 distinguish responsibility/authority/custody/consumption and support provenance/qualification; R §2.5 defines Information Steward separately. F-L §3 preserves non-destructive history. These are constraints, not prepopulated Package 2 properties or relationships.

F-T §1.1 makes the Domain03 ownership matrix the authority for information responsibility and prohibits inventing upstream responsibilities. Its representative Order terminology has a separately recorded discrepancy (K3); the example is not used as canonical Business Architecture evidence.

[F-A: 2. The Four Pillars of Information Governance, lines 27–43](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md)

>
> Harmonia enforces strict separation across four distinct governance dimensions:
>
> ```text
> Information Responsibility
>     ≠ Information Authority
>     ≠ Information Custody
>     ≠ Information Consumption
> ```
>
> | Governance Dimension | Definition | Owning Context | Key Architectural Guardrails |
> | :--- | :--- | :--- | :--- |
> | **Information Responsibility** | Architectural ownership, semantic definition, and lifecycle governance of an information asset. | Derived strictly from **Domain 03 Capability responsibility**. | Non-transferable. Transits, queries, or presentations never shift responsibility. Architectural responsibility is distinct from the `Information Steward` role. |
> | **Information Authority** | The legal, clinical, professional, or organisational actor that authoritatively makes, signs, or attests to a specific assertion or relationship. | The **asserting/attesting legal entity, clinician, or registered system**. | May attach at granular assertion and relationship levels. |
> | **Information Custody** | The operational holding, durable persistence, caching, or physical storage of information instances. | The **Harmonia subsystem, database node, or cache cluster** hosting the data. | Custody implies security and preservation obligations, never semantic ownership. |
> | **Information Consumption** | The reading, querying, evaluating, aggregating, or rendering of information by a consuming service or user. | The **consuming Capability, presentation tier, or external subscriber**. | Consumption grants no authority to alter originating facts or re-author source semantics. |

[F-A: 4.1 Provenance Scope, lines 75–82](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md)

> Provenance attaches to:
> - **Assertions**: Who asserted what fact, based on what diagnostic or observational evidence, at what recorded timestamp.
> - **Relationships**: Who created, confirmed, or severed an association between concepts.
> - **Corrections & Retractions**: The authorising party, rationale, and timestamp for an erratum or status refutation.
> - **Transformations & Derivations**: The algorithm, translation map, or synthetic rule that derived a secondary concept from source inputs.
> - **Fulfilments & Outcomes**: The performing practitioner and device recording a clinical delivery or result.
> - **Assemblies**: The assembly definition, generating system, and snapshot timestamp of a composite view.

[F-A: 5. Semantic Qualification Dimensions, lines 86–95](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md)

>
> An Information Assertion or Relationship may be qualified across several semantic dimensions:
>
> 1. **Verification State**: The epistemic certainty of the assertion (*Confirmed*, *Provisional*, *Refuted*, *Entered-in-Error*).
> 2. **Confidence / Epistemic Strength**: The degree of clinical or algorithmic confidence (*Definite*, *Probable*, *Suspected*, *Uncertain*).
> 3. **Quality & Completeness Assessment**: Metadata assessing source record completeness, calibration status, or image resolution.
> 4. **Evidentiary Basis / Reason**: The diagnostic test, clinical observation, legal certificate, or patient report providing the foundation for the assertion.
> 5. **Qualifying Authority**: The credentialed supervisor or secondary sign-off authority validating the assertion.
> 6. **Effective Context**: Clinical setting or situational bounds under which the assertion applies (e.g. *Post-Operative Recovery*, *Fasting State*).

### 3.3 Package 1 source constraints

[P1-P, line 81](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md)

> - **Authority Invariant**: Cross-authority correlation links distinct identifier namespaces without merging or transferring their originating authorities, and without implying destructive consolidation into a single authoritative source identity.

[P1-R, line 49](../../docs/markdown/04-information-architecture/information-families/practitioner.md)

> - **Boundary Rule**: $\text{Practitioner} \neq \text{Professional Registration}$. Registration is a governed assertion concerning a Practitioner, not the definition of the Practitioner entity itself.

[P1-R, line 62](../../docs/markdown/04-information-architecture/information-families/practitioner.md)

> - **Authority Invariant**: Originates from an external regulatory authority. Harmonia records verification provenance without claiming originating registration authority.

[P1-L, line 58](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md)

> - **Architectural Boundary**: Captures what the physical care-place **is** built to support, independent of its transient operational occupancy.

[P1-S, line 97](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md)

> 1. **Associated Request Mechanism**: An `Order` is an authoritative clinical requisition, directive, or prescription requesting the delivery of a service. It is **NOT** a mandatory lifecycle stage of `Healthcare Service`.

[P1-S, line 98](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md)

> 2. **Delivery Without an Order**: Service delivery may legitimately occur without an `Order` where business or clinical semantics permit (e.g. *Emergency Resuscitation*, *Triage Assessment*, *Direct Routine Nursing Care*, *Scheduled Facility Maintenance*).

[P1-S, line 75](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md)

> - **Architectural Scope & Ownership Assessment**: Evaluates single service deliveries or aggregated cohorts across an episode of care. Because Harmonia's core operational capabilities do not claim R1 architectural ownership for statutory health insurance billing reconciliation or external accreditation governance, `AssuredHealthcareService` is designated as a **Reference / Contextual Concept** where Harmonia consumes or supports assurance interfaces without owning the external statutory authority.

[P1-D, line 91](../../docs/markdown/04-information-architecture/information-families/device.md)

> 3. **Observation Demarcation**: The device association ledger records *which* device was attached to *whom* and *where*; it does **NOT** own the continuous physiological observations or telemetry waveforms generated by the device (those belong to *Service Delivery* and the *Longitudinal Health Record*).

[P1-O: 5. Canonical Role Taxonomy & Semantic Demarcations, lines 94–109](../../docs/markdown/04-information-architecture/information-families/organisation.md)

>
> ### 5.1 Organisation Identity vs. Service Provider Role
> Harmonia maintains a strict separation between an entity and the business roles it fulfills:
>
> $$\text{Healthcare Organisation [Entity]} \xrightarrow{\text{fulfills}} \text{Service Provider [Business Role]}$$
>
> 1. **`Healthcare Organisation`**: The enduring organisational entity.
> 2. **`Service Provider`**: A Domain 03 **Business Role** assumed by an organisation (or independent practitioner entity) when offering healthcare services within a collaboration. `Service Provider` is **NOT** a Domain 04 entity class.
>
> ### 5.2 Independence of Organisational Semantics
> Harmonia strictly avoids conflating distinct organisational dimensions:
> - **Organisational Identity** $\neq$ **Service Provision**;
> - **Organisational Identity** $\neq$ **Physical Location Ownership**;
> - **Organisational Identity** $\neq$ **Regulatory Authority**;
> - **Organisational Unit** $\neq$ **Physical Ward / Care Place**.

[P1-S: 2. Domain 03 Responsibility & Traceability, lines 29–39](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md)

>
> This information family derives directly from Domain 03 Business Information Responsibilities:
>
> | Domain 03 Capability | Domain 03 Function / Feature | Domain 03 Information Responsibility | Realised Domain 04 Concepts |
> | :--- | :--- | :--- | :--- |
> | **`L1: Health Service Administration`** | `Maintain Healthcare Service Definition`, `Maintain Service / Location / Provider Map`, `Maintain Service Availability`, `Maintain Service Eligibility Rules` | `Service Catalogue & Service/Location/Provider Map`, `Service Availability Schedules`, `Service Eligibility Rule Sets` | `OfferedHealthcareService`, `DeliverableHealthcareService`, `Availability Schedule`, `Eligibility Rule Set` |
> | **Performing Clinical / Operational Capabilities** *(e.g. `L1: Inpatient Care Enablement`, `L1: Diagnostic Administration`)* | Service execution and clinical delivery functions | Execution records and clinical activity outputs | `HealthcareServiceDelivery` |
> | **Diagnostic / Clinical Capabilities** | Result reporting, diagnostic interpretation, and outcome logging | Clinical findings, diagnostic reports, and operational outputs | `ServiceOutcome` |
> | **Clinical Governance / Quality & Assurance** *(Reference / Contextual)* | Audit review and quality compliance monitoring | Compliance audit ledgers, assurance certifications, and funding reconciliation | `AssuredHealthcareService` *(Reference / Contextual)* |
> | **`L1: Order Administration`** | `Receive Order Request`, `Resolve Order Destination`, `Manage Order Progression`, `Coordinate Order Modification / Cancellation`, `Associate Order Outcome` | `Clinical Order Master Record`, `Closed-Loop Tracking Ledger`, `Order-Result Correlation Matrix` | `Order` *(Associated Request / Direction Mechanism)* |

[P1-P, line 135](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md)

> | **Carer Relationship** | `Person` (*Carer / Support*) | `Person` (*Care Recipient*) | *Carer Association* | Nominated primary/secondary carer supporting the individual's daily living and care needs. |

Package 1 describes a Carer Relationship supporting “care needs”. This frozen descriptive usage is additional context for §5.9/§8, without supplying a Domain03 Care Need definition, separate responsibility or a complete Condition/Problem/Care Need model. The exact Package 1 concepts and demarcations remain unchanged.

Additional fixed definitions to reuse: P1-P §§3.1–3.7 (`Person`, identity/identifiers and `Healthcare Subject Context`); P1-R §§3.1–3.7 (practitioner, registration, privileges, bindings and endpoint); P1-O §§3–5 (organisation identity/units and distinction from Service Provider capacity); P1-L §§3–5 (location/care-place and operational-state exclusion); P1-S §§3–5 (Offered/Deliverable/Delivery/Outcome/Assured and Service Provision); P1-D §§3–5 (device definition/instance/associations).

The approved plan also carries the revision instruction's explicit non-equivalences: Referral ≠ Order; Appointment ≠ Encounter; Encounter ≠ HealthcareServiceDelivery; Order ≠ Result; Medication Order ≠ Dispense ≠ Administration; Procedure Activity ≠ Procedure Documentation; Clinical Document ≠ Clinical Fact; Condition ≠ Problem ≠ Care Need; Goal ≠ Outcome; Care Plan ≠ Activity; Clinical Communication ≠ Clinical Record. These are governing constraints carried by the approved plan/user instruction, not a claim that the complete list is printed in a particular frozen Foundation file. They do not define either side completely or make Clinical Fact canonical.

## 4. Shared Business Architecture evidence

The following verbatim extracts avoid repeating cross-cutting source text in each area. Raw `L1:` source labels remain quoted evidence subject to K9. No cross-cutting consumer thereby owns the business information it uses.

[M, line 61](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md)

> Therefore, **ownership of a Function does not imply ownership of every information item upon which that Function operates**.

[O, line 8](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

> **Business information ownership derives strictly from Capability responsibility and the Functions/Processes through which that responsibility is discharged.**

[O, line 11](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

> Consuming, searching, transporting, caching, indexing, coordinating, presenting, or physically persisting information by another capability **never transfers architectural ownership**:

[D, line 8](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> **Harmonia enables, informs, and coordinates healthcare delivery across care boundaries; it does not practice medicine, prescribe pharmaceuticals, make autonomous clinical decisions, or replace clinician judgment.**

[E: 2.1 Patient Clinical Record, lines 32–45](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Patient Clinical Record`
> - **Architectural Mandate**: Governs the canonical, longitudinal, cross-institutional health record for every patient across the healthcare region.
> - **Functions & Exposed Services**:
>   - **Feature: Longitudinal Health Record (LHR) Assembly**:
>     - *Function*: `Assemble Longitudinal Clinical Record` — Synthesises clinical documents, encounters, diagnostic reports, and medication histories across disparate source systems into a unified chronological clinical view.
>     - *Exposed Service*: `Longitudinal Clinical Record Query` — Discloses the comprehensive clinical timeline to authorised clinicians.
>   - **Feature: Active Clinical Record Access**:
>     - *Function*: `Maintain Active Clinical Record` — Maintains active problem lists, current medication regimens, allergies, and open clinical alerts for active care coordination.
>     - *Exposed Service*: `Active Problem & Allergy Summary Query` — Exposes immediate active clinical summaries.
>   - **Feature: Durable Clinical Record Preservation**:
>     - *Function*: `Preserve Durable Clinical Record` — Establishes durable, immutable, append-only historical truth with complete provenance and versioning.
>     - *Exposed Service*: `Historical Clinical Record Access` — Provides legal health record retrieval.
> - **Information Responsibility**: Governed Longitudinal Health Record (LHR), Canonical Clinical Timeline, Active Problem List, Immutable Historical Record Store.

[E: 2.2 Health Information Exchange (HIE), lines 49–66](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Health Information Exchange`
> - **Architectural Mandate**: Manages the ingestion, transformation, routing, addressed distribution, and publish-subscribe syndication of health information across organizational and jurisdictional boundaries.
> - **Ownership Invariant**: Health Information Exchange owns **exchange transaction state, delivery receipts, and routing policies**. It does **not** acquire ownership of the business information transported.
> - **Functions & Exposed Services**:
>   - **Feature: Direct Submission Ingestion & Delivery**:
>     - *Function*: `Receive Information Submission` — Validates and ingests standards-compliant clinical messages from external health providers.
>     - *Exposed Service*: `HIE Submission Gateway Service` — Ingress endpoint for regional data contributions.
>   - **Feature: Clinical Record Retrieval Mediation**:
>     - *Function*: `Mediate Information Retrieval` — Federates queries across participating hospital and diagnostic node repositories.
>     - *Exposed Service*: `Federated Health Information Query` — Resolves distributed record queries.
>   - **Feature: Addressed Clinical Distribution**:
>     - *Function*: `Distribute Addressed Information` — Directs point-to-point clinical documents to specific recipient practitioner endpoints.
>     - *Exposed Service*: `Addressed Document Delivery Service` — Securely delivers documents to designated recipient mailboxes.
>   - **Feature: Event-Driven Syndication**:
>     - *Function*: `Syndicate Information Change` — Broadcasts event notifications and record updates to subscribed regional care network systems.
>     - *Exposed Service*: `Clinical Event Syndication Stream` — Emits publish-subscribe clinical event feeds.
> - **Information Responsibility**: Exchange Transaction Ledger, Route Resolution Map, Subscription & Syndication Matrix.

[E: 2.3 Health Information Access, lines 70–84](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Health Information Access`
> - **Architectural Mandate**: Provides high-performance search, retrieval, and access-qualified filtering of clinical information across the longitudinal record.
> - **Ownership Invariant**: Consumes access control and consent determinations from *Health Information Control*. Search behaviour does **not** own security policy.
> - **Functions & Exposed Services**:
>   - **Feature: Federated Clinical Query**:
>     - *Function*: `Search Health Information` — Executes structured semantic searches across diagnostic reports, clinical notes, and medication histories.
>     - *Exposed Service*: `Clinical Information Search Service` — Discloses search capabilities to clinical user interfaces.
>   - **Feature: Access-Controlled Information Filtering**:
>     - *Function*: `Retrieve Health Information` — Fetches discrete clinical observations and original attachments.
>     - *Exposed Service*: `Clinical Resource Retrieval Service` — Delivers structured clinical information to requesting applications.
>   - **Feature: Access-Qualified Result Filtering**:
>     - *Function*: `Apply Access-Qualified Result Filtering` — Redacts or masks search results and clinical content based on the caller's verified security context and patient consent directives.
>     - *Exposed Service*: *(Embedded within Search and Retrieval Services)*.
> - **Information Responsibility**: Query Execution Context, Filtered Result Projection State.

[E: 2.4 Health Information Communication, lines 88–96](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Health Information Communication`
> - **Architectural Mandate**: Encapsulates standards-based health information communication to mediate technical transmission between participating parties.
> - **Ownership Invariant**: Does **not** own every business interaction communicated through it; provides protocol adaptation and secure transport mediation.
> - **Functions & Exposed Services**:
>   - **Feature: Standards-Based Boundary Exchange**:
>     - *Function*: `Mediate Standards-Based Health Information Communication` — Handles standards-based communication mediation, transaction coordination, and technical error handling.
>     - *Exposed Service*: `Standards Communication Gateway Service` — Protocol interface for external client integrations.
> - **Information Responsibility**: Transport Protocol Binding Map, Network Session State.

[E: 2.7 Clinical Collaboration, lines 134–149](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Clinical Collaboration`
> - **Architectural Mandate**: Governs real-time multidisciplinary clinical collaboration channels, patient care-team virtual workspaces, and secure clinical discussions.
> - **Key Architectural Semantics**:
>   - **Discourse vs. Record Guardrail**: Real-time collaborative discussion and messaging in team channels do **not** automatically constitute authoritative clinical health records or observations. Authoritative entries require explicit formal document submission.
> - **Functions & Exposed Services**:
>   - **Feature: Care-Team Clinical Collaboration Coordination**:
>     - *Function*: `Coordinate Care-Team Collaboration` — Manages care-team communication spaces, participant memberships, and clinical handovers.
>     - *Exposed Service*: `Clinical Collaboration Space Service` — Manages team collaboration spaces and discussions.
>   - **Feature: In-Conversation LHR Query Resolution**:
>     - *Function*: `Resolve LHR Query within Collaboration` — Contextually projects active patient summaries directly into collaborative clinical discussion channels.
>     - *Exposed Service*: `Collaboration Clinical Summary Resolution` — Embeds clinical summaries in collaboration feeds.
>   - **Feature: Zero-PHI Collaboration Metadata Governance**:
>     - *Function*: `Govern Collaboration Metadata` — Enforces privacy policies, participant access gates, and zero-PHI space metadata rules.
>     - *Exposed Service*: `Collaboration Space Governance Service` — Governs space lifecycles and membership.
> - **Information Responsibility**: Collaboration Space Metadata, Care-Team Channel Ledger, Discussion Thread Index.

[E: 2.10 Calendar Management, lines 190–201](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Calendar Management`
> - **Architectural Mandate**: Projects unified chronological timelines across appointments, on-call rosters, and scheduled operational events.
> - **Ownership Invariant**: Does **not** acquire authoritative ownership of rosters or appointment bookings (which remain owned by source scheduling and workforce systems).
> - **Functions & Exposed Services**:
>   - **Feature: Schedule Projection Integration**:
>     - *Function*: `Project Operational Schedule` — Aggregates and overlays multidisciplinary schedules onto a unified temporal view.
>     - *Exposed Service*: `Unified Calendar Projection Query` — Discloses consolidated temporal schedules.
>   - **Feature: Temporal Event Correlation**:
>     - *Function*: `Correlate Activity with Temporal Context` — Associates clinical events with shift boundaries and appointment slots.
>     - *Exposed Service*: `Temporal Context Resolution` — Resolves operational shifts.
> - **Information Responsibility**: Projected Unified Timeline, Temporal Correlation Index.

E §2.5 additionally names `Evaluate Information Access Authority` / `Policy Evaluation & Authorisation Service`, `Evaluate Consent Constraint` / `Consent Enforcement Service`, `Propagate Security Context` / `Security Context Validation Service`, and `Record Security-Significant Activity` / `Security Audit Ingress`. E §2.9 states Workflow & Activity Coordination owns generic coordination/timers, not business meaning or clinical outcome. These are supporting boundaries, not new ownership assignments for Package 2.

T's shared Features `FEAT-ISE-01`–`03` support the Patient Clinical Record source names; `FEAT-ISE-24`–`25` support Calendar Management; `FEAT-ISE-04`–`10`, `14`–`18`, `20`–`23` provide exchange/access/communication/knowledge/collaboration/workflow context. E includes additional names/behaviour that are not individually identified in T; absence of an ID is not repaired by creating one.

**Frozen process catalogue boundary:** [P §1.2, lines 18–44](../../docs/markdown/03-business-architecture/processes/business-processes.md)

>
> Harmonia recognises exactly sixteen justified principal Business Processes in R1:
>
> ```text
> Principal Business Processes (R1)
> ├── Entity & Administrative Processes
> │   ├── 1. Governed Person Identity Correction
> │   └── 2. Practitioner Verification
> ├── Clinical Lifecycle & Service Administration Processes
> │   ├── 3. Referral Progression
> │   ├── 4. Encounter Lifecycle
> │   ├── 5. Closed-Loop Order Progression
> │   └── 6. Clinical Document Lifecycle
> ├── Healthcare Facility & Logistics Operations Processes
> │   ├── 7. Theatre Case Progression
> │   ├── 8. Clinic / Operational Queue Progression
> │   ├── 9. Bed Turnover
> │   ├── 10. Operational Work Progression
> │   ├── 11. Patient Transport
> │   ├── 12. Specimen Transport
> │   └── 13. Discharge Progression
> └── Horizontal Workflow Coordination Processes
>     ├── 14. Work Order Progression (Human Doing)
>     ├── 15. To Do Progression (Human Reviewing / Deciding)
>     └── 16. Synthetic Task Progression (Automated System Work)
> ```

### 4.1 Business Interaction catalogue excerpts (type: Business Interaction)

[I, line 43](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Clinical Information Supply**: Routine, authorised transmission or contribution of clinical information, discharge summaries, or diagnostic results into the integration environment.

[I, line 44](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Clinical Information Provision**: The delivery of requested clinical record content to an authorised clinician or system.

[I, line 45](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Clinical Information Query**: A targeted request to discover or search for clinical records, observations, or history for a specific healthcare subject.

[I, line 46](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Clinical Information Review**: The structured examination, validation, or clinical evaluation of clinical record content by a practitioner.

[I, line 47](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Clinical Collaboration**: Structured, multi-party dialogue, handover messaging, or clinical commentary among care team members.

[I, line 48](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Clinical Information Notification**: An automated or operational notice informing participants that new or updated clinical information is now available.

[I, line 53](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Service Referral**: A formal request for a specialist, service provider, or clinical department to assess, manage, or assume care responsibility for a patient.

[I, line 54](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Service Request**: A formal order placed for a specific diagnostic test, therapeutic procedure, or clinical service.

[I, line 55](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Service Response**: A communication indicating the status, triage decision, scheduled timing, or acceptance disposition of a referral or request.

[I, line 56](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Service Outcome**: The formal clinical finding, procedural report, or discharge disposition resulting from completed service delivery.

[I, line 57](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Transfer of Care**: The formal transition of clinical and medico-legal accountability for a patient between care teams, wards, or organisations.

[I, line 58](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Service Coordination**: Dialogue aimed at synchronising multiple care delivery activities across clinical specialties or care settings.

[I, line 59](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Coordination Outcome**: A recorded summary of coordinated care decisions, agreed milestones, or integrated care plans.

[I, line 80](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Information Challenge**: A formal dispute or query raised regarding the accuracy, completeness, or attribution of a health record.

[I, line 81](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Information Correction**: The governed rectification, amendment, or retraction of erroneous or misattributed health information.

[I, line 82](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Information Submission**: The formal submission of clinical documents or datasets for governance validation, indexing, or archival.

[I, line 83](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Information Qualification**: The formal annotation, verification stamp, or clinical validation applied to an information asset.

[I, line 84](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Information Publishing**: The distribution of governed, approved health information into public or shared access registries.

[I, line 133](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Participation Registration**: The formal enrolment or addition of an actor into a multidisciplinary programme, registry, or care collective.

[I, line 134](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Participation Query**: A lookup to determine whether an actor is actively enrolled in a specific care programme or team.

[I, line 135](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Participation Withdrawal**: The formal removal or resignation of an actor from a participation context.

**Additional catalogue usages of finding — contextual wording, not a Finding definition:**

[I, line 128](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Review Outcome**: The documented findings, consensus decisions, and quality recommendations resulting from a clinical review.

[I, line 152](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md)

> - **Incident Report**: The formal communication and investigation findings regarding an incident.

I §3 separately distinguishes response/outcome, routine supply/formal submission, information notification/event notification, access/disclosure and participation/relationship. The catalogue names do not themselves establish a function/service-to-interaction mapping for every subject below.

### 4.2 Business Collaboration catalogue excerpts (type: Business Collaboration)

[C: 3.1 Patient Collaboration, lines 48–51](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - **Purpose**: Governs the structured, longitudinal relationship between a patient (or their authorised carer/representative) and their primary care network, health navigators, and participating health services.
> - **Participating Roles**: `Patient`, `Carer`, `Support Person`, `Advocate`, `Representative`, `Clinician`, `Care Coordinator`.
> - **Governed Activities**: Shared goal setting, patient-reported outcome tracking, consent directive establishment, and preference communication.

[C: 3.2 Practitioner Collaboration, lines 53–56](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - **Purpose**: Governs professional peer-to-peer clinical engagement, informal secondary consultations, multidisciplinary review panels, and clinical knowledge sharing across practitioner networks.
> - **Participating Roles**: `Practitioner`, `Clinician`, `Care Coordinator`, `Terminology Steward`.
> - **Governed Activities**: Peer review, diagnostic consultation, case conferencing, and shared clinical governance discussions.

[C: 3.3 Service Provider Collaboration, lines 58–61](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - **Purpose**: Governs formal inter-organisational healthcare service partnerships, diagnostic service level agreements, shared service networks, and regional commissioning alliances.
> - **Participating Roles**: `Service Provider`, `Organisation`, `Organisational Unit`, `Service Coordinator`.
> - **Governed Activities**: Service directory coordination, regional capacity sharing, diagnostic panel contracting, and cross-organisational service eligibility alignment.

[C: 3.4 Care-Team Collaboration, lines 63–66](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - **Purpose**: Governs the active, multidisciplinary clinical team assembled around a specific patient for an acute episode, chronic disease management programme, or inpatient stay.
> - **Participating Roles**: `Clinician` (Lead & Consulting), `Care Coordinator`, `Patient`, `Carer`, `Performer`.
> - **Governed Activities**: Shared care planning, real-time longitudinal health record (LHR) review, multidisciplinary ward rounds, clinical messaging, and transition-of-care handovers.

[C: 3.5 Service-Delivery Collaboration, lines 68–71](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - **Purpose**: Governs the operational and clinical partnership between requesting clinicians and service-fulfilment teams during the active execution of a complex clinical procedure, diagnostic pathway, or surgical episode.
> - **Participating Roles**: `Requester`, `Performer`, `Clinician`, `Service Coordinator`, `Ward Coordinator`.
> - **Governed Activities**: Theatre list progression, procedural preparation, perioperative care coordination, and diagnostic order closed-loop fulfilment.

[C: 3.6 Operational Collaboration, lines 73–76](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - **Purpose**: Governs facility-wide operational logistics, resource balancing, bed management, patient flow coordination, and dispatch between clinical units and non-clinical support services.
> - **Participating Roles**: `Patient Flow Coordinator`, `Bed Manager`, `Ward Coordinator`, `Work Coordinator`, `Wardsperson`.
> - **Governed Activities**: Daily operational huddles, emergency department bed pull, ward turnover coordination, porter dispatch, and discharge transport scheduling.

[C: 3.7 Information-Sharing Collaboration, lines 78–81](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - **Purpose**: Governs formal, trusted regional health information exchange, cross-custodian clinical data syndication, and statutory reporting networks.
> - **Participating Roles**: `Information Supplier`, `Information Client`, `Information Steward`, `Information Custodian`, `Policy Authority`, `Regulator`.
> - **Governed Activities**: Federated health information exchange, public health surveillance reporting, registry submission, and privacy policy compliance monitoring.

[C, line 29](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - Business Interactions may execute independently as discrete transactions or be structured within an overarching **Business Collaboration**.

[C, line 88](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md)

> - Do **not** create a Business Collaboration merely because two Actors interact.

C explicitly names shared goal setting, shared care planning, diagnostic consultation, diagnostic closed-loop fulfilment, messaging and transition handovers as governed activities. It does not provide a complete capability/Feature/function/interaction/collaboration traceability matrix. Subject associations below identify source relevance, not newly established architecture links.

## 5. Eleven investigation-area evidence baselines

Each baseline separates direct source statements, supporting context, catalogue scope, information-responsibility demarcations, frozen constraints and questions still open. Quoted responsibilities are upstream assets, not a proposed Package 2 concept inventory. “Questions” are deferred for review/derivation, not answered requirements or semantic decisions.

### 5.1 Referral

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 2. Referral Administration (`Harmonia-Relevant`), lines 274–278](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **2.1 Referral Intake & Progression**
>   - `FEAT-SA-03`: **Referral Document Ingestion**: Ingest structured clinical referral messages across integration boundaries.
>   - `FEAT-SA-04`: **Referral Supporting Information**: Collate and associate supporting clinical documentation, diagnostic history, and patient context with referral requests.
>   - `FEAT-SA-05`: **Referral Status & Outcome Tracking**: Track referral operational progression and communicate referral outcomes (acceptance, decline, or redirection) to referring providers.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 3. Referral Administration, lines 25–40](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Referral Administration`
> - **Key Architectural Semantics**:
>   - **Referral vs. Order**: A **Referral** concerns the *assumption or sharing of ongoing clinical care responsibility* by a receiving specialist or service provider. An **Order** concerns *bounded fulfilment of a specific diagnostic or therapeutic task*.
> - **Functions & Exposed Services**:
>   - **Feature: Referral Document Ingestion**:
>     - *Function*: `Receive Referral` — Validates and ingests structured electronic referrals, clinical indications, and priority categories.
>     - *Exposed Service*: `Referral Submission Service` — Exposed to external GP practices and referring health facilities.
>   - **Feature: Referral Supporting Information**:
>     - *Function*: `Assemble Referral Context` — Gathers relevant clinical history, medications, and diagnostic investigations into the referral package.
>     - *Exposed Service*: `Referral Context Query` — Provides comprehensive referral dossiers to intake triage clinicians.
>   - **Feature: Referral Status & Outcome Tracking**:
>     - *Function*: `Manage Referral Progression` — Tracks referral disposition across triage states: *Submitted* $\to$ *Triaged* $\to$ *Accepted/Waitlisted* $\to$ *Scheduled* $\to$ *Discharged*.
>     - *Exposed Service*: `Referral Status & Outcome Service` — Exposes progression milestones to referrers and patients.
>     - *Governed Process*: **Referral Progression Process**.
> - **Information Responsibility**: Referral Master Record, Triage Decision Ledger, Referral Disposition Log.

**Direct Business Process evidence:**

[P: 3.3 Referral Progression Process, lines 82–94](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Referral Administration`
> - **Process Purpose**: Governs the end-to-end operational progression of an incoming clinical referral from receipt through specialist clinical triage, booking, and final service acceptance.
> - **State Progression Lifecycle**:
>   $$\text{Submitted} \longrightarrow \text{Intake Validated} \longrightarrow \text{Clinically Triaged} \longrightarrow \text{Accepted / Waitlisted} \longrightarrow \text{Scheduled} \longrightarrow \text{Consultation Attended} \longrightarrow \text{Discharged / Rejected}$$
> - **Key State Dispositions**:
>   - `Submitted`: Electronic referral received from GP or external facility.
>   - `Intake Validated`: Administrative validation of patient details, mandatory fields, and tests.
>   - `Clinically Triaged`: Senior clinician assigns urgency category (e.g., Cat 1 urgent within 30 days).
>   - `Accepted / Waitlisted`: Referral accepted and placed onto the specialty waitlist.
>   - `Scheduled`: Outpatient appointment allocated and patient notified.
>   - `Consultation Attended`: Patient seen; initial specialist assessment completed.
>   - `Discharged / Rejected`: Referral completed and discharged back to primary care, or formally rejected with clinical rationale.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Referral Administration, line 42](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Referral Administration** | **Referral Master & Triage Ledger** | Incoming referral packages, clinical indications, priority triage categories, specialist acceptance decisions, and referral disposition histories. | Does not own closed-loop diagnostic or therapeutic orders. |

**Supporting behaviour / boundary sources:** [D: 2.1 Primary Care Enablement, lines 32–42](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [R: 3.2 Referral Direction is Contextual, lines 148–150](../../docs/markdown/03-business-architecture/actors-roles/roles.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

[X §2: explicit dependency rows, lines 50–51](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Referral Administration** | `Person Identifier Resolution` | **Person Identity** *(Client Admin)* | Disambiguates and validates referred patient identity against master registries. |
| **Referral Administration** | `Service Provision Resolution` | **Health Service Administration** | Resolves target clinical specialty clinics, receiving providers, and referral catchment rules. |

**Business Interaction catalogue/context:** `Service Referral`; `Service Response`; `Transfer of Care`; `Service Coordination` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Patient Collaboration`; `Care-Team Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B explicitly assigns receipt, supporting-context assembly and referral progression; P assigns an intake/triage/acceptance/booking/attendance/disposition process. O assigns referral packages, clinical indications, priorities, acceptance decisions and histories while excluding closed-loop orders.

**Responsibility, originating authority, stewardship, custody and consumption:** The referrer and triage decision-making clinicians are identified by the source descriptions. They do not establish a complete assertion-by-assertion authority allocation. Stewardship, custody and consumers are not thereby new owners; B names referrers, patients and triage clinicians as consumers.

**Applicable frozen Foundation / Package 1 constraints:** F-G 6/7/12/14; F-R role qualification; P1-P identity/source correlation, P1-R practitioner authority, P1-S existing service provision. The approved non-equivalence Referral ≠ Order remains fixed.

**Questions created by this evidence — deferred, not answered:**

- Does the wider “assess, manage, or assume” scope in I/R conflict with B/T’s ongoing-care-responsibility emphasis, or describe a different scope? (K1)
- Which supporting information remains independently sourced, and what evidence governs referral progression/disposition as distinct from clinical care responsibility?
- How do the shorter B progression and fuller P progression relate? Do they describe summary/detail or different lifecycle obligations? (K10)

**Gaps / trace strength:** Direct: B/T/P/O. Catalogue/context: I/C. No explicit mapping of every B function/service to a named I interaction or C collaboration is present. No Referral definition or referral-to-scheduling relationship is derived here.

### 5.2 Appointment / Scheduling

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 3. Scheduling Administration (`Harmonia-Relevant`), lines 280–283](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **3.1 Appointment & Booking Administration**
>   - `FEAT-SA-06`: **Appointment Notification Ingestion**: Ingest scheduled appointment events from departmental scheduling systems.
>   - `FEAT-SA-07`: **Appointment Status Synchronization**: Propagate booking cancellations, reschedules, and attendances across care coordination systems.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 4. Scheduling Administration, lines 44–55](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Scheduling Administration`
> - **Key Architectural Semantics**:
>   - **Coordination vs. Authoritative Booking**: Harmonia synchronises and communicates appointment milestones; it does **not** replace the authoritative booking/scheduling engines of host PAS or departmental scheduling platforms.
> - **Functions & Exposed Services**:
>   - **Feature: Appointment Notification Ingestion**:
>     - *Function*: `Receive Appointment Notification` — Ingests appointment creation, rescheduling, cancellation, and check-in events from host PAS engines.
>     - *Exposed Service*: `Appointment Notification Ingress` — Ingests booking events from departmental schedulers.
>   - **Feature: Appointment Status Synchronization**:
>     - *Function*: `Synchronise Appointment Status` — Projects synchronised appointment timelines to clinical dashboards and care coordinators.
>     - *Exposed Service*: `Appointment Schedule Query` — Exposes consolidated patient appointment schedules across facilities.
> - **Information Responsibility**: Appointment Event History, Schedule Synchronisation State.

**Direct Business Process evidence:**

No dedicated Appointment/Scheduling process is named in the canonical P catalogue. See the supporting-context references below; no absent process is manufactured.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Scheduling Administration, line 43](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Scheduling Administration** | **Appointment Synchronisation State** | Ingested appointment booking milestones, schedule change events, and consolidated patient appointment timeline caches. | Does not own host PAS booking engines or master clinic slots. |

[O §2: Calendar Management, line 65](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Calendar Management** | **Projected Unified Temporal Timeline** | Composite chronological schedule projections, shift boundaries, and temporal activity correlation indices. | Does not own authoritative host rosters or booking slots. |

**Supporting behaviour / boundary sources:** [E: 2.10 Calendar Management, lines 190–201](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [H: 2.1 Clinic & Practice Operations, lines 35–42](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md); [P: 3.4 Encounter Lifecycle Process, lines 98–109](../../docs/markdown/03-business-architecture/processes/business-processes.md); [P: 4.8 Clinic / Operational Queue Progression Process, lines 163–175](../../docs/markdown/03-business-architecture/processes/business-processes.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

No subject-specific row was identified in X §2; no missing dependency is invented.

**Business Interaction catalogue/context:** `Service Response`; `Service Coordination`; `Service Outcome` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Care-Team Collaboration`; `Operational Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns appointment-notification ingestion and synchronised schedule/status exposure. E assigns calendar projection and temporal correlation. O excludes source booking engines/master slots; Calendar Management’s O row excludes authoritative rosters/slots.

**Responsibility, originating authority, stewardship, custody and consumption:** Authoritative booking/scheduling systems are explicitly external in B and E. Harmonia’s synchronisation/projection responsibility is stated separately. Originating authority for individual notifications/correlations, stewardship and custody allocations are not fully specified.

**Applicable frozen Foundation / Package 1 constraints:** B/E explicitly retain external booking authority; F-G 6/7/14; F-V projection versus managed composition; P1-S availability/service binding is not automatically a patient booking. Appointment ≠ Encounter.

**Questions created by this evidence — deferred, not answered:**

- What information does Harmonia govern in synchronisation/projection, and what booking information does it only receive/reference?
- What, if any, business evidence requires an Appointment/Encounter or Appointment/delivery association? The adjacent encounter/clinic processes do not establish a universal conversion.
- How should notification currency, rescheduling/cancellation and source authority be understood without assuming booking ownership?

**Gaps / trace strength:** No dedicated Appointment/Scheduling process is named in the canonical P catalogue. The referenced encounter/clinic processes are supporting context only. The selected I/C entries are not explicit function-level mappings. No Appointment ownership or lifecycle is decided.

### 5.3 Episode / Encounter

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 4. Episode & Encounter Administration (`Harmonia-Core`), lines 285–289](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **4.1 Encounter Lifecycle Management**
>   - `FEAT-SA-08`: **Encounter Context Creation**: Create and maintain authoritative encounter tracking contexts for hospital admissions, outpatient visits, and community contacts.
>   - `FEAT-SA-09`: **Encounter State Progression**: Track encounter status transitions (`PLANNED` $\to$ `ARRIVED` $\to$ `IN_PROGRESS` $\to$ `ON_LEAVE` $\to$ `DISCHARGED`).
>   - `FEAT-SA-10`: **Encounter Bed/Location Association**: Dynamically maintain the association between an encounter, care team, and physical bed or ward location.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 5. Episode & Encounter Administration, lines 59–74](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Episode & Encounter Administration`
> - **Key Architectural Semantics**:
>   - **Encounter Context**: Tracks the clinical and administrative interaction between a patient and healthcare provider during an emergency, inpatient, or outpatient episode.
> - **Functions & Exposed Services**:
>   - **Feature: Encounter Context Creation**:
>     - *Function*: `Establish Encounter Context` — Binds patient, attending practitioner, clinical class (e.g., Inpatient, Emergency, Ambulatory), and admitting diagnosis to a unique encounter identifier.
>     - *Exposed Service*: `Encounter Context Resolution` — Discloses active encounter details to clinical applications.
>   - **Feature: Encounter State Progression**:
>     - *Function*: `Progress Encounter State` — Coordinates encounter state transitions: *Planned* $\to$ *Arrived* $\to$ *In-Progress* $\to$ *Discharged* $\to$ *Completed*.
>     - *Exposed Service*: `Encounter Lifecycle Event Notification` — Emits encounter transition events across the HIE.
>     - *Governed Process*: **Encounter Lifecycle Process**.
>   - **Feature: Encounter Bed/Location Association**:
>     - *Function*: `Maintain Encounter Care-Place Association` — Binds the encounter to physical wards, rooms, and beds over time.
>     - *Exposed Service*: `Encounter Location History Query` — Discloses patient location tracking history.
> - **Information Responsibility**: Encounter Master Record, Encounter State Transition Log, Encounter Care-Place Movement Ledger.

**Direct Business Process evidence:**

[P: 3.4 Encounter Lifecycle Process, lines 98–109](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Episode & Encounter Administration`
> - **Process Purpose**: Governs the clinical and administrative lifecycle of an acute, emergency, inpatient, or outpatient encounter between a patient and healthcare services.
> - **State Progression Lifecycle**:
>   $$\text{Planned / Booked} \longrightarrow \text{Arrived} \longrightarrow \text{Triaged / Ingested} \longrightarrow \text{Active In-Progress} \longrightarrow \text{Discharged} \longrightarrow \text{Completed / Encoded}$$
> - **Key State Dispositions**:
>   - `Planned / Booked`: Elective admission or clinic appointment scheduled.
>   - `Arrived`: Patient presents at facility or emergency desk.
>   - `Triaged / Ingested`: Clinical triage category assigned; encounter record activated.
>   - `Active In-Progress`: Inpatient care, bedside monitoring, and clinical orders actively underway.
>   - `Discharged`: Patient clinically discharged; care-place vacated.
>   - `Completed / Encoded`: Clinical documentation finalized, ICD/DRG coding completed, and encounter legally closed.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Episode & Encounter Admin, line 44](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Episode & Encounter Admin** | **Encounter Master & Movement Ledger** | Encounter identifiers, clinical class, admitting diagnoses, attending clinicians, encounter lifecycle states, and historical care-place movement logs. | Does not own longitudinal health record aggregation. |

**Supporting behaviour / boundary sources:** [D: 2.1 Primary Care Enablement, lines 32–42](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.4 Inpatient Care Enablement, lines 74–84](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.8 Community Care Enablement, lines 130–140](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.10 Remote & Virtual Care Enablement, lines 155–165](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

[X §2: explicit dependency rows, lines 52–53](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Episode & Encounter Admin** | `Care-Place Specification Lookup` | **Location Administration** | Resolves physical ward, room, and bed structural attributes for encounter bed placement. |
| **Episode & Encounter Admin** | `Bed Availability Query` | **Bed & Care-Place Management** | Checks real-time operational bed readiness before confirming patient bed moves. |

[O, line 22](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

> - An **Encounter Context** is conceptually a healthcare episode, not a FHIR `Encounter` resource or JSON wire payload.

**Business Interaction catalogue/context:** `Service Coordination`; `Transfer of Care`; `Service Outcome` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Care-Team Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns encounter establishment, state progression and temporal care-place association; P governs encounter progression; O’s shortened capability label assigns encounter master/movement information and excludes LHR aggregation. O §1.3 calls Encounter Context “conceptually a healthcare episode”.

**Responsibility, originating authority, stewardship, custody and consumption:** Responsibility for encounter tracking is explicit; supplied diagnosis/clinical assertions are not assigned a single originating authority by these sections. Episode has no separately named Feature, function, process or ownership row in the inspected sources.

**Applicable frozen Foundation / Package 1 constraints:** Appointment ≠ Encounter; Encounter ≠ HealthcareServiceDelivery; F-R temporal/n-ary context, F-G 6/7/12/14; P1-P/R/L/S reuse. F-V §2 labels Encounter Context an illustrative candidate assembly, not a complete Episode model.

**Questions created by this evidence — deferred, not answered:**

- Does “episode” in B/O/D/C denote independently governed information, an informal scope term, or context? Evidence does not determine this.
- What relationship, responsibility, provenance, meaningful lifecycle or cardinality between Episode and Encounter is actually supported? None is fixed by the area name.
- How do T’s ON_LEAVE, B’s shorter states and P’s fuller encoded/closed states relate? (K10)

**Gaps / trace strength:** Encounter behaviours are direct; Episode-specific independence is insufficiently evidenced. Community/virtual contexts are expressly present, but do not by themselves define a universal Episode boundary. No containment or cardinality is derived.

### 5.4 Order

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 5. Order Administration (`Harmonia-Core`), lines 291–296](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **5.1 Closed-Loop Order Progression**
>   - `FEAT-SA-11`: **Order Request Ingestion**: Ingest clinical diagnostic, medication, and procedure order requests with validated ordering clinician context.
>   - `FEAT-SA-12`: **Order Destination Resolution & Routing**: Resolve fulfillment endpoints and reliably route orders to performing service systems.
>   - `FEAT-SA-13`: **Order Closed-Loop Progression Tracking**: Track order progression across destination determination, routing, delivery, receipt acknowledgement (distinguishing technical delivery acknowledgement from business acknowledgement), execution progress, completion, and outcome association.
>   - `FEAT-SA-14`: **Order Cancellation & Modification Coordination**: Coordinate order cancellation and amendment requests, ensuring synchronization with performing systems.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 6. Order Administration, lines 78–100](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Order Administration`
> - **Key Architectural Semantics**:
>   - **Closed-Loop Progression**: Tracks diagnostic, pathology, imaging, and procedural orders from requisition to final result binding, ensuring zero lost orders.
>   - **Order $\neq$ Referral**: Closed-loop execution of requested investigations without transferring longitudinal care responsibility.
> - **Functions & Exposed Services**:
>   - **Feature: Order Request Ingestion**:
>     - *Function*: `Receive Order Request` — Ingests structured orders containing clinical indication, requesting clinician, and test specifications.
>     - *Exposed Service*: `Order Requisition Ingress` — Ingests electronic orders from clinical ordering systems.
>   - **Feature: Order Destination Resolution & Routing**:
>     - *Function*: `Resolve Order Destination` — Evaluates order routing rules to determine target laboratory, imaging centre, or procedural unit.
>     - *Exposed Service*: `Order Dispatch Service` — Transmits orders to performing diagnostic systems.
>   - **Feature: Order Closed-Loop Progression Tracking**:
>     - *Function*: `Manage Order Progression` — Tracks order state: *Placed* $\to$ *Received* $\to$ *Specimen Collected* $\to$ *In-Progress* $\to$ *Preliminary Result* $\to$ *Final Result* $\to$ *Closed*.
>     - *Exposed Service*: `Order Status & Tracking Query` — Provides real-time order tracking to ordering clinicians.
>     - *Governed Process*: **Closed-Loop Order Progression Process**.
>   - **Feature: Order Cancellation & Modification Coordination**:
>     - *Function*: `Coordinate Order Modification / Cancellation` — Handles requests to amend clinical details or cancel unexecuted orders.
>     - *Exposed Service*: `Order Cancellation Service` — Dispatches cancel directives to performing systems.
>   - **Feature: Order Result Association**:
>     - *Function*: `Associate Order Outcome` — Correlates returning diagnostic reports and observations back to the originating order requisition.
>     - *Exposed Service*: `Order Outcome Notification` — Emits completion notices to ordering practitioners.
> - **Information Responsibility**: Clinical Order Master Record, Closed-Loop Tracking Ledger, Order-Result Correlation Matrix.

**Direct Business Process evidence:**

[P: 3.5 Closed-Loop Order Progression Process, lines 113–125](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Order Administration`
> - **Process Purpose**: Governs the rigorous, closed-loop tracking of diagnostic pathology, radiology, and procedural orders from requisition to result correlation, preventing dropped or unfulfilled investigations.
> - **State Progression Lifecycle**:
>   $$\text{Requisition Placed} \longrightarrow \text{Order Dispatched} \longrightarrow \text{Specimen Collected / Scheduled} \longrightarrow \text{In-Execution} \longrightarrow \text{Preliminary Result Bound} \longrightarrow \text{Final Result Bound} \longrightarrow \text{Closed / Verified}$$
> - **Key State Dispositions**:
>   - `Requisition Placed`: Electronic order created and signed by requesting clinician.
>   - `Order Dispatched`: Order routed and accepted by performing diagnostic service.
>   - `Specimen Collected / Scheduled`: Bio-specimen collected or imaging appointment booked.
>   - `In-Execution`: Laboratory analysis or diagnostic scanning underway.
>   - `Preliminary Result Bound`: Critical or interim observations published and linked to order.
>   - `Final Result Bound`: Authoritative diagnostic report signed and bound to order.
>   - `Closed / Verified`: Requesting clinician acknowledges result; order closed.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Order Administration, line 45](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Order Administration** | **Clinical Order & Closed-Loop Matrix** | Diagnostic pathology/radiology requisitions, procedural orders, routing directives, closed-loop state milestones, and order-result correlation links. | Does not own diagnostic interpretations or referral assumption of care. |

**Supporting behaviour / boundary sources:** [R: 2.2 Care Delivery Roles, lines 72–82](../../docs/markdown/03-business-architecture/actors-roles/roles.md); [P1-S: Key Semantics & Invariants, lines 96–99](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

[X §2: explicit dependency rows, lines 54–55](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Order Administration** | `Practitioner Role Resolution` | **Provider Administration** | Validates requesting and attending clinician credentials and ordering privileges. |
| **Order Administration** | `Service Provision Resolution` | **Health Service Administration** | Determines performing diagnostic laboratory or imaging centre routing destinations. |

**Business Interaction catalogue/context:** `Service Request`; `Service Response`; `Service Outcome`; `Service Coordination` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Service-Delivery Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns request ingestion, destination resolution, closed-loop progression, amendment/cancellation coordination and outcome association. O excludes diagnostic interpretation and referral assumption of care. P supplies process purpose, state sequence and signed result/sign-off dispositions.

**Responsibility, originating authority, stewardship, custody and consumption:** B identifies requesting clinician and performing destinations. P identifies requesting/performing acceptance and result acknowledgement; these are process statements, not blanket originating authority for every linked result. R names Requester and Performer Business Roles.

**Applicable frozen Foundation / Package 1 constraints:** Referral ≠ Order ≠ Result; P1-S already establishes Order as an associated direction mechanism, delivery without mandatory Order and independent service-delivery meaning; F-G 6/7/12/13. F-T’s representative names are not canonical Domain03 labels (K3).

**Questions created by this evidence — deferred, not answered:**

- What scope difference exists between T’s diagnostic/medication/procedure orders and B/P’s diagnostic/pathology/imaging/procedural emphasis? (K11)
- What does the signed final-result disposition in P constrain, and which lifecycle scope does it establish? (K2/K10)
- B names Order Result Association without an individual T Feature ID. What traceability qualification is needed? (K5)

**Gaps / trace strength:** No new direction/result relationship is established. C expressly includes diagnostic closed-loop fulfilment, but no exhaustive per-function C/I mapping is present. Missing identifiers and variant Foundation names remain visible.

### 5.5 Diagnostics

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 6. Diagnostic Administration (`Harmonia-Core`), lines 298–302](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **6.1 Diagnostic Workflow Management**
>   - `FEAT-SA-15`: **Diagnostic Request Correlation**: Correlate diagnostic test orders with collected clinical specimens and patient encounters.
>   - `FEAT-SA-16`: **Diagnostic Report Ingestion & Binding**: Ingest finalized laboratory and radiology reports, verifying content integrity and binding them to the parent order.
>   - `FEAT-SA-17`: **Diagnostic Report Distribution**: Distribute finalized laboratory, pathology, and imaging reports to ordering clinicians and designated care teams.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 7. Diagnostic Administration, lines 104–119](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Diagnostic Administration`
> - **Key Architectural Semantics**:
>   - **Result Management & Distribution**: Manages the correlation, notification, and delivery of pathology, radiology, and point-of-care results.
>   - **Ownership Guardrail**: Does **not** own the clinical diagnostic interpretation or specimen analysis (which is owned by the performing diagnostic service provider/LIS).
> - **Functions & Exposed Services**:
>   - **Feature: Diagnostic Request Correlation**:
>     - *Function*: `Correlate Diagnostic Request` — Matches incoming lab/radiology results to active patient encounters and order requisitions.
>     - *Exposed Service*: `Diagnostic Correlation Resolution` — Discloses correlated order/result linkages.
>   - **Feature: Diagnostic Report Ingestion & Binding**:
>     - *Function*: `Bind Diagnostic Report` — Associates structured observations, reference ranges, and abnormal flags with patient health records.
>     - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.
>   - **Feature: Diagnostic Report Distribution**:
>     - *Function*: `Distribute Diagnostic Report` — Delivers preliminary, final, and corrected reports to ordering and copied practitioners.
>     - *Exposed Service*: `Diagnostic Result Delivery Service` — Emits secure result notifications.
> - **Information Responsibility**: Diagnostic Request-Report Linkage Registry, Result Distribution Ledger.

**Direct Business Process evidence:**

No standalone Diagnostic process is named in P; Order progression is adjacent evidence. See the supporting-context references below; no absent process is manufactured.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Diagnostic Administration, line 46](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Diagnostic Administration** | **Diagnostic Correlation & Linkage Registry** | Diagnostic order-to-report correlation bindings, accession number linkages, report delivery status ledgers, and addenda/correction notices. | Does not own clinical specimen analysis or imaging interpretation. |

**Supporting behaviour / boundary sources:** [D: 2.5 Diagnostic Services Enablement, lines 88–98](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.10 Remote & Virtual Care Enablement, lines 155–165](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [P: 3.5 Closed-Loop Order Progression Process, lines 113–125](../../docs/markdown/03-business-architecture/processes/business-processes.md); [R: 2.2 Care Delivery Roles, lines 72–82](../../docs/markdown/03-business-architecture/actors-roles/roles.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

[X §2: explicit dependency rows, lines 56–57](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Diagnostic Administration** | `Order Requisition Ingress` | **Order Administration** | Correlates incoming diagnostic observations with active closed-loop order requisitions. |
| **Diagnostic Administration** | `Terminology Mapping Service` | **Clinical Knowledge Services** | Normalises local laboratory and radiology test codes to canonical SNOMED-CT/LOINC concepts. |

**Business Interaction catalogue/context:** `Clinical Information Supply`; `Clinical Information Query`; `Clinical Information Review`; `Service Outcome`; `Information Qualification`; `Information Correction` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Practitioner Collaboration`; `Service-Delivery Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns request correlation, report binding and preliminary/final/corrected report distribution, excluding specimen analysis/interpretation. D names publication of observations/reference ranges/image-study links. O assigns correlation/accession/delivery/correction information; O §1.2’s performing-provider / Diagnostic Administration wording needs scope review.

**Responsibility, originating authority, stewardship, custody and consumption:** The performing diagnostic provider is expressly outside administration’s analysis/interpretation responsibility. The evidence does not completely allocate originating authority for measurement, interpretation, qualification or correlation. Provider source authority must not be inferred from delivery/publishing.

**Applicable frozen Foundation / Package 1 constraints:** Order ≠ Result; F-A granular authority, F-L preserved corrections; F-G 6/7/12/14; P1-S service delivery/outcome distinction; P1-D association does not own physiological observations.

**Questions created by this evidence — deferred, not answered:**

- What independent meanings, if any, are required by the observation/finding language across B/D/I/O/R? It does not supply canonical definitions or equivalence.
- Are diagnostic activity, reported observations, interpretation and report independently required meanings? This step only records the behaviour and question.
- Do T’s finalised-report emphasis and B’s preliminary/final/corrected distribution have matching scopes? What does P’s signature wording constrain? (K2/K11)

**Gaps / trace strength:** No standalone Diagnostic process is named in P; Order progression is adjacent evidence. Observation occurs in clinical query/retrieval/performer and telemetry contexts beyond diagnostics; Finding also appears in review/incident outcomes. These occurrences do not resolve their semantic distinction.

### 5.6 Medication

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 7. Medication Administration (`Harmonia-Relevant`), lines 304–308](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **7.1 Medication Order & Dispense Management**
>   - `FEAT-SA-18`: **Medication Order Ingestion**: Ingest inpatient and discharge prescription orders.
>   - `FEAT-SA-19`: **Dispense Event Tracking**: Ingest medication dispense verification records from pharmacy management systems.
>   - `FEAT-SA-20`: **Medication Administration Record (MAR) Ingestion**: Ingest point-of-care medication administration events.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 8. Medication Administration, lines 123–138](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Medication Administration`
> - **Key Architectural Semantics**:
>   - **Event Tracking**: Aggregates medication orders, pharmacy dispense records, and bedside administration events into a coherent timeline.
>   - **Non-Practice Guardrail**: Harmonia tracks and communicates medication events; it does **not** prescribe drugs, dispense pharmaceuticals, or perform clinical administration.
> - **Functions & Exposed Services**:
>   - **Feature: Medication Order Ingestion**:
>     - *Function*: `Receive Medication Order` — Ingests inpatient and outpatient drug prescriptions.
>     - *Exposed Service*: `Medication Order Ingress` — Ingests prescription events.
>   - **Feature: Dispense Event Tracking**:
>     - *Function*: `Track Dispense Event` — Ingests community and hospital pharmacy dispensing records.
>     - *Exposed Service*: `Dispense Event Ingress` — Ingests pharmacy supply notifications.
>   - **Feature: Medication Administration Record (MAR) Ingestion**:
>     - *Function*: `Receive Medication Administration Event` — Records nurse-administered doses, e-MAR documentation, and infusion telemetry.
>     - *Exposed Service*: `Medication Administration Ingress` — Ingests administration records.
> - **Information Responsibility**: Medication Event Timeline, Prescription-Dispense-Administration Correlation Matrix.

**Direct Business Process evidence:**

No dedicated Medication process is named in P. See the supporting-context references below; no absent process is manufactured.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Medication Administration, line 47](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Medication Administration** | **Medication Event Timeline Matrix** | Aggregated prescription orders, community/hospital pharmacy dispense records, and nurse e-MAR administration logs. | Does not prescribe, dispense, or administer medications. |

**Supporting behaviour / boundary sources:** [D: 2.6 Medication Therapy Enablement, lines 102–112](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.7 Preventive Care Enablement, lines 116–126](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [E: 2.1 Patient Clinical Record, lines 32–45](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [R: 2.2 Care Delivery Roles, lines 72–82](../../docs/markdown/03-business-architecture/actors-roles/roles.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

No subject-specific row was identified in X §2; no missing dependency is invented.

**Business Interaction catalogue/context:** `Clinical Information Supply`; `Clinical Information Review`; `Service Request`; `Service Outcome` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Patient Collaboration`; `Care-Team Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns order ingestion, dispense tracking and administration-event ingestion; it expressly excludes prescribing, dispensing and clinical administration. O assigns timeline/correlation information. D assigns history projection and allergy/adverse-reaction correlation; T supplies reconciliation-ingestion and therapy-update distribution descriptions.

**Responsibility, originating authority, stewardship, custody and consumption:** R Prescriber/Performer describe business capacities, but B/O’s non-practice boundary remains. Sources describe supplied events from pharmacies/clinical administration. They do not fully assign originating/attesting authority, stewardship or qualification for each event/history contribution.

**Applicable frozen Foundation / Package 1 constraints:** Medication Order ≠ Dispense ≠ Administration; F-A assertion authority; F-G 6/7/12/13/14; P1-P/R subject/provider semantics and P1-D device association. No Medication Definition is established by a pattern reference.

**Questions created by this evidence — deferred, not answered:**

- How do supplied direction/supply/administration information and history/reconciliation evidence differ without assuming realised resource boundaries?
- Do the sources independently require Medication Definition, or only understanding/reference to medication meaning? No named definition responsibility was found.
- How do B/O’s nurse/bedside examples relate to D/T’s broader contexts, and why does D associate Medication Administration Tracking with allergy/adverse-reaction correlation while T describes therapy-update distribution? (K4/K11)

**Gaps / trace strength:** No dedicated Medication process is named in P. Definition, history authority, event-to-order cardinality and setting-neutral definition remain undecided. Catalogue collaboration scope alone supplies no ownership.

### 5.7 Procedure

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 8. Procedure Administration (`Harmonia-Relevant`), lines 310–313](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **8.1 Procedure & Surgical Administration**
>   - `FEAT-SA-21`: **Procedure Booking Ingestion**: Ingest scheduled surgical case bookings and procedural requests.
>   - `FEAT-SA-22`: **Procedural Documentation Ingestion**: Ingest operative reports, anesthesia records, and post-procedure summaries.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 9. Procedure Administration, lines 142–151](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Procedure Administration`
> - **Functions & Exposed Services**:
>   - **Feature: Procedure Booking Ingestion**:
>     - *Function*: `Receive Procedure Booking / Request` — Ingests procedural and surgical requisitions.
>     - *Exposed Service*: `Procedure Booking Ingress` — Receives surgical/procedural requests.
>   - **Feature: Procedural Documentation Ingestion**:
>     - *Function*: `Receive Procedural Documentation` — Ingests operation reports, anaesthetic records, and post-procedure notes.
>     - *Exposed Service*: `Procedural Documentation Ingress` — Ingests surgical notes into patient record streams.
> - **Information Responsibility**: Procedure Request Register, Procedural Documentation Index.

**Direct Business Process evidence:**

No general Procedure process is named in P. See the supporting-context references below; no absent process is manufactured.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Procedure Administration, line 48](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Procedure Administration** | **Procedure Request & Documentation Index** | Surgical/procedural booking requisitions, operation reports, anaesthetic records, and procedural note indices. | Does not own surgical theatre suite logistics. |

**Supporting behaviour / boundary sources:** [H: 2.3 Theatre Operations, lines 56–63](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md); [P: 4.7 Theatre Case Progression Process, lines 146–159](../../docs/markdown/03-business-architecture/processes/business-processes.md); [R: 2.2 Care Delivery Roles, lines 72–82](../../docs/markdown/03-business-architecture/actors-roles/roles.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

No subject-specific row was identified in X §2; no missing dependency is invented.

**Business Interaction catalogue/context:** `Service Request`; `Service Response`; `Service Outcome`; `Service Coordination` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Service-Delivery Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns receipt of procedural/surgical requests and procedural documentation. O assigns the request/document index and excludes theatre logistics. H/P explicitly govern theatre-specific operational milestones.

**Responsibility, originating authority, stewardship, custody and consumption:** Request/document receipt and indexing are administration responsibilities. Sources identify procedural performers and authored documentation as context, but do not completely establish Procedure Activity/outcome assertion authority or a reusable Procedure Definition owner.

**Applicable frozen Foundation / Package 1 constraints:** Procedure Activity ≠ Procedure Documentation; Order ≠ Result; F-G 6/7/12/13; P1-S definition/delivery/outcome meanings are reused, not a mandate for Procedure Definition. D §1 preserves the non-practice boundary.

**Questions created by this evidence — deferred, not answered:**

- What procedure information is required beyond bookings and documentation, and what is the scope of non-surgical procedural behaviour in this baseline? (K4)
- Does reusable definition information have independent evidence, or are frozen service meanings sufficient? No separate definition responsibility is identified.
- How must documentation about performed activity be distinguished from that activity, without promoting theatre milestones to a universal Procedure lifecycle?

**Gaps / trace strength:** No general Procedure process is named in P. Theatre Case Progression is direct only for Theatre Operations; no universal procedure scope, definition, activity/documentation relationship or lifecycle is derived.

### 5.8 Clinical Document

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 9. Clinical Record Administration (`Harmonia-Core`), lines 315–319](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **9.1 Clinical Documentation Lifecycle**
>   - `FEAT-SA-23`: **Clinical Document Ingestion**: Ingest structured and unstructured clinical documents (discharge summaries, consult notes, letters).
>   - `FEAT-SA-24`: **Document Versioning & Supersession**: Manage document classification, author attribution, amendment attribution, versioning, and supersession or withdrawal without destructive data loss.
>   - `FEAT-SA-25`: **Document Metadata Indexing**: Index clinical document metadata (author, specialty, encounter, date, confidentiality) for rapid discovery.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 10. Clinical Record Administration, lines 155–170](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Clinical Record Administration`
> - **Key Architectural Semantics**:
>   - **Document Governance**: Governs the ingestion, metadata indexing, versioning, addenda, and legal lifecycle of clinical documents (discharge summaries, specialist letters, advance care directives).
> - **Functions & Exposed Services**:
>   - **Feature: Clinical Document Ingestion**:
>     - *Function*: `Receive Clinical Document` — Receives clinical documents from information suppliers.
>     - *Exposed Service*: `Clinical Document Ingress` — Exposes document submission endpoints across the HIE.
>   - **Feature: Document Versioning & Supersession**:
>     - *Function*: `Govern Clinical Document Lifecycle` — Manages document states: *Draft* $\to$ *Preliminary* $\to$ *Final* $\to$ *Amended* $\to$ *Superseded* $\to$ *Entered-in-Error*.
>     - *Exposed Service*: `Clinical Document Lifecycle Service` — Allows authoring clinicians to publish addenda or corrections.
>     - *Governed Process*: **Clinical Document Lifecycle Process**.
>   - **Feature: Document Metadata Indexing**:
>     - *Function*: `Index Clinical Document Metadata` ��� Indexes author, specialty, encounter, date, and document type (LOINC/SNOMED).
>     - *Exposed Service*: `Document Metadata Registry Query` — Discloses document registry metadata to clinical viewers.
> - **Information Responsibility**: Clinical Document Registry, Document Version History, Document Lifecycle State Register.

**Direct Business Process evidence:**

[P: 3.6 Clinical Document Lifecycle Process, lines 129–140](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Clinical Record Administration`
> - **Process Purpose**: Governs the versioning, clinical sign-off, addenda, superseding, and legal status of clinical documents (discharge summaries, specialist letters, advance care directives).
> - **State Progression Lifecycle**:
>   $$\text{Draft} \longrightarrow \text{Preliminary} \longrightarrow \text{Final Signed} \longrightarrow \text{Amended / Addended} \longrightarrow \text{Superseded} \longrightarrow \text{Entered-in-Error}$$
> - **Key State Dispositions**:
>   - `Draft`: Incomplete clinical document saved during consultation.
>   - `Preliminary`: Document authored pending senior registrar or consultant countersignature.
>   - `Final Signed`: Authoritative clinical document legally signed and published.
>   - `Amended / Addended`: Governed supplementary clinical addendum appended to signed document.
>   - `Superseded`: Entire document replaced by a newer revision; prior version retained for audit.
>   - `Entered-in-Error`: Document formally retracted due to wrong patient or invalid content; content struck through with retraction notice.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Clinical Record Administration, line 49](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Record Administration** | **Clinical Document Registry & Lifecycle State** | Clinical document master metadata, structured and narrative document repository, document version histories, addenda links, and legal document states (*Draft* to *Superseded*). | Does not own real-time collaborative discussion transcripts. |

**Supporting behaviour / boundary sources:** [E: 2.1 Patient Clinical Record, lines 32–45](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [E: 2.2 Health Information Exchange (HIE), lines 49–66](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [E: 2.7 Clinical Collaboration, lines 134–149](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

[X §2: explicit dependency rows, lines 58–59](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Record Administration** | `Healthcare Subject Context Resolution` | **Healthcare Subject Context** | Validates patient demographic context and indigenous/interpreter requirements for document headers. |
| **Clinical Record Administration** | `Semantic Conformance Verification Service` | **Information Design Governance** | Verifies clinical document structural and semantic compliance against governed interchange profiles. |

**Business Interaction catalogue/context:** `Clinical Information Supply`; `Clinical Information Review`; `Information Submission`; `Information Qualification`; `Information Correction`; `Information Publishing` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Information-Sharing Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns ingestion, versioning/supersession and metadata indexing. O assigns document registry/history/legal states and excludes live discourse. P explicitly describes sign-off, Final Signed, addenda, supersession and entered-in-error. E separately assigns integrated record synthesis/preservation.

**Responsibility, originating authority, stewardship, custody and consumption:** B/P refer to authoring clinicians, sign-off and corrections; document responsibility belongs to Clinical Record Administration, integrated synthesis to Patient Clinical Record. This does not establish one originating authority for all conveyed assertions or make every document a signed legal instrument.

**Applicable frozen Foundation / Package 1 constraints:** Clinical Document ≠ Clinical Fact; F-A assertion/document authority, F-L non-destructive history, F-V assembly authority; F-G 6/7/12/13/14. Source lifecycle wording is not automatically a complete document definition.

**Questions created by this evidence — deferred, not answered:**

- What does P’s signed/legal process wording constrain compared with B/F-L’s broader document lifecycle? (K2)
- Which information is asserted in or conveyed by a document, and what authority survives document amendment/supersession?
- Does the descriptive use of “clinical facts” in O create any independently governed Clinical Fact meaning? There is no named Clinical Fact ownership row; the question remains open.

**Gaps / trace strength:** No Clinical Fact concept is declared. Document/report signature scope and the relation between original documents and longitudinal synthesis require review, not convention. Contextual standards references in B indexing remain source wording only.

### 5.9 Problem / Condition / Care Need

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 1. Patient Clinical Record (`Harmonia-Core`), lines 588–592](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **1.1 Longitudinal Clinical Record Governance**
>   - `FEAT-ISE-01`: **Longitudinal Health Record (LHR) Assembly**: Assemble disparate encounters, diagnostic reports, medications, and clinical documents into a unified, vendor-neutral longitudinal timeline.
>   - `FEAT-ISE-02`: **Active Clinical Record Access**: Provide low-latency, active distributed access to consolidated patient clinical summaries.
>   - `FEAT-ISE-03`: **Durable Clinical Record Preservation**: Durably maintain governed, vendor-neutral longitudinal clinical representation and preservation with verifiable integrity controls.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

Primary E §2.1 is quoted in §4 above. Its three source functions/services are `Assemble Longitudinal Clinical Record` / `Longitudinal Clinical Record Query`, `Maintain Active Clinical Record` / `Active Problem & Allergy Summary Query`, and `Preserve Durable Clinical Record` / `Historical Clinical Record Access`. Source Feature names are `Longitudinal Health Record (LHR) Assembly`, `Active Clinical Record Access`, `Durable Clinical Record Preservation`. This is direct Patient Clinical Record evidence, not a complete three-concept model. [E, lines 32–45](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

**Direct Business Process evidence:**

No dedicated Condition/Problem/Care Need process or three-part taxonomy is established in P/O. See the supporting-context references below; no absent process is manufactured.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Patient Clinical Record, line 52](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Patient Clinical Record** | **Governed Longitudinal Health Record (LHR)** | Canonical longitudinal health record synthesis, unified chronological clinical timeline, active problem lists, verified allergy lists, and immutable historical record archives. | Does not own original legal documents; owns the canonical integrated clinical synthesis. |

B §11 Care Plan Administration and its O row are separately recorded in §5.10; their diagnosis/problem associations are supporting evidence, not ownership of all diagnoses or care needs.

**Supporting behaviour / boundary sources:** [B: 11. Care Plan Administration, lines 174–186](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [D: 2.1 Primary Care Enablement, lines 32–42](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.3 Emergency Care Enablement, lines 60–70](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.7 Preventive Care Enablement, lines 116–126](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.8 Community Care Enablement, lines 130–140](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

No subject-specific row was identified in X §2; no missing dependency is invented.

**Business Interaction catalogue/context:** `Clinical Information Review`; `Information Challenge`; `Information Qualification`; `Information Correction`; `Service Coordination` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Patient Collaboration`; `Care-Team Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** E assigns active problem lists, clinical record synthesis and durable history. O assigns integrated synthesis but excludes original legal documents. B §11 associates care plans with “active diagnoses, health problems, and care team members”. D gives preventive/community/social-support contexts, not the three-concept taxonomy.

**Responsibility, originating authority, stewardship, custody and consumption:** Responsibility for active-record/synthesis is evidenced. Originating authorities, stewardship and meaningful lifecycle for each Condition/Problem/Care Need are not individually allocated. A function consuming a diagnosis does not establish ownership of that diagnosis.

**Applicable frozen Foundation / Package 1 constraints:** Condition ≠ Problem ≠ Care Need; F-M non-hierarchical categories; F-A granular qualification; F-G 6/7/12/13/14; F-C membership is not ownership; P1-P subject meaning.

**Questions created by this evidence — deferred, not answered:**

- What business evidence supports independently meaningful Condition, Problem and Care Need, and what information must the enterprise distinguish?
- Is Care Need legitimately required as reference/contextual information, or does the absence of a named behaviour/responsibility reveal insufficient evidence?
- What relationships/qualification/lifecycles among the three, if any, can be justified? No pathology-only definition, inheritance or complete authority allocation is supplied.

**Gaps / trace strength:** No dedicated Condition/Problem/Care Need process or three-part taxonomy is established in P/O. Case-insensitive Domain03 search found no standalone “Care Need” or “Care Needs” phrase; the README uses general “healthcare needs” wording. Problem/diagnosis/condition/support references are not a substitute definition. This is insufficient evidence, not proof the information is unnecessary.

### 5.10 Care Plan

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 10. Care Plan Administration (`Harmonia-Relevant`), lines 321–323](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **10.1 Care Plan Coordination**
>   - `FEAT-SA-26`: **Care Plan Ingestion & Distribution**: Ingest multidisciplinary care plans and syndicate updates to authorized care team members.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 11. Care Plan Administration, lines 174–186](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Care Plan Administration`
> - **Functions & Exposed Services**:
>   - **Feature: Care Plan Ingestion & Distribution**:
>     - *Function*: `Receive Care Plan` — Ingests multidisciplinary care plans, clinical goals, and patient action steps.
>     - *Exposed Service*: `Care Plan Ingress` — Ingests shared care plans.
>   - **Feature: Care Plan Context Association**:
>     - *Function*: `Associate Care Plan Context` — Links care plans to active diagnoses, health problems, and care team members.
>     - *Exposed Service*: `Care Plan Context Query` — Discloses active care plans to treating clinicians.
>   - **Feature: Care Plan Update Distribution**:
>     - *Function*: `Distribute Care Plan Change` — Broadcasts care plan updates to all enrolled care team participants.
>     - *Exposed Service*: `Care Plan Change Notification` — Emits care plan revision notices.
> - **Information Responsibility**: Shared Care Plan Registry, Clinical Goal Ledger, Care Plan Revision History.

**Direct Business Process evidence:**

No dedicated Care Plan process is named in P. See the supporting-context references below; no absent process is manufactured.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Care Plan Administration, line 50](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Care Plan Administration** | **Shared Care Plan & Clinical Goal Registry** | Multidisciplinary care plan definitions, agreed clinical goals, barrier logs, action items, and care team participant enrolments. | Does not own individual clinical encounter notes. |

**Supporting behaviour / boundary sources:** [D: 2.1 Primary Care Enablement, lines 32–42](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.8 Community Care Enablement, lines 130–140](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [E: 2.1 Patient Clinical Record, lines 32–45](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

No subject-specific row was identified in X §2; no missing dependency is invented.

**Business Interaction catalogue/context:** `Service Coordination`; `Coordination Outcome`; `Participation Registration`; `Participation Query`; `Participation Withdrawal` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Patient Collaboration`; `Care-Team Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns plan ingestion, context association and change distribution. O names multidisciplinary plans/goals/barriers/action items/participant enrolment and excludes encounter notes. C explicitly names shared goal setting, patient-reported outcome tracking and shared care planning.

**Responsibility, originating authority, stewardship, custody and consumption:** Plan-registry/revision responsibility is explicit; source contributors/agreement and authority over constituent activities/goals/outcomes are not fully allocated. Membership does not establish access authority. Stewardship/custody cannot be inferred from plan distribution.

**Applicable frozen Foundation / Package 1 constraints:** Goal ≠ Outcome; Care Plan ≠ Activity; Condition ≠ Problem ≠ Care Need; F-G 6/7/12/14/15; F-C membership and F-R role separation; P1-P/R/S reuse. R §3.1 keeps participation, caring/access/consent/decision authority distinct.

**Questions created by this evidence — deferred, not answered:**

- What information does care planning actually coordinate, and which goal/action/participant/barrier meanings are independently required?
- What does Goal mean in the business scope and how is it distinguished from actual outcome information?
- How do B’s Context Association/Update Distribution Features relate to the sole identified T Feature FEAT-SA-26? Are D’s chronic/community plan functions aligned with T’s same-named Features? (K5/K11)

**Gaps / trace strength:** No dedicated Care Plan process is named in P. No care-plan definition/binding stage, plan-owned activity/outcome or canonical decomposition is inferred from the registry row.

### 5.11 Clinical Communication

**Business Enabling Capability / view evidence.** The primary source owner label is reproduced verbatim below; its `L1:` prefix is the unresolved source typing issue K9, not adopted classification. T supplies the exact Feature identifiers, scope descriptions, capability naming and relevance classification; no hierarchy level is invented.

[T: 11. Clinical Communication Administration (`Harmonia-Core`), lines 325–329](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **11.1 Secure Clinical Communication**
>   - `FEAT-SA-27`: **Clinical Communication Distribution**: Distribute secure clinical communications between verified healthcare providers and designated care team endpoints.
>   - `FEAT-SA-28`: **Delivery Acknowledgement Tracking**: Correlate recipient delivery and read receipts with outbound clinical communications, distinguishing transport delivery from recipient acknowledgement.
>   - `FEAT-SA-29`: **Communication Audit Logging**: Maintain an enduring, non-PHI, tamper-evident log of all clinical communication transactions.

**Direct Business Function / Business Service / Feature / Information Responsibility evidence:**

[B: 12. Clinical Communication Administration, lines 190–206](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md)

> - **Owning Capability**: `L1: Clinical Communication Administration`
> - **Key Architectural Semantics**:
>   - **Technical vs. Business Acknowledgement**:
>     - **Technical ACK**: Confirms technical/transport receipt by participating systems.
>     - **Business Delivery ACK**: Confirms that the target clinician or clinical application has *received, parsed, accepted, and filed* the communication into the clinical record.
> - **Functions & Exposed Services**:
>   - **Feature: Clinical Communication Distribution**:
>     - *Function*: `Distribute Clinical Communication` — Routes secure messages, referral letters, and critical alerts to practitioner endpoints.
>     - *Exposed Service*: `Secure Clinical Message Dispatch` — Exposes message dispatch to clinical authors.
>   - **Feature: Delivery Acknowledgement Tracking**:
>     - *Function*: `Track Business Delivery Acknowledgement` — Tracks end-to-end delivery state and records business-level delivery/rejection acknowledgements.
>     - *Exposed Service*: `Communication Delivery Status Query` — Discloses delivery confirmation to message senders.
>   - **Feature: Communication Audit Logging**:
>     - *Function*: `Record Communication Transaction` — Maintains an immutable audit trail of clinical message dispatch and receipt.
>     - *Exposed Service*: `Communication Audit Query` — Discloses transmission history for medico-legal verification.
> - **Information Responsibility**: Clinical Message Dispatch Log, Business Delivery Acknowledgement Ledger, Medico-Legal Transmission Audit Register.

**Direct Business Process evidence:**

No dedicated Clinical Communication process is named in P. See the supporting-context references below; no absent process is manufactured.

**Authoritative Information Responsibility matrix evidence:**

[O §2: Clinical Communication Admin, line 51](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Communication Admin** | **Clinical Message Dispatch & Audit Register** | Secure message dispatch logs, end-to-end business delivery acknowledgements (business ACKs), and non-repudiation communication audit trails. | Does not own the clinical content of messages. |

**Supporting behaviour / boundary sources:** [E: 2.1 Patient Clinical Record, lines 32–45](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [E: 2.2 Health Information Exchange (HIE), lines 49–66](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [E: 2.4 Health Information Communication, lines 88–96](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [E: 2.7 Clinical Collaboration, lines 134–149](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [B: 10. Clinical Record Administration, lines 155–170](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md). The referenced sections remain authoritative for their own scopes; related scope does not create a missing direct trace.

[X §2: explicit dependency rows, line 67](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Logistics** | `Secure Clinical Message Dispatch` | **Clinical Communication Admin** | Sends automated electronic specimen tracking notifications and urgent critical result dispatch alerts. |

**Business Interaction catalogue/context:** `Clinical Collaboration`; `Clinical Information Supply`; `Clinical Information Notification`; `Information Submission`; `Information Qualification` (I, exact definitions in §4.1). **Business Collaboration catalogue/context:** `Practitioner Collaboration`; `Care-Team Collaboration`; `Information-Sharing Collaboration` (C, exact purposes/activities in §4.2). These are relevant source scopes; an explicit per-function membership/interaction mapping is not established merely by listing them.

**What the frozen evidence establishes:** B assigns distribution, business acknowledgement tracking and transaction audit. O excludes clinical-content ownership. E distinguishes technical mediation, exchange state, collaboration discourse and governed record/document submission. T describes recipient delivery/read receipts, while B’s business ACK also includes filing.

**Responsibility, originating authority, stewardship, custody and consumption:** Dispatch/acknowledgement/evidence responsibility is distinct from communicated-content authority. Source reported filing/receipt is evidence with its own asserting source; B/O/E do not allocate one uniform originating authority or incorporation rule for every communication. Custody/consumption confer none.

**Applicable frozen Foundation / Package 1 constraints:** Clinical Communication ≠ Clinical Record; technical/business acknowledgement distinction; F-A/F-G 6/7/12/14; AX-13 bounds external outcome/control claims; formal document submission boundary in E §2.7/T is frozen source evidence.

**Questions created by this evidence — deferred, not answered:**

- What does a reported business acknowledgement establish across B/T scopes, and does filing concern an external recipient record or Harmonia’s governed record? (K7)
- What exact business behaviour qualifies/incorporates conveyed assertions, separately from the communication? E supports formal submission for collaborative entries, not a universal verification transformation.
- X names Patient Clinical Record as owner of Collaboration Clinical Summary Resolution, while E exposes it under Clinical Collaboration. Which scope/ownership is intended? (K12)

**Gaps / trace strength:** No dedicated Clinical Communication process is named in P. No communication “becomes record” relationship, verification state machine or new incorporation behaviour is established.

## 6. Supporting-context evidence and source comparisons

The excerpts in this section provide the exact supporting Feature / Function / Business Service wording behind §5's references. They remain scoped to their source capability. They do not create new family ownership, universal processes or function-to-interaction mappings. Source `L1:` labels remain subject to K9. Catalogue numbering is retained without assigning invented hierarchy levels.

### 6.1 Service Delivery contexts

D explicitly describes enablement/correlation behaviour, not Harmonia performing clinical practice. The following contextual sources bear on referral support, encounters, diagnostic information, medication, procedures, records, problems/conditions, care planning and communication. T's corresponding catalogue names are quoted separately because their names and same-named Feature descriptions sometimes differ (K5/K11). These pairs are comparison evidence, not an asserted equivalence mapping.

[D: 2.1 Primary Care Enablement, lines 32–42](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Primary Care Enablement`
> - **Clinical Operating Context**: General practice clinics, community medical centres, and family medicine practices providing longitudinal whole-person care.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: General Practice Shared Record Ingestion**:
>     - *Function*: `Correlate Primary Care Consultation Context` — Binds GP consultation notes, vital signs, and encounter episodes to the regional Longitudinal Health Record (LHR).
>     - *Exposed Service*: `Primary Care Summary Ingress` — Ingests GP event summaries into the HIE.
>   - **Feature: Primary Care Notification Dispatch**:
>     - *Function*: `Coordinate Multi-Agency Chronic Care Plan` — Enables GPs to track multi-provider disease management milestones (e.g., Diabetes or Cardiovascular Care Plans).
>     - *Exposed Service*: `Chronic Care Timeline Query` — Exposes aggregated multidisciplinary care timelines.
> - **Architectural Dependencies**: Consumes *Person Identifier Resolution*, *Patient Clinical Record Query*, *Referral Submission*, *Care Plan Administration*.

[T: 2. Primary Care (`Harmonia-Relevant`), lines 372–375](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **2.1 Primary Care Coordination**
>   - `FEAT-SD-01`: **General Practice Shared Record Ingestion**: Ingest shared health summaries, encounter notes, and immunization records from primary care practice management systems.
>   - `FEAT-SD-02`: **Primary Care Notification Dispatch**: Dispatch emergency department arrival, acute admission, and discharge notifications to the patient's registered general practitioner.

[D: 2.2 Acute Care Enablement, lines 46–56](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Acute Care Enablement`
> - **Clinical Operating Context**: Hospital specialty wards, surgical suites, and intensive care units managing severe, urgent, or post-operative conditions.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Acute Care Clinical Context Provision**:
>     - *Function*: `Assemble Acute Clinical Baseline` — Rapidly aggregates previous discharge summaries, active medications, allergies, and diagnostic alerts upon acute hospital presentation.
>     - *Exposed Service*: `Acute Admission Dossier Query` — Delivers a high-priority clinical briefing to admitting teams.
>   - **Feature: Acute Clinical State Event Capture**:
>     - *Function*: `Route Acute Critical Result Alert` — Immediately directs urgent lab/radiology alerts (e.g., critical troponin, intracranial bleed) to active acute attending clinicians.
>     - *Exposed Service*: `Acute Clinical Alert Dispatch` — Dispatches urgent interruptive alerts to on-duty ward teams.
> - **Architectural Dependencies**: Consumes *Encounter Context Resolution*, *Diagnostic Result Delivery*, *Health Information Control (Break-Glass Access)*.

[T: 3. Acute Care (`Harmonia-Relevant`), lines 377–380](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **3.1 Acute Care Monitoring & Event Coordination**
>   - `FEAT-SD-03`: **Acute Clinical State Event Capture**: Ingest clinical deterioration notifications and acute clinical status updates.
>   - `FEAT-SD-04`: **Acute Care Clinical Context Provision**: Provide consolidated longitudinal history and active medication lists to acute care clinicians at point of triage.

[D: 2.3 Emergency Care Enablement, lines 60–70](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Emergency Care Enablement`
> - **Clinical Operating Context**: Emergency Departments (ED), trauma centres, and urgent care clinics operating under time-critical, high-acuity constraints.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Emergency Department Arrival & Triage Ingestion**:
>     - *Function*: `Bind ED Triage Assessment` — Captures ATS (Australasian Triage Scale) category, presenting complaint, and ambulance paramedic handover data.
>     - *Exposed Service*: `ED Triage Stream Ingress` — Ingests emergency triage presentations.
>   - **Feature: Emergency Care Cross-Facility Correlation**:
>     - *Function*: `Provide Emergency LHR Summary` — Delivers rapid, override-capable access to Advance Care Directives, allergy records, and recent hospital admissions.
>     - *Exposed Service*: `Emergency Fast-Path LHR Query` — Provides low-latency emergency record summaries.
> - **Architectural Dependencies**: Consumes *Person Identifier Resolution (Fast-Match)*, *Client Privacy (Break-Glass Override)*, *Encounter State Progression*.

[T: 4. Emergency Care (`Harmonia-Relevant`), lines 382–385](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **4.1 Emergency Care Progression**
>   - `FEAT-SD-05`: **Emergency Department Arrival & Triage Ingestion**: Ingest emergency presentation, triage category, and chief complaint records.
>   - `FEAT-SD-06`: **Emergency Care Cross-Facility Correlation**: Correlate incoming emergency presentations with recent regional admissions, prior discharge summaries, and known allergy profiles.

[D: 2.4 Inpatient Care Enablement, lines 74–84](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Inpatient Care Enablement`
> - **Clinical Operating Context**: Inpatient hospital wards, rehabilitation units, and sub-acute facilities managing multi-day hospital stays.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Inpatient Rounding Context Provision**:
>     - *Function*: `Consolidate Inpatient Clinical Round Feed` — Aggregates daily vital signs, overnight pathology, allied health notes, and medication administration records.
>     - *Exposed Service*: `Ward Round Clinical Briefing Query` — Discloses consolidated 24-hour clinical briefing views.
>   - **Feature: Inpatient Clinical Progression Tracking**:
>     - *Function*: `Assemble Inpatient Discharge Dossier` — Synthesises inpatient diagnoses, procedural summaries, reconciled discharge medications, and follow-up instructions into a canonical discharge summary package.
>     - *Exposed Service*: `Discharge Dossier Packaging Service` — Emits canonical discharge summaries to GPs and community providers.
> - **Architectural Dependencies**: Consumes *Encounter Care-Place Movement*, *Clinical Document Lifecycle*, *Medication Administration Event Ingestion*, *Discharge Management*.

[T: 5. Inpatient Care (`Harmonia-Relevant`), lines 387–390](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **5.1 Multidisciplinary Inpatient Care Coordination**
>   - `FEAT-SD-07`: **Inpatient Rounding Context Provision**: Aggregate diagnostic results, current medication administration records, and nursing notes for multidisciplinary inpatient rounds.
>   - `FEAT-SD-08`: **Inpatient Clinical Progression Tracking**: Capture inpatient clinical milestone completions (e.g., medical stability achieved, allied health clearance).

[D: 2.5 Diagnostic Services Enablement, lines 88–98](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Diagnostic Services Enablement`
> - **Clinical Operating Context**: Clinical pathology laboratories, diagnostic imaging networks (radiology/MRI/CT), nuclear medicine, and clinical neurophysiology departments.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Laboratory & Imaging Result Distribution**:
>     - *Function*: `Receive Diagnostic Requisition Feed` — Normalises order requisitions from across the health network into modality worklists.
>     - *Exposed Service*: `Modality Worklist Distribution` — Supplies order worklists to diagnostic laboratory/imaging systems.
>   - **Feature: Diagnostic History Consolidation**:
>     - *Function*: `Publish Governed Diagnostic Report` — Normalises and distributes structured diagnostic observations, reference ranges, and diagnostic image study links into the LHR.
>     - *Exposed Service*: `Diagnostic Publication Service` — Publishes verified diagnostic reports across the enterprise.
> - **Architectural Dependencies**: Consumes *Order Administration (Closed-Loop Progression)*, *Information Design Governance (LOINC/SNOMED Bindings)*.

[T: 8. Diagnostic Services (`Harmonia-Relevant`), lines 392–395](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **8.1 Diagnostic Service Integration**
>   - `FEAT-SD-09`: **Laboratory & Imaging Result Distribution**: Reliably distribute structured diagnostic reports (pathology, radiology) to ordering clinicians and designated secondary recipients.
>   - `FEAT-SD-10`: **Diagnostic History Consolidation**: Consolidate historical diagnostic trends (e.g., cumulative laboratory results) across multiple regional providers into an integrated view.

[D: 2.6 Medication Therapy Enablement, lines 102–112](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Medication Therapy Enablement`
> - **Clinical Operating Context**: Hospital pharmacies, clinical pharmacotherapy review panels, community dispensing networks, and outpatient infusion suites.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Medication History Reconciliation Ingestion**:
>     - *Function*: `Project Longitudinal Medication History` — Aggregates community dispensing, hospital e-MAR administrations, and specialist prescriptions into a unified chronological medication profile.
>     - *Exposed Service*: `Longitudinal Medication History Query` — Discloses full medication timelines for medication reconciliation.
>   - **Feature: Medication Administration Tracking**:
>     - *Function*: `Correlate Adverse Reaction & Allergy Profile` — Binds confirmed substance allergies, adverse drug reactions, and severity grades to the patient identity.
>     - *Exposed Service*: `Allergy & Adverse Reaction Query` — Exposes active allergy profiles to clinical decision support systems.
> - **Architectural Dependencies**: Consumes *Medication Administration*, *Patient Clinical Record*, *Health Information Control*.

[T: 10. Medication Therapy (`Harmonia-Relevant`), lines 397–400](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **10.1 Medication Therapy Provision and Monitoring**
>   - `FEAT-SD-11`: **Medication History Reconciliation Ingestion**: Ingest reconciled medication histories compiled during clinical transitions of care.
>   - `FEAT-SD-12`: **Medication Administration Tracking**: Distribute medication therapy orders, dispensing updates, and administration records across clinical care boundaries.

[D: 2.7 Preventive Care Enablement, lines 116–126](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Preventive Care Enablement`
> - **Clinical Operating Context**: Public health screening programmes (breast/bowel/cervical), national immunisation registries, and health promotion initiatives.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Immunisation Event Submission**:
>     - *Function*: `Syndicate Immunisation Record` — Records vaccine encounters and syndicates records to national immunisation registers (e.g., AIR).
>     - *Exposed Service*: `Immunisation History Query` — Discloses complete vaccination history.
>   - **Feature: Screening Recall Notification Distribution**:
>     - *Function*: `Project Screening Eligibility Timeline` — Tracks age-based and risk-based screening schedules across primary care.
>     - *Exposed Service*: `Preventive Screening Status Query` — Discloses screening due dates to primary care providers.
> - **Architectural Dependencies**: Consumes *Health Information Exchange (Syndication)*, *Healthcare Subject Demographic Context*.

[T: 12. Preventive Care (`Harmonia-Relevant`), lines 402–405](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **12.1 Screening and Immunisation Enablement**
>   - `FEAT-SD-13`: **Immunisation Event Submission**: Ingest point-of-care immunisation administration records and syndicate to national immunization registers.
>   - `FEAT-SD-14`: **Screening Recall Notification Distribution**: Distribute clinical screening reminders and recall notices to designated healthcare providers.

[D: 2.8 Community Care Enablement, lines 130–140](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Community Care Enablement`
> - **Clinical Operating Context**: District nursing, home-based palliative care, community mental health teams, and allied health community outreach.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Community Encounter Ingestion**:
>     - *Function*: `Synchronize Community Visit Record` — Ingests clinical notes, wound assessments, and vital signs recorded in community and home settings.
>     - *Exposed Service*: `Community Care Encounter Ingress` — Ingests community care documentation.
>   - **Feature: Community Care Plan Synchronization**:
>     - *Function*: `Bind Community Support Network Context` — Links community nursing teams with local non-clinical social care and home care packages.
>     - *Exposed Service*: `Community Care Team Query` — Exposes community provider contact rosters.
> - **Architectural Dependencies**: Consumes *Client Relationship*, *Care-Team Collaboration*, *Encounter Administration*.

[T: 14. Community Care (`Harmonia-Relevant`), lines 407–410](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **14.1 Community Care Coordination**
>   - `FEAT-SD-15`: **Community Encounter Ingestion**: Ingest home visit notes, community nursing observations, and social support assessments.
>   - `FEAT-SD-16`: **Community Care Plan Synchronization**: Synchronize community care plan updates between hospital outreach teams and community health organizations.

[D: 2.9 Outreach Care Enablement, lines 144–151](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Outreach Care Enablement`
> - **Clinical Operating Context**: Mobile specialist clinics, rural and remote fly-in fly-out (FIFO) clinical teams, and school health screening programmes.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Outreach Encounter Outcome Ingestion**:
>     - *Function*: `Reconcile Outreach Clinical Batch` — Reconciles and merges clinical documentation recorded during disconnected remote clinic operations.
>     - *Exposed Service*: `Outreach Batch Reconciliation Service` — Reconciles batch clinical encounters upon network reconnection.
> - **Architectural Dependencies**: Consumes *Health Information Exchange*, *Person Identity Correction*, *Clinical Record Administration*.

[T: 15. Outreach Care (`Harmonia-Relevant`), lines 412–415](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **15.1 Outreach Care Continuity**
>   - `FEAT-SD-17`: **Outreach Visit Context Provision**: Package and deliver portable clinical summaries and offline-capable care summaries for remote outreach clinics.
>   - `FEAT-SD-18`: **Outreach Encounter Outcome Ingestion**: Ingest and reconcile clinical documentation gathered during rural and remote outreach visits.

[D: 2.10 Remote & Virtual Care Enablement, lines 155–165](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md)

> - **Owning Capability**: `L1: Remote & Virtual Care Enablement`
> - **Clinical Operating Context**: Telehealth consultations, virtual hospital-in-the-home (vHIT), remote patient monitoring (RPM), and asynchronous specialist e-consults.
> - **Enabling Functions & Exposed Services**:
>   - **Feature: Virtual Care Session Context Binding**:
>     - *Function*: `Correlate Virtual Care Consultation Context` — Binds video consultation metadata, remote practitioner identity, and shared digital whiteboard artifacts to the encounter.
>     - *Exposed Service*: `Virtual Encounter Context Resolution` — Binds virtual session identifiers to patient encounters.
>   - **Feature: Remote Telemetry Ingestion**:
>     - *Function*: `Ingest Remote Patient Telemetry` — Ingests continuous or periodic physiological measurements (e.g., blood pressure, pulse oximetry, continuous glucose) from home monitoring devices.
>     - *Exposed Service*: `Remote Telemetry Ingress` — Ingests remote patient data streams into the LHR.
> - **Architectural Dependencies**: Consumes *Clinical Device Administration*, *Patient Clinical Record*, *Clinical Communication Administration*.

[T: 16. Remote & Virtual Care (`Harmonia-Relevant`), lines 417–422](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **16.1 Remote Patient Monitoring**
>   - `FEAT-SD-19`: **Remote Telemetry Ingestion**: Ingest home patient monitoring data, biometric telemetry, and patient-reported outcome measures (PROMs).
>   - `FEAT-SD-20`: **Virtual Care Session Context Binding**: Associate synchronous virtual/telehealth consultations with the longitudinal health record and active care episode.
>
> *(Capabilities 1 Clinical Assessment, 6 Ambulatory Care, 7 Surgical & Procedural Care, 9 Therapeutic Services, 11 Allied Health, and 13 Rehabilitation remain Adjacent or Reference and are not decomposed into atomic features).*

[T, line 422](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> *(Capabilities 1 Clinical Assessment, 6 Ambulatory Care, 7 Surgical & Procedural Care, 9 Therapeutic Services, 11 Allied Health, and 13 Rehabilitation remain Adjacent or Reference and are not decomposed into atomic features).*

The undecomposed surgical/procedural-care context supplies a scope boundary; it does not justify inventing a general Procedure process or definition. D includes additional details within some Features; differing emphasis is preserved rather than silently replaced with T wording.

### 6.2 Operations, adjacent processes and ownership boundaries

[H: 2.1 Clinic & Practice Operations, lines 35–42](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md)

> - **Owning Capability**: `L1: Clinic & Practice Operations`
> - **Functions & Exposed Services**:
>   - **Feature: Operational Queue Progression**:
>     - *Function*: `Manage Clinic Queue Progression` — Tracks patient arrival, rooming, consultation in-progress, and check-out states.
>     - *Exposed Service*: `Clinic Queue Telemetry Service` — Discloses real-time clinic waiting times and queue states.
>     - *Governed Process*: **Clinic / Operational Queue Progression Process**.
> - **Information Responsibility**: Clinic Queue State, Patient Waiting Time Metrics.

[T: 1. Clinic & Practice Operations (`Harmonia-Relevant`), lines 464–467](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **1.1 Session & Queue Coordination**
>   - `FEAT-HSO-01`: **Clinic Session Tracking**: Ingest and track clinic operating session start, delay, and completion states.
>   - `FEAT-HSO-02`: **Operational Queue Progression**: Track client queue progression through reception, waiting area, consultation, and billing.

[P: 4.8 Clinic / Operational Queue Progression Process, lines 163–175](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Clinic & Practice Operations`
> - **Process Purpose**: Governs the movement and state tracking of outpatients through an ambulatory specialty clinic session.
> - **State Progression Lifecycle**:
>   $$\text{Scheduled} \longrightarrow \text{Patient Arrived} \longrightarrow \text{Checked-In} \longrightarrow \text{Roomed / Pre-Consult} \longrightarrow \text{Consultation In-Progress} \longrightarrow \text{Consultation Concluded} \longrightarrow \text{Departed}$$
> - **Key State Dispositions**:
>   - `Scheduled`: Appointment booked on session list.
>   - `Patient Arrived`: Patient enters waiting area.
>   - `Checked-In`: Reception verifies demographics and marks patient ready.
>   - `Roomed / Pre-Consult`: Nursing staff record baseline vitals and room patient.
>   - `Consultation In-Progress`: Clinician actively conducting consultation.
>   - `Consultation Concluded`: Clinical discussion finished; follow-up orders placed.
>   - `Departed`: Patient checks out and departs clinic facility.

[H: 2.3 Theatre Operations, lines 56–63](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md)

> - **Owning Capability**: `L1: Theatre Operations`
> - **Functions & Exposed Services**:
>   - **Feature: Operating Theatre Case Progression**:
>     - *Function*: `Manage Theatre Case Progression` — Coordinates surgical case milestones: *Case Called* $\to$ *Patient in Anaesthetic Bay* $\to$ *Anaesthesia Commenced* $\to$ *Knife to Skin* $\to$ *Procedure Finished* $\to$ *In PACU/Recovery* $\to$ *Ward Handover Complete*.
>     - *Exposed Service*: `Theatre Case Progression Telemetry` — Emits real-time surgical milestone updates.
>     - *Governed Process*: **Theatre Case Progression Process**.
> - **Information Responsibility**: Theatre Slate Register, Perioperative Milestone Log.

[T: 3. Theatre Operations (`Harmonia-Relevant`), lines 474–477](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **3.1 Perioperative Logistics**
>   - `FEAT-HSO-05`: **Operating Theatre Case Progression**: Track surgical case state transitions (`CALLED` $\to$ `IN_THEATRE` $\to$ `ANAESTHETISED` $\to$ `INCISION` $\to$ `CLOSED` $\to$ `RECOVERY`).
>   - `FEAT-HSO-06`: **Perioperative Resource Notification**: Dispatch alerts to portering, sterilization, and recovery teams based on surgical case progression milestones.

[P: 4.7 Theatre Case Progression Process, lines 146–159](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Theatre Operations`
> - **Process Purpose**: Governs the perioperative operational milestones of a surgical procedure within the operating theatre suite.
> - **State Progression Lifecycle**:
>   $$\text{Case Called} \longrightarrow \text{In Anaesthetic Bay} \longrightarrow \text{Anaesthesia Commenced} \longrightarrow \text{Patient in Theatre} \longrightarrow \text{Knife-to-Skin} \longrightarrow \text{Procedure Finished} \longrightarrow \text{In PACU Recovery} \longrightarrow \text{Ward Handover Complete}$$
> - **Key State Dispositions**:
>   - `Case Called`: Theatre team requests ward to send patient for surgery.
>   - `In Anaesthetic Bay`: Patient arrives in theatre holding and identity/consent verified.
>   - `Anaesthesia Commenced`: Anaesthetic induction underway.
>   - `Patient in Theatre`: Patient transferred onto operating table.
>   - `Knife-to-Skin`: Surgical incision initiated (operative start).
>   - `Procedure Finished`: Wound closure completed and dressings applied.
>   - `In PACU Recovery`: Patient monitored in post-anaesthesia care unit.
>   - `Ward Handover Complete`: Post-operative clinical handover to inpatient ward completed.

[H: 2.6 Bed & Care-Place Management, lines 87–99](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md)

> - **Owning Capability**: `L1: Bed & Care-Place Management`
> - **Key Architectural Semantics**:
>   - **Operational State vs. Definition**: While *Location Administration* defines the physical care-place, *Bed & Care-Place Management* owns the real-time operational status (*Available*, *Occupied*, *Reserved*, *Blocked*, *Dirty*, *Cleaning In-Progress*, *Maintenance Lock*).
> - **Functions & Exposed Services**:
>   - **Feature: Bed Availability Tracking**:
>     - *Function*: `Maintain Operational Bed State` — Tracks real-time bed status and environmental locks.
>     - *Exposed Service*: `Bed Availability Query` — Discloses real-time bed availability across hospital directorates.
>   - **Feature: Bed State Progression**:
>     - *Function*: `Manage Bed Turnover Progression` — Coordinates cleaning, disinfection, and preparation: *Vacated* $\to$ *Cleaning Requested* $\to$ *Cleaning In-Progress* $\to$ *Inspected/Ready* $\to$ *Occupied*.
>     - *Exposed Service*: `Bed Turnover Lifecycle Service` — Coordinates turnover requests with environmental services.
>     - *Governed Process*: **Bed Turnover Process**.
> - **Information Responsibility**: Operational Bed Status Registry, Care-Place Lock Register, Bed Turnover Milestone Log.

[T: 6. Bed & Care-Place Management (`Harmonia-Core`), lines 489–493](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **6.1 Bed State & Turnover Management**
>   - `FEAT-HSO-11`: **Bed Availability Tracking**: Maintain operational status of all registered beds and care-places (`VACANT`, `OCCUPIED`, `BLOCKED`, `RESERVED`, `DIRTY`, `CLEANING`, `CLEAN`).
>   - `FEAT-HSO-12`: **Bed State Progression**: Progress bed states in response to clinical and environmental events (e.g., patient discharge automatically transitions bed to `DIRTY`).
>   - `FEAT-HSO-13`: **Bed Readiness Notification**: Notify bed allocation managers and clinical units when a cleaned bed achieves `READY` status.

[H: 2.14 Clinical Logistics, lines 180–187](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md)

> - **Owning Capability**: `L1: Clinical Logistics`
> - **Functions & Exposed Services**:
>   - **Feature: Pathology Specimen Transport Tracking**:
>     - *Function*: `Manage Specimen Transport Progression` — Tracks the physical transit of pathology bio-specimens, blood products, and surgical trays: *Collected* $\to$ *Courier Picked Up* $\to$ *In-Transit* $\to$ *Laboratory Ingress Received*.
>     - *Exposed Service*: `Specimen Transit Tracking Service` — Exposes chain-of-custody tracking.
>     - *Governed Process*: **Specimen Transport Process**.
> - **Information Responsibility**: Specimen Courier Manifest, Chain-of-Custody Tracking Register.

[T: 19. Clinical Logistics Coordination (`Harmonia-Relevant`), lines 528–530](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **19.1 Specimen & Supply Courier Tracking**
>   - `FEAT-HSO-26`: **Pathology Specimen Transport Tracking**: Track pickup, custody chain, transport conditions, and laboratory delivery of clinical specimens.

[P: 4.12 Specimen Transport Process, lines 224–235](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Clinical Logistics`
> - **Process Purpose**: Governs the physical courier chain-of-custody, transport, and delivery of pathology bio-specimens, blood products, and surgical biopsies.
> - **State Progression Lifecycle**:
>   $$\text{Specimen Packaged} \longrightarrow \text{Courier Dispatched} \longrightarrow \text{Chain-of-Custody Signed} \longrightarrow \text{In-Transit (Monitored)} \longrightarrow \text{Lab Ingress Received} \longrightarrow \text{Specimen Accepted}$$
> - **Key State Dispositions**:
>   - `Specimen Packaged`: Clinical unit packages bio-sample with barcode manifest.
>   - `Courier Dispatched`: Courier route assigned for pickup.
>   - `Chain-of-Custody Signed`: Courier scans barcode and accepts legal custody.
>   - `In-Transit (Monitored)`: Specimen in transit under temperature/time constraints.
>   - `Lab Ingress Received`: Central pathology specimen reception logs physical receipt.
>   - `Specimen Accepted`: Specimen integrity verified and passed to analytical track.

[H: 2.15 Discharge Management, lines 191–198](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md)

> - **Owning Capability**: `L1: Discharge Management`
> - **Functions & Exposed Services**:
>   - **Feature: Discharge Readiness & Coordination Oversight**:
>     - *Function*: `Manage Discharge Coordination Progression` — Synchronises multi-agency discharge readiness: *Discharge Planning Initiated* $\to$ *Medications Reconciled* $\to$ *Transport Booked* $\to$ *Discharge Summary Finalised* $\to$ *Physically Departed*.
>     - *Exposed Service*: `Discharge Readiness Telemetry` — Discloses discharge barrier checklists and planned departure times.
>     - *Governed Process*: **Discharge Progression Process**.
> - **Information Responsibility**: Discharge Readiness Checklist Ledger, Estimated Date of Discharge (EDD) Register.

[T: 20. Discharge Management (`Harmonia-Core`), lines 532–536](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **20.1 Discharge Progression & Coordination**
>   - `FEAT-HSO-27`: **Discharge Milestone Tracking**: Track clinical criteria-led discharge milestones (medications dispensed, transportation arranged, home care confirmed).
>   - `FEAT-HSO-28`: **Discharge Readiness & Coordination Oversight**: Track and aggregate multidisciplinary discharge milestones, home care confirmations, and transit readiness across care teams.
>   - `FEAT-HSO-29`: **Discharge Notification Dispatch**: Dispatch departure notifications, updated bed states, and finalized discharge summaries to external care providers upon physical patient exit.

[P: 4.13 Discharge Progression Process, lines 239–250](../../docs/markdown/03-business-architecture/processes/business-processes.md)

> - **Owning Capability**: `L1: Discharge Management`
> - **Process Purpose**: Governs the multidisciplinary coordination of patient discharge planning, pharmacy reconciliation, transport, and community handover.
> - **State Progression Lifecycle**:
>   $$\text{Discharge Planning Initiated} \longrightarrow \text{Clinical Readiness Confirmed} \longrightarrow \text{Medications Reconciled} \longrightarrow \text{Transport & Services Booked} \longrightarrow \text{Discharge Summary Signed} \longrightarrow \text{Physically Departed}$$
> - **Key State Dispositions**:
>   - `Discharge Planning Initiated`: Estimated Date of Discharge (EDD) set on admission.
>   - `Clinical Readiness Confirmed`: Attending medical team declares patient fit for discharge.
>   - `Medications Reconciled`: Hospital pharmacy reconciles and dispenses discharge medications.
>   - `Transport & Services Booked`: Community nursing, home equipment, and patient transport booked.
>   - `Discharge Summary Signed`: Final discharge summary published and sent to GP.
>   - `Physically Departed`: Patient departs ward; care-place turnover triggered.

[O §2: Bed & Care-Place Management, line 53](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Bed & Care-Place Management** | **Operational Bed State & Turnover Ledger** | Real-time bed occupancy status (*Available*, *Occupied*, *Cleaning*, *Locked*), environmental locks, turnover milestone logs, and cleaning verification timestamps. | Does not own static physical care-place definitions. |

[X §2: explicit dependency rows, line 64](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Bed & Care-Place Management** | `Care-Place Specification Lookup` | **Location Administration** | Resolves physical care-place capabilities (e.g., negative pressure, telemetry wiring) for isolation placement. |

[O §2: Clinical Logistics, line 56](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Logistics** | **Specimen Manifest & Chain-of-Custody Register** | Pathology bio-specimen courier manifests, temperature compliance logs, courier custody signatures, and laboratory delivery receipts. | Does not own pathology diagnostic results. |

[X §2: explicit dependency rows, line 67](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Logistics** | `Secure Clinical Message Dispatch` | **Clinical Communication Admin** | Sends automated electronic specimen tracking notifications and urgent critical result dispatch alerts. |

[O §2: Discharge Management, line 57](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Discharge Management** | **Discharge Readiness & EDD Register** | Multidisciplinary discharge barrier checklists, Estimated Date of Discharge (EDD) records, and departure readiness confirmations. | Does not own clinical discharge summary authoring. |

[X §2: explicit dependency rows, line 68](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Discharge Management** | `Clinical Document Lifecycle Service` | **Clinical Record Administration** | Triggers publication of signed clinical discharge summaries upon patient departure. |

Clinic queues are supporting scheduling/encounter context; theatre progression is limited to the Theatre Operations boundary. Bed operational state is separate from encounter care-place associations and Package 1 location definition. Specimen transit supplies custody and delivery evidence relevant to diagnostic correlation, not authority over analysis or interpretation. Discharge coordination consumes medication reconciliation and document readiness; it does not own prescribing, original document authoring or all care-planning meanings. These are source demarcations, not new Package 2 relationships. T's `Clinical Logistics Coordination` and H/O's `Clinical Logistics` are a further naming variant within K5.

### 6.3 Shared enabling Features, governance roles and information responsibilities

These sources govern shared services or roles; consuming them does not confer their responsibility or originating authority on every Package 2 subject.

[T: 1. Patient Clinical Record (`Harmonia-Core`), lines 588–592](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **1.1 Longitudinal Clinical Record Governance**
>   - `FEAT-ISE-01`: **Longitudinal Health Record (LHR) Assembly**: Assemble disparate encounters, diagnostic reports, medications, and clinical documents into a unified, vendor-neutral longitudinal timeline.
>   - `FEAT-ISE-02`: **Active Clinical Record Access**: Provide low-latency, active distributed access to consolidated patient clinical summaries.
>   - `FEAT-ISE-03`: **Durable Clinical Record Preservation**: Durably maintain governed, vendor-neutral longitudinal clinical representation and preservation with verifiable integrity controls.

[T: 2. Health Information Exchange (`Harmonia-Core`), lines 594–599](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **2.1 Multi-Model Information Exchange**
>   - `FEAT-ISE-04`: **Direct Submission Ingestion & Delivery**: Support push submissions of clinical documents and messages across boundaries.
>   - `FEAT-ISE-05`: **Clinical Record Retrieval Mediation**: Mediate query-based retrieval requests across external and internal repositories.
>   - `FEAT-ISE-06`: **Addressed Clinical Distribution**: Fan out clinical events and diagnostic reports to designated external and internal recipients with per-destination delivery tracking.
>   - `FEAT-ISE-07`: **Event-Driven Syndication**: Publish clinical and directory change events to subscribed systems matching interest filters.

[T: 3. Health Information Access (`Harmonia-Core`), lines 601–604](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **3.1 Governed Query & Search**
>   - `FEAT-ISE-08`: **Federated Clinical Query**: Execute bounded, indexed queries across distributed clinical resources and directory graphs.
>   - `FEAT-ISE-09`: **Access-Controlled Information Filtering**: Filter search results dynamically according to requesting user authorizations and patient consent directives.

[T: 4. Health Information Communication (`Harmonia-Core`), lines 606–608](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **4.1 Boundary Protocol Mediation**
>   - `FEAT-ISE-10`: **Standards-Based Boundary Exchange**: Provide standards-conformant boundary exchange for healthcare messaging and resource representations.

[T: 5. Health Information Control (`Harmonia-Core`), lines 610–614](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **5.1 Policy & Security Governance**
>   - `FEAT-ISE-11`: **Health Information Access & Consent Control**: Evaluate patient consent preferences, practitioner authority boundaries, and access control policies for managed clinical records.
>   - `FEAT-ISE-12`: **Security Context Propagation**: Propagate tamper-evident, attributable security context across asynchronous processing boundaries.
>   - `FEAT-ISE-13`: **Non-PHI Compliance Auditing**: Generate tamper-evident, verifiable audit records of security-significant events without emitting unmasked PHI.

[T: 6. Clinical Knowledge Services (`Harmonia-Relevant`), lines 616–619](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **6.1 Terminology & Reference Mapping**
>   - `FEAT-ISE-14`: **Clinical Concept Resolution**: Resolve local clinical codes against standard terminologies (SNOMED CT, LOINC, ICD-10).
>   - `FEAT-ISE-15`: **Bidirectional Terminology Mapping**: Translate between legacy local code systems and canonical national healthcare profiles.

[T: 7. Clinical Collaboration (`Harmonia-Core`), lines 621–625](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **7.1 LHR-Aware Multidisciplinary Collaboration**
>   - `FEAT-ISE-16`: **Care-Team Clinical Collaboration Coordination**: Coordinate secure multidisciplinary clinical communication and consultation exchange bound to patient encounters or clinical topics.
>   - `FEAT-ISE-17`: **In-Conversation LHR Query Resolution**: Resolve clinical data queries within an authorized collaboration context, presenting verified clinical summaries directly to care team members.
>   - `FEAT-ISE-18`: **Zero-PHI Collaboration Metadata Governance**: Ensure collaboration channel titles, topics, and aliases use opaque identifiers free of patient names, MRNs, or sensitive details.

[T: 9. Workflow & Activity Coordination (`Harmonia-Core`), lines 631–636](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **9.1 Multi-Work-Unit Orchestration**
>   - `FEAT-ISE-20`: **Work Order Progression**: Manage execution states and dispatch of human operational work orders.
>   - `FEAT-ISE-21`: **To Do Decision & Approval Coordination**: Coordinate human clinical review items, sign-offs, and approval workflows.
>   - `FEAT-ISE-22`: **Synthetic Task Orchestration**: Execute non-human, automated platform activity units and sequence orchestration.
>   - `FEAT-ISE-23`: **Workflow Timeout & Escalation Oversight**: Supervise operational deadlines, executing automated timeout notifications and supervisory escalations.

[T: 10. Calendar Management (`Harmonia-Core`), lines 638–641](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **10.1 Operational & Clinical Calendar Sync**
>   - `FEAT-ISE-24`: **Schedule Projection Integration**: Project clinician rosters, on-call schedules, and facility operating calendars into operational views.
>   - `FEAT-ISE-25`: **Temporal Event Correlation**: Correlate incoming clinical events with scheduled appointments and operational calendar milestones.

[T: 12. Information Design Governance (`Harmonia-Core`), lines 647–650](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **12.1 Canonical Semantics & Schema Governance**
>   - `FEAT-ISE-27`: **Information Standards & Semantic Governance**: Govern canonical clinical data definitions, terminology bindings, and exchange profiles across regional systems.
>   - `FEAT-ISE-28`: **Semantic Conformance Verification**: Verify inbound and outbound representations against published semantic rules and conformance constraints.

[E: 2.5 Health Information Control, lines 100–116](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Health Information Control`
> - **Architectural Mandate**: Enforces enterprise default-deny authorization, ABAC/RBAC policy evaluation, consent restriction enforcement, immutable security context propagation, and non-repudiation audit logging.
> - **Functions & Exposed Services**:
>   - **Feature: Health Information Access & Consent Control**:
>     - *Function*: `Evaluate Information Access Authority` — Evaluates user role, organisation, patient relationship, purpose of use, and break-glass overrides against default-deny policies.
>     - *Exposed Service*: `Policy Evaluation & Authorisation Service` — Evaluates access requests across the platform.
>   - **Feature: Security Context Propagation**:
>     - *Function*: `Evaluate Consent Constraint` — Cross-references access requests against client consent directives and sensitive health category restrictions.
>     - *Exposed Service*: `Consent Enforcement Service` — Returns masking and redaction instructions.
>   - **Feature: Security Context Binding**:
>     - *Function*: `Propagate Security Context` — Generates and binds canonical, immutable security contexts to all intra-platform transactions.
>     - *Exposed Service*: `Security Context Validation Service` — Verifies transaction security credentials.
>   - **Feature: Non-PHI Compliance Auditing**:
>     - *Function*: `Record Security-Significant Activity` — Records tamper-evident audit evidence (who, what, when, why, patient MRN, policy outcome) without logging unmasked PHI.
>     - *Exposed Service*: `Security Audit Ingress` — Ingests audit events across all capabilities.
> - **Information Responsibility**: Authorisation Policy Ruleset, Security Context Tokens, Tamper-Evident Security Audit Trail.

[E: 2.6 Clinical Knowledge Services, lines 120–130](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Clinical Knowledge Services`
> - **Architectural Mandate**: Resolves canonical medical ontologies (SNOMED-CT, LOINC, ICD-10, AMT) and executes cross-terminology concept mappings.
> - **Functions & Exposed Services**:
>   - **Feature: Clinical Concept Resolution**:
>     - *Function*: `Resolve Clinical Concept` — Validates concept identifiers and retrieves preferred clinical rubrics and hierarchy paths.
>     - *Exposed Service*: `Terminology Concept Resolution Service` — Exposes concept validation lookups.
>   - **Feature: Bidirectional Terminology Mapping**:
>     - *Function*: `Map Clinical Concept` — Translates local lab codes and legacy terms to canonical SNOMED/LOINC codes using governed translation maps.
>     - *Exposed Service*: `Terminology Mapping Service` ��� Translates codes across terminology systems.
> - **Information Responsibility**: Canonical Clinical Ontologies, Semantic Value Sets, Cross-Terminology Mapping Tables.

[E: 2.9 Workflow & Activity Coordination, lines 163–186](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Workflow & Activity Coordination`
> - **Architectural Mandate**: Coordinates execution state, task dispatch, timers, deadlines, and escalations across three distinct activity archetypes:
>   1. **Work Order**: Assigned physical/human operational tasks (*human doing* — e.g., transport a patient, clean a bay).
>   2. **To Do**: Assigned clinical review or administrative decision tasks (*human reviewing/updating/deciding* — e.g., review abnormal lab result, countersign discharge summary).
>   3. **Synthetic Task**: Automated system-executable activities (*non-human executable work* — e.g., generate summary, syndicate batch, evaluate rules).
> - **Ownership Invariant**: Workflow & Activity Coordination owns **generic activity coordination semantics and timers**. It does **not** acquire ownership of the business meaning or clinical outcome of the activity.
> - **Functions & Exposed Services**:
>   - **Feature: Work Order Progression**:
>     - *Function*: `Coordinate Work Order` — Coordinates operational task lifecycles and dispatches.
>     - *Exposed Service*: `Work Order Coordination Service` — Manages operational work progression.
>     - *Governed Process*: **Work Order Progression Process**.
>   - **Feature: To Do Decision & Approval Coordination**:
>     - *Function*: `Coordinate To Do` — Manages clinical review worklists, reminders, and sign-off queues.
>     - *Exposed Service*: `To Do Management Service` — Provides task inbox and sign-off management.
>     - *Governed Process*: **To Do Progression Process**.
>   - **Feature: Synthetic Task Orchestration**:
>     - *Function*: `Coordinate Synthetic Task` — Orchestrates automated system workflows and operational progression.
>     - *Exposed Service*: `Synthetic Task Execution Service` — Coordinates automated tasks.
>     - *Governed Process*: **Synthetic Task Progression Process**.
>   - **Feature: Workflow Timeout & Escalation Oversight**:
>     - *Function*: `Supervise Activity Timeout / Escalation` — Monitors SLAs and triggers escalations when clinical or operational deadlines elapse.
>     - *Exposed Service*: `Activity Escalation Telemetry` — Discloses SLA breaches.
> - **Information Responsibility**: Generic Activity Instance Registry, Task State Progression Graph, SLA Deadline & Timer Ledger.

[E: 2.12 Information Design Governance, lines 217–236](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md)

> - **Owning Capability**: `L1: Information Design Governance`
> - **Architectural Mandate**: Governs the enterprise canonical information model, data definitions, constraint schemas, terminology bindings, and exchange profiles.
> - **Functions & Exposed Services**:
>   - **Feature: Information Standards & Semantic Governance**:
>     - *Function*: `Govern Information Semantics` — Authors and validates canonical data concepts and entity relationships.
>     - *Exposed Service*: `Semantic Model Definition Service` — Discloses canonical model definitions.
>   - **Feature: Information Definition & Schema Governance**:
>     - *Function*: `Govern Information Definition` — Maintains governed data element dictionaries, data types, and structural rules.
>     - *Exposed Service*: `Information Dictionary Query` — Provides data dictionary specifications.
>   - **Feature: Terminology Binding Governance**:
>     - *Function*: `Govern Terminology Binding` — Binds canonical data elements to authoritative terminology value sets.
>     - *Exposed Service*: `Terminology Binding Specification Lookup` — Discloses value set bindings.
>   - **Feature: Exchange Profile Governance**:
>     - *Function*: `Govern Exchange Profile` — Defines standards-based interchange profiles and message constraints.
>     - *Exposed Service*: `Exchange Profile Specification Query` — Discloses interoperability schemas.
>   - **Feature: Semantic Conformance Verification**:
>     - *Function*: `Verify Semantic Conformance` — Validates candidate messages against canonical governance schemas.
>     - *Exposed Service*: `Semantic Conformance Verification Service` — Validates representations for compliance.
> - **Information Responsibility**: Enterprise Canonical Semantic Model, Governed Data Dictionary, Terminology Binding Matrix, Exchange Profile Catalogue, Semantic Conformance Ruleset.

[R: 2.2 Care Delivery Roles, lines 72–82](../../docs/markdown/03-business-architecture/actors-roles/roles.md)

>
> Care Delivery Roles represent healthcare professionals, clinical teams, and service agents directly involved in clinical decision-making, order placement, or service execution.
>
> - **Practitioner**: An individual registered, certified, or qualified to provide healthcare services.
>   - **Clinician**: A Practitioner actively engaged in direct clinical assessment, diagnosis, treatment, or medical decision-making.
> - **Care Coordinator**: A practitioner or case manager responsible for planning, synchronising, and monitoring cross-specialty or cross-setting care delivery for a patient.
> - **Referrer**: A practitioner or service agent who formally requests another service provider to assess, manage, or assume partial/total clinical responsibility for a patient.
> - **Requester**: A practitioner or clinical agent who formally places an order for a diagnostic investigation, medication supply, or procedural intervention.
> - **Prescriber**: A practitioner legally authorised and clinically responsible for authorising the dispensing and administration of scheduled medications.
> - **Performer**: A clinician, proceduralist, technician, or care agent who executes a diagnostic test, surgical procedure, clinical observation, or therapeutic intervention.

[R: 2.5 Governance / Authority Roles, lines 113–122](../../docs/markdown/03-business-architecture/actors-roles/roles.md)

>
> Governance and Authority Roles represent statutory regulators, policy bodies, and enterprise data stewards responsible for compliance, privacy governance, terminology standards, and system security.
>
> - **Regulator**: A statutory authority responsible for professional registration, healthcare standards enforcement, and accreditation (e.g., AHPRA).
> - **Policy Authority**: An entity responsible for defining enterprise or jurisdictional clinical, security, and operational policies.
> - **System Steward**: An organisational authority responsible for the governance, availability, and lifecycle integrity of digital health systems.
> - **Information Steward**: An authority responsible for defining the governance policies, classification rules, retention periods, and privacy standards for health information assets.
> - **Information Custodian**: An entity or designated role holding operational custody and legal responsibility for safeguarding, preserving, and managing access to health records.
> - **Terminology Steward**: An authority responsible for authoring, validating, and governing canonical value sets, clinical ontologies, and semantic mapping tables.

[R: 2.6 Information / Service Participation Roles, lines 126–132](../../docs/markdown/03-business-architecture/actors-roles/roles.md)

>
> Information and Service Participation Roles represent general participant capacities in service provision and information exchange.
>
> - **Service Provider**: An organisation, facility, or practitioner acting in the capacity of delivering a defined healthcare or diagnostic service.
> - **Information Supplier**: A participant (person, organisation, or system) that routinely produces, transmits, or submits healthcare information, diagnostic results, or clinical documents into the integration environment.
> - **Information Client**: A participant (person, organisation, or system) that queries, retrieves, or consumes healthcare information for clinical review or operational support.

[R: 3.1 Distinct Authority and Membership Concepts, lines 140–146](../../docs/markdown/03-business-architecture/actors-roles/roles.md)

> > **Care Team membership, caring responsibility, advocacy, representation, information-access authority, consent authority, and clinical decision-making authority are distinct concepts and must not be inferred from one another.**
>
> - A **Carer** does not automatically hold **Substitute Decision-Maker** authority.
> - A **Support Person** does not automatically hold **Information Access** authority.
> - A member of a **Care Team** does not automatically have unrestricted authority to access all historical health information outside the clinical encounter scope.
> - **Consent Authority** derives from the patient or legitimate legal representative; **Governance Authority** derives from statutory or organisational mandate. Neither implies the other.

[R: 3.2 Referral Direction is Contextual, lines 148–150](../../docs/markdown/03-business-architecture/actors-roles/roles.md)

> - Do **not** introduce `Referring Provider` or `Receiving Provider` as fundamental architectural Roles.
> - Referral direction is contextual: an organisation or practitioner acts in the stable role of `Service Provider` or `Practitioner`, taking on the contextual qualifier of `Referrer` (source) or `Referee` (target) within a specific `Service Referral` interaction.

[O §2: Patient Clinical Record, line 52](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Patient Clinical Record** | **Governed Longitudinal Health Record (LHR)** | Canonical longitudinal health record synthesis, unified chronological clinical timeline, active problem lists, verified allergy lists, and immutable historical record archives. | Does not own original legal documents; owns the canonical integrated clinical synthesis. |

[O §2: Health Information Exchange, line 58](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Health Information Exchange** | **Exchange Transaction Ledger & Route Matrix** | Ingress message transaction receipts, federated query logs, point-to-point routing tables, and publish-subscribe syndication topic matrices. | Does not own transported message content. |

[O §2: Health Information Access, line 59](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Health Information Access** | **Query Execution Context & Projection State** | Semantic query execution contexts, search indices, and access-qualified temporary result projection state. | Does not own access control policies or queried source records. |

[O §2: Health Information Control, line 60](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Health Information Control** | **Security Context & Policy Ruleset** | Authorisation policy definitions (RBAC/ABAC rules), security context tokens, consent restriction matrices, and tamper-evident security audit trails. | Does not own patient demographic or clinical data. |

[O §2: Clinical Knowledge Services, line 61](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Knowledge Services** | **Canonical Ontologies & Mapping Tables** | Governed clinical terminology structures (SNOMED-CT, LOINC, AMT), value sets, and cross-ontology semantic translation maps. | Does not own patient-specific clinical coded observations. |

[O §2: Clinical Collaboration, line 62](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Collaboration** | **Collaboration Space Metadata & Discourse Index** | Care-team collaboration channel metadata, membership registries, discussion thread indices, and clinical summary contextual projection links. | Collaborative discourse does not constitute authoritative clinical records. |

[O §2: Workflow & Activity Coordination, line 64](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Workflow & Activity Coordination** | **Generic Activity Instance Registry & Timers** | Generic task state progression graphs, timer triggers, deadline escalation rules, and worklist queues across Work Orders, To Dos, and Synthetic Tasks. | Owns generic coordination semantics; does not own business meaning or clinical outcome. |

[O §2: Information Design Governance, line 67](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

| Source owning-capability label | Conceptual Information Asset | Scope (verbatim) | Non-ownership (verbatim) |
| :--- | :--- | :--- | :--- |
| **Information Design Governance** | **Canonical Semantic Models & Conformance Schemas** | Enterprise canonical semantic model definitions, data element dictionaries, terminology binding rules, exchange profiles, and semantic conformance constraint sets. | Does not own runtime instances of data. |

[X §2: explicit dependency rows, line 69](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md)

| Consuming Capability Context | Business Service Consumed | Source exposing / owning label | Purpose (verbatim) |
| :--- | :--- | :--- | :--- |
| **Clinical Collaboration** | `Collaboration Clinical Summary Resolution` | **Patient Clinical Record** | Projects real-time clinical summaries into multidisciplinary care-team discussion feeds. |

[O, line 73](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

> 1. **Stewardship by Owning Capability**: The capability that creates or governs a business information asset is solely accountable for its semantic validity, lifecycle progression, and retention policies.

O §3 assigns capability accountability for semantic validity, lifecycle and retention under the heading “Stewardship by Owning Capability”. R separately defines Information Steward and Information Custodian Business Roles; F-G 6 distinguishes architectural responsibility from the governance role. No individual steward, custodian or assertion originator is inferred from a capability row. T/E generic semantic-governance and terminology services do not establish that Medication Definition or Procedure Definition is independently required.

### 6.4 Exact evidence for scope and layer-assignment tensions

[T: Distinction: Referral vs. Order, lines 252–254](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **Referral Administration**: A **Referral** asks another provider, service, or organization to **assume clinical responsibility** (wholly or collaboratively) for the patient's care. It involves intake review, triage, acceptance/rejection, and care handoff.
> - **Order Administration**: An **Order** requests a defined, bounded **fulfilment activity** (e.g., execute a diagnostic test, dispense a medication, perform a procedure) and strictly supports a **closed-loop progression** returning an outcome to the requester.

[T: Distinction: Technical ACK vs. Business ACK, lines 256–259](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> - **Technical Delivery Acknowledgement**: A transport-level receipt (e.g., MLLP commit, HTTP 200/202, message broker ACK) confirming that bytes crossed the boundary and were durably accepted into an integration queue.
> - **Business Acknowledgement**: A clinical/operational message generated by the receiving application or clinical staff confirming that the order or referral has been reviewed, accepted for execution, scheduled, or rejected.
> $$\text{\bf Technical Delivery Acknowledgement} \neq \text{\bf Business Acknowledgement}$$

[T: Order Closed-Loop Progression Semantics, lines 261–263](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> All order-related capabilities must support the end-to-end closed loop:
> $$\text{Destination} \longrightarrow \text{Route} \longrightarrow \text{Deliver} \longrightarrow \text{Acknowledge} \longrightarrow \text{Progress} \longrightarrow \text{Complete} \longrightarrow \text{Outcome}$$

[T: Clinical Collaboration Context, lines 573–575](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md)

> Clinical Collaboration is **Harmonia-Core** because Harmonia provides **Longitudinal Health Record (LHR)-aware collaboration spaces**. Clinicians can converse within secure multidisciplinary team rooms and query patient context directly (e.g., *"What were Fred's last three HbA1c results?"*).
> *Important Boundary*: A clinical statement made within a collaboration chat does **not** automatically become an authoritative clinical Observation or medical record entry. It remains collaborative discourse unless formally authored into a clinical document.

[F-T: 2.3 Representative Example 3: Clinical Order Administration, lines 96–108](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md)

>
> ```text
> Order Administration (Capability / Feature: Closed-Loop Order Status Tracking)
>     ↓ delivers
> Track Order Fulfilment Status (Function)
>     ↓ establishes
> Clinical Order Record, Order Status History, Order-Result Reconciliation Binding (Information Responsibility)
>     ↓ realised as
> Order (Associated Direction Mechanism)
> Order Routing Directive (Assertion / Instruction)
> Order-Result Correlation Link (Relationship with Provenance & Status)
> ```

[F-T: 1.1 Traceability Principles, lines 31–34](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md)

> 1. **Derivation, Not Redefinition**: Domain 04 does not invent new business capabilities or alter the ownership boundaries established in Domain 03.
> 2. **Purity of Information Responsibilities**: The Business Information Responsibility matrix (`docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md`) is the authoritative source for architectural information responsibilities. Architectural information responsibility is a capability property and is distinct from the governance role of `Information Steward`.
> 3. **Bounded Responsibility Derivation**: Information Concepts for which Harmonia claims architectural responsibility SHALL trace to an owning Domain 03 Information Responsibility. Referenced, consumed, externally authoritative or contextual Information Concepts may be represented where required to discharge a traced Harmonia responsibility, but SHALL NOT thereby acquire Harmonia ownership or authority.

[O: 1.2 Non-Transfer of Ownership Guardrail, lines 10–16](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md)

> Consuming, searching, transporting, caching, indexing, coordinating, presenting, or physically persisting information by another capability **never transfers architectural ownership**:
> - **Health Information Exchange** transports clinical documents, but ownership of the document content remains with *Clinical Record Administration*.
> - **Health Information Access** executes semantic searches across diagnostic reports, but ownership of the diagnostic findings remains with the performing diagnostic provider / *Diagnostic Administration*.
> - **Health Information Control** evaluates access policies, but does not own the patient demographic or clinical data being protected.
> - **Workflow & Activity Coordination** coordinates task timers and state transitions, but does not own the clinical or operational meaning of the task.
> - **Presentation Services** renders clinical summaries on clinician dashboards, but owns only the presentation view configuration, not the clinical facts rendered.

[M: 6. Business Information Responsibility, lines 124–135](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md)

>
> > **Business information ownership derives strictly from Capability responsibility and the Functions/Processes through which that responsibility is discharged.**
>
> - **No Ownership Transfer**: Access, use, transformation, transport, search, presentation, caching, persistence, or coordination of information does not transfer architectural ownership.
> - **Pure Business Semantics**: Business information concepts must retain authentic clinical/business definitions. They must **never** be prematurely translated into:
>   - FHIR Resource structures (e.g., `Patient`, `Observation`, `Task`);
>   - Database schemas, tables, or DDL;
>   - JSON schemas, DTOs, or wire payloads;
>   - Java classes or programming interfaces.
>
> *(Such technology mappings belong exclusively to downstream Information Architecture Domain 04 and Application Architecture Domain 05).*

The last M sentence locates technology mappings in “downstream Information Architecture Domain 04 and Application Architecture Domain 05”. F-M and F-G establish pure semantic Domain04 and downstream representation separation. This is a frozen layer-assignment ambiguity (K13); the current authorised step produces no mapping and makes no correction to either source.

## 7. Unresolved issues for G1 architectural review

### K1 — Referral scope

**Evidence:** [B: 3. Referral Administration, lines 25–40](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [T: Distinction: Referral vs. Order, lines 252–254](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); [R: 2.2 Care Delivery Roles, lines 72–82](../../docs/markdown/03-business-architecture/actors-roles/roles.md); exact extracts in §§3–6.

**Unresolved observation:** B/T emphasise assumption/sharing of ongoing care responsibility; I Service Referral and R Referrer include requests to “assess, manage, or assume”.

**Architectural consequence / review question:** Does the narrow description constrain one referral scope, omit other behaviour, or conflict with the wider interaction/role scope? No Referral definition is established. Bound the source scope before a dependent definition.

### K2 — Signatures and legal scope

**Evidence:** [P: 3.5 Closed-Loop Order Progression Process, lines 113–125](../../docs/markdown/03-business-architecture/processes/business-processes.md); [P: 3.6 Clinical Document Lifecycle Process, lines 129–140](../../docs/markdown/03-business-architecture/processes/business-processes.md); [B: 10. Clinical Record Administration, lines 155–170](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); exact extracts in §§3–6.

**Unresolved observation:** P explicitly requires a signed final diagnostic report in its result-bound disposition and Final Signed/legal sign-off in its document process. B also mentions legal lifecycle, but uses Final rather than Final Signed; F-L is broader.

**Architectural consequence / review question:** Which process/document/report subset carries these obligations? Preserve the actual legal/signature evidence without extending it to every document/report or inferring immutability. This scope must be reviewed before a dependent general definition.

### K3 — Foundation Order example terminology

**Evidence:** [F-T: 2.3 Representative Example 3: Clinical Order Administration, lines 96–108](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md); [B: 6. Order Administration, lines 78–100](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); exact extracts in §§3–6.

**Unresolved observation:** The representative Foundation example uses five exact upstream labels absent from inspected Domain03: Closed-Loop Order Status Tracking; Track Order Fulfilment Status; Clinical Order Record; Order Status History; Order-Result Reconciliation Binding.

**Architectural consequence / review question:** Can the example be retained as illustrative without treating its labels as canonical? Package 2 uses B/O/P evidence. No invented Domain03 trace or frozen-source repair is made.

### K4 — Narrow settings and examples

**Evidence:** [B: 8. Medication Administration, lines 123–138](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [B: 9. Procedure Administration, lines 142–151](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [D: 2.6 Medication Therapy Enablement, lines 102–112](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [H: 2.3 Theatre Operations, lines 56–63](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md); exact extracts in §§3–6.

**Unresolved observation:** Medication administration wording includes bedside/nurse/e-MAR examples; Procedure and Theatre evidence emphasise surgery. D states a non-practice enablement boundary and wider settings.

**Architectural consequence / review question:** Which wording is illustrative, and which is a scope restriction? No setting/actor-specific Medication definition or universal surgical Procedure definition is inferred. Broader support, where missing, remains insufficient.

### K5 — Naming variants and missing Feature identifiers

**Evidence:** [B: 6. Order Administration, lines 78–100](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [B: 11. Care Plan Administration, lines 174–186](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [T: 5. Order Administration (`Harmonia-Core`), lines 291–296](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); [T: 10. Care Plan Administration (`Harmonia-Relevant`), lines 321–323](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); exact extracts in §§3–6.

**Unresolved observation:** O/X abbreviate Episode & Encounter Admin and Clinical Communication Admin; B/T use Administration. D uses enablement names where T names care contexts. H/O Clinical Logistics and T Clinical Logistics Coordination differ. Order Result Association, Care Plan Context Association and Care Plan Update Distribution have no individual Feature ID in T.

**Architectural consequence / review question:** Are names shorthand or materially different scopes? Missing IDs are not assigned. No silent alias/equivalence catalogue or new hierarchy classification is established; behavioural scope differences are separately tracked in K11.

### K6 — Package 1 delivery responsibility

**Evidence:** [P1-S: 2. Domain 03 Responsibility & Traceability, lines 29–39](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md); [B: 7. Diagnostic Administration, lines 104–119](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [D: 1. Contextual Scope & Architectural Intent, lines 3–10](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); exact extracts in §§3–6.

**Unresolved observation:** P1-S cites Performing Clinical / Operational Capabilities including Inpatient Care Enablement and Diagnostic Administration for execution records. D says Harmonia does not practise medicine; B excludes diagnostic analysis/interpretation. O §1.2 couples diagnostic-finding ownership with performing provider / Diagnostic Administration.

**Architectural consequence / review question:** Does execution-record responsibility describe external fulfilment evidence, or is source responsibility inconsistent? Preserve Package 1 meanings and all exclusions; do not infer Harmonia performs medicine or originates clinical outcomes.

### K7 — Acknowledgement and incorporation

**Evidence:** [B: 12. Clinical Communication Administration, lines 190–206](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [T: 11. Clinical Communication Administration (`Harmonia-Core`), lines 325–329](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); [T: Distinction: Technical ACK vs. Business ACK, lines 256–259](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); [E: 2.7 Clinical Collaboration, lines 134–149](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); exact extracts in §§3–6.

**Unresolved observation:** B business ACK includes received/parsed/accepted/filed. T generic business ACK includes review, scheduling, execution acceptance or rejection; FEAT-SA-28 specifies recipient delivery/read receipts. E/T require formal document submission for authoritative collaborative entries, and O excludes message-content ownership.

**Architectural consequence / review question:** What does each ACK assert, from which source, about which recipient process? What incorporation behaviour is actually supported? A filing assertion does not by itself establish a universal communication-to-record transformation, content authority or verification gate. AX-13 bounds claims about external effects.

### K8 — Referrer role ambiguity

**Evidence:** [R: 2.2 Care Delivery Roles, lines 72–82](../../docs/markdown/03-business-architecture/actors-roles/roles.md); [R: 3.2 Referral Direction is Contextual, lines 148–150](../../docs/markdown/03-business-architecture/actors-roles/roles.md); exact extracts in §§3–6.

**Unresolved observation:** R lists Referrer as a Business Role, while its guardrail describes Referrer as a contextual qualifier. Requester and Performer are explicitly Business Roles.

**Architectural consequence / review question:** Does the role/qualifier usage reflect different scopes or a contradiction? F-R/F-G preserve the distinction from a local Relationship Role. Matching labels do not transfer authority; no role catalogue is repaired.

### K9 — Capability metamodel typing

**Evidence:** [CT: Tier 2: Business Enabling Capability, lines 76–84](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md); [M: 1.1 Four-Tier Capability Hierarchy, lines 18–27](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); [B: 3. Referral Administration, lines 25–40](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); exact extracts in §§3–6.

**Unresolved observation:** Strategy separates 16 L1 Business Capabilities from conceptual-tier Business Enabling Capabilities. B/D/E/H/P use L1 owner labels; M gives Order Administration as an L1 example. No explicit individual level is established for the seven corrected plan labels.

**Architectural consequence / review question:** The authorised plan typing correction is complete. Frozen raw labels remain evidence, without adopting their L1 classification. Review the upstream metamodel conflict separately; do not infer L2 from Tier 2.

### K10 — Process/lifecycle variants

**Evidence:** [B: 3. Referral Administration, lines 25–40](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [P: 3.3 Referral Progression Process, lines 82–94](../../docs/markdown/03-business-architecture/processes/business-processes.md); [B: 5. Episode & Encounter Administration, lines 59–74](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md); [P: 3.4 Encounter Lifecycle Process, lines 98–109](../../docs/markdown/03-business-architecture/processes/business-processes.md); [T: 4. Episode & Encounter Administration (`Harmonia-Core`), lines 285–289](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); exact extracts in §§3–6.

**Unresolved observation:** B gives shorter referral/encounter/order/document progressions than P. T encounters include ON_LEAVE; P has encoding/legal closure and other detailed dispositions; B includes Completed. P/B document final/signature states differ.

**Architectural consequence / review question:** Are these summary/detail views, scoped variants or conflicting required states? No union, canonical lifecycle or generalised Information Concept lifecycle is inferred. A business process sequence is not automatically the lifecycle of every information item it uses.

### K11 — Feature behaviour and subject scope variants

**Evidence:** [T: 5. Order Administration (`Harmonia-Core`), lines 291–296](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); [T: 6. Diagnostic Administration (`Harmonia-Core`), lines 298–302](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); [D: 2.1 Primary Care Enablement, lines 32–42](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.5 Diagnostic Services Enablement, lines 88–98](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.6 Medication Therapy Enablement, lines 102–112](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); [D: 2.8 Community Care Enablement, lines 130–140](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md); exact extracts in §§3–6.

**Unresolved observation:** T orders include medication while B/P emphasise diagnostics/procedures. T diagnostic distribution says finalised; B says preliminary/final/corrected. D attaches Medication Administration Tracking to allergy/adverse-reaction correlation, whereas FEAT-SD-12 describes medication event distribution. D Primary Care Notification Dispatch coordinates chronic care plans; FEAT-SD-02 dispatches admission/discharge notifications. D Community Care Plan Synchronization binds support networks; FEAT-SD-16 synchronises plan updates. D diagnostic Features describe requisition feeds/publication where T describes result distribution/history consolidation.

**Architectural consequence / review question:** Do same-named Features encompass both behaviours, differ in abstraction/scope, or require upstream clarification? Do not equate names mechanically, discard one description or decide the information boundary until the relevant scope is reviewed.

### K12 — Dependency owner mismatch

**Evidence:** [E: 2.7 Clinical Collaboration, lines 134–149](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); [X: 2. Cross-Capability Service Consumption Matrix, lines 44–71](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md); exact extracts in §§3–6.

**Unresolved observation:** X exposes Collaboration Clinical Summary Resolution under Patient Clinical Record; E exposes that exact Business Service under Clinical Collaboration.

**Architectural consequence / review question:** Is this a consumed source service, an exposed contextual resolution service or conflicting ownership? The service is not silently moved or renamed. This affects collaboration/record responsibility traceability.

### K13 — Domain03 mapping-layer wording

**Evidence:** [M: 6. Business Information Responsibility, lines 124–135](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); [F-G: Guardrail 3: No Implementation Class or Schema Conflation, lines 21–24](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md); [F-G: Guardrail 4: Shared Physical Mapping, lines 28–31](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md); exact extracts in §§3–6.

**Unresolved observation:** M locates technology mappings in downstream Information Architecture Domain04 and Application Architecture Domain05; frozen Foundation establishes pure semantic Domain04 and separates realised representations.

**Architectural consequence / review question:** Clarify the intended scope of that downstream-layer sentence. It supplies no permission to design realised objects or standards mappings in this task. Both sources remain unchanged and the explicit current scope is preserved.

K1–K8 revalidate the approved plan’s issues. K9–K13 are additional observations from this evidence pass; they are recorded here without rewriting the approved plan. All remain OPEN for G1 review. Recording a tension is not a determination that frozen architecture is invalid. No issue has been resolved from a standard, implementation convention or anticipated model.

## 8. Evidence limits and deferred questions

These limits describe the inspected source set, not proof that information is unnecessary. They must not be filled by healthcare convention. Only a subsequent authorised derivation may turn evidence into Information Requirements and justified candidate dispositions.

| Area / limit | What is actually present | What remains insufficient or undecided |
| :--- | :--- | :--- |
| Episode | Capability title, episode/context language, encounter functions/process and O's “conceptually a healthcare episode” sentence. | Separate Episode Feature, function, process or responsibility; independence, governance, authority, relationship and cardinality. |
| Observation / Finding | Diagnostic observations/reference ranges/abnormal flags, remote measurements, clinical query/review and Performer scopes; finding usages also occur in non-diagnostic review/incident contexts. | Complete definitions, equivalence or distinctness, semantic categories, per-assertion authority, qualification and lifecycle. |
| Condition / Problem / Care Need | Active problem lists; diagnosis/health-problem care-plan associations; condition and preventive/community/social-support contexts. | Complete three-part meanings/governance or their relationships. No standalone “Care Need” / “Care Needs” phrase is found in the frozen Domain03 Markdown source set. The README's general “healthcare needs” wording is preserved below. This is a scoped search result, not a semantic conclusion. |
| Clinical Fact | O uses “clinical facts” descriptively under Presentation Services non-ownership; document/content and original-document/synthesis distinctions exist. | No named Clinical Fact responsibility is established. Its canonical status remains undecided. |
| Medication / Procedure Definition | Generic clinical concept/semantic-governance services; existing Healthcare Service definitions; medication and procedure request/event/document behaviours. | No separately named Medication Definition or Procedure Definition responsibility is found. Generic definition capability is not evidence that either is independently required. |
| Appointment | Explicit external booking engines/master slots; notification, history, synchronisation and projection responsibilities. | Ownership of appointment booking by Harmonia; universal appointment-to-encounter or delivery relationship; general Appointment lifecycle. |
| Communication incorporation | Formal document submission for authoritative collaborative entries; source ACK claims; separate dispatch/content/document/record responsibilities. | A uniform authority/qualification/incorporation rule for all communicated information, or communication itself becoming a record. |
| Process coverage | P's exact 16-process catalogue; four directly named Service Administration processes; theatre, clinic, transit and discharge context where relevant. | No dedicated Appointment/Scheduling, Episode-only, Diagnostic, Medication, general Procedure, Condition/Problem/Care Need, Care Plan or Clinical Communication process in that catalogue. Absence does not require inventing a process. |
| Interaction / collaboration trace | I catalogue definitions and C purposes/governed activities, alongside direct business behaviour and X dependencies. | Exhaustive per-Feature/function/service interaction/collaboration assignments. Relevant catalogue scope is not a newly asserted upstream mapping. |
| Responsibility / governance | Capability responsibilities, explicit non-ownership, governance roles, Foundation assertion-level authority/provenance rules. | Complete originating authority, stewardship, custody, provenance, qualification and effective-time allocations for each eventual concept/assertion/relationship. Receipt/use/indexing/distribution/preservation does not supply the missing allocation. |
| Reusable pattern | Frozen Definition → Contextualisation / Binding → Fulfilment → Outcome → Accountability; Package 1 applications remain frozen. | Applicability to any Package 2 family or candidate. No five-box completion, lifecycle or concept inventory is inferred. |

### 8.1 Scoped exact-name checks

The following reproducible searches examine every `*.md` file under `docs/markdown/03-business-architecture` as captured in the pre-step snapshot. They are case-insensitive text checks, not semantic proofs. Names use literal matching; `Care Need` / `Care Needs` uses word boundaries to distinguish the standalone phrase from “healthcare needs”. None is replaced by a similar-looking label.

| Literal text searched | Domain03 matching files / lines |
| :--- | :--- |
| `Closed-Loop Order Status Tracking` | 0 matching lines |
| `Track Order Fulfilment Status` | 0 matching lines |
| `Clinical Order Record` | 0 matching lines |
| `Order Status History` | 0 matching lines |
| `Order-Result Reconciliation Binding` | 0 matching lines |
| `Medication Definition` | 0 matching lines |
| `Procedure Definition` | 0 matching lines |

Standalone `Care Need` / `Care Needs` check: `\bcare needs?\b` (case insensitive), 0 matching lines. The broader substring `care need` does occur inside the generic term “healthcare needs” in B-INDEX line 7; that occurrence does not establish the three-part definition under investigation.

[B-INDEX, line 7](../../docs/markdown/03-business-architecture/README.md)

> Where Domain 02 answers **what capabilities are required to respond to healthcare needs**, Domain 03 defines **how business participants interact, how capability boundaries deliver and expose behaviour, how operational lifecycles progress, and how conceptual information responsibility is partitioned**.

All 15 Domain03 Markdown files were included. Ownership absence is checked against O §2; dedicated process absence against P §1.2 and its named sections. The three B-only Features in K5 were checked against the complete T catalogue: no individual `FEAT-…` assignment was found. Names and behaviours are not reconstructed from absence.

## 9. Completion verification and G1 handover

### 9.1 Verification actually performed

| Check | Result / limit |
| :--- | :--- |
| Frozen inputs | **PASS** — all 64 pre-step protected-file SHA-256 hashes match; exact fingerprints below. |
| Bounded plan correction | **PASS** — the current approved plan equals the incoming plan with exactly seven authorised label replacements; no other text/structure change. |
| Superseded proposal | **PASS** — original `domain04-package2-clinical-information-families.md` equals its pre-step copy. |
| Evidence capture | **PASS** — eleven investigation areas; source line locations, quotation checks and exact I/C catalogue names validated. All report source/plan links resolve. This checks transcription/navigation, not semantic acceptance of unresolved source tensions. |
| Scope / staging | **PASS** — task changes are the seven plan substitutions and this report. Frozen family/navigation/source documents are unchanged. Incoming staged plan content is preserved; no staging or commit performed. |
| Whitespace | **PASS** — plan diff and new-report whitespace checks pass. |
| Required architecture regression | **PASS** — current run: 90 tests, 0 failures, 0 errors, 0 skipped; Maven BUILD SUCCESS in 16.506 seconds. Command below was bounded at 180 seconds and ran offline. It verifies existing implementation invariants; it does not approve the evidence or resolve architecture questions. |

```bash
timeout --signal=TERM --kill-after=10s 180s mvn -o test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

Local command output: `/tmp/harmonia-package2-step1-architecture-tests.log`; Maven completion timestamp `2026-10-07T13:18:20+11:00`. No additional tests or production changes were introduced.

### 9.2 Review boundary

**Step 1 is complete; G1 is pending architectural review.** Completion means the evidence and its limits have been recorded, not that the source disputes or semantic questions have been settled.

The review can now assess exact capability/Feature/function/service/process/responsibility evidence per area; the limited catalogue/context status of interaction/collaboration associations; the external scheduling and non-practice boundaries; K1–K13; and the explicit missing traces in §8. A material source question must be resolved or explicitly bounded before a later decision depends on it. Any frozen-source correction requires separate authorisation and cannot be silently folded into Package 2 derivation.

No candidate is promoted to canonical status by this report. No new Information Requirement, semantic definition/relationship/cardinality/lifecycle, authority allocation or pattern-applicability decision is established. No Package 2 family document, diagram, FHIR mapping, Data Object, API, persistence model or implementation was created. **Step 2 and every subsequent step remain NOT STARTED.**

## 10. Reproducible source fingerprints

SHA-256 values below are the pre-step baseline and were rechecked after evidence capture. They identify working-file contents, including any pre-existing state, rather than assuming the repository commit alone identifies the authoritative snapshot. The Domain04 files in this list are Foundation and the six Package 1 families/navigation only. No Package 2 information family has been added.

| Protected source | SHA-256 |
| :--- | :--- |
| [AGENTS.md](../../AGENTS.md) | `280c2be9c2464c8ff3c816b568abc88cb154810f5840e8442e924ce9c6223fd5` |
| [docs/AGENTS.md](../../docs/AGENTS.md) | `141bda5f73e4032e60f5df5f069d47989ad2a67a5c444b6b8423365f12dc3dcf` |
| [docs/architectural-axioms.md](../../docs/architectural-axioms.md) | `633b83fe8b3da9658e15fa4c3416df110e1075822d59dcf4f6dc68de6737d6ec` |
| [docs/implementation/harmonia-convergence-runtime-integration-plan.md](../../docs/implementation/harmonia-convergence-runtime-integration-plan.md) | `2217e091dd509ea66bdd239ee2660f37fc5aac744a644a82f4a53c221210423b` |
| [docs/markdown/01-motivation/README.md](../../docs/markdown/01-motivation/README.md) | `e3578022dbb01fe12d66269b9b58198f8ca4dffe3bb5c659a4f92865d129b248` |
| [docs/markdown/01-motivation/orientation-view.md](../../docs/markdown/01-motivation/orientation-view.md) | `d284df3f3cce1d6a476984e6bb8ce9c83f2a513ea3e092f9fef9eb84a0f722cc` |
| [docs/markdown/01-motivation/goals-outcomes/business-outcomes.md](../../docs/markdown/01-motivation/goals-outcomes/business-outcomes.md) | `a825c2294c84b9117f5a594404ff277510f4414d252fa4d460e2eea62cd621d0` |
| [docs/markdown/01-motivation/goals-outcomes/strategic-goals.md](../../docs/markdown/01-motivation/goals-outcomes/strategic-goals.md) | `84f577d30e57b831b28a00135d7fd9e2431fe002dc50f108022738432641d093` |
| [docs/markdown/01-motivation/stakeholders/enterprise-stakeholders.md](../../docs/markdown/01-motivation/stakeholders/enterprise-stakeholders.md) | `04283275d49dd9e8cb6e96c13a7b7a86ceec165fd341ce2f3e36a4beda4d4d25` |
| [docs/markdown/01-motivation/stakeholders/external-authorities.md](../../docs/markdown/01-motivation/stakeholders/external-authorities.md) | `d89dd970d9833fa5fc593abb2301e79e7f3af9fecacbc39f4b8c2d8a153aead6` |
| [docs/markdown/01-motivation/requirements-constraints/external-constraints.md](../../docs/markdown/01-motivation/requirements-constraints/external-constraints.md) | `6b44e6d48cd10e8391c0f213e7077947cec588d42900da7fc3529a9667006736` |
| [docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md) | `77b87a5a110375b0065310e469c01ac3bda32843b81fc204816b958ba286c31a` |
| [docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md](../../docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md) | `2d9cf4938cbf9ee1547b26700b8f50f98d3e8c6d5f58dcdfcc345016413a8e52` |
| [docs/markdown/01-motivation/drivers-assessments/drivers.md](../../docs/markdown/01-motivation/drivers-assessments/drivers.md) | `0e33b045fc15cceac34ff3f9954c41a1121806fff46e2e8d34c87c2dba24c9fd` |
| [docs/markdown/01-motivation/drivers-assessments/assessments.md](../../docs/markdown/01-motivation/drivers-assessments/assessments.md) | `c3a1a0814093a71b78b9462667af7e1687bf859ca882cb1465b8863f31dc9cb7` |
| [docs/markdown/01-motivation/principles/reclassified-principles.md](../../docs/markdown/01-motivation/principles/reclassified-principles.md) | `4b2e5bfa6d1ba4a3078732371c513e708ae4437620230b2beb1d9ba9537ddd5b` |
| [docs/markdown/01-motivation/principles/architectural-axioms.md](../../docs/markdown/01-motivation/principles/architectural-axioms.md) | `3a85359443bdb8669a36b7586cfba030af543d235eb0325388bdd3b11b063c70` |
| [docs/markdown/02-strategy/README.md](../../docs/markdown/02-strategy/README.md) | `18068839f01c61a0e953f3ef51fe9a8d8bfb05b18a66e6f83e8088ecefe7a16d` |
| [docs/markdown/02-strategy/strategic-views/index.md](../../docs/markdown/02-strategy/strategic-views/index.md) | `6b012f6ab852086b34b7ddb8f6f3feb33fa035ae5e65a1f588946bc390c33f78` |
| [docs/markdown/02-strategy/strategic-views/strategic-value-streams.md](../../docs/markdown/02-strategy/strategic-views/strategic-value-streams.md) | `c6cd18e6ec74389b4e790263588dd8fb3869693a63c2c6e98c9218d9a190555a` |
| [docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md) | `ab626d62f9229be09be50231104f9ec9dc41d659aaf92684502821a75d7df1b1` |
| [docs/markdown/02-strategy/courses-of-action/index.md](../../docs/markdown/02-strategy/courses-of-action/index.md) | `4f8376877c91afdfb2c33d9a006982d9a461aa427339d461b238efa92496b1d3` |
| [docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md](../../docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md) | `d20544fa128b6a8665eced0e4f933ae6adca22ca1ccf48491c660061a53b855c` |
| [docs/markdown/02-strategy/resources/index.md](../../docs/markdown/02-strategy/resources/index.md) | `a638b0f16a03613f19c604c95b0f1a4e503d16c462cecfbc19b82f5c80328a5e` |
| [docs/markdown/02-strategy/resources/strategic-resources.md](../../docs/markdown/02-strategy/resources/strategic-resources.md) | `35b748edc3106e4982a8563f9fc3cc378c1c330ca886b4b34fbeb05060e8efac` |
| [docs/markdown/02-strategy/capabilities/index.md](../../docs/markdown/02-strategy/capabilities/index.md) | `6250736f398ff5c9a6db86e207c5913cf88a7b52ad0d650951780d567856d8fd` |
| [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) | `66578a1a47a8d9433e1c0a6bb912245fe9c11def1c7b2e489faf9bdc548d3398` |
| [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../docs/markdown/02-strategy/capabilities/business-capabilities.md) | `9c5c02a79938723cf9855db28408e9357557f96a1857f92a1b4fac869ac4ff08` |
| [docs/markdown/02-strategy/capabilities/enterprise-capabilities.md](../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md) | `7e6ab475c24d92c1f74fb71edae59f5c9547aa721e3c8db896165928af647f39` |
| [docs/markdown/02-strategy/capabilities/ict-foundation-lenses.md](../../docs/markdown/02-strategy/capabilities/ict-foundation-lenses.md) | `ca23dfa60ed3c78aa5911222cdd14d70ddf39f1305f072a4803d5ba47042781a` |
| [docs/markdown/02-strategy/capability-maps/index.md](../../docs/markdown/02-strategy/capability-maps/index.md) | `787af457a79c41c576c220d2e53a1b8a9d7f28d76effaa01b83f54e7ad3b66dc` |
| [docs/markdown/02-strategy/capability-maps/capability-tier-model.md](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md) | `33964cfb94c056668dc06773fb56ca52b81cac04170ca13c27a1fdbf1c1fa5ff` |
| [docs/markdown/03-business-architecture/README.md](../../docs/markdown/03-business-architecture/README.md) | `3289874b7e1826cefea569a1dfb70a9d9d7db4322c684c85cedfeb1a76327c3e` |
| [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md) | `efc3e08976bd85d1c4b7ad8f0baab3536696bf8d3f86b28d61a5558ac329440e` |
| [docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md) | `fc7ae4439fcd44bb1bc147be76a4e1bb3072c690d82b0e668cad7fe4857ed264` |
| [docs/markdown/03-business-architecture/behaviours/index.md](../../docs/markdown/03-business-architecture/behaviours/index.md) | `a65426ca8496d44d6766d80d8c56cbf1f6b66e2570c35f912e54497c94859cc4` |
| [docs/markdown/03-business-architecture/behaviours/01-entity-management.md](../../docs/markdown/03-business-architecture/behaviours/01-entity-management.md) | `2937517dab56c8d9a94f38b9eacc58308fa65a1441f440aa15b872c0f2e2327c` |
| [docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md) | `b58e54f2c6876d08e19ad317d76113e1413d44ad0aa3d1eb8c65ab4a8a50566f` |
| [docs/markdown/03-business-architecture/behaviours/03-service-delivery.md](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md) | `e68194af1425d0e36e4f0cc75c8f799a051623f601c1317bd3f64278a42a33a7` |
| [docs/markdown/03-business-architecture/behaviours/02-service-administration.md](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md) | `9b38c30242b2ca2d9b038b02e2568b74a998da89f8ab712814f524dd288ca035` |
| [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) | `389e24dc28505f547ecfeb27c29bcaa8ca24a0964930877ae400b1db016246fd` |
| [docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md) | `046f5a3a0969b9b3f6f16528888c4367f2fff6a79a6e0f485ded72369b2419ed` |
| [docs/markdown/03-business-architecture/collaborations-interactions/interactions.md](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md) | `dd66da80713042ca1748826c4e1f68ddb62ba44526595ae6cfb318080df558fb` |
| [docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md) | `bd2b12095a4a0de266dc1aedb4b5a40632bc8e9573f3783ff478783acb741263` |
| [docs/markdown/03-business-architecture/processes/business-processes.md](../../docs/markdown/03-business-architecture/processes/business-processes.md) | `492a1199b731f9d2b52a62a5b3145cebc07bb85de172a3653053d43b8b8e2af6` |
| [docs/markdown/03-business-architecture/actors-roles/roles.md](../../docs/markdown/03-business-architecture/actors-roles/roles.md) | `2490a48eb946d41a2026f3df28b40c660f1775385228971a48c30cc9e391d6b2` |
| [docs/markdown/03-business-architecture/actors-roles/actors.md](../../docs/markdown/03-business-architecture/actors-roles/actors.md) | `67bb617bb5a0c717a16862575003d8e96c66e2113bc2da5aeec7b6b30ed1e365` |
| [docs/markdown/04-information-architecture/README.md](../../docs/markdown/04-information-architecture/README.md) | `81ecde7115b6bf7cf3395e71b1ba19e6a7d97539b6118077d58c2844cda1c39c` |
| [docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md) | `6a1d8a99ed7c79e8c81f19c18b04a12b57456f732469d9bcd6fee03e26d5bd3a` |
| [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) | `689669149a7f6e122ed3cb4f320719ca7f8293276da34d8efe3c95115c541b0d` |
| [docs/markdown/04-information-architecture/governance/authority-custody-provenance.md](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md) | `4897d11db77de992df7490acdcf447f2c60b3cfe52004b471c65e4de84dd57d4` |
| [docs/markdown/04-information-architecture/governance/information-lifecycle.md](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md) | `04ac6605e9bbaaca0b62154d1fcf011eb1f67c7d17ae0ad31dde97503e462508` |
| [docs/markdown/04-information-architecture/patterns/information-relationships.md](../../docs/markdown/04-information-architecture/patterns/information-relationships.md) | `a674fef9a3b50e17568ee5d3602780492b022a11fd894664d2653bbab502de1f` |
| [docs/markdown/04-information-architecture/patterns/definition-to-accountability.md](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md) | `457e6fda71d1ba1ca6a8900f0a129c1be42c96effc3ff9f98ae3e0f135293dcc` |
| [docs/markdown/04-information-architecture/patterns/containment-and-collections.md](../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md) | `1e86cb0e205a304c312fbad151766ecc9bce6de7272892e2ce8558b10e30a26d` |
| [docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md) | `7191d414ce2511e899fffaa2dc8dbccf80d74eacc774a03bc721266551a9e2de` |
| [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md) | `1c9ea3a3f8bb4642abdbcc7939081f3dd4a65b75634573e841575589609a50a7` |
| [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md) | `93458260567b2947cc78856388b62dca88d830bf8cda51c6c2af3a2bc5133273` |
| [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md) | `af57a877a0d88c9590d55f0cc9e9f1893c0d87d010438bf167d7b368b236dedd` |
| [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md) | `5392b13633e63724d632c79a2c8c18b2dd22235320b35f931de81690fb189446` |
| [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md) | `a5d7d21ecf7a35d9e50d37f2b57cbbe6c138fe724662639c7eee6b7bb4efc0c8` |
| [docs/markdown/04-information-architecture/information-families/README.md](../../docs/markdown/04-information-architecture/information-families/README.md) | `3982d5aabb4bf9805c6113fff97cfee21c5b76b59b8a680965e881937bb16346` |
| [docs/markdown/04-information-architecture/information-families/device.md](../../docs/markdown/04-information-architecture/information-families/device.md) | `2dc66e7f8091c0215608eba4e41e9f35a7038450b24e2ed4d34094c1ef2bed06` |
| [docs/markdown/04-information-architecture/traceability/domain03-traceability.md](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md) | `20a168df6cd4efb68e03b90b60b29c9f69bafca9360ea0252b9a526b3090f1a0` |

**Working-plan fingerprints (non-authoritative):** incoming approved plan `85355c6f047be9bbd9b875da45c90e7e39ff606087635b475e52fbfd164ff35c`; corrected approved plan `d382df9b757679eccd32c286ca304ae4344e795b24f8ea05f82ab89e6fb1d3e3`; superseded original proposal `8ace145233a96bde9c27dbfd8acd3bc964c1b3aaf11e39ee59723c52a75e80b9`.
