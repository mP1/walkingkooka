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
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HsvTest extends ClassTestCase<Hsv> {

    // constants

    private final static HueHsvComponent HUE = HueHsvComponent.with(0);
    private final static SaturationHsvComponent SATURATION = SaturationHsvComponent.with(0);
    private final static ValueHsvComponent VALUE = ValueHsvComponent.with(0);

    // tests

    @Test(expected = NullPointerException.class)
    public void testWithNullHueFails() {
        Hsv.with(null, HsvTest.SATURATION, HsvTest.VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullSaturationFails() {
        Hsv.with(HsvTest.HUE, null, HsvTest.VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        Hsv.with(HsvTest.HUE, HsvTest.SATURATION, null);
    }

    @Test
    public void testWith() {
        final Hsv hsv = Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE);
        assertSame("hue", HsvTest.HUE, hsv.hue);
        assertSame("saturation", HsvTest.SATURATION, hsv.saturation);
        assertSame("value", HsvTest.VALUE, hsv.value);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullComponentFails() {
        Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE).set(null);
    }

    @Test
    public void testSetSameHue() {
        final Hsv hsv = Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE);
        assertSame(hsv, hsv.set(HsvTest.HUE));
        assertSame("hue", HsvTest.HUE, hsv.hue);
        assertSame("saturation", HsvTest.SATURATION, hsv.saturation);
        assertSame("value", HsvTest.VALUE, hsv.value);
    }

    @Test
    public void testSetDifferentHue() {
        final HueHsvComponent different = HueHsvComponent.with(180);
        final Hsv hsv = Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE).set(different);
        assertSame("hue", different, hsv.hue);
        assertSame("saturation", HsvTest.SATURATION, hsv.saturation);
        assertSame("value", HsvTest.VALUE, hsv.value);
    }

    @Test
    public void testSetSameSaturation() {
        final Hsv hsv = Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE);
        assertSame(hsv, hsv.set(HsvTest.SATURATION));
        assertSame("hue", HsvTest.HUE, hsv.hue);
        assertSame("saturation", HsvTest.SATURATION, hsv.saturation);
        assertSame("value", HsvTest.VALUE, hsv.value);
    }

    @Test
    public void testSetDifferentSaturation() {
        final HsvComponent different = SaturationHsvComponent.with(0.5f);
        final Hsv hsv = Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE).set(different);
        assertSame("hue", HsvTest.HUE, hsv.hue);
        assertSame("saturation", different, hsv.saturation);
        assertSame("value", HsvTest.VALUE, hsv.value);
    }

    @Test
    public void testSetSameValue() {
        final Hsv hsv = Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE);
        assertSame(hsv, hsv.set(HsvTest.VALUE));
        assertSame("hue", HsvTest.HUE, hsv.hue);
        assertSame("saturation", HsvTest.SATURATION, hsv.saturation);
        assertSame("value", HsvTest.VALUE, hsv.value);
    }

    @Test
    public void testSetDifferentValue() {
        final HsvComponent different = ValueHsvComponent.with(0.5f);
        final Hsv hsv = Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE).set(different);
        assertSame("hue", HsvTest.HUE, hsv.hue);
        assertSame("saturation", HsvTest.SATURATION, hsv.saturation);
        assertSame("value", different, hsv.value);
    }

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
    public void testRedToHsv() {
        this.toColorAndCheck(0f, 1.0f, 1.0f, 0xFF0000);
    }

    @Test
    public void testReddishToHsv() {
        this.toColorAndCheck(352f, 0.996f, 1.0f, 0xFF0123);
    }

    @Test
    public void testGreenToHsv() {
        this.toColorAndCheck(120f, 1.0f, 1.0f, 0x00FF00);
    }

    @Test
    public void testGreenishToHsv() {
        this.toColorAndCheck(133f, 0.929f, 0.996f, 0x12FE45);
    }

    @Test
    public void testBlueToHsv() {
        this.toColorAndCheck(240f, 1.0f, 1.0f, 0x0000FF);
    }

    @Test
    public void testBlueishToHsv() {
        this.toColorAndCheck(231f, 0.927f, 0.961f, 0x1234F5);
    }

    @Test
    public void testYellowToHsv() {
        this.toColorAndCheck(60f, 1.0f, 1.0f, 0xFFFF00);
    }

    @Test
    public void testYellowishToHsv() {
        this.toColorAndCheck(52f, 0.996f, 0.996f, 0xFEDC01);
    }

    @Test
    public void testPurpleToHsv() {
        this.toColorAndCheck(300f, 1.0f, 1.0f, 0xFF00FF);
    }

    @Test
    public void testPurplishToHsv() {
        this.toColorAndCheck(309f, 0.929f, 0.996f, 0xFE12DC);
    }

    @Test
    public void testCyanToHsv() {
        this.toColorAndCheck(180f, 1.0f, 1.0f, 0x00FFFF);
    }

    public void testCyanishToHsv() {
        this.toColorAndCheck(189f, 1.0f, 0.871f, 0x00BCDE);
    }

    private void toColorAndCheck(final float hue, final float saturation, final float value, final int rgb) {
        final Hsv hsv = Hsv.with(HsvComponent.hue(hue), HsvComponent.saturation(saturation), HsvComponent.value(value));
        final Color expected = Color.fromRgb(rgb);
        final Color actual = hsv.toColor();
        if ((false == this.isEquals(expected.red, actual.red)) || (false == this.isEquals(expected.green, actual.green))
                || (false == this.isEquals(expected.blue, actual.blue))) {
            assertEquals("failed to convert " + hsv + " to a Color", expected, actual);
        }
    }

    private boolean isEquals(final ColorComponent expected, final ColorComponent actual) {
        return Math.abs(expected.value - actual.value) < 3;
    }

    @Test
    public void testToString() {
        assertEquals(HsvTest.HUE + "," + HsvTest.SATURATION + "," + HsvTest.VALUE,
                Hsv.with(HsvTest.HUE, HsvTest.SATURATION, HsvTest.VALUE).toString());
    }

    @Override
    protected Class<Hsv> type() {
        return Hsv.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
