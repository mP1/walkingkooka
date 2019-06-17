/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.SearchNode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class EbnfGroupOptionalRepeatParentParserTokenTestCase<T extends EbnfParentParserToken<T>> extends EbnfParentParserTokenTestCase2<T> {

    EbnfGroupOptionalRepeatParentParserTokenTestCase() {
        super();
    }

    @Test
    public final void testTooManyTokensIgnoringCommentsSymbolsWhitespaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), this.identifier1(), this.comment2(), this.identifier("identifier3"));
        });
    }

    @Test
    public void testWithoutCommentsSymbolsWhitespace() {
        final EbnfParserToken identifier1 = this.identifier1();
        final EbnfParserToken comment2 = this.comment2();

        final T token = this.createToken(this.text(), identifier1, comment2);
        assertEquals(Lists.of(identifier1, comment2), token.value(), "value");

        final T without = token.withoutCommentsSymbolsOrWhitespace().get().cast();
        assertNotSame(token, without);
        assertEquals(Lists.of(identifier1), without.value(), "value");
    }

    @Test
    public final void testToSearchNode() {
        final T token = this.createToken(this.text(), symbol(this.openChar()), this.identifier1(), symbol(this.closeChar()));
        final SearchNode searchNode = token.toSearchNode();

        assertEquals(token.text(), searchNode.text(), "text");
    }

    @Override
    final List<ParserToken> tokens() {
        return Lists.of(this.identifier1());
    }

    @Override
    public T createDifferentToken() {
        return this.createToken(this.openChar() + "different" + this.closeChar(), this.identifier("different"));
    }

    @Override
    final public String text() {
        return this.openChar() + this.identifier1().text() + this.closeChar();
    }

    abstract String openChar();

    abstract String closeChar();
}
