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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ContextTesting;
import walkingkooka.collect.list.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface with helpers that assist testing of {@link PojoNodeContext}.
 */
public interface PojoNodeContextTesting<C extends PojoNodeContext> extends ContextTesting<C> {

    @Test
    default void testPropertiesWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().properties(null);
        });
    }

    @Test
    default void testWithBooleanPrimitiveFails() {
        this.propertiesFails(Boolean.TYPE);
    }

    @Test
    default void testWithBytePrimitiveFails() {
        this.propertiesFails(Byte.TYPE);
    }

    @Test
    default void testWithShortPrimitiveFails() {
        this.propertiesFails(Short.TYPE);
    }

    @Test
    default void testWithIntegerPrimitiveFails() {
        this.propertiesFails(Integer.TYPE);
    }

    @Test
    default void testWithLongPrimitiveFails() {
        this.propertiesFails(Long.TYPE);
    }

    @Test
    default void testWithFloatPrimitiveFails() {
        this.propertiesFails(Float.TYPE);
    }

    @Test
    default void testWithDoublePrimitiveFails() {
        this.propertiesFails(Double.TYPE);
    }

    @Test
    default void testWithCharacterPrimitiveFails() {
        this.propertiesFails(Character.TYPE);
    }

    @Test
    default void testWithVoidPrimitiveFails() {
        this.propertiesFails(Void.TYPE);
    }

    default void propertiesFails(final Class<?> type) {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().properties(type);
        });
    }

    default List<PojoProperty> properties(final Class<?> type) {
        return this.properties(this.createContext(), type);
    }

    default List<PojoProperty> properties(final C context,
                                          final Class<?> type) {
        final List<PojoProperty> properties = context.properties(type);
        assertNotNull(properties, () -> "properties for " + type.getName());
        return properties;
    }

    default void propertiesAndCheck(final Class<?> type,
                                    final String... names) {
        this.propertiesAndCheck(type, Arrays.stream(names)
                .map(n -> PojoName.property(n))
                .collect(Collectors.toCollection(TreeSet::new)));
    }

    default void propertiesAndCheck(final Class<?> type,
                                    final PojoName... names) {
        this.propertiesAndCheck(type, new TreeSet<>(Lists.of(names)));
    }

    default void propertiesAndCheck(final Class<?> type,
                                    final Set<PojoName> names) {
        final List<PojoProperty> properties = this.properties(type);
        assertEquals(names,
                properties.stream()
                        .map(p -> p.name())
                        .collect(Collectors.toCollection(TreeSet::new)),
                () -> properties + " for " + type.getName());
    }

    default <T> void getAndCheck(final T instance,
                                 final PojoName name,
                                 final Object expected) {
        this.getAndCheck(instance,
                this.property(instance, name),
                expected);
    }

    default <T> void getAndCheck(final T instance,
                                 final PojoProperty property,
                                 final Object expected) {
        assertEquals(expected,
                property.get(instance),
                () -> "Failed to return correct value for " + property + " from " + instance);
    }

    default <T> T setAndGetCheck(final T instance,
                                 final PojoName name,
                                 final Object value) {
        final T result = this.setAndCheck(instance,
                this.property(instance, name),
                value);
        final T instance2 = null == result ? instance : result;
        this.getAndCheck(instance2, name, value);
        return instance2;
    }

    default <T> T setAndCheck(final T instance,
                              final PojoName name,
                              final Object value) {
        return this.setAndCheck(instance,
                this.property(instance, name),
                value);
    }

    default <T> T setAndCheck(final T instance,
                              final PojoProperty property,
                              final Object value) {
        return Cast.to(property.set(instance, value));
    }

    default <T> PojoProperty property(final T instance,
                                      final PojoName name) {
        final List<PojoProperty> properties = this.properties(instance.getClass());
        return properties.stream()
                .filter(p -> p.name().equals(name))
                .collect(Collectors.toList())
                .iterator().next();
    }

    // TypeNameTesting.....................................................................................

    @Override
    default String typeNameSuffix() {
        return PojoNodeContext.class.getSimpleName();
    }
}
