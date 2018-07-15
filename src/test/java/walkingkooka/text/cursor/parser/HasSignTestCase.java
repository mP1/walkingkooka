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
import walkingkooka.Cast;
import walkingkooka.test.TestCase;

public abstract class HasSignTestCase<T extends HasSign & ParserToken> extends TestCase {

    @Test
    public void testIsNegativeZero() {
        final T token = this.createToken(0);
        assertEquals(token.toString(), false, token.isNegative());
    }

    @Test
    public void testIsNegativePositive() {
        final T token = this.createToken(1);
        assertEquals(token.toString(), false, token.isNegative());
    }

    @Test
    public void testIsNegativeNegative() {
        final T token = this.createToken(-2);
        assertEquals(token.toString(), true, token.isNegative());
    }

    @Test
    public void testSetNegativeSamePositive() {
        final T token = this.createToken(1);
        assertSame(token, token.setNegative(false));
    }

    @Test
    public void testSetNegativeSameNegtive() {
        final T token = this.createToken(-2);
        assertSame(token, token.setNegative(true));
    }

    @Test
    public void testSetNegativeDifferentNegative() {
        final T token = this.createToken(3);
        final T different = Cast.to(token.setNegative(true));
        assertSame(this.createToken(-3), different);
        assertEquals("text", token.text(), different.text());
    }

    @Test
    public void testSetNegativeDifferentPositive() {
        final T token = this.createToken(-4);
        final T different = Cast.to(token.setNegative(false));
        assertSame(this.createToken(4), different);
        assertEquals("text", token.text(), different.text());
    }

    abstract T createToken(final int number);
}
