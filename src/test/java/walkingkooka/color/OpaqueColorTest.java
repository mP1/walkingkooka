/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.color;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OpaqueColorTest extends ColorTestCase<OpaqueColor> {

    @Test
    public final void testWithNullRedFails() {
        assertThrows(NullPointerException.class, () -> {
            OpaqueColor.createOpaqueColor(null, GREEN, BLUE);
        });
    }

    @Test
    public final void testWithNullGreenFails() {
        assertThrows(NullPointerException.class, () -> {
            OpaqueColor.createOpaqueColor(RED, null, BLUE);
        });
    }

    @Test
    public final void testWithNullBlueFails() {
        assertThrows(NullPointerException.class, () -> {
            OpaqueColor.createOpaqueColor(RED, GREEN, null);
        });
    }

    @Test
    public void testWith() {
        final OpaqueColor color = OpaqueColor.createOpaqueColor(RED, GREEN, BLUE);
        assertSame(RED, color.red(), "red");
        assertSame(GREEN, color.green(), "green");
        assertSame(BLUE, color.blue(), "blue");
        assertSame(AlphaColorComponent.OPAQUE, color.alpha(), "alpha");
        assertEquals((RED.value << 16) + (GREEN.value << 8) + BLUE.value, color.rgb(), "rgb");
    }

    @Test
    public void testFromRgb() {
        final Color color = Color.fromRgb(0x010203);
        assertSame(RED, color.red(), "red");
        assertSame(GREEN, color.green(), "green");
        assertSame(BLUE, color.blue(), "blue");
        assertSame(AlphaColorComponent.OPAQUE, color.alpha(), "alpha");
        assertEquals(0x010203, color.rgb(), "rgb");
    }

    @Test
    public void testRgbAndArgbAndValue() {
        final Color color = OpaqueColor.createOpaqueColor(RedColorComponent.with((byte) 0x80),
                GreenColorComponent.with((byte) 0x81),
                BlueColorComponent.with((byte) 0x82));
        assertEquals(0x808182, color.rgb(), "rgb");
        assertEquals(0xFF808182, color.argb(), "argb");
        assertEquals(0x808182, color.value(), "value");
    }

    @Override
    public void testHasAlpha() {
        final OpaqueColor color = OpaqueColor.createOpaqueColor(RED, GREEN, BLUE);
        assertFalse(color.hasAlpha(), color + " has no alpha");
    }

    @Test
    public void testWithAlpha() {
        final Color color = this.createColorHslOrHsv();
        this.checkNotEquals(AlphaColor.with(color.red(), color.green(), color.blue(), AlphaColorComponent.with((byte) 4)));
    }

    @Test
    public void testToAwtColor() {
        final java.awt.Color color = Color.fromRgb(0x010203).toAwtColor();
        assertEquals(1, color.getRed(), "red");
        assertEquals(2, color.getGreen(), "green");
        assertEquals(3, color.getBlue(), "blue");
    }

    // HasJsonNode............................................................................................

    @Test
    public void testFromJsonNodeString() {
        this.fromJsonNodeAndCheck(JsonNode.string("#123456"),
                Cast.to(Color.fromRgb(0x123456)));
    }

    @Test
    public void testToJsonNode() {
        this.toJsonNodeAndCheck(Color.fromRgb(0x123456),
                JsonNode.string("#123456"));
    }

    // Object............................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(Color.fromRgb(0x010203), "#010203");
    }

    @Override
    OpaqueColor createColorHslOrHsv(final RedColorComponent red,
                                    final GreenColorComponent green,
                                    final BlueColorComponent blue) {
        return OpaqueColor.createOpaqueColor(red, green, blue);
    }

    // ClassTesting ...................................................................................................

    @Override
    public Class<OpaqueColor> type() {
        return OpaqueColor.class;
    }

    // SerializableTesting ............................................................................................

    @Override
    public OpaqueColor serializableInstance() {
        final byte value = 1;
        return OpaqueColor.createOpaqueColor(RedColorComponent.with(value),
                GreenColorComponent.with(value),
                BlueColorComponent.with(value));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
