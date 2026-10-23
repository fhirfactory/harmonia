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
import continuum_navigation as nav

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


def descendants(node):
    return nav.descendants(node)


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
    # Use canonical navigation for chapters. Exchange's physical workbench
    # representation may have more than one context for a shared location.
    model_bytes, _ = c.project(root)
    from lxml import etree as ET
    model = ET.fromstring(model_bytes)
    catalogue, elements = c.load(root)
    release = c.read_json(root / 'architecture-release.json')
    tree = nav.load(root, elements)
    locations = nav.by_element(tree)
    model_nodes = {node.get('identifier'): node for node in model.findall('.//a:organizations//a:item', c.N)}
    for node in nav.walk(tree):
        identifier = nav.transport_id(node)
        if node['number'] == 'R':
            projected = [model_nodes[identifier + '-' + layer] for layer in ('motivation', 'strategy')]
        else:
            projected = [model_nodes[identifier]] if identifier in model_nodes else []
        c.require(projected, 'Missing Exchange navigation correspondence: ' + node['number'])
        # XPath union records both native-layer index contexts without changing
        # the single canonical R chapter or any RAD/Manifest navigation.
        node['modelLocator'] = ' | '.join(model.getroottree().getpath(item) for item in projected)
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

    def heading(node, depth):
        c.require(depth < len(HEADINGS), 'Continuum hierarchy exceeds declared LaTeX section depth')
        headings.append({'title': node['title'], 'number': node['number'], 'depth': depth,
                         'origin': 'canonical Continuum navigation', 'locator': node['modelLocator'],
                         'canonicalLocator': node['locator'], 'path': node['path'],
                         'kind': node.get('kind', 'section'),
                         **{key: node[key] for key in ('occurrence', 'contextCanonicalId', 'contextRole',
                             'canonicalReference', 'relationshipIds', 'historicalReferences', 'successorReference') if key in node}})
        text = tex(nav.title(node))
        command = HEADINGS[depth + (1 if node['number'].startswith('G.') else 0)]
        return ('\\' + command + '*{' + text + '}\n'
                + r'\phantomsection' + '\n'
                + r'\label{navigation:' + node['number'] + '}' + '\n'
                + r'\addcontentsline{toc}{' + command + '}{' + text + '}\n')

    def entry(cid, node):
        m, body, source = elements[cid]
        placements.append({'canonicalId': cid, 'number': node['number'], 'path': node['path'],
                           'modelLocator': node['modelLocator'], 'source': str(source.relative_to(root)),
                           'sourceSha256': c.sha(source.read_bytes()), 'standing': m['standing'],
                           'unresolvedProperties': m.get('unresolvedProperties', {}),
                           'sourceUnresolved': m.get('sourceUnresolved'),
                           'classificationIssue': node.get('classificationIssue'),
                           'publication': 'Continuum narrative'})
        link = os.path.relpath(source, output)
        index_name = tex(m['displayName']).replace('!', '"!').replace('@', '"@').replace('|', '"|')
        result = [r'\label{' + label(cid) + '}', r'\index{' + index_name + ' (' + node['number'] + ')}',
                  r'{\small ' + tex(m['archimateType'] + (' / ' + m['specialization'] if m.get('specialization') else '') + ' · ' + m['standing']['semantic'] + ' · ' + m['standing']['currentness']) + r'}\par']
        if m['archimateType'] == 'Stakeholder':
            concerns = [(concern, ids[cid]) for concern, ids in nav.stakeholder_associations(elements).items() if cid in ids]
            if concerns:
                result.append(r'{\small Existing associated Motivation material: ' + '; '.join(
                    ref(concern, elements) + ' (' + locations[concern]['number'] + '); canonical associations: '
                    + tex(', '.join(ids)) for concern, ids in concerns) + r'.}\par')
            else:
                result.append(r'No canonical Motivation association is represented. Index placement asserts none.\par')
        if m['standing']['currentness'] in HISTORICAL:
            result.append(r'\href{' + link + r'}{Historical canonical definition and provenance}.\par')
            return '\n'.join(result)
        # Definitions and boundaries remain source prose. Repeated source
        # contributions/history remain in the linked canonical record/Manifest.
        content = body.split('## Architectural content\n', 1)[1].split('\n## ', 1)[0].strip()
        if m['archimateType'] == 'Stakeholder':
            paragraphs = [p for p in content.split('\n\n') if p and not p.startswith('#')]
            content = paragraphs[0] if paragraphs else content
        result.append(prose.render(content, source))
        if m.get('unresolvedProperties') or m.get('sourceUnresolved'):
            result.append(r'{\small Recorded unresolved matters: ' + tex(', '.join(sorted(m.get('unresolvedProperties', {}))) or 'source-qualified matters') + r'; exact scope remains in the canonical record.}\par')
        if node.get('classificationIssue'):
            result.append(r'{\small Navigation classification: ' + tex(node['classificationIssue']) + r'}\par')
        result.append(r'{\footnotesize \href{' + link + r'}{Canonical record: ' + cid + r'}; full qualifications and provenance.}\par')
        return '\n'.join(result)

    def render(node, depth):
        out = [heading(node, depth)]
        if node.get('description'):
            out.append(tex(node['description']) + r'\par')
        for role, reference in nav.references(node):
            cid = reference['canonicalId']
            out.append(r'{\small ' + tex(role + ': ' + reference['number'] + ' ') + ref(cid, elements)
                       + r'; Canonical ID: \texttt{\seqsplit{' + cid + '}}. '
                       + tex(reference['standing']['semantic'] + ' · ' + reference['standing']['currentness']) + r'.}\par')
        if node.get('contextCanonicalId'):
            out.append(r'Contextual reference only; no additional architectural instance or semantic relationship.\par')
        if node.get('relationshipIds'):
            out.append(tex('Existing canonical associations: ' + ', '.join(node['relationshipIds'])) + r'\par')
        if node.get('relatedCanonicalIds'):
            out.append(r'\begin{longtable}{p{.22\linewidth}p{.73\linewidth}}')
            for cid in node['relatedCanonicalIds']:
                out.append(tex(locations[cid]['number']) + ' & ' + ref(cid, elements) + r' \\')
            out.append(r'\end{longtable}')
        if node.get('source'):
            target = os.path.relpath(root / node['source'].split('#')[0], output)
            if '#' in node['source']:
                target += '#' + node['source'].split('#', 1)[1]
            out.append(r'\href{' + target + r'}{Governed source: ' + tex(node['title']) + r'}.\par')
        if node.get('canonicalId'):
            out.append(entry(node['canonicalId'], node))
        for child in node.get('children', []):
            out.append(render(child, depth + 1))
        return '\n'.join(out)

    includes = []
    for part in tree:
        domain = part['title'].casefold()
        headings.append({'title': part['title'], 'number': part['number'], 'depth': -1,
                         'origin': 'canonical Continuum navigation', 'locator': part['modelLocator'],
                         'canonicalLocator': part['locator'], 'path': part['path'], 'kind': 'domain'})
        if part['namespace'] == 'G':
            out = [r'\chapter*{G Architecture Governance}', r'\label{navigation:G}',
                   r'\addcontentsline{toc}{chapter}{G Architecture Governance}',
                   'The existing governance contracts establish authority, decision rights, review and evidence. '
                   'These links expose that repository through the architectural navigation; they create no parallel governance records.']
        else:
            part_title = ('Reference navigation — R ' + part['title'] if part['namespace'] == 'R'
                          else 'Part ' + part['namespace'] + ' — ' + part['title'])
            out = [r'\part*{' + tex(part_title) + '}', r'\label{navigation:' + part['number'] + '}',
                   r'\addcontentsline{toc}{part}{' + tex(part_title) + '}']
        if part.get('description'):
            out.append(tex(part['description']) + r'\par')
        for note in notes.get(domain, []):
            c.require(note['sources'] and all(cid in elements for cid in note['sources']), 'Unresolved reader-note source')
            out.append(tex(note['text']) + '\n\n' + r'{\small Reading sources: ' + '; '.join(ref(cid, elements) for cid in note['sources']) + r'.}\par')
        picture, records = diagram(part, elements, population)
        if records:
            out.append(picture)
            diagrams.append({'domain': domain, 'selection': 'Largest explicit active Composition neighbourhood, at most six targets; stable name/identity ordering', 'edges': records})
        children = part.get('children', [])
        for child in children:
            out.append(render(child, 0))
        name = 'parts/' + domain.replace(' ', '-').replace('&', 'and') + '.tex'
        artifacts[name] = ('\n'.join(out) + '\n').encode()
        includes.append(r'\input{' + name + '}')
    c.require(Counter(item['canonicalId'] for item in placements) == Counter(elements.keys()), 'RAD must account for every canonical element exactly once')
    template = (root / SOURCE / 'templates/publication.tex').read_text()
    master = template.replace('@VERSION@', release['version']).replace('@PREVIOUS@', release['previous_version']).replace('@CONTENT@', '\n'.join(includes))
    c.require('@CONTENT@' not in master, 'Unexpanded publication template')
    artifacts['continuum-rad.tex'] = master.encode()
    artifacts['structure.json'] = c.encoded({'release': release, 'hierarchySource': str(nav.SOURCE),
        'modelSha256': c.sha(model_bytes), 'domainClassificationSource': str(nav.SOURCE),
        'domainPartition': 'Canonical taxonomy: Governance G, Parts I–VI; R is cross-cutting reference navigation outside architectural decomposition. It holds the Stakeholder Index and historical Capability identities. Every node has a derived coordinate. Unpopulated domains record absence without speculative subsections.',
        'headings': headings, 'elements': placements, 'diagrams': diagrams,
        'limits': 'Hierarchy is navigation, not semantic containment or identity. Standing, native typing and relationships are unchanged. No architecture is invented for publication.'})
    artifacts['README.md'] = (f'# Harmonia Architecture Continuum RAD — {release["version"]}\n\n'
        '[Read the PDF](continuum-rad.pdf) · [LaTeX source](continuum-rad.tex) · [Continuum correspondence](structure.json)\n\n'
        'Front matter; Architecture Governance; Parts I–VI; Architectural Name Index. '
        'Chapters and coordinates follow the canonical Continuum navigation. '
        'No Technology or Implementation & Migration subsections are invented where no elements are represented. '
        'Every element retains identity, native type, independent standing and source correspondence. '
        'Publication supplies no architectural acceptance.\n').encode()
    artifacts['publication.json'] = c.encoded({'status': 'PASS', 'release': release, 'canonicalSources': len(elements),
        'narrativeDescriptions': sum(p['standing']['currentness'] not in HISTORICAL for p in placements),
        'historicalReferencesInModelHierarchy': sum(p['standing']['currentness'] in HISTORICAL for p in placements),
        'architectureHeadings': len(headings), 'architectureHeadingOrigins': dict(Counter(h['origin'] for h in headings)),
        'aiAssistance': 'Retained source-cited reader notes and deterministic presentation. Canonical descriptions are rendered without semantic rewriting. Diagrams select existing native assertions with direction/state/standing.',
        'sourceInputs': ['canonical Continuum Markdown', str(nav.SOURCE), str(c.DESIGN / 'projection-template.xml'),
                         str(SOURCE / 'reader-notes.json'), str(SOURCE / 'templates/publication.tex'), 'architecture-release.json'],
        'allocatedIdentities': 0, 'legacyRadIngested': False, 'sha256': {name: c.sha(data) for name, data in sorted(artifacts.items())}})
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
