#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render the accepted-instance publication report from recorded verification."""
import hashlib

import synchronise_baseline as s
import reconcile_108 as x


def render(report_path, guide_path):
    import view_presentation as vp
    if vp.SELECTION.exists():
        return vp.render(report_path, guide_path)
    import home_context as hc
    current_completion = hc.SOURCE.exists()
    evidence = s.read(hc.EVIDENCE / 'verification.json' if current_completion else
                      report_path.parent / 'reconciliation-verification.json')
    s.g.require(evidence['architecture_release'] == s.load_release(), 'Stale reconciliation release evidence')
    required = {'archimate_tests', 'governance_tests', 'architecture_tests', 'publication', 'identity',
                'identity_registry', 'rads', 'release', 'preservation', 'serialization_roundtrip', 'archi_transport'}
    s.g.require(required <= set(evidence['checks']), 'Missing reconciliation verification')
    for name, check in evidence['checks'].items():
        s.g.require(check['status'] == 'PASS' and
                    hashlib.sha256((s.ROOT / check['log']).read_bytes()).hexdigest() == check['log_sha256'],
                    'Incomplete or changed reconciliation verification: ' + name)
    outputs, b, result = s.publish()
    saved = s.read(s.metadata.validation_path())
    s.g.require(saved['output_sha256'] == result['output_sha256'] == s.r.sha(s.metadata.model_path()),
                'Stale reconciliation publication')
    if current_completion:
        s.g.require(evidence['published_sha256'] == saved['output_sha256'], 'Stale Home-completion verification')
    for path, content in outputs.items():
        if path != s.metadata.validation_path():
            s.g.require(path.read_bytes() == content, 'Stale reconciliation output: ' + str(path))
    capture = x.read()
    names = '\n'.join('| ' + ' | '.join((r['cr'], r['displayName'], r['canonicalId'],
                       b.home_context_index[r['representationId']]['record']['canonicalName'] if current_completion else r['canonicalName'])) + ' |'
                      for r in capture['elements'])
    checks = '\n'.join('| ' + name + ' | ' + c['status'] + ' | `' + c['log'] + '` |'
                       for name, c in sorted(evidence['checks'].items()))
    version = s.load_release()['version']
    report = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-{version} — authorised 1.0.8 reconciliation publication

All 26 accepted run-001 CRs and all six accepted run-002 CRs are reconciled.
Run-001: CR-001–015, CR-022/024/026/028 and CR-030–036. Run-002: CR-001–006.
**Accepted CRs not reconciled: none.** Per-CR results, required resulting states
and actual published-state signatures are recorded in
[run-001 execution results](../../ingestion/1.0.8/run-001/reconciliation/execution-001/results.json)
and [run-002 execution results](../../ingestion/1.0.8/run-002/reconciliation/execution-001/results.json).
The ten interpretation findings retain their historical On-Hold Dispositions and
Resolved Outcomes. The original inputs, detection, prior review revisions and
pre-execution adjudicated reviews remain preserved in the ingestion runs.
Execution revisions 5 and 2 link the later architect decisions and actual results
without rewriting earlier instructions. The preservation check covers 108 original
files and proves the ten complete interpretation records remain byte-identical.

The resulting model contains 1,308 elements, 1,077 relationships and 1,198 Views.
It retains all 1,299 existing identities and adds nine UUIDv4 identities, persisted
once in the governed accepted-ingestion source capture. Publication allocates none.
Six accepted CT3 → Feature Compositions retain native Composition; four native
Realizations coexist with four directed Associations specialised as Home.
The 208 fixed baseline Home assertions remain unchanged. The 93 unestablished and
18 ambiguous baseline Home assessments remain unresolved and non-blocking.

Only the four accepted authored Views change. Their exact submitted type,
membership, connection references, geometry and styles are preserved. Other Views,
existing relationships retain their prior semantic content. Home-derived naming
amends only the two qualified-name Properties of applicable existing elements;
their other semantic content remains unchanged.
Existing baseline Name/displayName conformance debt is preserved outside these CRs;
every new element's native Name equals its derived displayName.

The Product naming execution stop was resolved by the Solution Architect's
2026-10-19 vocabulary decision: native Product uses `PRODUCT`. The four Product
names below are derived by the shared lexical and qualification machinery.
No Product-specific naming algorithm or inferred hierarchy is introduced.
This was the sole execution-contract gap requiring a new architect decision;
it is resolved. The [execution-contract observation](../../ingestion/1.0.8/run-001/reconciliation/execution-001/execution-contract.md)
preserves the stop, decision, resumption and harness observations. No further
architectural/governance gap or matter requiring new Solution Architect adjudication
was identified. The outstanding Home cases remain modelling/adjudication debt.

| run-001 CR | displayName | canonicalId | canonicalName |
| --- | --- | --- | --- |
{names}

