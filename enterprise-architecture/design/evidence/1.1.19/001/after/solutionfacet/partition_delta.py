from pathlib import Path
import hashlib, json

base = Path('enterprise-architecture/design/evidence/1.1.19/001/after/solutionfacet')
source = base / 'architectural-delta.json'
data = json.loads(source.read_text())
dest = base / 'batches'
dest.mkdir(exist_ok=True)
manifest = {'sourceSha256': hashlib.sha256(source.read_bytes()).hexdigest(),
            'limitBytes': 128000, 'batches': []}
records = []
for category in ('elements', 'relationships', 'views'):
    for action in ('added', 'removed', 'changed'):
        for index, row in enumerate(data[category][action]):
            if action == 'changed':
                for number, difference in enumerate(row['differences']):
                    records.append({'sourcePointer': f'/{category}/{action}/{index}/differences/{number}',
                                    'category': category, 'action': action,
                                    'identifier': row['identifier'], 'difference': difference})
            else:
                records.append({'sourcePointer': f'/{category}/{action}/{index}',
                                'category': category, 'action': action, 'record': row})
for category in ('viewNavigation', 'classified'):
    for field, value in data[category].items():
        entries = enumerate(value) if isinstance(value, list) else [('value', value)]
        for index, entry in entries:
            records.append({'sourcePointer': f'/{category}/{field}' + (f'/{index}' if index != 'value' else ''),
                            'category': category, 'field': field, 'record': entry})

def encoded(rows):
    return json.dumps({'sourceSha256': manifest['sourceSha256'], 'records': rows},
                      ensure_ascii=False, indent=2).encode() + b'\n'

pending = []
def emit(rows):
    content = encoded(rows)
    assert len(content) <= 128000
    name = f'batch-{len(manifest["batches"])+1:03}.json'
    (dest / name).write_bytes(content)
    manifest['batches'].append({'file': name, 'bytes': len(content),
                              'sha256': hashlib.sha256(content).hexdigest(),
                              'records': len(rows),
                              'categories': sorted({r['category'] for r in rows})})

for row in records:
    assert len(encoded([row])) <= 128000, row['sourcePointer']
    if pending and len(encoded(pending + [row])) > 100000:
        emit(pending)
        pending = []
    pending.append(row)
if pending:
    emit(pending)

seen = []
for batch in manifest['batches']:
    content = (dest / batch['file']).read_bytes()
    assert hashlib.sha256(content).hexdigest() == batch['sha256']
    for row in json.loads(content)['records']:
        target = data
        for token in row['sourcePointer'].strip('/').split('/'):
            target = target[int(token)] if isinstance(target, list) else target[token]
        assert target == row.get('difference', row.get('record'))
        seen.append(row['sourcePointer'])
assert len(set(seen)) == len(records)
manifest.update(recordCount=len(records), exactPointerCoverage='PASS',
                outcome='PASS: independently hash/pointer-verifiable batches; no record discarded')
(dest / 'manifest.json').write_text(json.dumps(manifest, indent=2) + '\n')
print(json.dumps(manifest, indent=2))
