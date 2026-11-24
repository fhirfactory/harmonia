# AX-05 / Domain02 Strategy — State-Responsibility Reconciliation

**Date:** 2026-10-08. **Disposition:** **APPROVED / CLOSED**. Existing Strategy inconsistency reconciled against unchanged AX-05. This record documents a correction, not a change in AX-05 architectural intent or a broader Strategy redesign.

**Human architectural approval:** Human architectural review accepted this reconciliation on **2026-10-08**, including the complete A01–A24 affected-statement inventory, its classifications/dispositions, the documented responsibility boundaries and exclusions, and the qualified interpretation of state progression. These semantics are accepted without refinement or extension.

The accepted distinction is **Ponos progresses operational activity. Mneme manages active use. Mnemosyne establishes durable truth.** This shorthand does not replace the normative wording of AX-05. The governing architectural authority remains unchanged; the corrected defect was in Domain02 Strategy wording.

The original completion report's CLI-provenance limitation and unrelated protected G2 whitespace diagnostic remain historical administrative/verification observations. They do not invalidate or alter the approved architectural result, and neither observation is rewritten or repaired by this approval transaction.

Approval closes this reconciliation only. REQ-FND-005 remains APPROVED and Domain01 remains CLOSED / FROZEN; Independent Assurance Strategy derivation and the other deferred matters remain outside scope. The [approval/closure completion report](../../../../.junie/reports/2026-10-08-ax05-domain02-strategy-approval-closure.md) records this status transaction and its verification.

## 1. Discovery, Authority and Incoming State

