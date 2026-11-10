#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Bounded migration investigation; retrieval scores are evidence, not decisions."""
import collections
import gzip
import json
import math
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[8]
sys.path.insert(0, str(ROOT / 'enterprise-architecture/tooling'))
import architecture_continuum as c

BASE = ROOT / 'enterprise-architecture/design/evidence/1.1.20/001'
OUT = BASE / 'after/capability-model-replacement'
FR = BASE / 'after/pass3-fr-reconciliation'
SNAPSHOT = json.loads(gzip.decompress((BASE / 'before/capability-model-replacement/baseline.json.gz').read_bytes()))
OLD = {k: v for k, v in SNAPSHOT.items() if v['metadata']['archimateType'] == 'Capability'}
TARGET = {}
for source in ('capability-reconciliation.json', 'feature-reconciliation.json'):
    for row in c.read_json(FR / source):
        row = dict(row)
        row['level'] = row.get('level', 4)
        row['parent'] = row.get('parent', row['id'].rsplit('.', 1)[0] if '.' in row['id'] else None)
        TARGET[row['id']] = row

STOP = set('a an the and or of to for by with within from through is are be can has have its that this their as on at into each any all applicable appropriate relevant governed management administration support enablement information health clinical service services operational current'.split())
ALIASES = {'authorisation': 'authorization', 'authorise': 'authorize', 'organisation': 'organization', 'organisational': 'organization', 'utilisation': 'utilization', 'recognition': 'recognize', 'determination': 'determine', 'evaluation': 'evaluate', 'assessment': 'assess', 'administration': 'administer', 'management': 'manage', 'provision': 'provide', 'retrieval': 'retrieve', 'communication': 'communicate', 'qualification': 'qualify', 'patient': 'person', 'client': 'person', 'notification': 'notify', 'configuration': 'configure', 'preservation': 'preserve', 'verification': 'verify', 'discovery': 'discover', 'continuation': 'continue', 'continuity': 'continue'}

def tokens(text):
    text = re.sub(r'([a-z])([A-Z])', r'\1 \2', text)
    words = re.findall(r'[a-z][a-z0-9]+', text.lower())
    words = [ALIASES.get(w, w) for w in words if w not in STOP]
    words = [w[:-3] if len(w) > 6 and w.endswith('ing') else w[:-1] if len(w) > 4 and w.endswith('s') else w for w in words]
    return words + [a + '_' + b for a, b in zip(words, words[1:])]

def ancestry(code):
    row = TARGET[code]
    return (ancestry(row['parent']) if row.get('parent') else []) + [code]

def old_text(cid):
    x = OLD[cid]; m = x['metadata']
    definition = x['body'].split('## Architectural content\n', 1)[1].split('**Overview**')[0].strip()
    return (m['displayName'] + ' ') * 7 + (definition + ' ') * 2 + (m.get('friendlyName') or '').replace('.', ' ')

class Retrieval:
    def __init__(self):
        bags = {}
        for code, row in TARGET.items():
            bag = collections.Counter()
            for text, weight in [(row['name'], 5), (row['description'], 2), (' '.join(TARGET[a]['name'] for a in ancestry(code)[:-1]), 1)]:
                for t in tokens(text): bag[t] += weight
            bags[code] = bag
        df = collections.Counter(t for bag in bags.values() for t in bag)
        self.idf = {t: math.log(1 + len(bags) / n) for t, n in df.items()}
        self.bags = {code: {t: (1 + math.log(n)) * self.idf[t] for t, n in bag.items()} for code, bag in bags.items()}
        self.norms = {code: math.sqrt(sum(w*w for w in bag.values())) for code, bag in self.bags.items()}

    def search(self, text, level=None, allowed=None, limit=5):
        query = collections.Counter(tokens(text))
        query = {t: (1 + math.log(n)) * self.idf.get(t, 0) for t, n in query.items()}
        norm = math.sqrt(sum(w*w for w in query.values())) or 1
        scores = []
        for code, bag in self.bags.items():
            if level and TARGET[code]['level'] != level: continue
            if allowed and not any(code == p or code.startswith(p + '.') for p in allowed): continue
            score = sum(w * bag.get(t, 0) for t, w in query.items()) / (norm * self.norms[code])
            scores.append((score, code))
        return [{'id': code, 'score': round(score, 5), 'name': TARGET[code]['name']} for score, code in sorted(scores, reverse=True)[:limit]]

def main():
    retrieval = Retrieval()
    rows = []
    for cid, x in OLD.items():
        m = x['metadata']
        if m['identity'].get('correspondence'): continue
        match = re.match(r'\[CT([123])\]', m['displayName'])
        level = int(match[1]) if match else 4
        matches = retrieval.search(old_text(cid), level=level)
        rows.append({'canonicalId': cid, 'name': m['displayName'], 'body': x['body'], 'level': level, 'suggestions': matches})
    OUT.mkdir(exist_ok=True, parents=True)
    (OUT / 'retrieval-investigation.json').write_bytes(c.encoded(rows))
    for row in sorted(rows, key=lambda x: (x['level'], x['name'])):
        print(row['canonicalId'][:8], row['name'], '=>', '; '.join(x['id'] + ' ' + x['name'] for x in row['suggestions'][:2]))

if __name__ == '__main__': main()
