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

final class HasJsonNodeNumberMapper extends HasJsonNodeMapper<Number> {

    final static HasJsonNodeNumberMapper instance() {
        return new HasJsonNodeNumberMapper();
    }

    private HasJsonNodeNumberMapper() {
        super();
    }

    @Override
    Number fromJsonNode0(final JsonNode node) {
        return node.numberValueOrFail();
    }

    @Override
    JsonNode toJsonNodeWithType0(final Number value) {
        return this.toJsonNode0(value);
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("number");

    @Override
    JsonNode toJsonNode0(final Number value) {
        return JsonNode.number(value.doubleValue());
    }
}
