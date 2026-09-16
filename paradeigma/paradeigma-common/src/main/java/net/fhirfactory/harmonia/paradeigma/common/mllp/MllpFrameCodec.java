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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Utility for encoding and decoding MLLP (<VT>...<FS><CR>) framed HL7 messages.
 */
public final class MllpFrameCodec {

    private MllpFrameCodec() {
        // Utility class
    }

    /**
     * Encloses raw HL7 text in MLLP delimiters: &lt;VT&gt;payload&lt;FS&gt;&lt;CR&gt;.
     *
     * @param hl7Message raw HL7 message string
     * @param charset character encoding
     * @return byte array containing the MLLP frame
     */
    public static byte[] encode(String hl7Message, Charset charset) {
        Objects.requireNonNull(hl7Message, "hl7Message must not be null");
        Charset cs = (charset != null) ? charset : MllpConstants.DEFAULT_CHARSET;
        byte[] payloadBytes = hl7Message.getBytes(cs);

        byte[] frame = new byte[payloadBytes.length + 3];
        frame[0] = MllpConstants.START_BLOCK;
        System.arraycopy(payloadBytes, 0, frame, 1, payloadBytes.length);
        frame[frame.length - 2] = MllpConstants.END_BLOCK;
        frame[frame.length - 1] = MllpConstants.CARRIAGE_RETURN;
        return frame;
    }

    /**
     * Encloses raw HL7 text in MLLP delimiters using default UTF-8 encoding.
     *
     * @param hl7Message raw HL7 message string
     * @return byte array containing the MLLP frame
     */
    public static byte[] encode(String hl7Message) {
        return encode(hl7Message, MllpConstants.DEFAULT_CHARSET);
    }

    /**
     * Writes an MLLP-encoded message to an OutputStream.
     *
     * @param out output stream
     * @param hl7Message message string
     * @param charset character encoding
     * @throws IOException on I/O error
     */
    public static void writeFrame(OutputStream out, String hl7Message, Charset charset) throws IOException {
        byte[] frame = encode(hl7Message, charset);
        out.write(frame);
        out.flush();
    }

    /**
     * Writes an MLLP-encoded message to an OutputStream using default UTF-8 encoding.
     *
     * @param out output stream
     * @param hl7Message message string
     * @throws IOException on I/O error
     */
    public static void writeFrame(OutputStream out, String hl7Message) throws IOException {
        writeFrame(out, hl7Message, MllpConstants.DEFAULT_CHARSET);
    }

    /**
     * Reads a single MLLP-framed HL7 message from an InputStream.
     * Consumes leading bytes until &lt;VT&gt; (0x0B) is found, buffers bytes until &lt;FS&gt;&lt;CR&gt; (0x1C 0x0D),
     * and returns the decoded string.
     *
     * @param in input stream
     * @param charset character encoding
     * @return decoded HL7 message string, or null if end of stream reached before Start Block
     * @throws IOException on socket or framing error
     */
    public static String readFrame(InputStream in, Charset charset) throws IOException {
        Charset cs = (charset != null) ? charset : MllpConstants.DEFAULT_CHARSET;

        int b;
        // Search for START_BLOCK (<VT>)
        while ((b = in.read()) != -1) {
            if (b == MllpConstants.START_BLOCK) {
                break;
            }
        }

        if (b == -1) {
            return null; // Stream closed before frame start
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
        while ((b = in.read()) != -1) {
            if (b == MllpConstants.END_BLOCK) {
                int next = in.read();
                if (next == MllpConstants.CARRIAGE_RETURN) {
                    return buffer.toString(cs);
                } else if (next == -1) {
                    throw new IOException("Stream closed prematurely after MLLP END_BLOCK (expected CARRIAGE_RETURN)");
                } else {
                    // Encountered unexpected character after END_BLOCK, keep buffering
                    buffer.write(b);
                    buffer.write(next);
                }
            } else {
                buffer.write(b);
            }
        }

        throw new IOException("Stream closed unexpectedly before MLLP END_BLOCK / CARRIAGE_RETURN delimiters");
    }

    /**
     * Reads a single MLLP-framed HL7 message from an InputStream using default UTF-8 encoding.
     *
     * @param in input stream
     * @return decoded HL7 message string, or null if end of stream reached before Start Block
     * @throws IOException on socket or framing error
     */
    public static String readFrame(InputStream in) throws IOException {
        return readFrame(in, MllpConstants.DEFAULT_CHARSET);
    }

    /**
     * Checks whether a raw byte array is properly framed with MLLP delimiters.
     *
     * @param bytes raw frame bytes
     * @return true if bytes start with &lt;VT&gt; and end with &lt;FS&gt;&lt;CR&gt;
     */
    public static boolean isValidFrame(byte[] bytes) {
        if (bytes == null || bytes.length < 3) {
            return false;
        }
        return bytes[0] == MllpConstants.START_BLOCK
                && bytes[bytes.length - 2] == MllpConstants.END_BLOCK
                && bytes[bytes.length - 1] == MllpConstants.CARRIAGE_RETURN;
    }

    /**
     * Strips MLLP delimiters from a framed byte array and returns the message string.
     *
     * @param framedBytes bytes with MLLP delimiters
     * @param charset character encoding
     * @return raw HL7 message string
     */
    public static String decode(byte[] framedBytes, Charset charset) {
        if (!isValidFrame(framedBytes)) {
            throw new IllegalArgumentException("Invalid MLLP frame: missing Start Block or End Block delimiters");
        }
        Charset cs = (charset != null) ? charset : MllpConstants.DEFAULT_CHARSET;
        return new String(framedBytes, 1, framedBytes.length - 3, cs);
    }
}
