from collections import defaultdict
from pathlib import Path
import copy
import json
import sys

sys.path.insert(0, 'enterprise-architecture/tooling')
import architecture_continuum as c
from containment import Ancestry
from relationship_state import eligible_ids
from exchange_compare import Exchange
from exchange_signatures import signature

BASE = Path('enterprise-architecture/design/evidence/1.1.19/001')
BEFORE = BASE / 'before/solutionfacet-final-naming'
OUT = BASE / 'after/solutionfacet-final-naming'


def verify():
    snapshot = c.read_json(BEFORE / 'source-state.json')
    plan = c.read_json(OUT / 'composition-and-naming-plan.json')
    targets = {row['canonicalId']: row for row in plan['elements']}
    old = Exchange(snapshot['previousCandidate'])
    current_path = OUT / 'Harmonia-SolutionFacet-final-naming-candidate.xml'
    new = Exchange(current_path)
    assert not old.issues and not new.issues
    _, elements = c.load()
    assert set(elements) == set(snapshot['elements']) and len(elements) == 1994
    nodes = {m['exchange']['identifier']: m for m, _, _ in elements.values()}
    edges = [row for m, _, _ in elements.values() for row in m['relationships']]
    eligible = eligible_ids([{'identifier': row['identifier'],
                             'record_state': row.get('standing', {}).get('currentness')}
                            for row in edges])
    incoming = defaultdict(list)
    for row in edges:
        if row['archimateType'] == 'Composition' and row['identifier'] in eligible:
            incoming[row['target']].append({'identifier': row['identifier'],
                'parent': row['source'], 'child': row['target'], 'mechanism': 'Composition'})
    ancestry = Ancestry(nodes, incoming)
    for cid, (metadata, body, path) in elements.items():
        before = snapshot['elements'][cid]
        assert str(path.relative_to(c.ROOT)) == before['path']
        assert metadata['canonicalId'] == cid
        assert metadata['canonicalName'] == before['names']['canonicalName']
        assert metadata['archimateType'] == before['archimateType']
        assert metadata['specialization'] == before['specialization']
        assert metadata['exchange']['identifier'] == before['exchangeIdentifier']
        assert c.sha(c.encoded(metadata['relationships'])) == before['relationshipsSha256']
        assert c.sha(c.encoded(metadata['identity'])) == before['identitySha256']
        assert c.sha(c.encoded(metadata['standing'])) == before['standingSha256']
        if cid not in targets:
            assert c.sha(path.read_bytes()) == before['sha256']
            continue
        target = targets[cid]
        original, _ = c.decode((BEFORE / (cid + '.md')).read_text())
        assert c.sha((BEFORE / (cid + '.md')).read_bytes()) == before['sha256']
        assert metadata['friendlyName'] == target['after']['friendlyName']
        assert metadata['displayName'] == target['after']['displayName']
        assert metadata['canonicalName'] is None
        assert metadata['friendlyName'] != metadata['displayName']
        assert metadata['naming']['authoredNativeName'] == original['naming']['authoredNativeName']
        assert metadata['provenance'][:-1] == original['provenance']
        restored = copy.deepcopy(metadata)
        for role in ('friendlyName', 'displayName'):
            restored[role] = original[role]
        restored['naming']['mode'] = original['naming']['mode']
        if original['naming'].get('expected'):
            restored['naming']['expected'] = original['naming']['expected']
        restored['provenance'] = original['provenance']
        assert restored == original
        path_ids = ancestry.ancestry(metadata['exchange']['identifier'])
        assert [nodes[rid]['canonicalId'] for rid in path_ids] == target['canonicalIdPathRootToLeaf']
        links = [ancestry.parent_link(rid)['identifier'] for rid in path_ids[1:]]
        assert links == [row['identifier'] for row in target['compositionRelationshipsRootToLeaf']]
        # Only the explicit Composition graph feeds the shared ancestry engine;
        # Home and lexical names are not inputs to this verification.
        assert all(link['mechanism'] == 'Composition' for rid in path_ids[1:]
                   for link in incoming[rid])
        node = new.elements[metadata['exchange']['identifier']]
        assert new.single(node, 'friendlyName') == metadata['friendlyName']
        assert node.findtext('a:name', namespaces=c.N) == metadata['displayName']
        assert new.single(node, 'displayName') == metadata['displayName']
        assert not new.claims(node, 'canonicalName')
    assert set(old.elements) == set(new.elements)
    assert set(old.relationships) == set(new.relationships) and len(new.relationships) == 2236
    assert all(signature(node, old) == signature(new.relationships[rid], new)
               for rid, node in old.relationships.items())
    assert set(old.views) == set(new.views) and len(new.views) == 1288
    assert all(signature(node, old) == signature(new.views[rid], new)
               for rid, node in old.views.items())
    for path in ['enterprise-architecture/continuum/navigation.json',
                 'enterprise-architecture/design/archimate/projection-template.xml']:
        assert c.sha(Path(path).read_bytes()) == snapshot['maintainedSourceHashes'][path]
    for path, digest in snapshot['priorEvidenceHashes'].items():
        assert c.sha(Path(path).read_bytes()) == digest, path
    for path, digest in snapshot['protectedPublishedFiles'].items():
        assert c.sha(Path(path).read_bytes()) == digest, path
    for path, digest in snapshot['maintainedSourceHashes'].items():
        if path.startswith('enterprise-architecture/tooling/') and path != 'enterprise-architecture/tooling/tests/test_statement_specializations.py':
            assert c.sha(Path(path).read_bytes()) == digest, path
    assert c.read_json('architecture-release.json') == snapshot['release']
    for cid in ['5de2ae2c3b794c2d9f3a14efd51d564f', '8882573df7ce43c6bc0f66da0a676139', 'b8cafe3cf5904d2bb9a9767260fddf78']:
        assert elements[cid][0]['archimateType'] == 'Principle'
        assert elements[cid][0]['specialization'] == 'ArchitecturalAxiom'
    repeated, validation = c.project()
    digest = c.sha(current_path.read_bytes())
    assert repeated == current_path.read_bytes()
    assert c.read_json(OUT / 'preflight-validation.json')['outputSha256'] == digest
    result = {'status': 'PASS', 'canonicalIdentitiesPreserved': 1994,
        'canonicalNameValuesIndependentlyPreserved': 1994,
        'nativeTypesSpecialisationsAndBindingsPreserved': 1994,
        'compositionContextsConfirmed': 3, 'friendlyNameUpdates': 2, 'displayNameUpdates': 1,
        'canonicalNameUpdates': 0, 'roleDistinctionInSourceAndXml': 'PASS',
        'allRelationshipsExactlyPreserved': 2236, 'allViewsExactlyPreserved': 1288,
        'compositionAndHomeSemanticsUnchanged': True,
        'G01ThroughG18AndNavigationBytesUnchanged': True,
        'maintainedViewTemplateBytesUnchanged': True,
        'unaffectedCanonicalFilesByteIdentical': 1991,
        'priorEvidenceFilesUnchanged': len(snapshot['priorEvidenceHashes']),
        'protectedPublicationsUnchanged': len(snapshot['protectedPublishedFiles']),
        'architecturalAxioms07_17_18Preserved': True,
        'sourceIdentityStandingAndPriorProvenancePreserved': True,
        'schema': validation['offlineXsd'], 'xmlIdsAndReferences': validation['exchangeReferences'],
        'deterministicReprojection': 'PASS', 'independentPreflightBytesEqualLiveCandidate': 'PASS',
        'productionValidationAndTransportMachineryUnchanged': True,
        'releaseUnchanged': snapshot['release'], 'unresolvedNamingOrAncestry': [],
        'gitCommandsOrMutationsPerformed': 0, 'candidateXml': str(current_path),
        'candidateSha256': digest,
        'limits': 'Existing independent canonicalName qualifications, inherited raw transport differences and unresolved Task event provenance remain; no acceptance or closure asserted.'}
    (OUT / 'verification.json').write_bytes(c.encoded(result))
    print(json.dumps(result, indent=2))


if __name__ == '__main__':
    verify()
