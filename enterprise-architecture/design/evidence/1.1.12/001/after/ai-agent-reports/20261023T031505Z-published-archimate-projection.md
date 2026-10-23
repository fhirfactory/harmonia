<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Execution Report — Published ArchiMate release projection

Report-Produced-At: 2026-10-23T03:15:05.082845Z

The attributable system UTC timestamp above records emission of this completed
Report through the execution tool. It is a Report-production event, distinct
from task issuance, validation, file metadata or architectural acceptance.
Executor: Codex. HCRL: 1.1.12 iteration 001.

## Task, source and standing

This Report corresponds to the [formal Task](../../before/ai-agent-tasks/unresolved-published-archimate-projection.md),
which retains the Solution Architect's operative instruction verbatim. Its
instruction-entry timestamp was not exposed; `Task-Issued-At` and its required
filename prefix remain UNRESOLVED for Solution Architect treatment before
closure. No current clock or Git/file timestamp is substituted for that event.

Task entry was clean accepted commit
`ed8121ac8eccddb578641c5b2f88bb869950764e`, release 1.1.11 / previous 1.1.10.
The completed register HCRL 1.1.11-002 establishes acceptance; the linked pending
copy is historical review input. Both are unchanged. The issued task authorises
this bounded publication implementation and verification, and preparation of a
pending 1.1.12 HCRL. It does not confer acceptance or closure.

Canonical AX-04/08/14/15/17, Architecture Governance, Review Praxis, Change
Provenance, release policy and applicable Continuum/round-trip contracts were
loaded. Reusing the projector keeps machinery subordinate to canonical authority;
publication, workbench proposals and evidence retain distinct standing. No
architectural conflict or new semantic design decision was discovered.

## Published result and its derivation

Final repository-relative publication path:

`enterprise-architecture/design/archimate/published/1.1.11/Harmonia-Architecture.xml`

Published XML filename: **`Harmonia-Architecture.xml`**, the existing maintained
generator/Manifest convention, within the requested release-qualified structure.
This is the **ArchiMate Open Exchange projection of the accepted Harmonia
Architecture Continuum release 1.1.11**. It is a durable release artefact suitable
as the starting model for Archi 5.10, with no independent architectural authority.

The new file was generated from the live accepted canonical inputs, their
unchanged navigation and maintained presentation template while root release
authority still recorded 1.1.11, using:

```bash
python3 -B enterprise-architecture/tooling/architecture_continuum.py project --publish \
  --report enterprise-architecture/design/evidence/1.1.12/001/after/projection-validation.json
```

Declared Python dependencies were supplied by the already installed packages
under `/tmp/harmonia-ea-deps` through `PYTHONPATH`; no historical generator,
historical configuration or network installation was used.

XML size: **26,617,294 bytes**. SHA-256:

`e33ae52b3934cf4d9b9b0102ce65e09461fa554022f43d56f2637685d8c39caa`

The file was not copied from an intermediate, test model, evidence copy or
post-Archi export. The retained [verification](../publication-verification.json)
and [bounded reproduction/check script](../verify-publication.py) establish:

- two independent executions of the original accepted projector against an
  isolated Git snapshot of `ed8121a` produce exactly those bytes;
- the current maintained projector against that same accepted source tree also
  produces exactly those bytes;
- the unchanged accepted Manifest Exchange publication matches byte for byte;
- the accepted actual-Archi baseline hash matches, while its returned control's
  hash differs. No incoming file was chosen by timestamp or naming similarity.

The supporting script invokes the maintained projector and validators; it is
iteration-specific verification, not a second production generator.

## Bounded machinery and documentation changes

`enterprise-architecture/tooling/architecture_continuum.py` extends the existing
`project` command with `--publish`. It derives the destination from the source
tree's root release authority. Combined `--output ... --publish` emits both the
existing Manifest XML and durable ArchiMate XML from one validated generation.
No version override relabels current architecture as a historical release.
An explicitly selected historical `--root` must contain its actual source inputs.

Repeated identical publication is safe. Differing existing durable XML is rejected
before either destination is written. `--check` checks all selected destinations
without writing. Publication never allocates architectural identities or increments
the release. The existing Makefile `continuum` and `check` commands use this mode;
the existing independent-generation check now verifies the durable XML as well.
There is no second generator, release process or general release automation.

Two bounded regression tests check real source generation, repeated publication,
read-only missing/stale checks, corruption rejection, preservation of the other
destination after rejected writes, next-release routing, preservation of previous
published bytes and the absence of implicit release increments/identity allocation.

Current documentation changes are confined to:

- `enterprise-architecture/design/archimate/README.md`: links the accepted 1.1.11
  starting XML and explains canonical authority, released projections, architect
  exports, evidence copies and deterministic generation/reproduction;
- `enterprise-architecture/tooling/README.md`: documents the additional output,
  existing command integration, checks and release-history protection;
- `enterprise-architecture/governance/archimate-round-trip-engineering-praxis.md`:
  adds only the publication location and artefact-role clarification. Existing
  triggers, exact-input discovery, adjudication and application stages are unchanged.

An architect-modified Archi export is proposed input to a subsequent round-trip,
retained as appropriate `before` evidence of that activity. It neither changes
nor supersedes the Continuum merely by existing. Evidence copies demonstrate
generation/testing/transport/transition and are not the normal place to retrieve
a published release. No current governed requirement for a permanent ingest
directory was found; generic incoming-landing references do not mandate one.
No ingest directory, automatic ingestion or future input-handling design is added.

