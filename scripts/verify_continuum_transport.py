#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Retain exact no-edit transport evidence; never normalise raw comparator findings."""
import argparse
from collections import Counter
import gzip
from pathlib import Path
import sys
import architecture_continuum as c
import continuum_workbench as w


def verify(baseline,returned,execution_log,operator,output):
    sys.path.insert(0,str(c.ROOT/'docs/archimate/framework/tools'))
    import diagnose_roundtrip as d
    raw=d.compare(d.Exchange(baseline),d.Exchange(returned))
    _,before=w.model(baseline);_,after=w.model(returned)
    c.require(set(before)==set(after),'Canonical population lost in transport')
    fields={k:sum(x[k]==after[cid][k] for cid,x in before.items()) for k in ['identifier','archimateType','specialization','name','documentation']}
    roles={k:sum(x['properties'].get(k)==after[cid]['properties'].get(k) for cid,x in before.items()) for k in c.ROLES}
    c.require(all(v==len(before) for v in (*fields.values(),*roles.values())),'Canonical transport field drift')
    for kind in ('relationships','presentation'):
        c.require(not any(raw[kind][k] for k in ('missing_ids','additional_ids','changed_records')),'Relationship/View transport drift')
    review=w.review(baseline,returned)
    counts={'elementChanges':len(review['elementChanges']),'relationshipAndViewChanges':len(review['relationshipAndViewChanges']),'unresolved':len(review['unresolved'])}
    c.require(not any(counts.values()),'No-edit submission unexpectedly contains canonical changes')
    dest=output.parent/'archi-transport';dest.mkdir(parents=True,exist_ok=True)
    (dest/'returned-model.xml.gz').write_bytes(gzip.compress(returned.read_bytes(),mtime=0))
    (dest/'transport.log').write_bytes(execution_log.read_bytes())
    report={'actualArchiImportExport':'PASS','operator':operator,'baselineSha256':c.sha(baseline.read_bytes()),'returnedXmlSha256':c.sha(returned.read_bytes()),'returnedModel':'archi-transport/returned-model.xml.gz','inputIssues':raw['input_issues'],'metrics':raw['metrics'],'preservedElementFieldCounts':fields,'preservedPropertyValueAndOwnerCounts':roles,'relationships':{k:raw['relationships'][k] for k in ['before','after','missing_ids','additional_ids','changed_records']},'views':{k:raw['presentation'][k] for k in ['before','after','missing_ids','additional_ids','changed_records']},'rawComparatorStatus':raw['comparison_status'],'rawClassifications':raw['classifications'],'rawReasons':dict(Counter(f['reason'] for f in raw['findings'])),'canonicalWorkbenchReview':counts,'limits':['The operator/execution log attributes the actual transport; comparison alone cannot authenticate execution.','Not whole-model byte fidelity: Archi adds en language attributes and restructures organization navigation. Raw differences remain visible.','The maintained presentation template is not replaced by the no-edit return. Interactive visual inspection and architectural acceptance are not asserted.'],'invocation':'Archi -nosplash -consoleLog -configuration <temporary-config> -data <temporary-workspace> -application com.archimatetool.commandline.app -a --xmlexchange.import <published-XML> --saveModel <temporary-native-model> --xmlexchange.export <returned-XML> --xmlexchange.exportFolders --xmlexchange.exportLang en','environmentResolution':'Initial sandbox display access failed gtk_init_check; authorised execution with local display access succeeded. No network dependency.'}
    output.write_bytes(c.encoded(report));return report

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__)
    for name in ('baseline','returned','execution-log','report'):p.add_argument('--'+name,type=Path,required=True)
    p.add_argument('--operator',required=True);a=p.parse_args()
    print(verify(a.baseline,a.returned,a.execution_log,a.operator,a.report)['actualArchiImportExport'])
