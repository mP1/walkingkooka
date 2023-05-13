
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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ChainIterableTest implements IterableTesting<ChainIterable<String>, String> {

    // constants

    private final static Iterable<String> FIRST = Lists.of("1a");

    private final static Iterable<String> SECOND = Lists.of("2b");

    // tests

    @Test
    public void testWithNullIterablesFails() {
        assertThrows(
                NullPointerException.class,
                () -> ChainIterable.with(null)
        );
    }

    @Test
    public void testWithZero() {
        //noinspection unchecked
        assertSame(
                Iterables.empty(),
                ChainIterable.with()
        );
    }

    @Test
    public void testWithOne() {
        final Iterable<?> only = Iterables.fake();
        //noinspection unchecked
        assertSame(only, ChainIterable.with(only));
    }

    @Test
    public void testIterator() {
        this.checkEquals(
                ChainIterableIterator.class,
                this.createIterable().iterator().getClass()
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createIterable(),
                Lists.of(FIRST, SECOND).toString()
        );
    }

    @Override
    public ChainIterable<String> createIterable() {
        return this.createIterable(FIRST, SECOND);
    }

    private ChainIterable<String> createIterable(final Iterable<String> first,
                                                 final Iterable<String> second) {
        //noinspection unchecked
        return Cast.to(ChainIterable.with(first, second));
    }

    @Override
    public Class<ChainIterable<String>> type() {
        return Cast.to(ChainIterable.class);
    }
}
