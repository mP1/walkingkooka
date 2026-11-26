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
import walkingkooka.reflect.ThrowableTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasLineBreakDelimiterTesting2;
import walkingkooka.text.HasMultiLineTextTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.text.SupportsLineBreakDelimiterTesting2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CsvTsvStringListTestCase<T extends CsvTsvStringList<T>> implements DelimiterStringImmutableListTesting2<T>,
    ParseStringTesting<T>,
    CanRemoveTrailingNullOrEmptyStringsTesting,
    HasCsvStringListTesting,
    HasCsvStringListTsvStringListTesting2<T>,
    HasLineBreakDelimiterTesting2<T>,
    HasMultiLineTextTesting,
    HasTsvStringListTesting,
    SupportsLineBreakDelimiterTesting2<T>,
    ThrowableTesting {

    CsvTsvStringListTestCase() {
        super();
    }

    // makeXXXStringList................................................................................................

    @Test
    public final void testMakeXXXStringListWithNullListFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.makeXXXStringList(
                null,
                Function.identity()
            )
        );
    }

    @Test
    public final void testMakeXXXStringListWithNullMapperFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.makeXXXStringList(
                Lists.empty(),
                null
            )
        );
    }

    @Test
    public final void testMakeXXXStringListWithMapperReturnsNullFails() {
        this.getMessageAndCheck(
            assertThrows(
                NullPointerException.class,
                () -> this.makeXXXStringList(
                    Lists.of(0),
                    (final Integer integer) -> null
                )
            ),
            "Mapper returned null"
        );
    }

    @Test
    public final void testMakeXXXStringListWithEmptyList() {
        this.makeXXXStringListAndCheck(
            Lists.empty(),
            (final Object object) -> {
                throw new UnsupportedOperationException();
            },
            this.empty()
        );
    }

    @Test
    public final void testMakeXXXStringListWithNonEmptyList() {
        this.makeXXXStringListAndCheck(
            Lists.of(123),
            (final Integer value) -> value.toString(),
            this.empty()
                .concat("123")
        );
    }

    private <E> void makeXXXStringListAndCheck(final List<E> list,
                                               final Function<E, String> mapper,
                                               final T expected) {
        this.checkEquals(
            expected,
            this.makeXXXStringList(
                list,
                mapper
            ),
            list::toString
        );
    }

    abstract <E> T makeXXXStringList(final List<E> list,
                                     final Function<E, String> mapper);
    // getCharacter.....................................................................................................

    @Test
    public final void testGetCharacterWithEmptyStringFails() {
        this.getMessageAndCheck(
            assertThrows(
                IllegalArgumentException.class,
                () -> this.empty()
                    .concat(
                        ""
                    ).getCharacter(0)
            ),
            "Empty \"0\""
        );
    }


    @Test
    public final void testGetCharacterWithMultiCharacterStringFails() {
        this.getMessageAndCheck(
            assertThrows(
                IllegalArgumentException.class,
                () -> this.empty()
                    .concat(
                        "AB"
                    ).getCharacter(0)
            ),
            "Length 2 of \"value\" expected 1 = \"AB\""
        );
    }

    // setElements......................................................................................................

    @Test
    public final void testSetElementsIncludesNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.empty()
                .setElements(
                    Lists.of(null)
                )
        );
    }

    @Test
    public final void testSetElementsWithEmpty() {
        assertSame(
            this.empty(),
            this.empty()
                .setElements(
                    Lists.of(
                        "1A"
                    )
                ).setElements(Lists.empty())
        );
    }

    @Test
    public final void testSetElementsWithCsvStringList() {
        final T list = this.empty()
            .concat("abc");

        assertSame(
            list,
            this.empty()
                .setElements(
                    Lists.of(
                        "1A"
                    )
                ).setElements(list)
        );
    }

    // delete...........................................................................................................

    @Test
    public final void testDelete() {
        this.deleteAndCheck(
            this.empty()
                .concat("a")
                .concat("b")
                .concat("c")
                .setLineBreak(2),
            "b",
            this.createList(
                Lists.of(
                    CsvTsvStringListElement.string("a"),
                    CsvTsvStringListElement.stringLineBreak("c")
                )
            )
        );
    }

    // deleteAtIndex....................................................................................................

    @Test
    public final void testDeleteAtIndex() {
        this.deleteAtIndexAndCheck(
            this.empty()
                .concat("a")
                .concat("b")
                .concat("c")
                .setLineBreak(2),
            1,
            this.createList(
                Lists.of(
                    CsvTsvStringListElement.string("a"),
                    CsvTsvStringListElement.stringLineBreak("c")
                )
            )
        );
    }

    // deleteAll........................................................................................................

    @Test
    public final void testDeleteAll() {
        this.deleteAllAndCheck(
            this.empty()
                .concat("a")
                .concat("b")
                .concat("c")
                .concat("d")
                .setLineBreak(3),
            Lists.of(
                "b",
                "c"
            ),
            this.createList(
                Lists.of(
                    CsvTsvStringListElement.string("a"),
                    CsvTsvStringListElement.stringLineBreak("d")
                )
            )
        );
    }

    // deleteIf.........................................................................................................

    @Test
    public final void testDeleteIf() {
        this.deleteIfAndCheck(
            this.empty()
                .concat("a")
                .concat("b")
                .concat("c")
                .concat("d")
                .setLineBreak(2) // deleted
                .setLineBreak(3),
            (final String string) -> Lists.of(
                "b",
                "c"
            ).contains(string),
            this.createList(
                Lists.of(
                    CsvTsvStringListElement.string("a"),
                    CsvTsvStringListElement.stringLineBreak("d")
                )
            )
        );
    }

    // swap.............................................................................................................

    @Test
    public final void testSwap() {
        this.swapAndCheck(
            this.empty()
                .concat("a")
                .concat("b")
                .setLineBreak(1)
                .concat("c")
                .concat("d"),
            1,
            2,
            this.createList(
                Lists.of(
                    CsvTsvStringListElement.string("a"),
                    CsvTsvStringListElement.string("c"),
                    CsvTsvStringListElement.stringLineBreak("b"),
                    CsvTsvStringListElement.string("d")
                )
            )
        );
    }

    // subList..........................................................................................................

    private final static String STRING1 = "AAA";

    private final static String STRING2 = "BBB";

    private final static String STRING3 = "CCC";

    private final static String STRING4 = "DDD";

    private final static String STRING5 = "EEE";
    
    private final static String[] ELEMENTS = new String[]{
        STRING1,
        STRING2,
        STRING3,
        STRING4,
        STRING5
    };

    @Test
    public final void testSubList() {
        this.subListAndCheck(
            this.createList(ELEMENTS),
            0,
            2,
            this.createList(
                STRING1,
                STRING2
            )
        );
    }

    @Test
    public final void testSubList2() {
        this.subListAndCheck(
            this.createList(ELEMENTS),
            2,
            4,
            this.createList(
                STRING3,
                STRING4
            )
        );
    }

    @Test
    public final void testSubListSubList() {
        this.subListAndCheck(
            this.createList(ELEMENTS)
                .subList(
                    1,
                    4
                ), // 2, 3, 4
            1,
            3,
            this.createList(
                STRING3,
                STRING4
            )
        );
    }


    // HasMultiLineText.................................................................................................

    @Test
    public final void testMultiLineTextWhenEmpty() {
        this.multiLineTextAndCheck(
            this.empty(),
            LineEnding.CR,
            ""
        );
    }

    @Test
    public final void testMultiLineTextCr() {
        this.multiLineTextAndCheck(
            this.createList(
                    "111",
                    "222"
                ).setLineBreak(0)
                .setLineBreak(1),
            LineEnding.CR,
            "111\r" +
                "222\r"
        );
    }

    @Test
    public final void testMultiLineTextNl() {
        this.multiLineTextAndCheck(
            this.createList(
                    "111",
                    "222"
                ).setLineBreak(0)
                .setLineBreak(1),
            LineEnding.NL,
            "111\n" +
                "222\n"
        );
    }

    @Test
    public final void testMultiLineTextNlAndStringIncludeCr() {
        this.multiLineTextAndCheck(
            this.createList(
                    "111\r",
                    "222\r"
                ).setLineBreak(0)
                .setLineBreak(1),
            LineEnding.NL,
            "111\\r\n" +
                "222\\r\n"
        );
    }

    @Test
    public final void testMultiLineTextNlAndStringIncludeNl() {
        this.multiLineTextAndCheck(
            this.createList(
                    "111\n",
                    "222\n"
                ).setLineBreak(0)
                .setLineBreak(1),
            LineEnding.NL,
            "111\\n\n" +
                "222\\n\n"
        );
    }

    @Test
    public final void testMultiLineTextCommasAndDoubleQuotes() {
        // strings also sorted so SortedSets expectations will also pass

        this.multiLineTextAndCheck(
            this.createList(
                "comma,",
                "double-quote\"",
                "last"
            ).setLineBreak(0)
                .setLineBreak(1)
                .setLineBreak(2),
            LineEnding.NL,
            "comma,\n" +
                "double-quote\"\n" +
                "last\n"
        );
    }

    private T createList(final String... strings) {
        return this.createList(
            Arrays.stream(strings)
                .map(CsvTsvStringListElement::string)
                .collect(Collectors.toList())
        );
    }

    abstract T createList(final List<CsvTsvStringListElement> strings);

    @Override
    public T createList() {
        return this.empty();
    }

    abstract T empty();

    // parse.........

    @Override
    public final void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public final void testParseEmptyString() {
        this.parseStringAndCheck(
            "",
            this.empty()
        );
    }

    @Test
    public final void testParseUnquotedCr() {
        this.parseStringAndCheck(
            "\r",
            this.empty().concat("")
                .lastLineBreak()
        );
    }

    @Test
    public final void testParseUnquotedCr2() {
        this.parseStringAndCheck(
            "a\rb",
            this.empty().concat("a")
                .lastLineBreak()
                .concat("b")
        );
    }

    @Test
    public final void testParseUnquotedCrNl() {
        this.parseStringAndCheck(
            "\r\n",
            this.empty().concat("")
                .lastLineBreak()
        );
    }

    @Test
    public final void testParseUnquotedCrNl2() {
        this.parseStringAndCheck(
            "a\r\nb",
            this.empty().concat("a")
                .lastLineBreak()
                .concat("b")
        );
    }

    @Test
    public final void testParseUnquotedNl() {
        this.parseStringAndCheck(
            "\n",
            this.empty().concat("")
                .lastLineBreak()
        );
    }

    @Test
    public final void testParseUnquotedNl2() {
        this.parseStringAndCheck(
            "a\nb",
            this.empty().concat("a")
                .lastLineBreak()
                .concat("b")
        );
    }

    @Test
    public final void testParseUnquotedNl3() {
        this.parseStringAndCheck(
            "a\n",
            this.empty().concat("a")
                .lastLineBreak()
                .concat("")
        );
    }

    @Test
    public final void testParseUnquotedDoubleQuote() {
        this.parseStringInvalidCharacterFails(
            "a\"b",
            '\"'
        );
    }

    @Test
    public final void testParseUnescapedToken() {
        final String text = "A";

        this.parseStringAndCheck(
            text,
            this.empty()
                .concat(text)
        );
    }

    @Test
    public final void testParseUnescapedToken2() {
        final String text = "abc";

        this.parseStringAndCheck(
            text,
            this.empty()
                .concat(text)
        );
    }

    @Test
    public final void testParseUnescapedTokenSpacesBefore() {
        final String text = " A";

        this.parseStringAndCheck(
            text,
            this.empty()
                .concat(text)
        );
    }

    @Test
    public final void testParseUnescapedTokenSpacesAfter() {
        final String text = "A ";

        this.parseStringAndCheck(
            text,
            this.empty()
                .concat(text)
        );
    }

    @Test
    public final void testParseUnescapedTokenSpacesBeforeAndAfter() {
        final String text = "A ";

        this.parseStringAndCheck(
            text,
            this.empty()
                .concat(text)
        );
    }

    @Test
    public final void testParseEmptyQuotedToken() {
        this.parseStringAndCheck(
            "\"\"",
            this.empty()
                .concat("")
        );
    }

    @Test
    public final void testParseQuotedToken() {
        this.parseStringAndCheck(
            "\"A\"",
            this.empty()
                .concat("A")
        );
    }

    @Test
    public final void testParseQuotedToken2() {
        this.parseStringAndCheck(
            "\"ABC\"",
            this.empty()
                .concat("ABC")
        );
    }

    @Test
    public final void testParseQuotedTokenWithEscapedDoubleQuote() {
        this.parseStringAndCheck(
            "\"\"\"\"",
            this.empty()
                .concat("\"")
        );
    }

    @Test
    public final void testParseQuotedTokenWithEscapedDoubleQuote2() {
        this.parseStringAndCheck(
            "\"a\"\"b\"",
            this.empty()
                .concat("a\"b")
        );
    }

    @Test
    public final void testParseMissingTerminateQuote() {
        this.parseStringFails(
            "\"abc",
            new EndOfTextException("Missing terminating '\"'")
        );
    }

    @Test
    public final void testParseQuotedWithCr() {
        this.parseStringAndCheck(
            "\"abc\r\"",
            this.empty()
                .concat("abc\r")
        );
    }

    @Test
    public final void testParseQuotedWithNl() {
        this.parseStringAndCheck(
            "\"abc\n\"",
            this.empty()
                .concat("abc\n")
        );
    }

    @Test
    public final void testParseQuotedCrUnquoted() {
        this.parseStringAndCheck(
            "\"abc\"\rdef",
            this.empty()
                .concat("abc")
                .lastLineBreak()
                .concat("def")
        );
    }

    @Test
    public final void testParseQuotedCrNlUnquoted() {
        this.parseStringAndCheck(
            "\"abc\"\r\ndef",
            this.empty()
                .concat("abc")
                .lastLineBreak()
                .concat("def")
        );
    }

    @Test
    public final void testParseQuotedNlUnquoted() {
        this.parseStringAndCheck(
            "\"abc\"\ndef",
            this.empty()
                .concat("abc")
                .lastLineBreak()
                .concat("def")
        );
    }

    @Test
    public final void testParseQuotedTokenCrQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\"\r\"def\"",
            this.empty()
                .concat("abc")
                .lastLineBreak()
                .concat("def")
        );
    }

    @Test
    public final void testParseQuotedTokenNlQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\"\n\"def\"",
            this.empty()
                .concat("abc")
                .lastLineBreak()
                .concat("def")
        );
    }

    @Test
    public final void testParseQuotedTokenCrNlQuotedToken() {
        this.parseStringAndCheck(
            "\"abc\"\r\n\"def\"",
            this.empty()
                .concat("abc")
                .lastLineBreak()
                .concat("def")
        );
    }

    // lineBreak........................................................................................................

    @Test
    public final void testLineBreak() {
        final T list = this.createList("a", "b");

        this.lineBreakAndCheck(
            list,
            0,
            false
        );
    }

    @Test
    public final void testLineBreak2() {
        final T list = this.createList("a", "b");

        this.lineBreakAndCheck(
            list,
            1,
            false
        );
    }

    // setLineBreak.....................................................................................................

    @Test
    public final void testSetLineBreakSame() {
        final T list = this.createList("a", "b")
            .setLineBreak(0);

        this.setLineBreakAndCheck(
            list,
            0,
            list
        );
    }

    @Test
    public final void testSetLineBreakWithDifferent() {
        final T list = this.createList("a", "b");

        // false -> true
        this.setLineBreakAndCheck(
            list,
            0,
            this.createList(
                Lists.of(
                    CsvTsvStringListElement.stringLineBreak("a"),
                    CsvTsvStringListElement.string("b")
                )
            )
        );
    }

    // removeLineBreak..................................................................................................

    @Test
    public final void testRemoveLineBreakSame() {
        final T list = this.createList("a,b");

        this.removeLineBreakAndCheck(
            list,
            0,
            list
        );
    }

    @Test
    public final void testRemoveLineBreakWithDifferent() {
        final T list = this.createList("a", "b")
            .setLineBreak(1);

        this.removeLineBreakAndCheck(
            list,
            1,
            this.createList(
                "a",
                "b"
            )
        );
    }

    @Test
    public final void testRemoveLineBreakWithDifferent2() {
        final T list = this.createList("a", "b")
            .setLineBreak(0)
            .setLineBreak(1);

        this.removeLineBreakAndCheck(
            list,
            1,
            this.createList(
                Lists.of(
                    CsvTsvStringListElement.stringLineBreak("a"),
                    CsvTsvStringListElement.string("b")
                )
            )
        );
    }

    @Override
    public final T createHasLineBreakDelimiter() {
        return this.createList();
    }

    @Override
    public final T createSupportsLineBreakDelimiter() {
        return this.createList();
    }

    @Override
    public final T createHasCsvStringListTsvStringList() {
        return this.createList();
    }

    // multiLineText....................................................................................................

    @Test
    public final void testMultiLineTextWithoutLineBreaks() {
        this.multiLineTextAndCheck(
            this.createList("a,b"),
            TEXT_CONTEXT,
            "a,b"
        );
    }

    // setAllLineBreaks.................................................................................................

    @Test
    public final void testSetAllLineBreaks() {
        this.multiLineTextAndCheck(
            this.createList("aaa", "bbb")
                .setAllLineBreaks(),
            TEXT_CONTEXT,
            "aaa\n" +
                "bbb\n"
        );
    }

    // equals...........................................................................................................

    @Test
    public final void testEqualsDifferentLineBreaks() {
        final T list = this.createList("a", "b", "c");

        this.checkNotEquals(
            list,
            list.setLineBreak(0)
        );
    }

    @Test
    public final void testEqualsDifferentLineBreaks2() {
        final T list = this.createList("a", "b", "c");

        this.checkNotEquals(
            list.setLineBreak(0),
            list.setLineBreak(0)
                .setLineBreak(1)
        );
    }
}
