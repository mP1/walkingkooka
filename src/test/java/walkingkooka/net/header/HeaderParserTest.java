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
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals(position, parser.position, "position");
    }

    private void whitespaceInvalidCharacterFails(final String text,
                                                 final int invalidCharacterPosition) {
        final HeaderValueException expected = assertThrows(HeaderValueException.class, () -> {
            this.whitespace(text);
        });
        assertEquals(new InvalidCharacterException(text, invalidCharacterPosition).getMessage(),
                expected.getMessage(),
                "message");
    }

    private HeaderParser whitespace(final String text) {
        final HeaderParser parser = new TestHeaderParser(text);
        parser.whitespace0();
        return parser;
    }

    // token..................................................................................................

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
        final HeaderParser parser = new TestHeaderParser(text);
        assertEquals(expectedText,
                parser.token(CharPredicates.digit()),
                "token in " + CharSequences.quoteAndEscape(text));
    }

    // quoted.................................................................................................

    @Test
    public void testQuotedTextEmpty() {
        this.quotedAndCheck("\"\"", false, "\"\"");
    }

    @Test
    public void testQuotedTextEmpty2() {
        this.quotedAndCheck("\"\" ", false, "\"\"");
    }

    @Test
    public void testQuotedText() {
        this.quotedAndCheck("\"A\" ", false, "\"A\"");
    }

    @Test
    public void testQuotedText2() {
        this.quotedAndCheck("\"AB\" ", false, "\"AB\"");
    }

    @Test
    public void testQuotedTextEscapedBackslash() {
        this.quotedAndCheck("\"A\\\\B\" ", true, "\"A\\\\B\"");
    }

    @Test
    public void testQuotedTextEscapedDoubleQuote() {
        this.quotedAndCheck("\"A\\\"B\" ", true, "\"A\\\"B\"");
    }

    @Test
    public void testQuotedTextEscapedTab() {
        this.quotedAndCheck("\"A\\\tB\" ", true, "\"A\\\tB\"");
    }

    @Test
    public void testQuotedTextEscaped2() {
        this.quotedAndCheck("\"A\" ", true, "\"A\"");
    }

    @Test
    public void testQuotedTextEscaped3() {
        this.quotedAndCheck("\"AB\" ", true, "\"AB\"");
    }

    private void quotedAndCheck(final String text,
                                final boolean escapingSupported,
                                final String expectedText) {
        final HeaderParser parser = new TestHeaderParser(text);
        assertEquals(expectedText,
                parser.quotedText(CharPredicates.ascii(), escapingSupported),
                "quoted text in " + CharSequences.quoteAndEscape(text));
    }

    // encodedText.................................................................................................

    @Test
    public void testEncodedTextEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.encodedText("");
        });
    }

    @Test
    public void testEncodedTextCharsetInvalidCharacterFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.encodedText("utf\08");
        });
    }

    @Test
    public void testEncodedTextCharsetEmptyFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.encodedText("'en'abc");
        });
    }

    @Test
    public void testEncodedTextLanguageInvalidCharacterFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.encodedText("utf-8'a\0c'abc");
        });
    }

    @Test
    public void testEncodedTextLanguageUnclosedFails() {
        assertThrows(InvalidEncodedTextHeaderException.class, () -> {
            this.encodedText("utf-8'en");
        });
    }

    @Test
    public void testEncodedTextStringInvalidCharacterFails() {
        assertThrows(InvalidEncodedTextHeaderException.class, () -> {
            this.encodedText("utf-8''ab ");
        });
    }

    @Test
    public void testEncodedText() {
        this.encodedTextAndCheck("utf-8'en'abc",
                EncodedText.with(CharsetName.UTF_8,
                        Optional.of(LanguageTagName.with("en")),
                        "abc"));
    }

    @Test
    public void testEncodedTextWithoutLanguage() {
        this.encodedTextAndCheck("utf-8''abc",
                EncodedText.with(CharsetName.UTF_8,
                        EncodedText.NO_LANGUAGE,
                        "abc"));
    }

    @Test
    public void testEncodedTextNil() {
        this.encodedTextAndCheck2("%00", "\0");
    }

    @Test
    public void testEncodedText01() {
        this.encodedTextAndCheck2("%01", "\u0001");
    }

    @Test
    public void testEncodedText20() {
        this.encodedTextAndCheck2("%20", " ");
    }

    @Test
    public void testEncodedText3f() {
        this.encodedTextAndCheck2("%3f", "\u003f");
    }

    private void encodedTextAndCheck2(final String text, final String value) {
        this.encodedTextAndCheck("utf-8'en'" + text,
                EncodedText.with(CharsetName.UTF_8,
                        Optional.of(LanguageTagName.with("en")),
                        value));
    }

    // strange encoding/decoding when loop until Character#MAX_VALUE
    @Test
    public void testEncodedTextHeaderTextRoundtrip() {
        for (int i = 0x3f; i < 50000; i++) {
            final EncodedText encodedText = EncodedText.with(CharsetName.UTF_8,
                    Optional.of(LanguageTagName.with("en")),
                    Character.valueOf((char) i).toString());
            this.encodedTextAndCheck(encodedText.toHeaderText(), encodedText);
        }
    }

    private void encodedTextAndCheck(final String text,
                                     final EncodedText expectedText) {
        assertEquals(expectedText,
                this.encodedText(text),
                "quoted text in " + CharSequences.quoteAndEscape(text));
    }

    private EncodedText encodedText(final String text) {
        return new TestHeaderParser(text).encodedText();
    }

    // events.................................................................................................

    @Test
    public void testParseSlash() {
        this.parseAndCheck("/", "[slash]");
    }

    @Test
    public void testParseWildcard() {
        this.parseAndCheck("*", "[wildcard]");
    }

    @Test
    public void testParseWildcardWildcard() {
        this.parseAndCheck("**", "[wildcard][wildcard]");
    }

    @Test
    public void testParseToken() {
        this.parseAndCheck("1", "[token-1]");
    }

    @Test
    public void testParseToken2() {
        this.parseAndCheck("123", "[token-123]");
    }

    @Test
    public void testParseTokenWildcardToken() {
        this.parseAndCheck("123*456", "[token-123][wildcard][token-456]");
    }

    @Test
    public void testParseTokenSlashToken() {
        this.parseAndCheck("123/456", "[token-123][slash][token-456]");
    }

    @Test
    public void testParseSpaceToken() {
        this.parseAndCheck(" 1", "[ws][token-1]");
    }

    @Test
    public void testParseTabToken() {
        this.parseAndCheck("\t1", "[ws][token-1]");
    }

    @Test
    public void testParseCrNlSpaceToken() {
        this.parseAndCheck("\r\n 1", "[ws][token-1]");
    }

    @Test
    public void testParseCrNlTabToken() {
        this.parseAndCheck("\r\n 1", "[ws][token-1]");
    }

    @Test
    public void testParseSpaceTabCrNlSpaceTabToken() {
        this.parseAndCheck(" \t\r\n \t1", "[ws][token-1]");
    }

    @Test
    public void testParseTokenSpace() {
        this.parseAndCheck("1 ", "[token-1][ws]");
    }

    @Test
    public void testParseTokenTab() {
        this.parseAndCheck("1\t", "[token-1][ws]");
    }

    @Test
    public void testParseTokenCrNlSpace() {
        this.parseAndCheck("1\r\n ", "[token-1][ws]");
    }

    @Test
    public void testParseTokenCrNlTab() {
        this.parseAndCheck("1\r\n\t", "[token-1][ws]");
    }

    @Test
    public void testParseTokenTokenSeparator() {
        this.parseAndCheck("1;", "[token-1][token-separator]");
    }

    @Test
    public void testParseTokenTokenSeparatorToken() {
        this.parseAndCheck("1;23", "[token-1][token-separator][token-23]");
    }

    @Test
    public void testParseQuotedToken() {
        this.parseAndCheck("\"1\"", "[quoted-1]");
    }

    @Test
    public void testParseQuotedTokenTokenSeparator() {
        this.parseAndCheck("\"1\",", "[quoted-1][multi]");
    }

    @Test
    public void testParseQuotedTokenQuotedToken() {
        this.parseAndCheck("\"1\"\"23\"", "[quoted-1][quoted-23]");
    }

    @Test
    public void testParseQuotedTokenSlashWildcardToken() {
        this.parseAndCheck("\"1\"/*23", "[quoted-1][slash][wildcard][token-23]");
    }


    private void parseAndCheck(final String text, final String events) {
        final StringBuilder recorded = new StringBuilder();

        new HeaderParser(text) {
            @Override
            void whitespace() {
                recorded.append("[ws]");
                this.whitespace0();
            }

            @Override
            void tokenSeparator() {
                recorded.append("[token-separator]");
            }

            @Override
            void keyValueSeparator() {
                recorded.append("[key-value-separator]");
            }

            @Override
            void multiValueSeparator() {
                recorded.append("[multi]");
            }

            @Override
            void wildcard() {
                recorded.append("[wildcard]");
                this.position++;
            }

            @Override
            void slash() {
                recorded.append("[slash]");
            }

            @Override
            void quotedText() {
                final String text = this.quotedText(CharPredicates.letterOrDigit(), true);
                recorded.append("[quoted-" + text.substring(1, text.length() - 1) + ']');
            }

            @Override
            void token() {
                recorded.append("[token-" + this.token(CharPredicates.letterOrDigit()) + "]");
            }

            @Override
            void endOfText() {

            }

            @Override
            void missingValue() {
                this.failMissingValue(VALUE_LABEL);
            }
        }.parse();

        assertEquals(events,
                recorded.toString(),
                "recorded events");
    }

    // helpers.................................................................................................

    @Override
    Void parse(final String text) {
        new TestHeaderParser(text);
        return null;
    }

    static class TestHeaderParser extends HeaderParser {
        TestHeaderParser(final String text) {
            super(text);
        }

        @Override
        void whitespace() {

        }

        @Override
        void tokenSeparator() {

        }

        @Override
        void keyValueSeparator() {

        }

        @Override
        void multiValueSeparator() {

        }

        @Override
        void wildcard() {
            this.position++;
        }

        @Override
        void slash() {

        }

        @Override
        void quotedText() {

        }

        @Override
        void token() {

        }

        @Override
        void endOfText() {

        }

        @Override
        void missingValue() {
            this.failMissingValue(VALUE_LABEL);
        }
    }

    @Override
    String valueLabel() {
        return VALUE_LABEL;
    }

    private final static String VALUE_LABEL = "Value";

    @Override
    protected Class<HeaderParser> type() {
        return Cast.to(HeaderParser.class);
    }
}
