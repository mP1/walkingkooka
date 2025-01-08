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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class IteratorIterableTest implements ClassTesting2<IteratorIterable<Object>>,
    IterableTesting<IteratorIterable<Object>, Object> {

    @Test
    public void testWithNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> IteratorIterable.with(null));
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

        assertThrows(IllegalStateException.class, iterable::iterator);
    }

    @Test
    public void testToString() {
        final Iterator<Object> iterator = this.createIterator();
        this.toStringAndCheck(IteratorIterable.with(iterator), iterator.toString());
    }

    @Override
    public IteratorIterable<Object> createIterable() {
        return IteratorIterable.with(this.createIterator());
    }

    private Iterator<Object> createIterator() {
        return Iterators.array(new Object[]{"1", "2"});
    }

    @Override
    public Class<IteratorIterable<Object>> type() {
        return Cast.to(IteratorIterable.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
