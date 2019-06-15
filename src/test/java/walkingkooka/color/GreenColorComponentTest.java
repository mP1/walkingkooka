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

public final class GreenColorComponentTest extends ColorComponentTestCase<GreenColorComponent> {

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(ColorComponent.green(VALUE2));
    }

    @Test
    public void testRed() {
        this.checkNotEquals(ColorComponent.red(VALUE));
    }

    @Test
    public void testBlue() {
        this.checkNotEquals(ColorComponent.blue(VALUE));
    }

    @Test
    public void testAlpha() {
        this.checkNotEquals(ColorComponent.alpha(VALUE));
    }

    @Override
    GreenColorComponent createColorComponent(final byte value) {
        return GreenColorComponent.with(value);
    }

    @Override
    public Class<GreenColorComponent> type() {
        return GreenColorComponent.class;
    }

    @Override
    public GreenColorComponent serializableInstance() {
        return GreenColorComponent.with((byte) 123);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
