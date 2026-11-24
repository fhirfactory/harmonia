Report-Produced-At: 2026-11-01T00:05:28.454092Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Execution Report — Archi catalogue organisation and display names, RAD 1.1.17 / 002

**Outcome: implemented and prepared for Solution Architect incorporated-result review, with explicit transport qualifications. Final acceptance is not asserted.**

The corresponding [Task](../../before/ai-agent-tasks/20261031T234829Z-archi-catalogue-and-display-names.md) entered the interaction at **2026-10-31T23:48:29.359Z**. Exact operative wording, attributable event provenance and baseline correspondence are retained. [HCRL 002](../../../../../review/1.1.17/hcrl-1.1.17-002.pending.md) records the authorised meaning, actual result, limitations and pending human checkpoint.

## Baseline, authority and scope

Used the existing **1.1.17 Iteration 001 candidate**, SHA-256 `a35583c227e8cac0bfb856708c97f600be406fe2acdff53361b326677e012e0a`, including its existing uncommitted changes. The immediate accepted predecessor remains **1.1.16**. Root `architecture-release.json` remains `version=1.1.17`, `previous_version=1.1.16`; no additional increment was allocated. The original candidate is retained as [compressed before XML](../../before/iteration-001-Harmonia-Architecture.xml.gz), with [baseline pins](../../before/baseline.json), initial Git state and 1,862 purpose-selected input hashes.

Loaded current Governance, Review Praxis, Change Provenance, Continuum Representation, Incorporation Praxis and relevant modelling/mapping/round-trip contracts, following the canonical AX-03/04/14/17 definitions and provenance requirements. HCRL DR-1 explains consistency: native semantics remain native, the existing machinery is reused, and architectural authority, reader organisation, workbench catalogues and authored presentation remain distinct. The operative Task expressly authorises replacing earlier folder-generation contracts. No NFR discovery, reclassification, Home reassignment, architectural redesign or View development occurred.

## Machinery changes and catalogue accounting

Removed the old hierarchy-generation path in `tooling/continuum_navigation.py`: `workbench_hierarchy_members`, Capability/Feature and Topic ancestry directories, state-selected Home/Composition folder eligibility, numbered parent/leaf folders, contextual/reference indexes and cross-layer folder exceptions. Underlying relationships, Home/containment semantics, naming validation and canonical reading navigation remain unchanged.

The existing projector now produces:

| Population | Result |
| --- | --- |
| Elements | **1,842**, exactly once in **27 native-type directories** |
| Element path | Native Archi category / Catalogue / native ArchiMate type |
| Relationships | **1,882**, exactly once in **681 source directories** |
| Relationship path | Relations / Catalogue / explicit source element, with no type subdirectories |
| Existing Views | **1,216**, with unchanged records and folder tree |

[Accounting by native category/type](../catalogue-accounting.json) supplies complete counts. ValueStream is native Strategy; Grouping is Other, including Package and RequirementGroup. Feature, Topic and Requirement specialisations remain properties of their native base elements. Native Name and element-entry label equal governed displayName verbatim across all supported types. Generated coordinates no longer prefix catalogue labels; meaningful identifiers already in governed names are preserved without regex stripping. canonicalId, canonicalName, friendlyName and source identifiers remain unchanged.

Relationship folder selection resolves the explicit source Exchange binding to its governed canonicalId, independently of storage owner, name, type, target or semantic containment. Readable `displayName [canonicalId]` labels and identity-derived transport folder IDs distinguish equal names deterministically. Only elements with outgoing edges receive a source directory. Folder placement asserts no relationship and changes no direction.

`continuum_workbench.py` now detects View-folder changes in its existing default review, including empty-folder edits. The element-only application adapter refuses applied View/folder changes, requiring existing adjudicated presentation-template amendment. The normalised review path remains review-only. Incoming element/relationship folders are not architectural authority and are reconstructed as catalogues.

Removed downstream RAD correspondence's dependency on numbered XML folders: canonical/contextual subjects reference actual native XML elements; structural reading headings retain canonical navigation locators and null XML-folder locators. This preserves reader-facing chapters without reconstructing old Archi hierarchies. Updated current representation, modelling, mapping, navigation, publication, round-trip and operational guidance with the scoped catalogue contract.

