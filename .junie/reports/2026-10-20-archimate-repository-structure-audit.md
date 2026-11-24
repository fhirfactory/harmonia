<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate repository structure audit — 2026-10-20

**Analysis only.** This report records purpose, provenance, dependencies and structural findings. It authorises no cleanup, relocation, regeneration, governance amendment or release change. The report is an execution artefact, not architectural authority.

The recovery boundary is **`8f3244b1af1481885b578f3749b35ebf0ffdc6a6`**, `Harmonia Design - Information Architecture Design (II)`, committed 2026-10-20 12:31:49 +11:00. At audit entry, `HEAD`, `Harmonia-GovernedAccess` and the local remote-tracking reference `origin/Harmonia-GovernedAccess` identified that commit, and ordinary Git status was clean. No fetch was performed; agreement with the local remote-tracking reference corroborates the user's committed/pushed baseline statement without independently querying the server. Root `architecture-release.json` records **1.1.0**, predecessor **1.0.10**. The published 1.1.0 XML SHA-256 is `4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1`.

The user confirms that the Solution Architect has inspected RAD-1.1.0 in Archi. Several retained promotion records still say final inspection is pending. This audit preserves those records and distinguishes their recorded handoff state from the user's later confirmation.

**Principal result:** the current Praxis and shared implementation already have stable homes. They do not have to be reconstructed from Run-001 or Run-002 to prepare a new review. However, current publication/replay, governance-transition verification, regression fixtures and specialised evidence verification remain coupled to historical paths. The published recovery boundary also omits locally present, hash-referenced evidence files. File count alone substantially understates that second problem and overstates the amount of independently maintained Python source.

## 1. Current repository map

Paths below are relative to repository root. The tree describes actual responsibilities, including areas omitted from the current workspace diagram.

```text
AGENTS.md                                  repository instructions; trigger entry
architecture-release.json                  single current release authority
docs/AGENTS.md                             inherits root instructions
docs/markdown/governance/                  current governed contracts and registers
    archimate-round-trip-engineering-praxis.md
    architecture-authoring-praxis.md        serial authoring/submission lifecycle
    archimate-ingestion-change-review.md    CR/adjudication/execution contract
    archimate-ingestion-publication-semantics.md
    canonical-identity-and-naming.md
    architecture-modelling-strategy.md
    archi-navigation-and-view-convention.md
    baseline/                              governed captures, amendments, projections
    architecture-identity/                 identity inventory/adoption/binding material
    templates/                             review template
docs/archimate/
    README.md                              workbench/workflow entry and navigation
    framework/
        tools/                             shared executables/libraries; bounded adapters
            reconciliation/                accepted-information adapter and instructions
        tests/                             maintained regressions and fixture locators
        schemas/                           vendored offline XSDs and provenance/licence
        baseline/                          historical framework baseline and locks
        Makefile, requirements.txt         build/check interface and Python dependency
        historical-locations.json          exact relocation map for frozen locators
        governance-transitions.json        registered governance hash transitions
        historical-workspace-readme.md     preserved former operating description
        roundtrip-increment-1.md            preserved earlier diagnostic procedure
    governance-tooling/
        1.0.14/                            explicitly non-RAD normalisation evidence
        migrations/2026-10-19-reclassify-1.0.14/
                                           relocation proof and pre-change consumers
    processing/
        publishing/<release>/              publication work, captures, proof and reports
        ingestion/<baseline>/run-NNN/       detection, review, adjudication and execution
        machinery-validation/information-architecture/
            specimen.json                  frozen baseline/submission/control locators
            run-001/                       naming candidate and validation evidence
            run-002/                       workspace candidate, archived machinery,
                                           governance transitions and correction history
    exchange/
        publish/<release>/                 seven finished XML publication packages
        ingest/<baseline>/                 architect exports/native models; local backup
.junie/plans/, .junie/reports/              execution plans, reports, probes and evidence
scripts/                                   release/identity/RADS support and tests
docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/
                                           pinned historical recovery inputs/tools
```

There is **no top-level `docs/archimate/source/` directory**. Durable semantic source is largely in RADS; publication-era source captures are also retained under processing and the historical LaTeX tree. A directory named `baseline` can therefore mean either governed architectural captures in RADS or historical regression specimens in the framework. A directory named `governance` inside a run means frozen transition evidence, not a second current policy set.

The three apparently overlapping areas have distinguishable intended roles:

| Area | Actual role | Authority implications |
| --- | --- | --- |
| `framework/` | Reusable code/configuration, tests, schemas, historical replay adapters and instructions | Executable mechanism; it cannot decide architectural acceptance. Some files deliberately support only old releases. |
| `governance-tooling/` | Evidence of internal tooling development and correction of the former 1.0.14 release classification | Its name does not make it current governance or a competing executable framework. The current release is not 1.0.14. |
| `processing/` | Run/release-specific inputs, outputs, decisions, validations, source captures and before states | Mostly execution/evidence material, but some current code reads it as an indispensable input. Its contents are not uniformly disposable intermediates. |

The [workspace README](../../docs/archimate/README.md), [framework README](../../docs/archimate/framework/README.md), [processing README](../../docs/archimate/processing/README.md) and [non-RAD evidence README](../../docs/archimate/governance-tooling/README.md) describe these boundaries, with the documentation limitations in sections 8 and 11.

## 2. File population and size analysis

### Measurement basis

The inventory used `git ls-files -z`, recursive filesystem enumeration, file sizes, SHA-256 hashes and read-only gzip decompression. Counts refer to files, not directories. Unless labelled **workspace**, tables count tracked files at the recovery boundary, before creating this report. Sizes are sums of file lengths, **MiB = 1,048,576 bytes**; compressed files contribute their stored compressed size. They are not allocated disk blocks, expanded payload sizes or attributed Git-pack storage. Descendant tables overlap their parent totals.

All 4,547 tracked repository files total **829,901,475 bytes (791.46 MiB)**. `docs/archimate` accounts for **1,829 files, 562,128,030 bytes (536.09 MiB)**: about 40.2% of tracked files and 67.7% of tracked file bytes. This does not imply the same proportions of Git storage, where identical blobs, delta compression and historical revisions affect size.

