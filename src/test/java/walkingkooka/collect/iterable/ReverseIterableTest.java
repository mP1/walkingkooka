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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ReverseIterableTest implements ClassTesting2<ReverseIterable<String>>,
    IterableTesting<ReverseIterable<String>, String> {

    // constants

    private final static Iterable<String> ITERABLE = Iterables.fake();

    // tests

    @Test
    public void testWrapNullIterableFails() {
        assertThrows(NullPointerException.class, () -> ReverseIterable.wrap(null));
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
        this.checkEquals("3", iterator.next());
        this.checkEquals("2", iterator.next());
        this.checkEquals("1", iterator.next());
        assertFalse(iterator.hasNext(), "iterator should be empty");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createIterable(), ITERABLE.toString());
    }

    @Override
    public ReverseIterable<String> createIterable() {
        return Cast.to(ReverseIterable.wrap(ITERABLE));
    }

    @Override
    public Class<ReverseIterable<String>> type() {
        return Cast.to(ReverseIterable.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
