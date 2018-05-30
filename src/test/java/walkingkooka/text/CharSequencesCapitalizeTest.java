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

final public class CharSequencesCapitalizeTest extends StaticMethodTestCase {

    @Test
    public void testNullFails() {
        try {
            CharSequences.capitalize(null);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testEmpty() {
        assertSame("", CharSequences.capitalize(""));
    }

    @Test
    public void testSingleLetter() {
        assertEquals("A", CharSequences.capitalize("a"));
    }

    @Test
    public void testOneChar() {
        assertEquals("1", CharSequences.capitalize("1"));
    }

    @Test
    public void testCapitalize() {
        assertEquals("Apple", CharSequences.capitalize("apple"));
    }
}
