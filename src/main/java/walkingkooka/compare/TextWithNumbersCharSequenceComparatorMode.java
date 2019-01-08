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
        TextWithNumbersCharSequenceComparatorMode compare(final char c1,
                                                          final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState state) {
            TextWithNumbersCharSequenceComparatorMode next;

            // they may both be digits.
            if (isDigit(digit(c1)) && isDigit(digit(c2))) {
                if (('0' == c1) || ('0' == c2)) {
                    next = SKIP_LEADING_ZEROS;
                } else {
                    next = WHOLE_NUMBER_DIGITS;
                }
            } else {
                next = this.compareNonDigits(c1, c2, state);
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
        TextWithNumbersCharSequenceComparatorMode compare(final char c1,
                                                          final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState state) {
            if ('0' == c1) {
                state.pos1 = this.skipZeroes(state.chars1, 1 + state.pos1);
            }
            if ('0' == c2) {
                state.pos2 = this.skipZeroes(state.chars2, 1 + state.pos2);
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
        TextWithNumbersCharSequenceComparatorMode compare(final char c1,
                                                          final char c2,
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

            TextWithNumbersCharSequenceComparatorMode next;
            if (Comparators.EQUAL != state.result) {
                next = STOP;
            } else {
                next = MAYBE_DECIMAL;

                // same number of digits need to check them of by of.
                for (int i = 0; i < digitCount1; i++) {
                    final char c = chars1.charAt(i + pos1);
                    final char d = chars2.charAt(i + pos2);

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
                if (false == isDigit(chars.charAt(j))) {
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
        TextWithNumbersCharSequenceComparatorMode compare(final char c1,
                                                          final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState state) {
            return this.compareMaybeDigits(c1, c2, state);
        }

    },
    /**
     * Handles comapring the fractional digits after the decimal point.
     */
    FRACTIONAL_DIGITS {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1,
                                                          final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState state) {
            return this.compareMaybeDigits(c1, c2, state);
        }
    },
    /**
     * A sentinel used to denote that the result is ready in the {@link
     * TextWithNumbersCharSequenceComparatorState}.
     */
    STOP {
        @Override
        TextWithNumbersCharSequenceComparatorMode compare(final char c1,
                                                          final char c2,
                                                          final TextWithNumbersCharSequenceComparatorState state) {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * This is called without either {@link CharSequence} being exhaused.
     */
    abstract TextWithNumbersCharSequenceComparatorMode compare(final char c1,
                                                               final char c2,
                                                               final TextWithNumbersCharSequenceComparatorState state);

    /**
     * Assumes both characters are digits and compares them of values.
     */
    final TextWithNumbersCharSequenceComparatorMode compareNonDigits(final char c1,
                                                                     final char c2,
                                                                     final TextWithNumbersCharSequenceComparatorState state) {
        state.result = state.comparator.compare(c1, c2);

        TextWithNumbersCharSequenceComparatorMode next;
        if (Comparators.EQUAL == state.result) {
            state.next();
            next = this;
        } else {
            next = STOP;
        }
        return next;
    }

    /**
     * After testing if both are digits then compares their values.
     */
    final TextWithNumbersCharSequenceComparatorMode compareMaybeDigits(final char c1,
                                                                       final char c2,
                                                                       final TextWithNumbersCharSequenceComparatorState state) {
        // they may both be digits.
        final int value1 = digit(c1);
        final int value2 = digit(c2);
        final boolean digit1 = isDigit(value1);
        final boolean digit2 = isDigit(value2);

        TextWithNumbersCharSequenceComparatorMode next;

        // compare values if both are digits
        if (digit1 && digit2) {
            state.result = value1 - value2;
            if (Comparators.EQUAL == state.result) {
                next = this;
                state.next();
            } else {
                // give up state.result has the "result"
                next = STOP;
            }
        } else {
            if (digit1) {
                state.result = Comparators.MORE;
                next = STOP;
            } else {
                if (digit2) {
                    state.result = Comparators.LESS;
                    next = STOP;
                } else {
                    final boolean decimal1 = state.isDecimal(c1);
                    final boolean decimal2 = state.isDecimal(c2);
                    if(decimal1 || decimal2) {
                        if(decimal1) {
                            state.pos1++;
                        }
                        if(decimal2) {
                            state.pos2++;
                        }
                        next = FRACTIONAL_DIGITS;
                        if(decimal1 ^ decimal2) {
                            state.result = this.compareNonDigits(c1, c2, state);
                        }
                    } else {
                        next = this.compareNonDigits(c1, c2, state);
                    }
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