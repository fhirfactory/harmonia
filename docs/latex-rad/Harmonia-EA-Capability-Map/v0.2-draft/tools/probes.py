#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reproduce the publication's original thirteen in-memory rejection probes."""
import copy
import json
from pathlib import Path

from check import validate_model

BASE=Path(__file__).resolve().parents[1]
M=json.loads((BASE/'model.json').read_text())


def mutate(m,probe):
    a,b=m['elements'][:2]
    if probe=='missing_reference_id':a['reference_id']=None
    elif probe=='duplicate_reference_id':a['reference_id']=b['reference_id']
    elif probe=='missing_structural_id':a['canonical_id']=None
    elif probe=='duplicate_structural_id':a['canonical_id']=b['canonical_id']
    elif probe=='alias_current_id_collision':a['id_aliases'].append(b['canonical_id'])
    elif probe=='duplicate_historical_alias':
        alias=next(x['id_aliases'][0] for x in m['elements'] if x['id_aliases'])
        a['id_aliases'].append(alias)
    elif probe=='baseline_reference_change':a['reference_id']='INVALID-REFERENCE'
    elif probe=='baseline_label_change':a['latex_label']='invalid:label'
    elif probe=='canonical_definition_change':a['governed_definition_latex']='Invalid replacement definition.'
    elif probe=='new_element_promotion':
        next(x for x in m['elements'] if x['baseline_version'] is None)['architectural_status']='Canonical'
    elif probe=='proposed_placement_promotion':
        next(x for x in m['elements'] if x['architectural_status']=='Proposed')['placement_status']='Established'
    elif probe=='proposed_relationship_promotion':
        next(r for r in m['relationships'] if r['status']=='Proposed')['status']='established'
    elif probe=='feature_without_ct3_frame':
        feature=next(x for x in m['elements'] if x['reference_id']=='FEAT-SA-03')
        feature['owner_key']=next(x['key'] for x in m['elements'] if x.get('reviewed_name')=='Referral Administration')
    else:raise ValueError(probe)


def main():
    probes=['missing_reference_id','duplicate_reference_id','missing_structural_id','duplicate_structural_id',
            'alias_current_id_collision','duplicate_historical_alias','baseline_reference_change','baseline_label_change',
            'canonical_definition_change','new_element_promotion','proposed_placement_promotion',
            'proposed_relationship_promotion','feature_without_ct3_frame']
    results=[]
    for probe in probes:
        model=copy.deepcopy(M);mutate(model,probe)
        try:validate_model(model)
        except AssertionError as error:
            results.append(dict(probe=probe,result='REJECTED',reason=str(error)))
        else:raise AssertionError('Invalid model accepted: '+probe)
    report={'result':'PASS','purpose':'In-memory fault injection proves the original material architecture/identity guards still reject invalid models after reconciliation. No committed model is mutated.',
            'rejected_invalid_models':len(results),'probes':results}
    (BASE/'validator-probes.json').write_text(json.dumps(report,indent=2)+'\n')
    print('PASS:',len(results),'invalid models rejected')


if __name__=='__main__':main()
