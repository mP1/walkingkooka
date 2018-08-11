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

package walkingkooka.text;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

final public class WhitespaceCharSequenceTest extends CharSequenceTestCase<Whitespace> {

    @Test
    public void testCharAt() {
        this.checkCharAt(" \t\r\n");
    }

    @Test
    public void testSubSequence() {
        assertEquals(Whitespace.with("\t"), Whitespace.with(" \t\r ").subSequence(1, 2));
    }

    @Override
    @Test
    public void testEmptySubSequence() {
        final Whitespace whitespace = this.createCharSequence();
        assertSame(Whitespace.EMPTY, whitespace.subSequence(2, 2));
    }

    @Override
    protected Whitespace createCharSequence() {
        return Whitespace.with(" \t\r\n");
    }
}
