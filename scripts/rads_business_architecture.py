#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later.
"""Validate and navigate the RADS Business Architecture reference candidate.

The capture is maintained against architectural evidence, never regenerated from
Markdown nesting. This tool generates views of the captured assertions only.
"""

import argparse
import hashlib
import json
import re
import sys
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MODEL_DIR = ROOT / 'docs/markdown/03-business-architecture/reference-model'
DEFAULT_MODEL = MODEL_DIR / 'business-architecture.json'
SCHEMA_PATH = MODEL_DIR / 'business-architecture.schema.json'
TOKEN_CODE = r'[^.\s]+'
CAPABILITY_PATH = rf'CT1-{TOKEN_CODE}(?:\.CT2-{TOKEN_CODE}(?:\.CT3-{TOKEN_CODE})?)?'
CT3_PATH = rf'CT1-{TOKEN_CODE}\.CT2-{TOKEN_CODE}\.CT3-{TOKEN_CODE}'
CANONICAL = re.compile(
    rf'(?:{CAPABILITY_PATH}|{CT3_PATH}\.FEAT-{TOKEN_CODE})(?:\.(?:FN|SV|PR)-{TOKEN_CODE})?\Z')
LOCAL_PREFIX = {'feature': 'FEAT', 'function': 'FN', 'service': 'SV', 'process': 'PR'}
RELATION_TYPES = {
    'capability_decomposition': ({'capability'}, {'capability'}),
    'responsibility_parent': ({'capability'}, {'capability'}),
    'enabled_by': ({'capability'}, {'capability'}),
    'realises_feature': ({'function'}, {'feature'}),
    'associated_feature': ({'service'}, {'feature'}),
    'contributes_feature': ({'function', 'service'}, {'feature'}),
    'governed_process': ({'function'}, {'process'}),
    'participating_function': ({'process'}, {'function'}),
    'uses_process': ({'service'}, {'process'}),
    'exposes_process': ({'service'}, {'process'}),
}


def load_json(path):
    """Refuse duplicate object properties rather than silently retaining one."""
    def pairs(items):
        result = {}
        for name, value in items:
            if name in result:
                raise ValueError(f'duplicate JSON property {name!r}')
            result[name] = value
        return result
    return json.loads(Path(path).read_text(), object_pairs_hook=pairs)


def schema_errors(value, schema, document=None, path='$'):
    """Check the JSON Schema vocabulary used by this dependency-free contract.

    Deliberately bounded to the published schema's vocabulary. Unknown keywords
    fail loudly; extending the schema cannot silently disable validation.
    """
    document = document or schema
    supported = {'$schema', '$id', '$defs', 'title', 'description', '$ref', 'anyOf',
                 'type', 'const', 'enum', 'required', 'properties', 'additionalProperties',
                 'items', 'minItems', 'uniqueItems', 'minLength', 'pattern', 'minimum'}
    unknown = set(schema) - supported
    if unknown:
        return [f'{path}: unsupported schema keywords {sorted(unknown)}']
    if '$ref' in schema:
        target = document
        if not schema['$ref'].startswith('#/'):
            return [f'{path}: only local schema references are supported']
        for part in schema['$ref'][2:].split('/'):
            target = target[part]
        return schema_errors(value, target, document, path)
    if 'anyOf' in schema and not any(not schema_errors(value, option, document, path) for option in schema['anyOf']):
        return [f'{path}: no permitted schema alternative matches']
    if 'const' in schema and (value != schema['const'] or type(value) is not type(schema['const'])):
        return [f'{path}: expected constant {schema["const"]!r}']
    if 'enum' in schema and not any(type(value) is type(item) and value == item for item in schema['enum']):
        return [f'{path}: value outside enum {schema["enum"]!r}']
    checks = {'object': lambda v: isinstance(v, dict), 'array': lambda v: isinstance(v, list),
              'string': lambda v: isinstance(v, str), 'integer': lambda v: type(v) is int,
              'null': lambda v: v is None}
    if 'type' in schema and not checks[schema['type']](value):
        return [f'{path}: expected {schema["type"]}']
    errors = []
    if isinstance(value, dict):
        for name in schema.get('required', []):
            if name not in value:
                errors.append(f'{path}: missing property {name}')
        properties = schema.get('properties', {})
        for name, item in value.items():
            if name in properties:
                errors.extend(schema_errors(item, properties[name], document, f'{path}.{name}'))
            else:
                additional = schema.get('additionalProperties', True)
                if additional is False:
                    errors.append(f'{path}: unexpected property {name}')
                elif isinstance(additional, dict):
                    errors.extend(schema_errors(item, additional, document, f'{path}.{name}'))
    if isinstance(value, list):
        if len(value) < schema.get('minItems', 0):
            errors.append(f'{path}: too few items')
        if schema.get('uniqueItems') and len({json.dumps(v, sort_keys=True) for v in value}) != len(value):
            errors.append(f'{path}: duplicate items')
        for i, item in enumerate(value):
            errors.extend(schema_errors(item, schema.get('items', {}), document, f'{path}[{i}]'))
    if isinstance(value, str):
        if len(value) < schema.get('minLength', 0):
            errors.append(f'{path}: string too short')
        if 'pattern' in schema and not re.search(schema['pattern'], value):
            errors.append(f'{path}: pattern mismatch')
    if type(value) is int and value < schema.get('minimum', value):
        errors.append(f'{path}: below minimum')
    return errors


