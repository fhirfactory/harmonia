#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render the final bounded 1.0.6 projection report and Archi inspection guide."""
import argparse
import collections
from pathlib import Path
import sys

import synchronise_baseline as s

REPORT = s.DEST / '2026-10-17-archimate-markdown-baseline-synchronisation.md'
GUIDE = s.DEST / 'archi-inspection-guide.md'


def validate_correction_preservation(b, root):
    s.validate_projection_preservation(root, b)
    previous = s.read(s.DEST / 'projection-start-preservation.json')
    documentation = s.read(s.DEST / 'corrections-preservation.json')
    s.g.require(documentation['status'] == 'PASS' and
                documentation['corrected_model_sha256'] == previous['starting_model_sha256'],
                'Earlier Documentation correction evidence changed')


def render():
    evidence = s.read(s.DEST / 'verification.json')
    validation = s.read(s.DEST / 'validation.json')
    outputs, b, fresh = s.publish()
    root = s.g.parse(s.DEST / 'Harmonia-Architecture-Baseline.xml')
    validate_correction_preservation(b, root)
    s.g.require(validation['output_sha256'] == fresh['output_sha256'] == s.r.sha(s.DEST / 'Harmonia-Architecture-Baseline.xml'), 'Stale generated model')
    s.g.require(evidence['architecture_release'] == validation['architecture_release'] == s.load_release(), 'Stale release evidence')
    s.g.require(evidence['status'] == 'PASS' and all(x['status'] == 'PASS' for x in evidence['checks'].values()), 'Required verification incomplete')
    for relative, expected in evidence['files_sha256'].items():
        s.g.require(s.r.sha(s.ROOT / relative) == expected, 'Stale baseline verification: ' + relative)
    for check in evidence['checks'].values():
        s.g.require(s.r.sha(s.DEST / check['log']) == check['log_sha256'], 'Verification log changed')
    generated = sorted(str(p.relative_to(s.ROOT)) for p in [*outputs, REPORT, GUIDE])
    s.g.require(len(generated) == 14 and len(set(generated)) == 14, 'Generated-output manifest changed; review report count')
    p = validation['human_inspection_projection']
    start = s.read(s.DEST / 'human-inspection-start-preservation.json')
    landscape_rows = '\n'.join('| ' + s.r.CATEGORIES[k] + ' | ' + ' | '.join(str(v.get(level, 0)) for level in ('CT1', 'CT2', 'CT3', 'Feature')) + ' |'
                               for k,v in p['landscapes_by_taxonomy_and_child_level'].items())
    totals = collections.Counter()
    for row in p['landscapes_by_taxonomy_and_child_level'].values():
        totals.update(row)
    shared = '\n'.join(f"- `{x['key']}` → " + ', '.join('`' + f + '`' for f in x['features']) for x in p['shared_behaviour_retained_at_capability'])
    retained = '\n'.join(f"- `{x['key']}` — `{' / '.join(x['home'])}`" for x in p['no_established_feature_association'])
    unresolved = '\n'.join(f"- Assertion {x['source_index']}: `{x['from']}` → `{x['to']}`; {x['status']}." for x in p['unresolved_feature_assertions'])
    checks = '\n'.join(f"| {name} | {x['status']} | {x['result']} | [{x['log']}]({x['log']}) |" for name,x in evidence['checks'].items())
    commands = '\n'.join('- `' + x['command'] + '`' for x in evidence['checks'].values())
    changed = '\n'.join('- `' + x + '`' for x in evidence['human_inspection_correction']['files_changed'])
    realisation_standing = collections.Counter()
    native = {x['identifier']: x for x in b.crosswalk['relationships']}
    for spec in b.crosswalk['views'].values():
        if spec['role'].startswith('assertion:'):
            edge = native[spec['relationships'][0]]
            realisation_standing[edge['record_state'] + ' / ' + edge['source_assertion']['status']] += 1
    report = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate / Markdown baseline synchronisation — 2026-10-17

## Final human-inspection correction — same uncommitted Release 1.0.6

Ready for Mark's final import and inspection in **Archi 5.10.0**. No commit,
release increment, semantic reconciliation or native GUI acceptance is claimed.
Import [Harmonia-Architecture-Baseline.xml](Harmonia-Architecture-Baseline.xml)
and follow the [inspection guide](archi-inspection-guide.md).

