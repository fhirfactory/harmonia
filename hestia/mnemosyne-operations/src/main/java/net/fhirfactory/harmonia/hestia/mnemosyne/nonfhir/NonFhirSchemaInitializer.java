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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Explicit development bootstrap for the V001 Non-FHIR schema. Production deployment must use
 * its governed migration process; this helper does not run implicitly when the application starts.
 */
public final class NonFhirSchemaInitializer {

    private static final String SCHEMA_RESOURCE = "/db/mnemosyne/nonfhir/V001__information_unit_and_status.sql";

    /**
     * Applies the idempotent development DDL to a supplied datasource.
     *
     * @param dataSource PostgreSQL datasource
     * @throws SQLException when a DDL statement fails
     * @throws IllegalStateException when the schema resource is missing or unreadable
     */
    public void initializeDevelopmentSchema(DataSource dataSource) throws SQLException {
        Objects.requireNonNull(dataSource, "dataSource must not be null");
        String script = loadScript();
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            for (String fragment : script.split(";")) {
                String ddl = fragment.strip();
                if (!ddl.isEmpty()) {
                    statement.execute(ddl);
                }
            }
        }
    }

    /** Loads the bundled development DDL without writing into external locations. */
    private String loadScript() {
        try (InputStream input = NonFhirSchemaInitializer.class.getResourceAsStream(SCHEMA_RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException("Development schema resource is unavailable: " + SCHEMA_RESOURCE);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Development schema resource could not be read", exception);
        }
    }
}