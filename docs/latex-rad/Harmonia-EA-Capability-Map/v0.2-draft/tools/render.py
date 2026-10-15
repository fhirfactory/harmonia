#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render the committed Step 2 model; never allocate identities or alter v0.1."""
import collections
import hashlib
import importlib.util
import json
import re
import sys
from pathlib import Path

BASE=Path(__file__).resolve().parents[1]
V01=BASE.parent
ROOT=V01.parents[2]
spec=importlib.util.spec_from_file_location('v01_render',V01/'tools/render.py')
v01=importlib.util.module_from_spec(spec);spec.loader.exec_module(v01)
M=json.loads((BASE/'model.json').read_text())
E={e['key']:e for e in M['elements']}
R=M['relationships']
RECON=json.loads((BASE/'v0.3-reconciliation.json').read_text())
CORPUS=json.loads((BASE/'corpus-register.json').read_text())
esc=v01.esc
table=v01.table
ref=v01.ref


def code(s):
    return r'\texttt{\seqsplit{'+esc(s)+r'}}'


def prose(s):
    return v01.markdown(s,'metamodel')


def evidence_label(path):
    return 'evidence:'+hashlib.sha256(path.encode()).hexdigest()[:16]


def cite(citation):
    if 'source' in citation:
        return v01.sourceref(citation['source'],citation['line'])
    path=citation['path']; label=evidence_label(path)
    return r'\hyperref['+label+']{'+esc(Path(path).name)+':'+str(citation['line'])+'}'


def chain(e):
    result=[];seen={e['key']}
    while e.get('parent_key') or e.get('owner_key'):
        e=E[e.get('parent_key') or e['owner_key']]
        assert e['key'] not in seen,'Cyclic placement'
        seen.add(e['key']);result.insert(0,e)
    return result


def title(e):
    if e['type']=='capability':
        return ('BC ' if e['category']=='business' else 'BEC ')+e['tier']+': '+e['name']
    if e['type']=='enterprise_capability':
        return 'Enterprise '+e['tier']+': '+e['name']
    return e['type'].title()+': '+e['name']


def field(heading, text):
    return '\\paragraph*{'+esc(heading)+'}\n'+text+'\n'


def metadata(e):
    status=e['architectural_status']
    rows=[['Reference Id',code(e['reference_id']),'Stable concept identity.'],
          ['Element status',status,'Canonical responsibility retained from v0.1.' if status=='Canonical' else
           'Preserved Candidate concern; not promoted.' if status=='Candidate' else 'Step 2 architectural proposal.'],
          ['Type / tier',esc(e['type'].replace('_',' '))+' / '+(e['tier'] or 'behaviour'),'Type is independent of heading depth.'],
          ['Direct placement',e['placement_status'],'Root placement.' if not(e.get('parent_key') or e.get('owner_key')) else 'Immediate context; complete path status is separate.'],
          ['Complete structural assertion',e['structural_id_status'],'Proposed ancestry does not demote a Canonical responsibility.'],
          ['Structural Canonical ID',code(e['canonical_id']),'Current proposed context where marked Proposed.']]
    ancestors=chain(e)
    if ancestors:
        rows.append(['Current context',r' $\rightarrow$ '.join(ref(a) for a in ancestors),'Linked responsibility ancestry.'])
    if e.get('id_aliases'):
        rows.append(['Historical ID aliases','; '.join(code(a) for a in e['id_aliases']),'Direct to this identity; never reusable.'])
    if e.get('canonical_name') and e['name']!=e['canonical_name']:
        rows.append(['Established name',esc(e['canonical_name']),'Current display rename is Proposed; meaning unchanged.'])
    if e.get('name_aliases'):
        rows.append(['Name history',esc('; '.join(e['name_aliases'])),'Names do not establish another identity.'])
    if e['type']=='capability':
        rows.append(['Harmonia relevance',esc(e.get('relevance') or 'Unresolved in the semantic baseline'),'Independent of element/placement status; never inherited from an ancestor.'])
    if e.get('responsibility_owner_key') and e['responsibility_owner_key']!=e.get('owner_key'):
        rows.append(['Established broader owner',ref(E[e['responsibility_owner_key']]),'Preserved accountability; narrower CT3 frame Proposed.'])
    citations=list(e.get('sources',[]))+e.get('provenance',{}).get('evidence',[])
    if citations:
        rows.append(['Evidence', '; '.join(dict.fromkeys(cite(s) for s in citations)), 'Source standing does not approve the derived assertion.'])
    return table(['Property','Value','Meaning'],rows,[.20,.48,.32])


