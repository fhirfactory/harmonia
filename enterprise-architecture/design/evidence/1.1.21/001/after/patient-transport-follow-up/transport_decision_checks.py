# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only interpretation of the existing exact transport; raw findings unchanged."""
import sys,json,gzip,collections,hashlib
from pathlib import Path
sys.path.insert(0,'enterprise-architecture/tooling');import architecture_continuum as c,exchange_compare as ec
old=Path('enterprise-architecture/design/evidence/1.1.21/001/after/stakeholder-journey-incorporation');out=old.parent/'patient-transport-follow-up';before=old.parent.parent/'before/patient-transport-follow-up'
corrected=len(sys.argv)>1 and sys.argv[1]=='corrected'
if corrected:old=out
inputs=[old/'candidate/Harmonia-Architecture.xml',old/'archi-transport/returned-model.xml.gz',old/'archi-transport/raw-comparison.json.gz',old/'archi-transport/native-model.archimate.gz',old/('corrected-transport-operation.json' if corrected else 'transport-assurance.json'),c.ROOT/c.TOOLING/'exchange_compare.py',c.ROOT/c.TOOLING/'verify_continuum_transport.py']
pins={str(p):c.sha(p.read_bytes()) for p in inputs};(before/('corrected-transport-input-pins.json' if corrected else 'transport-input-pins.json')).write_bytes(c.encoded(pins))
a=ec.Exchange(inputs[0]);b=ec.Exchange(inputs[1]);raw=json.loads(gzip.decompress(inputs[2].read_bytes()));assert raw['published_sha256']==a.xml_sha256 and raw['incoming_sha256']==b.xml_sha256
# Property claims remain keyed by their actual definition names; value ordering,
# multiplicity, text and all non-language attributes are checked, not discarded.
value_count=0;languages=collections.Counter();owner_counts=collections.Counter()
def properties(model,node):
 return [(model.keys[p.get('propertyDefinitionRef')],[(v.text or '',dict(v.attrib)) for v in p.findall('a:value',c.N)]) for p in node.findall('a:properties/a:property',c.N)]
for kind,ta,tb in [('element',a.elements,b.elements),('relationship',a.relationships,b.relationships),('view',a.views,b.views),('model',{'model':a.root},{'model':b.root})]:
 assert set(ta)==set(tb)
 for ident,node in ta.items():
  x,y=properties(a,node),properties(b,tb[ident]);dx={k:v for k,v in x};dy={k:v for k,v in y};assert len(dx)==len(x) and len(dy)==len(y);assert set(dx)==set(dy),(kind,ident,'property key')
  owner_counts[kind]+=1
  for key,vals in dx.items():
   assert len(vals)==len(dy[key]),(kind,ident,key,'multiplicity')
   for (tx,ax),(ty,ay) in zip(vals,dy[key]):
    value_count+=1;assert tx==ty,(kind,ident,key,'text');assert {k:v for k,v in ax.items() if k!=c.LANG}=={k:v for k,v in ay.items() if k!=c.LANG}
    if ax.get(c.LANG)!=ay.get(c.LANG):languages[(ax.get(c.LANG),ay.get(c.LANG))]+=1
# Everything semantic outside property-ID/order/language differences is exact.
for kind,ta,tb in [('element',a.elements,b.elements),('relationship',a.relationships,b.relationships)]:
 for ident,x in ta.items():
  y=tb[ident];assert dict(x.attrib)==dict(y.attrib),(kind,ident,'attributes')
  for tag in ['name','documentation']:
   vx,vy=ec.values(x,tag),ec.values(y,tag);assert len(vx)==len(vy)
   for xx,yy in zip(vx,vy):
    assert xx['text']==yy['text'];ax,ay=xx['attributes'],yy['attributes'];assert {k:v for k,v in ax.items() if k!=c.LANG}=={k:v for k,v in ay.items() if k!=c.LANG}
    if ax.get(c.LANG)!=ay.get(c.LANG):languages[(ax.get(c.LANG),ay.get(c.LANG))]+=1
