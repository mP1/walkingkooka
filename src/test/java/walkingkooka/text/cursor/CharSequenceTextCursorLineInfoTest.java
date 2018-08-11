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
import static org.junit.Assert.assertNull;

final public class CharSequenceTextCursorLineInfoTest extends TextCursorLineInfoTestCase<CharSequenceTextCursorLineInfo> {

    private final static String TEXT = "123";
    private final static int POS = 1;
    private final static int LINE_NUMBER = 1;

    // tests

    @Override
    public void testNaming() {
        this.checkNaming(TextCursorLineInfo.class);
    }

    @Test
    public void testWith() {
        final CharSequenceTextCursorLineInfo info = CharSequenceTextCursorLineInfo
                .with(CharSequenceTextCursorLineInfoTest.TEXT, CharSequenceTextCursorLineInfoTest.POS);
        TestCase.assertSame("text", CharSequenceTextCursorLineInfoTest.TEXT, info.text);
        assertEquals("pos", CharSequenceTextCursorLineInfoTest.POS, info.pos);
        assertNull("lineAndColumn", info.lineAndColumn);
    }

    @Test
    public void testMiddleOfFirstLine() {
        this.lineWithPosition("line", 1, "line", 1, 2);
    }

    @Test
    public void testSecondLineAfterNewLine() {
        final String prefix = "first\nl";
        this.lineWithPosition(prefix + "ine\nafter", prefix, "line", 2, 2);
    }

    @Test
    public void testAfterLastChar() {
        final String text = "first\nsecond\rthird";
        this.lineWithPosition(text, text.length() + 1, "", 4, 1);
    }

    private void lineWithPosition(final String text, final String pos, final String line, final int lineNumber,
                                  final int column) {
        if (false == text.startsWith(pos)) {
            assertEquals("Pos text must be at the start of text", text, pos);
        }
        this.lineWithPosition(text, pos.length(), line, lineNumber, column);
    }

    private void lineWithPosition(final String text, final int pos, final String line, final int lineNumber,
                                  final int column) {
        final CharSequenceTextCursorLineInfo info = CharSequenceTextCursorLineInfo.with(text, pos);
        assertEquals("lineNumber", lineNumber, info.lineNumber());
        assertEquals("lineNumber", lineNumber, info.lineNumber());
        assertEquals("column()", column, info.column());
        assertEquals("text()", line, info.text());
    }

    @Test
    public void testSummary() {
        final CharSequenceTextCursorLineInfo info = CharSequenceTextCursorLineInfo.with("line1\nline2", 8);
        assertEquals("(3,2)", info.summary());
    }

    @Test
    public void testToString() {
        final CharSequenceTextCursorLineInfo info = CharSequenceTextCursorLineInfo
                .with(CharSequenceTextCursorLineInfoTest.TEXT, 1);
        assertEquals("Line: " + CharSequenceTextCursorLineInfoTest.LINE_NUMBER + "="
                + CharSequences.quoteAndEscape(CharSequenceTextCursorLineInfoTest.TEXT), info.toString());
    }

    @Override
    protected CharSequenceTextCursorLineInfo createLineInfo() {
        return CharSequenceTextCursorLineInfo.with(CharSequenceTextCursorLineInfoTest.TEXT, 1);
    }

    @Override
    protected Class<CharSequenceTextCursorLineInfo> type() {
        return CharSequenceTextCursorLineInfo.class;
    }
}
