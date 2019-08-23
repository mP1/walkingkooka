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

import walkingkooka.tree.json.JsonNode;

public final class BasicMapperDoubleTest extends BasicMapperTestCase2<BasicMapperDouble, Double> {

    @Override
    BasicMapperDouble mapper() {
        return BasicMapperDouble.instance();
    }

    @Override
    Double value() {
        return 123.5;
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
    Double jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "double";
    }

    @Override
    Class<Double> mapperType() {
        return Double.class;
    }

    @Override
    public Class<BasicMapperDouble> type() {
        return BasicMapperDouble.class;
    }
}
