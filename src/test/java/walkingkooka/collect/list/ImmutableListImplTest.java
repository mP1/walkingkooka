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

package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.IteratorTesting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ImmutableListImplTest extends ImmutableListImplTestCase<ImmutableListImpl<?>> implements IteratorTesting {

    // isImmutable......................................................................................................

    @Test
    public void testIsImmutableArrayListFalse() {
        this.isImmutableAndCheck(new ArrayList<>(), false);
    }

    @Test
    public void testIsImmutableLinkedListFalse() {
        this.isImmutableAndCheck(new LinkedList<>(), false);
    }

    @Test
    public void testIsImmutableEmptyTrue() {
        this.isImmutableAndCheck(Lists.empty(), true);
    }

    @Test
    public void testIsImmutableOneTrue() {
        this.isImmutableAndCheck(Lists.of("only!"), true);
    }

    @Test
    public void testIsImmutableImmutableListTrue() {
        this.isImmutableAndCheck(Lists.of("1a", "2b"), true);
    }

    @Test
    public void testIsImmutableImmutableListTrue2() {
        final List<String> list = Lists.array();
        list.add("1a");
        list.add("2b");

        this.isImmutableAndCheck(Lists.immutable(list), true);
    }

    private void isImmutableAndCheck(final List<?> list,
                                     final boolean expected) {
        this.checkEquals(
            expected,
            list instanceof ImmutableList,
            list::toString
        );
    }

    // immutable........................................................................................................

    @Test
    public void testImmutableEmpty() {
        final List<?> list = Lists.empty();
        assertSame(list, Lists.immutable(list));
    }

    @Test
    public void testImmutableImmutableOne() {
        final List<?> list = Lists.immutable(Lists.of("1a"));
        assertSame(list, Lists.immutable(list));
    }

    @Test
    public void testImmutableList() {
        final List<String> list = Lists.array();
        list.add("1a");
        list.add("2b");
        final Iterator<String> listIterator = list.iterator();

        final List<String> immutable = this.immutableAndCheck(list);
        this.iterateAndCheck(immutable.iterator(), listIterator.next(), listIterator.next());
    }

    private List<String> immutableAndCheck(final List<String> from) {
        final List<String> immutable = Lists.immutable(from);
        this.checkEquals(true,
            immutable instanceof ImmutableListImpl,
            () -> "from " + from + " type=" + immutable.getClass().getName() + " " + immutable);
        return immutable;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableListImpl<?>> type() {
        return Cast.to(ImmutableListImpl.class);
    }
}
