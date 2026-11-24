# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify the bounded correction against its actual working-tree before state."""
import sys,json,copy
from pathlib import Path
sys.path.insert(0,'enterprise-architecture/tooling');import architecture_continuum as c,continuum_navigation as n
root=Path.cwd();out=root/'enterprise-architecture/design/evidence/1.1.21/001/after/patient-transport-follow-up';before=out.parent.parent/'before/patient-transport-follow-up';s=c.read_json(before/'source-state.json');_,e=c.load();old='5f6d333e94634420b8a8bbf8b9cb6e2a';new='ad2012d59bb542e8bc7d8bd20547e342';a,b=e[old][0],e[new][0]
changed=[str(p.relative_to(root)) for m,body,p in e.values() if c.sha(p.read_bytes())!=s['sha256'][str(p.relative_to(root))]]
assert set(changed)=={s['selectedElements'][cid]['path'] for cid in [old,new]}
assert len(e)==6542
for cid,rec in s['selectedElements'].items():
 assert e[cid][0]['exchange']==rec['metadata']['exchange'];assert e[cid][0]['archimateType']==rec['metadata']['archimateType']
assert e[old][1].startswith(s['selectedElements'][old]['body'])
assert a['relationships'][0]==s['selectedElements'][old]['metadata']['relationships'][0]
rows={r['identifier']:r for m,body,p in e.values() for r in m['relationships']};assert set(rows)==set(s['relationships'])
rid='id-3555d732029e4afc8c838289340cbf54';changededges=[key for key,row in rows.items() if c.sha(c.encoded(row))!=s['relationships'][key]];assert changededges==[rid]
prior=s['selectedElements'][new]['metadata']['relationships'][0];expected=copy.deepcopy(prior);expected['source']=a['exchange']['identifier'];expected['provenance']['endpointReconciliation']=rows[rid]['provenance']['endpointReconciliation'];assert expected==rows[rid]
assert a['stakeholderPerspective']=='Patient' and b['standing']['currentness']=='superseded' and not b.get('stakeholderPerspective')
assert len([m for m,body,p in e.values() if m.get('stakeholderPerspective')])==16
xml=c.secure_xml(root/c.DESIGN/'projection-template.xml');now={v.get('identifier'):v for v in xml.findall('.//a:view',c.N)};changedviews=[key for key,v in now.items() if c.sha(c.ET.tostring(v))!=s['viewHashes'][key]]
expectedviews={'id-75a00ddc7c384343a0b95324d5f2d092','id-cbbd8ecf3d3c4a8cb5c317cf88482765'};assert set(changedviews)==expectedviews
for vid in expectedviews:
 v=copy.deepcopy(now[vid]);nodes=[node for node in v.findall('.//a:node',c.N) if node.get('elementRef')==a['exchange']['identifier']];assert len(nodes)==1;nodes[0].set('elementRef',b['exchange']['identifier']);assert c.sha(c.ET.tostring(v))==s['viewHashes'][vid]
for vid,v in now.items():assert all(node.get('elementRef')!=b['exchange']['identifier'] for node in v.findall('.//a:node',c.N))
pins=c.read_json(root/'enterprise-architecture/design/evidence/1.1.21/001/before/stakeholder-journey-incorporation/source-state.json')['sha256'];protected={p:h for p,h in pins.items() if '/published/' in p or '/register/' in p or p=='architecture-release.json'};assert all(c.sha((root/p).read_bytes())==h for p,h in protected.items())
for pinfile in ['transport-input-pins.json','corrected-transport-input-pins.json']:
 for path,h in c.read_json(before/pinfile).items():assert c.sha(Path(path).read_bytes())==h
record={'status':'PASS','canonicalElements':len(e),'relationships':len(rows),'changedCanonicalSources':changed,'newCanonicalIds':0,'newOrRemovedRelationshipIds':0,'onlyReboundCurrentParticipationRelationship':rid,'historicalPrivacyRelationshipExact':True,'preservedOriginalPatientBodyPrefix':True,'broaderPatientGroupUnchanged':True,'currentCataloguePerspectives':16,'viewsWithOnlyPatientElementRefChange':changedviews,'allOtherViewsByteIdentical':len(now)-len(changedviews),'visualGeometryVisualIdsConnectionsUnchanged':True,'originalTransportInputsComparatorAndVerifierUnchanged':True,'acceptedPublicationRegisterAndReleaseFilesUnchanged':len(protected),'scope':'Compares against the actual pre-follow-up working tree; does not relabel old incorporation evidence as current.'}
(out/'correction-validation.json').write_bytes(c.encoded(record));print(json.dumps(record,indent=2))
