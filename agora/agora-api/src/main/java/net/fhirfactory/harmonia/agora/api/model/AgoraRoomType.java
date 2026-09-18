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

package net.fhirfactory.harmonia.agora.api.model;

/**
 * Standard Matrix room types managed within Agora collaboration spaces.
 */
public enum AgoraRoomType {
    STATISTICS("Patient Statistics", "statistics", true),
    TASKS("Patient Tasks", "tasks", true),
    DISCUSSION("Patient Discussion", "discussion", true),
    DIAGNOSTICS("Patient Diagnostics", "diagnostics", true),
    PRACTITIONER_ROLE("Practitioner Role", "practitioner-role", true),
    GROUP("Group Collaboration", "group", false),
    SPACE("Collaboration Space", "space", false);

    private final String displayName;
    private final String suffix;
    private final boolean childRoom;

    AgoraRoomType(String displayName, String suffix, boolean childRoom) {
        this.displayName = displayName;
        this.suffix = suffix;
        this.childRoom = childRoom;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isChildRoom() {
        return childRoom;
    }
}
