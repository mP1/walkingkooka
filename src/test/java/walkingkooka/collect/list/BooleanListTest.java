package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;

public class BooleanListTest implements ListTesting2<BooleanList, Boolean>,
    ClassTesting<BooleanList>,
    ImmutableListTesting<BooleanList, Boolean> {

    private final static Boolean BOOLEAN1 = true;

    private final static Boolean BOOLEAN2 = false;

    // list.............................................................................................................

    @Test
    public void testGet() {
        this.getAndCheck(
            this.createList(),
            0, // index
            BOOLEAN1 // expected
        );
    }

    @Test
    public void testGet2() {
        this.getAndCheck(
            this.createList(),
            1, // index
            BOOLEAN2 // expected
        );
    }

    @Test
    public void testSetFails() {
        this.setFails(
            this.createList(),
            0, // index
            BOOLEAN1 // expected
        );
    }

    // setElements......................................................................................................

    @Test
    public void testWithDoesntDoubleWrap() {
        final BooleanList list = this.createList();
        assertSame(
            list,
            list.setElements(list)
        );
    }

    @Test
    public void testSetElementsWithEmpty() {
        assertSame(
            BooleanList.EMPTY,
            new BooleanList(
                Lists.of(
                    true,
                    false,
                    null
                )
            ).setElements(Lists.empty())
        );
    }

    // removeIndex......................................................................................................

    @Test
    public void testRemoveIndexFails() {
        final BooleanList list = this.createList();

        this.removeIndexFails(
            list,
            0
        );
    }

    @Test
    public void testRemoveElementFails() {
        final BooleanList list = this.createList();

        this.removeFails(
            list,
            list.get(0)
        );
    }

    @Test
    public void testReplaceWithNull() {
        final BooleanList strings = this.createList();

        this.replaceAndCheck(
            strings,
            1,
            (Boolean) null,
            new BooleanList(
                Lists.of(
                    BOOLEAN1,
                    null
                )
            )
        );
    }

    @Override
    public BooleanList createList() {
        return new BooleanList(
            Lists.of(
                BOOLEAN1,
                BOOLEAN2
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<BooleanList> type() {
        return BooleanList.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
