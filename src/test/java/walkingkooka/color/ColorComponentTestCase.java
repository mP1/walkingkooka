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

package walkingkooka.color;


import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

abstract public class ColorComponentTestCase<C extends ColorComponent> extends ClassTestCase<C> {

    ColorComponentTestCase() {
        super();
    }

    // constants

    private final static byte VALUE = 123;
    private final static int VALUE2 = 240;
    private final static byte BYTE_VALUE2 = (byte) VALUE2;

    // tests

    @Test
    public final void testWith() {
        final C component = this.createColorComponent(VALUE);
        assertEquals("value", VALUE, component.value());
        assertEquals("unsignedIntValue", VALUE, component.unsignedIntValue);
        assertEquals("floatValue", 123.0f / 255f, component.floatValue, 0.1f);
    }

    @Test
    public final void testWithZero() {
        final byte value = 0;
        final C component = this.createColorComponent(value);
        assertEquals("value", value, component.value());
        assertEquals("unsignedIntValue", 0, component.unsignedIntValue);
        assertEquals("floatValue", 0.0f, component.floatValue, 0.1f);
    }

    @Test
    public final void testWith0xFF() {
        final byte value = (byte) 0xFF;
        final C component = this.createColorComponent(value);
        assertEquals("value", value, component.value());
        assertEquals("unsignedIntValue", 0xFF, component.unsignedIntValue);
        assertEquals("floatValue", 1.0f, component.floatValue, 0.1f);
    }

    // add
    @Test
    public final void testAddZero() {
        final C component = this.createColorComponent(VALUE);
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAddZero2() {
        final C component = this.createColorComponent(BYTE_VALUE2);
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAddOne() {
        final C component = this.createColorComponent(VALUE);
        final C added = Cast.to(component.add(1));
        assertEquals("result of add was not the same component type", component.getClass(), added.getClass());
        assertEquals(VALUE + 1, added.unsignedIntValue);
    }

    @Test
    public final void testAddOne2() {
        final C component = this.createColorComponent(BYTE_VALUE2);
        final C added = Cast.to(component.add(1));
        assertEquals("result of add was not the same component type", component.getClass(), added.getClass());
        assertEquals(VALUE2 + 1, added.unsignedIntValue);
    }

    @Test
    public final void testAddNegativeOne() {
        final C component = this.createColorComponent(VALUE);
        final C added = Cast.to(component.add(-1));
        assertEquals("result of add was not the same component type", component.getClass(), added.getClass());
        assertEquals(VALUE - 1, added.unsignedIntValue);
    }

    @Test
    public final void testAddNegativeOne2() {
        final C component = this.createColorComponent(BYTE_VALUE2);
        final C added = Cast.to(component.add(-1));
        assertEquals("result of add was not the same component type", component.getClass(), added.getClass());
        assertEquals(VALUE2 - 1, added.unsignedIntValue);
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

    // invert

    @Test
    public final void testInvert1() {
        final C component = this.createColorComponent((byte) 0);
        final ColorComponent inverted = component.invert();
        assertNotSame("invert should not return this", component, inverted);
        assertEquals("value", (byte) 255, inverted.value);
    }

    @Test
    public final void testInvert2() {
        final C component = this.createColorComponent((byte) 1);
        final ColorComponent inverted = component.invert();
        assertNotSame("invert should not return this", component, inverted);
        assertEquals("value", (byte) 254, inverted.value);
    }

    @Test
    public final void testInvertAll() {
        for (int i = 0; i < 255; i++) {
            final C component = this.createColorComponent((byte) i);
            final ColorComponent inverted = component.invert();
            assertNotSame("invert should not return this", component, inverted);
            assertEquals("value", (byte) ~i, inverted.value);
        }
    }

    @Test
    public final void testSameValueSameInstance() {
        assertSame(this.createColorComponent(VALUE), this.createColorComponent(VALUE));
    }

    @Test
    public final void testIsMethods() throws Exception {
        final C component = this.createColorComponent();
        final String name = component.getClass().getSimpleName();
        assertEquals(name + " starts with Color", true, name.endsWith(ColorComponent.class.getSimpleName()));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(0, name.length() - ColorComponent.class.getSimpleName().length()));

        for(Method method : component.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();

            if(!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(method + " returned",
                    methodName.equals(isMethodName),
                    method.invoke(component));
        }
    }

    @Test
    public final void testToString() {
        assertEquals(Integer.toHexString(VALUE).toUpperCase(),
                this.createColorComponent(VALUE).toString());
    }

    @Test
    public final void testToStringFF() {
        assertEquals("FF", this.createColorComponent((byte) 0xFF).toString());
    }

    @Test
    public final void testToStringLessThan16() {
        assertEquals("0F", this.createColorComponent((byte) 0xF).toString());
    }

    final C createColorComponent() {
        return this.createColorComponent(VALUE);
    }

    abstract C createColorComponent(byte value);

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
