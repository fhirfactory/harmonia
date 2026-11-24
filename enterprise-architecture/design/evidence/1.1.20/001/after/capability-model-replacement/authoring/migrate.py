#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""One authorised migration. Normal publication never imports this adapter.

New identities are allocated by the maintained create operation in a sparse,
portable allocation workspace, persisted immediately, then assembled with the
entire preserved baseline. Full validation precedes canonical application.
"""
import copy
import os
import shutil
import tempfile
from investigate import *
from plan import AUTHORITY, HCRL, prepare, definitions
from governed_naming import qualified_names

BEFORE = BASE / 'before/capability-model-replacement'
CATALOGUE = FR / 'Harmonia_1.1.20_Candidate_017_Pass3_014_FR_Reconciled_Catalogue.md'
LEDGER = OUT / 'identity-allocations.jsonl'
SCOPE = 'Candidate migration; partial scope correspondence; no implementation satisfaction or transferred domain authority'

def store(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(c.encoded(data))

def prop(ref, value):
    return {'ref': ref, 'values': [{'text': value, 'attributes': {}}]}

def set_prop(rows, ref, value):
    rows[:] = [p for p in rows if p['ref'] != ref] + [prop(ref, value)]

def clean_name(row):
    # This is an editorial addition marker, not part of the capability's name.
    return re.sub(r' \*\*\[NEW[^\]]*\]\*\*$', '', row['name'])

def inputs(code):
    return [{'key': a, 'type': 'feature' if TARGET[a]['level'] == 4 else 'capability',
             'name': clean_name(TARGET[a]), 'tier': 'FT' if TARGET[a]['level'] == 4 else 'CT' + str(TARGET[a]['level']),
             'category': 'enterprise' if a.startswith('E') else 'business'} for a in ancestry(code)]

def spec(code):
    row = TARGET[code]
    source = FR / ('feature-reconciliation.json' if row['level'] == 4 else 'capability-reconciliation.json')
    source_rows = c.read_json(source)
    index = next(i for i, x in enumerate(source_rows) if x['id'] == code)
    display = ('[Feature]' if row['level'] == 4 else '[CT' + str(row['level']) + ']') + ' ' + clean_name(row)
    naming_inputs = inputs(code)
    metadata = {'schemaVersion': 1, 'canonicalId': None, 'archimateType': 'Capability',
        'specialization': 'Feature' if row['level'] == 4 else None, 'displayName': display,
        **qualified_names(naming_inputs), 'standing': {'semantic': 'Accepted replacement model — Pass3/014; implementation unverified', 'currentness': 'current', 'incorporationReview': 'Pending Solution Architect review of migration result'},
        'identity': {'authority': AUTHORITY, 'sourceIdentity': code},
        'naming': {'authority': 'enterprise-architecture/governance/canonical-identity-and-naming.md', 'inputs': naming_inputs,
                   'categoryInterpretation': 'Accepted combined Business/Business Enabling hierarchy uses existing business CTn vocabulary; Enterprise uses CTn-EC. No artificial BEC split.'},
        'catalogue': {'id': code, 'parent': row['parent'], 'tier': 'Feature' if row['level'] == 4 else 'CT' + str(row['level']),
                      'sourceName': row['name'], 'hierarchy': 'Enterprise' if code.startswith('E') else 'Business/Business Enabling'},
        'provenance': [{'source': str(CATALOGUE.relative_to(ROOT)), 'selector': code, 'role': 'Accepted Pass3/014 source definition, now incorporated into this canonical resource', 'retainedAt': str(CATALOGUE.relative_to(ROOT)), 'sha256': c.sha(CATALOGUE.read_bytes())},
                       {'source': str(source.relative_to(ROOT)), 'selector': '/' + str(index), 'role': 'Exact source contributions, original wording/criteria/status and accepted adjudication correspondence; historical assurance evidence', 'retainedAt': str(source.relative_to(ROOT))},
                       {'source': AUTHORITY, 'selector': 'Authorised Activity / Scope / B', 'role': 'Explicit canonical incorporation and UUID allocation authority', 'retainedAt': AUTHORITY}],
        'sourceBindings': [{'source': str(CATALOGUE.relative_to(ROOT)), 'selector': code}], 'relationships': [], 'unresolvedProperties': {}}
    if code.startswith('E07.03.05'):
        metadata['standing']['qualification'] = 'Separate E07.03.05 semantic review remains unresolved as recorded by Pass3/014; incorporation does not resolve it.'
    body = '# ' + display + '\n\n## Architectural content\n\n' + row['description'] + '\n'
    # Preserve source annotations and cross-cutting contribution context without
    # turning contextual text into an unauthorised native relationship.
    if row['level'] < 4:
        text = CATALOGUE.read_text()
        match = re.search(r'^#{2,4} ' + re.escape(code) + r' — .*\n(.*?)(?=^#{2,4} |\Z)', text, re.M | re.S)
        if match:
            context = [line for line in match[1].splitlines() if line.startswith('*') and not line.startswith('-')]
            if context: body += '\n### Retained source context\n\n' + '\n'.join(context) + '\n'
    body += '\n### Incorporation standing\n\nCatalogue identity `' + code + '`. The accepted definition is preserved above. Migration verification and source measurements do not establish implementation satisfaction. Existing source qualifications remain in force.\n'
    return {'metadata': metadata, 'body': body, 'navigationParent': ['Strategy']}

def allocate():
    allocated = {}
    if LEDGER.exists():
        for line in LEDGER.read_text().splitlines():
            entry = json.loads(line); code = entry['catalogueId']
            assert code not in allocated
            allocated[code] = entry
    workspace = OUT / 'authoring/allocation-workspace'
    config = workspace / c.CONFIG
    config.mkdir(parents=True, exist_ok=True)
    for name in ['type-catalogue.json', 'element.schema.json', 'native-relationships.json']:
        p = config / name
        if not p.exists(): p.symlink_to(ROOT / c.CONFIG / name)
    for relative in [c.TOOLING, Path('enterprise-architecture/design/evidence/1.1.20/001')]:
        p = workspace / relative
        if not p.exists(): p.parent.mkdir(parents=True, exist_ok=True); p.symlink_to(ROOT / relative, target_is_directory=True)
    nav_template = c.read_json(BEFORE / 'navigation.json')
    for root in nav_template['roots']: root['children'] = []
    nav_path = workspace / 'enterprise-architecture/continuum/navigation.json'
    nav_path.parent.mkdir(parents=True, exist_ok=True)
    if not nav_path.exists(): store(nav_path, nav_template)
    # Recover a create that finished before interruption; never allocate again.
    for cid, (m, body, p) in c.load(workspace)[1].items():
        code = m['catalogue']['id']
        if code not in allocated:
            entry = {'catalogueId': code, 'canonicalId': cid, 'metadata': m, 'body': body}
            with LEDGER.open('a') as f: f.write(json.dumps(entry, ensure_ascii=False) + '\n'); f.flush(); os.fsync(f.fileno())
            allocated[code] = entry
        assert allocated[code]['canonicalId'] == cid
        p.unlink()
    store(nav_path, nav_template)
    for index, code in enumerate(TARGET, 1):
        if code in allocated: continue
        p = c.create(spec(code), root=workspace)
        m, body = c.decode(p.read_text())
        entry = {'catalogueId': code, 'canonicalId': m['canonicalId'], 'metadata': m, 'body': body}
        with LEDGER.open('a') as f: f.write(json.dumps(entry, ensure_ascii=False) + '\n'); f.flush(); os.fsync(f.fileno())
        allocated[code] = entry
        p.unlink(); store(nav_path, nav_template)
        if index % 250 == 0: print('Persisted create allocations', index, '/', len(TARGET), flush=True)
    assert set(allocated) == set(TARGET)
    shutil.rmtree(workspace)
    return allocated

def new_relation(source, target, kind, key, *, historical=None):
    row = {'identifier': c.semantic_id('capability-replacement:' + key), 'archimateType': kind,
        'source': source, 'target': target, 'standing': {'semantic': 'Candidate semantic migration' if historical else 'Accepted Pass3/014 containment', 'currentness': 'current'},
        'provenance': {'source': AUTHORITY, 'selector': 'C' if historical else 'B', 'hcrl': HCRL},
        'properties': [prop('propid-7', 'current'), prop('propid-6', 'Candidate' if historical else 'Accepted')]}
    if historical:
        row['provenance']['predecessorRelationshipIdentifier'] = historical['identifier']
        row['provenance']['coverage'] = SCOPE
        for name in ['specialization', 'attributes', 'name']:
            if name in historical: row[name] = copy.deepcopy(historical[name])
        # Preserve non-derived relation properties, especially Home specialisation.
        row['properties'] += [copy.deepcopy(p) for p in historical.get('properties', []) if p['ref'] not in ['propid-7', 'propid-6']]
        row['documentation'] = copy.deepcopy(historical.get('documentation', [])) + [{'text': SCOPE + '. Original relationship ' + historical['identifier'] + ' remains preserved as history.', 'attributes': {}}]
    return row

def assemble(allocated, ledger):
    result = copy.deepcopy(SNAPSHOT)
    new = {}
    for code, entry in allocated.items():
        m = copy.deepcopy(entry['metadata']); body = entry['body']; cid = m['canonicalId']
        m['exchange']['properties'] = [prop('propid-25', code), prop('propid-24', m['catalogue']['tier']), prop('propid-5', 'FT' if m.get('specialization') else m['catalogue']['tier']), prop('propid-7', 'current'), prop('propid-6', 'Accepted')]
        new[code] = m
        result[cid] = {'metadata': m, 'body': body, 'path': str(c.element_path(m, c.read_json(ROOT / c.CONFIG / 'type-catalogue.json')))}
    byrid = {x['metadata']['exchange']['identifier']: cid for cid, x in SNAPSHOT.items()}
    correspondence = []
    for cid, mapping in ledger.items():
        m = result[cid]['metadata']
        original_identity = copy.deepcopy(m['identity'])
        codes = mapping['targetCatalogueIds']
        m['standing']['currentness'] = 'superseded' if codes else 'retired'
        m['identity']['replacementHistory'] = {'authority': AUTHORITY, 'originalIdentityMetadata': original_identity,
            'replacementCatalogueIds': codes, 'replacementCanonicalIds': [new[code]['canonicalId'] for code in codes],
            'disposition': mapping['disposition'], 'completeSemanticEquivalenceAsserted': False}
        m['identity'].pop('predecessorCanonicalIds', None)
        if codes:
            primary = new[codes[0]]
            m['identity']['correspondence'] = {'authority': AUTHORITY, 'successorCanonicalId': primary['canonicalId'],
                'historicalIdentityReserved': True, 'disposition': 'Primary replacement scope; partial/multiple correspondence retained separately, not complete equivalence'}
            primary['identity'].setdefault('predecessorCanonicalIds', []).append(cid)
        else: m['identity'].pop('correspondence', None)
        set_prop(m['exchange']['properties'], 'propid-7', m['standing']['currentness'])
        mapping['replacementCanonicalIds'] = [new[code]['canonicalId'] for code in codes]
        correspondence.append(mapping)
        task_link = os.path.relpath(ROOT / AUTHORITY, (ROOT / result[cid]['path']).parent)
        result[cid]['body'] += '\n## Capability model replacement history\n\nThe original content above remains historical and unchanged. This resource is ' + m['standing']['currentness'] + ' under [the replacement task](' + task_link + '). ' + ('Primary replacement catalogue identity `' + codes[0] + '`; additional scope correspondences: ' + ', '.join('`' + code + '`' for code in codes[1:]) + '. ' if codes else 'No legitimate replacement responsibility is asserted. ') + 'Original canonical/Exchange identities and source contributions remain reserved. Correspondence does not assert complete semantic equivalence, implementation satisfaction or transfer domain authority.\n'
    # Every old relationship touching the replacement boundary is retained with
    # exact identity/endpoints/type. Explicit historical state excludes it from
    # active containment without losing authored View connection references.
    retired_relations = set()
    originals = {}
    relation_evidence = []
    for cid, x in result.items():
        if cid not in SNAPSHOT: continue
        for row in x['metadata']['relationships']:
            source = byrid.get(row['source']); target = byrid.get(row['target'])
            if source not in OLD and target not in OLD: continue
            originals[row['identifier']] = copy.deepcopy(row)
            row['migrationHistory'] = {'authority': AUTHORITY, 'originalStanding': copy.deepcopy(row['standing']), 'disposition': 'superseded relationship; exact original preserved in before snapshot'}
            row['standing']['currentness'] = 'superseded'
            set_prop(row.setdefault('properties', []), 'propid-7', 'superseded')
            retired_relations.add(row['identifier'])
    # Preserve qualified predicates as historical assertions with unchanged
    # original predicate, endpoints and mapping semantics; synchronize their
    # source-qualified relation state rather than leaving contradictory claims.
    for x in result.values():
        for q in x['metadata'].get('qualifiedAssertions', []):
            if q.get('nativeMapping', {}).get('relationshipIdentifier') in retired_relations:
                q['preReplacementRecordState'] = q['record_state']; q['record_state'] = 'superseded'
        for r in x['metadata']['relationships']:
            if r['identifier'] not in retired_relations: continue
            for contribution in r.get('qualifiedPredicateContributions', []):
                contribution['recordState'] = 'superseded'
    # Deterministic target selection is bounded by the individually investigated
    # semantic correspondences, never a global name match or Cartesian fan-out.
    retrieval = Retrieval()
    def select(cid, context='', home=False):
        if cid not in OLD: return result[cid]['metadata']['exchange']['identifier'], None
        codes = ledger[cid]['targetCatalogueIds']
        if not codes: return None, None
        if home or len(codes) == 1: chosen = codes[0]
        else:
            query = collections.Counter(tokens(context or old_text(cid)))
            query = {t: (1 + math.log(n)) * retrieval.idf.get(t, 0) for t, n in query.items()}
            scores = {code: sum(w * retrieval.bags[code].get(t, 0) for t, w in query.items()) / retrieval.norms[code] for code in codes}
            chosen = max(enumerate(codes), key=lambda x: (scores[x[1]], -x[0]))[1]
        return new[chosen]['exchange']['identifier'], chosen
    seen = {}
    for rid, prior in originals.items():
        a = byrid[prior['source']]; b = byrid.get(prior['target'])
        record = {'predecessorRelationshipIdentifier': rid, 'archimateType': prior['archimateType'],
            'specialization': prior.get('specialization'), 'oldSourceCanonicalId': a, 'oldTargetCanonicalId': b,
            'originalStanding': prior['standing'], 'originalProvenance': prior['provenance']}
        # The source/result hierarchy is independently accepted. Old Composition
        # never supplies new structural edges or foreign ancestry.
        if prior['archimateType'] == 'Composition' and a in OLD and b in OLD:
            record.update(disposition='replaced-by-accepted-hierarchy', rationale='Accepted catalogue parentage replaces the old hierarchy; no cross-product of source correspondences creates containment.')
            relation_evidence.append(record); continue
        if b is None:
            record.update(disposition='retired-without-equivalent', rationale='Relationship-to-relationship assertion remains historical; no element endpoint is invented.')
            relation_evidence.append(record); continue
        context = definitions(a) + ' ' + SNAPSHOT[a]['metadata']['displayName'] + ' ' + definitions(b) + ' ' + SNAPSHOT[b]['metadata']['displayName']
        ar, ac = select(a, context)
        br, bc = select(b, context, home=prior.get('specialization') == 'Home')
        if not ar or not br or ar == br:
            record.update(disposition='retired-without-equivalent' if not ar or not br else 'collapsed-within-replacement-scope',
                rationale='No legitimate replacement endpoint remains, or both endpoints collapse to the same responsibility. Preserve the original without manufacturing a self-dependency.')
            relation_evidence.append(record); continue
        # Explicit historical assertions do not acquire currentness by migration.
        states = [v['text'] for p in prior.get('properties', []) if p['ref'] == 'propid-7' for v in p['values']]
        if states and states != ['current']:
            record.update(disposition='retained-historical-only', rationale='Original relationship has explicit non-current state; correspondence supplies no promotion.')
            relation_evidence.append(record); continue
        key = (prior['archimateType'], prior.get('specialization'), ar, br, json.dumps(prior.get('attributes', {}), sort_keys=True))
        if key in seen:
            fresh = seen[key]; disposition = 'merged-equivalent-migrated-assertion'
            fresh['provenance'].setdefault('additionalPredecessorRelationshipIdentifiers', []).append(rid)
        else:
            fresh = new_relation(ar, br, prior['archimateType'], rid, historical=prior)
            owner = next(cid for cid, x in result.items() if x['metadata']['exchange']['identifier'] == ar)
            result[owner]['metadata']['relationships'].append(fresh)
            seen[key] = fresh; disposition = 'migrated'
        record.update(disposition=disposition, resultingRelationshipIdentifier=fresh['identifier'],
            newSourceCatalogueId=ac, newTargetCatalogueId=bc, coverage='partial; no implementation satisfaction',
            rationale='Preserve native relationship meaning, direction and Home specialisation. Select the most context-supported endpoint only among investigated source correspondences; secondary coverage remains explicitly partial.')
        relation_evidence.append(record)
    hierarchy = []
    for code, m in new.items():
        if not TARGET[code]['parent']: continue
        parent = new[TARGET[code]['parent']]
        r = new_relation(parent['exchange']['identifier'], m['exchange']['identifier'], 'Composition', TARGET[code]['parent'] + '>' + code)
        parent['relationships'].append(r)
        hierarchy.append({'parent': TARGET[code]['parent'], 'child': code, 'relationshipIdentifier': r['identifier']})
    # A migrated explicit Home carries its naming context. No Realization,
    # Serving, view or folder is used to invent a Home or naming ancestry.
    names = []
    new_by_exchange = {m['exchange']['identifier']: code for code, m in new.items()}
    for cid, x in result.items():
        if cid not in SNAPSHOT or cid in OLD: continue
        m = x['metadata']; homes = [r for r in m['relationships'] if r.get('specialization') == 'Home' and r['identifier'] not in retired_relations]
        if not homes: continue
        migrated_homes = [r for r in homes if r['target'] in new_by_exchange]
        if not migrated_homes: continue
        assert len(homes) == 1, ('multiple Home assertions', cid)
        code = new_by_exchange[homes[0]['target']]
        kinds = {'BusinessFunction': 'function', 'BusinessService': 'service', 'BusinessProcess': 'process'}
        if m['archimateType'] not in kinds: continue
        local = re.sub(r'^\[[^]]+\] ', '', m['displayName'])
        naming_inputs = inputs(code) + [{'key': cid, 'type': kinds[m['archimateType']], 'name': local}]
        previous = {k: m[k] for k in ['friendlyName', 'canonicalName']}
        m.update(qualified_names(naming_inputs))
        m['naming']['preReplacementNames'] = previous
        m['naming']['inputs'] = naming_inputs
        m['naming']['authority'] = 'enterprise-architecture/governance/canonical-identity-and-naming.md'
        m['naming']['expected'] = {k: m[k] for k in ['displayName', 'friendlyName', 'canonicalName']}
        names.append({'canonicalId': cid, 'explicitHomeRelationshipIdentifier': homes[0]['identifier'], 'targetCatalogueId': code,
            'before': previous, 'after': {k: m[k] for k in previous}, 'displayNameAndIdentityPreserved': True})
    return result, new, correspondence, relation_evidence, hierarchy, names

def navigation(result, new, ledger):
    nav = c.read_json(BEFORE / 'navigation.json')
    def node(code, context=False):
        n = {'kind': 'context' if context else 'element', 'basis': 'Accepted Pass3/014 hierarchy; explicit replacement task. Navigation supplies no relationship.'}
        n['contextCanonicalId' if context else 'canonicalId'] = new[code]['canonicalId']
        if context: n['contextRole'] = 'capability'
        return n
    nodes = {code: node(code) for code in TARGET}
    roots = {'B': [], 'E': []}
    for code in TARGET:
        parent = TARGET[code]['parent']
        if parent: nodes[parent].setdefault('children', []).append(nodes[code])
        else: roots[code[0]].append(nodes[code])
    strategy = nav['roots'][2]
    cap_section = next(x for x in strategy['children'] if x.get('title') == 'Capabilities')
    cap_section['children'] = [{'title': 'Business and Business Enabling Capabilities', 'basis': AUTHORITY, 'children': roots['B']}, {'title': 'Enterprise Capabilities', 'basis': AUTHORITY, 'children': roots['E']}]
    # Preserve unpaired retirements in their native Strategy context. Historical
    # cross-reference entries require a real, reciprocal successor correspondence.
    unpaired = [cid for cid, row in ledger.items() if not row['targetCatalogueIds']]
    if unpaired: cap_section['children'].append({'title': 'Retired Capability Responsibilities', 'basis': AUTHORITY, 'children': [{'canonicalId': cid, 'kind': 'element', 'basis': 'No legitimate successor; source identity and definition retained'} for cid in unpaired]})
    history = next(x for x in nav['roots'][-1]['children'] if x.get('title') == 'Historical Capability Identities')
    history['children'] = [{'canonicalId': cid, 'kind': 'historical', 'basis': 'Reserved source identity; explicit primary successor with separate multiple/partial scope correspondence'} for cid, row in sorted(ledger.items()) if row['targetCatalogueIds']]
    # Rebuild only existing Business capability contexts, retaining each actual
    # behaviour element once and the full accepted ancestry of its target context.
    business = nav['roots'][3]
    section = next(x for x in business['children'] if x.get('title') == 'Business Behaviour')
    allocations = []
    def capture(n, contexts=()):
        context = n.get('contextCanonicalId')
        if context in OLD: contexts += (context,)
        if n.get('canonicalId'):
            preserved = copy.deepcopy(n)
            preserved.pop('children', None)
            allocations.append((preserved, contexts))
        for child in n.get('children', []): capture(child, contexts)
    capture(section)
    section['children'] = []
    contexts = {}
    behaviour_groups = {}
    unallocated = []
    for n, previous_contexts in allocations:
        cid = n['canonicalId']; m = result[cid]['metadata']
        home_codes = [r['provenance'].get('predecessorRelationshipIdentifier') for r in m['relationships'] if r.get('specialization') == 'Home' and r.get('provenance', {}).get('coverage')]
        chosen = None
        if home_codes:
            target = next(r['target'] for r in m['relationships'] if r.get('specialization') == 'Home' and r.get('provenance', {}).get('coverage'))
            chosen = next(code for code, x in new.items() if x['exchange']['identifier'] == target)
        if not chosen:
            for old in reversed(previous_contexts):
                codes = ledger[old]['targetCatalogueIds']
                if codes: chosen = codes[0]; break
        if not chosen: unallocated.append(n); continue
        parent = section
        for code in ancestry(chosen):
            if code not in contexts:
                contexts[code] = node(code, context=True)
                parent.setdefault('children', []).append(contexts[code])
            parent = contexts[code]
        group_title = {'BusinessFunction': 'Business Functions', 'BusinessService': 'Business Services',
                       'BusinessProcess': 'Business Processes', 'BusinessEvent': 'Business Events'}[m['archimateType']]
        group_key = (chosen, group_title)
        if group_key not in behaviour_groups:
            behaviour_groups[group_key] = {'title': group_title, 'basis': 'Retained Business behaviour navigation convention; native classification unchanged', 'children': []}
            parent.setdefault('children', []).append(behaviour_groups[group_key])
        behaviour_groups[group_key]['children'].append(n)
    if unallocated: section['children'].append({'title': 'Behaviour with Unestablished Capability Context', 'basis': 'Preserved source allocation without inventing Home or coverage', 'children': unallocated})
    return nav

def run(apply=False):
    assert c.read_json(ROOT / 'architecture-release.json') == {'version':'1.1.20','previous_version':'1.1.19'}
    pinned = c.read_json(BEFORE / 'input-manifest.json')
    for source in pinned['verified_sources']:
        assert c.sha((ROOT / source['path']).read_bytes()) == source['sha256'], ('Accepted input changed', source['path'])
    assert c.sha((ROOT / AUTHORITY).read_bytes()) == pinned['task_sha256'], 'Operative Task changed'
    allocated = allocate()
    ledger = prepare()
    result, new, correspondence, relations, hierarchy, names = assemble(allocated, ledger)
    nav = navigation(result, new, ledger)
    stage = Path(tempfile.mkdtemp(prefix='harmonia-capability-replacement-'))
    for relative in [c.CONFIG, c.TOOLING, c.DESIGN, Path('enterprise-architecture/design/evidence')]:
        p = stage / relative; p.parent.mkdir(parents=True, exist_ok=True); p.symlink_to(ROOT / relative, target_is_directory=True)
    shutil.copyfile(ROOT / 'architecture-release.json', stage / 'architecture-release.json')
    for x in result.values():
        path = stage / x['path']; path.parent.mkdir(parents=True, exist_ok=True); path.write_text(c.encode(x['metadata'], x['body']))
    store(stage / 'enterprise-architecture/continuum/navigation.json', nav)
    print('Staged', len(result), 'elements at', stage, flush=True)
    validation = c.validate(stage, previous=SNAPSHOT)
    store(OUT / 'staged-validation.json', validation)
    content, projection_validation = c.project(stage)
    store(OUT / 'projection-validation.json', projection_validation)
    candidate = OUT / 'candidate-v2'; candidate.mkdir(exist_ok=True)
    (candidate / 'Harmonia-Architecture-candidate.xml').write_bytes(content)
    store(OUT / 'semantic-correspondence.json', correspondence)
    store(OUT / 'relationship-reconciliation.json', relations)
    store(OUT / 'hierarchy-reconciliation.json', hierarchy)
    store(OUT / 'home-naming-reconciliation.json', names)
    store(OUT / 'summary.json', {'standing':'Working Continuum candidate; migration result review pending', 'activePopulation':dict(collections.Counter(new[code]['catalogue']['tier'] for code in new)),
        'baselineElements':len(SNAPSHOT), 'resultingElements':len(result), 'newCanonicalIdentities':len(new),
        'retainedLegacyCapabilityIdentities':len(OLD), 'legacyRetiredWithoutSuccessor':sum(not r['targetCatalogueIds'] for r in ledger.values()),
        'relationshipDispositions':dict(collections.Counter(r['disposition'] for r in relations)), 'acceptedHierarchyRelationships':len(hierarchy),
        'migratedRelationshipTypes':dict(collections.Counter(r['archimateType'] for r in relations if r['disposition']=='migrated')),
        'homeDerivedNamingChanges':len(names), 'canonicalApplied':apply, 'stage':str(stage)})
    if apply:
        # Detect changes since the pinned baseline before touching canonical state.
        for cid, x in SNAPSHOT.items(): assert c.sha((ROOT / x['path']).read_bytes()) == x['sha256'], ('Baseline changed', cid)
        assert (ROOT / 'enterprise-architecture/continuum/navigation.json').read_bytes() == (BEFORE / 'navigation.json').read_bytes()
        for x in result.values():
            p = ROOT / x['path']; content = c.encode(x['metadata'], x['body'])
            if not p.exists() or p.read_text() != content: p.parent.mkdir(parents=True, exist_ok=True); p.write_text(content)
        store(ROOT / 'enterprise-architecture/continuum/navigation.json', nav)
        print('Applied validated working Continuum; no accepted publication or release mutation', flush=True)
    else: print('Staged candidate only; use --apply after validation', flush=True)

if __name__ == '__main__': run('--apply' in sys.argv)
