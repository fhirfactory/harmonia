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

package net.fhirfactory.harmonia.mllpgateway.hl7;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import net.fhirfactory.harmonia.logging.DefaultPhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import net.fhirfactory.harmonia.mllpgateway.camel.IncomingAdtMessageProcessorWrapper;
import net.fhirfactory.harmonia.mllpgateway.camel.IncomingMfnMessageProcessorWrapper;
import net.fhirfactory.harmonia.mllpgateway.config.MllpConfig;
import net.fhirfactory.harmonia.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultProvenanceService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultTaskService;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import org.apache.camel.CamelContext;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IncomingHl7LoggingSecurityTest {

    private static final String PHI_MARKER = "PATIENT-PHI-MARKER-92831";
    private static final String SECRET_TOKEN = "TOKEN-SECRET-MARKER-81742";

    private ListAppender<ILoggingEvent> adtWrapperAppender;
    private ListAppender<ILoggingEvent> mfnWrapperAppender;
    private ListAppender<ILoggingEvent> adtProcessorAppender;
    private ListAppender<ILoggingEvent> mfnProcessorAppender;
    private ListAppender<ILoggingEvent> ormProcessorAppender;
    private ListAppender<ILoggingEvent> oruProcessorAppender;
    private ListAppender<ILoggingEvent> phiAppender;

    private Logger adtWrapperLogger;
    private Logger mfnWrapperLogger;
    private Logger adtProcessorLogger;
    private Logger mfnProcessorLogger;
    private Logger ormProcessorLogger;
    private Logger oruProcessorLogger;
    private Logger phiLogger;

    private DefaultTaskService taskService;
    private DefaultCommunicationService communicationService;
    private DefaultProvenanceService provenanceService;
    private TaskEventProducerService taskEventProducerService;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.setPhiEnabled(false);

        adtWrapperAppender = new ListAppender<>();
        mfnWrapperAppender = new ListAppender<>();
        adtProcessorAppender = new ListAppender<>();
        mfnProcessorAppender = new ListAppender<>();
        ormProcessorAppender = new ListAppender<>();
        oruProcessorAppender = new ListAppender<>();
        phiAppender = new ListAppender<>();

        adtWrapperAppender.start();
        mfnWrapperAppender.start();
        adtProcessorAppender.start();
        mfnProcessorAppender.start();
        ormProcessorAppender.start();
        oruProcessorAppender.start();
        phiAppender.start();

        adtWrapperLogger = (Logger) LoggerFactory.getLogger(IncomingAdtMessageProcessorWrapper.class);
        mfnWrapperLogger = (Logger) LoggerFactory.getLogger(IncomingMfnMessageProcessorWrapper.class);
        adtProcessorLogger = (Logger) LoggerFactory.getLogger(IncomingAdtMessageProcessor.class);
        mfnProcessorLogger = (Logger) LoggerFactory.getLogger(IncomingMfnMessageProcessor.class);
        ormProcessorLogger = (Logger) LoggerFactory.getLogger(IncomingOrmMessageProcessor.class);
        oruProcessorLogger = (Logger) LoggerFactory.getLogger(IncomingOruMessageProcessor.class);
        phiLogger = (Logger) LoggerFactory.getLogger(DefaultPhiLogger.PHI_LOGGER_NAME);

        adtWrapperLogger.setLevel(Level.DEBUG);
        mfnWrapperLogger.setLevel(Level.DEBUG);
        adtProcessorLogger.setLevel(Level.DEBUG);
        mfnProcessorLogger.setLevel(Level.DEBUG);
        ormProcessorLogger.setLevel(Level.DEBUG);
        oruProcessorLogger.setLevel(Level.DEBUG);
        phiLogger.setLevel(Level.DEBUG);

        adtWrapperLogger.addAppender(adtWrapperAppender);
        mfnWrapperLogger.addAppender(mfnWrapperAppender);
        adtProcessorLogger.addAppender(adtProcessorAppender);
        mfnProcessorLogger.addAppender(mfnProcessorAppender);
        ormProcessorLogger.addAppender(ormProcessorAppender);
        oruProcessorLogger.addAppender(oruProcessorAppender);
        phiLogger.addAppender(phiAppender);

        taskService = new DefaultTaskService();
        taskService.clear();
        communicationService = new DefaultCommunicationService();
        communicationService.clear();
        provenanceService = new DefaultProvenanceService();
        provenanceService.clear();
        taskEventProducerService = mock(TaskEventProducerService.class);

        camelContext = new DefaultCamelContext();
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.setPhiEnabled(false);

        if (adtWrapperLogger != null) adtWrapperLogger.detachAppender(adtWrapperAppender);
        if (mfnWrapperLogger != null) mfnWrapperLogger.detachAppender(mfnWrapperAppender);
        if (adtProcessorLogger != null) adtProcessorLogger.detachAppender(adtProcessorAppender);
        if (mfnProcessorLogger != null) mfnProcessorLogger.detachAppender(mfnProcessorAppender);
        if (ormProcessorLogger != null) ormProcessorLogger.detachAppender(ormProcessorAppender);
        if (oruProcessorLogger != null) oruProcessorLogger.detachAppender(oruProcessorAppender);
        if (phiLogger != null) phiLogger.detachAppender(phiAppender);
    }

    @Test
    @DisplayName("ADT wrapper logs message metadata at DEBUG without raw HL7 message body or PHI marker")
    void testAdtWrapperLogsMetadataAtDebugWithoutRawHl7OrPhiMarker() throws Exception {
        IncomingAdtMessageProcessor processor = new IncomingAdtMessageProcessor(taskService, communicationService, taskEventProducerService);
        IncomingAdtMessageProcessorWrapper wrapper = new IncomingAdtMessageProcessorWrapper(processor);

        String hl7A01 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-A01-LOG01|P|2.4\r" +
                "EVN|A01|20260907101500\r" +
                "PID|1||" + PHI_MARKER + "^^^HOSPITAL^MR||" + PHI_MARKER + "^DOE^JANE||19800512|F|||123 MAIN ST^^SPRINGFIELD^IL^62701\r" +
                "PV1|1|I|WARDA^RM101^BED1^HOSPITAL||||DOC01^" + SECRET_TOKEN + "^^DR||||||||||||V20260907-01\r";

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(hl7A01);

        wrapper.process(exchange);

        List<ILoggingEvent> events = adtWrapperAppender.list;
        assertThat(events).isNotEmpty();

        // Operational wrapper logs at any level (DEBUG, INFO, WARN, ERROR) must never contain PHI or secrets
        for (ILoggingEvent event : events) {
            String formatted = event.getFormattedMessage();
            assertThat(formatted).doesNotContain(PHI_MARKER);
            assertThat(formatted).doesNotContain(SECRET_TOKEN);
            assertThat(formatted).doesNotContain("PID|1||");
            assertThat(formatted).doesNotContain("PV1|1|I|");
        }

        // Verify the safe DEBUG metadata log
        boolean foundMetadataLog = events.stream()
                .filter(e -> e.getLevel() == Level.DEBUG)
                .anyMatch(e -> e.getFormattedMessage().contains("Received HL7 message for processing")
                        && e.getFormattedMessage().contains("messageType=ADT^A01")
                        && e.getFormattedMessage().contains("controlId=MSG-A01-LOG01")
                        && e.getFormattedMessage().contains("length="));
        assertThat(foundMetadataLog).isTrue();
    }

    @Test
    @DisplayName("ADT wrapper failure logs safe category at WARN without leaking error message or PHI marker")
    void testAdtWrapperFailureLogsCategoryAtWarnWithoutErrorMessage() throws Exception {
        IncomingAdtMessageProcessor mockProcessor = mock(IncomingAdtMessageProcessor.class);
        when(mockProcessor.processAdtMessage(anyString()))
                .thenReturn(AdtProcessingResult.failure("MSG-FAIL-ADT", "A01", "MSA|AE", "Sensitive exception details: " + PHI_MARKER + " " + SECRET_TOKEN));

        IncomingAdtMessageProcessorWrapper wrapper = new IncomingAdtMessageProcessorWrapper(mockProcessor);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody("MSH|^~\\&|EPIC|HOSPITAL|||20260907101500||ADT^A01|MSG-FAIL-ADT|P|2.4\r");

        wrapper.process(exchange);

        List<ILoggingEvent> events = adtWrapperAppender.list;
        List<ILoggingEvent> warnEvents = events.stream()
                .filter(e -> e.getLevel() == Level.WARN)
                .toList();

        assertThat(warnEvents).hasSize(1);
        String warnMsg = warnEvents.get(0).getFormattedMessage();
        assertThat(warnMsg).contains("ADT trigger event processing failed [controlId=MSG-FAIL-ADT, triggerEvent=A01, category=ADT_PROCESSING_FAILED]");
        assertThat(warnMsg).doesNotContain(PHI_MARKER);
        assertThat(warnMsg).doesNotContain(SECRET_TOKEN);
        assertThat(warnMsg).doesNotContain("Sensitive exception details");
    }

    @Test
    @DisplayName("MFN wrapper logs message metadata at DEBUG without raw HL7 message body or PHI marker")
    void testMfnWrapperLogsMetadataAtDebugWithoutRawHl7OrPhiMarker() throws Exception {
        IncomingMfnMessageProcessor processor = new IncomingMfnMessageProcessor(taskService, communicationService, taskEventProducerService);
        IncomingMfnMessageProcessorWrapper wrapper = new IncomingMfnMessageProcessorWrapper(processor);

        String hl7Mfn = "MSH|^~\\&|STAFF_APP|HOSPITAL_A|HIE_APP|HIE_DEST|20260910120000||MFN^M02|MSG-MFN-LOG02|P|2.4\r" +
                "MFI|PRA^Practitioner Master File|STAFF_APP|UPD|20260910120000|20260910120000|NE\r" +
                "MFE|MUP|MFN-ENTRY-001|20260910120000|DOC-12345^" + PHI_MARKER + "|CE\r" +
                "STF|DOC-12345^" + PHI_MARKER + "|NPI-9876543210^^^NPI^NPI|" + PHI_MARKER + "^" + SECRET_TOKEN + "|PHY|M\r";

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(hl7Mfn);

        wrapper.process(exchange);

        List<ILoggingEvent> events = mfnWrapperAppender.list;
        assertThat(events).isNotEmpty();

        for (ILoggingEvent event : events) {
            String formatted = event.getFormattedMessage();
            assertThat(formatted).doesNotContain(PHI_MARKER);
            assertThat(formatted).doesNotContain(SECRET_TOKEN);
            assertThat(formatted).doesNotContain("STF|DOC-12345");
            assertThat(formatted).doesNotContain("MFE|MUP|");
        }

        boolean foundMetadataLog = events.stream()
                .filter(e -> e.getLevel() == Level.DEBUG)
                .anyMatch(e -> e.getFormattedMessage().contains("Received HL7 MFN message for processing")
                        && e.getFormattedMessage().contains("messageType=MFN^M02")
                        && e.getFormattedMessage().contains("controlId=MSG-MFN-LOG02")
                        && e.getFormattedMessage().contains("length="));
        assertThat(foundMetadataLog).isTrue();
    }

    @Test
    @DisplayName("MFN wrapper failure logs safe category at WARN without leaking error message or PHI marker")
    void testMfnWrapperFailureLogsCategoryAtWarnWithoutErrorMessage() throws Exception {
        IncomingMfnMessageProcessor mockProcessor = mock(IncomingMfnMessageProcessor.class);
        when(mockProcessor.processMfnMessage(anyString()))
                .thenReturn(MfnProcessingResult.failure("MSG-FAIL-MFN", "M02", "MSA|AE", "Sensitive MFN details: " + PHI_MARKER + " " + SECRET_TOKEN));

        IncomingMfnMessageProcessorWrapper wrapper = new IncomingMfnMessageProcessorWrapper(mockProcessor);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody("MSH|^~\\&|STAFF_APP|HOSPITAL_A|||20260910120000||MFN^M02|MSG-FAIL-MFN|P|2.4\r");

        wrapper.process(exchange);

        List<ILoggingEvent> warnEvents = mfnWrapperAppender.list.stream()
                .filter(e -> e.getLevel() == Level.WARN)
                .toList();

        assertThat(warnEvents).hasSize(1);
        String warnMsg = warnEvents.get(0).getFormattedMessage();
        assertThat(warnMsg).contains("MFN trigger event processing failed [controlId=MSG-FAIL-MFN, triggerEvent=M02, category=MFN_PROCESSING_FAILED]");
        assertThat(warnMsg).doesNotContain(PHI_MARKER);
        assertThat(warnMsg).doesNotContain(SECRET_TOKEN);
        assertThat(warnMsg).doesNotContain("Sensitive MFN details");
    }

    @Test
    @DisplayName("ADT processor operational logs suppress PHI while PhiLogger diagnostics are controlled")
    void testAdtProcessorOperationalLogsSuppressPhiAndSecretWhilePhiLoggerControlled() {
        IncomingAdtMessageProcessor processor = new IncomingAdtMessageProcessor(taskService, communicationService, taskEventProducerService);

        String hl7A01 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-A01-SAFE01|P|2.4\r" +
                "EVN|A01|20260907101500\r" +
                "PID|1||PAT10099^^^HOSPITAL^MR||" + PHI_MARKER + "^DOE^JANE||19800512|F|||123 MAIN ST\r" +
                "PV1|1|I|WARDA^RM101^BED1^HOSPITAL||||DOC01^" + SECRET_TOKEN + "^^DR||||||||||||V20260907-01\r";

        // 1. When PhiLoggingConfig is false (default), PhiLogger is completely silent
        processor.processAdtMessage(hl7A01);

        assertThat(phiAppender.list).isEmpty();

        // Operational logs (INFO, WARN, ERROR) must not contain PHI or secrets
        List<ILoggingEvent> operationalEvents = adtProcessorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.INFO || e.getLevel() == Level.WARN || e.getLevel() == Level.ERROR)
                .toList();

        assertThat(operationalEvents).isNotEmpty();
        for (ILoggingEvent event : operationalEvents) {
            String formatted = event.getFormattedMessage();
            assertThat(formatted).doesNotContain(PHI_MARKER);
            assertThat(formatted).doesNotContain(SECRET_TOKEN);
        }

        // 2. When PhiLoggingConfig is true, PhiLogger receives deliberate diagnostic event
        PhiLoggingConfig.setPhiEnabled(true);
        phiAppender.list.clear();
        adtProcessorAppender.list.clear();

        processor.processAdtMessage(hl7A01);

        assertThat(phiAppender.list).isNotEmpty();
        boolean foundPhiDiagnostic = phiAppender.list.stream()
                .anyMatch(e -> e.getFormattedMessage().contains("Inbound HL7 ADT message received")
                        && e.getFormattedMessage().contains(PHI_MARKER));
        assertThat(foundPhiDiagnostic).isTrue();
    }

    @Test
    @DisplayName("ADT processor error logging sanitizes exception message and suppresses PHI marker")
    void testAdtProcessorErrorLoggingSanitizesExceptionMessage() throws Exception {
        TaskEventProducerService failingProducer = mock(TaskEventProducerService.class);
        doThrow(new RuntimeException("Connection error with upstream payload: " + PHI_MARKER + " " + SECRET_TOKEN))
                .when(failingProducer).sendTaskEvent(any(ErgonEvent.class));

        IncomingAdtMessageProcessor processor = new IncomingAdtMessageProcessor(taskService, communicationService, failingProducer);

        String hl7A01 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-A01-ERR01|P|2.4\r" +
                "PID|1||PAT10099||SMITH^JOHN\r";

        AdtProcessingResult result = processor.processAdtMessage(hl7A01);
        assertThat(result.isSuccess()).isFalse();

        List<ILoggingEvent> errorEvents = adtProcessorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents).hasSize(1);
        String errorMsg = errorEvents.get(0).getFormattedMessage();
        assertThat(errorMsg).contains("Error processing HL7 v2.4 ADT message [controlId=MSG-A01-ERR01, triggerEvent=A01, exception=java.lang.RuntimeException, errorCode=AE, description=Processing error]");
        assertThat(errorMsg).doesNotContain(PHI_MARKER);
        assertThat(errorMsg).doesNotContain(SECRET_TOKEN);
        assertThat(errorMsg).doesNotContain("Connection error with upstream payload");
    }

    @Test
    @DisplayName("MFN processor error logging sanitizes exception message and suppresses PHI marker")
    void testMfnProcessorErrorLoggingSanitizesExceptionMessage() throws Exception {
        TaskEventProducerService failingProducer = mock(TaskEventProducerService.class);
        doThrow(new RuntimeException("MFN sync error with practitioner: " + PHI_MARKER + " " + SECRET_TOKEN))
                .when(failingProducer).sendTaskEvent(any(ErgonEvent.class));

        IncomingMfnMessageProcessor processor = new IncomingMfnMessageProcessor(taskService, communicationService, failingProducer);

        String hl7Mfn = "MSH|^~\\&|STAFF_APP|HOSPITAL_A|HIE_APP|HIE_DEST|20260910120000||MFN^M02|MSG-MFN-ERR02|P|2.4\r" +
                "MFI|PRA^Practitioner Master File|STAFF_APP|UPD|20260910120000|20260910120000|NE\r" +
                "MFE|MUP|MFN-ENTRY-001|20260910120000|DOC-12345^SMITH^JOHN|CE\r" +
                "STF|DOC-12345^SMITH^JOHN|NPI-9876543210^^^NPI^NPI~DOC-12345^^^HOSP^MD|SMITH^JOHN^ROBERT|PHY|M\r";

        MfnProcessingResult result = processor.processMfnMessage(hl7Mfn);
        assertThat(result.isSuccess()).isFalse();

        List<ILoggingEvent> errorEvents = mfnProcessorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents).hasSize(1);
        String errorMsg = errorEvents.get(0).getFormattedMessage();
        assertThat(errorMsg).contains("Error processing HL7 v2.4 MFN message [controlId=MSG-MFN-ERR02, triggerEvent=M02, exception=java.lang.RuntimeException, errorCode=AE, description=Processing error]");
        assertThat(errorMsg).doesNotContain(PHI_MARKER);
        assertThat(errorMsg).doesNotContain(SECRET_TOKEN);
        assertThat(errorMsg).doesNotContain("MFN sync error with practitioner");
    }

    @Test
    @DisplayName("ORM processor error logging sanitizes exception message and suppresses PHI marker")
    void testOrmProcessorErrorLoggingSanitizesExceptionMessage() throws Exception {
        TaskEventProducerService failingProducer = mock(TaskEventProducerService.class);
        doThrow(new RuntimeException("ORM Petasos error with order details: " + PHI_MARKER + " " + SECRET_TOKEN))
                .when(failingProducer).sendTaskEvent(any());

        IncomingOrmMessageProcessor processor = new IncomingOrmMessageProcessor(taskService, communicationService, provenanceService,
                failingProducer, new MllpConfig("0.0.0.0", 2104, true));

        String ormHl7 = "MSH|^~\\&|PARADEIGMA_EMR|FACILITY|HARMONIA|HIE|20260915120000||ORM^O01|MSG-ORM-ERR03|P|2.4\r" +
                "PID|1||PAT-101^^^MRN||Smith^John\r" +
                "PV1|1|I|WARD-3A^301^A\r" +
                "ORC|NW|ORD-1001|||IP||^^^R||20260915120000\r" +
                "OBR|1|ORD-1001||CBC^Complete Blood Count^LN\r";

        OrmProcessingResult result = processor.processOrmMessage(ormHl7);
        assertThat(result.isSuccess()).isFalse();

        List<ILoggingEvent> errorEvents = ormProcessorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents).hasSize(1);
        String errorMsg = errorEvents.get(0).getFormattedMessage();
        assertThat(errorMsg).contains("Failed to parse and process HL7 ORM message [exception=java.lang.RuntimeException, errorCode=AE, description=Processing error]");
        assertThat(errorMsg).doesNotContain(PHI_MARKER);
        assertThat(errorMsg).doesNotContain(SECRET_TOKEN);
        assertThat(errorMsg).doesNotContain("ORM Petasos error with order details");
    }

    @Test
    @DisplayName("ORU processor error logging sanitizes exception message and suppresses PHI marker")
    void testOruProcessorErrorLoggingSanitizesExceptionMessage() throws Exception {
        TaskEventProducerService failingProducer = mock(TaskEventProducerService.class);
        doThrow(new RuntimeException("ORU Petasos error with clinical result: " + PHI_MARKER + " " + SECRET_TOKEN))
                .when(failingProducer).sendTaskEvent(any());

        IncomingOruMessageProcessor processor = new IncomingOruMessageProcessor(taskService, communicationService, provenanceService,
                failingProducer, new MllpConfig("0.0.0.0", 2102, true));

        String oruHl7 = "MSH|^~\\&|PARADEIGMA_LMS|FACILITY|HARMONIA|HIE|20260915120000||ORU^R01|MSG-ORU-ERR04|P|2.4\r" +
                "PID|1||PAT-102^^^MRN||Smith^John\r" +
                "PV1|1|I|WARD-3A^301^A\r" +
                "ORC|RE|ORD-1002|LMS-2002||CM||||20260915120000\r" +
                "OBR|1|ORD-1002|LMS-2002|CBC^Complete Blood Count^LN|||20260915115000\r" +
                "OBX|1|NM|718-7^Haemoglobin^LN||145.0|g/L|130-180|N|||F\r";

        OruProcessingResult result = processor.processOruMessage(oruHl7);
        assertThat(result.isSuccess()).isFalse();

        List<ILoggingEvent> errorEvents = oruProcessorAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents).hasSize(1);
        String errorMsg = errorEvents.get(0).getFormattedMessage();
        assertThat(errorMsg).contains("Failed to parse and process HL7 ORU message [exception=java.lang.RuntimeException, errorCode=AE, description=Processing error]");
        assertThat(errorMsg).doesNotContain(PHI_MARKER);
        assertThat(errorMsg).doesNotContain(SECRET_TOKEN);
        assertThat(errorMsg).doesNotContain("ORU Petasos error with clinical result");
    }
}
