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

package net.fhirfactory.harmonia.paradeigma.common.generator;

import org.hl7.fhir.r5.model.*;

import java.util.Date;
import java.util.List;

/**
 * Deterministic generator for FHIR R5 Group resources representing practitioner clinical panels and teams.
 */
public class GroupGenerator {

    private static final String[] PANEL_NAMES = {
            "Cardiovascular Clinical Specialist Panel", "Trauma & Acute Resuscitation Team",
            "Multidisciplinary Oncology Tumor Board", "Clinical Governance & Quality Committee",
            "Rapid Response Medical Emergency Team"
    };

    private final SeedRandom random;

    public GroupGenerator() {
        this(new SeedRandom());
    }

    public GroupGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public GroupGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
    }

    public Group generate(String id, String managingOrgId, List<String> practitionerIds) {
        return generateValid(id, managingOrgId, practitionerIds);
    }

    public Group generateValid(String id, String managingOrgId, List<String> practitionerIds) {
        String grpId = id != null ? id : "grp-" + random.nextInt(10000, 99999);
        String name = random.pick(PANEL_NAMES) + " (" + random.nextInt(10, 99) + ")";

        Group group = new Group();
        group.setId(new IdType("Group", grpId));
        group.setName(name);
        group.setType(Group.GroupType.PRACTITIONER);
        group.setMembership(Group.GroupMembershipBasis.ENUMERATED);

        if (managingOrgId != null) {
            String ref = managingOrgId.startsWith("Organization/") ? managingOrgId : "Organization/" + managingOrgId;
            group.setManagingEntity(new Reference(ref));
        }

        if (practitionerIds != null) {
            for (String practId : practitionerIds) {
                String ref = practId.startsWith("Practitioner/") ? practId : "Practitioner/" + practId;
                group.addMember(new Group.GroupMemberComponent().setEntity(new Reference(ref)));
            }
        }

        group.addIdentifier(new Identifier()
                .setSystem("urn:harmonia:group:identifier")
                .setValue("GRP-" + random.nextInt(1000, 9999)));

        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        group.setMeta(meta);

        return group;
    }

    public Group generateIncomplete(String id) {
        String grpId = id != null ? id : "grp-inc-" + random.nextInt(10000, 99999);
        Group group = new Group();
        group.setId(new IdType("Group", grpId));
        group.setType(Group.GroupType.PRACTITIONER);
        // Missing name, membership basis, managing entity, members
        return group;
    }

    public Group generateInvalid(String id) {
        String grpId = id != null ? id : "grp-inv-" + random.nextInt(10000, 99999);
        Group group = new Group();
        group.setId(new IdType("Group", grpId));
        group.setName("");
        return group;
    }

    public Group generateBrokenReference(String id) {
        return generateValid(
                id,
                "Organization/non-existent-org-99999",
                List.of("Practitioner/non-existent-pract-99999")
        );
    }

    public Group generateUpdate(Group existing, int newVersion) {
        Group updated = existing.copy();
        if (updated.getMeta() == null) {
            updated.setMeta(new Meta());
        }
        updated.getMeta().setVersionId(String.valueOf(newVersion));
        updated.getMeta().setLastUpdated(new Date());
        updated.setName(existing.getName() + " - Expanded Panel");
        return updated;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
