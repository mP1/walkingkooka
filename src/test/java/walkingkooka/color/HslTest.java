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
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HslTest extends ClassTestCase<Hsl> implements HashCodeEqualsDefinedTesting<Hsl>, SerializationTesting<Hsl> {

    // constants

    private final static HueHslComponent HUE = HueHslComponent.with(0);
    private final static SaturationHslComponent SATURATION = SaturationHslComponent.with(0);
    private final static LightnessHslComponent LIGHTNESS = LightnessHslComponent.with(0);

    // tests

    @Test(expected = NullPointerException.class)
    public void testWithNullHueFails() {
        Hsl.with(null, SATURATION, LIGHTNESS);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullSaturationFails() {
        Hsl.with(HUE, null, LIGHTNESS);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullLightnessFails() {
        Hsl.with(HUE, SATURATION, null);
    }

    @Test
    public void testWith() {
        final Hsl hsl = Hsl.with(HUE, SATURATION, LIGHTNESS);
        assertSame("hue", HUE, hsl.hue);
        assertSame("saturation", SATURATION, hsl.saturation);
        assertSame("lightness", LIGHTNESS, hsl.lightness);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullComponentFails() {
        Hsl.with(HUE, SATURATION, LIGHTNESS).set(null);
    }

    @Test
    public void testSetSameHue() {
        final Hsl hsl = Hsl.with(HUE, SATURATION, LIGHTNESS);
        assertSame(hsl, hsl.set(HUE));
        assertSame("hue", HUE, hsl.hue);
        assertSame("saturation", SATURATION, hsl.saturation);
        assertSame("lightness", LIGHTNESS, hsl.lightness);
    }

    @Test
    public void testSetDifferentHue() {
        final HueHslComponent different = HueHslComponent.with(180);
        final Hsl hsl = Hsl.with(HUE, SATURATION, LIGHTNESS).set(different);
        assertSame("hue", different, hsl.hue);
        assertSame("saturation", SATURATION, hsl.saturation);
        assertSame("lightness", LIGHTNESS, hsl.lightness);
    }

    @Test
    public void testSetSameSaturation() {
        final Hsl hsl = Hsl.with(HUE, SATURATION, LIGHTNESS);
        assertSame(hsl, hsl.set(SATURATION));
        assertSame("hue", HUE, hsl.hue);
        assertSame("saturation", SATURATION, hsl.saturation);
        assertSame("lightness", LIGHTNESS, hsl.lightness);
    }

    @Test
    public void testSetDifferentSaturation() {
        final HslComponent different = SaturationHslComponent.with(0.5f);
        final Hsl hsl = Hsl.with(HUE, SATURATION, LIGHTNESS).set(different);
        assertSame("hue", HUE, hsl.hue);
        assertSame("saturation", different, hsl.saturation);
        assertSame("lightness", LIGHTNESS, hsl.lightness);
    }

    @Test
    public void testSetSameLightness() {
        final Hsl hsl = Hsl.with(HUE, SATURATION, LIGHTNESS);
        assertSame(hsl, hsl.set(LIGHTNESS));
        assertSame("hue", HUE, hsl.hue);
        assertSame("saturation", SATURATION, hsl.saturation);
        assertSame("lightness", LIGHTNESS, hsl.lightness);
    }

    @Test
    public void testSetDifferentLightness() {
        final HslComponent different = LightnessHslComponent.with(0.5f);
        final Hsl hsl = Hsl.with(HUE, SATURATION, LIGHTNESS).set(different);
        assertSame("hue", HUE, hsl.hue);
        assertSame("saturation", SATURATION, hsl.saturation);
        assertSame("lightness", different, hsl.lightness);
    }

    // toHsl http://web.forret.com/tools/color.asp
    // http://serennu.com/colour/hsltorgb.php

    @Test
    public void testBlackToColor() {
        this.toColorAndCheck(0, 0, 0, 0);
    }

    @Test
    public void testWhiteToColor() {
        this.toColorAndCheck(0, 0, 1.0f, 0xFFFFFF);
    }

    @Test
    public void testGrayToColor() {
        this.toColorAndCheck(0, 0, 0.533f, 0x888888);
    }

    @Test
    public void testRedToHsl() {
        this.toColorAndCheck(0f, 1.0f, 1.0f, 0xFF0000);
    }

    @Test
    public void testReddishToHsl() {
        this.toColorAndCheck(352f, 0.996f, 0.502f, 0xFF0123);
    }

    @Test
    public void testGreenToHsl() {
        this.toColorAndCheck(120f, 1.0f, 1.0f, 0x00FF00);
    }

    @Test
    public void testGreenishToHsl() {
        this.toColorAndCheck(133f, 0.925f, 0.533f, 0x19F549);
    }

    @Test
    public void testBlueToHsl() {
        this.toColorAndCheck(240f, 1.0f, 1.0f, 0x0000FF);
    }

    @Test
    public void testBlueishToHsl() {
        this.toColorAndCheck(231f, 0.89f, 0.516f, 0x1838F2);
    }

    @Test
    public void testYellowToHsl() {
        this.toColorAndCheck(60f, 1.0f, 1.0f, 0xFFFF00);
    }

    @Test
    public void testYellowishToHsl() {
        this.toColorAndCheck(52f, 0.992f, 0.5f, 0xFEDC01);
    }

    @Test
    public void testPurpleToHsl() {
        this.toColorAndCheck(300f, 1.0f, 1.0f, 0xFF00FF);
    }

    @Test
    public void testPurplishToHsl() {
        this.toColorAndCheck(309f, 0.99f, 0.533f, 0xFE12DC);
    }

    @Test
    public void testCyanToHsl() {
        this.toColorAndCheck(180f, 1.0f, 1.0f, 0x00FFFF);
    }

    @Test
    public void testCyanishToHsl() {
        this.toColorAndCheck(189f, 1, 0.435f, 0x00BCDE);
    }

    private void toColorAndCheck(final float hue, final float saturation, final float value, final int rgb) {
        final Hsl hsl = Hsl.with(HslComponent.hue(hue), HslComponent.saturation(saturation),
                HslComponent.lightness(value));
        final Color expected = Color.fromRgb(rgb);
        final Color actual = hsl.toColor();
        if ((false == this.isEquals(expected.red, actual.red)) || //
                (false == this.isEquals(expected.green, actual.green)) || //
                (false == this.isEquals(expected.blue, actual.blue))) {
            assertEquals("failed to convert " + hsl + " to a Color", expected, actual);
        }
    }

    private boolean isEquals(final ColorComponent expected, final ColorComponent actual) {
        return Math.abs(expected.value - actual.value) < 5;
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentHue() {
        this.checkNotEquals(Hsl.with(HueHslComponent.with(180), SATURATION, LIGHTNESS));
    }

    @Test
    public void testEqualsDifferentSaturation() {
        this.checkNotEquals(Hsl.with(HUE, SaturationHslComponent.with(0.5f), LIGHTNESS));
    }

    @Test
    public void testEqualsDifferentLightness() {
        this.checkNotEquals(Hsl.with(HUE, SATURATION, LightnessHslComponent.with(0.5f)));
    }

    public void testToString() {
        assertEquals(HUE + "," + SATURATION + "," + LIGHTNESS,
                Hsl.with(HUE, SATURATION, LIGHTNESS).toString());
    }

    @Override
    public Class<Hsl> type() {
        return Hsl.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public Hsl createObject() {
        return Hsl.with(HUE, SATURATION, LIGHTNESS);
    }

    @Override
    public Hsl serializableInstance() {
        return Hsl.with(HueHslComponent.with(180), SaturationHslComponent.with(0.5f), LightnessHslComponent.with(0.5f));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
