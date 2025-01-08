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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ReadOnlyIterableTest implements ClassTesting2<ReadOnlyIterable<String>>,
    IterableTesting<ReadOnlyIterable<String>, String>,
    IteratorTesting {

    // constants

    private final static Iterable<String> ITERABLES = Iterables.fake();

    // tests

    @Test
    public void testWrapNullIterableFails() {
        assertThrows(NullPointerException.class, () -> ReadOnlyIterable.wrap(null));
    }

    @Test
    public void testIterator() {
        final List<String> iterable = Lists.array();
        iterable.add("1");
        iterable.add("2");
        iterable.add("3");

        this.iterateAndCheck(ReadOnlyIterable.wrap(iterable).iterator(), "1", "2", "3");
    }

    @Test
    public void testIteratorRemoveFails() {
        final List<String> iterable = Lists.array();
        iterable.add("1");
        iterable.add("2");
        iterable.add("3");

        final Iterator<String> readOnly = ReadOnlyIterable.wrap(iterable).iterator();
        this.iterateAndCheck(readOnly, "1", "2", "3");
        this.removeUnsupportedFails(readOnly);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(ReadOnlyIterable.wrap(ITERABLES), ITERABLES.toString());
    }

    @Override
    public ReadOnlyIterable<String> createIterable() {
        return ReadOnlyIterable.wrap(ITERABLES);
    }

    @Override
    public Class<ReadOnlyIterable<String>> type() {
        return Cast.to(ReadOnlyIterable.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
