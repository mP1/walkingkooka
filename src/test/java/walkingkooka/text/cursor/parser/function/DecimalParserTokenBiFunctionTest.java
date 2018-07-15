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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.DecimalParserToken;
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.NumberParserToken;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;
import walkingkooka.text.cursor.parser.SequenceParserToken;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DecimalParserTokenBiFunctionTest extends ParserBiFunctionTestCase2<DecimalParserTokenBiFunction<FakeParserContext>, DecimalParserToken> {

    private final static Integer WITHOUT_WHOLE = null;

    private final static Character WITHOUT_DECIMAL = null;
    private final static Character DECIMAL = '.';

    private final static Integer WITHOUT_FRACTION = null;

    private final static Character WITHOUT_E = null;
    private final static Character E = 'E';

    private final static Integer WITHOUT_EXPONENT = null;

    @Test(expected = ParserException.class)
    public void testWrongTokenCountFail() {
        this.createBiFunction().apply(this.sequence(
                ParserTokens.string("1", "1"),
                ParserTokens.string("2", "2")),
                this.createContext());
    }

    @Test(expected = ParserException.class)
    public void testMissingRequiredWholeNumberFails() {
        this.applyAndCheck2(
                WITHOUT_WHOLE,
                WITHOUT_DECIMAL,
                WITHOUT_FRACTION,
                E,
                0, //==-1**2^10
                "1"
        );
    }

    @Test
    public void testOnlyZeroWholeNumber() {
        this.applyAndCheck2(
                0,
                WITHOUT_DECIMAL,
                WITHOUT_FRACTION,
                WITHOUT_E,
                WITHOUT_EXPONENT,
                0
        );
    }

    @Test
    public void testOnlyWholeNumber() {
        this.applyAndCheck2(
                1,
                WITHOUT_DECIMAL,
                WITHOUT_FRACTION,
                WITHOUT_E,
                WITHOUT_EXPONENT,
                1
        );
    }

    @Test
    public void testOnlyWholeNumber2() {
        this.applyAndCheck2(
                12345,
                WITHOUT_DECIMAL,
                WITHOUT_FRACTION,
                WITHOUT_E,
                WITHOUT_EXPONENT,
                12345
        );
    }

    @Test
    public void testOnlyNegativeWholeNumber() {
        this.applyAndCheck2(
                -123,
                WITHOUT_DECIMAL,
                WITHOUT_FRACTION,
                WITHOUT_E,
                WITHOUT_EXPONENT,
                -123
        );
    }

    @Test
    public void testWholeNumberWithFraction() {
        this.applyAndCheck2(
                1,
                DECIMAL,
                5,
                WITHOUT_E,
                WITHOUT_EXPONENT, //== 1.5
                1.5
        );
    }

    @Test
    public void testPositiveWholeNumberWithFraction2() {
        this.applyAndCheck2(
                123,
                DECIMAL,
                75,
                WITHOUT_E,
                WITHOUT_EXPONENT, //==123.75
                123.75
        );
    }

    @Test
    public void testNegativeWholeNumberWithFraction3() {
        this.applyAndCheck2(
                -1,
                DECIMAL,
                5,
                WITHOUT_E,
                WITHOUT_EXPONENT, //==-1.5
                -1.5
        );
    }

    @Test
    public void testExponent() {
        this.applyAndCheck2(
                1,
                WITHOUT_DECIMAL,
                WITHOUT_FRACTION,
                E,
                0, //==-1**2^10
                "1E0"
        );
    }

    @Test
    public void testExponent2() {
        this.applyAndCheck2(
                123,
                WITHOUT_DECIMAL,
                WITHOUT_FRACTION,
                E,
                5, //==-1**2^10
                "123E5"
        );
    }

    @Test
    public void testExponentWithFraction() {
        this.applyAndCheck2(
                1,
                DECIMAL,
                23,
                E,
                5, //==-1**2^10
                "1.23E5"
        );
    }

    private void applyAndCheck2(final Integer whole,
                                final Character decimal,
                                final Integer fraction,
                                final Character e,
                                final Integer exponent,
                                final double result) {
        this.applyAndCheck2(whole,
                decimal,
                fraction,
                e,
                exponent,
                new BigDecimal(result));
    }

    private void applyAndCheck2(final Integer whole,
                                final Character decimal,
                                final Integer fraction,
                                final Character e,
                                final Integer exponent,
                                final String result) {
        this.applyAndCheck2(whole,
                decimal,
                fraction,
                e,
                exponent,
                new BigDecimal(result));
    }

    private void applyAndCheck2(final Integer whole,
                                final Character decimal,
                                final Integer fraction,
                                final Character e,
                                final Integer exponent,
                                final BigDecimal result) {
        this.applyAndCheck3(number(whole),
                character(decimal),
                number(fraction),
                character(e),
                number(exponent),
                result);
    }

    private ParserToken number(final Integer value) {
        return null != value ?
                ParserTokens.number(BigInteger.valueOf(value), value.toString()) :
                ParserTokens.missing(NumberParserToken.NAME, "");
    }

    private ParserToken character(final Character value) {
        return null != value ?
                ParserTokens.character(value, value.toString()) :
                ParserTokens.missing(CharacterParserToken.NAME, "");
    }

    private void applyAndCheck3(final ParserToken whole,
                                final ParserToken decimal,
                                final ParserToken fraction,
                                final ParserToken e,
                                final ParserToken exponent,
                                final BigDecimal result) {
        final SequenceParserToken sequence = this.sequence(whole, decimal, fraction, e, exponent);
        this.applyAndCheck(sequence, ParserTokens.decimal(result, sequence.text()));
    }

    @Override
    protected DecimalParserTokenBiFunction<FakeParserContext> createBiFunction() {
        return DecimalParserTokenBiFunction.get();
    }

    @Override protected Class<DecimalParserTokenBiFunction<FakeParserContext>> type() {
        return Cast.to(DecimalParserTokenBiFunction.class);
    }
}
