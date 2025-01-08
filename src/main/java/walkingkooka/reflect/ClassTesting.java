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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.Fake;
import walkingkooka.test.TestSuiteNameTesting;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Mixin that contains a variety of helpers that test class structure related items.
 */
public interface ClassTesting<T> extends TestSuiteNameTesting<T> {

    @Test
    default void testClassVisibility() {
        final Class<?> type = this.type();
        final JavaVisibility visibility = Fake.class.isAssignableFrom(type) ?
            JavaVisibility.PUBLIC :
            this.typeVisibility();

        this.checkEquals(visibility,
            JavaVisibility.of(type),
            () -> type.getName() + " visibility");
    }

    @Test
    default void testAllMethodsVisibility() {
        this.allMethodsVisibilityCheck();
    }

    default void allMethodsVisibilityCheck(final String... ignoreMethodNames) {
        ClassMethodTesting.testAllMethodsVisibility(this.type(), ignoreMethodNames);
    }

    JavaVisibility typeVisibility();

    /**
     * Fail if any public static method includes any of the given invalid types.
     */
    default void publicStaticMethodParametersTypeCheck(final Class<?>... invalidTypes) {
        final Class<T> type = this.type();

        final Predicate<Class<?>> invalidTypeTester = (t) -> Arrays.stream(invalidTypes)
            .filter(i -> i.isAssignableFrom(t))
            .limit(1)
            .count() == 1;

        this.checkEquals(Lists.empty(),
            Arrays.stream(type.getMethods())
                .filter(MethodAttributes.STATIC::is)
                .filter(m -> Arrays.stream(m.getParameterTypes()).filter(invalidTypeTester).limit(1).count() == 1)
                .collect(Collectors.toList()),
            () -> type.getName() + " includes several methods with invalid parameter types " + Arrays.stream(invalidTypes).map(Class::getName).collect(Collectors.joining(",")));
    }
}
