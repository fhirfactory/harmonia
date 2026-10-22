#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reproducible review browser and LaTeX catalogue from canonical Markdown.

No source classification/allocation or LaTeX reverse ingestion. The HTML browser
contains the full element bodies; the PDF is explicitly an identity/standing
catalogue. Both preserve historical and qualified populations visibly.
"""
import argparse
from collections import Counter
import html
import json
import os
from pathlib import Path
import re
import sys
import markdown
import architecture_continuum as c
import ea_references

PAGE='''<!doctype html>
<html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Harmonia Enterprise Architecture — RAD __VERSION__ review</title>
<style>
:root{font:16px/1.55 system-ui,sans-serif;color:#172c38;background:#f4f7f8}body{margin:0}header{padding:1.4rem 2rem;background:#103544;color:white}h1{font-size:1.6rem;margin:0}header p{margin:.4rem 0}main{display:grid;grid-template-columns:24rem minmax(0,1fr);gap:1rem;padding:1rem}aside,article{background:white;border:1px solid #d3dfe5;border-radius:6px;padding:1rem}aside{align-self:start;position:sticky;top:1rem;max-height:90vh;overflow:auto}input,select{box-sizing:border-box;width:100%;padding:.55rem;margin:.25rem 0;font:inherit;border:1px solid #8199a6;border-radius:4px}button{font:inherit;cursor:pointer;border:0;background:transparent;color:#075e80;text-align:left;width:100%;padding:.45rem}button:hover,button:focus{background:#e0eff5}li{margin:.3rem 0}#list{list-style:none;padding:0}#list small{display:block;color:#53646d;font-size:.72rem}article{padding:1.5rem;overflow-wrap:anywhere}pre{background:#eef3f5;overflow:auto;padding:1rem}code{font-size:.88em}table{border-collapse:collapse;width:100%;font-size:.9rem}th,td{padding:.4rem;border:1px solid #c6d3da;text-align:left;vertical-align:top}blockquote{border-left:3px solid #268dad;margin:1rem 0;padding:.5rem 1rem;background:#f1f8fb}a{color:#00648a}.facts{background:#eef5f8;padding:1rem}.tag{font-weight:bold}summary{cursor:pointer} @media(max-width:850px){main{display:block}aside{position:static;max-height:30vh;margin-bottom:1rem}}
</style>
<header><h1>Harmonia Enterprise Architecture · RAD __VERSION__</h1><p>__COUNT__ individually represented elements</p><p>Governed HCRLs record baseline acceptance. Source standing is retained. Publication and validation confer no acceptance.</p></header>
<main><aside><label for="search">Find an element by name, identity or content</label><input id="search" type="search" placeholder="Principle, Pragma, canonicalId…"><label for="type">Native type / specialisation</label><select id="type"><option value="">All types</option></select><label for="state">Currentness</label><select id="state"><option value="">All currentness states</option></select><p id="total"></p><ul id="list"></ul></aside><article id="detail"><h2>Review the migrated continuum</h2><p>Select an element to inspect its canonical content, architectural standing, identity and source provenance. Historical and candidate elements remain qualified.</p><p>This browser is a generated projection. Edit the canonical Markdown under <code>enterprise-architecture/continuum/</code> through the governed Praxis.</p></article></main>
<script type="application/json" id="architecture">__DATA__</script>
<script>
'use strict';const rows=JSON.parse(document.getElementById('architecture').textContent),byId=new Map(rows.map(x=>[x.id,x]));const el=id=>document.getElementById(id);const esc=x=>String(x).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
for(const [id,values] of [['type',[...new Set(rows.map(x=>x.type))].sort()],['state',[...new Set(rows.map(x=>x.currentness))].sort()]])for(const v of values){const o=document.createElement('option');o.value=v;o.textContent=v;el(id).append(o)}
function show(id){const r=byId.get(id);if(!r)return;el('detail').innerHTML='<div class="facts"><p class="tag">'+esc(r.type)+'</p><p>'+esc(r.standing)+' · '+esc(r.currentness)+'</p><p>canonicalId: <code>'+esc(r.id)+'</code><br>friendlyName: '+esc(r.friendly ?? 'Unresolved')+'<br>canonicalName: '+esc(r.canonical ?? 'Unresolved — see source provenance')+'</p><a href="'+esc(r.source)+'">Canonical Markdown</a></div>'+r.html+'<details><summary>Source provenance and unresolved properties</summary><pre>'+esc(JSON.stringify({provenance:r.provenance,unresolved:r.unresolved},null,2))+'</pre></details>';location.hash=id;for(const a of el('detail').querySelectorAll('a[data-element]'))a.addEventListener('click',e=>{e.preventDefault();show(a.dataset.element)});}
function filter(){const q=el('search').value.toLowerCase(),type=el('type').value,state=el('state').value;const filtered=rows.filter(r=>(!type||r.type===type)&&(!state||r.currentness===state)&&(!q||(r.name+' '+r.id+' '+r.friendly+' '+r.plain).toLowerCase().includes(q)));el('total').textContent=filtered.length+' elements';el('list').replaceChildren();for(const r of filtered){const li=document.createElement('li'),b=document.createElement('button');b.textContent=r.name;b.onclick=()=>show(r.id);li.append(b);const s=document.createElement('small');s.textContent=r.type+' · '+r.currentness+' · '+r.standing;li.append(s);el('list').append(li)}}
for(const id of ['search','type','state'])el(id).addEventListener('input',filter);window.addEventListener('hashchange',()=>show(location.hash.slice(1)));filter();if(location.hash)show(location.hash.slice(1));
</script></html>
'''


def tex(value):
    chars={'\\':r'\textbackslash{}','{':r'\{','}':r'\}','$':r'\$','&':r'\&','#':r'\#','%':r'\%','_':r'\_','^':r'\textasciicircum{}','~':r'\textasciitilde{}'}
    return ''.join(chars.get(x,x) for x in str(value))


def outputs(root,output):
    report=c.validate(root);_,elements=c.load(root)
    version=c.read_json(root/'architecture-release.json')['version']
    rows=[];index=['<!-- Generated from canonical continuum Markdown; edit the sources. -->\n',f'# RAD-{version} Enterprise Architecture review index\n',
                  'Governed HCRLs record baseline acceptance. Existing source standing remains independent.\n',
                  '[Interactive architecture browser](continuum.html) · [ArchiMate Exchange](Harmonia-Architecture.xml) · [LaTeX catalogue](continuum-catalogue.tex)\n']
    body=[r'\documentclass[10pt,a4paper]{article}',r'\usepackage[margin=20mm]{geometry}',r'\usepackage{fontspec}',r'\setmainfont{DejaVu Sans}',r'\usepackage{hyperref}',r'\usepackage{xurl}',r'\usepackage{seqsplit}',r'\emergencystretch=3em',r'\hypersetup{hidelinks}',r'\setlength{\parindent}{0pt}',r'\begin{document}',r'\section*{Harmonia Enterprise Architecture — RAD '+tex(version)+'}',
          r'Identity and standing catalogue for holistic Solution Architect/ARB review. Full architectural content remains in the canonical Markdown and generated HTML browser. Publication confers no acceptance.\par',
          tex(str(len(elements))+' individually represented elements; no publication identity allocation.')+r'\par']
    pathmap={p.resolve():cid for cid,(_,_,p) in elements.items()}
    successors=ea_references.successors(root)
    current_type=None
    from lxml import html as lh
    for cid,(m,content,path) in sorted(elements.items(),key=lambda x:(x[1][0]['archimateType'],x[1][0].get('specialization') or '',x[1][0]['displayName'],x[0])):
        kind=m['archimateType']+(' / '+m['specialization'] if m.get('specialization') else '')
        if current_type!=kind:
            index+=['\n## '+kind+'\n','| Element | Canonical identity | Standing / currentness |','| --- | --- | --- |'];body.append(r'\section*{'+tex(kind)+'}');current_type=kind
        rel=os.path.relpath(path,output)
        index.append('| ['+m['displayName'].replace('|','\\|')+']('+rel+') | '+cid+' | '+m['standing']['semantic'].replace('|','\\|')+' / '+m['standing']['currentness']+' |')
        fragment=lh.fragment_fromstring(markdown.markdown(content,extensions=['tables','fenced_code']),create_parent='div')
        for a in fragment.iter('a'):
            href=a.get('href','')
            if href and not re.match(r'^[a-z]+:|^#|^//',href):
                target=(path.parent/href.split('#')[0]).resolve()
                target=ea_references.current_target(root,target,successors)
                if target in pathmap:a.set('data-element',pathmap[target]);a.set('href','#'+pathmap[target])
                else:a.set('href',os.path.relpath(target,output)+('#'+href.split('#',1)[1] if '#' in href else ''))
        rows.append({'id':cid,'name':m['displayName'],'type':kind,'standing':m['standing']['semantic'],'currentness':m['standing']['currentness'],'friendly':m['friendlyName'],'canonical':m['canonicalName'],'source':rel,
                     'html':lh.tostring(fragment,encoding='unicode'),'plain':content,'provenance':m['provenance'],'unresolved':m.get('unresolvedProperties',{})})
        body.extend([r'\subsection*{'+tex(m['displayName'])+'}',r'{\small '+tex(kind+' | '+m['standing']['semantic']+' | '+m['standing']['currentness'])+r'}\par',
                     r'\texttt{'+cid+r'}\par',r'Friendly: \seqsplit{'+tex(m['friendlyName'] or 'Unresolved')+r'}\par',r'Canonical: \seqsplit{'+tex(m['canonicalName'] or 'Unresolved')+r'}\par',
                     r'{\footnotesize\url{'+rel+r'}}\par'])
    body.append(r'\end{document}')
    page=PAGE.replace('__VERSION__',version).replace('__COUNT__',str(len(rows))).replace('__DATA__',json.dumps(rows,ensure_ascii=False,separators=(',',':')).replace('<','\\u003c'))
    artifacts={'continuum.html':page.encode(),'README.md':('\n'.join(index)+'\n').encode(),'continuum-catalogue.tex':('\n'.join(body)+'\n').encode()}
    report={k:v for k,v in report.items() if k not in ('unresolvedProperties','counts')};report.update(publication='Generated HTML full-content browser and LaTeX identity/standing catalogue',sha256={name:c.sha(data) for name,data in artifacts.items()},sourceInputs='Canonical Markdown, maintained type/schema configuration and root release',latexIngested=False)
    artifacts['publication.json']=c.encoded(report)
    return artifacts


def main():
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--root',type=Path,default=c.ROOT);p.add_argument('--output',type=Path,required=True);p.add_argument('--check',action='store_true');a=p.parse_args()
    output=a.output.resolve();artifacts=outputs(a.root,output)
    for name,data in artifacts.items():
        path=output/name
        if a.check:c.require(path.exists() and path.read_bytes()==data,'Missing/stale publication: '+str(path))
        else:path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(data)
    print(json.dumps({'status':'PASS','artifacts':len(artifacts),'check':a.check}))
if __name__=='__main__':main()
