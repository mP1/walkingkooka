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

final public class CharSequencesStartsWithTest extends StaticMethodTestCase {

    @Test
    public void testDifferent() {
        final CharSequence test = "apple";
        final String startsWith = "X";
        Assert.assertFalse(CharSequences.startsWith(test, startsWith));
    }

    @Test
    public void testMixedCase() {
        final CharSequence test = "apple";
        final String startsWith = "aP";
        Assert.assertFalse(CharSequences.startsWith(test, startsWith));
    }

    @Test
    public void testStartsWithDifferentCase() {
        final CharSequence test = "Apple";
        final String startsWith = "AP";
        Assert.assertFalse(CharSequences.startsWith(test, startsWith));
    }

    @Test
    public void testSameCase() {
        final CharSequence test = "Apple";
        final String startsWith = test + "Banana";
        Assert.assertFalse(CharSequences.startsWith(test, startsWith));
    }

    @Test
    public void testStartsWith() {
        final CharSequence first = "green.apple";
        final String second = "green";

        Assert.assertTrue(CharSequences.startsWith(first, second));
    }

    @Test
    public void testFailsBecauseSecondIsLongerThanFirst() {
        final CharSequence first = "green.apple";
        final String second = "green.apple.big";

        Assert.assertFalse(CharSequences.startsWith(first, second));
    }
}
