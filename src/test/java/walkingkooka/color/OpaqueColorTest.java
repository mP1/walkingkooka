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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public final class OpaqueColorTest extends ColorTestCase<Color> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullRedFails() {
        Color.with(null, GREEN, BLUE);
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullGreenFails() {
        Color.with(RED, null, BLUE);
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullBlueFails() {
        Color.with(RED, GREEN, null);
    }
    
    @Test
    public void testWith() {
        final Color color = Color.with(RED, GREEN, BLUE);
        assertSame("red", RED, color.red());
        assertSame("green", GREEN, color.green());
        assertSame("blue", BLUE, color.blue());
        assertSame("alpha", AlphaColorComponent.OPAQUE, color.alpha());
        assertEquals("rgb",
                (RED.value << 16) + (GREEN.value << 8) + BLUE.value, color.rgb());
    }

    @Test
    public void testFromRgb() {
        final Color color = Color.fromRgb(0x010203);
        assertSame("red", RED, color.red());
        assertSame("green", GREEN, color.green());
        assertSame("blue", BLUE, color.blue());
        assertSame("alpha", AlphaColorComponent.OPAQUE, color.alpha());
        assertEquals("rgb", 0x010203, color.rgb());
    }

    @Test
    public void testFromRgbIgnoresUpper8Bits() {
        final Color color = Color.fromRgb(0xFF010203);
        assertSame("red", RED, color.red());
        assertSame("green", GREEN, color.green());
        assertSame("blue", BLUE, color.blue());
        assertSame("alpha", AlphaColorComponent.OPAQUE, color.alpha());
        assertEquals("rgb", 0x010203, color.rgb());
    }

    @Test
    public void testRgbAndArgbAndValue() {
        final Color color = Color.with(RedColorComponent.with((byte) 0x80), GreenColorComponent.with((byte) 0x81),
                BlueColorComponent.with((byte) 0x82));
        assertEquals("rgb", 0x808182, color.rgb());
        assertEquals("argb", 0xFF808182, color.argb());
        assertEquals("value", 0x808182, color.value());
    }

    @Override
    public void testHasAlpha() {
        final Color color = Color.with(RED, GREEN, BLUE);
        assertFalse(color + " has no alpha", color.hasAlpha());
    }

    @Test
    public void testToAwtColor() {
        final java.awt.Color color = Color.fromRgb(0x010203).toAwtColor();
        assertEquals("red", 1, color.getRed());
        assertEquals("green", 2, color.getGreen());
        assertEquals("blue", 3, color.getBlue());
    }

    @Test
    public void testToString() {
        assertEquals("#010203", Color.fromRgb(0x010203).toString());
    }

    @Override
    Color createObject(final RedColorComponent red, final GreenColorComponent green, final BlueColorComponent blue) {
        return Color.with(red, green, blue);
    }

    @Override
    protected Class<Color> type() {
        return Color.class;
    }
}
