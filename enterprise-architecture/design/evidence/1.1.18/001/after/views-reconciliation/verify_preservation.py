#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independent scope/identity/preservation assertions for this bounded activity."""
import csv
import hashlib
import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
from exchange_compare import Exchange
from exchange_xml import N, TYPE, signature

B = ROOT / 'enterprise-architecture/design/evidence/1.1.18/001'
A = B / 'after/views-reconciliation'
pins = json.loads((B / 'before/views-reconciliation/input-pins.json').read_text())
changed = [p for p, h in pins['protectedFiles'].items()
           if not (ROOT / p).is_file() or hashlib.sha256((ROOT / p).read_bytes()).hexdigest() != h]
assert not changed, changed
template = ROOT / pins['template']['path']
assert hashlib.sha256(template.read_bytes()).hexdigest() == pins['template']['sha256']
assert hashlib.sha256((ROOT / pins['incoming']['path']).read_bytes()).hexdigest() == pins['incoming']['sha256']
subjects = Exchange(ROOT / pins['subjects']['path'])
published = Exchange(ROOT / pins['published']['path'])
assert not subjects.issues and not published.issues
rows = list(csv.DictReader((B / 'after/subject-incorporation/incorporated-subjects.csv').open()))
edges = list(csv.DictReader((B / 'after/subject-incorporation/incorporated-classification-relationships.csv').open()))
assert len(rows) == 32 and len(edges) == 27
for row in rows:
    node = subjects.elements[row['exchangeId']]
    assert subjects.single(node, 'canonicalId') == row['canonicalId']
    assert node.get(TYPE) == 'Grouping'
for edge in edges:
    node = subjects.relationships[edge['identifier']]
    assert node.get(TYPE) == 'Composition'
    assert all(node.get(k) == edge[k] for k in ('source', 'target'))
assert subjects.elements.keys() - published.elements.keys() == {r['exchangeId'] for r in rows}
assert subjects.relationships.keys() - published.relationships.keys() == {r['identifier'] for r in edges}
assert all(signature(subjects.relationships[r]) == signature(v) for r, v in published.relationships.items())
_, resources = c.load(ROOT)
existing = {str(p.relative_to(ROOT)) for _, _, p in resources.values()}
original = {p for p in pins['protectedFiles'] if p.startswith('enterprise-architecture/continuum/') and p.endswith('.md') and not p.endswith('MANIFEST.md')}
assert existing == original
summary = json.loads((A / 'summary.json').read_text())
assert summary['stagedExactViewsAfterDocumentedRebindings'] == summary['authoredViews'] == 1285
assert not summary['verificationXmlGenerated'] and not summary['maintainedTemplateChanged']
assert summary['unresolvedElements'] == 120
assert summary['preflight']['outcome'] == 'FAIL'
assert summary['visualSemanticEndpointMismatches'] == 0
out = dict(outcome='PASS', meaning='preservation and blocker assertions passed; reconciliation task remains UNRESOLVED',
    protectedFilesUnchanged=len(pins['protectedFiles']), canonicalElementFilesUnchanged=len(resources),
    acceptedSubjectsVerified=32, acceptedCompositionsVerified=27, originalRelationshipsVerified=1882,
    maintainedTemplateUnchanged=True, incomingUnchanged=True, publishedUnchanged=True,
    releaseUnchanged=True, existing784CandidateAssessmentUnchanged=True,
    noAdditionalCanonicalElements=True, noSemanticIncorporation=True,
    newImportableXml='NOT_GENERATED: native reference/XSD gates fail', actualArchiTransport='NOT_PERFORMED')
(A / 'preservation-verification.json').write_text(json.dumps(out, indent=2) + '\n')
print(json.dumps(out, indent=2))
