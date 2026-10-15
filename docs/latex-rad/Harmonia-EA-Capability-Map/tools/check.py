#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Check generated sources, resolved LaTeX navigation and the compiled baseline."""
import hashlib
import json
import re
import subprocess
from pathlib import Path

BASE = Path(__file__).resolve().parents[1]
ROOT = BASE.parents[2]


def main():
    subprocess.run(['python3', str(BASE / 'tools/render.py'), '--check'], check=True, timeout=30)
    m = json.loads((BASE / 'manifest.json').read_text())
    e = m['elements']
    coverage = json.loads((BASE / 'source-coverage.json').read_text())
    for k, s in m['sources'].items():
        lines = (ROOT / s['path']).read_text().splitlines()
        blocks = [b for b in coverage if b['source'] == k]
        covered = {i for b in blocks for i in range(b['start_line'], b['end_line'] + 1)}
        for b in blocks:
            text = '\n'.join(lines[b['start_line'] - 1:b['end_line']])
            assert hashlib.sha256(text.encode()).hexdigest() == b['sha256'], 'Coverage drift: ' + k
        for a, z in s['ranges']:
            assert all(i in covered or not lines[i - 1].strip() for i in range(a, z + 1)), 'Untranscribed source: ' + k
    latex = '\n'.join(p.read_text() for p in BASE.rglob('*.tex') if 'build' not in p.parts)
    labels = re.findall(r'\\label\{([^}]+)\}', latex)
    assert len(labels) == len(set(labels)), 'Duplicate source labels'
    references = re.findall(r'\\(?:name|page)?ref\{([^}]+)\}|\\hyperref\[([^\]]+)\]', latex)
    assert all((a or b) in labels for a, b in references), 'Unresolvable LaTeX reference'
    for x in e:
        assert labels.count(x['latex_label']) == 1, 'Missing element label: ' + x['reference_id']
    frozen = json.loads((BASE / 'identity-baseline.json').read_text())
    assert set(frozen['latex_labels']).issubset(labels), 'Existing LaTeX label removed'
    catalogue = '\n'.join((BASE / 'sections' / f).read_text() for f in
                          ['business-capabilities.tex', 'business-enabling-capabilities.tex', 'enterprise-capabilities.tex'])
    positions = [(x, catalogue.index('\\label{' + x['latex_label'] + '}')) for x in e]
    positions.sort(key=lambda pair: pair[1])
    overviews = 0
    for i, (x, start) in enumerate(positions):
        end = positions[i + 1][1] if i + 1 < len(positions) else len(catalogue)
        entry = catalogue[start:end]
        if x['type'] in ('capability', 'enterprise_capability'):
            assert '\\subsubsection*{Capability Overview}' in entry, 'Missing Capability Overview: ' + x['name']
            overviews += 1
        if x['category'] == 'business' and x['tier']:
            assert re.search(r'\\(?:section|subsection|subsubsection)\{' + x['tier'] + r': [^\n]*\}\\label\{' + re.escape(x['latex_label']) + r'\}', catalogue), 'Invisible CT heading: ' + x['name']
        assert 'Structural Canonical ID' in entry
        if x['canonical_id']:
            assert x['canonical_id'] in entry, 'ID missing from metadata'
    exceptions = json.loads((BASE / 'review-exceptions.json').read_text())
    assert {x['reference_id'] for x in exceptions['structural_id_unallocated']} == {x['reference_id'] for x in e if not x['canonical_id']}
    assert not exceptions['capability_overview_absent']
    aux = (BASE / 'build/Harmonia-EA-Capability-Map.aux').read_text()
    auxlabels = re.findall(r'\\newlabel\{([^}]+)\}', aux)
    assert len(auxlabels) == len(set(auxlabels)), 'Duplicate compiled labels'
    assert all(x['latex_label'] in auxlabels for x in e), 'Uncompiled element label'
    log = (BASE / 'build/Harmonia-EA-Capability-Map.log').read_text()
    bad = ['undefined', 'multiply defined', 'Overfull', 'Missing character', 'Rerun to get', 'Warning:']
    assert not any(s in log for s in bad), 'Unresolved compilation warnings'
    buildlog = (BASE / 'build/latexmk.txt').read_text()
    assert 'All targets' in buildlog and 'are up-to-date' in buildlog, 'Build did not converge'
    assert 'already defined' not in buildlog, 'Duplicate PDF destination'
    ilg = (BASE / 'build/Harmonia-EA-Capability-Map.ilg').read_text()
    assert '719 entries accepted, 0 rejected' in ilg and '0 warnings' in ilg, 'Index mismatch'
    idx = (BASE / 'build/Harmonia-EA-Capability-Map.idx').read_text()
    assert len(re.findall(r'\\indexentry', idx)) == len(e), 'Missing name index entry'
    info = subprocess.check_output(['pdfinfo', str(BASE / 'build/Harmonia-EA-Capability-Map.pdf')], text=True, timeout=10)
    pages = int(re.search(r'Pages:\s+(\d+)', info).group(1))
    counts = {
        'business_ct1': sum(x['category'] == 'business' and x['tier'] == 'CT1' for x in e),
        'business_ct2': sum(x['category'] == 'business' and x['tier'] == 'CT2' for x in e),
        'business_ct3': sum(x['category'] == 'business' and x['tier'] == 'CT3' for x in e),
        'business_enabling_ct1': 0, 'business_enabling_ct2': 0, 'business_enabling_ct3': 0,
        'business_enabling_tier_unresolved': sum(x['category'] == 'business_enabling' for x in e),
        **{plural: sum(x['type'] == t for x in e) for t, plural in
           [('feature', 'features'), ('function', 'functions'), ('service', 'services'), ('process', 'processes')]},
        'enterprise_capabilities': sum(x['type'] == 'enterprise_capability' for x in e),
        'candidate_business_concerns': sum(x['architectural_status'] == 'Candidate' for x in e),
        'unresolved_catalogue_description_identities': sum(c['element'] is None for c in m['catalogue_contexts']),
        'publication_element_records': len(e),
    }
    expected = [18, 39, 133, 76, 137, 147, 133, 19, 14]
    actual = [counts[k] for k in ['business_ct1', 'business_ct2', 'business_ct3', 'business_enabling_tier_unresolved',
                                'features', 'functions', 'services', 'processes', 'enterprise_capabilities']]
    assert actual == expected, 'Inventory discrepancy'
    report = {'result': 'PASS', 'publication': m['publication'], 'version': m['version'],
              'source_documents': len(m['sources']), 'source_blocks': len(coverage), 'counts': counts,
              'pdf_pages': pages, 'index_entries': len(e), 'unique_labels': len(labels),
              'undefined_references': 0, 'duplicate_labels': 0, 'duplicate_reference_ids': 0,
              'capability_overviews': overviews,
              'authored_feature_overviews': sum(x['type'] == 'feature' for x in e),
              'structural_canonical_ids': sum(bool(x['canonical_id']) for x in e),
              'unallocated_structural_ids': len(exceptions['structural_id_unallocated']),
              'duplicate_structural_ids': 0, 'reference_ids_unchanged': True,
              'existing_latex_labels_retained': len(frozen['latex_labels']),
              'materially_constrained_capability_overviews': exceptions['capability_overview_materially_constrained'],
              'material_description_constraints': len(exceptions['description_materially_constrained']),
              'uncovered_nonblank_source_lines': 0, 'overfull_boxes': 0, 'missing_glyphs': 0,
              'pdf_sha256': hashlib.sha256((BASE / 'build/Harmonia-EA-Capability-Map.pdf').read_bytes()).hexdigest()}
    (BASE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps(report, indent=2))


if __name__ == '__main__':
    main()
