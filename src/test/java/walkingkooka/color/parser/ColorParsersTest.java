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
import walkingkooka.type.JavaVisibility;

import java.lang.reflect.Method;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColorParsersTest implements ClassTesting2<ColorParsers>,
        PublicStaticHelperTesting<ColorParsers> {
    
    // hsl(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHslIncompleteFails() {
        this.parseFails(ColorParsers.hsl(),
                "hsl(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslNumberCommaNumberCommaPercentageFails() {
        this.parseFails(ColorParsers.hsl(),
                "hsl(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslNumberCommaPercentageCommaNumberFails() {
        this.parseFails(ColorParsers.hsl(),
                "hsl(359,50%,10)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslNumberCommaNumberCommaNumber() {
        this.parseHslAndCheck("hsl(359,0,1)", 359, 0f, 1f);
    }

    @Test
    public void testParseHslNumberDegCommaNumberCommaNumber() {
        this.parseHslAndCheck("hsl(359deg,1.0,0.5)", 359, 1.0f, 0.5f);
    }

    @Test
    public void testParseHslNumberSpaceNumberSpaceNumber() {
        this.parseHslAndCheck("hsl(359 0.25 0.5)", 359, 0.25f, 0.5f);
    }

    @Test
    public void testParseHslSpaceNumberSpaceNumberSpaceNumberSpace() {
        this.parseHslAndCheck("hsl( 99 0.25 0.5 )", 99, 0.25f, 0.5f);
    }

    @Test
    public void testParseHslPercentageCommaPercentageCommaPercentage() {
        this.parseHslAndCheck("hsl(359,0%,1%)", 359, 0f, 0.01f);
    }

    @Test
    public void testParseHslPercentageDegCommaPercentageCommaPercentage() {
        this.parseHslAndCheck("hsl(359deg,100%,50%)", 359, 1.0f, 0.5f);
    }

    @Test
    public void testParseHslPercentageSpacePercentageSpacePercentage() {
        this.parseHslAndCheck("hsl(359 25% 50%)", 359, 0.25f, 0.5f);
    }

    private void parseHslAndCheck(final String text,
                                  final float hue,
                                  final float saturation,
                                  final float lightness) {
        this.parseAndCheck(ColorParsers.hsl(),
                text,
                Hsl.with(HslComponent.hue(hue),
                        HslComponent.saturation(saturation),
                        HslComponent.lightness(lightness)));
    }

    // hsl(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHslaIncompleteFails() {
        this.parseFails(ColorParsers.hsl(),
                "hsl(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslaNumberCommaNumberCommaPercentageFails() {
        this.parseFails(ColorParsers.hsl(),
                "hsl(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslaNumberCommaPercentageCommaNumberFails() {
        this.parseFails(ColorParsers.hsl(),
                "hsl(359,50%,10)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHslaNumberCommaNumberCommaNumberCommaNumber() {
        this.parseHslAndCheck("hsla(359,0,0.5,1.0)", 359, 0f, 0.5f, 1f);
    }

    @Test
    public void testParseHslaNumberDegCommaNumberCommaNumberCommaNumber() {
        this.parseHslAndCheck("hsla(359deg,1.0,0.5,0.25)", 359, 1.0f, 0.5f, 0.25f);
    }

    @Test
    public void testParseHslaNumberSpaceNumberSpaceNumberSlashNumber() {
        this.parseHslAndCheck("hsla(359 0.25 0.5/0.75)", 359, 0.25f, 0.5f, 0.75f);
    }

    @Test
    public void testParseHslaSpaceNumberSpaceNumberSpaceNumberSpaceSlashNumber() {
        this.parseHslAndCheck("hsla( 99 0.25 0.5/0.75)", 99, 0.25f, 0.5f, 0.75f);
    }

    @Test
    public void testParseHslaPercentageCommaPercentageCommaPercentage() {
        this.parseHslAndCheck("hsla(359,0%,1%,2%)", 359, 0f, 0.01f, 0.02f);
    }

    @Test
    public void testParseHslaPercentageDegCommaPercentageCommaPercentage() {
        this.parseHslAndCheck("hsla(359deg,100%,50%,25%)", 359, 1.0f, 0.5f, 0.25f);
    }

    @Test
    public void testParseHslaPercentageSpacePercentageSlashPercentage() {
        this.parseHslAndCheck("hsla(359 25% 50%/75%)", 359, 0.25f, 0.5f, 0.75f);
    }

    @Test
    public void testParseHslaPercentageSpacePercentageSpaceSlashSpacePercentage() {
        this.parseHslAndCheck("hsla(359 25% 50% / 75%)", 359, 0.25f, 0.5f, 0.75f);
    }

    private void parseHslAndCheck(final String text,
                                   final float hue,
                                   final float saturation,
                                   final float lightness,
                                   final float alpha) {
        this.parseAndCheck(ColorParsers.hsl(),
                text,
                Hsl.with(HslComponent.hue(hue),
                        HslComponent.saturation(saturation),
                        HslComponent.lightness(lightness))
                        .set(HslComponent.alpha(alpha)));
    }


    // hsv(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHsvIncompleteFails() {
        this.parseFails(ColorParsers.hsv(),
                "hsv(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvNumberCommaNumberCommaPercentageFails() {
        this.parseFails(ColorParsers.hsv(),
                "hsv(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvNumberCommaPercentageCommaNumberFails() {
        this.parseFails(ColorParsers.hsv(),
                "hsv(359,50%,10)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvNumberCommaNumberCommaNumber() {
        this.parseHsvAndCheck("hsv(359,0,1)", 359, 0f, 1f);
    }

    @Test
    public void testParseHsvNumberDegCommaNumberCommaNumber() {
        this.parseHsvAndCheck("hsv(359deg,1.0,0.5)", 359, 1.0f, 0.5f);
    }

    @Test
    public void testParseHsvNumberSpaceNumberSpaceNumber() {
        this.parseHsvAndCheck("hsv(359 0.25 0.5)", 359, 0.25f, 0.5f);
    }

    @Test
    public void testParseHsvSpaceNumberSpaceNumberSpaceNumberSpace() {
        this.parseHsvAndCheck("hsv( 99 0.25 0.5 )", 99, 0.25f, 0.5f);
    }

    @Test
    public void testParseHsvPercentageCommaPercentageCommaPercentage() {
        this.parseHsvAndCheck("hsv(359,0%,1%)", 359, 0f, 0.01f);
    }

    @Test
    public void testParseHsvPercentageDegCommaPercentageCommaPercentage() {
        this.parseHsvAndCheck("hsv(359deg,100%,50%)", 359, 1.0f, 0.5f);
    }

    @Test
    public void testParseHsvPercentageSpacePercentageSpacePercentage() {
        this.parseHsvAndCheck("hsv(359 25% 50%)", 359, 0.25f, 0.5f);
    }

    private void parseHsvAndCheck(final String text,
                                  final float hue,
                                  final float saturation,
                                  final float value) {
        this.parseAndCheck(ColorParsers.hsv(),
                text,
                Hsv.with(HsvComponent.hue(hue),
                        HsvComponent.saturation(saturation),
                        HsvComponent.value(value)));
    }

    // hsv(359,1.0,1.0).................................................................................................

    @Test
    public void testParseHsvaIncompleteFails() {
        this.parseFails(ColorParsers.hsv(),
                "hsv(359",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvaNumberCommaNumberCommaPercentageFails() {
        this.parseFails(ColorParsers.hsv(),
                "hsv(359,50,10%)",
                ParserReporterException.class);
    }

    @Test
    public void testParseHsvaNumberCommaNumberCommaNumberCommaNumber() {
        this.parseHsvaAndCheck("hsva(359,0,0.5,1.0)", 359, 0f, 0.5f, 1.0f);
    }

    @Test
    public void testParseHsvaNumberDegCommaNumberCommaNumberCommaNumber() {
        this.parseHsvaAndCheck("hsva(359deg,1.0,0.5,0.25)", 359, 1.0f, 0.5f, 0.25f);
    }

    @Test
    public void testParseHsvaNumberSpaceNumberSpaceNumberSpaceNumber() {
        this.parseHsvaAndCheck("hsva(359 0.25 0.5 / 0.75)", 359, 0.25f, 0.5f, 0.75f);
    }

    @Test
    public void testParseHsvaSpaceNumberSpaceNumberSpaceNumberSpaceSlashNumber() {
        this.parseHsvaAndCheck("hsva( 99 0.25 0.5/0.75)", 99, 0.25f, 0.5f, 0.75f);
    }

    @Test
    public void testParseHsvaPercentageCommaPercentageCommaPercentage() {
        this.parseHsvaAndCheck("hsva(359,0%,1%,2%)", 359, 0f, 0.01f, 0.02f);
    }

    @Test
    public void testParseHsvaPercentageDegCommaPercentageCommaPercentage() {
        this.parseHsvaAndCheck("hsva(359deg,100%,50%,25%)", 359, 1.0f, 0.5f, 0.25f);
    }

    @Test
    public void testParseHsvaPercentageSpacePercentageSlashPercentage() {
        this.parseHsvaAndCheck("hsva(359 25% 50%/75%)", 359, 0.25f, 0.5f, 0.75f);
    }

    @Test
    public void testParseHsvaPercentageSpacePercentageSpaceSlashSpacePercentage() {
        this.parseHsvaAndCheck("hsva(359 25% 50% / 75%)", 359, 0.25f, 0.5f, 0.75f);
    }

    private void parseHsvaAndCheck(final String text,
                                   final float hue,
                                   final float saturation,
                                   final float value,
                                   final float alpha) {
        this.parseAndCheck(ColorParsers.hsv(),
                text,
                Hsv.with(HsvComponent.hue(hue),
                        HsvComponent.saturation(saturation),
                        HsvComponent.value(value))
                        .set(HsvComponent.alpha(alpha)));
    }

    // rgba(1,2,3).......................................................................................................

    @Test
    public void testParseRgbaIncompleteFails() {
        this.parseFails(ColorParsers.rgb(),
                "rgba(1",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbaMissingParensRightFails() {
        this.parseFails(ColorParsers.rgb(),
                "rgba(1,2,3,0.5",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbaNumberCommaPercentageCommaNumberCommaNumberFails() {
        this.parseFails(ColorParsers.rgb(),
                "rgba(1,100%,3,0.5)",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbaNumberCommaNumberCommaNumberCommaNumber() {
        this.parseRgbaAndCheck("rgba(0,1,255,127)", 0, 1, 255, 127);
    }

    @Test
    public void testParseRgbaNumberCommaNumberCommaNumberCommaPercentage() {
        this.parseRgbaAndCheck("rgba(0,1,254,50%)", 0, 1, 254, 128);
    }

    @Test
    public void testParseRgbaPercentageCommaPercentageCommaPercentageCommaNumber() {
        this.parseRgbaAndCheck("rgba(0%,1%,100%,127)", 0, 3, 255, 127);
    }

    @Test
    public void testParseRgbaPercentageCommaPercentageCommaPercentageCommaPercentage() {
        this.parseRgbaAndCheck("rgba(0%,1%,100%,25%)", 0, 3, 255, 64);
    }

    @Test
    public void testParseRgbaSpacePercentageSpaceCommaPercentageCommaPercentageCommaPercentage() {
        this.parseRgbaAndCheck("rgba( 0% ,1%,100%,50%)", 0, 3, 255, 128);
    }

    @Test
    public void testParseRgbaSpacePercentageSpaceCommaPercentageCommaPercentageCommaSpacePercentageSpace() {
        this.parseRgbaAndCheck("rgba( 0% ,1%,100%, 50% )", 0, 3, 255, 128);
    }

    @Test
    public void testParseRgbaSpaceNumberSpaceNumberSpaceNumberSlashNumber() {
        this.parseRgbaAndCheck("rgba( 0 1 2/3 )", 0, 1, 2, 3);
    }

    @Test
    public void testParseRgbaSpaceNumberSpaceNumberSpaceNumberSpaceSlashSpaceNumber() {
        this.parseRgbaAndCheck("rgba( 0 1 2 / 3 )", 0, 1, 2, 3);
    }

    private void parseRgbaAndCheck(final String text,
                                   final int red,
                                   final int green,
                                   final int blue,
                                   final int alpha) {
        this.parseAndCheck(
                ColorParsers.rgb(),
                text,
                Color.with(
                        ColorComponent.red((byte) red),
                        ColorComponent.green((byte) green),
                        ColorComponent.blue((byte) blue))
                        .set(ColorComponent.alpha((byte) alpha)));
    }

    // rgb(1,2,3).......................................................................................................

    @Test
    public void testParseRgbIncompleteFails() {
        this.parseFails(ColorParsers.rgb(),
                "rgb(1",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbMissingParensRightFails() {
        this.parseFails(ColorParsers.rgb(),
                "rgb(1,2,3",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbNumberCommaPercentageCommaNumberFails() {
        this.parseFails(ColorParsers.rgb(),
                "rgb(1,2%,3)",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbNumberCommaNumberCommaNumber() {
        this.parseRgbAndCheck("rgb(0,1,2)", 0, 1, 2);
    }

    @Test
    public void testParseRgbNumberCommaNumberCommaNumber2() {
        this.parseRgbAndCheck("rgb(99,128,255)", 99, 128, 255);
    }

    @Test
    public void testParseRgbNumberCommaNumberCommaPercentage() {
        this.parseRgbAndCheck("rgb(25%,50%,100%)", 64, 128, 255);
    }

    @Test
    public void testParseRgbWhitespaceNumberCommaWhitespaceNumberCommaWhitespaceNumberCommaWhitespaceNumber() {
        this.parseRgbAndCheck("rgb( 1, 2, 3)", 1, 2, 3);
    }

    @Test
    public void testParseRgbNumberWhitespaceCommaNumberWhitespaceCommaNumberWhitespaceCommaNumberWhitespace() {
        this.parseRgbAndCheck("rgb(1 ,2 ,3 )", 1, 2, 3);
    }

    @Test
    public void testParseRgbPercentageSpacePercentageSpacePercentageSpace() {
        this.parseRgbAndCheck("rgb(0% 1% 2%)", 0, 3, 5);
    }

    @Test
    public void testParseRgbSpacePercentageSpacePercentageSpacePercentageSpace() {
        this.parseRgbAndCheck("rgb( 0% 1% 2% )", 0, 3, 5);
    }

    private void parseRgbAndCheck(final String text, final int red, final int green, final int blue) {
        this.parseAndCheck(ColorParsers.rgb(),
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
                        .map(t -> ColorFunctionFunctionParserToken.class.cast(t).toColorHslOrHsv())
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
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
