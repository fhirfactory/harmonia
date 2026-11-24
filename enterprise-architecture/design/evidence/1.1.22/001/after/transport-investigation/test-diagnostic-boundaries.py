#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Adversarial checks on diagnostic dimensions; strict checks stay unchanged."""
import copy
import importlib.util
from pathlib import Path
import sys
import tempfile
import unittest

HERE=Path(__file__).resolve().parent
spec=importlib.util.spec_from_file_location('transport_analysis',HERE/'analyse-transport.py')
a=importlib.util.module_from_spec(spec);spec.loader.exec_module(a)
sys.path.insert(0,str(a.ROOT/'enterprise-architecture/tooling/tests'))
from test_current_ea import MODEL
d,g=a.d,a.g

class DiagnosticBoundaries(unittest.TestCase):
    def setUp(self):
        self.tmp=tempfile.TemporaryDirectory();self.addCleanup(self.tmp.cleanup)
        self.path=Path(self.tmp.name)/'model.xml';self.path.write_text(MODEL)
        self.model=d.Exchange(self.path)

    def test_relationship_type_direction_endpoint_and_access_changes_remain_detectable(self):
        original=self.model.relationships['composition']
        for attr,value in ((g.TYPE,'Association'),('source','two'),('target','one'),('accessType','Read')):
            changed=copy.deepcopy(original);changed.set(attr,value)
            self.assertNotEqual(a.signature(original,self.model,True),a.signature(changed,self.model,True),attr)

    def test_text_properties_multiplicity_and_whitespace_are_preserved(self):
        original=self.model.elements['one']
        for mutation in ('text','value','duplicate','whitespace'):
            changed=copy.deepcopy(original)
            if mutation=='text':changed.find('a:name',g.N).text='Different'
            elif mutation=='value':changed.find('.//a:value',g.N).text='different'
            elif mutation=='duplicate':changed.find('a:properties',g.N).append(copy.deepcopy(changed.find('.//a:property',g.N)))
            else:changed.find('a:name',g.N).text=' One '
            self.assertNotEqual(a.signature(original,self.model,True),a.signature(changed,self.model,True),mutation)

    def test_language_is_a_separately_visible_raw_dimension(self):
        self.model.elements['one'].find('a:name',g.N).set(d.LANG,'fr')
        changed=self.path.with_name('changed.xml');changed.write_bytes(g.ET.tostring(self.model.root))
        raw=d.compare(d.Exchange(self.path),d.Exchange(changed))
        self.assertEqual(raw['classifications'][d.SEMANTIC],1)
        self.assertEqual(raw['findings'][0]['after'],'fr')
        self.assertIn('one',raw['elements']['changed_records'])

    def test_view_geometry_styles_bendpoints_and_order_are_not_implicitly_unordered(self):
        original=g.ET.fromstring(('<view xmlns="'+g.NS+'" identifier="v"><node identifier="n" x="1" y="2" w="3" h="4"/><connection identifier="c"><bendpoint x="5" y="6"/><bendpoint x="7" y="8"/></connection></view>').encode())
        for mutation in ('geometry','style','routing-order','child-order'):
            changed=copy.deepcopy(original)
            if mutation=='geometry':changed[0].set('x','99')
            elif mutation=='style':g.ET.SubElement(changed[1],g.tag('style'),lineWidth='9')
            elif mutation=='routing-order':changed[1].append(changed[1][0])
            else:changed.append(changed[0])
            self.assertNotEqual(a.signature(original,self.model),a.signature(changed,self.model),mutation)

    def test_nonredundant_labels_and_folder_metadata_are_not_erased_by_signature(self):
        original=g.ET.fromstring(('<item xmlns="'+g.NS+'" identifierRef="one"><label>Alias</label><documentation>Authored</documentation></item>').encode())
        for mutation in ('alias','documentation','reference'):
            changed=copy.deepcopy(original)
            if mutation=='alias':changed[0].text='Different'
            elif mutation=='documentation':changed[1].text='Different'
            else:changed.set('identifierRef','two')
            self.assertNotEqual(a.signature(original,self.model),a.signature(changed,self.model),mutation)

if __name__=='__main__':unittest.main(verbosity=2)
