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

import java.util.Collections;

public final class ImmutableListImplSingletonTest extends ImmutableListImplNotEmptyTestCase<ImmutableListImplSingleton<String>> {

    private final static String ELEMENT = "*element*";

    @Test
    public void testWithNonNull() {
        ImmutableListImplSingleton.singleton(ELEMENT);
    }

    @Test
    public void testWithNull() {
        ImmutableListImplSingleton.singleton(null);
    }

    @Test
    public void testAddFails() {
        this.addFails(this.createList(), "fails!!!");
    }

    @Test
    public void testContainsNotNull() {
        this.containsAndCheck(
            ImmutableListImplSingleton.withElement(ELEMENT),
            ELEMENT
        );
    }

    @Test
    public void testContainsNull() {
        this.containsAndCheck(
            ImmutableListImplSingleton.withElement(null),
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
            ImmutableListImplSingleton.withElement(null),
            "NOT!"
        );
    }

    @Test
    public void testGetNull() {
        this.getAndCheck(
            ImmutableListImplSingleton.withElement(null),
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
    public void testDeleteAll() {
        this.deleteAllAndCheck(
            this.createList(),
            Lists.of(ELEMENT),
            Lists.empty()
        );
    }

    @Test
    public void testDeleteIf() {
        this.deleteIfAndCheck(
            this.createList(),
            Predicates.is(ELEMENT),
            Lists.empty()
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createList(), Collections.singleton(ELEMENT).toString());
    }

    @Override
    public ImmutableListImplSingleton<String> createList() {
        return ImmutableListImplSingleton.withElement(ELEMENT);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableListImplSingleton<String>> type() {
        return Cast.to(ImmutableListImplSingleton.class);
    }
}
