# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reproduce only the bounded Pass3/013 offline outputs, from repository root."""
from pathlib import Path
import copy
import csv
import hashlib
import json
import re
import shutil
from ooxml import Workbook, q, put

ROOT = Path('enterprise-architecture/design/evidence/1.1.20/001')
SOURCE = ROOT / 'after/pass3-xd-consolidation'
OUT = ROOT / 'after/pass3-sr-reconciliation'
CATALOGUE = 'Harmonia_1.1.20_Candidate_017_Pass3_013_SR_Reconciled_Catalogue.md'
WORKBOOK = 'Harmonia_1.1.20_Pass3_013_SR_Adjudication_Workbook.xlsx'
sha = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()
read = lambda p: json.loads(p.read_text())


def write(name, obj):
    (OUT / name).write_text(json.dumps(obj, indent=2, ensure_ascii=False) + '\n')


lock = read(ROOT / 'before/pass3-sr-reconciliation-input-manifest.json')
assert all(sha(Path(p)) == digest for p, digest in lock['inputs'].items()), 'Source changed'
prior_manifest = read(SOURCE / 'source-result-manifest.json')
source_cat = Path(prior_manifest['result_catalogue'])
source_book = Path(prior_manifest['result_workbook'])
changes = read(OUT / 'authoring/changes.json')
bf = read(SOURCE / 'feature-reconciliation.json')
bc = read(SOURCE / 'capability-reconciliation.json')
byf = {x['id']: x for x in bf}
byc = {x['id']: x for x in bc}
source_text = source_cat.read_text()
source_handover_block = re.search(r'^#### B05\.03\.04 — .*?(?=^#{1,4} |\Z)', source_text, re.M | re.S).group()
supporting_context = re.search(r'^\*Enterprise contributions:.*\*$', source_handover_block, re.M).group()
retired = {'B05.03.04'} | {f'B05.03.04.F{n:02}' for n in range(1, 6)}
features = [copy.deepcopy(x) for x in bf if x['id'] not in retired]
caps = [copy.deepcopy(x) for x in bc if x['id'] not in retired]
for x in caps:
    if x['id'] in changes['capabilities']:
        x['pass3_012'] = copy.deepcopy(byc[x['id']])
        x.update({k: v for k, v in changes['capabilities'][x['id']].items() if k != 'sr'})
        x['sr_decision'] = changes['capabilities'][x['id']]['sr']
        if x['id'] == 'B05.03.01':
            x['consolidated_supporting_context'] = supporting_context
for x in features:
    if x['parent'] in changes['capabilities']:
        x['pass3_012'] = copy.deepcopy(byf[x['id']])
        x['parent_name'] = changes['capabilities'][x['parent']]['name']
        x['sr_decision'] = changes['capabilities'][x['parent']]['sr']
    if x['id'] in changes['features']:
        x['description'] = changes['features'][x['id']]
        x['normalisation'] = 'Accepted-SR-refinement; prior AR/XD treatment retained in pass3_012'
        x['criterion_treatment'] = 'Original criterion retained for its existing obligation; accepted SR definition adds or clarifies responsibility. Original criterion alone does not assure the entire refined definition.'
        x['current_assurance_condition'] = x['description']
forbidden = set(changes['features']) - set(x['id'] for x in features)
assert not forbidden

transfer = []
for m in changes['sr001_transfers']:
    row = copy.deepcopy(m)
    row['decision'] = 'SR-001'
    row['status'] = 'Accepted and incorporated'
    row['source_record'] = copy.deepcopy(byf[m['source']])
    row['source_catalogue'] = str(source_cat)
    row['target_definitions'] = {x['id']: x['description'] for x in features if x['id'] in m['targets']}
    row['criterion_treatment'] = 'Retained assurance condition of consolidated responsibility; source identifier retired, criterion not discarded or asserted tested.'
    transfer.append(row)
    for x in features:
        if x['id'] in m['targets']:
            x.setdefault('consolidated_source_criteria', []).append({
                'source_feature': m['source'], 'source_criterion': row['source_record']['source_criterion'],
                'original_description': row['source_record']['original_description'],
                'original_status': row['source_record']['original_status'],
                'primary_criterion_target': m['primary_criterion_target']})
