/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class ReadOnlyIterableTest extends IterableTestCase<ReadOnlyIterable<String>, String> {

    // constants

    private final static Iterable<String> ITERABLES = Iterables.fake();

    // tests

    @Test
    public void testWrapNullIterableFails() {
        assertThrows(NullPointerException.class, () -> {
            ReadOnlyIterable.wrap(null);
        });
    }

    @Test
    public void testIterator() {
        final List<String> iterable = Lists.array();
        iterable.add("1");
        iterable.add("2");
        iterable.add("3");

        final ReadOnlyIterable<String> readOnly = ReadOnlyIterable.wrap(iterable);
        final Iterator<String> iterator = readOnly.iterator();

        assertTrue(iterator.hasNext(), "iterator should not be empty");
        assertEquals("1", iterator.next());

        assertTrue(iterator.hasNext(), "iterator should not be empty");
        assertEquals("2", iterator.next());

        assertTrue(iterator.hasNext(), "iterator should not be empty");
        assertEquals("3", iterator.next());

        assertFalse(iterator.hasNext(), "iterator should be empty");
    }

    @Test
    public void testIteratorRemoveFails() {
        final List<String> iterable = Lists.array();
        iterable.add("1");
        iterable.add("2");
        iterable.add("3");

        final ReadOnlyIterable<String> readOnly = ReadOnlyIterable.wrap(iterable);
        final Iterator<String> iterator = readOnly.iterator();

        assertTrue(iterator.hasNext(), "iterator should not be empty");
        assertEquals("1", iterator.next());

        assertThrows(UnsupportedOperationException.class, () -> {
            iterator.remove();
        });
    }

    @Test
    public void testToString() {
        assertEquals(ReadOnlyIterableTest.ITERABLES.toString(),
                ReadOnlyIterable.wrap(ReadOnlyIterableTest.ITERABLES).toString());
    }

    @Override
    protected ReadOnlyIterable<String> createIterable() {
        return ReadOnlyIterable.wrap(ReadOnlyIterableTest.ITERABLES);
    }

    @Override
    protected Class<ReadOnlyIterable<String>> type() {
        return Cast.to(ReadOnlyIterable.class);
    }
}
