# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded documentation/release preservation check; no ingestion implementation."""
from pathlib import Path
import hashlib
import json
import re
import subprocess
import sys
from urllib.parse import unquote, urlsplit

ROOT = Path.cwd()
EVIDENCE = ROOT / '.junie/reports/2026-10-18-archimate-ingestion-change-review'
sys.path.insert(0, str(ROOT / 'scripts'))
from rads_business_architecture import anchors

documents = [
    'docs/markdown/governance/archimate-ingestion-change-review.md',
    'docs/markdown/governance/templates/archimate-ingestion-change-review-template.md',
    'docs/markdown/governance/README.md',
    'docs/archimate/README.md',
    'docs/archimate/framework/README.md',
    'docs/archimate/processing/ingestion/README.md',
    '.junie/reports/2026-10-18-archimate-ingestion-change-review.md',
]
errors = []
links = []
for name in documents:
    path = ROOT / name
    if not path.is_file():
        errors.append(f'Missing document: {name}')
        continue
    content = path.read_text()
    # The governed source-anchor helper checks explicit IDs and Markdown slugs.
    rendered = re.sub(r'(?ms)^```[^\n]*\n.*?^```[ \t]*$', '', content)
    for target in re.findall(r'(?<!!)\[[^\]\n]+\]\(([^)\n]+)\)', rendered):
        ref = urlsplit(target)
        if ref.scheme or ref.netloc:
            continue
        destination = (path.parent / unquote(ref.path)).resolve() if ref.path else path
        if not destination.exists():
            errors.append(f'{name}: missing local link {target}')
        elif ref.fragment and destination.is_file():
            if unquote(ref.fragment) not in anchors(destination.read_text()):
                errors.append(f'{name}: missing anchor {target}')
        links.append({'document': name, 'target': target})
    for line_no, line in enumerate(content.splitlines(), 1):
        if line.rstrip() != line:
            errors.append(f'{name}:{line_no}: trailing whitespace')

template = (ROOT / documents[1]).read_text()
visible = re.sub(r'<!--.*?-->', '', template, flags=re.S).lstrip()
if not visible.startswith('| ID | Brief Description | Status |'):
    errors.append('Template does not begin with compact summary')
rows = re.findall(r'^\| \[(CR-\d{3,})\]\((#[^)]+)\) \| ([^|]+) \| ([^|]+) \|$', template, re.M)
dispositions = {'Accept', 'Reject', 'On-Hold', 'Manual Intervention'}
if len(rows) != 4 or {row[3].strip() for row in rows} != dispositions:
    errors.append('Template must demonstrate exactly the four dispositions')
for number, (cr, anchor, description, status) in enumerate(rows, 1):
    if cr != f'CR-{number:03d}' or len(description.strip()) > 80:
        errors.append(f'Invalid ID/description: {cr}')
    heading = re.search(rf'^## {cr} — {re.escape(description.strip())}\n\n\*\*Status:\*\* (.+)$', template, re.M)
    if not heading or heading.group(1) != status.strip():
        errors.append(f'Summary/detail mismatch: {cr}')

snapshot = json.loads((EVIDENCE / 'start-preservation.json').read_text())
allowed_changes = {
    'architecture-release.json', 'docs/markdown/governance/README.md',
    'docs/archimate/README.md', 'docs/archimate/framework/README.md',
    'docs/archimate/processing/ingestion/README.md',
}
changed = []
missing = []
for name, digest in snapshot['files_sha256'].items():
    path = ROOT / name
    if not path.is_file():
        missing.append(name)
    elif hashlib.sha256(path.read_bytes()).hexdigest() != digest:
        changed.append(name)
unexpected = sorted(set(changed) - allowed_changes)
errors.extend(f'Unexpected protected-file change: {name}' for name in unexpected)
errors.extend(f'Missing protected file: {name}' for name in missing)
release = json.loads((ROOT / 'architecture-release.json').read_text())
if release != {'version': '1.0.9', 'previous_version': '1.0.8'}:
    errors.append(f'Unexpected release transition: {release}')

new_model = Path('/tmp/harmonia-change-review-1.0.9/docs/archimate/exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml')
old_model = ROOT / 'docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml'
generation = {'status': 'UNRESOLVED', 'new_exchange_package_written_to_repository': False}
if new_model.is_file():
    before, after = old_model.read_bytes(), new_model.read_bytes()
    if after.count(b'>1.0.9</value>') != 1 or after.replace(b'>1.0.9</value>', b'>1.0.8</value>', 1) != before:
        errors.append('Independent reproduction has differences beyond one model release value')
    else:
        generation = {
            'status': 'PASS', 'only_difference': 'One model-level release value: 1.0.8 -> 1.0.9',
            'published_baseline_sha256': hashlib.sha256(before).hexdigest(),
            'independent_reproduction_sha256': hashlib.sha256(after).hexdigest(),
            'new_exchange_package_written_to_repository': False,
        }
else:
    errors.append('Independent generation output unavailable')

result = {
    'status': 'FAIL' if errors else 'PASS', 'architecture_release': release,
    'documents_checked': documents, 'local_links_and_anchors_checked': len(links),
    'template_dispositions': [row[3].strip() for row in rows],
    'summary_character_limit': 80, 'protected_files_checked': len(snapshot['files_sha256']),
    'protected_files_unchanged': len(snapshot['files_sha256']) - len(changed) - len(missing),
    'expected_existing_file_changes': sorted(changed),
    'unexpected_changes': unexpected, 'missing_files': missing,
    'input_xml_and_all_semantic_models_and_blind_reports_unchanged': not unexpected and not missing,
    'independent_generation_comparison': generation, 'errors': errors,
    'links': links,
}
(EVIDENCE / 'documentation-preservation-validation.json').write_text(json.dumps(result, indent=2) + '\n')
print(json.dumps({key: value for key, value in result.items() if key != 'links'}, indent=2))
sys.exit(bool(errors))
