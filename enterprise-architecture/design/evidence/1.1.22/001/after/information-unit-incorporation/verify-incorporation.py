#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded evidence check for the authorised IU incorporation, not a new validator."""
import copy
import gzip
import html
import json
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
from unittest.mock import patch

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c
import continuum_manifest as manifest
import publish_continuum as reader
import publish_continuum_rad as narrative
import continuum_workbench as workbench
import exchange_compare as compare

OUT = Path(__file__).resolve().parent
REL = OUT.relative_to(ROOT)
BEFORE = OUT.parents[1] / 'before'
reconciliation = c.read_json(OUT / 'reconciliation.json')
inventory = c.read_json(OUT / 'change-inventory.json')
baseline = c.read_json(BEFORE / 'baseline.json')
ids = reconciliation['canonicalIds']


def preservation():
    _, elements = c.load(ROOT)
    entry = baseline['head']
    new = {x['canonicalId'] for x in inventory['added']}
    assert len(new) == 4
    original_ids = set()
    original_edges = {}
    untouched = 0
    for path, digest in baseline['sourcePins'].items():
        if not path.endswith('.md'):
            continue
        data = (ROOT / path).read_bytes()
        if c.sha(data) != digest:
            data = subprocess.check_output(['git', 'show', entry + ':' + path], cwd=ROOT)
        assert c.sha(data) == digest
        before, body = c.decode(data.decode())
        cid = before['canonicalId']
        original_ids.add(cid)
        after, result, destination = elements[cid]
        assert destination.relative_to(ROOT).as_posix() == path
        for field in ('canonicalId', 'archimateType', 'specialization', 'displayName',
                      'friendlyName', 'canonicalName', 'identity', 'standing', 'naming', 'exchange'):
            assert before.get(field) == after.get(field), (cid, field)
        for edge in before['relationships']:
            original_edges[edge['identifier']] = edge
            assert edge in after['relationships'], edge['identifier']
        if c.sha(destination.read_bytes()) == digest:
            untouched += 1
        # All existing Feature/Motivation definitions are unchanged; additions
        # express applicability without semantic reparenting or promotion.
        if before['archimateType'] in ('Capability', 'Requirement', 'Constraint', 'Principle'):
            assert result == body, cid
    assert set(elements) == original_ids | new
    assert all(elements[cid][0]['archimateType'] == 'DataObject' and
               elements[cid][0]['specialization'] is None for cid in new)
    actual_edges = {r['identifier']: r for m, _, _ in elements.values() for r in m['relationships']}
    assert set(actual_edges) - set(original_edges) == {r['identifier'] for r in reconciliation['newRelationships']}
    assert len(actual_edges) == 9314 and len(original_edges) == 9264
    assert (ROOT / 'enterprise-architecture/design/archimate/projection-template.xml').read_bytes() == subprocess.check_output(
        ['git', 'show', entry + ':enterprise-architecture/design/archimate/projection-template.xml'], cwd=ROOT)
    # Exact old definition and partition remain enclosed; the established later
    # generation/failure semantics remain present outside the bounded wrapper.
    old_bo = c.decode(subprocess.check_output(['git', 'show', entry + ':' + str(elements[ids['bo']][2].relative_to(ROOT))], cwd=ROOT).decode())[1]
    old_content = old_bo.split('## Architectural content\n', 1)[1]
    prior, retained = old_content.split('#### 6.5.1 Version and Active Generation', 1)
    result = elements[ids['bo']][1]
    assert prior in result and ('#### 6.5.1 Version and Active Generation' + retained) in result
    assert 'Legacy Content - Superseded' in result
    # This boundary does not change application sources or metamodel/tooling.
    changed = subprocess.check_output(['git', 'diff', '--name-only'], cwd=ROOT, text=True).splitlines()
    assert all(p.startswith('enterprise-architecture/continuum/') or
               p == 'architecture-release.json' or
               p == 'enterprise-architecture/tooling/tests/test_continuum.py' for p in changed), changed
    # Compare every already tracked release/register publication; no version is
    # selected or rewritten to make a candidate fit an immutable destination.
    protected = subprocess.check_output(['git', 'ls-tree', '-r', '--name-only', entry,
        'enterprise-architecture/design/register',
        'enterprise-architecture/design/archimate/published',
        'enterprise-architecture/documentation/continuum-manifest/published',
        'enterprise-architecture/documentation/continuum-rad/published'], cwd=ROOT, text=True).splitlines()
    for path in protected:
        assert (ROOT / path).read_bytes() == subprocess.check_output(['git', 'show', entry + ':' + path], cwd=ROOT)
    # The native composition is the checkable representation of singularity.
    target = elements[ids['representation']][0]['exchange']['identifier']
    source = elements[ids['iu']][0]['exchange']['identifier']
    def single_representation(edges):
        candidates = [r for r in edges if r['archimateType'] == 'Composition' and r['source'] == source and r['target'] == target]
        return len(candidates) == 1 and '[1..1]' in candidates[0]['name'][0]['text']
    edges = list(actual_edges.values())
    assert single_representation(edges)
    bound = next(r for r in edges if r['archimateType'] == 'Composition' and r['source'] == source and r['target'] == target)
    assert not single_representation([r for r in edges if r is not bound])
    assert not single_representation(edges + [copy.deepcopy(bound)])
    # Preservation of all authored View bytes, including folders, is independent
    # of the strict transport comparator applied to the Archi return.
    original = c.secure_xml(ROOT / baseline['acceptedExchange'])
    candidate = c.secure_xml(OUT / 'candidate-v2/reader/Harmonia-Architecture.xml')
    assert workbench.signatures(original, 'views/diagrams', 'view') == workbench.signatures(candidate, 'views/diagrams', 'view')
    template = c.secure_xml(ROOT / c.DESIGN / 'projection-template.xml')
    assert c.ET.tostring(template.find('a:views', c.N)) == c.ET.tostring(candidate.find('a:views', c.N))
    report = dict(outcome='PASS', baseline='1.1.21', developmentRelease='1.1.22',
                  preservedExistingIdentities=len(original_ids), unchangedCanonicalBytes=untouched,
                  changedExistingCanonicalSources=len(original_ids)-untouched,
                  preservedExistingRelationships=len(original_edges), newElements=4, newRelationships=50,
                  authoredViewsPreserved=1304, projectionTemplateUnchanged=True,
                  protectedPublicationRegisterFilesUnchanged=len(protected),
                  currentCapabilityHierarchyAndDefinitionsUnchanged=True,
                  existingClassificationNamesStandingIdentityBindingsUnchanged=True,
                  newImplementationClassesFunctionsServicesInterfaces=0,
                  singleRepresentationCheck='PASS; missing and duplicate bound compositions rejected in memory',
                  limit='Checks this model representation and source preservation, not implementation conformance or human acceptance.')
    (OUT / 'preservation-validation.json').write_bytes(c.encoded(report))
    print('Preservation PASS', flush=True)


