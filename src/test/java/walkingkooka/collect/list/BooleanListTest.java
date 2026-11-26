package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.PublicClassTesting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public class BooleanListTest implements ListTesting2<BooleanList, Boolean>,
    PublicClassTesting<BooleanList>,
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

    // subList..........................................................................................................

    private final static List<Boolean> ELEMENTS = Lists.of(
        true,
        true,
        false,
        false,
        true
    );

    @Test
    public void testSubList() {
        this.subListAndCheck(
            new BooleanList(ELEMENTS),
            0,
            2,
            new BooleanList(
                Lists.of(
                    true,
                    true
                )
            )
        );
    }

    @Test
    public void testSubList2() {
        this.subListAndCheck(
            new BooleanList(ELEMENTS),
            2,
            4,
            new BooleanList(
                Lists.of(
                    false,
                    false
                )
            )
        );
    }

    @Test
    public void testSubListSubList() {
        this.subListAndCheck(
            new BooleanList(ELEMENTS)
                .subList(
                    1,
                    4
                ), // true, false, false
            1,
            3,
            new BooleanList(
                Lists.of(
                    false,
                    false
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

    // CanFirstOrEmpty..................................................................................................

    @Test
    public void testFirstOrEmptyWhenEmpty() {
        this.firstOrEmptyAndCheck(
            BooleanList.EMPTY
        );
    }

    @Test
    public void testFirstOrEmptyWhenNotEmptyTrue() {
        this.firstOrEmptyAndCheck(
            BooleanList.EMPTY.concat(
                true
            ),
            true
        );
    }

    @Test
    public void testFirstOrEmptyWhenNotEmptyFalse() {
        this.firstOrEmptyAndCheck(
            BooleanList.EMPTY.concat(
                false
            ),
            false
        );
    }

    // class............................................................................................................

    @Override
    public Class<BooleanList> type() {
        return BooleanList.class;
    }
}
