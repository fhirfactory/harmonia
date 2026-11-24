#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only diagnostic of one exact projection and its actual Archi return.

This evidence script does not import projectors or regenerate a publication.
Run from repository root with --work pointing at the isolated Archi outputs.
"""
import argparse
from collections import Counter, defaultdict
import gzip
import hashlib
import json
from pathlib import Path
import xml.etree.ElementTree as ET

N = {'a': 'http://www.opengroup.org/xsd/archimate/3.0/'}
XSI = '{http://www.w3.org/2001/XMLSchema-instance}type'
PUBLISHED = Path('enterprise-architecture/design/archimate/published/1.1.11/Harmonia-Architecture.xml')
TEMPLATE = Path('enterprise-architecture/design/archimate/projection-template.xml')


def exchange(path):
    return ET.fromstring(gzip.decompress(path.read_bytes()) if path.suffix == '.gz' else path.read_bytes())


def items(root):
    result = []

    def walk(parent, path):
        for node in parent.findall('a:item', N):
            label = node.findtext('a:label', namespaces=N)
            row = {'identifier': node.get('identifier'), 'ref': node.get('identifierRef'),
                   'label': label, 'path': path + ([label] if label else []),
                   'children': len(node.findall('a:item', N)),
                   'documentation': node.findtext('a:documentation', namespaces=N)}
            result.append(row)
            walk(node, row['path'])
    for org in root.findall('a:organizations', N):
        walk(org, [])
    return result


def native(root):
    folders, objects = [], {}

    def walk(node, path):
        for folder in node.findall('folder'):
            p = path + [folder.get('name')]
            folders.append({'path': p, 'type': folder.get('type'), 'id': folder.get('id'),
                            'directObjects': len(folder.findall('element')),
                            'childFolders': len(folder.findall('folder'))})
            for obj in folder.findall('element'):
                objects[obj.get('id')] = {'path': p + [obj.get('name')], 'type': obj.get(XSI)}
            walk(folder, p)
    walk(root, [])
    return folders, objects


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--work', required=True, type=Path)
    ap.add_argument('--output', required=True, type=Path)
    args = ap.parse_args()
    root = Path.cwd()
    published, returned, template = [exchange(p) for p in
                                    (root / PUBLISHED, args.work / 'returned.xml', root / TEMPLATE)]
    imported = ET.parse(args.work / 'published.archimate').getroot()
    src, ret, templ = items(published), items(returned), items(template)
    folders, objects = native(imported)
    definitions = {p.get('identifier'): p.findtext('a:name', namespaces=N)
                   for p in published.findall('a:propertyDefinitions/a:propertyDefinition', N)}
    rows = []
    elements = {e.get('identifier'): e for e in published.findall('a:elements/a:element', N)}
    views = {v.get('identifier'): v.findtext('a:name', namespaces=N)
             for v in published.findall('a:views/a:diagrams/a:view', N)}
    refs = defaultdict(list)
    returned_refs = defaultdict(list)
    for x in src:
        if x['ref']:
            refs[x['ref']].append(x)
    for x in ret:
        if x['ref']:
            returned_refs[x['ref']].append(x)
    for eid, e in elements.items():
        props = {definitions[p.get('propertyDefinitionRef')]: p.findtext('a:value', namespaces=N)
                 for p in e.findall('a:properties/a:property', N)}
        paths = refs[eid]
        parent = any(x['children'] for x in paths)
        imported_path = objects[eid]['path']
        rows.append({'canonicalId': props['canonicalId'], 'exchangeId': eid,
                     'name': e.findtext('a:name', namespaces=N), 'nativeType': e.get(XSI),
                     'logicalNumber': props['Continuum / Navigation Number'],
                     'logicalPath': props['Continuum / Navigation Path'],
                     'sourceItems': paths, 'nativePath': imported_path,
                     'returnedItems': returned_refs[eid], 'navigationParent': parent,
                     'directNativeRoot': len(imported_path) == 2,
                     'classification': 'canonical element separated from navigation context' if parent
                     else 'native-layer organisation effect' if paths[0]['path'][0].startswith(('R ', 'III '))
                     and imported_path[0] not in ('Business',) else 'coherent nested element placement'})
    # Compare actual object placement; Exchange reference leaves often have no label.
    def folder_paths(records, ids):
        return {eid: [x['path'][:-1] if x['label'] else x['path'] for x in records if x['ref'] == eid]
                for eid in ids}
    vp = folder_paths(src, views)
    vt = folder_paths(templ, views)
    vr = folder_paths(ret, views)
    view_rows = [{'viewId': eid, 'name': name, 'templateFolders': vt[eid], 'publishedFolders': vp[eid],
                  'nativePath': objects[eid]['path'], 'returnedFolders': vr[eid]}
                 for eid, name in views.items()]
    view_paths_ok = all(vp[eid] == vt[eid] == vr[eid] == [objects[eid]['path'][:-1]] for eid in views)
    parent_rows = [x for x in rows if x['navigationParent']]
    nav_src = [x for x in src if x['identifier'] and x['identifier'].startswith('navigation-')]
    # Native folder prefixes vary by layer. Test suffix paths to identify logical folders.
    retained = [x for x in nav_src if any(f['path'][1:] == x['path'] for f in folders)]
    missing = [x for x in nav_src if x not in retained and not x['ref']]
    summary = {'publishedSha256': hashlib.sha256((root / PUBLISHED).read_bytes()).hexdigest(),
               'elementCount': len(elements), 'viewCount': len(views),
               'semanticRelationshipCount': len(published.findall('a:relationships/a:relationship', N)),
               'sourceNavigationItems': len(nav_src), 'sourceElementReferenceMultiplicity': dict(Counter(len(refs[e]) for e in elements)),
               'returnedElementReferenceMultiplicity': dict(Counter(len(returned_refs[e]) for e in elements)),
               'parentReferenceCount': len(parent_rows), 'parentTypes': dict(Counter(x['nativeType'] for x in parent_rows)),
               'allParentsRemainAtNativeRoot': all(x['directNativeRoot'] for x in parent_rows),
               'rootElementsWithoutParentItems': [x['exchangeId'] for x in rows if x['directNativeRoot'] and not x['navigationParent']],
               'viewOrganisationExactAcrossTemplatePublishedNativeReturned': view_paths_ok,
               'viewFolderCount': sum(f['path'][0] == 'Views' for f in folders),
               'viewTopBranches': dict(Counter(objects[e]['path'][1] for e in views)),
               'layers': {f['path'][0]: {'elements': sum(x['nativePath'][0] == f['path'][0] for x in rows),
                                         'directRootElements': sum(x['directNativeRoot'] and x['nativePath'][0] == f['path'][0] for x in rows),
                                         'foldersIncludingRoot': sum(x['path'][0] == f['path'][0] for x in folders)}
                          for f in folders if len(f['path']) == 1},
               'navigationOnlyItemsNotRetainedAsExactFolderPath': missing}
    args.output.mkdir(parents=True, exist_ok=True)
    for name, data in [('summary.json', summary), ('elements.json', rows), ('views.json', view_rows), ('folders.json', folders)]:
        payload = (json.dumps(data, indent=2, ensure_ascii=False) + '\n').encode()
        if name == 'summary.json':
            (args.output / name).write_bytes(payload)
        else:
            (args.output / (name + '.gz')).write_bytes(gzip.compress(payload, mtime=0))
    print(json.dumps({k: v for k, v in summary.items() if k != 'navigationOnlyItemsNotRetainedAsExactFolderPath'}, indent=2))
    print('Missing navigation-only folder paths:', len(missing))


if __name__ == '__main__':
    main()
