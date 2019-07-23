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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Helper for testing a parse string method, which may be either a static or instance method.
 */
public interface ParseStringTesting<T> extends Testing {

    @Test
    default void testParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.parse(null);
        });
    }

    @Test
    default void testParseEmptyFails() {
        assertThrows(this.parseFailedExpected(IllegalArgumentException.class), () -> {
            this.parse("");
        });
    }

    /**
     * Implementing tests should call the static parse method.
     */
    T parse(String text);

    default T parseAndCheck(final String text, final T value) {
        final T parsed = this.parse(text);
        assertEquals(value,
                parsed,
                () -> "Parsing of " + CharSequences.quoteAndEscape(text) + " failed"
        );
        return parsed;
    }

    default void parseInvalidCharacterFails(final String text) {
        this.parseInvalidCharacterFails(text, text.length() - 1);
    }

    default void parseInvalidCharacterFails(final String text, final char c) {
        this.parseInvalidCharacterFails(text, text.indexOf(c));
    }

    default void parseInvalidCharacterFails(final String text, final int pos) {
        this.parseFails(text, this.parseFailedExpected(new InvalidCharacterException(text, pos)));
    }

    /**
     * Parsers the given text and verifies the expected {@link RuntimeException} was thrown
     */
    default void parseFails(final String text, final Class<? extends RuntimeException> expected) {

        final Class<? extends RuntimeException> expected2 = this.parseFailedExpected(expected);
        assertThrows(expected2, () -> {
            this.parse(text);
        });
    }

    /**
     * Some APIs wrap the original exception inside another.
     */
    Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected);

    /**
     * Parsers the given text and verifies the expected {@link RuntimeException} was thrown and messages match.
     */
    default void parseFails(final String text, final RuntimeException expected) {
        final RuntimeException expected2 = this.parseFailedExpected(expected);
        final RuntimeException thrown = assertThrows(expected.getClass(), () -> {
            this.parse(text);
        });
        assertEquals(expected2.getMessage(),
                thrown.getMessage(),
                () -> "Incorrect failure message for " + CharSequences.quoteAndEscape(text));
    }

    /**
     * Some APIs wrap the original exception inside another.
     */
    RuntimeException parseFailedExpected(final RuntimeException expected);
}
