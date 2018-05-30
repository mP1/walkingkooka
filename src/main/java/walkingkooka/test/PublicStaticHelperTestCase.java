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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * Base class for testing a {@link PublicStaticHelper} with tests mostly concerned with visibility
 * of members.
 */
abstract public class PublicStaticHelperTestCase<H extends PublicStaticHelper>
        extends StaticHelperTestCase<H> {

    /**
     * Protected for sub classing.
     */
    protected PublicStaticHelperTestCase() {
        super();
    }

    @Test final public void testClassIsPublic() {
        final Class<?> type = this.type();
        if (false == Modifier.isPublic(type.getModifiers())) {
            Assert.fail(type.getName() + " is not public=" + type);
        }
    }

    /**
     * Verifies that all static methods are public, package private or private.
     */
    @Test final public void testCheckVisibilityOfAllStaticMethods() {
        final Class<H> type = this.getTypeAndCheck();
        final Set<String> invalid = Sets.sorted();

        for (final Method method : type.getDeclaredMethods()) {
            final int modifier = method.getModifiers();
            if (Modifier.isPublic(modifier)) {
                continue;
            }
            if (Modifier.isPrivate(modifier)) {
                continue;
            }
            // skip package private classes
            // if protected method
            if (Modifier.isProtected(modifier)) {
                invalid.add(method.toGenericString());
            }
        }

        if (false == invalid.isEmpty()) {
            Assert.fail("Not all static methods are be public/private=" + invalid);
        }
    }

    /**
     * Verifies that the parameter and return types of all public methods are also public.
     */
    @Test final public void testPublicStaticMethodsParameterAndReturnTypesArePublic() {
        final Class<H> type = this.getTypeAndCheck();

        if (Modifier.isPublic(type.getModifiers())) {
            for (final Method method : type.getDeclaredMethods()) {

                // skip non public methods
                if (false == Modifier.isPublic(method.getModifiers())) {
                    continue;
                }

                String message = null;

                final Class<?> returnType = method.getReturnType();
                if (false == Modifier.isPublic(returnType.getModifiers())) {
                    message = "Public static methods must return a public type=" + method;
                }
                for (final Class<?> parameterType : method.getParameterTypes()) {
                    if (false == Modifier.isPublic(parameterType.getModifiers())) {
                        message =
                                "All parameters types of a public static method must be a public type="
                                        + method;
                    }
                }

                if (null != message) {
                    if (false == this.canHavePublicTypes(method)) {
                        Assert.fail(message);
                    }
                }
            }
        }
    }

    /**
     * Methods should return true if the method contains package private types. This method is only
     * called when public types are encountered while checking methods by {@link
     * #testPublicStaticMethodsParameterAndReturnTypesArePublic()}.
     */
    @Override
    abstract protected boolean canHavePublicTypes(Method method);

    /**
     * Verifies that at least of public static field or method is present.
     */
    @Test final public void testContainsPublicStaticMethodsOrField() {
        final Class<H> type = this.getTypeAndCheck();

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

            if (0 == count) {
                Assert.fail("No public static fields/methods found");
            }
        }
    }
}
