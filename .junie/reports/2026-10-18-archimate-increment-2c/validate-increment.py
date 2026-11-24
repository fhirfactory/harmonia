# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only 2C documentation/preservation audit; writes task evidence only."""
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
from urllib.parse import unquote, urlsplit

ROOT = Path.cwd()
EVIDENCE = ROOT / '.junie/reports/2026-10-18-archimate-increment-2c'
REPRODUCTION = Path('/tmp/harmonia-increment-2c-1.0.11')
sys.path.insert(0, str(ROOT / 'scripts'))
from rads_business_architecture import anchors


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def record(name, data):
    (EVIDENCE / name).write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n')


documents = [
    'docs/archimate/README.md',
    'docs/archimate/exchange/ingest/README.md',
    'docs/archimate/processing/ingestion/README.md',
    'docs/markdown/governance/README.md',
    'docs/markdown/governance/architecture-authoring-praxis.md',
    '.junie/reports/2026-10-18-archimate-increment-2c.md',
]
allowed_changes = set(documents) | {
    'architecture-release.json',
    'docs/markdown/governance/baseline/correspondence.json',
}
errors, links = [], []
for name in documents:
    path = ROOT / name
    content = path.read_text()
    rendered = re.sub(r'(?ms)^```[^\n]*\n.*?^```[ \t]*$', '', content)
    for target in re.findall(r'(?<!!)\[[^\]\n]+\]\(([^)\n]+)\)', rendered):
        ref = urlsplit(target)
        if ref.scheme or ref.netloc:
            continue
        destination = (path.parent / unquote(ref.path)).resolve() if ref.path else path
        if not destination.exists():
            errors.append(f'{name}: missing link {target}')
        elif ref.fragment and destination.is_file():
            if unquote(ref.fragment) not in anchors(destination.read_text()):
                errors.append(f'{name}: missing anchor {target}')
        links.append({'document': name, 'target': target})
    for number, line in enumerate(content.splitlines(), 1):
        if line.rstrip() != line:
            errors.append(f'{name}:{number}: trailing whitespace')

snapshot = json.loads((EVIDENCE / 'start-preservation.json').read_text())
changed, missing = [], []
for name, digest in snapshot['files_sha256'].items():
    path = ROOT / name
    if not path.is_file():
        missing.append(name)
    elif sha(path) != digest:
        changed.append(name)
unexpected = sorted(set(changed) - allowed_changes)
errors.extend('Unexpected existing-file change: ' + name for name in unexpected)
errors.extend('Missing existing file: ' + name for name in missing)
current_paths = set(filter(None, subprocess.check_output(
    ['git', 'ls-files', '--cached', '--others', '--exclude-standard', '-z'], timeout=15
).decode().split('\0')))
unexpected_new = sorted(name for name in current_paths - set(snapshot['files_sha256'])
                        if name not in allowed_changes
                        and not name.startswith(str(EVIDENCE.relative_to(ROOT)) + '/'))
errors.extend('Unexpected new file: ' + name for name in unexpected_new)

release = json.loads((ROOT / 'architecture-release.json').read_text())
if release != {'version': '1.0.11', 'previous_version': '1.0.10'}:
    errors.append('Unexpected release transition')

pin_path = 'docs/markdown/governance/baseline/correspondence.json'
old_pins = json.loads(subprocess.check_output(['git', 'show', 'HEAD:' + pin_path], timeout=15))
new_pins = json.loads((ROOT / pin_path).read_text())
praxis = 'docs/markdown/governance/architecture-authoring-praxis.md'
if new_pins['current_authority_sha256'].pop(praxis) != sha(ROOT / praxis):
    errors.append('New Praxis authority pin mismatch')
if old_pins != new_pins:
    errors.append('Baseline correspondence changed beyond the new Praxis pin')

original = (EVIDENCE / 'original-workspace-readme.md').read_text()
historical_notes = original.split('Publication: ', 1)[1].split('\n\nHistorical reports,', 1)[0]
historical_notes = 'Publication: ' + historical_notes
current_readme = (ROOT / 'docs/archimate/README.md').read_text()
wrapped = current_readme.split('------- Legacy Content - Superseded ------- Start ------', 1)[1]
wrapped = wrapped.split('------- Legacy Content - Superseded ------- Finish ----', 1)[0]
if historical_notes not in wrapped:
    errors.append('Original legacy architectural notes changed or are outside wrapper')

baseline = ROOT / 'docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml'
generated = REPRODUCTION / 'docs/archimate/exchange/publish/1.0.11/Harmonia-Architecture-1.0.11.xml'
before, after = baseline.read_bytes(), generated.read_bytes()
release_only = (after.count(b'>1.0.11</value>') == 1
                and after.replace(b'>1.0.11</value>', b'>1.0.8</value>', 1) == before)
if not release_only:
    errors.append('Generated XML differs beyond one model-level release value')
markdown = []
for path in sorted((REPRODUCTION / 'docs/markdown').rglob('*')):
    if path.is_file():
        relative = path.relative_to(REPRODUCTION)
        equal = path.read_bytes() == (ROOT / relative).read_bytes()
        markdown.append({'path': str(relative), 'identical': equal, 'sha256': sha(path)})
        if not equal:
            errors.append('Generated RADS drift: ' + str(relative))
if len(markdown) != 10:
    errors.append('Expected ten independently generated RADS files')
validation = json.loads((REPRODUCTION / 'docs/archimate/processing/publishing/1.0.11/publication-validation-1.0.11.json').read_text())
if (validation['status'] != 'PASS' or validation['architecture_release'] != release
        or validation['checks']['deterministic_regeneration'] != 'PASS'):
    errors.append('Generated publication validation/release/determinism mismatch')
record('independent-generation-validation.json', validation)

commands = json.loads((EVIDENCE / 'command-results.json').read_text())
errors.extend('Verification did not pass: ' + entry['key'] for entry in commands
              if entry['result'] != 'PASS' or entry['exit_code'] != 0)
whitespace = subprocess.run(['git', 'diff', '--check', '--', *sorted(allowed_changes)],
                            capture_output=True, text=True, timeout=15)
if whitespace.returncode:
    errors.append('Focused whitespace check: ' + whitespace.stdout + whitespace.stderr)
result = {
    'status': 'FAIL' if errors else 'PASS', 'architecture_release': release,
    'documents_checked': documents, 'local_links_and_anchors_checked': len(links),
    'preservation': {
        'existing_files_checked': len(snapshot['files_sha256']),
        'unchanged': len(snapshot['files_sha256']) - len(changed) - len(missing),
        'expected_changes': sorted(changed), 'unexpected_changes': unexpected,
        'missing_files': missing, 'unexpected_new_files': unexpected_new,
        'blind_xml_backup_evidence_semantic_sources_and_existing_publications_unchanged': not unexpected and not missing,
        'wrapped_original_architectural_notes_unchanged': historical_notes in wrapped,
        'correspondence_change': 'New Praxis authority pin only; existing contents unchanged',
    },
    'generation': {
        'xml_release_value_only': release_only, 'baseline_sha256': sha(baseline),
        'independent_xml_sha256': sha(generated), 'markdown': markdown,
        'repository_exchange_publication_written': False,
        'limitations': 'Preservation-only existing publisher reproduction, not 2B Name/displayName migration or Containment View conformance; no new Published Baseline.',
    },
    'focused_whitespace_exit_code': whitespace.returncode, 'errors': errors, 'links': links,
}
record('documentation-preservation-validation.json', result)
print(json.dumps({key: value for key, value in result.items() if key != 'links'}, indent=2))
sys.exit(bool(errors))