| Major location | Tracked files | Tracked MiB | Workspace files |
| --- | ---: | ---: | ---: |
| `docs/archimate/README.md` | 1 | 0.01 | 1 |
| `docs/archimate/exchange/` | 14 | 225.48 | 15 |
| `docs/archimate/framework/` | 89 | 1.56 | 134 |
| `docs/archimate/governance-tooling/` | 39 | 20.30 | 39 |
| `docs/archimate/processing/` | 1,686 | 288.73 | 1,721 |
| **`docs/archimate/` total** | **1,829** | **536.09** | **1,910** |
| `.junie/plans/` | 117 | 2.73 | 117 |
| `.junie/reports/` | 281 | 124.23 | 281 |
| **`.junie/` total** | **398** | **126.95** | **398** |

The ArchiMate workspace total is **585,680,993 bytes (558.55 MiB)**. Its 81 additional, ignored files are 45 Python bytecode files, 35 raw logs and one native-model `.bak`. Their combined 22.46 MiB is dominated by the 21,794,328-byte backup. A clean ordinary Git status does not expose this population. All `.py` source files counted here are tracked; the bytecode files are not source.

| Subpopulation | Tracked files | MiB | Interpretation |
| --- | ---: | ---: | --- |
| `exchange/publish/` | 8 | 121.27 | Seven generated XMLs and the package index |
| `exchange/ingest/` | 6 | 104.22 | Three XML exports, two native `.archimate` models and README |
| `processing/publishing/` | 543 | 112.48 | Publication-specific outputs, histories and validation |
| `processing/ingestion/` | 238 | 107.50 | Submission review/reconciliation evidence, including README |
| `processing/machinery-validation/` | 904 | 68.76 | Specimen manifest plus two runs |
| RADS `governance/baseline/` | 26 | 11.84 | Mixed governed captures, projections, authority and history |
| RADS `governance/architecture-identity/` | 11 | 4.02 | Identity supporting records and projections |
| All RADS `governance/` | 54 | 18.21 | Includes the two preceding populations |
| LaTeX `v0.2-draft/` recovery tree | 77 | 24.12 | Historical dependency outside the ArchiMate tree |

All `.junie` counts include unrelated runtime/security work. A clearly bounded **filename-based navigation subset**, selecting paths containing `archimate`, `archi-`, `roundtrip`, `run-002`, `information-architecture`, `canonical-identity`, `change-request`, `change-review` or `rad-1.1.0`, contains **143 files / 113.61 MiB**. This is a reproducible search subset, not a claim that every architecture-related execution artefact can be classified by its filename.

### Release and run populations

| `processing/publishing/` release | Files | MiB | Main purpose |
| --- | ---: | ---: | --- |
| 1.0.1 | 2 | 0.02 | Early publication report/guide |
| 1.0.4 | 4 | 0.25 | Locked capability/navigation publication evidence |
| 1.0.5 | 27 | 21.15 | Historical v0.2 recovery, source manifest and recovered model |
| 1.0.6 | 88 | 1.09 | Synchronisation and subsequent preservation/inspection checks |
| 1.0.7 | 21 | 16.37 | Canonical identity establishment and source history |
| 1.0.8 | 68 | 13.97 | Workspace migration inventories, movement/reference audits and replay |
| 1.0.9 | 216 | 32.38 | Accepted Business changes and Home/View/containment completions |
| 1.0.10 | 92 | 21.05 | Initial IA population and canonical-name completion, before states and transport |
| 1.1.0 | 25 | 6.20 | Current publication evidence, actual transport and release audit |

The publishing totals sum to 543. Ingestion contains **109 files / 31.42 MiB** under baseline 1.0.8, **128 / 76.08 MiB** under baseline 1.0.10, plus its README. The latter's `run-002/` includes the 22,393,508-byte architect no-edit export, review, detection, reconciliation and validation.

Machinery-validation contains **Run-001: 21 files / 23.89 MiB**, **Run-002: 882 / 44.87 MiB**, and the 713-byte shared specimen manifest. Across `docs/archimate`, paths containing an actual numeric `run-NNN` directory account for **1,136 files / 173.65 MiB**; all other paths account for **693 / 362.44 MiB**. That remainder includes release-specific history and generated packages: **outside a run directory does not mean reusable source**.

Run-002's six top-level `validation/pre-*` archives alone contain **718 files / 19.81 MiB**, 39.3% of the entire tracked ArchiMate file count:

| Archive | Files | Stored bytes |
| --- | ---: | ---: |
| `pre-containment-correction/` | 131 | 3,276,990 |
| `pre-element-projection-correction/` | 121 | 2,849,658 |
| `pre-view-navigation-guard/` | 94 | 3,395,919 |
| `pre-context-completion/` | 93 | 3,386,040 |
| `pre-contained-leaf-correction/` | 181 | 4,418,076 |
| `pre-contained-leaf-regression-completion/` | 98 | 3,444,469 |

### Extensions and payload types

| Final extension/type in `docs/archimate` | Tracked files | Stored bytes |
| --- | ---: | ---: |
| `.gz` | 1,202 | 117,350,549 |
| `.json` | 280 | 38,820,857 |
| `.log` | 118 | 740,706 |
| `.py` | 102 | 1,258,262 |
| `.md` | 76 | 6,834,134 |
| `.xml` | 22 | 345,008,689 |
| `.txt` | 13 | 8,858,121 |
| No suffix, including Makefile/dotfiles | 8 | 4,260 |
| `.xsd` | 5 | 98,921 |
| `.archimate` | 2 | 43,140,075 |
| `.diff` | 1 | 13,456 |
| **Total** | **1,829** | **562,128,030** |

The 1,202 gzip files include **261 ending `.py.gz`** and **490 ending `.gz.gz`**; 41 of the latter have three `.gz` layers. After removing every gzip layer in memory, the complete logical type population is:

| Payload type | Occurrences, including uncompressed originals |
| --- | ---: |
| Python | 637 |
| JSON | 437 |
| Markdown | 313 |
| Logs | 292 |
| XSD | 55 |
| XML | 49 |
| Text | 27 |
| Native Archi models | 10 |
| No suffix | 8 |
| Diff | 1 |
| **Total** | **1,829** |

This explains why searching for `.py` files alone understates code-copy proliferation, while counting Python-looking payloads as independently maintained programs greatly overstates it.

## 3. Authoritative source locations

