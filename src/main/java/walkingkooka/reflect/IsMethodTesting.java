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
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Scans a class for one or more instance is methods. Only methods that match the class less a prefix and suffix should
 * return true.
 */
public interface IsMethodTesting<T> extends Testing {

    @Test
    default void testIsMethods() throws Exception {
        final T object = this.createIsMethodObject();
        final String name = object.getClass().getSimpleName();

        final String prefix = this.isMethodTypeNamePrefix();
        final String suffix = this.isMethodTypeNameSuffix();

        // remove prefix and suffix and create is method name...
        final String isMethodName = "is" + CharSequences.capitalize(
            name.substring(prefix.length(),
                name.length() - suffix.length()));

        final Method isMethod = object.getClass().getMethod(isMethodName);
        isMethod.setAccessible(true);

        this.checkEquals(
            true,
            isMethod.invoke(object),
            () -> "Is method " + isMethod.toGenericString() + " should have returned true for " + object
        );

        final Predicate<String> filter = this.isMethodIgnoreMethodFilter();

        // all other is methods should return false.
        this.checkEquals(
            Lists.empty(),
            Arrays.stream(object.getClass().getMethods())
                .filter(m -> !MethodAttributes.STATIC.is(m)) // filter static methods
                .filter(m -> m.getName().startsWith("is")) // only process
                .filter(m -> !m.getName().equals("isSymbol")) // special case ignore isSymbol
                .filter(m -> !m.getName().equals(isMethodName)) // skip isMethod for object.class
                .filter(m -> !filter.test(m.getName())) // skip special case
                .filter(m -> {
                    try {
                        m.setAccessible(true);
                        return Boolean.TRUE.equals(m.invoke(object));
                    } catch (final Exception cause) {
                        cause.printStackTrace();
                        throw new Error(cause);
                    }
                })
                .map(Method::toGenericString)
                .collect(Collectors.toList()),
            "IsMethods that should have returned false but returned true."
        );
    }

    /**
     * Factory that creates the instance that will have its is methods tested.
     */
    T createIsMethodObject();

    /**
     * Common prefix that all classes share in their naming and should be removed prior to calculating is method name.
     */
    String isMethodTypeNamePrefix();

    /**
     * Common suffix that all classes share in their naming and should be removed prior to calculating is method name.
     */
    String isMethodTypeNameSuffix();

    /**
     * A {@link Predicate} that should match any is methods that should not be tested.
     */
    Predicate<String> isMethodIgnoreMethodFilter();
}
