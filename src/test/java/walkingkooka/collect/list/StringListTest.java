package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;

public class StringListTest implements ListTesting2<StringList, String>,
    ClassTesting<StringList>,
    ImmutableListTesting<StringList, String> {

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
