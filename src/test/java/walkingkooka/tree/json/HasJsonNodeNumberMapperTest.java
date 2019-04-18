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

public final class HasJsonNodeNumberMapperTest extends HasJsonNodeMapperTestCase2<HasJsonNodeNumberMapper, Number> {

    @Test
    public void testToJsonNodeByte() {
        this.toJsonNodeAndCheck2(Byte.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeShort() {
        this.toJsonNodeAndCheck2(Short.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeInteger() {
        this.toJsonNodeAndCheck2(Integer.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeLong() {
        this.toJsonNodeAndCheck2(Long.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeFloat() {
        this.toJsonNodeAndCheck2(Float.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeDouble() {
        this.toJsonNodeAndCheck2(Double.MAX_VALUE);
    }

    private void toJsonNodeAndCheck2(final Number value) {
        this.toJsonNodeWithTypeAndCheck(value, JsonNode.number(value.doubleValue()));
    }

    @Override
    HasJsonNodeNumberMapper mapper() {
        return HasJsonNodeNumberMapper.instance();
    }

    @Override
    Double value() {
        return 123.0;
    }

    @Override
    boolean requiresTypeName() {
        return false;
    }

    @Override
    JsonNode node() {
        return JsonNode.number(this.value());
    }

    @Override
    Number jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "number";
    }

    @Override
    Class<Number> mapperType() {
        return Number.class;
    }

    @Override
    public Class<HasJsonNodeNumberMapper> type() {
        return HasJsonNodeNumberMapper.class;
    }
}
