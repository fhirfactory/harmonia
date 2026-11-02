from pathlib import Path
import copy, json, shutil, sys, tempfile
sys.path.insert(0, 'enterprise-architecture/tooling')
sys.path.insert(0, 'enterprise-architecture/tooling/tests')
from types import SimpleNamespace
import architecture_continuum as c
from exchange_compare import Exchange
from exchange_signatures import signature
from current_source_fixture import copy_retained_inputs

base = Path('enterprise-architecture/design/evidence/1.1.19/001')
out = base / 'after/solutionfacet'
snapshot = c.read_json(base / 'before/solutionfacet-baseline.json')
candidate = out / 'Harmonia-SolutionFacet-candidate.xml'
a = Exchange('enterprise-architecture/design/archimate/published/1.1.19/Harmonia-Architecture.xml')
b = Exchange(base / 'before/Harmonia-Continuum-1.1.19-001.xml')
v = Exchange(candidate)
assert not a.issues and not b.issues and not v.issues
catalogue, elements = c.load()
assert set(elements) == set(snapshot['elements'])
assert set(a.elements) == set(v.elements) == set(b.elements)
assert set(v.relationships) == set(b.relationships)
assert set(v.relationships) - set(a.relationships) == {'id-8590dea2bc74433dbc99f17927cdc092'}
facets = {cid: m for cid, (m, _, _) in elements.items() if m['specialization'] == 'SolutionFacet'}
assert len(facets) == 229
for cid, (m, _, path) in elements.items():
    before = snapshot['elements'][cid]
    assert str(path.relative_to(c.ROOT)) == before['path']
    assert m['exchange']['identifier'] == before['exchangeId']
    assert m['archimateType'] == before['nativeType']
    assert c.sha(c.encoded(m['identity'])) == before['identitySha256']
    assert m['standing'] == before['standing']
    relationships = [r for r in m['relationships'] if r['identifier'] != 'id-8590dea2bc74433dbc99f17927cdc092']
    assert c.sha(c.encoded(relationships)) == before['relationshipSha256']
    prior = m['provenance'][:-2] if cid in facets else m['provenance']
    assert c.sha(c.encoded(prior)) == before['provenanceSha256']
    if cid in facets:
        node = b.elements[m['exchange']['identifier']]
        assert m['displayName'] == b.single(node, 'displayName')
        assert m['friendlyName'] == b.single(node, 'friendlyName')
    else:
        assert c.sha(path.read_bytes()) == before['sha256']
for rid, node in a.relationships.items():
    assert signature(node, a) == signature(v.relationships[rid], v)
assert signature(v.relationships['id-8590dea2bc74433dbc99f17927cdc092'], v) == signature(b.relationships['id-8590dea2bc74433dbc99f17927cdc092'], b)
assert set(v.views) == set(b.views)
assert all(signature(node, b) == signature(v.views[rid], v) for rid, node in b.views.items())
old_tree = c.read_json('enterprise-architecture/continuum/navigation.json')
import subprocess
baseline_tree = json.loads(subprocess.check_output(['git', 'show', snapshot['gitHead'] + ':enterprise-architecture/continuum/navigation.json']))
def structural(node):
    if isinstance(node, dict):
        return {key: structural(value) for key, value in node.items() if key not in ('title', 'description')}
    if isinstance(node, list):
        return [structural(value) for value in node]
    return node
assert structural(old_tree) == structural(baseline_tree)
groups = {cid: m['subjectGroup'] for cid, (m, _, _) in elements.items() if m.get('subjectGroup')}
roots = sorted(m['sourceId'] for m in groups.values() if m.get('sourceId') in {f'G{i:02}' for i in range(1, 19)})
assert roots == [f'G{i:02}' for i in range(1, 19)]
before_source = {str(p.relative_to(c.ROOT)): c.sha(p.read_bytes()) for _, _, p in elements.values()}
regenerated, validation = c.project()
assert regenerated == candidate.read_bytes()
with tempfile.TemporaryDirectory(prefix='harmonia-solutionfacet-independent-') as temporary:
    stage = Path(temporary)
    for rel in (c.CONTINUUM, c.CONFIG, c.SCHEMAS):
        shutil.copytree(c.ROOT / rel, stage / rel)
    (stage / c.DESIGN).mkdir(parents=True)
    shutil.copyfile(c.ROOT / c.DESIGN / 'projection-template.xml', stage / c.DESIGN / 'projection-template.xml')
    shutil.copyfile(c.ROOT / 'architecture-release.json', stage / 'architecture-release.json')
    copy_retained_inputs(c.ROOT, stage)
    independent, _ = c.project(stage)
    assert independent == regenerated
    assert all(not (stage / forbidden).exists() for forbidden in ('docs', 'scripts', '.git'))
assert before_source == {str(p.relative_to(c.ROOT)): c.sha(p.read_bytes()) for _, _, p in c.load()[1].values()}
assert snapshot['release'] == c.read_json('architecture-release.json')
for path, digest in snapshot['protectedPublishedFiles'].items():
    assert c.sha(Path(path).read_bytes()) == digest, path
composition = [r for _, (m, _, _) in elements.items() for r in m['relationships'] if r['archimateType'] == 'Composition']
home = [r for _, (m, _, _) in elements.items() for r in m['relationships'] if r.get('specialization') == 'Home']
result = {'status': 'PASS', 'elements': len(elements), 'canonicalIdentitiesPreserved': len(elements),
          'representationIdentitiesPreserved': len(elements), 'relationships': len(v.relationships),
          'existingRelationshipsExactlyPreserved': len(a.relationships), 'authoredAggregationAdded': 1,
          'compositionAssertionsPreserved': len(composition), 'homeAssertionsPreserved': len(home),
          'G01ThroughG18AndClassificationHierarchyPreserved': True, 'solutionFacets': len(facets),
          'views': len(v.views), 'allAuthoredViewContentPreserved': True,
          'noUnauthorisedElementAdditionsOrDeletions': True,
          'nonFacetCanonicalFilesUnchanged': len(elements) - len(facets),
          'priorIdentityStandingAndProvenancePreserved': True, 'deterministicReprojection': 'PASS',
          'isolatedMaintainedInputsProjection': 'PASS', 'offlineXsd': validation['offlineXsd'],
          'identityAndReferences': 'PASS', 'protectedPublishedFilesUnchanged': len(snapshot['protectedPublishedFiles']),
          'releaseUnchanged': snapshot['release'], 'candidateSha256': c.sha(candidate.read_bytes()),
          'limits': ['Three unrelated ArchitecturalAxiom reclassifications remain baseline-qualified differences.',
                     'Submitted explicit displayName/friendlyName retained; native names and inconsistencies recorded.',
                     'Automated preservation does not establish Solution Architect visual inspection or acceptance.']}
(out / 'verification.json').write_bytes(c.encoded(result))
print(json.dumps(result, indent=2))
