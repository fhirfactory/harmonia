# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Small evidence-local OOXML reader/writer; no office application claims."""
import copy
import posixpath
import re
import xml.etree.ElementTree as ET
import zipfile

S = 'http://schemas.openxmlformats.org/spreadsheetml/2006/main'
R = 'http://schemas.openxmlformats.org/officeDocument/2006/relationships'
P = 'http://schemas.openxmlformats.org/package/2006/relationships'
T = 'http://schemas.openxmlformats.org/package/2006/content-types'
ET.register_namespace('x', S)
ET.register_namespace('r', R)


def q(name):
    return '{' + S + '}' + name


def xml(root):
    return ET.tostring(root, encoding='utf-8', xml_declaration=True)


def column(n):
    result = ''
    while n:
        n, a = divmod(n - 1, 26)
        result = chr(65 + a) + result
    return result


def col_number(ref):
    result = 0
    for ch in re.match('[A-Z]+', ref).group():
        result = result * 26 + ord(ch) - 64
    return result


def put(row, n, value):
    ref = column(n) + row.get('r')
    old = next((c for c in row if c.get('r') == ref), None)
    style = old.get('s') if old is not None else None
    if old is not None:
        row.remove(old)
    attrs = {'r': ref, 't': 'inlineStr'}
    if style is not None:
        attrs['s'] = style
    c = ET.SubElement(row, q('c'), attrs)
    text = ET.SubElement(ET.SubElement(c, q('is')), q('t'))
    text.text = str(value)
    if str(value).strip() != str(value):
        text.set('{http://www.w3.org/XML/1998/namespace}space', 'preserve')
    row[:] = sorted(row, key=lambda x: col_number(x.get('r')))


def make_sheet(rows, styles):
    root = ET.Element(q('worksheet'))
    views = ET.SubElement(root, q('sheetViews'))
    view = ET.SubElement(views, q('sheetView'), workbookViewId='0')
    ET.SubElement(view, q('pane'), ySplit='1', topLeftCell='A2', activePane='bottomLeft', state='frozen')
    cols = ET.SubElement(root, q('cols'))
    ET.SubElement(cols, q('col'), min='1', max='1', width='25', customWidth='1')
    ET.SubElement(cols, q('col'), min='2', max=str(max(map(len, rows))), width='65', customWidth='1')
    data = ET.SubElement(root, q('sheetData'))
    for n, values in enumerate(rows, 1):
        row = ET.SubElement(data, q('row'), r=str(n), ht='32' if n == 1 else '85', customHeight='1')
        for i, value in enumerate(values, 1):
            put(row, i, value)
        for c in row:
            c.set('s', str(styles[0 if n == 1 else 1]))
    ET.SubElement(root, q('autoFilter'), ref=f'A1:{column(max(map(len, rows)))}{len(rows)}')
    return root


class Workbook:
    def __init__(self, path):
        with zipfile.ZipFile(path) as z:
            assert z.testzip() is None
            self.data = {n: z.read(n) for n in z.namelist()}
        self.root = ET.fromstring(self.data['xl/workbook.xml'])
        self.rels = ET.fromstring(self.data['xl/_rels/workbook.xml.rels'])
        self.types = ET.fromstring(self.data['[Content_Types].xml'])
        self.sheets = self.root.find(q('sheets'))
        self.strings = []
        if 'xl/sharedStrings.xml' in self.data:
            self.strings = [''.join(t.text or '' for t in s.iter(q('t'))) for s in ET.fromstring(self.data['xl/sharedStrings.xml'])]
        styles = ET.fromstring(self.data['xl/styles.xml']).find(q('cellXfs'))
        self.styles = (len(styles) - 1, len(styles) - 2)

    def path(self, name):
        sheet = next(s for s in self.sheets if s.get('name') == name)
        target = next(r.get('Target') for r in self.rels if r.get('Id') == sheet.get('{' + R + '}id'))
        return target.lstrip('/') if target.startswith('/') else posixpath.normpath('xl/' + target)

    def read(self, name):
        return ET.fromstring(self.data[self.path(name)])

    def value(self, cell):
        if cell.get('t') == 's':
            return self.strings[int(cell.find(q('v')).text)]
        if cell.get('t') == 'inlineStr':
            return ''.join(t.text or '' for t in cell.iter(q('t')))
        v = cell.find(q('v'))
        return '' if v is None else v.text or ''

    def values(self, row):
        cells = {col_number(c.get('r')): self.value(c) for c in row}
        return [cells.get(i, '') for i in range(1, max(cells, default=0) + 1)]

    def rows(self, name):
        return [self.values(row) for row in self.read(name).find(q('sheetData'))]

    def save_sheet(self, name, root):
        self.data[self.path(name)] = xml(root)

    def replace(self, name, rows):
        self.save_sheet(name, make_sheet(rows, self.styles))

    def append(self, name, content):
        assert name not in {s.get('name') for s in self.sheets} and len(name) <= 31
        n = max(int(s.get('sheetId')) for s in self.sheets) + 1
        path = f'xl/worksheets/sheet{n}.xml'
        assert path not in self.data
        rid = f'rIdPass3SR{n}'
        s = ET.SubElement(self.sheets, q('sheet'), name=name, sheetId=str(n))
        s.set('{' + R + '}id', rid)
        ET.SubElement(self.rels, '{' + P + '}Relationship', Id=rid, Type=R + '/worksheet', Target=f'worksheets/sheet{n}.xml')
        ET.SubElement(self.types, '{' + T + '}Override', PartName='/' + path, ContentType='application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml')
        self.data[path] = content if isinstance(content, bytes) else xml(make_sheet(content, self.styles))

    def write(self, path):
        self.data['xl/workbook.xml'] = xml(self.root)
        self.data['xl/_rels/workbook.xml.rels'] = xml(self.rels)
        self.data['[Content_Types].xml'] = xml(self.types)
        if 'docProps/app.xml' in self.data:
            app = ET.fromstring(self.data['docProps/app.xml'])
            vt = '{http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes}'
            ap = '{http://schemas.openxmlformats.org/officeDocument/2006/extended-properties}'
            titles = app.find(ap + 'TitlesOfParts')
            if titles is not None:
                titles.clear()
                vector = ET.SubElement(titles, vt + 'vector', size=str(len(self.sheets)), baseType='lpstr')
                for s in self.sheets:
                    ET.SubElement(vector, vt + 'lpstr').text = s.get('name')
            pairs = app.find(ap + 'HeadingPairs')
            if pairs is not None:
                for item in pairs.iter(vt + 'i4'):
                    item.text = str(len(self.sheets))
            self.data['docProps/app.xml'] = xml(app)
        with zipfile.ZipFile(path, 'w', zipfile.ZIP_DEFLATED) as z:
            for name, content in self.data.items():
                # Stable member timestamps support reproducible workbook bytes.
                info = zipfile.ZipInfo(name, (1980, 1, 1, 0, 0, 0))
                info.compress_type = zipfile.ZIP_DEFLATED
                z.writestr(info, content)
