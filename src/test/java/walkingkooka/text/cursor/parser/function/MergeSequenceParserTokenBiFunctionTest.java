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
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.NumberParserToken;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;
import walkingkooka.text.cursor.parser.StringParserToken;

import java.math.BigInteger;

public final class MergeSequenceParserTokenBiFunctionTest extends ParserBiFunctionTestCase2<MergeSequenceParserTokenBiFunction<NumberParserToken, FakeParserContext>, NumberParserToken> {

    private final static StringParserToken BEFORE = ParserTokens.string("before", "before");
    private final static NumberParserToken MIDDLE = ParserTokens.number(BigInteger.valueOf(123), "123");
    private final static StringParserToken AFTER = ParserTokens.string("after", "after");

    @Test(expected = ParserException.class)
    public void testWrongTokenCount() {
        this.apply(BEFORE, MIDDLE);
    }

    @Test(expected = ParserException.class)
    public void testWrongTokenCount2() {
        this.apply(BEFORE, MIDDLE, AFTER, ParserTokens.string("extra", "extra"));
    }

    @Test
    public void testApply() {
        this.applyAndCheck(this.sequence(BEFORE, MIDDLE, AFTER),
                MIDDLE.setText("before123after"));
    }

    private void applyAndCheck(final ParserToken first, final ParserToken second, final NumberParserToken result) {
        this.applyAndCheck(this.sequence(first, second), result);
    }

    @Override
    protected MergeSequenceParserTokenBiFunction<NumberParserToken, FakeParserContext> createBiFunction() {
        return MergeSequenceParserTokenBiFunction.with(NumberParserToken.class);
    }

    @Override
    protected Class<MergeSequenceParserTokenBiFunction<NumberParserToken, FakeParserContext>> type() {
        return Cast.to(MergeSequenceParserTokenBiFunction.class);
    }
}
