# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded source/result preservation checks; not implementation acceptance."""
from collections import Counter
import csv
import hashlib
import json
from pathlib import Path
import posixpath
import re
import sys
import xml.etree.ElementTree as ET
import zipfile

ROOT = Path('enterprise-architecture/design/evidence/1.1.20/001')
SOURCE = ROOT / 'after/pass3-sr-reconciliation'
OUT = ROOT / 'after/pass3-fr-reconciliation'
sys.path.insert(0, str(SOURCE / 'authoring'))
from ooxml import Workbook, q, col_number

read = lambda p: json.loads(p.read_text())
sha = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()
manifest = read(OUT / 'source-result-manifest.json')
lock = read(ROOT / 'before/pass3-fr-reconciliation-input-manifest.json')
changes = read(OUT / 'authoring/changes.json')
bf = {x['id']: x for x in read(SOURCE / 'feature-reconciliation.json')}
bc = {x['id']: x for x in read(SOURCE / 'capability-reconciliation.json')}
tf = {x['id']: x for x in read(OUT / 'feature-reconciliation.json')}
tc = {x['id']: x for x in read(OUT / 'capability-reconciliation.json')}
criteria = read(OUT / 'criterion-reconciliation.json')
prior_criteria = read(SOURCE / 'criterion-reconciliation.json')
retirement = read(OUT / 'fr-retirement-trace.json')
decisions = read(OUT / 'fr-incorporation.json')
before = Path(manifest['baseline_catalogue']).read_text()
after = Path(manifest['result_catalogue']).read_text()
checks = {}


def check(name, passed):
    checks[name] = 'PASS' if passed else 'FAIL'


fp = re.compile(r'^- \*\*`([^`]+)` — (.*?):\*\* (.*)$', re.M)
hp = re.compile(r'^(#{2,4}) ([BE]\d+(?:\.\d+){0,2}) — (.*)$', re.M)


def parse(text):
    fs = {i: (n, d) for i, n, d in fp.findall(text)}
    cs, blocks, parents, errors = {}, {}, {}, []
    heads = list(hp.finditer(text))
    for n, head in enumerate(heads):
        block = text[head.start():heads[n + 1].start() if n + 1 < len(heads) else len(text)]
        blocks[head[2]] = block
        cs[head[2]] = (len(head[1]) - 1, head[3], next(s for s in block.splitlines()[1:] if s.strip()))
    for line in text.splitlines():
        h, f = hp.fullmatch(line), fp.fullmatch(line)
        if h:
            level = len(h[1]) - 1
            if level > 1 and h[2].rsplit('.', 1)[0] != parents.get(level - 1):
                errors.append(h[2])
            parents[level] = h[2]
        if f and f[1].rsplit('.', 1)[0] != parents.get(3):
            errors.append(f[1])
    return fs, cs, blocks, errors


