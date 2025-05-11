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

import walkingkooka.collect.iterator.Iterators;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A {@link Set} known to be immutable and holds a copy of any {@link Set} given to it.
 */
final class ImmutableSortedSetImpl<E> extends AbstractSet<E> implements ImmutableSortedSetDefaults<ImmutableSortedSet<E>, E>,
    ImmutableSortedSet<E> {

    /**
     * Returns a {@link ImmutableSortedSet} which is immutable including copying elements if necessary.
     */
    static <E> ImmutableSortedSet<E> with(final SortedSet<E> sortedSet) {
        ImmutableSortedSet<E> immutableSortedSet;

        if (sortedSet instanceof ImmutableSortedSet) {
            immutableSortedSet = (ImmutableSortedSet<E>) sortedSet;
        } else {
            final TreeSet<E> treeSet = new TreeSet<>(sortedSet.comparator());
            treeSet.addAll(sortedSet);
            immutableSortedSet = new ImmutableSortedSetImpl<>(treeSet);
        }
        return immutableSortedSet;
    }

    /**
     * Package private to limit sub classing.
     */
    ImmutableSortedSetImpl(final SortedSet<E> sortedSet) {
        super();
        this.sortedSet = sortedSet;
    }

    @Override
    public boolean contains(final Object other) {
        return this.sortedSet.contains(other);
    }

    @Override
    public Iterator<E> iterator() {
        return Iterators.readOnly(
            this.sortedSet.iterator()
        );
    }

    @Override
    public int size() {
        return this.sortedSet.size();
    }

    @Override
    public String toString() {
        return this.sortedSet.toString();
    }

    // SortedSet........................................................................................................

    @Override
    public Comparator<? super E> comparator() {
        return this.sortedSet.comparator();
    }

    @Override
    public SortedSet<E> subSet(final E fromElement,
                               final E toElement) {
        return with(
            this.sortedSet.subSet(
                fromElement,
                toElement
            )
        );
    }

    @Override
    public SortedSet<E> headSet(final E toElement) {
        return with(
            this.sortedSet.headSet(toElement)
        );
    }

    @Override
    public SortedSet<E> tailSet(final E fromElement) {
        return with(
            this.sortedSet.tailSet(fromElement)
        );
    }

    @Override
    public E first() {
        return this.sortedSet.first();
    }

    @Override
    public E last() {
        return this.sortedSet.last();
    }

    private final SortedSet<E> sortedSet;

    // ImmutableSortedSet...............................................................................................

    @Override
    public SortedSet<E> toSet() {
        final SortedSet<E> sortedSet = this.sortedSet;

        final TreeSet<E> treeSet = new TreeSet<>(sortedSet.comparator());
        treeSet.addAll(sortedSet);
        return treeSet;
    }

    @Override
    public void elementCheck(final E element) {
        // NOP
    }

    @Override
    public ImmutableSortedSet<E> setElements(final SortedSet<E> elements) {
        final ImmutableSortedSet<E> copy = with(elements);

        return this.equals(copy) ?
            this :
            copy;
    }
}
