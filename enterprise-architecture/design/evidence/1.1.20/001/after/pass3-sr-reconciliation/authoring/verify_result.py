# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Validate the bounded SR transition; no implementation/office/SA acceptance."""
from collections import Counter
import csv
import hashlib
import json
from pathlib import Path
import posixpath
import re
import sys
import xml.etree.ElementTree as ET
from ooxml import Workbook, q, col_number

ROOT = Path('enterprise-architecture/design/evidence/1.1.20/001')
SOURCE = ROOT / 'after/pass3-xd-consolidation'
OUT = ROOT / 'after/pass3-sr-reconciliation'
read = lambda p: json.loads(p.read_text())
sha = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()
manifest = read(OUT / 'source-result-manifest.json')
lock = read(ROOT / 'before/pass3-sr-reconciliation-input-manifest.json')
changes = read(OUT / 'authoring/changes.json')
before = Path(manifest['baseline_catalogue']).read_text()
after = Path(manifest['result_catalogue']).read_text()
fp = re.compile(r'^- \*\*`([^`]+)` — (.*?):\*\* (.*)$', re.M)
hp = re.compile(r'^(#{2,4}) ([BE]\d+(?:\.\d+){0,2}) — (.*)$', re.M)


def parse(text):
    features = {i: (n, d) for i, n, d in fp.findall(text)}
    capabilities = {}
    blocks = {}
    heads = list(hp.finditer(text))
    for n, head in enumerate(heads):
        block = text[head.start():heads[n + 1].start() if n + 1 < len(heads) else len(text)]
        blocks[head[2]] = block
        description = next(line for line in block.splitlines()[1:] if line.strip())
        capabilities[head[2]] = (len(head[1]) - 1, head[3], description)
    return features, capabilities, blocks


sf, sc, sb = parse(before)
tf, tc, tb = parse(after)
bf = {x['id']: x for x in read(SOURCE / 'feature-reconciliation.json')}
bc = {x['id']: x for x in read(SOURCE / 'capability-reconciliation.json')}
features = {x['id']: x for x in read(OUT / 'feature-reconciliation.json')}
caps = {x['id']: x for x in read(OUT / 'capability-reconciliation.json')}
criteria = read(OUT / 'criterion-reconciliation.json')
transfers = read(OUT / 'sr001-feature-reconciliation.json')
retirement = read(OUT / 'sr-retirement-trace.json')
checks = {}


def check(name, passed):
    checks[name] = 'PASS' if passed else 'FAIL'


def counts(fs, cs, prefix=''):
    return {**{f'ct{level}': sum(i.startswith(prefix) and x[0] == level for i, x in cs.items()) for level in (1, 2, 3)}, 'features': sum(i.startswith(prefix) for i in fs)}


check('baseline_manifest_and_all_24_source_files_unchanged', all(sha(Path(p)) == digest for p, digest in lock['inputs'].items()))
check('baseline_catalogue_hash_correspondence', sha(Path(manifest['baseline_catalogue'])) == manifest['baseline_catalogue_sha256'] == '54f7b6de1a78cb28a4db41dcc19dc30ee3e80aee88ea14ded7d3714aa9a0e5d0')
check('baseline_workbook_hash_correspondence', sha(Path(manifest['baseline_workbook'])) == manifest['baseline_workbook_sha256'] == '0500831703b2d5a3b7698caaddc7efebb8482c4934f228a9a3868b239a50cb75')
check('baseline_population', counts(sf, sc) == {'ct1': 24, 'ct2': 145, 'ct3': 586, 'features': 2941})
check('result_population_and_authorised_delta', counts(tf, tc) == {'ct1': 24, 'ct2': 145, 'ct3': 585, 'features': 2936})
removed_features = {f'B05.03.04.F{n:02}' for n in range(1, 6)}
check('exact_retirement_and_no_new_identifiers', set(sf) - set(tf) == removed_features and set(sc) - set(tc) == {'B05.03.04'} and not set(tf) - set(sf) and not set(tc) - set(sc))
check('identifiers_unique', len(fp.findall(after)) == len(tf) and len(hp.findall(after)) == len(tc) and not set(tf) & set(tc))
parents = {}
bad = []
for line in after.splitlines():
    h, f = hp.fullmatch(line), fp.fullmatch(line)
    if h:
        level, ident = len(h[1]) - 1, h[2]
        if level > 1 and ident.rsplit('.', 1)[0] != parents.get(level - 1):
            bad.append(ident)
        parents[level] = ident
    if f and f[1].rsplit('.', 1)[0] != parents.get(3):
        bad.append(f[1])
