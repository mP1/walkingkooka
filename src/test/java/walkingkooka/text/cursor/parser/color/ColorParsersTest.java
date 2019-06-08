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
package walkingkooka.text.cursor.parser.color;

import org.junit.jupiter.api.Test;
import walkingkooka.Value;
import walkingkooka.color.Color;
import walkingkooka.color.ColorComponent;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.color.Hsl;
import walkingkooka.color.HslComponent;
import walkingkooka.color.Hsv;
import walkingkooka.color.HsvComponent;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.ParserReporterException;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Method;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColorParsersTest implements ClassTesting2<ColorParsers>,
        PublicStaticHelperTesting<ColorParsers> {
    
    // hsl(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHslFunctionIncompleteFails() {
        this.parseFails(ColorParsers.hslFunction(),
                "hsl(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslFunctionMissingPercentSignFails() {
        this.parseFails(ColorParsers.hslFunction(),
                "hsl(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslFunctionMissingParensRightFails() {
        this.parseFails(ColorParsers.hslFunction(),
                "hsl(359,50%,10%",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslFunction() {
        this.parseHslAndCheck("hsl(359,100%,50%)", 359, 1.0f, 0.5f);
    }

    @Test
    public void testParseHslFunction2() {
        this.parseHslAndCheck("hsl(99,0%,25%)", 99, 0f, 0.25f);
    }

    @Test
    public void testParseHslFunctionExtraWhitespace() {
        this.parseHslAndCheck("hsl( 299,100%, 50% )", 299, 1f, 0.5f);
    }

    private void parseHslAndCheck(final String text,
                                  final float hue,
                                  final float saturation,
                                  final float lightness) {
        this.parseAndCheck(ColorParsers.hslFunction(),
                text,
                Hsl.with(HslComponent.hue(hue),
                        HslComponent.saturation(saturation),
                        HslComponent.lightness(lightness)));
    }

    // hsla(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHslaFunctionIncompleteFails() {
        this.parseFails(ColorParsers.hslaFunction(),
                "hsla(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslaFunctionMissingPercentSignFails() {
        this.parseFails(ColorParsers.hslaFunction(),
                "hsla(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslaFunctionMissingParensRightFails() {
        this.parseFails(ColorParsers.hslaFunction(),
                "hsla(359,50%,10%",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslaFunction() {
        this.parseHslaAndCheck("hsla(359,100%,50%,25%)", 359, 1.0f, 0.5f, 0.25f);
    }

    @Test
    public void testParseHslaFunction2() {
        this.parseHslaAndCheck("hsla(99,0%,25%,50%)", 99, 0f, 0.25f, 0.5f);
    }

    @Test
    public void testParseHslaFunctionExtraWhitespace() {
        this.parseHslaAndCheck("hsla( 299,100%, 50%, 0%)", 299, 1f, 0.5f, 0);
    }

    private void parseHslaAndCheck(final String text,
                                   final float hue,
                                   final float saturation,
                                   final float lightness,
                                   final float alpha) {
        this.parseAndCheck(ColorParsers.hslaFunction(),
                text,
                Hsl.with(HslComponent.hue(hue),
                        HslComponent.saturation(saturation),
                        HslComponent.lightness(lightness))
                        .set(HslComponent.alpha(alpha)));
    }

    // hsv(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHsvFunctionIncompleteFails() {
        this.parseFails(ColorParsers.hsvFunction(),
                "hsv(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvFunctionMissingPercentSignFails() {
        this.parseFails(ColorParsers.hsvFunction(),
                "hsv(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvFunctionMissingParensRightFails() {
        this.parseFails(ColorParsers.hsvFunction(),
                "hsv(359,50%,10%",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvFunction() {
        this.parseHsvAndCheck("hsv(359,100%,50%)", 359, 1.0f, 0.5f);
    }

    @Test
    public void testParseHsvFunction2() {
        this.parseHsvAndCheck("hsv(99,0%,25%)", 99, 0f, 0.25f);
    }

    @Test
    public void testParseHsvFunctionExtraWhitespace() {
        this.parseHsvAndCheck("hsv( 299,100%, 50% )", 299, 1f, 0.5f);
    }

    private void parseHsvAndCheck(final String text,
                                  final float hue,
                                  final float saturation,
                                  final float value) {
        this.parseAndCheck(ColorParsers.hsvFunction(),
                text,
                Hsv.with(HsvComponent.hue(hue),
                        HsvComponent.saturation(saturation),
                        HsvComponent.value(value)));
    }

    // hsva(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHsvaFunctionIncompleteFails() {
        this.parseFails(ColorParsers.hsvaFunction(),
                "hsva(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvaFunctionMissingPercentSignFails() {
        this.parseFails(ColorParsers.hsvaFunction(),
                "hsva(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvaFunctionMissingParensRightFails() {
        this.parseFails(ColorParsers.hsvaFunction(),
                "hsva(359,50%,10%",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvaFunction() {
        this.parseHsvaAndCheck("hsva(359,100%,50%,25%)", 359, 1.0f, 0.5f, 0.25f);
    }

    @Test
    public void testParseHsvaFunction2() {
        this.parseHsvaAndCheck("hsva(99,0%,25%,50%)", 99, 0f, 0.25f, 0.5f);
    }

    @Test
    public void testParseHsvaFunctionExtraWhitespace() {
        this.parseHsvaAndCheck("hsva( 299,100%, 50%, 0%)", 299, 1f, 0.5f, 0);
    }

    private void parseHsvaAndCheck(final String text,
                                   final float hue,
                                   final float saturation,
                                   final float value,
                                   final float alpha) {
        this.parseAndCheck(ColorParsers.hsvaFunction(),
                text,
                Hsv.with(HsvComponent.hue(hue),
                        HsvComponent.saturation(saturation),
                        HsvComponent.value(value))
                        .set(HsvComponent.alpha(alpha)));
    }

    // rgba(1,2,3).......................................................................................................

    @Test
    public void testParseRgbaFunctionIncompleteFails() {
        this.parseFails(ColorParsers.rgbaFunction(),
                "rgba(1",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbaFunctionMissingParensRightFails() {
        this.parseFails(ColorParsers.rgbaFunction(),
                "rgba(1,2,3,0.5",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbaFunction() {
        this.parseRgbaAndCheck("rgba(1,2,3,0.5)", 1, 2, 3, 127);
    }

    @Test
    public void testParseRgbaFunction2() {
        this.parseRgbaAndCheck("rgba(12,34,56,0.5)", 12, 34, 56, 127);
    }

    @Test
    public void testParseRgbaFunction3() {
        this.parseRgbaAndCheck("rgba(99,128,255,0.5)", 99, 128, 255, 127);
    }

    @Test
    public void testParseRgbaFunction4() {
        this.parseRgbaAndCheck("rgba(0,0,0,0)", 0, 0, 0, 0);
    }

    @Test
    public void testParseRgbaFunction5() {
        this.parseRgbaAndCheck("rgba(255,254,253,1.0)", 255, 254, 253, 255);
    }

    @Test
    public void testParseRgbaFunctionExtraWhitespace() {
        this.parseRgbaAndCheck("rgba( 1,2 , 3, 0 )", 1, 2, 3, 0);
    }

    private void parseRgbaAndCheck(final String text,
                                   final int red,
                                   final int green,
                                   final int blue,
                                   final int alpha) {
        this.parseAndCheck(
                ColorParsers.rgbaFunction(),
                text,
                Color.with(
                        ColorComponent.red((byte) red),
                        ColorComponent.green((byte) green),
                        ColorComponent.blue((byte) blue))
                        .set(ColorComponent.alpha((byte) alpha)));
    }

    // rgb(1,2,3).......................................................................................................

    @Test
    public void testParseRgbFunctionIncompleteFails() {
        this.parseFails(ColorParsers.rgbFunction(),
                "rgb(1",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbFunctionMissingParensRightFails() {
        this.parseFails(ColorParsers.rgbFunction(),
                "rgb(1,2,3",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbFunction() {
        this.parseRgbAndCheck("rgb(1,2,3)", 1, 2, 3);
    }

    @Test
    public void testParseRgbFunction2() {
        this.parseRgbAndCheck("rgb(12,34,56)", 12, 34, 56);
    }

    @Test
    public void testParseRgbFunction3() {
        this.parseRgbAndCheck("rgb(99,128,255)", 99, 128, 255);
    }

    @Test
    public void testParseRgbFunctionExtraWhitespace() {
        this.parseRgbAndCheck("rgb( 1,2 , 3 )", 1, 2, 3);
    }

    private void parseRgbAndCheck(final String text, final int red, final int green, final int blue) {
        this.parseAndCheck(ColorParsers.rgbFunction(),
                text,
                Color.with(ColorComponent.red((byte) red),
                        ColorComponent.green((byte) green),
                        ColorComponent.blue((byte) blue)));
    }

    // helpers..........................................................................................................

    private void parseFails(final Parser<ParserContext> parser,
                            final String text,
                            final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            parser.orReport(ParserReporters.basic())
                    .parse(TextCursors.charSequence(text), parserContext());
        });
    }

    private void parseAndCheck(final Parser<ParserContext> parser,
                               final String text,
                               final ColorHslOrHsv value) {
        assertEquals(value,
                parser.orReport(ParserReporters.basic())
                        .parse(TextCursors.charSequence(text),
                                this.parserContext())
                        .map(t -> Value.class.cast(t).value())
                        .orElseThrow(() -> new AssertionError()),
                () -> "parse " + CharSequences.quoteAndEscape(text));
    }

    private ParserContext parserContext() {
        return ParserContexts.basic(DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    @Override
    public Class<ColorParsers> type() {
        return ColorParsers.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
