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

import walkingkooka.InvalidCharacterException;
import walkingkooka.text.CharSequences;

/**
 * The {@link EmailAddressParser} which throws an {@link IllegalArgumentException} on any failed tests.
 */
final class EmailAddressParserWith extends EmailAddressParser {

    static EmailAddress parseOrFail(final String email) {
        return new EmailAddressParserWith().parse(email);
    }

    private EmailAddressParserWith() {
        super();
    }

    @Override
    void emailTooLong(final String email) {
        this.fail(EmailAddress.EMAIL_TOO_LONG + " " + CharSequences.quoteAndEscape(email));
    }

    @Override
    void missingUser(final String email) {
        this.fail(EmailAddress.missingUser(email));
    }

    @Override
    void userNameTooLong(final int length, final String email) {
        this.fail(EmailAddress.userNameTooLong(length, email));
    }

    @Override
    void missingHost(final String email) {
        this.fail(EmailAddress.missingHost(email));
    }

    @Override
    void invalidCharacter(final int at, final String email) {
        throw new InvalidCharacterException(email, at);
    }

    @Override
    void invalidHostAddress(final IllegalArgumentException failed) {
        throw failed;
    }

    private void fail(final String message) {
        throw new IllegalArgumentException(message);
    }

    @Override
    public String toString() {
        return "EmailAddress.with";
    }
}
