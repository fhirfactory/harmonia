#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Prepare explicitly scoped CR proposals; never allocate architectural IDs."""
import json
from pathlib import Path
ROOT=Path.cwd()
OUT=ROOT/'enterprise-architecture/design/evidence/1.1.18/001/after/bounded-subject-review'
REVIEW=ROOT/'enterprise-architecture/design/review/1.1.18/change-review.md'
data=json.loads((OUT/'scoped-subject-evidence.json').read_text())
assert (OUT/'scoped-subject-evidence.json').stat().st_size<=128000
assert len(data['folders'])==32 and not data['submittedDirectIncidentRelationships']
assert not data['scopedNativeElementIds']
assert '## CR-' not in REVIEW.read_text(),'Existing CRs require explicit bounded revision; never overwrite human dispositions'
rows=data['folders'];policy=data['uniformFolderAndProposalPolicy']
evidence='../../evidence/1.1.18/001/after/bounded-subject-review/scoped-subject-evidence.json'
task='../../evidence/1.1.18/001/before/ai-agent-tasks/unresolved-bounded-subject-interpretation.md'
previous='../../evidence/1.1.18/001/before/bounded-subject-review/change-review.md'
mapping='../../evidence/1.1.18/001/before/Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md'
crs=[];bycode={}
for row in rows:
    number=len(crs)+1;cid=f'CR-{number:03}';bycode[row['code']]=cid
    crs.append(dict(id=cid,kind='Subject instance proposal',code=row['code'],title='Propose '+row['code']+' '+row['mappingName'],row=row,dependencies=[],disposition='On-Hold',outcome='Unresolved',instruction='Not authorised',processing='Not yet processed'))
for edge in data['proposedSubjectContainment']:
    cid=f'CR-{len(crs)+1:03}'
    crs.append(dict(id=cid,kind='Subject classification relationship proposal',code=edge['childCode'],title='Classify '+edge['childCode']+' beneath '+edge['parentCode'],edge=edge,dependencies=[bycode[edge['parentCode']],bycode[edge['childCode']]],disposition='On-Hold',outcome='Unresolved',instruction='Not authorised',processing='Not yet processed'))
