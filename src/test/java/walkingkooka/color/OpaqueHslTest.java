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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OpaqueHslTest extends HslTestCase<OpaqueHsl> {

    // tests

    @Test
    public void testWithNullHueFails() {
        assertThrows(NullPointerException.class, () -> {
            OpaqueHsl.with(null, SATURATION, LIGHTNESS);
        });
    }

    @Test
    public void testWithNullSaturationFails() {
        assertThrows(NullPointerException.class, () -> {
            OpaqueHsl.with(HUE, null, LIGHTNESS);
        });
    }

    @Test
    public void testWithNullLightnessFails() {
        assertThrows(NullPointerException.class, () -> {
            OpaqueHsl.with(HUE, SATURATION, null);
        });
    }

    @Test
    public void testWith() {
        final OpaqueHsl hsl = OpaqueHsl.withOpaque(HUE, SATURATION, LIGHTNESS);
        assertSame(HUE, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(this.alphaHslComponent(), hsl.alpha(), "alpha");
    }

    @Override
    void toColorAndCheck(final float hue, final float saturation, final float value, final int rgb) {
        final OpaqueHsl hsl = OpaqueHsl.withOpaque(HslComponent.hue(hue), HslComponent.saturation(saturation),
                HslComponent.lightness(value));
        final Color expected = Color.fromRgb(rgb);
        final Color actual = hsl.toColor();
        if ((false == this.isEquals(expected.red, actual.red)) || //
                (false == this.isEquals(expected.green, actual.green)) || //
                (false == this.isEquals(expected.blue, actual.blue))) {
            assertEquals(expected, actual, "failed to convert " + hsl + " to a Color");
        }
    }

    @Test
    public void testParse() {
        this.parseAndCheck("hsl(359,100%,50%)",
                OpaqueHsl.withOpaque(HslComponent.hue(359),
                        HslComponent.saturation(1.0f),
                        HslComponent.lightness(0.5f)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(OpaqueHsl.withOpaque(HUE, SATURATION, LIGHTNESS),
                "hsl(359,50%,25%)");
    }

    @Test
    public void testToStringZeroes() {
        this.toStringAndCheck(OpaqueHsl.withOpaque(HslComponent.hue(0), HslComponent.saturation(0), HslComponent.lightness(0)),
                "hsl(0,0%,0%)");
    }

    @Override
    OpaqueHsl createHsl(final HueHslComponent hue,
                        final SaturationHslComponent saturation,
                        final LightnessHslComponent lightness) {
        return OpaqueHsl.withOpaque(hue, saturation, lightness);
    }

    @Override
    AlphaHslComponent alphaHslComponent() {
        return AlphaHslComponent.OPAQUE;
    }

    @Override
    Class<OpaqueHsl> hslType() {
        return OpaqueHsl.class;
    }

    // ParseStringTesting .............................................................................................

    @Override
    public OpaqueHsl parse(final String text) {
        return Cast.to(Hsl.parseHsl(text));
    }
}
