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

import java.util.function.Supplier;

/**
 * Architectural and API-level control interface for logging Patient Health Information (PHI).
 * <p>
 * Operational logging (INFO, WARN, ERROR) is strictly prohibited from containing PHI.
 * Therefore, {@code PhiLogger} intentionally exposes ONLY {@code debug(...)} and {@code trace(...)}
 * methods. Methods for {@code info}, {@code warn}, or {@code error} do not exist on this interface.
 * </p>
 * <p>
 * All diagnostic events emitted through this logger automatically route to the {@code org.harmonia.phi}
 * namespace and carry the {@code PHI} SLF4J Marker. Output is only emitted when both PHI diagnostic mode
 * is enabled ({@code harmonia.logging.phi-enabled=true}) and the corresponding SLF4J log level is enabled.
 * </p>
 */
public interface PhiLogger {

    /**
     * Checks whether PHI debug logging is enabled.
     * Requires both {@code harmonia.logging.phi-enabled=true} and SLF4J DEBUG level active.
     *
     * @return true if PHI debug logging is enabled, false otherwise
     */
    boolean isDebugEnabled();

    /**
     * Checks whether PHI trace logging is enabled.
     * Requires both {@code harmonia.logging.phi-enabled=true} and SLF4J TRACE level active.
     *
     * @return true if PHI trace logging is enabled, false otherwise
     */
    boolean isTraceEnabled();

    /**
     * Log a message at the DEBUG level with PHI security gating and PHI marker.
     *
     * @param message the message string to be logged
     */
    void debug(String message);

    /**
     * Log a message with a single argument at the DEBUG level.
     *
     * @param format the format string
     * @param arg the argument
     */
    void debug(String format, Object arg);

    /**
     * Log a message with two arguments at the DEBUG level.
     *
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    void debug(String format, Object arg1, Object arg2);

    /**
     * Log a message with variable arguments at the DEBUG level.
     *
     * @param format the format string
     * @param arguments variable arguments
     */
    void debug(String format, Object... arguments);

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying message.
     *
     * @param message the message accompanying the exception
     * @param throwable the exception to log
     */
    void debug(String message, Throwable throwable);

    /**
     * Log a message with lazily evaluated supplier arguments at the DEBUG level.
     * Suppliers are only executed if both PHI diagnostic mode and DEBUG level are enabled.
     *
     * @param message the format message string
     * @param suppliers lazy value suppliers
     */
    void debug(String message, Supplier<?>... suppliers);

    /**
     * Log a message at the TRACE level with PHI security gating and PHI marker.
     *
     * @param message the message string to be logged
     */
    void trace(String message);

    /**
     * Log a message with a single argument at the TRACE level.
     *
     * @param format the format string
     * @param arg the argument
     */
    void trace(String format, Object arg);

    /**
     * Log a message with two arguments at the TRACE level.
     *
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    void trace(String format, Object arg1, Object arg2);

    /**
     * Log a message with variable arguments at the TRACE level.
     *
     * @param format the format string
     * @param arguments variable arguments
     */
    void trace(String format, Object... arguments);

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying message.
     *
     * @param message the message accompanying the exception
     * @param throwable the exception to log
     */
    void trace(String message, Throwable throwable);

    /**
     * Log a message with lazily evaluated supplier arguments at the TRACE level.
     * Suppliers are only executed if both PHI diagnostic mode and TRACE level are enabled.
     *
     * @param message the format message string
     * @param suppliers lazy value suppliers
     */
    void trace(String message, Supplier<?>... suppliers);
}
