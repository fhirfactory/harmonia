#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Complete bounded source contributions using exact, already governed locators.

No allocation, name matching or classifier. Every selector below comes from a
persisted resource's source record. Original entry bytes are read through Git.
Reruns are keyed by source/range/identity and preserve already authored content.
"""
from collections import defaultdict
import os
import re
import architecture_continuum as c
import continuum_source_succession as s
from ingest_rad_continuum import BASE, ENTRY


def main():
    _, elements = c.load()
    capture = c.read_json(c.ROOT / BASE / 'architecture.json')
    ranges = defaultdict(set)
    texts = {}
    def source(path):
        if path not in texts: texts[path] = s.original(path).splitlines(keepends=True)
        return texts[path]
    for row in capture['elements']:
        cid = row['canonicalId']
        for loc in row.get('sources', []):
            spec = capture['sources'][loc['source']]
            path = spec['path']; lines = source(path)
            start = loc['line'] - 1
            # Exact source evidence must agree; never search for a similar name.
            if lines[start].strip() != loc['text'].strip():
                continue
            text = lines[start].strip()
            if text.startswith('#') and row['type'] == 'process' and loc['source'] in ('processes','assurance'):
                level = len(text.split()[0]); stop = start + 1
                while stop < len(lines) and not re.match(r'^#{1,'+str(level)+r'} ', lines[stop]): stop += 1
                while stop > start+1 and (not lines[stop-1].strip() or lines[stop-1].startswith('<a id=')): stop -= 1
            elif text.startswith('#') and row['type'] == 'capability' and loc['source'] in ('business','enabling','enterprise'):
                # Direct definition only. Child headings belong to their own elements.
                stop = start + 1
                while stop < len(lines) and not re.match(r'^#+ ', lines[stop]): stop += 1
                while stop > start+1 and (not lines[stop-1].strip() or lines[stop-1].startswith('<a id=')): stop -= 1
            elif not text.startswith('#') and re.match(r'\s*(?:- |\| |\*\*)', lines[start]) and row['type'] in ('capability','feature','function','service','process'):
                stop = start + 1
                # Wrapped prose belonging to that source assertion, not the next bullet.
                while stop < len(lines) and lines[stop].strip() and not re.match(r'\s*(?:[-*] |\||#|<a )', lines[stop]): stop += 1
            else: continue
            ranges[(path,start,stop)].add(cid)
    keys={r['key']:r['canonicalId'] for r in capture['elements']}
    for row in capture['relationships']:
        for loc in row.get('sources', []):
            path=capture['sources'][loc['source']]['path'];lines=source(path)
            start=loc['line']-1
            if lines[start].strip()==loc['text'].strip() and re.match(r'\s*(?:[-*] |\|)',lines[start]):
                ranges[(path,start,start+1)].add(keys[row['from']])
    report = []
    added = 0
    for (source_path,start,stop), ids in sorted(ranges.items()):
        # Overlapping locator rows sharing a heading are retained individually;
        # retirement merges spans and records every canonical successor.
        text = ''.join(texts[source_path][start:stop]).strip()
        selector = 'entry lines '+str(start+1)+'-'+str(stop)
        for cid in sorted(ids):
            m,body,path=elements[cid]
            # A source assertion may contribute to several independently bound
            # resources. It is provenance, not a second identity-binding claim.
            if not any(p['source']==source_path and p['selector']==selector for p in m['provenance']):
                m['provenance'].append({'source':source_path,'selector':selector,'role':'Exact source-locator contribution; original qualifications retained','revision':ENTRY})
                # Rebase local Markdown links using the explicit source base.
                def link(match):
                    target=match[1]
                    if re.match(r'^[a-z]+:|^//',target):return match[0]
                    base,sep,fragment=target.partition('#')
                    dest=(c.ROOT/source_path).parent/base if base else c.ROOT/source_path
                    return ']('+os.path.relpath(dest,path.parent)+(sep+fragment if sep else '')+')'
                rendered=re.sub(r'\]\(([^\s)]+)\)',link,text)
                body+='\n## Source-qualified contribution\n\nSource: `'+source_path+'` — '+selector+'. Standing: exact originating assertion; current resource standing unchanged.\n\n'+rendered+'\n'
                path.write_text(c.encode(m,body));elements[cid]=(m,body,path);added+=1
        report.append({'source':source_path,'startLine':start+1,'endLine':stop,'selector':selector,'canonicalIds':sorted(ids),'originalSha256':c.sha(''.join(texts[source_path][start:stop]).encode())})
    (s.AFTER/'source-range-correspondence.json').write_bytes(c.encoded(report))
    print({'addedContributions':added,'exactSourceRanges':len(report),'allocatedIdentities':0})

if __name__=='__main__':main()
