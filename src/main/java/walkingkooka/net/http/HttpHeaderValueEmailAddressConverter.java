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

package walkingkooka.net.http;


import walkingkooka.naming.Name;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * A {@link HttpHeaderValueConverter} that parses a header value into a {@link EmailAddress}.
 * This is useful for headers such as {@link HttpHeaderName#FROM}.
 */
final class HttpHeaderValueEmailAddressConverter extends HttpHeaderValueConverter<EmailAddress> {

    /**
     * Singleton
     */
    final static HttpHeaderValueEmailAddressConverter INSTANCE = new HttpHeaderValueEmailAddressConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpHeaderValueEmailAddressConverter() {
        super();
    }

    @Override
    EmailAddress parse0(final String value, final Name name) {
        final Optional<EmailAddress> emailAddress = EmailAddress.tryParse(value);
        if (!emailAddress.isPresent()) {
            throw new IllegalArgumentException(name + " contains invalid email " + CharSequences.quote(value));
        }
        return emailAddress.get();
    }

    @Override
    public String toString() {
        return toStringType(EmailAddress.class);
    }
}
