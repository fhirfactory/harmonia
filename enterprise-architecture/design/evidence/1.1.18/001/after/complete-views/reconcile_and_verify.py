#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Apply approved presentation after an isolated normal-projector preflight."""
import copy
import json
from pathlib import Path
import sys
import tempfile
import uuid

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_manifest
import containment
from exchange_compare import Exchange
from exchange_xml import N, TYPE, signature
from home_relationships import authored_homes

B = Path('enterprise-architecture/design/evidence/1.1.18/001')
A = ROOT / B / 'after/complete-views'


def save(name, data):
    (A / name).write_bytes(c.encoded(data))


def view_tree(root):
    matches = root.xpath('./a:organizations/a:item[a:label="Views"]', namespaces=N)
    assert len(matches) == 1
    return matches[0]


def disposition_folders(root):
    source = view_tree(root)
    subject = source.xpath('./a:item[a:label="1. Subject-Based-Views"]', namespaces=N)[0]
    changes = []
    for group, old, new in (
        ('07. Resilience-and-Recovery', '04. Continuity-of-Operations', '04. Disaster-Recovery'),
        ('11. Verification-and-Testing', '04. Performance-and-Capability-Testing', '04. Performance-and-Capacity-Testing')):
        matches = subject.xpath('./a:item[a:label=$group]/a:item/a:label[text()=$old]', namespaces=N, group=group, old=old)
        assert len(matches) == 1
        matches[0].text = new
        changes.append(dict(group=group, before=old, after=new, authority='Explicit Solution Architect folder disposition'))
    assert len(subject.xpath('./a:item[a:label="03. Security-and-Management"]', namespaces=N)) == 1
    return changes


