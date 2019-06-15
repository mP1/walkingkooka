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

public final class HueHsvComponentTest extends HsvComponentTestCase<HueHsvComponent> {

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(HueHsvComponent.with(0));
    }

    @Test
    public void testToString0() {
        this.toStringAndCheck(HueHsvComponent.with(0), "0");
    }

    @Test
    public void testToStringTwoHalf() {
        this.toStringAndCheck(HueHsvComponent.with(2.5f), "3");
    }

    @Test
    public void testToString359() {
        this.toStringAndCheck(HueHsvComponent.with(359), "359");
    }

    @Override
    HueHsvComponent createHsvComponent(final float value) {
        return HueHsvComponent.with(value);
    }

    @Override
    float value() {
        return 359;
    }

    @Override
    float min() {
        return HueHsvComponent.MIN;
    }

    @Override
    float max() {
        return HueHsvComponent.MAX;
    }

    @Override
    public Class<HueHsvComponent> type() {
        return HueHsvComponent.class;
    }

    @Override
    public HueHsvComponent serializableInstance() {
        return HueHsvComponent.with(180);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
