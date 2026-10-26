# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Governed lexical spelling and qualified names; no allocation or migration."""
import re
import exchange_xml as g

FORMAL = {'feature': 'FEAT', 'function': 'FN', 'service': 'SV', 'process': 'PR',
          'product': 'PRODUCT', 'BusinessObject': 'BO', 'Artifact': 'ARTIFACT',
          'Package': 'PKG', 'Class': 'CLASS'}

def component(label):
    """PascalCase lexical spelling; punctuation separates words, '&' spells And.

    Case boundaries preserve existing compound words; initialisms become ordinary
    PascalCase words. This never adds architectural grouping or removes words.
    """
    label = label.replace('&', ' And ')
    label = re.sub(r'([a-z0-9])([A-Z])', r'\1 \2', label)
    label = re.sub(r'([A-Z])([A-Z][a-z])', r'\1 \2', label)
    words = re.findall(r'[^\W_]+', label, re.UNICODE)
    value = ''.join(word[:1].upper() + word[1:].lower() for word in words)
    g.require(value and value[0].isalpha() and not any(c.isspace() for c in value),
              'Cannot establish PascalCase component: ' + repr(label))
    return value



def formal_qualifier(record):
    """The governed naming vocabulary, independently of identity or ancestry."""
    if record['type'] == 'DataObject':
        if record.get('specialization') == 'Class':
            return 'CLASS'
        g.require(record.get('information_map') is True or record.get('specialization') == 'Topic',
                  'TOPIC requires established InformationMap capture or explicit Topic specialization')
        return 'TOPIC'
    if record['type'] == 'Grouping':
        if record.get('specialization') == 'Package':
            return 'PKG'
    if record['type'] in ('Package', 'Class'):
        return FORMAL[record['type']]
    if record['type'] in FORMAL:
        return FORMAL[record['type']]
    tier = record.get('tier')
    g.require(tier in ('CT1', 'CT2', 'CT3'), 'Missing formal tier: ' + record['key'])
    return tier + {'business': '', 'business_enabling': '-BEC', 'enterprise': '-EC'}[record['category']]



def qualified_names(path):
    """Apply the same lexical and qualification rules to each governed component."""
    friendly_labels = []
    canonical_labels = []
    for resource in path:
        if 'nameComponent' in resource:
            value = resource['nameComponent']
            g.require(isinstance(value, str) and re.fullmatch(r'[A-Z][A-Za-z0-9]*', value)
                      and resource.get('nameComponentAuthority'), 'Unqualified/unproven name component decision')
            canonical_val = value
            friendly_val = value
        else:
            canonical_val = component(resource['name'])
            if resource.get('specialization') == 'Package' or resource.get('type') == 'Package':
                friendly_val = resource['name']
            else:
                friendly_val = canonical_val
        friendly_labels.append(friendly_val)
        canonical_labels.append(canonical_val)
    return dict(friendlyName='.'.join(friendly_labels),
                canonicalName='.'.join(formal_qualifier(e) + '-' + label for e, label in zip(path, canonical_labels)))

