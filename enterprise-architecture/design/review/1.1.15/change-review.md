<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |
| [CR-001](#cr-001--add-class-packages-model-core-and-information) | Add Class Packages model, core, and information | Accept with amendment | Accept the model → core → information Package hierarchy and its Composition relationships. Rename the Harmonia Grouping specialisation ClassPackage to Package. Apply Package consistently to model, core, and information, including correcting the missing specialisation on core. Update the applicable representation, naming and containment rules so that Package is a recognised Grouping specialisation, Package-to-Package Composition expresses containment, and Package ancestry contributes to the qualified architectural name. |
| [CR-002](#cr-002--add-logical-model-class-informationunit) | Add Logical Model Class InformationUnit | Accept | Including recognition of Class as the Harmonia DataObject specialisation and the Package-derived qualified name. Foundational Topics from draft DRP are deferred. |
| [CR-003](#cr-003--add-non-functional-requirement-groups) | Add Non-Functional Requirement Groups | Accept | With the candidate hierarchy retained and RequirementGroup recognised as the Harmonia Grouping specialisation. Composition provides RequirementGroup → RequirementGroup containment. |
| [CR-004](#cr-004--add-functional-requirement-groups) | Add Functional Requirement Groups | Accept / Resolved | Accept the candidate Functional Requirement Group hierarchy: Functional Requirements → Information Management. Both specialise Grouping as RequirementGroup. Composition expresses RequirementGroup containment, consistent with CR-003. |
| [CR-005](#cr-005--add-requirement-information-durability-during-platform-failure) | Add Requirement Information Durability During Platform Failure | Accept / Resolved | Accept Information Durability During Platform Failure as an intentionally incomplete Requirement (placeholder/thinking point). Absence of documentation/properties is accepted and does not prevent incorporation. Contained via Composition under [RequirementGroup] Information Management. No Realisation edge from InformationUnit; zero DRP inference. |
| [CR-006](#cr-006--add-seven-architectural-overview-views) | Add Seven Architectural Overview Views | Accept | Preserve all seven Views exactly as architect-authored, including the empty View and the absence of visual relationship connections. No inferred connections, no automatic population, and no requirement to “complete” the empty View. |
| [CR-007](#cr-007--relocate-17-preexisting-view-folder-navigation-paths) | Relocate 17 Preexisting View Folder Navigation Paths | Accept / Resolved | Accept all 17 detected relocations of preexisting Views as intentional architect-authored navigation changes. Preserve the candidate View locations rather than restoring their baseline locations. |
| [CR-008](#cr-008--adjudicate-organization-identifier-loss-and-relational-flat-folder-nav) | Adjudicate Organization Identifier Loss and Relational Flat-Folder Nav | Accept / Resolved by governing decision. | The observed non-View organization identifier loss and relationship-folder differences are derived projection/serialization effects, not architectural changes. Non-View navigation is regenerated from governed Continuum containment. No candidate navigation structure is incorporated as independent architectural state. |
| [CR-009](#cr-009--adjudicate-value-language-representation-xml-lang-and-normalisations) | Adjudicate Value Language Representation (xml:lang) and Normalisations | Accept / Resolved by governing decision. | The demonstrated xml:lang="en" additions, semantically equivalent property-definition rebindings, and ordering normalisations are representation/transport effects and do not constitute architectural change. The accepted round-trip machinery shall normalise them where equivalence is deterministically established; anything outside those established rules remains reviewable. |

# ArchiMate Ingestion Change Review — Harmonia 1.1.15 Iteration 001

Standing: **CONFIRMED & COMPLETED for Release 1.1.16.** This review records adjudicated decisions for Harmonia release 1.1.15 iteration 001. All nine Change Requests (CR-001 through CR-009) have been formally adjudicated and resolved by the Solution Architect, incorporated into the Architecture Continuum and projection template under release 1.1.16 with published release 1.1.15 as predecessor baseline, verified across multi-tier checks, and confirmed by the Solution Architect. Completed record registered at [hcrl-1.1.15-001.complete.md](../../register/1.1.16/hcrl-1.1.15-001.complete.md).

Follow the [governed Change Review contract](../../../governance/archimate-ingestion-change-review.md) and the single [Architecture Change Provenance contract](../../../governance/architecture-change-provenance.md). This review implements those contracts; it defines no independent provenance policy.

## Review basis

- Review/run: `1.1.15/001` (iteration `001`); review revision: `3`.
- Prepared by / date: Junie (autonomous AI co-executor) / 2026-10-25.
- Incoming export: `enterprise-architecture/design/evidence/1.1.15/001/before/harmonia-continuum-1.1.15-001.xml`; file size: 28,590,881 bytes (28.6 MB); SHA-256: `a31ed511c07e0a54c503383f6e52dfdad1e5905e642c05e7dc14fb7649af2eb9`. Candidate workbench export provenance remains unverified; no transport receipt establishes origin, and the working-file hypothesis `enterprise-architecture/design/archimate/working/harmona-1.1.15-001.archimate` is not asserted as authoritative.
- Governed comparison baseline / release: `enterprise-architecture/design/archimate/published/1.1.14/Harmonia-Architecture.xml` / release `RAD-1.1.14`; file size: 26,919,406 bytes (26.9 MB); SHA-256: `ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`.
- Target RADS/source revision: Target release `RAD-1.1.15`, active branch `Data-Governence-Services`.
- Transport control, if used: None. No no-edit control export is available for this baseline; the Archi binary is absent from PATH; uncertainty is preserved under AX-15.
- Detection:
  - Model comparison: `enterprise-architecture/tooling/exchange_compare.py compare` executed with `/tmp/harmonia-tooling-venv/bin/python`; persisted at `enterprise-architecture/design/evidence/1.1.15/001/after/model-comparison.json` (SHA-256: `97a2160f72314f4c53c940b0d1e56a7b913676ef374c86546739fbb3a2c305d6`). Comparison exit status: `DIFFERENCES`. Comparator totals: 17,957 `SEMANTIC DIFFERENCE`; 2,294 `UNEXPLAINED DIFFERENCE` (2,263 under `/model/organizations`, 31 elsewhere); 540 `INFORMATION LOSS`; 296 `SEMANTICALLY EQUIVALENT NORMALISATION`. Total findings: 21,087.
  - Property-definition rebinding table: `enterprise-architecture/design/evidence/1.1.15/001/after/propdef-rebindings.md` (45 old→new `propertyDefinition/@identifier` bindings extracted from the comparison report).
  - Direct organization inventory: `enterprise-architecture/design/evidence/1.1.15/001/after/organization-inventory.json` (SHA-256: `d9ddcb677be6d42ad73c4651072d7847d24755184de4a2fcac3d5868b849b0a3`). Non-positional count: all 2,894 baseline organization `@identifier` values absent in candidate; organization items 5,135 → 6,838.
  - Canonical-source intake: `enterprise-architecture/tooling/continuum_workbench.py review` executed read-only with `timeout 110`. Captured at `enterprise-architecture/design/evidence/1.1.15/001/after/continuum-workbench-review.txt`: stderr `FAIL: Missing/ambiguous canonicalId; creation requires reviewed identity allocation`, process exit status **1**, no `--output` JSON produced. Elapsed ~3 s. See [Intake verification](#intake-verification-continuum_workbench-review) for the abort scope.
- Evidence limits: Transport origin unverified; no-edit control unavailable; Archi binary absent from PATH; organization identifier loss and navigation divergence are preserved as raw discovery findings under AX-15 and AX-17 without attributing producer cause or assuming the loss is acceptable.
- Active run records: `enterprise-architecture/design/evidence/1.1.15/001/before/` and `enterprise-architecture/design/evidence/1.1.15/001/after/`.

### Intake verification: continuum_workbench review

- Command: `/tmp/harmonia-tooling-venv/bin/python enterprise-architecture/tooling/continuum_workbench.py review --baseline <1.1.14 XML> --submitted <1.1.15-001 XML> --output enterprise-architecture/design/evidence/1.1.15/001/after/continuum-workbench-review.json`.
- Observed result: stderr `FAIL: Missing/ambiguous canonicalId; creation requires reviewed identity allocation`; process exit status 1; no output JSON written. Full captured text is in `after/continuum-workbench-review.txt`.
- Scope of the abort: the tool's `model()` reader calls `c.require` per element (`continuum_workbench.py` line 30). It stops at the first candidate element *lacking* a canonicalId, which is `InformationUnit` (`id-f5c345ab6c47433da11ee12f5d84e557`, candidate index 1306). The tool's stderr does not name the element. The element's identification is from agent inspection of the XML order and the `model()` traversal. The baseline and candidate elements before it were evaluated. It therefore did not evaluate the other 11 added elements, did not reach the projection check, and did not reach the relationship or View signature comparison. No review JSON was produced.
- Metadata on the other 11 added elements (missing `canonicalId`, `displayName`, `friendlyName`, `canonicalName`, `archimateType`, `harmoniaType`) is the agent's direct XML inspection, not tool output. The tool does not report it.
- Interpretation: the captured FAIL is intake evidence that the candidate elements do not yet carry canonical identity metadata. It does not authorise identity allocation (`canonical-identity-and-naming.md` lines 292–296 and Praxis lines 183–186 keep allocation behind adjudication).
- No intake PASS is inferred. The comparator gate result is reported as `DIFFERENCES` only.

### Decomposition record

The candidate export contains additions originating from offline design proposal `DRP-Information-Unit-Framework.draft.002.md`, together with model-wide representation and navigation divergences. Under the Praxis rule that raw XML counts are not CR counts (Praxis line 121), changes with distinct architectural concepts or adjudication concerns are decomposed into discrete, independently adjudicable Change Requests. The DRP is a proposal, not an accepted architecture; its contents establish no canonical authority before adjudication (AX-17).

| Observation / evidence locator | CR(s) | Decomposition reason and evidence-only treatment |
| --- | --- | --- |
| 3 added Grouping elements (`model`, `core`, `information`) and 2 Compositions between them | [CR-001](#cr-001--add-class-packages-model-core-and-information) | Logical packaging additions; the `core` Specialization anomaly, the containment rule conflict, and the ungoverned `ClassPackage` specialization. |
| 1 added DataObject (`InformationUnit`) and 1 Composition from `information` | [CR-002](#cr-002--add-logical-model-class-informationunit) | Logical model class; `Class` specialization, qualified-name path, and absence of the DRP Topics. |
| 5 added Grouping elements (NFR group hierarchy) and 4 Compositions | [CR-003](#cr-003--add-non-functional-requirement-groups) | NFR grouping hierarchy; candidate-only structure; `RequirementGroup` specialization and containment rule. |
| 2 added Grouping elements (`Functional Requirements`, `Information Management`) and 1 Composition | [CR-004](#cr-004--add-functional-requirement-groups) | Functional grouping hierarchy; candidate-only structure; specialization and containment rule. |
| 1 added Requirement element (`Information Durability During Platform Failure`) and 1 Composition | [CR-005](#cr-005--add-requirement-information-durability-during-platform-failure) | Requirement without documentation or properties; containment governed by Home for native Requirement (Praxis lines 132–140). |
| 7 added presentation Views (15 visual nodes, 0 visual connections) | [CR-006](#cr-006--add-seven-architectural-overview-views) | Overview diagrams; one View has 0 nodes; zero connections on all seven. |
| 17 baseline Views with relocated folder paths (5 REQ-FND, 11 `[Topic]`, 1 boundary View) | [CR-007](#cr-007--relocate-17-preexisting-view-folder-navigation-paths) | Preexisting View navigation trees; protected by Praxis lines 176–181 and require a separately reviewed directive. |
| Organization `@identifier` loss (2,894 absent); flat `/Relations` placement of 1,660 relationships; 540 positional `INFORMATION LOSS`; 2,263 positional `UNEXPLAINED DIFFERENCE` under `/model/organizations` | [CR-008](#cr-008--adjudicate-organization-identifier-loss-and-relational-flat-folder-nav) | Organization identifier and navigation uncertainty; separated from representation findings. |
| 17,957 `SEMANTIC DIFFERENCE` (`xml:lang="en"` added to property values); 296 `SEMANTICALLY EQUIVALENT NORMALISATION` (45 propertyDefinition rebindings, 250 element property child-order, 1 propertyDefinitions child-order); 3 non-organization child-order findings | [CR-009](#cr-009--adjudicate-value-language-representation-xml-lang-and-normalisations) | Language-attribute representation and serialization normalisations; kept separate from text identity. |
| `continuum_workbench.py review` intake | Shared intake evidence (see [Intake verification](#intake-verification-continuum_workbench-review)) | FAIL on canonicalId; no PASS inferred; identity allocation not authorised. |

## Governed transition and authorised scope

*(Initial intake snapshot preserved for historical context; updated with actual resulting governed state following Solution Architect adjudication and confirmation)*

- Originating governed state / RAD release: Baseline RAD-1.1.14 at `enterprise-architecture/design/archimate/published/1.1.14/Harmonia-Architecture.xml` (SHA-256: `ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`), branch `Data-Governence-Services`.
- Initial intended resulting state / release: Proposed incorporation of Calliope Information Unit foundational model and requirement grouping structures for RAD-1.1.15 under HCRL iteration 001.
- Target HCRL and applicable DR(s): `enterprise-architecture/design/review/1.1.15/hcrl-1.1.15-001.pending.md`, Decision Record DR-1; completed and registered in `enterprise-architecture/design/register/1.1.16/hcrl-1.1.15-001.complete.md`.
- Actual resulting governed state / release: **Release 1.1.16** (with published release 1.1.15 preserved as immutable predecessor baseline), confirmed and completed following Solution Architect adjudication.
- Architectural purpose and bounded differences: Calliope Information Unit logical model from the DRP proposal, comprising 12 added elements, 9 added relationships, 7 added views, and representation and navigation deltas.
- Dependencies, exclusions and unresolved matters at intake (all adjudicated/resolved):
  - Canonical identity allocation: Resolved; newly allocated UUIDv4 canonical IDs assigned during canonical authoring.
  - Specialisations `Package`, `RequirementGroup`, and DataObject `Class`: Resolved; incorporated into schema enum and type catalogue.
  - Containment: Resolved; Composition containment established for Package and RequirementGroup.
  - Naming ancestry: Resolved; Package ancestry contributes to qualified architectural names (`PKG-<Name>`, `CLASS-<Name>`).
  - Property-definition rebindings, `xml:lang`, and organization identifier loss: Resolved by governing decision as normalisations and derived projection effects.
  - Missing Specialization on `core`: Resolved; `Package` applied consistently.
  - 17 preexisting View folder relocations: Resolved; confirmed as intentional architect-authored relocations and preserved in projection template organization hierarchy.
- Solution Architect authorisation: Adjudicated (all nine CRs accepted/resolved); confirmed and completed for Release 1.1.16 by Mark Hunter, Solution Architect.
- Material decision revisions: Applied as Release 1.1.16 with predecessor 1.1.15 per Solution Architect directive.
- Historical reconstruction, only if separately authorised: Not applicable.

## Changed-content inventory

Owner paths were verified against `enterprise-architecture/governance/continuum/type-catalogue.json` and the repository tree. The type-catalogue directory for native `Grouping` is `grouping` relative to `enterprise-architecture/continuum/`; that directory is currently ABSENT. For native `DataObject` it is `application/data-object` (present; Topic subdirectory `application/data-object/topic`). Native `Requirement` is `motivation/requirement` (present). No `enterprise-architecture/design/archimate/templates/` directory exists. View and View navigation are owned by `enterprise-architecture/design/archimate/projection-template.xml` (see `design/archimate/README.md` lines 6–9). Element navigation is owned by `enterprise-architecture/continuum/navigation.json`.

| CR / directive | Operation / correspondence | Owner path and existence | Architectural meaning / evidence contribution |
| --- | --- | --- | --- |
| [CR-001](#cr-001--add-class-packages-model-core-and-information) | Add 3 Groupings and 2 Compositions | `enterprise-architecture/continuum/grouping/`: ABSENT (directory declared by `type-catalogue.json` for native Grouping; no Grouping directory exists yet). Prospective placement only. | Logical packaging structure `model`, `core`, `information`. |
| [CR-002](#cr-002--add-logical-model-class-informationunit) | Add DataObject and 1 Composition | `enterprise-architecture/continuum/application/data-object/`: exists (Topic subdirectory `topic/`). Prospective placement only. | Logical model class `InformationUnit`. |
| [CR-003](#cr-003--add-non-functional-requirement-groups) | Add 5 Groupings and 4 Compositions | `enterprise-architecture/continuum/grouping/`: ABSENT (see CR-001). Prospective placement only. | NFR requirement grouping hierarchy. |
| [CR-004](#cr-004--add-functional-requirement-groups) | Add 2 Groupings and 1 Composition | `enterprise-architecture/continuum/grouping/`: ABSENT (see CR-001). Prospective placement only. | Functional requirement grouping hierarchy. |
| [CR-005](#cr-005--add-requirement-information-durability-during-platform-failure) | Add Requirement and 1 Composition | `enterprise-architecture/continuum/motivation/requirement/`: exists. Prospective placement only. | Requirement `Information Durability During Platform Failure`. |
| [CR-006](#cr-006--add-seven-architectural-overview-views) | Add 7 Views | Owner: `enterprise-architecture/design/archimate/projection-template.xml` (exists). | Seven overview diagrams (15 visual nodes, 0 connections). |
| [CR-007](#cr-007--relocate-17-preexisting-view-folder-navigation-paths) | Relocate 17 View folder paths | Owner: `enterprise-architecture/design/archimate/projection-template.xml` (exists); baseline `published/1.1.14/Harmonia-Architecture.xml` (PRESENT). | 17 preexisting View navigation trees changed. |
| [CR-008](#cr-008--adjudicate-organization-identifier-loss-and-relational-flat-folder-nav) | Organization structure and relationship navigation | Owner: `enterprise-architecture/continuum/navigation.json` (exists) for element navigation; baseline published XML PRESENT (2,894 organization `@identifier`, 0 relationship folder references). | Organization identifier divergence; flat `/Relations` placement. |
| [CR-009](#cr-009--adjudicate-value-language-representation-xml-lang-and-normalisations) | Property representation | Baseline PRESENT (19,820 of 37,777 element property values carry `en`; 17,957 do not); candidate PRESENT (`en` on all 37,787 values). | Language attribute on values; propdef rebindings; child order. |

## Evidence and closure

- HCRL/DR evidence contribution: Contributed to `hcrl-1.1.15-001.pending.md` (DR-1); completed and registered in `enterprise-architecture/design/register/1.1.16/hcrl-1.1.15-001.complete.md`.
- Before/after state: Baseline RAD-1.1.14 (`Harmonia-Architecture.xml`), predecessor RAD-1.1.15 (`Harmonia-Architecture.xml`), candidate `harmonia-continuum-1.1.15-001.xml`, and resulting published RAD-1.1.16 (`Harmonia-Architecture.xml`). Resulting Continuum state: **Confirmed and completed for Release 1.1.16**.
- Evidence selection:
  - Model comparison report: `enterprise-architecture/design/evidence/1.1.15/001/after/model-comparison.json`.
  - Direct organization inventory: `enterprise-architecture/design/evidence/1.1.15/001/after/organization-inventory.json`.
  - Propdef rebinding table: `enterprise-architecture/design/evidence/1.1.15/001/after/propdef-rebindings.md`.
  - Intake validation: `enterprise-architecture/design/evidence/1.1.15/001/after/continuum-workbench-review.txt` (stderr and EXIT_CODE=1).
- Verification conclusions and limits:
  - Element text identity: all 1,602 baseline elements match by identifier; their folder label paths are identical in baseline and candidate (0 differences). For the six canonical contract properties, text values are identical but language attributes differ on their 8,095 values, yielding `exact_value_language_multiplicity_owner_preserved = 0` for that property scope. The full 17,957 `SEMANTIC DIFFERENCE` findings cover the projector-emitted key population: those six contract properties, six `Continuum / *` keys, and `Specialization`; the contract properties account for 8,095 and the other keys account for 9,862. Whether the language attribute is material is **UNRESOLVED**. The comparator's classification is not treated as acceptance.
  - Relationship identity: all 1,651 baseline relationships match by identifier. Baseline has no relationship folder references; candidate places all 1,660 relationships in a single `/Relations` folder.
  - View identity: all 1,209 baseline Views match by identifier; 1,192 retain identical folder paths; 17 have relocated paths (CR-007).
  - Added entities: 12 elements, 9 relationships, 7 views, as identified by the comparator and by direct inspection.
  - Distinct gate results:
    - Model comparison delta: `DIFFERENCES` (exit 0). No PASS is inferred for validity.
    - Continuum workbench intake: FAIL (stderr, exit 1). Stopped at the first candidate element lacking canonicalId (`InformationUnit`, index 1306); later checks were not reached.
    - XSD / schema validation: Executed on release 1.1.16 projection (`offlineXsd: PASS` against `archimate3_Diagram.xsd`).
  - Closure confirmation: Confirmed and completed by Mark Hunter, Solution Architect, in registered record [hcrl-1.1.15-001.complete.md](../../register/1.1.16/hcrl-1.1.15-001.complete.md).
  - Processing retention/dependencies: Retained at `enterprise-architecture/design/evidence/1.1.15/001/` for recovery and validation.

<a id="cr-001--add-class-packages-model-core-and-information"></a>

## CR-001 — Add Class Packages model, core, and information

**Disposition:** Accepted with amendment

**Outcome:** Resolved

**Outcome Reason:** Adjudicated by Solution Architect: accept model → core → information Package hierarchy with amendment to rename ClassPackage to Package, apply Package consistently across all three elements, and update metamodel/naming/containment contracts.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add three Grouping elements establishing the packaging namespace `model.core.information`, and two Composition relationships linking them:
1. `[ClassPackage] model` (Grouping, Specialization `ClassPackage`)
2. `[ClassPackage] core` (Grouping, no Specialization)
3. `[ClassPackage] information` (Grouping, Specialization `ClassPackage`)
4. Composition `[ClassPackage] model` -> `[ClassPackage] core`
5. Composition `[ClassPackage] core` -> `[ClassPackage] information`

**DRP Cross-Reference & Standing:** `DRP-Information-Unit-Framework.draft.002.md` is an offline design proposal. Its content is not accepted architecture (AX-17).
- **In DRP:** §3.1 proposes package root `model.core`; §5 proposes `model.core.information`. §3.2 names the specialisation `Package` ("Package is the Harmonia specialisation of ArchiMate Grouping").
- **In candidate only:** `ClassPackage` as the Specialization on `model` and `information`; no specialisation on `core`. The DRP names `Package`, not `ClassPackage`.
- **Governance and representation conflicts:**
  - *Containment:* Praxis lines 132–140 select Home for contained native type Grouping ("Other applicable native types", contained element → home). The candidate uses Composition between Groupings for `core` and `information`. Grouping containment is not governed by Composition under the current rule.
  - *Naming ancestry:* `canonical-identity-and-naming.md` lines 233–234 state that Grouping, folders and Views supply no naming ancestry. DRP §3.2 relies on package Composition for qualified namespace.
  - *Specialisation representation:* `governance/continuum/element.schema.json` (`specialization` enum, lines 95–101) permits only `null`, `ImplementationGuideline`, `Feature`, `Topic`. `governance/continuum/type-catalogue.json` lists the same specialisations, plus the relationship specialisation `Home`. `ClassPackage` and `Package` are not in this representation profile. Adopting either requires a representation-contract amendment (schema and type-catalogue), in addition to any naming-contract change. Under `canonical-identity-and-naming.md` line 212, unknown or incompatible authored specialisations remain unresolved.
  - *Stop condition:* Praxis lines 194–200 stop dependent work where required new naming or collection rules are needed.

### Detected Evidence

**Observed Evidence:** Pinned candidate export `harmonia-continuum-1.1.15-001.xml` (direct XML inspection and comparator output):
- Elements:
  - `id-8a0f2c41d3264f988442781309550cc0`: Grouping `[ClassPackage] model`, Specialization `ClassPackage`. Candidate folder: `Other/Package Groups/model`.
  - `id-a8f6eefb1d7a44a09acf06a41b59ffab`: Grouping `[ClassPackage] core`, no Specialization; no `properties` block and no documentation. Candidate folder: `Other/Package Groups/model/core`.
  - `id-74614b2b4bdd4897852e3483d597bd01`: Grouping `[ClassPackage] information`, Specialization `ClassPackage`. Candidate folder: `Other/Package Groups/model/core/information`.
- Relationships:
  - `id-11f188596d0e4bb0a669caa4cfdfd7d5`: Composition `[ClassPackage] model` -> `[ClassPackage] core`.
  - `id-1bf19e908b514a7db10d38ecb022f938`: Composition `[ClassPackage] core` -> `[ClassPackage] information`.
- Intake: the captured `continuum_workbench.py review` run aborted at `InformationUnit` (see [Intake verification](#intake-verification-continuum_workbench-review)). The metadata absence for these three Groupings is from direct XML inspection, not tool output.

### Architectural Questions / Adjudication

Questions (governance decisions requiring Solution Architect authority):
1. Specialization of `[ClassPackage] core`: Should `core` declare a Specialization (for example `ClassPackage`, matching `model` and `information`, or `Package` per DRP §3.2), or remain unspecialised? Any new specialisation value requires a representation-contract amendment (`element.schema.json` and `type-catalogue.json`).
2. Packaging specialisation vocabulary: Does the Solution Architect authorise a contract amendment that establishes `Package` or `ClassPackage` as a governed Grouping specialisation, and if so, under which authority?
3. Containment and naming ancestry: Does the Solution Architect authorise amending Praxis lines 132–140 and `canonical-identity-and-naming.md` lines 233–234 so that Grouping Composition can establish containment and qualified namespace, or should Home govern this containment?

**Solution Architect Decision:** Accept with amendment. Accept the model → core → information Package hierarchy and its Composition relationships. Rename the Harmonia Grouping specialisation `ClassPackage` to `Package`. Apply `Package` consistently to `model`, `core`, and `information`, including correcting the missing specialisation on `core`. Update the applicable representation schema enum, type catalogue, naming contract, and containment rules so that `Package` is a recognised `Grouping` specialisation, Package-to-Package Composition expresses containment, and Package ancestry contributes to the qualified architectural name (`PKG-<Name>`). Author elements under `enterprise-architecture/continuum/grouping/package/`.

### Related Change Requests

Required by [CR-002](#cr-002--add-logical-model-class-informationunit) (parent packaging context). Related to [CR-006](#cr-006--add-seven-architectural-overview-views) (overview diagrams).

### Authorised Reconciliation Instruction

1. Update `element.schema.json` and `type-catalogue.json` to recognise `Package` as a native `Grouping` specialisation with directory `grouping/package`, display prefix `[Package]`, and canonical qualifier `PKG`.
2. Update `canonical-identity-and-naming.md`, `archimate-round-trip-engineering-praxis.md`, `governed_naming.py`, `information_naming.py`, and `containment.py` to recognise Package Composition containment and naming ancestry.
3. Author canonical Package Markdown elements `model`, `core`, and `information` with allocated UUIDv4 canonical IDs and their Composition relationships.

### Reconciliation / Processing Record

Processed directly into canonical Continuum representations and tooling contracts for release 1.1.16.

---

<a id="cr-002--add-logical-model-class-informationunit"></a>

## CR-002 — Add Logical Model Class InformationUnit

**Disposition:** Accepted

**Outcome:** Resolved

**Outcome Reason:** Adjudicated by Solution Architect: accept logical model class InformationUnit as DataObject Class specialisation with Package-derived qualified name; defer draft DRP Topics.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add one DataObject element representing the logical Class `InformationUnit` under `[ClassPackage] information`, linked by a Composition:
1. `InformationUnit` (DataObject, Specialization `Class`)
2. Composition `[ClassPackage] information` -> `InformationUnit`

**DRP Cross-Reference & Standing:**
- **In DRP:** §5 and §6 propose five foundational Topics (`InformationUnit`, `InformationUnitIdentity`, `InformationUnitMetadata`, `InformationUnitContent`, `InformationUnitContext`) alongside one logical Class `InformationUnit`. §5 poses the open question whether the final qualified location is `model.core.InformationUnit` or `model.core.information.InformationUnit`.
- **In candidate only:** the logical Class `InformationUnit` (DataObject, Specialization `Class`). None of the five Topics is in the candidate.
- **Representation and governance conflicts:**
  - *Specialisation:* `Class` is not in the representation profile (`element.schema.json` specialization enum; `type-catalogue.json` specialisations). The DataObject Topic rule (`canonical-identity-and-naming.md` lines 205–213) governs `Topic` only. Line 212 keeps unknown or incompatible specialisations unresolved. Accepting `Class` requires a representation-contract amendment.
  - *Containment:* The native type DataObject is governed by Composition (Praxis line 138), so the Composition from `information` is consistent with the rule for this element. However, its parent is a Grouping, which supplies no naming ancestry (`canonical-identity-and-naming.md` line 233). Parent-level containment is subject to CR-001.
  - *Axioms:* AX-04, AX-05, AX-06, AX-14, AX-15, AX-17.

### Detected Evidence

**Observed Evidence:** Pinned candidate export `harmonia-continuum-1.1.15-001.xml` (direct XML inspection):
- Element `id-f5c345ab6c47433da11ee12f5d84e557`: DataObject `InformationUnit`, Specialization `Class`. Candidate folder: `Application`.
- Relationship `id-b63d27292ce9457b886a0b50e0534075`: Composition `[ClassPackage] information` -> `InformationUnit`.
- Intake: the captured `continuum_workbench.py review` run stopped at the first candidate element lacking canonicalId (`InformationUnit`, element index 1306), identified by agent inspection of `model()` order and the XML; the tool's stderr names no element. The tool evaluated the baseline and candidate elements 0–1305 before this point. The absence of metadata on this element is confirmed by direct XML inspection.

### Architectural Questions / Adjudication

Questions (governance decisions requiring Solution Architect authority):
1. Foundational Topics: DRP §5 and §6 propose five Topics alongside the logical Class. Should the Solution Architect incorporate only the logical Class at this stage, or defer incorporation until the foundational Topics are authored?
2. Qualified path: Should the qualified name of `InformationUnit` be `model.core.InformationUnit` or `model.core.information.InformationUnit` (DRP §5 open question)?
3. Specialisation governance: Does the Solution Architect authorise a representation-contract amendment (`element.schema.json`, `type-catalogue.json`, and the naming contract) establishing `Class` as a governed DataObject specialisation alongside `Topic`?

**Solution Architect Decision:** Accept. Accept logical model class `InformationUnit` (`[Class] InformationUnit`). Recognise `Class` as a Harmonia `DataObject` specialisation. Establish Package-derived qualified name `model.core.information.InformationUnit` (`PKG-Model.PKG-Core.PKG-Information.CLASS-InformationUnit`). The five foundational Topics from draft DRP §5 and §6 are deferred and not inferred at this stage.

### Related Change Requests

Requires [CR-001](#cr-001--add-class-packages-model-core-and-information) (parent package). Related to [CR-006](#cr-006--add-seven-architectural-overview-views) (overview diagram).

### Authorised Reconciliation Instruction

1. Update `element.schema.json` and `type-catalogue.json` to recognise `Class` as a native `DataObject` specialisation with directory `application/data-object/class`, display prefix `[Class]`, and canonical qualifier `CLASS`.
2. Update naming rules and tooling (`governed_naming.py`, `information_naming.py`) to support `Class` qualification.
3. Author canonical element `[Class] InformationUnit` with allocated UUIDv4 canonical ID under `enterprise-architecture/continuum/application/data-object/class/`.

### Reconciliation / Processing Record

Processed directly into canonical Continuum representation and tooling contracts for release 1.1.16.

---

<a id="cr-003--add-non-functional-requirement-groups"></a>

## CR-003 — Add Non-Functional Requirement Groups

**Disposition:** Accepted

**Outcome:** Resolved

**Outcome Reason:** Adjudicated by Solution Architect: retain candidate NFR grouping hierarchy; recognise RequirementGroup as Grouping specialisation with Composition containment.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add five Grouping elements with Specialization `RequirementGroup`, and four Compositions organising them as a hierarchy:
1. `[RequirementGroup] Non-Functional-Requirements`
2. `[RequirementGroup] Performance Requirements`
3. `[RequirementGroup] Disaster Recovery Requirements`
4. `[RequirementGroup] Availability`
5. `[RequirementGroup] Information Availability`
6. Composition `Non-Functional-Requirements` -> `Performance Requirements`
7. Composition `Non-Functional-Requirements` -> `Disaster Recovery Requirements`
8. Composition `Non-Functional-Requirements` -> `Availability`
9. Composition `Availability` -> `Information Availability`

**DRP Cross-Reference & Standing:**
- **In DRP:** §1.1 frames Enterprise Capability Features (for example `DataManagement.InformationUnits.DataSteadfastness.Availability`); §1.2 lists SHALL statements. The DRP contains **no** requirement groups.
- **In candidate only:** the NFR requirement group hierarchy and all five Groupings.
- **Representation and governance conflicts:**
  - `RequirementGroup` is not in `element.schema.json` specialization enum or `type-catalogue.json`. Adopting it requires a representation-contract amendment.
  - Containment of Groupings by Composition conflicts with Praxis lines 132–140 (Home for Other applicable native types).
  - Under `canonical-identity-and-naming.md` line 212, the specialisation remains unresolved.

### Detected Evidence

**Observed Evidence:** Pinned candidate export (direct XML inspection):
- Elements:
  - `id-812dcde922614fde87adbccc993d0294`: Grouping `[RequirementGroup] Non-Functional-Requirements`, Specialization `RequirementGroup`. Folder: `Other/Requirement Groups/Non-Functional-Requirements`.
  - `id-7a965baa4e6d46bd92a9b41433920d21`: Grouping `[RequirementGroup] Performance Requirements`, Specialization `RequirementGroup`. Folder: `Other/Requirement Groups/Non-Functional-Requirements/Performance Requirements`.
  - `id-9384a733b3b04279ace34e4fdca25409`: Grouping `[RequirementGroup] Disaster Recovery Requirements`, Specialization `RequirementGroup`. Folder: `Other/Requirement Groups/Non-Functional-Requirements/Disaster Recovery Requirements`.
  - `id-a6e03ca37cb54e69b9052cd12d6642eb`: Grouping `[RequirementGroup] Availability`, Specialization `RequirementGroup`. Folder: `Other/Requirement Groups/Non-Functional-Requirements/Availability Requirements`.
  - `id-e373852f1b5a4d9bbe4c646b2f6615ed`: Grouping `[RequirementGroup] Information Availability`, Specialization `RequirementGroup`. Folder: `Other/Requirement Groups/Non-Functional-Requirements/Availability Requirements/Information Availability`.
- Relationships: `id-171c590aa9a9420eb2f4941620fa31cd`, `id-17958c7240b44bb8b58956e80b4535ff`, `id-47586d7d4bf4491ab66594bb8251d3fc`, `id-3434cff3125743f491c966eb0225490a`.
- Intake: the captured workbench run aborted at `InformationUnit` (CR-002) and did not evaluate these five elements. Their missing metadata is from direct XML inspection.

### Architectural Questions / Adjudication

Questions (governance decisions requiring Solution Architect authority):
1. Requirement grouping specialisation: Does the Solution Architect authorise `RequirementGroup` as a governed Grouping specialisation? That requires a representation-contract amendment (`element.schema.json` and `type-catalogue.json`).
2. Relationship to DRP §1.1 Features: Should these NFR groups link to the Capability Features framed in DRP §1.1, or remain standalone Groupings?
3. Containment rule: Should requirement-grouping containment be governed by Home (Praxis lines 132–140) or by Composition, and what authority would establish the rule?

**Solution Architect Decision:** Accept. Retain the candidate NFR requirement group hierarchy (`Non-Functional-Requirements`, `Performance Requirements`, `Disaster Recovery Requirements`, `Availability`, `Information Availability`) and their four Compositions. Recognise `RequirementGroup` as a Harmonia `Grouping` specialisation. Composition provides RequirementGroup → RequirementGroup containment.

### Related Change Requests

Related to [CR-004](#cr-004--add-functional-requirement-groups) and [CR-006](#cr-006--add-seven-architectural-overview-views).

### Authorised Reconciliation Instruction

1. Update `element.schema.json` and `type-catalogue.json` to recognise `RequirementGroup` as a native `Grouping` specialisation with directory `grouping/requirement-group` and display prefix `[RequirementGroup]`.
2. Update Praxis and `containment.py` so that `RequirementGroup` Composition expresses containment.
3. Author the five NFR Grouping elements with allocated UUIDv4 canonical IDs under `enterprise-architecture/continuum/grouping/requirement-group/` along with their four Compositions.

### Reconciliation / Processing Record

Processed directly into canonical Continuum representations and tooling contracts for release 1.1.16.

---

<a id="cr-004--add-functional-requirement-groups"></a>

## CR-004 — Add Functional Requirement Groups

**Disposition:** Accepted

**Outcome:** Resolved

**Outcome Reason:** Adjudicated by Solution Architect: accept candidate Functional Requirement Group hierarchy (Functional Requirements → Information Management) as RequirementGroup specialisations with Composition containment.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add two Grouping elements with Specialization `RequirementGroup`, and one Composition:
1. `[RequirementGroup] Functional Requirements`
2. `[RequirementGroup] Information Management`
3. Composition `Functional Requirements` -> `Information Management`

**DRP Cross-Reference & Standing:**
- **In DRP:** §1.1 and §1.2 contain Features and requirement statements; the DRP contains **no** requirement grouping structures.
- **In candidate only:** both Groupings and the Composition.
- **Representation and governance conflicts:** `RequirementGroup` is not in the representation profile (schema enum; type-catalogue). Containment by Composition conflicts with Praxis lines 132–140. The specialisation remains unresolved under `canonical-identity-and-naming.md` line 212.

### Detected Evidence

**Observed Evidence:** Pinned candidate export (direct XML inspection):
- `id-2f1bdf2e08464b8084ead4f7ae315e4a`: Grouping `[RequirementGroup] Functional Requirements`, Specialization `RequirementGroup`. Folder: `Other/Requirement Groups/Functional Requirements`.
- `id-458ce11fb42643e48a71bfda600b0d08`: Grouping `[RequirementGroup] Information Management`, Specialization `RequirementGroup`. Folder: `Other/Requirement Groups/Functional Requirements/Information Management`.
- Relationship `id-c4e3157f4c5441b2b370eff5a4450e7a`: Composition `Functional Requirements` -> `Information Management`.
- Intake: the captured workbench run aborted at `InformationUnit` (CR-002) and did not evaluate these elements.

### Architectural Questions / Adjudication

Questions (governance decisions requiring Solution Architect authority):
1. Functional requirement grouping: Does the Solution Architect approve this grouping structure and its placement under `Other/Requirement Groups/`?
2. Specialisation and containment: Does the Solution Architect authorise `RequirementGroup` as a governed specialisation (requiring a representation-contract amendment), and which containment mechanism (Home or Composition) governs it?

**Solution Architect Decision:** Accept. Accept the candidate Functional Requirement Group hierarchy: `Functional Requirements` → `Information Management`. Both specialise Grouping as `RequirementGroup`. Composition expresses `RequirementGroup` containment, consistent with CR-003.

### Related Change Requests

Required by [CR-005](#cr-005--add-requirement-information-durability-during-platform-failure) (parent of the requirement). Related to [CR-006](#cr-006--add-seven-architectural-overview-views).

### Authorised Reconciliation Instruction

1. Author elements `[RequirementGroup] Functional Requirements` and `[RequirementGroup] Information Management` with allocated UUIDv4 canonical IDs under `enterprise-architecture/continuum/grouping/requirement-group/`.
2. Author the Composition relationship from `Functional Requirements` to `Information Management`.

### Reconciliation / Processing Record

Processed directly into canonical Continuum representations and tooling contracts for release 1.1.16.

---

<a id="cr-005--add-requirement-information-durability-during-platform-failure"></a>

## CR-005 — Add Requirement Information Durability During Platform Failure

**Disposition:** Accepted

**Outcome:** Resolved

**Outcome Reason:** Adjudicated by Solution Architect: accept Information Durability During Platform Failure as an intentionally incomplete Requirement placeholder contained via Composition under Information Management; zero Realisation relationship; zero DRP inference.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add one Requirement element under `[RequirementGroup] Information Management`, linked by a Composition:
1. `[Requirement] Information Durability During Platform Failure` (Requirement)
2. Composition `[RequirementGroup] Information Management` -> `[Requirement] Information Durability During Platform Failure`

**DRP Cross-Reference & Standing:**
- **In DRP:** §1.2 lists technology-neutral SHALL statements; §1.1 references platform failure under Feature `DataManagement.InformationUnits.DataSteadfastness.Availability`. The specific requirement name is candidate only.
- **Containment conflict:** a native Requirement is in the "Other applicable native types" row of Praxis lines 132–140, so the governing mechanism is **Home** (contained element → home). The candidate uses Composition from a Grouping.
- **Axioms:** AX-05, AX-06, AX-10, AX-11, AX-14, AX-15, AX-17.

### Detected Evidence

**Observed Evidence:** Pinned candidate export (direct XML inspection):
- Element `id-1dc1ebeceba74dec8fde94f14eff5ce1`: Requirement `[Requirement] Information Durability During Platform Failure`. No Specialization, no documentation, no `properties` block. Folder: `Motivation`.
- Relationship `id-a1b10b97b59f46e98cc0a6b0d41a2687`: Composition `[RequirementGroup] Information Management` -> `[Requirement] Information Durability During Platform Failure`.
- Intake: the captured workbench run aborted at `InformationUnit` (CR-002) and did not evaluate this element.

### Architectural Questions / Adjudication

Questions (governance decisions requiring Solution Architect authority):
1. Requirement text and properties: Does the Solution Architect intend to author requirement statement, rationale, and verification criteria before canonical incorporation?
2. Containment mechanism: Should containment of this Requirement be governed by Home (Praxis lines 132–140) or by Composition from a RequirementGroup? If Composition, which authority would amend the rule?

**Solution Architect Decision:** Accept. Accept `Information Durability During Platform Failure` as an intentionally incomplete Requirement — a placeholder / thinking point. Its absence of documentation and properties is accepted and does not prevent incorporation. Accept its Composition containment beneath `[RequirementGroup] Information Management`. Do not add or infer a Realisation relationship from `InformationUnit`. No additional architecture is to be inferred from the DRP.

### Related Change Requests

Requires [CR-004](#cr-004--add-functional-requirement-groups) (parent group). Related to [CR-006](#cr-006--add-seven-architectural-overview-views).

### Authorised Reconciliation Instruction

1. Update Praxis and `containment.py` to permit Composition containment of native `Requirement` when nested under a `RequirementGroup`.
2. Author element `[Requirement] Information Durability During Platform Failure` with an allocated UUIDv4 canonical ID under `enterprise-architecture/continuum/motivation/requirement/` without documentation, properties, or Realisation relationship.
3. Author the Composition relationship from `[RequirementGroup] Information Management` to this Requirement.

### Reconciliation / Processing Record

Processed directly into canonical Continuum representation and tooling contracts for release 1.1.16.

---

<a id="cr-006--add-seven-architectural-overview-views"></a>

## CR-006 — Add Seven Architectural Overview Views

**Disposition:** Accepted

**Outcome:** Resolved

**Outcome Reason:** Adjudicated by Solution Architect: preserve all seven overview Views exactly as architect-authored in projection-template.xml, including the empty View and zero visual connections.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add seven overview presentation Views (15 visual nodes, 0 visual connections) illustrating the package, class, and requirement hierarchies. Views are owned by `enterprise-architecture/design/archimate/projection-template.xml`.
1. `class-packages-overview` (3 nodes)
2. `information-package-overview` (2 nodes)
3. `functional-requirements-overview` (2 nodes)
4. `information-management-requirements-overview` (2 nodes)
5. `non-functional-requirements-overview` (4 nodes)
6. `availability-requirements-overview` (2 nodes)
7. `information-availability-management-requirements-overview` (0 nodes)

### Detected Evidence

**Observed Evidence:** Pinned candidate export (comparator presentation metrics and direct XML inspection):
- `id-4927667988ca4a52b620d3d8436077b7`: `class-packages-overview` (3 nodes, 0 connections). Folder: `Views/3-ApplicationLayer/1-InformationMap/Classes`.
- `id-25cb1748ba154926a59a25e4539c9096`: `information-package-overview` (2 nodes, 0 connections). Folder: `Views/3-ApplicationLayer/1-InformationMap/Classes/model/core/information`.
- `id-35ab31e1f4d14980b0d33929462bb7b3`: `functional-requirements-overview` (2 nodes, 0 connections). Folder: `Views/0-Motivation/Requirements/Functional Requirements`.
- `id-0abeb9dfc5bc441299712f3ee08e49de`: `information-management-requirements-overview` (2 nodes, 0 connections). Folder: `Views/0-Motivation/Requirements/Functional Requirements/Information Management`.
- `id-8009ccd75bd44bb78dfa7a7b6c6fdd80`: `non-functional-requirements-overview` (4 nodes, 0 connections). Folder: `Views/0-Motivation/Requirements/Non-Functional-Requirements`.
- `id-a313bbc71a524c139bb1018bb1960186`: `availability-requirements-overview` (2 nodes, 0 connections). Folder: `Views/0-Motivation/Requirements/Non-Functional-Requirements/Availability Requirements`.
- `id-e7190de0c6f64b5c99b74fa6be52eb05`: `information-availability-management-requirements-overview` (0 nodes, 0 connections). Folder: `Views/0-Motivation/Requirements/Non-Functional-Requirements/Availability Requirements/Information Availability Management`. Empty presentation anomaly.
- Relationship to the comparator: presentation node count 4,005 → 4,020 (+15); connection count 1,804 → 1,804 (+0).

### Architectural Questions / Adjudication

Questions:
1. Empty View: Does the Solution Architect intend to populate `information-availability-management-requirements-overview`, retain it as an empty placeholder, or remove it from the candidate set?
2. Visual connections: All seven Views have zero visual connections. Does the Solution Architect intend to add connections or retain the current layout?

**Solution Architect Decision:** Accept. Preserve all seven overview Views exactly as architect-authored in `projection-template.xml`, including the empty View (`information-availability-management-requirements-overview`) and the absence of visual relationship connections. No inferred connections, no automatic population, and no requirement to "complete" the empty View.

### Related Change Requests

References elements from [CR-001](#cr-001--add-class-packages-model-core-and-information), [CR-002](#cr-002--add-logical-model-class-informationunit), [CR-003](#cr-003--add-non-functional-requirement-groups), [CR-004](#cr-004--add-functional-requirement-groups), [CR-005](#cr-005--add-requirement-information-durability-during-platform-failure). Related to [CR-007](#cr-007--relocate-17-preexisting-view-folder-navigation-paths) (View navigation).

### Authorised Reconciliation Instruction

1. Incorporate the seven overview Views into `enterprise-architecture/design/archimate/projection-template.xml` under `<views><diagrams>` matching candidate identifiers, geometry, styling, nodes, and empty connection sets.
2. Add corresponding organization items under `<organizations>` in `projection-template.xml`.

### Reconciliation / Processing Record

Processed directly into projection template for release 1.1.16.

---

<a id="cr-007--relocate-17-preexisting-view-folder-navigation-paths"></a>

## CR-007 — Relocate 17 Preexisting View Folder Navigation Paths

**Disposition:** Accepted

**Outcome:** Resolved

**Outcome Reason:** Adjudicated by Solution Architect: all 17 detected relocations of preexisting Views are intentional architect-authored navigation changes; preserve candidate locations.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Relocate navigation folder paths for 17 preexisting baseline Views in the organization tree:
1. Five `REQ-FND` co-governance Views move from `/Views/0-Motivation/Requirement Co-governance` to `/Views/0-Motivation/Requirements/Requirement Co-governance`:
   - `id-2e771bce18cc5aca80e3c4ad72a01554`: `REQ-FND-001: Durable Ingress Acceptance Boundary — co-governance`
   - `id-4afe4ce42e6e56b0867aca68bb355877`: `REQ-FND-002: Operational Activity Progression State — co-governance`
   - `id-633aecdb63ab5281af2a383a2b11800d`: `REQ-FND-003: Subject Referential Integrity — co-governance`
   - `id-a6fd22d1736d5453bec5194f87b61b80`: `REQ-FND-004: Explicit Indeterminate Outcome — co-governance`
   - `id-5a0eb09317f250eb91bdd86ae38de5e1`: `REQ-FND-005: Independent Assurance of Governed Activity — co-governance`
2. Eleven `[Topic]` Views and the boundary View move from `/Views/3-ApplicationLayer/1-InformationMap/<Name>` to `/Views/3-ApplicationLayer/1-InformationMap/Topics/<Name>`, with the boundary View moving from `1-InformationMap` to `1-InformationMap/Topics`:
   - `id-5ba19156cb0042078a310cbe57ead042`: `[Topic] Individual` (`.../Individual` → `.../Topics/Individual`)
   - `id-80049507c9254357acb5b3a8ad40a8dd`: `[Topic] Workforce` (`.../Workforce` → `.../Topics/Workforce`)
   - `id-92781b38f5d946828f64a7916a7a18ad`: `[Topic] Care` (`.../Care` → `.../Topics/Care`)
   - `id-18e0217911394fec9c22da7620f497d2`: `[Topic] Coordination` (`.../Coordination` → `.../Topics/Coordination`)
   - `id-d0c8d828b67e440c88cf1fa5c0fab1dc`: `[Topic] Equipment` (`.../Equipment` → `.../Topics/Equipment`)
   - `id-f149b52ad8cd4590b0d52597460ec64d`: `[Topic] Ordering` (`.../Ordering` → `.../Topics/Ordering`)
   - `id-37a27c14f07d45d0bb34399fb1ddd5b8`: `[Topic] Enterprise` (`.../Enterprise` → `.../Topics/Enterprise`)
   - `id-efe1fe4f30854c45909458e496aced94`: `[Topic] Place` (`.../Place` → `.../Topics/Place`)
   - `id-e386cab4c72748ff8b35ebb6a2e0fc02`: `[Topic] Scheduling` (`.../Scheduling` → `.../Topics/Scheduling`)
   - `id-c36226e854dd4c2192d650bb3ecef384`: `[Topic] Service` (`.../Service` → `.../Topics/Service`)
   - `id-f11523a5e2a449a9af538d768f977b0b`: `[Topic] TaskCompletion` (`.../TaskCompletion` → `.../Topics/TaskCompletion`)
   - `id-f04466cd677051cc98098438b8a4f980`: `InformationMap — representation boundary` (`.../1-InformationMap` → `.../1-InformationMap/Topics`)

**Governance Authority & View Preservation:** Praxis lines 176–181 (section "Common interpretation and projection rules"): "All existing Views are preserved architect-authored presentation artefacts in this round-trip process. Preserve identity, membership, connections, nested nodes, order, layout, geometry, style, Documentation and their navigation tree. They are not disposable Element/Relationship folders... A separately reviewed directive is required to change one." Stop condition for destructive View changes without authority: Praxis lines 194–200.

### Detected Evidence

**Observed Evidence:** Direct comparison of organization folder label paths by View identifier (old = 1.1.14 baseline; new = candidate):
- 17 of 1,209 baseline Views have different folder label paths; 1,192 are identical.
- Views with relocated paths are listed above with their old and new paths.
- Views in the candidate organization tree are identified by `identifierRef`. The baseline has no relationship folder references (CR-008).

### Architectural Questions / Adjudication

Question:
1. Does the Solution Architect approve relocating the 17 preexisting Views to the candidate folder paths (`.../Requirements/` and `.../Topics/`), or should their baseline navigation-tree paths be preserved under Praxis lines 176–181?

**Solution Architect Decision:** Accept. The Solution Architect confirms that all 17 relocations of preexisting Views were intentional architect-authored navigation changes. Preserve the candidate View locations rather than restoring their baseline locations.

### Related Change Requests

Related to [CR-006](#cr-006--add-seven-architectural-overview-views) (new Views) and [CR-008](#cr-008--adjudicate-organization-identifier-loss-and-relational-flat-folder-nav) (organization navigation).

### Authorised Reconciliation Instruction

1. Update the organization `<item>` hierarchy in `enterprise-architecture/design/archimate/projection-template.xml` to locate the 5 REQ-FND Views under `.../Requirements/Requirement Co-governance` and the 11 Topic Views plus 1 boundary View under `.../1-InformationMap/Topics/...`.

### Reconciliation / Processing Record

Processed directly into projection template organization hierarchy for release 1.1.16.

---

<a id="cr-008--adjudicate-organization-identifier-loss-and-relational-flat-folder-nav"></a>

## CR-008 — Adjudicate Organization Identifier Loss and Relational Flat-Folder Nav

**Disposition:** Accepted by governing decision

**Outcome:** Resolved

**Outcome Reason:** Resolved by governing decision: non-View organization identifier loss and relationship-folder differences are derived projection/serialization effects; non-View navigation regenerated from Continuum.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Discovery findings on the Open Exchange `organizations` structure. These are recorded without attributing producer cause or assuming acceptability.
1. *Organization item identifier absence:* all 2,894 baseline organization `@identifier` values are absent in the candidate (0 present). Organization items change from 5,135 to 6,838; `identifierRef` attributes change from 2,811 to 4,490 (`organization-inventory.json`).
2. *Positional comparator findings:* 540 positional `INFORMATION LOSS` and 2,263 positional `UNEXPLAINED DIFFERENCE` findings under `/model/organizations`. Positional findings are not an identifier-based inventory.
3. *Element folder label paths:* all 1,602 baseline elements have identical folder label paths in the candidate (0 differences by direct comparison). Element folder placement is therefore preserved for existing elements.
4. *Relationship navigation:* baseline has 0 relationship folder references. Candidate places all 1,660 relationships (1,651 baseline + 9 added) in a single flat `/Relations` folder.
5. *Placement of added elements:* `model` → `Other/Package Groups/model`; `core` → `Other/Package Groups/model/core`; `information` → `Other/Package Groups/model/core/information`; `InformationUnit` → `Application`; NFR and functional Groupings → `Other/Requirement Groups/...`; Requirement → `Motivation`.

**Governance Authority & Axiom Alignment:**
- Praxis lines 156–175 (section "Common interpretation and projection rules"): "Element and Relationship folders are derived navigation projections. Rebuild declared populations from this shared resolution and governed collection/role configuration, independently of submitted folder ancestry." And: "Relationship navigation follows its actual source's resulting path, reuses a source context where present and supplies a source-element context otherwise."
- Element navigation is owned by `enterprise-architecture/continuum/navigation.json` (canonical owner of element navigation).
- AX-14, AX-15, AX-17: the organization identifier loss and flat relational placement are preserved as discovery findings pending adjudication.

### Detected Evidence

**Observed Evidence:**
- Comparison report `model-comparison.json`: 540 `INFORMATION LOSS` and 2,263 `UNEXPLAINED DIFFERENCE` findings under `/model/organizations`.
- Direct inventory `organization-inventory.json`: baseline 5,135 items, 2,894 `@identifier`, 2,811 `@identifierRef`; candidate 6,838 items, 0 `@identifier`, 4,490 `@identifierRef`; 2,894 baseline identifiers absent.
- Element folder verification: 0 element folder differences across 1,602 baseline elements.
- Relationship folder verification: baseline 0 folder references; candidate 1,660 in `/Relations`.

### Architectural Questions / Adjudication

Questions:
1. Navigation regeneration: Under Praxis lines 156–175, element and relationship navigation is derived from resolved containment and governed collection configuration (owner `continuum/navigation.json`). Does the Solution Architect confirm that canonical regeneration applies during round-trip publication, or should the candidate organization structure be preserved?
2. Relationship navigation: Does the Solution Architect approve placing all relationships in the flat `/Relations` folder, or should relationship navigation follow its source element context (Praxis lines 168–171)?
3. Organization identifiers: Does the Solution Architect require the 2,894 baseline organization `@identifier` values to be preserved, or accept their absence as a serialization difference? This is an identity-of-navigation question, distinct from element and relationship identity.

**Solution Architect Decision:** Accept / Resolved by governing decision. The observed non-View organization identifier loss and relationship-folder differences are derived projection/serialization effects, not architectural changes. Non-View navigation is regenerated from governed Continuum containment. No candidate navigation structure is incorporated as independent architectural state.

### Related Change Requests

Related to [CR-007](#cr-007--relocate-17-preexisting-view-folder-navigation-paths) (View navigation paths) and [CR-009](#cr-009--adjudicate-value-language-representation-xml-lang-and-normalisations) (representation differences).

### Authorised Reconciliation Instruction

1. Regenerate non-View navigation deterministically from governed Continuum containment during projection. Update `navigation.json` only as a derived representation of governed containment.

### Reconciliation / Processing Record

Resolved by governing decision and enforced via projection tooling.

---

<a id="cr-009--adjudicate-value-language-representation-xml-lang-and-normalisations"></a>

## CR-009 — Adjudicate Value Language Representation (xml:lang) and Normalisations

**Disposition:** Accepted by governing decision

**Outcome:** Resolved

**Outcome Reason:** Resolved by governing decision: xml:lang="en" additions, propdef rebindings, and ordering normalisations are representation/transport effects normalised by accepted round-trip machinery.

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Discovery findings on property-value representation, separated into three distinct classes. Text identity and language attribute are assessed separately.

1. *Text identity (preserved):* the text of every property value on all 1,602 baseline elements is identical to the candidate. Text identity is **not** the same as representation identity.
2. *Language attribute (`xml:lang`) — SEMANTIC DIFFERENCE:* the candidate adds `xml:lang="en"` to every property value lacking it in the baseline (17,957 `None → en` findings); the candidate carries `xml:lang="en"` on all 37,787 values, including the 10 added values. In the baseline, 19,820 of 37,777 property values carry `xml:lang="en"` and 17,957 do not. The missing attributes correspond exactly to projector-generated keys: the six contract properties (`canonicalId`, `displayName`, `friendlyName`, `canonicalName`, `archimateType`, `harmoniaType`), six `Continuum / *` keys, and `Specialization` (250 values). The six contract properties account for 8,095 findings; the other keys account for 9,862. The comparator reports all 17,957 as semantic differences; their materiality is **UNRESOLVED**. The comparator's classification is not acceptance and is not inferred to be non-material.
3. *Property-definition identifier rebindings — SEMANTICALLY EQUIVALENT NORMALISATION (comparator) and rebinding obligation (Praxis):* 45 `propertyDefinition/@identifier` rebindings (old → new bindings listed in `after/propdef-rebindings.md`), for example `Architectural Status` `propid-26` → `propid-32` and `Baseline / Architectural Status` `propid-47` → `propid-52`. The comparator classifies these as SEMANTICALLY EQUIVALENT NORMALISATION. Separately, Praxis lines 183–186 require old and new bindings to be reported with evidence. The comparator classification does not discharge that reporting obligation; the table in `propdef-rebindings.md` is that report.
4. *Property child order — SEMANTICALLY EQUIVALENT NORMALISATION:* 250 `/model/elements/element/properties/child-order` findings and 1 `/model/propertyDefinitions/child-order` finding. Property order is a serialization order under the comparator.
5. *Non-organization child order (UNEXPLAINED DIFFERENCE):* 3 child-order findings outside `organizations`: `/model/elements[1]/child-order` (first difference at index 0), `/model/relationships[1]/child-order`, `/model/views[1]/diagrams[1]/child-order` (first difference at index 1194).

**Tool and governance notes:**
- `continuum_workbench.py` lines 43–45 state that Exchange property-definition IDs "are transport identifiers" and that the governed comparison reuses exact property name, value/language and child ordering. Line 45 states: "Never call property-ID rewriting an architectural edit." This comment is the tool's own position, and it is distinct from the Praxis obligation.
- The tool comment does not itself establish that rebinding is non-material, and it does not replace the Praxis reporting obligation in lines 183–186.
- Praxis lines 116–119 require the review to state what changed and what remained unchanged, including bindings.
- Under AX-14, AX-15, and AX-17, these findings are submitted for Solution Architect adjudication rather than assumed acceptable.

### Detected Evidence

**Observed Evidence:**
- Comparison report `model-comparison.json`: 17,957 `SEMANTIC DIFFERENCE`; 296 `SEMANTICALLY EQUIVALENT NORMALISATION` (45 propdef rebindings, 250 element property child-order, 1 propertyDefinitions child-order); 3 non-organization child-order findings in `UNEXPLAINED DIFFERENCE`.
- Rebinding table: `enterprise-architecture/design/evidence/1.1.15/001/after/propdef-rebindings.md` (45 rows, old and new `propertyDefinition/@identifier`).
- Text identity: all 1,602 baseline element property texts match.

### Architectural Questions / Adjudication

Questions:
1. Language attribute: Does the Solution Architect adjudicate the addition of `xml:lang="en"` on property values as a non-material Open Exchange serialization variation, or as an unapproved semantic difference requiring remediation? Materiality is UNRESOLVED until adjudicated.
2. Rebindings: Does the Solution Architect accept the 45 propertyDefinition rebindings listed in `propdef-rebindings.md` as transport serialization, or require them to be reversed or recorded as bindings in a canonical source?
3. Child order: Does the Solution Architect accept the property and document child-order differences as serialization order, given that the comparator treats property order as a normalisation?

**Solution Architect Decision:** Accept / Resolved by governing decision. The demonstrated `xml:lang="en"` additions, semantically equivalent property-definition rebindings, and ordering normalisations are representation/transport effects and do not constitute architectural change. The accepted round-trip machinery shall normalise them where equivalence is deterministically established; anything outside those established rules remains reviewable.

### Related Change Requests

Related to [CR-008](#cr-008--adjudicate-organization-identifier-loss-and-relational-flat-folder-nav) (organization navigation divergence).

### Authorised Reconciliation Instruction

1. Rely on deterministic projection and normalized review machinery to handle representation and transport normalisations.

### Reconciliation / Processing Record

Resolved by governing decision and enforced via normalized review machinery.
