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

package walkingkooka.text.cursor;

import walkingkooka.test.Testing;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin that is useful for testing several interfaces in the text.cursor package.
 */
public interface TextCursorTesting extends Testing {

    default void moveNextFails(final TextCursor cursor) {
        assertThrows(TextCursorException.class, cursor::next);
    }

    default void atAndCheck(final TextCursor cursor, final char expected) {
        this.atAndCheck(cursor, expected, null);
    }

    default void atAndCheck(final TextCursor cursor, final char expected, final String message) {
        Objects.requireNonNull(cursor, "cursor");

        this.checkEquals(
            expected,
            cursor.at(),
            message + "=" + cursor
        );
    }

    default void atAndCheck(final char at, final char expected, final TextCursor cursor) {
        this.checkEquals(
            expected,
            at,
            () -> "Wrong character " + cursor);
    }

    default void atAndCheck(final char at, final char expected, final String message) {
        this.checkEquals(
            expected,
            at,
            message
        );
    }

    default void atFails(final TextCursor cursor) {
        assertThrows(TextCursorException.class, cursor::at);
    }
}
