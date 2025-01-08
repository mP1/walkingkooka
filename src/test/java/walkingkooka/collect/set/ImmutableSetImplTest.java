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
import walkingkooka.collect.iterator.IteratorTesting;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ImmutableSetImplTest extends ImmutableSetImplTestCase<ImmutableSetImpl<?>> implements IteratorTesting {

    // immutable........................................................................................................

    @Test
    public void testImmutableEmpty() {
        final Set<?> set = Sets.empty();
        assertSame(set, Sets.immutable(set));
    }

    @Test
    public void testImmutableUnsortedSet() {
        final Set<String> set = Sets.hash();
        set.add("1a");
        set.add("2b");
        final Iterator<String> setIterator = set.iterator();

        final Set<String> immutable = this.immutableAndCheck(set);
        this.iterateAndCheck(immutable.iterator(), setIterator.next(), setIterator.next());
    }

    @Test
    public void testImmutableSortedSet1() {
        final Set<String> set = SortedSets.tree();
        set.add("1a");

        final Set<String> immutable = this.immutableAndCheck(set);
        this.iterateAndCheck(immutable.iterator(), "1a");

        this.containsAndCheck(set, "1a", true);
        this.containsAndCheck(set, "1A", false);

        this.containsAndCheck(set, "?", false);
        ;
    }

    @Test
    public void testImmutableSortedComparatorSet1() {
        final Set<String> set = SortedSets.tree(String.CASE_INSENSITIVE_ORDER);
        set.add("1A");

        final Set<String> immutable = this.immutableAndCheck(set);
        this.iterateAndCheck(immutable.iterator(), "1A");

        this.containsAndCheck(set, "1a", true);
        this.containsAndCheck(set, "1A", true);

        this.containsAndCheck(set, "?", false);
    }

    @Test
    public void testImmutableSortedSetMany() {
        final Set<String> set = SortedSets.tree();
        set.add("2b");
        set.add("3c");
        set.add("1a");

        final Set<String> immutable = this.immutableAndCheck(set);
        this.iterateAndCheck(immutable.iterator(), "1a", "2b", "3c");

        this.containsAndCheck(set, "1a", true);
        this.containsAndCheck(set, "1A", false);

        this.containsAndCheck(set, "2b", true);
        this.containsAndCheck(set, "2B", false);

        this.containsAndCheck(set, "3c", true);
        this.containsAndCheck(set, "3C", false);
    }

    @Test
    public void testImmutableSortedComparatorSetMany() {
        final Set<String> set = SortedSets.tree(String.CASE_INSENSITIVE_ORDER);
        set.add("2B");
        set.add("3C");
        set.add("1a");

        final Set<String> immutable = this.immutableAndCheck(set);
        this.iterateAndCheck(immutable.iterator(), "1a", "2B", "3C");

        this.containsAndCheck(set, "1a", true);
        this.containsAndCheck(set, "1A", true);

        this.containsAndCheck(set, "2b", true);
        this.containsAndCheck(set, "2B", true);

        this.containsAndCheck(set, "3c", true);
        this.containsAndCheck(set, "3C", true);

        this.containsAndCheck(set, "?", false);
    }

    private void containsAndCheck(final Set<?> set,
                                  final Object contains,
                                  final boolean expected) {
        this.checkEquals(expected,
            set.contains(contains),
            () -> set + " contains " + contains);
    }

    private Set<String> immutableAndCheck(final Set<String> from) {
        final Set<String> immutable = Sets.immutable(from);
        this.checkEquals(true,
            immutable instanceof ImmutableSetImpl,
            () -> "from " + from + " type=" + immutable.getClass().getName() + " " + immutable);
        return immutable;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableSetImpl<?>> type() {
        return Cast.to(ImmutableSetImpl.class);
    }
}
