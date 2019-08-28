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

public final class BasicMarshallerTypedBigDecimalTest extends BasicMarshallerTypedTestCase2<BasicMarshallerTypedBigDecimal, BigDecimal> {

    @Override
    BasicMarshallerTypedBigDecimal marshaller() {
        return BasicMarshallerTypedBigDecimal.instance();
    }

    @Override
    BigDecimal value() {
        return new BigDecimal("123.45");
    }

    @Override
    JsonNode node() {
        return JsonNode.string(this.value().toString());
    }

    @Override
    BigDecimal jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "big-decimal";
    }

    @Override
    Class<BigDecimal> marshallerType() {
        return BigDecimal.class;
    }

    @Override
    Class<? extends RuntimeException> fromFailsCauseType() {
        return NumberFormatException.class;
    }

    @Override
    public Class<BasicMarshallerTypedBigDecimal> type() {
        return BasicMarshallerTypedBigDecimal.class;
    }
}
