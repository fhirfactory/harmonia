#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only checks and inspectable tree extracted from actual Archi native saves.

Run from repository root. This evidence program does not generate publications,
alter native models or confer architectural acceptance. Output is a separate
temporary directory by default; retained evidence is never silently overwritten.
"""
import argparse
from collections import Counter
import gzip
import hashlib
import html
import json
from pathlib import Path
import sys
from types import SimpleNamespace
import xml.etree.ElementTree as ET

sys.path.insert(0, str(Path.cwd() / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_navigation as nav
import continuum_workbench as w
from exchange_signatures import signature
from exchange_xml import load_schema

N = c.N
XSI = c.XSI
HERE = Path(__file__).resolve().parent
BASELINE = Path('enterprise-architecture/design/archimate/published/1.1.11/Harmonia-Architecture.xml')
PUBLISHED = Path('enterprise-architecture/design/archimate/published/1.1.13/Harmonia-Architecture.xml')
OLD_NATIVE = Path('enterprise-architecture/design/evidence/1.1.13/001/after/diagnostics/published-imported.archimate.gz')


def read(path):
    data = path.read_bytes()
    return gzip.decompress(data) if path.suffix == '.gz' else data


def digest(data):
    return hashlib.sha256(data).hexdigest()


def organisation(root):
    rows = []

    def visit(parent, path):
        for item in parent.findall('a:item', N):
            label = item.findtext('a:label', namespaces=N)
            folder = not item.get('identifierRef') or bool(item.findall('a:item', N))
            row = {'id': item.get('identifier'), 'ref': item.get('identifierRef'), 'label': label,
                   'folderPath': path, 'children': len(item.findall('a:item', N))}
            rows.append(row)
            visit(item, path + ([label] if label and folder else []))
    for group in root.findall('a:organizations', N):
        visit(group, [])
    return rows


def native(root):
    folders, objects = {}, {}

    def visit(parent, path):
        for folder in parent.findall('folder'):
            parts = path + (folder.get('name'),)
            assert parts not in folders, 'Ambiguous native folder path'
            folders[parts] = {'id': folder.get('id'), 'documentation': folder.findtext('documentation') or '',
                              'type': folder.get('type')}
            for element in folder.findall('element'):
                eid = element.get('id')
                assert eid not in objects, 'Repeated native object identity'
                objects[eid] = {'path': list(parts) + [element.get('name')], 'type': element.get(XSI)}
            visit(folder, parts)
    visit(root, ())
    return folders, objects


def records(root, xpath):
    model = SimpleNamespace(keys={d.get('identifier'): d.findtext('a:name', namespaces=N)
                                  for d in root.findall('a:propertyDefinitions/a:propertyDefinition', N)})
    return {e.get('identifier'): signature(e, model) for e in root.findall(xpath, N)}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, default=Path('/tmp/harmonia-workbench-check'))
    args = parser.parse_args()
    args.output.mkdir(parents=True, exist_ok=True)
    old, before = w.model(BASELINE)
    projected, current = w.model(PUBLISHED)
    first, returned = w.model(HERE / 'returned.xml.gz')
    second, returned_again = w.model(HERE / 'second.xml.gz')
    assert len(before) == 1602 and before == current == returned == returned_again
    schema = load_schema(c.ROOT / c.SCHEMAS / 'archimate3_Diagram.xsd')
    for model in (projected, first, second):
        schema.assertValid(model)
    signatures = {}
    for xpath, expected in [('a:relationships/a:relationship', 1651), ('a:views/a:diagrams/a:view', 1209)]:
        populations = [records(model, xpath) for model in (old, projected, first, second)]
        assert all(len(population) == expected for population in populations)
        assert all(population == populations[0] for population in populations)
        signatures[xpath] = {'count': expected, 'allExactSignaturesPreserved': True}
    # Independently check the maintained signature helper selects actual Views.
    assert len(w.signatures(projected, 'views/diagrams', 'view')) == 1209
    old_folders, old_objects = native(ET.fromstring(read(OLD_NATIVE)))
    folders, objects = native(ET.fromstring(read(HERE / 'published.archimate.gz')))
    second_folders, second_objects = native(ET.fromstring(read(HERE / 'second.archimate.gz')))
    assert objects == second_objects
    assert {k: v['documentation'] for k, v in folders.items()} == {k: v['documentation'] for k, v in second_folders.items()}
    canonical = {e['identifier']: cid for cid, e in current.items()}
    view_ids = set(records(projected, 'a:views/a:diagrams/a:view'))
    rows = organisation(projected)
    placements = {}
    for model in (projected, first, second):
        items = organisation(model)
        refs = [r['ref'] for r in items if r['ref'] in canonical]
        assert Counter(refs) == Counter(canonical.keys())
        assert not any(r['children'] and r['ref'] in canonical for r in items)
    for row in rows:
        if row['ref'] in canonical:
            eid = row['ref']
            expected_path = row['folderPath'] + [current[canonical[eid]]['name']]
            assert objects[eid]['path'] == expected_path, (eid, expected_path, objects[eid])
            placements[eid] = expected_path
    assert len(placements) == 1602
    assert not any(len(objects[eid]['path']) == 2 for eid in canonical)
    # Every generated navigation-only folder survives with its documentation.
    navigation_folders = [r for r in rows if r['id'] and r['id'].startswith('navigation-') and not r['ref']]
    for row in navigation_folders:
        path = tuple(row['folderPath'] + [row['label']])
        assert path in folders, path
        assert folders[path]['documentation'], path
    original_parents = [r for r in organisation(old) if r['ref'] in canonical and r['children']]
    assert len(original_parents) == 218
    parent_rows = []
    for row in original_parents:
        eid = row['ref']; cid = canonical[eid]
        assert len(old_objects[eid]['path']) == 2
        assert objects[eid]['path'][-2] == row['label']
        parent_rows.append({'canonicalId': cid, 'exchangeId': eid, 'nativeType': current[cid]['archimateType'],
                            'before': old_objects[eid]['path'], 'after': objects[eid]['path']})
    parent_types = Counter(r['nativeType'] for r in parent_rows)
    assert parent_types == Counter(Capability=204, DataObject=11, Driver=3)
    # Leaves are not wrapped. R's native-layer-qualified index label is an
    # intentional context change, as are the four relocated ValueStreams.
    parent_ids = {r['exchangeId'] for r in parent_rows}
    for eid, cid in canonical.items():
        if eid not in parent_ids and current[cid]['archimateType'] != 'ValueStream':
            previous, actual = old_objects[eid]['path'], objects[eid]['path']
            if previous[1].startswith('R Cross-Cutting References'):
                assert len(previous) == len(actual) and previous[2:] == actual[2:]
                assert actual[1].startswith(previous[1]) and 'not decomposition' in actual[1]
            else:
                assert actual == previous, eid
    # All authored Views, their folders, and their organisation items are exact.
    for eid in view_ids:
        assert old_objects[eid] == objects[eid] == second_objects[eid]
    view_paths = {p for p in folders if p[0] == 'Views'}
    assert view_paths == {p for p in old_folders if p[0] == 'Views'}
    assert len(view_paths) == 1032
    template = c.secure_xml(c.ROOT / c.DESIGN / 'projection-template.xml')
    template_rows = [r for r in organisation(template) if r['ref'] in view_ids]
    assert len(template_rows) == 1209
    for row in template_rows:
        assert objects[row['ref']]['path'][:-1] == row['folderPath']
    catalogue, elements = c.load()
    tree = nav.load(elements=elements)
    contexts = [n for n in nav.walk(tree) if n.get('contextCanonicalId')]
    business = [n for n in nav.walk(tree) if n['number'].startswith('III.4')]
    for node in business:
        cid = node.get('canonicalId')
        if cid:
            eid = elements[cid][0]['exchange']['identifier']
            assert old_objects[eid]['path'] == objects[eid]['path']
    for node in contexts:
        context = next(r for r in rows if r['id'] == nav.transport_id(node))
        assert not context['ref']
        data = folders[tuple(context['folderPath'] + [context['label']])]
        assert node['contextCanonicalId'] in data['documentation']
    streams = [e for cid, e in current.items() if e['archimateType'] == 'ValueStream']
    assert len(streams) == 4
    for stream in streams:
        path = objects[stream['identifier']]['path']
        assert path[0] == 'Strategy' and 'Business navigation reference' in path[1]
        assert len(path) == 3 and objects[stream['identifier']]['type'] == 'archimate:ValueStream'
    assert not any(p[:2] == ('Strategy', 'III Business') for p in folders)
    layers = {p[0]: sum(objects[eid]['path'][0] == p[0] for eid in canonical)
              for p in folders if len(p) == 1}
    result = {'status': 'PASS', 'standing': 'Observed engineering results; pending Solution Architect inspection and acceptance',
              'canonicalElementsExactIncludingAllProperties': 1602, 'signatures': signatures,
              'xsdPublicationAndBothReturns': 'PASS', 'singleLeafPlacementForEveryCanonicalElement': True,
              'diagnosedParentsRemediated': len(parent_rows), 'parentTypes': dict(parent_types),
              'leavesUnwrappedWithOnlyValueStreamAndQualifiedReferenceIndexContextChanges': True, 'canonicalElementsDetachedAtNativeRoots': 0,
              'contextFoldersWithCanonicalMetadata': len(contexts), 'businessBehaviourContexts': sum(n.get('contextRole') == 'capability' for n in contexts),
              'allGeneratedNavigationFoldersRetained': len(navigation_folders),
              'viewsAndOrganisationPreserved': 1209, 'viewFoldersPreserved': len(view_paths),
              'valueStreamsNativeStrategy': 4, 'foreignBusinessRootUnderStrategy': False,
              'secondPassAllObjectTypesAndPathsStable': True, 'secondPassAllFolderPathsAndDocumentationStable': True,
              'firstAndSecondReturnedXmlByteIdentical': read(HERE / 'returned.xml.gz') == read(HERE / 'second.xml.gz'),
              'layersCanonicalElements': layers,
              'hashes': {str(p): digest(read(p)) for p in (BASELINE, PUBLISHED, HERE / 'published.archimate.gz', HERE / 'returned.xml.gz', HERE / 'second.archimate.gz', HERE / 'second.xml.gz')},
              'limits': ['Native folder representation IDs are generated by Archi and excluded from path stability.',
                         'Relations remain the existing flat native Relations population, with 1,651 preserved relationships.',
                         'Reference folders provide documented canonical locators, not clickable aliases.',
                         'This evidence is extracted from actual saved Archi models; it is not a screenshot or human acceptance.']}
    (args.output / 'verification.json').write_bytes(c.encoded(result))
    (args.output / 'parent-placement.json').write_bytes(c.encoded(parent_rows))
    (args.output / 'native-inventory.json.gz').write_bytes(gzip.compress(c.encoded({'folders': [{'path': p, **v} for p, v in folders.items()], 'objects': objects}), mtime=0))
    render(args.output, ET.fromstring(read(HERE / 'published.archimate.gz')), current, folders, objects, parent_rows)
    print(json.dumps(result, indent=2))


def render(output, root, canonical, folders, objects, parents):
    """Human-readable complete native tree and bounded representative paths."""
    cid_by_rep = {e['identifier']: cid for cid, e in canonical.items()}
    titles = {'Motivation parent Driver': '24/7 Availability', 'Strategy Business Capabilities CT1': '[CT1] Individual Care Delivery',
              'Strategy Business Enabling Capabilities': '[CT1] Exchange', 'Application parent Topic': '[Topic] Care'}
    selected = {}
    for label, name in titles.items():
        candidates = [eid for eid, obj in objects.items() if obj['path'][-1] == name and eid in cid_by_rep]
        if label == 'Strategy Business Enabling Capabilities':
            candidates = [eid for eid in candidates if any('Business Enabling Capabilities' in p for p in objects[eid]['path'])]
        if candidates:
            selected[label] = objects[candidates[0]]['path'][:-1]
    # Choose from actual paths instead of relying on an invented capability name.
    bc = [v['path'] for eid, v in objects.items() if eid in cid_by_rep and any('Business Capabilities' in p for p in v['path'])]
    bec = [v['path'] for eid, v in objects.items() if eid in cid_by_rep and any('Business Enabling Capabilities' in p for p in v['path'])]
    selected.setdefault('Strategy Business Capabilities CT1', min(bc, key=len)[:-1])
    selected.setdefault('Strategy Business Enabling Capabilities', min(bec, key=len)[:-1])
    chain = next(p for p in sorted(bec, key=len) if sum('[CT' in s for s in p[:-1]) >= 3)
    selected['CT1 → CT2 → CT3 canonical placement'] = chain[:-1]
    selected['Business Behaviour'] = ['Business', 'III Business', 'III.4 Business Behaviour']
    selected['Business Behaviour deep context'] = max([v['path'] for eid, v in objects.items() if eid in cid_by_rep and 'III.4 Business Behaviour' in v['path']], key=len)[:-1]
    selected['Application Information Architecture / Topics'] = ['Application', 'IV Application', 'IV.1 Information Architecture', 'IV.1.1 Topics']
    selected['Four ValueStreams in native Strategy'] = next(list(p) for p in folders if len(p) == 2 and 'Business navigation reference' in p[-1])
    selected['Business ValueStream reference context'] = ['Business', 'III Business', 'III.2 Products and Value Streams', 'III.2.2 Value Streams']
    for layer in ('Motivation', 'Strategy'):
        selected['Cross-Cutting References — ' + layer] = next(list(p) for p in folders if len(p) == 2 and p[0] == layer and p[1].startswith('R Cross-Cutting'))
    selected['Historical Capability identities'] = next(list(p) for p in folders if p[-1].startswith('R.2 Historical'))
    selected['Views — original folders'] = ['Views']
    selected['Deep canonical navigation'] = max([v['path'] for eid, v in objects.items() if eid in cid_by_rep], key=len)[:-1]
    assert len(selected) >= 14 and all(tuple(p) in folders for p in selected.values())
    folder_ids = {p: 'folder-' + v['id'] for p, v in folders.items()}
    e = html.escape
    def tree(parent, path=()):
        parts = []
        for folder in parent.findall('folder'):
            current = path + (folder.get('name'),)
            parts.append('<details id="' + e(folder_ids[current]) + '"><summary>📁 ' + e(current[-1]) + '</summary>')
            doc = folder.findtext('documentation')
            if doc:
                parts.append('<pre>' + e(doc) + '</pre>')
            for obj in folder.findall('element'):
                eid = obj.get('id'); cid = cid_by_rep.get(eid)
                parts.append('<div class="object">◇ ' + e(obj.get('name') or '') + ' <small>' + e(obj.get(XSI) or '') + '</small><br><code>' + e(eid) + (' · canonicalId=' + cid if cid else '') + '</code></div>')
            parts.append(tree(folder, current) + '</details>')
        return ''.join(parts)
    links = ''.join('<li><a href="#' + e(folder_ids[tuple(path)]) + '">' + e(label) + '</a></li>' for label, path in selected.items())
    page = '''<!doctype html><html lang="en"><meta charset="utf-8"><title>1.1.13 actual Archi workbench tree</title>
<style>body{font:15px system-ui;margin:2em;max-width:1500px;background:#fafafa;color:#222}details{margin:.35em 0 .35em 1.4em}summary{cursor:pointer;padding:.25em}.object{margin:.5em 0 .5em 2.4em}small,code{color:#555}code{font-size:11px}pre{white-space:pre-wrap;background:#eee;padding:1em;margin-left:2em}nav{padding:1em;background:#e9f0f7}a{color:#14548b}</style>
<h1>1.1.13 — actual imported Archi 5.10 tree</h1>
<p>Extracted from the retained native save <code>published.archimate.gz</code>, after actual import of the exact published XML. Folder names, element names, native types, IDs and documentation below are read from that save. This is an inspectable inventory, not a screenshot or a statement of Solution Architect acceptance. Click a representative branch to expand its ancestors.</p>
<p>1,602 canonical elements · 1,651 relationships · 1,209 authored Views · 218 repaired canonical parents · 0 detached canonical elements.</p>
<nav><strong>Representative inspection</strong><ul>''' + links + '</ul></nav>' + tree(root) + '''
<script>function reveal(){const target=document.getElementById(decodeURIComponent(location.hash.slice(1)));if(!target)return;for(let p=target;p;p=p.parentElement)if(p.tagName==='DETAILS')p.open=true;target.scrollIntoView();}window.addEventListener('hashchange',reveal);reveal();</script></html>'''
    (output / 'inspection.html').write_text(page)
    lines = ['# Actual imported Archi workbench inspection', '',
             'These paths and extracts come from `published.archimate.gz`, the actual Archi 5.10 save.',
             'Use [the complete inspectable tree](inspection.html) and load the published XML or decompressed native save in Archi.',
             'Agent inspection supplies evidence; Solution Architect inspection and acceptance remain pending.', '']
    for label, path in selected.items():
        lines += ['## ' + label, '', '`' + ' → '.join(path) + '`', '', '```text', path[-1] + '/']
        child_folders = sorted(p for p in folders if p[:-1] == tuple(path))
        direct = [(eid, v) for eid, v in objects.items() if v['path'][:-1] == path]
        for eid, v in direct[:8]:
            lines.append('  ◇ ' + v['path'][-1] + ' [' + v['type'] + '; ' + eid + ']')
        for p in child_folders[:8]:
            lines.append('  📁 ' + p[-1] + '/')
            for eid, v in list((eid, v) for eid, v in objects.items() if tuple(v['path'][:-1]) == p)[:2]:
                lines.append('    ◇ ' + v['path'][-1] + ' [' + eid + ']')
        if len(child_folders) > 8 or len(direct) > 8:
            lines.append('  … full population available in inspection.html and native-inventory.json.gz')
        lines += ['```', '']
    lines += ['## Open the actual native model', '', '```bash',
              'gzip -dc published.archimate.gz > /tmp/harmonia-1.1.13.archimate', '```', '',
              'Open that file in Archi 5.10. The published XML is the deterministic starting projection;',
              'the native save and returned XML are verification controls, not replacement authorities.', '',
              'Relations remain in the existing native Relations folder. Reference-only folders carry',
              'canonical coordinates/identifiers in their documentation; Archi does not provide live aliases.', '']
    (output / 'inspection.md').write_text('\n'.join(lines))


if __name__ == '__main__':
    main()
