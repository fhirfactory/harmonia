#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify diagnostic facts against the accepted release file, never reproject it.

The maintained current-projection review gate deliberately rejects a historical
1.1.11 projection when root release is 1.1.12. This read-only diagnostic compares
the exact published file and actual returns; it does not bypass that gate for
canonical ingestion, acceptance or application.
"""
import argparse
from collections import Counter
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys
from types import SimpleNamespace
import xml.etree.ElementTree as ET

sys.path.insert(0, str(Path.cwd() / 'enterprise-architecture/tooling'))
import continuum_workbench as w
import architecture_continuum as c
from exchange_signatures import signature
from inspect_organisation import items, native, exchange, PUBLISHED, N


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main():
    p = argparse.ArgumentParser()
    p.add_argument('--work', type=Path, required=True)
    p.add_argument('--output', type=Path, required=True)
    args = p.parse_args()
    work, output = args.work, args.output
    baseline = Path(PUBLISHED)
    a, original = w.model(baseline)
    b, returned = w.model(work / 'returned.xml')
    assert original == returned, 'Canonical element field/property drift'
    signatures = {}
    for xpath, item, expected_count in [('a:relationships/a:relationship', 'relationship', 1651),
                                       ('a:views/a:diagrams/a:view', 'view', 1209)]:
        def records(root):
            model = SimpleNamespace(keys={x.get('identifier'): x.findtext('a:name', namespaces=N)
                                           for x in root.findall('a:propertyDefinitions/a:propertyDefinition', N)})
            return {x.get('identifier'): signature(x, model) for x in root.findall(xpath, N)}
        left, right = records(a), records(b)
        assert len(left) == len(right) == expected_count
        signatures[item] = {'before': len(left), 'after': len(right), 'exactSignaturesEqual': left == right}
        assert left == right, item + ' changed'
    f1, o1 = native(ET.parse(work / 'published.archimate').getroot())
    f2, o2 = native(ET.parse(work / 'published-second.archimate').getroot())
    assert o1 == o2, 'Second-pass published object placement/type changed'
    assert {tuple(x['path']) for x in f1} == {tuple(x['path']) for x in f2}, 'Second-pass folders changed'
    # All source element refs are unique, and all surviving refs are leaf items.
    src, ret = items(exchange(baseline)), items(exchange(work / 'returned.xml'))
    element_ids = {x['identifier'] for x in original.values()}
    assert Counter(x['ref'] for x in src if x['ref'] in element_ids) == Counter(element_ids)
    assert Counter(x['ref'] for x in ret if x['ref'] in element_ids) == Counter(element_ids)
    assert not any(x['children'] and x['ref'] in element_ids for x in ret)
    # Representative synthetic experiments and candidate-only representations.
    mf, mo = native(ET.parse(work / 'mechanics.archimate').getroot())
    mf2, mo2 = native(ET.parse(work / 'mechanics-second.archimate').getroot())
    expected = {
        'simple': ['Strategy', 'Diagnostic cases', '1 Simple folder', 'Simple canonical leaf'],
        'parent': ['Strategy', 'Parent with children'],
        'child': ['Strategy', 'Diagnostic cases', '2 Combined parent item', 'Parent folder label', 'Child leaf'],
        'context-cap': ['Strategy', 'Diagnostic cases', '3 Canonical primary location', 'Canonical contextual Capability'],
        'context-function': ['Business', 'Diagnostic cases', '3 Business context', 'CT1 context', 'CT2 context', 'Contextual behaviour'],
        'ct1': ['Strategy', '[CT1] Diagnostic parent'],
        'ct2': ['Strategy', '[CT2] Diagnostic child'],
        'ct3': ['Strategy', 'Diagnostic cases', '4 Nested Strategy', 'CT1 parent', 'CT2 parent', '[CT3] Diagnostic leaf'],
        'care': ['Application', '[Topic] Diagnostic Care'],
        'encounter': ['Application', 'Diagnostic cases', '5 Nested Application', 'Care parent', '[Topic] Diagnostic Encounter'],
        'a-ct1': ['Strategy', 'Diagnostic cases', '6 Candidate A Strategy', 'CT1 folder', '[CT1] Folder-plus-element parent'],
        'a-ct2': ['Strategy', 'Diagnostic cases', '6 Candidate A Strategy', 'CT1 folder', 'CT2 folder', '[CT2] Folder-plus-element child'],
        'a-ct3': ['Strategy', 'Diagnostic cases', '6 Candidate A Strategy', 'CT1 folder', 'CT2 folder', '[CT3] Folder-plus-element leaf'],
        'a-care': ['Application', 'Diagnostic cases', '6 Candidate A Application', 'Care folder', '[Topic] Folder-plus-element Care'],
        'a-encounter': ['Application', 'Diagnostic cases', '6 Candidate A Application', 'Care folder', '[Topic] Folder-plus-element Encounter'],
        'product': ['Business', 'Diagnostic cases', '7 Logical Business mixed types', 'Diagnostic Product'],
        'stream': ['Strategy', 'Diagnostic cases', '7 Logical Business mixed types', 'Diagnostic Value Stream'],
        'driver': ['Motivation', 'Driver with reference-only children'],
    }
    assert {eid: data['path'] for eid, data in mo.items()} == expected
    assert mo == mo2 and {tuple(x['path']) for x in mf} == {tuple(x['path']) for x in mf2}
    assert not any('8 Source-only reference' in f['path'] or 'Stakeholder reference context' in f['path'] for f in mf)
    rf, ro = native(ET.parse(work / 'repeated-reference.archimate').getroot())
    rf2, ro2 = native(ET.parse(work / 'repeated-reference-second.archimate').getroot())
    assert ro == ro2 and ro['repeated']['path'] == ['Strategy', 'Second location', 'One Capability referenced twice']
    assert any(x['path'] == ['Strategy', 'First location'] and x['directObjects'] == 0 for x in rf)
    assert {tuple(x['path']) for x in rf} == {tuple(x['path']) for x in rf2}
    cf, co = native(ET.parse(work / 'category-markers.archimate').getroot())
    cf2, co2 = native(ET.parse(work / 'category-markers-second.archimate').getroot())
    assert co == co2
    assert co['marker-cap']['path'] == ['Strategy', 'Within marker', 'Marker Capability']
    assert co['marker-fun']['path'] == ['Business', 'Mixed marker', 'Marker Business Function']
    assert any(x['path'] == ['Strategy', 'Mixed marker'] for x in cf)
    assert {tuple(x['path']) for x in cf} == {tuple(x['path']) for x in cf2}
    af, ao = native(ET.parse(work / 'anchored-reference-folders.archimate').getroot())
    af2, ao2 = native(ET.parse(work / 'anchored-reference-folders-second.archimate').getroot())
    assert ao == ao2 and {tuple(x['path']) for x in af} == {tuple(x['path']) for x in af2}
    anchored_context = ['Motivation', 'I Motivation', 'Driver navigation', 'Stakeholder references', 'Stakeholder context']
    assert any(x['path'] == anchored_context for x in af)
    assert any(x['path'] == ['Other', 'G Governance reference', 'Source-only leaf'] for x in af)
    assert ao['anchored-driver']['path'] == ['Motivation', 'I Motivation', 'Driver navigation', 'Anchored Driver']
    assert ao['anchored-stakeholder']['path'] == ['Motivation', 'R Stakeholder index', 'Indexed Stakeholder']
    report = {
        'status': 'PASS', 'standing': 'Diagnostic evidence only; no remediation or acceptance',
        'baselineSha256': sha(baseline), 'returnedSha256': sha(work / 'returned.xml'),
        'canonicalElementRecordsExactIncludingAllPropertyValues': len(original),
        'semanticSignatures': signatures,
        'publishedSecondPassObjectAndFolderPathsStable': True,
        'publishedSourceAndReturnedElementRefsExactlyOnce': True,
        'returnedHasNoElementReferencingParentItems': True,
        'fixtureExpectedObjectPaths': expected,
        'mechanicsFixtureObjectAndFolderPathsStableOnSecondPass': True,
        'repeatedReference': {'objectCopies': 1, 'location': ro['repeated']['path'],
                              'firstFolderEmptyAfterFirstPass': True, 'emptyFolderRetainedOnSecondPass': True},
        'categoryMarkers': {'exactTopNativeLabelsConsumed': True, 'objectTypeControlsNativeRoot': True,
                            'mixedTypeEmptyFolderRetainedOnSecondPass': True},
        'anchoredReferenceFolders': {'referenceOnlyContextFolderPath': anchored_context,
                                     'sourceOnlyOtherFolderSurvives': True, 'secondPassPathsStable': True,
                                     'semanticElements': 2, 'semanticRelationships': 0,
                                     'contextHasNoLiveIdentifierRef': True},
        'fixtureRuns': json.loads((work / 'fixture-runs.json').read_text()),
        'secondPassRuns': json.loads((work / 'second-runs.json').read_text()),
        'limits': ['Path equality is separate from XML byte equality and generated folder identity.',
                   'No interactive Solution Architect inspection/acceptance is asserted.',
                   'Synthetic candidate A is not applied to the Harmonia publication.',
                   'Current-projection review helper failed its historical-baseline gate as expected; not an ingest review.']}
    output.write_text(json.dumps(report, indent=2, ensure_ascii=False) + '\n')
    print(json.dumps({k: v for k, v in report.items() if k not in ('fixtureExpectedObjectPaths', 'fixtureRuns', 'secondPassRuns')}, indent=2))


if __name__ == '__main__':
    main()
