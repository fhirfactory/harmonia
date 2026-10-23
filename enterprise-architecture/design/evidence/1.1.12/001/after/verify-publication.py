#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded 1.1.11 source, publication and preservation verification for HCRL 1.1.12-001.

Requires the accepted Git commit and installed maintained-tooling dependencies.
Uses the maintained projector, never a replacement XML generator.
"""
import argparse
import io
import json
from pathlib import Path
import subprocess
import sys
import tarfile
import tempfile


def verify(root):
    sys.path.insert(0, str(root / 'enterprise-architecture/tooling'))
    import architecture_continuum as c
    import continuum_navigation as nav
    import continuum_workbench as w
    from exchange_xml import load_schema

    def git(*args):
        return subprocess.check_output(['git', *args], cwd=root, timeout=60)

    baseline = git('rev-parse', 'ed8121a^{commit}').decode().strip()
    publication = c.ARCHIMATE_PUBLICATION / '1.1.11/Harmonia-Architecture.xml'
    content = (root / publication).read_bytes()
    protected = [c.CONTINUUM, c.DESIGN / 'projection-template.xml',
                 c.PUBLICATION / '1.1.11', c.RAD_PUBLICATION / '1.1.11']
    protected += [Path('enterprise-architecture/design') / kind / '1.1.11'
                  for kind in ('evidence', 'review', 'register')]
    # Compare every protected accepted file, including the generated root
    # Continuum manifest, against its exact committed bytes; detect extra files.
    tracked = git('ls-tree', '-r', '--name-only', baseline, '--', *map(str, protected)).decode().splitlines()
    for relative in tracked:
        c.require((root / relative).read_bytes() == git('show', baseline + ':' + relative),
                  'Accepted artefact changed: ' + relative)
    present = {str(p.relative_to(root)) for rel in protected
               for p in ((root / rel).rglob('*') if (root / rel).is_dir() else [root / rel])
               if p.is_file() and '__pycache__' not in p.parts and 'build' not in p.parts}
    c.require(present == set(tracked), 'Accepted file population changed: ' + str(present ^ set(tracked)))

    inputs = [c.CONTINUUM, c.CONFIG.parent, c.DESIGN, c.TOOLING,
              Path('architecture-release.json')]
    inputs += [Path(source['retainedAt']) for m, _, _ in c.load(root)[1].values()
               for source in m['provenance'] if source.get('retainedAt')]
    with tempfile.TemporaryDirectory(prefix='harmonia-1111-accepted-source-') as tmp:
        stage = Path(tmp)
        archive = git('archive', baseline, '--', *map(str, sorted(set(inputs))))
        with tarfile.open(fileobj=io.BytesIO(archive)) as source:
            for member in source:
                c.require(not Path(member.name).is_absolute() and '..' not in Path(member.name).parts,
                          'Unsafe archive member')
                if member.isfile():
                    target = stage / member.name
                    target.parent.mkdir(parents=True, exist_ok=True)
                    target.write_bytes(source.extractfile(member).read())
        authority = c.read_json(stage / 'architecture-release.json')
        c.require(authority == {'version': '1.1.11', 'previous_version': '1.1.10'},
                  'Wrong accepted source release')
        fingerprint = {str(p.relative_to(stage)): c.sha(p.read_bytes())
                       for rel in (c.CONTINUUM, c.CONFIG.parent, c.DESIGN, c.TOOLING)
                       for p in sorted((stage / rel).rglob('*')) if p.is_file()}
        fingerprint['architecture-release.json'] = c.sha((stage / 'architecture-release.json').read_bytes())
        generations = []
        for name in ('first.xml', 'repeat.xml'):
            command = [sys.executable, '-B', str(stage / c.TOOLING / 'architecture_continuum.py'),
                       '--root', str(stage), 'project', '--output', str(stage / name)]
            run = subprocess.run(command, cwd=stage, text=True, capture_output=True, timeout=90)
            c.require(run.returncode == 0, run.stdout[-2000:] + run.stderr[-2000:])
            generated = (stage / name).read_bytes()
            c.require(generated == content, 'Accepted-source projection differs from published XML')
            generations.append(c.sha(generated))
        # The current maintained projector must also reproduce the accepted bytes.
        current, validation = c.project(stage)
        c.require(current == content, 'Current projector changed accepted release projection')
        _, elements = c.load(stage)
        model, projected = w.model(root / publication)
        c.require(set(projected) == set(elements), 'Canonical identity population drift')
        for cid, (metadata, body, _) in elements.items():
            row = projected[cid]
            c.require(row['identifier'] == metadata['exchange']['identifier'], 'Representation identity drift')
            c.require(row['name'] == metadata['displayName'], 'Native Name drift')
            c.require(row['archimateType'] == metadata['archimateType'], 'Native type drift')
            c.require(row['documentation'] == body.split('## Architectural content\n', 1)[1].strip(), 'Content drift')
            for role in c.ROLES:
                expected = [metadata[role]] if metadata.get(role) is not None else None
                c.require(row['properties'].get(role) == expected, 'Canonical role drift: ' + role)
            c.require(row['properties']['Continuum / Semantic Standing'] == [metadata['standing']['semantic']], 'Standing drift')
            c.require(row['properties']['Continuum / Currentness'] == [metadata['standing']['currentness']], 'Currentness drift')
        template = c.secure_xml(stage / c.DESIGN / 'projection-template.xml')
        c.require(c.ET.tostring(model.find('a:views', c.N)) == c.ET.tostring(template.find('a:views', c.N)),
                  'Authored Views changed')
        edges = model.findall('a:relationships/a:relationship', c.N)
        expected_edges = {r['identifier']: r for m, _, _ in elements.values() for r in m['relationships']}
        c.require({r.get('identifier') for r in edges} == set(expected_edges), 'Semantic edge population drift')
        for edge in edges:
            row = expected_edges[edge.get('identifier')]
            for key in ('source', 'target'):
                c.require(edge.get(key) == row[key], 'Semantic endpoint drift')
            c.require(edge.get(c.XSI) == row['archimateType'], 'Semantic type drift')
        tree = nav.load(stage, elements)
        locations = nav.by_element(tree)
        references = nav.transport_metadata(tree)
        for cid, row in projected.items():
            props = row['properties']
            c.require(props['Continuum / Navigation Number'] == [locations[cid]['number']], 'Navigation number drift')
            c.require(props['Continuum / Navigation Path'] == [' / '.join(locations[cid]['path'])], 'Navigation path drift')
            c.require(json.loads(props['Continuum / Navigation References'][0]) == references[cid], 'Navigation references drift')
        load_schema(stage / c.SCHEMAS / 'archimate3_Diagram.xsd').assertValid(model)

    manifest_xml = root / c.PUBLICATION / '1.1.11/Harmonia-Architecture.xml'
    c.require(content == manifest_xml.read_bytes(), 'Accepted Manifest Exchange differs')
    previous_transport = c.read_json(root / 'enterprise-architecture/design/evidence/1.1.11/002/after/archi-transport.json')
    c.require(c.sha(content) == previous_transport['baselineSha256'], 'Accepted Archi-tested baseline differs')
    c.require(c.sha(content) != previous_transport['returnedXmlSha256'], 'Published XML selected a returned control')
    return {'status': 'PASS', 'baselineCommit': baseline, 'publication': str(publication),
            'filename': publication.name, 'xmlBytes': len(content), 'sha256': c.sha(content),
            'acceptedOriginalProjectorRuns': generations, 'currentProjectorMatchesAccepted': True,
            'acceptedManifestExchangeMatches': True, 'acceptedArchiBaselineMatches': True,
            'isAcceptedArchiReturnedControl': False, 'offlineXsd': 'PASS',
            'elements': len(projected), 'semanticRelationships': len(edges),
            'authoredViews': len(model.findall('a:views/a:diagrams/a:view', c.N)),
            'canonicalIdsNamesTypesContentStanding': 'PASS', 'relationshipIdsEndpointsTypes': 'PASS',
            'authoredViewSubtreeIncludingGeometry': 'PASS',
            'navigationCoordinatesPathsAndReferences': len(projected),
            'sourceInputs': fingerprint, 'acceptedProtectedFiles': len(tracked),
            'acceptedArchitectureNavigationPublicationsEvidenceHcrlsUnchanged': True,
            'projectionValidation': {k: v for k, v in validation.items() if k not in ('counts', 'unresolvedProperties')},
            'limits': 'Schema and exact source reproduction do not by themselves assert new Archi execution or Solution Architect acceptance.'}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--root', type=Path, default=Path(__file__).resolve().parents[6])
    parser.add_argument('--report', type=Path, required=True)
    args = parser.parse_args()
    report = verify(args.root.resolve())
    args.report.write_text(json.dumps(report, indent=2, ensure_ascii=False) + '\n')
    print(json.dumps({key: value for key, value in report.items()
                      if key not in ('sourceInputs', 'projectionValidation')}, indent=2))
