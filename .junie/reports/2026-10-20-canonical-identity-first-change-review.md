<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical-identity-first ArchiMate Change Review — implementation and verification

## Result and boundary

The normal `diagnose_roundtrip.py compare` command, supplied with the RAD-1.0.10
no-edit control, independently detects **38 evolved + 32 new + 0 deleted
architectural elements**. All **1,362 baseline canonical identities** correlate;
all **38 changed Exchange bindings** retain their original canonical identity.
There are 1,324 unchanged elements and no unresolved identity correlations.

The no-edit control detects **0 evolved + 0 new + 0 deleted**, with all 1,362
elements unchanged. The raw XML comparator's historical transport findings remain
exactly observable; its missing/additional representation records are not used as
architectural deletion/creation conclusions.

This work implements detection and candidate interpretation only. Disposition is
`NOT_RECORDED`, Outcome is `Unresolved`, and Execution is `NOT_AUTHORISED` in the
machine evidence. Detection status `DETECTED` does not accept the submitted
architecture. No architectural reconciliation, identity allocation, metadata
repair, relationship conversion or publication was performed.

## Authority loaded for this task

The repository instructions, documentation instructions and these sources were
loaded afresh; the prior first-pass report was navigation and empirical context,
not architectural authority:

- [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md):
  AX-04 keeps architecture independent of engine representation; AX-14 preserves
  identity, representation, type, classification, navigation and presentation as
  separate meanings; AX-15 preserves uncertainty; AX-17 keeps proposed changes
  distinct from accepted architecture and prohibits invented resolutions.
