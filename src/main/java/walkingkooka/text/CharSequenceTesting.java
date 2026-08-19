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

import walkingkooka.test.Testing;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing any {@link CharSequence} with most tests testing parameter validation.
 */
public interface CharSequenceTesting extends Testing {


    default void subSequenceFails(final CharSequence sequence,
                                  final int from,
                                  final int to) {
        assertThrows(
            StringIndexOutOfBoundsException.class,
            () -> sequence.subSequence(
                from,
                to
            )
        );
    }

    default void checkEquals2(final CharSequence actual,
                              final String expected) {
        this.checkEquals2(
            actual,
            expected.toCharArray()
        );
    }

    default void checkEquals2(final CharSequence actual,
                              final char... c) {
        this.lengthAndCheck(actual, c.length);
        this.charAtAndCheck(actual, c);
        this.checkEquals(
            new String(c),
            actual.toString(),
            "toString"
        );
    }

    default void lengthAndCheck(final CharSequence chars,
                                final int length) {
        this.checkEquals(
            length,
            chars.length(),
            () -> "length of " + chars
        );
    }

    default void lengthAndCheck(final String message,
                                final CharSequence chars,
                                final int length) {
        this.checkEquals(
            length,
            chars.length(),
            message
        );
    }

    default void charAtAndCheck(final CharSequence chars,
                                final char... c) {
        this.charAtAndCheck(
            chars,
            0,
            c
        );
    }

    default void charAtAndCheck(final CharSequence chars,
                                final String c) {
        this.charAtAndCheck(
            chars,
            c.toCharArray()
        );
    }

    default void charAtAndCheck(final CharSequence chars,
                                final int index,
                                final char... c) {
        final int length = c.length;
        for (int i = 0; i < length; i++) {
            this.charAtAndCheck(
                chars,
                index + i,
                c[i]
            );
        }
    }

    default void charAtAndCheck(final CharSequence chars,
                                final int index,
                                final char c) {
        final char d = chars.charAt(index);
        if (c != d) {
            this.checkEquals(
                CharSequences.quoteAndEscape(c),
                CharSequences.quoteAndEscape(chars.charAt(index)),
                "Wrong char at " + index + " in " + chars
            );
        }
    }

    default String toString(final char c) {
        return CharSequences.escape(Character.toString(c)).toString();
    }

    default void subSequenceAndCheck(final CharSequence chars,
                                     final int start,
                                     final int end,
                                     final String expected) {
        final CharSequence sub = chars.subSequence(start, end);

        this.lengthAndCheck(
            sub,
            end - start
        );
        this.charAtAndCheck(
            sub,
            expected
        );
    }
}
