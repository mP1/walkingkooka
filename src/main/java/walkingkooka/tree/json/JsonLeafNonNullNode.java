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

/**
 * Base type for all the leaf json nodes that are not {@link JsonNullNode}
 */
abstract class JsonLeafNonNullNode<V> extends JsonLeafNode<V> {

    JsonLeafNonNullNode(final JsonNodeName name, final int index, final V value) {
        super(name, index, value);
    }

    // isXXX..................................................................................................

    @Override
    public final boolean isNull() {
        return false;
    }

    // Object......................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    final boolean equalsDescendants(final JsonNode other) {
        return true;
    }

    @Override
    final boolean equalsValue(final JsonNode other) {
        return Objects.equals(this.value, JsonLeafNonNullNode.class.cast(other).value);
    }
}
