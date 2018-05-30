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

final public class ReverseIterableTest extends IterableTestCase<ReverseIterable<String>, String> {

    // constants

    private final static Iterable<String> ITERABLE = Iterables.fake();

    // tests

    @Test
    public void testWrapNullIterableFails() {
        try {
            ReverseIterable.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testUnwrapReversedIterable() {
        final Iterable<Object> iterable = Iterables.fake();
        assertSame(iterable, ReverseIterable.wrap(ReverseIterable.wrap(iterable)));
    }

    @Test
    public void testIterator() {
        final List<String> iterable = Lists.array();
        iterable.add("1");
        iterable.add("2");
        iterable.add("3");

        final Iterator<String> iterator = ReverseIterable.wrap(iterable).iterator();
        Assert.assertEquals("3", iterator.next());
        Assert.assertEquals("2", iterator.next());
        Assert.assertEquals("1", iterator.next());
        Assert.assertFalse("iterator should be empty", iterator.hasNext());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(ReverseIterableTest.ITERABLE.toString(),
                ReverseIterable.wrap(ReverseIterableTest.ITERABLE).toString());
    }

    @Override
    protected ReverseIterable<String> createIterable() {
        return Cast.to(ReverseIterable.wrap(ReverseIterableTest.ITERABLE));
    }

    @Override
    protected Class<ReverseIterable<String>> type() {
        return Cast.to(ReverseIterable.class);
    }
}
