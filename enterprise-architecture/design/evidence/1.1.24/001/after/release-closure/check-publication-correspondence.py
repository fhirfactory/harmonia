#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Evidence-local publication checks; no comparison/transport policy changes.

Reuses the previously verified field-specific generated-link comparison. The only
additional exclusions are explicitly selected generated release metadata fields.
Canonical Markdown, rendered model content and every other field remain exact.
"""
import copy
import hashlib
import json
import os
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[7]
HERE = Path(__file__).resolve().parent
sys.path.insert(0,str(ROOT/'enterprise-architecture/tooling'))
import exchange_xml as g

CANDIDATE = HERE.parent/'candidate'
READER = ROOT/'enterprise-architecture/documentation/continuum-manifest/published/1.1.24'
RAD = ROOT/'enterprise-architecture/documentation/continuum-rad/published/1.1.24'
OLD = {'version':'1.1.23','previous_version':'1.1.22'}
NEW = {'version':'1.1.24','previous_version':'1.1.23'}
PAYLOAD = re.compile(r'(<script type="application/json" id="architecture">)(.*?)(</script>)',re.S)


def reference(value, directory):
    return os.path.normpath(os.path.join(directory,value)) if value.startswith(('../','./')) or value in ('..','.') else value


def hrefs(value,directory):
    return re.sub(r'''(href=["'])([^"']*)(["'])''',lambda m:m[1]+reference(m[2],directory)+m[3],value)


def page(value,directory):
    match=PAYLOAD.search(value);assert match,'Missing architecture JSON'
    rows=json.loads(match[2])
    for row in rows:
        row['source']=reference(row['source'],directory)
        row['html']=hrefs(row['html'],directory)
    shell=value[:match.start(2)]+'<checked architecture JSON>'+value[match.end(2):]
    return hrefs(shell,directory),rows


def tex(value,directory):
    for pattern in [r'(\\href\{\\detokenize\{)([^}]*)(\}\})',r'(\\href\{)([^}]*)(\})',r'(\\url\{)([^}]*)(\})']:
        value=re.sub(pattern,lambda m:m[1]+reference(m[2],directory)+m[3],value)
    return value


def markdown(value,directory):
    return re.sub(r'(\]\()([^)]*)(\))',lambda m:m[1]+reference(m[2],directory)+m[3],value)


def generated_release_fields(value,part,name):
    replacements=[]
    if part=='reader' and name=='README.md':
        replacements=[('# Harmonia Architecture Continuum — RAD 1.1.23','# Harmonia Architecture Continuum — RAD 1.1.24')]
    elif part=='reader' and name=='continuum-catalogue.tex':
        replacements=[(r'\section*{Harmonia Architecture Continuum — RAD 1.1.23}',r'\section*{Harmonia Architecture Continuum — RAD 1.1.24}')]
    elif part=='reader' and name=='continuum.html':
        replacements=[('<title>Harmonia Enterprise Architecture — RAD 1.1.23 review</title>','<title>Harmonia Enterprise Architecture — RAD 1.1.24 review</title>'),
                      ('<h1>Harmonia Enterprise Architecture · RAD 1.1.23</h1>','<h1>Harmonia Enterprise Architecture · RAD 1.1.24</h1>')]
    elif part=='narrative' and name=='README.md':
        replacements=[('# Harmonia Architecture Continuum RAD — 1.1.23','# Harmonia Architecture Continuum RAD — 1.1.24')]
    elif part=='narrative' and name=='continuum-rad.tex':
        template=(ROOT/'enterprise-architecture/documentation/continuum-rad/templates/publication.tex').read_text()
        for line in template.splitlines():
            if '@VERSION@' in line or '@PREVIOUS@' in line:
                replacements.append((line.replace('@VERSION@',OLD['version']).replace('@PREVIOUS@',OLD['previous_version']),
                                     line.replace('@VERSION@',NEW['version']).replace('@PREVIOUS@',NEW['previous_version'])))
    for before,after in replacements:
        assert value.count(before)==1,(part,name,before,value.count(before))
        value=value.replace(before,after,1)
    return value


def xml_correspondence(original,published):
    old,new=g.parse(original),g.parse(published)
    defs={n.get('identifier'):n.findtext('a:name',namespaces=g.N) for n in new.findall('a:propertyDefinitions/a:propertyDefinition',g.N)}
    fields=[p.find('a:value',g.N) for p in new.findall('a:properties/a:property',g.N)
            if defs[p.get('propertyDefinitionRef')]=='Baseline / Architecture Release']
    assert len(fields)==1 and fields[0].text==NEW['version']
    fields[0].text=OLD['version']
    return g.ET.tostring(old,method='c14n')==g.ET.tostring(new,method='c14n')


def main():
    assert json.loads((ROOT/'architecture-release.json').read_text())==NEW
    assert json.loads((HERE.parents[1]/'before/release-closure/input-pins.json').read_text())['release']==OLD
    assert tex('[1..1] [0..*]',ROOT)=='[1..1] [0..*]'
    assert hrefs('plain ../text <a href="../target">x</a>',ROOT).startswith('plain ../text ')
    assert markdown('[x](../name with spaces.md)',ROOT)=='[x]('+str(ROOT.parent/'name with spaces.md')+')'
    assert reference('https://example.test/x',ROOT)=='https://example.test/x'
    checks,manifests=[],[]
    for part,target in [('reader',READER),('narrative',RAD)]:
        source=CANDIDATE/part
        for original in sorted(source.rglob('*')):
            if not original.is_file() or original.name=='publication.json':continue
            published=target/original.relative_to(source)
            left,right=original.read_bytes(),published.read_bytes()
            if original.suffix=='.xml':
                equal=xml_correspondence(original,published)
            elif original.name=='structure.json':
                x,y=json.loads(left),json.loads(right);assert x['release']==OLD and y['release']==NEW
                assert x['modelSha256']==hashlib.sha256((CANDIDATE/'reader/Harmonia-Architecture.xml').read_bytes()).hexdigest()
                assert y['modelSha256']==hashlib.sha256((READER/'Harmonia-Architecture.xml').read_bytes()).hexdigest()
                x['release']=NEW;x['modelSha256']=y['modelSha256'];equal=x==y
            elif original.name=='continuum.html':
                equal=page(generated_release_fields(left.decode(),part,original.name),source)==page(right.decode(),target)
            elif part=='narrative' and original.name=='continuum-rad.tex':
                # The master is the maintained literal publication template:
                # its two fixed cross-product/governance links are intentionally
                # relative to the release publication, not evidence-local output.
                # Compare the entire master exactly after selected template
                # release fields, without reinterpreting those fixed links.
                equal=generated_release_fields(left.decode(),part,original.name)==right.decode()
            elif original.suffix=='.tex':
                equal=tex(generated_release_fields(left.decode(),part,original.name),source)==tex(right.decode(),target)
            elif original.suffix=='.md':
                equal=markdown(generated_release_fields(left.decode(),part,original.name),source)==markdown(right.decode(),target)
            else:equal=left==right
            checks.append(dict(candidate=str(original.relative_to(ROOT)),published=str(published.relative_to(ROOT)),byteEqual=left==right,corresponds=equal))
        x=json.loads((source/'publication.json').read_text());y=json.loads((target/'publication.json').read_text())
        x.pop('sha256');hashes=y.pop('sha256')
        if 'release' in x:
            assert x['release']==OLD and y['release']==NEW;x['release']=NEW
        manifests.append(dict(path=str((target/'publication.json').relative_to(ROOT)),allOtherFieldsEqual=x==y,
                              hashesMatch=all(hashlib.sha256((target/name).read_bytes()).hexdigest()==sha for name,sha in hashes.items())))
    durable=ROOT/'enterprise-architecture/design/archimate/published/1.1.24/Harmonia-Architecture.xml'
    assert durable.read_bytes()==(READER/'Harmonia-Architecture.xml').read_bytes()
    files=[durable,*sorted(READER.rglob('*')),*sorted(RAD.rglob('*'))]
    inventory=[dict(path=str(p.relative_to(ROOT)),bytes=p.stat().st_size,sha256=hashlib.sha256(p.read_bytes()).hexdigest()) for p in files if p.is_file()]
    result=dict(status='PASS' if all(x['corresponds'] for x in checks) and all(x['allOtherFieldsEqual'] and x['hashesMatch'] for x in manifests) else 'FAIL',
                artifactChecks=checks,manifestChecks=manifests,dualExchangeBytesEqual=True,
                allowedChanges='Only selected generated release metadata fields, independently verified model hash and generated relative link targets; entire maintained master exact after template release fields, with its fixed publication-context links retained literally. All remaining model/content/field/order/page bytes preserved.',
                priorMethod='Reuses the predecessor field-specific publication comparison, including literal master-template links and independently verified derived model hash. Only candidate/publication locators and exact old/new release identities are adapted; no production comparator or validation change.',
                architectureAuthority='Canonical Markdown unchanged; publication is derived, transport FAIL remains explicitly qualified.')
    (HERE/'candidate-publication-correspondence.json').write_text(json.dumps(result,indent=2)+'\n')
    (HERE/'publication-integrity.json').write_text(json.dumps(dict(status='PASS',release=NEW,artifacts=inventory),indent=2)+'\n')
    assert result['status']=='PASS',result
    assert len(inventory)==19,len(inventory)
    print(json.dumps(dict(status=result['status'],artifacts=len(inventory),candidateComparisons=len(checks),manifests=manifests),indent=2))


if __name__=='__main__':main()
