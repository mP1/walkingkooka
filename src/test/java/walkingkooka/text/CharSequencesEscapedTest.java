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

final public class CharSequencesEscapedTest extends StaticMethodTestCase {

    @Test
    public void testNull() {
        assertEquals(null, CharSequences.quoteAndEscape(null));
    }

    @Test
    public void testUnneeded() {
        final String string = "apple";
        assertEquals('"' + string + '"', CharSequences.quoteAndEscape(string));
    }

    @Test
    public void testNewLine() {
        final String string = "apple\nbanana";
        assertEquals("\"apple\\nbanana\"", CharSequences.quoteAndEscape(string));
    }

    @Test
    public void testCarriageReturn() {
        final String string = "apple\rbanana";
        assertEquals("\"apple\\rbanana\"", CharSequences.quoteAndEscape(string));
    }

    @Test
    public void testBackslash() {
        final String string = "apple\\banana";
        assertEquals("\"apple\\\\banana\"", CharSequences.quoteAndEscape(string));
    }

    @Test
    public void testTab() {
        final String string = "apple\tbanana";
        assertEquals("\"apple\\tbanana\"", CharSequences.quoteAndEscape(string));
    }

    @Test
    public void testEscapedDoubleQuote() {
        final String string = "apple\"";
        assertEquals("\"apple\\\"\"", CharSequences.quoteAndEscape(string));
    }

    @Test
    public void testAddEscaped() {
        final String string = "apple\t\n\r\\";
        assertEquals("\"apple\\t\\n\\r\\\\\"", CharSequences.quoteAndEscape(string));
    }
}
