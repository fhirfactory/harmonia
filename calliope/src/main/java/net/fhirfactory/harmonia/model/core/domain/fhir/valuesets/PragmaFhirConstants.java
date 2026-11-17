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

package net.fhirfactory.harmonia.model.core.domain.fhir.valuesets;

import net.fhirfactory.harmonia.model.core.workflow.tasks.Pragma;

/**
 * Standard identifier systems, extension URLs, and prefixes for {@link Pragma} FHIR mappings.
 */
public final class PragmaFhirConstants {

    public static final String IDENTIFIER_SYSTEM_PRAGMA_ID = "http://fhirfactory.net/harmonia/task/pragma-id";
    public static final String IDENTIFIER_SYSTEM_CORRELATION_ID = "http://fhirfactory.net/harmonia/task/correlation-id";
    public static final String IDENTIFIER_SYSTEM_CAUSATION_ID = "http://fhirfactory.net/harmonia/task/causation-id";

    public static final String EXTENSION_PRAXIS_ID = "http://fhirfactory.net/harmonia/task/praxis-id";
    public static final String EXTENSION_CHECKPOINT = "http://fhirfactory.net/harmonia/task/checkpoint";
    public static final String EXTENSION_CHECKPOINT_STAGE = "http://fhirfactory.net/harmonia/task/checkpoint-stage";
    public static final String EXTENSION_CHECKPOINT_ERGON = "http://fhirfactory.net/harmonia/task/checkpoint-ergon";
    public static final String EXTENSION_CHECKPOINT_STEP = "http://fhirfactory.net/harmonia/task/checkpoint-step";
    public static final String EXTENSION_METADATA_PREFIX = "http://fhirfactory.net/harmonia/task/metadata/";

    public static final String EXTENSION_SECURITY_PREFIX = "http://fhirfactory.net/harmonia/task/security/";
    public static final String EXTENSION_SECURITY_PRINCIPAL_ID = EXTENSION_SECURITY_PREFIX + "principal-id";
    public static final String EXTENSION_SECURITY_PRINCIPAL_TYPE = EXTENSION_SECURITY_PREFIX + "principal-type";
    public static final String EXTENSION_SECURITY_SOURCE_DOMAIN = EXTENSION_SECURITY_PREFIX + "source-domain";
    public static final String EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID = EXTENSION_SECURITY_PREFIX + "executing-principal-id";
    public static final String EXTENSION_SECURITY_EXECUTING_PRINCIPAL_TYPE = EXTENSION_SECURITY_PREFIX + "executing-principal-type";
    public static final String EXTENSION_SECURITY_EXECUTING_SOURCE_DOMAIN = EXTENSION_SECURITY_PREFIX + "executing-source-domain";
    public static final String EXTENSION_SECURITY_AUTHORITY = EXTENSION_SECURITY_PREFIX + "authority";
    public static final String EXTENSION_SECURITY_POLICY_VERSION = EXTENSION_SECURITY_PREFIX + "policy-version";

    public static final String LEGACY_IDENTIFIER_SYSTEM_PRAGMA_ID = "http://fhirfactory.net/hie/task/pragma-id";
    public static final String LEGACY_IDENTIFIER_SYSTEM_CORRELATION_ID = "http://fhirfactory.net/hie/task/correlation-id";
    public static final String LEGACY_IDENTIFIER_SYSTEM_CAUSATION_ID = "http://fhirfactory.net/hie/task/causation-id";
    public static final String LEGACY_EXTENSION_PRAXIS_ID = "http://fhirfactory.net/hie/task/praxis-id";
    public static final String LEGACY_EXTENSION_METADATA_PREFIX = "http://fhirfactory.net/hie/task/metadata/";
    public static final String LEGACY_EXTENSION_SECURITY_PREFIX = "http://fhirfactory.net/hie/task/security/";

    private PragmaFhirConstants() {
        // Constants utility
    }
}
