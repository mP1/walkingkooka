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
import walkingkooka.tree.json.JsonNodeName;

public final class BasicJsonMarshallerTypedJsonNodeNameTest extends BasicJsonMarshallerTypedTestCase2<BasicJsonMarshallerTypedJsonNodeName, JsonNodeName> {

    @Override
    BasicJsonMarshallerTypedJsonNodeName marshaller() {
        return BasicJsonMarshallerTypedJsonNodeName.instance();
    }

    @Override
    JsonNodeName value() {
        return JsonNodeName.with("prop-1");
    }

    @Override
    JsonNode node() {
        return JsonNode.string(this.value().toString());
    }

    @Override
    JsonNodeName jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "json-property-name";
    }

    @Override
    Class<JsonNodeName> marshallerType() {
        return JsonNodeName.class;
    }

    @Override
    Class<? extends RuntimeException> fromFailsCauseType() {
        return NullPointerException.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedJsonNodeName> type() {
        return BasicJsonMarshallerTypedJsonNodeName.class;
    }
}
