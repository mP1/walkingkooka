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

public abstract class ParserTokenTestCase<T extends ParserToken> extends PublicClassTestCase<T> {

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