## Preservation and verification

The [repeatable proof](../verify-catalogue.py) and [preservation result](../preservation-verification.json) establish that the **entire candidate XML outside organizations is unchanged** from Iteration 001. This includes all element/relationship identities, native types, specialisations, names, descriptions, properties, source/target direction and every View record. The View organisation subtree is unchanged, including labels, order, folder metadata and supported presentation. All canonical files, navigation, root release, projection template and Iteration 001 evidence covered by the input pins remain byte-identical. No identity is allocated. Earlier accepted publications are unchanged.

The candidate and maintained publication metadata were regenerated through existing tooling; explicit preimage/hash checks bounded replacement to the unsigned 1.1.17 candidate after preserving its before bytes. Repeated generation is deterministic. Actual returned-model catalogue reconstruction produces the exact candidate organisation while preserving returned semantics and Views. [Validation summary](../validation-summary.json) records commands, outcomes and limits. All **85 tooling tests**, all **90 Java architecture tests**, publication/staleness/link checks and the 1,842-element browser smoke test pass. Isolated reproduction passes all 13 operations (including all 85 tests), reproduces 20 derived artifacts byte-identically, preserves all 1,842 canonical sources and allocates zero live identities, with no Legacy-RAD/scripts/historical configuration dependency. Detailed results are recorded there and in HCRL DR-2.

An actual isolated **Archi 5.10 import, native save and complete-model export** succeeded. Agent checks inspect actual native folders and names, unique element/relationship coverage, explicit source membership and retained View presentation. [Native-tree inspection aid](../native-tree-inspection.md), compressed [native save](../archi-transport/imported-model.archimate.gz), [returned XML](../archi-transport/returned-model.xml.gz), [execution log](../archi-transport/execution.txt), [raw comparison](../archi-transport/raw-comparison.json.gz) and [normalised review](../archi-transport/normalised-review.json) retain the exact evidence.

**Qualified transport, not whole-model raw equivalence.** Archi omits 3,724 redundant reference-label subtrees and 724 generated folder identifiers, producing raw information-loss and anonymous-order findings. Native element names/catalogue structure are verified separately, and missing generated labels/IDs are recoverable by exact regeneration. Archi adds `xml:lang="en"` to previously untagged values, including names/documentation on the 222 Iteration 001 relationships. All 21,194 raw semantic-attribute findings are specifically absent-to-English language additions; the normalised review exposes 222 relationship language changes, zero element changes and no exceptional conditions. No production comparator is relaxed. No identity, endpoint, text, property value or View content/tree is lost. The original canonical/template, candidate, exact export and native save remain retained; no returned control replaces an authoritative source.

The initial default Python lacked the declared dependencies; validation uses the existing dependency-complete tooling virtual environment. The first sandboxed Archi attempt lacked display access; isolated desktop-display execution then succeeded. An initial integration test exposed the retired RAD XML-folder correspondence and was corrected within this scope. Working logs are not treated as architectural authority.

## Candidate, unresolved review and Git state

Import the exact [candidate Open Exchange](../../../../../archimate/published/1.1.17/Harmonia-Architecture.xml), SHA-256 `b3c1bd71497497eb64e39e754dc69443d8012c762ffa00b9221116f12f3578a1`, into Archi 5.10. The Manifest Exchange copy has identical bytes; narrative source correspondence and publication metadata are current. No PDF compilation claim is made. [Review index](../review-index.md) links the reviewable result.

Solution Architect structural inspection and confirmation remain pending under Review Praxis §5, including treatment of the documented transport qualifications. This report does not resolve Iteration 001's existing naming/source uncertainties or supply its final result acceptance. No new architectural meaning is inferred to close those matters.

Changes remain uncommitted in the shared working tree, preserving all pre-existing Iteration 001 work. [Git-state evidence](../git-state.json) records HEAD, branch, tracked differences and complete status. No staging, commit, branch mutation, merge, rebase, tag or push occurred. No production implementation changed. The 1.1.17 candidate and supporting evidence are present in that working tree; they are not claimed recoverable from the earlier HEAD commit. HCRL remains pending; no final architectural acceptance is asserted.
