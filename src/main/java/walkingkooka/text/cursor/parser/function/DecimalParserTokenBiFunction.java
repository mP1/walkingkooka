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
package walkingkooka.text.cursor.parser.function;

import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.DecimalParserToken;
import walkingkooka.text.cursor.parser.NumberParserToken;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;
import walkingkooka.text.cursor.parser.SequenceParserToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Assumes five tokens (many may be missing) wrapped in a {@link SequenceParserToken}.
 * <ol>
 * <li>Whole number part (required)</li>
 * <li>decimal point (optional)</li>
 * <li>Fraction part (optional)</li>
 * <li>Exponent (optional)</li>
 * <li>Exponent number</li>
 * </ol>
 */
final class DecimalParserTokenBiFunction<C extends ParserContext> extends ParserBiFunctionTemplate<C, DecimalParserToken> {

    /**
     * Type safe singleton getter.
     */
    static <C extends ParserContext> DecimalParserTokenBiFunction<C> get() {
        return Cast.to(INSTANCE);
    }

    private final static DecimalParserTokenBiFunction INSTANCE = new DecimalParserTokenBiFunction();

    private DecimalParserTokenBiFunction(){
        super();
    }

    private final static int WHOLE_NUMBER = 0;
    private final static int DECIMAL = WHOLE_NUMBER + 1;
    private final static int FRACTION = DECIMAL + 1;
    private final static int E = FRACTION + 1;
    private final static int EXPONENT = E + 1;
    private final static int COUNT = EXPONENT + 1;

    @Override
    DecimalParserToken apply0(final SequenceParserToken token, final C c) {
        token.checkTokenCount(COUNT);

        BigDecimal value = new BigDecimal(number(token.required(WHOLE_NUMBER, NumberParserToken.class)));

        // decimal-point then fraction ???
        final Optional<CharacterParserToken> decimal = token.optional(DECIMAL, CharacterParserToken.class);
        if(decimal.isPresent()){
            final BigDecimal fraction = this.fraction(token.required(FRACTION, NumberParserToken.class));
            value = value.signum() < 0 ?
                    value.subtract(fraction) :
                    value.add(fraction);
        }

        // e for exponent ?
        final Optional<CharacterParserToken> e = token.optional(E, CharacterParserToken.class);
        if(e.isPresent()) {
            BigInteger exponent = number(token.required(EXPONENT, NumberParserToken.class));
            value = value.scaleByPowerOfTen(exponent.intValue());
        }

        return ParserTokens.decimal(value, token.text());
    }

    private BigInteger number(final ParserToken token) {
        return token.isMissing() ?
                BigInteger.ZERO :
                NumberParserToken.class.cast(token).value();
    }

    private BigDecimal fraction(final ParserToken token) {
        // 1 decpt = x/10
        // 2 decpt = x/100
        return new BigDecimal(number(token))
                .divide(BigDecimal.TEN.pow(token.text().length()));
    }

    @Override
    public String toString() {
        return DecimalParserTokenBiFunction.class.getName();
    }
}
