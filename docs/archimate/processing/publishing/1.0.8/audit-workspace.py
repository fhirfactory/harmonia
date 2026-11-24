from pathlib import Path
import json,re,hashlib,subprocess,sys
E=Path('docs/archimate/processing/publishing/1.0.8');root=Path.cwd()
sys.path.insert(0,'docs/archimate/framework/tools');import workspace_paths as w
files=[Path(p) for p in ['docs/archimate/README.md','docs/archimate/framework/README.md','docs/archimate/processing/README.md','docs/archimate/processing/ingestion/README.md','docs/archimate/exchange/ingest/README.md','docs/archimate/exchange/publish/README.md','docs/markdown/governance/baseline/README.md','docs/markdown/governance/README.md','docs/markdown/governance/archi-navigation-and-view-convention.md','docs/markdown/governance/rad-archimate-mapping-catalogue.md','docs/archimate/processing/publishing/1.0.8/publication-1.0.8.md','docs/archimate/processing/publishing/1.0.8/archi-inspection-1.0.8.md','.junie/reports/2026-10-18-archimate-workspace-restructure.md']]
missing=[];links=0
for p in files:
 for target in re.findall(r'\[[^\]]*\]\(([^)]+)\)',p.read_text()):
  if '://' in target or target.startswith('#'):continue
  target=target.split('#')[0];links+=1
  if not (p.parent/target).exists():missing.append((str(p),target))
assert not missing,missing
for directory in w.PUBLISH.iterdir():
 if directory.is_dir():assert len(list(directory.iterdir()))==1 and all(x.suffix=='.xml' for x in directory.iterdir()),directory
assert w.INGEST.is_dir() and w.INGESTION.is_dir()
assert not Path('docs/archimate/capability-architecture').exists()
assert not (w.PUBLISH/'synchronised-baseline').exists()
assert set(x.name for x in w.PROCESSING.iterdir())=={'README.md','publishing','ingestion'}
assert json.loads(Path('architecture-release.json').read_text())==dict(version='1.0.8',previous_version='1.0.7')
assert 'PASS: all 14 generated outputs reproduce byte-for-byte' in (E/'final-layout-make-checks.log').read_text()
assert not re.search('FINAL_[A-Z_]+',Path('.junie/reports/2026-10-18-archimate-workspace-restructure.md').read_text())
for path,digest in json.loads((E/'workspace-verification-1.0.8.json').read_text())['files_sha256'].items():assert hashlib.sha256(Path(path).read_bytes()).hexdigest()==digest,path
assert subprocess.run(['git','diff','--check'],capture_output=True).returncode==0
result=dict(status='PASS',current_document_files=len(files),local_links_checked=links,missing_current_links=missing,processing_directions=['publishing','ingestion'],published_packages='EXACTLY ONE XML EACH',old_umbrella='ABSENT',old_synchronised_publication_container='ABSENT',release=dict(version='1.0.8',previous_version='1.0.7'),complete_independent_reproduction='14/14 BYTE-IDENTICAL',current_verification_fingerprints='ALL MATCH',git_diff_check='PASS',increment_2='NOT BEGUN')
(E/'final-workspace-audit.json').write_text(json.dumps(result,indent=2)+'\n')
paths=sorted(p for p in Path('docs/archimate').rglob('*') if '__pycache__' not in p.parts)
(E/'final-directory-tree.txt').write_text('Final ArchiMate tree (Python runtime caches excluded; frozen logs included):\n'+'\n'.join(str(p)+('/' if p.is_dir() else '') for p in paths)+'\n')
print('PASS:',links,'local links across',len(files),'current documents; lean XML packages; two processing directions; all pinned files unchanged; final 14-output reproduction PASS.')
