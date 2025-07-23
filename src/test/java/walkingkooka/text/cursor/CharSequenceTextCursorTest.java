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
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharSequenceTextCursorTest implements ClassTesting2<CharSequenceTextCursor>,
    TextCursorTesting2<CharSequenceTextCursor>,
    ToStringTesting<CharSequenceTextCursor> {

    @Test
    public void testFromNullFails() {
        assertThrows(NullPointerException.class, () -> CharSequenceTextCursor.with(null));
    }

    @Test
    public void testFrom() {
        final String text = "text";
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with(text);
        this.checkEquals(0, cursor.position, "positon");
        this.checkEquals(text, cursor.text, "text");
    }

    @Test
    public void testAtWhenEmpty() {
        this.atFails(CharSequenceTextCursor.with(""));
    }

    @Test
    public void testAtWhenEmpty2() {
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with("123");
        cursor.next();
        cursor.next();
        cursor.next();
        this.atFails(cursor);
    }

    @Override
    public void testLineInfo() {
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with("text\nnext");
        cursor.next();
        this.lineInfoCheck(cursor, "text", 1, 2);
    }

    @Test
    public void testText() {
        final String text = "Hello\nWorld\n";

        this.textAndCheck(
            CharSequenceTextCursor.with(text),
            text
        );
    }

    @Test
    public void testToStringAtBeginning() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.toStringContainsCheck(cursor, "at 0 \"[a]bcdefgh\"");
    }

    @Test
    public void testToStringNearBeginning() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        cursor.next();
        this.toStringContainsCheck(cursor, "at 1 \"a[b]cdefghi\"");
    }

    @Test
    public void testToStringNearBeginning2() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 2);
        this.toStringContainsCheck(cursor, "at 2 \"ab[c]defghij\"");
    }

    @Test
    public void testToStringMiddle() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 13);
        this.toStringContainsCheck(cursor, "at 13 \"ghijklm[n]opqrstu\"");
    }

    @Test
    public void testToSteringAtEnd() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 25);
        this.toStringContainsCheck(cursor, "at 25 \"stuvwxy[z]\"");
    }

    @Test
    public void testToStringAfterEnd() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        cursor.end();
        this.toStringContainsCheck(cursor, "at 26 \"tuvwxyz\"[]");
    }

    @Test
    public void testToStringNearEnd() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 24);
        this.toStringContainsCheck(cursor, "at 24 \"rstuvwx[y]z\"");
    }

    @Test
    public void testToStringNearEnd2() {
        final CharSequenceTextCursor cursor = this.createTextCursor("abcdefghijklmnopqrstuvwxyz");
        this.moveBy(cursor, 23);
        this.toStringContainsCheck(cursor, "at 23 \"qrstuvw[x]yz\"");
    }

    @Override
    public CharSequenceTextCursor createTextCursor(final String text) {
        return CharSequenceTextCursor.with(text);
    }

    // TODO TextCursor.skip
    private void moveBy(final TextCursor cursor, final int skip) {
        for (int i = 0; i < skip; i++) {
            cursor.next();
        }
    }

    // ClassTestCase.......................................................................................

    @Override
    public Class<CharSequenceTextCursor> type() {
        return CharSequenceTextCursor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
