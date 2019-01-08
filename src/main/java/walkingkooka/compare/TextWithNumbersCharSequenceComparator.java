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

import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparator} that makes it easy to sort file listings and the like so numbers within
 * filenames are sorted in a sensible fashion.
 *
 * <pre>
 * file001
 * file4
 * file030
 * </pre>
 */
final class TextWithNumbersCharSequenceComparator<C extends CharSequence>
        implements Comparator<C>, HashCodeEqualsDefined, Serializable {

    /**
     * Factory that creates a new {@link TextWithNumbersCharSequenceComparator}
     */
    static <S extends CharSequence> TextWithNumbersCharSequenceComparator<S> with(final CaseSensitivity sensitivity,
                                                                                  final Parser<BigDecimalParserToken, ParserContext> decimal) {
        Objects.requireNonNull(sensitivity, "sensitivity");
        Objects.requireNonNull(decimal, "decimal");

        return new TextWithNumbersCharSequenceComparator<S>(sensitivity, decimal);
    }

    /**
     * Private ctor use factory
     */
    private TextWithNumbersCharSequenceComparator(final CaseSensitivity sensitivity,
                                                  final Parser<BigDecimalParserToken, ParserContext> decimal) {
        super();
        this.caseSensitivity = sensitivity;
        this.decimal = decimal;
    }

    // Comparator

    @Override
    public int compare(final C chars1, final C chars2) {
        final TextWithNumbersCharSequenceComparatorState state
                = new TextWithNumbersCharSequenceComparatorState(chars1, chars2, this);
        final int length1 = chars1.length();
        final int length2 = chars2.length();

        TextWithNumbersCharSequenceComparatorMode mode = TextWithNumbersCharSequenceComparatorMode.NON_DIGITS;

        // keep trying until mode is stop or we run out of characters for of or both chars.
        do {
            final int pos1 = state.pos1;
            final int pos2 = state.pos2;
            if (pos1 == length1 || pos2 == length2) {
                // end of both reached must be equal
                if (pos1 == length1 && pos2 == length2) {
                    state.result = Comparators.EQUAL;
                } else {
                    state.result = pos1 == length1 ?
                            Comparators.LESS :
                            Comparators.MORE;
                }
                break;
            } else {
                // neither is exhausted
                mode = mode.compare(chars1.charAt(pos1), chars2.charAt(pos2), state);
            }
        } while (TextWithNumbersCharSequenceComparatorMode.STOP != mode);

        return state.result;
    }

    /**
     * Compare the two characters using the {@link CaseSensitivity}
     */
    int compare(final char c, final char d) {
        return this.caseSensitivity.compare(c, d);
    }

    private final CaseSensitivity caseSensitivity;
    final Parser<BigDecimalParserToken, ParserContext> decimal;

    // Object

    @Override
    public int hashCode() {
        return Objects.hash(this.caseSensitivity, this.decimal);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof TextWithNumbersCharSequenceComparator && this.equals0((TextWithNumbersCharSequenceComparator) other);
    }

    private boolean equals0(final TextWithNumbersCharSequenceComparator<?> other) {
        return this.caseSensitivity == other.caseSensitivity &&
                this.decimal.equals(other.decimal);
    }

    @Override
    public String toString() {
        return "text with numbers(CASE " + this.caseSensitivity + ")";
    }

    // Serializable

    private static final long serialVersionUID = 5479671780950129435L;
}
