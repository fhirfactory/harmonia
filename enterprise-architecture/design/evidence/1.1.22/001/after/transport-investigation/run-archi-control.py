#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded, isolated Archi control; never changes the input or existing outputs."""
import argparse
import gzip
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import tempfile
import time

p = argparse.ArgumentParser(description=__doc__)
p.add_argument('--input', type=Path, required=True)
p.add_argument('--output', type=Path, required=True)
a = p.parse_args()
source = a.input.resolve()
a.output.mkdir(parents=True, exist_ok=False)
archi = Path('/home/hunterm/Development/Tools/Archi')
sha = lambda b: hashlib.sha256(b).hexdigest()
report = dict(input=str(source), inputSha256=sha(source.read_bytes()),
              operator='Codex, user-authorised 1.1.22/001 transport investigation',
              timeoutSeconds=120, installation=str(archi),
              limits='Read-only control input; isolated temporary configuration/workspace. No architectural acceptance.')
with tempfile.TemporaryDirectory(prefix='harmonia-transport-investigation-') as tmp:
    work = Path(tmp)
    shutil.copytree(archi / 'configuration', work / 'configuration')
    transport_source=source
    if source.suffix=='.gz':
        transport_source=work/'input.xml'
        transport_source.write_bytes(gzip.decompress(source.read_bytes()))
    report['inputXmlSha256']=sha(transport_source.read_bytes())
    cmd = [str(archi / 'Archi'), '-nosplash', '-consoleLog', '-configuration', str(work / 'configuration'),
           '-data', str(work / 'workspace'), '-eclipse.keyring', str(work / 'secure_storage'),
           '-application', 'com.archimatetool.commandline.app', '-a', '--xmlexchange.import', str(transport_source),
           '--saveModel', str(work / 'native.archimate'), '--xmlexchange.export', str(work / 'returned.xml'),
           '--xmlexchange.exportFolders', '--xmlexchange.exportLang', 'en']
    report['command'] = cmd
    start = time.monotonic()
    with (a.output / 'execution.txt').open('wb') as log:
        try:
            run = subprocess.run(cmd, stdout=log, stderr=subprocess.STDOUT, timeout=120)
            report.update(exitCode=run.returncode, outcome='PASS' if run.returncode == 0 else 'FAIL')
        except subprocess.TimeoutExpired:
            report.update(exitCode=None, outcome='TIMEOUT')
    report['elapsedSeconds'] = round(time.monotonic() - start, 3)
    for name in ('native.archimate', 'returned.xml'):
        f = work / name
        if f.is_file():
            data = f.read_bytes()
            (a.output / (name + '.gz')).write_bytes(gzip.compress(data, mtime=0))
            report[name] = dict(sha256=sha(data), bytes=len(data))
assert sha(source.read_bytes()) == report['inputSha256']
(a.output / 'execution.json').write_text(json.dumps(report, indent=2) + '\n')
print(json.dumps(report))
raise SystemExit(0 if report['outcome'] == 'PASS' else 1)
