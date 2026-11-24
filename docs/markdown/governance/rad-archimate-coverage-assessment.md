<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# RAD → ArchiMate publication coverage assessment

**Assessment snapshot: 2026-10-16, installed in the authorised release bundle.** The [release authority](../../../architecture-release.json) supplies the current release to generated evidence; the [release policy](architecture-release-policy.md) governs increments. Architectural findings below remain unresolved. Read with the [specification](rad-archimate-model-mapping.md), [catalogue](rad-archimate-mapping-catalogue.md) and [incremental plan](rad-archimate-publication-plan.md).

## 1. Evidence and scope

The original discovery snapshot fingerprinted 124 RADS source files, including four explicitly candidate scratchpad TeX publications. The regenerated inventory also fingerprints the newly established release policy; its `summary.source_files` is the current census. The four mapping documents and the inventory itself are excluded to avoid self-reference. The machine-readable [source inventory](rad-archimate-source-inventory.json) records paths, hashes, source locators, capture assertions and proposed representations. A file census is not a claim that every prose statement is a separate architectural construct or has been individually adjudicated. Sources were inspected progressively through the axioms, completion programme, Motivation/Strategy catalogues, Business metamodel/reference capture, Information metamodel/families/patterns and Domain05 responsibility/traceability material. Decisions and retained uncertainties were read as assertion-specific qualifications, not overridden by directory placement.

The attached [Domain03→04 traceability framework](../04-information-architecture/traceability/domain03-traceability.md) supplies the governing responsibility derivation. The Business reference capture is a navigation/validation representation whose cited RAD sources remain authoritative. The [previous Reference-ID reconciliation](../../archimate/capability-architecture/reference-id-reconciliation-report.md) supplies the per-element 228-object reconciliation and existing 14 exact EC matches. Those matches were independently checked against the current source and immutable fixture for the Documentation candidate.

Current programme standing matters: Domain03 and Domain04 are semantically complete for agreed R1/R2 scope with retained uncertainty; Domain05 A4 is accepted as the Step-B candidate baseline, B2.1 is PASS, and B2.2/Steps C–E/final Solution Architecture are not completed. Semantic sufficiency is not the same as full ID allocation, graph density, automation readiness or final implementation approval.

## 2. Already represented

| Measure | Proven baseline publication | Documentation release bundle |
| --- | ---: | ---: |
| Semantic Capabilities | 228 | 228 |
| Composition relationships | 189 | 189 |
| Baseline Business / BEC / Enterprise elements | 52 / 162 / 14 | Unchanged |
| Exact demonstrated RAD Reference-ID matches | 14 EC identities | Same 14 |
| Unresolved RAD identity crosswalks | 214 | 214 |
| Capability Documentation fields populated | 0 | 14 |
| New semantic identities or relationships | 0 | 0 |
| Generated capability Views / retained diagnostic | 4 / 1 | Same Views and visual IDs |

The four Views remain Landscape (39 nodes/0 connections), Business (52/34), Business Enabling (162/155), Enterprise (14/0). The baseline establishes no Enterprise hierarchy. The original import diagnostic is retained. This release creates no diagram beyond those already generated.

The baseline is the proven Archi 5.9 import representation and must remain immutable evidence. Successful import does not establish that its unreferenced capabilities match current RAD identities or classifications. The original fixture SHA-256 is `dbcf7b41ad7909fe27c9babaefa6e7c07810f8518558c672732f394f910efa80`. Its protected semantic-content digest is `f7f3e0723066b7cdd2bc582b82f53eb65f5ec3731b4b3b68e2c063d3ff597db4`.

## 3. Governed construct census and readiness

Counts below have different scopes and overlap. They must not be summed into a purported total architecture element count.

