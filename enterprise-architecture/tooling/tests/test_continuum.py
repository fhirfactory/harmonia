# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Meaningful corruption, creation, round-trip and publication boundary checks."""
import copy
from contextlib import contextmanager
import json
from pathlib import Path
import shutil
import sys
import tempfile
import unittest
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import architecture_continuum as c
import continuum_workbench as w
import publish_continuum as pub
import continuum_manifest as manifest
import continuum_navigation as nav
import markdown
from lxml import html


class ContinuumTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.tmp=tempfile.TemporaryDirectory(prefix='harmonia-continuum-test-')
        cls.root=Path(cls.tmp.name)
        for rel in (c.CONTINUUM,c.CONFIG,c.DESIGN,c.SCHEMAS):
            shutil.copytree(c.ROOT/rel,cls.root/rel,ignore=shutil.ignore_patterns('published','__pycache__'))
        shutil.copyfile(c.ROOT/'architecture-release.json',cls.root/'architecture-release.json')
        shutil.copyfile(c.ROOT/'enterprise-architecture/governance/authority-successors.json',cls.root/'enterprise-architecture/governance/authority-successors.json')
        cls.catalogue,cls.elements=c.load(cls.root)
        cls.cid=next(cid for cid,(m,_,_) in cls.elements.items() if m['archimateType']=='Principle')
        cls.baseline=cls.root/'baseline.xml';cls.baseline.write_bytes(c.project(cls.root)[0])
        cls.authority=cls.root/'review-authority.md';cls.authority.write_text('Synthetic test-only authority; no production architectural decision.\n')

    @classmethod
    def tearDownClass(cls):cls.tmp.cleanup()

    @contextmanager
    def changed(self,cid=None,**fields):
        cid=cid or self.cid;m,body,path=self.elements[cid];old=path.read_bytes();m=copy.deepcopy(m);m.update(fields)
        try:
            path.write_text(c.encode(m,body));yield m,body,path
        finally:path.write_bytes(old)

    def test_population_and_provenance(self):
        r=c.validate(self.root)
        self.assertEqual((r['elements'],r['relationships'],r['qualifiedSourcePredicates']),(1602,1651,462))
        self.assertEqual(r['elements'],r['provenanceCoverage'])

    def test_identity_type_location_and_standing_fail_closed(self):
        cases=[{'canonicalId':'bad'}, {'archimateType':'InventedDocument'}, {'specialization':'Feature'},
               {'standing':{'currentness':'current'}}, {'provenance':[]}, {'friendlyName':None}]
        for fields in cases:
            with self.subTest(fields=fields),self.changed(**fields),self.assertRaises(ValueError):c.validate(self.root)
        m,b,p=self.elements[self.cid];wrong=p.with_name('wrong.md');p.rename(wrong)
        try:
            with self.assertRaisesRegex(ValueError,'location'):c.validate(self.root)
        finally:wrong.rename(p)

    def test_duplicate_binding_and_withdrawn_id(self):
        other=next(m for cid,(m,b,p) in self.elements.items() if cid!=self.cid)
        with self.changed(sourceBindings=other['sourceBindings']),self.assertRaisesRegex(ValueError,'multiple identities'):c.validate(self.root)
        path=self.root/c.CONFIG/'migration-boundary.json';old=path.read_bytes();data=json.loads(old)
        data['withdrawnUnacceptedAllocations'].append({'canonicalId':self.cid});path.write_bytes(c.encoded(data))
        try:
            with self.assertRaisesRegex(ValueError,'Withdrawn'):c.validate(self.root)
        finally:path.write_bytes(old)

    def test_qualified_predicates_keep_subject_target_and_standing(self):
        cid=next(cid for cid,(m,b,p) in self.elements.items() if m.get('qualifiedAssertions'))
        for key,value in [('sourceCanonicalId','0'*32),('targetCanonicalId','0'*32),('record_state','')]:
            rows=copy.deepcopy(self.elements[cid][0]['qualifiedAssertions']);rows[0][key]=value
            with self.subTest(key=key),self.changed(cid,qualifiedAssertions=rows),self.assertRaises(ValueError):c.validate(self.root)

    def test_native_information_naming_is_checked_independently(self):
        cid=next(cid for cid,(m,b,p) in self.elements.items() if m.get('specialization')=='Topic')
        naming=copy.deepcopy(self.elements[cid][0]['naming']);naming['expected']['friendlyName']='WrongName'
        with self.changed(cid,friendlyName='WrongName',naming=naming),self.assertRaisesRegex(ValueError,'information naming'):
            c.project(self.root)

    def test_containment_cycle_is_rejected(self):
        cid=next(cid for cid,(m,b,p) in self.elements.items() if m['archimateType']=='Capability' and m['relationships'])
        m=self.elements[cid][0];relations=copy.deepcopy(m['relationships'])
        relations.append({'identifier':'test-cycle','source':m['exchange']['identifier'],'target':m['exchange']['identifier'],
            'archimateType':'Composition','standing':{'semantic':'Proposed'},'provenance':{'source':'synthetic test'},'properties':[]})
        with self.changed(cid,relationships=relations),self.assertRaisesRegex(ValueError,'containment'):
            c.project(self.root)

    def valid_spec(self):
        spec=c.template('Principle',root=self.root);m=spec['metadata'];m.update(displayName='ABC',friendlyName='Abc',canonicalName='MOT-Abc',standing={'semantic':'Proposed','currentness':'current'},identity={'authority':'Synthetic authorised creation test'},provenance=[{'source':'test-input','selector':'ABC','role':'test definition','maintained':True}],sourceBindings=[{'source':'test-input','selector':'ABC'}])
        m['naming']['inputs']=[{'key':'test-input:ABC','type':'Principle','name':'ABC'}]
        spec['navigationParent']=['Motivation','Unclassified Motivation']
        spec['body']='# ABC\n\n## Architectural content\n\nA proposed test principle.\n'
        return spec

    def test_creation_invalid_input_allocates_and_writes_nothing(self):
        invalid=self.valid_spec();invalid['metadata']['standing']={}
        empty=self.valid_spec();empty['body']='# ABC\n\n## Architectural content\n\n'
        for spec in (invalid,empty):
            with patch.object(c.uuid,'uuid4',side_effect=AssertionError('Unexpected allocation')):
                with self.assertRaises(ValueError):c.create(spec,self.root)
        self.assertEqual(len(c.load(self.root)[1]),1602)

    def test_native_endpoints_reject_schema_valid_but_illegal_relationship(self):
        cid = next(cid for cid, (m, _, _) in self.elements.items()
                   if m['archimateType'] == 'Capability' and m['relationships'])
        rows = copy.deepcopy(self.elements[cid][0]['relationships'])
        # Capability cannot assign behaviour, even though Assignment is an XSD type.
        rows[0]['archimateType'] = 'Assignment'
        with self.changed(cid, relationships=rows), self.assertRaisesRegex(ValueError, 'native relationship endpoints'):
            c.validate(self.root)

    def test_reserved_historical_identity_cannot_become_a_second_current_capability(self):
        cid = next(cid for cid, (m, _, _) in self.elements.items() if m['identity'].get('correspondence'))
        standing = copy.deepcopy(self.elements[cid][0]['standing']); standing['currentness'] = 'current'
        with self.changed(cid, standing=standing), self.assertRaisesRegex(ValueError, 'remain reserved'):
            c.validate(self.root)
        identity = copy.deepcopy(self.elements[cid][0]['identity'])
        identity['correspondence']['successorCanonicalId'] = cid
        with self.changed(cid, identity=identity), self.assertRaisesRegex(ValueError, 'identity correspondence'):
            c.validate(self.root)

    def test_native_predicate_cannot_lose_qualification_or_reverse_enablement(self):
        cid = next(cid for cid, (m, _, _) in self.elements.items()
                   if any(q['source_assertion']['type'] == 'enabled_by' for q in m.get('qualifiedAssertions', [])))
        rows = copy.deepcopy(self.elements[cid][0]['qualifiedAssertions'])
        row = next(q for q in rows if q['source_assertion']['type'] == 'enabled_by')
        row['nativeMapping']['reverse'] = False
        with self.changed(cid, qualifiedAssertions=rows), self.assertRaisesRegex(ValueError, 'Serving rule'):
            c.validate(self.root)
        rows = copy.deepcopy(self.elements[cid][0]['qualifiedAssertions'])
        row = next(q for q in rows if q['source_assertion']['type'] == 'enabled_by')
        row['source_assertion']['status'] = 'unresolved'
        with self.changed(cid, qualifiedAssertions=rows), self.assertRaisesRegex(ValueError, 'Unestablished relationship'):
            c.validate(self.root)

    def test_implementation_guideline_keeps_course_of_action_semantics(self):
        cid = next(cid for cid, (m, _, _) in self.elements.items() if m.get('specialization') == 'ImplementationGuideline')
        m = self.elements[cid][0]
        self.assertEqual(m['archimateType'], 'CourseOfAction')
        self.assertIsNone(m['canonicalName'])
        self.assertIn('canonicalName', m['unresolvedProperties'])
        with self.changed(cid, archimateType='Requirement'), self.assertRaisesRegex(ValueError, 'incompatible'):
            c.load(self.root)

    def test_creation_then_duplicate_retry_preserves_one_identity(self):
        nav_path=self.root/nav.SOURCE;nav_before=nav_path.read_bytes()
        spec=self.valid_spec();path=c.create(spec,self.root)
        try:
            cid=c.decode(path.read_text())[0]['canonicalId'];self.assertEqual(cid[12],'4')
            with patch.object(c.uuid,'uuid4',side_effect=AssertionError('Unexpected allocation')):
                with self.assertRaisesRegex(ValueError,'already bound'):c.create(spec,self.root)
            self.assertEqual(c.decode(path.read_text())[0]['canonicalId'],cid)
        finally:
            path.unlink()
            nav_path.write_bytes(nav_before)

    def test_projection_and_publication_are_deterministic_and_allocate_nothing(self):
        before={cid:c.sha(p.read_bytes()) for cid,(m,b,p) in self.elements.items()}
        with patch.object(c.uuid,'uuid4',side_effect=AssertionError('Unexpected allocation')):
            a,report=c.project(self.root);b,_=c.project(self.root);self.assertEqual(a,b)
            output=self.root/c.PUBLICATION/'1.1.5'
            first=pub.outputs(self.root,output);self.assertEqual(first,pub.outputs(self.root,output))
        self.assertEqual(before,{cid:c.sha(p.read_bytes()) for cid,(m,b,p) in self.elements.items()})
        self.assertEqual(report['informationNaming']['metadata_changes'],{})
        template=c.secure_xml(self.root/c.DESIGN/'projection-template.xml');model=c.ET.fromstring(a)
        self.assertEqual(c.ET.tostring(template.find('a:views',c.N)),c.ET.tostring(model.find('a:views',c.N)))
        projected={node.get('identifier'):node for node in model.findall('a:elements/a:element',c.N)}
        for m,body,_ in self.elements.values():
            self.assertEqual(projected[m['exchange']['identifier']].findtext('a:documentation',namespaces=c.N),
                             body.split('## Architectural content\n',1)[1].strip())
        self.assertNotRegex(a,rb'[^\s][ \t]+\r?\n')

    def manifest_document(self):
        return html.fromstring(markdown.markdown(manifest.render(self.root).decode(), extensions=['tables']))

    def test_manifest_covers_each_element_once_in_canonical_navigation_with_source_values(self):
        doc=self.manifest_document()
        rows=doc.xpath('//tr[count(td)=9]')
        tree=nav.load(self.root,self.elements)
        nodes=[n for n in nav.walk(tree) if n.get('canonicalId')]
        self.assertEqual(len(rows),len(self.elements))
        self.assertEqual([r[6].text_content() for r in rows],[n['canonicalId'] for n in nodes])
        for row,node in zip(rows,nodes):
            m=self.elements[node['canonicalId']][0]
            cells=[td.text_content() for td in row]
            self.assertEqual(cells[:4],[node['number'],m['displayName'],m['archimateType'],m.get('specialization') or '—'])
            self.assertEqual(cells[4],'Semantic: '+m['standing']['semantic']+'Currentness: '+m['standing']['currentness'])
            self.assertEqual(cells[5],m['canonicalName'] or 'Unresolved')
            relative=self.elements[m['canonicalId']][2].relative_to(self.root/c.CONTINUUM).as_posix()
            self.assertEqual(cells[7],relative)
            self.assertEqual(row[7][0].get('href'),relative)
            for key,reason in m.get('unresolvedProperties',{}).items():
                self.assertIn('Unresolved '+key+': '+reason,cells[8])
        anchors={a.get('id') for a in doc.xpath('//*[@id]')}
        for a in doc.xpath('//a[@href]'):
            target=a.get('href')
            if target.startswith('#'):
                self.assertIn(target[1:],anchors)
        for node in nav.walk(tree):
            self.assertIn(nav.title(node),doc.text_content())

    def test_manifest_escapes_source_text_without_hiding_qualifications(self):
        value='Proposed | [label](fake-target) *qualified* `literal` & <script>x</script> \\ suffix'
        standing={'semantic':value,'currentness':'pending_adjudication','qualification':'First line\nSecond | line'}
        with self.changed(standing=standing,sourceUnresolved={'dimensions':['a|b'],'adjudication':None}):
            doc=self.manifest_document()
        row=next(r for r in doc.xpath('//tr[count(td)=9]') if r[6].text_content()==self.cid)
        self.assertEqual(row[4].text_content(),'Semantic: '+value+'Currentness: pending_adjudication')
        self.assertIn('standing.qualification: First lineSecond | line',row[8].text_content())
        self.assertIn('"adjudication": null',row[8].text_content())
        self.assertFalse(row.xpath('.//script|.//em|.//code|.//a[@href="fake-target"]'))
        self.assertTrue(row[8].xpath('.//br'))

    def test_manifest_check_rejects_missing_stale_and_changed_metadata_without_writing(self):
        path=self.root/c.MANIFEST;original=path.read_bytes() if path.exists() else None
        try:
            path.unlink(missing_ok=True)
            with self.assertRaisesRegex(ValueError,'Missing/stale'):manifest.publish(self.root,check=True)
            self.assertFalse(path.exists())
            manifest.publish(self.root);content=path.read_bytes()
            manifest.publish(self.root,check=True)
            path.write_bytes(content+b'\nmanual change\n')
            with self.assertRaisesRegex(ValueError,'Missing/stale'):manifest.publish(self.root,check=True)
            self.assertEqual(path.read_bytes(),content+b'\nmanual change\n')
            path.write_bytes(content)
            with self.changed(standing={'semantic':'Proposed test change','currentness':'pending_adjudication'}):
                with self.assertRaisesRegex(ValueError,'Missing/stale'):manifest.publish(self.root,check=True)
            self.assertEqual(path.read_bytes(),content)
        finally:
            if original is None:path.unlink(missing_ok=True)
            else:path.write_bytes(original)

    def test_manifest_exclusion_is_exact_and_invalid_elements_still_fail(self):
        for relative in ('README.md','motivation/MANIFEST.md'):
            path=self.root/c.CONTINUUM/relative;path.write_text('# Not an element\n')
            try:
                with self.assertRaisesRegex(ValueError,'front matter'):manifest.render(self.root)
            finally:
                path.unlink()
        with self.changed(archimateType='InventedType'),self.assertRaises(ValueError):manifest.render(self.root)

    def test_manifest_is_deterministic_and_changes_no_sources_or_projection(self):
        before={cid:c.sha(p.read_bytes()) for cid,(_,_,p) in self.elements.items()}
        path=self.root/c.MANIFEST;original=path.read_bytes() if path.exists() else None
        try:
            with patch.object(c.uuid,'uuid4',side_effect=AssertionError('Unexpected allocation')):
                first=manifest.render(self.root)
                with patch.object(c,'load',return_value=(self.catalogue,dict(reversed(list(self.elements.items()))))):
                    self.assertEqual(first,manifest.render(self.root))
                manifest.publish(self.root)
                self.assertEqual(first,path.read_bytes())
                self.assertEqual(self.baseline.read_bytes(),c.project(self.root)[0])
                self.assertEqual(before,{cid:c.sha(p.read_bytes()) for cid,(_,_,p) in c.load(self.root)[1].items()})
        finally:
            if original is None:path.unlink(missing_ok=True)
            else:path.write_bytes(original)

    def test_workbench_exact_baseline_and_complete_dispositions(self):
        r=w.review(self.baseline,self.baseline,self.root);self.assertFalse(r['elementChanges']);self.assertFalse(r['relationshipAndViewChanges'])
        bad=self.root/'not-exact.xml';bad.write_bytes(self.baseline.read_bytes()+b'\n')
        with self.assertRaisesRegex(ValueError,'exact current'):w.review(bad,self.baseline,self.root)
        tree=c.secure_xml(self.baseline);rid=self.elements[self.cid][0]['exchange']['identifier'];node=tree.find("a:elements/a:element[@identifier='"+rid+"']/a:documentation",c.N);node.text+='\nSynthetic reviewed amendment.'
        submitted=self.root/'submitted.xml';submitted.write_bytes(c.ET.tostring(tree));r=w.review(self.baseline,submitted,self.root)
        evidence=self.root/'review.json';evidence.write_bytes(c.encoded(r))
        decision={'authority':'review-authority.md','review':'review.json','reviewSha256':c.sha(evidence.read_bytes()),'replacements':[]}
        with self.assertRaisesRegex(ValueError,'every reviewed change'):w.apply(decision,self.root)
        decision['dispositions']={self.cid:{'action':'apply','reason':'Synthetic approved amendment'}}
        m,b,p=self.elements[self.cid];old=p.read_bytes();decision['replacements']=[{'metadata':m,'body':b+'\nSynthetic reviewed amendment.\n'}]
        try:
            result=w.apply(decision,self.root);self.assertEqual(result['changed'],1)
            with self.assertRaisesRegex(ValueError,'changed since review'):w.apply(decision,self.root)
        finally:p.write_bytes(old)

    def test_review_hash_must_match(self):
        evidence=self.root/'tampered.json';evidence.write_text('{}')
        with self.assertRaisesRegex(ValueError,'evidence changed'):w.apply({'authority':'review-authority.md','review':'tampered.json','reviewSha256':'0'*64},self.root)

if __name__=='__main__':unittest.main()
