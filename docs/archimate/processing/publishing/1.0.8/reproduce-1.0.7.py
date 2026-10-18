from pathlib import Path
import json,sys,hashlib,tempfile
root=Path.cwd();sys.path.insert(0,str(root/'scripts'))
import architecture_release as ar
fixture=root/'docs/archimate/processing/publishing/1.0.7/architecture-release.json'
loader=ar.load_release
# Explicit historical release fixture in an isolated process; never change current authority.
ar.load_release=lambda path=fixture:loader(path)
sys.path.insert(0,str(root/'docs/archimate/framework/tools'))
import synchronise_baseline as s
import workspace_paths as w
release=(root/'architecture-release.json').read_bytes()
with tempfile.TemporaryDirectory(prefix='harmonia-final-107-',dir='/tmp') as temp:
 sys.argv=['synchronise_baseline.py','--output-directory',temp]
 assert s.main()==0
 files=sorted(p for p in Path(temp).rglob('*') if p.is_file());assert len(files)==12
 result=[]
 for p in files:
  target=root/p.relative_to(temp)
  assert p.read_bytes()==target.read_bytes(),target
  result.append({'path':str(target.relative_to(root)),'sha256':hashlib.sha256(p.read_bytes()).hexdigest(),'status':'BYTE-IDENTICAL'})
assert (root/'architecture-release.json').read_bytes()==release
(root/'docs/archimate/processing/publishing/1.0.8/final-layout-107-reproduction.json').write_text(json.dumps({'status':'PASS','fixture':str(fixture.relative_to(root)),'outputs':result},indent=2)+'\n')
print('PASS: final framework independently reproduces all 12 Release 1.0.7 core outputs byte-for-byte; current release authority unchanged.')
