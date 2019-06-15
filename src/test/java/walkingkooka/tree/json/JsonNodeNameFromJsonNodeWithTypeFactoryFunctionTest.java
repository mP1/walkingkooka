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

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.test.ToStringTesting;
import walkingkooka.util.FunctionTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonNodeNameFromJsonNodeWithTypeFactoryFunctionTest implements FunctionTesting<JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress>,
        JsonNode,
        EmailAddress>,
        ToStringTesting<JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress>> {

    @Test
    public void testWithNullSourceFails() {
        assertThrows(NullPointerException.class, () -> {
            JsonNodeNameFromJsonNodeWithTypeFactoryFunction.with(this.typeNameProperty(), null, this.valueType());
        });
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            JsonNodeNameFromJsonNodeWithTypeFactoryFunction.with(this.typeNameProperty(), JsonNode.object(), null);
        });
    }

    @Test
    public void testWith() {
        final JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress> function = this.createFunction();
        assertEquals(null, function.mapper, "mapper");
    }

    @Test
    public void testApply() {
        final EmailAddress email = this.value();
        final JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress> function = this.createFunction();

        this.applyAndCheck(function,
                email.toJsonNode(),
                email);

        assertNotEquals(null, function.mapper, "mapper");
    }

    @Test
    public void testApplyTwice() {
        final EmailAddress email1 = this.value();
        final JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress> function = this.createFunction();

        this.applyAndCheck(function,
                email1.toJsonNode(),
                email1);

        final EmailAddress email2 = EmailAddress.parse("user2@example.com");
        this.applyAndCheck(function,
                email2.toJsonNode(),
                email2);
    }

    @Test
    public void testApplyTypeMissingFromSource() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            this.createFunction(JsonNode.object())
                    .apply(this.value().toJsonNode());
        });
        assertEquals("Unknown property typeNameProperty1={}", thrown.getMessage(), "message");
    }

    @Test
    public void testToString() {
        final JsonObjectNode object = this.objectWithType(EmailAddress.class);
        this.toStringAndCheck(this.createFunction(object), this.typeNameProperty() + " in " + object);
    }

    @Override
    public JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress> createFunction() {
        return this.createFunction(this.objectWithType(EmailAddress.class));
    }

    public JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress> createFunction(final JsonObjectNode objectWithType) {
        final Class<EmailAddress> type = this.valueType();

        return Cast.to(this.typeNameProperty()
                .fromJsonNodeWithTypeFactory(objectWithType, type));
    }


    private JsonNodeName typeNameProperty() {
        return JsonNodeName.with("typeNameProperty1");
    }

    private JsonObjectNode objectWithType(final Class<?> type) {
        return JsonNode.object()
                .set(typeNameProperty(), HasJsonNode.typeName(type).get());
    }

    private EmailAddress value() {
        return EmailAddress.parse("user@example.com");
    }

    private Class<EmailAddress> valueType() {
        return EmailAddress.class;
    }

    @Override
    public Class<JsonNodeNameFromJsonNodeWithTypeFactoryFunction<EmailAddress>> type() {
        return Cast.to(JsonNodeNameFromJsonNodeWithTypeFactoryFunction.class);
    }
}