check('ct1_ct2_ct3_feature_containment', not bad)
check('surviving_containment_and_numbering_gaps_preserved', all(x['parent'] == bf[i]['parent'] and i.rsplit('.', 1)[0] in tc for i, x in features.items()) and all(i.rsplit('.', 1)[0] in tc for i, x in tc.items() if x[0] > 1))
check('business_enterprise_hierarchies_distinct', counts(tf, tc, 'B') == {'ct1': 15, 'ct2': 86, 'ct3': 291, 'features': 1465} and counts(tf, tc, 'E') == {'ct1': 9, 'ct2': 59, 'ct3': 294, 'features': 1471})
check('only_authorised_capability_definition_changes', {i for i in tc if sc[i] != tc[i]} == set(changes['capabilities']))
check('only_authorised_feature_definition_changes', {i for i in tf if sf[i] != tf[i]} == set(changes['features']))
check('all_surviving_feature_names_unchanged', all(sf[i][0] == tf[i][0] for i in tf))
check('all_unaffected_capability_blocks_byte_preserved', all(sb[i] == tb[i] for i in tc if i not in changes['capabilities']))
check('canonical_register_and_markdown_correspondence', set(features) == set(tf) and set(caps) == set(tc) and all((x['name'], x['description']) == tf[i] and x['parent_name'] == caps[x['parent']]['name'] for i, x in features.items()) and all((x['level'], x['name'], x['description']) == tc[i] for i, x in caps.items()))
notes = re.compile(r'^\*(?:Demand:|Enterprise contributions:).*$', re.M)
check('supporting_demand_contribution_and_findings_preserved', Counter(notes.findall(before)) == Counter(notes.findall(after)))
check('retired_handover_block_preserved_exactly_in_history', retirement[0]['pass3_012_block'] == sb['B05.03.04'] and retirement[0]['supporting_context_transferred_unchanged'] in tb['B05.03.01'])
check('all_five_retired_source_feature_records_preserved', {x['source'] for x in transfers} == removed_features and all(x['source_record'] == bf[x['source']] for x in transfers))
check('source_to_target_mapping_complete_and_active', all(x['targets'] and set(x['targets']) <= set(tf) and x['primary_criterion_target'] in x['targets'] and all(i.startswith('B05.03.01.') for i in x['targets']) for x in transfers))
check('all_consolidated_criteria_attached_to_targets', all(any(c['source_feature'] == x['source'] and c['source_criterion'] == x['source_record']['source_criterion'] for c in features[i].get('consolidated_source_criteria', [])) for x in transfers for i in x['targets']))
check('sr001_unique_obligations_visible', all(term in tf[i][1].lower() for i, terms in {
    'B05.03.01.F01': ['explicit', 'clinical responsibility', 'accountable', 'receipt notification', 'required evidence'],
    'B05.03.01.F02': ['outstanding care obligations', 'accountable next owner', 'care context', 'receiving party'],
    'B05.03.01.F03': ['verified', 'identity assurance'],
    'B05.03.01.F04': ['boundary', 'traceable', 'operational work allocation'],
    'B05.03.01.F05': ['context-defined interval', 'delay', 'independently evidenced'],
    'B05.03.01.F06': ['rejected', 'resolution', 'recovered', 'escalation', 'without falsely completing']}.items() for term in terms))
