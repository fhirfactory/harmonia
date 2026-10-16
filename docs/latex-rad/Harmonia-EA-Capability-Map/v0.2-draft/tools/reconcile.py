#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reconcile every v0.3 primary concept and captured FN/SV/PR occurrence.

Old working IDs are derivation locators, not aliases of newly allocated identities.
The decisions below are semantic dispositions, not a mass import of the candidate.
"""
import collections
import hashlib
import json
import re
import sys
from pathlib import Path

BASE = Path(__file__).resolve().parents[1]
ROOT = BASE.parents[3]
MODEL = json.loads((BASE/'model.json').read_text())
E = {e['key']:e for e in MODEL['elements']}
V03 = ROOT/'docs/markdown/scratchpad/capability-reference-candidate/Harmonia-Capability-Reference-Candidate-v0.3.tex'
TEXT = V03.read_text()


def normal(text):
    return re.sub(r'[^a-z0-9]', '', text.lower())


def name(text):
    return text.replace(r'\&','&').replace(' — Harmonia-Relevant','').replace(' — ADJUDICATED','').strip()


def lookup(value, category=None, typ=None):
    matches = [e for e in E.values() if
               any(normal(value)==normal(n or '') for n in [e['name'],e.get('canonical_name'),e['reference_id']]+e.get('name_aliases',[]))
               and (not category or e['category']==category) and (not typ or e['type']==typ)]
    # Preserve prior Stage-2 derivation meaning when a reviewed homonym has a
    # different responsibility. Its pending/historical disposition is explicit.
    predecessors = [e for e in matches if not e['key'].startswith('reviewed:')]
    if predecessors: matches = predecessors
    return matches[0]['key'] if len(matches)==1 else None


def key(value, category=None, typ=None):
    result = lookup(value,category,typ)
    assert result, ('Explicit reconciliation target',value,category,typ)
    return result


BC_REVIEW = {
 'Health Product Management': ('Reclassified scope', ['Health Product Administration'], 'The candidate business frame mixes product stewardship with representation. Product definitions, formularies and trace context are explicitly placed in BEC; procurement/supply-chain authority remains external and is not silently reassigned.'),
 'Information Model Governance': ('Reconciled to Canonical', ['Information Model Governance Oversight'], 'Current canonical oversight wording distinguishes governance effectiveness from performing the model discovery/adoption/education/compliance peers.'),
 'Standards Discovery': ('Not retained as a separate responsibility', ['Health Information Standards Governance'], 'The old generic label adds no described responsibility beyond the current bounded standards-governance concern. The approved RAD deliberately does not establish this conventional peer; do not equate it to model discovery.'),
 'Standards Education': ('Not retained as a separate responsibility', ['Health Information Standards Governance'], 'Generic standards education was mechanically proposed without a material Harmonia responsibility delta; current model-education responsibility is narrower and is not declared equivalent.'),
 'Standards Bodies Engagement': ('Reclassified participation', ['Health Information Standards Governance'], 'Engaging standards authorities is a participation mode within governance rather than a separately described capability outcome. External standards authority remains external.'),
 'Standards Compliance': ('Reclassified concern', ['Health Information Standards Governance'], 'General conformity is relevant but the old generic prose supplies no separate outcome beyond the parent. Model Compliance remains specifically Canonical; no broader equivalence is asserted.'),
 'Standards-Based Interoperability Exploration': ('Reclassified concern', ['Health Information Exchange'], 'Exploration concerns organisational exchange evolution, not another demonstrated operating ability. Preserve as derivation evidence within the established BC scope.'),
 'Standards-Based Interoperability Support': ('Reclassified enablement', ['Health Information Exchange Enablement'], 'Reusable operational support belongs in the independently meaningful BEC exchange responsibility rather than a product-shaped BC child.'),
 'Standards-Based Interoperability Publication': ('Reclassified enablement', ['Health Information Communication'], 'External publication is retained within standards-governed boundary exchange; it does not become a second business capability merely by naming the interface activity.'),
 'Standards-Based Interoperability Client-Support': ('Reclassified enablement', ['Health Information Exchange Enablement'], 'Client mediation is retained within the exchange contract; the old label does not describe a distinct organisational outcome.'),
 'Health Information Sharing Framework Oversight': ('Reconciled concern', ['Health Information Sharing Programme Management'], 'Programme oversight is retained as a plausible aspect of the Candidate sharing-programme concern. Its proposed placement and description remain low confidence, not an approved equivalence.'),
 'Health Information Sharing Client-Support': ('Reclassified enablement', ['Health Information Exchange Enablement'], 'The exchange support contract carries the useful operational concern; it does not imply a new governing business authority.'),
 'Health Information Sharing Release Management': ('Reclassified change concern', ['Health Information Sharing Programme Management'], 'Release/change context is retained in the Candidate programme concern; exact governance authority and membership remain for review.'),
 'Clinical Message Conveyance': ('Reclassified enablement', ['Clinical Communication Administration'], 'Directed clinical communication is modelled in its bounded BEC rather than duplicating the organisational Digital Communication Channel.'),
 'Health Event Notification': ('Reclassified enablement', ['Health Information Exchange Enablement', 'Population Health Notification Enablement'], 'Generic syndication and required population notifications have different contract semantics; the former BC label is decomposed by purpose rather than copied.'),
 'Health Service Collaboration': ('Reclassified enablement', ['Clinical Collaboration','General Collaboration'], 'Clinical-context collaboration and operational discussion remain distinct BEC responsibilities supporting the business channel; no conversational clinical authority is inferred.'),
 'Domain Health Information Services': ('Rejected implementation-shaped grouping', ['Healthcare Entity Information Enablement','Governed Health Information Enablement'], 'A directory/MPI/register/LHR service list mixes clinical domain authority and platform realisation. The useful information responsibilities are placed by meaning, not retained as an artificial BC family.'),
 'Provider Directory Services': ('Reclassified enablement', ['Provider Administration','Provider Directory Discovery & Resolution'], 'Registry content and provider discovery are explicit; this does not allocate employment, credential-issuing or privilege authority.'),
 'Enterprise Master Patient Index Services': ('Retained external/roadmap concern', ['Person Identity'], 'R1/R2 supports source-attributed identifier correlation and corrections. Master-person reconciliation and probabilistic merge/unmerge remain uncommitted R3/external responsibilities, not a proposed expansion of the Canonical identity boundary.'),
 'Immunisation Register Services': ('Reclassified integration concern', ['Preventive Care'], 'Immunisation submission and qualified history are explicit BEC behaviour; the originating national register remains separately authoritative.'),
 'Longitudinal Health Record Services': ('Reclassified enablement', ['Patient Clinical Record'], 'Longitudinal synthesis and preservation have a Canonical BEC responsibility; a service-shaped BC would duplicate that architecture at the wrong construct.'),
 'Collaboration Services': ('Reclassified enablement', ['Clinical Collaboration','General Collaboration'], 'The clinical and general collaboration responsibilities retain different context and metadata boundaries; no generic service-shaped BC is needed.')
}

OLD_ROOTS = {
 'Entity Information Enablement':['Entity Information Enablement'],
 'Care Administration Enablement':['Care Administration Enablement'],
 'Clinical Service Context Enablement':['Clinical Service Dependency Management'],
 'Health Service Operations Enablement':['Health Service Operations Enablement'],
 'Shared Information and Coordination Enablement':['Shared Information & Coordination Enablement','Health Informatics Management','Independent Assurance Enablement']
}
OLD_RENAMES = {'HIE Integration Services':'Health Information Exchange Enablement', 'Clinical Credential Management':'Clinical Qualification Management'}
CONTEXT = {}
for c in MODEL['catalogue_contexts']:
    resolution = next(r for r in MODEL['catalogue_context_resolutions'] if r['context']==c['key'])
    CONTEXT[normal(c['name'])] = resolution['element']

# Scoped proposals where v0.3 exposed a useful delta. The other 85 generic label
# families receive an explicit disposition below; identity similarity is never
# used to declare a canonical Feature equivalence.
REFINED_FEATURES = {
 'Identity Correction':'FEAT-EM-04',
 'Identity Correlation':'FEAT-EM-02',
 'Practitioner Discovery':'Provider Directory Discovery & Resolution',
 'Device Provenance Tracking':'Device Association Lineage',
 'Referral Routing':'Referral Destination Coordination',
 'Encounter History Retrieval':'Encounter History Retrieval',
 'Care Goal Association':'Care Goal & Plan Progression Representation',
 'Care Plan Lifecycle Tracking':'Care Goal & Plan Progression Representation',
 'Record Provenance Management':'Information Transformation Lineage',
 'Decision Traceability':'Mandatory Significant Activity Audit Capture'
}

PATTERNS = [
 (['Identity Management','Identity Correlation','Identity Correction','Identity Resolution','Location Identity Management','Device Identity Management','Subject Identity Correlation','Specimen Identity Management','Medication Identity Resolution'],
  'Identity/identifier behaviour must remain scoped to the actual subject and source authority. Generic identity wording does not establish a second identity owner or master reconciliation engine.', ['EC-01','EC-05']),
 (['Relationship Management','Professional Relationship Management','Organisational Relationship Management','Organisational Structure Management','Location Hierarchy Management','Device Relationship Management','Subject Relationship Management','Specimen Relationship Management','Qualification Association'],
  'The useful qualified relationship/structure concern is retained in its placed capability and existing explicit Features. Repeating the generic label under every entity kind would obscure distinct associations and approval mandates.', ['EC-01','EC-03']),
 (['Identity Discovery','Practitioner Discovery','Organisation Discovery','Location Discovery','Service Discovery','Discovery','Subject Information Discovery','Record Discovery','Information Retrieval','Care Plan Context Retrieval'],
  'Discovery/retrieval is retained as a reusable contribution and locally exposed behaviour where evidenced. A shared retrieval word does not justify another domain-specific Feature in each neighbouring capability.', ['EC-05','EC-06']),
 (['Context Establishment','Care Context Association','Participant Association','Communication Context Management','Subject Context Management','Record Context Association'],
  'Context is retained in the actual qualified participant, transaction or activity responsibility. The old generic description provides no independent semantic delta warranting another local Feature.', ['EC-02']),
 (['State Monitoring','State Tracking','Lifecycle Management','Encounter Lifecycle Management','Request Lifecycle Management','Appointment Lifecycle Management','Referral Lifecycle Management','Record Lifecycle Management','Information Lifecycle Management','Device State Management','Location Availability Management','Service Availability Management'],
  'The capability and its explicit lifecycle Features retain state/temporal meaning. Reuse of state machinery does not make all subjects share one lifecycle or grant operational capacity authority to a directory description.', ['EC-03','EC-04']),
 (['Exception Management','Exception Reporting','Appointment Exception Management','Request Exception Management','Outcome Reporting'],
  'Exception/outcome handling is retained in the applicable transaction or work contract. The old generic label did not describe a separate capability-specific state change; uncertain effects remain explicit and do not become success, failure or clinical outcome by default.', ['EC-12','EC-10']),
 (['Criteria Management','Policy Evaluation','Evidence Association','Eligibility Validation','Service Eligibility Management','Availability Validation','Medication Safety Context Validation'],
  'Policy, eligibility, qualification and assurance evidence are different concerns. Their useful participation is retained without copying one generic policy Feature into every clinical setting or granting clinical safety/acceptance decisions to Harmonia.', ['EC-06','EC-02']),
 (['Service Definition Management','Service Definition Management'],
  'Definition management belongs to service-definition responsibility; it is not reproduced beneath service mapping, availability, eligibility and diagnostic execution contexts.', ['EC-01','EC-13']),
 (['Referral Status Tracking','Referral Outcome Association','Request Fulfilment Tracking','Request Status Correlation','Diagnostic Result Association','Diagnostic Status Tracking','Diagnostic Request Correlation','Medication Activity Tracking','Medication Information Correlation','Communication Delivery Tracking'],
  'Correlation/progression is retained in the appropriate referral, order, report, medication or communication responsibility. Technical ACK, disposition, fulfilment and outcome retain distinct meanings; the generic candidate is not a new universal status Feature.', ['EC-03','EC-08','EC-10']),
 (['Assignment & Routing','Participant Coordination','Recipient Resolution'],
  'Coordination uses verified participant context and the assigned routing/dispatch contract. It does not establish clinical work allocation, practitioner privilege or a central dispatch authority from shared vocabulary.', ['EC-08','EC-10']),
 (['Diagnostic Provenance Capture','Device Provenance Tracking','Information Provenance'],
  'Meaningful attribution is retained through lineage/audit participation under its actual owner; diagnostic origin and source authority are not transferred to an exchange or terminology capability.', ['EC-07']),
 (['Specimen Custody Tracking'],
  'The established specimen transport Feature already preserves the useful courier/custody concern. Payload tracking is not specimen analysis or transfer of diagnostic-result authority.', ['EC-01','EC-07']),
 (['Information Validation','Information Correlation'],
  'Semantic validation, terminology mapping and information correlation remain distinct governed behaviours. Their recurrence supports conformance/context contributions, not automatic replication of a generic Feature under every information capability.', ['EC-13','EC-02'])
]
FAMILY = {normal(n):(reason,targets) for ns,reason,targets in PATTERNS for n in ns}
EC_FAMILIES = {
 'Entity and Identity':['Governed Information Operations'],
 'Identity and Correlation':['EC-01'], 'Identity and Correlation Enablement':['EC-01'],
 'Relationship and Structure':['EC-01'], 'Relationship and Structure Enablement':['EC-01'],
 'Information and Semantics':['Governed Information Operations'],
 'Information Lifecycle':['EC-03','EC-04'], 'Information Lifecycle Enablement':['EC-03','EC-04'],
 'Discovery and Retrieval':['EC-05'], 'Discovery and Retrieval Enablement':['EC-05'],
 'Semantic Conformance':['EC-13'], 'Semantic Conformance Enablement':['EC-13'],
 'Context, Activity and Exchange':['Coordinated Activity & Interaction','EC-02'],
 'Context and State':['EC-02','EC-03'], 'Context and State Enablement':['EC-02','EC-03'],
 'Activity Coordination':['EC-10'], 'Activity Coordination Enablement':['EC-10'],
 'Governance, Trust and Assurance':['Trust, Accountability & Assurance'],
 'Governed Control':['EC-06'], 'Governed Control Enablement':['EC-06'],
 'Provenance and Accountability':['EC-07'], 'Provenance and Accountability Enablement':['EC-07'],
 'Assurance and Resilience':['EC-12','EC-14'], 'Assurance and Resilience Enablement':['EC-12','EC-14'],
 'Existing Enterprise Capabilities -- Retained for Reconciliation':['Governed Information Operations','Coordinated Activity & Interaction','Trust, Accountability & Assurance']
}


def reconcile(item):
    title = item['name']; kind = item['category']; path = item['path']
    if kind=='business':
        match = lookup(title,'business')
        if match:
            status = E[match]['architectural_status']
            return ('Retained Canonical' if status=='Canonical' else 'Retained '+status,
                    [match], 'The current v0.1 identity/semantic boundary takes precedence over the old working ID and generic candidate description. Placement/extra description is separately Proposed where applicable.')
        assert title in BC_REVIEW, ('Unreviewed BC concept',title)
        decision,targets,reason = BC_REVIEW[title]
        resolved=[]
        for target in targets:
            # BC Exchange is selected explicitly for the organisational concern.
            category='business' if target in ('Health Information Exchange','Health Information Standards Governance','Health Information Sharing Programme Management','Information Model Governance Oversight') else None
            resolved.append(key(target,category))
        return decision,resolved,reason
    if kind=='business_enabling' and item['construct']!='Feature':
        if title in OLD_ROOTS:
            targets=[key(t,'business_enabling') for t in OLD_ROOTS[title]]
            if title=='Clinical Service Context Enablement':
                targets += [r['key'] for r in MODEL['reviewed_bec_reconciliation']['service_context_relocations']]
            return 'Reconciled to human-reviewed hierarchy',targets,'The explicit reviewed candidate replaces the former grouping. Dependency tracking is distinct from surviving care-information and coordination responsibilities, which are separately relocated. This is a bounded reconciliation, not a fresh hierarchy synthesis.'
        title=OLD_RENAMES.get(title,title)
        match=lookup(title,'business_enabling') or CONTEXT.get(normal(title))
        assert match,('Unreviewed BEC capability',title)
        element=E[match]
        if element['record_state']=='pending_adjudication':
            return 'HUMAN ADJUDICATION REQUIRED',[match],element['adjudication_note']
        if element['record_state']=='superseded':
            decision=next(r for r in MODEL['reviewed_bec_reconciliation']['decisions'] if r['reference_id']==element['reference_id'])
            return 'Reconciled predecessor — '+decision['classification'],decision['targets'],decision.get('reason') or 'Predecessor semantics are explicitly distributed to the reviewed successors; the original identity and exact pre-review assertions remain in history.'
        return ('Retained Canonical responsibility' if E[match]['architectural_status']=='Canonical' else 'Retained Proposed responsibility',
                [match], 'The responsibility survives under its explicit reviewed placement or retained meaningful CT3 frame. Original identity and semantics are preserved; reviewed structural relationships remain Proposed.')
    if kind=='enterprise' and item['construct']!='Feature':
        ec=re.match(r'EC-\d+',title)
        if ec:
            return 'Retained Canonical',[key(ec[0])],'Preserve EC-01..EC-14 identities and exact responsibilities, placing them in the integrated proposed hierarchy rather than a coexistence branch.'
        assert title in EC_FAMILIES,('Unreviewed EC grouping',title)
        return 'Integrated reusable responsibility',[key(t,'enterprise') for t in EC_FAMILIES[title]],'Reconcile recurrence with established EC semantics. Correlation is a property within the appropriate responsibility; operational resilience and independent assurance remain separate.'
    if item['construct']=='Feature':
        token=re.search(r'FEAT-(?:EM|SA|SD|HSO|ISE)-\d+',item['body'])
        if token and kind=='business_enabling':
            return 'Retained Canonical',[key(token[0])],'Use the stable v0.1 Feature Reference ID, label and governed definition; old candidate parentage is not authority.'
        if title.lower().startswith('no established feature decomposition'):
            return 'Removed non-element placeholder',[], 'A statement that no Feature was captured is not a Feature. The product context remains explicitly identified and placed without manufacturing symmetric behaviour.'
        context=normal(path[-2].split(' - ',1)[-1]) if len(path)>1 else ''
        frame=CONTEXT.get(context)
        # Refine only where the context actually fits the proposed behaviour.
        proposal=REFINED_FEATURES.get(title)
        if proposal:
            p=key(proposal)
            if title not in ('Identity Correlation','Identity Correction','Device Provenance Tracking','Practitioner Discovery') or context in {
                'personidentity','deviceassociation','practitioner','practitionerrole'}:
                return 'Refined / repositioned',[p], 'Retain the useful semantic delta under a specific identified Feature and boundary; this is derivation provenance rather than an old-ID alias or automatic equivalence.'
        assert normal(title) in FAMILY,('Unreviewed Feature family',title,context)
        reason,targets=FAMILY[normal(title)]
        resolved=[key(t,'enterprise') for t in targets]
        if frame: resolved.insert(0,frame)
        return ('Reclassified recurrence evidence' if kind=='enterprise' else 'Subsumed concern; no distinct Feature delta', resolved,
                reason + (' Enterprise reuse is represented by EC responsibilities; the metamodel frames Harmonia Features in BEC CT3s, not beneath ECs.' if kind=='enterprise' else ' Target capability/reuse participation is not a claim of Feature equivalence.'))
    raise AssertionError(item)


def main():
    previous=json.loads((BASE/'pre-review-v0.3-reconciliation.json').read_text())
    previous_by_id={r['candidate_reference_id']:r for r in previous['primary_concepts']}
    pattern=re.compile(r'\\(chapter|section|subsection|subsubsection)\{((?:\\.|[^{}])*)\}\\label\{([^}]+)\}')
    matches=list(pattern.finditer(TEXT)); chain={}; records=[]
    for i,m in enumerate(matches):
        body=TEXT[m.end():matches[i+1].start() if i+1<len(matches) else len(TEXT)]
        rid=re.search(r'\\textbf\{Reference ID:\} \\texttt\{([^}]+)\}',body)
        if not rid: continue
        heading=name(m[2]); level=['chapter','section','subsection','subsubsection'].index(m[1])
        chain[level]=heading
        for j in list(chain):
            if j>level:del chain[j]
        category={'bc':'business','bec':'business_enabling','ec':'enterprise'}[m[3].split(':')[1].split('-')[0]]
        construct,title=heading.split(' - ',1)
        record={'candidate_reference_id':rid[1], 'candidate_label':m[3], 'line':TEXT[:m.start()].count('\n')+1,
                'category':category,'construct':construct,'name':title,'path':list(chain.values()),'body':body}
        disposition,targets,rationale=reconcile(record)
        record.pop('body')
        record.update(disposition=disposition,targets=targets,rationale=rationale,decision_status='Proposed review disposition')
        record['previous_decision_locator']={'file':'pre-review-v0.3-reconciliation.json','candidate_reference_id':record['candidate_reference_id']}
        assert record['candidate_reference_id'] in previous_by_id
        records.append(record)
    # Captured behaviour identities were never independent candidate definitions;
    # reconcile exact type/name to the canonical record, not adjacent table order.
    behaviours=[]
    pattern=re.compile(r'(Function|Service|Process) & \\texttt\{([^}]+)\} & (.*?) & ([^\n]*)')
    for m in pattern.finditer(TEXT):
        typ=m[1].lower(); n=name(m[3]); target=lookup(n,typ=typ)
        if not target:
            assert 'unresolved / not established' in n.lower(), ('Unreviewed captured behaviour',typ,n)
            behaviours.append({'candidate_reference_id':m[2],'name':n,'type':typ,'line':TEXT[:m.start()].count('\n')+1,
                               'target':None,'disposition':'Removed non-element association placeholder',
                               'rationale':'An unresolved association notice is not a Function, Service or Process identity; baseline unresolved associations remain separately recorded.'})
            continue
        behaviours.append({'candidate_reference_id':m[2],'name':n,'type':typ,'line':TEXT[:m.start()].count('\n')+1,
                           'target':target,'disposition':'Retained Canonical identity; candidate table pairing not imported',
                           'rationale':'The exact v0.1 definition and owning responsibility prevail. Adjacency in the old Feature table does not establish a Function/Service/Process or Feature association.'})
    report={'source':str(V03.relative_to(ROOT)),'sha256':hashlib.sha256(V03.read_bytes()).hexdigest(),
            'primary_concepts':records,'captured_behaviour_occurrences':behaviours,
            'summary':dict(collections.Counter(r['disposition'] for r in records)),
            'unreviewed':0,'candidate_identifiers_are_aliases':False}
    report['previous_reconciliation']={'file':'pre-review-v0.3-reconciliation.json',
        'sha256':hashlib.sha256((BASE/'pre-review-v0.3-reconciliation.json').read_bytes()).hexdigest(),
        'role':'Exact original Stage-2 derivation/reconciliation history retained unchanged'}
    text=json.dumps(report,indent=2,ensure_ascii=False)+'\n';p=BASE/'v0.3-reconciliation.json'
    if '--check' in sys.argv:assert p.read_text()==text,'v0.3 reconciliation drift'
    else:p.write_text(text)
    reverse={}
    for r in records:
        for target in r['targets']:
            reverse.setdefault(E[target]['reference_id'],[]).append({
                'candidate_reference_id':r['candidate_reference_id'],'line':r['line'],
                'disposition':r['disposition'],'rationale':r['rationale'],
                'role':'Derivation/reconciliation provenance; not an identity alias or automatic equivalence'})
    text=json.dumps(reverse,indent=2,ensure_ascii=False)+'\n';p=BASE/'derivation-provenance.json'
    if '--check' in sys.argv:assert p.read_text()==text,'Reverse derivation provenance drift'
    else:p.write_text(text)
    print('Reconciled',len(records),'primary concepts and',len(behaviours),'behaviour occurrences')


if __name__=='__main__':main()