This pass started from the current accepted uncommitted candidate, SHA-256
`{start['starting_model_sha256']}`. The
[previous completion report](pre-human-inspection-synchronisation-report.md),
[previous verification](pre-human-inspection-verification.json), and earlier
Documentation and projection preservation proofs remain historical stage evidence.
Their 1,012-View preservation statements describe that earlier stage; the View
presentation changes and new landscapes below are explicitly authorised here.
No earlier baseline was used to replace the starting candidate.

## Authority and unchanged semantic architecture

The Markdown RAD remains the governed source for architecture, design, motivation,
implementation, decisions, governance, traceability and rationale. ArchiMate remains
the governed architectural projection and human workbench; Open Exchange XML is
interchange. Archi changes are inputs for reconciliation into Markdown RAD and/or
governed projection metadata. LaTeX remains downstream:
`Markdown RAD → LaTeX RAD → publication output`.

AX-04 keeps semantic architecture separate from navigation/layout machinery;
AX-14 preserves identity, type, standing and lifecycle distinctions; AX-17 forbids
invented ancestry or associations. The exact governed capture, metamodel,
reconciliation and crosswalk supply keys and assertions. Names, proximity,
implementation structure and chat supply no architectural authority.

| Object | Starting candidate | Final candidate | Result |
| --- | ---: | ---: | --- |
| Semantic elements | 1,299 | {validation['after']['elements']:,} | Exact IDs, Names, types, Documentation and Properties unchanged |
| Relationships | 852 | {validation['after']['relationships']} | Exact IDs, endpoints, standing, Documentation and Properties unchanged |
| Existing Views | 1,012 | 1,012 retained | All existing View IDs retained |
| New landscape Views | 0 | {p['new_landscape_views']} | Deterministic context/projection IDs |
| Total Views | 1,012 | {validation['after']['views']:,} | No existing View removed |
| Property definitions | 55 | 55 | Exact content unchanged |
| Persistent Architecture Identities allocated | 0 | 0 | All 14 adopted bindings unchanged |
| Current governed Features | 157 | 157 | All complete CT1 → CT2 → CT3 → Feature paths retained |

The [new starting proof](human-inspection-start-preservation.json) checks exact
semantic and relationship signatures against the actual starting candidate.
Unrelated Views remain exact. {p['reused_landscape_views']} existing immediate-child
Views change only their presentation Name to identify their landscape purpose.
{p['business_views_with_feature_layout']} existing Business Views receive generic
Feature layout and association endpoint completion. Their existing node/connection
IDs, styling, View Documentation and metadata remain protected. Added visual
appearances use deterministic IDs; changed connection endpoints still reference
the same semantic relationship endpoints. The validator limits these exceptions
to declared landscape Names and Business visual presentation.

Source current/pending/superseded populations remain 845 / 69 / 32; source
Canonical/Proposed/Candidate populations remain 716 / 227 / 3. The 462 metadata-only
relationship assertions, deferred relationships, 214 unproven historical 1.0.4
crosswalks and all unresolved identity states remain unchanged.

Documentation remains **396 entries / 675 current explanatory fields**, with
**212 fields across 98 entries historical**. Every semantic Documentation entry
is identical to the starting candidate. Current explanation stays before
`---` / `## History`; uncertain historical explanation stays below it. Structured
metadata remains Properties. The classification manifest and authority/publication
lifecycle are unchanged.

## Generator and projection changes

`feature_projection.py` supplies one exact assertion index shared by Business
resource navigation, Feature-specific View navigation and diagram layout.
`realises_feature`, `associated_feature` and `contributes_feature` endpoints are
used only when current and not unresolved, with current endpoint records.
Recorded Proposed relationships remain Proposed; their display is not acceptance.
The explicit Function `governed_process` path can place that Process in the
Function's Feature context. This is navigation through existing assertions,
**not** an invented direct Process→Feature semantic relationship. Participation,
Service use, common ownership and lexical similarity are not traversed.

**{p['features_with_behaviour_associations']} Features** have recorded Business
behaviour associations. Unique associations group **{p['grouped_by_feature']['function']}
Functions, {p['grouped_by_feature']['process']} Processes and
{p['grouped_by_feature']['service']} Services** beneath Feature resource contexts.
All **{p['realisation_views_in_feature_context']} existing Feature-specific
realisation Views** move beneath their exact Feature subjects; standing breakdown:
`{dict(sorted(realisation_standing.items()))}`. Historical subjects use qualified
historical folders. CT3/Capability-wide Behaviour Views retain their Capability
contexts. There is no CT3-level generic Feature Realisations bucket.

