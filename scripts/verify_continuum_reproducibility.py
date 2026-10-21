#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Prove routine generation with only maintained inputs in an isolated checkout."""
import argparse
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import architecture_continuum as c


def verify(root=c.ROOT):
    _,elements=c.load(root)
    version=c.read_json(root/'architecture-release.json')['version']
    relative=c.PUBLICATION/version
    before={str(p.relative_to(root)):c.sha(p.read_bytes()) for _,_,p in elements.values()}
    with tempfile.TemporaryDirectory(prefix='harmonia-maintained-inputs-') as tmp:
        stage=Path(tmp)
        for _,_,path in elements.values():
            dest=stage/path.relative_to(root);dest.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(path,dest)
        for rel in (c.CONFIG,c.DESIGN,Path('docs/archimate/framework/schemas')):
            shutil.copytree(root/rel,stage/rel)
        code=Path('docs/archimate/framework/tools');(stage/code).mkdir(parents=True)
        for path in (root/code).glob('*.py'):shutil.copyfile(path,stage/code/path.name)
        for rel in ('architecture-release.json','docs/archimate/framework/historical-locations.json',
                    'scripts/architecture_continuum.py','scripts/continuum_manifest.py','scripts/publish_continuum.py','scripts/architecture_release.py'):
            dest=stage/rel;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(root/rel,dest)
        env={**os.environ,'PYTHONDONTWRITEBYTECODE':'1'}
        commands=[['python3','scripts/continuum_manifest.py'],
                  ['python3','scripts/architecture_continuum.py','project','--output',str(relative/'Harmonia-Architecture.xml')],
                  ['python3','scripts/publish_continuum.py','--output',str(relative)]]
        for command in commands:
            result=subprocess.run(command,cwd=stage,env=env,capture_output=True,text=True,timeout=90)
            c.require(result.returncode==0,'Isolated generation failed: '+result.stderr)
        hashes={}
        for name in ('Harmonia-Architecture.xml','continuum.html','README.md','continuum-catalogue.tex','publication.json'):
            actual=(stage/relative/name).read_bytes();expected=(root/relative/name).read_bytes()
            c.require(actual==expected,'Isolated generation differs: '+name);hashes[name]=c.sha(actual)
        actual=(stage/c.MANIFEST).read_bytes()
        c.require(actual==(root/c.MANIFEST).read_bytes(),'Isolated generation differs: '+str(c.MANIFEST))
        hashes[str(c.MANIFEST)]=c.sha(actual)
        c.require(not (stage/'docs/markdown').exists() and not (stage/'docs/archimate/processing').exists(),'Historical source unexpectedly included')
    c.require(before=={str(p.relative_to(root)):c.sha(p.read_bytes()) for _,_,p in elements.values()},'Verification modified canonical sources')
    return {'status':'PASS','canonicalSources':len(elements),'identicalArtifacts':hashes,
            'historicalCapturesPresent':False,'historicalProcessingPresent':False,'latexSourceCopiesPresent':False,
            'sessionLogsPresent':False,'allocatedIdentities':0,
            'inputs':'Canonical Markdown, maintained configuration/presentation, Python sources, offline schemas, historical-location configuration (no historical files), root release. Installed Python dependencies: lxml, jsonschema, markdown.'}

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--report',type=Path,required=True);a=p.parse_args()
    report=verify();a.report.parent.mkdir(parents=True,exist_ok=True);a.report.write_bytes(c.encoded(report));print(report['status'])