def explanatory(e):
    baseline=e.get('baseline_description',{})
    overview=e.get('overview') or e.get('proposal_overview') or baseline.get('Capability Overview') or baseline.get('Feature Overview') or baseline.get('Process Overview') or baseline.get('Function Overview') or baseline.get('Service Overview')
    # Some canonical behaviour has only exact governed description; that description
    # is sufficient and is not replaced with a generic generated paraphrase.
    out=[]
    if overview:
        heading='Capability Overview' if e['type'] in ('capability','enterprise_capability') else e['type'].title()+' Overview'
        out.append(field(heading,prose(overview)))
    exact=e.get('governed_definition_latex')
    if exact:
        out.append(field('Definition / governed semantic detail',exact))
    if e.get('definition'):
        out.append(field('Definition — Proposed',prose(e['definition'])))
    if e.get('proposal_definition'):
        out.append(field('Semantic elaboration — Proposed',prose(e['proposal_definition'])))
    # Keep explanatory v0.1 prose distinguishable when it records an unresolved
    # structural position. It is never rewritten as though it anticipated Step 2.
    for heading,text in baseline.items():
        if heading.endswith('Overview'):continue
        out.append(field(heading+(' — v0.1 explanation' if any(s in text.lower() for s in ('unresolved','not establish','no feature','not inferred')) else ''),prose(text)))
    if e.get('intent') and e['intent']!=e.get('overview'):
        out.append(field('Intent',prose(e['intent'])))
    if e.get('boundary') or e.get('proposal_boundary'):
        out.append(field('Boundary',prose(e.get('boundary') or e['proposal_boundary'])))
    if e.get('placement_rationale') and e['placement_rationale']!=overview:
        out.append(field('Proposed Placement Rationale' if e['placement_status']=='Proposed' else 'Responsibility Context',prose(e['placement_rationale'])))
    if e.get('alternative_considered'):
        out.append(field('Alternative Considered / Review Note',prose(e['alternative_considered'])))
    if e.get('naming_rationale'):
        out.append(field('Proposed Naming Decision',prose(e['naming_rationale'])))
    if e.get('confidence') in ('Low','Medium'):
        out.append(field('Review Confidence',esc(e['confidence'])+'; applies to the proposal, not to preserved canonical meaning.'))
    if e.get('provenance'):
        p=e['provenance']
        out.append(field('Derivation Provenance',esc(p['derivation']+'. '+p['method'])+' '+
                         '; '.join(cite(c) for c in p['evidence'])))
    prior=[r for r in RECON['primary_concepts'] if e['key'] in r['targets'] and r['disposition'] in
           ('Refined / repositioned','Retained Proposed','Retained Proposed responsibility','Redesigned grouping','Integrated reusable responsibility')]
    if prior:
        out.append(field('Prior Candidate Derivation',
            '\\hyperref[model:reconciliation]{v0.3 reconciliation}: '+ '; '.join(code(r['candidate_reference_id'])+' (line '+str(r['line'])+')' for r in prior[:8])+
            ('. Additional locators are retained in derivation-provenance.json.' if len(prior)>8 else '. Working locators are not identity aliases.')))
    # Display only material unresolved semantic matters, not the now-completed
    # allocation/tier tasks recorded in the frozen source baseline.
    remaining=[u for u in e.get('unresolved',[]) if u not in ('feature_decomposition','feature_association','function_mapping','exposure_consumer','strategy_catalogue_correspondence','relevance','name_uniqueness')]
    if remaining:
        out.append(field('Preserved v0.1 Review Matters',prose('\n\n'.join(M['unresolved_meanings'][u] for u in remaining))))
    return ''.join(out)


def scope(e):
    children=[x for x in E.values() if x.get('parent_key')==e['key'] or x.get('owner_key')==e['key']]
    if not children:return ''
    return field('Scope / Contained Elements',table(['Element / page','Type / tier','Element / placement status'],
        [[ref(c,True),esc(c['type'].replace('_',' '))+' '+(c['tier'] or ''),c['architectural_status']+' / '+c['placement_status']] for c in children],[.52,.20,.28]))


def local_relationships(e):
    rels=[r for r in R if r['type'] not in ('capability_decomposition','responsibility_parent') and (r['from']==e['key'] or r['to']==e['key'])]
    if not rels:return ''
    # The relationship register holds evidence/rationale once; local entries retain
    # full typed navigation and status rather than another unauditable prose claim.
    rows=[]
    for r in rels:
        outgoing=r['from']==e['key']; other=E[r['to'] if outgoing else r['from']]
        status='Established' if r['status']=='established' else 'Unresolved association' if r['status']=='unresolved' else 'Proposed'
        rows.append([esc(('To: ' if outgoing else 'From: ')+r['type'].replace('_',' ')),ref(other,True),status+(' / '+esc(r['contribution']) if r.get('contribution') else '')])
    return field('Relationships / Architectural Relevance',table(['Relationship','Related element','Assertion status'],rows,[.27,.48,.25]))