## Verification and practical limits

| Required verification | Actual result |
| --- | --- |
| Accepted canonical 1.1.11 derivation; no accidental input selection | PASS: accepted Git snapshot, original/current projector correspondence and exact historical baseline/Manifest byte equality. |
| Canonical element population | PASS: 1,602 elements; exact canonicalId population, representation bindings, native Names/types, content, naming roles and standing. |
| Semantic relationship population | PASS: 1,651 edges; identifiers, endpoints and native types match canonical assertions; full XML matches accepted projection. |
| Authored Views | PASS: 1,209; complete template View subtree including layout/geometry is preserved. |
| Required navigation metadata | PASS: all 1,602 elements retain exact coordinates, paths and typed navigation references from accepted navigation. |
| Open Exchange validity | PASS: secure parsing, bundled offline Diagram XSD, identifiers/references, governed containment and information naming checks. |
| Supported Archi compatibility | PASS: actual Archi 5.10.0 import, native save, no-edit export and validation using the new published path. |
| Deterministic generation | PASS: original accepted projector twice, current projector against accepted inputs and isolated maintained-input generation agree exactly. |
| Existing current EA tests | PASS: all 62 maintained tests in the isolated tree, including two new publication regressions; zero failures/errors. |
| Focused publication/source suite | PASS: 22 tests, zero failures/errors, 197.294 seconds. |
| Java architecture suite | PASS: 90 tests across 11 architecture classes; zero failures/errors/skips, Java 21; offline Maven build succeeds in 29.893 seconds. |
| Accepted-source staleness/links | PASS: `make -C enterprise-architecture/tooling check`, including both Exchange destinations, with actual accepted 1.1.11 release inputs. |
| Browser regression smoke check | PASS: population, identity search, selection, Feature filter and combined currentness filter; DOM contract only. |
| Accepted 1.1.11 preservation | PASS: all 1,688 protected files equal their committed bytes and the protected file population is unchanged. |

[Reproducibility](../reproducibility.json) records **20 identical derived artefacts**
in the isolated maintained-input tree: the previous 19 plus the durable ArchiMate
XML. That tree contains no `docs/`, `scripts/`, Git metadata, historical framework
or historical processing configuration. Only required current canonical sources,
governance, presentation inputs/tooling and declared retained evidence are supplied.
The initial source-tree snapshot itself uses Git solely to identify/recover the
accepted historical input for this bounded verification, not as a production
projection dependency.

[Actual transport](../archi-transport.json) retains the compressed no-edit return
and attributed execution log. Canonical workbench review reports zero element
changes, zero relationship/View changes and zero unresolved canonical findings.
The new no-edit return also reproduces the accepted control hash
`a61375509cfbd4c69dd8cee112ce72880c07122e9eec4dcc0c3dd9af1b60a8e6`.
Archi adds language attributes and restructures organisation. The raw comparator
continues to report representational differences, including information-loss and
unexplained categories. They are retained, not normalised away or described as
whole-model byte fidelity. The canonical fields, semantic edges, Views and all
derived element navigation properties survive; accepted 1.1.11 folder/context
transport limitations remain unchanged. Neither new interactive visual inspection
nor Solution Architect architectural acceptance is asserted.

Earlier attempts are distinguished in [test results](../test-results.json): the
first Archi sandbox launch **FAILed** at GTK initialization before XML import;
the display-enabled isolated-workspace retry succeeded after automatic sandbox
approval. An initial supporting-check script **FAILed** because it compared
list-valued parsed properties to scalar expected values; that check was corrected
and the complete source/publication verification then passed. These are resolved
environment/check defects, not failures of the published XML. No TIMEOUT or
STALLED outcome occurred.

## Release, preservation and matters for disposition

The existing expected-current release-policy operation prepares exactly one patch:
**1.1.11 → 1.1.12**, previous version 1.1.11. This records the publication/tooling
change set and supplies no acceptance. The generated initial XML intentionally
remains a projection of accepted **1.1.11**. No speculative newly accepted 1.1.12
Continuum or unrelated reader publication is manufactured. Staleness and transport
checks above used actual 1.1.11 inputs before that tooling-only release preparation;
they are not claims that absent 1.1.12 reader publications passed a current-release
staleness check. Future accepted Continuum publication uses the existing command
and its existing acceptance gates.

Canonical Markdown, the root generated Continuum Manifest, accepted navigation,
presentation template/authored Views, 1.1.11 Manifest and RAD publications,
1.1.11 evidence, pending HCRL and completed register HCRL remain byte-identical to
accepted commit `ed8121a`. There are no Java/runtime changes, new identities,
semantic relationships, altered standing or unrelated cleanup. The worktree
contains this new change set; no commit, push or acceptance is asserted.

The [pending HCRL 1.1.12-001](../../../../../review/1.1.12/hcrl-1.1.12-001.pending.md)
records the publication meaning and actual implementation/verification facts.
Solution Architect review/result confirmation and treatment of the unavailable
Task instruction-entry event/prefix remain outstanding. The evidence-layout check
therefore reports **UNRESOLVED** temporal provenance, with no layout errors.
No other new semantic issue requiring disposition was discovered. The HCRL is
not accepted, completed or closed.
