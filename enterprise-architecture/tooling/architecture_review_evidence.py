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
import os
import re
import sys

from architecture_release import version_parts

ROOT = Path(__file__).resolve().parents[2]
ROLES = {"ai-agent-tasks": ("before", "Task-Issued-At"),
         "ai-agent-reports": ("after", "Report-Produced-At")}


def boundary_paths(root, version, iteration):
    version_parts(version)
    if not re.fullmatch(r'\d{3,}', iteration) or int(iteration) == 0:
        raise ValueError('Use a positive HCRL iteration, such as 004')
    design = Path(root) / 'enterprise-architecture/design'
    return design / 'evidence' / version / iteration, design / 'review' / version


def relative_link(source, target):
    return Path(os.path.relpath(target, source.parent)).as_posix()


def emit_record(root, version, iteration, role, name, body, *, event=None, source=None):
    """Emit one existing-model Task/Report, never manufacture a Task event time.

    Without a supplied Report event, the system clock captures completion of the
    first report write; subsequent field/prefix synchronization is maintenance.
    """
    if role not in ROLES or not re.fullmatch(r'[a-z0-9]+(?:-[a-z0-9]+)*', name):
        raise ValueError('Use a known evidence role and a descriptive lowercase name')
    boundary, _ = boundary_paths(root, version, iteration)
    side, label = ROLES[role]
    directory = boundary / side / role
    if event is None and role == 'ai-agent-tasks':
        event, source = 'UNRESOLVED', source or 'Instruction-entry timestamp unavailable; SA treatment required'
    if event is not None:
        if not source:
            raise ValueError('An explicit event requires its attributable source')
        if event == 'UNRESOLVED':
            prefix = 'unresolved-'
        else:
            if not re.fullmatch(r'\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|[+-]\d{2}:\d{2})', event):
                raise ValueError('Event requires ISO 8601 seconds and timezone')
            prefix = datetime.fromisoformat(event.replace('Z', '+00:00')).astimezone(timezone.utc).strftime('%Y%m%dT%H%M%SZ-')
        path = directory / (prefix + name + '.md')
        directory.mkdir(parents=True, exist_ok=True)
        with path.open('x', encoding='utf-8') as stream:
            stream.write(f'{label}: {event}\n\nEvent source: {source}\n\n{body}')
    else:
        # A temporary completed emission, followed by mechanical synchronization.
        import tempfile
        directory.mkdir(parents=True, exist_ok=True)
        with tempfile.NamedTemporaryFile(mode='w', encoding='utf-8', dir=directory, suffix='.tmp', delete=False) as stream:
            stream.write(body)
            temporary = Path(stream.name)
        event = datetime.now(timezone.utc).isoformat(timespec='microseconds').replace('+00:00', 'Z')
        prefix = datetime.fromisoformat(event.replace('Z', '+00:00')).strftime('%Y%m%dT%H%M%SZ-')
        path = directory / (prefix + name + '.md')
        try:
            with path.open('x', encoding='utf-8') as stream:
                stream.write(f'{label}: {event}\n\nEvent source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.\n\n{body}')
        finally:
            temporary.unlink()
    check_event(path, label)
    return path


def check_links(path):
    """Check local Markdown file links without following them or loading context."""
    text = re.sub(r'```.*?```', '', path.read_text(encoding='utf-8'), flags=re.S)
    errors = []
    for target in re.findall(r'\]\(([^\s)]+)\)', text):
        target = target.strip('<>').split('#', 1)[0]
        if target and not re.match(r'[a-zA-Z][a-zA-Z0-9+.-]*:', target):
            if not (path.parent / target).is_file():
                errors.append(f'{path}: missing link target {target}')
    return errors


