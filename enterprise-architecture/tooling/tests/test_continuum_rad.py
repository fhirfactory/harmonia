# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Publication boundary: model correspondence, qualification and no enrichment."""
import json
from pathlib import Path
import shutil
import sys
import tempfile
import unittest
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c
import publish_continuum_rad as rad
import continuum_navigation as nav


class ContinuumRadTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.temp = tempfile.TemporaryDirectory(prefix='harmonia-rad-test-')
        cls.root = Path(cls.temp.name)
        for rel in (c.CONTINUUM, c.CONFIG, c.DESIGN, c.SCHEMAS, rad.SOURCE):
            shutil.copytree(c.ROOT / rel, cls.root / rel, ignore=shutil.ignore_patterns('published', '__pycache__'))
        shutil.copyfile(c.ROOT / 'architecture-release.json', cls.root / 'architecture-release.json')
        shutil.copyfile(c.ROOT / 'enterprise-architecture/governance/authority-successors.json', cls.root / 'enterprise-architecture/governance/authority-successors.json')
        cls.elements = c.load(cls.root)[1]
        cls.output = cls.root / c.RAD_PUBLICATION / c.read_json(cls.root / 'architecture-release.json')['version']
        cls.before = {cid: path.read_bytes() for cid, (_, _, path) in cls.elements.items()}
        with patch.object(c.uuid, 'uuid4', side_effect=AssertionError('Publication allocated identity')):
            cls.artifacts = rad.outputs(cls.root, cls.output)
        cls.structure = json.loads(cls.artifacts['structure.json'])

    @classmethod
    def tearDownClass(cls):
        cls.temp.cleanup()

    def test_complete_correspondence_and_unchanged_canonical_sources(self):
        rows = self.structure['elements']
        self.assertEqual(len(rows), len(self.elements))
        self.assertEqual({r['canonicalId'] for r in rows}, set(self.elements))
        for row in rows:
            m, _, path = self.elements[row['canonicalId']]
            self.assertEqual(path.read_bytes(), self.before[row['canonicalId']])
            self.assertEqual(row['standing'], m['standing'])
            self.assertEqual(row['unresolvedProperties'], m.get('unresolvedProperties', {}))
            self.assertEqual(row['publication'], 'Continuum narrative')

    def test_hierarchy_matches_canonical_coordinates_and_exchange_to_full_depth(self):
        model = c.ET.fromstring(c.project(self.root)[0])
        nodes = list(nav.walk(nav.load(self.root,self.elements)))
        headings = self.structure['headings']
        self.assertEqual([h['number'] for h in headings],[n['number'] for n in nodes])
        for heading,node in zip(headings,nodes):
            projected, = model.xpath(heading['locator'])
            self.assertEqual(projected.findtext('a:label',namespaces=c.N),nav.title(node))
            self.assertEqual(heading['title'],node['title'])
            self.assertEqual(heading['depth'],len(node['path'])-2)
            self.assertEqual(heading['origin'],'canonical Continuum navigation')
        self.assertGreater(max(h['depth'] for h in headings),4)
        self.assertEqual([p['canonicalId'] for p in self.structure['elements']],
                         [n['canonicalId'] for n in nodes if n.get('canonicalId')])
        forbidden={'Continuum additions','Unreconciled 1.0.4 Reference','BusinessObjectMap','InformationMap','Application Layer','Business Layer','Stakeholders'}
        self.assertFalse(forbidden & {h['title'] for h in headings})
        master=self.artifacts['continuum-rad.tex'].decode()
        self.assertNotIn(r'\appendix',master)
        self.assertIn('Architectural Name Index',master)
        expected={'parts/architecture-governance.tex','parts/motivation.tex','parts/strategy.tex',
                  'parts/business.tex','parts/application.tex','parts/technology.tex','parts/implementation-and-migration.tex'}
        self.assertEqual({name for name in self.artifacts if name.startswith('parts/')},expected)
        for name in expected:
            self.assertIn(r'\input{'+name+'}',master)
        for part in ['technology','implementation-and-migration']:
            text=self.artifacts['parts/'+part+'.tex'].decode()
            self.assertIn('No individually represented',text)
            self.assertNotIn(r'\chapter',text)
        metadata=json.loads(self.artifacts['publication.json'])
        self.assertEqual(set(metadata['sha256']),set(self.artifacts)-{'publication.json'})

    def test_diagrams_retain_exact_existing_assertions_and_direction(self):
        edges = {r['identifier']: r for m, _, _ in self.elements.values() for r in m.get('relationships', [])}
        self.assertTrue(self.structure['diagrams'])
        for diagram in self.structure['diagrams']:
            for edge in diagram['edges']:
                actual = edge['relationship']
                self.assertEqual(actual, edges[actual['identifier']])
                self.assertEqual(actual['source'], self.elements[edge['sourceCanonicalId']][0]['exchange']['identifier'])
                self.assertEqual(actual['target'], self.elements[edge['targetCanonicalId']][0]['exchange']['identifier'])
                self.assertNotIn(edge['recordState'], ('superseded', 'historical_reference'))

    def test_deterministic_without_legacy_rad_and_note_source_failure(self):
        self.assertFalse((self.root / 'docs').exists())
        with patch.object(c.uuid, 'uuid4', side_effect=AssertionError('Publication allocated identity')):
            self.assertEqual(self.artifacts, rad.outputs(self.root, self.output))
        notes = self.root / rad.SOURCE / 'reader-notes.json'
        before = notes.read_bytes()
        changed = json.loads(before)
        changed['application'][0]['sources'] = ['0' * 32]
        try:
            notes.write_bytes(c.encoded(changed))
            with self.assertRaisesRegex(ValueError, 'reader-note source'):
                rad.outputs(self.root, self.output)
        finally:
            notes.write_bytes(before)

    def test_canonical_ingestion_has_no_documentation_subtree_exception(self):
        unexpected = self.root / c.CONTINUUM / 'documentation/published/forbidden.md'
        unexpected.parent.mkdir(parents=True)
        unexpected.write_text('# Not a canonical element\n')
        try:
            with self.assertRaisesRegex(ValueError, 'front matter'):
                c.load(self.root)
        finally:
            unexpected.unlink()


if __name__ == '__main__':
    unittest.main()
