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

package net.fhirfactory.harmonia.mllpgatewaycli.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class MllpClientTest {

    @Test
    @DisplayName("encodeMllpFrame wraps text in <VT> payload <FS><CR>")
    void testEncodeMllpFrame() {
        String msg = "MSH|^~\\&|TEST\r";
        byte[] frame = MllpClient.encodeMllpFrame(msg);

        assertThat(frame[0]).isEqualTo((byte) 0x0B); // <VT>
        assertThat(frame[frame.length - 2]).isEqualTo((byte) 0x1C); // <FS>
        assertThat(frame[frame.length - 1]).isEqualTo((byte) 0x0D); // <CR>

        String extracted = new String(frame, 1, frame.length - 3, StandardCharsets.UTF_8);
        assertThat(extracted).isEqualTo(msg);
    }

    @Test
    @DisplayName("readMllpFrame extracts payload correctly from InputStream")
    void testReadMllpFrame() throws Exception {
        String payload = "MSH|^~\\&|APP\rMSA|AA|MSG001\r";
        byte[] frame = MllpClient.encodeMllpFrame(payload);

        InputStream in = new ByteArrayInputStream(frame);
        String result = MllpClient.readMllpFrame(in);

        assertThat(result).isEqualTo(payload);
    }

    @Test
    @DisplayName("parseAck correctly identifies AA application accept")
    void testParseAckAccept() {
        String ack = "MSH|^~\\&|HIE|HIE_IM|HIE_CLI|FACILITY_CLI|20260907150000||ACK^A01|ACK-001|P|2.4\r" +
                "MSA|AA|MSG-12345|Transaction Successful\r";

        MllpResult result = MllpClient.parseAck(ack, 45);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getAckCode()).isEqualTo("AA");
        assertThat(result.getMessageControlId()).isEqualTo("MSG-12345");
        assertThat(result.getAckText()).isEqualTo("Transaction Successful");
        assertThat(result.getDurationMs()).isEqualTo(45);
    }

    @Test
    @DisplayName("parseAck correctly identifies AE error / NACK")
    void testParseAckError() {
        String nack = "MSH|^~\\&|HIE|HIE_IM|HIE_CLI|FACILITY_CLI|20260907150000||ACK^A01|ACK-002|P|2.4\r" +
                "MSA|AE|MSG-99999|Invalid Patient ID\r";

        MllpResult result = MllpClient.parseAck(nack, 30);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAckCode()).isEqualTo("AE");
        assertThat(result.getMessageControlId()).isEqualTo("MSG-99999");
        assertThat(result.getAckText()).isEqualTo("Invalid Patient ID");
        assertThat(result.getErrorMessage()).contains("NACK received: AE");
    }

    @Test
    @DisplayName("MllpClient connects to mock TCP socket server and exchanges MLLP message/ACK")
    void testSendOverSocket() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            Thread serverThread = new Thread(() -> {
                try (Socket clientSocket = serverSocket.accept()) {
                    InputStream in = clientSocket.getInputStream();
                    OutputStream out = clientSocket.getOutputStream();

                    String received = MllpClient.readMllpFrame(in);
                    assertThat(received).contains("MSG-CLIENT-TEST");

                    String ack = "MSH|^~\\&|MOCK|MOCK|CLI|CLI|20260907||ACK|ACK-99|P|2.4\r" +
                            "MSA|AA|MSG-CLIENT-TEST|OK\r";
                    out.write(MllpClient.encodeMllpFrame(ack));
                    out.flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            serverThread.start();

            MllpClient client = new MllpClient("127.0.0.1", port, 2000);
            String testMessage = "MSH|^~\\&|CLI|CLI|MOCK|MOCK|20260907||ADT^A01|MSG-CLIENT-TEST|P|2.4\rEVN|A01|20260907\r";
            MllpResult result = client.sendMessage(testMessage);

            serverThread.join();

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getAckCode()).isEqualTo("AA");
            assertThat(result.getMessageControlId()).isEqualTo("MSG-CLIENT-TEST");
        }
    }
}
