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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;

public final class EbnfRangeParserTest extends EbnfParserTestCase2<EbnfRangeParserToken> {

    @Test
    public void testBeginOnlyFails() {
        this.parseFailAndCheck(TERMINAL1_TEXT);
    }

    @Test
    public void testBeginBetweenFails() {
        this.parseThrowsEndOfText(TERMINAL1_TEXT + BETWEEN);
    }

    @Test
    public void testBetweenOnlyFails() {
        this.parseThrows(BETWEEN, '.', 1, 1);
    }

    @Test
    public void testDoubleBetweenFails() {
        this.parseThrows(TERMINAL1_TEXT + BETWEEN + BETWEEN, BETWEEN.charAt(0), 16, 1);
    }

    @Test
    public void testDoubleBetweenFails2() {
        this.parseThrows(TERMINAL1_TEXT + BETWEEN + BETWEEN + TERMINAL2_TEXT, BETWEEN.charAt(0), 16, 1);
    }

    @Test
    public void testBeginWhitespaceBetweenEnd() {
        final String text = TERMINAL1_TEXT + WHITESPACE1 + BETWEEN + TERMINAL2_TEXT;
        this.parseAndCheck(text,
                range(text, terminal1(), whitespace1(), between(), terminal2()),
                text);
    }

    @Test
    public void testBeginCommentBetweenEnd() {
        final String text = TERMINAL1_TEXT + COMMENT1 + BETWEEN + TERMINAL2_TEXT;
        this.parseAndCheck(text,
                range(text, terminal1(), comment1(), between(), terminal2()),
                text);
    }

    @Test
    public void testBeginBetweenWhitespaceEnd() {
        final String text = TERMINAL1_TEXT + BETWEEN + WHITESPACE1 + TERMINAL2_TEXT;
        this.parseAndCheck(text,
                range(text, terminal1(), between(), whitespace1(), terminal2()),
                text);
    }

    @Test
    public void testBeginBetweenCommentEnd() {
        final String text = TERMINAL1_TEXT + BETWEEN + COMMENT1 + TERMINAL2_TEXT;
        this.parseAndCheck(text,
                range(text, terminal1(), between(), comment1(), terminal2()),
                text);
    }

    // trailing....

    @Test
    public void testEndWhitespace() {
        final String text = TERMINAL1_TEXT + BETWEEN + TERMINAL2_TEXT;
        this.parseAndCheck(text + WHITESPACE1,
                range(text, terminal1(), between(), terminal2()),
                text,
                WHITESPACE1);
    }

    @Test
    public void testEndComments() {
        final String text = TERMINAL1_TEXT + BETWEEN + TERMINAL2_TEXT;
        this.parseAndCheck(text + COMMENT1,
                range(text, terminal1(), between(), terminal2()),
                text,
                COMMENT1);
    }

    @Override public Parser<EbnfParserContext> createParser() {
        return EbnfGrammarParser.RANGE;
    }

    @Override
    String text() {
        return TERMINAL1_TEXT + BETWEEN + TERMINAL2_TEXT;
    }

    @Override
    EbnfRangeParserToken token(final String text) {
        return token(text,
                terminal1(),
                between(),
                terminal2());
    }

    private EbnfRangeParserToken token(final String text, final ParserToken... tokens) {
        return EbnfRangeParserToken.with(Lists.of(tokens), text);
    }
}
