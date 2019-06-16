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
import walkingkooka.net.email.EmailAddress;
import walkingkooka.tree.json.HasJsonNode;

public final class HasJsonNodeWithTypeTextStylePropertyValueHandlerTest extends TextStylePropertyValueHandlerTestCase<HasJsonNodeWithTypeTextStylePropertyValueHandler, Object> {

    @Test
    public void testCheckWrongValueTypeFails() {
        this.checkFails(this, "Property " + this.propertyName().inQuotes() + " value " + this + " is not a supported type");
    }

    @Test
    public void testFromJsonNode() {
        final Color color = Color.fromRgb(0x123456);
        this.fromJsonNodeAndCheck(color.toJsonNodeWithType(), color);
    }

    @Test
    public void testToJsonNode() {
        final Color color = Color.fromRgb(0x123456);
        this.toJsonNodeAndCheck(color, color.toJsonNodeWithType());
    }

    @Test
    public void testToJsonNodeRgba() {
        final EmailAddress emailAddress = EmailAddress.parse("user@example.com");
        this.toJsonNodeAndCheck(emailAddress, emailAddress.toJsonNodeWithType());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), "HasJsonNodeWithType");
    }

    @Override
    HasJsonNodeWithTypeTextStylePropertyValueHandler handler() {
        return HasJsonNodeWithTypeTextStylePropertyValueHandler.INSTANCE;
    }

    @Override
    TextStylePropertyName<Object> propertyName() {
        return Cast.to(TextStylePropertyName.with("unknown"));
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
        return HasJsonNode.class.getSimpleName();
    }

    @Override
    public Class<HasJsonNodeWithTypeTextStylePropertyValueHandler> type() {
        return HasJsonNodeWithTypeTextStylePropertyValueHandler.class;
    }
}
