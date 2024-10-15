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

import java.util.Arrays;
import java.util.HashSet;

public final class ImmutableSetImplNonSingletonTest extends ImmutableSetImplTestCase2<ImmutableSetImplNonSingleton<String>> {

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
        this.iterateAndCheck(
                this.createSet()
                        .iterator(),
                ELEMENT1,
                ELEMENT2
        );
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createSet(), ELEMENT1);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createSet(), 2);
    }

    // verify replace bug which *ALWAYS* concats
    @Test
    public void testReplaceOldMissing() {
        final String newElement = "*newElement*";

        this.replaceAndCheck(
                ImmutableSetImplNonSingleton.with(
                        Sets.of(
                                ELEMENT1,
                                ELEMENT2
                        )
                ),
                "*missing*",
                newElement,
                ImmutableSetImplNonSingleton.with(
                        Sets.of(
                                ELEMENT1,
                                ELEMENT2,
                                newElement
                        )
                )
        );
    }


    @Test
    public void testReplaceOld() {
        final String newElement = "*newElement*";

        this.replaceAndCheck(
                ImmutableSetImplNonSingleton.with(
                        Sets.of(
                                ELEMENT1,
                                ELEMENT2
                        )
                ),
                ELEMENT1,
                newElement,
                ImmutableSetImplNonSingleton.with(
                        Sets.of(
                                newElement,
                                ELEMENT2
                        )
                )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createSet(),
                new HashSet<>(
                        Arrays.asList(
                                ELEMENT1,
                                ELEMENT2
                        )
                ).toString()
        );
    }

    @Override
    public ImmutableSetImplNonSingleton<String> createSet() {
        return ImmutableSetImplNonSingleton.nonSingleton(this.elements());
    }

    private String[] elements() {
        return new String[] {
                ELEMENT1,
                ELEMENT2
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableSetImplNonSingleton<String>> type() {
        return Cast.to(ImmutableSetImplNonSingleton.class);
    }
}
