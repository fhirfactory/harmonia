/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Factory for creating and obtaining {@link PhiLogger} instances.
 * <p>
 * Ensures that all components across the Harmonia platform obtain uniform,
 * security-gated PHI logger instances.
 * </p>
 */
public final class PhiLoggerFactory {

    private static final ConcurrentMap<String, PhiLogger> LOGGER_CACHE = new ConcurrentHashMap<>();

    private PhiLoggerFactory() {
        // utility class
    }

    /**
     * Obtains a {@link PhiLogger} instance associated with the specified class.
     *
     * @param clazz the class for which to obtain the logger
     * @return a thread-safe {@link PhiLogger} instance
     */
    public static PhiLogger getLogger(Class<?> clazz) {
        return getLogger(clazz != null ? clazz.getName() : "Unknown");
    }

    /**
     * Obtains a {@link PhiLogger} instance associated with the specified name.
     *
     * @param name the name/category for which to obtain the logger
     * @return a thread-safe {@link PhiLogger} instance
     */
    public static PhiLogger getLogger(String name) {
        String safeName = (name != null && !name.isBlank()) ? name : "Unknown";
        return LOGGER_CACHE.computeIfAbsent(safeName, DefaultPhiLogger::new);
    }

    /**
     * Clears the logger instance cache (useful for testing).
     */
    public static void clearCacheForTesting() {
        LOGGER_CACHE.clear();
    }
}
