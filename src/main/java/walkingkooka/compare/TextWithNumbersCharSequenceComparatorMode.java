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

enum TextWithNumbersCharSequenceComparatorMode {
    /**
     * Compares characters of letters unless they are digits which switches modes.
     */
    NON_DIGITS {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1, final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState job) {
            TextWithNumbersCharSequenceComparatorMode next;

            // they may both be digits.
            final int value1 = TextWithNumbersCharSequenceComparatorMode.digit(c1);
            final int value2 = TextWithNumbersCharSequenceComparatorMode.digit(c2);
            final boolean digit1
                    = TextWithNumbersCharSequenceComparatorMode.isDigit(value1);
            final boolean digit2
                    = TextWithNumbersCharSequenceComparatorMode.isDigit(value2);
            if (digit1 && digit2) {
                if (('0' == c1) || ('0' == c2)) {
                    next = SKIP_LEADING_ZEROS;
                } else {
                    next = WHOLE_NUMBER_DIGITS;
                }
            } else {
                next = this.compareNonDigits(c1, c2, job);
            }
            return next;
        }
    },
    /**
     * Skips if any leading zeros for both {@link CharSequence} advancing passed them. The character
     * after may or may not be a digit.
     */
    SKIP_LEADING_ZEROS {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1, final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState job) {
            if ('0' == c1) {
                job.pos1 = this.skipZeroes(job.chars1, 1 + job.pos1);
            }
            if ('0' == c2) {
                job.pos2 = this.skipZeroes(job.chars2, 1 + job.pos2);
            }
            return WHOLE_NUMBER_DIGITS;
        }

        /**
         * Skips over zero characters in the {@link CharSequence}
         */
        private int skipZeroes(final CharSequence chars, final int i) {
            final int length = chars.length();

            int j = i;
            while (j < length) {
                if (chars.charAt(j) != '0') {
                    break;
                }
                j++;
            }
            return j;
        }
    },
    /**
     * Compares and consumes any digits, if a decimal for both then a switch is made to
     * FRACTIONAL_NUMBERS otherwise its back to NON_DIGITS
     */
    WHOLE_NUMBER_DIGITS {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1, final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState state) {
            // count the number of digits for both
            final int pos1 = state.pos1;
            final CharSequence chars1 = state.chars1;
            final int digitCount1 = this.countDigits(chars1, pos1);

            final int pos2 = state.pos2;
            final CharSequence chars2 = state.chars2;
            final int digitCount2 = this.countDigits(chars2, pos2);

            // the longer run of digits must be the larger...
            state.result = digitCount1 - digitCount2;

            TextWithNumbersCharSequenceComparatorMode next = null;
            if (Comparators.EQUAL != state.result) {
                next = STOP;
            } else {
                next = MAYBE_DECIMAL;

                // same number of digits need to check them of by of.
                for (int i = 0; i < digitCount1; i++) {
                    final int result = chars1.charAt(i + pos1) - chars2.charAt(i + pos2);
                    if (Comparators.EQUAL != result) {
                        state.result = result;
                        next = STOP;
                        break;
                    }
                }

                state.pos1 += digitCount1;
                state.pos2 += digitCount2;
            }
            return next;
        }

        private int countDigits(final CharSequence chars, final int pos) {
            int j = pos;

            final int stop = chars.length();
            while (j < stop) {
                if (false == TextWithNumbersCharSequenceComparatorMode.isDigit(chars.charAt(
                        j))) {
                    break;
                }
                j++;
            }

            return j - pos;
        }
    },
    /**
     * Handles comapring the fractional digits after the decimal point.
     */
    MAYBE_DECIMAL {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1, final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState job) {
            return this.compareMaybeDigits(c1, c2, job);
        }

    },
    /**
     * Handles comapring the fractional digits after the decimal point.
     */
    FRACTIONAL_DIGITS {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1, final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState job) {
            return this.compareMaybeDigits(c1, c2, job);
        }
    },
    /**
     * A sentinel used to denote that the result is ready in the {@link
     * TextWithNumbersCharSequenceComparatorState}.
     */
    STOP {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1, final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState job) {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * This is called without either {@link CharSequence} being exhaused.
     */
    abstract TextWithNumbersCharSequenceComparatorMode compare(char c1, char c2,
                                                               TextWithNumbersCharSequenceComparatorState job);

    /**
     * Assumes both characters are digits and compares them of values.
     */
    final TextWithNumbersCharSequenceComparatorMode compareNonDigits(final char c1, final char c2,
                                                                     final TextWithNumbersCharSequenceComparatorState job) {
        job.result = job.comparator.compareNonDigits(c1, c2);

        TextWithNumbersCharSequenceComparatorMode next = null;
        if (Comparators.EQUAL == job.result) {
            job.next();
            next = this;
        } else {
            next = STOP;
        }
        return next;
    }

    /**
     * After testing if both are digits then compares their values.
     */
    final TextWithNumbersCharSequenceComparatorMode compareMaybeDigits(final char c1, final char c2,
                                                                       final TextWithNumbersCharSequenceComparatorState job) {
        // they may both be digits.
        final int value1 = TextWithNumbersCharSequenceComparatorMode.digit(c1);
        final int value2 = TextWithNumbersCharSequenceComparatorMode.digit(c2);
        final boolean digit1 = TextWithNumbersCharSequenceComparatorMode.isDigit(value1);
        final boolean digit2 = TextWithNumbersCharSequenceComparatorMode.isDigit(value2);

        TextWithNumbersCharSequenceComparatorMode next = null;

        // compare values if both are digits
        if (digit1 && digit2) {
            job.result = value1 - value2;
            if (Comparators.EQUAL == job.result) {
                next = this;
                job.next();
            } else {
                // give up job.result has the "result"
                next = STOP;
            }
        } else {
            if (digit1) {
                job.result = Comparators.MORE;
                next = STOP;
            } else {
                if (digit2) {
                    job.result = Comparators.LESS;
                    next = STOP;
                } else {
                    next = this.compareNonDigits(c1, c2, job);
                }
            }

        }
        return next;
    }

    static int digit(final char c) {
        return Character.digit(c, 10);
    }

    static boolean isDigit(final char c) {
        return Character.isDigit(c);
    }

    static boolean isDigit(final int value) {
        return value >= 0;
    }
}