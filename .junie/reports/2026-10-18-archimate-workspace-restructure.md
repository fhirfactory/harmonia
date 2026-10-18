<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate workspace restructure — Architecture Release 1.0.8

Date: 2026-10-18. Status: **COMPLETE — validation PASS; ready for Solution Architect acceptance**.
Prepared for Solution Architect acceptance. No repository commit or new real-Archi
run is claimed. Increment 2 has not begun.

## Authority and release treatment

The immutable accepted starting checkpoint is
`107cf351f0a784d8d43a9942e0443390ea98e74e`, Architecture Release **1.0.7**
(previous 1.0.6). The Solution Architect superseded the original no-increment
request: the release policy remains authoritative, with no task exception.
The normal patch mechanism prepared **1.0.8**, previous **1.0.7**. Root
[architecture-release.json](../../architecture-release.json) remains the single
current-release authority; generation never increments it. This is one
repository/workspace structure and tooling change set, requiring acceptance and
commit together under the existing release policy.

The final responsibility adjudication supersedes both the earlier three-boundary
interpretation and the intermediate relocation of synchronisation material under
publish. The subsequent direction clarification splits processing into
**publishing** and **ingestion**. It does not authorise an ingestion workflow.

Relevant authority was loaded from AGENTS.md, docs/AGENTS.md, the release policy,
Architectural Axioms and governed publication/identity inputs. AX-04 and AX-06
separate machinery from semantics and authority; AX-08 preserves evidence meaning;
AX-14 preserves semantic distinctions; AX-15 and AX-17 retain uncertainty and
architectural decision boundaries. No architecture change is inferred from moving
files. The policy conflict was explicitly adjudicated, not silently waived.
The [earlier unaccepted preflight narrative](../../docs/archimate/processing/publishing/1.0.8/preflight-working-report.md)
remains a working-history attachment; its stopped status is superseded here.

## Starting inventory and classification before movement

The initial checkpoint was clean. The full physical inventory contained **203
files: 183 tracked files and 20 ignored Python bytecode caches**. All tracked
ArchiMate workspace files lived under `docs/archimate/capability-architecture/`.

| Original location | Tracked files |
| --- | ---: |
| Root files | 14 |
| baseline/ | 3 |
| recovery-v0.2/ | 26 |
| schemas/ | 7 |
| synchronised-baseline/ | 109 |
| tests/ | 10 |
| tools/ | 14 |

The [original movement plan](../../docs/archimate/processing/publishing/1.0.8/movement-plan.json) records every physical file,
original path, proposed destination, responsibility, size, hash and tracked flag.
[Original reference inventory](../../docs/archimate/processing/publishing/1.0.8/reference-inventory.txt) records the pre-move
search; [search diagnostics](../../docs/archimate/processing/publishing/1.0.8/reference-search-diagnostics.txt) record its scope.
The reviewed path assumptions covered Python, Makefile, Markdown, JSON manifests,
release fixtures, generated reports, schemas, recovery and round-trip tools.
No live Java, shell, Maven or CI consumer of the old workspace path required an
edit. Schema includes remain schema-relative. Historical command logs are evidence,
not live build configuration.

Classification was recorded before moves, including corrections at each
adjudication. See the [intermediate classification](../../docs/archimate/processing/publishing/1.0.8/revised-movement-plan.json),
[processing-model plan](../../docs/archimate/processing/publishing/1.0.8/final-movement-plan.json) and
[direction-split plan](../../docs/archimate/processing/publishing/1.0.8/processing-direction-movement-plan.json).
The [complete final source → destination map](../../docs/archimate/processing/publishing/1.0.8/accepted-source-destination-map.json)
resolves all 183 tracked originals to their actual current locations and records
before/after hashes. Earlier plans retain their original proposed paths as history.
Ignored bytecode was incidental runtime state, outside governed evidence.

