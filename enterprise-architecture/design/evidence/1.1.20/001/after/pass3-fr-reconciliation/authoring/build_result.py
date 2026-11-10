# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reproduce the authorised offline Pass3/014; never writes maintained machinery."""
import copy
import csv
import hashlib
import json
from pathlib import Path
import re
import sys

ROOT = Path('enterprise-architecture/design/evidence/1.1.20/001')
SOURCE = ROOT / 'after/pass3-sr-reconciliation'
OUT = ROOT / 'after/pass3-fr-reconciliation'
# Reuse the predecessor's bounded OOXML adapter, pinned by the input manifest.
sys.path.insert(0, str(SOURCE / 'authoring'))
from ooxml import Workbook, q

CATALOGUE = 'Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md'
WORKBOOK = 'Harmonia_1.1.20_Pass3_014_FR_Adjudication_Workbook.xlsx'
PACKAGE = ROOT / 'before/Harmonia_1.1.20_FR001-FR009_Accepted_Adjudication_Package.md'
TASK = ROOT / 'before/ai-agent-tasks/unresolved-pass3-fr-reconciliation.md'
LOCK = ROOT / 'before/pass3-fr-reconciliation-input-manifest.json'
read = lambda p: json.loads(p.read_text())
sha = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()


def write(name, value):
    (OUT / name).write_text(json.dumps(value, indent=2, ensure_ascii=False) + '\n')


lock = read(LOCK)
assert all(sha(Path(p)) == digest for p, digest in lock['inputs'].items()), 'Input changed'
manifest = read(SOURCE / 'source-result-manifest.json')
source_cat = Path(manifest['result_catalogue'])
source_book = Path(manifest['result_workbook'])
changes = read(OUT / 'authoring/changes.json')
bf = read(SOURCE / 'feature-reconciliation.json')
bc = read(SOURCE / 'capability-reconciliation.json')
byf = {x['id']: x for x in bf}
byc = {x['id']: x for x in bc}
retired = changes['retired']
assert set(changes['features']) <= byf.keys() and set(retired) <= byf.keys()
features = [copy.deepcopy(x) for x in bf if x['id'] not in retired]
caps = copy.deepcopy(bc)
treatment = ('Original source criterion retained as historical assessment evidence; '
             'does not override the accepted FR definition or alone assure all refined scope. '
             'No implementation verification, new KPI or threshold is asserted.')
for x in features:
    if x['id'] in changes['features']:
        x['pass3_013'] = copy.deepcopy(byf[x['id']])
        x.update(changes['features'][x['id']])
        x['normalisation'] = 'Accepted-FR-refinement; prior AR/XD/SR treatment retained in pass3_013'
        x['criterion_treatment'] = treatment
        x['current_assurance_condition'] = x['description']
for x in caps:
    if x['id'] in changes['capabilities']:
        x['pass3_013'] = copy.deepcopy(byc[x['id']])
        x.update(changes['capabilities'][x['id']])
current = {x['id']: x for x in features}
history = []
for ident, disposition in retired.items():
    target = disposition['target']
    record = {'source': ident, 'targets': [target], 'primary_criterion_target': target,
              'decision': disposition['decision'], 'status': 'Accepted and incorporated offline',
              'source_record': byf[ident], 'source_catalogue': str(source_cat),
              'target_definition': current[target]['description'], 'criterion_treatment': treatment}
    history.append(record)
    current[target].setdefault('consolidated_source_criteria', []).append({
        'source_feature': ident, 'source_criterion': byf[ident]['source_criterion'],
        'original_description': byf[ident]['original_description'],
        'original_status': byf[ident]['original_status'], 'primary_criterion_target': target})
write('feature-reconciliation.json', features)
write('capability-reconciliation.json', caps)
write('fr-retirement-trace.json', history)

