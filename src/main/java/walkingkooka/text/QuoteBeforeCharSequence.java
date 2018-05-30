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
 * A {@link CharSequence} that adds a double quote before another {@link CharSequence}.
 */
final class QuoteBeforeCharSequence extends QuotedCharSequence<QuoteBeforeCharSequence> {

    private static final long serialVersionUID = 3443044283286719532L;

    static CharSequence with(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return chars.length() == 0 ? "\"" : new QuoteBeforeCharSequence(chars);
    }

    /**
     * Private constructor use static factory.
     */
    private QuoteBeforeCharSequence(final CharSequence chars) {
        super(chars);
    }

    @Override
    int quoteCount() {
        return 1;
    }

    @Override
    char charAtIndex(final int index) {
        char c = '"';

        if (index > 0) {
            c = this.charSequence().charAt(index - 1);
        }

        return c;
    }

    @Override
    CharSequence doSubSequence(final int start, final int end) {
        CharSequence chars = this.charSequence();

        if (start == 0) {
            chars = QuoteBeforeCharSequence.with(chars.subSequence(0, end - 1));
        } else {
            chars = chars.subSequence(start - 1, end - 1);
        }
        return chars;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof QuoteBeforeCharSequence;
    }

    @Override
    String buildToString() {
        return new StringBuilder().append('"').append(this.charSequence()).toString();
    }
}
