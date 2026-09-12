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

package net.fhirfactory.hie.befe;

import jakarta.ws.rs.core.Response;
import net.fhirfactory.hie.befe.rest.*;
import net.fhirfactory.hie.befe.service.FhirCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class FhirRestResourceIntegrationTest {

    private FhirCacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new FhirCacheService();
        cacheService.init();
    }

    private void injectService(Object resource) throws Exception {
        Field field = resource.getClass().getDeclaredField("cacheService");
        field.setAccessible(true);
        field.set(resource, cacheService);
    }

    @Test
    @DisplayName("1. Person REST Controller CRUD & Search")
    void testPersonController() throws Exception {
        PersonResource controller = new PersonResource();
        injectService(controller);

        String payload = """
                {
                  "resourceType": "Person",
                  "name": [{"family": "Smith", "given": ["Alice"]}],
                  "gender": "female",
                  "identifier": [{"system": "urn:mrn", "value": "MRN-1001"}]
                }
                """;

        Response createRes = controller.create(payload);
        assertThat(createRes.getStatus()).isEqualTo(201);
        String createdJson = (String) createRes.getEntity();
        assertThat(createdJson).contains("Smith");

        // Search by name
        Response searchRes = controller.search(null, "Smith", null);
        assertThat(searchRes.getStatus()).isEqualTo(200);
        assertThat((String) searchRes.getEntity()).contains("Bundle");

        // Read
        String personId = createRes.getLocation().getPath().substring(createRes.getLocation().getPath().lastIndexOf('/') + 1);
        Response readRes = controller.read(personId);
        assertThat(readRes.getStatus()).isEqualTo(200);
        assertThat((String) readRes.getEntity()).contains("Alice");

        // Update
        String updatePayload = """
                {
                  "resourceType": "Person",
                  "name": [{"family": "Smith-Johnson", "given": ["Alice"]}],
                  "gender": "female"
                }
                """;
        Response updateRes = controller.update(personId, updatePayload);
        assertThat(updateRes.getStatus()).isEqualTo(200);
        assertThat((String) updateRes.getEntity()).contains("Smith-Johnson");

        // Delete
        Response delRes = controller.delete(personId);
        assertThat(delRes.getStatus()).isEqualTo(204);
        assertThat(controller.read(personId).getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("2. Practitioner & PractitionerRole REST Controllers")
    void testPractitionerControllers() throws Exception {
        PractitionerResource practitionerController = new PractitionerResource();
        injectService(practitionerController);

        String docPayload = """
                {
                  "resourceType": "Practitioner",
                  "name": [{"family": "Watson", "given": ["John"], "prefix": ["Dr."]}],
                  "gender": "male"
                }
                """;
        Response docRes = practitionerController.create(docPayload);
        assertThat(docRes.getStatus()).isEqualTo(201);
        String docId = docRes.getLocation().getPath().substring(docRes.getLocation().getPath().lastIndexOf('/') + 1);

        PractitionerRoleResource roleController = new PractitionerRoleResource();
        injectService(roleController);

        String rolePayload = """
                {
                  "resourceType": "PractitionerRole",
                  "active": true,
                  "identifier": [{"system": "urn:roles", "value": "CARDIO-01"}]
                }
                """;
        Response roleRes = roleController.create(rolePayload);
        assertThat(roleRes.getStatus()).isEqualTo(201);
    }

    @Test
    @DisplayName("3. Organization, Location, HealthcareService & Group Controllers")
    void testFacilityAndCohortControllers() throws Exception {
        // Organization
        OrganizationResource orgController = new OrganizationResource();
        injectService(orgController);
        Response orgRes = orgController.create("{\"resourceType\":\"Organization\",\"name\":\"General Hospital\"}");
        assertThat(orgRes.getStatus()).isEqualTo(201);

        // Location
        LocationResource locController = new LocationResource();
        injectService(locController);
        Response locRes = locController.create("{\"resourceType\":\"Location\",\"name\":\"Ward 3B\"}");
        assertThat(locRes.getStatus()).isEqualTo(201);

        // HealthcareService
        HealthcareServiceResource hsController = new HealthcareServiceResource();
        injectService(hsController);
        Response hsRes = hsController.create("{\"resourceType\":\"HealthcareService\",\"name\":\"Emergency Response\"}");
        assertThat(hsRes.getStatus()).isEqualTo(201);

        // Group
        GroupResource groupController = new GroupResource();
        injectService(groupController);
        Response grpRes = groupController.create("{\"resourceType\":\"Group\",\"type\":\"person\",\"membership\":\"definitional\",\"name\":\"Hypertension Study\"}");
        assertThat(grpRes.getStatus()).isEqualTo(201);
    }

    @Test
    @DisplayName("4. Provenance, AuditEvent, Consent, Task, Communication & DocumentReference Controllers")
    void testNewResourceControllers() throws Exception {
        // Provenance
        ProvenanceResource provController = new ProvenanceResource();
        injectService(provController);
        Response provRes = provController.create("{\"resourceType\":\"Provenance\",\"target\":[{\"reference\":\"Person/101\"}]}");
        assertThat(provRes.getStatus()).isEqualTo(201);
        String provId = provRes.getLocation().getPath().substring(provRes.getLocation().getPath().lastIndexOf('/') + 1);
        assertThat(provController.read(provId).getStatus()).isEqualTo(200);

        // AuditEvent
        AuditEventResource auditController = new AuditEventResource();
        injectService(auditController);
        Response auditRes = auditController.create("{\"resourceType\":\"AuditEvent\",\"action\":\"C\",\"code\":{\"text\":\"Create\"}}");
        assertThat(auditRes.getStatus()).isEqualTo(201);
        String auditId = auditRes.getLocation().getPath().substring(auditRes.getLocation().getPath().lastIndexOf('/') + 1);
        assertThat(auditController.read(auditId).getStatus()).isEqualTo(200);

        // Consent
        ConsentResource consentController = new ConsentResource();
        injectService(consentController);
        Response consentRes = consentController.create("{\"resourceType\":\"Consent\",\"status\":\"active\",\"category\":[{\"text\":\"Research\"}]}");
        assertThat(consentRes.getStatus()).isEqualTo(201);
        String consentId = consentRes.getLocation().getPath().substring(consentRes.getLocation().getPath().lastIndexOf('/') + 1);
        assertThat(consentController.read(consentId).getStatus()).isEqualTo(200);

        // Task
        TaskResource taskController = new TaskResource();
        injectService(taskController);
        Response taskRes = taskController.create("{\"resourceType\":\"Task\",\"status\":\"requested\",\"intent\":\"order\",\"description\":\"Lab Test\"}");
        assertThat(taskRes.getStatus()).isEqualTo(201);
        String taskId = taskRes.getLocation().getPath().substring(taskRes.getLocation().getPath().lastIndexOf('/') + 1);
        assertThat(taskController.read(taskId).getStatus()).isEqualTo(200);

        // Communication
        CommunicationResource commController = new CommunicationResource();
        injectService(commController);
        Response commRes = commController.create("{\"resourceType\":\"Communication\",\"status\":\"completed\",\"note\":[{\"text\":\"Reminder sent\"}]}");
        assertThat(commRes.getStatus()).isEqualTo(201);
        String commId = commRes.getLocation().getPath().substring(commRes.getLocation().getPath().lastIndexOf('/') + 1);
        assertThat(commController.read(commId).getStatus()).isEqualTo(200);

        // DocumentReference
        DocumentReferenceResource docController = new DocumentReferenceResource();
        injectService(docController);
        Response docRes = docController.create("{\"resourceType\":\"DocumentReference\",\"status\":\"current\",\"description\":\"Summary Note\"}");
        assertThat(docRes.getStatus()).isEqualTo(201);
        String docId = docRes.getLocation().getPath().substring(docRes.getLocation().getPath().lastIndexOf('/') + 1);
        assertThat(docController.read(docId).getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("5. TaskSequence & SystemStatus REST Controllers")
    void testTaskSequenceAndStatusControllers() throws Exception {
        net.fhirfactory.hie.befe.service.TaskSequenceCacheService seqService = new net.fhirfactory.hie.befe.service.TaskSequenceCacheService();
        seqService.init();

        TaskSequenceResource seqResource = new TaskSequenceResource();
        Field field = seqResource.getClass().getDeclaredField("sequenceCacheService");
        field.setAccessible(true);
        field.set(seqResource, seqService);

        String seqJson = """
                {
                  "sequenceId": "seq-test-pipeline",
                  "sequenceName": "Test Admission Pipeline",
                  "description": "Processes admission triggers",
                  "enabled": true,
                  "sourceQueueName": "task.event.queue.pas-gw",
                  "targetGatewayInstances": ["pas-gw"],
                  "targetTriggerTypes": ["ADT^A01"],
                  "activityIds": ["patient-identity-update", "patient-demographics-update"]
                }
                """;

        Response createRes = seqResource.createSequence(seqJson);
        assertThat(createRes.getStatus()).isEqualTo(201);
        assertThat((String) createRes.getEntity()).contains("seq-test-pipeline");

        Response getRes = seqResource.getSequenceById("seq-test-pipeline");
        assertThat(getRes.getStatus()).isEqualTo(200);
        assertThat((String) getRes.getEntity()).contains("Test Admission Pipeline");

        Response getAllRes = seqResource.getAllSequences();
        assertThat(getAllRes.getStatus()).isEqualTo(200);
        assertThat((String) getAllRes.getEntity()).contains("seq-test-pipeline");

        SystemStatusResource statusResource = new SystemStatusResource();
        Field statusSeqField = statusResource.getClass().getDeclaredField("taskSequenceCacheService");
        statusSeqField.setAccessible(true);
        statusSeqField.set(statusResource, seqService);

        Response statusRes = statusResource.getSystemStatus();
        assertThat(statusRes.getStatus()).isEqualTo(200);

        Response delRes = seqResource.deleteSequence("seq-test-pipeline");
        assertThat(delRes.getStatus()).isEqualTo(204);
    }
}