org_a=a.root.findall('a:organizations//a:item',c.N);org_b=b.root.findall('a:organizations//a:item',c.N);assert len(org_a)==len(org_b)
folder_count=0;lost_folder_ids=0;lost_labels=collections.Counter();folder_docs=0;recoverable=0
for x,y in zip(org_a,org_b):
 assert x.get('identifierRef')==y.get('identifierRef'),'Organisation member/order changed'
 assert {k:v for k,v in x.attrib.items() if k!='identifier'}=={k:v for k,v in y.attrib.items() if k!='identifier'}
 if x.get('identifier')!=y.get('identifier'):assert x.get('identifier') and y.get('identifier') is None;lost_folder_ids+=1
 if x.get('identifierRef'):
  ref=x.get('identifierRef');lx=ec.values(x,'label');ly=ec.values(y,'label')
  if lx!=ly:
   assert len(lx)==1 and not ly
   if ref in a.elements:expected=a.elements[ref].findtext('a:name',namespaces=c.N);kind='element'
   elif ref in a.relationships:expected=a.relationships[ref].findtext('a:name',namespaces=c.N) or ref;kind='relationship'
   else:raise AssertionError('Unexpected lost View organisation label')
   assert lx[0]['text']==expected;lost_labels[kind]+=1;recoverable+=1
 else:
  folder_count+=1;assert ec.values(x,'label')==ec.values(y,'label');assert ec.values(x,'documentation')==ec.values(y,'documentation');folder_docs+=len(ec.values(x,'documentation'))
 assert properties(a,x)==properties(b,y)
# Preserve every graphical object attribute, reference, coordinate/bendpoint and
# pre-existing style. Only record additions; do not assert visual equivalence.
style_additions=0;changed_connection_orders=[];styles=[];nodes=0;connections=0
for vid,v in a.views.items():
 returned=b.views[vid]
 for tag in ['name','documentation']:assert ec.values(v,tag)==ec.values(returned,tag)
 for kind in ['node','connection']:
  aa={n.get('identifier'):n for n in v.findall('.//a:'+kind,c.N)};bb={n.get('identifier'):n for n in returned.findall('.//a:'+kind,c.N)};assert set(aa)==set(bb)
  for ident,x in aa.items():
   y=bb[ident];assert dict(x.attrib)==dict(y.attrib),(vid,ident,'visual attributes')
   assert [n.get('identifier') for n in x.findall('a:node',c.N)]==[n.get('identifier') for n in y.findall('a:node',c.N)]
   assert [dict(n.attrib) for n in x.findall('a:bendpoint',c.N)]==[dict(n.attrib) for n in y.findall('a:bendpoint',c.N)]
   for tag in ['label','documentation']:assert ec.values(x,tag)==ec.values(y,tag)
   sx=x.find('a:style',c.N);sy=y.find('a:style',c.N)
   if sx is None and sy is not None:
    assert kind=='connection';style_additions+=1;styles.append(ec.g.ET.tostring(sy).decode().split('>',1)[1])
   elif sx is not None:
    assert sy is not None
    assert [(n.tag,dict(n.attrib),(n.text or '').strip()) for n in sx.iter()]==[(n.tag,dict(n.attrib),(n.text or '').strip()) for n in sy.iter()],(vid,ident,'existing style')
  if kind=='node':nodes+=len(aa)
  else:connections+=len(aa)
 co=[n.get('identifier') for n in v.findall('a:connection',c.N)];cr=[n.get('identifier') for n in returned.findall('a:connection',c.N)]
 if co!=cr:assert set(co)==set(cr);changed_connection_orders.append(vid)
 assert [n.get('identifier') for n in v.findall('a:node',c.N)]==[n.get('identifier') for n in returned.findall('a:node',c.N)]
