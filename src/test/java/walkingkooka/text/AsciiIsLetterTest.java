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

final public class AsciiIsLetterTest extends StaticMethodTestCase {

    @Test
    public void testNumber() {
        Assert.assertFalse(Ascii.isLetter('0'));
    }

    @Test
    public void testBeforeUpperA() {
        Assert.assertFalse(Ascii.isLetter((char) ('A' - 1)));
    }

    @Test
    public void testUpperA() {
        Assert.assertTrue(Ascii.isLetter('A'));
    }

    @Test
    public void testUpper() {
        Assert.assertTrue(Ascii.isLetter('B'));
    }

    @Test
    public void testUpperZ() {
        Assert.assertTrue(Ascii.isLetter('Z'));
    }

    @Test
    public void testAfterUpperZ() {
        Assert.assertFalse(Ascii.isLetter((char) ('Z' + 1)));
    }

    @Test
    public void testBeforeLowerA() {
        Assert.assertFalse(Ascii.isLetter((char) ('a' - 1)));
    }

    @Test
    public void testLowerA() {
        Assert.assertTrue(Ascii.isLetter('a'));
    }

    @Test
    public void testLower() {
        Assert.assertTrue(Ascii.isLetter('b'));
    }

    @Test
    public void testLowerZ() {
        Assert.assertTrue(Ascii.isLetter('z'));
    }

    @Test
    public void testAfterLowerZ() {
        Assert.assertFalse(Ascii.isLetter((char) ('z' + 1)));
    }

    @Test
    public void testNonAsciiLetter() {
        final char c = 255;
        Assert.assertTrue(Character.isLetter(c));
        Assert.assertFalse(Ascii.isLetter(c));
    }
}
