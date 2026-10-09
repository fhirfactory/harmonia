# Domain 02 Strategy Cleanup — Step 1 Read-Only Assessment

**Date:** 2026-10-09 (Australia/Sydney). **Status:** assessment complete; Step 2 not authorised or executed. **Standing:** execution history and assessment evidence, not architectural authority.

**Repository baseline:** commit `92c5a9212967abf92a1819d70fa85b5a4e569bc7`; worktree clean on entry. Repository content, not conversational memory or historical implementation, determines the assessment.

## 1. Executive Summary

Domain 02 has a usable current capability core, but the complete tree is not yet a clean, consistently authoritative Strategy baseline. The approved five-region/eighteen Business Capability model and Health Service Assurance derivation are explicit and coherent. EC-12 Operational Assurance and EC-14 Service Guardian are distinct, and the accepted Mneme/Mnemosyne state boundary is preserved. The principal defects are concentrated in old value-stream traceability, generic range summaries, stronger cross-cutting responsibility wording and several Courses of Action statements.

| Category | Findings | Meaning |
|---|---:|---|
| A — Mechanical Cleanup | 10 | Exact replacements or scope qualifications are established; no new architectural decision required. |
| B — Architectural Reconciliation Required | 11 | Wording conflicts or responsibility/sufficiency questions require explicit judgement. |
| C — Potentially Stale but Ambiguous | 11 | Intended current reference or meaning lacks authoritative evidence. |
| D — Reviewed and Still Valid | 6 | Specifically questioned material should be preserved. |
| **Total** | **38** | Counts are by register entry, not by individual repeated occurrence. |

Mechanical cleanup is bounded. Complete semantic closure is more substantial than a terminology pass because all four value-stream Business Capability traceability blocks use a superseded model, accompanied by old EC, contextual-view, motivation and axiom labels. Reconciliation can remain bounded to those blocks and the questions recorded here; no capability or component redesign is supported.

The old traceability blocks threaten truthful use of Domain 02 as the upstream authority for R1/R2 Business, Information and Application Architecture. They must not be consumed as settled mappings. The native-model prohibition, blanket evidence wording and clinical-authority attribution also warrant review before being used as normative downstream constraints. This does not invalidate the established current capability responsibilities or approved assurance derivation: downstream work may use those explicit, separately evidenced responsibilities while preserving the recorded gaps under AX-17. A mechanical-only Step 2 would improve readability but would not close the Category B/C architectural questions.

### Assessment method and authority

All **18 current files / 4,601 lines** under `docs/markdown/02-strategy/` were reviewed, including both dated reconciliation records, all indexes, ASCII and Mermaid diagrams, tables, summaries and navigation. Supporting material was limited to authority and clarification needed for this assessment:

- `docs/architectural-axioms.md`: central authority, particularly AX-03/04/05/06/07/08/09/10/11/13/14/15/17.
- Current Domain 01 foundational requirements and external constraints; Driver and Goal catalogues to test value-stream motivation labels; the local axiom view for the AX-16 discrepancy.
- Current Domain 02 Business, Business Enabling and Enterprise Capability catalogues, decomposition/derivation model and approved Health Service Assurance derivation.
- `docs/architecture-decisions.md`: relevant current responsibility decisions, especially ADR-001–007, 013–019; central axioms take precedence.
- Domain 03 metamodel §5 and cross-capability dependency rules for bounded ownership/consumption; the approved assurance behaviour and Role boundaries for later semantic clarification. Its detailed Business elements are comparison evidence, not content to import into Strategy.
- `docs/architecture/execution-model.md`: the approved runtime-AI position and its preserved Twin/assurance boundaries, with implementation-labelled sections treated as downstream descriptions rather than Strategy authority.
- `docs/markdown/04-information-architecture/reviews/package2-g1-review.md` §§11 and 16 only for the approved preservation of established identities/unresolved ancestry and AX-16 standing already referenced by Strategy. No Information Architecture was derived.
- `docs/deferred-document-register.md`: deferral/scope control. Items 01–03 remain resolved; Item 04 remains Deferred and was not used as correction authority.
- The user-linked `.junie/reports/2026-10-08-domain04-package2-g2-semantic-derivation-block1.md` was read as historical handover evidence only. Reports do not approve architecture.

AX-17 governs every proposed treatment: a known current catalogue name does not establish a replacement historical relationship. Missing mappings, ancestry, relevance classifications, criteria and allocations remain missing. Category A corrections do not extend the assurance contribution matrix or allocate EC-14.

## 2. Finding Register

Each entry has exactly one category. Line numbers refer to the reviewed current files. Multiple locations under one ID share one diagnostic concern and are counted once. Category B asks the question; Category C identifies missing evidence; neither is resolved by this report.

### D02-CLN-001 — ICT lens catalogue excludes EC-14

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-001` |
| Category | A |
| File | `docs/markdown/02-strategy/capabilities/ict-foundation-lenses.md` |
| Section | Overview L13; first diagram L18; Downstream Progression L202 |
| Current Content | Generic Enterprise Capability references end at EC-13. |
| Problem | These describe the whole reusable-function catalogue rather than a specifically bounded existing contribution model. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md §§Overview, The 14 Canonical Enterprise Capabilities; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md §Unresolved Relationships and Downstream Boundary. |
| Recommended Treatment | Cleanup: replace the three catalogue ranges with `EC-01 .. EC-14`. Preserve the 18 lenses and existing lens examples; add no EC-14-to-lens or component mapping. |
| Confidence | High |

### D02-CLN-002 — Generic Course of Action capability range is obsolete

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-002` |
| Category | A |
| File | `docs/markdown/02-strategy/courses-of-action/index.md`<br>`docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` |
| Section | Index §2 diagram L42 and quality criterion 2 L52; catalogue overview L7 |
| Current Content | The generic diagram/quality test permits EC-01 through EC-13; the six existing courses are described against that range. |
| Problem | The catalogue now includes EC-14. Existing course-to-EC relationships remain the older bounded set, so expanding their rows is not a mechanical correction. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md §The 14 Canonical Enterprise Capabilities; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md §Unresolved Relationships and Downstream Boundary. |
| Recommended Treatment | Cleanup: use `EC-01 .. EC-14` for the overall catalogue and generic eligibility test. Qualify the existing course model as established EC-01–13 relationships, with EC-14 course relationships unresolved. Preserve every existing course-to-EC row. |
| Confidence | High |

### D02-CLN-003 — Logical model needs an explicit EC-14 scope qualification

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-003` |
| Category | A |
| File | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md` |
| Section | Overview L5; §6 L395–438; §7 diagrams; §8 L542–551 |
| Current Content | The model derives responsibilities from EC-01 through EC-13 and offers an apparently complete current responsibility view without mentioning EC-14. |
| Problem | The retained derivations are valid, but standalone reading conceals the approved new capability and its explicitly unallocated standing. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md L24–32, L230, L321; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md §Unresolved Relationships and Downstream Boundary. |
| Recommended Treatment | Cleanup: retain `EC-01 through EC-13` as the established composition scope and add the exact established fact: EC-14 Service Guardian exists; its strategic logical and application component allocation remains unresolved. Link the approved derivation. Do not add a composition entry, diagram node, seam or component allocation for EC-14. |
| Confidence | High |

### D02-CLN-004 — Generic axiom summaries omit AX-17

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-004` |
| Category | A |
| File | `docs/markdown/02-strategy/courses-of-action/index.md`<br>`docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | Course index §2 diagram L35 and criterion 1 L51; value-stream §4 Motivation node L290 |
| Current Content | Generic summaries stop at `AX-01..16`. |
| Problem | AX-17 is now central authority. A simple range expansion would also conceal the separate AX-16 register discrepancy. |
| Authority | `docs/architectural-axioms.md`, §AX-17; `docs/markdown/01-motivation/principles/architectural-axioms.md` L14. |
| Recommended Treatment | Cleanup: refer to the current authoritative Architectural Axiom register, explicitly including AX-17 as the derivation/uncertainty guardrail. Preserve the unresolved AX-16 standing (D02-CLN-020); establish no new motivational parent relationship. |
| Confidence | High |

### D02-CLN-005 — Canonical EC names drift in otherwise coherent compositions

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-005` |
| Category | A |
| File | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md`<br>`docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md`<br>`docs/markdown/02-strategy/capabilities/ict-foundation-lenses.md` |
| Section | Logical §6 L400–436; COA-02 L60, COA-04 L114, COA-05 L141; lens 03 L117 |
| Current Content | `EC-03 State & Lifecycle Governance`; `EC-04 Information Management & Access`; `EC-09 Event & Subscription Management`; `EC-10 Activity & Execution Coordination`; lens `EC-13 Semantic Governance`. |
| Problem | The identifiers and described responsibility clusters match current catalogue entries, but the displayed names are not canonical. This does not include the incompatible value-stream EC mappings. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md canonical headings and summary matrix. |
| Recommended Treatment | Cleanup at the same identifiers only: `EC-03 Managed State & Lifecycle`; `EC-04 Information Management`; `EC-09 Event & Subscription`; `EC-10 Activity & Execution`; `EC-13 Semantic Governance & Conformance`. Preserve facets such as standards-facing EC-08 and all existing compositions. |
| Confidence | High |

