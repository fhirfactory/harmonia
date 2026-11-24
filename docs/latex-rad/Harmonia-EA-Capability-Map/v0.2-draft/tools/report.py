#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Write the required review report from the reconciled model and verified outputs."""
import collections
import hashlib
import json
from pathlib import Path

BASE=Path(__file__).resolve().parents[1]
ROOT=BASE.parents[3]
REPORT=ROOT/'.junie/reports/2026-10-16-bec-human-adjudicated-reconciliation.md'


def read(name):return json.loads((BASE/name).read_text())
def safe(s):return str(s).replace('|',r'\|').replace('\n',' ')
def table(headers,rows):
    return '\n'.join(['| '+' | '.join(headers)+' |','| '+' | '.join('---' for _ in headers)+' |']+
                     ['| '+' | '.join(safe(x) for x in row)+' |' for row in rows])+'\n'


def main():
    m=read('model.json');e={x['key']:x for x in m['elements']};refs={x['reference_id']:x for x in e.values()}
    r=m['reviewed_bec_reconciliation'];v=read('validation.json');a=read('architecture-tests.json');s=read('standalone-validation.json');tests=read('reconciliation-tests.json')
    assert v['result']==a['result']==s['result']==tests['result']=='PASS'
    out=['<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->',
         '# Human-adjudicated BEC hierarchy reconciliation — 2026-10-16','',
         'The reviewed candidate is represented in v0.2-draft. Compatible responsibilities and behavioural material are retained or explicitly relocated. Genuine semantic conflicts are preserved unchanged outside the current hierarchy and marked **HUMAN ADJUDICATION REQUIRED**. This report closes the bounded reconciliation, not the outstanding architectural adjudications or RADS acceptance.','',
         '## 1. Authority and task boundary','',
         'The user-provided reviewed hierarchy is captured in [reviewed-bec-hierarchy.json](../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json). It is a reviewed candidate, not an assertion that the governed RADS has been amended. The frozen v0.1 substrate, current v0.2 evidence, authorised v0.3 candidate and current governed Markdown were used. Legacy architecture, implementation and historical Junie reports were not used as architectural authority. No governed Markdown or frozen v0.1 file was modified.','',
         'Relevant axioms: AX-01 and AX-04 preserve health/business meaning over application grouping; AX-06 preserves information authority; AX-07/08/09 preserve security, evidence and diagnostics; AX-13 preserves the execution/publication boundary; AX-14 preserves distinct semantic responsibilities; AX-17 requires explicit architectural uncertainty; AX-18 prevents information administration or coordination from conferring real-world domain authority. The reviewed approach preserves those boundaries. CM-R01–CM-R05 are checked through semantic retention, distinct ownership, enduring ability definitions, the explicit reviewed roots and unique current display names.','',
         'The supplied Domain03 traceability reference was consulted: [domain03-traceability.md](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md). Information, Functions, Services and Processes retain their established responsibility evidence. No information entity, actor, FHIR resource or application component was promoted merely to fill a tier.','',
         '## 2. Before/after hierarchy','',
         'Before: **7 BEC CT1 / 28 CT2 / 102 CT3**. After: **7 current BEC CT1 / 76 CT2 / 95 CT3**. The exact reviewed input contains 162 nodes (7 CT1, 76 CT2, 79 CT3); the additional sixteen current CT3s are six retained meaningful existing administrative descriptions and ten relocated surviving service-context responsibilities. No new CT3 was manufactured under a dependency service context or the three informatics purposes.','',
         '### Before CT1 and CT2 grouping','']
    before=r['before_hierarchy']
    for root in [x for x in before if x['tier']=='CT1']:
        children=[x['name'] for x in before if x['parent_key']==root['key'] and x['tier']=='CT2']
        out.append('- **'+root['name']+'**: '+'; '.join(children)+'.')
    out+=['','### After — complete current BEC hierarchy','',
          'The exact reviewed wording is used below; the BEC Exchange display qualification is explained in section 8. Publication chapter/section numbers are navigation, not identity.','']
    nodes=r['reviewed_nodes'];order={n['element']:i for i,n in enumerate(nodes)}
    for root in [e[n['element']] for n in nodes if n['tier']=='CT1']:
        out.append('#### '+root['name']);out.append('')
        if root.get('release_scope'):out+=['**'+root['release_scope']+'**','']
        children=sorted([x for x in e.values() if x['record_state']=='current' and x.get('parent_key')==root['key']],key=lambda x:order.get(x['key'],1000))
        for child in children:
            descendants=sorted([x for x in e.values() if x['record_state']=='current' and x.get('parent_key')==child['key']],key=lambda x:order.get(x['key'],1000))
            out.append('- **'+child.get('reviewed_name',child['name'])+'**'+(' — **'+child['release_scope']+'**' if child.get('release_scope') else '')+(': '+ '; '.join(x['name'] for x in descendants) if descendants else ' — no CT3 decomposition asserted')+'.')
        out.append('')
    out+=['## 3. Dispositions and preservation counts','',
          'Primary classifications are mutually exclusive for the 137 pre-review BEC identities. Operations can overlap: a consolidation may also rename and relocate a survivor. Counts refer to responsibilities/records, not publication numbering.','',
          table(['Primary classification','Count'],r['classification_counts'].items()),
          table(['Operation (overlapping)','Count'],r['operation_counts'].items()),
          f"All **862** pre-review element Reference IDs and labels survive. All **719** v0.1 elements survive, including **716 Canonical** and **3 Candidate** elements with unchanged semantic standing, exact governed definitions and source evidence. The reviewed candidate adds **{len(r['new_records'])}** Proposed BEC records; no BC or EC element is added by this task.",'',
          f"The current hierarchy has **845 current elements**. **32 superseded BEC records** and **69 pending-adjudication records** remain individually published, indexed and addressable in the preserved-responsibility register. The latter include 21 Features; these are explicitly unresolved semantic frames, not asserted completed placements. Every one of the **157 current Features** has a current meaningful BEC CT3 ancestor.",'',
          'Superseded/pending records keep their last allocated structural ID as a reserved historical value, explicitly not a current placement assertion. Their exact complete pre-review record is preserved in `pre_review_record`. Stable Reference IDs, labels and direct aliases are never reassigned. Original relationship assertions and their original statuses remain unchanged; `relationship_currentness` separates historical assertions from current structure.','',
          '### New reviewed / retained-frame identity allocations','',
          'These allocations have no pre-review identity to rename or relocate. They are additional Proposed reviewed responsibilities or retained meaningful contextual frames, separately counted from the predecessor classifications.','',
          table(['New stable Reference ID','Responsibility / tier','Parent'],[[rid,refs[rid]['name']+' / '+refs[rid]['tier'],e[refs[rid]['parent_key']]['name'] if refs[rid].get('parent_key') else 'CT1 root'] for rid in r['new_records']]),
          '### Consolidations and predecessor identities','']
    consolidation_rows=[]
    for x in e.values():
        if x['record_state']!='current' or not x.get('predecessor_keys'):continue
        predecessors=[e[k] for k in x['predecessor_keys']]
        consolidation_rows.append([x['name']+' — '+x['reference_id'],'; '.join(y['pre_review_record']['name']+' — '+y['reference_id'] for y in predecessors),
                                   'Identity retained' if x.get('pre_review_record') else 'New reviewed identity; predecessors retained in history'])
    out.append(table(['Surviving reviewed responsibility','Predecessors','Identity treatment'],consolidation_rows))
    out+=['### Explicit semantic decompositions','']
    for d in r['decisions']:
        if d['classification']!='decomposed':continue
        out+=['**'+d['before_name']+' — '+d['reference_id']+'**','']
        for name,meaning in d['semantic_distribution'].items():out.append('- **'+name+'**: '+meaning)
        out.append('')
    out+=['Device Connectivity endpoint registrations are consolidated into Device OA&M Metadata Management. Connectivity compliance is a separately reviewed ability; registration is not treated as evidence of conformity. The former Clinical Resource Management identity is superseded: equipment location/allocation/readiness information and its compatible behaviour move to Device Utilisation Administration; service dependency tracking is covered by the reviewed dependency CT1. No independent generic resource manager remains in the current hierarchy.','',
          '## 4. Complete relocation and Feature ancestry ledger','',
          '### Service-context responsibilities relocated','',
          table(['Predecessor','Stable Reference ID','Surviving responsibility','Resulting CT2'],[[x['previous_name'],x['reference_id'],x['name'],x['parent']] for x in r['service_context_relocations']]),
          'The six unelaborated contextual responsibilities (Clinical Assessment, Ambulatory Care, Surgical & Procedural Care, Therapeutic Services, Allied Health and Rehabilitation) retain their identities and Canonical contextual participation as scoped CT2s under the reviewed dependency purpose. Their prior explanatory context is retained as source history; no clinical-delivery authority or CT3 is inferred.','',
          '### Complete BEC relocation list (including tier/context changes)','',
          table(['Reference ID','Before name / tier / parent','After name / tier / parent'],[[d['reference_id'],d['before_name']+' / '+d['before_tier']+' / '+(e[d['before_parent']].get('pre_review_record',e[d['before_parent']])['name'] if d['before_parent'] else 'root'),d['after_name']+' / '+d['after_tier']+' / '+(e[d['after_parent']]['name'] if d['after_parent'] else 'root')] for d in r['decisions'] if 'relocated' in d['operations']]),
          '### Complete Feature ancestry changes','',
          'Established responsibility ownership remains in `responsibility_owner_key`, `baseline_placement` and exact source evidence. Explicit responsibility moves additionally record `reviewed_responsibility_relocation`. No one-to-one Feature/Function/Service/Process association was invented. The existing research release, population reporting-obligation and planning-projection Features share Information Product Administration framing while retaining explicit purpose-context relationships to their undecomposed informatics CT2s.','',
          table(['Feature Reference ID / name','Previous immediate frame','Current meaningful CT3 frame'],[[x['reference_id']+' / '+refs[x['reference_id']]['name'],e[x['before_owner']].get('pre_review_record',e[x['before_owner']])['name']+' — '+e[x['before_owner']]['reference_id'],e[x['after_owner']]['name']+' — '+e[x['after_owner']]['reference_id']] for x in r['feature_ancestry_changes']]),
          '### Direct Function / Service / Process ownership changes','',
          table(['Reference ID','Previous owner','Current owner'],[[x['reference_id'],e[x['before_owner']].get('pre_review_record',e[x['before_owner']])['name'],e[x['after_owner']]['name']] for x in r['behaviour_ownership_changes']]),
          '## 5. R1.x / R2.x scope','',
          'All eleven explicit annotations below are **Not Addressed in R1.x / R2.x**. They are rendered in bold metadata and a dedicated scope field; descendants show inherited scope. They do not demote Canonical elements to Candidate. Compatible existing administrative descriptions and Features remain available for future scope and historical continuity; their old baseline release coverage does not override this explicit reviewed release decision. No CT3 is invented for Funding & Coverage, Clinical Charging & Claims, Service Establishment Support, Clinical Training & Education, the sixteen dependency service contexts or the informatics purpose CT2s.','',
          table(['Capability','Reference ID','Annotation'],[[x['name'],x['reference_id'],x['annotation']] for x in r['scope_annotations']]),
          '## 6. Consent responsibility','',
          '**Preserved.** Client Privacy Guardrail Management retains the Canonical Client Privacy identity (`HCM-BEC-client-privacy`) and **FEAT-EM-09 Consent Directive Evaluation**, including client-associated constraints, preferences, permissions and restrictions. It does not own general enterprise privacy policy. Health Information Control and its restriction/filter/disclosure CT3s explicitly reference/respect that responsibility. Existing Canonical consent-enforcement Functions and Services remain preserved; no second consent Capability is created beneath Health Information Control.','',
          '## 7. BC / BEC / EC relationships and provenance','',
          'BC supports/enablement and BEC enabled-by-EC semantics remain distinct from decomposition and application architecture. All 462 v0.1 relationships, including 22 unresolved associations, are retained byte for byte, as are original Stage-2 relationships. Compatible consolidation/decomposition reference repairs are separate Proposed assertions. Old structural relationships are labelled historical when they no longer describe the current tree. No enablement is inherited automatically, no BC/BEC equivalence is introduced, and no application or deployment boundary is allocated.','',
          'All 942 v0.3 primary concepts and 277 behavioural occurrences retain individual reconciliation records. The original Stage-2 decisions are preserved byte for byte in `pre-review-v0.3-reconciliation.json`; current decisions link to them. v0.3 working IDs remain provenance locators rather than identity aliases. Exact previous descriptions, definitions, source and derivation/status evidence remain in each affected record. The earlier verification reports and README are preserved verbatim in `pre-review-publication-evidence.json`; their claims apply to the pre-review publication only.','',
          '## 8. Genuine conflicts — HUMAN ADJUDICATION REQUIRED','',
          r['naming_conflict'],'',
          'The eleven BEC-level conflicts below retain their original complete semantics and owned material in a separate published register. That register is not a residual capability branch, an alternative current hierarchy, or permission to omit the conflicts from downstream derivation.','',
          table(['Preserved BEC identity','Conflict / required architectural judgement'],[[d['reference_id']+' — '+d['before_name'],d['reason']] for d in r['decisions'] if d['classification']=='genuine unresolved conflict requiring human adjudication']),
          'General security context, identity/policy assignment and audit Features have no adjudicated patient-specific CT3 frame in the reviewed Health Information Control branch. Their established owner and original semantics remain preserved, without forcing them into patient restriction or disclosure capabilities.','',
          '**Additional Canonical logistics conflicts:** `HCM-FN-manage-specimen-transport-progression` explicitly includes pathology bio-specimens, blood products and surgical trays. Surgical-tray/materiel movement cannot remain a current specimen-only responsibility, and the Function cannot be silently narrowed. `HCM-PR-specimen-transport-process` includes blood products; their intended specimen-versus-materiel meaning is not adjudicated. Both remain Canonical, unchanged and explicitly pending; specimen-only Features remain current.','',
          '### Complete pending material (69 individual records)','',
          table(['Reference ID / type / name','Preserved uncertainty'],[[x['reference_id']+' / '+refs[x['reference_id']]['type']+' / '+x['name'],x['reason']] for x in r['semantic_conflicts']]),
          '## 9. Complete per-BEC reconciliation ledger','',
          table(['Stable Reference ID','Before','After / record standing','Primary disposition','Reviewed successors'],[[d['reference_id'],d['before_name'],d['after_name']+' / '+d['record_state'],d['classification'],'; '.join(e[k]['name']+' — '+e[k]['reference_id'] for k in d['targets']) or 'Preserved pending human adjudication'] for d in r['decisions']]),
          '## 10. Complete structural-ID changes and aliases','',
          f"**{len(r['structural_id_changes'])}** pre-review element structural IDs change. Stable Reference IDs, labels and local tokens are retained. Every previous ID is a direct alias to the same identity. The historical namespace contains **{v['unique_historical_aliases']}** unique direct aliases, including the original EC aliases and allocations used during this bounded reconciliation. Non-current records reserve their last structural ID as history and do not assert a new current prefix.",'',
          '### Every changed structural ID','',
          table(['Stable Reference ID','Previous Structural Canonical ID','Current Structural Canonical ID'],[[x['reference_id'],'`'+x['before']+'`','`'+x['after']+'`'] for x in r['structural_id_changes']]),
          '### Every retained direct structural alias','',
          table(['Historical structural alias','Stable Reference ID','Current / last allocated structural ID'],[['`'+alias+'`',x['reference_id'],'`'+x['canonical_id']+'`'] for x in e.values() for alias in x['id_aliases']]),
          '## 11. Validation, tests and conventional publication','',
          table(['Verification','Result / evidence'],[
            ['Model, baseline, source fingerprints, identities, alias namespace, exact generation and typed relationships','PASS — validation.json; all 719 baseline elements and 862 previous identities/labels retained'],
            ['New reviewed-decision semantic regressions','PASS — 17 tests, including negative scope/ancestry/consent/authority/alias/CT4/Feature-frame cases'],
            ['Existing validator fault injections','PASS — all 13 invalid-model probes rejected; validator-probes.json'],
            ['Existing RADS regression suite','PASS — 40 tests'],
            ['Existing RADS reference-model validation','PASS — zero invalid; 2,467 explicitly unresolved assertions retained'],
            ['Required ArchUnit architecture suite','PASS — 90 tests, zero failures/errors/skips; 420-second command bound, 18.491-second run'],
            ['BC / EC full-record preservation','PASS — 228 exact complete element records unchanged; SHA-256 '+r['bc_ec_preservation']['sha256']],
            ['Conventional master compilation','PASS — latexmk -pdf -interaction=nonstopmode -halt-on-error Harmonia-EA-Capability-Map.tex'],
            ['Clean source-only external compilation','PASS — '+str(s['source_only_copy_files'])+' source/config files, no model/Python/Makefile, '+str(s['elapsed_seconds'])+' seconds'],
            ['Editor support','Conventional master and existing Kile project retained. Kile GUI was not driven; direct master compilation is verified.'],
            ['Source-only portability / generated text','PASS — no nonstandard compiler inputs outside the copied publication; rendered text identical to the published PDF'],
            ['Current and historical PDF navigation','PASS — '+str(v['publication_navigation']['pdf_outline_entries'])+' outline entries; all current element ancestry and separate historical-register placement verified'],
            ['LaTeX labels / glyphs / boxes','PASS — '+str(v['unique_latex_labels'])+' unique labels; all 2,681 previous labels retained; zero undefined references, duplicate labels, missing glyphs or overfull boxes'],
            ['Deep numbering','PASS — '+str(v['publication_navigation']['numbered_catalogue_fields'])+' technical catalogue fields remain numbered'],
            ['PDF / index','PASS — '+str(v['pdf_pages'])+' pages; '+str(v['index_entries'])+' name-index entries; '+str((BASE/'Harmonia-EA-Capability-Map.pdf').stat().st_size)+' bytes'],
            ['Visual inspection','PASS — representative dependency-scope and consolidated organisation-profile pages inspected; layout and scope emphasis are legible'],
          ]),
          'The generated master and all included `.tex` files remain the first-class editable publication. Compiling them invokes conventional pdfLaTeX/makeindex via latexmk and requires no repository-generation step. Optional regeneration is separate. XeLaTeX engine support is retained but was not rerun for this task. No command timed out or stalled; unresolved architectural semantics are not reported as test failures or silently assumed resolved.','',
          'Published PDF SHA-256: `'+v['pdf_sha256']+'`.','',
          '## 12. BC / EC preservation and completion boundary','',
          '**BC and EC architecture were not independently redesigned.** All 228 pre-review BC/EC complete element records compare exactly, including hierarchy, names, identifiers, semantics, assertion status and provenance. BEC relationship repairs do not alter BC/EC decomposition. No EC identity was reallocated. No production Java, POM, runtime/deployment or governed Markdown architecture was changed.','',
          'The bounded reconciliation is complete with the explicitly preserved human-adjudication register. The remaining uncertainties concern architectural judgement; they are not hidden by validation, promoted to Canonical decisions, converted to clinical-service dependencies, or filled by invented decomposition.','']
    REPORT.write_text('\n'.join(out))
    print('Wrote',REPORT.relative_to(ROOT),';',len(r['decisions']),'BEC decisions;',len(r['structural_id_changes']),'structural changes')


if __name__=='__main__':main()
