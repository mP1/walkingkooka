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

package walkingkooka.tree.patch;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.function.Function;

/**
 * Creates {@link NodePatch} taking the name and value types from properties.
 */
final class HasJsonNodeNodePatchFromJsonFormat extends NodePatchFromJsonFormat {

    static final HasJsonNodeNodePatchFromJsonFormat INSTANCE = new HasJsonNodeNodePatchFromJsonFormat();

    private HasJsonNodeNodePatchFromJsonFormat() {
        super();
    }

    @Override
    void accept(final NodePatchFromJsonObjectNodePropertyVisitor visitor,
                final JsonObjectNode node) {
        visitor.acceptNodePatch(node);
    }

    @Override
    Function<String, Name> nameFactory(final NodePatchFromJsonObjectNodePropertyVisitor visitor) {
        return (string) -> visitor.pathNameFactory().apply(JsonNode.string(string));
    }

    @Override
    Node<?, ?, ?, ?> valueOrFail(final AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor visitor) {
        return Cast.to(visitor.valueFactory().apply(visitor.propertyOrFail(visitor.value, NodePatch.VALUE_PROPERTY)));
    }

    @Override
    public String toString() {
        return HasJsonNode.class.getSimpleName();
    }
}