check('operational_and_human_handover_blocks_unchanged', all(sb[i] == tb[i] for i in ['E06.08.05', 'B07.04.05']))
old_retired = [x[0] for x in read(SOURCE / 'retirement-trace.json')]
all_retired = old_retired + list(removed_features) + ['B05.03.04']
check('retired_identifiers_absent_from_active_hierarchy_and_references', all(i not in tf and i not in tc and not re.search(r'(?<![\w.])' + re.escape(i) + r'(?![\w.])', after) for i in all_retired))
check('sr004_handoff_routing_distinction', all(term in tc[i][2].lower() for i, terms in {
    'E06.03.01': ['transfer', 'accountable', 'outcome correlation', 'audit evidence', 'delivery attempt', 'accepted handoff'],
    'E06.03.03': ['destination selection', 'decision context', 'queue delivery', 'does not establish delivery or acceptance']}.items() for term in terms))
check('sr015_ergon_activity_task_commitment_distinctions', all(term in tc[i][2].lower() for i in ['E06.08.07', 'E06.09.07'] for term in ['ergon', 'activity', 'task', 'authoritative information commitment', 'stimulus', 'effector', 'implementation mechanism']))
check('sr015_inherited_recovery_obligations_preserved', all(term in tf[i][1].lower() for i, terms in {
    'E06.08.07.F01': ['accepted governed work', 'recoverable', 'failure'],
    'E06.08.07.F02': ['recovered subscriptions', 'currency', 'effective-state'],
    'E06.08.07.F03': ['redelivery', 'repeated offers', 'duplicate accepted undertakings'],
    'E06.08.07.F04': ['unavailable subscribers', 'disappear', 'falsely completed'],
    'E06.08.07.F05': ['publication, match, claim, assignment', 'eventual outcome', 'recovery'],
    'E06.09.07.F01': ['accepted unresolved work', 'restart', 'additional task'],
    'E06.09.07.F02': ['late progress report', 'historical task', 'current policy'],
    'E06.09.07.F03': ['contradictory completion reports', 'surfaced', 'arbitrarily collapsed'],
    'E06.09.07.F04': ['prior responsible party', 'reason'],
    'E06.09.07.F05': ['communication or ui session', 'durably accepted task']}.items() for term in terms))
check('sr016_protection_and_domain_authority_distinctions', all(term in tc[i][2].lower() for i, terms in {
    'B13.04.02': ['de-identification', 'pseudonymisation', 'minimisation', 'research utility', 're-identification risks', 'does not authorise unrestricted disclosure', 'every underlying transformation', 'every disclosure decision'],
    'B14.06.01': ['authorised access, presentation and disclosure', 'does not necessarily require de-identification', 'autonomy', 'every underlying transformation', 'every disclosure decision']}.items() for term in terms))
check('sr016_inherited_safeguarding_obligations_preserved', all(term in tf[i][1].lower() for i, terms in {
    'B13.04.02.F01': ['consent', 'safeguarding', 'wellbeing'], 'B13.04.02.F02': ['meaningful participant consent'],
    'B13.04.02.F03': ['participant risk', 'monitored', 'adverse conditions'], 'B13.04.02.F04': ['withdrawal', 'accurate supporting evidence'],
    'B13.04.02.F05': ['adverse events', 'appropriate response', 'adverse conditions'], 'B14.06.01.F01': ['qualifying safeguarding concerns', 'review', 'protective action'],
    'B14.06.01.F02': ['abuse and neglect', 'recognised', 'adverse conditions'], 'B14.06.01.F03': ['reported safely', 'adverse conditions'],
    'B14.06.01.F04': ['protective responses', 'appropriate'], 'B14.06.01.F05': ['safeguarding', 'follow-up']}.items() for term in terms))
