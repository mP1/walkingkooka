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

public final class RedColorComponentTest extends ColorComponentTestCase<RedColorComponent> {

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(ColorComponent.red(VALUE2));
    }

    @Test
    public void testBlue() {
        this.checkNotEquals(ColorComponent.blue(VALUE));
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
    RedColorComponent createColorComponent(final byte value) {
        return RedColorComponent.with(value);
    }

    @Override
    public Class<RedColorComponent> type() {
        return RedColorComponent.class;
    }

    @Override
    public RedColorComponent serializableInstance() {
        return RedColorComponent.with((byte) 123);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
