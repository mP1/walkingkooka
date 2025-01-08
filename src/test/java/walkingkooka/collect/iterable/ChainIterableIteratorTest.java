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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ChainIterableIteratorTest implements IteratorTesting, ClassTesting<ChainIterableIterator<String>> {

    @Test
    public void testIterateHasNextAndNextOnly() {
        this.iterateUsingHasNextAndCheck(
            this.createIterator(),
            "1a", "2b", "3c"
        );
    }

    @Test
    public void testIterateHasNextAndNextOnly2() {
        this.iterateUsingHasNextAndCheck(
            ChainIterableIterator.with(
                Lists.of("1a"),
                Lists.empty(),
                Lists.empty(),
                Lists.of("2b", "3c")
            ),
            "1a", "2b", "3c"
        );
    }

    @Test
    public void testIterateNextOnly() {
        this.iterateAndCheck(
            this.createIterator(),
            "1a", "2b", "3c"
        );
    }

    @Test
    public void testIterateNextOnly2() {
        this.iterateAndCheck(
            ChainIterableIterator.with(
                Lists.of("1a"),
                Lists.empty(),
                Lists.empty(),
                Lists.of("2b", "3c")
            ),
            "1a", "2b", "3c"
        );
    }

    @Test
    public void testLazyIterator() {
        final Iterator<String> iterator = ChainIterableIterator.with(
            Lists.of("1a"),
            () -> {
                throw new UnsupportedOperationException();
            }
        );

        this.checkEquals(
            "1a",
            iterator.next()
        );
        assertThrows(
            UnsupportedOperationException.class,
            () -> iterator.next()
        );
    }

    @Test
    public void testRemoveWithNextFails() {
        final List<String> iterable = Lists.array();
        iterable.add("1a");
        iterable.add("2b");

        final Iterator<String> iterator = ChainIterableIterator.with(iterable);
        iterator.next();
        iterator.remove();

        this.checkEquals(
            Lists.of("2b"),
            iterable
        );

        this.removeWithoutNextFails(
            iterator
        );
    }

    private ChainIterableIterator<String> createIterator() {
        return ChainIterableIterator.with(
            Lists.of("1a"),
            Lists.empty(),
            Lists.of("2b", "3c")
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ChainIterableIterator<String>> type() {
        return Cast.to(ChainIterableIterator.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
