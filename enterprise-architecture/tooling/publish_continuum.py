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
import continuum_navigation as nav

PAGE='''<!doctype html>
<html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Harmonia Enterprise Architecture — RAD __VERSION__ review</title>
<style>
:root{font:16px/1.55 system-ui,sans-serif;color:#172c38;background:#f4f7f8}body{margin:0}header{padding:1.4rem 2rem;background:#103544;color:white}h1{font-size:1.6rem;margin:0}header p{margin:.4rem 0}main{display:grid;grid-template-columns:24rem minmax(0,1fr);gap:1rem;padding:1rem}aside,article{background:white;border:1px solid #d3dfe5;border-radius:6px;padding:1rem}aside{align-self:start;position:sticky;top:1rem;max-height:90vh;overflow:auto}input,select{box-sizing:border-box;width:100%;padding:.55rem;margin:.25rem 0;font:inherit;border:1px solid #8199a6;border-radius:4px}button{font:inherit;cursor:pointer;border:0;background:transparent;color:#075e80;text-align:left;width:100%;padding:.45rem}button:hover,button:focus{background:#e0eff5}li{margin:.3rem 0}#list{list-style:none;padding:0}#list small{display:block;color:#53646d;font-size:.72rem}article{padding:1.5rem;overflow-wrap:anywhere}pre{background:#eef3f5;overflow:auto;padding:1rem}code{font-size:.88em}table{border-collapse:collapse;width:100%;font-size:.9rem}th,td{padding:.4rem;border:1px solid #c6d3da;text-align:left;vertical-align:top}blockquote{border-left:3px solid #268dad;margin:1rem 0;padding:.5rem 1rem;background:#f1f8fb}a{color:#00648a}.facts{background:#eef5f8;padding:1rem}.tag{font-weight:bold}summary{cursor:pointer} @media(max-width:850px){main{display:block}aside{position:static;max-height:30vh;margin-bottom:1rem}}
</style>
<header><h1>Harmonia Enterprise Architecture · RAD __VERSION__</h1><p>__COUNT__ individually represented elements</p><p>Governed HCRLs record baseline acceptance. Source standing is retained. Publication and validation confer no acceptance.</p></header>
<main><aside><label for="search">Find an element by name, identity or content</label><input id="search" type="search" placeholder="Principle, Pragma, canonicalId…"><label for="type">Native type / specialisation</label><select id="type"><option value="">All types</option></select><label for="state">Currentness</label><select id="state"><option value="">All currentness states</option></select><p id="total"></p><nav id="hierarchy" aria-label="Architecture hierarchy">__TREE__</nav><ul id="list"></ul></aside><article id="detail"><h2>Read the Architecture Continuum</h2><p>Select an element to inspect its canonical content, architectural standing, identity and source provenance. Historical and candidate elements remain qualified.</p><p>This browser is a generated projection. Edit the canonical Markdown under <code>enterprise-architecture/continuum/</code> through the governed Praxis.</p></article></main>
<script type="application/json" id="architecture">__DATA__</script>
<script>
'use strict';const rows=JSON.parse(document.getElementById('architecture').textContent),byId=new Map(rows.map(x=>[x.id,x]));const el=id=>document.getElementById(id);const esc=x=>String(x).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
for(const [id,values] of [['type',[...new Set(rows.map(x=>x.type))].sort()],['state',[...new Set(rows.map(x=>x.currentness))].sort()]])for(const v of values){const o=document.createElement('option');o.value=v;o.textContent=v;el(id).append(o)}
function show(id){const r=byId.get(id);if(!r)return;el('detail').innerHTML='<div class="facts"><p>'+esc(r.number+' · '+r.path.join(' / '))+'</p><p class="tag">'+esc(r.type)+'</p><p>'+esc(r.standing)+' · '+esc(r.currentness)+'</p><p>canonicalId: <code>'+esc(r.id)+'</code><br>friendlyName: '+esc(r.friendly ?? 'Unresolved')+'<br>canonicalName: '+esc(r.canonical ?? 'Unresolved — see source provenance')+'</p><a href="'+esc(r.source)+'">Canonical Markdown</a></div>'+r.html+'<details><summary>Source provenance and unresolved properties</summary><pre>'+esc(JSON.stringify({standing:r.qualifications,classification:r.classification,provenance:r.provenance,unresolved:r.unresolved},null,2))+'</pre></details>';location.hash=id;for(const a of el('detail').querySelectorAll('a[data-element]'))a.addEventListener('click',e=>{e.preventDefault();show(a.dataset.element)});}
function filter(){const q=el('search').value.toLowerCase(),type=el('type').value,state=el('state').value;const filtered=rows.filter(r=>(!type||r.type===type)&&(!state||r.currentness===state)&&(!q||(r.number+' '+r.path.join(' ')+' '+r.name+' '+r.id+' '+r.friendly+' '+r.plain).toLowerCase().includes(q)));el('hierarchy').hidden=Boolean(q||type||state);el('list').hidden=!Boolean(q||type||state);el('total').textContent=filtered.length+' elements';el('list').replaceChildren();for(const r of filtered){const li=document.createElement('li'),b=document.createElement('button');b.textContent=r.number+' '+r.name;b.onclick=()=>show(r.id);li.append(b);const s=document.createElement('small');s.textContent=r.type+' · '+r.currentness+' · '+r.standing;li.append(s);el('list').append(li)}}
for(const id of ['search','type','state'])el(id).addEventListener('input',filter);window.addEventListener('hashchange',()=>show(location.hash.slice(1)));filter();if(location.hash)show(location.hash.slice(1));
</script></html>
'''


