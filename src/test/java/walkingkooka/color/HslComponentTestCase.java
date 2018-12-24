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
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

abstract public class HslComponentTestCase<C extends HslComponent> extends ClassTestCase<C> {

    HslComponentTestCase() {
        super();
    }

    // constants

    private final static float LIGHTNESS = 0.5f;

    // tests

    @Test(expected = IllegalArgumentException.class)
    public final void testBelowLowerBoundsFails() {
        this.createHslComponent(HueHslComponent.MIN - 0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAboveUpperBoundsFails() {
        this.createHslComponent(HueHslComponent.MAX + 0.1f);
    }

    @Test
    public final void testWith() {
        final C component = this.createHslComponent(LIGHTNESS);
        assertEquals("value", LIGHTNESS, component.value(), 0.1);
    }

    // set

    @Test
    public final void testSameValue() {
        final C component = this.createHslComponent(LIGHTNESS);
        assertSame(component, component.setValue(LIGHTNESS));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetBelowLowerBoundsFails() {
        this.createHslComponent(this.min()).setValue(this.min() - 0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetAboveUpperBoundsFails() {
        this.createHslComponent(this.min()).setValue(this.max() + 0.1f);
    }

    @Test
    public final void testSet() {
        final float min = this.min();
        final float value = (min + this.max()) / 2;
        final C original = this.createHslComponent(min);
        final HslComponent component = original.setValue(value);
        assertNotSame(original, component);
        assertEquals("value", value, component.value(), 0.1);
    }

    // add

    @Test
    public final void testAddZero() {
        final C component = this.createHslComponent(LIGHTNESS);
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAddZero2() {
        final C component = this.createHslComponent(LIGHTNESS);
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
        final C component = this.createHslComponent(initial);
        final HslComponent added = component.add(delta);
        assertEquals("value", expected, added.value(), 0.1f);
    }

    @Test
    public final void testIsMethods() throws Exception {
        final C component = this.createHslComponent();
        final String name = component.getClass().getSimpleName();

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(0, name.length() - "HslComponent".length()));

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

    @Test
    public final void testToString() {
        assertEquals(String.valueOf(LIGHTNESS), this.createHslComponent(LIGHTNESS).toString());
    }

    final C createHslComponent() {
        return this.createHslComponent(LIGHTNESS);
    }

    abstract C createHslComponent(float value);

    abstract float min();

    abstract float max();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
