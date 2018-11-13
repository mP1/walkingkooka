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
import walkingkooka.test.PackagePrivateStaticHelperTestCase;

import static org.junit.Assert.assertEquals;

final public class CharSequenceEscapingTest extends PackagePrivateStaticHelperTestCase<CharSequenceEscaping> {

    @Test
    public void testNoNeeded() {
        escapeAndCheck("apple", "apple");
    }

    @Test
    public void testNewLine() {
        escapeAndCheck("apple\nbanana", "apple\\nbanana");
    }

    @Test
    public void testCarriageReturn() {
        escapeAndCheck("apple\rbanana", "apple\\rbanana");
    }

    @Test
    public void testBackslash() {
        escapeAndCheck("apple\\banana", "apple\\\\banana");
    }

    @Test
    public void testTab() {
        escapeAndCheck("apple\tbanana", "apple\\tbanana");
    }

    @Test
    public void testDoubleQuote() {
        escapeAndCheck("apple\"", "apple\\\"");
    }

    @Test
    public void testSingleQuote() {
        escapeAndCheck("apple\'", "apple\\\'");
    }

    @Test
    public void testNul() {
        escapeAndCheck("apple\0", "apple\\0");
    }

    @Test
    public void testTabNewLineCarriageReturn() {
        escapeAndCheck("apple\t\n\r\\", "apple\\t\\n\\r\\\\");
    }

    @Test
    public void testControlCharacter() {
        escapeAndCheck("apple\u000F;banana", "apple\\u000F;banana");
    }

    @Test
    public void testControlCharacter2() {
        escapeAndCheck("apple\u001F;banana", "apple\\u001F;banana");
    }

    private void escapeAndCheck(final CharSequence chars, final String expected) {
        assertEquals("escape " + CharSequences.quote(chars), expected, CharSequenceEscaping.escape(chars));
    }

    @Override
    protected Class<CharSequenceEscaping> type() {
        return CharSequenceEscaping.class;
    }
}