The audit applies root `AGENTS.md` and `docs/AGENTS.md`. The relevant axioms are **AX-04** (reuse suitable machinery), **AX-14** (separate semantics, representation, projection and presentation), **AX-15** (preserve uncertain execution effects) and **AX-17** (retain unresolved matters for the Solution Architect). These support investigating provenance before choosing a simpler structure; they do not authorise migration.

| Source | Governs or supplies | Standing distinction |
| --- | --- | --- |
| [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md) | Highest architectural design authority | Historical copied axioms/contracts cannot override it. |
| [Round-Trip Engineering Praxis](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md) | Reusable trigger vocabulary, stages, checkpoints, current containment/projection and preservation rules | Current normative Praxis; not the run READMEs. |
| [Architecture Authoring Praxis](../../docs/markdown/governance/architecture-authoring-praxis.md) | One published baseline → authoring iteration → submission → adjudication → next publication | Lifecycle contract, distinct from executable stage helpers. |
| [Change Review contract](../../docs/markdown/governance/archimate-ingestion-change-review.md) and [template](../../docs/markdown/governance/templates/archimate-ingestion-change-review-template.md) | CR decomposition, Disposition/Instruction/Outcome, dependencies, review and execution evidence | The agent prepares the review; only the Solution Architect adjudicates. |
| [Publication semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md), identity/naming, modelling strategy, mapping and navigation contracts | Interpretation, recognition, names, Home/Composition, identity and projections | Their separate concerns remain governed; similarly named snapshot files are historical evidence. |
| [Release policy](../../docs/markdown/governance/architecture-release-policy.md) and root release JSON | Release allocation and bounded machinery-validation exception | The historical RAD-1.0.10 cycle is not a standing instruction to create another run or rewind 1.1.0. |
| RADS `governance/baseline/architecture.json`, `motivation.json`, `business-homes.json`, `home-derived-metadata.json`, `information-architecture.json`, `accepted-ingestion-1.0.8.json`, `accepted-ingestion-1.0.10.json` | Persisted governed resources, identities, accepted amendments and provenance | Machine-readable source can be authoritative governed source. It is not disposable because it is JSON or originally captured by tooling. |
| RADS `baseline/release-1.1.0.json` | Explicit accepted promotion scope and pinned inputs | Release-specific governed decision, not the current release allocator. |
| `baseline/correspondence.json`, documentation classification and source-presentation overlays | Exact source boundaries and reviewed projection/preservation classifications | Configuration/evidence required by current validation, rather than arbitrary cached output. |

The RADS baseline also contains derived Markdown catalogues. Location in RADS establishes the governed documentation context, but **does not make every file there manually authored primary source**. Headers and generators identify projections; source captures and persisted IDs must be distinguished from those projections before any later retention decision.

In the opposite direction, a run's reviewed instructions and `decision.json` retain **execution-specific human authority** when explicitly hash-bound by the accepted capture/promotion. They are not current general governance, but they are more than unapproved generated observations. The 1.1.0 publisher consumes this distinction directly.

The supplied [Class and Package design note](../../docs/markdown/scratchpad/DesignNote-ClassAndPackage.md) explicitly remains candidate direction with deferred mapping/classification. It is not authority to reclassify CR-155–158 or restructure this repository. Promotion explicitly excludes that metamodel work.

## 4. Generated material

The large file population has several causes with separate retention implications:

1. **Multiple full-model publications and transport specimens.** Seven published XMLs occupy about 121 MiB. Architect inputs/native models add about 104 MiB. Full models are also retained as candidates, reconciled outputs, compressed actual exports and before states. These embody different points in a governed lineage; similar model counts do not establish duplication.
2. **Publication history and layered projections.** Recovery, synchronisation, identity establishment, accepted Business changes, Home naming, View selection, initial IA, naming completion and accepted IA promotion each retained their own validation and original artefacts.
3. **Repeated bounded execution and proof.** Initial, failed, corrected and final test/check attempts retain logs, summaries, manifests and diagnoses rather than replacing every prior result.
4. **Complete before archives and source snapshots.** Corrections captured trees that already contained compressed machinery and governance evidence. Compressing those captures again produces `.gz.gz` and `.gz.gz.gz`, repeated path hierarchies and repeated payloads. These are storage/provenance effects, not additional frameworks.
5. **Evidence in two broad homes.** `.junie/reports` holds early diagnostic/proof bundles while later execution evidence lives under ArchiMate processing; some outputs are copied between them.

Precisely identifiable generated populations include:

| Population | Determinable count | Reproduction/retention meaning |
| --- | ---: | --- |
| Finished published XMLs | 7 | Generated representations and historical authoring baselines; retain exact package identity/provenance. |
| Current 1.1.0 publication output set | 9 | `verify_baseline_outputs.py` and the publication manifest define/reproduce nine outputs. Other files in the release evidence directory are additional proof, inputs or snapshots. |
| Framework fixture files | 3 | One XML specimen and two JSON fixture manifests; historical real-input files are referenced elsewhere, not all copied into `tests/fixtures`. |
| Python bytecode in the workspace | 45, all ignored | Interpreter cache; not maintained source or run proof in the inspected manifests. |
| Gzip payload copies | 1,202 | A compression population, **not** a count of safely regenerable/disposable files. Includes unique inputs, human decisions, source captures and proof. |
| Six Run-002 pre-correction archives | 718 | Generated archive files preserving old outputs/source states, not 718 new architectural assertions. |

Candidates, proposed metadata, containment/navigation JSON, comparator results, renderers' Markdown and validation summaries can often be derived from exact pinned inputs and machinery. That is **conditional technical reproducibility**, not a retention decision. A newly run test or Archi invocation cannot recreate the fact that an earlier command passed or failed in its original environment. A generated report with human edits may also cease to be purely derived.

Persisted canonical UUID allocations are particularly significant: ordinary publication replays them; it must not allocate substitutes. The 32 new 1.1.0 IDs already reside in the accepted resource capture. Authored Views, incoming models, reviewed decisions and prior-name/identity evidence likewise cannot be reconstructed safely from current naming rules alone.

## 5. Run-specific evidence

### Machinery Run-001

The 21 files hold baseline naming preview, proposed metadata, rebindings, candidate in plain and gzip form, comparison evidence, verification, governance-transition snapshots and bounded test evidence. The plain `candidate.xml` is byte-identical to the decompressed `candidate.xml.gz`. Run-002 explicitly checks both forms and the preserved Run-001 naming/rebinding result. This duplication has an observable current regression dependency.

