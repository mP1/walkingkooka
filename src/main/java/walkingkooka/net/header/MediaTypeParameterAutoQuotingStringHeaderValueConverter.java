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

import walkingkooka.InvalidCharacterException;
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
final class MediaTypeParameterAutoQuotingStringHeaderValueConverter extends HeaderValueConverter2<String> {

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
    String parse0(final String value, final Name name) {
        return value.charAt(0) == DOUBLE_QUOTE ?
                this.parseQuoted(value, name) :
                this.parseUnquoted(value, name);
    }

    /**
     * Verifies the contents and ending double quote of the given characters.
     */
    private String parseQuoted(final String text, final Name name) {
        final int last = text.length() -1;

        final StringBuilder raw = new StringBuilder();
        boolean escaped = false;

        int i = 1;
        while (i < last) {
            final char c = text.charAt(i);
            i++;

            if (escaped) {
                raw.append(c);
                escaped = false;
                continue;
            }
            if (BACKSLASH == c) {
                escaped = true;
                continue;
            }
            if(TOKEN.test(c) || SPECIAL.test(c)) {
                raw.append(c);
                continue;
            }
            throw new InvalidCharacterException(text, i);
        }

        if (escaped) {
            throw new InvalidCharacterException(text, text.length() - 1);
        }

        final char c = text.charAt(last);
        if(DOUBLE_QUOTE != c) {
            throw new InvalidCharacterException(text, last);
        }

        return raw.toString();
    }

    /**
     * Verifies the content of an unquoted text.
     */
    private String parseUnquoted(final String text, final Name name) {
        int i = 0;
        for(char c : text.toCharArray()) {
            if(!TOKEN.test(c)) {
                throw new InvalidCharacterException(text, i);
            }
            i++;
        }
        return text;
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, String.class);
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc1341"></a>
     * <pre>
     * tspecials :=  "(" / ")" / "<" / ">" / "@"  ; Must be in
     *                        /  "," / ";" / ":" / "\" / <">  ; quoted-string,
     *                        /  "/" / "[" / "]" / "?" / "."  ; to use within
     *                        /  "="                        ; parameter values
     * </pre>
     * Backslashes and double quote characters are escaped.
     */
    @Override
    String toText0(final String value, final Name name) {
        StringBuilder b = new StringBuilder();
        boolean quotesRequired = false;

        for (char c : value.toCharArray()) {
            if (BACKSLASH == c || DOUBLE_QUOTE == c) {
                b.append(BACKSLASH);
                b.append(c);
                quotesRequired = true;
                continue;
            }
            if (!isTokenCharacter(c)) {
                b.append(c);
                quotesRequired = true;
                continue;
            }
            b.append(c);
        }

        return quotesRequired ?
                b.insert(0, DOUBLE_QUOTE).append(DOUBLE_QUOTE).toString() :
                value;
    }

    private static boolean isTokenCharacter(final char c) {
        return TOKEN.test(c);
    }

    private final static CharPredicate TOKEN = CharPredicates.rfc2045Token();
    private final static CharPredicate SPECIAL = CharPredicates.rfc2045TokenSpecial();

    private final static char BACKSLASH = '\\';
    private final static char DOUBLE_QUOTE = '"';

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return MediaType.class.getSimpleName() + "Parameter" + String.class.getSimpleName();
    }
}