### D02-CLN-006 — Pass B is described as future work despite an existing model

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-006` |
| Category | A |
| File | `docs/markdown/02-strategy/README.md`<br>`docs/markdown/02-strategy/capabilities/enterprise-capabilities.md` |
| Section | README L15; Enterprise methodology L72 and Downstream Progression L316 |
| Current Content | Responsibilities are 'elaborated in Pass B' / 'will be elaborated in Pass B'; Pass B appears under downstream architectural passes. |
| Problem | The current logical model already establishes the stated responsibility definitions. This wording can incorrectly make established Strategy look merely prospective. |
| Authority | docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md Overview and §§3–8. |
| Recommended Treatment | Cleanup: refer in present tense to the existing Strategic Logical Component Responsibility Model and link it. This establishes no overall Domain 02 approval, closure or freeze status. |
| Confidence | High |

### D02-CLN-007 — Seven-component summaries blur six components and one construct

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-007` |
| Category | A |
| File | `docs/markdown/02-strategy/README.md`<br>`docs/markdown/02-strategy/capability-maps/index.md` |
| Section | README navigation L192; capability-map document list L29 |
| Current Content | '7-component responsibility model' / '7 logical component responsibilities'. |
| Problem | The detailed model explicitly passes six logical components and passes Digital Twin only as a construct, failing it as an independent platform component. |
| Authority | docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md Digital Twin boundary evaluation L287–296; summary L312–320. |
| Recommended Treatment | Cleanup: say 'six strategic logical components and the Digital Twin coordination construct' in these summaries. Preserve the seven responsibility profiles and existing anchors; do not create an additional Application Component. |
| Confidence | High |

### D02-CLN-008 — Damaged text diagram borders

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-008` |
| Category | A |
| File | `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`<br>`docs/markdown/02-strategy/capabilities/ict-foundation-lenses.md`<br>`docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md` |
| Section | BEC five-view diagram L52; ICT technology box L30; logical critical-seam diagram L329; logical ASCII view L460, L474, L484, L491 |
| Current Content | Three border lines contain U+FFFD replacement characters; several logical boxes lack their right vertical border. |
| Problem | These are presentation defects. They obscure diagram readability without establishing an alternative responsibility. |
| Authority | docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md intact adjacent diagram borders; docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md adjacent box edges and equivalent Mermaid view. |
| Recommended Treatment | Cleanup: replace corrupt horizontal-border characters with `─` and restore missing aligned `│` box borders. Preserve every label, connector and responsibility relationship. |
| Confidence | High |

### D02-CLN-009 — Feature description spelling defect

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-009` |
| Category | A |
| File | `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` |
| Section | View 4 / Work Allocation & Dispatch / FEAT-HSO-19 L522 |
| Current Content | 'Instatiate operational units of work'. |
| Problem | Unambiguous spelling defect; the Feature name and responsibility are otherwise intact. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md EC-10 'Instantiating operational work items'; docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md FEAT-HSO-19 Work Item Instantiation. |
| Recommended Treatment | Cleanup: `Instatiate` → `Instantiate`. Preserve the Feature identifier and name. |
| Confidence | High |

### D02-CLN-010 — Generic reuse descriptions use stale cross-cutting labels

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-010` |
| Category | A |
| File | `docs/markdown/02-strategy/README.md`<br>`docs/markdown/02-strategy/capabilities/business-capabilities.md`<br>`docs/markdown/02-strategy/capability-maps/capability-tier-model.md`<br>`docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` |
| Section | README L13; Business catalogue downstream L329; tier model rule 4 L25; BEC View 5 diagram L49 |
| Current Content | 'cross-cutting Enterprise Capabilities', 'cross-cutting reusable ICT capabilities', 'cross-cutting, reusable capabilities', 'cross-cutting system capabilities'. |
| Problem | These generic labels describe reuse, not an independently established ownership classification. Their contexts need no new responsibility relationship. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md Overview definition of reusable ICT functionality; docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md §5 Cross-Cutting Responsibility Rule; task's explicit concern/responsibility principle. |
| Recommended Treatment | Cleanup only these descriptive phrases to `reusable Enterprise Capabilities`, `reusable ICT capabilities`, `reusable capabilities`, and `reusable system capabilities`, respectively. Preserve the approved Intrinsic / Shared Enablement view name. Stronger collaborative/allocation statements and COA-06 are separate Category B findings, not blanket replacements. |
| Confidence | High |

### D02-CLN-011 — Collaborative realisation can be read as shared semantic ownership

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-011` |
| Category | B |
| File | `docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`<br>`docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md`<br>`docs/markdown/02-strategy/capabilities/index.md`<br>`docs/markdown/02-strategy/capability-maps/capability-tier-model.md` |
| Section | Enterprise methodology L56–62 and EC-02 L115; logical G1 L22; capability index L86; tier model L118 |
| Current Content | Capabilities are 'inherently' / 'intrinsically cross-cutting'; allocating them to one component is described as architecturally invalid. |
| Problem | Collaborative enforcement across machinery is compatible with bounded semantic responsibility. The prohibition on allocation can also be read as denying a responsibility centre. Terminology alone does not prove duplicated ownership. |
| Authority | `docs/architectural-axioms.md`, AX-07 (Themis owns policy evaluation), AX-04 and AX-14; docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md §5; docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md boundary-test Dependency criterion L60. |
| Recommended Treatment | Adjudication: does each sentence describe participating enforcement/consumption while retaining a bounded defining responsibility, or assert joint ownership? Confirm and state that distinction before changing these normative guardrails. No concrete duplicated Capability or Feature ownership was established by this assessment. |
| Confidence | High |

### D02-CLN-012 — COA-06 name and assurance breadth require deliberate reconciliation

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-012` |
| Category | B |
| File | `docs/markdown/02-strategy/courses-of-action/index.md`<br>`docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` |
| Section | Index COA-06 row L78; detailed COA-06 L149–171; alignment matrix L186 |
| Current Content | Approved name 'Collaborative Cross-Cutting Capability Realisation'; summary lists 'assurance', while the mapped assurance EC is EC-12. |
| Problem | The name is an established Course of Action, not an expendable adjective. Unqualified assurance can now mean independent governed assurance, whose Course of Action relationship is unestablished. |
| Authority | docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md unresolved Course of Action relationships; docs/markdown/02-strategy/capabilities/enterprise-capabilities.md EC-12/EC-14 boundary; docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md §5. |
| Recommended Treatment | Adjudication: retain or explicitly revise the Course of Action name, and determine whether its 'assurance' means only the existing EC-12 operational contribution or has an approved additional relationship. Do not silently add EC-14, delete EC-12, or propagate an inferred rename to the value streams. |
| Confidence | High |

### D02-CLN-013 — Normative Strategy wording binds to downstream machinery

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-013` |
| Category | B |
| File | `docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md`<br>`docs/markdown/02-strategy/capabilities/enterprise-capabilities.md` |
| Section | COA-04 downstream L118; COA-06 downstream L171; EC-10 L183–187 versus methodology L64–69 |
| Current Content | Courses specify Pragma/Petasos and require `themis-api` / `petasos-api` packaging. EC-10 dispatches to 'execution daemons' / 'execution queues', then says it is free of daemons. |
| Problem | The detailed prescriptions and EC-10's internal contradiction blur technology-neutral responsibility with Domain 05 realisation. References to downstream examples can be legitimate; normative package selection is a different commitment. |
| Authority | docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md G2/G4 and conceptual-view disclaimer; `docs/markdown/02-strategy/courses-of-action/index.md` technology-invariance test; docs/markdown/02-strategy/capabilities/enterprise-capabilities.md Zero Technology Leakage; `docs/architectural-axioms.md`, AX-04/AX-17. |
| Recommended Treatment | Adjudication: which statements are explanatory downstream examples and which are intended architectural mandates? Decide their status and documentation home without deriving replacement components, APIs or runtime designs. |
| Confidence | High |

### D02-CLN-014 — COA-01 blanket representation prohibition conflicts with native-model preservation

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-014` |
| Category | B |
| File | `docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` |
| Section | COA-01 L20, L37; alignment matrix L181 |
| Current Content | Internal processing 'must never adopt an external exchange representation as their private operational domain model'; external FHIR structures must not be the sole internal representation. |
| Problem | Read as a prohibition on using native standards models internally, this conflicts with AX-03. Read as a prohibition on allowing external contracts to define private execution semantics, it may be sound. The distinction needs explicit judgement. |
| Authority | `docs/architectural-axioms.md`, AX-02 and AX-03, which retains native standards representations and permits appropriate extensibility; docs/markdown/02-strategy/capabilities/enterprise-capabilities.md EC-08 boundary rule. |
| Recommended Treatment | Adjudication: clarify the boundary between native information representation and Harmonia-private management/execution semantics. Preserve AX-03; do not introduce a parallel model or redesign Calliope. |
| Confidence | High |

### D02-CLN-015 — Blanket provenance/audit capture exceeds meaningful-evidence boundaries

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-015` |
| Category | B |
| File | `docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`<br>`docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` |
| Section | Enterprise methodology L59; EC-07 L154–160; COA-03 L75 and L81 |
| Current Content | Evidence 'across every boundary hop and state transformation'; every action executing an access decision 'must capture semantic provenance'. |
| Problem | The universal wording can promote ordinary machinery and every policy evaluation to evidence. Central axioms distinguish evidence significance, transient diagnostics and policy-governed recording. Neither recorded provenance nor audit information constitutes an assurance conclusion. |
| Authority | `docs/architectural-axioms.md`, AX-07 ('not every authorization evaluation' belongs in audit), AX-08/AX-09; `docs/architecture-decisions.md` ADR-016; docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md assurance evidence principles. |
| Recommended Treatment | Adjudication: identify which actions require meaningful durable evidence and which supply transient diagnostics or operational facts. Determine whether 'capture' is intended as durable evidence or contextual attribution; retain evidence/assurance separation. |
| Confidence | High |

