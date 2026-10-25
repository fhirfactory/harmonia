# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only Exchange structure and raw transport fidelity; no baseline discovery."""
import argparse
from collections import Counter, defaultdict
import gzip
import hashlib
import json
from pathlib import Path
import re
import exchange_xml as g
BASE = g.BASE

def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()

ROLES = ("canonicalId", "displayName", "friendlyName", "canonicalName", "archimateType", "harmoniaType")
EQUIVALENT = "SEMANTICALLY EQUIVALENT NORMALISATION"
PRESENTATION = "PRESENTATION-PRESERVING TRANSFORMATION"
LOSS = "INFORMATION LOSS"
SEMANTIC = "SEMANTIC DIFFERENCE"
UNKNOWN = "UNEXPLAINED DIFFERENCE"
CATEGORIES = (EQUIVALENT, PRESENTATION, LOSS, SEMANTIC, UNKNOWN)
LANG = '{http://www.w3.org/XML/1998/namespace}lang'
# Exact keys written without language by architecture_continuum.project(). This
# is deliberately not a wildcard for arbitrary authored properties or languages.
PROJECTED_KEYS = frozenset((*ROLES, 'Specialization',
    'Continuum / Canonical Source', 'Continuum / Semantic Standing',
    'Continuum / Currentness', 'Continuum / Navigation Number',
    'Continuum / Navigation Path', 'Continuum / Navigation References'))


def english_projection_value(a, b, before, after):
    """Only the demonstrated absent → en transport change on a generated scalar."""
    if a.tag != g.tag('value') or b.tag != a.tag or len(a) or len(b):
        return False
    pa, pb = a.getparent(), b.getparent()
    if pa is None or pb is None or pa.tag != g.tag('property') or pb.tag != pa.tag:
        return False
    key = before.keys.get(pa.get('propertyDefinitionRef'))
    if key not in PROJECTED_KEYS or after.keys.get(pb.get('propertyDefinitionRef')) != key:
        return False
    if len(pa.findall('a:value', g.N)) != 1 or len(pb.findall('a:value', g.N)) != 1:
        return False
    # These keys are generated on semantic elements, not on Views/model metadata.
    owners = [p.getparent() for p in (pa, pb)]
    if any(p is None or p.getparent() is None or p.getparent().tag != g.tag('element') for p in owners):
        return False
    inherited = [n.get(LANG) for v in (a, b) for n in v.iterancestors() if n.get(LANG) is not None]
    if any(lang != 'en' for lang in inherited):
        return False
    return (a.get(LANG) is None and b.get(LANG) == 'en'
            and (a.text or '') == (b.text or '')
            and dict(a.attrib) == {k: v for k, v in b.attrib.items() if k != LANG})


def local(tag):
    return tag.removeprefix("{" + g.NS + "}")



def children(node):
    return [c for c in node if isinstance(c.tag, str)]



def expanded_type(node):
    value = node.get(g.TYPE)
    if value is None:
        return None
    prefix, name = value.split(":", 1) if ":" in value else (None, value)
    namespace = node.nsmap.get(prefix)
    return "{" + namespace + "}" + name if namespace else value



def values(node, name):
    # Preserve language, whitespace, multiplicity and attributes. Never strip UUIDs.
    return [{"text": c.text or "", "attributes": dict(sorted(c.attrib.items()))}
            for c in node.findall("a:" + name, g.N)]



