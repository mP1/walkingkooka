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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

abstract public class HsvComponentTestCase<C extends HsvComponent> extends ClassTestCase<C>
        implements HashCodeEqualsDefinedTesting<C>, SerializationTesting<C> {

    HsvComponentTestCase() {
        super();
    }

    // constants

    private final static float VALUE = 0.5f;

    // tests

    @Test(expected = IllegalArgumentException.class)
    public final void testBelowLowerBoundsFails() {
        this.createHsvComponent(HueHsvComponent.MIN - 0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAboveUpperBoundsFails() {
        this.createHsvComponent(HueHsvComponent.MAX + 0.1f);
    }

    @Test
    public final void testWith() {
        final C component = this.createHsvComponent(VALUE);
        assertEquals("value", VALUE, component.value(), 0.1);
    }

    // set

    @Test
    public final void testSameValue() {
        final C component = this.createHsvComponent();
        assertSame(component, component.setValue(VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetBelowLowerBoundsFails() {
        this.createHsvComponent().setValue(this.min() - 0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetAboveUpperBoundsFails() {
        this.createHsvComponent().setValue(this.max() + 0.1f);
    }

    @Test
    public final void testSet() {
        final float min = this.min();
        final float value = (min + this.max()) / 2;
        final C original = this.createHsvComponent(min);
        final HsvComponent component = original.setValue(value);
        assertNotSame(original, component);
        assertEquals("value", value, component.value(), 0.1f);
    }

    // add

    @Test
    public final void testAddZero() {
        final C component = this.createHsvComponent(VALUE);
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAddZero2() {
        final C component = this.createHsvComponent(VALUE);
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAdd() {
        final float min = this.min();
        this.addAndCheck(min, 0.1f, min + 0.1f);
    }

    @Test
    public final void testAddUnderflows() {
        final float min = this.min();
        final float max = this.max();
        this.addAndCheck((min + max) / 2, -max, min);
    }

    @Test
    public final void testAddOverflow() {
        final float min = this.min();
        final float max = this.max();
        this.addAndCheck((min + max) / 2, max, max);
    }

    private void addAndCheck(final float initial, final float delta, final float expected) {
        final C component = this.createHsvComponent(initial);
        final HsvComponent added = component.add(delta);
        assertEquals("value", expected, added.value(), 0.1f);
    }

    @Test
    public final void testIsMethods() throws Exception {
        final C component = this.createHsvComponent();
        final String name = component.getClass().getSimpleName();

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(0, name.length() - "HsvComponent".length()));

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

    // toString

    public final void testToString() {
        assertEquals(String.valueOf(VALUE), this.createHsvComponent(VALUE).toString());
    }

    final C createHsvComponent() {
        return this.createHsvComponent(VALUE);
    }

    abstract C createHsvComponent(float value);

    abstract float min();

    abstract float max();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public final C createObject() {
        return this.createHsvComponent();
    }
}
