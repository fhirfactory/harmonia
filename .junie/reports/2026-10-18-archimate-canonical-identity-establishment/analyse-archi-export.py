#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only Release 1.0.7 evidence probe; no comparator equivalence rules."""
import copy
import json
import sys
from collections import Counter, defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[3]
sys.path.insert(0, str(ROOT / 'docs/archimate/capability-architecture/tools'))
import diagnose_roundtrip as d
import publish_navigation as navigation

HERE = Path(__file__).resolve().parent
OLD = HERE.parent / '2026-10-18-archimate-roundtrip-increment-1'
PUBLISHED = d.BASE / 'synchronised-baseline/Harmonia-Architecture-1.0.7.xml'
INCOMING = HERE / 'archi-exports/release-1.0.7.xml'


def organisation(model):
    folders, references, empty = Counter(), Counter(), Counter()
    items = model.root.findall('.//a:organizations//a:item', d.g.N)

    def visit(item, path):
        labels = d.values(item, 'label')
        current = path + ((d.encoded(labels),) if labels else ())
        if labels:
            folders[current] += 1
            if not item.findall('a:item', d.g.N):
                empty[current] += 1
        if item.get('identifierRef'):
            references[(current, item.get('identifierRef'))] += 1
        for child in item.findall('a:item', d.g.N):
            visit(child, current)

    for item in model.root.findall('a:organizations/a:item', d.g.N):
        visit(item, ())
    return dict(items=len(items), identifiers=sum('identifier' in i.attrib for i in items),
                folders=folders, references=references, empty=empty)


def no_properties(node):
    result = copy.deepcopy(node)
    for child in result.findall('a:properties', d.g.N):
        result.remove(child)
    return d.g.signature(result)


