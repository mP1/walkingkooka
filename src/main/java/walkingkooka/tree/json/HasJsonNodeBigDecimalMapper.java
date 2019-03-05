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

import java.math.BigDecimal;

final class HasJsonNodeBigDecimalMapper extends HasJsonNodeMapper2<BigDecimal> {

    static HasJsonNodeBigDecimalMapper instance() {
        return new HasJsonNodeBigDecimalMapper();
    }

    private HasJsonNodeBigDecimalMapper() {
        super();
    }

    @Override
    BigDecimal fromJsonNodeNull() {
        return null;
    }

    @Override
    BigDecimal fromJsonNode0(final JsonNode node) {
        return new BigDecimal(node.stringValueOrFail());
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("big-decimal");

    @Override
    JsonNode toJsonNode0(final BigDecimal value) {
        return JsonNode.string(value.toString());
    }
}
