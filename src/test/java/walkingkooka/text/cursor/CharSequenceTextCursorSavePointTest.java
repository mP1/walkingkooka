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

import static org.junit.Assert.assertNotSame;

final public class CharSequenceTextCursorSavePointTest extends
        TextCursorSavePointTestCase<CharSequenceTextCursorSavePoint, CharSequenceTextCursor> {

    @Test
    public void testSaveAndRestoreWithFromAndTo() {
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with("0123456789");
        this.atAndCheck(cursor, '0');

        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        this.atAndCheck(cursor, '1');

        save.restore();
        this.atAndCheck(cursor, '0');
    }

    @Test
    public void testLineInfo() {
        final CharSequenceTextCursor cursor = CharSequenceTextCursor.with("0123456789");
        this.atAndCheck(cursor, '0');

        final TextCursorLineInfo info = cursor.lineInfo();

        final TextCursorSavePoint save = cursor.save();
        cursor.next();
        this.atAndCheck(cursor, '1');

        final TextCursorLineInfo saveInfo = save.lineInfo();
        assertNotSame(info, saveInfo);
        assertEquals("save column", info.column(), saveInfo.column());
        assertEquals("save lineNumber", info.lineNumber(), saveInfo.lineNumber());
        TestCase.assertEquals("save text", info.text(), saveInfo.text());
    }

    @Override
    protected CharSequenceTextCursor createTextCursor(final String text) {
        return CharSequenceTextCursor.with(text);
    }

    @Override
    protected Class<CharSequenceTextCursorSavePoint> type() {
        return CharSequenceTextCursorSavePoint.class;
    }
}
