#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Make retained, unprojected source predicates readable on their own subjects."""
import json,os
from collections import Counter
import architecture_continuum as c
from ingest_rad_continuum import BASE,EVIDENCE,ENTRY


def main():
    _,elements=c.load();template=c.secure_xml(c.ROOT/c.DESIGN/'projection-template.xml')
    defs={p.get('identifier'):p.findtext('a:name',namespaces=c.N) for p in template.findall('a:propertyDefinitions/a:propertyDefinition',c.N)}
    source=c.read_json(c.ROOT/BASE/'architecture.json');keys={r['key']:r['canonicalId'] for r in source['elements']}
    assertions={}
    for m,_,_ in elements.values():
        for prop in m['exchange']['properties']:
            if defs[prop['ref']].endswith('Deferred Relationship Assertions'):
                for value in prop['values']:
                    for row in json.loads(value['text']):
                        key=row['snapshot_pointer']
                        c.require(key not in assertions or assertions[key]==row,'Conflicting retained predicate')
                        assertions[key]=row
    byowner={}
    for pointer,row in sorted(assertions.items()):
        a=row['source_assertion'];cid=keys[a['from']];target=keys[a['to']]
        byowner.setdefault(cid,[]).append({'sourcePointer':pointer,'sourceCanonicalId':cid,'targetCanonicalId':target,**row})
    for cid,rows in byowner.items():
        m,body,path=elements[cid]
        if 'qualifiedAssertions' in m:
            c.require(m['qualifiedAssertions']==rows,'Qualified assertion drift');continue
        m['qualifiedAssertions']=rows
        m['provenance'].append({'source':str(BASE/'architecture.json'),'selector':'source-qualified relationship assertions whose from-key binds to canonicalId '+cid,'role':'Preserved non-native predicates, not new graph relationships','revision':ENTRY})
        body+='\n## Qualified relationship assertions\n\nThese source predicates retain their recorded standing and currentness. Their native mapping remains unestablished; they are not inferred ArchiMate edges. The complete original assertions and qualifications are retained in this element’s metadata.\n\n| Source predicate | Target element | Standing / currentness | Unresolved native meaning |\n| --- | --- | --- | --- |\n'
        for row in rows:
            a=row['source_assertion'];target=elements[row['targetCanonicalId']]
            label=target[0]['displayName'].replace('|','\\|')
            body+='| '+a['type']+' | ['+label+']('+os.path.relpath(target[2],path.parent)+') | '+a['status']+' / '+row['record_state']+' | '+row['reason'].replace('|','\\|')+' |\n'
        path.write_text(c.encode(m,body))
    report={'sourcePredicatesPreserved':len(assertions),'sourceElements':len(byowner),'byPredicate':dict(Counter(r['source_assertion']['type'] for r in assertions.values())),'nativeRelationshipsAdded':0,'canonicalIdsAllocated':0}
    (c.ROOT/EVIDENCE/'after/qualified-assertion-coverage.json').write_bytes(c.encoded(report));print(json.dumps(report))
if __name__=='__main__':main()
