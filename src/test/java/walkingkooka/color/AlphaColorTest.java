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
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AlphaColorTest extends ColorTestCase<AlphaColor> {

    @Test
    public void testFromArgb() {
        final Color color = Color.fromArgb(0x04010203);
        assertSame(RED, color.red(), "red");
        assertSame(GREEN, color.green(), "green");
        assertSame(BLUE, color.blue(), "blue");
        assertSame(ColorComponent.alpha((byte) 0x4), color.alpha(), "alpha");
        assertEquals(0x04010203, color.argb(), "argb");
    }

    @Test
    public void testFromArgbWhenOpaque() {
        final Color color = Color.fromArgb(0xFF010203);
        assertSame(RED, color.red(), "red");
        assertSame(GREEN, color.green(), "green");
        assertSame(BLUE, color.blue(), "blue");
        assertEquals(OpaqueColor.class, color.getClass(), "color must not be a AlphaColor");
        assertEquals(0xFF010203, color.argb(), "argb");
    }

    @Test
    public void testRgbAndArgbAndValue() {
        final Color color = Color.with(ColorComponent.red((byte) 0x80),
                ColorComponent.green((byte) 0x81),
                ColorComponent.blue((byte) 0x82)).set(ColorComponent.alpha((byte) 0x84));
        assertEquals(0x808182, color.rgb(), "rgb");
        assertEquals(0x84808182, color.argb(), "argb");
        assertEquals(0x84808182, color.value(), "value");
    }

    @Test
    @Override
    public void testHasAlpha() {
        final AlphaColor color = AlphaColor.with(RED, GREEN, BLUE, ALPHA);
        assertTrue(color.hasAlpha(), color + " has alpha");
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
        assertSame(color.red(), opaque.red(), "red");
        assertSame(color.green(), opaque.green(), "green");
        assertSame(color.blue(), opaque.blue(), "blue");
        assertSame(replacement, opaque.alpha(), "alpha");
    }

    @Test
    public void testMixDifferentAlphaVeryLargeAmount() {
        final AlphaColorComponent replacement = AlphaColorComponent.with(DIFFERENT);
        this.mixAndCheck(this.createObject(), replacement, LARGE_AMOUNT, replacement);
    }

    @Test
    public void testToAwtColor() {
        final java.awt.Color color = this.createObject().toAwtColor();
        assertEquals(1, color.getRed(), "red");
        assertEquals(2, color.getGreen(), "green");
        assertEquals(3, color.getBlue(), "blue");
        assertEquals(4, color.getAlpha(), "alpha");
    }

    @Test
    public void testEqualsDifferentAlpha() {
        final Color color = this.createObject();
        this.checkNotEquals(AlphaColor.with(color.red(), color.green(), color.blue(), AlphaColorComponent.with((byte) 0xff)));
    }

    // HasJsonNode............................................................................................

    @Test
    public void testToJsonNode() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Color.fromArgb(0x11223344).toJsonNode();
        });
    }

    // Object............................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(Color.fromArgb(0x04010203), "#04010203");
    }

    @Override
    AlphaColor createObject(final RedColorComponent red, final GreenColorComponent green,
                            final BlueColorComponent blue) {
        return AlphaColor.with(red, green, blue, ALPHA);
    }

    @Override
    public Class<AlphaColor> type() {
        return AlphaColor.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public AlphaColor serializableInstance() {
        final byte value = 1;
        return AlphaColor.with(ColorComponent.red(value),
                ColorComponent.green(value),
                ColorComponent.blue(value),
                ColorComponent.alpha(value));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
