#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify publication hierarchy against ownership, and inspect the built PDF."""
import collections
import re
import subprocess

LEVELS=('chapter','section','subsection','subsubsection','paragraph','subparagraph')
LAYERS={
    'business':('model:bc','Business Capability Architecture'),
    'business_enabling':('model:bec','Business Enabling Capability Architecture'),
    'enterprise':('model:ec','Enterprise Capability Architecture'),
}


def ancestors(element,elements):
    result=[]
    while element.get('parent_key') or element.get('owner_key'):
        element=elements[element.get('parent_key') or element['owner_key']]
        assert element not in result,'Cyclic publication ancestry'
        result.insert(0,element)
    return result


def validate_source(elements,tex):
    entries=re.findall(r'\\(chapter|section|subsection|subsubsection|paragraph|subparagraph)\{([^\n]*)\}\n\\label\{([^}]+)\}',tex)
    published={label:(level,title) for level,title,label in entries}
    for element in elements.values():
        depth=len(ancestors(element,elements))
        assert published[element['latex_label']][0]==LEVELS[depth], 'Heading does not follow actual ownership: '+element['reference_id']
    for label,title in LAYERS.values():
        assert '\\part{'+title+'}\\label{'+label+'}' in tex,'Missing architectural Part'
    assert '\\setcounter{secnumdepth}{5}' in tex,'Deep field numbering disabled'
    fields=collections.Counter(re.findall(r'\\cataloguefield\{([^}]+)\}\{',tex))
    assert fields['paragraph']>0,'Feature/behaviour fields lack paragraph numbering'
    return {'numbered_architectural_entries':len(elements),'numbered_catalogue_fields':sum(fields.values()),
            'catalogue_fields_by_heading_level':dict(fields)}


def braced_groups(text):
    """Read nested TeX groups, retaining escaped braces inside heading names."""
    result=[];depth=0;start=0;i=0
    while i<len(text):
        c=text[i]
        if c=='\\':
            i+=2;continue
        if c=='{':
            if depth==0:start=i+1
            depth+=1
        elif c=='}':
            depth-=1
            if depth==0:result.append(text[start:i])
        i+=1
    assert depth==0,'Unbalanced auxiliary groups'
    return result


def validate_pdf(elements,tex,pdf):
    aux={}
    for line in pdf.with_suffix('.aux').read_text().splitlines():
        if line.startswith('\\newlabel{'):
            label,values=line[len('\\newlabel{'):].split('}',1)
            aux[label]=braced_groups(braced_groups(values)[0])
    output=subprocess.check_output(['mutool','show',str(pdf),'outline'],text=True,timeout=30)
    outline={};path=[]
    for line in output.splitlines():
        match=re.fullmatch(r'[|+\-](\t+)"(.*)"\t#nameddest=(.+)',line)
        assert match,'Unrecognised PDF outline entry: '+line
        tabs,title,destination=match.groups();depth=len(tabs)-1
        assert depth<=len(path),'Skipped outline level'
        path=path[:depth]
        assert destination not in outline,'Duplicate PDF outline destination'
        outline[destination]={'parent':path[-1] if path else None,'depth':depth,'title':title}
        path.append(destination)
    toc=pdf.with_suffix('.toc').read_text()
    toc_destinations=set(re.findall(r'\{([^{}]+)\}%$',toc,re.M))
    for label,title in LAYERS.values():
        destination=aux[label][3]
        assert outline[destination]['parent'] is None,'Architectural Part is not top-level'
        assert outline[destination]['title'].endswith(title),'Architectural Part title lost'
        assert re.search(r'\\contentsline \{part\}.*\{'+re.escape(destination)+r'\}',toc),'Part missing from contents'
    for element in elements.values():
        chain=ancestors(element,elements)
        number,_,_,destination,*_=aux[element['latex_label']]
        assert re.fullmatch(r'\d+(?:\.\d+){'+str(len(chain))+r'}',number),'Missing/contextless publication number'
        expected_parent=chain[-1]['latex_label'] if chain else LAYERS[element['category']][0]
        assert outline[destination]['parent']==aux[expected_parent][3],'PDF outline changes ownership: '+element['reference_id']
        assert destination in toc_destinations,'Architectural entry missing from contents'
        prefix=element['tier']+': ' if element['type'] in ('capability','enterprise_capability') else element['type'].title()+': '
        assert prefix in outline[destination]['title'],'Architectural type absent from bookmark'
    # Inspect rendered text, not merely the configured numbering depth.
    text=subprocess.check_output(['pdftotext','-layout',str(pdf),'-'],text=True,timeout=60)
    headings=('Capability Overview','Feature Overview','Function Overview','Service Overview','Process Overview',
              'Definition / governed semantic detail','Intent','Boundary','Harmonia Architectural Relevance',
              'Scope / Contained Elements','Relationships / Architectural Relevance','Derivation Provenance')
    printed={}
    for title in headings:
        expected=len(re.findall(r'\\cataloguefield\{[^}]+\}\{'+re.escape(title)+r'\}',tex))
        if expected:
            pattern=r'^\s*\d+(?:\.\d+)+\.?\s+'+r'\s+'.join(re.escape(w) for w in title.split())+r'\s*$'
            count=len(re.findall(pattern,text,re.M))
            assert count==expected,f'Numbered PDF heading coverage: {title}: {count}/{expected}'
            printed[title]=count
    return {'pdf_outline_matches_all_element_ancestry':True,'architectural_parts_top_level':True,
            'parts_and_all_elements_in_contents':True,'pdf_outline_entries':len(outline),
            'numbered_pdf_catalogue_headings_verified':printed}
