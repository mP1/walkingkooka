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
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasLineBreakDelimiterTesting2;
import walkingkooka.text.HasMultiLineTextTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.text.SupportsLineBreakDelimiterTesting2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CsvTsvStringListTestCase<T extends CsvTsvStringList<T>> implements DelimiterStringImmutableListTesting<T>,
    ParseStringTesting<T>,
    CanRemoveTrailingNullOrEmptyStringsTesting,
    HasCsvStringListTesting,
    HasLineBreakDelimiterTesting2<T>,
    HasMultiLineTextTesting,
    HasTsvStringListTesting,
    SupportsLineBreakDelimiterTesting2<T> {

    CsvTsvStringListTestCase() {
        super();
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

    @Override
    public final Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public final RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
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

    // multiLineText....................................................................................................

    @Test
    public final void testMultiLineTextWithoutLineBreaks() {
        this.multiLineTextAndCheck(
            this.createList("a,b"),
            TEXT_CONTEXT,
            "a,b"
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
