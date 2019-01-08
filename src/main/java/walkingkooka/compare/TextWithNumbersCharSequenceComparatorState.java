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

package walkingkooka.compare;

import walkingkooka.text.CharSequences;

import java.math.BigDecimal;

/**
 * Only holds both {@link CharSequence} and the next character index for each.
 */
final class TextWithNumbersCharSequenceComparatorState {

    TextWithNumbersCharSequenceComparatorState(final CharSequence chars1,
                                               final CharSequence chars2,
                                               final TextWithNumbersCharSequenceComparator<?> comparator) {
        super();
        this.chars1 = chars1;
        this.pos1 = 0;
        this.chars2 = chars2;
        this.pos2 = 0;
        this.comparator = comparator;
    }

    /**
     * Advances the position within for both {@link CharSequence}.
     */
    void next() {
        this.pos1++;
        this.pos2++;
    }

    boolean isDecimal(final char c) {
        return this.comparator.decimal.test(c);
    }

    final CharSequence chars1;

    final CharSequence chars2;

    final TextWithNumbersCharSequenceComparator<?> comparator;

    int pos1;

    int pos2;

    int result;

    BigDecimal value;

    @Override
    public String toString() {
        return this.highlight(this.chars1, this.pos1) +
                " " +
                this.highlight(this.chars2, this.pos2);
    }

    private CharSequence highlight(final CharSequence chars, final int pos) {
        final int length = chars.length();
        return CharSequences.quoteAndEscape(pos > length ?
                chars + "[]" :
                chars.subSequence(0, pos) +
                        "[" + chars.charAt(pos) + "]" +
                        chars.subSequence(pos + 1, length));
    }
}