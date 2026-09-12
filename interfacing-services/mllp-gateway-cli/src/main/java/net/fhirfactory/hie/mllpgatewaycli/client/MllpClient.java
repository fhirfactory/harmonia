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

package net.fhirfactory.hie.mllpgatewaycli.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * Socket client implementing standard Minimal Lower Layer Protocol (MLLP) framing
 * for HL7 v2 messages.
 */
public class MllpClient {

    private static final Logger log = LoggerFactory.getLogger(MllpClient.class);

    public static final byte START_BLOCK = 0x0B; // <VT>
    public static final byte END_BLOCK = 0x1C;   // <FS>
    public static final byte CARRIAGE_RETURN = 0x0D; // <CR>

    private final String host;
    private final int port;
    private final int timeoutMs;

    public MllpClient(String host, int port) {
        this(host, port, 5000);
    }

    public MllpClient(String host, int port, int timeoutMs) {
        this.host = host != null && !host.isBlank() ? host.trim() : "localhost";
        this.port = port > 0 ? port : 2575;
        this.timeoutMs = timeoutMs > 0 ? timeoutMs : 5000;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    /**
     * Sends an HL7 message over MLLP to the configured endpoint and waits for ACK.
     */
    public MllpResult sendMessage(String hl7Message) {
        if (hl7Message == null || hl7Message.isBlank()) {
            return MllpResult.failure("HL7 message is null or empty", 0);
        }

        long startTime = System.currentTimeMillis();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            socket.setSoTimeout(timeoutMs);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // Encode MLLP frame: <VT> payload <FS><CR>
            byte[] frame = encodeMllpFrame(hl7Message);
            log.debug("Sending {} bytes to {}:{}", frame.length, host, port);
            out.write(frame);
            out.flush();

            // Read response
            String ackResponse = readMllpFrame(in);
            long duration = System.currentTimeMillis() - startTime;

            if (ackResponse == null || ackResponse.isBlank()) {
                return MllpResult.failure("Received empty response from MLLP server", duration);
            }

            return parseAck(ackResponse, duration);

        } catch (SocketTimeoutException e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Socket timeout after {} ms communicating with {}:{}", timeoutMs, host, port);
            return MllpResult.failure("Socket timeout after " + timeoutMs + " ms: " + e.getMessage(), duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Failed to send MLLP message to {}:{} - {}", host, port, e.getMessage());
            return MllpResult.failure("Connection failed to " + host + ":" + port + " - " + e.getMessage(), duration);
        }
    }

    /**
     * Encodes a message string into an MLLP framed byte array.
     */
    public static byte[] encodeMllpFrame(String message) {
        byte[] payload = message.getBytes(StandardCharsets.UTF_8);
        byte[] frame = new byte[payload.length + 3];
        frame[0] = START_BLOCK;
        System.arraycopy(payload, 0, frame, 1, payload.length);
        frame[frame.length - 2] = END_BLOCK;
        frame[frame.length - 1] = CARRIAGE_RETURN;
        return frame;
    }

    /**
     * Reads an MLLP framed message from an input stream.
     */
    public static String readMllpFrame(InputStream in) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        boolean started = false;

        while ((b = in.read()) != -1) {
            if (!started) {
                if (b == START_BLOCK) {
                    started = true;
                }
                continue;
            }

            if (b == END_BLOCK) {
                // Consume trailing carriage return if present
                int next = in.read();
                if (next != -1 && next != CARRIAGE_RETURN) {
                    // Non-standard trailing byte, ignore or handle
                }
                break;
            }

            buffer.write(b);
        }

        if (!started && buffer.size() == 0) {
            return null;
        }

        return buffer.toString(StandardCharsets.UTF_8);
    }

    /**
     * Parses the ACK code and control ID from an HL7 ACK message.
     */
    public static MllpResult parseAck(String ackMessage, long durationMs) {
        String normalized = ackMessage.replace("\r\n", "\r").replace("\n", "\r");
        String[] segments = normalized.split("\r");

        String ackCode = null;
        String msgControlId = null;
        String textMessage = null;

        for (String seg : segments) {
            if (seg.startsWith("MSA|")) {
                String[] fields = seg.split("\\|", -1);
                if (fields.length > 1) {
                    ackCode = fields[1].trim();
                }
                if (fields.length > 2) {
                    msgControlId = fields[2].trim();
                }
                if (fields.length > 3) {
                    textMessage = fields[3].trim();
                }
                break;
            }
        }

        boolean isAccept = "AA".equalsIgnoreCase(ackCode) || "CA".equalsIgnoreCase(ackCode);
        if (isAccept) {
            return MllpResult.success(ackCode, msgControlId, textMessage, ackMessage, durationMs);
        } else if (ackCode != null) {
            return MllpResult.nack(ackCode, msgControlId, textMessage, ackMessage, durationMs);
        } else {
            // No MSA segment found, but got response
            return MllpResult.success("UNKNOWN", msgControlId, textMessage, ackMessage, durationMs);
        }
    }
}