write('sr001-feature-reconciliation.json', transfer)
write('feature-reconciliation.json', features)
write('capability-reconciliation.json', caps)

# Restrict Markdown transformation to exact authorised definitions and retirement.
fp = re.compile(r'^- \*\*`([^`]+)` — (.*?):\*\* (.*)$')
hp = re.compile(r'^(#{2,4}) ([BE]\d+(?:\.\d+){0,2}) — (.*)$')
source_lines = source_text.splitlines(keepends=True)
new_lines = []
skip = False
next_description = None
for line in source_lines:
    h = hp.fullmatch(line.rstrip('\n'))
    if h:
        _, ident, _ = h.groups()
        skip = ident == 'B05.03.04'
        next_description = ident if ident in changes['capabilities'] else None
        if skip:
            continue
        if next_description:
            line = h[1] + ' ' + ident + ' — ' + changes['capabilities'][ident]['name'] + '\n'
    if skip:
        continue
    f = fp.fullmatch(line.rstrip('\n'))
    if f and f[1] in changes['features']:
        line = f'- **`{f[1]}` — {f[2]}:** {changes["features"][f[1]]}\n'
    elif next_description and not h and line.strip():
        line = changes['capabilities'][next_description]['description'] + '\n'
        if next_description == 'B05.03.01':
            line += '\n' + supporting_context + '\n'
        next_description = None
    new_lines.append(line)
text = ''.join(new_lines)
text = text.replace('**Offline working candidate — Pass 3/012.** Prepared under accepted XD-001–XD-028 Revision 2 and AR-001/AR-002.', '**Offline working candidate — Pass 3/013.** Prepared under accepted XD-001–XD-028 Revision 2, confirmed SR-001/SR-004/SR-015/SR-016/SR-017 and AR-001/AR-002.', 1)
text = text.replace('CT1 → CT2 → CT3 → Feature identities and containment are preserved.', 'CT1 → CT2 → CT3 → Feature identities and containment are preserved except for the expressly authorised clinical handover consolidation. Surviving identifiers are unchanged.', 1)
text = text.replace('Original source criteria are historical evidence where Revision 2 supersedes their meaning;', 'Original source criteria are historical evidence where accepted decisions supersede their meaning;', 1)
old = 'Pre-existing accepted-SR correspondence discrepancies are preserved in [the qualification register](sr-correspondence-qualifications.json); they are not represented as newly incorporated.'
new = 'The five SR correspondence discrepancies identified in Pass3/012 are reconciled under the confirmed decisions; see [the SR incorporation register](sr-incorporation.json) and [the clinical handover reconciliation](sr001-feature-reconciliation.json). Original criteria, source status and historical identities remain separately traceable; no implementation assurance is inferred.'
assert old in text
text = text.replace(old, new, 1)
(OUT / CATALOGUE).write_text(text)

sr = [
    {'id': 'SR-001', 'disposition': 'Implement accepted consolidation', 'elements': ['B05.03.01', 'B05.03.04'], 'incorporation': 'Consolidated five source Features into existing F01/F02/F04/F05, with identity assurance at F03 and rejection/escalation at F06. Retired source CT3 and its five Feature identities. Operational handovers unchanged.'},
    {'id': 'SR-004', 'disposition': 'Keep distinct; refine wording', 'elements': ['E06.03.01', 'E06.03.03'], 'incorporation': 'Applied accepted CT3 names and boundaries; refined handoff state/accountability, correlation and evidence. Destination selection, delivery attempt and accepted handoff remain distinct. All ten Feature identities/containments retained; XD-012 decision evidence unchanged.'},
    {'id': 'SR-015', 'disposition': 'Keep distinct; refine wording', 'elements': ['E06.08.07', 'E06.09.07'], 'incorporation': 'Applied outcome/continuation versus long-duration activity responsibilities and accepted names. Retained all ten recovery/subscription/lineage obligations and original criteria alongside Task/activity state and outcome semantics. No automatic extra Task or commitment inference.'},
    {'id': 'SR-016', 'disposition': 'Keep distinct; refine wording', 'elements': ['B13.04.02', 'B14.06.01'], 'incorporation': 'Applied research de-identification/pseudonymisation/minimisation and participant-contextual protection responsibilities. Retained all ten safeguarding/consent/risk/withdrawal/adverse-response obligations and original criteria. Research utility, re-identification risk, disclosure conditions and domain authority remain distinct.'},
    {'id': 'SR-017', 'disposition': 'Keep distinct; correct narrative correspondence', 'elements': ['E03.06.04', 'E03.06.05'], 'incorporation': 'Corrected current SR narrative: E03.06.04 exposes server interactions to external clients; E03.06.05 consumes external servers as client. All ten already correct Features, both CT3 definitions and containment preserved byte-for-byte.'}
]
for x in sr:
    x['status'] = 'Accepted and incorporated'
    x['authority'] = '../../before/pass3-sr-reconciliation-issued-directions.md'
    x['result_review'] = 'Solution Architect review pending; offline only'
    x['qualification'] = 'Original recovery/safeguarding obligations remain additional conditions; original criteria do not alone assure the expanded accepted scope.' if x['id'] in ('SR-015', 'SR-016') else ''
