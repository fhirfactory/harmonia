#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Explicit Home interchange and replay of the fixed accepted Business migration.

No inference from native relationships, names, navigation, Views or graph paths.
Endpoint restrictions below validate this migration, not a general Home type rule.
"""
import hashlib
import json
from pathlib import Path

import generate_views as g

ROOT = g.BASE.parents[2]
REGISTER = ROOT / 'docs/markdown/governance/baseline/business-homes.json'
BUSINESS = {'function': 'BusinessFunction', 'process': 'BusinessProcess',
            'service': 'BusinessService'}


def assertion(model, relationship):
    """Return explicit semantics, resolving Archi's property definition reference.

    Ordinary Association, other specialisations and direction stay distinct.
    Relationship Name never supplies specialisation. Retain the raw XML as well
    when collecting ingestion evidence; this record does not replace it.
    """
    claims = []
    for prop in relationship.findall('a:properties/a:property', g.N):
        name = model.keys.get(prop.get('propertyDefinitionRef'))
        if name in ('Specialization', 'specialization'):
            claims.extend(v.text for v in prop.findall('a:value', g.N))
    if len(claims) > 1:
        raise ValueError('Ambiguous relationship specialisation')
    return dict(identifier=relationship.get('identifier'),
                native_type=relationship.get(g.TYPE).split(':')[-1],
                source=relationship.get('source'), target=relationship.get('target'),
                isDirected=relationship.get('isDirected') in ('true', '1'),
                specialization=claims[0] if claims else None)


def authored_homes(model):
    result = []
    for rel in model.relationships.values():
        item = assertion(model, rel)
        if item['specialization'] != 'Home':
            continue
        g.require(item['native_type'] == 'Association' and item['isDirected'],
                  'Home must be a directed native Association: ' + item['identifier'])
        result.append(item)
    return result


def load(b):
    data = json.loads(REGISTER.read_text())
    digest = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()
    g.require(digest(REGISTER) == b.manifest['business_home_normalisation']['sha256'],
              'Business Home register drift; explicit review required')
    g.require(digest(ROOT / 'docs/markdown/governance/baseline/architecture.json') ==
              data['source_capture_sha256'], 'Business Home source capture drift')
    for path, expected in data['evidence_sha256'].items():
        g.require(digest(ROOT / path) == expected, 'Business Home evidence drift: ' + path)
    expected = {k for k, e in b.records.items() if e['type'] in BUSINESS}
    records = data['records']
    resources = {e['key']: e for e in json.loads(
        (ROOT / 'docs/markdown/governance/baseline/architecture.json').read_text())['elements']}
    g.require({e['source_key'] for e in records} == expected and len(records) == len(expected),
              'Missing/duplicate Business Home assessment')
    ids, sources = set(), set()
    for row in records:
        e = b.records[row['source_key']]
        g.require(row['source_canonicalId'] == resources[row['source_key']]['canonicalId'] and
                  row['source_type'] == e['type'] and row['record_state'] == e['record_state'],
                  'Business Home source identity/currentness drift')
        g.require(row['status'] in ('normalised', 'unresolved', 'ambiguous'),
                  'Unknown Home assessment status')
        if row['status'] != 'normalised':
            g.require('relationship_identifier' not in row, 'Unresolved Home cannot emit an edge')
            continue
        target = b.records[row['target_key']]
        g.require(e['record_state'] == target['record_state'] == 'current' and
                  target['type'] == 'feature' and
                  resources[row['target_key']]['canonicalId'] == row['target_canonicalId'],
                  'Business migration Home target identity/type/currentness drift')
        g.require(row['relationship_type'] == 'Association' and row['isDirected'] is True and
                  row['specialization'] == 'Home', 'Invalid Business Home semantics')
        g.require(row['relationship_identifier'] not in ids and row['source_key'] not in sources,
                  'Duplicate fixed Business Home assertion')
        ids.add(row['relationship_identifier']); sources.add(row['source_key'])
        g.require(row['evidence'], 'Home requires explicit allocation evidence')
        for proof in row['evidence']:
            text = (ROOT / proof['source']).read_text()
            g.require(proof['block'] in text and proof['statement'] in proof['block'] and
                      proof['feature_key'] == row['target_key'], 'Home allocation evidence drift')
            binding = b.source['relationships'][proof['binding_source_relationship_index']]
            g.require(binding['to'] == row['target_key'] and binding['status'] == 'established',
                      'Home corroborating binding drift')
    return data


def apply(b):
    """Replay literal accepted rows only, after legacy View/layout generation."""
    b.business_homes = load(b)
    definitions = b.root.find('a:propertyDefinitions', g.N)
    definition = g.child(definitions, 'propertyDefinition', identifier='specialization', type='string')
    g.child(definition, 'name', 'Specialization')
    relationships = b.root.find('a:relationships', g.N)
    existing = {r.get('identifier') for r in relationships}
    for row in b.business_homes['records']:
        if row['status'] != 'normalised':
            continue
        identifier = row['relationship_identifier']
        g.require(identifier not in existing, 'Home relationship identity collision')
        existing.add(identifier)
        rel = g.child(relationships, 'relationship', identifier=identifier,
                      source=b.ids[row['source_key']], target=b.ids[row['target_key']],
                      isDirected='true')
        rel.set(g.TYPE, 'Association')
        props = g.child(rel, 'properties')
        g.child(g.child(props, 'property', propertyDefinitionRef='specialization'), 'value', 'Home')
        b.homes[identifier] = ['Relations', *b.homes[b.ids[row['source_key']]]]


def validate(b, root):
    definitions = root.findall('a:propertyDefinitions/a:propertyDefinition[@identifier="specialization"]', g.N)
    g.require(len(definitions) == 1 and definitions[0].get('type') == 'string' and
              definitions[0].findtext('a:name', namespaces=g.N) == 'Specialization',
              'Business Home specialisation definition changed')
    relationships = {e.get('identifier'): e for e in root.findall('a:relationships/a:relationship', g.N)}
    for row in b.business_homes['records']:
        if row['status'] != 'normalised':
            continue
        rel = relationships[row['relationship_identifier']]
        g.require(rel.get(g.TYPE) == 'Association' and rel.get('isDirected') == 'true' and
                  rel.get('source') == b.ids[row['source_key']] and
                  rel.get('target') == b.ids[row['target_key']] and
                  rel.find('a:name', g.N) is None and
                  rel.findtext('a:properties/a:property[@propertyDefinitionRef="specialization"]/a:value',
                               namespaces=g.N) == 'Home', 'Business Home interchange semantics changed')


def markdown(b, link):
    lines = ['', '## Explicit Business Home assertions', '',
             '[Modelling authority](../architecture-modelling-strategy.md). Fixed one-time '
             '[migration register](business-homes.json); native relationships above remain unchanged. '
             'Unresolved and ambiguous assessments are preserved in that register. '
             'No Realization or presentation inference applies.', '',
             '| Business behaviour | Directed Association «Home» target | Relationship ID |',
             '| --- | --- | --- |']
    for row in b.business_homes['records']:
        if row['status'] == 'normalised':
            lines.append(f"| {link(b.records[row['source_key']])} | {link(b.records[row['target_key']])} | `{row['relationship_identifier']}` |")
    return lines
