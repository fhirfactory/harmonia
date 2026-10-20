<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia ArchiMate framework

Reusable generation, validation, testing, comparison and recovery tools live here,
with schemas, projection configuration and framework baselines. `baseline/`
contains pinned historical XML and semantic/navigation locks for tests and replay.
`documentation-map.json` supports the projection. Architectural authority remains
in the [Markdown RAD](../../markdown/).

RAD-1.0.10 adds the [bounded initial IA projection](../../markdown/governance/initial-information-architecture-projection.md)
after the unchanged RAD-1.0.9 replay. `information_population.py` validates the
resource-owned capture and once-only allocation history, then introduces 50 native
BusinessObjects and 4 native DataObjects, flat navigation and no relationships or
diagram Views. It does not implement IA ingestion/reconciliation. `make all` uses
the current overlay and independently reproduces all 15 generated outputs.
------- Legacy Content - Superseded ------- Start ------
Superseded by: [IA vocabulary adjudication](../../markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary)
and [cross-layer Composition rule](../../markdown/governance/architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming).

The canonical-name vocabulary/context stop on the new resources remains explicit;
the existing diagnostic correctly reports incomplete future naming conformance.

------- Legacy Content - Superseded ------- Finish ----

The same unsigned candidate now completes local canonicalNames using BO/TOPIC
and the existing friendlyName components. `canonical-name-completion/` preserves
the original candidate, source/history and new verification. Composition ancestry
governs contextual naming across all layers; no IA Composition is created and no
root status is inferred. Context-dependent names await authored containment.
Existing contextual Business names without Composition remain a reported finding.
`synchronise_baseline.publish(information=False)` retains the pre-IA projection
boundary for historical regression tests. Presentation selection and Home audits
retain their original evidence release when replayed in the new publication.
The test timeout is 420 seconds: the established full suite required about
269 seconds before adding IA preservation tests; ongoing test completions remain
the progress criterion. Publication and individual checks keep their own bounds.

Requirements: Python 3.9+ and `lxml` from `requirements.txt`. From the repository root:

```bash
PYTHONDONTWRITEBYTECODE=1 make -C docs/archimate/framework all
python3 -B docs/archimate/framework/tools/governed_metadata.py validate
python3 -B docs/archimate/framework/tools/recover_v02.py --check
python3 -B docs/archimate/framework/tools/diagnose_roundtrip.py diagnose --baseline-context
```

`make all` generates the current publication, runs publication/recovery,
identity/naming and round-trip tests, checks saved outputs/reports and independently
reproduces all **15** current generated outputs byte-for-byte (14 for the
historical publication set). Generation reads the root
release authority and allocates no Canonical IDs. The XML goes to
`../exchange/publish/<release>/`. Validation, report and guide go to
`../processing/publishing/<release>/`. Eleven current textual/JSON projection outputs remain
in governed Markdown RADS. Complete 1.0.8 report generation requires its
hash-pinned `workspace-verification-1.0.8.json`; use
`python3 -B docs/archimate/framework/tools/report_baseline.py` to render it after
verification. The report checker does not manufacture passing evidence.

The existing Increment 1 fidelity diagnostic accepts explicit input paths.
A bare `--incoming` filename resolves inside `exchange/ingest/`:

```bash
python3 -B docs/archimate/framework/tools/diagnose_roundtrip.py compare \
  --published docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml \
  --incoming proposed-export.xml \
  --output /tmp/harmonia-transport-diagnostic.json
```

The raw fidelity findings retain their original meaning and comparison rules.
The same command also returns `architectural_review`: element correlation by
unique valid `canonicalId` before Exchange ID, with both representation bindings
and their file hashes/line locators retained. An Exchange-ID fallback applies only
when both records lack canonical claims. Lost, conflicting, multiple or duplicate
claims remain unresolved. Names and navigation never supply identity.

For authored-change detection, provide an explicit no-edit export of the same
baseline with `--control`. The review compares authored fields against that
control, retaining baseline/control differences as transport evidence. It does
not apply blanket text, metadata or navigation normalisation. Without a control,
field differences remain observations with unestablished transport origin.

```bash
timeout --signal=TERM --kill-after=5s 120s python3 -B \
  docs/archimate/framework/tools/diagnose_roundtrip.py compare \
  --published docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml \
  --incoming docs/archimate/exchange/ingest/1.0.10/harmonia-architecture-1.0.10.xml \
  --control docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/Archi-no-edit-export.xml.gz \
  --output /tmp/harmonia-information-change-review.json
```

