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
import walkingkooka.collect.iterator.Iterators;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

final public class IteratorIterableTest extends IterableTestCase<IteratorIterable<Object>, Object> {

    @Test
    public void testWithNullIteratorFails() {
        try {
            IteratorIterable.with(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testIterable() {
        final Iterator<Object> iterator = this.createIterator();
        final IteratorIterable<Object> iterable = IteratorIterable.with(iterator);
        assertSame(iterator, iterable.iterator());
    }

    @Test
    public void testIterableTwiceFails() {
        final Iterable<Object> iterable = this.createIterable();
        iterable.iterator();
        try {
            iterable.iterator();
            Assert.fail();
        } catch (final IllegalStateException expected) {
        }
    }

    @Test
    public void testToString() {
        final Iterator<Object> iterator = this.createIterator();
        assertEquals(iterator.toString(), IteratorIterable.with(iterator).toString());
    }

    @Override
    protected IteratorIterable<Object> createIterable() {
        return IteratorIterable.with(this.createIterator());
    }

    private Iterator<Object> createIterator() {
        return Iterators.array(new Object[]{"1", "2"});
    }

    @Override
    protected Class<IteratorIterable<Object>> type() {
        return Cast.to(IteratorIterable.class);
    }
}
