#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Check rendered local source links without treating code/provenance text as links."""
import argparse
from pathlib import Path
import re
from urllib.parse import unquote,urlsplit
import markdown
from lxml import html
import architecture_continuum as c
import ea_references


def verify(root=c.ROOT):
    _,elements=c.load(root);missing=[];anchors=[];checked=0;historical=0;promoted=0;context=0
    successors=ea_references.successors(root)
    for cid,(m,body,path) in elements.items():
        doc=html.fragment_fromstring(markdown.markdown(body,extensions=['tables','fenced_code']),create_parent='div')
        for node in doc.iter('a'):
            target=node.get('href')
            if not target:continue
            url=urlsplit(target)
            if url.scheme or target.startswith('//'):continue
            dest=(path.parent/unquote(url.path)).resolve() if url.path else path
            current=ea_references.current_target(root,dest,successors)
            promoted+=current!=dest
            dest=current
            # Canonical definitions retain their exact repository-instruction
            # source citation. AGENTS still governs agent work; link checking
            # neither interprets that instruction nor requires its source copy.
            if dest == root/'AGENTS.md':
                context+=1
                continue
            if ea_references.historical_target(root,dest):
                historical+=1
                continue
            checked+=1
            if not dest.exists():missing.append({'canonicalId':cid,'target':target});continue
            if url.fragment and dest.suffix=='.md':
                text=dest.read_text();ids=set(re.findall(r'<a id="([^"]+)"',text))
                ids.update(re.sub(r'[^\w\- ]','',h.lower()).replace(' ','-') for h in re.findall(r'^#+ (.+)$',text,re.M))
                if unquote(url.fragment) not in ids:anchors.append({'canonicalId':cid,'target':target})
    return {'status':'PASS' if not missing and not anchors else 'FAIL','renderedLocalLinksChecked':checked,'promotedAuthorityLinks':promoted,'historicalProvenanceLinks':historical,'repositoryInstructionSourceCitations':context,'missingTargetFiles':missing,'unresolvedAnchors':anchors,'limit':'Current EA local targets/anchors only. Historical source/evidence and repository-instruction citations remain locators and are not prerequisites; external network targets and code examples are not dereferenced.'}

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--report',type=Path,required=True);a=p.parse_args()
    result=verify();a.report.write_bytes(c.encoded(result));print(result['status']);raise SystemExit(0 if result['status']=='PASS' else 1)
