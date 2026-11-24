#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independent bounded source/evidence/preservation checks; no acceptance."""
import collections
import csv
import hashlib
import json
from pathlib import Path
import re
import sys
ROOT=Path.cwd()
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
from architecture_review_evidence import check_links,check_iteration
from exchange_xml import parse,N,TYPE
from governed_naming import component

BASE=ROOT/'enterprise-architecture/design/evidence/1.1.18/001'
D=BASE/'after/bounded-subject-review'
RESULT=D/'verification.json'
RESULT.write_text('{"outcome":"PENDING"}\n')
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
def load(p):return json.loads(p.read_text())
payload=load(D/'scoped-subject-evidence.json')
assert 0<(D/'scoped-subject-evidence.json').stat().st_size<=128000
assert (D/'scoped-subject-evidence.json').stat().st_size==90051
rows=payload['folders'];policy=payload['uniformFolderAndProposalPolicy']
assert len(rows)==32 and len({r['code'] for r in rows})==32
assert policy['proposedNativeType']=='Grouping' and policy['proposedSpecialization'] is None
assert policy['proposedCanonicalName'] is None and policy['canonicalIdAllocated'] is None
assert policy['submittedNativeType'] is None and policy['folderCanonicalId'] is None
assert not payload['scopedNativeElementIds'] and not payload['submittedDirectIncidentRelationships']
incoming=ROOT/payload['inputs']['submission']['path']
xml=parse(incoming)
candidate_hierarchy=load(BASE/'after/reconciliation/candidate-subject-hierarchy.json')
mapping={r['code']:r for r in candidate_hierarchy}
assert set(mapping)=={r['code'] for r in rows}
_,canonical=c.load()
all_groupings=[m for m,b,p in canonical.values() if m['archimateType']=='Grouping']
subject_context={m['subjectGroup']['sourceId']:m for m,b,p in canonical.values() if 'subjectGroup' in m}
assert len(subject_context)==82
for row in rows:
    # XPath/line/labels and lack of native references are checked against secure
    # XML directly, rather than trusting the assessment's stated zero counters.
    nodes=xml.getroottree().xpath(row['sourceXPath'])
    assert len(nodes)==1
    node=nodes[0]
    assert node.tag=='{'+N['a']+'}item'
    assert node.sourceline==row['sourceLine'] and node.findtext('a:label',namespaces=N)==row['folderLabel']
    assert node.attrib=={} and not node.findall('.//a:item[@identifierRef]',N)
    assert {child.tag.rsplit('}',1)[-1] for child in node}<={'label','item'}
    assert [n.findtext('a:label',namespaces=N) for n in reversed(list(node.iterancestors())) if n.tag=='{'+N['a']+'}item']+ [row['folderLabel']]==row['folderPath']
    assert row['code'] not in subject_context
    assert row['mappingName']==mapping[row['code']]['name'] and row['mappingConcern']==mapping[row['code']]['concern']
    assert row['proposedFriendlyName']==component(row['mappingName'])
    assert all(component(m['displayName'])!=component(row['mappingName']) for m in all_groupings)
    assert not row['nativeElementReviewLeads'] and not row['existingScopedSourceBindings'] and not row['existingGroupingLexicalLeads']
    native_name_set={n.findtext('a:name',namespaces=N) for n in xml.findall('a:elements/a:element',N)}
    assert row['mappingName'] not in native_name_set and row['code'] not in native_name_set
for name,input_pin in payload['inputs'].items():
    p=ROOT/input_pin['path'];assert sha(p)==input_pin['sha256'] and p.stat().st_size==input_pin['bytes']
edges=payload['proposedSubjectContainment']
assert len(edges)==27 and len({(r['parentCode'],r['childCode']) for r in edges})==27
assert {e['childCode'] for e in edges}=={r['code'] for r in rows if '.' in r['code']}
for edge in edges:
    assert edge['parentCode']==edge['childCode'].split('.')[0]
    assert edge['nativeType']=='Composition' and edge['direction']=='parent to child'
    assert all(edge[k] is None for k in ('incomingRelationship','proposedRelationshipIdentifier','sourceCanonicalId','targetCanonicalId'))
assert all(row['code'].startswith(('G14','G15','G16','G17','G18')) for row in rows)
typos=[r for r in rows if r['nameFinding'].startswith('TYPO')]
assert len(typos)==1 and typos[0]['code']=='G18.03' and typos[0]['nameFinding']=='TYPO_INFORMATION_OMITS_I'
assert typos[0]['folderLabel']=='03. Directory-Informaton-Maintenance'
flat=list(csv.DictReader((D/'subject-reconciliation.csv').open(newline='')))
assert len(flat)==32 and {r['code'] for r in flat}==set(mapping)
for row,record in zip(rows,flat):
    assert record['mappingName']==row['mappingName'] and record['folderLabel']==row['folderLabel']
    assert record['proposedCanonicalName']=='' and record['proposedSpecialization']==''
assert len(payload['governedSubjectContext'])==82
for ctx in payload['governedSubjectContext']:
    actual=subject_context[ctx['code']]
    assert actual['canonicalId']==ctx['canonicalId'] and actual['displayName']==ctx['displayName'] and actual['exchange']['identifier']==ctx['exchangeId']

pins=load(BASE/'before/bounded-subject-review/preservation-pins.json')
checked=[];differences=[]
for pin in pins['files']:
    path=ROOT/pin['path']
    if path==BASE/'after/report-index.md':
        # The index is extended. Its original state is retained, not overwritten
        # without correspondence or misreported as unchanged.
        path=BASE/'before/bounded-subject-review/report-index.md'
    checked.append(pin['path'])
    if not path.is_file() or sha(path)!=pin['sha256']:differences.append(pin['path'])
