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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

abstract public class ColorEqualityTestCase<C extends Color> extends HashCodeEqualsDefinedEqualityTestCase<C> {

    ColorEqualityTestCase() {
        super();
    }

    // constants

    final static RedColorComponent RED = RedColorComponent.with((byte) 1);
    final static GreenColorComponent GREEN = GreenColorComponent.with((byte) 2);
    final static BlueColorComponent BLUE = BlueColorComponent.with((byte) 3);

    // tests

    @Test
    public void testDifferentRed() {
        this.checkNotEquals(
                this.createColor(RedColorComponent.with((byte) 0xff), GREEN,
                        BLUE));
    }

    @Test
    public void testDifferentGreen() {
        this.checkNotEquals(this.createColor(RED, GreenColorComponent.with((byte) 0xff), BLUE));
    }

    @Test
    public void testDifferentBlue() {
        this.checkNotEquals(this.createColor(RED, GREEN, BlueColorComponent.with((byte) 0xff)));
    }

    @Override
    protected C createObject() {
        return this.createColor(RED, GREEN, BLUE);
    }

    abstract C createColor(RedColorComponent red, GreenColorComponent green, BlueColorComponent blue);
}
