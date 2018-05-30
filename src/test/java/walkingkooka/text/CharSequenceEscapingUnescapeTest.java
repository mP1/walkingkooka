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

package walkingkooka.text;

import org.junit.Test;
import walkingkooka.test.StaticMethodTestCase;

final public class CharSequenceEscapingUnescapeTest extends StaticMethodTestCase {

    @Test
    public void testUnneeded() {
        final String string = "before";
        assertEquals(string, CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testNewLine() {
        final String string = "before\\nafter";
        assertEquals("before\nafter", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testCarriageReturn() {
        final String string = "before\\rafter";
        assertEquals("before\rafter", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testBackslash() {
        final String string = "before\\\\after";
        assertEquals("before\\after", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testTab() {
        final String string = "before\\tafter";
        assertEquals("before\tafter", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testDoubleQuote() {
        final String string = "before\\\"after";
        assertEquals("before\"after", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testSingleQuote() {
        final String string = "before\\\'";
        assertEquals("before\'", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testNul() {
        final String string = "before\\0";
        assertEquals("before\0", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testEscape() {
        final String string = "before\\t\\n\\r\\\\";
        assertEquals("before\t\n\r\\", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testIncompleteUnicode() {
        final String string = "b\\u";
        assertEquals("b\\u", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testInvalidUnicode1() {
        final String string = "*\\uX000";
        assertEquals("*\\uX000", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testInvalidUnicode2() {
        final String string = "*\\u0X00";
        assertEquals("*\\u0X00", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testInvalidUnicode3() {
        final String string = "*\\u00X0";
        assertEquals("*\\u00X0", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testInvalidUnicode4() {
        final String string = "*\\u000X";
        assertEquals("*\\u000X", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testUnicode() {
        final String string = "*\\u0001";
        assertEquals("*\u0001", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testUnicode2() {
        final String string = "*\\u0021";
        assertEquals("*\u0021", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testUnicode3() {
        final String string = "*\\u0321";
        assertEquals("*\u0321", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testUnicode4() {
        final String string = "*\\u4321";
        assertEquals("*\u4321", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testManyUnicode() {
        final String string = "\\u4321\\u5678\\u4321\\u5678";
        assertEquals("\u4321\u5678\u4321\u5678", CharSequenceEscaping.unescape(string));
    }

    @Test
    public void testEscapeUnescape() {
        final String string = "before\t \n \r \\ \' \\ \0";
        assertEquals(string, CharSequenceEscaping.unescape(CharSequences.escape(string)));
    }
}
