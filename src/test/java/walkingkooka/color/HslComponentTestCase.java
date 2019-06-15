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
import walkingkooka.text.CharSequences;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

abstract public class HslComponentTestCase<C extends HslComponent> extends HslOrHsvComponentTestCase<C> {

    HslComponentTestCase() {
        super();
    }

    // tests

    @Test
    public final void testSet() {
        final float min = this.min();
        final float value = (min + this.max()) / 2;
        final C original = this.createHslComponent(min);
        final HslComponent component = original.setValue(value);
        assertNotSame(original, component);
        assertEquals(value, component.value(), 0.1, "value");
    }

    // add

    @Test
    public final void testAddZero() {
        final C component = this.createHslComponent(value());
        assertSame(component, component.add(0));
    }

    @Test
    public final void testAddZero2() {
        final C component = this.createHslComponent(value());
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
        assertEquals(expected, added.value(), 0.1f, "value");
    }

    @Test
    public final void testIsMethods() throws Exception {
        final C component = this.createHslComponent();
        final String name = component.getClass().getSimpleName();

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(0, name.length() - "HslComponent".length()));

        for (Method method : component.getClass().getMethods()) {
            if (MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();

            if (!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(methodName.equals(isMethodName),
                    method.invoke(component),
                    method + " returned");
        }
    }

    final C createHslComponent() {
        return this.createHslComponent(this.value());
    }

    abstract C createHslComponent(final float value);

    @Override
    final C createHslOrHsvComponent(final float value) {
        return this.createHslComponent(value);
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final String isMethodTypeNameSuffix() {
        return HslComponent.class.getSimpleName();
    }

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNameSuffix() {
        return HslComponent.class.getSimpleName();
    }

}
