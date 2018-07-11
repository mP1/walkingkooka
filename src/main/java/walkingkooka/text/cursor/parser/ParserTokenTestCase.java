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

package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertNotSame;

public abstract class ParserTokenTestCase<T extends ParserToken> extends PublicClassTestCase<T> {

    @Test(expected = NullPointerException.class)
    public final void testSetTextNullFails() {
        this.createToken().setText(null);
    }

    @Test
    public final void testSetTextSame() {
        final T token = this.createToken();
        assertSame(token, token.setText(token.text()));
    }

    @Test
    public final void testSetTextDifferent() {
        final T token = this.createToken();
        final String differentText = this.createDifferentToken().text();
        assertNotEquals("different text must be different from tokens", token.text(), differentText);

        final ParserToken token2 = token.setText(differentText);
        assertNotSame(token, token2);
        assertEquals("text", differentText, token2.text());
        assertEquals("type of token after set must remain the same=" + token2, token.getClass(), token2.getClass());

        assertNotEquals("tokens must be different", token, token2);

        final ParserToken token3 = token2.setText(token.text());
        assertEquals("after setting original text tokens must be equal", token, token3);
    }

    @Test
    public final void testEqualsNull() {
        assertNotEquals(this.createToken(), null);
    }

    @Test
    public final void testEqualsObject() {
        assertNotEquals(this.createToken(), new Object());
    }

    @Test
    public final void testEqualsSame() {
        final T token = this.createToken();
        assertEquals(token, token);
    }

    @Test
    public final void testEqualsEqual() {
        assertEquals(this.createToken(), this.createToken());
    }

    @Test
    public final void testEqualsDifferent() {
        assertNotEquals(this.createToken(), this.createDifferentToken());
    }

    protected abstract T createToken();

    protected abstract T createDifferentToken();
}