### D02-CLN-016 — Iris exclusion overstates Mneme/Themis clinical authority

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-016` |
| Category | B |
| File | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md` |
| Section | Iris anti-responsibilities L225, read with L215–235 |
| Current Content | Clinical identity, clinical authority and state validation are parenthetically 'owned by Mneme / Themis'. |
| Problem | Iris's exclusion is valid, but the combined owner attribution can imply clinical-source authority for active access or policy machinery and omit distinct semantic/durable domains. |
| Authority | `docs/architectural-axioms.md`, AX-05/AX-06/AX-07; `docs/architecture-decisions.md` ADR-007; docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md source-attribution and care-enablement boundaries. |
| Recommended Treatment | Adjudication: identify the intended scope of 'clinical authority' and 'state validation' in this exclusion. Preserve Iris non-authority and the distinction among source clinical authority, governed access/coordination, policy evaluation and durable state establishment. Do not invent a replacement clinical authority allocation. |
| Confidence | High |

### D02-CLN-017 — Unqualified release/Feature coverage sufficiency needs a current scope statement

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-017` |
| Category | B |
| File | `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`<br>`docs/markdown/02-strategy/README.md` |
| Section | BEC principles L14; Scope L59–62; assurance addition L18 and L664–763; README navigation L197 |
| Current Content | 'Comprehensive capability and Feature coverage' sufficient for 1.x/2.x; overview calls the five-view catalogue 'complete'. |
| Problem | Three assurance capabilities have no approved Feature decomposition, contextual-view placement or relevance classification, and BC-17 enablement remains unresolved. Intentional absence is valid; a blanket completeness claim may conceal it. |
| Authority | docs/markdown/02-strategy/capabilities/business-capabilities.md relevance and downstream boundaries; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md unresolved relationships; docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md L18 and L668. |
| Recommended Treatment | Adjudication: what exact release/responsibility scope does the sufficiency assertion certify after BC-18/EC-14? Bound the assertion to established material and explicit gaps. Do not fill the gaps or require assurance Features merely for completeness. |
| Confidence | High |

### D02-CLN-018 — Universal value-stream justification conflicts with explicit absent relationships

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-018` |
| Category | B |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | §4 Traceability Principles L305 versus scope note L300 |
| Current Content | 'Every capability in Harmonia exists to serve one or more Strategic Value Streams.' |
| Problem | The same document says BC-17/18 and assurance-related stream relationships are unresolved. The Business catalogue also includes Reference/Adjacent enterprise capabilities beyond direct Harmonia execution. |
| Authority | docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md unresolved Value Stream/stage relationships; docs/markdown/02-strategy/capabilities/business-capabilities.md enablement versus ownership and relevance taxonomy; `docs/architectural-axioms.md`, AX-17. |
| Recommended Treatment | Adjudication: is this a modelling objective for eventual Harmonia capability traceability or an assertion of established current relationships? Define its scope/status without assigning BC-18 or EC-14 to a stream or introducing a new stream. |
| Confidence | High |

### D02-CLN-019 — Ponos's standalone assurance-boundary wording requires review

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-019` |
| Category | B |
| File | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md` |
| Section | Ponos profile L126–151; Ponos/Twin seam L363–368; composition L411–416 |
| Current Content | Ponos consumes EC-12 and handles progression/failure; no local distinction from independently governed assurance is stated. |
| Problem | The association is valid (D02-CLN-033). After EC-14, readers may overread 'Operational Assurance' as independent conclusion authority. No text here actually grants such authority; adequacy of the local boundary wording needs review. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md EC-12/EC-14 boundary; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md preserved responsibility/allocation limits; docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md REQ-FND-005; docs/markdown/03-business-architecture/behaviours/health-service-assurance.md §7.2. |
| Recommended Treatment | Adjudication: should the Ponos profile explicitly identify execution integrity and Management Monitoring while excluding subject control of independent assurance? The exact clarification must preserve EC-12 and leave EC-14/assurance execution allocation unresolved. This is not a finding that Ponos's EC-12 dependency is invalid. |
| Confidence | Medium |

### D02-CLN-020 — AX-16's central-register standing remains unresolved

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-020` |
| Category | B |
| File | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md`<br>`docs/markdown/02-strategy/strategic-views/strategic-value-streams.md`<br>`docs/markdown/02-strategy/courses-of-action/index.md`<br>`docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` |
| Section | Logical G3/profile/seams L37, L134, L361; VS-03 L158, L187, L196; COA-04/05 and index grounding |
| Current Content | Strategy cites AX-16 as an axiom governing entity/activity coordination. |
| Problem | Domain 01 has an AX-16 Principle, but the highest-authority central register has no AX-16 entry. Coordination is also established by REQ-FND-002; that does not resolve the axiom-register discrepancy. |
| Authority | `docs/architectural-axioms.md`, actual central-register entries; `docs/markdown/01-motivation/principles/architectural-axioms.md` §AX-16; docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md REQ-FND-002. |
| Recommended Treatment | Adjudication in a separate authority-reconciliation task: establish AX-16's standing. Record the limitation in cleanup; do not add/remove an axiom, renumber references, weaken established coordination or interpret the historical report as approval. |
| Confidence | High |

### D02-CLN-021 — Technical acknowledgement examples overstate durability semantics

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-021` |
| Category | B |
| File | `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`<br>`docs/markdown/02-strategy/capability-maps/capability-tier-model.md` |
| Section | BEC Service Administration distinction L260–265; tier Order example L201–203 |
| Current Content | Technical ACK, including HTTP 200/202, confirms durable acceptance into an integration queue; another description says it confirms only receipt of bits. |
| Problem | Technical versus business acceptance is valid. Generic response codes do not universally prove queue durability; local positive acceptance is governed by the actual durable boundary, which may be committed state or durable work transfer. |
| Authority | docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md REQ-FND-001; `docs/architecture-decisions.md` ADR-018 work-acceptance/state-commit distinction; `docs/architectural-axioms.md`, AX-14/AX-15. |
| Recommended Treatment | Adjudication: state what the applicable acknowledgement contract establishes without universalising response-code or queue semantics. Preserve technical ACK versus business disposition and explicit external-outcome uncertainty; design no protocol/API. |
| Confidence | High |

### D02-CLN-022 — VS-01 Business Capability mappings use a superseded model

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-022` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | VS-01 / Representative Strategic Traceability L92 |
| Current Content | BC-01, 02, 03, 04, 08 and 12 use patient identification, document ingress, longitudinal record, terminology, query and diagnostic-integration names. |
| Problem | Each identifier now denotes a different canonical Business Capability. The repository establishes no old-to-current mapping; substituting the current name at the same ordinal would change the claimed relationship. |
| Authority | docs/markdown/02-strategy/capabilities/business-capabilities.md current 18-entry model; `docs/architectural-axioms.md`, AX-17. The dated five-region review corroborates historical discovery only. |
| Recommended Treatment | Preserve uncertainty: `Current mapping unresolved`. Obtain an approved capability-to-stream mapping or explicitly approved treatment of the historical block before editing. Do not renumber, delete dependencies or infer Feature replacements. |
| Confidence | High |

### D02-CLN-023 — VS-02 Business Capability mappings use a superseded model

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-023` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | VS-02 / Representative Strategic Traceability L145 |
| Current Content | BC-02, 03, 06, 07, 08, 11 and 12 use document ingress, longitudinal record, security, audit, query, membrane and diagnostic-integration names. |
| Problem | Each identifier now denotes a different canonical Business Capability. The repository establishes no old-to-current mapping; substituting the current name at the same ordinal would change the claimed relationship. |
| Authority | docs/markdown/02-strategy/capabilities/business-capabilities.md current 18-entry model; `docs/architectural-axioms.md`, AX-17. The dated five-region review corroborates historical discovery only. |
| Recommended Treatment | Preserve uncertainty: `Current mapping unresolved`. Obtain an approved capability-to-stream mapping or explicitly approved treatment of the historical block before editing. Do not renumber, delete dependencies or infer Feature replacements. |
| Confidence | High |

### D02-CLN-024 — VS-03 Business Capability mappings use a superseded model

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-024` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | VS-03 / Representative Strategic Traceability L198 |
| Current Content | BC-05, 09, 10, 14, 15 and 16 use facility/resource management, coordination, referral/order, collaboration, activity and entity-state progression names. |
| Problem | Each identifier now denotes a different canonical Business Capability. The repository establishes no old-to-current mapping; substituting the current name at the same ordinal would change the claimed relationship. |
| Authority | docs/markdown/02-strategy/capabilities/business-capabilities.md current 18-entry model; `docs/architectural-axioms.md`, AX-17. The dated five-region review corroborates historical discovery only. |
| Recommended Treatment | Preserve uncertainty: `Current mapping unresolved`. Obtain an approved capability-to-stream mapping or explicitly approved treatment of the historical block before editing. Do not renumber, delete dependencies or infer Feature replacements. |
| Confidence | High |

