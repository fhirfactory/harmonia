#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Publish the explicitly accepted RAD-1.1.0 amendment; never allocate identity.

Replay resource-owned reconciliation records through the shared adapter, then
change only model-level release/standing metadata. Historical captures, plans,
publications and machinery-validation evidence are read-only inputs.
"""
import argparse
import copy
import json
from pathlib import Path
import sys
from types import SimpleNamespace

from reconciliation import information as r
from architecture_release import load_release

g, m = r.g, r.metadata
AUTHORITY = m.RAD / 'release-1.1.0.json'
EVIDENCE = m.PUBLISHING / '1.1.0'
MODEL = m.PUBLISH / '1.1.0/Harmonia-Architecture-1.1.0.xml'
REPORT = EVIDENCE / 'information-architecture-publication.md'
GUIDE = EVIDENCE / 'archi-inspection-guide.md'
RELEASE = dict(version='1.1.0', previous_version='1.0.10')


def active():
    return load_release() == RELEASE


def pinned(spec):
    path = m.ROOT / spec['path']
    g.require(r.sha(path.read_bytes()) == spec['sha256'], 'Promotion input changed: ' + spec['path'])
    return path


def inputs():
    decision = m.read(AUTHORITY)
    g.require(decision['standing'] == 'ACCEPTED_RELEASE_PROMOTION' and
              decision['architecture_release'] == RELEASE, 'Unsupported release promotion')
    paths = {key: pinned(spec) for key, spec in decision['inputs'].items()}
    plan, source = m.read(paths['plan']), m.read(paths['accepted_source'])
    g.require(plan['release_authority'] == decision['starting_release'], 'Historical release lineage changed')
    r.validate_establishment(m.read(paths['establishment']), paths['accepted_source'].read_bytes(),
                             paths['plan'].read_bytes(), plan)
    g.require(source['authority'] == plan['authority'], 'Accepted source authority changed')
    for key in ('decision', 'review'):
        pinned(plan['authority'][key])
    models = {key: r.diagnostic.Exchange(pinned(spec)) for key, spec in plan['inputs'].items()}
    g.require(all(not model.issues for model in models.values()), 'Invalid reconciliation input')
    g.require(models['candidate'].sha256 == decision['inputs']['candidate']['sha256'], 'Candidate mismatch')
    groups, _ = r.validate_scope(models['baseline'], models['control'], models['candidate'], plan['scope'])
    accepted = {row['cr'] for key in ('elements', 'relationships', 'views') for row in groups[key].values()}
    g.require(accepted == set(decision['accepted_crs']) == {'CR-%03d' % n for n in range(1, 155)},
              'Promotion acceptance scope differs')
    g.require({row['cr'] for row in groups['held'].values()} == set(decision['held_crs']) ==
              {'CR-%03d' % n for n in range(155, 159)}, 'Held scope differs')
    return decision, plan, source, models, paths


def release_metadata(root):
    """Return a copy with the publication standing, without touching architecture."""
    root = copy.deepcopy(root)
    r.naming.set_property(root, root, 'Baseline / Architecture Release', RELEASE['version'])
    r.naming.set_property(root, root, r.naming.MARKER, None)
    definitions = root.find('a:propertyDefinitions', g.N)
    for definition in list(definitions):
        if definition.findtext('a:name', namespaces=g.N) == r.naming.MARKER:
            rid = definition.get('identifier')
            g.require(not root.xpath('.//a:property[@propertyDefinitionRef=$rid]', namespaces=g.N, rid=rid),
                      'Candidate standing unexpectedly used outside model')
            definitions.remove(definition)
    return root


def counts(root):
    from synchronise_baseline import counts as census
    return census(root)


def amend_metadata_index(index):
    """Carry forward historical resources and overlay accepted instance metadata."""
    import information_population as ia
    index = {**index, **ia.descriptors(ia.load())}
    _, _, source, _, _ = inputs()
    amended_ids = {row['canonicalId'] for row in source['elements']}
    index = {rid: item for rid, item in index.items() if item['record']['canonicalId'] not in amended_ids}
    for row in source['elements']:
        index[row['representationId']] = dict(record=row,
            expected={role: row[role] for role in ('displayName', 'friendlyName', 'canonicalName')},
            archimateType=row['archimateType'], harmoniaType=row.get('harmoniaType'))
    return index


def publish():
    g.require(active(), 'RAD-1.1.0 publication requires its explicit root release authority')
    decision, plan, source, models, paths = inputs()
    records, classification_sources = r.workspace_policy.load_records()
    replay = r.render(models['candidate'], source, plan['scope'], records)
    g.require(replay['reconciled.xml'] == paths['reconciled'].read_bytes(),
              'Reconciliation does not reproduce the accepted identity-bearing result')
    before = g.ET.fromstring(replay['reconciled.xml'])
    root = release_metadata(before)
    content = g.serialize(root)
    parsed = g.ET.fromstring(content)
    g.load_schema(g.BASE / 'schemas/archimate3_Diagram.xsd').assertValid(parsed)
    g.validate_identifiers(parsed)
    # Reversing only the two model properties and removed unused definition must
    # recover the exact reconciled content signature, including every View.
    restored = copy.deepcopy(parsed)
    for path in ('a:properties', 'a:propertyDefinitions'):
        current = restored.find(path, g.N)
        current.getparent().replace(current, copy.deepcopy(before.find(path, g.N)))
    g.require(g.signature(restored) == g.signature(before), 'Publication changed protected content')
    for path in ('a:views', "a:organizations/a:item[a:label='Views']"):
        g.require(g.ET.tostring(parsed.find(path, g.N)) ==
                  g.ET.tostring(models['candidate'].root.find(path, g.N)), 'Published authored Views changed')
    baseline, candidate = models['baseline'], models['candidate']
    identity = r.element_correlation.canonical_index(r.model_at(candidate, parsed))
    g.require(not identity['issues'] and all(c['valid'] for c in identity['claims'].values()),
              'Published identity population is incomplete')
    correlation = r.element_correlation.correlate(baseline, r.model_at(candidate, parsed))
    g.require(correlation['status'] == 'CORRELATED' and not correlation['deleted'], 'Canonical continuity lost')
    allocated = sorted((dict(cr=row['cr'], displayName=row['displayName'], canonicalId=row['canonicalId'],
                            representationId=row['representationId']) for row in source['elements']
                        if row['operation'] == 'add'), key=lambda row: row['cr'])
    evolved = [dict(cr=row['cr'], canonicalId=row['canonicalId'], previous=row['previous'],
                    representationId=row['representationId'], displayName=row['displayName'])
               for row in source['elements'] if row['operation'] == 'evolve']
    held = []
    for row in source['held']:
        rid = row['id']
        node = r.navigation.index(parsed, 'a:elements/a:element')[rid]
        g.require(g.signature(node) == g.signature(candidate.elements[rid]) and
                  r.review.authored_element(candidate, rid) == r.review.authored_element(baseline, rid),
                  'Held DataObject changed')
        g.require(r.review.native_type(node) == 'DataObject' and
                  not r.review.specialization(candidate, node)['claims'], 'Held classification inferred')
        held.append(dict(cr=row['cr'], representationId=rid, name=node.findtext('a:name', namespaces=g.N),
                         canonicalId=candidate.single(node, 'canonicalId'), status='On-Hold / Unresolved',
                         native_type='DataObject', content='UNCHANGED'))
    report = dict(status='PASS', architecture_release=RELEASE, before=counts(baseline.root), after=counts(parsed),
                  output_sha256=r.sha(content), accepted_crs=decision['accepted_crs'], held_unchanged=held,
                  new_governed_identities=allocated, canonical_identities=len(identity['claims']),
                  existing_canonical_identities_preserved=len(correlation['matches']),
                  identities_allocated_by_publication=0, rebindings_preserved=len(evolved),
                  checks=dict(offline_xsd='PASS', identifiers_references='PASS', canonical_continuity='PASS',
                              persisted_metadata_derivation='PASS', semantic_and_view_preservation='PASS',
                              held_dataobjects_unchanged='PASS', deterministic_workspace='PASS',
                              projection_independent_of_incoming_folders='PASS'),
                  unexpected_architectural_differences=[],
                  expected_representation_changes=['32 persisted canonicalId properties added since Run-002 candidate',
                      'Workspace projection identities use those canonical IDs instead of temporary representation identities',
                      'Model release becomes 1.1.0; candidate-standing property and unused definition removed'],
                  final_visual_inspection='PENDING_SOLUTION_ARCHITECT',
                  actual_archi_transport='See roundtrip/archi-transport-verification.json; separate from visual inspection')
    outputs = {MODEL: content, EVIDENCE / 'publication-validation-1.1.0.json': r.encoded(report),
               EVIDENCE / 'identity-provenance.json': r.encoded(dict(
                   new_governed_elements=allocated, evolved_elements=evolved,
                   original_rebindings=decision['inputs']['rebindings'],
                   allocated_during_reconciliation=32, allocated_during_publication=0)),
               **{EVIDENCE / name: replay[name] for name in
                  ('resolved-containment.json', 'workspace-projection.json', 'element-generation-proof.json')}}
    manifest = dict(architecture_release=RELEASE, authority=dict(path=str(AUTHORITY.relative_to(m.ROOT)),
                    sha256=r.sha(AUTHORITY.read_bytes())), inputs=decision['inputs'],
                    classification_sources=classification_sources,
                    machinery_sha256={str(path.relative_to(m.ROOT)): r.sha(path.read_bytes()) for path in sorted([
                        *g.BASE.joinpath('tools').rglob('*.py'), *g.BASE.joinpath('schemas').glob('*.xsd'),
                        m.ROOT / 'scripts/architecture_release.py'])},
                    outputs={str(path.relative_to(m.ROOT)): r.sha(data) for path, data in outputs.items()})
    outputs[EVIDENCE / 'publication-manifest.json'] = r.encoded(manifest)
    return outputs, SimpleNamespace(information_release=True), report


def render_reports():
    _, _, result = publish()
    report = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-1.1.0 Information Architecture publication

The Solution Architect authorised promotion of corrected Run-002, **CR-001–154**,
from RAD-1.0.10 into **RAD-1.1.0** on 2026-10-20. The governed
[release decision](../../../../markdown/governance/baseline/release-1.1.0.json)
pins the accepted resource capture, instructions, candidate, Archi export and
representation-rebinding provenance. Historical specimens remain unchanged.

The release incorporates 32 new Topics, 38 existing BusinessObject → Topic
evolutions, 12 BusinessObject name migrations, 59 Compositions, one Aggregation,
11 new Views and one revised View. All **1,394 elements, 1,137 relationships and
1,209 Views** are retained. The 1,362 established canonical identities remain;
the 32 new identities were already allocated once in the accepted resource
capture. Publication allocates **zero** identities. See
[identity provenance](identity-provenance.json) for every new ID and all 38 rebindings.

**CR-155–158 remain On-Hold / Unresolved and byte-content unchanged:** Context
Topic, Pragma Topic, PetasosParcel Topic and Pylai.Diadromí Topic remain native
DataObjects without inferred specialisation. No Class, Package,
ImplementationObject or other new metamodel construct is introduced.

Shared reconciliation, naming, containment and workspace projectors validate
persisted metadata and regenerate all Element/Relationship navigation. Existing
Views and their navigation retain the reviewed candidate's exact serialization.
The [generation proof](element-generation-proof.json) verifies absent/misleading
input folders; [publication validation](publication-validation-1.1.0.json)
records schema, reference, identity, semantic and presentation checks. Bounded
test results are in [validation](validation/test-results.json); actual final-model
Archi transport is in [roundtrip](roundtrip/archi-transport-verification.json).
Known transport serialization effects remain visible; the native publication
retains the reviewed presentation. Final human rendering inspection remains pending.

Expected differences from the reviewed candidate are the 32 persisted IDs,
their resulting workspace projection IDs, model release metadata and removal of
the candidate-standing marker. **No unexpected architectural differences.**
No documentation-quality sweep or workspace restructuring was performed.

Import [Harmonia-Architecture-1.1.0.xml](../../../exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml).
SHA-256: `{result['output_sha256']}`.
'''
    guide = f'''<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Solution Architect inspection — RAD-1.1.0

Import the complete Open Exchange model
`docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml`
into Archi 5.10, including organisation information and English (`en`) locale.
Expected SHA-256: `{result['output_sha256']}`.

Confirm release 1.1.0, 1,394 elements, 1,137 relationships and 1,209 Views.
Inspect `[Topic] Care/[Topic] Care` and the contained
`[Topic] Care/[Topic] Encounter/[Topic] Encounter` references, the other
containment hierarchies, and source-organised Relations. Inspect the InformationMap
and 11 Topic Views, their existing layout and Topic native DataObject specialisation.
The four unresolved legacy DataObjects retain their previous names and types.
Final Solution Architect inspection is a human handoff; CLI transport establishes
interchange behaviour only. No next architecture increment has begun.
'''
    return {REPORT: report.encode(), GUIDE: guide.encode()}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--check', action='store_true')
    parser.add_argument('--output-directory', type=Path)
    args = parser.parse_args()
    try:
        outputs, _, result = publish()
        outputs.update(render_reports())
        release_before = (m.ROOT / 'architecture-release.json').read_bytes()
        for path, content in outputs.items():
            if args.output_directory:
                base = args.output_directory.resolve()
                g.require(base.is_relative_to(Path('/tmp')), 'Independent reproduction must be under /tmp')
                path = base / path.relative_to(m.ROOT)
            if args.check:
                g.require(path.read_bytes() == content, 'Publication differs: ' + str(path))
            else:
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_bytes(content)
        g.require((m.ROOT / 'architecture-release.json').read_bytes() == release_before,
                  'Publication changed release authority')
        print(r.encoded(dict(status='PASS', architecture_release=RELEASE, counts=result['after'],
                            output_sha256=result['output_sha256'], identities_allocated=0)).decode(), end='')
        return 0
    except (OSError, ValueError, KeyError, g.ET.LxmlError) as exc:
        print('UNRESOLVED: ' + str(exc), file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
