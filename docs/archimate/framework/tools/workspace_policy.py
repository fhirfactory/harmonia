# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Governed collection roles, independent of incoming workspace placement.

Resource captures supply classification/standing only. Their parent_key,
owner_key, naming_context and navigation_path never enter current ancestry.
The shared resolver supplies every containment path.
"""
import hashlib
import json

import generate_views as g
import governed_metadata as metadata
import containment
from element_correlation import canonical_index
from change_review import native_type
from workspace_navigation import INFORMATION_ANCHORS

CATEGORIES = {'business': ('Business Layer', 'Business Capabilities'),
              'business_enabling': ('Business Layer', 'Business Enabling Capabilities'),
              'enterprise': ('Application Layer', 'Enterprise Capabilities')}
BUSINESS = {'BusinessFunction': 'Functions', 'BusinessProcess': 'Processes', 'BusinessService': 'Services'}
STATES = {'superseded': 'Superseded', 'pending_adjudication': 'Pending Adjudication'}
SOURCES = ('architecture.json', 'motivation.json', 'accepted-ingestion-1.0.8.json')


def load_records(paths=None):
    """Read current governed resource classification, with exact provenance."""
    paths = [metadata.RAD / name for name in SOURCES] if paths is None else paths
    records, provenance = {}, {}
    for path in paths:
        data = path.read_bytes()
        provenance[str(path.relative_to(metadata.ROOT))] = hashlib.sha256(data).hexdigest()
        source = json.loads(data)
        for row in [*source.get('elements', []), *source.get('historical_reference_elements', [])]:
            identity = row.get('canonicalId')
            g.require(identity and identity not in records, 'Missing/duplicate governed resource identity')
            records[identity] = row
    return records, provenance


def placements(model, resolved, records):
    """Declare collections for all supported elements; never read organisations.

    New native types/classifications without a collection rule stop explicitly.
    An unestablished Home is visible navigation status, not inferred parentage.
    """
    claims = canonical_index(model)
    g.require(not claims['issues'], 'Ambiguous identity prevents collection classification')
    paths = resolved.paths()
    bound = {rid: records.get(claim['canonicalId']) for rid, claim in claims['claims'].items()}
    # Recorded context is used only to recover the existing collection category.
    # Its ancestry never enters the projected current containment path.
    representations = {row.get('representationId'): key for key, row in records.items() if row.get('representationId')}
    representations.update({rid: claim['canonicalId'] for rid, claim in claims['claims'].items()
                            if claim['canonicalId'] in records})
    bykey = {row['key']: key for key, row in records.items()}
    incoming = {}
    for key, row in records.items():
        parent = None
        if row.get('naming_context'):
            parent = representations.get(row['naming_context'][-1])
        elif row.get('owner_key') or row.get('parent_key'):
            parent = bykey.get(row.get('owner_key') or row.get('parent_key'))
        if parent:
            incoming[key] = [dict(identifier=key, parent=parent, mechanism='recorded collection classification')]
    categories = containment.Ancestry(records, incoming)

    def category(rid):
        # A selected explicit Home takes precedence over responsibility category.
        row = bound[rid]
        g.require(row is not None, 'Unestablished governed collection classification: ' + rid)
        # Current semantic ancestors precede a stale recorded owner/category.
        # This chooses only a collection role; it asserts no new parent or tier.
        for ancestor in paths[rid][:-1]:
            if bound[ancestor] and bound[ancestor].get('category') in CATEGORIES:
                return bound[ancestor]['category']
        values = {records[key]['category'] for key in categories.ancestry(row['canonicalId'])
                  if records[key].get('category')}
        g.require(len(values) <= 1, 'Conflicting governed collection category: ' + rid)
        value = next(iter(values), None)
        g.require(value in CATEGORIES, 'Unestablished collection category: ' + rid)
        return value

    result = {}
    for rid, node in sorted(model.elements.items()):
        native = native_type(node)
        if native in INFORMATION_ANCHORS:
            result[rid] = dict(anchor=INFORMATION_ANCHORS[native], suffix=())
            continue
        row = bound[rid]
        g.require(row is not None, 'Unmapped collection; adjudication required: ' + rid)
        state = row.get('record_state', 'current')
        if native == 'Capability':
            cat = category(rid)
            anchor = ('Strategy', *CATEGORIES[cat])
            if state == 'historical_reference':
                anchor = ('Strategy', 'Unreconciled 1.0.4 Reference', CATEGORIES[cat][-1])
            elif state != 'current':
                g.require(state in STATES, 'Unestablished navigation standing: ' + state)
                anchor += (STATES[state],)
            elif row['type'] == 'feature' and len(paths[rid]) == 1:
                anchor += ('Unresolved Feature Ancestry',)
            suffix = ()
        elif native in BUSINESS:
            has_home = bool(resolved.incoming.get(rid))
            subject = paths[rid][-2] if has_home else rid
            anchor = ('Business', CATEGORIES[category(subject)][-1])
            if not has_home:
                anchor += ('Unestablished Home',)
            suffix = (BUSINESS[native],)
        elif native == 'Product':
            anchor, suffix = ('Business', 'Products'), ()
        elif row['type'] == native and row.get('subject'):
            anchor = ('Motivation',)
            if state != 'current':
                anchor += ('Historical Motivation',)
            anchor += (row['subject'],)
            suffix = ()
        else:
            raise ValueError('No governed collection rule: ' + rid + ' (' + native + ')')
        result[rid] = dict(anchor=anchor, suffix=suffix)
    return result