sf, sc, sb, se = parse(before)
af, ac, ab, ae = parse(after)
population = lambda fs, cs: {**{f'ct{n}': sum(x[0] == n for x in cs.values()) for n in (1, 2, 3)}, 'features': len(fs)}
check('all_pinned_inputs_unchanged', all(sha(Path(p)) == digest for p, digest in lock['inputs'].items()))
check('all_result_manifest_hashes_match', all(sha(OUT / p) == digest for p, digest in manifest['evidence_sha256'].items()))
check('source_result_catalogue_and_workbook_hashes_match', all(sha(Path(manifest[k])) == manifest[k + '_sha256'] for k in ['baseline_catalogue', 'baseline_workbook', 'result_catalogue', 'result_workbook']))
check('verified_source_population', population(sf, sc) == {'ct1': 24, 'ct2': 145, 'ct3': 585, 'features': 2936})
check('result_population_and_sole_minus_one_delta', population(af, ac) == {'ct1': 24, 'ct2': 145, 'ct3': 585, 'features': 2935})
check('only_B110501F02_retired_no_new_ids', set(sf) - set(af) == {'B11.05.01.F02'} and not set(af) - set(sf) and set(sc) == set(ac))
check('hierarchy_order_and_surviving_numbering_preserved', list(sc) == list(ac) and [i for i in sf if i != 'B11.05.01.F02'] == list(af))
check('unique_ids', len(af) == len(fp.findall(after)) and len(ac) == len(hp.findall(after)) and not set(af) & set(ac))
check('source_and_result_hierarchy_containment_valid', not se and not ae)
check('all_parent_containment_unchanged', all(x['parent'] == bf[i]['parent'] and x['parent'] in tc and tc[x['parent']]['level'] == 3 for i, x in tf.items()))
check('registers_and_catalogue_correspond_exactly', set(tf) == set(af) and set(tc) == set(ac) and all((x['name'], x['description']) == af[i] and x['parent_name'] == tc[x['parent']]['name'] for i, x in tf.items()) and all((x['level'], x['name'], x['description']) == ac[i] for i, x in tc.items()))
check('exact_sixteen_surviving_feature_changes', {i for i in af if sf[i] != af[i]} == set(changes['features']) and len(changes['features']) == 16)
check('only_FR003_parent_refined', {i for i in ac if sc[i] != ac[i]} == {'E07.02.02'} and all(sc[i][0:2] == ac[i][0:2] for i in ac))
check('all_unaffected_feature_records_unchanged', all(x == bf[i] for i, x in tf.items() if i not in changes['features']))
check('all_unaffected_capability_records_unchanged', all(x == bc[i] for i, x in tc.items() if i != 'E07.02.02'))
affected_parents = {bf[i]['parent'] for i in set(changes['features']) | {'B11.05.01.F02'}}
check('all_unaffected_catalogue_blocks_byte_preserved', all(sb[i] == ab[i] for i in ac if i not in affected_parents))
notes = re.compile(r'^\*(?:Demand:|Enterprise contributions:).*$', re.M)
check('all_demand_contribution_findings_annotations_preserved', Counter(notes.findall(before)) == Counter(notes.findall(after)))

# Compare against independently re-extracted issued wording, not just authoring JSON.
package = Path(manifest['operative_package']).read_text()
accepted = {i: (n, d) for i, n, d in re.findall(r'^- \*\*(?:Retain )?`([^`]+) — ([^`]+)`\*\*\s*\n  \*\*Canonical semantic definition:\*\* (.+)$', package, re.M)}
check('all_sixteen_names_and_definitions_match_accepted_package_exactly', len(accepted) == 16 and all(af.get(i) == value for i, value in accepted.items()))
check('FR006_FR009_share_single_unchanged_F01_wording', changes['features']['E07.04.01.F01']['fr_decisions'] == ['FR-006', 'FR-009'] and af['E07.04.01.F01'] == accepted['E07.04.01.F01'] and af['E07.04.01.F01'][1] != af['E07.04.01.F05'][1])
check('FR003_parent_includes_programmatic_human_and_distinct_authority', all(s in ac['E07.02.02'][2] for s in ['programmatic participants', 'human participants', 'distinct interaction and authority requirements', 'automated confirmation is equivalent to human authority']))
check('FR001_adjacent_payload_retention_unchanged', sb['E03.03.04'] == ab['E03.03.04'] and sf['E03.03.04.F01'] == af['E03.03.04.F01'])
check('all_neighbouring_feature_definitions_preserved', all(sf[i] == af[i] for p in affected_parents for i in af if i.startswith(p + '.') and i not in accepted))
check('canonical_refinements_separate_from_status_and_metrics', all(not re.search(r'PROPOSED|unverified|proportion of|\d+\s*%', n + ' ' + d, re.I) for n, d in accepted.values()) and not re.search(r'PROPOSED|unverified|proportion of', ac['E07.02.02'][2], re.I))
check('exact_nine_FR_decisions_traceable', [x['id'] for x in decisions] == [f'FR-{i:03}' for i in range(1, 10)] and all(x['scope_disposition'] == manifest['scope_disposition'] for x in decisions))
check('retirement_preserves_exact_source_record_and_survivor', len(retirement) == 1 and retirement[0]['source'] == 'B11.05.01.F02' and retirement[0]['source_record'] == bf['B11.05.01.F02'] and retirement[0]['targets'] == ['B11.05.01.F01'] and retirement[0]['target_definition'] == af['B11.05.01.F01'][1])
check('retired_source_criterion_attached_to_active_survivor', any(x['source_feature'] == 'B11.05.01.F02' and x['source_criterion'] == bf['B11.05.01.F02']['source_criterion'] for x in tf['B11.05.01.F01']['consolidated_source_criteria']))
check('all_2941_source_criteria_preserved_in_order', len(criteria) == len(prior_criteria) == 2941 and [x['source_feature'] for x in criteria] == [x['source_feature'] for x in prior_criteria])
fields = ['source_criterion', 'measurement_criterion', 'original_description', 'original_status', 'implementation_verification', 'pass3_012_description']
check('original_assessments_and_status_verbatim', all(all(x[k] == old[k] for k in fields) for x, old in zip(criteria, prior_criteria)))
check('all_criterion_targets_active', all(set(x['active_features']) <= tf.keys() and x['primary_criterion_target'] in x['active_features'] for x in criteria))
check('only_FR002_criterion_targets_changed', [x['source_feature'] for x, old in zip(criteria, prior_criteria) if (x['active_features'], x['primary_criterion_target']) != (old['active_features'], old['primary_criterion_target'])] == ['B11.05.01.F02'])
check('prior_SR_criterion_mappings_unchanged', all(x == old for x, old in zip(criteria, prior_criteria) if x['source_feature'].startswith('B05.03.04.')))
check('changed_assessments_retain_complete_previous_record', all(x.get('pass3_013_assessment') == old for x, old in zip(criteria, prior_criteria) if x['source_feature'] in set(accepted) | {'B11.05.01.F02'}))
with (OUT / 'measurement-criteria.csv').open(newline='') as stream:
    csv_rows = list(csv.DictReader(stream))
