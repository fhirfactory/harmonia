#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Apply the explicitly selected, bounded normative DesignNote assertions.

The retained plan is the reviewed source correspondence, not a prose classifier.
UUIDs are allocated by the existing authoring operation only; retries resolve the
persisted exact source binding before attempting creation.
"""
from collections import Counter
import copy
import os
from pathlib import Path
import re
import subprocess
import sys
import uuid
from urllib.parse import urlsplit, urlunsplit

import architecture_continuum as c

EVIDENCE = Path('enterprise-architecture/design/evidence/1.1.5/002')
PLAN = EVIDENCE / 'before/arb-designnote-assertion-plan.json'
AUTHORITY = 'enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation'


def selector(row):
    return 'original lines ' + str(row['startLine']) + '-' + str(row['endLine'])


def apply(root=c.ROOT):
    plan = c.read_json(root / PLAN)
    _, elements = c.load(root)
    bindings = {(b['source'], b['selector']): cid for cid, (m, _, _) in elements.items() for b in m['sourceBindings']}
    sys.path.insert(0, str(root / 'docs/archimate/framework/tools'))
    from governed_metadata import component
    results = []
    for row in plan['newAssertions']:
        loc = (row['source'], selector(row))
        if loc in bindings:
            cid = bindings[loc]
            m, body, path = elements[cid]
            c.require(m['identity'].get('normalisationAuthority') == AUTHORITY, 'Source already belongs to a different construct; reconcile instead of allocating')
        else:
            text = subprocess.check_output(['git', 'show', row['sourceRevision'] + ':' + row['source']], cwd=root, text=True)
            original = '\n'.join(text.splitlines()[row['startLine'] - 1:row['endLine']])
            c.require(original == row['sourceText'], 'Reviewed source assertion changed')
            native = 'CourseOfAction' if row['classification'] == 'ImplementationGuideline' else row['classification']
            special = 'ImplementationGuideline' if native == 'CourseOfAction' else None
            spec = c.template(native, special, root)
            m = spec['metadata']
            title = ('[ImplementationGuideline] ' if special else '') + row['displayName']
            local = component(row['displayName'])
            m.update(displayName=title, friendlyName=local, canonicalName=None if special else 'MOT-' + local,
                     standing={'semantic': 'Adjudicated Domain05 direction — ' + row['adjudication'] + '; holistic result acceptance pending', 'currentness': 'current'},
                     identity={'authority': AUTHORITY, 'normalisationAuthority': AUTHORITY},
                     provenance=[{'source': row['source'], 'selector': selector(row), 'revision': row['sourceRevision'],
                                  'role': 'Normative assertion reconciled under ' + row['adjudication']},
                                 {'source': 'docs/markdown/05-information-systems-architecture/sources.md', 'selector': row['adjudication'],
                                  'revision': plan['sourceEdition'], 'role': 'Source adjudication and later-correction boundary'}],
                     sourceBindings=[{'source': loc[0], 'selector': loc[1]}])
            m['naming'].update(mode='Local governed lexical component; no ancestry invented')
            if special:
                m['unresolvedProperties']['canonicalName'] = 'Q-01: ARB authorises ImplementationGuideline as CourseOfAction, but no formal canonical-name qualifier vocabulary is established. Do not invent one.'
            else:
                m['naming']['inputs'] = [{'type': native, 'name': row['displayName'], 'key': row['source'] + ':' + selector(row)}]
            # Preserve full original statement, including its modality and scope.
            spec['body'] = '# ' + title + '\n\n## Architectural content\n\n' + original + '\n\nThis assertion is normalised from the governed DesignNote under ' + row['adjudication'] + ' and the ARB release Praxis. Original candidate/release qualifications and separately deferred implementation details remain in force.\n'
            path = c.create(spec, root)
            m, body = c.decode(path.read_text()); cid = m['canonicalId']
            elements[cid] = m, body, path; bindings[loc] = cid
        subject, _, subject_path = elements[row['subjectCanonicalId']]
        rid = 'id-' + uuid.uuid5(c.SEMANTIC_NAMESPACE, 'normative-subject:' + cid).hex
        relation = {'identifier': rid, 'archimateType': 'Association', 'source': m['exchange']['identifier'],
                    'target': subject['exchange']['identifier'], 'attributes': {'isDirected': 'true'},
                    'standing': copy.deepcopy(m['standing']),
                    'provenance': {'source': row['source'], 'selector': selector(row), 'revision': row['sourceRevision'], 'authority': AUTHORITY},
                    'name': [{'text': 'applies to', 'attributes': {c.LANG: 'en'}}],
                    'documentation': [{'text': 'The source explicitly bounds this assertion to its architectural subject; this is not a containment or ownership transfer.', 'attributes': {c.LANG: 'en'}}], 'properties': []}
        old = next((r for r in m['relationships'] if r['identifier'] == rid), None)
        if old: c.require(old == relation, 'Normative assertion subject changed')
        else:
            m['relationships'].append(relation)
            body += '\nArchitectural subject: [' + subject['displayName'] + '](' + os.path.relpath(subject_path, path.parent) + ').\n'
            path.write_text(c.encode(m, body))
        results.append({**row, 'canonicalId': cid, 'canonicalSource': str(path.relative_to(root)), 'relationshipIdentifier': rid})
        print('Applied ' + str(len(results)) + '/' + str(len(plan['newAssertions'])) + ': ' + row['displayName'], flush=True)
    # An element's existence alone does not prove that a particular assertion is
    # represented. Bind and retain each exact reconciled contribution with its
    # source qualification. Corrected/superseded statements remain evidence only;
    # their later U-decision interpretation is already in the canonical subject.
    for row in plan['assertions']:
        if row['disposition'] != 'represented by existing canonical assertion':
            continue
        m, body, path = elements[row['canonicalId']]
        binding = {'source': row['source'], 'selector': selector(row)}
        def rebase(match):
            target = match.group(0)
            parts = urlsplit(target)
            if parts.scheme or parts.netloc:
                return match.group(0)
            absolute = (root / row['source']).parent / parts.path if parts.path else root / row['source']
            target = urlunsplit(('', '', os.path.relpath(absolute, path.parent), parts.query, parts.fragment))
            return target
        # Match the destination independently of nested brackets in link labels.
        rendered = re.sub(r'(?<=\]\()[^\s)]+(?=\))', rebase, row['text'])
        # Exact original bytes remain in the source/plan. Remove table padding
        # from this rendered contribution without rewriting the source capture.
        rendered = '\n'.join(line.rstrip() for line in rendered.splitlines())
        if '\n## ARB DesignNote assertion contributions\n' not in body:
            body += '\n## ARB DesignNote assertion contributions\n\nThese bounded source assertions are reconciled under the cited U-decisions and ARB release Praxis. They retain the source\'s candidate, scope and detailed-design qualifications. They do not accept the whole Domain05 candidate or create a new element per paragraph. Later-corrected assertions are retained in the disposition evidence instead of being restored here.\n'
        heading = '\n### ' + Path(row['source']).stem + ' — ' + selector(row) + '\n\n'
        contribution = heading + 'Source: `' + row['source'] + '`; ' + row['adjudication'] + '; source-qualified assertion, current subject standing unchanged.\n\n' + rendered + '\n'
        if binding in m['sourceBindings']:
            start = body.find(heading)
            c.require(start >= 0, 'Bound assertion body missing')
            boundaries = [pos for token in ('\n### DesignNote-', '\n## ') if (pos := body.find(token, start + len(heading))) >= 0]
            end = min(boundaries) if boundaries else len(body)
            body = body[:start] + contribution + body[end:]
        else:
            body += contribution
            m['sourceBindings'].append(binding)
            m['provenance'].append({**binding, 'revision': row['sourceRevision'], 'role': 'Exact DesignNote assertion contribution; source qualification retained', 'authority': AUTHORITY, 'adjudication': row['adjudication']})
        elements[row['canonicalId']] = m, body, path
        path.write_text(c.encode(m, body))
    report = {k: v for k, v in plan.items() if k != 'newAssertions'}
    result_bindings = {(r['source'], r['startLine']): r for r in results}
    for row in report['assertions']:
        result = result_bindings.get((row['source'], row['startLine']))
        if result:
            row['canonicalId'] = result['canonicalId']; row['canonicalSource'] = result['canonicalSource']
    report['normalisedAssertions'] = results
    report['counts'] = {'notes': len(plan['notes']), 'sourceBlocks': len(report['assertions']),
                        'dispositions': dict(Counter(r['disposition'] for r in report['assertions'])),
                        'newNativeTypes': dict(Counter(r['classification'] for r in results)),
                        'newElementIdentities': len(results)}
    (root / EVIDENCE / 'after/arb-designnote-assertion-dispositions.json').write_bytes(c.encoded(report))
    c.validate(root)
    return report


if __name__ == '__main__':
    report = apply()
    print(report['counts'])