| Responsibility | Classification and treatment |
| --- | --- |
| FRAMEWORK | Reusable tools, schemas, tests, projection configuration and test/recovery baselines |
| PUBLISHED EXCHANGE ARTEFACT | Finished versioned Open Exchange XML |
| INGESTED EXCHANGE ARTEFACT | No live incoming reconciliation input existed in the old workspace; historical exports remain in their frozen proof bundles |
| HISTORICAL / FROZEN EVIDENCE | Original release-specific reports, archives, manifests, logs and preservation/verification evidence; move without rewriting |
| DOCUMENTATION | New current workspace/boundary/command instructions, plus operational RADS navigation edits |
| UNCERTAIN | No unresolved destination decision; unversioned historical support reports remain with framework baseline support rather than assigning an invented release |

## Source → destination allocation

Paths below are relative to `docs/archimate/`; individual files are enumerated in
the final mapping attachment.

| Original material under capability-architecture/ | Final destination | Rationale |
| --- | --- | --- |
| tools/, tests/, schemas/, baseline XML and semantic/navigation locks, Makefile, requirements, documentation-map | framework/ | Reusable machinery and support |
| README.md | framework/historical-workspace-readme.md | Preserve mixed historical narrative unchanged; new current README supplied |
| roundtrip-increment-1.md | framework/roundtrip-increment-1.md | Frozen Increment 1 procedure |
| view-generation-report.md, reference-id-reconciliation-report.md | framework/baseline/ | Unversioned historical framework support; no release inferred |
| documentation-publication-report.md, release-verification.json | processing/publishing/1.0.1/ | Original documentation-publication evidence |
| Harmonia-Capability-Architecture.xml | exchange/publish/1.0.4/ | Finished navigation publication |
| navigation report/verification, validation.json | processing/publishing/1.0.4/ | Release-specific navigation processing evidence |
| recovery-v0.2/ contents | processing/publishing/1.0.5/ | Historical 1.0.5A recovery working state, replay inputs/output and verification; recovery specimen is retained with its evidence |
| synchronised-baseline/Harmonia-Architecture-Baseline.xml | exchange/publish/1.0.6/ | Finished 1.0.6 publication |
| synchronised-baseline synchronisation reports, preservation proofs, validation, verification and logs | processing/publishing/1.0.6/ | Original 1.0.6 processing evidence |
| synchronised-baseline/Harmonia-Architecture-1.0.7.xml | exchange/publish/1.0.7/ | Finished accepted identity-bearing publication |
| synchronised-baseline canonical-identity establishment report/guide/validation/verification and identity-establishment-1.0.7/ | processing/publishing/1.0.7/ | Original establishment/source-history evidence, including prior-release archive |
| New 1.0.8 XML | exchange/publish/1.0.8/ | Finished structure/tooling-release exchange output |
| New restructure reports, validation, comparisons, command logs, inventories, verification manifest and inspection guide | processing/publishing/1.0.8/ | This release's publication processing evidence |
| Future incoming exports | exchange/ingest/ | Landing boundary only |
| Future incoming analysis working state/evidence | processing/ingestion/ | Separate direction; README only, no workflow implemented |

This specifically classifies former synchronised-baseline contents by original
release and responsibility. They were not moved wholesale into a 1.0.8 container.
No `exchange/publish/synchronised-baseline/` container remains.

## Final directory tree and boundaries

```text
docs/archimate/
├── README.md
├── framework/
│   ├── baseline/
│   ├── schemas/
│   ├── tests/
│   ├── tools/
│   ├── historical-locations.json
│   ├── documentation-map.json
│   ├── Makefile
│   └── README.md, requirements.txt, preserved historical instructions
├── processing/
│   ├── README.md
│   ├── publishing/
│   │   ├── 1.0.1/
│   │   ├── 1.0.4/
│   │   ├── 1.0.5/
│   │   ├── 1.0.6/
│   │   ├── 1.0.7/
│   │   └── 1.0.8/
│   └── ingestion/
│       └── README.md
└── exchange/
    ├── publish/
    │   ├── README.md
    │   ├── 1.0.4/Harmonia-Capability-Architecture.xml
    │   ├── 1.0.6/Harmonia-Architecture-Baseline.xml
    │   ├── 1.0.7/Harmonia-Architecture-1.0.7.xml
    │   └── 1.0.8/Harmonia-Architecture-1.0.8.xml
    └── ingest/
        └── README.md
```

