#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Generate small synthetic organisation probes; never reads/writes publication.

All identifiers here are diagnostic representation identifiers, not Continuum IDs.
The folder-plus-leaf cases test a candidate only and supply no application authority.
"""
from pathlib import Path
import xml.etree.ElementTree as E

NS = 'http://www.opengroup.org/xsd/archimate/3.0/'
X = '{http://www.w3.org/2001/XMLSchema-instance}'
E.register_namespace('', NS)
E.register_namespace('xsi', X[1:-1])


def tag(s):
    return '{' + NS + '}' + s


def model(name):
    r = E.Element(tag('model'), identifier='diagnostic-' + name)
    E.SubElement(r, tag('name')).text = 'Diagnostic only — ' + name
    return r, E.SubElement(r, tag('elements')), E.SubElement(r, tag('organizations'))


def element(es, identifier, native, name):
    e = E.SubElement(es, tag('element'), {'identifier': identifier, X + 'type': native})
    E.SubElement(e, tag('name')).text = name
    return identifier


def item(parent, name=None, ref=None, note=None):
    attrs = {'identifierRef': ref} if ref else {}
    x = E.SubElement(parent, tag('item'), attrs)
    if name:
        E.SubElement(x, tag('label')).text = name
    if note:
        E.SubElement(x, tag('documentation')).text = note
    return x


def write(r, name):
    E.indent(r)
    (Path(__file__).parent / 'fixtures' / name).write_bytes(E.tostring(r, encoding='utf-8', xml_declaration=True))


def main():
    (Path(__file__).parent / 'fixtures').mkdir(exist_ok=True)
    r, es, org = model('organisation-mechanics')
    suite = item(org, 'Diagnostic cases')
    simple = element(es, 'simple', 'Capability', 'Simple canonical leaf')
    item(item(suite, '1 Simple folder'), ref=simple)

    parent = element(es, 'parent', 'Capability', 'Parent with children')
    child = element(es, 'child', 'Capability', 'Child leaf')
    item(item(item(suite, '2 Combined parent item'), 'Parent folder label', parent), ref=child)

    cap = element(es, 'context-cap', 'Capability', 'Canonical contextual Capability')
    fun = element(es, 'context-function', 'BusinessFunction', 'Contextual behaviour')
    item(item(suite, '3 Canonical primary location'), ref=cap)
    ct1 = item(item(suite, '3 Business context'), 'CT1 context', note='Reference to context-cap; not identifierRef')
    item(item(ct1, 'CT2 context'), ref=fun)

    c1 = element(es, 'ct1', 'Capability', '[CT1] Diagnostic parent')
    c2 = element(es, 'ct2', 'Capability', '[CT2] Diagnostic child')
    c3 = element(es, 'ct3', 'Capability', '[CT3] Diagnostic leaf')
    top = item(item(suite, '4 Nested Strategy'), 'CT1 parent', c1)
    item(item(top, 'CT2 parent', c2), ref=c3)

    care = element(es, 'care', 'DataObject', '[Topic] Diagnostic Care')
    encounter = element(es, 'encounter', 'DataObject', '[Topic] Diagnostic Encounter')
    item(item(item(suite, '5 Nested Application'), 'Care parent', care), ref=encounter)

    a1 = element(es, 'a-ct1', 'Capability', '[CT1] Folder-plus-element parent')
    a2 = element(es, 'a-ct2', 'Capability', '[CT2] Folder-plus-element child')
    a3 = element(es, 'a-ct3', 'Capability', '[CT3] Folder-plus-element leaf')
    top = item(item(suite, '6 Candidate A Strategy'), 'CT1 folder')
    item(top, ref=a1)
    sub = item(top, 'CT2 folder')
    item(sub, ref=a2)
    item(sub, ref=a3)
    ac = element(es, 'a-care', 'DataObject', '[Topic] Folder-plus-element Care')
    ae = element(es, 'a-encounter', 'DataObject', '[Topic] Folder-plus-element Encounter')
    top = item(item(suite, '6 Candidate A Application'), 'Care folder')
    item(top, ref=ac)
    item(top, ref=ae)

    product = element(es, 'product', 'Product', 'Diagnostic Product')
    stream = element(es, 'stream', 'ValueStream', 'Diagnostic Value Stream')
    top = item(suite, '7 Logical Business mixed types')
    item(top, ref=product)
    item(top, ref=stream)
    item(suite, '8 Source-only reference', note='No referenced model object')

    driver = element(es, 'driver', 'Driver', 'Driver with reference-only children')
    top = item(item(suite, '9 Motivation'), 'Driver navigation', driver)
    item(top, 'Stakeholder reference context', note='Reference only; no model object')
    write(r, 'mechanics.xml')

    r, es, org = model('repeated-reference')
    cap = element(es, 'repeated', 'Capability', 'One Capability referenced twice')
    item(item(org, 'First location'), ref=cap)
    item(item(org, 'Second location'), ref=cap)
    write(r, 'repeated-reference.xml')

    # Exact native category labels are consumed as markers. Numbered ones are not.
    r, es, org = model('category-markers')
    cap = element(es, 'marker-cap', 'Capability', 'Marker Capability')
    fun = element(es, 'marker-fun', 'BusinessFunction', 'Marker Business Function')
    top = item(org, 'Strategy')
    item(item(top, 'Within marker'), ref=cap)
    item(item(top, 'Mixed marker'), ref=fun)
    item(org, 'Technology & Physical', note='Explicit empty native marker')
    item(org, 'V Technology', note='Numbered empty navigation, no native marker')
    write(r, 'category-markers.xml')

    # A native root marker can anchor reference-only folders without inventing
    # model objects or inserting a second live reference to the context target.
    r, es, org = model('anchored-reference-folders')
    driver = element(es, 'anchored-driver', 'Driver', 'Anchored Driver')
    stakeholder = element(es, 'anchored-stakeholder', 'Stakeholder', 'Indexed Stakeholder')
    motivation = item(org, 'Motivation')
    context = item(item(motivation, 'I Motivation'), 'Driver navigation')
    item(context, ref=driver)
    group = item(context, 'Stakeholder references')
    item(group, 'Stakeholder context', note='Canonical target: anchored-stakeholder; reference only')
    item(item(motivation, 'R Stakeholder index'), ref=stakeholder)
    other = item(org, 'Other')
    item(item(other, 'G Governance reference'), 'Source-only leaf', note='Diagnostic source locator only')
    write(r, 'anchored-reference-folders.xml')


if __name__ == '__main__':
    main()