### D02-CLN-025 — VS-04 Business Capability mappings use a superseded model

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-025` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | VS-04 / Representative Strategic Traceability L255 |
| Current Content | BC-01, 03, 04, 13 and 14 use patient identification, longitudinal record, terminology, presentation and collaboration names. |
| Problem | Each identifier now denotes a different canonical Business Capability. The repository establishes no old-to-current mapping; substituting the current name at the same ordinal would change the claimed relationship. |
| Authority | docs/markdown/02-strategy/capabilities/business-capabilities.md current 18-entry model; `docs/architectural-axioms.md`, AX-17. The dated five-region review corroborates historical discovery only. |
| Recommended Treatment | Preserve uncertainty: `Current mapping unresolved`. Obtain an approved capability-to-stream mapping or explicitly approved treatment of the historical block before editing. Do not renumber, delete dependencies or infer Feature replacements. |
| Confidence | High |

### D02-CLN-026 — Value-stream EC labels include incompatible responsibilities

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-026` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | Representative traceability L94, L147, L200, L257 |
| Current Content | Examples: EC-03 'Semantic Normalisation'; EC-05 'Entity State Coordination'; EC-09 'Activity Lifecycle Management'; EC-10 'Task Envelope Management'. Other labels are narrower functions of current ECs. |
| Problem | The first group conflicts with current EC scope or imports a downstream construct. Functionally compatible labels (identifier resolution, longitudinal information, terminology, collaboration) do not supply an approved translation for the whole historical model. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md canonical definitions/matrix; `docs/architectural-axioms.md`, AX-17. |
| Recommended Treatment | `Current mapping unresolved` for incompatible references and the intended reconstructed contribution sets. Preserve correctly named EC-02, EC-06, EC-07 and EC-12. Catalogue names are available for comparison, but do not infer stream membership from name similarity. |
| Confidence | High |

### D02-CLN-027 — Value-stream contextual-view mappings use obsolete view meanings

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-027` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | Representative Business Enabling Contexts L93, L146, L199, L256 |
| Current Content | Views 1–5 are labelled Ingress & Processing, Information Store & Query, Security & Audit, Cross-Enterprise Interoperability, Operational Activity & Workflow. |
| Problem | Current views 1–5 mean Entity Management, Service Administration, Service Delivery, Health Service Operations and Intrinsic / Shared Enablement. These are semantic replacements of a model, not display-name changes. |
| Authority | docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md The Five Contextual Views; `docs/architectural-axioms.md`, AX-17. |
| Recommended Treatment | `Current mapping unresolved`. Missing evidence: an approved translation or current representative stream-to-enabling-context relationship. Do not carry the old ordinals into the new views. |
| Confidence | High |

### D02-CLN-028 — Value-stream Driver/Goal identifiers lack current authoritative aliases

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-028` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | Representative Drivers & Goals L91, L144, L197, L254 |
| Current Content | DRV-01 Fragmented Care Delivery, DRV-02 Semantic Heterogeneity, DRV-03 Regulatory Compliance, DRV-05 Operational Inefficiency; GOAL-01 Unified Longitudinal View, GOAL-02 Semantic Interoperability, GOAL-03 Security & Privacy Governance, GOAL-05 Information Longevity, GOAL-06 Operational Efficiency. |
| Problem | Current Domain 01 defines seven named Drivers and six named Goals with different numbered meanings. No alias or translation for these DRV-/GOAL- labels was established by the consulted authority. |
| Authority | `docs/markdown/01-motivation/drivers-assessments/drivers.md`; `docs/markdown/01-motivation/goals-outcomes/strategic-goals.md`; `docs/architectural-axioms.md`, AX-17. |
| Recommended Treatment | `Current mapping unresolved`. Obtain documented motivation relationships and any historical aliases. DRV-04/GOAL-04 resemble current identity/safety concerns, but resemblance is not an identifier migration decision. |
| Confidence | High |

### D02-CLN-029 — Value-stream axiom labels describe earlier meanings

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-029` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | Motivational Axioms L90, L143, L196, L253 |
| Current Content | AX-04 Semantic Preservation; AX-06 Provenance & Attribution; AX-09 Non-Destructive Evolution; AX-10 Asynchronous Operational Progression; AX-11 Component Responsibility; AX-14 Jurisdictional Alignment. |
| Problem | These meanings differ from central axiom definitions. Correct modern titles are known, but the axioms intended to ground the original claims are not established by replacing titles at the old IDs. |
| Authority | `docs/architectural-axioms.md`, corresponding central AX-04/06/09/10/11/14 entries. |
| Recommended Treatment | `Current mapping unresolved` for the intended motivational grounding. Validate each relationship against current axioms before editorial repair. Keep compatible abbreviated references distinguishable from the mismatched meanings; do not treat AX-17 as a new stakeholder motivation. |
| Confidence | High |

### D02-CLN-030 — Strategic resources cite an obsolete constraint identifier range

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-030` |
| Category | C |
| File | `docs/markdown/02-strategy/resources/strategic-resources.md` |
| Section | Candidate summary L23; Candidate 1 disposition L108 |
| Current Content | Domain 01 constraints are referenced as CON-01..CON-11. |
| Problem | Current authority has three general categories CST-EXT-001..003 and jurisdictional instances. No eleven-item old-to-new crosswalk is documented here. |
| Authority | `docs/markdown/01-motivation/requirements-constraints/external-constraints.md`; master requirement catalogue; `docs/architectural-axioms.md`, AX-17. |
| Recommended Treatment | `Current mapping unresolved` for individual old CON references. A future edit may refer to the current constraint catalogue and applicable category only after confirming the intended constraint relationship; do not manufacture eleven-to-three traceability. |
| Confidence | High |

### D02-CLN-031 — Australian resource scope and wider platform scope are not clearly separated

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-031` |
| Category | C |
| File | `docs/markdown/02-strategy/resources/strategic-resources.md` |
| Section | Candidate 1 scope note L107; admitted SR-02/SR-03; compare Enterprise EC-07 L158 |
| Current Content | HIPAA and GDPR are excluded as outside Harmonia's 'canonical Australian healthcare scope'. |
| Problem | Domain 01's applicable-jurisdiction constraint explicitly includes non-Australian examples and the Enterprise catalogue uses HIPAA/GDPR. Australian strategic-resource instances can be valid; exclusion from all Harmonia scope is a broader assertion with no established supporting decision in the consulted authority. |
| Authority | `docs/markdown/01-motivation/requirements-constraints/external-constraints.md` CST-EXT-001; docs/markdown/02-strategy/capabilities/enterprise-capabilities.md EC-07; `docs/architectural-axioms.md`, AX-01/AX-02/AX-17. |
| Recommended Treatment | Preserve the admitted Australian resources. Missing evidence: whether the exclusion is an Australian deployment/resource-selection qualification or a platform-wide scope decision. Adjudicate that scope separately; invent no new jurisdictional resource. |
| Confidence | Medium |

### D02-CLN-032 — Digital Twin 'active threads' is an uncertain historical shorthand

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-032` |
| Category | C |
| File | `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` |
| Section | VS-03 Decoupling Note L192 |
| Current Content | Execution examples include 'Digital Twin active threads'. |
| Problem | The logical model defines a coordination construct, not a permanent thread or independent execution engine. The phrase could refer to permissible temporary machinery rather than a required Twin identity; the intended assertion is unclear. |
| Authority | docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md Digital Twin definition/candidate test/negative constraints; `docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` COA-05 demand-driven lifecycle. |
| Recommended Treatment | Preserve the downstream-below-value-stream rule. Missing evidence: whether 'active threads' is merely a historical runtime example or a claimed Twin realisation. Do not derive a thread/actor model or replace it with a newly allocated execution mechanism. |
| Confidence | Medium |

### D02-CLN-033 — EC-12 and its Ponos association remain architecturally valid

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-033` |
| Category | D |
| File | `docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`<br>`docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md` |
| Section | EC-12 L198–209 and summary L267; Ponos dependency L148, seam L366, composition L415 |
| Current Content | Concurrency integrity, resilience, duplicate suppression, durable acceptance, exception/recovery and telemetry; Ponos consumes EC-12. |
| Problem | Investigated because its name and Ponos relationship might be mistaken for independent governed assurance. No deletion or EC-14 substitution is justified. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md explicit EC-12/EC-14 boundary; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md approved contribution matrix; docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md REQ-FND-001/002/004/005. |
| Recommended Treatment | Preserve EC-12, Ponos execution responsibility and the association. EC-12's direct Governed Assurance contribution is limited to generic operational/processing assurance and does not give Ponos independent conclusion authority. Address standalone wording only through D02-CLN-019. |
| Confidence | High |

### D02-CLN-034 — Approved Health Service Assurance semantics remain internally consistent

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-034` |
| Category | D |
| File | `docs/markdown/02-strategy/capabilities/business-capabilities.md`<br>`docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`<br>`docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`<br>`docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md` |
| Section | BC-18 and boundaries; three assurance capabilities/principles; EC-12/EC-14; approved matrix |
| Current Content | BC-18 → Assurance Design / Assurance Criteria Management / Governed Assurance → collaborative contributions including EC-02 and EC-14. |
| Problem | Specifically checked for governance/monitoring/evidence conflation, subject control, evidence insufficiency, recursion, clinical authority and financial governance. No conflicting assertion was found in this approved derivation. |
| Authority | docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md REQ-FND-005; `docs/architectural-axioms.md`, AX-14/AX-17; docs/markdown/03-business-architecture/behaviours/health-service-assurance.md approved semantic clarifications, §7.1/§7.2. |
| Recommended Treatment | Preserve canonical definitions, independent authority, contextual/temporal evidence, assessment/adjudication distinction and explicit insufficiency. Preserve unresolved component allocation. No Domain 03 Functions, Processes, Services, Roles or Interactions need to be copied into Strategy. |
| Confidence | High |

