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
import walkingkooka.collect.iterator.IteratorTesting;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AutoExpandArrayListTest implements ListTesting2<AutoExpandArrayList<String>, String>, IteratorTesting {

    @Test
    public void testGetSize() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        this.getAndCheck(list, 0, null);
    }

    @Test
    public void testGetGreaterThanSize() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        this.getAndCheck(list, 10, null);
        this.sizeAndCheck(list, 0);
    }

    @Test
    public void testSetAndGet() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.set(10, "!");

        this.getAndCheck(list, 10, "!");
        this.sizeAndCheck(list, 11);
    }

    @Test
    public void testIterator() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.add("A");
        list.add("B");
        list.add("C");

        this.iterateAndCheck(
            list.iterator(),
            "A", "B", "C"
        );
    }

    @Test
    public void testSetExpandsWhenEmpty() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.set(0, "A");

        this.getAndCheck(list, 0, "A");
        this.sizeAndCheck(list, 1);

        this.checkEquals(
            Lists.of("A"),
            list
        );
    }

    @Test
    public void testSetExpandsWhenEmpty2() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.set(1, "B");

        this.getAndCheck(list, 1, "B");
        this.sizeAndCheck(list, 2);

        this.checkEquals(
            Lists.of(null, "B"),
            list
        );
    }

    @Test
    public void testSetExpandsWhenEmpty3() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.set(10, "X");

        this.getAndCheck(list, 10, "X");
        this.sizeAndCheck(list, 11);

        this.checkEquals(
            Arrays.asList(null, null, null, null, null, null, null, null, null, null, "X"),
            list
        );
    }

    @Test
    public void testSetMany() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.set(1, "B");
        list.set(2, "C");
        list.set(0, "A");

        this.sizeAndCheck(list, 3);

        this.checkEquals(
            Lists.of("A", "B", "C"),
            list
        );
    }

    @Test
    public void testAddAndSet() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.add("A");
        list.add("B");
        list.set(0, "!");

        this.sizeAndCheck(list, 2);

        this.checkEquals(
            Lists.of("!", "B"),
            list
        );
    }

    @Test
    public void testAddAndSet2() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.add("A");
        list.add("B");
        list.set(0, "!");
        list.set(3, "!!");

        this.sizeAndCheck(list, 4);

        this.checkEquals(
            Lists.of("!", "B", null, "!!"),
            list
        );
    }

    @Test
    public void testRemoveInvalidFails() {
        final AutoExpandArrayList list = AutoExpandArrayList.empty();
        list.add("A");
        list.add("B");

        assertThrows(
            IndexOutOfBoundsException.class,
            () -> list.remove(2)
        );
    }

    @Override
    public AutoExpandArrayList<String> createList() {
        return AutoExpandArrayList.empty();
    }

    @Override
    public Class<AutoExpandArrayList<String>> type() {
        return Cast.to(AutoExpandArrayList.class);
    }
}
