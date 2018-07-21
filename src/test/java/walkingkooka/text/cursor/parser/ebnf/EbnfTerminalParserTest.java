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
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;

public final class EbnfTerminalParserTest extends EbnfParserTestCase3<EbnfTerminalParserToken>{

    @Test
    public void testIncompleteFails() {
        this.parseFailAndCheck("'incomplete");
    }

    @Test
    public void testSingleQuoteLiteral() {
        final String text = "hello";
        final String quoted = singleQuote(text);

        this.parseAndCheck(quoted, new EbnfTerminalParserToken(text, quoted), quoted);
    }

    @Test
    public void testDoubleQuoteLiteral() {
        final String text = "hello";
        final String quoted = doubleQuote(text);

        this.parseAndCheck(quoted, new EbnfTerminalParserToken(text, quoted), quoted);
    }

    private static String singleQuote(final String text) {
        return '\'' + text + '\'';
    }

    private static String doubleQuote(final String text) {
        return '"' + text + '"';
    }

    @Override
    protected Parser<ParserToken, EbnfParserContext> createParser() {
        return EbnfGrammarParser.TERMINAL;
    }

    @Override
    String text() {
        return singleQuote("hello");
    }

    @Override
    EbnfTerminalParserToken token(final String text) {
        final String quotedText = this.text();
        return new EbnfTerminalParserToken(
                quotedText.substring(1, quotedText.length() -1),
                text);
    }
}
