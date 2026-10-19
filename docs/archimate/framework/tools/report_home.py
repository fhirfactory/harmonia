#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render Home migration evidence without inventing validation or acceptance."""
import collections
import posixpath

import synchronise_baseline as s


def render(report_path, guide_path):
    release = s.load_release()
    evidence = s.read(report_path.parent / 'home-verification.json')
    s.g.require(evidence['architecture_release'] == release and evidence['status'] == 'PASS',
                'Stale/incomplete Home verification')
    required = {'archimate_tests', 'architecture_tests', 'governance_tests', 'home_validation',
                'rads_validation', 'release'}
    s.g.require(required <= evidence['checks'].keys(), 'Required Home checks missing')
    for name, check in evidence['checks'].items():
        s.g.require(check['status'] == 'PASS' and s.r.sha(s.ROOT / check['log']) == check['log_sha256'],
                    'Home check failed or evidence changed: ' + name)
    for path, digest in evidence['files_sha256'].items():
        s.g.require(s.r.sha(s.ROOT / path) == digest, 'Stale Home evidence: ' + path)
    outputs, b, validation = s.publish()
    saved = s.read(s.metadata.validation_path())
    s.g.require(saved['output_sha256'] == validation['output_sha256'] == s.r.sha(s.metadata.model_path()),
                'Stale Home publication')
    s.g.require(all(value == 'PASS' for value in saved['checks'].values()), 'Home publication checks incomplete')
    for path, content in outputs.items():
        if path != s.metadata.validation_path():
            s.g.require(path.read_bytes() == content, 'Stale generated Home output: ' + str(path))
    run = s.ROOT / 'docs/archimate/processing/ingestion/1.0.8/run-002'
    detection = s.read(run / 'detection/explicit-homes.json')
    result = s.read(run / 'validation/validation-results.json')
    s.g.require(result['status'] == 'PASS' and result['resulting_model_sha256'] == saved['output_sha256'],
                'Home semantic validation is stale')
    records = b.business_homes['records']
    counts = collections.Counter(r['status'] for r in records)

    def relative(path):
        return posixpath.relpath(str(path), str(report_path.parent))

    def link(path, label):
        return '[' + label.replace('|', '\\|') + '](' + relative(path) + ')'

    def subject(row):
        e = b.records[row['source_key']]
        return link(s.RAD / (s.group(e) + '.md#' + s.anchor(e)), e['name'])

    authored = '\n'.join('| ' + row['source_element']['name'] + ' | ' + row['source_element']['native_type'] +
                         ' | ' + row['target_element']['name'] + ' | Capability / Feature | `' + row['identifier'] + '` |'
                         for row in detection['authored_homes'])
    normalised = collections.Counter(r['source_type'] for r in records if r['status'] == 'normalised')
    ambiguous, unresolved = [], []
    for row in records:
        if row['status'] == 'normalised':
            continue
        candidates = '; '.join(b.records[k]['name'] for k in row['candidate_feature_keys']) or 'Not established'
        target = ambiguous if row['status'] == 'ambiguous' else unresolved
        target.append('| ' + subject(row) + ' | `' + row['source_key'] + '` | ' +
                      row['record_state'] + ' | ' + candidates + ' |')
    checks = '\n'.join('| ' + name + ' | PASS | ' + str(c.get('tests', c.get('checks', ''))) +
                       ' | ' + link(s.ROOT / c['log'], 'log') + ' |'
                       for name, c in evidence['checks'].items())
    generated = sorted(str(p.relative_to(s.ROOT)) for p in [*outputs, report_path, guide_path])
    s.g.require(len(set(generated)) == len(generated) == 14, 'Generated Home output set changed')
    report = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Explicit Home and Business baseline normalisation — {release['version']}

