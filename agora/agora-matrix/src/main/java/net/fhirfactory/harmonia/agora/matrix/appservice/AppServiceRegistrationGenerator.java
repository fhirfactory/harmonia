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

package net.fhirfactory.harmonia.agora.matrix.appservice;

/**
 * Generator for standard Harmonia Agora Application Service registration configurations.
 */
public final class AppServiceRegistrationGenerator {

    public static final String DEFAULT_APP_SERVICE_ID = "harmonia-agora";
    public static final String DEFAULT_SENDER_LOCALPART = "_harmonia_bot";
    public static final String USER_NAMESPACE_REGEX = "@_harmonia_.*";
    public static final String ALIAS_NAMESPACE_REGEX = "#_harmonia_.*";
    public static final String ROOM_NAMESPACE_REGEX = ".*";

    private AppServiceRegistrationGenerator() {
    }

    /**
     * Generates a default registration configuration for Agora using default identifiers and namespaces.
     *
     * @param url     the callback URL where Synapse pushes AS transactions
     * @param asToken the application service authentication token
     * @param hsToken the homeserver authentication token
     * @return the configured AppServiceRegistration instance
     */
    public static AppServiceRegistration generateDefaultRegistration(String url, String asToken, String hsToken) {
        return generateRegistration(DEFAULT_APP_SERVICE_ID, url, asToken, hsToken, DEFAULT_SENDER_LOCALPART);
    }

    /**
     * Generates a registration configuration with specified identifiers and standard Harmonia namespaces.
     *
     * @param id              the Application Service ID
     * @param url             the callback URL
     * @param asToken         the AS token
     * @param hsToken         the homeserver token
     * @param senderLocalpart the localpart of the bot user
     * @return the configured AppServiceRegistration instance
     */
    public static AppServiceRegistration generateRegistration(
            String id,
            String url,
            String asToken,
            String hsToken,
            String senderLocalpart
    ) {
        return AppServiceRegistration.builder()
                .id(id != null ? id : DEFAULT_APP_SERVICE_ID)
                .url(url)
                .asToken(asToken)
                .hsToken(hsToken)
                .senderLocalpart(senderLocalpart != null ? senderLocalpart : DEFAULT_SENDER_LOCALPART)
                .rateLimited(false)
                .addUserNamespace(true, USER_NAMESPACE_REGEX)
                .addAliasNamespace(true, ALIAS_NAMESPACE_REGEX)
                .addRoomNamespace(false, ROOM_NAMESPACE_REGEX)
                .build();
    }
}
