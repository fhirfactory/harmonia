# Health Service Assurance — Bounded Business Architecture Derivation

## 1. Authority and Derivation Scope

This view carries the [approved Domain02 Health Service Assurance derivation](../../02-strategy/capability-maps/health-service-assurance-derivation.md) into Domain03. Its upstream responsibility chain is [REQ-FND-005 — Independent Assurance of Governed Activity](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity) → [BC-18 Health Service Assurance](../../02-strategy/capabilities/business-capabilities.md#18-health-service-assurance) → **Assurance Design**, **Assurance Criteria Management** and **Governed Assurance**, with the approved collaborative Enterprise Capability contributions including **EC-14 Service Guardian**.

[AX-14](../../../architectural-axioms.md#ax-14--semantic-distinctions-are-preserved) requires the responsibility distinctions below to remain explicit. [AX-17](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty) and the [Domain03 metamodel](../metamodel/business-architecture-metamodel.md) require established responsibility to be distinguished from unestablished naming, decomposition, exposure, progression and authority relationships. AX-06, AX-07, AX-08 and AX-09 continue to govern information authority, governed access, meaningful evidence and contextual evidence selection. AX-15 governs operational uncertainty without making it equivalent to insufficient assurance evidence.

The bounded derivation establishes the **[Guardian Business Role](../actors-roles/roles.md#guardian-governed-assurance)** and documents directly supported business responsibility consequences. It does not allocate actors to that Role or name new Functions, Services, Processes, Interactions or Collaborations. The three assurance capabilities remain established responsibility contexts with unresolved contextual-view placement, Capability Tier, complete ancestry, Feature decomposition and structural Canonical IDs. This document is a capability-scoped view, not a sixth contextual view or an identifier ancestor.

**EC-14 Service Guardian is an Enterprise Capability. Guardian is a Business Role.** The EC contribution model does not establish Role identity, Business Function ownership, Business Service exposure or a Business Collaboration. The Guardian Role is derived from the approved independent-assurance responsibility; it is not a reclassification of EC-14.

## 2. Governance, Management, Guardianship and Clinical Authority

```text
Governance defines
       ↓
Management performs
       ↓
Guardianship assures
```

This expresses semantic responsibilities, not organisational structure, a compulsory execution sequence or technical topology.

| Responsibility | Business meaning | Preserved boundary |
| :--- | :--- | :--- |
| **Governance** | Defines applicable requirements, obligations, authority, constraints, controls, reporting requirements and checkpoints. | Establishes the normative envelope without taking over the operational means or progression of the governed activity. Subject-specific governance remains intrinsic to its established subject capability. |
| **Operational management** | Performs or manages the subject activity within its constraints, including monitoring for progression, assignment, delegation, operational escalation, response, remediation and expected failure/recovery. | Monitoring for operational completion or deciding what happens next does not establish independent assurance. |
| **Guardianship / governed assurance** | Independently evaluates governed activity, information, state or outcomes against applicable criteria and establishes findings and conclusions from sufficient trustworthy evidence. | A Guardian may manage its own assurance activity; it does not thereby perform or manage the subject, direct its operational response or confer clinical authority. |
| **Clinical review / clinical assurance** | Exercises the applicable clinical process and authority's responsibility for clinical evaluation and professional judgement. | Clinical adequacy, clinical correctness and professional clinical judgement are not established by the Guardian Role. Clinical quality, safety and improvement retain their existing responsibilities. |

A real-world Actor may fulfil different Roles in different contexts where governance permits. That possibility does not relax REQ-FND-005: assurance progression and conclusion must remain independently governed rather than controlled by the performer or manager of the subject. The subject may contribute evidence but must not solely determine, suppress, manufacture or retrospectively alter its assurance conclusion. Evidence dependency and control dependency remain distinct.

Information concerning clinical activity may be subject to Guardianship where an explicit applicable governed requirement establishes that concern. This does not constitute **Clinical Services Delivery Assurance** or transfer clinical-care responsibility to Harmonia. The [approved clinical boundary](../../02-strategy/capabilities/business-enabling-capabilities.md#clinical-services-delivery-assurance-boundary), [Service Delivery care-enablement principle](03-service-delivery.md#the-fundamental-care-enablement-principle), and existing clinical review responsibilities remain controlling. No generic clinical-assurance authority Role is derived.

## 3. Direct Capability-Scoped Responsibility Consequences

These are responsibility consequences of the approved definitions, not a catalogue of newly named Business Functions or processes. Guardian's evaluation/conclusion responsibility is established within **Governed Assurance**. Responsibility for performing Assurance Design or Assurance Criteria Management is not automatically assigned to Guardian.

| Established owning responsibility context | Business behaviour directly supported by approved Strategy | Derivation limit |
| :--- | :--- | :--- |
| [**Assurance Design**](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-design) | Determines and defines how satisfaction will be assured, including assurance disposition, applicable criteria, required evidence and evaluation expectations. | Does not redefine the governing requirement or establish who approves the disposition. Its peer relationship with behaviour and failure/recovery design does not create additional capabilities or Functions. |
| [**Assurance Criteria Management**](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-criteria-management) | Establishes and manages reusable, governed, temporally identifiable assurance criteria. | Criteria may operationalise a governing requirement without silently altering, weakening, strengthening or replacing it. Not every criterion must derive from formal Governance. Approval authority and detailed lifecycle remain unestablished. |
| [**Governed Assurance**](../../02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance) | Establishes the governed subject/context and applicable criteria; assembles contextual evidence associations; assesses evidence; adjudicates assessed detail; establishes findings, exceptions and conclusions, preserving evidence insufficiency. | Owns assurance evaluation and conclusion responsibility, not source information or the subject's operation, remediation or clinical judgement. The Guardian Role expresses this responsibility without determining its internal Function decomposition or execution mechanism. |

The approved [assurance dispositions](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-disposition) remain explicit governed decisions. **Not Assurable** does not remove the requirement; **Not Worth Assuring** does not make it optional. Disposition does not require specific assurance of every individual activity, information item or outcome.

The approved [generic processing assurance obligation](../../02-strategy/capabilities/business-enabling-capabilities.md#generic-processing-assurance-and-system-operations) constrains Harmonia-managed workflow behaviour. Evaluating business progression, completion, timeliness, pathways, failure/recovery and outcomes for assurance is distinct from monitoring to progress that work. The existing operational Processes and Functions retain their owners; neither successful completion nor an operationally healthy environment establishes an assurance conclusion. No final criterion catalogue or applicability model is supplied by these examples.

Assessment asks what the evidence demonstrates against applicable criteria; adjudication establishes what finding or conclusion follows. These meanings do not mechanically require separate Functions, Roles or process stages. Quantitative, qualitative, compound, temporal and confidence-bearing assessment remain possible; assurance is not universally reducible to PASS / FAIL / UNKNOWN. An assurance activity may execute correctly while lacking enough evidence to conclude. That insufficiency is distinct from its own execution failure or indeterminate execution outcome and must not imply satisfaction or non-satisfaction.

## 4. Information and Dependency Consequences

The [Business Information Responsibility material](../information-responsibility/information-responsibility.md#4-assurance-related-business-information-responsibilities) records the directly supported demarcations: Assurance Design defines how satisfaction will be assured; Assurance Criteria Management manages criteria; Governed Assurance owns contextual evidentiary association and its findings/conclusions. Underlying source information remains owned and managed by its established capability. Evaluating it does not acquire its originating authority.

Evidence association must distinguish the relevant information state from its current state where temporal/version-specific inclusion matters. Evaluation must use temporally appropriate evidence and criteria. Evidence collection, provenance, audit information and operational completion are not themselves assurance conclusions. No assurance Information Object, outcome taxonomy, representation, custody, retention rule or persistence structure is defined here.

An evidence need does not identify a Business Service/provider or authorise a new consumption edge. The [dependency assessment](../dependencies/cross-capability-dependencies.md#4-assurance-dependency-derivation-boundary) preserves that distinction, including the prohibition on subject control of assurance progression or conclusion. The approved Enterprise Capability contribution matrix remains a Strategy contribution model, not a Business Service consumption matrix.

## 5. Additional Business Elements Not Yet Established

The following candidate concerns are explicitly **unresolved and non-authoritative**. They are not allocated canonical element names or IDs, and their inclusion does not add them to a catalogue.

| Candidate concern assessed | What approved architecture establishes | Architectural decision required before adding an element |
| :--- | :--- | :--- |
| **Business Functions** for design/disposition, criteria management, evidence assembly, assessment, adjudication or reporting | The three named owning capability responsibilities and distinct assurance semantics above. | Approve Function names, granularity and decomposition within those responsibility contexts; determine any Feature association. The EC-14 conceptual sequence does not settle these choices. |
| **Exposed Business Services** for criteria, evidence association, evaluation or findings/conclusions | Reusable criteria, evidence needs and reportable conclusions; findings may inform operational response. | Identify the behaviour actually exposed, an identifiable consumer outside its owner, the exposing owner and the Business Service contract. Reportability and reusability alone establish none of these. |
| **Business Processes** for assurance activity or criteria lifecycle | Independently governed assurance progression and potentially temporal criteria; management of assurance's own activity is permitted. | Establish the activity instance, bounded process responsibility, initiation/completion conditions, meaningful states, dispositions, transitions and insufficiency treatment. EC-14's explanatory sequence and existing coordination lifecycles are not assurance process specifications. |
| **Business Interactions** for evidence contribution, assurance initiation or communicating findings | Subjects may contribute evidence and conclusions are reportable. | Establish participating Actors/Roles, exchange purpose and commitments, initiation/response/outcome semantics and a canonical Interaction name. Clinical Review Request/Outcome and operational alerts cannot supply these through similarity. |
| **Guardian collaborations** | Guardian is a stable assurance capacity; capability realisation is collaborative. | Establish an enduring, structured Actor/Role association with a defined purpose and permitted control relationships. Enterprise Capability collaboration does not establish a Business Collaboration or Guardian membership of an existing one. |
| **Responsibility relationships** beyond Guardian evaluation/conclusion | Assurance, governance, operational management, source-information ownership and clinical authority remain separate. | Decide eligible Actors, assurance mandates, coverage/applicability, disposition and criterion approval authority, and any additional Role assignments. Guardian does not automatically own design, criteria approval, operational response or clinical review. |
| **Further Business Information responsibilities** | The responsibility demarcations and temporal/contextual evidence obligations in §4. | Establish any additional asset names, decomposition, authority/approval, custody, lifecycle and retention responsibilities. No source-information reassignment or exhaustive assurance-information catalogue is inferred. |

The [Role catalogue](../actors-roles/roles.md#guardian-governed-assurance) also records the incoming care-support use of **Guardian** and any unresolved canonical identification. A Role name or identifier collision must not be silently resolved by borrowing a namespace or context convention.

## 6. Bounded Outcome and Navigation

The role and direct responsibility boundaries are established; additional Business elements remain unresolved as recorded above. Existing metamodel abbreviations **CT1 / CT2 / CT3 / FT** and **FN / SV / PR**, established Feature identifiers and the Qualified Architectural Reference Grammar remain unchanged. No unresolved tier, ancestry, Feature placement or structural identifier is filled to accommodate assurance.

This derivation stops in Business Architecture. Information Architecture and all application, runtime, execution, representation, persistence and deployment allocation remain downstream. The completion report is execution evidence and confers no architectural authority.

- [Domain03 orientation](../README.md)
- [Capability-scoped behaviour index](index.md)
- [Guardian Role and boundaries](../actors-roles/roles.md#guardian-governed-assurance)
- [Collaboration derivation boundary](../collaborations-interactions/collaborations.md#44-guardian-collaboration-derivation-boundary)
- [Interaction derivation boundary](../collaborations-interactions/interactions.md#4-guardianship-and-clinical-review-boundary)
- [Process derivation boundary](../processes/business-processes.md#6-governed-assurance-process-derivation-boundary)
