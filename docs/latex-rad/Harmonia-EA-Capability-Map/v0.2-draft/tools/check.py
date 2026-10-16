#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Architecture/identity/publication checks, preserving uncertainty as data."""
import argparse
import collections
import hashlib
import json
import re
import subprocess
import sys
from pathlib import Path

from baseline import BASE, V01, ROOT, frozen_file, load_v01
from navigation import validate_source, validate_pdf


def read(name):return json.loads((BASE/name).read_text())
def digest(path):return hashlib.sha256(path.read_bytes()).hexdigest()


def validate_model(m):
    elements=m['elements'];e={x['key']:x for x in elements}
    assert len(e)==len(elements),'Duplicate internal element key'
    for field in ('reference_id','latex_label','canonical_id'):
        values=[x[field] for x in elements]
        assert all(values),'Missing '+field
        assert len(values)==len(set(values)),'Duplicate '+field
    ids={x['canonical_id']:x['key'] for x in elements}
    historical={}
    for x in elements:
        for alias in x['id_aliases']:
            assert alias not in ids and alias not in historical,'ID alias collision or reuse'
            historical[alias]=x['key']
    assert all(v in e for v in historical.values()),'Indirect/unresolvable alias'
    assert set(read('identity-lock.json'))=={x['reference_id'] for x in elements},'Unfrozen allocation'
    identity_lock=read('identity-lock.json')
    for x in elements:
        frozen=identity_lock[x['reference_id']]
        assert all(x[k]==v for k,v in frozen.items()),'Frozen identity allocation changed'
        assert x['architectural_status'] in ('Canonical','Proposed','Candidate')
        assert x['placement_status'] in ('Established','Proposed')
        assert x['structural_id_status'] in ('Established','Proposed')
        if x['architectural_status']=='Proposed':
            assert x['placement_status']=='Proposed' and x['structural_id_status']=='Proposed','Proposal promoted'
            assert x.get('provenance',{}).get('derivation') and x['provenance']['evidence'],'Missing derivation provenance'
            assert all(len(x.get(k,''))>30 for k in ('definition','overview','boundary')),'Skeletal proposed element'
        assert x['record_state'] in ('current','superseded','pending_adjudication')
        if x['record_state']!='current':
            assert x.get('pre_review_record'),'Non-current responsibility lost its exact previous record'
            if x['record_state']=='pending_adjudication':
                assert 'HUMAN ADJUDICATION REQUIRED' in x.get('adjudication_note',''),'Hidden architectural uncertainty'
            continue
        if x.get('parent_key'):
            parent=e[x['parent_key']]
            assert parent['category']==x['category'],'Cross-category decomposition'
            assert (parent['tier'],x['tier']) in [('CT1','CT2'),('CT2','CT3')],'Skipped/invalid capability tier'
        if x['type'] in ('capability','enterprise_capability'):
            assert x['tier'] in ('CT1','CT2','CT3'),'Unplaced capability'
            assert (not x.get('parent_key'))==(x['tier']=='CT1'),'Floating capability or parented root'
            assert not x.get('owner_key'),'Capability has behaviour ownership'
            suffix=x['tier']+'-'+x['local_id']
            if x['type']=='enterprise_capability' and x['baseline_version']:
                suffix=x['tier']+'-'+x['reference_id']
        elif x['type']=='feature':
            owner=e[x['owner_key']]
            assert owner['category']=='business_enabling' and owner['tier']=='CT3','Feature lacks BEC CT3 frame'
            assert e[owner['parent_key']]['tier']=='CT2'
            assert e[e[owner['parent_key']]['parent_key']]['tier']=='CT1'
            suffix=x['local_id'] if x['local_id'].startswith('FEAT-') else 'FEAT-'+x['local_id']
        else:
            assert x['type'] in ('function','service','process') and x.get('owner_key'),'Disconnected behaviour'
            owner=e[x['owner_key']]
            assert owner['type'] in ('capability','feature'),'Invalid behavioural responsibility anchor'
            suffix={'function':'FN-','service':'SV-','process':'PR-'}[x['type']]+x['local_id']
        context=x.get('parent_key') or x.get('owner_key')
        expected=(e[context]['canonical_id']+'.' if context else '')+suffix
        assert x['canonical_id']==expected,'Structural context/type grammar mismatch: '+x['reference_id']
        parts=x['canonical_id'].split('.')
        assert all('.'.join(parts[:i]) in ids for i in range(1,len(parts))),'Orphan structural prefix'
    caps=[x for x in elements if x['record_state']=='current' and x['type'] in ('capability','enterprise_capability')]
    names=[x['name'].casefold() for x in caps]
    assert len(names)==len(set(names)),'Current capability name collision'
    # Feature names are unique within their CT1 responsibility frame.
    feature_names=[]
    for x in elements:
        if x['type']=='feature' and x['record_state']=='current':feature_names.append((x['canonical_id'].split('.')[0],x['name'].casefold()))
    assert len(feature_names)==len(set(feature_names)),'Feature naming collision'
    baseline=json.loads((V01/'manifest.json').read_text())
    baseline_e={x['key']:x for x in baseline['elements']}
    semantic=read('semantic-baseline.json')
    assert len(baseline_e)==719
    for k,old in baseline_e.items():
        current=e[k]
        for field in ('reference_id','latex_label','architectural_status','sources','type','category'):
            assert current[field]==old[field],'Baseline semantic identity changed: '+field
        assert current['canonical_name']==old['name'],'Established name overwritten'
        if current['name']!=old['name']:
            assert current['name_status']=='Proposed' and old['name'] in current['name_aliases'],'Unmarked proposed rename/history loss'
        assert current['governed_definition_latex']==semantic[current['reference_id']]['governed_definition_latex'],'Definition changed'
        assert current['baseline_placement']=={k:old.get(k) for k in ('tier','parent_key','owner_key','canonical_id')}
        if old['architectural_status']=='Canonical' and old['category']=='business':
            assert all(current[field]==old[field] for field in ('canonical_id','parent_key','tier')),'Established BC placement changed'
        if old['canonical_id'] and old['canonical_id']!=current['canonical_id']:
            assert old['canonical_id'] in current['id_aliases'],'Allocated historical ID lost'
        if old['owner_key']:
            assert current['responsibility_owner_key']==old['owner_key'],'Established responsibility owner lost'
            owners=[];p=current
            while p.get('owner_key') or p.get('parent_key'):
                p=e[p.get('owner_key') or p['parent_key']];owners.append(p['key'])
                assert len(owners)<10,'Cyclic context'
            if old['owner_key'] not in owners:
                relocation=current.get('reviewed_responsibility_relocation',{})
                assert relocation.get('previous_owner') in e and relocation.get('current_frame')==current['owner_key'] and relocation.get('status')=='Proposed','New frame escapes established responsibility without explicit reviewed reconciliation'
    for x in elements:
        if x['key'] not in baseline_e:assert x['architectural_status']=='Proposed','New responsibility promoted'
    for r in baseline['relationships']:
        assert r in m['relationships'],'Baseline relationship altered/dropped'
    for r in m['relationships']:
        assert r['from'] in e and r['to'] in e,'Unresolved relationship endpoint'
        assert r['status'] in ('established','unresolved','Proposed')
        if r not in baseline['relationships']:
            if r['status']=='established':
                assert r['type']=='enterprise_contribution' and r.get('sources'),'Unjustified relationship promotion'
                assert r['contribution']!='No Material Role'
                row=r['sources'][0]['text'];ec=e[r['to']]['reference_id']
                cols=[p.strip() for p in row.strip('|').split('|')]
                index=['Assurance Design','Assurance Criteria Management','Governed Assurance'].index(e[r['from']].get('canonical_name') or e[r['from']]['name'])+1
                assert ec in cols[0] and cols[index]==r['contribution'],'Incorrect approved matrix import'
            else:assert r['status']=='Proposed' and r.get('provenance'),'Unmarked new relationship'
        if r['type']=='enterprise_enabled_by':
            assert e[r['from']]['category']=='business_enabling' and e[r['to']]['category']=='enterprise'
        if r['type']=='enabled_by':
            assert e[r['from']]['category']=='business' and e[r['to']]['category']=='business_enabling'
    for c in m['relationship_constraints']:
        assert c['contribution']=='No Material Role' and c['status']=='established'
        assert not any(r['from']==c['from'] and r['to']==c['to'] and r['type'] in ('enterprise_contribution','enterprise_enabled_by') for r in m['relationships']),'Approved negative contribution overridden'
    assertions=m['placement_assertions']
    assert len(assertions)==len(e) and {a['element'] for a in assertions}==set(e),'Missing explicit current placement'
    assert len(m['catalogue_context_resolutions'])==73
    for c in m['catalogue_context_resolutions']:assert c['element'] in e
    for x in elements:
        if x['type']=='process' and x['architectural_status']=='Proposed':
            assert len(x.get('ordered_function_chain',[]))>=2
            assert all(e[k]['type']=='function' for k in x['ordered_function_chain'])
    validate_review(m,e)
    return e,historical,baseline


