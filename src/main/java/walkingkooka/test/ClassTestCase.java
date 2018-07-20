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
import walkingkooka.type.PublicClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

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

    final protected void classIsFinalIfAllConstructorsArePrivateTest() {
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

    final protected void checkAllConstructorsVisibility() {
        final Class<T> type = this.type();
        if (this.isFinal(type)) {
            this.checkAllConstructorsArePrivate(type);
        } else {
            this.checkAllConstructorsArePackagePrivate(type);
        }
    }

    final protected void checkAllConstructorsArePrivate(final Class<T> type) {
        for (final Constructor<?> constructor : type.getConstructors()) {
            if (false == this.isPrivate(constructor)) {
                Assert.fail("Constructor is not private=" + constructor);
            }
        }
    }

    final protected void checkAllConstructorsArePackagePrivate(final Class<T> type) {
        for (final Constructor<?> constructor : type.getConstructors()) {
            this.checkConstructorIsPackagePrivate(constructor);
        }
    }

    final protected void checkConstructorIsPublic(final Constructor<?> constructor) {
        if (false == this.isPublic(constructor)) {
            Assert.fail("Constructor is not public=" + constructor);
        }
    }

    final protected void checkConstructorIsPublicOrPackagePrivate(final Constructor<?> constructor) {
        if (false == this.isPublic(constructor)) {
            Assert.fail("Constructor is not public=" + constructor);
        }
    }

    final protected void checkConstructorIsProtected(final Constructor<?> constructor) {
        if (false == this.isProtected(constructor)) {
            Assert.fail("Constructor is not protected=" + constructor);
        }
    }

    final protected void checkConstructorIsPackagePrivate(final Constructor<?> constructor) {
        if (false == this.isPackagePrivate(constructor)) {
            Assert.fail("Constructor is not package private=" + constructor);
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

    // helpers

    abstract protected Class<T> type();

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

    protected void checkNaming(final Class<?>... superTypes) {
        Assert.assertNotNull("superTypes is null", superTypes);

        final int count = superTypes.length;
        final String[] names = new String[count];
        for (int i = 0; i < count; i++) {
            names[i] = superTypes[i].getSimpleName();
        }
        this.checkNaming(names);
    }

    protected void checkNamingIgnoringCase(final Class<?> superType) {
        this.checkNaming(true, superType.getSimpleName());
    }

    protected void checkNamingIgnoringCase(final String... superTypes) {
        this.checkNaming(true, superTypes);
    }

    protected void checkNaming(final String... superTypes) {
        this.checkNaming(false, superTypes);
    }

    private void checkNaming(final boolean ignoreCase, final String... superTypes) {
        Assert.assertNotNull("superTypes is null", superTypes);

        final String name = this.type().getName();

        final StringBuilder b = new StringBuilder();
        for (final String superType : superTypes) {
            Assert.assertNotNull("superType contains null", superType);
            b.append(superType);
        }

        final String expected = b.toString();

        if (ignoreCase) {
            if (false == name.toLowerCase().endsWith(expected.toLowerCase())) {
                failNotEquals("wrong ending", expected, name);
            }
        } else {
            if (false == name.endsWith(expected)) {
                failNotEquals("wrong ending", expected, name);
            }
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
        Assert.assertNotNull("type is null", type);
        Assert.assertNotNull("start is null", start);
        Assert.assertNotNull("end is null", end);

        final String name = type.getSimpleName();
        if (false == name.startsWith(start)) {
            failNotEquals("wrong start", name, start);
        }

        if (false == name.endsWith(end)) {
            failNotEquals("wrong ending", name, end);
        }
    }
}
