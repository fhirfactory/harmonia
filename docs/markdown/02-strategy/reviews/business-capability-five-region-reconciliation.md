# Domain02 Strategy — Five-Region Business Capability Reconciliation

**Date:** 2026-10-08. **Decision status:** Human architectural decision already approved; this record documents its bounded reconciliation. It does not request or confer a new architectural approval.

The normative Business Capability definitions belong in the [canonical Business Capability model](../capabilities/business-capabilities.md). This review record distinguishes the approved decision, its direct documentary consequences, preserved semantics and unresolved downstream consequences. It is not authority over that model, the central axioms, accepted ADRs or requirements. The [completion report](../../../../.junie/reports/2026-10-08-domain02-business-capability-reconciliation.md) records execution and verification only.

## 1. Authority and Approved Human Decisions

The task instruction records the completed human architectural review. The approved model has **five natural operational regions and eighteen L1 Business Capabilities**:

| Region | Approved name | Established catalogue numbers |
| :--- | :--- | :--- |
| 1 | Care & Health Delivery | 01–08 |
| 2 | Health Information & Digital Health | 09–12 |
| 3 | Research & Innovation | 13 |
| 4 | Health Service Management | 14–16 |
| 5 | Governance & Assurance | 17–18 |

The approved changes are limited to renaming Region 4 and Capability 14, clarifying Capability 06's operational boundary, and appending **17 Health Service Governance** and **18 Health Service Assurance**. Existing numbers 01–16 are retained. Capabilities 03 and 05 remain in Region 1, 10 and 12 in Region 2, 13 in Region 3, and 15 and 16 in Region 4. Governance intrinsic to managing a particular subject remains with that subject's capability.