write('sr-incorporation.json', sr)

# Prior XD/history artefacts retain their bytes and relative retirement evidence.
for name in ['xd-incorporation.json', 'retirement-trace.json', 'source-editorial-notes.json', 'spreadsheet-application-check.json']:
    shutil.copyfile(SOURCE / name, OUT / name)
new_retirement = [{'source': 'B05.03.04', 'targets': ['B05.03.01'], 'decision': 'SR-001', 'source_record': byc['B05.03.04'], 'source_catalogue': str(source_cat), 'pass3_012_block': source_handover_block, 'supporting_context_transferred_unchanged': supporting_context}] + transfer
write('sr-retirement-trace.json', new_retirement)
status = [{**x, 'historical_issue': read(SOURCE / 'sr-correspondence-qualifications.json')[n][1], 'resolution': x['incorporation']} for n, x in enumerate(sr)]
status.append({'id': 'Temporal provenance', 'status': 'UNRESOLVED', 'resolution': 'Original instruction-entry event times and Task filename prefixes unavailable; SA treatment required before HCRL closure.'})
status.append({'id': 'Spreadsheet application', 'status': 'UNRESOLVED', 'resolution': 'Prior bounded LibreOffice launcher failures remain qualified. No new application load/export or visual inspection is claimed; current ZIP/XML and data checks are independent.'})
write('sr-correspondence-qualifications.json', status)

# Exactly one record for each baseline source Feature, including the five retired sources.
criteria = []
current = {x['id']: x for x in features}
transfer_by_id = {x['source']: x for x in transfer}
for original in bf:
    ident = original['id']
    m = transfer_by_id.get(ident)
    targets = m['targets'] if m else [ident]
    now = current[m['primary_criterion_target'] if m else ident]
    criteria.append({
        'source_feature': ident, 'active_features': targets,
        'primary_criterion_target': m['primary_criterion_target'] if m else ident,
        'original_description': original['original_description'],
        'source_criterion': original['source_criterion'],
        'measurement_criterion': original['measurement_criterion'],
        'criterion_treatment': m['criterion_treatment'] if m else now['criterion_treatment'],
        'current_assurance_condition': now['current_assurance_condition'],
        'original_status': original['original_status'],
        'implementation_verification': original['implementation_verification'],
        'pass3_012_description': original['description']})
write('criterion-reconciliation.json', criteria)
columns = list(criteria[0])
with (OUT / 'measurement-criteria.csv').open('w', newline='') as stream:
    writer = csv.DictWriter(stream, fieldnames=columns)
    writer.writeheader()
    for row in criteria:
        writer.writerow({**row, 'active_features': '; '.join(row['active_features'])})

definition_changes = []
for kind, old_rows, rows in [('CT3', byc, caps), ('Feature', byf, features)]:
    for x in rows:
        old = old_rows[x['id']]
        if (old['name'], old['description']) != (x['name'], x['description']):
            definition_changes.append({'id': x['id'], 'kind': kind, 'decision': x['sr_decision'], 'before_name': old['name'], 'after_name': x['name'], 'before_description': old['description'], 'after_description': x['description']})
write('definition-changes.json', definition_changes)

