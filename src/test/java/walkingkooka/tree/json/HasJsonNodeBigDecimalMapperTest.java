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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public final class HasJsonNodeBigDecimalMapperTest extends HasJsonNodeMapperTestCase2<HasJsonNodeBigDecimalMapper, BigDecimal> {

    @Test
    public void testFromEmptyStringFails() {
        this.fromJsonNodeFailed(JsonNode.string(""), JsonNodeException.class);
    }

    @Test
    public void testFromInvalidNumberFails() {
        this.fromJsonNodeFailed(JsonNode.string("1A"), JsonNodeException.class);
    }

    @Override
    HasJsonNodeBigDecimalMapper mapper() {
        return HasJsonNodeBigDecimalMapper.instance();
    }

    @Override
    BigDecimal value() {
        return new BigDecimal("123.45");
    }

    @Override
    boolean requiresTypeName() {
        return true;
    }

    @Override
    JsonNode node() {
        return JsonNode.string(this.value().toString());
    }

    @Override
    String typeName() {
        return "big-decimal";
    }

    @Override
    public Class<HasJsonNodeBigDecimalMapper> type() {
        return HasJsonNodeBigDecimalMapper.class;
    }
}
