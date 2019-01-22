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

import walkingkooka.collect.map.Maps;

import java.util.AbstractList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An immutable {@link List} view of elements belonging to a {@link JsonObjectNode}.
 */
final class JsonObjectNodeList extends AbstractList<JsonNode> {

    /**
     * Empty list constant.
     */
    static final JsonObjectNodeList EMPTY = new JsonObjectNodeList(Maps.empty());

    /**
     * Factory only used by {@link JsonObjectNode}
     */
    static JsonObjectNodeList with(final Map<JsonNodeName, JsonNode> nameToValues) {
        return new JsonObjectNodeList(nameToValues);
    }

    /**
     * Private ctor use factory.
     */
    private JsonObjectNodeList(final Map<JsonNodeName, JsonNode> nameToValues) {
        super();
        this.nameToValues = nameToValues;
    }

    @Override
    public JsonNode get(int index) {
        return this.list().get(index);
    }

    @Override
    public int size() {
        return this.nameToValues.size();
    }

    @Override
    public String toString() {
        return "[[[MAP=" + this.nameToValues.toString() + " LIST=" + this.list() + "]]]";
    }

    final Map<JsonNodeName, JsonNode> nameToValues;

    /**
     * Lazily loaded list view of json object properties.
     */
    private List<JsonNode> list() {
        if(null==this.list) {
            this.list = this.nameToValues.values().stream().collect(Collectors.toList());
        }
        return this.list;
    }

    private List<JsonNode> list;
}