class Exchange:
    """Shared Property resolver over the existing secure parser and offline XSDs.

    Index all native types and all claims using explicit inputs only.
    """
    def __init__(self, path):
        self.path = Path(path)
        self.root = g.parse(path)
        self.sha256 = sha(path)
        self.xml_sha256 = (hashlib.sha256(gzip.decompress(self.path.read_bytes())).hexdigest()
                           if self.path.suffix == '.gz' else self.sha256)
        self.issues = []
        try:
            g.validate_identifiers(self.root)
        except ValueError as exc:
            self.issues.append(str(exc))
        schema = g.load_schema(BASE / "schemas/archimate3_Diagram.xsd")
        if not schema.validate(self.root):
            self.issues.extend("XSD: " + e.message for e in schema.error_log)
        self.elements = self.index("a:elements/a:element")
        self.relationships = self.index("a:relationships/a:relationship")
        self.views = self.index("a:views/a:diagrams/a:view")
        self.definitions = self.index("a:propertyDefinitions/a:propertyDefinition")
        by_name = defaultdict(list)
        for identifier, definition in self.definitions.items():
            names = definition.findall("a:name", g.N)
            if len(names) == 1 and names[0].text:
                by_name[names[0].text].append(identifier)
            else:
                self.issues.append("Ambiguous/empty Property definition name: " + str(identifier))
        self.keys = {ids[0]: key for key, ids in by_name.items() if len(ids) == 1}
        self.definition_ids = {key: identifier for identifier, key in self.keys.items()}
        for key, ids in sorted(by_name.items()):
            if len(ids) != 1:
                self.issues.append("Duplicate Property definition name: " + repr(key))
        for prop in self.root.findall(".//a:property", g.N):
            if prop.get("propertyDefinitionRef") not in self.keys:
                self.issues.append("Unresolved/ambiguous Property reference: " + str(prop.get("propertyDefinitionRef")))
        self.check_references()
        self.issues = sorted(set(self.issues))

    def index(self, path):
        result = {}
        for position, element in enumerate(self.root.findall(path, g.N)):
            key = element.get("identifier")
            if not key or key in result:
                # This is a diagnostic locator, never an allocated identity.
                # Invalid input remains unresolved; preserve every record.
                key = f"(unresolved identifier at {path} position {position})"
            result[key] = element
        return result

    def claims(self, owner, key):
        return [p for p in owner.findall("a:properties/a:property", g.N)
                if self.keys.get(p.get("propertyDefinitionRef")) == key]

    def texts(self, owner, key):
        return [v.text or "" for p in self.claims(owner, key) for v in p.findall("a:value", g.N)]

    def single(self, owner, key):
        claims = self.claims(owner, key)
        texts = self.texts(owner, key)
        return texts[0] if len(claims) == len(texts) == 1 else None

    def check_references(self):
        all_ids = {e.get("identifier") for e in self.root.iter() if e.get("identifier")}
        tables = {"elementRef": self.elements, "relationshipRef": self.relationships,
                  "viewRef": self.views, "identifierRef": all_ids}
        for node in self.root.iter():
            for attr, table in tables.items():
                if attr in node.attrib and node.get(attr) not in table:
                    self.issues.append("Unresolved " + attr + ": " + node.get(attr))
        semantic_ids = self.elements.keys() | self.relationships.keys()
        for identifier, rel in self.relationships.items():
            if any(rel.get(k) not in semantic_ids for k in ("source", "target")):
                self.issues.append("Unresolved relationship endpoint: " + str(identifier))
        for vid, view in self.views.items():
            nodes = {n.get("identifier"): n for n in view.findall(".//a:node", g.N)}
            connections = {n.get("identifier"): n for n in view.findall(".//a:connection", g.N)}
            for cid, connection in connections.items():
                endpoints = [connection.get(k) for k in ("source", "target")]
                if any(e not in nodes.keys() | connections.keys() for e in endpoints):
                    self.issues.append("Unresolved visual endpoint: " + str(vid) + "/" + str(cid))
                rel = self.relationships.get(connection.get("relationshipRef"))
                if rel is not None and all(e in nodes for e in endpoints):
                    refs = [nodes[e].get("elementRef") for e in endpoints]
                    if all(refs) and refs != [rel.get("source"), rel.get("target")]:
                        self.issues.append("Visual/semantic endpoint mismatch: " + str(cid))

    def metrics(self):
        rows = [{"identifier": key, "name": values(view, "name"),
                 "elements": len(view.findall(".//a:node[@elementRef]", g.N)),
                 "visual_objects": len(view.findall(".//a:node", g.N)),
                 "connections": len(view.findall(".//a:connection", g.N))}
                for key, view in self.views.items()]
        largest = sorted(rows, key=lambda r: (-r["visual_objects"], -r["connections"], r["identifier"]))
        return dict(semantic_elements=len(self.elements), relationships=len(self.relationships),
                    views=len(self.views), visual_objects=sum(r["visual_objects"] for r in rows),
                    visual_connections=sum(r["connections"] for r in rows),
                    largest_view=largest[0] if largest else None,
                    xml_bytes=self.path.stat().st_size, xml_decimal_mb=self.path.stat().st_size / 1_000_000)



