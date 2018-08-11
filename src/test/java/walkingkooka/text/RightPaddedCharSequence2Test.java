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

final public class RightPaddedCharSequence2Test
        extends CharSequenceTestCase<RightPaddedCharSequence2> {

    // constants

    private final static String SEQUENCE = "123abcde456";

    private final static int SEQUENCE_OFFSET = 3;

    private final static int SEQUENCE_LENGTH = 5;

    private final static int PADDING_LENGTH = 3;

    private final static char PADDING = '.';

    private final static int LENGTH = RightPaddedCharSequence2Test.PADDING_LENGTH
            + RightPaddedCharSequence2Test.SEQUENCE_LENGTH;

    // tests

    @Test
    public void testCharAt() {
        this.checkCharAt("abcde" + RightPaddedCharSequence2Test.PADDING
                + RightPaddedCharSequence2Test.PADDING + RightPaddedCharSequence2Test.PADDING);
    }

    @Test
    public void testLength() {
        this.checkLength(RightPaddedCharSequence2.wrap(RightPaddedCharSequence2Test.SEQUENCE,
                RightPaddedCharSequence2Test.SEQUENCE_OFFSET,
                RightPaddedCharSequence2Test.SEQUENCE_LENGTH,
                RightPaddedCharSequence2Test.PADDING_LENGTH,
                RightPaddedCharSequence2Test.PADDING), RightPaddedCharSequence2Test.LENGTH);
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(6, 7);
        assertNotEquals("class", RightPaddedCharSequence2.class, sub.getClass());
        this.checkEquals(sub, RightPaddedCharSequence2Test.PADDING);
        assertEquals("sub.toString", ".", sub.toString());
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(5, 8);
        assertNotEquals("class", RightPaddedCharSequence2.class, sub.getClass());
        this.checkEquals(sub,
                RightPaddedCharSequence2Test.PADDING,
                RightPaddedCharSequence2Test.PADDING,
                RightPaddedCharSequence2Test.PADDING);
        assertEquals("sub.toString", "...", sub.toString());
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        this.checkSubSequence(0, RightPaddedCharSequence2Test.SEQUENCE_LENGTH, "abcde");
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 4);
        this.checkEquals(sub, "bcd");
        assertEquals("sub.toString", "bcd", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 7);
        this.checkEquals(sub, "abcde..");
        assertEquals("sub.toString", "abcde..", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 6);
        this.checkEquals(sub, "bcde.");
        assertEquals("sub.toString", "bcde.", sub.toString());
    }

    @Test
    public void testSubSequenceTwice() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7).subSequence(1, 5);
        this.checkEquals(sub, "cde.");
        assertEquals("sub.toString", "cde.", sub.toString());
    }

    @Test
    public void testToString() {
        assertEquals("abcde...", this.createCharSequence().toString());
    }

    @Override
    protected RightPaddedCharSequence2 createCharSequence() {
        return RightPaddedCharSequence2.wrap(RightPaddedCharSequence2Test.SEQUENCE,
                RightPaddedCharSequence2Test.SEQUENCE_OFFSET,
                RightPaddedCharSequence2Test.SEQUENCE_LENGTH,
                RightPaddedCharSequence2Test.PADDING_LENGTH,
                RightPaddedCharSequence2Test.PADDING);
    }
}
