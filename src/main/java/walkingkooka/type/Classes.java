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

package walkingkooka.type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

final public class Classes implements PublicStaticHelper {

    /**
     * Tests if the given type is a primitive or its matching wrapper type.
     */
    public static boolean isPrimitiveOrWrapper(final Class<?> classs) {
        Objects.requireNonNull(classs, "type");
        return classs.isPrimitive() ||
                classs == Boolean.class ||
                classs == Byte.class ||
                classs == Character.class ||
                classs == Double.class ||
                classs == Float.class ||
                classs == Integer.class ||
                classs == Long.class ||
                classs == Number.class ||
                classs == Short.class ||
                classs == Void.class;
    }

    /**
     * Tests if the given {@link Class} is static.
     */
    public static boolean isStatic(final Class<?> classs) {
        return Modifier.isStatic(Classes.modifiers(classs));
    }

    /**
     * Tests if the given {@link Class} is abstract.
     */
    public static boolean isAbstract(final Class<?> classs) {
        return Modifier.isAbstract(Classes.modifiers(classs));
    }

    /**
     * Tests if the given {@link Class} is final.
     */
    public static boolean isFinal(final Class<?> classs) {
        return Modifier.isFinal(Classes.modifiers(classs));
    }

    /**
     * Tests if the given {@link Class} is public.
     */
    public static boolean isPublic(final Class<?> classs) {
        return Modifier.isPublic(Classes.modifiers(classs));
    }

    /**
     * Tests if the given {@link Class} is private.
     */
    public static boolean isPrivate(final Class<?> classs) {
        return Modifier.isPrivate(Classes.modifiers(classs));
    }

    /**
     * Tests if the given {@link Class} is package private.
     */
    public static boolean isPackagePrivate(final Class<?> classs) {
        final int modifiers = Classes.modifiers(classs);
        return (false == Modifier.isPublic(modifiers)) && (false == Modifier.isPrivate(modifiers));
    }

    /**
     * Checks then returns the integer holding the modifiers for the given non null {@link
     * Class}.
     */
    private static int modifiers(final Class<?> classs) {
        Objects.requireNonNull(classs, "class");
        return classs.getModifiers();
    }

    /**
     * Verifies that an {@link Class raw class} is an interface.
     */
    public static <I, J extends I> void checkImplements(final Class<I> intf, final J instance) {
        Objects.requireNonNull(intf, "intf");

        if (false == intf.isInstance(instance)) {
            throw new IllegalArgumentException(Classes.interfaceNotImplemented(intf, instance));
        }
    }

    /**
     * Returns a message that a particular instance does not implement the given {@link Class
     * interface}.
     */
    public static String interfaceNotImplemented(final Class<?> intf, final Object instance) {
        return instance.getClass().getName() + " does not implement " + intf.getName();
    }

    /**
     * Tests if the given exception class is a checked exception. Ideally the {@link Class}
     * parameterized type should extends {@link Throwable} but {@link Method#getExceptionTypes()}
     * uses a wildcard.
     */
    public static boolean isCheckedException(final Class<?> throwable) {
        Objects.requireNonNull(throwable, "throwable");

        boolean checked = false;

        if (Exception.class.isAssignableFrom(throwable)) {
            checked = !Error.class.isAssignableFrom(throwable) && //
                    !RuntimeException.class.isAssignableFrom(throwable);
        }

        return checked;
    }

    /**
     * Returns the primitive {@link Class} for the given {@link Class}. if the input is not a
     * wrapper type null is returned.
     */
    public static Class<?> primitive(final Class<?> classs) {
        Objects.requireNonNull(classs, "class");

        Class<?> result = null;
        do {
            if (Boolean.class == classs) {
                result = Boolean.TYPE;
                break;
            }
            if (Byte.class == classs) {
                result = Byte.TYPE;
                break;
            }
            if (Character.class == classs) {
                result = Character.TYPE;
                break;
            }
            if (Double.class == classs) {
                result = Double.TYPE;
                break;
            }
            if (Float.class == classs) {
                result = Float.TYPE;
                break;
            }
            if (Integer.class == classs) {
                result = Integer.TYPE;
                break;
            }
            if (Long.class == classs) {
                result = Long.TYPE;
                break;
            }
            if (Short.class == classs) {
                result = Short.TYPE;
                break;
            }
        } while (false);
        return result;
    }

    /**
     * Returns the primitive {@link Class} for the given {@link Class}. if the input is not a
     * wrapper type null is returned.
     */
    public static Class<?> wrapper(final Class<?> classs) {
        Objects.requireNonNull(classs, "class");

        Class<?> result = null;
        do {
            if (Boolean.TYPE == classs) {
                result = Boolean.class;
                break;
            }
            if (Byte.TYPE == classs) {
                result = Byte.class;
                break;
            }
            if (Character.TYPE == classs) {
                result = Character.class;
                break;
            }
            if (Double.TYPE == classs) {
                result = Double.class;
                break;
            }
            if (Float.TYPE == classs) {
                result = Float.class;
                break;
            }
            if (Integer.TYPE == classs) {
                result = Integer.class;
                break;
            }
            if (Long.TYPE == classs) {
                result = Long.class;
                break;
            }
            if (Short.TYPE == classs) {
                result = Short.class;
                break;
            }
        } while (false);
        return result;
    }

    /**
     * Stop creation
     */
    private Classes() {
        throw new UnsupportedOperationException();
    }
}
