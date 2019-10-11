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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin that is useful for testing several interfaces in the text.cursor package.
 */
public interface TextCursorTesting<T> {

    default void checkEmpty(final TextCursor cursor) {
        this.checkEmpty(cursor, "Cursor not empty");
    }

    default void checkEmpty(final TextCursor cursor, final String message) {
        assertEquals(true,
                cursor.isEmpty(),
                () -> message + '=' + cursor);
    }

    default void checkNotEmpty(final TextCursor cursor) {
        this.checkNotEmpty(cursor, "cursor not empty " + cursor);
    }

    default void checkNotEmpty(final TextCursor cursor, final String message) {
        assertEquals(false,
                cursor.isEmpty(),
                () -> "cursor not empty " + cursor);
    }

    default void moveNextFails(final TextCursor cursor) {
        assertThrows(TextCursorException.class, cursor::next);
    }

    default void atAndCheck(final TextCursor cursor, final char expected) {
        this.atAndCheck(cursor, expected, null);
    }

    default void atAndCheck(final TextCursor cursor, final char expected, final String message) {
        Objects.requireNonNull(cursor, "cursor");

        assertEquals(expected,
                cursor.at(),
                message + "=" + cursor);
    }

    default void atAndCheck(final char at, final char expected, final TextCursor cursor) {
        assertEquals(expected,
                at,
                () -> "Wrong character " + cursor);
    }

    default void atAndCheck(final char at, final char expected, final String message) {
        assertEquals(expected, at, message);
    }

    default void atFails(final TextCursor cursor) {
        assertThrows(TextCursorException.class, cursor::at);
    }
}