def entry(e,depth):
    cmd=['section','subsection','subsubsection','paragraph'][min(depth,3)]
    star='*' if depth>=3 else ''
    out=['\\Needspace{8\\baselineskip}\n\\'+cmd+star+'{'+esc(title(e))+'}\n\\phantomsection\\label{'+e['latex_label']+'}\n',
         '\\index{'+esc(e['name'].replace('!',' ').replace('@',' '))+'}\n',metadata(e),explanatory(e),scope(e)]
    if e.get('ordered_function_chain'):
        out.append(field('Proposed Ordered Function Chain',r' $\rightarrow$ '.join(ref(E[k],True) for k in e['ordered_function_chain'])))
    out.append(local_relationships(e))
    return ''.join(out)


def hierarchy(category,heading,label):
    out=['\\chapter{'+heading+'}\\label{'+label+'}\n']
    if category=='business':
        out.append('The established 190-node BC hierarchy is retained. Six Proposed CT3 refinements and one Proposed preparedness CT2 are additive. The three existing Candidate concerns retain Candidate status and receive Proposed placement. Clinical and enterprise authority remain separate from Harmonia information support.\n\n')
    elif category=='business_enabling':
        out.append('Seven proposed CT1 frames organise the enabling architecture independently of the five historical views. Existing responsibilities remain Canonical, while inferred decomposition and Feature framing are Proposed. Functions, Services and Processes may remain directly Capability-owned; they are not printed as a compulsory derivation chain.\n\n')
    else:
        out.append('EC-01 through EC-14 retain Canonical responsibility, stable Reference ID and exact governed scope. Three Proposed CT1s and selected CT3 specialisations form one integrated hierarchy. Historical EC tokens are direct structural-ID aliases. No Feature is placed under an EC, and no application-component or deployment allocation is inferred.\n\n')
    roots=[e for e in E.values() if e['category']==category and e['tier']=='CT1']
    out.append(table(['CT1 / page','Element status','Placement'],[[ref(e,True),e['architectural_status'],e['placement_status']] for e in roots],[.64,.18,.18]))
    def walk(e,depth):
        out.append(entry(e,depth))
        for c in E.values():
            if c.get('parent_key')==e['key']:walk(c,depth+1)
        for c in E.values():
            if c.get('owner_key')==e['key']:
                out.append(entry(c,3))
                for b in E.values():
                    if b.get('owner_key')==c['key']:out.append(entry(b,3))
    for root in roots:walk(root,0)
    return ''.join(out)


def relationships():
    out=['\\chapter{Relationship register}\\label{model:relationships}\n',
         'An edge has its own assertion status. Canonical endpoints do not promote a Proposed edge. Unresolved associations are retained non-edges. Enablement does not mean decomposition, runtime dependency, implementation equivalence or transfer of originating authority; mappings are not inherited by descendants.\n\n']
    for typ in dict.fromkeys(r['type'] for r in R):
        out.append('\\section{'+esc(typ.replace('_',' ').title())+'}\n')
        rows=[]
        for r in R:
            if r['type']!=typ:continue
            status='Established' if r['status']=='established' else 'Unresolved; not an asserted association' if r['status']=='unresolved' else 'Proposed'
            detail=esc(r.get('rationale') or r.get('contribution') or '')
            if r.get('sources'):detail+=' '+ '; '.join(cite(c) for c in r['sources'])
            if r.get('supporting_responsibility_elements'):
                detail+=' Examples: '+ '; '.join(ref(E[k]) for k in r['supporting_responsibility_elements'])
            rows.append([ref(E[r['from']],True),ref(E[r['to']],True),status+'\n\n'+detail])
        out.append(table(['From','To','Assertion / rationale'],rows,[.27,.27,.46]))
    out.append('\\section{Established absence constraints}\nThe approved assurance matrix explicitly records No Material Role for these pairs; this is preserved rather than filled by analogy.\n')
    out.append(table(['BEC','EC','Constraint'],[[ref(E[r['from']]),ref(E[r['to']]),esc(r['contribution'])] for r in M['relationship_constraints']]))
    return ''.join(out)