Business diagrams use a Capability context above Feature columns, with associated
behaviour beneath each Feature. Shared behaviour has multiple visual appearances
of its single semantic ID. A separate lower band holds unassociated behaviour.
Existing page populations are retained and completed with missing associated
endpoints so paging cannot hide an association. Native edges are drawn only from
existing governed native mappings; no Function→Process→Service chain is implied.
Coordinates, columns, absence of rectangle overlap and semantic connection
endpoints are mechanically checked. Crossing minimisation follows same-column
realisation routing; perceived readability and native rendering require Mark's
inspection. Person Identity Administration is the representative regression,
not a coordinate special case.

## Landscape coverage and ordering

There are **{sum(totals.values())} landscape Views**: **{p['reused_landscape_views']}
reused** and **{p['new_landscape_views']} new**. The child-level totals are
**{totals['CT1']} CT1 landscapes**, **{totals['CT2']} CT2 landscapes**,
**{totals['CT3']} CT3 landscapes** and **{totals['Feature']} Feature landscapes**.
These names describe the child population: a CT2 landscape belongs to CT1;
a CT3 landscape belongs to CT2. Counts include independent Strategy and Business
projections where applicable; Enterprise has Strategy landscapes only.

| Taxonomy | Root → CT1 | CT1 → CT2 | CT2 → CT3 | CT3 → Feature |
| --- | ---: | ---: | ---: | ---: |
{landscape_rows}

BC/BEC rows each include both Strategy and Business Views. Source context counts
are half those row counts. Enterprise is the already represented independent
catalogue, with no Application architecture expansion. BC contains **zero Features**;
there is no BC Feature landscape fixture to manufacture. Natural terminal
contexts retain their existing anchor/context Views. Each landscape contains only
the context (where there is a semantic context) and its **immediate** governed
children. Root landscapes contain CT1 only. No generic Landscapes folder exists.
A landscape is first among serialized View references in its context. Its View
display Name has a local `0-` prefix: installed Archi 5.10.0 comparator bytecode
confirms that tree Views sort by display Name independently of XML item order.
Validation also checks name ordering. This View-only prefix does not change
semantic element Names or resource numbering. Native GUI rendering remains a
human check. See the recorded comparator inspection log.

View-tree numbering remains `0-Motivation`, `1-Strategy`, `2-BusinessLayer`,
`3-ApplicationLayer`, `4-TechnologyLayer`, `5-Implementation`. Native resource roots
remain unnumbered, with human-readable navigation labels. Redundant resource
constructs such as `Strategy / 1-Strategy`, `Motivation / 0-Motivation` and
`Business / 2-BusinessLayer` remain absent. Semantic Names remain unprefixed.

## Explicit exceptions and uncertainty

The governed single canonical resource-home convention cannot assign the following
**{len(p['shared_behaviour_retained_at_capability'])} shared elements** to every
Feature without multiple resource homes or an arbitrary preferred association.
Their affected resource relocation is deliberately stopped: existing Capability
homes remain. No relationship is discarded. Their multiple Feature contexts are
exposed by realisation Views where native mappings exist and by repeated visual
appearances in Business diagrams. This is a navigation constraint, not a claim
that a semantic association is unresolved. No preferred Feature was inferred.

{shared}

The following **{len(p['no_established_feature_association'])} current Business
elements** have no usable recorded Feature association and retain the finest
established Capability resource context (which can be broader than CT3):

{retained}

There are **{len(p['unresolved_feature_assertions'])} unresolved current Feature
assertions**. They remain metadata/findings and are excluded from Feature grouping;
no replacement association is inferred:

{unresolved}

No current Feature ancestry is unresolved: all 157 recorded paths remain complete.
Pending and superseded records, unresolved identity and definition cases remain
available for inspection. This task makes no architectural adjudication.

## Workbench, schema and recovery

**Archi 5.10.0** remains the supported human workbench. The existing namespace,
schemaLocation and all bundled schema bytes are unchanged. The previous pass
proved all five installed Archi 5.10.0 schemas byte-identical to the repository
schemas. This pass validates against those schemas. The earlier native CLI import
could not initialise GTK against the available display; it was not retried
unchanged and no successful native GUI import is claimed. Mark performs final
import, rendering, ordering, navigation and layout acceptance.

Immutable 1.0.5A recovery replay passes byte-for-byte, including model hash
`f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1`.
Its complete file boundary, historical reports and release fixture are protected.
Root Architecture Release remains **1.0.6**, previous **1.0.5**; generation reads it
and never increments it. No commit has been made.

## Verification and generated-output set

