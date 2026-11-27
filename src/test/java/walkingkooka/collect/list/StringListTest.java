package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.text.HasMultiLineTextTesting;
import walkingkooka.text.LineEnding;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class StringListTest implements ListTesting2<StringList, String>,
    PublicClassTesting<StringList>,
    CanRemoveTrailingNullOrEmptyStringsTesting,
    ImmutableListTesting<StringList, String>,
    HasCsvStringListTesting,
    HasMultiLineTextTesting,
    HasTsvStringListTesting,
    StringLikeListTesting2<StringList> {

    private final static String STRING1 = "AAA";

    private final static String STRING2 = "BBB";

    private final static String STRING3 = "CCC";

    private final static String STRING4 = "DDD";

    private final static String STRING5 = "EEE";

    // list.............................................................................................................

    @Test
    public void testGet() {
        this.getAndCheck(
            this.createList(),
            0, // index
            STRING1 // expected
        );
    }

    @Test
    public void testGet2() {
        this.getAndCheck(
            this.createList(),
            1, // index
            STRING2 // expected
        );
    }

    @Test
    public void testSetFails() {
        this.setFails(
            this.createList(),
            0, // index
            STRING1 // expected
        );
    }

    // setElements......................................................................................................

    @Test
    public void testSetElementsWithEmpty() {
        assertSame(
            StringList.EMPTY,
            new StringList(
                Lists.of(
                    "apple",
                    "banana",
                    "carrot"
                )
            ).setElements(Lists.empty())
        );
    }

    // removeIndex......................................................................................................

    @Test
    public void testRemoveIndexFails() {
        final StringList list = this.createList();

        this.removeIndexFails(
            list,
            0
        );
    }

    @Test
    public void testRemoveElementFails() {
        final StringList list = this.createList();

        this.removeFails(
            list,
            list.get(0)
        );
    }

    @Test
    public void testReplaceWithNull() {
        final StringList strings = this.createList();

        this.replaceAndCheck(
            strings,
            1,
            (String) null,
            new StringList(
                Lists.of(
                    STRING1,
                    null
                )
            )
        );
    }

    // subList..........................................................................................................

    private final static List<String> ELEMENTS = Lists.of(
        STRING1,
        STRING2,
        STRING3,
        STRING4,
        STRING5
    );

    @Test
    public void testSubList() {
        this.subListAndCheck(
            new StringList(ELEMENTS),
            0,
            2,
            new StringList(
                Lists.of(
                    STRING1,
                    STRING2
                )
            )
        );
    }

    @Test
    public void testSubList2() {
        this.subListAndCheck(
            new StringList(ELEMENTS),
            2,
            4,
            new StringList(
                Lists.of(
                    STRING3,
                    STRING4
                )
            )
        );
    }

    @Test
    public void testSubListSubList() {
        this.subListAndCheck(
            new StringList(ELEMENTS)
                .subList(
                    1,
                    4
                ), // 2, 3, 4
            1,
            3,
            new StringList(
                Lists.of(
                    STRING3,
                    STRING4
                )
            )
        );
    }

    @Override
    public StringList createList() {
        return new StringList(
            Lists.of(
                STRING1,
                STRING2
            )
        );
    }

    // CanFirstOrEmpty..................................................................................................

    @Test
    public void testFirstOrEmptyWhenEmpty() {
        this.firstOrEmptyAndCheck(
            StringList.EMPTY
        );
    }

    @Test
    public void testFirstOrEmptyWhenNotEmpty() {
        final String first = "111";

        this.firstOrEmptyAndCheck(
            StringList.EMPTY.concat(
                first
            ).concat("222"),
            first
        );
    }

    // CanRemoveTrailingNullOrEmptyStrings..............................................................................

    @Test
    public void testCanRemoveTrailingEmptyStringsWhenNone() {
        this.removeTrailingNullOrEmptyStringsAndCheck(
            StringList.EMPTY.concat("aaa")
                .concat("bbb")
                .concat("111")
        );
    }

    @Test
    public void testCanRemoveTrailingEmptyStringsWhenSomeNull() {
        final StringList stringList = StringList.EMPTY.concat("aaa")
            .concat("bbb")
            .concat("111");

        this.removeTrailingNullOrEmptyStringsAndCheck(
            stringList.concat(null),
            stringList
        );
    }

    @Test
    public void testCanRemoveTrailingEmptyStringsWhenSomeEmpty() {
        final StringList stringList = StringList.EMPTY.concat("aaa")
            .concat("bbb")
            .concat("111");

        this.removeTrailingNullOrEmptyStringsAndCheck(
            stringList.concat(""),
            stringList
        );
    }

    @Test
    public void testCanRemoveTrailingEmptyStringsWhenSomeNullAndEmpty() {
        final StringList stringList = StringList.EMPTY.concat("aaa")
            .concat("bbb")
            .concat("111");

        this.removeTrailingNullOrEmptyStringsAndCheck(
            stringList.concat("")
                .concat(null),
            stringList
        );
    }

    // HasCsvStringList.................................................................................................

    @Test
    public void testCsvStringList() {
        this.csvStringListAndCheck(
            StringList.EMPTY.concat("aaa")
                .concat("bbb")
                .concat("111"),
            "aaa,bbb,111"
        );
    }

    @Test
    public void testCsvStringListIncludesNull() {
        this.csvStringListAndCheck(
            StringList.EMPTY.concat("aaa")
                .concat(null)
                .concat("111"),
            "aaa,,111"
        );
    }

    @Test
    public void testCsvStringListIncludesEmptyString() {
        this.csvStringListAndCheck(
            StringList.EMPTY.concat("aaa")
                .concat("")
                .concat("111"),
            "aaa,,111"
        );
    }

    // HasMultiLineText.................................................................................................

    @Test
    public void testMultiLineTextWithCr() {
        this.multiLineTextAndCheck(
            StringList.EMPTY.concat("111")
                .concat("222"),
            LineEnding.CR,
            "111\r" +
                "222\r"
        );
    }

    @Test
    public void testMultiLineTextWithNl() {
        this.multiLineTextAndCheck(
            StringList.EMPTY.concat("111")
                .concat("222"),
            LineEnding.NL,
            "111\n" +
                "222\n"
        );
    }

    @Test
    public void testMultiLineTextWithELementsIncludingLineFeedAndLineFeedNl() {
        this.multiLineTextAndCheck(
            StringList.EMPTY.concat("111\n")
                .concat("222"),
            LineEnding.NL,
            "111\n\n" +
                "222\n"
        );
    }

    // HasTsvStringList.................................................................................................

    @Test
    public void testTsvStringList() {
        this.tsvStringListAndCheck(
            StringList.EMPTY.concat("aaa")
                .concat("bbb")
                .concat("111"),
            "aaa\tbbb\t111"
        );
    }

    @Test
    public void testTsvStringListIncludesNull() {
        this.tsvStringListAndCheck(
            StringList.EMPTY.concat("aaa")
                .concat("")
                .concat("111"),
            "aaa\t\t111"
        );
    }

    @Test
    public void testTsvStringListIncludesEmptyString() {
        this.tsvStringListAndCheck(
            StringList.EMPTY.concat("aaa")
                .concat("")
                .concat("111"),
            "aaa\t\t111"
        );
    }

    // StringLikeList...................................................................................................

    @Test
    public void testGetCharacterWithNullString() {
        this.getCharacterAndCheck(
            StringList.EMPTY.concat(null),
            0,
            null
        );
    }

    // class............................................................................................................

    @Override
    public Class<StringList> type() {
        return StringList.class;
    }
}
