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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ImmutableSetTest extends ImmutableSetTestCase<ImmutableSet<?>> implements IteratorTesting {

    // isImmutable......................................................................................................

    @Test
    public void testIsImmutableHashSetFalse() {
        this.isImmutableAndCheck(new HashSet<>(), false);
    }

    @Test
    public void testIsImmutableTreeSetFalse() {
        this.isImmutableAndCheck(new TreeSet<>(), false);
    }

    @Test
    public void testIsImmutableEmptyTrue() {
        this.isImmutableAndCheck(Sets.empty(), true);
    }

    @Test
    public void testIsImmutableOneTrue() {
        this.isImmutableAndCheck(Sets.of("only!"), true);
    }

    @Test
    public void testIsImmutableImmutableSetTrue() {
        this.isImmutableAndCheck(Sets.of("1a", "2b"), true);
    }

    @Test
    public void testIsImmutableImmutableSetUnsortedTrue() {
        final Set<String> set = Sets.hash();
        set.add("1a");
        set.add("2b");

        this.isImmutableAndCheck(Sets.immutable(set), true);
    }

    @Test
    public void testIsImmutableImmutableSetSortedTrue() {
        final Set<String> set = Sets.sorted();
        set.add("1a");
        set.add("2b");

        this.isImmutableAndCheck(Sets.immutable(set), true);
    }

    private void isImmutableAndCheck(final Set<?> set, final boolean expected) {
        assertEquals(expected,
                ImmutableSet.isImmutable(set),
                set::toString);
    }

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
    public void testImmutableSortedSet() {
        final Set<String> set = Sets.sorted();
        set.add("2b");
        set.add("3c");
        set.add("1a");

        final Set<String> immutable = this.immutableAndCheck(set);
        this.iterateAndCheck(immutable.iterator(), "1a", "2b", "3c");
    }

    @Test
    public void testImmutableSortedComparatorSet() {
        final Set<String> set = Sets.sorted(String.CASE_INSENSITIVE_ORDER);
        set.add("2B");
        set.add("3C");
        set.add("1a");

        final Set<String> immutable = this.immutableAndCheck(set);
        this.iterateAndCheck(immutable.iterator(), "1a", "2B", "3C");
    }

    private Set<String> immutableAndCheck(final Set<String> from) {
        final Set<String> immutable = Sets.immutable(from);
        assertEquals(true,
                immutable instanceof ImmutableSet,
                () -> "from " + from + " type=" + immutable.getClass().getName() + " " + immutable);
        return immutable;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableSet<?>> type() {
        return Cast.to(ImmutableSet.class);
    }
}