def compare(before, after, *, normalised=False):
    """Exhaustive XML probe; match existing representation IDs, never names.

    Only Property-definition *keys* use exact names (not architectural identity).
    Unordered keyed semantic collections can reorder. Visual stacking, routing,
    languages, text and anonymous organisation order cannot silently reorder.
    """
    findings = []
    common_keys = before.definition_ids.keys() & after.definition_ids.keys()

    def add(category, path, a, b, reason):
        findings.append(dict(classification=category, path=path, before=a, after=b, reason=reason))

    def attrs(node, doc):
        result = dict(node.attrib)
        if g.TYPE in result:
            result[g.TYPE] = expanded_type(node)
        ref = result.get("propertyDefinitionRef")
        if ref in doc.keys and doc.keys[ref] in common_keys:
            result["propertyDefinitionRef"] = "Property key: " + doc.keys[ref]
        return result

    def keyed(parent, doc):
        counts, result = Counter(), {}
        for child in children(parent):
            tag = local(child.tag)
            if child.tag == g.tag("propertyDefinition") and doc.keys.get(child.get("identifier")) in common_keys:
                key = tag + "[key=" + doc.keys[child.get("identifier")] + "]"
            elif child.tag == g.tag("property") and doc.keys.get(child.get("propertyDefinitionRef")) in common_keys:
                key = tag + "[key=" + doc.keys[child.get("propertyDefinitionRef")] + "]"
            elif child.tag == g.tag("item"):
                # Anonymous navigation after Archi export has no stable identity.
                # Pair strictly by position only for this no-edit probe, retaining
                # every label, reference, order and lost identifier as evidence.
                counts[tag] += 1
                key = tag + "[position=" + str(counts[tag]) + "]"
            elif child.get("identifier") is not None:
                key = tag + "[id=" + child.get("identifier") + "]"
            else:
                counts[tag] += 1
                key = tag + "[" + str(counts[tag]) + "]"
            # Preserve duplicate claims instead of dict overwrite.
            if key in result:
                counts[key] += 1
                key += "[duplicate=" + str(counts[key]) + "]"
            result[key] = child
        return result

    def content_text(node):
        # XML element-only indentation is syntax. Leaf strings, even all-space
        # values, are data. Non-whitespace mixed content remains observable.
        if children(node) and not (node.text or "").strip():
            return None
        return node.text

    def walk(a, b, path):
        if normalised and a.tag == g.tag('organizations'):
            # Reviewed separately by View identity/tree. Derived navigation is
            # regenerated by the existing governed projector after incorporation.
            return
        aa, ba = attrs(a, before), attrs(b, after)
        if a.tag == g.tag("propertyDefinition") and before.keys.get(a.get("identifier")) in common_keys:
            old, new = aa.pop("identifier", None), ba.pop("identifier", None)
            if old != new:
                # Rekeying alone is equivalent. Metadata below is still compared.
                add(EQUIVALENT, path + "/@identifier", old, new, "Exact unique Property key resolves both definition IDs; all other definition content and owner values compared independently")
        for key in sorted(aa.keys() | ba.keys()):
            if aa.get(key) == ba.get(key):
                continue
            if normalised and key == LANG and english_projection_value(a, b, before, after):
                category, reason = EQUIVALENT, 'Generated scalar: unchanged text/attributes; absent xml:lang becomes en'
            elif key not in ba:
                category, reason = LOSS, "Attribute absent after transport"
            elif key == "identifier" and a.tag == g.tag("item"):
                category, reason = UNKNOWN, "Organisation item identity transformed; paths do not establish identity"
            elif "/element[id=" in path or "/relationship[id=" in path:
                category, reason = SEMANTIC, "Semantic record attribute changed"
            else:
                category, reason = UNKNOWN, "No proven representational equivalence for this attribute"
            add(category, path + "/@" + key, aa.get(key), ba.get(key), reason)
        at, bt = content_text(a), content_text(b)
        if at != bt:
            category = LOSS if at is not None and bt is None else SEMANTIC if "/element[id=" in path or "/relationship[id=" in path else UNKNOWN
            add(category, path + "/text()", at, bt, "Exact text compared; whitespace/language selection is not normalised")
        ac, bc = keyed(a, before), keyed(b, after)
        if list(ac) != list(bc):
            safe = a.tag in {g.tag(n) for n in ("elements", "relationships", "diagrams", "propertyDefinitions", "properties")}
            safe = safe and ac.keys() == bc.keys() and not any("duplicate=" in k for k in ac)
            population = normalised and a.tag in {g.tag(n) for n in ('elements', 'relationships', 'diagrams')}
            if not population:
                add(EQUIVALENT if safe else UNKNOWN, path + "/child-order", list(ac), list(bc),
                    "Uniquely keyed unordered collection" if safe else "Order retained as evidence: may affect stacking, routing, languages or anonymous navigation")
        for key in sorted(ac.keys() | bc.keys()):
            subpath = path + "/" + key
            if key not in bc:
                add(LOSS, subpath, g.signature(ac[key]), None, "Subtree absent after transport; raw input retained by hash")
            elif key not in ac:
                add(UNKNOWN, subpath, None, g.signature(bc[key]), "Additional subtree with no established equivalence")
            else:
                walk(ac[key], bc[key], subpath)
                tail_a, tail_b = ac[key].tail, bc[key].tail
                if (tail_a or "").strip() or (tail_b or "").strip():
                    if tail_a != tail_b:
                        add(UNKNOWN, subpath + "/tail()", tail_a, tail_b, "Mixed-content tail changed")

    walk(before.root, after.root, "/model")
    counts = Counter(f["classification"] for f in findings)
    issues = {"published": before.issues, "incoming": after.issues}
    status = "UNRESOLVED" if any(issues.values()) else "DIFFERENCES" if any(counts[k] for k in (LOSS, SEMANTIC, UNKNOWN)) else "EQUIVALENT"

    changed_records = defaultdict(set)
    record_pattern = re.compile(r'/((?:element|relationship|view))\[id=([^\]]+)\]')
    material_paths = [f['path'] for f in findings if f['classification'] != EQUIVALENT]
    for path in material_paths:
        for prefix, identifier in record_pattern.findall(path):
            changed_records[prefix].add(identifier)

    def record_evidence(table_a, table_b, prefix):
        common = table_a.keys() & table_b.keys()
        changed = sorted(key for key in common if key in changed_records[prefix] or
                         (']' in key and any(f'/{prefix}[id={key}]' in path for path in material_paths)))
        return dict(before=len(table_a), after=len(table_b), retained_ids=len(table_a.keys() & table_b.keys()),
                    missing_ids=sorted(table_a.keys() - table_b.keys()), additional_ids=sorted(table_b.keys() - table_a.keys()),
                    changed_records=changed)

    presentation = record_evidence(before.views, after.views, "view")
    for tag in ("node", "connection"):
        left = {(vid, n.get("identifier")) for vid, v in before.views.items() for n in v.findall(".//a:" + tag, g.N)}
        right = {(vid, n.get("identifier")) for vid, v in after.views.items() for n in v.findall(".//a:" + tag, g.N)}
        presentation[tag + "_identity"] = dict(before=len(left), after=len(right), retained=len(left & right),
                                                missing=sorted(left - right), additional=sorted(right - left))
    presentation["coverage"] = "View-local visual IDs, parent/nesting, membership, coordinates, dimensions, ordered bendpoints, child order, style, labels and residual XML; no presentation replay or speculative equivalence rules"
    contract = {}
    for role in ROLES:
        observed, preserved, changed = 0, 0, []
        for identifier, owner in sorted(before.elements.items()):
            claims = before.claims(owner, role)
            if not claims:
                continue
            observed += 1
            target = after.elements.get(identifier)
            left = [values(p, "value") for p in claims]
            right = [] if target is None else [values(p, "value") for p in after.claims(target, role)]
            if left == right:
                preserved += 1
            else:
                changed.append(dict(element=identifier, before=left, after=right))
        contract[role] = dict(published_owners=observed, exact_value_language_multiplicity_owner_preserved=preserved, changes=changed)
    return dict(comparison_status=status, purpose="Raw representation fidelity; use continuum_workbench for canonical Change Review; no architecture acceptance",
                published_sha256=before.sha256, incoming_sha256=after.sha256, input_issues=issues,
                classifications={k: counts[k] for k in CATEGORIES}, metrics={"published": before.metrics(), "incoming": after.metrics()},
                elements=record_evidence(before.elements, after.elements, "element"),
                relationships=record_evidence(before.relationships, after.relationships, "relationship"),
                presentation=presentation, contract_properties=contract, findings=findings,
                empirical_archi_fidelity="UNRESOLVED: comparator alone does not authenticate an Archi execution; use the hash-pinned evidence procedure")


