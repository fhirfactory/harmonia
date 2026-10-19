#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Validate explicit Home evidence, fixed migration and review audit independently."""
import collections
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys

ROOT = Path.cwd()
HERE = Path(__file__).resolve().parent
RUN = HERE.parent
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import analyse_home_submission as analysis
import diagnose_roundtrip as d
import home_relationships as h
import synchronise_baseline as s


def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def sections(text):
    return {m.group(1): m.group(2) for m in re.finditer(
        r'^## (CR-\d+) — .*?\n(.*?)(?=^<a id="cr-|\Z)', text, re.M | re.S)}


def field(text, name):
    return text.split('### ' + name + '\n', 1)[1].split('\n### ', 1)[0].strip()


def main():
    checks = []
    def check(label, condition):
        checks.append({'check': label, 'outcome': 'PASS' if condition else 'FAIL'})
        if not condition:
            raise AssertionError(label)
    original = RUN.parent / 'run-001'
    start = json.loads((RUN / 'detection/start-preservation.json').read_text())
    for path, digest in start['files'].items():
        if path == 'architecture-release.json':
            continue
        check('Preserved starting file: ' + path, sha(ROOT / path) == digest)
    source = ROOT / 'docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml'
    evidence = json.loads((RUN / 'detection/explicit-homes.json').read_text())
    check('Fresh input hash matches evidence', sha(source) == evidence['input']['sha256'])
    check('Compressed updated input is byte-identical',
          gzip.decompress((RUN / 'detection/submission.xml.gz').read_bytes()) == source.read_bytes())
    fresh = analysis.analyse(source, original / 'detection/submission.xml',
                             ROOT / 'docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml')
    # Input path spelling is operational evidence; compare architectural content.
    check('Independent fresh read reproduces all Home observations', fresh['authored_homes'] == evidence['authored_homes'])
    check('Exactly four authored Home relationships', len(fresh['authored_homes']) == 4)
    check('All prior submitted native semantic relationships unchanged', fresh['original_relationships_preserved'])
    check('No submitted element added, removed or changed in Home update',
          all(not value for value in fresh['delta_from_prior_input']['elements'].values()))
    check('Only four explicitly authored semantic edges added',
          set(fresh['delta_from_prior_input']['relationships']['added']) ==
          {r['identifier'] for r in fresh['authored_homes']} and
          not fresh['delta_from_prior_input']['relationships']['removed'] and
          not fresh['delta_from_prior_input']['relationships']['changed'])
    for row in fresh['authored_homes']:
        check('Authored Home native/direction/Feature target: ' + row['identifier'],
              row['native_type'] == 'Association' and row['isDirected'] and
              row['specialization'] == 'Home' and row['feature_target_confirmed'] and row['name'] is None)
    outputs, b, report = s.publish()
    root = s.g.parse(s.metadata.model_path())
    s.validate(b, root)
    for path, content in outputs.items():
        check('Independent generated output reproduction: ' + str(path.relative_to(ROOT)),
              path.read_bytes() == content or (path == s.metadata.validation_path() and
                  json.loads(path.read_text())['output_sha256'] == report['output_sha256']))
    before = d.Exchange(ROOT / 'docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml')
    after = d.Exchange(s.metadata.model_path())
    check('Normalised Exchange schema and references', not after.issues)
    for kind in ('elements', 'relationships', 'views'):
        old, new = getattr(before, kind), getattr(after, kind)
        check('All original ' + kind + ' preserved exactly with resolved property names',
              all(key in new and analysis.signature(node, before) == analysis.signature(new[key], after)
                  for key, node in old.items()))
    homes = h.authored_homes(after)
    fixed = [r for r in b.business_homes['records'] if r['status'] == 'normalised']
    check('Exactly 208 fixed existing-baseline Home assertions', len(homes) == len(fixed) == 208)
    check('No unregistered additional relationship', after.relationships.keys() - before.relationships.keys() ==
          {r['relationship_identifier'] for r in fixed})
    check('No new 1.0.8 element Home manufactured',
          all(row['source'] in before.elements and row['target'] in before.elements for row in homes))
    h.validate(b, root)
    for row in b.business_homes['records']:
        if row['status'] != 'normalised':
            check('No guessed Home: ' + row['source_key'],
                  not any(home['source'] == b.ids[row['source_key']] for home in homes))
    historical = sections((original / 'history/revision-003/change-review.md').read_text())
    current = sections((original / 'change-review.md').read_text())
    check('All 36 original CRs retained', historical.keys() == current.keys() and len(current) == 36)
    for cid in current:
        check(cid + ' original Disposition preserved',
              re.search(r'^\*\*Disposition:\*\* (.*)$', current[cid], re.M).group(1) ==
              re.search(r'^\*\*Disposition:\*\* (.*)$', historical[cid], re.M).group(1))
        check(cid + ' original Detected Evidence preserved',
              field(current[cid], 'Detected Evidence') == field(historical[cid], 'Detected Evidence'))
    outcomes = {}
    for cid in ('CR-023', 'CR-025', 'CR-027', 'CR-029'):
        outcomes[cid] = re.search(r'^\*\*Outcome:\*\* (.*)$', current[cid], re.M).group(1)
        check(cid + ' Resolved by governing Home decision', outcomes[cid] == 'Resolved' and
              'architecture-modelling-strategy.md#explicit-architectural-home' in current[cid])
    check('Native Realizations independently remain unresolved',
          all('**Outcome:** Unresolved' in current[cid] for cid in ('CR-022','CR-024','CR-026','CR-028')))
    overlay = json.loads((original / 'review-revision-004.json').read_text())
    check('Review overlay and displayed fields agree', all(
          f"**Disposition:** {row['disposition']}" in current[row['id']] and
          f"**Outcome:** {row['outcome']}" in current[row['id']] for row in overlay['crs']))
    check('Release patch increment', s.load_release() == dict(version='1.0.14', previous_version='1.0.13'))
    result = dict(status='PASS', governance_release=s.load_release(), checks=checks,
                  authored_home_count=4, normalised_home_count=208,
                  assessments=dict(collections.Counter(r['status'] for r in b.business_homes['records'])),
                  resulting_model=str(s.metadata.model_path().relative_to(ROOT)),
                  resulting_model_sha256=sha(s.metadata.model_path()), cr_outcomes=outcomes,
                  native_relationships_preserved=len(before.relationships),
                  original_views_preserved=len(before.views), new_canonical_ids=0,
                  downstream_information_application_ingestion=False)
    (HERE / 'validation-results.json').write_text(json.dumps(result, ensure_ascii=False, indent=2)+'\n')
    print(json.dumps({k:v for k,v in result.items() if k != 'checks'}, indent=2))
    print('PASS:', len(checks), 'independent checks')


if __name__ == '__main__':
    main()