w = Workbook(source_book)
w.append('History P012 SR Decisions', w.data[w.path('SR Decisions')])
w.append('History P012 Exceptions', w.data[w.path('Reconciliation Exceptions')])
sr_by = {x['id']: x for x in sr}
narratives = {
    'SR-001': 'Clinical handover acceptance and evidence consolidate into B05.03.01; clinical responsibility remains distinct from operational allocation and human operational handover.',
    'SR-004': 'E06.03.01 governs Parcel handoff state/accountability; E06.03.03 applies routing decisions/destination selection. Selection, delivery attempt and accepted handoff are distinct.',
    'SR-015': 'E06.08.07 processes Ergon outcomes and Task continuation; E06.09.07 maintains continuing asynchronous activities within the applicable Task. Ergon/activity/Task/commitment outcomes are distinct.',
    'SR-016': 'B13.04.02 prepares research-purpose information with de-identification, pseudonymisation and minimisation, preserving utility and managing re-identification risk. B14.06.01 protects participant information in authorised access/presentation/disclosure; de-identification is not always required and does not authorise unrestricted disclosure.',
    'SR-017': 'E03.06.04: Harmonia subsystems act as FHIR RESTful servers exposing interactions to external FHIR clients. E03.06.05: Harmonia subsystems act as FHIR RESTful clients consuming services exposed by external FHIR servers.'
}
sheet = w.read('SR Decisions')
for row in sheet.find(q('sheetData')):
    vals = w.values(row)
    if vals and vals[0] in sr_by:
        x = sr_by[vals[0]]
        put(row, 5, narratives[x['id']])
        put(row, 6, x['incorporation'])
        put(row, 7, 'ACCEPTED AND INCORPORATED — offline Pass3/013; result review pending')
w.save_sheet('SR Decisions', sheet)
w.replace('Reconciliation Exceptions', [['Reference', 'Observed correspondence issue / qualification', 'Actual treatment / status']] + [[x['id'], x.get('historical_issue', x['resolution']), x['status'] + ': ' + x['resolution']] for x in status])
w.replace('Overview', [
    ['Harmonia 1.1.20 / Pass3 013', 'Offline SR reconciliation; Solution Architect result review pending'],
    ['Baseline', str(source_cat) + '; original bytes preserved; exact hashes in source-result-manifest.json'],
    ['Hierarchy', '24 CT1 / 145 CT2 / 585 CT3 / 2936 Features; one CT3 and five source Features retired under SR-001'],
    ['XD-001–XD-028', 'Accepted and incorporated in Pass3/012; all definitions and current XD/history sheets preserved'],
    ['SR-001/004/015/016/017', 'Accepted and incorporated offline; current narrative corrected; original SR register in History P012 SR Decisions'],
    ['AR-001 / AR-002', 'Canonical outcomes remain separate from original criteria and source status; no implementation assurance inferred'],
    ['Measurement criteria', '2941 source records: 2936 surviving Feature records and five consolidated source criteria linked to existing active targets'],
    ['Inherited obligations', 'Recovery/subscription and safeguarding obligations retained alongside accepted SR-015/SR-016 scope; source criteria alone do not assure every refined responsibility'],
    ['Retired identities', 'Prior XD retirement history unchanged; SR001 Merge Map and SR Retirement Trace preserve exact source records and criteria'],
    ['FR-001–FR-009 / E07.03.05 / FC-042,059,101', 'Original unresolved/excluded standing retained; no adjudication or closure'],
    ['Qualifications', 'Task instruction-entry times unresolved; external office application load/export and visual inspection unresolved'],
    ['Boundary', 'No Continuum or accepted release mutation, release allocation, HCRL completion/advancement, commit or push']])
w.replace('Feature Definitions', [['Feature ID', 'Parent CT3', 'Name', 'Canonical definition', 'Authoring treatment', 'Technical correspondence', 'SR decision', 'Pass3/012 name', 'Pass3/012 definition']] + [[x['id'], x['parent'], x['name'], x['description'], x['normalisation'], x['technical_correspondence'], x.get('sr_decision', ''), byf[x['id']]['name'], byf[x['id']]['description']] for x in features])
w.replace('Capability Definitions', [['Capability ID', 'Level', 'Name', 'Definition', 'Original name', 'Original definition', 'SR decision', 'Pass3/012 name', 'Pass3/012 definition']] + [[x['id'], x['level'], x['name'], x['description'], x['original_name'], x['original_description'], x.get('sr_decision', ''), byc[x['id']]['name'], byc[x['id']]['description']] for x in caps])
w.replace('Measurement Criteria', [columns] + [[('; '.join(x[col]) if col == 'active_features' else x[col]) for col in columns] for x in criteria])
retirement_sheet = w.read('Retirement Trace')
data = retirement_sheet.find(q('sheetData'))
from xml.etree import ElementTree as ET
for record in new_retirement:
    original = record['source_record']
    row = ET.SubElement(data, q('row'), r=str(len(data) + 1))
    vals = [record['source'], '; '.join(record['targets']), 'SR-001 accepted consolidation', original['name'], original['original_description'], str(source_cat)]
    for n, val in enumerate(vals, 1):
        put(row, n, val)
        row[-1].set('s', str(w.styles[1]))
