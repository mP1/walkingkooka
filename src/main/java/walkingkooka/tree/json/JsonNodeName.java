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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.search.SearchNodeName;

/**
 * The name of any property of object key.
 */
public final class JsonNodeName implements Name, Comparable<JsonNodeName>, HashCodeEqualsDefined {

    private final static int INDEX_CACHE_SIZE = 128;

    /**
     * Creates a {@link JsonNodeName} from the index.
     */
    static JsonNodeName index(final int index) {
        if(index < 0){
            throw new IllegalArgumentException("Index " + index + " must not be negative");
        }
        return index < INDEX_CACHE_SIZE ? INDEX_CACHE[index] : new JsonNodeName(index);
    }

    private final static JsonNodeName[] INDEX_CACHE = fillIndexCache();

    private static JsonNodeName[] fillIndexCache() {
        final JsonNodeName[] cache = new JsonNodeName[INDEX_CACHE_SIZE];
        for(int i = 0; i < INDEX_CACHE_SIZE; i++){
            cache[i] = new JsonNodeName(i);
        }
        return cache;
    }

    /**
     * Factory that creates a new {@link JsonNodeName}
     */
    public static JsonNodeName with(final String name) {
        CharSequences.failIfNullOrEmpty(name, "name");

        return new JsonNodeName(name);
    }

    static JsonNodeName fromClass(final Class<? extends JsonNode> klass) {
        final String name = klass.getSimpleName();
        return new JsonNodeName(name.substring("Json".length(), name.length() - Name.class.getSimpleName().length()));
    }

    JsonNodeName(final int index) {
        this(String.valueOf(index));
    }

    JsonNodeName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Creates the {@link SearchNodeName} for this node name. Only used by {@link JsonObjectNode#toSearchNode()}.
     */
    final SearchNodeName toSearchNodeName() {
        return SearchNodeName.with(this.name);
    }

    // Object..................................................................................................

    public final int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof JsonNodeName &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final JsonNodeName other) {
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final JsonNodeName other) {
        return this.name.compareTo(other.name);
    }
}
