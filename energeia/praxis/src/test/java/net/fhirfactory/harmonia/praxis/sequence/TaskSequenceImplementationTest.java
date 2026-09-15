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

package net.fhirfactory.harmonia.praxis.sequence;

import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.praxis.PraxisDefinition;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.infrastructure.MessageQueueToExchangeConduit;
import net.fhirfactory.harmonia.erga.patient.demographics.PatientDemographicsUpdateErgon;
import net.fhirfactory.harmonia.erga.patient.identity.PatientIdentityUpdateErgon;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
        PraxisDefinition def = new PraxisDefinition("seq-impl-1", "Implementation Test 1");
        def.setSequenceDescription("Description");
        def.setTargetGatewayInstances(List.of("gw-1"));
        def.setTargetTriggerTypes(List.of("A01"));
        def.setActivityIds(Map.of(0, "act-1", 1, "act-2"));

        PraxisImplementation impl = new PraxisImplementation(def);
        assertThat(impl.getPraxisId()).isEqualTo("seq-impl-1");
        assertThat(impl.getPraxisName()).isEqualTo("Implementation Test 1");
        assertThat(impl.getSequenceDescription()).isEqualTo("Description");
        assertThat(impl.getTargetGatewayInstances()).containsExactly("gw-1");
        assertThat(impl.getTargetTriggerTypes()).containsExactly("A01");
        assertThat(impl.getActivityIds()).containsEntry(0, "act-1").containsEntry(1, "act-2");

        PraxisDefinition backToDef = impl.toDefinition();
        assertThat(backToDef.getPraxisId()).isEqualTo(impl.getPraxisId());
        assertThat(backToDef.getPraxisName()).isEqualTo(impl.getPraxisName());
    }

    @Test
    @DisplayName("Should manage activities map, order entries, and list views correctly")
    void testActivityMapManagement() {
        PraxisImplementation impl = new PraxisImplementation("seq-act", "Activity Test");

        PatientIdentityUpdateErgon idActivity = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon demoActivity = new PatientDemographicsUpdateErgon();

        impl.addActivity(0, idActivity);
        impl.addActivity(1, demoActivity);

        assertThat(impl.size()).isEqualTo(2);
        assertThat(impl.getActivityCount()).isEqualTo(2);
        assertThat(impl.isEmpty()).isFalse();
        assertThat(impl.getActivity(0)).isSameAs(idActivity);
        assertThat(impl.getActivity(1)).isSameAs(demoActivity);
        assertThat(impl.getActivityById(PatientIdentityUpdateErgon.DEFAULT_ACTIVITY_ID)).isSameAs(idActivity);
        assertThat(impl.containsActivity(PatientDemographicsUpdateErgon.DEFAULT_ACTIVITY_ID)).isTrue();
        assertThat(impl.getActivityList()).containsExactly(idActivity, demoActivity);

        assertThat(impl.getActivityIds()).containsEntry(0, PatientIdentityUpdateErgon.DEFAULT_ACTIVITY_ID);
        assertThat(impl.getActivityIds()).containsEntry(1, PatientDemographicsUpdateErgon.DEFAULT_ACTIVITY_ID);

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
        PraxisImplementation impl = new PraxisImplementation("seq-chain", "Chained Sequence");

        MessageQueueToExchangeConduit conduit = new MessageQueueToExchangeConduit();
        PatientIdentityUpdateErgon idUpdate = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon demoUpdate = new PatientDemographicsUpdateErgon();

        impl.setActivities(new ErgonBase[]{conduit, idUpdate, demoUpdate});
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
        PraxisImplementation impl = new PraxisImplementation("seq-pipeline", "Pipeline Test");
        impl.setTargetGatewayInstances(List.of("pas-gw"));
        impl.setTargetTriggerTypes(List.of("A01"));
        impl.setInputEndpoint("direct:patient-seq-test-in");
        impl.setOutputEndpoint("mock:end-of-pipeline");

        PatientIdentityUpdateErgon idUpdate = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon demoUpdate = new PatientDemographicsUpdateErgon();

        impl.setActivities(new ErgonBase[]{idUpdate, demoUpdate});
        impl.configureChainedEndpoints();

        impl.registerRoutes(camelContext);
        camelContext.addRoutes(impl.createSequencePipelineRoute());
        camelContext.start();

        // 1. Send matching event
        ErgonEvent matchingEvent = new ErgonEvent("task-1", "comm-1", "A01", "PROCESS", "requested");
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
        PraxisImplementation impl = new PraxisImplementation("seq-pipeline-no-out", "Pipeline Test No Out");
        impl.setTargetGatewayInstances(List.of("pas-gw"));
        impl.setTargetTriggerTypes(List.of("A01"));
        impl.setInputEndpoint("direct:patient-seq-test-no-out-in");

        PatientIdentityUpdateErgon idUpdate = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon demoUpdate = new PatientDemographicsUpdateErgon();

        impl.setActivities(new ErgonBase[]{idUpdate, demoUpdate});
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
        assertThat(matchingExchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DEMOGRAPHICS_UPDATED)).isEqualTo(true);
    }

    @Test
    @DisplayName("Should register all activities into CamelContext")
    void testRegisterRoutes() throws Exception {
        PraxisImplementation impl = new PraxisImplementation("seq-reg", "Register Test");
        PatientIdentityUpdateErgon idUpdate = new PatientIdentityUpdateErgon();
        impl.addActivity(idUpdate);

        impl.registerRoutes(camelContext);
        camelContext.start();

        assertThat(camelContext.getRoute("activity-patient-identity-update")).isNotNull();
    }
}
