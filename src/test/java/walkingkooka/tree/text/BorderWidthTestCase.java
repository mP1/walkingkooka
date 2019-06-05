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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;

public abstract class BorderWidthTestCase<W extends BorderWidth> extends LengthTextStylePropertyValueTestCase<W> {

    BorderWidthTestCase() {
        super();
    }

    @Test
    public final void testWithNumberFails() {
        this.withFails(Length.number(1));
    }

    @Test
    public final void testWithNormalFails() {
        this.withFails(Length.normal());
    }

    @Test
    public final void testWithPixel() {
        this.withAndCheck(Length.pixel(12.0));
    }

    @Override
    final Length<?> length() {
        return Length.pixel(12.5);
    }

    @Override
    final Length<?> differentLength() {
        return Length.pixel(99.0);
    }
}
