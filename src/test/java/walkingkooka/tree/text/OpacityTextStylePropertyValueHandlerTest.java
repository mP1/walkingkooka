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

public final class OpacityTextStylePropertyValueHandlerTest extends TextStylePropertyValueHandlerTestCase<OpacityTextStylePropertyValueHandler, Opacity> {

    @Test
    public void testFromJsonNode() {
        final Opacity opacity = Opacity.with(0.95);
        this.fromJsonNodeAndCheck(opacity.toJsonNode(), opacity);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.converter(), "Opacity");
    }

    @Override
    OpacityTextStylePropertyValueHandler converter() {
        return OpacityTextStylePropertyValueHandler.INSTANCE;
    }

    @Override
    TextStylePropertyName<Opacity> propertyName() {
        return TextStylePropertyName.OPACITY;
    }

    @Override
    Opacity propertyValue() {
        return Opacity.with(0.25);
    }

    @Override
    String propertyValueType() {
        return Opacity.class.getName();
    }

    @Override
    public String typeNamePrefix() {
        return Opacity.class.getSimpleName();
    }

    @Override
    public Class<OpacityTextStylePropertyValueHandler> type() {
        return OpacityTextStylePropertyValueHandler.class;
    }
}
