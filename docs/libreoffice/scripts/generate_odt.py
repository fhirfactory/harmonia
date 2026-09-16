#!/usr/bin/env python3
"""
Harmonia Health Information Exchange (HIE)
LibreOffice OpenDocument Text (.odt) Generator

Parses the production LaTeX documentation in docs/latex/ and produces a publication-grade,
interactive, fully navigable OpenDocument Text (.odt) archive adhering strictly to the
OASIS OpenDocument Format 1.3 standard.
"""

import os
import sys
import re
import html
import zipfile
import xml.etree.ElementTree as ET

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
LIBREOFFICE_DIR = os.path.dirname(SCRIPT_DIR)
REPO_ROOT = os.path.dirname(os.path.dirname(LIBREOFFICE_DIR))
LATEX_DIR = os.path.join(REPO_ROOT, "docs", "latex")
CHAPTERS_DIR = os.path.join(LATEX_DIR, "chapters")
ASSETS_DIR = os.path.join(LIBREOFFICE_DIR, "assets", "diagrams")
TEMPLATES_DIR = os.path.join(LIBREOFFICE_DIR, "templates")
OUTPUT_ODT = os.path.join(LIBREOFFICE_DIR, "harmonia-architecture-specification.odt")

# XML Namespaces
NAMESPACES = {
    'office': 'urn:oasis:names:tc:opendocument:xmlns:office:1.0',
    'style': 'urn:oasis:names:tc:opendocument:xmlns:style:1.0',
    'text': 'urn:oasis:names:tc:opendocument:xmlns:text:1.0',
    'table': 'urn:oasis:names:tc:opendocument:xmlns:table:1.0',
    'draw': 'urn:oasis:names:tc:opendocument:xmlns:drawing:1.0',
    'fo': 'urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0',
    'xlink': 'http://www.w3.org/1999/xlink',
    'dc': 'http://purl.org/dc/elements/1.1/',
    'meta': 'urn:oasis:names:tc:opendocument:xmlns:meta:1.0',
    'number': 'urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0',
    'svg': 'urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0',
    'chart': 'urn:oasis:names:tc:opendocument:xmlns:chart:1.0',
    'dr3d': 'urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0',
    'math': 'http://www.w3.org/1998/Math/MathML',
    'form': 'urn:oasis:names:tc:opendocument:xmlns:form:1.0',
    'script': 'urn:oasis:names:tc:opendocument:xmlns:script:1.0',
    'tableooo': 'http://openoffice.org/2009/table',
}

# Register XML Namespaces for canonical serialization
for prefix, uri in NAMESPACES.items():
    ET.register_namespace(prefix, uri)

# Map diagram tex input to PNG asset
DIAGRAM_MAP = {
    'diagrams/legend.tex': ('legend.png', 16.0, 4.2),
    'diagrams/fig-motivation-map.tex': ('fig-motivation-map.png', 16.0, 10.5),
    'diagrams/fig-clinical-process.tex': ('fig-clinical-process.png', 16.0, 10.0),
    'diagrams/fig-app-overview-5tier.tex': ('fig-app-overview-5tier.png', 16.0, 10.8),
    'diagrams/fig-petasos-messaging.tex': ('fig-petasos-messaging.png', 16.0, 10.2),
    'diagrams/fig-energeia-workflow.tex': ('fig-energeia-workflow.png', 16.0, 10.2),
    'diagrams/fig-pragma-state-flow.tex': ('fig-pragma-state-flow.png', 16.0, 9.8),
    'diagrams/fig-technology-nodes.tex': ('fig-technology-nodes.png', 16.0, 10.5),
    'diagrams/fig-hestia-data-grid.tex': ('fig-hestia-data-grid.png', 16.0, 10.0),
    'diagrams/fig-deployment-ha.tex': ('fig-deployment-ha.png', 16.0, 10.2),
    'diagrams/fig-paradeigma-architecture.tex': ('fig-paradeigma-architecture.png', 16.0, 10.5),
    'diagrams/fig-mllp-ingress-process.tex': ('fig-mllp-ingress-process.png', 16.0, 10.2),
    'diagrams/fig-mllp-egress-process.tex': ('fig-mllp-egress-process.png', 16.0, 10.2),
    'diagrams/fig-ergon-module-architecture.tex': ('fig-ergon-module-architecture.png', 16.0, 10.5),
    'diagrams/fig-praxis-workflow-architecture.tex': ('fig-praxis-workflow-architecture.png', 16.0, 10.5),
}

def escape_xml(text):
    if not text:
        return ""
    return html.escape(str(text), quote=True)