check('sr017_correct_roles_and_all_ten_features_unchanged', all(sb[i] == tb[i] for i in ['E03.06.04', 'E03.06.05']) and 'Server' in tc['E03.06.04'][1] and 'Client' in tc['E03.06.05'][1] and 'Enabled interactions return' in tf['E03.06.04.F05'][1] and 'Outbound requests' in tf['E03.06.05.F05'][1])

preserved_fields = ['original_name', 'original_description', 'source_line', 'original_status', 'source_criterion', 'measurement_criterion', 'implementation_verification', 'technical_correspondence']
check('all_original_feature_criteria_status_and_verification_preserved', all(all(x[k] == bf[i][k] for k in preserved_fields) for i, x in features.items()))
check('all_original_capability_source_definitions_preserved', all(all(x[k] == bc[i][k] for k in ['original_name', 'original_description', 'source_line']) for i, x in caps.items()))
check('all_2941_source_criteria_retained_once', len(criteria) == len(bf) and len({x['source_feature'] for x in criteria}) == len(bf) and all(all(x[k] == bf[x['source_feature']][k] for k in ['original_description', 'source_criterion', 'measurement_criterion', 'original_status', 'implementation_verification']) for x in criteria))
with (OUT / 'measurement-criteria.csv').open(newline='') as stream:
    csv_rows = list(csv.DictReader(stream))
check('csv_source_criterion_and_target_correspondence', csv_rows == [{**x, 'active_features': '; '.join(x['active_features'])} for x in criteria])
check('all_criterion_targets_are_active', all(set(x['active_features']) <= set(tf) and x['primary_criterion_target'] in x['active_features'] for x in criteria))
check('ar001_no_measurement_formula_as_canonical_definition', not any(re.search(r'^(proportion\b|percentage\b|elapsed time\b|number of\b|rate of\b|conformance of .*under tests)', d, re.I) for _, d in tf.values()))
check('ar002_no_discovery_or_verification_badges', not any(re.search(r'\bPROPOSED\b|\[NEW C\d+\]|INFERRED_FROM_EVIDENCE|— unverified', t) for t in [v for x in tf.values() for v in x] + [v for x in tc.values() for v in x[1:]]))
check('ar002_substantive_unverified_subjects_preserved', {i for i, x in sf.items() if re.search('unverified', ' '.join(x), re.I)} == {i for i, x in tf.items() if re.search('unverified', ' '.join(x), re.I)})

xd = read(OUT / 'xd-incorporation.json')
check('all_28_xd_records_preserved_byte_for_byte', (SOURCE / 'xd-incorporation.json').read_bytes() == (OUT / 'xd-incorporation.json').read_bytes() and {x['id'] for x in xd} == {f'XD-{n:03}' for n in range(1, 29)})
xd_ids = sorted({i for x in xd for i in x['elements']})
check('all_xd_affected_elements_unchanged_or_still_retired', all((i in sf and tf.get(i) == sf[i]) or (i in sc and tc.get(i) == sc[i]) or (i in old_retired and i not in sf and i not in sc and i not in tf and i not in tc) for i in xd_ids))
check('xd012_decision_and_actual_movement_evidence_unchanged', all(tf[i] == sf[i] for i in ['E06.03.03.F02', 'E06.03.02.F04']))
check('xd027_authorised_selective_contextualisation_unchanged', tf['E06.02.03.F03'] == sf['E06.02.03.F03'] and 'General inputs remain valid' in tf['E06.02.03.F03'][1])
check('prior_xd_retirement_evidence_preserved', (OUT / 'retirement-trace.json').read_bytes() == (SOURCE / 'retirement-trace.json').read_bytes())

w = Workbook(Path(manifest['result_workbook']))
oldw = Workbook(Path(manifest['baseline_workbook']))
check('source_and_result_workbook_load_with_stdlib_ooxml', True)
parsed = {}
for n, b in w.data.items():
    if n.endswith(('.xml', '.rels')):
        parsed[n] = ET.fromstring(b)