check('CSV_all_source_criteria_and_targets_correspond', len(csv_rows) == len(criteria) and all(all(row[k] == x[k] for k in fields) and json.loads(row['active_features']) == x['active_features'] for row, x in zip(csv_rows, criteria)))

sw = Workbook(Path(manifest['baseline_workbook']))
tw = Workbook(Path(manifest['result_workbook']))
changed_sheets = {'Overview', 'Feature Duplicates', 'Feature Definitions', 'Capability Definitions', 'Measurement Criteria', 'Retirement Trace'}
check('all_unaffected_workbook_sheets_byte_preserved', all(sw.data[sw.path(s.get('name'))] == tw.data[tw.path(s.get('name'))] for s in sw.sheets if s.get('name') not in changed_sheets))
check('screening_proposals_preserved_as_exact_historical_sheet', sw.data[sw.path('Feature Duplicates')] == tw.data[tw.path('History P013 Feature Duplicates')])
check('workbook_sheet_population_and_unique_names', len(tw.sheets) == 32 and len({s.get('name') for s in tw.sheets}) == 32 and all(len(s.get('name')) <= 31 for s in tw.sheets))
book_features = tw.rows('Feature Definitions')[1:]
book_caps = tw.rows('Capability Definitions')[1:]
check('active_workbook_features_exactly_match_current_catalogue', len(book_features) == len(tf) and {r[0] for r in book_features} == tf.keys() and all((r[2], r[3]) == af[r[0]] and r[1] == tf[r[0]]['parent'] for r in book_features))
check('active_workbook_capabilities_exactly_match_current_catalogue', len(book_caps) == len(tc) and all((int(r[1]), r[2], r[3]) == ac[r[0]] for r in book_caps))
book_criteria = tw.rows('Measurement Criteria')
check('workbook_measurements_preserve_all_original_criteria', len(book_criteria) == 2942 and all(r[0] == x['source_feature'] and r[book_criteria[0].index('source_criterion')] == x['source_criterion'] and json.loads(r[1]) == x['active_features'] for r, x in zip(book_criteria[1:], criteria)))
check('workbook_retirement_history_appended_without_loss', tw.rows('Retirement Trace')[:-1] == sw.rows('Retirement Trace') and tw.rows('Retirement Trace')[-1][0:2] == ['B11.05.01.F02', 'B11.05.01.F01'])
old_inactive = {r[0] for r in sw.rows('Retirement Trace')[1:]}
new_inactive = {r[0] for r in tw.rows('Retirement Trace')[1:]}
check('offline_retirement_population_14_to_15_features_2_CT3_unchanged', len(old_inactive) == 16 and len(new_inactive) == 17 and sum('.F' in i for i in old_inactive) == 14 and sum('.F' in i for i in new_inactive) == 15 and new_inactive - old_inactive == {'B11.05.01.F02'} and not new_inactive & (set(tf) | set(tc)))
check('workbook_nine_decisions_and_one_retirement', len(tw.rows('FR Decisions')) == 10 and len(tw.rows('FR Retirement Trace')) == 2)
screen = tw.rows('Feature Duplicates')
check('current_screening_rows_explicitly_superseded', all('HISTORICAL' in r[5] and r[12] == 'ACCEPTED AND INCORPORATED OFFLINE' for r in screen[1:] if r[11] in {x['id'] for x in decisions}))
xml_ok = relationships_ok = cells_ok = True
with zipfile.ZipFile(Path(manifest['result_workbook'])) as z:
    names = set(z.namelist())
    for name in names:
        if name.endswith(('.xml', '.rels')):
            root = ET.fromstring(z.read(name))
            if name.endswith('.rels'):
                base = '' if name == '_rels/.rels' else posixpath.dirname(name).removesuffix('/_rels')
                for rel in root:
                    if rel.get('TargetMode') == 'External':
                        continue
                    target = rel.get('Target', '')
                    path = target.lstrip('/') if target.startswith('/') else posixpath.normpath(posixpath.join(base, target))
                    relationships_ok &= path in names
    for sheet in tw.sheets:
        for row in tw.read(sheet.get('name')).find(q('sheetData')):
            refs = [c.get('r') for c in row]
            cells_ok &= len(refs) == len(set(refs)) and all(ref.endswith(row.get('r')) for ref in refs)
