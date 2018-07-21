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

public class EbnfRuleParserTokenTest extends EbnfParentParserTokenTestCase<EbnfRuleParserToken> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullTokenFails() {
        this.createToken(this.text(), Cast.<List<EbnfParserToken>>to(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIdentifierFails() {
        this.createToken(this.text(), assignment(), terminal(), terminator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIdentifierFails2() {
        this.createToken(this.text(), terminal(), assignment(), identifier(), terminator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingTokenFails() {
        this.createToken(this.text(), identifier(), assignment(), terminator());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespace() {
        final EbnfRuleParserToken token = this.createToken();
        assertSame(token, token.withoutCommentsSymbolsOrWhitespace().get());
    }

    @Override
    protected EbnfRuleParserToken createDifferentToken() {
        return this.createToken("xyz=qrs;",
                identifier("xyz"), assignment(), identifier("qrs"), terminator());
    }

    @Override
    final String text() {
        return "abc123=def456";
    }

    final List<EbnfParserToken> tokens() {
        return Lists.of(this.identifier(), this.assignment(), this.terminal(), this.terminator());
    }

    @Override
    EbnfRuleParserToken createToken(final String text, final List<EbnfParserToken> tokens) {
        return EbnfRuleParserToken.with(tokens, text);
    }

    private EbnfIdentifierParserToken identifier() {
        return identifier("abc");
    }

    private EbnfTerminalParserToken terminal() {
        return terminal("xyz");
    }

    private EbnfTerminalParserToken terminal(final String text) {
        return EbnfParserToken.terminal(text, '"' + text + '"');
    }

    private EbnfSymbolParserToken assignment() {
        return symbol("=");
    }

    private EbnfSymbolParserToken terminator() {
        return symbol(";");
    }

    @Override
    protected Class<EbnfRuleParserToken> type() {
        return EbnfRuleParserToken.class;
    }
}
