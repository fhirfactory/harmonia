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

package net.fhirfactory.harmonia.mllpgateway.service;

import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProvenanceServiceTest {

    private DefaultProvenanceService provenanceService;

    @BeforeEach
    void setUp() {
        provenanceService = new DefaultProvenanceService();
        provenanceService.clear();
    }

    @Test
    @DisplayName("Create, Read, Update, Delete Provenance resource")
    void testCrudOperations() {
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-001");
        prov.addTarget(new Reference("Task/MSG-1001").setDisplay("Task for ADT A01"));
        prov.setRecorded(new Date());

        Provenance.ProvenanceAgentComponent agent = prov.addAgent();
        agent.setWho(new Reference("Device/mllp-gateway").setDisplay("HIE MLLP Gateway"));

        Provenance created = provenanceService.create(prov);
        assertThat(created).isNotNull();
        assertThat(created.getIdPart()).isEqualTo("prov-001");
        assertThat(provenanceService.count()).isEqualTo(1);
        assertThat(FhirSecurityTagManager.hasConfidentiality(created, FhirConfidentialityEnum.N)).isTrue();

        Optional<Provenance> fetched = provenanceService.getById("prov-001");
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getTarget()).hasSize(1);
        assertThat(fetched.get().getTargetFirstRep().getReference()).isEqualTo("Task/MSG-1001");
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched.get(), FhirConfidentialityEnum.N)).isTrue();

        // Update
        Provenance toUpdate = fetched.get();
        Provenance.ProvenanceEntityComponent entity = toUpdate.addEntity();
        entity.setRole(Provenance.ProvenanceEntityRole.SOURCE);
        entity.setWhat(new Reference("Communication/comm-1001"));
        provenanceService.update("prov-001", toUpdate);

        Optional<Provenance> updated = provenanceService.getById("prov-001");
        assertThat(updated).isPresent();
        assertThat(updated.get().getEntity()).hasSize(1);
        assertThat(updated.get().getEntityFirstRep().getWhat().getReference()).isEqualTo("Communication/comm-1001");
        assertThat(FhirSecurityTagManager.hasConfidentiality(updated.get(), FhirConfidentialityEnum.N)).isTrue();

        // Delete
        boolean deleted = provenanceService.delete("prov-001");
        assertThat(deleted).isTrue();
        assertThat(provenanceService.count()).isEqualTo(0);
        assertThat(provenanceService.getById("prov-001")).isEmpty();
    }

    @Test
    @DisplayName("Search Provenance by Target, Agent, and Patient")
    void testSearchOperations() {
        Provenance prov1 = new Provenance();
        prov1.setId("Provenance/prov-A01");
        prov1.addTarget(new Reference("Task/TASK-1001"));
        Provenance.ProvenanceAgentComponent agent1 = prov1.addAgent();
        agent1.setWho(new Reference("Practitioner/DOC-1").setDisplay("Dr. House"));
        prov1.setPatient(new Reference("Patient/PAT-100").setDisplay("John Doe"));

        Provenance prov2 = new Provenance();
        prov2.setId("Provenance/prov-A08");
        prov2.addTarget(new Reference("Task/TASK-2002"));
        Provenance.ProvenanceAgentComponent agent2 = prov2.addAgent();
        agent2.setWho(new Reference("Device/gateway-2").setDisplay("Gateway Two"));
        prov2.setPatient(new Reference("Patient/PAT-200").setDisplay("Jane Smith"));

        provenanceService.create(prov1);
        provenanceService.create(prov2);

        // Search by Target
        List<Provenance> byTarget = provenanceService.search(null, "TASK-1001", null, null);
        assertThat(byTarget).hasSize(1);
        assertThat(byTarget.get(0).getIdPart()).isEqualTo("prov-A01");

        // Search by Agent
        List<Provenance> byAgent = provenanceService.search(null, null, "House", null);
        assertThat(byAgent).hasSize(1);
        assertThat(byAgent.get(0).getIdPart()).isEqualTo("prov-A01");

        // Search by Patient
        List<Provenance> byPatient = provenanceService.search(null, null, null, "PAT-200");
        assertThat(byPatient).hasSize(1);
        assertThat(byPatient.get(0).getIdPart()).isEqualTo("prov-A08");
    }
}
