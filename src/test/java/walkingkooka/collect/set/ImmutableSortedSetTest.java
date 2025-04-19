

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

package walkingkooka.collect.set;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Comparator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ImmutableSortedSetTest implements ClassTesting<ImmutableSortedSet<String>> {

    // collector........................................................................................................

    @Test
    public void testCollectorWithNullComparator() {
        final Set<String> set = Sets.of(
            "1A",
            "2B",
            "3C"
        );

        final ImmutableSortedSet<String> collected = set.stream()
            .collect(ImmutableSortedSet.collector(null));

        this.checkEquals(
            set,
            collected
        );
    }

    @Test
    public void testCollectorWithComparator() {
        final Set<String> set = Sets.of(
            "1A",
            "2B",
            "3C"
        );

        final Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;

        final ImmutableSortedSet<String> collected = set.stream()
            .collect(ImmutableSortedSet.collector(comparator));

        this.checkEquals(
            set,
            collected
        );

        assertSame(
            comparator,
            collected.comparator(),
            "comparator"
        );
    }

    // class............................................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<ImmutableSortedSet<String>> type() {
        return Cast.to(ImmutableSortedSet.class);
    }
}
