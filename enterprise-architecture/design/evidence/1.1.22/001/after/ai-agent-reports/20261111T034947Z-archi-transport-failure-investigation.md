Report-Produced-At: 2026-11-11T03:49:47.028012Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi transport failure investigation — execution Report

**The failure is inherited Archi serialization behaviour, not a demonstrated
Information Unit architecture defect.** Seven independently checked mechanisms
explain all **151,349 raw findings**. No element/relationship graph or text/value
alteration was found. There are genuine representation differences, including
lost derived folder IDs and changes to connection ordering and implicit style
state. **Strict verification remains FAIL for both 1.1.22 and 1.1.21.**

The [issued Task](../../before/ai-agent-tasks/20261111T033823Z-archi-transport-failure-investigation.md),
instruction-entry **2026-11-11T03:38:23.096Z**, authorises this investigation within
existing **1.1.22/001**, including bounded correction only of demonstrable
comparison defects. [HCRL DR-3](../../../../../review/1.1.22/hcrl-1.1.22-001.pending.md#dr-3)
records the semantic conclusions; [evidence navigation](../transport-investigation/README.md)
links the diagnostic outputs and reproducible commands. The earlier incorporation
Report and original comparison evidence are preserved; their raw categories remain
historical observations, not adjudicated semantic-loss counts.

## Exact inputs and method

The existing candidate-v2 Exchange is unchanged, SHA-256
`8ef02598667b643850efca6bd3a8f3f8438f08dce1c764c190018de3a410e312`.
Its original returned Exchange, SHA-256
`d79a958a14639288c23bcf2de9653543712a23c6333c71cb1ec862ff904ead62`,
native save and compressed raw comparison remain at their original paths under
`after/information-unit-incorporation/`. The [entry pins](../../before/transport-investigation-inputs.json)
and [preservation check](../transport-investigation/preservation-check.json)
verify **6,553 input files unchanged**, including every canonical source,
navigation/Manifest, release authority and those four transport inputs.

The accepted predecessor control uses immutable 1.1.21 Exchange, SHA-256
`6471411187a5ff72b005c9b9855c48beb3d86b7ae6c7c556a4eb58338011a739`.
Actual Archi 5.10 import/save/export ran using the same installed application,
`--xmlexchange.exportFolders --xmlexchange.exportLang en`, isolated temporary
configuration/workspace and a 120-second bound. It **PASSed in 12.139 seconds**.
The exact [execution/log/native/return](../transport-investigation/baseline-1.1.21-control/execution.json)
are retained. Returned XML SHA-256 is
`0c1422c1f0dc609e9c963eb7a1d66b169e758f437e859e7e1d5189060724e89f`.
Previously established GTK display restrictions justified the same approved
outside-sandbox execution; no accepted file was mutated.

The investigation first sampled every raw category, then exhaustively accounted
for every finding. An independent evidence diagnostic compares elements and
relationships by existing IDs, resolves Property-definition keys, and checks all
text, property values/multiplicity, native types, endpoints and other attributes.
Language is a **separately inventoried dimension**, not silently discarded.
Navigation comparison removes only separately counted derived IDs and labels
proven reconstructible from retained records. View checks retain nodes, nesting,
order, geometry, routing, attributes, metadata and explicit styles; global
connection sequence and added defaults are inventoried independently. Nothing
changes the production comparator or strict verification policy.

[Candidate exhaustive analysis](../transport-investigation/candidate-v2-analysis/analysis.json)
contains counts and representative before/after evidence for every pattern.
[Installed importer/exporter bytecode excerpts](../transport-investigation/installed-archi-bytecode-evidence.txt)
and the plugin hash corroborate the observed mechanisms. This applies AX-04's
separation of machinery from meaning, AX-08's purposeful evidence, AX-14's semantic
distinctions and AX-15/AX-17's explicit uncertainty/authority. No architecture
specification or axiom is changed.

## Causes and significance

| Independently established mechanism | 1.1.22 count | Finding and qualification |
| --- | ---: | --- |
| Property-definition IDs rekeyed | 69 | Exact unique keys, definition metadata and all owners/values match. Representation ID change is semantically equivalent. |
| Unordered keyed collections reordered | 4,624 | 4,409 element Property collections, 212 relationship Property collections and 3 top-level collections. Membership/content preserved; no visual ordering is included in this equivalence. |
| English language annotation added | 111,908 | 99,680 element and 12,228 relationship attributes change precisely from absent to `en`; text and all other attributes remain unchanged. Exporter writes the configured language on emitted strings. No existing language tag or text is removed/changed; inputs contain only `en` or absent language tags. This is architecture-text/value-preserving annotation, but is not full metadata equality or a general multilingual-normalisation rule. |
| Redundant catalogue-reference labels omitted | 15,860 | 6,546 element Names, 1,553 relationship Names and 7,761 unnamed-relationship IDs reconstruct the exact generated labels. No custom alias is silently treated as redundant. References, ordered placement and all remaining tree metadata match. |
| Derived catalogue folder IDs omitted | 2,537 | Real representation information loss: import creates native folders, and export writes organisation items without their original IDs. All affected IDs belong to generated non-View catalogues. No canonical ID, authored View-folder ID, label, reference, folder metadata or hierarchy is lost. |
| Effective default styles made explicit | 238 connections | Original style is absent; the retained first native save has no explicit line/font attributes on these connections. Export writes effective black line/text and Sans 9 using its default color/font factories. No existing explicit style is removed/changed. The exchange loses the unspecified/default-selection state; future preference or platform effects are not proven equivalent. |
| Global connection order becomes source-containment order | 15 Views | Export traverses native `eAllContents`; each returned sequence exactly matches the retained native source-connection containment order. Order within each source is preserved. Original global XML connection sequence is not recoverable from that sequence alone. No topology or routing change occurs; crossing-line/stacking effects remain unverified. |

The first two groups account for the original **4,693 equivalent findings**.
The **15,860 labels** are additionally proved recoverable housekeeping by the
diagnostic, without reclassifying the retained raw report. The language group
preserves architectural strings and values while recording a real annotation
change. Default styles are qualified by the observed installation; they are not
declared universally presentation-equivalent.

Raw counts are larger than the underlying transformations. Label removal also
produces **15,860 child-list findings**, and style insertion another **238**.
These are not additional deletion/reordering events. Consequently:

| Retained raw classification | Count | Actual decomposition |
| --- | ---: | --- |
| Semantically equivalent normalisation | 4,693 | 69 key-resolved definition rekeys + 4,624 unordered collection reorders |
| Semantic difference | 111,908 | Language annotations only; no graph/text/value drift |
| Information loss | 18,397 | 15,860 reconstructible labels + 2,537 derived folder IDs |
| Unexplained difference | 16,351 | 15,860 label-removal child lists + 238 style insertions + their 238 child lists + 15 connection sequence changes |

There are **zero unaccounted transformation patterns**. A child-list observation
after inserting/removing a child is not, by itself, evidence of reordering
surviving children. Raw category labels are conservative XML observations, not
independent architectural decisions or an error count.

## Verified preservation and actual loss

All **6,546 elements and 9,314 relationships** preserve their existing IDs,
native types, endpoints, text, property values/multiplicity and non-language
attributes. There are **zero detected graph/text/value losses or alterations**.
The 6,456 changed relationship records contain the language additions above;
their relationship meaning is unchanged at these checked dimensions. Property
definition resolution preserves exact keys and metadata, not arbitrary name
matching between architectural elements.

All **1,304 Views, 4,599 visual nodes and 2,042 connections** retain membership,
element/relationship references, endpoints, geometry, nesting/node order,
bendpoints, metadata and existing explicit styles. After separately inventorying
the 238 style additions and 15 sequence changes, **zero other View differences**
remain. The entire ordered organisation tree, including authored View hierarchy,
empty folders and metadata, also matches after the documented catalogue omissions.

Thus actual representation loss comprises **2,537 generated folder IDs**, the
**original global connection sequence in 15 Views**, and the **unspecified style
state on 238 connections**. Redundant labels are reconstructible rather than
lost architectural content. The latter two presentation differences must not be
declared harmless solely from stable IDs or preserved geometry. No rendered-pixel
comparison, alternate-platform experiment or human structural acceptance occurred.

## Accepted-predecessor and second-pass controls

The [1.1.21 analysis](../transport-investigation/baseline-1.1.21-analysis/analysis.json)
finds **151,162 raw observations** and the **same seven mechanisms**. Existing
relationship records before transport are identical across the two releases;
their corresponding returned records are also identical. The **same 6,456
relationship IDs and 15 View IDs** differ in both comparisons. Every existing
View before and after transport matches between releases, including the same
238 added styles and exactly the same connection sequence transformations.
[Cross-release checks](../transport-investigation/cross-release-checks.json)
retain those independent equality results.

The entire 187-observation increase in 1.1.22 is explained by **40 generated
scalar language annotations**, **54 new catalogue-reference labels plus their
54 child-list observations**, and **39 derived source-folder IDs**. The first
corresponds to four new DataObjects; the label increase corresponds to those
four elements and 50 relationships. New relationship definitions were already
written with English annotation and introduce no additional relationship drift.
No IU-specific transport-failure pattern was introduced.

A second actual import/native save/export of the existing returned v2 Exchange
**PASSed in 12.641 seconds**. Its [execution and outputs](../transport-investigation/candidate-return-control/execution.json)
show **byte-identical returned Exchange XML**, SHA-256
`d79a958a14639288c23bcf2de9653543712a23c6333c71cb1ec862ff904ead62`.
This establishes a stable result under this installation/configuration. It does
not recover the omitted source distinctions or make the first round trip lossless.

## Comparison defects, verification and disposition

**No comparison-algorithm defect requiring a production correction was
demonstrated.** The raw comparator correctly exposes exact representation drift;
the strict verifier deliberately rejects it. The reporting hazard was interpreting
language/subtree/child-list categories as adjudicated semantic errors. The evidence
diagnostic separates those meanings without weakening or replacing either tool.
No production comparator, verifier, workbench or test expectation was modified.

| Verification | Result |
| --- | --- |
| Exhaustive category accounting and independent graph/navigation/View dimensions | **PASS**, with the separately recorded language, derived-ID, style and order differences |
| Equivalent actual 1.1.21 transport | **PASS** import/native save/export; same inherited differences |
| Actual second-pass v2 transport | **PASS**, exact XML byte equality with its input |
| Existing current-EA tests | **PASS**, 11 tests in 3.912 seconds |
| Existing normalised-review boundary tests | **PASS**, 10 tests in 0.114 seconds |
| New evidence-diagnostic adversarial checks | **PASS**, 5 tests in 0.017 seconds: type/direction/endpoints, property value/multiplicity, exact whitespace, language visibility, geometry/styles/bendpoint order and authored labels/metadata remain detectable |
| Required Maven architecture suite | **PASS**, 90 tests, zero failures/errors/skips; build 19.457 seconds |
| Replayed strict verification against original 1.1.22 candidate and actual 1.1.21 control | **FAIL** for both: Relationship/View transport drift; [commands/results](../transport-investigation/strict-verification-replays.json) |
| Original architectural inputs/transport evidence and comparison production files | **PASS**, unchanged |

No operation timed out or stalled. Bounded diagnostic harness setup corrections
were completed before the final analyses; they changed no governed input or
production comparison rule. The malformed-input test intentionally emits a FAIL
diagnostic and passes by verifying that fail-closed behaviour.

The defensible action is to **preserve the IU incorporation**, retain the strict
FAIL, and present DR-3's precise presentation-fidelity questions to the Solution
Architect. Remediation of original connection order or default-style state, or
any acceptance exception, requires an explicit scoped disposition before dependent
work. This investigation **proposes/applies no architecture alteration or waiver**.
Inherited behaviour and second-pass stability do not grant acceptance. Existing
DR-4 naming qualifications, human structural review and whole-HCRL result
confirmation remain separate pending obligations.

Release authority remains **1.1.22 / previous 1.1.21**, iteration **001**, on
`Mneme-and-Mnemosyne-Build`, HEAD
`cb21ca0c5291f726f4babc7e59dabeba1a6c302c`. This activity adds its Task, Report and
purpose-selected control/analysis/check evidence, and updates the pending HCRL and
review-guide cross-references. The pre-existing uncommitted IU incorporation is
preserved. No canonical/application/production-tool change, release/iteration
advance, publication, completed registration, staging, commit or push occurred.
Post-emission link/evidence/diff checks are maintenance within this same boundary.
