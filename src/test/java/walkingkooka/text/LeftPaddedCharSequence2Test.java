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
import static org.junit.Assert.assertNotEquals;

final public class LeftPaddedCharSequence2Test extends CharSequenceTestCase<LeftPaddedCharSequence2> {

    // constants

    private final static String SEQUENCE = "123abcde456";

    private final static int SEQUENCE_OFFSET = 3;

    private final static int SEQUENCE_LENGTH = 5;

    private final static int PADDING_LENGTH = 3;

    private final static char PADDING = '.';

    private final static int LENGTH = LeftPaddedCharSequence2Test.PADDING_LENGTH
            + LeftPaddedCharSequence2Test.SEQUENCE_LENGTH;

    // tests

    @Test
    public void testCharAt() {
        this.checkCharAt(LeftPaddedCharSequence2Test.PADDING,
                LeftPaddedCharSequence2Test.PADDING,
                LeftPaddedCharSequence2Test.PADDING,
                'a',
                'b',
                'c',
                'd',
                'e');
    }

    @Test
    public void testLength() {
        this.checkLength(LeftPaddedCharSequence2Test.LENGTH);
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 1);
        assertNotEquals("class", LeftPaddedCharSequence2.class, sub.getClass());
        this.checkEquals(sub, LeftPaddedCharSequence2Test.PADDING);
        assertEquals("sub.toString", ".", sub.toString());
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 3);
        assertNotEquals("class", LeftPaddedCharSequence2.class, sub.getClass());
        this.checkEquals(sub,
                LeftPaddedCharSequence2Test.PADDING,
                LeftPaddedCharSequence2Test.PADDING,
                LeftPaddedCharSequence2Test.PADDING);
        assertEquals("sub.toString", "...", sub.toString());
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        assertEquals("abcde", sequence.subSequence(3, LeftPaddedCharSequence2Test.LENGTH));
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(4, LeftPaddedCharSequence2Test.LENGTH);
        this.checkEquals(sub, "bcde".toCharArray());
        assertEquals("sub.toString", "bcde", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, LeftPaddedCharSequence2Test.LENGTH);
        this.checkEquals(sub, "..abcde".toCharArray());
        assertEquals("sub.toString", "..abcde", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7);
        this.checkSubSequence(sequence, 1, 7, "..abcd");
        assertEquals("sub.toString", "..abcd", sub.toString());
    }

    @Test
    public void testSubSequenceTwice() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7).subSequence(1, 5);
        this.checkEquals(sub, ".abc");
        assertEquals("sub.toString", ".abc", sub.toString());
    }

    @Test
    public void testToString() {
        assertEquals("...abcde", this.createCharSequence().toString());
    }

    @Override
    protected LeftPaddedCharSequence2 createCharSequence() {
        return LeftPaddedCharSequence2.wrap(LeftPaddedCharSequence2Test.SEQUENCE,
                LeftPaddedCharSequence2Test.SEQUENCE_OFFSET,
                LeftPaddedCharSequence2Test.SEQUENCE_LENGTH,
                LeftPaddedCharSequence2Test.PADDING_LENGTH,
                LeftPaddedCharSequence2Test.PADDING);
    }
}
