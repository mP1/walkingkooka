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

final public class CharSequenceEscapingEscapeTest extends StaticMethodTestCase {

    @Test
    public void testUnneeded() {
        final String string = "apple";
        assertEquals(string, CharSequenceEscaping.escape(string));
    }

    @Test
    public void testNewLine() {
        final String string = "apple\nbanana";
        assertEquals("apple\\nbanana", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testCarriageReturn() {
        final String string = "apple\rbanana";
        assertEquals("apple\\rbanana", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testBackslash() {
        final String string = "apple\\banana";
        assertEquals("apple\\\\banana", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testTab() {
        final String string = "apple\tbanana";
        assertEquals("apple\\tbanana", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testDoubleQuote() {
        final String string = "apple\"";
        assertEquals("apple\\\"", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testSingleQuote() {
        final String string = "apple\'";
        assertEquals("apple\\\'", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testNul() {
        final String string = "apple\0";
        assertEquals("apple\\0", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testTabNewLineCarriageReturn() {
        final String string = "apple\t\n\r\\";
        assertEquals("apple\\t\\n\\r\\\\", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testControlCharacter() {
        final String string = "apple\u000F;banana";
        assertEquals("apple\\u000F;banana", CharSequenceEscaping.escape(string));
    }

    @Test
    public void testControlCharacter2() {
        final String string = "apple\u001F;banana";
        assertEquals("apple\\u001F;banana", CharSequenceEscaping.escape(string));
    }
}
