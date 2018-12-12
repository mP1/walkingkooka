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

/**
 * A {@link HeaderValueConverter} that if necessary adds quotes and escape/encodes such characters.
 * It assumes any {@link String} to be parsed have had any double quotes removed, and adds them
 * when necessary.
 * <br>
 * <a href="https://mimesniff.spec.whatwg.org/#parsing-a-mime-type">mime type</a>
 * <a href="https://fetch.spec.whatwg.org/#collect-an-http-quoted-string">Quoted string</a>
 */
final class MediaTypeParameterAutoQuotingStringHeaderValueConverter extends QuotedHeaderValueConverter<String> {

    /**
     * Singleton
     */
    final static MediaTypeParameterAutoQuotingStringHeaderValueConverter INSTANCE = new MediaTypeParameterAutoQuotingStringHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private MediaTypeParameterAutoQuotingStringHeaderValueConverter() {
        super();
    }

    @Override
    boolean allowBackslashEscaping() {
        return true;
    }

    @Override
    boolean isCharacterWithinQuotes(final char c) {
        return SPECIAL.test(c) || TOKEN.test(c);
    }

    @Override
    String createQuotedValue(final String raw, final String text) {
        return raw;
    }

    @Override
    boolean isCharacterWithoutQuotes(final char c) {
        return TOKEN.test(c);
    }

    @Override
    String createUnquotedValue(final String raw) {
        return raw;
    }

    private final static CharPredicate TOKEN = CharPredicates.rfc2045Token();
    private final static CharPredicate SPECIAL = CharPredicates.rfc2045TokenSpecial();

    @Override
    void check0(final Object value) {
        this.checkType(value, String.class);
    }

    @Override
    String toText0(final String value, final Name name) {
        return this.toTextMaybeEscapeAndQuote(value, name);
    }

    @Override
    public String toString() {
        return MediaType.class.getSimpleName() + "Parameter" + String.class.getSimpleName();
    }
}
