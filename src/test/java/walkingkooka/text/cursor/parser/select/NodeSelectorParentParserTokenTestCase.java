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
package walkingkooka.text.cursor.parser.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodeSelectorParentParserTokenTestCase<T extends NodeSelectorParentParserToken<T>> extends NodeSelectorParserTokenTestCase<T> {

    final static String NUMBER1 = "1";
    final static String NUMBER2 = "22";

    final static String TEXT1 = "text-1";
    final static String TEXT2 = "text-2";

    final static String WHITESPACE = "   ";

    @Test
    public final void testWithNullTokensFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createToken(this.text(), Cast.<List<ParserToken>>to(null));
        });
    }

    @Test
    public final void testWithCopiesTokens() {
        final List<ParserToken> tokens = this.tokens();
        final String text = this.text();
        final T token = this.createToken(text, tokens);
        this.checkText(token, text);
        assertEquals(tokens, token.value(), "tokens");
        assertEquals(tokens, token.value(), "tokens not copied");
    }

    @Test
    public final void testWithoutSymbolsCached() {
        final T token = this.createToken();
        assertSame(token.withoutSymbols(), token.withoutSymbols());
        assertSame(token.withoutSymbols().get().withoutSymbols(), token.withoutSymbols().get().withoutSymbols());
    }

    @Test
    public final void testWithoutSymbolsDoubleSame() {
        final T token = this.createToken();
        assertSame(token.withoutSymbols(), token.withoutSymbols());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespacePropertiesNullCheck() throws Exception {
        final Optional<NodeSelectorParserToken> without = this.createToken().withoutSymbols();
        if (without.isPresent()) {
            BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(without.get(), Predicates.never());
        }
    }

    @Override
    public T createToken(final String text) {
        return this.createToken(text, this.tokens());
    }

    abstract T createToken(final String text, final List<ParserToken> tokens);

    final T createToken(final String text, final ParserToken... tokens) {
        return this.createToken(text, Lists.of(tokens));
    }

    abstract List<ParserToken> tokens();

    final void checkValue(final T token, final ParserToken... tokens) {
        assertEquals(Lists.of(tokens), token.value(), "value");
    }
}
