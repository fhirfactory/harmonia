#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Author, validate and project canonical Markdown architectural elements.

The continuum is the maintained source. Publication never allocates identity,
classifies prose, chooses standing, or reads a historical processing workspace.
JSON front matter is a strict, portable subset of YAML. The Markdown body owns
the architectural explanation; exchange properties retain qualified source facts.
"""
from __future__ import annotations

import argparse
from collections import Counter
import copy
import hashlib
import json
from pathlib import Path
import re
import sys
import uuid
from jsonschema import Draft202012Validator

from lxml import etree as ET

ROOT = Path(__file__).resolve().parents[2]
CONTINUUM = Path('enterprise-architecture/continuum')
CONFIG = Path('enterprise-architecture/governance/continuum')
TOOLING = Path('enterprise-architecture/tooling')
SCHEMAS = TOOLING / 'schemas'
DESIGN = Path('enterprise-architecture/design/archimate')
DOCUMENTATION = Path('enterprise-architecture/documentation')
PUBLICATION = DOCUMENTATION / 'continuum-manifest/published'
RAD_PUBLICATION = DOCUMENTATION / 'continuum-rad/published'
MANIFEST = CONTINUUM / 'MANIFEST.md'
NS = 'http://www.opengroup.org/xsd/archimate/3.0/'
N = {'a': NS}
XSI = '{http://www.w3.org/2001/XMLSchema-instance}type'
LANG = '{http://www.w3.org/XML/1998/namespace}lang'
UUID32 = re.compile(r'[0-9a-f]{32}\Z')
ROLES = ('canonicalId', 'displayName', 'friendlyName', 'canonicalName')
SEMANTIC_NAMESPACE = uuid.uuid5(uuid.NAMESPACE_URL, 'urn:harmonia:architecture:semantic:v1')


class ContinuumError(ValueError):
    pass


def require(condition, message):
    if not condition:
        raise ContinuumError(message)


def unique(pairs):
    result = {}
    for key, value in pairs:
        require(key not in result, 'Duplicate JSON key: ' + key)
        result[key] = value
    return result


def read_json(path):
    return json.loads(Path(path).read_text(), object_pairs_hook=unique)


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, indent=2) + '\n').encode()


def sha(content):
    return hashlib.sha256(content).hexdigest()


def tag(local):
    return '{' + NS + '}' + local


def secure_xml(path):
    tree = ET.parse(str(path), ET.XMLParser(resolve_entities=False, no_network=True))
    require(not tree.docinfo.doctype, 'DTD is forbidden: ' + str(path))
    return tree.getroot()


def decode(content):
    require(content.startswith('---\n'), 'Missing JSON front matter')
    parts = content.split('\n---\n', 1)
    require(len(parts) == 2, 'Unterminated front matter')
    metadata = json.loads(parts[0][4:], object_pairs_hook=unique)
    require(isinstance(metadata, dict), 'Metadata must be an object')
    body = parts[1].strip() + '\n'
    return metadata, body


def encode(metadata, body):
    return '---\n' + json.dumps(metadata, ensure_ascii=False, indent=2) + '\n---\n\n' + body.strip() + '\n'


def element_path(metadata, catalogue):
    native = metadata.get('archimateType')
    require(native in catalogue['types'], 'Unknown native ArchiMate type: ' + str(native))
    specialization = metadata.get('specialization')
    if specialization:
        require(specialization in catalogue['specializations'], 'Ungoverned specialization: ' + str(specialization))
        profile = catalogue['specializations'][specialization]
        require(profile['nativeType'] == native, 'Specialization incompatible with native type')
    else:
        profile = catalogue['types'][native]
    cid = metadata.get('canonicalId', '')
    require(isinstance(cid, str) and UUID32.fullmatch(cid), 'canonicalId must be one UUID32 value')
    return CONTINUUM / profile['directory'] / (cid + '.md')


def load(root=ROOT):
    catalogue = read_json(root / CONFIG / 'type-catalogue.json')
    result = {}
    for path in sorted((root / CONTINUUM).rglob('*.md')):
        # Publications live outside canonical state. Only the exact generated
        # navigation manifest is exempt; every other Markdown must be an element.
        if path == root / MANIFEST:
            continue
        metadata, body = decode(path.read_text())
        expected = element_path(metadata, catalogue)
        require(path.relative_to(root) == expected, 'Incorrect element location: ' + str(path.relative_to(root)) + '; expected ' + str(expected))
        cid = metadata['canonicalId']
        require(cid not in result, 'Duplicate canonical identity: ' + cid)
        result[cid] = (metadata, body, path)
    return catalogue, result


def validate(root=ROOT, *, previous=None, pending=None):
    catalogue, elements = load(root)
    if pending:
        for metadata, body, path in pending:
            require(metadata['canonicalId'] not in elements, 'Duplicate pending identity')
            require(path == root / element_path(metadata, catalogue), 'Incorrect pending location')
            elements[metadata['canonicalId']] = (metadata, body, path)
    require(elements, 'The continuum is empty')
    schema = Draft202012Validator(read_json(root / CONFIG / 'element.schema.json'))
    native_schema = secure_xml(root / catalogue['nativeMetamodel'])
    xsd = {'xs': 'http://www.w3.org/2001/XMLSchema'}
    native_types = {n.get('name') for n in native_schema.xpath("xs:complexType[xs:complexContent/xs:extension/@base='RealElementType' or xs:complexContent/xs:extension/@base='CompositeType']", namespaces=xsd)}
    require(set(catalogue['types']) == native_types, 'Native type catalogue/schema drift')
    native_relationships = {n.get('name') for n in native_schema.xpath("xs:complexType[xs:complexContent/xs:extension/@base='RelationshipType']", namespaces=xsd)}
    applicability = read_json(root / CONFIG / 'native-relationships.json')
    require(set(applicability['keys']) == native_relationships, 'Native relationship applicability/schema drift')
    representations, bindings, relationships, qualified = {}, {}, {}, {}
    unresolved = []
    for cid, (m, body, path) in elements.items():
        problems = sorted(schema.iter_errors(m), key=lambda e: str(list(e.path)))
        require(not problems, 'Schema violation ' + cid + ': ' + '; '.join(e.message for e in problems[:3]))
        require(m.get('schemaVersion') == 1, 'Unsupported representation schema: ' + cid)
        require(m.get('displayName') and isinstance(m['displayName'], str), 'Missing displayName: ' + cid)
        require(body.startswith('# ' + m['displayName'] + '\n'), 'Heading/displayName mismatch: ' + cid)
        require('## Architectural content\n' in body, 'Missing architectural content section: ' + cid)
        content = body.split('## Architectural content\n', 1)[1].strip()
        require(content and not content.startswith('<definition,'), 'Unpopulated architectural content: ' + cid)
        require(m.get('standing', {}).get('semantic') and m['standing'].get('currentness'), 'Missing independent standing/currentness: ' + cid)
        require(m.get('provenance') and m.get('identity', {}).get('authority'), 'Missing provenance or identity authority: ' + cid)
        require(m.get('naming', {}).get('authority'), 'Missing naming authority: ' + cid)
        require(not any('<' in m[field] for field in ('displayName',)), 'Unpopulated template: ' + cid)
        require(not m['standing']['semantic'].startswith('<') and not m['identity']['authority'].startswith('<'), 'Unpopulated authority/standing: ' + cid)
        for role in ('friendlyName', 'canonicalName'):
            value = m.get(role)
            require(isinstance(value, str) and bool(value) or (value is None and role in m.get('unresolvedProperties', {})), 'Missing/unqualified ' + role + ': ' + cid)
            if value:
                require(not any(c.isspace() for c in value), 'Whitespace in ' + role + ': ' + cid)
        if m.get('naming', {}).get('expected'):
            for role, value in m['naming']['expected'].items():
                require(m.get(role) == value, 'Persisted naming mismatch ' + role + ': ' + cid)
        if m['naming'].get('mode', '').startswith('Local lexical component'):
            from governed_naming import component
            label = re.sub(r'^\[BusinessObject\] ', '', m['displayName'])
            require(m['friendlyName'] == component(label), 'Local lexical derivation mismatch: ' + cid)
        if m['naming'].get('inputs'):
            from governed_naming import qualified_names, component
            inputs = m['naming']['inputs']
            motivation = {'Stakeholder','Driver','Assessment','Goal','Outcome','Principle','Requirement','Constraint','Meaning','Value'}
            if m['archimateType'] in motivation:
                require(len(inputs) == 1 and inputs[0]['type'] == m['archimateType'], 'Unestablished Motivation naming context: ' + cid)
                local = inputs[0].get('nameComponent')
                if local:
                    require(inputs[0].get('nameComponentAuthority') and re.fullmatch(r'[A-Z][A-Za-z0-9]*', local), 'Unqualified spelling decision: ' + cid)
                else:
                    local = component(inputs[0]['name'])
                names = {'friendlyName': local, 'canonicalName': 'MOT-' + local}
            else:
                names = qualified_names(inputs)
            for role, value in names.items():
                require(m[role] == value, 'Governed naming derivation mismatch ' + role + ': ' + cid)
        specialization = m.get('specialization')
        if specialization:
            prefix = catalogue['specializations'][specialization]['displayPrefix']
            require(m['displayName'].startswith(prefix + ' '), 'Specialization/displayName mismatch: ' + cid)
        for source in m['provenance']:
            require(isinstance(source, dict) and source.get('source') and source.get('selector') and source.get('role'), 'Incomplete source provenance: ' + cid)
            require(source.get('revision') or source.get('retainedAt') or source.get('maintained'), 'Unrecoverable provenance: ' + cid)
            if source.get('retainedAt'):
                retained = Path(source['retainedAt'])
                require(not retained.is_absolute() and '..' not in retained.parts, 'Retained evidence must be repository-relative: ' + cid)
                require((root / source['retainedAt']).exists(), 'Missing retained evidence: ' + source['retainedAt'])
        for binding in m.get('sourceBindings', []):
            key = (binding['source'], binding['selector'])
            require(key not in bindings or bindings[key] == cid, 'Source binding maps to multiple identities: ' + str(key))
            bindings[key] = cid
        rid = m.get('exchange', {}).get('identifier')
        require(rid and rid not in representations, 'Missing/duplicate Exchange identity: ' + cid)
        representations[rid] = cid
        for row in m.get('relationships', []):
            relid = row['identifier']
            require(relid not in relationships, 'Duplicate relationship assertion: ' + relid)
            require(row.get('source') == rid, 'Relationship stored under wrong source element: ' + relid)
            require(row['archimateType'] in native_relationships, 'Unknown native relationship: ' + relid)
            require('canonicalId' not in row, 'Relationship cannot own canonicalId: ' + relid)
            require(row.get('standing') and row.get('provenance'), 'Unqualified relationship: ' + relid)
            if row.get('specialization') == 'Home':
                require(row['archimateType'] == 'Association' and row.get('attributes', {}).get('isDirected') == 'true', 'Invalid Home assertion: ' + relid)
            relationships[relid] = row
        for row in m.get('qualifiedAssertions', []):
            pointer = row['sourcePointer']
            require(pointer not in qualified, 'Duplicate qualified assertion: ' + pointer)
            require(row['sourceCanonicalId'] == cid, 'Qualified assertion stored under wrong subject: ' + pointer)
            require(row['targetCanonicalId'] in elements, 'Missing qualified assertion target: ' + pointer)
            require(row.get('reason') and row.get('record_state') and row.get('source_assertion', {}).get('status'), 'Unqualified source predicate: ' + pointer)
            require(row.get('snapshot_pointer') == pointer and 'canonicalId' not in row, 'Invalid qualified assertion identity: ' + pointer)
            qualified[pointer] = row
        for prop, reason in m.get('unresolvedProperties', {}).items():
            unresolved.append({'canonicalId': cid, 'property': prop, 'reason': reason})
    for rid, row in relationships.items():
        require(row['target'] in representations or row['target'] in relationships, 'Unresolved relationship endpoint: ' + rid)
        if row['target'] in representations:
            a = elements[representations[row['source']]][0]['archimateType']
            b = elements[representations[row['target']]][0]['archimateType']
            require(applicability['keys'][row['archimateType']] in applicability['endpoints'].get(a, {}).get(b, ''),
                    'Invalid native relationship endpoints: ' + rid + ' (' + a + ' → ' + b + ')')
        else:
            require(row['archimateType'] == 'Association', 'Only Association may target a relationship: ' + rid)
    for cid, (m, _, _) in elements.items():
        lineage = m['identity'].get('correspondence')
        if lineage:
            successor = lineage.get('successorCanonicalId')
            require(successor in elements and successor != cid and lineage.get('authority'), 'Invalid identity correspondence: ' + cid)
            current = elements[successor][0]
            require(lineage.get('historicalIdentityReserved') and m['standing']['currentness'] == 'superseded', 'Historical identity must remain reserved: ' + cid)
            require(current['standing']['currentness'] == 'current' and current['archimateType'] == m['archimateType'], 'Invalid correspondence successor: ' + cid)
            require(cid in current['identity'].get('predecessorCanonicalIds', []), 'Missing inverse identity lineage: ' + cid)
        for predecessor in m['identity'].get('predecessorCanonicalIds', []):
            require(predecessor in elements and elements[predecessor][0]['identity'].get('correspondence', {}).get('successorCanonicalId') == cid,
                    'Inconsistent predecessor identity lineage: ' + cid)
        for q in m.get('qualifiedAssertions', []):
            mapping = q.get('nativeMapping')
            if not mapping:
                continue
            require(mapping.get('authority'), 'Unqualified native predicate mapping: ' + q['sourcePointer'])
            excluded = q['source_assertion']['status'].lower() in {'unresolved', 'rejected', 'superseded'}
            if excluded:
                require(mapping.get('relationshipType') is None and not mapping.get('relationshipIdentifier'), 'Unestablished relationship materialised: ' + q['sourcePointer'])
                continue
            rid = mapping.get('relationshipIdentifier')
            require(rid in relationships, 'Missing materialised predicate: ' + q['sourcePointer'])
            relation = relationships[rid]
            predicate = q['source_assertion']['type']
            if predicate in {'enabled_by', 'enterprise_enabled_by', 'uses_function', 'uses_process', 'exposes_process'}:
                provider_type = elements[q['targetCanonicalId']][0]['archimateType']
                consumer_type = m['archimateType']
                serving_valid = applicability['keys']['Serving'] in applicability['endpoints'].get(provider_type, {}).get(consumer_type, '')
                require(mapping.get('reverse') is True and mapping['relationshipType'] == ('Serving' if serving_valid else 'Association'), 'ARB provider-to-consumer Serving rule violated: ' + q['sourcePointer'])
            elif predicate == 'realises_feature':
                target_type = elements[q['targetCanonicalId']][0]['archimateType']
                realization_valid = applicability['keys']['Realization'] in applicability['endpoints'].get(m['archimateType'], {}).get(target_type, '')
                require(not mapping.get('reverse') and mapping['relationshipType'] == ('Realization' if realization_valid else 'Association'), 'ARB Realization rule violated: ' + q['sourcePointer'])
            a, b = (q['targetCanonicalId'], cid) if mapping.get('reverse') else (cid, q['targetCanonicalId'])
            require(mapping.get('sourceCanonicalId') == a and mapping.get('targetCanonicalId') == b, 'Incorrect predicate mapping direction: ' + q['sourcePointer'])
            require(relation['source'] == elements[a][0]['exchange']['identifier'] and relation['target'] == elements[b][0]['exchange']['identifier'],
                    'Materialised predicate endpoint mismatch: ' + q['sourcePointer'])
            require(relation['archimateType'] == mapping['relationshipType'], 'Materialised predicate type mismatch: ' + q['sourcePointer'])
            contribution = {'sourcePointer': q['sourcePointer'], 'sourceAssertion': q['source_assertion'], 'recordState': q['record_state'], 'authority': mapping['authority']}
            preserved = (contribution in relation.get('qualifiedPredicateContributions', [])) if mapping.get('existingNativeRelationshipReused') else (
                relation['standing'] == {'semantic': q['source_assertion']['status'], 'currentness': q['record_state']} and relation['provenance'].get('sourceAssertion') == q['source_assertion'])
            require(preserved,
                    'Materialised predicate loses source standing/provenance: ' + q['sourcePointer'])
    boundary_path = root / CONFIG / 'migration-boundary.json'
    if boundary_path.exists():
        boundary = read_json(boundary_path)
        for item in boundary.get('withdrawnUnacceptedAllocations', []):
            withdrawn = item['canonicalId'] if isinstance(item, dict) else item
            require(withdrawn not in elements, 'Withdrawn identity reused: ' + withdrawn)
        for cid, prior in boundary['identities'].items():
            require(cid in elements, 'Lost established identity/tombstone: ' + cid)
            m = elements[cid][0]
            require(m['exchange']['identifier'] == prior['representationId'], 'Unreviewed representation rebinding: ' + cid)
            require(m['archimateType'] == prior['archimateType'], 'Unreviewed native retyping: ' + cid)
    if previous:
        for cid in previous:
            require(cid in elements, 'Lost previous identity/tombstone: ' + cid)
    if not pending:
        import continuum_navigation
        navigation = continuum_navigation.load(root, elements)
    counts = Counter((m['archimateType'], m.get('specialization') or '', m['standing']['semantic'], m['standing']['currentness']) for m, _, _ in elements.values())
    return {'status': 'PASS', 'elements': len(elements), 'relationships': len(relationships),
            'qualifiedSourcePredicates': len(qualified),
            'canonicalIdentities': len(elements), 'representationIdentities': len(representations),
            'provenanceCoverage': len(elements), 'sourceBindings': len(bindings),
            'supportedNativeTypes': len(catalogue['types']),
            'navigation': 'Prospective creation preflight' if pending else 'PASS',
            'counts': [{'type': key[0], 'specialization': key[1], 'standing': key[2], 'currentness': key[3], 'count': count} for key, count in sorted(counts.items())],
            'unresolvedProperties': unresolved, 'architecturalAcceptance': 'See governed HCRLs; validation supplies no acceptance'}


def properties(node, rows):
    if rows:
        group = ET.SubElement(node, tag('properties'))
        for row in rows:
            prop = ET.SubElement(group, tag('property'), propertyDefinitionRef=row['ref'])
            for value in row['values']:
                child = ET.SubElement(prop, tag('value'), **value.get('attributes', {}))
                child.text = value['text']


def set_property(rows, definitions, name, value):
    ref = next((key for key, label in definitions.items() if label == name), None)
    require(ref is not None, 'Missing projection property definition: ' + name)
    rows[:] = [row for row in rows if row['ref'] != ref]
    if value is not None:
        rows.append({'ref': ref, 'values': [{'text': str(value), 'attributes': {}}]})


def project(root=ROOT):
    validation = validate(root)
    catalogue, elements = load(root)
    model = secure_xml(root / DESIGN / 'projection-template.xml')
    definitions = {p.get('identifier'): p.findtext('a:name', namespaces=N) for p in model.findall('a:propertyDefinitions/a:propertyDefinition', N)}
    import continuum_navigation as navigation
    hierarchy = navigation.load(root, elements)
    locations = navigation.by_element(hierarchy)
    navigation_metadata = navigation.transport_metadata(hierarchy)
    # Projection-only properties expose location without altering native Name,
    # canonicalId, existing representation bindings or canonical metadata.
    propdefs = model.find('a:propertyDefinitions', N)
    for identifier, name in [('continuum-navigation-number', 'Continuum / Navigation Number'),
                             ('continuum-navigation-path', 'Continuum / Navigation Path'),
                             ('continuum-navigation-references', 'Continuum / Navigation References')]:
        require(identifier not in definitions and name not in definitions.values(), 'Duplicate navigation property definition')
        prop = ET.SubElement(propdefs, tag('propertyDefinition'), identifier=identifier, type='string')
        ET.SubElement(prop, tag('name')).text = name
        definitions[identifier] = name
    es = model.find('a:elements', N)
    rs = model.find('a:relationships', N)
    require(es is not None and not len(es) and rs is not None and not len(rs), 'Projection template must not own semantic elements or relationships')
    relationships = []
    for cid, (m, body, _) in sorted(elements.items()):
        node = ET.SubElement(es, tag('element'), identifier=m['exchange']['identifier'])
        node.set(XSI, m['archimateType'])
        ET.SubElement(node, tag('name'), {LANG: 'en'}).text = m['displayName']
        # The canonical source body owns the explanation. History and qualification
        # remain visible; no property or body text is interpreted as acceptance.
        text = body.split('## Architectural content\n', 1)[1].strip()
        ET.SubElement(node, tag('documentation'), {LANG: 'en'}).text = text
        rows = copy.deepcopy(m.get('exchange', {}).get('properties', []))
        for name in ROLES:
            set_property(rows, definitions, name, m.get(name))
        set_property(rows, definitions, 'archimateType', m['archimateType'])
        set_property(rows, definitions, 'harmoniaType', m.get('specialization'))
        # Feature is already an established specialization even in captures whose
        # original transport used only harmoniaType. Topic needs explicit authorship.
        set_property(rows, definitions, 'Specialization', m.get('specialization'))
        set_property(rows, definitions, 'Continuum / Canonical Source', str(element_path(m, catalogue)))
        set_property(rows, definitions, 'Continuum / Semantic Standing', m['standing']['semantic'])
        set_property(rows, definitions, 'Continuum / Currentness', m['standing']['currentness'])
        set_property(rows, definitions, 'Continuum / Navigation Number', locations[cid]['number'])
        set_property(rows, definitions, 'Continuum / Navigation Path', ' / '.join(locations[cid]['path']))
        set_property(rows, definitions, 'Continuum / Navigation References', json.dumps(navigation_metadata[cid], ensure_ascii=False, sort_keys=True))
        properties(node, rows)
        relationships.extend(m.get('relationships', []))
    for row in sorted(relationships, key=lambda row: row['identifier']):
        node = ET.SubElement(rs, tag('relationship'), identifier=row['identifier'], source=row['source'], target=row['target'], **row.get('attributes', {}))
        node.set(XSI, row['archimateType'])
        for key in ('name', 'documentation'):
            for value in row.get(key, []):
                ET.SubElement(node, tag(key), **value.get('attributes', {})).text = value['text']
        properties(node, row.get('properties', []))
    release = read_json(root / 'architecture-release.json')
    model_props = model.find('a:properties', N)
    for prop in model_props:
        if definitions[prop.get('propertyDefinitionRef')] == 'Baseline / Architecture Release':
            prop.find('a:value', N).text = release['version']
        if definitions[prop.get('propertyDefinitionRef')] == 'Baseline / Canonical Textual Projection':
            prop.find('a:value', N).text = str(CONTINUUM)
    # Canonical organisation replaces architecture folders. Authored Views,
    # their geometry, identifiers and View-folder navigation remain intact.
    navigation.project(model, hierarchy, elements)
    content = ET.tostring(model, encoding='UTF-8', xml_declaration=True, pretty_print=True)
    # Preserve literal source-text padding in parsed documentation without
    # emitting trailing whitespace in the generated XML's physical lines.
    # Numeric character references round-trip to the original characters.
    content = re.sub(rb'(?<=\S)[ \t]+(?=\r?\n)',
                     lambda match: b''.join(b'&#32;' if char == 32 else b'&#9;' for char in match.group()), content)
    parsed = ET.fromstring(content)
    # Reuse the existing offline resolver for the XML namespace and DC imports.
    from exchange_xml import load_schema
    schema = load_schema(root / SCHEMAS / 'archimate3_Diagram.xsd')
    schema.assertValid(parsed)
    all_ids = [x.get('identifier') for x in parsed.iter() if x.get('identifier')]
    identifiers = set(all_ids)
    require(len(all_ids) == len(identifiers), 'Duplicate XML identifiers')
    for node in parsed.iter():
        for attr in ('elementRef', 'relationshipRef', 'identifierRef', 'propertyDefinitionRef'):
            if node.get(attr):
                require(node.get(attr) in identifiers, 'Dangling XML reference: ' + node.get(attr))
    # Reuse the governed state selection, Home/Composition ancestry and native
    # information naming semantics independently of persisted expected values.
    import containment
    import information_naming
    from exchange_compare import Exchange
    import tempfile
    resolved = containment.resolve(parsed)
    require(not resolved.findings(), 'Invalid governed containment: ' + str(resolved.findings()[:3]))
    with tempfile.TemporaryDirectory(prefix='harmonia-continuum-validation-') as tmp:
        check_path = Path(tmp) / 'model.xml'
        check_path.write_bytes(content)
        model_check = Exchange(check_path)
        require(not model_check.issues, 'Exchange structural issues: ' + str(model_check.issues[:3]))
        names = information_naming.propose(model_check, resolved)
        require(not any(row['changes'] for row in names['records']), 'Native information naming differs from persisted governed values')
    validation.update(containment='PASS', informationNaming=names['summary'])
    validation.update(offlineXsd='PASS', exchangeReferences='PASS', outputSha256=sha(content),
                      publicationAllocatedIdentities=0, actualArchiTransport='NOT_PERFORMED')
    return content, validation


def semantic_id(cid):
    key = json.dumps(['HarmoniaArchitecture', cid], separators=(',', ':'))
    return 'id-' + uuid.uuid5(SEMANTIC_NAMESPACE, key).hex


def create(spec, root=ROOT):
    """Explicit authorised allocation; validation/publication never call this."""
    catalogue, existing = load(root)
    import continuum_navigation as navigation
    navigation.load(root, existing)
    parent_path = spec.get('navigationParent')
    require(isinstance(parent_path, list) and parent_path, 'Creation requires an explicit navigationParent path')
    # Preflight placement without allocating an identity or changing live state.
    nav_data = read_json(root / navigation.SOURCE)
    navigation.with_element(nav_data, '0' * 32, parent_path, existing)
    m, body = copy.deepcopy(spec['metadata']), spec['body']
    require(m.get('identity', {}).get('authority') and m.get('provenance'), 'Creation needs attributable authority and provenance')
    require(not m.get('canonicalId'), 'Create allocates only new identities; edit the existing source to preserve an identity')
    existing_bindings = {(b['source'], b['selector']) for item, _, _ in existing.values() for b in item.get('sourceBindings', [])}
    require(m.get('sourceBindings'), 'Creation needs an explicit construct/source binding')
    require(all((b['source'], b['selector']) not in existing_bindings for b in m['sourceBindings']), 'Source already bound; reconcile against its existing identity')
    # Check type before allocation; names never establish correspondence.
    temporary = {**m, 'canonicalId': '0' * 32}
    element_path(temporary, catalogue)
    # Validate the entire prospective result before allocating or writing.
    preview = copy.deepcopy(m)
    preview['canonicalId'] = '0' * 32
    preview['exchange'] = {'identifier': semantic_id(preview['canonicalId']), 'properties': []}
    validate(root, pending=[(preview, body, root / element_path(preview, catalogue))])
    prospective = {**existing, preview['canonicalId']: (preview, body, root / element_path(preview, catalogue))}
    navigation.load(root, prospective, document=navigation.with_element(nav_data, preview['canonicalId'], parent_path, existing))
    m['canonicalId'] = uuid.uuid4().hex
    require(m['canonicalId'] not in existing, 'UUID collision; do not retry or reuse identity')
    m['identity']['allocation'] = 'UUIDv4 allocated once by explicit create'
    m['exchange'] = {'identifier': semantic_id(m['canonicalId']), 'properties': []}
    path = root / element_path(m, catalogue)
    path.parent.mkdir(parents=True, exist_ok=True)
    require(not path.exists(), 'Refusing to replace an existing element')
    validate(root, pending=[(m, body, path)])
    with path.open('x') as output:
        output.write(encode(m, body))
    (root / navigation.SOURCE).write_bytes(encoded(navigation.with_element(nav_data, m['canonicalId'], parent_path, existing)))
    return path


def template(native, specialization=None, root=ROOT):
    catalogue = read_json(root / CONFIG / 'type-catalogue.json')
    m = {'schemaVersion': 1, 'canonicalId': None, 'archimateType': native,
         'specialization': specialization, 'displayName': '<governed name>',
         'friendlyName': None, 'canonicalName': None,
         'standing': {'semantic': '<source standing>', 'currentness': '<source currentness>'},
         'identity': {'authority': '<operative HCRL/Task decision>'},
         'naming': {'authority': 'enterprise-architecture/governance/canonical-identity-and-naming.md'},
         'provenance': [], 'sourceBindings': [], 'relationships': [], 'unresolvedProperties': {}}
    element_path({**m, 'canonicalId': '0' * 32}, catalogue)
    return {'metadata': m, 'navigationParent': ['<authorised domain>', '<authorised navigation context>'], 'body': '# <governed name>\n\n## Architectural content\n\n<definition, responsibility, boundaries, source-qualified assertions and unresolved matters>\n'}


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--root', type=Path, default=ROOT)
    sub = p.add_subparsers(dest='command', required=True)
    v = sub.add_parser('validate'); v.add_argument('--report', type=Path)
    g = sub.add_parser('project'); g.add_argument('--output', type=Path, required=True); g.add_argument('--report', type=Path); g.add_argument('--check', action='store_true')
    t = sub.add_parser('template'); t.add_argument('--type', required=True); t.add_argument('--specialization')
    c = sub.add_parser('create'); c.add_argument('--spec', type=Path, required=True)
    args = p.parse_args()
    try:
        if args.command == 'template':
            print(encoded(template(args.type, args.specialization, args.root)).decode(), end='')
        elif args.command == 'create':
            print(create(read_json(args.spec), args.root))
        else:
            if args.command == 'project':
                content, report = project(args.root)
                if args.check:
                    require(args.output.exists() and args.output.read_bytes() == content, 'Projection is missing or stale')
                else:
                    args.output.parent.mkdir(parents=True, exist_ok=True); args.output.write_bytes(content)
            else:
                report = validate(args.root)
            if args.report:
                args.report.parent.mkdir(parents=True, exist_ok=True); args.report.write_bytes(encoded(report))
            print(json.dumps({k: v for k, v in report.items() if k not in ('counts', 'unresolvedProperties')}, ensure_ascii=False))
        return 0
    except (ContinuumError, ValueError, KeyError, OSError, ET.LxmlError) as error:
        print('FAIL: ' + str(error), file=sys.stderr)
        return 1


if __name__ == '__main__':
    raise SystemExit(main())
