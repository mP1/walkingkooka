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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertNotSame;

final public class CharSequenceTextCursorSavePointTest implements ClassTesting2<CharSequenceTextCursorSavePoint>,
    TextCursorSavePointTesting<CharSequenceTextCursorSavePoint, CharSequenceTextCursor> {

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
        this.checkEquals(info.column(), saveInfo.column(), "save column");
        this.checkEquals(info.lineNumber(), saveInfo.lineNumber(), "save lineNumber");
        this.checkEquals(info.text(), saveInfo.text(), "save text");
    }

    @Override
    public CharSequenceTextCursor createTextCursor(final String text) {
        return CharSequenceTextCursor.with(text);
    }

    // ClassTestCase.......................................................................................

    @Override
    public Class<CharSequenceTextCursorSavePoint> type() {
        return CharSequenceTextCursorSavePoint.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
