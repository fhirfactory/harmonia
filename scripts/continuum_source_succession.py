#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded RAD-1.1.5 succession: exact selected content, unchanged source bytes.

No heuristic element creation or source deletion. Whole-section retirement requires
an exact inspected correspondence. Other material retains its recorded role/open
question; file location never supplies architectural classification.
"""
from collections import defaultdict,Counter
import os
from pathlib import Path
import re
import subprocess
from urllib.parse import urlsplit,unquote
import architecture_continuum as c
from ingest_rad_continuum import ENTRY,EVIDENCE,BASE

AFTER=c.ROOT/EVIDENCE/'after'
START='<!-- continuum-supersession:start -->'
END='<!-- continuum-supersession:end -->'


def original(path):
    return subprocess.run(['git','show',ENTRY+':'+str(path)],cwd=c.ROOT,check=True,capture_output=True).stdout.decode()


def slug(value):
    return re.sub(r'[^\w\- ]','',value.strip('# ').lower()).replace(' ','-')


def selection():
    selected=defaultdict(lambda:defaultdict(set))
    for row in c.read_json(AFTER/'section-correspondence.json'):
        if row['completeSection']:selected[row['source']][row['selector']].add(row['canonicalId'])
    for row in c.read_json(AFTER/'definition-ingestion.json')['allocated']:
        if row['selector']+'\n' in original(row['source']) and (row['selector'].startswith('#') or row['selector'].startswith(('- ', '| '))):
            selected[row['source']][row['selector']].add(row['canonicalId'])
    for row in c.read_json(AFTER/'section-correspondence.json'):
        if row['selector'].startswith('table assertion: '):
            selected[row['source']][row['selector'].removeprefix('table assertion: ')].add(row['canonicalId'])
    m=c.read_json(c.ROOT/BASE/'motivation.json')
    for row in m['elements']:
        if row['record_state']=='current':selected[row['source']][row['heading']].add(row['canonicalId'])
    # Earlier IA projection copied some complete sections and some individual
    # assertions in a shared section. Retire only byte-equivalent whole sections.
    from ingest_rad_continuum import section
    for row in c.read_json(c.ROOT/BASE/'information-architecture.json')['elements']:
        e=row['evidence'];src=e['source'];heading=e['heading']
        raw=original(src); start=raw.index(heading+'\n')+len(heading)+1; stop=re.search(r'(?m)^#{1,'+str(e['heading_level'])+r'} ',raw[start:]); part=raw[start:start+stop.start()] if stop else raw[start:]; part=re.sub(r'\n<a id="[^"]+"></a>\s*$','',part.rstrip()).strip().removesuffix('---').rstrip()
        if e['assertion'].strip() == part:
            selected[src][heading].add(row['canonicalId'])
    return selected


def rebase_links(elements,selected):
    targets={}
    for src,headings in selected.items():
        text=original(src)
        for heading,ids in headings.items():
            if not heading.startswith('#'): continue
            if len(ids)!=1:continue
            cid=next(iter(ids));start=text.index(heading+'\n')
            targets[(src,slug(heading))]=elements[cid][2]
            anchor=re.search(r'<a id="([^"]+)"></a>\s*$',text[:start])
            if anchor:targets[(src,anchor[1])]=elements[cid][2]
    changed=0;unresolved=[]
    def links(text,source,path,cid):
        def replace(match):
            target=match['target'];angle=target.startswith('<');target=target.strip('<>')
            url=urlsplit(target)
            if url.scheme or target.startswith('//'):return match[0]
            old=(c.ROOT/source).parent/unquote(url.path) if url.path else c.ROOT/source
            if target.startswith('/') or target.startswith(('docs/','enterprise-architecture/')):old=c.ROOT/target.lstrip('/').split('#')[0]
            old=old.resolve()
            try:src=str(old.relative_to(c.ROOT))
            except ValueError:return match[0]
            dest=targets.get((src,unquote(url.fragment)))
            fragment='' if dest else ('#'+url.fragment if url.fragment else '')
            if dest is None:dest=old
            if not dest.exists():
                unresolved.append({'canonicalId':cid,'sourceBase':source,'target':target,'reason':'Source link target absent; preserved source reference for review'})
            rel=os.path.relpath(dest,path.parent)+fragment
            return match['lead']+('<'+rel+'>' if angle else rel)
        # Code examples are content, not live Markdown links.
        pieces=[];fenced=False
        for line in text.splitlines(keepends=True):
            if line.lstrip().startswith('```'):fenced=not fenced;pieces.append(line);continue
            pieces.append(line if fenced else re.sub(r'(?P<lead>\]\(|^\[[^\]]+\]:\s*)(?P<target><[^>]+>|[^\s)]+)',replace,line))
        return ''.join(pieces)
    for cid,(m,body,path) in elements.items():
        if m.get('representation',{}).get('sourceLinksRebased'):continue
        preferred=next((p['source'] for p in m['provenance'] if p['role']=='current governed definition'),None)
        if not preferred:preferred=next((p['source'] for p in m['provenance'] if p['role']=='source-qualified original definition'),None)
        if not preferred:preferred=next((p['source'] for p in m['provenance'] if p['source'].endswith('.md') and 'ingested' in p['role']),None)
        # Captured prose sometimes already uses repository-relative links.
        preferred=preferred or next((p['source'] for p in m['provenance'] if p['source'].endswith('.md') and p['role']=='original source locator; recorded edition'), 'README.md')
        blocks=re.split(r'(\n## Source-qualified contribution\n\nSource: `[^`]+`[^\n]*\n)',body)
        result=links(blocks[0],preferred,path,cid)
        for i in range(1,len(blocks),2):
            source=re.search(r'Source: `([^`]+)`',blocks[i])[1]
            result+=blocks[i]+links(blocks[i+1],source,path,cid)
        m['representation']={'sourceLinksRebased':True,'authority':'HCRL 1.1.5-002 DR-3; source meaning unchanged'}
        path.write_text(c.encode(m,result));changed+=1
    return {'rebasedElements':changed,'unresolvedSourceLinks':unresolved}


def retire(elements,selected):
    result=[]
    range_file=AFTER/'source-range-correspondence.json'
    ranges=c.read_json(range_file) if range_file.exists() else []
    for row in ranges:selected.setdefault(row['source'],{})
    for source,headings in sorted(selected.items()):
        text=original(source);path=c.ROOT/source
        # Exact entry edition protects unique user edits and repeat invocation.
        current=path.read_text()
        c.require(current==text or START in current,'Source changed since entry: '+source)
        candidates=[]
        for heading,ids in headings.items():
            start=text.index(heading+'\n')
            if heading.startswith('#'):
                level=len(heading.split(' ')[0])
                match=re.search(r'(?m)^#{1,'+str(level)+'} ',text[start+len(heading)+1:])
                end=start+len(heading)+1+match.start() if match else len(text)
            else:end=start+len(heading)+1
            # Keep a following explicit anchor with its following section.
            anchor=re.search(r'\n<a id="[^"]+"></a>\s*$',text[start:end])
            if anchor:end=start+anchor.start()
            candidates.append((start,end,set(ids),heading))
        lines=text.splitlines(keepends=True)
        for row in ranges:
            if row['source']==source:
                a=sum(map(len,lines[:row['startLine']-1]));b=sum(map(len,lines[:row['endLine']]))
                c.require(c.sha(text[a:b].encode())==row['originalSha256'],'Source range drift')
                candidates.append((a,b,set(row['canonicalIds']),row['selector']))
        spans=[]
        for start,end,ids,heading in sorted(candidates,key=lambda x:(x[0],-x[1])):
            if spans and start<spans[-1][1]:
                spans[-1][1]=max(end,spans[-1][1]);spans[-1][2].update(ids);continue
            spans.append([start,end,ids,heading])
        rendered=text
        for start,end,ids,heading in reversed(spans):
            refs='\n'.join('- ['+elements[cid][0]['displayName']+']('+os.path.relpath(elements[cid][2],path.parent)+')' for cid in sorted(ids))
            before=(START+'\n------- Legacy Content - Superseded ------- Start ------\n'
                    'Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:\n'+refs+
                    '\nOriginal source standing and unresolved qualifications are preserved.\n'
                    '---------------------------------------------------------\n\n')
            after='\n\n------- Legacy Content - Superseded ------- Finish ----\n'+END+'\n\n'
            rendered=rendered[:start]+before+rendered[start:end]+after+rendered[end:]
        # Remove only the wrapper's extra EOF separator, never original text.
        if rendered.endswith(END+'\n\n'):rendered=rendered[:-1]
        if spans:path.write_text(rendered)
        result.append({'source':source,'entryRevision':ENTRY,'entrySha256':c.sha(text.encode()),
            'retiredSections':[{'selector':h,'startLine':text[:a].count('\n')+1,'endLine':text[:b].count('\n')+1,'startCharacter':a,'endCharacter':b,'canonicalIds':sorted(ids),'originalSha256':c.sha(text[a:b].encode())} for a,b,ids,h in spans],
            'originalBytesPreserved':all(text[a:b] in rendered for a,b,_,_ in spans)})
    return result


def inventory(elements,retired):
    files=subprocess.run(['git','ls-tree','-r','--name-only',ENTRY,'docs/markdown'],cwd=c.ROOT,check=True,capture_output=True,text=True).stdout.splitlines()
    sources=defaultdict(set)
    for cid,(m,_,_) in elements.items():
        for p in m['provenance']:sources[p['source']].add(cid)
    retired_by={r['source']:r for r in retired}
    records=[]
    for file in files:
        text=original(file) if not file.endswith('.gz') else ''
        active='partially incorporated definitions and qualified source assertions'
        reason='Exact incorporated ranges have canonical successors listed below. Unwrapped passages retain their original standing; they are not a second canonical representation of retired definitions.'
        if '/baseline/' in file or '/architecture-identity/' in file or '/reference-model/' in file:
            active='historical capture/publication or identity-binding evidence';reason='Current resource authoring succeeds to canonical continuum Markdown. Frozen captures/aliases retain historical evidence and replay roles; they allocate no new identity.'
        elif '/reviews/' in file:
            active='retained decision/review evidence';reason='Adjudications retain their original scope and standing. A review record is not itself an architectural element.'
        elif '/design-notes/' in file:
            active='retained source/adjudication evidence';reason='Sources U01–U19 distinguish working-note provenance from incorporated authority. Only U18/U19 semantic contract sections are retired here after explicit incorporation; original note text remains unchanged.'
        elif '/scratchpad/' in file:
            active='historical working/publication evidence';reason='Scratchpad text and LaTeX copies confer no new architectural identities or standing.'
        elif '/governance/' in file and '/04-information-' not in file:
            active='maintained governance, metamodel or machinery contract';reason='Governance controls architectural activity; native mapping and identity contracts remain governing. Runtime architectural assertions such as axioms are retired only at their incorporated sections.'
        elif any(k in file for k in ['/metamodel/','/guardrails/modelling-guardrails','capability-tier-model','ict-foundation-lenses','/traceability/step-b-method']):
            active='maintained metamodel/modelling/derivation governance';reason='Rules/lenses are not fabricated instances. Runtime Information Unit sections have individual canonical successors; unretired modelling rules retain their governance role.'
        elif file.endswith(('README.md','index.md','orientation-view.md','reclassified-principles.md')) or file.endswith('principles/architectural-axioms.md'):
            active='navigation/scope and standing';reason='Navigation, programme status and conceptual reading order do not create additional architectural elements.'
        elif file.endswith('architecture-completion-plan.md'):
            active='programme scope and sequencing authority';reason='Programme steps are not automatically WorkPackages, Plateaus or architecture acceptance.'
        elif file.endswith(('sources.md','gaps.md')):
            active='source adjudications and unresolved review record';reason='Retains qualified evidence, closed decisions and deliberate design deferrals; no invented native Gap or Assessment.'
        if file.endswith(('actors-roles/actors.md','patterns/containment-and-collections.md','traceability/domain03-traceability.md')):
            active='maintained metamodel/modelling/derivation governance'
            reason='Actor categories/examples, qualified information-containment rules or traceability rules govern modelling. They do not instantiate example actors, counterpart objects or native edges.'
        if file.endswith(('traceability/b2-1-individual-care-delivery-health-information-access.md','traceability/health-information-access-worked-example.md')):
            active='retained traceability derivation and worked-example evidence'
            reason='Established support boundaries and explicit non-equivalences explain existing elements; illustrative associations are not new identities or native edges. Canonical source correspondence remains explicit, not name-based.'
        issues=[]
        if file.endswith('external-authorities.md'):issues=['Q-03: governing-body interest/operational identity facets are not adjudicated as separate native Stakeholders/Actors; preserve without coercing source statement that these are not enterprise stakeholders.']
        if file.endswith('master-requirements-catalogue.md'):issues=['Q-04: downstream Candidate CORE/SEC/PROV/INT/APP/DIR ranges require owner reconciliation and explicit identity correspondence.']
        if file.endswith(('assemblies-and-views.md','patterns/information-relationships.md')):issues=['Q-05: explicit correspondence between conceptual candidates/reified Relationship and accepted authored Topics is unestablished. Do not create duplicate or inferred cross-level counterparts.']
        if any(file.endswith('information-families/'+x+'.md') for x in ['referral','appointment-scheduling','episode-encounter','order']):issues=['Q-05: remaining G2 candidate constructs and accepted later Topics lack explicit same-instance or distinct-instance correspondence. Exact approved existing correspondences are incorporated; other candidates stay qualified here.']
        if file.endswith(('logical-component-responsibilities.md','candidate-system-map.md')):issues=['Q-06: Strategy construct facets versus ApplicationComponents, especially Digital Twin, lack a same-instance mapping; retain strategic meanings and diagram predicates without merging by name.']
        if file.endswith(('component-register.md','pathways-journeys-and-handoff.md','pylai-external-interoperability.md')):issues=['Q-07: native classifications/identity for Ergo pattern, Erga collection, Pathway/Segment/Traversal/Route/Horos/Terma and composite support bundles remain expressly open in the mapping catalogue.']
        if file.endswith(('candidate-feature-map.md','data-and-persistence.md','middleware.md')):issues=['Q-08: unallocated proposals, source carrier shapes and unresolved control facets remain qualified; F/D/M assertion contributions are not separate Features/DataObjects/Components by row.']
        if file.endswith('DesignNote-ClassAndPackage.md'):issues=['Q-09: note explicitly defers native classification; Class/Package are not inferred ArchiMate element types.']
        if file.endswith(('dependencies/cross-capability-dependencies.md','information-responsibility/information-responsibility.md','patterns/definition-to-accountability.md')):
            issues=['Q-10: retained source-qualified ownership/consumption and cross-level assertions need an explicit instance/endpoint crosswalk; labels and similar names cannot establish it. Generic modelling invariants remain governing; no source predicate is silently discarded or coerced into a native edge.']
        r=retired_by.get(file,{})
        records.append({'source':file,'entryRevision':ENTRY,'entrySha256':c.sha(subprocess.run(['git','show',ENTRY+':'+file],cwd=c.ROOT,check=True,capture_output=True).stdout),
                        'role':active,'reason':reason,'canonicalIds':sorted(sources[file]),'retiredSectionCount':len(r.get('retiredSections',[])),'unresolved':issues})
    return {'entryRevision':ENTRY,'filesProcessed':len(records),'roleCounts':dict(Counter(r['role'] for r in records)),
            'retiredSourceSections':sum(len(r['retiredSections']) for r in retired),'sourcesWithRetiredSections':len(retired),
            'interpretation':'Inventory records examined source roles and exact incorporation, not a claim that every source assertion has architectural acceptance or every retained paragraph is incorporated.', 'sources':records}


def main():
    catalogue,elements=c.load();selected=selection()
    # Link rebasing precedes wrappers and uses exact source anchors/heading bindings.
    links=rebase_links(elements,selected)
    retired=retire(elements,selected)
    (AFTER/'source-succession.json').write_bytes(c.encoded(retired))
    (AFTER/'corpus-accounting.json').write_bytes(c.encoded(inventory(elements,retired)))
    if links['rebasedElements'] or not (AFTER/'source-link-rebasing.json').exists():
        (AFTER/'source-link-rebasing.json').write_bytes(c.encoded(links))
    from finalize_continuum_succession import finalize
    finalize()
    print('Retired',sum(len(r['retiredSections']) for r in retired),'sections across',len(retired),'sources; source text preserved.')
    print('Source link gaps:',len(links['unresolvedSourceLinks']))
if __name__=='__main__':main()
