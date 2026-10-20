#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only audit of corrected Run-002 against its preserved original evidence.

Consumes the common resolver's evidence; does not independently walk architecture.
Run from the repository root after candidate regeneration. JSON goes to stdout.
"""
import gzip
import hashlib
import json
from pathlib import Path


def sha(data):
    return hashlib.sha256(data).hexdigest()


def main():
    repository = Path.cwd()
    run = Path(__file__).resolve().parent.parent
    archive = run / 'validation/pre-containment-correction'
    archived = json.loads((archive / 'manifest.json').read_bytes())
    for relative, row in archived['files'].items():
        assert sha(gzip.decompress((archive / row['snapshot']).read_bytes())) == row['sha256'], relative

    def original(name):
        row = archived['files'][str((run / name).relative_to(repository))]
        return gzip.decompress((archive / row['snapshot']).read_bytes())

    before = json.loads(original('manifest.json'))
    after = json.loads((run / 'manifest.json').read_bytes())
    for relative, expected in after['files_sha256'].items():
        assert sha((run / relative).read_bytes()) == expected, relative
    for relative, expected in after['source_sha256'].items():
        assert sha((repository / relative).read_bytes()) == expected, relative
    assert before['frozen_files_before'] == after['frozen_files_before'] == after['frozen_files_after']
    for relative, expected in after['frozen_files_before'].items():
        assert sha((repository / relative).read_bytes()) == expected, relative
    assert original('candidate.xml') == (run / 'candidate.xml').read_bytes()
    previous = json.loads(original('workspace-projection.json'))
    current = json.loads((run / 'workspace-projection.json').read_bytes())
    for field in ('element_plan', 'relationship_plan', 'summary'):
        assert previous[field] == current[field], field
    resolved = json.loads((run / 'resolved-containment.json').read_bytes())
    assert resolved['findings'] == current['out_of_scope_containment_findings'] == []
    old_findings = previous['out_of_scope_containment_findings']
    direct = [r for r in old_findings if len(r['direct_parent_relationships']) > 1]
    assert len(old_findings) == 109 and len(direct) == 61
    dispositions = []
    for row in old_findings:
        edges = row['direct_parent_relationships']
        selected = [edge for edge in edges if edge in resolved['selected_relationships']]
        excluded = {edge: resolved['excluded_relationships'][edge] for edge in edges
                    if edge in resolved['excluded_relationships']}
        assert row['element'] in resolved['ancestry']
        assert len(selected) == 1
        if len(edges) > 1:
            assert list(excluded.values()) == ['preserved historical assertion']
        dispositions.append(dict(element=row['element'], original_direct_relationships=edges,
            selected_current_relationships=selected, excluded_historical_relationships=excluded,
            resolved_ancestry=resolved['ancestry'][row['element']]))
    result = dict(status='PASS', policy=resolved['version'],
        original_direct_cases=len(direct), original_affected_paths=len(old_findings),
        propagated_cases=len(old_findings) - len(direct), genuine_current_ambiguities=0,
        excluded_historical_relationships=len(resolved['excluded_relationships']),
        selected_by_mechanism={kind: sum(row['mechanism'] == kind for row in resolved['selected_relationships'].values())
                               for kind in ('Home', 'Composition')},
        historical_assertions='PRESERVED_IN_BYTE_IDENTICAL_CANDIDATE',
        candidate='BYTE_IDENTICAL', element_placement='UNCHANGED', relationship_placement='UNCHANGED',
        candidate_xml_sha256=sha((run / 'candidate.xml').read_bytes()),
        frozen_files_verified=len(after['frozen_files_before']), archived_files_verified=len(archived['files']),
        original_manifest_sha256=sha(original('manifest.json')),
        corrected_manifest_sha256=sha((run / 'manifest.json').read_bytes()), dispositions=dispositions)
    print(json.dumps(result, indent=2, sort_keys=True))


if __name__ == '__main__':
    main()
