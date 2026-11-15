import hashlib,json,shutil,sys,tempfile
from pathlib import Path
root=Path.cwd();sys.path.insert(0,str(root/'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_manifest as manifest
import publish_continuum as pub
import publish_continuum_rad as rad
out=root/'enterprise-architecture/design/evidence/1.1.25/001/after/release-closure'
_,elements=c.load(root);hashes={}
def equal(rel,content):
 if isinstance(content,str):content=content.encode()
 assert (root/rel).read_bytes()==content,str(rel)
 hashes[str(rel)]=hashlib.sha256(content).hexdigest()
with tempfile.TemporaryDirectory(prefix='harmonia-1.1.25-publication-reproduction-') as name:
 stage=Path(name)
 for rel in [c.CONTINUUM,c.TOOLING,Path('enterprise-architecture/governance'),c.DESIGN,rad.SOURCE]:
  shutil.copytree(root/rel,stage/rel,ignore=shutil.ignore_patterns('published','__pycache__','.venv'))
 shutil.copyfile(root/'architecture-release.json',stage/'architecture-release.json')
 for metadata,body,path in elements.values():
  for origin in metadata['provenance']:
   if origin.get('retainedAt'):
    rel=Path(origin['retainedAt']);(stage/rel).parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(root/rel,stage/rel)
 c.validate(stage);print('Maintained inputs validated; no docs/, scripts/ or Git.',flush=True)
 equal(c.MANIFEST,manifest.render(stage))
 content,validation=c.project(stage)
 for rel in [Path('enterprise-architecture/design/archimate/published/1.1.25/Harmonia-Architecture.xml'),Path('enterprise-architecture/documentation/continuum-manifest/published/1.1.25/Harmonia-Architecture.xml')]:equal(rel,content)
 print('Manifest and both Exchange copies reproduced',flush=True)
 for label,generator,relative in [('reader',pub.outputs,Path('enterprise-architecture/documentation/continuum-manifest/published/1.1.25')),('narrative',rad.outputs,Path('enterprise-architecture/documentation/continuum-rad/published/1.1.25'))]:
  artifacts=generator(stage,stage/relative)
  for filename,data in artifacts.items():equal(relative/filename,data)
  print(label+' reproduced: '+str(len(artifacts))+' artifacts',flush=True)
 for forbidden in ['docs','scripts','.git']:assert not (stage/forbidden).exists()
 assert all(c.sha(p.read_bytes())==c.sha((stage/p.relative_to(root)).read_bytes()) for m,b,p in elements.values())
report={'status':'PASS','release':'1.1.25','identicalArtifacts':hashes,'canonicalSources':len(elements),'liveIdentityAllocations':0,'legacyRadPresent':False,'scriptsPresent':False,'gitPresent':False,'limits':'Independent final-publication generation reproduction; no broad test-suite PASS, Java verification, new Archi transport or new human acceptance claim.'}
(out/'reproducibility.json').write_text(json.dumps(report,indent=2)+'\n');print('PASS: '+str(len(hashes))+' identical artifacts',flush=True)
