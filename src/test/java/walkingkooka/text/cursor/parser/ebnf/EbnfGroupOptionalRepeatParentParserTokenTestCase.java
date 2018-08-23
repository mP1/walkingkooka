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
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public abstract class EbnfGroupOptionalRepeatParentParserTokenTestCase<T extends EbnfParentParserToken<T>> extends EbnfParentParserTokenTestCase2<T> {

    @Test(expected = IllegalArgumentException.class)
    public final void testTooManyTokensIgnoringCommentsSymbolsWhitespaceFails() {
        this.createToken(this.text(), this.identifier1(), this.comment2(), this.identifier("identifier-3"));
    }

    @Test
    public void testWithoutCommentsSymbolsWhitespace() {
        final EbnfParserToken identifier1 = this.identifier1();
        final EbnfParserToken comment2 = this.comment2();

        final T token = this.createToken(this.text(), identifier1, comment2);
        assertEquals("value", Lists.of(identifier1, comment2), token.value());

        final T without = token.withoutCommentsSymbolsOrWhitespace().get().cast();
        assertNotSame(token, without);
        assertEquals("value", Lists.of(identifier1), without.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetValueWrongCountFails() {
        this.createToken().setValue(Lists.of(identifier1(), identifier2()));
    }

    @Test
    public final void testSetValueDifferent() {
        final T token = this.createToken();

        final EbnfParserToken differentValue = this.identifier2();
        final T different = token.setValue(Lists.of(differentValue));
        assertNotSame(token, different);

        this.checkValue(different, differentValue);
        assertEquals(Optional.of(different), different.withoutCommentsSymbolsOrWhitespace());
    }

    @Test
    public final void testSetValueDifferent2() {
        final T token = this.createToken();

        final EbnfParserToken differentValue = this.identifier2();
        final T different = token.setValue(Lists.of(differentValue, whitespace()));
        assertNotSame(token, different);

        this.checkValue(different, differentValue, whitespace());

        final Optional<EbnfParserToken> differentWithout = different.withoutCommentsSymbolsOrWhitespace();
        assertNotEquals(Optional.of(different), differentWithout);

        this.checkValue(differentWithout.get(), differentValue);
    }

    @Override
    final List<ParserToken> tokens() {
        return Lists.of(this.identifier1());
    }

    @Override
    protected T createDifferentToken() {
        return this.createToken(this.openChar() + "different" + this.closeChar(), this.identifier("different"));
    }

    @Override
    final String text() {
        return this.openChar() + this.identifier1().text() + this.closeChar();
    }

    abstract String openChar();

    abstract String closeChar();
}
