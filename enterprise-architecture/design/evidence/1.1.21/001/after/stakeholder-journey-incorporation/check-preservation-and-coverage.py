# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
import json,sys,hashlib,subprocess,re,copy
from pathlib import Path
from collections import Counter,defaultdict
sys.path.insert(0,'enterprise-architecture/tooling')
import architecture_continuum as c
from lxml import etree as ET
root=Path.cwd();base=root/'enterprise-architecture/design/evidence/1.1.21/001/after/stakeholder-journey-incorporation'
pins=json.loads((base.parent.parent/'before/stakeholder-journey-incorporation/source-state.json').read_text())
plan=json.loads((base/'incorporation-plan.json').read_text());result=json.loads((base/'incorporation-results.json').read_text());alloc=json.loads((base/'allocated-identities.json').read_text())
_,elements=c.load();changed=[];unchanged=[]
for path,digest in pins['sha256'].items():
 p=root/path
 (unchanged if p.is_file() and c.sha(p.read_bytes())==digest else changed).append(path)
oldpaths=[p for p in pins['sha256'] if p.startswith(str(c.CONTINUUM)+'/') and p.endswith('.md') and not p.endswith('/MANIFEST.md')]
req=''.join(pins['gitHead']+':'+p+'\n' for p in oldpaths)
blob=subprocess.run(['git','cat-file','--batch'],input=req.encode(),stdout=subprocess.PIPE,check=True).stdout
cursor=0;old={}
for path in oldpaths:
 end=blob.index(b'\n',cursor);head=blob[cursor:end].split();size=int(head[-1]);cursor=end+1;data=blob[cursor:cursor+size];cursor+=size+1
 m,b=c.decode(data.decode());old[m['canonicalId']]=(m,b,path)
assert len(old)==5685 and set(old)<=set(elements)
oldrels={};preserved_fields=[];appended_bodies=[]
for cid,(prior,body,path) in old.items():
 m,b,p=elements[cid]
 assert str(p.relative_to(root))==path
 for key in ['canonicalId','archimateType','displayName','friendlyName','canonicalName','sourceBindings','exchange','naming','unresolvedProperties']:
  assert prior[key]==m[key],(cid,key)
 for key in ['standing','identity']:
  for field,value in prior[key].items():assert m[key][field]==value,(cid,key,field)
 assert all(row in m['provenance'] for row in prior['provenance'])
 assert b.startswith(body),(cid,'body')
 if b!=body:appended_bodies.append(cid)
 if prior.get('specialization')!=m.get('specialization'):assert cid in result['reusedJourneys'] and m['specialization']=='Journey'
 for row in prior['relationships']:oldrels[row['identifier']]=row;assert row in m['relationships'],(cid,row['identifier'])
allrels={r['identifier']:r for m,b,p in elements.values() for r in m['relationships']}
newrels={rid:r for rid,r in allrels.items() if rid not in oldrels}
assert len(newrels)==2628 and len(oldrels)==6636
publications=[p for p in pins['sha256'] if '/published/' in p or '/register/' in p]
assert all(p in unchanged for p in publications)
assert 'architecture-release.json' in unchanged
cap_ids={cid for cid,(m,b,p) in old.items() if m['archimateType']=='Capability'}
assert cap_ids=={cid for cid,(m,b,p) in elements.items() if m['archimateType']=='Capability'}
assert all(elements[cid][0]['specialization']==old[cid][0]['specialization'] for cid in cap_ids)
rep={m['exchange']['identifier']:cid for cid,(m,b,p) in elements.items()}
assert not any(r['archimateType'] in ('Composition','Aggregation') and (rep[r['source']] in cap_ids or rep[r['target']] in cap_ids) for r in newrels.values())
oldxml=ET.fromstring(subprocess.run(['git','show',pins['gitHead']+':enterprise-architecture/design/archimate/projection-template.xml'],stdout=subprocess.PIPE,check=True).stdout)
newxml=c.secure_xml(root/c.DESIGN/'projection-template.xml')
views=lambda t:{v.get('identifier'):ET.tostring(v) for v in t.findall('.//'+c.tag('views')+'/'+c.tag('diagrams')+'/'+c.tag('view'))}
ov,nv=views(oldxml),views(newxml);assert len(ov)==1288 and len(nv)==1304;assert all(nv[k]==v for k,v in ov.items())
# Verify original source bytes by removing exactly the bounded wrappers only.
succ=json.loads((base/'source-section-succession.json').read_text());sources=sorted({x['source'] for x in succ['correspondence']})
for source in sources:
 current=(root/source).read_text()
 restored=re.sub(r'<!-- harmonia-1.1.21-section-supersession:start -->\n.*?---------------------------------------------------------\n\n','',current,flags=re.S)
 restored=restored.replace('\n------- Legacy Content - Superseded ------- Finish ----\n<!-- harmonia-1.1.21-section-supersession:end -->\n\n','')
 original=subprocess.run(['git','show',pins['gitHead']+':'+source],stdout=subprocess.PIPE,check=True).stdout
 assert restored.encode()==original,source
