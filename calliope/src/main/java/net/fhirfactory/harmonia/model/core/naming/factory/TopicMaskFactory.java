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

package net.fhirfactory.harmonia.model.core.naming.factory;

import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import net.fhirfactory.harmonia.model.core.naming.valuesets.MaskType;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Factory responsible for creating standard {@link TopicMask} instances.
 */
public final class TopicMaskFactory {

    private TopicMaskFactory() {
        // Utility factory
    }

    /**
     * Constructs an unrestricted mask that matches any Topic.
     *
     * @return inclusive unrestricted TopicMask
     */
    public static TopicMask unrestricted() {
        return new TopicMask(UUID.randomUUID(), MaskType.Inclusive, null, null);
    }

    /**
     * Builds an inclusive TopicMask from a dot-delimited pattern string.
     *
     * @param dotSeparatedPattern dot-separated pattern (e.g. {@code Health.HL7.2.4.*} or {@code Domain=Health.*})
     * @return constructed TopicMask
     */
    public static TopicMask fromNamePattern(String dotSeparatedPattern) {
        return fromNamePattern(dotSeparatedPattern, MaskType.Inclusive);
    }

    /**
     * Builds a TopicMask from a dot-delimited pattern string with explicit mask depth semantics.
     *
     * @param dotSeparatedPattern dot-separated pattern string
     * @param maskType          containment depth semantics
     * @return constructed TopicMask
     */
    public static TopicMask fromNamePattern(String dotSeparatedPattern, MaskType maskType) {
        if (StringUtils.isBlank(dotSeparatedPattern) || "*".equals(dotSeparatedPattern.trim()) || ".*".equals(dotSeparatedPattern.trim())) {
            return new TopicMask(UUID.randomUUID(), maskType != null ? maskType : MaskType.Inclusive, null, null);
        }
        String[] parts = dotSeparatedPattern.trim().split("\\.");
        List<String> segments = new ArrayList<>();
        if (!dotSeparatedPattern.contains("=") && parts.length >= 4 && StringUtils.isNumeric(parts[2]) && StringUtils.isNumeric(parts[3])) {
            segments.add(parts[0]);
            segments.add(parts[1]);
            segments.add(parts[2] + "\\." + parts[3]);
            for (int i = 4; i < parts.length; i++) {
                segments.add(parts[i]);
            }
        } else {
            segments.addAll(Arrays.asList(parts));
        }

        TopicMaskBuilder builder = new TopicMaskBuilder().maskType(maskType);

        boolean hasCanonical = false;
        for (String seg : segments) {
            if (seg.contains("=")) {
                hasCanonical = true;
                break;
            }
        }

        for (int i = 0; i < segments.size(); i++) {
            String part = segments.get(i).trim();
            if ("*".equals(part)) {
                part = ".*";
            }
            if (hasCanonical) {
                if (part.contains("=")) {
                    String[] qn = part.split("=", 2);
                    String q = "*".equals(qn[0].trim()) ? ".*" : qn[0].trim();
                    String n = "*".equals(qn[1].trim()) ? ".*" : qn[1].trim();
                    builder.addCanonicalPattern(i, q, n);
                } else {
                    builder.addCanonicalPattern(i, ".*", part);
                }
            } else {
                builder.addNamePattern(i, part);
            }
        }
        return builder.build();
    }
}
