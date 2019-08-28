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

final class BasicMarshallerDouble extends BasicMarshaller<Double> {

    static BasicMarshallerDouble instance() {
        return new BasicMarshallerDouble();
    }

    private BasicMarshallerDouble() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<Double> type() {
        return Double.class;
    }

    @Override
    String typeName() {
        return "double";
    }

    @Override
    Double fromJsonNodeNonNull(final JsonNode node,
                               final FromJsonNodeContext context) {
        return node.numberValueOrFail().doubleValue();
    }

    @Override
    Double fromJsonNodeNull(final FromJsonNodeContext context) {
        return null;
    }

    @Override
    JsonNode toJsonNodeNonNull(final Double value,
                               final ToJsonNodeContext context) {
        return JsonNode.number(value);
    }

    @Override
    JsonNode toJsonNodeWithTypeNonNull(final Double value,
                                       final ToJsonNodeContext context) {
        return this.toJsonNodeNonNull(value, context);
    }
}