- [Canonical identity contract](../../docs/markdown/governance/canonical-identity-and-naming.md#architectural-identity):
  unique valid equal canonical IDs identify the same architectural instance;
  rename, retype and relocation retain identity. Exchange identifiers belong to
  a separate identity domain.
- [Change Review contract](../../docs/markdown/governance/archimate-ingestion-change-review.md)
  and [ingestion/publication semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md):
  observation, interpretation, disposition, outcome and execution remain separate;
  explicit Composition is separate from navigation and View presentation.
- [Release policy](../../docs/markdown/governance/architecture-release-policy.md):
  accepted-change patch governance remains a separate acceptance concern, discussed
  below. No release number is inferred from these submitted XML filenames.

The task explicitly authorises recognising the submitted native DataObject plus
Harmonia Topic specialisation. This implementation recognises that evidence;
it does not establish a new governed Topic publication profile.

## Inputs and retained evidence

The regression manifest pins all inputs:
[information-change-review.json](../../docs/archimate/framework/tests/fixtures/roundtrip/information-change-review.json).
The actual submitted XML is used directly; it is neither repaired nor replaced
by a synthetic extraction or the adjacent native Archi files.

| Input | SHA-256 of XML bytes |
| --- | --- |
| [RAD-1.0.10 publication](../../docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml) | `b1036f745e8ae3754951cd123b2b1a54a7eaa76c5fa83933abbd11ff2d4153bc` |
| [Architect-authored candidate](../../docs/archimate/exchange/ingest/1.0.10/harmonia-architecture-1.0.10.xml) | `034dd70974245523ec11b503a9cf0535827d9ff6f161cafcabf002555dbf16ed` |
| [No-edit control](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/Archi-no-edit-export.xml.gz), decompressed | `ac7fad3d30b230e894fa12e64e74e77939fc6574dd3336a8ddd7569f70c22b65` |

The compressed control file hash is
`1e2ecfb5585448c84eb009d22c9d1f7794d8e452f6d841865e08d675526aca8d`.
The machinery reports both container and XML hashes for compressed inputs.

Normal command outputs and verification logs are retained in
[the evidence directory](2026-10-20-canonical-identity-first-change-review-evidence/).
`authored-comparison.json.gz` and `control-comparison.json.gz` contain the complete
normal comparator outputs, compressed without altering their JSON contents.
`authored-diagnostic.json.gz` retains the normal identity diagnostic.
`pinned-transport-verification.json` retains the existing no-edit verifier's result.
The [38-row rebinding table](2026-10-20-canonical-identity-first-change-review-evidence/representation-rebindings.tsv)
provides each canonical ID, both Exchange IDs, both XML line locators and the
incoming Name without requiring the large JSON reports to be opened.
No bespoke identity-analysis script is needed to produce these results.

## Machinery changed

| File | Responsibility |
| --- | --- |
| [element_correlation.py](../../docs/archimate/framework/tools/element_correlation.py) | Shared canonical-identity-first correlation, claim validation and unresolved conflict evidence. |
| [change_review.py](../../docs/archimate/framework/tools/change_review.py) | Read-only element observations, Topic interpretation, explicit Composition context, stale metadata findings and control-aware population evidence. |
| [diagnose_roundtrip.py](../../docs/archimate/framework/tools/diagnose_roundtrip.py) | Adds `architectural_review` to the normal comparator, accepts `--control`, and uses shared correlation in identity diagnostics. Retains the original raw fidelity result domain and exit semantics. |
| [Framework README](../../docs/archimate/framework/README.md) | Documents normal invocation, result domains, control requirement and stop boundary. |
| [test_change_review.py](../../docs/archimate/framework/tests/test_change_review.py) and [test_roundtrip.py](../../docs/archimate/framework/tests/test_roundtrip.py) | Synthetic edge cases, real export/control regressions and revised canonical-swap diagnostic expectations. |

The raw comparator's changed-record index now scans material finding paths once
instead of rescanning all findings for every record. This changes no raw finding
classification or content. Compressed-input XML hashes are computed once per
input, avoiding repeated decompression during per-record provenance collection.

### Previous correlation behaviour

The raw comparator keyed semantic elements by Exchange `identifier`. When an ID
disappeared, it reported a missing subtree; a different ID produced an additional
subtree. Its contract-property census also looked up the old representation ID.
The identity diagnostic described an unchanged canonical ID attached to a changed
representation ID as a binding conflict or possible reuse.

These behaviours exposed **38 missing + 70 additional representation records**,
but did not identify that 38 of the additional records were existing instances.
The raw record census remains valuable transport evidence and is still reported.
Architectural continuity is now interpreted separately with the identity contract.

### New correlation precedence and unresolved cases

1. Resolve canonical Properties through their exact definition keys, retaining all
   claims, values, multiplicity and malformed/ambiguous claims.
2. Match a valid lowercase UUID32 occurring on exactly one element in each
   snapshot by canonical ID. Native type, names and representation ID do not
   override this match.
3. Use equal Exchange IDs only when **both** records have no canonical claim.
   Such matches are explicitly `REPRESENTATION_ID_ONLY`; they do not acquire
   canonical identity.
4. Report lost, changed, duplicate, multiple or ambiguously defined canonical
   claims as unresolved. Do not rescue them through a name/path match or silently
   classify the affected records as confidently new/deleted.
5. Identify genuinely unmatched additions and disappearances. An established
   canonical identity absent from the incoming snapshot is still reported as a
   disappearance; it is never silently approved for removal.

Canonical swaps on retained representation IDs keep canonical correlation primary
while also reporting conflicting representation claims. The existing swap test
now expects two claim-conflict findings and two separate representation rebindings,
instead of four findings conflating these meanings.

### Representation rebinding and history

Each rebinding records `canonicalId`, `baseline_id`, `incoming_id`, correlation
basis and `representation_identity=REBOUND`. In `architectural_review`, each side
also has its file path, file/XML SHA-256, original XML line and representation ID.
These records appear both in the element's evolution evidence and in
`representation_rebindings`. The complete evidence retains all 38 old and new
bindings. Neither the baseline XML nor an identity lock/registry is overwritten.

### Topic and Composition interpretation

A Topic requires **native `DataObject` plus one explicit `Specialization=Topic`
claim**, resolving the Property definition rather than assuming its transport ID.
The existing lowercase `specialization` interchange spelling is also recognised;
multiple/conflicting claims remain unresolved. `[Topic] …` is separately recorded
as a display convention and never substitutes for canonical identity or silently
retypes an element. A display qualifier alone, or Topic on an incompatible native
type, produces an interpretation issue.

`composition_context` contains incoming native Composition edges and their explicit
source identities. The Topic graph reports actual Composition endpoint pairs,
cycles, duplicate pairs and competing parents. Navigation locations and nested
View appearances are recorded independently; neither creates containment.
Aggregation remains Aggregation. No conceptual BusinessObject counterpart is
manufactured.

### Stale derived metadata

The native type, Name, Documentation and explicit specialisation remain authored
evidence. Conflicting `archimateType` or `displayName` Properties are reported as
`STALE_OR_CONFLICTING_DERIVED_METADATA`. Authored Topic evidence remains visible
when `harmoniaType` is absent or contradictory. Persisted friendly/canonical names
whose semantic inputs changed receive `DERIVATION_REVIEW_REQUIRED`.

For the real export the machinery reports:

| Finding on evolved elements | Count |
| --- | ---: |
| Native type differs from persisted `archimateType` | 6 |
| Native Name differs from persisted `displayName` | 36 |
| Persisted `friendlyName` requires derivation review | 38 |
| Persisted `canonicalName` requires derivation review | 38 |
| Explicit Topic lacks a persisted `harmoniaType` | 38 |

All original Property values remain in the evidence. No replacement names are
computed; a derivation-review finding is not a claim that a new governed contextual
name has already been established. Historical source path/hash/standing Properties
remain provenance, not acceptance of the changed semantics.

## Transport separation and acceptance evidence

Supply an explicit no-edit export of the comparison baseline with `--control`.
For each correlated element, the machinery compares authored fields with that
control and retains baseline/control field differences separately. Exact
control subtraction is evidence for this input lineage, not a blanket rule to
ignore whitespace, language, metadata loss, Documentation or navigation edits.
Without a control, field differences are explicitly observations whose transport
origin has not been established; the caller must not treat those counts as a
transport-filtered architectural population.

| Evidence | No-edit control | Architect-authored export |
| --- | ---: | ---: |
| Canonical identities correlated | 1,362 | 1,362 |
| Evolved elements | 0 | **38** |
| Genuinely new elements | 0 | **32** |
| Deleted elements | 0 | **0** |
| Unchanged elements | 1,362 | 1,324 |
| Representation rebindings | 0 | **38** |
| Unresolved baseline/incoming correlations | 0 / 0 | 0 / 0 |
| Explicit Topic nodes | 0 | 70 |
| Explicit Topic Composition edges | 0 | 59 |
| Topic cycles / duplicate endpoint pairs / competing parents | 0 / 0 / 0 | 0 / 0 / 0 |

All 38 evolved records independently contain each of: preserved canonical ID,
changed Exchange ID, BusinessObject → DataObject, Topic specialisation, changed
Name, changed Documentation, navigation relocation and new Composition context.
The twelve retained BusinessObjects and four pre-existing DataObjects are counted
among unchanged elements, with no enforced Topic convention or conversion.
The raw relationship census preserves 60 additional records: 59 Compositions
and one Aggregation. Relationship identity is not assigned an element canonical ID.

The exhaustive raw comparator still reports:

| Raw classification | No-edit control | Authored export |
| --- | ---: | ---: |
| Semantically equivalent normalisation | 65 | 62 |
| Presentation-preserving transformation | 0 | 0 |
| Information loss | 1,819 | 1,869 |
| Semantic difference | 1,669 | 1,669 |
| Unexplained difference | 4,871 | 5,138 |

The complete no-edit raw findings match the retained historical comparator output
exactly as JSON data. Their historical `SEMANTIC DIFFERENCE` label is left intact;
it is not reinterpreted as 1,669 newly authored architectural changes. Both normal
comparison commands return raw `DIFFERENCES` and exit **2**, as before. The separate
`architectural_review.status` is `DETECTED` with the populations above.

The normal `diagnose --previous` command also reports **0 continuity conflicts**,
**38 representation rebindings**, **1,362 valid canonical IDs**, and **32 elements
without canonical IDs**. Its snapshot audit is `PASS`; future identity readiness
remains `NONCONFORMANT` because new unaccepted elements have not received IDs.
That is not an instruction to allocate them.

## Reproduction and automated verification

Run from the repository root:

```bash
timeout --signal=TERM --kill-after=5s 120s python3 -B \
  docs/archimate/framework/tools/diagnose_roundtrip.py compare \
  --published docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml \
  --incoming docs/archimate/exchange/ingest/1.0.10/harmonia-architecture-1.0.10.xml \
  --control docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/Archi-no-edit-export.xml.gz \
  --output /tmp/harmonia-canonical-authored-comparison.json
```

For the no-edit run, set `--incoming` to the same control path, retaining
`--control`, and choose a distinct output. Output must not overwrite any input.
The report's `architectural_review.population` and `representation_rebindings`
are the normal machinery's acceptance evidence.

The 22 new tests cover equal canonical/Exchange IDs; changed Exchange IDs; native
retyping; rename; Topic specialisation; genuine new elements without canonical IDs,
including equal-name records; genuine canonical disappearance; lost/changed/malformed
claims; duplicate and multiple claims; ambiguous Property definitions; representation
fallback without canonical metadata; stale metadata; explicit Composition independent
of folders, nested Views and Aggregation; graph ambiguity; exact control subtraction;
control identity conflict; deterministic read-only output; lifecycle separation;
and the hash-pinned real export/no-edit regression. The existing canonical-swap
diagnostic test is updated as described above.

| Verification | Result | Evidence |
| --- | --- | --- |
| `timeout --signal=TERM --kill-after=5s 120s python3 -B -m unittest discover -s docs/archimate/framework/tests -p test_change_review.py -v` | **PASS: 22 tests**, 10.339 s | [Focused regression log](2026-10-20-canonical-identity-first-change-review-evidence/change-review-tests.log.gz) |
| Same focused invocation with `-p test_roundtrip.py` | **PASS: 27 tests** | [Round-trip log](2026-10-20-canonical-identity-first-change-review-evidence/roundtrip-tests.log.gz) |
| `timeout --signal=TERM --kill-after=5s 420s python3 -B -m unittest discover -s docs/archimate/framework/tests -v` | **PASS: 238 tests**, 383.343 s | [Framework log](2026-10-20-canonical-identity-first-change-review-evidence/framework-tests.log.gz) |
| `timeout --signal=TERM --kill-after=5s 300s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | **PASS: 90 tests**, no failures/errors/skips; Maven 19.893 s | [Architecture log](2026-10-20-canonical-identity-first-change-review-evidence/architecture-tests.log.gz) |
| Existing RAD-1.0.10 `canonical-name-completion/roundtrip/verify-transport.py`, bounded at 120 s | **PASS** | [Verifier output](2026-10-20-canonical-identity-first-change-review-evidence/pinned-transport-verification.json) |
| Normal control and authored `compare --control` commands, bounded at 120 s | **PASS for the acceptance populations**; raw `DIFFERENCES`, exit 2 | Full comparator JSON files in the evidence directory |
| Normal `diagnose --model … --previous …`, bounded at 120 s | **PASS audit**, exit 0; metadata/identity readiness findings remain visible | `authored-diagnostic.json.gz` |
| `python3 -B scripts/architecture_release.py check`; `git diff --check` | **PASS** | Release remains `1.0.10`, predecessor `1.0.9`; no whitespace errors |

Three exploratory combined regression invocations reached their 120-second
command limit: **TIMEOUT**, not a semantic FAIL. The last meaningful progress was
the real no-edit population assertion passing, followed by the raw-control
comparison. Diagnostic inspection isolated repeated decompression of the same
22 MB control during per-element provenance hashing. XML hashes are now cached
per Exchange input. The historical JSON/Python tuple comparison was also changed
to an exact JSON-data digest comparison, avoiding inappropriate large assertion
diffs. `timeout` regained control through TERM; these read-only checks had no
governed mutation in flight. The final 22-test run establishes the semantic result
after the fixes; no timeout uncertainty remains for those required behaviours.
The [retained timeout log](2026-10-20-canonical-identity-first-change-review-evidence/exploratory-timeout.log.gz)
records the last incomplete run. If this performance issue recurs, profile
compressed-input provenance before increasing execution limits.

The broader suite ran across the implementation work; the final focused runs
were repeated after the compressed-hash optimisation and additional ambiguous-key
and valid-disappearance fixture checks. Input hashes were rechecked after
verification. The final evidence manifest records output and tool-source hashes.

## Remaining limitations and architectural questions

- The control is explicitly supplied, not automatically selected by release name.
  Hash-pinned regression inputs and retained operator evidence establish the
  RAD-1.0.10 control used here. The comparator does not authenticate GUI history
  or claim new human rendering inspection.
- Two snapshots establish observed continuity, not lifetime non-reuse. Duplicate,
  malformed, conflicting and missing authority remain unresolved evidence.
- The added interpretation focuses on elements, Topic classification and explicit
  Composition context. The original exhaustive relationship/View/presentation
  probe remains available; this does not implement a full automatic CR-granularity,
  adjudication, authored-View preservation or reconciliation engine.
- Topic recognition is not a decision to accept a new Topic publication profile,
  contextual names, the 32 new elements, or any submitted relationships. Such
  decisions and metadata treatment remain separate Solution Architect work.
- The real regression deliberately requires the supplied incoming XML at its
  hash-pinned location. Its directory was already untracked at task start; the
  task does not silently track, copy, rewrite or replace the architect's files.
- Release handling was raised separately because AGENTS.md requires a patch for
  an accepted change set, whereas the current unsigned 1.0.10 publication tooling
  explicitly stops later publication pending IA carry-forward review. The root
  release authority remains unchanged during this reviewable implementation;
  any acceptance increment must follow the release policy without authorising
  architecture reconciliation or bypassing that publication stop.

## Preservation and stop boundary

Only reusable diagnostic/review code, its tests/fixture manifest, tooling usage
documentation and this implementation evidence are changed. The specified
baseline, candidate and no-edit XML hashes remain unchanged. RADS source captures,
identity locks/registries, the previous review, published packages and the submitted
native Archi files are untouched. No new IDs were allocated; none of the 38
canonical identities was replaced. No submitted Topic or relationship was accepted
into governed architecture. Work stops after implementation, tests and this report.