def view_navigation(model):
    """Keep authored View trees, including empty folders, metadata and ordering.

    Folder paths are scoped comparison locators, never canonical identity. A
    duplicate path/reference is an exception rather than a selected winner.
    Non-View siblings cannot affect the order of retained View children.
    """
    from exchange_signatures import signature
    folders, placements, orders, issues = {}, {}, {}, []
    org = model.root.find('a:organizations', g.N)
    if org is None:
        return dict(folders=folders, placements=placements, orders=orders, issues=issues)

    def relevant(item, in_views=False):
        return (in_views or item.findtext('a:label', namespaces=g.N) == 'Views'
                or item.get('identifierRef') in model.views
                or any(relevant(c) for c in item.findall('a:item', g.N)))

    def walk(parent, path=(), in_views=False):
        order = []
        for item in parent.findall('a:item', g.N):
            authored = in_views or item.findtext('a:label', namespaces=g.N) == 'Views'
            if not relevant(item, authored):
                continue
            ref = item.get('identifierRef')
            labels = values(item, 'label')
            label = ' | '.join(v['text'] for v in labels) if labels else '<unlabelled>'
            location = path + (label,)
            # Label-less references get an identity locator, not a fake folder.
            key = json.dumps(location if not ref else path + ('ref:' + ref,), ensure_ascii=False)
            metadata = dict(attributes=dict(item.attrib),
                            content=[signature(c, model) for c in children(item) if c.tag != g.tag('item')])
            if ref in model.views:
                if ref in placements:
                    issues.append('Duplicate View navigation reference: ' + ref)
                placements.setdefault(ref, []).append(dict(path=list(path), metadata=metadata))
                order.append('view:' + ref)
            else:
                if ref:
                    issues.append('Non-View reference inside retained View hierarchy: ' + ref)
                if key in folders:
                    issues.append('Ambiguous repeated View folder path: ' + key)
                folders[key] = metadata
                order.append('folder:' + key)
            if item.findall('a:item', g.N):
                walk(item, location if not ref else path + ('@' + ref,), authored)
        orders[json.dumps(path, ensure_ascii=False)] = order

    walk(org)
    return dict(folders=folders, placements=placements, orders=orders, issues=issues)


