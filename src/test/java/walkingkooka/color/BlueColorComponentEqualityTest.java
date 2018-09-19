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

public final class BlueColorComponentEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<BlueColorComponent> {

    private final static byte VALUE = 1;

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(BlueColorComponent.with((byte) 2));
    }

    @Test
    public void testRed() {
        this.checkNotEquals(ColorComponent.red(VALUE));
    }

    @Test
    public void testGreen() {
        this.checkNotEquals(ColorComponent.green(VALUE));
    }

    @Test
    public void testAlpha() {
        this.checkNotEquals(ColorComponent.alpha(VALUE));
    }

    @Override
    protected BlueColorComponent createObject() {
        return BlueColorComponent.with(VALUE);
    }
}
