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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.cleanId;
import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.cleanPhoneNumber;

/**
 * Builds administrative FHIR resources ({@link Organization}, {@link Location}) from HL7 v2 MFN^M02 messages.
 */
public class MfnAdministrativeResourceBuilder {

    /**
     * Builds Organization resources from MSH sending/receiving facilities, STF department/service, ORG, PRA, AFF, EDU, and CER segments.
     *
     * @param terser            optional HAPI Terser instance
     * @param rawMessage        raw HL7 v2 MFN message string
     * @param sendingFacility   MSH sending facility
     * @param receivingFacility MSH receiving facility
     * @return list of unique Organization resources
     */
    public List<Organization> buildOrganizations(Terser terser, String rawMessage, String sendingFacility, String receivingFacility) {
        Map<String, Organization> orgMap = new LinkedHashMap<>();

        // 1. MSH Sending Facility
        if (StringUtils.isNotBlank(sendingFacility)) {
            String id = cleanId(sendingFacility);
            Organization org = new Organization();
            org.setId("Organization/" + id);
            org.setName(sendingFacility.trim());
            org.setActive(true);
            org.getTypeFirstRep().setText("Sending Facility").addCoding()
                    .setSystem("http://terminology.hl7.org/CodeSystem/organization-type")
                    .setCode("prov")
                    .setDisplay("Healthcare Provider");
            orgMap.put(id, org);
        }

        // 2. MSH Receiving Facility
        if (StringUtils.isNotBlank(receivingFacility)) {
            String id = cleanId(receivingFacility);
            if (!orgMap.containsKey(id)) {
                Organization org = new Organization();
                org.setId("Organization/" + id);
                org.setName(receivingFacility.trim());
                org.setActive(true);
                org.getTypeFirstRep().setText("Receiving Facility");
                orgMap.put(id, org);
            }
        }

        if (StringUtils.isBlank(rawMessage)) {
            return new ArrayList<>(orgMap.values());
        }

        // 3. STF-8 Department
        Pattern stfPattern = Pattern.compile("^STF\\|(.*?)$", Pattern.MULTILINE);
        Matcher stfMatcher = stfPattern.matcher(rawMessage);
        while (stfMatcher.find()) {
            String stfLine = stfMatcher.group(1);
            String[] fields = ("STF|" + stfLine).split("\\|", -1);
            String deptField = fields.length > 8 ? fields[8] : "";
            if (StringUtils.isNotBlank(deptField)) {
                String[] deptParts = deptField.split("\\^");
                String code = deptParts[0].trim();
                String name = deptParts.length > 1 && StringUtils.isNotBlank(deptParts[1]) ? deptParts[1].trim() : code;
                String id = cleanId(code);
                if (!orgMap.containsKey(id)) {
                    Organization org = new Organization();
                    org.setId("Organization/" + id);
                    org.setName(name);
                    org.setActive(true);
                    org.getTypeFirstRep().setText("Department").addCoding()
                            .setSystem("http://terminology.hl7.org/CodeSystem/organization-type")
                            .setCode("dept")
                            .setDisplay("Hospital Department");
                    orgMap.put(id, org);
                }
            }

            // STF-9 Hospital Service
            String serviceField = fields.length > 9 ? fields[9] : "";
            if (StringUtils.isNotBlank(serviceField)) {
                String[] servParts = serviceField.split("\\^");
                String code = servParts[0].trim();
                String name = servParts.length > 1 && StringUtils.isNotBlank(servParts[1]) ? servParts[1].trim() : code;
                String id = cleanId(code);
                if (!orgMap.containsKey(id)) {
                    Organization org = new Organization();
                    org.setId("Organization/" + id);
                    org.setName(name);
                    org.setActive(true);
                    org.getTypeFirstRep().setText("Hospital Service");
                    orgMap.put(id, org);
                }
            }
        }

        // 4. ORG segment(s)
        Pattern orgPattern = Pattern.compile("^ORG\\|(.*?)$", Pattern.MULTILINE);
        Matcher orgMatcher = orgPattern.matcher(rawMessage);
        while (orgMatcher.find()) {
            String orgLine = orgMatcher.group(1);
            String[] fields = ("ORG|" + orgLine).split("\\|", -1);

            String orgCode = fields.length > 3 ? fields[3].split("\\^")[0].trim() : "";
            String orgName = fields.length > 5 ? fields[5].trim() : "";
            String orgAddr = fields.length > 6 ? fields[6].trim() : "";
            String orgPhone = fields.length > 7 ? fields[7].trim() : "";

            String id = StringUtils.isNotBlank(orgCode) ? cleanId(orgCode) : (StringUtils.isNotBlank(orgName) ? cleanId(orgName) : "org-" + UUID.randomUUID().toString().substring(0, 8));
            Organization org = orgMap.computeIfAbsent(id, k -> {
                Organization o = new Organization();
                o.setId("Organization/" + k);
                o.setActive(true);
                return o;
            });

            if (StringUtils.isNotBlank(orgName)) {
                org.setName(orgName);
            } else if (!org.hasName() && StringUtils.isNotBlank(orgCode)) {
                org.setName(orgCode);
            }

            if (StringUtils.isNotBlank(orgAddr) || StringUtils.isNotBlank(orgPhone)) {
                ExtendedContactDetail contact = org.addContact();
                if (StringUtils.isNotBlank(orgAddr)) {
                    String[] addrParts = orgAddr.split("\\^");
                    Address addr = new Address();
                    if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                    if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                    if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                    if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
                    if (addrParts.length > 5 && StringUtils.isNotBlank(addrParts[5])) addr.setCountry(addrParts[5]);
                    contact.setAddress(addr);
                }
                if (StringUtils.isNotBlank(orgPhone)) {
                    ContactPoint cp = contact.addTelecom();
                    cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
                    cp.setValue(cleanPhoneNumber(orgPhone));
                }
            }
        }

        // 5. PRA-2 Practitioner Group
        Pattern praPattern = Pattern.compile("^PRA\\|(.*?)$", Pattern.MULTILINE);
        Matcher praMatcher = praPattern.matcher(rawMessage);
        if (praMatcher.find()) {
            String praLine = praMatcher.group(1);
            String[] fields = ("PRA|" + praLine).split("\\|", -1);
            String groupField = fields.length > 2 ? fields[2] : "";
            if (StringUtils.isNotBlank(groupField)) {
                String[] parts = groupField.split("\\^");
                String code = parts[0].trim();
                String name = parts.length > 1 && StringUtils.isNotBlank(parts[1]) ? parts[1].trim() : code;
                String id = cleanId(code);
                if (!orgMap.containsKey(id)) {
                    Organization org = new Organization();
                    org.setId("Organization/" + id);
                    org.setName(name);
                    org.setActive(true);
                    org.getTypeFirstRep().setText("Practitioner Group").addCoding()
                            .setSystem("http://terminology.hl7.org/CodeSystem/organization-type")
                            .setCode("team")
                            .setDisplay("Organizational team");
                    orgMap.put(id, org);
                }
            }
        }

        // 6. AFF-2 Professional Organization
        Pattern affPattern = Pattern.compile("^AFF\\|(.*?)$", Pattern.MULTILINE);
        Matcher affMatcher = affPattern.matcher(rawMessage);
        while (affMatcher.find()) {
            String affLine = affMatcher.group(1);
            String[] fields = ("AFF|" + affLine).split("\\|", -1);
            String affName = fields.length > 2 ? fields[2].split("\\^")[0].trim() : "";
            String affAddr = fields.length > 3 ? fields[3].trim() : "";
            if (StringUtils.isNotBlank(affName)) {
                String id = cleanId(affName);
                Organization org = orgMap.computeIfAbsent(id, k -> {
                    Organization o = new Organization();
                    o.setId("Organization/" + k);
                    o.setName(affName);
                    o.setActive(true);
                    o.getTypeFirstRep().setText("Professional Association");
                    return o;
                });
                if (StringUtils.isNotBlank(affAddr) && !org.hasContact()) {
                    String[] addrParts = affAddr.split("\\^");
                    Address addr = new Address();
                    if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                    if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                    if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                    if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
                    if (addrParts.length > 5 && StringUtils.isNotBlank(addrParts[5])) addr.setCountry(addrParts[5]);
                    org.addContact().setAddress(addr);
                }
            }
        }

        // 7. EDU-5 Schools
        Pattern eduPattern = Pattern.compile("^EDU\\|(.*?)$", Pattern.MULTILINE);
        Matcher eduMatcher = eduPattern.matcher(rawMessage);
        while (eduMatcher.find()) {
            String eduLine = eduMatcher.group(1);
            String[] fields = ("EDU|" + eduLine).split("\\|", -1);
            String school = fields.length > 5 ? fields[5].split("\\^")[0].trim() : "";
            String schoolAddr = fields.length > 6 ? fields[6].trim() : "";
            if (StringUtils.isNotBlank(school)) {
                String id = cleanId(school);
                Organization org = orgMap.computeIfAbsent(id, k -> {
                    Organization o = new Organization();
                    o.setId("Organization/" + k);
                    o.setName(school);
                    o.setActive(true);
                    o.getTypeFirstRep().setText("Educational Institute").addCoding()
                            .setSystem("http://terminology.hl7.org/CodeSystem/organization-type")
                            .setCode("edu")
                            .setDisplay("Educational Institute");
                    return o;
                });
                if (StringUtils.isNotBlank(schoolAddr) && !org.hasContact()) {
                    String[] addrParts = schoolAddr.split("\\^");
                    Address addr = new Address();
                    if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                    if (addrParts.length > 1 && StringUtils.isNotBlank(addrParts[1])) addr.setCity(addrParts[1]);
                    if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setState(addrParts[2]);
                    if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setPostalCode(addrParts[3]);
                    if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setCountry(addrParts[4]);
                    org.addContact().setAddress(addr);
                }
            }
        }

        // 8. CER-4 Certifying Authorities
        Pattern cerPattern = Pattern.compile("^CER\\|(.*?)$", Pattern.MULTILINE);
        Matcher cerMatcher = cerPattern.matcher(rawMessage);
        while (cerMatcher.find()) {
            String cerLine = cerMatcher.group(1);
            String[] fields = ("CER|" + cerLine).split("\\|", -1);
            String authority = fields.length > 4 ? fields[4].split("\\^")[0].trim() : "";
            if (StringUtils.isNotBlank(authority)) {
                String id = cleanId(authority);
                orgMap.computeIfAbsent(id, k -> {
                    Organization o = new Organization();
                    o.setId("Organization/" + k);
                    o.setName(authority);
                    o.setActive(true);
                    o.getTypeFirstRep().setText("Certifying Body");
                    return o;
                });
            }
        }

        return new ArrayList<>(orgMap.values());
    }

