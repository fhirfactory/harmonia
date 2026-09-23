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

package net.fhirfactory.harmonia.praxis.camel;

import ca.uhn.fhir.context.FhirContext;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.logging.DefaultPhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskEventMessageProcessor Logging Hardening Tests")
class TaskEventMessageProcessorLoggingTest {

    private static final String PHI_MARKER = "PATIENT-PHI-MARKER-92831";
    private static final String SECRET_TOKEN = "TOKEN-SECRET-MARKER-81742";

    private TaskCacheService taskCacheService;
    private TaskEventMessageProcessor processor;
    private FhirContext fhirContext;
    private DefaultCamelContext camelContext;
    private ObjectMapper objectMapper;

    private Logger processorLogger;
    private Logger phiLogbackLogger;
    private ListAppender<ILoggingEvent> processorAppender;
    private ListAppender<ILoggingEvent> phiAppender;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        System.clearProperty(PhiLoggingConfig.PROPERTY_PHI_ENABLED);

        fhirContext = FhirContext.forR5();
        taskCacheService = new TaskCacheService();
        taskCacheService.init();
        taskCacheService.clear();

        objectMapper = new ObjectMapper();

        processor = new TaskEventMessageProcessor();
        processor.setFhirContext(fhirContext);
        processor.setTaskCacheService(taskCacheService);
        processor.setObjectMapper(objectMapper);

        camelContext = new DefaultCamelContext();

        processorLogger = (Logger) LoggerFactory.getLogger(TaskEventMessageProcessor.class);
        processorLogger.setLevel(Level.TRACE);
        processorAppender = new ListAppender<>();
        processorAppender.start();
        processorLogger.addAppender(processorAppender);

        phiLogbackLogger = (Logger) LoggerFactory.getLogger(DefaultPhiLogger.PHI_LOGGER_NAME);
        phiLogbackLogger.setLevel(Level.DEBUG);
        phiAppender = new ListAppender<>();
        phiAppender.start();
        phiLogbackLogger.addAppender(phiAppender);
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
        System.clearProperty(PhiLoggingConfig.PROPERTY_PHI_ENABLED);

