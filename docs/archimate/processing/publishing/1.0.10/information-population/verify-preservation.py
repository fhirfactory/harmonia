#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independent before/after semantic, identity, View and navigation census."""
import collections
import hashlib
import json
from pathlib import Path
import sys

ROOT = next(p for p in Path(__file__).resolve().parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import generate_views as g
import governed_metadata as m
import information_population as ia

source = ia.load()
before_path = ROOT / source['originating_model']['path']
before, after = g.parse(before_path), g.parse(m.model_path())
g.load_schema(g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(after)
g.validate_identifiers(after)
counts = {}
for name, path in [('elements', 'a:elements/a:element'), ('relationships', 'a:relationships/a:relationship'),
                   ('views', 'a:views/a:diagrams/a:view')]:
    old = {e.get('identifier'): e for e in before.findall(path, g.N)}
    new = {e.get('identifier'): e for e in after.findall(path, g.N)}
    assert old.keys() <= new.keys(), name
    assert all(g.signature(e) == g.signature(new[key]) for key, e in old.items()), name
    counts[name] = dict(existing_preserved=len(old), introduced=len(new)-len(old))
old_ids = {e.get('identifier') for e in before.iter() if e.get('identifier')}
new_ids = {e.get('identifier') for e in after.iter() if e.get('identifier')}
assert old_ids <= new_ids
added = {e.get('identifier'): e for e in after.findall('a:elements/a:element', g.N)
         if e.get('identifier') not in old_ids}
assert set(added) == {e['representationId'] for e in source['elements']}
assert collections.Counter(e.get(g.TYPE) for e in added.values()) == {'BusinessObject': 50, 'DataObject': 4}
assert not any(r.get('source') in added or r.get('target') in added
               for r in after.findall('a:relationships/a:relationship', g.N))
names = [e.findtext('a:name', namespaces=g.N) for e in after.findall('a:elements/a:element', g.N)
         if e.get(g.TYPE) in ('BusinessObject', 'DataObject', 'Artifact')]
assert len(names) == len(set(names)) == 54
assert not any(e.get(g.TYPE) == 'Artifact' for e in after.findall('a:elements/a:element', g.N))
assert not any(name.startswith('Endpoint ') for name in names)
assert all(not e.findall("a:properties/a:property[@propertyDefinitionRef='governed-canonical-name']", g.N)
           for e in added.values())
assert g.signature(ia.project(before, source)) == g.signature(after)
print(json.dumps(dict(status='PASS', counts=counts, every_preexisting_xml_identity='PRESERVED',
    prior_model_sha256=hashlib.sha256(before_path.read_bytes()).hexdigest(),
    current_model_sha256=hashlib.sha256(m.model_path().read_bytes()).hexdigest(),
    source_capture_sha256=hashlib.sha256(ia.SOURCE.read_bytes()).hexdigest(),
    IA_semantic_relationships=0, Artifacts=0, information_names='54 UNIQUE',
    candidate_and_ambiguous_promotion='NONE: exact reviewed allowlist only',
    canonicalName='STOPPED on all 54 new resources; existing names exact'), indent=2))
