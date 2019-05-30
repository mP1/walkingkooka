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

public final class FontWeightTextPropertyValueHandlerTest extends TextPropertyValueHandlerTestCase<FontWeightTextPropertyValueHandler, FontWeight> {

    @Test
    public void testFromJsonNode() {
        final FontWeight fontWeight = FontWeight.with(123);
        this.fromJsonNodeAndCheck(fontWeight.toJsonNode(), fontWeight);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.converter(), "FontWeight");
    }

    @Override
    FontWeightTextPropertyValueHandler converter() {
        return FontWeightTextPropertyValueHandler.INSTANCE;
    }

    @Override
    TextPropertyName<FontWeight> propertyName() {
        return TextPropertyName.FONT_WEIGHT;
    }

    @Override
    FontWeight propertyValue() {
        return FontWeight.with(400);
    }

    @Override
    String propertyValueType() {
        return FontWeight.class.getName();
    }

    @Override
    public String typeNamePrefix() {
        return FontWeight.class.getSimpleName();
    }

    @Override
    public Class<FontWeightTextPropertyValueHandler> type() {
        return FontWeightTextPropertyValueHandler.class;
    }
}
