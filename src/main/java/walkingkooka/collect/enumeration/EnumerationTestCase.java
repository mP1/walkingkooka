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

package walkingkooka.collect.enumeration;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

abstract public class EnumerationTestCase<E extends Enumeration<T>, T>
        extends ClassTestCase<E> {

    protected EnumerationTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Enumeration.class);
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected E createEnumeration();

    protected void checkDoesntHasMoreElements(final Enumeration<?> enumeration) {
        this.checkDoesntHasMoreElements("enumeration.hasMoreElements should return false",
                enumeration);
    }

    protected void checkDoesntHasMoreElements(final String message,
                                              final Enumeration<?> enumeration) {
        Objects.requireNonNull(enumeration, "enumeration");
        if (enumeration.hasMoreElements()) {
            fail(message + "=" + enumeration);
        }
    }

    protected void checkHasMoreElements(final Enumeration<T> enumeration) {
        this.checkHasMoreElements("enumeration.hasMoreElements should return true", enumeration);
    }

    protected void checkHasMoreElements(final String message, final Enumeration<T> enumeration) {
        Objects.requireNonNull(enumeration, "enumeration");
        if (false == enumeration.hasMoreElements()) {
            fail(message + "=" + enumeration);
        }
    }

    protected void checkNextElementFails(final Enumeration<?> enumeration) {
        this.checkNextElementFails("enumeration.next must throw NoSuchElementException",
                enumeration);
    }

    protected void checkNextElementFails(final String message, final Enumeration<?> enumeration) {
        try {
            final Object next = enumeration.nextElement();
            fail(message + "=" + next);
        } catch (final NoSuchElementException ignored) {
        }
    }

    @SafeVarargs
    protected final void enumerateUsingHasMoreElementAndCheck(final T... expected) {
        this.enumerateUsingHasMoreElementsAndCheck(this.createEnumeration(), expected);
    }

    @SafeVarargs
    protected final <U> void enumerateUsingHasMoreElementsAndCheck(final Enumeration<U> enumeration,
                                                                   final U... expected) {
        Objects.requireNonNull(enumeration, "enumeration");

        int i = 0;
        final List<U> consumed = Lists.array();
        while (enumeration.hasMoreElements()) {
            final U next = enumeration.nextElement();
            assertEquals(expected[i], next, "element " + i);
            consumed.add(next);
            i++;
        }
        if (i != expected.length) {
            assertEquals(null, this.toString(consumed), this.toString(expected));
        }
        this.checkNextElementFails(enumeration);
    }

    @SafeVarargs
    protected final void enumerateAndCheck(final T... expected) {
        this.enumerateAndCheck(this.createEnumeration(), expected);
    }

    @SafeVarargs
    protected final <U> void enumerateAndCheck(final Enumeration<U> enumeration, final U... expected) {
        Objects.requireNonNull(enumeration, "enumeration");

        int i = 0;
        final List<U> consumed = Lists.array();
        final int expectedCount = expected.length;
        while (i < expectedCount) {
            final U next = enumeration.nextElement();
            assertEquals(expected[i], next, "element " + i);
            consumed.add(next);
            i++;
        }
        this.checkNextElementFails(enumeration);
    }

    private String toString(final List<?> list) {
        return list.size() + "=" + list;
    }

    private String toString(final Object... list) {
        return this.toString(Arrays.asList(list));
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
