# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Promotion preserves accepted architecture and never reallocates identities."""
import copy
from pathlib import Path
import sys
import tempfile
import unittest
from unittest import mock

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / 'tools'))
import publish_information_release as p


class InformationReleaseTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        with mock.patch.object(p, 'load_release', return_value=p.RELEASE), mock.patch.object(
                p.m, 'allocate_missing', side_effect=AssertionError('Publication allocated identity')):
            cls.outputs, _, cls.report = p.publish()
        cls.decision, cls.plan, cls.source, cls.models, _ = p.inputs()
        cls.root = p.g.ET.fromstring(cls.outputs[p.MODEL])

    def test_publication_reuses_every_persisted_identity_and_preserves_rebindings(self):
        self.assertEqual(0, self.report['identities_allocated_by_publication'])
        self.assertEqual(1362, self.report['existing_canonical_identities_preserved'])
        self.assertEqual(1394, self.report['canonical_identities'])
        self.assertEqual(38, self.report['rebindings_preserved'])
        current = p.r.model_at(self.models['candidate'], self.root)
        for row in self.source['elements']:
            node = current.elements[row['representationId']]
            self.assertEqual(row['canonicalId'], current.single(node, 'canonicalId'))

    def test_held_classification_and_authored_views_remain_exact(self):
        candidate = self.models['candidate']
        for row in self.source['held']:
            rid = row['id']
            node = self.root.find("a:elements/a:element[@identifier='%s']" % rid, p.g.N)
            self.assertEqual(p.g.signature(candidate.elements[rid]), p.g.signature(node))
        for path in ('a:views', "a:organizations/a:item[a:label='Views']"):
            self.assertEqual(p.g.ET.tostring(candidate.root.find(path, p.g.N)),
                             p.g.ET.tostring(self.root.find(path, p.g.N)))
        self.assertEqual(154, len(self.report['accepted_crs']))
        self.assertEqual(4, len(self.report['held_unchanged']))

    def test_only_model_release_and_candidate_standing_are_promoted(self):
        candidate = self.models['candidate']
        definitions = {x.findtext('a:name', namespaces=p.g.N) for x in
                       self.root.findall('a:propertyDefinitions/a:propertyDefinition', p.g.N)}
        self.assertNotIn(p.r.naming.MARKER, definitions)
        current = p.r.model_at(candidate, self.root)
        self.assertEqual('1.1.0', current.single(self.root, 'Baseline / Architecture Release'))
        self.assertEqual([], self.report['unexpected_architectural_differences'])
        self.assertEqual(1137, self.report['after']['relationships'])
        self.assertEqual(1209, self.report['after']['views'])
        # A copied model can be promoted without mutating its input or children.
        original = copy.deepcopy(candidate.root)
        before = p.g.serialize(original)
        promoted = p.release_metadata(original)
        self.assertEqual(before, p.g.serialize(original))
        self.assertEqual(p.g.signature(original.find('a:elements', p.g.N)),
                         p.g.signature(promoted.find('a:elements', p.g.N)))

    def test_changed_pinned_capture_fails_closed(self):
        with tempfile.TemporaryDirectory(dir='/tmp') as temp:
            source = Path(temp) / 'source.json'
            source.write_text('changed identity source')
            with self.assertRaisesRegex(ValueError, 'Promotion input changed'):
                p.pinned(dict(path=str(source), sha256='0' * 64))

    def test_wrong_release_cannot_publish(self):
        with mock.patch.object(p, 'load_release', return_value=dict(version='1.1.1', previous_version='1.1.0')):
            with self.assertRaisesRegex(ValueError, 'explicit root release authority'):
                p.publish()


if __name__ == '__main__':
    unittest.main()
