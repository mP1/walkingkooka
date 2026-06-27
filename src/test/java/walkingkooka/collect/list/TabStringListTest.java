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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TabStringListTest implements DelimiterStringImmutableListTesting<TabStringList>,
    ParseStringTesting<TabStringList> {

    // setElements......................................................................................................

    @Test
    public void testSetElementsIncludesNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TabStringList.EMPTY.setElements(
                Lists.of(null)
            )
        );
    }

    @Test
    public void testSetElementsWithEmpty() {
        assertSame(
            TabStringList.EMPTY,
            TabStringList.EMPTY.setElements(
                Lists.of(
                    "1A"
                )
            ).setElements(Lists.empty())
        );
    }

    @Test
    public void testSetElementsWithTabStringList() {
        final TabStringList tabStringList = TabStringList.EMPTY.concat("abc");

        assertSame(
            tabStringList,
            TabStringList.EMPTY.setElements(
                Lists.of(
                    "1A"
                )
            ).setElements(tabStringList)
        );
    }

    @Override
    public TabStringList createList() {
        return TabStringList.EMPTY;
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
            TabStringList.EMPTY
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
            "A\t",
            TabStringList.EMPTY.concat("A")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            "\t",
            TabStringList.EMPTY.concat("")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            "\t\t",
            TabStringList.EMPTY.concat("")
                .concat("")
                .concat("")
        );
    }

    @Test
    public void testParseUnescapedToken() {
        final String text = "A";

        this.parseStringAndCheck(
            text,
            TabStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedToken2() {
        final String text = "abc";

        this.parseStringAndCheck(
            text,
            TabStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenSpacesBefore() {
        final String text = " A";

        this.parseStringAndCheck(
            text,
            TabStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenSpacesAfter() {
        final String text = "A ";

        this.parseStringAndCheck(
            text,
            TabStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenSpacesBeforeAndAfter() {
        final String text = "A ";

        this.parseStringAndCheck(
            text,
            TabStringList.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc\tdef",
            TabStringList.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc\tdef\tghi",
            TabStringList.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi")
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedTokenUnescapedTokenWithSpacesBeforeAndAfter() {
        this.parseStringAndCheck(
            " abc\tdef \t ghi ",
            TabStringList.EMPTY.concat(" abc")
                .concat("def ")
                .concat(" ghi ")
        );
    }

    @Test
    public void testParseQuotedTokenWithoutSeparator() {
        this.parseStringInvalidCharacterFails(
            "\"\" \tabc",
            ' '
        );
    }

    @Test
    public void testParseQuotedTokenWithoutSeparator2() {
        this.parseStringInvalidCharacterFails(
            "\"a\" \tbc",
            ' '
        );
    }

    @Test
    public void testParseEmptyQuotedToken() {
        this.parseStringAndCheck(
            "\"\"",
            TabStringList.EMPTY.concat("")
        );
    }

    @Test
    public void testParseQuotedToken() {
        this.parseStringAndCheck(
            "\"A\"",
            TabStringList.EMPTY.concat("A")
        );
    }

    @Test
    public void testParseQuotedToken2() {
        this.parseStringAndCheck(
            "\"ABC\"",
            TabStringList.EMPTY.concat("ABC")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote() {
        this.parseStringAndCheck(
            "\"\"\"\"",
            TabStringList.EMPTY.concat("\"")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote2() {
        this.parseStringAndCheck(
            "\"a\"\"b\"",
            TabStringList.EMPTY.concat("a\"b")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\"\t\"def\"",
            TabStringList.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\"\t\"def\"\t\"ghi\"",
            TabStringList.EMPTY.concat("abc")
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
            "\"abc\"\t\"def",
            new EndOfTextException("Missing terminating '\"'")
        );
    }

    @Test
    public void testParseQuotedWithTab() {
        this.parseStringAndCheck(
            "\"abc\t\"",
            TabStringList.EMPTY.concat("abc\t")
        );
    }

    @Test
    public void testParseQuotedWithCr() {
        this.parseStringAndCheck(
            "\"abc\r\"",
            TabStringList.EMPTY.concat("abc\r")
        );
    }

    @Test
    public void testParseQuotedWithNl() {
        this.parseStringAndCheck(
            "\"abc\n\"",
            TabStringList.EMPTY.concat("abc\n")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted() {
        this.parseStringAndCheck(
            "\"abc\"\tdef",
            TabStringList.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted2() {
        this.parseStringAndCheck(
            "\"abc\"\t\"def\"\"ghi\"\tjkl",
            TabStringList.EMPTY.concat("abc")
                .concat("def\"ghi")
                .concat("jkl")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted3() {
        this.parseStringAndCheck(
            "\"abc\"\tdef\t\"ghi\"\"jkl\"\tmno",
            TabStringList.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi\"jkl")
                .concat("mno")
        );
    }

    @Override
    public TabStringList parseString(final String text) {
        return TabStringList.parse(text);
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
        final String text = "\tabc\tdef\t\tg";

        this.textAndCheckAndParseCheck(
            TabStringList.parse(text),
            text
        );
    }

    @Test
    public void testTextWithDoubleQuotes() {
        this.textAndCheckAndParseCheck(
            TabStringList.EMPTY
                .concat("a1")
                .concat("\"Hello\""),
            "a1\t\"\"\"Hello\"\"\""
        );
    }

    @Test
    public void testTextWithTab() {
        this.textAndCheckAndParseCheck(
            TabStringList.EMPTY
                .concat("a1")
                .concat("tab\t"),
            "a1\t\"tab\t\""
        );
    }

    @Test
    public void testTextWithCr() {
        this.textAndCheckAndParseCheck(
            TabStringList.EMPTY
                .concat("a1")
                .concat("cr\r"),
            "a1\t\"cr\r" +
                "\""
        );
    }

    @Test
    public void testTextWithNl() {
        this.textAndCheckAndParseCheck(
            TabStringList.EMPTY
                .concat("a1")
                .concat("nl\n"),
            "a1\t\"nl\n" +
                "\""
        );
    }

    @Test
    public void testTextMixed() {
        this.textAndCheckAndParseCheck(
            TabStringList.EMPTY
                .concat("a1")
                .concat("Hello\"\r\n\tHello2")
                .concat("nl\n"),
            "a1\t\"Hello\"\"\r\n\tHello2\"\t\"nl\n" +
                "\""
        );
    }

    private void textAndCheckAndParseCheck(final TabStringList list,
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

    // HasTextWithSeparator.............................................................................................

    @Test
    public void testHasTextWithSeparatorDifferentSeparator() {
        this.textWithSeparatorAndCheck(
            TabStringList.parse("aaa\tbb\tcc"),
            ';',
            "aaa;bb;cc"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TabStringList> type() {
        return TabStringList.class;
    }
}