The [earlier architectural investigation](../../04-information-architecture/reviews/dokimasia-r1-motivation-traceability-ax05-investigation.md#7-strategy--ax-05-conflict-reconstructed-from-source) discovered explicit Strategy exclusions of Mnemosyne's authoritative state progression, authoritative version progression and durable truth while tracing a separate assurance concern. Those exclusions are visible in the committed Strategy baseline. The present instruction authorises this separate, bounded reconciliation.

At session entry, the worktree already contained AX-05 corrections in [logical-component-responsibilities.md](../strategic-views/logical-component-responsibilities.md) and [strategic-courses-of-action.md](../courses-of-action/strategic-courses-of-action.md), alongside unrelated changes in Domains01/04 and reports. The corrections in those two Strategy files were inspected and retained. They are not claimed as newly authored in this session. The smallest additional semantic edit qualifies the remaining G3 pronoun “the two” after its expanded three-responsibility list. A navigation link, this review record and the [completion report](../../../../.junie/reports/2026-10-08-ax05-domain02-strategy-reconciliation.md) complete the work.

Repository-wide and documentation AGENTS.md govern the review. [Central architectural axioms](../../governance/architectural-axioms.md#1-purpose) are the highest architectural authority; accepted decisions and requirements refine their application. Existing code, document repetition and historical execution reports cannot override them.

**No inspected approved amendment, superseding decision or accepted ADR requires reconsidering AX-05.** The relevant authority chain corroborates it. No higher-authority responsibility conflict requiring the specified stop was found.

## 2. Canonical AX-05 and Corroborating Evidence

The governing wording is [central AX-05](../../governance/architectural-axioms.md#ax-05):

> Mneme owns Harmonia's application-facing access to managed information
> and the distributed active-state representation, observation and
> coordination required to use that information safely.
>
> Mnemosyne owns Harmonia's authoritative durable representation of
> managed information. It atomically establishes authoritative state and
> authoritative version progression and persists the durable management
> metadata required to interpret that state.
>
> Mneme manages active use; Mnemosyne establishes durable truth.

Its consequences permit Mneme to reject or coordinate proposed state progression before persistence, reserve establishment of new authoritative durable state to Mnemosyne, require Mneme convergence after authoritative commit, forbid authority promotion on either subsystem's loss, and require reconstructability where applicable. Successfully established active-state generation and authoritative version remain separate concurrency domains; failed/degraded convergence cannot establish a trusted active generation.

| Evidence inspected | Effect on the responsibility boundary |
| :--- | :--- |
| [Domain01 AX-05](../../01-motivation/principles/architectural-axioms.md#ax-05-active-state-and-authoritative-durable-state-are-distinct), Principle, Implications and Current Realisation | Component-neutral principle agrees with central AX-05. Current Realisation expressly reserves authoritative durable state/version progression to Mnemosyne alone. It supplies no competing intent. |
| [ADR register](../../../architecture-decisions.md), ADR-001, 003, 007, 009, 010 and 014–017 | Ponos owns work execution; Mnemosyne owns durable application state; source clinical authority, information credibility, durable work transfer and evidence remain distinct concerns. |
| [Accepted ADR-018](../../../architecture-decisions.md#adr-018-----mnemosyne-defines-the-authoritative-durable-state-boundary) | Mnemosyne defines committed authoritative application state. Mneme cache/replication does not establish durable acceptance. Petasos durable work acceptance does not replace Mnemosyne committed-state authority. The earlier Mneme distributed-durability alternative is explicitly rejected. |
| [Accepted ADR-019](../../../architecture-decisions.md#adr-019--mneme-owns-distributed-resource-access-and-coordination) | Mneme is non-authoritative, reconstructable active access/coordination. Final persistence and persisted version belong to Mnemosyne. Coordination does not confer durable-write authority. |
| [Accepted ADR-020](../../../architecture-decisions.md#adr-020--governed-information-uses-lifecycle-state-rather-than-physical-deletion) | Governed lifecycle changes establish authoritative updates/versions in Mnemosyne; Mneme eviction does not alter durable state. No lifecycle or physical-delete changes are made here. |
| [Governed write/concurrency contract](../../../design/governed-write-concurrency-contract.md), §§2–3 and version responsibilities | Corroborates active coordination versus authoritative conditional commit/version authority. Implementation mechanisms are not imported into Strategy. |
| [REQ-FND-001](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-001-durable-ingress-acceptance-boundary) and [master requirement catalogue](../../01-motivation/requirements-constraints/master-requirements-catalogue.md) | Positive acknowledgement requires the applicable durable acceptance boundary; active cache presence alone is insufficient. Durable work acceptance and committed information state must not be conflated. |
| [Assessments](../../01-motivation/drivers-assessments/assessments.md), false acceptance and persistence contention; [Drivers](../../01-motivation/drivers-assessments/drivers.md), availability and coordinated activity; [strategic goals](../../01-motivation/goals-outcomes/strategic-goals.md), durable acceptance and responsive access | Motivate durable acceptance and responsive active access without assigning durable authority to operational execution. |
| [Approved/refrozen Domain01 review](../../01-motivation/reviews/independent-assurance-reconciliation.md) and [foundational requirements](../../01-motivation/requirements-constraints/foundational-requirements.md) | REQ-FND-005 approval and Domain01 CLOSED / FROZEN are preserved. The prior unresolved Strategy issue is historical discovery evidence; assurance derivation is not authorised here. |

AX-14 preserves these semantic distinctions. AX-15 preserves uncertain operational outcomes. [AX-17](../../governance/architectural-axioms.md#ax-17) requires truthful authority and traceability: no missing responsibility, protocol, assurance relationship or deployment decision is supplied by inference. AX-12 classification and AX-16 central-register standing remain separate, unchanged matters.

## 3. Responsibility Domains and Progression Terminology

| Concern | Established responsibility | Boundary preserved |
| :--- | :--- | :--- |
| Operational activity progression | Ponos executes/progresses operational units of work and workflows. | Executing an activity that changes information does not establish authoritative durable information state or its version. |
| Active information/state management | Mneme supplies application-facing access, active distributed representation, observation and coordination; may reject/coordinate a proposed information-state change before persistence. | Active state/generation is reconstructable and does not establish authoritative durable state/version. Mneme is not the operational workflow engine. |
| Authoritative durable state establishment | Mnemosyne atomically establishes authoritative state and authoritative version progression, persists interpreting durable management metadata, and supplies preservation/recovery. | This does not allocate operational workflow execution, application-facing query interfaces, Digital Twin coordination or active distributed state to Mnemosyne. |

The shorthand “Ponos progresses activity; Mneme manages active use; Mnemosyne establishes durable truth” summarises the established boundary; it does not replace normative AX-05.

The exact AX-05 wording “proposed state progression before persistence” is retained where surrounding text identifies Mneme coordination followed by exclusive Mnemosyne establishment. It is not interpreted as authoritative commitment by Mneme. Workflow progression exclusions refer to operational activity execution. Authoritative state/version progression refers to establishing committed durable state. No universal reinterpretation of “state progression” is applied.

## 4. Complete Affected-Statement Inventory

The classifications below concern the committed wording where it differs from the incoming worktree, except A22, which identifies the residual incoming ambiguity. “Retained incoming correction” means the correction was already present at session entry. Current source locations are stable section/line references, not the old investigation's historical line numbers.

| ID / Strategy location | Original concern and classification | Exact semantic correction / disposition |
| :--- | :--- | :--- |
| A01 — Logical model G3, Mneme bullet (L34) | “governs what is known and its current managed state”: ambiguous / scope-mismatched if read as durable-state authority. | Retained incoming correction: manages active use and the current active representation. |
| A02 — Logical model G3, Mnemosyne bullet and AX-05 paragraph (L36,39) | Three domains were not explicitly represented: incomplete but compatible. | Retained incoming correction: explicit Mnemosyne authoritative durable state/version establishment; Twins do not acquire that authority. |
| A03 — Component 1 introduction (L73) | “application-facing operational authority”: ambiguous / scope-mismatched. | Retained incoming correction: “application-facing access boundary”, with distributed access and active governance retained. |
| A04 — Component 1 architectural distinction (L77) | “runtime management vs. durable preservation”: incomplete but compatible. | Retained incoming correction: active runtime management versus authoritative durable state establishment, preservation and recovery; durable business concepts still pass through Mneme. |
| A05 — Component 1 anti-responsibilities (L80) | Durable establishment/version exclusion missing: incomplete but compatible. | Retained incoming correction explicitly excludes authoritative durable state and authoritative version progression from Mneme. |
| A06 — Component 2 responsibility and descriptive paragraph (L100,102) | Preservation/recovery alone: incomplete but compatible, and scope-mismatched if used as its exhaustive boundary. | Retained incoming correction adds atomic authoritative state/version establishment and durable interpreting metadata; preservation/history/recovery retained. |
| A07 — Component 2 clarification (L106), authoritative state | Denial of authoritative state progression: directly contradictory to AX-05. | Retained incoming correction: only Mnemosyne can establish a new authoritative durable state. |
| A08 — Component 2 clarification (L106), authoritative version | Denial of authoritative version progression: directly contradictory. | Retained incoming correction: authoritative version progression belongs to Mnemosyne; active generation is a separate domain. |
| A09 — Component 2 clarification (L106), durable truth | Denial of durable truth: directly contradictory. | Retained incoming correction: Mnemosyne establishes durable truth. |
| A10 — Component 2 clarification (L106), operational state | Unqualified operational-state progression exclusion: ambiguous / scope-mismatched. | Retained incoming correction limits the exclusion to operational activity progression/workflow execution; preserves Mneme proposal/coordination and convergence after commit. |
| A11 — Component 2 anti-responsibilities (L111) | Active-state and Twin exclusions were implicit: incomplete but compatible. | Retained incoming correction explicitly excludes active distributed state and entity-centred Digital Twin coordination. |
| A12 — Component 2 boundary evaluation (L116–118) | Preservation-only responsibility/cohesion/authority summary: incomplete, duplicated and stale relative to AX-05. | Retained incoming correction aligns all three with atomic authoritative state/version establishment, metadata, preservation and recovery. Other evaluation points retained. |
| A13 — Component 3 anti-responsibilities (L138) | Execution-to-durable-authority exclusion missing: incomplete but compatible. | Retained incoming correction excludes authoritative durable information-state/version establishment from Ponos even when activity changes information. |
| A14 — Summary table, Mneme and Mnemosyne rows (L314–315) | Preservation-only duplicated responsibility shorthand: incomplete / stale. | Retained incoming correction adds Mnemosyne establishment and Mneme's exclusion of that authority; operational and standards exclusions retained. |
| A15 — Critical seam diagram and Seam 1 boundary/split (L340–341,350,353) | Runtime/preservation split alone: incomplete / stale duplicated summary. | Retained incoming correction names active versus authoritative state; Mnemosyne atomically establishes state/versions and interpreting durable metadata. |
| A16 — Seam 1 rule (L354), durable truth | “Mnemosyne does not own ... durable truth”: directly contradictory. | Retained incoming correction: Mnemosyne establishes durable truth. |
| A17 — Seam 1 rule (L354), progression | “does not own state progression”: ambiguous / scope-mismatched. | Retained incoming correction separates Ponos operational progression, Mneme proposed-state coordination and Mnemosyne durable establishment; retains convergence, distinct concurrency and the governed application-access boundary. |
| A18 — Seam 2 Mneme split (L359) | Unqualified “what is known/current managed state”: ambiguous if read across authority domains. | Retained incoming correction explicitly describes active use/current active representation. Governed transition requests and workflow exclusions remain. |
| A19 — Seam 6 split and rule (L388–389) | “authoritative runtime information and execution capabilities” and unrestricted cache/database wording: ambiguous / scope-mismatched. | Retained incoming correction distinguishes Mneme governed access from Ponos execution and reserves durable establishment/version to Mnemosyne. Iris uses defined interfaces and never writes directly to databases or caches. |
| A20 — Composition map, conceptual ASCII/Mermaid views and final summary (L409,476–485,517,546) | Preservation-only duplicated Mnemosyne descriptions: incomplete / stale. | Retained incoming correction includes authoritative durable state/version establishment in each representative view; diagrams remain conceptual, not runtime or deployment allocations. |
| A21 — COA-02 description, AX-05 traceability and matrix (L48,53,182) | Durable-preservation-only shorthand: incomplete / stale duplicated summary. | Retained incoming correction includes atomic authoritative state/version establishment and interpreting metadata, alongside preservation/recovery and active access separation. |
| A22 — G3 Digital Twin bullet (L37) | Incoming “coordinate the two” follows three responsibilities: ambiguous. | **This session:** names “active information/state management and operational activity” explicitly. The next paragraph still excludes Mnemosyne durable-state authority from Twins. |
| A23 — COA-02 component seam (L61), progression exclusion | “Mnemosyne does not own state progression”: ambiguous / scope-mismatched; application-facing query exclusion is consistent. | Retained incoming correction limits exclusions to operational activity progression, workflow execution, active distributed state, Twin coordination and application-facing queries. It assigns durable establishment/version to Mnemosyne, proposal/coordination to Mneme, and requires post-commit convergence/distinct concurrency domains. |
| A24 — Strategic views navigation (L27) | Review traceability absent; mechanical navigation addition. | **This session:** links this dated review without changing a component or capability definition. |

No direct AX-05 contradiction remains in current Strategy responsibility wording. Historical quotations/dispositions in review records do not reactivate superseded Strategy exclusions.

## 5. Compatible Statements Deliberately Retained

- G1 reusable capability versus centralised service, G2 component boundaries by architectural responsibility, and G4 execution/standards/transport distinctions are unchanged.
- Mneme's runtime responsibility statement, active access-contract/presentation authority, enduring business concepts, application access and active relationship/context responsibilities are retained with its explicit durable-authority exclusion.
- Component 2's “Durable Preservation & Recovery” heading, tier-model Mnemosyne label, storage-lens preservation question/relationship, and EC-04 preservation shorthand are compatible facet descriptions. They are not exhaustive definitions or denials of establishment. Their text and existing anchors remain unchanged.
- Mnemosyne historical preservation/recovery, operational workflow/task exclusions, external standards exclusions and application-facing query/persistence exclusion remain unchanged. Preservation immutability does not prohibit establishment of successive authoritative versions.
- Ponos execution/lifecycle authority and guarded state-transition requests concern operational execution; they do not confer durable information-state authority.
- The Digital Twin construct coordinates Mneme/Ponos concerns without becoming an independent persistence or workflow engine. Entity-lifecycle coordination authority is not durable commit authority.
- Iris remains non-authoritative and consumes governed application interfaces. Validation exclusions are not interpreted as granting Mneme durable authority; broader clinical-authority allocation is outside this AX-05 review.
- EC-03 lifecycle, EC-04 validation/persistence/version/access and EC-10 execution functions describe reusable capability, not exclusive subsystem allocation. Feature-level encounter, order, theatre and bed-state progression remains domain behaviour.
- COA-02 strategic approach and its duplicated course-index wording are retained; the corrected detailed seam provides the full responsibility distinction. COA-04 and COA-05 retain operational/Twin coordination.
- Value streams retain stakeholder-state/value descriptions, durability/integrity/recovery outcomes, source-clinical-authority exclusions and the rule against mapping stages directly to components. Their unrelated motivational labels are not corrected here.
- Business-capability preservation, healthcare-feature lifecycle descriptions, national terminology/directory authority and strategic-resource custody do not supply a competing durable-state allocation.
- All EC-12 and existing assurance-related Strategy text remains unchanged. Assessing a search hit for AX-05 contradiction supplies no assurance derivation or approval.

## 6. Rejected Interpretations

| Interpretation rejected | Reason |
| :--- | :--- |
| Preserve Strategy exclusions by weakening AX-05 or silently treating them as an amendment. | Downstream wording cannot override central AX-05; no approved override was found. |
| Interpret every “state progression” as workflow execution. | Explicit authoritative state/version establishment is a different responsibility. The committed exclusions of those qualified responsibilities are direct contradictions. |
| Reduce Mnemosyne to preservation after some other component establishes durable truth. | AX-05 assigns atomic establishment and authoritative version progression to Mnemosyne itself. |
| Make Mnemosyne the operational workflow or Digital Twin engine. | Establishing committed state/version does not own operational activity progression or entity-centred coordination. |
| Promote Mneme active state, cache replication or active token to durable truth. | Coordination, active generation and reconstructability remain distinct from authoritative durable state/version. |
| Give Ponos durable authority because execution changes information. | Ponos requests changes through governed contracts; Mnemosyne establishes their authoritative durable result. |
| Bypass Mneme with application database/JPA access, or derive a new centralised service/network protocol from the correction. | The governed access boundary and responsibility-versus-topology distinction are preserved. |
| Treat durable acceptance, source clinical credibility, independent assurance or evidence custody as interchangeable with durable-state authority. | ADR-018, AX-06 and AX-17 preserve these different meanings; this review derives no assurance responsibility. |

## 7. Downstream Implications Deferred

Future derivation must respect the repaired durable-state boundary when interpreting any managed information, including information that a later approved assurance design may manage. A stored conclusion, its durable commit/version, its credibility and the responsibility permitted to determine it remain different questions. AX-05 establishes only the state-access/coordination/commit boundary; it supplies no assurance evaluator, component allocation, information model or execution relationship.

No independent assurance Enterprise Capability, EC-12 modification, Dokimasia allocation, Assurance Praxis, Pragma/Praxis change, assurance execution allocation to Ponos or assurance-state semantics is introduced. Domain03–04, G2 work, the approved downstream finding, implementation, APIs, persistence and deployment remain untouched. AX-12 and AX-16 consistency matters remain unresolved outside scope. No convergence/runtime milestone or later implementation step is commenced.

Frozen Domain01 and prior investigation/report statements that the Strategy issue was unresolved remain byte-identical as historical evidence. This dated Domain02 record supplies the new disposition; it does not revise historical conclusions.

## 8. Review Coverage and Validation

All 15 incoming Domain02 Strategy artefacts were searched using all requested terms, case-insensitively across naming/hyphen variants, and assessed in their surrounding component, course, capability, feature, resource or value-stream context. A supplementary scan covered standalone state, authority, durability, version, commit, workflow and activity references. Headings, ASCII/Mermaid models, matrices, summary duplicates and navigation were included. No alternate Domain02 source artefact was found outside this canonical directory. Earlier mixed Motivation/Strategy LaTeX and external/local-directory strategy material are not Domain02 responsibility definitions and are not modified.

The appendix records every current matching source line and its contextual disposition; snippets are locators, not replacement normative statements. Overlapping source blocks are intentionally represented so omissions and stale duplicates can be checked. The review/navigation additions are historical traceability, not a new authority source.

Validation is recorded in the completion report: architecture tests PASS (90 tests; zero failures/errors/skips), source semantic review, local links/anchors, incoming-content SHA-256 preservation and git diff --check. No production changes require a second architecture-suite run. Maven conformance tests do not by themselves establish documentation semantics.

## 9. Complete Source Inventory

Classification codes in this appendix:

- **C1 — Consistent:** named component responsibilities remain distinct in their surrounding profile/seam; retained.
- **C2 — Consistent:** capability, feature, stakeholder value, preservation or execution description supplies no conflicting exclusive component authority; retained.
- **C3 — Consistent:** semantic, security, source, navigation or architectural authority wording supplies no durable-state allocation; retained.
- **I — Incomplete but compatible:** intentionally limited shorthand/facet; retained with the full responsibility defined in the logical model/COA-02.
- **Q — Ambiguous, qualified:** incoming G3 pronoun repaired in this session.
- **CS — Consistent, scope qualified:** responsibility authority/validation is read within its stated active, operational or presentation domain, not as durable establishment.

Primary hits cover the requested terms. Supplemental hits cover standalone/broader terms and include context, authority evaluations and diagram fragments. No remaining contradictory or unresolved state-progression exclusion was found. Classification is confined to AX-05 responsibility consistency, not a certification of unrelated traceability or assurance wording.

**Coverage:** 15 source artefacts; 234 primary matching lines and 220 supplemental matching lines (454 total). Source positions refer to the reviewed 2026-10-08 worktree.

### README.md

[Source](../README.md); 199 lines reviewed; 8 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L9 / supplemental | `` Domain 02 establishes an authoritative, technology-neutral architecture that defines: `` | C3 |
| L25 / supplemental | `` 2. **Component-Capability Conflation**: Defining capabilities in terms of existing software modules or middleware platforms (e.g., equating integration capabilities with a specific message broker o… `` | C2 |
| L38 / supplemental | `` - **Enforce Technology-Neutral System Boundaries**: Ensure that integration boundaries, operational progression, and state management remain stable when underlying storage products, message brokers… `` | C2 |
| L39 / supplemental | `` - **Govern Architectural Evolution Across Releases**: Provide clear scope sufficiency for Harmonia 1.x and 2.x while delineating roadmap candidates (such as authoritative master-patient EMPI reconc… `` | C3 |
| L119 / primary | ``        ▼  Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, Digital Twin `` | C1 |
| L154 / primary | `` To preserve architectural clarity, Domain 02 enforces strict boundary rules: `` | C3 |
| L163 / primary | `` - Strategic logical component responsibility boundaries (Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, and Digital Twin). `` | C1 |
| L172 / primary | `` - **Physical Schemas & DDL**: Database table schemas, JPA entity annotations, JSON schemas, and MLLP framing bytes (application logical/software representation belongs downstream in Domain 05, exch… `` | C2 |

### capabilities/business-capabilities.md

[Source](../capabilities/business-capabilities.md); 210 lines reviewed; 11 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L9 / supplemental | `` - **Independent of Organizational Hierarchy**: It describes an operational ability, not a department, committee, or job title. `` | C2 |
| L116 / primary | `` - **Harmonia Enabling Role**: Harmonia supports clinical safety by providing patient identifier correlation and association, eliminating duplicate event processing, ensuring non-destructive data pr… `` | C3 |
| L134 / primary | `` - **Operational Scope**: Governing the lifecycle, structure, indexing, durable preservation, and retrieval of clinical health records, medical documentation, and clinical knowledge assets. `` | C2 |
| L135 / primary | `` - **Harmonia Enabling Role**: Harmonia provides vendor-neutral longitudinal clinical record assembly, durable historical state preservation, and active distributed access to managed clinical inform… `` | C2 |
| L145 / supplemental | `` - **Harmonia Enabling Role**: Harmonia delivers multi-protocol boundary adaptation, resilient message distribution, and asynchronous activity coordination across enterprise boundaries. `` | C2 |
| L149 / primary | `` - **Operational Scope**: Enforcing digital identity governance, authentication, attribute- and role-based access control (ABAC/RBAC), verifiable data protection, compliance auditing, and privacy pr… `` | C3 |
| L173 / supplemental | `` - **Harmonia Enabling Role**: Harmonia delivers master Healthcare Provider Directory management (Practitioners, Roles, Organizations, Locations, Endpoints), federating authoritative national and re… `` | C3 |
| L178 / supplemental | `` - **Harmonia Enabling Role**: Harmonia integrates with patient administration and financial systems to exchange clinical activity summaries and charging records without managing enterprise ERP func… `` | C2 |
| L194 / primary | `` \| **09** \| Health Information & Knowledge Management \| Health Information & Digital \| **Harmonia-Core** \| Vendor-neutral longitudinal clinical record assembly, durable state preservation. \| `` | C2 |
| L196 / supplemental | `` \| **11** \| Connected Health Services \| Health Information & Digital \| **Harmonia-Core** \| Multi-protocol boundary adaptation, resilient message distribution, asynchronous activity coordination. \| `` | C2 |
| L201 / supplemental | `` \| **16** \| Corporate Resources & Enterprise Services \| Enterprise Management \| **Reference** \| Integration with patient administration and financial systems for activity and charging records. \| `` | C2 |

### capabilities/business-enabling-capabilities.md

[Source](../capabilities/business-enabling-capabilities.md); 667 lines reviewed; 41 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L14 / supplemental | `` 5. **Architectural Scope Sufficiency**: This model is considered **sufficient for the architecture and strategic analysis of Harmonia 1.x and 2.x**. Roadmap items (such as authoritative master-pati… `` | C2 |
| L49 / supplemental | `` │    Control, Collaboration, Workflow Coordination, and Presentation.    │ `` | C2 |
| L59 / supplemental | `` - **Coverage Sufficiency**: The model provides comprehensive capability and Feature coverage to define Harmonia's intended responsibilities, boundaries, dependencies, and enabling relationships for… `` | C2 |
| L63 / supplemental | ``   - **Harmonia 3.x Roadmap Candidate**: Authoritative master-patient identity reconciliation, probabilistic matching algorithms, and Enterprise Master Person Index (EMPI) merge/unmerge engines rema… `` | C2 |
| L112 / supplemental | ``   - *Definition*: Resolving, maintaining, and correlating identities and identifiers assigned to an individual across different authorities. `` | C3 |
| L113 / supplemental | ``   - `FEAT-EM-01`: **Identifier Resolution**: Ingest and resolve external identifiers against registered issuing authorities and namespace systems. `` | C3 |
| L114 / supplemental | ``   - `FEAT-EM-02`: **Cross-Authority Identifier Correlation**: Correlate disparate local identifiers (e.g., hospital MRN, clinic client ID, national IHI) belonging to the same individual. `` | C3 |
| L116 / supplemental | ``   - `FEAT-EM-04`: **Governed Identity Correction**: Apply administrative identity updates, linked-record corrections, and split/merge notifications propagated from authoritative sources. `` | C3 |
| L126 / supplemental | ``   - `FEAT-EM-08`: **Relationship Validity Tracking**: Maintain effective date intervals and authority scopes for nominated client relationships. `` | C3 |
| L158 / supplemental | ``   - *Definition*: Authoritative identification and profiling of legal and operational healthcare entities. `` | C3 |
| L256 / supplemental | `` - **Order Administration**: An **Order** requests a defined, bounded **fulfilment activity** (e.g., execute a diagnostic test, dispense a medication, perform a procedure) and strictly supports a **… `` | C2 |
| L259 / supplemental | `` - **Technical Delivery Acknowledgement**: A transport-level receipt (e.g., MLLP commit, HTTP 200/202, message broker ACK) confirming that bytes crossed the boundary and were durably accepted into a… `` | C2 |
| L263 / supplemental | `` Business acknowledgement is contextual and may communicate acceptance, rejection or another explicit disposition; it does not establish a universal reviewed → accepted → scheduled progression or tr… `` | C3 |
| L291 / supplemental | ``   - `FEAT-SA-08`: **Encounter Context Creation**: Create and maintain authoritative encounter tracking contexts for hospital admissions, outpatient visits, and community contacts. `` | C3 |
| L292 / primary | ``   - `FEAT-SA-09`: **Encounter State Progression**: Track encounter status transitions (`PLANNED` $\to$ `ARRIVED` $\to$ `IN_PROGRESS` $\to$ `ON_LEAVE` $\to$ `DISCHARGED`). `` | C2 |
| L303 / supplemental | `` - **6.1 Diagnostic Workflow Management** `` | C2 |
| L324 / supplemental | ``   - `FEAT-SA-24`: **Document Versioning & Supersession**: Manage document classification, author attribution, amendment attribution, versioning, and supersession or withdrawal without destructive d… `` | C2 |
| L334 / supplemental | ``   - `FEAT-SA-28`: **Delivery Acknowledgement Tracking**: Correlate recipient delivery and read receipts with outbound clinical communications, distinguishing transport delivery from recipient ackno… `` | C3 |
| L341 / supplemental | `` - Export of clinical activity summaries and charging records to billing systems. *(No features decomposed)*. `` | C2 |
| L369 / primary | `` 1. **Heterogeneous Axes Preserved**: The 16 Service Delivery capabilities represent heterogeneous clinical settings (acute, emergency, primary), modalities (virtual, outreach), and specialties (dia… `` | C3 |
| L370 / supplemental | `` 2. **Zero Harmonia-Core in Service Delivery**: No Service Delivery capability is classified as `Harmonia-Core`. Harmonia provides integration, transport, state management, and notification; **Harmo… `` | C2 |
| L372 / primary | `` $$\text{Provide Context} \longrightarrow \text{Receive Clinical State/Outcome} \longrightarrow \text{Correlate} \longrightarrow \text{Preserve Provenance} \longrightarrow \text{Make Available} \lon… `` | C2 |
| L385 / supplemental | ``   - `FEAT-SD-03`: **Acute Clinical State Event Capture**: Ingest clinical deterioration notifications and acute clinical status updates. `` | C2 |
| L462 / supplemental | `` &gt; **Where operational activity occurs in the context of a Healthcare Service, that service context must be explicitly associated with the activity and state rather than inferred solely from practit… `` | C2 |
| L464 / supplemental | `` $$\text{Event} \longrightarrow \text{Entity Context} \longrightarrow \text{Required Activity} \longrightarrow \text{Assignment} \longrightarrow \text{Dispatch} \longrightarrow \text{ACK} \longright… `` | C2 |
| L482 / primary | ``   - `FEAT-HSO-05`: **Operating Theatre Case Progression**: Track surgical case state transitions (`CALLED` $\to$ `IN_THEATRE` $\to$ `ANAESTHETISED` $\to$ `INCISION` $\to$ `CLOSED` $\to$ `RECOVERY`). `` | C2 |
| L483 / primary | ``   - `FEAT-HSO-06`: **Perioperative Resource Notification**: Dispatch alerts to portering, sterilization, and recovery teams based on surgical case progression milestones. `` | C2 |
| L496 / supplemental | `` - **6.1 Bed State & Turnover Management** `` | C2 |
| L498 / primary | ``   - `FEAT-HSO-12`: **Bed State Progression**: Progress bed states in response to clinical and environmental events (e.g., patient discharge automatically transitions bed to `DIRTY`). `` | C2 |
| L507 / supplemental | ``   - `FEAT-HSO-15`: **Operational Capacity Metric Aggregation**: Aggregate operational capacity telemetry (occupancy rates, wait times, diversion status) across facilities. `` | C2 |
| L561 / supplemental | `` 9. Workflow & Activity Coordination [Harmonia-Core] `` | C2 |
| L575 / supplemental | `` #### Workflow & Activity Coordination: Three Work Units `` | C2 |
| L579 / supplemental | `` *Execution Semantics*: Workflow Management provides the coordination and state engine through which these three activity types progress. `` | C2 |
| L583 / supplemental | `` *Important Boundary*: A clinical statement made within a collaboration chat does **not** automatically become an authoritative clinical Observation or medical record entry. It remains collaborative… `` | C2 |
| L590 / primary | `` - Authoritative clinical state preservation. `` | C2 |
| L600 / primary | ``   - `FEAT-ISE-03`: **Durable Clinical Record Preservation**: Durably maintain governed, vendor-neutral longitudinal clinical representation and preservation with verifiable integrity controls. `` | C2 |
| L620 / supplemental | ``   - `FEAT-ISE-11`: **Health Information Access & Consent Control**: Evaluate patient consent preferences, practitioner authority boundaries, and access control policies for managed clinical records. `` | C3 |
| L639 / supplemental | `` #### 9. Workflow & Activity Coordination (`Harmonia-Core`) `` | C2 |
| L642 / supplemental | ``   - `FEAT-ISE-21`: **To Do Decision & Approval Coordination**: Coordinate human clinical review items, sign-offs, and approval workflows. `` | C2 |
| L643 / supplemental | ``   - `FEAT-ISE-22`: **Synthetic Task Orchestration**: Execute non-human, automated platform activity units and sequence orchestration. `` | C2 |
| L644 / supplemental | ``   - `FEAT-ISE-23`: **Workflow Timeout & Escalation Oversight**: Supervise operational deadlines, executing automated timeout notifications and supervisory escalations. `` | C2 |

### capabilities/enterprise-capabilities.md

[Source](../capabilities/enterprise-capabilities.md); 281 lines reviewed; 45 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L28 / primary | `` │ Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, Digital Twin           │ `` | C1 |
| L41 / primary | `` - If a function recurs across clinical document versioning, directory update proposals, and bed state turnover, it reflects an underlying Enterprise Capability (**EC-03 Managed State & Lifecycle**). `` | C2 |
| L46 / supplemental | `` - **Acknowledgement** is a reusable interaction pattern within exchange semantics (EC-08) and operational activity tracking (EC-10), **not** an independent capability. `` | C2 |
| L47 / supplemental | `` - **Assignment and Dispatch** are core execution functions within **EC-10 Activity & Execution**. `` | C2 |
| L55 / primary | `` - **EC-02 Context Management**: Context must be established at ingress (Pylai), maintained during active access (Mneme), propagated across execution units (Ponos), and preserved in audit (Kleio). I… `` | C1 |
| L56 / primary | `` - **EC-06 Policy & Control**: Policy evaluation must guard ingress boundaries, storage persistence interfaces, and presentation gateways alike. `` | C2 |
| L57 / supplemental | `` - **EC-07 Provenance & Traceability**: Attribution and audit evidence must be captured across every boundary hop and state transformation. `` | C2 |
| L70 / primary | `` An Enterprise Capability describes *what* functionality is provided. Subsystems (such as Pylai, Ponos, Mneme, Mnemosyne, Calliope, and Iris) represent *strategic logical component responsibility ce… `` | C1 |
| L80 / primary | `` │  EC-03  Managed State & Lifecycle                                      │ `` | C2 |
| L87 / supplemental | `` │  EC-10  Activity & Execution                                           │ `` | C2 |
| L100 / supplemental | ``   - Resolving and maintaining multi-authority identifier systems and aliases without destructive overwriting. `` | C3 |
| L106 / primary | `` - **Architectural Scope**: Establish, validate, propagate, and preserve the execution, clinical, security, and administrative context within which information and activities have meaning. `` | C3 |
| L110 / primary | ``   - Ensuring context preservation during cross-protocol transformations across disparate healthcare exchange formats and protocols. `` | C3 |
| L114 / primary | `` ### EC-03: Managed State & Lifecycle `` | C2 |
| L115 / primary | `` - **Architectural Scope**: Govern the current and historical state, effective temporal periods, state transition validations, and lifecycle progression of managed integration artifacts and entities. `` | C2 |
| L117 / primary | ``   - Defining and enforcing valid state transition state machines (e.g., `REQUESTED` $\to$ `VALIDATED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`). `` | C2 |
| L119 / primary | ``   - Supporting non-destructive state progression, preserving historical state versions for longitudinal review. `` | C2 |
| L120 / primary | ``   - Rejecting illegal or out-of-order state transitions while accommodating idempotent replay. `` | C2 |
| L124 / primary | `` - **Architectural Scope**: Validate, represent, persist, retrieve, version, and manage governed clinical, administrative, and operational information assets. `` | C2 |
| L127 / primary | ``   - Providing authoritative durable persistence for historical health records and administrative registries. `` | C2 |
| L129 / supplemental | ``   - Governing document versioning, supersession, amendments, and retractions without data loss. `` | C2 |
| L130 / primary | `` - **Architectural Principle**: Enforces the foundational separation between *active distributed state access* and *authoritative durable state preservation* (Axiom AX-05). `` | I |
| L142 / supplemental | `` - **Architectural Scope**: Evaluate and enforce security, consent, privacy, and regulatory policies including authority, access control, and information handling rules. `` | C3 |
| L151 / primary | `` - **Architectural Scope**: Preserve origin attribution, transformation history, execution checkpoints, tamper-evident audit evidence, and end-to-end traceability across the platform. `` | C3 |
| L171 / supplemental | ``   - Ingesting operational and clinical event triggers from external systems and internal state changes. `` | C2 |
| L177 / supplemental | `` ### EC-10: Activity & Execution `` | C2 |
| L178 / supplemental | `` - **Architectural Scope**: Define, instantiate, match, assign, dispatch, supervise, and progress coordinated operational and clinical activity units. `` | C2 |
| L183 / supplemental | ``   - Supervising execution progress, tracking timeouts, managing escalations, and handling activity interruptions. `` | C2 |
| L184 / supplemental | `` - **Decoupling Note**: Free of worker threads, thread pools, execution daemons, or specific workflow runtime engines. `` | C2 |
| L196 / primary | `` - **Architectural Scope**: Ensure concurrency integrity, system resilience, duplicate detection, exception handling, automated recovery, and operational telemetry across platform operations. `` | C2 |
| L199 / supplemental | ``   - Guaranteeing ingress dual-write safety (durable acceptance before emitting positive application ACK). `` | C2 |
| L200 / primary | ``   - Managing structured failure recovery, exception escalation, and dead-letter isolation. `` | C2 |
| L210 / supplemental | ``   - Governing semantic versioning, deprecation, and backward compatibility across schema revisions. `` | C2 |
| L221 / primary | `` \| **EC-03** \| Managed State & Lifecycle \| State machines, temporal validity, lifecycle progression \| State validation, effective periods, non-destructive updates \| `` | C2 |
| L222 / primary | `` \| **EC-04** \| Information Management \| Validation, durable persistence, active distributed state \| Schema validation, durable preservation and recovery, active access \| `` | I |
| L228 / supplemental | `` \| **EC-10** \| Activity & Execution \| Work unit instantiation, assignment, dispatch, supervision \| Matching, dispatching, progress tracking, escalation \| `` | C2 |
| L230 / primary | `` \| **EC-12** \| Operational Assurance \| Resilience, concurrency integrity, duplicate suppression \| Dual-write safety, deduplication, recovery, telemetry \| `` | C2 |
| L240 / supplemental | `` - **Healthcare Need**: Resolving multi-authority identifiers (MRN, national IHI, clinic client IDs) for an incoming patient transaction. `` | C3 |
| L244 / primary | ``   - `EC-03 Managed State & Lifecycle`: Validates identifier active/superseded status. `` | C2 |
| L245 / supplemental | ``   - `EC-07 Provenance & Traceability`: Records origin authority, timestamp, and audit trail. `` | C3 |
| L256 / primary | ``   - `EC-03 Managed State & Lifecycle`: Enforces order state progression (`PLACED` $\to$ `ACCEPTED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`). `` | C2 |
| L257 / supplemental | ``   - `EC-10 Activity & Execution`: Instantiates activity units, monitors timeouts, and supervises fulfillment. `` | C2 |
| L265 / primary | ``   - `EC-03 Managed State & Lifecycle`: Progresses work order state (`CREATED` $\to$ `DISPATCHED` $\to$ `IN_PROGRESS` $\to$ `DONE`). `` | C2 |
| L268 / supplemental | ``   - `EC-10 Activity & Execution`: Executes allocation logic, worker dispatch, and escalation oversight. `` | C2 |
| L278 / primary | `` - **Authoring Pass B (Strategy Components & Resources)**: Evaluates how these capabilities cluster into the strategic logical component responsibility model (Mneme, Mnemosyne, Ponos, Pylai, Calliop… `` | C1 |

### capabilities/ict-foundation-lenses.md

[Source](../capabilities/ict-foundation-lenses.md); 203 lines reviewed; 19 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L13 / primary | `` - **Enterprise Capabilities (EC-01 .. EC-13)** define the *reusable architectural functions* required across healthcare features (e.g., Activity & Execution, Context Management, Managed State). `` | C2 |
| L38 / supplemental | `` - **EC-10 (Activity & Execution)** is evaluated through: `` | C2 |
| L64 / supplemental | `` \| **Infinispan Grid** \| If replaced with Redis or Hazelcast tomorrow, active distributed state access still exists. \| **Technology Realisation** (NOT a capability) \| Technology Architecture (Domain… `` | C2 |
| L65 / primary | `` \| **PostgreSQL Database**\| If replaced with CockroachDB or Oracle tomorrow, authoritative durable persistence still exists. \| **Technology Realisation** (NOT a capability) \| Technology Architecture… `` | C2 |
| L67 / supplemental | `` \| **Apache Camel** \| If replaced with Spring Integration or native routing daemons tomorrow, activity orchestration still exists. \| **Technology Realisation** (NOT a capability) \| Application Archi… `` | C2 |
| L71 / primary | `` \| **Storage & Persistence Services** \| If underlying storage engines change, the architectural discipline of data storage and recovery endures. \| **ICT Foundation Lens (Lens 11)** \| Domain 02 Strat… `` | C2 |
| L83 / supplemental | `` │  05  Process, Workflow & Automation                                    │ `` | C2 |
| L89 / primary | `` │  11  Storage & Persistence Services                                    │ `` | C2 |
| L124 / supplemental | `` #### 05. Process, Workflow & Automation `` | C2 |
| L125 / supplemental | `` - **Focus**: State machine execution engines, long-running orchestration, saga choreography, compensation handling, and event-driven automation. `` | C2 |
| L127 / primary | `` - **Relationship to Strategy**: Shapes realization of `EC-10 Activity & Execution` and `EC-03 Managed State & Lifecycle`. `` | C2 |
| L152 / supplemental | `` - **Relationship to Strategy**: Realizes runtime execution mechanics for `EC-10 Activity & Execution`. `` | C2 |
| L154 / primary | `` #### 11. Storage & Persistence Services `` | C2 |
| L155 / primary | `` - **Focus**: Durable persistence management, transaction and consistency boundaries, write-ahead journaling, tamper-evident audit logging, and distributed storage systems. `` | C2 |
| L156 / primary | `` - **Enabling Question**: *How is committed information durably preserved, recovered after failure, and isolated across tenants?* `` | I |
| L157 / primary | `` - **Relationship to Strategy**: Realizes durable preservation for `EC-04 Information Management` (Mnemosyne). `` | I |
| L180 / primary | `` - **Focus**: Circuit breaking, retry back-off policies, dead-letter queuing, high availability failover, disaster recovery, and chaos engineering. `` | C2 |
| L181 / primary | `` - **Enabling Question**: *How does the platform prevent cascading failures, preserve state during outages, and recover gracefully?* `` | C2 |
| L186 / supplemental | `` - **Enabling Question**: *How are IT operations governed, operational incidents managed, and service commitments maintained?* `` | C2 |

### capabilities/index.md

[Source](../capabilities/index.md); 133 lines reviewed; 4 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L69 / supplemental | ``   5. *Intrinsic / Shared Enablement*: Longitudinal clinical records, health information exchange, clinical collaboration, and workflow coordination. `` | C2 |
| L103 / primary | `` ### Preserved Architectural Principle `` | C3 |
| L104 / supplemental | `` &gt; **A Harmonia-Core capability may enable a Harmonia-Relevant business capability without Harmonia owning or asserting platform authority over that healthcare business domain.** `` | C3 |
| L116 / primary | `` When specific combinations of Enterprise Capabilities and domain-specific behaviours recur coherently across multiple features, they indicate candidate **Strategic Logical Component boundaries** (M… `` | C1 |

### capability-maps/capability-tier-model.md

[Source](../capability-maps/capability-tier-model.md); 254 lines reviewed; 22 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L19 / supplemental | `` Unresolved Capability Tier or ancestry SHALL NOT prevent downstream derivation where the owning Capability or Feature and its relevant responsibility are established. Traceability SHALL reference t… `` | C3 |
| L97 / supplemental | ``   5. *Intrinsic / Shared Enablement*: Longitudinal clinical records, health information exchange, clinical collaboration, and workflow coordination. `` | C2 |
| L129 / supplemental | `` - **Domain-Specific Enabling Behaviour**: The specific clinical or operational logic, context rules, and progression semantics unique to a healthcare domain (e.g., healthcare identifier system rule… `` | C2 |
| L130 / supplemental | `` - **Reusable Enterprise Capabilities**: The horizontal, platform-wide capabilities (EC-01 through EC-13) that execute state management, context propagation, policy evaluation, provenance capture, a… `` | C2 |
| L150 / primary | `` │  - Mneme (Managed Information & Active State)          │ `` | C1 |
| L151 / primary | `` │  - Mnemosyne (Durable Preservation & Recovery)         │ `` | I |
| L152 / primary | `` │  - Ponos (Managed Operational Activity Execution)      │ `` | C1 |
| L154 / supplemental | `` │  - Calliope (Semantic Authority & Conformance)         │ `` | C3 |
| L170 / supplemental | `` In healthcare integration, identifying an individual across disparate clinics, hospitals, and diagnostic centres requires resolving identifiers (MRNs, national healthcare identifiers, local practic… `` | C3 |
| L173 / supplemental | ``   - Healthcare identifier system semantics (e.g., verifying issuing authority jurisdiction, assigning authority namespaces). `` | C3 |
| L179 / primary | ``   - **EC-03 Managed State & Lifecycle**: Governing the lifecycle status (active, superseded, suspended) of identifier records. `` | C2 |
| L180 / supplemental | ``   - **EC-07 Provenance & Traceability**: Recording the authoritative origin, verification timestamp, and issuing system attribution for each identifier. `` | C3 |
| L181 / supplemental | ``   - **EC-06 Policy & Control**: Enforcing default-deny access policies governing which requesting systems may view or resolve specific identifier authorities. `` | C3 |
| L194 / supplemental | ``   A transport-level ACK (e.g., MLLP commit or HTTP 200/202) confirms only that the receiver obtained the bits; it does *not* confirm that the receiving laboratory accepted clinical responsibility f… `` | C2 |
| L200 / primary | ``   - **EC-03 Managed State & Lifecycle**: Tracking the formal Order state progression (`PLACED` $\to$ `RECEIVED` $\to$ `ACCEPTED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`). `` | C2 |
| L201 / supplemental | ``   - **EC-10 Activity & Execution**: Instantiating and executing operational units of work, tracking timeouts, managing escalations, and supervising fulfillment deadlines. `` | C2 |
| L203 / primary | ``   - **EC-12 Operational Assurance**: Guaranteeing dual-write safety, duplicate suppression, and exception recovery across asynchronous boundary handoffs. `` | C2 |
| L212 / supplemental | ``   $$\text{Event} \longrightarrow \text{Entity Context} \longrightarrow \text{Required Activity} \longrightarrow \text{Assignment} \longrightarrow \text{Dispatch} \longrightarrow \text{ACK} \longrig… `` | C2 |
| L213 / supplemental | `` - **Healthcare Service Context Rule**: Where operational activity occurs in the context of a Healthcare Service, that service context must be explicitly associated with the activity/state rather th… `` | C2 |
| L222 / primary | ``   - **EC-03 Managed State & Lifecycle**: Managing the lifecycle of the operational activity (`REQUESTED` $\to$ `ASSIGNED` $\to$ `ACCEPTED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`). `` | C2 |
| L225 / supplemental | ``   - **EC-10 Activity & Execution**: Governing the assignment logic, dispatch mechanism, human notification, timeout monitoring, and escalation triggers. `` | C2 |
| L227 / primary | ``   - **EC-12 Operational Assurance**: Preventing duplicate assignment and ensuring resilient recovery if a dispatched worker does not acknowledge within defined thresholds. `` | C2 |

### capability-maps/index.md

[Source](../capability-maps/index.md); 43 lines reviewed; 0 matching lines.

No requested or supplemental term hits. Navigation and mapping structure inspected; no Mneme/Mnemosyne responsibility allocation.

### courses-of-action/index.md

[Source](../courses-of-action/index.md); 92 lines reviewed; 6 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L63 / primary | `` - *“Deploy Ponos task workers”* — Ponos is a logical component; deployment belongs downstream. `` | C1 |
| L73 / primary | `` \| **COA-01** \| **Boundary Membrane Sovereignty** \| Preserve strict separation between external standards-compliant exchange representations and internal Harmonia-governed operational semantics. \| `… `` | C3 |
| L74 / primary | `` \| **COA-02** \| **Distinct Management and Durable Preservation of Information and State** \| Maintain a clear separation between the governed management of Harmonia information and state and its dura… `` | I |
| L75 / primary | `` \| **COA-03** \| **Meaning-Centric Provenance and Traceability** \| Preserve provenance, authority, attribution and traceability for information-significant and business-significant actions and state … `` | C2 |
| L76 / primary | `` \| **COA-04** \| **Governed Asynchronous Activity Progression** \| Progress multi-stage operational activities through explicit, observable unit-of-work transitions coordinated with entity state, sepa… `` | C2 |
| L77 / supplemental | `` \| **COA-05** \| **Entity-Centred Operational Coordination** \| Coordinate governed information, state and operational activity around real-world healthcare entities where those entities are operation… `` | C2 |

### courses-of-action/strategic-courses-of-action.md

[Source](../courses-of-action/strategic-courses-of-action.md); 192 lines reviewed; 45 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L5 / supplemental | `` This catalogue establishes the six authoritative **Strategic Courses of Action** for Harmonia. Each Course of Action defines a technology-neutral architectural approach that configures Harmonia's r… `` | C3 |
| L16 / primary | `` &gt; **Preserve strict separation between external standards-compliant exchange representations and internal Harmonia-governed operational semantics.** `` | C3 |
| L20 / supplemental | `` - Internal Harmonia processing, context propagation, execution coordination, and state management must never adopt an external exchange representation as their private operational domain model. `` | C2 |
| L28 / primary | ``   - `AX-14` (Preserve Distinctions): Distinctions between external exchange syntax and internal operational meaning are preserved. `` | C3 |
| L41 / primary | `` ### COA-02: Distinct Management and Durable Preservation of Information and State `` | I |
| L44 / primary | `` &gt; **Maintain a clear separation between the governed management of Harmonia information and state and its durable preservation and recovery, allowing runtime management and persistence concerns to … `` | I |
| L47 / supplemental | `` - Runtime management of information, active relationships, context, and state is an operational governance concern requiring high availability, concurrent access, and responsive query navigation. `` | C2 |
| L48 / primary | `` - Authoritative durable state establishment, preservation and recovery atomically establish authoritative state and authoritative version progression, preserve the durable management metadata requi… `` | C2 |
| L49 / primary | `` - The distinction is strictly one of **architectural responsibility**, not a crude division between "ephemeral data" and "durable data". Information managed at runtime frequently represents durable… `` | C2 |
| L53 / primary | ``   - `AX-05` (Active vs Durable State): Application-facing active state access/coordination and authoritative durable state/version establishment remain separate architectural concerns. `` | C2 |
| L54 / primary | ``   - `AX-09` (Ephemeral Operational State): Transient execution states are reconstructable and decoupled from durable health records. `` | C2 |
| L55 / primary | ``   - `AX-11` (High Availability & Responsiveness): Information access must not be blocked by backend persistence latencies or recovery locks. `` | C2 |
| L56 / primary | ``   - `AX-14` (Preserve Distinctions): Preserve the boundary between runtime information governance and storage mechanics. `` | C3 |
| L57 / primary | `` - **Strategic Drivers & Goals**: 24/7 Clinical Operational Continuity, High-Throughput Access, Authoritative Preservation. `` | C3 |
| L60 / supplemental | `` - **Primary Capabilities**: Shapes `EC-03` (State & Lifecycle Governance) and `EC-04` (Information Management & Access). `` | C2 |
| L61 / primary | `` - **Component Boundary Impact**: Explicitly establishes the seam between **Mneme** (application-facing access and active information/state management) and **Mnemosyne** (authoritative durable state… `` | C1 |
| L65 / primary | `` - Technology Architecture (Domain 07) must allow distributed in-memory data fabrics (for runtime state) and durable relational/document stores (for preservation) to be configured and scaled indepen… `` | C2 |
| L72 / primary | `` &gt; **Preserve provenance, authority, attribution and traceability for information-significant and business-significant actions and state changes, while avoiding unnecessary elevation of transient op… `` | C2 |
| L75 / supplemental | `` - Every action that modifies clinical information, asserts authority, alters lifecycle state, or executes an access decision must capture semantic provenance: who, what, when, why, and under whose … `` | C2 |
| L81 / supplemental | ``   - `AX-06` (Explicit Authority): Every state change and action must trace to an explicit, authenticated authority. `` | C2 |
| L84 / primary | ``   - `AX-14` (Preserve Distinctions): Distinguish enduring clinical/business evidence from transient technical logs. `` | C3 |
| L96 / primary | `` ### COA-04: Governed Asynchronous Activity Progression `` | C2 |
| L99 / supplemental | `` &gt; **Progress multi-stage operational activities through explicit, observable unit-of-work transitions coordinated with entity state, separating execution from boundary exchange.** `` | C2 |
| L102 / primary | `` - Work progresses through discrete, observable units of work whose lifecycle state transitions are explicitly tracked and auditable. `` | C2 |
| L103 / primary | `` - Operational progression progresses in lockstep with governed entity state (`AX-16`), preserving explicit uncertainty (`AX-15`) when external responses are pending or failed. `` | C2 |
| L104 / supplemental | `` - Execution progression is strictly decoupled from boundary transport and exchange mechanics: activity execution determines *that* an external interaction is needed, but does not own transport prot… `` | C2 |
| L108 / primary | ``   - `AX-10` (Distribution & Normal Failure): Systems fail normally; activity progression must be resilient, idempotent, and restartable. `` | C2 |
| L109 / primary | ``   - `AX-15` (Preserve Uncertainty): Never assume success or failure; pending, timed-out, or ambiguous outcomes must be explicitly modeled. `` | C3 |
| L110 / supplemental | ``   - `AX-16` (Activity & State Progress Together): Operational activity and managed information state advance in coordinated lockstep. `` | C2 |
| L111 / primary | `` - **Strategic Drivers & Goals**: Resilient Regional Integration, Asynchronous Clinical Decoupling, Bounded Workflow Recovery. `` | C2 |
| L114 / supplemental | `` - **Primary Capabilities**: Shapes `EC-03` (State & Lifecycle Governance), `EC-09` (Event & Subscription Management), and `EC-10` (Activity & Execution Coordination). `` | C2 |
| L115 / primary | `` - **Component Boundary Impact**: Grounds the responsibility of **Ponos** as the execution engine for governed units of work, while strictly enforcing Guardrail G4 (Ponos progresses execution, but d… `` | C1 |
| L118 / supplemental | `` - Application Architecture (Domain 05) and Integration Architecture (Domain 06) implement durable task envelopes (Pragma), resilient messaging (Petasos), and explicit fan-out tracking extensions (e… `` | C2 |
| L125 / supplemental | `` &gt; **Coordinate governed information, state and operational activity around real-world healthcare entities where those entities are operationally significant in their own right and require active ma… `` | C2 |
| L128 / supplemental | `` - Harmonia organizes the coordination of state and ongoing activities around the operational identity of the real-world entity itself. `` | C2 |
| L129 / primary | `` - **The Digital Twin is the architectural construct** through which this entity-centred coordination strategy is realised. A Twin coordinates information, state, and activity across the Mneme/Ponos… `` | C1 |
| L130 / supplemental | `` - **Demand-Driven Lifecycle**: A managed entity does not require a permanently running execution thread or process. Twins are activated when entity-centred operational activity or active monitoring… `` | C2 |
| L131 / supplemental | `` - **Representation $\neq$ Twin**: Simply possessing a database record or FHIR resource does not justify or constitute a Digital Twin. Coordination is justified only when the entity is operationally… `` | C2 |
| L137 / supplemental | ``   - `AX-16` (Activity & State Progress Together): Entity state and active operational workflows are coordinated dynamically. `` | C2 |
| L141 / supplemental | `` - **Primary Capabilities**: Shapes `EC-01` (Managed Entity & Relationship), `EC-02` (Context Management), and `EC-10` (Activity & Execution Coordination). `` | C2 |
| L142 / primary | `` - **Component Boundary Impact**: Validates the **Digital Twin** as an active management construct bridging the **Mneme ↔ Ponos** boundary, while asserting that the Twin is an architectural construc… `` | C1 |
| L182 / primary | `` \| **COA-02: Distinct Management & Preservation of State** \| `AX-05`, `AX-09`, `AX-11`, `AX-14` \| 24/7 Availability, Enduring Information Preservation \| `EC-03`, `EC-04` \| Mneme manages active acces… `` | C1 |
| L184 / primary | `` \| **COA-04: Governed Asynchronous Activity Progression** \| `AX-10`, `AX-15`, `AX-16` \| Asynchronous Integration, Resilient Recovery \| `EC-03`, `EC-09`, `EC-10` \| Units of work track discrete state;… `` | C2 |
| L185 / primary | `` \| **COA-05: Entity-Centred Operational Coordination** \| `AX-01`, `AX-11`, `AX-16` \| Patient-Centric Care, High Horizontal Concurrency \| `EC-01`, `EC-02`, `EC-10` \| Digital Twins coordinate entity s… `` | C1 |
| L192 / supplemental | `` Together, these six Courses of Action provide a comprehensive, technology-neutral strategic framework. They guide how Harmonia realizes its enterprise capabilities, respects its foundational archit… `` | C2 |

### resources/index.md

[Source](../resources/index.md); 87 lines reviewed; 1 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L7 / primary | `` Harmonia references the resource concepts established in **ArchiMate® 3.2** while applying explicit qualification to preserve architectural clarity and prevent the inflation of implementation artef… `` | C3 |

### resources/strategic-resources.md

[Source](../resources/strategic-resources.md); 144 lines reviewed; 13 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L22 / primary | `` \| **National Clinical Terminology Assets (SNOMED CT-AU / AMT)** \| Authoritative clinical vocabularies and ontologies \| External normative clinical vocabularies required for clinical semantic govern… `` | C3 |
| L24 / supplemental | `` \| **Authoritative Healthcare Provider Graph** \| Aggregated graph of practitioners, organisations, and endpoints \| Represents information actively ingested, reconciled, and managed by Harmonia rathe… `` | C3 |
| L27 / supplemental | `` \| **Kleio Audit & Provenance Evidence Trail** \| Durable records of security and operational events \| Represents operational compliance, audit, and provenance data generated by platform execution. \|… `` | C2 |
| L74 / primary | `` - **Architectural Axioms**: `AX-01` (Health-Information Centric), `AX-06` (Explicit Authority), `AX-14` (Preserve Distinctions). `` | C3 |
| L83 / supplemental | `` **National Clinical Terminology Assets** comprise the authoritative, governed clinical vocabularies, concept identifiers, descriptions, and semantic relationships used to capture clinical meaning u… `` | C3 |
| L86 / supplemental | `` Harmonia requires authoritative clinical vocabularies to govern semantic meaning during data ingestion, correlation, and egress: `` | C3 |
| L90 / primary | `` These terminology assets enable Harmonia to evaluate clinical semantic equivalence, enforce conformance rules, and execute meaning-preserving transformations without corrupting clinical intent. `` | C3 |
| L95 / primary | `` - **Architectural Axioms**: `AX-04` (Own Semantics / Use Machinery), `AX-08` (Meaning over Machinery), `AX-14` (Preserve Distinctions). `` | C3 |
| L106 / supplemental | `` - **Candidate Frameworks**: Privacy Act 1988, Australian Privacy Principles (APPs), My Health Record Act 2012, and state/territory health records legislation. `` | C2 |
| L110 / supplemental | `` ### Candidate 2: Authoritative Healthcare Provider Graph `` | C3 |
| L117 / primary | `` - **Strategic Significance Assessment**: The longitudinal clinical record is the core clinical payload governed and preserved by Harmonia. It is the primary subject of Harmonia's information manage… `` | C3 |
| L126 / supplemental | `` - **Candidate Description**: The durable, non-PHI security audit records, provenance assertions, and evidence trails generated during execution. `` | C2 |
| L141 / supplemental | `` - **SR-02 (Australian National Healthcare Directory & Identifier Specifications)** defines *how healthcare participants and endpoints are authoritatively discovered and identified*. `` | C3 |

### strategic-views/index.md

[Source](../strategic-views/index.md); 34 lines reviewed; 3 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L16 / supplemental | `` - **Guardrail Enforcement**: All strategic views continuously reflect the Four Strategic Boundary Guardrails (G1 through G4), maintaining clean separation between managed information/state, operati… `` | C2 |
| L25 / primary | `` 2. [Strategic Logical Component Responsibility Model](logical-component-responsibilities.md): Comprehensive formulation of the 7 candidate logical responsibilities (Mneme, Mnemosyne, Ponos, Pylai, … `` | C1 |
| L27 / primary | `` The [AX-05 state-responsibility reconciliation record](../reviews/ax05-state-responsibility-reconciliation.md) documents the bounded review, statement inventory, and correction of the existing Mnem… `` | C1 |

### strategic-views/logical-component-responsibilities.md

[Source](../strategic-views/logical-component-responsibilities.md); 551 lines reviewed; 167 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L31 / supplemental | `` ### Guardrail G3: Managed Information/State and Managed Activity Remain Distinct `` | C2 |
| L32 / supplemental | `` &gt; **Managed information/state and managed activity remain distinct architectural responsibilities.** `` | C2 |
| L34 / primary | `` - **Mneme** manages active use of what is known and its current active representation. `` | C1 |
| L35 / primary | `` - **Ponos** governs what is happening and how operational activity progresses. `` | C1 |
| L36 / primary | `` - **Mnemosyne** establishes authoritative durable state and authoritative version progression. `` | C1 |
| L37 / supplemental | `` - **Digital Twins** coordinate active information/state management and operational activity for real-world entities without collapsing those distinct responsibilities into each other. `` | Q |
| L39 / primary | `` Under [AX-05](../../../architectural-axioms.md#ax-05-----active-state-and-authoritative-durable-state-are-distinct), operational activity progression, active information/state management, and autho… `` | C1 |
| L45 / primary | `` - **Ponos**: Determines *that* an external interaction is required to progress an activity. `` | C1 |
| L49 / primary | `` Ponos must not acquire bespoke transport machinery or wire protocols merely to progress activity. Pylai must not become a generic transport layer merely because it governs the standards-facing cont… `` | C1 |
| L59 / supplemental | `` 3. **Authority**: Is it unambiguous what information, state, or decisions the component is authoritative for? `` | C2 |
| L68 / primary | `` ### Component 1: Mneme (Managed Information & State Runtime Management) `` | C1 |
| L71 / primary | `` &gt; **Mneme governs and provides runtime management of Harmonia-managed information, relationships, context, and state.** `` | C1 |
| L73 / primary | `` Mneme provides the application-facing access boundary for Harmonia-managed clinical, administrative, and operational entities. It provides distributed runtime access, active relationship navigation… `` | C1 |
| L76 / primary | `` - Mneme governs what Harmonia currently knows about managed entities and their active relationships. `` | C1 |
| L77 / primary | `` - **Architectural Clarification**: Mneme is **not** merely an "ephemeral cache" or a "transient state tier". Information and state managed through Mneme frequently represent enduring, durable busin… `` | C1 |
| L79 / primary | `` #### Anti-Responsibilities (What Mneme Does NOT Own) `` | C1 |
| L80 / primary | `` - Does not establish authoritative durable state or authoritative version progression (owned by Mnemosyne). `` | C1 |
| L81 / primary | `` - Does not own durable historical preservation or cold-start recovery mechanisms (owned by Mnemosyne). `` | C1 |
| L82 / primary | `` - Does not own operational activity progression or task workflow execution (owned by Ponos). `` | C1 |
| L84 / primary | `` - Does not expose raw database tables or persistent storage structures to client applications. `` | C2 |
| L87 / supplemental | `` - **Responsibility**: PASS. Singular focus on runtime management and access of governed information and state. `` | C2 |
| L88 / primary | `` - **Cohesion**: PASS. Information access, active relationships, context correlation, and active state naturally co-vary. `` | C2 |
| L89 / supplemental | `` - **Authority**: PASS. Authoritative for runtime access contracts and active entity state presentation. `` | CS |
| L91 / supplemental | `` - **Exclusion**: PASS. Clear anti-responsibilities regarding durable storage mechanics, workflow execution, and external protocols. `` | C2 |
| L97 / primary | `` ### Component 2: Mnemosyne (Durable Preservation & Recovery) `` | I |
| L100 / primary | `` &gt; **Mnemosyne establishes authoritative durable state and authoritative version progression and provides durable preservation and recovery of Harmonia-managed information and state.** `` | C1 |
| L102 / primary | `` Mnemosyne atomically establishes authoritative state and authoritative version progression and persists the durable management metadata required to interpret that state. It is also responsible for … `` | C1 |
| L105 / primary | `` - Mnemosyne ensures that once information or state is committed, it is durably preserved against loss, system failure, or disaster, and can be reliably recovered. `` | C1 |
| L106 / primary | `` - **Architectural Clarification**: Mnemosyne establishes durable truth; it does **not** own operational activity progression or workflow execution. Ponos progresses operational activity; Mneme mana… `` | C1 |
| L108 / primary | `` #### Anti-Responsibilities (What Mnemosyne Does NOT Own) `` | C1 |
| L109 / primary | `` - Does not govern application-facing runtime access or query presentation (owned by Mneme). `` | C1 |
| L110 / primary | `` - Does not own workflow progression or operational task execution (owned by Ponos). `` | C1 |
| L111 / primary | `` - Does not own active distributed state (owned by Mneme) or entity-centred Digital Twin coordination. `` | C1 |
| L113 / primary | `` - Does not expose its persistence implementation (e.g., JPA entities, SQL schemas) as an application-facing query path. `` | C2 |
| L116 / primary | `` - **Responsibility**: PASS. Singular focus on authoritative durable state establishment, preservation and recovery. `` | C2 |
| L117 / primary | `` - **Cohesion**: PASS. Atomic authoritative persistence, authoritative version progression, version preservation, and recovery mechanics are tightly cohesive. `` | C2 |
| L118 / primary | `` - **Authority**: PASS. Establishes authoritative durable state and authoritative versions, including the durable management metadata and recovery baseline. `` | C2 |
| L120 / primary | `` - **Exclusion**: PASS. Excludes application query APIs, workflow progression, and external protocols. `` | C2 |
| L126 / primary | `` ### Component 3: Ponos (Managed Activity Execution) `` | C1 |
| L129 / primary | `` &gt; **Ponos executes and progresses Harmonia-managed operational activity within governed context.** `` | C1 |
| L131 / primary | `` Ponos is the execution engine responsible for progressing governed operational activities, units of work, and asynchronous clinical workflows through discrete, auditable lifecycle transitions. `` | C1 |
| L134 / primary | `` - Ponos coordinates discrete task units, evaluates workflow step progression, and ensures that operational activity advances in lockstep with governed entity state (`AX-16`). `` | C1 |
| L135 / primary | `` - Ponos models and preserves explicit uncertainty (`AX-15`) during distributed execution. `` | C1 |
| L137 / primary | `` #### Anti-Responsibilities (What Ponos Does NOT Own) `` | C1 |
| L138 / primary | `` - Does not establish authoritative durable information state or authoritative version progression (owned by Mnemosyne), including when activity execution causes information to change. `` | C1 |
| L140 / primary | `` - Does not govern durable preservation or storage management (owned by Mnemosyne). `` | C1 |
| L145 / primary | `` - **Responsibility**: PASS. Singular focus on operational activity execution and workflow progression. `` | C2 |
| L146 / primary | `` - **Cohesion**: PASS. Unit-of-work tracking, state transitions, and asynchronous execution naturally belong together. `` | C2 |
| L147 / supplemental | `` - **Authority**: PASS. Authoritative for operational task progression and execution lifecycle state. `` | CS |
| L149 / primary | `` - **Exclusion**: PASS. Explicitly excludes transport machinery, durable preservation, and external wire protocols. `` | C2 |
| L168 / primary | `` - Does not progress internal business activities or workflows (owned by Ponos). `` | C1 |
| L169 / primary | `` - Does not alter internal managed state destructively (owned by Mneme). `` | C1 |
| L176 / supplemental | `` - **Authority**: PASS. Authoritative for external standards conformance and boundary contract behaviour. `` | C3 |
| L178 / primary | `` - **Exclusion**: PASS. Excludes internal workflow progression, internal state governance, and transport machinery. `` | C2 |
| L184 / supplemental | `` ### Component 5: Calliope (Semantic Authority & Conformance) `` | C3 |
| L189 / primary | `` Calliope acts as the design-time and reference-time semantic authority for Harmonia. It establishes the canonical vocabulary, data structures, and transformation rules that ensure clinical meaning … `` | C3 |
| L197 / primary | `` - Does not own durable clinical persistence (owned by Mnemosyne). `` | C1 |
| L198 / primary | `` - Does not execute operational tasks or workflow logic (owned by Ponos). `` | C1 |
| L204 / supplemental | `` - **Authority**: PASS. Authoritative for canonical information structures and semantic definitions. `` | C3 |
| L206 / supplemental | `` - **Exclusion**: PASS. Excludes runtime transaction mediation, database storage, and workflow execution. `` | C2 |
| L215 / supplemental | `` &gt; **Iris provides contextual human interaction with Harmonia-managed information and activity while remaining non-authoritative for both.** `` | C2 |
| L220 / supplemental | `` - Iris delivers role-tailored, context-sensitive human interaction with clinical records, provider directories, and integration workflows. `` | C2 |
| L222 / primary | `` - **Architectural Clarification**: Iris remains strictly non-authoritative for clinical information and operational activity. Displaying or initiating an action does not transfer authority to the p… `` | C2 |
| L225 / primary | `` - Does not own clinical identity, clinical authority, or state validation (owned by Mneme / Themis). `` | CS |
| L226 / primary | `` - Does not access persistence stores or databases directly (violating Invariant 3). `` | C2 |
| L227 / primary | `` - Does not execute autonomous backend workflows or task progression (owned by Ponos). `` | C1 |
| L228 / supplemental | `` - Does not establish authoritative clinical or audit records. `` | C3 |
| L233 / supplemental | `` - **Authority**: PASS. Authoritative only for human presentation layout and client-side view state; strictly non-authoritative for business/clinical truth. `` | C2 |
| L235 / primary | `` - **Exclusion**: PASS. Excludes backend persistence, server-side workflow progression, and security enforcement. `` | C2 |
| L244 / supplemental | `` &gt; **A Digital Twin is an active management construct associated with a real-world entity and responsible for coordinating the information and operational activity associated with that entity.** `` | C2 |
| L257 / supplemental | ``   - Relevant governed state (e.g., operational status, active availability). `` | C2 |
| L262 / supplemental | `` - **Ward Twin $\neq$ Location**: A Ward Twin coordinates clinical activity and beds across a care facility, drawing context from `Location`, `HealthcareService`, and `Organization`. `` | C2 |
| L265 / supplemental | `` - **Bed Twin $\neq$ Location / Bed Record**: A Bed Twin coordinates physical occupancy, availability, and admission state. `` | C2 |
| L272 / supplemental | `` &gt; *"A Harmonia-managed entity is a candidate for Digital Twin coordination where the entity is operationally significant in its own right and Harmonia must coordinate evolving governed state and op… `` | C2 |
| L275 / primary | `` An entity may be represented and durably preserved by Harmonia without requiring active Twin coordination. A Twin becomes operationally active only when entity-centred coordination of evolving stat… `` | C2 |
| L281 / supplemental | `` 3. **Practitioner Twin**: Healthcare practitioners managing clinical workflows. `` | C2 |
| L288 / supplemental | `` - **Responsibility**: PASS. Singular focus on coordinating information, state, and activity for a specific real-world entity. `` | C2 |
| L289 / supplemental | `` - **Cohesion**: PASS. The entity's state, context, and operational activity naturally belong together. `` | C2 |
| L290 / supplemental | `` - **Authority**: PASS. Authoritative for coordinating that entity's operational lifecycle. `` | CS |
| L292 / primary | `` - **Exclusion**: PASS. Does not own generic execution engines or persistence mechanisms. `` | C2 |
| L293 / primary | `` - **Substitutability**: PASS as a concept, but fails component independence: it relies entirely on Mneme for information/state and Ponos for execution. `` | C1 |
| L301 / primary | `` - An unmanaged persistent actor or permanent background thread; `` | C2 |
| L302 / supplemental | `` - An independent secondary workflow engine; `` | C2 |
| L314 / primary | `` \| **Mneme** \| Governs and provides runtime management of Harmonia-managed information, relationships, context, and state. \| Does not establish authoritative durable state/versions or provide durabl… `` | C1 |
| L315 / primary | `` \| **Mnemosyne** \| Establishes authoritative durable state/versions and provides durable preservation and recovery of Harmonia-managed information and state. \| Does not govern application-facing run… `` | C1 |
| L316 / primary | `` \| **Ponos** \| Executes and progresses Harmonia-managed operational activity within governed context. \| Does not own external transport/connectivity machinery; does not govern durable preservation; … `` | C1 |
| L317 / primary | `` \| **Pylai** \| Governs standards-conformant external representation and interaction semantics across the boundary. \| Does not progress internal operational activities; does not alter internal manage… `` | C2 |
| L318 / primary | `` \| **Calliope** \| Governs semantic definitions, canonical data models, terminology bindings, and conformance rules. \| Does not synchronously mediate runtime transactions; does not own durable clinic… `` | C2 |
| L319 / supplemental | `` \| **Iris** \| Provides contextual human interaction with Harmonia-managed information and activity while remaining non-authoritative. \| Does not own clinical identity or authority; does not access d… `` | C2 |
| L320 / primary | `` \| **Digital Twin** \| Active management construct coordinating information, state, and activity for a specific real-world entity. \| Is NOT an independent deployable platform component; is not a pers… `` | C2 |
| L332 / supplemental | ``                     │ Contextual Human Interaction (Non-authoritative) `` | C3 |
| L337 / primary | ``    │   Mneme   │◄───────►│   Ponos   │ `` | C1 |
| L340 / primary | ``          │ Authoritative State │ Standards Interaction `` | C2 |
| L343 / primary | ``    │ Mnemosyne │         │   Pylai   │ `` | C1 |
| L349 / primary | `` ### Seam 1: Mneme ↔ Mnemosyne `` | C1 |
| L350 / primary | `` - **Architectural Boundary**: Separation of active runtime information/state management from authoritative durable state establishment, preservation and recovery. `` | C2 |
| L352 / primary | ``   - **Mneme**: Governs and provides runtime management of Harmonia-managed information, active relationships, context, and state. `` | C1 |
| L353 / primary | ``   - **Mnemosyne**: Atomically establishes authoritative durable state and authoritative version progression, persists the durable management metadata required to interpret that state, and provides … `` | C1 |
| L354 / primary | `` - **Seam Rule**: The distinction is one of **architectural responsibility**, not "ephemeral vs durable data". State managed through Mneme may represent durable business concepts. Ponos progresses o… `` | C1 |
| L356 / primary | `` ### Seam 2: Mneme ↔ Ponos `` | C1 |
| L357 / supplemental | `` - **Architectural Boundary**: Separation of managed information/state from operational activity execution (Guardrail G3). `` | C2 |
| L359 / primary | ``   - **Mneme**: Manages active use of what is known and its current active representation. `` | C1 |
| L360 / primary | ``   - **Ponos**: Governs what is happening and executes operational activity within governed context. `` | C1 |
| L361 / primary | `` - **Seam Rule**: Ponos never updates clinical entity state arbitrarily; it requests state transitions through governed contracts. Conversely, Mneme never progresses workflow activities or manages t… `` | C1 |
| L363 / primary | `` ### Seam 3: Ponos ↔ Digital Twin `` | C1 |
| L364 / supplemental | `` - **Architectural Boundary**: Generic activity execution engine vs. entity-centred coordination semantics. `` | C2 |
| L366 / primary | ``   - **Ponos**: Provides the generic execution engine, task scheduler, unit-of-work progression, and failure handling. `` | C1 |
| L367 / primary | ``   - **Digital Twin**: Coordinates information, state, and activity for a *specific real-world entity* across the Mneme/Ponos seam. `` | C1 |
| L368 / primary | `` - **Seam Rule**: **Ponos executes; the Digital Twin coordinates for an entity.** The Digital Twin is not a second workflow engine; it delegates execution mechanics to Ponos and retrieves/updates en… `` | C1 |
| L370 / primary | `` ### Seam 4: Pylai ↔ Mneme `` | C1 |
| L371 / supplemental | `` - **Architectural Boundary**: External standards-conformant exchange vs. internal managed meaning and state. `` | C2 |
| L374 / primary | ``   - **Mneme**: Governs Harmonia-managed information, relationships, context, and state. `` | C1 |
| L375 / primary | `` - **Seam Rule**: This seam represents the transition between **external standards meaning** and **Harmonia-governed meaning/state**. It is not a statement about network packets or wire bytes. Pylai… `` | C1 |
| L378 / supplemental | `` - **Architectural Boundary**: Semantic design authority vs. operational runtime execution. `` | C3 |
| L381 / primary | ``   - **Runtime Components (Pylai, Mneme, Ponos)**: Consume published semantic definitions and execute runtime validation. `` | C1 |
| L382 / supplemental | `` - **Seam Rule**: Calliope is the design authority; it does not synchronously mediate runtime transactions. Schemas and terminology mappings are published and distributed for local evaluation across… `` | C3 |
| L384 / primary | `` ### Seam 6: Iris ↔ Mneme / Ponos `` | C1 |
| L387 / supplemental | ``   - **Iris**: Provides contextual human interaction, presentation rendering, and human task initiation while remaining strictly non-authoritative. `` | C3 |
| L388 / primary | ``   - **Mneme & Ponos**: Provide governed application-facing information/state access (Mneme) and operational activity execution (Ponos). Authoritative durable state and authoritative version establi… `` | C1 |
| L389 / supplemental | `` - **Seam Rule**: Iris never writes directly to databases or caches. It queries information through defined application-facing interfaces and initiates workflows via governed execution requests. `` | C2 |
| L400 / supplemental | `` + [EC-03 State & Lifecycle Governance] `` | C2 |
| L404 / primary | ``     └──► MNEME (Managed Information & State Runtime Management) `` | C1 |
| L406 / supplemental | `` [EC-03 State & Lifecycle Governance] `` | C2 |
| L409 / primary | ``     └──► MNEMOSYNE (Authoritative Durable State Establishment, Preservation & Recovery) `` | C1 |
| L412 / supplemental | `` + [EC-03 State & Lifecycle Governance] `` | C2 |
| L414 / supplemental | `` + [EC-10 Activity & Execution Coordination] `` | C2 |
| L416 / primary | ``     └──► PONOS (Managed Activity Execution) `` | C1 |
| L427 / supplemental | ``     └──► CALLIOPE (Semantic Authority & Conformance) `` | C3 |
| L435 / supplemental | `` + [EC-03 State & Lifecycle Governance] `` | C2 |
| L436 / supplemental | `` + [EC-10 Activity & Execution Coordination] `` | C2 |
| L437 / primary | ``     └──► DIGITAL TWIN (Entity-Centred Coordination Construct across Mneme/Ponos Seam) `` | C1 |
| L461 / supplemental | ``              └──────────┬──────────┘               │    Authoritative)   │ `` | C3 |
| L468 / supplemental | ``        [ SEMANTIC       │      [ INFORMATION & STATE ]        │     [ ACTIVITY `` | C2 |
| L469 / supplemental | ``        AUTHORITY ]      │            ┌───────────┐            │    PROGRESSION ] `` | C3 |
| L470 / primary | ``      ┌─────────────┐    │            │   MNEME   │◄───────────┘    ┌───────────┐ `` | C1 |
| L471 / primary | ``      │  CALLIOPE   │    │            │  Managed  │                 │   PONOS   │ `` | C1 |
| L473 / supplemental | ``      │ Definitions │    │            │  & State  │                 │ Activity  │ `` | C2 |
| L477 / primary | ``                         │                  │ Authoritative State Seam    │ `` | C2 |
| L480 / primary | ``                         │            │ MNEMOSYNE │                       │ `` | C1 |
| L481 / supplemental | ``                         │            │ Durable   │                       │ `` | C2 |
| L482 / supplemental | ``                         │            │ State &   │                       │ `` | C2 |
| L483 / supplemental | ``                         │            │ Versions  │                       │ `` | C2 |
| L484 / primary | ``                         │            │Preservation                       │ `` | C3 |
| L485 / primary | ``                         │            │ & Recovery│                       │ `` | C2 |
| L493 / primary | ``                                       │  Mneme/Ponos Seam   │ `` | C1 |
| L507 / supplemental | ``     subgraph Semantics ["Semantic Authority"] `` | C3 |
| L508 / supplemental | ``         Calliope["&lt;b&gt;Calliope&lt;/b&gt;&lt;br/&gt;Semantic Authority & Conformance&lt;br/&gt;&lt;i&gt;Canonical schemas, terminology bindings, conformance governance&lt;/i&gt;"] `` | C3 |
| L512 / supplemental | ``         Iris["&lt;b&gt;Iris&lt;/b&gt;&lt;br/&gt;Contextual Human Interaction&lt;br/&gt;&lt;i&gt;Contextual human presentation and discovery (non-authoritative)&lt;/i&gt;"] `` | C3 |
| L515 / supplemental | ``     subgraph InformationAndState ["Information & State Governance"] `` | C2 |
| L516 / primary | ``         Mneme["&lt;b&gt;Mneme&lt;/b&gt;&lt;br/&gt;Managed Information & State Runtime Management&lt;br/&gt;&lt;i&gt;Runtime access, relationship navigation, context & active state&lt;/i&gt;"] `` | C1 |
| L517 / primary | ``         Mnemosyne["&lt;b&gt;Mnemosyne&lt;/b&gt;&lt;br/&gt;Authoritative Durable State Establishment, Preservation & Recovery&lt;br/&gt;&lt;i&gt;Atomic authoritative state/version establishment and durable management metadata&lt;/i&gt;"] `` | C1 |
| L520 / primary | ``     subgraph Execution ["Operational Activity Progression"] `` | C2 |
| L521 / primary | ``         Ponos["&lt;b&gt;Ponos&lt;/b&gt;&lt;br/&gt;Managed Activity Execution&lt;br/&gt;&lt;i&gt;Governed unit-of-work execution and workflow progression&lt;/i&gt;"] `` | C1 |
| L525 / supplemental | ``         DigitalTwin["&lt;b&gt;Digital Twin&lt;/b&gt;&lt;br/&gt;&lt;i&gt;Active Management Construct&lt;/i&gt;&lt;br/&gt;Coordinates information, state, and activity for real-world entities"] `` | C2 |
| L528 / primary | ``     Iris --- Mneme `` | C1 |
| L529 / primary | ``     Iris --- Ponos `` | C1 |
| L530 / primary | ``     Pylai --- Mneme `` | C1 |
| L531 / primary | ``     DigitalTwin --- Mneme `` | C1 |
| L532 / primary | ``     DigitalTwin --- Ponos `` | C1 |
| L533 / primary | ``     Mneme --- Mnemosyne `` | C1 |
| L535 / primary | ``     Calliope --- Mneme `` | C1 |
| L544 / supplemental | `` 2. **Calliope** establishes authoritative semantic definitions without runtime transaction bottlenecks. `` | C3 |
| L545 / primary | `` 3. **Mneme** governs runtime management of information, context, and state. `` | C1 |
| L546 / primary | `` 4. **Mnemosyne** establishes authoritative durable state and authoritative version progression and provides durable preservation and recovery without exposing direct persistence paths. `` | C1 |
| L547 / primary | `` 5. **Ponos** progresses operational units of work and workflows without acquiring transport machinery. `` | C1 |
| L548 / supplemental | `` 6. **Iris** delivers non-authoritative contextual presentation. `` | C3 |
| L549 / primary | `` 7. **The Digital Twin** acts as an active management construct coordinating entity-centred state and activity across the Mneme/Ponos seam. `` | C1 |

### strategic-views/strategic-value-streams.md

[Source](../strategic-views/strategic-value-streams.md); 304 lines reviewed; 69 matching lines.

| Line / scan | Statement locator (excerpt) | Classification |
| :--- | :--- | :--- |
| L15 / supplemental | `` A strategic value stream is not a technical workflow, a message integration pipeline, or a sequence of software component interactions. Value streams model the progressive transformation of healthc… `` | C2 |
| L31 / primary | `` In alignment with Strategic Guardrails **G1** (*Reusable Capability $\neq$ Centralised Service*) and **G2** (*Component Boundaries Follow Architectural Responsibility*), value stream stages are **n… `` | C1 |
| L33 / primary | `` Such mappings confuse stakeholder value with system execution topologies. Value streams are enabled by Business Capabilities and Enterprise Capabilities; those capabilities are in turn collaborativ… `` | C1 |
| L44 / supplemental | `` \| **VS-02** \| Unqualified Information $\to$ Governed, Consumable Information \| Transforms information with incomplete or unverified structure, meaning, or authority into governed information that c… `` | C3 |
| L45 / supplemental | `` \| **VS-03** \| Operational Need $\to$ Coordinated Activity $\to$ Resolved Outcome \| Transforms an operational healthcare need into governed, coordinated activity aligned with entity state. \| `` | C2 |
| L61 / primary | ``     S2 --&gt; S3["S3: Meaning Established & Preserved"] `` | C3 |
| L69 / supplemental | ``    - *Stakeholder State*: Clinical observations, pathology reports, diagnostic imaging results, medication orders, discharge summaries, or encounter notes occur across distributed care settings and… `` | C2 |
| L72 / primary | ``    - *Stakeholder State*: Subject-of-care identity is established to the level required for safe association of the information, preserving source identifiers, provenance, confidence, and ambiguity… `` | C2 |
| L74 / primary | `` 3. **S3 — Meaning Established & Preserved**: `` | C3 |
| L75 / primary | ``    - *Stakeholder State*: Clinical concepts, classifications, and terminologies are mapped to canonical standards (e.g., SNOMED CT-AU, AMT) while strictly preserving original source clinical intent… `` | C2 |
| L78 / supplemental | ``    - *Stakeholder State*: Disparate episodic facts, temporal observations, diagnostic series, and encounter histories are synthesized into a cumulative, longitudinal patient-centred record. `` | C2 |
| L81 / supplemental | ``    - *Stakeholder State*: The longitudinal clinical picture is made accessible in a contextual, consumable representation, structured for clinical review, clinical decision support, or standards-co… `` | C2 |
| L84 / supplemental | `` #### Clinical Authority Boundary Guardrail `` | C3 |
| L86 / primary | `` Harmonia provides **governed, vendor-neutral longitudinal clinical representation and preservation**. Harmonia does **not** claim originating clinical authority for source clinical facts. The origi… `` | C3 |
| L90 / primary | `` - **Motivational Axioms**: `AX-01` (Patient-Centricity), `AX-02` (Open Standards), `AX-03` (Canonical Representation), `AX-04` (Semantic Preservation), `AX-06` (Provenance & Attribution), `AX-08` (… `` | C3 |
| L95 / primary | `` - **Strategic Courses of Action**: `COA-01` (Boundary Membrane Sovereignty), `COA-02` (Distinct Management and Durable Preservation), `COA-03` (Meaning-Centric Provenance and Traceability). `` | C2 |
| L103 / supplemental | `` Information arriving at the healthcare enterprise boundary is frequently incomplete, syntactically inconsistent, ambiguous in authority, or lacking explicit privacy controls. Consuming or publishin… `` | C3 |
| L105 / supplemental | `` The strategic value created by **VS-02** is the progressive **qualification, governance, policy attachment, and durable integrity assurance of information**, transforming raw external data into tru… `` | C2 |
| L111 / supplemental | ``     S3 --&gt; S4["S4: Durably Available Info"] `` | C2 |
| L118 / supplemental | ``    - *Stakeholder State*: Raw, unvalidated data streams, documents, or transactions arrive at the enterprise membrane with unverified structural conformance, incomplete provenance, and unverified a… `` | C2 |
| L121 / supplemental | ``    - *Stakeholder State*: Data structures are validated against canonical schemas, syntactically parsed, and reconciled into well-defined, verifiable information models. `` | C2 |
| L124 / supplemental | ``    - *Stakeholder State*: Information is evaluated against security policies, organizational authority boundaries, consent directives, and confidentiality classifications under default-deny governa… `` | C2 |
| L125 / supplemental | ``    - *Value Generated*: Information carries explicit, enforceable access policies and privacy guardrails, ensuring that sensitive healthcare data cannot be disclosed without legitimate authority. `` | C3 |
| L126 / supplemental | `` 4. **S4 — Durably Available Information**: `` | C2 |
| L127 / primary | ``    - *Stakeholder State*: Information is preserved with verifiable integrity, non-destructive versioning, and comprehensive provenance, remaining resiliently queryable and recoverable. `` | C2 |
| L130 / supplemental | ``    - *Stakeholder State*: Governed information is projected into standards-compliant external representations (such as FHIR R5 Core and Australian profiles) across explicit egress boundaries. `` | C2 |
| L137 / primary | `` $$\text{Aggregation} \longrightarrow \text{Processing} \longrightarrow \text{Persistence} \longrightarrow \text{Publishing}$$ `` | C2 |
| L139 / supplemental | `` **Crucial Distinction**: This behavioural pattern describes the internal activity contributing to the transformation. It must **not** be substituted for the value stream stage names. The value stre… `` | C2 |
| L143 / supplemental | `` - **Motivational Axioms**: `AX-02` (Open Standards), `AX-03` (Canonical Representation), `AX-05` (State Separation), `AX-07` (Default-Deny Security), `AX-09` (Non-Destructive Evolution), `AX-11` (C… `` | C2 |
| L148 / primary | `` - **Strategic Courses of Action**: `COA-01` (Boundary Membrane Sovereignty), `COA-02` (Distinct Management and Durable Preservation), `COA-03` (Meaning-Centric Provenance and Traceability), `COA-06… `` | C2 |
| L152 / supplemental | `` ### VS-03: Operational Need → Coordinated Activity → Resolved Outcome `` | C2 |
| L156 / supplemental | `` Healthcare delivery involves dynamic, distributed operational coordination—from processing an urgent laboratory order, to coordinating an inpatient bed transfer, managing clinical task escalation, … `` | C2 |
| L158 / supplemental | `` The strategic value created by **VS-03** is the **governed, coordinated progression of operational activity together with the corresponding state of affected real-world healthcare entities** (`AX-1… `` | C2 |
| L162 / supplemental | ``     S1["S1: Operational Need Identified"] --&gt; S2["S2: Required Activity Established"] `` | C2 |
| L163 / supplemental | ``     S2 --&gt; S3["S3: Activity Coordinated"] `` | C2 |
| L166 / supplemental | ``     S5 --&gt; S6["S6: Affected Entity State Progressed"] `` | C2 |
| L172 / supplemental | ``    - *Stakeholder State*: A healthcare operational trigger occurs (e.g., patient admission, clinical order placed, diagnostic test requested, bed turnover required, specialist referral initiated). `` | C2 |
| L174 / supplemental | `` 2. **S2 — Required Activity Established**: `` | C2 |
| L175 / supplemental | ``    - *Stakeholder State*: The activity required to address the operational need is established together with its relevant context, expected outcome, and completion conditions. `` | C2 |
| L177 / supplemental | `` 3. **S3 — Activity Coordinated**: `` | C2 |
| L178 / supplemental | ``    - *Stakeholder State*: Required activity is coordinated with the appropriate people, services, resources, and affected real-world entities. `` | C2 |
| L181 / supplemental | ``    - *Stakeholder State*: Execution of the activity advances through observable, auditable lifecycle states with active oversight, milestone tracking, and escalation management. `` | C2 |
| L184 / supplemental | ``    - *Stakeholder State*: Clinical or operational results are achieved, verified, and recorded with full attribution and provenance. `` | C2 |
| L185 / supplemental | ``    - *Value Generated*: Activity resolution is verified against the original intent, establishing reliable evidence of care delivery. `` | C2 |
| L186 / supplemental | `` 6. **S6 — Affected Entity State Progressed**: `` | C2 |
| L187 / primary | ``    - *Stakeholder State*: The state of affected real-world entities (e.g., patient clinical journey, bed occupancy status, practitioner assignment, care team schedule) advances to its next governed… `` | C2 |
| L188 / supplemental | ``    - *Value Generated*: The physical and organizational reality of the healthcare facility stays synchronized with system records, eliminating state drift and coordination failures. `` | C2 |
| L192 / primary | `` In accordance with architectural principles, execution constructs (such as Ponos execution engines, Digital Twin active threads, worker pools, messaging queues, Work Orders, To Dos, and FHIR Tasks)… `` | C1 |
| L196 / supplemental | `` - **Motivational Axioms**: `AX-01` (Patient-Centricity), `AX-06` (Provenance & Attribution), `AX-10` (Asynchronous Operational Progression), `AX-11` (Component Responsibility), `AX-15` (Explicit Un… `` | C2 |
| L198 / primary | `` - **Business Capabilities**: `BC-05` (Healthcare Resource & Facility Management), `BC-09` (Care Coordination & Workflow), `BC-10` (Referral & Order Management), `BC-14` (Secure Clinical Communicati… `` | C2 |
| L199 / supplemental | `` - **Business Enabling Contexts**: View 1 (Ingress & Processing), View 5 (Operational Activity & Workflow). `` | C2 |
| L200 / supplemental | `` - **Enterprise Capabilities**: `EC-01` (Identifier Resolution), `EC-02` (Context Management), `EC-05` (Entity State Coordination), `EC-09` (Activity Lifecycle Management), `EC-10` (Task Envelope Ma… `` | C2 |
| L201 / primary | `` - **Strategic Courses of Action**: `COA-04` (Governed Asynchronous Activity Progression), `COA-05` (Entity-Centred Operational Coordination), `COA-06` (Collaborative Cross-Cutting Capability Realis… `` | C2 |
| L225 / supplemental | ``    - *Stakeholder State*: Clinically relevant information, historical observations, diagnostic findings, care history, and other governed clinical knowledge exist across the healthcare ecosystem. `` | C2 |
| L228 / supplemental | ``    - *Stakeholder State*: Authorized queries, alerts, and subscriptions discover clinical facts pertinent to an active patient problem, clinical event, or multidisciplinary consultation. `` | C2 |
| L231 / supplemental | ``    - *Stakeholder State*: Participant identities, clinical roles, care team relationships, consent policies, and default-deny governance rules are evaluated prior to disclosing clinical information. `` | C2 |
| L234 / supplemental | ``    - *Stakeholder State*: Longitudinal patient timeline context, active problems, current medication regimens, allergies, and relevant diagnostic findings are bound to the collaborative space. `` | C2 |
| L237 / supplemental | ``    - *Stakeholder State*: Verified clinical summaries, trends, and diagnostic reports are rendered seamlessly alongside collaborative communication channels without exposing internal platform mecha… `` | C2 |
| L240 / supplemental | ``    - *Stakeholder State*: Treating clinicians and multidisciplinary care team members engage in timely, context-rich deliberation, arriving at informed, coordinated clinical care decisions. `` | C2 |
| L243 / supplemental | `` #### Boundary Guardrail: Clinical Authority & Expertise in Collaboration `` | C3 |
| L247 / supplemental | `` 2. **Harmonia is not the clinical decision-maker**: Clinical responsibility and decision-making authority rest entirely with the attending clinicians and care teams. `` | C3 |
| L248 / supplemental | `` 3. **Harmonia does not originate clinical assertions during collaboration**: Collaborative dialogue (e.g., in Matrix/Agora channels) does not automatically become an authoritative clinical record. … `` | C3 |
| L249 / supplemental | `` 4. **Clinical statements require source attribution**: Any clinical order or diagnostic finding resulting from collaboration must be explicitly committed through governed clinical workflows with so… `` | C2 |
| L253 / primary | `` - **Motivational Axioms**: `AX-01` (Patient-Centricity), `AX-04` (Semantic Preservation), `AX-06` (Provenance & Attribution), `AX-07` (Default-Deny Security), `AX-11` (Component Responsibility), `A… `` | C3 |
| L256 / supplemental | `` - **Business Enabling Contexts**: View 2 (Information Store & Query), View 3 (Security & Audit), View 4 (Cross-Enterprise Interoperability), View 5 (Operational Activity & Workflow). `` | C2 |
| L268 / supplemental | `` Harmonia R1.x/R2.x restricts its strategic value streams strictly to VS-01 through VS-04. The model does not attempt to map every granular workflow across the global healthcare industry. Future rel… `` | C2 |
| L274 / supplemental | `` $$\text{Ingest Trigger} \longrightarrow \text{Transport \& Deduplicate} \longrightarrow \text{Orchestrate Erga} \longrightarrow \text{Update Clinical State} \longrightarrow \text{Dispatch Egress / … `` | C2 |
| L278 / primary | `` 2. It represents internal runtime component sequencing, message mechanics, and database persistence operations. `` | C2 |
| L296 / primary | ``     COA --&gt; COMP["Strategic Logical Responsibilities&lt;br/&gt;Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, Digital Twin"] `` | C1 |