def views():
    out=['\\chapter{Contextual views and prior descriptions}\\label{model:contexts}\n',
         'These views provide navigation across the proposed hierarchy. Membership remains different from decomposition. In entity information, distinct named descriptions become Proposed CT3s. Elsewhere the Canonical responsibility itself usually provides CT3 framing, so an old description is not mechanically made another tier.\n\n']
    for v in M['views']:
        if v['kind']!='business_enabling_view':continue
        out.append('\\section{'+esc(v['name'])+'}\n')
        cs=[c for c in M['catalogue_contexts'] if c['view']==v['key']]
        rows=[]
        for c in cs:
            res=next(x for x in M['catalogue_context_resolutions'] if x['context']==c['key'])
            e=E[res['element']]
            rows.append([esc(c['name']),ref(e,True),esc(e['tier'])+' / '+e['architectural_status']+'; placement Proposed'])
        out.append(table(['Source contextual description','Current responsibility frame','Standing'],rows,[.37,.40,.23]))
    return ''.join(out)


def identities():
    out=['\\chapter{Identity and change registers}\\label{model:identifiers}\n',
         'Reference IDs and LaTeX labels are stable. Structural IDs describe current proposed architecture; ID aliases are allocated historical values resolving directly to the same element. Old v0.3 working IDs are provenance locators, not identity aliases.\n\n']
    out.append(table(['Reference / structural identity','Element / page','Element / path standing'],
        [[code(e['reference_id'])+'\n\n'+code(e['canonical_id']),ref(e,True),e['architectural_status']+' / '+e['structural_id_status']] for e in sorted(E.values(),key=lambda x:x['reference_id'])],[.44,.35,.21]))
    out.append('\\section{Historical allocated IDs}\n')
    out.append(table(['Historical ID alias','Current identity','Current structural ID'],
        [[code(a),ref(e,True),code(e['canonical_id'])] for e in E.values() for a in e['id_aliases']],[.15,.37,.48]))
    return ''.join(out)


def reconciliation():
    out=['\\chapter{v0.3 concept reconciliation}\\label{model:reconciliation}\n',
         'All 942 primary concepts and 277 captured behaviour occurrences were inspected against the v0.1 substrate and current responsibility boundaries. The machine-readable reconciliation retains an individual disposition, exact source line, target identities and rationale for each. Old working IDs do not become aliases, and generated pattern repetition does not create Canonical responsibility.\n\n']
    out.append(table(['Disposition','Concept count'],[[esc(k),str(v)] for k,v in RECON['summary'].items()],[.8,.2]))
    # Full decisions are useful but repeated generic prose belongs in JSON. The
    # reference book includes every concept's ID, placement/meaning target and
    # disposition; detailed semantic reasons remain in the reviewable register.
    for category in ('business','business_enabling','enterprise'):
        out.append('\\section{'+esc(category.replace('_',' ').title())+'}\n')
        rows=[]
        for r in RECON['primary_concepts']:
            if r['category']!=category:continue
            rows.append([code(r['candidate_reference_id'])+'\n\n'+esc(r['name'])+' (line '+str(r['line'])+')',
                         esc(r['disposition']),'; '.join(ref(E[k]) for k in r['targets']) or 'Non-element placeholder'])
        out.append(table(['Prior concept / source line','Decision — Proposed','Current responsibility / reference'],rows,[.38,.22,.40]))
    return ''.join(out)


def sources():
    out=['\\chapter{Source and derivation provenance}\\label{model:sources}\n',
         'The frozen v0.1 source transcriptions retain their exact governed wording, including unresolved statements that Step 2 proposes to resolve structurally. The corpus register fingerprints current material surveyed for broader context. Citation is evidence, not a change to source standing. The v0.3 candidate is explicitly authorised prior reasoning, not authority. No superseded legacy architecture is imported.\n\n']
    for c in CORPUS:
        path=c['path']
        out.append('\\phantomsection\\label{'+evidence_label(path)+'}\n')
        out.append(table(['Source','Fingerprint / role'],[[code(path),code(c['sha256'])+'\n\n'+esc(c['role'])]],[.56,.44]))
    return ''.join(out)