check('xlsx_all_xml_and_relationship_parts_wellformed', True)
bad_targets = []
for name, tree in parsed.items():
    if name.endswith('.rels'):
        parent = '' if name == '_rels/.rels' else str(Path(name).parent.parent)
        for r in tree:
            if r.get('TargetMode') == 'External':
                continue
            target = r.get('Target')
            resolved = target.lstrip('/') if target.startswith('/') else posixpath.normpath(posixpath.join(parent, target))
            if resolved not in w.data:
                bad_targets.append([name, resolved])
check('xlsx_all_internal_relationship_targets_exist', not bad_targets)
names = [s.get('name') for s in w.sheets]
check('xlsx_sheet_names_ids_unique_and_valid', len(names) == len(set(names)) == 29 and all(len(n) <= 31 for n in names) and len({s.get('sheetId') for s in w.sheets}) == 29)
check('xlsx_all_worksheet_cell_orders_and_refs_valid', all([col_number(c.get('r')) for c in row] == sorted(set(col_number(c.get('r')) for c in row)) and all(re.fullmatch(r'[A-Z]+' + row.get('r'), c.get('r')) for c in row) for name in names for row in w.read(name).find(q('sheetData'))))
unchanged_sheets = [s.get('name') for s in oldw.sheets if s.get('name') not in ['Overview', 'SR Decisions', 'Reconciliation Exceptions', 'Feature Definitions', 'Capability Definitions', 'Measurement Criteria', 'Retirement Trace']]
check('xlsx_xd_fr_screening_and_prior_history_sheets_byte_preserved', all(w.data[w.path(n)] == oldw.data[oldw.path(n)] for n in unchanged_sheets))
check('xlsx_prior_sr_and_exceptions_byte_preserved_as_history', all(w.data[w.path('History P012 ' + n)] == oldw.data[oldw.path(n if n == 'SR Decisions' else 'Reconciliation Exceptions')] for n in ['SR Decisions', 'Exceptions']))
check('xlsx_prior_retirement_rows_preserved', w.rows('Retirement Trace')[:len(oldw.rows('Retirement Trace'))] == oldw.rows('Retirement Trace'))
oldsr = {x[0]: x for x in oldw.rows('SR Decisions')[1:]}
newsr = {x[0]: x for x in w.rows('SR Decisions')[1:]}
check('xlsx_only_five_current_sr_rows_changed', {i for i in newsr if newsr[i] != oldsr[i]} == {'SR-001', 'SR-004', 'SR-015', 'SR-016', 'SR-017'} and all('ACCEPTED AND INCORPORATED' in newsr[i][6] for i in ['SR-001', 'SR-004', 'SR-015', 'SR-016', 'SR-017']))
check('xlsx_sr017_current_narrative_correct', 'E03.06.04: Harmonia subsystems act as FHIR RESTful servers' in newsr['SR-017'][4] and 'E03.06.05: Harmonia subsystems act as FHIR RESTful clients' in newsr['SR-017'][4])
fr = w.rows('Feature Definitions')[1:]
cr = w.rows('Capability Definitions')[1:]
check('xlsx_active_feature_definitions_match_catalogue', len(fr) == len(tf) and len({x[0] for x in fr}) == len(tf) and all(x[0] in tf and (x[2], x[3]) == tf[x[0]] and x[1] == features[x[0]]['parent'] for x in fr))
check('xlsx_active_capability_definitions_match_catalogue', len(cr) == len(tc) and len({x[0] for x in cr}) == len(tc) and all(x[0] in tc and (int(x[1]), x[2], x[3]) == tc[x[0]] for x in cr))
check('xlsx_measurement_register_exact_correspondence', w.rows('Measurement Criteria') == [list(criteria[0])] + [[('; '.join(x[k]) if k == 'active_features' else x[k]) for k in criteria[0]] for x in criteria])
check('xlsx_five_transfer_rows_and_six_retirement_rows', len(w.rows('SR001 Merge Map')) == 6 and len(w.rows('SR Retirement Trace')) == 7)
check('xlsx_current_sr_correspondence_issues_marked_resolved', all('Accepted and incorporated' in x[2] for x in w.rows('Reconciliation Exceptions')[1:6]))
check('xlsx_external_office_qualification_preserved', (SOURCE / 'spreadsheet-application-check.json').read_bytes() == (OUT / 'spreadsheet-application-check.json').read_bytes() and 'UNRESOLVED' in str(w.rows('Reconciliation Exceptions')))
check('source_and_result_hashes_match_manifest', all(sha(Path(manifest[k])) == manifest[k + '_sha256'] for k in ['baseline_catalogue', 'baseline_workbook', 'result_catalogue', 'result_workbook']))
check('result_evidence_hashes_match_manifest', all(sha(OUT / p) == digest for p, digest in manifest['evidence_sha256'].items()))
check('release_authority_bytes_unchanged', sha(Path('architecture-release.json')) == lock['release_authority_sha256'])
protected_ok = True
for directory, record in lock['protected_trees'].items():
    paths = sorted(p for p in Path(directory).rglob('*') if p.is_file()) if Path(directory).exists() else []
    digest = hashlib.sha256('\n'.join(str(p) + ':' + sha(p) for p in paths).encode()).hexdigest()
    protected_ok &= len(paths) == record['files'] and digest == record['sha256']