### D02-CLN-035 — State separation and six component seams remain valid

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-035` |
| Category | D |
| File | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md`<br>`docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` |
| Section | Logical G2–G4; component profiles; six seams; conceptual diagrams; COA-02 |
| Current Content | Mneme manages active use; Mnemosyne establishes authoritative durable state/version; Ponos progresses activity; Pylai governs external contracts; Calliope governs meaning; Iris presents without business authority. |
| Problem | Current wording does not reintroduce the previously corrected AX-05 denial of durable-state authority. Qualified responsibility authority and six-point boundary tests are conceptual, not Domain 05 allocations. |
| Authority | `docs/architectural-axioms.md`, AX-05/AX-06/AX-07/AX-13; `docs/architecture-decisions.md` ADR-004/007/018/019; docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md conceptual-view disclaimer. |
| Recommended Treatment | Preserve the accepted state-responsibility distinctions and seams. Mnemosyne preservation-only shorthand remains a compatible facet when read with its full definition. Specific naming, assurance-context and Iris-attribution findings do not invalidate the whole model. |
| Confidence | High |

### D02-CLN-036 — Digital Twin remains an entity-centred coordination construct

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-036` |
| Category | D |
| File | `docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md`<br>`docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md` |
| Section | Logical Component 7 L241–306, summary L320 and seams; COA-05 |
| Current Content | Composite entity context, candidate-Twin test, demand-driven coordination across Mneme/Ponos; not an independent platform component. |
| Problem | Tested against later assurance and runtime-AI material. Neither changes Twin identity, makes representation alone sufficient, or establishes an EC-14 allocation. |
| Authority | docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md canonical definition; `docs/architecture/execution-model.md` §6 approved runtime-AI position; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md allocation boundary; `docs/architectural-axioms.md`, AX-05/AX-17. |
| Recommended Treatment | Preserve definition, composite context, candidate test and constraints. Invent no Dokimasia Twin classification, permanent actor/thread, independent engine or AI requirement. |
| Confidence | High |

### D02-CLN-037 — Dated reconciliation records retain truthful historical standing

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-037` |
| Category | D |
| File | `docs/markdown/02-strategy/reviews/ax05-state-responsibility-reconciliation.md`<br>`docs/markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md` |
| Section | AX-05 status/approval/scope and source inventory; five-region pre-edit inventory/deferred consequences |
| Current Content | Earlier counts, EC-01..13 quotations, old Region 4, no assurance derivation in that task, and previously open assurance questions. |
| Problem | These are explicitly dated incoming-state/task-boundary records. Later BC-18/EC-14 approval supersedes their open questions as current architecture, but does not make the historical task outcomes false. |
| Authority | docs/markdown/02-strategy/capabilities/business-capabilities.md current model; docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md current derivation; `docs/architectural-axioms.md`, AX-17; repository AGENTS §6 historical artefact hierarchy. |
| Recommended Treatment | Preserve historical quotations, inventories, test counts and task-scoped APPROVED/CLOSED status. Use the current canonical catalogue for present meaning. The apparent relative link at AX-05 record L463 is inside a literal code quotation, not a broken active navigation link. |
| Confidence | High |

### D02-CLN-038 — Representative reuse and established counts remain valid

| Field | Assessment |
|---|---|
| Finding ID | `D02-CLN-038` |
| Category | D |
| File | `docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`<br>`docs/markdown/02-strategy/capability-maps/capability-tier-model.md`<br>`docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` |
| Section | Three composition examples; established Feature catalogue; ICT/Service Delivery scope references |
| Current Content | Person identifiers, closed-loop orders and work dispatch compose bounded EC contributions; 137 established Features; 16 Service Delivery contexts. |
| Problem | These are representative compositions, not joint ownership or exhaustive assurance derivation. EC-12 denotes deterministic identifier/concurrency integrity, durable handoffs and resilient dispatch; it does not authorise demographic MPI merge or independent assurance conclusions. |
| Authority | docs/markdown/02-strategy/capabilities/enterprise-capabilities.md named scopes and representative compositions; docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md FT definitions, non-MPI/roadmap boundaries and approved assurance addition; docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md REQ-FND-003. |
| Recommended Treatment | Preserve existing Feature names/identifiers and representative relationships. The verified counts are 31 EM + 29 SA + 20 SD + 29 HSO + 28 ISE = 137; 16 Service Delivery contexts and 18 ICT lenses are not obsolete Business/Enterprise Capability counts. No EC-14 addition to these examples is required. |
| Confidence | High |


## 3. EC-12 / EC-14 Analysis

### Responsibilities and established relationships

| Concept | Established responsibility | Boundary |
|---|---|---|
| EC-12 Operational Assurance | Concurrency integrity, resilience, duplicate detection/idempotence, durable ingress acceptance, exception handling, recovery, dead-letter isolation and operational telemetry. | Operational integrity does not itself establish independent assurance disposition, assurance criteria applicability, assessment/adjudication or findings/conclusions. |
| Operational/activity monitoring | Observes health, activity state and progress, including operational metrics, timeouts and failures. | An observation may support a decision or evidence association; observation alone is not assurance. |
| Management Monitoring | Supervises operational progression so those responsible for the subject can manage, recover, escalate or remediate it. | Management authority over the subject differs from independent assurance authority. |
| Ponos | Defines/progresses activity execution, dispatches and supervises work, handles execution failure/recovery and consumes EC-12. | Its strategic execution responsibility establishes no independent assurance conclusion authority and no allocation of EC-14. |
| Governed Assurance | Independently evaluates whether governed behaviour/state/outcomes satisfy applicable expectations, using criteria and trustworthy contextual evidence. | Does not manage, assign, delegate, remediate or operationally escalate the subject activity; cannot infer failure from insufficient evidence. |
| EC-14 Service Guardian | Assurance disposition, assurance-specific criteria applicability, contextual evidentiary association, assessment, adjudication, independent findings and conclusions. | A reusable assurance-specific contribution, not sole realisation, a runtime engine or an allocated component. Does not own governing requirements, source information or subject activity. |
| Independent assurance | Control of assurance progression and conclusion remains independent of the subject activity. | Evidence dependency is permitted; control dependency is not. Physical or organisational separation is not automatically required. |

The approved Strategy matrix differentiates the relevant contributions precisely:

| Enterprise Capability | Assurance Design | Assurance Criteria Management | Governed Assurance |
|---|---|---|---|
| EC-02 Context Management | Supporting | Supporting | Direct |
| EC-12 Operational Assurance | Supporting | Supporting | Direct, limited to generic operational/processing assurance contribution |
| EC-14 Service Guardian | Direct | Direct | Direct |

These are three rows from the approved fourteen-capability first-pass matrix, not a replacement matrix. Direct contribution does not mean ownership or sole realisation. EC-02 establishes/propagates context without acquiring assurance-specific meaning. EC-12 retains its operational scope. EC-14 supplies the distinguishing assurance semantics. EC-10's direct contribution to Governed Assurance establishes no Ponos allocation.

Authority: `docs/markdown/02-strategy/capabilities/enterprise-capabilities.md`, EC-12, EC-12 / EC-14 Boundary and EC-14; `docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md`, Collaborative Enterprise Capability Contribution Matrix and Preserved Semantic Boundaries; `docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md`, REQ-FND-005.

### Answers to the EC-12/Ponos questions

1. **Current meaning:** EC-12 is the established platform operational-integrity capability described above. Its name does not make it the complete Harmonia assurance model.
2. **Meaning of the Ponos association:** Ponos consumes operational integrity in activity execution and supervision, including failure/recovery and operational monitoring. This supports Management Monitoring and dependable execution. It does not claim independent conclusions about its subject activity.
3. **Continued validity:** the association remains valid. The current EC-12 boundary expressly preserves its responsibilities and approved limited contribution. Removing EC-12 from Ponos or replacing it with EC-14 is unsupported.
4. **Wording clarification:** the standalone logical model does not repeat the EC-12/EC-14 distinction. Whether it needs an explicit exclusion of independent conclusion authority is an adjudication question (D02-CLN-019), alongside the unqualified assurance wording in COA-06 (D02-CLN-012). A local acknowledgement that EC-14 exists but has no established allocation is mechanical (D02-CLN-003).
5. **Architectural conflict:** no genuine conflict in EC-12's scope or its Ponos association was established. The risk concerns an overreading of older wording, not evidence that Ponos has been granted EC-14's authority. Category B findings do not manufacture that conflict.

