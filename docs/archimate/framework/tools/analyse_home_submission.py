#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only explicit Home submission evidence, without architectural inference."""
import argparse
import json
from pathlib import Path

import diagnose_roundtrip as d
import home_relationships as h


def signature(node, model):
    attrs = dict(node.attrib)
    if 'propertyDefinitionRef' in attrs:
        attrs['propertyDefinitionRef'] = model.keys[attrs['propertyDefinitionRef']]
    return [node.tag, attrs, (node.text or '').strip() if len(node) else node.text,
            [signature(c, model) for c in node]]


def analyse(incoming, previous, baseline):
    model, prior, published = (d.Exchange(p) for p in (incoming, previous, baseline))
    d.g.require(not model.issues, 'Invalid submitted Exchange: ' + repr(model.issues))
    homes = h.authored_homes(model)
    parents = {}
    for rel in model.relationships.values():
        if rel.get(d.g.TYPE) == 'Composition':
            parents.setdefault(rel.get('target'), []).append(rel.get('source'))

    def element(identifier):
        node = model.elements[identifier]
        return dict(identifier=identifier, name=node.findtext('a:name', namespaces=d.g.N),
                    native_type=node.get(d.g.TYPE), canonicalId=model.single(node, 'canonicalId'),
                    harmoniaType=model.single(node, 'harmoniaType'))

    def contexts(identifier, seen=()):
        d.g.require(identifier not in seen, 'Cyclic authored Composition context')
        return [{**element(p), 'parents': contexts(p, (*seen, identifier))}
                for p in parents.get(identifier, [])]

    for row in homes:
        row['source_element'] = element(row['source'])
        row['target_element'] = element(row['target'])
        target = row['target_element']
        row['feature_target_confirmed'] = target['native_type'] == 'Capability' and (
            target['harmoniaType'] == 'Feature' or target['name'].startswith('[Feature] '))
        d.g.require(row['feature_target_confirmed'], 'Current Business example does not target a Feature')
        row['capability_context'] = contexts(row['target'])
        node = model.relationships[row['identifier']]
        row['line'] = node.sourceline
        row['name'] = node.findtext('a:name', namespaces=d.g.N)
        row['xml'] = d.g.ET.tostring(node, encoding='unicode', with_tail=False)
    delta = {}
    for kind in ('elements', 'relationships', 'views'):
        old, new = getattr(prior, kind), getattr(model, kind)
        delta[kind] = dict(added=sorted(new.keys() - old.keys()), removed=sorted(old.keys() - new.keys()),
                           changed=sorted(k for k in old.keys() & new.keys()
                                          if signature(old[k], prior) != signature(new[k], model)))
    return dict(input=dict(path=str(incoming), sha256=model.sha256, bytes=Path(incoming).stat().st_size),
                previous_input=dict(path=str(previous), sha256=prior.sha256),
                published_baseline=dict(path=str(baseline), sha256=published.sha256),
                authored_homes=homes, delta_from_prior_input=delta,
                original_relationships_preserved=all(k in model.relationships and
                    signature(v, prior) == signature(model.relationships[k], model)
                    for k, v in prior.relationships.items()),
                schema_and_references='PASS', inferred_homes=0)


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--incoming', required=True, type=Path)
    p.add_argument('--previous', required=True, type=Path)
    p.add_argument('--baseline', required=True, type=Path)
    p.add_argument('--output', required=True, type=Path)
    args = p.parse_args()
    result = analyse(args.incoming, args.previous, args.baseline)
    args.output.write_text(json.dumps(result, ensure_ascii=False, indent=2) + '\n')
    print(json.dumps(dict(authored_homes=len(result['authored_homes']),
                         delta=result['delta_from_prior_input'],
                         native_relationships_preserved=result['original_relationships_preserved'])))


if __name__ == '__main__':
    main()
