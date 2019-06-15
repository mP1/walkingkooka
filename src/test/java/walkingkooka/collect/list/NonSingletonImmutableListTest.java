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

import java.util.Iterator;
import java.util.List;

public final class NonSingletonImmutableListTest extends ImmutableListTestCase2<NonSingletonImmutableList<String>> {

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
        this.getAndCheck(NonSingletonImmutableList.with(Lists.of(null, ELEMENT2)),0,  null);
    }

    @Test
    public void testGetInvalidIndexFails() {
        this.getFails(this.createList(), 3);
    }

    @Test
    public void testIterator() {
        final Iterator<String> elements = this.elements().iterator();

        this.iterateAndCheck(this.createList().iterator(), elements.next(), elements.next());
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
    public void testToString() {
        this.toStringAndCheck(this.createList(), this.elements().toString());
    }

    @Override
    public NonSingletonImmutableList<String> createList() {
        return NonSingletonImmutableList.with(this.elements());
    }

    private List<String> elements() {
        final List<String> list = Lists.array();
        list.add(ELEMENT1);
        list.add(ELEMENT2);
        return list;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<NonSingletonImmutableList<String>> type() {
        return Cast.to(NonSingletonImmutableList.class);
    }
}
