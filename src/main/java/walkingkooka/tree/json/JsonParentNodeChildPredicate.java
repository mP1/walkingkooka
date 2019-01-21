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

import java.util.function.BiPredicate;

/**
 * A {@link BiPredicate} used by {@link JsonParentNode} to compare values after fetching nodes by name.
 */
final class JsonParentNodeChildPredicate implements BiPredicate<JsonNode, JsonNode> {

    /**
     * Singleton
     */
    final static JsonParentNodeChildPredicate INSTANCE = new JsonParentNodeChildPredicate();

    /**
     * Private ctor use singleton.
     */
    private JsonParentNodeChildPredicate() {
        super();
    }

    /**
     * Tests for equality ignoring the name as this assumes the name fetched the child.
     */
    @Override
    public boolean test(final JsonNode first, final JsonNode other) {
        return null != first &&
                first.equalsValue(other) &&
                first.equalsDescendants(other);
    }
}
