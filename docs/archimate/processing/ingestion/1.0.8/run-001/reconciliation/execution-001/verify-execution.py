#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify this execution's preservation, review audit and actual resulting state."""
import hashlib
import json
from pathlib import Path
import re
import sys

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import generate_views as g


def read(path):
    return json.loads(path.read_text())


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def blocks(text):
    return {match.group(2): match.group(1) for match in re.finditer(
        r'(<a id="(cr-\d+)"></a>.*?)(?=\n<a id="cr-\d+"></a>|\Z)', text, re.S)}


def main():
    before = read(HERE / 'before.json')
    allowed = {'architecture-release.json', 'docs/archimate/exchange/publish/README.md',
               'docs/markdown/governance/baseline/README.md',
               'docs/markdown/governance/baseline/correspondence.json',
               *('docs/markdown/governance/baseline/' + p + '.md'
                 for p in ('features', 'processes', 'relationships', 'services')),
               *('docs/archimate/processing/ingestion/1.0.8/' + run + '/change-review.md'
                 for run in ('run-001', 'run-002'))}
    changed = [p for p, digest in before.items() if sha(ROOT / p) != digest]
    g.require(set(changed) <= allowed, 'Unexpected original-file changes: ' + str(set(changed) - allowed))
    g.require(sha(HERE / 'architecture-release-before.json') == before['architecture-release.json'],
              'Pre-execution release not preserved')
    inventory = read(HERE / 'cr-inventory.json')
    model = g.parse(ROOT / 'docs/archimate/exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml')
    resources = {e.get('identifier'): e for e in model.xpath('//*[@identifier]')}
    totals = {}
    interpretations = []
    for run, revision in (('run-001', 5), ('run-002', 2)):
        directory = HERE.parents[2] / run
        archived = directory / 'reconciliation/execution-001/change-review-before.md'
        current = directory / 'change-review.md'
        g.require(sha(archived) == before[str(current.relative_to(ROOT))], 'Adjudicated review changed')
        overlay = read(directory / f'review-revision-{revision:03d}.json')
        g.require(sha(archived) == overlay['prior_review_sha256'] and
                  sha(current) == overlay['current_review_sha256'], 'Review overlay drift')
        old_blocks, new_blocks = blocks(archived.read_text()), blocks(current.read_text())
        accepted = {r['cr'] for r in inventory if r['run'] == run and r['disposition'] == 'Accept'}
        results = read(directory / 'reconciliation/execution-001/results.json')['results']
        g.require({r['cr'] for r in results} == accepted == {r['cr'] for r in overlay['results']},
                  'Accepted CR/result population differs')
        for row in results:
            block = new_blocks[row['cr'].lower()]
            g.require('**Disposition:** Accept' in block and '**Outcome:** Resolved' in block and
                      row['processing_result'] == 'PASS', 'Current CR state mismatch')
            actual = resources[row['representationId']]
            g.require(g.digest(g.signature(actual)) == row['resulting_state_signature_sha256'],
                      'Recorded resulting state differs: ' + run + '/' + row['cr'])
            for entry in row['required_state_entries']:
                for identifier in re.findall(r'id-[0-9a-f]{32}', entry):
                    g.require(identifier in resources, 'Unestablished required resource: ' + identifier)
        for cr in old_blocks.keys() - {c.lower() for c in accepted}:
            g.require(old_blocks[cr] == new_blocks[cr], 'Historical interpretation record changed: ' + cr)
            interpretations.append(run + '/' + cr.upper())
        totals[run] = len(results)
    g.require(totals == {'run-001': 26, 'run-002': 6} and len(interpretations) == 10,
              'Execution population differs')
    result = dict(status='PASS', original_files=len(before),
                  original_inputs_detection_history_and_prior_publications='UNCHANGED',
                  authorised_current_file_changes=sorted(changed),
                  original_adjudicated_review_snapshots='BYTE_IDENTICAL',
                  interpretation_records_byte_identical=sorted(interpretations),
                  current_review_overlay_hashes='PASS', accepted_resulting_states=totals,
                  actual_result_signatures_and_required_resource_references='PASS')
    print(json.dumps(result, indent=2))


if __name__ == '__main__':
    main()
