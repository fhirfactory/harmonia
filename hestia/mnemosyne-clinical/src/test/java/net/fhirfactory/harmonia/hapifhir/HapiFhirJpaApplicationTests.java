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

package net.fhirfactory.harmonia.hapifhir;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HapiFhirJpaApplicationTests {

    @Autowired
    private DaoRegistry daoRegistry;

    @Autowired
    private JpaStorageSettings storageSettings;

    @Autowired
    private ISearchParamRegistry searchParamRegistry;

    @Autowired
    private ISearchCoordinatorSvc searchCoordinatorSvc;

    @Autowired
    private HapiTransactionService transactionService;

    @Autowired
    private IFhirSystemDao<Bundle, Meta> systemDao;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("1. Core HAPI JPA infrastructure beans instantiate in Spring context")
    void contextLoadsAndCoreBeansInstantiated() {
        assertThat(daoRegistry).isNotNull();
        assertThat(storageSettings).isNotNull();
        assertThat(searchParamRegistry).isNotNull();
        assertThat(searchCoordinatorSvc).isNotNull();
        assertThat(transactionService).isNotNull();
        assertThat(systemDao).isNotNull();
    }

    @Test
    @DisplayName("2. Operational resource DAOs resolve through DaoRegistry for core FHIR R5 resources")
    void operationalResourceDaosResolveThroughDaoRegistry() {
        List<Class<? extends org.hl7.fhir.instance.model.api.IBaseResource>> resourceClasses = List.of(
                Patient.class,
                Practitioner.class,
                PractitionerRole.class,
                Organization.class,
                Location.class,
                HealthcareService.class,
                Consent.class,
                Task.class,
                Provenance.class,
                Person.class,
                RelatedPerson.class,
                Communication.class,
                Endpoint.class,
                DocumentReference.class,
                Group.class
        );

        for (Class<? extends org.hl7.fhir.instance.model.api.IBaseResource> clazz : resourceClasses) {
            String resourceName = clazz.getSimpleName();
            IFhirResourceDao<?> daoByClass = daoRegistry.getResourceDao(clazz);
            assertThat(daoByClass)
                    .as("DaoRegistry should resolve DAO by class for %s", resourceName)
                    .isNotNull();

            IFhirResourceDao<?> daoByName = daoRegistry.getResourceDao(resourceName);
            assertThat(daoByName)
                    .as("DaoRegistry should resolve DAO by resource name '%s'", resourceName)
                    .isNotNull();
            assertThat(daoByName).isSameAs(daoByClass);
        }
    }

    @Test
    @DisplayName("3. Expected HFJ_* schema tables and indexes are created against the test database")
    void expectedHfjSchemaObjectsCreatedAgainstDatabase() throws Exception {
        Set<String> tableNames = new HashSet<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tableNames.add(rs.getString("TABLE_NAME").toUpperCase());
                }
            }
        }

        List<String> expectedTables = List.of(
                "HFJ_RESOURCE",
                "HFJ_RES_VER",
                "HFJ_SPIDX_STRING",
                "HFJ_SPIDX_TOKEN",
                "HFJ_SPIDX_DATE",
                "HFJ_SPIDX_URI",
                "HFJ_SPIDX_QUANTITY",
                "HFJ_RES_LINK",
                "HFJ_RES_TAG",
                "HFJ_TAG_DEF",
                "HFJ_BINARY_STORAGE_BLOB"
        );

        for (String expectedTable : expectedTables) {
            assertThat(tableNames)
                    .as("Database should contain HAPI FHIR JPA table %s", expectedTable)
                    .contains(expectedTable);
        }
    }
}
