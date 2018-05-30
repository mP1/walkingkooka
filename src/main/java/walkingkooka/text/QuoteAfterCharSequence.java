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
 * A {@link CharSequence} adds a double quote after another {@link CharSequence}
 */
final class QuoteAfterCharSequence extends QuotedCharSequence<QuoteAfterCharSequence> {

    private static final long serialVersionUID = -4281458051231165298L;

    static CharSequence with(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return chars.length() == 0 ? "\"" : new QuoteAfterCharSequence(chars);
    }

    /**
     * Private constructor use static factory.
     */
    private QuoteAfterCharSequence(final CharSequence chars) {
        super(chars);
    }

    @Override
    int quoteCount() {
        return 1;
    }

    @Override
    char charAtIndex(final int index) {
        char c = '"';

        final CharSequence chars = this.charSequence();
        if (index < chars.length()) {
            c = chars.charAt(index);
        }

        return c;
    }

    @Override
    CharSequence doSubSequence(final int start, final int end) {
        CharSequence chars = this.charSequence();

        // keep closing quote
        final int last = chars.length();
        if (end <= last) {
            chars = chars.subSequence(start, end);
        } else {
            chars = QuoteAfterCharSequence.with(chars.subSequence(start, last));
        }

        return chars;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof QuoteAfterCharSequence;
    }

    @Override
    String buildToString() {
        return new StringBuilder().append(this.charSequence()).append('"').toString();
    }
}