# Retain all 2,941 source criteria, including previously retired SR sources.
criteria = read(SOURCE / 'criterion-reconciliation.json')
for x in criteria:
    targets = [retired.get(i, {}).get('target', i) for i in x['active_features']]
    x['active_features'] = list(dict.fromkeys(targets))
    primary = x['primary_criterion_target']
    x['primary_criterion_target'] = retired.get(primary, {}).get('target', primary)
    if x['source_feature'] in changes['features'] or x['source_feature'] in retired:
        x['pass3_013_assessment'] = next(y for y in read(SOURCE / 'criterion-reconciliation.json')
                                          if y['source_feature'] == x['source_feature'])
        x['criterion_treatment'] = treatment
        x['current_assurance_condition'] = current[x['primary_criterion_target']]['description']
        x['fr_decisions'] = (changes['features'][x['source_feature']]['fr_decisions']
                             if x['source_feature'] in changes['features'] else ['FR-002'])
write('criterion-reconciliation.json', criteria)
columns = list(dict.fromkeys(k for x in criteria for k in x))
with (OUT / 'measurement-criteria.csv').open('w', newline='') as stream:
    writer = csv.DictWriter(stream, fieldnames=columns)
    writer.writeheader()
    for row in criteria:
        writer.writerow({k: json.dumps(v, ensure_ascii=False) if isinstance(v, (list, dict)) else v
                         for k, v in row.items()})

source_text = source_cat.read_text()
lines = []
cap = None
for line in source_text.splitlines(keepends=True):
    f = re.fullmatch(r'- \*\*`([^`]+)` — (.*?):\*\* (.*)\n?', line)
    h = re.fullmatch(r'#### ([BE][0-9.]+) — (.*)\n?', line)
    if h:
        cap = h[1] if h[1] in changes['capabilities'] else None
    elif cap and line.strip():
        line = changes['capabilities'][cap]['description'] + '\n'
        cap = None
    if f:
        if f[1] in retired:
            continue
        if f[1] in changes['features']:
            x = changes['features'][f[1]]
            line = f'- **`{f[1]}` — {x["name"]}:** {x["description"]}\n'
    lines.append(line)
text = ''.join(lines)
text = text.replace('Pass 3/013.', 'Pass 3/014.', 1)
text = text.replace('and AR-001/AR-002.', 'accepted FR-001–FR-009 and AR-001/AR-002.', 1)
text = text.replace('SR-017 accepted FR-', 'SR-017, accepted FR-', 1)
text = text.replace('except for the expressly authorised clinical handover consolidation.',
                    'except for the expressly authorised clinical handover consolidation and FR-002 Feature retirement.', 1)
old = "FR-001–FR-009, E07.03.05's separate semantic review and FC-042/059/101 retain their existing unresolved/excluded standing. No completion of those workstreams is implied."
new = ("FR-001–FR-009 are incorporated under the accepted package and explicit offline Pass3/014 disposition; "
       "see [the nine-decision incorporation trace](fr-incorporation.json) and "
       "[FR-002 retirement evidence](fr-retirement-trace.json). E07.03.05's separate semantic review "
       "and FC-042/059/101 retain their existing unresolved/excluded standing. No completion of those workstreams is implied.")
assert old in text
text = text.replace(old, new, 1)
for name in ['sr-incorporation.json', 'sr001-feature-reconciliation.json']:
    text = text.replace('](' + name + ')', '](../pass3-sr-reconciliation/' + name + ')')
(OUT / CATALOGUE).write_text(text)

definition_changes = []
for kind, olds, rows in [('CT3', byc, caps), ('Feature', byf, features)]:
    for x in rows:
        old = olds[x['id']]
        if (old['name'], old['description']) != (x['name'], x['description']):
            definition_changes.append({'id': x['id'], 'kind': kind, 'fr_decisions': x['fr_decisions'],
                                       'before_name': old['name'], 'after_name': x['name'],
                                       'before_description': old['description'], 'after_description': x['description']})
