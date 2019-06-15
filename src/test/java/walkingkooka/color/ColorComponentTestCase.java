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

package walkingkooka.color;


import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.JavaVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

abstract public class ColorComponentTestCase<C extends ColorComponent> implements ClassTesting2<C>,
        HashCodeEqualsDefinedTesting<C>,
        IsMethodTesting<C>,
        SerializationTesting<C>,
        ToStringTesting<C>,
        TypeNameTesting<C> {

    ColorComponentTestCase() {
        super();
    }

    // constants

    final static byte VALUE = 0x11;
    final static byte VALUE2 = 0x22;

    // tests

    @Test
    public final void testWith() {
        this.createComponentAndCheck(VALUE, VALUE, VALUE / 255f);
    }

    @Test
    public final void testWith2() {
        this.createComponentAndCheck(VALUE2, VALUE2, VALUE2 / 255f);
    }

    @Test
    public final void testWithZero() {
        this.createComponentAndCheck((byte) 0, 0x0, 0.0f);
    }

    @Test
    public final void testWith0xFF() {
        this.createComponentAndCheck((byte) 0xFF, 0xFF, 1.0f);
    }

    private void createComponentAndCheck(final byte value, final int unsigned, final float floatValue) {
        final C component = this.createColorComponent(value);
        assertEquals(value, component.value(), "value");
        assertEquals(unsigned, component.unsignedIntValue, "unsignedIntValue");
        assertEquals(floatValue, component.floatValue, 0.1f, "floatValue");
    }

    // add
    @Test
    public final void testAddZero() {
        final C component = this.createColorComponent(VALUE);
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAddZero2() {
        final C component = this.createColorComponent(VALUE2);
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAddOne() {
        this.addAndCheck(this.createColorComponent(VALUE), 1, VALUE + 1);
    }

    @Test
    public final void testAddOne2() {
        this.addAndCheck(this.createColorComponent(VALUE2), 1, VALUE2 + 1);
    }

    @Test
    public final void testAddNegativeOne() {
        this.addAndCheck(this.createColorComponent(VALUE), -1, VALUE - 1);
    }

    @Test
    public final void testAddNegativeOne2() {
        this.addAndCheck(this.createColorComponent(VALUE2), -1, VALUE2 - 1);
    }

    @Test
    public final void testAddSaturatedOverflows() {
        final C component = this.createColorComponent(VALUE);
        final C added = Cast.to(component.add(512));
        assertEquals(255, added.unsignedIntValue);
    }

    @Test
    public final void testAddSaturatedUnderflow() {
        final C component = this.createColorComponent(VALUE);
        final C added = Cast.to(component.add(-512));
        assertEquals(0, added.unsignedIntValue);
    }

    private void addAndCheck(final ColorComponent component,
                             final int add,
                             final int value) {
        final ColorComponent added = Cast.to(component.add(add));
        assertNotSame(component, added);
        assertEquals(component.getClass(), added.getClass(), "result of add was not the same component type");
        assertEquals(value, added.unsignedIntValue, "component " + component + " add " + add + " =" + value);
    }

    // invert

    @Test
    public final void testInvert1() {
        this.invertAndCheck(this.createColorComponent((byte) 0), 255);
    }

    @Test
    public final void testInvert2() {
        this.invertAndCheck(this.createColorComponent((byte) 1), 254);
    }

    @Test
    public final void testInvertAll() {
        for (int i = 0; i < 255; i++) {
            this.invertAndCheck(this.createColorComponent((byte) i), ~i);
        }
    }

    private void invertAndCheck(final ColorComponent component, final int value) {
        final ColorComponent inverted = component.invert();
        assertNotSame(component, inverted, "invert should not return this");
        assertEquals((byte) value, inverted.value(), "value");
    }

    @Test
    public final void testSameValueSameInstance() {
        assertSame(this.createColorComponent(VALUE), this.createColorComponent(VALUE));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createColorComponent(VALUE), Integer.toHexString(VALUE).toUpperCase());
    }

    @Test
    public final void testToStringFF() {
        this.toStringAndCheck(this.createColorComponent((byte) 0xFF), "ff");
    }

    @Test
    public final void testToStringLessThan16() {
        this.toStringAndCheck(this.createColorComponent((byte) 0xF), "0f");
    }

    @Test
    public final void testFF() throws Exception {
        this.cloneUsingSerialization(this.createColorComponent((byte) 0x255));
    }

    final C createColorComponent() {
        return this.createColorComponent(VALUE);
    }

    abstract C createColorComponent(byte value);

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public final C createObject() {
        return this.createColorComponent();
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final C createIsMethodObject() {
        return this.createObject();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return ColorComponent.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return ColorComponent.class.getSimpleName();
    }
}
