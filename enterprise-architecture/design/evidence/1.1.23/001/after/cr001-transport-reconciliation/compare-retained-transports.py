#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded evidence comparison, not a production verifier or acceptance policy.

Reuses the accepted predecessor's completed investigation and the same unchanged
diagnostic applied to retained candidate inputs. No Archi execution or predecessor
investigation is repeated. All child orders and language remain visible in the
cross-release record comparison, including presentation/default/order fields.
"""
import gc
import gzip
import hashlib
import importlib.util
import json
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[7]
HERE = Path(__file__).resolve().parent
BOUNDARY = HERE.parents[1]
PINS = BOUNDARY / 'before/cr001-transport-reconciliation/inputs-and-preservation.json'
pins = json.loads(PINS.read_text())
inputs = {k: ROOT / v['path'] for k, v in pins['inputs'].items()}
spec = importlib.util.spec_from_file_location('predecessor_diagnostic', inputs['existingDiagnostic'])
diag = importlib.util.module_from_spec(spec)
spec.loader.exec_module(diag)
d, g = diag.d, diag.g


def signature(node, model, resolve_property_keys=False):
    """All fields/order, excluding XML formatting whitespace and prefix spelling.

    Across releases retain literal Property IDs; for source/return new-record
    checks also resolve exact unique Property keys, independently pinned below.
    No language, style, node/connection/property order or value is suppressed.
    """
    attrs = dict(node.attrib)
    if g.TYPE in attrs:
        attrs[g.TYPE] = d.expanded_type(node)
    if resolve_property_keys and 'propertyDefinitionRef' in attrs:
        ref = attrs['propertyDefinitionRef']
        assert ref in model.keys, ref
        attrs['propertyDefinitionRef'] = model.keys[ref]
    children = [signature(c, model, resolve_property_keys) for c in node if isinstance(c.tag, str)]
    text = node.text
    if len(node) and not (text or '').strip():
        text = None
    tail = node.tail if (node.tail or '').strip() else None
    return [node.tag, attrs, text, tail, children]


def digest(value):
    return hashlib.sha256(json.dumps(value, ensure_ascii=False, sort_keys=True).encode()).hexdigest()


def mismatch(left, right):
    return sorted(k for k in left.keys() & right.keys() if left[k] != right[k])


def main():
    for role, record in pins['inputs'].items():
        assert hashlib.sha256(inputs[role].read_bytes()).hexdigest() == record['sha256'], role
    previous = json.loads(inputs['predecessorAnalysis'].read_text())
    current = json.loads((HERE / 'candidate-analysis/analysis.json').read_text())
    assert previous['inputs']['beforeXmlSha256'] == pins['inputs']['predecessorOriginal']['sha256']
    assert previous['inputs']['returnedXmlSha256'] == pins['inputs']['predecessorReturned']['uncompressedSha256']
    assert current['inputs']['beforeXmlSha256'] == pins['inputs']['candidateOriginal']['sha256']
    assert current['inputs']['returnedXmlSha256'] == pins['inputs']['candidateReturned']['uncompressedSha256']

    stages = {}
    # Load one XML tree at a time; retain only per-record evidence digests.
    for role in ['predecessorOriginal', 'predecessorReturned', 'candidateOriginal', 'candidateReturned']:
        model = d.Exchange(inputs[role])
        assert not model.issues, (role, model.issues)
        stages[role] = {
            kind: {cid: digest(signature(n, model)) for cid, n in getattr(model, kind).items()}
            for kind in ['elements', 'relationships', 'views', 'definitions']
        }
        stages[role]['resolvedRelationships'] = {
            cid: digest(signature(n, model, True)) for cid, n in model.relationships.items()
        }
        stages[role]['languageValues'] = sorted({n.get(d.LANG) for n in model.root.iter() if n.get(d.LANG) is not None})
        print('Compared retained XML fields: ' + role, flush=True)
        del model
        gc.collect()

    po, pr, co, cr = (stages[k] for k in ['predecessorOriginal', 'predecessorReturned', 'candidateOriginal', 'candidateReturned'])
    old_ids, all_ids = set(po['relationships']), set(co['relationships'])
    new_ids = all_ids - old_ids
    relationship_rows = []
    for cid in sorted(all_ids):
        relationship_rows.append(dict(identifier=cid, introduced=cid in new_ids,
            predecessorOriginal=po['relationships'].get(cid), predecessorReturned=pr['relationships'].get(cid),
            candidateOriginal=co['relationships'][cid], candidateReturned=cr['relationships'][cid],
            candidateKeyResolvedOriginal=co['resolvedRelationships'][cid],
            candidateKeyResolvedReturned=cr['resolvedRelationships'][cid]))
    view_rows = [dict(identifier=cid, predecessorOriginal=po['views'][cid], predecessorReturned=pr['views'][cid],
                     candidateOriginal=co['views'].get(cid), candidateReturned=cr['views'].get(cid))
                 for cid in sorted(po['views'])]

    def patterns(analysis):
        return {(r['scope'], r['pattern'], r['rawClassification']): r['count']
                for r in analysis['exhaustivePatternAccounting']}
    p, c = patterns(previous), patterns(current)
    rows = [dict(scope=s, pattern=k, rawClassification=cl, predecessor=p.get((s,k,cl),0),
                 candidate=c.get((s,k,cl),0), delta=c.get((s,k,cl),0)-p.get((s,k,cl),0))
            for s,k,cl in sorted(p.keys() | c.keys())]
    existing_original_differences = mismatch(po['relationships'], co['relationships'])
    existing_returned_differences = mismatch(pr['relationships'], cr['relationships'])
    view_original_differences = mismatch(po['views'], co['views'])
    view_returned_differences = mismatch(pr['views'], cr['views'])
    new_differences = sorted(cid for cid in new_ids if co['resolvedRelationships'][cid] != cr['resolvedRelationships'][cid])
    new_elements = set(co['elements']) - set(po['elements'])
    old_folders = {r['identifier']: r for r in previous['navigation']['omittedIdentifiers']}
    current_folders = {r['identifier']: r for r in current['navigation']['omittedIdentifiers']}
    added_folder_records = [current_folders[k] for k in sorted(current_folders.keys()-old_folders.keys())]
    raw = json.loads(gzip.decompress(inputs['candidateRaw'].read_bytes()))
    new_relationship_findings = []
    added_element_language_findings = []
    for finding in raw['findings']:
        rel = re.search(r'/relationship\[id=([^\]]+)\]', finding['path'])
        if rel and rel[1] in new_ids:
            new_relationship_findings.append(finding)
        element = re.search(r'/element\[id=([^\]]+)\]', finding['path'])
        if element and element[1] in new_elements and finding['path'].endswith('/@'+d.LANG):
            added_element_language_findings.append(finding)
    assert all(f['before'] is None and f['after']=='en' for f in added_element_language_findings)
    assert len(added_element_language_findings)==110
    vkeys = ['connectionDefaultStyleAdditions', 'defaultStyleExceptions', 'nativeConnectionAttributesForAddedStyles',
             'connectionSequenceChanges', 'otherViewDifferences']
    view_dimensions = {k: previous['views'][k] == current['views'][k] for k in vkeys}
    definitions = {side: stages['predecessor'+side]['definitions'] == stages['candidate'+side]['definitions']
                   for side in ['Original', 'Returned']}
    result = dict(
        status='PASS', scope='Bounded retained evidence comparison; no new transport run or predecessor investigation',
        inputs=pins['inputs'], checkedInputIssues='None in all four securely parsed/offline-validated Exchange inputs',
        oldRelationships=len(old_ids), candidateRelationships=len(all_ids), newRelationships=len(new_ids),
        missingExistingOriginalRelationships=sorted(old_ids-all_ids),
        missingExistingReturnedRelationships=sorted(set(pr['relationships'])-set(cr['relationships'])),
        existingOriginalRelationshipDifferences=existing_original_differences,
        existingReturnedRelationshipDifferences=existing_returned_differences,
        newRelationshipIDs=sorted(new_ids), newKeyResolvedFieldOrOrderDifferences=new_differences,
        newRelationshipMissingAfter=sorted(new_ids-set(cr['relationships'])),
        rawFindingsWithinNewRelationshipRecords=new_relationship_findings,
        newElementIDs=sorted(new_elements), languageAdditionsOnNewElements=len(added_element_language_findings),
        allPropertyDefinitionsCrossReleaseEqual=definitions,
        allOriginalViewsCrossReleaseEqual=not view_original_differences and po['views'].keys()==co['views'].keys(),
        allReturnedViewsCrossReleaseEqual=not view_returned_differences and pr['views'].keys()==cr['views'].keys(),
        originalViewDifferences=view_original_differences, returnedViewDifferences=view_returned_differences,
        exactViewDiagnosticRecordsCrossReleaseEqual=view_dimensions,
        viewCount=len(co['views']), visualNodes=current['views']['nodesBefore'], visualConnections=current['views']['connectionsBefore'],
        newPatternClasses=[list(k) for k in sorted(c.keys()-p.keys())], patternCounts=rows,
        predecessorUnexplainedPatterns=previous['unexplainedPatternCount'], candidateUnexplainedPatterns=current['unexplainedPatternCount'],
        rawFindingCounts=dict(predecessor=previous['rawFindingCount'], candidate=current['rawFindingCount'], delta=current['rawFindingCount']-previous['rawFindingCount']),
        rawClassifications=dict(predecessor=previous['rawClassifications'], candidate=current['rawClassifications']),
        languageValuesByInput={k:v['languageValues'] for k,v in stages.items()},
        noGraphTextValueDifferences=current['semanticDimensions']['elements']['textAndNonLanguageAttributeDifferences']==[]
            and current['semanticDimensions']['relationships']['textAndNonLanguageAttributeDifferences']==[],
        navigationAfterSeparateOmissionAccountingEqual=current['navigation']['exactRemainingTreeMetadataReferencesAndOrderingEqual'],
        navigationExceptions=current['navigation']['exceptions'],
        generatedFolderIDsOmitted=dict(predecessor=previous['navigation']['omittedIdentifierCount'], candidate=current['navigation']['omittedIdentifierCount'],
                                      omittedWithinViews=current['navigation']['omittedIdentifiersWithinViews'],
                                      missingPriorOmissions=sorted(old_folders.keys()-current_folders.keys()),
                                      changedPriorOmissionRecords=mismatch(old_folders,current_folders),
                                      addedSourceCatalogueFolderRecords=added_folder_records),
        reconstructedLabels=dict(predecessor=previous['navigation']['omittedLabelsEqualReferencedNames'],candidate=current['navigation']['omittedLabelsEqualReferencedNames']),
        strictTransport='FAIL retained, not replayed or reclassified',
        architecturalAcceptance='CR-001 On-Hold / Unresolved; Solution Architect adjudication pending',
        limitations='No rendered-pixel/cross-platform proof, no second-pass candidate stability test, no recovery of lost folder IDs, original global sequence or implicit style state.'
    )
    assert len(new_ids)==62 and len(old_ids)==9314 and len(all_ids)==9376
    assert not old_ids-all_ids and not set(pr['relationships'])-set(cr['relationships'])
    assert not any([existing_original_differences,existing_returned_differences,new_differences,view_original_differences,view_returned_differences])
    assert not new_relationship_findings and len(new_elements)==11
    assert not old_folders.keys()-current_folders.keys() and not mismatch(old_folders,current_folders)
    assert len(added_folder_records)==16 and all(r['identifier'].startswith('catalogue-source-') and not r['withinViews'] for r in added_folder_records)
    assert all(view_dimensions.values()) and all(definitions.values())
    assert po['views'].keys()==co['views'].keys() and pr['views'].keys()==cr['views'].keys()
    assert not result['newPatternClasses'] and not result['candidateUnexplainedPatterns']
    assert result['noGraphTextValueDifferences'] and result['navigationAfterSeparateOmissionAccountingEqual']
    assert not result['navigationExceptions'] and not result['generatedFolderIDsOmitted']['omittedWithinViews']
    (HERE/'per-record-correspondence.json').write_text(json.dumps(dict(signatureContract=signature.__doc__,
         relationships=relationship_rows, views=view_rows),indent=2)+'\n')
    (HERE/'comparison.json').write_text(json.dumps(result,indent=2)+'\n')
    print(json.dumps({k:v for k,v in result.items() if k not in ['inputs','newRelationshipIDs']},indent=2))


if __name__ == '__main__':
    main()
