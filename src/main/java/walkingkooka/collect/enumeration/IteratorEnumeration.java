package walkingkooka.collect.enumeration;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Enumeration} view of an {@link Iterator}.
 */
final class IteratorEnumeration<E> implements Enumeration<E> {

    static <E> IteratorEnumeration<E> with(final Iterator<E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        return new IteratorEnumeration<>(iterator);
    }

    /**
     * Private constructor use static factory
     */
    private IteratorEnumeration(final Iterator<E> iterator) {
        super();
        this.iterator = iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return this.iterator.hasNext();
    }

    @Override
    public E nextElement() {
        return this.iterator.next();
    }

    /**
     * The wrapped adapted {@link Iterator}.
     */
    private final Iterator<E> iterator;

    @Override
    public String toString() {
        return this.iterator.toString();
    }
}