Run-001 does **not** contain an independent maintained Python tree. It contains frozen governance bytes and fingerprints of machinery. Some historical machinery versions are available only as versions in later snapshots, not as earlier standalone source commits.

### Machinery Run-002

The 882-file population comprises:

| Component | Files | Purpose |
| --- | ---: | --- |
| Top-level candidate, proposed metadata, rebindings, containment/projection/proof/verification and manifest | 8 | Current frozen machinery result and output pins |
| `governance/` | 10 | Before-document snapshots and registered governance transitions |
| `machinery/` | 88 | Compressed tools, tests, schemas, rules, source/configuration dependencies, including older transition evidence |
| `validation/` | 776 | Replay scripts, dependency snapshots, correction checks, attempts, manifests and six before archives |

The 22,336,985-byte candidate remains the **proposed machinery specimen**, even though a separately reconciled/accepted derivative became RAD-1.1.0. Historical candidate markers do not need to be rewritten into acceptance statements. Promotion persists new identities and changes release/standing metadata through its own governed path.

The following material has its only operative home in execution-specific locations:

| Material | Location / reason it matters |
| --- | --- |
| Exact historical governance-transition before bytes | `run-001/governance/` and `run-002/governance/`, selected by stable `framework/governance-transitions.json` |
| Frozen machinery and correction-era source versions | `run-002/machinery/` and `validation/pre-*/`; needed for exact historical replay, not current policy |
| Run-002 isolated restoration/replay harness | `run-002/validation/reproduce_candidate.py`; no corresponding general framework replay command |
| Particular containment/leaf correction audits | `validation/verify_containment_correction.py` and `contained-leaf-correction/verify_candidate.py` |
| Four import-time replay dependencies | `validation/dependencies/`, selected by `replay-dependencies.json` |
| Human-adjudicated IA review and exact execution plan | `processing/ingestion/1.0.10/run-002/` and `reconciliation/execution-001/` |
| Release-specific actual Archi transport and complete evidence verifier | `processing/publishing/1.1.0/roundtrip/verify-transport.py` and `validation/verify-release.py` |

### Ingestion and publication executions

Do not identify a run by `run-002` alone. There are separate machinery-validation and ingestion namespaces, plus baseline 1.0.8 and 1.0.10 ingestion runs. The ingestion run for 1.0.10 holds the architect's export of the corrected machinery candidate, the 158-CR review and revisions, 154 accepted CRs, four held CRs, issued instructions, execution plan, before snapshots, established source manifest and reconciled model. Its `execution-001/before/` also captures source/governance/test files, explaining another Python-copy population.

For historical reproduction/proof, preserve access to the **transitive evidence closure**: exact baseline/submission/control, review revisions and issued decisions, persisted identities/captures, bindings, plan/source-manifest, machinery/configuration/schemas, historical governance bytes, candidate/reconciled/publication hashes, actual Archi native/export artefacts, commands/toolchain fingerprints, observed results and failures. A summary or hash alone cannot supply missing original bytes; exact reproduction does not independently prove human acceptance.

This closure is a requirement on retrievability and evidence integrity. It does **not** establish that every member must remain an individual file in the active checkout. Historical policy currently requires preserving runs and before evidence; a later authorised retention/package decision would have to respect or explicitly adjudicate that policy. This audit changes none of it.

## 6. Duplication analysis

### Exact bytes and compressed-equivalent payloads

All 1,829 tracked `docs/archimate` files were hashed. Two separate comparisons were made:

| Comparison | Duplicate groups | Files in groups | Occurrences beyond one per group |
| --- | ---: | ---: | ---: |
| Stored bytes exactly identical | 245 | 912 | 667 |
| Payload bytes identical after removing all `.gz` layers | 229 | 1,173 | 944 |

The second count is **not XML canonicalisation or semantic comparison**. It only decompresses; different XML bytes remain different. Its lower group count reflects formerly separate compressed/uncompressed groups merging. These populations overlap and must not be added.

For scale only, summing each group's stored lengths minus its smallest stored member gives **17.69 MiB** for exact-byte duplication and **168.02 MiB** for decompressed-payload duplication. These are mathematical population measures, not storage-saving commitments or proposed deletions. The latter would sometimes retain a compressed specimen instead of a required plain publication; current path consumers and proof semantics prohibit that shortcut without a design decision.

Examples established by hashes:

| Duplicate population | Provenance and implication |
| --- | --- |
| Run-001 plain/gzip candidate | Same payload, two import/replay representations; Run-002 currently requires both. |
| Run-001 / Run-002 `proposed-metadata.json` and rebindings | Deliberately invariant naming/identity evidence across projection work. |
| Reconciliation `artifacts/workspace-projection.json` and 1.1.0 publication copy | Byte-identical proof carried forward across execution/publication boundaries. The same applies to their containment and folder-generation proof. |
| Ingestion 1.0.10 `change-review.md` and `execution-001/change-review-revision-003.md` | Current review and preserved execution-bound revision are identical at this commit, while earlier revisions remain different records. |
| RADS `baseline/architecture.json` and publishing 1.0.7 `identity-establishment-1.0.7/architecture.json` | Byte-identical 7,260,844-byte resource captures. One is live governed source; the other is source-establishment history. |
| That architecture capture through run dependency/machinery/before archives | Nine payload copies inside ArchiMate; ten including its current RADS source. Same bytes retained under different provenance paths/compression layers. |
| `.junie` third-blind-analysis `baseline-comparison.json.gz` and ingestion 1.0.8 Run-001 `detection/baseline-diagnostic.json.gz` | Same diagnostic copied across report/processing homes. |
| Architect inputs and candidates under ingestion `execution-001/before/` | Compressed copies of existing published/input/candidate bytes, alongside new states that are not duplicates. |
| Migration `reference-consumers-before/` | Preserved earlier READMEs, three Python source files and location-map consumers. Similar names do not make them current code. |

`machinery`-named path components occur in 540 tracked files, and `before` components in 177. These are **overlapping path observations**, because before archives contain machinery copies; they are not additional disjoint totals.

A duplicate is evidence of byte reuse, not proof of redundant purpose. Conversely, different hashes do not prove separate architectural meaning: 1.0.7 and 1.0.8 publication XMLs have equal lengths but differ in release metadata, and correction-era candidates can differ only in navigation. No fuzzy duplicate estimate is substituted for these distinctions.

