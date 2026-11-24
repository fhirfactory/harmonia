#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independent diagnostic decompositions; never modifies XML or verification policy.

Every excluded dimension is inventoried separately, including exact language,
catalogue IDs/labels, connection order and explicit default styles. Equality of
the remaining dimensions supplies no waiver of those recorded differences.
"""
import argparse
from collections import Counter, defaultdict
import copy
import gzip
import hashlib
import json
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[7]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import exchange_compare as d
import exchange_xml as g

def signature(n, model, without_language=False):
    attrs = dict(n.attrib)
    if g.TYPE in attrs:
        attrs[g.TYPE] = d.expanded_type(n)
    if 'propertyDefinitionRef' in attrs:
        attrs['propertyDefinitionRef'] = model.keys.get(attrs['propertyDefinitionRef'], attrs['propertyDefinitionRef'])
    if without_language:
        attrs.pop(d.LANG, None)
    kids = [signature(c, model, without_language) for c in n if isinstance(c.tag, str)]
    if n.tag == g.tag('properties'):
        kids.sort(key=repr)
    text = n.text
    if len(n) and not (text or '').strip():
        text = None
    tail = n.tail if (n.tail or '').strip() else None
    return (n.tag, sorted(attrs.items()), text, tail, kids)

def digest(value):
    return hashlib.sha256(json.dumps(value, ensure_ascii=False, sort_keys=True).encode()).hexdigest()

def analyse(before_path, after_path, native_path, output, raw_path=None):
    output.mkdir(parents=True, exist_ok=True)
    a, b = d.Exchange(before_path), d.Exchange(after_path)
    raw = json.loads(gzip.decompress(raw_path.read_bytes())) if raw_path else d.compare(a, b)
    if not raw_path:
        (output / 'raw-comparison.json.gz').write_bytes(gzip.compress(json.dumps(raw, ensure_ascii=False).encode(), mtime=0))
    patterns = Counter()
    examples = {}
    for f in raw['findings']:
        p = f['path']
        scope = ('navigation' if '/organizations[' in p else 'relationships' if '/relationship[id=' in p
                 else 'views' if '/view[id=' in p else 'elements' if '/element[id=' in p else 'other')
        if f['classification'] == d.EQUIVALENT:
            kind = 'property-definition-rekey' if '/@identifier' in p else 'unordered-keyed-collection-order'
        elif p.endswith('/@' + d.LANG) and f['before'] is None and f['after'] == 'en':
            kind = 'absent-to-en-language-annotation'
        elif scope == 'navigation' and f['reason'] == 'Attribute absent after transport':
            kind = 'organization-item-identifier-omitted'
        elif scope == 'navigation' and f['reason'].startswith('Subtree absent'):
            kind = 'organization-reference-label-omitted'
        elif scope == 'navigation' and p.endswith('/child-order'):
            kind = 'organization-label-removal-child-list'
        elif scope == 'views' and p.endswith('/style[1]') and f['before'] is None:
            kind = 'connection-default-style-added'
        elif scope == 'views' and '/connection[id=' in p and p.endswith('/child-order'):
            kind = 'connection-style-addition-child-list'
        elif scope == 'views' and p.endswith('/child-order'):
            kind = 'view-connection-sequence-changed'
        else:
            kind = 'UNEXPLAINED'
        key = (scope, kind, f['classification'])
        patterns[key] += 1
        examples.setdefault(key, f)
    rows = [dict(scope=s, pattern=p, rawClassification=c, count=v, example=examples[(s,p,c)])
            for (s,p,c),v in sorted(patterns.items())]
    assert sum(r['count'] for r in rows) == len(raw['findings'])

    semantics = {}
    for name in ('elements', 'relationships'):
        left, right = getattr(a, name), getattr(b, name)
        common = left.keys() & right.keys()
        mismatches = [k for k in sorted(common) if signature(left[k], a, True) != signature(right[k], b, True)]
        semantics[name] = dict(before=len(left), after=len(right), missing=sorted(left.keys()-right.keys()),
            additional=sorted(right.keys()-left.keys()), textAndNonLanguageAttributeDifferences=mismatches,
            diagnosticSignatureBefore=digest({k:signature(v,a,True) for k,v in sorted(left.items())}),
            diagnosticSignatureAfter=digest({k:signature(v,b,True) for k,v in sorted(right.items())}),
            excludedDimension='Language inventoried separately; property definitions resolved by exact unique key; properties unordered only')

    # Match entire navigation in its original positional sequence. Only remove
    # separately counted IDs and labels proven redundant with referenced Names.
    org_a, org_b = copy.deepcopy(a.root.find('a:organizations', g.N)), copy.deepcopy(b.root.find('a:organizations', g.N))
    omissions, ids, exceptions = Counter(), [], []
    left_items, right_items = list(org_a.iter(g.tag('item'))), list(org_b.iter(g.tag('item')))
    if len(left_items) != len(right_items):
        exceptions.append('Navigation item population differs')
    for x,y in zip(left_items,right_items):
        if x.get('identifier') is not None and y.get('identifier') is None:
            is_view = (x.findtext('a:label',namespaces=g.N)=='Views' or
                       any(n.findtext('a:label',namespaces=g.N)=='Views' for n in x.iterancestors()))
            ids.append(dict(identifier=x.get('identifier'),label=d.values(x,'label'),withinViews=is_view))
            x.attrib.pop('identifier')
        ref=x.get('identifierRef')
        if ref and x.findall('a:label',g.N) and not y.findall('a:label',g.N):
            owner=a.elements.get(ref)
            if owner is None: owner=a.relationships.get(ref)
            if owner is None: owner=a.views.get(ref)
            name=owner.findtext('a:name',namespaces=g.N) if owner is not None else None
            expected=[dict(text=name or ref,attributes={d.LANG:'en'})]
            if owner is None or d.values(x,'label') != expected:
                exceptions.append('Omitted label not equal to referenced Name: '+str(ref))
            else:
                kind='elements' if ref in a.elements else 'relationships' if ref in a.relationships else 'views'
                omissions[kind+('-name' if name else '-identifier-fallback')]+=1
                for label in x.findall('a:label',g.N): x.remove(label)
    navigation_equal=signature(org_a,a)==signature(org_b,b)
    nav = dict(itemCounts=[len(left_items),len(right_items)], omittedIdentifierCount=len(ids),
               omittedIdentifiersWithinViews=sum(x['withinViews'] for x in ids),
               omittedIdentifiers=ids, omittedLabelsEqualReferencedNames=dict(omissions),
               exactRemainingTreeMetadataReferencesAndOrderingEqual=navigation_equal, exceptions=exceptions,
               limits='Removed folder IDs are real representation information loss even where derived; redundant labels are independently recoverable from retained Names.')

    native_bytes=gzip.decompress(native_path.read_bytes())
    assert b'<!DOCTYPE' not in native_bytes and b'<!ENTITY' not in native_bytes
    native=g.ET.fromstring(native_bytes,g.ET.XMLParser(resolve_entities=False,no_network=True,huge_tree=True))
    native_views={n.get('id'):n for n in native.iter() if n.get(g.TYPE)=='archimate:ArchimateDiagramModel'}
    default_style=g.ET.fromstring(('''<style xmlns="'''+g.NS+'''"><lineColor r="0" g="0" b="0"/><font name="Sans" size="9"><color r="0" g="0" b="0"/></font></style>''').encode())
    style_additions=[]
    style_exceptions=[]
    orders=[]
    view_mismatches=[]
    native_default_attrs=[]
    totals=Counter()
    for vid in sorted(a.views.keys() & b.views.keys()):
        va,vb=a.views[vid],b.views[vid]
        ca={x.get('identifier'):x for x in va.findall('a:connection',g.N)}
        cb={x.get('identifier'):x for x in vb.findall('a:connection',g.N)}
        na={x.get('identifier'):x for x in va.findall('.//a:node',g.N)}
        nb={x.get('identifier'):x for x in vb.findall('.//a:node',g.N)}
        totals['nodesBefore']+=len(na);totals['nodesAfter']+=len(nb)
        totals['connectionsBefore']+=len(ca);totals['connectionsAfter']+=len(cb)
        assert na.keys()==nb.keys() and ca.keys()==cb.keys()
        if any(signature(na[k],a)!=signature(nb[k],b) for k in na):
            view_mismatches.append(dict(view=vid,reason='Node fields, properties, geometry, nesting or child order differs'))
        va_clean,vb_clean=copy.deepcopy(va),copy.deepcopy(vb)
        for cid in ca:
            x,y=ca[cid],cb[cid]
            if x.find('a:style',g.N) is None and y.find('a:style',g.N) is not None:
                sty=y.find('a:style',g.N)
                if signature(sty,b)!=signature(default_style,b): style_exceptions.append([vid,cid])
                style_additions.append(dict(view=vid,connection=cid))
                nview=native_views.get(vid)
                nconn=next((n for n in nview.iter() if n.get('id')==cid),None) if nview is not None else None
                native_default_attrs.append(dict(view=vid,connection=cid,attributes=dict(nconn.attrib) if nconn is not None else None))
                clean=next(n for n in vb_clean.findall('a:connection',g.N) if n.get('identifier')==cid)
                clean.remove(clean.find('a:style',g.N))
                if not len(clean) and not (clean.text or '').strip():
                    # Indentation of the removed style was not a leaf string in
                    # the original document. Remove this analysis-only residue.
                    clean.text=None
        if list(ca)!=list(cb):
            # Archi stores connections under source objects; export follows
            # native eAllContents order. Verify against the actual native save.
            nv=native_views.get(vid)
            native_order=[n.get('id') for n in nv.iter() if n.tag=='sourceConnection'] if nv is not None else []
            source_groups=defaultdict(list)
            for cid in ca: source_groups[ca[cid].get('source')].append(cid)
            within_source_preserved=all([cid for cid in cb if cb[cid].get('source')==src]==ids for src,ids in source_groups.items())
            orders.append(dict(view=vid,before=list(ca),after=list(cb),nativeContainmentOrder=native_order,
                               exportEqualsNativeContainmentOrder=list(cb)==native_order,
                               withinSourceSequencePreserved=within_source_preserved))
        # Analyse connection records independently, retaining the changed global
        # sequence above. Never sort nodes, nested visuals or routing points.
        left=[signature(n,a) for n in va_clean if n.tag!=g.tag('connection')]
        right=[signature(n,b) for n in vb_clean if n.tag!=g.tag('connection')]
        left_con={n.get('identifier'):signature(n,a) for n in va_clean.findall('a:connection',g.N)}
        right_con={n.get('identifier'):signature(n,b) for n in vb_clean.findall('a:connection',g.N)}
        if dict(va.attrib)!=dict(vb.attrib) or left!=right or left_con!=right_con:
            view_mismatches.append(dict(view=vid,reason='Residual after separately inventoried connection style/order differences'))
    views=dict(before=len(a.views),after=len(b.views),missing=sorted(a.views.keys()-b.views.keys()),additional=sorted(b.views.keys()-a.views.keys()),
               **totals, connectionDefaultStyleAdditions=style_additions, defaultStyleExceptions=style_exceptions,
               nativeConnectionAttributesForAddedStyles=native_default_attrs, connectionSequenceChanges=orders,
               otherViewDifferences=view_mismatches,
               limits='Exact styles/order remain recorded and strict verification unchanged. No pixel equivalence, cross-platform defaults or architectural acceptance asserted.')
    result=dict(inputs=dict(before=str(before_path),beforeXmlSha256=a.xml_sha256,returned=str(after_path),returnedXmlSha256=b.xml_sha256,native=str(native_path)),
                inputIssues=raw['input_issues'],rawClassifications=raw['classifications'],rawFindingCount=len(raw['findings']),
                exhaustivePatternAccounting=rows,unexplainedPatternCount=sum(v for (s,p,c),v in patterns.items() if p=='UNEXPLAINED'),
                semanticDimensions=semantics,navigation=nav,views=views,
                strictTransport='FAIL retained; this analysis neither changes nor replaces the strict gate')
    (output/'analysis.json').write_text(json.dumps(result,ensure_ascii=False,indent=2)+'\n')
    print(json.dumps(dict(output=str(output),rawFindings=len(raw['findings']),patterns=len(rows),unexplained=result['unexplainedPatternCount'],
          semanticMismatches={k:len(v['textAndNonLanguageAttributeDifferences']) for k,v in semantics.items()},
          navigationEqual=nav['exactRemainingTreeMetadataReferencesAndOrderingEqual'],navigationExceptionCount=len(exceptions),
          newStyles=len(style_additions),styleExceptionCount=len(style_exceptions),sequenceChanges=len(orders),otherViewDifferenceCount=len(view_mismatches))))
    return result

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__)
    for arg in ('before','returned','native','output'):
        p.add_argument('--'+arg,type=Path,required=True)
    p.add_argument('--raw',type=Path)
    a=p.parse_args()
    analyse(a.before,a.returned,a.native,a.output,a.raw)
