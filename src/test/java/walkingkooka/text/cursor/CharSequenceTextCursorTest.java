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

final public class CharSequenceTextCursorTest extends TextCursorTestCase<CharSequenceTextCursor> {

    @Test(expected = NullPointerException.class)
    public void testFromNullFails() {
        CharSequenceTextCursor.with(null);
    }

    @Test
    public void testFrom() {
        final String text = "text";
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with(text);
        assertEquals("positon", 0, cursor.position);
        TestCase.assertEquals("text", text, cursor.text);
    }

    @Test
    public void testAtWhenEmpty() {
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with("");
        this.atFails(cursor, CharSequenceTextCursor.cursorIsEmpty(0, 0));
    }

    @Test
    public void testAtWhenEmpty2() {
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with("123");
        cursor.next();
        cursor.next();
        cursor.next();
        this.atFails(cursor, CharSequenceTextCursor.cursorIsEmpty(3, 3));
    }

    @Override
    public void testLineInfo() {
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with("text\nnext");
        cursor.next();
        this.checkLineInfo(cursor, "text", 1, 2);
    }


    @Test
    public void testToStringAtBeginning() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.checkToStringContains(cursor, "at 0 \"[a]bcdefgh\"");
    }

    @Test
    public void testToStringNearBeginning() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        cursor.next();
        this.checkToStringContains(cursor, "at 1 \"a[b]cdefghi\"");
    }

    @Test
    public void testToStringNearBeginning2() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 2);
        this.checkToStringContains(cursor, "at 2 \"ab[c]defghij\"");
    }

    @Test
    public void testToStringMiddle() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 13);
        this.checkToStringContains(cursor, "at 13 \"ghijklm[n]opqrstu\"");
    }

    @Test
    public void testToSteringAtEnd() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 25);
        this.checkToStringContains(cursor, "at 25 \"stuvwxy[z]\"");
    }

    @Test
    public void testToStringAfterEnd() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        cursor.end();
        this.checkToStringContains(cursor, "at 26 \"tuvwxyz\"[]");
    }

    @Test
    public void testToStringNearEnd() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 24);
        this.checkToStringContains(cursor, "at 24 \"rstuvwx[y]z\"");
    }

    @Test
    public void testToStringNearEnd2() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 23);
        this.checkToStringContains(cursor, "at 23 \"qrstuvw[x]yz\"");
    }

    @Override
    protected CharSequenceTextCursor createTextCursor(final String text) {
        return CharSequenceTextCursor.with(text);
    }

    private void moveBy(final TextCursor cursor, final int skip) {
        for (int i = 0; i < skip; i++) {
            cursor.next();
        }
    }

    private void checkToStringContains(final CharSequenceTextCursor cursor, final String... components) {
        final String toString = cursor.toString();
        TestCase.assertContains(toString, components);
    }

    @Override
    protected Class<CharSequenceTextCursor> type() {
        return CharSequenceTextCursor.class;
    }
}