Governed Markdown RAD changes are the accepted element entries in
[Features](../../../../markdown/governance/baseline/features.md),
[Services](../../../../markdown/governance/baseline/services.md),
[Processes](../../../../markdown/governance/baseline/processes.md), the new
[Products catalogue](../../../../markdown/governance/baseline/products.md), and the
[17 accepted relationship records](../../../../markdown/governance/baseline/relationships.md#accepted-108-instance-relationships).
The source capture, correspondence manifest and baseline README connect these
derived catalogues to the accepted CRs. The naming contract records `PRODUCT`;
the release policy records the expressly authorised RAD number. No additional
architectural responsibility or hierarchy is introduced.

The subsequent Solution Architect Home-context decision completes this same unsigned
RAD-1.0.9 candidate. All {result['home_context']['applicable']} established explicit Homes are processed:
{result['home_context']['friendlyName_changes']} friendly names and
{result['home_context']['canonicalName_changes']} canonical names change;
{result['home_context']['element_relocations']} elements and
{result['home_context']['relationship_relocations']} source-based relationship references relocate.
The governed Home context supplies both names and principal navigation, using the
existing lexical rules and Feature folder convention. All 1,308 canonical identities,
responsibility semantics and every View remain unchanged by this completion.
The 93 unestablished and 18 ambiguous Home cases retain their prior names and locations.
The frozen captures and reconciliation audit are preserved; current resource fields
are in [Home-derived amendments](../../../../markdown/governance/baseline/home-derived-metadata.json).
See [completion evidence and all six representative results](home-context-completion/README.md)
and [complete population comparison](home-context-completion/population.json).

Validation passed {evidence['test_counts']['archimate']} ArchiMate tests,
{evidence['test_counts']['governance']} governance tests and
{evidence['test_counts']['architecture']} Java architecture tests. Publication
checks include identity, relationship types/directions, semantic endpoints,
View references/geometry, offline Open Exchange schema validation and preservation.

| Check | Result | Evidence |
| --- | --- | --- |
{checks}

The complete generated set contains 15 outputs: the original 14-output publication
set plus the accepted Product Markdown catalogue. Independent reproduction checks
every output, including this report and the guide, byte-for-byte. Reproduction
evidence is recorded separately to avoid a circular self-attestation.
See [independent reproduction evidence](home-context-completion/reproduction.json).

Open Exchange publication: [Harmonia-Architecture-{version}.xml](../../../exchange/publish/{version}/Harmonia-Architecture-{version}.xml).
SHA-256: `{saved['output_sha256']}`.
Resource source: `docs/markdown/governance/baseline/accepted-ingestion-1.0.8.json`.
RAD release {version} follows architecture baseline 1.0.8; the preserved governance/
tooling sequence through 1.0.14 does not determine this architecture number.

Real Archi 5.10 no-edit CLI import/export and schema validation completed.
All 1,308 identities and naming Properties, native element/relationship content,
212 Home assertions and the four accepted authored Views survive. Raw comparison
remains **DIFFERENCES**: its 1,669 semantic-text findings are exactly identical to
the established 1.0.7 transport evidence. Organisation IDs/order, default styles
and other raw transport findings remain visible; no equivalence rule was added.
This is not a claim of whole-model lossless fidelity.
Human rendering acceptance remains **PENDING INSPECTION**. No Information or
Application Architecture work was commenced.

The [inspection guide](archi-inspection-guide.md) and
[real Archi transport evidence](home-context-completion/roundtrip/archi-transport-verification.json) retain
that separate handoff gate. The recorded actual export and native Archi model,
raw comparator, CLI logs and read-only verification probe are in
`home-context-completion/roundtrip/`.
The mechanical reconciliation, publication and validation scope ends here.
The pre-completion candidate, original report and original transport evidence
remain preserved separately; their earlier results are not current validation.
'''
    guide = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi handoff — RAD-{version}

Import `docs/archimate/exchange/publish/{version}/Harmonia-Architecture-{version}.xml`
into Archi 5.10 as a fresh complete model. Expected SHA-256:
`{saved['output_sha256']}`.

Inspect the four accepted authored Views and their original geometry. Verify the
four Association «Home» edges coexist with the native Realizations. Check the new
Product metadata and the Identifier Capture CT3 Composition. Check unrelated Views
and existing identities remain intact. Inspect Identifier Create, Identifier Update,
Identifier Verification and Verify Identity Source under their explicit Home Features.
Inspect Maintain Clinical Privileges and Clinical Privilege Verification under their
Home context while confirming unchanged responsibility Properties. All 212 Home
sources use the same governed context for both names and resource navigation.
The 111 unresolved Home cases retain their existing locations. Native Products
retain their independently governed names and authored presentation.

The recorded real CLI no-edit round trip is in the accompanying transport evidence.
Human import/rendering acceptance is still pending. For a further human no-edit
probe, export the complete model including every View and folder
organisation, with English language `en` and validation enabled. Preserve the raw
export and record the actual operator/version/profile. Run the existing
`diagnose_roundtrip.py compare` on the published/exported pair, with diagnostic
output under `/tmp` or `.junie/reports`. Review all semantic, metadata, relationship,
View and presentation findings. Do not report an actual Archi round trip until
that evidence exists. Current human state: **PENDING INSPECTION**.
'''
    return {report_path: report.encode(), guide_path: guide.encode()}
