# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Snapshot correlation only: architectural identity precedes representation identity.

No names, native types, navigation, allocation or mutation participate in matching.
Ambiguous claims are retained, never resolved by a representation-ID fallback.
"""
from collections import defaultdict
import re

import exchange_xml as g

UUID32 = re.compile(r'[0-9a-f]{32}\Z')


def canonical_index(model):
    claims, owners, issues = {}, defaultdict(list), []
    definitions = {rid for rid, node in model.definitions.items()
                   if any(n.text == 'canonicalId' for n in node.findall('a:name', g.N))}
    for rid, node in sorted(model.elements.items()):
        # Even an ambiguously defined canonical Property is a claim, not absence.
        properties = [p for p in node.findall('a:properties/a:property', g.N)
                      if p.get('propertyDefinitionRef') in definitions]
        texts = [v.text or '' for p in properties for v in p.findall('a:value', g.N)]
        value = model.single(node, 'canonicalId')
        valid = len(properties) == len(texts) == 1 and value is not None and bool(UUID32.fullmatch(value))
        claims[rid] = dict(values=texts, present=bool(properties), valid=valid,
                           canonicalId=value if valid else None)
        for text in set(texts):
            owners[text].append(rid)
        if properties and not valid:
            issues.append(dict(kind='INVALID_OR_MULTIPLE_CANONICAL_CLAIM',
                               representation_id=rid, values=texts))
    for value, records in sorted(owners.items()):
        if len(records) > 1:
            issues.append(dict(kind='DUPLICATE_CANONICAL_ID', canonicalId=value,
                               representation_ids=records))
    unique = {value: records[0] for value, records in owners.items()
              if len(records) == 1 and claims[records[0]]['valid']}
    return dict(claims=claims, owners=dict(owners), unique=unique, issues=issues)


def correlate(before, after):
    left, right = canonical_index(before), canonical_index(after)
    matches, issues = [], []
    for side, index in (('baseline', left), ('incoming', right)):
        issues.extend(dict(snapshot=side, **issue) for issue in index['issues'])
    matched_left, matched_right = set(), set()

    def match(old, new, canonical, basis):
        matches.append(dict(canonicalId=canonical, baseline_id=old, incoming_id=new,
                            basis=basis, representation_identity='PRESERVED' if old == new else 'REBOUND'))
        matched_left.add(old)
        matched_right.add(new)

    for value in sorted(left['unique'].keys() & right['unique'].keys()):
        match(left['unique'][value], right['unique'][value], value, 'CANONICAL_ID')

    # A representation can persist without canonical metadata only when neither
    # side claims canonical identity. Lost/mutated claims are not new instances.
    for rid in sorted(before.elements.keys() & after.elements.keys()):
        a, b = left['claims'][rid], right['claims'][rid]
        if not a['present'] and not b['present']:
            match(rid, rid, None, 'REPRESENTATION_ID_ONLY')
        elif a != b:
            issues.append(dict(kind='REPRESENTATION_CANONICAL_CLAIM_CONFLICT', representation_id=rid,
                               baseline=a, incoming=b))

    unresolved_left, unresolved_right = set(), set()
    for own, other, matched, unresolved in ((left, right, matched_left, unresolved_left),
                                           (right, left, matched_right, unresolved_right)):
        for rid, claim in own['claims'].items():
            if rid in matched:
                continue
            # A shared representation with contradictory/missing identity, or any
            # ambiguous claim to this identity, prevents a new/deleted conclusion.
            if (rid in other['claims'] or
                    (claim['present'] and rid not in own['unique'].values()) or
                    any(v in other['owners'] for v in claim['values'])):
                unresolved.add(rid)
    deleted = sorted(before.elements.keys() - matched_left - unresolved_left)
    new = sorted(after.elements.keys() - matched_right - unresolved_right)
    rebindings = [row for row in matches if row['representation_identity'] == 'REBOUND']
    return dict(status='UNRESOLVED' if issues or unresolved_left or unresolved_right or before.issues or after.issues else 'CORRELATED',
                precedence=['unique valid canonicalId in both snapshots',
                            'same representation ID only when both canonical claims are absent'],
                matches=matches, representation_rebindings=rebindings,
                new=new, deleted=deleted,
                deleted_canonical_ids=[left['claims'][rid]['canonicalId'] for rid in deleted
                                       if left['claims'][rid]['valid']],
                unresolved=dict(baseline=sorted(unresolved_left), incoming=sorted(unresolved_right)),
                issues=issues)
