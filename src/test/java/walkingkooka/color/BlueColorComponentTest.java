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

public final class BlueColorComponentTest extends ColorComponentTestCase<BlueColorComponent> {

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(BlueColorComponent.with(VALUE2));
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
    BlueColorComponent createColorComponent(final byte value) {
        return BlueColorComponent.with(value);
    }

    @Override
    public Class<BlueColorComponent> type() {
        return BlueColorComponent.class;
    }

    @Override
    public BlueColorComponent serializableInstance() {
        return BlueColorComponent.with((byte) 123);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
