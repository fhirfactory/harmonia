#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Hash-bound Archi review and explicitly adjudicated canonical-source amendments.

Review never writes canonical elements. Apply consumes an explicit disposition
file, checks every exact source preimage and validates the complete prospective
continuum before any writes. Names/paths never correlate identities. New identities,
splits, deletions and retypings require separate authorised creation/lineage work.
"""
import argparse
import copy
import json
from pathlib import Path
import sys
import architecture_continuum as c


def model(path):
    root = c.secure_xml(path)
    definitions = {n.get('identifier'): n.findtext('a:name', namespaces=c.N) for n in root.findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
    result = {}
    for node in root.findall('a:elements/a:element', c.N):
        props = {}
        for p in node.findall('a:properties/a:property', c.N):
            key = definitions.get(p.get('propertyDefinitionRef'))
            c.require(key is not None, 'Undefined Exchange property')
            c.require(key not in props, 'Repeated Exchange property: ' + key)
            props[key] = [v.text or '' for v in p.findall('a:value', c.N)]
        ids = props.get('canonicalId', [])
        c.require(len(ids) == 1 and c.UUID32.fullmatch(ids[0]), 'Missing/ambiguous canonicalId; creation requires reviewed identity allocation')
        cid = ids[0]; c.require(cid not in result, 'Duplicate canonicalId claim')
        specs = props.get('Specialization', props.get('specialization', []))
        c.require(len(specs) <= 1, 'Ambiguous authored specialization')
        result[cid] = {'identifier': node.get('identifier'), 'archimateType': node.get(c.XSI),
                       'specialization': specs[0] if specs else None,
                       'name': node.findtext('a:name', namespaces=c.N),
                       'documentation': node.findtext('a:documentation', namespaces=c.N) or '',
                       'properties': props}
    return root, result


def signatures(root, group, item):
    # Exchange property-definition IDs are transport identifiers. Reuse the
    # governed comparison by exact property name, retaining value/language and
    # child ordering. Never call property-ID rewriting an architectural edit.
    from types import SimpleNamespace
    from exchange_signatures import signature
    model=SimpleNamespace(keys={n.get('identifier'):n.findtext('a:name',namespaces=c.N)
        for n in root.findall('a:propertyDefinitions/a:propertyDefinition',c.N)})
    path = '/'.join('a:' + part for part in group.split('/')) + '/a:' + item
    return {n.get('identifier'): signature(n,model) for n in root.findall(path,c.N)}


def review(baseline, submitted, root=c.ROOT):
    c.validate(root)
    _, elements = c.load(root)
    before, original = model(baseline); after, proposed = model(submitted)
    c.require(set(original) == set(elements), 'Baseline/current canonical population mismatch')
    # The exact baseline must be the current projection, not a convenient old export.
    projected, _ = c.project(root)
    c.require(baseline.read_bytes() == projected, 'Baseline is not the exact current canonical projection')
    changes, unresolved = [], []
    for cid in sorted(set(original) | set(proposed)):
        if cid not in original or cid not in proposed:
            unresolved.append({'canonicalId': cid, 'reason': 'Added/deleted submitted identity requires explicit creation/retirement and lineage adjudication'}); continue
        a,b = original[cid], proposed[cid]
        if a == b: continue
        c.require(a['identifier'] == b['identifier'], 'Representation rebinding requires explicit separate adjudication: '+cid)
        m,body,path=elements[cid]
        fields = {key:{'before':a[key],'submitted':b[key]} for key in a if a[key] != b[key]}
        # This is review evidence. Stale derived properties are retained as claims;
        # they do not overwrite canonical names or establish semantic standing.
        changes.append({'canonicalId':cid,'source':str(path.relative_to(root)),
                        'sourceSha256':c.sha(path.read_bytes()),'changes':fields,
                        'disposition':'UNREVIEWED','replacement':None})
    context=[]
    for group,item in [('relationships','relationship'),('views/diagrams','view')]:
        a,b=signatures(before,group,item),signatures(after,group,item)
        for key in sorted(set(a)|set(b)):
            if a.get(key)!=b.get(key):context.append({'kind':item,'identifier':key,'before':a.get(key),'submitted':b.get(key),'disposition':'UNREVIEWED'})
    return {'schemaVersion':1,'standing':'UNREVIEWED; no application authority',
            'baselineSha256':c.sha(baseline.read_bytes()),'submissionSha256':c.sha(submitted.read_bytes()),
            'elementChanges':changes,'relationshipAndViewChanges':context,'unresolved':unresolved,
            'canonicalIdsAllocated':0,'canonicalSourcesChanged':0}


def apply(decision, root=c.ROOT):
    c.require(decision.get('authority'), 'Missing attributable reviewed HCRL disposition')
    authority=root/decision['authority'].split('#')[0]
    c.require(authority.is_file(), 'Reviewed authority source is missing')
    review_path=root/decision['review']
    c.require(c.sha(review_path.read_bytes()) == decision['reviewSha256'], 'Reviewed evidence changed')
    evidence=c.read_json(review_path)
    c.require(not evidence.get('unresolved'), 'Resolve affected added/deleted identities before apply')
    projected, _ = c.project(root)
    c.require(c.sha(projected) == evidence['baselineSha256'], 'Current projection has changed since review')
    expected={r['canonicalId']:r for r in evidence['elementChanges']}
    _,elements=c.load(root)
    replacements=decision.get('replacements',[])
    ids=[r['metadata']['canonicalId'] for r in replacements]
    c.require(len(ids)==len(set(ids)), 'Repeated replacement')
    dispositions=decision.get('dispositions', {})
    required=set(expected) | {r['kind']+':'+r['identifier'] for r in evidence['relationshipAndViewChanges']}
    c.require(set(dispositions)==required, 'Explicit disposition required for every reviewed change')
    for key, disposition in dispositions.items():
        c.require(disposition.get('action') in ('apply', 'reject', 'defer') and disposition.get('reason'), 'Missing action/reason: '+key)
        c.require(disposition['action'] != 'apply' or key in ids, 'This adapter applies element amendments only; relationship/View changes require reviewed canonical/template amendment')
    c.require(set(ids)=={key for key,d in dispositions.items() if d['action']=='apply'}, 'Replacement/disposition mismatch')
    pending=[]
    for replacement in replacements:
        m=copy.deepcopy(replacement['metadata']);cid=m['canonicalId']
        c.require(cid in expected, 'Replacement is outside reviewed element scope')
        old,_,path=elements[cid]
        c.require(c.sha(path.read_bytes())==expected[cid]['sourceSha256'], 'Canonical preimage changed: '+cid)
        c.require(m['archimateType']==old['archimateType'] and m.get('specialization')==old.get('specialization'), 'Retyping needs separate lineage adjudication')
        c.require(m['exchange']['identifier']==old['exchange']['identifier'], 'Representation identity changed')
        m['provenance'].append({'source':decision['review'],'selector':'canonicalId='+cid,
            'role':'Reviewed Archi amendment under '+decision['authority'],'retainedAt':decision['review']})
        pending.append((m,replacement['body'],path))
    # Validate in a temporary maintained-input checkout; no live partial writes.
    import tempfile,shutil
    with tempfile.TemporaryDirectory(prefix='harmonia-continuum-apply-') as tmp:
        stage=Path(tmp)
        for rel in [c.CONTINUUM,c.CONFIG,c.SCHEMAS]:
            shutil.copytree(root/rel,stage/rel)
        # Retained provenance is part of the prospective validation input. It is
        # evidence only; no historical processor or source capture is executed.
        for m,_,_ in elements.values():
            for source in m['provenance']:
                if source.get('retainedAt'):
                    rel=Path(source['retainedAt'])
                    c.require(not rel.is_absolute() and '..' not in rel.parts, 'Evidence must be repository-relative')
                    (stage/rel).parent.mkdir(parents=True,exist_ok=True)
                    shutil.copyfile(root/rel,stage/rel)
        (stage/decision['review']).parent.mkdir(parents=True,exist_ok=True)
        shutil.copyfile(review_path,stage/decision['review'])
        for m,body,path in pending:(stage/path.relative_to(root)).write_text(c.encode(m,body))
        c.validate(stage)
    # Preflight above covers every replacement. Ordinary filesystem writes are
    # not claimed as a multi-file transaction: retained review/preimages support recovery.
    for m,body,path in pending:path.write_text(c.encode(m,body))
    return {'status':'PASS','changed':len(pending),'allocatedIdentities':0,
            'deferred':[k for k,d in dispositions.items() if d['action']=='defer'],
            'rejected':[k for k,d in dispositions.items() if d['action']=='reject'],
            'architecturalClosure':'Not asserted'}


def main():
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--root',type=Path,default=c.ROOT)
    sub=p.add_subparsers(dest='command',required=True)
    r=sub.add_parser('review');r.add_argument('--baseline',type=Path,required=True);r.add_argument('--submitted',type=Path,required=True);r.add_argument('--output',type=Path,required=True)
    a=sub.add_parser('apply');a.add_argument('--decision',type=Path,required=True)
    args=p.parse_args()
    try:
        if args.command=='review':
            report=review(args.baseline,args.submitted,args.root);args.output.parent.mkdir(parents=True,exist_ok=True);args.output.write_bytes(c.encoded(report))
            print(json.dumps({'elementChanges':len(report['elementChanges']),'relationshipAndViewChanges':len(report['relationshipAndViewChanges']),'unresolved':len(report['unresolved'])}))
        else:print(json.dumps(apply(c.read_json(args.decision),args.root)))
        return 0
    except (ValueError,KeyError,OSError) as e:print('FAIL: '+str(e),file=sys.stderr);return 1
if __name__=='__main__':raise SystemExit(main())
