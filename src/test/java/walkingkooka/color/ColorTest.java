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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

public final class ColorTest extends ClassTestCase<Color> implements ParseStringTesting<Color> {

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
    public void testParseEightDigitsFails() {
        this.parseFails("#12345678", IllegalArgumentException.class);
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

    @Override
    protected Class<Color> type() {
        return Color.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
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
