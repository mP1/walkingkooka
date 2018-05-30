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

final public class CharSequencesEqualsTest extends StaticMethodTestCase {

    @Test
    public void testDifferentLength() {
        final CharSequence test = "apple";
        final String other = "different";
        Assert.assertFalse(CharSequences.equals(test, other));
    }

    @Test
    public void testSame() {
        final CharSequence test = "apple";
        final String other = "apple";
        Assert.assertTrue(CharSequences.equals(test, other));
    }

    @Test
    public void testDifferentCase() {
        final CharSequence test = "apple";
        final String other = "APPLE";
        Assert.assertFalse(CharSequences.equals(test, other));
    }
}
