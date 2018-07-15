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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;

import java.math.BigInteger;
import java.util.function.BiFunction;

public class TransformingParserTest extends ParserTestCase2<TransformingParser<StringParserToken, NumberParserToken, FakeParserContext>, NumberParserToken> {

    private final static int RADIX = 10;
    private final static Parser<StringParserToken, FakeParserContext> PARSER = Parsers.stringCharPredicate(CharPredicates.digit());
    private final static BiFunction<StringParserToken, FakeParserContext, NumberParserToken> TRANSFORMER = (t, c) -> {
        return ParserTokens.number(new BigInteger(t.value(), RADIX), t.text());
    };

    @Test(expected = NullPointerException.class)
    public void testWithNullParserFails() {
        TransformingParser.with(null, TRANSFORMER);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTransformerFails() {
        TransformingParser.with(PARSER, null);
    }

    @Test
    public void testFailure() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testFailure2() {
        this.parseFailAndCheck("abc");
    }

    @Test
    public void testSuccessEoc() {
        this.parseAndCheck2("1", 1, "1", "");
    }

    @Test
    public void testSuccessEoc2() {
        this.parseAndCheck2("123", 123, "123", "");
    }

    @Test
    public void testSuccessTokenAfter() {
        this.parseAndCheck2("123abc", 123, "123", "abc");
    }

    @Test
    public void testToString() {
        assertEquals(PARSER.toString(), this.createParser().toString());
    }

    @Override
    protected TransformingParser<StringParserToken, NumberParserToken, FakeParserContext> createParser() {
        return TransformingParser.with(PARSER, TRANSFORMER);
    }

    private TextCursor parseAndCheck2(final String in, final long value, final String text, final String textAfter){
        return this.parseAndCheck3(in, value, text, textAfter);
    }

    private TextCursor parseAndCheck3(final String from, final long value, final String text, final String textAfter){
        return this.parseAndCheck(this.createParser(),
                this.createContext(),
                TextCursors.charSequence(from),
                ParserTokens.number(BigInteger.valueOf(value), text),
                text,
                textAfter);
    }

    @Override
    protected Class<TransformingParser<StringParserToken, NumberParserToken, FakeParserContext>> type() {
        return Cast.to(TransformingParser.class);
    }
}
