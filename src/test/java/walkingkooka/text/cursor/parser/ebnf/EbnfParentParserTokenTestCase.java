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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class EbnfParentParserTokenTestCase<T extends EbnfParentParserToken<T>> extends EbnfParserTokenTestCase<T> {

    final static String COMMENT1 = "(*comment-1*)";
    final static String COMMENT2 = "(*comment-2*)";

    final static String TERMINAL_TEXT1 = "terminal-1";
    final static String TERMINAL_TEXT2 = "terminal-2";

    final static String WHITESPACE = "   ";

    EbnfParentParserTokenTestCase() {
        super();
    }

    @Test
    public final void testWithNullTokensFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createToken(this.text(), Cast.<List<ParserToken>>to(null));
        });
    }

    @Test
    public final void testWithEmptyTokensFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), Lists.empty());
        });
    }

    @Test
    public final void testWithCopiesTokens() {
        final List<ParserToken> tokens = this.tokens();
        final String text = this.text();
        final T token = this.createToken(text, tokens);
        this.checkText(token, text);
        this.checkValue(token, tokens);
        assertEquals(tokens, token.value(), "tokens");
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
        assertSame(token.withoutCommentsSymbolsOrWhitespace(), token.withoutCommentsSymbolsOrWhitespace());
    }

    @Test
    public final void testSetTextDifferentWithoutCommentsSymbolsOrWhitespace() {
        final T token = this.createTokenWithNoise();

        final String originalText = token.text();
        final List<ParserToken> valueWithout = Cast.<T>to(token.withoutCommentsSymbolsOrWhitespace().get()).value();

        final String differentText = "different";
        final T different = token.setText(differentText).cast();
        assertEquals(differentText, different.text(), "text");
        this.checkValue(Cast.<T>to(different.withoutCommentsSymbolsOrWhitespace().get()), valueWithout);

        assertEquals(originalText, token.text(), "original name");
        this.checkValue(different.withoutCommentsSymbolsOrWhitespace().get().cast(), valueWithout);

        assertNotEquals(token.value().size(), valueWithout.size(), "original token should have some comments/symbols/whitespace");
    }

    @Test
    public final void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createToken().setValue(null);
        });
    }

    @Test
    public final void testSetValueSame() {
        final T token = this.createToken();
        assertSame(token, token.setValue(token.value()));
    }

    @Test
    public final void testSetValueSame2() {
        final T token = this.createToken();
        assertSame(token, token.setValue(this.tokens()));
    }

    abstract T createTokenWithNoise();

    @Override
    public final T createToken(final String text) {
        return this.createToken(text, this.tokens());
    }

    final T createToken(final String text, final ParserToken... tokens) {
        return this.createToken(text, Lists.of(tokens));
    }

    abstract T createToken(final String text, final List<ParserToken> tokens);

    abstract List<ParserToken> tokens();

    final EbnfCommentParserToken comment1() {
        return this.comment(COMMENT1);
    }

    final EbnfCommentParserToken comment2() {
        return this.comment(COMMENT2);
    }

    final EbnfCommentParserToken comment(final String text) {
        return EbnfParserToken.comment(text, text);
    }

    final EbnfIdentifierParserToken identifier1() {
        return this.identifier("identifier1");
    }

    final EbnfIdentifierParserToken identifier2() {
        return this.identifier("identifier2");
    }

    final EbnfIdentifierParserToken identifier(final String text) {
        return EbnfParserToken.identifier(EbnfIdentifierName.with(text), text);
    }

    final EbnfWhitespaceParserToken whitespace() {
        return EbnfParserToken.whitespace(WHITESPACE, WHITESPACE);
    }

    final EbnfWhitespaceParserToken whitespace(final String text) {
        return EbnfParserToken.whitespace(text, text);
    }

    final EbnfTerminalParserToken terminal1() {
        return terminal(TERMINAL_TEXT1);
    }

    final EbnfTerminalParserToken terminal2() {
        return terminal(TERMINAL_TEXT2);
    }

    final EbnfTerminalParserToken terminal(final String text) {
        return EbnfParserToken.terminal(text, '"' + text + '"');
    }

    final EbnfSymbolParserToken assignment() {
        return symbol("=");
    }

    final EbnfSymbolParserToken between() {
        return symbol("..");
    }

    final EbnfSymbolParserToken terminator() {
        return symbol(";");
    }

    final void checkValue(final EbnfParserToken parent, final ParserToken... values) {
        checkValue(EbnfParentParserToken.class.cast(parent), values);
    }

    final void checkValue(final EbnfParentParserToken parent, final ParserToken... values) {
        checkValue(parent, Lists.of(values));
    }

    final void checkValue(final EbnfParentParserToken parent, final List<ParserToken> values) {
        assertEquals(values, parent.value(), "value");
    }
}
