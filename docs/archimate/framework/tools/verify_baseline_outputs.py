#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independently reproduce and compare the complete current baseline output set."""
import os
from pathlib import Path
import subprocess
import sys
import tempfile

import synchronise_baseline as s
import report_baseline as report


def main():
    outputs, baseline, _ = s.publish()
    paths = sorted([*outputs, report.REPORT, report.GUIDE])
    expected = 9 if getattr(baseline, 'information_release', False) else (15 if hasattr(baseline, 'reconciliation') else 14)
    s.g.require(len(set(paths)) == len(paths) == expected, "Complete generated-output set changed")
    release = (s.ROOT / "architecture-release.json").read_bytes()
    with tempfile.TemporaryDirectory(prefix="harmonia-publication-reproduction-", dir="/tmp") as temp:
        env = {**os.environ, "PYTHONDONTWRITEBYTECODE": "1"}
        for script in ("synchronise_baseline.py", "report_baseline.py"):
            subprocess.run([sys.executable, str(s.BASE / "tools" / script), "--output-directory", temp],
                           check=True, timeout=90, env=env, stdout=subprocess.DEVNULL)
        for path in paths:
            other = Path(temp) / path.relative_to(s.ROOT)
            s.g.require(path.read_bytes() == other.read_bytes(), "Independent regeneration differs: " + str(path.relative_to(s.ROOT)))
    s.g.require(release == (s.ROOT / "architecture-release.json").read_bytes(), "Generation changed release")
    print(f"PASS: all {len(paths)} generated outputs reproduce byte-for-byte in independent processes; release unchanged")
    return 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except (OSError, ValueError, subprocess.SubprocessError) as exc:
        print("INVALID: " + str(exc), file=sys.stderr)
        sys.exit(1)
