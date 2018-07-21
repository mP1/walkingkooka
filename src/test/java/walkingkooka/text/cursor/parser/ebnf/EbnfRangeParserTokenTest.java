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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.List;

public final class EbnfRangeParserTokenTest extends EbnfParentParserTokenTestCase<EbnfRangeParserToken> {

    private final static String TERMINAL_TEXT1 = "terminal-1";
    private final static String TERMINAL_TEXT2 = "terminal-2";
    private final static String TERMINAL1 = "'terminal-1'";
    private final static String TERMINAL2 = "'terminal-2'";

    private final static String RANGE = "..";

    private final static String WHITESPACE = "   ";
    private final static String COMMENT = "(*comment*)";

    @Test(expected = NullPointerException.class)
    public final void testWithNullTokenFails() {
        this.createToken(this.text(), Cast.<List<EbnfParserToken>>to(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingBeginIdentifierFails() {
        this.createToken(this.text(), identifier(), range(), terminal2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingRangeBetweenFails() {
        this.createToken(this.text(), terminal1(), terminal2(), terminal2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIdentifierFails2() {
        this.createToken(this.text(), terminal1(), range(), range());
    }

    @Test
    public void testTerminalBeginWhitespace() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + WHITESPACE + RANGE + TERMINAL_TEXT2,
                terminal1, whitespace("   "), range(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testTerminalBeginComment() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + COMMENT + RANGE + TERMINAL_TEXT2,
                terminal1, comment(), range(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testRangeBeginWhitespace() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + RANGE + WHITESPACE + TERMINAL_TEXT2,
                terminal1, range(), whitespace(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testRangeBeginComment() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + RANGE + COMMENT +TERMINAL_TEXT2,
                terminal1, range(), comment(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespace() {
        final EbnfRangeParserToken token = this.createToken();
        assertSame(token, token.withoutCommentsSymbolsOrWhitespace().get());
    }

    @Override
    protected EbnfRangeParserToken createDifferentToken() {
        return this.createToken("'different-1'..'different-2'",
                terminal("different-1"), range(), terminal("different-2"));
    }

    @Override
    final String text() {
        return "'terminal-1'..'terminal2'";
    }

    final List<EbnfParserToken> tokens() {
        return Lists.of(terminal1(), range(), terminal2());
    }

    @Override
    EbnfRangeParserToken createToken(final String text, final List<EbnfParserToken> tokens) {
        return EbnfRangeParserToken.with(tokens, text);
    }

    private EbnfTerminalParserToken terminal1() {
        return terminal(TERMINAL_TEXT1);
    }

    private EbnfTerminalParserToken terminal2() {
        return terminal(TERMINAL_TEXT2);
    }

    private EbnfTerminalParserToken terminal(final String text) {
        return EbnfParserToken.terminal(text, '"' + text + '"');
    }

    private EbnfSymbolParserToken range() {
        return symbol("=");
    }

    private EbnfIdentifierParserToken identifier() {
        return EbnfParserToken.identifier("identifier", "identifier");
    }

    private EbnfCommentParserToken comment() {
        return this.comment(COMMENT);
    }

    private EbnfWhitespaceParserToken whitespace() {
        return this.whitespace(WHITESPACE);
    }

    @Override
    protected Class<EbnfRangeParserToken> type() {
        return EbnfRangeParserToken.class;
    }
}
