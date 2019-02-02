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

import org.junit.jupiter.api.Test;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

final public class LeftPaddedCharSequence2Test extends CharSequenceTestCase<LeftPaddedCharSequence2>
        implements SerializationTesting<LeftPaddedCharSequence2> {

    // constants

    private final static String SEQUENCE = "123abcde456";

    private final static int SEQUENCE_OFFSET = 3;

    private final static int SEQUENCE_LENGTH = 5;

    private final static int PADDING_LENGTH = 3;

    private final static char PADDING = '.';

    private final static int LENGTH = PADDING_LENGTH
            + SEQUENCE_LENGTH;

    // tests

    @Test
    public void testCharAt() {
        this.checkCharAt(PADDING,
                PADDING,
                PADDING,
                'a',
                'b',
                'c',
                'd',
                'e');
    }

    @Test
    public void testLength() {
        this.checkLength(LENGTH);
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 1);
        assertNotEquals(LeftPaddedCharSequence2.class, sub.getClass(), "class");
        this.checkEquals2(sub, PADDING);
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 3);
        assertNotEquals(LeftPaddedCharSequence2.class, sub.getClass(), "class");
        this.checkEquals2(sub,
                PADDING,
                PADDING,
                PADDING);
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        assertEquals("abcde", sequence.subSequence(3, LENGTH));
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(4, LENGTH);
        this.checkEquals2(sub, "bcde".toCharArray());
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, LENGTH);
        this.checkEquals2(sub, "..abcde".toCharArray());
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7);
        this.checkSubSequence(sequence, 1, 7, "..abcd");
    }

    @Test
    public void testSubSequenceTwice() {
        final LeftPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7).subSequence(1, 5);
        this.checkEquals2(sub, ".abc");
    }

    @Test
    public void testEqualsSameWrappedButDifferentOffsetAndLength() {
        final CharSequence sequence = "abcde";
        checkNotEquals(LeftPaddedCharSequence2.wrap(sequence,
                0,
                3,
                PADDING_LENGTH,
                PADDING),
                LeftPaddedCharSequence2.wrap(sequence,
                        1,
                        3,
                        PADDING_LENGTH,
                        PADDING));
    }

    @Test
    public void testEqualsSameButDifferentOffset() {
        checkEqualsAndHashCode(LeftPaddedCharSequence2.wrap(
                "..abc..",
                2,
                3,
                PADDING_LENGTH,
                PADDING),
                LeftPaddedCharSequence2.wrap(".abc.",
                        1,
                        3,
                        PADDING_LENGTH,
                        PADDING));
    }

    @Test
    public void testToString() {
        assertEquals("...abcde", this.createCharSequence().toString());
    }

    @Override
    protected LeftPaddedCharSequence2 createCharSequence() {
        return LeftPaddedCharSequence2.wrap(SEQUENCE,
                SEQUENCE_OFFSET,
                SEQUENCE_LENGTH,
                PADDING_LENGTH,
                PADDING);
    }

    @Override
    public Class<LeftPaddedCharSequence2> type() {
        return LeftPaddedCharSequence2.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public LeftPaddedCharSequence2 serializableInstance() {
        return LeftPaddedCharSequence2.wrap("123abcde456", 3, 5, 5, 'X');
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
