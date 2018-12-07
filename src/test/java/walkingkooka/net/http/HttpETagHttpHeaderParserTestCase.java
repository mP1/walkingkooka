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

package walkingkooka.net.http;

import org.junit.Test;

public abstract class HttpETagHttpHeaderParserTestCase<P extends HttpETagHttpHeaderParser>
        extends HttpHeaderParserTestCase<P, HttpETag> {

    HttpETagHttpHeaderParserTestCase() {
        super();
    }

    // parse ...........................................................................................

    @Test
    public final void testInvalidInitialFails2() {
        this.parseInvalidCharacterFails("w");
    }

    @Test
    public final void testInvalidInitialFails3() {
        this.parseInvalidCharacterFails("0");
    }

    @Test
    public final void testInvalidQuotedCharacterFails() {
        this.parseInvalidCharacterFails("\"abc\0\"", '\0');
    }

    @Test
    public final void testInvalidQuotedCharacterFails2() {
        this.parseInvalidCharacterFails("W/\"abc\0\"", '\0');
    }

    @Test
    public final void testWFails() {
        this.parseFails("W", HttpETagHttpHeaderParser.incompleteWeakIndicator("W"));
    }

    @Test
    public final void testWeaknessWithoutQuotedValueFails() {
        this.parseFails("W/", HttpETagHttpHeaderParser.missingETagValue("W/"));
    }

    @Test
    public final void testWeakInvalidFails() {
        this.parseInvalidCharacterFails("WA");
    }

    @Test
    public final void testWeakInvalidFails2() {
        this.parseInvalidCharacterFails("W0");
    }

    @Test
    public final void testBeginQuoteFails() {
        final String text = "\"";
        this.parseFails(text, HttpETagHttpHeaderParser.missingClosingQuote(text));
    }

    @Test
    public final void testBeginQuoteFails2() {
        final String text = "\"A";
        this.parseFails(text, HttpETagHttpHeaderParser.missingClosingQuote(text));
    }

    @Test
    public final void testBeginQuoteFails3() {
        final String text = "\"'";
        this.parseFails(text, HttpETagHttpHeaderParser.missingClosingQuote(text));
    }

    @Test
    public final void testWildcard() {
        this.parseAndCheck("*", HttpETag.wildcard());
    }

    @Test
    public final void testValue() {
        this.parseAndCheck("\"a\"", "a");
    }

    @Test
    public final void testValue2() {
        this.parseAndCheck("\"0\"", "0");
    }

    @Test
    public final void testValue3() {
        this.parseAndCheck("\"0123456789ABCDEF\"", "0123456789ABCDEF");
    }

    @Test
    public final void testWeakValue() {
        this.parseAndCheck("W/\"a\"", "a", HttpETagValidator.WEAK);
    }

    @Test
    public final void testWeakValue2() {
        this.parseAndCheck("W/\"0\"", "0", HttpETagValidator.WEAK);
    }

    @Test
    public final void testWeakValue3() {
        this.parseAndCheck("W/\"0123456789ABCDEF\"", "0123456789ABCDEF", HttpETagValidator.WEAK);
    }

    final void parseAndCheck(final String text, final String value) {
        this.parseAndCheck(text, value, HttpETagValidator.STRONG);
    }

    final void parseAndCheck(final String text, final String value, final HttpETagValidator validator) {
        this.parseAndCheck(text, HttpETag.with(value, validator));
    }
}
