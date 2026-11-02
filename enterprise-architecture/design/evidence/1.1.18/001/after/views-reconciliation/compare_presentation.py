#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded presentation evidence; never writes canonical or maintained sources.

Retains the complete authored presentation in a staged template, inventories its
references against established identities, and exercises the maintained projector
in isolation. This is not a semantic incorporation adapter or an import repair.
"""
import copy
from collections import Counter
import hashlib
import json
from pathlib import Path
import sys
import tempfile

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
from element_correlation import correlate
from exchange_compare import Exchange
from exchange_xml import N, TYPE, load_schema, signature

BOUNDARY = ROOT / 'enterprise-architecture/design/evidence/1.1.18/001'
BEFORE = BOUNDARY / 'before/views-reconciliation'
AFTER = BOUNDARY / 'after/views-reconciliation'


def write(name, data):
    (AFTER / name).write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n')


def digest(node):
    return hashlib.sha256(json.dumps(signature(node), ensure_ascii=False).encode()).hexdigest()


def view_tree(model):
    roots = model.root.xpath('./a:organizations/a:item[a:label="Views"]', namespaces=N)
    assert len(roots) == 1
    return roots[0]


def folders(tree):
    """Ordered structural inventory; sibling positions distinguish equal names."""
    rows = []

    def walk(node, path, positions):
        label = node.findtext('a:label', namespaces=N)
        current = path + [label]
        if not node.get('identifierRef'):
            children = node.findall('a:item', N)
            rows.append(dict(path=current, positions=positions, empty=not children,
                             attributes=dict(node.attrib), digest=digest(node)))
        for i, child in enumerate(node.findall('a:item', N)):
            walk(child, current, positions + [i])
    walk(tree, [], [])
    return rows


def paths_by_view(tree):
    result = {}
    for node in tree.iter(c.tag('item')):
        rid = node.get('identifierRef')
        if rid:
            ancestors = list(reversed(list(node.iterancestors(c.tag('item')))))
            result.setdefault(rid, []).append([p.findtext('a:label', namespaces=N) for p in ancestors])
    return result


def run():
    pins = json.loads((BEFORE / 'input-pins.json').read_text())
    # Frozen preimage, not the live mutable template, supplies the comparison.
    models = {name: Exchange(ROOT / pins[name]['path'])
              for name in ('incoming', 'published', 'subjects')}
    models['template'] = Exchange(BEFORE / 'projection-template.xml')
    incoming, subjects, template = (models[k] for k in ('incoming', 'subjects', 'template'))
    for name in ('incoming', 'published', 'subjects'):
        assert not models[name].issues, (name, models[name].issues)
    correlation = correlate(subjects, incoming)
    assert not correlation['issues'] and not any(correlation['unresolved'].values())
    mappings = {m['incoming_id']: m for m in correlation['matches']}
    rebound = {old: m['baseline_id'] for old, m in mappings.items()}
    property_map = {}
    for ref in {n.get('propertyDefinitionRef') for n in incoming.root.find('a:views', N).iter() if n.get('propertyDefinitionRef')}:
        key = incoming.keys[ref]
        dest = template.definition_ids.get(key)
        assert dest is not None, ('Unresolved presentation Property definition', ref, key)
        left, right = copy.deepcopy(incoming.definitions[ref]), copy.deepcopy(template.definitions[dest])
        left.attrib.pop('identifier'); right.attrib.pop('identifier')
        assert signature(left) == signature(right), ('Conflicting Property definition', key)
        property_map[ref] = dest
    unresolved = []
    references = Counter()
    relationship_mismatches = []
    for vid, view in incoming.views.items():
        for node in view.iter():
            for attr, table in [('elementRef', subjects.elements),
                                ('relationshipRef', subjects.relationships),
                                ('viewRef', incoming.views),
                                ('propertyDefinitionRef', subjects.definitions)]:
                ref = node.get(attr)
                if not ref:
                    continue
                references[attr] += 1
                mapped = (rebound.get(ref, ref) if attr == 'elementRef' else
                          property_map.get(ref, ref) if attr == 'propertyDefinitionRef' else ref)
                if mapped not in table:
                    source = incoming.elements.get(ref) if attr == 'elementRef' else None
                    unresolved.append(dict(view=vid, viewName=view.findtext('a:name', namespaces=N),
                        visualObject=node.get('identifier'), attribute=attr, reference=ref,
                        sourceXPath=incoming.root.getroottree().getpath(node), sourceLine=node.sourceline,
                        nativeType=source.get(TYPE) if source is not None else None,
                        name=source.findtext('a:name', namespaces=N) if source is not None else None,
                        canonicalId=incoming.single(source, 'canonicalId') if source is not None else None,
                        disposition='UNRESOLVED: no established canonical mapping; no allocation/removal'))
        nodes = {n.get('identifier'): n for n in view.findall('.//a:node', N)}
        for conn in view.findall('.//a:connection[@relationshipRef]', N):
            rel = subjects.relationships.get(conn.get('relationshipRef'))
            ends = [nodes.get(conn.get(k)) for k in ('source', 'target')]
            if rel is not None and all(n is not None and n.get('elementRef') for n in ends):
                refs = [rebound.get(n.get('elementRef'), n.get('elementRef')) for n in ends]
                if refs != [rel.get('source'), rel.get('target')]:
                    relationship_mismatches.append(dict(view=vid, connection=conn.get('identifier'),
                        relationship=rel.get('identifier'), visualEndpoints=refs,
                        canonicalEndpoints=[rel.get('source'), rel.get('target')]))
    write('unresolved-references.json', unresolved)
    write('relationship-endpoint-check.json', dict(mismatches=relationship_mismatches))
    # Complete incoming Views + only the Views organisation branch. Non-View
    # folders, semantic records, model metadata and property definitions are NOT
    # copied from the submission.
    staged = copy.deepcopy(template.root)
    staged.replace(staged.find('a:views', N), copy.deepcopy(incoming.root.find('a:views', N)))
    org = staged.find('a:organizations', N)
    org.replace(org.xpath('./a:item[a:label="Views"]', namespaces=N)[0], copy.deepcopy(view_tree(incoming)))
    # Accepted CR-060 is the sole authorised textual presentation transformation.
    typo = staged.xpath('./a:organizations/a:item[a:label="Views"]/a:item[a:label="1. Subject-Based-Views"]/a:item[a:label="18. Provider Directory"]/a:item/a:label[text()="03. Directory-Informaton-Maintenance"]', namespaces=N)
    assert len(typo) == 1
    typo[0].text = '03. Directory-Information-Maintenance'
    transformations = [dict(kind='ACCEPTED_CR_060', before='03. Directory-Informaton-Maintenance',
                            after=typo[0].text, semanticEffect='none')]
    for node in staged.find('a:views', N).iter():
        ref = node.get('elementRef')
        if ref in rebound and rebound[ref] != ref:
            transformations.append(dict(kind='ESTABLISHED_IDENTITY_REBINDING', before=ref, after=rebound[ref]))
            node.set('elementRef', rebound[ref])
        prop = node.get('propertyDefinitionRef')
        if prop in property_map and property_map[prop] != prop:
            transformations.append(dict(kind='PROPERTY_DEFINITION_REBINDING', before=prop,
                after=property_map[prop], key=incoming.keys[prop], visualObject=node.getparent().getparent().get('identifier')))
            node.set('propertyDefinitionRef', property_map[prop])
    assert not staged.findall('a:elements/a:element', N)
    assert not staged.findall('a:relationships/a:relationship', N)
    assert signature(staged.find('a:propertyDefinitions', N)) == signature(template.root.find('a:propertyDefinitions', N))
    staged_path = AFTER / 'projection-template.staged.xml'
    staged_path.write_bytes(c.ET.tostring(staged, encoding='UTF-8', xml_declaration=True, pretty_print=True))
    staged_model = Exchange(staged_path)
    expected_tree = copy.deepcopy(view_tree(incoming))
    expected_tree.xpath('.//a:label[text()="03. Directory-Informaton-Maintenance"]', namespaces=N)[0].text = '03. Directory-Information-Maintenance'
    assert signature(view_tree(staged_model)) == signature(expected_tree)
    expected_views = copy.deepcopy(incoming.root.find('a:views', N))
    for node in expected_views.iter():
        if node.get('propertyDefinitionRef'):
            node.set('propertyDefinitionRef', property_map[node.get('propertyDefinitionRef')])
    assert signature(staged_model.root.find('a:views', N)) == signature(expected_views)
    schema = load_schema(ROOT / c.SCHEMAS / 'archimate3_Diagram.xsd')
    # A maintained template deliberately has empty mandatory semantic collections
    # and is not a complete Exchange instance. Validate a complete in-memory
    # structural probe with canonical collections, without writing an invalid
    # reference-bearing model as a verification XML.
    structural_probe = copy.deepcopy(subjects.root)
    structural_probe.replace(structural_probe.find('a:views', N), copy.deepcopy(staged_model.root.find('a:views', N)))
    probe_org = structural_probe.find('a:organizations', N)
    probe_org.replace(probe_org.xpath('./a:item[a:label="Views"]', namespaces=N)[0],
                      copy.deepcopy(view_tree(staged_model)))
    structural_xsd = schema.validate(structural_probe)
    structural_xsd_errors = [error.message for error in schema.error_log]
    write('structural-probe-xsd.json', dict(outcome='PASS' if structural_xsd else 'FAIL',
        errors=structural_xsd_errors, scope='complete in-memory probe; no full candidate written'))
    write('intentional-transformations.json', transformations)
    trees = {name: folders(view_tree(model)) for name, model in models.items()}
    trees['staged'] = folders(view_tree(staged_model))
    write('folder-inventories.json', trees)
    paths = {name: paths_by_view(view_tree(model)) for name, model in models.items()}
    affected = {r['view'] for r in unresolved}
    view_comparison = []
    changed = []
    for vid, view in incoming.views.items():
        existing = vid in subjects.views
        raw_equal = existing and signature(view) == signature(subjects.views[vid])
        if existing and not raw_equal:
            before = copy.deepcopy(subjects.views[vid])
            after = copy.deepcopy(view)
            # Classify comparisons by established definition keys; representation
            # IDs alone cannot preserve the meaning of View properties.
            lang = '{http://www.w3.org/XML/1998/namespace}lang'
            for v in (before, after):
                model = subjects if v is before else incoming
                for prop in v.findall('.//a:property', N):
                    prop.set('propertyDefinitionRef', model.keys[prop.get('propertyDefinitionRef')])
                for value in v.findall('.//a:properties/a:property/a:value', N):
                    if value.get(lang) == 'en':
                        value.attrib.pop(lang)
            changed.append(dict(id=vid, name=view.findtext('a:name', namespaces=N),
                onlyPropertyDefinitionOrEnglishLanguage=signature(before) == signature(after)))
        expected_view = next(v for v in expected_views.find('a:diagrams', N) if v.get('identifier') == vid)
        view_comparison.append(dict(id=vid, name=view.findtext('a:name', namespaces=N),
            population='existing' if existing else 'added',
            stagedExactAfterDocumentedRebindings=digest(expected_view) == digest(staged_model.views[vid]),
            stagedRawExact=digest(view) == digest(staged_model.views[vid]),
            differsFromSubjects=existing and not raw_equal,
            moved=existing and paths['incoming'].get(vid) != paths['subjects'].get(vid),
            unresolvedReferences=vid in affected))
    write('view-comparison.json', view_comparison)
    write('changed-existing-views.json', changed)
    # Prove the accepted semantic baseline is reproducible before testing the
    # presentation candidate. project() is read-only; no publication flags used.
    content, base_validation = c.project(ROOT)
    assert content == (ROOT / pins['subjects']['path']).read_bytes()
    write('canonical-projection-validation.json', base_validation)
    # Isolated root shares read-only sources; only its template is different.
    # The normal generation gate must reject unresolved references, not repair
    # them or emit an import-ready claim. No full invalid model is written.
    with tempfile.TemporaryDirectory(prefix='harmonia-view-preflight-') as tmp:
        isolated = Path(tmp)
        (isolated / 'enterprise-architecture/design/archimate').mkdir(parents=True)
        for rel in ('architecture-release.json', 'enterprise-architecture/continuum',
                    'enterprise-architecture/governance', 'enterprise-architecture/tooling',
                    'enterprise-architecture/design/evidence', 'enterprise-architecture/design/review',
                    'enterprise-architecture/design/register'):
            dest = isolated / rel
            dest.parent.mkdir(parents=True, exist_ok=True)
            dest.symlink_to(ROOT / rel, target_is_directory=(ROOT / rel).is_dir())
        (isolated / c.DESIGN / 'projection-template.xml').write_bytes(staged_path.read_bytes())
        try:
            candidate, validation = c.project(isolated)
        except (ValueError, c.ET.DocumentInvalid) as exc:
            preflight = dict(outcome='FAIL', meaning='expected reference gate rejection; task outcome UNRESOLVED',
                             message=str(exc), outputWritten=False)
        else:
            preflight = dict(outcome='PASS', outputSha256=c.sha(candidate), validation=validation, outputWritten=False)
    write('staged-generation-preflight.json', preflight)
    assert unresolved and preflight['outcome'] == 'FAIL'  # observed bounded failure remains explicit
    summary = dict(outcome='UNRESOLVED', maintainedTemplateChanged=False, verificationXmlGenerated=False,
        blocker='120 unincorporated native Groupings have no established canonical mappings',
        baselines={name:dict(elements=len(model.elements),relationships=len(model.relationships),views=len(model.views),
            viewFoldersIncludingRoot=len(trees[name]), emptyViewFolders=sum(f['empty'] for f in trees[name])) for name,model in models.items()},
        authoredViews=len(incoming.views), addedViews=len(incoming.views.keys()-subjects.views.keys()),
        removedViews=len(subjects.views.keys()-incoming.views.keys()), rawChangedExistingViews=len(changed),
        propertyDefinitionOrLanguageOnlyExistingViewChanges=sum(r['onlyPropertyDefinitionOrEnglishLanguage'] for r in changed),
        movedExistingViews=sum(r['moved'] for r in view_comparison),
        stagedExactViewsAfterDocumentedRebindings=sum(r['stagedExactAfterDocumentedRebindings'] for r in view_comparison),
        stagedRawExactViews=sum(r['stagedRawExact'] for r in view_comparison),
        stagedViewsSubtreeExactAfterDocumentedPropertyRebindings=True, stagedExactViewOrganisationExceptCR060=True,
        propertyDefinitionMappings=property_map, reboundPropertyOccurrences=len(transformations)-1,
        stagedFolderCount=len(trees['staged']), stagedEmptyFolders=sum(f['empty'] for f in trees['staged']),
        unresolvedElements=len({r['reference'] for r in unresolved if r['attribute']=='elementRef'}),
        unresolvedNodeOccurrences=len(unresolved), affectedViews=len(affected),
        canonicalCorrelation=dict(status=correlation['status'], matches=len(mappings), rebindings=len(correlation['representation_rebindings']),
            unrelatedIncomingElementsExcluded=len(correlation['new']), acceptedSubjectsAbsentFromIncoming=len(correlation['deleted'])),
        viewReferences=dict(references), visualSemanticEndpointMismatches=len(relationship_mismatches),
        inMemoryCompleteStructuralProbeXsd='PASS' if structural_xsd else 'FAIL',
        acceptedProjectionExactReproduction='PASS', acceptedSubjects=32, acceptedCompositions=27,
        preflight=preflight, actualArchiTransport='NOT_PERFORMED', solutionArchitectVisualAcceptance='PENDING')
    write('summary.json', summary)
    print(json.dumps(summary, indent=2))


if __name__ == '__main__':
    run()