typo=next(row for row in rows if row['code']=='G18.03')
crs.append(dict(id='CR-060',kind='View-navigation label proposal',code='G18.03',title='Correct the G18.03 View-folder spelling',row=typo,dependencies=[],disposition='On-Hold',outcome='Unresolved',instruction='Not authorised',processing='Not yet processed'))
assert len(crs)==60 and all(len(r['title'])<=80 for r in crs)
lines=['| ID | Brief Description | Disposition | Outcome |','| --- | --- | --- | --- |']
lines += [f"| [{r['id']}](#{r['id'].lower()}) | {r['title']} | On-Hold | Unresolved |" for r in crs]
lines += ['', '# Bounded Subject Change Review — 1.1.18 / 001', '',
'**Completed bounded interpretation; all architectural proposals await adjudication.**',
'This replaces the initial scope-gate brief; its [prior version]('+previous+') is preserved. No previously allocated CRs existed. CR-001–CR-060 are new, stable review-local references, not canonical identities.', '',
'## Review basis and authority', '',
'Prepared by Codex under the [explicit bounded-scope instruction]('+task+'). The Solution Architect authorises interpretation of G14–G18, their subgroups and directly incident relationships only. This does not accept any Subject, edge or spelling change. Instruction-entry time is unavailable and remains UNRESOLVED, distinct from report production.', '',
'The canonical comparison is the current 1.1.18 projection at `enterprise-architecture/design/archimate/published/1.1.18/Harmonia-Architecture.xml`; SHA-256 `7df92528a09f9a765ddebec105643bae08ac92583cad4e8901b3135d49db2c2e`. Incoming export 004 is retained at `enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-Continuum-1.1.18-001-004.xml`; SHA-256 `4ddfaa9045d8d0196f1003c9344d85eb2f121398a79a579fbb6c18545470c0de`. Source HEAD is `763f97586685d5c4b9744509b067d95b9694d0db`, plus pinned workspace inputs. Exact baseline reprojection and input pins have been rechecked; no release acceptance is inferred.', '',
'The original full delta remains 3,824,520 bytes and is not interpreted. The complete scoped evidence is **90,051 bytes**, below the unchanged 128,000-byte limit. Repeated uniform policy/context was factored without dropping scoped observations. See [scoped evidence]('+evidence+') and [preflight](../../evidence/1.1.18/001/after/bounded-subject-review/payload-preflight.json). The maintained secure parser, offline schemas, Exchange reference checks, canonical source loader/projector and governed lexical naming function are reused. No import/application adapter or limit waiver was introduced.', '',
'## Observed evidence and candidate interpretation', '',
'Observed: `Views / 1. Subject-Based-Views` contains five new empty numbered folders (14–18) and 27 empty child folders. All 32 have neither `identifier` nor `identifierRef`, canonical identity, native element/type/specialisation, Documentation, Properties, nor View membership references. No corresponding scoped native element is found by explicit names/code/source claims. There are **zero incoming semantic relationships incident to G14–G18 element endpoints**, because the submission supplies no such endpoints. Folder nesting is presentation; it is not Subject Composition, Home or Capability alignment.', '',
'Candidate interpretation: the user extension intent plus the [candidate hierarchy and concern definitions]('+mapping+') support proposing 32 native Grouping Subjects, with null specialisation, applying the existing governed Subject pattern. Native subject instances and 27 classification Composition relationships would be new architectural records **if separately accepted**. They are not imported XML records. Existing 82 G01–G13 source-bound Subjects, their IDs, qualified standing and canonical relationships remain intact. No existing governed G14–G18 code/source binding or exact normalised Grouping-label collision is found. That bounded duplicate search is not proof of universal semantic novelty.', '',
'Ordinal + parent context + matching label/concern establishes only this candidate crosswalk. Neither source codes nor names nor XML folders supply canonical IDs. A later authorised creation must recheck existing identities and allocate UUIDv4 once through governed creation; no prospective ID is allocated here. No Grouping formal-name token exists: all proposed canonicalName values remain null with the recorded reason. Friendly names are local lexical candidates, never subject-ancestor-qualified names. Native Name would equal the separately accepted displayName. Existing Grouping specialisation, Component ownership, Home, RequirementGroup meaning and Capability hierarchy are not changed.', '',
'The only lexical discrepancy beyond punctuation/hyphen formatting is G18.03: submitted `03. Directory-Informaton-Maintenance`, candidate `Directory Information Maintenance`. CR-060 separately proposes correcting the authored empty View-folder label; the immutable export remains unchanged.', '',
'## Decomposition and dependencies', '',
'Each proposed classification Subject has independently reviewable scope/definition and may be accepted, rejected or revised separately; therefore CR-001–CR-032 each address one architectural instance. Each parent/subgroup classification can independently be declined or revised even if both Subjects exist; CR-033–CR-059 separately address those semantic edges and depend on the endpoint proposals. This is architectural decomposition, not conversion of XML-record counts into CRs: the scoped input has zero native Subject/relationship records. CR-060 is an independent authored presentation spelling decision and does not depend on accepting any semantic Subject.', '',
'| Evidence / proposed meaning | CRs | HCRL correspondence |',
'| --- | --- | --- |',
'| Five group and 27 subgroup concepts, with exact mapping concerns and folder locators | CR-001–CR-032 | DR-2.01–DR-2.05 |',
'| Explicit proposed Subject classification parent/child pairs | CR-033–CR-059 | DR-2.01–DR-2.05 |',
'| G18.03 empty View-folder spelling | CR-060 | DR-10 |',
'| Remaining export elements, relationships, Views/navigation, and full delta | None; unadjudicated outside scope | DR-1 exclusion |', '',
'## Preserved state, exclusions and verification', '',
'All existing canonical sources, identities, relationships, View templates, published inputs, original export, release authority and the original 784-element Motivation/Capability assessment are preserved. No Motivation incorporation, Capability relationship, subject-to-statement assignment, arbitrary ancestor edge or release scope is proposed as an accepted result. All other export changes remain unadjudicated. Existing Views and their navigation are preserved; this review does not adopt the rest of the submitted Views tree or its newly added Views. Any future incorporation of the empty folder branches must be separately bounded to preserve other authored presentation, and must not replace the entire maintained template from this export.', '',
'Current canonical schema/provenance/identity/navigation/containment/XSD/reference and exact baseline checks pass; input XML is technically valid. Scope extraction, reference absence, all 32 crosswalk rows, 27 proposal dependencies, original-candidate preservation and review consistency are checked in [verification](../../evidence/1.1.18/001/after/bounded-subject-review/verification.json). A successful check is not architectural acceptance. No actual Archi transport/visual inspection or publication was performed. The prior Java suite remains unexecuted because Maven dependency resolution attempted a write to the read-only local cache; this documentation-only bounded step does not rerun the unchanged command. Task issuance event provenance remains unresolved.', '',
'The common [HCRL](hcrl-1.1.18-001.pending.md) records the scope disposition and material findings. The scope gate is answered; **semantic adjudication and incorporation remain pending**. No commit, push, HCRL completion or release transition has occurred.', '']

