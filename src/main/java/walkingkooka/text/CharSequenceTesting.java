/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing any {@link CharSequence} with most tests testing parameter validation.
 */
public interface CharSequenceTesting<C extends CharSequence> extends HashCodeEqualsDefinedTesting2<C>,
    ToStringTesting<C>,
    TypeNameTesting<C> {

    @Test
    default void testLengthAndToStringCompatible() {
        final C sequence = this.createCharSequence();
        this.checkEquals(sequence.length(),
            sequence.toString().length(),
            () -> sequence + " length is different from that of toString()");
    }

    @Test
    default void testCharAtAndToStringCompatible() {
        final C sequence = this.createCharSequence();
        final int length = sequence.length();
        final char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = sequence.charAt(i);
        }

        this.checkEquals(new String(chars), sequence.toString());
    }

    @Test
    default void testNegativeIndexFails() {
        this.charAtFails(-1);
    }

    @Test
    default void testInvalidIndexFails() {
        this.charAtFails(Integer.MAX_VALUE);
    }

    default void charAtFails(final int index) {
        this.charAtFails(this.createCharSequence(), index);
    }

    default void charAtFails(final CharSequence sequence, final int index) {
        assertThrows(Exception.class, () -> this.createCharSequence().charAt(index));
    }

    @Test
    default void testNegativeSubSequenceFromIndexFails() {
        this.subSequenceFails(-1, 0);
    }

    @Test
    default void testInvalidSubSequenceFromIndexFails() {
        final C sequence = this.createCharSequence();
        final int from = sequence.length();
        this.subSequenceFails(sequence, from + 1, from);
    }

    @Test
    default void testNegativeSubSequenceToFails() {
        this.subSequenceFails(0, -1);
    }

    @Test default void testSubSequenceFromAfterToFails() {
        this.subSequenceFails(1, 0);
    }

    @Test default void testSubsequenceInvalidToIndexFails() {
        final C sequence = this.createCharSequence();
        this.subSequenceFails(sequence, 0, Integer.MAX_VALUE);
    }

    default void subSequenceFails(final int from, final int to) {
        this.subSequenceFails(this.createCharSequence(), from, to);
    }

    default void subSequenceFails(final C sequence, final int from, final int to) {
        assertThrows(StringIndexOutOfBoundsException.class, () -> sequence.subSequence(from, to));
    }

    @Test
    default void testSubSequenceWithSameFromAndToReturnsThis() {
        final C sequence = this.createCharSequence();
        assertSame(sequence, sequence.subSequence(0, sequence.length()));
    }

    @Test
    default void testEmptySubSequence() {
        final C sequence = this.createCharSequence();
        this.checkEquals2(sequence.subSequence(0, 0), "");
    }

    @Test
    default void testEmptySubSequence2() {
        final C sequence = this.createCharSequence();

        final int length = sequence.length();
        this.checkEquals(
            true,
            length >= 1,
            () -> "sequence length must be greater than equal to 1=" + CharSequences.quote(sequence.toString())
        );
        this.checkEquals2(sequence.subSequence(length - 1, length - 1), "");
    }

    @Test
    default void testToStringCached() {
        final C sequence = this.createCharSequence();
        assertSame(sequence.toString(), sequence.toString());
    }

    /**
     * Creates or returns the "default" {@link CharSequence} being tested. Many test helper overloads assume this instance.
     */
    C createCharSequence();

    default void checkEquals2(final CharSequence actual, final String expected) {
        this.checkEquals2(actual, expected.toCharArray());
    }

    default void checkEquals2(final CharSequence actual, final char... c) {
        this.checkLength(actual, c.length);
        this.checkCharAt(actual, c);
        this.checkEquals(new String(c), actual.toString(), "toString");
    }

    default void checkLength(final int length) {
        this.checkLength(this.createCharSequence(), length);
    }

    default void checkLength(final CharSequence chars, final int length) {
        this.checkEquals(length, chars.length(), () -> "length of " + chars);
    }

    default void checkLength(final String message, final CharSequence chars, final int length) {
        this.checkEquals(length, chars.length(), message);
    }

    default void checkCharAt(final String c) {
        this.checkCharAt(c.toCharArray());
    }

    default void checkCharAt(final char... c) {
        this.checkCharAt(0, c);
    }

    default void checkCharAt(final int index, final char... c) {
        this.checkCharAt(this.createCharSequence(), index, c);
    }

    default void checkCharAt(final CharSequence chars, final char... c) {
        this.checkCharAt(chars, 0, c);
    }

    default void checkCharAt(final CharSequence chars, final String c) {
        this.checkCharAt(chars, c.toCharArray());
    }

    default void checkCharAt(final CharSequence chars, final int index, final char... c) {
        final int length = c.length;
        for (int i = 0; i < length; i++) {
            this.checkCharAt(chars, index + i, c[i]);
        }
    }

    default void checkCharAt(final CharSequence chars, final int index, final char c) {
        final char d = chars.charAt(index);
        if (c != d) {
            this.checkEquals(CharSequences.quoteAndEscape(c),
                CharSequences.quoteAndEscape(chars.charAt(index)),
                "Wrong char at " + index + " in " + chars);
        }
    }

    default String toString(final char c) {
        return CharSequences.escape(Character.toString(c)).toString();
    }

    default void checkSubSequence(final int start, final int end, final String expected) {
        this.checkSubSequence(this.createCharSequence(), start, end, expected);
    }

    default void checkSubSequence(final CharSequence chars, final int start, final int end,
                                  final String expected) {
        final CharSequence sub = chars.subSequence(start, end);
        this.checkLength(sub, end - start);
        this.checkCharAt(sub, expected);
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return CharSequence.class.getSimpleName();
    }
}
