#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded RAD-1.1.5 ingestion of explicitly governed resource captures.

This is an initial migration adapter, not a prose classifier or routine publisher.
It correlates only exact canonicalId values and preserves source-edition evidence.
The accepted 1.1.0 projection supplies established representation bindings and
presentation; every element must independently exist in a governed source capture.
"""
from collections import defaultdict
import copy
import json
from pathlib import Path
import re

from lxml import etree as ET
import architecture_continuum as c

ROOT = c.ROOT
ENTRY = 'aadcd55252fa7965be8a4321832ecc6d5b97c6e0'
EVIDENCE = Path('enterprise-architecture/design/evidence/1.1.5/002')
TASK = str(EVIDENCE / 'before/ai-agent-tasks/20261021T092725Z-establish-and-apply-element-continuum.md')
AUTHORITY = 'enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#dr-2'
BASE = Path('docs/markdown/governance/baseline')
MODEL = Path('docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml')


def rows(node):
    return [{'ref': p.get('propertyDefinitionRef'), 'values': [{'text': v.text or '', 'attributes': dict(v.attrib)} for v in p.findall('a:value', c.N)]} for p in node.findall('a:properties/a:property', c.N)]


def section(source, heading, level):
    text = (ROOT / source).read_text()
    start = text.index(heading + '\n') + len(heading) + 1
    stop = re.search(r'(?m)^#{1,' + str(level) + r'} ', text[start:])
    body = text[start:start + stop.start()] if stop else text[start:]
    body = re.sub(r'\n<a id="[^"]+"></a>\s*$', '', body.rstrip())
    return body.strip().removesuffix('---').rstrip()


def provenance(path, selector, role, **kwargs):
    return {'source': str(path), 'selector': selector, 'role': role, 'revision': ENTRY,
            'sha256': c.sha((ROOT / path).read_bytes()), **kwargs}


def ingest():
    catalogue = c.read_json(ROOT / c.CONFIG / 'type-catalogue.json')
    c.require(not list((ROOT / c.CONTINUUM).rglob('*.md')), 'Initial ingestion refuses an already populated continuum')
    captures, byid = {}, defaultdict(list)
    for name in ('architecture', 'motivation', 'information-architecture', 'accepted-ingestion-1.0.8', 'accepted-ingestion-1.0.10'):
        path = BASE / (name + '.json')
        captures[name] = c.read_json(ROOT / path)
        for field in ('elements', 'historical_reference_elements'):
            for i, record in enumerate(captures[name].get(field, [])):
                byid[record['canonicalId']].append((name, field, i, record))
    model = c.secure_xml(ROOT / MODEL)
    defs = {p.get('identifier'): p.findtext('a:name', namespaces=c.N) for p in model.findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
    nodes = model.findall('a:elements/a:element', c.N)
    converted, boundary, source_map = {}, {}, defaultdict(list)
    for node in nodes:
        props = {defs[row['ref']]: row['values'][0]['text'] for row in rows(node)}
        cid = props.get('canonicalId')
        c.require(cid in byid, 'Projection element lacks independent canonical source: ' + str(cid))
        records = byid[cid]
        native = node.get(c.XSI)
        latest = records[-1][3]
        # Source precedence is the explicit accepted reconciliation, not file time.
        if records[-1][0] == 'accepted-ingestion-1.0.10':
            c.require(latest['archimateType'] == native and latest['representationId'] == node.get('identifier'), 'Accepted reconciliation/projection mismatch: ' + cid)
        original = records[0][3]
        currentness = original.get('record_state', 'current')
        semantic = original.get('architectural_status', original.get('status', original.get('evidence', {}).get('standing')))
        if semantic is None:
            semantic = 'Historical reference — RAD correspondence unresolved' if currentness == 'historical_reference' else 'Accepted bounded reconciliation'
        standing = {'semantic': semantic, 'currentness': currentness}
        for source_field in ('placement_status', 'structural_id_status', 'name_status', 'ancestry_status'):
            if original.get(source_field) is not None:
                standing[source_field] = original[source_field]
        if records[-1][0] == 'accepted-ingestion-1.0.10':
            standing['representationDecision'] = 'Accepted RAD-1.1.0 CR ' + latest['cr']
        prov, bindings = [], []
        for capture_name, field, i, record in records:
            capture_path = BASE / (capture_name + '.json')
            selector = '/' + field + '/' + str(i)
            prov.append(provenance(capture_path, selector, 'governed source resource', sourceKey=record.get('key', record.get('baseName'))))
            bindings.append({'source': str(capture_path), 'selector': selector})
            source_map[str(capture_path)].append(cid)
        prov.append(provenance(MODEL, 'element[@identifier="' + node.get('identifier') + '"]', 'accepted representation binding and presentation; not an additional architecture source'))
        doc = node.findtext('a:documentation', namespaces=c.N) or ''
        # Current Motivation sections are maintained definitions, with an exact
        # source heading/capture binding. Historical records remain captured text.
        if records[0][0] == 'motivation' and currentness == 'current':
            doc = section(original['source'], original['heading'], original['heading_level'])
            prov.append(provenance(original['source'], original['heading'], 'current governed definition'))
            source_map[original['source']].append(cid)
        evidence = original.get('evidence', {})
        if evidence.get('source'):
            prov.append(provenance(evidence['source'], evidence['heading'], 'source-qualified original definition', standing=evidence.get('standing', 'Retained as recorded')))
            source_map[evidence['source']].append(cid)
        for locator in original.get('sources', []):
            source_key = locator.get('source')
            source_spec = captures['architecture']['sources'].get(source_key)
            if source_spec:
                path = source_spec['path']
                prov.append({'source': path, 'selector': locator.get('anchor') or 'line ' + str(locator.get('line')), 'role': 'original source locator; recorded edition', 'revision': ENTRY, 'captureSource': str(BASE / 'architecture.json'), 'recordedSourceSha256': source_spec['sha256']})
                source_map[path].append(cid)
        if original.get('source') and str(original['source']).startswith('docs/') and records[0][0] == 'motivation' and currentness != 'current':
            prov.append({'source': original['source'], 'selector': original.get('heading') or original['key'], 'role': 'historical source correspondence', 'revision': ENTRY, 'recordedSourceSha256': original.get('source_sha256')})
        specialization = props.get('harmoniaType')
        if specialization == 'Topic':
            c.require(props.get('Specialization') == 'Topic', 'Topic requires explicit authored specialization')
        c.require(specialization in (None, 'Feature', 'Topic'), 'Unknown source specialization')
        m = {'schemaVersion': 1, 'canonicalId': cid, 'archimateType': native, 'specialization': specialization,
             **{name: props.get(name) for name in c.ROLES[1:]},
             'standing': standing,
             'identity': {'allocation': 'Preserved existing resource-owned UUID32; no allocation in migration', 'authority': str(BASE / 'release-1.1.0.json'), 'migrationAuthority': AUTHORITY},
             'naming': {'authority': 'docs/markdown/governance/canonical-identity-and-naming.md', 'mode': 'Preserved governed names; source-qualified context and unresolved ancestry retained', 'expected': {name: props.get(name) for name in c.ROLES[1:]}},
             'provenance': prov, 'sourceBindings': bindings,
             'exchange': {'identifier': node.get('identifier'), 'previousNativeName': node.findtext('a:name', namespaces=c.N), 'properties': rows(node)},
             'relationships': [], 'unresolvedProperties': {}}
        if original.get('unresolved') or original.get('adjudication_note'):
            m['sourceUnresolved'] = {'dimensions': original.get('unresolved', []), 'adjudication': original.get('adjudication_note')}
        if not doc:
            doc = 'The governed source has no established definition. This absence is retained for Solution Architect review; no definition is inferred from the name.'
            m['unresolvedProperties']['definition'] = 'Absent in the exact governed source and accepted representation.'
        body = '# ' + m['displayName'] + '\n\n## Architectural content\n\n' + doc.strip() + '\n'
        converted[node.get('identifier')] = (m, body)
        boundary[cid] = {'representationId': node.get('identifier'), 'archimateType': native, 'sourceCanonicalId': cid, 'standing': standing}
    c.require(set(boundary) == set(byid), 'Source/projection canonical population mismatch')
    for node in model.findall('a:relationships/a:relationship', c.N):
        source = node.get('source')
        c.require(source in converted, 'Relationship source is not a known canonical element')
        properties = rows(node)
        values = {defs[row['ref']]: row['values'][0]['text'] for row in properties}
        attributes = {k: v for k, v in node.attrib.items() if k not in ('identifier', 'source', 'target', c.XSI)}
        rel = {'identifier': node.get('identifier'), 'source': source, 'target': node.get('target'),
               'archimateType': node.get(c.XSI), 'attributes': attributes,
               'standing': {key: value for key, value in values.items() if any(word in key.lower() for word in ('status', 'state', 'standing'))} or {'source': 'Retained accepted model assertion; original qualifications apply'},
               'provenance': {'source': str(MODEL), 'revision': ENTRY, 'selector': 'relationship[@identifier="' + node.get('identifier') + '"]'},
               'properties': properties}
        if values.get('Specialization'):
            rel['specialization'] = values['Specialization']
        for local in ('name', 'documentation'):
            rel[local] = [{'text': x.text or '', 'attributes': dict(x.attrib)} for x in node.findall('a:' + local, c.N)]
        converted[source][0]['relationships'].append(rel)
    for m, body in converted.values():
        path = ROOT / c.element_path(m, catalogue)
        path.parent.mkdir(parents=True, exist_ok=True); path.write_text(c.encode(m, body))
    for parent in ('elements', 'relationships'):
        group = model.find('a:' + parent, c.N)
        for node in list(group): group.remove(node)
    definitions = model.find('a:propertyDefinitions', c.N)
    for key, name in [('continuum-source', 'Continuum / Canonical Source'), ('continuum-standing', 'Continuum / Semantic Standing'), ('continuum-currentness', 'Continuum / Currentness')]:
        d = ET.SubElement(definitions, c.tag('propertyDefinition'), identifier=key, type='string')
        ET.SubElement(d, c.tag('name')).text = name
    design = ROOT / c.DESIGN; design.mkdir(parents=True, exist_ok=True)
    (design / 'projection-template.xml').write_bytes(ET.tostring(model, encoding='UTF-8', xml_declaration=True, pretty_print=True))
    (ROOT / c.CONFIG / 'migration-boundary.json').write_bytes(c.encoded({'authority': AUTHORITY, 'entryRevision': ENTRY, 'identities': boundary}))
    after = ROOT / EVIDENCE / 'after'; after.mkdir(parents=True, exist_ok=True)
    (after / 'initial-source-correspondence.json').write_bytes(c.encoded({'entryRevision': ENTRY, 'sources': {source: sorted(set(ids)) for source, ids in sorted(source_map.items())}, 'sourceRecords': sum(len(v) for v in byid.values()), 'uniqueIdentities': len(byid), 'explicitEvolvedIdentityOverlays': 50, 'allocatedIdentities': 0}))
    print(json.dumps({'importedCanonicalElements': len(converted), 'preservedRelationships': sum(len(m['relationships']) for m, _ in converted.values()), 'allocatedIdentities': 0}))


if __name__ == '__main__':
    ingest()
