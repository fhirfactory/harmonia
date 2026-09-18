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

package net.fhirfactory.harmonia.paradeigma.common.mllp;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Common constants for Minimal Lower Layer Protocol (MLLP) framing over TCP/IP.
 *
 * MLLP frame structure:
 * <pre>
 *   &lt;VT&gt; (0x0B) + HL7 Payload + &lt;FS&gt; (0x1C) + &lt;CR&gt; (0x0D)
 * </pre>
 */
public final class MllpConstants {

    /** Start Block delimiter character (&lt;VT&gt; / Vertical Tab / 0x0B). */
    public static final byte START_BLOCK = 0x0B;

    /** End Block delimiter character (&lt;FS&gt; / File Separator / 0x1C). */
    public static final byte END_BLOCK = 0x1C;

    /** Carriage Return character (&lt;CR&gt; / 0x0D). */
    public static final byte CARRIAGE_RETURN = 0x0D;

    /** Default character set for HL7 messages (UTF-8). */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /** Default socket connect timeout in milliseconds. */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 5000;

    /** Default socket read timeout in milliseconds. */
    public static final int DEFAULT_READ_TIMEOUT_MS = 10000;

    /** Default buffer size in bytes for reading MLLP streams. */
    public static final int BUFFER_SIZE = 8192;

    private MllpConstants() {
        // Utility class
    }
}