families=collections.Counter()
for row in raw['findings']:
 p=row['path'];scope='organisation' if '/organizations[' in p else 'relationships' if '/relationships[' in p else 'views' if '/views[' in p else 'elements' if '/elements[' in p else 'other'
 families[(scope,row['classification'],row['reason'])]+=1
assert all(row.get('before') is None and row.get('after')=='en' and row['path'].endswith('/@'+c.LANG) for row in raw['findings'] if row['classification']=='SEMANTIC DIFFERENCE')
assert sum(languages.values())==raw['classifications']['SEMANTIC DIFFERENCE']
assert pins=={str(p):c.sha(p.read_bytes()) for p in inputs}
report={'scope':'Exact '+('corrected' if corrected else 'original')+' 1.1.21 transport only; diagnostic interpretation does not modify comparator, raw evidence, classifications, canonical model or template.','baselineXmlSha256':a.xml_sha256,'returnedXmlSha256':b.xml_sha256,'rawClassificationsUnchanged':raw['classifications'],'architecturalTextAndStructureChecks':{'ownerPopulations':dict(owner_counts),'allElementAndRelationshipAttributesIncludingEndpointsIdentical':True,'allElementRelationshipNameAndDocumentationTextsPreservedWithOnlyLanguageAnnotationChanges':True,'propertyValueCountWithExactTextMultiplicityAndOwner':value_count,'languageAttributeChanges':[{'before':x,'after':y,'count':n} for (x,y),n in languages.items()]},'organisation':{'memberReferencesAndTheirOrderedPlacementIdentical':True,'foldersWithUnchangedNamesAndDocumentation':folder_count,'folderDocumentationRecordsPreserved':folder_docs,'folderIdentifiersLost':lost_folder_ids,'leafLabelsLost':dict(lost_labels),'allLostLeafLabelTextsRecoverableExactlyFromReferencedRecords':recoverable,'meaning':'Real loss of representation IDs and explicit labels. These are generated locators/duplicate labels, not canonical element IDs or architectural definitions. Regeneration remains possible; the returned Exchange is not byte- or metadata-faithful.'},'presentation':{'visualNodesWithIdenticalAttributesAndReferences':nodes,'connectionsWithIdenticalAttributesAndBendpoints':connections,'preExistingStylesIdentical':True,'explicitConnectionStylesAdded':style_additions,'viewsWithReorderedConnections':changed_connection_orders,'viewNodeOrderUnchanged':True,'meaning':'Default-looking black line/Sans 9 styling was made explicit. Connection stacking order may affect overlaps; no pixel equivalence or invariance across Archi preferences is established.'},'findingFamilyAccounting':[{'scope':s,'rawClassification':cl,'reason':reason,'count':n} for (s,cl,reason),n in sorted(families.items())],'demonstratedArchitecturalContentLoss':'None found in checked identities, native types, endpoints, names, documentation, owner/key/value/multiplicity, geometry or references. This is a bounded conclusion, not whole-byte fidelity.','semanticQualification':str(sum(languages.values()))+' absent-to-en language annotations change language metadata, even though all associated texts and owners survive. Treating them as English for this exact dataset is reasonable with an explicit qualification; no general multilingual equivalence is established.','remainingUncertainty':['View connection paint/stacking order and newly explicit styles may affect appearance; numeric geometry and labels are preserved but pixel equivalence has not been demonstrated.','Returned organisation locators and explicit labels cannot serve as a lossless editable source; canonical Markdown and the authored template remain authoritative.'],'recommendation':'Reasonably accept this exact retained Archi operation with a bounded transport qualification, preserving strict FAIL and the exact native/returned evidence. Do not apply returned XML or treat this as approval of comparator changes or future runs. '+('This corrected run establishes correspondence to the corrected candidate pinned above.' if corrected else 'This original run does not establish corrected-candidate correspondence; see the separately pinned corrected analysis.')}
(out/('corrected-transport-decision-analysis.json' if corrected else 'transport-decision-analysis.json')).write_bytes(c.encoded(report));print(json.dumps(report,indent=2))
