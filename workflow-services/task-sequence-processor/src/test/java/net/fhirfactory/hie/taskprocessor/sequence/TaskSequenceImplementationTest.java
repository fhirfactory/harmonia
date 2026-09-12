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

package net.fhirfactory.hie.taskprocessor.sequence;

import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.model.topic.TopicSubscription;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit;
import net.fhirfactory.hie.taskprocessors.patient.demographics.PatientDemographicsUpdate;
import net.fhirfactory.hie.taskprocessors.patient.identity.PatientIdentityUpdate;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskSequenceImplementationTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;

    @BeforeEach
    void setUp() throws Exception {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (producerTemplate != null) {
            producerTemplate.stop();
        }
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    @DisplayName("Should initialize from TaskSequenceDefinition")
    void testInitializeFromDefinition() {
        TaskSequenceDefinition def = new TaskSequenceDefinition("seq-impl-1", "Implementation Test 1");
        def.setSequenceDescription("Description");
        def.setTargetGatewayInstances(List.of("gw-1"));
        def.setTargetTriggerTypes(List.of("A01"));
        def.setActivityIds(Map.of(0, "act-1", 1, "act-2"));

        TaskSequenceImplementation impl = new TaskSequenceImplementation(def);
        assertThat(impl.getSequenceId()).isEqualTo("seq-impl-1");
        assertThat(impl.getSequenceName()).isEqualTo("Implementation Test 1");
        assertThat(impl.getSequenceDescription()).isEqualTo("Description");
        assertThat(impl.getTargetGatewayInstances()).containsExactly("gw-1");
        assertThat(impl.getTargetTriggerTypes()).containsExactly("A01");
        assertThat(impl.getActivityIds()).containsEntry(0, "act-1").containsEntry(1, "act-2");

        TaskSequenceDefinition backToDef = impl.toDefinition();
        assertThat(backToDef.getSequenceId()).isEqualTo(impl.getSequenceId());
        assertThat(backToDef.getSequenceName()).isEqualTo(impl.getSequenceName());
    }

    @Test
    @DisplayName("Should manage activities map, order entries, and list views correctly")
    void testActivityMapManagement() {
        TaskSequenceImplementation impl = new TaskSequenceImplementation("seq-act", "Activity Test");

        PatientIdentityUpdate idActivity = new PatientIdentityUpdate();
        PatientDemographicsUpdate demoActivity = new PatientDemographicsUpdate();

        impl.addActivity(0, idActivity);
        impl.addActivity(1, demoActivity);

        assertThat(impl.size()).isEqualTo(2);
        assertThat(impl.getActivityCount()).isEqualTo(2);
        assertThat(impl.isEmpty()).isFalse();
        assertThat(impl.getActivity(0)).isSameAs(idActivity);
        assertThat(impl.getActivity(1)).isSameAs(demoActivity);
        assertThat(impl.getActivityById(PatientIdentityUpdate.DEFAULT_ACTIVITY_ID)).isSameAs(idActivity);
        assertThat(impl.containsActivity(PatientDemographicsUpdate.DEFAULT_ACTIVITY_ID)).isTrue();
        assertThat(impl.getActivityList()).containsExactly(idActivity, demoActivity);

        assertThat(impl.getActivityIds()).containsEntry(0, PatientIdentityUpdate.DEFAULT_ACTIVITY_ID);
        assertThat(impl.getActivityIds()).containsEntry(1, PatientDemographicsUpdate.DEFAULT_ACTIVITY_ID);

        // Remove activity
        impl.removeActivity(0);
        assertThat(impl.size()).isEqualTo(1);
        assertThat(impl.getActivity(0)).isNull();
        assertThat(impl.getActivity(1)).isSameAs(demoActivity);

        impl.clearActivities();
        assertThat(impl.isEmpty()).isTrue();
        assertThat(impl.getActivityIds()).isEmpty();
    }

    @Test
    @DisplayName("Should configure chained endpoints between sequential activities")
    void testConfigureChainedEndpoints() {
        TaskSequenceImplementation impl = new TaskSequenceImplementation("seq-chain", "Chained Sequence");

        MessageQueueToExchangeConduit conduit = new MessageQueueToExchangeConduit();
        PatientIdentityUpdate idUpdate = new PatientIdentityUpdate();
        PatientDemographicsUpdate demoUpdate = new PatientDemographicsUpdate();

        impl.setActivities(new TaskProcessingActivity[]{conduit, idUpdate, demoUpdate});
        impl.setOutputEndpoint("mock:result");
        impl.configureChainedEndpoints();

        assertThat(conduit.getInputEndpoint()).isEqualTo("jms:queue:task.event.queue");
        assertThat(conduit.getOutputEndpoint()).isEqualTo("direct:seq-seq-chain-step-2");
        assertThat(idUpdate.getInputEndpoint()).isEqualTo("direct:seq-seq-chain-step-2");
        assertThat(idUpdate.getOutputEndpoint()).isEqualTo("direct:seq-seq-chain-step-3");
        assertThat(demoUpdate.getInputEndpoint()).isEqualTo("direct:seq-seq-chain-step-3");
        assertThat(demoUpdate.getOutputEndpoint()).isEqualTo("mock:result");
    }

    @Test
    @DisplayName("Should execute pipeline route through activities and apply gateway/trigger filter")
    void testPipelineRouteExecution() throws Exception {
        TaskSequenceImplementation impl = new TaskSequenceImplementation("seq-pipeline", "Pipeline Test");
        impl.setTargetGatewayInstances(List.of("pas-gw"));
        impl.setTargetTriggerTypes(List.of("A01"));
        impl.setInputEndpoint("direct:patient-seq-test-in");
        impl.setOutputEndpoint("mock:end-of-pipeline");

        PatientIdentityUpdate idUpdate = new PatientIdentityUpdate();
        PatientDemographicsUpdate demoUpdate = new PatientDemographicsUpdate();

        impl.setActivities(new TaskProcessingActivity[]{idUpdate, demoUpdate});
        impl.configureChainedEndpoints();

        impl.registerRoutes(camelContext);
        camelContext.addRoutes(impl.createSequencePipelineRoute());
        camelContext.start();

        // 1. Send matching event
        TaskEvent matchingEvent = new TaskEvent("task-1", "comm-1", "A01", "PROCESS", "requested");
        matchingEvent.setGatewayInstanceId("pas-gw");

        Exchange matchingExchange = camelContext.createProducerTemplate().send(impl.getPipelineInputEndpoint(), exchange -> {
            exchange.getMessage().setHeader("HIE_GATEWAY_INSTANCE", "pas-gw");
            exchange.getMessage().setHeader("HIE_TRIGGER_TYPE", "A01");
            exchange.getMessage().setBody(matchingEvent);
        });

        assertThat(matchingExchange.getException()).isNull();

        // 2. Send non-matching event (should be filtered out)
        Exchange nonMatchingExchange = camelContext.createProducerTemplate().send(impl.getPipelineInputEndpoint(), exchange -> {
            exchange.getMessage().setHeader("HIE_GATEWAY_INSTANCE", "other-gw");
            exchange.getMessage().setHeader("HIE_TRIGGER_TYPE", "A03");
            exchange.getMessage().setBody("non-matching");
        });

        assertThat(nonMatchingExchange.getException()).isNull();
    }

    @Test
    @DisplayName("Should execute pipeline route without explicit outputEndpoint configured without error")
    void testPipelineRouteExecutionWithoutOutputEndpoint() throws Exception {
        TaskSequenceImplementation impl = new TaskSequenceImplementation("seq-pipeline-no-out", "Pipeline Test No Out");
        impl.setTargetGatewayInstances(List.of("pas-gw"));
        impl.setTargetTriggerTypes(List.of("A01"));
        impl.setInputEndpoint("direct:patient-seq-test-no-out-in");

        PatientIdentityUpdate idUpdate = new PatientIdentityUpdate();
        PatientDemographicsUpdate demoUpdate = new PatientDemographicsUpdate();

        impl.setActivities(new TaskProcessingActivity[]{idUpdate, demoUpdate});
        impl.configureChainedEndpoints();

        impl.registerRoutes(camelContext);
        camelContext.addRoutes(impl.createSequencePipelineRoute());
        camelContext.start();

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-TEST-01|P|2.4\r" +
                "PID|||MRN-TEST-999^^^HOSP||DOE^JANE||19850412|F|||100 MAIN ST^^BOSTON^MA^02115^USA";

        Exchange matchingExchange = camelContext.createProducerTemplate().send(impl.getPipelineInputEndpoint(), exchange -> {
            exchange.getMessage().setHeader("HIE_GATEWAY_INSTANCE", "pas-gw");
            exchange.getMessage().setHeader("HIE_TRIGGER_TYPE", "A01");
            exchange.getMessage().setHeader("HIE_RAW_MESSAGE", hl7);
            exchange.getMessage().setBody(hl7);
        });

        assertThat(matchingExchange.getException()).isNull();
        assertThat(matchingExchange.getMessage().getHeader(PatientDemographicsUpdate.HEADER_PATIENT_DEMOGRAPHICS_UPDATED)).isEqualTo(true);
    }

    @Test
    @DisplayName("Should register all activities into CamelContext")
    void testRegisterRoutes() throws Exception {
        TaskSequenceImplementation impl = new TaskSequenceImplementation("seq-reg", "Register Test");
        PatientIdentityUpdate idUpdate = new PatientIdentityUpdate();
        impl.addActivity(idUpdate);

        impl.registerRoutes(camelContext);
        camelContext.start();

        assertThat(camelContext.getRoute("activity-patient-identity-update")).isNotNull();
    }
}
