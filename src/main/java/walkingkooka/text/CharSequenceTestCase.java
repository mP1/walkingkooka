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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Base class for testing any {@link CharSequence} with most tests testing parameter validation.
 */
abstract public class CharSequenceTestCase<C extends CharSequence & HashCodeEqualsDefined> extends ClassTestCase<C>
        implements HashCodeEqualsDefinedTesting<C> {

    protected CharSequenceTestCase() {
        super();
    }

    @Test
    final public void testLengthAndToStringCompatible() {
        final C sequence = this.createCharSequence();
        assertEquals(sequence + " length is different from that of toString()",
                sequence.length(),
                sequence.toString().length());
    }

    @Test
    final public void testCharAtAndToStringCompatible() {
        final C sequence = this.createCharSequence();
        final int length = sequence.length();
        final char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = sequence.charAt(i);
        }

        assertEquals(new String(chars), sequence.toString());
    }

    @Test
    final public void testNegativeIndexFails() {
        this.charAtFails(-1);
    }

    @Test
    final public void testInvalidIndexFails() {
        final C sequence = this.createCharSequence();
        this.charAtFails(sequence, Integer.MAX_VALUE);
    }

    final protected void charAtFails(final int index) {
        this.charAtFails(this.createCharSequence(), index);
    }

    final protected void charAtFails(final CharSequence sequence, final int index) {
        try {
            sequence.charAt(index);
            Assert.fail();
        } catch (final StringIndexOutOfBoundsException ignored) {
        }
    }

    @Test final public void testNegativeSubSequenceFromIndexFails() {
        this.subSequenceFails(-1, 0);
    }

    @Test final public void testInvalidSubSequenceFromIndexFails() {
        final C sequence = this.createCharSequence();
        final int from = sequence.length();
        this.subSequenceFails(sequence, from + 1, from);
    }

    @Test final public void testNegativeSubSequenceToFails() {
        this.subSequenceFails(0, -1);
    }

    @Test final public void testSubSequenceFromAfterToFails() {
        this.subSequenceFails(1, 0);
    }

    @Test final public void testSubsequenceInvalidToIndexFails() {
        final C sequence = this.createCharSequence();
        this.subSequenceFails(sequence, 0, Integer.MAX_VALUE);
    }

    final protected void subSequenceFails(final int from, final int to) {
        this.subSequenceFails(this.createCharSequence(), from, to);
    }

    final protected void subSequenceFails(final C sequence, final int from, final int to) {
        try {
            sequence.subSequence(from, to);
            Assert.fail();
        } catch (final StringIndexOutOfBoundsException ignored) {
        } catch (final IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testSubSequenceWithSameFromAndToReturnsThis() {
        final C sequence = this.createCharSequence();
        assertSame(sequence, sequence.subSequence(0, sequence.length()));
    }

    @Test
    public void testEmptySubSequence() {
        final C sequence = this.createCharSequence();
        this.checkEquals2(sequence.subSequence(0, 0), "");
    }

    @Test
    public void testEmptySubSequence2() {
        final C sequence = this.createCharSequence();

        final int length = sequence.length();
        assertTrue("sequence length must be greater than equal to 1=" + CharSequences.quote(
                sequence.toString()), length >= 1);
        this.checkEquals2(sequence.subSequence(length - 1, length - 1), "");
    }

    @Test
    public void testToStringCached() {
        final C sequence = this.createCharSequence();
        assertSame(sequence.toString(), sequence.toString());
    }

    abstract protected C createCharSequence();

    @Override
    public final C createObject() {
        return this.createCharSequence();
    }

    protected void checkEquals2(final CharSequence actual, final String expected) {
        this.checkEquals2(actual, expected.toCharArray());
    }

    protected void checkEquals2(final CharSequence actual, final char... c) {
        this.checkLength(actual, c.length);
        this.checkCharAt(actual, c);
    }

    protected void checkLength(final int length) {
        this.checkLength(this.createCharSequence(), length);
    }

    protected void checkLength(final CharSequence chars, final int length) {
        assertEquals("length of " + chars, length, chars.length());
    }

    protected void checkLength(final String message, final CharSequence chars, final int length) {
        assertEquals(message, length, chars.length());
    }

    protected void checkCharAt(final String c) {
        this.checkCharAt(c.toCharArray());
    }

    protected void checkCharAt(final char... c) {
        this.checkCharAt(0, c);
    }

    protected void checkCharAt(final int index, final char... c) {
        this.checkCharAt(this.createCharSequence(), index, c);
    }

    protected void checkCharAt(final CharSequence chars, final char... c) {
        this.checkCharAt(chars, 0, c);
    }

    protected void checkCharAt(final CharSequence chars, final String c) {
        this.checkCharAt(chars, c.toCharArray());
    }

    protected void checkCharAt(final CharSequence chars, final int index, final char... c) {
        final int length = c.length;
        for (int i = 0; i < length; i++) {
            this.checkCharAt(chars, index + i, c[i]);
        }
    }

    private void checkCharAt(final CharSequence chars, final int index, final char c) {
        final char d = chars.charAt(index);
        if (c != d) {
            assertEquals("Wrong char at " + index + " in " + chars,
                    CharSequenceTestCase.toString(c),
                    CharSequenceTestCase.toString(chars.charAt(index)));
        }
    }

    private static String toString(final char c) {
        return CharSequences.escape(Character.toString(c)).toString();
    }

    protected void checkSubSequence(final int start, final int end, final String expected) {
        this.checkSubSequence(this.createCharSequence(), start, end, expected);
    }

    protected void checkSubSequence(final CharSequence chars, final int start, final int end,
                                    final String expected) {
        final CharSequence sub = chars.subSequence(start, end);
        this.checkLength(sub, end - start);
        this.checkCharAt(sub, expected);
    }
}
