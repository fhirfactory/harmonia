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

import net.fhirfactory.harmonia.praxis.service.PraxisService;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.infrastructure.MessageQueueToExchangeConduit;
import net.fhirfactory.harmonia.erga.patient.demographics.PatientDemographicsUpdateErgon;
import net.fhirfactory.harmonia.erga.patient.identity.PatientIdentityUpdateErgon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TaskSequenceDefaultSeederTest {

    private PraxisService sequenceService;
    private TaskSequenceDefaultSeeder seeder;

    @BeforeEach
    void setUp() {
        sequenceService = new PraxisService();
        sequenceService.init();
        sequenceService.clear();
        seeder = new TaskSequenceDefaultSeeder(sequenceService);
    }

    @Test
    @DisplayName("Should seed by default only seq-patient-identity-pipeline into sequenceService and cache")
    void testSeedOnlyPatientIdentityPipelineByDefault() {
        Map<String, ErgonBase> activityIndex = new LinkedHashMap<>();
        activityIndex.put(MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID, new MessageQueueToExchangeConduit());
        activityIndex.put(PatientIdentityUpdateErgon.DEFAULT_ACTIVITY_ID, new PatientIdentityUpdateErgon());
        activityIndex.put(PatientDemographicsUpdateErgon.DEFAULT_ACTIVITY_ID, new PatientDemographicsUpdateErgon());

        List<Praxis> seeded = seeder.seedDefaultSequences(activityIndex);

        assertThat(seeded).hasSize(1);
        Praxis seq = seeded.get(0);
        assertThat(seq.getPraxisId()).isEqualTo("seq-patient-identity-pipeline");
        assertThat(seq.getPraxisName()).isEqualTo("Patient Identity Update Sequence");
        assertThat(seq.getTargetGatewayInstances()).containsExactly("*");
        assertThat(seq.getTargetTriggerTypes()).containsExactly("*");
        assertThat(seq.getActivityIds()).containsValues(
                MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID,
                PatientIdentityUpdateErgon.DEFAULT_ACTIVITY_ID,
                PatientDemographicsUpdateErgon.DEFAULT_ACTIVITY_ID
        );

        // Verify stored in sequence service
        assertThat(sequenceService.count()).isEqualTo(1);
        assertThat(sequenceService.getById("seq-patient-identity-pipeline")).isPresent();
        assertThat(sequenceService.getById("seq-admission-pipeline")).isEmpty();
        assertThat(sequenceService.getById("seq-order-result-pipeline")).isEmpty();
    }

    @Test
    @DisplayName("Should create patient identity update sequence with default fallback activity ids when index is empty")
    void testCreatePatientIdentityUpdateSequenceWithEmptyIndex() {
        Praxis seq = seeder.createPatientIdentityUpdateSequence(Map.of());

        assertThat(seq).isNotNull();
        assertThat(seq.getPraxisId()).isEqualTo("seq-patient-identity-pipeline");
        assertThat(seq.getActivityIds()).isNotEmpty();
        assertThat(seq.getActivityIds()).containsValues(
                MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID,
                "patient-identity-update",
                "patient-demographics-update"
        );
    }

    @Test
    @DisplayName("Should handle null sequenceService gracefully")
    void testSeedWithNullSequenceService() {
        TaskSequenceDefaultSeeder seederNullService = new TaskSequenceDefaultSeeder();
        List<Praxis> seeded = seederNullService.seedDefaultSequences(Map.of());

        assertThat(seeded).hasSize(1);
        assertThat(seeded.get(0).getPraxisId()).isEqualTo("seq-patient-identity-pipeline");
    }
}
