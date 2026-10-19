#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only current presentation, transport and preserved-history checks."""
from collections import Counter
import contextlib
import copy
import gzip
import hashlib
import io
import json
from pathlib import Path
import runpy
import sys
import tempfile

HERE = Path(__file__).resolve().parent
ROOT = next(p for p in HERE.parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import diagnose_roundtrip as d
import governed_metadata as m
import home_context as hc
import home_relationships as h
import publish_navigation as nav
import synchronise_baseline as s
import view_presentation as p


def sha(data):
    return hashlib.sha256(data).hexdigest()


def preservation():
    outputs, b, _ = s.publish(presentation=False)
    relative = str(m.model_path().relative_to(ROOT))
    assert outputs[m.model_path()] == p.before_bytes(relative), 'Pre-presentation publisher no longer reproduces its pinned candidate'
    current = d.g.parse(m.model_path())
    scope = m.read(p.SELECTION)
    assert p.selection(b, d.g.ET.fromstring(outputs[m.model_path()])) == scope
    result = p.audit(d.g.ET.fromstring(outputs[m.model_path()]), current, scope)
    protected, snapshotted = [], []
    for relative, row in m.read(HERE / 'before.json').items():
        assert sha(p.before_bytes(relative)) == row['sha256']
        (snapshotted if 'snapshot' in row else protected).append(relative)
    result.update(unchanged_protected_files=len(protected), byte_recoverable_snapshots=len(snapshotted),
                  published_sha256=sha(m.model_path().read_bytes()))
    (HERE / 'population-and-changes.json').write_bytes(m.encoded(result))
    return {k: v for k, v in result.items() if k not in ('connection_changes', 'changed_views')}


def serialization():
    with tempfile.TemporaryDirectory(prefix='harmonia-presentation-serialization-', dir='/tmp') as temporary:
        path = Path(temporary) / 'roundtrip.xml'
        path.write_bytes(d.g.serialize(d.g.parse(m.model_path())))
        result = d.compare(d.Exchange(m.model_path()), d.Exchange(path))
    assert result['comparison_status'] == 'EQUIVALENT', result['classifications']
    return dict(status='PASS', comparison_status=result['comparison_status'], classifications=result['classifications'])


def signature(element, model, properties=True):
    result = copy.deepcopy(element)
    if properties:
        for prop in result.findall('.//a:property', d.g.N):
            prop.set('propertyDefinitionRef', model.keys[prop.get('propertyDefinitionRef')])
    else:
        for prop in result.findall('a:properties', d.g.N):
            result.remove(prop)
    return d.g.signature(result)


def transport():
    before = d.Exchange(m.model_path())
    with tempfile.TemporaryDirectory(prefix='harmonia-presentation-transport-', dir='/tmp') as temporary:
        path = Path(temporary) / 'no-edit-export.xml'
        path.write_bytes(gzip.decompress((HERE / 'roundtrip/Archi-no-edit-export.xml.gz').read_bytes()))
        after = d.Exchange(path)
        raw = d.compare(before, after)
    assert not before.issues and not after.issues, (before.issues, after.issues)
    for role in d.ROLES:
        claims = [{identifier: [d.values(prop, 'value') for prop in model.claims(owner, role)]
                   for identifier, owner in model.elements.items() if model.claims(owner, role)} for model in (before, after)]
        assert claims[0] == claims[1], role
    for name in ('elements', 'relationships'):
        left, right = getattr(before, name), getattr(after, name)
        assert left.keys() == right.keys()
        for identifier in left:
            assert signature(left[identifier], before, False) == signature(right[identifier], after, False), identifier
    homes = [{row['identifier']: row for row in h.authored_homes(model)} for model in (before, after)]
    assert homes[0] == homes[1] and len(homes[0]) == 212
    capture = m.read(m.RAD / 'accepted-ingestion-1.0.8.json')
    for name in ('elements', 'relationships', 'views'):
        for row in capture[name]:
            identifier = row['representationId']
            assert signature(getattr(before, name)[identifier], before) == signature(getattr(after, name)[identifier], after), identifier
    assert before.views.keys() == after.views.keys()
    objects = Counter()
    for identifier, view in before.views.items():
        other = after.views[identifier]
        assert view.attrib == other.attrib and d.values(view, 'name') == d.values(other, 'name'), identifier
        for tag in ('node', 'connection'):
            left = {n.get('identifier'): n for n in view.findall('.//a:'+tag, d.g.N)}
            right = {n.get('identifier'): n for n in other.findall('.//a:'+tag, d.g.N)}
            assert left.keys() == right.keys()
            for key, item in left.items():
                target = right[key]
                assert item.attrib == target.attrib and item.getparent().get('identifier') == target.getparent().get('identifier'), key
                if tag == 'connection':
                    assert d.g.signature(item) == d.g.signature(target), key
                else:
                    assert [n.get('identifier') for n in item.findall('a:node', d.g.N)] == [n.get('identifier') for n in target.findall('a:node', d.g.N)]
            objects[tag] += len(left)
        assert [n.get('identifier') for n in view.findall('a:node', d.g.N)] == [n.get('identifier') for n in other.findall('a:node', d.g.N)]
    assert nav.navigation_homes(before.root) == nav.navigation_homes(after.root)
    original = m.read(ROOT / '.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-fidelity.json')
    semantic = lambda report: Counter(d.encoded(f) for f in report['findings'] if f['classification'] == d.SEMANTIC)
    assert semantic(raw) == semantic(original) and sum(semantic(raw).values()) == 1669
    assert raw['comparison_status'] == 'DIFFERENCES'
    (HERE / 'roundtrip/raw-comparator.json.gz').write_bytes(gzip.compress(m.encoded(raw), mtime=0))
    result = dict(status='PASS', scope='Actual Archi 5.10 no-edit transport of the corrected RAD-1.0.9 candidate',
        operator='Codex, installed Archi CLI; no human rendering attestation',
        published_sha256=before.sha256, exported_sha256=after.sha256,
        elements=len(before.elements), relationships=len(before.relationships), views=len(before.views), objects=dict(objects),
        all_identity_naming_properties='EXACT', native_semantic_content='EXACT', home_assertions=212,
        all_view_geometry_and_connections='EXACT', protected_authored_views='EXACT', navigation_homes='EXACT',
        raw_comparator_status=raw['comparison_status'], raw_classifications=raw['classifications'],
        unchanged_historical_semantic_text_findings=1669, human_rendering='PENDING SOLUTION ARCHITECT INSPECTION')
    (HERE / 'roundtrip/archi-transport-verification.json').write_bytes(m.encoded(result))
    return result


def history():
    # Historical scripts are executed unchanged against their own pinned inputs.
    # Their original hashes/results are never reissued as current validation.
    import verify_governance_evidence as vg
    original_sha = vg.sha
    manifest = m.read(HERE / 'before.json')
    def historical_sha(path):
        relative = str(path.relative_to(ROOT)) if path.is_relative_to(ROOT) else None
        if relative in manifest and 'snapshot' in manifest[relative]:
            return sha(p.before_bytes(relative))
        return original_sha(path)
    vg.sha = historical_sha
    try:
        governance = vg.verify()
    finally:
        vg.sha = original_sha
    evidence = []
    for folder in (m.PUBLISHING / '1.0.9/roundtrip', hc.EVIDENCE / 'roundtrip'):
        saved = m.read(folder / 'archi-transport-verification.json')
        relative = str(m.model_path().relative_to(ROOT))
        candidates = [p.before_bytes(relative), hc.before_bytes(relative)]
        data = next(value for value in candidates if sha(value) == saved['published_sha256'])
        with tempfile.TemporaryDirectory(prefix='harmonia-presentation-history-', dir='/tmp') as temporary:
            snapshot = Path(temporary) / 'published.xml'
            snapshot.write_bytes(data)
            original_exchange = d.Exchange
            def exchange(path):
                return original_exchange(snapshot if Path(path) == m.model_path() else path)
            d.Exchange = exchange
            stream = io.StringIO()
            try:
                with contextlib.redirect_stdout(stream):
                    runpy.run_path(str(folder / 'verify-transport.py'), run_name='__main__')
            finally:
                d.Exchange = original_exchange
        evidence.append(dict(path=str(folder.relative_to(ROOT)), result=json.loads(stream.getvalue())))
    outputs, b, _ = s.publish(presentation=False)
    home_audit = hc.audit(b, d.g.ET.fromstring(outputs[m.model_path()]))
    assert home_audit == m.read(hc.EVIDENCE / 'population.json')
    return dict(status='PASS', governance_evidence=governance, prior_transport_replays=evidence,
                original_home_population_audit='REPRODUCED EXACTLY against original candidate', historical_files='UNCHANGED')


if __name__ == '__main__':
    result = dict(preservation=preservation, serialization=serialization, transport=transport, history=history)[sys.argv[1]]()
    print(m.encoded(result).decode(), end='')
