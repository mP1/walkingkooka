/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package walkingkooka.net.email;

import walkingkooka.Value;
import walkingkooka.net.HostAddress;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

import java.io.Serializable;
import java.util.Optional;

/**
 * A {@link Value} that holds a valid email address. Getters are available to get the components. Note that email address characters are validated
 * using the rules from the RFC as detailed on http://en.wikipedia.org/wiki/Email_address. <br>
 * NOTE Note that 8 tests from DominicsayersComIsemailEmailAddressTest still fail. Doing some research to find out if surrounding addresses
 * with [] is for emails only.
 */
final public class EmailAddress implements Value<String>, HashCodeEqualsDefined, Serializable {

    /**
     * Tries to parse the email or returns an {@link Optional#empty()}.
     */
    public static Optional<EmailAddress> tryParse(final String email) {
        return EmailAddressParserTryParse.tryParse(email);
    }

    /**
     * Creates and validates the given email.
     */
    public static EmailAddress with(final String email) {
        return EmailAddressParserWith.parseOrFail(email);
    }

    /**
     * The entire email cannot be longer than this.
     */
    final static int MAX_EMAIL_LENGTH = 255;

    /**
     * The message when an email is too long.
     */
    final static String EMAIL_TOO_LONG = "Email too long";

    /**
     * The maximum number of characters that can appear in as a user name in an email.
     */
    final static int MAX_LOCAL_LENGTH = 65;

    /**
     * Message when an email is missing a user.
     */
    static String missingUser(final String address) {
        return "Missing user=" + CharSequences.quote(address);
    }

    /**
     * Message when a user name is too long.
     */
    static String userNameTooLong(final int length) {
        return "User too long=" + length;
    }

    /**
     * Message when an email is missing a host.
     */
    static String missingHost(final String address) {
        return "Missing host=" + CharSequences.quote(address);
    }

    /**
     * Unconditionally creates a new {@link EmailAddress}
     */
    static EmailAddress with0(final String address, final String user, final HostAddress host) {
        return new EmailAddress(address, user, host);
    }

    /**
     * Private constructor
     */
    private EmailAddress(final String address, final String user, final HostAddress host) {
        this.address = address;
        this.user = user;
        this.host = host;
    }

    // Value

    /**
     * Getter that returns the entire email address as a {@link String}.
     */
    @Override
    public String value() {
        return this.address;
    }

    private final String address;

    // Email

    /**
     * Getter that returns only the {@link String user} portion of the email address.
     */
    public String user() {
        return this.user;
    }

    private final String user;

    /**
     * Getter that returns the {@link HostAddress} portion of the email address.
     */
    public HostAddress host() {
        return this.host;
    }

    private final HostAddress host;

    // Object

    @Override
    public int hashCode() {
        return this.user.hashCode() ^ this.host.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof EmailAddress) && this.equals((EmailAddress) other));
    }

    private boolean equals(final EmailAddress other) {
        return this.user.equals(other.user) && this.host.equals(other.host);
    }

    /**
     * Dumps the address as is without quotes etc.
     */
    @Override
    public String toString() {
        return this.address;
    }

    // Serializable

    private final static long serialVersionUID = 1L;
}
