<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate projection and review workbench

The maintained source is the [individual-element continuum](../../README.md).
[projection-template.xml](projection-template.xml) owns existing model-level
properties, property definitions, View navigation and **1,216 Views**, including their
geometry. Its element/relationship collections are empty. The current projector
fills those collections from canonical Markdown and preserves the Views.
From 1.1.10, canonical [Continuum navigation](../../continuum/navigation.json)
owns architectural organisation. The projector replaces architecture folders
from that tree through a native-layer-aware adapter and exposes every coordinate in organisation labels and element
location properties. It preserves native Name/displayName and identity. The
separate View folders remain presentation navigation, outside architectural
numbering. No semantic relationship follows from folder nesting.

The **Architecture Continuum is canonical architecture authority**. Open Exchange
XML is its derived projection/interchange representation; **Archi is the architecture
workbench**. Import the published
[1.1.16 Open Exchange projection](published/1.1.16/Harmonia-Architecture.xml)
into Archi 5.10 to begin work on that release baseline.

The [1.1.16 projection](published/1.1.16/Harmonia-Architecture.xml)
establishes the navigable architecture baseline incorporating the Calliope Information Unit foundational model, requirement groupings, and overview views,
accepted by the Solution Architect in [completed HCRL 1.1.15-001](../register/1.1.16/hcrl-1.1.15-001.complete.md).
The [1.1.15 projection](published/1.1.15/Harmonia-Architecture.xml)
remains the immutable predecessor baseline, and [1.1.14](published/1.1.14/Harmonia-Architecture.xml)
remains the historical comparison baseline for the 001 candidate submission.
The [accepted 1.1.13 projection](published/1.1.13/Harmonia-Architecture.xml)
implements the Solution Architect's workbench-remediation dispositions, accepted
after actual Archi 5.10 visual inspection in [completed HCRL 002](../register/1.1.13/hcrl-1.1.13-002.complete.md).
Its [actual imported tree evidence](../evidence/1.1.13/002/after/contained-elements/inspection.html)
is a reader-facing inventory of the retained Archi 5.10 native save, with canonical
IDs and folder documentation; it is not a substitute for inspecting Archi or an
assertion of acceptance. The accepted 1.1.11 publication remains historical.

<a id="physical-workbench-organisation--1113-candidate"></a>
## Physical workbench organisation — 1.1.13

Canonical navigation semantics are shared across projections; physical navigation
representation may be projection-specific where required by the target. RAD and
Manifest continue to consume the unchanged shared tree. The Exchange adapter
preserves native Archi layer containers and the canonical coordinates:

- Every canonical member of an established governed containment hierarchy has a
  numbered directory containing its single canonical element, including leaves.
  Contained children's directories nest below it. Adding or removing a child
  leaves the parent's directory and canonical placement intact. This applies to
  the three Strategy Capability scopes (including Features under their recorded
  parent/owner ancestry) and logical Information Architecture Topic decomposition.
  Existing explicit, directed Home relationships additionally establish model
  containment for 212 Business Behaviour elements (99 Services, 104 Functions,
  nine Processes). They receive directories at their unchanged governed Business
  navigation homes; Capability contexts and allocations remain unchanged.
  Their existing coordinates, qualifications and ancestry remain unchanged.
- Containment comes from the applicable governed model, never a universal rule
  inferred from native relationships or folders. Other navigation parents retain
  folder-plus-element representation for Archi mechanics; Motivation Stakeholder
  associations do not thereby become containment. Unclassified/standalone subjects,
  application scopes, contextual allocations and reference indexes are not turned
  into containment hierarchies. The existing governed model resolver selects
  applicable Home/type-selected Composition relationships with their state, while
  historical/index occurrences retain reference presentation. No ordinary
  Association or allocation is reinterpreted as containment.
