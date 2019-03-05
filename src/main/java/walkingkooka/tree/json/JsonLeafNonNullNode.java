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

package walkingkooka.tree.json;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Base type for all the leaf json nodes that are not {@link JsonNullNode}
 */
abstract class JsonLeafNonNullNode<V> extends JsonLeafNode<V> {

    JsonLeafNonNullNode(final JsonNodeName name, final int index, final V value){
        super(name, index, value);
    }

    @Override
    <T> T fromJsonNode0(final Class<T> type) {
        return HasJsonNodeMapper.mapperOrFail(type)
                .fromJsonNode(this);
    }

    /**
     * Not an array of elements therefore cant be a list.
     */
    final <T> List<T> fromJsonNodeList0(final Class<T> elementType) {
        return this.reportInvalidNodeObject();
    }

    /**
     * Not an array of elements therefore cant be a set.
     */
    final <T> Set<T> fromJsonNodeSet0(final Class<T> elementType) {
        return this.reportInvalidNodeObject();
    }

    /**
     * Not an array of entries therefore cant be a map.
     */
    final <K, V> Map<K, V> fromJsonNodeMap0(final Class<K> keyType, final Class<V> valueType) {
        return this.reportInvalidNodeObject();
    }

    /**
     * {@see HasJsonNodeMapMapper#fromJsonNodeWithType}
     */
    @Override
    public final <T> T fromJsonNodeWithType() {
        return HasJsonNodeMapMapper.fromJsonNodeWithType(this);
    }

    /**
     * Not a null or object therefore fail.
     */
    @Override
    public final <T> List<T> fromJsonNodeWithTypeList() {
        return this.reportInvalidNodeArray();
    }

    /**
     * Not a null or object therefore fail.
     */
    public <T> Set<T> fromJsonNodeWithTypeSet() {
        return this.reportInvalidNodeArray();
    }

    /**
     * Not a null or object therefore fail.
     */
    public <K, V> Map<K, V> fromJsonNodeWithTypeMap() {
        return this.reportInvalidNodeArray();
    }

    // isXXX..................................................................................................

    @Override
    public final boolean isNull() {
        return false;
    }

    // HasText......................................................................................................

    @Override
    public String text() {
        return String.valueOf(this.value());
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