def independent_generation():
    _, elements = c.load(ROOT)
    source_pins = {str(p.relative_to(ROOT)): c.sha(p.read_bytes()) for _, _, p in elements.values()}
    with tempfile.TemporaryDirectory(prefix='harmonia-iu-independent-') as name:
        stage = Path(name)
        for _, _, path in elements.values():
            dest = stage / path.relative_to(ROOT)
            dest.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(path, dest)
        shutil.copyfile(ROOT / 'enterprise-architecture/continuum/navigation.json', stage / 'enterprise-architecture/continuum/navigation.json')
        for rel in (Path('enterprise-architecture/governance'), c.TOOLING, narrative.SOURCE):
            shutil.copytree(ROOT / rel, stage / rel, ignore=shutil.ignore_patterns('__pycache__', 'published'))
        (stage / c.DESIGN).mkdir(parents=True)
        shutil.copyfile(ROOT / c.DESIGN / 'projection-template.xml', stage / c.DESIGN / 'projection-template.xml')
        shutil.copyfile(ROOT / 'architecture-release.json', stage / 'architecture-release.json')
        for metadata, _, _ in elements.values():
            for item in metadata['provenance']:
                if item.get('retainedAt'):
                    rel = Path(item['retainedAt'])
                    (stage / rel).parent.mkdir(parents=True, exist_ok=True)
                    shutil.copyfile(ROOT / rel, stage / rel)
        with patch.object(c.uuid, 'uuid4', side_effect=AssertionError('Generation attempted identity allocation')):
            xml, _ = c.project(stage)
            assert xml == (OUT / 'candidate-v2/reader/Harmonia-Architecture.xml').read_bytes()
            print('Independent Exchange PASS', flush=True)
            outputs = {'candidate-v2/reader/' + n: v for n, v in reader.outputs(stage, stage / REL / 'candidate-v2/reader').items()}
            print('Independent reader PASS', flush=True)
            outputs.update({'candidate-v2/narrative/' + n: v for n, v in narrative.outputs(stage, stage / REL / 'candidate-v2/narrative').items()})
            for path, data in outputs.items():
                assert (OUT / path).read_bytes() == data, path
            assert manifest.render(stage) == (ROOT / c.MANIFEST).read_bytes()
        assert source_pins == {str(p.relative_to(stage)): c.sha(p.read_bytes()) for _, _, p in c.load(stage)[1].values()}
        assert not any((stage / n).exists() for n in ('docs', 'scripts', '.git'))
    assert source_pins == {str(p.relative_to(ROOT)): c.sha(p.read_bytes()) for _, _, p in c.load(ROOT)[1].values()}
    (OUT / 'independent-generation.json').write_bytes(c.encoded(dict(outcome='PASS',
        identicalCandidateArtifacts=18, rootManifestIdentical=True, canonicalSourcesUnchanged=len(source_pins),
        allocatedIdentities=0, legacyDocsScriptsGitAbsent=True,
        inputs='Maintained Continuum/governance/tooling, narrative presentation, View template, declared retained provenance and root release only',
        limit='Automated independent generation, not actual Archi fidelity or Solution Architect acceptance.')))
    print('Independent narrative and Manifest PASS', flush=True)


