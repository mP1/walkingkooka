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

package walkingkooka.type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

final public class Types implements PublicStaticHelper {

    /**
     * Tests if the given type is a primitive or its matching wrapper type.
     */
    public static boolean isPrimitiveOrWrapper(final Class<?> type) {
        Objects.requireNonNull(type, "type");
        return type.isPrimitive() ||
                type == Boolean.class ||
                type == Byte.class ||
                type == Character.class ||
                type == Double.class ||
                type == Float.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Number.class ||
                type == Short.class ||
                type == Void.class;
    }

    /**
     * Tests if the given {@link Class} is static.
     */
    public static boolean isStatic(final Class<?> klass) {
        return Modifier.isStatic(Types.modifiers(klass));
    }

    /**
     * Tests if the given {@link Class} is abstract.
     */
    public static boolean isAbstract(final Class<?> klass) {
        return Modifier.isAbstract(Types.modifiers(klass));
    }

    /**
     * Tests if the given {@link Class} is final.
     */
    public static boolean isFinal(final Class<?> klass) {
        return Modifier.isFinal(Types.modifiers(klass));
    }

    /**
     * Tests if the given {@link Class} is public.
     */
    public static boolean isPublic(final Class<?> klass) {
        return Modifier.isPublic(Types.modifiers(klass));
    }

    /**
     * Tests if the given {@link Class} is private.
     */
    public static boolean isPrivate(final Class<?> klass) {
        return Modifier.isPrivate(Types.modifiers(klass));
    }

    /**
     * Tests if the given {@link Class} is package private.
     */
    public static boolean isPackagePrivate(final Class<?> klass) {
        final int modifiers = Types.modifiers(klass);
        return (false == Modifier.isPublic(modifiers)) && (false == Modifier.isPrivate(modifiers));
    }

    /**
     * Checks then returns the integer holding the modifiers for the given non null {@link
     * Class}.
     */
    private static int modifiers(final Class<?> klass) {
        Objects.requireNonNull(klass, "class");
        return klass.getModifiers();
    }

    /**
     * Verifies that an {@link Class raw class} is an interface.
     */
    public static <I, J extends I> void checkImplements(final Class<I> intf, final J instance) {
        Objects.requireNonNull(intf, "intf");

        if (false == intf.isInstance(instance)) {
            throw new IllegalArgumentException(Types.interfaceNotImplemented(intf, instance));
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
    public static Class<?> primitive(final Class<?> klass) {
        Objects.requireNonNull(klass, "class");

        Class<?> result = null;
        do {
            if (Boolean.class == klass) {
                result = Boolean.TYPE;
                break;
            }
            if (Byte.class == klass) {
                result = Byte.TYPE;
                break;
            }
            if (Character.class == klass) {
                result = Character.TYPE;
                break;
            }
            if (Double.class == klass) {
                result = Double.TYPE;
                break;
            }
            if (Float.class == klass) {
                result = Float.TYPE;
                break;
            }
            if (Integer.class == klass) {
                result = Integer.TYPE;
                break;
            }
            if (Long.class == klass) {
                result = Long.TYPE;
                break;
            }
            if (Short.class == klass) {
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
    public static Class<?> wrapper(final Class<?> klass) {
        Objects.requireNonNull(klass, "class");

        Class<?> result = null;
        do {
            if (Boolean.TYPE == klass) {
                result = Boolean.class;
                break;
            }
            if (Byte.TYPE == klass) {
                result = Byte.class;
                break;
            }
            if (Character.TYPE == klass) {
                result = Character.class;
                break;
            }
            if (Double.TYPE == klass) {
                result = Double.class;
                break;
            }
            if (Float.TYPE == klass) {
                result = Float.class;
                break;
            }
            if (Integer.TYPE == klass) {
                result = Integer.class;
                break;
            }
            if (Long.TYPE == klass) {
                result = Long.class;
                break;
            }
            if (Short.TYPE == klass) {
                result = Short.class;
                break;
            }
        } while (false);
        return result;
    }

    //    /**
    //     * Creates a {@link WildcardType} with the given upper bound.
    //     */
    //    public static WildcardType subTypeOf(final Type bound)
    //    {
    //        Objects.requireNonNull(klass, "class");
    //
    //        return com.google.inject.util.Types.subtypeOf(bound);
    //    }
    //
    //    /**
    //     * Creates a {@link WildcardType} with the given lower bound.
    //     */
    //    public static WildcardType superTypeOf(final Type bound)
    //    {
    //        TypeParameters.BOUND.check(bound);
    //        return com.google.inject.util.Types.supertypeOf(bound);
    //    }
    //
    //    /**
    //     * Creates a {@link ParameterizedType}
    //     */
    //    public static ParameterizedType parameterizedType(final Type rawType,
    //            final Type... typeArguments)
    //    {
    //        TypeParameters.RAW_TYPE.check(rawType);
    //        TypeParameters.TYPE_ARGUMENTS.check(typeArguments);
    //
    //        return com.google.inject.util.Types.newParameterizedType(rawType, typeArguments);
    //    }
    //
    //    /**
    //     * Creates an enclosed {@link ParameterizedType}
    //     */
    //    public static ParameterizedType enclosedParameterizedType(final Type ownerType,
    //            final Type rawType, final Type... typeArguments)
    //    {
    //        TypeParameters.OWNER_TYPE.check(ownerType);
    //        TypeParameters.RAW_TYPE.check(rawType);
    //        TypeParameters.TYPE_ARGUMENTS.check(typeArguments);
    //
    //        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    //    }
    //
    //    /**
    //     * Returns a {@link GenericArrayType}.
    //     */
    //    public static GenericArrayType arrayOf(final Type componentType)
    //    {
    //        TypeParameters.COMPONENT_TYPE.check(componentType);
    //        return com.google.inject.util.Types.arrayOf(componentType);
    //    }
    //
    //    /**
    //     * Returns a {@link ParameterizedType} containing a {@link List} of {@link Type type}.
    //     */
    //    public static ParameterizedType listOf(final Type type)
    //    {
    //        TypeParameters.TYPE.check(type);
    //        return com.google.inject.util.Types.listOf(type);
    //    }
    //
    //    /**
    //     * Returns a {@link ParameterizedType} containing a {@link Set} of {@link Type type}.
    //     */
    //    public static ParameterizedType setOf(final Type type)
    //    {
    //        TypeParameters.TYPE.check(type);
    //        return com.google.inject.util.Types.setOf(type);
    //    }
    //
    //    /**
    //     * Returns a {@link ParameterizedType} containing a {@link Map} with the key and value {@link
    //     * Type types}.
    //     */
    //    public static ParameterizedType mapOf(final Type key, final Type value)
    //    {
    //        TypeParameters.KEY.check(key);
    //        TypeParameters.VALUE.check(value);
    //        return com.google.inject.util.Types.mapOf(key, value);
    //    }
    //
    //    /**
    //     * Returns the raw {@link Class} from the first element in the given array of {@link Type}
    //     */
    //    public static Class<?> raw(final Type[] types)
    //    {
    //        return RawClass.get(types);
    //    }
    //
    //    /**
    //     * Returns the raw {@link Class} given this {@link Type}.
    //     */
    //    public static Class<?> raw(final Type type)
    //    {
    //        return RawClass.get(type);
    //    }

    /**
     * Stop creation
     */
    private Types() {
        throw new UnsupportedOperationException();
    }
}