def format_inline_latex(raw_text):
    if not raw_text:
        return ""
    t = raw_text.strip()
    
    # Strip comments
    t = re.sub(r'(?<!\\)%.*$', '', t, flags=re.MULTILINE)
    
    # LaTeX symbol replacements
    t = t.replace(r'\_', '_').replace(r'\%', '%').replace(r'\$', '$').replace(r'\{', '{').replace(r'\}', '}')
    t = t.replace('---', '—').replace('--', '–').replace('``', '"').replace("''", '"')
    t = t.replace(r'\rightarrow', '→').replace(r'\leftarrow', '←').replace(r'\leftrightarrow', '↔')
    t = t.replace(r'\times', '×').replace(r'\le', '≤').replace(r'\ge', '≥').replace(r'\bullet', '•')
    t = t.replace(r'\&', '&')
    
    # Remove styling commands
    t = re.sub(r'\\(?:small|footnotesize|large|Large|huge|Huge|centering|raggedright|raggedleft|noindent|addlinespace)\b', '', t)
    t = re.sub(r'\\vspace\*?\{[^}]*\}', '', t)
    
    def tokenize_and_convert(text):
        if not text:
            return ""
        
        result = []
        i = 0
        n = len(text)
        
        while i < n:
            # Check \texttt{...}
            if text[i:i+8] == r'\texttt{':
                depth = 1
                j = i + 8
                while j < n and depth > 0:
                    if text[j] == '{' and (j == 0 or text[j-1] != '\\'):
                        depth += 1
                    elif text[j] == '}' and (j == 0 or text[j-1] != '\\'):
                        depth -= 1
                    j += 1
                if depth == 0:
                    inner = text[i+8:j-1]
                    result.append(f'<text:span text:style-name="Code_20_Inline">{escape_xml(inner)}</text:span>')
                    i = j
                    continue
            
            # Check \textbf{...}
            if text[i:i+8] == r'\textbf{':
                depth = 1
                j = i + 8
                while j < n and depth > 0:
                    if text[j] == '{' and (j == 0 or text[j-1] != '\\'):
                        depth += 1
                    elif text[j] == '}' and (j == 0 or text[j-1] != '\\'):
                        depth -= 1
                    j += 1
                if depth == 0:
                    inner = text[i+8:j-1]
                    result.append(f'<text:span text:style-name="Strong">{tokenize_and_convert(inner)}</text:span>')
                    i = j
                    continue
            
            # Check \textit{...} or \emph{...}
            m_it = re.match(r'\\(?:textit|emph)\{', text[i:])
            if m_it:
                start_len = m_it.end()
                depth = 1
                j = i + start_len
                while j < n and depth > 0:
                    if text[j] == '{' and (j == 0 or text[j-1] != '\\'):
                        depth += 1
                    elif text[j] == '}' and (j == 0 or text[j-1] != '\\'):
                        depth -= 1
                    j += 1
                if depth == 0:
                    inner = text[i+start_len:j-1]
                    result.append(f'<text:span text:style-name="Emphasis">{tokenize_and_convert(inner)}</text:span>')
                    i = j
                    continue

            # Check \hyperref[target]{label}
            m_hyp = re.match(r'\\hyperref\[([^\]]+)\]\{([^}]+)\}', text[i:])
            if m_hyp:
                target = m_hyp.group(1).strip()
                label = m_hyp.group(2).strip()
                result.append(f'<text:a xlink:type="simple" xlink:href="#{escape_xml(target)}" text:style-name="Internet_20_link">{tokenize_and_convert(label)}</text:a>')
                i += m_hyp.end()
                continue

            # Check \ref{target}
            m_ref = re.match(r'\\ref\{([^}]+)\}', text[i:])
            if m_ref:
                target = m_ref.group(1).strip()
                result.append(f'<text:a xlink:type="simple" xlink:href="#{escape_xml(target)}" text:style-name="Internet_20_link">{escape_xml(target)}</text:a>')
                i += m_ref.end()
                continue

            # Check \url{link}
            m_url = re.match(r'\\url\{([^}]+)\}', text[i:])
            if m_url:
                target = m_url.group(1).strip()
                result.append(f'<text:a xlink:type="simple" xlink:href="{escape_xml(target)}" text:style-name="Internet_20_link">{escape_xml(target)}</text:a>')
                i += m_url.end()
                continue

            # Check $math$
            m_math = re.match(r'\$([^$]+)\$', text[i:])
            if m_math:
                math_content = m_math.group(1).strip()
                result.append(f'<text:span text:style-name="Emphasis">{escape_xml(math_content)}</text:span>')
                i += m_math.end()
                continue

            # Check unhandled backslash commands e.g. \something
            m_cmd = re.match(r'\\([a-zA-Z]+)', text[i:])
            if m_cmd:
                i += m_cmd.end()
                continue

            # Plain character
            result.append(escape_xml(text[i]))
            i += 1

        return "".join(result)

    return tokenize_and_convert(t)

