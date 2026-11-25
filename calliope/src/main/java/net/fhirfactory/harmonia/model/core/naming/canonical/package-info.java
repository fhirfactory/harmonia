/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * Strict scanning, decoding and escaping of the Topic and TopicMask canonical text forms.
 *
 * <p>Implementation package of the Naming component; not part of the supported
 * Naming API. The parsers enforce size and depth bounds before allocation,
 * decode exactly one escape layer and return neutral decoded fields; they never
 * construct models. It depends only on {@code naming.validation} and
 * {@code naming.valuesets}.</p>
 */
package net.fhirfactory.harmonia.model.core.naming.canonical;
