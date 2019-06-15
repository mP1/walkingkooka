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
package walkingkooka.tree.pojo;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link PojoNodeContext} that discovers properties using reflection for given types.
 */
final class ReflectionPojoNodeContext implements PojoNodeContext {

    // properties....

    @Override
    public List<PojoProperty> properties(final Class<?> type) {
        Objects.requireNonNull(type, "type");

        List<PojoProperty> properties = this.typeToProperties.get(type);
        if (null == properties) {
            properties = discoverProperties(type);
            this.typeToProperties.put(type, properties);
        }
        return properties;
    }

    /**
     * A cache that holds class to its properties for future reuse.
     */
    private final Map<Class<?>, List<PojoProperty>> typeToProperties = Maps.concurrent();

    private List<PojoProperty> discoverProperties(final Class<?> type) {
        if (type.isPrimitive()) {
            throw new IllegalArgumentException("Primitive types not supported, type= " + CharSequences.quote(type.getName()));
        }

        try {
            return discoverProperties0(type);
        } catch (final Exception cause) {
            throw new ReflectionPojoException("Failed to retrieve all properties for " + CharSequences.quote(type.getName()) + ", " + cause.getMessage(), cause);
        }
    }

    private static List<PojoProperty> discoverProperties0(final Class<?> type) {
        final List<PojoProperty> properties = Lists.array();

        for (Method method : type.getMethods()) {
            if (isStatic(method)) {
                continue;
            }
            if (!isWithoutParameters(method)) {
                continue;
            }
            if (isObjectMethod(method)) {
                continue;
            }
            acceptMethod(method, properties);
        }

        properties.sort(POJO_PROPERTY_COMPARATOR);
        return properties;
    }

    private final static Comparator<PojoProperty> POJO_PROPERTY_COMPARATOR = (p1, p2) -> p1.name().compareTo(p2.name());

    private static boolean isStatic(final Method method) {
        return MethodAttributes.STATIC.is(method);
    }

    private static boolean isWithoutParameters(final Method method) {
        return method.getParameterTypes().length == 0;
    }

    private static boolean isObjectMethod(final Method method) {
        final String name = method.getName();
        return "getClass".equals(name) || "hashCode".equals(name) || "toString".equals(name);
    }

    private static void acceptMethod(final Method method, final List<PojoProperty> properties) {
        final Class<?> type = method.getReturnType();
        if (Void.TYPE != type) {
            for (; ; ) {
                final String methodName = method.getName();
                if (type == Boolean.TYPE || type == Boolean.class) {
                    if (isPrefixed(methodName, "is")) {
                        findSetterAndSave(removePrefix(methodName, "is"),
                                method,
                                properties);
                        break;
                    }
                }
                if (isPrefixed(methodName, "get")) {
                    findSetterAndSave(removePrefix(methodName, "get"),
                            method,
                            properties);
                    break;
                }
                if (Character.isLowerCase(methodName.charAt(0))) {
                    findSetterAndSave(methodName,
                            method,
                            properties);
                }
                break;
            }
        }
    }

    // isX where X is a capital letter
    private static boolean isPrefixed(final String name, final String prefix) {
        final int prefixLength = prefix.length();
        return name.length() >= prefixLength + 1 &&
                name.startsWith(prefix) &&
                Character.isUpperCase(name.charAt(prefixLength));
    }

    private static String removePrefix(final String name, final String prefix) {
        final int prefixLength = prefix.length();
        return Character.toLowerCase(name.charAt(prefixLength)) + name.substring(prefixLength + 1);
    }

    private static void findSetterAndSave(final String propertyName, final Method getter, final List<PojoProperty> properties) {
        properties.add(ReflectionPojoProperty.with(PojoName.property(propertyName),
                getter,
                setter(propertyName, getter)));
    }

    private static Method setter(final String propertyName, final Method getter) {
        final String name = "set" + CharSequences.capitalize(propertyName);

        Method setter = null;
        try {
            setter = getter.getDeclaringClass().getMethod(name, getter.getReturnType());
            if (isStatic(setter)) {
                setter = null;
            }
        } catch (final NoSuchMethodException fail) {
            // no setter present...
        } catch (final Exception fail) {
            throw new ReflectionPojoException("Setter method discovery failed, " + fail.getMessage(), fail);
        }
        return setter;
    }

    /**
     * Creates a new {@link List}
     */
    public List<Object> createList(final Class<?> type) {
        List<Object> list;
        try {
            list = Cast.to(type.newInstance());
        } catch (final Exception cause) {
            list = Lists.array();
        }
        return list;
    }

    /**
     * Creates a new {@link Set}
     */
    public Set<Object> createSet(final Class<?> type) {
        Set<Object> list;
        try {
            list = Cast.to(type.newInstance());
        } catch (final Exception cause) {
            list = Sets.ordered();
        }
        return list;
    }

    /**
     * Creates a new {@link Map}
     */
    public Map<Object, Object> createMap(final Class<?> type) {
        Map<Object, Object> list;
        try {
            list = Cast.to(type.newInstance());
        } catch (final Exception cause) {
            list = Maps.ordered();
        }
        return list;
    }

    @Override
    public String toString() {
        return this.typeToProperties.toString();
    }
}
