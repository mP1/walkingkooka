/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.reflect;

import walkingkooka.test.Testing;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Mixing interface that provides helpers to test bean properties.
 */
public interface BeanPropertiesTesting extends Testing {

    /**
     * Checks that all properties do not return null.
     */
    default void allPropertiesNeverReturnNullCheck(final Object object,
                                                   final Predicate<Method> skip) throws Exception {
        final Predicate<Method> filter = (method) -> !MethodAttributes.STATIC.is(method) &&
            method.getReturnType() != Void.class &&
            method.getParameterTypes().length == 0 &&
            method.getDeclaringClass() != Object.class &&
            !skip.test(method);

        final List<Method> properties = Arrays.stream(object.getClass().getMethods())
            .filter(filter)
            .collect(Collectors.toList());
        this.checkNotEquals(
            0,
            properties.size(),
            () -> "Found zero properties for type=" + object.getClass().getName()
        );
        for (Method method : properties) {
            method.setAccessible(true);
            this.checkNotEquals(
                null,
                method.invoke(object),
                () -> "null should not have been returned by " + method + " for " + object
            );
        }
    }
}