Accepted Solution Architect modelling/governance change: **{release['previous_version']} → {release['version']}**.
{link(s.ROOT / 'docs/markdown/governance/architecture-modelling-strategy.md', 'Architecture Modelling Strategy')}
establishes native directed Association specialised as Home, independently of
Realization, structural containment, ownership, naming and navigation. Semantic
compatibility constrains all Harmonia ArchiMate specialisations. AX-04/14/17 apply.
No Information Architecture or Application Architecture ingestion occurred.

## Fresh Solution Architect-authored input — four relationships

The current 1.0.8 XML was freshly read: SHA-256 `{detection['input']['sha256']}`,
{detection['input']['bytes']:,} bytes. {link(run / 'detection/submission.xml.gz', 'Compressed exact input')}
and {link(run / 'detection/explicit-homes.json', 'complete endpoint/context evidence')}
preserve the authored assertions; the landing XML and native Archi file remain
byte-identical to task start. A distinct updated input has its own run-002 under
the existing run convention; run-001's original observations are not replaced.

| Authored source | Native source type | Authored target | Target type | Authored relationship ID |
| --- | --- | --- | --- | --- |
{authored}

Every row is an unnamed native **Association**, directed **source → target**, with
Archi specialisation **Home**. All targets are Features. The submitted Capability
context is Entity Information Enablement → Client Administration → Person Identity
Administration → Feature, preserving its independently governed standing.
The update adds exactly these four edges and four visual connections in two
existing Views; every previous submitted element and semantic edge is unchanged.
The architecture definition does not depend on XML. The current export's
`isDirected="true"` and relationship property `specialization = Home` demonstrate
interchange fidelity; the displayed property-definition name is `Specialization`.

## Existing-baseline normalisation — {counts['normalised']} additional assertions

All **{len(records)} existing Business behaviours** were assessed. The fixed
{link(s.home.REGISTER, 'migration register')} records exact canonical endpoint
identities, literal relationship IDs, semantic allocation statements, corroborating
source-key bindings and preserved standing. **{normalised['function']} Functions,
{normalised['service']} Services and {normalised['process']} Processes** now have
explicit Home. The full materialised list appears in
{link(s.RAD / 'relationships.md#explicit-business-home-assertions', 'baseline relationship assertions')}.

Evidence is an explicit governed Feature responsibility allocation in the Business
catalogue plus its independently reconciled binding. A Process requires its own
explicit Governed Process statement. A sole Realization, heading, folder, View,
name or graph traversal supplies no Home. Multi-Feature behaviour and proposed
bindings are withheld where a singular Home needs architectural judgement.
The register is the one-time accepted migration result. Publication replays its
literal rows and validates their fingerprints; it discovers no additional Home.

## Uncertainty retained for the Solution Architect

**{counts['unresolved']}** behaviours have insufficient current deterministic Home
evidence: {sum(r['status']=='unresolved' and r['record_state']=='current' for r in records)}
current records and {sum(r['status']=='unresolved' and r['record_state']!='current' for r in records)}
pending-adjudication records. **{counts['ambiguous']}** additional current behaviours
have competing plausible Feature contexts. Candidate contexts below are review
evidence, not asserted Homes. Direct Capability-scoped responsibility remains
valid; absence of a Home is not a newly introduced conformance failure.

### More than one plausible Home — {counts['ambiguous']}

| Business behaviour | Exact source key | Currentness | Candidate Feature contexts for adjudication |
| --- | --- | --- | --- |
{chr(10).join(ambiguous)}

### Home not deterministically established — {counts['unresolved']}

| Business behaviour | Exact source key | Currentness | Retained Feature evidence, if any |
| --- | --- | --- | --- |
{chr(10).join(unresolved)}

## Change Review consequences

CR-023, CR-025, CR-027 and CR-029 have **Outcome Resolved** and Outcome Reason
referencing the governing Home decision: **Realization does not establish Home
or logical context**. Each historical **Disposition remains On-Hold**. Revision 3
and all original detected evidence remain preserved. Original run-001 now has
10 Resolved and 26 Unresolved Outcomes; all 36 Dispositions remain On-Hold.

