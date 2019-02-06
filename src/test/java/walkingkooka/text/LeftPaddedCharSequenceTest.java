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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LeftPaddedCharSequenceTest extends CharSequenceTestCase<LeftPaddedCharSequence>
        implements SerializationTesting<LeftPaddedCharSequence>{

    // constants

    private final static String SEQUENCE = "abcde";

    private final static int LENGTH = 8;

    private final static char PADDING = '.';

    // tests

    @Test
    public void testWrapNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> {
            this.wrapFails(null, LENGTH);
        });
    }

    @Test
    public void testWrapInvalidLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.wrapFails(SEQUENCE, 4);
        });
    }

    private void wrapFails(final CharSequence sequence, final int length) {
        LeftPaddedCharSequence.wrap(sequence, length, PADDING);
    }

    @Test
    public void testPaddingNotRequired() {
        assertSame(SEQUENCE,
                LeftPaddedCharSequence.wrap(SEQUENCE,
                        5,
                        PADDING));
    }

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
        assertEquals(LENGTH,
                LeftPaddedCharSequence.wrap(SEQUENCE,
                        LENGTH,
                        PADDING).length());
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 1);
        assertNotEquals(LeftPaddedCharSequence.class, sub.getClass(), "class");
        this.checkEquals2(sub, PADDING);
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 3);
        assertNotEquals(LeftPaddedCharSequence.class, sub.getClass(), "class");
        this.checkEquals2(sub,
                PADDING,
                PADDING,
                PADDING);
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        assertSame(SEQUENCE,
                sequence.subSequence(3, LENGTH));
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(4, LENGTH);
        this.checkEquals2(sub, "bcde");
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final LeftPaddedCharSequence sequence = this.createCharSequence(); // ...abcde
        final CharSequence sub = sequence.subSequence(1, LENGTH);
        this.checkEquals2(sub, "..abcde");
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final LeftPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7);
        this.checkEquals2(sub, "..abcd");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharSequence(), "...abcde");
    }

    @Override
    protected LeftPaddedCharSequence createCharSequence() {
        return (LeftPaddedCharSequence) LeftPaddedCharSequence.wrap(SEQUENCE,
                LENGTH,
                PADDING);
    }

    @Override
    public Class<LeftPaddedCharSequence> type() {
        return LeftPaddedCharSequence.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public LeftPaddedCharSequence serializableInstance() {
        return (LeftPaddedCharSequence) LeftPaddedCharSequence.wrap("x", 2, 'p');
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