The final generated-output set contains **14 files**. Earlier references to
12 counted only core generator outputs; 14 includes the completion report and
inspection guide. New landscapes are inside XML and add no output files.
Preservation proofs, verification logs and frozen prior reports are audit inputs
or evidence, not generator outputs. Same-process regeneration checks core
generator determinism. Independent processes reproduce and compare the complete
14-file output set byte-for-byte, including the report and guide.

{chr(10).join('- `' + x + '`' for x in generated)}

| Check | Result | Evidence | Log |
| --- | --- | --- | --- |
{checks}

The publication gate includes XSD, exact crosswalk/identity continuity, capability
ancestry, independent BC/BEC Feature reachability, Feature-centred resources and
Views, landscapes, layout, Documentation publication and rejection regressions.
Exact commands:

{commands}

## Files changed in this bounded correction

{changed}

Generated catalogues that reproduce unchanged remain in the output manifest above.
The earlier uncommitted candidate's other modifications are retained; no rollback
or restart from a prior release occurred.

## Remaining human acceptance

Mark should complete every point in the [Archi 5.10.0 guide](archi-inspection-guide.md),
including immediate-child landscapes and ordering, Person Identity Feature resource
and View contexts, its CT3-wide Behaviour View and visual grouping, shared-resource
exceptions, terminal contexts, numbering, Documentation and visible pending cases.
The 13 shared-resource exceptions and 20 unresolved assertions are explicit review
items; this task does not resolve them through preferred ownership or new edges.

Final model SHA-256: `{validation['output_sha256']}`.
'''
    guide = '''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi 5.10.0 final human inspection — Release 1.0.6

Mark performs final Open Exchange import and human acceptance in **Archi 5.10.0**.
Import `Harmonia-Architecture-Baseline.xml` as a new model. Automated checks prove
Exchange/XSD structure, exact source correspondence, serialized order and layout
invariants; they do not prove native GUI behaviour. No successful GUI import is
claimed. The prior native CLI attempt failed to initialise GTK.

## Landscapes and natural drill-down

Use **Views / 1-Strategy / Capabilities** and repeat the BC/BEC checks in
**Views / 2-BusinessLayer**. Landscape must be the first **View** inside its
own context; classified subfolders may appear before View entries in Archi.
Landscape display Names carry a local **0-** prefix to sort first in Archi;
the descriptive Names below omit that prefix for readability. Both serialized
order and name order are validated. Confirm native ordering. No separate
Landscapes folder should appear.

1. Open **1-Business Capabilities / Business Capabilities — CT1 Landscape**.
   It should show all 18 governed CT1 Business Capabilities, no CT2/CT3/Features.
2. Drill into **[CT1] Care Access & Coordination** and open its **CT2 Landscape**.
   Show that context and exactly Care Access Management, Care Scheduling & Capacity
   Management, Care Coordination & Transition, Care Support Logistics Management
   and Service Delivery Preparedness Management. No grandchildren belong here.
3. Open **[CT2] Care Access Management / Care Access Management — CT3 Landscape**.
   Show the context and exactly Care Intake Management, Care Referral Management
   and Care Eligibility Determination.
4. Open **2-Business Enabling Capabilities / Business Enabling Capabilities — CT1
   Landscape**. It should show all 7 current CT1 BECs, no deeper descendants.
5. Drill into **[CT1] Entity Information Enablement / Entity Information Enablement
   — CT2 Landscape**. Its seven direct CT2 children include Client Administration
   and Provider Administration. Then open **[CT2] Client Administration / Client
   Administration — CT3 Landscape**. Its four direct children are Person Identity
   Administration, Healthcare Subject Profile Administration, Client Relationship
   Administration and Client Privacy Guardrail Management.
6. Open **[CT3] Person Identity Administration / Person Identity Administration —
   Feature Landscape** in both Strategy and Business View projections. Its first
   View shows the CT3 and exactly five Features: Cross-Authority Identifier
   Correlation; Governed Identity Correction; Identifier Resolution; Identity Alias
   Association; Identity Source & Correction Lineage. No Business behaviour is
   included in this immediate decomposition landscape.
7. Inspect a natural terminal: **1-Business Capabilities / [CT1] Individual Care
   Delivery / [CT2] Individual Health Assessment / [CT3] Health History Assessment**.
   Retain its intentional Overview anchor. BC has no governed Features; no BC
   Feature example or invented Feature landscape should appear.
8. The existing Enterprise catalogue under Strategy follows the same direct-child
   rule. Application coverage boundary Views still express non-expansion.

## Person Identity Administration — three consistent projections

Resource path: **Business / Business Enabling Capabilities / [CT1] Entity Information
Enablement / [CT2] Client Administration / [CT3] Person Identity Administration**.

