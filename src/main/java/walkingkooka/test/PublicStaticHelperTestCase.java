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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;
import walkingkooka.type.PublicStaticHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base class for testing a {@link PublicStaticHelper} with tests mostly concerned with visibility
 * of members.
 */
abstract public class PublicStaticHelperTestCase<H extends PublicStaticHelper>
        extends ClassTestCase<H> {

    /**
     * Protected for sub classing.
     */
    protected PublicStaticHelperTestCase() {
        super();
    }

    @Test
    final public void testClassIsFinal() {
        final Class<H> type = this.type();
        assertTrue(Modifier.isFinal(type.getModifiers()), () -> type.getName() + " is NOT final");
    }

    @Test
    final public void testOnlyConstructorIsPrivate() throws Exception {
        final Class<H> type = this.type();
        final Constructor<H> constructor = type.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), () -> type.getName() + " is NOT private");
    }

    @Test
    public final void testDefaultConstructorThrowsUnsupportedOperationException() throws Exception {
        final Class<H> type = this.type();
        final Constructor<H> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);

        final InvocationTargetException cause = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
        final Throwable target = cause.getTargetException();
        assertTrue(target instanceof UnsupportedOperationException, "Expected UnsupportedOperationException but got " + target);
    }

    @Test
    final public void testAllMethodsAreStatic() {
        this.methodAndCheck(m -> false == MethodAttributes.STATIC.is(m),
                "All methods must be static");
    }

    /**
     * Verifies that all static methods are public, package private or private.
     */
    @Test
    final public void testCheckVisibilityOfAllStaticMethods() {
        this.methodAndCheck(m -> MemberVisibility.PROTECTED.is(m),
                "All methods must be public or package private");
    }

    /**
     * Verifies that the parameter and return types of all public methods are also public.
     */
    @Test
    final public void testPublicStaticMethodsParameterAndReturnTypesArePublic() {
        this.methodAndCheck(this::publicReturnTypeAndParameters,
                "All method parameter and return type must be public");
    }

    private boolean publicReturnTypeAndParameters(final Method method) {
        return !MemberVisibility.PUBLIC.is(method.getReturnType()) ||
                Arrays.stream(method.getParameterTypes())
                        .anyMatch(t -> !MemberVisibility.PUBLIC.is(t));
    }

    private void methodAndCheck(final Predicate<Method> predicate, final String message) {
        assertEquals(Lists.empty(),
                Arrays.stream(this.type().getDeclaredMethods())
                        .filter(m -> !m.getName().startsWith("$")) // filter out any special methods like Jacoco's
                        .filter(predicate)
                        .collect(Collectors.toList()),
                message);
    }

    /**
     * Methods should return true if the method contains package private types. This method is only
     * called when public types are encountered while checking methods by {@link
     * #testPublicStaticMethodsParameterAndReturnTypesArePublic()}.
     */
    abstract protected boolean canHavePublicTypes(Method method);

    /**
     * Verifies that at least of public static field or method is present.
     */
    @Test
    final public void testContainsPublicStaticMethodsOrField() {
        final Class<H> type = this.type();

        if (Modifier.isPublic(type.getModifiers())) {
            int count = 0;
            for (final Method method : type.getDeclaredMethods()) {
                final int modifiers = method.getModifiers();
                if (false == Modifier.isStatic(modifiers)) {
                    continue;
                }
                if (Modifier.isPublic(modifiers)) {
                    count++;
                }
            }
            for (final Field method : type.getDeclaredFields()) {
                final int modifiers = method.getModifiers();
                if (false == Modifier.isStatic(modifiers)) {
                    continue;
                }
                if (Modifier.isPublic(modifiers)) {
                    count++;
                }
            }

            assertNotEquals(0, count, "No public static fields/methods found");
        }
    }

    @Test
    @Disabled
    public void testToString() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