        if (processorAppender != null) {
            processorAppender.stop();
            processorLogger.detachAppender(processorAppender);
        }
        if (phiAppender != null) {
            phiAppender.stop();
            phiLogbackLogger.detachAppender(phiAppender);
        }
    }

    @Test
    @DisplayName("Operational logs must never emit PHI marker or secret token during TaskEvent processing")
    void testOperationalLogsSuppressPhiMarkerAndSecretTokenOnNormalEvent() throws Exception {
        ErgonEvent event = new ErgonEvent(
                "event-phi-test-1",
                "TRIAGE",
                "in-progress",
                "gateway-alpha",
                "ADT",
                "A01",
                "CTRL-12345",
                "source-with-token-" + SECRET_TOKEN,
                "Patient admission event with PHI: " + PHI_MARKER
        );

        String jsonPayload = objectMapper.writeValueAsString(event);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(jsonPayload);

        processor.process(exchange);

        List<ILoggingEvent> operationalEvents = processorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.INFO || e.getLevel() == Level.WARN || e.getLevel() == Level.ERROR)
                .toList();

        assertThat(operationalEvents).isNotEmpty();

        for (ILoggingEvent logEvent : operationalEvents) {
            String msg = logEvent.getFormattedMessage();
            assertThat(msg)
                    .as("Operational log at level %s must not contain PHI marker", logEvent.getLevel())
                    .doesNotContain(PHI_MARKER);
            assertThat(msg)
                    .as("Operational log at level %s must not contain secret token", logEvent.getLevel())
                    .doesNotContain(SECRET_TOKEN);
        }

        // Verify required safe operational metadata is present at INFO
        List<String> infoMessages = operationalEvents.stream()
                .filter(e -> e.getLevel() == Level.INFO)
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        assertThat(infoMessages).anyMatch(m ->
                m.contains("taskId=event-phi-test-1") &&
                m.contains("businessStatus=TRIAGE") &&
                m.contains("priority=none") &&
                m.contains("eventType=ADT") &&
                m.contains("triggerReason=A01") &&
                m.contains("stage=PROCESSING")
        );

        // Verify PhiLogger stayed silent by default (harmonia.logging.phi-enabled=false)
        assertThat(phiAppender.list)
                .as("PhiLogger must not emit any events when phi-enabled is false by default")
                .noneMatch(e -> e.getFormattedMessage().contains(PHI_MARKER));
    }

    @Test
    @DisplayName("Controlled PhiLogger emits diagnostic payload at DEBUG when explicitly enabled")
    void testControlledPhiLoggerEmitsWhenExplicitlyEnabled() throws Exception {
        PhiLoggingConfig.setPhiEnabled(true);

        ErgonEvent event = new ErgonEvent(
                "event-phi-test-2",
                "DISPATCH",
                "requested",
                "pas-gw",
                "ADT",
                "A08",
                "CTRL-67890",
                "mllp-gateway",
                "Diagnostic event with PHI: " + PHI_MARKER
        );

        String jsonPayload = objectMapper.writeValueAsString(event);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(jsonPayload);

        processor.process(exchange);

        // PhiLogger should have emitted diagnostic payload under org.harmonia.phi
        List<ILoggingEvent> phiEvents = phiAppender.list.stream()
                .filter(e -> e.getLevel() == Level.DEBUG)
                .filter(e -> e.getFormattedMessage().contains(PHI_MARKER))
                .toList();

        assertThat(phiEvents)
                .as("PhiLogger should emit diagnostic event containing payload when explicitly enabled")
                .isNotEmpty();

        // Operational logs at INFO, WARN, ERROR must still be strictly clean
        List<ILoggingEvent> operationalEvents = processorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.INFO || e.getLevel() == Level.WARN || e.getLevel() == Level.ERROR)
                .toList();

        for (ILoggingEvent logEvent : operationalEvents) {
            assertThat(logEvent.getFormattedMessage())
                    .as("Operational log must remain free of PHI even when PhiLogger is enabled")
                    .doesNotContain(PHI_MARKER);
        }
    }

    @Test
    @DisplayName("Invalid JSON payload fallback logs safe warning without payload or exception message leakage")
    void testInvalidJsonFallbackLogsSafeWarningWithoutPayloadLeakage() throws Exception {
        String invalidPayload = "{ invalid JSON with patient " + PHI_MARKER + " and secret " + SECRET_TOKEN;

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(invalidPayload);

        processor.process(exchange);

        List<ILoggingEvent> allEvents = processorAppender.list;

        for (ILoggingEvent logEvent : allEvents) {
            assertThat(logEvent.getFormattedMessage())
                    .as("No log at any level should contain PHI marker on parse failure")
                    .doesNotContain(PHI_MARKER);
            assertThat(logEvent.getFormattedMessage())
                    .as("No log at any level should contain secret token on parse failure")
                    .doesNotContain(SECRET_TOKEN);
        }

        // Verify safe failure categories in WARN/ERROR logs
        List<String> warnAndErrorMsgs = allEvents.stream()
                .filter(e -> e.getLevel() == Level.WARN || e.getLevel() == Level.ERROR)
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        assertThat(warnAndErrorMsgs).anyMatch(m -> m.contains("category=TASK_EVENT_PARSE_FAILURE"));
        assertThat(warnAndErrorMsgs).anyMatch(m -> m.contains("category=INVALID_TASK_EVENT"));
    }

    @Test
    @DisplayName("Missing taskId logs safe error category without payload dump")
    void testMissingTaskIdLogsSafeErrorWithoutPayloadDump() throws Exception {
        ErgonEvent event = new ErgonEvent(
                "",
                "PROCESS",
                "completed",
                "gw-1",
                "Task with missing ID and PHI: " + PHI_MARKER
        );

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(event);

        processor.process(exchange);

        List<ILoggingEvent> errorEvents = processorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents).isNotEmpty();

        for (ILoggingEvent errEvent : errorEvents) {
            assertThat(errEvent.getFormattedMessage())
                    .as("Error log must not contain PHI marker")
                    .doesNotContain(PHI_MARKER);
            assertThat(errEvent.getFormattedMessage())
                    .contains("category=INVALID_TASK_EVENT")
                    .contains("stage=VALIDATION");
        }
    }

    @Test
    @DisplayName("Null message body logs safe warning with category EMPTY_PAYLOAD")
    void testNullMessageBodyLogsSafeWarning() throws Exception {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(null);

        processor.process(exchange);

        List<ILoggingEvent> warnEvents = processorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.WARN)
                .toList();

        assertThat(warnEvents).hasSize(1);
        assertThat(warnEvents.get(0).getFormattedMessage())
                .contains("category=EMPTY_PAYLOAD")
                .contains("stage=RECEIVED");
    }
}
