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

package net.fhirfactory.harmonia.mllpout.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In-process mock TCP MLLP server for end-to-end testing of outbound transmissions.
 */
public class MockMllpServer implements AutoCloseable {

    private static final byte START_BLOCK = 0x0B; // <VT>
    private static final byte END_BLOCK = 0x1C;   // <FS>
    private static final byte CARRIAGE_RETURN = 0x0D; // <CR>
    private static final Pattern MSH_10_PATTERN = Pattern.compile("^MSH\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|([^|\\r\\n]+)");

    private final ServerSocket serverSocket;
    private final int port;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final List<String> receivedMessages = new CopyOnWriteArrayList<>();
    private final List<Thread> workerThreads = new CopyOnWriteArrayList<>();

    private volatile String ackCode = "AA";
    private volatile String ackTextMessage = "Message Accepted";
    private volatile long responseDelayMs = 0;
    private volatile boolean closeSocketAbruptly = false;

    public MockMllpServer() throws Exception {
        this.serverSocket = new ServerSocket(0);
        this.port = serverSocket.getLocalPort();
        startAcceptLoop();
    }

    private void startAcceptLoop() {
        Thread acceptThread = new Thread(() -> {
            while (running.get() && !serverSocket.isClosed()) {
                try {
                    Socket client = serverSocket.accept();
                    Thread worker = new Thread(() -> handleClient(client));
                    workerThreads.add(worker);
                    worker.start();
                } catch (Exception ignored) {
                }
            }
        });
        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    private void handleClient(Socket client) {
        try (client;
             InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()) {

            while (running.get() && !client.isClosed()) {
                String hl7 = readMllpMessage(in);
                if (hl7 == null) {
                    break;
                }
                receivedMessages.add(hl7);

                if (closeSocketAbruptly) {
                    client.close();
                    return;
                }

                if (responseDelayMs > 0) {
                    Thread.sleep(responseDelayMs);
                }

                String controlId = extractControlId(hl7);
                String ack = buildAck(controlId, ackCode, ackTextMessage);
                writeMllpMessage(out, ack);
            }
        } catch (Exception ignored) {
        }
    }

    private String readMllpMessage(InputStream in) throws Exception {
        int b;
        while ((b = in.read()) != -1) {
            if (b == START_BLOCK) {
                break;
            }
        }
        if (b == -1) {
            return null;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while ((b = in.read()) != -1) {
            if (b == END_BLOCK) {
                in.read(); // Read trailing CR
                break;
            }
            buffer.write(b);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private void writeMllpMessage(OutputStream out, String hl7) throws Exception {
        out.write(START_BLOCK);
        out.write(hl7.getBytes(StandardCharsets.UTF_8));
        out.write(END_BLOCK);
        out.write(CARRIAGE_RETURN);
        out.flush();
    }

    private String extractControlId(String hl7) {
        Matcher matcher = MSH_10_PATTERN.matcher(hl7.trim());
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "UNKNOWN_ID";
    }

    private String buildAck(String controlId, String ackCode, String text) {
        return "MSH|^~\\&|MOCK_RECEIVER|FACILITY|HARMONIA|HIE|20260915083000||ACK^A01|ACK" + System.currentTimeMillis() + "|P|2.4\r" +
                "MSA|" + ackCode + "|" + controlId + "|" + (text != null ? text : "") + "\r";
    }

    public int getPort() {
        return port;
    }

    public List<String> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);
    }

    public int getReceivedCount() {
        return receivedMessages.size();
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

    public void setAckTextMessage(String ackTextMessage) {
        this.ackTextMessage = ackTextMessage;
    }

    public void setResponseDelayMs(long responseDelayMs) {
        this.responseDelayMs = responseDelayMs;
    }

    public void setCloseSocketAbruptly(boolean closeSocketAbruptly) {
        this.closeSocketAbruptly = closeSocketAbruptly;
    }

    @Override
    public void close() throws Exception {
        running.set(false);
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
