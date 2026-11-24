#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Record an actually executed bounded check, including unsuccessful attempts."""
import hashlib
import json
import os
from pathlib import Path
import signal
import subprocess
import sys
import time

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').is_file())
name, seconds, *command = sys.argv[1:]
log = HERE / 'validation' / (name + '.log')
record = log.with_suffix('.json')
if record.exists():
    raise ValueError('Keep earlier attempts: choose a distinct check name')
start = time.monotonic()
with log.open('wb') as stream:
    process = subprocess.Popen(command, cwd=ROOT, stdout=stream, stderr=subprocess.STDOUT, start_new_session=True)
    try:
        code = process.wait(timeout=float(seconds))
        status = 'PASS' if code == 0 else 'FAIL'
    except subprocess.TimeoutExpired:
        status = 'TIMEOUT'
        os.killpg(process.pid, signal.SIGTERM)
        try:
            process.wait(timeout=5)
        except subprocess.TimeoutExpired:
            os.killpg(process.pid, signal.SIGKILL)
            process.wait(timeout=5)
        code = process.returncode
result = dict(status=status, command=command, timeout_seconds=float(seconds),
              elapsed_seconds=round(time.monotonic()-start, 3), exit_code=code,
              log=str(log.relative_to(ROOT)), log_sha256=hashlib.sha256(log.read_bytes()).hexdigest())
record.write_text(json.dumps(result, indent=2)+'\n')
print(json.dumps(result), flush=True)
sys.exit(0 if status == 'PASS' else 1)