check('continuum_register_and_publication_tree_bytes_unchanged', protected_ok)

summary = {
    'checks': checks, 'all_automated_checks_pass': all(v == 'PASS' for v in checks.values()),
    'before': counts(sf, sc), 'after': counts(tf, tc),
    'hierarchies': {i: {'before': counts(sf, sc, i), 'after': counts(tf, tc, i)} for i in ['B', 'E']},
    'changed_capability_definitions': sum(tc[i] != sc[i] for i in tc),
    'changed_feature_definitions': sum(tf[i] != sf[i] for i in tf),
    'retired_capabilities': ['B05.03.04'], 'retired_features': sorted(removed_features),
    'added_identifiers': [], 'feature_names_changed': 0,
    'source_criterion_records': len(criteria), 'source_measurements': sum(bool(x['measurement_criterion']) for x in criteria),
    'xd_active_elements_checked_unchanged': [i for i in xd_ids if i in sf or i in sc],
    'xd_historical_elements_checked_still_retired': [i for i in xd_ids if i in old_retired], 'workbook_sheets': len(names),
    'ooxml_tooling': 'Python standard library zipfile and ElementTree: source/result load, ZIP CRC, XML, internal relationships, cells and data correspondence PASS; not full ECMA-376 XSD or spreadsheet application validation.',
    'qualifications': ['Original instruction-entry timestamps and Task filename prefixes remain UNRESOLVED for SA treatment before HCRL closure.', 'External office application load/export and visual inspection remain UNRESOLVED; prior launch failures are not a workbook parse-failure finding.', 'Original source criteria are preserved; they do not alone prove every accepted refined responsibility or implementation conformance.', 'Bounded text assertions complement source/result semantic review; they do not establish whole-candidate architectural acceptance.'],
    'unresolved_architectural_conflicts': [], 'result_review': 'Solution Architect review pending; offline only'
}
(OUT / 'validation.json').write_text(json.dumps(summary, indent=2, ensure_ascii=False) + '\n')
print(json.dumps({'checks': len(checks), 'all_pass': summary['all_automated_checks_pass'], 'failed': [n for n, v in checks.items() if v != 'PASS'], 'before': summary['before'], 'after': summary['after'], 'hierarchies': summary['hierarchies']}, indent=2))
sys.exit(0 if summary['all_automated_checks_pass'] else 1)
