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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class AlphaColorTest extends ColorTestCase<AlphaColor> {

    @Test
    public void testFromArgb() {
        final Color color = Color.fromArgb(0x04010203);
        assertSame("red", RED, color.red());
        assertSame("green", GREEN, color.green());
        assertSame("blue", BLUE, color.blue());
        assertSame("alpha", ColorComponent.alpha((byte) 0x4), color.alpha());
        assertEquals("argb", 0x04010203, color.argb());
    }

    @Test
    public void testFromArgbWhenOpaque() {
        final Color color = Color.fromArgb(0xFF010203);
        assertSame("red", RED, color.red());
        assertSame("green", GREEN, color.green());
        assertSame("blue", BLUE, color.blue());
        assertEquals("color must not be a AlphaColor", OpaqueColor.class, color.getClass());
        assertEquals("argb", 0xFF010203, color.argb());
    }

    @Test
    public void testRgbAndArgbAndValue() {
        final Color color = Color.with(ColorComponent.red((byte) 0x80),
                ColorComponent.green((byte) 0x81),
                ColorComponent.blue((byte) 0x82)).set(ColorComponent.alpha((byte) 0x84));
        assertEquals("rgb", 0x808182, color.rgb());
        assertEquals("argb", 0x84808182, color.argb());
        assertEquals("value", 0x84808182, color.value());
    }

    @Test
    @Override
    public void testHasAlpha() {
        final AlphaColor color = AlphaColor.with(RED, GREEN, BLUE, ALPHA);
        assertTrue(color + " has alpha", color.hasAlpha());
    }

    @Test
    public void testSetSameAlpha() {
        final AlphaColor color = this.createObject();
        assertSame(color, color.set(ALPHA));
    }

    @Test
    public void testSetOpaqueAlphaBecomesOpaqueColor() {
        final AlphaColor color = this.createObject();
        final ColorComponent replacement = AlphaColorComponent.OPAQUE;
        final OpaqueColor opaque = (OpaqueColor) color.set(replacement);
        assertSame("red", color.red(), opaque.red());
        assertSame("green", color.green(), opaque.green());
        assertSame("blue", color.blue(), opaque.blue());
        assertSame("alpha", replacement, opaque.alpha());
    }

    @Test
    public void testMixDifferentAlphaVeryLargeAmount() {
        final AlphaColorComponent replacement = AlphaColorComponent.with(DIFFERENT);
        this.mixAndCheck(this.createObject(), replacement, LARGE_AMOUNT, replacement);
    }

    @Test
    public void testToAwtColor() {
        final java.awt.Color color = this.createObject().toAwtColor();
        assertEquals("red", 1, color.getRed());
        assertEquals("green", 2, color.getGreen());
        assertEquals("blue", 3, color.getBlue());
        assertEquals("alpha", 4, color.getAlpha());
    }
    
    @Test
    public void testToString() {
        assertEquals("#04010203", Color.fromArgb(0x04010203).toString());
    }

    @Override
    AlphaColor createObject(final RedColorComponent red, final GreenColorComponent green,
                            final BlueColorComponent blue) {
        return AlphaColor.with(red, green, blue, ALPHA);
    }

    @Override
    protected boolean typeMustBePublic() {
        return true;
    }

    @Override
    protected Class<AlphaColor> type() {
        return AlphaColor.class;
    }
}
