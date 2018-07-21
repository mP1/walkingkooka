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

public abstract class EbnfParentParserTokenTestCase<T extends EbnfParentParserToken> extends EbnfParserTokenTestCase<T> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullTokensFails() {
        this.createToken(this.text(), Cast.<List<EbnfParserToken>>to(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithEmptyTokensFails() {
        this.createToken(this.text(), Lists.empty());
    }

    @Test
    public final void testWithCopiesTokens() {
        final List<EbnfParserToken> tokens = this.tokens();
        final String text = this.text();
        final T token = this.createToken(text, tokens);
        this.checkText(token, text);
        assertEquals("tokens", tokens, token.value());
        assertSame("tokens not copied", tokens, token.value());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespaceCached() {
        final T token = this.createToken();
        assertSame(token.withoutCommentsSymbolsOrWhitespace(), token.withoutCommentsSymbolsOrWhitespace());
        assertSame(token.withoutCommentsSymbolsOrWhitespace().get().withoutCommentsSymbolsOrWhitespace(), token.withoutCommentsSymbolsOrWhitespace().get().withoutCommentsSymbolsOrWhitespace());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespaceDoubleSame() {
        final T token = this.createToken();
        assertSame(token, token.withoutCommentsSymbolsOrWhitespace().get());
    }

    @Override
    final T createToken(final String text) {
        return this.createToken(text, this.tokens());
    }

    final T createToken(final String text, final EbnfParserToken...tokens) {
        return this.createToken(text, Lists.of(tokens));
    }

    abstract T createToken(final String text, final List<EbnfParserToken> tokens);

    abstract List<EbnfParserToken> tokens();

    final EbnfCommentParserToken comment(final String text) {
        return EbnfParserToken.comment(text, text);
    }

    final EbnfIdentifierParserToken identifier(final String text) {
        return EbnfParserToken.identifier(text, text);
    }

    final EbnfWhitespaceParserToken whitespace(final String text) {
        return EbnfParserToken.whitespace(text, text);
    }
}
