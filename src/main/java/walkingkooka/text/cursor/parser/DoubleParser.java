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
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.math.BigInteger;
import java.util.Optional;

/**
 * A {@link Parser} that parser that parsers double numbers, including the sign, decimals and any exponent.
 */
final class DoubleParser<C extends ParserContext> extends Parser2<C> {

    /**
     * Factory that creates a {@link DoubleParser}
     */
    static <C extends ParserContext> DoubleParser<C> instance() {
        return Cast.to(INSTANCE);
    }

    private final static DoubleParser<?> INSTANCE = new DoubleParser();

    /**
     * Private ctor to limit subclassing.
     */
    private DoubleParser() {
        super();
    }

    private final static int RADIX = 10;

    private final static int NAN_N = 1;
    private final static int NAN_NA = NAN_N * 2;
    private final static int NAN_NAN = NAN_NA * 2;
    private final static int NAN_FINISH = NAN_NAN * 2;

    private final static int INFINITY_I = NAN_FINISH * 2;
    private final static int INFINITY_IN = INFINITY_I * 2;
    private final static int INFINITY_INF = INFINITY_IN * 2;
    private final static int INFINITY_INFI = INFINITY_INF * 2;
    private final static int INFINITY_INFIN = INFINITY_INFI * 2;
    private final static int INFINITY_INFINI = INFINITY_INFIN * 2;
    private final static int INFINITY_INFINIT = INFINITY_INFINI * 2;
    private final static int INFINITY_INFINITY = INFINITY_INFINIT * 2;
    private final static int INFINITY_FINISH = INFINITY_INFINITY * 2;

    private final static int NUMBER_SIGN = INFINITY_FINISH * 2;
    private final static int NUMBER_ZERO = NUMBER_SIGN * 2;
    private final static int NUMBER_DIGIT = NUMBER_ZERO * 2;
    private final static int DECIMAL = NUMBER_DIGIT * 2;
    private final static int DECIMAL_DIGIT = DECIMAL * 2;
    private final static int EXPONENT = DECIMAL_DIGIT * 2;
    private final static int EXPONENT_SIGN = EXPONENT * 2;
    private final static int EXPONENT_ZERO = EXPONENT_SIGN * 2;
    private final static int EXPONENT_DIGIT = EXPONENT_ZERO * 2;

    private final static int FINISH = EXPONENT_DIGIT * 2;

    private final static int FAIL = FINISH * 2;

