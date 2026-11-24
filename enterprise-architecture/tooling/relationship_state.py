# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Shared current/accepted population selection, extracted from View selection.

Currentness is independent of architectural approval. Unclassified submitted
relationships may participate conditionally, never as implicitly accepted facts.
"""
import exchange_xml as g


def eligible_ids(records, accepted=(), proposed=()):
    """Retain the established current-record / accepted-ingestion selection."""
    return ({row['identifier'] for row in records if row['record_state'] == 'current'}
            | set(accepted) | set(proposed))


def exchange_population(model, records=None, accepted=(), include_proposals=True):
    """Adapt Exchange's source-qualified state Properties to the same selector.

    A declared record population is exhaustive; otherwise unclassified Exchange
    assertions remain proposals. Explicit historical state is never a proposal
    fallback. Resolve Property definitions, not an assumed transport key.
    """
    if records is None:
        records = []
        proposed = []
        for identifier, edge in sorted(model.relationships.items()):
            values = [v.text for p in edge.findall('a:properties/a:property', g.N)
                      if model.keys.get(p.get('propertyDefinitionRef')) in ('v0.2 / Record State', 'Record State')
                      for v in p.findall('a:value', g.N)]
            g.require(len(values) <= 1, 'Ambiguous relationship currentness: ' + identifier)
            if values:
                records.append(dict(identifier=identifier, record_state=values[0]))
            elif include_proposals:
                proposed.append(identifier)
    else:
        proposed = []
    selected = eligible_ids(records, accepted, proposed)
    return dict(eligible=selected,
                excluded=set(model.relationships) - selected,
                states={row['identifier']: row['record_state'] for row in records},
                proposed=set(proposed))