write('definition-changes.json', definition_changes)
decisions = copy.deepcopy(changes['decisions'])
for d in decisions:
    if d['id'] == 'FR-001':
        d['identifiers'] = ['E03.03.03.F01', 'E03.03.03.F02']
        d['preserved_adjacent_boundary'] = 'E03.03.04.F01'
    d['changed_features'] = [x['id'] for x in definition_changes
                             if x['kind'] == 'Feature' and d['id'] in x['fr_decisions']]
    d['changed_capabilities'] = [x['id'] for x in definition_changes
                                 if x['kind'] == 'CT3' and d['id'] in x['fr_decisions']]
    d['retired'] = [i for i, v in retired.items() if v['decision'] == d['id']]
    d['authority'] = str(PACKAGE)
    d['authority_selector'] = '§3 / ' + d.pop('authority_section').upper()
    d['scope_disposition'] = str(TASK)
    d['state'] = 'Accepted decision incorporated offline; resulting candidate review pending'
write('fr-incorporation.json', decisions)

# Preserve the predecessor workbook and all unaffected worksheets in the successor.
w = Workbook(source_book)
w.append('History P013 Feature Duplicates', w.data[w.path('Feature Duplicates')])
screening = w.rows('Feature Duplicates')
head = screening[0]
for row in screening[1:]:
    ident = row[head.index('Feature Review ID')]
    if ident not in {d['id'] for d in decisions}:
        continue
    d = next(d for d in decisions if d['id'] == ident)
    row[head.index('SuggestedDisposition')] = 'HISTORICAL SCREENING PROPOSAL — superseded for ' + ident
    row[head.index('Standing')] = 'HISTORICAL PROPOSAL — accepted FR Decisions governs'
    row[head.index('Reviewer Decision')] = d['disposition']
    row[head.index('Reviewer Comment')] = 'Accepted package §3; applied offline Pass3/014; result review pending'
    row[head.index('Adjudication Standing')] = 'ACCEPTED AND INCORPORATED OFFLINE'
w.replace('Feature Duplicates', screening)
w.replace('Feature Definitions', [['Feature ID', 'Parent CT3', 'Name', 'Canonical definition', 'Authoring treatment', 'Technical correspondence', 'SR decision', 'FR decisions', 'Pass3/013 name', 'Pass3/013 definition']] + [[x['id'], x['parent'], x['name'], x['description'], x['normalisation'], x['technical_correspondence'], x.get('sr_decision', ''), '; '.join(x.get('fr_decisions', [])), byf[x['id']]['name'], byf[x['id']]['description']] for x in features])
w.replace('Capability Definitions', [['Capability ID', 'Level', 'Name', 'Definition', 'Original name', 'Original definition', 'SR decision', 'FR decisions', 'Pass3/013 name', 'Pass3/013 definition']] + [[x['id'], x['level'], x['name'], x['description'], x['original_name'], x['original_description'], x.get('sr_decision', ''), '; '.join(x.get('fr_decisions', [])), byc[x['id']]['name'], byc[x['id']]['description']] for x in caps])
w.replace('Measurement Criteria', [columns] + [[json.dumps(x.get(col, ''), ensure_ascii=False) if isinstance(x.get(col), (list, dict)) else x.get(col, '') for col in columns] for x in criteria])
retirement_rows = w.rows('Retirement Trace')
for x in history:
    original = x['source_record']
    retirement_rows.append([x['source'], '; '.join(x['targets']), 'FR-002 accepted consolidation', original['name'], original['original_description'], str(source_cat)])
