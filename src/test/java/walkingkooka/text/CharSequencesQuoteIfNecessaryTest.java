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

final public class CharSequencesQuoteIfNecessaryTest extends StaticMethodTestCase {

    @Test
    public void testNull() {
        final CharSequence chars = null;
        assertEquals(chars, CharSequences.quoteIfNecessary(chars));
    }

    @Test
    public void testUnnecessary() {
        final CharSequence chars = "apple";
        assertEquals(chars, CharSequences.quoteIfNecessary(chars));
    }

    @Test
    public void testQuotesRequired() {
        final CharSequence chars = "apple banana";
        Assert.assertEquals('"' + chars.toString() + '"',
                CharSequences.quoteIfNecessary(chars).toString());
    }

    @Test
    public void testAlreadyQuoted() {
        final String already = "\"abc\"";
        assertEquals(already, CharSequences.quoteIfNecessary(already));
    }
}
