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
import walkingkooka.text.Ascii;

/**
 * A {@link HeaderValueConverter} that handles string values in quotes with possible backslash escaping.
 */
final class QuotedStringHeaderValueConverter extends StringHeaderValueConverter {

    /**
     * Factory that creates a new {@link QuotedStringHeaderValueConverter}.
     */
    final static QuotedStringHeaderValueConverter with(final CharPredicate predicate,
                                                       final boolean supportBackslashEscaping) {
        return new QuotedStringHeaderValueConverter(predicate, supportBackslashEscaping);
    }

    /**
     * Private ctor use singleton.
     */
    private QuotedStringHeaderValueConverter(final CharPredicate predicate,
                                             final boolean supportBackslashEscaping) {
        super(predicate);
        this.supportBackslashEscaping = supportBackslashEscaping;
    }

    @Override
    String parse0(final String text, final Name name) {
        final int length = text.length();

        final int last = length - 1;

        // must start and end with double quotes...
        this.failIfNotDoubleQuotes(text, 0);

        final StringBuilder value = new StringBuilder();

        final boolean supportBackslashEscaping = this.supportBackslashEscaping;
        final CharPredicate predicate = this.predicate;

        boolean escapeNext = false;

        for (int i = 1; i < last; i++) {
            final char c = text.charAt(i);
            if(!Ascii.isPrintable(c)) {
                throw new InvalidCharacterException(text, i);
            }
            if (escapeNext) {
                value.append(c);
                escapeNext = false;
                continue;
            }
            if (BACKSLASH == c && supportBackslashEscaping) {
                escapeNext = true;
                continue;
            }
            if (!predicate.test(c)) {
                throw new InvalidCharacterException(text, i);
            }
            value.append(c);
        }

        this.failIfNotDoubleQuotes(text, last);

        return value.toString();
    }

    private void failIfNotDoubleQuotes(final String text, final int position) {
        if (DOUBLE_QUOTE != text.charAt(position)) {
            throw new InvalidCharacterException(text, position);
        }
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, String.class);
    }

    @Override
    String toText0(final String value, final Name name) {
        final boolean supportBackslashEscaping = this.supportBackslashEscaping;
        final CharPredicate predicate = this.predicate;
        final StringBuilder text = new StringBuilder();
        text.append(DOUBLE_QUOTE);

        int i = 0;
        for (char c : value.toCharArray()) {
            if (BACKSLASH == c || DOUBLE_QUOTE == c || !predicate.test(c)) {
                if (supportBackslashEscaping) {
                    text.append(BACKSLASH);
                    text.append(c);
                    continue;
                }
                throw new InvalidCharacterException(value, i);
            }
            text.append(c);
            i++;
        }

        text.append(DOUBLE_QUOTE);
        return text.toString();
    }

    private final boolean supportBackslashEscaping;

    private final static char DOUBLE_QUOTE = '"';
    private final static char BACKSLASH = '\\';
}