w.replace('Retirement Trace', retirement_rows)
w.append('FR Decisions', [['FR', 'Accepted disposition', 'Meaning / distinction', 'Reviewed identifiers', 'Changed Features', 'Changed CT3', 'Retired', 'State', 'Authority']] + [[d['id'], d['disposition'], d['subject'], '; '.join(d['identifiers']), '; '.join(d['changed_features']), '; '.join(d['changed_capabilities']), '; '.join(d['retired']), d['state'], d['authority']] for d in decisions])
w.append('FR Retirement Trace', [['Retired source', 'Active survivor', 'Decision', 'Source name', 'Pass3/013 definition', 'Original description', 'Original criterion', 'Original status', 'Active definition', 'Source locator']] + [[x['source'], '; '.join(x['targets']), x['decision'], x['source_record']['name'], x['source_record']['description'], x['source_record']['original_description'], x['source_record']['source_criterion'], x['source_record']['original_status'], x['target_definition'], x['source_catalogue']] for x in history])
overview = w.rows('Overview')
for row in overview:
    if row[0] == 'Harmonia 1.1.20 / Pass3 013':
        row[:] = ['Harmonia 1.1.20 / Pass3 014', 'Offline FR reconciliation; Solution Architect result review pending']
    elif row[0] == 'Baseline':
        row[1] = str(source_cat) + '; original bytes preserved; exact hashes in source-result-manifest.json'
    elif row[0] == 'Hierarchy':
        row[1] = '24 CT1 / 145 CT2 / 585 CT3 / 2935 active Features; only B11.05.01.F02 retired in this transition'
    elif row[0] == 'Measurement criteria':
        row[1] = '2941 source records: 2935 active identities and six consolidated sources mapped to active survivors; original criteria/status preserved'
    elif row[0] == 'Retired identities':
        row[1] += '; FR Retirement Trace preserves B11.05.01.F02 consolidated into F01'
    elif row[0].startswith('FR-001'):
        row[:] = ['FR-001–FR-009', 'All nine accepted decisions incorporated; original proposals retained in History P013 Feature Duplicates; FR Decisions controls the current result']
overview += [['Excluded work', 'E07.03.05 and FC-042/059/101 retain their independent standing; no AR-003, protocol, AI authority or canonical incorporation'],
             ['Source criteria', treatment],
             ['Scope disposition', 'Solution Architect: Authorise offline Pass3/014 reconciliation; preserve the Continuum']]
w.replace('Overview', overview)
w.write(OUT / WORKBOOK)

def population(fs, cs):
    return {**{f'ct{n}': sum(x['level'] == n for x in cs) for n in (1, 2, 3)}, 'features': len(fs)}

generated = [CATALOGUE, WORKBOOK, 'feature-reconciliation.json', 'capability-reconciliation.json',
             'criterion-reconciliation.json', 'measurement-criteria.csv', 'definition-changes.json',
             'fr-incorporation.json', 'fr-retirement-trace.json', 'authoring/changes.json',
             'authoring/build_result.py', 'authoring/verify_result.py', 'authoring/README.md']
write('source-result-manifest.json', {
    'standing': 'Offline candidate; Solution Architect result review pending',
    'baseline_catalogue': str(source_cat), 'baseline_catalogue_sha256': sha(source_cat),
    'baseline_workbook': str(source_book), 'baseline_workbook_sha256': sha(source_book),
    'baseline_package_manifest': str(SOURCE / 'source-result-manifest.json'),
    'baseline_package_manifest_sha256': sha(SOURCE / 'source-result-manifest.json'),
    'input_manifest': str(LOCK), 'input_manifest_sha256': sha(LOCK),
    'operative_package': str(PACKAGE), 'operative_package_sha256': sha(PACKAGE),
    'scope_disposition': str(TASK), 'scope_disposition_sha256': sha(TASK),
    'result_catalogue': str(OUT / CATALOGUE), 'result_catalogue_sha256': sha(OUT / CATALOGUE),
    'result_workbook': str(OUT / WORKBOOK), 'result_workbook_sha256': sha(OUT / WORKBOOK),
    'before': population(bf, bc), 'after': population(features, caps),
    'retirement_register_before': {'features': sum('.F' in r[0] for r in retirement_rows[1:-1]), 'ct3': sum('.F' not in r[0] for r in retirement_rows[1:-1])},
    'retirement_register_after': {'features': sum('.F' in r[0] for r in retirement_rows[1:]), 'ct3': sum('.F' not in r[0] for r in retirement_rows[1:])},
    'retired': list(retired), 'added_identifiers': [],
    'historical_retirements': ['../pass3-sr-reconciliation/retirement-trace.json', '../pass3-sr-reconciliation/sr-retirement-trace.json'],
    'reproduction': 'PYTHONDONTWRITEBYTECODE=1 python3 ' + str(OUT / 'authoring/build_result.py'),
    'evidence_sha256': {name: sha(OUT / name) for name in generated}})
print('Produced Pass3/014:', population(features, caps), ';', len(definition_changes),
      'refined definitions;', len(w.sheets), 'workbook sheets.')