def execute():
    pins = c.read_json(ROOT / B / 'before/complete-views/input-pins.json')
    results = c.read_json(A / 'five-element-results.json')
    assert len(results) == 5
    assert all(uuid.UUID(r['canonicalId']).version == 4 for r in results)
    stage_source = ROOT / B / 'after/subject-hierarchy/projection-template.staged.xml'
    staged = c.secure_xml(stage_source)
    # Preserve the authored numeric prefix. Earlier staging normalised its
    # missing period, which would make two distinct retained folder paths equal.
    # This is presentation punctuation, not an architectural spelling correction.
    prefix = view_tree(staged).xpath('./a:item[a:label="1. Subject-Based-Views"]/a:item[a:label="03. Security-and-Management"]/a:item[a:label="03. Authorisation-Enforcement" and not(a:item)]/a:label', namespaces=N)
    assert len(prefix) == 1
    prefix[0].text = '03 Authorisation-Enforcement'
    folder_dispositions = disposition_folders(staged)
    staged_path = A / 'projection-template.reconciled.xml'
    staged_path.write_bytes(c.ET.tostring(staged, encoding='UTF-8', xml_declaration=True, pretty_print=True))
    # Scope: the template owns presentation/model metadata, not semantic records.
    before_template = c.secure_xml(ROOT / B / 'before/complete-views/projection-template.xml')
    assert not staged.findall('a:elements/a:element', N) and not staged.findall('a:relationships/a:relationship', N)
    for section in ('properties', 'propertyDefinitions', 'name', 'documentation'):
        left, right = before_template.find('a:'+section, N), staged.find('a:'+section, N)
        assert (left is None) == (right is None)
        if left is not None:assert signature(left) == signature(right)
    # Full normal generation gates run before changing the maintained template.
    with tempfile.TemporaryDirectory(prefix='harmonia-complete-views-') as tmp:
        isolated = Path(tmp)
        (isolated / c.DESIGN).mkdir(parents=True)
        for relative in ('architecture-release.json', 'enterprise-architecture/continuum',
                         'enterprise-architecture/governance', 'enterprise-architecture/tooling',
                         'enterprise-architecture/design/evidence', 'enterprise-architecture/design/review',
                         'enterprise-architecture/design/register'):
            path = isolated / relative
            path.parent.mkdir(parents=True, exist_ok=True)
            path.symlink_to(ROOT / relative, target_is_directory=(ROOT / relative).is_dir())
        (isolated / c.DESIGN / 'projection-template.xml').write_bytes(staged_path.read_bytes())
        preview, preview_validation = c.project(isolated)
    save('presentation-preflight.json', preview_validation)
    maintained = ROOT / c.DESIGN / 'projection-template.xml'
    permitted = [(ROOT / B / 'before/complete-views/projection-template.xml').read_bytes()]
    initial = A / 'initial-checks/projection-template.reconciled.xml.gz'
    if initial.exists():
        import gzip
        permitted.append(gzip.decompress(initial.read_bytes()))
    assert maintained.read_bytes() in permitted
    maintained.write_bytes(staged_path.read_bytes())
    output = A / 'Harmonia-Subjects-Views-reconciled-unreleased.xml'
    validation = c.write_projection(ROOT, output)
    assert output.read_bytes() == preview
    save('generation-validation.json', validation)
    model = Exchange(output)
    assert not model.issues
    authored = Exchange(ROOT / B / 'before/Harmonia-Continuum-1.1.18-001-004.xml')
    assert not authored.issues
    _, elements = c.load(ROOT)
    baseline = Exchange(ROOT / B / 'after/subject-hierarchy/Harmonia-Subject-Hierarchy-canonical-unreleased.xml')
    assert len(model.elements) == 1994 and len(model.relationships) == 2235
    assert model.elements.keys() - baseline.elements.keys() == {r['exchangeId'] for r in results}
    assert model.relationships.keys() == baseline.relationships.keys()
    assert all(signature(node)==signature(model.relationships[rid]) for rid,node in baseline.relationships.items())
    assert all(model.single(model.elements[rid],'canonicalId')==baseline.single(node,'canonicalId') for rid,node in baseline.elements.items())
    old_id = 'c20aa17ce4aa4a58be5eb3cff2ecdd1d'
    old_exchange = elements[old_id][0]['exchange']['identifier']
    changed_old_elements = [rid for rid,node in baseline.elements.items() if signature(node)!=signature(model.elements[rid])]
    assert changed_old_elements == [old_exchange]
    for row in results:
        node = model.elements[row['exchangeId']]
        assert node.get(TYPE) == row['nativeType']
        assert model.single(node,'canonicalId') == row['canonicalId']
        assert node.findtext('a:name',namespaces=N) == row['displayName']
        assert model.single(node,'Specialization') == row['specialization']
    # All previous canonical bytes remain exact except the authorised retained
    # historical combined Stakeholder evolution.
    modified_sources = []
    for relative, digest in pins['canonical'].items():
        if c.sha((ROOT/relative).read_bytes()) != digest:modified_sources.append(relative)
    expected_old = 'enterprise-architecture/continuum/motivation/stakeholder/'+old_id+'.md'
    assert modified_sources == [expected_old]
    assert all(c.sha((ROOT/p).read_bytes())==digest for p,digest in pins['protected'].items())
    # Preserve the existing Grouping and Home pairs exactly, not only their counts.
    pairs = c.read_json(ROOT / B / 'after/subject-hierarchy/relationship-results.json')
    homes = {r['identifier']: r for r in authored_homes(model)}
    assert len(pairs)==422 and len({r['childCanonicalId'] for r in pairs})==211
    for edge in pairs:
        node = model.relationships[edge['identifier']]
        assert node.get('source')==edge['source'] and node.get('target')==edge['target']
        if edge['kind']=='Home':assert edge['identifier'] in homes
    assert not containment.resolve(model.root).findings()
    expected_views = copy.deepcopy(authored.root.find('a:views',N))
    definition_mappings = {}
    rebound_count = 0
    for node in expected_views.iter():
        ref=node.get('propertyDefinitionRef')
        if ref:
            dest=model.definition_ids[authored.keys[ref]]
            definition_mappings[ref]=dest
            rebound_count+=int(ref!=dest)
            node.set('propertyDefinitionRef',dest)
    assert rebound_count==68
    assert signature(expected_views)==signature(model.root.find('a:views',N))
    assert model.views.keys()==authored.views.keys() and len(model.views)==1285
    expected_tree=copy.deepcopy(view_tree(authored.root))
    spellings=[]
    for node in expected_tree.iter():
        if node.tag==c.tag('label') and node.text:
            old=node.text
            node.text=node.text.replace('Achitecture','Architecture').replace('Informaton','Information')
            if node.text!=old:spellings.append(dict(before=old,after=node.text))
    # Apply the two explicit folder changes to an independent complete-model
    # wrapper so the same identity/path constraints are checked.
    wrapper=copy.deepcopy(authored.root)
    org=wrapper.find('a:organizations',N)
    org.replace(view_tree(wrapper),expected_tree)
    disposition_folders(wrapper)
    assert signature(view_tree(wrapper))==signature(view_tree(model.root))
    folders=view_tree(model.root).xpath('.//a:item[not(@identifierRef)]',namespaces=N)
    assert len(folders)+1==1183
    assert sum(not node.findall('a:item',N) for node in folders)==42
    previous_missing=c.read_json(ROOT / B / 'after/views-reconciliation/unresolved-references.json')
    assert len(previous_missing)==127
    assert all(row['reference'] in model.elements for row in previous_missing)
    # Typed reference/visual endpoint checks are also performed by Exchange.
    refs={attr:sum(bool(n.get(attr)) for n in model.root.find('a:views',N).iter())
          for attr in ('elementRef','relationshipRef','propertyDefinitionRef')}
    manifest=continuum_manifest.publish(ROOT)
    save('presentation-transformations.json',dict(propertyDefinitionMappings=definition_mappings,
        reboundPropertyOccurrences=rebound_count, spelling=spellings, folderDispositions=folder_dispositions,
        G03='Authored Security-and-Management retained; canonical Subject unchanged',
        numericPrefix='Original authored 03 Authorisation-Enforcement retained; optional punctuation normalisation withdrawn to preserve distinct folder paths',
        architecturalReferenceRebindings=0))
    summary=dict(outcome='PASS',standing='Unreleased verification candidate; Solution Architect visual acceptance pending',
        output=str(output.relative_to(ROOT)),sha256=c.sha(output.read_bytes()),elements=1994,relationships=2235,
        views=1285,viewFoldersIncludingRoot=1183,emptyFolders=42,
        originalCanonicalIdentitiesPreserved=1989,originalRelationshipsPreserved=2235,
        approvedElementsRepresented=5,stakeholderEvolution='Two new distinct successors; combined UUID/Exchange ID/definition/references retained as superseded history',
        priorGroupingIdentitiesAnd211PairsPreserved=True,previousMissingNodeReferencesResolved=127,
        remainingMissingReferences=0,viewGeometryMembershipConnectionsStyles='Exact parsed View subtree after documented Property definition rebindings',
        viewOrganisation='Exact authored tree after approved folder dispositions and spelling corrections',
        viewReferences=refs,offlineXsd='PASS',typedExchangeReferences='PASS',canonicalSchema='PASS',
        containment='PASS',protectedInputsUnchanged=len(pins['protected']),manifest=manifest,
        actualArchiImportExport='NOT_PERFORMED',visualAcceptance='PENDING',releaseTransition=False,
        protectedPublicationChanged=False,closure=False,commit=False,push=False)
    save('verification.json',summary)
    print(json.dumps(summary,indent=2))


if __name__=='__main__':
    execute()
