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
import walkingkooka.tree.Node;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.pointer.NodePointer;

import java.util.function.Function;

final class AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor extends NodePatchFromJsonObjectNodePropertyVisitor {

    static AddNodePatch<?, ?> add(final JsonObjectNode patch,
                                  final NodePatchFromJsonFormat format) {
        final AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor visitor = new AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor(patch, format);
        visitor.accept(patch);
        return AddNodePatch.with(visitor.path(), Cast.to(visitor.value()));
    }

    static ReplaceNodePatch<?, ?> replace(final JsonObjectNode patch,
                                          final NodePatchFromJsonFormat format) {
        final AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor visitor = new AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor(patch, format);
        visitor.accept(patch);
        return ReplaceNodePatch.with(visitor.path(), Cast.to(format.valueOrFail(visitor)));
    }

    static TestNodePatch<?, ?> test(final JsonObjectNode patch,
                                    final NodePatchFromJsonFormat format) {
        final AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor visitor = new AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor(patch, format);
        visitor.accept(patch);
        return TestNodePatch.with(visitor.path(), Cast.to(format.valueOrFail(visitor)));
    }

    AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor(final JsonObjectNode patch,
                                                               final NodePatchFromJsonFormat format) {
        super(patch, format);
    }

    @Override
    void visitFrom(final String from) {
        this.unknownPropertyPresent(NodePatch.FROM_PROPERTY);
    }

    // VALUE TYPE .............................................................................................

    void visitValueType(final JsonNode valueType) {
    }

    // VALUE ........................................................................................................

    void visitValue(final JsonNode value) {
        this.value = value;
    }

    Node<?, ?, ?, ?> value() {
        return this.format.valueOrFail(this);
    }

    /**
     * Once all properties are visited this will be converted into a {@link NodePointer}
     */
    JsonNode value;

    /**
     * Returns a factory that uses the {@link NodePatch#VALUE_TYPE_PROPERTY} when creating values from json.
     */
    final Function<JsonNode, Node<?, ?, ?, ?>> valueFactory() {
        if(null == this.valueFactory) {
            this.valueFactory = Cast.to(NodePatch.VALUE_TYPE_PROPERTY.fromJsonNodeWithTypeFactory(this.patch, Node.class));
        }
        return this.valueFactory;
    }

    private Function<JsonNode, Node<?, ?, ?, ?>> valueFactory;
}