| Source population | Count / finding | Publication implications |
| --- | --- | --- |
| Motivation/Strategy selected source sections | 71: 6 enterprise stakeholders, 5 external authorities, 7 Drivers, 5 Assessments, 6 Goals, 3 Outcomes, 18 Axioms, 5 foundational Requirements, 3 external Constraints, 6 Courses of Action, 3 Resources, 4 Value Streams | Native concepts are mostly clear; not all section locators are governed IDs. No new objects generated. |
| Already named IDs within that population | 42: AX, REQ-FND, CST-EXT, COA, SR, VS and O identifiers | Suitable for a reviewed identity register/source-extraction slice; type compatibility does not approve new relationships. Others require explicit allocation/crosswalk, not name hashes. |
| Business capture elements | 705: 269 Capability records (including 3 candidate BC concerns), 137 Features, 147 Functions, 133 Services, 19 Processes | Existing assertions and keys preserved; all require publication identity decisions before semantic allocation. |
| Established Capability identities in RAD | 190 BC, 76 BEC, 14 EC = 280 | Only 14 have verified identity correspondence to the 228 Exchange elements. |
| Established BC hierarchy | 18 CT1, 39 CT2, 133 CT3; 172 captured parent edges | Deterministic source hierarchy ready in principle, but no authority to replace 52 baseline BC objects or invent a crosswalk. |
| Business Enabling framing | All 76 BEC tiers/complete ancestry unresolved; all 137 Feature frames/complete ancestry unresolved in current capture | Preserve responsibility/local Feature IDs. Do not make catalogue contexts CT2/CT3 or owners CT3 to satisfy grammar. |
| Complete Canonical IDs in Business capture | 0 of 705 | Missing canonical allocation is explicit. It is not permission to turn record keys, labels or ordinals into IDs. |
| Local IDs in Business capture | 137 Feature local IDs; 568 records without a local ID | Preserve FEAT spelling exactly. A local ID does not establish complete ancestry or a full structural Canonical ID. |
| Capture summaries | 652 present / 53 absent: 50 Capability records and 3 Processes lack summaries | Summary absence is a documentation-review lead; inspect authoritative source sections before declaring a missing governed definition. No summaries synthesised. |
| Captured relationships | 462: 440 established / 22 unresolved | Unresolved 11 realises_feature and 11 associated_feature assertions remain findings, not graph edges. Source predicates do not all map to one native edge. |
| Established relationship breakdown | 172 decomposition, 125 realises_feature, 108 associated_feature, 16 governed_process, 10 enabled_by, 5 participating_function, 1 responsibility_parent, 1 contributes_feature, 1 uses_process, 1 exposes_process | Do not conflate associated/contributes/realises or infer a mandatory Service/Process/Function tree. |
| Catalogue contexts | 73: 4 link to independently established Capabilities; 69 lack standalone identity | 69 descriptions are not missing Capability nodes to auto-create. |
| Presentation groups | 10: five BC regions and five BEC natural views | Presentation only. Three assurance BECs have no assigned contextual view. |
| Business participant definition inventory | 34 Business Roles, 69 Business Interactions, 7 Collaborations; 7 Actor categories separately | Categories are not seven actual enterprise participants; role families and examples are not new identities. Exact identity allocation remains required. |
| Information families | 11 family files; 57 selected principal-concept section candidates | A review/navigation count, not 57 approved objects. Sections include properties, partial/candidate meanings, repeated concepts and G2 distinctions. |
| Domain05 component register | 49 C-key rows | Mixed software, information, interface, behaviour, control and documentary constructs; instance dispositions in catalogue. Not 49 software components. |
| Domain05 feature map | 48 rows / 47 distinct F labels | Duplicate F46 is an identity/navigation conflict; solution-feature labels are not Strategy FT IDs. |
| Domain05 data/middleware maps | 30 D rows / 19 M rows | Mixed bundles, not 30 new DataObjects or 19 components; software/data/interaction facets need explicit identity/source contracts. |
| Technology/platform and Migration | No complete accepted first-class topology/Plateau/Gap/WorkPackage catalogue found in inspected RADS | Relevant constraints and direction exist, but no authority for converting legacy runtime manifests/plans wholesale. Count is unestablished, not zero architecture. |

