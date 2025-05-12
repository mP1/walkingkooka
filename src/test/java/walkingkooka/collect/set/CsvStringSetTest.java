/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.collect.set;

import org.junit.jupiter.api.Test;
import walkingkooka.EndOfTextException;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasTextTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CsvStringSetTest implements ImmutableSetTesting<CsvStringSet, String>,
    ParseStringTesting<CsvStringSet>,
    HasTextTesting {

    // setElements......................................................................................................

    @Test
    public void testSetElementsIncludesNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> CsvStringSet.EMPTY.setElements(
                Sets.of(null)
            )
        );
    }

    @Test
    public void testSetElementsWithEmpty() {
        assertSame(
            CsvStringSet.EMPTY,
            CsvStringSet.EMPTY.setElements(
                Sets.of(
                    "1A"
                )
            ).setElements(Sets.empty())
        );
    }

    @Test
    public void testSetElementsWithCsvStringSet() {
        final CsvStringSet csvStringSet = CsvStringSet.EMPTY.concat("abc");

        assertSame(
            csvStringSet,
            CsvStringSet.EMPTY.setElements(
                Sets.of(
                    "1A"
                )
            ).setElements(csvStringSet)
        );
    }

    @Override
    public CsvStringSet createSet() {
        return CsvStringSet.EMPTY;
    }

    // ParseString......................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseEmptyString() {
        this.parseStringAndCheck(
            "",
            CsvStringSet.EMPTY
        );
    }

    @Test
    public void testParseUnquotedCr() {
        this.parseStringInvalidCharacterFails(
            "a\rb",
            '\r'
        );
    }

    @Test
    public void testParseUnquotedNl() {
        this.parseStringInvalidCharacterFails(
            "a\nb",
            '\n'
        );
    }

    @Test
    public void testParseUnquotedDoubleQuote() {
        this.parseStringInvalidCharacterFails(
            "a\"b",
            '\"'
        );
    }

    @Test
    public void testParseUnescapedAndEmptyToken() {
        this.parseStringAndCheck(
            "A,",
            CsvStringSet.EMPTY.concat("A")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            ",",
            CsvStringSet.EMPTY.concat("")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            ",,",
            CsvStringSet.EMPTY.concat("")
                .concat("")
                .concat("")
        );
    }

    @Test
    public void testParseUnescapedToken() {
        final String text = "A";

        this.parseStringAndCheck(
            text,
            CsvStringSet.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedToken2() {
        final String text = "abc";

        this.parseStringAndCheck(
            text,
            CsvStringSet.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc,def",
            CsvStringSet.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc,def,ghi",
            CsvStringSet.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi")
        );
    }

    @Test
    public void testParseQuotedTokenWithoutSeparator() {
        this.parseStringInvalidCharacterFails(
            "\"\" ,abc",
            ' '
        );
    }

    @Test
    public void testParseQuotedTokenWithoutSeparator2() {
        this.parseStringInvalidCharacterFails(
            "\"a\" ,bc",
            ' '
        );
    }

    @Test
    public void testParseEmptyQuotedToken() {
        this.parseStringAndCheck(
            "\"\"",
            CsvStringSet.EMPTY.concat("")
        );
    }

    @Test
    public void testParseQuotedToken() {
        this.parseStringAndCheck(
            "\"A\"",
            CsvStringSet.EMPTY.concat("A")
        );
    }

    @Test
    public void testParseQuotedToken2() {
        this.parseStringAndCheck(
            "\"ABC\"",
            CsvStringSet.EMPTY.concat("ABC")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote() {
        this.parseStringAndCheck(
            "\"\"\"\"",
            CsvStringSet.EMPTY.concat("\"")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote2() {
        this.parseStringAndCheck(
            "\"a\"\"b\"",
            CsvStringSet.EMPTY.concat("a\"b")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\",\"def\"",
            CsvStringSet.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\",\"def\",\"ghi\"",
            CsvStringSet.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi")
        );
    }

    @Test
    public void testParseMissingTerminateQuote() {
        this.parseStringFails(
            "\"abc",
            new EndOfTextException("Missing terminating '\"'")
        );
    }

    @Test
    public void testParseMissingTerminateQuote2() {
        this.parseStringFails(
            "\"abc\",\"def",
            new EndOfTextException("Missing terminating '\"'")
        );
    }

    @Test
    public void testParseQuotedWithComma() {
        this.parseStringAndCheck(
            "\"abc,\"",
            CsvStringSet.EMPTY.concat("abc,")
        );
    }

    @Test
    public void testParseQuotedWithCr() {
        this.parseStringAndCheck(
            "\"abc\r\"",
            CsvStringSet.EMPTY.concat("abc\r")
        );
    }

    @Test
    public void testParseQuotedWithNl() {
        this.parseStringAndCheck(
            "\"abc\n\"",
            CsvStringSet.EMPTY.concat("abc\n")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted() {
        this.parseStringAndCheck(
            "\"abc\",def",
            CsvStringSet.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted2() {
        this.parseStringAndCheck(
            "\"abc\",\"def\"\"ghi\",jkl",
            CsvStringSet.EMPTY.concat("abc")
                .concat("def\"ghi")
                .concat("jkl")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted3() {
        this.parseStringAndCheck(
            "\"abc\",def,\"ghi\"\"jkl\",mno",
            CsvStringSet.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi\"jkl")
                .concat("mno")
        );
    }

    @Override
    public CsvStringSet parseString(final String text) {
        return CsvStringSet.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // HasText..........................................................................................................

    @Test
    public void testTextWithEscapingUnnecessary() {
        final String text = ",abc,def,g";

        this.textAndCheckAndParseCheck(
            CsvStringSet.parse(text),
            text
        );
    }

    @Test
    public void testTextWithDoubleQuotes() {
        this.textAndCheckAndParseCheck(
            CsvStringSet.EMPTY
                .concat("a1")
                .concat("\"Hello\""),
            "\"\"\"Hello\"\"\",a1"
        );
    }

    @Test
    public void testTextWithComma() {
        this.textAndCheckAndParseCheck(
            CsvStringSet.EMPTY
                .concat("a1")
                .concat("comma,"),
            "a1,\"comma,\""
        );
    }

    @Test
    public void testTextWithCr() {
        this.textAndCheckAndParseCheck(
            CsvStringSet.EMPTY
                .concat("a1")
                .concat("cr\r"),
            "a1,\"cr\r" +
                "\""
        );
    }

    @Test
    public void testTextWithNl() {
        this.textAndCheckAndParseCheck(
            CsvStringSet.EMPTY
                .concat("a1")
                .concat("nl\n"),
            "a1,\"nl\n" +
                "\""
        );
    }

    @Test
    public void testTextMixed() {
        this.textAndCheckAndParseCheck(
            CsvStringSet.EMPTY
                .concat("a1")
                .concat("Hello\"\r\n,Hello2")
                .concat("nl\n"),
            "\"Hello\"\"\r\n,Hello2\",a1,\"nl\n" +
                "\""
        );
    }

    private void textAndCheckAndParseCheck(final CsvStringSet list,
                                           final String expected) {
        this.textAndCheck(
            list,
            expected
        );

        // should roundtrip
        this.parseStringAndCheck(
            expected,
            list
        );
    }

    // class............................................................................................................

    @Override
    public Class<CsvStringSet> type() {
        return CsvStringSet.class;
    }
}
