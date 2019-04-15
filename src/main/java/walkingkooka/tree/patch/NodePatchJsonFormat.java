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

import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Optional;

enum NodePatchJsonFormat {
    JSON {
        @Override
        JsonObjectNode setPathComponentType(final JsonObjectNode object,
                                            final NodePointer<?, ?> path) {
            return object;
        }

        @Override
        JsonObjectNode setPathComponentType(final JsonObjectNode object,
                                            final NodePointer<?, ?> from,
                                            final NodePointer<?, ?> path) {
            return object;
        }

        @Override
        JsonObjectNode setValueType(final JsonObjectNode object,
                                    final Object value) {
            return object;
        }
    },
    NODE_PATCH {
        @Override
        JsonObjectNode setPathComponentType(final JsonObjectNode object,
                                            final NodePointer<?, ?> path) {
            return this.setPathComponentType0(object,
                    NodePatchJsonFormatNodePointerVisitor.pathNameType(path));
        }

        @Override
        JsonObjectNode setPathComponentType(final JsonObjectNode object,
                                            final NodePointer<?, ?> from,
                                            final NodePointer<?, ?> path) {
            Optional<JsonStringNode> type = NodePatchJsonFormatNodePointerVisitor.pathNameType(from);
            if (!type.isPresent()) {
                type = NodePatchJsonFormatNodePointerVisitor.pathNameType(path);
            }
            return this.setPathComponentType0(object, type);
        }

        /**
         * Adds the path component type properites if necessary to the given object.
         */
        private JsonObjectNode setPathComponentType0(final JsonObjectNode object,
                                                     final Optional<JsonStringNode> pathComponentType) {
            return pathComponentType
                    .map(t -> object.set(NodePatch.PATH_NAME_TYPE_PROPERTY, t))
                    .orElse(object);
        }

        @Override
        JsonObjectNode setValueType(final JsonObjectNode object,
                                    final Object value) {
            return object.set(NodePatch.VALUE_TYPE_PROPERTY,
                    typeOrFail(value));
        }

        /**
         * Accepts a value such as a path or value and returns a {@link JsonStringNode} with the type name.
         */
        private JsonStringNode typeOrFail(final Object value) {
            return HasJsonNode.typeName(value.getClass())
                    .orElseThrow(() -> new IllegalArgumentException("Type not registered as supporting json: " + value));
        }
    };

    abstract JsonObjectNode setPathComponentType(final JsonObjectNode object,
                                                 final NodePointer<?, ?> path);

    abstract JsonObjectNode setPathComponentType(final JsonObjectNode object,
                                                 final NodePointer<?, ?> from,
                                                 final NodePointer<?, ?> path);

    abstract JsonObjectNode setValueType(final JsonObjectNode object,
                                         final Object value);
}