The machine inventory lists every captured Business record/relationship/context, each selected Motivation/Strategy definition section, participant definition, family section candidate and every C/F/D/M row. It preserves the exact source assertions/locators and the proposed representation or adjudication gate. It is not a new canonical model and is not consumed by the capability Documentation publisher.

## 4. Exact Documentation eligibility

All EC-01–EC-14 have one unique existing Reference ID and exactly one authoritative definition in [enterprise-capabilities.md](../02-strategy/capabilities/enterprise-capabilities.md). There are no duplicate Reference IDs or conflicting identity assignments among those 14. Their XML names retain an EC prefix; the governed titles do not. This is a label difference with exact ID authority, not a fuzzy match. The publication leaves names unchanged.

The [explicit manifest](../../archimate/capability-architecture/documentation-map.json) records each existing XML identifier, Reference ID, Exchange label, governed title, source heading and description hash. A document-wide fingerprint and per-description fingerprints reject source drift. Documentation uses the complete section body, not just Architectural Scope: EC-12/14 responsibility separation, EC-14 non-allocation and conceptual-sequence qualifications remain included.

The 214 other baseline elements have no existing Reference ID or demonstrated repository UUID crosswalk. Of these, the earlier audit found 111 exact same-category label coincidences and 103 without such a label. Both sets remain **unmapped**; even exact label equality is not identity authority. No further Documentation is eligible. No absence of a similar name proves absence of the semantic responsibility under another governed definition.

## 5. Conflicts, ambiguity and source governance

| Finding | Evidence / practical consequence | Required resolution |
| --- | --- | --- |
| Release governance established by owner decision | Previous baseline 1.0.0; this authorised bundle prepares 1.0.1. Root `architecture-release.json` and the [release policy](architecture-release-policy.md) establish the single mechanism. Maven `1.0.0-SNAPSHOT`, historical branches and R1/R2 scope retain separate meanings. | Prior procedural blocker resolved by explicit governance authority; architectural/publication findings below remain unchanged. |
| Proven Exchange vs current RAD inventory | Baseline 52 BC/162 BEC/14 EC versus RAD 190 established BC/76 BEC/14 EC; only 14 exact crosswalks. At least 138 current BC identities cannot be individually represented by only 52 BC objects, but exact absent objects cannot all be identified safely. | Separate semantic-generation/source-governance reconciliation. Preserve baseline IDs and evidence; no JSON-to-XML replacement. |
| 266 current BC/BEC identities lack verified representation | Current source identities cannot be marked represented merely from label overlap. Their per-record listing is retained in the previous reconciliation. | Authorised ID crosswalk or explicit identity adjudication; “unverified representation” does not mean all are physically absent. |
| Duplicate F46 documentary ID | [feature-map rows](../05-information-systems-architecture/candidate-feature-map.md): Omilia operations/client experience and Shared Security and Assurance Nomenclature share F46. | Disambiguate governed navigation/reference history; do not silently renumber/merge or allocate semantic IDs from F46. |
| C/F/D/M keys are documentary | [Domain05 authority](../05-information-systems-architecture/README.md) explicitly declares navigation role. | Approve publication identity crosswalks without calling those keys Canonical IDs. |
| Health Information Exchange name conflict | Separate established BC CT2 and BEC in [Business reference capture](../03-business-architecture/reference-model/README.md) violate global name uniqueness; names do not establish equivalence. | Architectural naming adjudication; keep separate identities/categories. |
| Coordinate Care-Team Collaboration boundary conflict | Capture README records wording about managing clinical handovers against metamodel §3.7 exclusion. | Preserve both assertions; no inferred clinical authority or automatic downstream trace. |
| Feature native framing edge | **Native mapping resolved, 2026-10-19:** accepted CT3 Capability → Composition → Feature Capability canonically establishes framing/semantic containment under the [metamodel rule](../03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy). Feature retains Harmonia Feature classification. | No submitted instance is accepted by this rule. Preserve current standing and ancestry/identity uncertainty; do not manufacture missing CT3s or regenerate source captures. |
| Business responsibility and information predicates | Requires/Consumes/Produces, enabled-by, owner, participation, contribution and realisation are distinct. | Approve predicate-preserving native mappings; do not collapse into Access/Serving/Composition for convenience. |
| Mixed Domain05 register types | Ergo pattern, Ergon software component, Praxis envelope, Journey information, Pathway controls and Terma/Horos boundaries differ. | Apply per-construct mapping; resolve genuinely mixed facets before generation. |
| Information semantic categories | Activity/Event/Outcome/Device information does not become ApplicationProcess/BusinessEvent/Motivation Outcome/Technology Device merely by name. | Use conceptual information projections, preserve semantic category metadata and qualified associations. |
| Domain04 completed baseline with retained candidate packages | G2 source assertions have A/B/C/D and specific approvals; detailed Task/ReportedTask/assembly mappings remain qualified. | Evaluate each assertion, not a global “complete” flag; do not mark all family headings approved. |
| Legacy technology, Security and migration material | Runtime plan governs implementation sequencing, not automatic current RAD topology or transitions. External accepted ADRs remain subject to axioms and legacy reconciliation. | Separate bounded review/incorporation, original-content preservation and explicit source authority. |

