/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.text.CaseKind;
import walkingkooka.text.CaseSensitivity;

import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparator} that supports comparing text with embedded numbers so letters with number sequences are sorted
 * by the numeric value. Text without digits is compared lexicographically.
 * <pre>
 * abc < def
 * ABC < DEF
 *
 * A123 > A45
 * A123BC > A45BC
 * A0001 == A1
 * A100 > A20
 * </pre>
 */
abstract class TextWithNumbersComparator implements Comparator<CharSequence> {

    TextWithNumbersComparator() {
        super();
    }

    @Override
    public final int compare(final CharSequence left,
                             final CharSequence right) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");

        int result = Comparators.EQUAL;

        int leftLength = left.length();
        int rightLength = right.length();

        int leftPos = 0;
        int rightPos = 0;

        Loop:
        do {
            if (leftPos == leftLength) {
                result = rightPos - rightLength;
                break;
            }

            if (rightPos == rightLength) {
                result = leftLength - leftPos;
                break;
            }

            final int leftNumberStart = skipZeroDigits(left, leftPos);
            final int rightNumberStart = skipZeroDigits(right, rightPos);

            // left and right are NOT numbers compare characters
            if (-1 == leftNumberStart && -1 == rightNumberStart) {
                result = this.compare(
                    left.charAt(leftPos),
                    right.charAt(rightPos)
                );
                leftPos++;
                rightPos++;
                continue;
            }

            // left AND right are numbers
            if (-1 != leftNumberStart && -1 != rightNumberStart) {
                // both left and right are numbers...
                final int leftNumberEnd = findLastDigit(left, leftNumberStart);
                final int rightNumberEnd = findLastDigit(right, rightNumberStart);

                // different lengths ? longer number must be greater
                final int leftNumberLength = leftNumberEnd - leftNumberStart;
                final int rightNumberLength = rightNumberEnd - rightNumberStart;

                result = leftNumberLength - rightNumberLength;
                if (0 != result) {
                    break;
                }

                // compare digits(characters) from end to start.
                int numberOffset = leftNumberLength - 1;

                while (numberOffset >= 0) {
                    result = left.charAt(leftNumberStart + numberOffset) - right.charAt(rightNumberStart + numberOffset);
                    if (Comparators.EQUAL != result) {
                        break Loop;
                    }

                    numberOffset--;
                }

                // equal continue AFTER number ends.
                leftPos = leftNumberEnd;
                rightPos = rightNumberEnd;
            } else {
                // left is NOT a number therefore right is LESS
                result = 0 == leftNumberStart ?
                    Comparators.LESS :
                    Comparators.MORE;
                break;
            }
        } while (Comparators.EQUAL == result);

        return result;
    }

    final int compare(final char left,
                      final char right) {
        return this.caseSensitivity()
            .compare(
                left,
                right
            );
    }

    abstract CaseSensitivity caseSensitivity();

    private static int skipZeroDigits(final CharSequence text,
                                      final int pos) {
        int nonZero = -1;

        final int length = text.length();

        int i = pos;

        while (i < length) {
            final char c = text.charAt(i);
            final int value = digitNumericValue(c);
            if (value < 0) {
                break;
            }
            nonZero = i;
            if (0 != value) {
                break;
            }

            i++;
        }

        return nonZero;
    }

    private static int findLastDigit(final CharSequence text,
                                     final int pos) {
        final int length = text.length();

        int nonDigit = length;

        int i = pos;

        while (i < length) {
            final char c = text.charAt(i);
            final int value = digitNumericValue(c);
            if (value < 0) {
                nonDigit = i;
                break;
            }

            i++;
        }

        return nonDigit;
    }

    private static int digitNumericValue(final char c) {
        return Character.digit(
            c,
            10
        );
    }

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return TextWithNumbersComparator.class.getSimpleName() +
            "(Case" +
            CaseKind.TITLE.change(
                this.caseSensitivity()
                    .name(),
                CaseKind.PASCAL
            )
            + ")";
    }

    // class............................................................................................................


}
