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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicateBuilder;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link Value} that holds a valid email address. Getters are available to get the components. Note that email address characters are validated
 * using the rules from the RFC as detailed on http://en.wikipedia.org/wiki/Email_address. <br>
 * NOTE Note that 8 tests from DominicsayersComIsemailEmailAddressTest still fail. Doing some research to find out if surrounding addresses
 * with [] is for emails only.
 */
final public class EmailAddress implements Value<String>, HashCodeEqualsDefined, Serializable {

    /**
     * Creates and validates the given email.
     */
    public static EmailAddress with(final String email) {
        Objects.requireNonNull(email, "email");

        if (email.length() >= EmailAddress.MAX_EMAIL_LENGTH) {
            EmailAddress.fail(EmailAddress.EMAIL_TOO_LONG);
        }

        final int length = email.length();
        boolean quoted = false;
        int charactersSinceDot = 0;
        int userNameCharacterCount = 0;
        char previous = 0;

        HostAddress hostAddress = null;
        String user = null;

        for (int i = 0; i < length; i++) {
            final char c = email.charAt(i);
            final char wasPrevious = previous;
            previous = c;

            // found opening quote ?
            if ((false == quoted) && ('"' == c)) {
                quoted = true;
                continue;
            }
            if (('.' != c) && EmailAddress.USERNAME_CHARACTERS.test(c)) {
                charactersSinceDot++;
                continue;
            }

            if (quoted) {
                // support escaped quoted double quotes
                if ('\\' == wasPrevious) {
                    if ('"' == c) {
                        continue;
                    }
                    if ('\\' == c) {
                        previous = 0;
                        continue;
                    }
                }
                if (EmailAddress.QUOTABLE_USERNAME_CHARACTERS.test(c)) {
                    charactersSinceDot++;
                    continue;
                }
                // found closing quote
                if (c == '\"') {
                    quoted = false;
                    continue;
                }
                continue;
            }
            // only match dot if previous was not a dot.
            if (('.' == c) && (0 != charactersSinceDot)) {
                userNameCharacterCount += charactersSinceDot;
                charactersSinceDot = 0;
                continue;
            }
            if ((false == quoted) && (c == '@')) {
                if ('.' == wasPrevious) {
                    EmailAddress.fail(EmailAddress.invalidCharacter(email, i - 1));
                }
                userNameCharacterCount += charactersSinceDot;
                if (0 == userNameCharacterCount) {
                    EmailAddress.fail(EmailAddress.missingUser(email));
                }
                // if @ is the last character complain because host is missing.
                if ((length - 1) == i) {
                    EmailAddress.fail(EmailAddress.missingHost(email));
                }
                if (userNameCharacterCount > EmailAddress.MAX_LOCAL_LENGTH) {
                    EmailAddress.fail(EmailAddress.userNameTooLong(userNameCharacterCount));
                }
                user = email.substring(0, i);
                hostAddress = HostAddress.withEmail(email, i + 1);
                break;
            }

            EmailAddress.fail(EmailAddress.invalidCharacter(email, i));
        }
        if (null == hostAddress) {
            EmailAddress.fail(EmailAddress.missingHost(email));
        }
        return new EmailAddress(email, user, hostAddress);
    }

    /**
     * The entire email cannot be longer than this.
     */
    final static int MAX_EMAIL_LENGTH = 255;

    /**
     * Email is too long
     */
    final static String EMAIL_TOO_LONG = "Email too long";

    private final static CharPredicate USERNAME_CHARACTERS = CharPredicateBuilder.create()//
            .range('A', 'Z')//
            .range('a', 'z')//
            .range('0', '9')//
            .any("!#$%&'*+-/=?^_`{|}~")//
            .any('.')//
            .build();

    private final static CharPredicate QUOTABLE_USERNAME_CHARACTERS = CharPredicates.any(" <>[]:;@\\");

    /**
     * Message when an email is missing a user.
     */
    static String missingUser(final String address) {
        return "Missing user=" + CharSequences.quote(address);
    }

    /**
     * The maximum number of characters that can appear in as a user name in an email.
     */
    final static int MAX_LOCAL_LENGTH = 64;

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
     * Message when an email contains an invalid character
     */
    static String invalidCharacter(final String address, final int at) {
        return "Invalid character '" + address.charAt(at) + "' at " + at + "=" + CharSequences.quote(address);
    }

    /**
     * Used to report a failure with the email address.
     */
    static void fail(final String message) {
        throw new IllegalArgumentException(message);
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
