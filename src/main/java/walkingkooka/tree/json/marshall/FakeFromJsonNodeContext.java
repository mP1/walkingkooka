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

package walkingkooka.tree.json.marshall;

import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A fake {@link FromJsonNodeContext}
 */
public class FakeFromJsonNodeContext extends FakeJsonNodeContext implements FromJsonNodeContext {
    @Override
    public <T> T fromJsonNode(final JsonNode node,
                              final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<T> fromJsonNodeList(final JsonNode node,
                                        final Class<T> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<T> fromJsonNodeSet(final JsonNode node,
                                      final Class<T> elementType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> Map<K, V> fromJsonNodeMap(final JsonNode node,
                                            final Class<K> keyType,
                                            final Class<V> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T fromJsonNodeWithType(final JsonNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<T> fromJsonNodeWithTypeList(final JsonNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<T> fromJsonNodeWithTypeSet(final JsonNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> Map<K, V> fromJsonNodeWithTypeMap(final JsonNode node) {
        throw new UnsupportedOperationException();
    }
}