## 7. Python/tooling analysis

### Population and maintenance status

| Location | Plain `.py` | All Python payload occurrences after gzip removal | Distinct Python payloads within row | Classification |
| --- | ---: | ---: | ---: | --- |
| `framework/tools/` including reconciliation | 40 | 40 | 40 | Maintained framework source, including bounded historical adapters |
| `framework/tests/` | 24 | 24 | 24 | Maintained tests; synthetic fixtures are often inline Python builders |
| `governance-tooling/` | 4 | 4 | 4 | Three frozen source copies; one migration-specific replay script |
| Ingestion 1.0.8 | 8 | 8 | 8 | Run-specific detection, curated review preparation and verification |
| Ingestion 1.0.10 | 4 | 47 | 44 | Four execution audit scripts plus 43 compressed source copies |
| Machinery Run-001 | 0 | 0 | 0 | Fingerprints/governance evidence, no Python files |
| Machinery Run-002 | 3 | 456 | 81 | Three execution verifiers/replay scripts plus archived source/tests |
| Publishing 1.0.6 | 2 | 2 | 2 | Inspection/snapshot audits |
| Publishing 1.0.8 | 5 | 5 | 5 | Migration/preservation/reproduction scripts |
| Publishing 1.0.9 | 6 | 45 | 43 | Six bounded check/transport scripts plus 39 source snapshots |
| Publishing 1.0.10 | 4 | 4 | 4 | Initial IA/completion/transport verification |
| Publishing 1.1.0 | 2 | 2 | 2 | Release-specific transport and integrity verification |
| **ArchiMate total** | **102** | **637** | **151 across the whole population** | Row-wise unique counts overlap |
| `.junie/reports/` outside that total | 4 | 4 | 4 | Identity-export probe and 2A/2B/2C documentation audits |

Of the 637 Python occurrences, **535 are compressed snapshots**, representing **105 distinct byte contents**. **383 snapshot occurrences** match one of **50 current framework Python files** exactly after decompression. The other **152 occurrences / 55 contents** are older or other source versions. They are not all generated Python programs: the *copy/archive operation* is generated, while the preserved bodies are authored implementation or test source. No basis was found for treating these snapshots as independently maintained forks.

Outside the framework, the 38 plain ArchiMate Python files comprise **three frozen pre-migration source copies and 35 execution/migration-specific scripts**. Some remain operational dependencies, so “execution-specific” does not imply safe to delete. The four `.junie` Python files are additional historical probes, not a second maintained round-trip framework.

### Framework responsibilities

| Maintained source | Role and scope |
| --- | --- |
| `roundtrip_praxis.py` | `discover`, `observe`, `inspect-review`; produces intake/applicability evidence, not an automatically adjudicated review |
| `diagnose_roundtrip.py`, `element_correlation.py`, `change_review.py` | Secure offline Exchange parsing/validation, canonical continuity, raw transport and architectural observations |
| `relationship_state.py`, `containment.py`, `home_relationships.py` | Shared relationship standing and type-selected Home/Composition resolution |
| `governed_metadata.py`, `information_naming.py`, `home_context.py` | Persisted identity/name validation and governed naming/context |
| `workspace_policy.py`, `workspace_navigation.py`, `feature_projection.py`, `view_presentation.py` | Collection, containment and presentation projections; historical View-generation code does not authorise replacing submitted Views |
| `information_naming_run.py`, `information_workspace_run.py` | RAD-1.0.10 specimen adapters; the latter explicitly fixes `RUN = 'run-002'` |
| `reconciliation/information.py` | Bounded agent-reviewed accepted information adapter; explicit capture, replay and check modes |
| `publish_information_release.py` | Explicit RAD-1.1.0 promotion/publication adapter; not a release-independent reconciler |
| `run_evidence.py`, `governance_history.py`, `workspace_paths.py` | Guarded output correction, registered historical transitions and exact workspace mapping |
| `synchronise_baseline.py`, `reconcile_108.py`, `information_population.py`, `recover_v02.py`, publishers/reporters/verifiers | Current dispatch plus scoped older publication/recovery paths and their evidence-backed rendering |

The framework is small in byte terms: **1.56 MiB** including its tests/schemas/locks. Requirements are Python 3.9+ as documented and `lxml>=4.9,<7` in `requirements.txt`. The version range is not a complete environment lock. Vendored schemas have their own provenance and licence. Archi transport records identify installed Archi 5.10 and plugin hashes, while offline schema provenance records the earlier bundled schema source; these are different provenance roles.

Root `scripts/architecture_release.py`, `architecture_identity.py` and `rads_business_architecture.py`, with `scripts/tests/`, support release/identity/RADS checks. The identity helper imports framework code. Historical v0.2 LaTeX capture/render/navigation/reconciliation tools supply pinned recovery inputs; they are not current authoring authority. The Maven architecture suite checks broader repository invariants and appears in retained workflow validation evidence; it is not the Python round-trip entry point. No `.github/` workflow directory exists in this checkout, and no separate ArchiMate launcher was identified in the inspected IDEA configuration.

### Execution-only Python that requires historical discovery

The Run-002 isolated replay restores compressed source/configuration into a temporary tree, copies hash-pinned specimens and compares candidate/naming/containment/navigation bytes. Its implementation exists only under `run-002/validation/`. Its provenance-specific work is legitimate, but discovering this harness is necessary to perform that historical proof: **a structural dependency on a historical execution path**.

Similarly, complete 1.1.0 evidence verification and actual transport assertions are implemented only beneath that release's `roundtrip/` and `validation/`, with closely related verifiers repeated for previous releases. Their fixed hashes, counts and residual assertions are execution evidence. Any generally reusable verification mechanics embedded there are not exposed through one durable general framework interface. This is a structural gap for a fresh agent asked to perform the *complete* governed verification, although it does not prevent initial intake.

The old `prepare-review.py` under ingestion 1.0.8 explicitly describes a manually curated, run-specific decomposition. It is not a hidden universal `change-review.md` generator that future sessions should copy. Current Praxis deliberately assigns independent CR decomposition to the processing agent.

## 8. Praxis/governance discoverability

### Stable discovery succeeds

