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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

public final class ColorTest implements ClassTesting2<Color>,
        ParseStringTesting<Color> {

    // parse..................................................................

    @Test
    public void testParseMissingLeadingHashFails() {
        this.parseFails("123", IllegalArgumentException.class);
    }

    @Test
    public void testParseInvalidFails() {
        this.parseFails("xyz", IllegalArgumentException.class);
    }

    @Test
    public void testParseInvalidFails2() {
        this.parseFails("#1x3", IllegalArgumentException.class);
    }

    @Test
    public void testParseOneDigitFails() {
        this.parseFails("#1", IllegalArgumentException.class);
    }

    @Test
    public void testParseTwoDigitsFails() {
        this.parseFails("#12", IllegalArgumentException.class);
    }

    @Test
    public void testParseFiveDigitsFails() {
        this.parseFails("#12345", IllegalArgumentException.class);
    }

    @Test
    public void testParseSevenDigitsFails() {
        this.parseFails("#1234567", IllegalArgumentException.class);
    }

    // rgba(1,2,3).......................................................................................................

    @Test
    public void testParseRgbaFunctionIncompleteFails() {
        this.parseFails("rgba(1", IllegalArgumentException.class);
    }

    @Test
    public void testParseRgbaFunctionMissingParensRightFails() {
        this.parseFails("rgba(1,2,3,0.5", IllegalArgumentException.class);
    }

    @Test
    public void testParseRgbaFunction() {
        this.parseRgbaAndCheck3("rgba(1,2,3,0.5)", 1, 2, 3, 127);
    }

    @Test
    public void testParseRgbaFunction2() {
        this.parseRgbaAndCheck3("rgba(12,34,56,0.5)", 12, 34, 56, 127);
    }

    @Test
    public void testParseRgbaFunction3() {
        this.parseRgbaAndCheck3("rgba(99,128,255,0.5)", 99, 128, 255, 127);
    }

    @Test
    public void testParseRgbaFunction4() {
        this.parseRgbaAndCheck3("rgba(0,0,0,0)", 0, 0, 0, 0);
    }

    @Test
    public void testParseRgbaFunction5() {
        this.parseRgbaAndCheck3("rgba(255,254,253,1.0)", 255, 254, 253, 255);
    }

    @Test
    public void testParseRgbaFunctionExtraWhitespace() {
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
    public void testParseRgbFunctionIncompleteFails() {
        this.parseFails("rgb(1", IllegalArgumentException.class);
    }

    @Test
    public void testParseRgbFunctionMissingParensRightFails() {
        this.parseFails("rgb(1,2,3", IllegalArgumentException.class);
    }

    @Test
    public void testParseRgbFunction() {
        this.parseRgbAndCheck2("rgb(1,2,3)", 1, 2, 3);
    }

    @Test
    public void testParseRgbFunction2() {
        this.parseRgbAndCheck2("rgb(12,34,56)", 12, 34, 56);
    }

    @Test
    public void testParseRgbFunction3() {
        this.parseRgbAndCheck2("rgb(99,128,255)", 99, 128, 255);
    }

    @Test
    public void testParseRgbFunctionExtraWhitespace() {
        this.parseRgbAndCheck2("rgb( 1,2 , 3 )", 1, 2, 3);
    }

    private void parseRgbAndCheck2(final String text, final int red, final int green, final int blue) {
        this.parseAndCheck(text, Color.with(RedColorComponent.with((byte)red),
                GreenColorComponent.with((byte)green),
                BlueColorComponent.with((byte)blue)));
    }

    // #123456..........................................................................................................

    @Test
    public void testParseHashRedRedGreenGreenBlueBlue() {
        this.parseRgbAndCheck("#123456", 0x123456);
    }

    @Test
    public void testParseHashRedRedGreenGreenBlueBlue2() {
        this.parseRgbAndCheck("#000011", 0x000011);
    }

    @Test
    public void testParseHashRedRedGreenGreenBlueBlue3() {
        this.parseRgbAndCheck("#010101", 0x010101);
    }

    // #123.............................................................................................................

    @Test
    public void testParseHashRedGreenBlue() {
        this.parseRgbAndCheck("#123", 0x112233);
    }

    @Test
    public void testParseHashRedGreenBlue2() {
        this.parseRgbAndCheck("#001", 0x000011);
    }

    @Test
    public void testParseHashRedGreenBlue3() {
        this.parseRgbAndCheck("#010", 0x001100);
    }

    @Test
    public void testParseHashRedGreenBlue4() {
        this.parseRgbAndCheck("#100", 0x110000);
    }

    private void parseRgbAndCheck(final String text, final int rgb) {
        this.parseAndCheck(text, Color.fromRgb(rgb));
    }

    // #1234.............................................................................................................

    @Test
    public void testParseHashAlphaRedGreenBlueBlack() {
        this.parseArgbAndCheck("#F000", 0xFF000000);
    }

    @Test
    public void testParseHashAlphaRedGreenBlue() {
        this.parseArgbAndCheck("#1234", 0x11223344);
    }

    @Test
    public void testParseHashAlphaRedGreenBlue2() {
        this.parseArgbAndCheck("#0001", 0x00000011);
    }

    @Test
    public void testParseHashAlphaRedGreenBlue3() {
        this.parseArgbAndCheck("#0010", 0x00001100);
    }

    @Test
    public void testParseHashAlphaRedGreenBlue4() {
        this.parseArgbAndCheck("#0100", 0x00110000);
    }

    @Test
    public void testParseHashAlphaRedGreenBlue5() {
        this.parseArgbAndCheck("#FEDC", 0xFFEEDDCC);
    }

    @Test
    public void testParseHashAlphaRedGreenBlueWhite() {
        this.parseArgbAndCheck("#ffff", 0xffffffff);
    }

    // #12345678.......................................................................................................

    @Test
    public void testParseHashAlphaAlphaRedRedGreenGreenBlueBlue() {
        this.parseArgbAndCheck("#01234567", 0x01234567);
    }

    @Test
    public void testParseHashAlphaAlphaRedRedGreenGreenBlueBlue2() {
        this.parseArgbAndCheck("#12345678", 0x12345678);
    }

    @Test
    public void testParseHashAlphaAlphaRedRedGreenGreenBlueBlueZeroes() {
        this.parseArgbAndCheck("#00000000", 0x0);
    }

    @Test
    public void testParseHashAlphaAlphaRedRedGreenGreenBlueBlue3() {
        this.parseArgbAndCheck("#abcdef12", 0xabcdef12);
    }

    @Test
    public void testParseHashAlphaAlphaRedRedGreenGreenBlueBlueUpperCaseHex() {
        this.parseArgbAndCheck("#ABCDEF12", 0xABCDEF12);
    }

    @Test
    public void testParseHashAlphaAlphaRedRedGreenGreenBlueBlueFFFFFFFF() {
        this.parseArgbAndCheck("#FFFFFFFF", 0xFFFFFFFF);
    }

    private void parseArgbAndCheck(final String text, final int argb) {
        this.parseAndCheck(text, Color.fromArgb(argb));
    }

    // name............................................................................................................

    @Test
    public void testParseNameUnknownFails() {
        this.parseFails("Unknown", IllegalArgumentException.class);
    }

    @Test
    public void testParseBlack() {
        this.parseAndCheck("black", Color.BLACK);
    }

    @Test
    public void testParseCyan() {
        this.parseAndCheck("CYAN", WebColorName.CYAN.color());
    }

    @Override
    public Class<Color> type() {
        return Color.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public Color parse(final String text) {
        return Color.parse(text);
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
