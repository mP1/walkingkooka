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

public final class AlphaHslTest extends HslTestCase<AlphaHsl> {

    @Test
    public void testWith() {
        final AlphaHslComponent alpha = this.alphaHslComponent();
        final Hsl hsl = AlphaHsl.withAlpha(HUE, SATURATION, LIGHTNESS, alpha);
        assertSame(HUE, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(alpha, hsl.alpha(), "alpha");
    }

    @Override
    void toColorAndCheck(final float hue, final float saturation, final float value, final int rgb) {
        final float alpha = 0.5f;

        final AlphaHsl hsla = AlphaHsl.withAlpha(HslComponent.hue(hue),
                HslComponent.saturation(saturation),
                HslComponent.lightness(value),
                HslComponent.alpha(alpha));
        final Color expected = Color.fromRgb(rgb)
                .set(ColorComponent.alpha(ColorComponent.toByte(alpha)));
        final Color actual = hsla.toColor();
        if ((false == this.isEquals(expected.red, actual.red)) || //
                (false == this.isEquals(expected.green, actual.green)) || //
                (false == this.isEquals(expected.blue, actual.blue) ||
                        (false == this.isEquals(expected.alpha(), actual.alpha())))) {
            assertEquals(expected, actual, "failed to convert " + hsla + " to a Color");
        }
    }

    @Test
    public void testParse() {
        this.parseAndCheck("hsla(359,100%,50%,25%)",
                AlphaHsl.withAlpha(HslComponent.hue(359),
                        HslComponent.saturation(1.0f),
                        HslComponent.lightness(0.5f),
                        HslComponent.alpha(0.25f)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(AlphaHsl.withAlpha(HUE, SATURATION, LIGHTNESS, HslComponent.alpha(0.75f)),
                "hsla(359,50%,25%,75%)");
    }

    @Test
    public void testToStringZeroes() {
        this.toStringAndCheck(AlphaHsl.withAlpha(HslComponent.hue(0),
                HslComponent.saturation(0),
                HslComponent.lightness(0),
                HslComponent.alpha(0)),
                "hsla(0,0%,0%,0%)");
    }

    @Override
    AlphaHsl createHsl(final HueHslComponent hue,
                       final SaturationHslComponent saturation,
                       final LightnessHslComponent lightness) {
        return AlphaHsl.withAlpha(hue, saturation, lightness, this.alphaHslComponent());
    }

    @Override
    AlphaHslComponent alphaHslComponent() {
        return this.alphaHslComponent;
    }

    private final AlphaHslComponent alphaHslComponent = AlphaHslComponent.with(0.5f);

    @Override
    Class<AlphaHsl> hslType() {
        return AlphaHsl.class;
    }

    // ParseStringTesting .............................................................................................

    @Override
    public AlphaHsl parse(final String text) {
        return Cast.to(Hsl.parseHsl(text));
    }
}
