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
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.Assert.assertEquals;
import static walkingkooka.net.header.HeaderParser.fail;

public final class HeaderParserTest extends HeaderParserTestCase<HeaderParser, Void> {

    // consumeWhitespace......................................................................................

    @Test
    public void testConsumeWhitespaceNonWhitespace() {
        this.consumeWhitespaceAndCheck("AB", 0);
    }

    @Test
    public void testConsumeWhitespaceSpaceNonWhitespace() {
        this.consumeWhitespaceAndCheck(" B", 1);
    }

    @Test
    public void testConsumeWhitespaceTabNonWhitespace() {
        this.consumeWhitespaceAndCheck("\tB", 1);
    }

    @Test
    public void testConsumeWhitespaceCrFails() {
        consumeWhitespaceInvalidCharacterFails("\rA", 1);
    }

    @Test
    public void testConsumeWhitespaceCrNlFails() {
        consumeWhitespaceInvalidCharacterFails("\r\nA", 2);
    }

    @Test
    public void testConsumeWhitespaceCrNlSpaceNonWhitespace() {
        this.consumeWhitespaceAndCheck("\r\n B", 3);
    }

    @Test
    public void testConsumeWhitespaceCrNlTabNonWhitespace() {
        this.consumeWhitespaceAndCheck("\r\n\tB", 3);
    }

    @Test
    public void testConsumeWhitespaceCrNlSpaceCrNlTabNonWhitespace() {
        this.consumeWhitespaceAndCheck("\r\n \r\n\tB", 6);
    }

    private void consumeWhitespaceAndCheck(final String text, final int positionAfter) {
        final HeaderParser parser = this.consumeWhitespace(text);
        this.checkPosition(parser, positionAfter);
    }

    private void checkPosition(final HeaderParser parser, final int position) {
        assertEquals("position", position, parser.position);
    }

    private void consumeWhitespaceInvalidCharacterFails(final String text,
                                                        final int invalidCharacterPosition) {
        try {
            this.consumeWhitespace(text);
            fail("Expected invalid character");
        } catch (final HeaderValueException expected) {
            assertEquals("message",
                    new InvalidCharacterException(text, invalidCharacterPosition).getMessage(),
                    expected.getMessage());
        }
    }

    private HeaderParser consumeWhitespace(final String text) {
        final HeaderParser parser = new HeaderParser(text) {
        };
        parser.consumeWhitespace();
        return parser;
    }

    // tokenText......................................................................................

    @Test
    public void testTokenTextEmpty() {
        this.tokenTextAndCheck("ABC", "");
    }

    @Test
    public void testTokenText() {
        this.tokenTextAndCheck("1ABC", "1");
    }

    @Test
    public void testTokenText2() {
        this.tokenTextAndCheck("123ABC", "123");
    }

    private void tokenTextAndCheck(final String text,
                                   final String expectedText) {
        final HeaderParser parser = new HeaderParser(text) {
        };
        assertEquals(parser.toString(),
                expectedText,
                parser.tokenText(CharPredicates.digit()));
    }

    @Override
    Void parse(final String text) {
        new HeaderParser(text) {
        };
        return null;
    }

    @Override
    protected Class<HeaderParser> type() {
        return Cast.to(HeaderParser.class);
    }
}
