#!/usr/bin/env python3
"""Read-only Pass 1 source/row/preservation checks; writes only verification.json.

Run from any directory. This is bounded review evidence, not canonical authoring
or a general lifecycle tool. It cannot adjudicate the analyst's recommendations.
"""
import collections
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
from datetime import datetime, timezone

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').exists())
EVIDENCE = HERE.parent.parent

def read(path):
    return json.loads(path.read_text())

def check(value, message):
    if not value:
        raise AssertionError(message)

def main():
    elements = {}
    for path in (ROOT / 'enterprise-architecture/continuum').rglob('*.md'):
        if path.name == 'MANIFEST.md':
            continue
        content = path.read_text()
        front, body = content.split('\n---\n', 1)
        metadata = json.loads(front.removeprefix('---\n'))
        cid = metadata['canonicalId']
        check(cid not in elements, 'duplicate canonical identity: ' + cid)
        elements[cid] = (metadata, body, path)
    caps = {cid: item for cid, item in elements.items() if item[0]['archimateType'] == 'Capability'}
    expected = {cid for cid, (m, _, _) in caps.items() if m['standing']['currentness'] in ('pending_adjudication', 'superseded') or m['standing']['semantic'] in ('Proposed', 'Candidate')}
    rows = read(HERE / 'reconciliation.json')
    screen = read(HERE / 'population-screening.json')
    counts = read(HERE / 'population-summary.json')
    ids = [r['canonicalId'] for r in rows]
    refs = [r['reference'] for r in rows]
    check(len(ids) == len(set(ids)) and len(refs) == len(set(refs)), 'duplicate row identity/reference')
    check(set(ids) == expected, 'source population and inventory differ')
    check(len(screen) == len(caps) and {r['canonicalId'] for r in screen} == set(caps), 'screening population incomplete')
    check({r['canonicalId'] for r in screen if r['included']} == expected, 'screening and inventory inclusion differ')
    check(len(elements) == 1996 and len(caps) == 842 and len(rows) == 436, 'bounded baseline counts changed')
    by_currentness = collections.Counter(r['standing']['currentness'] for r in rows)
    by_recommendation = collections.Counter(r['recommendedDisposition'] for r in rows)
    check(by_currentness == {'pending_adjudication': 32, 'superseded': 246, 'current': 158}, 'standing counts differ')
    check(by_recommendation == {'ACCEPT': 189, 'RETIRE': 246, 'REJECT': 1}, 'recommendation counts differ')
    check(dict(by_currentness) == counts['byCurrentness'] and dict(by_recommendation) == counts['byRecommendation'], 'summary counts differ')
    xids = {m['exchange']['identifier']: cid for cid, (m, _, _) in elements.items()}
    relationships = {}
    for m, _, _ in elements.values():
        for r in m.get('relationships', []):
            old = relationships.setdefault(r['identifier'], r)
            check(old == r, 'conflicting relationship representations')
    check(len(relationships) == 2246, 'native graph count changed')
    correspondence_count = 0
    provenance_objects = set()
    for row in rows:
        cid = row['canonicalId']
        m, body, path = caps[cid]
        check(row['name'] == m['displayName'] and row['standing'] == m['standing'], 'source name/standing mismatch: ' + cid)
        definition = body.split('## Architectural content', 1)[1].strip().split('\n\n')[0]
        check(row['definition'] == definition and row['source']['definition'] == definition, 'definition changed in evidence: ' + cid)
        check(row['source']['identity'] == m['identity'] and row['source']['provenance'] == m['provenance'], 'identity/provenance mismatch: ' + cid)
        check(row['elementType'] == ('Feature' if m.get('specialization') == 'Feature' else 'Capability'), 'type changed in evidence')
        check(row['solutionArchitectDecision'] == 'PENDING' and row['incorporationStatus'] == 'NOT AUTHORISED', 'unapproved decision/incorporation')
        for field in ('rationale', 'recommendedTreatment', 'consequentialChanges', 'solutionArchitectJudgement'):
            check(bool(row[field].strip()), 'missing reasoning field: ' + field)
        check(row['recommendedDisposition'] in ('ACCEPT', 'REJECT', 'RETIRE'), 'invalid disposition')
        for p in m['provenance']:
            check((ROOT / p['source']).exists(), 'source path missing: ' + p['source'])
            if p.get('revision') and re.fullmatch('[a-f0-9]{40}', p['revision']):
                provenance_objects.add(p['revision'] + ':' + p['source'])
        expected_edges = {rid for rid, r in relationships.items() if m['exchange']['identifier'] in (r['source'], r['target'])}
        recorded = row['architecturalContext']['nativeRelationships']
        check({r['identifier'] for r in recorded} == expected_edges, 'incident relationship omission: ' + cid)
        for r in recorded:
            original = relationships[r['identifier']]
            check(r['source'] == xids[original['source']] and r['target'] == xids[original['target']] and r['type'] == original['archimateType'] and r['standing'] == original.get('standing', {}), 'relationship context altered')
        for target in row['comparisons']:
            original, target_body, _ = elements[target['canonicalId']]
            check(target['name'] == original['displayName'] and target['standing'] == original['standing'], 'comparison standing/name altered')
            check(target['definition'] == target_body.split('## Architectural content', 1)[1].strip().split('\n\n')[0], 'comparison definition altered')
        co = m['identity'].get('correspondence')
        if co:
            correspondence_count += 1
            successor = elements[co['successorCanonicalId']][0]
            check(cid in successor['identity'].get('predecessorCanonicalIds', []), 'inverse predecessor correspondence missing')
            check(row['comparisons'][0]['canonicalId'] == co['successorCanonicalId'] and row['equivalenceAssessment'] == 'equivalent', 'existing correspondence lost')
    check(correspondence_count == 214, 'correspondence population differs')
    # Independent corroboration from the preserved historical source population.
    source = read(ROOT / 'docs/markdown/governance/baseline/architecture.json')
    main_ids = {x['canonicalId'] for x in source['elements'] if x.get('type') in ('capability', 'enterprise_capability', 'feature')}
    historical_ids = {x['canonicalId'] for x in source['historical_reference_elements'] if x.get('type') in ('capability', 'enterprise_capability', 'feature')}
    other = set(caps) - main_ids - historical_ids
    check(len(main_ids) == 627 and len(historical_ids) == 214 and len(other) == 1, 'source population accounting differs')
    check(main_ids.isdisjoint(historical_ids) and main_ids | historical_ids | other == set(caps), 'source identities omitted or multiply accounted')
    historical_by_id = {x['canonicalId']: x for x in source['historical_reference_elements']}
    for row in rows:
        if row['canonicalId'] in historical_by_id:
            original = historical_by_id[row['canonicalId']]
            check(row['sourceRecordEvidence'] == original, 'historical source record not faithfully preserved')
            check(row['architecturalContext']['sourceTier'] == original.get('tier') and row['architecturalContext']['category'] == original.get('category'), 'historical tier/category omitted')
    source_account = read(HERE / 'source-population-accounting.json')
    check(set(source_account['mainCapabilityFeatureSourceIdentities']) == main_ids and set(source_account['historicalReferenceSourceIdentities']) == historical_ids and set(source_account['separateBoundedReconciliationIdentities']) == other, 'source evidence mismatch')
    mapping_targets = read(ROOT / 'enterprise-architecture/design/evidence/1.1.18/001/after/reconciliation/mapping-targets.json')
    historical_mapping_targets = {x['canonicalId'] for x in mapping_targets if x['standing']['currentness'] in ('superseded', 'pending_adjudication') or x['standing']['semantic'] in ('Proposed', 'Candidate')}
    check(historical_mapping_targets <= set(ids), 'previously assessed unresolved target omitted')
    # Confirm cited editions are recoverable; no source is copied or interpreted by this gate.
    objects = sorted(provenance_objects)
    result = subprocess.run(['git', 'cat-file', '--batch-check'], cwd=ROOT, input='\n'.join(objects) + '\n', text=True, capture_output=True, timeout=30, check=True)
    unresolved_editions = [obj for obj, line in zip(objects, result.stdout.splitlines()) if line.endswith(' missing')]
    check(len(result.stdout.splitlines()) == len(objects), 'source recoverability response incomplete')
    check(not unresolved_editions, 'recorded source editions unavailable: ' + str(unresolved_editions))
    # Required table columns, unique row occurrences and working progressive-review anchors.
    table_refs = []
    required_headers = ['Reference', 'Canonical Identity', 'Name', 'Element Type', 'Current Standing', 'Definition', 'Architectural Context', 'Historical Evidence', 'Current Equivalent', 'Equivalence Assessment', 'Recommended Disposition', 'Recommended Treatment', 'Rationale', 'Consequential Changes', 'Solution Architect Decision', 'Incorporation Status']
    for page, page_count in counts['pages'].items():
        text = (HERE / 'inventory' / page).read_text()
        header = next(line for line in text.splitlines() if line.startswith('| Reference |'))
        check([c.strip() for c in header.strip('|').split('|')] == required_headers, 'missing full-table columns: ' + page)
        page_refs = []
        for line in text.splitlines():
            if re.match(r'^\| \[P1-\d{3}\]\(#', line):
                check(len(line.strip('|').split('|')) == 16, 'malformed table cell escaping')
                page_refs.append(re.search(r'P1-\d{3}', line)[0])
        check(len(page_refs) == page_count and page_count <= 40, 'page accounting/bound exceeded')
        check(len(re.findall(r'<a id="p1-\d{3}"></a>', text)) == page_count, 'detail anchors incomplete')
        table_refs.extend(page_refs)
    check(collections.Counter(table_refs) == collections.Counter(refs), 'full table omission/duplication')
    index = (HERE / 'adjudication-inventory.md').read_text()
    index_refs = re.findall(r'^\| \[(P1-\d{3})\]', index, re.M)
    check(collections.Counter(index_refs) == collections.Counter(refs), 'complete index omission/duplication')
    link_count = 0
    awaiting_output = []
    generated_refs = set()
    for path in HERE.rglob('*.md'):
        text = path.read_text()
        for match in re.finditer(r'\[[^\]\n]+\]\(([^\s)]+)\)', text):
            target = match[1]
            if target.startswith(('https:', 'http:', 'mailto:')):
                continue
            dest, _, anchor = target.partition('#')
            resolved = (path.parent / dest).resolve() if dest else path
            # These are finite output files produced by this check and the final provenance check.
            if not resolved.exists() and resolved in (HERE / 'verification.json', HERE / 'evidence-layout.json'):
                awaiting_output.append(str(resolved.relative_to(ROOT)))
                continue
            check(resolved.exists(), 'broken review link: ' + str(path.relative_to(ROOT)) + ' -> ' + target)
            if anchor and resolved.is_relative_to(HERE) and re.fullmatch(r'p1-\d{3}', anchor):
                check('id="' + anchor + '"' in resolved.read_text(), 'missing review row anchor: ' + target)
                generated_refs.add(anchor)
            link_count += 1
    check(generated_refs == {ref.lower() for ref in refs}, 'some detail rows are not navigable')
    pins = read(EVIDENCE / 'before/pass1-source-state.json')
    for path, digest in pins['files'].items():
        check(hashlib.sha256((ROOT / path).read_bytes()).hexdigest() == digest, 'protected baseline mutated: ' + path)
    check(read(ROOT / 'architecture-release.json') == {'version': '1.1.20', 'previous_version': '1.1.19'}, 'release mutated')
    accepted = ROOT / 'enterprise-architecture/design/archimate/published/1.1.19/Harmonia-Architecture.xml'
    check(hashlib.sha256(accepted.read_bytes()).hexdigest() == '9d4b6e27d69bf8999144ca474acc0e6264f0a2de9d42a8ee41f042cdf6095d09', 'accepted publication changed')
    check(subprocess.check_output(['git', 'rev-parse', 'HEAD'], cwd=ROOT, text=True).strip() == pins['head'], 'Git HEAD changed')
    hcrl_path = 'enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md'
    old_hcrl = subprocess.check_output(['git', 'show', pins['head'] + ':' + hcrl_path], cwd=ROOT, text=True)
    new_hcrl = (ROOT / hcrl_path).read_text()
    start = new_hcrl.index('\n### Pass 1 preparation — inventory submitted for adjudication\n')
    end = new_hcrl.index('\n<a id="dr-6"></a>', start)
    check(new_hcrl[:start] + new_hcrl[end:] == old_hcrl, 'prior HCRL wording/decisions modified')
    status = subprocess.check_output(['git', 'status', '--porcelain', '--untracked-files=all'], cwd=ROOT, text=True)
    changed = [line[3:] for line in status.splitlines()]
    allowed_prefix = str(EVIDENCE.relative_to(ROOT)) + '/'
    check(all(path == hcrl_path or path.startswith(allowed_prefix + 'before/') or path.startswith(allowed_prefix + 'after/pass1-historical-reconciliation/') or path.startswith(allowed_prefix + 'after/ai-agent-reports/') for path in changed), 'changes outside authorised evidence/DR-5 scope')
    report = {
        'status': 'PASS', 'verifiedAt': datetime.now(timezone.utc).isoformat(),
        'scope': 'Accounting, source correspondence, review navigation and preservation; no semantic adjudication or implementation satisfaction.',
        'canonicalElements': len(elements), 'nativeCapabilityRecords': len(caps), 'nativeRelationships': len(relationships),
        'historicalMarkedPopulation': 278, 'pendingAdjudication': 32, 'superseded': 246,
        'additionalCurrentUnresolved': 158, 'inventoryIdentities': len(rows),
        'inventoryTypes': dict(collections.Counter(r['elementType'] for r in rows)),
        'recommendedDispositions': dict(by_recommendation),
        'omittedIdentities': sorted(expected - set(ids)), 'unexpectedIdentities': sorted(set(ids) - expected),
        'duplicateIdentities': [], 'duplicateReferences': [], 'existingCorrespondencesPreserved': correspondence_count,
        'historicalSourceEquation': '627 + 214 + 1 = 842',
        'recordedSourceEditionsRecovered': len(objects), 'unresolvedSourceEditions': unresolved_editions,
        'priorMappingTargetPopulation': len(mapping_targets), 'priorUnresolvedTargetsInventoried': len(historical_mapping_targets),
        'completeTablePages': len(counts['pages']), 'fullColumnCount': 16, 'reviewLinksChecked': link_count,
        'renderedTableStructure': read(HERE / 'markdown-table-verification.json'),
        'protectedFilesIdentical': len(pins['files']), 'acceptedPredecessorSHA256': hashlib.sha256(accepted.read_bytes()).hexdigest(),
        'canonicalModelModified': False, 'releaseAllocated': False, 'priorHCRLContentPreserved': True,
        'resultingGitState': {'head': pins['head'], 'trackedMutation': hcrl_path, 'newEvidencePaths': [p for p in changed if p != hcrl_path], 'stagingCommitPush': 'not performed'},
        'forwardOutputFilesAwaitingEmission': sorted(set(awaiting_output)),
        'limits': ['Semantic recommendations require Solution Architect adjudication.', 'No new Archi transport or visual acceptance is claimed.', 'Inherited property, source and transport qualifications are preserved.']
    }
    (HERE / 'verification.json').write_text(json.dumps(report, indent=2, ensure_ascii=False) + '\n')
    print(json.dumps({k: report[k] for k in ('status', 'nativeCapabilityRecords', 'inventoryIdentities', 'recommendedDispositions', 'protectedFilesIdentical', 'reviewLinksChecked', 'recordedSourceEditionsRecovered', 'forwardOutputFilesAwaitingEmission')}, ensure_ascii=False))

if __name__ == '__main__':
    try:
        main()
    except (AssertionError, KeyError, ValueError, OSError, subprocess.SubprocessError) as error:
        print('FAIL: ' + str(error), file=sys.stderr)
        sys.exit(1)
