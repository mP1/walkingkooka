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
import walkingkooka.tree.json.JsonNode;

public final class StringTextStylePropertyValueHandlerTest extends TextStylePropertyValueHandlerTestCase2<StringTextStylePropertyValueHandler, String> {

    @Test
    public void testCheckEmptyStringFails() {
        this.checkFails("", "Property \"text\" contains an empty/whitespace value \"\"");
    }

    @Test
    public void testCheckWhitespace() {
        this.check(" \t");
    }

    @Test
    public void testFromJsonNode() {
        final String value = "abc123";
        this.fromJsonNodeAndCheck(JsonNode.string(value), value);
    }

    @Test
    public void testToJsonNode() {
        final String value = "abc123";
        this.toJsonNodeAndCheck(value, JsonNode.string(value));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), "String");
    }

    @Override
    StringTextStylePropertyValueHandler handler() {
        return StringTextStylePropertyValueHandler.INSTANCE;
    }

    @Override
    TextStylePropertyName<String> propertyName() {
        return TextStylePropertyName.TEXT;
    }

    @Override
    String propertyValue() {
        return "abc123";
    }

    @Override
    String propertyValueType() {
        return String.class.getSimpleName();
    }

    @Override
    public String typeNamePrefix() {
        return String.class.getSimpleName();
    }

    @Override
    public Class<StringTextStylePropertyValueHandler> type() {
        return StringTextStylePropertyValueHandler.class;
    }
}
