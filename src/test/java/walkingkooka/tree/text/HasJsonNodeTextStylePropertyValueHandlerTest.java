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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.tree.json.HasJsonNode;

public final class HasJsonNodeTextStylePropertyValueHandlerTest extends TextStylePropertyValueHandlerTestCase2<HasJsonNodeTextStylePropertyValueHandler<Color>, Color> {

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
        this.toStringAndCheck(this.handler(), "Color");
    }

    @Override
    HasJsonNodeTextStylePropertyValueHandler<Color> handler() {
        return HasJsonNodeTextStylePropertyValueHandler.with(Color.class, Color::fromJsonNodeColor);
    }

    @Override
    TextStylePropertyName<Color> propertyName() {
        return TextStylePropertyName.BACKGROUND_COLOR;
    }

    @Override
    Color propertyValue() {
        return Color.BLACK;
    }

    @Override
    String propertyValueType() {
        return Color.class.getSimpleName();
    }

    @Override
    public String typeNamePrefix() {
        return HasJsonNode.class.getSimpleName();
    }

    @Override
    public Class<HasJsonNodeTextStylePropertyValueHandler<Color>> type() {
        return Cast.to(HasJsonNodeTextStylePropertyValueHandler.class);
    }
}
