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

final class HasJsonNodeByteMapper extends HasJsonNodeMapper3<Byte> {

    static HasJsonNodeByteMapper instance() {
        return new HasJsonNodeByteMapper();
    }

    private HasJsonNodeByteMapper() {
        super();
    }

    @Override
    Class<Byte> type() {
        return Byte.class;
    }

    @Override
    Byte fromJsonNode2(final Number number) {
        return number.byteValue();
    }

    @Override
    JsonStringNode typeName() {
        return JSON_STRING_NODE;
    }

    private final JsonStringNode JSON_STRING_NODE = JsonStringNode.with("byte");
}
