<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# CR-001 — bounded comparison of retained 1.1.22 and 1.1.23 transport

[Formal execution Report](../ai-agent-reports/20261111T081809Z-cr001-transport-reconciliation.md).

**No new transformation mechanism or additional architectural graph/text/value
loss was detected.** All existing relationship and View source/returned records
match the accepted predecessor at the checked XML dimensions. The candidate's
additional findings are fully accounted for by previously established mechanisms.
**Strict transport remains FAIL; CR-001 remains On-Hold / Unresolved.**

The [operative Task](../../before/ai-agent-tasks/unresolved-cr001-transport-reconciliation.md)
authorises only this bounded verification within **1.1.23/001**, and records the
Solution Architect's completed/accepted native Archi-tree and generated HTML-reader
visual review. That acceptance is distinct from transport disposition and whole
HCRL confirmation. The Task event/prefix is explicitly unresolved; no time is guessed.

## Exact evidence and method

[Input and preservation pins](../../before/cr001-transport-reconciliation/inputs-and-preservation.json)
identify the four exact Exchange originals/returns, both native saves, original
raw findings, accepted predecessor analysis and unmodified diagnostic. The original
1.1.22 candidate-v2 SHA-256 is
`8ef02598667b643850efca6bd3a8f3f8438f08dce1c764c190018de3a410e312`;
its return is `d79a958a14639288c23bcf2de9653543712a23c6333c71cb1ec862ff904ead62`.
The 1.1.23 candidate SHA-256 is
`eb4c340c25a2d8ee114053a445056b1afaf4b000e38b15c2f0259384e7f5f6a9`;
its return is `617f10f2fa8bcd87bfbab2a0858b60ba72a0a3ad0b07c8530f99f731bdea69cb`.
Compressed-container hashes and native-save identities are also pinned.