No source conflict is resolved by publication dates, existing code, catalogue indentation, implementation convenience, report recommendations or matching similar names.

## 6. Traceability readiness and gaps

The RAD already governs the direction of reasoning and some concrete paths. Domain03→04 explicitly preserves capability responsibility, Function/Process information accountability and source authority. B1 preserves dual Capability anchors, distinct Requires/Consumes/Produces and genuine Metabolē Realisation. B2.1 establishes seven material enablement relationships and no application allocation. The capture includes three additional assurance support assertions and explicit behaviour/Feature relations. These are useful starting slices once identities and native relation profiles are ready.

The six Health Information Access worked-example Functions do not establish software allocations. No EC-14 component is allocated. Full detailed derivation across 137 Features and the whole information-responsibility matrix remains reserved. Retained BEC ancestry, Feature associations, dependency ownership, TaskOutcome detail, Diagram-to-source identity, Application/Information coverage and technology support relationships remain incomplete or unestablished. No exhaustive Role/Function or all-layer matrix is required merely for visual completeness.

Selecting Ponos can eventually reveal the governed execution responsibility and associated information/management seams. At present, publication cannot honestly provide a complete approved path from every Business Capability through its Functions and Information to Ponos or its deployment node. That is missing established traceability/identity, not a reason to invent edges. A component-specific View should visibly state the boundary of established traces.

## 7. Publication verification and presentation limitations

The publication uses the immutable proven fixture and the Archi 5.9 plugin's bundled local Model/View/Diagram XSDs with offline W3C imports. The generated [publication report](../../archimate/capability-architecture/documentation-publication-report.md) and [validation evidence](../../archimate/capability-architecture/validation.json) record the current release and complete verification results. Checks cover the original semantic lock after removing only the 14 authorised additions, exact unique references, source/description drift, duplicate IDs, View-local endpoints, semantic endpoint agreement, View organisation, original diagnostic preservation, visual identity, XML schema validation, repeated generation/idempotence and portable configurable inputs.

All four generated Views and their visual identifiers/layout remain identical. The baseline fixture/lock remain unchanged; the published XML adds only the 14 governed Documentation fields. Its SHA-256 is recorded in `validation.json`; no Archi GUI import/visual-inspection result is claimed for the enriched publication.

Unresolved presentation matters: Documentation contains verbatim Markdown, so formatting/relative links may render as plain text in Archi; full descriptions are deliberately longer than scope-only summaries. BEC hierarchy remains tall (approximately 12,540 layout units); the five natural groupings are not assigned from names where membership is absent from the baseline. Labels/layout and all four Views require the user's Archi visual inspection. Enterprise remains flat because the baseline has no hierarchy. No extra diagram or inferred grouping is introduced in this slice.

Repository architecture-test and read-only reference-validation results are recorded in the generated publication report. Automated PASS does not resolve source-identity conflicts, Domain05 deferrals or human visual review. The 2,467 unresolved RAD dimensions/relationships remain findings.
