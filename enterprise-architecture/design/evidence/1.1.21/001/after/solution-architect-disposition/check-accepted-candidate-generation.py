# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded candidate reproduction using maintained entry points, no live publication."""
import sys,json,shutil,tempfile,subprocess,hashlib,os
from pathlib import Path
sys.path.insert(0,'enterprise-architecture/tooling')
import architecture_continuum as c
root=Path.cwd();relative=Path('enterprise-architecture/design/evidence/1.1.21/001/after/patient-transport-follow-up');out=root/relative
_,elements=c.load();before={str(p.relative_to(root)):c.sha(p.read_bytes()) for m,b,p in elements.values()}
with tempfile.TemporaryDirectory(prefix='harmonia-1121-independent-') as name:
 stage=Path(name)
 for rel in [c.CONTINUUM,Path('enterprise-architecture/governance'),c.TOOLING,c.DESIGN,Path('enterprise-architecture/documentation/continuum-rad')]:
  shutil.copytree(root/rel,stage/rel,ignore=shutil.ignore_patterns('__pycache__','.venv','published'))
 shutil.copyfile(root/'architecture-release.json',stage/'architecture-release.json')
 retained={item['retainedAt'] for m,b,p in elements.values() for item in m['provenance'] if item.get('retainedAt')}
 for rel in retained:
  dest=stage/rel;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(root/rel,dest)
 output=stage/relative;output.mkdir(parents=True,exist_ok=True)
 tools='enterprise-architecture/tooling/'
 commands=[[tools+'continuum_manifest.py'],[tools+'architecture_continuum.py','project','--output',str(relative/'candidate/Harmonia-Architecture.xml')],[tools+'publish_continuum.py','--output',str(relative/'candidate')],[tools+'publish_continuum_rad.py','--output',str(relative/'narrative')]]
 steps=[]
 for args in commands:
  run=subprocess.run([sys.executable,'-B',*args],cwd=stage,env={**os.environ,'PYTHONPATH':''},capture_output=True,text=True,timeout=90)
  assert run.returncode==0,(args,run.stdout[-1200:],run.stderr[-1200:])
  steps.append({'command':args,'status':'PASS','output':run.stdout[-1800:]})
 artifacts={}
 for folder in ['candidate','narrative']:
  for p in (out/folder).rglob('*'):
   if not p.is_file():continue
   rel=p.relative_to(root);other=stage/rel
   assert p.read_bytes()==other.read_bytes(),str(rel)
   artifacts[str(p.relative_to(out))]=c.sha(p.read_bytes())
 assert (root/c.MANIFEST).read_bytes()==(stage/c.MANIFEST).read_bytes()
 assert not any((stage/p).exists() for p in ['docs','scripts','.git'])
 assert before=={str(p.relative_to(root)):c.sha(p.read_bytes()) for m,b,p in c.load()[1].values()}
 record={'status':'PASS','scope':'Candidate generation only, using maintained entry points in an isolated current-EA tree; no accepted-release publication path or --publish operation used.','steps':steps,'identicalCandidateArtifacts':artifacts,'identicalRootManifestSha256':c.sha((root/c.MANIFEST).read_bytes()),'canonicalSourceCount':len(before),'canonicalSourcesUnchanged':True,'legacyDocsScriptsAndGitAbsent':True,'retainedProvenanceFiles':len(retained),'testSuites':'Separately retained bounded tooling checks; no repeated aggregate suite here.','limits':'Source-byte reproducibility only; no visual acceptance, PDF compilation or immutable publication claim.'}
 (root/'enterprise-architecture/design/evidence/1.1.21/001/after/solution-architect-disposition/independent-candidate-generation.json').write_bytes(c.encoded(record));print(json.dumps({'status':'PASS','identicalArtifacts':len(artifacts)+1}))