CR-022/024/026/028 remain independently Unresolved native Realization proposals;
none is rejected or replaced. CR-035/036 retain their original authored View
evidence and Unresolved Outcomes. Run-002 separately records four new Home edge
proposals and two updated View appearance proposals, all with initial/default
On-Hold Dispositions and Unresolved reconciliation Outcomes. The authored Home
intent is established; endpoint acceptance/identity and execution remain
separately governed. This task supplies no new endpoint identities or additional
Home for the new elements. {link(run.parent / 'run-001/change-review.md#explicit-home-decision', 'Original review outcome revision')};
{link(run / 'change-review.md', 'updated-input review')}.

## Preservation, interchange and validation

The normalised {link(s.metadata.model_path(), 'Open Exchange model')} retains
**all 1,299 existing elements, 852 native relationships and 1,196 Views**, with
resolved property names, definitions, identities and presentation unchanged.
Its only additional semantic edges are the 208 fixed Home assertions: **1,060
relationships** in total. Realization, Serving, Flow, Composition and all other
valid native edges remain intact. Source/target/direction/Association/Home survive
independent parse/serialization checks. Canonical IDs allocated: **zero**.
Original publications, source capture and authored 1.0.8 inputs remain unchanged.
No automatic naming, folder placement, View placement or Home inheritance is
introduced. Existing navigation is preserved independently of logical Home.

Model SHA-256: `{saved['output_sha256']}`.
{link(run / 'validation/validation-results.json', 'Independent semantic/audit validation')}
records {len(result['checks'])} passing checks, including original Dispositions,
detected evidence, all native relationships, no guessed Home and source hashes.

| Automated check | Result | Tests/checks | Evidence |
| --- | --- | ---: | --- |
{checks}

The initial framework run exposed an obsolete test fixture pinned to the current
publication path 1.0.8. The fixture now reads governed release authority while
preserving historical 1.0.8 proofs. Its initial failure log remains audit evidence;
the final complete suite passes. Full source validation retains **2,467 existing
unresolved architectural dimensions** without promoting or erasing them.

The standard publication gate reproduces all 14 generated outputs independently;
the saved comparison is evidence separate from this renderer. Generated reports
read recorded verification and refuse missing, failed or stale evidence.

## Release and stop boundary

The root release authority is **{release['version']}**, previous **{release['previous_version']}**.
This is an accepted modelling/governance and existing-baseline representation
change, not sign-off of all 1.0.8 proposals. Solution Architect final Archi import,
visual inspection and Business baseline sign-off remain outstanding. Home
cardinality/obligation, exhaustive types, inheritance, automatic naming/navigation
and Information/Application-specific rules remain deliberately undecided.
No downstream ingestion was begun. See {link(guide_path, 'inspection guide')}.
'''
    guide = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Solution Architect inspection — explicit Home, {release['version']}

Import {link(s.metadata.model_path(), 'the normalised existing Business baseline')}
into Archi 5.10.0 and inspect the 208 added relationship records. Confirm native
Association, directed source-to-Feature endpoints, Home specialisation and no
arbitrary relationship Name. Existing native relationships, names, folders and
Views should remain unchanged. This release introduces no new Home View layout.

The separately retained {link(run / 'detection/submission.xml.gz', 'updated 1.0.8 input')}
contains four Solution Architect-authored Home relationships and their four
connection appearances. Those exact authored assertions are preserved; this
normalisation does not merge its unaccepted endpoint additions into the baseline.
The {link(report_path, 'completion report')} lists 18 ambiguous and 93
unestablished cases for adjudication, exact evidence and affected Change Reviews.
No mandatory Home rule or automatic naming/navigation consequence is established.

Automated schema, reference, governance, identity, preservation and negative
conformance checks are recorded. No new GUI import or visual acceptance is
claimed. The Solution Architect retains final 1.0.8 Business sign-off authority.
Information Architecture and Application Architecture ingestion remain deferred.
'''
    return {report_path: report.encode(), guide_path: guide.encode()}
