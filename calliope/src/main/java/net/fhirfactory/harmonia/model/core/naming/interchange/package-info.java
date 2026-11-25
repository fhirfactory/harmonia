/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * Hardened, deterministic UTF-8 XML record format for Naming interchange.
 *
 * <p>Implementation package of the Naming component; not part of the supported
 * Naming API. It owns all XML concerns and works on neutral record values;
 * the supported {@code NamingXmlCodec} facade maps them to and from the models.
 * The hardened DOM parser is package-private. It depends only on
 * {@code naming.validation} and {@code naming.valuesets}.</p>
 */
package net.fhirfactory.harmonia.model.core.naming.interchange;