    /**
     * Builds Location resources from STF department/service and ORG segments.
     *
     * @param terser     optional HAPI Terser instance
     * @param rawMessage raw HL7 v2 MFN message string
     * @return list of Location resources
     */
    public List<Location> buildLocations(Terser terser, String rawMessage) {
        Map<String, Location> locationMap = new LinkedHashMap<>();
        if (StringUtils.isBlank(rawMessage)) {
            return new ArrayList<>(locationMap.values());
        }

        // 1. STF Department & Hospital Service locations
        Pattern stfPattern = Pattern.compile("^STF\\|(.*?)$", Pattern.MULTILINE);
        Matcher stfMatcher = stfPattern.matcher(rawMessage);
        while (stfMatcher.find()) {
            String stfLine = stfMatcher.group(1);
            String[] fields = ("STF|" + stfLine).split("\\|", -1);
            String deptField = fields.length > 8 ? fields[8] : "";
            if (StringUtils.isNotBlank(deptField)) {
                String[] deptParts = deptField.split("\\^");
                String code = deptParts[0].trim();
                String name = deptParts.length > 1 && StringUtils.isNotBlank(deptParts[1]) ? deptParts[1].trim() : code;
                String id = "loc-" + cleanId(code);
                if (!locationMap.containsKey(id)) {
                    Location loc = new Location();
                    loc.setId("Location/" + id);
                    loc.setName(name);
                    loc.setStatus(Location.LocationStatus.ACTIVE);
                    loc.setMode(Location.LocationMode.INSTANCE);
                    locationMap.put(id, loc);
                }
            }
        }

        // 2. ORG segments
        Pattern orgPattern = Pattern.compile("^ORG\\|(.*?)$", Pattern.MULTILINE);
        Matcher orgMatcher = orgPattern.matcher(rawMessage);
        while (orgMatcher.find()) {
            String orgLine = orgMatcher.group(1);
            String[] fields = ("ORG|" + orgLine).split("\\|", -1);

            String orgCode = fields.length > 3 ? fields[3].split("\\^")[0].trim() : "";
            String orgName = fields.length > 5 ? fields[5].trim() : "";
            String orgAddr = fields.length > 6 ? fields[6].trim() : "";
            String orgPhone = fields.length > 7 ? fields[7].trim() : "";

            String id = "loc-" + (StringUtils.isNotBlank(orgCode) ? cleanId(orgCode) : (StringUtils.isNotBlank(orgName) ? cleanId(orgName) : UUID.randomUUID().toString().substring(0, 8)));
            Location loc = locationMap.computeIfAbsent(id, k -> {
                Location l = new Location();
                l.setId("Location/" + k);
                l.setStatus(Location.LocationStatus.ACTIVE);
                l.setMode(Location.LocationMode.INSTANCE);
                return l;
            });

            if (StringUtils.isNotBlank(orgName)) {
                loc.setName(orgName);
            } else if (!loc.hasName() && StringUtils.isNotBlank(orgCode)) {
                loc.setName(orgCode);
            }

            if (StringUtils.isNotBlank(orgAddr)) {
                String[] addrParts = orgAddr.split("\\^");
                Address addr = new Address();
                if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
                if (addrParts.length > 5 && StringUtils.isNotBlank(addrParts[5])) addr.setCountry(addrParts[5]);
                loc.setAddress(addr);
            }

            if (StringUtils.isNotBlank(orgPhone)) {
                ContactPoint cp = loc.addContact().addTelecom();
                cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
                cp.setValue(cleanPhoneNumber(orgPhone));
            }
        }

        return new ArrayList<>(locationMap.values());
    }
}