The materially relevant axioms are [AX-14](../../governance/architectural-axioms.md#ax-14), which preserves governance, management, monitoring, evidence and assurance distinctions, and [AX-17](../../governance/architectural-axioms.md#ax-17), which requires the approved decision to be represented without inventing missing relationships. [AX-08](../../governance/architectural-axioms.md#ax-08) preserves meaningful evidence without equating evidence production with assurance. The approved independence boundary is consistent with unchanged [REQ-FND-005](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity); it establishes no physical execution separation.

Central axioms and accepted ADRs require no change. The [AX-05 reconciliation](ax05-state-responsibility-reconciliation.md) remains **APPROVED / CLOSED**, including its accepted state-responsibility boundaries. This task is outside the convergence/runtime implementation programme and starts no implementation milestone.

## 2. Pre-Edit Affected-Artefact Inventory

All sixteen incoming Markdown artefacts under `docs/markdown/02-strategy/` were searched before editing. The following five canonical artefacts require direct reconciliation; locations describe the incoming source so the inventory remains reviewable after line numbers change.

| Artefact | Directly affected incoming statements | Bounded documentary consequence |
| :--- | :--- | :--- |
| [Business Capability model](../capabilities/business-capabilities.md) | Title; quality-rule preamble and CM-R01 coverage; universal relevance-assignment statement; “The 16 L1 Business Capabilities”; four-region list and ASCII model; Capability 06 scope; Region 4 and Capability 14 scope/role; summary matrix; downstream-progression count. | Represent five regions/eighteen entries; clarify 06; rename and bound 14; define 17/18 and approved distinctions; append matrix/diagram entries; preserve existing classifications and leave new classifications/roles unresolved; demonstrate quality-rule conformance. Restore the full existing Capability 04 name and Region 2 name in summary representations. |
| [Strategy overview](../README.md) | Pedagogical reading-path count; inclusion count/classification statement; Business Capability navigation description. | Update counts/region wording and navigate to this record; qualify the extent of established relevance and downstream derivation. |
| [Capability framework index](../capabilities/index.md) | First diagram box; Business Capability scope/region names; universal relevance-assignment statement; navigation. | Update eighteen/five and Region 4/5 names; distinguish natural regions from tiers and enabling views; retain taxonomy while explicitly leaving 17/18 classification unresolved. |
| [Capability decomposition/derivation model](../capability-maps/capability-tier-model.md) | Stage 1 diagram and structure list; statement of intended Business Enabling coverage in §5. | Update eighteen/five and Region 4/5 names; qualify that existing derivation/coverage does not establish relationships for 17/18. Preserve decomposition rules, examples and downstream semantics. |
| [Strategic value streams](../strategic-views/strategic-value-streams.md) | §4 aggregate Business Capability Mermaid node says “16 L1s across 4 Natural Regions”. | Correct only that node's count and add a scope qualification; create no new stream, stage or capability-to-stream relationship. |

The remaining eleven Strategy artefacts were inspected by search and relevant context:

- `capabilities/business-enabling-capabilities.md`
- `capabilities/enterprise-capabilities.md`
- `capabilities/ict-foundation-lenses.md`
- `capability-maps/index.md`
- `courses-of-action/index.md`
- `courses-of-action/strategic-courses-of-action.md`
- `resources/index.md`
- `resources/strategic-resources.md`
- `strategic-views/index.md`
- `strategic-views/logical-component-responsibilities.md`
- `reviews/ax05-state-responsibility-reconciliation.md`

They contain no additional current count/region/name statement requiring a direct edit. The last record quotes the old Region 4 name in its historical source inventory (§9, incoming `business-capabilities.md` L201); that quotation is preserved as approved review evidence rather than rewritten as a current catalogue. Its approval and closure remain unchanged. No affected incoming link targets a renamed count, region or Capability 14 heading; existing catalogue links remain valid.

## 3. Direct Reconciliation and Preserved Semantics

| Concern | Direct reconciliation | Preserved boundary |
| :--- | :--- | :--- |
| Natural regions | Append Governance & Assurance; rename Enterprise Management to Health Service Management. | Contextual groupings do not define capability tiers, organisational structures, application boundaries or ownership hierarchies. They are distinct from the five Business Enabling contextual views. |
| Capability 06 | Retain clinical outcome/safety monitoring, incident management/reporting/response, infection control, operational response and improvement; remove independent clinical audit/evaluation ownership. | Monitoring for management and improvement remains operational. A clinical audit label alone does not determine its semantics; independent evaluation against governing criteria belongs to 18. Existing enabling role and relevance remain unchanged. |
| Capability 14 | Rename to Health Service Direction & Stewardship; retain executive direction, strategy, priorities, stewardship, organisational decision-making and supporting legal advice. | Governance authority, board governance, policy governance, compliance-requirement/reporting-requirement establishment and checkpoints belong to 17; independent assurance belongs to 18. Legal advice is not automatically relocated. Reference classification remains. |
| Capability 17 | Establish the approved requirements/obligations/authority/constraints/policy/controls/reporting/checkpoint boundary. | Governance establishes the normative envelope, not the means or progression of operational performance. Risk remains contextual; no separate L1 risk capability or risk-management product scope. |
| Capability 18 | Establish independent evaluation using sufficient trustworthy evidence to produce findings, exceptions and conclusions. | Evidence dependency does not compromise independence; control dependency does. Assurance manages its own work, not assignment, delegation, escalation, remediation, Incident Management/Response or progression of its subject. |
| Unaffected capabilities | Preserve names, numbers, region membership, scope, existing classifications and enabling roles of 01–05, 07–13, 15 and 16. | Subject-specific governance in 05, 10, 12 and 15 remains intrinsic to those subjects. Audit/provenance production and operational monitoring do not become independent assurance. |

The canonical model records the approved Governance / Management / Management Monitoring / Independent Assurance definitions, evidence distinctions, risk limits and CM-R01–CM-R05 conformance. These changes improve orthogonality through explicit purpose and control boundaries rather than moving every activity described as governance, compliance, risk, monitoring or audit into Region 5.

## 4. Unresolved Downstream Strategy Consequences

Under AX-17, the following remain unestablished and require a later separately authorised derivation:

| Affected statement or question | Deliberately unresolved disposition |
| :--- | :--- |
| The relevance taxonomy previously implied every Business Capability already had one assigned classification. The approved decision gives none for 17/18. | Preserve the four taxonomy categories and all sixteen existing assignments. Mark 17/18 classification and enabling roles **unresolved**, not a fifth relevance category or a candidate assignment. |
| Existing Business Enabling scope sufficiency and derivation-stage diagrams describe the prior derivation. | Do not claim that existing Features, enabling capabilities or aggregate diagram arrows establish coverage or relationships for 17/18. Existing responsibilities and Features remain unchanged. |
| EC-12 Operational Assurance versus the new Business Capability 18. | No equivalence, semantic change, new Enterprise Capability, composition or realisation decision. Whether EC-12 changes or collaborative realisation across existing capabilities is sufficient remains for subsequent review. |
| Courses of Action, Strategic Value Streams and logical component responsibilities. | No new allocation or relationship to 17/18. Existing streams/stages, courses, resources and component responsibilities remain unchanged. |
| Value-stream representative traceability (§2, incoming lines 92, 145, 198, 255). | Existing `BC-*` labels differ from the canonical Business Capability names: e.g. `BC-14` is “Secure Clinical Communication & Collaboration”, not catalogue 14. The sources do not establish a translation. This pre-existing traceability discrepancy is recorded for human review; no renaming, ordinal substitution or new mapping is inferred. It does not prevent the direct aggregate count correction. |
| Capability identity/decomposition terminology. | Retain the approved Business catalogue's L1 presentation and 01–18 numbering without using them to assign structural Canonical IDs, CT ancestry, roots or ownership. Existing decomposition/metamodel rules remain unchanged. |
| Prior Domain04 finding, incoming `reviews/dokimasia-assurance-architectural-finding.md` §4.2 source table (line 186). | Its mention of former Capability 14 is historical discovery context, not a current catalogue definition. Domain04 is preserved. No Dokimasia ancestor, assurance allocation or downstream information concept is inferred. |

There is no newly exposed contradiction requiring a new decision to make the approved Business Capability reconciliation. Existing traceability and downstream coverage gaps are preserved explicitly. If later derivation encounters a contradiction requiring new authority, AX-17 requires human architectural review rather than inference.

No Business Enabling Capability, Enterprise Capability, EC-12 change, Course of Action, Strategic Value Stream, logical component, Ponos, Mneme, Mnemosyne, Digital Twin, Dokimasia, Pragma, Praxis, Assurance Praxis, application, information model, API, persistence, deployment or technology allocation is derived. The governed Risk Register boundary records the approved contextual allowance only; it creates no information concept or lifecycle.

## 5. Execution Evidence

The completion report identifies exact files inspected/changed/created, direct edits, preserved incoming changes, quality-rule checks, architecture-test results, local-link/anchor checks and task-scoped whitespace validation. SHA-256 comparison against the pre-edit working tree verifies that Domains01/03/04, central axioms, accepted ADRs, REQ-FND-005, AX-05 closure and downstream Strategy definitions receive no task changes.

**Downstream Independent Assurance Strategy derivation is not performed.**
