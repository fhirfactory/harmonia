#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Evidence-only candidate/publication correspondence; no Archi policy changes.

Run from the repository root. Resolve paths only in generated link fields, never
in original Markdown/plain text, cardinalities or arbitrary parent-like strings.
"""
import hashlib
import json
import os
from pathlib import Path
import re

ROOT = Path.cwd()
BASE = ROOT / 'enterprise-architecture/design/evidence/1.1.22/001/after'
CANDIDATE = BASE / 'information-unit-incorporation/candidate-v2'
READER = ROOT / 'enterprise-architecture/documentation/continuum-manifest/published/1.1.22'
RAD = ROOT / 'enterprise-architecture/documentation/continuum-rad/published/1.1.22'
PAYLOAD = re.compile(r'(<script type="application/json" id="architecture">)(.*?)(</script>)', re.S)


def reference(value, directory):
    if value.startswith(('../', './')) or value in ('..', '.'):
        return os.path.normpath(os.path.join(directory, value))
    return value


def hrefs(value, directory):
    return re.sub(r'''(href=["'])([^"']*)(["'])''',
                  lambda m: m[1] + reference(m[2], directory) + m[3], value)


def page(value, directory):
    match = PAYLOAD.search(value)
    assert match, 'Missing architecture JSON'
    rows = json.loads(match[2])
    for row in rows:
        row['source'] = reference(row['source'], directory)
        row['html'] = hrefs(row['html'], directory)
    # Every other row field, row order and all non-payload page bytes survive.
    shell = value[:match.start(2)] + '<checked architecture JSON>' + value[match.end(2):]
    return hrefs(shell, directory), rows


def tex(value, directory):
    value = re.sub(r'(\\href\{\\detokenize\{)([^}]*)(\}\})',
                   lambda m: m[1] + reference(m[2], directory) + m[3], value)
    value = re.sub(r'(\\href\{)([^}]*)(\})',
                   lambda m: m[1] + reference(m[2], directory) + m[3], value)
    return re.sub(r'(\\url\{)([^}]*)(\})',
                  lambda m: m[1] + reference(m[2], directory) + m[3], value)


def markdown(value, directory):
    return re.sub(r'(\]\()([^)]*)(\))',
                  lambda m: m[1] + reference(m[2], directory) + m[3], value)


assert tex('[1..1] [0..*]', ROOT) == '[1..1] [0..*]'
assert hrefs('plain ../text <a href="../target">x</a>', ROOT).startswith('plain ../text ')
assert markdown('[x](../name with spaces.md)', ROOT) == '[x](' + str(ROOT.parent / 'name with spaces.md') + ')'
assert tex(r'\url{../target}', ROOT) == r'\url{' + str(ROOT.parent / 'target') + '}'
assert reference('https://example.test/x', ROOT) == 'https://example.test/x'


checks, manifests = [], []
for source, target in [(CANDIDATE / 'reader', READER), (CANDIDATE / 'narrative', RAD)]:
    for original in sorted(source.rglob('*')):
        if not original.is_file() or original.name == 'publication.json':
            continue
        published = target / original.relative_to(source)
        a, b = original.read_bytes(), published.read_bytes()
        equal = a == b
        if equal:
            corresponds = True
        elif original.name == 'continuum.html':
            corresponds = page(a.decode(), source) == page(b.decode(), target)
        elif original.suffix == '.tex':
            # Included parts use the master output base in the maintained publisher.
            corresponds = tex(a.decode(), source) == tex(b.decode(), target)
        elif original.suffix == '.md':
            corresponds = markdown(a.decode(), source) == markdown(b.decode(), target)
        else:
            corresponds = False
        checks.append({'candidate': original.relative_to(ROOT).as_posix(),
                       'published': published.relative_to(ROOT).as_posix(),
                       'byteEqual': equal, 'corresponds': corresponds})
    a = json.loads((source / 'publication.json').read_text())
    b = json.loads((target / 'publication.json').read_text())
    a.pop('sha256')
    hashes = b.pop('sha256')
    manifests.append({'path': (target / 'publication.json').relative_to(ROOT).as_posix(),
                      'nonHashFieldsEqual': a == b,
                      'declaredHashesMatch': all(hashlib.sha256((target / name).read_bytes()).hexdigest() == sha
                                                for name, sha in hashes.items())})

result = {
    'status': 'PASS' if all(x['corresponds'] for x in checks) and all(
        x['nonHashFieldsEqual'] and x['declaredHashesMatch'] for x in manifests) else 'FAIL',
    'artifactChecks': checks, 'manifestChecks': manifests,
    'method': 'Complete byte comparison, except resolving generated link targets against their respective output bases. HTML JSON compares all rows in order and every field; only source and rendered href fields resolve relative paths. Plain source text is unchanged. Every other page byte and every non-hash manifest field is compared; all published manifest hashes are checked.',
    'diagnosticBoundaryAssertions': 'PASS: preserve literal cardinalities and plain parent-like text, handle Markdown paths containing spaces and TeX URL fields, preserve external URLs.',
    'initialDiagnostics': 'Preliminary prefix heuristics and the first field-specific syntax pass were incomplete and returned FAIL: repository-root-only substitution missed common ancestors; arbitrary parent-prefix resolution also touched unchanged source Markdown and cardinalities such as [1..1]; the first field pass omitted spaced Markdown targets and TeX URL fields. Those observations are retained separately. The field-specific check fixes those diagnostic mistakes without altering publications or production comparison tooling.',
    'limits': 'Publication link correspondence only. No Archi transport normalisation, verifier correction or waiver.'
}
(BASE / 'release-closure/candidate-publication-correspondence.json').write_text(json.dumps(result, indent=2) + '\n')
print(json.dumps({'status': result['status'], 'artifacts': len(checks),
                  'byteEqual': sum(x['byteEqual'] for x in checks),
                  'pathRebased': sum(not x['byteEqual'] and x['corresponds'] for x in checks),
                  'manifestChecks': manifests,
                  'failures': [x for x in checks if not x['corresponds']]}, indent=2))
raise SystemExit(0 if result['status'] == 'PASS' else 1)