- Business Behaviour Capability contexts and Motivation Stakeholder references
  remain navigation-only folders, carrying existing canonical/association metadata.
  Explicit native category markers preserve them, source-only Governance folders,
  the System Map reference, and unpopulated authorised domain headings.
- Cross-Cutting References is a qualified index in each applicable native layer:
  Stakeholder Index under Motivation and Historical Capability Identities under
  Strategy. These are reference indexes, not architectural decomposition; each
  canonical indexed subject is placed once. Existing standing and successor links
  remain visible. No full foreign domain is copied into another layer.
- Business ValueStreams retain their native Strategy type and their canonical
  `III.2.2` coordinates. Strategy contains the explicitly labelled `III.2.2 Value
  Streams — Business navigation reference; native Strategy` folder. The accepted
  Business path contains reference-only folders identifying each canonical subject
  and its native location. There is no repeated live element placement and no
  `III Business` branch under Strategy.
- All 1,209 authored Views and their original folders remain unchanged. Relations
  retain Archi's native Relations organisation; this remediation does not introduce
  semantic containment or a new relationship-folder hierarchy.

Folder IDs are deterministic, mutable representation locators with no Canonical
ID. Folder documentation links subjects and source locators; it never establishes
another architectural element, relationship or authority. The native workbench
does not provide clickable element aliases for reference-only folders: use their
documented canonical identifier/coordinate and the element's unchanged navigation
properties. Archi may sort displayed children; no semantic order follows from it.

The Solution Architect's [contained-element clarification](../evidence/1.1.13/002/before/ai-agent-tasks/unresolved-contained-element-directories.md)
supersedes the original iteration 002 parent-only directory policy. Earlier
Task/Report and inspection evidence retain their original meaning. The Solution
Architect has completed actual workbench visual review and accepted 1.1.13 / 002.