Current canonical capability documents and the approved assurance derivation distinguish the responsibilities sufficiently. The complete Domain 02 tree is less clear because COA-06 and the standalone logical model lack equivalent qualifications. Domain 03's approved non-recursion clarification confirms that assurance can use the established activity framework for its own execution integrity without creating recursive assurance or assigning assurance to an application/runtime. No downstream allocation follows from that clarification.

### Assurance consistency checks

| Check | Assessment |
|---|---|
| Governance and assurance | Explicitly distinct in the current Business Capability catalogue and assurance derivation. BC-17 governing responsibility is not transferred to BC-18 or Service Guardian. |
| Monitoring and assurance | Explicit distinction preserved. Operational completion/verification in VS-03 describes operational value/evidence; it does not expressly claim an independent assurance conclusion. |
| Audit/provenance and assurance | EC-07 supplies trustworthy evidence contributions, not conclusions. Blanket all-hop capture elsewhere needs reconciliation (D02-CLN-015). |
| Subject control of independent assurance | No allocation granting the subject activity control of its independent conclusion was found. COA-06/Ponos wording still merits boundary review. |
| Evidence insufficiency | Explicit inability to establish a conclusion; neither satisfaction nor non-satisfaction follows. No conflicting failure-on-insufficiency rule found. |
| Recursion | No requirement for assurance recursively to assure itself found. Assurance activity integrity is distinguishable from independently assuring the subject. |
| Clinical assurance authority | Enterprise clinical-audit examples are qualified by Harmonia's explicit exclusion of Clinical Services Delivery Assurance. No clinical adequacy responsibility is derived. |
| Financial governance | Financial/charging exchange descriptions do not establish financial governance. No Harmonia financial-governance responsibility found. |
| EC-14 allocation | Explicitly unresolved. No EC-14 → Dokimasia, Ponos, Digital Twin or other component relationship is established. |

## 4. Capability Reference Audit

### Current approved reference baseline

The canonical Business Capability catalogue contains **18 Business Capabilities in five regions**: Care & Health Delivery (01–08); Health Information & Digital Health (09–12); Research & Innovation (13); Health Service Management (14–16); Governance & Assurance (17–18). BC-17 is Health Service Governance; BC-18 is Health Service Assurance. The Enterprise Capability catalogue contains **14**, EC-01 through EC-14. Existing 137 Features, 16 Service Delivery contexts and 18 ICT lenses measure different things and remain valid.

BC-18 has the approved three named Business Enabling Capabilities. Their contextual-view placement, relevance, Capability Tier/complete ancestry/root status/structural identities and additional Feature decomposition remain unestablished. The catalogue's L1 presentation is not evidence for silently assigning CT1 or a recovered structural identity.

### Historical Business Capability references in value streams

There are **24 BC occurrences across four representative traceability blocks, covering 16 distinct old identifiers**. The following table compares what the old identifier says with the current meaning of that number. It is an incompatibility audit, not a migration crosswalk. Every intended current stream relationship is unresolved.

| Old reference and name | Occurs in | Current catalogue meaning of that number — comparison only | Intended replacement relationship |
|---|---|---|---|
| BC-01 Patient Identification & Demographics | VS-01, VS-04 | 01 Individual Care Delivery | Current mapping unresolved |
| BC-02 Clinical Document Ingress & Processing | VS-01, VS-02 | 02 Care Access & Coordination | Current mapping unresolved |
| BC-03 Longitudinal Record Management | VS-01, VS-02, VS-04 | 03 Health Rights, Advocacy & Participation | Current mapping unresolved |
| BC-04 Terminology & Semantic Harmonisation | VS-01, VS-04 | 04 Diagnostic, Therapeutic & Clinical Support Services | Current mapping unresolved |
| BC-05 Healthcare Resource & Facility Management | VS-03 | 05 Health Products & Clinical Technology | Current mapping unresolved |
| BC-06 Security & Access Control | VS-02 | 06 Clinical Quality, Safety & Improvement | Current mapping unresolved |
| BC-07 Audit & Compliance | VS-02 | 07 Community Health & Wellbeing | Current mapping unresolved |
| BC-08 Clinical Query & Retrieval | VS-01, VS-02 | 08 Population Health & Health-System Planning | Current mapping unresolved |
| BC-09 Care Coordination & Workflow | VS-03 | 09 Health Information & Knowledge Management | Current mapping unresolved |
| BC-10 Referral & Order Management | VS-03 | 10 Standards, Semantics & Reference Governance | Current mapping unresolved |
| BC-11 Integration Membrane Governance | VS-02 | 11 Connected Health Services | Current mapping unresolved |
| BC-12 Diagnostic & Pathology Integration | VS-01, VS-02 | 12 Security, Privacy & Digital Trust | Current mapping unresolved |
| BC-13 Longitudinal Record Presentation & Exploration | VS-04 | 13 Health Research & Innovation | Current mapping unresolved |
| BC-14 Secure Clinical Communication & Collaboration | VS-03, VS-04 | 14 Health Service Direction & Stewardship | Current mapping unresolved |
| BC-15 Operational Activity Progression | VS-03 | 15 Workforce & Organisational Capability | Current mapping unresolved |
| BC-16 Real-World Entity State Progression | VS-03 | 16 Corporate Resources & Enterprise Services | Current mapping unresolved |

Locations: `docs/markdown/02-strategy/strategic-views/strategic-value-streams.md` L92, L145, L198 and L255; findings D02-CLN-022–025. Similarity to a current enabling capability or Feature is insufficient evidence to assign the old business-level relationship. The aggregate value-stream diagram correctly uses the current count and explicitly leaves BC-17/BC-18 relationships open; that does not repair the four old traceability blocks.

### Enterprise Capability references and counts

| Location / finding | Existing reference | Authoritative current reference or treatment |
|---|---|---|
| ICT lens overview/diagram/progression; D02-CLN-001 | Generic EC-01 .. EC-13 catalogue | EC-01 .. EC-14; no new lens allocation. |
| Course index generic diagram/quality test; D02-CLN-002 | Generic EC-01 .. EC-13 eligibility | Current catalogue EC-01 .. EC-14, with existing course mappings limited to established contributions. |
| Logical model; D02-CLN-003 | Composition from EC-01 through EC-13 | Retain established composition scope; explicitly acknowledge EC-14 and unresolved allocation. |
| Coherent logical/COA descriptions; D02-CLN-005 | EC-03 State & Lifecycle Governance; EC-04 Information Management & Access; EC-09 Event & Subscription Management; EC-10 Activity & Execution Coordination; abbreviated EC-13 Semantic Governance | EC-03 Managed State & Lifecycle; EC-04 Information Management; EC-09 Event & Subscription; EC-10 Activity & Execution; EC-13 Semantic Governance & Conformance. Preserve IDs/relationships. |
| Value streams; D02-CLN-026 | EC-03 Semantic Normalisation | Current EC-03 is Managed State & Lifecycle; intended current mapping unresolved. |
| Value streams; D02-CLN-026 | EC-05 Entity State Coordination | Current EC-05 is Search & Discovery; intended current mapping unresolved. |
| Value streams; D02-CLN-026 | EC-09 Activity Lifecycle Management | Current EC-09 is Event & Subscription; intended current mapping unresolved. |
| Value streams; D02-CLN-026 | EC-10 Task Envelope Management | Current EC-10 is Activity & Execution; a task-envelope design label does not establish the current relationship. Current mapping unresolved. |
| Historical inventories; D02-CLN-037 | Earlier counts/EC-01 .. EC-13 in dated quotations | Preserve as historical incoming state; not current catalogue statements. |

Other old EC labels in the value streams sometimes describe compatible narrower functions. That does not authenticate the superseded combination or establish a complete current crosswalk. Correctly named EC-02, EC-06, EC-07 and EC-12 can be distinguished from incompatible labels without automatically endorsing the entire block.

### Other obsolete or unestablished traceability

| Reference | Current evidence | Treatment |
|---|---|---|
| Views 1–5: Ingress & Processing; Information Store & Query; Security & Audit; Cross-Enterprise Interoperability; Operational Activity & Workflow | Current five contextual views: Entity Management; Service Administration; Service Delivery; Health Service Operations; Intrinsic / Shared Enablement | Current mapping unresolved. No ordinal transfer. D02-CLN-027. |
| DRV-/GOAL- identifiers and old motivation names | Current Domain 01 Driver/Goal catalogues use different numbered meanings; no approved alias/crosswalk found | Current mapping unresolved. D02-CLN-028. |
| AX-04 Semantic Preservation; AX-06 Provenance & Attribution; AX-09 Non-Destructive Evolution; AX-10 Asynchronous Operational Progression; AX-11 Component Responsibility; AX-14 Jurisdictional Alignment | Central axioms have different meanings | Intended current motivational grounding unresolved; do not merely substitute current titles at old IDs. D02-CLN-029. |
| Generic AX-01 through AX-16 summaries | Central register includes AX-17; AX-16 standing separately unresolved | Refer to current authoritative register including AX-17; avoid silently resolving AX-16. D02-CLN-004/020. |
| CON-01 .. CON-11 | Current external-constraint categories CST-EXT-001 .. CST-EXT-003 | Current mapping unresolved for the old individual constraints. D02-CLN-030. |
| BC-18 assurance stream/stage, Course of Action, resource and component relationships | Approved derivation explicitly leaves these unestablished | Preserve missing relationships. No inferred BC-18/EC-14 insertion. |
| New assurance capability-to-Feature mappings | Additional Feature decomposition not approved | No FEAT identifiers or associations to invent. Existing approved Feature catalogue remains intact. |

