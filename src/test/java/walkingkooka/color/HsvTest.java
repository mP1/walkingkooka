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
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HsvTest extends ColorHslOrHsvTestCase<Hsv> implements ParseStringTesting<Hsv> {

    // constants

    private final static HueHsvComponent HUE = HueHsvComponent.with(359);
    private final static SaturationHsvComponent SATURATION = SaturationHsvComponent.with(0.25f);
    private final static ValueHsvComponent VALUE = ValueHsvComponent.with(0.5f);

    // tests

    @Test
    public void testWithNullHueFails() {
        assertThrows(NullPointerException.class, () -> {
            Hsv.with(null, SATURATION, VALUE);
        });
    }

    @Test
    public void testWithNullSaturationFails() {
        assertThrows(NullPointerException.class, () -> {
            Hsv.with(HUE, null, VALUE);
        });
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Hsv.with(HUE, SATURATION, null);
        });
    }

    @Test
    public void testWith() {
        this.check(Hsv.with(HUE, SATURATION, VALUE), HUE, SATURATION, VALUE);
    }

    @Test
    public void testSetNullComponentFails() {
        assertThrows(NullPointerException.class, () -> {
            Hsv.with(HUE, SATURATION, VALUE).set(null);
        });
    }

    @Test
    public void testSetSameHue() {
        final Hsv hsv = Hsv.with(HUE, SATURATION, VALUE);

        this.check(Hsv.with(HUE, SATURATION, VALUE).set(HUE), HUE, SATURATION, VALUE);
        this.check(hsv, HUE, SATURATION, VALUE);
    }

    @Test
    public void testSetDifferentHue() {
        final HueHsvComponent different = HueHsvComponent.with(180);
        final Hsv hsv = Hsv.with(HUE, SATURATION, VALUE);

        this.check(Hsv.with(HUE, SATURATION, VALUE).set(different), different, SATURATION, VALUE);
        this.check(hsv, HUE, SATURATION, VALUE);
    }

    @Test
    public void testSetSameSaturation() {
        final Hsv hsv = Hsv.with(HUE, SATURATION, VALUE);
        assertSame(hsv, hsv.set(SATURATION));

        this.check(Hsv.with(HUE, SATURATION, VALUE).set(VALUE), HUE, SATURATION, VALUE);
        this.check(hsv, HUE, SATURATION, VALUE);
    }

    @Test
    public void testSetDifferentSaturation() {
        final SaturationHsvComponent different = SaturationHsvComponent.with(0.5f);
        final Hsv hsv = Hsv.with(HUE, SATURATION, VALUE);

        this.check(Hsv.with(HUE, SATURATION, VALUE).set(different), HUE, different, VALUE);
        this.check(hsv, HUE, SATURATION, VALUE);
    }

    @Test
    public void testSetSameValue() {
        final Hsv hsv = Hsv.with(HUE, SATURATION, VALUE);
        this.check(Hsv.with(HUE, SATURATION, VALUE).set(VALUE), HUE, SATURATION, VALUE);
        this.check(hsv, HUE, SATURATION, VALUE);
    }

    @Test
    public void testSetDifferentValue() {
        final ValueHsvComponent different = ValueHsvComponent.with(0.99f);
        this.check(Hsv.with(HUE, SATURATION, VALUE).set(different), HUE, SATURATION, different);
    }

    private void check(final Hsv hsv,
                       final HueHsvComponent hue,
                       final SaturationHsvComponent saturation,
                       final ValueHsvComponent value) {
        assertSame(hue, hsv.hue, "hue");
        assertSame(saturation, hsv.saturation, "saturation");
        assertSame(value, hsv.value, "value");
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
            assertEquals(expected, actual, () -> "failed to convert " + hsv + " to a Color");
        }
    }

    private boolean isEquals(final ColorComponent expected, final ColorComponent actual) {
        return Math.abs(expected.value - actual.value) < 3;
    }

    @Test
    public void testEqualsDifferentHue() {
        this.checkNotEquals(Hsv.with(HueHsvComponent.with(99), SATURATION, VALUE));
    }

    @Test
    public void testEqualsDifferentSaturation() {
        this.checkNotEquals(Hsv.with(HUE, SaturationHsvComponent.with(0.99f), VALUE));
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(Hsv.with(HUE, SATURATION, ValueHsvComponent.with(0.99f)));
    }

    @Test
    public void testParse() {
        this.parseAndCheck("hsv(359,100%,50%)",
                Hsv.with(HsvComponent.hue(359),
                        HsvComponent.saturation(1.0f),
                        HsvComponent.value(0.5f)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(Hsv.with(HUE, SATURATION, VALUE),
                "hsv(359,25%,50%)");
    }

    @Test
    public void testToStringZeroes() {
        this.toStringAndCheck(Hsv.with(HsvComponent.hue(0),
                HsvComponent.saturation(0),
                HsvComponent.value(0)),
                "hsv(0,0%,0%)");
    }

    @Override
    Hsv createColorHslOrHsv() {
        return Hsv.with(HUE, SATURATION, VALUE);
    }

    // ClassTesting ...................................................................................................

    @Override
    public Class<Hsv> type() {
        return Hsv.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HasJsonNodeTesting..............................................................................................

    @Override
    public Hsv fromJsonNode(final JsonNode from) {
        return Hsv.fromJsonNodeHsv(from);
    }

    // ParseStringTesting .............................................................................................

    @Override
    public Hsv parse(final String text) {
        return Hsv.parseHsv(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    // SerializableTesting ............................................................................................

    @Override
    public Hsv serializableInstance() {
        return Hsv.with(HueHsvComponent.with(180), SaturationHsvComponent.with(0.5f), ValueHsvComponent.with(0.5f));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
