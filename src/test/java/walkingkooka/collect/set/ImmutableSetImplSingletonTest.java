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

package walkingkooka.collect.set;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.Predicates;

import java.util.Collections;

public final class ImmutableSetImplSingletonTest extends ImmutableSetImplTestCase2<ImmutableSetImplSingleton<String>> {

    private final static String ELEMENT = "*element*";

    @Test
    public void testWithNull() {
        this.checkEquals(
            ImmutableSetImplSingleton.singleton(null),
            Collections.singleton(null)
        );
    }

    @Test
    public void testWithNonNull() {
        final String value = "abc123";
        this.checkEquals(
            ImmutableSetImplSingleton.singleton(value),
            Collections.singleton(value)
        );
    }

    @Test
    public void testAddFails() {
        this.addFails(this.createSet(), "fails!!!");
    }

    @Test
    public void testContains() {
        this.containsAndCheck(this.createSet(), ELEMENT);
    }

    @Test
    public void testContainsNot() {
        this.containsAndCheckAbsent(this.createSet(), "NOT!");
    }

    @Test
    public void testIterator() {
        this.iterateAndCheck(this.createSet().iterator(), ELEMENT);
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createSet(), ELEMENT);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createSet(), 1);
    }

    @Test
    public void testReplaceOld() {
        final String newElement = "*newElement*";

        this.replaceAndCheck(
            ImmutableSetImplSingleton.singleton(ELEMENT),
            ELEMENT,
            newElement,
            ImmutableSetImplSingleton.with(
                Sets.of(newElement)
            )
        );
    }

    @Test
    public void testReplaceOldMissing() {
        final String newElement = "*newElement*";

        this.replaceAndCheck(
            ImmutableSetImplSingleton.singleton(ELEMENT),
            "missing",
            newElement
        );
    }

    @Test
    public void testDeleteIf() {
        this.deleteIfAndCheck(
            this.createSet(),
            Predicates.is(ELEMENT),
            Sets.immutable(Sets.empty())
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSet(), Collections.singleton(ELEMENT).toString());
    }

    @Override
    public ImmutableSetImplSingleton<String> createSet() {
        return ImmutableSetImplSingleton.singleton(ELEMENT);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableSetImplSingleton<String>> type() {
        return Cast.to(ImmutableSetImplSingleton.class);
    }
}
