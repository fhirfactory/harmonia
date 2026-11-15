import hashlib
import json
import subprocess
from pathlib import Path
from lxml import etree as ET

root = Path.cwd()
cont = root / 'enterprise-architecture/continuum'
ev = root / 'enterprise-architecture/design/evidence/1.1.25/001'
baseline = json.loads((ev / 'before/baseline.json').read_text())
delta = json.loads((ev / 'after/canonical-delta.json').read_text())
changed_ids = {row['canonicalId'] for row in delta['changes']}

def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def parse(text):
    head, body = text[4:].split('\n---\n', 1)
    return json.loads(head), body

current = {}
changed = []
mismatches = []
for path in cont.rglob('*.md'):
    if path.name == 'MANIFEST.md':
        continue
    data, body = parse(path.read_text())
    ident = data['canonicalId']
    current[ident] = (data, body, path)
    old = baseline['elements'][ident]
    relation_hash = hashlib.sha256(json.dumps(data['relationships'], sort_keys=True, ensure_ascii=False).encode()).hexdigest()
    assert relation_hash == old['relationshipsSha256'], ident
    assert data['exchange']['identifier'] == old['exchangeId'], ident
    assert (data['displayName'], data['archimateType'], data.get('specialization')) == (old['displayName'], old['archimateType'], old['specialization']), ident
    if sha(path) != old['sha256']:
        changed.append(ident)
        assert ident in changed_ids, str(path)
        original, _ = parse(subprocess.check_output(['git', 'show', baseline['head'] + ':' + old['path']], text=True))
        for key in ['canonicalId', 'displayName', 'canonicalName', 'friendlyName', 'archimateType', 'specialization', 'identity', 'naming', 'exchange', 'relationships', 'unresolvedProperties']:
            assert original[key] == data[key], (ident, key)
    if data['specialization'] == 'Topic':
        assert sha(path) == old['sha256'], 'Existing Topic instance changed: ' + ident

assert set(current) == set(baseline['elements'])
assert set(changed) == changed_ids
for group in ['userFiles', 'protectedFiles']:
    for path, digest in baseline[group].items():
        if not (root / path).exists() or sha(root / path) != digest:
            mismatches.append({'group': group, 'path': path})
assert not mismatches, mismatches

anchors = changed_ids | {
    '13c028886fde4682812b45772e21522f', '307b3440a507403f8bc214d290bffb6a',
    '1af8a56358f7429fabfb57be66542a99', '7985e647bf9e4ca3ba70faf9e3e4ae02',
    '899d34ec491948b68aad09e507dea6d1', 'fefda110e2274884ad58906733606efd',
    'd739c75dffe2453081735710b285f03b', 'e282811b51274649816566aac7edeb8a',
    '171f05d188384cedb7d5c493eeaee519', 'b683ae87c72a4c16aba948a4b8cb0998',
    '410a938f42964474b7cf5655815dc830', '105f6ebe61f74bc6b51938ba550bc1a7',
    'dcb31810ae134818adeeba9b27bd2f30', '2988247b75664896a7deb97b12b20c0a',
    'bffb611e29844eedaf39a5b27ccbbcc7', '0bddade1309e426aa84d0cd97c35742a',
}
by_exchange = {d['exchange']['identifier']: ident for ident, (d, b, p) in current.items()}
all_relations = [r for d, b, p in current.values() for r in d['relationships']]
incident = [r for r in all_relations if by_exchange.get(r['source']) in anchors or by_exchange.get(r['target']) in anchors]
parents = {}
for r in all_relations:
    if r['archimateType'] == 'Composition':
        source, target = by_exchange[r['source']], by_exchange[r['target']]
        if current[source][0]['archimateType'] == current[target][0]['archimateType'] == 'Capability':
            parents.setdefault(target, []).append((source, r['identifier']))
feature_ids = {by_exchange[r['target']] for r in incident if current[by_exchange[r['target']]][0].get('specialization') == 'Feature'}

def ancestry(ident, seen=()):
    assert ident not in seen
    row = {'canonicalId': ident, 'displayName': current[ident][0]['displayName']}
    return [[row]] if ident not in parents else [path + [dict(row, compositionId=edge)] for parent, edge in parents[ident] for path in ancestry(parent, seen + (ident,))]

inventory = {
    'baselineHead': baseline['head'], 'standing': 'Existing identities/edges/Feature ancestry; no new native allocation',
    'anchors': [{'canonicalId': ident, 'path': str(current[ident][2].relative_to(root)), 'displayName': current[ident][0]['displayName'], 'archimateType': current[ident][0]['archimateType'], 'specialization': current[ident][0].get('specialization')} for ident in sorted(anchors)],
    'incidentRelationships': incident,
    'existingFeatureAncestry': {ident: ancestry(ident) for ident in sorted(feature_ids)},
    'relationshipCorrespondence': 'Every incident relationship is byte-for-field identical to predecessor source; whole-graph hashes checked independently against before/baseline.json.',
}
(ev / 'after/canonical-inventory.json').write_text(json.dumps(inventory, ensure_ascii=False, indent=2) + '\n')

N = {'a': 'http://www.opengroup.org/xsd/archimate/3.0/'}
base_xml = root / 'enterprise-architecture/design/archimate/published/1.1.24/Harmonia-Architecture.xml'
candidate = ev / 'after/candidate-v2/reader/Harmonia-Architecture.xml'
xml_comparison = {'status': 'NOT_PERFORMED', 'reason': 'Candidate not yet generated'}
if candidate.exists():
    a, b = ET.parse(str(base_xml)), ET.parse(str(candidate))
    for tag in ['relationships', 'organizations', 'views']:
        assert ET.tostring(a.find('a:' + tag, N)) == ET.tostring(b.find('a:' + tag, N)), tag
    ae = {e.get('identifier'): e for e in a.findall('a:elements/a:element', N)}
    be = {e.get('identifier'): e for e in b.findall('a:elements/a:element', N)}
    assert ae.keys() == be.keys()
    changed_xml = [by_exchange[key] for key in ae if ET.tostring(ae[key]) != ET.tostring(be[key])]
    assert set(changed_xml) == changed_ids, changed_xml
    xml_comparison = {'status': 'PASS', 'changedElementRecords': changed_xml, 'relationshipRecordsUnchanged': len(all_relations), 'authoredViewsUnchanged': len(a.findall('a:views/a:diagrams/a:view', N)), 'organizationsUnchanged': True, 'candidateSha256': sha(candidate)}

preservation = {
    'status': 'PASS', 'canonicalElements': len(current), 'relationships': len(all_relations),
    'changedCanonicalElements': sorted(changed), 'newElements': 0, 'newRelationships': 0,
    'unchangedUserFiles': len(baseline['userFiles']), 'unchangedProtectedFiles': len(baseline['protectedFiles']),
    'allExistingTopicInstancesUnchanged': True, 'allIdentityNameTypeAndRelationshipMetadataUnchanged': True,
    'mismatches': mismatches, 'exchangeComparison': xml_comparison,
    'gitHeadUnchanged': subprocess.check_output(['git', 'rev-parse', 'HEAD'], text=True).strip() == baseline['head'],
}
assert preservation['gitHeadUnchanged']
(ev / 'after/preservation.json').write_text(json.dumps(preservation, ensure_ascii=False, indent=2) + '\n')
print(json.dumps({k: v for k, v in preservation.items() if k != 'changedCanonicalElements'}, ensure_ascii=False))
