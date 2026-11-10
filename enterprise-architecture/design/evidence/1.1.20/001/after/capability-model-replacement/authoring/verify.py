#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independent acceptance-source, preservation and migration-boundary checks."""
import copy
import uuid
from investigate import *
from lxml import etree as ET
import continuum_navigation as navigation
import relationship_state
from types import SimpleNamespace

def run():
    checks = []
    def check(name, condition, detail=None):
        checks.append({'check': name, 'status': 'PASS' if condition else 'FAIL', 'detail': detail})
    _, elements = c.load()
    current = {cid:m for cid,(m,b,p) in elements.items() if m['archimateType']=='Capability' and m['standing']['currentness']=='current'}
    bycode = {m['catalogue']['id']:cid for cid,m in current.items()}
    check('Exact active population', collections.Counter(m['catalogue']['tier'] for m in current.values()) == {'CT1':24,'CT2':145,'CT3':585,'Feature':2935})
    check('Accepted stable catalogue identifiers, without omissions or extras', set(bycode)==set(TARGET) and len(bycode)==len(current))
    check('All pre-existing canonical identities retained', set(SNAPSHOT)<=set(elements))
    check('Exactly 3689 independent new canonical identities', len(set(elements)-set(SNAPSHOT))==3689)
    check('UUIDv4 allocation, no reuse of any prior identity', all(uuid.UUID(cid).version==4 for cid in current) and not set(current)&set(SNAPSHOT))
    catalogue=(FR/'Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md').read_text()
    headings={match[1]:match[2] for match in re.finditer(r'^#{2,4} ([BE]\d{2}(?:\.\d{2}){0,2}) — (.+)$',catalogue,re.M)}
    feature_rows={match[1]:(match[2],match[3]) for match in re.finditer(r'^- \*\*`([^`]+)` — (.+?):\*\* (.+)$',catalogue,re.M)}
    check('Catalogue Markdown independently agrees with incorporated populations', len(headings)==754 and len(feature_rows)==2935 and set(headings)|set(feature_rows)==set(bycode))
    check('Exact Feature definitions from accepted catalogue Markdown', all(TARGET[code]['description']==feature_rows[code][1] for code in feature_rows))
    check('Every accepted definition preserved verbatim in canonical content', all(elements[bycode[code]][1].split('## Architectural content\n',1)[1].lstrip().startswith(row['description']+'\n') for code,row in TARGET.items()))
    check('Feature native classification retained', all(m['archimateType']=='Capability' and (m.get('specialization')=='Feature')==(m['catalogue']['tier']=='Feature') for m in current.values()))
    check('Only the documented editorial NEW marker excluded from display names', all(m['catalogue']['sourceName']==TARGET[m['catalogue']['id']]['name'] and m['displayName'].split('] ',1)[1]==re.sub(r' \*\*\[NEW[^\]]*\]\*\*$','',TARGET[m['catalogue']['id']]['name']) for m in current.values()))
    check('Every old Capability/Feature inactive and definition/source contributions retained', all(elements[cid][0]['standing']['currentness'] in ('superseded','retired') and elements[cid][1].startswith(x['body'].rstrip()) and elements[cid][0]['provenance']==x['metadata']['provenance'] and elements[cid][0]['sourceBindings']==x['metadata']['sourceBindings'] for cid,x in OLD.items()))
    check('Historical Exchange identities remain unchanged', all(elements[cid][0]['exchange']['identifier']==x['metadata']['exchange']['identifier'] for cid,x in SNAPSHOT.items()))
    check('All historical native element types unchanged', all(elements[cid][0]['archimateType']==x['metadata']['archimateType'] and elements[cid][0].get('specialization')==x['metadata'].get('specialization') for cid,x in SNAPSHOT.items()))
    check('Unrelated element bodies, identities and source contributions unchanged', all(elements[cid][1]==x['body'] and elements[cid][0]['identity']==x['metadata']['identity'] and elements[cid][0]['provenance']==x['metadata']['provenance'] and elements[cid][0]['sourceBindings']==x['metadata']['sourceBindings'] for cid,x in SNAPSHOT.items() if cid not in OLD))
    check('No unrelated canonical metadata change', all({k:v for k,v in elements[cid][0].items() if k not in ('relationships','qualifiedAssertions','naming','friendlyName','canonicalName')}=={k:v for k,v in x['metadata'].items() if k not in ('relationships','qualifiedAssertions','naming','friendlyName','canonicalName')} for cid,x in SNAPSHOT.items() if cid not in OLD))
    originals={r['identifier']:r for x in SNAPSHOT.values() for r in x['metadata']['relationships']}
    actual={r['identifier']:r for m,b,p in elements.values() for r in m['relationships']}
    check('All 2246 original relationship identities, endpoints and native types retained', set(originals)<=set(actual) and all(all(actual[rid][field]==r[field] for field in ('source','target','archimateType')) for rid,r in originals.items()))
    byrid={m['exchange']['identifier']:cid for cid,(m,b,p) in elements.items()}
    touching={rid for rid,r in originals.items() if byrid[r['source']] in OLD or byrid.get(r['target']) in OLD}
    check('Exact replacement relationship boundary', len(touching)==1470)
    check('Unrelated original relationships unchanged', all(r==actual[rid] for rid,r in originals.items() if rid not in touching))
    check('Original relation labels, explanations and Home specialisation retained', all(actual[rid].get(k)==r.get(k) for rid,r in originals.items() for k in ('name','documentation','specialization','attributes')))
    check('Original relationship evidence retained independently', (BASE/'before/capability-model-replacement/baseline.json.gz').is_file())
    records=c.read_json(OUT/'relationship-reconciliation.json')
    check('Every boundary relationship has exactly one disposition', len(records)==len(touching) and {r['predecessorRelationshipIdentifier'] for r in records}==touching)
    migrated=[r for r in records if r.get('resultingRelationshipIdentifier')]
    check('Migrated relationship types and specialisations are preserved', all(actual[r['resultingRelationshipIdentifier']]['archimateType']==r['archimateType'] and actual[r['resultingRelationshipIdentifier']].get('specialization')==r.get('specialization') for r in migrated))
    active={rid:r for rid,r in actual.items() if not any(p['ref']=='propid-7' and any(v['text']!='current' for v in p['values']) for p in r.get('properties',[]))}
    check('No active relationship still touches a superseded Capability', not any(byrid[r['source']] in OLD or byrid.get(r['target']) in OLD for r in active.values()))
    expected_parent={code:row['parent'] for code,row in TARGET.items() if row['parent']}
    observed_parent={}
    for r in active.values():
        a,b=byrid[r['source']],byrid.get(r['target'])
        if r['archimateType']=='Composition' and a in current and b in current:
            code=current[b]['catalogue']['id'];check('Single accepted parent: '+code,code not in observed_parent)
            observed_parent[code]=current[a]['catalogue']['id']
    check('Exact accepted CT1 → CT2 → CT3 → Feature containment',observed_parent==expected_parent)
    check('Business/Business Enabling and Enterprise containment remain separate',all(a[0]==b[0] for a,b in observed_parent.items()))
    check('No relationship owns a canonicalId',all('canonicalId' not in r for r in actual.values()))
    check('No new Journey, UseCase, UserStory or stakeholder instance',all(m['archimateType']=='Capability' for cid,(m,b,p) in elements.items() if cid not in SNAPSHOT))
    homes=[r for rid,r in active.items() if r.get('specialization')=='Home']
    check('Explicit Home remains directed Association with one Home per source',all(r['archimateType']=='Association' and r.get('attributes',{}).get('isDirected')=='true' for r in homes) and len({r['source'] for r in homes})==len(homes))
    lineage=c.read_json(OUT/'semantic-correspondence.json')
    check('All 842 historical identity correspondences accounted for',len(lineage)==842 and {r['oldCanonicalId'] for r in lineage}==set(OLD))
    check('No complete semantic equivalence inferred from replacement',all(not elements[cid][0]['identity']['replacementHistory']['completeSemanticEquivalenceAsserted'] for cid in OLD))
    check('Only Communications and Publicity lacks a legitimate successor', [r['oldName'] for r in lineage if not r['targetCatalogueIds']]==['[CT3] Communications & Publicity'])
    check('Reciprocal primary successor references',all(not r['targetCatalogueIds'] or r['oldCanonicalId'] in elements[r['replacementCanonicalIds'][0]][0]['identity']['predecessorCanonicalIds'] for r in lineage))
    nav=navigation.load(c.ROOT,elements)
    check('Navigation covers each canonical element exactly once',len(navigation.by_element(nav))==len(elements))
    xml=c.secure_xml(OUT/'candidate-v2/Harmonia-Architecture-candidate.xml')
    template=c.secure_xml(c.ROOT/c.DESIGN/'projection-template.xml')
    check('All authored Views, objects, connections, ordering and geometry unchanged',ET.tostring(xml.find('a:views',c.N))==ET.tostring(template.find('a:views',c.N)))
    check('Every native Name equals governed displayName',all(n.findtext('a:name',namespaces=c.N)==elements[byrid[n.get('identifier')]][0]['displayName'] for n in xml.findall('a:elements/a:element',c.N)))
    protected=c.read_json(BASE/'before/capability-model-replacement/protected-files.json')
    check('All existing publications and complete registers immutable',all((c.ROOT/path).is_file() and c.sha((c.ROOT/path).read_bytes())==sha for path,sha in protected.items()),len(protected))
    check('Release unchanged: allocated 1.1.20, accepted predecessor 1.1.19',c.read_json(c.ROOT/'architecture-release.json')=={'version':'1.1.20','previous_version':'1.1.19'})
    pinned=c.read_json(BASE/'before/capability-model-replacement/input-manifest.json')['verified_sources']
    check('All pinned accepted source/package bytes unchanged',all(c.sha((ROOT/p['path']).read_bytes())==p['sha256'] for p in pinned),len(pinned))
    qualification=all('Separate E07.03.05 semantic review' in m['standing'].get('qualification','') for m in current.values() if m['catalogue']['id'].startswith('E07.03.05'))
    check('Separate E07.03.05 qualification carried without resolution',qualification)
    distinct_targets={r.get('newTargetCatalogueId') for r in migrated if r.get('newTargetCatalogueId')}
    linked={byrid[r[endpoint]] for r in active.values() if r['archimateType']!='Composition' for endpoint in ['source','target'] if byrid.get(r[endpoint]) in current}
    result={'status':'FAIL' if any(x['status']=='FAIL' for x in checks) else 'PASS','checks':checks,'checkCount':len(checks),
        'preservationChecks':len(checks)-len(observed_parent),'activeNonHierarchyRelationships':len(active)-len(observed_parent),
        'distinctReplacementTargets':len(distinct_targets),'replacementElementsWithNonHierarchyRelationships':len(linked),
        'activeFeaturesWithNonHierarchyRelationships':sum(current[cid].get('specialization')=='Feature' for cid in linked),
        'coverageQualification':'Direct represented links are partial architecture traceability; unlinked accepted outcomes are not inferred implementation gaps or satisfaction.',
        'actualArchiTransport':'NOT_PERFORMED','solutionArchitectStructuralReview':'PENDING','externalOfficeVerification':'UNRESOLVED — inherited qualification'}
    (OUT/'preservation-validation.json').write_bytes(c.encoded(result))
    print(json.dumps({k:v for k,v in result.items() if k!='checks'},indent=2))
    for x in checks:
        if x['status']=='FAIL':print(x)
    if result['status']!='PASS':raise SystemExit(1)

if __name__=='__main__':run()
