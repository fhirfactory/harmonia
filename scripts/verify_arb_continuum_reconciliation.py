#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify the bounded entry-to-result transition, including retry identity safety.

This checks attributable correspondence and preservation, not architectural
acceptance or semantic completeness merely from counting source blocks.
"""
from collections import Counter
import copy
from pathlib import Path
import subprocess
import uuid

import architecture_continuum as c
import apply_arb_continuum_reconciliation as capabilities
import apply_arb_designnote_assertions as notes
import apply_arb_remaining_sources as remaining


def verify(root=c.ROOT):
    plan = c.read_json(root / capabilities.PLAN)
    entry = plan['entryRevision']
    _, current = c.load(root)
    paths = subprocess.check_output(['git', 'ls-tree', '-r', '--name-only', entry, '--', str(c.CONTINUUM)], cwd=root, text=True).splitlines()
    before = {}
    for rel in paths:
        if not rel.endswith('.md') or rel == str(c.MANIFEST) or rel.startswith(str(c.PUBLICATION) + '/'):
            continue
        data = subprocess.check_output(['git', 'show', entry + ':' + rel], cwd=root)
        m, body = c.decode(data.decode())
        before[m['canonicalId']] = m, body, rel, data
    c.require(set(before) <= set(current), 'Established identity lost')
    prior_predicates = {}
    old_relationships = {}
    historical = {r['historicalCanonicalId'] for r in plan['capabilities']}
    for cid, (m, body, rel, data) in before.items():
        result = current[cid][0]
        c.require(result['exchange']['identifier'] == m['exchange']['identifier'], 'Representation rebound: ' + cid)
        c.require(result['archimateType'] == m['archimateType'], 'Unreviewed native retyping: ' + cid)
        c.require(result.get('specialization') == m.get('specialization'), 'Unreviewed specialisation: ' + cid)
        c.require(all(result[role] == m[role] for role in c.ROLES), 'Established identity/name role changed: ' + cid)
        c.require(result.get('sourceUnresolved') == m.get('sourceUnresolved'), 'Original source uncertainty changed: ' + cid)
        c.require(result.get('unresolvedProperties') == m.get('unresolvedProperties'), 'Original unresolved property changed: ' + cid)
        if cid not in historical:
            c.require(result['standing'] == m['standing'], 'Standing promoted: ' + cid)
        for q in m.get('qualifiedAssertions', []):
            prior_predicates[q['sourcePointer']] = q
        for r in m['relationships']:
            old_relationships[r['identifier']] = r
    actual_predicates = {q['sourcePointer']: q for m, _, _ in current.values() for q in m.get('qualifiedAssertions', [])}
    c.require(set(prior_predicates) == set(actual_predicates), 'Predicate population changed')
    for pointer, q in actual_predicates.items():
        original = copy.deepcopy(q)
        original.pop('nativeMapping', None)
        c.require(original == prior_predicates[pointer], 'Original predicate altered: ' + pointer)
    relationships = {r['identifier']: r for m, _, _ in current.values() for r in m['relationships']}
    for rid, old in old_relationships.items():
        result = copy.deepcopy(relationships[rid])
        result.pop('qualifiedPredicateContributions', None)
        c.require(result == old, 'Original relationship changed: ' + rid)
    note_plan = c.read_json(root / notes.PLAN)
    note_sources = {}
    for note in note_plan['notes']:
        original = subprocess.check_output(['git', 'show', note_plan['sourceEdition'] + ':' + note['source']], cwd=root)
        note_sources[note['source']] = original.decode()
        c.require(c.sha(original) == note['originalSha256'], 'Original DesignNote edition mismatch: ' + note['source'])
        at_entry = subprocess.check_output(['git', 'show', entry + ':' + note['source']], cwd=root)
        c.require((root / note['source']).read_bytes() == at_entry, 'DesignNote changed during follow-up: ' + note['source'])
    for row in note_plan['assertions']:
        original = note_sources[row['source']]
        c.require('\n'.join(original.splitlines()[row['startLine'] - 1:row['endLine']]) == row['text'], 'Source selector drift')
        c.require(row.get('disposition') and row.get('reason'), 'Undisposed source assertion')
        if row['disposition'] == 'represented by existing canonical assertion':
            m, body, path = current[row['canonicalId']]
            c.require({'source': row['source'], 'selector': notes.selector(row)} in m['sourceBindings'], 'Unbound DesignNote assertion')
            c.require('### ' + Path(row['source']).stem + ' — ' + notes.selector(row) in body, 'Unrepresented DesignNote assertion')
    allocated = set(current) - set(before)
    recorded = c.read_json(root / capabilities.EVIDENCE / 'after/arb-designnote-assertion-dispositions.json')
    c.require(allocated == {r['canonicalId'] for r in recorded['normalisedAssertions']}, 'Unexpected new element identity')
    c.require(all(uuid.UUID(cid).version == 4 for cid in allocated), 'New resource identity is not UUID4')
    for row in recorded['normalisedAssertions']:
        m, body, _ = current[row['canonicalId']]
        native = 'CourseOfAction' if row['classification'] == 'ImplementationGuideline' else row['classification']
        c.require(m['archimateType'] == native and row['sourceText'] in body, 'New assertion type/content drift')
    template = c.DESIGN / 'projection-template.xml'
    original = subprocess.check_output(['git', 'show', entry + ':' + str(template)], cwd=root)
    c.require(original == (root / template).read_bytes(), 'Existing presentation template changed')
    hashes = {cid: c.sha(path.read_bytes()) for cid, (_, _, path) in current.items()}
    # Reapply the exact plans; no new UUID or canonical file change is permitted.
    from unittest.mock import patch
    with patch.object(c.uuid, 'uuid4', side_effect=AssertionError('Unexpected retry allocation')):
        capabilities.apply(root)
        notes.apply(root)
        remaining.apply(root)
    c.require(hashes == {cid: c.sha(path.read_bytes()) for cid, (_, _, path) in c.load(root)[1].items()}, 'Non-idempotent application')
    return {'status': 'PASS', 'entryRevision': entry,
            'preservedElementIds': len(before), 'newElementIds': len(allocated),
            'modifiedExistingElements': sum(data != current[cid][2].read_bytes() for cid, (_, _, _, data) in before.items()),
            'preservedRepresentationIds': len(before), 'originalNativeRelationshipsPreserved': len(old_relationships),
            'newNativeRelationships': len(relationships) - len(old_relationships),
            'capabilityCorrespondences': dict(Counter(r['disposition'] for r in plan['capabilities'])),
            'originalPredicatesUnchanged': len(prior_predicates),
            'nativePredicateMappings': dict(Counter(q['nativeMapping']['relationshipType'] or 'explicitly unestablished' for q in actual_predicates.values())),
            'reusedNativeRelationships': sum(q['nativeMapping'].get('existingNativeRelationshipReused', False) for q in actual_predicates.values()),
            'unchangedDesignNotes': len(note_plan['notes']), 'sourceBlocksWithExplicitDispositions': len(note_plan['assertions']),
            'presentationTemplateUnchanged': True, 'retryAllocatedIds': 0, 'retryCanonicalChanges': 0,
            'limit': 'Source-block accounting and exact contribution preservation verify coverage/correspondence; they do not prove exhaustive semantic correctness or confer architectural acceptance.'}


if __name__ == '__main__':
    report = verify()
    (c.ROOT / capabilities.EVIDENCE / 'after/arb-transition-verification.json').write_bytes(c.encoded(report))
    print(report)
