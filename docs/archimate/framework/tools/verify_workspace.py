#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded 1.0.7 → 1.0.8 migration proof, not an architecture reconciler."""
import copy
import hashlib
import json

import generate_views as g
from workspace_paths import PUBLISH

BASELINE = PUBLISH / '1.0.7/Harmonia-Architecture-1.0.7.xml'
BASELINE_SHA256 = 'def447de2f522737c8b9c30dd9850a57bfec71eaa01f0820ff49283c40fba4bb'


def compare_release_only(before, after):
    """Require exact XML equality after restoring exactly one model release value.

    This migration-specific assertion changes no fidelity comparator or transport
    classification. No architecture, presentation, ordering or path is excluded.
    """
    old, new = g.ET.fromstring(before), g.ET.fromstring(after)
    path = "a:properties/a:property[@propertyDefinitionRef='baseline-architecture-release']/a:value"
    previous, current = old.findall(path, g.N), new.findall(path, g.N)
    g.require(len(previous) == len(current) == 1, 'Expected exactly one model release value')
    g.require(previous[0].text == '1.0.7' and current[0].text == '1.0.8', 'Unexpected migration release transition')
    restored = copy.deepcopy(new)
    restored.find(path, g.N).text = previous[0].text
    # Preserve text, tails, attributes, order, geometry, bendpoints and all Properties.
    original_xml, restored_xml = g.ET.tostring(old), g.ET.tostring(restored)
    g.require(original_xml == restored_xml, 'Unexpected architecture or presentation difference')
    # The actual published byte stream must also differ at exactly this one value.
    old_value, new_value = b'>1.0.7</value>', b'>1.0.8</value>'
    g.require(after.count(new_value) == 1 and after.replace(new_value, old_value, 1) == before,
              'Unexpected publication byte difference beyond model release metadata')
    counts = {name: len(old.findall(path, g.N)) for name, path in (
        ('semantic_elements', 'a:elements/a:element'),
        ('relationships', 'a:relationships/a:relationship'),
        ('views', 'a:views/a:diagrams/a:view'),
        ('visual_objects', 'a:views/a:diagrams/a:view//a:node'),
        ('visual_connections', 'a:views/a:diagrams/a:view//a:connection'),
        ('bendpoints', 'a:views/a:diagrams/a:view//a:bendpoint'))}
    return dict(status='PASS', before_sha256=hashlib.sha256(before).hexdigest(),
                after_sha256=hashlib.sha256(after).hexdigest(), counts=counts,
                exact_architecture_and_presentation_sha256=hashlib.sha256(original_xml).hexdigest(),
                architecture_semantic_delta='NONE', architecture_identity_delta='NONE',
                architecture_relationship_delta='NONE', architecture_presentation_delta='NONE',
                only_xml_difference='One model-level baseline-architecture-release value: 1.0.7 → 1.0.8')


def verify():
    from governed_metadata import model_path
    before = BASELINE.read_bytes()
    g.require(hashlib.sha256(before).hexdigest() == BASELINE_SHA256, 'Immutable accepted 1.0.7 publication changed')
    return compare_release_only(before, model_path().read_bytes())


if __name__ == '__main__':
    print(json.dumps(verify(), indent=2))
