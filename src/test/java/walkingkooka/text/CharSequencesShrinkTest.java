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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.StaticMethodTestCase;

import static org.junit.Assert.assertEquals;

final public class CharSequencesShrinkTest extends StaticMethodTestCase {

    @Test
    public void testNullCharSequenceFails() {
        this.shrinkFails(null, 10);
    }

    @Test
    public void testInvalidDesiredLengthFails() {
        this.shrinkFails("apple", 5);
    }

    private void shrinkFails(final CharSequence sequence, final int desiredLength) {
        try {
            CharSequences.shrink(sequence, desiredLength);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testTooShort() {
        final String string = "apple";
        this.shrink(string, 10, string);
    }

    @Test
    public void testAlreadyDesiredLengthed() {
        this.shrink("greenapple", 10, "greenapple");
    }

    @Test
    public void testNeedsShrinking() {
        this.shrink("apple banana", 10, "appl...ana");
    }

    @Test
    public void testNeedsShrinking2() {
        this.shrink("apple banana carrot", 11, "appl...rrot");
    }

    private void shrink(final CharSequence sequence, final int desiredLength,
                        final CharSequence expected) {
        final CharSequence actual = CharSequences.shrink(sequence, desiredLength);
        assertEquals("Shrinking \"" + sequence + "\" with a desired length=" + desiredLength,
                    expected,
                    actual);
    }
}
