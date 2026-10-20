#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reusable round-trip intake and review-applicability checkpoints.

The governed agent assembles independently adjudicable CRs from this evidence
and the review template. This helper neither invents CR granularity nor turns
reviewed prose into an automatically authorised architecture mutation.
"""
import argparse
import hashlib
import json
from pathlib import Path
import sys

import containment
import diagnose_roundtrip as diagnostic
import generate_views as g
from architecture_release import load_release
from workspace_paths import ROOT

AUTHORITY = 'docs/markdown/governance/archimate-round-trip-engineering-praxis.md'
TRIGGERS = {'review': 'Model Updated. Please review and generate change-review.md.',
            'apply': 'change-review.md reviewed and updated. Apply directives.'}


def discover(root=ROOT, baseline=None, incoming=None):
    """Require a unique applicable export; never select by timestamp or label."""
    release = load_release(root / 'architecture-release.json')
    version = release['version']
    exchange = root / 'docs/archimate/exchange'
    if baseline is None:
        candidates = sorted((exchange / 'publish' / version).glob('*.xml'))
        g.require(len(candidates) == 1, 'Applicable published baseline is missing/ambiguous; identify it explicitly')
        baseline, = candidates
    if incoming is None:
        candidates = sorted((exchange / 'ingest' / version).glob('*.xml'))
        candidates += sorted((exchange / 'ingest' / version).glob('*.xml.gz'))
        g.require(len(candidates) == 1, 'Architect export is missing/ambiguous; identify it explicitly')
        incoming, = candidates
    g.require(Path(baseline).resolve() != Path(incoming).resolve(), 'Baseline and architect export must be distinct')
    return dict(release=release, baseline=Path(baseline), incoming=Path(incoming))


def observe(baseline, incoming, control=None):
    before, after = diagnostic.Exchange(baseline), diagnostic.Exchange(incoming)
    transport = diagnostic.Exchange(control) if control else None
    evidence = diagnostic.compare(before, after, transport)
    try:
        effective = containment.resolve(after.root).evidence()
    except ValueError as exc:
        effective = dict(status='UNRESOLVED', reason=str(exc))
    return dict(authority=AUTHORITY, trigger=TRIGGERS['review'],
                checkpoint='AGENT_PREPARES_CHANGE_REVIEW_NO_ACCEPTANCE',
                evidence=evidence, effective_containment=effective,
                review_contract='docs/markdown/governance/archimate-ingestion-change-review.md',
                template='docs/markdown/governance/templates/archimate-ingestion-change-review-template.md',
                required_review_contents=['changed and unchanged architecture', 'canonical continuity and representation rebinding',
                    'new, deleted and evolved architecture', 'relationship changes and standing',
                    'derived metadata and navigation consequences', 'Views and presentation',
                    'validation, ambiguity and adjudication', 'proposed deterministic directives and prerequisites'],
                limitations=['Agent must decompose independently adjudicable CRs; raw records are not CRs.',
                             'Native Composition observations are not the effective state-filtered containment graph.',
                             'No missing control is fabricated; no approval, application or release occurs.'])


def inspect_review(path, intake):
    """Pin human adjudication to this input pair before agent contract review.

    Applicability is necessary, not sufficient execution authority. The agent
    must check every decision-linked instruction, dependency and expected state
    under the existing Change Review contract before dependent mutation.
    """
    data = Path(path).read_bytes()
    text = data.decode('utf-8')
    evidence = intake['evidence']
    for key in ('published_sha256', 'incoming_sha256'):
        g.require(evidence[key] in text, 'Reviewed document does not pin applicable input: ' + key)
    return dict(authority=AUTHORITY, trigger=TRIGGERS['apply'], review=str(path),
                reviewed_document_sha256=hashlib.sha256(data).hexdigest(),
                checkpoint='DIRECTIVE_CONTRACT_REVIEW_REQUIRED', automatic_execution_authorised=False,
                required_checks=['exact CR dispositions and decision-linked instructions',
                    'summary/detail consistency, approved scope and expected resulting state',
                    'prerequisites established in governed target state',
                    'no ambiguous or internally inconsistent directives',
                    'preserved prior review and per-CR execution/validation evidence',
                    'applicable Archi proof, acceptance and release gates'])


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('stage', choices=('discover', 'observe', 'inspect-review'))
    parser.add_argument('--baseline', type=Path)
    parser.add_argument('--incoming', type=Path)
    parser.add_argument('--control', type=Path)
    parser.add_argument('--review', type=Path)
    parser.add_argument('--intake', type=Path)
    parser.add_argument('--output', type=Path)
    args = parser.parse_args(argv)
    try:
        if args.stage == 'inspect-review':
            g.require(args.review and args.intake, 'Review and pinned intake evidence are required')
            report = inspect_review(args.review, json.loads(args.intake.read_bytes()))
            inputs = [args.review, args.intake]
        else:
            selected = discover(baseline=args.baseline, incoming=args.incoming)
            inputs = [selected['baseline'], selected['incoming'], *([args.control] if args.control else [])]
            report = selected if args.stage == 'discover' else observe(*inputs)
        data = (json.dumps(report, ensure_ascii=False, sort_keys=True, indent=2, default=str) + '\n').encode()
        if args.output:
            g.require(args.output.resolve() not in {path.resolve() for path in inputs}, 'Output would overwrite an input')
            # Existing evidence and human reviews are never silently replaced.
            with args.output.open('xb') as stream:
                stream.write(data)
        else:
            print(data.decode(), end='')
        return 0
    except (OSError, ValueError, KeyError, g.ET.LxmlError) as exc:
        print('UNRESOLVED: ' + str(exc), file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
