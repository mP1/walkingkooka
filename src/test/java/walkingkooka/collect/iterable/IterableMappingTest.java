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

package walkingkooka.collect.iterable;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class IterableMappingTest implements IterableTesting2<IterableMapping<String, Integer>, Integer> {

    @Test
    public void testWithNullIteratorFails() {
        assertThrows(
            NullPointerException.class,
            () -> IterableMapping.<String, Integer>with(
                null,
                Integer::parseInt
            )
        );
    }

    @Test
    public void testWithNullMapperFunctionFails() {
        assertThrows(
            NullPointerException.class,
            () -> IterableMapping.with(
                Iterables.fake(),
                null
            )
        );
    }

    @Test
    public void testIterateAndCheck() {
        this.iterateAndCheck(
            this.createIterable()
                .iterator(),
            11,
            22,
            33
        );
    }

    @Test
    public void testRemove() {
        final List<String> values = Lists.of(
            "1",
            "2",
            "3"
        );

        final Iterable<Integer> iterable = IterableMapping.with(
            values,
            Integer::parseInt
        );
        this.iterateAndCheck(
            iterable.iterator(),
            1, 2, 3
        );
    }

    @Override
    public IterableMapping<String, Integer> createIterable() {
        return IterableMapping.with(
            Iterables.iterator(
                Iterators.array("11", "22", "33")
            ),
            new Function<String, Integer>() {
                @Override
                public Integer apply(final String string) {
                    return Integer.parseInt(string);
                }

                @Override
                public String toString() {
                    return "Integer::parseInt";
                }
            }
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createIterable(),
            "[11, 22, 33] Integer::parseInt"
        );
    }

    // class............................................................................................................

    @Override
    public Class<IterableMapping<String, Integer>> type() {
        return Cast.to(IterableMapping.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
