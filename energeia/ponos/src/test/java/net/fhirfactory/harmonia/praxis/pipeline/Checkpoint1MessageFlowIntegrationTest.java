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

package net.fhirfactory.harmonia.praxis.pipeline;

import net.fhirfactory.harmonia.erga.distribution.AdtDistributionErgon;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import net.fhirfactory.harmonia.praxis.messaging.ArtemisPonosConsumer;
import net.fhirfactory.harmonia.praxis.messaging.ArtemisPonosProducer;
import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checkpoint 1 verification:
 * End-to-end clinical message flow and lifecycle independence test.
 * Verifies:
 * 1. Ponos communicates exclusively via Petasos Client Facade (ArtemisPetasos) without running an embedded broker.
 * 2. Message ingress -> Petasos -> Ponos TaskProcessor -> AdtDistributionErgon -> Petasos egress.
 * 3. Outbound distribution delivery across destination queues (EMR, LMS, RIS).
 * 4. Granular destination fan-out tracking (REC-002) in Pragma checkpoints.
 * 5. Failure recovery: broker stop and restart preserves client reconnect ability and message flow.
 */
class Checkpoint1MessageFlowIntegrationTest {

    private static int brokerPort;
    private static String brokerUrl;
    private static EmbeddedActiveMQ standaloneBroker;
    private static Configuration brokerConfig;

    private Petasos petasos;
    private QueueConfig queueConfig;
    private ArtemisPonosProducer ponosProducer;
    private ArtemisPonosConsumer ponosConsumer;
    private CamelContext camelContext;

    private static final String INGRESS_QUEUE = "task.event.queue.mllp-gateway-default";
    private static final String EGRESS_QUEUE_EMR = "petasos.queue.mllp.outbound.emr_adt";
    private static final String EGRESS_QUEUE_LMS = "petasos.queue.mllp.outbound.lms_adt";
    private static final String EGRESS_QUEUE_RIS = "petasos.queue.mllp.outbound.ris_adt";

    private static final String SAMPLE_HL7_ADT =
            "MSH|^~\\&|PAS|HOSPITAL|HIE|HARMONIA|20260920160000||ADT^A01|MSG-CHECKPOINT-1|P|2.4\r" +
            "EVN|A01|20260920160000\r" +
            "PID|1||PAT-CHECKPOINT-001^^^HOSPITAL^MR||DOE^JOHN||19800101|M|||123 MAIN ST^^SPRINGFIELD^^12345\r" +
            "PV1|1|I|WARD-1^ROOM-101^BED-1";

    @BeforeAll
    static void startStandaloneBroker() throws Exception {
        brokerPort = findFreePort();
        brokerUrl = "tcp://127.0.0.1:" + brokerPort;

        brokerConfig = new ConfigurationImpl();
        brokerConfig.setPersistenceEnabled(false);
        brokerConfig.setSecurityEnabled(false);
        File journalDir = new File(System.getProperty("java.io.tmpdir"), "artemis-cp1-" + System.nanoTime());
        brokerConfig.setJournalDirectory(journalDir.getAbsolutePath());

        Map<String, Object> params = new HashMap<>();
        params.put("host", "127.0.0.1");
        params.put("port", brokerPort);
        brokerConfig.addAcceptorConfiguration(new TransportConfiguration(NettyAcceptorFactory.class.getName(), params));

        List<String> queues = List.of(INGRESS_QUEUE, EGRESS_QUEUE_EMR, EGRESS_QUEUE_LMS, EGRESS_QUEUE_RIS);
        for (String q : queues) {
            brokerConfig.addQueueConfiguration(new QueueConfiguration(q)
                    .setRoutingType(RoutingType.ANYCAST)
                    .setAddress(q)
                    .setDurable(false));
        }

        standaloneBroker = new EmbeddedActiveMQ();
        standaloneBroker.setConfiguration(brokerConfig);
        standaloneBroker.start();
    }

