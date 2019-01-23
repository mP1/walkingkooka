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

package walkingkooka.net.header;

import walkingkooka.naming.Name;

/**
 * A {@link HeaderValueConverter} that expects a header value with a single {@link TokenHeaderValue token}.
 */
final class TokenHeaderValueHeaderValueConverter extends HeaderValueConverter2<TokenHeaderValue> {

    /**
     * Singleton
     */
    final static TokenHeaderValueHeaderValueConverter INSTANCE = new TokenHeaderValueHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private TokenHeaderValueHeaderValueConverter() {
        super();
    }

    @Override
    TokenHeaderValue parse0(final String text, final Name name) {
        return TokenHeaderValue.parse(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, TokenHeaderValue.class, name);
    }

    @Override
    String toText0(final TokenHeaderValue value, final Name name) {
        return value.toHeaderText();
    }

    @Override
    public String toString() {
        return TokenHeaderValue.class.getSimpleName();
    }
}
