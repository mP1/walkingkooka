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
 * A {@link HeaderValueConverter} that handles quoted and unquoted text values during parsing and auto adds quotes when
 * producing the header value text.
 */
final class MediaTypeAutoQuotingStringHeaderValueConverter extends HeaderValueConverter2<String> {

    /**
     * Singleton
     */
    final static MediaTypeAutoQuotingStringHeaderValueConverter INSTANCE = new MediaTypeAutoQuotingStringHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private MediaTypeAutoQuotingStringHeaderValueConverter() {
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
        this.parseRange(text, 1, last, raw, name);

        final char c = text.charAt(last);
        if(DOUBLE_QUOTE != c) {
            throw new HeaderValueException(MediaTypeHeaderParser.invalidCharacter(last, text));
        }

        return raw.toString();
    }

    /**
     * Verifies the content of an unquoted text.
     */
    private String parseUnquoted(final String text, final Name name) {
        final StringBuilder raw = new StringBuilder();
        this.parseRange(text, 0, text.length(), raw, name);
        return raw.toString();
    }

    /**
     * Parses the range of characters verifying all are correct and honours escaping.
     */
    private void parseRange(final String text,
                            final int start,
                            final int last,
                            final StringBuilder raw,
                            final Name name) {
        boolean escaped = false;

        int i = start;
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
            // TODO possibly non ascii characters are still invalid.
            raw.append(c);

        }

        if (escaped) {
            throw new HeaderValueException(MediaTypeHeaderParser.invalidCharacter(text.length() - 1, text));
        }
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
        boolean quoteRequired = false;

        for (char c : value.toCharArray()) {
            if (BACKSLASH == c || DOUBLE_QUOTE == c) {
                b.append(BACKSLASH);
                b.append(c);
                quoteRequired = true;
                continue;
            }
            if (!isTokenCharacter(c)) {
                b.append(c);
                quoteRequired = true;
                continue;
            }
            b.append(c);
        }

        return quoteRequired ?
                b.insert(0, DOUBLE_QUOTE).append(DOUBLE_QUOTE).toString() :
                value;
    }

    private static boolean isTokenCharacter(final char c) {
        return TOKEN.test(c);
    }

    private final static CharPredicate TOKEN = CharPredicates.rfc2045Token();

    private final static char BACKSLASH = '\\';
    private final static char DOUBLE_QUOTE = '"';

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return MediaType.class.getSimpleName() + " " + String.class.getSimpleName();
    }
}
