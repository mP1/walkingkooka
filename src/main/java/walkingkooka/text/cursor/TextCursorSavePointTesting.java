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


/**
 * Mixin that introduces tests and helpers to test {@link TextCursorSavePoint} implementations.
 */
public interface TextCursorSavePointTesting<S extends TextCursorSavePoint, C extends TextCursor>
    extends TextCursorTesting {

    // tests

    @Test
    default void testSave() {
        final C cursor = this.createTextCursor("0123456");
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '2');

        this.checkNotEquals(
            null,
            cursor.save(),
            "SavePointTestCase"
        );
    }

    @Test
    default void testSaveAndRestore() {
        final C cursor = this.createTextCursor("0123456");
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '2');

        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '4');

        save.restore();
        this.atAndCheck(cursor, '2');
    }

    @Test
    default void testMultipleRestores() {
        final C cursor = this.createTextCursor("0123456");
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '2');

        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '4');

        save.restore();
        this.atAndCheck(cursor, '2');

        cursor.next();
        cursor.next();
        save.restore();
        this.atAndCheck(cursor, '2');
    }

    @Test
    default void testMultipleSaves() {
        final C cursor = this.createTextCursor("0123456");
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '2');

        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        this.atAndCheck(cursor, '3');

        save.save();
        cursor.next();
        this.atAndCheck(cursor, '4');

        save.restore();
        this.atAndCheck(cursor, '3');
    }

    @Test
    default void testTextBetweenCursorBefore() {
        final C cursor = this.createTextCursor("0123456");
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '2');

        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        cursor.next();

        final TextCursorSavePoint save2 = cursor.save();
        cursor.next();
        cursor.next();
        save.restore();

        this.checkTextBetween(save2, "23");
    }

    @Test
    default void testTextBetweenCursorSame() {
        final C cursor = this.createTextCursor("0123456");
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '2');

        this.checkTextBetween(cursor.save(), "");
    }

    @Test
    default void testTextBetweenCursorAfter() {
        final C cursor = this.createTextCursor("0123456");
        cursor.next();
        cursor.next();
        this.atAndCheck(cursor, '2');

        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        cursor.next();
        this.checkTextBetween(save, "23");
    }

    @Test
    default void testToString() {
        final C cursor = this.createTextCursor("0123456789");
        cursor.next();
        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        cursor.next();

        final String actual = save.toString();
        save.restore();
        this.checkEquals("save " + cursor, actual);
    }

    C createTextCursor(String text);

    default void checkTextBetween(final TextCursorSavePoint save, final String expected) {
        final String actual = save.textBetween().toString();
        this.checkEquals(expected, actual, () -> "textBetween=" + save);
    }
}