The [complete final tree](../../docs/archimate/processing/publishing/1.0.8/final-directory-tree.txt) includes individual support
and evidence files. Publish packages contain XML only; no report, archive or
validation workspace is published as an exchange package.

The root [workspace README](../../docs/archimate/README.md) explains the four
responsibilities and both processing directions directly. Publication flows from
Harmonia RAD through framework/publishing processing to finished exchange output.
Future return input lands in exchange/ingest; its working state belongs in
processing/ingestion. Comparison/reconciliation remains Increment 2.

Canonical identity and names remain resource-owned metadata in the governed RAD.
Framework, processing and exchange files establish no separate architecture
source of truth. **Archi export is architecture change input, not replacement
architecture repository.** It may carry semantic changes, presentation changes
and known transport transformations; Solution Architect acceptance of reconciled
changes must precede entry into the RAD.

## Tooling, operational references and test changes

- Framework-relative `workspace_paths.py` defines framework, publish, ingest and
  publishing/ingestion processing locations. The old umbrella directory has no
  compatibility wrapper, symlink or duplicate operational tree.
- Current publication selects versioned exchange XML and release-specific
  publishing validation/report/guide paths. Ten governed Markdown/JSON projections
  retain their existing RADS destinations.
- Recovery uses processing/publishing/1.0.5 and its original release fixture.
  Identity source-history resolution uses the exact historical-location map and
  keeps the reallocation/refallback guards effective at the relocated archive.
- `scripts/architecture_identity.py` and its tests now locate framework inputs
  and the versioned historical 1.0.4 publication. Frozen manifest keys and hashes
  resolve through the explicit map; no architecture matching was added.
- Bare incoming filenames select exchange/ingest; explicit paths remain explicit.
  A test invokes the existing diagnostic from an unrelated working directory and
  confirms the awaiting-input result names the ingest path. Existing diagnostic
  write restrictions remain; processing/ingestion is reserved without implementing
  a new workflow.
- Portable publisher fixtures assert the relocated output paths and retain exact
  XML, identity, endpoint, reference and overwrite-protection assertions. Three
  focused workspace tests protect boundaries, historical lookup, incoming-path
  resolution and rejection of semantic/identity/relationship/presentation mutations.
- The Makefile's bounded full-suite allowance changed from 120 to 180 seconds
  because the observed suite takes 129–137 seconds. Assertions and fidelity
  classifications were not weakened. No comparison normalisation was added.
- The 1.0.8 support-report renderer validates completed, hash-pinned checks and
  exact migration proof. The complete independent reproduction remains 14 files.

Current documentation updated: workspace/framework/publish/ingest READMEs;
processing and ingestion-processing READMEs; governance index; baseline README;
Archi navigation convention and mapping catalogue links. The only current-authority
hash refreshes are the three actual navigation documents, fully accounted for in
[current authority pin updates](../../docs/archimate/processing/publishing/1.0.8/current-authority-pin-updates.json). Historical
pinned inputs, source-history pins and expected historical XML hashes are unchanged.

## Historical evidence and retained references

The [preservation result](../../docs/archimate/processing/publishing/1.0.8/preservation.json) proves **166
moved tracked files byte-identical**; the remaining **17**
are the explicitly listed framework tooling/test/Makefile edits. All **110**
recorded wider evidence/source files retain their hashes. The only three changes
in that wider pre-move set are root release metadata, current baseline navigation
and the explained correspondence current-authority pins.

Release 1.0.6 evidence and all relocated 1.0.7 establishment archives/reports remain
unchanged. Existing external Increment 1 and real-Archi 1.0.7 proof bundles remain
at their original `.junie/reports/` locations, outside the original workspace move.
Files temporarily co-located with them during an intermediate layout were moved
back into their correctly classified processing locations; original bundles were
not rewritten. The [1.0.7 empirical acceptance report](2026-10-18-archimate-canonical-identity-establishment.md)
remains unchanged, including its identity/naming acceptance conclusions and known
whole-document transport limitations. Earlier preparation-time pending wording
is retained as historical evidence; it does not reverse empirical acceptance.

