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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class QuotedStringHeaderValueConverterTest extends StringHeaderValueConverterTestCase<QuotedStringHeaderValueConverter> {

    @Test
    public void testParseControlCharacterFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.parse("a\\0");
        });
    }

    @Test
    public void testParseNonAsciiFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.parse("a\u0080");
        });
    }

    @Test
    public void testParseMissingOpeningDoubleQuoteFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.parse("abc\"");
        });
    }

    @Test
    public void testParseUnsupportedBackslashFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.parse("a\\bc");
        });
    }

    @Test
    public void testParse() {
        this.parseAndCheck("\"abc\"", "abc");
    }

    @Test
    public void testParseWithBackslashSupportedEscapedDoubleQuote() {
        this.parseAndCheck(this.converterSupportingBackslashes(),
                "\"a\\\"bc\"",
                "a\"bc");
    }

    @Test
    public void testParseWithBackslashSupportedEscapedBackslash() {
        this.parseAndCheck(this.converterSupportingBackslashes(),
                "\"a\\\\bc\"",
                "a\\bc");
    }

    @Test
    public void testToText() {
        this.toTextAndCheck("abc", "\"abc\"");
    }

    @Test
    public void testToTextWithBackslashSupported() {
        this.toTextAndCheck(this.converterSupportingBackslashes(),
                "abc",
                "\"abc\"");
    }

    @Test
    public void testToTextWithBackslashSupportedDoubleQuote() {
        this.toTextAndCheck(this.converterSupportingBackslashes(),
                "a\"bc",
                "\"a\\\"bc\"");
    }

    @Test
    public void testToTextWithBackslashSupportedBackslash() {
        this.toTextAndCheck(this.converterSupportingBackslashes(),
                "a\\bc",
                "\"a\\\\bc\"");
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck("\"abc\"", "abc");
    }

    @Test
    public void testRoundtripWithBackslash() {
        final QuotedStringHeaderValueConverter converter = this.converterSupportingBackslashes();
        final String text = "\"a\\\"bc\"";
        final String value = "a\"bc";

        this.parseAndCheck(converter, text, value);
        this.toTextAndCheck(converter, value, text);
    }

    @Override
    public String typeNamePrefix() {
        return "QuotedString";
    }

    @Override
    String invalidHeaderValue() {
        return "123";
    }

    @Override
    protected QuotedStringHeaderValueConverter converter() {
        return QuotedStringHeaderValueConverter.with(this.charPredicate(), false);
    }

    private QuotedStringHeaderValueConverter converterSupportingBackslashes() {
        return QuotedStringHeaderValueConverter.with(this.charPredicate(), true);
    }

    @Override
    final String converterToString() {
        return this.charPredicate().toString();
    }

    @Override
    public Class<QuotedStringHeaderValueConverter> type() {
        return QuotedStringHeaderValueConverter.class;
    }
}
