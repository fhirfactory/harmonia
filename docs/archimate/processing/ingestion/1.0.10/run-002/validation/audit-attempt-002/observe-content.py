import collections
import copy
import gzip
import hashlib
import json
from pathlib import Path
import sys

sys.path.insert(0, 'docs/archimate/framework/tools')
import change_review as cr
import containment
import diagnose_roundtrip as d
import element_correlation
import generate_views as g
import information_naming

ROOT = Path.cwd()
RUN = Path('docs/archimate/processing/ingestion/1.0.10/run-002')
DET = RUN / 'detection'
MACH = Path('docs/archimate/processing/machinery-validation/information-architecture/run-002')
fixture = json.loads(Path('docs/archimate/framework/tests/fixtures/roundtrip/information-change-review.json').read_bytes())
before = d.Exchange(Path(fixture['baseline']['path']))
control = d.Exchange(Path(fixture['control']['path']))
original = d.Exchange(Path(fixture['incoming']['path']))
candidate = d.Exchange(MACH / 'candidate.xml')
after = d.Exchange(RUN / 'ArchiMate-Architecture.xml')
sha = lambda p: hashlib.sha256(p.read_bytes()).hexdigest()

def save(name, data):
    if '--check' in sys.argv:
        assert json.loads((DET / name).read_bytes()) == data, name
        return
    with (DET / name).open('x') as stream:
        json.dump(data, stream, ensure_ascii=False, indent=2, sort_keys=True)
        stream.write('\n')

def label(model, rid):
    return model.elements[rid].findtext('a:name', namespaces=g.N)

def record(model, rid, table='elements'):
    node = getattr(model, table)[rid]
    return dict(identifier=rid, line=node.sourceline, native_type=cr.native_type(node),
                name=node.findtext('a:name', namespaces=g.N),
                canonical_id=model.single(node, 'canonicalId'),
                documentation=cr.text_values(node, 'documentation'),
                specialization=cr.specialization(model, node),
                source=node.get('source'), target=node.get('target'))

def relation(model, rid):
    node = model.relationships[rid]
    result = record(model, rid, 'relationships')
    result.update(source_name=label(model, node.get('source')), target_name=label(model, node.get('target')))
    return result

def relation_content(model, rid):
    node = model.relationships[rid]
    props = collections.defaultdict(list)
    for prop in node.findall('a:properties/a:property', g.N):
        props[model.keys[prop.get('propertyDefinitionRef')]].append(cr.signature(prop, model))
    return dict(attributes=dict(node.attrib), properties=dict(props),
                residual=[cr.signature(child, model) for child in node if child.tag != g.tag('properties')])

def nav_inventory(model):
    folders, ordered, attrs, languages = collections.Counter(), {}, collections.Counter(), collections.Counter()
    def walk(parent, path=()):
        tokens = []
        for node in parent.findall('a:item', g.N):
            labels = tuple(x.text or '' for x in node.findall('a:label', g.N))
            current = path + labels
            rid = node.get('identifierRef')
            tokens.append(('reference', rid, labels) if rid else ('folder', labels))
            if not rid:
                folders[current] += 1
            attrs.update(node.attrib.keys())
            for lab in node.findall('a:label', g.N):
                languages.update(lab.attrib.keys())
            walk(node, current)
        # A reference can share the containing folder's path. Only folders have children.
        if tokens:
            assert path not in ordered
            ordered[path] = tokens
    walk(model.root.find('a:organizations', g.N))
    return folders, ordered, attrs, languages

resolved = containment.resolve(after.root)
effective = resolved.evidence()
names = information_naming.propose(after, resolved)
save('information-naming.json', names)
assert not names['identity_issues'] and not names['input_issues']
assert not any(row['changes'] for row in names['records'])
oldnav, newnav = cr.navigation(candidate), cr.navigation(after)
assert oldnav == newnav
left, right = nav_inventory(candidate), nav_inventory(after)
assert left[0] == right[0]
assert set(left[1]) == set(right[1])
order_changes = [dict(path=list(path), before=left[1][path], after=right[1][path])
                 for path in sorted(left[1]) if left[1][path] != right[1][path]]
