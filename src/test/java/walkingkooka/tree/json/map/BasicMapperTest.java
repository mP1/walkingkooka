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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.json.UnsupportedTypeJsonNodeException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicMapperTest extends BasicMapperTestCase<BasicMapper<Void>> {

    @AfterEach
    public void afterEach() {
        TestJsonNodeMap.unregister();
    }

    // register.........................................................................................................

    @Test
    public void testRegisterNullTypeNameFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicMapper.register(null,
                    TestJsonNodeMap::fromJsonNode,
                    TestJsonNodeMap::toJsonNode,
                    TestJsonNodeMap.class);
        });
    }

    @Test
    public void testRegisterEmptyTypeNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicMapper.register("",
                    TestJsonNodeMap::fromJsonNode,
                    TestJsonNodeMap::toJsonNode,
                    TestJsonNodeMap.class);
        });
    }

    @Test
    public void testRegisterNullFromFunctionFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicMapper.register(TestJsonNodeMap.TYPE_NAME,
                    null,
                    TestJsonNodeMap::toJsonNode,
                    TestJsonNodeMap.class);
        });
    }

    @Test
    public void testRegisterNullToFunctionFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicMapper.register(TestJsonNodeMap.TYPE_NAME,
                    TestJsonNodeMap::fromJsonNode,
                    null,
                    TestJsonNodeMap.class);
        });
    }

    @Test
    public void testRegisterNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicMapper.register(TestJsonNodeMap.TYPE_NAME,
                    TestJsonNodeMap::fromJsonNode,
                    TestJsonNodeMap::toJsonNode,
                    null);
        });
    }

    @Test
    public void testRegisterConcrete() {
        TestJsonNodeMap.register();
    }

    @Test
    public void testRegisterTwiceFails() {
        TestJsonNodeMap.register();

        assertThrows(IllegalArgumentException.class, () -> {
            BasicMapper.register(TestJsonNodeMap.TYPE_NAME,
                    TestJsonNodeMap::fromJsonNode,
                    TestJsonNodeMap::toJsonNode,
                    TestJsonNodeMap.class);
        });
    }

    @Test
    public void testRegisteredTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicMapper.registeredType(null);
        });
    }

    @Test
    public void testRegisteredType() {
        assertNotEquals(Optional.empty(), BasicMapper.registeredType(JsonNode.string("big-decimal")));
    }

    @Test
    public void testRegisteredTypeUnknown() {
        assertEquals(Optional.empty(), BasicMapper.registeredType(JsonNode.string("???")));
    }

    // typeName..........................................................................................................

    @Test
    public void testTypeNameNullClassFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicMapper.typeName(null);
        });
    }

    @Test
    public void testTypeNameUnknown() {
        this.typeNameAndCheck(this.getClass(),
                Optional.empty());
    }

    @Test
    public void testTypeNameBigDecimal() {
        this.typeNameAndCheck(BigDecimal.class,
                Optional.of(JsonNode.string("big-decimal")));
    }

    @Test
    public void testTypeNameJsonObjectNode() {
        this.typeNameAndCheck(JsonObjectNode.class,
                Optional.of(JsonNode.string("json-node")));
    }

    private void typeNameAndCheck(final Class<?> type,
                                  final Optional<JsonStringNode> typeName) {
        assertEquals(typeName,
                BasicMapper.typeName(type),
                () -> "typeName of " + type.getName());
    }

    @Test
    public void testMapperOrFailStringUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            BasicMapper.mapperOrFail("???");
        });
    }

    @Test
    public void testMapperOrFailClassUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            BasicMapper.mapperOrFail(this.getClass());
        });
    }

    @Override
    public Class<BasicMapper<Void>> type() {
        return Cast.to(BasicMapper.class);
    }
}
