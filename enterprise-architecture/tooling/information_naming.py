# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Propose information-element names from native semantics, never derived claims.

No allocation, acceptance, source mutation, folder interpretation or reconciliation.
Historical source publication uses its persisted naming inputs independently.
"""
from collections import Counter
import re

import containment
import exchange_signatures as review
from element_correlation import canonical_index
import exchange_xml as g
import governed_naming as metadata

DERIVED = ('displayName', 'friendlyName', 'canonicalName', 'archimateType', 'harmoniaType')
INFORMATION_TYPES = {'BusinessObject', 'DataObject', 'Artifact'}
AUTHORITY = ('enterprise-architecture/governance/canonical-identity-and-naming.md'
             '#information-element-naming--machinery-validation-decision-2026-10-20')


def descriptor(model, rid):
    """Resolve authored classification and local Name; stale Properties are ignored."""
    node = model.elements[rid]
    native = review.native_type(node)
    result = dict(key=rid, type=native)
    names = node.findall('a:name', g.N)
    g.require(len(names) == 1 and (names[0].text or '').strip(), 'Missing/ambiguous native Name')
    label = names[0].text
    spec = review.specialization(model, node)
    g.require(not spec['claims'] or spec['single'] is not None, 'Ambiguous authored Specialization')
    g.require(spec['single'] is None or (native == 'DataObject' and spec['single'] in ('Topic', 'Class'))
              or (native == 'Grouping' and spec['single'] == 'Package'),
              'Unestablished/incompatible authored Specialization')
    if native == 'DataObject':
        g.require(spec['single'] in ('Topic', 'Class'), 'DataObject lacks explicit Topic or Class Specialization')
        result['specialization'] = spec['single']
        display_kind = spec['single']
    elif native == 'Grouping':
        g.require(spec['single'] == 'Package', 'Grouping lacks explicit Package Specialization')
        result['specialization'] = 'Package'
        display_kind = 'Package'
    elif native in ('BusinessObject', 'Artifact'):
        display_kind = native
    else:
        raise ValueError('Unestablished ancestor naming vocabulary: ' + native)
    prefix = re.match(r'^\[([^\]]+)\]\s+', label)
    if prefix:
        g.require(prefix.group(1) == display_kind, 'Incompatible/unknown display qualifier')
        label = label[prefix.end():]
        g.require(not label.startswith('[') and not (native == 'BusinessObject' and label.endswith(' BO')),
                  'Ambiguous repeated display qualification')
    elif label.startswith('['):
        raise ValueError('Malformed display qualifier')
    elif native == 'BusinessObject' and label.endswith(' BO'):
        label = label[:-3]
        g.require(not label.endswith(' BO'), 'Ambiguous repeated BO suffix')
    g.require(label and label == label.strip(), 'Empty or whitespace-ambiguous architectural name')
    result.update(name=label, displayName='[' + display_kind + '] ' + label,
                  harmoniaType=display_kind if display_kind in ('Topic', 'Class') else None)
    return result


def propose(model, resolved=None):
    identity = canonical_index(model)
    resolved = containment.resolve(model.root) if resolved is None else resolved
    parents = {key: [dict(relationship_id=edge['identifier'], source=edge['source'],
                         target=edge['target'], line=edge['line']) for edge in links
                     if edge['mechanism'] == 'Composition']
               for key, links in resolved.incoming.items()}
    descriptors, descriptor_errors = {}, {}
    for rid in sorted(model.elements):
        try:
            descriptors[rid] = descriptor(model, rid)
        except ValueError as exc:
            descriptor_errors[rid] = str(exc)

    records = []
    for rid, node in sorted(model.elements.items()):
        native = review.native_type(node)
        if native not in INFORMATION_TYPES:
            continue
        claim = identity['claims'][rid]
        unique = claim['valid'] and identity['unique'].get(claim['canonicalId']) == rid
        row = dict(representation_id=rid, line=node.sourceline,
                   authored=dict(native_type=native, names=review.text_values(node, 'name'),
                                 specialization=review.specialization(model, node),
                                 direct_composition=parents.get(rid, [])),
                   governed_identity=dict(**claim, unique=unique,
                       standing='PRESERVED_CLAIM' if unique else 'UNRESOLVED' if claim['present'] else 'ABSENT_NO_ALLOCATION'),
                   submitted_derived={key: dict(claims=[review.signature(p, model) for p in model.claims(node, key)],
                                               values=model.texts(node, key)) for key in DERIVED},
                   proposed=dict(archimateType=native), unresolved=[], context=None)
        if rid not in descriptors:
            row['unresolved'].append(dict(properties=['displayName', 'friendlyName', 'canonicalName', 'harmoniaType'],
                                          reason=descriptor_errors[rid]))
        else:
            local = descriptors[rid]
            row['architectural_name'] = local['name']
            row['classification'] = local.get('specialization') or native
            row['proposed'].update({key: local[key] for key in ('displayName', 'harmoniaType')})
            try:
                path = resolved.ancestry(rid)
                for key in path:
                    g.require(key in descriptors, descriptor_errors.get(key, 'Missing naming descriptor'))
                edges = [parents[key][0] for key in path[1:]]
                names = metadata.qualified_names([descriptors[key] for key in path])
                row['proposed'].update(names)
                row['context'] = dict(path=path, composition=edges,
                    standing='SUBMITTED_COMPOSITION_CONDITIONAL' if edges else 'LOCAL_CANDIDATE_ROOT_COMPLETENESS_UNESTABLISHED')
            except ValueError as exc:
                row['unresolved'].append(dict(properties=['friendlyName', 'canonicalName'], reason=str(exc)))
        row['changes'] = {key: dict(submitted=row['submitted_derived'][key]['values'], proposed=value)
                          for key, value in row['proposed'].items()
                          if (model.claims(node, key) if value is None else model.single(node, key) != value)}
        records.append(row)
    return dict(standing='PROPOSED_REGENERATED_METADATA_NOT_ACCEPTED', authority=AUTHORITY,
                identity_issues=identity['issues'], input_issues=model.issues, records=records,
                summary=dict(information_elements=len(records),
                    native_types=dict(Counter(r['authored']['native_type'] for r in records)),
                    classifications=dict(Counter(r.get('classification', 'UNRESOLVED') for r in records)),
                    fully_derived=sum(not r['unresolved'] for r in records),
                    unresolved=sum(bool(r['unresolved']) for r in records),
                    contextual=sum(bool(r['context'] and r['context']['composition']) for r in records),
                    preserved_identity=sum(r['governed_identity']['unique'] for r in records),
                    absent_identity=sum(not r['governed_identity']['present'] for r in records),
                    canonical_ids_allocated=0,
                    metadata_changes=dict(Counter(key for r in records for key in r['changes']))))