No obsolete current Business Capability count was found outside dated historical inventories. Current five-region capability maps and assurance navigation are present. The internal-link check found no active broken links; damaged diagrams and stale summary scope are recorded separately. No basis was found for deleting or renumbering approved Features.

## 5. Strategic Logical Component Assessment

`docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md` was reviewed in full: G1–G4, six boundary tests, seven profiles, summary table, six critical seams, Enterprise Capability compositions, ASCII/Mermaid views and downstream progression.

| Component / construct | Assessment | Preserved strategic responsibility and qualification | Findings |
|---|---|---|---|
| Mneme | Valid | Application-facing managed-information access, active distributed use, observation and coordination. Active state is reconstructable and cannot become authoritative durable truth by fallback. | D02-CLN-035; catalogue-name cleanup D02-CLN-005. |
| Mnemosyne | Valid | Authoritative durable managed-information state, authoritative version/state progression and recoverable history. Persistence implementation is not an application access mechanism. Preservation-only headings are compatible facets of the full definition. | D02-CLN-035; D02-CLN-005. |
| Ponos | Valid, with assurance wording requiring architectural review | Activity execution/progression, dispatch/supervision and failure/recovery. EC-12 consumption remains valid. A standalone assurance boundary may merit explicit clarification; no independent-conclusion or EC-14 allocation follows. | D02-CLN-019/033; D02-CLN-005. |
| Pylai | Valid | Standards-governed ingress/egress membrane, protocol boundary mediation and externally publishable representation. Private operational semantics remain internal; egress ends Harmonia management of the emitted representation. | D02-CLN-035; related COA-01 wording D02-CLN-014 does not redesign Pylai. |
| Calliope | Valid | Canonical meaning, schemas/models, terminology and semantic conformance. This strategic responsibility centre is distinct from the reusable EC-13 definition. | D02-CLN-035; D02-CLN-005. |
| Iris | Core responsibility valid; owner-attribution wording requires adjudication | Contextual presentation/interaction, decoupled from durable persistence and underlying clinical/business authority. The Mneme/Themis parenthesis for clinical authority/state validation overstates or conflates their scopes. | D02-CLN-016/035. |
| Digital Twin | Valid as a construct | Entity-centred composite state, contextual/demand-driven coordination across existing responsibilities; not an independent platform component, single resource or permanent actor/thread. | D02-CLN-036; historical active-thread wording D02-CLN-032. |

All seven profiles remain; the correct summary distinction is **six logical components plus the Digital Twin coordination construct**. This is a presentation qualification, not elimination or redesign of the seventh profile (D02-CLN-007).

The six seams preserve distinct responsibility: Mneme/Mnemosyne active versus durable state; Pylai/Calliope exchange versus semantic meaning; Ponos/Mneme execution versus managed state; Ponos/Digital Twin work progression versus entity coordination; Iris/Mneme presentation versus governed access; Pylai/Ponos external exchange versus internal activity. The six boundary tests remain useful strategic tests: responsibility centre, owned information/state, exclusions, consumption without duplicate ownership, failure boundary and substitution. They do not prescribe a deployable module topology.

The model's conceptual-view disclaimer and non-duplication dependency test preserve the difference between a **Strategic Logical Component responsibility** and a **Domain 05 Application Component allocation**. D02-CLN-013 records normative downstream package/runtime statements elsewhere that blur this distinction. The standalone model's EC-01–13 composition is established scope, not an omission to repair by assigning EC-14.

**Effect of EC-14:** the model's apparent completeness is affected by later Strategy. It should acknowledge EC-14's approved existence and its unresolved strategic logical/application allocation, while retaining established profiles, contributions and diagrams. No new component, node, seam, runtime or allocation can be derived from this assessment. Neither a capability contribution to assurance nor similarity to a component name establishes allocation.

## 6. Proposed Step 2 Scope

This is a proposed bounded documentation cleanup, subject to subsequent review and explicit authorisation. Step 2 was not executed.

### Safe mechanical corrections after authorisation

Limit the first editing package to D02-CLN-001–010:

- Correct generic Enterprise Capability catalogue ranges to EC-01–14, while retaining established EC-01–13 course/component contributions.
- Add a local EC-14 existence/unresolved-allocation qualification to the logical model; link the approved assurance derivation without drawing an allocation.
- Refer generic axiom summaries to the current authoritative register, including AX-17, without settling AX-16's standing.
- Use exact canonical EC names in coherent existing compositions; exclude incompatible value-stream meanings from this name correction.
- Replace future Pass B descriptions with links to the existing responsibility model, without claiming global approval/freeze/closure.
- Distinguish six logical components and one Digital Twin construct in summary wording.
- Repair the identified diagram-border corruption and missing edges; correct `Instatiate` to `Instantiate`.
- Replace only the four generic cross-cutting reuse descriptions identified in D02-CLN-010. Retain approved names and responsibility guardrails pending adjudication.

This package creates no traceability, Capability, Feature, information model or component allocation. Recheck local links, names/counts and unchanged diagram relationships after editing. Preserve the two dated reviews as historical records.

### Architectural questions requiring adjudication before dependent editing

| Findings | Decision needed |
|---|---|
| D02-CLN-011/012 | Distinguish bounded semantic responsibility from collaborative consumption/enforcement; determine the intended COA-06 name and whether its assurance denotes only EC-12's existing contribution. |
| D02-CLN-013 | Decide the status/home of normative package/runtime examples and technology leakage; do not design replacements. |
| D02-CLN-014 | Resolve the native-standard-representation versus private-operational-semantics wording against AX-03. |
| D02-CLN-015 | Establish policy/significance limits for provenance/audit capture and distinguish contextual attribution, transient diagnostics and durable evidence. |
| D02-CLN-016 | Clarify the Iris anti-responsibility owner attribution without inventing clinical authority. |
| D02-CLN-017/018 | Bound release/Feature completeness and universal stream-justification claims to established scope and explicitly missing relationships. |
| D02-CLN-019 | Decide whether Ponos needs a standalone independent-assurance boundary statement while preserving EC-12. |
| D02-CLN-021 | Clarify technical ACK semantics under the applicable acceptance contract; preserve technical/business disposition and external-outcome uncertainty. |

D02-CLN-020 belongs to separate central-authority reconciliation. Cleanup may acknowledge its known limitation but must not amend axioms or silently substitute/remove AX-16. REQ-FND-002 independently preserves the current entity/activity-coordination requirement.

### Ambiguous material that remains unresolved

D02-CLN-022–030 require approved replacement relationships or an explicitly approved treatment of the old traceability, not editorial inference. A subsequent cleanup must retain visible uncertainty for the four value-stream BC blocks, incompatible EC labels, old enabling views, motivation identifiers, axiom grounding and constraint references unless that evidence is supplied. Approval to clean wording alone does not approve a mapping. Quarantining, annotating or removing historical traceability would itself require an explicit decision about truthful preservation of its current standing.

D02-CLN-031 requires evidence of jurisdiction/platform scope; preserve the admitted Australian resources. D02-CLN-032 requires clarification of the historical Twin-thread example; preserve the strategic construct and do not replace the phrase with a newly designed runtime.

Preserve D02-CLN-033–038. In particular, neither EC-12, the accepted state/component seams, the Twin candidate definition, historical review outcomes nor the established Feature catalogue is a cleanup target for redesign.

### Matters belonging to another domain or separate task

- **Deferred Register Item 04:** outside this cleanup assessment and requiring separate metamodel reconciliation. Recovered SN/PS/BL/BE/EN namespaces, structural schemes, Capability Tier semantics and Information/Data Object identifiers are not correction authority. Any conflict between recovered material and current Strategy remains for that separate reconciliation.
- **Authority standing:** central AX-16 versus the Domain 01 local entry; no axiom edit here or implied by Step 2.
- **Previously unresolved source ancestry:** residual ON_LEAVE correspondence, discharge-publication timing and downstream Feature-association issues recorded in the current G1 review retain their separate source-reconciliation boundaries. They do not authorise automatic Domain 02 changes.
- **Additional Strategy derivation:** BC-17 enablement, assurance relevance/view placement/ancestry, additional assurance Features, streams, stages, Courses of Action and resources require explicit architectural work beyond this mechanical package.
- **Downstream architecture:** Domain 03 detailed behaviour, Domain 04 information derivation, Domain 05 allocation/service/API design, Dokimasia classification/execution relationships and assurance runtime design remain separate. No EC-14 allocation is proposed.
- **Runtime AI:** no contradiction requiring a Domain 02 change was established. The current execution position was used only to test consistency; no extension is proposed.

A mechanical package can be bounded and reviewable without declaring the whole Strategy baseline closed. Closure of upstream authority requires approved handling of the recorded Category B/C issues or an explicit authority statement preserving and excluding their unresolved relationships from downstream derivation.

## 7. Cross-Cutting Terminology Occurrence Audit

