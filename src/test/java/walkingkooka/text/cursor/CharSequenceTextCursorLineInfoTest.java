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
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class CharSequenceTextCursorLineInfoTest implements ClassTesting2<CharSequenceTextCursorLineInfo>,
        TextCursorLineInfoTesting<CharSequenceTextCursorLineInfo> {

    private final static String TEXT = "123";
    private final static int POS = 1;
    private final static int LINE_NUMBER = 1;

    // tests

    @Test
    public void testWith() {
        final CharSequenceTextCursorLineInfo info = CharSequenceTextCursorLineInfo
                .with(TEXT, POS);
        assertSame(TEXT, info.text, "text");
        assertEquals(POS, info.pos, "pos");
        assertNull(info.lineAndColumn, "lineAndColumn");
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
    public void testSecondLineAfterCarriageReturn() {
        final String prefix = "first\rl";
        this.lineWithPosition(prefix + "ine\rafter", prefix, "line", 2, 2);
    }

    @Test
    public void testAfterLastChar() {
        final String text = "first\nsecond\rthird";
        this.lineWithPosition(text, text.length() + 1, "third", 3, 6);
    }

    private void lineWithPosition(final String text,
                                  final String pos,
                                  final String line,
                                  final int lineNumber,
                                  final int column) {
        if (false == text.startsWith(pos)) {
            assertEquals("Pos text must be at the start of text", text, pos);
        }
        this.lineWithPosition(text, pos.length(), line, lineNumber, column);
    }

    private void lineWithPosition(final String text, final int pos, final String line, final int lineNumber,
                                  final int column) {
        final CharSequenceTextCursorLineInfo info = CharSequenceTextCursorLineInfo.with(text, pos);
        assertEquals(lineNumber, info.lineNumber(), "lineNumber=" + info);
        assertEquals(column, info.column(), "column()=" + info);
        assertEquals(line, info.text(), "text()=" + info);
    }

    @Test
    public void testSummary() {
        final CharSequenceTextCursorLineInfo info = CharSequenceTextCursorLineInfo.with("line1\nline2", 8);
        assertEquals("(3,2)", info.summary());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(CharSequenceTextCursorLineInfo.with(TEXT, 1),
                "Line: " + LINE_NUMBER + "=" + CharSequences.quoteAndEscape(TEXT));
    }

    // TextCursorLineInfoTesting................................................................................

    @Override
    public CharSequenceTextCursorLineInfo createLineInfo() {
        return CharSequenceTextCursorLineInfo.with(TEXT, 1);
    }

    // ClassTestCase..........................................................................................

    @Override
    public Class<CharSequenceTextCursorLineInfo> type() {
        return CharSequenceTextCursorLineInfo.class;
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
