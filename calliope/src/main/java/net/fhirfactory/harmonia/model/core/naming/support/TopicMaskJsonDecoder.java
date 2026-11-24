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

package net.fhirfactory.harmonia.model.core.naming.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Strict streaming adapter from JSON's logical form into immutable TopicMask values.
 * <p>
 * It rejects duplicate, coerced, incomplete, and unknown members before domain
 * construction. The complete bounded record list is read before RE2/J compilation,
 * so oversized or contradictory ordinary forms cannot trigger partial compilation.
 */
public final class TopicMaskJsonDecoder extends JsonDeserializer<TopicMask> {

    private static final Set<String> ORDINARY_FIELDS = Set.of("entries", "includeContainedElements");

    /**
     * Reads one explicit universal or complete ordinary logical representation.
     *
     * @param parser Jackson token stream positioned at the object start
     * @param context Jackson deserialization context
     * @return validated immutable mask
     * @throws IOException when the input is malformed or ambiguous
     */
    @Override
    public TopicMask deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.currentToken() != JsonToken.START_OBJECT) {
            throw invalid(parser);
        }
        Set<String> seen = new HashSet<>();
        List<EntryData> entryData = null;
        Boolean includeContainedElements = null;
        boolean universal = false;
        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            if (token != JsonToken.FIELD_NAME) {
                throw invalid(parser);
            }
            String field = parser.currentName();
            if (!seen.add(field)) {
                throw invalid(parser);
            }
            token = parser.nextToken();
            if (token == null) {
                throw invalid(parser);
            }
            switch (field) {
                case "universal" -> {
                    if (token != JsonToken.VALUE_TRUE) {
                        throw invalid(parser);
                    }
                    universal = true;
                }
                case "entries" -> entryData = readEntries(parser, token);
                case "includeContainedElements" -> {
                    if (token == JsonToken.VALUE_TRUE) {
                        includeContainedElements = true;
                    } else if (token == JsonToken.VALUE_FALSE) {
                        includeContainedElements = false;
                    } else {
                        throw invalid(parser);
                    }
                }
                default -> throw invalid(parser);
            }
        }
        if (universal) {
            if (!seen.equals(Set.of("universal"))) {
                throw invalid(parser);
            }
            return TopicMask.all();
        }
        if (!seen.equals(ORDINARY_FIELDS) || entryData == null || includeContainedElements == null) {
            throw invalid(parser);
        }
        try {
            return TopicMask.ordinary(compileEntries(entryData, parser), includeContainedElements);
        } catch (IllegalArgumentException exception) {
            throw invalid(parser);
        }
    }

    @Override
    public TopicMask getNullValue(DeserializationContext context) throws JsonMappingException {
        throw JsonMappingException.from(context, "Invalid TopicMask logical form");
    }

    /** Reads and bounds raw records without compiling expressions. */
    private static List<EntryData> readEntries(JsonParser parser, JsonToken token) throws IOException {
        if (token != JsonToken.START_ARRAY) {
            throw invalid(parser);
        }
        List<EntryData> entries = new ArrayList<>();
        while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
            if (token == null || entries.size() >= TopicLimits.MAX_DEPTH || token != JsonToken.START_OBJECT) {
                throw invalid(parser);
            }
            entries.add(readEntry(parser));
        }
        return entries;
    }

    /** Validates one entry object's unique keys and exact scalar token types. */
    private static EntryData readEntry(JsonParser parser) throws IOException {
        Set<String> seen = new HashSet<>();
        String qualifierRegex = null;
        String valueRegex = null;
        String versionRegex = null;
        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            if (token != JsonToken.FIELD_NAME) {
                throw invalid(parser);
            }
            String field = parser.currentName();
            if (!seen.add(field)) {
                throw invalid(parser);
            }
            token = parser.nextToken();
            if (token != JsonToken.VALUE_STRING) {
                throw invalid(parser);
            }
            switch (field) {
                case "qualifierRegex" -> qualifierRegex = parser.getText();
                case "valueRegex" -> valueRegex = parser.getText();
                case "versionRegex" -> versionRegex = parser.getText();
                default -> throw invalid(parser);
            }
        }
        if (!seen.contains("qualifierRegex") || !seen.contains("valueRegex")
                || !(seen.size() == 2 || (seen.size() == 3 && seen.contains("versionRegex")))) {
            throw invalid(parser);
        }
        return new EntryData(qualifierRegex, valueRegex, versionRegex);
    }

    /** Builds validated entries only after the enclosing logical shape is complete. */
    private static List<TopicMaskEntry> compileEntries(List<EntryData> entries, JsonParser parser)
            throws JsonMappingException {
        List<TopicMaskEntry> compiled = new ArrayList<>(entries.size());
        try {
            for (EntryData entry : entries) {
                compiled.add(entry.compile());
            }
        } catch (IllegalArgumentException exception) {
            throw invalid(parser);
        }
        return compiled;
    }

    /** Creates one immutable expression entry after decoding succeeds. */
    private record EntryData(String qualifierRegex, String valueRegex, String versionRegex) {
        private TopicMaskEntry compile() {
            return TopicMaskEntry.of(qualifierRegex, valueRegex, versionRegex);
        }
    }

    /** Creates a payload-safe mapping error without retaining parser input or causes. */
    private static JsonMappingException invalid(JsonParser parser) {
        return JsonMappingException.from(parser, "Invalid TopicMask logical form");
    }

    /** Strict decoder for one standalone or nested TopicMask entry. */
    public static final class EntryDeserializer extends JsonDeserializer<TopicMaskEntry> {

        /**
         * Validates one raw entry and compiles it only after its complete object is read.
         *
         * @param parser Jackson token stream positioned at the entry object
         * @param context Jackson deserialization context
         * @return immutable compiled expression entry
         * @throws IOException for malformed, ambiguous, or invalid expressions
         */
        @Override
        public TopicMaskEntry deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (parser.currentToken() != JsonToken.START_OBJECT) {
                throw invalid(parser);
            }
            try {
                return readEntry(parser).compile();
            } catch (IllegalArgumentException exception) {
                throw invalid(parser);
            }
        }

        /** Rejects null entries instead of treating them as absent criteria. */
        @Override
        public TopicMaskEntry getNullValue(DeserializationContext context) throws JsonMappingException {
            throw JsonMappingException.from(context, "Invalid TopicMask entry");
        }
    }
}