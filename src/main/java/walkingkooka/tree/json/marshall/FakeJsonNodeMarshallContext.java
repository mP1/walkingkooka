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

public class FakeJsonNodeMarshallContext extends FakeJsonNodeContext implements JsonNodeMarshallContext {

    @Override
    public JsonNodeMarshallContext setObjectPostProcessor(final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshall(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshallWithType(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshallList(final List<?> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshallSet(final Set<?> set) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshallMap(final Map<?, ?> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshallWithTypeList(final List<?> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshallWithTypeSet(final Set<?> set) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode marshallWithTypeMap(final Map<?, ?> map) {
        throw new UnsupportedOperationException();
    }
}