reconciliation=json.loads((base/'requirement-reconciliation.json').read_text());assert len(reconciliation)==784
for row in reconciliation:assert row['canonicalId'] in elements and elements[row['canonicalId']][0]['specialization']==row['kind']
journeys={cid for cid,(m,b,p) in elements.items() if m.get('specialization')=='Journey'}
uc={cid for cid,(m,b,p) in elements.items() if m.get('specialization')=='UseCase'};us={cid for cid,(m,b,p) in elements.items() if m.get('specialization')=='UserStory'}
adj=defaultdict(set);struct=defaultdict(set);parents=defaultdict(set);ucstories=defaultdict(set);storyuc=defaultdict(set);links=Counter();parts=[]
for rid,r in allrels.items():
 s,t=rep[r['source']],rep[r['target']];adj[s].add(t);adj[t].add(s)
 if rid in newrels:links[(elements[s][0].get('specialization') or elements[s][0]['archimateType'],r['archimateType'],elements[t][0].get('specialization') or elements[t][0]['archimateType'])]+=1
 if s in journeys and t in journeys and r['archimateType'] in ('Composition','Aggregation'):struct[s].add(t);parents[t].add(s)
 if s in uc and t in us:ucstories[s].add(t);storyuc[t].add(s)
 if 'participationCharacteristics' in r:parts.append({'stakeholder':elements[s][0].get('stakeholderPerspective',elements[s][0]['displayName']),'journey':elements[t][0]['displayName'],'characteristics':r['participationCharacteristics']})