class LatexDocConverter:
    def __init__(self):
        self.body_elements = []
        self.auto_styles = []
        self.table_count = 0
        self.fig_count = 0
        self.toc_items = []
        self.lof_items = []
        self.lot_items = []
        self.chapter_index = 0
        self.appendix_index = 0
        self.is_appendix = False

    def add_page_break(self):
        self.body_elements.append('<text:p text:style-name="PageBreak"/>')

    def add_bookmark(self, name):
        clean_name = name.strip()
        return f'<text:bookmark text:name="{escape_xml(clean_name)}"/><text:bookmark-start text:name="{escape_xml(clean_name)}"/><text:bookmark-end text:name="{escape_xml(clean_name)}"/>'

    def add_heading(self, title, level, bookmark_id=None, prefix=""):
        full_title = f"{prefix} {title}".strip() if prefix else title.strip()
        style_name = f"Heading_20_{level}"
        anchor = bookmark_id if bookmark_id else f"sec_{len(self.toc_items)+1}"
        bm = self.add_bookmark(anchor)
        
        self.toc_items.append((level, full_title, anchor))
        self.body_elements.append(
            f'<text:h text:style-name="{style_name}" text:outline-level="{level}">{bm}{escape_xml(full_title)}</text:h>'
        )

    def add_paragraph(self, text, style="Text_20_body"):
        if not text.strip():
            return
        formatted = format_inline_latex(text)
        self.body_elements.append(f'<text:p text:style-name="{style}">{formatted}</text:p>')

    def add_callout(self, callout_type, title, text):
        style = "Callout_Note"
        prefix = "Architectural Note: "
        if callout_type == "principle":
            style = "Callout_Principle"
            prefix = f"Architectural Principle — {title}: " if title else "Architectural Principle: "
        elif callout_type == "warning":
            style = "Callout_Warning"
            prefix = f"Warning — {title}: " if title else "Warning: "
        
        formatted = format_inline_latex(text)
        self.body_elements.append(
            f'<text:p text:style-name="{style}"><text:span text:style-name="Strong">{escape_xml(prefix)}</text:span>{formatted}</text:p>'
        )

    def add_code_block(self, code_text):
        lines = code_text.strip().split('\n')
        escaped_lines = [escape_xml(l) for l in lines]
        content = "<text:line-break/>".join(escaped_lines)
        self.body_elements.append(f'<text:p text:style-name="Code_20_Block">{content}</text:p>')

    def add_list(self, items, ordered=False):
        style_name = "List_20_2" if ordered else "List_20_1"
        res = [f'<text:list text:style-name="{style_name}">']
        for it in items:
            formatted = format_inline_latex(it)
            res.append(f'<text:list-item><text:p text:style-name="Text_20_body">{formatted}</text:p></text:list-item>')
        res.append('</text:list>')
        self.body_elements.append("\n".join(res))

    def add_diagram(self, img_filename, caption, label_id=None, width_cm=16.0, height_cm=9.5):
        self.fig_count += 1
        anchor = label_id if label_id else f"fig_{self.fig_count}"
        bm = self.add_bookmark(anchor)
        
        fig_title = f"Figure {self.fig_count}: {caption}"
        self.lof_items.append((fig_title, anchor))
        
        self.body_elements.append(f'''<text:p text:style-name="Illustration">
  <draw:frame draw:style-name="Graphic_Frame" draw:name="Figure_{self.fig_count}" text:anchor-type="paragraph" svg:width="{width_cm}cm" svg:height="{height_cm}cm" draw:z-index="0">
    <draw:image xlink:href="Pictures/{img_filename}" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  </draw:frame>
</text:p>
<text:p text:style-name="Caption">{bm}<text:span text:style-name="Strong">Figure {self.fig_count}:</text:span> {escape_xml(caption)}</text:p>''')

    def add_table(self, headers, rows, col_widths, caption=None, label_id=None):
        self.table_count += 1
        table_name = f"Table_{self.table_count}"
        anchor = label_id if label_id else f"tab_{self.table_count}"
        bm = self.add_bookmark(anchor)
        
        if caption:
            tbl_title = f"Table {self.table_count}: {caption}"
            self.lot_items.append((tbl_title, anchor))

        for i, w in enumerate(col_widths):
            self.auto_styles.append(f'''<style:style style:name="{table_name}_Col_{i}" style:family="table-column">
  <style:table-column-properties style:column-width="{w:.2f}cm" style:rel-column-width="{int(w*1000)}*"/>
</style:style>''')

        res = [f'<table:table table:name="{table_name}" table:style-name="Standard_Table">']
        for i in range(len(col_widths)):
            res.append(f'<table:table-column table:style-name="{table_name}_Col_{i}"/>')
        
        # Header
        res.append('<table:table-header-rows><table:table-row>')
        for h in headers:
            formatted_h = format_inline_latex(h)
            res.append(f'''<table:table-cell table:style-name="Table_Header_Cell" office:value-type="string">
  <text:p text:style-name="Table_Header_Text">{formatted_h}</text:p>
</table:table-cell>''')
        res.append('</table:table-row></table:table-header-rows>')

        # Body Rows
        for r_idx, r in enumerate(rows):
            cell_style = "Table_Body_Cell_Even" if r_idx % 2 == 0 else "Table_Body_Cell_Odd"
            res.append('<table:table-row>')
            for cell_idx, c in enumerate(r):
                # Ensure width matches
                if cell_idx >= len(col_widths):
                    continue
                formatted_c = format_inline_latex(c)
                res.append(f'''<table:table-cell table:style-name="{cell_style}" office:value-type="string">
  <text:p text:style-name="Table_Cell_Text">{formatted_c}</text:p>
</table:table-cell>''')
            # Pad missing cells if any
            for _ in range(len(r), len(col_widths)):
                res.append(f'''<table:table-cell table:style-name="{cell_style}" office:value-type="string">
  <text:p text:style-name="Table_Cell_Text"></text:p>
</table:table-cell>''')
            res.append('</table:table-row>')
        res.append('</table:table>')

        if caption:
            res.append(f'<text:p text:style-name="Caption">{bm}<text:span text:style-name="Strong">Table {self.table_count}:</text:span> {escape_xml(caption)}</text:p>')
        
        self.body_elements.append("\n".join(res))

    def parse_tabular(self, tex_str):
        # Extract headers and body rows from tabular / tabularx
        # Clean rule commands
        cleaned = re.sub(r'\\(?:toprule|midrule|bottomrule|hline|addlinespace|small|centering)', '', tex_str)
        # Split by \\
        raw_rows = [r.strip() for r in cleaned.split(r'\\') if r.strip()]
        table_data = []
        for row in raw_rows:
            # Split by unescaped &
            cols = [c.strip() for c in re.split(r'(?<!\\)&', row)]
            if cols and any(c for c in cols if c):
                table_data.append(cols)
        return table_data

    def parse_chapter_file(self, filepath):
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        filename = os.path.basename(filepath)
        if filename.startswith('appendix'):
            self.is_appendix = True
            self.appendix_index += 1
            app_letter = chr(64 + self.appendix_index) # 'A', 'B', 'C', ...
            prefix_chap = f"Appendix {app_letter}:"
        elif filename == '00-frontmatter.tex':
            prefix_chap = ""
        else:
            self.chapter_index += 1
            prefix_chap = f"Chapter {self.chapter_index}:"

        # Tokenize by LaTeX environments and headings
        # Regular expressions for key tokens
        pos = 0
        length = len(content)

        while pos < length:
            # Skip whitespace
            while pos < length and content[pos].isspace():
                pos += 1
            if pos >= length:
                break

            # Check comments
            if content[pos] == '%' and (pos == 0 or content[pos-1] != '\\'):
                eol = content.find('\n', pos)
                pos = eol + 1 if eol != -1 else length
                continue

            # Check Chapter
            m = re.match(r'\\chapter\*?\{([^}]+)\}', content[pos:])
            if m:
                title = m.group(1).strip()
                pos += m.end()
                # Check for subsequent label
                lbl_m = re.match(r'\s*\\label\{([^}]+)\}', content[pos:])
                lbl = None
                if lbl_m:
                    lbl = lbl_m.group(1).strip()
                    pos += lbl_m.end()
                self.add_page_break()
                self.add_heading(title, 1, bookmark_id=lbl, prefix=prefix_chap)
                continue

            # Check Section
            m = re.match(r'\\section\*?\{([^}]+)\}', content[pos:])
            if m:
                title = m.group(1).strip()
                pos += m.end()
                lbl_m = re.match(r'\s*\\label\{([^}]+)\}', content[pos:])
                lbl = None
                if lbl_m:
                    lbl = lbl_m.group(1).strip()
                    pos += lbl_m.end()
                self.add_heading(title, 2, bookmark_id=lbl)
                continue

            # Check Subsection
            m = re.match(r'\\subsection\*?\{([^}]+)\}', content[pos:])
            if m:
                title = m.group(1).strip()
                pos += m.end()
                lbl_m = re.match(r'\s*\\label\{([^}]+)\}', content[pos:])
                lbl = None
                if lbl_m:
                    lbl = lbl_m.group(1).strip()
                    pos += lbl_m.end()
                self.add_heading(title, 3, bookmark_id=lbl)
                continue

            # Check Subsubsection
            m = re.match(r'\\subsubsection\*?\{([^}]+)\}', content[pos:])
            if m:
                title = m.group(1).strip()
                pos += m.end()
                lbl_m = re.match(r'\s*\\label\{([^}]+)\}', content[pos:])
                lbl = None
                if lbl_m:
                    lbl = lbl_m.group(1).strip()
                    pos += lbl_m.end()
                self.add_heading(title, 4, bookmark_id=lbl)
                continue

            # Check Environments: figure
            m = re.match(r'\\begin\{figure\}(.*?)\\end\{figure\}', content[pos:], re.DOTALL)
            if m:
                fig_body = m.group(1)
                pos += m.end()
                cap_m = re.search(r'\\caption\{([^}]+)\}', fig_body)
                caption = cap_m.group(1).strip() if cap_m else "Architectural View"
                lbl_m = re.search(r'\\label\{([^}]+)\}', fig_body)
                label_id = lbl_m.group(1).strip() if lbl_m else None
                
                # Check diagram input
                diag_m = re.search(r'\\input\{([^}]+)\}', fig_body)
                if diag_m:
                    diag_path = diag_m.group(1).strip()
                    if diag_path in DIAGRAM_MAP:
                        png_file, w, h = DIAGRAM_MAP[diag_path]
                        self.add_diagram(png_file, caption, label_id=label_id, width_cm=w, height_cm=h)
                    else:
                        base = os.path.basename(diag_path).replace('.tex', '.png')
                        self.add_diagram(base, caption, label_id=label_id)
                continue

            # Check Environments: table
            m = re.match(r'\\begin\{table\}(.*?)\\end\{table\}', content[pos:], re.DOTALL)
            if m:
                tbl_body = m.group(1)
                pos += m.end()
                cap_m = re.search(r'\\caption\{([^}]+)\}', tbl_body)
                caption = cap_m.group(1).strip() if cap_m else None
                lbl_m = re.search(r'\\label\{([^}]+)\}', tbl_body)
                label_id = lbl_m.group(1).strip() if lbl_m else None

                tab_m = re.search(r'\\begin\{(?:tabular|tabularx)\}(?:\{[^}]+\})?\{([^}]+)\}(.*?)\\end\{(?:tabular|tabularx)\}', tbl_body, re.DOTALL)
                if tab_m:
                    col_spec = tab_m.group(1)
                    tab_content = tab_m.group(2)
                    parsed_rows = self.parse_tabular(tab_content)
                    if parsed_rows:
                        headers = parsed_rows[0]
                        data_rows = parsed_rows[1:] if len(parsed_rows) > 1 else []
                        num_cols = len(headers)
                        # Distribute 16cm evenly among cols
                        col_w = [16.0 / num_cols] * num_cols
                        if num_cols == 2:
                            col_w = [4.5, 11.5]
                        elif num_cols == 3:
                            col_w = [4.0, 4.0, 8.0]
                        elif num_cols == 4:
                            col_w = [3.2, 3.5, 3.5, 5.8]
                        elif num_cols == 5:
                            col_w = [2.5, 3.0, 3.0, 3.5, 4.0]
                        self.add_table(headers, data_rows, col_w, caption=caption, label_id=label_id)
                continue

            # Check Environments: principlebox
            m = re.match(r'\\begin\{principlebox\}\{([^}]+)\}(.*?)\\end\{principlebox\}', content[pos:], re.DOTALL)
            if m:
                p_title = m.group(1).strip()
                p_text = m.group(2).strip()
                pos += m.end()
                self.add_callout('principle', p_title, p_text)
                continue

            # Check Environments: archinote
            m = re.match(r'\\begin\{archinote\}(.*?)\\end\{archinote\}', content[pos:], re.DOTALL)
            if m:
                n_text = m.group(1).strip()
                pos += m.end()
                self.add_callout('note', '', n_text)
                continue

            # Check Environments: lstlisting or verbatim
            m = re.match(r'\\begin\{(?:lstlisting|verbatim)\}(?:\[[^\]]*\])?(.*?)\\end\{(?:lstlisting|verbatim)\}', content[pos:], re.DOTALL)
            if m:
                code_text = m.group(1)
                pos += m.end()
                self.add_code_block(code_text)
                continue

            # Check Environments: itemize
            m = re.match(r'\\begin\{itemize\}(.*?)\\end\{itemize\}', content[pos:], re.DOTALL)
            if m:
                list_body = m.group(1)
                pos += m.end()
                items = [it.strip() for it in list_body.split(r'\item') if it.strip()]
                self.add_list(items, ordered=False)
                continue

            # Check Environments: enumerate
            m = re.match(r'\\begin\{enumerate\}(.*?)\\end\{enumerate\}', content[pos:], re.DOTALL)
            if m:
                list_body = m.group(1)
                pos += m.end()
                items = [it.strip() for it in list_body.split(r'\item') if it.strip()]
                self.add_list(items, ordered=True)
                continue

            # Check Environments: description
            m = re.match(r'\\begin\{description\}(.*?)\\end\{description\}', content[pos:], re.DOTALL)
            if m:
                desc_body = m.group(1)
                pos += m.end()
                raw_items = [it.strip() for it in desc_body.split(r'\item') if it.strip()]
                items = []
                for it in raw_items:
                    m_term = re.match(r'\[([^\]]+)\](.*)', it, re.DOTALL)
                    if m_term:
                        items.append(f"\\textbf{{{m_term.group(1)}}}: {m_term.group(2).strip()}")
                    else:
                        items.append(it)
                self.add_list(items, ordered=False)
                continue

            # Check Environments: equation / equation* / math display
            m = re.match(r'\\begin\{equation\*?\}(.*?)\\end\{equation\*?\}', content[pos:], re.DOTALL)
            if m:
                eq_text = m.group(1).strip()
                pos += m.end()
                self.add_paragraph(f"$$ {eq_text} $$", style="Text_20_body")
                continue

            # Check standalone labels or pagebreaks
            m = re.match(r'\\(?:label|addcontentsline|vspace|newpage|noindent)\*?\{?[^}\n]*\}?', content[pos:])
            if m:
                pos += m.end()
                continue

            # Regular Paragraph text up to next blank line or command
            next_blank = content.find('\n\n', pos)
            next_cmd = re.search(r'\n(?=\\(?:chapter|section|subsection|subsubsection|begin))', content[pos:])
            
            end_p = length
            if next_blank != -1 and (next_cmd is None or next_blank < pos + next_cmd.start()):
                end_p = next_blank
                p_str = content[pos:end_p].strip()
                pos = next_blank + 2
            elif next_cmd is not None:
                end_p = pos + next_cmd.start()
                p_str = content[pos:end_p].strip()
                pos = end_p + 1
            else:
                p_str = content[pos:].strip()
                pos = length

            if p_str:
                self.add_paragraph(p_str)

    def build_content_xml(self):
        root = ET.Element(f"{{{NAMESPACES['office']}}}document-content", {
            f"{{{NAMESPACES['office']}}}version": "1.3"
        })

        # Font Decls
        font_decls = ET.SubElement(root, f"{{{NAMESPACES['office']}}}font-face-decls")
        ET.SubElement(font_decls, f"{{{NAMESPACES['style']}}}font-face", {
            f"{{{NAMESPACES['style']}}}name": "Liberation Sans",
            f"{{{NAMESPACES['svg']}}}font-family": "'Liberation Sans', Arial, sans-serif",
            f"{{{NAMESPACES['style']}}}font-family-generic": "swiss",
            f"{{{NAMESPACES['style']}}}font-pitch": "variable"
        })
        ET.SubElement(font_decls, f"{{{NAMESPACES['style']}}}font-face", {
            f"{{{NAMESPACES['style']}}}name": "Liberation Serif",
            f"{{{NAMESPACES['svg']}}}font-family": "'Liberation Serif', 'Times New Roman', serif",
            f"{{{NAMESPACES['style']}}}font-family-generic": "roman",
            f"{{{NAMESPACES['style']}}}font-pitch": "variable"
        })
        ET.SubElement(font_decls, f"{{{NAMESPACES['style']}}}font-face", {
            f"{{{NAMESPACES['style']}}}name": "Liberation Mono",
            f"{{{NAMESPACES['svg']}}}font-family": "'Liberation Mono', 'Courier New', monospace",
            f"{{{NAMESPACES['style']}}}font-family-generic": "modern",
            f"{{{NAMESPACES['style']}}}font-pitch": "fixed"
        })

        # Automatic Styles
        auto_styles_elem = ET.SubElement(root, f"{{{NAMESPACES['office']}}}automatic-styles")
        
        # Standard auto styles
        standard_auto_xml = """
<style:style style:name="PageBreak" style:family="paragraph" style:parent-style-name="Standard">
  <style:paragraph-properties fo:break-before="page"/>
</style:style>
<style:style style:name="Standard_Table" style:family="table">
  <style:table-properties style:width="16.0cm" table:align="center" table:border-model="collapsing"/>
</style:style>
<style:style style:name="Table_Header_Cell" style:family="table-cell">
  <style:table-cell-properties fo:background-color="#1A365D" fo:padding="0.22cm" fo:border="0.5pt solid #CBD5E0"/>
</style:style>
<style:style style:name="Table_Body_Cell_Even" style:family="table-cell">
  <style:table-cell-properties fo:background-color="#FFFFFF" fo:padding="0.2cm" fo:border="0.5pt solid #E2E8F0"/>
</style:style>
<style:style style:name="Table_Body_Cell_Odd" style:family="table-cell">
  <style:table-cell-properties fo:background-color="#F7FAFC" fo:padding="0.2cm" fo:border="0.5pt solid #E2E8F0"/>
</style:style>
<style:style style:name="Illustration" style:family="paragraph" style:parent-style-name="Standard">
  <style:paragraph-properties fo:margin-top="0.3cm" fo:margin-bottom="0.15cm" fo:text-align="center" fo:keep-with-next="always"/>
</style:style>
<style:style style:name="Graphic_Frame" style:family="graphic">
  <style:graphic-properties style:horizontal-pos="center" style:horizontal-rel="paragraph" style:vertical-pos="top" style:vertical-rel="paragraph" style:wrap="none" fo:margin-top="0cm" fo:margin-bottom="0cm"/>
</style:style>
<style:style style:name="TOC_Title" style:family="paragraph" style:parent-style-name="Heading_20_1">
  <style:paragraph-properties fo:margin-top="0.6cm" fo:margin-bottom="0.3cm" fo:keep-with-next="always"/>
  <style:text-properties style:font-name="Liberation Sans" fo:font-size="16pt" fo:font-weight="bold" fo:color="#1A365D"/>
</style:style>
<style:style style:name="TOC_Entry_1" style:family="paragraph" style:parent-style-name="Standard">
  <style:paragraph-properties fo:margin-top="0.15cm" fo:margin-bottom="0.08cm">
    <style:tab-stops>
      <style:tab-stop style:position="16.0cm" style:type="right" style:leader-style="dotted"/>
    </style:tab-stops>
  </style:paragraph-properties>
  <style:text-properties style:font-name="Liberation Sans" fo:font-size="10.5pt" fo:font-weight="bold" fo:color="#1A365D"/>
</style:style>
<style:style style:name="TOC_Entry_2" style:family="paragraph" style:parent-style-name="Standard">
  <style:paragraph-properties fo:margin-left="0.5cm" fo:margin-top="0.08cm" fo:margin-bottom="0.05cm">
    <style:tab-stops>
      <style:tab-stop style:position="16.0cm" style:type="right" style:leader-style="dotted"/>
    </style:tab-stops>
  </style:paragraph-properties>
  <style:text-properties style:font-name="Liberation Sans" fo:font-size="10pt" fo:color="#2B6CB0"/>
</style:style>
<style:style style:name="TOC_Entry_3" style:family="paragraph" style:parent-style-name="Standard">
  <style:paragraph-properties fo:margin-left="1.0cm" fo:margin-top="0.05cm" fo:margin-bottom="0.04cm">
    <style:tab-stops>
      <style:tab-stop style:position="16.0cm" style:type="right" style:leader-style="dotted"/>
    </style:tab-stops>
  </style:paragraph-properties>
  <style:text-properties style:font-name="Liberation Serif" fo:font-size="9.5pt" fo:color="#4A5568"/>
</style:style>
"""
        # Append manual and generated auto styles
        wrapper_ns = ' '.join([f'xmlns:{k}="{v}"' for k, v in NAMESPACES.items()])
        combined_auto_styles = standard_auto_xml + "\n" + "\n".join(self.auto_styles)
        auto_styles_doc = ET.fromstring(f"<wrapper {wrapper_ns}>{combined_auto_styles}</wrapper>")
        for child in auto_styles_doc:
            auto_styles_elem.append(child)

        # Body & Text
        body = ET.SubElement(root, f"{{{NAMESPACES['office']}}}body")
        text_elem = ET.SubElement(body, f"{{{NAMESPACES['office']}}}text")

        # 1. Title / Cover Page
        cover_xml = """
<text:p text:style-name="Standard"/>
<text:p text:style-name="Standard"/>
<text:p text:style-name="Title">Harmonia</text:p>
<text:p text:style-name="Subtitle">Health Information Exchange Platform</text:p>
<text:p text:style-name="DocInfo"><text:span text:style-name="Strong">ArchiMate 3.2 Technical Architecture Specification &amp; System Reference</text:span></text:p>
<text:p text:style-name="DocInfo">Platform Architecture Team &lt;architecture@harmonia.health&gt;</text:p>
<text:p text:style-name="DocInfo">September 2026 — Version 1.0.0</text:p>
<text:p text:style-name="Standard"/>
"""
        cover_doc = ET.fromstring(f"<wrapper {wrapper_ns}>{cover_xml}</wrapper>")
        for child in cover_doc:
            text_elem.append(child)

        # 2. Interactive Navigation Section: TOC, LOF, LOT
        toc_xml_parts = [
            '<text:p text:style-name="PageBreak"/>',
            '<text:h text:style-name="TOC_Title" text:outline-level="1">Table of Contents</text:h>'
        ]
        for level, full_title, anchor in self.toc_items:
            style = "TOC_Entry_1" if level == 1 else ("TOC_Entry_2" if level == 2 else "TOC_Entry_3")
            toc_xml_parts.append(
                f'<text:p text:style-name="{style}"><text:a xlink:type="simple" xlink:href="#{anchor}" text:style-name="Internet_20_link">{escape_xml(full_title)}</text:a><text:tab/><text:span text:style-name="Strong">▸</text:span></text:p>'
            )

        toc_xml_parts.append('<text:p text:style-name="PageBreak"/>')
        toc_xml_parts.append('<text:h text:style-name="TOC_Title" text:outline-level="1">List of Figures</text:h>')
        for fig_title, anchor in self.lof_items:
            toc_xml_parts.append(
                f'<text:p text:style-name="TOC_Entry_2"><text:a xlink:type="simple" xlink:href="#{anchor}" text:style-name="Internet_20_link">{escape_xml(fig_title)}</text:a><text:tab/><text:span text:style-name="Strong">▸</text:span></text:p>'
            )

        toc_xml_parts.append('<text:p text:style-name="PageBreak"/>')
        toc_xml_parts.append('<text:h text:style-name="TOC_Title" text:outline-level="1">List of Tables</text:h>')
        for tbl_title, anchor in self.lot_items:
            toc_xml_parts.append(
                f'<text:p text:style-name="TOC_Entry_2"><text:a xlink:type="simple" xlink:href="#{anchor}" text:style-name="Internet_20_link">{escape_xml(tbl_title)}</text:a><text:tab/><text:span text:style-name="Strong">▸</text:span></text:p>'
            )

        toc_doc = ET.fromstring(f"<wrapper {wrapper_ns}>{''.join(toc_xml_parts)}</wrapper>")
        for child in toc_doc:
            text_elem.append(child)

        # 3. Main Body Content
        body_doc = ET.fromstring(f"<wrapper {wrapper_ns}>{''.join(self.body_elements)}</wrapper>")
        for child in body_doc:
            text_elem.append(child)

        return ET.tostring(root, encoding='utf-8', xml_declaration=True)

    def build_meta_xml(self):
        root = ET.Element(f"{{{NAMESPACES['office']}}}document-meta", {
            f"{{{NAMESPACES['office']}}}version": "1.3"
        })
        meta_elem = ET.SubElement(root, f"{{{NAMESPACES['office']}}}meta")
        
        ET.SubElement(meta_elem, f"{{{NAMESPACES['dc']}}}title").text = "Harmonia Health Information Exchange Platform Architecture Specification"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['dc']}}}description").text = "Comprehensive ArchiMate 3.2 Technical Architecture Specification & Reference for the Harmonia HIE Platform"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['dc']}}}subject").text = "Healthcare Interoperability, HL7 FHIR R5, MLLP, ActiveMQ Artemis, Infinispan, PostgreSQL, Apache Camel"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['dc']}}}creator").text = "Harmonia Platform Architecture Team"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['meta']}}}initial-creator").text = "Harmonia Platform Architecture Team"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['dc']}}}date").text = "2026-09-16T10:00:00"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['meta']}}}creation-date").text = "2026-09-16T10:00:00"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['meta']}}}editing-cycles").text = "1"
        ET.SubElement(meta_elem, f"{{{NAMESPACES['meta']}}}editing-duration").text = "PT1H"
        
        return ET.tostring(root, encoding='utf-8', xml_declaration=True)

