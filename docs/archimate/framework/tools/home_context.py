#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Project the accepted explicit Home context using the governed naming/layout rules.

The frozen captures remain history. SOURCE amends their resource naming fields;
it establishes neither identities, Homes, responsibility nor semantic containment.
Establishment is explicit; ordinary validation/publication never repairs metadata.
"""
import collections
import containment
import copy
import gzip
import hashlib
from types import SimpleNamespace

import generate_views as g
import governed_metadata as m
import home_relationships as h
import publish_navigation as nav

SOURCE = m.RAD / 'home-derived-metadata.json'
AUTHORITY = 'docs/markdown/governance/architecture-modelling-strategy.md#home-derived-naming-and-navigation'
EVIDENCE = m.ROOT / 'docs/archimate/processing/publishing/1.0.9/home-context-completion'


def model(root):
    return SimpleNamespace(keys={p.get('identifier'): p.findtext('a:name', namespaces=g.N)
                                 for p in root.findall('a:propertyDefinitions/a:propertyDefinition', g.N)},
                           relationships={p.get('identifier'): p
                                          for p in root.findall('a:relationships/a:relationship', g.N)})


def ancestry(identifier, index, recorded=None):
    """Replay the accepted naming context through the common ancestry engine."""
    try:
        return (containment.recorded_context(index) if recorded is None else recorded).ancestry(identifier)
    except ValueError as exc:
        raise g.PublicationError('Invalid Home ancestry: ' + str(exc)) from exc


def feature_navigation(b, path, index):
    """Use the existing Feature Business projection, including unresolved framing."""
    records = [index[key]['record'] for key in path]
    feature = records[-1]
    if feature['key'] in b.records:
        raw = b.context_paths(feature)[2]
    else:
        # Accepted Features carry an explicit context instead of legacy owner_key.
        # Pass that same context to the existing Feature navigation machinery.
        raw = b.context_paths(feature, ancestry=records[:-1])[2]
    labels = {record['name']: '[' + ('Feature' if record['type'] == 'feature' else record['tier']) +
              '] ' + record['name'] for record in records}
    import synchronise_baseline as s
    return [labels.get(label, label) for label in s.navigation_path(raw)]


def derive(b, root, index):
    """Recognise explicit assertions first. Presentation is never an input."""
    resolved = containment.resolve(root)
    assertions = collections.defaultdict(list)
    for edge in resolved.selected.values():
        if edge['mechanism'] != 'Home':
            continue
        g.require(edge['child'] in index and edge['parent'] in index, 'Invalid Home endpoint')
        if index[edge['child']]['archimateType'] in h.BUSINESS.values():
            resolved.parent_link(edge['child'])
            assertions[edge['child']].append(edge)
    result = {}
    import recover_v02 as r
    try:
        recorded = containment.recorded_context(index)
    except ValueError as exc:
        raise g.PublicationError('Invalid Home ancestry: ' + str(exc)) from exc
    for source, edges in sorted(assertions.items()):
        g.require(len(edges) == 1, 'Multiple explicit Homes; adjudication required: ' + source)
        edge = edges[0]
        target = index[edge['parent']]
        g.require(target['harmoniaType'] == 'Feature' and target['archimateType'] == 'Capability',
                  'Invalid explicit Home target: ' + source)
        path = ancestry(edge['parent'], index, recorded)
        g.require(all(index[key]['record']['type'] in ('capability', 'enterprise_capability') for key in path[:-1]),
                  'Invalid governed Feature ancestry: ' + source)
        record = index[source]['record']
        names = m.qualified_names([*[index[key]['record'] for key in path], record])
        result[source] = dict(source_key=record['key'], canonicalId=record['canonicalId'],
                              home_relationship=edge['identifier'], home=edge['parent'],
                              semantic_context=path, **names,
                              navigation_path=[*feature_navigation(b, path, index), r.BEHAVIOURS[record['type']]])
    return result


def amended_index(index, derived, source=None):
    data = m.read(SOURCE) if source is None else source
    g.require(data['authority'] == AUTHORITY, 'Missing Home context authority')
    rows = data['resources']
    g.require(set(rows) == set(derived), 'Missing/extra Home-derived resource amendment')
    result = copy.deepcopy(index)
    for identifier, expected in derived.items():
        row = rows[identifier]
        g.require(row == {**expected, 'previous_names': {key: index[identifier]['record'][key]
                                                      for key in ('friendlyName', 'canonicalName')}},
                  'Home-derived resource metadata drift: ' + identifier)
        for role in ('friendlyName', 'canonicalName'):
            result[identifier]['record'][role] = row[role]
            result[identifier]['expected'][role] = expected[role]
    m.validate_records(result, {key: item['record']['canonicalId'] for key, item in index.items()})
    return result


def relocate(root, identifier, path, context, index):
    """Move the one existing reference; reuse the established folder convention."""
    import reconcile_108 as x
    refs = root.xpath('a:organizations//a:item[@identifierRef=$key]', namespaces=g.N, key=identifier)
    g.require(len(refs) == 1, 'Missing/multiple principal navigation location: ' + identifier)
    reference = refs[0]
    reference.getparent().remove(reference)
    x.add_reference(root, identifier, path, reference=reference,
                    folder_context=[index[key]['record'] for key in context])


def project(b, root, index, derived, amended):
    definitions = model(root).keys
    elements = {e.get('identifier'): e for e in root.findall('a:elements/a:element', g.N)}
    before = nav.navigation_homes(root)
    for identifier, row in derived.items():
        for prop in elements[identifier].findall('a:properties/a:property', g.N):
            role = definitions[prop.get('propertyDefinitionRef')]
            if role in ('friendlyName', 'canonicalName'):
                prop.find('a:value', g.N).text = amended[identifier]['record'][role]
        if before[identifier] != [tuple(row['navigation_path'])]:
            relocate(root, identifier, row['navigation_path'], row['semantic_context'], index)
    for edge in root.findall('a:relationships/a:relationship', g.N):
        source = edge.get('source')
        if source in derived:
            path = ['Relations', *derived[source]['navigation_path']]
            if before[edge.get('identifier')] != [tuple(path)]:
                relocate(root, edge.get('identifier'), path, derived[source]['semantic_context'], index)
    return root


def publish(outputs, b, report, index):
    root = g.ET.fromstring(outputs[m.model_path()])
    derived = derive(b, root, index)
    amended = amended_index(index, derived)
    before = nav.navigation_homes(root)
    project(b, root, index, derived, amended)
    b.home_context_index = amended
    b.home_context = derived
    b.reconciliation_root = copy.deepcopy(root)
    outputs = dict(outputs)
    outputs[m.model_path()] = g.serialize(root)
    import synchronise_baseline as s
    for identifier in derived:
        record = index[identifier]['record']
        path = m.RAD / (s.group(record) + '.md')
        text = outputs[path].decode()
        marker = ('| Source capture key | ' + record['key'] + ' |' if record['key'] in b.records else
                  '| representationId | ' + identifier + ' |')
        g.require(text.count(marker) == 1, 'Missing/ambiguous resource Markdown binding: ' + identifier)
        start = text.index(marker)
        end = text.find('\n\n', start)
        end = len(text) if end < 0 else end
        block = text[start:end]
        for role in ('friendlyName', 'canonicalName'):
            old, new = record[role], amended[identifier]['record'][role]
            if old != new:
                field = '| ' + role + ' | ' + old + ' |'
                g.require(block.count(field) == 1, 'Missing/ambiguous resource Markdown field: ' + identifier)
                block = block.replace(field, '| ' + role + ' | ' + new + ' |')
        outputs[path] = (text[:start] + block + text[end:]).encode()
    report = copy.deepcopy(report)
    relationships = model(root).relationships
    report['home_context'] = dict(applicable=len(derived),
        by_type=dict(collections.Counter(index[key]['archimateType'] for key in derived)),
        friendlyName_changes=sum(index[key]['record']['friendlyName'] != row['friendlyName'] for key, row in derived.items()),
        canonicalName_changes=sum(index[key]['record']['canonicalName'] != row['canonicalName'] for key, row in derived.items()),
        element_relocations=sum(before[key] != [tuple(row['navigation_path'])] for key, row in derived.items()),
        relationship_relocations=sum(before[key] != paths for key, paths in nav.navigation_homes(root).items()
                                     if key in relationships))
    report['output_sha256'] = hashlib.sha256(outputs[m.model_path()]).hexdigest()
    report['resource_and_view_folder_count'] = len(root.xpath(
        'a:organizations//a:item[not(@identifierRef)]', namespaces=g.N))
    report['roundtrip_status'] = ('See home-context-completion/verification.json for hash-bound actual Archi '
                                  'transport evidence; human rendering acceptance remains separate')
    report['markdown_sha256'] = {str(p.relative_to(m.ROOT)): hashlib.sha256(value).hexdigest()
                                 for p, value in outputs.items() if p.parent == m.RAD}
    report['checks']['home_derived_naming_and_navigation'] = 'PASS'
    outputs[m.validation_path()] = m.encoded(report)
    return outputs, b, report


def establish():
    """One authorised resource amendment, separate from ordinary publication."""
    g.require(not SOURCE.exists(), 'Home context amendment already established; review changes explicitly')
    import synchronise_baseline as s
    outputs, b, _ = s.publish(home_derived=False)
    index, _ = m.load(b)
    index.update(b.reconciliation_index)
    derived = derive(b, g.ET.fromstring(outputs[m.model_path()]), index)
    resources = {key: {**row, 'previous_names': {role: index[key]['record'][role]
                                              for role in ('friendlyName', 'canonicalName')}}
                 for key, row in derived.items()}
    data = dict(authority=AUTHORITY, architecture_release='1.0.9',
                scope='Current resource naming amendments to frozen captures; no identity or semantic assertions',
                resources=resources)
    amended_index(index, derived, data)
    SOURCE.write_bytes(m.encoded(data))
    print('Established Home-derived resource metadata:', len(resources))


def before_bytes(relative):
    row = m.read(EVIDENCE / 'before.json')[relative]
    data = (gzip.decompress((m.ROOT / row['snapshot']).read_bytes()) if 'snapshot' in row
            else (m.ROOT / relative).read_bytes())
    g.require(hashlib.sha256(data).hexdigest() == row['sha256'], 'Changed pre-completion snapshot: ' + relative)
    return data


def audit(b, root):
    """Independently bound the delta against the captured unsigned candidate."""
    presented_sha256 = None
    if hasattr(b, 'view_presentation'):
        import view_presentation as vp
        vp.audit(b.view_presentation_before, root, b.presentation_scope)
        presented_sha256 = hashlib.sha256(g.serialize(root)).hexdigest()
        # This historical contract still checks unchanged Views at its own
        # boundary. Current presentation has a separate exhaustive delta audit.
        root = copy.deepcopy(root)
        root.replace(root.find('a:views', g.N), copy.deepcopy(b.view_presentation_before.find('a:views', g.N)))
    import reconcile_108 as x
    index, _ = m.load(b)
    index.update(x.descriptors(x.read(), index))
    derived = derive(b, root, index)
    amended_index(index, derived)
    # The Home completion's before evidence belongs to its originating release,
    # even when unchanged Home semantics are replayed into a later publication.
    version = m.read(SOURCE)['architecture_release']
    historical_model = m.PUBLISH / version / ('Harmonia-Architecture-' + version + '.xml')
    previous = g.ET.fromstring(before_bytes(str(historical_model.relative_to(m.ROOT))))
    old_locations, locations = nav.navigation_homes(previous), nav.navigation_homes(root)
    property_keys = model(root).keys
    for section in ('elements/a:element', 'relationships/a:relationship', 'views/a:diagrams/a:view'):
        old = {e.get('identifier'): e for e in previous.findall('a:' + section, g.N)}
        new = {e.get('identifier'): e for e in root.findall('a:' + section, g.N)}
        g.require(old.keys() == new.keys(), 'Unrelated architectural identity/population change')
        for identifier in old:
            left, right = copy.deepcopy(old[identifier]), copy.deepcopy(new[identifier])
            if section.startswith('elements') and identifier in derived:
                for node in (left, right):
                    for prop in node.findall('a:properties/a:property', g.N):
                        role = property_keys[prop.get('propertyDefinitionRef')]
                        if role in ('friendlyName', 'canonicalName'):
                            prop.getparent().remove(prop)
            g.require(g.signature(left) == g.signature(right),
                      'Unexpected semantic/responsibility/presentation change: ' + identifier)
    edges = model(root).relationships
    for identifier, paths in locations.items():
        g.require(len(paths) == 1, 'Multiple navigation locations: ' + identifier)
        if identifier in derived:
            expected = tuple(derived[identifier]['navigation_path'])
        elif identifier in edges and edges[identifier].get('source') in derived:
            expected = ('Relations', *locations[edges[identifier].get('source')][0])
        else:
            expected = old_locations[identifier][0]
        g.require(paths == [expected], 'Home/unrelated navigation drift: ' + identifier)
    for path in ('architecture.json', 'motivation.json', 'business-homes.json', 'accepted-ingestion-1.0.8.json'):
        relative = str((m.RAD / path).relative_to(m.ROOT))
        g.require((m.ROOT / relative).read_bytes() == before_bytes(relative), 'Frozen architectural source changed')
    # Validate actual names, not merely the persisted amendments or expected root.
    values = {}
    keys = model(root).keys
    for element in root.findall('a:elements/a:element', g.N):
        values[element.get('identifier')] = {keys[p.get('propertyDefinitionRef')]: p.findtext('a:value', namespaces=g.N)
                                             for p in element.findall('a:properties/a:property', g.N)}
    rows = []
    for identifier, row in derived.items():
        record = index[identifier]['record']
        g.require(all(values[identifier][role] == row[role] for role in ('friendlyName', 'canonicalName')),
                  'Published Home-derived name drift: ' + identifier)
        rows.append(dict(representationId=identifier, canonicalId=record['canonicalId'], name=record['name'],
                         type=index[identifier]['archimateType'], home=row['home'],
                         home_relationship=row['home_relationship'], semantic_context=row['semantic_context'],
                         responsibility_context=record.get('owner_key'), responsibility_semantics='UNCHANGED',
                         before={**{key: record[key] for key in ('friendlyName', 'canonicalName')},
                                 'navigation_path': list(old_locations[identifier][0])},
                         after={**{key: row[key] for key in ('friendlyName', 'canonicalName')},
                                'navigation_path': list(locations[identifier][0])}))
    counts = dict(applicable=len(rows),
                  friendlyName_changes=sum(r['before']['friendlyName'] != r['after']['friendlyName'] for r in rows),
                  canonicalName_changes=sum(r['before']['canonicalName'] != r['after']['canonicalName'] for r in rows),
                  element_relocations=sum(r['before']['navigation_path'] != r['after']['navigation_path'] for r in rows),
                  relationship_relocations=sum(old_locations[key] != locations[key] for key in edges))
    unresolved = [r for r in b.business_homes['records'] if r['status'] != 'normalised']
    g.require(len(unresolved) == 111 and all(b.ids[r['source_key']] not in derived for r in unresolved),
              'Unresolved Home population changed')
    result = dict(status='PASS', architecture_release='1.0.9', counts=counts, resources=rows,
                canonical_identities='ALL 1308 UNCHANGED', responsibility_semantics='UNCHANGED',
                unresolved_homes=dict(unestablished=93, ambiguous=18, names_and_navigation='UNCHANGED'),
                unrelated_architecture_and_all_views='UNCHANGED',
                published_sha256=hashlib.sha256(g.serialize(root)).hexdigest())
    if presented_sha256:
        result.update(home_completion_boundary_sha256=result['published_sha256'],
                      published_sha256=presented_sha256,
                      unrelated_architecture_and_all_views='UNCHANGED at Home-completion boundary; current generated View delta separately validated')
    return result


if __name__ == '__main__':
    import argparse
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('command', choices=['establish', 'audit'])
    args = parser.parse_args()
    if args.command == 'establish':
        establish()
    else:
        import synchronise_baseline as s
        _, b, _ = s.publish()
        result = audit(b, g.parse(m.model_path()))
        print(m.encoded(result).decode(), end='')
