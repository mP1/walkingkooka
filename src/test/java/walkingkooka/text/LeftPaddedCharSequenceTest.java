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

final public class LeftPaddedCharSequenceTest extends CharSequenceTestCase<LeftPaddedCharSequence> {

    // constants

    private final static String SEQUENCE = "abcde";

    private final static int LENGTH = 8;

    private final static char PADDING = '.';

    // tests

    @Test(expected = NullPointerException.class)
    public void testWrapNullCharSequenceFails() {
        this.wrapFails(null, LeftPaddedCharSequenceTest.LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapInvalidLengthFails() {
        this.wrapFails(LeftPaddedCharSequenceTest.SEQUENCE, 4);
    }

    private void wrapFails(final CharSequence sequence, final int length) {
        LeftPaddedCharSequence.wrap(sequence, length, LeftPaddedCharSequenceTest.PADDING);
    }

    @Test
    public void testPaddingNotRequired() {
        assertSame(LeftPaddedCharSequenceTest.SEQUENCE,
                LeftPaddedCharSequence.wrap(LeftPaddedCharSequenceTest.SEQUENCE,
                        5,
                        LeftPaddedCharSequenceTest.PADDING));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt(LeftPaddedCharSequenceTest.PADDING,
                LeftPaddedCharSequenceTest.PADDING,
                LeftPaddedCharSequenceTest.PADDING,
                'a',
                'b',
                'c',
                'd',
                'e');
    }

    @Test
    public void testLength() {
        assertEquals(LeftPaddedCharSequenceTest.LENGTH,
                LeftPaddedCharSequence.wrap(LeftPaddedCharSequenceTest.SEQUENCE,
                        LeftPaddedCharSequenceTest.LENGTH,
                        LeftPaddedCharSequenceTest.PADDING).length());
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 1);
        assertNotEquals("class", LeftPaddedCharSequence.class, sub.getClass());
        this.checkEquals(sub, LeftPaddedCharSequenceTest.PADDING);
        assertEquals("sub.toString", ".", sub.toString());
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 3);
        assertNotEquals("class", LeftPaddedCharSequence.class, sub.getClass());
        this.checkEquals(sub,
                LeftPaddedCharSequenceTest.PADDING,
                LeftPaddedCharSequenceTest.PADDING,
                LeftPaddedCharSequenceTest.PADDING);
        assertEquals("sub.toString", "...", sub.toString());
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        assertSame(LeftPaddedCharSequenceTest.SEQUENCE,
                sequence.subSequence(3, LeftPaddedCharSequenceTest.LENGTH));
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(4, LeftPaddedCharSequenceTest.LENGTH);
        this.checkEquals(sub, "bcde");
        assertEquals("sub.toString", "bcde", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final LeftPaddedCharSequence sequence = this.createCharSequence(); // ...abcde
        final CharSequence sub = sequence.subSequence(1, LeftPaddedCharSequenceTest.LENGTH);
        this.checkEquals(sub, "..abcde");
        assertEquals("sub.toString", "..abcde", sub.toString());
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7);
        this.checkEquals(sub, "..abcd");
        assertEquals("sub.toString", "..abcd", sub.toString());
    }

    @Test
    public void testToString() {
        assertEquals("...abcde", this.createCharSequence().toString());
    }

    @Override
    protected LeftPaddedCharSequence createCharSequence() {
        return (LeftPaddedCharSequence) LeftPaddedCharSequence.wrap(LeftPaddedCharSequenceTest.SEQUENCE,
                LeftPaddedCharSequenceTest.LENGTH,
                LeftPaddedCharSequenceTest.PADDING);
    }

    @Override
    protected Class<LeftPaddedCharSequence> type() {
        return LeftPaddedCharSequence.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
