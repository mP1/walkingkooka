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
import walkingkooka.color.Color;

public final class ColorTextPropertyValueConverterTest extends TextPropertyValueConverterTestCase<ColorTextPropertyValueConverter, Color> {

    @Test
    public void testFromJsonNode() {
        final Color color = Color.fromRgb(0x123456);
        this.fromJsonNodeAndCheck(color.toJsonNode(), color);
    }

    @Test
    public void testFromJsonNodeRgba() {
        final Color color = Color.fromArgb(0x12345678);
        this.fromJsonNodeAndCheck(color.toJsonNode(), color);
    }

    @Test
    public void testToJsonNode() {
        final Color color = Color.fromRgb(0x123456);
        this.toJsonNodeAndCheck(color, color.toJsonNode());
    }

    @Test
    public void testToJsonNodeRgba() {
        final Color color = Color.fromArgb(0x12345678);
        this.toJsonNodeAndCheck(color, color.toJsonNode());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.converter(), "Color");
    }

    @Override
    ColorTextPropertyValueConverter converter() {
        return ColorTextPropertyValueConverter.INSTANCE;
    }

    @Override
    TextPropertyName<Color> propertyName() {
        return TextPropertyName.BACKGROUND_COLOR;
    }

    @Override
    Color propertyValue() {
        return Color.BLACK;
    }

    @Override
    String propertyValueType() {
        return Color.class.getName();
    }

    @Override
    public String typeNamePrefix() {
        return Color.class.getSimpleName();
    }

    @Override
    public Class<ColorTextPropertyValueConverter> type() {
        return ColorTextPropertyValueConverter.class;
    }
}
