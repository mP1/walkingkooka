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

public final class FontSizeTextPropertyValueHandlerTest extends TextPropertyValueHandlerTestCase<FontSizeTextPropertyValueHandler, FontSize> {

    @Test
    public void testFromJsonNode() {
        final FontSize fontSize = FontSize.with(123);
        this.fromJsonNodeAndCheck(fontSize.toJsonNode(), fontSize);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.converter(), "FontSize");
    }

    @Override
    FontSizeTextPropertyValueHandler converter() {
        return FontSizeTextPropertyValueHandler.INSTANCE;
    }

    @Override
    TextPropertyName<FontSize> propertyName() {
        return TextPropertyName.FONT_SIZE;
    }

    @Override
    FontSize propertyValue() {
        return FontSize.with(12);
    }

    @Override
    String propertyValueType() {
        return FontSize.class.getName();
    }

    @Override
    public String typeNamePrefix() {
        return FontSize.class.getSimpleName();
    }

    @Override
    public Class<FontSizeTextPropertyValueHandler> type() {
        return FontSizeTextPropertyValueHandler.class;
    }
}
