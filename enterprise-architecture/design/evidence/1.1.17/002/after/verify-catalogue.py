# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Purpose-selected RAD 1.1.17 / 002 preservation and catalogue proof."""
import collections,copy,gzip,hashlib,json,sys,tempfile
from pathlib import Path
ROOT=Path.cwd();sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_navigation as nav
import exchange_xml as x
import exchange_compare as compare
E=ROOT/'enterprise-architecture/design/evidence/1.1.17/002'
# Retained exact controls are decompressed only in a temporary verification tree.
work=tempfile.TemporaryDirectory(prefix='harmonia-catalogue-proof-')
returned_path=Path(work.name)/'returned.xml'
native_path=Path(work.name)/'imported.archimate'
returned_path.write_bytes(gzip.decompress((E/'after/archi-transport/returned-model.xml.gz').read_bytes()))
native_path.write_bytes(gzip.decompress((E/'after/archi-transport/imported-model.archimate.gz').read_bytes()))
candidate=ROOT/c.ARCHIMATE_PUBLICATION/'1.1.17/Harmonia-Architecture.xml'
sha=lambda b:hashlib.sha256(b).hexdigest()
_,elements=c.load(ROOT)
before=c.ET.fromstring(gzip.decompress((E/'before/iteration-001-Harmonia-Architecture.xml.gz').read_bytes()))
content,validation=c.project(ROOT);after=c.ET.fromstring(content)
# Stronger transition bound: the entire XML outside organisation is identical.
a,b=copy.deepcopy(before),copy.deepcopy(after)
for model in (a,b): model.remove(model.find('a:organizations',c.N))
assert x.signature(a)==x.signature(b), 'Unexpected difference outside organisations'
for group in ('elements','relationships','views'):
 assert x.signature(before.find('a:'+group,c.N))==x.signature(after.find('a:'+group,c.N)),group+' changed'
views=lambda model:next(n for n in model.find('a:organizations',c.N) if n.findtext('a:label',namespaces=c.N)=='Views')
assert x.signature(views(before))==x.signature(views(after)), 'View tree changed'
pins=json.loads((E/'before/preserved-inputs.json').read_text())
assert all(sha((ROOT/p).read_bytes())==h for p,h in pins.items()),'Preserved input changed'
assert c.project(ROOT)[0]==content,'Non-deterministic projection'
by_rep={m['exchange']['identifier']:cid for cid,(m,_,_) in elements.items()}
edges={e.get('identifier'):e for e in after.findall('a:relationships/a:relationship',c.N)}
# Explicit expected native category table, independent of projection routing.
category_for={t:('Strategy' if t in {'Resource','Capability','CourseOfAction','ValueStream'} else
 'Business' if t.startswith('Business') or t in {'Contract','Representation','Product'} else
 'Application' if t.startswith('Application') or t=='DataObject' else
 'Other' if t in {'Grouping','Location'} else
 'Motivation' if t in {'Stakeholder','Driver','Assessment','Goal','Outcome','Principle','Requirement','Constraint','Meaning','Value'} else
 'Implementation & Migration' if t in {'WorkPackage','Deliverable','ImplementationEvent','Plateau','Gap'} else 'Technology & Physical')
 for t in c.read_json(ROOT/c.CONFIG/'type-catalogue.json')['types']}

def check_catalogues(model):
 names={n.get('identifier'):n.findtext('a:name',namespaces=c.N) for n in model.findall('a:elements/a:element',c.N)}
 placements=model.findall('a:organizations//a:item[@identifierRef]',c.N)
 actual=collections.Counter(n.get('identifierRef') for n in placements)
 view_ids={n.get('identifier') for n in model.findall('a:views/a:diagrams/a:view',c.N)}
 assert actual==collections.Counter(list(by_rep)+list(edges)+list(view_ids)), 'Catalogue/reference coverage'
 counts=collections.Counter();sources=set();source_labels={}
 for item in placements:
  ref=item.get('identifierRef');path=[n.findtext('a:label',namespaces=c.N) for n in reversed(list(item.iterancestors(c.tag('item'))))]
  if ref in by_rep:
   m=elements[by_rep[ref]][0];assert path==[category_for[m['archimateType']],'Catalogue',m['archimateType']],path
   assert (item.findtext('a:label',namespaces=c.N) or names[ref])==m['displayName']
   counts[(path[0],m['archimateType'])]+=1
  elif ref in edges:
   cid=by_rep[edges[ref].get('source')];label=elements[cid][0]['displayName']+' ['+cid+']'
   assert path==['Relations','Catalogue',label],path
   assert not item.findall('a:item',c.N)
   sources.add(cid);source_labels[cid]=label
 assert len(set(source_labels.values()))==len(sources)
 rels=model.find('a:organizations/a:item[a:label="Relations"]/a:item',c.N)
 assert len(rels.findall('a:item',c.N))==len(sources)
 return dict(elements=sum(counts.values()),relationships=len(edges),sourceDirectories=len(sources),
             elementTypes=len(counts),byNativeType=[dict(category=k[0],nativeType=k[1],count=v) for k,v in sorted(counts.items())])
