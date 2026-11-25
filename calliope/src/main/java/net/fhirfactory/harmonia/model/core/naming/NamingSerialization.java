/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.io.InvalidObjectException;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;

/**
 * Shared checks for the serialization proxies of the Naming models.
 *
 * <p>Topic, TopicEntry, TopicMask and TopicMaskEntry serialize through small
 * proxy objects that carry only raw text fields. On deserialization each proxy
 * rebuilds its model through the same strict construction path used by the
 * builders and the canonical and XML readers, so a crafted or corrupted
 * stream can never yield an invalid or partially constructed model. The models
 * themselves reject any stream that bypasses their proxy.</p>
 *
 * <p>Failures are reported as {@link InvalidObjectException} with a message
 * that names only the model type; no field text is echoed.</p>
 */
final class NamingSerialization {

    /** Prevents construction of this stateless helper. */
    private NamingSerialization() {
        throw new AssertionError("No instances");
    }

    /**
     * Checks that parallel field arrays are present, of equal length and within the depth bound.
     *
     * @param type model type, used only in the failure message
     * @param first first field array
     * @param others remaining field arrays, which must match the first array's length
     * @return the common nonempty length
     * @throws InvalidObjectException when an array is absent, empty, too long or of a different length
     */
    static int requireParallel(Class<?> type, Object[] first, Object[]... others) throws InvalidObjectException {
        if (first == null || first.length == 0 || first.length > NamingLimits.MAX_DEPTH) {
            throw invalid(type);
        }
        for (Object[] other : others) {
            if (other == null || other.length != first.length) {
                throw invalid(type);
            }
        }
        return first.length;
    }

    /**what
     * Creates the payload-free deserialization failure for one model type.
     *
     * @param type model type
     * @return an exception naming only the type
     */
    static InvalidObjectException invalid(Class<?> type) {
        return new InvalidObjectException("Invalid serialized " + type.getSimpleName());
    }
}
