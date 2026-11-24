#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only preservation and isolated candidate reproduction for this iteration."""
import io
import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tarfile
import tempfile

ROOT = Path(__file__).resolve().parents[6]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_navigation as nav
import publish_continuum_rad as rad

BASE = Path(__file__).resolve().parent
HEAD = '8204fd49a4d380c068a03acdf30c17bb06e5d953'


def preservation():
    _, elements = c.load(ROOT)
    archive = subprocess.check_output(['git', 'archive', HEAD,
                                      'enterprise-architecture/continuum'], cwd=ROOT)
    old = {}
    with tarfile.open(fileobj=io.BytesIO(archive)) as source:
        for item in source:
            if item.isfile() and item.name.endswith('.md') and not item.name.endswith('/MANIFEST.md'):
                m, body = c.decode(source.extractfile(item).read().decode())
                old[m['canonicalId']] = (m, body)
    assert set(old) <= set(elements)
    old_rel = {r['identifier']: r for m, _ in old.values() for r in m['relationships']}
    new_rel = {r['identifier']: r for m, _, _ in elements.values() for r in m['relationships']}
    assert all(new_rel[k] == r for k, r in old_rel.items())
    before = json.loads((BASE.parent / 'before/baseline.json').read_text())
    changed = [p for p, h in before['sha256'].items() if c.sha((ROOT / p).read_bytes()) != h]
    assert all(p.startswith('enterprise-architecture/continuum/') for p in changed), changed
    new_ids = sorted(set(elements) - set(old))
    assert set(new_ids) == {x['canonicalId'] for x in json.loads((BASE / 'identity-allocation.json').read_text())}
    ids = json.loads((BASE / 'selected-identities.json').read_text())
    services = {ids[k] for k in ('reader', 'writer', 'search', 'summary_reader', 'summary_writer')}
    assert {elements[k][0]['displayName'] for k in services} == {
        'GovernedReader', 'GovernedWriter', 'GovernedSummarySearch',
        'GovernedSummaryReader', 'GovernedSummaryWriter'}
    assert not any(m['archimateType'] == 'ApplicationService' and m['displayName'] == 'GovernedSearch'
                   for m, _, _ in elements.values())
    # Definitions in Strategy and Business are unchanged; Motivation is additive only.
    for k, (m, body) in old.items():
        if m['archimateType'] in ('Capability', 'BusinessObject', 'BusinessProcess', 'BusinessFunction'):
            assert (m, body) == elements[k][:2], k
        if '/motivation/' in str(elements[k][2]):
            after_metadata, after_body, _ = elements[k]
            assert body == after_body, k
            assert {a: b for a, b in m.items() if a != 'relationships'} == {
                a: b for a, b in after_metadata.items() if a != 'relationships'}, k
    # All authored Views live in the maintained projection template, already pinned.
    template = c.secure_xml(ROOT / c.DESIGN / 'projection-template.xml')
    views = template.findall('.//a:views/a:diagrams/a:view', c.N)
    assert len(views) == 1304
    report = {
        'status': 'PASS', 'entryHead': HEAD, 'originalCanonicalIdsPreserved': len(old),
        'originalRelationshipRecordsPreservedExactly': len(old_rel),
        'addedCanonicalIds': new_ids, 'addedRelationships': sorted(set(new_rel) - set(old_rel)),
        'pinnedFiles': len(before['sha256']), 'changedPinnedFiles': changed,
        'approvedSourceFilesAcceptedPublicationsRegistersAndRelease': 'UNCHANGED',
        'strategyAndBusinessDefinitions': 'UNCHANGED',
        'motivationDefinitionsAndMetadataExceptAdditiveRelationships': 'UNCHANGED',
        'authoredViewsPreserved': len(views),
        'soleSearchIdentity': ids['search'], 'exposedContractIds': sorted(services),
        'limit': 'Independent source/record comparison; semantic approval and visual fidelity are not asserted.'}
    (BASE / 'preservation.json').write_bytes(c.encoded(report))
    print('preservation PASS', flush=True)


