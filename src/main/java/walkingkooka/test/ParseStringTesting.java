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

package walkingkooka.test;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.text.CharSequences;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Helper for testing a parse string method, which may be either a static or instance method.
 */
public interface ParseStringTesting<T> extends Testing {

    @Test
    default void testParseStringNullFails() {
        assertThrows(NullPointerException.class, () -> this.parseString(null));
    }

    @Test
    default void testParseStringEmptyFails() {
        assertThrows(this.parseStringFailedExpected(IllegalArgumentException.class), () -> this.parseString(""));
    }

    /**
     * Implementing tests should call the static parse method.
     */
    T parseString(final String text);

    default T parseStringAndCheck(final String text,
                                  final T value) {
        return this.parseStringAndCheck(
            this::parseString,
            text,
            value
        );
    }

    default <TT> TT parseStringAndCheck(final Function<String, TT> parser,
                                        final String text,
                                        final TT value) {
        final TT parsed = parser.apply(text);
        this.checkEquals(
            value,
            parsed,
            () -> "Parsing of " + CharSequences.quoteAndEscape(text) + " failed"
        );
        return parsed;
    }

    default void parseStringInvalidCharacterFails(final String text) {
        this.parseStringInvalidCharacterFails(text, text.length() - 1);
    }

    default void parseStringInvalidCharacterFails(final String text,
                                                  final char c) {
        this.parseStringInvalidCharacterFails(text, text.indexOf(c));
    }

    default void parseStringInvalidCharacterFails(final String text,
                                                  final int pos) {
        this.parseStringFails(text, this.parseStringFailedExpected(new InvalidCharacterException(text, pos)));
    }

    /**
     * Parsers the given text and verifies the expected {@link RuntimeException} was thrown
     */
    default void parseStringFails(final String text,
                                  final Class<? extends RuntimeException> expected) {

        final Class<? extends RuntimeException> expected2 = this.parseStringFailedExpected(expected);
        assertThrows(expected2, () -> this.parseString(text));
    }

    /**
     * Some APIs wrap the original exception inside another.
     */
    Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected);

    /**
     * Parsers the given text and verifies the expected {@link RuntimeException} was thrown and messages match.
     */
    default void parseStringFails(final String text,
                                  final RuntimeException expected) {
        final RuntimeException expected2 = this.parseStringFailedExpected(expected);
        final RuntimeException thrown = assertThrows(expected.getClass(), () -> this.parseString(text));
        this.checkEquals(
            expected2.getMessage(),
            thrown.getMessage(),
            () -> "Incorrect failure message for " + CharSequences.quoteAndEscape(text)
        );
    }

    /**
     * Some APIs wrap the original exception inside another.
     */
    RuntimeException parseStringFailedExpected(final RuntimeException expected);
}
