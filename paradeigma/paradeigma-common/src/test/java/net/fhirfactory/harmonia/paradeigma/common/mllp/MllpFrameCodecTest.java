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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MllpFrameCodecTest {

    private static final String SAMPLE_HL7 = "MSH|^~\\&|PAS|FACILITY|HARMONIA|HIE|20260915120000||ADT^A01|MSG-1001|P|2.4\r"
            + "PID|1||PAT-101^^^MRN||Smith^John\r";

    @Test
    @DisplayName("Encode wraps HL7 payload with <VT> and <FS><CR>")
    void testEncode() {
        byte[] frame = MllpFrameCodec.encode(SAMPLE_HL7);
        assertThat(frame).isNotNull();
        assertThat(frame[0]).isEqualTo(MllpConstants.START_BLOCK);
        assertThat(frame[frame.length - 2]).isEqualTo(MllpConstants.END_BLOCK);
        assertThat(frame[frame.length - 1]).isEqualTo(MllpConstants.CARRIAGE_RETURN);

        String decoded = MllpFrameCodec.decode(frame, StandardCharsets.UTF_8);
        assertThat(decoded).isEqualTo(SAMPLE_HL7);
    }

    @Test
    @DisplayName("Write and read MLLP frame from stream")
    void testStreamReadWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MllpFrameCodec.writeFrame(out, SAMPLE_HL7);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        String readMessage = MllpFrameCodec.readFrame(in);

        assertThat(readMessage).isEqualTo(SAMPLE_HL7);
    }

    @Test
    @DisplayName("Read multiple frames from continuous stream")
    void testReadMultipleFrames() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MllpFrameCodec.writeFrame(out, "MSG-1");
        MllpFrameCodec.writeFrame(out, "MSG-2");
        MllpFrameCodec.writeFrame(out, "MSG-3");

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        assertThat(MllpFrameCodec.readFrame(in)).isEqualTo("MSG-1");
        assertThat(MllpFrameCodec.readFrame(in)).isEqualTo("MSG-2");
        assertThat(MllpFrameCodec.readFrame(in)).isEqualTo("MSG-3");
        assertThat(MllpFrameCodec.readFrame(in)).isNull(); // End of stream
    }

    @Test
    @DisplayName("Handles leading junk bytes before <VT>")
    void testIgnoreLeadingJunk() throws IOException {
        byte[] junk = new byte[]{0x00, 0x20, 0x41, 0x0A};
        byte[] frame = MllpFrameCodec.encode(SAMPLE_HL7);

        byte[] combined = new byte[junk.length + frame.length];
        System.arraycopy(junk, 0, combined, 0, junk.length);
        System.arraycopy(frame, 0, combined, junk.length, frame.length);

        ByteArrayInputStream in = new ByteArrayInputStream(combined);
        String read = MllpFrameCodec.readFrame(in);
        assertThat(read).isEqualTo(SAMPLE_HL7);
    }

    @Test
    @DisplayName("Validation detects invalid frame bounds")
    void testInvalidFrame() {
        assertThat(MllpFrameCodec.isValidFrame(null)).isFalse();
        assertThat(MllpFrameCodec.isValidFrame(new byte[]{0x01, 0x02})).isFalse();
        assertThat(MllpFrameCodec.isValidFrame("plain text".getBytes(StandardCharsets.UTF_8))).isFalse();

        assertThatThrownBy(() -> MllpFrameCodec.decode("bad frame".getBytes(), StandardCharsets.UTF_8))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
