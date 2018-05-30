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

final public class AsciiIsDigitTest extends StaticMethodTestCase {

    @Test
    public void testLetter() {
        Assert.assertFalse(Ascii.isDigit('a'));
    }

    @Test
    public void testBeforeZero() {
        Assert.assertFalse(Ascii.isDigit((char) ('0' - 1)));
    }

    @Test
    public void testZero() {
        Assert.assertTrue(Ascii.isDigit('0'));
    }

    @Test
    public void testLower() {
        Assert.assertTrue(Ascii.isDigit('2'));
    }

    @Test
    public void testLowerNine() {
        Assert.assertTrue(Ascii.isDigit('9'));
    }

    @Test
    public void testAfterNine() {
        Assert.assertFalse(Ascii.isDigit((char) ('9' + 1)));
    }

    @Test
    public void testNonAsciiNumber() {
        char c = 0;
        for (int i = 255; i < 65536; i++) {
            if (Character.isDigit(c)) {
                c = (char) i;
                break;
            }
        }
        Assert.assertFalse("didnt find non ascii digit", c != 0);
        Assert.assertFalse(Ascii.isDigit(c));
    }
}
