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
 *
 */

package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.text.CaseSensitivity;

public final class AndNotParserTest extends ParserTestCase2<AndNotParser<StringParserToken, FakeParserContext>, StringParserToken> {

    private final static String LEFT = "left";
    private final static String RIGHT = "right";

    @Test(expected = NullPointerException.class)
    public void testWithNullLeftFails() {
        this.createParser(null, this.right());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullRightFails() {
        this.createParser(this.left(), null);
    }

    @Test
    public void testLeftFailed() {
        this.parseFailAndCheck("x");
    }

    @Test
    public void testLeftMissingFailed() {
        this.parseAndCheck(AndNotParser.with(this.missing(), Parsers.fake()),
            LEFT,
            this.missingParserToken(),
            "",
            LEFT);
    }

    @Test
    public void testLeftFailedRightPass() {
        this.parseFailAndCheck("right");
    }

    @Test
    public void testLeftMatchRightFail() {
        this.parseAndCheck(LEFT,
                this.leftToken(),
                LEFT);
    }

    private StringParserToken leftToken() {
        return ParserTokens.string(LEFT, LEFT);
    }

    @Test
    public void testLeftMatchRightMissing() {
        this.parseAndCheck(this.createParser(this.left(), this.missing()),
                LEFT,
                this.leftToken(),
                LEFT);
    }

    @Test
    public void testLeftMatchRightMatch() {
        this.parseFailAndCheck(this.createParser(string(LEFT), string(LEFT)),
                LEFT);
    }

    @Test
    public void testLeftMatchRightFail2() {
        final String after = "123";
        this.parseAndCheck(LEFT + after,
                this.leftToken(),
                LEFT,
                after);
    }

    @Override
    protected AndNotParser<StringParserToken, FakeParserContext> createParser() {
        return this.createParser(this.left(), this.right());
    }

    protected AndNotParser<StringParserToken, FakeParserContext> createParser(final Parser<StringParserToken, FakeParserContext> left,
                                                                              final Parser<StringParserToken, FakeParserContext> right) {
        return AndNotParser.with(left, right);
    }

    private Parser<StringParserToken, FakeParserContext> left() {
        return string(LEFT);
    }

    private Parser<StringParserToken, FakeParserContext> right() {
        return string(RIGHT);
    }

    private <T extends ParserToken> Parser<T, FakeParserContext> missing() {
        return Parsers.fixed(Cast.to(this.missingParserToken().success()));
    }

    private MissingParserToken missingParserToken() {
        return ParserTokens.missing(ParserTokenNodeName.with(0), "");
    }

    private Parser<StringParserToken, FakeParserContext> string(final String string) {
        return CaseSensitivity.SENSITIVE.parser(string);
    }

    @Override
    protected Class<AndNotParser<StringParserToken, FakeParserContext>> type() {
        return Cast.to(AndNotParser.class);
    }
}
