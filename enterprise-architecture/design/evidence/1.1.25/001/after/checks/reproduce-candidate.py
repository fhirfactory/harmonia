import hashlib
import json
import shutil
import sys
import tempfile
from pathlib import Path

root = Path.cwd()
sys.path.insert(0, str(root / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_manifest as manifest
import publish_continuum as pub
import publish_continuum_rad as rad

after = root / 'enterprise-architecture/design/evidence/1.1.25/001/after'
relative = after.relative_to(root) / 'candidate-v2'
hashes = {}
_, elements = c.load(root)
with tempfile.TemporaryDirectory(prefix='harmonia-iu-final002-reproduction-') as name:
    stage = Path(name)
    for rel in [c.CONTINUUM, c.TOOLING, Path('enterprise-architecture/governance'), c.DESIGN, rad.SOURCE]:
        shutil.copytree(root / rel, stage / rel, ignore=shutil.ignore_patterns('published', '__pycache__', '.venv'))
    shutil.copyfile(root / 'architecture-release.json', stage / 'architecture-release.json')
    for metadata, body, path in elements.values():
        for origin in metadata['provenance']:
            if origin.get('retainedAt'):
                rel = Path(origin['retainedAt'])
                (stage / rel).parent.mkdir(parents=True, exist_ok=True)
                shutil.copyfile(root / rel, stage / rel)
    print('Maintained input stage ready; no docs/, scripts/ or Git.', flush=True)
    c.validate(stage)
    expected_manifest = manifest.render(stage)
    if isinstance(expected_manifest, str):
        expected_manifest = expected_manifest.encode()
    assert expected_manifest == (root / c.MANIFEST).read_bytes(), 'Root Manifest differs'
    hashes[str(c.MANIFEST)] = hashlib.sha256(expected_manifest).hexdigest()
    print('Manifest reproduced', flush=True)
    for label, generator in [('reader', pub.outputs), ('narrative', rad.outputs)]:
        output = generator(stage, stage / relative / label)
        for artifact, content in output.items():
            if isinstance(content, str):
                content = content.encode()
            live = root / relative / label / artifact
            assert live.read_bytes() == content, str(live)
            hashes[str(relative / label / artifact)] = hashlib.sha256(content).hexdigest()
        print(label + ' reproduced: ' + str(len(output)) + ' artifacts', flush=True)
    for forbidden in ['docs', 'scripts', '.git']:
        assert not (stage / forbidden).exists()
    assert all(c.sha(p.read_bytes()) == c.sha((stage / p.relative_to(root)).read_bytes()) for m, b, p in elements.values())
report = {'status': 'PASS', 'identicalArtifacts': hashes, 'canonicalSources': len(elements), 'liveIdentityAllocations': 0, 'legacyRadPresent': False, 'scriptsPresent': False, 'gitPresent': False, 'limits': 'Maintained-input generation reproduction; not actual Archi transport, Java verification or human acceptance.'}
(after / 'reproducibility.json').write_text(json.dumps(report, indent=2) + '\n')
print('PASS: ' + str(len(hashes)) + ' identical artifacts', flush=True)
