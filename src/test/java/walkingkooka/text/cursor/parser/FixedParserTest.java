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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.text.cursor.TextCursors;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class FixedParserTest extends ParserTestCase2<FixedParser<MissingParserToken, ParserContext>, MissingParserToken> {

    private final static MissingParserToken RESULT = ParserTokens.missing(StringParserToken.NAME, "");

    @Test
    @Ignore
    public void testNullCursorFail() {
        // nop
    }

    public void testEmptyCursorFail() {
        // nop
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParserTokenFails() {
        FixedParser.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyTextParserTokenFails() {
        this.createParser(Optional.of(ParserTokens.missing(StringParserToken.NAME, "contains-text-and-will-fail")));
    }

    @Test
    public void testParse() {
        this.parseAndCheck("", RESULT, "", "");
    }

    @Test
    public void testParse2() {
        this.parseAndCheck("abc", RESULT, "", "abc");
    }

    @Test
    public void testParseEmptyOptionalResult() {
        final Optional<MissingParserToken> result = Optional.empty();
         this.parseAndCheck(this.createParser(result),
                 this.createContext(),
                 TextCursors.charSequence(""),
                 result,
                 "",
                 "");
    }

    @Test
    public void testToString() {
        assertEquals(Optional.of(RESULT).toString(), this.createParser().toString());
    }

    @Override
    protected FixedParser<MissingParserToken, ParserContext> createParser() {
        return this.createParser(Optional.of(RESULT));
    }

    private FixedParser<MissingParserToken, ParserContext> createParser(final Optional<MissingParserToken> result) {
        return FixedParser.with(result);
    }

    @Override
    protected Class<FixedParser<MissingParserToken, ParserContext>> type() {
        return Cast.to(FixedParser.class);
    }
}
