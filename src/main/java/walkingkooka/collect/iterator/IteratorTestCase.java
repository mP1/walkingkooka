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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

/**
 * Base class for testing a {@link Iterator} with mostly parameter checking tests.
 */
abstract public class IteratorTestCase<I extends Iterator<T>, T>
        extends ClassTestCase<I> {

    protected IteratorTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Iterator.class);
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected I createIterator();

    protected void checkHasNextFalse(final Iterator<?> iterator) {
        this.checkHasNextFalse("iterator.hasNext should return false", iterator);
    }

    protected void checkHasNextFalse(final String message, final Iterator<?> iterator) {
        Assert.assertNotNull("iterator", iterator);
        if (iterator.hasNext()) {
            Assert.fail(message + "=" + iterator);
        }
    }

    protected void checkHasNextTrue(final Iterator<T> iterator) {
        this.checkHasNextTrue("iterator.hasNext should return true", iterator);
    }

    protected void checkHasNextTrue(final String message, final Iterator<T> iterator) {
        Assert.assertNotNull("iterator", iterator);
        if (false == iterator.hasNext()) {
            Assert.fail(message + "=" + iterator);
        }
    }

    protected void checkNextFails(final Iterator<?> iterator) {
        this.checkNextFails("iterator.next must throw NoSuchElementException", iterator);
    }

    protected void checkNextFails(final String message, final Iterator<?> iterator) {
        try {
            final Object next = iterator.next();
            Assert.fail(message + "=" + next);
        } catch (final NoSuchElementException ignored) {
        }
    }

    protected void checkRemoveWithoutNextFails(final Iterator<?> iterator) {
        this.checkRemoveWithoutNextFails("iterator.remove must throw IllegalStateException",
                iterator);
    }

    protected void checkRemoveWithoutNextFails(final String message, final Iterator<?> iterator) {
        try {
            iterator.remove();
            Assert.fail(message);
        } catch (final IllegalStateException ignored) {
        }
    }

    protected void checkRemoveFails() {
        this.checkRemoveFails(this.createIterator());
    }

    protected void checkRemoveFails(final Iterator<?> iterator) {
        this.checkRemoveFails("iterator.remove must throw UnsupportedOperationException", iterator);
    }

    protected void checkRemoveFails(final String message, final Iterator<?> iterator) {
        try {
            iterator.remove();
            Assert.fail(message);
        } catch (final UnsupportedOperationException ignored) {
        }
    }

    protected void checkRemoveUnsupported(final Iterator<?> iterator) {
        this.checkRemoveUnsupported("iterator.remove must throw UnsupportedOperationException",
                iterator);
    }

    protected void checkRemoveUnsupported(final String message, final Iterator<?> iterator) {
        try {
            iterator.remove();
            Assert.fail(message);
        } catch (final UnsupportedOperationException ignored) {
        }
    }

    @SafeVarargs
    protected final void iterateUsingHasNextAndCheck(final T... expected) {
        this.iterateUsingHasNextAndCheck(this.createIterator(), expected);
    }

    @SafeVarargs
    protected final <U> void iterateUsingHasNextAndCheck(final Iterator<U> iterator, final U... expected) {
        Assert.assertNotNull("iterator", iterator);

        int i = 0;
        final List<U> consumed = Lists.array();
        while (iterator.hasNext()) {
            final U next = iterator.next();
            assertEquals("element " + i, expected[i], next);
            consumed.add(next);
            i++;
        }
        if (i != expected.length) {
            assertEquals(null, this.toString(consumed), this.toString(expected));
        }
        this.checkNextFails(iterator);
    }

    @SafeVarargs
    protected final void iterateAndCheck(final T... expected) {
        this.iterateAndCheck(this.createIterator(), expected);
    }

    @SafeVarargs
    protected final <U> void iterateAndCheck(final Iterator<U> iterator, final U... expected) {
        Assert.assertNotNull("iterator", iterator);

        int i = 0;
        final List<U> consumed = Lists.array();
        final int expectedCount = expected.length;
        while (i < expectedCount) {
            final U next = iterator.next();
            assertEquals("element " + i, expected[i], next);
            consumed.add(next);
            i++;
        }
        this.checkNextFails(iterator);
    }

    private String toString(final List<?> list) {
        return list.size() + "=" + list;
    }

    private String toString(final Object... list) {
        return this.toString(Arrays.asList(list));
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