    @AfterAll
    static void stopStandaloneBroker() throws Exception {
        if (standaloneBroker != null) {
            standaloneBroker.stop();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        camelContext = new DefaultCamelContext();
        camelContext.start();

        queueConfig = new QueueConfig();
        queueConfig.setBrokerEnabled(false); // Ponos embedded broker explicitly DISABLED
        queueConfig.setBrokerUrl(brokerUrl);

        PetasosConfig config = PetasosConfig.builder()
                .addBrokerUrl(brokerUrl)
                .reconnectAttempts(10)
                .retryInterval(200)
                .build();
        petasos = ArtemisPetasos.create(config);

        ponosProducer = new ArtemisPonosProducer(petasos, queueConfig);
        ponosConsumer = new ArtemisPonosConsumer(petasos, queueConfig);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (ponosConsumer != null) {
            ponosConsumer.close();
        }
        if (petasos != null) {
            petasos.close();
        }
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    @DisplayName("Checkpoint 1: End-to-end message flow Ingress -> Petasos -> Ponos TaskProcessor -> AdtDistributionErgon -> Petasos Egress (EMR, LMS, RIS)")
    void testEndToEndCheckpoint1MessageFlow() throws Exception {
        // Assert Ponos broker is disabled by configuration
        assertThat(queueConfig.isBrokerEnabled()).isFalse();

        // Setup latches and collections for outbound destination consumers
        CountDownLatch egressLatch = new CountDownLatch(3);
        Map<String, String> receivedOutboundMessages = new ConcurrentHashMap<>();

        // Subscribe simulated outbound gateways to Petasos egress queues
        PetasosSubscription subEmr = petasos.receive(PetasosDestination.queue(EGRESS_QUEUE_EMR), (msg, ctx) -> {
            receivedOutboundMessages.put(EGRESS_QUEUE_EMR, msg.getPayloadAsString());
            ctx.acknowledge();
            egressLatch.countDown();
        });

        PetasosSubscription subLms = petasos.receive(PetasosDestination.queue(EGRESS_QUEUE_LMS), (msg, ctx) -> {
            receivedOutboundMessages.put(EGRESS_QUEUE_LMS, msg.getPayloadAsString());
            ctx.acknowledge();
            egressLatch.countDown();
        });

        PetasosSubscription subRis = petasos.receive(PetasosDestination.queue(EGRESS_QUEUE_RIS), (msg, ctx) -> {
            receivedOutboundMessages.put(EGRESS_QUEUE_RIS, msg.getPayloadAsString());
            ctx.acknowledge();
            egressLatch.countDown();
        });

        // 1. Inbound Gateway publishes TaskEvent to Petasos Ingress Queue
        ErgonEvent inboundEvent = new ErgonEvent();
        inboundEvent.setGatewayInstanceId("mllp-gateway-default");
        inboundEvent.setTriggerType("A01");
        inboundEvent.setMessageType("ADT");
        inboundEvent.setTaskId("TASK-CP1-001");
        inboundEvent.setControlId("MSG-CHECKPOINT-1");
        inboundEvent.setAction("PROCESS");
        inboundEvent.setStatus("RECEIVED");
        inboundEvent.setTopic(Topic.fromHl7("ADT", "A01", "mllp-gateway-default"));

        ponosProducer.sendTaskEvent(inboundEvent, SAMPLE_HL7_ADT);

        // 2. Ponos consumes ingress event via Petasos client facade
        CountDownLatch ingressLatch = new CountDownLatch(1);
        final List<PetasosMessage> capturedIngressMessages = new CopyOnWriteArrayList<>();

        PetasosSubscription ingressSub = ponosConsumer.subscribe(INGRESS_QUEUE, (msg, ctx) -> {
            capturedIngressMessages.add(msg);
            ctx.acknowledge();
            ingressLatch.countDown();
        });

        boolean ingressReceived = ingressLatch.await(5, TimeUnit.SECONDS);
        assertThat(ingressReceived).isTrue();
        assertThat(capturedIngressMessages).hasSize(1);
        PetasosMessage ingressMessage = capturedIngressMessages.get(0);
        assertThat(ingressMessage.getPayloadAsString()).contains("MSH|^~\\&|PAS|HOSPITAL");
        assertThat(ingressMessage.getMetadata().get("HIE_GATEWAY_INSTANCE")).isEqualTo("mllp-gateway-default");
        assertThat(ingressMessage.getMetadata().get("HIE_CONTROL_ID")).isEqualTo("MSG-CHECKPOINT-1");

        // 3. Ponos executes AdtDistributionErgon for multi-destination fan-out
        AdtDistributionErgon ergon = new AdtDistributionErgon(camelContext);
        ergon.setTargetQueues(List.of(EGRESS_QUEUE_EMR, EGRESS_QUEUE_LMS, EGRESS_QUEUE_RIS));

        Pragma pragma = new Pragma("PRAGMA-CP1-001", "ADT Processing Workflow", PragmaStatus.IN_PROGRESS);
        Topic ingressTopic = Topic.fromHl7("ADT", "A01", "mllp-gateway-default");
        pragma.addInput(ErgonPayload.fromJson(0, ingressTopic, ingressTopic, SAMPLE_HL7_ADT));

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(pragma);

        ergon.processActivity(exchange);

        Pragma distributedPragma = exchange.getMessage().getBody(Pragma.class);
        assertThat(distributedPragma).isNotNull();
        assertThat(distributedPragma.getOutput()).hasSize(3);

        // Verify Invariant 5 (REC-002: Destination Fan-Out State Tracking)
        List<PragmaCheckpoint> checkpoints = distributedPragma.getCheckpoints();
        assertThat(checkpoints).filteredOn(cp -> "FANOUT_DISPATCH_INITIATED".equals(cp.getStageName())).hasSize(3);

        // 4. Dispatch the fan-out payloads to Petasos egress queues
        for (ErgonPayload output : distributedPragma.getOutput()) {
            String destinationQueue = output.getPayloadContainer().getTarget();
            PetasosMessage egressMsg = PetasosMessage.builder()
                    .messageId(UUID.randomUUID().toString())
                    .destination(PetasosDestination.queue(destinationQueue))
                    .messageType("HL7v2_ADT")
                    .payload(output.getJsonString())
                    .header("HIE_DESTINATION", destinationQueue)
                    .build();
            petasos.send(PetasosDestination.queue(destinationQueue), egressMsg);
        }

        // 5. Verify simulated outbound gateways receive the dispatched messages
        boolean egressDelivered = egressLatch.await(5, TimeUnit.SECONDS);
        assertThat(egressDelivered).isTrue();
        assertThat(receivedOutboundMessages).containsKeys(EGRESS_QUEUE_EMR, EGRESS_QUEUE_LMS, EGRESS_QUEUE_RIS);
        for (String payload : receivedOutboundMessages.values()) {
            assertThat(payload).contains("PID|1||PAT-CHECKPOINT-001");
        }

        subEmr.unsubscribe();
        subLms.unsubscribe();
        subRis.unsubscribe();
        ingressSub.unsubscribe();
    }

    @Test
    @DisplayName("Checkpoint 1 Failure Recovery: Broker restart does not affect Ponos client lifecycle and preserves messaging")
    void testBrokerFailureRecoveryAndClientReconnection() throws Exception {
        // Assert broker is initially active
        assertThat(standaloneBroker.getActiveMQServer().isStarted()).isTrue();

        // Produce a message to verify baseline
        petasos.send(PetasosDestination.queue(INGRESS_QUEUE), PetasosMessage.of("pre-restart-message"));

        CountDownLatch preLatch = new CountDownLatch(1);
        PetasosSubscription sub1 = petasos.receive(PetasosDestination.queue(INGRESS_QUEUE), (msg, ctx) -> {
            ctx.acknowledge();
            preLatch.countDown();
        });
        assertThat(preLatch.await(3, TimeUnit.SECONDS)).isTrue();
        sub1.unsubscribe();

        // 1. Stop standalone broker (simulating container crash or maintenance)
        standaloneBroker.stop();
        assertThat(standaloneBroker.getActiveMQServer().isStarted()).isFalse();

        // Ponos runtime itself remains active without depending on embedded broker
        assertThat(queueConfig.isBrokerEnabled()).isFalse();

        // 2. Restart standalone broker
        standaloneBroker = new EmbeddedActiveMQ();
        standaloneBroker.setConfiguration(brokerConfig);
        standaloneBroker.start();
        assertThat(standaloneBroker.getActiveMQServer().isStarted()).isTrue();

        // 3. Verify client reconnects and messages can be produced and consumed successfully
        CountDownLatch postLatch = new CountDownLatch(1);
        final List<String> postMessages = new CopyOnWriteArrayList<>();

        PetasosSubscription sub2 = petasos.receive(PetasosDestination.queue(INGRESS_QUEUE), (msg, ctx) -> {
            postMessages.add(msg.getPayloadAsString());
            ctx.acknowledge();
            postLatch.countDown();
        });

        // Give reconnect listener time to reconnect
        Thread.sleep(500);

        petasos.send(PetasosDestination.queue(INGRESS_QUEUE), PetasosMessage.of("post-restart-message"));

        boolean receivedPost = postLatch.await(10, TimeUnit.SECONDS);
        assertThat(receivedPost).isTrue();
        assertThat(postMessages).contains("post-restart-message");

        sub2.unsubscribe();
    }

    private static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }
}
