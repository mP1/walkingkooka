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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ThrowableTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ImmutableTypeRegistryTest implements ClassTesting2<ImmutableTypeRegistry>,
        ThrowableTesting,
        ToStringTesting<ImmutableTypeRegistry> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> ImmutableTypeRegistry.with(null));
    }

    @Test
    public void testAddNullFails() {
        assertThrows(NullPointerException.class, () -> this.registry().add(null));
    }

    @Test
    public void testAddAbstractFails() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> this.registry().add(AbstractList.class));
        checkMessage(thrown, "Type java.util.AbstractList is abstract");
    }

    @Test
    public void testAddNotSubclassFails() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> this.registry().add(HashSet.class));

        checkMessage(thrown, "Type java.util.HashSet is not a sub class of java.util.List");
    }

    @Test
    public void testContainsdSubClass() {
        final ImmutableTypeRegistry registry = this.registry();
        assertSame(registry, registry.add(ArrayList.class));
        this.containsCheck(registry,
                ArrayList.class,
                true);
    }

    @Test
    public void testContainsNotSubclass() {
        final ImmutableTypeRegistry registry = this.registry();
        registry.add(ArrayList.class);
        this.containsCheck(registry,
                LinkedList.class,
                false);
    }

    private void containsCheck(final ImmutableTypeRegistry registry,
                               final Class<?> type,
                               final boolean expected) {
        assertEquals(expected,
                registry.contains(type),
                () -> "registry " + registry + " contains " + type.getName());
    }

    @Test
    public void testToString() {
        final ImmutableTypeRegistry registry = this.registry();
        registry.add(ArrayList.class);
        registry.add(LinkedList.class);

        this.toStringAndCheck(registry,
                Lists.of(ArrayList.class.getName(), LinkedList.class.getName()).toString());
    }

    private ImmutableTypeRegistry registry() {
        return ImmutableTypeRegistry.with(List.class);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableTypeRegistry> type() {
        return ImmutableTypeRegistry.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
