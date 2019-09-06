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
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class FakeToJsonNodeContext extends FakeJsonNodeContext implements ToJsonNodeContext {

    @Override
    public ToJsonNodeContext setObjectPostProcessor(final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNode(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNodeWithType(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNodeList(final List<?> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNodeSet(final Set<?> set) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNodeMap(final Map<?, ?> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNodeWithTypeList(final List<?> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNodeWithTypeSet(final Set<?> set) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode toJsonNodeWithTypeMap(final Map<?, ?> map) {
        throw new UnsupportedOperationException();
    }
}
