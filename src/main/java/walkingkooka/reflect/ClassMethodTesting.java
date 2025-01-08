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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixin that contains a variety of helpers that test class methods.
 */
final class ClassMethodTesting {

    /**
     * If a method is not overridden it should be package private or private. Method names may include a trailing wildcard
     */
    static void testAllMethodsVisibility(final Class<?> type,
                                         final String... ignoreMethodNames) {
        if (JavaVisibility.PACKAGE_PRIVATE == JavaVisibility.of(type)) {

            final Set<String> ignoreMethodNamesSet = Sets.ordered();
            final List<String> prefixes = Lists.array();

            Arrays.stream(ignoreMethodNames)
                .forEach(methodName -> {
                    if (methodName.endsWith("*")) {
                        prefixes.add(methodName.substring(0, methodName.length() - 1));
                    } else {
                        ignoreMethodNamesSet.add(methodName);
                    }
                });

            final Set<Method> overridable = overridableMethods(type);

            for (final Method method : type.getDeclaredMethods()) {
                final String methodName = method.getName();
                if (ignoreMethodNamesSet.contains(methodName)) {
                    continue;
                }
                if (prefixes.stream()
                    .anyMatch(methodName::startsWith)) {
                    continue;
                }

                if (MethodAttributes.STATIC.is(method)) {
                    if (isMainMethod(method) || isEnumMethod(method)) {
                        continue;
                    }
                    assertEquals(
                        true,
                        JavaVisibility.of(method).isOrLess(JavaVisibility.PACKAGE_PRIVATE),
                        () -> "Static methods should be private/package private " + method.toGenericString()
                    );
                }
                if (overridable.contains(method)) {
                    continue;
                }
                if (MethodAttributes.BRIDGE.is(method) || MethodAttributes.SYNTHETIC.is(method) || isGeneric(method)) {
                    continue;
                }

                assertEquals(
                    true,
                    JavaVisibility.of(method).isOrLess(JavaVisibility.PACKAGE_PRIVATE),
                    () -> "Instance methods not overriding public/protected methods should be private/package private " + method.toGenericString()
                );
            }
        }
    }

    /**
     * Returns true if the given method has the signature of a main method used to start a java program in a JVM.
     */
    private static boolean isMainMethod(final Method method) {
        return method.getName().equals("main") &&
            Arrays.equals(new Class[]{String[].class}, method.getParameterTypes());
    }

    private static boolean isEnumMethod(final Method method) {
        boolean result = false;

        do {
            final String name = method.getName();
            if (method.getDeclaringClass().isEnum()) {
                if (name.equals("valueOf")) {
                    result = true;
                    break;
                }
                if (name.equals("values")) {
                    result = true;
                    break;
                }
            }
        } while (false);
        return result;
    }

    private static boolean isGeneric(final Method method) {
        return false == Arrays.equals(method.getGenericParameterTypes(), method.getParameterTypes());
    }

    private static Set<Method> overridableMethods(final Class<?> type) {
        final Set<Class<?>> alreadyVisited = new HashSet<>();
        final Set<Method> methods = new TreeSet<>((method1, method2) -> {
            int value;

            do {
                value = method1.getName().compareTo(method2.getName());
                if (0 != value) {
                    break;
                }

                final Class<?>[] parameterTypes1 = method1.getParameterTypes();
                final Class<?>[] parameterTypes2 = method2.getParameterTypes();
                value = parameterTypes1.length - parameterTypes2.length;
                if (0 != value) {
                    break;
                }
                // HACK Need a better way to determine if a method overrides another generic method.
                // for (int i = 0; i < parameterTypes1.length; i++) {
                // value = parameterTypes1[i].getName().compareTo(parameterTypes2[i].getName());
                // if (0 != value) {
                // break;
                // }
                // }
            } while (false);

            return value;
        });
        processImplementedInterfaces(type, alreadyVisited, methods);
        processClassAndImplementedInterfaces(type.getSuperclass(), alreadyVisited, methods);
        return methods;
    }

    private static void processClassAndImplementedInterfaces(final Class<?> type,
                                                             final Set<Class<?>> alreadyVisited,
                                                             final Set<Method> methods) {
        if (null != type && alreadyVisited.add(type)) {
            addMethods(type, methods);
            processClassAndImplementedInterfaces(type.getSuperclass(),
                alreadyVisited,
                methods);
            processImplementedInterfaces(type, alreadyVisited, methods);
        }
    }

    private static void addMethods(final Class<?> klass, final Set<Method> methods) {
        for (final Method method : klass.getDeclaredMethods()) {
            if (MethodAttributes.STATIC.is(method)) {
                continue;
            }
            methods.add(method);
        }
    }

    private static void processImplementedInterfaces(final Class<?> type,
                                                     final Set<Class<?>> alreadyVisited, final Set<Method> methods) {
        for (final Class<?> interfaceClass : type.getInterfaces()) {

            if (alreadyVisited.add(interfaceClass)) {
                addMethods(interfaceClass, methods);
                processImplementedInterfaces(interfaceClass,
                    alreadyVisited,
                    methods);
            }
        }
    }

    /**
     * Stop creation
     */
    private ClassMethodTesting() {
        throw new UnsupportedOperationException();
    }
}
