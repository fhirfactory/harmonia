#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Explicit source-section ingestion beyond the previously captured population.

Native classification below follows the governed mapping catalogue and inspected
definitions. Selection patterns select instances in those exact catalogues; they
do not classify arbitrary headings, families, examples or directory structures.
No name matching establishes identity. Exact existing source bindings are used
for repeat runs; explicit crosswalks reconcile already identified resources.
"""
from pathlib import Path
import re
import sys
import uuid
import architecture_continuum as c
from ingest_rad_continuum import ENTRY, TASK, AUTHORITY, EVIDENCE, provenance, section

ROOT = c.ROOT


class Ingestion:
    def __init__(self):
        self.catalogue, self.elements = c.load()
        self.bindings = {(b['source'], b['selector']): cid for cid, (m, _, _) in self.elements.items() for b in m.get('sourceBindings', [])}
        self.added, self.enriched = [], []
        sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
        from governed_metadata import component
        self.component = component

    def put(self, source, selector, native, name, text, *, standing='Canonical', existing=None, qualifications=None):
        binding = (source, selector)
        if binding in self.bindings:
            return self.bindings[binding]
        source_prov = provenance(source, selector, 'ingested architectural definition or explicit source contribution', standing=standing)
        if existing:
            m, body, path = self.elements[existing]
            # A prior explicitly accepted retyping governs the same canonical
            # instance. Earlier source abstraction is preserved as a qualification.
            if m['archimateType'] != native:
                source_prov['originalNativeClassification'] = native
                source_prov['qualification'] = 'Existing accepted native classification retained; this earlier source assertion does not reverse the accepted retyping.'
            m['provenance'].append(source_prov)
            m['sourceBindings'].append({'source': source, 'selector': selector})
            rendered_selector = '``' + selector + '``' if selector.startswith('table assertion: ') else selector
            body += '\n## Source-qualified contribution\n\nSource: `' + source + '` — ' + rendered_selector + '. Standing: ' + standing + '.\n\n' + text.strip() + '\n'
            self.elements[existing] = (m, body, path)
            self.enriched.append({'source': source, 'selector': selector, 'canonicalId': existing})
            cid = existing
        else:
            cid = uuid.uuid4().hex
            c.require(cid not in self.elements, 'UUID allocation collision')
            friendly = self.component(name)
            # Vocabulary for these additional types has not been adjudicated by
            # the current naming contract. Do not manufacture a formal qualifier.
            formal = 'BO' if native == 'BusinessObject' else 'MOT' if native in ('Principle', 'Constraint', 'Requirement', 'Stakeholder', 'Goal', 'Driver', 'Assessment', 'Outcome', 'Value', 'Meaning') else None
            m = {'schemaVersion': 1, 'canonicalId': cid, 'archimateType': native, 'specialization': None,
                 'displayName': ('[BusinessObject] ' if native == 'BusinessObject' else '') + name,
                 'friendlyName': friendly, 'canonicalName': formal + '-' + friendly if formal else None,
                 'standing': {'semantic': standing, 'currentness': 'current', 'qualification': qualifications or 'Exact source standing retained; migration/identity allocation does not approve architectural assertions.'},
                 'identity': {'allocation': 'UUIDv4 allocated once under the explicit RAD-1.1.5 migration Task', 'authority': TASK, 'sourceAbsenceCheck': 'No existing governed canonical source binding for this bounded definition; no name-based correspondence adopted.'},
                 'naming': {'authority': 'docs/markdown/governance/canonical-identity-and-naming.md', 'mode': 'Local lexical component; no root or containment completeness inferred', 'expected': {'friendlyName': friendly, 'canonicalName': formal + '-' + friendly if formal else None}},
                 'provenance': [source_prov], 'sourceBindings': [{'source': source, 'selector': selector}],
                 'exchange': {'identifier': c.semantic_id(cid), 'properties': []}, 'relationships': [],
                 'unresolvedProperties': {} if formal else {'canonicalName': 'Formal qualifier for native ' + native + ' is not established in the current naming contract; HCRL 1.1.5-002 DR-5. Identity and source content are independently determinate.'}}
            body = '# ' + m['displayName'] + '\n\n## Architectural content\n\n' + text.strip() + '\n'
            path = ROOT / c.element_path(m, self.catalogue)
            self.elements[cid] = (m, body, path)
            self.added.append({'source': source, 'selector': selector, 'canonicalId': cid, 'archimateType': native, 'displayName': m['displayName'], 'standing': standing})
        self.bindings[binding] = cid
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(c.encode(m, body))
        return cid

    def sections(self, source, pattern, native, **kwargs):
        text = (ROOT / source).read_text()
        for match in re.finditer(pattern, text, re.M):
            heading, name = match.group(0), match.group(1)
            self.put(source, heading, native, name, section(source, heading, len(heading.split(' ')[0])), **kwargs)

    def finish(self):
        path = ROOT / EVIDENCE / 'after/definition-ingestion.json'
        previous = c.read_json(path) if path.exists() else {'allocated': [], 'reconciledContributions': []}
        previous['allocated'].extend(self.added); previous['reconciledContributions'].extend(self.enriched)
        path.write_bytes(c.encoded(previous))
        print('Allocated', len(self.added), 'identities; reconciled', len(self.enriched), 'source contributions.')


def main():
    work = Ingestion()
    # Direct native mappings, explicitly defined individual strategic instances.
    for source, pattern, native in [
        ('02-strategy/courses-of-action/strategic-courses-of-action.md', r'^### COA-\d+: (.+)$', 'CourseOfAction'),
        ('02-strategy/resources/strategic-resources.md', r'^### SR-\d+: (.+)$', 'Resource'),
        ('02-strategy/strategic-views/strategic-value-streams.md', r'^### VS-\d+: (.+)$', 'ValueStream'),
        ('03-business-architecture/collaborations-interactions/collaborations.md', r'^### 3\.\d (.+)$', 'BusinessCollaboration')]:
        work.sections('docs/markdown/' + source, pattern, native)
    # Individual Role definitions, not the six classification families, Actor
    # categories, job-title examples, or contextual Actor-as-Role expressions.
    source = 'docs/markdown/03-business-architecture/actors-roles/roles.md'
    text = (ROOT / source).read_text()
    body = text[text.index('### 2.1'):text.index('## 3.')]
    # These separately defined Roles contain bullet-labelled attributes. Their
    # attributes are not extra Roles; ingest the complete definitions below.
    body = re.sub(r'(?ms)^#### Service Assurance Modeller.*?(?=^### 2\.6)', '', body)
    for match in re.finditer(r'^[ \t]*- \*\*([^*]+)\*\*: (.+)$', body, re.M):
        name, definition = match.groups()
        if name in ('Canonical Name', 'Fundamental Responsibility', 'Responsibility Contexts', 'Derivation', 'Definition', 'Performed Business Functions', 'Approved Process / Interaction Participation', 'Management Boundary', 'Clinical Boundary', 'Conclusion Boundary', 'Allocation Boundary'):
            continue
        work.put(source, match.group(0).strip(), 'BusinessRole', name, match.group(0).strip())
    for name in ('Service Assurance Modeller', 'Service Guardian'):
        heading = '#### ' + name
        work.put(source, heading, 'BusinessRole', name, section(source, heading, 4))
    # Explicitly catalogued discrete interactions. Their category headings do not
    # become extra elements. Boundary/participant qualifications follow below.
    source = 'docs/markdown/03-business-architecture/collaborations-interactions/interactions.md'
    text = (ROOT / source).read_text(); body = text[text.index('### 2.1'):text.index('## 3.')]
    for match in re.finditer(r'^- \*\*([^*]+)\*\*: (.+)$', body, re.M):
        name, definition = match.groups()
        work.put(source, match.group(0), 'BusinessInteraction', name, match.group(0))
    work.sections(source, r'^### 5\.[123] (.+)$', 'BusinessInteraction')
    # Candidate application constructs with determinate native mappings in the
    # governed catalogue. Bundles and expressly unresolved types are omitted here
    # and retained for DR-5; this does not classify all C rows as components.
    source = 'docs/markdown/05-information-systems-architecture/component-register.md'
    text = (ROOT / source).read_text()
    component_keys = 'C01 C02 C03 C39 C04 C23 C25 C26 C27 C08 C09 C10 C12 C49 C14 C15 C17 C18 C47 C48 C19 C20 C41 C42'.split()
    types = {key: 'ApplicationComponent' for key in component_keys}
    types.update(C13='ApplicationCollaboration', C28='ApplicationFunction', C40='ApplicationFunction', C29='ApplicationInterface', C06='DataObject', C35='DataObject', C07='DataObject', C44='DataObject')
    # Explicit source-established correspondences to the already identified
    # logical Pragma and Pylai.Diadromí records. Source C codes remain locators.
    crosswalk = {'C07': '27ad143e7c784e378556af8a57c438f7', 'C44': '978f5c2bd2d343ffaf3a13dff700c258'}
    for key, native in types.items():
        match = re.search(r'^### ' + key + r' — (.+)$', text, re.M)
        c.require(match is not None, 'Missing exact component-register section: ' + key)
        work.put(source, match.group(0), native, match.group(1), section(source, match.group(0), 3),
                 standing='Candidate — accepted Step-B baseline; final Solution Architecture unapproved', existing=crosswalk.get(key))
    work.finish()


if __name__ == '__main__':
    main()
