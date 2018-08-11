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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Base class for testing a {@link HashCodeEqualsDefined} with mostly parameter checking tests.
 */
abstract public class HashCodeEqualsDefinedEqualityTestCase<T extends HashCodeEqualsDefined>
        extends TestCase {

    protected HashCodeEqualsDefinedEqualityTestCase() {
        super();
    }

    // these tests verify that hashcode & equals never overrides another non abstract hashcode (except for object)

    @Test
    public void testHashCodeOnlyOverridesAbstractOrObject() {
        this.checkOverridesAbstractMethod("hashCode", false);
    }

    @Test
    public void testEqualsOnlyOverridesAbstractOrObject() {
        this.checkOverridesAbstractMethod("equals", true);
    }

    /**
     * Finds all methods with the base and for each found, searches the super type for an abstract
     * method of the same name and no parameters.
     */
    private void checkOverridesAbstractMethod(final String base, final boolean useSuperType) {
        final Class<?> type = this.objectType();
        for (final Method method : type.getDeclaredMethods()) {
            final String name = method.getName();
            if (name.startsWith(base)) {
                final Class<?> superType = type.getSuperclass();
                if (Object.class.equals(superType)) {
                    continue;
                }
                try {
                    this.extractNumeral(name, base);
                } catch (final NumberFormatException ignore) {
                    // ignore hashCode/equals suffixes that are not numbers.
                    continue;
                }

                // only trying $base<literal> methods
                try {
                    final Method overridden = superType.getDeclaredMethod(name, //
                            useSuperType ? new Class<?>[]{superType} : new Class[0]);
                    if (!HashCodeEqualsDefinedEqualityTestCase.isAbstract(overridden)) {
                        Assert.fail("Overridding non abstract method in super type=" + method);
                    }
                } catch (final NoSuchMethodException missing) {
                    Assert.fail("Unable to find abstract method in super type=" + method);
                }
            }
        }
    }

    @Test
    public void testHashCodeAndEqualsInPairs() {
        final Map<Integer, String> hashCode = Maps.sorted();
        final Map<Integer, String> equals = Maps.sorted();
        final Class<?> type = this.objectType();

        this.findAllHashCodeAndEquals(type, hashCode, equals);
        if (hashCode.entrySet().size() != equals.entrySet().size()) {
            assertEquals("Unmatched equals and hashCode methods found", hashCode, equals);
        }
    }

    private void findAllHashCodeAndEquals(final Class<?> type, final Map<Integer, String> hashCode,
                                          final Map<Integer, String> equals) {
        for (final Method method : type.getDeclaredMethods()) {
            if (HashCodeEqualsDefinedEqualityTestCase.isAbstract(method)
                    || HashCodeEqualsDefinedEqualityTestCase.isPrivate(method)) {
                continue;
            }

            final String name = method.getName();
            try {
                if (HashCodeEqualsDefinedEqualityTestCase.startsWithNotEquals(name, "hashCode")) {
                    hashCode.put(this.extractNumeral(name, "hashCode"), name);
                }
                if (HashCodeEqualsDefinedEqualityTestCase.startsWithNotEquals(name, "equals")) {
                    equals.put(this.extractNumeral(name, "equals"), name);
                }
            } catch (final NumberFormatException ignore) {
                // ignore hashCodeXXX where XXX is not a number.
            }
        }

        final Class<?> superType = type.getSuperclass();
        if (!Object.class.equals(superType)) {
            this.findAllHashCodeAndEquals(superType, hashCode, equals);
        }
    }

    private static boolean startsWithNotEquals(final String s, final String startsWith) {
        return !s.equals(startsWith) && s.startsWith(startsWith);
    }

    /**
     * Extracts the numeric literal after the base name from the provided method name. Eg equals123
     * returns 123.
     */
    private int extractNumeral(final String methodName, final String base) {
        return Integer.parseInt(methodName.substring(base.length()));
    }

    /**
     * Only allow equals(Object) and equals(T)
     */
    @Test
    public void testEqualsObjectAndPossiblyType() {
        final Set<String> parameterTypes = Sets.sorted();

        final Class<?> type = this.objectType();
        for (final Method method : type.getMethods()) {
            if (HashCodeEqualsDefinedEqualityTestCase.isAbstract(method)
                    && !HashCodeEqualsDefinedEqualityTestCase.isPublic(method)) {
                continue;
            }
            if (method.getName().equals("equals")) {
                parameterTypes.add(method.getParameterTypes()[0].getName());
            }
        }

        switch (parameterTypes.size()) {
            case 1:
                assertEquals("equal methods", Sets.of(Object.class.getName()), parameterTypes);
                break;
            default:
                assertEquals("equals method",
                        Sets.of(Object.class.getName(), type.getName()),
                        parameterTypes);
                break;
        }
    }

    private static boolean isAbstract(final Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }

    private static boolean isPrivate(final Method method) {
        return Modifier.isPrivate(method.getModifiers());
    }

    private static boolean isPublic(final Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    final Class<?> objectType() {
        return this.createObject().getClass();
    }

    @Test
    public void testHashCode() {
        final Object object = this.createObject();
        assertEquals("repeated calls to hashCode should return same value",
                object.hashCode(),
                object.hashCode());
    }

    @Test final public void testNullIsFalse() {
        final T object = this.createObject();
        if (object.equals(null)) {
            assertEquals(null, object, null);
        }
    }

    @Test final public void testDifferentType() {
        HashCodeEqualsDefinedEqualityTestCase.checkNotEquals(this.createObject(), new Object());
    }

    @Test final public void testSelf() {
        final Object object = this.createObject();
        HashCodeEqualsDefinedEqualityTestCase.checkEqualsAndHashCode(object, object);
    }

    @Test
    public void testEquals() {
        final Object object1 = this.createObject();
        final Object object2 = this.createObject();
        HashCodeEqualsDefinedEqualityTestCase.checkEqualsAndHashCode(object1, object2);
    }

    abstract protected T createObject();

    protected void checkEquals(final Object actual) {
        HashCodeEqualsDefinedEqualityTestCase.checkEquals(this.createObject(), actual);
    }

    static public void checkEquals(final Object expected, final Object actual) {
        Assert.assertNotNull("Expected is null", expected);
        Assert.assertNotNull("Actual is null", actual);

        if (false == expected.equals(actual)) {
            assertEquals(null, expected, actual);
        }
        if (false == actual.equals(expected)) {
            assertEquals(null, expected, actual);
        }
    }

    protected void checkEqualsAndHashCode(final Object actual) {
        HashCodeEqualsDefinedEqualityTestCase.checkEqualsAndHashCode(this.createObject(), actual);
    }

    static public void checkEqualsAndHashCode(final Object expected, final Object actual) {
        HashCodeEqualsDefinedEqualityTestCase.checkEquals(expected, actual);

        final int expectedHashCode = expected.hashCode();
        final int actualHashCode = expected.hashCode();

        if (expectedHashCode != actualHashCode) {
            assertEquals("Hashcode not equal",
                    expectedHashCode + "=" + expected,
                    actualHashCode + "=" + actual);
        }
    }

    protected void checkNotEquals(final Object actual) {
        HashCodeEqualsDefinedEqualityTestCase.checkNotEquals(this.createObject(), actual);
    }

    static public void checkNotEquals(final Object expected, final Object actual) {
        Assert.assertNotNull("Expected is null", expected);
        Assert.assertNotNull("Actual is null", expected);

        if (expected.equals(actual)) {
            Assert.fail(expected + " should not be equal to " + actual);
        }
        if (actual.equals(expected)) {
            Assert.fail(actual + " should not be equal to " + expected);
        }
    }
}
