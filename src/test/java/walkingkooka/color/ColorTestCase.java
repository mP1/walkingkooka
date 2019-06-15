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
import walkingkooka.Equality;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class ColorTestCase<C extends Color> extends ColorHslOrHsvTestCase<C> implements HasJsonNodeTesting<C>,
        TypeNameTesting<C> {

    ColorTestCase() {
        super();
    }

    // constants

    final static RedColorComponent RED = ColorComponent.red((byte) 1);
    final static GreenColorComponent GREEN = ColorComponent.green((byte) 2);
    final static BlueColorComponent BLUE = ColorComponent.blue((byte) 3);
    final static AlphaColorComponent ALPHA = AlphaColorComponent.with((byte) 4);

    final static float AMOUNT = 0.25f;
    final static float REVERSE_AMOUNT = 1.0f - AMOUNT;

    final static byte FROM_COMPONENT = (byte) 16; // starting with
    final static byte TO_COMPONENT = (byte) 32; // mixed with
    final static byte MIXED_COMPONENT = (byte) 20; // expected result

    final static byte FROM_COMPONENT2 = (byte) ColorComponent.MAX_VALUE - 16; // starting with
    final static byte TO_COMPONENT2 = (byte) ColorComponent.MAX_VALUE - 32; // mixed with
    final static byte MIXED_COMPONENT2 = (byte) ColorComponent.MAX_VALUE - 20; // expected result

    final static byte DIFFERENT = 0;
    final static float SMALL_AMOUNT = 1.0f / 512f;
    final static float LARGE_AMOUNT = 1 - SMALL_AMOUNT;

    // tests

    // HasJsonNodeTesting..................................................................

    @Override
    public void testFromJsonNullFails() {
        throw new UnsupportedOperationException();
    }

    // tests ..................................................................................

    abstract public void testHasAlpha();

    @Test
    public final void testSetNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createColorHslOrHsv().set(null);
        });
    }

    // red

    @Test
    public final void testSetSameRed() {
        final C color = this.createColorHslOrHsv();
        assertSame(color, color.set(RED));
    }

    @Test
    public final void testSetDifferentRed() {
        final C first = this.createColorHslOrHsv(RED, GREEN, BLUE);

        final RedColorComponent different = ColorComponent.red((byte) 0xFF);
        final Color color = first.set(different);

        assertSame(different, color.red(), "red");
        assertSame(GREEN, color.green(), "green");
        assertSame(BLUE, color.blue(), "blue");
        assertSame(first.alpha(), color.alpha(), "alpha");
    }

    // green
    @Test
    public final void testSetSameGreen() {
        final C color = this.createColorHslOrHsv();
        assertSame(color, color.set(GREEN));
    }

    @Test
    public final void testSetDifferentGreen() {
        final C color = this.createColorHslOrHsv(RED, GREEN, BLUE);

        final GreenColorComponent different = ColorComponent.green((byte) 0xFF);
        this.check(color.set(different), RED, different, BLUE, color.alpha());
        this.check(color, RED, GREEN, BLUE, color.alpha());
    }

    // blue
    @Test
    public final void testSetSameBlue() {
        final C color = this.createColorHslOrHsv();
        assertSame(color, color.set(BLUE));
    }

    @Test
    public final void testSetDifferentBlue() {
        final C color = this.createColorHslOrHsv(RED, GREEN, BLUE);

        final BlueColorComponent different = ColorComponent.blue((byte) 0xFF);

        this.check(color.set(different),
                RED,
                GREEN,
                different,
                color.alpha());
        this.check(color, RED, GREEN, BLUE, color.alpha());
    }

    // alpha

    @Test
    public final void testSetDifferentAlpha() {
        final C color = this.createColorHslOrHsv(RED, GREEN, BLUE);

        final AlphaColorComponent different = AlphaColorComponent.with((byte) 0xFF);

        this.check(color.set(different),
                RED,
                GREEN,
                BLUE,
                different);
        this.check(color, RED, GREEN, BLUE, color.alpha());
    }

    @Test
    public final void testSetRedGreenBlue() {
        final byte zero = 0;
        final C color = this.createColorHslOrHsv(ColorComponent.red(zero),
                ColorComponent.green(zero),
                ColorComponent.blue(zero));
        this.check(color.set(RED).set(BLUE).setGreen(GREEN),
                RED,
                GREEN,
                BLUE,
                color.alpha());
    }

    @Test
    public final void testSetRedGreenBlueAlpha() {
        final byte zero = 0;
        final C color = this.createColorHslOrHsv(ColorComponent.red(zero), ColorComponent.green(zero),
                ColorComponent.blue(zero));
        this.check(color.set(RED).set(BLUE).setGreen(GREEN)
                        .setAlpha(ALPHA),
                RED,
                GREEN,
                BLUE,
                ALPHA);
    }

    private void check(final Color color,
                       final RedColorComponent red,
                       final GreenColorComponent green,
                       final BlueColorComponent blue,
                       final AlphaColorComponent alpha) {
        assertSame(red, color.red(), "red");
        assertSame(green, color.green(), "green");
        assertSame(blue, color.blue(), "blue");
        assertSame(alpha, color.alpha(), "alpha");
    }

    // mix

    @Test
    public void testMixNullComponentFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createColorHslOrHsv().mix(null, 1.0f);
        });
    }

    @Test
    public void testMixInvalidAmountBelowZeroFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createColorHslOrHsv().mix(RED, -0.1f);
        });
    }

    @Test
    public void testMixInvalidAmountAboveOneFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createColorHslOrHsv().mix(RED, +1.1f);
        });
    }

    // mix red

    @Test
    public final void testMixSameRed() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.red(), AMOUNT);
    }

    @Test
    public final void testMixSameRedVerySmallAmount() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.red(), SMALL_AMOUNT);
    }

    @Test
    public final void testMixDifferentRedVeryLargeAmount() {
        final RedColorComponent replacement = ColorComponent.red(DIFFERENT);
        this.mixAndCheck(this.createColorHslOrHsv(), replacement, LARGE_AMOUNT, replacement);
    }

    @Test
    public final void testMixRed1() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.red(FROM_COMPONENT)), //
                ColorComponent.red(TO_COMPONENT),//
                AMOUNT, //
                ColorComponent.red(MIXED_COMPONENT));
    }

    @Test
    public final void testMixRed2() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.red(TO_COMPONENT)), //
                ColorComponent.red(FROM_COMPONENT),//
                REVERSE_AMOUNT, //
                ColorComponent.red(MIXED_COMPONENT));
    }

    @Test
    public final void testMixRed3() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.red(FROM_COMPONENT2)), //
                ColorComponent.red(TO_COMPONENT2),//
                AMOUNT, //
                ColorComponent.red(MIXED_COMPONENT2));
    }

    @Test
    public final void testMixRed4() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.red(TO_COMPONENT2)), //
                ColorComponent.red(FROM_COMPONENT2),//
                REVERSE_AMOUNT, //
                ColorComponent.red(MIXED_COMPONENT2));
    }

    // mix green

    @Test
    public final void testMixSameGreen() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.green(), AMOUNT);
    }

    @Test
    public final void testMixSameGreenVeryLargeAmount() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.green(), LARGE_AMOUNT);
    }

    @Test
    public final void testMixDifferentGreenVeryLargeAmount() {
        final GreenColorComponent replacement = ColorComponent.green(DIFFERENT);
        this.mixAndCheck(replacement, LARGE_AMOUNT, replacement);
    }

    @Test
    public final void testMixGreen1() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.green(FROM_COMPONENT)), //
                ColorComponent.green(TO_COMPONENT),//
                AMOUNT, //
                ColorComponent.green(MIXED_COMPONENT));
    }

    @Test
    public final void testMixGreen2() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.green(TO_COMPONENT)), //
                ColorComponent.green(FROM_COMPONENT),//
                REVERSE_AMOUNT, //
                ColorComponent.green(MIXED_COMPONENT));
    }

    @Test
    public final void testMixGreen3() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.green(FROM_COMPONENT2)), //
                ColorComponent.green(TO_COMPONENT2),//
                AMOUNT, //
                ColorComponent.green(MIXED_COMPONENT2));
    }

    @Test
    public final void testMixGreen4() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.green(TO_COMPONENT2)), //
                ColorComponent.green(FROM_COMPONENT2),//
                REVERSE_AMOUNT, //
                ColorComponent.green(MIXED_COMPONENT2));
    }

    // mix blue

    @Test
    public final void testMixSameBlue() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.blue(), AMOUNT);
    }

    @Test
    public final void testMixSameBlueVerySmallAmount() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.blue(), SMALL_AMOUNT);
    }

    @Test
    public final void testMixDifferentBlueVerySmallAmount() {
        this.mixAndCheckSame(ColorComponent.blue(DIFFERENT), SMALL_AMOUNT);
    }

    @Test
    public final void testMixDifferentBlueVeryLargeAmount() {
        final BlueColorComponent replacement = ColorComponent.blue(DIFFERENT);
        this.mixAndCheck(replacement, LARGE_AMOUNT, replacement);
    }

    @Test
    public final void testMixBlue1() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.blue(FROM_COMPONENT)), //
                ColorComponent.blue(TO_COMPONENT),//
                AMOUNT, //
                ColorComponent.blue(MIXED_COMPONENT));
    }

    @Test
    public final void testMixBlue2() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.blue(TO_COMPONENT)), //
                ColorComponent.blue(FROM_COMPONENT),//
                REVERSE_AMOUNT, //
                ColorComponent.blue(MIXED_COMPONENT));
    }

    @Test
    public final void testMixBlue3() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.blue(FROM_COMPONENT2)), //
                ColorComponent.blue(TO_COMPONENT2),//
                AMOUNT, //
                ColorComponent.blue(MIXED_COMPONENT2));
    }

    @Test
    public final void testMixBlue4() {
        this.mixAndCheck(this.createColorHslOrHsv().set(ColorComponent.blue(TO_COMPONENT2)), //
                ColorComponent.blue(FROM_COMPONENT2),//
                REVERSE_AMOUNT, //
                ColorComponent.blue(MIXED_COMPONENT2));
    }

    // mix alpha

    @Test
    public final void testMixSameAlpha() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.alpha(), AMOUNT);
    }

    @Test
    public final void testMixSameAlphaVerySmallAmount() {
        final C color = this.createColorHslOrHsv();
        this.mixAndCheckSame(color, color.alpha(), SMALL_AMOUNT);
    }

    @Test
    public final void testMixDifferentAlphaVerySmallAmount() {
        this.mixAndCheckSame(AlphaColorComponent.with(DIFFERENT), SMALL_AMOUNT);
    }

    @Test
    public final void testMixAlpha1() {
        this.mixAndCheck(this.createColorHslOrHsv().set(AlphaColorComponent.with(FROM_COMPONENT)), //
                AlphaColorComponent.with(TO_COMPONENT),//
                AMOUNT, //
                AlphaColorComponent.with(MIXED_COMPONENT));
    }

    @Test
    public final void testMixAlpha2() {
        this.mixAndCheck(this.createColorHslOrHsv().set(AlphaColorComponent.with(TO_COMPONENT)), //
                AlphaColorComponent.with(FROM_COMPONENT),//
                REVERSE_AMOUNT, //
                AlphaColorComponent.with(MIXED_COMPONENT));
    }

    @Test
    public final void testMixAlpha3() {
        this.mixAndCheck(this.createColorHslOrHsv().set(AlphaColorComponent.with(FROM_COMPONENT2)), //
                AlphaColorComponent.with(TO_COMPONENT2),//
                AMOUNT, //
                AlphaColorComponent.with(MIXED_COMPONENT2));
    }

    @Test
    public final void testMixAlpha4() {
        this.mixAndCheck(this.createColorHslOrHsv().set(AlphaColorComponent.with(TO_COMPONENT2)), //
                AlphaColorComponent.with(FROM_COMPONENT2),//
                REVERSE_AMOUNT, //
                AlphaColorComponent.with(MIXED_COMPONENT2));
    }

    // toHsv http://web.forret.com/tools/color.asp

    @Test
    public final void testBlackToHsv() {
        this.toHsvAndCheck(0x000000, 0f, 0f, 0f);
    }

    @Test
    public final void testWhiteToHsv() {
        this.toHsvAndCheck(0xFFFFFF, 0f, 0f, 1.0f);
    }

    @Test
    public final void testGrayToHsv() {
        this.toHsvAndCheck(0x888888, 0f, 0f, 0.533f);
    }

    @Test
    public final void testRedToHsv() {
        this.toHsvAndCheck(0xFF0000, 0f, 1.0f, 1.0f);
    }

    @Test
    public final void testReddishToHsv() {
        this.toHsvAndCheck(0xFF0123, 352f, 0.996f, 1.0f);
    }

    @Test
    public final void testGreenToHsv() {
        this.toHsvAndCheck(0x00FF00, 120f, 1.0f, 1.0f);
    }

    @Test
    public final void testGreenishToHsv() {
        this.toHsvAndCheck(0x12FE45, 133f, 0.929f, 0.996f);
    }

    @Test
    public final void testBlueToHsv() {
        this.toHsvAndCheck(0x0000FF, 240f, 1.0f, 1.0f);
    }

    @Test
    public final void testBlueishToHsv() {
        this.toHsvAndCheck(0x1234F5, 231f, 0.927f, 0.961f);
    }

    @Test
    public final void testYellowToHsv() {
        this.toHsvAndCheck(0xFFFF00, 60f, 1.0f, 1.0f);
    }

    @Test
    public final void testYellowishToHsv() {
        this.toHsvAndCheck(0xFEDC01, 52f, 0.996f, 0.996f);
    }

    @Test
    public final void testPurpleToHsv() {
        this.toHsvAndCheck(0xFF00FF, 300f, 1.0f, 1.0f);
    }

    @Test
    public final void testPurplishToHsv() {
        this.toHsvAndCheck(0xFE12DC, 309f, 0.929f, 0.996f);
    }

    @Test
    public final void testCyanToHsv() {
        this.toHsvAndCheck(0x00FFFF, 180f, 1.0f, 1.0f);
    }

    @Test
    public final void testCyanishToHsv() {
        this.toHsvAndCheck(0x00BCDE, 189f, 1.0f, 0.871f);
    }

    // toHsl http://web.forret.com/tools/color.asp

    @Test
    public final void testBlackToHsl() {
        this.toHslAndCheck(0x000000, 0f, 0f, 0f);
    }

    @Test
    public final void testWhiteToHsl() {
        this.toHslAndCheck(0xFFFFFF, 0f, 0f, 1.0f);
    }

    @Test
    public final void testGrayToHsl() {
        this.toHslAndCheck(0x888888, 0f, 0f, 0.533f);
    }

    @Test
    public final void testRedToHsl() {
        this.toHslAndCheck(0xFF0000, 0f, 1.0f, 0.5f);
    }

    @Test
    public final void testReddishToHsl() {
        this.toHslAndCheck(0xFF0123, 352f, 0.996f, 0.502f);
    }

    @Test
    public final void testGreenToHsl() {
        this.toHslAndCheck(0x00FF00, 120f, 1.0f, 0.5f);
    }

    @Test
    public final void testGreenishToHsl() {
        this.toHslAndCheck(0x12FE45, 133f, 0.925f, 0.533f);
    }

    @Test
    public final void testBlueToHsl() {
        this.toHslAndCheck(0x0000FF, 240f, 1.0f, 0.5f);
    }

    @Test
    public final void testBlueishToHsl() {
        this.toHslAndCheck(0x1234F5, 231f, 0.89f, 0.516f);
    }

    @Test
    public final void testYellowToHsl() {
        this.toHslAndCheck(0xFFFF00, 60f, 1.0f, 0.5f);
    }

    @Test
    public final void testYellowishToHsl() {
        this.toHslAndCheck(0xFEDC01, 52f, 0.992f, 0.5f);
    }

    @Test
    public final void testPurpleToHsl() {
        this.toHslAndCheck(0xFF00FF, 300f, 1.0f, 0.5f);
    }

    @Test
    public final void testPurplishToHsl() {
        this.toHslAndCheck(0xFE12DC, 309f, 0.925f, 0.533f);
    }

    @Test
    public final void testCyanToHsl() {
        this.toHslAndCheck(0x00FFFF, 180f, 1.0f, 0.5f);
    }

    @Test
    public final void testCyanishToHsl() {
        this.toHslAndCheck(0x00BCDE, 189f, 0.871f, 0.435f);
    }

    // HasJsonNode.......................................................................................................

    @Test
    public final void testFromJsonNodeBooleanFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true));
    }

    @Test
    public final void testFromJsonNodeNullFails() {
        this.fromJsonNodeFails(JsonNode.nullNode());
    }

    @Test
    public final void testFromJsonNodeNumberFails() {
        this.fromJsonNodeFails(JsonNode.number(123));
    }

    @Test
    public final void testFromJsonNodeArrayFails() {
        this.fromJsonNodeFails(JsonNode.array());
    }

    @Test
    public final void testFromJsonNodeObjectFails() {
        this.fromJsonNodeFails(JsonNode.object());
    }

    // helpers..........................................................................................................

    @Override
    final C createColorHslOrHsv() {
        return this.createColorHslOrHsv(RED, GREEN, BLUE);
    }

    abstract C createColorHslOrHsv(RedColorComponent red, GreenColorComponent green, BlueColorComponent blue);

    final void mixAndCheckSame(final ColorComponent mixed, final float amount) {
        this.mixAndCheckSame(this.createColorHslOrHsv(), mixed, amount);
    }

    final void mixAndCheckSame(final Color color, final ColorComponent mixed, final float amount) {
        final Color result = color.mix(mixed, amount);
        if (color != result) {
            assertSame(
                    color,
                    result,
                    "mixing " + color + " with " + toString(mixed) + " amount=" + amount
                            + " did not return the original color");
        }
    }

    final void mixAndCheck(final ColorComponent mixed,
                           final float amount,
                           final RedColorComponent red) {
        this.mixAndCheck(this.createColorHslOrHsv(), mixed, amount, red);
    }

    final void mixAndCheck(final Color color,
                           final ColorComponent mixed,
                           final float amount,
                           final RedColorComponent red) {
        this.mixAndCheck(color, mixed, amount, red, color.green(), color.blue(), color.alpha());
    }

    final void mixAndCheck(final ColorComponent mixed,
                           final float amount,
                           final GreenColorComponent green) {
        this.mixAndCheck(this.createColorHslOrHsv(), mixed, amount, green);
    }

    final void mixAndCheck(final Color color,
                           final ColorComponent mixed,
                           final float amount,
                           final GreenColorComponent green) {
        this.mixAndCheck(color, mixed, amount, color.red(), green, color.blue(), color.alpha());
    }

    final void mixAndCheck(final ColorComponent mixed,
                           final float amount,
                           final BlueColorComponent blue) {
        this.mixAndCheck(this.createColorHslOrHsv(), mixed, amount, blue);
    }

    final void mixAndCheck(final Color color,
                           final ColorComponent mixed,
                           final float amount,
                           final BlueColorComponent blue) {
        this.mixAndCheck(color, mixed, amount, color.red(), color.green(), blue, color.alpha());
    }

    final void mixAndCheck(final ColorComponent mixed,
                           final float amount,
                           final AlphaColorComponent alpha) {
        this.mixAndCheck(this.createColorHslOrHsv(), mixed, amount, alpha);
    }

    final void mixAndCheck(final Color color,
                           final ColorComponent mixed,
                           final float amount,
                           final AlphaColorComponent alpha) {
        this.mixAndCheck(color, mixed, amount, color.red(), color.green(), color.blue(), alpha);
    }

    final void mixAndCheck(final Color color, final ColorComponent mixed, final float amount,
                           final RedColorComponent red,
                           final GreenColorComponent green,
                           final BlueColorComponent blue,
                           final AlphaColorComponent alpha) {
        final Color mixedColor = color.mix(mixed, amount);
        assertNotSame(color, mixedColor, "mix should not return this but another Color");

        checkComponent(red, mixedColor.red(), "red", color, mixed, amount);
        checkComponent(green, mixedColor.green(), "green", color, mixed, amount);
        checkComponent(blue, mixedColor.blue(), "blue", color, mixed, amount);
        checkComponent(alpha, mixedColor.alpha(), "alpha", color, mixed, amount);
    }

    static private void checkComponent(final ColorComponent expected, final ColorComponent actual, final String label,
                                       final Color color,
                                       final ColorComponent mixed, final float amount) {
        if (expected != actual) {
            assertNotEquals(
                    toString(expected),
                    toString(actual),
                    label + " incorrect, failed to mix " + color + " with " + toString(mixed) + " amount=" + amount);
        }
    }

    static private String toString(final ColorComponent component) {
        return component.getClass().getSimpleName() + "=" + component;
    }

    // toHsl

    final void toHslAndCheck(final int rgb, final float hue, final float saturation, final float lightness) {
        this.toHslAndCheck(Color.fromRgb(rgb), hue, saturation, lightness);
    }

    final void toHslAndCheck(final Color color, final float hue, final float saturation, final float lightness) {
        this.toHslAndCheck(color,
                Hsl.with(HslComponent.hue(hue), HslComponent.saturation(saturation), HslComponent.lightness(lightness)));
    }

    final void toHslAndCheck(final Color color, final Hsl hsl) {
        final Hsl result = color.toHsl();
        if ((false == this.isEqual(hsl.hue(), result.hue(), 0.9f)) || //
                (false == this.isEqual(hsl.saturation(), result.saturation(), 0.13f)) || //
                (false == this.isEqual(hsl.lightness(), result.lightness(), 0.05f))) {
            assertNotEquals("failed to convert " + color + " to hsl", hsl.toString(), result.toString());
        }
    }

    private boolean isEqual(final HslComponent component, final HslComponent otherComponent, final float epislon) {
        return Equality.isAlmostEquals(component.value(), otherComponent.value(), epislon);
    }

    // toHsv

    final void toHsvAndCheck(final int rgb, final float hue, final float saturation, final float value) {
        this.toHsvAndCheck(Color.fromRgb(rgb), hue, saturation, value);
    }

    final void toHsvAndCheck(final Color color, final float hue, final float saturation, final float value) {
        this.toHsvAndCheck(color,
                Hsv.with(HsvComponent.hue(hue), HsvComponent.saturation(saturation), HsvComponent.value(value)));
    }

    final void toHsvAndCheck(final Color color, final Hsv hsv) {
        final Hsv result = color.toHsv();
        if ((false == this.isEqual(hsv.hue(), result.hue(), 0.5f)) || //
                (false == this.isEqual(hsv.saturation(), result.saturation(), 0.01f)) || //
                (false == this.isEqual(hsv.value(), result.value(), 0.01f))) {
            assertNotEquals("failed to convert " + color + " to hsv", hsv.toString(), result.toString());
        }
    }

    private boolean isEqual(final HsvComponent component, final HsvComponent otherComponent, final float epislon) {
        return Equality.isAlmostEquals(component.value(), otherComponent.value(), epislon);
    }

    // ClassTesting ...................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting ................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Color.class.getSimpleName();
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public final C fromJsonNode(final JsonNode from) {
        return Cast.to(Color.fromJsonNodeColor(from));
    }
}
