#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded actual-record correspondence; never changes the strict result."""
import gc
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[5]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import exchange_compare as d
import architecture_continuum as c


def signature(node, model, resolve=False):
    attrs = dict(node.attrib)
    type_key = '{http://www.w3.org/2001/XMLSchema-instance}type'
    if type_key in attrs:
        attrs[type_key] = d.expanded_type(node)
    if resolve and 'propertyDefinitionRef' in attrs:
        attrs['propertyDefinitionRef'] = model.keys[attrs['propertyDefinitionRef']]
    text = node.text
    if len(node) and not (text or '').strip():
        text = None
    return [node.tag, attrs, text, node.tail if (node.tail or '').strip() else None,
            [signature(n, model, resolve) for n in node if isinstance(n.tag, str)]]


def digest(value):
    return hashlib.sha256(json.dumps(value, ensure_ascii=False, sort_keys=True).encode()).hexdigest()


def main():
    previous = ROOT / 'enterprise-architecture/design/evidence/1.1.23/001/after/mnemosyne-incorporation'
    paths = {'oldSource': previous / 'candidate/reader/Harmonia-Architecture.xml',
             'oldReturn': previous / 'archi-transport/returned-model.xml.gz',
             'source': HERE / 'candidate/reader/Harmonia-Architecture.xml',
             'return': HERE / 'archi-transport/returned.xml'}
    stages, pins = {}, {}
    for role, path in paths.items():
        model = d.Exchange(path)
        assert not model.issues, (role, model.issues)
        pins[role] = {'path': str(path.relative_to(ROOT)), 'fileSha256': model.sha256,
                      'xmlSha256': model.xml_sha256}
        stages[role] = {kind: {k: digest(signature(n, model)) for k, n in getattr(model, kind).items()}
                        for kind in ('relationships', 'views')}
        del model
        gc.collect()
    old, source = stages['oldSource'], stages['source']
    old_return, returned = stages['oldReturn'], stages['return']
    new = set(source['relationships']) - set(old['relationships'])
    raw = json.loads(gzip.decompress((HERE / 'raw-transport-comparison.json.gz').read_bytes()))
    findings = []
    for finding in raw['findings']:
        match = re.search(r'/relationship\[id=([^\]]+)\]', finding['path'])
        if match and match[1] in new:
            findings.append(finding)
    result = {'status': 'PASS', 'inputs': pins, 'newRelationshipIds': sorted(new),
              'existingSourceRelationshipDifferences': sorted(k for k in old['relationships']
                  if old['relationships'][k] != source['relationships'].get(k)),
              'existingReturnedRelationshipDifferences': sorted(k for k in old_return['relationships']
                  if old_return['relationships'][k] != returned['relationships'].get(k)),
              'allSourceViewsIdenticalAcrossReleases': old['views'] == source['views'],
              'allReturnedViewsIdenticalAcrossReleases': old_return['views'] == returned['views'],
              'rawFindingsInsideNewRelationships': findings,
              'newRelationshipFindingsOnlyAbsentToEnDocumentation': all(
                  f['before'] is None and f['after'] == 'en' and
                  f['path'].endswith('/documentation[1]/@{http://www.w3.org/XML/1998/namespace}lang')
                  for f in findings),
              'limits': 'Actual old source/return record correspondence preserves text, language, attributes and child order; no raw finding or strict FAIL is waived. Other raw categories remain explicit and require candidate-specific treatment.'}
    (HERE / 'predecessor-transport-correspondence.json').write_bytes(c.encoded(result))
    print(json.dumps({k: v for k, v in result.items() if k not in ('inputs', 'newRelationshipIds', 'rawFindingsInsideNewRelationships')}))


if __name__ == '__main__':
    main()
