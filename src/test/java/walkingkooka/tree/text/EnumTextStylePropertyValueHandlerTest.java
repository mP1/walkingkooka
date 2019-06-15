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
import walkingkooka.tree.json.JsonNode;

public final class EnumTextStylePropertyValueHandlerTest extends TextStylePropertyValueHandlerTestCase2<EnumTextStylePropertyValueHandler<TextWrapping>, TextWrapping> {

    @Test
    public void testFromJsonNode() {
        final TextWrapping textWrapping = TextWrapping.CLIP;
        this.fromJsonNodeAndCheck(JsonNode.string(textWrapping.name()), textWrapping);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), TextWrapping.class.getSimpleName());
    }

    @Override
    EnumTextStylePropertyValueHandler<TextWrapping> handler() {
        return EnumTextStylePropertyValueHandler.with(TextWrapping::valueOf, TextWrapping.class);
    }

    @Override
    TextStylePropertyName<TextWrapping> propertyName() {
        return TextStylePropertyName.TEXT_WRAPPING;
    }

    @Override
    TextWrapping propertyValue() {
        return TextWrapping.CLIP;
    }

    @Override
    String propertyValueType() {
        return TextWrapping.class.getSimpleName();
    }

    @Override
    public String typeNamePrefix() {
        return Enum.class.getSimpleName();
    }

    @Override
    public Class<EnumTextStylePropertyValueHandler<TextWrapping>> type() {
        return Cast.to(EnumTextStylePropertyValueHandler.class);
    }
}