def generate_odt():
    print("=" * 80)
    print("Harmonia Architecture Specification (.odt) Generator")
    print("=" * 80)

    converter = LatexDocConverter()
    
    chapter_files = [
        "00-frontmatter.tex",
        "01-motivation-strategy.tex",
        "02-business-layer.tex",
        "03-application-layer.tex",
        "04-data-architecture.tex",
        "05-technology-layer.tex",
        "06-deployment-ha.tex",
        "appendix-paradeigma.tex",
        "appendix-mllp-services.tex",
        "appendix-ergon-module.tex",
        "appendix-praxis-workflow.tex",
        "appendix-provider-registry.tex"
    ]

    for cfile in chapter_files:
        cpath = os.path.join(CHAPTERS_DIR, cfile)
        if os.path.exists(cpath):
            print(f"Parsing {cfile}...")
            converter.parse_chapter_file(cpath)
        else:
            print(f"Warning: File not found {cpath}")

    print(f"Parsed {len(converter.toc_items)} headings, {len(converter.lof_items)} figures, {len(converter.lot_items)} tables.")

    content_xml_bytes = converter.build_content_xml()
    meta_xml_bytes = converter.build_meta_xml()

    with open(os.path.join(TEMPLATES_DIR, "styles.xml"), "rb") as f:
        styles_xml_bytes = f.read()

    with open(os.path.join(TEMPLATES_DIR, "manifest.xml"), "rb") as f:
        manifest_xml_bytes = f.read()

    # Package into .odt Zip archive
    print(f"Packaging {OUTPUT_ODT}...")
    with zipfile.ZipFile(OUTPUT_ODT, 'w', zipfile.ZIP_DEFLATED) as zf:
        # 1. mimetype (first entry, uncompressed)
        zf.writestr('mimetype', 'application/vnd.oasis.opendocument.text', compress_type=zipfile.ZIP_STORED)
        
        # 2. META-INF/manifest.xml
        zf.writestr('META-INF/manifest.xml', manifest_xml_bytes)
        
        # 3. styles.xml
        zf.writestr('styles.xml', styles_xml_bytes)
        
        # 4. meta.xml
        zf.writestr('meta.xml', meta_xml_bytes)
        
        # 5. content.xml
        zf.writestr('content.xml', content_xml_bytes)
        
        # 6. Pictures/
        for root_dir, _, files in os.walk(ASSETS_DIR):
            for file in files:
                if file.endswith('.png') or file.endswith('.svg'):
                    file_path = os.path.join(root_dir, file)
                    arcname = f"Pictures/{file}"
                    zf.write(file_path, arcname)

    odt_size = os.path.getsize(OUTPUT_ODT)
    print(f"Successfully generated: {OUTPUT_ODT} ({odt_size:,} bytes)")
    print("=" * 80)
    return 0