def validate_review(m,e):
    """Semantic guards for the human decisions, including deliberately empty tiers."""
    review=m['reviewed_bec_reconciliation']
    spec=read('reviewed-bec-hierarchy.json')
    by_name={x['reviewed_name']:x for x in e.values() if x.get('reviewed_name')}
    assert set(by_name)=={n['name'] for n in spec['nodes']},'Reviewed hierarchy coverage changed'
    for node in spec['nodes']:
        x=by_name[node['name']]
        assert x['record_state']=='current' and x['tier']==node['tier'],'Reviewed capability not current / wrong tier'
        assert x.get('parent_key')==(by_name[node['parent']]['key'] if node['parent'] else None),'Reviewed capability parent changed'
        assert x.get('release_scope')==node.get('release_scope'),'Explicit release scope lost or invented'
        assert x['placement_status']=='Proposed','Reviewed structure incorrectly promoted'
    scope='Not Addressed in R1.x / R2.x'
    dependency=by_name['Clinical Service Dependency Management']
    assert dependency['release_scope']==scope
    contexts=[x for x in e.values() if x.get('parent_key')==dependency['key'] and x['record_state']=='current']
    assert len(contexts)==16 and all(x['tier']=='CT2' for x in contexts)
    assert not any(x['record_state']=='current' and x.get('parent_key') in {c['key'] for c in contexts} for x in e.values()),'Artificial dependency-context CT3'
    for name in ['Funding & Coverage Administration','Clinical Charging & Claims Administration','Service Establishment Support','Clinical Training & Education','Research Information Curation','Population Health Information Reporting','Service Planning Data Management']:
        assert not any(x['record_state']=='current' and x.get('parent_key')==by_name[name]['key'] for x in e.values()),'Artificial CT3 decomposition: '+name
    for relocation in review['service_context_relocations']:
        x=e[relocation['key']]
        assert x['name']==relocation['name'] and x['parent_key']==by_name[relocation['parent']]['key'],'Service-enabling responsibility lost / mislocated'
        assert x['parent_key']!=dependency['key']
    assert e['capability:clinical-resource-management']['record_state']=='superseded','Obsolete resource capability retained'
    predecessor=read('pre-review-identity-lock.json')
    for rid,lock in predecessor.items():
        x=next(t for t in e.values() if t['reference_id']==rid)
        assert x['latex_label']==lock['latex_label'] and x['key']==lock['key'],'Stage-2 identity/label lost'
        assert set(lock['id_aliases'])<=set(x['id_aliases']),'Stage-2 historical alias lost'
        assert lock['canonical_id']==x['canonical_id'] or lock['canonical_id'] in x['id_aliases'],'Stage-2 structural history lost'
    expected={x['reference_id'] for x in e.values() if x.get('pre_review_record',{}).get('category')=='business_enabling'}
    assert {d['reference_id'] for d in review['decisions']}==expected,'Incomplete affected-BEC ledger'
    for d in review['decisions']:
        assert d['classification'] in ('retained unchanged','renamed','consolidated','decomposed','relocated','superseded by reviewed capability','retained but outside R1.x/R2.x','genuine unresolved conflict requiring human adjudication')
        assert all(k in e for k in d['targets'])
        if d['classification'] in ('consolidated','decomposed','superseded by reviewed capability'):assert d['targets'],'Missing explicit successors'
        if d['classification']=='decomposed':assert d.get('semantic_distribution'),'Lost decomposition semantics'
    consent=by_name['Client Privacy Guardrail Management']
    assert next(x for x in e.values() if x['reference_id']=='FEAT-EM-09')['owner_key']==consent['key'],'Canonical client consent ownership changed'
    for name in ['Health Information Control','Patient Health Information Restriction Management','Patient Health Information Filter Management','Patient Health Information Disclosure Management']:
        assert by_name[name]['respects_consent_responsibility']==consent['key'],'Consent duplicated or not respected'
    assert not any('consent' in x['name'].casefold() and x['type']=='capability' and x.get('parent_key')==by_name['Health Information Control']['key'] for x in e.values()),'Invented consent capability'
    assert 'service itself' in by_name['Assurance Service Evaluation']['definition'],'Assurance-service review confused with subject assurance'
    assert e['capability:governed-assurance']['record_state']=='pending_adjudication','Canonical subject assurance hidden or equated to service review'
    for rid in ('HCM-FN-manage-specimen-transport-progression','HCM-PR-specimen-transport-process'):
        x=next(x for x in e.values() if x['reference_id']==rid)
        assert x['record_state']=='pending_adjudication','Mixed specimen/materiel responsibility silently narrowed or republished'
        assert x['governed_definition_latex']==x['pre_review_record']['governed_definition_latex'],'Mixed Canonical logistics semantics changed'
    assert review['bc_ec_preservation']['exact_element_records_unchanged'] and not review['bc_ec_independent_redesign']
    bc_ec=[{k:v for k,v in x.items() if k!='record_state'} for x in e.values() if x['category'] in ('business','enterprise')]
    assert hashlib.sha256(json.dumps(bc_ec,sort_keys=True).encode()).hexdigest()==review['bc_ec_preservation']['sha256'],'BC/EC complete element records changed'
    assert len(m['relationship_currentness'])==len(m['relationships'])
    for record,r in zip(m['relationship_currentness'],m['relationships']):
        assert record['assertion_status']==r['status']
        if record['record_state']=='current':
            assert all(e[r[k]]['record_state']=='current' for k in ('from','to'))
            if r['type'] in ('capability_decomposition','responsibility_parent'):
                assert (e[r['to']].get('parent_key') or e[r['to']].get('owner_key'))==r['from'],'Stale decomposition asserted as current'


