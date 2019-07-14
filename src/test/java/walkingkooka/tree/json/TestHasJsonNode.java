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

import java.util.Objects;

final class TestHasJsonNode implements HasJsonNode {

    static TestHasJsonNode with(final String value) {
        Objects.requireNonNull(value, "value");
        return new TestHasJsonNode(value);
    }

    private TestHasJsonNode(final String value) {
        super();
        this.value = value;
    }

    private final String value;

    // HasJsonNode.......................................................................................................

    /**
     * Accepts a json string creating a {@link TestHasJsonNode}
     */
    static TestHasJsonNode fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return with(node.stringValueOrFail());
        } catch (final JsonNodeException cause) {
            throw new FromJsonNodeException(cause.getMessage(), node, cause);
        }
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.value);
    }

    static {
        HasJsonNode.register("test-HasJsonNode", TestHasJsonNode::fromJsonNode, TestHasJsonNode.class);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof TestHasJsonNode && other.toString().equals(this.value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
