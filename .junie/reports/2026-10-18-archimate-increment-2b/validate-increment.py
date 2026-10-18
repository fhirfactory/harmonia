# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only 2B documentation, preservation and publication-conformance audit.

Writes evidence only. No ingestion, adjudication, reconciliation or layout engine.
Run from the repository root after the bounded existing validators/generator.
"""
from collections import Counter
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
from urllib.parse import unquote, urlsplit
import xml.etree.ElementTree as ET

ROOT = Path.cwd()
EVIDENCE = ROOT / '.junie/reports/2026-10-18-archimate-increment-2b'
REPRODUCTION = Path('/tmp/harmonia-increment-2b-1.0.10')
sys.path.insert(0, str(ROOT / 'scripts'))
from rads_business_architecture import anchors


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def record(name, value):
    (EVIDENCE / name).write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n')


documents = [
    'docs/markdown/governance/archimate-ingestion-publication-semantics.md',
    'docs/markdown/governance/canonical-identity-and-naming.md',
    'docs/markdown/governance/archi-navigation-and-view-convention.md',
    'docs/markdown/governance/archimate-ingestion-change-review.md',
    'docs/markdown/governance/README.md',
    '.junie/reports/2026-10-18-archimate-increment-2b.md',
]
allowed_changes = set(documents) | {
    'architecture-release.json',
    'docs/markdown/governance/baseline/correspondence.json',
}
errors, links = [], []
for name in documents:
    path = ROOT / name
    if not path.is_file():
        errors.append('Missing document: ' + name)
        continue
    content = path.read_text()
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
    ['git', 'ls-files', '--cached', '--others', '--exclude-standard', '-z']
).decode().split('\0')))
new_paths = sorted(current_paths - set(snapshot['files_sha256']))
unexpected_new = [name for name in new_paths if name not in allowed_changes
                  and not name.startswith(str(EVIDENCE.relative_to(ROOT)) + '/')]
errors.extend('Unexpected new file: ' + name for name in unexpected_new)

release = json.loads((ROOT / 'architecture-release.json').read_text())
if release != {'version': '1.0.10', 'previous_version': '1.0.9'}:
    errors.append('Unexpected release transition')

pin_changes = json.loads((EVIDENCE / 'authority-pin-changes.json').read_text())
correspondence = json.loads((ROOT / 'docs/markdown/governance/baseline/correspondence.json').read_text())
for change in pin_changes['changes']:
    if correspondence['current_authority_sha256'][change['document']] != sha(ROOT / change['document']):
        errors.append('Authority pin mismatch: ' + change['document'])

baseline = ROOT / 'docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml'
generated = REPRODUCTION / 'docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml'
before, after = baseline.read_bytes(), generated.read_bytes()
release_only = (after.count(b'>1.0.10</value>') == 1
                and after.replace(b'>1.0.10</value>', b'>1.0.8</value>', 1) == before)
if not release_only:
    errors.append('Generated XML differs beyond the single model release value')
markdown = []
for path in sorted((REPRODUCTION / 'docs/markdown').rglob('*')):
    if path.is_file():
        relative = path.relative_to(REPRODUCTION)
        equal = path.read_bytes() == (ROOT / relative).read_bytes()
        markdown.append({'path': str(relative), 'identical': equal, 'sha256': sha(path)})
        if not equal:
            errors.append('Generated Markdown drift: ' + str(relative))
if not markdown:
    errors.append('No independently generated Markdown found')
validation = json.loads((REPRODUCTION / 'docs/archimate/processing/publishing/1.0.10/publication-validation-1.0.10.json').read_text())
record('independent-generation-validation.json', validation)

n = {'a': 'http://www.opengroup.org/xsd/archimate/3.0/'}
root = ET.fromstring(before)
definitions = {p.attrib['identifier']: p.findtext('a:name', namespaces=n)
               for p in root.findall('a:propertyDefinitions/a:propertyDefinition', n)}
elements = root.findall('a:elements/a:element', n)
findings, missing_display = [], []
for element in elements:
    props = {definitions[p.attrib['propertyDefinitionRef']]: [v.text for v in p.findall('a:value', n)]
             for p in element.findall('a:properties/a:property', n)}
    native_name = element.findtext('a:name', namespaces=n)
    display_names = props.get('displayName', [])
    if not display_names:
        missing_display.append(element.attrib['identifier'])
    if display_names != [native_name]:
        findings.append({
            'representationId': element.attrib['identifier'],
            'canonicalId': props.get('canonicalId', []),
            'archimateType': element.attrib['{http://www.w3.org/2001/XMLSchema-instance}type'],
            'nativeName': native_name, 'displayName': display_names,
        })
record('name-displayname-conformance.json', {
    'status': 'NONCONFORMANT' if findings else 'PASS',
    'scope': 'Published governed 1.0.8 baseline only; no incoming blind-test model parsed',
    'baseline': str(baseline.relative_to(ROOT)), 'baseline_sha256': sha(baseline),
    'elements_checked': len(elements), 'conforming_elements': len(elements) - len(findings),
    'mismatch_count': len(findings), 'missing_displayName': missing_display,
    'mismatches_by_native_type': dict(Counter(item['archimateType'] for item in findings)),
    'disposition': 'Separately authorised migration required. No model or source names repaired.',
    'findings': findings,
})

whitespace = subprocess.run(['git', 'diff', '--check', '--', *sorted(allowed_changes)],
                            capture_output=True, text=True, timeout=15)
if whitespace.returncode:
    errors.append('Focused git diff --check failed: ' + whitespace.stdout + whitespace.stderr)
result = {
    'status': 'FAIL' if errors else 'PASS', 'architecture_release': release,
    'documents_checked': documents, 'local_links_and_anchors_checked': len(links),
    'preservation': {
        'existing_files_checked': len(snapshot['files_sha256']),
        'unchanged': len(snapshot['files_sha256']) - len(changed) - len(missing),
        'expected_changes': sorted(changed), 'unexpected_changes': unexpected,
        'missing_files': missing, 'unexpected_new_files': unexpected_new,
        'blind_inputs_evidence_and_semantic_models_unchanged': not unexpected and not missing,
    },
    'generation': {
        'xml_release_value_only': release_only, 'baseline_sha256': sha(baseline),
        'independent_xml_sha256': sha(generated), 'markdown': markdown,
        'repository_exchange_publication_written': False,
        'limitations': 'Preservation-only reproduction of existing publisher; not 2B Name or Containment View conformance.',
    },
    'name_displayName_findings': len(findings), 'focused_whitespace_exit_code': whitespace.returncode,
    'errors': errors, 'links': links,
}
record('documentation-preservation-validation.json', result)
print(json.dumps({key: value for key, value in result.items() if key != 'links'}, indent=2))
sys.exit(bool(errors))
