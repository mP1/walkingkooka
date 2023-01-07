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

import java.util.Collections;

public final class ImmutableListSingletonTest extends ImmutableListTestCase2<ImmutableListSingleton<String>> {

    private final static String ELEMENT = "*element*";

    @Test
    public void testWithNonNull() {
        ImmutableListSingleton.singleton(ELEMENT);
    }

    @Test
    public void testWithNull() {
        ImmutableListSingleton.singleton(null);
    }

    @Test
    public void testAddFails() {
        this.addFails(this.createList(), "fails!!!");
    }

    @Test
    public void testContainsNotNull() {
        this.containsAndCheck(
                ImmutableListSingleton.withElement(ELEMENT),
                ELEMENT
        );
    }

    @Test
    public void testContainsNull() {
        this.containsAndCheck(
                ImmutableListSingleton.withElement(null),
                null
        );
    }

    @Test
    public void testContainsFalseNot() {
        this.containsAndCheckAbsent(
                this.createList(),
                "NOT!"
        );
    }

    @Test
    public void testContainsFalseNotNull() {
        this.containsAndCheckAbsent(
                ImmutableListSingleton.withElement(null),
                "NOT!"
        );
    }

    @Test
    public void testGetNull() {
        this.getAndCheck(
                ImmutableListSingleton.withElement(null),
                0,
                null
        );
    }

    @Test
    public void testGetInvalidIndexFails() {
        this.getFails(this.createList(), 1);
    }

    @Test
    public void testIterator() {
        this.iterateAndCheck(this.createList().iterator(), ELEMENT);
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createList(), ELEMENT);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createList(), 1);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createList(), Collections.singleton(ELEMENT).toString());
    }

    @Override
    public ImmutableListSingleton<String> createList() {
        return ImmutableListSingleton.withElement(ELEMENT);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableListSingleton<String>> type() {
        return Cast.to(ImmutableListSingleton.class);
    }
}
