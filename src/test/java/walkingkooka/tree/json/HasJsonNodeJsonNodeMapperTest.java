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

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

public final class HasJsonNodeJsonNodeMapperTest extends HasJsonNodeTypedMapperTestCase<HasJsonNodeJsonNodeMapper<JsonStringNode>, JsonNode> {

    @Test
    public void testToJsonWithType() {
        final JsonStringNode string = JsonNode.string("abc123");
        this.toJsonNodeWithTypeAndCheck(string,
                this.typeAndValue("json-string", string));
    }

    @Override
    HasJsonNodeJsonNodeMapper<JsonStringNode> mapper() {
        return HasJsonNodeJsonNodeMapper.with(JsonStringNode.class, "json-string");
    }

    @Override
    JsonStringNode value() {
        return JsonNode.string("json-string value");
    }

    @Override
    JsonNode node() {
        return this.value();
    }

    @Override
    JsonNode jsonNullNode() {
        return JsonNode.nullNode();
    }

    @Override
    String typeName() {
        return "json-string";
    }

    @Override
    Class<JsonNode> mapperType() {
        return Cast.to(JsonStringNode.class);
    }

    @Override
    public Class<HasJsonNodeJsonNodeMapper<JsonStringNode>> type() {
        return Cast.to(HasJsonNodeJsonNodeMapper.class);
    }
}
