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
import walkingkooka.collect.set.Sets;
import walkingkooka.type.PublicStaticHelper;
import walkingkooka.type.StaticHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Base class for testing a {@link PublicStaticHelper} with tests mostly concerned with visibility
 * of members.
 */
abstract public class StaticHelperTestCase<H extends StaticHelper> extends TestCase {

    StaticHelperTestCase() {
        super();
    }

    @Test final public void testClassIsFinal() {
        final Class<H> type = this.getTypeAndCheck();
        Assert.assertTrue(type + " is NOT final", Modifier.isFinal(type.getModifiers()));
    }

    @Test final public void testOnlyConstructorIsPrivate() throws Exception {
        final Class<H> type = this.getTypeAndCheck();
        final Constructor<H> constructor = type.getDeclaredConstructor();
        Assert.assertTrue(type + " is NOT private", Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void testDefaultConstructorThrowsUnsupportedOperationException() throws Exception {
        final Class<H> type = this.getTypeAndCheck();
        final Constructor<H> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
            Assert.fail();
        } catch (final InvocationTargetException expected) {
            final Throwable target = expected.getTargetException();
            Assert.assertTrue("Expected UnsupportedOperationException but got " + target,
                    target instanceof UnsupportedOperationException);
        }
    }

    @Test final public void testAllMethodsAreStatic() {
        final Class<H> type = this.getTypeAndCheck();
        for (final Method method : type.getDeclaredMethods()) {
            if (false == Modifier.isStatic(method.getModifiers())) {
                Assert.fail("Method is not static=" + method);
            }
        }
    }

    /**
     * Verifies that no instance methods are present.
     */
    @Test final public void testWithoutInstanceMethods() {
        final Set<String> instanceMethods = Sets.sorted();

        for (final Method method : this.getTypeAndCheck().getDeclaredMethods()) {
            if (false == Modifier.isStatic(method.getModifiers())) {
                instanceMethods.add(method.toGenericString());
            }
        }

        if (false == instanceMethods.isEmpty()) {
            Assert.fail("No instance methods should be declared=" + instanceMethods);
        }
    }

    // helpers

    /**
     * Returns the {@link Class} being analysed and tested.
     */
    abstract protected Class<H> type();

    /**
     * Validates the {@link Class} returned by test the this test class.
     */
    final Class<H> getTypeAndCheck() {
        final Class<H> type = this.type();
        final String name = type.getName();
        final String expected = name + "Test";
        final String actual = this.getClass().getName();
        if (false == expected.equals(actual)) {
            final String expected2 = name + "ClassTest";
            if (false == expected2.equals(actual)) {
                assertEquals("Test name is invalid for type " + type, expected, actual);
            }
        }
        return type;
    }

    /**
     * Used to verify if a {@link Method} may be package private. If this test fails the test
     * fails.
     */
    abstract boolean canHavePublicTypes(Method method);
}
