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
import org.hl7.fhir.r5.model.Attachment;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CommunicationServiceTest {

    private DefaultCommunicationService communicationService;

    @BeforeEach
    void setUp() {
        communicationService = new DefaultCommunicationService();
        communicationService.clear();
    }

    @Test
    @DisplayName("Create, Read, Update, Delete Communication resource")
    void testCrudOperations() {
        Communication comm = new Communication();
        comm.setId("Communication/comm-001");
        comm.setStatus(Enumerations.EventStatus.COMPLETED);
        comm.setSubject(new Reference("Patient/PAT100").setDisplay("John Doe"));

        Communication.CommunicationPayloadComponent payload = comm.addPayload();
        Attachment attachment = new Attachment();
        attachment.setContentType("application/hl7-v2");
        attachment.setData("MSH|^~\\&|TEST...".getBytes(StandardCharsets.UTF_8));
        payload.setContent(attachment);

        Communication created = communicationService.create(comm);
        assertThat(created).isNotNull();
        assertThat(created.getIdPart()).isEqualTo("comm-001");
        assertThat(communicationService.count()).isEqualTo(1);
        assertThat(FhirSecurityTagManager.hasConfidentiality(created, FhirConfidentialityEnum.N)).isTrue();

        Optional<Communication> fetched = communicationService.getById("comm-001");
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getSubject().getReference()).isEqualTo("Patient/PAT100");
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched.get(), FhirConfidentialityEnum.N)).isTrue();

        // Update
        Communication toUpdate = fetched.get();
        toUpdate.addNote().setText("Updated processing note");
        communicationService.update("comm-001", toUpdate);

        Optional<Communication> updated = communicationService.getById("comm-001");
        assertThat(updated).isPresent();
        assertThat(updated.get().getNote()).hasSize(1);
        assertThat(updated.get().getNoteFirstRep().getText()).isEqualTo("Updated processing note");
        assertThat(FhirSecurityTagManager.hasConfidentiality(updated.get(), FhirConfidentialityEnum.N)).isTrue();

        // Delete
        boolean deleted = communicationService.delete("comm-001");
        assertThat(deleted).isTrue();
        assertThat(communicationService.count()).isEqualTo(0);
        assertThat(communicationService.getById("comm-001")).isEmpty();
    }

    @Test
    @DisplayName("Search Communications by ID, Patient ID, and Identifier")
    void testSearchOperations() {
        Communication comm1 = new Communication();
        comm1.setId("Communication/comm-A01");
        comm1.setStatus(Enumerations.EventStatus.COMPLETED);
        comm1.setSubject(new Reference("Patient/PAT-999"));
        comm1.addIdentifier().setSystem("http://example.org/hl7/message-control-id").setValue("MSG-1001");

        Communication comm2 = new Communication();
        comm2.setId("Communication/comm-A08");
        comm2.setStatus(Enumerations.EventStatus.COMPLETED);
        comm2.setSubject(new Reference("Patient/PAT-888"));
        comm2.addIdentifier().setSystem("http://example.org/hl7/message-control-id").setValue("MSG-1002");

        communicationService.create(comm1);
        communicationService.create(comm2);

        // Search by ID
        List<Communication> searchById = communicationService.search("comm-A01", null, null);
        assertThat(searchById).hasSize(1);
        assertThat(searchById.get(0).getIdPart()).isEqualTo("comm-A01");

        // Search by Patient
        List<Communication> searchByPat = communicationService.search(null, "PAT-888", null);
        assertThat(searchByPat).hasSize(1);
        assertThat(searchByPat.get(0).getIdPart()).isEqualTo("comm-A08");

        // Search by Identifier
        List<Communication> searchByIdent = communicationService.search(null, null, "MSG-1001");
        assertThat(searchByIdent).hasSize(1);
        assertThat(searchByIdent.get(0).getIdPart()).isEqualTo("comm-A01");
    }
}
