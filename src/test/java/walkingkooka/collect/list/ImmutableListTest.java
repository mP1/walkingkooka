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
import walkingkooka.reflect.PublicClassTesting;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ImmutableListTest implements ImmutableListTesting<ImmutableList<String>, String>,
    PublicClassTesting<ImmutableList<String>> {

    // collector........................................................................................................

    @Test
    public void testCollector() {
        final List<String> list = Lists.of(
            "1A",
            "2B",
            "3C"
        );

        final ImmutableList<String> collected = list.stream()
            .collect(ImmutableList.collector());

        this.checkEquals(
            list,
            collected
        );
    }

    @Override
    public void testDeleteAllWithEmptyCollection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDeleteIfWithNeverPredicate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSwapSameIndices() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSwapDifferentSizeFails() {
        assertThrows(
            UnsupportedOperationException.class,
            () -> new TestImmutableList(
                Lists.of(
                    "111",
                    "222",
                    "333"
                )
            ).subList(1, 2)
        );
    }

    @Override
    public ImmutableList<String> createList() {
        return new TestImmutableList(
            Lists.empty()
        );
    }

    private static final class TestImmutableList extends AbstractList<String> implements ImmutableList<String> {

        TestImmutableList(final List<String> list) {
            super();
            this.list = list;
        }

        @Override
        public String get(int index) {
            return this.list.get(index);
        }

        @Override
        public int size() {
            return this.list.size();
        }

        private final List<String> list;

        @Override
        public ImmutableList<String> setElements(final Collection<String> elements) {
            return this.equals(elements) ?
                this :
                new TestImmutableList(
                    new ArrayList<>(elements)
                );
        }

        @Override
        public ImmutableList<String> subList(final int from,
                                             final int to) {
            return this.subListMaybeSame(
                from,
                to
            );
        }

        @Override
        public ImmutableList<String> setElementsFailIfDifferent(final Collection<String> elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> swap(final int left,
                                          final int right) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> concat(final String element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> concatAll(final Collection<String> elements) {
            Objects.requireNonNull(elements, "elements");

            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> replace(final int index,
                                             final String element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> deleteAtIndex(final int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> delete(final String element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> deleteAll(final Collection<String> elements) {
            Objects.requireNonNull(elements, "elements");

            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<String> deleteIf(final Predicate<? super String> predicate) {
            Objects.requireNonNull(predicate, "predicate");

            throw new UnsupportedOperationException();
        }

        @Override
        public List<String> toList() {
            return new ArrayList<>(this.list);
        }
    }

    // class............................................................................................................

    @Override
    public Class<ImmutableList<String>> type() {
        return Cast.to(ImmutableList.class);
    }
}