assert all(collections.Counter(tuple(map(str, t)) for t in row['before']) ==
           collections.Counter(tuple(map(str, t)) for t in row['after']) for row in order_changes)

# Assert actual serialized parent/self and contained child-folder/reference shape.
locations = {}
for item in after.root.findall('.//a:organizations//a:item', g.N):
    rid = item.get('identifierRef')
    if rid:
        locations[rid] = item
participants = {edge['parent'] for edge in effective['selected_relationships'].values()} | {
    edge['child'] for edge in effective['selected_relationships'].values()}
for rid in participants:
    folder = locations[rid].getparent()
    assert folder.findtext('a:label', namespaces=g.N) == label(after, rid)
    assert folder.xpath('a:item/@identifierRef', namespaces=g.N) == [rid]
care, = after.root.xpath("a:organizations/a:item[a:label='Application']/a:item[a:label='InformationMap']/a:item[a:label='[Topic] Care']", namespaces=g.N)
encounter, = care.xpath("a:item[a:label='[Topic] Encounter']", namespaces=g.N)
assert len(care.xpath('a:item/@identifierRef', namespaces=g.N)) == 1
assert encounter.xpath('a:item/@identifierRef', namespaces=g.N) == ['id-bcfd7137c33f4255ad3780826a64b4c5']
care_xml = g.ET.tostring(care, encoding='UTF-8', pretty_print=True, xml_declaration=True)
if '--check' in sys.argv:
    assert (DET/'export-care-context.xml').read_bytes() == care_xml
else:
    with (DET/'export-care-context.xml').open('xb') as stream:
        stream.write(care_xml)

view_a, view_b = candidate.root.find('a:views', g.N), after.root.find('a:views', g.N)
assert cr.signature(view_a, candidate) == cr.signature(view_b, after)
assert all(relation_content(candidate,rid)==relation_content(after,rid) for rid in candidate.relationships)
assert candidate.elements.keys()==after.elements.keys()
assert candidate.relationships.keys()==after.relationships.keys()
assert candidate.views.keys()==after.views.keys()

corr = element_correlation.correlate(before,after)
matches = {r['incoming_id']:r['baseline_id'] for r in corr['matches']}
evolved = [dict(baseline=record(before,m['baseline_id']),incoming=record(after,m['incoming_id']))
           for m in corr['matches'] if cr.native_type(before.elements[m['baseline_id']]) != cr.native_type(after.elements[m['incoming_id']])]
additions = [record(after,rid) for rid in corr['new']]
renames = [dict(baseline=record(before,old),incoming=record(after,new)) for new,old in matches.items()
           if cr.native_type(before.elements[old])==cr.native_type(after.elements[new]) and label(before,old)!=label(after,new)]
edges = [relation(after,rid) for rid in sorted(after.relationships.keys()-before.relationships.keys())]
retained_changed = [rid for rid in sorted(before.relationships.keys()&after.relationships.keys())
                    if relation_content(control,rid)!=relation_content(after,rid)]
assert retained_changed == []
newviews = [dict(identifier=rid,line=after.views[rid].sourceline,name=after.views[rid].findtext('a:name',namespaces=g.N),
                elements=sorted(set(after.views[rid].xpath('.//a:node/@elementRef',namespaces=g.N))),
                nodes=len(after.views[rid].findall('.//a:node',g.N)),connections=len(after.views[rid].findall('.//a:connection',g.N)))
            for rid in sorted(after.views.keys()-before.views.keys())]
