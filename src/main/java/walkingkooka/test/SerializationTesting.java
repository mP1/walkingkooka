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

package walkingkooka.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public interface SerializationTesting<S extends Serializable> extends Testing {

    @Test
    default void testSerialVersionUIDDeclared() {
        Class<?> t = this.type();
        do {
            try {
                if (false == Serializable.class.isAssignableFrom(t)) {
                    break;
                }
                final Field field = t.getDeclaredField("serialVersionUID");
                final int modifiers = field.getModifiers();
                if (false == Modifier.isStatic(modifiers)) {
                    Assert.fail(field + " is not static.");
                }
                if (false == Modifier.isFinal(modifiers)) {
                    Assert.fail(field + " is not final.");
                }
                if (false == Modifier.isPrivate(modifiers)) {
                    Assert.fail(field + " is not private.");
                }
            } catch (final NoSuchFieldException absent) {
                Assert.fail(t + " does not include a \"serialVersionUID\" field");
            }

            t = t.getSuperclass();
        } while (null != t);
    }

    @Test
    default void testSerializationWriteThenReadBack() {
        final S object = this.serializableInstance();
        S deserialized = null;
        try {
            deserialized = this.cloneUsingSerialization(object);
        } catch (final Exception exception) {
            exception.printStackTrace();
            Assert.fail(object + " is not serializable, cause: " + exception.getMessage());
        }

        if (this.serializableInstanceIsSingleton()) {
            assertSame("Singleton should return same instance", object, deserialized);
        } else {
            assertNotSame("Non singletons should deserialize a different but equal instance",
                    object,
                    deserialized);
            assertEquals(object, deserialized);
        }
    }

    /**
     * Uses serialization to write the given {@link Object} and then read it back using a {@link
     * ObjectInputStream} that is aware of the original's {@link ClassLoader}.
     */
    @SuppressWarnings("unchecked")
    default <S extends Serializable> S cloneUsingSerialization(final Serializable object)
            throws IOException, ClassNotFoundException {
        Assert.assertNotNull("object to be cloned is null", object);

        final Thread thread = Thread.currentThread();
        final ClassLoader classLoader = object.getClass().getClassLoader();

        try {
            thread.setContextClassLoader(classLoader);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final ObjectOutputStream serializer = new ObjectOutputStream(output);
            serializer.writeObject(object);
            output.flush();
            output.close();

            final ObjectInputStream deserializer = new ObjectInputStream(//
                    new ByteArrayInputStream(output.toByteArray())) {
                @Override
                public Class<?> resolveClass(final ObjectStreamClass desc)
                        throws IOException, ClassNotFoundException {
                    try {
                        return super.resolveClass(desc);
                    } catch (final ClassNotFoundException ex) {
                        final String name = desc.getName();
                        final Class<?> classs = Class.forName(name, false, classLoader);
                        if (classs == null) {
                            throw ex;
                        }
                        return classs;
                    }
                }
            };
            return (S) deserializer.readObject();
        } finally {
            thread.setContextClassLoader(classLoader);
        }
    }

    /**
     * Scans for public constants of the same type verifying that each when serialized remains a
     * singleton.
     */
    default void constantsAreSingletons() throws Exception {
        final Class<S> type = this.type();

        int i = 0;
        for (final Field field : type.getDeclaredFields()) {
            // skip non public non static fields
            final int modifiers = field.getModifiers();
            if (false == Modifier.isStatic(modifiers)) {
                continue;
            }
            if (false == Modifier.isPublic(modifiers)) {
                continue;
            }
            // wrong type
            if (false == field.getType().equals(type)) {
                continue;
            }
            // complain if not final.
            if (false == Modifier.isFinal(modifiers)) {
                Assert.fail(field.toString() + " is NOT final=" + field);
            }
            @SuppressWarnings("unchecked") final S constant = (S) field.get(null);
            final S deserialized = this.cloneUsingSerialization(constant);
            assertSame("Constant is not a singleton=" + field, constant, deserialized);
            i++;
        }

        if (0 == i) {
            Assert.fail("No public static constants found in " + type.getName());
        }
    }

    default void serializeSingletonAndCheck(final Serializable object) throws IOException, ClassNotFoundException {
        final S serialized = this.cloneUsingSerialization(object);
        assertSame(object + " is not a singleton", object, serialized);
    }

    Class<S> type();

    S serializableInstance();

    boolean serializableInstanceIsSingleton();
}
