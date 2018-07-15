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
import walkingkooka.text.cursor.parser.DecimalParserToken;
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.HasSign;
import walkingkooka.text.cursor.parser.NumberParserToken;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class SignedNumberParserTokenBiFunctionTest extends ParserBiFunctionTestCase2<SignedNumberParserTokenBiFunction<NumberParserToken, FakeParserContext>, NumberParserToken> {

    private final static BigInteger VALUE = BigInteger.valueOf(31);
    private final static String SIGN_TEXT = "---";

    @Test(expected = ParserException.class)
    public void testInvalidFirstToken() {
        this.applyAndCheck(this.invalidFirstToken(), this.secondToken(), null);
    }

    @Test(expected = ParserException.class)
    public void testInvalidSecondToken() {
        this.applyAndCheck(this.firstToken(), this.invalidSecondToken(), null);
    }

    @Test
    public void testApply() {
        this.applyAndCheck(this.firstToken(), this.secondToken(),
                ParserTokens.number(VALUE.negate(), SIGN_TEXT + VALUE));
    }

    @Test
    public void testApplyDecimal() {
        this.applyAndCheck(
                this.createBiFunction0(DecimalParserToken.class),
                this.sequence(
                    this.firstToken(),
                    ParserTokens.decimal(BigDecimal.valueOf(123), "123.ABC")
                ),
                this.createContext(),
                ParserTokens.decimal(BigDecimal.valueOf(-123), SIGN_TEXT + "123.ABC"));
    }

    private void applyAndCheck(final ParserToken first, final ParserToken second, final NumberParserToken result) {
        this.applyAndCheck(this.sequence(first, second), result);
    }

    @Override
    protected SignedNumberParserTokenBiFunction<NumberParserToken, FakeParserContext> createBiFunction() {
        return this.createBiFunction0(NumberParserToken.class);
    }

    private <TT extends ParserToken & HasSign> SignedNumberParserTokenBiFunction<TT, FakeParserContext> createBiFunction0(final Class<TT> tokenClass) {
        return SignedNumberParserTokenBiFunction.with(tokenClass);
    }

    @Override
    protected Class<SignedNumberParserTokenBiFunction<NumberParserToken, FakeParserContext>> type() {
        return Cast.to(SignedNumberParserTokenBiFunction.class);
    }

    private ParserToken invalidFirstToken() {
        return ParserTokens.singleQuoted("First", "'FIRST'");
    }

    private ParserToken invalidSecondToken() {
        return ParserTokens.singleQuoted("Second", "'Second'");
    }

    private ParserToken firstToken() {
        return ParserTokens.sign(true, SIGN_TEXT);
    }

    private ParserToken secondToken() {
        return ParserTokens.number(VALUE, "" + VALUE);
    }
}
