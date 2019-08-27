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

package walkingkooka.tree.json.map;

import walkingkooka.tree.json.JsonNode;

import java.util.Objects;

public final class TestJsonNodeMap extends TestJsonNodeMapAbstract {

    public final static String TYPE_NAME = "test-JsonNodeMap";

    public static TestJsonNodeMap with(final String value) {
        Objects.requireNonNull(value, "value");

        return new TestJsonNodeMap(value);
    }

    private TestJsonNodeMap(final String value) {
        super();
        this.value = value;
    }

    private final String value;

    // JsonNodeContext...................................................................................................

    /**
     * Accepts a json string creating a {@link TestJsonNodeMap}
     */
    public static TestJsonNodeMap fromJsonNode(final JsonNode node,
                                               final FromJsonNodeContext context) {
        return with(node.stringValueOrFail());
    }

    public JsonNode toJsonNode(final ToJsonNodeContext context) {
        return JsonNode.string(this.value);
    }

    public static void register() {
        remover = JsonNodeContext.register(TYPE_NAME,
                TestJsonNodeMap::fromJsonNode,
                TestJsonNodeMap::toJsonNode,
                TestJsonNodeMap.class);
        System.out.println("remover:"+ remover);
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
        return this == other || other instanceof TestJsonNodeMap && other.toString().equals(this.value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}