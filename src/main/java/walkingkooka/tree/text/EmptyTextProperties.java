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

package walkingkooka.tree.text;

import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;

import java.util.Map;

/**
 * A {@link TextProperties} with no properties and values.
 */
final class EmptyTextProperties extends TextProperties {

    /**
     * Singleton
     */
    final static EmptyTextProperties INSTANCE = new EmptyTextProperties();

    /**
     * Private ctor
     */
    private EmptyTextProperties() {
        super();
    }

    @Override
    public Map<TextPropertyName<?>, Object> value() {
        return Maps.empty();
    }

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    final boolean canBeEquals(final Object other) {
        return other instanceof EmptyTextProperties;
    }

    @Override
    boolean equals0(final TextProperties other) {
        return true; // singleton
    }

    @Override
    public String toString() {
        return "{}";
    }

    // HasJsonNode......................................................................................................

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.object();
    }
}
