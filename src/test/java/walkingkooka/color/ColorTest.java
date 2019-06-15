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
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

public final class ColorTest implements ClassTesting2<Color>,
        ParseStringTesting<Color> {

    // parseColor..................................................................

    @Test
    public void testParseColorMissingLeadingHashFails() {
        this.parseFails("123", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorInvalidFails() {
        this.parseFails("xyz", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorInvalidFails2() {
        this.parseFails("#1x3", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorOneDigitFails() {
        this.parseFails("#1", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorTwoDigitsFails() {
        this.parseFails("#12", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorFiveDigitsFails() {
        this.parseFails("#12345", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorSevenDigitsFails() {
        this.parseFails("#1234567", IllegalArgumentException.class);
    }

    // rgba(1,2,3).......................................................................................................

    @Test
    public void testParseColorRgbaFunctionIncompleteFails() {
        this.parseFails("rgba(1", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorRgbaFunctionMissingParensRightFails() {
        this.parseFails("rgba(1,2,3,0.5", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorRgbaFunction() {
        this.parseRgbaAndCheck3("rgba(1,2,3,127)", 1, 2, 3, 127);
    }

    @Test
    public void testParseColorRgbaFunction2() {
        this.parseRgbaAndCheck3("rgba(12,34,56,127)", 12, 34, 56, 127);
    }

    @Test
    public void testParseColorRgbaFunction3() {
        this.parseRgbaAndCheck3("rgba(99,128,255,127)", 99, 128, 255, 127);
    }

    @Test
    public void testParseColorRgbaFunction4() {
        this.parseRgbaAndCheck3("rgba(0,0,0,0%)", 0, 0, 0, 0);
    }

    @Test
    public void testParseColorRgbaFunction5() {
        this.parseRgbaAndCheck3("rgba(255,254,253,100%)", 255, 254, 253, 255);
    }

    @Test
    public void testParseColorRgbaFunctionExtraWhitespace() {
        this.parseRgbaAndCheck3("rgba( 1,2 , 3, 0 )", 1, 2, 3, 0);
    }

    private void parseRgbaAndCheck3(final String text,
                                    final int red,
                                    final int green,
                                    final int blue,
                                    final int alpha) {
        this.parseAndCheck(text,
                Color.with(RedColorComponent.with((byte) red),
                        GreenColorComponent.with((byte) green),
                        BlueColorComponent.with((byte) blue))
                        .set(AlphaColorComponent.with((byte) alpha)));
    }

    // rgb(1,2,3).......................................................................................................

    @Test
    public void testParseColorRgbFunctionIncompleteFails() {
        this.parseFails("rgb(1", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorRgbFunctionMissingParensRightFails() {
        this.parseFails("rgb(1,2,3", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorRgbFunction() {
        this.parseRgbAndCheck2("rgb(1,2,3)", 1, 2, 3);
    }

    @Test
    public void testParseColorRgbFunction2() {
        this.parseRgbAndCheck2("rgb(12,34,56)", 12, 34, 56);
    }

    @Test
    public void testParseColorRgbFunction3() {
        this.parseRgbAndCheck2("rgb(99,128,255)", 99, 128, 255);
    }

    @Test
    public void testParseColorRgbFunctionExtraWhitespace() {
        this.parseRgbAndCheck2("rgb( 1,2 , 3 )", 1, 2, 3);
    }

    private void parseRgbAndCheck2(final String text, final int red, final int green, final int blue) {
        this.parseAndCheck(text, Color.with(RedColorComponent.with((byte)red),
                GreenColorComponent.with((byte)green),
                BlueColorComponent.with((byte)blue)));
    }

    // #123456..........................................................................................................

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlue() {
        this.parseRgbAndCheck("#123456", 0x123456);
    }

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlue2() {
        this.parseRgbAndCheck("#000011", 0x000011);
    }

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlue3() {
        this.parseRgbAndCheck("#010101", 0x010101);
    }

    // #123.............................................................................................................

    @Test
    public void testParseColorHashRedGreenBlue() {
        this.parseRgbAndCheck("#123", 0x112233);
    }

    @Test
    public void testParseColorHashRedGreenBlue2() {
        this.parseRgbAndCheck("#001", 0x000011);
    }

    @Test
    public void testParseColorHashRedGreenBlue3() {
        this.parseRgbAndCheck("#010", 0x001100);
    }

    @Test
    public void testParseColorHashRedGreenBlue4() {
        this.parseRgbAndCheck("#100", 0x110000);
    }

    private void parseRgbAndCheck(final String text, final int rgb) {
        this.parseAndCheck(text, Color.fromRgb(rgb));
    }

    // #1234.............................................................................................................

    @Test
    public void testParseColorHashRedGreenBlueBlackAlpha() {
        this.parseArgbAndCheck("#000F", 0xFF000000);
    }

    @Test
    public void testParseColorHashRedGreenBlueAlpha() {
        this.parseArgbAndCheck("#1234", 0x44112233);
    }

    @Test
    public void testParseColorHashRedGreenBlueAlpha2() {
        this.parseArgbAndCheck("#1000", 0x00110000);
    }

    @Test
    public void testParseColorHashRedGreenBlueAlpha3() {
        this.parseArgbAndCheck("#0010", 0x00000011);
    }

    @Test
    public void testParseColorHashRedGreenBlueAlpha4() {
        this.parseArgbAndCheck("#0100", 0x00001100);
    }

    @Test
    public void testParseColorHashRedGreenBlueAlpha5() {
        this.parseArgbAndCheck("#FEDC", 0xCCFFEEDD);
    }

    @Test
    public void testParseColorHashRedGreenBlueWhiteAlpha() {
        this.parseArgbAndCheck("#ffff", 0xffffffff);
    }

    // #12345678.......................................................................................................

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlueAlphaAlpha() {
        this.parseArgbAndCheck("#01234567", 0x67012345);
    }

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlueAlphaAlpha2() {
        this.parseArgbAndCheck("#12345678", 0x78123456);
    }

    @Test
    public void testParseColorHashAlphaAlphaRedRedGreenGreenBlueBlueZeroes() {
        this.parseArgbAndCheck("#00000000", 0x0);
    }

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlueAlphaAlpha3() {
        this.parseArgbAndCheck("#abcdef12", 0x12abcdef);
    }

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlueAlphaAlphaUpperCaseHex() {
        this.parseArgbAndCheck("#ABCDEF12", 0x12ABCDEF);
    }

    @Test
    public void testParseColorHashRedRedGreenGreenBlueBlueAlphaAlphaFFFFFFFF() {
        this.parseArgbAndCheck("#FFFFFFFF", 0xFFFFFFFF);
    }

    private void parseArgbAndCheck(final String text, final int argb) {
        this.parseAndCheck(text, Color.fromArgb(argb));
    }

    // name............................................................................................................

    @Test
    public void testParseColorNameUnknownFails() {
        this.parseFails("Unknown", IllegalArgumentException.class);
    }

    @Test
    public void testParseColorBlack() {
        this.parseAndCheck("black", Color.BLACK);
    }

    @Test
    public void testParseColorCyan() {
        this.parseAndCheck("CYAN", WebColorName.CYAN.color());
    }

    // ClassTesting ...................................................................................................

    @Override
    public Class<Color> type() {
        return Color.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting .............................................................................................

    @Override
    public Color parse(final String text) {
        return Color.parseColor(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }
}
