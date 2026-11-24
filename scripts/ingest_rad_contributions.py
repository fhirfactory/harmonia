#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""RAD-1.1.5 explicit semantic contributions, using reviewed source bindings.

This bounded adapter is not a heading-to-element classifier. All owners below
are exact previously governed records or explicit C-register bindings. References
to another element in a responsibility definition do not allocate another identity.
"""
import re
import architecture_continuum as c
from ingest_rad_continuum import section, EVIDENCE
from ingest_rad_definitions import Ingestion

D4 = 'docs/markdown/04-information-architecture/'
D5 = 'docs/markdown/05-information-systems-architecture/'
CANDIDATE = 'Candidate — adjudicated Domain05 Step-B baseline; final Solution Architecture unapproved'


def main():
    w = Ingestion()
    register = D5 + 'component-register.md'
    owners = {}
    for (src, selector), cid in w.bindings.items():
        if src == register:
            match = re.match(r'### (C\d+) —', selector)
            if match: owners[match[1]] = cid
    ia = {x['baseName']: x['canonicalId'] for x in c.read_json(c.ROOT / 'docs/markdown/governance/baseline/information-architecture.json')['elements']}
    coverage = []

    def contribution(source, heading, owner, standing=CANDIDATE, text=None, selector=None):
        source = str(source)
        cid = owners.get(owner, ia.get(owner, owner))
        m = w.elements[cid][0]
        w.put(source, selector or heading, m['archimateType'], m['displayName'],
              section(source, heading, len(heading.split(' ')[0])) if text is None else text,
              existing=cid, standing=standing)
        coverage.append({'source': source, 'selector': selector or heading, 'canonicalId': cid, 'completeSection': text is None})
        return cid

    def grouped(relative, mapping, standing=CANDIDATE):
        source = (D4 if relative.startswith('IA:') else D5) + relative.removeprefix('IA:')
        headings = re.findall(r'^## .+$', (c.ROOT / source).read_text(), re.M)
        c.require(len(headings) == len(mapping), 'Explicit section mapping drift: ' + source)
        for heading, owner in zip(headings, mapping):
            if owner is not None: contribution(source, heading, owner, standing)

    # Cross-source same-instance correspondences explicitly established by these
    # responsibility views and their C/IA records, not by lexical similarity.
    grouped('context-and-persistence-management.md', ['C09','Context','C23','C19','Pragma','C40','C12','C12','C12','C09','C01'])
    grouped('calliope-models-and-datasets.md', ['C01','C41','C42','C01','C01','C01'])
    grouped('agora-omilia-collaboration.md', ['C18','C47','C18','C18','C18','C18','C18','C18'])
    grouped('sylloge-binary-content-management.md', ['C49'] * 5)
    # Unclassified Pathway/Horos/control facets retain their own source sections.
    # They are not smuggled into a catch-all component file.
    grouped('pathways-journeys-and-handoff.md', [None,'C06','Pragma',None,'C28','Pragma','C10','C10','C10','C35','C35'])
    grouped('pylai-external-interoperability.md', [None,None,'Pylai.Diadromí',None,None,None,None,None,None,'C14','C14'])
    # Explicit Services are behaviour surfaces, not the component which offers them.
    source = D5 + 'context-and-persistence-management.md'
    text = section(source, '## Mneme — Context and Pragma Manager', 2)
    for match in re.finditer(r'^\| \*\*(Context Load|Context Save|Adhoc Resource Access|Adhoc Search)\*\* \| (.+) \|$', text, re.M):
        name, definition = match.groups()
        w.put(source, match.group(0), 'ApplicationService', name,
              definition + '\n\nMneme exposes this principal service. Resource Harmonisation Manager remains internal, and supporting contracts are not additional exposed services. The Context/Pragma and authoritative-state boundaries apply.', standing=CANDIDATE)
    source = D5 + 'pylai-external-interoperability.md'; heading = '## External Interoperability Service'
    sid = w.put(source, heading, 'ApplicationService', 'External Interoperability Service', section(source, heading, 2), standing=CANDIDATE)
    coverage.append({'source': source, 'selector': heading, 'canonicalId': sid, 'completeSection': True})
    source = D5 + 'agora-omilia-collaboration.md'; heading = '## Application Services API'
    api = w.put(source, heading + ' / explicitly exposed interface', 'ApplicationInterface', 'Omilia Application Services API', section(source, heading, 2), standing=CANDIDATE)
    # Separately defined logical security information; no Topic classification or
    # BusinessRole equivalence follows from their names.
    for heading, name in [
        ('#### Criterion conjunction and additive grants', 'Criterion'),
        ('#### Criteria containment, AuthorisationRoles and Identity assignments', 'AuthorisationRole'),
        ('#### PractitionerCapacity and operational responsibility', 'PractitionerCapacity'),
        ('#### PEP-Context and distributed enforcement', 'PEP-Context')]:
        w.put(register, heading, 'DataObject', name, section(register, heading, 4), standing=CANDIDATE)
    heading = '#### PEP-Context and distributed enforcement'
    content = section(register, heading, 4)
    start = content.index('**PEP-Criteria is a precomputed')
    end = content.index('\n```', start)
    w.put(register, heading + ' / PEP-Criteria definition', 'DataObject', 'PEP-Criteria', content[start:end] + '\n\nThemis derives this information; PEP-Context supplies it to constrained local enforcement. It is not a general-purpose policy language or a persisted resource by default.', standing=CANDIDATE)
    # Whole, source-qualified concepts. Preserve the accepted retyping of earlier
    # IA records; do not allocate BusinessObject duplicates for those records.
    grouped('IA:patterns/metabole.md', ['Metabolē'] * 5, 'Adjudicated conceptual Information Architecture; original qualifications retained')
    grouped('IA:patterns/search-results.md', ['Search Result Set','Search Result Set','C23','Search Result Set','Search Result Set'], 'Adjudicated search boundary; exact source qualifications retained')
    source = D4 + 'metamodel/information-architecture-metamodel.md'
    for heading in re.findall(r'^## (?:[5-9]|10|11)\. .+$', (c.ROOT / source).read_text(), re.M):
        contribution(source, heading, 'Information Unit', 'Established Information Unit boundary; unresolved downstream realisation preserved')
    grouped('IA:governance/information-lifecycle.md', ['Information Unit'] * 4, 'Canonical information governance; concept-specific lifecycles retained')
    source = D4 + 'governance/authority-custody-provenance.md'; heading = '## 1. Information Assertion'
    assertion = w.put(source, heading, 'BusinessObject', 'Information Assertion', section(source, heading, 2), standing='Canonical conceptual information definition')
    coverage.append({'source': source, 'selector': heading, 'canonicalId': assertion, 'completeSection': True})
    for heading in re.findall(r'^## [2-6]\. .+$', (c.ROOT / source).read_text(), re.M):
        contribution(source, heading, 'Information Unit' if heading.startswith('## 6.') else assertion, 'Canonical information governance; originating authority preserved')
    # Retain architectural runtime constraints on the Information Unit, distinct
    # from metamodel rules which continue as governance.
    grouped('IA:guardrails/observable-information-and-domain-meaning.md', ['Information Unit','Information Unit','C23','C23'], 'Adjudicated observable-information boundary; runtime AI optional')
    # U18/U19 are themselves the retained adjudicated semantic contracts; unlike
    # the older working DesignNotes, they contain authoritative additional detail.
    grouped('design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md',
            [None,'C19','C25','C19','C19','C19','C19','C25','C25','C25','C25','C19',None,'C19'],
            'Agreed A4.7 decisions U19; whole-candidate acceptance and deferred mechanisms unchanged')
    grouped('design-notes/DesignNote-Pragma-Outputs-and-Pathway-Continuation.md',
            [None,'Pragma','C06','Pragma','Pragma','C35','Pragma','Pragma','Pragma'],
            'Architecturally adjudicated U18; candidate scope and representation deferrals retained')
    # Every key maps an inspected responsibility assertion to its explicitly
    # identified subject. None becomes a new Feature or a typed relationship.
    table_maps = {
      'candidate-feature-map.md': {
        **dict(zip(range(1,28), ['C41','C01','C02','C04','C04','C39','C23','C23','C23','C23','C39','C06','C10','C10','C03','C09','C09','C09','C12','C13','C14','C18','C47','C18','C42','C15','C39'])),
        31:'C49',32:'C19',33:'C20',34:'C28',35:'C27',37:'C28',38:'C35',39:'C39',40:'C39',41:'C40',42:'C12',43:'C12',44:'C12',45:'C42',47:'C14'},
      'data-and-persistence.md': {3:'C02',4:'C09',5:'Pragma',6:'C06',7:'C01',8:'C13',9:'C14',10:'C10',11:'PetasosParcel',12:'C03',13:'C39',14:'C18',15:'C49',16:'C01',17:'C19',18:'C19',19:'C26',21:'C35',22:'C28',23:'C28',24:'C28',25:'Information Unit',26:'Context',27:'C23',28:'Pragma',29:'C12',30:'C12'},
      'middleware.md': dict(zip(range(1,20), ['C10','C10','C04','C23','C09','C01','C02','C18','C39','C28','C19','C20','C28',None,'C35','C12','C40','C12','C12']))}
    for filename, mapping in table_maps.items():
        source = D5 + filename
        for line in (c.ROOT / source).read_text().splitlines():
            match = re.match(r'^\| ([FDM])(\d+) \|', line)
            if not match: continue
            number = int(match[2]); owner = mapping.get(number)
            if filename == 'candidate-feature-map.md' and number == 46:
                owner = 'C47' if 'Omilia collaboration operations' in line else 'C01'
            if owner:
                contribution(source, line, owner, text=line, selector='table assertion: ' + line)
    # Source-level assertion tables remain qualified contributions to explicitly
    # bound component owners. No F/D/M row becomes a new element or Strategy Feature.
    # Exact G2-D02/D03/D01 and existing Order correspondences. Other G2
    # candidates cannot be equated to later authored Topics by matching labels.
    for filename, owner, principal in [
      ('appointment-scheduling.md','Appointment','### 3.1 Appointment'),
      ('episode-encounter.md','Encounter','### 3.1 Encounter'),
      ('order.md','Order','### 3.1 Order')]:
        source = D4 + 'information-families/' + filename
        text = (c.ROOT / source).read_text()
        heading = next(h for h in re.findall(r'^### .+$', text, re.M) if h.startswith(principal))
        contribution(source,heading,owner,'G2 candidate contribution with only the explicitly recorded decisions approved; semantic classifications and residual questions remain qualified')
        for heading in re.findall(r'^## (?:1|6|8)\. .+$',text,re.M):
            contribution(source,heading,owner,'G2 source-qualified boundary and unresolved questions; no whole-family approval')
    source = D4 + 'information-families/episode-encounter.md'
    contribution(source,'### 3.5 Episode — APPROVED (G2-D01)','Episode','Approved G2-D01 meaning only; remaining family review open')
    w.finish()
    path = c.ROOT / EVIDENCE / 'after/section-correspondence.json'
    old = c.read_json(path) if path.exists() else []
    pairs = {(x['source'],x['selector'],x['canonicalId']):x for x in old + coverage}
    path.write_bytes(c.encoded(list(pairs.values())))
    print('Explicit whole-section correspondences:', sum(x['completeSection'] for x in pairs.values()))

if __name__ == '__main__': main()