check('OOXML_ZIP_XML_and_internal_relationships_valid', xml_ok and relationships_ok)
check('worksheet_cells_unique_and_row_aligned', cells_ok)
check('all_catalogue_relative_links_resolve', all((OUT / target.split('#')[0]).is_file() for target in re.findall(r'\]\(([^)]+)\)', after) if not target.startswith(('https:', '#'))))
check('no_retired_active_catalogue_references', 'B11.05.01.F02' not in after)
protected_ok = all(Path(p).is_file() and sha(Path(p)) == digest for p, digest in lock['protected_files'].items())
folders = ['enterprise-architecture/continuum', 'enterprise-architecture/design/archimate/published', 'enterprise-architecture/design/register', 'enterprise-architecture/documentation']
current_paths = {str(p) for d in folders for p in Path(d).rglob('*') if p.is_file()} | {'architecture-release.json'}
check('Continuum_publications_register_release_exactly_unchanged', protected_ok and current_paths == lock['protected_files'].keys())
check('accepted_predecessor_immutable_hash_verified', sha(Path(lock['accepted_predecessor']['publication'])) == lock['accepted_predecessor']['sha256'])
check('root_release_remains_1_1_20_from_1_1_19', read(Path('architecture-release.json')) == {'version': '1.1.20', 'previous_version': '1.1.19'})
result = {'standing': 'Automated offline source/result checks only; no implementation, office application or SA acceptance',
          'before': population(sf, sc), 'after': population(af, ac), 'checks': checks,
          'summary': dict(Counter(checks.values())),
          'qualifications': ['Instruction-entry event times/prefix unresolved for SA treatment before closure.',
                             'Full OOXML XSD and external office application load/export/visual inspection not established.',
                             'Original criteria retained; they do not alone assure all refined responsibility.']}
(OUT / 'validation.json').write_text(json.dumps(result, indent=2, ensure_ascii=False) + '\n')
print(json.dumps(result['summary']))
for name, outcome in checks.items():
    if outcome != 'PASS':
        print(outcome, name)
sys.exit(0 if all(value == 'PASS' for value in checks.values()) else 1)