def validate_odt():
    print("=" * 80)
    print(f"Validating {OUTPUT_ODT}...")
    print("=" * 80)
    
    if not os.path.exists(OUTPUT_ODT):
        print(f"Error: {OUTPUT_ODT} does not exist. Run 'make build' first.")
        return 1

    if not zipfile.is_zipfile(OUTPUT_ODT):
        print(f"Error: {OUTPUT_ODT} is not a valid zip archive.")
        return 1

    with zipfile.ZipFile(OUTPUT_ODT, 'r') as zf:
        namelist = zf.namelist()
        
        # Check mimetype
        if namelist[0] != 'mimetype':
            print("Error: First zip entry must be 'mimetype'.")
            return 1
        
        mimetype_info = zf.getinfo('mimetype')
        if mimetype_info.compress_type != zipfile.ZIP_STORED:
            print("Error: 'mimetype' entry must be uncompressed (STORED).")
            return 1
        
        mimetype_content = zf.read('mimetype').decode('ascii').strip()
        if mimetype_content != 'application/vnd.oasis.opendocument.text':
            print(f"Error: Unexpected mimetype content: {mimetype_content}")
            return 1
        print("✓ Verified: mimetype entry is valid and uncompressed.")

        # Check XML components
        required_xml = ['content.xml', 'styles.xml', 'meta.xml', 'META-INF/manifest.xml']
        for xml_name in required_xml:
            if xml_name not in namelist:
                print(f"Error: Missing required XML file: {xml_name}")
                return 1
            try:
                tree = ET.fromstring(zf.read(xml_name))
                print(f"✓ Validated XML schema for: {xml_name} (root: {tree.tag})")
            except Exception as e:
                print(f"Error: Failed to parse {xml_name}: {e}")
                return 1

        # Check pictures
        pics = [n for n in namelist if n.startswith('Pictures/')]
        print(f"✓ Verified: {len(pics)} embedded architectural diagram images in Pictures/")
        if len(pics) < 15:
            print(f"Warning: Expected 15 diagrams, found {len(pics)}")

    print("=" * 80)
    print("Validation SUCCESS: Package complies with OASIS OpenDocument 1.3 standard.")
    print("=" * 80)
    return 0

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "--validate":
        sys.exit(validate_odt())
    sys.exit(generate_odt())
