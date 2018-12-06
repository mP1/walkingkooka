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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

/**
 * A {@link HeaderValueConverter} that only accepts {@link String} that are valid RFC2045 characters.
 * Note that empty strings are accepted.
 */
final class Rfc2045StringHeaderValueConverter extends HeaderValueConverter2<String> {

    /**
     * Singleton
     */
    final static Rfc2045StringHeaderValueConverter INSTANCE = new Rfc2045StringHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private Rfc2045StringHeaderValueConverter() {
        super();
    }

    @Override
    String parse0(final String value, final Name name) {
        return this.checkString(value);
    }

    @Override
    void check0(final Object value) {
        this.checkString(this.checkType(value, String.class));
    }

    @Override
    String toText0(final String value, final Name name) {
        return this.checkString(value);
    }

    private String checkString(final String string) {
        int i = 0;
        for(char c : string.toCharArray()) {
            if(!PREDICATE.test(c)) {
                throw new HeaderValueException("Value contains invalid character " + CharSequences.quoteIfChars(c));
            }
        }
        return string;
    }

    private final static CharPredicate PREDICATE = CharPredicates.rfc2045Token();

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return toStringType(String.class);
    }
}
