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


import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class HsvComponentTestCase<C extends HsvComponent> implements ClassTesting2<C>,
        HashCodeEqualsDefinedTesting<C>,
        IsMethodTesting<C>,
        SerializationTesting<C>,
        ToStringTesting<C>,
        TypeNameTesting<C> {

    HsvComponentTestCase() {
        super();
    }

    // constants

    private final static float VALUE = 0.5f;

    // tests

    @Test
    public final void testBelowLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHsvComponent(HueHsvComponent.MIN - 0.1f);
        });
    }

    @Test
    public final void testAboveUpperBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHsvComponent(HueHsvComponent.MAX + 0.1f);
        });
    }

    @Test
    public final void testWith() {
        final C component = this.createHsvComponent(VALUE);
        assertEquals(VALUE, component.value(), 0.1, "value");
    }

    // set

    @Test
    public final void testSameValue() {
        final C component = this.createHsvComponent();
        assertSame(component, component.setValue(VALUE));
    }

    @Test
    public final void testSetBelowLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHsvComponent().setValue(this.min() - 0.1f);
        });
    }

    @Test
    public final void testSetAboveUpperBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHsvComponent().setValue(this.max() + 0.1f);
        });
    }

    @Test
    public final void testSet() {
        final float min = this.min();
        final float value = (min + this.max()) / 2;
        final C original = this.createHsvComponent(min);
        final HsvComponent component = original.setValue(value);
        assertNotSame(original, component);
        assertEquals(value, component.value(), 0.1f, "value");
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
        assertEquals(expected, added.value(), 0.1f, "value");
    }

    // toString

    public final void testToString() {
        this.toStringAndCheck(this.createHsvComponent(VALUE), String.valueOf(VALUE));
    }

    final C createHsvComponent() {
        return this.createHsvComponent(VALUE);
    }

    abstract C createHsvComponent(float value);

    abstract float min();

    abstract float max();

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public final C createObject() {
        return this.createHsvComponent();
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
        return HsvComponent.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return HsvComponent.class.getSimpleName();
    }
}
