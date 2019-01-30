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

package walkingkooka.color;

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class ColorTest extends ClassTestCase<Color> {

    // parse..................................................................

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        Color.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyFails() {
        Color.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLeadingHashFails() {
        Color.parse("123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFails() {
        Color.parse("xyz");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFails2() {
        Color.parse("#1x3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOneDigitFails() {
        Color.parse("#1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTwoDigitsFails() {
        Color.parse("#12");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFourDigitsFails() {
        Color.parse("#1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFiveDigitsFails() {
        Color.parse("#12345");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSevenDigitsFails() {
        Color.parse("#1234567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEightDigitsFails() {
        Color.parse("#12345678");
    }

    @Test
    public void testParseHashRedRedGreenGreenBlueBlue() {
        this.parseRgbAndCheck("#123456", 0x123456);
    }

    @Test
    public void testParseHashRedRedGreenGreenBlueBlue2() {
        this.parseRgbAndCheck("#000011", 0x000011);
    }

    @Test
    public void testParseHashRedRedGreenGreenBlueBlue3() {
        this.parseRgbAndCheck("#010101", 0x010101);
    }

    @Test
    public void testParseHashRedGreenBlue() {
        this.parseRgbAndCheck("#123", 0x112233);
    }

    @Test
    public void testParseHashRedGreenBlue2() {
        this.parseRgbAndCheck("#001", 0x000011);
    }

    @Test
    public void testParseHashRedGreenBlue3() {
        this.parseRgbAndCheck("#010", 0x001100);
    }

    @Test
    public void testParseHashRedGreenBlue4() {
        this.parseRgbAndCheck("#100", 0x110000);
    }

    private void parseRgbAndCheck(final String text, final int rgb) {
        assertEquals("parse " + CharSequences.quote(text),
                Color.fromRgb(rgb),
                Color.parse(text));
    }

    @Override
    protected Class<Color> type() {
        return Color.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
