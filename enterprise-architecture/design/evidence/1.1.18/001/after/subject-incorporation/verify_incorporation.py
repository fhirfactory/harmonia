#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independently prove the accepted 32/27 delta and preservation boundaries."""
import copy
import csv
import hashlib
import json
from pathlib import Path
import re
import sys
import uuid
ROOT=Path.cwd()
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
from architecture_review_evidence import check_links,check_iteration
from exchange_compare import Exchange
from exchange_signatures import signature
from exchange_xml import N,TYPE,signature as xml_signature,parse
from governed_naming import component

BASE=ROOT/'enterprise-architecture/design/evidence/1.1.18/001'
D=BASE/'after/subject-incorporation';BEFORE=BASE/'before/subject-incorporation'
ledger=json.loads((D/'incorporation-results.json').read_text())
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
def read(p):return json.loads(p.read_text())
_,elements=c.load()
subjects=ledger['subjects'];edges=ledger['relationships']
assert len(subjects)==32 and len(edges)==27 and len(elements)==1874
newids={r['canonicalId'] for r in subjects};newex={r['exchangeId'] for r in subjects}
assert len(newids)==32 and all(uuid.UUID(hex=x).version==4 for x in newids)
bycode={m['subjectGroup']['sourceId']:(m,b,p) for m,b,p in elements.values() if 'subjectGroup' in m}
assert len(bycode)==114
actual_pairs=set()
for row in subjects:
    m,body,path=bycode[row['code']]
    assert m['canonicalId']==row['canonicalId'] and m['exchange']['identifier']==row['exchangeId']==c.semantic_id(m['canonicalId'])
    assert str(path.relative_to(ROOT))==row['source']
    assert m['archimateType']=='Grouping' and m['specialization'] is None and m['canonicalName'] is None
    assert 'canonicalName' in m['unresolvedProperties'] and m['friendlyName']==component(m['displayName'])
    assert m['standing']['currentness']=='current' and 'Solution Architect accepted '+row['cr'] in m['standing']['semantic']
    assert 'naming ancestry' in body and 'Motivation allocation' in body
    assert len(m['relationships'])==(6 if row['code'] in ('G14','G16') else 5 if row['code'] in ('G15','G17','G18') else 0)
    assert (ROOT/row['source']).is_file()
    for edge in m['relationships']:
        assert edge['archimateType']=='Composition' and 'canonicalId' not in edge
        assert edge['source']==m['exchange']['identifier']
        assert edge['standing']['currentness']=='current'
        assert edge['target'] in newex
        child=next(r['code'] for r in subjects if r['exchangeId']==edge['target'])
        assert child.split('.')[0]==row['code']
        actual_pairs.add((row['code'],child))
assert actual_pairs=={(e['parentCode'],e['childCode']) for e in edges}
assert {e['childCode'] for e in edges}=={r['code'] for r in subjects if '.' in r['code']}
assert sum(len(m['relationships']) for m,b,p in elements.values())==1909

pins=read(BEFORE/'preimages.json')['files']
allowed={str((c.CONTINUUM/'navigation.json')),str(c.MANIFEST),str(c.DESIGN/'projection-template.xml'),'enterprise-architecture/design/review/1.1.18/change-review.md','enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md','enterprise-architecture/design/evidence/1.1.18/001/after/report-index.md'}
preserved=[];changed_allowed=[]
for pin in pins:
    p=ROOT/pin['path'];assert p.is_file(),pin['path']
    if pin['path'] in allowed:
        changed_allowed.append(pin['path'])
        if (BEFORE/p.name).exists():assert sha(BEFORE/p.name)==pin['sha256']
    else:
        assert sha(p)==pin['sha256'],'Unexpected mutation: '+pin['path'];preserved.append(pin['path'])
oldsource={x['path'] for x in pins if x['path'].startswith(str(c.CONTINUUM)+'/') and x['path'].endswith('.md') and x['path']!=str(c.MANIFEST)}
assert len(oldsource)==1842
assert {str(p.relative_to(ROOT)) for m,b,p in elements.values()}-oldsource=={r['source'] for r in subjects}
assert all(sha(ROOT/pin['path'])==pin['sha256'] for pin in pins if pin['path'] in oldsource)
candidate=read(BASE/'after/reconciliation/candidates.json')
assert len(candidate)==784 and all(r['proposedCanonicalId'] is None for r in candidate)

oldnav=read(BEFORE/'navigation.json');newnav=read(ROOT/c.CONTINUUM/'navigation.json')
def prune(nodes):
    result=[]
    for node in nodes:
        if node.get('canonicalId') in newids:continue
        node=copy.deepcopy(node)
        if 'children' in node:node['children']=prune(node['children'])
        result.append(node)
    return result
