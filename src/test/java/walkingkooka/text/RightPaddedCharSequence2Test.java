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

final public class RightPaddedCharSequence2Test extends CharSequenceTestCase<RightPaddedCharSequence2>
        implements SerializationTesting<RightPaddedCharSequence2> {

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
        this.checkCharAt("abcde" + PADDING
                + PADDING + PADDING);
    }

    @Test
    public void testLength() {
        this.checkLength(RightPaddedCharSequence2.wrap(SEQUENCE,
                SEQUENCE_OFFSET,
                SEQUENCE_LENGTH,
                PADDING_LENGTH,
                PADDING), LENGTH);
    }

    @Test
    public void testSubSequencePaddingOnly() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(6, 7);
        assertNotEquals(RightPaddedCharSequence2.class, sub.getClass(), "class");
        this.checkEquals2(sub, PADDING);
    }

    @Test
    public void testSubSequencePaddingOnly2() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(5, 8);
        assertNotEquals(RightPaddedCharSequence2.class, sub.getClass(), "class");
        this.checkEquals2(sub,
                PADDING,
                PADDING,
                PADDING);
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        this.checkSubSequence(0, SEQUENCE_LENGTH, "abcde");
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 4);
        this.checkEquals2(sub, "bcd");
    }

    @Test
    public void testSubSequenceWithLessPadding() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(0, 7);
        this.checkEquals2(sub, "abcde..");
    }

    @Test
    public void testSubSequenceWithLessPadding2() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 6);
        this.checkEquals2(sub, "bcde.");
    }

    @Test
    public void testSubSequenceTwice() {
        final RightPaddedCharSequence2 sequence = this.createCharSequence();
        final CharSequence sub = sequence.subSequence(1, 7).subSequence(1, 5);
        this.checkEquals2(sub, "cde.");
    }

    @Test
    public void testToString() {
        assertEquals("abcde...", this.createCharSequence().toString());
    }

    @Override
    protected RightPaddedCharSequence2 createCharSequence() {
        return RightPaddedCharSequence2.wrap(SEQUENCE,
                SEQUENCE_OFFSET,
                SEQUENCE_LENGTH,
                PADDING_LENGTH,
                PADDING);
    }

    @Override
    public Class<RightPaddedCharSequence2> type() {
        return RightPaddedCharSequence2.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public RightPaddedCharSequence2 serializableInstance() {
        return RightPaddedCharSequence2.wrap("123abcde456", 3, 5, 3, 'X'); // abcdeXXX
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
