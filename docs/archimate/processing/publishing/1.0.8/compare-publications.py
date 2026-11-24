from pathlib import Path
import json,sys,hashlib,difflib
sys.path.insert(0,'docs/archimate/framework/tools')
import synchronise_baseline as s
import report_baseline as report
import verify_workspace as v
E=Path('docs/archimate/processing/publishing/1.0.8');sha=lambda b:hashlib.sha256(b).hexdigest()
outputs,_,_=s.publish();assert len(outputs)==12
records=[]
for p in sorted(outputs):
 if p==s.metadata.model_path():
  prior=v.BASELINE;kind='RELEASE METADATA ONLY';details='Exactly one model-level baseline-architecture-release value: 1.0.7 → 1.0.8. Restoring it reproduces the accepted byte stream exactly.'
 elif p==s.metadata.validation_path():
  prior=s.metadata.ESTABLISHMENT_EVIDENCE/'canonical-identity-validation.json';kind='RELEASE METADATA ONLY';details='Exactly three changed JSON leaves: architecture_release.version, architecture_release.previous_version, output_sha256. The hash follows the explained XML release-value change.'
 else:prior=p;kind='BYTE-IDENTICAL';details='Unchanged textual/JSON governed projection.'
 before=prior.read_bytes();after=p.read_bytes()
 if kind=='BYTE-IDENTICAL':assert before==after,p
 if p==s.metadata.validation_path():
  differences=[]
  def compare(a,b,path=''):
   if isinstance(a,dict) and isinstance(b,dict):
    assert a.keys()==b.keys()
    for k in a:compare(a[k],b[k],path+'/'+k)
   elif a!=b:differences.append(dict(path=path,before=a,after=b))
  compare(json.loads(before),json.loads(after))
  assert {d['path'] for d in differences}=={'/architecture_release/version','/architecture_release/previous_version','/output_sha256'},differences
  (E/'validation-metadata-differences.json').write_text(json.dumps(dict(classification='release metadata only',differences=differences),indent=2)+'\n')
 records.append(dict(before=str(prior),after=str(p),before_sha256=sha(before),after_sha256=sha(after),classification=kind,explanation=details))
diff=[]
for before,after in [(s.metadata.ESTABLISHMENT_EVIDENCE/'canonical-identity-establishment.md',report.REPORT),(s.metadata.ESTABLISHMENT_EVIDENCE/'canonical-identity-archi-guide.md',report.GUIDE)]:
 a,b=before.read_bytes(),after.read_bytes()
 records.append(dict(before=str(before),after=str(after),before_sha256=sha(a),after_sha256=sha(b),classification='RELEASE-SPECIFIC SUPPORT DOCUMENTATION',explanation='New 1.0.8 workspace/publication report or guide describes the policy release, final paths, exact preservation proof and current completed checks. Original 1.0.7 document remains frozen. Support documentation is processing evidence, outside the exchange package. Every changed line is retained in publication-support-documents.diff.'))
 diff.extend(difflib.unified_diff(a.decode().splitlines(keepends=True),b.decode().splitlines(keepends=True),fromfile=str(before),tofile=str(after)))
(E/'publication-support-documents.diff').write_text(''.join(diff))
assert len(records)==14
(E/'publication-comparison.json').write_text(json.dumps(dict(status='PASS',comparison=v.verify(),outputs=records),indent=2)+'\n')
print('PASS: all 14 publication outputs accounted for: 10 byte-identical, XML release value, validation 3 metadata leaves, 2 new release-specific support documents.')