Root `AGENTS.md` has an exact **“ArchiMate round-trip task entry”** for both short triggers. It links directly to current RADS Praxis and forbids selecting ambiguous inputs by timestamp. `docs/AGENTS.md` adds no competing instruction set. The RADS governance index, authoring Praxis and ArchiMate README also link current round-trip governance. The framework README has a dedicated **“Governed round-trip engineering entry”** section with commands and component responsibilities. These are substantive existing bootstrap assets.

The current semantic rules are in RADS and shared code. The audit found **no requirement to treat Run-001/Run-002 copied policy as current authority** to understand containment, naming, CR checkpoints or View preservation. The run archives are frozen governing-history evidence. Distinguishing these roles avoids inventing an authority defect where the repository already supplies a stable contract.

### Friction and conflicting status descriptions

| Observation | Effect on a fresh session |
| --- | --- |
| `framework/README.md` is 27,630 bytes, mixing current commands with release histories and superseded sections | General intake commands appear around line 256, after earlier publication and frozen-run examples. The reader must establish applicability before choosing a command. |
| `processing/README.md`, ingestion/ingest READMEs and workspace prose still describe reconciliation as future/unimplemented | A general unattended engine indeed remains absent, but bounded accepted-information reconciliation and 1.1.0 publication now exist. The unqualified descriptions obscure that capability boundary. |
| Workspace diagram omits `processing/machinery-validation/` | The largest file population lacks a top-level map entry even though the framework later describes it. |
| `governance-tooling/README.md` still says root release is RAD-1.0.9; RADS baseline README still identifies 1.0.8 as current publication | These are live navigation/status statements with stale values, not current release authority. Root JSON and current package index disagree visibly. |
| “Current Run-002” instructions still say RAD remains 1.0.10/1.0.9 and advertise live `--check` commands | The unchanged-release guards make those commands inapplicable at root 1.1.0; a reader must discover the historical context separately. |
| Promotion authority/report/transport JSON retain pending visual-inspection wording | The user confirms later inspection; persisted status does not capture that completion. This is a discoverability/provenance gap, not grounds for this audit to rewrite the acceptance record. |
| Praxis names machinery modules largely as code names; it does not provide a single linked execution handbook | The reader follows authoring Praxis → workspace → framework, or searches by filename, to discover concrete paths and flags. The chain exists but requires several sources. |

File-target checks of Markdown links in root instructions, both principal ArchiMate READMEs, processing/ingestion/publish indexes and current RADS governance/Praxis found **no missing file targets**. This was a bounded existence check, not exhaustive anchor validation or verification of links inside every frozen report. The chief problem is status/applicability and execution coupling, rather than a missing root Praxis hyperlink.

## 9. Fresh Codex bootstrap trace

This trace models a new session receiving only **“Model Updated. Please review and generate change-review.md.”**, with this checkout and repository instructions. It does not assume memory of any earlier run.

| Step | Repository discovery | Required conclusion/action |
| --- | --- | --- |
| 1. Establish instructions | Root `AGENTS.md`; `docs/AGENTS.md` when entering documentation | Load axiom authority, RADS/legacy distinction, bounded-command rules and round-trip trigger entry. `.junie` is navigation/evidence, not authority. |
| 2. Load current Praxis | Direct root link to `docs/markdown/governance/archimate-round-trip-engineering-praxis.md` | First trigger stops at review. No acceptance, reconciliation, release increment or candidate regeneration is implied. |
| 3. Load applicable contracts | Praxis links to authoring lifecycle, CR contract/template, identity/naming, publication semantics and release policy; progressively follow modelling/navigation references | Distinguish authored semantics, transport differences, derived metadata, navigation and authored Views. Preserve uncertainty. |
| 4. Find operating code | Authoring Praxis links the ArchiMate workbench entry; its README links framework. Alternatively `rg --files` locates the named `roundtrip_praxis.py` | Discover `framework/tools/`, Python/lxml requirements, schemas and intake commands. No historical machinery restoration is needed for this step. |
| 5. Establish applicable baseline | Root release JSON plus `exchange/publish/README.md` and 1.1.0 provenance | Applicable current package is `exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml`, not the 1.0.10 frozen specimen. A version alone does not prove originating authoring provenance. |
| 6. Locate a new deliberate submission | `exchange/ingest/`, any explicit locator/manifests and original authoring baseline | At this boundary there is **no `exchange/ingest/1.1.0/` submission**. Existing 1.0.8/1.0.10 inputs are historical. The short request alone cannot turn one into a new submission. |
| 7. Pin and observe, once applicable input exists | `roundtrip_praxis.py discover/observe`, optional explicit control, diagnostic/correlation/containment tools | Discover checks unique XML/XML.gz in the current-version directory; explicit baseline/incoming override is available. Missing/competing provenance remains UNRESOLVED. No newest-file selection. |
| 8. Find transport control | Exact originating publication's retained no-edit evidence, if available | For 1.1.0, retained control is `processing/publishing/1.1.0/roundtrip/Archi-no-edit-export.xml.gz`. `discover` does not choose it automatically. No control means transport origin remains unestablished. |
| 9. Prepare checkpoint | CR template, intake evidence and ingestion run-layout convention | Allocate the process identity under the actual baseline, pin inputs, decompose independently adjudicable CRs and write `change-review.md` with On-Hold/Unresolved and Not authorised defaults. Stop for human review. |
| 10. Later directive trigger only | Reviewed document, preserved revision, exact hashes, dependencies and explicit instructions | `inspect-review` checks applicability, not prose acceptance. Apply only governed scope through supported adapters; unresolved scope/gates stop dependent work. |

The missing next submission at step 6 is **an input-state limit, not itself a structural defect**. This audit did not create one or generate a review. The helper's discoverability limitation is narrower: it searches the root current-release directory and does not itself interpret a submission register or older-baseline lineage. If a legitimate explicit submission has another originating baseline, the agent must establish that provenance and pass locators; this is a workflow limitation, not permission to choose a historical specimen by guesswork.

The agent can reach the review checkpoint from stable instructions and shared code. For later publication, comprehensive verification or historical replay, it currently has to discover the following historical dependencies:

