#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render frozen publication identities against fingerprinted RAD Markdown.

This is a publication tool, not a model reconciler or an identifier allocator.
Requires python3-markdown-it. Never reads the earlier candidate LaTeX files.
"""
import collections
import hashlib
import json
import re
import sys
from pathlib import Path

from markdown_it import MarkdownIt

BASE = Path(__file__).resolve().parents[1]
ROOT = BASE.parents[2]
M = json.loads((BASE / 'manifest.json').read_text())
E = {e['key']: e for e in M['elements']}
S = M['sources']
MD = MarkdownIt('commonmark').enable('table')
TEXT = {k: (ROOT / s['path']).read_text() for k, s in S.items()}
LINES = {k: t.splitlines() for k, t in TEXT.items()}
SOURCE_LINKS = {}
ANCHORS = {}
COVERAGE = []


def load_prose():
    """Read reviewable authored interpretation, never architecture extraction.

    Names are resolved only inside an explicitly bounded catalogue category;
    exact Reference IDs can also be used. Missing/ambiguous entries fail closed.
    """
    result = {}
    groups = {'business.md': 'business', 'business-leaves.md': 'business',
              'enabling.md': 'business_enabling', 'enterprise.md': 'enterprise',
              'features.md': 'feature', 'behaviour.md': 'behaviour'}
    for filename, group in groups.items():
        path = BASE / 'catalogue' / filename
        if not path.exists():
            raise ValueError('Missing authored catalogue: ' + filename)
        current, field = None, None
        for line in path.read_text().splitlines():
            if line.startswith('## '):
                name = line[3:]
                matches = [e for e in E.values() if
                           (e['category'] == group or e['type'] == group or
                            (group == 'behaviour' and e['type'] in ('function', 'service', 'process'))) and
                           name in (e['name'], e['reference_id'])]
                assert len(matches) == 1, ('Ambiguous authored identity', filename, name)
                current = matches[0]['key']
                assert current not in result, ('Duplicate authored identity', name)
                result[current] = {'file': 'catalogue/' + filename, 'sections': {}}
                field = None
            elif line.startswith('### '):
                field = line[4:]
                assert current and field not in result[current]['sections'], ('Prose section', line)
                result[current]['sections'][field] = ''
            elif current and field:
                result[current]['sections'][field] += line + '\n'
        for item in result.values():
            item['sections'] = {k: v.strip() for k, v in item['sections'].items()}
    return result


PROSE = load_prose()


def typed_title(e):
    if e['type'] == 'capability':
        return (e['tier'] or ('BEC' if e['category'] == 'business_enabling' else 'Candidate BC')) + ': ' + e['name']
    return {'enterprise_capability': 'Enterprise Capability', 'feature': 'Feature',
            'function': 'Function', 'service': 'Service', 'process': 'Process'}[e['type']] + ': ' + e['name']


def ancestry(e):
    chain, current = [], e
    while current.get('parent_key') or current.get('owner_key'):
        current = E[current.get('parent_key') or current['owner_key']]
        assert current not in chain, 'Cyclic ancestry'
        chain.insert(0, current)
    return chain


def esc(s):
    return ''.join({'\\': r'\textbackslash{}', '&': r'\&', '%': r'\%', '$': r'\$',
                    '#': r'\#', '_': r'\_', '{': r'\{', '}': r'\}', '~': r'\textasciitilde{}',
                    '^': r'\textasciicircum{}'}.get(c, c) for c in str(s))


def slug(s):
    # GitHub-style source-heading anchors, not publication identity generation.
    return re.sub(r'[^\w\- ]', '', s.lower()).replace(' ', '-')


def flow(s):
    """Typeset source equations as wrapping text, preserving words/operators."""
    if '$' not in s:
        return s
    s = s.replace('$$', '').replace('$', '')
    s = re.sub(r'\\text\{([^{}]*)\}', r'\1', s)
    for a, b in [(r'\longleftrightarrow', '↔'), (r'\longrightarrow', '→'),
                 (r'\rightarrow', '→'), (r'\times', '×'), (r'\neq', '≠'),
                 (r'\to', '→'), (r'\qquad', ' '), (r'\quad', ' '),
                 (r'\bf', ''), (r'\textbf', '')]:
        s = s.replace(a, b)
    return s.replace('{', '').replace('}', '')


def ref(e, page=False):
    lab = e['latex_label']
    return r'\nameref{' + lab + '}' + (r' (p.\,\pageref{' + lab + '})' if page else '')


def sourceref(src, line):
    label = SOURCE_LINKS.get((src, line), 'rad:' + src)
    return r'\hyperref[' + label + ']{' + esc(src) + ':' + str(line) + r'} (p.\,\pageref{' + label + '})'


def inline(tokens, src):
    out = []
    for t in tokens or []:
        if t.type == 'text':
            out.append(esc(flow(t.content)).replace('/', r'/\allowbreak{}'))
        elif t.type in ('softbreak', 'hardbreak'):
            out.append(' ')
        elif t.type == 'code_inline':
            # Sans text with URL-style discretionary breaks is readable for long IDs.
            out.append(r'\texttt{\seqsplit{' + esc(t.content) + '}}')
        elif t.type in ('strong_open', 'em_open'):
            out.append(r'\textbf{' if t.type == 'strong_open' else r'\emph{')
        elif t.type in ('strong_close', 'em_close'):
            out.append('}')
        elif t.type == 'link_open':
            target = t.attrGet('href')
            path, _, anchor = target.partition('#')
            if not path:
                key = (src, anchor)
            else:
                resolved = (ROOT / S[src]['path']).parent.joinpath(path).resolve()
                other = next((k for k, s in S.items() if (ROOT / s['path']).resolve() == resolved), None)
                key = (other, anchor)
            label = ANCHORS.get(key)
            if label:
                out.append(r'\hyperref[' + label + ']{')
            else:
                # Links outside the reproduced scope keep their exact RAD target.
                if not re.match(r'https?://', target):
                    resolved = (ROOT / S[src]['path']).parent.joinpath(path).resolve() if path else ROOT / S[src]['path']
                    try:
                        target = 'https://github.com/fhirfactory/harmonia/blob/' + M['baseline_commit'] + '/' + str(resolved.relative_to(ROOT)) + ('#' + anchor if anchor else '')
                    except ValueError:
                        raise ValueError('Reference outside repository: ' + target)
                out.append(r'\href{' + esc(target) + '}{')
        elif t.type == 'link_close':
            out.append('}')
        elif t.type == 'html_inline':
            if t.content in ('<br>', '<br/>', '<br />'):
                out.append(' / ')
            elif not t.content.startswith(('<a ', '</a>')):
                raise ValueError('Unhandled inline HTML: ' + t.content)
        elif t.type == 'image':
            out.append(esc(t.content))
        else:
            raise ValueError('Unhandled inline token: ' + t.type)
    return ''.join(out)


def table(headers, rows, widths=None, title=None):
    n = len(headers)
    if widths is None:
        widths = {2: [0.35, 0.65], 3: [0.24, 0.33, 0.43]}.get(n, [1 / n] * n)
    spec = '|' + '|'.join(r'>{\raggedright\arraybackslash}p{' + str(round(w, 4)) + r'\TableWidth}' for w in widths) + '|'
    title_row = (r'\hline\rowcolor{TableHead}\multicolumn{' + str(n) + r'}{|c|}{\textbf{' + esc(title) + r'}}\\') if title else ''
    out = [r'\begingroup\small\setlength{\TableWidth}{\dimexpr\linewidth-' + str(2 * n) + r'\tabcolsep-' + str(n + 1) + r'\arrayrulewidth\relax}',
           r'\begin{longtable}{' + spec + '}', title_row + r'\hline\rowcolor{TableHead}' + ' & '.join(r'\textbf{' + h + '}' for h in headers) + r'\\\hline\endfirsthead',
           r'\hline\rowcolor{TableHead}' + ' & '.join(r'\textbf{' + h + '}' for h in headers) + r'\\\hline\endhead',
           r'\hline\endfoot']
    out += [' & '.join(row) + r'\\\hline' for row in rows]
    out += [r'\end{longtable}\endgroup']
    return '\n'.join(out) + '\n'


def blocks(tokens, src):
    out, i, list_depth = [], 0, 0
    while i < len(tokens):
        t = tokens[i]
        if t.type == 'table_open':
            i += 1
            rows, row = [], []
            while tokens[i].type != 'table_close':
                z = tokens[i]
                if z.type == 'tr_open':
                    row = []
                elif z.type == 'inline':
                    row.append(inline(z.children, src))
                elif z.type == 'tr_close':
                    rows.append(row)
                i += 1
            out.append(table(rows[0], rows[1:]))
        elif t.type == 'heading_open':
            level = int(t.tag[1:])
            cmd = {1: 'section', 2: 'subsection'}.get(level, 'subsubsection')
            out.append('\\' + cmd + '*{' + inline(tokens[i + 1].children, src) + '}\n')
            i += 2
        elif t.type == 'paragraph_open':
            out.append('\n')
        elif t.type == 'paragraph_close':
            out.append('\n\n')
        elif t.type == 'inline':
            out.append(inline(t.children, src))
        elif t.type in ('bullet_list_open', 'ordered_list_open'):
            # Preserve source nesting without ever implying capability ancestry.
            list_depth += 1
            out.append('\n\\begin{' + ('itemize' if t.type == 'bullet_list_open' else 'enumerate') + '}\n')
        elif t.type in ('bullet_list_close', 'ordered_list_close'):
            out.append('\n\\end{' + ('itemize' if t.type == 'bullet_list_close' else 'enumerate') + '}\n')
            list_depth -= 1
        elif t.type == 'list_item_open':
            out.append('\n\\item ')
        elif t.type == 'list_item_close':
            out.append('\n')
        elif t.type == 'blockquote_open':
            out.append('\n\\begin{quote}\n')
        elif t.type == 'blockquote_close':
            out.append('\n\\end{quote}\n')
        elif t.type in ('fence', 'code_block'):
            content = t.content.strip()
            if t.info == 'mermaid':
                out.append('\n\\textit{RAD diagram: textual source notation.}\n\\begin{Verbatim}[breaklines,fontsize=\\footnotesize]\n' + content + '\n\\end{Verbatim}\n')
            else:
                # No containment pictographs. A linear source transcription is
                # explicitly not a publication hierarchy or an inferred edge set.
                out.append('\n\\textit{RAD source listing (presentation only; no inferred ancestry).}\n')
                clean = []
                for line in content.splitlines():
                    line = re.sub('[┌┐└┘├┤┬┴┼─│┆┊]', ' ', line).strip()
                    if line:
                        clean.append(esc(line))
                out.append('\\begin{quote}\n' + '\n\n'.join(clean) + '\n\\end{quote}\n')
        elif t.type == 'html_block':
            if not t.content.strip().startswith(('<!--', '<a ')):
                raise ValueError('Unhandled block HTML: ' + t.content[:100])
        elif t.type == 'hr':
            out.append('\n\\par\\smallskip\n')
        else:
            raise ValueError('Unhandled block token: ' + t.type)
        i += 1
    return ''.join(out)


def markdown(text, src):
    return blocks(MD.parse(text), src)


def source_sections():
    """Register source block anchors first so all internal links can resolve."""
    for src, s in S.items():
        for start, end in s['ranges']:
            chunk = '\n'.join(LINES[src][start - 1:end])
            tokens = MD.parse(chunk)
            for t in tokens:
                if not t.map:
                    continue
                a, b = t.map
                label = 'rad:' + src + ':' + str(start + a)
                # Label only top-level blocks; avoid anchors inside table/list cells.
                if t.level == 0:
                    for ln in range(start + a, start + b):
                        SOURCE_LINKS[(src, ln)] = label
                if t.type == 'heading_open':
                    title = LINES[src][start + a - 1].lstrip('# ')
                    ANCHORS.setdefault((src, slug(title)), label)
                if t.type == 'html_block':
                    for anchor in re.findall(r'<a id="([^"]+)"', t.content):
                        ANCHORS[(src, anchor)] = label
        ANCHORS[(src, '')] = 'rad:' + src
    # Nested heading anchors use their enclosing top-level block's real label.
    for src, s in S.items():
        for key, value in list(ANCHORS.items()):
            if key[0] == src and value.startswith('rad:' + src + ':'):
                line = int(value.rsplit(':', 1)[1])
                ANCHORS[key] = SOURCE_LINKS.get((src, line), 'rad:' + src)


def projection(src):
    s = S[src]
    out = ['\\chapter{' + esc(s['title']) + '}\\label{rad:' + src + '}\n',
           '\\noindent\\textbf{RAD source:} \\texttt{\\seqsplit{' + esc(s['path']) + '}}\n\n',
           'The following is a presentation transcription of the governed source. '
           'Source catalogue layout and retained illustrations do not allocate tiers or establish additional identities. '
           'Equations wrap as text; diagram notation is textual.\n\n']
    if src == 'assurance-strategy':
        out.append('\\textbf{Source qualification:} The opening 2026-10-14 reconciliation qualifies the older unresolved BC-18 tier/relevance statements later in this source. Later approved Domain03 assurance behaviour is reproduced separately; earlier Strategy stop boundaries retain their own scope.\n\n')
    for start, end in s['ranges']:
        chunk = '\n'.join(LINES[src][start - 1:end])
        tokens = MD.parse(chunk)
        # Render one complete top-level block at a time, with stable source locator.
        i = 0
        while i < len(tokens):
            t = tokens[i]
            j = i + 1
            if t.nesting == 1:
                depth = 1
                while j < len(tokens) and depth:
                    depth += tokens[j].nesting
                    j += 1
            if t.map:
                a, b = t.map
                lab = SOURCE_LINKS.get((src, start + a))
                if lab:
                    out.append('\\phantomsection\\label{' + lab + '}\n')
                COVERAGE.append({'source': src, 'start_line': start + a, 'end_line': start + b - 1,
                                 'kind': t.type, 'sha256': hashlib.sha256('\n'.join(LINES[src][start + a - 1:start + b - 1]).encode()).hexdigest()})
            out.append(blocks(tokens[i:j], src))
            i = j
    return ''.join(out)


def metadata(e):
    rows = [('Reference Id', r'\texttt{\seqsplit{' + esc(e['reference_id']) + '}}', 'Stable publication identity.'),
            ('Architectural Status', e['architectural_status'], 'Established by RAD.' if e['architectural_status'] == 'Canonical' else 'Not approved hierarchy; retain candidate standing.')]
    if e['type'] == 'capability':
        rows.append(('Capability Tier', e['tier'] or 'Unresolved', 'Explicit source assertion.' if e['tier'] else 'No tier inferred from presentation.'))
    elif e['type'] == 'feature':
        rows.append(('Type', 'FT / Feature', 'Not a Capability Tier.'))
    if e.get('parent_key') or e.get('owner_key'):
        chain = ancestry(e)
        rows.append(('Ancestry' if e['type'] == 'capability' else 'Owning Context',
                     r' $\rightarrow$ '.join(ref(x, True) for x in chain),
                     'Established BC responsibility path.' if e['category'] == 'business' else
                     'Established responsibility only; complete CT ancestry Unresolved.'))
    elif e['tier'] == 'CT1':
        rows.append(('Ancestry', 'CT1 root', 'Explicitly established.'))
    elif e['ancestry_status'] == 'unresolved':
        rows.append(('Ancestry', 'Unresolved', 'No root/parent inferred.'))
    rows.append(('Structural Canonical ID', r'\texttt{\seqsplit{' + esc(e['canonical_id']) + '}}' if e['canonical_id'] else 'Unresolved',
                 ('Existing governed EC identifier; no CT path applies.' if e['type'] == 'enterprise_capability' else
                  'Allocated v0.1; structural responsibility identity.') if e['canonical_id'] else
                 ('Candidate placement/identity not approved.' if e['architectural_status'] == 'Candidate' else
                  'Required BEC tier ancestry is not established.')))
    if e['type'] == 'capability':
        rows.append(('Harmonia Relevance', esc(e.get('relevance') or 'Unresolved'), 'Independent of status; never inherited.'))
    # Use all evidence blocks, deduplicated by link target; actual citations remain in manifest.
    cited, seen = [], set()
    for s in e['sources']:
        target = SOURCE_LINKS.get((s['source'], s['line']), 'rad:' + s['source'])
        if target not in seen:
            cited.append(sourceref(s['source'], s['line']))
            seen.add(target)
    rows.append(('Source', '; '.join(cited), 'Linked RAD detail; exact locators in manifest.'))
    if e.get('name_aliases'):
        rows.append(('Name Aliases', esc('; '.join(e['name_aliases'])), 'Same element only.'))
    # Enablement is independent of containment and is not inherited by children.
    enabled = [E[r['to']] for r in M['relationships'] if r['type'] == 'enabled_by' and r['status'] == 'established' and r['from'] == e['key']]
    supports = [E[r['from']] for r in M['relationships'] if r['type'] == 'enabled_by' and r['status'] == 'established' and r['to'] == e['key']]
    if enabled:
        rows.append(('Enabled By', '; '.join(ref(x) for x in enabled), 'Bounded support; not decomposition or runtime dependency.'))
    if supports:
        rows.append(('Supports', '; '.join(ref(x) for x in supports), 'Source-scoped; no inherited descendant edges.'))
    for typ, heading, comment in [
        ('realises_feature', 'Derived From', 'Established Feature realisation; not structural containment.'),
        ('associated_feature', 'Associated Feature', 'Established association only.'),
        ('governed_process', 'Governed Process', 'Established progression relationship.'),
        ('participating_function', 'Participating Functions', 'Participation retains each Function owner.'),
        ('uses_process', 'Uses Process', 'Established Service-to-Process relationship.'),
        ('exposes_process', 'Exposes Process', 'Exposure is distinct from Process ownership.')]:
        targets = [E[r['to']] for r in M['relationships'] if r['type'] == typ and
                   r['status'] == 'established' and r['from'] == e['key']]
        if targets:
            rows.append((heading, '; '.join(ref(x, True) for x in targets), comment))
    for unresolved, heading in [('feature_association', 'Feature Association'),
                                ('function_mapping', 'Internal Function Mapping'),
                                ('exposure_consumer', 'Exposure / Consumer')]:
        if unresolved in e['unresolved']:
            rows.append((heading, 'Unresolved', M['unresolved_meanings'][unresolved]))
    out = []
    title = 'Capability Metadata' if e['type'] in ('capability', 'enterprise_capability') else 'Element Metadata'
    out.append(table(['Name', 'Value', 'Comment / Description'], [[esc(a), b, esc(c)] for a, b, c in rows], [0.20, 0.47, 0.33], title))
    return ''.join(out)


def description(e):
    """Only extract exact wording from cited Markdown; no candidate summaries."""
    candidates = []
    for s in e['sources']:
        src, ln = s['source'], s['line'] - 1
        line = LINES[src][ln]
        if e['tier'] == 'CT3' and line.startswith('| '):
            cells = line.strip('| ').split('|')
            if len(cells) == 2:
                return markdown(cells[1].strip(), src)
        if e['type'] in ('feature', 'function', 'service'):
            match = re.search(r'\*\*' + re.escape(e['name']) + r'\*\*:\s*(.*)', line)
            if not match:
                match = re.search('`' + re.escape(e['name']) + r'`\s*—\s*(.*)', line)
            if match:
                return markdown(match.group(1), src)
        if line.startswith('#'):
            end = next((j for j in range(ln + 1, len(LINES[src])) if LINES[src][j].startswith('#')), len(LINES[src]))
            block = LINES[src][ln + 1:end]
            # Definition/Purpose/Scope is exact and may span a paragraph.
            for j, p in enumerate(block):
                if re.match(r'^(?:- )?\*\*(?:Definition|Process Purpose|Architectural Scope)\*\*:', p) or p.startswith('**Definition:**'):
                    para = [p]
                    for q in block[j + 1:]:
                        if not q.strip() or q.startswith(('- ', '**')):
                            break
                        para.append(q)
                    para[0] = re.sub(r'^(?:- )?\*\*Definition:?\*\*:?\s*', '', para[0])
                    candidates.append(markdown('\n'.join(para), src))
                    break
    return candidates[0] if candidates else ''


def source_excerpt(e):
    """Exact local semantic description; also retain local boundary qualifiers.

    Source-detail transcription remains unchanged. In particular this routine
    does not infer Function/Service pairs from neighbouring source bullets.
    """
    if e['type'] in ('process', 'enterprise_capability'):
        src = 'processes' if e['type'] == 'process' else 'enterprise'
        evidence = next(s for s in e['sources'] if s['source'] == src)
        a = evidence['line']
        b = next((j for j in range(a, len(LINES[src])) if re.match(r'^#{1,3} ', LINES[src][j])), len(LINES[src]))
        # Include the whole governed entry: progression, qualifications, etc.
        return markdown('\n'.join(LINES[src][a:b]), src)
    if e['type'] in ('function', 'service'):
        src = next(s['source'] for s in e['sources'] if s['source'] in ('em', 'sa', 'sd', 'hso', 'ise', 'assurance'))
        s = next(s for s in e['sources'] if s['source'] == src)
        a = s['line'] - 1
        line = LINES[src][a]
        match = re.search(r'`' + re.escape(e['name']) + r'`\s*—\s*(.*)', line)
        if match:
            out = markdown(match.group(1), src)
            # Preserve qualifications within this actual source behaviour group.
            start = max([j for j in range(a + 1) if re.match(r'^(?:### |  - \*\*)', LINES[src][j])] or [a])
            end = next((j for j in range(a + 1, len(LINES[src])) if re.match(r'^(?:### |  - \*\*|- \*\*Information Responsibility)', LINES[src][j])), len(LINES[src]))
            qualifiers = [l.strip().removeprefix('- ') for l in LINES[src][start:end]
                          if re.match(r'^\s*- \*(?:\*|)(?:Responsibility boundary|Authority boundary|Exposure qualification|Capacity boundary|Derivation|Additional Feature responsibility|Feature association not established)', l)]
            if qualifiers:
                out += markdown('\n\n'.join(qualifiers), src)
            return out
        # Assurance elements have independent named sections with richer prose.
        start = max([j for j in range(a + 1) if LINES[src][j].startswith(('### ', '#### '))])
        end = next((j for j in range(start + 1, len(LINES[src])) if LINES[src][j].startswith(('## ', '### ', '#### '))), len(LINES[src]))
        return markdown('\n'.join(LINES[src][start + 1:end]), src)
    if e['type'] == 'capability' and e['category'] == 'business_enabling':
        for s in e['sources']:
            if s['source'] != 'enabling':
                continue
            a = s['line']
            if not LINES['enabling'][a - 1].startswith(('#', '- **')):
                continue
            end = next((j for j in range(a, len(LINES['enabling'])) if
                        LINES['enabling'][j].startswith(('#', '- **'))), len(LINES['enabling']))
            chunk = LINES['enabling'][a:end]
            definitions = [re.sub(r'^\*{1,2}Definition:?\*{1,2}:?\s*', '', l.strip().removeprefix('- '))
                           for l in chunk if '*Definition*' in l or '**Definition' in l]
            if definitions:
                return markdown('\n'.join(definitions), 'enabling')
    return description(e)


def explanation(e, overview_only=False):
    item = PROSE.get(e['key'])
    if not item:
        return ''  # Concise behaviour description is supplied from its actual source.
    out = []
    for heading, prose in item['sections'].items():
        if heading.endswith('Overview') != overview_only:
            continue
        out.append('\\subsubsection*{' + esc(heading) + '}\n' + markdown(prose, 'metamodel'))
    return ''.join(out)


def notes(e):
    # These dimensions already have explicit values and explanations in metadata.
    # Repeating the same structural warning beneath 512 entries obscures the
    # particular semantic questions that require architectural review.
    metadata_dimensions = {'canonical_id', 'code_allocation', 'relevance', 'tier',
                           'ancestry', 'root_status', 'feature_association',
                           'function_mapping', 'exposure_consumer'}
    unresolved = [u for u in e['unresolved'] if u not in metadata_dimensions]
    if not unresolved:
        return ''
    out = ['\\subsubsection*{Additional Notes / Unresolved Matters}\n']
    for u in unresolved:
        text = M['unresolved_meanings'][u]
        out.append('\\textbf{' + esc(u.replace('_', ' ').title()) + ':} ' + esc(text) + '\n\n')
    return ''.join(out)


def children_table(e):
    children = [c for c in E.values() if c.get('parent_key') == e['key']]
    if not children:
        return ''
    title = 'Contained Capabilities' if e['category'] == 'business' else 'Established Partial Responsibility Relationship'
    return '\\subsubsection*{Scope}\n\\paragraph*{' + title + '}\n' + table(['Capability', 'Reference Id', 'Tier'],
        [[ref(c, True), r'\texttt{\seqsplit{' + esc(c['reference_id']) + '}}', c['tier'] or 'Unresolved'] for c in children], [0.49, 0.38, 0.13])


def entry(e, level):
    cmd = ['section', 'subsection', 'subsubsection', 'paragraph'][min(level, 3)]
    lab = e['latex_label']
    title = typed_title(e)
    index_name = esc(e['name'].replace('!', ' ').replace('@', ' '))
    if e['name'] == 'Health Information Exchange':
        index_name += '!Business Capability' if e['category'] == 'business' else '!Business Enabling Capability'
    out = ['\\Needspace{10\\baselineskip}\n\\' + cmd + '{' + esc(title) + '}\\label{' + lab + '}\n',
           '\\index{' + index_name + '}\n', metadata(e), explanation(e, overview_only=True)]
    exact = source_excerpt(e)
    if exact:
        heading = 'Definition' if e['type'] in ('capability', 'feature') else 'Governed Description and Qualifications'
        out.append('\\subsubsection*{' + heading + '}\n' + exact)
    out.append(explanation(e))
    out.append(children_table(e))
    if e['type'] == 'feature':
        related = [(E[r['from']], r) for r in M['relationships'] if r['to'] == e['key'] and
                   r['status'] == 'established' and r['type'] in ('realises_feature', 'associated_feature', 'contributes_feature')]
        if related:
            out.append('\\paragraph*{Established Behavioural Context}\n' + table(
                ['Element', 'Relationship to this Feature'],
                [[ref(c, True), esc(r['type'].replace('_', ' '))] for c, r in related]))
    out.append(notes(e))
    return ''.join(out)


def business():
    out = ['\\chapter{Business Capabilities}\\label{model:bc}\n',
           'The hierarchy below contains only explicit governed BC tier and parent assertions. '
           'Full definitions, decomposition assertions, boundaries, illustrative activities, care contexts, exclusions and stopping points are reproduced in \\hyperref[rad:business]{the BC source detail}. '
           'Relevance is not inherited. Regions are contextual groupings.\n\n']
    def walk(e, depth):
        out.append(entry(e, depth))
        for c in E.values():
            if c.get('parent_key') == e['key']:
                walk(c, depth + 1)
    for e in E.values():
        if e['category'] == 'business' and e['tier'] == 'CT1':
            walk(e, 0)
    out.append('\\section{Candidate concerns with unresolved placement}\nThese three named concerns remain candidates; no tier or parent is allocated. Other provisional proposals remain in the linked RAD prose and are not promoted to independently established elements.\n')
    for e in E.values():
        if e['category'] == 'business' and e['architectural_status'] == 'Candidate':
            out.append(entry(e, 1))
    return ''.join(out)


def enabling():
    out = ['\\chapter{Business Enabling Capabilities and owned behaviour}\\label{model:bec}\n',
           'All 76 established BEC contexts have unresolved CT tiers and complete ancestry. '
           'This chapter groups them by documented contextual view and responsibility; its heading depth is navigation, not a CT assertion. '
           'Features, Functions, Services and Processes below retain their actual owning context. '
           'Function/Service/Process and Feature associations form a graph, recorded in \\hyperref[model:relationships]{the relationship register}. '
           'A Feature listing is not a claim of complete CT1 → CT2 → CT3 → FT reconciliation.\n\n']
    caps = [e for e in E.values() if e['category'] == 'business_enabling']
    views = [(v['key'], v['name']) for v in M['views'] if v['key'].startswith('view:')] + [(None, 'Contextual view unestablished')]
    for vkey, name in views:
        group = [e for e in caps if (vkey in e['views'] if vkey else not e['views'])]
        if not group:
            continue
        out.append('\\section{' + esc(name) + '}\n')
        out.append(table(['Capability', 'Reference Id'], [[ref(e, True), r'\texttt{\seqsplit{' + esc(e['reference_id']) + '}}'] for e in group]))
        for e in group:
            out.append(entry(e, 1))
            owned = [c for c in E.values() if c.get('owner_key') == e['key']]
            if owned:
                out.append('\\subsubsection*{Scope}\n\\paragraph*{Contained Features and Established Owned Behaviour}\n')
                out.append(table(['Element', 'Type', 'Reference Id'], [[ref(c, True), esc(c['type'].title()), r'\texttt{\seqsplit{' + esc(c['reference_id']) + '}}'] for c in owned], [0.50, 0.13, 0.37]))
                for c in owned:
                    out.append(entry(c, 2))
    return ''.join(out)


def enterprise():
    out = ['\\chapter{Enterprise Capabilities}\\label{model:ec}\n',
           'These are the fourteen governed reusable architectural capabilities. Functional responsibility bullets are descriptive responsibilities, not additional named Function or Service identities. No CT decomposition or additional Enterprise peers are inferred.\n\n']
    for e in E.values():
        if e['type'] == 'enterprise_capability':
            out.append(entry(e, 0))
    return ''.join(out)


def relationships():
    out = ['\\chapter{Established and unresolved relationship register}\\label{model:relationships}\n',
           'Only source-evidenced assertions are listed. Unresolved associations are explicitly unestablished, not approved edges. '
           'The BC evidence/context table and the approved EC contribution matrix retain their exact semantics in the source detail; they are not coerced into these relationship types.\n\n']
    for typ in dict.fromkeys(r['type'] for r in M['relationships']):
        out.append('\\section{' + esc(typ.replace('_', ' ').title()) + '}\n')
        rels = [r for r in M['relationships'] if r['type'] == typ]
        out.append(table(['From', 'To', 'Status / evidence'], [[ref(E[r['from']], True), ref(E[r['to']], True),
           ('Canonical' if r['status'] == 'established' else '\\textbf{Unresolved; association not established}') + '; ' + '; '.join(sourceref(s['source'], s['line']) for s in r['sources'])] for r in rels], [0.32, 0.32, 0.36]))
    return ''.join(out)


def contexts():
    out = ['\\chapter{Catalogue descriptions with unresolved element identity}\\label{model:contexts}\n',
           'Of 73 named enabling decomposition descriptions, four independently identify established BEC contexts. '
           'The other 69 do not establish additional Capabilities or tiers. Their names and source wording remain in the full BEC transcription. '
           'The context keys below are navigation locators, not allocated architectural identities, ancestors or aliases.\n\n']
    out.append(table(['Source description', 'Identity standing', 'Source / established context'], [[esc(c['name']),
        'Canonical Capability identity' if c['element'] else 'Unresolved standalone element identity',
        sourceref(c['sources'][0]['source'], c['sources'][0]['line']) + (r'; ' + ref(E[c['element']], True) if c['element'] else '')] for c in M['catalogue_contexts']], [0.38, 0.27, 0.35]))
    return ''.join(out)


def identifier_register():
    out = ['\\chapter{Identifier register}\\label{model:identifiers}\n',
           'Reference IDs and labels are frozen in manifest.json. The register is sorted by Reference Id and includes candidates with their actual status.\n\n']
    out.append(table(['Reference Id / Structural Canonical ID', 'Element / page', 'Type / status'],
                     [[r'\texttt{\seqsplit{' + esc(e['reference_id']) + '}}' + '\n\n' +
                       (r'\texttt{\seqsplit{' + esc(e['canonical_id']) + '}}' if e['canonical_id'] else 'Structural ID: Unresolved'),
                       ref(e, True), esc(e['type'].replace('_', ' ')) + ' / ' + e['architectural_status']]
                      for e in sorted(E.values(), key=lambda e: e['reference_id'])], [0.40, 0.38, 0.22]))
    return ''.join(out)


def review_exceptions():
    def record(e, reason):
        return {'reference_id': e['reference_id'], 'latex_label': e['latex_label'], 'name': e['name'],
                'architectural_status': e['architectural_status'], 'reason': reason}
    structural = [record(e, 'Candidate placement is unapproved.' if e['architectural_status'] == 'Candidate' else
                         'Required BEC tier allocation and complete responsibility ancestry are unresolved under K9.')
                  for e in E.values() if not e['canonical_id']]
    limited = [record(e, 'Only the working name/spelling is established; the overview explicitly explains the missing semantic scope.')
               for e in E.values() if 'sharing_programme_placement' in e['unresolved']]
    material = []
    dimensions = {'semantic_boundary_conflict', 'name_uniqueness', 'medication_compounding',
                  'identified_readiness_placement', 'maintenance_membership', 'sharing_programme_placement',
                  'intervention_taxonomy', 'strategy_catalogue_correspondence', 'feature_association',
                  'function_mapping', 'exposure_consumer', 'enablement'}
    for e in E.values():
        reasons = [M['unresolved_meanings'][u] for u in e['unresolved'] if u in dimensions]
        if e['name'] == 'Encounter Lifecycle Process':
            reasons.append('Strategy ON_LEAVE applicability and transitions are unresolved.')
        if e['name'] == 'Synthetic Task Progression Process':
            reasons.append('Stalled/failed outcome position and transitions are unresolved.')
        if e['name'] in ('Clinical Determination', 'Individual Care Planning'):
            reasons.append('No CT3 set is adjudicated; examples are not promoted to children.')
        if reasons:
            material.append(record(e, ' '.join(reasons)))
    report = {'version': M['version'], 'structural_id_unallocated': structural,
              'capability_overview_absent': [], 'capability_overview_materially_constrained': limited,
              'description_materially_constrained': material,
              'structural_constraint_summary': 'All 76 BECs, 137 Features, 147 Functions, 133 Services and 19 Processes lack complete BEC tier ancestry; three Candidate concerns lack approved placement. No structural path is fabricated.',
              'interpretation_boundary': 'Authored explanations do not adjudicate the preserved issues. All lists use frozen Reference IDs.'}
    emit('review-exceptions.json', json.dumps(report, indent=2) + '\n')
    out = ['\\chapter{Description limits and identifier exceptions}\\label{model:exceptions}\n',
           'All 283 Capability records, including three Candidates, have a Capability Overview. '
           'The named sharing-programme concern has insufficient established scope for a full architectural account; its entry explains that limit. '
           'The machine-readable \\texttt{review-exceptions.json} lists every affected element by its stable Reference Id, name and label.\n\n',
           'Structural IDs are allocated to 190 canonical Business Capabilities and the fourteen existing Enterprise Capability IDs are retained. '
           'The 512 canonical BEC/Feature/behaviour records require unresolved BEC ancestry; three Candidate concerns also lack approved placement. '
           'Their publication identities remain usable. No absent tier is omitted or invented to obtain a structural path.\n\n',
           '\\section{Material semantic constraints}\n',
           table(['Element', 'Constraint preserved for review'], [[ref(E[next(k for k, e in E.items() if e['reference_id'] == r['reference_id'])], True), esc(r['reason'])] for r in material], [0.38, 0.62])]
    return ''.join(out)


def validate():
    for src, s in S.items():
        assert hashlib.sha256((ROOT / s['path']).read_bytes()).hexdigest() == s['sha256'], 'RAD drift: ' + src
    for prop in ('reference_id', 'latex_label', 'key'):
        values = [e[prop] for e in E.values()]
        assert len(values) == len(set(values)), 'Duplicate ' + prop
    for record in list(E.values()) + M['relationships'] + M['catalogue_contexts']:
        for s in record['sources']:
            assert LINES[s['source']][s['line'] - 1] == s['text'], ('Citation drift', s)
    frozen = json.loads((BASE / 'identity-baseline.json').read_text())
    assert M['version'] == frozen['version'] == '0.1'
    assert set(E) == {x['key'] for x in frozen['elements']}, 'Element inventory changed'
    for x in frozen['elements']:
        assert all(E[x['key']].get(k) == v for k, v in x.items()), ('Frozen identity/architecture drift', x['key'])
    for k in ('sources', 'relationships', 'catalogue_contexts'):
        assert hashlib.sha256(json.dumps(M[k], sort_keys=True).encode()).hexdigest() == frozen[k + '_sha256'], 'Frozen architecture drift: ' + k
    ids = [e['canonical_id'] for e in E.values() if e['canonical_id']]
    aliases = [(a, e['key']) for e in E.values() for a in e.get('id_aliases', [])]
    assert len(ids) == len(set(ids)) == 204, 'Structural ID uniqueness/allocation'
    assert len({a for a, _ in aliases}) == len(aliases), 'Duplicate ID alias'
    assert not set(ids).intersection(a for a, _ in aliases), 'Canonical/alias collision'
    for filename, fingerprint in M['explanatory_prose']['files'].items():
        assert hashlib.sha256((BASE / filename).read_bytes()).hexdigest() == fingerprint, 'Authored prose drift: ' + filename
    for e in E.values():
        for prop in ('parent_key', 'owner_key'):
            if e.get(prop):
                assert e[prop] in E
        if e['category'] == 'business' and e['architectural_status'] == 'Canonical':
            code = e['reference_id'].removeprefix('HCM-BC-').upper()
            assert e['local_id'] == code and re.fullmatch(r'[A-Z0-9]+(?:-[A-Z0-9]+)*', code)
            token = e['tier'] + '-' + code
            expected_id = (E[e['parent_key']]['canonical_id'] + '.' if e['parent_key'] else '') + token
            assert e['canonical_id'] == expected_id, ('Structural path', e['name'])
            for ancestor in ancestry(e):
                assert e['canonical_id'].startswith(ancestor['canonical_id'] + '.'), 'Ancestor prefix'
        elif e['type'] == 'enterprise_capability':
            assert e['canonical_id'] == e['local_id'] == e['reference_id']
        else:
            assert e['canonical_id'] is None, 'Speculative structural path'
        item = PROSE.get(e['key'], {})
        overview = item.get('sections', {}).get('Capability Overview')
        if e['type'] in ('capability', 'enterprise_capability'):
            assert overview and len(overview.split()) >= 35, ('Missing/substantive Capability Overview', e['name'])
        if e['type'] == 'feature':
            assert len(item.get('sections', {}).get('Feature Overview', '').split()) >= 25, ('Feature Overview', e['name'])
        if e['type'] in ('function', 'service', 'process'):
            assert source_excerpt(e), ('Missing meaningful behaviour description', e['name'])
        if e['tier'] in ('CT2', 'CT3'):
            p = E[e['parent_key']]
            assert p['tier'] == {'CT2': 'CT1', 'CT3': 'CT2'}[e['tier']]
            assert p['category'] == e['category'] == 'business'
            assert any(r['type'] == 'capability_decomposition' and r['from'] == p['key'] and r['to'] == e['key'] for r in M['relationships'])
        if e['category'] == 'business_enabling':
            assert e['tier'] is None
    for r in M['relationships']:
        assert r['from'] in E and r['to'] in E

    # Independent source inventories: not counts copied from a candidate capture.
    bc = TEXT['business']
    ct1 = re.findall(r'^#### \d{2}\. (.+)$', bc, re.M)
    ct2 = re.findall(r'^##### (?:\d{2}\.\d{2} |CT2 — )(.+)$', bc, re.M)
    ct3, in_ct3 = [], False
    for line in bc.splitlines():
        if line.startswith('| CT3 Business Capability |'):
            in_ct3 = True
        elif in_ct3 and line.startswith('|') and not re.match(r'^\|\s*:?-', line):
            ct3.append(line.split('|')[1].strip())
        elif in_ct3 and not line.startswith('|'):
            in_ct3 = False
    for tier, names in [('CT1', ct1), ('CT2', ct2), ('CT3', ct3)]:
        assert set(names) == {e['name'] for e in E.values() if e['category'] == 'business' and e['tier'] == tier}, ('BC inventory', tier)
    parents, current = {}, None
    for line in LINES['business']:
        if line.startswith('##### '):
            current = re.sub(r'^##### (?:\d+\.\d+ |CT2 — )', '', line)
        if line.startswith('**Tier and ancestry:** CT2; parent '):
            parents[current] = line.split('; parent ', 1)[1].removesuffix(' (CT1).')
        if line.startswith('| ') and current and line.count('|') == 3 and not line.startswith(('| CT3', '| :')):
            name = line.split('|')[1].strip()
            if name in ct3:
                parents[name] = current
    for e in E.values():
        if e['tier'] in ('CT2', 'CT3'):
            assert parents[e['name']] == E[e['parent_key']]['name'], ('BC parent assertion', e['name'])
    feature_tokens = set(re.findall(r'`(FEAT-[A-Z]+-\d+)`:', TEXT['enabling']))
    assert feature_tokens == {e['local_id'] for e in E.values() if e['type'] == 'feature'}
    expected = {'function': set(), 'service': set(), 'process': set()}
    for src in ['em', 'sa', 'sd', 'hso', 'ise']:
        expected['function'].update(re.findall(r'\*Function\*:\s*`([^`]+)`', TEXT[src]))
        expected['service'].update(re.findall(r'\*Exposed Service\*:\s*`([^`]+)`', TEXT[src]))
    for typ, a, b in [('function', '## 3.', '## 4.'), ('service', '## 5.', '## 6.')]:
        chunk = TEXT['assurance'].split(a, 1)[1].split(b, 1)[0]
        expected[typ].update(re.findall(r'\*\*Canonical Name\*\*:\s*`([^`]+)`', chunk))
    expected['process'].update(re.findall(r'^### (?:\d+\.\d+ )?(.+ Process)\s*$', TEXT['processes'], re.M))
    # Three assurance processes have unnumbered names beginning "Assurance Process".
    expected['process'].update(re.findall(r'^### \d+\.\d+ (Assurance Process .+)$', TEXT['processes'], re.M))
    for typ, names in expected.items():
        actual = {e['name'] for e in E.values() if e['type'] == typ}
        assert names == actual, (typ, 'missing', names - actual, 'extra', actual - names)
    # Every Function, Service and Process is checked against its actual Markdown
    # owner declaration, not a catalogue position or a candidate-capture parent.
    for e in E.values():
        if e['type'] not in ('function', 'service', 'process'):
            continue
        evidence = next(s for s in e['sources'] if s['source'] in ('em', 'sa', 'sd', 'hso', 'ise', 'assurance', 'processes'))
        lines, ln = LINES[evidence['source']], evidence['line'] - 1
        declarations = [l for l in lines[:ln + 1] if '**Owning Capability**:' in l]
        if evidence['source'] == 'processes':
            end = next((j for j in range(ln + 1, len(lines)) if lines[j].startswith('### ')), len(lines))
            declarations = [l for l in lines[ln:end] if '**Owning Capability**:' in l] or declarations
        match = re.search(r'\*\*Owning Capability\*\*:\s*(?:`([^`]+)`|\[([^\]]+)\])', declarations[-1])
        assert match and (match.group(1) or match.group(2)) == E[e['owner_key']]['name'], ('Behaviour owner', e['name'])
    # Catalogue root entries establish named BEC responsibility contexts, not CT1.
    bec_names = set()
    for t in MD.parse(TEXT['enabling']):
        if t.type == 'fence':
            for line in t.content.splitlines():
                match = re.match(r'^\d+\. (.*?)(?: \[.*\])?$', line)
                if match:
                    bec_names.add(match.group(1))
    bec_names.update(['Person Identity', 'Healthcare Subject', 'Client Relationship', 'Client Privacy',
                      'Assurance Design', 'Assurance Criteria Management', 'Governed Assurance', 'Credential Management'])
    assert bec_names == {e['name'] for e in E.values() if e['category'] == 'business_enabling'}, 'BEC inventory'
    print('PASS: source fingerprints, exact citations, identities, parent references, BC/Feature/behaviour inventories')


def emit(relative, text):
    path = BASE / relative
    if relative in ('sections/business-capabilities.tex', 'sections/business-enabling-capabilities.tex',
                    'sections/enterprise-capabilities.tex', 'sections/identifiers.tex',
                    'sections/review-exceptions.tex'):
        text = '\n'.join(line.rstrip() for line in text.splitlines()).rstrip() + '\n'
    if path.suffix == '.tex':
        text = '% Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later\n% Generated from fingerprinted RAD Markdown and frozen publication identities.\n' + text
    if '--check' in sys.argv:
        assert path.read_text() == text, 'Generated publication drift: ' + relative
    else:
        path.write_text(text)


def main():
    validate()
    source_sections()
    emit('sections/business-capabilities.tex', business())
    emit('sections/business-enabling-capabilities.tex', enabling())
    emit('sections/enterprise-capabilities.tex', enterprise())
    emit('sections/relationships.tex', relationships())
    emit('sections/catalogue-contexts.tex', contexts())
    emit('sections/identifiers.tex', identifier_register())
    emit('sections/review-exceptions.tex', review_exceptions())
    inputs = []
    for src in S:
        path = 'sections/rad-' + src + '.tex'
        emit(path, projection(src))
        inputs.append('\\input{' + path + '}')
    emit('sections/rad-detail.tex', '\n'.join(inputs) + '\n')
    emit('source-coverage.json', json.dumps(COVERAGE, indent=2) + '\n')
    print('Rendered', len(E), 'identities and', len(COVERAGE), 'complete RAD source blocks')


if __name__ == '__main__':
    main()