    /**
     * Reads character by character until a non digit is found, using a {@link BigInteger} to hold the value.
     */
    @Override
    Optional<ParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint save) {
        final char decimalSeparator = context.decimalSeparator();
        final char negativeSign = context.negativeSign();
        final char positiveSign = context.positiveSign();
        final char littleE = Character.toLowerCase(context.exponentSymbol());
        final char bigE = Character.toUpperCase(littleE);

        Optional<ParserToken> token = Optional.empty();

        // optional(+/-)
        // 0 OR 1-9
        //      repeat(0-9)
        // if DECIMAL
        //    repeat(0-9)
        // if E/e
        //    optional(+/-)
        //    repeat(0-9)

        double number = 0;
        boolean numberNegative = false;
        double fractionFactor = 1;
        int exponent = 0;
        boolean exponentNegative = false;
        int mode = NAN_N | INFINITY_I | NUMBER_SIGN | NUMBER_ZERO | NUMBER_DIGIT;
        boolean empty = true;

        for (; ; ) {
            final char c = cursor.at();

            for (; ; ) {
                if ((NAN_N & mode) != 0) {
                    if ('N' == c) {
                        cursor.next();
                        mode = NAN_NA;
                        break;
                    }
                }
                if ((NAN_NA & mode) != 0) {
                    if ('a' == c) {
                        cursor.next();
                        mode = NAN_NAN;
                        break;
                    }
                    empty = true;
                    mode = FAIL;
                    break;
                }
                if ((NAN_NAN & mode) != 0) {
                    if ('N' == c) {
                        cursor.next();
                        token = token(Double.NaN, save);
                        mode = NAN_FINISH;
                        break;
                    }
                    empty = true;
                    mode = FAIL;
                    break;
                }
                if ((NUMBER_SIGN & mode) != 0) {
                    if (positiveSign == c) {
                        cursor.next();
                        mode = INFINITY_I | NUMBER_ZERO | NUMBER_DIGIT;
                        break;
                    }
                    if (negativeSign == c) {
                        cursor.next();
                        numberNegative = true;
                        mode = INFINITY_I | NUMBER_ZERO | NUMBER_DIGIT;
                        break;
                    }
                }
                if ((NUMBER_ZERO & mode) != 0) {
                    if ('0' == c) {
                        cursor.next();
                        mode = DECIMAL | EXPONENT;
                        empty = false;
                        break;
                    }
                }
                if ((NUMBER_DIGIT & mode) != 0) {
                    final int digit = digit(c);
                    if (digit >= 0) {
                        cursor.next();
                        number = number(number, digit);
                        mode = NUMBER_DIGIT | DECIMAL | EXPONENT;
                        empty = false;
                        break;
                    }
                }
                if ((DECIMAL & mode) != 0) {
                    if (decimalSeparator == c) {
                        cursor.next();
                        mode = DECIMAL_DIGIT | EXPONENT;
                        break;
                    }
                }
                if ((DECIMAL_DIGIT & mode) != 0) {
                    final int digit = digit(c);
                    if (digit >= 0) {
                        cursor.next();
                        number = number(number, digit);
                        fractionFactor = fractionFactor * RADIX;
                        break;
                    }
                }
                if ((EXPONENT & mode) != 0) {
                    if (littleE == c || bigE == c) {
                        cursor.next();
                        mode = EXPONENT_SIGN | EXPONENT_ZERO | EXPONENT_DIGIT;
                        break;
                    }
                }
                if ((EXPONENT_ZERO & mode) != 0) {
                    if ('0' == c) {
                        cursor.next();
                        mode = FINISH;
                        break;
                    }
                }
                if ((EXPONENT_SIGN & mode) != 0) {
                    if (positiveSign == c) {
                        cursor.next();
                        mode = EXPONENT_DIGIT;
                        break;
                    }
                    if (negativeSign == c) {
                        cursor.next();
                        exponentNegative = true;
                        mode = EXPONENT_DIGIT;
                        break;
                    }
                }
                if ((EXPONENT_DIGIT & mode) != 0) {
                    final int digit = digit(c);
                    if (digit >= 0) {
                        cursor.next();
                        exponent = exponent(exponent, digit);
                        break;
                    }
                }
                if ((INFINITY_I & mode) != 0) {
                    if ('I' == c) {
                        cursor.next();
                        mode = INFINITY_IN;
                        break;
                    }
                }
                if ((INFINITY_IN & mode) != 0) {
                    if ('n' == c) {
                        cursor.next();
                        mode = INFINITY_INF;
                        break;
                    }
                    mode = FAIL;
                    break;
                }
                if ((INFINITY_INF & mode) != 0) {
                    if ('f' == c) {
                        cursor.next();
                        mode = INFINITY_INFI;
                        break;
                    }
                    mode = FAIL;
                    break;
                }
                if ((INFINITY_INFI & mode) != 0) {
                    if ('i' == c) {
                        cursor.next();
                        mode = INFINITY_INFIN;
                        break;
                    }
                    mode = FAIL;
                    break;
                }
                if ((INFINITY_INFIN & mode) != 0) {
                    if ('n' == c) {
                        cursor.next();
                        mode = INFINITY_INFINI;
                        break;
                    }
                    mode = FAIL;
                    break;
                }
                if ((INFINITY_INFINI & mode) != 0) {
                    if ('i' == c) {
                        cursor.next();
                        mode = INFINITY_INFINIT;
                        break;
                    }
                    mode = FAIL;
                    break;
                }
                if ((INFINITY_INFINIT & mode) != 0) {
                    if ('t' == c) {
                        cursor.next();
                        mode = INFINITY_INFINITY;
                        break;
                    }
                    mode = FAIL;
                    break;
                }
                if ((INFINITY_INFINITY & mode) != 0) {
                    if ('y' == c) {
                        cursor.next();
                        token = token(numberNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY, save);
                        mode = INFINITY_FINISH;
                        break;
                    }
                    mode = FAIL;
                    break;
                }
                // invalid char
                mode = FINISH;
                break;
            }

            if (FAIL == mode || NAN_FINISH == mode || INFINITY_FINISH == mode) {
                break;
            }

            if (FINISH == mode || cursor.isEmpty()) {
                if (!empty) {
                    if (exponentNegative) {
                        exponent = -exponent;
                    }
                    if (numberNegative) {
                        number = -number;
                    }
                    number = number / fractionFactor;
                    if (0 != exponent) {
                        number = number * Math.pow(RADIX, exponent);
                    }
                    token = token(number, save);
                }
                break;
            }
        }

        return token;
    }

    private static int digit(final char c) {
        return Character.digit(c, RADIX);
    }

    private static double number(final double value, final int digit) {
        return value * RADIX + digit;
    }

    private static int exponent(final int value, final int digit) {
        return value * RADIX + digit;
    }

    private static Optional<ParserToken> token(final double value, final TextCursorSavePoint save) {
        return Optional.of(DoubleParserToken.with(value, save.textBetween().toString()));
    }

    @Override
    public String toString() {
        return "Double";
    }
}
