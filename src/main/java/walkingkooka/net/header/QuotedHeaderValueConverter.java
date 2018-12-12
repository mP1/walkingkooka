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

/**
 * Base class for any converter that handles both quoted and unquoted string values.
 */
abstract class QuotedHeaderValueConverter<V> extends HeaderValueConverter2<V> {

    /**
     * Package private to limit sub classing.
     */
    QuotedHeaderValueConverter() {
        super();
    }

    @Override
    final V parse0(final String value, final Name name) {
        return value.charAt(0) == DOUBLE_QUOTE ?
                this.parseQuoted(value) :
                this.parseUnquoted(value);
    }

    /**
     * Verifies the contents and ending double quote of the given characters.
     */
    private V parseQuoted(final String text) {
        final int last = text.length() - 1;

        final StringBuilder raw = new StringBuilder();
        final boolean escapingAllowed = this.allowBackslashEscaping();
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
            if (BACKSLASH == c && escapingAllowed) {
                escaped = true;
                continue;
            }
            if (this.isCharacterWithinQuotes(c)) {
                raw.append(c);
                continue;
            }
            throw new InvalidCharacterException(text, i);
        }

        if (escaped) {
            throw new InvalidCharacterException(text, text.length() - 1);
        }

        final char c = text.charAt(last);
        if (DOUBLE_QUOTE != c) {
            throw new InvalidCharacterException(text, last);
        }

        return this.createQuotedValue(raw.toString(), text);
    }

    /**
     * Must return true if backslash escaping is allowed in quotes.
     */
    abstract boolean allowBackslashEscaping();

    /**
     * Returns true for all valid characters within quotes.
     */
    abstract boolean isCharacterWithinQuotes(final char c);

    /**
     * Factory that returns the final quoted product.
     */
    abstract V createQuotedValue(final String raw, final String text);

    /**
     * Verifies the content of an unquoted text.
     */
    private V parseUnquoted(final String text) {
        int i = 0;
        for (char c : text.toCharArray()) {
            if (!this.isCharacterWithoutQuotes(c)) {
                throw new InvalidCharacterException(text, i);
            }
            i++;
        }
        return this.createUnquotedValue(text);
    }

    /**
     * Returns true for all acceptable unquoted characters.
     */
    abstract boolean isCharacterWithoutQuotes(final char c);

    /**
     * Factory that returns the final unquoted product raw.
     */
    abstract V createUnquotedValue(final String raw);

    // toText..............................................................................................

    /**
     * Escapes if necessary and auto quotes the given text.
     */
    final String toTextMaybeEscapeAndQuote(final String value, final Name name) {
        StringBuilder b = new StringBuilder();

        final boolean allowBackslashEscaping = this.allowBackslashEscaping();
        boolean quotesRequired = false;

        for (char c : value.toCharArray()) {
            if (allowBackslashEscaping) {
                if (BACKSLASH == c || DOUBLE_QUOTE == c) {
                    b.append(BACKSLASH);
                    b.append(c);
                    quotesRequired = true;
                    continue;
                }
            }
            if (!this.isCharacterWithoutQuotes(c)) {
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

    private final static char BACKSLASH = '\\';
    private final static char DOUBLE_QUOTE = '"';
}
