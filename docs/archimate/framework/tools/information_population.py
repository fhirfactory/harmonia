#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Replay the bounded initial IA element population. Allocate no identities/edges.

This is a publication overlay, not IA ingestion or architectural reconciliation.
The retained resource capture and source assertions supply every semantic input.
"""
import collections
import copy
import gzip
import hashlib
import json
import json
from pathlib import Path
import sys

import generate_views as g
import governed_metadata as m
import reconcile_108 as accepted

SOURCE = m.RAD / 'information-architecture.json'
EVIDENCE = m.PUBLISHING / '1.0.10/information-population'
COMPLETION = EVIDENCE / 'canonical-name-completion'
NAMING_AUTHORITY = 'docs/markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary'
NAMING = ('Name == displayName; conceptual suffix BO, logical suffix Topic. Local friendlyName uses existing '
          'component lexical rule only; no IA ancestry is asserted. canonicalName applies governed BO/TOPIC '
          'qualifiers to the same local component under the Solution Architect adjudication.')
REPORT = EVIDENCE.parent / 'information-population.md'
GUIDE = EVIDENCE.parent / 'archi-inspection-guide.md'
PATHS = {'BusinessObject': ('Business', 'BusinessObjectMap'),
         'DataObject': ('Application', 'InformationMap')}
# Exact task-authorised navigation spelling only. Existing folder identities stay.
RENAMES = {
    ('Application', 'Information Map'): 'InformationMap',
    ('Views', '2-BusinessLayer', '1-Business Capabilities'): '1-BusinessCapabilities',
    ('Views', '2-BusinessLayer', '2-Business Enabling Capabilities'): '2-BusinessEnablingCapabilities',
    ('Views', '3-ApplicationLayer', '2-Enterprise Capabilities'): '2-EnterpriseCapabilities',
}
EMPTY_PATHS = [('Technology & Physical', 'DataMap'),
               ('Views', '2-BusinessLayer', '3-BusinessObjectMap'),
               ('Views', '4-TechnologyLayer', '1-DataMap')]
AUTHORITY_AMENDMENTS = frozenset('docs/markdown/governance/' + name for name in (
    'canonical-identity-and-naming.md', 'architecture-release-policy.md',
    'architecture-modelling-strategy.md', 'archimate-ingestion-publication-semantics.md',
    'rad-archimate-model-mapping.md', 'rad-archimate-mapping-catalogue.md'))


def active():
    from architecture_release import load_release, version_parts
    if SOURCE.exists():
        version = load_release()['version']
        g.require(version_parts(version) <= version_parts('1.0.10'),
                  'IA capture exists: explicitly review its carry-forward before a later publication')
        return version == '1.0.10'
    return False


def sha(data):
    return hashlib.sha256(data).hexdigest()


def preserved_authority(relative, expected):
    """Verify live adjudication separately from the unchanged original pin."""
    if relative not in AUTHORITY_AMENDMENTS:
        return False
    manifest = m.read(COMPLETION / 'manifest.json')
    row = manifest.get('governed_authority_amendments', {}).get(relative)
    if row is None:
        return False
    original = gzip.decompress((COMPLETION / 'before' / (Path(relative).name + '.gz')).read_bytes())
    g.require(sha(original) == row['original_sha256'] == expected and
              sha((m.ROOT / relative).read_bytes()) == row['current_sha256'],
              'IA adjudicated authority/history drift: ' + relative)
    return True


def names(row):
    """Existing local naming derivation with the adjudicated native vocabulary.

    Scope comes from the reviewed IA capture, never from inspecting folders.
    This function is not a general DataObject-to-Topic inference mechanism.
    """
    native = row['archimateType']
    g.require(native in PATHS, 'No authorised information qualifier for ' + native)
    resource = dict(type=native, name=row['displayName'])
    if native == 'DataObject':
        resource['information_map'] = True
    return m.qualified_names([resource])


def complete_names(original):
    """The only authorised source amendment; no allocation or semantic changes."""
    data = copy.deepcopy(original)
    data['naming'] = NAMING
    data['canonical_name_adjudication'] = NAMING_AUTHORITY
    for row in data['elements']:
        derived = names(row)
        g.require(row['friendlyName'] == derived['friendlyName'], 'Completion would rename friendlyName')
        row['canonicalName'] = derived['canonicalName']
        row['metadata_stops'] = {}
    return data


def load():
    data = m.read(SOURCE)
    history = EVIDENCE / 'allocation'
    manifest = m.read(history / 'manifest.json')
    g.require(sha((history / 'prior-release.json').read_bytes()) == manifest['prior_release_sha256'],
              'Prior release authority snapshot changed')
    original = gzip.decompress((history / 'information-architecture.json.gz').read_bytes())
    g.require(sha(original) == manifest['source_sha256'], 'IA allocation history changed')
    amendment = m.read(COMPLETION / 'manifest.json')
    g.require(amendment['authority'] == NAMING_AUTHORITY and
              amendment['original_source_sha256'] == sha(original) and
              amendment['completed_source_sha256'] == sha(SOURCE.read_bytes()), 'IA naming amendment history changed')
    g.require(gzip.decompress((COMPLETION / 'information-architecture.json.gz').read_bytes()) == SOURCE.read_bytes(),
              'Completed IA source snapshot changed')
    g.require(data == complete_names(json.loads(original)),
              'IA completion changed identity, population, meaning or non-canonical naming metadata')
    prior = m.ROOT / data['originating_model']['path']
    g.require(sha(prior.read_bytes()) == data['originating_model']['sha256'], 'RAD-1.0.9 baseline changed')
    g.require(data['architecture_release'] == '1.0.10' and data['originating_release'] == '1.0.9', 'Unreviewed IA release')
    import architecture_identity as identity
    import synchronise_baseline as s
    ids, used_names = set(), set()
    for row in data['elements']:
        native = row['archimateType']
        g.require(native in PATHS, 'Only native BusinessObject/DataObject allowed')
        value = row['canonicalId']
        g.require(m.UUID32.fullmatch(value) and value not in ids, 'Invalid/colliding IA canonical identity')
        ids.add(value)
        g.require(row['representationId'] == identity.semantic_id(value), 'IA representation binding changed')
        name = row['baseName'] + (' BO' if native == 'BusinessObject' else ' Topic')
        g.require(name == row['displayName'] and name not in used_names, 'IA name collision/drift; architect adjudication required')
        used_names.add(name)
        g.require(row['friendlyName'] == m.component(name), 'IA local lexical friendlyName drift')
        g.require(not row['metadata_stops'] and
                  all(row.get(k) == v for k, v in names(row).items()), 'IA canonical-name derivation drift')
        evidence = row['evidence']
        path = (m.ROOT / evidence['source']).resolve()
        g.require(path.is_relative_to((m.ROOT / 'docs/markdown').resolve()), 'IA evidence must be governed RADS')
        g.require(sha(path.read_bytes()) == evidence['source_sha256'], 'IA authoritative source drift: ' + evidence['source'])
        text = s.section(path, evidence['heading'], evidence['heading_level'])
        g.require(evidence['assertion'] in text and row['documentation'] == text, 'IA selected definition drift')
    g.require(collections.Counter(e['archimateType'] for e in data['elements']) == {'BusinessObject': 50, 'DataObject': 4},
              'Unreviewed IA population change')
    return data


def descriptors(data):
    return {e['representationId']: dict(record=e, expected=dict(displayName=e['displayName'], **names(e)),
                                      archimateType=e['archimateType'], harmoniaType=None)
            for e in data['elements']}


def folder(root, path):
    parent = root.find('a:organizations', g.N)
    for depth, label in enumerate(path):
        matches = [i for i in parent.findall('a:item', g.N) if not i.get('identifierRef') and
                   i.findtext('a:label', namespaces=g.N) == label]
        g.require(len(matches) <= 1, 'Ambiguous IA navigation')
        if matches:
            parent = matches[0]
        else:
            parent = g.child(parent, 'item', identifier=g.visual_id(root.get('identifier'), 'initial-ia-folder', *path[:depth+1]))
            g.child(parent, 'label', label, **{g.LANG: 'en'})
    return parent


def rename_navigation(root):
    def walk(parent, path=()):
        for item in parent.findall('a:item', g.N):
            if item.get('identifierRef'):
                continue
            label = item.find('a:label', g.N)
            original = (*path, label.text)
            if original in RENAMES:
                label.text = RENAMES[original]
            walk(item, original)
    walk(root.find('a:organizations', g.N))


def project(before, data):
    root = copy.deepcopy(before)
    existing = {e.get('identifier') for e in root.findall('a:elements/a:element', g.N)}
    old_names = {e.findtext('a:name', namespaces=g.N) for e in root.findall('a:elements/a:element', g.N)}
    old_ids = {v.text for v in root.findall("a:elements/a:element/a:properties/a:property[@propertyDefinitionRef='governed-canonical-id']/a:value", g.N)}
    rename_navigation(root)
    for row in data['elements']:
        identifier = row['representationId']
        g.require(identifier not in existing and row['displayName'] not in old_names and row['canonicalId'] not in old_ids,
                  'Existing/IA identity or model-wide name collision; architect adjudication required')
        element = g.child(root.find('a:elements', g.N), 'element', identifier=identifier, **{g.TYPE: row['archimateType']})
        g.child(element, 'name', row['displayName'], **{g.LANG: 'en'})
        # Verbatim selected governed section, including its bounded qualifications.
        text = row['documentation'] + '\n\nSource standing: ' + row['evidence']['standing']
        if row['baseName'] == 'TaskOutcome':
            import synchronise_baseline as s
            text += '\n\n' + s.section(m.ROOT / row['evidence']['source'], '## 5. Explicitly Unresolved Architecture', 2)
        g.child(element, 'documentation', text, **{g.LANG: 'en'})
        props = g.child(element, 'properties')
        values = {m.property_id(k): row[k] for k in (*m.FIELDS, 'archimateType')}
        values.update({'baseline-source-path': row['evidence']['source'],
                       'baseline-source-section': row['evidence']['heading'],
                       'baseline-source-sha256': row['evidence']['source_sha256']})
        for key, value in values.items():
            g.child(g.child(props, 'property', propertyDefinitionRef=key), 'value', value)
        parent = folder(root, PATHS[row['archimateType']])
        g.child(parent, 'item', identifier=g.visual_id(root.get('identifier'), 'initial-ia-reference', identifier), identifierRef=identifier)
    for path in EMPTY_PATHS:
        folder(root, path)
    root.find("a:properties/a:property[@propertyDefinitionRef='baseline-architecture-release']/a:value", g.N).text = data['architecture_release']
    return root


def validate(b, root):
    """Exact allowlist comparison plus schema, references and name/identity checks."""
    import synchronise_baseline as s
    data = load()
    g.load_schema(g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(root)
    g.validate_identifiers(root)
    expected = project(b.information_before, data)
    # Equality includes every existing element/property/relationship/View, visual
    # object and organisation identity; only the explicit projection may differ.
    g.require(g.signature(root) == g.signature(expected), 'IA projection changed protected architecture, metadata, Views or navigation')
    old = b.information_before
    all_ids = {e.get('identifier') for e in root.iter() if e.get('identifier')}
    for element in root.iter():
        for key in ('elementRef', 'relationshipRef', 'identifierRef', 'propertyDefinitionRef', 'source', 'target'):
            value = element.get(key)
            g.require(value is None or value in all_ids, 'Unresolved IA/model reference: ' + str(value))
    elements = root.findall('a:elements/a:element', g.N)
    information = [e for e in elements if e.get(g.TYPE) in ('BusinessObject', 'DataObject', 'Artifact')]
    names = [e.findtext('a:name', namespaces=g.N) for e in information]
    g.require(len(names) == len(set(names)), 'Model-wide information-object Name collision')
    new_ids = set(descriptors(data))
    edges = root.findall('a:relationships/a:relationship', g.N)
    g.require(not any(e.get('source') in new_ids or e.get('target') in new_ids for e in edges), 'New IA semantic relationship forbidden')
    for path in ('a:elements/a:element', 'a:relationships/a:relationship', 'a:views/a:diagrams/a:view'):
        after = {e.get('identifier'): e for e in root.findall(path, g.N)}
        for e in old.findall(path, g.N):
            g.require(g.signature(e) == g.signature(after[e.get('identifier')]), 'Existing semantic/View record changed')
    m.validate_publication(b, root)
    return s.counts(root)


def overlay(outputs, b, report):
    data = load()
    prior = g.parse(m.ROOT / data['originating_model']['path'])
    before = g.ET.fromstring(outputs[m.model_path()])
    # The upstream publisher must reproduce the complete originating architecture.
    before.find("a:properties/a:property[@propertyDefinitionRef='baseline-architecture-release']/a:value", g.N).text = '1.0.9'
    g.require(g.signature(before) == g.signature(prior), 'Upstream projection differs from pinned RAD-1.0.9')
    b.information_before = before
    b.information_index = descriptors(data)
    root = project(before, data)
    content = g.serialize(root)
    result = validate(b, g.ET.fromstring(content))
    b.information_population = data
    outputs, report = dict(outputs), copy.deepcopy(report)
    outputs[m.model_path()] = content
    report.update(after=result, output_sha256=sha(content),
        originating_model=data['originating_model'],
        information_population=dict(BusinessObject=50, DataObject=4, Artifact=0,
            semantic_relationships_created=0, existing_relationships_changed=0,
            views_created=0, existing_views_changed=0, canonicalName_stopped=0, canonicalName_completed=54,
            allocation='54 once-only identities persisted in governed resource capture; publication allocates zero',
            qualification='Local lexical friendlyName only; no IA semantic naming ancestry or Home asserted'))
    report['governed_metadata'] = m.validate_publication(b, root)
    report['checks'].update({k: 'PASS' for k in ('initial_ia_source_evidence', 'initial_ia_exact_preservation',
        'initial_ia_names_unique', 'initial_ia_no_semantic_relationships', 'initial_ia_no_artifacts')})
    report['roundtrip_status'] = 'See information-population/validation and roundtrip evidence; human inspection pending'
    report['metadata_limitations'] = {}
    outputs[m.validation_path()] = m.encoded(report)
    return outputs, b, report


def render():
    """Render source/evidence-backed handoff; never claim unexecuted validation."""
    import synchronise_baseline as s
    outputs, _, validation = s.publish()
    g.require(m.model_path().read_bytes() == outputs[m.model_path()], 'Stale IA publication')
    data = load()
    evidence_path = COMPLETION / 'verification.json'
    verification = m.read(evidence_path) if evidence_path.exists() else {'checks': {}}
    if evidence_path.exists():
        g.require(verification['published_sha256'] == validation['output_sha256'], 'Stale IA completion verification')
    checks = []
    for name, item in sorted(verification['checks'].items()):
        g.require(sha((m.ROOT / item['log']).read_bytes()) == item['log_sha256'], 'Changed IA verification log')
        checks.append('| ' + name + ' | ' + item['status'] + ' |')
    rows = '\n'.join('| ' + ' | '.join((row['archimateType'], row['displayName'], row['canonicalId'], row['canonicalName'],
        row['evidence']['source'] + ' — ' + row['evidence']['heading'])) + ' |' for row in data['elements'])
    report = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-1.0.10 — initial Information Architecture elements

Deterministic projection: **50 BusinessObjects, 4 DataObjects, 0 Artifacts**.
All 1,308 prior elements, 1,077 relationships and 1,198 Views retain exact content
and identities. No IA semantic relationships or diagram Views were created.
The originating RAD-1.0.9 model remains unchanged at SHA-256
`{data['originating_model']['sha256']}`.
Current model SHA-256: `{validation['output_sha256']}`.

The [complete task report](../../../../../.junie/reports/2026-10-19-initial-information-architecture-population.md)
records source authority, exclusions, collisions, decisions still required and
exact changed files. The [governed projection contract](../../../../markdown/governance/initial-information-architecture-projection.md)
records the bounded task decision. Source assertions, standing, source fingerprints,
and immutable resource-owned identities are persisted in
[the capture](../../../../markdown/governance/baseline/information-architecture.json).

The Solution Architect's BO/TOPIC vocabulary adjudication completes canonicalName
on all 54 IA resources using the unchanged local friendlyName components. No
Artifact/ARTEFACT qualifier is authorised. RAD-1.0.10 remains the same unsigned
candidate; the pre-adjudication source and evidence remain preserved in
[completion history](information-population/canonical-name-completion/manifest.json).
Folders supply no semantic ancestry.
Names carry BO/Topic suffixes, without cross-level equivalence. Source-specific
qualifications survive in Documentation; the Domain05 candidate baseline is not
promoted to final Solution Architecture. Human rendering review and IA ingestion,
reconciliation and architect-authored relationship round trips remain outstanding.

| Executed verification | Result |
| --- | --- |
{chr(10).join(checks) if checks else '| Evidence not yet recorded | PENDING |'}

| Native type | Governed Name | canonicalId | canonicalName | Source / selected section |
| --- | --- | --- | --- | --- |
{rows}
'''
    guide = '''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Initial IA element population — Archi handoff

Import `docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml`
into Archi 5.10 as a new model. Retain the exact originating package provenance.

Locate 50 elements at **Business → BusinessObjectMap** and 4 at
**Application → InformationMap**. Archi's native Business/Application/Technology & Physical
resource roots remain native; the separate Views tree follows:

```text
2-BusinessLayer
    1-BusinessCapabilities
    2-BusinessEnablingCapabilities
    3-BusinessObjectMap
3-ApplicationLayer
    1-InformationMap
    2-EnterpriseCapabilities
    3-ApplicationMap
    4-MiddlewareMap
4-TechnologyLayer
    1-DataMap
```

DataMap is empty. The new View folders are empty workbench locations; there are
no generated IA diagrams, nodes, connection lines or semantic grouping elements.
Read source qualifications in Documentation. Every new resource carries its
canonicalId, native type, displayName, unchanged local lexical friendlyName and
completed canonicalName using the governed BO/TOPIC qualifiers. No Artifact/ARTEFACT
qualifier is established. This completes the same unsigned RAD-1.0.10 candidate.

Review the full task report before authoring IA organisation, relationships and
Views. In particular both Endpoint BO interpretations remain unpopulated. Similar
names across levels imply no mapping. Existing Home applies only in its governed
Business scope. Subsequent architect-authored work must provide the evidence for
IA ingestion/reconciliation/round-trip validation; this handoff implements none.
'''
    return {REPORT: report.encode(), GUIDE: guide.encode()}
