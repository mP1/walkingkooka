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

package walkingkooka.text.cursor.parser;

import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ParserTokenTesting2 {

    /**
     * The base is typically the base sub class of the type being tested, and holds the public static factory method
     * usually named after the type being tested.
     */
    static void publicStaticFactoryCheck(final Class<?> base,
                                         final String prefix,
                                         final Class<?> suffixType,
                                         final Class<?> type) {
        final String suffix = suffixType.getSimpleName();

        final String name = type.getSimpleName();
        final String without = Character.toLowerCase(name.charAt(prefix.length())) +
                name.substring(prefix.length() + 1, name.length() - suffix.length());

        final String factoryMethodName = factoryMethodNameSpecialFixup(without, suffix);

        final List<Method> publicStaticMethods = Arrays.stream(base.getMethods())
                .filter(m -> MethodAttributes.STATIC.is(m) && MemberVisibility.PUBLIC.is(m))
                .collect(Collectors.toList());

        final List<Method> factoryMethods = publicStaticMethods.stream()
                .filter(m -> m.getName().equals(factoryMethodName) &&
                        m.getReturnType().equals(type))
                .collect(Collectors.toList());

        final String publicStaticMethodsToString = publicStaticMethods.stream()
                .map(m -> m.toGenericString())
                .collect(Collectors.joining(LineEnding.SYSTEM.toString()));
        assertEquals(1,
                factoryMethods.size(),
                () -> "Expected only a single factory method called " + CharSequences.quote(factoryMethodName) +
                        " for " + type + " on " + base.getName() + " but got " + factoryMethods + "\n" + publicStaticMethodsToString);
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

    private ParserTokenTesting2() {
        throw new UnsupportedOperationException();
    }
}
