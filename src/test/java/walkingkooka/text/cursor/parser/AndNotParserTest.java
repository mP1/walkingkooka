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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AndNotParserTest extends ParserTestCase<AndNotParser<ParserContext>> {

    private final static String LEFT = "left";
    private final static String RIGHT = "right";

    @Test
    public void testWithNullLeftFails() {
        assertThrows(NullPointerException.class, () -> this.createParser(null, this.right()));
    }

    @Test
    public void testWithNullRightFails() {
        assertThrows(NullPointerException.class, () -> this.createParser(this.left(), null));
    }

    @Test
    public void testLeftFailed() {
        this.parseFailAndCheck("x");
    }

    @Test
    public void testLeftMissingFailed() {
        this.parseFailAndCheck(AndNotParser.with(this::missing, Parsers.fake()), "A");
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
        this.parseAndCheck(this.createParser(this.left(), this::missing),
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
    public AndNotParser<ParserContext> createParser() {
        return this.createParser(this.left(), this.right());
    }

    protected AndNotParser<ParserContext> createParser(final Parser<ParserContext> left,
                                                       final Parser<ParserContext> right) {
        return AndNotParser.with(left, right);
    }

    private Parser<ParserContext> left() {
        return string(LEFT);
    }

    private Parser<ParserContext> right() {
        return string(RIGHT);
    }

    private Optional<ParserToken> missing(final TextCursor cursor, final ParserContext context) {
        return Optional.empty();
    }

    private Parser<ParserContext> string(final String string) {
        return Parsers.string(string, CaseSensitivity.SENSITIVE);
    }

    @Override
    public Class<AndNotParser<ParserContext>> type() {
        return Cast.to(AndNotParser.class);
    }
}