| Feature context | Functions | Processes | Services |
| --- | --- | --- | --- |
| Cross-Authority Identifier Correlation | Correlate Person Identifiers | — | Person Identifier Correlation |
| Governed Identity Correction | Apply Governed Person Identity Correction | Governed Person Identity Correction Process | Person Identity Correction |
| Identifier Resolution | Resolve Person Identifier | — | Person Identifier Resolution |
| Identity Alias Association | Maintain Person Identity Aliases | — | — |
| Identity Source & Correction Lineage | No established association | — | — |

These allocations come from exact assertions. The Process navigation path follows
its explicit Function→governed_process record; no direct Process→Feature semantic
relationship was added. Empty association contexts remain meaningful.

1. Expand each classified Feature folder in the Business resources and inspect
   its Functions / Processes / Services. Check the table above. The semantic
   Names remain unprefixed; classification appears in navigation folders.
2. At **Views / 2-BusinessLayer / 2-Business Enabling Capabilities**, follow the
   same classified branch. Feature-specific realisation Views belong inside their
   exact Feature folders. For example, the Resolve Person Identifier realisation
   View belongs inside **[Feature] Identifier Resolution**. No generic CT3-level
   Feature Realisations collection should remain.
3. **Person Identity Administration — Behaviour 1** remains directly in the CT3
   context. It must not be placed beneath one Feature.
4. Open that Behaviour View. The CT3 is above Feature columns; each Feature's
   associated behaviour is beneath it. Inspect the correction Function, Process
   and Service together. Confirm readable spacing, labels, actual relationship
   endpoints and reduced crossing. Layout does not assert a mandatory chain.
5. Compare the same Feature-bearing branch in Strategy: it retains complete
   CT1→CT2→CT3→Feature framing and its Feature Landscape; Business behaviour remains
   in Business projections. Candidate and Proposed standing remains in Properties.

## Shared, terminal and uncertain cases

Shared behaviour retains its recorded Capability resource home because the
single-home convention supplies no preferred Feature. For example, **Manage Theatre
Case Progression** relates to two Features; its Process likewise has contextual
paths through both. Confirm multiple visual appearances reference the same
semantic ID, and realisation Views sit under their exact Features. Review the 13
listed shared-resource cases in the completion report. No arbitrary resource home
under one Feature should be selected.

Inspect **Health Information Communication** under the Business Enabling pending
population and read its adjudication note/currentness in Properties. Also inspect
an unresolved Feature assertion from the report: it remains metadata/findings,
not a silently created relationship. Historical 1.0.4 reference population and
unresolved identity crosswalks remain separate. Do not treat display as approval.

## Numbering, Documentation and authority

- The View root sequence remains **0-Motivation, 1-Strategy, 2-BusinessLayer,
  3-ApplicationLayer, 4-TechnologyLayer, 5-Implementation**.
- Native resource roots contain no redundant `Strategy / 1-Strategy`,
  `Motivation / 0-Motivation` or `Business / 2-BusinessLayer` construction.
  Resource labels are human-readable and unnumbered.
- Inspect **Care Access Management** Documentation: definition and current
  explanation, including Intent/Boundary where available, appear before History.
  Inspect pending **Health Information Communication** for retained uncertain
  predecessor material below `---` / `## History`. Structured metadata stays in
  Properties. The 396-entry / 675-field current classification and 212 historical
  fields across 98 entries are unchanged.
- Markdown RAD remains source authority; Archi edits are reconciliation inputs;
  LaTeX is downstream publication. Existing Exchange schemas remain unchanged.
- Confirm release Properties say **1.0.6**. Nothing has been committed. Record
  human acceptance or remaining projection findings after these checks.
'''
    return {REPORT: report.encode(), GUIDE: guide.encode()}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--check', action='store_true')
    parser.add_argument('--output-directory', type=Path)
    args = parser.parse_args()
    try:
        for path, content in render().items():
            if args.output_directory:
                dest = args.output_directory.resolve()
                s.g.require(dest.is_relative_to(Path('/tmp')), 'Independent report output must be under /tmp')
                path = dest / path.relative_to(s.ROOT)
            if args.check:
                s.g.require(path.read_bytes() == content, 'Stale baseline report/guide: ' + path.name)
            else:
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_bytes(content)
        print('PASS: final baseline completion report and Archi 5.10.0 inspection guide')
        return 0
    except (OSError, ValueError, KeyError, s.g.ET.LxmlError) as exc:
        print('INVALID: ' + str(exc), file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