assert not differences,differences
original_pins=load(BASE/'after/reconciliation/input-pins.json')
source_checked=[]
for pin in original_pins['files']:
    if pin['path'].startswith(('enterprise-architecture/design/evidence/1.1.18/001/after/','enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/')):continue
    p=ROOT/pin['path'];assert p.is_file() and sha(p)==pin['sha256'],pin['path'];source_checked.append(pin['path'])
initial_canonical={pin['path'] for pin in pins['files'] if pin['path'].startswith('enterprise-architecture/continuum/')}
assert initial_canonical=={str(p.relative_to(ROOT)) for p in (ROOT/'enterprise-architecture/continuum').rglob('*') if p.is_file()}
prior_candidates=load(BASE/'after/reconciliation/candidates.json')
assert len(prior_candidates)==784 and collections.Counter(r['specialization'] for r in prior_candidates)=={'UseCase':363,'UserStory':421}
assert all(r['proposedCanonicalId'] is None for r in prior_candidates)

crs=load(D/'change-requests.json')
assert [r['id'] for r in crs]==[f'CR-{i:03}' for i in range(1,61)]
assert all((r['disposition'],r['outcome'],r['instruction'],r['processing'])==('On-Hold','Unresolved','Not authorised','Not yet processed') for r in crs)
assert len([r for r in crs if r['kind']=='Subject instance proposal'])==32
assert len([r for r in crs if r['kind']=='Subject classification relationship proposal'])==27
assert len([r for r in crs if r['kind']=='View-navigation label proposal'])==1
code_cr={r['code']:r['id'] for r in crs if r['kind']=='Subject instance proposal'}
for cr in crs:
    assert len(cr['title'])<=80
    if 'edge' in cr:assert cr['dependencies']==[code_cr[cr['edge']['parentCode']],code_cr[cr['edge']['childCode']]]
review=ROOT/'enterprise-architecture/design/review/1.1.18/change-review.md'
text=review.read_text()
assert re.findall(r'^## (CR-\d+) —',text,re.M)==[r['id'] for r in crs]
assert re.findall(r'^\| \[(CR-\d+)\]',text,re.M)==[r['id'] for r in crs]
assert text.count('**Disposition:** On-Hold')==60 and text.count('**Outcome:** Unresolved')==60
assert text.count('**Authorised Reconciliation Instruction:** Not authorised.')==60
assert text.count('**Reconciliation / Processing Record:** Not yet processed.')==60
for cr in crs:assert f'| [{cr["id"]}](#{cr["id"].lower()}) | {cr["title"]} | On-Hold | Unresolved |' in text
hcrl=ROOT/'enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md'
ht=hcrl.read_text()
assert re.findall(r'^## DR-(\d+) —',ht,re.M)==[str(i) for i in range(1,11)]
assert 'Authorised — bounded interpretation only' in ht and 'CR-060' in ht
assert ht.count('Minutiae-Accepted:')==1
docs=[review,hcrl,BASE/'after/report-index.md',ROOT/'.junie/reports/2026-11-02-bounded-subject-reconciliation.md']
docs+=list((BASE/'after/ai-agent-reports').glob('*.md'))
link_errors=[]
for doc in docs:
    if not doc.is_file():link_errors.append('Missing document: '+str(doc.relative_to(ROOT)))
    else:link_errors.extend(check_links(doc))
provenance=check_iteration(ROOT,'1.1.18','001')
assert not provenance['errors']
result=dict(outcome='PASS' if not link_errors else 'FAIL',scope='Bounded interpretation and evidence only; no architectural acceptance',scopedEvidenceBytes=90051,limitBytes=128000,folderCrosswalk='PASS: 32 XML XPath/line/label/absence checks against original input',nativeScopedElementEndpoints=0,submittedIncidentSemanticRelationships=0,subjectConceptProposals=32,subjectClassificationEdgeProposals=27,viewSpellingProposals=1,canonicalNameAndIdentityAllocation='PASS: null; no formal token or UUID invented',governedSubjectIdentityContext='PASS: all 82 existing source-bound Subjects preserved',originalCandidateAssessment=dict(outcome='PASS',rows=784,useCases=363,userStories=421,preservedFilesChecked=len(checked),hashDifferences=differences,originalReportsAndProductionEvents='Unchanged'),originalSourcePreservation=dict(outcome='PASS',filesChecked=len(source_checked),canonicalPopulationUnchanged=True),changeReview=dict(outcome='PASS',independentCRs=60,allOnHoldUnresolved=True,summaryDetailAndEndpointDependencies='PASS'),canonicalAndInputValidation=load(D/'summary.json')['canonicalValidation'],documentLinks=dict(outcome='PASS' if not link_errors else 'FAIL',errors=link_errors),provenance=provenance,javaArchitectureSuite='UNRESOLVED: prior Maven dependency-cache write failure before tests; unchanged command not rerun',actualArchiTransport='NOT PERFORMED',incorporation='NOT AUTHORISED / NOT PERFORMED',capabilityRelationshipsCreated=0,canonicalIdsAllocated=0,canonicalSourcesChanged=0,fullExportDelta='Retained above 128 KB; not interpreted',otherIncomingChanges='UNADJUDICATED / NOT AUTHORISED FOR INCORPORATION',releaseTransition='NOT PERFORMED',commitPushAndHcrlClosure='NOT PERFORMED')
RESULT.write_text(json.dumps(result,ensure_ascii=False,indent=2)+'\n')
print(json.dumps({k:v for k,v in result.items() if k in ('outcome','scopedEvidenceBytes','subjectConceptProposals','subjectClassificationEdgeProposals','viewSpellingProposals','documentLinks','provenance')},indent=2))
assert not link_errors,link_errors
