# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Recognise explicitly authored Home assertions; no historical inference."""
import exchange_xml as g

def assertion(model, relationship):
    """Return explicit semantics, resolving Archi's property definition reference.

    Ordinary Association, other specialisations and direction stay distinct.
    Relationship Name never supplies specialisation. Retain the raw XML as well
    when collecting ingestion evidence; this record does not replace it.
    """
    claims = []
    for prop in relationship.findall('a:properties/a:property', g.N):
        name = model.keys.get(prop.get('propertyDefinitionRef'))
        if name in ('Specialization', 'specialization'):
            claims.extend(v.text for v in prop.findall('a:value', g.N))
    if len(claims) > 1:
        raise ValueError('Ambiguous relationship specialisation')
    return dict(identifier=relationship.get('identifier'),
                native_type=relationship.get(g.TYPE).split(':')[-1],
                source=relationship.get('source'), target=relationship.get('target'),
                isDirected=relationship.get('isDirected') in ('true', '1'),
                specialization=claims[0] if claims else None)



def authored_homes(model):
    result = []
    for rel in model.relationships.values():
        item = assertion(model, rel)
        if item['specialization'] != 'Home':
            continue
        g.require(item['native_type'] == 'Association' and item['isDirected'],
                  'Home must be a directed native Association: ' + item['identifier'])
        result.append(item)
    return result

