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

import static org.junit.Assert.assertEquals;

final public class CharSequencesQuoteIfCharsTest extends StaticMethodTestCase {

    @Test
    public void testNull() {
        assertEquals("" + null, CharSequences.quoteIfChars(null));
    }

    @Test
    public void testNonCharSequence() {
        assertEquals("1", CharSequences.quoteIfChars(1L));
    }

    @Test
    public void testCharSequenceRequiresQuotes() {
        assertEquals("\"abc\\n\"", CharSequences.quoteIfChars("abc\n"));
    }

    @Test
    public void testCharSequenceAlreadyQuoted() {
        assertEquals("\"abc\\n\"", CharSequences.quoteIfChars("\"abc\n\""));
    }

    @Test
    public void testCharArrayRequiredQuotes() {
        assertEquals("\"abc\\n\"", CharSequences.quoteIfChars("\"abc\n\"".toCharArray()));
    }

    @Test
    public void testCharArrayAlreadyQuoted() {
        assertEquals("\"abc\\n\"", CharSequences.quoteIfChars("\"abc\n\"".toCharArray()));
    }

    @Test
    public void testChar() {
        assertEquals("'\\n'", CharSequences.quoteIfChars(Character.valueOf('\n')));
    }
}
