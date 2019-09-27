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

import java.util.Collection;
import java.util.stream.Collectors;

abstract class BasicJsonMarshallerTypedCollection<C extends Collection<?>> extends BasicJsonMarshallerTyped<C> {

    BasicJsonMarshallerTypedCollection() {
        super();
    }

    @Override
    final C unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }

    @Override
    final JsonNode marshallNonNull(final C value,
                                     final JsonNodeMarshallContext context) {
        return JsonObjectNode.array()
                .setChildren(value.stream()
                        .map(context::marshallWithType)
                        .collect(Collectors.toList()));
    }
}
