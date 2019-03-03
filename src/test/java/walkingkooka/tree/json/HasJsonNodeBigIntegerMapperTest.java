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

import java.math.BigInteger;

public final class HasJsonNodeBigIntegerMapperTest extends HasJsonNodeMapperTestCase2<HasJsonNodeBigIntegerMapper, BigInteger> {

    @Test
    public void testFromEmptyStringFails() {
        this.fromJsonNodeFailed(JsonNode.string(""), JsonNodeException.class);
    }

    @Test
    public void testFromInvalidNumberFails() {
        this.fromJsonNodeFailed(JsonNode.string("1A"), JsonNodeException.class);
    }

    @Test
    public void testFromInvalidNumberDecimalFails() {
        this.fromJsonNodeFailed(JsonNode.string("1.5"), JsonNodeException.class);
    }

    @Override
    HasJsonNodeBigIntegerMapper mapper() {
        return HasJsonNodeBigIntegerMapper.instance();
    }

    @Override
    BigInteger value() {
        return new BigInteger("123");
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
        return "big-integer";
    }

    @Override
    public Class<HasJsonNodeBigIntegerMapper> type() {
        return HasJsonNodeBigIntegerMapper.class;
    }
}