The [Review Praxis safeguard](../../governance/architecture-review-praxis.md#5-report-and-verify-the-incorporated-result)
requires import into the supported version and Solution Architect review of the
actual workbench tree before acceptance of material organisation changes.

## Publication and round-trip use

The [1.1.16 projection](published/1.1.16/Harmonia-Architecture.xml), completed under
[HCRL 1.1.15-001](../register/1.1.16/hcrl-1.1.15-001.complete.md), incorporates the
accepted Calliope Information Unit foundational model (`model.core.information.InformationUnit`),
requirement groupings, 7 overview Views, and 17 intentional preexisting View relocations.
The [1.1.15 projection](published/1.1.15/Harmonia-Architecture.xml), prepared under
[HCRL 1.1.15-002](../review/1.1.15/hcrl-1.1.15-002.pending.md), carries the
machinery change's release metadata over the unchanged canonical architecture as the immutable predecessor baseline.
Its elements, relationships, Views and navigation match 1.1.14. Incorporated-result
confirmation for HCRL 1.1.15-002 remains pending with the Solution Architect. This projection applies none
of the candidate changes from HCRL 1.1.15-001.
The immutable 1.1.14 publication remains the historical comparison baseline for the 001 candidate submission.

`published/<release>/Harmonia-Architecture.xml` is the durable release artefact
for loading that Continuum release into Archi. The filename follows the maintained
projector's existing convention. The initial 1.1.11 artefact is generated from the
accepted 1.1.11 Continuum and presentation template, with the same bytes as its
accepted Manifest Exchange publication. That older publication remains unchanged.
Adding this operational location does not change 1.1.11 architecture or navigation.

A local native `.archimate` workspace is a human workbench. XML exported after
architect changes is **proposed input** to a subsequent Architecture Archi-Roundtrip;
it neither modifies nor supersedes the Continuum merely by existing. Retain the
exact submitted export as appropriate `before` evidence of the activity reviewing
it, with exact baseline correspondence under the existing provenance contract.
Future input handling belongs to the existing Round-Trip Praxis; this publication
structure establishes no permanent `ingest/` directory or automatic ingest mechanism.

Copies under governed evidence demonstrate generation, testing, transport or
transition activity. They are evidence, not the normal operational location for
retrieving a released workbench projection. Archi-returned controls must never
replace the generated release projection or its maintained presentation template.

The existing `make -C enterprise-architecture/tooling continuum` publication
command writes both the Manifest Exchange and this durable release projection
from one validated generation. `check` verifies both against the canonical source.
For just the durable XML, from the matching release source tree:

```bash
python3 enterprise-architecture/tooling/architecture_continuum.py project --publish
python3 enterprise-architecture/tooling/architecture_continuum.py project --publish --check
```

The root release authority determines `<release>`; generation never increments
it or supplies acceptance. Preparing a candidate is distinct from publishing an
accepted release under the existing HCRL gates. Repeating identical publication
is safe; differing existing published bytes are rejected to preserve release
history. To reproduce a historical release, use its exact canonical sources,
navigation, template, release authority and compatible maintained tooling in an
isolated source tree (`--root <tree>` before `project`). Do not relabel today's
Continuum with a historical version. No historical backfill beyond 1.1.11 is implied.

```bash
python3 enterprise-architecture/tooling/continuum_workbench.py review --normalised \
  --baseline /path/to/exact-current-projection.xml \
  --submitted /path/to/complete-model-export.xml \
  --output /path/to/iteration/evidence/review.json
python3 enterprise-architecture/tooling/continuum_workbench.py apply --decision /path/to/reviewed-decision.json
```

Commands run from the repository root. The accepted `--normalised` path uses
existing canonical identity where established and discovers new architect-authored
elements without allocating missing canonical IDs. It reports a bounded semantic
and View/navigation delta without writing sources. The default review without
that flag remains the existing canonical-amendment intake. The baseline must
match the current projection byte for byte.
The [existing Round-Trip Praxis](../../governance/archimate-round-trip-engineering-praxis.md)
and common HCRL process govern adjudication; the JSON adapter does not replace them.

The existing element-amendment adapter consumes its schema-1 review; it rejects
the normalized review-only schema rather than inferring application authority.
Use the applicable governed incorporation/amendment operation after adjudication.
A decision for that existing adapter names `authority` (repository-relative HCRL path and DR anchor), `review`
(repository-relative review JSON), its exact `reviewSha256`, a `dispositions` map,
and `replacements`. Every reviewed element uses its canonicalId as disposition key;
relationships/Views use `relationship:<identifier>` / `view:<identifier>`. Each
value records `action` (`apply`, `reject`, `defer`) and an attributable `reason`.
Each applied replacement supplies complete `metadata` and `body`. Existing identity,
native type and specialisation must remain unchanged in this bounded adapter.
The review hash, complete disposition coverage, exact baseline and source preimages
are checked before prospective validation and writes. The adapter validates the
presence of recorded authority, not its authenticity; agents must read the actual
Solution Architect disposition and obey its scope.

New elements, retirements, splits, merges and retypings require their separately
reviewed identity/lineage work under the incorporation Praxis. Relationship or View
application requires reviewed amendment to the owning canonical element or maintained
presentation template, followed by projection and verification. Record deferred
items explicitly. This adapter rejects an unsupported automatic `apply` rather
than silently dropping part of a submission. Multi-file writes are preflighted,
but no filesystem transaction or concurrent-writer guarantee is claimed.

The [actual Archi transport evidence](../evidence/1.1.5/002/after/archimate-validation.json)
distinguishes canonical semantics from raw XML transport. The no-edit return
preserves canonical values/owners, native relationships and Views. Archi adds
language attributes and restructures organization folders; whole-model raw
fidelity is not claimed. Preserve the presentation source and exact returned
control; do not silently replace the template with that return. The full raw
comparison can be regenerated with `python3 enterprise-architecture/tooling/exchange_compare.py compare --baseline <exact-XML> --submitted <returned-XML-or-gz> --report <report.json>` using the
published XML and retained `returned-model.xml.gz` (decompress first).
