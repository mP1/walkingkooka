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
    public void testParseFourDigitsFails() {
        this.parseFails("#1234", IllegalArgumentException.class);
    }

    @Test
    public void testParseFiveDigitsFails() {
        this.parseFails("#12345", IllegalArgumentException.class);
    }

    @Test
    public void testParseSevenDigitsFails() {
        this.parseFails("#1234567", IllegalArgumentException.class);
    }

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
