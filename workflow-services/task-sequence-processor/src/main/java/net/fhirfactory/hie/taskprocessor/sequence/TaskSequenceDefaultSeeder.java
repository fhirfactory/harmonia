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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.model.topic.TopicSubscription;
import net.fhirfactory.hie.taskprocessor.service.TaskSequenceService;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Seeds default {@link TaskSequence} definitions into {@link TaskSequenceService} and the Infinispan cache.
 * <p>
 * By default, only the {@code seq-patient-identity-pipeline} sequence is loaded and seeded.
 */
@ApplicationScoped
public class TaskSequenceDefaultSeeder {

    private static final Logger log = LoggerFactory.getLogger(TaskSequenceDefaultSeeder.class);

    public static final String DEFAULT_PATIENT_IDENTITY_SEQUENCE_ID = "seq-patient-identity-pipeline";
    public static final String DEFAULT_PATIENT_IDENTITY_SEQUENCE_NAME = "Patient Identity Update Sequence";

    @Inject
    private TaskSequenceService sequenceService;

    public TaskSequenceDefaultSeeder() {
    }

    public TaskSequenceDefaultSeeder(TaskSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * Seeds initial standard default TaskSequence configurations in Infinispan cache.
     * By default, seeds only the {@code seq-patient-identity-pipeline} sequence.
     *
     * @param activityIndex index of available CDI activities
     * @return list of seeded {@link TaskSequence} instances
     */
    public List<TaskSequence> seedDefaultSequences(Map<String, TaskProcessingActivity> activityIndex) {
        List<TaskSequence> seeds = new ArrayList<>();

        TaskSequence patientIdSeq = createPatientIdentityUpdateSequence(activityIndex);
        seeds.add(patientIdSeq);

        if (sequenceService != null) {
            sequenceService.save(patientIdSeq);
        }

        log.info("TaskSequenceDefaultSeeder seeded {} default TaskSequence configuration(s) to Infinispan cache: [{}]",
                seeds.size(), patientIdSeq.getSequenceId());
        return seeds;
    }

    /**
     * Constructs the default Patient Identity Update Sequence ({@code seq-patient-identity-pipeline}).
     *
     * @param activityIndex index of available CDI activities
     * @return initialized {@link TaskSequence}
     */
    public TaskSequence createPatientIdentityUpdateSequence(Map<String, TaskProcessingActivity> activityIndex) {
        TaskSequence patientIdSeq = new TaskSequence(DEFAULT_PATIENT_IDENTITY_SEQUENCE_ID, DEFAULT_PATIENT_IDENTITY_SEQUENCE_NAME);
        patientIdSeq.setSequenceDescription("Extracts, normalizes, and updates patient identity and demographics across all clinical messages");
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
        }

        if (patientSeqActivities.isEmpty()) {
            patientSeqActivities.put(0, MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID);
            patientSeqActivities.put(1, "patient-identity-update");
            patientSeqActivities.put(2, "patient-demographics-update");
        }

        patientIdSeq.setActivityIds(patientSeqActivities);
        return patientIdSeq;
    }

    public TaskSequenceService getSequenceService() {
        return sequenceService;
    }

    public void setSequenceService(TaskSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }
}
