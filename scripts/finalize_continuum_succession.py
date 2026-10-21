# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
import sys,os,re
from pathlib import Path
import architecture_continuum as c
import continuum_source_succession as s
def finalize():
    _,elements=c.load();records=c.read_json(s.AFTER/'source-succession.json');account=c.read_json(s.AFTER/'corpus-accounting.json')
    index=c.ROOT/c.PUBLICATION/'1.1.5/README.md'
    historical_names = {'docs/markdown/governance/baseline/'+n+'.md' for n in ['business-capabilities','business-enabling-capabilities','enterprise-capabilities','features','functions','information-architecture','motivation','processes','products','relationships','services']}
    records = [r for r in records if r['source'] not in historical_names]
    for r in records:
     p=c.ROOT/r['source'];original=s.original(r['source'])
     headings=re.findall(r'^## .+$',original,re.M)
     retired={row['selector'] for row in r['retiredSections']}
     r['activeArchitecturalCarrierRetired']=bool(headings) and all(h in retired for h in headings)
     r['successorRole']='Historical source and navigation; all architectural sections incorporated' if r['activeArchitecturalCarrierRetired'] else 'Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles'
     banner='<!-- continuum-source-succession -->\n> **RAD-1.1.5 source succession:** '+r['successorRole']+'. See the [canonical continuum]('+os.path.relpath(index,p.parent)+') and [section correspondence]('+os.path.relpath(s.AFTER/'source-succession.json',p.parent)+'). Original source wording and standing below are preserved.\n\n'
     text=p.read_text();p.write_text(banner+text if not text.startswith('<!-- continuum-source-succession -->') else text)
    for name in ['business-capabilities','business-enabling-capabilities','enterprise-capabilities','features','functions','information-architecture','motivation','processes','products','relationships','services']:
     source='docs/markdown/governance/baseline/'+name+'.md';p=c.ROOT/source;raw=s.original(source)
     ids=sorted({cid for cid in elements if cid in raw})
     if name=='relationships':ids=sorted({cid for cid,(m,_,_) in elements.items() if any(r['identifier'] in raw for r in m['relationships'])})
     banner='<!-- continuum-source-succession -->\n------- Legacy Content - Superseded ------- Start ------\nSuperseded as an active architectural carrier by the [canonical element continuum]('+os.path.relpath(index,p.parent)+').\n[Exact source/identity correspondence]('+os.path.relpath(s.AFTER/'source-succession.json',p.parent)+') records this historical generated carrier. Its original bytes and then-current claims are retained below; it is no longer a current authoring source.\n---------------------------------------------------------\n\n'
     p.write_text(banner+raw+'\n------- Legacy Content - Superseded ------- Finish ----\n')
     records.append({'source':source,'entryRevision':s.ENTRY,'entrySha256':c.sha(raw.encode()),'retiredSections':[{'selector':'whole historical generated carrier','canonicalIds':ids,'originalSha256':c.sha(raw.encode())}],'originalBytesPreserved':raw in p.read_text(),'activeArchitecturalCarrierRetired':True,'successorRole':'Historical generated publication; canonical Markdown is the active source'})
    rby={r['source']:r for r in records}
    for rec in account['sources']:
     if rec['source'] in rby:
      r=rby[rec['source']];rec['retiredSectionCount']=len(r['retiredSections']);rec['activeArchitecturalCarrierRetired']=r['activeArchitecturalCarrierRetired'];rec['successorRole']=r['successorRole']
    account['retiredSourceSections']=sum(len(r['retiredSections']) for r in records);account['sourcesWithRetiredSections']=len(records);account['fullyRetiredCarriers']=sum(r['activeArchitecturalCarrierRetired'] for r in records)
    (s.AFTER/'source-succession.json').write_bytes(c.encoded(records));(s.AFTER/'corpus-accounting.json').write_bytes(c.encoded(account));print({k:account[k] for k in ['filesProcessed','retiredSourceSections','sourcesWithRetiredSections','fullyRetiredCarriers']})
    # Highest authority remains the same 18 axioms, now canonical individual Principles.
    axioms=c.read_json(c.ROOT/'docs/markdown/governance/baseline/motivation.json')['elements']
    lines=['<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->','','# Harmonia Architectural Axioms','','The following individually represented Principles are the highest architectural authority. RAD-1.1.5 changes their canonical representation, not their meaning, identity or authority. This page is navigation, not a second definition register.','','The [original register](../../docs/markdown/governance/architectural-axioms.md) preserves unchanged source sections and supersession correspondence. The [representation contract](continuum-representation.md) and [HCRL 002](../design/review/1.1.5/hcrl-1.1.5-002.pending.md) record application and holistic review standing.','']
    for row in axioms:
     if row['key'].startswith('AX-') and row['record_state']=='current':lines.append('- ['+row['name']+']('+os.path.relpath(elements[row['canonicalId']][2],c.ROOT/'enterprise-architecture/governance')+')')
    (c.ROOT/'enterprise-architecture/governance/architectural-axioms.md').write_text('\n'.join(lines)+'\n')

if __name__ == '__main__': finalize()
