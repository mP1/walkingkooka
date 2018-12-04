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

import org.junit.Test;
import walkingkooka.type.PublicClass;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.fail;

/**
 * Adds some additional tests to verify the visibility of the class and constructors.
 */
abstract public class PackagePrivateClassTestCase<T> extends ClassTestCase<T> {

    protected PackagePrivateClassTestCase() {
        super();
    }

    /**
     * If a method is not overridden it should be package private or private.
     */
    @Test
    public void testAllMethodsVisibility() {
        final Class<T> type = this.type();
        if (false == type.isAnnotationPresent(PublicClass.class)) {
            final Set<Method> overridable = this.overridableMethods(type);

            for (final Method method : type.getDeclaredMethods()) {
                if (this.isStatic(method)) {
                    if (this.isEnumMethod(method)) {
                        continue;
                    }
                    if (this.isPublic(method) || this.isProtected(method)) {
                        fail("Static methods should be package private="
                                + method.toGenericString());
                    }
                }
                if (overridable.contains(method)) {
                    continue;
                }
                if (this.isBridge(method) || this.isSythetic(method) || this.isGeneric(method)) {
                    continue;
                }
                if (this.isPublic(method) || this.isProtected(method)) {
                    fail(
                            "Method must be package private/private of it does not override a public/protected method="
                                    + method.toGenericString());
                }
            }
        }
    }

    private Set<Method> overridableMethods(final Class<?> type) {
        final Set<Class<?>> alreadyVisited = new HashSet<Class<?>>();
        final Set<Method> methods = new TreeSet<Method>(new Comparator<Method>() {

            @Override
            public int compare(final Method method1, final Method method2) {
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
            }
        });
        this.processClassAndImplementedInterfaces(type, alreadyVisited, methods);
        return methods;
    }

    private void processClassAndImplementedInterfaces(final Class<?> type,
                                                      final Set<Class<?>> alreadyVisited, final Set<Method> methods) {
        if ((null != type) && (type != Object.class) && alreadyVisited.add(type)) {
            this.addMethods(type, methods);
            this.processClassAndImplementedInterfaces(type.getSuperclass(),
                    alreadyVisited,
                    methods);
            this.processImplementedInterfaces(type, alreadyVisited, methods);
        }
    }

    private void addMethods(final Class<?> klass, final Set<Method> methods) {
        for (final Method method : klass.getDeclaredMethods()) {
            if (this.isStatic(method)) {
                continue;
            }
            methods.add(method);
        }
    }

    private void processImplementedInterfaces(final Class<?> type,
                                              final Set<Class<?>> alreadyVisited, final Set<Method> methods) {
        for (final Class<?> interfaceClass : type.getInterfaces()) {
            if (alreadyVisited.add(interfaceClass)) {
                this.addMethods(interfaceClass, methods);
                this.processClassAndImplementedInterfaces(interfaceClass.getSuperclass(),
                        alreadyVisited,
                        methods);
            }
        }
    }

    @Override
    protected boolean typeMustBePublic() {
        return false;
    }
}
