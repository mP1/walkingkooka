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
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class ColorHslOrHsvTestCase<T extends ColorHslOrHsv> implements ClassTesting2<T>,
        HashCodeEqualsDefinedTesting<T>,
        HasJsonNodeTesting<T>,
        SerializationTesting<T>,
        ToStringTesting<T> {

    ColorHslOrHsvTestCase() {
        super();
    }

    @Test
    public final void testIsColor() {
        final T colorHslOrHsv = this.createColorHslOrHsv();
        assertEquals(colorHslOrHsv instanceof Color,
                colorHslOrHsv.isColor(),
                () -> "isColor " + colorHslOrHsv);
    }

    @Test
    public final void testIsHsl() {
        final T colorHslOrHsv = this.createColorHslOrHsv();
        assertEquals(colorHslOrHsv instanceof Hsl,
                colorHslOrHsv.isHsl(),
                () -> "isHsl " + colorHslOrHsv);
    }

    @Test
    public final void testIsHsv() {
        final T colorHslOrHsv = this.createColorHslOrHsv();
        assertEquals(colorHslOrHsv instanceof Hsv,
                colorHslOrHsv.isHsv(),
                () -> "isHsv " + colorHslOrHsv);
    }

    @Test
    public final void testToColor() {
        final T colorHslOrHsv = this.createColorHslOrHsv();
        final Color color = colorHslOrHsv.toColor();

        if (colorHslOrHsv instanceof Color) {
            assertSame(colorHslOrHsv,
                    color,
                    () -> colorHslOrHsv + " toColor()");
        } else {
            assertNotSame(colorHslOrHsv,
                    color,
                    () -> colorHslOrHsv + " toColor()");
        }
    }

    @Test
    public final void testToHsl() {
        final T colorHslOrHsv = this.createColorHslOrHsv();
        final Hsl hsl = colorHslOrHsv.toHsl();

        if (colorHslOrHsv instanceof Hsl) {
            assertSame(colorHslOrHsv,
                    hsl,
                    () -> colorHslOrHsv + " toHsl()");
        } else {
            assertNotSame(colorHslOrHsv,
                    hsl,
                    () -> colorHslOrHsv + " toHsl()");
        }
    }

    @Test
    public final void testToHsv() {
        final T colorHslOrHsv = this.createColorHslOrHsv();
        final Hsv hsv = colorHslOrHsv.toHsv();

        if (colorHslOrHsv instanceof Hsv) {
            assertSame(colorHslOrHsv,
                    hsv,
                    () -> colorHslOrHsv + " toHsv()");
        } else {
            assertNotSame(colorHslOrHsv,
                    hsv,
                    () -> colorHslOrHsv + " toHsv()");
        }
    }

    abstract T createColorHslOrHsv();

    @Override
    public final T createObject() {
        return this.createColorHslOrHsv();
    }

    // HasJsonNodeTesting..............................................................................................

    @Override
    public final T createHasJsonNode() {
        return this.createColorHslOrHsv();
    }
}
