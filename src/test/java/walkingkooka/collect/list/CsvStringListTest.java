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

package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.EndOfTextException;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasTextTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CsvStringListTest implements ImmutableListTesting<CsvStringList, String>,
    ParseStringTesting<CsvStringList>,
    HasTextTesting {

    // setElements......................................................................................................

    @Test
    public void testSetElementsIncludesNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> CsvStringList.EMPTY.setElements(
                Lists.of(null)
            )
        );
    }

    @Test
    public void testSetElementsWithEmpty() {
        assertSame(
            CsvStringList.EMPTY,
            CsvStringList.EMPTY.setElements(
                Lists.of(
                    "1A"
                )
            ).setElements(Lists.empty())
        );
    }

    @Test
    public void testSetElementsWithCsvStringList() {
        final CsvStringList csvStringList = CsvStringList.EMPTY.concat("abc");

        assertSame(
            csvStringList,
            CsvStringList.EMPTY.setElements(
                Lists.of(
                    "1A"
                )
            ).setElements(csvStringList)
        );
    }

    @Override
    public CsvStringList createList() {
        return CsvStringList.EMPTY;
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
            CsvStringList.EMPTY
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
            CsvStringList.EMPTY.concat("A")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            ",",
            CsvStringList.EMPTY.concat("")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            ",,",
            CsvStringList.EMPTY.concat("")
                .concat("")
                .concat("")
        );
    }

    @Test
    public void testParseUnescapedToken() {
        final String text = "A";

        this.parseStringAndCheck(
            text,
            CsvStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedToken2() {
        final String text = "abc";

        this.parseStringAndCheck(
            text,
            CsvStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenSpacesBefore() {
        final String text = " A";

        this.parseStringAndCheck(
            text,
            CsvStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenSpacesAfter() {
        final String text = "A ";

        this.parseStringAndCheck(
            text,
            CsvStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenSpacesBeforeAndAfter() {
        final String text = "A ";

        this.parseStringAndCheck(
            text,
            CsvStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc,def",
            CsvStringList.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc,def,ghi",
            CsvStringList.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi")
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedTokenUnescapedTokenWithSpacesBeforeAndAfter() {
        this.parseStringAndCheck(
            " abc,def , ghi ",
            CsvStringList.EMPTY.concat(" abc")
                .concat("def ")
                .concat(" ghi ")
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
            CsvStringList.EMPTY.concat("")
        );
    }

    @Test
    public void testParseQuotedToken() {
        this.parseStringAndCheck(
            "\"A\"",
            CsvStringList.EMPTY.concat("A")
        );
    }

    @Test
    public void testParseQuotedToken2() {
        this.parseStringAndCheck(
            "\"ABC\"",
            CsvStringList.EMPTY.concat("ABC")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote() {
        this.parseStringAndCheck(
            "\"\"\"\"",
            CsvStringList.EMPTY.concat("\"")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote2() {
        this.parseStringAndCheck(
            "\"a\"\"b\"",
            CsvStringList.EMPTY.concat("a\"b")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\",\"def\"",
            CsvStringList.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\",\"def\",\"ghi\"",
            CsvStringList.EMPTY.concat("abc")
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
            CsvStringList.EMPTY.concat("abc,")
        );
    }

    @Test
    public void testParseQuotedWithCr() {
        this.parseStringAndCheck(
            "\"abc\r\"",
            CsvStringList.EMPTY.concat("abc\r")
        );
    }

    @Test
    public void testParseQuotedWithNl() {
        this.parseStringAndCheck(
            "\"abc\n\"",
            CsvStringList.EMPTY.concat("abc\n")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted() {
        this.parseStringAndCheck(
            "\"abc\",def",
            CsvStringList.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted2() {
        this.parseStringAndCheck(
            "\"abc\",\"def\"\"ghi\",jkl",
            CsvStringList.EMPTY.concat("abc")
                .concat("def\"ghi")
                .concat("jkl")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted3() {
        this.parseStringAndCheck(
            "\"abc\",def,\"ghi\"\"jkl\",mno",
            CsvStringList.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi\"jkl")
                .concat("mno")
        );
    }

    @Override
    public CsvStringList parseString(final String text) {
        return CsvStringList.parse(text);
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
        final String text = ",abc,def,,g";

        this.textAndCheckAndParseCheck(
            CsvStringList.parse(text),
            text
        );
    }

    @Test
    public void testTextWithDoubleQuotes() {
        this.textAndCheckAndParseCheck(
            CsvStringList.EMPTY
                .concat("a1")
                .concat("\"Hello\""),
            "a1,\"\"\"Hello\"\"\""
        );
    }

    @Test
    public void testTextWithComma() {
        this.textAndCheckAndParseCheck(
            CsvStringList.EMPTY
                .concat("a1")
                .concat("comma,"),
            "a1,\"comma,\""
        );
    }

    @Test
    public void testTextWithCr() {
        this.textAndCheckAndParseCheck(
            CsvStringList.EMPTY
                .concat("a1")
                .concat("cr\r"),
            "a1,\"cr\r" +
                "\""
        );
    }

    @Test
    public void testTextWithNl() {
        this.textAndCheckAndParseCheck(
            CsvStringList.EMPTY
                .concat("a1")
                .concat("nl\n"),
            "a1,\"nl\n" +
                "\""
        );
    }

    @Test
    public void testTextMixed() {
        this.textAndCheckAndParseCheck(
            CsvStringList.EMPTY
                .concat("a1")
                .concat("Hello\"\r\n,Hello2")
                .concat("nl\n"),
            "a1,\"Hello\"\"\r\n,Hello2\",\"nl\n" +
                "\""
        );
    }

    private void textAndCheckAndParseCheck(final CsvStringList list,
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
    public Class<CsvStringList> type() {
        return CsvStringList.class;
    }
}
