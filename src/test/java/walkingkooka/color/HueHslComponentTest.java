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

public final class HueHslComponentTest extends HslComponentTestCase<HueHslComponent> {

    @Override
    HueHslComponent createHslComponent(final float value) {
        return HueHslComponent.with(value);
    }

    @Override
    float min() {
        return HueHslComponent.MIN;
    }

    @Override
    float max() {
        return HueHslComponent.MAX;
    }

    @Override
    public Class<HueHslComponent> type() {
        return HueHslComponent.class;
    }

    @Override
    public HueHslComponent serializableInstance() {
        return HueHslComponent.with(180);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
