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

public final class HsvEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<Hsv> {

    // constants

    private final static HueHsvComponent HUE = HueHsvComponent.with(0);
    private final static SaturationHsvComponent SATURATION = SaturationHsvComponent.with(0);
    private final static ValueHsvComponent VALUE = ValueHsvComponent.with(0);

    // tests

    @Test
    public void testDifferentHue() {
        this.checkNotEquals(Hsv.with(HueHsvComponent.with(180), SATURATION, VALUE));
    }

    @Test
    public void testDifferentSaturation() {
        this.checkNotEquals(Hsv.with(HUE, SaturationHsvComponent.with(0.5f), VALUE));
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(Hsv.with(HUE, SATURATION, ValueHsvComponent.with(0.5f)));
    }

    @Override
    protected Hsv createObject() {
        return Hsv.with(HUE, SATURATION, VALUE);
    }

}
