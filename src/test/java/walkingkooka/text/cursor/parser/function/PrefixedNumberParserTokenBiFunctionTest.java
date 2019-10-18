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
package walkingkooka.text.cursor.parser.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.cursor.parser.BigIntegerParserToken;
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PrefixedNumberParserTokenBiFunctionTest implements ClassTesting2<PrefixedNumberParserTokenBiFunction<FakeParserContext>>,
        ParserBiFunctionTesting<PrefixedNumberParserTokenBiFunction<FakeParserContext>,
                FakeParserContext,
                BigIntegerParserToken> {

    @Test
    public void testInvalidFirstToken() {
        assertThrows(ParserException.class, () -> this.applyAndCheck(this.invalidFirstToken(), this.secondToken(), null));
    }

    @Test
    public void testInvalidSecondToken() {
        assertThrows(ParserException.class, () -> this.applyAndCheck(this.firstToken(), this.invalidSecondToken(), null));
    }

    @Test
    public void testApply() {
        this.applyAndCheck(this.firstToken(), this.secondToken(),
                ParserTokens.bigInteger(BigInteger.valueOf(31), "0x1f"));
    }

    private void applyAndCheck(final ParserToken first, final ParserToken second, final BigIntegerParserToken result) {
        this.applyAndCheck(this.sequence(first, second), result);
    }

    @Override
    public PrefixedNumberParserTokenBiFunction<FakeParserContext> createBiFunction() {
        return PrefixedNumberParserTokenBiFunction.get();
    }

    @Override
    public Class<PrefixedNumberParserTokenBiFunction<FakeParserContext>> type() {
        return Cast.to(PrefixedNumberParserTokenBiFunction.class);
    }

    private ParserToken invalidFirstToken() {
        return ParserTokens.singleQuoted("First", "'FIRST'");
    }

    private ParserToken invalidSecondToken() {
        return ParserTokens.singleQuoted("Second", "'Second'");
    }

    private ParserToken firstToken() {
        return ParserTokens.string("0x", "0x");
    }

    private ParserToken secondToken() {
        return ParserTokens.bigInteger(BigInteger.valueOf(31), "1f");
    }

    @Override
    public FakeParserContext createContext() {
        return new FakeParserContext();
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