Historical literal paths remain in reports, fixtures, frozen governance assessment
narrative, archived command logs, original manifest dictionary keys and the
resource-owned sourceHistory field. Stable UUID namespaces and visual identity
keys also retain their spelling; they are not filesystem paths. The
[retained-reference classification](../../docs/archimate/processing/publishing/1.0.8/retained-reference-classification.json)
and [exact search results](../../docs/archimate/processing/publishing/1.0.8/final-retained-references.txt) distinguish these cases
from current navigation. `historical-locations.json` provides exact current file
locations while preserving expected content hashes. Ambiguous parent-directory
mappings are omitted in favour of exact file mappings. No old filesystem umbrella
is required. Frozen relative links may describe former locations; use this map.

The [ignored-file audit](../../docs/archimate/processing/publishing/1.0.8/ignored-file-audit.json) confirms no relocated original
tracked file is now ignored by Git. There is no demonstrated external dependency
requiring a compatibility wrapper. A pre-existing replay dependency on ignored
historical logs is recorded below rather than hidden.

## Release 1.0.7 → 1.0.8 architecture comparison

The [exact XML migration proof](../../docs/archimate/processing/publishing/1.0.8/migration-comparison.json) requires the one
model-level `baseline-architecture-release` value to change from 1.0.7 to 1.0.8,
then restores that value in memory and requires exact serialized document equality.
It also requires the actual published byte streams to differ only at that value.
No semantic Property, order, whitespace, Documentation or visual content is excluded.
This is a task-specific preservation assertion, not a new transport comparator.

| Invariant | 1.0.7 → 1.0.8 result |
| --- | --- |
| Semantic elements | 1,299 unchanged |
| Canonical IDs | 1,299 unchanged; zero allocated or modified |
| displayName, friendlyName, canonicalName | All 1,299 values per field unchanged |
| Native ArchiMate types and Harmonia semantic types | Unchanged, including all applicable harmoniaType values |
| Documentation and semantic Properties | Unchanged |
| Relationships and endpoints | All 852 unchanged |
| EC bindings | All 14 unchanged; registry byte-identical; read-only binding verification PASS |
| Views | All 1,196 unchanged |
| Visual objects | All 3,915 unchanged |
| Visual connections | All 1,449 unchanged |
| Geometry, styles, bendpoints and governed navigation/presentation | Unchanged, including all 2,922 bendpoints |

`architecture.json`, `motivation.json` and the identity registry retain their
pre-move bytes. No capability hierarchy or identity scope was expanded.
No relationship, View, node or connection Canonical IDs were introduced.

```text
Architecture semantic delta:       NONE
Architecture identity delta:       NONE
Architecture relationship delta:   NONE
Architecture presentation delta:   NONE
Repository structure delta:        YES
Tooling/path delta:                 YES
Documentation/path delta:           YES
Release metadata delta:             YES
```

Accepted 1.0.7 XML SHA-256:
`def447de2f522737c8b9c30dd9850a57bfec71eaa01f0820ff49283c40fba4bb`.

Resulting 1.0.8 XML SHA-256:
`504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a`.

## Publication comparison and deterministic regeneration

The [complete publication comparison](../../docs/archimate/processing/publishing/1.0.8/publication-comparison.json) inventories
all 14 outputs, before/after paths and hashes, classification and exact explanation.

| Output population | Difference and classification |
| --- | --- |
| Ten RADS textual/JSON projections | Byte-identical |
| Open Exchange XML | One model-level release value; release metadata only; exact architecture/presentation equality proved |
| Publication validation JSON | Three leaves only: architecture_release.version, architecture_release.previous_version, output_sha256; release metadata only |
| Publication report and inspection guide | New 1.0.8 processing documents describing the structure release, current paths, evidence and unchanged architecture; historical 1.0.7 documents preserved |

