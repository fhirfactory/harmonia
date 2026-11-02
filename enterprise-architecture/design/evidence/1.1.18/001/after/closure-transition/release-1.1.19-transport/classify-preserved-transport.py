# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
# Purpose-selected evidence check over retained exact inputs; no production validator changes.
import sys,json,gzip,re,copy
from collections import Counter
from pathlib import Path
sys.path.insert(0,'enterprise-architecture/tooling')
import architecture_continuum as c,exchange_compare as d
import continuum_workbench as w
from exchange_signatures import signature
p=Path('enterprise-architecture/design/evidence/1.1.18/001/after/closure-transition/release-1.1.19-transport')
raw=json.loads(gzip.decompress((p/'archi-raw-comparison.json.gz').read_bytes()))
LANG='{http://www.w3.org/XML/1998/namespace}lang'
classes=Counter();unclassified=[]
for f in raw['findings']:
 path=f['path'];reason=f['reason']
 if f['classification']==d.EQUIVALENT: classes['maintained comparator equivalent']+=1
 elif reason=='Semantic record attribute changed' and path.endswith('/@'+LANG) and f['before'] is None and f['after']=='en': classes['absent scalar xml:lang becomes en']+=1
 elif reason=='Attribute absent after transport' and '/organizations[' in path and path.endswith('/@identifier'): classes['organisation folder representation identifier omitted']+=1
 elif reason=='Subtree absent after transport; raw input retained by hash' and '/organizations[' in path and path.endswith('/label[1]'): classes['redundant catalogue reference label omitted']+=1
 elif reason=='Order retained as evidence: may affect stacking, routing, languages or anonymous navigation' and '/organizations[' in path and f['before']==['label[1]'] and f['after']==[]: classes['catalogue reference child order reflects omitted sole label']+=1
 else: unclassified.append({k:f[k] for k in ['classification','path','reason']})
a=Path('enterprise-architecture/design/archimate/published/1.1.19/Harmonia-Architecture.xml');b=p/'archi-returned.xml.gz'
ba,af=d.Exchange(a),d.Exchange(b)
counts={}
for group,item in [('elements','element'),('relationships','relationship'),('views/diagrams','view')]:
 x={n.get('identifier'):n for n in ba.root.findall('/'.join('a:'+s for s in group.split('/'))+'/a:'+item,c.N)}
 y={n.get('identifier'):n for n in af.root.findall('/'.join('a:'+s for s in group.split('/'))+'/a:'+item,c.N)}
 def transport_signature(node, model):
  n=copy.deepcopy(node)
  for s in n.iter():
   if s.tag in ['{'+c.N['a']+'}'+t for t in ['name','documentation','value']] and s.get(LANG)=='en': s.attrib.pop(LANG)
  return signature(n,model)
 def keyed_signature(node,model):
  n=copy.deepcopy(node)
  for props in n.findall('.//a:properties',c.N):
   children=list(props)
   assert len({model.keys[v.get('propertyDefinitionRef')] for v in children})==len(children)
   props[:]=sorted(children,key=lambda v:model.keys[v.get('propertyDefinitionRef')])
  return transport_signature(n,model)
 counts[item]={'before':len(x),'after':len(y),'sameIds':x.keys()==y.keys(),'equalAfterOnlyAbsentEnglishLanguage':sum(transport_signature(n,ba)==transport_signature(y[k],af) for k,n in x.items()),'equalAlsoAllowingUniquelyKeyedUnorderedProperties':sum(keyed_signature(n,ba)==keyed_signature(y[k],af) for k,n in x.items())}
nav=d.navigation_delta(ba,af)
rep={'status':'CLASSIFIED WITH RAW STRICT-VERIFIER FAILURE RETAINED','classifications':dict(classes),'unclassifiedCount':len(unclassified),'unclassified':unclassified,'boundedIndependentSignatureCheck':counts,'viewNavigationDeltaCounts':{k:len(v) for k,v in nav.items()},'limits':['Not raw XML equivalence; the strict maintained transport verifier failed and remains unchanged.','The additional evidence signatures separately account for absent/en scalar-language additions and uniquely keyed unordered Property order, retaining identity, types, endpoints, text, value multiplicity, other attributes and ordered View content. No production validator is changed.','Archi omits redundant element/relationship catalogue reference labels and generated organisation identifiers. The maintained canonical projection regenerates them; exact input and output are retained.','The no-edit return is evidence only; it does not replace canonical resources, maintained presentation or the accepted candidate.','Visual acceptance of the candidate is separately attributed to the Solution Architect closure Task; no acceptance of transport loss or unrelated semantics is inferred.']}
(p/'archi-transport-classification.json').write_bytes(c.encoded(rep))
print(json.dumps(rep,indent=2))
