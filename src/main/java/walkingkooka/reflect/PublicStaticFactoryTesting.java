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

import walkingkooka.collect.set.SortedSets;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains non public methods to assist the testing of a type with for static factory methods.
 */
public final class PublicStaticFactoryTesting {

    /**
     * The base is typically the base sub class of the type being tested, and holds the public static factory method
     * usually named after the type being tested.
     */
    public static void checkFactoryMethods(final Class<?> base,
                                           final String prefix,
                                           final String suffix,
                                           final Class<?> type) {
        final String name = type.getSimpleName();
        final String without = Character.toLowerCase(name.charAt(prefix.length())) +
            name.substring(prefix.length() + 1, name.length() - suffix.length());

        final String factoryMethodName = factoryMethodNameSpecialFixup(without, suffix);

        final Comparator<Method> methodSorter = (l, r) -> {
            int result = l.getName().compareTo(r.getName());
            if (0 != result) {
                result = l.toGenericString().compareTo(r.toGenericString()); // good enuff even if return type is sorted before params.
            }
            return result;
        };

        final Set<Method> publicStaticMethods = Arrays.stream(base.getMethods())
            .filter(m -> MethodAttributes.STATIC.is(m) && JavaVisibility.PUBLIC == JavaVisibility.of(m))
            .collect(Collectors.toCollection(() -> SortedSets.tree(methodSorter)));

        final Set<Method> factoryMethods = publicStaticMethods.stream()
            .filter(m -> m.getName().equals(factoryMethodName) &&
                m.getReturnType().equals(type))
            .collect(Collectors.toCollection(() -> SortedSets.tree(methodSorter)));

        final String publicStaticMethodsToString = publicStaticMethods.stream()
            .map(Method::toGenericString)
            .collect(Collectors.joining(LineEnding.SYSTEM.toString()));

        assertEquals(
            1,
            factoryMethods.size(),
            () -> "Expected only a single factory method called " + CharSequences.quote(factoryMethodName) +
                " for " + type + " on " + base.getName() + " but got " + factoryMethods + "\n" + publicStaticMethodsToString
        );
    }

    private static String factoryMethodNameSpecialFixup(final String name, final String suffix) {
        String factoryMethodName = name;
        for (String possible : FACTORY_METHOD_NAME_SPECIALS) {
            if (name.equals(possible)) {
                factoryMethodName = name + suffix;
                break;
            }
        }
        return factoryMethodName;
    }

    private final static String[] FACTORY_METHOD_NAME_SPECIALS = new String[]{"boolean", "byte", "double", "equals", "int", "long", "null", "short"};

    /**
     * Stop creation.
     */
    private PublicStaticFactoryTesting() {
        throw new UnsupportedOperationException();
    }
}