changedviews = [dict(identifier=rid,line=after.views[rid].sourceline,name=after.views[rid].findtext('a:name',namespaces=g.N),
                     baseline_nodes=len(before.views[rid].findall('.//a:node',g.N)),incoming_nodes=len(after.views[rid].findall('.//a:node',g.N)),
                     elements=sorted(set(after.views[rid].xpath('.//a:node/@elementRef',namespaces=g.N))))
                for rid in sorted(after.views.keys()&before.views.keys())
                if cr.signature(control.views[rid],control)!=cr.signature(after.views[rid],after)]
uncertain = [dict(incoming=record(after,r['representation_id']),unresolved=r['unresolved']) for r in names['records'] if r['unresolved']]
inventory=dict(new_elements=additions,evolved_elements=evolved,retained_element_name_migrations=renames,
               new_relationships=edges,changed_retained_relationships_after_control=retained_changed,
               new_views=newviews,changed_retained_views_after_control=changedviews,
               unchanged_unclassified_dataobjects=uncertain,representation_rebindings=[m for m in corr['matches'] if m['representation_identity']=='REBOUND'])
save('architectural-inventory.json',inventory)

manifest=json.loads((MACH/'manifest.json').read_bytes())
assert sha(MACH/'candidate.xml') == manifest['files_sha256']['candidate.xml']
assert all(sha(ROOT/path)==value for path,value in manifest['frozen_files_before'].items())
assert all(sha(MACH/path)==value for path,value in manifest['files_sha256'].items())
assert all(sha(ROOT/path)==value for path,value in manifest['source_sha256'].items())
raw=json.loads(gzip.decompress((DET/'candidate-to-export.json.gz').read_bytes()))['evidence']
summary=dict(status='PASS_FOR_OBSERVED_SEMANTIC_AND_VIEW_CONTENT',whole_xml_equivalence='NOT_ESTABLISHED',
             candidate_sha256=candidate.sha256,incoming_sha256=after.sha256,
             input_issues=dict(published=before.issues,control=control.issues,original=original.issues,candidate=candidate.issues,incoming=after.issues),
             candidate_to_export_population=raw['architectural_review']['population'],
             exact_properties=raw['contract_properties'],
             relationships_preserved=len(after.relationships),views_preserved=len(after.views),
             views_content_sha256=g.digest(cr.signature(view_b, after)),
             views_comparison='EXACT_CONTENT_WITH_PROPERTY_REFERENCES_RESOLVED_BY_DEFINITION_KEY',
             all_navigation_reference_paths='EXACT_MATCH',folder_path_multiset='EXACT_MATCH',
             navigation_folder_count=sum(right[0].values()),navigation_reference_count=len(newnav),
             navigation_item_attributes=dict(before=left[2],after=right[2]),
             navigation_label_attributes=dict(before=left[3],after=right[3]),
             navigation_sibling_order_changes=len(order_changes),
             containment=dict(selected=len(effective['selected_relationships']),
               mechanisms=dict(collections.Counter(v['mechanism'] for v in effective['selected_relationships'].values())),
               excluded=len(effective['excluded_relationships']),conditional=len(effective['conditional_proposals']),
               findings=effective['findings'],participants_with_own_context=len(participants),care_encounter='PASS'),
             information_naming=names['summary'],
             inherited_proposal_counts={k:len(v) for k,v in inventory.items()},
             historical_run_integrity=dict(outputs=len(manifest['files_sha256']),sources=len(manifest['source_sha256']),frozen=len(manifest['frozen_files_before'])),
             residual_classifications=raw['classifications'],
             residual_property_definition_language_loss=dict(key='Machinery validation / Candidate standing',before='en',after=None),
             authenticated_archi_execution='UNRESOLVED_NO_VERSION_OPERATOR_OR_EXPORT_PROFILE_ATTESTATION',
             accepted_changes=0,identities_allocated=0,release_created=False)
save('navigation-order-differences.json',order_changes)
save('review-audit.json',summary)
print(json.dumps(summary,ensure_ascii=False,indent=2)[:10500])
