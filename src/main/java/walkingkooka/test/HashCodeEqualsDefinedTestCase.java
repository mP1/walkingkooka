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

package walkingkooka.test;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Throwables;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertNotEquals;

/**
 * Base class for testing a {@link HashCodeEqualsDefined} with mostly parameter checking tests.
 */
abstract public class HashCodeEqualsDefinedTestCase<T extends HashCodeEqualsDefined>
        extends TestCase {

    protected HashCodeEqualsDefinedTestCase() {
        super();
    }

    @Test final public void testIsFinalIfNotAbstract() {
        final Class<?> type = this.type();
        if (false == Modifier.isAbstract(type.getModifiers())) {
            Assert.assertTrue(type.getName() + " is not abstract and not final",
                    Modifier.isFinal(type.getModifiers()));
        }
    }

    public final void testNotPublicConstructor() {
        try {
            final Class<?> type = this.type();
            for (final Constructor<?> constructor : type.getDeclaredConstructors()) {
                Assert.assertSame("Constructor must be public=" + constructor,
                        MemberVisibility.PUBLIC,
                        MemberVisibility.get(constructor));
            }
        } catch (final Exception cause) {
            throw new RuntimeException(Throwables.message("Cannot check if constructors are private",
                    cause), cause);
        }
    }

    public final void testCheckHashCodeOverridden() throws Exception {
        final Class<?> type = this.type();
        final Method hashCode = type.getMethod("hashCode");
        if (hashCode.getDeclaringClass().equals(Object.class)) {
            Assert.fail("hashCode() not overridden by " + type.getName());
        }
    }

    public final void testCheckEqualsOverridden() throws Exception {
        final Class<?> type = this.type();
        final Method equals = type.getMethod("equals", Object.class);
        if (equals.getDeclaringClass().equals(Object.class)) {
            Assert.fail("equals(Object) not overridden by " + type.getName());
        }
    }

    public final void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    protected void checkEquals(final T first, final Object second) {
        assertEquals(first, second);
        assertEquals(second, first);
    }

    protected void checkNotEquals(final T first, final Object second) {
        assertNotEquals(first, second);
        assertNotEquals(second, first);
    }

    abstract protected Class<? extends T> type();
}
