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

package walkingkooka.tree.json.marshall;

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.Objects;

public final class TestJsonNodeValue extends TestJsonNodeValueAbstract {

    public final static String TYPE_NAME = "test-JsonNodeValue";

    public static TestJsonNodeValue with(final String value) {
        Objects.requireNonNull(value, "value");

        return new TestJsonNodeValue(value);
    }

    private TestJsonNodeValue(final String value) {
        super();
        this.value = value;
    }

    private final String value;

    // JsonNodeContext...................................................................................................

    /**
     * Accepts a json string creating a {@link TestJsonNodeValue}
     */
    public static TestJsonNodeValue unmarshall(final JsonNode node,
                                                 final JsonNodeUnmarshallContext context) {
        String value = null;
        for (JsonNode child : node.objectOrFail().children()) {
            final JsonNodeName property = child.name();
            if (KEY.equals(property)) {
                value = child.stringValueOrFail();
                continue;
            }
            JsonNodeUnmarshallContext.unknownPropertyPresent(property, node);
        }

        return with(value);
    }

    public JsonObjectNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.object()
                .set(KEY, JsonNode.string(this.value));
    }

    final static JsonNodeName KEY = JsonNodeName.with("string");

    public static void register() {
        remover = JsonNodeContext.register(TYPE_NAME,
                TestJsonNodeValue::unmarshall,
                TestJsonNodeValue::marshall,
                TestJsonNodeValue.class);
    }

    public static void unregister() {
       if(null != remover) {
           remover.run();
           remover = null;
       }
    }

    private static Runnable remover = null;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof TestJsonNodeValue && other.toString().equals(this.value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
