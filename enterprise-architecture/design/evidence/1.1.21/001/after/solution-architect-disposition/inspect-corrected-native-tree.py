# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Inspect the retained actual native save; this does not assert human review."""
import gzip,json,html
from pathlib import Path
from lxml import etree as E
source=Path('enterprise-architecture/design/evidence/1.1.21/001/after/patient-transport-follow-up/archi-transport');out=Path('enterprise-architecture/design/evidence/1.1.21/001/after/solution-architect-disposition/native-tree');out.mkdir(exist_ok=True);root=E.fromstring(gzip.decompress((source/'native-model.archimate.gz').read_bytes()))
folders=[];paths={};native_types={}
def walk(folder,parent):
 path=parent+[folder.get('name','')];elements=folder.findall('element');children=folder.findall('folder')
 folders.append({'path':path,'nativeFolderId':folder.get('id'),'nativeType':folder.get('type'),'directElements':len(elements),'childFolders':len(children)})
 for e in elements:
  paths[e.get('id')]=path;native_types[e.get('id')]=e.get('{http://www.w3.org/2001/XMLSchema-instance}type')
 for child in children:walk(child,path)
for folder in root.findall('folder'):walk(folder,[])
record={'standing':'Actual imported native tree observation only; Solution Architect structural review NOT PERFORMED','modelId':root.get('id'),'folders':folders,'members':{key:{'path':path,'nativeType':native_types[key]} for key,path in paths.items()}}
(out/'imported-tree.json.gz').write_bytes(gzip.compress(json.dumps(record,ensure_ascii=False,indent=2).encode(),mtime=0))
body=['<!doctype html><meta charset="utf-8"><title>1.1.21 actual Archi imported tree</title><style>body{font:15px sans-serif;max-width:90rem;margin:2rem}table{border-collapse:collapse}td,th{border:1px solid #ccc;padding:.4rem}summary{cursor:pointer}</style>','<h1>Actual Archi 5.10 imported tree — 1.1.21 candidate</h1><p>This inventory comes from the retained native save. It is supporting inspection evidence; human structural/visual review and acceptance remain pending.</p><p>Canonical candidate SHA-256: <code>6471411187a5ff72b005c9b9855c48beb3d86b7ae6c7c556a4eb58338011a739</code>.</p>']
for folder in root.findall('folder'):
 name=folder.get('name','');subset=[f for f in folders if f['path'][0]==name]
 body.append('<details><summary>'+html.escape(name)+' — '+str(len(subset))+' folders</summary><table><tr><th>Native folder path</th><th>Direct members</th><th>Child folders</th></tr>')
 for f in subset:body.append('<tr><td>'+html.escape(' / '.join(f['path']))+'</td><td>'+str(f['directElements'])+'</td><td>'+str(f['childFolders'])+'</td></tr>')
 body.append('</table></details>')
(out/'imported-tree.html').write_text('\n'.join(body)+'\n');print('Observed',len(folders),'native folders;',len(paths),'direct element/relationship/View entries')