def tex(value):
    chars={'\\':r'\textbackslash{}','{':r'\{','}':r'\}','$':r'\$','&':r'\&','#':r'\#','%':r'\%','_':r'\_','^':r'\textasciicircum{}','~':r'\textasciitilde{}'}
    return ''.join(chars.get(x,x) for x in str(value))


def outputs(root, output):
    report = c.validate(root)
    _, elements = c.load(root)
    tree = nav.load(root, elements)
    locations = nav.by_element(tree)
    version = c.read_json(root / 'architecture-release.json')['version']
    rows = []
    index = ['<!-- Generated from canonical Continuum architecture and navigation. -->',
             f'# Harmonia Architecture Continuum — RAD {version}', '',
             '[Architecture browser](continuum.html) · [ArchiMate Exchange](Harmonia-Architecture.xml) · [Catalogue](continuum-catalogue.tex)', '',
             'Canonical ID is identity; name is meaning; number is location. Hierarchy asserts no semantic containment.', '']
    body = [r'\documentclass[10pt,a4paper]{article}', r'\usepackage[margin=20mm]{geometry}',
            r'\usepackage{fontspec}', r'\setmainfont{DejaVu Sans}', r'\usepackage{hyperref}',
            r'\usepackage{xurl}', r'\usepackage{seqsplit}', r'\emergencystretch=3em',
            r'\hypersetup{hidelinks}', r'\setlength{\parindent}{0pt}', r'\begin{document}',
            r'\section*{Harmonia Architecture Continuum — RAD ' + tex(version) + '}',
            r'Identity and standing catalogue. Canonical architecture and navigation supply the source; publication supplies no acceptance.\par']
    pathmap = {p.resolve(): cid for cid, (_, _, p) in elements.items()}
    successors = ea_references.successors(root)
    from lxml import html as lh

    def source_link(node):
        if node.get('canonicalId'):
            return os.path.relpath(elements[node['canonicalId']][2], output)
        if node.get('source'):
            source, marker, anchor = node['source'].partition('#')
            return os.path.relpath(root / source, output) + (marker + anchor if marker else '')
        return None

    def render(node, depth=0):
        title = nav.title(node)
        link = source_link(node)
        index.append('  ' * depth + '- ' + ('[' + title.replace('|', r'\|') + '](' + link + ')' if link else title))
        command = 'section' if depth < 2 else 'subsection'
        body.append('\\' + command + '*{' + tex(title) + '}')
        if node.get('description'):
            body.append(tex(node['description']) + r'\par')
        cid = node.get('canonicalId')
        if cid:
            m, content, path = elements[cid]
            kind = m['archimateType'] + (' / ' + m['specialization'] if m.get('specialization') else '')
            fragment = lh.fragment_fromstring(markdown.markdown(content, extensions=['tables', 'fenced_code']), create_parent='div')
            for a in fragment.iter('a'):
                href = a.get('href', '')
                if href and not re.match(r'^[a-z]+:|^#|^//', href):
                    target = (path.parent / href.split('#')[0]).resolve()
                    target = ea_references.current_target(root, target, successors)
                    if target in pathmap:
                        a.set('data-element', pathmap[target]); a.set('href', '#' + pathmap[target])
                    else:
                        a.set('href', os.path.relpath(target, output) + ('#' + href.split('#', 1)[1] if '#' in href else ''))
            rows.append({'id': cid, 'name': m['displayName'], 'number': node['number'], 'path': node['path'],
                         'type': kind, 'standing': m['standing']['semantic'], 'currentness': m['standing']['currentness'],
                         'qualifications': m['standing'], 'classification': node.get('classificationIssue'),
                         'friendly': m['friendlyName'], 'canonical': m['canonicalName'], 'source': link,
                         'html': lh.tostring(fragment, encoding='unicode'), 'plain': content,
                         'provenance': m['provenance'], 'unresolved': m.get('unresolvedProperties', {})})
            body.extend([r'{\small ' + tex(kind + ' | ' + m['standing']['semantic'] + ' | ' + m['standing']['currentness']) + r'}\par',
                         r'\texttt{' + cid + r'}\par', r'Canonical: \seqsplit{' + tex(m['canonicalName'] or 'Unresolved') + r'}\par'])
        if link:
            body.append(r'{\footnotesize\url{' + link + r'}}\par')
        for child in node.get('children', []):
            render(child, depth + 1)
    for part in tree:
        render(part)
    body.append(r'\end{document}')

    def browser(node):
        label = html.escape(nav.title(node))
        cid = node.get('canonicalId')
        if cid:
            label = '<a href="#' + cid + '">' + label + '</a>'
        elif node.get('source'):
            label = '<a href="' + html.escape(source_link(node), quote=True) + '">' + label + '</a>'
        children = node.get('children', [])
        content = '<p>' + html.escape(node.get('description', '')) + '</p>' if node.get('description') else ''
        if node.get('relatedCanonicalIds'):
            content += '<p>' + '; '.join('<a href="#' + related + '">' + html.escape(nav.title(locations[related])) + '</a>' for related in node['relatedCanonicalIds']) + '</p>'
        if children or content:
            return '<li data-number="' + node['number'] + '"><details><summary>' + label + '</summary>' + content + '<ul>' + ''.join(browser(child) for child in children) + '</ul></details></li>'
        return '<li data-number="' + node['number'] + '">' + label + '</li>'
    hierarchy = '<ul>' + ''.join(browser(part) for part in tree) + '</ul>'
    page = (PAGE.replace('__VERSION__', version).replace('__COUNT__', str(len(rows)))
            .replace('__TREE__', hierarchy).replace('__DATA__', json.dumps(rows, ensure_ascii=False, separators=(',', ':')).replace('<', '\\u003c')))
    artifacts = {'continuum.html': page.encode(), 'README.md': ('\n'.join(index) + '\n').encode(),
                 'continuum-catalogue.tex': ('\n'.join(body) + '\n').encode(), 'navigation.json': c.encoded({'roots': tree})}
    report = {k: v for k, v in report.items() if k not in ('unresolvedProperties', 'counts')}
    report.update(publication='Continuum taxonomy browser and qualified catalogue',
                  sha256={name: c.sha(data) for name, data in artifacts.items()},
                  sourceInputs='Canonical Markdown, canonical navigation, maintained type/schema configuration and root release', latexIngested=False)
    artifacts['publication.json'] = c.encoded(report)
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
