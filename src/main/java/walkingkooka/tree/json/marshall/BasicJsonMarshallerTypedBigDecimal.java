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

import java.math.BigDecimal;

final class BasicJsonMarshallerTypedBigDecimal extends BasicJsonMarshallerTyped<BigDecimal> {

    static BasicJsonMarshallerTypedBigDecimal instance() {
        return new BasicJsonMarshallerTypedBigDecimal();
    }

    private BasicJsonMarshallerTypedBigDecimal() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<BigDecimal> type() {
        return BigDecimal.class;
    }

    @Override
    String typeName() {
        return "big-decimal";
    }

    @Override
    BigDecimal unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }

    @Override
    BigDecimal unmarshallNonNull(final JsonNode node,
                                 final JsonNodeUnmarshallContext context) {
        return new BigDecimal(node.stringValueOrFail());
    }

    @Override
    JsonNode marshallNonNull(final BigDecimal value,
                             final JsonNodeMarshallContext context) {
        return JsonNode.string(value.toString());
    }
}
