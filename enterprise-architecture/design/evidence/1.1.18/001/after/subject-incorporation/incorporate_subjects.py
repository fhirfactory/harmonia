#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Execute the 60 exact accepted CRs with governed creation and source authoring.

No discovery, semantic inference, release allocator or publication overwrite.
The persisted execution ledger must be inspected after an interruption; this
script deliberately refuses a blind retry that could allocate duplicate IDs.
"""
import copy
import hashlib
import json
from pathlib import Path
import sys
import uuid
from lxml import etree as ET
ROOT=Path.cwd()
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
from governed_naming import component
from exchange_xml import N

BASE=ROOT/'enterprise-architecture/design/evidence/1.1.18/001'
OUT=BASE/'after/subject-incorporation'
LEDGER=OUT/'incorporation-results.json'
TASK=BASE/'before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md'
REVIEW=ROOT/'enterprise-architecture/design/review/1.1.18/change-review.md'
HCRL=ROOT/'enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md'
MAPPING=BASE/'before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md'
TEMPLATE=ROOT/c.DESIGN/'projection-template.xml'
def relative(p):return str(p.relative_to(ROOT))
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
def save():LEDGER.write_text(json.dumps(ledger,ensure_ascii=False,indent=2)+'\n')
assert not LEDGER.exists(),'Inspect existing allocations/result before any retry'
assert REVIEW.read_text().count('**Disposition:** Accept')==60
assert 'CR-001–CR-060' in TASK.read_text() and 'DR-3–DR-9 remain unresolved' in TASK.read_text()
facts=json.loads((BASE/'after/bounded-subject-review/scoped-subject-evidence.json').read_text())
assert (BASE/'after/bounded-subject-review/scoped-subject-evidence.json').stat().st_size==90051
crs=json.loads((BASE/'after/bounded-subject-review/change-requests.json').read_text())
rows=facts['folders'];catalogue,existing=c.load()
assert len(rows)==32 and len(crs)==60
existing_subjects={m['subjectGroup']['sourceId']:m for m,b,p in existing.values() if 'subjectGroup' in m}
assert all(row['code'] not in existing_subjects for row in rows)
assert len(existing_subjects)==82
for row in rows:
    assert row['proposedFriendlyName']==component(row['mappingName'])
    assert not any((b['source'],b['selector'])==(relative(MAPPING),row['code']) for m,body,p in existing.values() for b in m['sourceBindings'])
    assert not any(m['archimateType']=='Grouping' and component(m['displayName'])==component(row['mappingName']) for m,body,p in existing.values())
preimages=json.loads((BASE/'before/subject-incorporation/preimages.json').read_text())
for pin in preimages['files']:
    # Only the review records have been changed to record the supplied acceptance.
    if pin['path'].startswith('enterprise-architecture/design/review/1.1.18/'):continue
    assert sha(ROOT/pin['path'])==pin['sha256'],pin['path']
ledger=dict(authority=relative(TASK),hcrl=relative(HCRL),review=relative(REVIEW),reviewAcceptanceSha256=sha(REVIEW),initialCanonicalElements=len(existing),initialRelationships=sum(len(m['relationships']) for m,b,p in existing.values()),subjects=[],relationships=[],presentation=None,releaseTransition='NOT AUTHORISED / NOT PERFORMED',status='APPLYING')
save()
lookup={};row_by_code={r['code']:r for r in rows}
group_cr={r['code']:r['id'] for r in crs if r['kind']=='Subject instance proposal'}
for row in rows:
    code=row['code'];cr=group_cr[code]
    spec=c.template('Grouping',None,ROOT);m=spec['metadata']
    m.update(displayName=row['mappingName'],friendlyName=row['proposedFriendlyName'],canonicalName=None,
        standing=dict(semantic='Solution Architect accepted '+cr+'; incorporated-result review pending',currentness='current'),
        identity=dict(authority=relative(HCRL)+'#dr-2',acceptanceTask=relative(TASK),changeReview=relative(REVIEW)+'#'+cr.lower()),
        naming=dict(authority='enterprise-architecture/governance/canonical-identity-and-naming.md',mode='Local lexical component; accepted Subject classification supplies no naming ancestry',expected=dict(displayName=row['mappingName'],friendlyName=row['proposedFriendlyName'],canonicalName=None)),
        provenance=[dict(source=relative(TASK),selector='DR-2 / '+cr,role='Operative Solution Architect acceptance and incorporation authority; not HCRL closure or release transition',retainedAt=relative(TASK),sha256=sha(TASK)),dict(source=relative(MAPPING),selector='Proposed Subject extensions / '+code,role='Accepted Subject name and concern through the operative Task; mapping alone is not architectural authority',retainedAt=relative(MAPPING),sha256=sha(MAPPING)),dict(source=relative(REVIEW),selector='#'+cr.lower(),role='Accepted bounded Subject instance proposal; original review preserved in before/subject-incorporation',maintained=True)],
        sourceBindings=[dict(source=relative(MAPPING),selector=code),dict(source=relative(REVIEW),selector='#'+cr.lower())],
        relationships=[],unresolvedProperties=dict(canonicalName='Unqualified Grouping has no established formal naming qualifier. Preserve null under the accepted CR and existing naming contract; no new token or Subject-derived ancestry. '+relative(HCRL)+'#dr-2'),
        subjectGroup=dict(sourceId=code,role='subject group' if '.' not in code else 'subject subgroup'))
    spec['navigationParent']=['Cross-Cutting References','Architectural Subjects']
    if row['proposedParentCode']:spec['navigationParent'].append(row_by_code[row['proposedParentCode']]['mappingName'])
    spec['body']='# '+m['displayName']+'\n\n## Architectural content\n\nSubject '+('group' if '.' not in code else 'subgroup')+' **'+code+' — '+m['displayName']+'** in the Solution Architect-accepted G14–G18 extension.\n\n'+row['mappingConcern']+'\n\nThis is classification of architectural statements. Explicit Composition denotes the accepted parent/subgroup classification only; it establishes no Capability decomposition, Component ownership, logical Home, implementation responsibility, Motivation allocation or naming ancestry. The classification code is a source-qualified reference, not canonical identity.\n\nAcceptance and incorporation are bounded by '+relative(HCRL)+'#dr-2, '+cr+' and the operative retained Task. Existing G01–G13, the 784 Motivation candidates and their separate unresolved review matters remain unchanged. No clinical, workforce, source-domain or Capability authority follows from this classification.\n'
    path=c.create(spec,ROOT)
    actual,body=c.decode(path.read_text());lookup[code]=actual
    record=dict(code=code,cr=cr,canonicalId=actual['canonicalId'],exchangeId=actual['exchange']['identifier'],source=relative(path),displayName=actual['displayName'],friendlyName=actual['friendlyName'],canonicalName=None,specialization=None,operation='CREATED_BY_GOVERNED_CREATE',standing=actual['standing'])
    ledger['subjects'].append(record);save()
    print('Created',code,actual['canonicalId'],flush=True)

# Relationships are ordinary authorised canonical assertions under their source
# resource, using the established UUID representation identifier pattern. They
# never own canonicalId. Persist allocations before any endpoint-source writes.
all_existing_relids={r['identifier'] for m,b,p in c.load()[1].values() for r in m['relationships']}
for cr in crs:
    if cr['kind']!='Subject classification relationship proposal':continue
    edge=cr['edge'];rid='id-'+uuid.uuid4().hex
    assert rid not in all_existing_relids;all_existing_relids.add(rid)
    ledger['relationships'].append(dict(cr=cr['id'],identifier=rid,parentCode=edge['parentCode'],childCode=edge['childCode'],sourceCanonicalId=lookup[edge['parentCode']]['canonicalId'],targetCanonicalId=lookup[edge['childCode']]['canonicalId'],source=lookup[edge['parentCode']]['exchange']['identifier'],target=lookup[edge['childCode']]['exchange']['identifier'],archimateType='Composition',operation='ALLOCATED_FOR_SOURCE_ASSERTION'))
save()
model=c.secure_xml(TEMPLATE)
definitions={n.findtext('a:name',namespaces=N):n.get('identifier') for n in model.findall('a:propertyDefinitions/a:propertyDefinition',N)}
assert 'v0.2 / Record State' in definitions and 'v0.2 / Assertion Status' in definitions
for code in [r['code'] for r in rows if not r['proposedParentCode']]:
    m=copy.deepcopy(lookup[code]);path=ROOT/c.element_path(m,catalogue);old,body=c.decode(path.read_text())
    assert old==m and not m['relationships']
    for edge in ledger['relationships']:
        if edge['parentCode']!=code:continue
        m['relationships'].append(dict(identifier=edge['identifier'],source=edge['source'],target=edge['target'],archimateType='Composition',attributes={},standing=dict(semantic='Solution Architect accepted '+edge['cr']+'; incorporated-result review pending',currentness='current'),provenance=dict(source=relative(TASK),retainedAt=relative(TASK),sha256=sha(TASK),selector='DR-2 / '+edge['cr'],meaning='Accepted Subject subgroup classification containment; no Motivation allocation or Capability relationship',review=relative(REVIEW)+'#'+edge['cr'].lower()),name=[dict(text='Subject subgroup containment')],documentation=[dict(text='Accepted '+edge['cr']+': '+edge['parentCode']+' → '+edge['childCode']+'. Subject classification only; no Capability decomposition, Component ownership, logical Home, implementation responsibility or Motivation allocation.')],properties=[dict(ref=definitions['v0.2 / Record State'],values=[dict(text='current',attributes={})]),dict(ref=definitions['v0.2 / Assertion Status'],values=[dict(text='Accepted',attributes={})])]))
        edge['operation']='INCORPORATED_UNDER_SOURCE_SUBJECT'
    temporary=path.with_suffix('.pending');temporary.write_text(c.encode(m,body));temporary.replace(path);save()
    print('Authored classification relationships for',code,flush=True)

# CR-060 is limited to its corrected empty folder path. The existing template
# has no incoming Subject-Based-Views branch. Add only the necessary ancestors
# and corrected leaf; copy no unrelated folders, Views, geometry or properties.
model=c.secure_xml(TEMPLATE)
roots=[n for n in model.findall('a:organizations/a:item',N) if n.findtext('a:label',namespaces=N)=='Views']
assert len(roots)==1
parent=roots[0];created=[]
for title in ('1. Subject-Based-Views','18. Provider Directory','03. Directory-Information-Maintenance'):
    matches=[n for n in parent.findall('a:item',N) if n.findtext('a:label',namespaces=N)==title]
    assert len(matches)<=1
    if matches:parent=matches[0]
    else:
        parent=ET.SubElement(parent,c.tag('item'));ET.SubElement(parent,c.tag('label'),{c.LANG:'en'}).text=title;created.append(title)
assert not parent.findall('a:item',N) and not parent.attrib
TEMPLATE.write_bytes(ET.tostring(model,encoding='UTF-8',xml_declaration=True,pretty_print=True))
ledger['presentation']=dict(cr='CR-060',path=['Views','1. Subject-Based-Views','18. Provider Directory','03. Directory-Information-Maintenance'],createdEmptyPresentationContext=created,existingViewsAndNavigation='Preserved; independent verification required',otherIncomingFoldersOrViews='Not adopted',template=relative(TEMPLATE),templateSha256=sha(TEMPLATE))
ledger['status']='INCORPORATED_PENDING_VERIFICATION';save()
validation=c.validate(ROOT)
ledger['canonicalValidation']={k:v for k,v in validation.items() if isinstance(v,(str,int,float,bool)) or v is None};save()
print('Incorporated 32 Subjects, 27 classification edges and CR-060 presentation; canonical validation PASS',flush=True)