def main():
    before, after = d.Exchange(PUBLISHED), d.Exchange(INCOMING)
    raw = json.loads((HERE / 'archi-fidelity.json').read_text())
    diagnostic = json.loads((HERE / 'roundtrip-diagnostic.json').read_text())
    historical = json.loads((OLD / 'archi-fidelity.json').read_text())['probes']['release-1.0.6']
    analysis = json.loads((OLD / 'empirical-analysis.json').read_text())['probes']['release-1.0.6']
    assert raw['published_sha256'] == before.sha256 and raw['incoming_sha256'] == after.sha256
    assert diagnostic['input_sha256'] == after.sha256
    assert not before.issues and not after.issues, [before.issues, after.issues]
    assert set(before.elements) == set(after.elements) and len(before.elements) == 1299
    assert set(before.relationships) == set(after.relationships) and len(before.relationships) == 852

    properties = {}
    for role in d.ROLES:
        left, right = {}, {}
        for model, result in ((before, left), (after, right)):
            for identifier, owner in model.elements.items():
                claims = model.claims(owner, role)
                if claims:
                    result[identifier] = [d.values(p, 'value') for p in claims]
        missing = sorted(left.keys() - right.keys())
        changed = sorted(k for k in left.keys() & right.keys() if left[k] != right[k])
        additional = sorted(right.keys() - left.keys())
        properties[role] = dict(status='PASS' if left == right else 'FAIL',
                                published=len(left), exported=len(right),
                                exactly_retained=len(left) - len(missing) - len(changed),
                                missing=len(missing), changed=len(changed), additional=len(additional),
                                affected_elements=sorted(set(missing + changed + additional)))
        assert left == right, (role, properties[role])
        assert len(left) == (178 if role == 'harmoniaType' else 1299), role

    owners = []
    for model in (before, after):
        index = defaultdict(list)
        for identifier, element in model.elements.items():
            value = model.single(element, 'canonicalId')
            assert value is not None and d.UUID32.fullmatch(value), (identifier, value)
            index[value].append(identifier)
        owners.append(dict(index))
    duplicate = {v: ids for v, ids in owners[1].items() if len(ids) > 1}
    wrong_owner = {v: dict(published=ids, exported=owners[1].get(v, []))
                   for v, ids in owners[0].items() if owners[1].get(v) != ids}
    assert not duplicate and not wrong_owner, (duplicate, wrong_owner)
    properties['canonicalId'].update(duplicated=0, malformed=0, multiple_claims=0,
                                     wrong_owner_association=0)
    assert diagnostic['future_identity_readiness'] == 'CONFORMANT_IN_SNAPSHOT'
    assert diagnostic['future_naming_readiness'] == 'CONFORMANT_WITH_GOVERNED_DERIVATION'
    assert not diagnostic['naming']['conflicts']

    semantic = {}
    for name in ('elements', 'relationships'):
        left, right = getattr(before, name), getattr(after, name)
        changed = [identifier for identifier in left if no_properties(left[identifier]) != no_properties(right[identifier])]
        assert not changed, (name, changed)
        semantic[name] = dict(published=len(left), exported=len(right), retained_ids=len(left),
                              missing_ids=[], additional_ids=[], all_non_property_content_exact=True)
    semantic['relationship_endpoints_exact'] = all(
        (rel.get('source'), rel.get('target')) == (after.relationships[key].get('source'), after.relationships[key].get('target'))
        for key, rel in before.relationships.items())
    semantic['relationship_types'] = dict(Counter(d.expanded_type(r) for r in before.relationships.values()))

    registry = json.loads(d.REGISTRY.read_text())
    bindings = []
    for record in registry['records']:
        code, identifier = record['canonical_architecture_id'], record['archimate']['semantic_id']
        left = [key for key, owner in before.elements.items() if code in before.texts(owner, d.EC_PROPERTY)]
        right = [key for key, owner in after.elements.items() if code in after.texts(owner, d.EC_PROPERTY)]
        assert left == right == [identifier], (code, left, right, identifier)
        bindings.append(dict(alias=code, element=identifier, canonicalId=before.single(before.elements[identifier], 'canonicalId')))
    assert len(bindings) == 14

    org_a, org_b = organisation(before), organisation(after)
    organisation_result = {key: [org_a[key], org_b[key]] for key in ('items', 'identifiers')}
    for key in ('folders', 'references', 'empty'):
        assert org_a[key] == org_b[key], key
        organisation_result[key] = dict(count=sum(org_a[key].values()), exact_path_multisets_equal=True)
    assert navigation.navigation_homes(before.root) == navigation.navigation_homes(after.root)
    organisation_result['existing_navigation_homes_equal'] = True

    assert set(before.views) == set(after.views)
    presentation = dict(raw['presentation'])
    presentation['exact_view_attributes_and_names'] = 0
    presentation['exact_node_attributes_parent_and_node_order'] = 0
    presentation['exact_connections_including_endpoints_styles_and_ordered_bendpoints'] = 0
    presentation['ordered_bendpoints'] = 0
    for vid, view in before.views.items():
        target = after.views[vid]
        assert view.attrib == target.attrib and d.values(view, 'name') == d.values(target, 'name'), vid
        presentation['exact_view_attributes_and_names'] += 1
        for tag in ('node', 'connection'):
            left = {n.get('identifier'): n for n in view.findall('.//a:' + tag, d.g.N)}
            right = {n.get('identifier'): n for n in target.findall('.//a:' + tag, d.g.N)}
            assert left.keys() == right.keys(), (vid, tag)
            for identifier, item in left.items():
                incoming = right[identifier]
                assert item.attrib == incoming.attrib, (vid, identifier)
                assert item.getparent().get('identifier') == incoming.getparent().get('identifier'), (vid, identifier)
                if tag == 'node':
                    assert [n.get('identifier') for n in item.findall('a:node', d.g.N)] == [n.get('identifier') for n in incoming.findall('a:node', d.g.N)]
                    presentation['exact_node_attributes_parent_and_node_order'] += 1
                else:
                    assert d.g.signature(item) == d.g.signature(incoming), (vid, identifier)
                    presentation['exact_connections_including_endpoints_styles_and_ordered_bendpoints'] += 1
                    presentation['ordered_bendpoints'] += len(item.findall('a:bendpoint', d.g.N))
        assert [n.get('identifier') for n in view.findall('a:node', d.g.N)] == [n.get('identifier') for n in target.findall('a:node', d.g.N)]

    # Exact event equality preserves all historical loss/change classifications.
    def non_definition_events(probe):
        return Counter(d.encoded(f) for f in probe['findings'] if '/propertyDefinitions[' not in f['path'])
    assert non_definition_events(raw) == non_definition_events(historical)
    definition_events = [f for f in raw['findings'] if '/propertyDefinitions[' in f['path']]
    for finding in definition_events:
        assert (finding['classification'] == d.EQUIVALENT and
                (finding['path'].endswith('/@identifier') or finding['path'].endswith('/child-order'))) or (
                    finding['classification'] == d.LOSS and finding['path'].endswith('/name[1]/@{http://www.w3.org/XML/1998/namespace}lang')
                    and finding['before'] == 'en' and finding['after'] is None), finding
    assert before.definition_ids.keys() == after.definition_ids.keys()

    result = dict(status='PASS', architecture_release='1.0.7',
                  scope='Canonical Identity and Governed Naming round-trip transport; whole-document comparison retains DIFFERENCES',
                  published_sha256=before.sha256, incoming_sha256=after.sha256,
                  input_issues=raw['input_issues'], properties=properties, semantic=semantic,
                  ec_bindings=dict(exactly_retained=14, bindings=bindings),
                  organisation=organisation_result, presentation=presentation,
                  increment_1_decomposition=dict(
                      exact_non_definition_findings_retained=sum(non_definition_events(raw).values()),
                      all_non_definition_findings_identical_to_historical_release_106=True,
                      property_definitions=len(before.definitions),
                      definition_event_classifications=dict(Counter(f['classification'] for f in definition_events)),
                      added_definition_keys=sorted(before.definition_ids.keys() - d.Exchange(d.BASE / 'synchronised-baseline/Harmonia-Architecture-Baseline.xml').definition_ids.keys()),
                      known_property_text=analysis['property_text'],
                      known_presentation=analysis['presentation'],
                      new_behaviours=[], comparator_normalisation_changed=False),
                  raw_classifications=raw['classifications'], increment_2='NOT STARTED')
    (HERE / 'empirical-acceptance.json').write_text(d.encoded(result))
    print(json.dumps(dict(status=result['status'], properties=properties,
                         exact_non_definition_findings_retained=result['increment_1_decomposition']['exact_non_definition_findings_retained'],
                         new_behaviours=[]), indent=2))


if __name__ == '__main__':
    main()
