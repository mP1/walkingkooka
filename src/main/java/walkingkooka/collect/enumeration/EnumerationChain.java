package walkingkooka.collect.enumeration;

import walkingkooka.collect.list.Lists;

import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An {@link Enumeration} that chains two {@link Enumeration enumerations together}. When the first becomes empty the next is called.
 */
final class EnumerationChain<E> implements Enumeration<E> {

    /**
     * Wraps the given enumerations.
     */
    static <E> EnumerationChain<E> with(final List<Enumeration<E>> enumerations) {
        Objects.requireNonNull(enumerations, "enumerations");

        return new EnumerationChain<>(
            Lists.immutable(
                enumerations
            )
        );
    }

    /**
     * Private constructor use static factory.
     */
    private EnumerationChain(final List<Enumeration<E>> enumerations) {
        super();
        this.current = enumerations.get(0);
        this.enumerations = enumerations;
        this.next = 1;
    }

    /**
     * Tests if another element is available. If the current enumeration is empty the next is checked etc.
     */
    @Override
    public boolean hasMoreElements() {
        boolean hasMoreElements = false;
        Enumeration<E> current = this.current;

        for (; ; ) {
            if (null == current) {
                break;
            }
            hasMoreElements = current.hasMoreElements();
            if (hasMoreElements) {
                break;
            }
            current = this.loadNext();
        }

        return hasMoreElements;
    }

    /**
     * Attempts to fetch an element from the current element or if that is empty from the next until none.
     */
    @Override
    public E nextElement() {
        Enumeration<E> current = this.current;
        for (; ; ) {
            if (null == current) {
                throw new NoSuchElementException();
            }
            // current is not empty next!.
            if (current.hasMoreElements()) {
                break;
            }
            current = this.loadNext();
        }
        return current.nextElement();
    }

    /**
     * The current {@link Enumeration}. This will become null when exhausted.
     */
    private Enumeration<E> current;

    /**
     * All the enumerations
     */
    private final List<Enumeration<E>> enumerations;

    /**
     * An index that points to the next {@link Enumeration}.
     */
    private int next;

    /**
     * Advances to the next {@link Enumeration} if possible setting {@link #current}.
     */
    private Enumeration<E> loadNext() {
        final List<Enumeration<E>> enumerations = this.enumerations;

        final int next = this.next;
        Enumeration<E> current = null;
        if (next < enumerations.size()) {
            this.next = next + 1;
            current = enumerations.get(next);
        }
        this.current = current;
        return current;
    }

    /**
     * Dumps the current {@link Enumeration} with trailing ellipses if it is not the last.
     */
    @Override
    public String toString() {
        final Enumeration<E> current = this.current;
        return null == current ?
            "" :
            this.next == this.enumerations.size() ?
                current.toString() :
                current + "...";
    }
}
