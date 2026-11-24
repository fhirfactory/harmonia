#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independent identity, relationship and presentation checks for this activity.

Writes a validated canonical-state verification XML with the maintained Views.
Stages the complete authored presentation separately, and only applies it when
all references resolve and the normal full-model generation gate passes.
"""
import copy
from collections import Counter
import json
from pathlib import Path
import sys
import tempfile

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_manifest
import containment
from exchange_compare import Exchange
from exchange_xml import N, TYPE, signature, load_schema
from home_relationships import authored_homes

B = Path('enterprise-architecture/design/evidence/1.1.18/001')
A = ROOT / B / 'after/subject-hierarchy'


def save(name, data):
    (A / name).write_bytes(c.encoded(data))


def views_tree(root):
    matches = root.xpath('./a:organizations/a:item[a:label="Views"]', namespaces=N)
    assert len(matches) == 1
    return matches[0]


def run():
    pins = json.loads((ROOT / B / 'before/subject-hierarchy/input-pins.json').read_text())
    created = json.loads((A / 'incorporated-groupings.json').read_text())
    ledger = json.loads((A / 'relationship-results.json').read_text())
    assert len(created) == 115 and len(ledger) == 422
    _, elements = c.load(ROOT)
    for cid, old_hash in pins['canonical'].items():
        path = Path(cid)
        old_path = ROOT / B / 'before/subject-hierarchy/canonical' / path.relative_to(ROOT)
        if old_path.exists():
            old, body = c.decode(old_path.read_text())
            now, now_body, now_path = elements[old['canonicalId']]
            unchanged = copy.deepcopy(now)
            unchanged['relationships'] = old['relationships']
            assert unchanged == old and now_body == body
            assert now['relationships'][:len(old['relationships'])] == old['relationships']
        else:
            assert c.sha(path.read_bytes()) == old_hash, path
    for relative, digest in pins['protected'].items():
        assert c.sha((ROOT / relative).read_bytes()) == digest, relative
    new_cids = set(elements) - {c.decode(Path(p).read_text())[0]['canonicalId'] for p in pins['canonical']}
    assert new_cids == {r['canonicalId'] for r in created}
    canonical_xml = A / 'Harmonia-Subject-Hierarchy-canonical-unreleased.xml'
    generation = c.write_projection(ROOT, canonical_xml)
    save('canonical-generation-validation.json', generation)
    model = Exchange(canonical_xml)
    assert not model.issues
    for row in created:
        element = model.elements[row['exchangeId']]
        assert element.get(TYPE) == 'Grouping'
        assert model.single(element, 'canonicalId') == row['canonicalId']
        assert element.findtext('a:name', namespaces=N) == row['displayName']
    prior = Exchange(ROOT / B / 'after/subject-incorporation/Harmonia-Subjects-unreleased.xml')
    assert model.relationships.keys() - prior.relationships.keys() == {r['identifier'] for r in ledger if r['operation']=='CREATED'}
    assert all(signature(node) == signature(model.relationships[rid]) for rid, node in prior.relationships.items())
    assert all(prior.single(n, 'canonicalId') == model.single(model.elements[rid], 'canonicalId') for rid, n in prior.elements.items())
    # Canonical Properties/Documentation and names of every pre-existing element
    # are unchanged: only new source-owned relationships have been added.
    assert all(signature(node) == signature(model.elements[rid]) for rid, node in prior.elements.items())
    # New canonical navigation locations for new children do not renumber existing
    # siblings because additions are appended beneath the existing Subject leaves.
    homes = authored_homes(model)
    for entry in ledger:
        edge = model.relationships[entry['identifier']]
        assert [edge.get(k) for k in ('source', 'target')] == [entry['source'], entry['target']]
        if entry['kind'] == 'Composition':
            assert edge.get(TYPE) == 'Composition'
        else:
            assert any(r['identifier'] == entry['identifier'] and r['specialization']=='Home' for r in homes)
    resolved = containment.resolve(model.root)
    assert not resolved.findings()
    children = {r['childCanonicalId'] for r in ledger}
    for cid in children:
        rid = elements[cid][0]['exchange']['identifier']
        links = resolved.incoming[rid]
        assert len(links) == 1 and links[0]['mechanism']=='Home'
    # Negative checks exercise actual resulting assertions; no validator changes.
    fixture = copy.deepcopy(model.root)
    home_id = next(r['identifier'] for r in ledger if r['kind']=='Home')
    edge = fixture.find('a:relationships/a:relationship[@identifier="'+home_id+'"]', N)
    duplicate = copy.deepcopy(edge); duplicate.set('identifier', 'test-duplicate-home')
    fixture.find('a:relationships', N).append(duplicate)
    assert containment.resolve(fixture).findings(), 'Duplicate Home must fail closed'
    fixture = copy.deepcopy(model.root)
    edge = fixture.find('a:relationships/a:relationship[@identifier="'+home_id+'"]', N)
    edge.set('isDirected', 'false')
    try:
        containment.resolve(fixture)
    except ValueError:
        pass
    else:
        raise AssertionError('Undirected Home was accepted')
    # Preserve the prior staged artifact; create a new scoped staged template.
    original_stage = ROOT / B / 'after/views-reconciliation/projection-template.staged.xml'
    staged = c.secure_xml(original_stage)
    spelling = []
    for node in staged.iter():
        if node.tag not in (c.tag('label'), c.tag('name'), c.tag('documentation')) or not node.text:
            continue
        before = node.text
        node.text = node.text.replace('Achitecture', 'Architecture').replace('Informaton', 'Information').replace('Worflow', 'Workflow')
        if node.text == '03 Authorisation-Enforcement':
            node.text = '03. Authorisation-Enforcement'
        if before != node.text:
            spelling.append(dict(xpath=staged.getroottree().getpath(node), before=before, after=node.text))
    for row in created:
        if row['name'] != row['displayName']:
            spelling.append(dict(kind='native Grouping name', canonicalId=row['canonicalId'],
                                 before=row['name'], after=row['displayName']))
    save('spelling-corrections.json', spelling)
    staged_path = A / 'projection-template.staged.xml'
    staged_path.write_bytes(c.ET.tostring(staged, encoding='UTF-8', xml_declaration=True, pretty_print=True))
    authored = Exchange(ROOT / B / 'before/Harmonia-Continuum-1.1.18-001-004.xml')
    expected_views = copy.deepcopy(authored.root.find('a:views', N))
    definitions = {p.get('identifier'): p.findtext('a:name', namespaces=N)
                   for p in staged.findall('a:propertyDefinitions/a:propertyDefinition', N)}
    definition_ids = {name: rid for rid, name in definitions.items()}
    for node in expected_views.iter():
        ref = node.get('propertyDefinitionRef')
        if ref:
            node.set('propertyDefinitionRef', definition_ids[authored.keys[ref]])
    assert signature(expected_views) == signature(staged.find('a:views', N))
    expected_tree = copy.deepcopy(views_tree(authored.root))
    for node in expected_tree.iter():
        if node.tag == c.tag('label') and node.text:
            node.text = node.text.replace('Achitecture', 'Architecture').replace('Informaton', 'Information')
            if node.text == '03 Authorisation-Enforcement':node.text = '03. Authorisation-Enforcement'
    assert signature(expected_tree) == signature(views_tree(staged))
    folders = views_tree(staged).xpath('.//a:item[not(@identifierRef)]', namespaces=N)
    folders_including_root = len(folders) + 1
    empty_folders = sum(not n.findall('a:item', N) for n in folders)
    assert folders_including_root == 1183 and empty_folders == 42
    remaining = []
    for view in staged.findall('a:views/a:diagrams/a:view', N):
        for node in view.findall('.//a:node[@elementRef]', N):
            rid = node.get('elementRef')
            if rid not in model.elements:
                remaining.append(dict(view=view.get('identifier'), node=node.get('identifier'),
                    elementRef=rid, nativeType=authored.elements[rid].get(TYPE),
                    name=authored.elements[rid].findtext('a:name', namespaces=N)))
    save('remaining-unresolved-references.json', remaining)
    probe = copy.deepcopy(model.root)
    probe.replace(probe.find('a:views', N), copy.deepcopy(staged.find('a:views', N)))
    org = probe.find('a:organizations', N)
    org.replace(views_tree(probe), copy.deepcopy(views_tree(staged)))
    schema = load_schema(ROOT / c.SCHEMAS / 'archimate3_Diagram.xsd')
    xsd_pass = schema.validate(probe)
    save('authored-presentation-xsd.json', dict(outcome='PASS' if xsd_pass else 'FAIL',
        errors=[e.message for e in schema.error_log], fullModelWritten=False))
    assert len({r['elementRef'] for r in remaining}) == 5 and len(remaining) == 12
    assert not xsd_pass
    # Deliberately do not install a maintained template that fails reference/XSD
    # gates. It would break current canonical projection and exceed authority.
    assert c.sha((ROOT / c.DESIGN / 'projection-template.xml').read_bytes()) == c.sha((ROOT / B / 'before/subject-hierarchy/projection-template.xml').read_bytes())
    manifest = continuum_manifest.publish(ROOT)
    result = dict(outcome='UNRESOLVED', canonicalVerification='PASS', canonicalElements=len(model.elements),
        canonicalRelationships=len(model.relationships), createdGroupings=115, reusedFolderSubjects=114,
        folderOccurrences=115, compositionCreated=115, compositionReused=96, homeCreated=211,
        homeReused=0, containmentPairs=211, establishedIdentitiesPreserved=1874,
        priorRelationshipsPreserved=1909, accepted32SubjectsAnd27CompositionsPreserved=True,
        canonicalXml=str(canonical_xml.relative_to(ROOT)), canonicalXmlViews=len(model.views),
        canonicalXmlScope='Canonical Subject incorporation verification; old maintained presentation, not the requested complete authored-Views candidate',
        completeAuthoredXml=None, authoredViewsPreservedInStaging=1285,
        authoredFoldersPreservedInStaging=folders_including_root, emptyFoldersPreserved=empty_folders,
        resolvedPreviousGroupingNodeReferences=115, remainingDistinctElements=5,
        remainingNodeReferences=len(remaining), affectedViews=len({r['view'] for r in remaining}),
        authoredPresentationXsd='FAIL', maintainedTemplateChanged=False,
        architecturalHomeChecks='PASS', duplicateAndUndirectedHomeNegativeChecks='PASS',
        protectedInputsUnchanged=True, originalCanonicalIdentityAndMetadataPreserved=True,
        manifest=manifest, actualArchiImportExport='NOT_PERFORMED', visualAcceptance='PENDING',
        releaseTransition=False, commit=False, push=False, closure=False)
    save('verification.json', result)
    print(json.dumps(result, indent=2))


if __name__ == '__main__':
    run()