for cr in crs:
    lines += [f'<a id="{cr["id"].lower()}"></a>',f'## {cr["id"]} — {cr["title"]}','', '**Disposition:** On-Hold', '', '**Outcome:** Unresolved', '', '**Outcome Reason:** The bounded interpretation is complete; no Solution Architect decision accepts this architectural proposal, identity allocation or application.', '']
    if cr['kind']=='Subject instance proposal':
        row=cr['row'];code=row['code']
        lines += ['**Proposed Architectural Change:** Establish the separately reviewable '+row['role']+' Subject **'+code+' — '+row['mappingName']+'** as native `Grouping`, specialisation null. Concern: '+row['mappingConcern'], '',
        '**Observed Evidence:** Incoming folder `'+ ' / '.join(row['folderPath'])+'`; XML line '+str(row['sourceLine'])+', XPath `'+row['sourceXPath']+'` in the pinned export. Candidate selector `Proposed Subject extensions / '+code+'` in the pinned mapping. This is an empty View folder, with no native semantic instance or incident relationship. No governed code/source binding or exact Grouping-label collision was found. See scoped evidence row `folders[code='+code+']`.', '',
        '**Naming and identity proposal:** displayName `'+row['mappingName']+'`; local friendlyName `'+row['proposedFriendlyName']+'`; canonicalName **null**, because the Grouping formal qualifier remains unestablished. Canonical ID **unset**. The folder/code supplies no identity, semantic parent, specialisation or naming ancestry. Preserve source ID '+code+' only as scoped provenance/classification metadata. Any later authorised native storage uses `continuum/grouping/<allocated UUID>.md`; proposed reading context is `'+ ' / '.join(row['prospectiveNavigation'])+'` without converting navigation into semantic authority.', '',
        '**Architectural Questions / Adjudication:** Is this an independently bounded Subject with the stated concern and exclusions? Approve/revise/reject that concept and its type/name treatment separately. If an existing canonical instance is intended, supply that ID and continuity evidence rather than merging by label. Do not infer workforce, clinical, source-domain or implementation authority from the Subject title.', '',
        '**Solution Architect Decision:** Not recorded. Rationale, decision-linked authority and adjudicator/event time: no architectural disposition supplied. The interpretation authorisation is not this decision.', '',
        '**Related Change Requests:** Subject containment is separately proposed below; acceptance of this instance supplies no edge. '+('Parent instance '+bycode[row['proposedParentCode']]+' is relevant context; its containment CR remains separate.' if row['proposedParentCode'] else 'No semantic parent is proposed for this top-level classification group.'), '']
    elif cr['kind']=='Subject classification relationship proposal':
        edge=cr['edge']
        lines += ['**Proposed Architectural Change:** Native `Composition`, **'+edge['parentCode']+' → '+edge['childCode']+'**, for Subject-subgroup classification only. This is not Home, capability decomposition, implementation ownership or statement assignment.', '',
        '**Observed Evidence:** The candidate mapping explicitly organises '+edge['childCode']+' within '+edge['parentCode']+'. The scoped XML contains only nested empty View folders: **no such semantic edge is submitted**. The candidate proposal applies the established Subject classification pattern in `continuum-representation.md`; folder nesting is not asserted as the source of a native relationship. See `proposedSubjectContainment[childCode='+edge['childCode']+']` in the scoped evidence.', '',
        '**Architectural Questions / Adjudication:** Accept/revise/reject this individual classification relationship, its direction and source-qualified standing. Endpoint canonical IDs and relationship identifier remain **unset**; after any separate acceptance, resolve accepted endpoint identities and allocate only the relationship representation identifier through the applicable governed mechanism. Do not allocate a canonical UUID to a relationship.', '',
        '**Solution Architect Decision:** Not recorded. No architectural adjudicator/date, disposition rationale or executable application authority exists for this edge.', '',
        '**Related Change Requests / prerequisites:** '+', '.join(cr['dependencies'])+' must establish accepted endpoint identities before this proposal can be applied. Their acceptance alone does not accept this edge.', '']
    else:
        row=cr['row']
        lines += ['**Proposed Architectural Change:** Change only the empty authored View-folder label at `Views / 1. Subject-Based-Views / 18. Provider Directory / 03. Directory-Informaton-Maintenance` to `03. Directory-Information-Maintenance` in a separately authorised maintained presentation result. Preserve ordinal, position, siblings and all other View content/navigation. The immutable incoming XML is never edited.', '',
        '**Observed Evidence:** Exact incoming label at XML line '+str(row['sourceLine'])+', XPath `'+row['sourceXPath']+'`; the candidate mapping spells `Directory Information Maintenance`. This is a presentation spelling discrepancy, not a native Subject rename or an identity rebind.', '',
        '**Architectural Questions / Adjudication:** Does the Solution Architect authorise this one authored navigation-label correction? Its independence permits leaving the folder spelling unchanged even if a semantic Subject is later established with the mapping name.', '',
        '**Solution Architect Decision:** Not recorded. The scope authorisation permits review, not alteration of authored presentation.', '',
        '**Related Change Requests:** No execution prerequisite on semantic Subject creation; '+bycode['G18.03']+' independently addresses the candidate Subject. Do not treat either decision as automatically accepting the other.', '']
    lines += ['**Authorised Reconciliation Instruction:** Not authorised.', '', '**Reconciliation / Processing Record:** Not yet processed. Review evidence only; no canonical, relationship, template, publication or release mutation.', '']

text='\n'.join(lines)+'\n'
REVIEW.write_text(text)
(OUT/'change-requests.json').write_text(json.dumps(crs,ensure_ascii=False,indent=2)+'\n')
print(json.dumps(dict(changeRequests=len(crs),subjectInstances=32,classificationRelationships=27,presentationSpelling=1,allDispositions='On-Hold',reviewBytes=len(text.encode())),indent=2))
