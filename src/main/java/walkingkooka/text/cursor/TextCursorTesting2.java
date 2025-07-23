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

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasTextTesting;

import static org.junit.jupiter.api.Assertions.assertSame;

public interface TextCursorTesting2<C extends TextCursor>
    extends TextCursorTesting,
    CanBeEmptyTesting,
    HasTextTesting,
    TypeNameTesting<C> {

    @Test
    default void testIsEmptyNotEmpty() {
        final C cursor = this.createTextCursor("1");
        this.isEmptyAndCheck(
            cursor,
            false
        );
    }

    @Test
    default void testIsEmpty() {
        final C cursor = this.createTextCursor("");
        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    @Test
    default void testIsEmptyTwice() {
        final C cursor = this.createTextCursor("");
        this.isEmptyAndCheck(
            cursor,
            true
        );
        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    @Test
    default void testIsEmptyAfterNextAndEmpty() {
        final C cursor = this.createTextCursor("1");
        cursor.next();
        this.isEmptyAndCheck(
            cursor,
            true,
            "cursor should be empty"
        );
    }

    @Test
    default void testNext() {
        final C cursor = this.createTextCursor("123");
        assertSame(cursor, cursor.next(), "cursor didnt return this");
    }

    @Test
    default void testNextWhenEmptyFails() {
        final C cursor = this.createTextCursor("1");
        cursor.next(); // 1
        this.moveNextFails(cursor);
    }

    @Test
    default void testAtWhenEmptyFails() {
        final C cursor = this.createTextCursor("123");
        this.consumeAndCheck(cursor, "123");
        this.atFails(cursor);
    }

    @Test
    default void testAt() {
        final C cursor = this.createTextCursor("123");
        this.atAndCheck(cursor, '1');
        cursor.next();
        this.atAndCheck(cursor, '2');
        cursor.next();
        this.atAndCheck(cursor, '3');
    }

    @Test
    default void testAtTwice() {
        final C cursor = this.createTextCursor("123");
        this.atAndCheck(cursor, '1');
        this.atAndCheck(cursor, '1');

        cursor.next();
        this.atAndCheck(cursor, '2');
        this.atAndCheck(cursor, '2');

        cursor.next();
        this.atAndCheck(cursor, '3');
        this.atAndCheck(cursor, '3');
    }

    @Test
    default void testAtAfterNext() {
        final C cursor = this.createTextCursor("123");
        cursor.next();
        this.atAndCheck(cursor, '2');
    }

    @Test
    default void testNextTooManyFails() {
        final C cursor = this.createTextCursor("123");
        cursor.next();
        this.atAndCheck(cursor, '2');
        cursor.next();
        this.atAndCheck(cursor, '3');
        cursor.next();

        this.moveNextFails(cursor);
    }

    @Test
    default void testEndAlreadyEmpty() {
        final TextCursor cursor = this.createTextCursor("1");
        cursor.next();
        cursor.end();
        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    @Test
    default void testEnd() {
        final TextCursor cursor = this.createTextCursor("1234567890");
        assertSame(cursor, cursor.end(), "cursor didnt return this");
        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    @Test
    default void testLineInfo() {
        final C cursor = this.createTextCursor("text\nnext");
        cursor.next();

        this.lineInfoCheck(cursor, "text", 1, 2);
    }

    @Test
    default void testSaveAndRestore() {
        final C cursor = this.createTextCursor("1234");

        final TextCursorSavePoint saved1 = cursor.save();
        cursor.next(); // 2

        final TextCursorSavePoint saved2 = cursor.save();
        cursor.next(); // 3
        saved2.restore();
        this.atAndCheck(cursor, '2', "cursor was not restored correctly");

        saved1.restore();
        this.atAndCheck(cursor, '1', "cursor was not restored correctly");
    }

    @Test
    default void testSavePointUpdated() {
        final C cursor = this.createTextCursor("text");
        cursor.next();
        final TextCursorSavePoint saved = cursor.save();
        cursor.next();
        saved.save();
    }

    // factory

    default C createTextCursor() {
        return this.createTextCursor("123");
    }

    C createTextCursor(String text);

    default void consumeAndCheck(final TextCursor cursor, final String text) {
        for (char c : text.toCharArray()) {
            this.atAndCheck(cursor, c, "Expected '" + c + "' while consuming " + CharSequences.quote(text));
            cursor.next();
        }
    }

    default void lineInfoCheck(final TextCursor cursor,
                               final String text,
                               final int lineNumber,
                               final String columnNumber) {
        this.lineInfoCheck(cursor, text, lineNumber, columnNumber.length());
    }

    default void lineInfoCheck(final TextCursor cursor,
                               final String text,
                               final int lineNumber,
                               final int columnNumber) {
        final TextCursorLineInfo info = cursor.lineInfo();

        this.checkEquals(text, info.text(), "text");
        this.checkEquals(lineNumber, info.lineNumber(), "lineNumber");
        this.checkEquals(columnNumber, info.column(), "column");
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return TextCursor.class.getSimpleName();
    }
}
