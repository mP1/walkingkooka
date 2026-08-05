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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CanFirstOrEmptyTest implements ClassTesting<CanFirstOrEmpty<String>> {

    // firstOrEmptyCollection...........................................................................................

    @Test
    public void testFirstOrEmptyCollectionWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> CanFirstOrEmpty.firstOrEmptyCollection(null)
        );
    }

    @Test
    public void testFirstOrEmptyCollectionWithListEmpty() {
        this.firstOrEmptyCollectionAndCheck(
            Lists.empty()
        );
    }

    @Test
    public void testFirstOrEmptyCollectionWithListNotEmpty() {
        this.firstOrEmptyCollectionAndCheck(
            Lists.of(
                111,
                222,
                333
            ),
            111
        );
    }

    @Test
    public void testFirstOrEmptyCollectionWithSetEmpty() {
        this.firstOrEmptyCollectionAndCheck(
            Sets.empty()
        );
    }

    @Test
    public void testFirstOrEmptyCollectionWithSetNotEmpty() {
        this.firstOrEmptyCollectionAndCheck(
            SortedSets.of(
                111,
                222,
                333
            ),
            111
        );
    }

    @Test
    public void testFirstOrEmptyCollectionWithCanBeEmptyWhenEmpty() {
        this.firstOrEmptyCollectionAndCheck(
            Lists.immutable(
                Lists.empty()
            )
        );
    }

    @Test
    public void testFirstOrEmptyCollectionWithCanBeEmptyNotEmpty() {
        this.firstOrEmptyCollectionAndCheck(
            Lists.immutable(
                Lists.of(
                    111,
                    222,
                    333
                )
            ),
            111
        );
    }

    private <T> void firstOrEmptyCollectionAndCheck(final Collection<T> collection) {
        this.firstOrEmptyCollectionAndCheck(
            collection,
            Optional.empty()
        );
    }

    private <T> void firstOrEmptyCollectionAndCheck(final Collection<T> collection,
                                                    final T expected) {
        this.firstOrEmptyCollectionAndCheck(
            collection,
            Optional.of(expected)
        );
    }

    private <T> void firstOrEmptyCollectionAndCheck(final Collection<T> collection,
                                                    final Optional<T> expected) {
        this.checkEquals(
            expected,
            CanFirstOrEmpty.firstOrEmptyCollection(collection),
            () -> "firstOrEmptyCollection " + collection
        );
    }

    // class............................................................................................................

    @Override
    public Class<CanFirstOrEmpty<String>> type() {
        return Cast.to(CanFirstOrEmpty.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
