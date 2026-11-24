#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Purpose-selected verification of this pending candidate; no live publication.

Run with the repository's declared EA Python dependencies. This is iteration
evidence, not a new lifecycle command or architectural source. Maintained
projectors generate in isolation at the same relative candidate destinations.
"""
import sys
from pathlib import Path
import json
import shutil
import tempfile
from urllib.parse import urlsplit, unquote

ROOT=Path(__file__).resolve().parents[6]
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
import publish_continuum as reader
import publish_continuum_rad as rad
import continuum_manifest as manifest
import markdown
from lxml import html

REL=Path('enterprise-architecture/design/evidence/1.1.23/001/after/mnemosyne-incorporation/candidate')
OUT=ROOT/REL.parent/'independent-reproduction.json'

def main():
    _,elements=c.load(ROOT)
    before={str(p.relative_to(ROOT)):c.sha(p.read_bytes()) for m,b,p in elements.values()}
    hashes={}
    with tempfile.TemporaryDirectory(prefix='harmonia-mnemosyne-independent-') as name:
        stage=Path(name)
        for rel in [c.CONTINUUM,Path('enterprise-architecture/governance'),c.TOOLING,c.DESIGN,rad.SOURCE]:
            shutil.copytree(ROOT/rel,stage/rel,ignore=shutil.ignore_patterns('published','__pycache__','.venv'))
        shutil.copyfile(ROOT/'architecture-release.json',stage/'architecture-release.json')
        for m,b,p in elements.values():
            for source in m['provenance']:
                if source.get('retainedAt'):
                    rel=Path(source['retainedAt']);(stage/rel).parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(ROOT/rel,stage/rel)
            doc=html.fragment_fromstring(markdown.markdown(b,extensions=['tables','fenced_code']),create_parent='div')
            for node in doc.iter('a'):
                url=urlsplit(node.get('href',''))
                if url.scheme or not url.path:continue
                target=(p.parent/unquote(url.path)).resolve()
                if target.is_relative_to(ROOT/'enterprise-architecture/design') and target.is_file():
                    rel=target.relative_to(ROOT);(stage/rel).parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(target,stage/rel)
        print('Isolated maintained sources and declared evidence prepared; no docs/scripts/Git.',flush=True)
        payload,validation=c.project(stage)
        assert payload==(ROOT/REL/'reader/Harmonia-Architecture.xml').read_bytes()
        hashes['reader/Harmonia-Architecture.xml']=c.sha(payload)
        print('PASS isolated full XML projection',flush=True)
        for part,publisher in [('reader',reader),('narrative',rad)]:
            generated=publisher.outputs(stage,stage/REL/part)
            for filename,content in generated.items():
                assert content==(ROOT/REL/part/filename).read_bytes(),(part,filename)
                hashes[part+'/'+filename]=c.sha(content)
            print('PASS isolated '+part+' '+str(len(generated))+' artifacts',flush=True)
        produced=manifest.render(stage)
        assert produced==(ROOT/c.MANIFEST).read_bytes()
        hashes[str(c.MANIFEST)]=c.sha(produced)
        assert before=={str(p.relative_to(stage)):c.sha(p.read_bytes()) for m,b,p in c.load(stage)[1].values()}
        for forbidden in ['docs','scripts','.git']:assert not (stage/forbidden).exists()
    assert before=={str(p.relative_to(ROOT)):c.sha(p.read_bytes()) for m,b,p in c.load(ROOT)[1].values()}
    result={'status':'PASS','canonicalSources':len(elements),'identicalArtifacts':hashes,'independentInputs':'Current canonical Continuum, governance, tooling, maintained presentation and declared retained provenance; installed dependencies; unchanged root release authority.','absentInputs':['docs','scripts','.git','historical processing workspaces','local interaction logs'],'allocatedIdentities':0,'liveCanonicalSourceMutations':0,'acceptedPublicationWrites':0,'rootRelease':'1.1.22; candidate for proposed 1.1.23 transition, no allocation','limits':'Projection/publication-preparation reproducibility only; mutation tests, actual Archi transport and human acceptance are separate evidence.'}
    OUT.write_bytes(c.encoded(result));print('PASS independent reproduction',flush=True)

if __name__=='__main__':main()
