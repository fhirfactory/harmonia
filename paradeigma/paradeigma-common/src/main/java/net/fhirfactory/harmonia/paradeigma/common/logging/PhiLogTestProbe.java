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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.common.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import net.fhirfactory.harmonia.logging.DefaultPhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * In-memory Logback test probe that attaches temporary appenders to SLF4J / Logback
 * during scenario and test runs to inspect, verify, and assert operational and PHI diagnostic log streams.
 */
public class PhiLogTestProbe implements AutoCloseable {

    private final LoggerContext loggerContext;
    private final Logger rootLogger;
    private final Logger phiLoggerInternal;

    private final ListAppender<ILoggingEvent> rootOperationalAppender;
    private final ListAppender<ILoggingEvent> phiDiagnosticAppender;

    private final Level previousPhiLevel;
    private final boolean previousAdditive;

    private PhiLogTestProbe() {
        this.loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        this.phiLoggerInternal = loggerContext.getLogger(DefaultPhiLogger.PHI_LOGGER_NAME);

        this.previousPhiLevel = phiLoggerInternal.getLevel();
        this.previousAdditive = phiLoggerInternal.isAdditive();

        String runId = UUID.randomUUID().toString().substring(0, 8);

        this.rootOperationalAppender = new ListAppender<>();
        this.rootOperationalAppender.setName("PROBE_ROOT_" + runId);
        this.rootOperationalAppender.start();
        this.rootLogger.addAppender(rootOperationalAppender);

        this.phiDiagnosticAppender = new ListAppender<>();
        this.phiDiagnosticAppender.setName("PROBE_PHI_" + runId);
        this.phiDiagnosticAppender.start();
        this.phiLoggerInternal.addAppender(phiDiagnosticAppender);

        this.phiLoggerInternal.setAdditive(false);
        this.phiLoggerInternal.setLevel(Level.TRACE);
    }

    /**
     * Starts capturing log events across operational and PHI log channels.
     */
    public static PhiLogTestProbe startCapture() {
        return new PhiLogTestProbe();
    }

    public List<ILoggingEvent> getOperationalEvents() {
        return Collections.unmodifiableList(new ArrayList<>(rootOperationalAppender.list));
    }

    public List<ILoggingEvent> getPhiDiagnosticEvents() {
        return Collections.unmodifiableList(new ArrayList<>(phiDiagnosticAppender.list));
    }

    public List<ILoggingEvent> getAllEvents() {
        List<ILoggingEvent> all = new ArrayList<>();
        all.addAll(rootOperationalAppender.list);
        all.addAll(phiDiagnosticAppender.list);
        return Collections.unmodifiableList(all);
    }

    public void clear() {
        rootOperationalAppender.list.clear();
        phiDiagnosticAppender.list.clear();
    }

    public void assertNoPhiInOperationalLogs(String... phiSentinels) {
        List<ILoggingEvent> ops = getOperationalEvents();
        if (phiSentinels != null && phiSentinels.length > 0) {
            for (String sentinel : phiSentinels) {
                if (sentinel != null && !sentinel.isBlank()) {
                    for (ILoggingEvent event : ops) {
                        assertThat(event.getFormattedMessage())
                                .as("Operational log must NOT contain PHI sentinel '%s'", sentinel)
                                .doesNotContain(sentinel);
                    }
                }
            }
        }

        // Also assert no operational events carry the PHI marker
        for (ILoggingEvent event : ops) {
            if (event.getMarkerList() != null) {
                assertThat(event.getMarkerList())
                        .as("Operational logs must not have PHI marker attached")
                        .noneMatch(m -> "PHI".equalsIgnoreCase(m.getName()));
            }
        }
    }

    public void assertPhiPresentInDiagnosticLogs(String... expectedPhrases) {
        List<ILoggingEvent> phiEvents = getPhiDiagnosticEvents();
        assertThat(phiEvents)
                .as("Expected PHI diagnostic events to be captured")
                .isNotEmpty();

        if (expectedPhrases != null) {
            for (String expected : expectedPhrases) {
                if (expected != null && !expected.isBlank()) {
                    assertThat(phiEvents)
                            .extracting(ILoggingEvent::getFormattedMessage)
                            .anyMatch(msg -> msg.contains(expected));
                }
            }
        }
    }

    public void assertPhiMarkerAttachedToDiagnosticLogs() {
        List<ILoggingEvent> phiEvents = getPhiDiagnosticEvents();
        assertThat(phiEvents)
                .as("Expected PHI diagnostic events to exist")
                .isNotEmpty();

        for (ILoggingEvent event : phiEvents) {
            assertThat(event.getMarkerList())
                    .as("All events in org.harmonia.phi must carry PHI marker")
                    .isNotNull()
                    .anyMatch(m -> "PHI".equalsIgnoreCase(m.getName()));
        }
    }

    public void assertNoSecretsInAnyLog(List<String> secretSentinels) {
        SecretLeakageAssertion.assertNoSecretsPresent(getAllEvents(), secretSentinels);
    }

    @Override
    public void close() {
        if (rootOperationalAppender != null) {
            rootLogger.detachAppender(rootOperationalAppender);
            rootOperationalAppender.stop();
        }
        if (phiDiagnosticAppender != null) {
            phiLoggerInternal.detachAppender(phiDiagnosticAppender);
            phiDiagnosticAppender.stop();
        }
        phiLoggerInternal.setLevel(previousPhiLevel);
        phiLoggerInternal.setAdditive(previousAdditive);
        PhiLoggingConfig.reset();
    }
}
