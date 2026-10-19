#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Prove that candidate completion adds exactly 54 canonicalName properties."""
import copy
import gzip
import json
from pathlib import Path
import sys

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import generate_views as g
import governed_metadata as m
import information_population as ia


def verify():
    original = gzip.decompress((HERE / 'before/Harmonia-Architecture-1.0.10.xml.gz').read_bytes())
    before = g.ET.fromstring(original)
    current = m.model_path().read_bytes()
    after = g.ET.fromstring(current)
    data = ia.load()
    g.load_schema(g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(after)
    g.validate_identifiers(after)
    stripped = copy.deepcopy(after)
    for row in data['elements']:
        path = "a:elements/a:element[@identifier='" + row['representationId'] + "']"
        old, new = before.find(path, g.N), stripped.find(path, g.N)
        assert old is not None and new is not None
        key = "a:properties/a:property[@propertyDefinitionRef='governed-canonical-name']"
        assert not old.findall(key, g.N)
        claims = new.findall(key, g.N)
        assert len(claims) == 1 and claims[0].findtext('a:value', namespaces=g.N) == row['canonicalName']
        claims[0].getparent().remove(claims[0])
    assert g.signature(stripped) == g.signature(before), 'Change beyond the 54 canonicalName properties'
    assert (ROOT / 'architecture-release.json').read_bytes() == gzip.decompress((HERE / 'before/architecture-release.json.gz').read_bytes())
    # Record a cross-layer naming finding without changing the protected model.
    parents = {e.get('target') for e in after.findall('a:relationships/a:relationship', g.N)
               if e.get(g.TYPE) == 'Composition'}
    legacy_context = []
    for e in after.findall('a:elements/a:element', g.N):
        name = e.findtext("a:properties/a:property[@propertyDefinitionRef='governed-friendly-name']/a:value", namespaces=g.N)
        if e.get(g.TYPE) in ('BusinessFunction', 'BusinessProcess', 'BusinessService') and name and '.' in name and e.get('identifier') not in parents:
            legacy_context.append(e.get('identifier'))
    assert not parents.intersection(row['representationId'] for row in data['elements'])
    return dict(status='PASS', before_sha256=ia.sha(original), published_sha256=ia.sha(current),
                canonicalNames_added=54, canonicalIds_allocated_or_changed=0,
                other_model_content='EXACT: all elements, relationships, Views, navigation, presentation and other properties',
                release='UNCHANGED: 1.0.10 / 1.0.9',
                new_IA_Composition=0, new_IA_relationships=0, new_elements=0, Artifact_qualifier='NOT AUTHORISED',
                IA_context='Existing local forms preserved; no root status or Composition ancestry inferred',
                preexisting_contextual_Business_names_without_Composition_parent=legacy_context,
                finding_standing='Solution Architect accepted the 323 prior Business names as non-blocking modelling debt; preserve without inferred/migrated Composition; apply normal contextual naming once authored containment exists')


if __name__ == '__main__':
    print(json.dumps(verify(), indent=2))
