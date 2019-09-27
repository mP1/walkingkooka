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

import java.math.RoundingMode;

/**
 * Handles converting to and from json a {@link RoundingMode} enum.
 */
final class BasicJsonMarshallerTypedRoundingMode extends BasicJsonMarshallerTyped<RoundingMode> {

    static BasicJsonMarshallerTypedRoundingMode instance() {
        return new BasicJsonMarshallerTypedRoundingMode();
    }

    private BasicJsonMarshallerTypedRoundingMode() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<RoundingMode> type() {
        return RoundingMode.class;
    }

    @Override
    String typeName() {
        return "rounding-mode";
    }

    @Override
    RoundingMode unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }

    @Override
    RoundingMode unmarshallNonNull(final JsonNode node,
                                     final JsonNodeUnmarshallContext context) {
        return RoundingMode.valueOf(node.stringValueOrFail());
    }

    @Override
    JsonNode toJsonNodeNonNull(final RoundingMode mode,
                               final ToJsonNodeContext context) {
        return JsonNode.string(mode.name());
    }
}
