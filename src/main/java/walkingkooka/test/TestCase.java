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
import walkingkooka.collect.list.Lists;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Base class that includes many useful overloaded assert messages.
 */
abstract public class TestCase {

    protected TestCase() {
        super();
    }

    protected void checkAllPublicIsNonPrimitiveMethods(final Object object) throws Exception {
        assertNotNull("object must not be null", object);

        final List<Method> notNull = Lists.array();
        int isCount = 0;

        for (final Method method : object.getClass().getMethods()) {
            // only check isXXX methods
            final String name = method.getName();
            if (false == name.startsWith("is")) {
                continue;
            }

            // do not care about static isXXX methods.
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            final Class<?> returnType = method.getReturnType();
            if (Void.TYPE.equals(returnType)) {
                Assert.fail("Void method " + method.toGenericString());
            }
            // do not care about primitive types
            if (returnType.isPrimitive()) {
                continue;
            }

            // can not possibly guess parameters so skip
            if (method.getParameterTypes().length > 0) {
                continue;
            }

            Object isValue = null;
            try {
                isValue = method.invoke(object);
            } catch (final Exception cause) {
                throw new Exception(method.toGenericString() + " message=" + cause.getMessage(),
                        cause);
            }
            if (null != isValue) {
                notNull.add(method);
            }

            isCount++;
        }

        switch (isCount) {
            case 0:
                Assert.fail("Did not find any isXXX methods, expected several");
            case 1:
                Assert.fail("Only found isXXX methods, expected several");
            default:
                break;
        }

        final int notNullCount = notNull.size();
        switch (notNullCount) {
            case 0:
                Assert.fail("All the isXXX methods returned null");
                break;
            case 1:
                break;
            default:
                Assert.fail(notNullCount + " isXXX methods returned not null, methods: " + notNull);
        }
    }

    protected void checkToStringOverridden(final Class<?> type) {
        if (false == Fake.class.isAssignableFrom(type)) {
            this.checkToStringOverridden0(type);
        }
    }

    private void checkToStringOverridden0(final Class<?> type) {
        boolean notOverridden = true;

        try {
            final Method method = type.getMethod("toString");
            if (method.getDeclaringClass() != Object.class) {
                notOverridden = false;
            }
        } catch (final NoSuchMethodException cause) {
            notOverridden = true;
        }

        if (notOverridden) {
            Assert.fail(type.getName() + " did not override Object.toString");
        }
    }

    protected byte[] resourceAsBytes(final Class<?> source, final String resource) throws IOException {
        try(final ByteArrayOutputStream out = new ByteArrayOutputStream()){
            final byte[] buffer = new byte[4096];
            try(final InputStream in = source.getResourceAsStream(resource)){
                assertNotNull("Resource " + source.getName() + "/" + resource + " not found", in);
                for(;;){
                    final int count = in.read(buffer);
                    if(-1 == count){
                        break;
                    }
                    out.write(buffer, 0, count);
                }
            }
            out.flush();
            return out.toByteArray();
        }
    }

    protected Reader resourceAsReader(final Class<?> source, final String resource) throws IOException {
        return new StringReader(this.resourceAsText(source, resource));
    }

    protected String resourceAsText(final Class<?> source, final String resource) throws IOException {
        return new String(this.resourceAsBytes(source, resource));
    }

    public static void failNotSame(final Object expected, final Object actual) {
        failNotSame(null, expected, actual);
    }

    public static void failNotSame(final String message, final Object expected, final Object actual) {
        if ((null != expected) && (null != actual) && (false == expected.getClass()
                .equals(actual.getClass()))) {
            Assert.assertNotSame(message, //
                    expected.getClass().getSimpleName() + "=" + expected, //
                    actual.getClass().getSimpleName() + "=" + actual);
        }
        Assert.assertNotSame(message, expected, actual);
    }

    private static boolean nullSafeEquals(final Object first, final Object second) {
        return null == first ? null == second : first.equals(second);
    }

    private static String toString(final boolean[] array) {
        String toString = "null";
        if (null != array) {
            final StringBuilder b = new StringBuilder();

            final int length = array.length;
            b.append(length);
            b.append(' ');

            for (int i = 0; i < length; i++) {
                b.append(array[i] ? '1' : '0');
            }
            toString = b.toString();
        }

        return toString;
    }

    private static String toString(final byte[] array) {
        return null == array ? "null" : array.length + "=" + Arrays.toString(array);
    }

    private static String toString(final char[] array) {
        return null == array ? "null" : array.length + "=" + Arrays.toString(array);
    }

    private static String toString(final Object[] array) {
        return null == array ? "null" : array.length + "=" + Arrays.toString(array);
    }
}
