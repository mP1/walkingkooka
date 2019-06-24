/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.email;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.net.HostAddress;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Value} that holds a valid email address. Getters are available to get the components. Note that email address characters are validated
 * using the rules from the RFC as detailed on http://en.wikipedia.org/wiki/Email_address. <br>
 * NOTE Note that 8 tests from DominicsayersComIsemailEmailAddressTest still fail. Doing some research to find out if surrounding addresses
 * with [] is for emails only.
 */
final public class EmailAddress implements Value<String>,
        Comparable<EmailAddress>,
        HashCodeEqualsDefined,
        HasJsonNode,
        Serializable {

    /**
     * Tries to parse the email or returns an {@link Optional#empty()}.
     */
    public static Optional<EmailAddress> tryParse(final String email) {
        return EmailAddressParserTryParse.tryParse(email);
    }

    /**
     * Creates and validates the given email.
     */
    public static EmailAddress parse(final String email) {
        return EmailAddressParserWith.parseOrFail(email);
    }

    /**
     * The entire email cannot be longer than this.
     */
    final static int MAX_EMAIL_LENGTH = 255;

    /**
     * The maximum number of characters that can appear in as a user name in an email.
     */
    final static int MAX_LOCAL_LENGTH = 65;

    /**
     * Message when an email is missing a user.
     */
    static String missingUser(final String address) {
        return "Email missing user=" + CharSequences.quote(address);
    }

    /**
     * Message when an email is missing a host.
     */
    static String missingHost(final String address) {
        return "Email missing host=" + CharSequences.quote(address);
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

    // HasJsonNode...............................................................................

    /**
     * Accepts a json string holding an email.
     */
    public static EmailAddress fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return parse(node.stringValueOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.toString());
    }

    static {
        HasJsonNode.register("email", EmailAddress::fromJsonNode, EmailAddress.class);
    }

    // Object....................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.user, this.host);
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
                other instanceof EmailAddress && this.equals0(Cast.to(other));
    }

    private boolean equals0(final EmailAddress other) {
        return this.user.equals(other.user) && this.host.equals(other.host);
    }

    /**
     * Dumps the address as is without quotes etc.
     */
    @Override
    public String toString() {
        return this.address;
    }

    // Comparable....................................................................................................

    @Override
    public int compareTo(final EmailAddress other) {
        int result = this.user.compareTo(other.user);
        if (0 == result) {
            result = this.host.compareTo(other.host);
        }
        return result;
    }

    // Serializable....................................................................................................

    private final static long serialVersionUID = 1L;
}
