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

package net.fhirfactory.hie.mllpgatewaycli.template;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Registry containing pre-defined HL7 v2.4 ADT message templates.
 */
public class AdtTemplateRegistry {

    private static final Map<AdtTemplateType, String> TEMPLATES = new LinkedHashMap<>();

    static {
        // A01: Admit / Visit Notification
        TEMPLATES.put(AdtTemplateType.A01,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A01|${messageControlId}|P|2.4\r" +
                "EVN|A01|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|I|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}|||||||||||||||||||||||||${timestamp}\r");

        // A02: Transfer a Patient
        TEMPLATES.put(AdtTemplateType.A02,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A02|${messageControlId}|P|2.4\r" +
                "EVN|A02|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|I|${currentLocation}|||${priorLocation}|${attendingDoctor}|||||||||||${visitNumber}\r");

        // A03: Discharge / End Visit
        TEMPLATES.put(AdtTemplateType.A03,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A03|${messageControlId}|P|2.4\r" +
                "EVN|A03|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|I|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}||||||||||||||||||||||||||${timestamp}\r");

        // A04: Register a Patient (Outpatient)
        TEMPLATES.put(AdtTemplateType.A04,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A04|${messageControlId}|P|2.4\r" +
                "EVN|A04|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|O|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");

        // A05: Pre-Admit a Patient
        TEMPLATES.put(AdtTemplateType.A05,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A05|${messageControlId}|P|2.4\r" +
                "EVN|A05|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|P|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");

        // A08: Update Patient Information
        TEMPLATES.put(AdtTemplateType.A08,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A08|${messageControlId}|P|2.4\r" +
                "EVN|A08|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|I|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");

        // A11: Cancel Admit
        TEMPLATES.put(AdtTemplateType.A11,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A11|${messageControlId}|P|2.4\r" +
                "EVN|A11|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|I|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");

        // A12: Cancel Transfer
        TEMPLATES.put(AdtTemplateType.A12,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A12|${messageControlId}|P|2.4\r" +
                "EVN|A12|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|I|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");

        // A13: Cancel Discharge
        TEMPLATES.put(AdtTemplateType.A13,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A13|${messageControlId}|P|2.4\r" +
                "EVN|A13|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|I|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");

        // A31: Update Person Information
        TEMPLATES.put(AdtTemplateType.A31,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A31|${messageControlId}|P|2.4\r" +
                "EVN|A31|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "PV1|1|O|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");

        // A40: Merge Patient
        TEMPLATES.put(AdtTemplateType.A40,
                "MSH|^~\\&|${sendingApp}|${sendingFacility}|${receivingApp}|${receivingFacility}|${timestamp}||ADT^A40|${messageControlId}|P|2.4\r" +
                "EVN|A40|${timestamp}\r" +
                "PID|1||${mrn}^^^${assigningAuth}^${identifierType}||${lastName}^${firstName}^^^^||${dob}|${gender}|||${streetAddress}^^${city}^${state}^${postalCode}^USA||${phoneNumber}|||||ACC-${mrn}\r" +
                "MRG|PRIOR-${mrn}^^^${assigningAuth}^${identifierType}\r" +
                "PV1|1|I|${currentLocation}||||${attendingDoctor}|||||||||||${visitNumber}\r");
    }

    public static Optional<String> getRawTemplate(AdtTemplateType type) {
        return Optional.ofNullable(TEMPLATES.get(type));
    }

    public static Set<AdtTemplateType> getSupportedTypes() {
        return Collections.unmodifiableSet(TEMPLATES.keySet());
    }

    public static Map<AdtTemplateType, String> getAllTemplates() {
        return Collections.unmodifiableMap(TEMPLATES);
    }
}
