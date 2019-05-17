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

import walkingkooka.net.HostAddress;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicateBuilder;
import walkingkooka.predicate.character.CharPredicates;

import java.util.Objects;

/**
 * A parser which accepts a {@link String}, all failures call a method which may or may not throw an exception.
 */
abstract class EmailAddressParser {

    EmailAddressParser() {
        super();
    }

    final EmailAddress parse(final String email) {
        Objects.requireNonNull(email, "email");

        EmailAddress emailAddress = null;

        Exit:
        //
        do {
            if (email.length() >= EmailAddress.MAX_EMAIL_LENGTH) {
                this.emailTooLong(email);
                break;
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
                if (('.' != c) && USERNAME_CHARACTERS.test(c)) {
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
                    if (QUOTABLE_USERNAME_CHARACTERS.test(c)) {
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
                        this.invalidCharacter(i - 1, email);
                        break;
                    }
                    userNameCharacterCount += charactersSinceDot;
                    if (0 == userNameCharacterCount) {
                        this.missingUser(email);
                        break Exit;
                    }
                    // if @ is the last character complain because host is missing.
                    if ((length - 1) == i) {
                        this.missingHost(email);
                        break Exit;
                    }
                    if (userNameCharacterCount >= EmailAddress.MAX_LOCAL_LENGTH) {
                        this.userNameTooLong(userNameCharacterCount, email);
                        break Exit;
                    }
                    user = email.substring(0, i);

                    try {
                        hostAddress = HostAddress.withEmail(email, i + 1);
                    } catch (final IllegalArgumentException cause) {
                        this.invalidHostAddress(cause);
                    }
                    break;
                }

                invalidCharacter(i, email);
                break Exit;
            }
            if (null == hostAddress) {
                this.missingHost(email);
                break;
            }
            emailAddress = EmailAddress.with0(email, user, hostAddress);
        } while (false);

        return emailAddress;
    }

    private final static CharPredicate USERNAME_CHARACTERS = CharPredicateBuilder.empty()//
            .range('A', 'Z')//
            .range('a', 'z')//
            .range('0', '9')//
            .any("!#$%&'*+-/=?^_`{|}~")//
            .any('.')//
            .build();

    private final static CharPredicate QUOTABLE_USERNAME_CHARACTERS = CharPredicates.any(" <>[]:;@\\");

    /**
     * Email too long.
     */
    abstract void emailTooLong(final String email);

    /**
     * Message when an email is missing a user.
     */
    abstract void missingUser(final String email);

    /**
     * Message when a user name is too long.
     */
    abstract void userNameTooLong(final int length, final String email);

    /**
     * Message when an email is missing a host.
     */
    abstract void missingHost(final String email);

    /**
     * Message when an email contains an invalid character
     */
    abstract void invalidCharacter(final int at, final String email);

    /**
     * An invalid host address.
     */
    abstract void invalidHostAddress(final IllegalArgumentException failed);
}
