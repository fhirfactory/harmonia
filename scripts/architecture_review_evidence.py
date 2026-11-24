#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only layout/event-prefix check for one explicitly selected HCRL iteration.

Checks conventional Markdown Task/Report records, not evidence significance,
event authenticity, task/report semantic correspondence or acceptance. This is
not a retrospective audit or a requirement to create empty evidence packages.
"""
import argparse
from datetime import datetime, timezone
import json
from pathlib import Path
import re
import sys

from architecture_release import version_parts

ROOT = Path(__file__).resolve().parents[1]
ROLES = {"ai-agent-tasks": ("before", "Task-Issued-At"),
         "ai-agent-reports": ("after", "Report-Produced-At")}


def check_event(path, label):
    """Return unresolved provenance, or verify the recorded time and UTC prefix."""
    text = path.read_text(encoding="utf-8")
    match = re.search(r"^\*{0,2}" + re.escape(label) + r":\s*([^\s*]+)", text, re.M)
    if not match:
        raise ValueError(f"{path}: missing conventional Markdown {label} field")
    value = match[1].rstrip(".")
    if value.upper().startswith("UNRESOLVED"):
        return f"{path}: original {label} and filename prefix require Solution Architect treatment"
    if not re.fullmatch(r"\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(?::\d{2}(?:\.\d+)?)?(?:Z|[+-]\d{2}:\d{2})", value):
        raise ValueError(f"{path}: {label} must record ISO 8601 with Z or explicit offset")
    try:
        event = datetime.fromisoformat(value.replace("Z", "+00:00"))
    except ValueError as exc:
        raise ValueError(f"{path}: invalid {label}: {value}") from exc
    prefix = event.astimezone(timezone.utc).strftime("%Y%m%dT%H%M%SZ-")
    if not path.name.startswith(prefix) or path.name == prefix + ".md":
        raise ValueError(f"{path}: filename must begin {prefix}<descriptive-name>.md")
    return None


def check_iteration(root, version, iteration):
    version_parts(version)
    if not re.fullmatch(r"\d{3,}", iteration) or int(iteration) == 0:
        raise ValueError("Use the corresponding positive HCRL iteration, such as 004")
    design = Path(root) / "enterprise-architecture/design"
    hcrls = [design / kind / version / f"hcrl-{version}-{iteration}.{state}.md"
             for kind, state in (("review", "pending"), ("register", "complete"))]
    if not any(path.is_file() for path in hcrls):
        raise ValueError(f"No corresponding HCRL for {version}-{iteration}")
    release = design / "evidence" / version
    if any((release / side).exists() for side in ("before", "after", *ROLES)):
        raise ValueError(f"{release}: evidence requires an HCRL iteration before before/after")
    boundary = release / iteration
    errors, unresolved, records = [], [], []
    for child in sorted(boundary.iterdir()) if boundary.exists() else []:
        if child.name not in ("before", "after") or not child.is_dir():
            errors.append(f"{child}: iteration evidence belongs under before/ or after/")
    for path in sorted(boundary.rglob("*")):
        if path.name in ROLES:
            side, label = ROLES[path.name]
            if path != boundary / side / path.name or not path.is_dir():
                errors.append(f"{path}: {path.name} belongs directly under {side}/")
                continue
            for record in sorted(path.rglob("*")):
                if not record.is_file():
                    continue
                if record.suffix != ".md":
                    unresolved.append(f"{record}: this check supports Markdown; inspect other representations separately")
                    continue
                try:
                    issue = check_event(record, label)
                    if issue:
                        unresolved.append(issue)
                    records.append(str(record.relative_to(design)))
                except ValueError as exc:
                    errors.append(str(exc))
    counts = {role: sum(f"/{role}/" in path for path in records) for role in ROLES}
    if bool(counts["ai-agent-tasks"]) != bool(counts["ai-agent-reports"]):
        unresolved.append("Formal Task/Report side is absent; establish actual activity and correspondence before closure")
    return dict(outcome="FAIL" if errors else "UNRESOLVED" if unresolved else "PASS",
                version=version, iteration=iteration, records=records,
                errors=errors, unresolved=unresolved,
                limits="Layout and recorded event/prefix correspondence only; no semantic coverage, authenticity or acceptance claim")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--repository", type=Path, default=ROOT)
    parser.add_argument("--version", required=True)
    parser.add_argument("--iteration", required=True)
    args = parser.parse_args()
    try:
        result = check_iteration(args.repository, args.version, args.iteration)
    except (OSError, ValueError) as exc:
        result = dict(outcome="FAIL", errors=[str(exc)])
    print(json.dumps(result, indent=2))
    return {"PASS": 0, "FAIL": 1, "UNRESOLVED": 2}[result["outcome"]]


if __name__ == "__main__":
    sys.exit(main())
