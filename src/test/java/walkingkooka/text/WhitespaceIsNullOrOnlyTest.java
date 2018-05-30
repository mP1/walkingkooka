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

final public class WhitespaceIsNullOrOnlyTest extends StaticMethodTestCase {

    @Test
    public void testNull() {
        Assert.assertTrue(Whitespace.isNullOrOnly(null));
    }

    @Test
    public void testEmpty() {
        Assert.assertTrue(Whitespace.isNullOrOnly(""));
    }

    @Test
    public void testWhitespaceFilled() {
        Assert.assertTrue(Whitespace.isNullOrOnly(" \t \t"));
    }

    @Test
    public void testWhitespaceAndLetters() {
        Assert.assertFalse(Whitespace.isNullOrOnly(" \tA"));
    }

    @Test
    public void testLetters2() {
        Assert.assertFalse(Whitespace.isNullOrOnly("A"));
    }
}