auto = retirement_sheet.find(q('autoFilter'))
if auto is not None:
    auto.set('ref', f'A1:F{len(data)}')
w.save_sheet('Retirement Trace', retirement_sheet)
w.append('SR Incorporation', [['SR', 'Accepted disposition', 'State', 'Elements', 'Actual incorporation', 'Qualification']] + [[x['id'], x['disposition'], x['status'], '; '.join(x['elements']), x['incorporation'], x['qualification']] for x in sr])
w.append('SR001 Merge Map', [['Retired source', 'Active coverage', 'Primary criterion target', 'Unique obligation', 'Pass3/012 canonical definition', 'Original source description / status', 'Original source criterion', 'Actual target definitions']] + [[x['source'], '; '.join(x['targets']), x['primary_criterion_target'], x['obligation'], x['source_record']['description'], x['source_record']['original_description'], x['source_record']['source_criterion'], '\n'.join(k + ': ' + v for k, v in x['target_definitions'].items())] for x in transfer])
w.append('SR Retirement Trace', [['Retired source', 'Active coverage', 'Decision', 'Source name', 'Pass3/012 description', 'Original description', 'Original measurement', 'Original status', 'Source locator']] + [[x['source'], '; '.join(x['targets']), x['decision'], x['source_record']['name'], x['source_record']['description'], x['source_record']['original_description'], x['source_record'].get('measurement_criterion', ''), x['source_record'].get('original_status', ''), x['source_catalogue']] for x in new_retirement])
w.write(OUT / WORKBOOK)

write('source-result-manifest.json', {
    'baseline_catalogue': str(source_cat), 'baseline_catalogue_sha256': sha(source_cat),
    'baseline_workbook': str(source_book), 'baseline_workbook_sha256': sha(source_book),
    'baseline_package_manifest': str(SOURCE / 'source-result-manifest.json'),
    'baseline_package_manifest_sha256': sha(SOURCE / 'source-result-manifest.json'),
    'input_manifest': str(ROOT / 'before/pass3-sr-reconciliation-input-manifest.json'),
    'input_manifest_sha256': sha(ROOT / 'before/pass3-sr-reconciliation-input-manifest.json'),
    'operative_directions': str(ROOT / 'before/pass3-sr-reconciliation-issued-directions.md'),
    'operative_directions_sha256': sha(ROOT / 'before/pass3-sr-reconciliation-issued-directions.md'),
    'result_catalogue': str(OUT / CATALOGUE), 'result_catalogue_sha256': sha(OUT / CATALOGUE),
    'result_workbook': str(OUT / WORKBOOK), 'result_workbook_sha256': sha(OUT / WORKBOOK),
    'before': {'ct1': 24, 'ct2': 145, 'ct3': 586, 'features': 2941},
    'after': {'ct1': 24, 'ct2': 145, 'ct3': 585, 'features': 2936},
    'retired': sorted(retired), 'added_identifiers': [],
    'reproduction': 'PYTHONDONTWRITEBYTECODE=1 python3 ' + str(OUT / 'authoring/build_result.py'),
    'evidence_sha256': {str(p.relative_to(OUT)): sha(p) for p in sorted(OUT.rglob('*')) if p.is_file() and p.name not in {'source-result-manifest.json', 'validation.json', 'reconciliation-report.md'} and '__pycache__' not in str(p)}
})
print('Produced Pass3/013:', len(caps), 'capabilities;', len(features), 'Features;', len(w.sheets), 'workbook sheets;', len(definition_changes), 'definition changes.')
