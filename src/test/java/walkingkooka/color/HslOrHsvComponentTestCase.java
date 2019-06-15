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

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HslOrHsvComponentTestCase<C extends HslOrHsvComponent> extends ColorHslOrHsvComponentTestCase<C> {

    HslOrHsvComponentTestCase() {
        super();
    }


    @Test
    public final void testBelowLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHslOrHsvComponent(this.min() - 0.1f);
        });
    }

    @Test
    public final void testAboveUpperBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHslOrHsvComponent(this.max() + 0.1f);
        });
    }

    @Test
    public final void testWith() {
        final C component = this.createHslOrHsvComponent(value());
        assertEquals(value(), component.value(), 0.1, "value");
    }

    // set

    @Test
    public final void testSameValue() {
        final C component = this.createHslOrHsvComponent(value());
        assertSame(component, component.setValue(value()));
    }

    @Test
    public final void testSetBelowLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHslOrHsvComponent(this.min()).setValue(this.min() - 0.1f);
        });
    }

    @Test
    public final void testSetAboveUpperBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createHslOrHsvComponent(this.min()).setValue(this.max() + 0.1f);
        });
    }

    abstract float value();

    abstract float min();

    abstract float max();

    abstract C createHslOrHsvComponent(final float value);

    // HashCodeEqualsDefinedTesting......................................................................................

    @Override
    public C createObject() {
        return this.createHslOrHsvComponent(this.value());
    }

    // IsMethodTesting..................................................................................................

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }
}
