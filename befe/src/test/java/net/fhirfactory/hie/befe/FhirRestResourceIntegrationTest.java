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
}
