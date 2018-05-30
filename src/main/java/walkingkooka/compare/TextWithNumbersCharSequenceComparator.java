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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;

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
    static <S extends CharSequence> TextWithNumbersCharSequenceComparator<S> with(
            final CaseSensitivity sensitivity, final CharPredicate decimal) {
        Objects.requireNonNull(sensitivity, "sensitivity");
        Objects.requireNonNull(decimal, "decimal");

        return new TextWithNumbersCharSequenceComparator<S>(sensitivity, decimal);
    }

    /**
     * Private ctor use factory
     */
    private TextWithNumbersCharSequenceComparator(final CaseSensitivity sensitivity,
                                                  final CharPredicate decimal) {
        super();
        this.caseSensitivity = sensitivity;
        this.decimal = decimal;
    }

    // Comparator

    @Override
    public int compare(final C chars1, final C chars2) {
        final TextWithNumbersCharSequenceComparatorState job
                = new TextWithNumbersCharSequenceComparatorState(chars1, chars2, this);
        final int length1 = chars1.length();
        final int length2 = chars2.length();

        TextWithNumbersCharSequenceComparatorMode mode
                = TextWithNumbersCharSequenceComparatorMode.NON_DIGITS;

        // keep trying until mode is stop or we run out of characters for of or both chars.
        do {
            final int pos1 = job.pos1;
            final int pos2 = job.pos2;
            if ((pos1 == length1) || (pos2 == length2)) {
                if ((pos1 == length1) && (pos2 == length2)) {
                    job.result = Comparators.EQUAL;
                } else {
                    job.result = pos1 == length1 ? Comparators.LESS : Comparators.MORE;
                }
                break;
            } else {
                // neither is exhausted
                mode = mode.compare(chars1.charAt(pos1), chars2.charAt(pos2), job);
            }
        } while (TextWithNumbersCharSequenceComparatorMode.STOP != mode);

        return job.result;
    }

    /**
     * Tests if the character is actuallyt the decimal point.
     */
    boolean isDecimalPoint(final char c) {
        return this.decimal.test(c);
    }

    private final CharPredicate decimal;

    /**
     * Compare the two characters using the {@link CaseSensitivity}
     */
    int compareNonDigits(final char c, final char d) {
        return this.caseSensitivity.compare(c, d);
    }

    /**
     * Compares two characters using the {@link CaseSensitivity}
     */
    int compare(final char c, final char other) {
        return this.caseSensitivity.compare(c, other);
    }

    private final CaseSensitivity caseSensitivity;

    // Object

    @Override
    public int hashCode() {
        return this.caseSensitivity.hashCode() ^ this.decimal.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof TextWithNumbersCharSequenceComparator)
                && this.equals0((TextWithNumbersCharSequenceComparator) other));
    }

    private boolean equals0(final TextWithNumbersCharSequenceComparator<?> other) {
        return this.caseSensitivity == other.caseSensitivity
                && this.decimal.equals(other.decimal);
    }

    @Override
    public String toString() {
        return "text with numbers(CASE " + this.caseSensitivity + ")";
    }

    // Serializable

    private static final long serialVersionUID = 5479671780950129435L;
}