def reproduce():
    _, elements = c.load(ROOT)
    hashes = {str(p.relative_to(ROOT)): c.sha(p.read_bytes()) for _, _, p in elements.values()}
    relative = BASE.relative_to(ROOT) / 'candidate'
    steps = []
    with tempfile.TemporaryDirectory(prefix='harmonia-124-reproduce-') as tmp:
        stage = Path(tmp)
        for rel in (c.CONTINUUM, c.TOOLING, Path('enterprise-architecture/governance'), c.DESIGN, rad.SOURCE):
            shutil.copytree(ROOT / rel, stage / rel,
                            ignore=shutil.ignore_patterns('__pycache__', '.venv', 'published'))
        shutil.copyfile(ROOT / 'architecture-release.json', stage / 'architecture-release.json')
        retained = set()
        for metadata, _, _ in elements.values():
            for source in metadata['provenance']:
                if source.get('retainedAt'):
                    rel = Path(source['retainedAt'])
                    assert rel.is_relative_to('enterprise-architecture/design')
                    (stage / rel).parent.mkdir(parents=True, exist_ok=True)
                    shutil.copyfile(ROOT / rel, stage / rel)
                    retained.add(str(rel))
        reader, narrative = relative / 'reader', relative / 'narrative'
        # Existing publishers with evidence-local destinations, never accepted publication.
        commands = [
            ['architecture_continuum.py', 'validate'],
            ['continuum_manifest.py'],
            ['architecture_continuum.py', 'project', '--output', str(reader / 'Harmonia-Architecture.xml')],
            ['publish_continuum.py', '--output', str(reader)],
            ['publish_continuum_rad.py', '--output', str(narrative)],
        ]
        env = {**os.environ, 'PYTHONDONTWRITEBYTECODE': '1', 'PYTHONPATH': ''}
        for cmd in commands:
            result = subprocess.run([sys.executable, '-B', str(c.TOOLING / cmd[0]), *cmd[1:]],
                                    cwd=stage, env=env, capture_output=True, text=True, timeout=100)
            steps.append({'command': cmd, 'exitCode': result.returncode,
                          'output': result.stdout + result.stderr})
            assert result.returncode == 0, steps[-1]
            print('reproduced ' + cmd[0], flush=True)
        compared = {}
        for live in sorted((BASE / 'candidate').rglob('*')) + [ROOT / c.MANIFEST]:
            if not live.is_file():
                continue
            rel = live.relative_to(ROOT)
            assert live.read_bytes() == (stage / rel).read_bytes(), str(rel)
            compared[str(rel)] = c.sha(live.read_bytes())
        assert hashes == {str(p.relative_to(stage)): c.sha(p.read_bytes())
                          for _, _, p in c.load(stage)[1].values()}
        assert not any((stage / p).exists() for p in ('docs', 'scripts', '.git', '.junie'))
    assert hashes == {str(p.relative_to(ROOT)): c.sha(p.read_bytes())
                      for _, _, p in c.load(ROOT)[1].values()}
    (BASE / 'reproducibility.json').write_bytes(c.encoded({
        'status': 'PASS', 'canonicalSources': len(elements), 'steps': steps,
        'identicalArtifacts': compared, 'canonicalSourcesUnmodified': True,
        'rootRelease': '1.1.23 — pending candidate, no allocation',
        'inputs': 'Maintained canonical Markdown/navigation, EA governance/tooling, Archi projection configuration, narrative configuration, explicitly declared retained provenance and root release; installed Python dependencies.',
        'declaredRetainedProvenance': sorted(retained),
        'excludedInputs': ['docs', 'scripts', '.git', '.junie', 'historical publications', 'undeclared historical processing'],
        'limits': 'Reproduction of candidate XML/reader/narrative/Manifest; transport, PDF compilation and human acceptance are separate.'}))
    print('reproducibility PASS', flush=True)


if __name__ == '__main__':
    preservation()
    reproduce()
