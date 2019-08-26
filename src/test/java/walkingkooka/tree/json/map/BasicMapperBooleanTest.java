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

package walkingkooka.tree.json.map;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;

public final class BasicMapperBooleanTest extends BasicMapperTestCase2<BasicMapperBoolean, Boolean> {

    @Test
    public void testFromTrue() {
        this.fromJsonNodeAndCheck(JsonNode.booleanNode(true), true);
    }

    @Test
    public void testFromFalse() {
        this.fromJsonNodeAndCheck(JsonNode.booleanNode(false), false);
    }

    @Test
    public void testToTrue() {
        this.toJsonNodeWithTypeAndCheck(true, JsonNode.booleanNode(true));
    }

    @Test
    public void testToFalse() {
        this.toJsonNodeWithTypeAndCheck(false, JsonNode.booleanNode(false));
    }

    @Override
    BasicMapperBoolean mapper() {
        return BasicMapperBoolean.instance();
    }

    @Override
    Boolean value() {
        return true;
    }

    @Override
    boolean requiresTypeName() {
        return false;
    }

    @Override
    JsonNode node() {
        return JsonNode.booleanNode(this.value());
    }

    @Override
    Boolean jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "boolean";
    }

    @Override
    Class<Boolean> mapperType() {
        return Boolean.class;
    }

    @Override
    public Class<BasicMapperBoolean> type() {
        return BasicMapperBoolean.class;
    }
}