def navigation_delta(before, after):
    a, b = view_navigation(before), view_navigation(after)
    moves = []
    for vid in sorted(a['placements'].keys() & b['placements'].keys()):
        if a['placements'][vid] != b['placements'][vid]:
            moves.append(dict(identifier=vid, name=values(after.views[vid], 'name'),
                              before=a['placements'][vid], submitted=b['placements'][vid]))
    folders = [dict(path=key, before=a['folders'].get(key), submitted=b['folders'].get(key))
               for key in sorted(a['folders'].keys() | b['folders'].keys())
               if a['folders'].get(key) != b['folders'].get(key)]
    orders = []
    for key in sorted(a['orders'].keys() & b['orders'].keys()):
        left, right = a['orders'][key], b['orders'][key]
        common = set(left) & set(right)
        if [x for x in left if x in common] != [x for x in right if x in common]:
            orders.append(dict(parent=key, before=left, submitted=right))
    return dict(changedViews=moves, folderChanges=folders, orderChanges=orders,
                addedViewPlacements={k: b['placements'].get(k, []) for k in sorted(after.views.keys() - before.views.keys())},
                removedViewPlacements={k: a['placements'].get(k, []) for k in sorted(before.views.keys() - after.views.keys())},
                issues=a['issues'] + b['issues'])


