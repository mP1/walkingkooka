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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.StaticMethodTestCase;

final public class CharSequencesIndexOfTest extends StaticMethodTestCase {

    @Test
    public void testNullCharsFails() {
        this.indexOfFails(null, "searchFor");
    }

    @Test
    public void testNullSearchForFails() {
        this.indexOfFails("chars", null);
    }

    @Test
    public void testEmptySearchForFails() {
        this.indexOfFails("chars", "");
    }

    private void indexOfFails(final CharSequence chars, final String searchFor) {
        try {
            CharSequences.indexOf(chars, searchFor);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testNotContained() {
        final StringBuilder chars = new StringBuilder("apple");
        final String search = "banana";

        Assert.assertEquals(-1, CharSequences.indexOf(chars, search));
    }

    @Test
    public void testContainsWithSameCase() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "banana";

        Assert.assertEquals(6, CharSequences.indexOf(chars, search));
    }

    @Test
    public void testContainsDifferentCase() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "BANAna";

        Assert.assertEquals(-1, CharSequences.indexOf(chars, search));
    }

    @Test
    public void testPartialButIncompleteMatch() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "BANANARAMAMA";

        Assert.assertEquals(-1, CharSequences.indexOf(chars, search));
    }

    @Test
    public void testMatchesStart() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "apple";

        Assert.assertEquals(0, CharSequences.indexOf(chars, search));
    }

    @Test
    public void testMatchesEnd() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "carrot";

        Assert.assertEquals("apple banana ".length(), CharSequences.indexOf(chars, search));
    }

    @Test
    public void testOneChar() {
        final StringBuilder chars = new StringBuilder("a");
        Assert.assertEquals(0, CharSequences.indexOf(chars, chars.toString()));
    }

    @Test
    public void testOneCharIfIgnoringCase() {
        Assert.assertEquals(-1, CharSequences.indexOf(new StringBuilder("a"), "A"));
    }

    @Test
    public void testOneCharFails() {
        Assert.assertEquals(-1, CharSequences.indexOf(new StringBuilder("a"), "b"));
    }

    @Test
    public void testEmptyCharSequence() {
        Assert.assertEquals(-1, CharSequences.indexOf(new StringBuilder(), "never matched"));
    }

    @Test
    public void testSearchForContainCharSequenceAndMore() {
        final StringBuilder chars = new StringBuilder("chars");
        Assert.assertEquals(-1,
                CharSequences.indexOf(new StringBuilder(chars), chars + " + extra"));
    }

    @Test
    public void testEqual() {
        final StringBuilder chars = new StringBuilder("chars");
        Assert.assertEquals(0, CharSequences.indexOf(new StringBuilder(chars), "" + chars));
    }

    @Test
    public void testEqualButDifferentCase() {
        final String chars = "chars";
        Assert.assertEquals(-1, CharSequences.indexOf(chars.toUpperCase(), chars));
    }
}
