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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Socket-based client for transmitting MLLP-framed HL7 messages to a target host and port
 * and receiving synchronous HL7 ACK responses.
 */
public class MllpClient implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(MllpClient.class);

    private final String host;
    private final int port;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final Charset charset;
    private final int maxRetries;
    private final long retryDelayMs;

    private Socket persistentSocket;
    private InputStream socketIn;
    private OutputStream socketOut;
    private final boolean keepAlive;

    public MllpClient(String host, int port) {
        this(host, port, MllpConstants.DEFAULT_CONNECT_TIMEOUT_MS, MllpConstants.DEFAULT_READ_TIMEOUT_MS,
                MllpConstants.DEFAULT_CHARSET, 0, 1000L, false);
    }

    public MllpClient(String host, int port, int connectTimeoutMs, int readTimeoutMs) {
        this(host, port, connectTimeoutMs, readTimeoutMs, MllpConstants.DEFAULT_CHARSET, 0, 1000L, false);
    }

    public MllpClient(String host, int port, int connectTimeoutMs, int readTimeoutMs,
                      Charset charset, int maxRetries, long retryDelayMs, boolean keepAlive) {
        this.host = Objects.requireNonNull(host, "host must not be null");
        this.port = port;
        this.connectTimeoutMs = connectTimeoutMs > 0 ? connectTimeoutMs : MllpConstants.DEFAULT_CONNECT_TIMEOUT_MS;
        this.readTimeoutMs = readTimeoutMs > 0 ? readTimeoutMs : MllpConstants.DEFAULT_READ_TIMEOUT_MS;
        this.charset = charset != null ? charset : MllpConstants.DEFAULT_CHARSET;
        this.maxRetries = Math.max(0, maxRetries);
        this.retryDelayMs = Math.max(0L, retryDelayMs);
        this.keepAlive = keepAlive;
    }

    /**
     * Transmits an HL7 message framed in MLLP and waits for the ACK response.
     *
     * @param hl7Message raw HL7 message
     * @return raw HL7 ACK string received from the server
     * @throws MllpException on connection, timeout, or protocol error
     */
    public synchronized String sendAndReceive(String hl7Message) {
        Objects.requireNonNull(hl7Message, "hl7Message must not be null");

        int attempts = 0;
        Exception lastException = null;

        while (attempts <= maxRetries) {
            attempts++;
            long startTime = System.currentTimeMillis();
            Socket socket = null;
            boolean isPersistent = this.keepAlive && this.persistentSocket != null && !this.persistentSocket.isClosed();

            try {
                if (isPersistent) {
                    socket = this.persistentSocket;
                } else {
                    socket = createSocket();
                    if (this.keepAlive) {
                        this.persistentSocket = socket;
                        this.socketIn = new BufferedInputStream(socket.getInputStream());
                        this.socketOut = new BufferedOutputStream(socket.getOutputStream());
                    }
                }

                InputStream in = isPersistent ? this.socketIn : new BufferedInputStream(socket.getInputStream());
                OutputStream out = isPersistent ? this.socketOut : new BufferedOutputStream(socket.getOutputStream());

                // Send MLLP framed message
                LOG.debug("[MLLP Client] Sending message to {}:{} (attempt {}/{})", host, port, attempts, maxRetries + 1);
                MllpFrameCodec.writeFrame(out, hl7Message, charset);

                // Receive MLLP framed ACK
                String ack = MllpFrameCodec.readFrame(in, charset);
                long duration = System.currentTimeMillis() - startTime;

                if (ack == null) {
                    throw new MllpConnectionException("Server at " + host + ":" + port + " closed connection without sending ACK");
                }

                LOG.debug("[MLLP Client] Received ACK from {}:{} in {} ms", host, port, duration);
                return ack;

            } catch (SocketTimeoutException e) {
                lastException = new MllpTimeoutException("MLLP read timed out after " + readTimeoutMs + " ms waiting for response from " + host + ":" + port, e);
                closePersistentSocket();
            } catch (IOException e) {
                lastException = new MllpConnectionException("MLLP connection/transmission failed to " + host + ":" + port + ": " + e.getMessage(), e);
                closePersistentSocket();
            } finally {
                if (!keepAlive && socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                }
            }

            if (attempts <= maxRetries && retryDelayMs > 0) {
                LOG.warn("[MLLP Client] Send failed to {}:{} (attempt {}/{}). Retrying in {} ms...",
                        host, port, attempts, maxRetries + 1, retryDelayMs);
                try {
                    Thread.sleep(retryDelayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new MllpException("MLLP retry interrupted", ie);
                }
            }
        }

        if (lastException instanceof MllpException) {
            throw (MllpException) lastException;
        }
        throw new MllpException("Failed to send MLLP message after " + attempts + " attempts", lastException);
    }

    /**
     * Transmits an HL7 message without waiting for an ACK (fire-and-forget).
     *
     * @param hl7Message raw HL7 message
     */
    public synchronized void sendOneWay(String hl7Message) {
        Objects.requireNonNull(hl7Message, "hl7Message must not be null");
        try (Socket socket = createSocket()) {
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            MllpFrameCodec.writeFrame(out, hl7Message, charset);
        } catch (IOException e) {
            throw new MllpConnectionException("Failed to send one-way MLLP message to " + host + ":" + port + ": " + e.getMessage(), e);
        }
    }

    private Socket createSocket() throws IOException {
        Socket socket = new Socket();
        socket.setReuseAddress(true);
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(readTimeoutMs);
        socket.connect(new InetSocketAddress(host, port), connectTimeoutMs);
        return socket;
    }

    private synchronized void closePersistentSocket() {
        if (persistentSocket != null) {
            try {
                persistentSocket.close();
            } catch (IOException ignored) {
            }
            persistentSocket = null;
            socketIn = null;
            socketOut = null;
        }
    }

    @Override
    public synchronized void close() {
        closePersistentSocket();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public Charset getCharset() {
        return charset;
    }
}
