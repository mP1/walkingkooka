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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColorTest extends ClassTestCase<Color> {

    // parse..................................................................

    @Test
    public void testParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Color.parse(null);
        });
    }

    @Test
    public void testParseEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("");
        });
    }

    @Test
    public void testMissingLeadingHashFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("123");
        });
    }

    @Test
    public void testParseInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("xyz");
        });
    }

    @Test
    public void testParseInvalidFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("#1x3");
        });
    }

    @Test
    public void testParseOneDigitFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("#1");
        });
    }

    @Test
    public void testParseTwoDigitsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("#12");
        });
    }

    @Test
    public void testParseFourDigitsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("#1234");
        });
    }

    @Test
    public void testParseFiveDigitsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("#12345");
        });
    }

    @Test
    public void testParseSevenDigitsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("#1234567");
        });
    }

    @Test
    public void testParseEightDigitsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Color.parse("#12345678");
        });
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
        assertEquals(Color.fromRgb(rgb),
                Color.parse(text),
                "parse " + CharSequences.quote(text));
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
