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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Startup Warning Notification Tests")
class PhiStartupWarningTest {

    private Logger configLogger;
    private ListAppender<ILoggingEvent> appender;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        configLogger = (Logger) LoggerFactory.getLogger(PhiLoggingConfig.class);
        configLogger.setLevel(Level.WARN);
        appender = new ListAppender<>();
        appender.start();
        configLogger.addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
        if (appender != null) {
            appender.stop();
            configLogger.detachAppender(appender);
        }
    }

    @Test
    @DisplayName("TEST 11: Startup warning appears once when PHI mode is enabled")
    void test11_StartupWarningAppearsWhenPhiEnabled() {
        PhiLoggingConfig.setPhiEnabled(true);

        assertThat(appender.list).isNotEmpty();
        String fullWarning = appender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .reduce("", (a, b) -> a + " " + b);

        assertThat(fullWarning)
                .contains("PHI diagnostic logging is ENABLED")
                .contains("DEBUG/TRACE diagnostic logs may contain protected health information");

        // Verify it is emitted only once per JVM lifecycle
        int initialSize = appender.list.size();
        PhiLoggingConfig.checkAndEmitStartupWarning();
        PhiLoggingConfig.isPhiEnabled();
        assertThat(appender.list).hasSize(initialSize);
    }

    @Test
    @DisplayName("TEST 12: Startup warning contains strictly NO PHI")
    void test12_StartupWarningContainsNoPhi() {
        PhiLoggingConfig.setPhiEnabled(true);

        for (ILoggingEvent event : appender.list) {
            String msg = event.getFormattedMessage();
            assertThat(msg)
                    .doesNotContain("Patient")
                    .doesNotContain("MRN")
                    .doesNotContain("DOB")
                    .doesNotContain("John")
                    .doesNotContain("Doe")
                    .doesNotContain("Observation")
                    .doesNotContain("Diagnosis");
        }
    }
}
