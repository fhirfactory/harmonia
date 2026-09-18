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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Multi-threaded TCP server listening for MLLP-framed HL7 messages,
 * invoking a configured {@link MllpMessageHandler}, and returning framed HL7 ACK responses.
 */
public class MllpServer implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(MllpServer.class);

    private final String bindHost;
    private int port;
    private final MllpMessageHandler messageHandler;
    private final Charset charset;
    private final int readTimeoutMs;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private ServerSocket serverSocket;
    private ExecutorService acceptExecutor;
    private ExecutorService workerExecutor;

    private final AtomicLong receivedCount = new AtomicLong(0);
    private final AtomicLong ackSentCount = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final CopyOnWriteArrayList<String> receivedMessages = new CopyOnWriteArrayList<>();

    public MllpServer(int port, MllpMessageHandler messageHandler) {
        this("0.0.0.0", port, messageHandler, MllpConstants.DEFAULT_CHARSET, MllpConstants.DEFAULT_READ_TIMEOUT_MS);
    }

    public MllpServer(String bindHost, int port, MllpMessageHandler messageHandler) {
        this(bindHost, port, messageHandler, MllpConstants.DEFAULT_CHARSET, MllpConstants.DEFAULT_READ_TIMEOUT_MS);
    }

    public MllpServer(String bindHost, int port, MllpMessageHandler messageHandler, Charset charset, int readTimeoutMs) {
        this.bindHost = Objects.requireNonNull(bindHost, "bindHost must not be null");
        this.port = port;
        this.messageHandler = Objects.requireNonNull(messageHandler, "messageHandler must not be null");
        this.charset = charset != null ? charset : MllpConstants.DEFAULT_CHARSET;
        this.readTimeoutMs = readTimeoutMs > 0 ? readTimeoutMs : MllpConstants.DEFAULT_READ_TIMEOUT_MS;
    }

    /**
     * Starts the MLLP TCP server and begins accepting connections.
     */
    public synchronized void start() throws IOException {
        if (running.get()) {
            return;
        }

        InetAddress addr = "0.0.0.0".equals(bindHost) ? null : InetAddress.getByName(bindHost);
        serverSocket = new ServerSocket(port, 50, addr);
        serverSocket.setReuseAddress(true);
        this.port = serverSocket.getLocalPort();
        running.set(true);

        acceptExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "mllp-acceptor-" + port);
            t.setDaemon(true);
            return t;
        });

        workerExecutor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "mllp-worker-" + port);
            t.setDaemon(true);
            return t;
        });

        acceptExecutor.submit(this::acceptLoop);
        LOG.info("[MLLP Server] Started on {}:{}", bindHost, port);
    }

    private void acceptLoop() {
        while (running.get()) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(readTimeoutMs);
                clientSocket.setTcpNoDelay(true);
                workerExecutor.submit(() -> handleClient(clientSocket));
            } catch (SocketException se) {
                if (!running.get()) {
                    break; // Server socket closed normally
                }
                LOG.error("[MLLP Server] Socket error in accept loop: {}", se.getMessage());
            } catch (IOException e) {
                if (running.get()) {
                    LOG.error("[MLLP Server] I/O error accepting connection on port {}: {}", port, e.getMessage(), e);
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        String remoteAddress = clientSocket.getRemoteSocketAddress().toString();
        LOG.debug("[MLLP Server] Client connected from {}", remoteAddress);

        try (InputStream in = new BufferedInputStream(clientSocket.getInputStream());
             OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {

            while (running.get()) {
                String rawHl7Message;
                try {
                    rawHl7Message = MllpFrameCodec.readFrame(in, charset);
                } catch (IOException e) {
                    LOG.debug("[MLLP Server] Connection closed or read failed from {}: {}", remoteAddress, e.getMessage());
                    break;
                }

                if (rawHl7Message == null) {
                    break; // Client closed connection cleanly
                }

                receivedCount.incrementAndGet();
                receivedMessages.add(rawHl7Message);
                LOG.debug("[MLLP Server] Received message (length {} chars) from {}", rawHl7Message.length(), remoteAddress);

                try {
                    String ackResponse = messageHandler.handleMessage(rawHl7Message);
                    if (ackResponse != null) {
                        MllpFrameCodec.writeFrame(out, ackResponse, charset);
                        ackSentCount.incrementAndGet();
                        LOG.debug("[MLLP Server] Returned ACK to {}", remoteAddress);
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    LOG.error("[MLLP Server] Error handling message from {}: {}", remoteAddress, e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            LOG.debug("[MLLP Server] Exception on client connection {}: {}", remoteAddress, e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }
            LOG.debug("[MLLP Server] Client disconnected from {}", remoteAddress);
        }
    }

    /**
     * Stops the MLLP TCP server and releases sockets and worker threads.
     */
    public synchronized void stop() {
        if (!running.getAndSet(false)) {
            return;
        }

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOG.warn("[MLLP Server] Error closing server socket on port {}: {}", port, e.getMessage());
        }

        if (acceptExecutor != null) {
            acceptExecutor.shutdownNow();
        }
        if (workerExecutor != null) {
            workerExecutor.shutdown();
            try {
                if (!workerExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                    workerExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                workerExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        LOG.info("[MLLP Server] Stopped on {}:{}", bindHost, port);
    }

    @Override
    public void close() {
        stop();
    }

    public boolean isRunning() {
        return running.get();
    }

    public int getPort() {
        return port;
    }

    public String getBindHost() {
        return bindHost;
    }

    public long getReceivedCount() {
        return receivedCount.get();
    }

    public long getAckSentCount() {
        return ackSentCount.get();
    }

    public long getErrorCount() {
        return errorCount.get();
    }

    public CopyOnWriteArrayList<String> getReceivedMessages() {
        return receivedMessages;
    }

    public void clearReceivedMessages() {
        receivedMessages.clear();
    }
}
