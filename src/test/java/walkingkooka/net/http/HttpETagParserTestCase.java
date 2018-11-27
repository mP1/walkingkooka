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
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class HttpETagParserTestCase<P extends HttpETagParser>
        extends PackagePrivateClassTestCase<P> {

    HttpETagParserTestCase() {
        super();
    }

    // parse ...........................................................................................

    @Test(expected = NullPointerException.class)
    public final void testNullFails() {
        this.parseOne(null);
    }

    @Test
    public final void testEmpty() {
        this.parseFails("", "Text is empty");
    }

    @Test
    public final void testInvalidInitialFails() {
        this.parseFails("w");
    }

    @Test
    public final void testInvalidInitialFails2() {
        this.parseFails("0");
    }

    @Test
    public final void testInvalidQuotedCharacterFails() {
        this.parseFails("\"abc\0\"", '\0');
    }

    @Test
    public final void testInvalidQuotedCharacterFails2() {
        this.parseFails("W/\"abc\0\"", '\0');
    }

    @Test
    public final void testWFails() {
        this.parseFails("W", HttpETagParser.incompleteWeakIndicator("W"));
    }

    @Test
    public final void testWeaknessWithoutQuotedValueFails() {
        this.parseFails("W/", HttpETagParser.missingETagValue("W/"));
    }

    @Test
    public final void testWeakInvalidFails() {
        this.parseFails("WA");
    }

    @Test
    public final void testWeakInvalidFails2() {
        this.parseFails("W0");
    }

    @Test
    public final void testBeginQuoteFails() {
        final String text = "\"";
        this.parseFails(text, HttpETagParser.missingClosingQuote(text));
    }

    @Test
    public final void testBeginQuoteFails2() {
        final String text = "\"A";
        this.parseFails(text, HttpETagParser.missingClosingQuote(text));
    }

    @Test
    public final void testBeginQuoteFails3() {
        final String text = "\"'";
        this.parseFails(text, HttpETagParser.missingClosingQuote(text));
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
        this.parseAndCheck("W/\"a\"", "a", HttpETag.WEAK);
    }

    @Test
    public final void testWeakValue2() {
        this.parseAndCheck("W/\"0\"", "0", HttpETag.WEAK);
    }

    @Test
    public final void testWeakValue3() {
        this.parseAndCheck("W/\"0123456789ABCDEF\"", "0123456789ABCDEF", HttpETag.WEAK);
    }

    final void parseAndCheck(final String text, final String value) {
        this.parseAndCheck(text, value, HttpETag.NO_WEAK);
    }

    final void parseAndCheck(final String text, final String value, final Optional<HttpETagWeak> weakness) {
       this.parseAndCheck(text, HttpETag.with(value, weakness));
    }

    final void parseAndCheck(final String text, final HttpETag tag) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(text),
                tag,
                this.parseOne(text));
    }

    final void parseFails(final String text) {
        this.parseFails(text, text.length() - 1);
    }

    final void parseFails(final String text, final char pos) {
        this.parseFails(text, text.indexOf(pos));
    }

    final void parseFails(final String text, final int pos) {
        parseFails(text, HttpETagParser.invalidCharacter(pos, text));
    }

    final void parseFails(final String text, final String message) {
        try {
            this.parseOne(text);
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("Incorrect failure message for " + CharSequences.quoteIfChars(text),
                    message,
                    expected.getMessage());
        }
    }

    abstract HttpETag parseOne(final String text);
}
