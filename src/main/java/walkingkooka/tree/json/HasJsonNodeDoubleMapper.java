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

final class HasJsonNodeDoubleMapper extends HasJsonNodeMapper<Double> {

    static HasJsonNodeDoubleMapper instance() {
        return new HasJsonNodeDoubleMapper();
    }

    private HasJsonNodeDoubleMapper() {
        super();
    }

    @Override
    Double fromJsonNode0(final JsonNode node) {
        return node.numberValueOrFail().doubleValue();
    }

    @Override
    JsonNode toJsonNodeWithType0(final Double value) {
        return this.toJsonNode0(value);
    }

    @Override
    JsonStringNode typeName() {
        return JSON_STRING_NODE;
    }

    private final JsonStringNode JSON_STRING_NODE = JsonStringNode.with("double");

    @Override
    JsonNode toJsonNode0(final Double value) {
        return JsonNode.number(value);
    }
}
