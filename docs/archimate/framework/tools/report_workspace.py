#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render the workspace-only release report from completed, hash-pinned checks."""

import synchronise_baseline as s
import verify_workspace as v


def render(report_path, guide_path):
    release = s.load_release()
    s.g.require(release == {'version': '1.0.8', 'previous_version': '1.0.7'}, 'Unexpected workspace release')
    evidence = s.read(s.metadata.PUBLISHING / '1.0.8/workspace-verification-1.0.8.json')
    s.g.require(evidence['architecture_release'] == release and evidence['status'] == 'PASS', 'Stale workspace evidence')
    required = {'publication_tests', 'identity_release_tests', 'architecture_tests',
                'historical_recovery', 'source_identity', 'ec_bindings', 'release',
                'baseline_107_reproduction', 'relocated_107_generation', 'preservation', 'migration_comparison'}
    s.g.require(required <= evidence['checks'].keys(), 'Missing required workspace checks')
    for name, check in evidence['checks'].items():
        s.g.require(check['status'] == 'PASS', 'Workspace check not passed: ' + name)
        s.g.require(s.r.sha(s.ROOT / check['log']) == check['log_sha256'], 'Changed workspace check log: ' + name)
    for path, digest in evidence['files_sha256'].items():
        s.g.require(s.r.sha(s.ROOT / path) == digest, 'Stale workspace fingerprint: ' + path)
    comparison = v.verify()
    s.g.require(comparison == evidence['comparison'], 'Stale architecture/presentation proof')
    outputs, b, validation = s.publish()
    s.validate(b, s.g.parse(s.metadata.model_path()))
    for path, content in outputs.items():
        if path != s.metadata.validation_path():
            s.g.require(path.read_bytes() == content, 'Stale generated output: ' + str(path))
    saved = s.read(s.metadata.validation_path())
    s.g.require(saved['output_sha256'] == comparison['after_sha256'] == validation['output_sha256'], 'Stale publication validation')
    s.g.require(all(x == 'PASS' for x in saved['checks'].values()), 'Publication validation incomplete')
    generated = sorted(str(p.relative_to(s.ROOT)) for p in [*outputs, report_path, guide_path])
    s.g.require(len(generated) == len(set(generated)) == 14, 'Generated-output set changed')
    rows = '\n'.join('| ' + name + ' | PASS | `' + c['log'] + '` |' for name, c in evidence['checks'].items())
    report = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Workspace publication — Architecture Release 1.0.8

The Solution Architect authorised the policy-defined 1.0.7 → 1.0.8 increment for
workspace structure and tooling. Semantic, identity, relationship and presentation
deltas are **NONE**. The exact XML byte difference is one model-level release value.
All 1,299 elements, governed names, Properties, 852 relationships, 14 EC bindings,
1,196 Views, 3,915 visual objects, 1,449 connections and 2,922 bendpoints are retained.
The comparison restores that single release value in memory and requires exact
document and byte equality. It excludes no architecture or presentation content
and changes no transport comparator or fidelity classification.

Release 1.0.7 XML SHA-256: `{comparison['before_sha256']}`.
Release 1.0.8 XML SHA-256: `{comparison['after_sha256']}`.

The immutable 1.0.7 real Archi 5.10 proof remains the empirical Canonical
Identity/Governed Naming acceptance evidence. No repeat Archi run is required
solely for relocation; no new empirical run or whole-document fidelity is claimed.
Its earlier preparation-time reports retain their original pending wording.
Current generated validation retains that historical preparation status field;
the completed empirical conclusion is in the preserved acceptance evidence.

| Check | Result | Evidence |
| --- | --- | --- |
{rows}

The complete generated set is 14 files, reproduced independently byte-for-byte by
`make baseline-independent`. That separate check compares the saved set to new
process output; a generated report cannot certify its own byte comparison.

{chr(10).join('- `' + path + '`' for path in generated)}

The [framework](../../../framework/README.md) operates on governed RADS resources
and projection metadata. Publications live in `exchange/publish`; incoming exports
belong in `exchange/ingest` and grant no automatic architectural authority.
Historical locators and evidence retain their original bytes. The location map
provides current navigation without rewriting historical meaning.

See the [migration report](../../../../../.junie/reports/2026-10-18-archimate-workspace-restructure.md),
[1.0.7 empirical acceptance](../../../../../.junie/reports/2026-10-18-archimate-canonical-identity-establishment.md)
and [inspection guide](archi-inspection-1.0.8.md).
Increment 2 has not begun. Release 1.0.8 is eligible as its starting baseline only
after successful migration validation and Solution Architect acceptance.
'''
    guide = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Solution Architect — Release 1.0.8 publication

Import `docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml`
using Open Exchange import in Archi 5.10.0 when needed.
SHA-256: `{comparison['after_sha256']}`.

This is the unchanged accepted 1.0.7 semantic and presentation architecture with
the model-level release value updated to 1.0.8. Existing navigation and all View
geometry, bendpoints and identities are preserved. No repeat no-edit Archi run is
required solely for the workspace move. The preserved 1.0.7 empirical evidence
establishes Canonical Identity and Governed Naming transport; characterised
whole-document transport transformations and fidelity limitations remain.

Use the [current framework commands](../../../framework/README.md) for diagnostics.
Future exports land in `docs/archimate/exchange/ingest/`. They are change input,
not replacement architectural authority. The Solution Architect must review and
reconcile proposed changes before their acceptance into the Harmonia RAD.
Increment 2 comparison/reconciliation and presentation replay remain future work.
'''
    return {report_path: report.encode(), guide_path: guide.encode()}
