# Domain04 Package 2 — G1 Architectural Review Record

**Record type:** Architectural review and decision record

**Review scope:** Package 2, G1 — K1: Referral Scope; K2: Signatures and Legal Scope; K3: Domain04 Foundation Order Traceability Terminology; K4: Narrow Settings / Examples; K5: Naming Variants and Missing Feature IDs; K6: Package1 Delivery Responsibility; K7: Acknowledgement and Incorporation; K8: Referrer Role Ambiguity; K9: Capability Metamodel Typing (approved identification prerequisite and final disposition); K10: Behaviour versus Business Process Progression / Detail; K11: Behavioural Scope Differences; K12: Dependency / Service Ownership Mismatch; K13: Domain03 Mapping-Layer Wording / Domain04 Boundary

**Decision date:** 2026-10-07

**G1 status: G1 — CLOSED.**

**Closure date:** 2026-10-07.

Sections 2–15 retain the approved decision-time dispositions and their historical stop boundaries. The separately authorised reconciliation is recorded in §16; statements about later reconciliation in those sections describe the decision-time state, not an additional approval requirement. No K1–K13 disposition is reopened.

## 1. Purpose and authority

This record retains the approved G1 architectural interpretations of K1, K2, K3, K4, K5, K6, K7 and K8 as durable review context for subsequent Domain04 Package 2 derivation. The K1–K8 approval applies to those entries only; it does not approve G1 as a whole or resolve any other G1 issue. K2 retains explicitly unresolved semantics within its approved disposition. K4 retains explicit scope uncertainties within its approved disposition. K6 retains explicit semantic and responsibility/authority uncertainties within its approved disposition. K7 retains contextual acknowledgement semantics and explicitly unresolved incorporation semantics within its approved disposition. K8 retains the Business Role / contextual Referral participation boundary, an upstream typing reconciliation requirement and explicit eligibility, participation and authority uncertainties within its approved disposition.

Section 10 retains the approved Business Architecture identification metamodel as a K9 prerequisite. Section 11 records the final approved K9 disposition, closing K9 while preserving unresolved Capability Tier, ancestry and structural Canonical ID information. K9 closure does not complete source reconciliation or approve G1 as a whole.

Section 12 records the final approved K10 disposition: Behaviour and Process are complementary representations within established responsibility. The three separately discovered progression/timing uncertainties remain unresolved for later architectural clarification or reconciliation; they do not keep K10 open. K10 closure does not complete source reconciliation or approve G1 as a whole.

Section 13 records the final approved K11 disposition, preserving legitimate behavioural scope differences and recording five Feature-to-behaviour associations as not established. No replacement Feature mapping is required to close K11. K11 closure does not complete source reconciliation or approve G1 as a whole.

Section 14 records the final approved K12 disposition, preserving Clinical Collaboration Service ownership and rejecting the unsupported dependency-matrix attribution to Patient Clinical Record. Unknown dependency relationships remain explicit without requiring replacement mappings. The cross-G1 evidence in §14.6 now traces to authoritative AX-17; its earlier candidate standing is retained as history, not current normative status. K12 closure does not complete source reconciliation or approve G1 as a whole.

Section 15 records the final approved K13 disposition, preserving Domain04 semantic/conceptual mapping and placing realised representation mappings downstream. It records the Domain03 metamodel allocation defect and the two directly related Domain02/Domain03 README handoff defects. K13 supplies additional historical evidence for the principle subsequently formalised centrally as AX-17. K13 closure does not complete source reconciliation, approve G1 as a whole or authorise commencement of G2.

The [Architectural Axioms](../../../architectural-axioms.md), repository guardrails and frozen architecture remain governing authorities. Approval of dispositions and permission to reconcile sources were separate at decision time. The subsequently authorised controlled G1 reconciliation applies only approved K1–K13 effects and AX-17; §16 records its bounded changes and verification.

The [Package 2 evidence baseline](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md) remains the evidence register, including the frozen-source quotations and fingerprints captured on 2026-10-07. This record preserves the architectural decisions arising from that evidence. It is not a completion report, execution history, replacement evidence baseline or Package 2 Information Family.

## 2. K1 — Referral Scope

**Status: RESOLVED — APPROVED**

**Disposition:** Resolve the scope tension through the approved broader Business Architecture interpretation. The narrower upstream wording is overly narrow and requires later G1 reconciliation. The outstanding wording correction and explicitly retained uncertainties do not reopen the approved K1 scope decision.

### 2.1 Approved interpretation

A Referral formally requests another care participant or service provider to assess, manage, or assume some degree of clinical care responsibility for a subject of care.

Its purpose may include establishing shared responsibility or transferring ongoing care responsibility.

The Referral itself does not establish acceptance, service delivery, assumption of responsibility, or transfer of care.

The following semantic distinction SHALL be preserved:

```text
Referral intent
    ≠ Referral disposition
    ≠ responsibility actually assumed
    ≠ Transfer of Care
```

`assess`, `manage`, and `assume` represent alternative and potentially overlapping requested purposes or responsibilities. They are not established as lifecycle stages or a mandatory Referral subtype taxonomy. `partial` / `total` SHALL NOT be encoded as an architectural taxonomy at this stage.

### 2.2 Frozen evidence basis