def guide():
    counts=collections.Counter(e['architectural_status'] for e in E.values())
    out=['\\chapter{Reading the proposed architecture}\\label{model:guide}\\label{guide}\n',
         'Version 0.2-draft is Step 2: a coherent proposed architecture for semantic and structural review. It does not replace the governed Markdown RAD or declare any architecture domain complete. The 0.1 publication remains independently reproducible in the parent directory. Its identities, definitions, semantic boundaries, source traceability and established relationships are preserved.\n\n',
         'Element, naming, direct-placement, complete-path and relationship status are separate assertions. A Canonical responsibility can have a Proposed CT3 placement and Proposed structural identifier. A Candidate concern remains Candidate even when a best placement is proposed. Source uncertainty is retained in the governed appendix; the proposal makes a current judgement rather than leaving the concept floating.\n\n',
         'Capability, Feature, Function, Service and Process remain different constructs. Every Feature is framed by BEC CT3 with complete CT1/CT2 ancestry. Directly Capability-owned behaviour remains valid. Services expose a client contract; Processes chain Functions where meaningful. None of those semantics is a mandatory Feature-to-Function-to-Service-to-Process hierarchy.\n\n',
         'The fourteen Canonical EC responsibilities become CT2s under three Proposed CT1 frames. The application of the structural ID grammar to ECs is itself Proposed; EC-01 through EC-14 remain stable Reference IDs and historical structural aliases. Reuse does not mandate centralised services, separate processes or distributed responsibility.\n\n',
         'Downstream Application Component derivation may use this current proposed model as its working assumption, while retaining every assertion status. Derive cohesive responsibility centres from capabilities; do not force them to reproduce current Mneme, Mnemosyne, Ponos, Calliope or other component boundaries. No capability in this publication allocates an application component, module, process or deployment.\n\n',
         'AX-01/04 preserve healthcare meaning over machinery; AX-05 separates active use from durable establishment; AX-06 qualifies source authority; AX-07/08/09 distinguish governed action, meaningful evidence and diagnostics; AX-10/11/12 preserve operational integrity; AX-13 governs boundary publication; AX-14/15/17 preserve distinctions, uncertainty and authority; AX-16/18 preserve entity progression without inferred domain authority. CM-R01 through CM-R05 are reviewed through complete placement, bounded responsibility, explicit definitions, cohesive roots and unique current display names.\n\n']
    out.append(table(['Standing','Elements'],[[k,str(v)] for k,v in counts.items()],[.7,.3]))
    out.append(prose((BASE/'catalogue/review-focus.md').read_text()))
    return ''.join(out)


def emit(path,text):
    p=BASE/path
    if path.endswith('.tex'):
        text='% Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later\n% Generated from the explicit 0.2-draft model; all assertion status is retained.\n'+text
    text='\n'.join(l.rstrip() for l in text.splitlines()).rstrip()+'\n'
    if '--check' in sys.argv:assert p.read_text()==text,'Generated publication drift: '+path
    else:p.write_text(text)


def main():
    v01.source_sections()
    emit('sections/reading-guide.tex',guide())
    for category,heading,label,file in [
        ('business','Business Capabilities','model:bc','business-capabilities'),
        ('business_enabling','Business Enabling Capabilities','model:bec','business-enabling-capabilities'),
        ('enterprise','Enterprise Capabilities','model:ec','enterprise-capabilities')]:
        emit('sections/'+file+'.tex',hierarchy(category,heading,label))
    emit('sections/relationships.tex',relationships())
    emit('sections/catalogue-contexts.tex',views())
    emit('sections/identifiers.tex',identities())
    emit('sections/reconciliation.tex',reconciliation())
    emit('sections/sources.tex',sources())
    # Preserve the proven v0.1 transcriber and complete line-range coverage.
    inputs=[]
    for source in v01.S:
        path='sections/rad-'+source+'.tex';emit(path,v01.projection(source))
        inputs.append('\\input{'+path+'}')
    emit('sections/rad-detail.tex','\n'.join(inputs))
    emit('source-coverage.json',json.dumps(v01.COVERAGE,indent=2))
    proposals=['<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->',
               '# Step 2 proposed architecture catalogue','',
               'Generated from the explicit authored proposals in tools/compose.py; status is Proposed throughout.','']
    for e in E.values():
        if e['architectural_status']!='Proposed':continue
        proposals.extend(['## '+e['reference_id']+' — '+e['name'],'',
                          '**Type / tier:** '+e['type']+' / '+(e['tier'] or 'behaviour'),'',
                          '**Definition:** '+e['definition'],'',e['overview'],'',
                          '**Intent:** '+e.get('intent',e['overview']),'',
                          '**Boundary:** '+e['boundary'],'',
                          '**Current structural identity:** '+e['canonical_id'],'',
                          '**Provenance:** '+e['provenance']['derivation'], ''])
    emit('catalogue/proposals.md','\n'.join(proposals))
    print('Rendered',len(E),'elements and complete frozen source semantic detail')


if __name__=='__main__':main()
