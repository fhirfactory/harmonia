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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.topic.TopicSubscription;
import net.fhirfactory.harmonia.praxis.service.PraxisService;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.infrastructure.MessageQueueToExchangeConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Seeds default {@link Praxis} definitions into {@link PraxisService} and the Infinispan cache.
 * <p>
 * By default, only the {@code seq-patient-identity-pipeline} sequence is loaded and seeded.
 */
@ApplicationScoped
public class TaskSequenceDefaultSeeder {

    private static final Logger log = LoggerFactory.getLogger(TaskSequenceDefaultSeeder.class);

    public static final String DEFAULT_PATIENT_IDENTITY_SEQUENCE_ID = "seq-patient-identity-pipeline";
    public static final String DEFAULT_PATIENT_IDENTITY_SEQUENCE_NAME = "Patient Identity Update Sequence";

    public static final String DEFAULT_PROVIDER_REGISTRY_SEQUENCE_ID = "seq-provider-registry-change-pipeline";
    public static final String DEFAULT_PROVIDER_REGISTRY_SEQUENCE_NAME = "Provider Registry Change Pipeline Sequence";

    @Inject
    private PraxisService sequenceService;

    public TaskSequenceDefaultSeeder() {
    }

    public TaskSequenceDefaultSeeder(PraxisService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * Seeds initial standard default TaskSequence configurations in Infinispan cache.
     * By default, seeds only the {@code seq-patient-identity-pipeline} sequence.
     *
     * @param activityIndex index of available CDI activities
     * @return list of seeded {@link Praxis} instances
     */
    public List<Praxis> seedDefaultSequences(Map<String, ErgonBase> activityIndex) {
        List<Praxis> seeds = new ArrayList<>();

        Praxis patientIdSeq = createPatientIdentityUpdateSequence(activityIndex);
        seeds.add(patientIdSeq);

        if (sequenceService != null) {
            sequenceService.save(patientIdSeq);
        }

        log.info("TaskSequenceDefaultSeeder seeded {} default TaskSequence configuration(s) to Infinispan cache: [{}]",
                seeds.size(), patientIdSeq.getPraxisId());
        return seeds;
    }

    /**
     * Seeds the Provider Registry TaskSequence ({@code seq-provider-registry-change-pipeline}) into cache.
     */
    public Praxis seedProviderRegistrySequence(Map<String, ErgonBase> activityIndex) {
        Praxis prSeq = createProviderRegistryChangeSequence(activityIndex);
        if (sequenceService != null) {
            sequenceService.save(prSeq);
        }
        log.info("TaskSequenceDefaultSeeder seeded Provider Registry TaskSequence [{}] to Infinispan cache",
                prSeq.getPraxisId());
        return prSeq;
    }

    /**
     * Seeds all standard Paradeigma TaskSequences (ADT fanout, ORM routing, ORU processing) into cache.
     */
    public List<Praxis> seedParadeigmaSequences(Map<String, ErgonBase> activityIndex) {
        List<Praxis> seeds = new ArrayList<>();

        Praxis patientIdSeq = createPatientIdentityUpdateSequence(activityIndex);
        seeds.add(patientIdSeq);

        Praxis prSeq = createProviderRegistryChangeSequence(activityIndex);
        seeds.add(prSeq);

        Praxis orderRoutingSeq = createOrderRoutingSequence(activityIndex);
        seeds.add(orderRoutingSeq);

        Praxis resultSeq = createResultProcessingSequence(activityIndex);
        seeds.add(resultSeq);

        if (sequenceService != null) {
            for (Praxis p : seeds) {
                sequenceService.save(p);
            }
        }

        log.info("TaskSequenceDefaultSeeder seeded {} Paradeigma TaskSequence configuration(s) to Infinispan cache",
                seeds.size());
        return seeds;
    }

    /**
     * Constructs the Provider Registry Change Pipeline Sequence ({@code seq-provider-registry-change-pipeline}).
     *
     * @param activityIndex index of available CDI activities
     * @return initialized {@link Praxis}
     */
    public Praxis createProviderRegistryChangeSequence(Map<String, ErgonBase> activityIndex) {
        Praxis prSeq = new Praxis(DEFAULT_PROVIDER_REGISTRY_SEQUENCE_ID, DEFAULT_PROVIDER_REGISTRY_SEQUENCE_NAME);
        prSeq.setDescription("Processes, validates, approves and persists asynchronous FHIR Provider Registry changes");
        prSeq.setVersion("1.0.0");
        prSeq.setTargetGatewayInstances(List.of("*"));
        prSeq.setTargetTriggerTypes(List.of("POST", "PUT", "CREATE", "UPDATE", "CHANGE", "*"));

        TopicSubscription sub = new TopicSubscription("Health", "FHIR", "R5", "*", "*");
        prSeq.setTopicSubscriptions(List.of(sub));

        Map<Integer, String> activities = new TreeMap<>();
        int order = 0;
        String[] ergonIds = new String[]{
                "practitioner-change-ergon",
                "practitioner-role-change-ergon",
                "organization-change-ergon",
                "location-change-ergon",
                "healthcare-service-change-ergon",
                "endpoint-change-ergon",
                "group-change-ergon"
        };
        if (activityIndex != null) {
            for (String ergonId : ergonIds) {
                if (activityIndex.containsKey(ergonId)) {
                    activities.put(order++, ergonId);
                }
            }
        }
        if (activities.isEmpty()) {
            for (String ergonId : ergonIds) {
                activities.put(order++, ergonId);
            }
        }
        prSeq.setActivityIds(activities);
        return prSeq;
    }

    /**
     * Constructs the default Patient Identity Update Sequence ({@code seq-patient-identity-pipeline}).
     *
     * @param activityIndex index of available CDI activities
     * @return initialized {@link Praxis}
     */
    public Praxis createPatientIdentityUpdateSequence(Map<String, ErgonBase> activityIndex) {
        Praxis patientIdSeq = new Praxis(DEFAULT_PATIENT_IDENTITY_SEQUENCE_ID, DEFAULT_PATIENT_IDENTITY_SEQUENCE_NAME);
        patientIdSeq.setDescription("Extracts, normalizes, and updates patient identity and demographics across all clinical messages and distributes ADT");
        patientIdSeq.setVersion("1.0.0");
        patientIdSeq.setTopicSubscriptions(List.of(TopicSubscription.forAll()));
        patientIdSeq.setTargetGatewayInstances(List.of("*"));
        patientIdSeq.setTargetTriggerTypes(List.of("*"));

        Map<Integer, String> patientSeqActivities = new TreeMap<>();
        int pOrder = 0;
        if (activityIndex != null) {
            if (activityIndex.containsKey(MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID)) {
                patientSeqActivities.put(pOrder++, MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
            } else if (activityIndex.containsKey("message-queue-conduit")) {
                patientSeqActivities.put(pOrder++, "message-queue-conduit");
            }
            if (activityIndex.containsKey("patient-identity-update")) {
                patientSeqActivities.put(pOrder++, "patient-identity-update");
            }
            if (activityIndex.containsKey("patient-demographics-update")) {
                patientSeqActivities.put(pOrder++, "patient-demographics-update");
            }
            if (activityIndex.containsKey("adt-distribution")) {
                patientSeqActivities.put(pOrder++, "adt-distribution");
            }
        }

        if (patientSeqActivities.isEmpty()) {
            patientSeqActivities.put(0, MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
            patientSeqActivities.put(1, "patient-identity-update");
            patientSeqActivities.put(2, "patient-demographics-update");
        }

        patientIdSeq.setActivityIds(patientSeqActivities);
        return patientIdSeq;
    }

    public Praxis createOrderRoutingSequence(Map<String, ErgonBase> activityIndex) {
        Praxis orderSeq = new Praxis("seq-order-routing-pipeline", "Orders Routing Task Sequence");
        orderSeq.setDescription("Deterministically routes ORM orders based on OBR-4 Universal Service Identifier");
        orderSeq.setVersion("1.0.0");
        orderSeq.setTargetGatewayInstances(List.of("*"));
        orderSeq.setTargetTriggerTypes(List.of("ORM^O01", "O01", "ORM"));

        Map<Integer, String> activities = new TreeMap<>();
        int order = 0;
        if (activityIndex != null) {
            if (activityIndex.containsKey(MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID)) {
                activities.put(order++, MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
            }
            if (activityIndex.containsKey("orm-routing")) {
                activities.put(order++, "orm-routing");
            }
        }
        if (activities.isEmpty()) {
            activities.put(0, MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
            activities.put(1, "orm-routing");
        }
        orderSeq.setActivityIds(activities);
        return orderSeq;
    }

    public Praxis createResultProcessingSequence(Map<String, ErgonBase> activityIndex) {
        Praxis resSeq = new Praxis("seq-result-processing-pipeline", "Results Processing Task Sequence");
        resSeq.setDescription("Processes and records clinical observation results (ORU^R01)");
        resSeq.setVersion("1.0.0");
        resSeq.setTargetGatewayInstances(List.of("*"));
        resSeq.setTargetTriggerTypes(List.of("ORU^R01", "R01", "ORU"));

        Map<Integer, String> activities = new TreeMap<>();
        int order = 0;
        if (activityIndex != null) {
            if (activityIndex.containsKey(MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID)) {
                activities.put(order++, MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
            }
            if (activityIndex.containsKey("oru-processing")) {
                activities.put(order++, "oru-processing");
            }
        }
        if (activities.isEmpty()) {
            activities.put(0, MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
            activities.put(1, "oru-processing");
        }
        resSeq.setActivityIds(activities);
        return resSeq;
    }

    public PraxisService getSequenceService() {
        return sequenceService;
    }

    public void setSequenceService(PraxisService sequenceService) {
        this.sequenceService = sequenceService;
    }
}