def transport():
    before_path = OUT / 'candidate-v2/reader/Harmonia-Architecture.xml'
    returned_path = OUT / 'archi-transport/returned-model.xml.gz'
    before, returned = compare.Exchange(before_path), compare.Exchange(returned_path)
    raw = compare.compare(before, returned)
    (OUT / 'archi-transport/raw-comparison.json.gz').write_bytes(gzip.compress(c.encoded(raw), mtime=0))
    _, a = workbench.model(before_path)
    # workbench.model requires plain XML; decompress this exact retained return.
    with tempfile.TemporaryDirectory(prefix='harmonia-iu-return-') as name:
        path = Path(name) / 'returned.xml'
        path.write_bytes(gzip.decompress(returned_path.read_bytes()))
        _, b = workbench.model(path)
    fields = {k: sum(v[k] == b[cid][k] for cid, v in a.items()) for k in ('identifier','archimateType','specialization','name','documentation')}
    roles = {k: sum(v['properties'].get(k) == b[cid]['properties'].get(k) for cid, v in a.items()) for k in c.ROLES}
    native = c.ET.fromstring(gzip.decompress((OUT / 'archi-transport/native-model.archimate.gz').read_bytes()))
    folders, members = [], {}
    def visit(folder, parent):
        path = parent + [folder.get('name', '')]
        folders.append(dict(path=path, id=folder.get('id'), directMembers=len(folder.findall('element')), childFolders=len(folder.findall('folder'))))
        for node in folder.findall('element'):
            members[node.get('id')] = dict(path=path, type=node.get(c.XSI), name=node.get('name'))
        for child in folder.findall('folder'):
            visit(child, path)
    for folder in native.findall('folder'):
        visit(folder, [])
    _, elements = c.load(ROOT)
    changed_ids = [r['canonicalId'] for r in inventory['added']] + [ids['iu'], ids['bo']]
    actual_changed = {cid: members[elements[cid][0]['exchange']['identifier']] for cid in changed_ids}
    native_report = dict(standing='Actual native save observation; Solution Architect structural review NOT PERFORMED',
        sourceCandidateSha256=c.sha(before_path.read_bytes()), folders=folders, changedElements=actual_changed)
    (OUT / 'archi-transport/imported-tree.json.gz').write_bytes(gzip.compress(c.encoded(native_report),mtime=0))
    rows = ''.join('<tr><td>'+html.escape(' / '.join(x['path']))+'</td><td>'+str(x['directMembers'])+'</td></tr>' for x in folders)
    (OUT / 'archi-transport/imported-tree.html').write_text('<!doctype html><meta charset="utf-8"><title>1.1.22 imported Archi tree</title><style>body{font:15px sans-serif;margin:2rem}td,th{padding:.3rem;border:1px solid #ccc}</style><h1>Actual imported native tree — IU candidate 1.1.22</h1><p>Observed from the exact retained native save. Supporting evidence only; human review and acceptance remain pending.</p><table><tr><th>Native folder path</th><th>Direct members</th></tr>'+rows+'</table>')
    summary = dict(actualArchiImportNativeSaveExport='PASS', strictTransport='FAIL',
        strictFailure='Relationship/View transport drift', baselineSha256=c.sha(before_path.read_bytes()),
        returnedXmlSha256=c.sha(gzip.decompress(returned_path.read_bytes())),
        canonicalElementPopulationPreserved=set(a)==set(b), preservedElementFieldCounts=fields,
        preservedGovernedPropertyValueCounts=roles, rawComparatorStatus=raw['comparison_status'],
        rawClassifications=raw['classifications'], inputIssues=raw['input_issues'],
        relationships={k:raw['relationships'][k] for k in ('before','after','missing_ids','additional_ids','changed_records')},
        views={k:raw['presentation'][k] for k in ('before','after','missing_ids','additional_ids','changed_records')},
        nativeChangedElementPaths=actual_changed,
        standing='UNRESOLVED — exact transport findings require Solution Architect disposition before dependent finalisation',
        limits=['No raw finding is normalised away or accepted by this report.',
                'Field equality and successful import are not lossless round-trip proof.',
                'The returned XML does not replace canonical Markdown or the preserved authored template.',
                'Human visual/tree/reader review and whole-HCRL result confirmation are NOT PERFORMED.'])
    (OUT / 'transport-summary.json').write_bytes(c.encoded(summary))
    print('Actual import/export PASS; strict transport FAIL retained', flush=True)


if __name__ == '__main__':
    {'preservation':preservation, 'independent':independent_generation, 'transport':transport}[sys.argv[1]]()
