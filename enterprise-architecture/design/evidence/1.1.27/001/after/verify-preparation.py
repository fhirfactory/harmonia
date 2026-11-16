#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded document/source checks for this backlog-preparation candidate only.

This evidence-local check supplies no architectural or implementation acceptance.
It reads the explicit candidate and originating inventory without allocating a
release or repairing unavailable provenance events.
"""

import hashlib
import json
import re
import subprocess
from datetime import datetime, timezone
from pathlib import Path
from urllib.parse import unquote


ROOT = Path(__file__).resolve().parents[6]
AFTER = Path(__file__).resolve().parent
BOUNDARY = AFTER.parent
ITEM = ROOT / "docs/backlog/calliope-oo-01-topic-models.md"
HCRL = ROOT / "enterprise-architecture/design/review/1.1.27/hcrl-1.1.27-001.pending.md"


def sha(path):
    """Hash retained bytes to detect drift, without implying their acceptance."""
    return hashlib.sha256(path.read_bytes()).hexdigest()


def run():
    """Return structural results separately from unresolved event provenance."""
    errors = []
    source = json.loads((BOUNDARY / "before/source-state.json").read_text())
    text = ITEM.read_text()
    expected = ["Overview", "Context", "Associated Requirements",
                "Expected / Associated Activity", "Expected Outcomes", "Acceptance Criteria"]
    actual = re.findall(r"^## (.+)$", text, re.M)
    if actual != expected:
        errors.append({"sections": actual, "expected": expected})
    synopses = ["High-Level Synopsis of Effort", "High-Level Synopsis of Activities",
                "High-Level Synopsis of Outcome", "High-Level Synopsis of Acceptance Criteria"]
    if re.findall(r"^### (.+)$", text, re.M) != synopses:
        errors.append("Overview synopsis mismatch")
    for metadata in ["**Backlog Item ID:** BL-CALLIOPE-OO-01",
                     "**Backlog Item Revision:** 1"]:
        if metadata not in text:
            errors.append("Missing metadata: " + metadata)
    if not text.startswith("<!-- Copyright") or "BL-CALLIOPE-OO-01" not in re.search(r"^# (.+)$", text, re.M)[1]:
        errors.append("Missing copyright or identifying title")
    if len(re.findall(r"^\| AC-\d+ \|", text, re.M)) != 7:
        errors.append("Acceptance-criteria table mismatch")

    paths = [ITEM, HCRL, AFTER / "engineering-assessment.md"]
    paths += sorted((BOUNDARY / "before/ai-agent-tasks").glob("*.md"))
    paths += sorted((AFTER / "ai-agent-reports").glob("*.md"))
    links = 0
    for path in paths:
        for raw in re.findall(r"\]\(([^)]+)\)", path.read_text()):
            raw = raw.strip("<>")
            if re.match(r"^[A-Za-z][A-Za-z0-9+.-]*:", raw):
                continue
            name, _, fragment = raw.partition("#")
            target = (path.parent / unquote(name)).resolve() if name else path
            links += 1
            if not target.is_file():
                errors.append(f"{path.relative_to(ROOT)}: missing {raw}")
            elif fragment and target.suffix == ".md":
                target_text = target.read_text()
                explicit = re.findall(r'<a\s+id="([^"]+)"', target_text)
                headings = re.findall(r"^#{1,6} (.+)$", target_text, re.M)
                anchors = explicit + [re.sub(r"[^\w\- ]", "", h.lower()).replace(" ", "-") for h in headings]
                if unquote(fragment) not in anchors:
                    errors.append(f"{path.relative_to(ROOT)}: unresolved anchor {raw}")

    for entry in source["productionFiles"]:
        if sha(ROOT / entry["path"]) != entry["sha256"]:
            errors.append("Production drift: " + entry["path"])
    if sha(BOUNDARY / "before/junie-phase1-review.source.md") != source["junieSource"]["excerptSha256"]:
        errors.append("Retained Junie excerpt changed")
    if json.loads((ROOT / "architecture-release.json").read_text()) != source["architectureRelease"]:
        errors.append("Release authority changed")
    head = subprocess.check_output(["git", "rev-parse", "HEAD"], cwd=ROOT, text=True).strip()
    if head != source["head"]:
        errors.append("HEAD drift")
    tracked = subprocess.check_output(["git", "diff", "HEAD", "--name-only"], cwd=ROOT, text=True).splitlines()
    if tracked:
        errors.append({"unexpectedTrackedChanges": tracked})
    untracked = subprocess.check_output(["git", "ls-files", "--others", "--exclude-standard"], cwd=ROOT, text=True).splitlines()
    allowed = ("docs/backlog/calliope-oo-01-topic-models.md",
               "enterprise-architecture/design/review/1.1.27/",
               "enterprise-architecture/design/evidence/1.1.27/001/")
    unexpected = [p for p in untracked if not any(p == prefix or p.startswith(prefix) for prefix in allowed)]
    if unexpected:
        errors.append({"unexpectedUntrackedChanges": unexpected})
    whitespace = subprocess.run(["git", "diff", "--no-index", "--check", "/dev/null", str(ITEM)],
                                cwd=ROOT, capture_output=True, text=True)
    # --no-index implies --exit-code: 1 alone reports a difference from /dev/null.
    # --check emits diagnostics for whitespace defects; do not mislabel a new file.
    if whitespace.returncode not in (0, 1) or whitespace.stdout or whitespace.stderr:
        errors.append({"backlogWhitespace": whitespace.stdout + whitespace.stderr})

    semantic = HCRL.read_text()
    table_ids = re.findall(r"^\| \[DR-(\d+)\]", semantic, re.M)
    section_ids = re.findall(r"^## DR-(\d+) —", semantic, re.M)
    if table_ids != ["1", "2", "3", "4"] or section_ids != table_ids:
        errors.append("HCRL Decision Review correspondence mismatch")
    if semantic.count("Minutiae-Accepted:") != 1 or "Status: PENDING" not in semantic:
        errors.append("HCRL pending/minutiae structure mismatch")

    return {
        "checkedAt": datetime.now(timezone.utc).isoformat(),
        "meaning": "Current validation observation, not instruction issuance or acceptance",
        "command": "python3 -B enterprise-architecture/design/evidence/1.1.27/001/after/verify-preparation.py",
        "structuralOutcome": "FAIL" if errors else "PASS",
        "errors": errors,
        "candidate": {"item": str(ITEM.relative_to(ROOT)), "id": "BL-CALLIOPE-OO-01",
                      "revision": "1", "sha256": sha(ITEM), "baseHead": head},
        "requiredSections": actual,
        "overviewSynopses": synopses,
        "acceptanceCriteria": 7,
        "whitespaceCheck": {"exitCode": whitespace.returncode,
                            "diagnostics": whitespace.stdout + whitespace.stderr,
                            "meaning": "git diff --no-index --check; exit 1 without diagnostics denotes new-file differences, not whitespace failure"},
        "checkerCorrection": "Initial local checker falsely classified that benign exit 1 as FAIL; corrected interpretation and reran. No source or whitespace defect was found.",
        "maintainedAndFormalMarkdownFiles": len(paths),
        "localLinksAndAnchorsChecked": links,
        "productionFilesUnchanged": len(source["productionFiles"]),
        "trackedChanges": tracked,
        "newFiles": untracked,
        "architectureReleaseUnchanged": source["architectureRelease"],
        "temporalProvenance": "UNRESOLVED; original Task/follow-up instruction-entry times and filename correspondence need SA treatment",
        "limits": "Structure, local references and source/scope preservation; semantic adequacy requires SA review. No solution-wide software readiness claim."
    }


if __name__ == "__main__":
    result = run()
    destination = AFTER / "document-validation.json"
    destination.write_text(json.dumps(result, indent=2) + "\n")
    print(json.dumps(result, indent=2))
    raise SystemExit(0 if result["structuralOutcome"] == "PASS" else 1)
