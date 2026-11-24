#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify retained assessment correspondence and preservation, not acceptance."""
import collections
import csv
import hashlib
import json
from pathlib import Path
import re
import sys
ROOT=Path.cwd()
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import architecture_continuum as c
from architecture_review_evidence import check_links, check_iteration
D=ROOT/'enterprise-architecture/design/evidence/1.1.18/001/after/reconciliation'

def read(name):return json.loads((D/name).read_text())
def digest(p):return hashlib.sha256(p.read_bytes()).hexdigest()
catalogue,elements=c.load()
rows=read('candidates.json');sources=read('source-records.json')
manifest=list(csv.DictReader((ROOT/'openspec/MANIFEST.csv').open(newline='')))
assert len(rows)==784 and collections.Counter(r['specialization'] for r in rows)=={'UseCase':363,'UserStory':421}
assert len({(r['source'],r['specialization']) for r in rows})==784
assert len(sources)==421 and {r['source'] for r in rows}=={r['path'] for r in sources}=={r['spec_path'] for r in manifest}
source_map={s['path']:s for s in sources}
for r in rows:
    text=(ROOT/r['source']).read_text()
    match=re.search(r'^- \*\*User Story:\*\* (.*)$',text,re.M)
    expected=match.group(1) if r['specialization']=='UserStory' else re.search(r'^# (.+)$',text,re.M).group(1)
    assert r['originalText']==expected
    assert r['sourceSha256']==digest(ROOT/r['source'])
    assert r['sourceId']==source_map[r['source']]['sourceId']
    assert r['wordingMatches'] and r['sourceIdMatches'] and r['actorMatches']
    assert r['proposedCanonicalId'] is None
    assert r['capabilityRelationshipType'] is None and r['subjectRelationshipType'] is None
    assert r['alignmentOutcome']!='UNASSESSED'
    assert r['incorporationOutcome'].startswith('HOLD:')
    for rec in r['alignmentRecommendation']:
        actual,_,path=elements[rec['canonicalId']]
        assert actual['standing']['currentness']=='current'
        assert actual['standing']==rec['standing'] and actual['displayName']==rec['name']
        assert str(path.relative_to(ROOT))==rec['source']
assert not {'UseCase','UserStory'} & set(catalogue['specializations'])
csv_rows=list(csv.DictReader((D/'candidate-assessment.csv').open(newline='')))
assert len(csv_rows)==784
for r,flat in zip(rows,csv_rows):
    assert flat['source_id']==r['sourceId'] and flat['specialization']==r['specialization']
    assert flat['original_text']==r['originalText'] and flat['source']==r['source']
assert (ROOT/'enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md').read_bytes()==(ROOT/'enterprise-architecture/design/archimate/working/1.1.18/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md').read_bytes()
pins=read('input-pins.json');checked=[];excluded=[];mismatches=[]
for item in pins['files']:
    # Initial capture included five then-existing assessment outputs. They are
    # activity outputs, not pre-existing protected inputs; do not misrepresent
    # their expected later edits as source mutations or rewrite the original pin.
    if item['path'].startswith(('enterprise-architecture/design/evidence/1.1.18/001/after/','enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/')):
        excluded.append(item['path']);continue
    checked.append(item['path']);p=ROOT/item['path']
    if not p.is_file() or digest(p)!=item['sha256']:mismatches.append(item['path'])
assert not mismatches,mismatches
initial_canonical={p for p in checked if p.startswith('enterprise-architecture/continuum/')}
current_canonical={str(p.relative_to(ROOT)) for p in (ROOT/'enterprise-architecture/continuum').rglob('*') if p.is_file()}
assert initial_canonical==current_canonical
delta=json.loads((D.parent/'architectural-delta.json').read_text())
assert delta['canonicalValidation']['status']=='PASS'
assert delta['baselineSha256']==pins['baseline']['sha256']
assert delta['submissionSha256']==pins['submission']['sha256']
assert delta['canonicalValidation']['outputSha256']==pins['baseline']['sha256']
assert delta['canonicalSourcesChanged']==0 and delta['canonicalIdsAllocated']==0
report_dir=ROOT/'enterprise-architecture/design/evidence/1.1.18/001/after/ai-agent-reports'
docs=[ROOT/'enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md',ROOT/'enterprise-architecture/design/review/1.1.18/change-review.md',D.parent/'report-index.md',ROOT/'.junie/reports/2026-11-02-openspec-subject-capability-reconciliation.md']
docs+=list(report_dir.glob('*.md')) if report_dir.exists() else []
links=[]
for p in docs:
    if p.exists():links+=check_links(p)
    else:links.append('Not yet emitted: '+str(p.relative_to(ROOT)))
provenance=check_iteration(ROOT,'1.1.18','001')
result=dict(evidenceConsistency='PASS',candidateRows=784,sourceFiles=421,wordingAndSourceCorrespondence='PASS',existingRecommendationIdentitiesAndStanding='PASS',protectedInputs=dict(outcome='PASS',checkedFiles=len(checked),mismatches=mismatches,excludedActivityOutputPins=excluded,canonicalFilePopulationUnchanged=True),canonicalBaselineValidation=delta['canonicalValidation'],incomingValidation=dict(outcome='PASS',basis='Maintained Exchange parser/offline XSD/reference/identifier checks; summary.json submissionIssues empty'),fullDeltaReview=dict(outcome='UNRESOLVED',bytes=(D.parent/'architectural-delta.json').stat().st_size,conditions=delta['exceptionalConditions'],architecturalInterpretation='Pending bounded-scope disposition'),javaArchitectureSuite=dict(outcome='UNRESOLVED',harness='Maven dependency resolution failed on read-only local repository before test execution',command="timeout 120 mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false",log='architecture-tests.log'),documentLinks=dict(outcome='FAIL' if links else 'PASS',issues=links),provenance=provenance,architecturalAcceptance='NOT RECORDED',canonicalIdsAllocated=0,canonicalSourcesChanged=0,releaseTransition='NOT PERFORMED',actualArchiTransport='NOT PERFORMED')
assert not read('summary.json')['submissionIssues']
(D/'verification.json').write_text(json.dumps(result,ensure_ascii=False,indent=2)+'\n')
print(json.dumps({k:v for k,v in result.items() if k in ('evidenceConsistency','candidateRows','sourceFiles','documentLinks','provenance')},indent=2))
