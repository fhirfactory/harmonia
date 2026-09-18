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

package net.fhirfactory.harmonia.agora.matrix.admin;

/**
 * Exception thrown when a Synapse Administration REST API call returns an error or fails.
 */
public class SynapseAdminException extends RuntimeException {

    private final int httpStatus;
    private final String errcode;
    private final String error;

    public SynapseAdminException(String message) {
        super(message);
        this.httpStatus = 0;
        this.errcode = null;
        this.error = null;
    }

    public SynapseAdminException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = 0;
        this.errcode = null;
        this.error = null;
    }

    public SynapseAdminException(int httpStatus, String errcode, String error, String message) {
        super(formatMessage(httpStatus, errcode, error, message));
        this.httpStatus = httpStatus;
        this.errcode = errcode;
        this.error = error;
    }

    private static String formatMessage(int httpStatus, String errcode, String error, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Synapse Admin API error [HTTP ").append(httpStatus).append("]");
        if (errcode != null && !errcode.isBlank()) {
            sb.append(" errcode=").append(errcode);
        }
        if (error != null && !error.isBlank()) {
            sb.append(" error=\"").append(error).append("\"");
        }
        if (message != null && !message.isBlank()) {
            sb.append(" - ").append(message);
        }
        return sb.toString();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrcode() {
        return errcode;
    }

    public String getError() {
        return error;
    }
}
