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

package net.fhirfactory.harmonia.befe.config;

import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import net.fhirfactory.harmonia.kleio.persistence.qualifier.KleioAudit;

import javax.sql.DataSource;

/**
 * Composition-root CDI producer for the Kleio audit persistence {@link DataSource}.
 * Binds the physical WildFly container DataSource (KleioAuditDS) to the logical
 * {@link KleioAudit} qualifier required by Kleio repositories.
 */
@ApplicationScoped
@DataSourceDefinition(
        name = "java:jboss/datasources/KleioAuditDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        url = "${env.FHIR_DB_URL:jdbc:postgresql://postgres-1:5432/fhir_node_1}",
        user = "${env.FHIR_DB_USER:fhir_user}",
        password = "${env.FHIR_DB_PASSWORD:fhir_password}",
        properties = {
                "serverName=${env.FHIR_DB_HOST:postgres-1}",
                "portNumber=${env.FHIR_DB_PORT:5432}",
                "databaseName=${env.FHIR_DB_NAME:fhir_node_1}"
        }
)
public class AuditDataSourceProducer {

    @Resource(lookup = "java:jboss/datasources/KleioAuditDS")
    private DataSource dataSource;

    @Produces
    @KleioAudit
    @ApplicationScoped
    public DataSource produceAuditDataSource() {
        return dataSource;
    }
}
