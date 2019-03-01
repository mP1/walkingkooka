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
import walkingkooka.collect.list.ListTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ToStringTesting;

import java.util.Map;

public final class JsonObjectNodeListTest implements ListTesting<JsonObjectNodeList, JsonNode>,
        ToStringTesting<JsonObjectNodeList> {

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createList(), "[first=true, second=false]");
    }

    @Override
    public JsonObjectNodeList createList() {
        final Map<JsonNodeName, JsonNode> map = Maps.ordered();
        map.put(JsonNodeName.with("first"), JsonNode.booleanNode(true));
        map.put(JsonNodeName.with("second"), JsonNode.booleanNode(false));

        return JsonObjectNodeList.with(map);
    }

    @Override
    public Class<JsonObjectNodeList> type() {
        return JsonObjectNodeList.class;
    }
}
