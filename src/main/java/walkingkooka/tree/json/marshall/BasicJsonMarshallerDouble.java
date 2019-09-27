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

final class BasicJsonMarshallerDouble extends BasicJsonMarshaller<Double> {

    static BasicJsonMarshallerDouble instance() {
        return new BasicJsonMarshallerDouble();
    }

    private BasicJsonMarshallerDouble() {
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
    Double unmarshallNonNull(final JsonNode node,
                               final JsonNodeUnmarshallContext context) {
        return node.numberValueOrFail().doubleValue();
    }

    @Override
    Double unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }

    @Override
    JsonNode marshallNonNull(final Double value,
                               final JsonNodeMarshallContext context) {
        return JsonNode.number(value);
    }

    @Override
    JsonNode marshallWithTypeNonNull(final Double value,
                                       final JsonNodeMarshallContext context) {
        return this.marshallNonNull(value, context);
    }
}