`comparison_status` and exit code still describe raw representation fidelity
(`2` means raw differences or unresolved inputs); architectural population and
interpretation status are under `architectural_review`. `change_review.py` reads
native DataObject plus explicit `Specialization=Topic` as submitted Topic evidence.
It records native Composition context independently of folders and Views, and
flags stale derived metadata without deriving replacements. Topic graph cycles,
duplicate pairs and competing parents are review issues. These are candidate
interpretations: no disposition, identity allocation, metadata regeneration,
reconciliation or publication occurs.

An absent export remains an awaiting-input result. Explicit relative and absolute
input paths continue to work. Diagnostic outputs remain restricted to
`.junie/reports/` or `/tmp`. The hash-pinned real-input regression manifest is
`tests/fixtures/roundtrip/information-change-review.json`; the submitted export
must be present at its declared location. Tests never repair or copy it.

The subsequently governed [Ingestion Change Review contract](../../markdown/governance/archimate-ingestion-change-review.md)
and [run convention](../processing/ingestion/README.md#run-layout) define review
records and executable authorisation. The read-only comparison evidence above
supports those records; it does not implement their adjudication or reconciliation
workflow or relax the Increment 1 diagnostic output restriction.

`recovery-check` replays the pinned v0.2 snapshot with its 1.0.5 release fixture.
Earlier report renderers and `legacy-all` are historical replay machinery; they
require their corresponding historical release/verification fixtures and do not
replace current gates. Historical templates, manifest hash keys, UUID namespaces
and stable identity keys deliberately retain their original spelling.

`workspace_paths.py` resolves current framework, publishing/ingestion processing,
publish and ingest locations relative to the framework. Its explicit
`historical-locations.json` map resolves relocated historical inputs without
changing historical text or expected hashes. No obsolete umbrella directory or
compatibility wrapper remains. See the [workspace](../README.md), preserved
[Increment 1 procedure](roundtrip-increment-1.md) and [former README](historical-workspace-readme.md).

The [Architecture Modelling Strategy](../../markdown/governance/architecture-modelling-strategy.md)
governs explicit Home independently of ingestion. `home_relationships.py` reads
Archi's explicit specialisation through its property definition and replays only
the fixed accepted Business Home register. It infers no Home.

------- Legacy Content - Superseded ------- Start ------
Superseded by: [Home-derived naming/navigation](../../markdown/governance/architecture-modelling-strategy.md#home-derived-naming-and-navigation)

Added Home edges
are applied after legacy layout generation, so this change prescribes no new
folder/View placement.

------- Legacy Content - Superseded ------- Finish ----

The historical Home migration's bounded publication
validation and report remain in `governance-tooling/1.0.14/processing/`;
historical report renderers retain their original release scope and 14-output set.
The [non-RAD evidence manifest](../governance-tooling/1.0.14/manifest.json) and
exact historical location map preserve the original paths and hashes. The
publisher rejects this internal identifier as a RAD release and rejects its
reappearance in either architecture publication namespace. RAD report selection
does not compare architecture versions with internal governance/tooling versions.

```bash
python3 -B docs/archimate/framework/tools/verify_governance_evidence.py
```

Run this from the repository root to verify the relocated evidence and protected
RAD-1.0.9 bytes, and replay the preserved reconciliation audit using only the
explicit historical locator map. Frozen ledgers and reports retain their original
text and original link bases; they are not refreshed as RAD-1.0.9 evidence.

The authorised 1.0.8 reconciliation produces **RAD-1.0.9**, independently of the
preserved governance/tooling sequence. `reconcile_108.py` replays the immutable
[accepted resource capture](../../markdown/governance/baseline/accepted-ingestion-1.0.8.json)
after the existing projection. The capture persists nine allocated identities;
publication allocates none. Explicit Home remains independent of Realization.
The 93 unestablished and 18 ambiguous baseline Home cases remain unresolved.
The accepted Product catalogue is the fifteenth output. `report_baseline.py`
dispatches the current evidence-backed reconciliation report and inspection guide
in `processing/publishing/1.0.9/`. Run `verify_baseline_outputs.py` for independent
reproduction of all 15 outputs, including the report and guide. The real Archi
no-edit transport evidence and pending human rendering gate are recorded there.

The RAD-1.0.9 completion adds `home_context.py` after accepted-instance replay.
It uses explicit Home and governed semantic ancestry for both qualified names and
principal resource navigation. The current resource fields and prior-name provenance
are persisted in `docs/markdown/governance/baseline/home-derived-metadata.json`;
ordinary publication validates them and allocates no identities. The original
captures, Home register, ingestion/audit records and pre-completion candidate remain
preserved. Relationship references follow each source's resulting location.
Generated View presentation is unchanged.

```bash
python3 -B docs/archimate/framework/tools/home_context.py audit
python3 -B docs/archimate/processing/publishing/1.0.9/home-context-completion/roundtrip/verify-transport.py
```

The [completion evidence](../processing/publishing/1.0.9/home-context-completion/README.md)
records the full 212-element population, all six named examples, 111 untouched
unresolved Home cases and the new actual Archi transport. It completes the unsigned
RAD-1.0.9 candidate without a further release increment. The original
`reconciliation-verification.json` remains historical; the current report reads
`home-context-completion/verification.json`. The governance evidence verifier
replays historical resulting-state signatures against the preserved original
candidate, separately from current Home-projection validation.

## Information Architecture machinery-validation runs

The [governed lifecycle](../../markdown/governance/architecture-release-policy.md#machinery-validation-runs--2026-10-20)
keeps RAD-1.0.10 and the architect's export immutable while the naming machinery
is developed. The [naming contract](../../markdown/governance/canonical-identity-and-naming.md#information-element-naming--machinery-validation-decision-2026-10-20)
establishes `[BusinessObject] Element`, `[Topic] Element` and `[Artifact] Element`.
Topic recognition requires native DataObject and explicit Specialization=Topic.

From the repository root, generate a new run once, or verify an existing run:

```bash
timeout --signal=TERM --kill-after=5s 120s python3 docs/archimate/framework/tools/information_naming_run.py --run run-001
timeout --signal=TERM --kill-after=5s 120s python3 docs/archimate/framework/tools/information_naming_run.py --run run-001 --check
```

Use a new run number for subsequent naming attempts. Generation refuses existing
or incomplete output files; `--check` regenerates in memory and never writes.
The shared [specimen manifest](../processing/machinery-validation/information-architecture/specimen.json)
pins the same baseline, immutable submission and no-edit control for every run.
Output lives under `processing/machinery-validation/information-architecture/run-NNN/`,
never a publication-release directory. Runs leave root release authority unchanged.

`information_naming.py` resolves native semantics and reuses the shared lexical
and canonical-qualifier machinery in `governed_metadata.py`. Only explicit native
Composition supplies context. Missing classification, ambiguous ancestry and
unestablished ancestor vocabulary block dependent names. The historical 54-element
source publisher continues to replay its original suffix-based inputs; the run
contains a separate baseline naming preview. Exact governance transition snapshots
preserve old hash pins without allowing arbitrary live-document changes.

Run-001 contains a full `candidate.xml.gz` (decompress before importing into
Archi), proposed metadata with authored inputs and submitted claims, baseline
preview, control-aware change review, representation rebindings, verification
and a hash manifest. The model carries explicit proposed-candidate standing.
Verification reverses only the allowlisted naming edits and candidate marker,
then checks exact protected semantic/presentation content. Independent determinate
metadata may be proposed while other properties remain unresolved; unresolved
submitted claims stay visible and are not silently repaired. No canonical IDs,
Composition, physical Artifact population, architecture acceptance or RAD release
are created. Automation does not establish a real Archi round trip or acceptance.

## Governed round-trip engineering entry

The [ArchiMate Round-Trip Engineering Praxis](../../markdown/governance/archimate-round-trip-engineering-praxis.md)
is the repository authority for the two Solution Architect triggers. Root
`AGENTS.md` directs Codex and Junie to it. The agent follows the Praxis and the
existing Change Review template; tools support the stages, without assigning
architectural dispositions or guessing CR decomposition.

| Framework component | Reusable responsibility |
| --- | --- |
| `roundtrip_praxis.py` | Unique input discovery, comprehensive comparator/intake evidence and hash-bound review applicability. The agent writes the architectural review and checks reviewed directives under the CR contract. |
| `diagnose_roundtrip.py`, `element_correlation.py`, `change_review.py` | Offline XML validation, exhaustive transport/presentation observations, canonical-first correlation and architectural change evidence. |
| `relationship_state.py`, `containment.py` | Current/history selection before type-selected Home/Composition ancestry; one common resolver and graph walker. |
| `governed_metadata.py`, `information_naming.py` | Existing lexical qualification and explicit information-type/specialisation naming proposals, retaining submitted claims. |
| `workspace_policy.py`, `workspace_navigation.py` | Governed collection classification and deterministic Element/Relationship projection from shared current containment; no incoming folder ancestry. |
| `run_evidence.py`, `governance_history.py` | Guarded, archived in-run corrections; exact registered governance transitions without weakening historical pins. |
| Existing resource/publication adapters and `scripts/architecture_release.py` | Decision-linked persistence/publication and explicitly gated release management; historical adapters retain their bounded scopes. |

From the repository root, obtain the exact input selection or read-only intake:

```sh
python3 -B docs/archimate/framework/tools/roundtrip_praxis.py discover
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/framework/tools/roundtrip_praxis.py observe --output /tmp/harmonia-roundtrip-intake.json
python3 -B docs/archimate/framework/tools/roundtrip_praxis.py inspect-review --review PATH/change-review.md --intake /tmp/harmonia-roundtrip-intake.json
```

Use explicit `--baseline`, `--incoming`, and optional `--control` where necessary.
A control is never guessed from an unrelated export. `observe` writes evidence,
not an adjudicated Change Review. The governed agent prepares independently
adjudicable CRs and the full `change-review.md` checkpoint from that evidence.
`inspect-review` binds the reviewed document to the input hashes; it does not
approve its prose or execute directives. There is no unattended general-purpose
reconciliation engine. Unsupported scopes/adapters remain explicit under AX-17.

### Current Run-002 Element projection correction

Run-002 now declares the complete 1,394-element population. The runner is a
specimen adapter over shared capabilities, not the definition of the Praxis.
It preserves Run-001 naming/rebinding evidence, resolves containment once and
passes that model to naming, collection policy, projection and verification.

`workspace_policy.py` obtains collection categories/standing from resource-owned
RADS records by canonical identity. Recorded context may recover an existing
collection category; its parent/owner/naming paths never become current
containment. Missing current Feature Composition remains under `Unresolved
Feature Ancestry`; Business behaviour without explicit Home appears under
`Unestablished Home`. These are navigation findings, not new semantic parents.
Other new/unmapped classifications stop until a governed collection rule exists.
Collections are presentation configuration and need not exist in the input.

All Element and Relations branches are rebuilt, while the complete Views subtree
and Views navigation remain preserved. Every resolved containment participant,
including a contained leaf, receives a same-named Element context folder holding
its own reference; contained child folders nest recursively. Standalone policy
and collection/role classification remain unchanged. This supersedes the earlier
direct-leaf projection description below under the current
[governed rule](../../markdown/governance/archimate-round-trip-engineering-praxis.md#common-interpretation-and-projection-rules).
Final serialized XML regressions explicitly assert Care/self and
Care/Encounter/self, with Home and deeper Composition cases as well.
Relationships mirror their actual source;
existing source contexts are reused, and leaf sources receive a source folder.
Historical relationships keep their records and one navigation reference but
supply no current containment. The IA hierarchy and `[BusinessObject]` / `[Topic]`
names/native specialisation remain unchanged in meaning.

`element-generation-proof.json` records in-memory tests with **all** Element and
Relationship folders absent, then replaced by a deliberately misleading flat
folder. Both regenerate the exact same organisation as the candidate. The
immutable architect submission is never edited. Regression tests also change
semantic relationships while retaining old folders and require changed output.

Verify the current candidate read-only:

```sh
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/framework/tools/information_workspace_run.py --check
timeout --signal=TERM --kill-after=5s 120s python3 -B docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/reproduce_candidate.py
```

For an explicitly authorised correction only, `--before-archive PATH` invokes the
shared writer, which checks the archived manifest and every old generated output
before replacing anything. A stale archive or repeated correction is refused.
The before evidence for this correction is under
`run-002/validation/pre-element-projection-correction/`. Prior correction evidence
and Run-001 remain unchanged. This is still Run-002; RAD remains 1.0.10/1.0.9.
The subsequent contained-leaf correction retains its preceding state under
`run-002/validation/pre-contained-leaf-correction/` and proves exact preservation
of the existing Relationship navigation, including projection identities.
Actual Archi inspection/round-trip and acceptance remain separate human gates.

### Historical Run-002 implementation description

------- Legacy Content - Superseded ------- Start ------
Superseded by: [Round-Trip Engineering Praxis](../../markdown/governance/archimate-round-trip-engineering-praxis.md#common-interpretation-and-projection-rules)
and the current Run-002 correction description above. The following original
section is retained unchanged as implementation history.
---------------------------------------------------------

### Run-002 workspace organisation

Run-002 uses the same frozen inputs and reproduces Run-001 naming output through
the common containment model, followed by `workspace_navigation.py`. Verify it from the repository
root:

```sh
timeout --signal=TERM --kill-after=5s 120s python3 docs/archimate/framework/tools/information_workspace_run.py --check
```

The primary model is plain
`docs/archimate/processing/machinery-validation/information-architecture/run-002/candidate.xml`,
ready for Archi Exchange import. Generation refuses an existing run directory;
`--check` compares every generated byte without writing. Run-001 is retained
unchanged; its historical source-fingerprint manifest is not rewritten to reflect
new machinery. Run-002 explicitly verifies its naming and rebinding evidence.

The authorised containment correction is part of Run-002. `containment.py` reuses
the existing Home recogniser and the relationship-currentness selector extracted
from View selection into `relationship_state.py`. Filtering precedes resolution.
The contained element's native type selects Composition for Capability,
BusinessObject, DataObject and Artifact; Feature inherits Capability and Topic
inherits DataObject. Every other applicable type uses Home: directed Association
with explicit `Specialization=Home`, from contained element to its home. Ordinary
Association and Composition on other contained types do not supply containment.

Naming, workspace projection, the bounded Capability reader and containment View
selection consume this common interpretation. `Ancestry` is the single graph
walker, including explicit adapters for frozen source-qualified naming-context
replay. Those historical adapters preserve accepted names; they neither create
native relationships nor fill absent current Composition from owner records.
Run-002 resolves once and shares the model between naming, projection and checking.
There is no separate IA ancestry traversal. Folders never feed back into resolution.

`--correct-run-002` is the guarded, one-time replacement mode for this authorised
correction. It verifies the original manifest/output hashes against
`validation/pre-containment-correction/` before replacing generated evidence.
The archive retains the original report, machinery and findings unchanged.

For the declared information population (native BusinessObject, DataObject and
Artifact), only explicit native Composition supplies ancestry. Each element with
Composition children has a same-named navigation folder containing its own element
and direct children; children with descendants have nested context folders. Leaves
have direct references. Elements without Composition parents or children remain
directly in their native information collections. This establishes no complete
root status. Original IA folder placement has no effect on the derived hierarchy.
Collection anchors are presentation configuration, independent of containment.
The projector accepts other declared native-type populations/collection anchors;
it consumes their type-selected Home/Composition paths from the same resolver,
at arbitrary depth, without a list of IA domain names.

Competing parents, duplicate Composition assertions, cycles, missing endpoints or
containment crossing the declared population stop candidate generation before any
run output is written. No parent is chosen. Existing out-of-scope containment
uncertainty is reported while other Elements navigation remains intact. This
bounded projection does not reconcile other architectural populations.
The old 61 direct / 109 propagated findings disappear after preserved historical
assertions are filtered. They were not competing current containment. Assertions
remain in XML. The correction preserves the original candidate bytes, including
its IA folders and all source-based relationship navigation.

Relations mirrors each source element's resulting navigation path, with a source
element context for leaves and non-projected elements. A Composition parent's
existing context is reused. Every relationship appears once under its actual
source, regardless of type or target; empty relation branches are omitted. Retained
non-IA paths are presentation scaffolding, not newly derived semantic containment.

Workspace IDs use a versioned `workspace-` projection namespace. Element contexts
use unique canonical identity when present, otherwise explicit representation
identity scoped to this frozen model. This allocates no canonical IDs. Context IDs
exclude labels and Composition ancestry; native collection is a projection role.
Retained scaffold mirrors use existing folder identity, or their explicit label
path where the export has no folder ID. Relationship references use retained
relationship representation identity. All XML ID collisions stop processing.

`resolved-containment.json` records selected, excluded and wrong-mechanism
relationships, current ancestry and genuine findings. `workspace-projection.json`
records identity-based placements, paths, counts and
out-of-scope findings. The manifest pins frozen inputs, all Run-001 evidence,
native submission files, release authority and machinery sources. Byte snapshots
under `machinery/` preserve tools, schemas, regression tests and applicable rules.
Historical replay uses these snapshots with the hash-pinned frozen files. The
run's `validation/reproduce_candidate.py` restores archived machinery into an
isolated temporary tree and compares the complete candidate, metadata and
navigation evidence byte for byte. Its pinned import-time configuration snapshots
are retained under `validation/dependencies/`. The live `--check` also compares
the full manifest; inherited Run-001 rebinding evidence retains its original
absolute provenance locators. Model Documentation, Views and their organisation
are preserved. Documentation cleanup remains deferred. Neither
generation nor automated verification supplies manual Archi acceptance or a real
complete-model round trip.

------- Legacy Content - Superseded ------- Finish ----
