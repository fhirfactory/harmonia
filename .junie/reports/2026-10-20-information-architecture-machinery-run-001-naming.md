<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Information Architecture machinery-validation run-001 — naming

## Result and stop boundary

Run-001 establishes the three governed information-element display conventions
and produces a **proposed full-model candidate** from the unchanged architect
submission. Naming metadata is deterministically proposed for **82 of 86** native
information elements: **12 BusinessObjects and 70 Topics**. **59** elements receive
Composition-derived context. Four legacy DataObjects remain unclassified under
the explicit Topic-specialisation rule. There are **zero Artifacts** in the input
or candidate; Artifact behaviour is exercised only by synthetic tests.

Identity-first detection remains **38 evolved + 32 new + 0 deleted**, with
**38 representation rebindings** and **1,362 preserved baseline canonical
identities**. The 32 new elements have no canonical ID in the specimen and remain
without one. No ID is allocated, no architecture is reconciled, no submitted
relationship is accepted, and no RAD release is created or incremented.

The candidate is processing evidence, not accepted architecture. Its model-level
Property states `run-001: PROPOSED; NOT ACCEPTED; NOT A RAD RELEASE`. Existing
unresolved submitted metadata remains visible; independent determinate metadata
does not establish complete architecture. No real Archi import/export proof or
Solution Architect acceptance is claimed for this candidate.

## Authority and lifecycle

Authority was loaded afresh from repository instructions, the Architectural
Axioms, the identity/naming contract, modelling strategy, release policy and
ingestion/publication semantics. Prior reports assisted navigation and supplied
empirical review context; they were not architectural authority.

AX-01 preserves information meaning; AX-04 keeps semantic decisions independent
of tooling; AX-14 distinguishes abstraction levels, architectural identity,
representation identity, authored semantics and derived metadata; AX-15 preserves
uncertain results; AX-17 keeps candidates and unresolved architecture separate
from accepted architecture. Native authored changes drive proposed derivation,
and machinery failure cannot author a different model to obtain success.

The authorised governance decision establishes this controlled cycle:

```text
RAD-1.0.10 + immutable architect-authored 1.0.10 submission
    → run-001 → review / machinery improvement → run-002 → run-003 → …
    → successful automation and real Archi round-trip proof
    → Solution Architect acceptance → RAD-1.1.0
```

Run numbers identify attempts, never releases. All attempts use the same
hash-pinned specimens; prior run evidence remains intact. The explicit
2026-10-20 cycle decision takes precedence over the general accepted-change patch
rule for this bounded machinery work, including its supporting governance.
`architecture-release.json` remains `version=1.0.10`, `previous_version=1.0.9`.
RAD-1.1.0 may be allocated only at the eventual accepted architectural transition.

## Immutable inputs and evidence

The shared [specimen manifest](../../docs/archimate/processing/machinery-validation/information-architecture/specimen.json)
pins inputs independently of processing-run numbers. Generation checks the byte
hashes before and after processing and preserves release authority. Compressed
control evidence has both container and decompressed-XML hashes.

| Input | SHA-256 |
| --- | --- |
| [RAD-1.0.10 publication](../../docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml) | `b1036f745e8ae3754951cd123b2b1a54a7eaa76c5fa83933abbd11ff2d4153bc` |
| [Immutable architect submission](../../docs/archimate/exchange/ingest/1.0.10/harmonia-architecture-1.0.10.xml) | `034dd70974245523ec11b503a9cf0535827d9ff6f161cafcabf002555dbf16ed` |
| [No-edit control](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/Archi-no-edit-export.xml.gz), compressed | `1e2ecfb5585448c84eb009d22c9d1f7794d8e452f6d841865e08d675526aca8d` |
| No-edit control, XML bytes | `ac7fad3d30b230e894fa12e64e74e77939fc6574dd3336a8ddd7569f70c22b65` |

All generated run material is under
[processing/machinery-validation/information-architecture/run-001](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/),
outside RAD publication-release directories:

| Evidence | Meaning |
| --- | --- |
| [manifest.json](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/manifest.json) | Run standing, frozen input hashes, unchanged release authority, machinery/governance fingerprints and generated-artifact hashes. |
| [candidate.xml.gz](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/candidate.xml.gz) | Complete proposed Exchange model, compressed without omitting model content. Decompress before any future Archi import. |
| [proposed-metadata.json](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/proposed-metadata.json) | Per-element authored inputs, identity claims, submitted derived claims, proposed values, differences, Composition paths and unresolved dependencies. |
| [baseline-naming-preview.json](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/baseline-naming-preview.json) | How the same machinery treats the frozen baseline, including all 50 original BusinessObject suffix names, without changing that baseline. |
| [change-review.json.gz](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/change-review.json.gz) | Full control-aware identity-first change detection, authored changes, stale-metadata evidence and Topic graph. |
| [representation-rebindings.json](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/representation-rebindings.json) | All 38 canonical IDs, old/new Exchange bindings, both source hashes and XML line locators. |
| [verification.json](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/verification.json) | Schema and protected-content checks, population, naming counts, no-allocation and unperformed acceptance/Archi gates. |
| [governance/manifest.json](../../docs/archimate/processing/machinery-validation/information-architecture/run-001/governance/manifest.json) | Exact before/after governance hashes and compressed before snapshots; historical release pins and captures are unchanged. |
| validation/ | Bounded command logs and validation ledger, including automated tests and read-only replay. |

