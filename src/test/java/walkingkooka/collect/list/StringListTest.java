package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.HasTextWithTextContextTesting;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertSame;

public class StringListTest implements ListTesting2<StringList, String>,
    ClassTesting<StringList>,
    CanRemoveTrailingNullOrEmptyStringsTesting,
    ImmutableListTesting<StringList, String>,
    HasCsvStringListTesting,
    HasTextWithTextContextTesting,
    HasTsvStringListTesting {

    private final static String STRING1 = "AAA";

    private final static String STRING2 = "BBB";

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
    public void testWithDoesntDoubleWrap() {
        final StringList list = this.createList();
        assertSame(
            list,
            list.setElements(list)
        );
    }

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

    // HasTextWithLineBreaks............................................................................................

    @Test
    public void testTextWithLineBreaksWithCr() {
        this.textWithTextContextAndCheck(
            StringList.EMPTY.concat("111")
                .concat("222"),
            LineEnding.CR,
            "111\r" +
                "222\r"
        );
    }

    @Test
    public void testTextWithLineBreaksWithNl() {
        this.textWithTextContextAndCheck(
            StringList.EMPTY.concat("111")
                .concat("222"),
            LineEnding.NL,
            "111\n" +
                "222\n"
        );
    }

    @Test
    public void testTextWithLineBreaksWithELementsIncludingLineFeedAndLineFeedNl() {
        this.textWithTextContextAndCheck(
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

    // class............................................................................................................

    @Override
    public Class<StringList> type() {
        return StringList.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