def depth(cid):return 1+max((depth(ch) for ch in struct[cid]),default=0)
coverage={'journeyCount':len(journeys),'useCases':len(uc),'userStories':len(us),'structuralRootJourneys':len(journeys-set(parents)),'maximumRepresentedJourneyLevels':max(map(depth,journeys)),'sharedConstituents':[{'canonicalId':cid,'name':elements[cid][0]['displayName'],'parents':sorted(ps)} for cid,ps in parents.items() if len(ps)>1],'useCasesWithoutJourney':sorted(cid for cid in uc if not adj[cid]&journeys),'userStoriesWithoutJourney':sorted(cid for cid in us if not adj[cid]&journeys),'useCasesContributingToMultipleStories':len([s for s,ts in ucstories.items() if len(ts)>1]),'storiesWithMultipleUseCases':len([s for s,ts in storyuc.items() if len(ts)>1]),'userStoriesWithoutUseCases':len(us-set(storyuc)),'participation':parts,'newRelationshipCoverage':[{'source':s,'relationship':r,'target':t,'count':n} for (s,r,t),n in sorted(links.items())],'interpretation':'Optional relationships, partial coverage and evidence-based omissions supply no completeness, satisfaction, implementation or acceptance claim.'}
preservation={'status':'PASS','baselineGitHead':pins['gitHead'],'priorElementsPreserved':len(old),'priorRelationshipsPreservedExactly':len(oldrels),'nativeTypesNamesBindingsNamingStandingAndOriginalProvenancePreserved':True,'originalBodiesPreserved':len(old),'bodiesWithQualifiedAppendices':appended_bodies,'reusedValueStreamsClassifiedAsJourney':result['reusedJourneys'],'priorViewsPreservedExactly':len(ov),'newViews':len(nv)-len(ov),'acceptedPublicationAndRegisterFilesUnchanged':len(publications),'releaseAuthorityUnchanged':True,'capabilityIdentitiesAndStructuralHierarchyUnchanged':len(cap_ids),'originalSourceBytesPreservedBehindBoundedWrappers':len(sources),'supersededSections':succ['sections'],'pinnedFilesUnchanged':len(unchanged),'changedPinnedPaths':changed}
(base/'preservation-validation.json').write_bytes(c.encoded(preservation));(base/'semantic-coverage.json').write_bytes(c.encoded(coverage))
# Readable catalogue and exact review targets; no additional architectural assertions.
lines=['<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->','','# Stakeholder and Journey incorporation catalogue','','Candidate definitions and contextual relationships await incorporated-result review. Existing standing and unresolved source/naming qualifications remain.','','## Stakeholder perspectives','','| Perspective | Canonical identity | Contextual Journey assertions |','| --- | --- | --- |']
for name,cid in result['stakeholderCatalogue'].items():
 m,b,p=elements[cid];count=sum(x['stakeholder']==name for x in parts);lines.append(f'| {name} | [{cid}]({__import__("os").path.relpath(p,base)}) | {count} |')
lines+=['','Participation is evidence-based. Zero participation assertions does not remove a catalogue perspective or justify inventing participation. Ordinary software interactions do not qualify an Autonomous System by themselves.','','## Journeys','','| Journey | Canonical identity | Intended outcome | Constituents |','| --- | --- | --- | --- |']
for j in plan['journeys']:
 cid=j.get('existingCanonicalId') or alloc['journey/'+j['key']]['canonicalId'];m,b,p=elements[cid]
 lines.append('| '+j['name'].replace('|','\\|')+' | ['+cid+']('+__import__('os').path.relpath(p,base)+')'+(' (reused)' if j.get('existingCanonicalId') else '')+' | '+j['intendedOutcome'].replace('|','\\|')+' | '+', '.join(j.get('children',[]))+' |')
lines+=['','Definitions retain conditional, alternative, repeated, concurrent and asynchronous progression as applicable. Aggregation expresses independently reusable structure; Triggering expresses progression/dependencies. Neither defines an executable workflow.','','## Authored candidate Views','','Import [the exact candidate](candidate/Harmonia-Architecture.xml); inspect the folder **Journeys and stakeholder perspectives**. The native save in `archi-transport/native-model.archimate.gz` preserves the actual imported workbench. The [observed native tree inventory](archi-transport/imported-tree.html) supports inspection. Human visual/tree review is not claimed.','','| View | Presentation identifier |','| --- | --- |']
for v in json.loads((base/'authored-views.json').read_text())['newViews']:lines.append('| '+v['name']+' | `'+v['identifier']+'` |')
lines+=['','Inspect representative progression/reuse, the independent requirements View, requirement SolutionFacet context, stakeholder participation properties, NFR directed constraint Associations, and preserved Capability/Business/Application boundaries. Use the [reader](candidate/continuum.html) and [narrative structure](narrative/structure.json) for the separate reader structural checkpoint.','','The existing Application DataObject Journey (`359b901df27f4b43b5a2ff14a8484e34`) remains observed runtime progression under Energeia; these reusable strategic definitions do not replace it.']
(base/'catalogue-and-review-guide.md').write_text('\n'.join(lines)+'\n')
print(json.dumps({k:v for k,v in preservation.items() if k not in ('changedPinnedPaths','bodiesWithQualifiedAppendices')},indent=2));print(json.dumps({k:v for k,v in coverage.items() if not isinstance(v,list)},indent=2));print('new coverage',coverage['newRelationshipCoverage'])
