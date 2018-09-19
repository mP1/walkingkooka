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

public final class AlphaColorEqualityTest extends ColorEqualityTestCase<AlphaColor> {

    private final static AlphaColorComponent ALPHA = AlphaColorComponent.with((byte) 4);

    @Test
    public void testDifferentAlpha() {
        final Color color = this.createObject();
        this.checkNotEquals(AlphaColor.with(color.red(), color.green(), color.blue(), AlphaColorComponent.with((byte) 0xff)));
    }

    @Override
    AlphaColor createColor(final RedColorComponent red, final GreenColorComponent green, final BlueColorComponent blue) {
        return AlphaColor.with(red, green, blue, ALPHA);
    }
}
