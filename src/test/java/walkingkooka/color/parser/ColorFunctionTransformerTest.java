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

package walkingkooka.color.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.color.Hsl;
import walkingkooka.color.HslComponent;
import walkingkooka.color.Hsv;
import walkingkooka.color.HsvComponent;
import walkingkooka.type.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColorFunctionTransformerTest extends ColorFunctionTestCase<ColorFunctionTransformer> {

    @Test
    public void testFunctionNameInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ColorFunctionTransformer.functionName(ColorFunctionParserToken.functionName("unknown", "unknown"));
        });
    }

    // hsl..............................................................................................................

    @Test
    public void testHslNumberNumberNumber() {
        this.colorHslOrHsvAndCheck("hsl",
                hsl(359, 0.25f,0.5f),
                number(359),
                number(0.25),
                number(0.5));
    }

    @Test
    public void testHslNumberNumberNumberNumber() {
        this.colorHslOrHsvAndCheck("hsl",
                hsl(359, 0.25f,0.5f, 0.75f),
                number(359),
                number(0.25),
                number(0.5),
                number(0.75));
    }

    @Test
    public void testHslNumberNumberNumberNumber2() {
        this.colorHslOrHsvAndCheck("hsl",
                hsl(359, 0.25f,0.5f, 0.75f),
                number(359),
                number(0.25),
                number(0.5),
                number(0.75));
    }

    @Test
    public void testHslNumberNumberNumberPercentage() {
        this.colorHslOrHsvAndCheck("hsl",
                hsl(359, 0.25f,0.5f, 0.75f),
                number(359),
                number(0.25),
                number(0.5),
                percentage(75));
    }

    @Test
    public void testHslPercentagePercentagePercentagePercentage() {
        this.colorHslOrHsvAndCheck("hsl",
                hsl(359, 0.25f,0.5f, 0.75f),
                number(359),
                percentage(25),
                percentage(50),
                percentage(75));
    }

    @Test
    public void testHslPercentagePercentagePercentagePercentage2() {
        this.colorHslOrHsvAndCheck("hsl",
                hsl(359, 0.25f,0.5f, 0.75f),
                number(359),
                percentage(25),
                percentage(50),
                percentage(75));
    }

    @Test
    public void testHslaNumberNumberNumber() {
        this.colorHslOrHsvAndCheck("hsla",
                hsl(359, 0.25f,0.5f),
                number(359),
                number(0.25),
                number(0.5));
    }

    private Hsl hsl(final float hue,
                    final float saturation,
                    final float lightness) {
        return Hsl.with(HslComponent.hue(hue), HslComponent.saturation(saturation), HslComponent.lightness(lightness));
    }

    private Hsl hsl(final float hue,
                    final float saturation,
                    final float lightness,
                    final float alpha) {
        return hsl(hue, saturation, lightness).set(HslComponent.alpha(alpha));
    }

    // hsv..............................................................................................................

    @Test
    public void testHsvNumberNumberNumber() {
        this.colorHslOrHsvAndCheck("hsv",
                hsv(359, 0.25f,0.5f),
                number(359),
                number(0.25),
                number(0.5));
    }

    @Test
    public void testHsvNumberNumberNumberNumber() {
        this.colorHslOrHsvAndCheck("hsv",
                hsv(359, 0.25f,0.5f, 0.75f),
                number(359),
                number(0.25),
                number(0.5),
                number(0.75));
    }

    @Test
    public void testHsvNumberNumberNumberNumber2() {
        this.colorHslOrHsvAndCheck("hsv",
                hsv(359, 0.25f,0.5f, 0.75f),
                number(359),
                number(0.25),
                number(0.5),
                number(0.75));
    }

    @Test
    public void testHsvNumberNumberNumberPercentage() {
        this.colorHslOrHsvAndCheck("hsv",
                hsv(359, 0.25f,0.5f, 0.75f),
                number(359),
                number(0.25),
                number(0.5),
                percentage(75));
    }

    @Test
    public void testHsvPercentagePercentagePercentagePercentage() {
        this.colorHslOrHsvAndCheck("hsv",
                hsv(359, 0.25f,0.5f, 0.75f),
                number(359),
                percentage(25),
                percentage(50),
                percentage(75));
    }

    @Test
    public void testHsvPercentagePercentagePercentagePercentage2() {
        this.colorHslOrHsvAndCheck("hsv",
                hsv(359, 0.25f,0.5f, 0.75f),
                number(359),
                percentage(25),
                percentage(50),
                percentage(75));
    }

    @Test
    public void testHsvaNumberNumberNumber() {
        this.colorHslOrHsvAndCheck("hsva",
                hsv(359, 0.25f,0.5f),
                number(359),
                number(0.25),
                number(0.5));
    }

    private Hsv hsv(final float hue,
                    final float saturation,
                    final float value) {
        return Hsv.with(HsvComponent.hue(hue), HsvComponent.saturation(saturation), HsvComponent.value(value));
    }

    private Hsv hsv(final float hue,
                    final float saturation,
                    final float value,
                    final float alpha) {
        return hsv(hue, saturation, value).set(HsvComponent.alpha(alpha));
    }

    // rgb..............................................................................................................

    @Test
    public void testRgb() {
        this.colorHslOrHsvAndCheck("rgb",
                Color.parseColor("#123456"),
                number(0x12),
                number(0x34),
                number(0x56));
    }

    @Test
    public void testRgbWithAlpha() {
        this.colorHslOrHsvAndCheck("rgb",
                Color.parseColor("#1234567f"),
                number(0x12),
                number(0x34),
                number(0x56),
                number(0x7f));
    }

    @Test
    public void testRgbWithAlphaPercentage() {
        this.colorHslOrHsvAndCheck("rgb",
                Color.parseColor("#12345680"),
                number(0x12),
                number(0x34),
                number(0x56),
                percentage(50));
    }

    @Test
    public void testRgba() {
        this.colorHslOrHsvAndCheck("rgba",
                Color.parseColor("#123456"),
                number(0x12),
                number(0x34),
                number(0x56));
    }

    @Test
    public void testRgbaWithAlpha() {
        this.colorHslOrHsvAndCheck("rgba",
                Color.parseColor("#1234567f"),
                number(0x12),
                number(0x34),
                number(0x56),
                number(0x7f));
    }

    @Test
    public void testRgbaWithAlphaPercentage() {
        this.colorHslOrHsvAndCheck("rgba",
                Color.parseColor("#12345680"),
                number(0x12),
                number(0x34),
                number(0x56),
                percentage(50));
    }

    private ColorFunctionParserToken number(final double value ) {
        return ColorFunctionParserToken.number(value, String.valueOf(value));
    }

    private ColorFunctionParserToken percentage(final double value ) {
        return ColorFunctionParserToken.percentage(value, String.valueOf(value) + "%");
    }

    private void colorHslOrHsvAndCheck(final String name,
                                       final ColorHslOrHsv colorHslOrHsv,
                                       final ColorFunctionParserToken...values) {
        assertEquals(colorHslOrHsv,
                ColorFunctionTransformer.functionName(ColorFunctionFunctionNameParserToken.with(name, name))
                        .colorHslOrHsv(
                            values[0],
                                values[1],
                                values[2],
                                Optional.ofNullable(values.length == 4 ? values[3] : null )),
                () -> name + " " + colorHslOrHsv);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ColorFunctionTransformer> type() {
        return ColorFunctionTransformer.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
