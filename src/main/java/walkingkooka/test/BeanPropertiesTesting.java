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
 *
 */

package walkingkooka.test;

import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Contains non public methods to assist the testing of a type with for static factory methods.
 */
public final class BeanPropertiesTesting {

    /**
     * Checks that all properties do not return null.
     */
    public static void allPropertiesNeverReturnNullCheck(final Object object,
                                                         final Predicate<Method> skip) throws Exception {
        final List<Method> properties = Arrays.stream(object.getClass().getMethods())
                .filter((m) -> propertiesNeverReturnNullCheckFilter(m, skip))
                .collect(Collectors.toList());
        assertNotEquals(0,
                properties.size(),
                "Found zero properties for type=" + object.getClass().getName());
        for (Method method : properties) {
            method.setAccessible(true);
            assertNotNull(method.invoke(object),
                    () -> "null should not have been returned by " + method + " for " + object);
        }
    }

    /**
     * Keep instance methods, that return something, take no parameters, arent a Object member.
     */
    private static boolean propertiesNeverReturnNullCheckFilter(final Method method,
                                                                final Predicate<Method> skip) {
        return !MethodAttributes.STATIC.is(method) &&
                method.getReturnType() != Void.class &&
                method.getParameterTypes().length == 0 &&
                method.getDeclaringClass() != Object.class &&
                !skip.test(method);
    }

    /**
     * Stop creation.
     */
    private BeanPropertiesTesting() {
        throw new UnsupportedOperationException();
    }
}