def prepare_review(root, version, iteration, delta_path, task_path):
    """Deterministic checkpoint scaffolding; leaves interpretation open.

    Exact inputs, existing Task, generated Report and relative links are checked
    once. No AI, DRP traversal, disposition inference or recursive provenance.
    Existing review/HCRL files are never overwritten.
    """
    import hashlib
    from time import perf_counter
    started = perf_counter()
    root, delta_path, task_path = Path(root), Path(delta_path), Path(task_path)
    boundary, review_dir = boundary_paths(root, version, iteration)
    if task_path.parent.resolve() != (boundary / 'before/ai-agent-tasks').resolve():
        raise ValueError('Task must belong to the selected iteration')
    if not delta_path.resolve().is_relative_to((boundary / 'after').resolve()):
        raise ValueError('Delta must be retained under the selected iteration after/')
    temporal = check_event(task_path, 'Task-Issued-At')
    delta = json.loads(delta_path.read_text())
    if not isinstance(delta, dict) or delta.get('schemaVersion') != 2 or 'exceptionalConditions' not in delta:
        raise ValueError('Expected a normalised review delta')
    for item in delta['inputs'].values():
        path = Path(item['path'])
        if not path.is_absolute():
            path = root / path
        data = path.read_bytes()
        if len(data) != item['bytes'] or hashlib.sha256(data).hexdigest() != item['sha256']:
            raise ValueError('Pinned input changed: ' + str(path))
    review = review_dir / 'change-review.md'
    hcrl = review_dir / f'hcrl-{version}-{iteration}.pending.md'
    if review.exists() or hcrl.exists():
        raise ValueError('Existing Change Review/HCRL requires explicit bounded update; no automatic overwrite')
    review_dir.mkdir(parents=True, exist_ok=True)
    counts = {kind: {action: len(delta[kind][action]) for action in ('added', 'removed', 'changed')}
              for kind in ('elements', 'relationships', 'views')}
    summary = json.dumps(counts, ensure_ascii=False, sort_keys=True)
    review.write_text(f'''| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |

# ArchiMate Change Review — {version}/{iteration}

UNREVIEWED; no application authority.
Preparation status: TARGETED_INTERPRETATION_REQUIRED. This scaffold is not a completed architectural review.

## Review basis

[Normalised delta]({relative_link(review, delta_path)}) · [Task]({relative_link(review, task_path)}) · [Pending HCRL]({relative_link(review, hcrl)})

Detected population: {summary}. Existing View navigation changes: {len(delta['viewNavigation']['changedViews'])}.
Input hashes/sizes, preservation inventories, normalisation counts, rebindings and validation are in the linked delta.
Derived non-View navigation is regenerated through the existing Continuum projector after incorporation.

## Targeted interpretation required

No CR identifiers are allocated by this scaffold. Decompose independently adjudicable architectural changes under the existing CR contract, with linked table entries, Disposition On-Hold, Outcome Unresolved, and reconciliation not authorised. Do not use one XML record per CR or bundle independent decisions mechanically.

Targeted interpretation must establish architectural meaning, relevant Continuum authority, independently adjudicable CRs and dependencies. Start with delta context seeds and source locators. Use vicinity search and bounded relevant artefacts/references. Consult a DRP only for a specifically relevant detected change. Do not scan the corpus or consume whole XML/raw comparisons/session histories.

Exceptional conditions: {json.dumps(delta['exceptionalConditions'], ensure_ascii=False)}
Temporal provenance: {temporal or 'Task event/prefix correspondence checked; authenticity remains attributable-source responsibility.'}
No-edit transport is NOT_PERFORMED unless separately evidenced; this does not authenticate an Archi execution.

STOP for Solution Architect after targeted interpretation and HCRL preparation. Unresolved conditions go to review; no autonomous investigation or implementation/reviewer loop. Application remains a separate adjudicated step.
''', encoding='utf-8')
    hcrl.write_text(f'''| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Detected architectural delta | Not recorded | Targeted interpretation required; no application authority |
| [DR-2](#dr-2) | Bounded supporting Minutiae | Not recorded | Deterministic evidence and unresolved limits |

# Pending HCRL — {version}/{iteration}

UNREVIEWED; no application authority. Pending preparation consumes no release.

<a id="dr-1"></a>
## DR-1 — Detected architectural delta

[Change Review]({relative_link(hcrl, review)}); population: {summary}. Preserve independent CR correspondence, relevant architectural conclusions and Solution Architect dispositions here after targeted interpretation. No architectural acceptance is inferred.

<a id="dr-2"></a>
## DR-2 — Bounded supporting Minutiae

[Evidence]({relative_link(hcrl, delta_path)}) contains pinned inputs, deterministic validation, normalization and explicit exceptions. Task timestamp limitation: {temporal or 'none detected mechanically'}.

Minutiae-Accepted: [awaiting Solution Architect: Yes | No]
Comment: [awaiting Solution Architect]
''', encoding='utf-8')
    body = f'''# Deterministic round-trip preparation — Report

Standing: mechanical preparation; targeted architectural interpretation remains required.
Corresponding [Task]({relative_link(boundary / 'after/ai-agent-reports/report.md', task_path)}).
Prepared [Change Review]({relative_link(boundary / 'after/ai-agent-reports/report.md', review)}) and [pending HCRL]({relative_link(boundary / 'after/ai-agent-reports/report.md', hcrl)}).
Input/validation facts: [normalised delta]({relative_link(boundary / 'after/ai-agent-reports/report.md', delta_path)}).
Population: {summary}. No canonical identity allocation, Continuum amendment, release allocation or Git mutation performed by preparation.
Exceptions: {json.dumps(delta['exceptionalConditions'], ensure_ascii=False)}. Temporal limit: {temporal or 'none detected mechanically'}.
No AI review, corpus scan or architectural acceptance performed. STOP for targeted interpretation and Solution Architect adjudication.
'''
    report = emit_record(root, version, iteration, 'ai-agent-reports', 'round-trip-preparation', body)
    validation = check_iteration(root, version, iteration)
    validation['unresolved'].extend('Delta exception: ' + json.dumps(x, ensure_ascii=False)
                                    for x in delta['exceptionalConditions'])
    for path in (task_path, report, review, hcrl):
        validation['errors'].extend(check_links(path))
    validation['outcome'] = 'FAIL' if validation['errors'] else 'UNRESOLVED' if validation['unresolved'] else 'PASS'
    return dict(outcome=validation['outcome'], preparation='TARGETED_INTERPRETATION_REQUIRED',
                elapsedSeconds=round(perf_counter() - started, 6), review=str(review), hcrl=str(hcrl),
                report=str(report), validation=validation)


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
    mode = parser.add_mutually_exclusive_group()
    mode.add_argument('--prepare', action='store_true', help='Checkpoint scaffolding; no architectural interpretation or acceptance')
    mode.add_argument('--record-role', choices=tuple(ROLES))
    parser.add_argument('--delta', type=Path)
    parser.add_argument('--task', type=Path)
    parser.add_argument('--content', type=Path)
    parser.add_argument('--name')
    parser.add_argument('--event')
    parser.add_argument('--event-source')
    args = parser.parse_args()
    try:
        if args.prepare:
            if not args.delta or not args.task:
                raise ValueError('--prepare requires --delta and --task')
            result = prepare_review(args.repository, args.version, args.iteration, args.delta, args.task)
        elif args.record_role:
            if not args.content or not args.name:
                raise ValueError('--record-role requires --content and --name')
            path = emit_record(args.repository, args.version, args.iteration, args.record_role,
                               args.name, args.content.read_text(), event=args.event, source=args.event_source)
            result = dict(outcome='UNRESOLVED' if check_event(path, ROLES[args.record_role][1]) else 'PASS', record=str(path))
        else:
            result = check_iteration(args.repository, args.version, args.iteration)
    except (OSError, ValueError, KeyError, TypeError) as exc:
        result = dict(outcome="FAIL", errors=[str(exc)])
    print(json.dumps(result, indent=2))
    return {"PASS": 0, "FAIL": 1, "UNRESOLVED": 2}[result["outcome"]]


if __name__ == "__main__":
    sys.exit(main())
