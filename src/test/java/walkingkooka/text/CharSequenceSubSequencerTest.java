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
import walkingkooka.test.PackagePrivateStaticHelperTestCase;

import static org.junit.Assert.assertEquals;

final public class CharSequenceSubSequencerTest
        extends PackagePrivateStaticHelperTestCase<CharSequenceSubSequencer> {

    @Test(expected = NullPointerException.class)
    public void testNullFails() {
        CharSequenceSubSequencer.make(null, 1, 1);
    }

    @Test
    public void testToBeforeFromFails() {
        final int from = 4;
        final int to = -5;
        final String string = "string";
        try {
            CharSequenceSubSequencer.make(string, from, to);
            Assert.fail();
        } catch (final IndexOutOfBoundsException expected) {
            assertEquals("message",
                    CharSequenceSubSequencer.toIndexBeforeFromIndex(from, to, string.length()),
                    expected.getMessage());
        }
    }

    @Test
    public void testMake() {
        assertEquals("sub", CharSequenceSubSequencer.make(" sub ", 1, 4));
    }

    @Test
    public void testNegativeTo() {
        assertEquals("bcd", CharSequenceSubSequencer.make("abcdef", 1, -2));
    }

    @Test
    public void testZeroTo() {
        assertEquals("bcdef", CharSequenceSubSequencer.make("abcdef", 1, 0));
    }
    
    @Override
    protected Class<CharSequenceSubSequencer> type() {
        return CharSequenceSubSequencer.class;
    }
}
