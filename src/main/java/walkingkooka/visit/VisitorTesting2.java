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

package walkingkooka.visit;

import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.MethodAttributes;
import walkingkooka.test.Testing;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class VisitorTesting2 implements Testing {

    static void instanceCheck(final String methodName, final Class<? extends Visitor<?>> type) {
        allMethodsAndCheck(methodName,
            type,
            (m) -> !MethodAttributes.STATIC.is(m));
    }

    static void protectedMethodCheck(final String methodName, final Class<? extends Visitor<?>> type) {
        allMethodsAndCheck(methodName,
            type,
            (m) -> JavaVisibility.PROTECTED == JavaVisibility.of(m));
    }

    static void singleParameterCheck(final String methodName, final Class<? extends Visitor<?>> type) {
        allMethodsAndCheck(methodName,
            type,
            (m) -> m.getParameterTypes().length == 1);
    }

    static void methodParameterTypesPublicCheck(final String methodName, final Class<? extends Visitor<?>> type) {
        allMethodsAndCheck(methodName,
            type,
            VisitorTesting2::allParametersTypesPublic);
    }

    private static boolean allParametersTypesPublic(final Method method) {
        return Arrays.stream(method.getParameterTypes())
            .filter(t -> JavaVisibility.PUBLIC == JavaVisibility.of(t))
            .count() == method.getParameterTypes().length;
    }

    static void methodReturnTypeVoidCheck(final String methodName,
                                          final Class<? extends Visitor<?>> type,
                                          final Class<?> returnType) {
        allMethodsAndCheck(methodName,
            type,
            (m) -> returnType == m.getReturnType());
    }

    private static void allMethodsAndCheck(final String methodName,
                                           final Class<? extends Visitor<?>> type,
                                           final Predicate<Method> predicate) {
        final List<Method> failed = Arrays.stream(type.getDeclaredMethods())
            .filter(m -> m.getName().equals(methodName))
            .filter(predicate.negate())
            .collect(Collectors.toList());

        assertEquals(
            Lists.empty(),
            failed,
            () -> "Several methods in " + type.getName() + " failed"
        );
    }

    private VisitorTesting2() {
        throw new UnsupportedOperationException();
    }
}