The [validation leaf diff](../../docs/archimate/processing/publishing/1.0.8/validation-metadata-differences.json) accounts for
every JSON change. The new XML hash follows its explained release-value change;
no expected historical hash was refreshed to suppress a difference. The
[full support-document diff](../../docs/archimate/processing/publishing/1.0.8/publication-support-documents.diff) accounts for
every line changed in the two release-specific documents. Their changes are
release/location/verification documentation, outside the exchange package; there
is **no publication architecture difference**. No path-only difference in an
otherwise frozen historical generated output was silently accepted.

1. The original committed 1.0.7 machinery independently reproduced all **14**
   accepted outputs byte-for-byte in an isolated checkpoint. See
   [checkpoint reproduction log](../../docs/archimate/processing/publishing/1.0.8/release-1.0.7-independent-retry.log).
2. The relocated machinery reproduced all **12 core** 1.0.7 outputs byte-for-byte,
   including its exact accepted XML. After the final direction split, a fresh
   process used the preserved 1.0.7 release fixture without changing current release
   authority and repeated that result. See [final-layout reproduction](../../docs/archimate/processing/publishing/1.0.8/final-layout-107-reproduction.json)
   and [execution log](../../docs/archimate/processing/publishing/1.0.8/final-layout-107-reproduction.log).
3. **Release 1.0.8 independently reproduces all 14 generated outputs byte-for-byte** in fresh generation/report processes. The current publication, report and independent-output Make targets all pass; release authority is unchanged. See [final Make execution](../../docs/archimate/processing/publishing/1.0.8/final-layout-make-checks.log).

No repeat real-Archi import/export is required solely for relocation. No new
empirical transport result or broader whole-document fidelity is claimed.

## Complete validation results

| Suite/check | Result | Evidence |
| --- | --- | --- |
| Framework publication/recovery, baseline, naming/identity, round-trip and workspace | PASS: 154 tests, 136.523 s | [full log](../../docs/archimate/processing/publishing/1.0.8/publication-tests-final-layout.log) |
| Baseline tests | PASS: 31 | Included in full framework log |
| Documentation / navigation / publication metadata / Views | PASS: 10 / 16 / 3 / 28 | Included in full framework log |
| Recovery | PASS: 14 | Included in full framework log |
| Identity/naming | PASS: 22 | Included in full framework log |
| Round-trip | PASS: 27 | Included in full framework log |
| Workspace boundaries / mutation rejection | PASS: 3 | Included in full framework log |
| Identity/release scripts | PASS: 25 tests, 30.554 s | [log](../../docs/archimate/processing/publishing/1.0.8/identity-release-tests-final-layout.log) |
| Repository architecture | PASS: 90 tests; zero failures/errors/skips; Maven 32.782 s | [log](../../docs/archimate/processing/publishing/1.0.8/architecture-tests-final-layout.log) |
| Resource-owned Canonical IDs / governed names | PASS: 1,299 valid, no missing/malformed/duplicate/multiple claims | [log](../../docs/archimate/processing/publishing/1.0.8/source_identity-final.log) |
| Identity registry / EC bindings | PASS | [registry](../../docs/archimate/processing/publishing/1.0.8/identity_registry-final.log), [bindings](../../docs/archimate/processing/publishing/1.0.8/ec_bindings-final.log) |
| Release policy authority | PASS: 1.0.8, previous 1.0.7 | [log](../../docs/archimate/processing/publishing/1.0.8/release-final.log) |
| Historical 1.0.5A recovery replay | PASS: original bytes and pinned source boundary | [log](../../docs/archimate/processing/publishing/1.0.8/historical_recovery-final.log) |
| Current publication --check | PASS: exact saved outputs, XSD, deterministic generation | [log](../../docs/archimate/processing/publishing/1.0.8/publication_check-final.log) |
| Exact architecture/presentation migration | PASS | [proof](../../docs/archimate/processing/publishing/1.0.8/migration-comparison.json) |
| Historical evidence preservation | PASS | [proof](../../docs/archimate/processing/publishing/1.0.8/preservation.json) |
| Make current output/report checks and independent reproduction | PASS: saved output/report checks and all 14 independent bytes | [log](../../docs/archimate/processing/publishing/1.0.8/final-layout-make-checks.log) |
| Current Markdown navigation links and final layout | PASS | [audit](../../docs/archimate/processing/publishing/1.0.8/final-workspace-audit.json) |

