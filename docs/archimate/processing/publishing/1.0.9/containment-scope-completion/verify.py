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
    original = d.g.ET.fromstring(outputs[m.model_path()])
    current = d.g.parse(m.model_path())
    scope = m.read(p.SELECTION)
    assert p.selection(b, original) == scope
    regeneration = p.audit(original, current, scope)
    previous = d.g.ET.fromstring(p.before_bytes(str(m.model_path().relative_to(ROOT))))
    incremental = p.audit(previous, current, scope)
    census = m.read(HERE / 'semantic-census-before.json')
    assert regeneration['containment_views_validated'] == 231
    assert regeneration['containment_views_changed'] == 231
    assert incremental['containment_views_changed'] == 112
    assert set(incremental['changed_views']) == set(census['additional_views'])
    assert incremental['business_views_changed'] == incremental['business_connections_changed'] == 0
    assert regeneration['business_views_validated'] == 61
    assert regeneration['business_connections_changed'] == regeneration['known_stale_connections_changed'] == 29
    assert len(incremental['visual_nodes_created']) == 1 and len(incremental['visual_connections_created']) == 331
    assert len(scope['oversized']) == 3
    feature = p.views(current)['id-c796d1c0d4c158d08f8bd86308f2f0a0']
    population = {node.get('elementRef') for node in feature.findall('a:node', d.g.N)}
    assert len(population) == 7 and 'id-727a264026584f80bc3304ff8383dc92' in population
    protected, snapshots = [], []
    for relative, row in m.read(HERE / 'before.json').items():
        assert sha(p.before_bytes(relative)) == row['sha256']
        (snapshots if 'snapshot' in row else protected).append(relative)
    result = dict(status='PASS', regeneration=regeneration, incremental=incremental,
        published_sha256=sha(m.model_path().read_bytes()),
        unchanged_protected_files=len(protected), byte_recoverable_snapshots=len(snapshots))
    (HERE / 'population-and-changes.json').write_bytes(m.encoded(result))
    return {**result, 'regeneration': {k:v for k,v in regeneration.items() if not isinstance(v, list)},
            'incremental': {k:v for k,v in incremental.items() if not isinstance(v, list)}}


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
    # Replay the unchanged earlier verifier with its original presentation
    # implementation and pinned candidate. Never rewrite earlier evidence.
    import importlib.util
    with tempfile.TemporaryDirectory(prefix='harmonia-containment-history-', dir='/tmp') as temporary:
        original_path = Path(temporary) / 'view_presentation.py'
        original_path.write_bytes(p.before_bytes('docs/archimate/framework/tools/view_presentation.py'))
        spec = importlib.util.spec_from_file_location('historical_view_presentation', original_path)
        previous_p = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(previous_p)
        original_before = previous_p.before_bytes
        old_manifest = m.read(previous_p.EVIDENCE / 'before.json')
        new_manifest = m.read(HERE / 'before.json')
        def historical_before(relative):
            row = old_manifest[relative]
            if 'snapshot' not in row and relative in new_manifest:
                value = p.before_bytes(relative)
                assert sha(value) == row['sha256'], relative
                return value
            return original_before(relative)
        previous_p.before_bytes = historical_before
        current_module = sys.modules['view_presentation']
        sys.modules['view_presentation'] = previous_p
        try:
            verifier = runpy.run_path(str(previous_p.EVIDENCE / 'verify.py'))
            earlier = verifier['history']()
            snapshot = Path(temporary) / 'prior-candidate.xml'
            snapshot.write_bytes(p.before_bytes(str(m.model_path().relative_to(ROOT))))
            original_exchange = d.Exchange
            def exchange(path):
                return original_exchange(snapshot if Path(path) == m.model_path() else path)
            d.Exchange = exchange
            try:
                # The earlier probe's output locations must also remain intact.
                with tempfile.TemporaryDirectory(prefix='harmonia-old-probe-', dir='/tmp') as probe:
                    destination = Path(probe)
                    (destination / 'roundtrip').mkdir()
                    for name in ('Archi-no-edit-export.xml.gz',):
                        (destination / 'roundtrip' / name).write_bytes((previous_p.EVIDENCE / 'roundtrip' / name).read_bytes())
                    verifier['transport'].__globals__['HERE'] = destination
                    prior_transport = verifier['transport']()
            finally:
                d.Exchange = original_exchange
        finally:
            sys.modules['view_presentation'] = current_module
    return dict(status='PASS', previous_history=earlier, previous_presentation_transport=prior_transport,
                historical_files='UNCHANGED; checked against their pinned candidates')


def integrity():
    evidence = m.read(HERE / 'verification.json')
    assert evidence['status'] == 'PASS'
    assert evidence['architecture_release'] == m.read(ROOT / 'architecture-release.json')
    assert evidence['published_sha256'] == sha(m.model_path().read_bytes())
    for group in ('source_sha256', 'artifacts_sha256'):
        for relative, digest in evidence[group].items():
            assert sha((ROOT / relative).read_bytes()) == digest, relative
    for name, check in evidence['checks'].items():
        assert check['status'] == 'PASS' and sha((ROOT / check['log']).read_bytes()) == check['log_sha256'], name
    for check in evidence['unsuccessful_attempts']:
        assert sha((ROOT / check['log']).read_bytes()) == check['log_sha256']
    reproduction = m.read(HERE / 'reproduction.json')
    assert reproduction['status'] == 'PASS' and reproduction['generated_outputs'] == 15
    for relative, digest in reproduction['outputs_sha256'].items():
        assert sha((ROOT / relative).read_bytes()) == digest, relative
    for relative in m.read(HERE / 'before.json'):
        p.before_bytes(relative)
    return dict(status='PASS', published_sha256=evidence['published_sha256'],
        checks=len(evidence['checks']), generated_outputs=15, before_files=647,
        all_sources_artifacts_logs_and_protected_files='HASH VERIFIED',
        unsigned=True, information_architecture='NOT COMMENCED')


if __name__ == '__main__':
    result = dict(preservation=preservation, serialization=serialization, transport=transport,
                  history=history, integrity=integrity)[sys.argv[1]]()
    print(m.encoded(result).decode(), end='')
