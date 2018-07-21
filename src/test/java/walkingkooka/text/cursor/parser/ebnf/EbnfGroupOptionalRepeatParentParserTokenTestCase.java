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

import static org.junit.Assert.assertNotSame;

public abstract class EbnfGroupOptionalRepeatParentParserTokenTestCase<T extends EbnfParentParserToken> extends EbnfParentParserTokenTestCase2<T> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullTokenFails() {
        this.createToken(this.text(), Cast.<List<EbnfParserToken>>to(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTooManyTokensIgnoringCommentsSymbolsWhitespaceFails() {
        this.createToken(this.text(), this.identifier("identifier-1"), this.comment("(*comment-2*)"), this.identifier("identifier-3"));
    }

    @Test
    public void testWithoutCommentsSymbolsWhitespace() {
        final EbnfParserToken identifier1 = this.identifier("identifier1");
        final EbnfParserToken comment2 = this.comment("(*comment2*)");

        final T token = this.createToken(this.text(), identifier1, comment2);
        assertEquals("value", Lists.of(identifier1, comment2), token.value());

        final T without = token.withoutCommentsSymbolsOrWhitespace().get().cast();
        assertNotSame(token, without);
        assertEquals("value", Lists.of(identifier1), without.value());
    }


    @Override
    final List<EbnfParserToken> tokens() {
        return Lists.of(this.identifier("identifier-1"));
    }

    @Override
    protected T createDifferentToken() {
        return this.createToken(this.openChar() + "different" + this.closeChar(), this.identifier("different"));
    }

    @Override
    final String text() {
        return this.openChar() + "identifier1" + this.closeChar();
    }

    abstract char openChar();

    abstract char closeChar();
}