The completed [1.1.22 analysis](../../../../1.1.22/001/after/transport-investigation/candidate-v2-analysis/analysis.json)
and [accepted predecessor HCRL DR-3](../../../../../register/1.1.22/hcrl-1.1.22-001.complete.md#dr-3)
establish the seven mechanisms and release-specific qualifications. No Archi
run, bytecode investigation, second-pass control or full predecessor analysis was
repeated. The same unchanged diagnostic was applied once to the retained candidate
original/return/native/raw inputs. Its
[candidate analysis](candidate-analysis/analysis.json) accounts for every raw finding
and separately checks graph/text/values, languages, navigation omissions, styles
and connection sequences. Existing raw classifications and strict results are intact.

The bounded [comparison script](compare-retained-transports.py) independently
parses and offline-validates all four Exchange inputs, compares existing records
across releases and tests each new relationship. Its all-field signatures preserve
language, value multiplicity and child order, including properties, nodes,
connections, styles and bendpoints. Only XML formatting whitespace and namespace
prefix spelling are immaterial. Cross-release checks retain literal Property IDs;
source/return checks of new relationships resolve exact unique Property keys.
All 69 Property-definition records are also identical across releases at each
stage. No naming-based architectural identity inference is used.

- [Comparison conclusions and pattern deltas](comparison.json).
- [Per-relationship and per-View source/return digests](per-record-correspondence.json).
- [Candidate diagnostic execution](candidate-analysis-execution.txt) and
  [bounded comparison execution](comparison-execution.txt).
- [Tests and retained logs](test-results.json).

## Confirmed inherited behaviour

| Mechanism | Accepted 1.1.22 | Candidate 1.1.23 | Significance |
| --- | ---: | ---: | --- |
| Property-definition rekeys | 69 | 69 | Exact unique keys/content preserved; representation IDs change. |
| Unordered keyed collection reorders | 4,624 | 4,624 | 4,409 element Property collections, 212 relationship collections, three top-level collections; membership/content preserved. Visual order is not included in this equivalence. |
| Absent-to-English language annotations | 111,908 | 112,018 | Existing strings/values unchanged; 110 additions occur on the eleven new elements. No pre-existing tag changes/removals; inputs contain only `en` or absent annotations. Metadata equality/multilingual equivalence is not asserted. |
| Reconstructible catalogue-reference labels omitted | 15,860 | 15,933 | Eleven new element labels and 62 named-relationship labels account for the increase. Labels exactly reconstruct from retained Names/IDs; no custom alias loss. |
| Generated source/catalogue folder IDs omitted | 2,537 | 2,553 | Real representation information loss, including 16 additional generated source-folder IDs; none belongs to authored View hierarchy or canonical identity. |
| Implicit connection styles made explicit | 238 | 238 | Exactly the same connections, native attributes and effective black line/text, Sans 9 defaults. Unspecified/default-selection state is lost; cross-platform/future preference equivalence remains unproved. |
| Global connection order becomes source-containment order | 15 Views | 15 Views | Exact same before/after sequences and native containment order, including preserved per-source sequence. Original global ordering is lost; stacking/crossing-line pixel effects remain unproved. |

The **272** additional raw observations are precisely **110 + 73 + 73 + 16**:
language additions, omitted labels, corresponding child-list observations and
omitted folder IDs. There are **zero new or unaccounted transformation patterns**.
Raw `UNEXPLAINED DIFFERENCE` remains **16,424**, decomposed as 15,933 label-removal
child lists, 238 style insertions, 238 corresponding child lists and 15 sequence
changes. Diagnostic explanation does not rewrite that retained classification.

The previous 2,537 omitted-folder records are unchanged and remain a subset of
the candidate's omissions. All 16 additions have `catalogue-source-` IDs:
ten new behaviour/service sources and six existing Motivation sources that now
have outgoing relationships. Their exact IDs/labels are in `comparison.json`.
All generated catalogue labels, ordering, references and other tree metadata
remain equal after separately accounting for the documented omissions.

## Existing relationships, new relationships and Views

All **9,314 existing relationships** have identical source records across releases
and identical returned records across releases. This compares actual attributes,
language, text, properties and ordering, beyond merely matching the 6,456 affected
IDs. No materially different transformation of an existing relationship is detected.

All **62 new relationships** are present after transport and preserve every checked
field, language, value/multiplicity and ordering after exact Property-key resolution.
There are **zero raw findings inside these new relationship records**. Their
62 generated catalogue labels are omitted under the inherited reconstructible-label
behaviour; that is distinct from losing relationship definitions or names.

All **1,304 View records**, including the fifteen affected Views, are identical
across releases before transport and again after transport. All **4,599 nodes**
and **2,042 connections** retain the checked fields, membership, references,
endpoints, geometry, nesting/node order, bendpoints, metadata and explicit styles.
The added-style records/native attributes and complete connection-sequence records
match the predecessor exactly. There are zero other View differences after those
two separately inventoried transformations; no new presentation-relevant XML
transformation was found. This does not claim rendered-pixel equality.

## Consequences, remaining uncertainty and recommendation

Equivalent serialization comprises the exact-key rekeys and non-visual unordered
collection reorders. Language annotation is a real metadata change with no detected
graph/text/value change. Redundant labels are recoverable. Actual representation
loss remains generated folder IDs, implicit style-selection state and original
global connection order. No evidence shows additional architectural information
loss or new semantic/presentation transformation beyond these predecessor categories.
The 16 additional folder-ID omissions are explicitly included, rather than calling
the candidate lossless or claiming all loss counts are identical.

Recommend **candidate-specific acceptance with these bounded qualifications**, if
the Solution Architect agrees: retain strict FAIL, the exact source/return/native/raw
evidence, all seven mechanisms, 2,553 generated-folder-ID omissions, 238 implicit-style
state changes and fifteen global-sequence changes; assert no lossless transport,
rendered-pixel or cross-platform equivalence, or general future exemption. No
canonical architectural remediation is demonstrated as necessary. If those
representation losses are unacceptable, targeted source-order/default-state or
catalogue-ID preservation would require a separately authorised machinery change;
none is proposed as an authorised implementation here.

The accepted human native-tree/HTML-reader review is recorded with its actual scope;
it is not a before/after rendered-pixel test. No new candidate second-pass stability
run was performed, and the predecessor's stability result is not transferred by
assumption. Transport acceptance and whole-HCRL/result confirmation remain for the
Solution Architect. [CR-001](../mnemosyne-incorporation/change-review.md#cr-001)
remains **On-Hold / Unresolved**; no waiver, release increment, publication, iteration
advance, commit or push is performed.
