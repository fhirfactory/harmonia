<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Calliope Information Unit model, requirement groupings & model-wide representation/navigation ingestion review | Accepted per Solution Architect adjudication | All nine Change Requests (CR-001 through CR-009) adjudicated and accepted/resolved by Solution Architect. Authorised for application as release 1.1.16 with predecessor 1.1.15; application and verification complete; confirmed and closed by Solution Architect in registered record [hcrl-1.1.15-001.complete.md](../../register/1.1.16/hcrl-1.1.15-001.complete.md). |
| [DR-2](#dr-2) | Bounded supporting Minutiae, pinned model evidence & execution state | Accepted per Solution Architect confirmation | Pinned baseline (1.1.14), published predecessor baseline (1.1.15), and candidate (1.1.15-001) evidence; release 1.1.16 projection and verification hashes; task and report temporal provenance; release authority governance; zero git mutations. Minutiae-Accepted: Yes. Confirmed and closed in registered record [hcrl-1.1.15-001.complete.md](../../register/1.1.16/hcrl-1.1.15-001.complete.md). |

# HCRL — 1.1.14 → 1.1.15: Calliope Information Unit model, requirement groupings & model-wide representation/navigation ingestion review

**Standing.** Pending iteration **001** for **RAD-1.1.14 → RAD-1.1.15**. Mark Hunter, Solution Architect, issued the operative model-update task in session records, retained in the [formal Task][task]. The candidate Open Exchange model export at `enterprise-architecture/design/evidence/1.1.15/001/before/harmonia-continuum-1.1.15-001.xml` introduces the foundational Information Unit logical model structure under Calliope (`model.core.information.InformationUnit`) along with associated requirement grouping structures and seven overview views, originating from the offline design proposal `enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.002.md`. In strict adherence to repository governance rules ([AGENTS.md](../../../../AGENTS.md) and [ArchiMate Round-Trip Engineering Praxis](../../../governance/archimate-round-trip-engineering-praxis.md)), this iteration executes Phase 1 of the round trip: pinning baseline evidence, evaluating model differences, decomposing deltas into discrete Change Requests, authoring the [Change Review][change-review], emitting formal Task and [Report][report] provenance, and stopping at the mandatory Change Review Checkpoint for Solution Architect adjudication.

All nine Change Requests ([CR-001][cr-001] through [CR-009][cr-009]) in `change-review.md` are initialised with default Disposition **On-Hold** and Outcome **Unresolved**. No architectural change, canonical identity allocation, continuum mutation, or release increment is applied. Root release authority records `1.1.14` (`architecture-release.json`); pending iterations allocate no release increment.

**Review basis.** The immediate governed predecessor release is RAD-1.1.14, whose published baseline `enterprise-architecture/design/archimate/published/1.1.14/Harmonia-Architecture.xml` (file size: 26,919,406 bytes; SHA-256: `ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`) establishes the accepted canonical architecture and durable ArchiMate projection. The candidate model is `enterprise-architecture/design/evidence/1.1.15/001/before/harmonia-continuum-1.1.15-001.xml` (file size: 28,590,881 bytes; SHA-256: `a31ed511c07e0a54c503383f6e52dfdad1e5905e642c05e7dc14fb7649af2eb9`). Candidate workbench export provenance remains unverified; no transport receipt accompanies the export, and the working file hypothesis `enterprise-architecture/design/archimate/working/harmona-1.1.15-001.archimate` is not asserted as authoritative.

Entry tracked state for this review stream is branch `Data-Governence-Services`. Mark Hunter, Solution Architect, issued the operative instruction; Junie agent co-executor conducted Phase 1 discovery, comparison, decomposition, Change Review authoring, and provenance emission on 2026-10-25. The common [Architecture Governance](../../../governance/architecture-governance.md), [Architecture Review Praxis](../../../governance/architecture-review-praxis.md), [Architecture Change Provenance](../../../governance/architecture-change-provenance.md), [Architecture Release Policy](../../../governance/architecture-release-policy.md), and [ArchiMate Round-Trip Engineering Praxis](../../../governance/archimate-round-trip-engineering-praxis.md) govern this review.

The Architecture Continuum remains canonical authority. Architectural Axioms AX-01 through AX-17 govern interpretation. In accordance with fail-closed governance (AX-17 and AGENTS.md §1.5), the offline design proposal `DRP-Information-Unit-Framework.draft.002.md` is a proposal, not accepted architecture; its contents establish no pre-adjudication canonical authority. Discovered discrepancies and uncertainties are preserved neutrally as discovery evidence under AX-14, AX-15, and AX-17. Zero production Java source files, Architecture Continuum elements, ArchiMate published models, or architecture release numbers have been modified.

<a id="dr-1"></a>
## DR-1 — Calliope Information Unit foundational model, requirement groupings & model-wide representation/navigation ingestion review

**Background & Activity Context.** The ArchiMate Round-Trip Engineering process was initiated upon the candidate export `harmonia-continuum-1.1.15-001.xml`. The export reflects additions drafted in `DRP-Information-Unit-Framework.draft.002.md`, seeking to establish foundational Information Unit structures under Calliope. In accordance with Praxis lines 116–122, raw comparator counts are decomposed into discrete, independently adjudicable Change Requests across architectural layers and concerns.

**Change Review Summary & Decomposed Change Requests.** The governed review document is authored at [change-review.md](change-review.md) (Status: **CONFIRMED & COMPLETED for Release 1.1.16**). All nine Change Requests have been formally adjudicated and resolved by the Solution Architect:

1. **[CR-001] Add Class Packages model, core, and information**:
   - *Added Elements (3)*: Groupings `[ClassPackage] model` (`id-8a0f2c41d3264f988442781309550cc0`), `[ClassPackage] core` (`id-a8f6eefb1d7a44a09acf06a41b59ffab`), `[ClassPackage] information` (`id-74614b2b4bdd4897852e3483d597bd01`).
   - *Added Relationships (2)*: Compositions `model` -> `core` (`id-11f188596d0e4bb0a669caa4cfdfd7d5`), `core` -> `information` (`id-1bf19e908b514a7db10d38ecb022f938`).
   - *Architectural Discrepancies & Findings*:
     - Property anomaly: `[ClassPackage] core` lacks a `Specialization` attribute in candidate XML.
     - Containment conflict: Praxis lines 132–140 require Home for native Grouping containment ("Other applicable native types"); candidate uses Composition.
     - Specialization profile gap: `ClassPackage` is not in the representation schema enum (`governance/continuum/element.schema.json` allows only `null`, `ImplementationGuideline`, `Feature`, `Topic`). Adopting it requires a representation-contract amendment.
     - Naming ancestry conflict: `canonical-identity-and-naming.md` lines 233–234 establish that Groupings supply no naming ancestry, whereas DRP §3.2 assumes Composition establishes `model.core.information`.
     - Owner directory: `enterprise-architecture/continuum/grouping/` is declared by `type-catalogue.json` but currently ABSENT.

2. **[CR-002] Add Logical Model Class InformationUnit**:
   - *Added Element (1)*: DataObject `InformationUnit` (`id-f5c345ab6c47433da11ee12f5d84e557`).
   - *Added Relationship (1)*: Composition `information` -> `InformationUnit` (`id-b63d27292ce9457b886a0b50e0534075`).
   - *Architectural Discrepancies & Findings*:
     - Specialization profile gap: Specialization `Class` is not in `element.schema.json`; requires representation-contract amendment.
     - Qualified name: DRP names `model.core.information.InformationUnit`, but parent Groupings supply no naming ancestry.
     - DRP Topics absent: DRP §3.3 proposed 16 Topics; none are present in candidate model.
     - Intake abort: `continuum_workbench.py review` aborts at candidate element index 1306 (`InformationUnit`) due to missing `canonicalId` (exit 1).

3. **[CR-003] Add Non-Functional Requirement Groups**:
   - *Added Elements (5)*: Groupings `Non-Functional-Requirements` (`id-812dcde922614fde87adbccc993d0294`), `Performance Requirements` (`id-7a965baa4e6d46bd92a9b41433920d21`), `Disaster Recovery Requirements` (`id-9384a733b3b04279ace34e4fdca25409`), `Availability` (`id-a6e03ca37cb54e69b9052cd12d6642eb`), `Information Availability` (`id-e373852f1b5a4d9bbe4c646b2f6615ed`).
   - *Added Relationships (4)*: Compositions structuring the NFR hierarchy (`id-171c590aa9a9420eb2f4941620fa31cd`, `id-17958c7240b44bb8b58956e80b4535ff`, `id-47586d7d4bf4491ab66594bb8251d3fc`, `id-3434cff3125743f491c966eb0225490a`).
   - *Architectural Discrepancies & Findings*:
     - Candidate-only structure: Baseline has no requirement groupings.
     - Specialization profile gap: `RequirementGroup` is not in `element.schema.json`.
     - Containment conflict: Praxis lines 132–140 require Home for native Grouping containment; candidate uses Composition.

4. **[CR-004] Add Functional Requirement Groups**:
   - *Added Elements (2)*: Groupings `Functional Requirements` (`id-2f1bdf2e08464b8084ead4f7ae315e4a`), `Information Management` (`id-458ce11fb42643e48a71bfda600b0d08`).
   - *Added Relationship (1)*: Composition `Functional Requirements` -> `Information Management` (`id-c4e3157f4c5441b2b370eff5a4450e7a`).
   - *Architectural Discrepancies & Findings*:
     - Candidate-only structure: Baseline has no requirement groupings.
     - Specialization profile gap: `RequirementGroup` is not in `element.schema.json`.
     - Containment conflict: Praxis lines 132–140 require Home for native Grouping containment; candidate uses Composition.

5. **[CR-005] Add Requirement Information Durability During Platform Failure**:
   - *Added Element (1)*: Requirement `Information Durability During Platform Failure` (`id-1dc1ebeceba74dec8fde94f14eff5ce1`).
   - *Added Relationship (1)*: Composition `Information Management` -> `Information Durability During Platform Failure` (`id-a1b10b97b59f46e98cc0a6b0d41a2687`).
   - *Architectural Discrepancies & Findings*:
     - Lacks documentation and properties in candidate XML.
     - Containment conflict: Praxis lines 132–140 select Home for native Requirement containment; candidate uses Composition.
     - Realisation gap: Offline draft DRP §6.1 states requirement is realised by `InformationUnit`; candidate contains no realisation relationship.

6. **[CR-006] Add Seven Architectural Overview Views**:
   - *Added Views (7)*: `class-packages-overview` (3 visual nodes), `information-package-overview` (2 nodes), `functional-requirements-overview` (2 nodes), `information-management-requirements-overview` (2 nodes), `non-functional-requirements-overview` (4 nodes), `availability-requirements-overview` (2 nodes), `information-availability-management-requirements-overview` (0 nodes). All 7 views have 0 visual connections.
   - *Architectural Discrepancies & Findings*:
     - Presentation anomaly: View `information-availability-management-requirements-overview` contains 0 visual nodes.
     - Connections absent: All 7 views omit relationship connections between visual nodes.
     - Placement: Baseline uses `design/archimate/projection-template.xml`; no `templates/` directory exists.

7. **[CR-007] Relocate 17 Preexisting View Folder Navigation Paths**:
   - *Scope (17 Views)*: 5 REQ-FND Views relocated from `Other/Requirements` to `Other/Requirement Groups/Functional Requirements/Information Management`; 11 `[Topic]` Views relocated from `Application/Views` to `Application`; 1 boundary View `mneme-to-mnemosyne-information-flow` relocated to `Business`.
   - *Architectural Rule*: Preexisting View navigation is protected by Praxis lines 176–181 ("Unambiguous directive required to move or reparent an existing View. Never infer View reorganisation from element edits, relationship edits or template regeneration"). Relocations require a separately reviewed directive.

8. **[CR-008] Adjudicate Organization Identifier Loss and Relational Flat-Folder Nav**:
   - *Discovery Findings*:
     - All 2,894 baseline organization `@identifier` attributes are absent in candidate (0 present); organization items change from 5,135 to 6,838 (`organization-inventory.json`).
     - Positional comparator findings: 540 `INFORMATION LOSS` and 2,263 `UNEXPLAINED DIFFERENCE` findings under `/model/organizations` (`model-comparison.json`).
     - Relationship navigation: Baseline has 0 relationship folder references; candidate places all 1,660 relationships in a single flat `/Relations` folder.
     - Element folder preservation: All 1,602 baseline elements retain 100% identical folder label paths (0 differences).
     - Governance stance: Retained neutrally as discovery evidence under AX-14, AX-15, and AX-17 without attributing producer cause or assuming acceptability. Element navigation is owned by `continuum/navigation.json`.

9. **[CR-009] Adjudicate Value Language Representation (xml:lang) and Normalisations**:
   - *Discovery Findings*:
     - Language attribute: Candidate adds `xml:lang="en"` to every property value lacking it in baseline (17,957 `None -> en` findings); candidate has `en` on all 37,787 values. In baseline, 19,820 of 37,777 values carry `en` and 17,957 do not. Missing attributes in baseline correspond to projector-emitted keys: six contract properties (8,095 values), `Continuum / *` keys, and `Specialization` (total 9,862 values). Text identity of all 1,602 elements is 100% preserved. Materiality is UNRESOLVED.
     - Property-definition rebindings: 45 `propertyDefinition/@identifier` rebindings (old -> new bindings listed in `after/propdef-rebindings.md`). Comparator classifies as SEMANTICALLY EQUIVALENT NORMALISATION; Praxis lines 183–186 require explicit reporting of old/new bindings.
     - Property child order: 250 element property child-order and 1 propertyDefinitions child-order normalisations.
     - Non-organization child order: 3 child-order findings outside `organizations` in `model-comparison.json`.

**Solution Architect Disposition (DR-1).**

> **Accepted per Solution Architect adjudication (CR-001 through CR-009).**
> All nine Change Requests are adjudicated and resolved: CR-001 (Accept with amendment), CR-002 (Accept), CR-003 (Accept), CR-004 (Accept / Resolved), CR-005 (Accept / Resolved), CR-006 (Accept), CR-007 (Accept / Resolved), CR-008 (Accept / Resolved by governing decision), CR-009 (Accept / Resolved by governing decision). Authorised additions are to be incorporated into the Architecture Continuum and projection template under release 1.1.16 (with published release 1.1.15 as predecessor baseline). HCRL remains in Pending standing awaiting post-verification confirmation.

**Reconciliation Instruction & Processing Record.**
- Reconciliation Instruction: Authorised to apply accepted additions directly to Architecture Continuum and projection template, project release 1.1.16, and perform multi-tier verification.
- Processing Record: Application and multi-tier verification completed; confirmed and closed under Solution Architect confirmation for release 1.1.16. See completed record at [hcrl-1.1.15-001.complete.md](../../register/1.1.16/hcrl-1.1.15-001.complete.md).

<a id="dr-2"></a>
## DR-2 — Bounded Minutiae, pinned model evidence & execution state

Disclosed supporting and minor matters for Solution Architect review:

1. **Pinned Baseline and Candidate Evidence**:
   - Baseline published model: `enterprise-architecture/design/archimate/published/1.1.14/Harmonia-Architecture.xml` (file size: 26,919,406 bytes; SHA-256: `ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`).
   - Candidate submitted model: `enterprise-architecture/design/evidence/1.1.15/001/before/harmonia-continuum-1.1.15-001.xml` (file size: 28,590,881 bytes; SHA-256: `a31ed511c07e0a54c503383f6e52dfdad1e5905e642c05e7dc14fb7649af2eb9`).
   - Byte-level SHA-256 digests pinned; no XML-canonical digests computed.

2. **Attributable Temporal Provenance**:
   - Task issuance timestamp `2026-10-25T10:02:46.358Z` recovered from session records (`~/.junie/history.jsonl`), representing the earliest attributable issuance of the exact instruction text. An alternate execution entry `2026-10-25T10:19:20.839Z` is preserved as an unresolved attribution detail for Solution Architect treatment before closure. Formal Task is retained at `enterprise-architecture/design/evidence/1.1.15/001/before/ai-agent-tasks/20261025T100246Z-model-update.md`.
   - Report production timestamp is recorded at the completed write boundary (`2026-10-25T11:35:34.031Z`, prefix `20261025T113534Z-`), derived from session event log `~/.junie/sessions/session-261025-210129-viv7/events.jsonl` (`FileChangesBlockUpdatedEvent` at epoch ms `1792928134031`). Retained at `enterprise-architecture/design/evidence/1.1.15/001/after/ai-agent-reports/20261025T113534Z-model-update.md`. The initial estimated value `2026-10-25T11:34:00Z` was corrected as a subsequent provenance amendment without erasing the original attributable event.

3. **Comparison Evidence Artifacts**:
   - `model-comparison.json`: Generated by `exchange_compare.py compare` (SHA-256: `97a2160f72314f4c53c940b0d1e56a7b913676ef374c86546739fbb3a2c305d6`).
   - `organization-inventory.json`: Direct XML inventory of organization items and identifiers (SHA-256: `d9ddcb677be6d42ad73c4651072d7847d24755184de4a2fcac3d5868b849b0a3`).
   - `propdef-rebindings.md`: Table of 45 old->new `propertyDefinition/@identifier` rebindings extracted from comparison output.
   - `continuum-workbench-review.txt`: Read-only `continuum_workbench.py review` execution output capturing stderr `FAIL: Missing/ambiguous canonicalId; creation requires reviewed identity allocation` and process exit code 1.

4. **Workbench Transport and PATH Limits**:
   - Candidate model workbench export provenance remains unverified; no transport receipt or export log is present.
   - The Archi binary is absent from PATH; no-edit transport control verification is unavailable, preserving uncertainty under AX-15.

5. **Release Authority & Repository State**:
   - Root `architecture-release.json` advanced to version `1.1.16` (`previous_version: 1.1.15`).
   - Published release 1.1.15 baseline preserved byte-for-byte unchanged (`enterprise-architecture/design/archimate/published/1.1.15/Harmonia-Architecture.xml`, file size: 26,919,406 bytes [corrected from earlier estimate 26,920,447 bytes]; SHA-256: `61e7ebb958b321404c1c4694ae952119480f1fabd04dc2d319f35def84313dec`).
   - Durable release 1.1.16 projection published at `enterprise-architecture/design/archimate/published/1.1.16/Harmonia-Architecture.xml` (file size: 26,959,573 bytes [corrected from earlier pre-publication estimate 26,940,323 bytes]; SHA-256: `44c0fc9190d43ce82773ec1df6839cdbc14a0bc1d11ff4edc7ce173cde4cafac`).
   - Zero git commits, branches, merges, tags, or pushes executed. Git operations remain strictly read-only inspection.

6. **Post-Adjudication Application & Verification Evidence**:
   - `architecture_continuum.py validate`: PASS (1,614 elements, 1,660 relationships, 1,614 canonical identities, navigation PASS).
   - Resulting-state delta verification against baseline 1.1.15: Exactly 12 elements added, 9 relationships added, 7 overview views added, 17 preexisting views relocated in organization hierarchy. Zero regressions or unexpected diffs.
   - Offline XML Schema validation: PASS against `archimate3_Diagram.xsd`.
   - Tooling validation: `make -C enterprise-architecture/tooling check` PASS; all 82 Python tooling unit tests PASS.
   - Architecture tests: Maven ArchUnit suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) PASS (90 tests run, 0 failures, 0 errors).
   - Attributable temporal provenance: Task issuance `2026-10-25T22:57:53.954Z` ([Task][task-post]); Report production `2026-10-26T00:07:33.144Z` ([Report][report-post]).

