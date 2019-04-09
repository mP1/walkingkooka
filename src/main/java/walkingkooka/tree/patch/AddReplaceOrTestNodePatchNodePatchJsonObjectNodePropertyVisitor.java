/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.tree.patch;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.pointer.NodePointer;

final class AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor extends NodePatchJsonObjectNodePropertyVisitor {

    static AddNodePatch<?, ?> add(final JsonNode patch) {
        final AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor visitor = new AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor(patch);
        visitor.accept(patch);
        return AddNodePatch.with(Cast.to(visitor.pathOrFail()), Cast.to(visitor.valueOrFail()));
    }

    static ReplaceNodePatch<?, ?> replace(final JsonNode patch) {
        final AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor visitor = new AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor(patch);
        visitor.accept(patch);
        return ReplaceNodePatch.with(Cast.to(visitor.pathOrFail()), Cast.to(visitor.valueOrFail()));
    }

    static TestNodePatch<?, ?> test(final JsonNode patch) {
        final AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor visitor = new AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor(patch);
        visitor.accept(patch);
        return TestNodePatch.with(Cast.to(visitor.pathOrFail()), Cast.to(visitor.valueOrFail()));
    }

    AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor(final JsonNode patch) {
        super(patch);
    }

    @Override
    void visitFrom(final JsonStringNode from) {
        this.unknownPropertyPresent(NodePatch.FROM_PROPERTY);
    }

    // VALUE TYPE .............................................................................................

    final void visitValueType(final JsonNode valueType) {
        this.valueType = typeOrFail(valueType, NodePatch.VALUE_TYPE_PROPERTY);
    }

    private Class<?> valueType;

    final Class<Name> valueTypeOrFail() {
        return Cast.to(propertyOrFail(this.valueType, NodePatch.VALUE_TYPE_PROPERTY));
    }

    // VALUE ........................................................................................................

    final void visitValue(final JsonNode value) {
        this.value = value;
    }

    final Object valueOrFail() {
        return propertyOrFail(this.value, NodePatch.VALUE_PROPERTY).fromJsonNode(this.valueTypeOrFail());
    }

    /**
     * Once all properties are visited this will be converted into a {@link NodePointer}
     */
    private JsonNode value;
}
