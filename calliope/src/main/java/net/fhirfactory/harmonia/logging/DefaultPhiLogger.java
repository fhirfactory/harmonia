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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Default implementation of {@link PhiLogger} routing all PHI diagnostic events
 * to the dedicated {@code org.harmonia.phi} logger namespace and attaching the mandatory {@code PHI} marker.
 */
public class DefaultPhiLogger implements PhiLogger {

    public static final String PHI_LOGGER_NAME = "org.harmonia.phi";
    public static final String PHI_MARKER_NAME = "PHI";
    public static final Marker PHI_MARKER = MarkerFactory.getMarker(PHI_MARKER_NAME);

    private final Logger delegate;
    private final String sourceName;

    /**
     * Constructs a new {@code DefaultPhiLogger}.
     *
     * @param sourceName name of the originating class or component
     */
    public DefaultPhiLogger(String sourceName) {
        this.sourceName = Objects.requireNonNullElse(sourceName, "Unknown");
        this.delegate = LoggerFactory.getLogger(PHI_LOGGER_NAME);
    }

    /**
     * Constructs a new {@code DefaultPhiLogger} with explicit delegate (for testing/customization).
     *
     * @param sourceName name of the originating class or component
     * @param delegate the underlying SLF4J logger delegate
     */
    public DefaultPhiLogger(String sourceName, Logger delegate) {
        this.sourceName = Objects.requireNonNullElse(sourceName, "Unknown");
        this.delegate = Objects.requireNonNullElseGet(delegate, () -> LoggerFactory.getLogger(PHI_LOGGER_NAME));
    }

    public String getSourceName() {
        return sourceName;
    }

    public Logger getDelegate() {
        return delegate;
    }

    @Override
    public boolean isDebugEnabled() {
        return PhiLoggingConfig.isPhiEnabled() && delegate.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return PhiLoggingConfig.isPhiEnabled() && delegate.isTraceEnabled();
    }

    @Override
    public void debug(String message) {
        if (isDebugEnabled()) {
            delegate.debug(PHI_MARKER, message);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (isDebugEnabled()) {
            Object eval = (arg instanceof Supplier<?>) ? ((Supplier<?>) arg).get() : arg;
            delegate.debug(PHI_MARKER, format, eval);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            Object eval1 = (arg1 instanceof Supplier<?>) ? ((Supplier<?>) arg1).get() : arg1;
            Object eval2 = (arg2 instanceof Supplier<?>) ? ((Supplier<?>) arg2).get() : arg2;
            delegate.debug(PHI_MARKER, format, eval1, eval2);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            if (arguments == null) {
                delegate.debug(PHI_MARKER, format, (Object[]) null);
                return;
            }
            Object[] evaluated = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                evaluated[i] = (arguments[i] instanceof Supplier<?>) ? ((Supplier<?>) arguments[i]).get() : arguments[i];
            }
            delegate.debug(PHI_MARKER, format, evaluated);
        }
    }

    @Override
    public void debug(String message, Throwable throwable) {
        if (isDebugEnabled()) {
            delegate.debug(PHI_MARKER, message, throwable);
        }
    }

    @Override
    public void debug(String message, Supplier<?>... suppliers) {
        if (isDebugEnabled()) {
            if (suppliers == null) {
                delegate.debug(PHI_MARKER, message);
                return;
            }
            Object[] evaluated = new Object[suppliers.length];
            for (int i = 0; i < suppliers.length; i++) {
                evaluated[i] = (suppliers[i] != null) ? suppliers[i].get() : null;
            }
            delegate.debug(PHI_MARKER, message, evaluated);
        }
    }

    @Override
    public void trace(String message) {
        if (isTraceEnabled()) {
            delegate.trace(PHI_MARKER, message);
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            Object eval = (arg instanceof Supplier<?>) ? ((Supplier<?>) arg).get() : arg;
            delegate.trace(PHI_MARKER, format, eval);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            Object eval1 = (arg1 instanceof Supplier<?>) ? ((Supplier<?>) arg1).get() : arg1;
            Object eval2 = (arg2 instanceof Supplier<?>) ? ((Supplier<?>) arg2).get() : arg2;
            delegate.trace(PHI_MARKER, format, eval1, eval2);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            if (arguments == null) {
                delegate.trace(PHI_MARKER, format, (Object[]) null);
                return;
            }
            Object[] evaluated = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                evaluated[i] = (arguments[i] instanceof Supplier<?>) ? ((Supplier<?>) arguments[i]).get() : arguments[i];
            }
            delegate.trace(PHI_MARKER, format, evaluated);
        }
    }

    @Override
    public void trace(String message, Throwable throwable) {
        if (isTraceEnabled()) {
            delegate.trace(PHI_MARKER, message, throwable);
        }
    }

    @Override
    public void trace(String message, Supplier<?>... suppliers) {
        if (isTraceEnabled()) {
            if (suppliers == null) {
                delegate.trace(PHI_MARKER, message);
                return;
            }
            Object[] evaluated = new Object[suppliers.length];
            for (int i = 0; i < suppliers.length; i++) {
                evaluated[i] = (suppliers[i] != null) ? suppliers[i].get() : null;
            }
            delegate.trace(PHI_MARKER, message, evaluated);
        }
    }
}
