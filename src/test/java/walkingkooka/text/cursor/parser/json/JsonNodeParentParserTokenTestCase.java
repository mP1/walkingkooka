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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public abstract class JsonNodeParentParserTokenTestCase<T extends JsonNodeParentParserToken<T>> extends JsonNodeParserTokenTestCase<T> {

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
        assertEquals("tokens not copied", tokens, token.value());
    }

    @Test
    public final void testWithoutSymbolsOrWhitespaceCached() {
        final T token = this.createToken();
        assertSame(token.withoutSymbolsOrWhitespace(), token.withoutSymbolsOrWhitespace());
        assertSame(token.withoutSymbolsOrWhitespace().get().withoutSymbolsOrWhitespace(), token.withoutSymbolsOrWhitespace().get().withoutSymbolsOrWhitespace());
    }

    @Test
    public final void testWithoutSymbolsOrWhitespaceDoubleSame() {
        final T token = this.createToken();
        assertSame(token.withoutSymbolsOrWhitespace(), token.withoutSymbolsOrWhitespace());
    }

    @Test
    public final void testSetTextDifferentWithout() {
        final T token = this.createToken();
        final List<?> childrenWithout = Cast.<T>to(token.withoutSymbolsOrWhitespace().get()).value();

        final String differentText = this.createDifferentToken().text();
        final T different = token.setText(differentText).cast();
        final T differentWithout = Cast.<T>to(different.withoutSymbolsOrWhitespace().get());
        assertEquals("children without", childrenWithout, differentWithout.value());

        assertNotEquals("without should have less tokens than with", token.value().size(), differentWithout.value().size());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespacePropertiesNullCheck() throws Exception {
        final Optional<JsonNodeParserToken> without = this.createToken().withoutSymbolsOrWhitespace();
        if(without.isPresent()){
            this.propertiesNeverReturnNullCheck(without.get());
        }
    }

    @Override
    protected T createToken(final String text) {
        return this.createToken(text, this.tokens());
    }

    abstract T createToken(final String text, final List<ParserToken> tokens);

    final T createToken(final String text, final ParserToken...tokens) {
        return this.createToken(text, Lists.of(tokens));
    }

    abstract List<ParserToken> tokens();

    final void checkValue(final T token, final ParserToken...tokens){
        assertEquals("value", Lists.of(tokens), token.value());
    }
}
