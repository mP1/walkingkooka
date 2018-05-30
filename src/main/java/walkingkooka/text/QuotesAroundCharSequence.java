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
 */

package walkingkooka.text;

import java.util.Objects;

/**
 * A {@link CharSequence} surrounds another {@link CharSequence} with double quotes.
 */
final class QuotesAroundCharSequence extends QuotedCharSequence<QuotesAroundCharSequence> {

    private static final long serialVersionUID = -4399387961024708428L;

    static CharSequence with(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return chars.length() == 0 ? "\"\"" : new QuotesAroundCharSequence(chars);
    }

    /**
     * Private constructor use static factory.
     */
    private QuotesAroundCharSequence(final CharSequence chars) {
        super(chars);
    }

    @Override
    int quoteCount() {
        return 2;
    }

    /**
     * If index is 0 or the last returns double quote otherwise looks up the wrapped {@link
     * CharSequence}.
     */
    @Override
    char charAtIndex(final int index) {
        char c = '"';

        if (0 != index) {
            final CharSequence chars = this.charSequence();
            if (index <= chars.length()) {
                c = chars.charAt(index - 1);
            }
        }

        return c;
    }

    /**
     * Returns another {@link CharSequence} with the start and end
     */
    @Override
    CharSequence doSubSequence(final int start, final int end) {
        CharSequence chars = this.charSequence();

        for (; ; ) {
            final int length = chars.length();

            // only the opening or closing quote were selected.
            if ((1 == end) || ((1 + length) == start)) {
                chars = "\"";
                break;
            }
            // without quotes
            if ((start > 0) && (end <= (1 + length))) {
                chars = chars.subSequence(start - 1, end - 1);
                break;
            }
            // with opening quote and without closing quote
            if (0 == start) {
                chars = QuoteBeforeCharSequence.with(chars.subSequence(0, end - 1));
                break;
            }
            // without opening quote
            chars = QuoteAfterCharSequence.with(chars.subSequence(start - 1, end - 2));
            break;
        }

        return chars;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof QuotesAroundCharSequence;
    }

    @Override
    String buildToString() {
        return new StringBuilder().append('"').append(this.charSequence()).append('"').toString();
    }
}
