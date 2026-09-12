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

package net.fhirfactory.hie.taskprocessors.hl7v2x.factories;

import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.cleanId;
import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.cleanPhoneNumber;

/**
 * Builds administrative FHIR resources ({@link RelatedPerson}, {@link Organization}) from HL7 v2 messages.
 */
public class AdtAdministrativeResourceBuilder {

    /**
     * Builds RelatedPerson resources from NK1 and GT1 segments.
     */
    public List<RelatedPerson> buildRelatedPersons(Terser terser, String rawMessage, String patientId) {
        List<RelatedPerson> list = new ArrayList<>();

        // Parse NK1 segments
        Pattern nk1Pattern = Pattern.compile("^NK1\\|(.*?)$", Pattern.MULTILINE);
        Matcher matcher = nk1Pattern.matcher(rawMessage);
        int idx = 1;
        while (matcher.find()) {
            String nk1Line = matcher.group(1);
            String[] fields = ("NK1|" + nk1Line).split("\\|", -1);

            String nameField = fields.length > 2 ? fields[2] : "";
            String relField = fields.length > 3 ? fields[3] : "";
            String addrField = fields.length > 4 ? fields[4] : "";
            String phoneField = fields.length > 5 ? fields[5] : "";

            RelatedPerson rp = new RelatedPerson();
            rp.setId("RelatedPerson/" + cleanId(patientId) + "-nk" + idx);
            rp.setActive(true);
            rp.setPatient(new Reference("Patient/" + cleanId(patientId)));

            if (StringUtils.isNotBlank(nameField)) {
                String[] nameParts = nameField.split("\\^");
                HumanName name = rp.addName();
                name.setUse(HumanName.NameUse.OFFICIAL);
                if (nameParts.length > 0 && StringUtils.isNotBlank(nameParts[0])) name.setFamily(nameParts[0]);
                if (nameParts.length > 1 && StringUtils.isNotBlank(nameParts[1])) name.addGiven(nameParts[1]);
            }

            if (StringUtils.isNotBlank(relField)) {
                String[] relParts = relField.split("\\^");
                String code = relParts[0];
                String text = relParts.length > 1 ? relParts[1] : code;
                CodeableConcept relConcept = rp.addRelationship();
                relConcept.setText(text);
                relConcept.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0131", code, text));
            }

            if (StringUtils.isNotBlank(phoneField)) {
                ContactPoint cp = rp.addTelecom();
                cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
                cp.setUse(ContactPoint.ContactPointUse.HOME);
                cp.setValue(cleanPhoneNumber(phoneField));
            }

            if (StringUtils.isNotBlank(addrField)) {
                String[] addrParts = addrField.split("\\^");
                Address addr = rp.addAddress();
                if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
            }

            list.add(rp);
            idx++;
        }

        // Parse GT1 segments (Guarantor)
        Pattern gt1Pattern = Pattern.compile("^GT1\\|(.*?)$", Pattern.MULTILINE);
        Matcher gt1Matcher = gt1Pattern.matcher(rawMessage);
        int gIdx = 1;
        while (gt1Matcher.find()) {
            String gt1Line = gt1Matcher.group(1);
            String[] fields = ("GT1|" + gt1Line).split("\\|", -1);
            String nameField = fields.length > 3 ? fields[3] : "";
            String addrField = fields.length > 5 ? fields[5] : "";
            String phoneField = fields.length > 6 ? fields[6] : "";
            String relField = fields.length > 11 ? fields[11] : "";

            RelatedPerson guarantor = new RelatedPerson();
            guarantor.setId("RelatedPerson/" + cleanId(patientId) + "-gt" + gIdx);
            guarantor.setActive(true);
            guarantor.setPatient(new Reference("Patient/" + cleanId(patientId)));

            if (StringUtils.isNotBlank(nameField)) {
                String[] nameParts = nameField.split("\\^");
                HumanName name = guarantor.addName();
                if (nameParts.length > 0 && StringUtils.isNotBlank(nameParts[0])) name.setFamily(nameParts[0]);
                if (nameParts.length > 1 && StringUtils.isNotBlank(nameParts[1])) name.addGiven(nameParts[1]);
            }

            CodeableConcept rel = guarantor.addRelationship();
            rel.setText("Guarantor");
            if (StringUtils.isNotBlank(relField)) {
                rel.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0131", relField, "Guarantor"));
            }

            if (StringUtils.isNotBlank(phoneField)) {
                ContactPoint cp = guarantor.addTelecom();
                cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
                cp.setValue(cleanPhoneNumber(phoneField));
            }

            list.add(guarantor);
            gIdx++;
        }

        return list;
    }

    /**
     * Builds Organization resources from MSH sending/receiving facilities.
     */
    public List<Organization> buildOrganizations(Terser terser, String rawMessage, String sendingFacility, String receivingFacility) {
        List<Organization> list = new ArrayList<>();
        Map<String, Organization> map = new LinkedHashMap<>();

        if (StringUtils.isNotBlank(sendingFacility)) {
            String id = cleanId(sendingFacility);
            Organization org = new Organization();
            org.setId("Organization/" + id);
            org.setName(sendingFacility);
            org.setActive(true);
            map.put(id, org);
        }

        if (StringUtils.isNotBlank(receivingFacility) && !map.containsKey(cleanId(receivingFacility))) {
            String id = cleanId(receivingFacility);
            Organization org = new Organization();
            org.setId("Organization/" + id);
            org.setName(receivingFacility);
            org.setActive(true);
            map.put(id, org);
        }

        list.addAll(map.values());
        return list;
    }
}