Commands ran with explicit timeouts: 180 s for framework tests; 120 s for
identity/release; 180 s for offline Maven architecture tests on JDK 21; 90 s for
publication/recovery commands; 30 s for report checking; 120 s for independent
reproduction. The Make verification group has a 180 s outer bound. No final check
is represented as PASS on timeout or missing output. Machine-readable detailed
counts are in [test results](../../docs/archimate/processing/publishing/1.0.8/final-test-results.json), commands and timings in
[command results](../../docs/archimate/processing/publishing/1.0.8/command-results-final.json), and hash-pinned pre-report checks
in [workspace verification](../../docs/archimate/processing/publishing/1.0.8/workspace-verification-1.0.8.json).

The commands executed from the relocated framework are:

```bash
python3 -B -m unittest discover -s docs/archimate/framework/tests -v
python3 -B -m unittest discover -s scripts/tests -p 'test_architecture*.py' -v
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn -o test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
python3 -B docs/archimate/framework/tools/synchronise_baseline.py
python3 -B docs/archimate/framework/tools/report_baseline.py
PYTHONDONTWRITEBYTECODE=1 make -C docs/archimate/framework \
  baseline-check baseline-report baseline-independent
```

## Earlier failed attempts, corrections and dependency limits

Earlier unsuccessful attempts are retained in processing/publishing/1.0.8 logs;
the final successful checks do not erase them.

- The isolated original 1.0.7 checkpoint initially lacked 22 pre-existing ignored
  historical logs required by the accepted report renderer. Copies were supplied
  only after their hashes matched the original evidence. The original 14-output
  reproduction then passed. See [dependency inventory](../../docs/archimate/processing/publishing/1.0.8/historical-ignored-dependencies.json)
  and original reproduction logs. Replaying that old report from a bare tracked
  checkout alone still needs those original logs; this pre-existing dependency is
  disclosed. The new 1.0.8 manifest references reviewable migration evidence logs.
- Initial portable publisher fixtures still expected outputs beside the framework.
  They were corrected to assert the governed new output paths, including parent
  creation. Full-suite passes followed; no architectural assertion was removed.
- A mistaken invocation of the identity `bindings` writer refreshed the saved
  historical bindings-validation.json. The preservation gate caught it. Exact
  original bytes were restored from the isolated checkpoint, then checks were
  rerun using the read-only binding API. Its original SHA-256 remains
  `330d7267e048e61ce8e664d4a105ed1923551a387d7ef8dca941db931dc13ba0`.
  No such change is retained.

There is no unresolved current operational path/dependency or classification
issue. Historical report renderers still require their historical checkout
fingerprints and release/verification fixtures; they are preserved replay tools,
not assertions that current 1.0.8 source hashes equal old tooling hashes.
Frozen historical relative links are navigated through the location map.

## Readiness and stopping boundary

Architecture Release **1.0.8**, previous **1.0.7**, is prepared under the existing
release policy. Release **1.0.7** remains the immutable accepted pre-restructure
identity-bearing architecture and empirical Archi 5.10 acceptance baseline.
The intended 1.0.7 → 1.0.8 delta is repository structure, paths, tooling,
documentation and release metadata only.

Recommend Solution Architect acceptance of the verified restructure. Upon
acceptance and committing the change set under the normal release mechanism,
**1.0.8** is ready to become the governed starting baseline for Increment 2.
No Increment 2 comparison/reconciliation workflow, presentation replay, architectural
change, new Canonical ID scope or new transport normalisation has begun.
