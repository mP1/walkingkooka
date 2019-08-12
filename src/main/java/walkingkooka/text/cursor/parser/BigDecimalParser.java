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

import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Optional;

/**
 * A {@link Parser} that parser that parsers {@link BigDecimal} numbers, including the sign, decimals and any exponent.
 * Note unlike {@link DoubleParser} which returns doubles, NAN and +/- INFINITY is not supported.
 */
final class BigDecimalParser<C extends ParserContext> extends Parser2<C> {

    /**
     * Factory that creates a {@link BigDecimalParser}
     */
    static <C extends ParserContext> BigDecimalParser<C> with() {
        return new BigDecimalParser<>();
    }

    /**
     * Private ctor to limit subclassing.
     */
    private BigDecimalParser() {
        super();
    }

    private final static int RADIX = 10;
    private final static BigDecimal RADIX_BIGDECIMAL = BigDecimal.valueOf(RADIX);

    private final static int NUMBER_SIGN = 1;
    private final static int NUMBER_ZERO = NUMBER_SIGN * 2;
    private final static int NUMBER_DIGIT = NUMBER_ZERO * 2;
    private final static int DECIMAL = NUMBER_DIGIT * 2;
    private final static int DECIMAL_DIGIT = DECIMAL * 2;
    private final static int EXPONENT = DECIMAL_DIGIT * 2;
    private final static int EXPONENT_SIGN = EXPONENT * 2;
    private final static int EXPONENT_ZERO = EXPONENT_SIGN * 2;
    private final static int EXPONENT_DIGIT = EXPONENT_ZERO * 2;

    private final static int FINISH = EXPONENT_DIGIT * 2;

    /**
     * Reads character by character until a non digit is found, using a {@link BigInteger} to hold the value.
     * Basically a cut down version of {@link DoubleParser#tryParse0(TextCursor, ParserContext, TextCursorSavePoint)},
     * but with the NAN and INFINITY modes cut out and a {@link BigDecimal} instead of double.
     * Concepts such as negative zero which are not supported by bigdecimal natively end up being plain zero.
     */
    @Override
    Optional<ParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint save) {
        final char decimalSeparator = context.decimalSeparator();
        final int negativeSign = context.negativeSign();
        final int positiveSign = context.positiveSign();
        final char littleE = Character.toLowerCase(context.exponentSymbol());
        final char bigE = Character.toUpperCase(littleE);

        final MathContext mathContext = context.mathContext();

        BigDecimalParserToken token = null;

        // optional(+/-)
        // 0 OR 1-9
        //      repeat(0-9)
        // if DECIMAL
        //    repeat(0-9)
        // if E/e
        //    optional(+/-)
        //    repeat(0-9)

        BigDecimal number = BigDecimal.ZERO;
        boolean numberNegative = false;
        int fractionFactor = 0;
        int exponent = 0;
        boolean exponentNegative = false;
        int mode = NUMBER_SIGN | NUMBER_ZERO | NUMBER_DIGIT;
        boolean empty = true;

        for (; ; ) {
            final char c = cursor.at();

            for (; ; ) {
                if ((NUMBER_SIGN & mode) != 0) {
                    if (positiveSign == c) {
                        cursor.next();
                        mode = NUMBER_ZERO | NUMBER_DIGIT;
                        break;
                    }
                    if (negativeSign == c) {
                        cursor.next();
                        numberNegative = true;
                        mode = NUMBER_ZERO | NUMBER_DIGIT;
                        break;
                    }
                }
                if ((NUMBER_ZERO & mode) != 0) {
                    if ('0' == c) {
                        cursor.next();
                        mode = NUMBER_ZERO | NUMBER_DIGIT | DECIMAL | EXPONENT;
                        empty = false;
                        break;
                    }
                }
                if ((NUMBER_DIGIT & mode) != 0) {
                    final int digit = digit(c);
                    if (digit >= 0) {
                        cursor.next();
                        number = number(number, digit, mathContext);
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
                        number = number(number, digit, mathContext);
                        fractionFactor--;
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
                // invalid char
                mode = FINISH;
                break;
            }

            if (FINISH == mode || cursor.isEmpty()) {
                if (!empty) {
                    if (exponentNegative) {
                        exponent = -exponent;
                    }
                    if (numberNegative) {
                        number = number.negate(mathContext);
                    }
                    exponent = exponent + fractionFactor;
                    if (0 != exponent) {
                        number = number.scaleByPowerOfTen(exponent);
                    }
                    token = token(number, save);
                }
                break;
            }
        }

        return Optional.ofNullable(token);
    }

    private static int digit(final char c) {
        return Character.digit(c, RADIX);
    }

    private static BigDecimal number(final BigDecimal value,
                                     final int digit,
                                     final MathContext context) {
        return value.multiply(RADIX_BIGDECIMAL, context)
                .add(BigDecimal.valueOf(digit));
    }

    private static int exponent(final int value, final int digit) {
        return value * RADIX + digit;
    }

    private static BigDecimalParserToken token(final BigDecimal value, final TextCursorSavePoint save) {
        return BigDecimalParserToken.with(value,
                save.textBetween().toString());
    }

    @Override
    public String toString() {
        return "Decimal";
    }
}