The complete tree was searched case-insensitively for cross-cutting capability/capabilities/feature/features, intrinsically/inherently cross-cutting, and equivalent reusable/horizontal/shared/collaborative wording. No literal cross-cutting Feature/Features phrase was found. Occurrences below are grouped only where they have the same meaning and treatment; literal code quotations in dated inventories are distinguished from active assertions.

| Current location / wording | Assessment under concern ≠ responsibility | Finding / treatment |
|---|---|---|
| README L13: cross-cutting Enterprise Capabilities | Stale generic reuse label; context establishes recurring functionality, not joint ownership. | A, D02-CLN-010: reusable Enterprise Capabilities. |
| Business catalogue L329: cross-cutting reusable ICT capabilities | Stale generic reuse label; current range is already EC-01–14. | A, D02-CLN-010: reusable ICT capabilities. |
| Tier model L25: cross-cutting, reusable capabilities | Stale generic reuse label; no additional ownership implied by this rule. | A, D02-CLN-010: reusable capabilities. |
| BEC diagram L49: cross-cutting system capabilities | Stale generic reuse label; preserve Intrinsic / Shared Enablement name and its members. | A, D02-CLN-010: reusable system capabilities. |
| Enterprise L56/L62 and EC-02 L115; logical G1 L22; capability index L86; tier L118: inherently/intrinsically cross-cutting, collaborative and invalid single-component allocation | Ambiguous responsibility wording: participating enforcement is compatible with bounded ownership; the allocation prohibition can be read more broadly. No duplicate owner was established solely from this wording. | B, D02-CLN-011: adjudicate the intended responsibility/realisation distinction. |
| COA-06 detailed L149/L152/L156/L171/L186 and course index L78 | Established title plus normative collaborative/local-contract statements. Title replacement is an architectural decision. Assurance breadth and mandated packaging add separate questions. | B, D02-CLN-012/013; retain pending decision. |
| Value streams L148/L201/L258: COA-06 title reference | Semantically harmless reference to the established course name. It does not itself allocate EC-14 or establish shared ownership. | Preserve current citation until the course's name/status is adjudicated; no independent mechanical rename. |
| Enterprise EC-07 L160: cross-cutting obligation embedded in envelopes, receipts and mutations | An evidence obligation may be discharged by many participating components while EC-07 remains bounded. The concern wording alone is harmless; universal capture scope needs separate review. | D02-CLN-015 covers the evidence-boundary question. No duplicate evidence owner inferred. |
| README L80/L166/L200; capability index L40/L92; tier L65/L123; ICT L5/L25; Enterprise L318: cross-cutting technical enablement considerations/lenses/criteria | Semantically harmless engineering considerations; lenses are explicitly distinguished from peers of the enabling model. | Preserve; no responsibility conflict identified. |
| Enterprise L10; tier L137; BEC L552: horizontal/domain-neutral/platform-wide functionality | Semantically harmless breadth of consumption or availability. The enclosing definitions retain distinct responsibilities and exclusions. | Preserve; D02-CLN-038. |
| README L37; capability-map index L7; composition examples and reusable capability ≠ centralised service guardrails | Distributed realisation and reusable consumption do not force a monolithic service or erase semantic ownership. | Preserve this principle; adjudicate only the stronger ownership/allocation statements above. |
| Dated reconciliation inventories, including old range/horizontal wording and COA-06 quotations | Historical evidence of the incoming document state, not renewed architectural assertions. | D, D02-CLN-037: preserve historical truth. |

The term cross-cutting is not intrinsically defective. The assessment establishes four unambiguous descriptive replacements, two grouped normative reconciliation concerns and harmless technical/reuse contexts. It found no proven duplicate Capability or Feature ownership arising merely from these terms. Responsibility conflicts involving evidence capture or authority attribution are recorded on their actual semantics rather than inferred from the adjective.

## 8. Coverage and Validation

### Complete Domain 02 review inventory

Every path below is relative to `docs/markdown/02-strategy/`. The inventory was checked against the current filesystem: **18 files, 4,601 lines**, with no omitted subtree.

| Reviewed file | Coverage |
|---|---|
| `README.md` | Strategy overview, derivation conventions, scope and navigation. |
| `capabilities/index.md` | Capability framework summaries, tiers, collaborative wording and links. |
| `capabilities/business-capabilities.md` | Current 18 names, five regions, relevance and BC-17/BC-18 boundaries. |
| `capabilities/business-enabling-capabilities.md` | All five views, capability/Feature entries, examples, boundaries and approved assurance addition. |
| `capabilities/enterprise-capabilities.md` | All EC-01–14 scopes, matrix, representative compositions and explicit EC-12/EC-14 boundary. |
| `capabilities/ict-foundation-lenses.md` | All 18 lenses, diagrams, count/range statements and examples. |
| `capability-maps/index.md` | Derivation-model descriptions, logical-model scope and assurance navigation. |
| `capability-maps/capability-tier-model.md` | Decomposition versus derivation, examples, diagrams, collaborative wording and unresolved assurance relationships. |
| `capability-maps/health-service-assurance-derivation.md` | Complete approved derivation, all fourteen contribution rows, independence/authority boundaries and missing allocations. |
| `courses-of-action/index.md` | All six names/summaries, axiom/EC ranges, quality criteria and navigation. |
| `courses-of-action/strategic-courses-of-action.md` | All six full definitions, implications, mandates and alignment matrix. |
| `resources/index.md` | Resource admission/closure scope, current entries and navigation. |
| `resources/strategic-resources.md` | Candidate/admitted resources, constraint references, jurisdiction and sufficiency claims. |
| `strategic-views/index.md` | View standing, responsibility/value-stream navigation and scope. |
| `strategic-views/logical-component-responsibilities.md` | Complete guardrails, tests, profiles, compositions, seams, diagrams and allocation disclaimers. |
| `strategic-views/strategic-value-streams.md` | All four streams/stages, four traceability blocks, execution notes, aggregate diagram and principles. |
| `reviews/ax05-state-responsibility-reconciliation.md` | Complete historical inventory, accepted state reconciliation, status/scope and literal source quotations. |
| `reviews/business-capability-five-region-reconciliation.md` | Complete historical five-region reconciliation, incoming state and deferred downstream consequences. |

### Verification results and limits

- **PASS — coverage and searches:** full Domain 02 review completed; searches covered cross-cutting/reusable/collaborative terminology, BC/EC identifiers and names, counts, Feature identifiers, assurance/monitoring/evidence terms, component references and document-status language.
- **PASS — active local links:** 223 active local Markdown links checked for target/anchor existence; no broken active target/anchor found. Code-fenced/inline literal historical quotations were excluded. The apparent old relative link at AX-05 review L463 is not active navigation.
- **PASS — Feature identity/count check:** 137 distinct definitions/identifiers and no duplicate Feature names: EM 31, SA 29, SD 20, HSO 29, ISE 28. Assurance's deliberately absent additional Feature decomposition was not filled.
- **PASS — current counts:** 18 Business Capabilities/five regions, 14 Enterprise Capabilities, 18 ICT lenses and 16 Service Delivery contexts are separately established. Historical counts were assessed as history.
- **PASS — mutation guardrail:** the final repository check compares HEAD and all 2,277 tracked-file bytes with the entry baseline. The tracked-content digest remains `319c309f9a6cb47b7a9c59038cacc254a8ce06e9290a25f69a5979a65e1d8c99`; the only new repository file is this assessment report. No index/navigation/generated file was changed.
- **Not run — builds, tests or generated documentation:** this is a documentation assessment with no implementation change. Running Maven or documentation generation could create unpermitted repository outputs. No historical suite result is claimed as validation of this task.

The static checks establish review coverage, reference defects and file preservation; they do not decide the unresolved architectural mappings, release sufficiency, central AX-16 standing or downstream allocations. No source-code implementation was used to determine what Strategy ought to say.

### Required task confirmations

| Confirmation | Result |
|---|---|
| 1. Complete current Domain 02 tree reviewed | Confirmed: all 18 files, including indexes and both historical review records. |
| 2. Stale capability terminology/identifier searches performed | Confirmed: cross-cutting/equivalent wording, BC/EC names/IDs/counts, Features and historical mappings. |
| 3. EC-12 and EC-14 assessed separately | Confirmed: distinct canonical meanings, contribution matrix and no substitution. |
| 4. Strategic logical component responsibilities reviewed | Confirmed: six components, Digital Twin construct, guardrails, boundary tests, seams, compositions and diagrams. |
| 5. Value-stream mappings reviewed | Confirmed: all four streams, all four traceability blocks and aggregate progression. |
| 6. Item 04 not used as metamodel-change authority | Confirmed: scope control only; separate metamodel reconciliation remains required. |
| 7. No Domain 02 or other architecture file modified | Confirmed: tracked-content preservation and sole-report addition checked. |
| 8. No implementation changes made | Confirmed: no source, tests, configuration or runtime mutation. |
| 9. No Dokimasia allocation invented | Confirmed: no EC-14 or assurance execution/component relationship assigned. |
| 10. No Domain 04/05 architecture derived | Confirmed: no Information/Data Objects, application allocation, service/API design or implementation design. |

> **No architecture was modified by this assessment. All proposed corrections remain subject to subsequent review and explicit authorisation.**
