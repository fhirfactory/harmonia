from pathlib import Path
import subprocess,time,json
E=Path('docs/archimate/processing/publishing/1.0.8')
commands={
'source_identity':['python3','-B','docs/archimate/framework/tools/governed_metadata.py','validate'],
'ec_bindings':['python3','-B','-c',"import sys,json; sys.path.insert(0,'scripts'); import architecture_identity as i; print(json.dumps(i.verify_bindings(i.read_json(i.DIRECTORY/'registry.json')),indent=2))"],
'identity_registry':['python3','-B','scripts/architecture_identity.py','validate'],
'release':['python3','-B','scripts/architecture_release.py','check'],
'historical_recovery':['python3','-B','docs/archimate/framework/tools/recover_v02.py','--check'],
'migration_comparison':['python3','-B','docs/archimate/framework/tools/verify_workspace.py'],
'publication_check':['python3','-B','docs/archimate/framework/tools/synchronise_baseline.py','--check']}
results={}
for name,command in commands.items():
 start=time.monotonic();log=E/(name+'-final.log')
 with log.open('w') as f:
  run=subprocess.run(['timeout','--signal=TERM','--kill-after=5s','90s',*command],stdout=f,stderr=subprocess.STDOUT)
 status='PASS' if run.returncode==0 else 'TIMEOUT' if run.returncode in (124,137) else 'FAIL'
 results[name]=dict(status=status,command=command,returncode=run.returncode,seconds=round(time.monotonic()-start,3),log=str(log))
 print(name,status,results[name]['seconds'],flush=True)
 if run.returncode: print(log.read_text()[-2500:]);break
(E/'command-results-final.json').write_text(json.dumps(results,indent=2)+'\n')
