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
import walkingkooka.test.SerializationTesting;

public final class AlphaColorComponentTest extends ColorComponentTestCase<AlphaColorComponent>
        implements SerializationTesting<AlphaColorComponent> {

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(AlphaColorComponent.with(VALUE2));
    }

    @Test
    public void testEqualsDifferentRed() {
        this.checkNotEquals(ColorComponent.red(VALUE));
    }

    @Test
    public void testEqualsDifferentGreen() {
        this.checkNotEquals(ColorComponent.green(VALUE));
    }

    @Test
    public void testEqualsDifferentBlue() {
        this.checkNotEquals(ColorComponent.blue(VALUE));
    }

    @Override
    AlphaColorComponent createColorComponent(final byte value) {
        return AlphaColorComponent.with(value);
    }

    @Override
    public Class<AlphaColorComponent> type() {
        return AlphaColorComponent.class;
    }

    @Override
    public AlphaColorComponent serializableInstance() {
        return AlphaColorComponent.with((byte) 123);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
