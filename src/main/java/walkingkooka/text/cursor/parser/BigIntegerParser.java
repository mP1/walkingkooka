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

import java.math.BigInteger;
import java.util.Optional;

/**
 * A {@link Parser} that matches a number using a given radix. Note it does not require or match a leading prefix.
 * Note this only parses numeric digits and not any leading minus sign.
 */
final class BigIntegerParser<C extends ParserContext> extends Parser2<C> {

    /**
     * Factory that creates a {@link BigIntegerParser}
     */
    static <C extends ParserContext> BigIntegerParser<C> with(final int radix) {
        if (radix <= 0) {
            throw new IllegalArgumentException("Radix " + radix + " must be > 0");
        }

        return new BigIntegerParser<>(radix);
    }

    /**
     * Private ctor to limit subclassing.
     */
    private BigIntegerParser(final int radix) {
        this.radix = radix;
        this.radixBigInteger = BigInteger.valueOf(radix);
    }

    /**
     * Reads character by character until a non digit is found, using a {@link BigInteger} to hold the value.
     */
    @Override
    Optional<ParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint save) {
        final char minusSign = context.minusSign();
        final char plusSign = context.plusSign();

        BigIntegerParserToken token;

        final int radix = this.radix;
        BigInteger number = BigInteger.ZERO;
        boolean empty = true;
        boolean signed = false;

        for (; ; ) {
            if (cursor.isEmpty()) {
                token = empty ?
                        null :
                        this.createToken(number, save);
                break;
            }

            char c = cursor.at();
            if (empty && 10 == this.radix) {
                if (minusSign == c) {
                    signed = true;
                    cursor.next();
                    continue;
                }
                if (plusSign == c) {
                    signed = false;
                    cursor.next();
                    continue;
                }
            }

            final int digit = Character.digit(c, radix);
            if (-1 == digit) {
                token = empty ?
                        null :
                        this.createToken(number, save);
                break;
            }
            empty = false;

            number = number.multiply(this.radixBigInteger);

            final BigInteger digitBigInteger = BigInteger.valueOf(digit);
            number = signed ?
                    number.subtract(digitBigInteger) :
                    number.add(digitBigInteger);

            cursor.next();
            if (cursor.isEmpty()) {
                token = this.createToken(number, save);
                break;
            }
        }

        return Optional.ofNullable(token);
    }

    private BigIntegerParserToken createToken(final BigInteger value, final TextCursorSavePoint save) {
        return BigIntegerParserToken.with(value,
                save.textBetween().toString());
    }

    private final int radix;
    private final BigInteger radixBigInteger;

    @Override
    public String toString() {
        final int radix = this.radix;
        return 10 == radix ? "BigInteger" : "BigInteger(base=" + radix + ")";
    }
}
