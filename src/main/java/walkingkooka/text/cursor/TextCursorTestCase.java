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

package walkingkooka.text.cursor;

import org.junit.Test;
import walkingkooka.test.TestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

abstract public class TextCursorTestCase<C extends TextCursor> extends TextCursorPackageTestCase<C> {

    protected TextCursorTestCase() {
        super();
    }

    // tests

    @Test
    public void testNaming() {
        this.checkNaming(TextCursor.class);
    }

    @Test
    public void testIsEmptyNotEmpty() {
        final C cursor = this.createTextCursor("1");
        this.checkNotEmpty(cursor);
    }

    @Test
    public void testIsEmpty() {
        final C cursor = this.createTextCursor("");
        this.checkEmpty(cursor);
    }

    @Test
    public void testIsEmptyTwice() {
        final C cursor = this.createTextCursor("");
        this.checkEmpty(cursor);
        this.checkEmpty(cursor);
    }

    @Test
    public void testIsEmptyAfterNextAndEmpty() {
        final C cursor = this.createTextCursor("1");
        cursor.next();
        this.checkEmpty(cursor, "cursor should be empty");
    }

    @Test
    public void testNext() {
        final C cursor = this.createTextCursor("123");
        TestCase.assertSame("cursor didnt return this", cursor, cursor.next());
    }

    @Test
    public void testNextWhenEmptyFails() {
        final C cursor = this.createTextCursor("1");
        cursor.next(); // 1
        this.moveNextFails(cursor, "empty cursor.next should have failed");
    }

    @Test
    public void testAtWhenEmptyFails() {
        final C cursor = this.createTextCursor("123");
        this.consumeAndCheck(cursor, "123");
        this.atFails(cursor, "empty cursor.at should have failed");
    }

    @Test
    public void testAt() {
        final C cursor = this.createTextCursor("123");
        this.atAndCheck(cursor, '1');
        cursor.next();
        this.atAndCheck(cursor, '2');
        cursor.next();
        this.atAndCheck(cursor, '3');
    }

    @Test
    public void testAtTwice() {
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
    public void testAtAfterNext() {
        final C cursor = this.createTextCursor("123");
        cursor.next();
        this.atAndCheck(cursor, '2');
    }

    @Test
    public void testNextTooManyFails() {
        final C cursor = this.createTextCursor("123");
        cursor.next();
        this.atAndCheck(cursor, '2');
        cursor.next();
        this.atAndCheck(cursor, '3');
        cursor.next();

        this.moveNextFails(cursor);
    }

    @Test
    public void testEndAlreadyEmpty() {
        final TextCursor cursor = this.createTextCursor("1");
        cursor.next();
        cursor.end();
        this.checkEmpty(cursor);
    }

    @Test
    public void testEnd() {
        final TextCursor cursor = this.createTextCursor("1234567890");
        TestCase.assertSame("cursor didnt return this", cursor, cursor.end());
        this.checkEmpty(cursor);
    }

    @Test
    public void testLineInfo() {
        final C cursor = this.createTextCursor("text\nnext");
        cursor.next();

        final TextCursorLineInfo info = cursor.lineInfo();
        assertEquals("text", "text", info.text());
        assertEquals("lineNumber", 1, info.lineNumber());
        assertEquals("column", 2, info.column());
    }

    @Test
    public void testSaveAndRestore() {
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
    public void testSavePointUpdated() {
        final C cursor = this.createTextCursor("text");
        cursor.next();
        final TextCursorSavePoint saved = cursor.save();
        cursor.next();
        saved.save();
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    // factory

    protected C createTextCursor() {
        return this.createTextCursor("123");
    }

    abstract protected C createTextCursor(String text);

    protected void consumeAndCheck(final TextCursor cursor, final String text) {
        for (char c : text.toCharArray()) {
            this.atAndCheck(cursor, c, "Expected '" + c + "' while consuming " + CharSequences.quote(text));
            cursor.next();
        }
    }

    protected void checkLineInfo(final TextCursor cursor, final String text, final int lineNumber, final String columnNumber) {
        this.checkLineInfo(cursor, text, lineNumber, columnNumber.length());
    }

    protected void checkLineInfo(final TextCursor cursor, final String text, final int lineNumber, final int columnNumber) {
        final TextCursorLineInfo info = cursor.lineInfo();

        assertEquals("text", text, info.text());
        assertEquals("lineNumber", lineNumber, info.lineNumber());
        assertEquals("column", columnNumber, info.column());
    }
}
