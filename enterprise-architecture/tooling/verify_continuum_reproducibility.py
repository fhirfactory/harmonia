#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Exercise current EA operations with Legacy-RAD and scripts absent."""
import argparse
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
from urllib.parse import urlsplit, unquote
import markdown
from lxml import html
import architecture_continuum as c
import publish_continuum_rad as rad
import continuum_navigation as nav


def verify(root=c.ROOT):
    _, elements = c.load(root)
    version = c.read_json(root / 'architecture-release.json')['version']
    relative = c.PUBLICATION / version
    before = {str(p.relative_to(root)): c.sha(p.read_bytes()) for _, _, p in elements.values()}
    with tempfile.TemporaryDirectory(prefix='harmonia-current-ea-') as tmp:
        stage = Path(tmp)
        for _, _, path in elements.values():
            dest = stage / path.relative_to(root)
            dest.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(path, dest)
        shutil.copyfile(root / nav.SOURCE, stage / nav.SOURCE)
        for rel in (Path('enterprise-architecture/governance'), c.TOOLING, c.DESIGN, rad.SOURCE):
            shutil.copytree(root / rel, stage / rel, ignore=shutil.ignore_patterns('__pycache__', '.venv', 'published'))
        shutil.copyfile(root / 'architecture-release.json', stage / 'architecture-release.json')
        # Copy only concrete current EA design targets used by standing link
        # verification. Do not load/copy historical evidence trees or releases.
        design_targets = set()
        for _, body, path in elements.values():
            document = html.fragment_fromstring(markdown.markdown(body, extensions=['tables', 'fenced_code']), create_parent='div')
            for node in document.iter('a'):
                url = urlsplit(node.get('href', ''))
                if url.scheme or not url.path:
                    continue
                target = (path.parent / unquote(url.path)).resolve()
                if target.is_relative_to(root / 'enterprise-architecture/design') and target.is_file():
                    rel = target.relative_to(root)
                    design_targets.add(str(rel))
                    (stage / rel).parent.mkdir(parents=True, exist_ok=True)
                    shutil.copyfile(target, stage / rel)
        # Current review evidence is optional to generation; retain it when
        # canonical metadata explicitly declares it as retained provenance.
        for metadata, _, _ in elements.values():
            for source in metadata['provenance']:
                if source.get('retainedAt'):
                    rel = Path(source['retainedAt'])
                    c.require(rel.is_relative_to('enterprise-architecture/design'),
                              'Current retained evidence is outside EA design: ' + str(rel))
                    (stage / rel).parent.mkdir(parents=True, exist_ok=True)
                    shutil.copyfile(root / rel, stage / rel)
        env = {**os.environ, 'PYTHONDONTWRITEBYTECODE': '1'}
        # Preserve installed dependency paths, never the original EA/script code.
        env['PYTHONPATH'] = os.pathsep.join(p for p in env.get('PYTHONPATH', '').split(os.pathsep)
                                         if p and not Path(p).resolve().is_relative_to(root.resolve()))
        prefix = [sys.executable, '-B']
        code = str(c.TOOLING)
        rad_relative = c.RAD_PUBLICATION / version
        commands = [prefix + [code + '/architecture_continuum.py', 'validate'],
                    prefix + [code + '/continuum_manifest.py'],
                    prefix + [code + '/architecture_continuum.py', 'project', '--output', str(relative / 'Harmonia-Architecture.xml'), '--publish'],
                    prefix + [code + '/publish_continuum.py', '--output', str(relative)],
                    prefix + [code + '/publish_continuum_rad.py', '--output', str(rad_relative)],
                    prefix + [code + '/continuum_manifest.py', '--check'],
                    prefix + [code + '/architecture_continuum.py', 'project', '--output', str(relative / 'Harmonia-Architecture.xml'), '--publish', '--check'],
                    prefix + [code + '/publish_continuum.py', '--output', str(relative), '--check'],
                    prefix + [code + '/publish_continuum_rad.py', '--output', str(rad_relative), '--check'],
                    prefix + [code + '/verify_continuum_links.py', '--report', 'links.json'],
                    prefix + [code + '/continuum_workbench.py', 'review', '--baseline', str(relative / 'Harmonia-Architecture.xml'), '--submitted', str(relative / 'Harmonia-Architecture.xml'), '--output', 'review.json'],
                    prefix + [code + '/exchange_compare.py', 'compare', '--baseline', str(relative / 'Harmonia-Architecture.xml'), '--submitted', str(relative / 'Harmonia-Architecture.xml'), '--report', 'raw.json'],
                    prefix + ['-m', 'unittest', 'discover', '-s', code + '/tests', '-v']]
        steps = []
        for command in commands:
            limit = 420 if 'unittest' in command else 90
            print('Running: ' + ' '.join(command[2:]), flush=True)
            try:
                result = subprocess.run(command, cwd=stage, env=env, capture_output='unittest' not in command, text=True, timeout=limit)
            except subprocess.TimeoutExpired as exc:
                raise c.ContinuumError('TIMEOUT: isolated operation: ' + ' '.join(command)) from exc
            stdout, stderr = result.stdout or '', result.stderr or ''
            diagnostic = (stage / 'links.json').read_text() if result.returncode and (stage / 'links.json').exists() else ''
            c.require(result.returncode == 0, 'Isolated operation failed: ' + ' '.join(command) + '\n' + stdout[-1500:] + stderr[-4000:] + diagnostic)
            steps.append({'command': command[2:], 'status': 'PASS', 'summary': (stdout + stderr)[-400:]})
            print('PASS: ' + command[2], flush=True)
        hashes = {}
        published = c.ARCHIMATE_PUBLICATION / version / 'Harmonia-Architecture.xml'
        actual = (stage / published).read_bytes()
        c.require(actual == (root / published).read_bytes(), 'Isolated ArchiMate publication differs')
        c.require(actual == (stage / relative / 'Harmonia-Architecture.xml').read_bytes(), 'Published/Manifest Exchange drift')
        hashes[str(published)] = c.sha(actual)
        for name in ('Harmonia-Architecture.xml', 'continuum.html', 'README.md', 'continuum-catalogue.tex', 'navigation.json', 'publication.json'):
            actual = (stage / relative / name).read_bytes()
            c.require(actual == (root / relative / name).read_bytes(), 'Isolated generation differs: ' + name)
            hashes[name] = c.sha(actual)
        actual = (stage / c.MANIFEST).read_bytes()
        c.require(actual == (root / c.MANIFEST).read_bytes(), 'Isolated manifest differs')
        hashes[str(c.MANIFEST)] = c.sha(actual)
        # Compare every derived narrative source/correspondence artifact; PDF
        # compilation/inspection is a separate publication check.
        for name in rad.outputs(stage, stage / rad_relative):
            actual = (stage / rad_relative / name).read_bytes()
            c.require(actual == (root / rad_relative / name).read_bytes(), 'Isolated RAD differs: ' + name)
            hashes[str(rad_relative / name)] = c.sha(actual)
        for forbidden in ('docs', 'scripts', '.git'):
            c.require(not (stage / forbidden).exists(), 'Forbidden isolated input: ' + forbidden)
        c.require(before == {str(p.relative_to(stage)): c.sha(p.read_bytes()) for _, _, p in c.load(stage)[1].values()}, 'Isolated operations modified canonical sources')
    c.require(before == {str(p.relative_to(root)): c.sha(p.read_bytes()) for _, _, p in c.load(root)[1].values()}, 'Verification modified live canonical sources')
    return {'status': 'PASS', 'canonicalSources': len(elements), 'identicalArtifacts': hashes,
            'steps': steps, 'legacyRadPresent': False, 'scriptsPresent': False,
            'historicalConfigurationPresent': False, 'allocatedLiveIdentities': 0,
            'currentDesignLinkTargets': sorted(design_targets),
            'inputs': 'EA continuum, governance, design presentation/declared retained evidence, tooling; root release and installed Python dependencies',
            'limits': 'Synthetic creation/application and transport fixtures; no actual Archi execution, visual inspection or architectural acceptance asserted.'}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--report', type=Path, required=True)
    args = parser.parse_args()
    report = verify()
    args.report.parent.mkdir(parents=True, exist_ok=True)
    args.report.write_bytes(c.encoded(report))
    print(report['status'])