pruned=copy.deepcopy(newnav);pruned['roots']=prune(pruned['roots'])
assert pruned==oldnav,'Unrelated canonical navigation change'
template_before=parse(BEFORE/'projection-template.xml')
template_after=parse(ROOT/c.DESIGN/'projection-template.xml')
def view_root(root):
    hits=[n for n in root.findall('a:organizations/a:item',N) if n.findtext('a:label',namespaces=N)=='Views']
    assert len(hits)==1;return hits[0]
new_views=view_root(template_after)
branch=[n for n in new_views.findall('a:item',N) if n.findtext('a:label',namespaces=N)=='1. Subject-Based-Views']
assert len(branch)==1
assert len(branch[0].findall('a:item',N))==1
group=branch[0].find('a:item',N);assert group.findtext('a:label',namespaces=N)=='18. Provider Directory'
assert len(group.findall('a:item',N))==1
leaf=group.find('a:item',N);assert leaf.findtext('a:label',namespaces=N)=='03. Directory-Information-Maintenance'
assert all(not n.attrib for n in branch[0].iter(c.tag('item')))
assert not branch[0].findall('.//a:item[@identifierRef]',N)
new_views.remove(branch[0])
assert xml_signature(template_after)==xml_signature(template_before),'Template mutation outside necessary CR-060 path'

baseline=Exchange(ROOT/c.ARCHIMATE_PUBLICATION/'1.1.18/Harmonia-Architecture.xml')
result=Exchange(D/'Harmonia-Subjects-unreleased.xml')
assert not baseline.issues and not result.issues
assert set(result.elements)-set(baseline.elements)==newex and not set(baseline.elements)-set(result.elements)
newrels={r['identifier'] for r in edges}
assert set(result.relationships)-set(baseline.relationships)==newrels and not set(baseline.relationships)-set(result.relationships)
assert set(result.views)==set(baseline.views) and len(result.views)==1216
for name in ('elements','relationships','views'):
    original=getattr(baseline,name);current=getattr(result,name)
    for key in original:
        assert signature(original[key],baseline)==signature(current[key],result),name+' changed: '+key
for row in subjects:
    node=result.elements[row['exchangeId']]
    assert node.get(TYPE)=='Grouping' and result.single(node,'canonicalId')==row['canonicalId']
    assert node.findtext('a:name',namespaces=N)==row['displayName']
    assert not result.texts(node,'Specialization')
    placements=result.root.findall('a:organizations//a:item[@identifierRef="'+row['exchangeId']+'"]',N)
    assert len(placements)==1 and placements[0].getparent().findtext('a:label',namespaces=N)=='Grouping'
    assert placements[0].getparent().getparent().findtext('a:label',namespaces=N)=='Catalogue'
for row in edges:
    node=result.relationships[row['identifier']]
    assert node.get(TYPE)=='Composition' and node.get('source')==row['source'] and node.get('target')==row['target']
    assert result.single(node,'v0.2 / Record State')=='current'
    assert result.single(node,'v0.2 / Assertion Status')=='Accepted'
    placements=result.root.findall('a:organizations//a:item[@identifierRef="'+row['identifier']+'"]',N)
    assert len(placements)==1
    assert row['sourceCanonicalId'] in placements[0].getparent().findtext('a:label',namespaces=N)
views_after=copy.deepcopy(view_root(result.root));views_old=view_root(baseline.root)
added=[n for n in views_after.findall('a:item',N) if n.findtext('a:label',namespaces=N)=='1. Subject-Based-Views']
assert len(added)==1;views_after.remove(added[0])
assert xml_signature(views_after)==xml_signature(views_old)

# Reconciliation manifests expose the real allocated identities, never substitute
# source classification codes for UUIDs or manufacture a relationship canonicalId.
for filename,records in [('incorporated-subjects.csv',subjects),('incorporated-classification-relationships.csv',edges)]:
    keys=list(records[0]);flat=[]
    for row in records:flat.append({k:json.dumps(v,ensure_ascii=False) if isinstance(v,(dict,list)) else v for k,v in row.items()})
    with (D/filename).open('w',newline='') as stream:
        writer=csv.DictWriter(stream,fieldnames=keys);writer.writeheader();writer.writerows(flat)
