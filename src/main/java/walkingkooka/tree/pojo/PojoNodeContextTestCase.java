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

package walkingkooka.tree.pojo;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class PojoNodeContextTestCase<F extends PojoNodeContext> extends ClassTestCase<F> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullFails() {
        this.createContext().properties(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithBooleanPrimitiveFails() {
        this.createContext().properties(Boolean.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithBytePrimitiveFails() {
        this.createContext().properties(Byte.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithShortPrimitiveFails() {
        this.createContext().properties(Short.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithIntegerPrimitiveFails() {
        this.createContext().properties(Integer.TYPE);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testWithLongPrimitiveFails() {
        this.createContext().properties(Long.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithFloatPrimitiveFails() {
        this.createContext().properties(Float.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithDoublePrimitiveFails() {
        this.createContext().properties(Double.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithCharacterPrimitiveFails() {
        this.createContext().properties(Character.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithVoidPrimitiveFails() {
        this.createContext().properties(Void.TYPE);
    }

    protected abstract F createContext();

    protected List<PojoProperty> properties(final Class<?> type) {
        return this.properties(this.createContext(), type);
    }

    protected List<PojoProperty> properties(final F context, final Class<?> type) {
        final List<PojoProperty> properties = context.properties(type);
        assertNotNull("properties for " + type.getName(), properties);
        return properties;
    }

    protected void propertiesAndCheck(final Class<?> type, final String...names){
        this.propertiesAndCheck(type, Arrays.stream(names)
                .map(n -> PojoName.property(n))
                .collect(Collectors.toCollection(TreeSet::new)));
    }

    protected void propertiesAndCheck(final Class<?> type, final PojoName...names){
        this.propertiesAndCheck(type, new TreeSet<>(Lists.of(names)));
    }

    protected void propertiesAndCheck(final Class<?> type, final Set<PojoName> names){
        final List<PojoProperty> properties = this.properties(type);
        assertEquals(properties + " for " + type.getName(),
                names,
                properties.stream()
                        .map(p -> p.name())
                        .collect(Collectors.toCollection(TreeSet::new)));
    }

    protected <T> void getAndCheck(final T instance, final PojoName name, final Object expected) {
        this.getAndCheck(instance,
                this.property(instance, name),
                expected);
    }

    protected <T> void getAndCheck(final T instance, final PojoProperty property, final Object expected) {
        assertEquals("Failed to return correct value for " + property + " from " + instance,
                expected,
                property.get(instance));
    }

    protected <T> T setAndGetCheck(final T instance, final PojoName name, final Object value) {
        final T result = this.setAndCheck(instance,
                this.property(instance, name),
                value);
        final T instance2 = null == result ? instance : result;
        this.getAndCheck(instance2, name, value);
        return instance2;
    }

    protected <T> T setAndCheck(final T instance, final PojoName name, final Object value) {
        return this.setAndCheck(instance,
                this.property(instance, name),
                value);
    }

    protected <T> T setAndCheck(final T instance, final PojoProperty property, final Object value) {
        return Cast.to(property.set(instance, value));
    }

    protected <T> PojoProperty property(final T instance, final PojoName name) {
        final List<PojoProperty> properties = this.properties(instance.getClass());
        return properties.stream()
                .filter(p -> p.name().equals(name))
                .collect(Collectors.toList())
                .iterator().next();
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
