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

import static org.junit.Assert.assertEquals;

final public class RepeatingCharSequenceTest extends CharSequenceTestCase<RepeatingCharSequence> {

    // constants

    private final static char CHAR = 'x';

    private final static int LENGTH = 5;

    // tests

    @Test
    public void testWithInvalidLengthFails() {
        try {
            RepeatingCharSequence.with(RepeatingCharSequenceTest.CHAR, -1);
            Assert.fail();
        } catch (final IllegalArgumentException expected) {
        }
    }

    @Test
    public void testEmpty() {
        assertSame(CharSequences.empty(), RepeatingCharSequence.with('x', 0));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt(RepeatingCharSequenceTest.CHAR,
                RepeatingCharSequenceTest.CHAR,
                RepeatingCharSequenceTest.CHAR,
                RepeatingCharSequenceTest.CHAR,
                RepeatingCharSequenceTest.CHAR);
    }

    @Test
    public void testLength() {
        this.checkLength(RepeatingCharSequenceTest.LENGTH);
    }

    @Test
    public void testSubSequence() {
        final RepeatingCharSequence sequence = (RepeatingCharSequence) RepeatingCharSequence.with(
                RepeatingCharSequenceTest.CHAR,
                RepeatingCharSequenceTest.LENGTH);
        final CharSequence sub = sequence.subSequence(1, 2);
        assertEquals("class", RepeatingCharSequence.class, sub.getClass());
        this.checkEquals(sub, RepeatingCharSequenceTest.CHAR);
    }

    @Override
    protected RepeatingCharSequence createCharSequence() {
        return (RepeatingCharSequence) RepeatingCharSequence.with(RepeatingCharSequenceTest.CHAR,
                RepeatingCharSequenceTest.LENGTH);
    }
}
