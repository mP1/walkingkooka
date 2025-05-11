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
import walkingkooka.collect.iterator.IteratorTesting;

import java.util.List;

public abstract class ImmutableListImplTestCase2<S extends ImmutableListImpl<String>> extends ImmutableListImplTestCase<S>
    implements ImmutableListTesting<S, String>,
    IteratorTesting {

    ImmutableListImplTestCase2() {
        super();
    }

    @Test
    public final void testContainsAbsent() {
        this.containsAndCheckAbsent(this.createList(), "!absent!");
    }

    @Test
    public final void testConcatNull() {
        final ImmutableListImpl<String> immutableList = this.createList();

        final List<String> elements = immutableList.toList();
        elements.add(null);

        this.toListAndCheck(
            immutableList.concat(null),
            elements
        );
    }

    @Test
    public final void testConcatNonNull() {
        final ImmutableListImpl<String> immutableList = this.createList();

        final List<String> elements = immutableList.toList();

        final String element = "concated555";
        elements.add(element);

        this.toListAndCheck(
            immutableList.concat(element),
            elements
        );
    }
}