def anchors(text):
    explicit = set(re.findall(r'<a\s+id="([^"]+)"', text))
    counts = Counter()
    for heading in re.findall(r'^#{1,6}\s+(.+)$', text, re.M):
        slug = re.sub(r'[^\w\s-]', '', heading.lower()).replace(' ', '-')
        count = counts[slug]
        counts[slug] += 1
        explicit.add(f'{slug}-{count}' if count else slug)
    return explicit


def validate(model, root=ROOT, check_sources=True):
    """Return INVALID errors separately from permitted architectural uncertainty."""
    errors = schema_errors(model, load_json(SCHEMA_PATH))
    if errors:
        return errors, []
    elements = {e['key']: e for e in model['elements']}
    views = {v['key']: v for v in model['views']}
    contexts = {c['key']: c for c in model['catalogue_contexts']}
    all_keys = [r['key'] for group in ('elements', 'views', 'catalogue_contexts') for r in model[group]]
    if len(all_keys) != len(set(all_keys)):
        errors.append('duplicate record key (including element/view/context collisions)')
    if model['authority'] not in model['sources']:
        errors.append('unresolved authority source reference')
    identifiers = {}
    capability_names = {}
    uncertainty = []

    def error(record, message):
        errors.append(f'{record["key"]}: {message}')

    def identifier(value, e, kind):
        if value in identifiers:
            prior, prior_kind = identifiers[value]
            error(e, f'identifier collision {value!r} ({prior_kind}/{kind}) with {prior["key"]}')
        else:
            identifiers[value] = e, kind

    for e in model['elements']:
        kind, tier, canonical = e['type'], e.get('tier'), e['canonical_id']
        unresolved = set(e['unresolved'])
        if unresolved - model['unresolved_meanings'].keys():
            error(e, 'undefined uncertainty reason')
        for reason in e['unresolved']:
            uncertainty.append({'key': e['key'], 'reason': reason})
        if e['ancestry_status'] == 'unresolved' and 'ancestry' not in unresolved:
            error(e, 'unresolved ancestry requires an explicit reason')
        if e['ancestry_status'] == 'established' and 'ancestry' in unresolved:
            error(e, 'established ancestry contradicts unresolved ancestry')
        if canonical is None and 'canonical_id' not in unresolved:
            error(e, 'unallocated Canonical ID requires explicit uncertainty')
        if canonical is not None and (unresolved & {'canonical_id', 'ancestry', 'code_allocation', 'tier', 'root_status'}):
            error(e, 'Canonical ID contradicts unresolved identity prerequisites')
        for view in e.get('views', []):
            if view not in views:
                error(e, f'unresolved contextual view reference {view}')
        context = e.get('catalogue_context')
        if context and context not in contexts:
            error(e, f'unresolved catalogue context reference {context}')
        if kind == 'capability':
            if e['name'] in capability_names:
                prior = capability_names[e['name']]
                # A captured source conflict remains a conflict, not a uniqueness
                # waiver or a BC/BEC identity merger. Both records must explicitly
                # preserve it; an unreported duplicate is still INVALID.
                if not (e.get('category') != prior.get('category')
                        and 'name_uniqueness' in e['unresolved']
                        and 'name_uniqueness' in prior['unresolved']):
                    error(e, 'duplicate Capability canonical name')
            capability_names[e['name']] = e
            if 'category' not in e:
                error(e, 'Capability category is required')
            if 'tier' not in e:
                error(e, 'explicit Capability tier value or null is required')
            if tier not in (None, 'CT1', 'CT2', 'CT3'):
                error(e, 'invalid Capability Tier (FT is a Feature)')
            if tier is None and 'tier' not in unresolved:
                error(e, 'unallocated Capability Tier requires explicit uncertainty')
            if tier is not None and 'tier' in unresolved:
                error(e, 'established tier contradicts unresolved tier')
            if e.get('owner'):
                error(e, 'Capability ancestry must use capability_parent, not behavioural owner')
        else:
            owner = elements.get(e.get('owner'))
            if not owner or owner['type'] not in ({'capability'} if kind == 'feature' else {'capability', 'feature'}):
                error(e, 'invalid or missing Capability/Feature responsibility context (orphan)')
            elif owner['status'] != 'established':
                error(e, 'owning responsibility context is not established')
            if kind == 'feature' and tier != 'FT':
                error(e, 'Feature must retain FT type')
            if kind == 'feature' and owner and owner.get('category') != 'business_enabling':
                error(e, 'Harmonia Feature owner must be Business Enabling, not Business Capability')
            if kind == 'feature' and e['ancestry_status'] == 'established':
                if not owner or owner.get('tier') != 'CT3' or owner['ancestry_status'] != 'established':
                    error(e, 'complete Feature ancestry requires an immediate Business Enabling CT3 frame')
            if kind != 'feature' and 'tier' in e:
                error(e, 'behaviour is not a Capability Tier')
            if 'category' in e or e.get('capability_parent'):
                error(e, 'behaviour/Feature cannot carry Capability ancestry properties')
        parent = elements.get(e.get('capability_parent'))
        if e.get('capability_parent') and (not parent or parent['type'] != 'capability'):
            error(e, 'invalid Capability parent; views/contexts cannot be ancestors')
        if parent and kind == 'capability' and parent.get('category') != e.get('category'):
            error(e, 'Capability decomposition cannot cross Business/Business Enabling categories')
        if e['ancestry_status'] == 'established' and kind == 'capability':
            if tier is None:
                error(e, 'complete Capability ancestry requires an established tier')
            expected = {'CT2': 'CT1', 'CT3': 'CT2'}.get(tier)
            if expected and (not parent or parent.get('tier') != expected or parent['ancestry_status'] != 'established'):
                error(e, 'missing established higher-tier ancestor')
            if tier == 'CT1' and e.get('capability_parent'):
                error(e, 'root CT1 cannot carry an ancestor')
        local = e.get('local_id')
        if local:
            prefix = tier if kind == 'capability' else LOCAL_PREFIX[kind]
            if not prefix or not re.fullmatch(rf'{prefix}-{TOKEN_CODE}', local):
                error(e, 'invalid local identifier token type')
            # Local codes are scoped tokens, not members of the complete-ID namespace.
            # Duplicate local lookups can be disambiguated by record key/full Canonical ID.
            if not any(local in ref['text'] for ref in e['sources']):
                error(e, 'local identifier spelling lacks source evidence')
        if canonical:
            identifier(canonical, e, 'canonical')
            if not CANONICAL.fullmatch(canonical):
                error(e, f'invalid Canonical ID token grammar {canonical!r}')
                continue
            parts = canonical.split('.')
            terminal_type = parts[-1].split('-', 1)[0]
            expected_type = tier if kind == 'capability' else LOCAL_PREFIX[kind]
            if terminal_type != expected_type:
                error(e, 'Canonical ID terminal token contradicts Element Type/Tier')
            if local and parts[-1] != local:
                error(e, 'Canonical ID changes established local identifier spelling')
        for alias in e.get('id_aliases', []):
            identifier(alias, e, 'alias')
            if not CANONICAL.fullmatch(alias):
                error(e, 'invalid historical Canonical ID Alias grammar')
            elif alias.split('.')[-1].split('-', 1)[0] not in ({'CT1', 'CT2', 'CT3'} if kind == 'capability' else {LOCAL_PREFIX[kind]}):
                error(e, 'ID Alias terminal token contradicts Element Type/Tier')

    by_id = {e['canonical_id']: e for e in model['elements'] if e['canonical_id']}
    for e in model['elements']:
        if e['type'] == 'capability' and 'name_uniqueness' in e['unresolved']:
            peers = [p for p in model['elements'] if p['key'] != e['key']
                     and p['type'] == 'capability' and p['name'] == e['name']
                     and p.get('category') != e.get('category')
                     and 'name_uniqueness' in p['unresolved']]
            if len(peers) != 1:
                error(e, 'name conflict must reference an actual explicitly unresolved cross-category collision')
    feature_names = set()
    for e in model['elements']:
        seen = {e['key']}
        node = e
        # Owner and partial Capability parent traversal never supplies missing CT ancestry.
        while node.get('owner') or node.get('capability_parent'):
            parent_key = node.get('owner') or node.get('capability_parent')
            if parent_key in seen:
                error(e, 'responsibility ancestry cycle')
                break
            seen.add(parent_key)
            node = elements.get(parent_key)
            if not node:
                break
        if e['type'] == 'feature' and node and node.get('tier') == 'CT1':
            name_scope = (node['key'], e['name'])
            if name_scope in feature_names:
                error(e, 'duplicate Feature name within established CT1 hierarchy')
            feature_names.add(name_scope)
        elif e['type'] == 'feature':
            uncertainty.append({'key': e['key'], 'reason': 'feature_name_scope_root_unresolved'})
        canonical = e['canonical_id']
        if not canonical or not CANONICAL.fullmatch(canonical):
            continue
        parts = canonical.split('.')
        for i in range(1, len(parts)):
            ancestor = by_id.get('.'.join(parts[:i]))
            expected = 'feature' if parts[i - 1].startswith('FEAT-') else 'capability'
            if not ancestor or ancestor['type'] != expected or ancestor['status'] != 'established':
                error(e, f'missing established ancestor {".".join(parts[:i])}')
        if len(parts) > 1:
            context = elements.get(e.get('capability_parent') if e['type'] == 'capability' else e.get('owner'))
            if not context or context['canonical_id'] != '.'.join(parts[:-1]):
                error(e, 'Canonical ID does not express current owning responsibility context')

    for c in model['catalogue_contexts']:
        if c['container'] not in elements or elements[c['container']]['type'] != 'capability':
            error(c, 'unresolved catalogue container reference')
        if c['view'] not in views:
            error(c, 'unresolved contextual view reference')
        if c['element_status'] == 'unresolved':
            uncertainty.append({'key': c['key'], 'reason': 'architectural_element_identity_unresolved'})
            if c['element'] is not None:
                error(c, 'unresolved catalogue identity cannot assert an Architectural Element')
        elif c['element'] not in elements or elements[c['element']]['type'] != 'capability' or elements[c['element']]['name'] != c['name']:
            error(c, 'invalid independently established Capability reference')

    relation_keys = set()
    for r in model['relationships']:
        pair = (r['type'], r['from'], r['to'])
        if pair in relation_keys:
            errors.append(f'duplicate or contradictory relationship {pair}')
        relation_keys.add(pair)
        start, end = elements.get(r['from']), elements.get(r['to'])
        if not start or not end:
            errors.append(f'unresolved model reference in relationship {pair}')
            continue
        types = RELATION_TYPES[r['type']]
        if start['type'] not in types[0] or end['type'] not in types[1]:
            errors.append(f'invalid relationship endpoint types {pair}')
        if r['status'] == 'established' and r['type'] in ('realises_feature', 'associated_feature', 'contributes_feature'):
            if start.get('owner') not in (end['key'], end.get('owner')):
                errors.append(f'Feature relationship crosses owning responsibility context {pair}')
        if r['status'] == 'unresolved':
            if not r.get('reason'):
                errors.append(f'unresolved relationship requires architectural reason {pair}')
            uncertainty.append({'relationship': pair, 'reason': r.get('reason')})
        if r['status'] == 'established' and r['type'] in ('capability_decomposition', 'responsibility_parent'):
            if end.get('capability_parent') != start['key']:
                errors.append(f'decomposition/partial parent contradicts recorded parent {pair}')

    source_texts = {}
    if check_sources:
        for source, record in model['sources'].items():
            path = (root / record['path']).resolve()
            if not path.is_relative_to((root / 'docs/markdown').resolve()) or not path.is_file():
                errors.append(f'{source}: invalid or missing RADS source path')
                continue
            data = path.read_bytes()
            if hashlib.sha256(data).hexdigest() != record['sha256']:
                errors.append(f'{source}: source drift; reconcile the capture before updating its fingerprint')
            source_texts[source] = data.decode()
    for group in ('elements', 'views', 'catalogue_contexts', 'relationships'):
        for record in model[group]:
            for ref in record['sources']:
                if ref['source'] not in model['sources']:
                    errors.append(f'unknown evidence source {ref["source"]}')
                if ref['source'] in source_texts:
                    contents = source_texts[ref['source']]
                    source_lines = contents.splitlines()
                    if ref['line'] > len(source_lines) or source_lines[ref['line'] - 1] != ref['text']:
                        errors.append(f'{ref["source"]}:{ref["line"]}: evidence locator mismatch')
                    if ref['anchor'] and ref['anchor'] not in anchors(contents):
                        errors.append(f'{ref["source"]}: missing source anchor {ref["anchor"]}')
    return errors, uncertainty


