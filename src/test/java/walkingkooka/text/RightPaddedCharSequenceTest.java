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
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

final public class RightPaddedCharSequenceTest extends CharSequenceTestCase<RightPaddedCharSequence> {

    // constants

    private final static String SEQUENCE = "abcde";

    private final static int LENGTH = 8;

    private final static char PADDING = '.';

    // tests

    @Test(expected = NullPointerException.class)
    public void testWrapNullCharSequenceFails() {
        RightPaddedCharSequence.wrap(null, RightPaddedCharSequenceTest.LENGTH, RightPaddedCharSequenceTest.PADDING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapInvalidLengthFails() {
        RightPaddedCharSequence.wrap(RightPaddedCharSequenceTest.SEQUENCE, 4, RightPaddedCharSequenceTest.PADDING);
    }

    @Test
    public void testPaddingNotRequired() {
        assertSame(RightPaddedCharSequenceTest.SEQUENCE,
                RightPaddedCharSequence.wrap(RightPaddedCharSequenceTest.SEQUENCE,
                        5,
                        RightPaddedCharSequenceTest.PADDING));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt(
                "abcde" + RightPaddedCharSequenceTest.PADDING + RightPaddedCharSequenceTest.PADDING
                        + RightPaddedCharSequenceTest.PADDING);
    }

    @Test
    public void testLength() {
        this.checkLength(RightPaddedCharSequence.wrap(RightPaddedCharSequenceTest.SEQUENCE,
                RightPaddedCharSequenceTest.LENGTH,
                RightPaddedCharSequenceTest.PADDING), RightPaddedCharSequenceTest.LENGTH);
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(6, 7);
        assertNotEquals("class", RightPaddedCharSequence.class, sub.getClass());
        this.checkEquals(sub, RightPaddedCharSequenceTest.PADDING);
        assertEquals("sub.toString", ".", sub.toString());
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(5, 8);
        assertNotEquals("class", RightPaddedCharSequence.class, sub.getClass());
        this.checkEquals(sub,
                RightPaddedCharSequenceTest.PADDING,
                RightPaddedCharSequenceTest.PADDING,
                RightPaddedCharSequenceTest.PADDING);
        assertEquals("sub.toString", "...", sub.toString());
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        assertSame(RightPaddedCharSequenceTest.SEQUENCE,
                sequence.subSequence(0, RightPaddedCharSequenceTest.SEQUENCE.length()));
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 4);
        this.checkEquals(sub, "bcd");
        assertEquals("sub.toString", "bcd", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 7);
        this.checkEquals(sub, "abcde..");
        assertEquals("sub.toString", "abcde..", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 6);
        this.checkEquals(sub, "bcde.");
        assertEquals("sub.toString", "bcde.", sub.toString());
    }

    @Test
    public void testToString() {
        assertEquals("abcde...", this.createCharSequence().toString());
    }

    @Override
    protected RightPaddedCharSequence createCharSequence() {
        return (RightPaddedCharSequence) RightPaddedCharSequence.wrap(RightPaddedCharSequenceTest.SEQUENCE,
                RightPaddedCharSequenceTest.LENGTH,
                RightPaddedCharSequenceTest.PADDING);
    }

    @Override
    protected Class<RightPaddedCharSequence> type() {
        return RightPaddedCharSequence.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
