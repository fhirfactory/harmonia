| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |
| [CR-001](#cr-001) | Candidate strict Archi transport qualification | Accept | Resolved |

# Mnemosyne incorporation — transport Change Review

**Current final decision:** CR-001 is **Accepted — Qualified** for release 1.1.23,
with native Disposition `Accept` and Outcome `Resolved`. The final closure Task
linked below supplies attributable acceptance. Earlier pending/On-Hold statements
throughout this document are retained pre-acceptance history; strict FAIL remains.

This specialised review feeds [HCRL DR-3](../../../../../review/1.1.23/hcrl-1.1.23-001.pending.md#dr-3).
The governed semantic record remains HCRL. This is a canonical-source ADD activity,
not an architect-authored model submission; there is no incoming architectural
change requiring automatic application. The returned model below is an exact
no-edit **transport control**, never an authorised replacement for Markdown or
the maintained View template. Accepted MP-01–MP-08 semantics are not reopened.

## Review basis and decomposition

- Accepted predecessor: root release **1.1.22**, entry Git
  `fde98cde3c44b4f4b89e7f3a29765e1a44fca515` on `Mneme-and-Mnemosyne-Build`.
- Exact transport baseline: [candidate XML](candidate/reader/Harmonia-Architecture.xml),
  SHA-256 `eb4c340c25a2d8ee114053a445056b1afaf4b000e38b15c2f0259384e7f5f6a9`.
- Exact return: [returned-model.xml.gz](archi-transport/returned-model.xml.gz),
  uncompressed SHA-256 recorded in [transport summary](transport-summary.json).
- Operator: Codex using installed supported Archi 5.10, temporary configuration,
  actual import, native save and Exchange export with folders and `en` language.
  [Successful execution log](archi-transport/successful-import-save-export.txt)
  and [native save](archi-transport/native-model.archimate.gz) are retained.
- Existing unmodified strict verifier, raw comparator and canonical field checks
  supply the evidence. [Raw findings](raw-transport-comparison.json.gz) remain intact.
- One CR covers the candidate's outstanding transport-gate treatment. Raw record
  counts are not independently authored architectural decisions. Separate HCRL
  DR-4 retains naming/contract/temporal qualifications; DR-1/DR-2 retain incorporation
  meaning and input acceptance.

The intended transition is 1.1.22 → proposed 1.1.23; actual accepted resulting
state/release is **Pending**. Canonical-source changes, before-state recovery and
exact accepted input are documented in the iteration's Task and correspondence.
No historical reconstruction or amendment to comparison policy is authorised.

<a id="cr-001"></a>
## CR-001 — Candidate strict Archi transport qualification

**Disposition:** Accept  
**Outcome:** Resolved  
**Solution Architect decision:** Accepted — Qualified, release 1.1.23 only.  
**Outcome Reason:** The Solution Architect explicitly accepts the bounded
investigation and release-specific qualifications through the
[final closure Task](../../before/ai-agent-tasks/unresolved-mnemosyne-final-acceptance-and-release-closure.md).
The transport uncertainty is dispositioned; strict verification remains FAIL.
This resolves the CR by governing decision, not by lossless transport or remediation.  
**Authorised Reconciliation Instruction:** Record the accepted qualification and
complete release 1.1.23 through existing controls. Do not apply the transport return
or change canonical architecture, authored Views, template or strict verifier.  
**Processing record:** Bounded comparison completed; final qualified acceptance
recorded. Original findings, before-review snapshots, models and failed strict
outcome are preserved. No transport return was applied.

**Historical observations/checkpoints below are preserved.** Earlier On-Hold /
Unresolved statements describe prior stages; the current table and final decision
supersede them. Native CR Disposition is `Accept`, Outcome is `Resolved`, and the
Solution Architect's exact acceptance label is **Accepted — Qualified**.

### Observations and consequences

All 6,557 canonical element identifiers, native types, specialisations, names,
documentation and four governed identity/name property values are preserved.
No element, relationship or View ID is missing or added. Nevertheless **6,456 of
9,376 relationship records** and **15 of 1,304 View records** differ. The strict
verifier correctly reports **FAIL: Relationship/View transport drift**.

Raw classifications are 4,693 equivalent normalisations, 18,486 information-loss
findings, 112,018 semantic-difference findings and 16,424 unexplained differences.
These are conservative field/subtree observations, not counts of independently
established architectural defects. No category is silently normalised to PASS.

The changed relationship and View ID sets match the accepted predecessor's failing
sets. The predecessor investigation describes language annotations, property rekeys,
unordered collections, generated labels/folder IDs, default styles and connection
sequence changes. That investigation and its explicit **1.1.22-specific** acceptance
remain governing history for that release, not proof that this candidate has
identical transformations or authority to accept them. The current bounded
[ID-set correspondence](predecessor-transport-correspondence.json) establishes only
its stated comparison. Pixel equivalence, lossless transport and harmlessness of
default-style/stacking changes are not established here.

### Bounded retained-evidence comparison — follow-up findings

The [Solution Architect Task](../../before/ai-agent-tasks/unresolved-cr001-transport-reconciliation.md)
authorises this comparison and records completed/accepted native Archi-tree and
generated HTML-reader visual review for this candidate. This is separate from
CR-001 transport adjudication and whole-HCRL/result confirmation. The
[before-review snapshot](../../before/cr001-transport-reconciliation/change-review.before.md)
preserves the prior checkpoint; its limited ID-set evidence is supplemented by
actual-record comparison, not silently promoted into equivalence.

The [bounded findings and qualification](../cr001-transport-reconciliation/README.md),
[comparison](../cr001-transport-reconciliation/comparison.json) and
[per-record evidence](../cr001-transport-reconciliation/per-record-correspondence.json)
establish:

- All **9,314 existing relationship source records**, and their corresponding
  **returned records**, match across releases, retaining language, literal Property
  IDs, text, attributes and child order. No materially different transformation
  of an existing relationship is detected.
- All **62 new relationships** preserve their checked fields/language/order after
  exact unique Property-key resolution. There are **zero raw findings inside
  those records**; their catalogue-label omissions are separately accounted for.
- All **1,304 Views** match across releases both before and after transport,
  including the exact **238** default-style additions/native attributes and **15**
  complete connection-sequence transformations. No additional geometry, routing,
  topology, node order, explicit-style or other View difference is detected.
- All **151,621 raw findings** are accounted for by the same seven mechanisms:
  exact-key rekeys, unordered keyed collections, absent-to-`en` annotations,
  reconstructible labels, generated-folder IDs, explicit effective defaults and
  source-containment connection order. **Zero new or unaccounted patterns** remain;
  original raw classifications, including `UNEXPLAINED DIFFERENCE`, are unchanged.
- The **272** extra observations are **110** annotations on eleven new elements,
  **73** reconstructible labels, their **73** child-list observations and **16**
  additional omitted `catalogue-source-` folder IDs. All prior omitted-folder
  records are unchanged. The candidate loses **2,553** generated folder IDs in
  total, none within authored View hierarchy. No extra graph/text/value loss is
  detected; these additional representation-ID losses remain explicit.

**Recommendation, not disposition:** candidate-specific acceptance with these
bounded qualifications and exact evidence, retaining strict **FAIL**, 238
implicit-style-state changes, fifteen global-sequence changes and 2,553 generated
folder-ID omissions. No lossless, rendered-pixel or cross-platform equivalence,
candidate second-pass stability, or future exemption is established. No architectural
correction is demonstrated as necessary. If representation losses are unacceptable,
targeted machinery remediation requires separate authority; none is applied here.
Human tree/reader acceptance does not prove pixel equivalence. The full predecessor
investigation and actual Archi runs were not repeated.

Required architecture tests **PASS** (90 tests, no failures/errors/skips), as do
five existing diagnostic boundary tests. The follow-up Task event/prefix remains
explicitly unresolved. Diagnostic explanation changes no verification policy.
The [formal comparison Report](../ai-agent-reports/20261111T081809Z-cr001-transport-reconciliation.md)
records actual execution, preservation, limits and resulting Git state.

### Solution Architect decision

**Transport adjudicator:** Mark Hunter, Solution Architect.  
**Decision:** Accepted — Qualified, specifically for release 1.1.23.  
**Attributable authority:** Final closure Task linked above; its instruction-entry
event/prefix is unavailable and remains explicitly unresolved under the accepted
provenance qualification. No decision timestamp is invented.  
**Rationale and scope:** The accepted bounded comparison explains all findings with
the established seven mechanisms, preserves existing relationships/Views at the
checked dimensions and detects no new architectural information loss. Retain the
2,553 generated-folder-ID omissions, 238 implicit-style-state changes and fifteen
global-order transformations; no lossless/pixel/cross-platform claim or future
exemption. The native-tree and HTML-reader visual reviews and whole incorporation
result are accepted. DR-4 naming, vocabulary and temporal qualifications remain.
No architectural remediation or production-verifier change is authorised.

Accepted predecessor publications remain byte-unchanged. Candidate review,
reproducibility and reporting may complete independently; accepted publication and
release completion are now authorised through their ordinary controls. Transport-return
application remains prohibited.
Preserve these inputs, return, native save and findings while that disposition or
recovery/validation dependency remains open. This record supplies no disposal authority.
