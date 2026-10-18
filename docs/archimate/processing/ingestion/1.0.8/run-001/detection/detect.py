#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded run-specific observation query; no reconciliation or source writes.

Run from the repository root with python3 -B. Outputs only beside this file.
Uses the unchanged secure Exchange reader and offline reference/schema checks.
This is retained detection evidence, not a reusable ingestion implementation.
"""
from collections import Counter, defaultdict
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys

ROOT = Path.cwd()
HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import diagnose_roundtrip as d
from lxml import etree

N = d.g.N
TYPE = d.g.TYPE
P = Path('docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml')
I = HERE / 'submission.xml'
C = Path('.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml')
SOURCE = Path('docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml')


def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def write(name, value):
    (HERE / name).write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n')


def name(node):
    return node.findtext('a:name', namespaces=N)


def signature(node, model, resolve_properties=False):
    attrs = dict(node.attrib)
    if TYPE in attrs:
        attrs[TYPE] = d.expanded_type(node)
    if resolve_properties and 'propertyDefinitionRef' in attrs:
        attrs['propertyDefinitionRef'] = model.keys[attrs['propertyDefinitionRef']]
    # Ignore XML indentation only on element-containing parents. Preserve leaf
    # text, attributes, order, property-value whitespace and visual geometry.
    text = node.text or ''
    if len(node) and not text.strip():
        text = ''
    return [node.tag, sorted(attrs.items()), text,
            [signature(child, model, resolve_properties) for child in node]]


def props(node, model):
    return [{'key': model.keys[p.get('propertyDefinitionRef')],
             'values': d.values(p, 'value')}
            for p in node.findall('a:properties/a:property', N)]


def locator(node, model):
    # Physical LF line, independently of parser treatment of CR/LF.
    identifier = node.get('identifier')
    data = model.path.read_bytes()
    token = ('identifier="' + identifier + '"').encode()
    offset = data.find(token)
    return {'xpath': model.root.getroottree().getpath(node),
            'physical_line': data[:offset].count(b'\n') + 1,
            'exchange_id': identifier}


def record(node, model):
    return {**locator(node, model), 'attributes': dict(node.attrib),
            'native_type': d.expanded_type(node), 'name': d.values(node, 'name'),
            'documentation': d.values(node, 'documentation'),
            'properties': props(node, model)}


def endpoint(identifier, model):
    node = model.elements.get(identifier)
    if node is None:
        return {'exchange_id': identifier, 'resolution': 'UNRESOLVED reference'}
    cid = model.single(node, 'canonicalId')
    return {'exchange_id': identifier, 'native_name': name(node),
            'native_type': d.expanded_type(node), 'canonicalId': cid,
            'harmoniaType': model.single(node, 'harmoniaType'),
            'standing_properties': [p for p in props(node, model)
                                    if 'Status' in p['key'] or 'Standing' in p['key']],
            'resolution': 'governed canonicalId' if cid else
                          'submitted record only; governed architectural identity unresolved'}


def organisation(model):
    folders, refs = Counter(), Counter()
    def visit(node, path):
        label = node.findtext('a:label', namespaces=N)
        if label is not None:
            path = path + (label,)
            folders[path] += 1
        ref = node.get('identifierRef')
        if ref:
            refs[(path, ref)] += 1
        for child in node.findall('a:item', N):
            visit(child, path)
    for node in model.root.findall('a:organizations/a:item', N):
        visit(node, ())
    return folders, refs


def org_delta(counter):
    return [{'path': list(k[0]), 'identifierRef': k[1], 'count': v}
            for k, v in sorted(counter.items())]


def visual(node, model):
    result = record(node, model)
    result['parent_exchange_id'] = node.getparent().get('identifier')
    result['xml'] = etree.tostring(node, encoding='unicode', with_tail=False)
    return result


def main():
    assert sha(SOURCE) == sha(I) == '6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e'
    assert sha(P) == '504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a'
    assert sha(C) == '38e49fa348d015aba6b19e21b4f96dc00fddf3c4d7671b3e352b4c07748917ea'
    p, i, c = (d.Exchange(x) for x in (P, I, C))
    assert not (p.issues or i.issues or c.issues), (p.issues, i.issues, c.issues)
    metrics = {k: m.metrics() for k, m in [('baseline', p), ('submission', i), ('control', c)]}
    added_e = [k for k in i.elements if k not in p.elements]
    added_r = [k for k in i.relationships if k not in p.relationships]
    added_v = [k for k in i.views if k not in p.views]
    added_e_set = set(added_e)
    ret_e = sorted(p.elements.keys() & i.elements.keys())
    ret_r = sorted(p.relationships.keys() & i.relationships.keys())
    ret_v = sorted(p.views.keys() & i.views.keys())
    substantive_e = [k for k in ret_e if signature(i.elements[k], i) != signature(c.elements[k], c)]
    substantive_r = [k for k in ret_r if signature(i.relationships[k], i) != signature(c.relationships[k], c)]
    changed_v = [k for k in ret_v if signature(i.views[k], i) != signature(c.views[k], c)]
    metadata_roles = ['canonicalId', 'displayName', 'friendlyName', 'canonicalName', 'archimateType', 'harmoniaType']
    continuity = [{'exchange_id': k, 'role': role, 'before': p.texts(p.elements[k], role),
                   'after': i.texts(i.elements[k], role)}
                  for k in ret_e for role in metadata_roles
                  if p.texts(p.elements[k], role) != i.texts(i.elements[k], role)]
    cid_owners = defaultdict(list)
    for k, node in i.elements.items():
        for cid in i.texts(node, 'canonicalId'):
            cid_owners[cid].append(k)
    write('identity.json', {
        'retained_metadata_mutations': continuity,
        'canonical_owner_count': len(cid_owners),
        'duplicate_canonical_claims': {k: v for k, v in cid_owners.items() if len(v) != 1},
        'malformed_claims': [k for k in cid_owners if not re.fullmatch('[0-9a-f]{32}', k)],
        'additions': [{'exchange_id': k, 'absent_applicable_machine_metadata': [role for role in metadata_roles
                       if (role != 'harmoniaType' or
                           (d.expanded_type(i.elements[k]).endswith('}Capability') and
                            name(i.elements[k]).startswith('[Feature] '))) and
                       not i.claims(i.elements[k], role)],
                       'treatment': 'Authoring evidence only; no defect or allocation instruction'} for k in added_e],
        'allocation': 'None; all proposed additions retain absent canonicalId',
        'retained_name_displayName_discrepancies': sum(name(p.elements[k]) != p.single(p.elements[k], 'displayName') for k in ret_e),
        'submitted_name_displayName_mutations': [],
    })
    element_rows = []
    for k in added_e:
        node = i.elements[k]
        recognition = ('Feature proposed classification recognised deterministically; native Capability retained'
                       if d.expanded_type(node).endswith('}Capability') and name(node).startswith('[Feature] ')
                       else 'Qualifier repeats native type; no additional Harmonia subtype recognised')
        element_rows.append({**record(node, i), 'recognition': recognition,
            'incident_relationships': [r for r, edge in i.relationships.items() if k in (edge.get('source'), edge.get('target'))],
            'view_appearances': [{'view': v, 'node': n.get('identifier')}
                for v, view in i.views.items() for n in view.findall('.//a:node', N) if n.get('elementRef') == k]})
    write('elements.json', {'added': element_rows, 'removed': sorted(p.elements.keys()-i.elements.keys()),
                           'retained_changes_beyond_control': substantive_e})

    def semantic_key(edge, model):
        ends = [endpoint(edge.get(a), model)['canonicalId'] for a in ['source', 'target']]
        return (d.expanded_type(edge), *ends) if all(ends) else None
    base_groups = defaultdict(list)
    for k, edge in p.relationships.items():
        base_groups[semantic_key(edge, p)].append(k)
    incoming_groups = defaultdict(list)
    for k, edge in i.relationships.items():
        incoming_groups[semantic_key(edge, i)].append(k)
    relationship_rows = []
    for k in added_r:
        edge = i.relationships[k]
        key = semantic_key(edge, i)
        matches = base_groups.get(key, []) if key else []
        relationship_rows.append({**record(edge, i), 'source': endpoint(edge.get('source'), i),
            'target': endpoint(edge.get('target'), i), 'resolved_canonical_comparison_key': key,
            'baseline_matches': matches,
            'duplicate_result': ('UNRESOLVED: new endpoint has no governed architectural identity'
                if key is None else ('candidate duplicate; compare assertion discriminators' if matches else
                                      'No baseline duplicate against resolved governed endpoints')),
            'incoming_resolved_key_matches': incoming_groups.get(key, []) if key else [],
            'baseline_discriminators_for_matches': {x: props(p.relationships[x], p) for x in matches}})
    raw_groups = defaultdict(list)
    for k, edge in i.relationships.items():
        raw_groups[(d.expanded_type(edge), edge.get('source'), edge.get('target'))].append(k)
    write('relationships.json', {'added': relationship_rows,
        'removed': sorted(p.relationships.keys()-i.relationships.keys()),
        'retained_changes_beyond_control': substantive_r,
        'duplicate_raw_exchange_triples': [v for v in raw_groups.values() if len(v) > 1],
        'duplicate_resolved_canonical_triples': [v for k, v in incoming_groups.items() if k is not None and len(v) > 1],
        'discriminator_rule': 'Preserve existing relationship binding and all reviewed assertion/crosswalk Properties; tuple equality is a candidate test, not permission to merge distinct assertions.'})

    view_rows = []
    for v in changed_v + added_v:
        view = i.views[v]
        before = c.views.get(v)
        base_view = p.views.get(v)
        old_nodes = {n.get('identifier'): n for n in before.findall('.//a:node', N)} if before is not None else {}
        old_connections = {n.get('identifier'): n for n in before.findall('.//a:connection', N)} if before is not None else {}
        nodes = {n.get('identifier'): n for n in view.findall('.//a:node', N)}
        connections = {n.get('identifier'): n for n in view.findall('.//a:connection', N)}
        connection_rows = []
        for k, connection in connections.items():
            rel = i.relationships[connection.get('relationshipRef')]
            connection_rows.append({**visual(connection, i), 'semantic_relationship': record(rel, i),
                'semantic_source': endpoint(rel.get('source'), i), 'semantic_target': endpoint(rel.get('target'), i),
                'relationship_origin': 'existing governed relationship' if rel.get('identifier') in p.relationships else 'new semantic relationship proposal',
                'appearance_change': 'added' if k not in old_connections else 'retained',
                'governed_relationship_properties': props(p.relationships[rel.get('identifier')], p) if rel.get('identifier') in p.relationships else []})
        view_rows.append({**record(view, i), 'change': 'added' if v in added_v else 'modified',
            'baseline_node_count': len(base_view.findall('.//a:node', N)) if base_view is not None else 0,
            'baseline_connection_count': len(base_view.findall('.//a:connection', N)) if base_view is not None else 0,
            'nodes': [{**visual(n, i), 'appearance_change': 'added' if k not in old_nodes else
                       ('modified beyond control' if signature(n, i) != signature(old_nodes[k], c) else 'retained'),
                       'baseline_xml': etree.tostring(next(x for x in base_view.findall('.//a:node', N) if x.get('identifier') == k), encoding='unicode', with_tail=False) if k in old_nodes else None,
                       'endpoint': endpoint(n.get('elementRef'), i)} for k, n in nodes.items()],
            'connections': connection_rows,
            'removed_nodes': sorted(old_nodes.keys()-nodes.keys()),
            'removed_connections': sorted(old_connections.keys()-connections.keys())})
    write('views.json', {'relevant_views': view_rows, 'removed_views': sorted(p.views.keys()-i.views.keys()),
                         'unchanged_beyond_control': len(ret_v)-len(changed_v)})
    pf, pr = organisation(p)
    inf, ir = organisation(i)
    write('navigation.json', {'added_folders': [{'path': list(k), 'count': v} for k, v in sorted((inf-pf).items())],
        'removed_folders': [{'path': list(k), 'count': v} for k, v in sorted((pf-inf).items())],
        'added_references': org_delta(ir-pr), 'removed_references': org_delta(pr-ir),
        'counts': {'baseline_folders': sum(pf.values()), 'submission_folders': sum(inf.values()),
                   'baseline_references': sum(pr.values()), 'submission_references': sum(ir.values())},
        'method': 'Path/reference multisets; no reconstruction of stripped organisation identities or semantic containment from folders.'})

    prop_changes = Counter()
    for collection in ['elements', 'relationships']:
        for k in getattr(p, collection).keys() & getattr(i, collection).keys():
            old = {row['key']: row['values'] for row in props(getattr(p, collection)[k], p)}
            new = {row['key']: row['values'] for row in props(getattr(i, collection)[k], i)}
            for key in old.keys() | new.keys():
                if old.get(key) != new.get(key):
                    prop_changes[key] += 1
    write('transport.json', {'control_path': str(C), 'control_sha256': sha(C),
        'retained_element_records_exact_against_control': len(ret_e)-len(substantive_e),
        'retained_relationship_records_exact_against_control': len(ret_r)-len(substantive_r),
        'evidence_property_changes_against_baseline': dict(prop_changes),
        'property_definitions': [{'key': key, 'baseline_id': p.definition_ids[key], 'incoming_id': i.definition_ids[key],
            'baseline_xml': etree.tostring(p.definitions[p.definition_ids[key]], encoding='unicode', with_tail=False),
            'incoming_xml': etree.tostring(i.definitions[i.definition_ids[key]], encoding='unicode', with_tail=False)} for key in sorted(p.definition_ids)],
        'property_definitions_exact_against_control': all(signature(i.definitions[i.definition_ids[key]], i) == signature(c.definitions[c.definition_ids[key]], c) for key in p.definition_ids),
        'original_organisation_item_ids': len(p.root.findall('.//a:organizations//a:item[@identifier]', N)),
        'incoming_organisation_item_ids': len(i.root.findall('.//a:organizations//a:item[@identifier]', N)),
        'model_properties_match_baseline': props(p.root, p) == props(i.root, i),
        'note': 'Control is transport evidence only; P remains semantic baseline. No comparator equivalence is added.'})

    # Verify the one-value publication lineage in memory; never rewrite either XML.
    old_pub = Path('docs/archimate/exchange/publish/1.0.7/Harmonia-Architecture-1.0.7.xml')
    release_pd = p.definition_ids['Baseline / Architecture Release']
    release_value = p.root.find("a:properties/a:property[@propertyDefinitionRef='" + release_pd + "']/a:value", N)
    assert release_value.text == '1.0.8'
    current_bytes = P.read_bytes()
    # Compare explicit bytes, not an XML serialization with unrelated whitespace.
    token = b'<value xml:lang="en">1.0.8</value>'
    assert current_bytes.count(token) == 1
    restored = current_bytes.replace(token, b'<value xml:lang="en">1.0.7</value>', 1)
    assert restored == old_pub.read_bytes()
    authority_paths = ['AGENTS.md', 'docs/AGENTS.md', 'architecture-release.json'] + [
        'docs/markdown/governance/' + f for f in ['architectural-axioms.md',
        'archimate-ingestion-change-review.md', 'archimate-ingestion-publication-semantics.md',
        'architecture-authoring-praxis.md', 'canonical-identity-and-naming.md',
        'archi-navigation-and-view-convention.md', 'rad-archimate-model-mapping.md',
        'architecture-release-policy.md', 'templates/archimate-ingestion-change-review-template.md']]
    publication_paths = ['docs/archimate/exchange/publish/README.md'] + [
        'docs/archimate/processing/publishing/1.0.8/' + f for f in [
        'publication-1.0.8.md', 'publication-comparison.json', 'archi-inspection-1.0.8.md',
        'publication-validation-1.0.8.json']]
    write('provenance.json', {'run': '1.0.8/run-001',
        'head': subprocess.check_output(['git', 'rev-parse', 'HEAD'], text=True).strip(),
        'current_governance_release': json.loads(Path('architecture-release.json').read_text()),
        'baseline_release_publication': '1.0.8', 'baseline_path': str(P), 'baseline_sha256': sha(P),
        'submission_path': str(SOURCE), 'submission_sha256': sha(SOURCE),
        'snapshot_path': 'detection/submission.xml', 'snapshot_sha256': sha(I),
        'iteration': 'Closed by deliberate Submission authorised in this task; earlier exports are intermediate evidence, not separate submissions.',
        'publication_evidence': {x: sha(x) for x in publication_paths},
        'authority_pins': {x: sha(x) for x in authority_paths},
        'baseline_relationship': 'Published index identifies 1.0.8 as applicable publication; publication report/inspection/comparison pin this exact artefact. Submission retains its model ID, 1.0.8 release Property, 1,299 original canonical owners and established element/relationship bindings. Task deliberately submits this closed blind-test iteration; reports support history only.',
        '107_to_108_exact_byte_restoration': True, '107_published_sha256': sha(old_pub),
        'control_path': str(C), 'control_sha256': sha(C),
        'evidence_limits': ['Native GUI edit sequence/operator attestation unknown.',
            'Exact second intermediate XML unavailable in preserved historical analysis; not needed for P-to-final detection.',
            'No whole-document lossless transport or visual paint-order equivalence claimed.',
            'Publication report retains preparation-time acceptance condition; current publication index/inspection and this baseline-specific task establish applicable published package without inventing another GUI acceptance event.'],
        'tools': {'python': sys.version, 'lxml': etree.LXML_VERSION,
                  'query_sha256': sha(__file__), 'existing_reader_sha256': sha(d.__file__)}})
    summary = {'metrics': metrics, 'added_elements': len(added_e), 'added_relationships': len(added_r),
        'new_views': len(added_v), 'modified_views_beyond_control': len(changed_v),
        'element_removals': sorted(p.elements.keys()-i.elements.keys()),
        'relationship_removals': sorted(p.relationships.keys()-i.relationships.keys()),
        'retained_element_changes_beyond_control': substantive_e,
        'retained_relationship_changes_beyond_control': substantive_r,
        'retained_governed_metadata_mutations': continuity,
        'input_schema_reference_issues': {'baseline': p.issues, 'submission': i.issues, 'control': c.issues}}
    write('summary.json', summary)
    print(json.dumps(summary, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    main()