| Dependency | Nature / structural assessment |
| --- | --- |
| Current 1.1.0 publisher → RADS promotion decision → historical candidate, review, plan, instructions, accepted source and establishment manifest | Exact release provenance is legitimate. Current rendering relies on a processing tree as part of its effective source graph, so the conceptual reusable/evidence boundary is not self-contained. **Structural coupling.** |
| `governance-transitions.json` → Run-001/Run-002 governance manifests/snapshots | Correctly preserves historical authority bytes; live source validation can traverse those run paths through `information_population.preserved_authority`. **Historical evidence is an executable dependency.** |
| Current regression suite → specimen manifest, actual exports, Run-001 outputs and Run-002 before archives | Legitimate real-input regression specimens live largely outside the fixture area. **Regression/retention coupling.** |
| Complete isolated candidate replay → `run-002/validation/reproduce_candidate.py` | Required replay mechanism is execution-local rather than a stable framework command. **Historical-code discovery defect.** |
| Complete release/transport proof → 1.1.0-specific Python verifiers and local ignored files | Required verification knowledge/code and some bytes are outside the stable maintained interface or Git recovery boundary. **Reproducibility defect.** |

These dependencies do not justify importing frozen Python/policy into the current source tree during this audit. They establish design inputs for a later durable structure.

## 10. Git-history versus retained-evidence analysis

### What Git already records

Git holds accepted source/governance versions, current tools/tests, published XMLs, review/decision artefacts and snapshots that were deliberately added. `git log` shows the current round-trip Praxis, `roundtrip_praxis.py`, `containment.py`, Run-002 pre-containment archive and replay harness first entering Git in **the same recovery commit**. Earlier committed releases remain in history; many correction-era working states did not exist as separately committed versions of the original source paths.

This distinction matters: Git history is not automatically a substitute for an uncommitted intermediate source state. Equally, retaining another copy of bytes already recoverable from a pinned commit is a separate evidence/storage choice, not an architectural necessity established merely by the copy's existence.

For each of the **1,202 tracked gzip files** in `docs/archimate`, the fully decompressed bytes were compared with current raw files in `docs/archimate`, `.junie`, `scripts`, RADS governance, root instructions/release, and with Git blob IDs reachable through `git rev-list --objects --all`:

| Decompressed payload relationship | Occurrences |
| --- | ---: |
| Matches a current raw file in that inspected scope | 695 |
| No such current match; matches a reachable historical standalone Git object | 111 |
| No current match or reachable standalone Git object found | 396 |

These are occurrences, not unique contents or deletion candidates. “No standalone object” does not mean untracked: the compressed archive itself is committed, and may be the only committed carrier of that intermediate state. Reachability covers locally available refs/history, not unreachable objects, external archives or a server audit. Some matches are small/empty output rather than meaningful duplicated source.

`historical-locations.json` preserves exact old-to-current locators, and `governance-transitions.json` preserves specifically registered before/after authority hashes. Both have operational consumers. Removing copied bytes while leaving their pinned hash/path consumers unchanged would break proof even where Git could retrieve the same content.

### The 1,714-file preservation claim

The publication's `protected-inputs.json` records **1,716 paths**. A read-only rehash in this audit found **1,714 unchanged** and exactly the two recorded textual amendments: the published-package index and RADS `baseline/information-architecture.md`. Thus 1,714 is a preservation-audit result, **not the number of current ArchiMate files or tracked files**.

Crucially, **17 of the 1,716 protected paths are not tracked**: one `.archimate.bak` and 16 raw logs. The preservation claim is about the local filesystem observed during promotion; it is broader than the committed recovery boundary.

### Evidence omitted from the committed recovery boundary

The committed `processing/publishing/1.1.0/release-verification-manifest.json` fingerprints 98 paths, of which **19 are ignored, untracked local logs**. Its `validation/test-results.json` references ten test/check logs; all ten are local, untracked, and currently match the recorded SHA-256 values. `roundtrip/execution.json` similarly hashes `roundtrip/archi-cli.log`; it exists locally and matches, but is not tracked. Neither adjacent `.log.gz` copies nor history for that Archi log path were found.

The missing-path union of protected-inputs and release-verification manifests is **36 paths: 35 logs plus the backup**. Sixteen have identical payload bytes somewhere else in current tracked ArchiMate/Junie material, sometimes an archived copy or the JSON that a log echoed. Twenty have no matching payload in that current scope. A separate reachable-Git-object check finds an additional historical raw-content match for `validation/release-check.log`; the other absence findings are not repaired by the inspected local history. No claim is made about backups outside the repository.

This is not merely missing optional logs:

* `roundtrip/verify-transport.py` directly reads `archi-cli.log` to validate its execution hash.
* `validation/verify-release.py` directly reads every protected path and the ten `test-results.json` log paths.
* Root `.gitignore` ignores `*.log` and `*.bak` (lines 48 and 76). Earlier evidence includes many explicitly tracked logs or compressed logs, while these recent files were not included.

Accordingly, **a clean checkout of the recovery commit cannot perform those verifications as written**. This conclusion follows from direct path reads and the index inventory; the audit did not run the verifiers, regenerate logs, restore files or alter ignore rules. The retained PASS summaries remain historical assertions supported by matching files in this workspace; missing committed evidence does not establish a semantic architecture failure. It establishes an incomplete repository-contained proof/recovery closure.

### Historical replay is also release-context-sensitive

The documented live Run-002 `--check` requires root release 1.0.10/1.0.9. The isolated `reproduce_candidate.py` validates `manifest.frozen_files_before`, which includes the old root release-file hash. At current 1.1.0 that one frozen file differs. The reconciliation adapter's documented historical `--check` likewise compares root release with its old plan binding and stops.

The 1.1.0 `validation/development-notes.md` explicitly explains that frozen replay ran **before** advancing root release authority. Current publication uses a separate adapter that validates the historical plan and the new promotion decision without rewriting either. This is sound lineage preservation, but the advertised historical check commands are not directly runnable in the current root state. A fresh session has to discover the context-sensitive replay method; it must never rewind the authoritative root file to make an old command pass.

## 11. Structural findings

