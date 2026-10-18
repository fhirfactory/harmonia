from pathlib import Path
import hashlib,json,sys
sys.path.insert(0,'docs/archimate/framework/tools')
import workspace_paths as w
import verify_workspace as v
E=Path('docs/archimate/processing/publishing/1.0.8')
plan=json.loads((E/'movement-plan.json').read_text());before=json.loads((E/'before-evidence.json').read_text())
sha=lambda p:hashlib.sha256(Path(p).read_bytes()).hexdigest()
records=[];changed=[];unchanged=[]
for r in plan['records']:
 if not r['tracked']:continue
 dest=w.historical_path(r['source']);relative=str(dest.relative_to(w.ROOT))
 actual=sha(dest)
 if actual!=r['sha256']:
  assert relative.startswith(('docs/archimate/framework/tools/','docs/archimate/framework/tests/')) or relative=='docs/archimate/framework/Makefile',(relative,actual)
  changed.append(dict(source=r['source'],destination=relative,before=r['sha256'],after=actual))
 else:unchanged.append(relative)
 if '/processing/' in relative:kind='HISTORICAL / FROZEN EVIDENCE';reason='Release-specific publication processing evidence, retained under its original release; bytes unchanged.'
 elif '/exchange/publish/' in relative:kind='PUBLISHED EXCHANGE ARTEFACT';reason='Finished historical exchange XML; bytes unchanged.'
 elif 'historical-workspace' in relative or 'roundtrip-increment' in relative:kind='HISTORICAL / FROZEN EVIDENCE';reason='Frozen mixed narrative; current instructions supplied separately.'
 else:kind='FRAMEWORK';reason='Reusable machinery or framework baseline/support fixture.'
 records.append(dict(source=r['source'],destination=relative,classification=kind,rationale=reason,before_sha256=r['sha256'],after_sha256=actual,bytes_unchanged=actual==r['sha256']))
unchanged_wider=[];edited_wider=[]
allowed={'architecture-release.json','docs/markdown/governance/baseline/README.md','docs/markdown/governance/baseline/correspondence.json'}
for p,digest in before.items():
 actual=sha(w.historical_path(p))
 if actual==digest:unchanged_wider.append(p)
 else:
  assert p in allowed,p
  edited_wider.append(p)
for p in ['docs/markdown/governance/baseline/architecture.json','docs/markdown/governance/baseline/motivation.json','docs/markdown/governance/architecture-identity/registry.json']:
 assert sha(p)==before[p],p
relocated=Path('/tmp/harmonia-restructured-107');reproduction=[]
for p in sorted(x for x in relocated.rglob('*') if x.is_file()):
 relative=p.relative_to(relocated);expected=w.historical_path(relative)
 # Original independent generation used an intermediate layout. Resolve by the exact filename for its two moved publication outputs.
 if not expected.exists():
  if p.name=='Harmonia-Architecture-1.0.7.xml':expected=w.PUBLISH/'1.0.7'/p.name
  elif p.name=='canonical-identity-validation.json':expected=w.ESTABLISHMENT_EVIDENCE/p.name
 assert p.read_bytes()==expected.read_bytes(),relative
 reproduction.append(dict(path=str(expected.relative_to(w.ROOT)),sha256=sha(p),status='BYTE-IDENTICAL'))
assert len(reproduction)==12,len(reproduction)
(E/'release-1.0.7-relocated-comparison.json').write_text(json.dumps(dict(status='PASS',outputs=reproduction),indent=2)+'\n')
result=dict(status='PASS',unchanged_moved_tracked_files=len(unchanged),unchanged_files=unchanged,changed_framework_files=changed,unchanged_wider_evidence_files=len(unchanged_wider),wider_metadata_edits=edited_wider,canonical_resource_captures='BYTE-IDENTICAL',ec_registry='BYTE-IDENTICAL',canonical_ids_allocated=0,canonical_ids_modified=0,empirical_evidence='ALL BEFORE HASHES PRESERVED',old_umbrella_exists=Path('docs/archimate/capability-architecture').exists())
assert not result['old_umbrella_exists']
(E/'preservation.json').write_text(json.dumps(result,indent=2)+'\n')
(E/'migration-comparison.json').write_text(json.dumps(v.verify(),indent=2)+'\n')
(E/'accepted-source-destination-map.json').write_text(json.dumps(dict(baseline_commit=plan['baseline_commit'],records=records,untracked_originals=[r for r in plan['records'] if not r['tracked']]),indent=2)+'\n')
print('PASS:',len(unchanged),'moved tracked files byte-identical;',len(changed),'framework files edited;',len(unchanged_wider),'wider evidence files unchanged; all 12 relocated 1.0.7 core outputs byte-identical.')
