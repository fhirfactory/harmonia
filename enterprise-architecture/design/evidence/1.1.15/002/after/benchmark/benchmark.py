#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded 001 regression evidence; no AI, canonical writes or Git mutation.

Run with the declared tooling Python environment. Checkpoint scaffolds are
demonstration outputs only, never replacements for the actual 001 review/HCRL.
"""
from pathlib import Path
import argparse
import json
import sys
import tempfile
from time import perf_counter

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_workbench as w
import architecture_review_evidence as evidence

OUT = Path(__file__).resolve().parent
BASELINE = ROOT / 'enterprise-architecture/design/archimate/published/1.1.14/Harmonia-Architecture.xml'
CANDIDATE = ROOT / 'enterprise-architecture/design/evidence/1.1.15/001/before/harmonia-continuum-1.1.15-001.xml'


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, default=OUT, help='Explicit fresh output for a repeat benchmark')
    outdir = parser.parse_args().output.resolve()
    outdir.mkdir(parents=True, exist_ok=True)
    destination = outdir / 'checkpoint-scaffold'
    if destination.exists():
        raise ValueError('Existing retained benchmark scaffold; choose an explicit new output before repeating')
    started = perf_counter()
    delta = w.normalised_review(BASELINE, CANDIDATE, ROOT)
    expected = {'elements': (1602, 12), 'relationships': (1651, 9), 'views': (1209, 7)}
    for kind, (retained, added) in expected.items():
        assert delta[kind]['retainedIds'] == retained
        assert len(delta[kind]['added']) == added
        assert not delta[kind]['removed'] and not delta[kind]['changed']
    assert len(delta['identity']['newWithoutCanonicalId']) == 12
    assert len(delta['viewNavigation']['changedViews']) == 17
    assert delta['outcome'] == 'REVIEW_READY', delta['exceptionalConditions']
    assert delta['canonicalIdsAllocated'] == delta['canonicalSourcesChanged'] == 0
    assert delta['classified']['normalisations']['Generated scalar: unchanged text/attributes; absent xml:lang becomes en'] == 17957
    assert len(delta['classified']['propertyDefinitionRebindings']) == 45
    delta_path = outdir / 'architectural-delta.json'
    delta_path.write_bytes(c.encoded(delta))
    with tempfile.TemporaryDirectory(prefix='harmonia-roundtrip-benchmark-') as tmp:
        stage = Path(tmp)
        boundary, _ = evidence.boundary_paths(stage, '1.1.15', '001')
        retained_delta = boundary / 'after/architectural-delta.json'
        retained_delta.parent.mkdir(parents=True)
        retained_delta.write_bytes(delta_path.read_bytes())
        task = evidence.emit_record(stage, '1.1.15', '001', 'ai-agent-tasks', 'model-update',
            '# BENCHMARK SCAFFOLD ONLY\n\nmodel update.\nmodel located at ' + str(CANDIDATE) + '\n\n'
            'The recorded original instruction-entry event is used to demonstrate mechanical prefix correspondence only. '
            'The actual 001 Task preserves the planning/execution attribution uncertainty; this demonstration does not resolve it.\n',
            event='2026-10-25T10:02:46.358Z',
            source='Recorded event in unchanged 001 Task; demonstration only, not a new substantive Task')
        preparation = evidence.prepare_review(stage, '1.1.15', '001', retained_delta, task)
        assert preparation['outcome'] == 'PASS', preparation
        assert preparation['preparation'] == 'TARGETED_INTERPRETATION_REQUIRED'
        # Retain only the useful generated review and HCRL examples. No duplicated
        # historical Task, delta, or synthetic formal Report becomes live evidence.
        destination.mkdir()
        for key in ('review', 'hcrl'):
            content = Path(preparation[key]).read_text()
            content = content.replace('../../evidence/1.1.15/001/after/architectural-delta.json', '../architectural-delta.json')
            task_link = '../../evidence/1.1.15/001/before/ai-agent-tasks/20261025T100246Z-model-update.md'
            original_task = ROOT / 'enterprise-architecture/design/evidence/1.1.15/001/before/ai-agent-tasks/20261025T100246Z-model-update.md'
            content = content.replace(task_link, evidence.relative_link(destination / Path(preparation[key]).name, original_task))
            (destination / Path(preparation[key]).name).write_text('<!-- BENCHMARK SCAFFOLD ONLY; not the operative 001 review/HCRL. -->\n' + content)
        for path in destination.glob('*.md'):
            assert not evidence.check_links(path), evidence.check_links(path)
    elapsed = perf_counter() - started
    original = json.loads((ROOT / 'enterprise-architecture/design/evidence/1.1.15/001/after/model-comparison.json').read_text())
    summary = dict(outcome='PASS', purpose='001 regression; proposed review preparation only',
        deterministicElapsedSeconds=round(elapsed, 6), comparisonAndCanonicalValidationSeconds=delta['deterministicElapsedSeconds'],
        evidencePreparationSeconds=preparation['elapsedSeconds'],
        normalisedDeltaBytes=delta_path.stat().st_size,
        compactDeltaBytes=len(json.dumps(delta, ensure_ascii=False, separators=(',', ':')).encode()),
        originalRawComparisonBytes=(ROOT / 'enterprise-architecture/design/evidence/1.1.15/001/after/model-comparison.json').stat().st_size,
        inputXmlBytes=sum(v['bytes'] for v in delta['inputs'].values()),
        originalRawFindings=len(original['findings']), originalRawClassifications=original['classifications'],
        suppressedLanguageFindings=17957, replacedPositionalOrganizationFindings=2803,
        retainedViewMoves=17, retainedViewFolderChanges=len(delta['viewNavigation']['folderChanges']),
        classificationCounts=delta['classified']['normalisations'],
        populations={k: dict(retainedIds=delta[k]['retainedIds'], added=len(delta[k]['added']), removed=0, changed=0) for k in expected},
        newElementsAwaitingCanonicalAllocation=12, representationAnomalies=delta['representationAnomalies'],
        exceptionalConditions=delta['exceptionalConditions'],
        aiCalls=0, corpusDrpReads=0, actualArchiTransport='NOT_PERFORMED',
        subsequentWork='Targeted architectural interpretation, independent CR decomposition, pending HCRL, STOP for Solution Architect; not benchmarked by an autonomous AI run',
        limits=['No claim of measured total AI interpretation time or token cost',
                'Original export provenance/no-edit control and temporal attribution uncertainty remain in 001',
                'Scaffolds are incomplete review examples; the actual 001 artefacts remain unchanged'])
    (outdir / 'benchmark-summary.json').write_bytes(c.encoded(summary))
    print(json.dumps(summary, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    main()
