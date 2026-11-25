/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * Supported Naming API: the immutable Topic and TopicMask models, their
 * dependent entry values, builders, XML codec facade and validation exception.
 *
 * <p>The Naming component is organised by function. This package and
 * {@code naming.valuesets} form the supported contract. The implementation
 * packages below carry the mechanics and are not part of the supported API,
 * even though Java requires their collaborating types to be public:</p>
 *
 * <ul>
 *   <li>{@code naming.validation} - field rules, resource limits and the
 *       payload-free rule violation;</li>
 *   <li>{@code naming.canonical} - strict canonical-text scanning and decoding;</li>
 *   <li>{@code naming.matching} - wildcard and version descriptions, compiled
 *       criteria, the evaluator and optional diagnostics;</li>
 *   <li>{@code naming.interchange} - the hardened, deterministic XML record format.</li>
 * </ul>
 *
 * <p>Dependencies point one way: implementation packages depend only on
 * {@code validation} and {@code valuesets} and never on these models. The
 * models alone assemble completed entries, so every construction route -
 * builders, canonical text and XML - applies the same strict validation with
 * the actual operation and entry index. A package-private boundary translates
 * implementation rule violations into {@link NamingValidationException}.</p>
 *
 * <p>Principal values: {@link Topic} (identity) and {@link TopicMask}
 * (selector). Dependent values: {@link TopicEntry} and {@link TopicMaskEntry},
 * created only through their owning model or builder. Builders:
 * {@link TopicBuilder} and {@link TopicMaskBuilder}. Adapter:
 * {@link NamingXmlCodec}.</p>
 *
 * <p>The four models and the validation exception are serializable. The
 * models serialize through validating proxies that rebuild them via the same
 * strict construction path, so deserialization cannot bypass validation.</p>
 */
package net.fhirfactory.harmonia.model.core.naming;
