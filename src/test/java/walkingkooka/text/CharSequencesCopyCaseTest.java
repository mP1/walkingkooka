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
 *
 */

package walkingkooka.text;

import org.junit.Test;
import walkingkooka.test.StaticMethodTestCase;

import static org.junit.Assert.assertEquals;

final public class CharSequencesCopyCaseTest extends StaticMethodTestCase {

    private final static String CHARS = "abc";
    private final static String CASE_SOURCE = "xyz";

    @Test(expected = NullPointerException.class)
    public void testNullCharsFails() {
        CharSequences.copyCase(null, CASE_SOURCE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCaseSourceFails() {
        CharSequences.copyCase(CHARS, null);
    }

    @Test
    public void testBothLowerCase() {
        copyCaseAndCheck("abc", "def", "abc");
    }

    @Test
    public void testEmptyCaseSource() {
        copyCaseAndCheck("abc", "", "abc");
    }

    @Test
    public void testEmptyChars() {
        copyCaseAndCheck("", CASE_SOURCE, "");
    }

    @Test
    public void testDifferentCase() {
        copyCaseAndCheck("abc", "XYZ", "ABC");
    }

    @Test
    public void testDifferentCase2() {
        copyCaseAndCheck("ABC", "xyz", "abc");
    }

    @Test
    public void testDifferentCase3() {
        copyCaseAndCheck("ABcd", "mnOP", "abCD");
    }

    @Test
    public void testCaseSourceNotLetters() {
        copyCaseAndCheck("ABcd", "123", "ABcd");
    }

    @Test
    public void testCharsIncludesNonLetters() {
        copyCaseAndCheck("AB12cd", "aBcDeF", "aB12cD");
    }

    @Test
    public void testCaseSourceShorter() {
        copyCaseAndCheck("ABcd", "a", "aBcd");
    }

    @Test
    public void testCaseSourceShorter2() {
        copyCaseAndCheck("aBcd", "A", "ABcd");
    }

    private static void copyCaseAndCheck(final String chars, final String caseSource, final String expected) {
        assertEquals("copyCase " + CharSequences.quote(chars) + ", "  + CharSequences.quote(caseSource),
                expected,
                CharSequences.copyCase(chars, caseSource).toString());
    }
}
