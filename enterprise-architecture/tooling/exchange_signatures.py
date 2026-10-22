# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Exact authored Exchange fields, resolving properties by definition name."""
import exchange_xml as g

def native_type(node):
    value = node.get(g.TYPE, '')
    if ':' in value:
        prefix, name = value.split(':', 1)
        return name if node.nsmap.get(prefix) == g.NS else value
    return value



def text_values(node, name):
    return [dict(text=c.text or '', attributes=dict(sorted(c.attrib.items())))
            for c in node.findall('a:' + name, g.N)]



def signature(node, model):
    """Exact content except XML indentation, prefixes and resolved Property IDs."""
    attrs = dict(node.attrib)
    if g.TYPE in attrs:
        attrs[g.TYPE] = native_type(node)
    if 'propertyDefinitionRef' in attrs:
        ref = attrs['propertyDefinitionRef']
        attrs['propertyDefinitionRef'] = model.keys.get(ref, 'UNRESOLVED:' + ref)
    text = node.text if not len(node) or (node.text or '').strip() else None
    return [node.tag, attrs, text, [signature(c, model) for c in node if isinstance(c.tag, str)]]



def specialization(model, node):
    properties = [p for key in ('Specialization', 'specialization') for p in model.claims(node, key)]
    texts = [v.text or '' for p in properties for v in p.findall('a:value', g.N)]
    return dict(claims=[signature(p, model) for p in properties], values=texts,
                single=texts[0] if len(properties) == len(texts) == 1 else None)