Minutiae-Accepted: Yes
Comment: Mark Hunter, Solution Architect, confirmed the resulting architecture and application for release 1.1.16 (predecessor 1.1.15), accepted the delta of 12 added elements, 9 added relationships, 7 added Views, and 17 View relocations, accepted the validation results, and directed completion and closure of HCRL 1.1.15-001. The alternate execution timestamp entry (2026-10-25T10:19:20.839Z) is retained as an accepted factual limitation without fabrication. The discrepancy in prior Report §2.2's identity listing is formally disclosed; actual Continuum frontmatter identities govern. Registered in [hcrl-1.1.15-001.complete.md](../../register/1.1.16/hcrl-1.1.15-001.complete.md).

This control and DR-2's opening-table entry describe the same bounded group decision. Silence, agent classification, preparation of this pending HCRL, and successful tooling supply no acceptance.

[task]: ../../evidence/1.1.15/001/before/ai-agent-tasks/20261025T100246Z-model-update.md
[report]: ../../evidence/1.1.15/001/after/ai-agent-reports/20261025T113534Z-model-update.md
[task-post]: ../../evidence/1.1.15/001/before/ai-agent-tasks/20261025T225753Z-continue-post-adjudication.md
[report-post]: ../../evidence/1.1.15/001/after/ai-agent-reports/20261026T000733Z-continue-post-adjudication.md
[change-review]: change-review.md
[cr-001]: change-review.md#cr-001--add-class-packages-model-core-and-information
[cr-002]: change-review.md#cr-002--add-logical-model-class-informationunit
[cr-003]: change-review.md#cr-003--add-non-functional-requirement-groups
[cr-004]: change-review.md#cr-004--add-functional-requirement-groups
[cr-005]: change-review.md#cr-005--add-requirement-information-durability-during-platform-failure
[cr-006]: change-review.md#cr-006--add-seven-architectural-overview-views
[cr-007]: change-review.md#cr-007--relocate-17-preexisting-view-folder-navigation-paths
[cr-008]: change-review.md#cr-008--adjudicate-organization-identifier-loss-and-relational-flat-folder-nav
[cr-009]: change-review.md#cr-009--adjudicate-value-language-representation-xml-lang-and-normalisations
