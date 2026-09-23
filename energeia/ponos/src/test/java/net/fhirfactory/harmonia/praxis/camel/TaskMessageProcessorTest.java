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
import net.fhirfactory.harmonia.logging.DefaultPhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMessageProcessorTest {

    private static final String PHI_MARKER = "PATIENT-PHI-MARKER-92831";
    private static final String SECRET_TOKEN = "TOKEN-SECRET-MARKER-81742";

    private TaskCacheService taskCacheService;
    private TaskMessageProcessor processor;
    private FhirContext fhirContext;
    private DefaultCamelContext camelContext;

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

        processor = new TaskMessageProcessor();
        processor.setFhirContext(fhirContext);
        processor.setTaskCacheService(taskCacheService);

        camelContext = new DefaultCamelContext();

        processorLogger = (Logger) LoggerFactory.getLogger(TaskMessageProcessor.class);
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
    @DisplayName("Should process Task object payload and update cache")
    void testProcessTaskObject() throws Exception {
        Task task = new Task();
        task.setId("Task/proc-test-1");
        task.setStatus(Task.TaskStatus.RECEIVED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setDescription("Test task processing");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(task);

        processor.process(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_TASK_ID")).isEqualTo("proc-test-1");
        assertThat(exchange.getMessage().getHeader("HIE_TASK_PROCESSED")).isEqualTo(true);

        // Verify task updated in cache
        Optional<Task> cached = taskCacheService.getTask("proc-test-1");
        assertThat(cached).isPresent();
        assertThat(cached.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(cached.get().getBusinessStatus().getText()).isEqualTo("PROCESSED");
        assertThat(cached.get().getNote()).isNotEmpty();
    }

    @Test
    @DisplayName("Should process JSON string payload and update cache")
    void testProcessJsonStringPayload() throws Exception {
        Task task = new Task();
        task.setId("Task/proc-test-2");
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setFor(new Reference("Patient/pat-99"));
        task.setDescription("JSON payload test");

        String json = fhirContext.newJsonParser().encodeResourceToString(task);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(json);

        processor.process(exchange);

        Optional<Task> cached = taskCacheService.getTask("proc-test-2");
        assertThat(cached).isPresent();
        assertThat(cached.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(cached.get().getFor().getReference()).isEqualTo("Patient/pat-99");
    }

    @Test
    @DisplayName("Operational logs must never emit PHI marker or secret token, while emitting safe metadata")
    void testOperationalLogsSuppressPhiMarkerAndSecretTokenOnNormalTask() throws Exception {
        Task task = new Task();
        task.setId("Task/proc-test-phi-1");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setPriority(Enumerations.RequestPriority.URGENT);
        task.setFor(new Reference("Patient/" + PHI_MARKER));
        task.setDescription("Clinical review with PHI: " + PHI_MARKER);
        task.addNote().setText("Confidential notes with token: " + SECRET_TOKEN);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(task);
        exchange.getMessage().setHeader("HIE_MESSAGE_TYPE", "ClinicalTask");
        exchange.getMessage().setHeader("HIE_TRIGGER_TYPE", "OrderReview");

        processor.process(exchange);

        List<ILoggingEvent> operationalEvents = processorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.INFO || e.getLevel() == Level.WARN || e.getLevel() == Level.ERROR)
                .toList();

        assertThat(operationalEvents).isNotEmpty();

        for (ILoggingEvent event : operationalEvents) {
            String msg = event.getFormattedMessage();
            assertThat(msg)
                    .as("Operational log at level %s must not contain PHI marker", event.getLevel())
                    .doesNotContain(PHI_MARKER);
            assertThat(msg)
                    .as("Operational log at level %s must not contain secret token", event.getLevel())
                    .doesNotContain(SECRET_TOKEN);
        }

        // Verify required safe operational metadata is present at INFO
        List<String> infoMessages = operationalEvents.stream()
                .filter(e -> e.getLevel() == Level.INFO)
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        assertThat(infoMessages).anyMatch(m ->
                m.contains("taskId=proc-test-phi-1") &&
                m.contains("priority=urgent") &&
                m.contains("eventType=ClinicalTask") &&
                m.contains("triggerReason=OrderReview") &&
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

        Task task = new Task();
        task.setId("Task/proc-test-phi-2");
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setDescription("Diagnostic task for " + PHI_MARKER);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(task);

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

        for (ILoggingEvent event : operationalEvents) {
            assertThat(event.getFormattedMessage())
                    .as("Operational log must remain free of PHI even when PhiLogger is enabled")
                    .doesNotContain(PHI_MARKER);
        }
    }

    @Test
    @DisplayName("Invalid payload parsing failure must log safe failure categories and suppress payload/e.getMessage()")
    void testInvalidPayloadErrorAndWarnSuppressRawContent() throws Exception {
        String invalidPayload = "{ invalid JSON with patient " + PHI_MARKER + " and secret " + SECRET_TOKEN;

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(invalidPayload);

        processor.process(exchange);

        List<ILoggingEvent> allEvents = processorAppender.list;

        for (ILoggingEvent event : allEvents) {
            assertThat(event.getFormattedMessage())
                    .as("No log at any level should contain PHI marker on parsing failure")
                    .doesNotContain(PHI_MARKER);
            assertThat(event.getFormattedMessage())
                    .as("No log at any level should contain secret token on parsing failure")
                    .doesNotContain(SECRET_TOKEN);
        }

        // Verify safe failure categories in WARN/ERROR logs
        List<String> warnAndErrorMsgs = allEvents.stream()
                .filter(e -> e.getLevel() == Level.WARN || e.getLevel() == Level.ERROR)
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        assertThat(warnAndErrorMsgs).anyMatch(m -> m.contains("category=TASK_PARSE_FAILURE"));
        assertThat(warnAndErrorMsgs).anyMatch(m -> m.contains("category=TASK_CREATION_FAILURE"));
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
