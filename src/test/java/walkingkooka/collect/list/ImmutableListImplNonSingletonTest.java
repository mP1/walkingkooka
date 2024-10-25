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

import java.util.Arrays;

public final class ImmutableListImplNonSingletonTest extends ImmutableListImplNotEmptyTestCase<ImmutableListImplNonSingleton<String>> {

    private final static String ELEMENT1 = "1a";
    private final static String ELEMENT2 = "2b";

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
        this.getAndCheck(ImmutableListImplNonSingleton.with(Lists.of(null, ELEMENT2)),0,  null);
    }

    @Test
    public void testGetInvalidIndexFails() {
        this.getFails(this.createList(), 3);
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
                (ImmutableList)
                        ImmutableListImplNonSingleton.with(
                                Lists.of(a, b, c)
                        ),
                1,
                2,
                (ImmutableList)
                        ImmutableListImplNonSingleton.with(
                                Lists.of(a, c, b)
                        )
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
    public void testToString() {
        this.toStringAndCheck(
                this.createList(),
                Arrays.toString(this.elements())
        );
    }

    @Override
    public ImmutableListImplNonSingleton<String> createList() {
        return ImmutableListImplNonSingleton.with(this.elements());
    }

    private String[] elements() {
        return new String[] {
                ELEMENT1,
                ELEMENT2
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableListImplNonSingleton<String>> type() {
        return Cast.to(ImmutableListImplNonSingleton.class);
    }
}