def architectural_delta(before, after):
    """Bounded review input. No canonical writes, allocation or DRP IO."""
    from element_correlation import correlate
    from exchange_signatures import native_type
    report = compare(before, after, normalised=True)
    correlation = correlate(before, after)
    nav = navigation_delta(before, after)
    material = [f for f in report['findings'] if f['classification'] != EQUIVALENT]
    normalisations = Counter(f['reason'] for f in report['findings'] if f['classification'] == EQUIVALENT)
    rebindings = [dict(key=before.keys[f['before']], before=f['before'], submitted=f['after'])
                  for f in report['findings'] if f['classification'] == EQUIVALENT
                  and '/propertyDefinition[key=' in f['path'] and f['path'].endswith('/@identifier')]

    def describe(node, model, kind):
        row = dict(identifier=node.get('identifier'), archimateType=native_type(node),
                   attributes=dict(node.attrib),
                   name=values(node, 'name'), documentation=values(node, 'documentation'))
        props = []
        for p in node.findall('a:properties/a:property', g.N):
            props.append(dict(key=model.keys.get(p.get('propertyDefinitionRef'), 'UNRESOLVED'), values=values(p, 'value')))
        if props:
            row['properties'] = props
        if kind == 'relationship':
            row.update(source=node.get('source'), target=node.get('target'))
        if kind == 'view':
            row.update(nodes=len(node.findall('.//a:node', g.N)),
                       connections=len(node.findall('.//a:connection', g.N)),
                       elementRefs=sorted({n.get('elementRef') for n in node.findall('.//a:node[@elementRef]', g.N)}),
                       relationshipRefs=sorted({n.get('relationshipRef') for n in node.findall('.//a:connection[@relationshipRef]', g.N)}))
        return row

    groups = {}
    for kind, left, right, observation in (
        ('element', before.elements, after.elements, report['elements']),
        ('relationship', before.relationships, after.relationships, report['relationships']),
        ('view', before.views, after.views, report['presentation'])):
        changed = []
        for rid in observation['changed_records']:
            prefix = '/' + kind + '[id=' + rid + ']'
            changed.append(dict(identifier=rid, name=values(right[rid], 'name'),
                                differences=[f for f in material if prefix in f['path']]))
        groups[kind + 's'] = dict(before=len(left), submitted=len(right), retainedIds=observation['retained_ids'],
            added=[describe(right[k], after, kind) for k in observation['additional_ids']],
            removed=[describe(left[k], before, kind) for k in observation['missing_ids']], changed=changed)
    record_pattern = re.compile(r'/(?:element|relationship|view)\[id=')
    anomalies = [f for f in material if not record_pattern.search(f['path'])]
    conditions = [dict(kind='INPUT_VALIDATION', snapshot=side, detail=issue)
                  for side, issues in report['input_issues'].items() for issue in issues]
    conditions += [dict(kind='IDENTITY', detail=issue) for issue in correlation['issues']]
    if any(correlation['unresolved'].values()):
        conditions.append(dict(kind='IDENTITY', detail=correlation['unresolved']))
    if correlation['representation_rebindings']:
        conditions.append(dict(kind='REPRESENTATION_REBINDING',
                               detail='Canonical continuity is recorded; this review adapter does not adjudicate representation rebinding.'))
    conditions += [dict(kind='VIEW_NAVIGATION', detail=issue) for issue in nav.pop('issues')]
    if anomalies:
        conditions.append(dict(kind='UNCLASSIFIED_REPRESENTATION', count=len(anomalies)))
    new_without_id = [k for k in correlation['new'] if after.single(after.elements[k], 'canonicalId') is None]

    def org_counts(model):
        items = model.root.findall('a:organizations//a:item', g.N)
        return dict(items=len(items), identifiers=sum(n.get('identifier') is not None for n in items),
                    references=sum(n.get('identifierRef') is not None for n in items))

    seeds = {r.get(k) for action in ('added', 'removed') for r in groups['relationships'][action]
             for k in ('source', 'target') if r.get(k) in before.elements}
    for row in groups['relationships']['changed']:
        for model in (before, after):
            rel = model.relationships[row['identifier']]
            seeds.update(rel.get(k) for k in ('source', 'target') if rel.get(k) in before.elements)

    return dict(schemaVersion=2, standing='UNREVIEWED; no application authority',
        outcome='UNRESOLVED' if conditions else 'REVIEW_READY',
        inputs={side: dict(path=str(model.path), sha256=model.sha256, xmlSha256=model.xml_sha256,
                           bytes=model.path.stat().st_size) for side, model in (('baseline', before), ('submitted', after))},
        **groups, viewNavigation=nav,
        classified=dict(normalisations=dict(normalisations), propertyDefinitionRebindings=rebindings,
                        nonViewNavigation='DERIVED: regenerate with existing Continuum projector after governed incorporation; no architectural CR',
                        organizationInventory=dict(baseline=org_counts(before), submitted=org_counts(after))),
        identity=dict(newWithoutCanonicalId=new_without_id,
                      allocation='Deferred to governed incorporation after adjudication',
                      rebindings=correlation['representation_rebindings']),
        representationAnomalies=anomalies, exceptionalConditions=conditions,
        contextPolicy='Known concept → vicinity search → bounded Continuum artefacts → references; DRP only for a specifically relevant detected change; no corpus scan',
        contextSeeds=sorted(seeds),
        canonicalIdsAllocated=0, canonicalSourcesChanged=0,
        nextAction='Targeted architectural interpretation; change-review.md; pending HCRL; STOP for Solution Architect. No autonomous investigation/reviewer loop.')


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('command', choices=['compare'])
    parser.add_argument('--baseline', type=Path, required=True)
    parser.add_argument('--submitted', type=Path, required=True)
    parser.add_argument('--report', type=Path, required=True)
    args = parser.parse_args()
    report = compare(Exchange(args.baseline), Exchange(args.submitted))
    args.report.parent.mkdir(parents=True, exist_ok=True)
    args.report.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    print(report['comparison_status'])
    return 1 if report['comparison_status'] == 'UNRESOLVED' else 0

if __name__ == '__main__':
    raise SystemExit(main())
