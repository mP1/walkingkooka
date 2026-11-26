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
import walkingkooka.Cast;
import walkingkooka.predicate.Predicates;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ImmutableListImplNonSingletonSubListTest extends ImmutableListImplNotEmptyTestCase<ImmutableListImplNonSingletonSubList<String>> {

    private final static String ELEMENT1 = "1a";
    private final static String ELEMENT2 = "2b";
    private final static String ELEMENT3 = "3c";
    private final static String ELEMENT4 = "4d";

    private final static Object[] ELEMENTS = new Object[]{
        ELEMENT1,
        ELEMENT2,
        ELEMENT3,
        ELEMENT4
    };

    @Override
    public void testAllConstructorsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testAddFails() {
        this.addFails(this.createList(), "fails!!!");
    }

    @Test
    public void testContains() {
        this.containsAndCheck(this.createList(), ELEMENT1);
    }

    @Test
    public void testContains2() {
        this.containsAndCheck(this.createList(), ELEMENT2);
    }

    @Test
    public void testGetNull() {
        this.getAndCheck(
            ImmutableListImplNonSingletonSubList.with(
                Lists.of(null, ELEMENT2)), 0, null);
    }

    @Test
    public void testGetInvalidIndexFails() {
        this.getFails(this.createList(), 3);
    }

    @Test
    public void testGet() {
        this.getAndCheck(
            new ImmutableListImplNonSingleton<>(
                ELEMENTS.clone()
            ).subList(
                0,
                3
            ),
            0,
            ELEMENT1
        );
    }

    @Test
    public void testGet2() {
        this.getAndCheck(
            new ImmutableListImplNonSingleton<>(
                ELEMENTS.clone()
            ).subList(
                1,
                4
            ),
            1,
            ELEMENT3
        );
    }

    @Test
    public void testGet3() {
        this.getAndCheck(
            new ImmutableListImplNonSingleton<>(
                ELEMENTS.clone()
            ).subList(
                2,
                4
            ),
            1,
            ELEMENT4
        );
    }

    @Test
    public void testIterator() {
        this.iterateAndCheck(
            this.createList().iterator(),
            this.elements()
        );
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createList(), ELEMENT1);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createList(), 2);
    }

    @Test
    public void testSwap() {
        final String a = "1a";
        final String b = "2b";
        final String c = "3c";

        this.swapAndCheck(
            ImmutableListImplNonSingletonSubList.with(
                Lists.of(a, b, c)
            ),
            1,
            2,
            ImmutableListImplNonSingletonSubList.with(
                Lists.of(a, c, b)
            )
        );
    }

    @Test
    public void testDeleteNull() {
        final ImmutableListImplNonSingletonSubList<String> list = this.createList();

        assertSame(
            list.delete(null),
            list
        );
    }

    @Test
    public void testDeleteAll() {
        this.deleteAllAndCheck(
            this.createList(),
            Lists.of(this.elements()),
            Lists.empty()
        );
    }

    @Test
    public void testDeleteIf() {
        this.deleteIfAndCheck(
            this.createList(),
            Predicates.is(ELEMENT1),
            ImmutableListImplNonSingletonSubList.with(
                Lists.of(ELEMENT2)
            )
        );
    }

    @Test
    public void testSubListEmpty() {
        this.subListAndCheck(
            this.createList(),
            0,
            0,
            ImmutableListImpl.empty()
        );
    }

    @Test
    public void testSubListEmpty2() {
        this.subListAndCheck(
            this.createList(),
            1,
            1,
            ImmutableListImpl.empty()
        );
    }

    @Test
    public void testSubList() {
        this.subListAndCheck(
            new ImmutableListImplNonSingleton(
                ELEMENTS.clone()
            ),
            0,
            1,
            new ImmutableListImplSingleton<>(
                ELEMENT1
            )
        );
    }

    @Test
    public void testSubList2() {
        this.subListAndCheck(
            new ImmutableListImplNonSingleton(
                ELEMENTS.clone()
            ),
            1,
            2,
            new ImmutableListImplSingleton<>(
                ELEMENT2
            )
        );
    }

    @Test
    public void testSubList3() {
        this.subListAndCheck(
            new ImmutableListImplNonSingletonSubList<>(
                ELEMENTS.clone(),
                0,
                4
            ),
            3,
            4,
            new ImmutableListImplSingleton<>(
                ELEMENT4
            )
        );
    }

    @Test
    public void testSubList4() {
        final Object[] elements = ELEMENTS.clone();

        this.subListAndCheck(
            new ImmutableListImplNonSingletonSubList<>(
                elements,
                0,
                4
            ),
            0,
            2,
            new ImmutableListImplNonSingletonSubList<>(
                elements,
                0,
                2
            )
        );
    }

    @Test
    public void testSubList5() {
        final Object[] elements = ELEMENTS.clone();

        this.subListAndCheck(
            new ImmutableListImplNonSingleton<>(
                elements
            ),
            0,
            2,
            new ImmutableListImplNonSingletonSubList<>(
                elements,
                0,
                2
            )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createList(),
            Arrays.toString(this.elements())
        );
    }

    @Override
    public ImmutableListImplNonSingletonSubList<String> createList() {
        return new ImmutableListImplNonSingletonSubList(
            this.elements(),
            0,
            2
        );
    }

    private String[] elements() {
        return new String[]{
            ELEMENT1,
            ELEMENT2
        };
    }

    // CanFirstOrEmpty..................................................................................................

    @Test
    public void testFirstOrEmptyWhenEmpty() {
        this.firstOrEmptyAndCheck(
            this.createList(),
            ELEMENT1
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableListImplNonSingletonSubList<String>> type() {
        return Cast.to(ImmutableListImplNonSingletonSubList.class);
    }
}