schema=read(D/'projection-validation.json')
assert schema['status']=='PASS' and schema['outputSha256']==sha(D/'Harmonia-Subjects-unreleased.xml')
tests=read(D/'final-test-results.json') if (D/'final-test-results.json').exists() else dict(outcome='PENDING')
supplemental=read(D/'supplemental-test-results.json') if (D/'supplemental-test-results.json').exists() else dict(outcome='PENDING')
report=dict(outcome='PASS',canonicalElements=1874,canonicalRelationships=1909,subjectsIncorporated=32,classificationRelationshipsIncorporated=27,canonicalIdentitiesAllocatedThroughGovernedCreate=32,relationshipRepresentationIdentifiersAllocated=27,existingCanonicalElementFilesBytePreserved=1842,existingSubjectsPreserved=82,originalAssessmentRowsUnchanged=784,oldProjectedElementsAndRelationships='PASS: every old semantic signature unchanged',views=dict(existingViewCount=1216,allViewContentsUnchanged=True,allExistingViewNavigationUnchanged=True,necessaryCR060EmptyPathOnly=True,emptyPresentationItemsAdded=3,unrelatedIncomingViewsAdopted=0),nativeCataloguePlacement='PASS: one native Grouping entry per Subject; one direct-source entry per relationship',canonicalNavigation='PASS: prune accepted new identities and original document is identical',naming='PASS: native Grouping, null specialisation, local friendlyName, canonicalName null with explicit unresolved reason',protectedFilesChecked=len(preserved),allowedExistingFilesChanged=changed_allowed,unreleasedProjection=dict(path=str((D/'Harmonia-Subjects-unreleased.xml').relative_to(ROOT)),sha256=sha(D/'Harmonia-Subjects-unreleased.xml'),publicationAllocatedIdentities=0),validation=schema,initialStandardToolingTests=dict(outcome='FAIL',executedTests=56,fixtureErrors=6,basis='Temporary source fixtures omitted declared retained inputs; original log preserved'),supplementalToolingTests=supplemental,finalStandardToolingTests=tests,javaArchitectureSuite='UNRESOLVED: earlier Maven read-only dependency-cache error before test execution; unchanged command not rerun',taskEventProvenance='UNRESOLVED: original instruction-entry event times unavailable; no substitute timestamps',actualArchiTransportAndSolutionArchitectInspection='NOT PERFORMED / REQUIRED BEFORE PUBLICATION ACCEPTANCE',release=dict(current=read(ROOT/'architecture-release.json'),transition='NOT AUTHORISED / NOT PERFORMED',publishedBaseline='Unchanged and intentionally not overwritten; current working result is unreleased'),commitPushAndHcrlClosure='NOT AUTHORISED / NOT PERFORMED')
if '--final' in sys.argv:
    current=ROOT/'enterprise-architecture/design/review/1.1.18'
    review=(current/'change-review.md').read_text()
    assert review.count('**Disposition:** Accept')==60 and review.count('**Outcome:** Unresolved')==60
    assert len(re.findall(r'^\| \[CR-\d+\].*?\| Accept \| Unresolved \|$',review,re.M))==60
    original_hcrl=(BEFORE/'hcrl-1.1.18-001.pending.md').read_text();hcrl=(current/'hcrl-1.1.18-001.pending.md').read_text()
    for number in range(3,10):
        pattern=rf'<a id="dr-{number}"></a>.*?(?=<a id="dr-{number+1}"></a>)'
        assert re.search(pattern,original_hcrl,re.S).group()==re.search(pattern,hcrl,re.S).group(),'Excluded DR text changed: '+str(number)
    docs=[current/'change-review.md',current/'hcrl-1.1.18-001.pending.md',BASE/'after/report-index.md',ROOT/'.junie/reports/2026-11-02-accepted-subject-incorporation.md']+list((BASE/'after/ai-agent-reports').glob('*.md'))
    (D/'verification.json').write_text('{"outcome":"PENDING_DOCUMENTATION_CHECK"}\n')
    problems=[]
    for doc in docs:
        assert doc.is_file(),doc;problems+=check_links(doc)
    report['documentLinks']=dict(outcome='FAIL' if problems else 'PASS',errors=problems)
    report['provenance']=check_iteration(ROOT,'1.1.18','001')
    assert not problems and not report['provenance']['errors']
    assert tests['outcome']=='PASS'
(D/'verification.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n')
print(json.dumps({k:v for k,v in report.items() if k in ('outcome','canonicalElements','canonicalRelationships','subjectsIncorporated','classificationRelationshipsIncorporated','existingCanonicalElementFilesBytePreserved','originalAssessmentRowsUnchanged','views','finalStandardToolingTests','documentLinks')},indent=2))
