/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.collect.iterator;

import walkingkooka.build.chain.ChainType;
import walkingkooka.build.chain.Chained;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An {@link Iterator} that chains two {@link Iterator iterators together}. When the first becomes
 * empty the next is called.
 */
final class IteratorChain<E> implements Iterator<E>, Chained<Iterator<E>> {

    /**
     * Creates an {@link IteratorChain} from two {@link Iterators}.
     */
    static <E> Iterator<E> wrap(final Iterator<E> first, final Iterator<E> second) {
        Objects.requireNonNull(first, "first Iterator");
        Objects.requireNonNull(second, "second Iterator");

        return Iterators.<E>builder().add(first).add(second).build();
    }

    /**
     * Wraps without copying the array and is called by {@link IteratorChainFactory}
     */
    static <E> Iterator<E> wrap(final Iterator<E>[] iterators) {
        return new IteratorChain<E>(iterators);
    }

    /**
     * Private constructor use static factory.
     */
    private IteratorChain(final Iterator<E>[] iterators) {
        super();
        this.current = iterators[0];
        this.iterators = iterators;
        this.next = 1;
    }

    /**
     * Tests if another element is available. If the current iterator is empty the next is checked
     * etc.
     */
    @Override
    public boolean hasNext() {
        boolean hasNext = false;
        Iterator<E> current = this.current;

        for (; ; ) {
            if (null == current) {
                break;
            }
            hasNext = current.hasNext();
            if (hasNext) {
                break;
            }
            current = this.loadNext();
        }

        return hasNext;
    }

    /**
     * Attempts to fetch an element from the current element or if that is empty from the next until
     * none.
     */
    @Override
    public E next() {
        Iterator<E> current = this.current;
        this.remove = null;

        for (; ; ) {
            if (null == current) {
                throw new NoSuchElementException();
            }
            // current is not empty next!.
            if (current.hasNext()) {
                break;
            }
            current = this.loadNext();
        }
        final E next = current.next();
        this.remove = current; // save this for remove(
        return next;
    }

    /**
     * The current {@link Iterator}. This will become null when exhausted.
     */
    private Iterator<E> current;

    /**
     * All the iterators
     */
    private final Iterator<E>[] iterators;

    /**
     * An index that points to the next {@link Iterator}.
     */
    private int next;

    /**
     * Advances to the next {@link Iterator} if possible setting {@link #current}.
     */
    private Iterator<E> loadNext() {
        final Iterator<E>[] iterators = this.iterators;
        final int next = this.next;
        Iterator<E> current = null;
        if (next < iterators.length) {
            this.next = next + 1;
            current = iterators[next];
            this.remove = null;
        }
        this.current = current;
        return current;
    }

    @Override
    public void remove() {
        final Iterator<E> remove = this.remove;
        if (null == remove) {
            throw new UnsupportedOperationException("remove without next");
        }
        this.remove = null;
        remove.remove();
    }

    /**
     * The {@link Iterator} that is called when removed. THis is set after each next but cleared by
     * remove.
     */
    private Iterator<E> remove;

    // Chained

    /**
     * Returns a clone of all {@link Iterator iterators}.
     */
    @Override
    public Iterator<E>[] chained() {
        return this.iterators.clone();
    }

    /**
     * Returns {@link ChainType#ALL} because all {@link Iterator iterators} are read.
     */
    @Override
    public ChainType chainType() {
        return IteratorChain.TYPE;
    }

    final static ChainType TYPE = ChainType.ALL;

    /**
     * Dumps the current {@link Iterator} with trailing ellipses if it is not the last.
     */
    @Override
    public String toString() {
        final Iterator<E> current = this.current;
        return null == current ?
                "" :
                this.next == this.iterators.length ? current.toString() : current + "...";
    }
}
