#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only, explicitly scoped Subject/navigation assessment; evidence only.

The full export delta is never an interpretation input. The user authorises
G14-G18 and incident relationships only. Folder ordinals and labels establish
candidate-mapping correspondence, never canonical identities or semantic edges.
"""
import csv
import hashlib
import json
from pathlib import Path
import re
import sys
ROOT=Path.cwd()
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
from exchange_compare import Exchange
from exchange_xml import N, TYPE
from governed_naming import component
from lxml import etree as ET

BOUNDARY=ROOT/'enterprise-architecture/design/evidence/1.1.18/001'
OUT=BOUNDARY/'after/bounded-subject-review'
PRIOR=BOUNDARY/'after/reconciliation'
SUBMISSION=BOUNDARY/'before/Harmonia-Continuum-1.1.18-001-004.xml'
BASELINE=ROOT/'enterprise-architecture/design/archimate/published/1.1.18/Harmonia-Architecture.xml'
MAPPING=ROOT/'enterprise-architecture/design/archimate/working/1.1.18/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md'
TASK=BOUNDARY/'before/ai-agent-tasks/unresolved-bounded-subject-interpretation.md'
OUT.mkdir(exist_ok=True)
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
def locator(p):return dict(path=str(p.relative_to(ROOT)),sha256=sha(p),bytes=p.stat().st_size)
def write(name,data):
    (OUT/name).write_text(json.dumps(data,ensure_ascii=False,indent=2)+'\n')
def label(node):return node.findtext('a:label',namespaces=N)
def ordinal(node):
    match=re.match(r'^(\d+)\.\s+',label(node) or '')
    return int(match[1]) if match else None
def bare(node):return re.sub(r'^\d+\.\s+','',label(node) or '')

catalogue,elements=c.load()
before,incoming=Exchange(BASELINE),Exchange(SUBMISSION)
assert not before.issues and not incoming.issues
pins=json.loads((PRIOR/'input-pins.json').read_text())
assert sha(BASELINE)==pins['baseline']['sha256'] and sha(SUBMISSION)==pins['submission']['sha256'] and sha(MAPPING)==pins['mapping']['sha256']
projected,validation=c.project(ROOT)
assert projected==BASELINE.read_bytes()
# The maintained source is authority; only source-bound Subject instances supply
# existing Subject identity evidence. Native Grouping label collisions are leads.
subjects={m['subjectGroup']['sourceId']:(m,b,p) for m,b,p in elements.values() if 'subjectGroup' in m}
assert len(subjects)==82
groups=[m for m,b,p in elements.values() if m['archimateType']=='Grouping']
source_rows=json.loads((PRIOR/'candidate-subject-hierarchy.json').read_text())
assert len(source_rows)==32
candidate_by_code={r['code']:r for r in source_rows}

view_roots=[n for n in incoming.root.findall('a:organizations/a:item',N) if label(n)=='Views']
assert len(view_roots)==1
branches=[n for n in view_roots[0].findall('a:item',N) if label(n)=='1. Subject-Based-Views']
assert len(branches)==1
branch=branches[0]
scope_roots=[n for n in branch.findall('a:item',N) if ordinal(n) in range(14,19)]
assert [ordinal(n) for n in scope_roots]==list(range(14,19))
scope_folders=[n for root in scope_roots for n in root.iter('{'+N['a']+'}item')]
assert len(scope_folders)==32
assert not any(node.get('identifier') or node.get('identifierRef') for node in scope_folders)
assert not any(node.findall('a:properties',N) or node.findall('a:documentation',N) for node in scope_folders)
assert not any(node.findall('.//a:item[@identifierRef]',N) for node in scope_roots)

# A separate machine inventory scans the complete element table for explicit
# native names/source claims corresponding to the scoped codes. It contributes
# only absence/collision evidence; it does not interpret other submitted changes.
def explicit_claims(node):
    claims={}
    for key in incoming.definition_ids:
        if key in ('canonicalId','displayName','friendlyName','canonicalName','Specialization','specialization') or re.search('subject|source.*(?:id|key)|identifier',key,re.I):
            values=incoming.texts(node,key)
            if values:claims[key]=values
    return claims

native_facts=[(ex,element.findtext('a:name',namespaces=N) or '',element.get(TYPE),explicit_claims(element)) for ex,element in incoming.elements.items()]
rows=[];fragments=[]
for group in scope_roots:
    group_code=f'G{ordinal(group):02}'
    items=[(group_code,group)]+[(group_code+f'.{ordinal(child):02}',child) for child in group.findall('a:item',N)]
    expected_children=[s['code'] for s in source_rows if s['code'].startswith(group_code+'.')]
    assert [code for code,node in items[1:]]==expected_children
    fragments.append(dict(group=group_code,sourceXPath=incoming.root.getroottree().getpath(group),sourceLine=group.sourceline,serialization='Derived secure-parser fragment; exact source remains pinned input, not byte-identical standalone XML',xml=ET.tostring(group,encoding='unicode',with_tail=False)))
    for code,node in items:
        src=candidate_by_code[code]
        native_hits=[]
        for ex,native_name,native_type,claims in native_facts:
            values=[v for vals in claims.values() for v in vals]
            if native_name in (src['name'],bare(node),code) or code in values:
                native_hits.append(dict(exchangeId=ex,nativeName=native_name,nativeType=native_type,claims=claims))
        # A code-qualified source binding or canonicalId could establish identity;
        # lexical spelling alone cannot. There is no such binding in this input.
        existing_bindings=[dict(canonicalId=m['canonicalId'],source=str(p.relative_to(ROOT))) for m,b,p in elements.values() if any(binding.get('source')==str(MAPPING.relative_to(ROOT)) and binding.get('selector')==code for binding in m['sourceBindings'])]
        lexical_leads=[dict(canonicalId=m['canonicalId'],name=m['displayName'],specialization=m['specialization'],standing=m['standing']) for m in groups if component(m['displayName'])==component(src['name'])]
        lexical_equal=component(bare(node))==component(src['name'])
        spelling='TYPO_INFORMATION_OMITS_I' if code=='G18.03' else ('LEXICALLY_EQUIVALENT_LABEL_FORMAT' if bare(node)!=src['name'] else 'EXACT_LABEL_TEXT')
        assert lexical_equal or code=='G18.03'
        rows.append(dict(code=code,role='group' if '.' not in code else 'subgroup',mappingName=src['name'],mappingConcern=src['concern'],mappingSelector='Proposed Subject extensions / '+code,folderLabel=label(node),folderPath=['Views','1. Subject-Based-Views']+([label(group)] if node is group else [label(group),label(node)]),sourceXPath=incoming.root.getroottree().getpath(node),sourceLine=node.sourceline,folderAttributes=dict(node.attrib),folderCanonicalId=None,folderIdentifier=None,folderIdentifierRef=None,viewReferences=0,nativeElementReviewLeads=native_hits,existingGovernedCodeBinding=subjects[code][0]['canonicalId'] if code in subjects else None,existingScopedSourceBindings=existing_bindings,existingGroupingLexicalLeads=lexical_leads,identityFinding='NO_ARCHITECTURAL_INSTANCE_IDENTITY_IN_EXPORT; proposed Subject requires separately authorised creation after duplicate/meaning adjudication',nameFinding=spelling,proposedDisplayName=src['name'],proposedFriendlyName=component(src['name']),proposedCanonicalName=None,canonicalNameReason='Native Grouping has no established formal naming qualifier; preserve existing unresolved vocabulary; no Subject/GRP token or ancestor qualification invented',submittedNativeType=None,submittedSpecialization=None,proposedNativeType='Grouping',proposedSpecialization=None,typeAuthority='continuum-representation.md / Iteration 001 subject groups/subgroups; this is a proposed extension, not an imported native assertion',proposedParentCode=group_code if '.' in code else None,prospectiveNavigation=['Cross-Cutting References','Architectural Subjects',src['name']] if '.' not in code else ['Cross-Cutting References','Architectural Subjects',candidate_by_code[group_code]['name'],src['name']],navigationQualification='Candidate context only; storage remains native grouping/<allocated UUID>.md; subject folder/ordinal does not become identity or naming ancestry',incorporation='NOT AUTHORISED',canonicalIdAllocated=None))

assert all(not r['nativeElementReviewLeads'] and not r['existingScopedSourceBindings'] and r['existingGovernedCodeBinding'] is None for r in rows)
assert all(not r['existingGroupingLexicalLeads'] for r in rows)
# No scoped folder supplies a native element endpoint. Do not substitute folder
# labels, implicit codes or unallocated UUIDs into semantic relationship tables.
scoped_element_ids={h['exchangeId'] for r in rows for h in r['nativeElementReviewLeads']}
incident=[dict(identifier=key,source=edge.get('source'),target=edge.get('target'),nativeType=edge.get(TYPE)) for key,edge in incoming.relationships.items() if edge.get('source') in scoped_element_ids or edge.get('target') in scoped_element_ids]
assert not scoped_element_ids and not incident
proposed_edges=[dict(parentCode=r['proposedParentCode'],childCode=r['code'],nativeType='Composition',direction='parent to child',meaning='Proposed Subject subgroup classification containment only; not Home, capability decomposition, implementation or authority assignment',incomingRelationship=None,proposedRelationshipIdentifier=None,sourceCanonicalId=None,targetCanonicalId=None,standing='Proposal for Solution Architect adjudication; not asserted',basis='Explicit proposed hierarchy in candidate mapping and user extension intent, applying governed Subject classification pattern; NOT inferred from XML folder nesting') for r in rows if r['proposedParentCode']]
assert len(proposed_edges)==27
context=[dict(code=code,canonicalId=m['canonicalId'],exchangeId=m['exchange']['identifier'],displayName=m['displayName'],nativeType=m['archimateType'],specialization=m['specialization'],standing=m['standing'],source=str(path.relative_to(ROOT))) for code,(m,body,path) in sorted(subjects.items())]

payload=dict(schemaVersion=1,scope='G14-G18 folders and proposed Subjects, their necessary identity/typing/naming context and direct incident relationships only',authority=locator(TASK),inputs=dict(baseline=locator(BASELINE),submission=locator(SUBMISSION),mapping=locator(MAPPING)),limitBytes=128000,fullDelta='Retained unchanged and excluded from interpretation; remains above limit',folders=rows,derivedSourceFragments=fragments,governedSubjectContext=context,scopedNativeElementIds=sorted(scoped_element_ids),submittedDirectIncidentRelationships=incident,proposedSubjectContainment=proposed_edges,unrelatedChangeInterpretation='NOT AUTHORISED / NOT PERFORMED',canonicalSourcesChanged=0,canonicalIdsAllocated=0)
verbose_bytes=len((json.dumps(payload,ensure_ascii=False,indent=2)+'\n').encode())
# Factor repeated policy text and redundant context, preserving every scoped
# observation. This changes serialization only, not scope, gates or meaning.
common_keys=['folderCanonicalId','folderIdentifier','folderIdentifierRef','viewReferences','identityFinding','proposedCanonicalName','canonicalNameReason','submittedNativeType','submittedSpecialization','proposedNativeType','proposedSpecialization','typeAuthority','navigationQualification','incorporation','canonicalIdAllocated']
assert all(all(r[k]==rows[0][k] for r in rows) for k in common_keys)
payload['uniformFolderAndProposalPolicy']={k:rows[0][k] for k in common_keys}
payload['folders']=[{k:v for k,v in r.items() if k not in common_keys} for r in rows]
payload['governedSubjectContext']=[{k:v for k,v in r.items() if k not in ('nativeType','specialization','standing')} for r in context]
payload['governedSubjectContextQualification']='All 82 are governed Grouping resources; their independent standing is preserved at the pinned canonical sources and existing governed-subjects.json. No state or meaning from other export changes is interpreted.'
encoded=json.dumps(payload,ensure_ascii=False,indent=2)+'\n'
write('payload-preflight.json',dict(verboseRepeatedContextBytes=verbose_bytes,scopedEvidenceBytes=len(encoded.encode()),limitBytes=128000,method='Factor identical per-row policy and redundant canonical context; preserve all scoped observations; same authorised scope; no limit waiver',gate='PASS' if len(encoded.encode())<=128000 else 'STOP'))
assert len(encoded.encode())<=128000,'Scoped payload exceeds limit: stop interpretation'
(OUT/'scoped-subject-evidence.json').write_text(encoded)
with (OUT/'subject-reconciliation.csv').open('w',newline='') as f:
    fields=['code','role','mappingName','folderLabel','sourceLine','sourceXPath','nameFinding','proposedDisplayName','proposedFriendlyName','proposedCanonicalName','proposedNativeType','proposedSpecialization','proposedParentCode','identityFinding','incorporation']
    writer=csv.DictWriter(f,fieldnames=fields);writer.writeheader()
    for row in rows:writer.writerow({k:row[k] for k in fields})
summary=dict(outcome='BOUNDED_INTERPRETATION_READY_FOR_REVIEW',scopedPayloadBytes=len(encoded.encode()),limitBytes=128000,viewFolderCount=len(rows),groups=5,subgroups=27,submittedScopedNativeElements=0,submittedScopedCanonicalIds=0,submittedIncidentSemanticRelationships=0,submittedViewReferences=0,existingGovernedSubjects=len(subjects),existingCodeOrScopedBindingMatches=0,exactGroupingLexicalLeads=0,proposedNativeGroupingSubjects=32,proposedSubgroupCompositionRelationships=27,spellingDiscrepancies=[dict(code=r['code'],submitted=r['folderLabel'],candidate=r['mappingName']) for r in rows if r['nameFinding'].startswith('TYPO')],nativeTypeOrSpecializationImport='None: input contains presentation folders only',identityAllocation='None; all prospective canonical IDs and relationship IDs unset',architecturalAcceptance='Not recorded',fullDeltaLimit='Still applicable; full delta not interpreted',canonicalValidation={k:v for k,v in validation.items() if isinstance(v,(str,int,float,bool)) or v is None},folderLabelInterpretation='32 candidate-code/ordinal/context correspondences, NOT canonical identity correlations')
write('summary.json',summary)
print(json.dumps({k:v for k,v in summary.items() if k!='canonicalValidation'},indent=2))