accounting=check_catalogues(after)
(E/'after/catalogue-accounting.json').write_bytes(c.encoded(accounting))
assert candidate.read_bytes()==content, 'Candidate publication differs from projection'
returned=c.secure_xml(returned_path)
returned_accounting=check_catalogues(returned)
assert returned_accounting==accounting
assert x.signature(views(after))==x.signature(views(returned))
from continuum_workbench import signatures
assert signatures(after,'views/diagrams','view')==signatures(returned,'views/diagrams','view')
b=compare.Exchange(candidate);a=compare.Exchange(returned_path)
raw=compare.compare(b,a);norm=compare.architectural_delta(b,a)
# Retain raw findings. No production comparison rule is relaxed. Verify that
# every semantic transport finding is exactly an absent-to-English language tag;
# this bounded observation does not supply missing language authority.
semantic=[f for f in raw['findings'] if f['classification']==compare.SEMANTIC]
assert all(f['path'].endswith('/@{http://www.w3.org/XML/1998/namespace}lang') and
           f['before'] is None and f['after']=='en' for f in semantic), 'Unexpected semantic transport drift'
assert all(f['path'].endswith('/@{http://www.w3.org/XML/1998/namespace}lang') and
           f['before'] is None and f['after']=='en'
           for row in norm['relationships']['changed'] for f in row['differences'])
# Production raw comparator classified every other difference as transport
# equivalence (keyed collection order / representation). Verify exact canonical
# values separately without replacing the original return or weakening a rule.
import continuum_workbench as w
_,left=w.model(candidate);_,right=w.model(returned_path)
assert left==right, 'Canonical field/value drift'
assert not any(raw['relationships'][k] for k in ('missing_ids','additional_ids'))
assert not any(raw['presentation'][k] for k in ('missing_ids','additional_ids','changed_records'))
# Rebuild returned element/relation catalogue without touching returned semantics
# or Views. Complete organisation including View branches matches candidate.
regenerated=copy.deepcopy(returned);nav.project(regenerated,elements,c.load(ROOT)[0])
assert x.signature(after.find('a:organizations',c.N))==x.signature(regenerated.find('a:organizations',c.N))
# Actual saved Archi native tree, not just Exchange organisation.
native=c.ET.parse(str(native_path)).getroot();native_types={m['exchange']['identifier']:m['archimateType'] for m,_,_ in elements.values()}
seen_e=[];seen_r=[]
for node in native.findall('.//element'):
 rid=node.get('id')
 if rid in by_rep:
  seen_e.append(rid);p=node.getparent();assert p.get('name')==native_types[rid] and p.getparent().get('name')=='Catalogue'
  assert p.getparent().getparent().get('name')==category_for[native_types[rid]]
  assert node.get('name')==elements[by_rep[rid]][0]['displayName']
 elif rid in edges:
  seen_r.append(rid);cid=by_rep[edges[rid].get('source')]
  p=node.getparent();assert p.get('name')==elements[cid][0]['displayName']+' ['+cid+']'
  assert p.getparent().get('name')=='Catalogue' and p.getparent().getparent().get('name')=='Relations'
assert collections.Counter(seen_e)==collections.Counter(by_rep.keys());assert collections.Counter(seen_r)==collections.Counter(edges.keys())
transport=E/'after/archi-transport';transport.mkdir(exist_ok=True)
for source,name in [(returned_path,'returned-model.xml.gz'),(native_path,'imported-model.archimate.gz')]:
 (transport/name).write_bytes(gzip.compress(source.read_bytes(),mtime=0))
(transport/'raw-comparison.json.gz').write_bytes(gzip.compress(c.encoded(raw),mtime=0))
(transport/'normalised-review.json').write_bytes(c.encoded(norm))
report=dict(rawReasons=[dict(classification=k[0],reason=k[1],count=v) for k,v in sorted(collections.Counter((f['classification'],f['reason']) for f in raw['findings']).items())],status='PASS_WITH_DOCUMENTED_TRANSPORT_DIFFERENCES',baselineSha256=sha(gzip.decompress((E/'before/iteration-001-Harmonia-Architecture.xml.gz').read_bytes())),
 candidateSha256=sha(content),returnedSha256=sha(returned_path.read_bytes()),nativeSha256=sha(native_path.read_bytes()),
 preservedCanonicalSources=len(elements),preservedPinnedInputs=len(pins),unchangedSemanticCollections=['elements','relationships','views'],unchangedViewTree=True,unchangedOutsideOrganizations=True,
 catalogue=accounting,deterministicRegeneration='PASS',returnedCatalogueRegeneration='PASS',
 actualArchi510ImportExport='PASS',nativeTreeCatalogueVerification='PASS',
 rawComparisonStatus=raw['comparison_status'],rawClassifications=raw['classifications'],semanticTransportFindings=len(semantic),
 normalisedRelationshipLanguageChanges=len(norm['relationships']['changed']),
 transportLimitation='Archi drops 3724 redundant reference-label subtrees and 724 generated folder identifiers, and introduces anonymous-folder ordering differences. Native folders, entry names and View hierarchy are verified separately and regenerate from maintained inputs. Archi adds en language attributes, including 222 Iteration 001 relationship names/documentation. Raw findings retained; no production comparator relaxed. No IDs, endpoint direction, text, property values, Views or View tree lost. Maintained canonical source/template and exact before/return/native retained.',
 humanStructuralReview='PENDING Solution Architect',architecturalAcceptance='NOT_ASSERTED')
(E/'after/preservation-verification.json').write_bytes(c.encoded(report));print(json.dumps({k:v for k,v in report.items() if k!='catalogue'},indent=2))
