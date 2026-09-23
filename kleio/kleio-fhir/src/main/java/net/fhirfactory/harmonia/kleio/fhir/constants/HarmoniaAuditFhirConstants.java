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

package net.fhirfactory.harmonia.kleio.fhir.constants;

import net.fhirfactory.harmonia.model.security.HarmoniaSecurityCodeSystem;

public final class HarmoniaAuditFhirConstants {
    private HarmoniaAuditFhirConstants() {}

    // 5 Frozen Extensions
    public static final String EXTENSION_SECURITY_DOMAIN = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/security-domain";
    public static final String EXTENSION_CORRELATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/correlation-id";
    public static final String EXTENSION_CAUSATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/causation-id";
    public static final String EXTENSION_OPERATION_ID = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/operation-id";
    public static final String EXTENSION_AUDIT_ATTRIBUTE = "http://harmonia.fhirfactory.net/fhir/StructureDefinition/audit-attribute";

    // Sub-extension keys for audit-attribute
    public static final String SUB_EXTENSION_KEY = "key";
    public static final String SUB_EXTENSION_VALUE = "value";

    // Governed CodeSystems
    public static final String SYSTEM_AUDIT_CLASSIFICATION = "http://harmonia.fhirfactory.net/security/audit-classification";
    public static final String SYSTEM_AUDIT_ACTION = "http://harmonia.fhirfactory.net/security/audit-action";
    public static final String SYSTEM_AUDIT_OUTCOME = "http://harmonia.fhirfactory.net/security/audit-outcome";
    public static final String SYSTEM_AGENT_ROLE = "http://harmonia.fhirfactory.net/security/agent-role";
    public static final String SYSTEM_PRINCIPAL_TYPE = "http://harmonia.fhirfactory.net/security/principal-type";
    public static final String SYSTEM_ENTITY_ROLE = "http://harmonia.fhirfactory.net/security/entity-role";
    public static final String SYSTEM_SOURCE_SUBSYSTEM = "http://harmonia.fhirfactory.net/source/subsystem";
    public static final String SYSTEM_THEMIS_DECISION = "http://harmonia.fhirfactory.net/security/themis-decision";
    public static final String SYSTEM_THEMIS_DECISION_REASON = "http://harmonia.fhirfactory.net/security/themis-decision-reason";
    public static final String SYSTEM_THEMIS_POLICY = "http://harmonia.fhirfactory.net/security/themis-policy";
    public static final String SYSTEM_ENTITY_DETAIL = "http://harmonia.fhirfactory.net/security/entity-detail";

    // Re-export or reference existing systems
    public static final String SYSTEM_SECURITY_LABEL = HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM;
    public static final String SYSTEM_AUTHORITY_CODE = HarmoniaSecurityCodeSystem.AUTHORITY_CODE_SYSTEM;

    // Governed Codes
    public static final String CODE_AGENT_ROLE_INITIATOR = "INITIATOR";
    public static final String CODE_AGENT_ROLE_EXECUTOR = "EXECUTOR";
    public static final String CODE_ENTITY_ROLE_TARGET = "TARGET";
    public static final String CODE_DETAIL_SECURITY_DOMAIN = "security-domain";
}
