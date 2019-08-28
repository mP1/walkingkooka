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

import java.util.Iterator;
import java.util.Set;

public final class ImmutableSetNonSingletonTest extends ImmutableSetTestCase2<ImmutableSetNonSingleton<String>> {

    private final static String ELEMENT1 = "1a";
    private final static String ELEMENT2 = "2b";

    @Test
    public void testAddFails() {
        this.addFails(this.createSet(), "fails!!!");
    }

    @Test
    public void testContains() {
        this.containsAndCheck(this.createSet(), ELEMENT1);
    }

    @Test
    public void testContains2() {
        this.containsAndCheck(this.createSet(), ELEMENT2);
    }

    @Test
    public void testIterator() {
        final Iterator<String> elements = this.elements().iterator();

        this.iterateAndCheck(this.createSet().iterator(), elements.next(), elements.next());
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createSet(), ELEMENT1);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createSet(), 2);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSet(), this.elements().toString());
    }

    @Override
    public ImmutableSetNonSingleton<String> createSet() {
        return ImmutableSetNonSingleton.withNonSingleton(this.elements());
    }

    private Set<String> elements() {
        final Set<String> set = Sets.ordered();
        set.add(ELEMENT1);
        set.add(ELEMENT2);
        return set;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableSetNonSingleton<String>> type() {
        return Cast.to(ImmutableSetNonSingleton.class);
    }
}
