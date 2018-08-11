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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

final public class ReadOnlyIterableTest extends IterableTestCase<ReadOnlyIterable<String>, String> {

    // constants

    private final static Iterable<String> ITERABLES = Iterables.fake();

    // tests

    @Test
    public void testWrapNullIterableFails() {
        try {
            ReadOnlyIterable.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testIterator() {
        final List<String> iterable = Lists.array();
        iterable.add("1");
        iterable.add("2");
        iterable.add("3");

        final ReadOnlyIterable<String> readOnly = ReadOnlyIterable.wrap(iterable);
        final Iterator<String> iterator = readOnly.iterator();

        Assert.assertTrue("iterator should not be empty", iterator.hasNext());
        assertEquals("1", iterator.next());

        Assert.assertTrue("iterator should not be empty", iterator.hasNext());
        assertEquals("2", iterator.next());

        Assert.assertTrue("iterator should not be empty", iterator.hasNext());
        assertEquals("3", iterator.next());

        Assert.assertFalse("iterator should be empty", iterator.hasNext());
    }

    @Test
    public void testIteratorRemoveFails() {
        final List<String> iterable = Lists.array();
        iterable.add("1");
        iterable.add("2");
        iterable.add("3");

        final ReadOnlyIterable<String> readOnly = ReadOnlyIterable.wrap(iterable);
        final Iterator<String> iterator = readOnly.iterator();

        Assert.assertTrue("iterator should not be empty", iterator.hasNext());
        assertEquals("1", iterator.next());

        try {
            iterator.remove();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
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
