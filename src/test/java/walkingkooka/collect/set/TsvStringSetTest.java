
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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TsvStringSetTest implements DelimiterStringImmutableSetTesting<TsvStringSet>,
    ParseStringTesting<TsvStringSet> {

    // setElements......................................................................................................

    @Test
    public void testSetElementsIncludesNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TsvStringSet.EMPTY.setElements(
                Sets.of(null)
            )
        );
    }

    @Test
    public void testSetElementsWithEmpty() {
        assertSame(
            TsvStringSet.EMPTY,
            TsvStringSet.EMPTY.setElements(
                Sets.of(
                    "1A"
                )
            ).setElements(Sets.empty())
        );
    }

    @Test
    public void testSetElementsWithTsvStringSet() {
        final TsvStringSet tsvStringSet = TsvStringSet.EMPTY.concat("abc");

        assertSame(
            tsvStringSet,
            TsvStringSet.EMPTY.setElements(
                Sets.of(
                    "1A"
                )
            ).setElements(tsvStringSet)
        );
    }

    @Override
    public TsvStringSet createSet() {
        return TsvStringSet.EMPTY;
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
            TsvStringSet.EMPTY
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
            TsvStringSet.EMPTY.concat("A")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            "\t",
            TsvStringSet.EMPTY.concat("")
                .concat("")
        );
    }

    @Test
    public void testParseEmptyTokenEmptyTokenEmptyToken() {
        this.parseStringAndCheck(
            "\t\t",
            TsvStringSet.EMPTY.concat("")
                .concat("")
                .concat("")
        );
    }

    @Test
    public void testParseUnescapedToken() {
        final String text = "A";

        this.parseStringAndCheck(
            text,
            TsvStringSet.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedToken2() {
        final String text = "abc";

        this.parseStringAndCheck(
            text,
            TsvStringSet.EMPTY.concat(text)
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc\tdef",
            TsvStringSet.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseUnescapedTokenUnescapedTokenUnescapedToken() {
        this.parseStringAndCheck(
            "abc\tdef\tghi",
            TsvStringSet.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi")
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
            TsvStringSet.EMPTY.concat("")
        );
    }

    @Test
    public void testParseQuotedToken() {
        this.parseStringAndCheck(
            "\"A\"",
            TsvStringSet.EMPTY.concat("A")
        );
    }

    @Test
    public void testParseQuotedToken2() {
        this.parseStringAndCheck(
            "\"ABC\"",
            TsvStringSet.EMPTY.concat("ABC")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote() {
        this.parseStringAndCheck(
            "\"\"\"\"",
            TsvStringSet.EMPTY.concat("\"")
        );
    }

    @Test
    public void testParseQuotedTokenWithEscapedDoubleQuote2() {
        this.parseStringAndCheck(
            "\"a\"\"b\"",
            TsvStringSet.EMPTY.concat("a\"b")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\"\t\"def\"",
            TsvStringSet.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedTokenQuotedTokenQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\"\t\"def\"\t\"ghi\"",
            TsvStringSet.EMPTY.concat("abc")
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
    public void testParseQuotedWithComma() {
        this.parseStringAndCheck(
            "\"abc\t\"",
            TsvStringSet.EMPTY.concat("abc\t")
        );
    }

    @Test
    public void testParseQuotedWithCr() {
        this.parseStringAndCheck(
            "\"abc\r\"",
            TsvStringSet.EMPTY.concat("abc\r")
        );
    }

    @Test
    public void testParseQuotedWithNl() {
        this.parseStringAndCheck(
            "\"abc\n\"",
            TsvStringSet.EMPTY.concat("abc\n")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted() {
        this.parseStringAndCheck(
            "\"abc\"\tdef",
            TsvStringSet.EMPTY.concat("abc")
                .concat("def")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted2() {
        this.parseStringAndCheck(
            "\"abc\"\t\"def\"\"ghi\"\tjkl",
            TsvStringSet.EMPTY.concat("abc")
                .concat("def\"ghi")
                .concat("jkl")
        );
    }

    @Test
    public void testParseQuotedAndUnquoted3() {
        this.parseStringAndCheck(
            "\"abc\"\tdef\t\"ghi\"\"jkl\"\tmno",
            TsvStringSet.EMPTY.concat("abc")
                .concat("def")
                .concat("ghi\"jkl")
                .concat("mno")
        );
    }

    @Override
    public TsvStringSet parseString(final String text) {
        return TsvStringSet.parse(text);
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
        final String text = "\tabc\tdef\tg";

        this.textAndCheckAndParseCheck(
            TsvStringSet.parse(text),
            text
        );
    }

    @Test
    public void testTextWithDoubleQuotes() {
        this.textAndCheckAndParseCheck(
            TsvStringSet.EMPTY
                .concat("a1")
                .concat("\"Hello\""),
            "\"\"\"Hello\"\"\"\ta1"
        );
    }

    @Test
    public void testTextWithTab() {
        this.textAndCheckAndParseCheck(
            TsvStringSet.EMPTY
                .concat("a1")
                .concat("tab\t"),
            "a1\t\"tab\t\""
        );
    }

    @Test
    public void testTextWithCr() {
        this.textAndCheckAndParseCheck(
            TsvStringSet.EMPTY
                .concat("a1")
                .concat("cr\r"),
            "a1\t\"cr\r" +
                "\""
        );
    }

    @Test
    public void testTextWithNl() {
        this.textAndCheckAndParseCheck(
            TsvStringSet.EMPTY
                .concat("a1")
                .concat("nl\n"),
            "a1\t\"nl\n" +
                "\""
        );
    }

    @Test
    public void testTextMixed() {
        this.textAndCheckAndParseCheck(
            TsvStringSet.EMPTY
                .concat("a1")
                .concat("Hello\"\r\n,Hello2")
                .concat("nl\n"),
            "\"Hello\"\"\r\n,Hello2\"\ta1\t\"nl\n" +
                "\""
        );
    }

    private void textAndCheckAndParseCheck(final TsvStringSet set,
                                           final String expected) {
        this.textAndCheck(
            set,
            expected
        );

        // should roundtrip
        this.parseStringAndCheck(
            expected,
            set
        );
    }

    // HasTextWithSeparator.............................................................................................

    @Test
    public void testHasTextWithSeparatorDifferentSeparator() {
        this.textWithSeparatorAndCheck(
            TsvStringSet.parse("aaa\tbb\tcc"),
            ';',
            "aaa;bb;cc"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TsvStringSet> type() {
        return TsvStringSet.class;
    }
}
