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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class RightPaddedCharSequenceTest extends CharSequenceTestCase<RightPaddedCharSequence>
        implements SerializationTesting<RightPaddedCharSequence> {

    // constants

    private final static String SEQUENCE = "abcde";

    private final static int LENGTH = 8;

    private final static char PADDING = '.';

    // tests

    @Test
    public void testWrapNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> {
            RightPaddedCharSequence.wrap(null, LENGTH, PADDING);
        });
    }

    @Test
    public void testWrapInvalidLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            RightPaddedCharSequence.wrap(SEQUENCE, 4, PADDING);
        });
    }

    @Test
    public void testPaddingNotRequired() {
        assertSame(SEQUENCE,
                RightPaddedCharSequence.wrap(SEQUENCE,
                        5,
                        PADDING));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt("abcde" + PADDING + PADDING
                        + PADDING);
    }

    @Test
    public void testLength() {
        this.checkLength(RightPaddedCharSequence.wrap(SEQUENCE,
                LENGTH,
                PADDING), LENGTH);
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(6, 7);
        assertNotEquals(RightPaddedCharSequence.class, sub.getClass(), "class");
        this.checkEquals2(sub, PADDING);
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(5, 8);
        assertNotEquals(RightPaddedCharSequence.class, sub.getClass(), "class");
        this.checkEquals2(sub,
                PADDING,
                PADDING,
                PADDING);
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        assertSame(SEQUENCE,
                sequence.subSequence(0, SEQUENCE.length()));
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        this.checkEquals2(sequence.subSequence(1, 4), "bcd");
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        this.checkEquals2(sequence.subSequence(0, 7), "abcde..");
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final RightPaddedCharSequence sequence = this.createCharSequence();
        this.checkEquals2(sequence.subSequence(1, 6), "bcde.");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharSequence(), "abcde...");
    }

    @Override
    protected RightPaddedCharSequence createCharSequence() {
        return (RightPaddedCharSequence) RightPaddedCharSequence.wrap(SEQUENCE,
                LENGTH,
                PADDING);
    }

    @Override
    public Class<RightPaddedCharSequence> type() {
        return RightPaddedCharSequence.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public RightPaddedCharSequence serializableInstance() {
        return (RightPaddedCharSequence) RightPaddedCharSequence.wrap("x", 2, 'p');
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
