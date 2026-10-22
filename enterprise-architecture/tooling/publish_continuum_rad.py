#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Publish the Continuum RAD using current model navigation and canonical prose.

Presentation only: no source discovery, reconciliation, identity allocation,
architecture enrichment, model mutation or dependency on the superseded RAD.
"""
import argparse
from collections import Counter, defaultdict
import json
import os
from pathlib import Path
import re
from types import SimpleNamespace
from urllib.parse import unquote, urlsplit

from lxml import html
import markdown
import architecture_continuum as c
import ea_references
from publish_continuum import tex
import relationship_state

SOURCE = c.DOCUMENTATION / 'continuum-rad'
HISTORICAL = {'historical_reference', 'superseded'}
HEADINGS = ('chapter', 'section', 'subsection', 'subsubsection', 'paragraph', 'subparagraph',
            'subsubparagraph', 'subsubsubparagraph')


def label(cid):
    return 'element:' + cid


def ref(cid, elements):
    return r'\hyperref[' + label(cid) + ']{' + tex(elements[cid][0]['displayName']) + '}'


class Prose:
    """Bounded Markdown-to-LaTeX rendering of the existing canonical prose."""
    def __init__(self, root, output, elements):
        self.root, self.output, self.elements = root, output, elements
        self.paths = {path.resolve(): cid for cid, (_, _, path) in elements.items()}
        self.successors = ea_references.successors(root)

    def link(self, href, text, path):
        url = urlsplit(href)
        if not url.scheme and not href.startswith('//'):
            target = (path.parent / unquote(url.path)).resolve() if url.path else path
            target = ea_references.current_target(self.root, target, self.successors)
            if target in self.paths:
                return r'\hyperref[' + label(self.paths[target]) + ']{' + text + '}'
            href = os.path.relpath(target, self.output) + ('#' + url.fragment if url.fragment else '')
        # \detokenize protects source URL characters from TeX interpretation.
        c.require(not any(x in href for x in '{}\\\n'), 'Unsafe publication URL: ' + href)
        return r'\href{\detokenize{' + href + '}}{' + text + '}'

    def children(self, node, path):
        result = self.text(node.text or '')
        for child in node:
            result += self.node(child, path) + self.text(child.tail or '')
        return result

    def text(self, value):
        # Canonical prose sometimes spells a URL literally rather than as a
        # Markdown link. Keep it breakable in the page instead of clipping it.
        pieces = re.split(r'(https?://[^\s<>(){}]+)', value)
        return ''.join(r'\url{' + piece + '}' if re.match(r'https?://', piece) else tex(piece)
                       for piece in pieces)

    def node(self, node, path):
        tag = node.tag
        if not isinstance(tag, str):
            return ''
        if tag == 'pre':
            text = ''.join(node.itertext())
            # Literal diagrams remain legible source; never execute their code.
            return '\n\\begin{quote}\\small\\ttfamily\n' + tex(text).replace('\n', '\\\\\n') + '\n\\end{quote}\n'
        if tag == 'table':
            rows = node.xpath('.//tr')
            if not rows:
                return ''
            cols = max(len(row) for row in rows)
            widths = r'>{\raggedright\arraybackslash}p{\dimexpr\linewidth/' + str(cols) + r'-2\tabcolsep\relax}'
            out = ['\n\\begin{longtable}{' + widths * cols + '}']
            for row in rows:
                out.append(' & '.join(self.children(cell, path) for cell in row) + r' \\')
            return '\n'.join(out) + '\n\\end{longtable}\n'
        text = self.children(node, path)
        if tag == 'a':
            return self.link(node.get('href'), text, path) if node.get('href') else text
        if tag == 'strong':
            return r'\textbf{' + text + '}'
        if tag == 'em':
            return r'\emph{' + text + '}'
        if tag == 'code':
            return r'\texttt{\seqsplit{' + text + '}}'
        if tag in ('ul', 'ol'):
            env = 'itemize' if tag == 'ul' else 'enumerate'
            return '\n\\begin{' + env + '}\n' + text + '\n\\end{' + env + '}\n'
        if tag == 'li':
            return '\n\\item ' + text
        if tag == 'blockquote':
            return '\n\\begin{quote}\n' + text + '\n\\end{quote}\n'
        if tag in ('h1', 'h2', 'h3', 'h4', 'h5', 'h6'):
            # Source subheadings are fields, not a second document hierarchy.
            return '\n\\par\\medskip\\textbf{' + text + '}\\par\n'
        if tag == 'p':
            return '\n' + text + '\n\n'
        if tag == 'br':
            return r'\newline{}'
        if tag == 'hr':
            return '\n\\medskip\\hrule\\medskip\n'
        return text

    def render(self, text, path):
        fragment = html.fragment_fromstring(markdown.markdown(text, extensions=['tables', 'fenced_code']), create_parent='div')
        return self.children(fragment, path)


def hierarchy(model, catalogue, elements):
    """Partition the complete existing model organisation by native EA domain.

    Every chapter/section is an actual organisation node, including the existing
    projector's Continuum additions branch. Classification selects the Part only;
    it never creates a replacement chapter, ancestry or historical appendix.
    """
    by_rep = {m['exchange']['identifier']: cid for cid, (m, _, _) in elements.items()}
    domain_for_type = {native: Path(profile['directory']).parts[0]
                       for native, profile in catalogue['types'].items()}
    organisations = model.find('a:organizations', c.N)
    domain_nodes = {node.findtext('a:label', namespaces=c.N).casefold(): node
                    for node in organisations.findall('a:item', c.N)
                    if node.findtext('a:label', namespaces=c.N)}
    order = list(dict.fromkeys(domain_for_type.values()))

    def walk(node, domain, path):
        text = node.findtext('a:label', namespaces=c.N)
        cid = by_rep.get(node.get('identifierRef'))
        if cid and domain_for_type[elements[cid][0]['archimateType']] != domain:
            return None
        title = text or (elements[cid][0]['displayName'] if cid else None)
        result = {'title': title, 'origin': 'model element reference' if cid else 'model organisation',
                  'locator': model.getroottree().getpath(node), 'modelIdentifier': node.get('identifier'),
                  'path': path + ([title] if title else []), 'elements': [], 'children': []}
        if cid:
            result['elements'].append(cid)
        for child in node.findall('a:item', c.N):
            item = walk(child, domain, result['path'])
            if item:
                c.require(item['title'], 'Unlabelled model container needs explicit publication treatment: ' + item['locator'])
                result['children'].append(item)
        return result if result['elements'] or result['children'] else None

    parts = []
    for domain in order:
        domain_node = domain_nodes.get(domain)
        relevant = [cid for cid, (m, _, _) in elements.items() if domain_for_type[m['archimateType']] == domain]
        if not relevant:
            continue
        c.require(domain_node is not None, 'Populated EA domain lacks an existing model organisation: ' + domain)
        part = walk(domain_node, domain, [])
        c.require(part is not None, 'Empty model domain organisation: ' + domain)
        for node in organisations.findall('a:item', c.N):
            title = node.findtext('a:label', namespaces=c.N)
            if title and title.casefold() not in order:
                branch = walk(node, domain, [])
                if branch:
                    part['children'].append(branch)
        parts.append(part)
    c.require(Counter(cid for part in parts for cid in descendants(part)) == Counter(elements.keys()),
              'Existing model organisation must account for every canonical element exactly once')
    return parts


def descendants(node):
    return node['elements'] + [cid for child in node['children'] for cid in descendants(child)]


def diagram(node, elements, population):
    """One small explicit Composition neighbourhood; never synthesize an edge."""
    ids = set(descendants(node))
    reps = {elements[cid][0]['exchange']['identifier']: cid for cid in ids}
    candidates = defaultdict(list)
    for cid in sorted(ids):
        m = elements[cid][0]
        if m['standing']['currentness'] in HISTORICAL:
            continue
        for edge in m.get('relationships', []):
            target = reps.get(edge['target'])
            if (edge['identifier'] in population['eligible'] and edge['archimateType'] == 'Composition'
                    and target and elements[target][0]['standing']['currentness'] not in HISTORICAL):
                candidates[cid].append((target, edge))
    if not candidates:
        return '', []
    source = min(candidates, key=lambda cid: (-len(candidates[cid]), elements[cid][0]['displayName'], cid))
    edges = sorted(candidates[source], key=lambda pair: (elements[pair[0]][0]['displayName'], pair[0]))[:6]
    out = [r'\begin{center}\begin{tikzpicture}[>=Stealth,every node/.style={draw=Ink!40,rounded corners,align=left,font=\small,text width=5.2cm,inner sep=6pt}]',
           r'\node (source) at (0,0) {' + ref(source, elements) + '};']
    records = []
    for i, (target, edge) in enumerate(edges):
        y = round((len(edges) - 1) * .85 - i * 1.7, 2)
        out.append(r'\node (target' + str(i) + ') at (8,' + str(y) + ') {' + ref(target, elements) + '};')
        state = population['states'].get(edge['identifier'], 'unclassified proposal')
        status = edge['standing'].get('v0.2 / Assertion Status') or edge['standing'].get('semantic') or 'source-qualified'
        out.append(r'\draw[->] (source.east) -- (3,0) |- (target' + str(i) + '.west);')
        out.append(r'\node[draw=none,text width=1.8cm,font=\tiny,align=center,inner sep=0pt] at (4.15,' + str(round(y + .38, 2)) + r') {Composition\newline{}' + tex(status + ' / ' + state) + '};')
        records.append({'sourceCanonicalId': source, 'targetCanonicalId': target, 'relationship': edge, 'recordState': state})
    out.append(r'\end{tikzpicture}\end{center}')
    out.append(r'{\small Selected outgoing Composition assertions; arrows retain source-to-target direction. '
               r'Labels retain assertion standing and record state; element standing accompanies each description. '
               r'\href{structure.json}{Complete relationship qualifications and provenance} remain source-defined. '
               r'No omitted edge or semantic effect is inferred.}\par')
    return '\n'.join(out), records


def outputs(root, output):
    # Use the full governed projection, including its existing additions
    # organisations. A presentation-template fragment is not the full hierarchy.
    model_bytes, _ = c.project(root)
    from lxml import etree as ET
    model = ET.fromstring(model_bytes)
    catalogue, elements = c.load(root)
    release = c.read_json(root / 'architecture-release.json')
    tree = hierarchy(model, catalogue, elements)
    prose = Prose(root, output, elements)
    notes = c.read_json(root / SOURCE / 'reader-notes.json')
    # Reuse the existing source-qualified relationship population selector.
    keys = {p.get('identifier'): p.findtext('a:name', namespaces=c.N) for p in model.findall('a:propertyDefinitions/a:propertyDefinition', c.N)}
    relationships = {}
    for m, _, _ in elements.values():
        for edge in m.get('relationships', []):
            node = ET.Element(c.tag('relationship'))
            c.properties(node, edge.get('properties', []))
            relationships[edge['identifier']] = node
    population = relationship_state.exchange_population(SimpleNamespace(keys=keys, relationships=relationships))
    artifacts, headings, placements, diagrams = {}, [], [], []

    def heading(node, depth, path):
        c.require(depth < len(HEADINGS), 'Model hierarchy exceeds declared LaTeX section depth')
        headings.append({'title': node['title'], 'depth': depth, 'origin': node['origin'],
                         'locator': node['locator'], 'modelIdentifier': node['modelIdentifier'], 'path': path})
        return '\\' + HEADINGS[depth] + '{' + tex(node['title']) + '}\n'

    def entry(cid, path, locator):
        m, body, source = elements[cid]
        placements.append({'canonicalId': cid, 'path': path, 'modelLocator': locator, 'source': str(source.relative_to(root)), 'sourceSha256': c.sha(source.read_bytes()), 'standing': m['standing'], 'unresolvedProperties': m.get('unresolvedProperties', {}), 'sourceUnresolved': m.get('sourceUnresolved'), 'publication': 'model narrative'})
        link = os.path.relpath(source, output)
        if m['standing']['currentness'] in HISTORICAL:
            return (r'\par\label{' + label(cid) + '}' + '\n'
                    + tex(m['standing']['semantic'] + ' / ' + m['standing']['currentness'])
                    + r'. \href{' + link + r'}{Historical canonical record}.\par' + '\n')
        result = [r'\label{' + label(cid) + '}',
                  r'\index{' + tex(m['displayName']).replace('!', '"!').replace('@', '"@').replace('|', '"|') + '}',
                  r'{\small ' + tex(m['archimateType'] + (' / ' + m['specialization'] if m.get('specialization') else '') + ' · ' + m['standing']['semantic'] + ' · ' + m['standing']['currentness']) + r'}\par']
        # Definitions and boundaries remain source prose. Repeated source
        # contributions/history stay in the linked canonical record and Manifest.
        content = body.split('## Architectural content\n', 1)[1].split('\n## ', 1)[0].strip()
        result.append(prose.render(content, source))
        if m.get('unresolvedProperties') or m.get('sourceUnresolved'):
            result.append(r'{\small Recorded unresolved matters: ' + tex(', '.join(sorted(m.get('unresolvedProperties', {}))) or 'source-qualified matters') + r'; see the canonical record for their exact scope.}\par')
        result.append(r'{\footnotesize \href{' + link + r'}{Canonical record: ' + cid + r'}; full qualifications, contributions and provenance.}\par')
        return '\n'.join(result)

    def render(node, depth, path):
        path = path + [node['title']]
        out = [heading(node, depth, path)]
        out += [entry(cid, path, node['locator']) for cid in node['elements']]
        for child in node['children']:
            out.append(render(child, depth + 1, path))
        return '\n'.join(out)

    includes = []
    for part in tree:
        domain = part['title'].casefold()
        headings.append({'title': part['title'], 'depth': -1, 'origin': part['origin'], 'locator': part['locator'], 'modelIdentifier': part['modelIdentifier'], 'path': [part['title']]})
        out = [r'\part{' + tex(part['title']) + '}']
        for note in notes.get(domain, []):
            c.require(note['sources'] and all(cid in elements for cid in note['sources']), 'Unresolved reader-note source')
            out.append(tex(note['text']) + '\n\n' + r'{\small Reading sources: ' + '; '.join(ref(cid, elements) for cid in note['sources']) + r'.}\par')
        picture, records = diagram(part, elements, population)
        if records:
            out += [picture]
            diagrams.append({'domain': domain, 'selection': 'Largest explicit active Composition neighbourhood, at most six targets; stable name/identity ordering', 'edges': records})
        for cid in part['elements']:
            out.append(entry(cid, [part['title']], part['locator']))
        for child in part['children']:
            out.append(render(child, 0, [part['title']]))
        name = 'parts/' + domain.replace(' ', '-') + '.tex'
        artifacts[name] = ('\n'.join(out) + '\n').encode()
        includes.append(r'\input{' + name + '}')
    c.require(Counter(item['canonicalId'] for item in placements) == Counter(elements.keys()), 'RAD must account for every canonical element exactly once')
    template = (root / SOURCE / 'templates/publication.tex').read_text()
    master = template.replace('@VERSION@', release['version']).replace('@PREVIOUS@', release['previous_version']).replace('@CONTENT@', '\n'.join(includes))
    c.require('@CONTENT@' not in master, 'Unexpanded publication template')
    artifacts['continuum-rad.tex'] = master.encode()
    artifacts['structure.json'] = c.encoded({'release': release, 'hierarchySource': str(c.PUBLICATION / release['version'] / 'Harmonia-Architecture.xml'), 'modelSha256': c.sha(model_bytes), 'domainClassificationSource': str(c.CONFIG / 'type-catalogue.json'), 'domainPartition': 'EA domains supply Parts; within each Part, existing model organisation nodes retain labels, relative order and nesting. Existing cross-domain Continuum additions is filtered by native domain without creating chapter or section labels. Historical records remain in their model branches.', 'headings': headings, 'elements': placements, 'diagrams': diagrams, 'limits': 'Organisation is navigation, not inferred containment. No fallback chapters, independent narrative taxonomy, historical appendix or truncated section depth.'})
    artifacts['README.md'] = (f'# Harmonia Architecture Continuum RAD — {release["version"]}\n\n'
        '[Read the PDF](continuum-rad.pdf) · [LaTeX source](continuum-rad.tex) · [Model correspondence](structure.json)\n\n'
        'Narrative publication of canonical Continuum definitions, following the complete governed model hierarchy. '
        'Each element retains independent standing and model placement, including historical branches. '
        'The publication is derived and supplies no architectural acceptance.\n').encode()
    artifacts['publication.json'] = c.encoded({'status': 'PASS', 'release': release, 'canonicalSources': len(elements), 'narrativeDescriptions': sum(p['standing']['currentness'] not in HISTORICAL for p in placements), 'historicalReferencesInModelHierarchy': sum(p['standing']['currentness'] in HISTORICAL for p in placements), 'architectureHeadings': len(headings), 'architectureHeadingOrigins': dict(Counter(h['origin'] for h in headings)), 'aiAssistance': 'Retained source-cited reader notes and deterministic presentation code; no model/API call during generation. Canonical descriptions are rendered without semantic rewriting. Diagrams select existing native assertions with their direction/state/standing.', 'sourceInputs': ['canonical Continuum Markdown', str(c.DESIGN / 'projection-template.xml'), str(c.CONFIG / 'type-catalogue.json'), str(SOURCE / 'reader-notes.json'), str(SOURCE / 'templates/publication.tex'), 'architecture-release.json'], 'allocatedIdentities': 0, 'legacyRadIngested': False, 'sha256': {name: c.sha(data) for name, data in sorted(artifacts.items())}})
    return artifacts


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--root', type=Path, default=c.ROOT)
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--check', action='store_true')
    args = parser.parse_args()
    artifacts = outputs(args.root.resolve(), args.output.resolve())
    for name, data in artifacts.items():
        path = args.output / name
        if args.check:
            c.require(path.exists() and path.read_bytes() == data, 'Missing/stale RAD publication: ' + str(path))
        else:
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
    print(json.dumps({'status': 'PASS', 'artifacts': len(artifacts), 'check': args.check}))


if __name__ == '__main__':
    main()
