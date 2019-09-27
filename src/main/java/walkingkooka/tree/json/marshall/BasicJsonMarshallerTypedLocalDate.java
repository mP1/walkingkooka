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

import java.time.LocalDate;

final class BasicJsonMarshallerTypedLocalDate extends BasicJsonMarshallerTyped<LocalDate> {

    static BasicJsonMarshallerTypedLocalDate instance() {
        return new BasicJsonMarshallerTypedLocalDate();
    }

    private BasicJsonMarshallerTypedLocalDate() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<LocalDate> type() {
        return LocalDate.class;
    }

    @Override
    String typeName() {
        return "local-date";
    }

    @Override
    LocalDate unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }

    @Override
    LocalDate unmarshallNonNull(final JsonNode node,
                                  final JsonNodeUnmarshallContext context) {
        return LocalDate.parse(node.stringValueOrFail());
    }

    @Override
    JsonNode toJsonNodeNonNull(final LocalDate value,
                               final ToJsonNodeContext context) {
        return JsonNode.string(value.toString());
    }
}
