<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Information Unit incorporation — review guide

**Current candidate: v2, development release 1.1.22; pending result acceptance.**
Review [HCRL 001](../../../../../review/1.1.22/hcrl-1.1.22-001.pending.md).
The accepted specification is unchanged; this review concerns its incorporated
result and the disclosed verification/naming qualifications.

[Formal execution Report](../ai-agent-reports/20261111T031941Z-information-unit-framework-incorporation.md)
records actual scope, reconciliation, checks, release and resulting Git state.

| Review material | Purpose |
| --- | --- |
| [Final Exchange](candidate-v2/reader/Harmonia-Architecture.xml) | Exact current candidate; SHA-256 `8ef02598667b643850efca6bd3a8f3f8438f08dce1c764c190018de3a410e312` |
| [Interactive reader](candidate-v2/reader/continuum.html) and [reader guide](candidate-v2/reader/README.md) | Inspect logical information structure, existing Feature ancestry, cross-layer references and preserved supersession |
| [Narrative LaTeX](candidate-v2/narrative/continuum-rad.tex) | Generated narrative candidate; PDF compilation not performed |
| [Native Archi model](archi-transport/native-model.archimate.gz) | Decompress and open in supported Archi; exact imported/saved v2 model, not an accepted canonical replacement |
| [Imported tree inventory](archi-transport/imported-tree.html) | Inspectable extraction from that native save; supports, but does not replace, actual workbench review |
| [Transport summary](transport-summary.json) | Strict transport FAIL and precise population/field checks; raw comparison and returned Exchange retained in the adjacent transport directory |
| [Reconciliation](reconciliation.json), [IU-01–IU-13 coverage](adjudication-coverage.json), [source succession](source-succession.md) | Historical Capability/Feature/Requirement proposals, accepted hierarchy reuse and exact canonical/source correspondence |
| [Change inventory](change-inventory.json) and [preservation](preservation-validation.json) | Four additions, 43 modified canonical files, 50 new relationships; no existing identity/edge loss |
| [Independent generation](independent-generation.json) | All 18 artifacts plus Manifest reproduce from maintained inputs; no allocated identities |

The [original candidate](candidate/reader/Harmonia-Architecture.xml) and
[first-candidate verification](first-candidate-verification/transport-summary.json)
are retained for correspondence. V2 explicitly adds Content-aware/Content-neutral
participation and appropriate Provenance for transformed governed IUs to
InformationRepresentation. No graph/type/count changes accompany that correction.

Suggested structural inspection follows the reused conceptual IU and logical
Calliope InformationUnit to its two constituents, InformationRepresentation and
Information Unit Metadata, then the common Topic and TopicMask models. Inspect the
metadata contexts, single-representation invariant, version distinctions and
Mneme/Mnemosyne/Themis/Kleio/Dokimasia references. Inspect existing Feature ancestry
and Motivation applicability without interpreting it as implementation satisfaction.
Check the bounded historical supersession and deep navigation in the reader.

Actual Archi import/native save/export **PASS**. Strict relationship/View fidelity
**FAIL** remains undispositioned: 6,456 relationship records and 15 View records
differ despite preserved ID populations and checked element fields. Raw comparator
labels do not supply architectural conclusions. No returned bytes replace canonical
Markdown or the authored View template. Human visual/tree/reader review and
whole-HCRL confirmation have **not** occurred.

The authorised [transport investigation](../transport-investigation/README.md)
now explains every raw category and establishes the same relationship/View
behaviour against accepted 1.1.21. No IU graph/text alteration was found; the exact
gate remains FAIL, with global connection sequence and default-style-state
qualifications preserved. Consult HCRL DR-3 and the investigation Report before
interpreting the earlier raw totals as architectural change counts.

Please disposition **DR-3's exact transport failure** before dependent finalisation,
record the applicable human structural review, and review **DR-4's four unresolved
DataObject canonical-name qualifiers**. Whole-HCRL result confirmation and bounded
Minutiae acceptance remain pending. The requirement for disposition comes from
the repository's [fail-closed governance](../../../../../../governance/architecture-governance.md#governance-enforcement-and-fail-closed-authority)
and [Review Praxis §§5–6](../../../../../../governance/architecture-review-praxis.md#5-report-and-verify-the-incorporated-result).
No accepted publication, completed registration, iteration advance, commit or push
has been performed.
