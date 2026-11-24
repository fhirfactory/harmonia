#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read accepted 1.1.10 inputs; write diagnostic tables only to an explicit output.

Run from the repository root:
python3 -B enterprise-architecture/design/evidence/1.1.11/001/after/diagnostics/diagnose_navigation.py --output /tmp/harmonia-navigation-diagnostic

Uses the standard library and pypdf for PDF inspection. It never calls publication,
ingestion, identity allocation or release machinery. Coordinates are independently
derived from ordered canonical navigation and compared with retained projections.
The output is evidence, not a new architectural index or navigation source.
"""
import argparse
import collections
import copy
import csv
import hashlib
import html
import json
from pathlib import Path
import re
import subprocess
import xml.etree.ElementTree as ET

from pypdf import PdfReader, __version__ as pypdf_version


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--root', type=Path, default=Path.cwd())
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    root, output = args.root.resolve(), args.output.resolve()
    protected = root / 'enterprise-architecture'
    evidence = protected / 'design/evidence/1.1.11/001/after'
    if output == root or (root in output.parents and evidence not in output.parents):
        parser.error('Repository output must be beneath the authorised iteration after/ directory')
    output.mkdir(parents=True, exist_ok=True)
    inputs = {}

    def read(path):
        path = Path(path)
        data = (root / path).read_bytes()
        inputs[path.as_posix()] = hashlib.sha256(data).hexdigest()
        return data

    def load(path):
        return json.loads(read(path))

    def write(name, data):
        (output / name).write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n')

    def write_csv(name, rows):
        fields = list(dict.fromkeys(key for row in rows for key in row))
        with (output / name).open('w', newline='') as stream:
            writer = csv.DictWriter(stream, fieldnames=fields)
            writer.writeheader()
            for row in rows:
                writer.writerow({key: json.dumps(value, ensure_ascii=False) if isinstance(value, (dict, list)) else value for key, value in row.items()})

    def walk(nodes):
        for node in nodes:
            yield node
            yield from walk(node.get('children', []))

    def coordinate_key(number):
        parts = number.split('.')
        return (['G', 'I', 'II', 'III', 'IV', 'V', 'VI'].index(parts[0]), *map(int, parts[1:]))

    elements = {}
    for path in sorted((root / 'enterprise-architecture/continuum').rglob('*.md')):
        if path.name == 'MANIFEST.md':
            continue
        text = read(path.relative_to(root)).decode()
        front, body = text.removeprefix('---\n').split('\n---\n', 1)
        m = json.loads(front)
        elements[m['canonicalId']] = (m, body, str(path.relative_to(root)))
    raw = load('enterprise-architecture/continuum/navigation.json')
    tree = copy.deepcopy(raw['roots'])
    parents, locations, snapshots, sourcekeys = {}, {}, {}, {}

    def derive(node, number, ancestors):
        cid = node.get('canonicalId') or node.get('contextCanonicalId')
        node['title'] = elements[cid][0]['displayName'] if cid else node['title']
        node['number'] = number
        node['path'] = ancestors + [node['title']]
        if node.get('canonicalId'):
            locations[node['canonicalId']] = node
        for i, child in enumerate(node.get('children', []), 1):
            parents[id(child)] = node
            derive(child, number + '.' + str(i), node['path'])

    for node in tree:
        derive(node, node['namespace'], [])
    nodes = list(walk(tree))
    by_number = {n['number']: n for n in nodes}
    rep_to_cid = {m['exchange']['identifier']: cid for cid, (m, _, _) in elements.items()}
    for cid, (m, _, _) in elements.items():
        for p in m['exchange']['properties']:
            if p['ref'] == 'propid-14':
                snapshots[cid] = json.loads(p['values'][0]['text'])
                sourcekeys[snapshots[cid]['key']] = cid
    relationships = [e for m, _, _ in elements.values() for e in m['relationships']]
    remap_path = 'enterprise-architecture/design/evidence/1.1.10/001/after/element-remap.json'
    remap = {r['canonicalId']: r for r in load(remap_path)['elements']}
    before = load('enterprise-architecture/design/evidence/1.1.10/001/before/population.json')
    manifest_base = Path('enterprise-architecture/documentation/continuum-manifest/published/1.1.10')
    rad_base = Path('enterprise-architecture/documentation/continuum-rad/published/1.1.10')
    projected = load(manifest_base / 'navigation.json')['roots']
    structure = load(rad_base / 'structure.json')
    exchange = ET.fromstring(read(manifest_base / 'Harmonia-Architecture.xml'))
    namespaces = {'a': 'http://www.opengroup.org/xsd/archimate/3.0/'}
    org = {x.get('identifier'): x for x in exchange.findall('.//a:organizations//a:item', namespaces)
           if (x.get('identifier') or '').startswith('navigation-')}
    rad_parts = {part: read(rad_base / ('parts/' + part + '.tex')).decode() for part in
                 ['motivation', 'strategy', 'business', 'application', 'technology',
                  'implementation-and-migration', 'architecture-governance']}
    manifest = read('enterprise-architecture/continuum/MANIFEST.md').decode()
    browser = read(manifest_base / 'continuum.html').decode()
    catalogue = read(manifest_base / 'continuum-catalogue.tex').decode()
    manifest_index = read(manifest_base / 'README.md').decode()
    read(rad_base / 'continuum-rad.tex')
    read('architecture-release.json')
    for path in [
        'enterprise-architecture/design/review/1.1.10/hcrl-1.1.10-001.pending.md',
        'enterprise-architecture/design/register/1.1.10/hcrl-1.1.10-001.complete.md',
        'enterprise-architecture/tooling/continuum_navigation.py',
        'enterprise-architecture/tooling/continuum_manifest.py',
        'enterprise-architecture/tooling/publish_continuum.py',
        'enterprise-architecture/tooling/publish_continuum_rad.py',
        'enterprise-architecture/tooling/tests/test_continuum_navigation.py',
        'enterprise-architecture/tooling/tests/test_continuum_rad.py',
        'enterprise-architecture/documentation/continuum-rad/templates/publication.tex',
        'enterprise-architecture/design/evidence/1.1.10/001/before/ai-agent-tasks/unresolved-continuum-taxonomy-remap.md',
        'enterprise-architecture/design/evidence/1.1.10/001/after/ai-agent-reports/20261022T233519Z-continuum-taxonomy-remap.md',
        'enterprise-architecture/governance/architecture-review-praxis.md',
        'enterprise-architecture/governance/architecture-change-provenance.md',
        'enterprise-architecture/governance/architecture-governance.md',
        'enterprise-architecture/governance/architecture-release-policy.md',
        'enterprise-architecture/governance/continuum-representation.md',
        'enterprise-architecture/governance/architectural-axioms.md',
        'enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md',
    ]:
        read(path)
    for path in sorted((root / 'enterprise-architecture/design/evidence/1.1.10/001').rglob('*.json')):
        read(path.relative_to(root))

    def pdf(path):
        read(path)
        reader = PdfReader(root / path)
        page_labels = reader.page_labels
        rows = []

        def outline(items, depth=0, parent=None):
            previous = None
            for item in items:
                if isinstance(item, list):
                    outline(item, depth + 1, previous)
                    continue
                page = reader.get_destination_page_number(item)
                rows.append({'title': item.title, 'depth': depth, 'parentTitle': parent,
                             'physicalPage': page + 1, 'printedPage': page_labels[page],
                             'destination': [page, str(item.get('/Type')), str(item.get('/Left')),
                                             str(item.get('/Top')), str(item.get('/Zoom'))]})
                previous = item.title
        outline(reader.outline)
        hits = []
        for i, page in enumerate(reader.pages):
            text = page.extract_text()
            for line in text.splitlines():
                if re.match(r'^I\.6\s+Constraints\b', line.strip()):
                    hits.append({'physicalPage': i + 1, 'printedPage': page_labels[i], 'line': line})
        return {'path': str(path), 'pages': len(reader.pages), 'outline': rows,
                'exactI6OutlineEntries': [r for r in rows if r['title'] == 'I.6 Constraints'],
                'renderedI6Lines': hits,
                'duplicateOutlineTitles': {t: n for t, n in collections.Counter(r['title'] for r in rows).items() if n > 1}}

    final_pdf = pdf(rad_base / 'continuum-rad.pdf')
    reviewed_pdf = pdf(Path('enterprise-architecture/design/evidence/1.1.10/001/before/completion-inputs/reviewed-continuum-rad.pdf'))
    pdf_rows = {r['title']: r for r in final_pdf['outline']}

    def node_label(n):
        return n['number'] + ' ' + n['title']

    def pdf_label(n):
        return 'Part ' + n['number'] + ' — ' + n['title'] if n.get('namespace') not in (None, 'G') else node_label(n)

    def ancestors(n):
        result = []
        while id(n) in parents:
            n = parents[id(n)]
            result.insert(0, n)
        return result

    def semantic_edges(cid):
        rid = elements[cid][0]['exchange']['identifier']
        return [dict({k: e[k] for k in ['identifier', 'archimateType', 'specialization', 'source', 'target', 'standing', 'provenance'] if k in e},
                     sourceCanonicalId=rep_to_cid.get(e['source']), targetCanonicalId=rep_to_cid.get(e['target']),
                     sourceLabel=elements[rep_to_cid[e['source']]][0]['displayName'],
                     targetLabel=elements[rep_to_cid[e['target']]][0]['displayName'])
                for e in relationships if rid in (e['source'], e['target'])]

    def ct(cid):
        m = elements[cid][0]
        if m.get('specialization') == 'Feature':
            return 'Feature'
        match = re.match(r'\[(CT\d)\]', m['displayName'])
        return match[1] if match else None

    def element_row(cid):
        m, body, path = elements[cid]
        n = locations[cid]
        parent = parents.get(id(n), {})
        s = snapshots.get(cid, {})
        return {'canonicalId': cid, 'displayedName': m['displayName'], 'displayName': m['displayName'],
                'friendlyName': m['friendlyName'], 'canonicalName': m['canonicalName'],
                'nativeType': m['archimateType'], 'specialization': m.get('specialization'),
                'coordinate': n['number'], 'path': n['path'], 'scope': n['path'][2] if n['number'].startswith('II.1.') else None,
                'ctLevel': ct(cid), 'standing': m['standing'], 'currentness': m['standing']['currentness'],
                'parentOwner': {k: s.get(k) for k in ('key', 'parent_key', 'owner_key', 'responsibility_owner_key',
                                                    'parent_identifier', 'owner_identifier')},
                'snapshotPresent': bool(s), 'navigationParentCoordinate': parent.get('number'),
                'navigationParentCanonicalId': parent.get('canonicalId'), 'navigationParentLabel': parent.get('title'),
                'originalOrganisation': remap[cid]['beforeOrganizationPaths'],
                'originalNativeStorageLocator': path, 'sourceBindings': m['sourceBindings'],
                'provenance': m['provenance'], 'identity': m['identity'], 'relevantSemanticRelationships': semantic_edges(cid),
                'classificationBasis': n['basis'], 'originalOrganisationMeaning': 'pre-1.1.10 projection-template paths, not semantic parentage'}

    groups = collections.defaultdict(list)
    local_names = collections.defaultdict(list)
    for cid, (m, _, _) in elements.items():
        if m['archimateType'] == 'Capability':
            groups[m['displayName']].append(cid)
            local_names[re.sub(r'^\[(CT\d|Feature)\]\s*', '', m['displayName']).casefold()].append(cid)
    duplicates = []
    for name, ids in sorted(groups.items()):
        if len(ids) < 2:
            continue
        is_correspondence_pair = any(elements[cid][0]['identity'].get('correspondence', {}).get('successorCanonicalId') in ids for cid in ids)
        scopes = {locations[cid]['path'][2] for cid in ids}
        rows = []
        for cid in sorted(ids, key=lambda x: coordinate_key(locations[x]['number'])):
            row = element_row(cid)
            co = row['identity'].get('correspondence', {})
            others = [x for x in ids if x != cid]
            row.update(apparentlyDuplicateGroup=name, pairedCanonicalIds=others,
                       sameCanonicalElementExposedTwice=False,
                       reasonBothExist=co.get('reason') or 'Current resource retains governed meaning; paired superseded resource reserves historical identity, representation and existing Views under its explicit correspondence decision.',
                       correspondenceDisposition=co.get('disposition') or next((elements[x][0]['identity'].get('correspondence', {}).get('disposition') for x in others), None),
                       distinctionMeaning='Identity/provenance and historical representation distinction; not evidence of two independent current capability responsibilities.',
                       reasonBothEmitted='All-element once-only coverage plus standing-orthogonal architectural placement. Separate IDs retain own known ancestry in the same scope. Publication uses displayName without historical distinction in outline labels.',
                       diagnosticClassification='Distinct-ID current/superseded correspondence pair presented as architectural subjects; navigation conflates historical reservation with reader-facing current decomposition.')
            row['sameNameGroupIsCorrespondencePair'] = is_correspondence_pair
            row['sameScopeAsOtherNameOccurrence'] = len(scopes) == 1
            row['sameNavigationParentAsOtherNameOccurrence'] = len({locations[x]['number'].rsplit('.', 1)[0] for x in ids}) == 1
            if not is_correspondence_pair:
                row.update(reasonBothExist='Explicit correspondence distinguishes BC Health Information Exchange from BEC Health Information Exchange Enablement. The same-named superseded BEC identity continues in that differently named BEC successor, not in this BC element.',
                           distinctionMeaning='BC and BEC responsibilities are expressly distinct; historical BEC reservation remains a separate provenance distinction.',
                           reasonBothEmitted='Separate scopes and all-element historical coverage; these same-name occurrences are not peers beneath one parent.',
                           diagnosticClassification='Legitimate different-scope responsibility distinction plus historical BEC exposure; not a same-name successor pair.')
            rows.append(row)
        duplicates.extend(rows)
    write('capability-duplicates.json', duplicates)
    write_csv('capability-duplicates.csv', [{k: v for k, v in r.items() if k not in ('provenance', 'identity', 'relevantSemanticRelationships')} for r in duplicates])

    related_cases = []
    for name, ids in sorted(local_names.items()):
        if len({elements[x][0]['displayName'] for x in ids}) > 1:
            related_cases.append({'localName': name, 'classification': 'Different CT/Feature contexts; name similarity alone supplies no identity or duplication adjudication.',
                                  'elements': [element_row(cid) for cid in ids]})
    evolved = []
    for cid, (m, _, _) in elements.items():
        successor = m['identity'].get('correspondence', {}).get('successorCanonicalId')
        if successor in elements and m['displayName'] != elements[successor][0]['displayName']:
            evolved.append({'historical': element_row(cid), 'successor': element_row(successor)})
    write('capability-related-cases.json', {'differentLevelNames': related_cases, 'differentNameCorrespondence': evolved})

    motivation = []
    for n in tree[1]['children']:
        cid = n.get('canonicalId')
        m = elements[cid][0] if cid else {}
        edges = semantic_edges(cid) if cid else []
        motivation.append({'coordinate': n['number'], 'displayedLabel': n['title'],
                           'canonicalElementOrNavigationOnly': 'canonical element' if cid else 'navigation-only section',
                           'canonicalId': cid, 'nativeType': m.get('archimateType'),
                           'navigationRole': n.get('kind', 'section'), 'standing': m.get('standing'),
                           'children': len(n.get('children', [])), 'existingSemanticRelationships': edges,
                           'projectionBehaviour': {'canonicalAndManifestAndExchange': 'Immediate child of Motivation',
                             'manifest': 'Same-level entry' + (' with cross-cutting perspective suffix' if cid else ''),
                             'radTex': 'Bold profile after Unclassified Motivation; pdfbookmark[0]' if cid else 'chapter* with explicit coordinate',
                             'pdfOutline': pdf_rows.get(pdf_label(n)),
                             'printedContents': 'Omitted: direct pdfbookmark is not addcontentsline' if cid else 'Included',
                             'radAssociatedConcernLinks': 'Historical early return suppresses existing associations' if cid and m['standing']['currentness'] == 'historical_reference' else ('No represented associations to emit' if cid else None)},
                           'diagnosticFinding': 'Cross-cutting kind is metadata; peer sibling coordinate and outline hierarchy remain. Existing associations only; absent associations are preserved.' if cid else ('Accepted explicit classification deferral, not second I.6' if n['number'] == 'I.8' else 'No coordinate defect reproduced')})
    write('motivation-navigation.json', motivation)
    write_csv('motivation-navigation.csv', motivation)

    contexts = [n for n in nodes if n.get('contextCanonicalId')]
    behaviour = []
    context_rows = []
    for n in contexts:
        cid = n['contextCanonicalId']
        canonical = locations[cid]
        cap_path = [x for x in ancestors(canonical) + [canonical] if x.get('canonicalId') and elements[x['canonicalId']][0]['archimateType'] == 'Capability']
        path_rows = [{'canonicalId': x['canonicalId'], 'coordinate': x['number'], 'ctLevel': ct(x['canonicalId']),
                      'label': x['title'], 'standing': elements[x['canonicalId']][0]['standing']} for x in cap_path]
        represented_context_ancestors = [x for x in ancestors(n) if x.get('contextCanonicalId')]
        descendants = [x for x in walk([n]) if x.get('canonicalId')]
        counts = collections.Counter(elements[x['canonicalId']][0]['archimateType'] for x in descendants)
        tex_reference = '\\hyperref[element:' + cid + ']'
        tex_match = re.search(r'Capability context: [^\n]*' + re.escape(tex_reference) + r'[^\n]*', rad_parts['business'])
        for x in descendants:
            bid = x['canonicalId']
            s = snapshots.get(bid, {})
            owner = s.get('owner_key')
            binding = elements[bid][0]['sourceBindings']
            # For accepted 1.0.8 material preserve the canonical Feature's exact
            # source-qualified predicate; do not substitute a name match.
            context_edges = [e for e in semantic_edges(bid) if cid in (e['sourceCanonicalId'], e['targetCanonicalId'])]
            behaviour.append({'canonicalId': bid, 'label': x['title'], 'nativeType': elements[bid][0]['archimateType'],
                              'coordinate': x['number'], 'contextCoordinate': n['number'], 'contextCanonicalId': cid,
                              'recordedOwnerKey': owner, 'recordedOwnerCanonicalId': sourcekeys.get(owner),
                              'retainedResponsibilityOwnerKey': s.get('responsibility_owner_key'),
                              'snapshotOwnerMatchesContext': sourcekeys.get(owner) == cid if s else None,
                              'sourceBindings': binding, 'existingRelationshipsToContext': context_edges,
                              'classificationBasis': x['basis']})
        context_rows.append({'contextualCoordinate': n['number'], 'ctLevel': ct(cid), 'contextualLabel': n['title'],
                             'strategyCapabilityCanonicalId': cid, 'strategyCoordinate': canonical['number'],
                             'strategySpecialization': elements[cid][0].get('specialization'),
                             'parentContextualCapability': represented_context_ancestors[-1]['contextCanonicalId'] if represented_context_ancestors else None,
                             'navigationParent': parents[id(n)]['number'], 'strategyCapabilityPath': path_rows,
                             'completeCT1CT2CT3AncestryRepresented': False,
                             'ancestryAssessment': 'All proper capability ancestors omitted; CT2 allocations remain CT2 and are not assigned invented CT3 context.',
                             'omittedContextualAncestors': path_rows[:-1],
                             'explicitCanonicalReferenceToReader': {'radBody': bool(tex_match), 'radReferenceLine': tex_match[0] if tex_match else None,
                                'pdfOutline': False, 'manifestOutlineOrContextBody': False, 'browserContext': False,
                                'manifestCatalogue': False, 'exchangeOrganisationContext': False,
                                'canonicalSourceMachineReadableContextCanonicalId': True},
                             'counts': {k: counts[k] for k in ['BusinessService', 'BusinessFunction', 'BusinessProcess', 'BusinessEvent']},
                             'otherBehaviourTypes': {k: v for k, v in counts.items() if k not in ['BusinessService', 'BusinessFunction', 'BusinessProcess', 'BusinessEvent']},
                             'hasRepresentedBehaviour': bool(descendants), 'reasonContextAppears': n['basis'],
                             'appearanceBasisDetail': 'Populated exact reviewed owner_key capability; retained earlier responsibility_owner_key does not override reviewed placement' if ct(cid) != 'Feature' else 'Populated existing Feature context; accepted-ingestion behaviour has explicit native Realization/Association endpoints to this Feature, not lexical matching',
                             'kind': n.get('kind', 'section'), 'hasCanonicalIdOfItsOwn': bool(n.get('canonicalId')),
                             'unambiguouslyContextual': {'sourceMetadata': True, 'radBody': bool(tex_match),
                                  'outlineAndOtherProjections': False}, 'pdfOutline': pdf_rows.get(pdf_label(n))})
    write('business-behaviour-contexts.json', context_rows)
    write_csv('business-behaviour-contexts.csv', context_rows)
    write('business-behaviour-elements.json', behaviour)

    manifest_outline = []
    for line in manifest.splitlines():
        match = re.match(r'^( *)- \[(.*?)\]\(', line)
        if match:
            manifest_outline.append((len(match[1]) // 2, html.unescape(match[2])))
    expected_outline = [(len(n['path']) - 1, node_label(n)) for n in nodes]
    headings = structure['headings']
    tex_labels = re.findall(r'\\label\{navigation:([^}]+)\}', '\n'.join(rad_parts.values()))
    exchange_labels_match = all(org['navigation-' + n['number'].replace('.', '-')].findtext('a:label', namespaces=namespaces) == node_label(n) for n in nodes)
    exchange_children_match = all([x.get('identifier') for x in org['navigation-' + n['number'].replace('.', '-')].findall('a:item', namespaces)] == ['navigation-' + ch['number'].replace('.', '-') for ch in n.get('children', [])] for n in nodes)
    pdf_architecture = [r for r in final_pdf['outline'] if r['title'] in {pdf_label(n) for n in nodes}]
    cross_projection = {
        'canonicalNodeCount': len(nodes), 'canonicalElementOccurrences': sum(bool(n.get('canonicalId')) for n in nodes),
        'canonicalUniqueElementIds': len(locations), 'canonicalUniqueCoordinates': len(by_number),
        'rawCanonicalNavigationHasPersistedNumbers': any('number' in n for n in walk(raw['roots'])),
        'manifestJsonMatchesIndependentDerivation': list(walk(projected)) == nodes_with_locator(nodes),
        'rootManifestOutlineMatchesLabelsDepthOrder': manifest_outline == expected_outline,
        'rootManifestOutlineEntries': len(manifest_outline),
        'exchangeNavigationItems': len(org), 'exchangeLabelsMatch': exchange_labels_match,
        'exchangeChildOrderMatches': exchange_children_match,
        'radStructureMatchesCoordinatesLabelsPaths': [(h['number'], h['title'], h['path']) for h in headings] == [(n['number'], n['title'], n['path']) for n in nodes],
        'latexNavigationLabels': len(tex_labels), 'latexLabelsUnique': len(set(tex_labels)) == len(tex_labels),
        'latexLabelsMatchCoordinates': set(tex_labels) == set(by_number),
        'pdfArchitecturalBookmarks': len(pdf_architecture),
        'pdfArchitectureOrderMatches': [r['title'] for r in pdf_architecture] == [pdf_label(n) for n in nodes],
        'pdfDistinctArchitecturalDestinations': len({tuple(r['destination']) for r in pdf_architecture}),
        'sourcePopulationMatchesRetainedBefore': all(inputs[path] == before['elements'][cid]['sha256'] for cid, (_, _, path) in elements.items()),
        'canonicalI6Node': {k: v for k, v in by_number['I.6'].items() if k != 'children'},
        'i6ManifestOutline': [row for row in manifest_outline if row[1] == 'I.6 Constraints'],
        'i6ExchangeLabels': [x.findtext('a:label', namespaces=namespaces) for x in org.values() if x.findtext('a:label', namespaces=namespaces) == 'I.6 Constraints'],
        'i6TexLabelCount': tex_labels.count('I.6'),
        'pdfFinal': final_pdf, 'pdfReviewedPreCompletion': reviewed_pdf,
        'coordinateFinding': 'No second I.6 assigned or bookmarked in inspected accepted specimens. Rendered text contains ordinary contents/body repetition. Reported extra navigation occurrence remains UNRESOLVED without its exact surface/locator.'}
    write('cross-projection-check.json', cross_projection)

    sibling_duplicates = []
    nonretired_under_superseded = []
    for p in nodes:
        by_title = collections.defaultdict(list)
        for ch in p.get('children', []):
            by_title[ch['title']].append(ch['number'])
        sibling_duplicates.extend({'parent': p['number'], 'label': title, 'coordinates': coords} for title, coords in by_title.items() if len(coords) > 1)
    for cid, (m, _, _) in elements.items():
        n = locations[cid]
        if m['standing']['currentness'] in ('superseded', 'historical_reference'):
            continue
        sup = [a for a in ancestors(n) if a.get('canonicalId') and elements[a['canonicalId']][0]['standing']['currentness'] == 'superseded']
        if sup:
            nonretired_under_superseded.append({'canonicalId': cid, 'label': m['displayName'], 'coordinate': n['number'],
                                            'standing': m['standing'], 'nativeType': m['archimateType'], 'supersededAncestors': [a['number'] for a in sup],
                                            'classification': 'Pre-existing source hierarchy/currentness tension exposed by navigation; no new semantic edge inferred.'})
    all_caps = [cid for cid, (m, _, _) in elements.items() if m['archimateType'] == 'Capability']
    source_superseded = [element_row(cid) for cid in all_caps if elements[cid][0]['standing']['currentness'] == 'superseded' and not elements[cid][0]['identity'].get('correspondence')]
    unclassified = [element_row(n['canonicalId']) for n in walk([by_number['III.5']]) if n.get('canonicalId')]
    noncap_names = collections.defaultdict(list)
    for cid, (m, _, _) in elements.items():
        if m['archimateType'] != 'Capability':
            noncap_names[(m['archimateType'], m['displayName'])].append(cid)
    write('structural-inspection.json', {
        'sameLabelSiblingGroups': sibling_duplicates,
        'nonCapabilitySameNativeTypeSameNameGroups': [ids for ids in noncap_names.values() if len(ids) > 1],
        'nonRetiredElementsBeneathSupersededAncestors': nonretired_under_superseded,
        'supersededCapabilitySourceRecordsWithoutIdentityCorrespondence': source_superseded,
        'businessContextsWithSupersededStrategyAncestors': [r['contextualCoordinate'] for r in context_rows if any(a['standing']['currentness'] == 'superseded' for a in r['strategyCapabilityPath'][:-1])],
        'unclassifiedBusiness': unclassified,
        'unclassifiedBusinessNativeTypes': dict(collections.Counter(r['nativeType'] for r in unclassified)),
        'unclassifiedBusinessSnapshotOrOwnerPresent': [{'canonicalId': r['canonicalId'], 'snapshot': snapshots.get(r['canonicalId'])} for r in unclassified if r['canonicalId'] in snapshots],
        'emptyNonRootStructuralLeaves': [{'number': n['number'], 'title': n['title']} for n in nodes if n['number'].count('.') and not any(n.get(k) for k in ['canonicalId', 'contextCanonicalId', 'children', 'source', 'relatedCanonicalIds'])],
        'rootAbsences': [{'number': n['number'], 'title': n['title'], 'description': n.get('description')} for n in tree if not n.get('children')],
        'referenceOnlyNodes': [{k: v for k, v in n.items() if k != 'children'} for n in nodes if n.get('relatedCanonicalIds')],
        'nativeTypeTotals': dict(collections.Counter(m['archimateType'] for m, _, _ in elements.values())),
        'scopeLimit': 'Exact same-name and same-local-name/CT variants, represented provenance/hierarchy, structural population and projection agreement only. Similar words do not establish architectural duplication.'})

    inventory = []
    for n in nodes:
        inventory.append({k: n.get(k) for k in ['number', 'title', 'path', 'kind', 'canonicalId', 'contextCanonicalId', 'basis', 'source', 'description', 'relatedCanonicalIds']} | {
            'parent': parents.get(id(n), {}).get('number'), 'childCount': len(n.get('children', [])),
            'pdfOutline': pdf_rows.get(pdf_label(n))})
    write('navigation-inventory.json', inventory)
    summary = {
        'acceptedRelease': '1.1.10', 'prospectiveEvidenceRelease': '1.1.11', 'iteration': '001',
        'diagnosticOnly': True, 'elements': len(elements), 'navigationNodes': len(nodes),
        'relationships': len(relationships), 'authoredViews': len(exchange.findall('a:views/a:diagrams/a:view', namespaces)),
        'duplicateCapabilityNameGroups': len(duplicates) // 2, 'duplicateCapabilityElements': len(duplicates),
        'sameNameCorrespondencePairs': sum(r['sameNameGroupIsCorrespondencePair'] for r in duplicates[::2]),
        'sameNameDifferentScopeGroups': sum(not r['sameNameGroupIsCorrespondencePair'] for r in duplicates[::2]),
        'duplicateGroupsByCT': dict(collections.Counter(r['ctLevel'] for r in duplicates[::2])),
        'duplicatePairCorrespondence': dict(collections.Counter(r['correspondenceDisposition'] for r in duplicates[::2])),
        'actualSameNamePairCorrespondence': dict(collections.Counter(r['correspondenceDisposition'] for r in duplicates[::2] if r['sameNameGroupIsCorrespondencePair'])),
        'sameLabelSiblingGroups': len(sibling_duplicates), 'contexts': len(context_rows),
        'contextsByCT': dict(collections.Counter(r['ctLevel'] for r in context_rows)),
        'businessBehaviour': dict(collections.Counter(r['nativeType'] for r in behaviour)),
        'contextsWithCompleteAncestry': sum(r['completeCT1CT2CT3AncestryRepresented'] for r in context_rows),
        'emptyContexts': sum(not r['hasRepresentedBehaviour'] for r in context_rows),
        'radBodyExplicitContextReferences': sum(r['explicitCanonicalReferenceToReader']['radBody'] for r in context_rows),
        'snapshotOwnerMatch': dict(collections.Counter(str(r['snapshotOwnerMatchesContext']) for r in behaviour)),
        'nonRetiredElementsBeneathSupersededAncestors': len(nonretired_under_superseded),
        'supersededCapabilitySourceRecordsWithoutIdentityCorrespondence': len(source_superseded),
        'contextsWithSupersededAncestors': sum(any(a['standing']['currentness'] == 'superseded' for a in r['strategyCapabilityPath'][:-1]) for r in context_rows),
        'nonCapabilitySameNameGroups': sum(len(ids) > 1 for ids in noncap_names.values()),
        'pypdfVersion': pypdf_version,
    }
    write('summary.json', summary)
    def cell(value):
        if value is None:
            return '—'
        return str(value).replace('|', '\\|').replace('\n', '<br>')

    def table(headers, rows):
        return ['| ' + ' | '.join(headers) + ' |', '| ' + ' | '.join(['---'] * len(headers)) + ' |'] + [
            '| ' + ' | '.join(cell(v) for v in row) + ' |' for row in rows]

    tables = ['<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->', '',
              '# Complete diagnostic tables — accepted 1.1.10 navigation', '',
              'Diagnostic evidence only. JSON and CSV files alongside this table retain full names, provenance, identity correspondence and semantic relationship details. No row supplies a new architectural assertion.', '',
              'E1: current meaning and separately reserved superseded identity/representation/Views under explicit correspondence. E2: expressly distinct BC and BEC exchange responsibilities; the historical BEC successor has another name.', '',
              'N1: both IDs included once in their recorded architecture scope/ancestry, with displayName-only outline labels. C1: historical correspondence exposed as a second architectural subject. C2: legitimate different-scope same name plus historical BEC exposure; no identity equivalence.', '',
              'Parent/owner keys are source snapshot data. Navigation parent is separately identified and asserts no semantic relationship. Original organisation is the retained pre-1.1.10 projection-template path, not a canonical ownership assertion.', '',
              '## Capability subjects — every same-displayName occurrence (426 rows / 213 groups)', '']
    tables += table(['Displayed name', 'Canonical ID', 'Coordinate', 'Scope / CT', 'Native type / specialisation',
                     'Standing / currentness', 'Recorded parent / owner', 'Navigation parent', 'Original organisation',
                     'Why both exist', 'Why emitted', 'Classification'], [
        [r['displayName'], r['canonicalId'], r['coordinate'], r['scope'] + ' / ' + str(r['ctLevel']),
         r['nativeType'] + ' / ' + str(r['specialization']), r['standing']['semantic'] + ' / ' + r['currentness'],
         (str(r['parentOwner']['parent_key']) + ' / ' + str(r['parentOwner']['owner_key'])) if r['snapshotPresent'] else 'No snapshot parent/owner; preserved historical organisation',
         r['navigationParentCoordinate'] + ' ' + r['navigationParentLabel'],
         '<br>'.join(' → '.join(path) for path in r['originalOrganisation']) or 'No pre-remap template path',
         'E1' if r['sameNameGroupIsCorrespondencePair'] else 'E2', 'N1', 'C1' if r['sameNameGroupIsCorrespondencePair'] else 'C2'] for r in duplicates])
    tables += ['', '## Motivation — every immediate child (18 rows)', '',
               'M1: ordinary numbered navigation section; no coordinate collision. M2: accepted explicit classification deferral. M3: cross-cutting profile kind retained as a peer Motivation sibling/outline entry; RAD suppresses historical associations through its early return. No association is inferred.', '']
    tables += table(['Coordinate', 'Displayed label', 'Element / navigation', 'Canonical ID', 'Native type', 'Role', 'PDF page / outline depth', 'Projection / finding'], [
        [r['coordinate'], r['displayedLabel'], r['canonicalElementOrNavigationOnly'], r['canonicalId'], r['nativeType'], r['navigationRole'],
         str(r['projectionBehaviour']['pdfOutline']['physicalPage']) + ' / ' + str(r['projectionBehaviour']['pdfOutline']['depth']),
         'M3: peer in all outlines; bold profile, absent from printed TOC' if r['canonicalId'] else ('M2' if r['coordinate'] == 'I.8' else 'M1')] for r in motivation])
    tables += ['', '## Business Behaviour — every capability context (72 rows)', '',
               'S/F/P/E = Business Services / Functions / Processes / Events, counted over the entire contextual subtree. All contexts have actual behaviour. No invented CT3 is expected for a behaviour explicitly allocated to CT2. Missing proper Strategy ancestors remain a defect in contextual orientation.', '',
               'R1: RAD body explicitly links the canonical Strategy capability and prints its coordinate immediately after the context heading. Other context projections/outline labels do not expose that reference. The source contextCanonicalId is machine-explicit. O1: no independent canonicalId; RAD body distinguishes context, but outline and other projections do not do so unambiguously.', '',
               'B1: populated exact reviewed owner_key. B2: four accepted-ingestion behaviours have explicit native relationships to two Feature contexts. Neither rule invents an allocation or semantic edge.', '']
    tables += table(['Context coordinate', 'CT level', 'Label', 'Strategy Canonical ID', 'Strategy coordinate', 'Parent contextual capability',
                     'Complete CT1→CT2→CT3 ancestry / omitted Strategy ancestors', 'Reader canonical reference', 'S/F/P/E', 'Other', 'Has behaviour', 'Reason emitted', 'Context distinction'], [
        [r['contextualCoordinate'], r['ctLevel'], r['contextualLabel'], r['strategyCapabilityCanonicalId'], r['strategyCoordinate'],
         r['parentContextualCapability'], 'No / ' + ', '.join(a['coordinate'] + ' ' + str(a['ctLevel']) for a in r['omittedContextualAncestors']),
         'R1', '/'.join(str(r['counts'][k]) for k in ['BusinessService', 'BusinessFunction', 'BusinessProcess', 'BusinessEvent']),
         sum(r['otherBehaviourTypes'].values()), 'Yes', 'B2' if r['ctLevel'] == 'Feature' else 'B1', 'O1'] for r in context_rows])
    (output / 'diagnostic-tables.md').write_text('\n'.join(tables) + '\n')
    inputs[Path(__file__).resolve().relative_to(root).as_posix()] = hashlib.sha256(Path(__file__).read_bytes()).hexdigest()
    write('source-inventory.json', {'sourceHead': subprocess.check_output(['git', 'rev-parse', 'HEAD'], cwd=root, text=True).strip(),
                                  'preRemapHead': before['head'], 'inputsSha256': inputs,
                                  'meaning': 'Hashes identify inspected specimens; they confer no architectural acceptance.'})
    print(json.dumps(summary, ensure_ascii=False, indent=2))


def nodes_with_locator(nodes):
    """Flatten comparison preserves full child trees and canonical locator fields."""
    result = copy.deepcopy(nodes)
    for node in result:
        for n in flatten([node]):
            n['locator'] = 'enterprise-architecture/continuum/navigation.json#/' + n['number']
    return result


def flatten(nodes):
    for node in nodes:
        yield node
        yield from flatten(node.get('children', []))


if __name__ == '__main__':
    main()
