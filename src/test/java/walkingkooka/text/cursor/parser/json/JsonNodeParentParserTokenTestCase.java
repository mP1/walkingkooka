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
package walkingkooka.text.cursor.parser.json;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

public abstract class JsonNodeParentParserTokenTestCase<T extends JsonNodeParentParserToken> extends JsonNodeParserTokenTestCase<T> {

    final static String NUMBER1 = "1";
    final static String NUMBER2 = "22";

    final static String TEXT1 = "text-1";
    final static String TEXT2 = "text-2";

    final static String WHITESPACE = "   ";

    @Test(expected = NullPointerException.class)
    public final void testWithNullTokensFails() {
        this.createToken(this.text(), Cast.<List<ParserToken>>to(null));
    }

    @Test
    public final void testWithCopiesTokens() {
        final List<ParserToken> tokens = this.tokens();
        final String text = this.text();
        final T token = this.createToken(text, tokens);
        this.checkText(token, text);
        assertEquals("tokens", tokens, token.value());
        assertSame("tokens not copied", tokens, token.value());
    }

    @Test
    public void testWithoutSymbolsOrWhitespaceCached() {
        final T token = this.createToken();
        assertSame(token.withoutSymbolsOrWhitespace(), token.withoutSymbolsOrWhitespace());
        assertSame(token.withoutSymbolsOrWhitespace().get().withoutSymbolsOrWhitespace(), token.withoutSymbolsOrWhitespace().get().withoutSymbolsOrWhitespace());
    }

    @Test
    public void testWithoutSymbolsOrWhitespaceDoubleSame() {
        final T token = this.createToken();
        assertSame(token, token.withoutSymbolsOrWhitespace().get());
    }

    abstract T createToken(final String text, final List<ParserToken> tokens);

    final T createToken(final String text) {
        return this.createToken(text, this.tokens());
    }

    final T createToken(final String text, final ParserToken...tokens) {
        return this.createToken(text, Lists.of(tokens));
    }

    abstract String text();

    abstract List<ParserToken> tokens();

    final void checkValue(final T token, final ParserToken...tokens){
        assertEquals("value", Lists.of(tokens), token.value());
    }
}