def included_tex(path,seen=None):
    seen=seen or set();path=path.resolve()
    assert path.is_relative_to(BASE),'LaTeX source outside the publication tree'
    assert path not in seen,'Duplicate/cyclic input'
    seen.add(path);text=path.read_text();chunks=[text]
    for inc in re.findall(r'\\input\{([^}]+)\}',text):
        chunks.append(included_tex(BASE/inc,seen))
    return '\n'.join(chunks)


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--model-only',action='store_true')
    parser.add_argument('--build-dir',type=Path,default=BASE/'build',
                        help='Directory containing ordinary LaTeX outputs (default: build/)')
    args=parser.parse_args()
    m=read('model.json');e,aliases,baseline=validate_model(m)
    for path,sha in read('v0.1-fingerprints.json').items():
        assert digest(frozen_file(path))==sha,'Frozen v0.1 file changed: '+path
    for source in read('corpus-register.json'):
        assert digest(ROOT/source['path'])==source['sha256'],'Corpus evidence drift: '+source['path']
    # Reuse all source fingerprint, exact-citation, source-line and coverage checks.
    old=load_v01()
    old.validate()
    assert read('source-coverage.json')==json.loads((V01/'source-coverage.json').read_text()),'Frozen source coverage changed'
    for x in e.values():
        for citation in x.get('provenance',{}).get('evidence',[]):
            assert (ROOT/citation['path']).read_text().splitlines()[citation['line']-1]==citation['text'],'Derivation citation drift'
    recon=read('v0.3-reconciliation.json')
    assert len(recon['primary_concepts'])==942 and len(recon['captured_behaviour_occurrences'])==277
    assert recon['unreviewed']==0 and not recon['candidate_identifiers_are_aliases']
    assert len({r['candidate_reference_id'] for r in recon['primary_concepts']})==942
    for r in recon['primary_concepts']:
        assert r['disposition'] and r['rationale'] and all(t in e for t in r['targets'])
    for script in ('compose.py','reconcile.py','render.py'):
        subprocess.run([sys.executable,str(BASE/'tools'/script),'--check'],check=True,timeout=60)
    tex=included_tex(BASE/'Harmonia-EA-Capability-Map.tex')
    labels=re.findall(r'\\label\{([^}]+)\}',tex)
    assert len(labels)==len(set(labels)),'Duplicate LaTeX label'
    assert {x['latex_label'] for x in e.values()}<=set(labels),'Element label not published'
    previous_labels=set(json.loads((V01/'identity-baseline.json').read_text())['latex_labels'])
    assert previous_labels<=set(labels),'Existing v0.1 label lost'
    pre_review_labels=set(read('pre-review-publication-labels.json'))
    assert pre_review_labels<=set(labels),'Existing v0.2 publication label lost'
    targets=re.findall(r'\\(?:ref|nameref|pageref)\{([^}]+)\}',tex)+re.findall(r'\\hyperref\[([^]]+)\]',tex)
    assert set(targets)<=set(labels),'Undefined cross references: '+str(set(targets)-set(labels))
    assert len(re.findall(r'\\index\{',tex))==len(e),'Incomplete name index source coverage'
    assert not any(cmd in tex for cmd in ('\\cappictograph','\\capchild','\\begin{tikzpicture}')),'Containment pictograph introduced'
    model_counts=collections.Counter((x['category'] or x['type'])+'_'+(x['tier'] or '') for x in e.values())
    report={'result':'PASS','version':'0.2-draft','elements':len(e),
            'statuses':dict(collections.Counter(x['architectural_status'] for x in e.values())),
            'counts':dict(model_counts),'new_elements':sum(x['baseline_version'] is None for x in e.values()),
            'current_counts':dict(collections.Counter((x['category'] or x['type'])+'_'+(x['tier'] or '') for x in e.values() if x['record_state']=='current')),
            'relationships':len(m['relationships']),'proposed_relationships':sum(r['status']=='Proposed' for r in m['relationships']),
            'preserved_baseline_elements':719,'preserved_baseline_relationships':len(baseline['relationships']),
            'canonical_definitions_preserved':True,'baseline_files_unchanged':True,
            'allocated_structural_ids':len(e),'unique_historical_aliases':len(aliases),
            'unplaced_capabilities':0,'features_without_bec_ct3':0,'unmarked_proposals':0,
            'feature_frame_assessment':'All current Features have CT3 frames; unresolved original Features are explicitly preserved outside the current tree.',
            'pending_feature_frames':sum(x['type']=='feature' and x['record_state']=='pending_adjudication' for x in e.values()),
            'reconciliation_record_states':dict(collections.Counter(x['record_state'] for x in e.values())),
            'unresolved_baseline_relationships_preserved':sum(r['status']=='unresolved' for r in baseline['relationships']),
            'v0.3_primary_concepts_reviewed':942,'v0.3_behaviour_occurrences_reviewed':277,
            'unique_latex_labels':len(labels),'undefined_references':0,'duplicate_reference_ids':0,
            'duplicate_structural_ids':0,'name_index_source_entries':len(e),
            'preserved_existing_latex_labels':len(previous_labels),
            'preserved_pre_review_latex_labels':len(pre_review_labels),
            'current_display_capability_names_unique':True,
            'governed_bc_bec_exchange_name_conflict':'Preserved; proposed display rename only',
            'limitations':'Structural/naming/support decisions remain Proposed. HUMAN ADJUDICATION REQUIRED responsibilities remain outside the current hierarchy with exact semantics, identities and owned material preserved.',
            'reviewed_bec_hierarchy':'PASS: complete reviewed candidate represented with explicit CM-R05 display-name qualification',
            'bc_ec_exact_records_unchanged':m['reviewed_bec_reconciliation']['bc_ec_preservation']}
    report['publication_navigation']=validate_source(e,tex)
    if not args.model_only:
        output=args.build_dir.resolve()/'Harmonia-EA-Capability-Map'
        log=output.with_suffix('.log').read_text()
        for pattern in (r'undefined references',r'Reference .* undefined',r'multiply defined',r'Overfull \\[hv]box',r'Missing character:',r'Label\(s\) may have changed',r'already defined'):
            assert not re.search(pattern,log,re.I),'LaTeX problem: '+pattern
        ilg=output.with_suffix('.ilg').read_text()
        assert f'{len(e)} entries accepted, 0 rejected' in ilg and '0 warnings' in ilg,'Index rejected/incomplete'
        idx=output.with_suffix('.idx').read_text()
        assert len(re.findall(r'\\indexentry',idx))==len(e),'Built index incomplete'
        pdf=output.with_suffix('.pdf')
        info=subprocess.check_output(['pdfinfo',str(pdf)],text=True,timeout=10)
        report.update(pdf_pages=int(re.search(r'Pages:\s+(\d+)',info)[1]),pdf_sha256=digest(pdf),
                      overfull_boxes=0,missing_glyphs=0,index_entries=len(e),pdf_build='PASS')
        report['publication_navigation'].update(validate_pdf(e,tex,pdf))
        (BASE/'validation.json').write_text(json.dumps(report,indent=2)+'\n')
    else:
        report['pdf_build']='Not assessed by --model-only'
        (BASE/'model-validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps(report,indent=2))


if __name__=='__main__':main()
