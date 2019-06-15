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
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HslTestCase<H extends Hsl> extends ColorHslOrHsvTestCase<Hsl> implements ParseStringTesting<H> {

    HslTestCase() {
        super();
    }

    // constants

    final static HueHslComponent HUE = HueHslComponent.with(359);
    final static SaturationHslComponent SATURATION = SaturationHslComponent.with(0.5f);
    final static LightnessHslComponent LIGHTNESS = LightnessHslComponent.with(0.25f);

    // tests

    @Test
    public final void testSetNullComponentFails() {
        assertThrows(NullPointerException.class, () -> {
            Hsl.with(HUE, SATURATION, LIGHTNESS).set(null);
        });
    }

    @Test
    public final void testSetSameHue() {
        final Hsl hsl = this.createHsl();
        assertSame(hsl, hsl.set(HUE));
        assertSame(HUE, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(this.alphaHslComponent(), hsl.alpha(), "alpha");
    }

    @Test
    public final void testSetDifferentHue() {
        final HueHslComponent different = HueHslComponent.with(180);
        final Hsl hsl = this.createHsl().set(different);
        assertSame(different, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(this.alphaHslComponent(), hsl.alpha(), "alpha");
    }

    @Test
    public final void testSetSameSaturation() {
        final Hsl hsl = this.createHsl();
        assertSame(hsl, hsl.set(SATURATION));
        assertSame(HUE, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(this.alphaHslComponent(), hsl.alpha(), "alpha");
    }

    @Test
    public final void testSetDifferentSaturation() {
        final HslComponent different = SaturationHslComponent.with(0.99f);
        final Hsl hsl = this.createHsl().set(different);
        assertSame(HUE, hsl.hue, "hue");
        assertSame(different, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(this.alphaHslComponent(), hsl.alpha(), "alpha");
    }

    @Test
    public final void testSetSameLightness() {
        final Hsl hsl = this.createHsl();
        assertSame(hsl, hsl.set(LIGHTNESS));
        assertSame(HUE, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(this.alphaHslComponent(), hsl.alpha(), "alpha");
    }

    @Test
    public final void testSetDifferentLightness() {
        final HslComponent different = LightnessHslComponent.with(0.5f);
        final Hsl hsl = this.createHsl().set(different);
        assertSame(HUE, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(different, hsl.lightness, "lightness");
        assertSame(this.alphaHslComponent(), hsl.alpha(), "alpha");
    }

    @Test
    public final void testSetAlphaSame() {
        final Hsl hsl = this.createHsl();
        assertSame(hsl, hsl.set(hsl.alpha()));
    }

    @Test
    public final void testSetAlphaDifferent() {
        final HslComponent different = AlphaHslComponent.with(0.25f);
        final Hsl hsl = this.createHsl().set(different);
        assertSame(HUE, hsl.hue, "hue");
        assertSame(SATURATION, hsl.saturation, "saturation");
        assertSame(LIGHTNESS, hsl.lightness, "lightness");
        assertSame(different, hsl.alpha(), "alpha");
    }

    // toHsl http://web.forret.com/tools/color.asp
    // http://serennu.com/colour/hsltorgb.php

    @Test
    public final void testBlackToColor() {
        this.toColorAndCheck(0, 0, 0, 0);
    }

    @Test
    public final void testWhiteToColor() {
        this.toColorAndCheck(0, 0, 1.0f, 0xFFFFFF);
    }

    @Test
    public final void testGrayToColor() {
        this.toColorAndCheck(0, 0, 0.533f, 0x888888);
    }

    @Test
    public final void testRedToHsl() {
        this.toColorAndCheck(0f, 1.0f, 1.0f, 0xFF0000);
    }

    @Test
    public final void testReddishToHsl() {
        this.toColorAndCheck(352f, 0.996f, 0.502f, 0xFF0123);
    }

    @Test
    public final void testGreenToHsl() {
        this.toColorAndCheck(120f, 1.0f, 1.0f, 0x00FF00);
    }

    @Test
    public final void testGreenishToHsl() {
        this.toColorAndCheck(133f, 0.925f, 0.533f, 0x19F549);
    }

    @Test
    public final void testBlueToHsl() {
        this.toColorAndCheck(240f, 1.0f, 1.0f, 0x0000FF);
    }

    @Test
    public final void testBlueishToHsl() {
        this.toColorAndCheck(231f, 0.89f, 0.516f, 0x1838F2);
    }

    @Test
    public final void testYellowToHsl() {
        this.toColorAndCheck(60f, 1.0f, 1.0f, 0xFFFF00);
    }

    @Test
    public final void testYellowishToHsl() {
        this.toColorAndCheck(52f, 0.992f, 0.5f, 0xFEDC01);
    }

    @Test
    public final void testPurpleToHsl() {
        this.toColorAndCheck(300f, 1.0f, 1.0f, 0xFF00FF);
    }

    @Test
    public final void testPurplishToHsl() {
        this.toColorAndCheck(309f, 0.99f, 0.533f, 0xFE12DC);
    }

    @Test
    public final void testCyanToHsl() {
        this.toColorAndCheck(180f, 1.0f, 1.0f, 0x00FFFF);
    }

    @Test
    public final void testCyanishToHsl() {
        this.toColorAndCheck(189f, 1, 0.435f, 0x00BCDE);
    }

    abstract void toColorAndCheck(final float hue,
                                  final float saturation,
                                  final float value,
                                  final int rgb);

    final boolean isEquals(final ColorComponent expected, final ColorComponent actual) {
        return Math.abs(expected.value - actual.value) < 5;
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public final void testEqualsDifferentHue() {
        this.checkNotEquals(Hsl.with(HueHslComponent.with(99), SATURATION, LIGHTNESS));
    }

    @Test
    public final void testEqualsDifferentSaturation() {
        this.checkNotEquals(Hsl.with(HUE, SaturationHslComponent.with(0.99f), LIGHTNESS));
    }

    @Test
    public final void testEqualsDifferentLightness() {
        this.checkNotEquals(Hsl.with(HUE, SATURATION, LightnessHslComponent.with(0.99f)));
    }

    @Override
    final Hsl createColorHslOrHsv() {
        return this.createHsl();
    }

    final Hsl createHsl() {
        return this.createHsl(HUE, SATURATION, LIGHTNESS);
    }

    abstract H createHsl(final HueHslComponent hue,
                         final SaturationHslComponent saturation,
                         final LightnessHslComponent lightness);

    abstract AlphaHslComponent alphaHslComponent();

    // ClassTesting ...................................................................................................

    @Override
    public final Class<Hsl> type() {
        return Cast.to(this.hslType());
    }

    abstract Class<H> hslType();

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // HasJsonNodeTesting..............................................................................................

    @Override
    public final Hsl fromJsonNode(final JsonNode from) {
        return Hsl.fromJsonNodeHsl(from);
    }

    // ParseStringTesting .............................................................................................

    @Override
    public final RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public final Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    // SerializableTesting ............................................................................................

    @Override
    public final Hsl serializableInstance() {
        return this.createHsl(HueHslComponent.with(180),
                SaturationHslComponent.with(0.5f),
                LightnessHslComponent.with(0.5f));
    }

    @Override
    public final boolean serializableInstanceIsSingleton() {
        return false;
    }
}
