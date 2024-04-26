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

import java.util.Collections;

public final class ImmutableListImplEmptyTest extends ImmutableListImplTestCase<ImmutableListImplEmpty<String>>
        implements ImmutableListTesting<ImmutableListImplEmpty<String>, String>,
        IteratorTesting {

    private final static String ELEMENT = "*element*";

    @Test
    public void testAddFails() {
        this.addFails(
                this.createList(),
                "fails!!!"
        );
    }

    @Test
    public void testContainsNotNull() {
        this.containsAndCheckAbsent(
                ImmutableListImplEmpty.empty(),
                "Hello"
        );
    }

    @Test
    public void testContainsNull() {
        this.containsAndCheckAbsent(
                ImmutableListImplEmpty.empty(),
                null
        );
    }

    @Test
    public void testGetInvalidIndexFails() {
        this.getFails(
                this.createList(),
                0
        );
    }

    @Test
    public void testIterator() {
        this.iterateAndCheck(
                this.createList().iterator()
        );
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(
                this.createList(),
                ELEMENT
        );
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(
                this.createList(),
                0
        );
    }

    @Test
    public void testAppendNew() {
        final String appended = "appended123";

        this.appendAndNewAndCheck(
                ImmutableListImplEmpty.empty(),
                appended,
                (ImmutableList)
                        ImmutableListImpl.with(
                        Lists.of(appended)
                )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createList(),
                Collections.emptyList().toString()
        );
    }

    @Override
    public ImmutableListImplEmpty<String> createList() {
        return ImmutableListImplEmpty.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableListImplEmpty<String>> type() {
        return Cast.to(ImmutableListImplEmpty.class);
    }
}
