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

public final class FontFamilyNameTextPropertyValueConverterTest extends TextPropertyValueConverterTestCase<FontFamilyNameTextPropertyValueConverter, FontFamilyName> {

    @Test
    public void testFromJsonNode() {
        final FontFamilyName fontFamilyName = FontFamilyName.with("Antiqua");
        this.fromJsonNodeAndCheck(fontFamilyName.toJsonNode(), fontFamilyName);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.converter(), "FontFamilyName");
    }

    @Override
    FontFamilyNameTextPropertyValueConverter converter() {
        return FontFamilyNameTextPropertyValueConverter.INSTANCE;
    }

    @Override
    TextPropertyName<FontFamilyName> propertyName() {
        return TextPropertyName.FONT_FAMILY_NAME;
    }

    @Override
    FontFamilyName propertyValue() {
        return FontFamilyName.with("Antiqua");
    }

    @Override
    String propertyValueType() {
        return FontFamilyName.class.getName();
    }

    @Override
    public String typeNamePrefix() {
        return FontFamilyName.class.getSimpleName();
    }

    @Override
    public Class<FontFamilyNameTextPropertyValueConverter> type() {
        return FontFamilyNameTextPropertyValueConverter.class;
    }
}