| ID | Finding | Evidence / practical consequence |
| --- | --- | --- |
| SF-01 | **Committed recovery/proof closure is incomplete.** | Thirty-six hash-referenced paths are ignored/untracked; current transport/release verifiers read some directly. Local successful verification does not imply verification from the pushed baseline. |
| SF-02 | **Stable root governance discovery works, but the operating picture is inconsistent.** | Exact trigger link and current Praxis exist; stale current-release values, “future/unimplemented” wording and mixed historical/current examples obscure actual bounded capabilities. |
| SF-03 | **Required replay/verification code lives only in historical execution paths.** | Run-002 restoration harness and release-specific transport/integrity verifiers have no general maintained command interface. A fresh complete-proof task requires archaeology beyond the stable intake path. |
| SF-04 | **Historical check instructions are not applicable to current release state.** | Guards and frozen root-release hashes still require 1.0.10; documented check commands do not describe a self-contained replay context at 1.1.0. This is not a reason to weaken the guards. |
| SF-05 | **Current publication's effective input graph crosses source/evidence boundaries.** | `publish_information_release.inputs()` follows the accepted RADS promotion decision into Run-002 candidate, review, plan, decisions, export and establishment artefacts. This is stronger than a documentary historical link. |
| SF-06 | **Governance-history validation depends on run-local frozen bytes.** | Stable transition register selects Run-001/Run-002 manifests. Authority remains in RADS, but the validation closure is execution-local and must be retained/discoverable. |
| SF-07 | **Archive growth compounds existing archive trees.** | Six pre-correction archives contain 718 files; 490 paths end `.gz.gz`, including 41 with three layers. Whole-state capture preserves context but repeatedly captures machinery and older evidence. |
| SF-08 | **Regression specimens and historical evidence are coupled.** | Maintained tests directly read run outputs/before archives and real controls outside three nominal fixture files. Moving “history” without mapping that graph would break tests. |
| SF-09 | **File and directory names do not consistently identify maintenance/authority roles.** | Python source appears live, frozen, compressed and execution-specific; `baseline` and `governance` names serve different roles; numeric run/release-like identifiers occupy several namespaces. Full locators and standing are essential. |
| SF-10 | **Lifecycle completion is not fully reflected in durable status records.** | User confirms final inspection; promotion decision/report/transport record retain pending handoff. Future sessions cannot infer a new acceptance from generated PASS results or silently resolve the stale record. |
| SF-11 | **Bootstrap requires distributed knowledge, although not historical policy reconstruction.** | Governance, lifecycle, commands, input/run layout, release provenance and environment are spread among root instructions, RADS and several READMEs. The current intake path is viable; complete execution remains less discoverable. |

The high file count has an evidenced explanation: full-model lineage, layered migration/publication work, immutable inputs, human review revisions, bounded verification attempts and repeated preservation archives. It is **not** evidence of 1,829 independent sources, 637 maintained Python modules, or seven competing architecture authorities. The primary structural concerns are role/applicability clarity and dependency/recovery closure.

Uncertainties and limits remain explicit:

* This is a complete file/hash inventory for `docs/archimate` and `.junie`, with targeted transitive inspection elsewhere. It is not a fresh execution of every historical command, an Archi invocation or a test-suite rerun.
* All cited test PASS counts are retained promotion evidence, not tests executed by this audit. No Maven/Python suite was run: this analysis creates only this report; generation and new machinery-validation runs are outside the authorised scope. Verification here consists of inventories, hashes, Git provenance and static dependency inspection.
* Exact byte duplication is established; semantic equivalence of every XML/JSON/history pair is not asserted.
* Complete historical environment reproduction is not proved by tool hashes or a broad lxml range. Installed binary paths, display access and original temporary workspaces are machine-specific; records describe them rather than bundling a portable runtime.
* No retention schedule, approved evidence-package format, general new-scope adapter or comprehensive disposal authority was established. A generated label alone cannot settle retention.

## 12. Candidate cleanup principles

These are design principles and options for Solution Architect/ChatGPT consideration. They prescribe no final directory tree, create no repository convention, and authorise no cleanup.

1. **Design around authority and purpose first.** Keep current governed contracts distinguishable from machine-readable accepted source, executable code/configuration, fixtures, generated publications and immutable execution evidence. Preserve source standing even where bytes coincide with a snapshot.
2. **Make the short trigger sufficient for discovery.** The existing root → Praxis link is an asset. A durable operating description could identify supported stages, their commands, required inputs, current capability limits and stop gates without requiring readers to interpret a release-development narrative.
3. **Define and verify the evidence closure before changing storage.** Enumerate exact inputs, decisions, allocated identities, machinery/environment, outputs and proof dependencies. A clean-checkout/retrieval check would distinguish repository-backed proof from local ignored state. Hashes verify retrieved bytes; they cannot replace absent bytes.
4. **Separate generally reusable mechanics from execution assertions.** Options include a stable replay/evidence-validation interface with run-owned manifests, while fixed populations, residuals, human decisions and historical contracts remain immutable execution data. Extraction would require separate review; copying an old script into the framework is not automatically correct.
5. **Use Git deliberately, with pinned recovery identities.** A commit/blob locator may replace some repeated source copies where exact bytes and path/hash translation are durably recoverable. Intermediate states that were never standalone commits need another carrier. Git history cannot be assumed to contain them merely because the final work was committed.
6. **Consider packaging and deduplication as evidence-preserving options.** Content-addressed payload storage, immutable archives, execution bundles or checkpoint/delta approaches could preserve historical proof without thousands of checkout entries. Any option must preserve exact originals, manifests, validation semantics, access and current consumers; no saving estimate here establishes an approved migration.
7. **Retain observed historical facts separately from reproducible outputs.** Candidate/projection/comparator outputs might be regenerated from exact locked inputs; observed command results, failed attempts, actual Archi exports and human decisions carry historical facts. Decide what exact outputs remain part of proof before substituting regeneration.
8. **Make fixtures explicit without destroying their provenance.** A future design could declare which real historical artefacts are regression fixtures and which only support archival proof. Either role may reference one preserved payload, provided independent purpose and provenance remain visible.
9. **Control archive amplification through a defined capture boundary.** Future evidence design could avoid recursively copying older archive payloads while still pinning their immutable identities. A full archive, reusable referenced payloads and incremental checkpoints are alternatives to assess, not instructions to delete current before states.
10. **Keep historical instructions historical and current applicability clear.** Resolve stale navigation/status descriptions in separately authorised work; preserve frozen original evidence and the supersession rules. Avoid weakening release/hash guards to make old examples run against a later baseline.
11. **Treat disposal as a later governed decision.** Ignored bytecode is a plausible cache category; ignored logs or backups referenced by proof are not equivalent. Reproduction capability, historical value, required retrievability and evidence policy should determine retention, not extension or file-count targets.

Only this audit report was created. RAD-1.1.0, the Praxis, `.gitignore`, source, tests, publications, run trees and retained evidence were not changed, and no new machinery-validation run was created.
