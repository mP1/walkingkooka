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
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;
import walkingkooka.type.PublicClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void testClassVisibility() {
        final Class<?> type = this.type();
        if(this.typeMustBePublic()) {
            assertTrue(type.getName() + " is not public=" + type, Modifier.isPublic(type.getModifiers()));
        } else {
            assertFalse(type.getName() + " is not package private=" + type, Modifier.isPublic(type.getModifiers()));
        }
    }

    @Test
    public void testClassIsFinalIfAllConstructorsArePrivate() {
        final Class<T> type = this.type();
        if (false == this.isAbstract(type)) {
            boolean mustBeFinal = true;
            for (final Constructor<?> constructor : type.getDeclaredConstructors()) {
                if (false == this.isPrivate(constructor)) {
                    mustBeFinal = false;
                    break;
                }
            }

            if (mustBeFinal) {
                if (false == this.isFinal(type)) {
                    Assert.fail("All constructors are private so class should be final="
                            + type.getName());
                }
            }
        }
    }

    /**
     * Constructor is private if this class is final, otherwise they are package private.
     */
    @Test
    public void testAllConstructorsVisibility() throws Throwable {
        final Class<T> type = this.type();
        if (this.isFinal(type)) {
            this.checkAllConstructorsArePrivate(type);
        } else {
            this.checkAllConstructorsArePackagePrivate(type);
        }
    }

    final protected void checkAllConstructorsArePrivate(final Class<T> type) {
        this.checkAllConstructorsVisibility(type, this::checkConstructorIsPrivate);
    }

    final protected void checkAllConstructorsAreProtected(final Class<T> type) {
        this.checkAllConstructorsVisibility(type, this::checkConstructorIsProtected);
    }

    final protected void checkAllConstructorsArePackagePrivate(final Class<T> type) {
        this.checkAllConstructorsVisibility(type, this::checkConstructorIsPackagePrivate);
    }

    private void checkAllConstructorsVisibility(final Class<T> type, final Consumer<Constructor<?>> checker) {
        for (final Constructor<?> constructor : type.getConstructors()) {
            checker.accept(constructor);
        }
    }

    final protected void checkConstructorIsPackagePrivate(final Constructor<?> constructor) {
        if (false == this.isPackagePrivate(constructor)) {
            Assert.fail("Constructor is not package private=" + constructor);
        }
    }

    final protected void checkConstructorIsPrivate(final Constructor<?> constructor) {
        if (false == this.isPrivate(constructor)) {
            Assert.fail("Constructor is not private=" + constructor);
        }
    }

    final protected void checkConstructorIsProtected(final Constructor<?> constructor) {
        if (false == this.isProtected(constructor)) {
            Assert.fail("Constructor is not protected=" + constructor);
        }
    }

    final protected void checkConstructorIsPublic(final Constructor<?> constructor) {
        if (false == this.isPublic(constructor)) {
            Assert.fail("Constructor is not public=" + constructor);
        }
    }

    @Test
    public void testAllFieldsVisibility() {
        final Class<T> type = this.type();
        if (false == type.isAnnotationPresent(PublicClass.class)) {
            if (false == type.isEnum()) {
                for (final Field field : type.getDeclaredFields()) {
                    // static fields may be public
                    if (this.isStatic(field)) {
                        continue;
                    }

                    if (this.isPublic(field) || this.isProtected(field)) {
                        Assert.fail("Fields must be private/package protected(testing)="
                                + field.toGenericString());
                    }
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

    // helpers

    abstract protected Class<T> type();

    abstract protected boolean typeMustBePublic();

    final boolean isPublic(final Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    final boolean isProtected(final Member member) {
        return Modifier.isProtected(member.getModifiers());
    }

    final boolean isPackagePrivate(final Member member) {
        return 0 == (member.getModifiers() & (Modifier.PRIVATE | Modifier.PROTECTED
                | Modifier.PUBLIC));
    }

    final boolean isPrivate(final Member member) {
        return Modifier.isPrivate(member.getModifiers());
    }

    final boolean isStatic(final Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    final boolean isFinal(final Class<?> type) {
        return Modifier.isFinal(type.getModifiers());
    }

    final boolean isFinal(final Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

    final boolean isAbstract(final Class<?> type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    final boolean isAbstract(final Member member) {
        return Modifier.isAbstract(member.getModifiers());
    }

    final boolean isBridge(final Method method) {
        return method.isSynthetic() || method.isBridge();
    }

    final boolean isSythetic(final Method method) {
        return Arrays.equals(method.getGenericParameterTypes(), method.getParameterTypes())
                || method.isBridge();
    }

    final boolean isGeneric(final Method method) {
        return Arrays.equals(method.getGenericParameterTypes(), method.getParameterTypes());
    }

    final boolean isEnumMethod(final Method method) {
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
        this.checkNaming(false, superTypes);
    }

    private void checkNaming(final boolean ignoreCase, final String... superTypes) {
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

    protected void checkNamingStartAndEnd(final Class<?> start, final Class<?> end) {
        this.checkNamingStartAndEnd(start, end.getSimpleName());
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

    protected void checkNamingStartAndEnd(final Class<?> type, final String start,
                                          final Class<?> end) {
        this.checkNamingStartAndEnd(type, start, end.getSimpleName());
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
