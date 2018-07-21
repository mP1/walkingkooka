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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserTestCase3;
import walkingkooka.text.cursor.parser.ParserToken;

public abstract class EbnfParserTestCase<T extends EbnfParserToken> extends ParserTestCase3<Parser<ParserToken, EbnfParserContext>, ParserToken, EbnfParserContext> {

    final static String COMMENT1 = "(*comment-1*)";
    final static String COMMENT2 = "(*comment-2*)";

    final static String IDENTIFIER1 = "abc123";
    final static String IDENTIFIER2 = "def456";

    final static String WHITESPACE1 = " ";
    final static String WHITESPACE2 = "   ";

    final static String TERMINAL1 = "terminal456";
    final static String TERMINAL1_TEXT = '"' + TERMINAL1 + '"';

    final static String TERMINAL2 = "terminal789";
    final static String TERMINAL2_TEXT = '"' + TERMINAL2 + '"';

    final static String ASSIGNMENT = "=";
    final static String TERMINATOR = ";";

    final static String BETWEEN = "..";

    @Test
    public void testOrphanedAssignmentFail() {
        this.parseFailAndCheck("=");
    }

    @Test
    public void testInvalidSymbolToken() {
        this.parseFailAndCheck("$");
    }

    @Override
    protected final EbnfParserContext createContext() {
        return new EbnfParserContext();
    }

    final EbnfParserToken comment1() {
        return EbnfParserToken.comment(COMMENT1, COMMENT1);
    }

    final EbnfParserToken comment2() {
        return EbnfParserToken.comment(COMMENT2, COMMENT2);
    }

    final EbnfParserToken identifier1() {
        return EbnfParserToken.identifier(IDENTIFIER1, IDENTIFIER1);
    }

    final EbnfParserToken identifier2() {
        return EbnfParserToken.identifier(IDENTIFIER2, IDENTIFIER2);
    }

    final EbnfParserToken whitespace1() {
        return EbnfParserToken.whitespace(WHITESPACE1, WHITESPACE1);
    }

    final EbnfParserToken whitespace2() {
        return EbnfParserToken.whitespace(WHITESPACE2, WHITESPACE2);
    }

    static EbnfSymbolParserToken symbol(final String symbol) {
        return EbnfParserToken.symbol(symbol, symbol);
    }

    static EbnfRuleParserToken rule(final String text, final EbnfParserToken...tokens) {
        return EbnfParserToken.rule(Lists.of(tokens), text);
    }

    static EbnfRangeParserToken range(final String text, final EbnfParserToken...tokens) {
        return EbnfParserToken.range(Lists.of(tokens), text);
    }

    static EbnfSymbolParserToken assignmentToken() {
        return symbol("=");
    }

    static EbnfTerminalParserToken terminal1() {
        return EbnfParserToken.terminal(TERMINAL1, TERMINAL1_TEXT);
    }

    static EbnfTerminalParserToken terminal2() {
        return EbnfParserToken.terminal(TERMINAL2, TERMINAL2_TEXT);
    }

    static EbnfSymbolParserToken terminatorToken() {
        return symbol(";");
    }

    static EbnfSymbolParserToken between() {
        return symbol("..");
    }
}
