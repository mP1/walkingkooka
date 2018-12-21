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

    // whitespace......................................................................................

    @Test
    public void testWhitespaceNonWhitespace() {
        this.whitespaceAndCheck("AB", 0);
    }

    @Test
    public void testWhitespaceSpaceNonWhitespace() {
        this.whitespaceAndCheck(" B", 1);
    }

    @Test
    public void testWhitespaceTabNonWhitespace() {
        this.whitespaceAndCheck("\tB", 1);
    }

    @Test
    public void testWhitespaceCrFails() {
        whitespaceInvalidCharacterFails("\rA", 1);
    }

    @Test
    public void testWhitespaceCrNlFails() {
        whitespaceInvalidCharacterFails("\r\nA", 2);
    }

    @Test
    public void testWhitespaceCrNlSpaceNonWhitespace() {
        this.whitespaceAndCheck("\r\n B", 3);
    }

    @Test
    public void testWhitespaceCrNlTabNonWhitespace() {
        this.whitespaceAndCheck("\r\n\tB", 3);
    }

    @Test
    public void testWhitespaceCrNlSpaceCrNlTabNonWhitespace() {
        this.whitespaceAndCheck("\r\n \r\n\tB", 6);
    }

    private void whitespaceAndCheck(final String text, final int positionAfter) {
        final HeaderParser parser = this.whitespace(text);
        this.checkPosition(parser, positionAfter);
    }

    private void checkPosition(final HeaderParser parser, final int position) {
        assertEquals("position", position, parser.position);
    }

    private void whitespaceInvalidCharacterFails(final String text,
                                                        final int invalidCharacterPosition) {
        try {
            this.whitespace(text);
            fail("Expected invalid character");
        } catch (final HeaderValueException expected) {
            assertEquals("message",
                    new InvalidCharacterException(text, invalidCharacterPosition).getMessage(),
                    expected.getMessage());
        }
    }

    private HeaderParser whitespace(final String text) {
        final HeaderParser parser = new HeaderParser(text) {
        };
        parser.whitespace();
        return parser;
    }

    // token......................................................................................

    @Test
    public void testTokenEmpty() {
        this.tokenAndCheck("ABC", "");
    }

    @Test
    public void testToken() {
        this.tokenAndCheck("1ABC", "1");
    }

    @Test
    public void testToken2() {
        this.tokenAndCheck("123ABC", "123");
    }

    private void tokenAndCheck(final String text,
                                   final String expectedText) {
        final HeaderParser parser = new HeaderParser(text) {
        };
        assertEquals(parser.toString(),
                expectedText,
                parser.token(CharPredicates.digit()));
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