| Frozen source | Architectural type | Material evidence |
| :--- | :--- | :--- |
| [Domain02 Business Enabling Capabilities — Referral vs. Order](../../02-strategy/capabilities/business-enabling-capabilities.md#distinction-referral-vs-order) | Business Enabling Capability semantic constraint | Referral asks another provider, service or organisation to “assume clinical responsibility” wholly or collaboratively; the description includes intake, triage, acceptance/rejection and care handoff. Order instead requests defined, bounded fulfilment with closed-loop progression. |
| [Domain03 Service Administration — Referral Administration](../../03-business-architecture/behaviours/02-service-administration.md#3-referral-administration) | Capability-scoped semantic constraint; Business Functions and Business Services | Referral is characterised through “assumption or sharing of ongoing clinical care responsibility.” `Receive Referral`, `Assemble Referral Context` and `Manage Referral Progression` discharge receipt, context assembly and disposition tracking through exposed services. |
| [Domain03 Business Roles — Referrer](../../03-business-architecture/actors-roles/roles.md#22-care-delivery-roles) | Business Role catalogue description | Referrer formally requests another provider to “assess, manage, or assume partial/total clinical responsibility.” The source's partial/total wording is evidence, not an approved taxonomy. |
| [Domain03 Business Interactions — Service Delivery Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#22-service-delivery-interactions) | Business Interactions | `Service Referral` requests assessment, management or assumption of care responsibility. `Service Response`, `Service Outcome`, `Transfer of Care` and `Service Coordination` are separately defined interactions. Transfer of Care is the formal transition of clinical and medico-legal accountability. |
| [Domain02 Business Enabling Capabilities — Referral Administration](../../02-strategy/capabilities/business-enabling-capabilities.md#2-referral-administration-harmonia-relevant) | Referral Administration Features | `FEAT-SA-03` and `FEAT-SA-04` cover ingestion and supporting information. `FEAT-SA-05` tracks progression and communicates outcomes including acceptance, decline or redirection. |
| [Domain03 Principal Business Processes — Referral Progression](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process) | Business Process | Separately describes submission, intake validation, clinical triage, acceptance/waitlisting, scheduling, attendance and discharge/rejection. Attendance includes initial specialist assessment; rejection is an explicit disposition. |
| [Domain03 Information Responsibility & Ownership — ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) | Business Information Responsibility | Referral Administration owns incoming packages, indications, priority categories, acceptance decisions and disposition histories. It does not own closed-loop diagnostic or therapeutic orders. |
| [Domain03 Business Collaborations — participation model](../../03-business-architecture/collaborations-interactions/collaborations.md#2-the-actor-role-interaction-collaboration-relationship) | Business Collaboration model and existence constraint | Interactions may execute independently or within a collaboration. Patient Collaboration is longitudinal; Care-Team Collaboration supports shared planning and handovers. A discrete interaction does not itself establish an enduring collaboration. |
| [Domain03 Cross-Capability Dependencies — service consumption matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) | Cross-capability Business Service dependencies | Referral Administration consumes `Person Identifier Resolution` and `Service Provision Resolution` for identity validation and target service/provider/catchment resolution. These dependencies do not establish acceptance or transfer of care. |

### 2.3 Architectural reasoning and boundaries

The narrower capability descriptions do not expressly identify a restricted Referral scope. Read as exhaustive definitions, they omit assessment and management purposes explicitly included in the frozen Role and Interaction descriptions. The approved resolution treats this as overly narrow upstream wording, rather than an irreconcilable contradiction requiring separate Referral architectures.

Assessment requests seek assessment; management requests seek clinical management; requests to assume responsibility express a desired care responsibility. None establishes the recipient's acceptance or the responsibility actually assumed. Sharing responsibility and transferring accountability are distinct possible arrangements, not necessary effects of issuing a Referral. No required sequence from assessment to management to assumption is established.

The [Interaction Boundary Rules](../../03-business-architecture/collaborations-interactions/interactions.md#3-interaction-boundary-rules--semantic-distinctions) explicitly distinguish a Service Response communicating triage or acceptance disposition from a Service Outcome reporting findings after delivery. Referral progression is therefore not evidence that request, acceptance, delivery and transfer are equivalent, or that every instance reaches every illustrated milestone.

Referral remains distinct from Order. The [Domain02 Order Closed Loop example](../../02-strategy/capability-maps/capability-tier-model.md#example-2-order-closed-loop) recognises clinical responsibility for performing an ordered test. Consequently, requesting clinical responsibility alone does not distinguish Referral from Order, and absence of ongoing care transfer does not by itself classify a request as an Order.

This interpretation preserves AX-01/AX-04 architectural meaning, AX-14 semantic distinctions and AX-15 uncertainty. Consistent with the [Domain03 care enablement principle](../../03-business-architecture/behaviours/03-service-delivery.md#the-fundamental-care-enablement-principle), Harmonia's referral information and coordination responsibilities do not displace the clinical responsibility of care participants.

### 2.4 Upstream finding and correction requirement

The narrower Domain02 / Domain03 wording characterising Referral primarily through assumption or sharing of ongoing care responsibility is overly narrow relative to the wider frozen Business Role and Business Interaction semantics.

The following sources SHALL be reconciled with the approved interpretation in later, separately authorised G1 reconciliation:

| Affected frozen source | Wording requiring correction or broadening |
| :--- | :--- |
| [Domain02 Business Enabling Capabilities, View 2 — Distinction: Referral vs. Order](../../02-strategy/capabilities/business-enabling-capabilities.md#distinction-referral-vs-order) | Broaden the assumption-of-responsibility description to include assessment and management purposes. Qualify care handoff as applicable to relevant purposes and outcomes, rather than a universal consequence. Preserve the bounded, closed-loop Order distinction. |
| [Domain03 Service Administration, §3 — Referral Administration, Key Architectural Semantics](../../03-business-architecture/behaviours/02-service-administration.md#3-referral-administration) | Replace the ongoing-care-centred characterisation with the approved request scope. Retain shared or transferred ongoing responsibility as a possible purpose or subsequent arrangement, distinct from the Referral itself. |

The wider `Referrer` and `Service Referral` purpose descriptions SHALL NOT be narrowed to require ongoing care responsibility. No frozen source is amended or authorised for amendment by this record.

### 2.5 Remaining uncertainty

The architecture has not yet established:

- what a particular Referral acceptance commits the recipient to;
- when any resulting responsibility becomes effective;
- the duration or extent of that responsibility;
- whether the referring participant relinquishes responsibility;
- the exact semantic boundary between an assessment Referral and a bounded Order.

These uncertainties SHALL remain explicit in subsequent derivation. They SHALL NOT be filled from healthcare convention, external standards or implementation models. Acceptance SHALL NOT be assigned an unsupported universal effect on care responsibility.

### 2.6 Domain04 consequence

Subsequent Referral Information Architecture derivation SHALL preserve the approved distinction between Referral intent, Referral disposition, responsibility actually assumed and Transfer of Care.

It SHALL NOT assume that every Referral:

- transfers care;
- establishes ongoing care responsibility;
- results in service delivery;
- results in an Appointment;
- creates an Encounter;
- produces an Order.

Referral acceptance is likewise not guaranteed: decline, rejection and redirection remain evidenced dispositions. A Referral interaction does not automatically establish an enduring care relationship or Business Collaboration.

These are approved Business Architecture interpretation constraints. This record derives no Referral Information Concept, semantic category, relationship, cardinality, lifecycle model, subtype taxonomy or realised representation. Further Package 2 derivation and resolution of other G1 issues require their own authorised scope.

## 3. K2 — Signatures and Legal Scope

**Status: RESOLVED — APPROVED WITH EXPLICIT UNRESOLVED SEMANTICS**

**Disposition:** Preserve the evidenced process-specific signing and finalisation requirements, distinguish consumption from responsibility and authority, and prohibit unsupported universalisation or semantic equivalence. The approved interpretation resolves K2's scope treatment for subsequent derivation; it does not complete the semantic model of signing, finality or legal qualification. The upstream reconciliation requirements and uncertainties below remain explicit without reopening this approved disposition.

### 3.1 Approved interpretation

Signing, finalisation, authorship, attestation, approval, authentication, verification, authority and legal qualification SHALL NOT be assumed to be synonymous or to constitute a single information state.

Existing signing and finalisation requirements remain valid only within the Business Process boundaries in which they are explicitly established.

A Business Process that requires or consumes signed, finalised, verified or otherwise qualified information does not thereby acquire architectural responsibility or authority to define the semantics, authority, qualification or validity of that information.

In particular, Closed-Loop Order Progression establishes a progression dependency upon appropriately qualified diagnostic information. It does not establish Order Administration as the authority defining what makes a Diagnostic Report authoritative, final, signed or legally valid.

`Final Signed` is currently an evidenced Business Process disposition. It SHALL NOT be promoted automatically into an indivisible Domain04 information state.

Domain04 SHALL NOT infer a universal signing requirement for:

- Clinical Documents;
- Diagnostic Reports;
- Order outcomes;
- clinical information generally.

### 3.2 Semantic guardrail

The following semantic distinctions SHALL be preserved:

```text
Lifecycle / finality
    ≠ authorship
    ≠ attestation
    ≠ approval
    ≠ authentication
    ≠ verification
    ≠ authority
    ≠ signature
    ≠ legal qualification
```

The architecture does not currently establish a complete semantic model connecting these dimensions. This distinction does not prescribe Information Concepts, attributes, independent states or data structures for them.

`Final` and `Finalised` are used as lifecycle or readiness wording without universal criteria. `Final Signed` has a local process description combining authoritative standing, legal signing and publication; that description does not establish equivalence among those characteristics. Signing, countersignature, attestation and approval are recognised activities, but their general criteria and effects are not defined. Authentication concerns trusted identity and security context; authorisation concerns permission to act. Neither establishes clinical authorship, attestation or legal validity.

### 3.3 Frozen evidence basis and bounded existing requirements

| Frozen source | Architectural type | Material evidence and bounded scope |
| :--- | :--- | :--- |
| [Domain03 Closed-Loop Order Progression, §3.5](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process) | Business Process; Order progression requirements | Within the described pathology, radiology and procedural Order process, `Requisition Placed` requires an electronic Order created and signed by the requesting clinician. `Final Result Bound` requires an "Authoritative diagnostic report signed and bound to order." `Closed / Verified` means that the requesting clinician acknowledges the result and the Order closes. Preliminary critical or interim observations are published and linked without an explicit signature condition. These are progression requirements, not definitions of diagnostic authority, report finality or legal validity. |
| [Domain03 Clinical Document Lifecycle, §3.6](../../03-business-architecture/processes/business-processes.md#36-clinical-document-lifecycle-process) | Business Process within Clinical Record Administration | Governs clinical sign-off and legal status, naming discharge summaries, specialist letters and advance care directives. `Preliminary` is an authored document pending senior registrar or consultant countersignature. `Final Signed` is an authoritative clinical document legally signed and published. `Amended / Addended` appends a governed addendum to a signed document. The named examples do not establish an exhaustive applicability boundary. |
| [Domain03 Clinical Record Administration, §10](../../03-business-architecture/behaviours/02-service-administration.md#10-clinical-record-administration) | Capability-scoped document governance; Business Function and Service | Governs document legal lifecycle. `Govern Clinical Document Lifecycle` names `Final`, while the associated process names `Final Signed`. The lifecycle service permits authoring clinicians to publish addenda or corrections. `Final` is not explicitly defined as equivalent to `Final Signed`. |
| [Domain03 Discharge Progression, §4.13](../../03-business-architecture/processes/business-processes.md#413-discharge-progression-process); [Discharge Management Function](../../03-business-architecture/behaviours/04-health-service-operations.md#215-discharge-management); [service dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) | Business Process milestone; Business Function; cross-capability Service consumption | The process names `Discharge Summary Signed`, described as a final summary published and sent to the GP. The Function names `Discharge Summary Finalised`. Discharge Management consumes Clinical Record Administration's `Clinical Document Lifecycle Service` to trigger publication of signed clinical discharge summaries upon departure. This establishes bounded discharge-summary requirements without equating signing and finalisation or transferring document-authoring responsibility. |
| [Domain03 Encounter Lifecycle, §3.4](../../03-business-architecture/processes/business-processes.md#34-encounter-lifecycle-process) | Business Process completion criterion | `Completed / Encoded` includes finalised clinical documentation, completed coding and a legally closed Encounter. It does not explicitly impose document signing or define legal closure. |
| [Domain02 Diagnostic Administration, FEAT-SA-16/17](../../02-strategy/capabilities/business-enabling-capabilities.md#6-diagnostic-administration-harmonia-core); [Domain03 Diagnostic Administration, §7](../../03-business-architecture/behaviours/02-service-administration.md#7-diagnostic-administration) | Business Enabling Capability Features; Business Functions and Services | Domain02 describes ingestion and distribution of finalised reports, including content-integrity verification during ingestion. Domain03 handles preliminary, final and corrected reports. Diagnostic Administration owns correlation and distribution behaviour, excluding specimen analysis and diagnostic interpretation. Neither source establishes a universal signature rule. |
| [Domain02 Clinical Record Administration, FEAT-SA-24/25](../../02-strategy/capabilities/business-enabling-capabilities.md#9-clinical-record-administration-harmonia-core); [Domain03 Information Governance Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#24-information-governance--control-interactions) | Attribution requirements; Business Interactions | Author and amendment attribution are explicit. `Information Qualification` describes annotation, verification or clinical validation; `Information Publishing` distributes governed, approved information. These statements do not equate authorship, qualification, approval or publication with signing. |
| [Domain03 Information Responsibility & Ownership](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) | Authoritative Business Information Responsibility demarcations | Order Administration owns requisitions, closed-loop milestones and Order-result links, excluding diagnostic interpretation. Diagnostic Administration owns correlation, delivery and correction notices, excluding analysis and interpretation. Clinical Record Administration governs document repositories, versions, addenda and legal document states. Discharge Management does not own discharge-summary authoring; Workflow & Activity Coordination does not own clinical outcome meaning. |
| [Domain04 assertion, authority and qualification governance](../governance/authority-custody-provenance.md); [lifecycle principles](../governance/information-lifecycle.md); [modelling guardrails](../guardrails/modelling-guardrails.md) | Frozen Foundation information governance constraints | Responsibility, authority, custody and consumption are distinct. Authority can attach to individual assertions and relationships. Provenance, epistemic verification and qualifying sign-off authority are separately described. Lifecycles are concept-specific; material transitions require authorising authority and provenance. These constraints do not supply missing universal signing or legal-validity semantics. |

### 3.4 Architectural reasoning and responsibility boundaries

Closed-Loop Order Progression requires or consumes qualified diagnostic evidence before its own progression. Binding a report to an Order neither originates the report's assertions nor defines the report's finality, signature or legal standing. Its `Closed / Verified` disposition describes acknowledgement and Order closure, not verification of diagnostic content.

Clinical Record Administration has the evidenced responsibility for document lifecycle governance. The performing diagnostic provider/LIS retains responsibility for specimen analysis and diagnostic interpretation; Diagnostic Administration's correlation and distribution responsibilities do not confer that originating authority. The exact allocation of authority over report finality and signatures, and its relationship to document governance, remains incompletely specified.

The [Domain03 ownership rule](../../03-business-architecture/information-responsibility/information-responsibility.md#1-architectural-principles-of-information-responsibility) and Domain04 governance distinction prevent consumption, coordination, custody or persistence from transferring semantic responsibility or originating authority. Actor credentials, authentication and permission to perform an operation likewise do not independently establish authority over the information asserted.

This interpretation preserves AX-01/AX-04 business meaning, AX-06 explicit information authority, AX-07 governed security, AX-08 meaningful evidence, AX-14 semantic distinctions and AX-15 uncertainty. The differing terminology is retained for reconciliation; omission of "Signed" elsewhere is not an explicit waiver of an evidenced signing requirement. No universalisation is justified, and the frozen evidence does not establish whether broader wording was accidental.

### 3.5 Upstream reconciliation required

Later, separately authorised reconciliation SHALL clarify:

- `Final` versus `Final Signed` in Clinical Record Administration, Clinical Document Lifecycle and Foundation lifecycle wording;
- `Discharge Summary Finalised` versus `Discharge Summary Signed` in discharge behaviour and process wording;
- the information classes and circumstances to which legal signing and countersignature apply;
- Order progression wording so that consuming a signed or final report is clearly distinguished from authority to define report semantics, qualifications and validity.

Reconciliation SHALL preserve the bounded existing requirements and SHALL NOT silently make the differing terms synonymous. No frozen Domain01–03 source is amended or authorised for amendment by this record.

### 3.6 Explicitly unresolved semantics

The architecture has not yet established universally:

- who may or must sign particular information;
- when countersignature is required;
- the semantic effect of signing;
- the relationship between signing, attestation and approval;
- what constitutes legal validity;
- what evidence constitutes a valid signature or attestation;
- the exhaustive information classes to which these requirements apply.

These SHALL remain explicit uncertainties. They SHALL NOT be completed from FHIR, HL7, jurisdictional convention, EMR behaviour or general healthcare practice. Local process descriptions remain evidence within their boundaries; they do not complete this missing general semantic model.

### 3.7 Domain04 consequence

Subsequent Package 2 derivation may model only those distinctions supported by architectural evidence.

It SHALL NOT:

- manufacture a universal `Final Signed` lifecycle state;
- infer signature from finality;
- infer finality from signature;
- infer legal validity from either;
- infer authority from authentication or permission;
- infer diagnostic-content verification from Order `Closed / Verified`;
- generalise process-specific signing requirements across unrelated information families.

This approved decision creates no Package 2 Information Concept, attribute, state model or data structure. Further derivation SHALL preserve the unresolved semantics rather than manufacture a complete model connecting these dimensions. The approval resolves K2 only; no other G1 issue is analysed or resolved by this entry.

## 4. K3 — Domain04 Foundation Order Traceability Terminology

**Status: RESOLVED — APPROVED: ARCHITECTURAL TERMINOLOGY / TRACEABILITY HYGIENE DEFECT**

**Disposition:** Retain the Foundation Order traceability example and require later, separately authorised correction of its stale typed labels to canonical upstream references. The intended architectural meaning already exists in frozen Domain02 and Domain03 architecture. The outstanding Foundation correction does not reopen the approved K3 disposition.

### 4.1 Approved interpretation

The five questioned labels in the [Domain04 Foundation Order traceability example, §2.3](../traceability/domain03-traceability.md#23-representative-example-3-clinical-order-administration) are stale/non-canonical architectural labels:

- `Closed-Loop Order Status Tracking`;
- `Track Order Fulfilment Status`;
- `Clinical Order Record`;
- `Order Status History`;
- `Order-Result Reconciliation Binding`.

They SHALL NOT be treated as authoritative Domain02 or Domain03 architectural elements. Their intended architectural meaning is already represented by canonical frozen upstream architecture.

No missing Business Capability, Feature, Business Function, Business Service, Business Process or Information Responsibility is established by K3. No alias SHALL be created merely to preserve the stale terminology.

The example explicitly types these labels as upstream architectural elements within a canonical derivation chain. Its representative or illustrative purpose does not remove the traceability defect or make those labels authoritative.

### 4.2 Canonical traceability and frozen evidence basis

The [Package 2 evidence baseline, K3](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k3--foundation-order-example-terminology) records the questioned Foundation wording. The investigation found no occurrence of any of the five exact labels in the frozen Domain02 or Domain03 Markdown sources. The canonical references below restore the already-established architecture while preserving architectural types.

#### Business Enabling Capability and Features

The owning **Business Enabling Capability** is `Order Administration`, in the Service Administration view, classified `Harmonia-Core`. The [Domain02 Order Administration catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#5-order-administration-harmonia-core) establishes the relevant **Features**:

| Feature identifier | Canonical Feature |
| :--- | :--- |
| `FEAT-SA-11` | `Order Request Ingestion` |
| `FEAT-SA-12` | `Order Destination Resolution & Routing` |
| `FEAT-SA-13` | `Order Closed-Loop Progression Tracking` |
| `FEAT-SA-14` | `Order Cancellation & Modification Coordination` |

The stale Foundation Feature label `Closed-Loop Order Status Tracking` corresponds most directly to `Order Closed-Loop Progression Tracking` (`FEAT-SA-13`).

#### Business Functions and exposed Business Services

[Domain03 Order Administration, §6](../../03-business-architecture/behaviours/02-service-administration.md#6-order-administration) establishes the following relevant **Business Functions** and their exposed **Business Services**:

| Business Function | Exposed Business Service |
| :--- | :--- |
| `Receive Order Request` | `Order Requisition Ingress` |
| `Resolve Order Destination` | `Order Dispatch Service` |
| `Manage Order Progression` | `Order Status & Tracking Query` |
| `Associate Order Outcome` | `Order Outcome Notification` |

The stale Foundation Function label `Track Order Fulfilment Status` corresponds most directly to the Business Function `Manage Order Progression`.

Functions and their exposed Services SHALL remain distinct. A Business Service SHALL NOT be substituted for a Business Function merely because its wording is similar. The requisition, routing and result-association portions of the example have the specific Function traces recorded above; they SHALL NOT all be attributed solely to progression tracking.

#### Business Process

The canonical governed **Business Process** is `Closed-Loop Order Progression Process`, explicitly linked from Domain03 Order Administration and defined in [Principal Business Processes, §3.5](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process).

This Process establishes progression from requisition through dispatch, execution, result binding and acknowledgement/closure. It SHALL remain distinct from the Business Function `Manage Order Progression` and the Business Service `Order Status & Tracking Query`.

#### Business Information Responsibility

The authoritative **Business Information Responsibility** anchor is `Clinical Order & Closed-Loop Matrix`, the Conceptual Information Asset in the [Domain03 canonical ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) owned by `Order Administration`.

Its scope includes diagnostic pathology/radiology requisitions, procedural orders, routing directives, closed-loop state milestones and order-result correlation links. Its non-ownership demarcation excludes diagnostic interpretations and referral assumption of care.

Supporting capability-scoped **Information Responsibility** wording in [Domain03 Order Administration, §6](../../03-business-architecture/behaviours/02-service-administration.md#6-order-administration) supplies the following correspondences:

| Stale Foundation Information Responsibility label | Supporting canonical Domain03 wording |
| :--- | :--- |
| `Clinical Order Record` | `Clinical Order Master Record` |
| `Order Status History` | `Closed-Loop Tracking Ledger` |
| `Order-Result Reconciliation Binding` | `Order-Result Correlation Matrix` |

These supporting labels SHALL NOT be interpreted as three new independent ownership-matrix entries or three Domain04 Information Concepts. The ownership matrix remains the authoritative responsibility anchor.

### 4.3 Semantic guardrail

Correction of K3 is architectural hygiene only. It SHALL restore traceability to already-established upstream architecture and SHALL NOT introduce new semantics.

`Order Status History` SHALL NOT be interpreted as establishing a new comprehensive historical-retention requirement.

`Order-Result Reconciliation Binding` SHALL NOT be interpreted as establishing result conflict-resolution, reconciliation or adjudication semantics. The frozen architecture currently establishes Order-result **correlation / association**.

If comprehensive historical retention or result reconciliation is later required, it must be established independently through architecture rather than inferred from the stale Foundation wording.

This interpretation preserves AX-04 architectural meaning, AX-06 authority boundaries, AX-14 semantic distinctions and AX-15 uncertainty, together with the Foundation's canonical derivation principles and responsibility-derivation guardrail. The approved K1 and K2 interpretations remain unchanged.

### 4.4 Foundation correction requirement

The Domain04 Foundation Order traceability example requires later, separately authorised correction. That correction SHALL:

- retain the example;
- replace stale typed labels with canonical upstream references;
- preserve Feature / Function / Service / Process / Information Responsibility distinctions;
- anchor information responsibility to `Clinical Order & Closed-Loop Matrix`;
- use supporting responsibility wording only where appropriate;
- preserve the existing illustrative purpose;
- introduce no new upstream elements or aliases;
- introduce no new Domain04 Information Concepts.

Removal of the example is not required. This record does not modify or authorise modification of the frozen Foundation; approval of the K3 disposition and authorisation to perform the correction remain separate.

### 4.5 Domain04 consequence

Subsequent Package 2 derivation SHALL use canonical Domain02 and Domain03 architectural terminology for traceability.

Stale or illustrative-looking labels in downstream architecture SHALL NOT be treated as evidence that an upstream architectural element exists.

Where a downstream example explicitly types a label as a Capability, Feature, Function, Service, Process or Information Responsibility, that label must resolve to the authoritative upstream architecture or be identified as non-canonical.

This approved decision creates no Package 2 Information Concept, relationship, lifecycle model or realised representation. It resolves K3 only; no other G1 issue is analysed or resolved by this entry. Frozen Domain01–03 and Domain04 Foundation sources remain unchanged.

## 5. K4 — Narrow Settings / Examples

**Status: RESOLVED — APPROVED WITH BOUNDED SCOPE AND EXPLICIT UNCERTAINTY**

**Disposition:** Preserve the broader Medication and Procedure information-administration responsibilities evidenced by frozen architecture, qualify narrow contextual wording through later authorised reconciliation, and retain Theatre's deliberately bounded surgical semantics. The explicit uncertainties and outstanding reconciliation requirements below do not reopen the approved K4 disposition.

### 5.1 Approved interpretation — Medication

The frozen architecture establishes Medication Administration information responsibilities beyond an exclusively bedside, nursing, e-MAR or inpatient context.

`bedside`, `nurse`, `e-MAR`, inpatient prescription, community/hospital dispensing and outpatient infusion wording identify evidenced contexts, actors or information sources. They SHALL NOT individually be treated as defining characteristics of Medication information or as exhaustive scope constraints unless explicitly stated as such.

The following distinctions SHALL be preserved:

```text
Medication information
    ≠ bedside-only information
    ≠ nursing-only information
    ≠ e-MAR-only information
    ≠ inpatient-only information
```

The architecture separately evidences:

- medication Order information;
- dispensing information;
- medication administration-event information.

These distinctions SHALL be preserved. The [Domain02 Medication Administration Features](../../02-strategy/capabilities/business-enabling-capabilities.md#7-medication-administration-harmonia-relevant) establish Order ingestion, dispense-event tracking and point-of-care administration-event ingestion. [Domain03 Medication Administration, §8](../../03-business-architecture/behaviours/02-service-administration.md#8-medication-administration) separately defines `Receive Medication Order`, `Track Dispense Event` and `Receive Medication Administration Event`, exposed through their respective ingress Services.

The broader context is evidenced by inpatient and outpatient prescriptions, community and hospital dispensing records, infusion telemetry, and [Medication Therapy Enablement's](../../03-business-architecture/behaviours/03-service-delivery.md#26-medication-therapy-enablement) hospital pharmacies, pharmacotherapy review panels, community dispensing networks and outpatient infusion suites. These are evidenced contexts, not an exhaustive setting or actor taxonomy.

This decision does NOT establish every possible medication setting, administration actor or source. Home administration, self-administration, community dose administration and other unevidenced contexts SHALL NOT be inferred merely from the approved broader interpretation.

Harmonia's information-administration responsibility SHALL NOT be interpreted as responsibility for clinically prescribing, dispensing or administering medication.

### 5.2 Approved interpretation — Procedure

Procedure Administration has an independently evidenced responsibility for procedural requests and procedural documentation, established through the [Domain02 Procedure Administration Features](../../02-strategy/capabilities/business-enabling-capabilities.md#8-procedure-administration-harmonia-relevant), [Domain03 Procedure Administration Functions](../../03-business-architecture/behaviours/02-service-administration.md#9-procedure-administration) and the [canonical Information Responsibility ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix).

Its authoritative responsibility anchor is `Procedure Request & Documentation Index`. The owning capability receives procedural/surgical requisitions and procedural documentation; its non-ownership demarcation excludes surgical theatre suite logistics.

Procedure Administration SHALL NOT be treated as synonymous with:

- surgery;
- Theatre;
- Theatre Operations;
- inpatient procedural activity.

The frozen architecture therefore supports:

```text
Procedure Administration
    ≠ surgery-only administration
    ≠ Theatre Operations
```

The architecture does NOT establish an exhaustive taxonomy of Procedure classes, settings or clinical performers. The absence of such an exhaustive taxonomy does not prevent subsequent Domain04 derivation from the evidenced Procedure Administration responsibility.

### 5.3 Theatre boundary

[Theatre Operations](../../03-business-architecture/behaviours/04-health-service-operations.md#23-theatre-operations) and [Theatre Case Progression Process](../../03-business-architecture/processes/business-processes.md#47-theatre-case-progression-process) are deliberately bounded surgical / operating-theatre semantics.

Their specific milestones, including `Anaesthesia Commenced`, `Knife-to-Skin`, `In PACU Recovery` and `Ward Handover Complete`, SHALL remain scoped to Theatre Operations. Surgical and Theatre-specific progression SHALL NOT be generalised into a universal Procedure lifecycle.

```text
Procedure information
    ≠ inherently Theatre information

Theatre progression
    ≠ universal Procedure progression
```

Theatre Operations is separately scoped from Procedure Administration. The frozen architecture does not establish a formal Service-consumption relationship between them; such a relationship SHALL NOT be inferred from shared surgical context.

### 5.4 Procedural Documentation Ingress

There is an identified tension between the broader `Receive Procedural Documentation` Business Function / Procedure Administration responsibility and the narrower `Procedural Documentation Ingress` Business Service description, which refers to ingesting `surgical notes`.

The architecture establishes broader procedural-documentation responsibility, but does not explicitly state that the exposed Service is intentionally restricted to surgical documentation. This is an upstream wording/scope reconciliation issue.

During later authorised reconciliation, determine whether the Service description should be qualified to reflect procedural documentation generally while retaining surgical notes as an evidenced covered context. Additional procedure classes or settings SHALL NOT be inferred to perform that correction. No new Service or capability SHALL be created.

The approved K4 scope interpretation does not silently rewrite the Service contract or authorise modification of its frozen description.

### 5.5 Responsibility boundary

Clinical activity performed by care participants SHALL remain distinct from information concerning that clinical activity administered, correlated, communicated or exposed by Harmonia.

No inspected evidence assigns clinical prescribing, dispensing, medication administration or procedure performance to Harmonia. The [care-enablement principle](../../03-business-architecture/behaviours/03-service-delivery.md#the-fundamental-care-enablement-principle) and Medication Administration's explicit non-practice guardrail preserve this boundary.

Managing information about performance SHALL NOT be interpreted as performing the clinical activity or acquiring originating clinical authority. Under the [Domain03 information-ownership rule](../../03-business-architecture/information-responsibility/information-responsibility.md#12-non-transfer-of-ownership-guardrail), access, use, transformation, transport, caching, persistence or consumption does not transfer architectural ownership. Consistent with approved K2, consumption of qualified information does not confer authority to define its semantics or validity.

The [Business Role model](../../03-business-architecture/actors-roles/roles.md#34-job-titles-are-not-architectural-roles) distinguishes professional job titles from architectural capacities. The contextual word `nurse` SHALL NOT itself establish an intrinsic architectural role or grant clinical authority to any alternative actor.

### 5.6 Upstream reconciliation required

The [Package 2 evidence baseline, K4](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k4--narrow-settings-and-examples) records the narrow wording and its surrounding frozen evidence. Later, separately authorised reconciliation SHALL address:

| Affected frozen source | Wording and reconciliation requirement |
| :--- | :--- |
| [Domain03 Medication Administration, §8 — Event Tracking](../../03-business-architecture/behaviours/02-service-administration.md#8-medication-administration) | Qualify `bedside administration events` as an evidenced context within the broader medication information-administration responsibility. |
| [Domain03 Medication Administration, §8 — Receive Medication Administration Event](../../03-business-architecture/behaviours/02-service-administration.md#8-medication-administration) | Qualify the nurse/e-MAR-specific wording while retaining nurse-administered doses, e-MAR documentation and infusion telemetry as evidenced covered contexts or sources. |
| [Domain03 Information Responsibility & Ownership — Medication Administration row](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) | Qualify the `Medication Event Timeline Matrix` scope wording referring to `nurse e-MAR administration logs` so it is not read as an exhaustive definition of medication administration-event information. |
| [Domain03 Procedure Administration, §9 — Procedural Documentation Ingress](../../03-business-architecture/behaviours/02-service-administration.md#9-procedure-administration) | Reconcile the `surgical notes` wording with the broader Function and responsibility; determine whether to qualify the Service description as set out in §5.4. |

Reconciliation SHALL retain evidenced concrete contexts while preventing them from being interpreted as exhaustive capability definitions. It SHALL preserve architectural types, the clinical-performance boundary and Theatre's explicitly bounded Process semantics. It SHALL NOT infer additional procedure classes or settings or create a new Service or capability.

No frozen Domain01–03 or Domain04 Foundation source is amended or authorised for amendment by this record.

### 5.7 Explicit uncertainty

The architecture does not currently establish:

- every Medication Administration setting;
- every actor capable of administering medication;
- every medication-event source;
- an exhaustive taxonomy of Procedure classes;
- every Procedure setting;
- every Procedure performer;
- a universal Procedure lifecycle;
- a formal Service-consumption relationship between Theatre Operations and Procedure Administration.

These uncertainties SHALL NOT be filled from FHIR, healthcare convention or implementation models. They remain explicit within the approved bounded interpretation rather than preventing derivation from responsibilities actually evidenced by frozen architecture.

### 5.8 Domain04 consequence

Subsequent Package 2 derivation SHALL NOT assume that:

- Medication information inherently means bedside activity;
- Medication administration inherently means nursing activity;
- e-MAR forms part of the abstract meaning of Medication information;
- Procedure information inherently means surgery;
- Procedure inherently occurs in Theatre;
- Theatre Case Progression defines the lifecycle of Procedure information;
- recording or managing information about clinical performance transfers clinical performance responsibility or originating authority to Harmonia.

Domain04 may derive only from the broader responsibilities actually evidenced by the frozen architecture and SHALL preserve the explicit uncertainties above.

This interpretation preserves AX-01/AX-04 business meaning, AX-06 authority boundaries, AX-14 semantic distinctions and AX-15 uncertainty. The approved K1–K3 entries remain unchanged.

This approved decision derives no Package 2 Information Concept, relationship, lifecycle model or realised representation. It resolves K4 only; no other G1 issue is analysed or resolved by this entry, and G1 as a whole is not approved.

## 6. K5 — Naming Variants and Missing Feature IDs

**Status: RESOLVED — APPROVED: CANONICAL NAMING AND FEATURE-TYPING RECONCILIATION REQUIRED**

**Disposition:** Use canonical Strategy Business Enabling Capability names in typed architectural references and require later, separately authorised reconciliation of variant owner names and unsupported Domain03 Feature typing. Preserve the evidenced Functions, Services and Information Responsibilities without creating a Capability, Feature or Feature identifier. The outstanding upstream corrections do not reopen the approved K5 disposition.

### 6.1 Canonical Capability naming

Typed architectural references SHALL use the canonical Strategy Business Enabling Capability names. The following abbreviations do not establish separate architectural elements:

| Presentation shorthand | Canonical Business Enabling Capability |
| :--- | :--- |
| `Episode & Encounter Admin` | `Episode & Encounter Administration` |
| `Clinical Communication Admin` | `Clinical Communication Administration` |

`Admin` is harmless presentation shorthand where used informally, but typed ownership, dependency and traceability references SHALL use the canonical full name.

`Encounter Administration` is a stale/non-canonical owner reference to `Episode & Encounter Administration`. Canonicalising that owner SHALL NOT broaden an encounter-specific dependency or behaviour into unsupported Episode semantics.

The frozen evidence establishes the same encounter context, progression and care-place responsibility across [Strategy's Episode & Encounter Administration](../../02-strategy/capabilities/business-enabling-capabilities.md#4-episode--encounter-administration-harmonia-core), [Domain03 Service Administration, §5](../../03-business-architecture/behaviours/02-service-administration.md#5-episode--encounter-administration), [the ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) and [the dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix). The dependency matrix attributes the exact `Encounter Location History Query` Service to the abbreviated owner. [Encounter Lifecycle Process](../../03-business-architecture/processes/business-processes.md#34-encounter-lifecycle-process) retains the full owner name.

Likewise, dispatch, acknowledgement tracking and transaction audit identify the same Clinical Communication Administration responsibility across [Strategy](../../02-strategy/capabilities/business-enabling-capabilities.md#11-clinical-communication-administration-harmonia-core), [Domain03 Service Administration, §12](../../03-business-architecture/behaviours/02-service-administration.md#12-clinical-communication-administration), the ownership matrix and the dependency matrix. The latter consumes the exact `Secure Clinical Message Dispatch` Service from the abbreviated owner. Naming equivalence does not resolve unrelated acknowledgement semantics or transfer ownership of clinical message content.

### 6.2 Service Delivery Capability naming

The following Domain03 `Enablement` owner forms identify the corresponding canonical Strategy Business Enabling Capabilities and SHALL NOT be treated as separate Capabilities:

| Domain03 owner form | Canonical Strategy Business Enabling Capability |
| :--- | :--- |
| `Primary Care Enablement` | `Primary Care` |
| `Acute Care Enablement` | `Acute Care` |
| `Emergency Care Enablement` | `Emergency Care` |
| `Inpatient Care Enablement` | `Inpatient Care` |
| `Diagnostic Services Enablement` | `Diagnostic Services` |
| `Medication Therapy Enablement` | `Medication Therapy` |
| `Preventive Care Enablement` | `Preventive Care` |
| `Community Care Enablement` | `Community Care` |
| `Outreach Care Enablement` | `Outreach Care` |
| `Remote & Virtual Care Enablement` | `Remote & Virtual Care` |

The [Strategy Service Delivery catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) and [Domain03 care-enablement contexts](../../03-business-architecture/behaviours/03-service-delivery.md#2-clinical-care-enablement-contexts) establish corresponding operating contexts and owned Feature headings. Both establish systems enablement rather than Harmonia performing clinical care. The [dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) also uses the Primary, Acute, Emergency and Inpatient Care `Enablement` owner forms.

`Enablement` may remain useful explanatory prose describing Harmonia's participation in these care contexts. It SHALL NOT be used as the name of a separate Business Enabling Capability where Strategy establishes the canonical owner without that suffix.

Canonicalising these owner names does not:

- alter subordinate Feature scope;
- create new information ownership;
- imply that Harmonia performs clinical care;
- resolve unrelated behavioural scope differences.

### 6.3 Clinical Logistics naming

`Clinical Logistics Coordination` is the canonical Business Enabling Capability name. Typed references to `Clinical Logistics` as the owner SHALL later be reconciled to `Clinical Logistics Coordination`.

The [Strategy Clinical Logistics Coordination catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#19-clinical-logistics-coordination-harmonia-relevant), [Domain03 Clinical Logistics behaviour](../../03-business-architecture/behaviours/04-health-service-operations.md#214-clinical-logistics), [ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix), [dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) and [Specimen Transport Process](../../03-business-architecture/processes/business-processes.md#412-specimen-transport-process) establish the common transport and chain-of-custody responsibility. Both questioned names identify the Capability owner; neither is the name of its Function or Service.

This correction SHALL preserve the independently typed:

- `Pathology Specimen Transport Tracking` Feature (`FEAT-HSO-26`);
- `Manage Specimen Transport Progression` Function;
- `Specimen Transit Tracking Service`;
- `Specimen Transport Process`;
- associated Information Responsibility, including the ownership matrix's `Specimen Manifest & Chain-of-Custody Register`.

Canonicalising the owner SHALL NOT broaden `FEAT-HSO-26` to every transported clinical item or create a general supply-logistics responsibility. Differing specimen, blood-product, surgical-tray and supply wording does not itself authorise such broadening.

### 6.4 Unsupported Domain03 Feature typing

The following Domain03 labels are typed as Features but have no corresponding canonical Strategy Feature:

- `Order Result Association`;
- `Care Plan Context Association`;
- `Care Plan Update Distribution`.

Their underlying Business Functions, Business Services and owning responsibilities are evidenced and SHALL be retained. Their unsupported Feature typing SHALL NOT be used as evidence that an independent Strategy Feature exists.

No `FEAT-*` identifier SHALL be invented. No new Strategy Feature is required merely because Domain03 decomposes behaviour more finely than Domain02.

These are Strategy-to-Business-Architecture naming and typing reconciliation defects. The [Domain03 Strategy alignment statement](../../03-business-architecture/README.md#4-relationship-to-strategy-domain-02) claims canonical Feature naming and structural alignment, and the [behaviour derivation convention](../../03-business-architecture/behaviours/index.md#2-derivation-conventions) requires canonical Strategy names and identifiers. The three unsupported headings do not satisfy that traceability requirement; their existence does not establish missing Business Architecture.

#### 6.4.1 Order Result Association

[Domain03 Order Administration, §6](../../03-business-architecture/behaviours/02-service-administration.md#6-order-administration) explicitly labels `Order Result Association` as a Feature, delivering the Function `Associate Order Outcome` and the Service `Order Outcome Notification`.

`Associate Order Outcome` and `Order Outcome Notification` remain valid Function and Service behaviour within Order Administration. The [canonical ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) includes order-result correlation links within `Clinical Order & Closed-Loop Matrix`; [Closed-Loop Order Progression Process](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process) includes result correlation and binding.

[Strategy's `Order Closed-Loop Progression Tracking`](../../02-strategy/capabilities/business-enabling-capabilities.md#5-order-administration-harmonia-core) (`FEAT-SA-13`) includes outcome association in its broader scope.

`Order Result Association` SHALL NOT be assigned `FEAT-SA-13` as an alias or independent Feature identity. Later reconciliation SHALL remove or correct the unsupported Feature-level typing while preserving the Function and Service.

#### 6.4.2 Care Plan Context Association

[Domain03 Care Plan Administration, §11](../../03-business-architecture/behaviours/02-service-administration.md#11-care-plan-administration) explicitly labels `Care Plan Context Association` as a Feature, delivering the Function `Associate Care Plan Context` and the Service `Care Plan Context Query`.

`Associate Care Plan Context` and `Care Plan Context Query` remain valid Function and Service behaviour within Care Plan Administration. The Function establishes diagnosis, health-problem and care-team association; the [canonical ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) establishes `Shared Care Plan & Clinical Goal Registry`, including care team participant enrolments.

No corresponding Strategy Feature has been established. This absence does not constitute a missing architectural responsibility.

Later reconciliation SHALL remove or correct the unsupported Feature-level typing while retaining the evidenced behaviour under the owning Capability. It SHALL NOT force this behaviour into [Strategy's `Care Plan Ingestion & Distribution`](../../02-strategy/capabilities/business-enabling-capabilities.md#10-care-plan-administration-harmonia-relevant) (`FEAT-SA-26`) merely to obtain a Feature identifier.

#### 6.4.3 Care Plan Update Distribution

[Domain03 Care Plan Administration, §11](../../03-business-architecture/behaviours/02-service-administration.md#11-care-plan-administration) explicitly labels `Care Plan Update Distribution` as a Feature, delivering the Function `Distribute Care Plan Change` and the Service `Care Plan Change Notification`.

`Distribute Care Plan Change` and `Care Plan Change Notification` remain valid Function and Service behaviour within Care Plan Administration. The capability's Information Responsibility includes care plan revision history, and the [canonical ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) establishes the shared-plan responsibility.

Update distribution is within the broader scope of [Strategy's `Care Plan Ingestion & Distribution`](../../02-strategy/capabilities/business-enabling-capabilities.md#10-care-plan-administration-harmonia-relevant) (`FEAT-SA-26`).

`Care Plan Update Distribution` SHALL NOT be treated as an alias for, or separately identified instance of, `FEAT-SA-26`. Later reconciliation SHALL remove or correct the unsupported Feature-level typing while preserving the finer-grained Function and Service.

### 6.5 Metamodel guardrail

The following distinctions SHALL be preserved:

```text
Capability / Feature
    ≠ Function
    ≠ Service
    ≠ Process
    ≠ Information Responsibility
```

Domain03 may decompose behaviour more finely than the Domain02 Feature catalogue. Therefore:

```text
absence of a one-to-one Strategy Feature
    ≠ missing Business Architecture
```

A Function or Service SHALL NOT be promoted to Feature solely to satisfy traceability symmetry. Likewise, an existing Feature identifier SHALL NOT be attached to narrower Domain03 behaviour merely because that Feature contains the behaviour.

This preserves the [Business Architecture metamodel's](../../03-business-architecture/metamodel/business-architecture-metamodel.md) Capability/Feature responsibility, Function behaviour, Service exposure, governed Process and Information Responsibility distinctions. K5 establishes no new Capability hierarchy-level assignment.

### 6.6 Upstream reconciliation required

The [Package 2 evidence baseline, K5](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k5--naming-variants-and-missing-feature-identifiers) and the approved investigation establish the following later, separately authorised G1 reconciliation requirements:

| Affected frozen source | Required reconciliation |
| :--- | :--- |
| Domain03 Information Responsibility & Ownership, rows for `Episode & Encounter Admin` and `Clinical Communication Admin`; dependency matrix, corresponding consuming/exposing owner references | Replace typed `Admin` owner references with canonical `Administration` names. |
| Domain03 Service Delivery, contextual scope and Community Care dependencies | Reconcile `Encounter Administration` to the canonical owner while preserving encounter-specific scope. |
| Domain03 Service Delivery, context/owner references in §§2.1–2.10; dependency matrix, Primary/Acute/Emergency/Inpatient Care consuming owner references | Use canonical Strategy Capability names for typed `... Enablement` owner references. Retain useful explanatory enablement prose without creating separate Capabilities. |
| Domain03 Health Service Operations, Clinical Logistics context/owner references; ownership and dependency matrix rows; Specimen Transport Process owner | Reconcile `Clinical Logistics` owner references to `Clinical Logistics Coordination`, preserving the independently typed Feature, Function, Service, Process and Information Responsibility. |
| Domain03 Service Administration, Order Administration and Care Plan Administration Feature headings | Remove or correct unsupported Feature typing for `Order Result Association`, `Care Plan Context Association` and `Care Plan Update Distribution`, retaining their valid Functions, Services and responsibilities. |

Reconciliation SHALL create no new Capability or Feature and SHALL invent no Feature identifier. No frozen Domain01–03 or Domain04 Foundation source is amended or authorised for amendment by this record. Approval of this disposition and authorisation to perform upstream correction remain separate.

### 6.7 Domain04 consequence

Subsequent Package 2 derivation SHALL:

- use canonical Strategy Capability and Feature names for traceability;
- not create duplicate responsibility because of naming variants;
- not derive distinct Information Concepts merely from abbreviated or variant owner names;
- not trace to the three unsupported labels as established Strategy Features;
- preserve their evidenced Functions, Services and Information Responsibilities;
- not collapse finer behavioural meaning merely to obtain a Feature identifier.

This interpretation preserves AX-01/AX-04 business meaning, AX-06 authority boundaries, AX-14 semantic distinctions and AX-15 uncertainty. The approved K1–K4 entries remain unchanged.

This approved decision creates no Capability, Feature, Feature identifier, Package 2 Information Concept, relationship, lifecycle model or realised representation. It resolves K5 only; no other G1 issue is analysed or resolved by this entry, and G1 as a whole is not approved. Frozen Domain01–03 and Domain04 Foundation sources remain unchanged.

## 7. K6 — Package1 Delivery Responsibility

**Status: RESOLVED — APPROVED WITH SEMANTIC, TRACEABILITY AND OWNERSHIP RECONCILIATION REQUIRED**

**Disposition:** Preserve the distinction between a real-world clinical phenomenon and the information representing it. Retain `HealthcareServiceDelivery` and `ServiceOutcome` as Information Concepts, without assigning clinical performance or originating clinical authority to Harmonia. Require later, separately authorised reconciliation of Package 1 semantic and traceability wording, Domain03 diagnostic ownership wording and the corresponding Foundation example. The retained uncertainties and outstanding corrections do not reopen the approved K6 boundary.

### 7.1 Approved core semantic boundary

An Information Concept may refer to a real-world clinical activity, event, result or consequence without becoming that activity, event, result or consequence.

Architectural responsibility for managing information representing a real-world phenomenon does not confer:

- responsibility for performing that phenomenon;
- originating clinical authority over it;
- authorship of externally supplied clinical information;
- authority to determine its clinical validity merely through ingestion, correlation, persistence, assembly or exposure.

The following distinction SHALL be preserved:

```text
Real-world phenomenon
    ↓ represented / described by
Information Concept
    ↓ realised later through
Application / standards / persistence representations
```

Semantic referent and information representation SHALL NOT be collapsed. A Business Information Concept remains distinct from Business Behaviour and from an Application Data Object, FHIR Resource, Persistence Entity or API Payload.

Architectural information responsibility, originating authority, clinical performer, information source, custodian, consumer and provenance SHALL remain separately attributable. Access, use, transformation, transport, caching, persistence, assembly or consumption does not transfer architectural ownership or originating authority. Conversely, responsibility for managing information does not make Harmonia the performer or originating clinical authority for the represented phenomenon.

### 7.2 HealthcareServiceDelivery

`HealthcareServiceDelivery` remains an Information Concept. Its semantic referent is a particular real-world execution / performance of a `DeliverableHealthcareService`.

It may carry information concerning:

- the particular performance;
- execution/progression;
- actual performer;
- time;
- location;
- equipment;
- provenance;
- qualification or attestation where established.

It SHALL NOT be interpreted as Harmonia itself performing the healthcare service.

It SHALL NOT be prematurely redefined as `HealthcareServiceDeliveryRecord`, `DeliveryAssertion` or another implementation/representation construct merely to repair the current wording.

The detailed distinction between activity representation, assertion and governed account remains explicitly unresolved where the frozen architecture does not establish it.

### 7.3 ServiceOutcome

`ServiceOutcome` remains an Information Concept concerning a result, finding, change, consequence or administrative artefact arising from or associated with service delivery.

The architecture SHALL preserve distinctions between, where applicable:

- real-world consequence;
- clinical finding/result;
- information reporting/asserting that result;
- Harmonia-managed association/correlation;
- administrative completion disposition.

These SHALL NOT be assumed synonymous.

Harmonia's management or correlation of `ServiceOutcome` information does not make Harmonia the originator or clinical authority for the represented outcome.

The precise information classification, authority and responsibility may differ by outcome kind and remain unresolved where not established by frozen architecture.

### 7.4 Clinical performance boundary

The frozen architecture establishes:

```text
Care participant / provider performs clinical activity
    ↓
performance / result information is asserted, recorded or supplied
    ↓
Harmonia manages, correlates, preserves, assembles or exposes information
within its established responsibilities
```

No inspected governing architecture assigns Harmonia responsibility for clinically performing healthcare services.

No inspected governing architecture assigns Diagnostic Administration responsibility for diagnostic analysis or clinical interpretation.

Harmonia may originate information within its own established responsibilities, including governed correlation, association, composition and progression information. That does not make Harmonia the originator of the underlying clinical phenomenon.

The Service Delivery capabilities describe enterprise care contexts in which Harmonia supplies integration enablement and clinical context correlation. Approved K5's canonical Capability names SHALL be retained; the non-canonical `... Enablement` owner forms SHALL NOT be reintroduced as separate Capabilities.

### 7.5 Capability / Performer boundary

The Package 1 phrase `Performing Clinical / Operational Capabilities` and equivalent Foundation wording SHALL NOT be interpreted literally as establishing a clinical performer.

The following distinctions SHALL be preserved:

```text
Capability
    ≠ Business Actor
    ≠ Business Role
    ≠ real-world Performer
```

A Capability establishes an architectural responsibility boundary. It does not itself perform a clinical activity.

Package 1 and Foundation traceability wording that implies otherwise requires later reconciliation. Contextual participation, an information source or a traceability dependency SHALL NOT be promoted to architectural ownership or clinical performance responsibility.

### 7.6 Diagnostic Administration

Diagnostic Administration is responsible, within the evidenced frozen architecture, for information-administration behaviour including:

- request/result correlation;
- external diagnostic report ingestion;
- report binding;
- notification/distribution;
- delivery status;
- addenda/correction handling within its established scope.

It SHALL NOT thereby be treated as responsible for:

- specimen analysis;
- diagnostic interpretation;
- originating diagnostic findings;
- clinical authorship of externally produced reports.

Managing, publishing, distributing or verifying ingestion integrity of a diagnostic report does not establish clinical authorship or interpretation authority.

Approved K2 remains applicable: consuming signed, finalised or otherwise qualified information does not transfer authority to define its semantics, validity or originating authority. This decision does not resolve K2's retained signing, finality or legal-qualification uncertainties.

### 7.7 Frozen evidence basis

The [Package 2 evidence baseline, K6](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k6--package-1-delivery-responsibility) and the focused K6 investigation establish the following evidence:

| Frozen source | Material evidence and responsibility boundary |
| :--- | :--- |
| [Domain02 Business Capabilities — Enablement vs. Ownership](../../02-strategy/capabilities/business-capabilities.md#foundational-principle-enablement-vs-ownership); [Business Enabling Capabilities — Service Delivery](../../02-strategy/capabilities/business-enabling-capabilities.md#architectural-principles-service-delivery) | Harmonia does not practise medicine, make diagnostic judgements or own clinical care delivery. No Service Delivery Capability is Harmonia-Core. The enabling pattern receives clinical state/outcome information, correlates it, preserves provenance and makes it available. |
| [Domain03 Service Delivery, §1](../../03-business-architecture/behaviours/03-service-delivery.md#1-contextual-scope--architectural-intent); [Business Roles](../../03-business-architecture/actors-roles/roles.md#22-care-delivery-roles) | Harmonia enables, informs and coordinates care. Clinical execution belongs to participants acting as Performer or Service Provider; Information Supplier is a separately defined participation role. |
| [Domain03 Service Administration, §§6–10](../../03-business-architecture/behaviours/02-service-administration.md#6-order-administration); [Closed-Loop Order Progression, §3.5](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process) | Order Administration ingests, routes and tracks requests and correlates returning reports. Diagnostic Administration ingests external results, binds and distributes reports, excluding specimen analysis and diagnostic interpretation. Procedure and Medication Administration receive information concerning performance. Clinical Record Administration governs document ingestion and lifecycle. |
| [Domain03 Information Responsibility & Ownership](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix); [Patient Clinical Record](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#21-patient-clinical-record) | The matrix bounds Order progression/correlation, Diagnostic correlation/linkage/delivery/correction notices, document governance and longitudinal synthesis. These responsibilities do not transfer originating authority over represented clinical facts. |
| [Package 1 Healthcare Service family, §§2–7](../information-families/healthcare-service.md#2-domain-03-responsibility--traceability) | Delivery is described through real-world performance, while execution records and practitioner assertion provenance show its informational context. Outcome wording spans consequences, findings, reports and artefacts, with retained attesting-author/device provenance. The generic clinical execution/interpretation traces conflict with the bounded upstream responsibilities. |
| [Foundation Information Concept metamodel](../metamodel/information-architecture-metamodel.md#2-the-core-information-concept); [authority, custody and provenance governance](../governance/authority-custody-provenance.md#2-the-four-pillars-of-information-governance); [Definition-to-Accountability service example](../patterns/definition-to-accountability.md#21-healthcare-service-concepts--semantics) | An Information Concept represents governed meaning concerning entities, activities, events and assertions. Responsibility, authority, custody and consumption are distinct. The service example repeats the performing-capability ownership wording requiring consistency review. |

### 7.8 Upstream defects and reconciliation required

#### 7.8.1 Package 1 semantic and traceability defects

Package 1's Delivery definition, “the real-world execution and performance”, and Outcome definition, describing findings, changes, results and artefacts “produced by a service delivery”, do not explicitly distinguish the semantic referent from its information representation. Their surrounding record and provenance wording supports an informational reading but does not eliminate the ambiguity.

Later authorised reconciliation SHALL qualify those definitions, diagrams and relationships against §§7.1–7.3, preserving their real-world referents, broad evidenced scope and provenance without renaming them as representation constructs or inventing a new classification.

The Package 1 traceability rows referring generically to:

- `Performing Clinical / Operational Capabilities`;
- `Diagnostic / Clinical Capabilities`;
- service execution / clinical delivery functions;
- diagnostic interpretation;

do not constitute valid evidence that Harmonia owns clinical performance or interpretation. Read as Harmonia responsibility assignments, they conflict with the explicit clinical-performance and diagnostic exclusions.

Later authorised reconciliation SHALL replace or qualify these rows using evidenced:

- Information Responsibilities;
- Business Functions;
- Business Services;
- Business Processes where appropriate;
- external performers/sources;
- contextual dependencies.

A universal Delivery or Outcome owner SHALL NOT be invented where the frozen architecture does not establish one.

#### 7.8.2 Domain03 ownership defect

The statement in [Domain03 Information Responsibility & Ownership, §1.2](../../03-business-architecture/information-responsibility/information-responsibility.md#12-non-transfer-of-ownership-guardrail):

> ownership of the diagnostic findings remains with the performing diagnostic provider / Diagnostic Administration

is architecturally ambiguous/defective. It improperly couples originating clinical authority with Diagnostic Administration's information-management responsibility.

Later authorised reconciliation SHALL separate externally originated diagnostic findings / interpretation and their attributable performer/source/authority from Diagnostic Administration's correlation, binding, delivery and correction-notice responsibilities.

The authoritative ownership matrix and explicit exclusions SHALL constrain that correction. It SHALL NOT expand Diagnostic Administration into clinical analysis, interpretation or authorship.

#### 7.8.3 Foundation reconciliation

The Foundation wording `Owned by the performing clinical/operational capability` in the Healthcare Service reference example requires later consistency review against the approved K6 boundary.

No frozen Domain01–03, Domain04 Foundation or Package 1 source is amended or authorised for amendment by this record. Approval of this disposition and authorisation to perform the corrections remain separate.

### 7.9 Existing Package 1 chain preserved

K6 does not redesign the frozen Package 1 relationship:

```text
OfferedHealthcareService
    ↓ contextualised / bound as
DeliverableHealthcareService
    ├── may-be-requested-through → Order
    └───────────────────────────→ HealthcareServiceDelivery
                                      ↓
                                ServiceOutcome
```

The following boundaries SHALL be preserved:

- Delivery may occur without Order;
- Order may fail/cancel without Delivery;
- Order requests/directs activity;
- Order owns its own progression/correlation information;
- Order does not thereby own clinical outcome content.

Service definition, contextual deliverability, request/direction, real-world performance, evidence/report/assertion of performance, outcome information and managed correlation SHALL NOT be collapsed merely because they are associated in this chain.

### 7.10 Explicit uncertainty

The architecture does not completely establish:

- the precise activity-representation versus assertion/account boundary for different Delivery information;
- the complete responsibility trace for every kind of `HealthcareServiceDelivery`;
- the complete responsibility trace for every kind of `ServiceOutcome`;
- measurement authority;
- interpretation authority;
- attestation authority;
- qualification authority;
- distinctions between administrative outcome artefacts and completion dispositions.

These uncertainties SHALL remain explicit. They SHALL NOT be resolved through FHIR, HL7, healthcare convention or implementation assumptions.

### 7.11 Domain04 consequence

Subsequent Package 2 derivation SHALL NOT infer that:

- Harmonia performed an activity because it manages information about it;
- Diagnostic Administration produced or interpreted a finding because it manages its report;
- Order owns outcome content because it correlates that outcome;
- Procedure Administration performed a procedure because it receives procedural information;
- Medication Administration clinically administered medication because it receives administration events;
- Encounter owns clinical activity merely because it supplies context;
- Clinical Document or longitudinal assembly acquires originating authority from its constituent information.

Derivation SHALL use evidenced, bounded information responsibilities and preserve the separately attributable performer, source, authority and provenance. The existing architecture distinguishes definitions, bindings, requests, performance, assertions, outcomes and correlation; K6 does not manufacture a complete ownership or classification model for their application to every Delivery or Outcome kind.

This interpretation preserves AX-01/AX-04 business meaning, AX-06 authority boundaries, AX-08 meaningful evidence, AX-14 semantic distinctions and AX-15 uncertainty. The approved K1–K5 entries remain unchanged.

This approved decision derives no Package 2 Information Concept, relationship, lifecycle model or realised representation. It resolves K6 only; no other G1 issue is analysed or resolved by this entry, and G1 as a whole is not approved. Frozen Domain01–03, Domain04 Foundation and Package 1 sources remain unchanged.

## 8. K7 — Acknowledgement and Incorporation

**Status: RESOLVED — APPROVED WITH CONTEXTUAL ACKNOWLEDGEMENT SEMANTICS AND UNRESOLVED INCORPORATION**

**Disposition:** Interpret acknowledgements only through the facts or dispositions they expressly acknowledge in their evidenced contexts. Preserve receiving-boundary acceptance, business disposition, clinical review, record-management behaviour and clinical authority as separately attributable concerns. Require later, separately authorised reconciliation of composite acknowledgement wording and retain unresolved incorporation semantics. The outstanding reconciliation and uncertainties do not reopen the approved K7 boundary.

### 8.1 Approved core rule

An acknowledgement SHALL be interpreted only according to the disposition or fact it expressly acknowledges.

The existence of an acknowledgement does not by itself establish:

- business acceptance;
- clinical review;
- clinical incorporation;
- approval;
- clinical authority;
- document finality;
- transfer of information responsibility.

No universal acknowledgement lifecycle is established. Different acknowledgement contexts may report different facts or dispositions. A particular acknowledgement may expressly report review or filing; the acknowledgement label alone establishes neither.

### 8.2 Technical Delivery Acknowledgement

The frozen architecture establishes Technical Delivery Acknowledgement as evidence of a receiving-boundary outcome. It confirms, within its evidenced scope, that information crossed the relevant boundary and was durably accepted into an integration queue.

The following distinction SHALL be preserved:

```text
sender transmitted
    ≠ receiving boundary durably accepted
    ≠ business disposition
```

Technical acknowledgement SHALL NOT be interpreted as:

- business acceptance;
- acceptance of clinical responsibility;
- clinical review;
- clinical incorporation;
- clinical endorsement;
- payload ownership transfer.

Technical Delivery Acknowledgement SHALL NOT be reduced merely to sender-side transmission success. Durable delivery responsibility at the receiving boundary does not transfer architectural responsibility or originating clinical authority over the payload.

### 8.3 Business Acknowledgement

Business Acknowledgement does not have one universal success meaning. It reports an evidenced business disposition in its particular context.

For Order / Referral contexts, frozen architecture permits dispositions including:

- reviewed;
- accepted for execution;
- scheduled;
- rejected.

These are not cumulative mandatory stages. Strategy explicitly identifies the receiving application or clinical staff as the producer of the clinical/operational acknowledgement message in these contexts.

The following distinction SHALL be preserved:

```text
Business Acknowledgement
    ≠ Business Acceptance
```

Acceptance may be one disposition reported by a Business Acknowledgement. A rejection may also be reported through the relevant business acknowledgement mechanism.

### 8.4 Clinical Communication Business Delivery Acknowledgement

Clinical Communication Administration currently describes a Business Delivery ACK using the composite wording:

```text
received, parsed, accepted, and filed
```

This wording SHALL NOT be interpreted as establishing a universal progression:

```text
received → parsed → accepted → filed → reviewed → authoritative
```

The source establishes that a recipient may report a stronger processing/filing disposition than technical receipt. However, the precise semantics of:

- `accepted`;
- `filed`;
- their relationship;
- the target record;
- the filing criteria;
- how the filing outcome is evidenced;

remain incompletely specified.

Later authorised reconciliation SHALL qualify this wording without discarding the evidenced possibility that a recipient reports filing.

### 8.5 Communication responsibility boundary

Clinical Communication Administration is responsible, within the frozen evidence, for communication transaction information including:

- dispatch;
- delivery / receipt tracking;
- acknowledgement state;
- rejection tracking;
- communication transaction audit;
- correlation with the outbound communication.

It SHALL NOT thereby acquire responsibility or originating authority for the clinical meaning of the payload. Tracking a recipient assertion that content was accepted or filed does not make Clinical Communication Administration responsible for performing that acceptance or filing.

The following SHALL remain separately attributable:

- responsibility for communication transaction information;
- responsibility for clinical payload information;
- originating authority;
- custody;
- consumption;
- assembly;
- review.

Health Information Exchange's exchange transaction state, delivery receipts and routing responsibilities, and Health Information Communication's technical mediation responsibility, do not confer clinical-content ownership. Cross-capability service consumption SHALL NOT transfer responsibility or originating authority.

### 8.6 Clinical review

Clinical Information Review is independently evidenced as a Business Interaction involving structured examination, validation or clinical evaluation of record content by a practitioner.

Review SHALL NOT universally imply:

- agreement;
- approval;
- verification;
- incorporation;
- publication;
- changed originating authority.

The effect of a particular review remains context-dependent unless explicitly established. Practitioner and Care-Team Collaborations support review participation; Clinical Collaboration's spaces, discourse indices and contextual projections do not establish a universal review or incorporation responsibility. Workflow coordination of a review task does not acquire its clinical meaning or outcome authority.

### 8.7 Incorporation

The frozen architecture does not establish one universal operation or Information Concept called `clinical incorporation`. It instead establishes distinct behaviours including:

- persistence / preservation;
- assembly inclusion;
- registration / indexing;
- document governance;
- formal authorship / submission in relevant collaboration contexts.

These SHALL NOT be assumed synonymous.

Patient Clinical Record has evidenced longitudinal synthesis and preservation responsibilities. Clinical Record Administration has evidenced document ingestion, metadata indexing, versioning and legal lifecycle governance responsibilities. Neither establishes a universal meaning or completion criterion for incorporation.

Clinical Collaboration's discourse-versus-record boundary requires formal authorship / submission in the relevant context; it does not establish that authorship or submission alone is sufficient for authoritative record entry.

No universal incorporation lifecycle or Information Concept SHALL be created merely to bridge acknowledgement and record management. Where later Package 2 derivation requires a precise incorporation/adoption meaning, it SHALL be derived from the relevant Business Architecture evidence rather than assumed here.

### 8.8 Authority boundary

Acknowledgement, receipt, persistence, filing, indexing, assembly or review SHALL NOT by themselves transfer originating clinical authority.

Where information is incorporated into an assembly or managed record context, constituent authority and provenance remain attributable unless architecture explicitly establishes another qualification.

Approved K2 and K6 remain controlling. Responsibility for communication evidence or governed composition does not make Harmonia the originating clinical authority for the underlying assertions.

Under AX-13, a recipient's externally reported outcome may be retained as attributable evidence but SHALL NOT extend Harmonia's operational control or guarantees into the external recipient environment. Under AX-15, an unconfirmed outcome SHALL remain uncertain rather than being inferred as success or failure.

### 8.9 Clinical Document boundary

Where a Clinical Document is communicated, the following distinction SHALL be preserved:

```text
Communication Transaction
    ≠ Clinical Document
    ≠ constituent clinical assertions
```

Successful communication or acknowledgement SHALL NOT by itself:

- sign the document;
- finalise the document;
- approve the document;
- verify its clinical content;
- alter its originating authority.

Approved K2's process-specific requirements and unresolved signing, finality and legal-qualification semantics remain unchanged.

### 8.10 Frozen evidence basis

The [Package 2 evidence baseline, K7](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k7--acknowledgement-and-incorporation) and the focused K7 investigation establish the following evidence:

| Frozen source | Material evidence and bounded responsibility |
| :--- | :--- |
| [Domain02 Technical ACK vs. Business ACK](../../02-strategy/capabilities/business-enabling-capabilities.md#distinction-technical-ack-vs-business-ack); [Order Closed Loop example](../../02-strategy/capability-maps/capability-tier-model.md#example-2-order-closed-loop) | Technical receiving-boundary acceptance is explicitly distinct from business acknowledgement and acceptance of clinical responsibility. Business acknowledgement reports contextual Order / Referral dispositions from receiving applications or clinical staff. |
| [Domain02 Clinical Communication Administration, FEAT-SA-27/28/29](../../02-strategy/capabilities/business-enabling-capabilities.md#11-clinical-communication-administration-harmonia-core); [Domain03 Clinical Communication Administration, §12](../../03-business-architecture/behaviours/02-service-administration.md#12-clinical-communication-administration) | Secure distribution, recipient delivery/read-receipt correlation, business delivery/rejection tracking and transaction audit are evidenced. The stronger `received, parsed, accepted, and filed` wording remains incompletely qualified. |
| [Domain03 ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix); [cross-capability service boundary](../../03-business-architecture/dependencies/cross-capability-dependencies.md#11-service-exposure-and-ownership-boundary-pattern) | Clinical Communication Administration owns dispatch/acknowledgement/audit information and explicitly does not own message clinical content. Consumption does not transfer ownership. Document governance, longitudinal synthesis and collaboration discourse have separate bounded responsibilities. |
| [Patient Clinical Record](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#21-patient-clinical-record); [Health Information Exchange](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#22-health-information-exchange-hie); [Health Information Communication](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#24-health-information-communication); [Clinical Record Administration](../../03-business-architecture/behaviours/02-service-administration.md#10-clinical-record-administration) | Longitudinal assembly/preservation, exchange state/receipts, technical mediation and document ingestion/indexing/lifecycle are distinct behaviours. None supplies a universal acknowledgement-to-incorporation transformation. |
| [Clinical Information Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#21-clinical-information-interactions); [Information Governance Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#24-information-governance--control-interactions); [Practitioner and Care-Team Collaborations](../../03-business-architecture/collaborations-interactions/collaborations.md#32-practitioner-collaboration) | Supply, Provision, Review, Collaboration, Notification, Submission, Qualification and Publishing are separately described. Practitioner review and collaboration participation do not establish a universal approval or incorporation effect. |
| [Clinical Collaboration behaviour](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#27-clinical-collaboration); [Strategy Clinical Collaboration Context](../../02-strategy/capabilities/business-enabling-capabilities.md#clinical-collaboration-context); [To Do Progression](../../03-business-architecture/processes/business-processes.md#515-to-do-progression-process) | Discourse does not automatically constitute authoritative record content. Relevant entries require formal authorship/submission. Review worklist progression is distinct from a universal clinical-content qualification rule. |
| [Referral Progression](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process); [Closed-Loop Order Progression](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process); [Clinical Document Lifecycle](../../03-business-architecture/processes/business-processes.md#36-clinical-document-lifecycle-process) | Receipt, triage, acceptance, dispatch, result acknowledgement/closure and document governance have bounded process meanings. No dedicated Clinical Communication process or universal incorporation process is established. Approved K1 and K2 interpretations remain controlling. |
| [REQ-FND-001](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-001-durable-ingress-acceptance-boundary); [Architectural Axioms](../../../architectural-axioms.md); [Foundation authority governance](../governance/authority-custody-provenance.md); [assembly authority preservation](../assemblies-views/assemblies-and-views.md#11-the-authority-preservation-invariant) | Durable ingress acceptance is a receiving-boundary obligation. AX-06 separates information authority from transport and persistence; AX-13 bounds external guarantees; AX-14 preserves meaningful distinctions; AX-15 preserves uncertainty. Custody, consumption and assembly do not acquire originating authority. |

### 8.11 Upstream reconciliation required

Later, separately authorised G1 reconciliation SHALL review:

1. **Clinical Communication Administration, §12 — `received, parsed, accepted, and filed`:** Qualify the composite wording so that it does not imply a universal acknowledgement progression or automatic clinical authority, while preserving the evidenced possibility of a recipient filing report. This is semantic qualification, with the exact acceptance/filing criteria remaining unresolved architecture where a later decision is needed.
2. **Strategy `FEAT-SA-28` delivery/read receipts versus Domain03 business delivery/rejection acknowledgement wording:** Clarify their relationship while preserving the distinction between receipt/read evidence and stronger business dispositions. This is semantic qualification; read evidence SHALL NOT automatically imply clinical review or filing.
3. **Closed-Loop Order Progression, §3.5 — `Order Dispatched`:** Preserve the locally defined milestone combining routing and acceptance, while preventing it from implying that dispatch universally means recipient acceptance. This is terminology hygiene / semantic qualification.
4. **Closed-Loop Order Progression, §3.5 — `Closed / Verified`:** Apply approved K2. Result acknowledgement SHALL NOT be reinterpreted as diagnostic-content verification. This retains the existing K2 boundary rather than establishing a new interpretation.

No frozen Domain01–03, Domain04 Foundation or Package 1 source is amended or authorised for amendment by this record. Approval of the K7 disposition and authorisation to perform upstream reconciliation remain separate.

### 8.12 Explicit uncertainty

The architecture does not completely establish:

- the precise recipient meaning of `accepted` in Clinical Communication;
- the target and criteria of `filed`;
- acknowledgement evidence and correlation requirements;
- the effects of particular clinical reviews;
- whether a future domain requires an explicit incorporation/adoption concept;
- prerequisites and completion criteria for any such incorporation;
- the sufficiency of formal authorship/submission for authoritative record entry.

These uncertainties SHALL remain explicit. They SHALL NOT be resolved through messaging standards, FHIR, healthcare convention or implementation assumptions.

### 8.13 Domain04 consequence

Subsequent Package 2 derivation, including Clinical Communication, Clinical Document, Diagnostics, Referral, Care Plan and other communicated clinical information, SHALL NOT infer:

- clinical acceptance merely from `ACK`;
- incorporation from delivery;
- approval from review;
- changed originating authority from filing/incorporation;
- payload ownership from communication management;
- document finality from recipient acceptance;
- that communication transaction information and communicated clinical information are the same Information Concept merely because they travel together.

Derivation SHALL infer only the disposition expressly evidenced by the relevant acknowledgement. These constraints establish no universal acknowledgement or incorporation lifecycle.

This interpretation preserves AX-01/AX-04 business meaning, AX-05 state separation, AX-06 authority boundaries, AX-08 meaningful evidence, AX-13 management boundaries, AX-14 semantic distinctions and AX-15 uncertainty. The approved K1–K6 entries remain unchanged.

This approved decision derives no Package 2 Information Concept, relationship, lifecycle model or realised representation. It resolves K7 only; no other G1 issue is analysed or resolved by this entry, and G1 as a whole is not approved. Frozen Domain01–03, Domain04 Foundation and Package 1 sources remain unchanged.

## 9. K8 — Referrer Role Ambiguity

**Status: RESOLVED — APPROVED: REFERRER BUSINESS ROLE DISTINCT FROM REFERRAL PARTICIPATION**

**Disposition:** Retain `Referrer` as the canonical Domain03 Care Delivery Business Role and distinguish its fulfilment from contextual participation in a particular Referral. Require later, separately authorised reconciliation of the Role model's use of `Referrer` as a contextual source qualifier. The outstanding reconciliation and explicitly retained uncertainties do not reopen the approved K8 boundary.

### 9.1 Approved core distinction

`Referrer` is the canonical Domain03 Care Delivery Business Role representing the capacity in which an Actor formally refers.

The following participation model SHALL be preserved:

```text
Actor
    → fulfils Business Role
    → participates in particular Referral behaviour
    → that participation may be represented through Information Relationships
```

These architectural concepts SHALL remain distinct:

```text
Actor
    ≠ Business Role
    ≠ contextual participation in a particular Referral
    ≠ Relationship Role / contextual qualifier
    ≠ information representing that participation
```

A Business Role SHALL NOT be created or inferred merely from a similarly named Relationship Role or contextual qualifier. A contextual referral position SHALL NOT automatically establish fulfilment of the `Referrer` Business Role. Referral behaviour may occur independently or within a Business Collaboration; participation does not itself establish an enduring collaboration.

### 9.2 Referrer Business Role

The frozen Role definition establishes a reusable capacity associated with formally requesting another care participant or service provider to assess, manage or assume some degree of clinical care responsibility. Approved K1 remains controlling, including its prohibition on treating `partial` / `total` as an architectural taxonomy at this stage.

Referrer fulfilment SHALL NOT imply that:

- the Referral was accepted;
- service delivery occurred;
- responsibility was actually assumed;
- Transfer of Care occurred.

Stable Business Role does not mean permanent assignment to an Actor.

### 9.3 Actor eligibility

The frozen architecture directly supports practitioner participation in the Referrer capacity. The phrase `service agent` prevents interpreting the Role as necessarily practitioner-only, but does not establish a complete Actor eligibility model.

Organisation participation as a referral source is evidenced. However, `Organisation-as-ServiceProvider#ReferralSource` does not by itself prove that the Organisation fulfils the `Referrer` Business Role.

Package 2 SHALL NOT derive an exhaustive Actor-to-Referrer eligibility matrix. Service-agent eligibility, explicit organisational Referrer fulfilment and other Actor-category eligibility remain explicitly uncertain.

### 9.4 Referrer vs. Requester

`Referrer` SHALL remain distinct from `Requester`:

- `Referrer` concerns the capacity to formally refer for assessment, management or possible assumption of care responsibility.
- `Requester` concerns the capacity to formally place an Order for investigation, medication supply or procedural intervention within its frozen scope.

One Actor may fulfil both Roles where independently established. Shared Actor identity SHALL NOT collapse the Role meanings.

### 9.5 Referrer vs. Service Provider

`Referrer` SHALL remain distinct from `Service Provider`. An Actor may fulfil both Roles where independently established. Service Provider fulfilment is not established as a prerequisite for Referrer fulfilment.

`Practitioner` SHALL likewise remain distinct from `Service Provider`.

### 9.6 Contextual Referral participation

Particular-Referral participation may later be represented using appropriately derived Information Relationships, Relationship Roles or contextual qualification. K8 does not prescribe their final Package 2 names, structure or cardinality.

Future Relationship Roles SHALL NOT be assumed to require the names `Referrer`, `Referee`, `Requester` or `Recipient`. Their semantics SHALL be derived from the Referral information requirements.

Relationship Source and Relationship Target positions SHALL NOT automatically imply:

- clinical Referrer;
- receiving care participant/provider;
- originating authority;
- acceptance;
- responsibility assumed.

### 9.7 Qualified Architectural References

The existing qualified-reference example:

```text
Organisation#ACT Health-as-ServiceProvider#ReferralSource
```

correctly demonstrates the distinction between:

- Actor category: `Organisation`;
- fulfilled Business Role: `ServiceProvider`;
- contextual function: `ReferralSource`.

`ReferralSource` SHALL NOT create or imply the `Referrer` Business Role. This separation SHALL guide later reconciliation of the ambiguous Role-model wording. A contextual qualifier identifies situational function; it does not create a new Business Role.

### 9.8 Referring Clinician Relationship Role

Package 1's `Referring Clinician` usage as a local Relationship Role remains valid. It describes semantic participation within that Information Relationship.

It SHALL NOT create a new Business Role or redefine the canonical `Referrer` Business Role. No Package 1 correction is authorised by K8 for this usage.

### 9.9 Authority and responsibility

Referrer fulfilment alone does not establish:

- blanket information authority;
- authority over every clinical assertion included with a Referral;
- clinical decision-making authority beyond that independently established;
- ownership of supporting information;
- transfer of originating authority.

An Actor may originate or attest particular assertions where independently established. Submitting or assembling a Referral does not make the Referrer authoritative for every supporting information item.

Approved K1, K6 and K7 remain controlling. Contextual referring or receiving participation, service-provider associations, receipt, acknowledgement and cross-capability service consumption SHALL NOT by themselves establish acceptance of clinical responsibility, Transfer of Care or transferred information ownership or originating authority.

### 9.10 Frozen evidence basis

The [Package 2 evidence baseline, K8](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k8--referrer-role-ambiguity) and the focused K8 investigation establish the following evidence:

| Frozen source | Material evidence and bounded interpretation |
| :--- | :--- |
| [Business Role model, §§1, 2.2, 2.6 and 3.2–3.3](../../03-business-architecture/actors-roles/roles.md) | Defines stable capacities and permits simultaneous fulfilment of distinct Roles. Catalogues Referrer and Requester separately, defines Service Provider separately, and preserves Practitioner ≠ Service Provider. The guardrail also calls Referrer a contextual source qualifier, establishing the typing defect requiring reconciliation. |
| [Business Actor model](../../03-business-architecture/actors-roles/actors.md); [participation model](../../03-business-architecture/collaborations-interactions/collaborations.md#2-the-actor-role-interaction-collaboration-relationship); [Service Delivery Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#22-service-delivery-interactions) | Actors fulfil Roles and participate in Interactions, optionally within Collaborations. Service Referral, Service Request, Service Response, Service Outcome and Transfer of Care are distinct Interactions. The evidence does not supply a complete Referrer eligibility matrix. |
| [Qualified Architectural Reference Grammar, §7](../../03-business-architecture/metamodel/business-architecture-metamodel.md#7-qualified-architectural-reference-grammar) | Separates canonical stable Role from contextual function. The Organisation-as-ServiceProvider#ReferralSource example explicitly demonstrates organisational source participation without separately assigning the Referrer Business Role. |
| [Referral Administration Features](../../02-strategy/capabilities/business-enabling-capabilities.md#2-referral-administration-harmonia-relevant); [Referral Functions and Services](../../03-business-architecture/behaviours/02-service-administration.md#3-referral-administration); [Referral Progression](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process) | Names referring providers, submitting GP practices/facilities, referrers as milestone consumers, and distinct receipt, triage, acceptance and subsequent progression dispositions. This participation and consumer wording does not independently establish every Actor's Business Role or care responsibility. Approved K1 controls Referral scope. |
| [Information Responsibility ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix); [cross-capability dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) | Referral Administration owns incoming packages, triage and disposition information, excluding closed-loop Orders. Service Provision Resolution resolves target clinics, receiving providers and catchment rules; resolution and consumption do not establish acceptance or transfer authority. |
| [Foundation Relationship Role separation](../patterns/information-relationships.md#2-strict-separation-relationship-role-vs-business-role); [modelling guardrails](../guardrails/modelling-guardrails.md); [Package 1 Practitioner family, §4.2](../information-families/practitioner.md#42-separation-of-relationship-roles-from-business-roles) | Local Relationship Roles do not create Business Roles. Referring Clinician is expressly a Relationship Role. Source/Target positions and endpoint Recipient participation do not establish clinical capacity, acceptance or authority. |
| [Foundation authority governance](../governance/authority-custody-provenance.md#2-the-four-pillars-of-information-governance); [Architectural Axioms](../../../architectural-axioms.md) | Responsibility, authority, custody and consumption remain distinct; authority can attach to individual assertions and relationships. Transport, assembly and contextual participation do not confer blanket originating authority. |

### 9.11 Upstream reconciliation required

Domain03 Role-model wording currently uses `Referrer` both as the canonical Business Role and as a contextual source qualifier. This is an architectural typing defect.

Later, separately authorised G1 reconciliation SHALL clarify [Role model §3.2 — Referral Direction is Contextual](../../03-business-architecture/actors-roles/roles.md#32-referral-direction-is-contextual) so that:

- `Referrer` remains the canonical Business Role;
- contextual referral direction is expressed separately;
- the existing `ReferralSource` qualified-reference example remains consistent with that separation;
- no new fundamental directional Business Role is created.

`Referee` SHALL NOT be silently renamed to `Recipient`, `Requester` or another term during this correction. Where the exact target contextual qualifier remains unresolved, that uncertainty SHALL be preserved rather than inventing terminology.

No frozen Domain01–03, Domain04 Foundation or Package 1 source is amended or authorised for amendment by this record. Approval of the K8 disposition and authorisation to perform upstream reconciliation remain separate.

### 9.12 Explicit uncertainty

The architecture does not completely establish:

- complete Actor eligibility for the Referrer Business Role, including service-agent and other Actor-category eligibility;
- explicit organisational Referrer assignment;
- per-participant capacity mappings;
- assertion-level authority allocation;
- exact future Relationship Role / qualifier names for Referral participation;
- K1's retained semantics concerning acceptance, effective responsibility and relinquishment.

These uncertainties SHALL remain explicit. They SHALL NOT be resolved through FHIR, referral-management conventions, EMR terminology, general healthcare convention or implementation assumptions.

### 9.13 Domain04 consequence

Subsequent Referral derivation SHALL NOT assume that:

- the Referrer Business Role itself is an Information Concept;
- every Referral source necessarily fulfils the Referrer Business Role;
- every Requester is a Referrer;
- every Referrer is a Practitioner;
- every Referrer is a Service Provider;
- every receiving participant has accepted the Referral;
- Relationship Roles create Business Roles;
- contextual participation transfers clinical or information authority.

Referral information SHALL distinguish the participant, its independently established Business Role where relevant, its contextual participation in the particular Referral, and information representing that participation.

This interpretation preserves AX-01/AX-04 business meaning, AX-06 explicit authority, AX-13 management boundaries, AX-14 semantic distinctions and AX-15 uncertainty. The approved K1–K7 entries remain unchanged.

This approved decision derives no Package 2 Information Concept, relationship, cardinality, lifecycle model or realised representation. It resolves K8 only; no other G1 issue is analysed or resolved by this entry, and G1 as a whole is not approved. Frozen Domain01–03, Domain04 Foundation and Package 1 sources remain unchanged.

## 10. K9 Prerequisite — Business Architecture Identification Metamodel

**Status: IDENTIFICATION METAMODEL APPROVED — K9 RECONCILIATION PENDING**

**Disposition:** Formalise typed contextual identification, common architectural identity and direct historical alias resolution before subsequent K9 reconciliation. This decision approves the identification convention and only its bounded documentation changes; it does not validate existing Capability Tier assignments or complete K9.

### 10.1 Approved Terminology and Typed Identification

`Layer / Architectural Layer` SHALL mean architectural layering within the TOGAF / ArchiMate architecture model. `Capability Tier` SHALL mean hierarchical decomposition within a Capability Model. `Derivation / Derivation Progression` SHALL mean progression between different architectural constructs or models.

`Layer ≠ Capability Tier ≠ Derivation Stage`.

Canonical decomposition terminology is `CT1` (Capability Tier 1), `CT2` (Capability Tier 2), `CT3` (Capability Tier 3) and `FT` (Feature). `L1`, `L2` and `L3` are deprecated as canonical capability classifications. Domain02's existing five stages constitute a derivation progression and SHALL NOT be reinterpreted as CT1 / CT2 / CT3 / FT.

The [Domain03 identification convention](../../03-business-architecture/metamodel/business-architecture-metamodel.md#8-architectural-element-identity-and-canonical-identification) defines the normative grammar. Capability tokens SHALL carry their tier type as `CT1-<approved-local-code>`, `CT2-<approved-local-code>` or `CT3-<approved-local-code>`. Behavioural tokens SHALL use `FN-`, `SV-` or `PR-` followed by an approved local code. Established Feature identifiers retain their canonical spelling, including `FEAT-EM-01`.

Examples such as `CT1-01.FN-01`, `CT1-01.CT2-02.PR-01` and `CT1-01.CT2-02.CT3-03.FEAT-EM-01.FN-01` are syntax examples only. They SHALL NOT allocate identifiers to existing elements.

### 10.2 Ancestry and Behavioural Anchoring

Not every branch must materialise every possible lower Capability Tier or a Feature. Functions, Services and Processes MAY be anchored directly at any Capability Tier or Feature establishing responsibility for that behaviour. Missing CT2, CT3 or FT elements SHALL NOT be manufactured to provide behavioural anchoring.

Where a lower Capability Tier is materialised, its established higher-tier Capability ancestry SHALL be represented in its Canonical ID. `CT1-01.CT3-02` SHALL NOT be generated merely because CT2 ancestry has not been identified. Required but unresolved ancestry SHALL leave the Canonical ID unresolved.

Identifier containment expresses responsibility context independently of Markdown nesting and SHALL NOT redefine Element Type, semantic relationships or implementation containment. FN / SV / PR are behaviour types, not additional Capability Tiers.

### 10.3 Identity, Rename and Alias Semantics

Every governed architectural element SHALL have exactly one Element Type, Canonical Name and current Canonical ID, with zero or more Name Aliases and ID Aliases. A name change alone does not change identity or require a Canonical ID change. A responsibility-context relocation SHALL change the Canonical ID and retain the previous Canonical ID as an ID Alias.

Canonical IDs and ID Aliases occupy a globally unique, non-reusable identifier namespace. ID Aliases SHALL be searchable, resolvable and non-canonical, and SHALL resolve directly to the same current element without requiring alias chains. New authoritative references SHALL use the current Canonical ID.

The approved rename `Clinical Credential Management → Clinical Qualification Management` preserves the existing professional / clinical qualification and competency responsibility. A Name Alias MAY retain the previous name only for that same element. Domain03 `Credential Management` retains its separate operational badge / physical-access responsibility; the rename SHALL NOT establish equivalence or aliasing between the two elements. Catalogue reconciliation is outside this documentation change.

Approved K5's semantic and traceability constraints remain unchanged. Unsupported Feature headings SHALL NOT gain Feature identity or aliases merely because their valid Functions or Services occur within broader Feature scope. No new Capability, Feature or Feature identifier is established by this decision.

### 10.4 K9 Evidence, Uncertainty and Validation Intent

The [frozen evidence baseline, K9](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k9--capability-metamodel-typing) records conflicting source-level classifications, including Domain03 owner labels and examples not established by Strategy. Those observations remain evidence of required reconciliation. The approved convention does not establish that any existing `L1 → CT1`, `L2 → CT2` or `L3 → CT3` assignment is correct.

`Identifier Resolution = FT / FEAT-EM-01` SHALL be preserved. Its full structural Canonical ID remains unresolved until actual Capability Tier ancestry and identifier allocation are architecturally established. The former incorrect Domain03 L3 example SHALL NOT establish ancestry or reclassify this Feature.

Existing catalogue assignments, duplicated overview diagrams and owner references remain unreconciled source assertions. Required ancestry and code allocation SHALL NOT be inferred from document nesting, catalogue numbering, contextual views or derivation-stage position. Identifier vocabularies and contexts for other Business Architecture element types remain unspecified by this decision.

The convention supports future navigation, diagrams, model generation, traceability matrices, cross-document references and AI-assisted interpretation. Future validation is intended to establish Canonical ID and ID Alias uniqueness, canonical/alias collision freedom, ancestor existence, valid element-type suffixes, valid Capability / Feature context, resolvable references, direct unambiguous alias resolution and absence of orphaned identifiers. No validators are implemented by this decision.

### 10.5 Bounded Documentation Authority and Stop Boundary

The approved documentation changes are confined to:

- [Domain03 Business Architecture metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md): terminology, decomposition rules, typed identification grammar, common identity, aliases, behavioural anchoring, validation intent and unresolved-context rules.
- [Domain02 capability decomposition / derivation model](../../02-strategy/capability-maps/capability-tier-model.md): distinguish Capability decomposition from the existing five-stage derivation progression without assigning existing elements to Capability Tiers.
- This G1 review record: retain the approved K9 prerequisite, its authority and its unresolved reconciliation boundary.

The evidence baseline remains a historical record; its frozen-source quotations and fingerprints are not rewritten by this decision. Existing K1–K8 dispositions remain unchanged.

This decision preserves AX-01/AX-04 business meaning, AX-14 semantic distinctions and AX-15 uncertainty. It SHALL NOT authorise repository-wide identifier migration, mechanical conversion of L1/L2/L3 assignments, Capability code allocation, manufactured ancestry, Package 2 Information Family changes, schemas or Java classes, completion of K9 reconciliation or investigation of K10. Work stops after these three documentation changes for architectural review.

## 11. K9 — Capability Metamodel Typing

**Status: RESOLVED — APPROVED: CAPABILITY IDENTITIES AND RESPONSIBILITIES PRESERVED; CAPABILITY TIER / ANCESTRY UNRESOLVED**

**Disposition:** Close K9 because the architectural investigation establishes what the current architecture does and does not determine. Preserve established architectural identities, responsibilities, behavioural ownership contexts and catalogued Feature classifications. Retain unresolved Capability Tiers, ancestry, root status and structural Canonical IDs explicitly. Completion of the full Business Enabling Capability hierarchy is not a prerequisite for Domain04 Package 2 derivation. Source reconciliation remains a later, separately authorised activity.

The approved identification prerequisite in §10, the [Capability decomposition / derivation model](../../02-strategy/capability-maps/capability-tier-model.md) and the [Architectural Element Identification metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#8-architectural-element-identity-and-canonical-identification) remain controlling. This final disposition supersedes the prerequisite's incomplete-K9 position without changing its identification rules or retrospectively validating legacy classifications. Approved K1–K8, including K5, remain unchanged.

### 11.1 Approved findings and explicit uncertainty

| Architectural dimension | Established finding | Unresolved boundary |
| :--- | :--- | :--- |
| Architectural identity | Business Enabling Capability identities are established for the majority of affected elements. | The architectural-element status of every named catalogue decomposition entry is not established. |
| Semantic responsibility | The relevant responsibilities of those established Capabilities are preserved. Named responsibility contexts for Functions, Services and Processes are substantially established. | Established responsibility does not prove numerical Capability Tier or complete structural ancestry. |
| Feature type | All explicitly catalogued Features are FT; existing Feature identifiers are preserved. `Identifier Resolution` is FT / `FEAT-EM-01`. | Unsupported Domain03 Feature headings remain governed by approved K5; presentation alone creates no Feature identity. |
| Capability Tier | CT1 / CT2 / CT3 are the approved decomposition terminology. | No affected legacy L1 / L2 / L3 owner assignment is independently validated sufficiently to establish a CT1 / CT2 / CT3 assignment. |
| Capability ancestry and root status | Selected explicit parent/responsibility relationships are established, including `Client Administration → Person Identity`. | Complete Capability ancestry and root status for the affected Business Enabling Capabilities remain unresolved. The explicit relationship does not establish either element's tier or Client Administration's root status. |
| Structural Canonical ID | The approved identification grammar and established Feature identifiers remain valid. | Complete structural Canonical IDs cannot yet be established where required tier, ancestry or identifier allocation is unresolved. |

The [Business Enabling Capability catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md), Domain03 behaviour ownership, [Process ownership](../../03-business-architecture/processes/business-processes.md), [Information Responsibility](../../03-business-architecture/information-responsibility/information-responsibility.md) and [cross-capability dependencies](../../03-business-architecture/dependencies/cross-capability-dependencies.md) establish the relevant identities and responsibility contexts within their evidenced boundaries. The explicit `Person Identity` parent relationship is stated in [Governed Person Identity Correction, §2.1](../../03-business-architecture/processes/business-processes.md#21-governed-person-identity-correction-process); its disputed L2 label does not establish the tier.

The [historical K9 evidence baseline](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k9--capability-metamodel-typing) remains evidence of the source assertions requiring reconciliation. Established owner identity, established responsibility and established Feature type SHALL remain distinct from unresolved Capability Tier, ancestry and structural identification. A typing defect SHALL NOT invalidate an otherwise established Capability identity or change its semantic responsibility.

### 11.2 Legacy classification disposition

No affected legacy L1 / L2 / L3 assignment has been independently validated sufficiently to authorise mechanical translation to CT1 / CT2 / CT3. The investigation categories remain:

| Existing assertion | Investigation category | Approved treatment |
| :--- | :--- | :--- |
| Affected legacy owner L1 / L2 assignments | C — unsupported / insufficiently evidenced | Preserve the established owning element and responsibility; retain its Capability Tier and complete ancestry as unresolved. |
| Former `Identifier Resolution` L3 example | D — established Feature incorrectly represented as a Capability Tier | Preserve FT / `FEAT-EM-01`; do not classify it as CT3 or use the former label to establish ancestry. |
| Former `Closed-Loop Order Progression` L3 and `Diagnostic Orders` L2 examples | C — unsupported / insufficiently evidenced | The former examples establish no validated Capability Tier or ancestry. Preserve independently evidenced Process responsibility without manufacturing a Capability identity or parent. |
| Unsupported Domain03 Feature headings | Approved K5 controls | Preserve valid Functions and Services in their evidenced owning context; create no Feature, Feature ID or alias to obtain traceability symmetry. |

No affected legacy assignment is established as category A — a semantically validated hierarchy requiring terminology migration alone. Unsupported hierarchy assertions SHALL NOT be treated as mere deprecated wording. `L1 → CT1`, `L2 → CT2` and `L3 → CT3` SHALL NOT be performed as a mechanical migration.

### 11.3 Downstream derivation rule

> Unresolved Capability Tier or ancestry SHALL NOT prevent downstream architectural derivation where the owning Capability or Feature and its relevant responsibility are otherwise established. Downstream traceability SHALL reference the established architectural element and SHALL NOT manufacture unresolved structural ancestry.

Domain04 Package 2 may therefore derive Information Concepts from established Business Architecture responsibility without waiting for complete CT1 / CT2 / CT3 classification. This permission does not establish missing responsibility or architectural identity where those are themselves unresolved.

Where a full structural Canonical ID cannot yet be established, traceability SHALL use the established architectural type, canonical name and existing identifier where one exists, while retaining the structural Canonical ID as unresolved. Placeholder structural IDs SHALL NOT be manufactured. An existing Feature identifier SHALL NOT be represented as proof of a complete structural Canonical ID.

### 11.4 Architectural Element identity and catalogue presentation

> Document structure, catalogue grouping, numbering, indentation and decomposition presentation SHALL NOT by themselves establish Architectural Element identity. Canonical Architectural IDs SHALL be assigned only to governed Architectural Elements whose identity and architectural context are established.

Named catalogue decomposition entries SHALL NOT automatically become CT2 or CT3 Capabilities because they appear beneath another Capability. Their architectural-element status may remain unresolved. Historical L1 / L2 / L3 labels and derivation-stage position likewise SHALL NOT serve as self-validating evidence of ancestry or tier.

The five contextual views — Entity Management, Service Administration, Service Delivery, Health Service Operations and Intrinsic / Shared Enablement — remain organisational views. They SHALL NOT automatically become CT1 elements or identifier ancestors. Established Capability contexts beneath a view do not by themselves prove root CT1 status.

### 11.5 Behavioural ownership and Feature preservation

Named responsibility contexts for affected Domain03 Functions, Services and Processes are substantially established even though their complete structural paths are not. Those behaviours may continue to trace to their established owning Capability or Feature without a complete structural Canonical ID. The identification prerequisite permits direct anchoring at an established Capability Tier or FT context; missing intermediate Capabilities or Features SHALL NOT be manufactured.

All explicitly catalogued Features SHALL remain FT, and all established Feature identifiers SHALL be preserved. The control case is:

| Identifier Resolution property | Approved finding |
| :--- | :--- |
| Type | FT |
| Existing Feature identifier | `FEAT-EM-01` |
| Owning context | Person Identity |
| Complete structural Canonical ID | Unresolved |

The explicit `Client Administration → Person Identity` relationship and Identifier Resolution's owning context establish only a partial responsibility chain. They SHALL NOT supply missing Capability Tier ancestry or a complete structural Canonical ID.

Approved K5 remains controlling for unsupported Feature headings, existing Feature IDs and canonical naming. Valid Functions and Services do not establish a new Feature merely through a heading or similarity to a broader Feature. No FN / SV / PR identifiers are allocated during K9 persistence.

### 11.6 Clinical Qualification / Credential distinction

`Clinical Qualification Management ≠ Credential Management`.

The approved rename `Clinical Credential Management → Clinical Qualification Management` preserves professional / clinical qualification and competency responsibility. Domain03 `Credential Management` retains its separately evidenced operational badge / physical-access responsibility. The elements SHALL NOT be merged or aliased. Neither element's Capability Tier or ancestry is established by the rename or by lexical similarity.

The lexical similarity of Feature or behaviour names SHALL NOT establish semantic identity. In particular, the badge/access-context `Operational Credential Check` behaviour SHALL NOT be bound to `FEAT-HSO-23` solely by name. That catalogued Feature's qualification/competency responsibility remains distinct from the operational badge/access behaviour.

### 11.7 Later, separately authorised G1 reconciliation

The minimum later reconciliation SHALL distinguish:

- **Terminology and unsupported hierarchy assertions:** remove or qualify unsupported numerical Capability Tier assertions in the affected sources; use approved terminology only where tier is independently established. Preserve established Capability identities and responsibilities; otherwise retain tier, root status and complete ancestry as unresolved.
- **Evidenced relationship:** preserve `Client Administration → Person Identity` without inventing tiers, intermediate parents or root status. Completion of the hierarchy may occur through later Business Architecture refinement and is not required to close K9.
- **Feature correction:** preserve catalogued FT identities and existing Feature IDs; correct the former Identifier Resolution Capability Tier representation; apply approved K5 to unsupported Feature headings without manufacturing Features or identifiers.
- **Canonical naming correction:** apply approved K5 naming corrections, including Service Delivery naming and Clinical Logistics Coordination, and the approved Clinical Qualification Management rename. Preserve the distinct operational Credential Management element and its responsibility.
- **Downstream traceability correction:** reconcile affected Package 1 traceability labels against established architectural types and owning responsibilities, retaining unresolved structural context explicitly. Preserve the separately approved K3/K6 constraints; do not change Function, Service, Process, Information Responsibility or dependency semantics to repair a type label.
- **Historical evidence and identification:** preserve the evidence baseline, including its historical quotations and fingerprints; allocate no speculative structural Canonical IDs. Catalogue presentation SHALL NOT create governed Architectural Elements or substitute for established context.

Approval of this disposition and authorisation to perform source reconciliation remain separate. This persistence task changes only this durable G1 review record; frozen Domain01–03, Domain04 Foundation and Package 1 sources remain unchanged.

### 11.8 Closure and stop boundary

K9 is RESOLVED on the approved interpretation above. Unresolved Capability Tier, ancestry, root status, decomposition-entry identity and structural Canonical ID information remain explicit architectural uncertainties; they do not reopen K9 or require completion of the full Business Enabling Capability hierarchy before downstream derivation from established responsibility.

This disposition preserves AX-01/AX-04 business meaning, AX-14 semantic distinctions and AX-15 uncertainty. It neither changes the frozen Capability Model nor authorises missing identity, ancestry or identifier allocation to be invented. It preserves the approved identification prerequisite and K1–K8 unchanged, completes no G1 reconciliation, derives no Package 2 Information Concept and does not investigate K10 or approve G1 as a whole. Work stops after persistence of this approved K9 disposition and review of the resulting diff.

## 12. K10 — Behaviour versus Business Process Progression / Detail

**Status: RESOLVED — APPROVED: BEHAVIOUR AND PROCESS ARE COMPLEMENTARY REPRESENTATIONS; PROCESS MAY ELABORATE PROGRESSION WITHOUT REDEFINING RESPONSIBILITY**

**Disposition:** Close K10 by establishing the semantic relationship between Domain03 Behaviour descriptions and their governed Business Processes. Preserve legitimate abstraction and detail differences, require bounded terminology qualification, and retain the three separately discovered progression/timing uncertainties explicitly. Those uncertainties do not invalidate the established metamodel relationship and do not keep K10 open. Later source reconciliation requires separate authorisation.

### 12.1 Approved metamodel finding

The [Domain03 Business Architecture metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#2-capability--feature-semantics) and [Behaviour derivation conventions](../../03-business-architecture/behaviours/index.md#2-derivation-conventions) establish:

| Architectural construct | Approved semantic distinction |
| :--- | :--- |
| Capability / Feature | Establishes architectural context, scope and responsibility. |
| Business Function | Represents behaviour delivered within that responsibility. |
| Business Service | Exposes Function behaviour outside the owning boundary where justified by an identifiable consumer. Exposure and consumption do not transfer ownership. |
| Business Process | Represents progression of business behaviour where meaningful states, dispositions or outcomes materially matter, within its owning Capability or Feature. |

A Behaviour document presents owning context, semantics, Functions, Services, justified Processes and Information Responsibility. It is not a separate architectural element type. Not every Function requires a Process, and a Service is not generated mechanically for every Function.

The [K10 evidence baseline](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k10--processlifecycle-variants) records the source variations. The focused investigation establishes explicit Behaviour-to-Process links for all sixteen principal Processes. These links establish corresponding responsibility contexts; they do not validate every depicted transition or resolve every local applicability question. Approved K1–K9 remain controlling and unchanged.

### 12.2 Behaviour / Process consistency rule

> A Business Process may elaborate progression for a defined activity within its owning Capability or Feature without reproducing the Behaviour description's stage list. It must preserve established responsibility, ownership, authority boundaries and supported obligations. Scoped applicability, compound checkpoints and alternative dispositions must remain explicit; additional detail must not silently become a universal requirement or redefine the owning responsibility.

> Omission of a Process checkpoint from a Behaviour summary does not invalidate that checkpoint; omission of Behaviour responsibility from a Process does not remove that responsibility.

A scoped Process may represent only part of a Capability's behaviour without narrowing the Capability itself. Stage-count equality is not required. Greater Process detail does not give the Process authority to redefine the governing responsibility.

The approved relationship does not require a union of displayed stages, a universal lifecycle or textually identical representations. Independently evidenced local obligations remain valid within their established scope; detail alone SHALL NOT broaden their applicability or transfer responsibility.

### 12.3 Referral

[Referral Administration Behaviour](../../03-business-architecture/behaviours/02-service-administration.md#3-referral-administration) and [Referral Progression Process](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process) are compatible summary/elaboration representations of the same owning responsibility.

Approved K1 remains controlling:

```text
Referral intent
    ≠ Referral disposition
    ≠ responsibility actually assumed
    ≠ Transfer of Care
```

Intake validation, clinical triage, consultation attendance and rejection may be explicit Process checkpoints without becoming universally mandatory Referral stages. `Accepted / Waitlisted` is a locally described compound checkpoint and SHALL NOT establish universal equivalence between acceptance and waitlisting.

The Process sequence SHALL NOT be interpreted as requiring every Referral to proceed through acceptance, scheduling, attendance or discharge. It does not guarantee service delivery, responsibility actually assumed or Transfer of Care. Rejection and the independently evidenced decline/redirection outcomes remain valid; their exact pathways SHALL NOT be invented from the displayed sequence.

Later qualification of pathway and acceptance/waitlisting wording SHALL preserve K1's approved Referral scope rather than narrow it to the illustrated specialist booking/attendance pathway.

### 12.4 Encounter

[Encounter Lifecycle Process](../../03-business-architecture/processes/business-processes.md#34-encounter-lifecycle-process) provides additional scoped lifecycle detail within [Episode & Encounter Administration](../../03-business-architecture/behaviours/02-service-administration.md#5-episode--encounter-administration) responsibility. Planning, arrival, activation, active progression, discharge and administrative completion need not be identical to the shorter Behaviour presentation.

`Completed / Encoded` criteria, including finalised clinical documentation, completed coding and legal closure, are locally described Process qualifications. They SHALL NOT establish universal:

- signing requirements;
- legal-closure semantics;
- applicability across all Encounter settings;
- authorship or originating authority over associated clinical information.

Approved K2 remains controlling. The additional completion criteria SHALL be preserved within their evidenced scope without making finalisation, signing, legal qualification or authority synonymous.

Strategy's independently evidenced [`ON_LEAVE` state](../../02-strategy/capabilities/business-enabling-capabilities.md#4-episode--encounter-administration-harmonia-core) remains valid. Its relationship to the illustrated Process remains unresolved and SHALL NOT be invented during K10 reconciliation. Omission from the shorter representations does not remove the independently evidenced state or establish its applicability and transitions universally.

### 12.5 Order

[Order Behaviour](../../03-business-architecture/behaviours/02-service-administration.md#6-order-administration) and [Closed-Loop Order Progression Process](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process) are compatible bounded representations requiring terminology qualification.

The following distinction SHALL be preserved:

```text
transmission / dispatch
    ≠ receipt
    ≠ acceptance
```

A Process checkpoint may combine routing and acceptance for that defined progression without redefining the semantics of the exposed dispatch Service. Behaviour `Received` SHALL NOT be equated with technical receipt or the Process's composite `Order Dispatched` checkpoint without evidence.

Approved K2, K3, K6 and K7 remain controlling for signing, result authority, correlation and acknowledgement semantics. Result binding/correlation does not confer originating clinical authority or establish result reconciliation. `Closed / Verified` retains its described requesting-clinician acknowledgement and Order-closure context; the label SHALL NOT establish universal clinical verification or incorporation.

Cancellation/modification responsibilities remain valid even where omitted from the illustrated Process progression. Their omission SHALL NOT remove the Behaviour responsibility or authorise missing pathways to be invented.

### 12.6 Clinical Document

[Clinical Document Lifecycle Process](../../03-business-architecture/processes/business-processes.md#36-clinical-document-lifecycle-process) provides additional qualification detail within [Clinical Record Administration](../../03-business-architecture/behaviours/02-service-administration.md#10-clinical-record-administration) responsibility.

`Final ≠ automatically Final Signed`.

Signing, countersigning, finalisation, publication, legal qualification and authority remain distinct under approved K2. The locally evidenced Process requirements remain valid within their scope; their omission from the shorter Behaviour description does not waive them or make them universal across Clinical Documents.

The Process sequence SHALL NOT imply that every Clinical Document must progress through amendment, supersession and entered-in-error states. Document governance does not transfer originating authority over constituent clinical assertions. Approved K6/K7 authority and acknowledgement boundaries remain unchanged.

### 12.7 Separately retained progression / timing findings

The three findings below remain **UNRESOLVED** for later G1 reconciliation / architectural clarification. They SHALL NOT keep K10 open because they do not invalidate the established Behaviour-versus-Process metamodel relationship. They SHALL remain unresolved until specifically corrected or clarified; no semantic repair is approved merely by recording them here.

#### 12.7.1 Discharge publication timing

The [Discharge Progression Process](../../03-business-architecture/processes/business-processes.md#413-discharge-progression-process) appears to place summary publication/transmission before physical departure: its `Discharge Summary Signed` checkpoint describes publication and transmission to the GP before `Physically Departed`. The [dependency model](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) appears to trigger publication upon departure.

The intended timing relationship remains unresolved. Separate publication events SHALL NOT be invented to reconcile these statements. Approved K2's distinction between summary finalisation and signing remains controlling.

#### 12.7.2 To Do progression ordering

The current [To Do Progression sequence](../../03-business-architecture/processes/business-processes.md#515-to-do-progression-process) places dismissal/delegation after action/decision. The architecture does not establish whether these are alternative dispositions, subsequent actions or scoped variants.

That relationship SHALL remain unresolved rather than being normalised into an invented transition model. Workflow coordination does not acquire the underlying clinical decision or outcome responsibility.

#### 12.7.3 Synthetic Task progression ordering

The current [Synthetic Task Progression sequence](../../03-business-architecture/processes/business-processes.md#516-synthetic-task-progression-process) places stalled/failed after successful completion. The architecture does not establish whether these are alternative outcomes, reopening/post-completion behaviour or simply a presentation defect.

That relationship SHALL remain unresolved. Success, failure and stalled progression SHALL NOT be made equivalent, and reopening or post-completion behaviour SHALL NOT be invented to reconcile the sequence.

### 12.8 Later, separately authorised G1 reconciliation

Later authorised G1 reconciliation SHALL:

- clarify the Behaviour / Process consistency rule while preserving complementary representations;
- qualify Referral pathway and acceptance/waitlisting wording under K1;
- preserve Encounter's local completion criteria without universalising them, retaining the unresolved `ON_LEAVE` relationship;
- distinguish Order transmission, receipt and acceptance;
- apply K2 document/signing qualifications without silently equating the terms;
- preserve the three separate progression/timing uncertainties without inventing semantics, pending their specific architectural correction or clarification.

Process stage lists SHALL NOT be copied into Behaviour descriptions merely for textual alignment. Approval of this interpretation and authorisation to reconcile frozen sources remain separate. The evidence baseline remains historical evidence; its quotations and fingerprints are not rewritten by this decision.

### 12.9 Closure and stop boundary

K10 is RESOLVED on the approved Behaviour / Process relationship and bounded qualifications above. The three separately retained progression/timing uncertainties do not reopen K10. This closure does not approve every Process transition or complete the unresolved applicability and qualification semantics retained by earlier decisions.

This interpretation preserves AX-01/AX-04 business meaning, AX-14 semantic distinctions and AX-15 uncertainty. Approved K1–K9, including the identification prerequisite, remain unchanged. This persistence task changes only this durable G1 review record; it modifies no frozen Domain01–03, Domain04 Foundation or Package 1 source, performs no G1 reconciliation, derives no Package 2 Information Concept and does not investigate K11 or approve G1 as a whole. Work stops after persistence of this approved K10 disposition and review of the resulting diff.

## 13. K11 — Behavioural Scope Differences

**Status: RESOLVED — APPROVED: SCOPE DIFFERENCES PRESERVED; UNSUPPORTED FEATURE-TO-BEHAVIOUR ASSOCIATIONS SHALL NOT BE INFERRED**

**Disposition:** Close K11 by preserving legitimate broader/narrower scope relationships and distinguishing established behavioural responsibility from validated Feature association. The five associations in §13.4 are not semantically established; this is an approved finding, not a requirement to discover replacement Features before K11 can close. Preserve the established elements and responsibilities on both sides. Later source reconciliation requires separate authorisation.

The [K11 evidence baseline](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k11--feature-behaviour-and-subject-scope-variants) retains the registered source differences as historical evidence. Approved K1–K10, including the Capability Tier / Architectural Element Identification prerequisite, remain controlling and unchanged.

### 13.1 Approved scope rule

> Shared subject matter, Capability context or clinical purpose does not establish that a Business Function or Service realises, specialises or belongs to a Strategy Feature. Feature association requires semantic evidence. Where such evidence is absent, the Function or Service remains anchored to its established Capability or Feature context without manufacturing an alternative Feature relationship.

> An unestablished Feature association is not itself evidence of a missing Business Function, missing Feature or missing architectural responsibility.

The following distinction SHALL be preserved:

```text
established Function / Service scope
    ≠ validated Feature association
```

Scope is determined by architectural responsibility and behaviour. Shared terminology or a catalogue heading does not validate the association. An established owning Capability context may remain usable while its complete Capability Tier, ancestry and structural Canonical ID remain unresolved under K9. K11 creates no Feature, alias, identifier or Capability ancestry.

### 13.2 Order Administration

**Finding: RESOLVED — broader responsibility / narrower contextual behaviour.**

[Strategy Order Administration](../../02-strategy/capabilities/business-enabling-capabilities.md#5-order-administration-harmonia-core) establishes broader responsibility across diagnostic, medication and procedure requests. [Domain03 Order Behaviour](../../03-business-architecture/behaviours/02-service-administration.md#6-order-administration) and the [Closed-Loop Order Progression Process](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process) use narrower diagnostic/procedural examples. These contextual examples SHALL NOT exclude the independently evidenced medication responsibility or become an exhaustive boundary for Order Administration.

The approved relationship does not authorise:

- inventing a medication-specific Process;
- transplanting diagnostic milestones into medication behaviour;
- treating every procedural outcome as a diagnostic report.

K10 remains controlling for Process-versus-Behaviour abstraction and the distinction between transmission, receipt and acceptance. The K3/K6 Order request, progression, correlation and result-authority boundaries remain unchanged; narrower examples do not transfer clinical performance or originating result authority to Order Administration.

### 13.3 Diagnostic Report Distribution

[Strategy Diagnostic Administration](../../02-strategy/capabilities/business-enabling-capabilities.md#6-diagnostic-administration-harmonia-core) explicitly requires finalised-report distribution through `Diagnostic Report Distribution [FEAT-SA-17]`. [Domain03 Diagnostic Administration](../../03-business-architecture/behaviours/02-service-administration.md#7-diagnostic-administration) additionally establishes preliminary, final and corrected report handling/distribution through its Function and Service behaviour. These responsibilities are compatible.

The finalised-report requirement SHALL NOT be interpreted as an exclusive `final-only` Diagnostic Administration boundary. The broader Domain03 handling SHALL NOT silently redefine the exact scope of `FEAT-SA-17`. Capability responsibility, the stated Strategy Feature requirement and the broader Function / Service scope remain distinguishable.

K2 and K6 remain controlling for finalisation, signing, verification, authorship and originating authority. Handling and distributing reports does not establish diagnostic interpretation, specimen analysis or originating clinical authorship.

### 13.4 Unsupported Service Delivery Feature associations

The following five associations between the [Strategy Service Delivery Feature catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#detailed-capability--feature-catalogue-service-delivery) and current Domain03 Function / Service blocks are **not semantically established by the architecture**. The owner names below apply approved K5 canonical naming; they do not validate the source's legacy numerical tier assertions or establish complete ancestry.

| Strategy Feature (FT) / existing identifier | Domain03 Business Function | Domain03 Business Service | Established owning Capability context | Approved association finding |
| :--- | :--- | :--- | :--- | :--- |
| Medication Administration Tracking / `FEAT-SD-12` | `Correlate Adverse Reaction & Allergy Profile` — allergy / adverse-reaction correlation | `Allergy & Adverse Reaction Query` | [Medication Therapy](../../03-business-architecture/behaviours/03-service-delivery.md#26-medication-therapy-enablement) | Feature association not established. |
| Primary Care Notification Dispatch / `FEAT-SD-02` | `Coordinate Multi-Agency Chronic Care Plan` — chronic-care milestone tracking | `Chronic Care Timeline Query` — multidisciplinary timeline query | [Primary Care](../../03-business-architecture/behaviours/03-service-delivery.md#21-primary-care-enablement) | Feature association not established. |
| Community Care Plan Synchronization / `FEAT-SD-16` | `Bind Community Support Network Context` — support-network association | `Community Care Team Query` — provider-roster query | [Community Care](../../03-business-architecture/behaviours/03-service-delivery.md#28-community-care-enablement) | Feature association not established. |
| Laboratory & Imaging Result Distribution / `FEAT-SD-09` | `Receive Diagnostic Requisition Feed` — requisition ingestion and worklist preparation | `Modality Worklist Distribution` | [Diagnostic Services](../../03-business-architecture/behaviours/03-service-delivery.md#25-diagnostic-services-enablement) | Feature association not established. |
| Diagnostic History Consolidation / `FEAT-SD-10` | `Publish Governed Diagnostic Report` — diagnostic report publication | `Diagnostic Publication Service` | [Diagnostic Services](../../03-business-architecture/behaviours/03-service-delivery.md#25-diagnostic-services-enablement) | Feature association not established. |

For each row, later reconciliation and downstream traceability SHALL:

- preserve the Strategy Feature as FT and preserve its existing Feature identifier;
- preserve the Domain03 Function / Service behaviour and its established owning Capability context;
- make no claim of Feature containment, equivalence, specialisation or realisation;
- assign no alternative Feature and create no Feature or alias to replace the unsupported association;
- infer no Capability ancestry or structural Canonical ID from the disputed association.

The architectural state is **Feature association not established**. This finding does not invalidate either established element, remove the Function / Service responsibility or require a replacement Feature mapping before K11 can close. Approved K5 remains controlling for canonical owner names and unsupported Feature headings, independently of these semantic association findings.

### 13.5 Service Delivery boundary

The frozen [Domain03 care enablement principle](../../03-business-architecture/behaviours/03-service-delivery.md#the-fundamental-care-enablement-principle) establishes Service Delivery as an enabling/contextual perspective. It does not establish that Harmonia performs clinical care. The [Strategy Service Delivery principles](../../02-strategy/capabilities/business-enabling-capabilities.md#architectural-principles-service-delivery), including zero Harmonia-Core Service Delivery, remain unchanged.

Shared clinical subject matter SHALL NOT collapse Service Delivery, Service Administration and Health Service Operations into one responsibility. Clinical activity performed by a care participant/provider, information administration concerning that activity and operational coordination supporting it remain distinct. K4 and K6 remain controlling for the clinical-performance boundary.

### 13.6 Downstream Domain04 constraint

> Domain04 derivation SHALL use established Business Architecture responsibility and behaviour as its semantic anchor. A Strategy Feature label SHALL NOT impose information scope where the Feature-to-behaviour relationship is not established.

Accordingly, later derivation SHALL preserve the following constraints:

- narrow diagnostic/procedural examples must not erase broader Order responsibility, including its independently evidenced medication responsibility;
- medication examples must not narrow the approved K4 medication boundary;
- report handling must preserve K6 authority boundaries and K2 qualification distinctions;
- the five unsupported Feature associations in §13.4 must not determine Package 2 Information Concept scope.

Where Capability, Function or Service responsibility is established but Feature association is not, K9 permits downstream traceability to the established element without manufacturing the missing Feature relationship or structural Canonical ID. Traceability SHALL use the established architectural type, canonical name and existing identifier where one exists, retaining unresolved structural information explicitly. No placeholder structural ID or speculative Feature association is authorised.

### 13.7 Later, separately authorised G1 reconciliation

Later authorised G1 reconciliation SHALL:

- qualify Order diagnostic/procedural examples so they do not exclude medication responsibility;
- clarify the finalised-report requirement versus broader Diagnostic Administration handling, without silently broadening the exact scope of `FEAT-SA-17`;
- remove or correct the unsupported implication that the five identified Domain03 Function / Service blocks realise the cited Strategy Features;
- preserve the Strategy Features as FT and preserve their existing identifiers;
- preserve the Domain03 Functions / Services and their established owning Capability contexts;
- refrain from replacing unsupported Feature associations with speculative alternatives;
- apply K5 canonical owner naming independently;
- preserve K10 decisions and its three separately retained progression/timing uncertainties: Discharge publication timing, To Do progression ordering and Synthetic Task progression ordering.

Approval of these findings does not authorise frozen-source reconciliation during this persistence task. The evidence baseline remains historical evidence; its quotations and fingerprints are not rewritten by this decision.

### 13.8 Closure and stop boundary

K11 is RESOLVED on the approved scope relationships and explicit absence of established Feature associations above. No replacement Feature mapping is required to close K11. Legitimate scope differences remain preserved; established behavioural scope and validated Feature association remain distinct.

This disposition preserves AX-01/AX-04 business meaning, AX-14 semantic distinctions and AX-15 uncertainty by retaining unsupported associations as not established. Approved K1–K10, including the identification prerequisite and K10's unresolved progression/timing findings, remain unchanged. This persistence task changes only this durable G1 review record; it modifies no frozen Domain01–03, Domain04 Foundation or Package 1 source, performs no G1 reconciliation, derives no Package 2 Information Concept and does not investigate K12 or approve G1 as a whole. Work stops after persistence of this approved K11 disposition and review of the resulting diff.

## 14. K12 — Dependency / Service Ownership Mismatch

**Status: RESOLVED — APPROVED: DEPENDENCY-MATRIX SERVICE OWNERSHIP ATTRIBUTION UNSUPPORTED; REMOVE WITHOUT REPLACEMENT**

**Disposition:** Close K12 by preserving the established Clinical Collaboration ownership chain for `Collaboration Clinical Summary Resolution`, rejecting its unsupported dependency-matrix attribution to Patient Clinical Record, and retaining unknown dependency relationships without manufacturing replacements. The approved later correction is removal or explicit withdrawal of the unsupported association without replacement. The remaining uncertainties do not keep K12 open. Source reconciliation requires separate authorisation.

The [K12 evidence baseline](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k12--dependency-owner-mismatch) retains the conflicting source assertions as historical evidence. Approved K1–K11, including the Capability Tier / Architectural Element Identification prerequisite, remain controlling and unchanged.

### 14.1 Established Service ownership

The supported responsibility chain is:

```text
Clinical Collaboration
    → In-Conversation LHR Query Resolution [FEAT-ISE-17]
    → Resolve LHR Query within Collaboration
    → Collaboration Clinical Summary Resolution
```

| Element | Established architectural type and relationship |
| :--- | :--- |
| Clinical Collaboration | Business Enabling Capability; owning responsibility context. Its Capability Tier and complete ancestry remain unresolved under K9. |
| In-Conversation LHR Query Resolution / `FEAT-ISE-17` | Established Feature (FT) within Clinical Collaboration. |
| Resolve LHR Query within Collaboration | Business Function within that responsibility; contextually projects active patient summaries into collaborative clinical discussion channels. |
| Collaboration Clinical Summary Resolution | Business Service exposing that Function; embeds clinical summaries in collaboration feeds. |

[Strategy's Clinical Collaboration definition and Feature catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#7-clinical-collaboration-harmonia-core) establish authorised in-conversation clinical queries and contextual summary presentation to care-team members. [Domain03 Clinical Collaboration Behaviour](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#27-clinical-collaboration) explicitly declares the Function and exposed Service. Their association is supported by responsibility and behaviour, rather than lexical similarity alone.

The [Function / Service ownership rules](../../03-business-architecture/metamodel/business-architecture-metamodel.md#3-function-and-service-semantics) preserve the owning responsibility when Function behaviour is exposed or consumed. No second Patient Clinical Record Function or Service with this identity is established. No alias, duplicate Service or structural Canonical ID SHALL be created to reconcile the inconsistent attribution. Existing `FEAT-ISE-17` remains preserved; unresolved structural identification remains governed by K9.

### 14.2 Unsupported dependency association

The existing [cross-capability dependency row](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) asserts:

```text
Consumer: Clinical Collaboration
    → consumes Collaboration Clinical Summary Resolution
    → from Patient Clinical Record
```

This association is **not supported by the architecture**. Patient Clinical Record is not established as the owner of this Business Service. The row is a dependency-matrix attribution defect producing an inconsistent Service-ownership assertion; it does not establish another Service or shared Service ownership.

The row SHALL NOT be repaired by:

- merely changing the owner to Clinical Collaboration;
- reversing provider and consumer;
- substituting another Patient Clinical Record Service;
- substituting a Health Information Access Service;
- inventing a new source Service;
- inventing a Feature, alias or dependency.

Changing only the owner would leave a self-consumption row in a cross-capability matrix; reversal or substitution would assert a different relationship without evidence. A provider/consumer reversal is not independently established.

The approved later correction is: **remove or explicitly withdraw the unsupported dependency association without replacement.** Preserve the established Service and supported clinical-information-use context, while retaining the precise source-Service dependency as unresolved.

### 14.3 Responsibility boundaries

| Established responsibility context | Preserved responsibility |
| :--- | :--- |
| [Patient Clinical Record](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#21-patient-clinical-record) | Longitudinal clinical information assembly; active clinical record maintenance; durable clinical record preservation; canonical integrated clinical synthesis. |
| [Clinical Collaboration](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#27-clinical-collaboration) | Collaboration spaces and memberships; collaborative discourse; in-conversation contextual query/resolution; contextual projection of clinical summaries into collaboration. |
| [Health Information Access](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#23-health-information-access) | Search and retrieval; access-qualified filtering; query execution context; result projection state. |

These responsibilities may interact without transferring ownership. The [Information Responsibility matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) preserves Patient Clinical Record's canonical integrated synthesis, Clinical Collaboration's contextual projection links and Health Information Access's query/projection context. Information Responsibility does not automatically allocate Service ownership.

The following distinctions SHALL remain explicit:

```text
information ownership
    ≠ Function ownership
    ≠ Service ownership
    ≠ Service consumption
    ≠ information use
    ≠ information presentation
```

Clinical Collaboration's contextual presentation of clinical information does not transfer originating clinical authority or ownership of the underlying clinical record. Patient Clinical Record ownership of canonical integrated synthesis does not confer ownership of collaboration behaviour. Health Information Access relevance does not establish a specific invocation or dependency. Collaborative discourse remains distinct from authoritative clinical record content under K7.

### 14.4 Preserved unresolved relationships

The following relationships remain **UNRESOLVED / NOT ESTABLISHED**, as applicable:

- the precise source Service used by `Resolve LHR Query within Collaboration`;
- the provider of that source Service;
- whether retrieval is direct or mediated;
- the formal consuming Capability / Function mapping for the exposed `Collaboration Clinical Summary Resolution` Service;
- the intended referent of the incorrect dependency row;
- unresolved structural Capability ancestry already governed by K9.

These unresolved relationships do not keep K12 open. They SHALL NOT be manufactured merely to complete the dependency chain. Established intended clinical recipients and information use do not supply an unevidenced formal Capability / Function consumption mapping or identify the source contract. K9's unresolved structural Canonical ID boundary remains unchanged.

### 14.5 Downstream Domain04 constraint

> Service ownership and information ownership SHALL remain independently traceable during Domain04 derivation.

Domain04 SHALL NOT infer:

- information ownership from Service ownership;
- Service ownership from information ownership;
- collaboration authority over source clinical information from contextual presentation;
- Patient Clinical Record ownership of collaboration behaviour from supply of underlying information;
- a second authoritative clinical record from query/projection state.

Established responsibility remains sufficient for downstream derivation under K9 without completing the missing dependency chain. Traceability SHALL preserve the established architectural elements and responsibility boundaries while retaining missing dependency and structural information explicitly. No speculative replacement dependency or structural Canonical ID is authorised.

<a id="146-candidate-g1-architectural-traceability-principle"></a>

### 14.6 AX-17 and G1 traceability evidence

**Standing: APPLICATION OF AUTHORITATIVE AX-17 — ARCHITECTURAL AUTHORITY AND EXPLICIT UNCERTAINTY.**

K9–K13 supplied repeated review evidence for the principle initially recorded here as a candidate. Its authoritative normative home is now [AX-17 — Architectural Authority and Explicit Uncertainty](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty); this record does not maintain a competing local axiom.

The maxim is an operational consequence of AX-17:

> **Traceability must be truthful, not artificially complete.**

| G1 evidence | Supporting progression |
| :--- | :--- |
| K9 | Incomplete Capability hierarchy must not be completed by invented ancestry. |
| K10 | Behaviour and Process traceability must not be completed through invented stage equivalence. |
| K11 | Function / Service traceability must not be completed through unsupported Feature association. |
| K12 | Cross-capability dependency traceability must not be completed through invented Service ownership or consumption. |
| K13 | Downstream representation traceability must not be completed through anticipated FHIR, application or technology mappings. |

This reconciliation changes the principle’s recorded standing, not the approved K9–K13 dispositions.

### 14.7 Later, separately authorised G1 reconciliation

Later authorised G1 reconciliation SHALL:

- remove or explicitly withdraw the unsupported dependency-matrix association without replacement;
- preserve the Clinical Collaboration Function, Service and `FEAT-ISE-17`;
- preserve Patient Clinical Record and Health Information Access responsibilities and existing Services;
- retain the supported clinical-information-use relationship while leaving the precise source-Service dependency unresolved;
- add no replacement dependency without evidence;
- reconcile the historical candidate to authoritative AX-17, retaining the supporting evidence and one normative home.

Approval of K12's disposition and authorisation to reconcile frozen sources remain separate. The evidence baseline remains historical evidence; its quotations and fingerprints are not rewritten by this decision.

### 14.8 Closure and stop boundary

K12 is RESOLVED on the established Clinical Collaboration ownership chain and rejection of the unsupported dependency association. The unknown relationships in §14.4 do not reopen K12 or require a replacement dependency before downstream derivation from established responsibility. The traceability evidence in §14.6 now refers to authoritative AX-17.

This disposition preserves AX-06 information authority, AX-14 semantic distinctions and AX-15 uncertainty by separating Service responsibility from information responsibility and retaining unknown relationships explicitly. Approved K1–K11, including the identification prerequisite, remain unchanged. This persistence task changes only this durable G1 review record; it modifies no frozen Domain01–03, Domain04 Foundation or Package 1 source, performs no G1 reconciliation, derives no Package 2 Information Concept and does not investigate K13 or approve G1 as a whole. Work stops after persistence of this approved K12 disposition and review of the resulting diff.

## 15. K13 — Domain03 Mapping-Layer Wording / Domain04 Boundary

**Status: RESOLVED — APPROVED: DOMAIN04 OWNS SEMANTIC INFORMATION ARCHITECTURE; REALISED REPRESENTATION MAPPINGS ARE DOWNSTREAM**

**Disposition:** Close K13 by establishing the frozen boundary between Domain04 semantic Information Architecture and downstream realised representations. Preserve legitimate conceptual/semantic mappings within Domain04, record the three bounded historical allocation/handoff defects, and require their correction only during separately authorised G1 reconciliation. Specific downstream mappings remain unestablished where the architecture has not allocated them. This decision does not authorise G2.

The [K13 evidence baseline](../../../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md#k13--domain03-mapping-layer-wording) retains the primary source conflict as historical evidence. The related README handoff defects in §15.6 were discovered during the focused investigation. Approved K1–K12, including the identification prerequisite and the traceability evidence now reconciled to AX-17, remain controlling and unchanged.

### 15.1 Approved Domain04 boundary

The frozen [Domain04 Information Architecture Foundation](../metamodel/information-architecture-metamodel.md#1-foundational-semantic-distinction) establishes:

> Information architecture is derived from business meaning and responsibility. Representation does not define meaning.

The following distinction SHALL be preserved:

```text
Business Information Concept
    ≠ Application Data Object
    ≠ FHIR Resource
    ≠ Persistence Entity
    ≠ Java Class
    ≠ Database Table
    ≠ API Payload
```

Domain04 owns conceptual / semantic Information Architecture, including:

- Information Requirements;
- Information Concepts;
- semantic relationships;
- conceptual distinctions and qualifications;
- information-responsibility traceability;
- authority and provenance semantics;
- temporal and lifecycle semantics;
- governed Collections;
- Assemblies;
- Information Views;
- conceptual mappings between business meaning and canonical information meaning.

Domain04 SHALL NOT become an application data-model, interoperability-profile, persistence-schema or implementation-representation catalogue. The [Foundation scope](../README.md#1-scope--architectural-purpose), [modelling guardrails](../guardrails/modelling-guardrails.md), [Information Family guidance](../information-families/README.md#relationship-to-the-domain-04-metamodel--foundational-patterns) and [Domain03 traceability framework](../traceability/domain03-traceability.md#1-the-four-tier-derivation-framework) remain governing sources for these semantic boundaries.

### 15.2 Semantic mapping versus realisation mapping

The following legitimate mappings remain Domain04 responsibilities:

| Semantic / conceptual mapping | Preserved meaning |
| :--- | :--- |
| Business behaviour → Information Requirement | Derive what must be known, distinguished, related or governed from evidenced business behaviour. |
| Information Requirement → Information Concept / Relationship | Establish justified conceptual meaning and semantic relationships. |
| Business responsibility → information responsibility traceability | Trace established upstream responsibility into the conceptual model without inventing or reallocating the Domain03 owner. |
| Source business meaning → canonical conceptual meaning | Preserve evidenced meaning through conceptual interpretation; similarity of names or structures does not establish equivalence. |
| Information Concept → semantic relationship / qualification / distinction | Preserve independently meaningful associations, qualifications and boundaries. |
| Concepts → governed Collection / Assembly / Information View | Establish justified semantic grouping, composition or purpose-specific projection, without specifying an application datatype or exchange payload. |

These are semantic/conceptual mappings. Realisation mappings are downstream. The correction SHALL preserve Domain04's ability to map business meaning into a coherent canonical information model; it SHALL NOT prohibit conceptual modelling merely because it uses the words mapping, projection, assembly or relationship.

### 15.3 Currently established downstream representation boundary

| Downstream domain / responsibility | Established allocation boundary |
| :--- | :--- |
| Domain05 — Application Architecture | Application-level logical/software representations. |
| Domain06 — Integration Architecture | Exchange contracts, interoperability profiles, wire payloads and transformations. |
| Domain07 — Technology Architecture | Physical platform, runtime, database/product and deployment realisation. |
| Persistence realisation — Domain05 / Domain07 | May involve both application and technology responsibilities; SHALL NOT be assigned more precisely than the architecture currently establishes. |

The [Foundation representation distinction](../metamodel/information-architecture-metamodel.md#the-six-way-independence-boundary), [shared physical mapping guardrail](../guardrails/modelling-guardrails.md#guardrail-4-shared-physical-mapping) and [Strategy downstream progression](../../02-strategy/capabilities/ict-foundation-lenses.md#downstream-progression) establish this boundary. They do not allocate actual Package2 objects, profiles, interfaces, storage structures or platforms.

More detailed Domain05–07 allocations SHALL NOT be manufactured merely to complete the architecture. Ordinary application logical models do not become Technology Architecture solely because software implements them. Downstream representation decisions SHALL preserve the established upstream meaning and responsibility distinctions.

### 15.4 FHIR / standards boundary

> Convergence with an existing standard is validation, not derivation.

The architectural progression is:

```text
business meaning
    → Domain04 conceptual meaning
    → independently established downstream representation
    → FHIR where appropriate
```

FHIR Resource/profile structure SHALL NOT be used as the reason to introduce, delimit or merge a Domain04 Information Concept. The [FHIR independence guardrail](../guardrails/modelling-guardrails.md#guardrail-2-fhir-independence) and [no speculative concepts guardrail](../guardrails/modelling-guardrails.md#guardrail-16-no-speculative-concepts) preserve business derivation before standards comparison.

Conceptual independence from FHIR does not require creation of a proprietary application representation. Where downstream architecture legitimately adopts a FHIR-native application representation, it may remain FHIR-native. The requirement is that the standard representation does not define upstream Information Architecture meaning. Application representation and interoperability exposure remain distinguishable responsibilities; all FHIR-related decisions SHALL NOT be forced into one domain merely because they use the same standard.

### 15.5 Primary K13 defect

[Domain03 Business Architecture metamodel, §6](../../03-business-architecture/metamodel/business-architecture-metamodel.md#6-business-information-responsibility) excludes premature translation of business information concepts into FHIR structures, database schemas, payloads and Java classes, then states:

> Such technology mappings belong exclusively to downstream Information Architecture Domain 04 and Application Architecture Domain 05.

This is a **historical architectural-boundary defect**. The reference to Domain04 as an owner of those realised technology mappings conflicts with the frozen Domain04 Foundation. The phrase “Such technology mappings” refers to the preceding representation list; it does not establish an exception for conceptual/semantic mapping.

During later authorised G1 reconciliation, only the defective allocation statement SHALL be replaced, preserving the surrounding Business Architecture responsibility and semantic-purity rules. The replacement SHALL establish that:

- Domain04 formalises conceptual information meaning, semantic relationships and responsibility traceability;
- application representations are downstream;
- exchange/interoperability representations are downstream;
- physical/technology realisation is downstream;
- these realised mappings remain outside Domain04.

The downstream references SHALL follow §15.3 without prematurely specifying persistence or other detailed allocations. The surrounding metamodel SHALL NOT be rewritten unnecessarily.

### 15.6 Related handoff defects

The two directly related instances below express the same historical boundary defect:

| Affected source | Current defective Domain04 handoff | Required later reconciliation |
| :--- | :--- | :--- |
| [Domain03 README — Scope & Architectural Purpose](../../03-business-architecture/README.md#1-scope--architectural-purpose) | Assigns “governed domain models and interchange profiles” to Domain04. | Preserve conceptual information-model derivation in Domain04; remove interchange-profile ownership from Domain04; place interoperability-profile responsibility downstream in Domain06 at the currently established level. |
| [Domain02 README — Relationship to Adjacent Domains](../../02-strategy/README.md#5-relationship-to-adjacent-domains) | Assigns canonical information models, “FHIR profiles”, terminologies and lifecycles to Domain04. | Preserve canonical conceptual information modelling and appropriate semantic governance references; remove FHIR-profile ownership from Domain04; reference downstream interoperability architecture for FHIR profiles. |

These findings authorise no source change during this persistence task. Reconciliation SHALL NOT redesign the domain taxonomy or manufacture downstream mappings to replace the defective handoff wording.

### 15.7 Architectural-control consequence

Retaining the current wording could cause a human or AI developer to treat the following as authorised Domain04 outputs:

- FHIR profiles;
- Java classes;
- DTOs;
- schemas;
- persistence entities;
- API payloads;
- interchange profiles.

This is therefore an **architectural-control defect**, not merely editorial hygiene. Conversely, correction SHALL NOT prohibit legitimate conceptual mapping, semantic relationships, information-responsibility traceability, Collections, Assemblies or Information Views within Domain04.

### 15.8 Package2 / G2 constraint

Package2 Information Families SHALL remain free of:

- FHIR Resource/profile mappings;
- application class/object mappings;
- persistence schemas;
- API payload mappings;
- integration message mappings;
- technology-specific representations.

Package2 SHOULD contain, where justified:

- evidenced Business Architecture context;
- Information Requirements;
- conceptual meanings and distinctions;
- semantic relationships;
- established information responsibility;
- separately qualified authority;
- provenance;
- temporal/lifecycle semantics;
- justified grouping/composition;
- truthful upstream traceability;
- explicit gaps and unresolved relationships.

This K13 decision does not authorise commencement of G2. It derives no Package2 Information Concept or realised representation and does not complete G1 reconciliation.

<a id="159-additional-evidence-for-the-candidate-traceability-principle"></a>

### 15.9 Additional G1 evidence for AX-17

K13 provided additional evidence for the earlier candidate in §14.6, now formalised as authoritative AX-17. Its operational consequence remains:

> **Traceability must be truthful, not artificially complete.**

A Domain04 Information Concept → downstream representation trace SHALL exist only where downstream architecture establishes that representation. A familiar FHIR Resource, anticipated Java class, likely persistence structure or probable API representation SHALL NOT be inserted merely to complete the derivation chain.

AX-17 now governs the authority and uncertainty obligations centrally. Missing downstream representation remains unestablished; this evidence supplies no mapping and does not duplicate the axiom locally.

### 15.10 Later, separately authorised G1 reconciliation

Later authorised G1 reconciliation SHALL:

- correct the Domain03 metamodel §6 allocation statement;
- correct the Domain03 README handoff wording;
- correct the Domain02 README handoff wording;
- preserve Domain04 semantic/conceptual mapping;
- preserve the frozen Domain04 Foundation;
- refrain from prematurely specifying Domain05–07 mappings beyond currently established responsibility;
- create no FHIR/application/persistence mappings;
- reconcile the historical candidate reference to authoritative AX-17.

Approval of K13's disposition and authorisation to reconcile frozen sources remain separate. The evidence baseline remains historical evidence; its quotations and fingerprints are not rewritten by this decision.

### 15.11 Closure and stop boundary

K13 is RESOLVED on the approved semantic/representation boundary and the three bounded historical wording defects above. Unestablished detailed downstream allocations do not reopen this boundary decision and SHALL NOT be completed through inference. Legitimate semantic/conceptual mappings remain within Domain04.

This disposition preserves AX-02/AX-03 standards and native-model boundaries, AX-04 architectural meaning, AX-14 semantic distinctions and AX-15 uncertainty. Approved K1–K12, including the identification prerequisite and the traceability evidence now reconciled to AX-17, remain unchanged. This persistence task changes only this durable G1 review record; it modifies no frozen Domain01–03, Domain04 Foundation, Package1 or downstream architecture source, performs no G1 reconciliation, derives no Package2 Information Concept or realised mapping, and does not commence G2 or approve G1 as a whole. Work stops after persistence of this approved K13 disposition and review of the resulting diff.


## 16. Controlled G1 Reconciliation and Closure

**G1 — CLOSED.**

**Closure date:** 2026-10-07.

**Reconciliation date:** 2026-10-07. K1–K13 remain **RESOLVED — APPROVED**, with the qualifications and unresolved matters recorded in their detailed dispositions.

### 16.1 Reconciliation authority and outcome

The controlled reconciliation applies only K1–K13, [AX-17 — Architectural Authority and Explicit Uncertainty](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty), and established rules necessary to implement those decisions. AX-01/AX-04 business meaning, AX-02/AX-03 standards independence, AX-06 explicit authority, AX-14 distinctions and AX-15 uncertainty remain preserved.

Corrections distinguish required wording changes, scope qualifications, withdrawal of unsupported assertions, necessary cross-references and deliberately preserved uncertainty. The pre-edit bounded set comprised 28 documentation files and one completion report; no unapproved source was added to that set. Each change traces to the matrix below. Unchanged conforming sources are recorded as such; no cosmetic edit was required to account for a decision.

The original evidence baseline remains unchanged historical evidence. Sections 2–15 remain the approved decision record; this section records reconciliation and closure. The reconciled source now represents current architecture. No Package2 Information Family has been derived.

### 16.2 Decision → Change → Verification matrix

| Decision | Approved architectural effect | Authoritative files changed | Verification | Remaining uncertainty |
| :--- | :--- | :--- | :--- | :--- |
| K1 | Referral requests assessment/management/possible assumption; intent, disposition, actual responsibility and Transfer of Care remain distinct. | [Strategy catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#distinction-referral-vs-order); [Service Administration](../../03-business-architecture/behaviours/02-service-administration.md#3-referral-administration); [Processes](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process) | Definitions and scoped specialist pathway reviewed; no universal acceptance, delivery, Appointment, Encounter, Order or transfer consequence introduced. | U1 |
| K2 | Retain process-specific signing requirements without universalising signing/finalisation/authority/legal qualification; consumption does not define originating validity. | [Service Administration](../../03-business-architecture/behaviours/02-service-administration.md#10-clinical-record-administration); [Operations](../../03-business-architecture/behaviours/04-health-service-operations.md#215-discharge-management); [Processes](../../03-business-architecture/processes/business-processes.md); [Foundation lifecycle](../governance/information-lifecycle.md) | Original local signing checkpoints retained; Final versus Final Signed and originating/consumer authority boundaries explicit. | U2, U9 |
| K3 | Restore canonical Order terminology and traceability labels without deriving a new model. | [Foundation traceability §2.3](../traceability/domain03-traceability.md#23-representative-example-3-order-administration) | Four approved Feature names/IDs, four Function/Service pairs, Process and responsibility anchor matched to durable K3. Existing outcome association remains Capability-scoped. No change required — authoritative Domain02/03 Order names already conform. | Full Feature associations are not inferred; retention and conflict-adjudication semantics not established by labels. |
| K4 | Medication orders, dispensing updates and administration-event information remain distinct and broad; Procedure extends beyond surgery, Theatre retains its surgical context. | [Service Administration](../../03-business-architecture/behaviours/02-service-administration.md#8-medication-administration); [Information Responsibility](../../03-business-architecture/information-responsibility/information-responsibility.md) | Bedside/nurse/e-MAR examples made non-exhaustive; Procedural Documentation Ingress broadened while retaining surgical notes. No change required — Theatre's surgical responsibility already conforms. | U3 |
| K5 | Apply approved owner names and remove unsupported Feature typing while preserving valid Functions/Services. | [Service Administration](../../03-business-architecture/behaviours/02-service-administration.md); [Service Delivery](../../03-business-architecture/behaviours/03-service-delivery.md); [Operations](../../03-business-architecture/behaviours/04-health-service-operations.md); [Processes](../../03-business-architecture/processes/business-processes.md); [Dependencies](../../03-business-architecture/dependencies/cross-capability-dependencies.md); [Information Responsibility](../../03-business-architecture/information-responsibility/information-responsibility.md) | Ten Service Delivery owner suffixes removed; Episode & Encounter Administration, Clinical Communication Administration and Clinical Logistics Coordination aligned. Three unsupported Feature headings untyped. All 132 Functions and 130 exposed Services retained. | The three former headings establish no Feature identity or replacement association. |
| K6 | Information concerning performance/outcomes is distinct from the phenomenon and confers neither performance responsibility nor originating authority. | [Healthcare Service family](../information-families/healthcare-service.md); [Foundation example](../patterns/definition-to-accountability.md#21-healthcare-service-concepts--semantics); [Information Responsibility §1.2](../../03-business-architecture/information-responsibility/information-responsibility.md#12-non-transfer-of-ownership-guardrail) | Delivery/Outcome definitions, diagrams and relationship qualifications reviewed; generic clinical-performance/interpretation traces replaced with evidenced information contexts. Diagnostic authority separated from administration; existing concepts, classifications and chain retained. | U4; no universal Delivery/Outcome owner established. |
| K7 | Technical acknowledgement, contextual business disposition, acceptance, review, incorporation, approval, authority and finality remain distinct. | [Strategy catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#distinction-technical-ack-vs-business-ack); [Service Administration](../../03-business-architecture/behaviours/02-service-administration.md#12-clinical-communication-administration); [Order Process](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process) | Universal received/parsed/accepted/filed progression removed; FEAT-SA-28 receipt/read evidence qualified; local Order routing/acceptance checkpoint retained without dispatch equivalence or authority transfer. | U2, U5 |
| K8 | Referrer is a Business Role; Actor, Role, particular participation, local Relationship Role and qualifier remain distinct. | [Roles §3.2](../../03-business-architecture/actors-roles/roles.md#32-referral-direction-is-contextual) | Referrer dual-use corrected; established ReferralSource qualified reference and Practitioner fulfilment preserved. No change required — Package1 Referring Clinician already conforms as a local Relationship Role. | U6 |
| K9 | Preserve identities, responsibilities, 137 FT Features and partial Person Identity chain without unsupported owner tiers, ancestry or structural IDs. | Domain02 [README](../../02-strategy/README.md), [capability index](../../02-strategy/capabilities/index.md), [Business Capability navigation](../../02-strategy/capabilities/business-capabilities.md#downstream-progression), [Business Enabling catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md), [derivation model](../../02-strategy/capability-maps/capability-tier-model.md); Domain03 [README](../../03-business-architecture/README.md), [metamodel §8.6](../../03-business-architecture/metamodel/business-architecture-metamodel.md#86-approved-k9-derivation-and-uncertainty-rules), [Behaviour index](../../03-business-architecture/behaviours/index.md), [Entity](../../03-business-architecture/behaviours/01-entity-management.md), [Administration](../../03-business-architecture/behaviours/02-service-administration.md), [Delivery](../../03-business-architecture/behaviours/03-service-delivery.md), [Operations](../../03-business-architecture/behaviours/04-health-service-operations.md), [Intrinsic](../../03-business-architecture/behaviours/05-intrinsic-enablement.md), [Processes](../../03-business-architecture/processes/business-processes.md); Domain04 [README](../README.md), [traceability](../traceability/domain03-traceability.md), Package1 [Person](../information-families/person-healthcare-subject.md), [Practitioner](../information-families/practitioner.md), [Organisation](../information-families/organisation.md), [Location](../information-families/healthcare-location.md), [Device](../information-families/device.md), [Service](../information-families/healthcare-service.md) | Affected L1/L2 owner assertions removed without CT translation; 137 Feature names/IDs unchanged and unique; no new structural ID or architectural alias allocated. Clinical Qualification Management rename applied; operational badge/access behaviour not assigned FEAT-HSO-23. | U7 |
| K10 | Behaviour/Process representations are complementary; scoped checkpoints do not redefine responsibility or require matching stage counts. | [Metamodel §4.3](../../03-business-architecture/metamodel/business-architecture-metamodel.md#43-behaviour-and-process-consistency); [Behaviour index](../../03-business-architecture/behaviours/index.md); [Service Administration](../../03-business-architecture/behaviours/02-service-administration.md); [Operations](../../03-business-architecture/behaviours/04-health-service-operations.md); [Processes](../../03-business-architecture/processes/business-processes.md); [Dependencies](../../03-business-architecture/dependencies/cross-capability-dependencies.md); [Foundation lifecycle](../governance/information-lifecycle.md) | Referral, Encounter, Order and Clinical Document control cases reviewed; all 16 Process identities and transition displays retained. Scoped applicability and local compound checkpoints qualified. | U8, U9 |
| K11 | Preserve five unestablished Feature associations; medication remains within Order scope; broader diagnostic handling does not redefine finalised-report Feature. | [Service Delivery](../../03-business-architecture/behaviours/03-service-delivery.md); [Service Administration](../../03-business-architecture/behaviours/02-service-administration.md); [Processes](../../03-business-architecture/processes/business-processes.md); [Information Responsibility](../../03-business-architecture/information-responsibility/information-responsibility.md); [Strategy catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md); [Domain03 README](../../03-business-architecture/README.md), [Behaviour index](../../03-business-architecture/behaviours/index.md), [metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#3-function-and-service-semantics) | Five blocks retain Capability-scoped Functions/Services and explicitly state Feature association not established; no alternate Feature. FEAT-SA-17 finalised-report meaning retained. | U10 |
| K12 | Preserve Clinical Collaboration ownership chain; withdraw unsupported consumption from Patient Clinical Record without replacement. | [Dependencies](../../03-business-architecture/dependencies/cross-capability-dependencies.md); [Intrinsic Behaviour](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#27-clinical-collaboration) | Incorrect matrix row removed; FEAT-ISE-17, Function and exposed Service retained. Patient Clinical Record, Clinical Collaboration and Health Information Access responsibilities preserved separately. | U11 |
| K13 | Domain04 owns conceptual meaning/relationships/responsibility traceability; realised representation is independently downstream. | [Domain03 metamodel §6](../../03-business-architecture/metamodel/business-architecture-metamodel.md#6-business-information-responsibility); [Domain03 README](../../03-business-architecture/README.md); [Domain02 README](../../02-strategy/README.md) | Three approved allocation defects corrected, including the related Domain02 physical-schema exclusion. No change required — authoritative Information metamodel §1 already conforms. Convergence is validation, not derivation; no proprietary model prerequisite or FHIR mapping introduced. | U12 |
| AX-17 | Reconcile earlier G1 candidate to the authoritative axiom; retain historical K9–K13 provenance and maxim as its consequence. | This review, §§1, 14.6, 15.9 and 16; [derivation model](../../02-strategy/capability-maps/capability-tier-model.md), [Business metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#86-approved-k9-derivation-and-uncertainty-rules), [Foundation traceability](../traceability/domain03-traceability.md#11-traceability-principles) | No current candidate/unformalised standing remains; competing local formulation removed; central AX-17 unchanged and authoritative. | Missing relationships remain explicit throughout U1–U13. |
| AX-16 | Investigate central-register discrepancy without presuming promotion, omission or supersession. | This review §16.3 only; no central-register or Domain01 correction. | Domain01 definition/cross-references, central register, decision register and available repository history reviewed; evidence does not establish central-register standing or cause. Existing identifiers preserved. | U13 — explicitly unresolved, nonblocking. |

### 16.3 AX-16 investigation and disposition

**Disposition: UNRESOLVED — insufficient authoritative evidence for central-register reconciliation.**

Domain01 currently defines [AX-16 — Operational Activity and Entity State Progress Together](../../01-motivation/principles/architectural-axioms.md#ax-16-operational-activity-and-entity-state-progress-together) and uses it in foundational-requirement cross-references. The central register previously ended at AX-15 and now contains AX-17, but still contains no AX-16 entry. These sources are inconsistent concerning AX-16's central representation.

The current Domain01 definition and cross-references establish its local documented presence and reserved identifier; they do not independently establish promotion into the central authoritative set. The current architectural decision register provides no AX-16 adoption, intentional-absence or supersession decision. Available history introduces the Domain01 definition in commit ed2fec6 (2026-10-05 21:41:03 +1100, “Harmonia Documentation - Domain 02 Strategy”) without a corresponding central-register entry. A search of available central-register history establishes no AX-16 occurrence. History is evidence of repository change, not approval.

The available evidence cannot distinguish an approved axiom missing from the central register, an unpromoted local statement, intentional absence/supersession, or another cause. Cause, central-register standing and correct reconciliation remain unresolved. Domain01 AX-16, central AX-17 and their identifiers are untouched.

This discrepancy does not materially prevent Package2 semantic derivation: the approved G1 responsibilities, semantic boundaries and explicit-uncertainty rule are independently established. No Package2 meaning, ownership or relationship is being inferred from the missing central entry. AX-16 uncertainty therefore does not block closure.

### 16.4 Preserved Uncertainty Register

These entries record architectural knowledge, not newly created backlog tasks or accepted suggestions. Detailed decision-specific qualifications in §§2–15 remain controlling.

| Entry | Preserved unresolved / unestablished knowledge | Derivation constraint and closure assessment |
| :--- | :--- | :--- |
| U1 — K1 | Context-specific acceptance, partial/shared/ongoing responsibility and Transfer of Care effects; no universal Appointment/Encounter/Order consequence. | Derive only evidenced Referral meaning; do not invent a universal care consequence. Nonblocking for that bounded derivation. |
| U2 — K2 | General signing/countersigning, finalisation, verification, authority and legal-qualification criteria, applicability and effects. | Preserve locally established requirements without a universal document lifecycle, signature rule or consumer-originating authority. Nonblocking with qualifications. |
| U3 — K4 | Exact medication and procedure information kinds, qualifiers and detailed cross-context relationships beyond established scope. | Retain orders, dispensing and administration-event distinctions; do not narrow to illustrative settings or invent uniform semantics. Nonblocking for evidenced meaning. |
| U4 — K6 | Activity representation versus assertion/account; complete Delivery/Outcome responsibility traces; outcome-kind semantics; measurement, interpretation, attestation and qualification authority; administrative artefact versus completion disposition. | Preserve existing concepts and contextual responsibility; no universal owner or representation. Nonblocking for evidenced conceptual meaning. |
| U5 — K7 | Business-acceptance and filing criteria; exact acknowledgement dispositions; clinical incorporation meaning, effect and representation. | Receipt/read/review do not imply acceptance, approval, incorporation, finality or authority. No universal Clinical Incorporation concept established. Nonblocking with explicit absence. |
| U6 — K8 | Exhaustive Actor eligibility for Referrer, target qualifier/role, detailed participation/information mappings and context-specific responsibility/authority effects. | Preserve supported Practitioner fulfilment and ReferralSource context; no eligibility matrix or target role invented. Nonblocking. |
| U7 — K9 | Affected Capability Tier, full ancestry, root status, decomposition-entry element identity, structural ID allocation and full Canonical IDs. | Preserve established identities/responsibility and partial Client Administration → Person Identity → Identifier Resolution [FT / FEAT-EM-01]. No intermediate tiers or placeholder IDs; derivation may use established ownership. Nonblocking under approved K9. |
| U8 — K10 | Exact Behaviour/Process stage correspondence where not established, including Encounter applicability/transitions relative to Strategy ON_LEAVE. | Preserve independently established states and obligations without inferred equivalence or stage-count equality. Nonblocking for scoped responsibilities. |
| U9 — K10 | Discharge publication before versus upon departure; To Do dismissal/delegation ordering; Synthetic Task stalled/failed ordering after Completed. | Conflicting timing and ambiguous orderings remain explicitly unresolved at source. Do not invent separate publication events, alternative transition paths, reopening or success/failure equivalence. Nonblocking for semantic derivation; these transition/timing questions remain unavailable as settled input. |
| U10 — K11 | Feature association not established for Medication Administration Tracking / allergy-adverse-reaction behaviour; Primary Care Notification Dispatch / chronic-care milestones; Community Care Plan Synchronization / support-network binding; Laboratory & Imaging Result Distribution / requisition-worklist handling; Diagnostic History Consolidation / diagnostic publication. | Preserve Strategy Features, Domain03 Functions/Services and owning Capabilities. No substitute Feature, alias, realisation or ancestry. Nonblocking when Domain04 traces to established Business responsibility rather than these labels. |
| U11 — K12 | Precise collaboration source Service and provider, direct versus mediated retrieval, formal consuming Capability/Function for Collaboration Clinical Summary Resolution, and incorrect dependency row's intended referent. | Existing Clinical Collaboration ownership is sufficient; the removed dependency has no replacement. Information use/presentation does not establish ownership or consumption. Nonblocking under approved K12. |
| U12 — K13 | Specific downstream application, exchange, FHIR and physical mappings; precise persistence allocation across Domain05/07. | Domain04 conceptual meaning remains independent; no anticipated representation trace. FHIR-native downstream representation is permitted if later architecture establishes it, without a proprietary-model prerequisite. Nonblocking for conceptual derivation. |
| U13 — AX-16 | Central-register standing, cause of absence and correct reconciliation of the Domain01 AX-16 definition. | Preserve identifier and both sources. Package2 can use approved business meaning/responsibility without settling this register discrepancy. Nonblocking; no promotion inferred. |

### 16.5 Cross-architecture consistency and architecture-control validation

Motivation and central axioms are preserved. Strategy retains all 137 Feature identities/names and its narrower finalised-report responsibility. Business Architecture retains all 132 Functions, 130 exposed Services and 16 principal Processes; approved ownership and Role distinctions remain explicit. Foundation and Package1 preserve established concepts/classifications and the Healthcare Service chain while correcting only authorised information/performance and hierarchy assertions. The Information Architecture metamodel already conforms and remains unchanged.

Semantic review of residual matches distinguished valid scoped signing, surgical Theatre context, contextual acknowledgement and historical evidence from defects. No residual approved defect was identified that prevents Package2 semantic derivation. No new hierarchy, Feature association, Service dependency, universal lifecycle or authority was manufactured.

A separate inconsistency discovered during AX-16 investigation remains unchanged: Domain01 records AX-12 as reclassified into Domain05 while the central register still retains AX-12 — Hide Plumbing, Not Information. Its cause and reconciliation are not authorised by K1–K13. It does not affect the approved Package2 business-meaning/responsibility basis and does not block G1. Seven pre-existing broken links in historical Junie artefacts also remain unchanged; none is an authoritative G1 input link.

### 16.6 Verification and closure criteria

- **PASS — architecture suite:** required Maven architecture invocation completed in 17.988 seconds; 90 tests, zero failures, errors or skips.
- **PASS — internal-link regression validation:** local Markdown file/anchor links checked against the saved pre-edit baseline, including inbound references and explicit historical navigation anchors; zero new broken links and zero broken links in the reviewed Domain01–04 sources. Seven unrelated historical Junie link defects preserved.
- **PASS — preservation/identifiers:** 137 Feature names/IDs unchanged and unique; all Function/Service identities, 16 Processes and original transition displays retained; Package1 concepts/classifications and approved K1–K13 dispositions preserved. Existing Canonical-ID/alias grammar examples unchanged; no architectural ID/alias allocated. Full IDs remain unresolved where ancestry is unestablished.
- **PASS — scope:** central register, Domain01, frozen evidence baseline, incoming plans, Information metamodel, production code and other out-of-scope tracked files preserved; incoming staging/index state unchanged.
- **PASS — whitespace:** git diff --check.
- **PASS — final closure record:** all fifteen matrix rows, U1–U13 uncertainty entries, links and task-specific scope validated. The complete task-specific diff is supplied with the execution report; it excludes incoming changes.

All seven closure criteria are satisfied: every K1–K13 decision is accounted for; required corrections are applied; no known contradiction invalidates bounded Package2 semantic derivation; deliberate uncertainty is preserved; AX-17 is authoritative and correctly referenced; required tests and validation pass; G2 has not commenced. **G1 is CLOSED.** Closure does not establish unresolved relationships or authorise any later derivation activity.

**G2 has NOT commenced.** Closure grants no instruction to commence G2. No Package2 Information Concept, Family, FHIR mapping/profile, application/persistence representation or integration contract was created. The [completion report](../../../../.junie/reports/2026-10-07-domain04-package2-g1-reconciliation-closure.md) is execution history and handover, not architectural authority.
