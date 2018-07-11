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
package walkingkooka.text.cursor.parser;

import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.math.BigInteger;
import java.util.Optional;

/**
 * A {@link Parser} that matches a number using a given radix. Note it does not require or match a leading prefix.
 * Note this only parses numeric digits and not any leading minus sign.
 */
final class NumberParser<C extends ParserContext> extends ParserTemplate2<NumberParserToken, C> {

    /**
     * Factory that creates a {@link NumberParser}
     */
    static <C extends ParserContext> NumberParser<C> with(final int radix) {
        if(radix <= 0) {
            throw new IllegalArgumentException("Radix " + radix + " must be > 0");
        }

        return new NumberParser<>(radix);
    }

    /**
     * Private ctor to limit subclassing.
     */
    private NumberParser(final int radix) {
        this.radix = radix;
        this.radixBigInteger = BigInteger.valueOf(radix);
    }

    /**
     * Reads character by character until a non digit is found, using a {@link BigInteger} to hold the value.
     */
    @Override
    Optional<NumberParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint save) {
        Optional<NumberParserToken> token;

        BigInteger number = BigInteger.ZERO;
        boolean empty = true;

        char c = cursor.at();
        for(;;){
            final int digit = Character.digit(c, this.radix);
            if(-1 == digit){
                token = empty ?
                        Optional.empty() :
                        this.createToken(number, save);
                break;
            }
            empty = false;

            number = number.multiply(this.radixBigInteger)
                    .add(BigInteger.valueOf(digit));

            cursor.next();
            if(cursor.isEmpty()){
                token = this.createToken(number, save);
                break;
            }
            c = cursor.at();
        }

        return token;
    }

    private Optional<NumberParserToken> createToken(final BigInteger value, final TextCursorSavePoint save){
        return NumberParserToken.with(value,
                save.textBetween().toString())
                .success();
    }

    private final int radix;
    private final BigInteger radixBigInteger;

    @Override
    public String toString() {
        final int radix = this.radix;
        return 10 == radix ? "number" : "number(base=" + radix+ ")";
    }
}
