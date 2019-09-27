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

import java.time.LocalTime;

final class BasicJsonMarshallerTypedLocalTime extends BasicJsonMarshallerTyped<LocalTime> {

    static BasicJsonMarshallerTypedLocalTime instance() {
        return new BasicJsonMarshallerTypedLocalTime();
    }

    private BasicJsonMarshallerTypedLocalTime() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<LocalTime> type() {
        return LocalTime.class;
    }

    @Override
    String typeName() {
        return "local-time";
    }

    @Override
    LocalTime unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }

    @Override
    LocalTime unmarshallNonNull(final JsonNode node,
                                final JsonNodeUnmarshallContext context) {
        return LocalTime.parse(node.stringValueOrFail());
    }

    @Override
    JsonNode marshallNonNull(final LocalTime value,
                             final JsonNodeMarshallContext context) {
        return JsonNode.string(value.toString());
    }
}
