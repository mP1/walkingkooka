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

import java.util.Optional;

/**
 * The {@link EmailAddressParser} which ignores failed condition.
 */
final class EmailAddressParserTryParse extends EmailAddressParser {

    static Optional<EmailAddress> tryParse(final String email) {
        return Optional.ofNullable(new EmailAddressParserTryParse().parse(email));
    }

    private EmailAddressParserTryParse() {
        super();
    }

    @Override
    void emailTooLong(final String email) {
        // nop
    }

    @Override
    void missingUser(final String email) {
        // nop
    }

    @Override
    void userNameTooLong(final int length, final String email) {
        // nop
    }

    @Override
    void missingHost(final String email) {
        // nop
    }

    @Override
    void invalidCharacter(final int at, final String email) {
        // nop
    }

    @Override
    void invalidHostAddress(final RuntimeException failed) {
        //nop
    }

    @Override
    public String toString() {
        return "EmailAddress.tryParse";
    }
}
