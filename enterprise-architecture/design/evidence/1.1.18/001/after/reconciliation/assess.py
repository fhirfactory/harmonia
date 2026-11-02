#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only input assessment; writes evidence only. Allocates no identities/edges.

Run from repository root using the maintained tooling dependency environment.
Lexical collisions are review leads, never identity or relationship assertions.
"""
import collections
import csv
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys

ROOT = Path.cwd()
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
from exchange_compare import Exchange
from exchange_xml import N, TYPE

BASE = ROOT / 'enterprise-architecture/design/evidence/1.1.18/001'
OUT = BASE / 'after/reconciliation'
MAPPING = ROOT / 'enterprise-architecture/design/archimate/working/1.1.18/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md'
SUBMISSION = BASE / 'before/Harmonia-Continuum-1.1.18-001-004.xml'
BASELINE = ROOT / 'enterprise-architecture/design/archimate/published/1.1.18/Harmonia-Architecture.xml'

def write(name, data):
    (OUT / name).write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n')

def rel(p):
    return str(p.relative_to(ROOT))

def pin(p):
    data = p.read_bytes()
    return dict(path=rel(p), bytes=len(data), sha256=hashlib.sha256(data).hexdigest())

catalogue, elements = c.load()
byex = {m['exchange']['identifier']: (m, body, path) for m, body, path in elements.values()}
subjects = {m['subjectGroup']['sourceId']: (m, body, path) for m, body, path in elements.values() if 'subjectGroup' in m}
before, incoming = Exchange(BASELINE), Exchange(SUBMISSION)
source_subject = []
for line in MAPPING.read_text().splitlines():
    cells = [v.strip().strip('*') for v in line.split('|')[1:-1]]
    if len(cells) == 3 and re.fullmatch(r'G\d\d(?:\.\d\d)?', cells[0]):
        source_subject.append(dict(code=cells[0], name=cells[1], concern=cells[2]))
write('candidate-subject-hierarchy.json', source_subject)
write('governed-subjects.json', [dict(code=k, canonicalId=m['canonicalId'], exchangeId=m['exchange']['identifier'], name=m['displayName'], specialization=m['specialization'], standing=m['standing'], source=rel(path), relationships=m['relationships']) for k,(m,b,path) in sorted(subjects.items())])

# Protect all pre-existing content on which this assessment depends or that is
# already locally edited; no Git-history recoverability claim for untracked bytes.
protected = set((ROOT/'enterprise-architecture/continuum').rglob('*'))
protected |= set((ROOT/'enterprise-architecture/design/archimate/working/1.1.18').rglob('*'))
protected |= {BASELINE, SUBMISSION, ROOT/'architecture-release.json'}
protected |= set((ROOT/'openspec').rglob('*'))
for raw in subprocess.check_output(['git','status','--porcelain','-z']).decode().split('\0'):
    if raw and not raw[3:].startswith(('enterprise-architecture/design/evidence/1.1.18/001/after/', 'enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/')):
        p=ROOT/raw[3:]
        if p.is_dir(): protected |= set(p.rglob('*'))
        else: protected.add(p)
pins = [pin(p) for p in sorted(protected) if p.is_file()]
if not (OUT/'input-pins.json').exists():
    write('input-pins.json', dict(gitHead=subprocess.check_output(['git','rev-parse','HEAD']).decode().strip(), baseline=pin(BASELINE), submission=pin(SUBMISSION), mapping=pin(MAPPING), files=pins))
snapshot = BASE/'before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md'
if not snapshot.exists(): snapshot.write_bytes(MAPPING.read_bytes())
assert snapshot.read_bytes() == MAPPING.read_bytes()

def field(text, key):
    match = re.search(r'^- \*\*'+re.escape(key)+r':\*\* (.*)$', text, re.M)
    return match.group(1).strip('`') if match else None

manifest=list(csv.DictReader((ROOT/'openspec/MANIFEST.csv').open(newline='')))
manifest_by_path={r['spec_path']:r for r in manifest}
all_specs={rel(p) for p in (ROOT/'openspec/specs').rglob('spec.md')}
mapping_rows=[]
for lineno,line in enumerate(MAPPING.read_text().splitlines(),1):
    if re.match(r'^\| (UC-|SR-)',line):
        cells=[x.strip() for x in re.split(r'(?<!\\)\|',line)[1:-1]]
        assert len(cells)==10,(lineno,len(cells))
        mapping_rows.append((lineno,cells))
source_records={}
candidates=[]
target_records={}
for lineno,v in mapping_rows:
    sid,kind,name,primary,secondary,target_name,target_id,qualification,actor,link=v
    path=(MAPPING.parent/link.strip('`')).resolve()
    text=path.read_text()
    source_path=rel(path)
    source_id=field(text,'Source Use Case') or field(text,'Source Requirement')
    story=field(text,'User Story')
    title=re.search(r'^# (.+)$',text,re.M).group(1)
    source_records[source_path]=dict(**pin(path), sourceId=source_id, title=title, userStory=story, sourceScope=field(text,'Source Scope'), primaryActor=field(text,'Primary Actor'), relatedUseCases=field(text,'Related Use Cases'), requirementHeadings=re.findall(r'^### Requirement: (.+)$',text,re.M), releaseStatements=[line for line in text.splitlines() if re.search(r'\b(?:R[1-9]|Release\s+\d|future|target.state)\b',line,re.I)], manifest=manifest_by_path.get(source_path))
    m,body,p=byex.get(target_id, ({},'',None))
    target_records[target_id]=dict(exchangeId=target_id, mappingName=target_name, canonicalId=m.get('canonicalId'), source=rel(p) if p else None, name=m.get('displayName'), specialization=m.get('specialization'), standing=m.get('standing'), identity=m.get('identity'), relationships=m.get('relationships'), content=body)
    # Exact source bindings are primary continuity evidence. Text matches are
    # deliberately segregated; an exact sentence still cannot establish identity.
    bound=[]; lexical=[]
    for em,eb,ep in elements.values():
        if any(b.get('source')==source_path for b in em.get('sourceBindings',[])):
            bound.append(dict(canonicalId=em['canonicalId'], specialization=em['specialization'], source=rel(ep), sourceBindings=em['sourceBindings']))
        if em['archimateType'] in ('Requirement','Constraint','Goal','Principle','Grouping') and (name==em['displayName'] or (name and name in eb)):
            lexical.append(dict(canonicalId=em['canonicalId'], source=rel(ep), reason='exact name or exact source text occurs; identity NOT established'))
    primary_exists=primary in subjects
    second_codes=re.findall(r'G\d\d\.\d\d',secondary)
    candidates.append(dict(row=lineno, sourceId=sid, specialization=kind, originalText=name, mappingSource=rel(MAPPING), source=source_path, sourceSha256=source_records[source_path]['sha256'], sourceIdMatches=sid==source_id, wordingMatches=name==(story if kind=='UserStory' else title), actorMatches=(actor or None)==field(text,'Primary Actor'), sourceScope=field(text,'Source Scope'), releaseStatements=source_records[source_path]['releaseStatements'], releaseAssignment='UNRESOLVED: source target-state scope does not allocate a governed release', primarySubject=primary, primaryGovernedId=subjects[primary][0]['canonicalId'] if primary_exists else None, secondarySubjects=second_codes, secondaryGovernedIds={k:subjects[k][0]['canonicalId'] if k in subjects else None for k in second_codes}, sourceBoundExisting=bound, lexicalReviewLeads=lexical, identityOutcome='EXISTING_SOURCE_BINDING_REQUIRES_SELECTOR_REVIEW' if bound else 'PROPOSED_INSTANCE_NO_EXISTING_SCOPED_SOURCE_BINDING', proposedCanonicalId=None, typeOutcome='UNRESOLVED: specialisation absent from governed catalogue' if kind not in catalogue['specializations'] else 'governed', proposedCapability=target_id, proposedCapabilityCanonicalId=m.get('canonicalId'), proposedCapabilityStanding=m.get('standing'), alignmentOutcome='UNASSESSED', mappingQualification=qualification, actor=actor))

write('source-records.json',list(source_records.values()))
write('candidates.json',candidates)
write('mapping-targets.json',list(target_records.values()))
features=[dict(canonicalId=m['canonicalId'], exchangeId=m['exchange']['identifier'], name=m['displayName'], source=rel(p), standing=m['standing'], identity=m['identity'], relationships=m['relationships'], content=b) for m,b,p in elements.values() if m['specialization']=='Feature']
write('existing-features.json',features)

duplicates={}
for label,key in [('scopedSourceAndType',lambda x:(x['source'],x['specialization'])), ('sourceIdAndType',lambda x:(x['sourceId'],x['specialization'])), ('sameWordingAndType',lambda x:(x['originalText'],x['specialization']))]:
    groups=collections.defaultdict(list)
    for row in candidates:groups[key(row)].append(dict(row=row['row'],source=row['source'],sourceId=row['sourceId'],specialization=row['specialization']))
    duplicates[label]=[dict(key=list(k),rows=v) for k,v in groups.items() if len(v)>1]
write('duplicate-review.json',duplicates)
summary=dict(candidateCount=len(candidates), specializations=dict(collections.Counter(x['specialization'] for x in candidates)), domains=dict(collections.Counter(Path(s).parts[2] for s in source_records)), sourceCount=len(source_records), sourceFilesNotMapped=sorted(all_specs-set(source_records)), mappingFilesNotInSpecs=sorted(set(source_records)-all_specs), manifestRows=len(manifest), sourceIdMismatches=[r['row'] for r in candidates if not r['sourceIdMatches']], wordingMismatches=[r['row'] for r in candidates if not r['wordingMatches']], actorMismatches=[r['row'] for r in candidates if not r['actorMatches']], sourceBoundCandidates=sum(bool(r['sourceBoundExisting']) for r in candidates), lexicalLeadCandidates=sum(bool(r['lexicalReviewLeads']) for r in candidates), primarySubjects=dict(collections.Counter(x['primarySubject'] for x in candidates)), missingSecondarySubjects=sorted({k for x in candidates for k,v in x['secondaryGovernedIds'].items() if v is None}), targetCounts=dict(collections.Counter(x['proposedCapability'] for x in candidates)), sourceReleaseStatements={s:r['releaseStatements'] for s,r in source_records.items() if r['releaseStatements']}, baselineIssues=before.issues, submissionIssues=incoming.issues, governedSubjectCount=len(subjects), duplicateCounts={k:len(v) for k,v in duplicates.items()})
write('summary.json',summary)
print(json.dumps({k:v for k,v in summary.items() if k not in ('sourceReleaseStatements','primarySubjects','targetCounts')},indent=2))