def resolve(model, reference):
    matches = [e for e in model['elements'] if reference in
               [e['key'], e['name'], e['canonical_id'], e.get('local_id')]
               + e.get('name_aliases', []) + e.get('id_aliases', [])]
    if len(matches) != 1:
        raise ValueError(f'reference {reference!r} resolves to {len(matches)} elements; use a record key')
    return matches[0]


def impact(model, reference):
    """Return evidence-backed dependants; unresolved mentions are separate."""
    target = resolve(model, reference)
    affected = {target['key']}
    while True:
        before = set(affected)
        for e in model['elements']:
            if e.get('owner') in affected or e.get('capability_parent') in affected:
                affected.add(e['key'])
        for r in model['relationships']:
            if r['status'] == 'established' and r['to'] in affected:
                affected.add(r['from'])
        if affected == before:
            break
    return {
        'target': target['key'],
        'affected_elements': [e for e in model['elements'] if e['key'] in affected],
        'catalogue_contexts': [c for c in model['catalogue_contexts'] if c['container'] in affected],
        'unresolved_relationships': [r for r in model['relationships'] if r['status'] == 'unresolved'
                                     and (r['from'] in affected or r['to'] in affected)],
    }


def mermaid(model, view):
    selected = [v for v in model['views'] if view in (v['name'], v['key'])]
    if len(selected) != 1:
        raise ValueError(f'unknown or ambiguous contextual view {view!r}')
    nodes = {e['key']: e for e in model['elements'] if selected[0]['key'] in e.get('views', [])}
    ids = {key: f'n{i}' for i, key in enumerate(nodes)}
    output = ['flowchart LR', '  %% Reference candidate; unresolved identity is explicit.']
    for key, e in nodes.items():
        identity = e['canonical_id'] or f'{e.get("local_id", "")} Canonical ID unresolved'.strip()
        tier = e.get('tier') or ('CT unresolved' if e['type'] == 'capability' else e['type'])
        label = f'{e["name"]} | {tier} | {identity}'
        # Entity-encode punctuation that could alter Mermaid syntax.
        label = label.replace('&', '&amp;').replace('"', '&quot;').replace('<', '&lt;').replace('>', '&gt;')
        output.append(f'  {ids[key]}["{label}"]')
        if e.get('owner') in nodes:
            output.append(f'  {ids[e["owner"]]} -->|owns| {ids[key]}')
    for r in model['relationships']:
        if r['from'] in nodes and r['to'] in nodes:
            edge = '-.->' if r['status'] == 'unresolved' else '-->'
            label = ('UNRESOLVED: ' if r['status'] == 'unresolved' else '') + r['type']
            output.append(f'  {ids[r["from"]]} {edge}|{label}| {ids[r["to"]]}')
    return '\n'.join(output) + '\n'


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--model', type=Path, default=DEFAULT_MODEL)
    sub = parser.add_subparsers(dest='command', required=True)
    sub.add_parser('validate')
    sub.add_parser('summary')
    sub.add_parser('unresolved')
    listing = sub.add_parser('list')
    listing.add_argument('--type', choices=['capability', 'feature', 'function', 'service', 'process'])
    listing.add_argument('--category', choices=['business', 'business_enabling'])
    listing.add_argument('--view')
    sub.add_parser('contexts').add_argument('--view')
    sub.add_parser('show').add_argument('reference')
    sub.add_parser('impact').add_argument('reference')
    sub.add_parser('diagram').add_argument('view')
    args = parser.parse_args(argv)
    try:
        model = load_json(args.model)
        errors, uncertainty = validate(model)
        if errors:
            print(json.dumps({'result': 'INVALID', 'errors': errors}, indent=2))
            return 1
        if args.command == 'validate':
            output = {'result': 'PASS', 'invalid': 0,
                      'unresolved_by_architectural_decision': len(uncertainty)}
        elif args.command == 'summary':
            output = {
                'capture_status': model['capture_status'],
                'elements': dict(Counter(e['type'] for e in model['elements'])),
                'capabilities': dict(Counter(e['category'] for e in model['elements'] if e['type'] == 'capability')),
                'established_tiers': dict(Counter(e['tier'] for e in model['elements'] if e.get('tier'))),
                'established_canonical_ids': sum(e['canonical_id'] is not None for e in model['elements']),
                'established_local_identifiers': sum(bool(e.get('local_id')) for e in model['elements']),
                'unresolved_catalogue_element_identities': sum(c['element_status'] == 'unresolved' for c in model['catalogue_contexts']),
            }
        elif args.command == 'unresolved':
            output = uncertainty
        elif args.command in ('list', 'contexts'):
            view = None
            if args.view:
                matches = [v['key'] for v in model['views'] if args.view in (v['key'], v['name'])]
                if len(matches) != 1:
                    raise ValueError(f'unknown or ambiguous contextual view {args.view!r}')
                view = matches[0]
            if args.command == 'contexts':
                output = [c for c in model['catalogue_contexts'] if not view or c['view'] == view]
            else:
                output = [{k: e[k] for k in ('key', 'type', 'category', 'name', 'tier', 'canonical_id', 'local_id', 'owner', 'views', 'unresolved') if k in e}
                          for e in model['elements']
                          if (not args.type or e['type'] == args.type)
                          and (not args.category or e.get('category') == args.category)
                          and (not view or view in e.get('views', []))]
        elif args.command == 'show':
            output = resolve(model, args.reference)
        elif args.command == 'impact':
            output = impact(model, args.reference)
        else:
            print(mermaid(model, args.view), end='')
            return 0
        print(json.dumps(output, indent=2, ensure_ascii=False))
        return 0
    except (OSError, ValueError, KeyError) as exc:
        print(json.dumps({'result': 'INVALID', 'errors': [str(exc)]}))
        return 1


if __name__ == '__main__':
    sys.exit(main())
