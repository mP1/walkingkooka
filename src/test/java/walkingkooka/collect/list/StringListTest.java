package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.CsvStringSet;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.HasTextWithLineBreaksTesting;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertSame;

public class StringListTest implements ListTesting2<StringList, String>,
    ClassTesting<StringList>,
    ImmutableListTesting<StringList, String>,
    HasCsvStringListTesting,
    HasTextWithLineBreaksTesting {

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

    // HasCsvStringList.................................................................................................

    @Test
    public void testCsvStringList() {
        final String csv = "aaa,bb,cc";

        this.csvStringListAndCheck(
            CsvStringSet.parse(csv),
            csv
        );
    }

    // HasTextWithLineBreaks............................................................................................

    @Test
    public void testTextWithLineBreaksWithCr() {
        this.textWithLineBreaksAndCheck(
            StringList.EMPTY.concat("111")
                .concat("222"),
            LineEnding.CR,
            "111\r" +
                "222\r"
        );
    }

    @Test
    public void testTextWithLineBreaksWithNl() {
        this.textWithLineBreaksAndCheck(
            StringList.EMPTY.concat("111")
                .concat("222"),
            LineEnding.NL,
            "111\n" +
                "222\n"
        );
    }

    @Test
    public void testTextWithLineBreaksWithELementsIncludingLineFeedAndLineFeedNl() {
        this.textWithLineBreaksAndCheck(
            StringList.EMPTY.concat("111\n")
                .concat("222"),
            LineEnding.NL,
            "111\n\n" +
                "222\n"
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
