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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.type.ClassAttributes;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Contains various tests toassert the visibility and final-ness of all methods, fields. Note this
 * class is only public because of JUNIT and is intended to only be sub classed by classes in the
 * same package.
 */
abstract public class ClassTestCase<T> extends TestCase
        implements TestSuiteNameTesting<T> {

    protected ClassTestCase() {
        super();
    }

    // tests

    @Test
    public void testClassVisibility() {
        final Class<?> type = this.type();
        final MemberVisibility visibility = Fake.class.isAssignableFrom(type)  ?
                MemberVisibility.PUBLIC :
                this.typeVisibility();

        assertEquals(visibility,
                MemberVisibility.get(type),
                type.getName() + " visibility");
    }

    @Test
    public void testClassIsFinalIfAllConstructorsArePrivate() {
        final Class<T> type = this.type();
        if (!Fake.class.isAssignableFrom(type)) {
            if (!ClassAttributes.ABSTRACT.is(type)) {
                boolean mustBeFinal = true;
                for (final Constructor<?> constructor : type.getDeclaredConstructors()) {
                    if (false == MemberVisibility.PRIVATE.is(constructor)) {
                        mustBeFinal = false;
                        break;
                    }
                }

                if (mustBeFinal) {
                    if (false == ClassAttributes.FINAL.is(type)) {
                        fail("All constructors are private so class should be final="
                                + type.getName());
                    }
                }
            }
        }
    }

    /**
     * Constructor is private if this class is final, otherwise they are package private.
     */
    @Test
    public void testAllConstructorsVisibility() throws Exception {
        this.checkAllConstructorsVisibility(ClassAttributes.FINAL.is(this.type()) ?
                MemberVisibility.PRIVATE :
                MemberVisibility.PACKAGE_PRIVATE);
    }

    protected final void checkAllConstructorsVisibility(final MemberVisibility visibility) {
        for (final Constructor<?> constructor : this.type().getConstructors()) {
            this.checkConstructorVisibility(constructor, visibility);
        }
    }

    protected final void checkConstructorVisibility(final Constructor<?> constructor,
                                                    final MemberVisibility visibility) {
        if (!visibility.is(constructor)) {
            fail("Constructor " + constructor + " is not " + visibility);
        }
    }

    @Test
    public void testAllFieldsVisibility() {
        final Class<T> type = this.type();

        if (false == type.isEnum()) {
            for (final Field field : type.getDeclaredFields()) {
                if (FieldAttributes.STATIC.is(field) ||
                        MemberVisibility.PRIVATE.is(field) ||
                        MemberVisibility.PACKAGE_PRIVATE.is(field)) {
                    continue;
                }
                fail("Fields must be private/package protected(testing)=" + field.toGenericString());
            }
        }
    }

    protected void propertiesNeverReturnNullCheck(final Object object) throws Exception {
        final List<Method> properties = Arrays.stream(object.getClass().getMethods())
                .filter((m) -> this.propertiesNeverReturnNullCheckFilter(m, object))
                .collect(Collectors.toList());
        assertNotEquals(0,
                properties.size(),
                "Found zero properties for type=" + object.getClass().getName());
        for(Method method : properties) {
            method.setAccessible(true);
            assertNotNull(method.invoke(object),
                    () -> "null should not have been returned by " + method + " for " + object);
        }
    }

    /**
     * Keep instance methods, that return something, take no parameters, arent a Object member or tagged with {@link SkipPropertyNeverReturnsNullCheck}
     */
    private boolean propertiesNeverReturnNullCheckFilter(final Method method, final Object object) {
        return !MethodAttributes.STATIC.is(method) &&
                method.getReturnType() != Void.class &&
                method.getParameterTypes().length == 0 &&
                method.getDeclaringClass() != Object.class &&
                !skipMethod(method, object);
    }

    private boolean skipMethod(final Method method, final Object object) {
        boolean skip = false;
        if(method.isAnnotationPresent(SkipPropertyNeverReturnsNullCheck.class)){
            final Class<?>[] skipTypes = method.getAnnotation(SkipPropertyNeverReturnsNullCheck.class).value();
            skip = Arrays.asList(skipTypes).contains(object.getClass());
        }
        return skip;
    }

    /**
     * If a method is not overridden it should be package private or private.
     */
    @Test
    public void testAllMethodsVisibility() {
        final Class<T> type = this.type();
        if (MemberVisibility.PACKAGE_PRIVATE.is(type)) {

            final Set<Method> overridable = this.overridableMethods(type);

            for (final Method method : type.getDeclaredMethods()) {
                if (MethodAttributes.STATIC.is(method)) {
                    if (this.isMainMethod(method) || this.isEnumMethod(method)) {
                        continue;
                    }
                    if (MemberVisibility.PUBLIC.is(method) || MemberVisibility.PROTECTED.is(method)) {
                        fail("Static methods should be package private="
                                + method.toGenericString());
                    }
                }
                if (overridable.contains(method)) {
                    continue;
                }
                if (MethodAttributes.BRIDGE.is(method) || MethodAttributes.SYNTHETIC.is(method) || this.isGeneric(method)) {
                    continue;
                }
                if (MemberVisibility.PUBLIC.is(method) || MemberVisibility.PROTECTED.is(method)) {
                    fail(
                            "Method must be package private/private of it does not override a public/protected method="
                                    + method.toGenericString());
                }
            }

        }
    }

    /**
     * Returns true if the given method has the signature of a main method used to start a java program in a JVM.
     */
    private boolean isMainMethod(final Method method) {
        return method.getName().equals("main") &&
                Arrays.equals(new Class[]{String[].class}, method.getParameterTypes());
    }

    private boolean isEnumMethod(final Method method) {
        boolean result = false;

        do {
            final String name = method.getName();
            if (method.getDeclaringClass().isEnum()) {
                if (name.equals("valueOf")) {
                    result = true;
                    continue;
                }
                if (name.equals("values")) {
                    result = true;
                    continue;
                }
            }
        } while (false);
        return result;
    }

    private boolean isGeneric(final Method method) {
        return Arrays.equals(method.getGenericParameterTypes(), method.getParameterTypes());
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
            if (MethodAttributes.STATIC.is(method)) {
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

    /**
     * Asserts that a field is public static and final.
     */
    protected void checkFieldIsPublicStaticFinal(final Class<?> enclosingType,
                                                 final String name,
                                                 final Class<?> fieldType) {
        Field field = null;
        try {
            field = enclosingType.getDeclaredField(name);
        } catch (final Exception cause) {
            Assertions.fail("Cannot find public constant field of type " + enclosingType + " called "
                    + name);
        }

        final Field field2 = field;
        assertEquals(fieldType, field.getType(), "The field " + name + " is wrong the type");
        assertTrue(FieldAttributes.STATIC.is(field), () -> "The field " + name + " must be static =" + field2);
        assertSame(MemberVisibility.PUBLIC, MemberVisibility.get(field), () -> "The field " + name + " must be public =" + field2);
        assertTrue(FieldAttributes.FINAL.is(field), () -> "The field " + name + " must be final=" + field2);
    }

    // helpers

    abstract protected MemberVisibility typeVisibility();
}
