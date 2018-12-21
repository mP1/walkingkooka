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

import org.junit.Test;

public abstract class ETagHeaderParserTestCase<P extends ETagHeaderParser>
        extends HeaderParserTestCase<P, ETag> {

    ETagHeaderParserTestCase() {
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
        this.parseFails("W", ETagHeaderParser.incompleteWeakIndicator("W"));
    }

    @Test
    public final void testWeaknessWithoutQuotedValueFails() {
        this.parseFails("W/", ETagHeaderParser.missingETagValue("W/"));
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
        this.parseMissingClosingQuoteFails("\"");
    }

    @Test
    public final void testBeginQuoteFails2() {
        this.parseMissingClosingQuoteFails("\"A");
    }

    @Test
    public final void testBeginQuoteFails3() {
        this.parseMissingClosingQuoteFails( "\"'");
    }

    @Test
    public final void testWildcard() {
        this.parseAndCheck("*", ETag.wildcard());
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
        this.parseAndCheck("W/\"a\"", "a", ETagValidator.WEAK);
    }

    @Test
    public final void testWeakValue2() {
        this.parseAndCheck("W/\"0\"", "0", ETagValidator.WEAK);
    }

    @Test
    public final void testWeakValue3() {
        this.parseAndCheck("W/\"0123456789ABCDEF\"", "0123456789ABCDEF", ETagValidator.WEAK);
    }

    final void parseAndCheck(final String text, final String value) {
        this.parseAndCheck(text, value, ETagValidator.STRONG);
    }

    final void parseAndCheck(final String text, final String value, final ETagValidator validator) {
        this.parseAndCheck(text, ETag.with(value, validator));
    }
}
