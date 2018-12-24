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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.type.ClassAttributes;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;
import walkingkooka.type.PublicClass;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Contains various tests toassert the visibility and final-ness of all methods, fields. Note this
 * class is only public because of JUNIT and is intended to only be sub classed by classes in the
 * same package.
 */
abstract public class ClassTestCase<T> extends TestCase {

    protected ClassTestCase() {
        super();
    }

    // tests

    @Test
    // not final if some tests want to @Ignore.
    public void testTestNaming() {
        final String type = this.type().getName();
        final String test = this.getClass().getName();
        if(!test.endsWith("Test")) {
            fail("Test name " + test + " incorrect for " + type);
        }
        assertEquals("Test name " + test + " incorrect for " + type, test, type + "Test");
    }

    @Test
    public void testClassVisibility() {
        final Class<?> type = this.type();
        final MemberVisibility visibility = Fake.class.isAssignableFrom(type)  ?
                MemberVisibility.PUBLIC :
                this.typeVisibility();

        assertEquals(type.getName() + " visibility",
                visibility,
                MemberVisibility.get(type));
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
                        Assert.fail("All constructors are private so class should be final="
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
    public void testAllConstructorsVisibility() throws Throwable {
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
        if (false == type.isAnnotationPresent(PublicClass.class)) {
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
    }

    /**
     * The base is typically the base sub class of the type being tested, and holds the public static factory method
     * usually named after the type being tested.
     */
    protected final void publicStaticFactoryCheck(final Class<?> base, final String prefix, final Class<?> suffixType) {
        final String suffix = suffixType.getSimpleName();

        final Class<?> type = this.type();
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
        assertEquals("Expected only a single factory method called " + CharSequences.quote(factoryMethodName) +
                " for " + type + " on " + base.getName() + " but got " + factoryMethods + "\n" + publicStaticMethodsToString,
                1,
                factoryMethods.size());
    }

    private static String factoryMethodNameSpecialFixup(final String name, final String suffix){
        String factoryMethodName = name;
        for(String possible : FACTORY_METHOD_NAME_SPECIALS){
            if(name.equals(possible)){
                factoryMethodName = name + suffix;
                break;
            }
        }
        return factoryMethodName;
    }

    private final static String[] FACTORY_METHOD_NAME_SPECIALS = new String[]{ "boolean", "byte", "double", "equals", "int", "long", "null", "short"};

    protected void propertiesNeverReturnNullCheck(final Object object) throws Exception {
        final List<Method> properties = Arrays.stream(object.getClass().getMethods())
                .filter((m) -> this.propertiesNeverReturnNullCheckFilter(m, object))
                .collect(Collectors.toList());
        assertNotEquals("Found zero properties for type=" + object.getClass().getName(), 0, properties.size());
        for(Method method : properties) {
            method.setAccessible(true);
            assertNotNull("null should not have been returned by " + method + " for " + object, method.invoke(object));
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
        if(MemberVisibility.PACKAGE_PRIVATE.is(type)) {
            if (false == type.isAnnotationPresent(PublicClass.class)) {
                final Set<Method> overridable = this.overridableMethods(type);

                for (final Method method : type.getDeclaredMethods()) {
                    if (MethodAttributes.STATIC.is(method)) {
                        if (this.isEnumMethod(method)) {
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
            Assert.fail("Cannot find public constant field of type " + enclosingType + " called "
                    + name);
        }
        Assert.assertEquals("The field " + name + " is wrong the type", fieldType, field.getType());
        Assert.assertTrue("The field " + name + " must be static =" + field, FieldAttributes.STATIC.is(field));
        Assert.assertSame("The field " + name + " must be public =" + field, MemberVisibility.PUBLIC, MemberVisibility.get(field));
        Assert.assertTrue("The field " + name + " must be final=" + field, FieldAttributes.FINAL.is(field));
    }

    // helpers

    abstract protected Class<T> type();

    abstract protected MemberVisibility typeVisibility();

    @SafeVarargs
    protected final void checkNaming(final Class<?>... superTypes) {
        assertNotNull("superTypes is null", superTypes);

        final int count = superTypes.length;
        final String[] names = new String[count];
        for (int i = 0; i < count; i++) {
            names[i] = superTypes[i].getSimpleName();
        }
        this.checkNaming(names);
    }

    protected void checkNaming(final String... superTypes) {
        assertNotNull("superTypes is null", superTypes);

        final String name = this.type().getName();

        final StringBuilder b = new StringBuilder();
        for (final String superType : superTypes) {
            assertNotNull("superType contains null", superType);
            b.append(superType);
        }

        final String expected = b.toString();
        if (false == name.endsWith(expected)) {
            assertEquals("wrong ending", expected, name);
        }
    }

    protected void checkNamingStartAndEnd(final Class<?> start, final String end) {
        this.checkNamingStartAndEnd(start.getSimpleName(), end);
    }

    protected void checkNamingStartAndEnd(final String start, final Class<?> end) {
        this.checkNamingStartAndEnd(start, end.getSimpleName());
    }

    protected void checkNamingStartAndEnd(final String start, final String end) {
        this.checkNamingStartAndEnd(this.type(), start, end);
    }

    protected void checkNamingStartAndEnd(final Class<?> type, final String start, final String end) {
        assertNotNull("type is null", type);
        assertNotNull("start is null", start);
        assertNotNull("end is null", end);

        // Allow some FakeXXX classes dropping the Fake...this allows the Fakes to be used in some tests.
        final String name = type.getSimpleName();
        if (false == name.startsWith(start) && false == name.startsWith(Fake.class.getSimpleName() + start)) {
            assertEquals("wrong start", name, start);
        }

        if (false == name.endsWith(end)) {
            assertEquals("wrong ending", name, end);
        }
    }
}
