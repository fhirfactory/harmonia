#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Inventory reviewed RAD source assertions; never generate Exchange semantics.

Locators and capture keys below remain navigation evidence, not allocated IDs.
Heading candidates are explicitly not an approved architectural element census.
"""
import argparse
import collections
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys

sys.path.insert(0, str(Path(__file__).resolve().parents[4] / 'scripts'))
from architecture_release import load_release


COMPONENT_TYPES = {
    'C01': ('ApplicationComponent', 'Direct', 'Models/DataSets responsibilities retain their separate boundaries.'),
    'C02': ('ApplicationComponent', 'Direct', 'Themis owns authorisation; external authentication remains SEP.'),
    'C03': ('ApplicationComponent', 'Direct', 'Kleio owns authoritative audit; no ownership of source evidence inferred.'),
    'C04': ('ApplicationComponent', 'Direct', 'Ponos is a reusable execution component; no Ergo containment.'),
    'C05': (None, 'Requires architectural adjudication', 'Ergo is a reusable behavioural pattern/contract; not automatically a component, function or process instance.'),
    'C06': ('DataObject', 'Derived but deterministic', 'Praxis operational/assurance envelope is application-owned information, not an execution engine; exact representation/identity remains open.'),
    'C07': ('DataObject', 'Direct', 'Pragma is expressly an Application Data Object; genuine Realization of Metabolē.'),
    'C08': ('ApplicationComponent', 'Direct', 'PragmaFactory application embodiment; explicit logical Aggregation under Pragma Management, not execution ownership.'),
    'C09': ('ApplicationComponent', 'Direct', 'Mneme managed Context/Pragma access and active coordination.'),
    'C10': ('ApplicationComponent', 'Direct', 'Petasos durable queue/handoff responsibility, not referenced information ownership.'),
    'C11': (None, 'Documentation only', 'Bundle of supporting contracts; not extra exposed Mneme Services or a component.'),
    'C12': ('ApplicationComponent', 'Direct', 'Mnemosyne authoritative durable establishment; not an application database access route.'),
    'C13': ('ApplicationCollaboration', 'Derived but deterministic', 'Explicit cross-component solution collaboration; no enclosing subsystem or inferred participant assignments.'),
    'C14': ('ApplicationComponent', 'Direct', 'Pylai external interoperability responsibility; component not identical to its exposed service.'),
    'C15': ('ApplicationComponent', 'Direct', 'Iris contextual presentation responsibility, no durable truth ownership.'),
    'C16': (None, 'Requires architectural adjudication', 'Three applications bundled under one navigation key; resolve separate software identities before allocation.'),
    'C17': ('ApplicationComponent', 'Direct', 'Backend-for-frontend mediation responsibility; no direct persistence access.'),
    'C18': ('ApplicationComponent', 'Direct', 'Agora controls collaboration and owns native information; Omilia operates.'),
    'C19': ('ApplicationComponent', 'Direct', 'Application DigitalTwin specialised execution responsibility; not a strategic component/deployment inference.'),
    'C20': ('ApplicationComponent', 'Direct', 'Dokimasia specialised independent assurance execution; terminal boundaries retained.'),
    'C21': (None, 'Requires architectural adjudication', 'Simulation support subsystem bundle; typed simulator instances need identities; production isolation preserved.'),
    'C22': (None, 'Documentation only', 'Support inventory, explicitly not a common subsystem; contained Administrative Functions are governed separately.'),
    'C23': ('ApplicationComponent', 'Direct', 'Ergon expressly bounded task-completion software component, despite behavioural contract.'),
    'C24': (None, 'Requires architectural adjudication', 'Meaningful Ergon collections/libraries/assembly; no automatically shared runtime container.'),
    'C25': ('ApplicationComponent', 'Direct', 'Behaviour specialises Ergon; distinct from ordinary Business Function by same word.'),
    'C26': ('ApplicationComponent', 'Direct', 'Dokimasia Ergon specialises Ergon for bounded assurance.'),
    'C27': ('ApplicationComponent', 'Direct', 'Discrete candidate AI Assurance Ergon responsibility established; detailed architecture/provider remains discovery.'),
    'C28': ('ApplicationFunction', 'Derived but deterministic', 'Contained Mneme management responsibility; not a separate deployable component or mandatory exposed Service.'),
    'C29': ('ApplicationInterface', 'Direct', 'Source explicitly establishes interface semantics; endpoint identity/technical contracts open.'),
    'C30': (None, 'Requires architectural adjudication', 'Generic behavioural/flow abstraction; not automatically a component or class instance.'),
    'C31': ('ApplicationInteraction (proposed)', 'Requires architectural adjudication', 'Comparable to inter-interface interaction; collective behaviour/participant typing needs review, no direct interface Assignment inference.'),
    'C32': (None, 'Requires architectural adjudication', 'Intra-component passage; not automatically a Process or Route element.'),
    'C33': ('DataObject (proposed)', 'Requires architectural adjudication', 'Ordered routing/control definition; exact representation/management remains deferred.'),
    'C34': ('DataObject (proposed)', 'Requires architectural adjudication', 'Control sequence is aggregated by Praxis [0..1]; no execution engine inferred.'),
    'C35': ('DataObject', 'Direct', 'Expressly candidate Application Data Object Journey; Energeia ownership, O&A durability, observed evidence not inference.'),
    'C36': (None, 'Requires architectural adjudication', 'Publishing combines provider definition and contained management behaviour; separate facets before allocation.'),
    'C37': (None, 'Requires architectural adjudication', 'Subscription combines consumer definition and selection behaviour; not Petasos destination binding.'),
    'C38': (None, 'Requires architectural adjudication', 'Pattern combines governed definition/evaluation; no final language or universal component.'),
    'C39': ('ApplicationComponent', 'Direct', 'Energeia discrete operational-management component; Ponos peer, no Composition.'),
    'C40': ('ApplicationFunction', 'Derived but deterministic', 'Internal Mneme exceptional-delivery management; not fifth exposed Service or separate execution engine.'),
    'C41': ('ApplicationComponent (logical library responsibility)', 'Derived but deterministic', 'Calliope-Models build-time library responsibility, not runtime service/deployment unit; binaries later Artifact only when governed.'),
    'C42': ('ApplicationComponent', 'Direct', 'Calliope-DataSets runtime mutable content management; no separate datastore selected.'),
    'C43': (None, 'Requires architectural adjudication', 'Terma logical external interface also acts; exposure/communication behaviour facets require native typing decision.'),
    'C44': ('DataObject', 'Derived but deterministic', 'Transient Diadromí interaction/context; genuine Metabolē Realization, no Pragma/Task/durable handoff equivalence.'),
    'C45': ('ApplicationFunction (proposed)', 'Requires architectural adjudication', 'Controller coordinates active Server READ/SEARCH; no recoverable state or universal component architecture.'),
    'C46': (None, 'Requires architectural adjudication', 'Praxis.Horos logical boundary; representation deferred, not endpoint technology or engine.'),
    'C47': ('ApplicationComponent', 'Direct', 'Harmonia.Omilia.Server operations responsibility; Synapse is provenance, not identity.'),
    'C48': ('ApplicationComponent', 'Direct', 'Harmonia.Omilia.Client; Element provenance not identity; Iris integration deferred.'),
    'C49': ('ApplicationComponent', 'Direct', 'Harmonia.Sylloge Binary Content Management; physical custody can remain external.'),
}


def headings(path, pattern):
    for number, line in enumerate(path.read_text().splitlines(), 1):
        if re.match(pattern, line):
            yield {'source_line': number, 'heading': line.lstrip('# '), 'source_text': line,
                   'identity_status': 'source locator only; no semantic identity allocated'}


def inventory(repository, documentation_map, release_file=None):
    repository = Path(repository)
    release = load_release(release_file or repository / 'architecture-release.json')
    rad = repository / 'docs/markdown'
    excluded = {'rad-archimate-model-mapping.md', 'rad-archimate-mapping-catalogue.md',
                'rad-archimate-coverage-assessment.md', 'rad-archimate-publication-plan.md',
                'rad-archimate-source-inventory.json'}
    sources = [{'path': str(p.relative_to(repository)), 'sha256': hashlib.sha256(p.read_bytes()).hexdigest(),
                'content_role': 'candidate/scratchpad navigation; not current architecture authority' if 'scratchpad' in p.parts else 'assertion-level standing must be preserved'}
               for p in sorted(rad.rglob('*')) if p.is_file() and p.name not in excluded]
    capture = json.loads((rad / '03-business-architecture/reference-model/business-architecture.json').read_text())
    types = {'capability': 'Capability', 'feature': 'Capability', 'function': 'BusinessFunction',
             'service': 'BusinessService', 'process': 'BusinessProcess'}
    elements = []
    for record in capture['elements']:
        item = dict(record)
        item['proposed_archimate_type'] = types[record['type']]
        item['mapping_classification'] = 'Derived but deterministic' if record['type'] == 'feature' else 'Direct'
        item['mapping_explanation'] = 'Feature preserves Capability semantics and FT classification; never CT4 or Requirement.' if record['type'] == 'feature' else 'Native type compatibility only; identity, definition and relationship gates are independent.'
        if record['status'] != 'established':
            item['mapping_classification'] = 'Requires architectural adjudication'
        item['publication_gates'] = ['Canonical ID/publication crosswalk not allocated', 'No exact proven-Exchange identity mapping demonstrated', 'Verify full governed description and each relationship']
        item['documentation_review'] = 'capture summary present; inspect source qualifications' if record.get('summary') else 'capture summary absent; inspect source, do not synthesize'
        elements.append(item)
    relationships = []
    for record in capture['relationships']:
        item = dict(record)
        typ = record['type']
        item['proposed_archimate_type'] = 'Composition' if typ == 'capability_decomposition' else 'Realization' if typ == 'realises_feature' else 'Association (source predicate retained; review required)'
        item['publication_gates'] = ['Exact endpoint identities/crosswalks required', 'Review typed direction and native legality']
        item['mapping_classification'] = ('Requires architectural adjudication' if record['status'] != 'established'
                                          else 'Direct' if typ in ('capability_decomposition', 'realises_feature') else 'Derived but deterministic')
        relationships.append(item)
    sections = []
    selectors = {
        '01-motivation/stakeholders/enterprise-stakeholders.md': (r'^## \d+\.', 'Stakeholder'),
        '01-motivation/stakeholders/external-authorities.md': (r'^### \d+\.', 'Stakeholder (governing-body facet)'),
        '01-motivation/drivers-assessments/drivers.md': (r'^## \d+\.', 'Driver'),
        '01-motivation/drivers-assessments/assessments.md': (r'^## \d+\.', 'Assessment'),
        '01-motivation/goals-outcomes/strategic-goals.md': (r'^## \d+\.', 'Goal'),
        '01-motivation/goals-outcomes/business-outcomes.md': (r'^## \d+\.', 'Outcome'),
        '01-motivation/requirements-constraints/foundational-requirements.md': (r'^## REQ-FND-', 'Requirement'),
        '01-motivation/requirements-constraints/external-constraints.md': (r'^## CST-EXT-', 'Constraint'),
        'governance/architectural-axioms.md': (r'^### AX-\d+', 'Principle'),
        '02-strategy/courses-of-action/strategic-courses-of-action.md': (r'^### COA-', 'CourseOfAction'),
        '02-strategy/resources/strategic-resources.md': (r'^### SR-', 'Resource'),
        '02-strategy/strategic-views/strategic-value-streams.md': (r'^### VS-', 'ValueStream'),
    }
    for rel, (pattern, target) in selectors.items():
        for item in headings(rad / rel, pattern):
            item.update(source_document='docs/markdown/' + rel, proposed_archimate_type=target,
                        classification='Derived but deterministic' if 'facet' in target else 'Direct',
                        generation_status='Not generated; exact identity/source contract required',
                        documentation='Definition section plus exclusions; not the heading alone')
            sections.append(item)
    business_participants = []
    selectors = [('03-business-architecture/actors-roles/roles.md', 'BusinessRole', r'^\s*- \*\*([^*]+)\*\*:', '## 2.', '## 3.'),
                 ('03-business-architecture/collaborations-interactions/interactions.md', 'BusinessInteraction', r'^- \*\*([^*]+)\*\*:', '## 2.', '## 3.'),
                 ('03-business-architecture/collaborations-interactions/collaborations.md', 'BusinessCollaboration', r'^### 3\.\d+ (.+)$', '## 3.', '## 4.')]
    for rel, target, pattern, start, end in selectors:
        active, role_details = False, False
        for number, line in enumerate((rad / rel).read_text().splitlines(), 1):
            if line.startswith(start):
                active = True
            if line.startswith(end):
                active = False
            if target == 'BusinessRole' and line.startswith('#### '):
                role_details = True
            if line.startswith('### '):
                role_details = False
            match = re.match(pattern, line) if active and not role_details else None
            if match and match[1] not in ('Canonical Name', 'Fundamental Responsibility'):
                business_participants.append({'name': match[1], 'proposed_archimate_type': target,
                    'source_document': 'docs/markdown/' + rel, 'source_line': number, 'definition_excerpt': line,
                    'classification': 'Direct', 'identity_status': 'No canonical identity allocated by this inventory',
                    'relationships': 'Only independently established participation/performance; no edges from nesting'})
    for rel, pattern, target in [('03-business-architecture/actors-roles/roles.md', r'^#### (Service Assurance Modeller|Service Guardian)$', 'BusinessRole'),
                                ('03-business-architecture/collaborations-interactions/interactions.md', r'^### 5\.[123] (.+)$', 'BusinessInteraction')]:
        for item in headings(rad / rel, pattern):
            item.update(source_document='docs/markdown/' + rel, proposed_archimate_type=target,
                        classification='Direct', documentation='Full qualified definition in this source section')
            business_participants.append(item)
    actor_categories = list(headings(rad / '03-business-architecture/actors-roles/actors.md', r'^### 2\.\d+ '))
    for item in actor_categories:
        item.update(proposed_archimate_type=None, classification='Property/metadata',
                    explanation='Actor category, not an automatically instantiated enterprise participant')
    family_sections = []
    for p in sorted((rad / '04-information-architecture/information-families').glob('*.md')):
        for item in headings(p, r'^### (?:[23]\.\d+ |Stage \d+:)'):
            item.update(source_document=str(p.relative_to(repository)), proposed_archimate_type='BusinessObject only if separately governed information meaning is established',
                        classification='Requires architectural adjudication',
                        explanation='Heading candidates include approved meanings, properties, illustrative concepts and unresolved assertions. This is no object count or approval decision.')
            family_sections.append(item)
    app = []
    files = [('component-register.md', r'^###\s+(C\d+)\s+[—–-]\s+(.+)$'),
             ('candidate-feature-map.md', r'^\|\s*(F\d+)\s*\|\s*([^|]+)'),
             ('data-and-persistence.md', r'^\|\s*(D\d+)\s*\|\s*([^|]+)'),
             ('middleware.md', r'^\|\s*(M\d+)\s*\|\s*([^|]+)')]
    for filename, pattern in files:
        path = rad / '05-information-systems-architecture' / filename
        rows = []
        for number, line in enumerate(path.read_text().splitlines(), 1):
            match = re.match(pattern, line)
            if not match:
                continue
            key, label = match.groups()
            if key.startswith('C'):
                target, classification, reason = COMPONENT_TYPES[key]
            elif key.startswith('F'):
                target, classification, reason = None, 'Documentation only', 'Solution-feature assertion bundle, not a Strategy FT; resolve behaviour/service/component facets and reuse existing identities.'
            elif key.startswith('D'):
                target, classification, reason = None, 'Requires architectural adjudication', 'Mixed information/representation/responsibility bundle; separately defined DataObjects may be projected after identity and source typing. No one DataObject per row.'
            else:
                target, classification, reason = None, 'Derived but deterministic', 'Relationship/responsibility bundle; source-specific typed edges, not an extra component, and no flow from adjacency.'
            rows.append({'source_key': key, 'label': label.strip(), 'source_document': str(path.relative_to(repository)),
                         'source_line': number, 'source_assertion': line, 'proposed_archimate_type': target,
                         'classification': classification, 'explanation': reason,
                         'identity_status': 'documentary navigation key; no canonical semantic ID',
                         'standing': 'accepted candidate baseline with assertion-specific adjudications/deferrals; not final MVP'})
        app.extend(rows)
    counts = collections.Counter(e['type'] for e in elements)
    return {
        'purpose': 'Bounded reconciliation/navigation inventory; not governed semantic identity allocation or an Exchange generator input',
        'repository_head': subprocess.run(['git', '-C', str(repository), 'rev-parse', 'HEAD'], capture_output=True, text=True, check=True, timeout=10).stdout.strip(),
        'architecture_release': release,
        'source_files': sources, 'capture_source_register': capture['sources'],
        'business_elements': elements, 'business_relationship_assertions': relationships,
        'catalogue_contexts': capture['catalogue_contexts'], 'presentation_groups': capture['views'],
        'motivation_strategy_section_inventory': sections, 'information_family_heading_candidates': family_sections,
        'business_participant_definitions': business_participants, 'actor_categories_not_instances': actor_categories,
        'application_register_rows': app,
        'exact_enterprise_documentation_crosswalk': documentation_map['matches'],
        'summary': {
            'source_files': len(sources), 'business_elements': len(elements), 'business_types': dict(sorted(counts.items())),
            'canonical_ids_allocated_in_capture': sum(bool(e.get('canonical_id')) for e in elements),
            'local_ids_in_capture': sum(bool(e.get('local_id')) for e in elements),
            'capture_summaries_missing': sum(not e.get('summary') for e in elements),
            'business_relationship_assertions': len(relationships),
            'established_relationship_assertions': sum(r['status'] == 'established' for r in relationships),
            'unresolved_relationship_assertions': sum(r['status'] == 'unresolved' for r in relationships),
            'application_rows': len(app), 'participant_definitions': len(business_participants),
            'participant_types': dict(collections.Counter(p['proposed_archimate_type'] for p in business_participants)), 'information_heading_candidates_not_object_count': len(family_sections),
            'motivation_strategy_source_sections': len(sections),
        },
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--repository', type=Path, required=True)
    parser.add_argument('--documentation-map', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--release-file', type=Path)
    parser.add_argument('--check', action='store_true')
    args = parser.parse_args()
    data = inventory(args.repository, json.loads(args.documentation_map.read_text()), args.release_file)
    if args.check:
        if json.loads(args.output.read_text()) != data:
            parser.exit(1, 'FAIL: source inventory differs from current release/source assessment\n')
    else:
        inputs = [args.repository / source['path'] for source in data['source_files']]
        inputs += [args.documentation_map, args.release_file or args.repository / 'architecture-release.json']
        if args.output.resolve() in {p.resolve() for p in inputs}:
            parser.exit(1, 'FAIL: output must not overwrite a publication input\n')
        args.output.write_text(json.dumps(data, indent=2, ensure_ascii=False) + '\n')
    print(json.dumps(data['summary'], indent=2))


if __name__ == '__main__':
    main()
