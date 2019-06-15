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

public final class ValueHsvComponentTest extends HsvComponentTestCase<ValueHsvComponent> {

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(ValueHsvComponent.with(0));
    }

    @Test
    public void testToString0() {
        this.toStringAndCheck(ValueHsvComponent.with(0), "0%");
    }

    @Test
    public void testToStringHalf() {
        this.toStringAndCheck(ValueHsvComponent.with(0.5f), "50%");
    }

    @Test
    public void testToStringOne() {
        this.toStringAndCheck(ValueHsvComponent.with(1.0f), "100%");
    }
    
    @Override
    ValueHsvComponent createHsvComponent(final float value) {
        return ValueHsvComponent.with(value);
    }

    @Override
    float value() {
        return 0.75f;
    }

    @Override
    float min() {
        return ValueHsvComponent.MIN;
    }

    @Override
    float max() {
        return ValueHsvComponent.MAX;
    }

    @Override
    public Class<ValueHsvComponent> type() {
        return ValueHsvComponent.class;
    }

    @Override
    public ValueHsvComponent serializableInstance() {
        return ValueHsvComponent.with(0.5f);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
