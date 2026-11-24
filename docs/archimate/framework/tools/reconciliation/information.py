#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Apply an agent-reviewed information scope; persist identities before replay.

The plan records the agent's contract review of human instructions. It is not a
prose-to-authority interpreter. Capture is explicit and refuses an existing source;
replay and --check never allocate identities or publish a RAD release.
"""
import argparse
import collections
import copy
import hashlib
import json
from pathlib import Path
import sys

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import change_review as review
import containment
import diagnose_roundtrip as diagnostic
import element_correlation
import generate_views as g
import governed_metadata as metadata
import information_naming
import information_naming_run as naming
import run_evidence
import workspace_navigation as navigation
import workspace_policy


def encoded(value):
    return (json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + '\n').encode()


def sha(data):
    return hashlib.sha256(data).hexdigest()


def model_at(model, root):
    result = copy.copy(model)
    result.root = root
    for attr, path in (('elements', 'a:elements/a:element'),
                       ('relationships', 'a:relationships/a:relationship'),
                       ('views', 'a:views/a:diagrams/a:view')):
        setattr(result, attr, navigation.index(root, path))
    return result


def authored(model, rid):
    row = review.authored_element(model, rid)
    row['properties'] = {key: value for key, value in row['properties'].items() if key not in metadata.ROLES}
    return row


def relationship_content(model, rid):
    node = model.relationships[rid]
    props = collections.defaultdict(list)
    for prop in node.findall('a:properties/a:property', g.N):
        props[model.keys[prop.get('propertyDefinitionRef')]].append(review.signature(prop, model))
    return dict(attributes=dict(node.attrib), properties=dict(props),
                residual=[review.signature(child, model) for child in node if child.tag != g.tag('properties')])


def scope_index(scope):
    groups = {}
    crs = []
    for key in ('elements', 'relationships', 'views', 'held'):
        rows = scope[key]
        groups[key] = {row['id']: row for row in rows}
        g.require(len(groups[key]) == len(rows), 'Duplicate scope identity: ' + key)
        crs.extend(row['cr'] for row in rows)
    g.require(len(crs) == len(set(crs)), 'Duplicate CR scope')
    g.require(not groups['elements'].keys() & groups['held'].keys(), 'Held element included in mutation scope')
    for row in groups['elements'].values():
        g.require(row.get('operation') in ('add', 'evolve', 'rename'), 'Unsupported element operation')
        g.require(row['operation'] == 'add' or row.get('baseline_id'), 'Missing amendment baseline binding')
    return groups


def validate_scope(baseline, control, submitted, scope):
    """Prove exhaustive semantic/View coverage; navigation/derived claims differ.

    The actual publication control accounts for its own transport differences.
    Missing/malformed identity, unaccepted changes and unresolved dependent naming
    stop before allocation. No folder or View supplies architectural ancestry.
    """
    groups = scope_index(scope)
    correlation = element_correlation.correlate(baseline, submitted)
    g.require(correlation['status'] == 'CORRELATED', 'Unresolved canonical correlation')
    g.require(not correlation['deleted'], 'Deletion is outside information reconciliation scope')
    additions = {rid for rid, row in groups['elements'].items() if row['operation'] == 'add'}
    g.require(set(correlation['new']) == additions, 'Unaccepted/missing added information element')
    matches = {row['incoming_id']: row['baseline_id'] for row in correlation['matches']}
    control_correlation = element_correlation.correlate(baseline, control)
    g.require(control_correlation['status'] == 'CORRELATED' and not control_correlation['new'] and
              not control_correlation['deleted'], 'Control is not this complete baseline')
    controls = {row['baseline_id']: row['incoming_id'] for row in control_correlation['matches']}
    for rid, prior in matches.items():
        if rid in groups['elements']:
            row = groups['elements'][rid]
            g.require(row['operation'] in ('evolve', 'rename') and row['baseline_id'] == prior,
                      'Unestablished amendment binding: ' + rid)
        else:
            g.require(authored(control, controls[prior]) == authored(submitted, rid),
                      'Unaccepted authored element change: ' + rid)
    for rid, row in groups['elements'].items():
        g.require(rid in submitted.elements and review.native_type(submitted.elements[rid]) in
                  information_naming.INFORMATION_TYPES, 'Scope contains unsupported information type')
        g.require(row['operation'] == 'add' or rid in matches, 'Unestablished existing canonical instance')
    g.require(baseline.relationships.keys() <= submitted.relationships.keys(), 'Unaccepted relationship deletion')
    g.require(submitted.relationships.keys() - baseline.relationships.keys() == groups['relationships'].keys(),
              'Unaccepted/missing semantic relationship')
    for rid in baseline.relationships:
        g.require(relationship_content(control, rid) == relationship_content(submitted, rid),
                  'Unaccepted retained relationship mutation: ' + rid)
    g.require(baseline.views.keys() <= submitted.views.keys(), 'Unaccepted View deletion')
    for rid, node in submitted.views.items():
        if rid not in groups['views']:
            g.require(rid in control.views and review.signature(control.views[rid], control) ==
                      review.signature(node, submitted), 'Unaccepted View change: ' + rid)
    g.require(groups['views'].keys() <= submitted.views.keys(), 'Missing approved View')
    g.require(groups['held'].keys() <= submitted.elements.keys(), 'Missing held instance')
    resolved = containment.resolve(submitted.root, accepted=groups['relationships'])
    g.require(not resolved.findings(), 'Unresolved effective containment')
    proposal = information_naming.propose(submitted, resolved)
    rows = {row['representation_id']: row for row in proposal['records']}
    g.require(not proposal['identity_issues'], 'Invalid identity claims')
    for rid in groups['elements']:
        g.require(rid in rows and not rows[rid]['unresolved'], 'Unresolved approved naming: ' + rid)
    return groups, proposal


def capture(baseline, control, submitted, scope, authority, allocator=None):
    """Create resource-owned records; call only for an explicitly new capture."""
    groups, proposal = validate_scope(baseline, control, submitted, scope)
    canonical = element_correlation.canonical_index(submitted)
    g.require(not canonical['issues'], 'Invalid canonical claims before allocation')
    missing = {rid for rid, claim in canonical['claims'].items() if not claim['present']}
    approved_new = {rid for rid, row in groups['elements'].items() if row['operation'] == 'add'}
    g.require(missing == approved_new, 'Allocation scope differs from exactly approved new resources')
    index = {rid: dict(record=({'canonicalId': claim['canonicalId']} if claim['present'] else {}), expected={})
             for rid, claim in canonical['claims'].items()}
    allocated = metadata.allocate_missing(index, **({'allocator': allocator} if allocator else {}))
    root = copy.deepcopy(submitted.root)
    nodes = navigation.index(root, 'a:elements/a:element')
    proposals = {row['representation_id']: row for row in proposal['records']}
    records = []
    for rid, instruction in sorted(groups['elements'].items()):
        fields = proposals[rid]['proposed']
        node = nodes[rid]
        naming.set_property(root, node, 'canonicalId', index[rid]['record']['canonicalId'])
        for key, value in fields.items():
            naming.set_property(root, node, key, value)
        node.find('a:name', g.N).text = fields['displayName']
        record = dict(key=index[rid]['record']['canonicalId'], canonicalId=index[rid]['record']['canonicalId'],
                      representationId=rid, cr=instruction['cr'], operation=instruction['operation'],
                      record_state='current', **fields,
                      xml=g.ET.tostring(node, encoding='unicode', with_tail=False),
                      provenance=dict(input_path=str(submitted.path), input_sha256=submitted.xml_sha256,
                                      input_line=submitted.elements[rid].sourceline))
        if instruction['operation'] != 'add':
            old = baseline.elements[instruction['baseline_id']]
            record['previous'] = dict(representationId=instruction['baseline_id'],
                canonicalId=baseline.single(old, 'canonicalId'), archimateType=review.native_type(old),
                **{key: baseline.single(old,key) for key in ('displayName','friendlyName','canonicalName')})
        records.append(record)
    def retained(table, rows):
        return [dict(representationId=rid, cr=row['cr'], record_state='current',
                     xml=g.ET.tostring(getattr(submitted, table)[rid], encoding='unicode', with_tail=False))
                for rid,row in sorted(rows.items())]
    return dict(schema_version=1, authority=authority,
                standing='ACCEPTED_RECONCILIATION_UNPUBLISHED',
                allocation=dict(count=allocated, mechanism='UUIDv4 via governed_metadata.allocate_missing; persisted once in element records; replay allocates zero'),
                elements=records, relationships=retained('relationships', groups['relationships']),
                views=retained('views', groups['views']), held=list(groups['held'].values()),
                property_definitions_sha256=g.digest(review.signature(submitted.root.find('a:propertyDefinitions',g.N),submitted)))


def render(submitted, source, scope, collection_records, *, prove=True):
    """Pure replay from accepted resource fields; never invoke the allocator."""
    groups = scope_index(scope)
    g.require(source['schema_version'] == 1 and source['standing'] == 'ACCEPTED_RECONCILIATION_UNPUBLISHED',
              'Unsupported accepted source schema/standing')
    g.require(source['property_definitions_sha256'] == g.digest(review.signature(
              submitted.root.find('a:propertyDefinitions', g.N), submitted)), 'Property definition binding drift')
    g.require(source['held'] == list(groups['held'].values()), 'Held source scope drift')
    for key in ('elements', 'relationships', 'views'):
        g.require(len(source[key]) == len(groups[key]), 'Duplicate/missing accepted source: ' + key)
    g.require({r['representationId'] for r in source['elements']} == groups['elements'].keys() and
              {r['representationId'] for r in source['relationships']} == groups['relationships'].keys() and
              {r['representationId'] for r in source['views']} == groups['views'].keys(), 'Accepted source scope drift')
    root = copy.deepcopy(submitted.root)
    elements = navigation.index(root,'a:elements/a:element')
    for row in source['elements']:
        rid = row['representationId']
        g.require(row['cr'] == groups['elements'][rid]['cr'], 'Source CR binding drift')
        g.require(row['operation'] == groups['elements'][rid]['operation'], 'Source operation drift')
        node = g.ET.fromstring(row['xml'].encode(), parser=g.ET.XMLParser(resolve_entities=False, no_network=True))
        g.require(node.get('identifier') == rid, 'Source XML identity drift')
        old = elements[rid]
        node.tail = old.tail
        old.getparent().replace(old,node)
    current = model_at(submitted,root)
    identities = element_correlation.canonical_index(current)
    g.require(not identities['issues'] and all(c['valid'] for c in identities['claims'].values()), 'Missing/invalid reconciled identity')
    for rid, claim in element_correlation.canonical_index(submitted)['claims'].items():
        if claim['present']:
            g.require(identities['claims'][rid]['canonicalId'] == claim['canonicalId'], 'Established canonical identity changed')
    approved_new = {rid for rid, row in groups['elements'].items() if row['operation'] == 'add'}
    g.require(source['allocation']['count'] == len(approved_new), 'Persisted allocation count drift')
    resolved = containment.resolve(root, accepted=groups['relationships'])
    g.require(not resolved.findings(), 'Reconciled containment finding')
    proposed = {row['representation_id']:row for row in information_naming.propose(current,resolved)['records']}
    for row in source['elements']:
        rid = row['representationId']
        g.require(authored(current,rid) == authored(submitted,rid), 'Authored information semantics changed')
        g.require(row['canonicalId'] == current.single(current.elements[rid],'canonicalId'), 'Resource/Exchange identity mismatch')
        g.require(not proposed[rid]['unresolved'], 'Unresolved persisted naming')
        for key,value in proposed[rid]['proposed'].items():
            g.require(row.get(key) == value, 'Persisted derived metadata drift: ' + rid + '/' + key)
            claims = current.claims(current.elements[rid],key)
            g.require((not claims) if value is None else current.single(current.elements[rid],key) == value,
                      'Resource/Exchange derived metadata mismatch: ' + rid + '/' + key)
        g.require(row['displayName'] == current.elements[rid].findtext('a:name',namespaces=g.N), 'Name/displayName mismatch')
    for table,key in (('relationships','relationships'),('views','views')):
        for row in source[key]:
            rid=row['representationId']
            captured=g.ET.fromstring(row['xml'].encode(),parser=g.ET.XMLParser(resolve_entities=False,no_network=True))
            g.require(row['cr']==groups[key][rid]['cr'] and review.signature(captured,current)==
                      review.signature(getattr(current,table)[rid],current), 'Accepted authored capture drift: '+rid)
    for rid in groups['held']:
        g.require(g.signature(current.elements[rid]) == g.signature(submitted.elements[rid]), 'Held element mutated')
    # Reverse exactly the approved element amendments; every other semantic and
    # presentation byte-content signature must remain the reviewed input's.
    restored=copy.deepcopy(root)
    restored_elements=navigation.index(restored,'a:elements/a:element')
    for rid in groups['elements']:
        old=restored_elements[rid]
        old.getparent().replace(old,copy.deepcopy(submitted.elements[rid]))
    g.require(g.signature(restored)==g.signature(submitted.root), 'Mutation outside accepted element scope')
    naming.set_property(root,root,naming.MARKER,source['authority']['review_identity']+': RECONCILED ACCEPTED SCOPE; UNPUBLISHED; NOT A RAD RELEASE')
    current=model_at(submitted,root)
    placements=workspace_policy.placements(current,resolved,collection_records)
    result,plan=navigation.project(current,root,resolved=resolved,placements=placements)
    content=g.serialize(result)
    parsed=g.ET.fromstring(content,parser=g.ET.XMLParser(resolve_entities=False,no_network=True))
    g.load_schema(g.BASE/'schemas/archimate3_Diagram.xsd').assertValid(parsed)
    g.validate_identifiers(parsed)
    checks=navigation.verify(current,root,parsed,plan,resolved=resolved,placements=placements)
    proof=navigation.prove_generation(current,root,parsed,placements,resolved) if prove else None
    for path in ('a:views', "a:organizations/a:item[a:label='Views']"):
        left=submitted.root.xpath(path,namespaces=g.N);right=parsed.xpath(path,namespaces=g.N)
        g.require(len(left)==len(right)==1 and g.signature(left[0])==g.signature(right[0]), 'Authored Views/navigation changed')
        g.require(g.ET.tostring(left[0])==g.ET.tostring(right[0]),'Serialized Views/navigation changed')
    report=dict(status='PASS',standing=source['standing'],identities=len(identities['claims']),
                new_persisted_identities=source['allocation']['count'],identities_allocated_by_replay=0,
                elements_reconciled=len(source['elements']),relationships_preserved=len(current.relationships),
                accepted_relationships=len(source['relationships']),views_preserved=len(current.views),
                accepted_view_changes=len(source['views']),held_unchanged=len(groups['held']),
                checks=checks,semantic_and_view_preservation='PASS',view_navigation='EXACT_SERIALIZED_MATCH',
                deterministic_projection='PASS',release_created=False,model_sha256=sha(content))
    return {'reconciled.xml':content,'validation.json':encoded(report),'workspace-projection.json':encoded(plan),
            'resolved-containment.json':encoded(resolved.evidence()),'element-generation-proof.json':encoded(proof)}


def load_plan(path):
    plan=metadata.read(path)
    root=metadata.ROOT
    models={}
    for key,spec in plan['inputs'].items():
        source=root/spec['path']
        g.require(sha(source.read_bytes())==spec['sha256'],'Pinned input changed: '+key)
        models[key]=diagnostic.Exchange(source)
        g.require(not models[key].issues,'Invalid input: '+key)
    for key in ('decision','review'):
        spec=plan['authority'][key]
        g.require(sha((root/spec['path']).read_bytes())==spec['sha256'],'Instruction/decision drift: '+key)
    decision=metadata.read(root/plan['authority']['decision']['path'])
    groups=scope_index(plan['scope'])
    accepted={row['cr'] for key in ('elements','relationships','views') for row in groups[key].values()}
    g.require(accepted==set(decision['accepted_crs']),'Plan exceeds recorded acceptance')
    text=(root/plan['authority']['review']['path']).read_text()
    for cid in accepted:
        body=text.split('## '+cid+' — ',1)[1].split('\n## ',1)[0]
        g.require('**Disposition:** Accept' in body and '**Instruction revision:** 2' in body,
                  'Missing decision-linked issued instruction: '+cid)
    g.require(metadata.read(root/'architecture-release.json')==plan['release_authority'],'Release authority changed')
    validate_scope(models['baseline'],models['control'],models['candidate'],plan['scope'])
    return plan,models


def validate_establishment(manifest, source_bytes, plan_bytes, plan):
    """Accepted source history forbids silently changing newly established IDs."""
    g.require(manifest['source_path'] == plan['source_path'] and
              manifest['source_sha256'] == sha(source_bytes), 'Established accepted source changed')
    g.require(manifest['plan_sha256'] == sha(plan_bytes) and manifest['instructions'] == plan['authority'] and
              manifest['release_authority'] == plan['release_authority'], 'Establishment authority drift')


def main(argv=None):
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--plan',type=Path,required=True)
    parser.add_argument('--source',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    parser.add_argument('--established-manifest', type=Path,
                        help='Original source-manifest.json for replay to a new directory; --check defaults to its output')
    mode=parser.add_mutually_exclusive_group(required=True)
    mode.add_argument('--capture',action='store_true')
    mode.add_argument('--check',action='store_true')
    mode.add_argument('--replay',action='store_true')
    args=parser.parse_args(argv)
    try:
        plan,models=load_plan(args.plan)
        g.require(args.source.resolve().is_relative_to(metadata.RAD.resolve()),'Resource capture must be in governed RADS')
        g.require(args.source.resolve()==(metadata.ROOT/plan['source_path']).resolve(),'Unexpected resource target')
        g.require(args.output.resolve().is_relative_to((metadata.ROOT/'docs/archimate/processing/ingestion').resolve()),
                  'Reconciliation outputs must remain in ingestion processing, not publication')
        if args.capture:
            g.require(not args.source.exists() and not args.output.exists(),'Capture/output already exists; replay persisted identities')
            data=capture(models['baseline'],models['control'],models['candidate'],plan['scope'],plan['authority'])
            # Preflight the complete output before persisting resource identities.
            records,_=workspace_policy.load_records()
            render(models['candidate'],data,plan['scope'],records)
            with args.source.open('xb') as stream:stream.write(encoded(data))
        source_bytes = args.source.read_bytes()
        if not args.capture:
            history = args.established_manifest or args.output / 'source-manifest.json'
            validate_establishment(metadata.read(history), source_bytes, args.plan.read_bytes(), plan)
        data=json.loads(source_bytes)
        g.require(data['authority']==plan['authority'],'Accepted source decision binding changed')
        records,_=workspace_policy.load_records()
        outputs=render(models['candidate'],data,plan['scope'],records)
        outputs['source-manifest.json']=encoded(dict(source_path=plan['source_path'],source_sha256=sha(args.source.read_bytes()),
            plan_sha256=sha(args.plan.read_bytes()),instructions=plan['authority'],release_authority=plan['release_authority']))
        run_evidence.write(args.output,outputs,check=args.check)
        print(encoded(json.loads(outputs['validation.json'])).decode(),end='')
        return 0
    except (OSError,ValueError,KeyError,IndexError,g.ET.LxmlError) as exc:
        print('UNRESOLVED: '+str(exc),file=sys.stderr)
        return 1


if __name__=='__main__':
    sys.exit(main())
