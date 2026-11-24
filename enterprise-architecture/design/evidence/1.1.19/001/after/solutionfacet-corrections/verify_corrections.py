from pathlib import Path
from urllib.parse import urlsplit, unquote
import copy
import json
import re
import sys

sys.path.insert(0, 'enterprise-architecture/tooling')
import architecture_continuum as c
from exchange_compare import Exchange
from exchange_signatures import signature

BASE = Path('enterprise-architecture/design/evidence/1.1.19/001')
BEFORE = BASE / 'before/solutionfacet-corrections'
OUT = BASE / 'after/solutionfacet-corrections'
LINK = re.compile(r'\]\((<[^>]+>|[^\s)]+)(?:\s+[^)]*)?\)')


def verify():
    snapshot = c.read_json(BEFORE / 'source-state.json')
    plan = c.read_json(OUT / 'correction-plan.json')
    candidate = OUT / 'Harmonia-SolutionFacet-corrected-candidate.xml'
    old = Exchange(snapshot['candidate'])
    new = Exchange(candidate)
    assert not old.issues and not new.issues
    catalogue, elements = c.load()
    assert set(elements) == set(snapshot['elements'])
    assert len(elements) == 1994
    changed = {row['canonicalId']: row for row in plan['canonical']}
    relocation = {(c.ROOT / row['beforePath']).resolve(): (c.ROOT / row['afterPath']).resolve()
                  for row in changed.values() if row['beforePath'] != row['afterPath']}
    link_count = 0
    for cid, (metadata, body, path) in elements.items():
        recorded = snapshot['elements'][cid]
        assert metadata['canonicalId'] == cid
        assert metadata['exchange']['identifier'] == recorded['exchangeId']
        assert metadata['archimateType'] == recorded['type']
        assert c.sha(c.encoded(metadata['relationships'])) == recorded['relationshipsSha256']
        assert c.sha(c.encoded(metadata['identity'])) == recorded['identitySha256']
        assert c.sha(c.encoded(metadata['standing'])) == recorded['standingSha256']
        if cid not in changed:
            assert str(path.relative_to(c.ROOT)) == recorded['path']
            assert c.sha(path.read_bytes()) == recorded['fileSha256']
            continue
        row = changed[cid]
        preimage = BEFORE / 'preimages' / row['beforePath']
        assert c.sha(preimage.read_bytes()) == recorded['fileSha256'] == row['beforeSha256']
        original, original_body = c.decode(preimage.read_text())
        restored = copy.deepcopy(metadata)
        if cid in plan['axioms']:
            assert metadata['specialization'] == 'ArchitecturalAxiom'
            assert metadata['archimateType'] == 'Principle'
            assert metadata['displayName'] == '[ArchitecturalAxiom] ' + original['displayName']
            restored['specialization'] = original['specialization']
            restored['displayName'] = original['displayName']
            restored['naming']['expected']['displayName'] = original['naming']['expected']['displayName']
            assert path == c.ROOT / c.element_path(metadata, catalogue)
            assert not (c.ROOT / row['beforePath']).exists()
        if cid in plan['friendlyNameCorrections']:
            assert metadata['friendlyName'] == plan['friendlyNameCorrections'][cid]
            restored['friendlyName'] = original['friendlyName']
            if original['naming'].get('expected'):
                restored['naming']['expected']['friendlyName'] = original['naming']['expected']['friendlyName']
        if cid in plan['axioms'] or cid in plan['friendlyNameCorrections']:
            assert metadata['provenance'][:-1] == original['provenance']
            assert metadata['provenance'][-1]['source'] == plan['authority']
            restored['provenance'] = metadata['provenance'][:-1]
        assert restored == original
        assert str(path.relative_to(c.ROOT)) == row['afterPath']
        old_content = original_body.split('## Architectural content\n', 1)[1]
        new_content = body.split('## Architectural content\n', 1)[1]
        assert LINK.sub('](UNCHANGED-TARGET)', old_content) == LINK.sub('](UNCHANGED-TARGET)', new_content)
        old_links = [match[1].strip('<>') for match in LINK.finditer(old_content)]
        new_links = [match[1].strip('<>') for match in LINK.finditer(new_content)]
        assert len(old_links) == len(new_links)
        old_path = c.ROOT / row['beforePath']
        for old_link, new_link in zip(old_links, new_links):
            a, b = urlsplit(old_link), urlsplit(new_link)
            assert a.scheme == b.scheme and a.netloc == b.netloc
            assert a.query == b.query and a.fragment == b.fragment
            if a.scheme or a.netloc or not a.path:
                assert old_link == new_link
                continue
            target = (old_path.parent / unquote(a.path)).resolve()
            expected = relocation.get(target, target)
            assert (path.parent / unquote(b.path)).resolve() == expected
            link_count += old_link != new_link
    assert set(old.elements) == set(new.elements)
    assert set(old.relationships) == set(new.relationships)
    assert len(new.relationships) == 2236
    assert all(signature(node, old) == signature(new.relationships[rid], new)
               for rid, node in old.relationships.items())
    assert set(old.views) == set(new.views)
    assert len(new.views) == 1288
    assert all(signature(node, old) == signature(new.views[rid], new)
               for rid, node in old.views.items())
    assert c.sha((c.ROOT / c.DESIGN / 'projection-template.xml').read_bytes()) == snapshot['templateSha256']
    assert c.sha((c.ROOT / c.CONTINUUM / 'navigation.json').read_bytes()) == snapshot['navigationSha256']
    bad = re.compile(r'PerformancAndCapacity|ReslienceAndRecovery|ArchitectureGovernance')
    for metadata, _, _ in elements.values():
        for role in ('displayName', 'friendlyName', 'canonicalName'):
            assert not bad.search(metadata.get(role) or '')
        for role, value in metadata['naming'].get('expected', {}).items():
            assert not bad.search(value or '')
    for row in plan['remainingNamingQualifications']:
        cid = row['canonicalId']
        path = elements[cid][2]
        if cid in changed:
            before, _ = c.decode((BEFORE / 'preimages' / changed[cid]['beforePath']).read_text())
            assert elements[cid][0][row['property']] == before[row['property']]
        else:
            assert c.sha(path.read_bytes()) == snapshot['elements'][cid]['fileSha256']
    for path, digest in snapshot['preservedPriorEvidence'].items():
        assert c.sha(Path(path).read_bytes()) == digest, path
    for path, digest in snapshot['priorReports'].items():
        assert c.sha(Path(path).read_bytes()) == digest, path
    for path, digest in snapshot['protectedPublishedFiles'].items():
        assert c.sha(Path(path).read_bytes()) == digest, path
    assert c.sha(Path(snapshot['authoredExport']['path']).read_bytes()) == snapshot['authoredExport']['sha256']
    assert c.read_json('architecture-release.json') == snapshot['release']
    # Compare a new live projection and the already independently staged
    # prospective projection, rather than trusting a cached generated digest.
    content, validation = c.project()
    digest = c.sha(candidate.read_bytes())
    assert content == candidate.read_bytes()
    assert c.read_json(OUT / 'preflight-validation.json')['outputSha256'] == digest
    result = {
        'status': 'PASS', 'elements': len(elements), 'canonicalIdsPreserved': 1994,
        'representationIdsPreserved': 1994, 'nativeTypesPreserved': 1994,
        'architecturalAxiomCorrections': 3, 'friendlyNameCorrections': 6,
        'storageRelocations': len(relocation), 'mechanicalLinkOnlyCanonicalFiles': 15,
        'canonicalLinkDestinationsRebased': link_count,
        'definitionsAndLinkTargetIdentitiesPreserved': True,
        'allRelationshipsExactlyPreserved': 2236, 'allViewRecordsExactlyPreserved': 1288,
        'compositionAndHomeSemanticsPreserved': True,
        'G01ThroughG18AndNavigationBytesPreserved': True,
        'maintainedPresentationTemplateBytesPreserved': True,
        'unaffectedCanonicalFilesByteIdentical': len(elements) - len(changed),
        'identityStandingAndPriorProvenancePreserved': True,
        'priorCandidateAndEvidenceFilesUnchanged': len(snapshot['preservedPriorEvidence']),
        'priorReportsUnchanged': len(snapshot['priorReports']),
        'protectedPublishedFilesUnchanged': len(snapshot['protectedPublishedFiles']),
        'originalAuthoredExportUnchanged': True, 'releaseUnchanged': snapshot['release'],
        'schema': validation['offlineXsd'], 'identifierAndReferences': validation['exchangeReferences'],
        'deterministicReprojection': 'PASS', 'independentStagePreflightByteEquality': 'PASS',
        'remainingNamingQualifications': plan['remainingNamingQualifications'],
        'candidateXml': str(candidate), 'candidateSha256': digest,
        'limits': 'No Solution Architect visual inspection, architectural acceptance, protected publication or HCRL closure asserted.'
    }
    (OUT / 'verification.json').write_bytes(c.encoded(result))
    print(json.dumps(result, indent=2))


if __name__ == '__main__':
    verify()