The candidate XML SHA-256 is
`4bd981b239393c1ce09d5a831b52b1ebe82daf4807139e5414c25ec3e91a28a3`.
The manifest separately fingerprints its gzip container.

## Governance and documentation changed

| File | Change |
| --- | --- |
| [canonical-identity-and-naming.md](../../docs/markdown/governance/canonical-identity-and-naming.md#information-element-naming--machinery-validation-decision-2026-10-20) | Establishes displays, local-name extraction, explicit Topic classification, `BO`/`TOPIC`/`ARTIFACT` canonical qualifiers, conditional context and proposed-metadata boundaries. |
| [architecture-release-policy.md](../../docs/markdown/governance/architecture-release-policy.md#machinery-validation-runs--2026-10-20) | Establishes frozen-input run lifecycle, evidence namespace, no patch increments and eventual acceptance gate for RAD-1.1.0. |
| [architecture-modelling-strategy.md](../../docs/markdown/governance/architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming) | References the new naming decision while preserving Composition authority and the prohibition on physical population. |
| [archimate-ingestion-publication-semantics.md](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md#proposed-metadata-during-machinery-validation--2026-10-20) | Separates authored semantics, identity, submitted metadata and proposed regeneration; permits isolated machine candidates without architectural acceptance. |
| [framework README](../../docs/archimate/framework/README.md#information-architecture-machinery-validation-runs) | Documents generation, read-only replay, candidate handling and subsequent run numbering. Earlier workspace changes were retained. |

Historical suffix-based baseline captures, publications, prior reports and
completion manifests are preserved. Exact governance transition snapshots let
baseline replay validate its original authority fingerprints through this explicit
amendment; they do not waive arbitrary live-document drift.

## Naming machinery and behaviour

[information_naming.py](../../docs/archimate/framework/tools/information_naming.py)
resolves native Names, types, explicit Specialization and Composition. It reuses
the existing PascalCase and qualified-name derivation in
[governed_metadata.py](../../docs/archimate/framework/tools/governed_metadata.py),
which now supports explicit Topic specialisation and `ARTIFACT` vocabulary.
Historical InformationMap source replay retains its existing reviewed scope rule.

[information_naming_run.py](../../docs/archimate/framework/tools/information_naming_run.py)
checks frozen inputs, runs the existing identity-first review, produces metadata
and a full candidate, and verifies exact protected-content preservation. Generation
refuses an existing or incomplete output set. `--check` repeats processing in memory
and compares exact generated bytes without writing. Subsequent runs use a new
directory with the same shared inputs. No identity allocator or publisher is called.

[information_population.py](../../docs/archimate/framework/tools/information_population.py)
adds exact validation of the preserved governance hash transition; it continues
replaying the original 54-element RAD-1.0.10 source capture. It does not apply the
new naming proposals to accepted resources. Identity-first correlation/review
machinery and its prior evidence were retained without alteration by this task.

| Abstraction | Recognition and proposed behaviour |
| --- | --- |
| Business / Conceptual | Native BusinessObject. `Information Unit BO` → `[BusinessObject] Information Unit`; local names `InformationUnit` and `BO-InformationUnit`. Matching display prefixes are omitted from lexical components. All 50 baseline BusinessObjects receive valid prefix proposals in the preview; 12 remain BusinessObjects in the submission. The 38 architect-retyped instances are treated as Topics, retaining canonical identity. |
| Application / Logical | Native DataObject **and** exactly one explicit Specialization=Topic. `[Topic] OrganisationalUnit` retains that display. Under the authored Enterprise Composition it proposes `Enterprise.OrganisationalUnit` and `TOPIC-Enterprise.TOPIC-OrganisationalUnit`. All 70 submitted Topics receive names and `harmoniaType=Topic`; stale BO names/types do not reverse the authored retyping. |
| Technology / Physical | Native Artifact uses `[Artifact] Element`, a qualifier-free local friendly component and `ARTIFACT-` canonical qualification. Synthetic `FHIR Package` → `[Artifact] FHIR Package`, `FhirPackage`, `ARTIFACT-FhirPackage`. No additional subtype or physical population is invented. |

Only explicit native parent/source → child/target Composition establishes naming
ancestry. The submitted Topic graph has 70 nodes and 59 Composition edges, with no
cycles, duplicate endpoint pairs or competing parents. All 59 child paths generate
contextual names; eleven Topic roots and twelve BusinessObjects receive local
candidate names without asserting complete root status. Acceptance of these
semantic inputs remains separate. No folders, Views, visual nesting, Grouping,
Aggregation, Specialization relationship, Realization, Association or lexical
similarity supplies ancestry. Elements at different abstraction levels remain
independent; no counterpart or cross-level mapping is created.

The run records each `canonicalId` claim independently from names and derives no
identity from those names, types or paths. All 54 existing information identities
and all 1,362 model-wide baseline identities are preserved. The 32 new Topics retain
their authored Exchange identifiers without receiving canonical IDs.

## Regenerated metadata and unresolved matters

| Property | Run-001 result |
| --- | --- |
| archimateType | Proposed from the native type on all 86 information elements; 38 values differ or are absent (six stale inherited values and 32 new elements). |
| displayName | Determinate for 82 classified elements; 80 values differ or are absent. The candidate's native Name equals proposed displayName for these elements. |
| friendlyName | Proposed for 82 elements from architectural local names and explicit Composition paths; 82 values differ or are absent. |
| canonicalName | Proposed for the same 82 elements with each component's governed qualifier; 82 values differ or are absent. |
| harmoniaType | Proposed Topic on 70 elements; no additional BusinessObject or Artifact subtype. An inapplicable derived subtype may be proposed for removal without changing authored Specialization. |
| canonicalId | Preserved, never regenerated. All 32 absent IDs remain absent. |

No displayName, friendlyName or canonicalName collisions occur within the 82
proposed classified information elements. This does not establish lifetime naming
uniqueness or architectural correctness beyond the inspected population.

Four unchanged DataObjects retain `Context Topic`, `Pragma Topic`,
`PetasosParcel Topic` and `Pylai.Diadromí Topic` Names without explicit Topic
Specialization. Run-001 proposes only their native archimateType and records the
remaining classification/naming dependency as unresolved. Suffixes and inherited
TOPIC canonicalName values cannot supply the missing authored specialisation.
Their existing submitted naming Properties remain visible in the candidate.

Unknown or incompatible classification, malformed/ambiguous qualifiers, cycles,
competing/duplicate Composition parents, missing endpoints, unsupported ancestor
naming vocabulary and unproven digit-leading spelling stop dependent names.
Independent determinate fields may still be proposed. This bounded IA resolver
does not implement every native ancestor class or infer a spelling decision from
stale derived metadata. It does not adjudicate native relationship legality or
Harmonia meaning merely because XSD validation succeeds.

## Verification

All commands are bounded and run from the repository root. Logs and the final
validation ledger are retained under the run's `validation/` directory.

| Check | Result |
| --- | --- |
| Focused naming tests: `timeout --signal=TERM --kill-after=5s 120s python3 -m unittest discover -s docs/archimate/framework/tests -p 'test_information_naming.py' -v` | PASS — 22 tests. |
| Framework tests: `timeout --signal=TERM --kill-after=5s 420s python3 -m unittest discover -s docs/archimate/framework/tests -v` | PASS — 260 tests in 302.675 seconds, zero failures/errors. Includes baseline replay, canonical metadata, identity-first review, naming, navigation, presentation, recovery and workspace gates. |
| Required architecture tests: `timeout --signal=TERM --kill-after=5s 300s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | PASS — 90 tests, zero failures/errors/skips; Maven 18.829 seconds. |
| Run generation: `timeout --signal=TERM --kill-after=5s 120s python3 docs/archimate/framework/tools/information_naming_run.py --run run-001` | PASS. |
| Exact read-only replay: same command with `--check` | PASS — candidate, metadata, evidence and source-fingerprint manifest reproduce. |
| Candidate XSD / identifier validation and independent allowlist reversal | PASS — restoring only proposed naming fields and the candidate marker reproduces the submitted model's protected content exactly. |
| `python3 scripts/architecture_release.py check` and frozen-input hashes | PASS — release remains 1.0.10 / 1.0.9; frozen inputs unchanged. |
| `git diff --check` | PASS. |
| Real candidate Archi round trip / Solution Architect acceptance | NOT PERFORMED / NOT RECORDED. |

The initial focused test run exposed three helper/harness failures: a case-folded
Property-definition identifier collision in synthetic Specialization probes and
a temporary-output-root reporting assumption. The helper now distinguishes exact
Property keys deterministically, and the isolated output test supplies its root.
The corrected 22-test invocation passes. The initial failure log is retained as
development diagnostics; it was not evidence of an architectural defect and did
not cause any edit to the frozen specimen.

## Candidates for run-002

1. Establish an explicit governed treatment for the four pre-existing logical
   DataObjects lacking Topic Specialization. Do not repair the frozen submission
   or infer classification from their suffixes or stale metadata.
2. Keep the missing canonical IDs on 32 new Topics explicit. Any eventual
   allocation requires authorised accepted-source persistence; repeatable naming
   machinery must not allocate them during experimental runs.
3. Review the eleven local Topic candidates and remaining local BusinessObjects
   without manufacturing parents or declaring root completeness. Additional
   ancestor vocabulary or spelling decisions require explicit authority if a
   later bounded case needs them.
4. After machinery review, perform a real complete-model no-edit Archi round trip
   for the selected run candidate and compare native semantics, naming Properties,
   canonical identities and representation effects. Existing baseline control
   evidence is not proof for the newly generated candidate. Preserve any new
   differences for the next run; do not hide them through specimen changes.

These are follow-up candidates, not reconciliations performed here. Work stops
after run-001 evidence, verification and this report. No run-002 or RAD-1.1.0
directory, model or release authority has been created.
