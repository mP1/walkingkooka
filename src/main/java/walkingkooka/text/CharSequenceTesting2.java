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
public interface CharSequenceTesting2<C extends CharSequence> extends CharSequenceTesting,
    HashCodeEqualsDefinedTesting2<C>,
    ToStringTesting<C>,
    TypeNameTesting<C> {

    @Test
    default void testLengthAndToStringCompatible() {
        final C sequence = this.createCharSequence();
        this.checkEquals(
            sequence.length(),
            sequence.toString().length(),
            () -> sequence + " length is different from that of toString()"
        );
    }

    @Test
    default void testCharAtAndToStringCompatible() {
        final C sequence = this.createCharSequence();
        final int length = sequence.length();
        final char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = sequence.charAt(i);
        }

        this.checkEquals(
            new String(chars),
            sequence.toString()
        );
    }

    @Test
    default void testCharAtWithNegativeIndexFails() {
        this.charAtFails(-1);
    }

    @Test
    default void testCharAtWithInvalidIndexFails() {
        this.charAtFails(Integer.MAX_VALUE);
    }

    default void charAtFails(final int index) {
        this.charAtFails(this.createCharSequence(), index);
    }

    default void charAtFails(final CharSequence sequence,
                             final int index) {
        assertThrows(
            Exception.class,
            () -> this.createCharSequence()
                .charAt(index)
        );
    }

    @Test
    default void testSubSequenceNegativeFromIndexFails() {
        this.subSequenceFails(-1, 0);
    }

    @Test
    default void testSubSequenceInvalidFromIndexFails() {
        final C sequence = this.createCharSequence();
        final int from = sequence.length();
        this.subSequenceFails(
            sequence,
            from + 1,
            from
        );
    }

    @Test
    default void testtestSubSequenceNegativeToFails() {
        this.subSequenceFails(0, -1);
    }

    @Test //
    default void testSubSequenceFromAfterToFails() {
        this.subSequenceFails(1, 0);
    }

    @Test //
    default void testSubsequenceInvalidToIndexFails() {
        final C sequence = this.createCharSequence();
        this.subSequenceFails(
            sequence,
            0,
            Integer.MAX_VALUE
        );
    }

    default void subSequenceFails(final int from,
                                  final int to) {
        this.subSequenceFails(
            this.createCharSequence(),
            from,
            to
        );
    }

    @Test
    default void testSubSequenceWithSameFromAndToReturnsThis() {
        final C sequence = this.createCharSequence();
        assertSame(
            sequence,
            sequence.subSequence(
                0,
                sequence.length()
            )
        );
    }

    @Test
    default void testSubSequenceEmpty() {
        final C sequence = this.createCharSequence();
        this.checkEquals2(
            sequence.subSequence(0, 0),
            ""
        );
    }

    @Test
    default void testSubSequenceEmpty2() {
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

    default void lengthAndCheck(final int length) {
        this.lengthAndCheck(
            this.createCharSequence(),
            length
        );
    }

    default void charAtAndCheck(final char... c) {
        this.charAtAndCheck(
            this.createCharSequence(),
            c
        );
    }

    default String toString(final char c) {
        return CharSequences.escape(Character.toString(c)).toString();
    }

    // class............................................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return CharSequence.class.getSimpleName();
    }
}
