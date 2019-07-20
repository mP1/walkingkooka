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

import java.math.RoundingMode;

/**
 * Handles converting to and from json a {@link RoundingMode} enum.
 */
final class HasJsonNodeRoundingModeMapper extends HasJsonNodeTypedMapper<RoundingMode> {

    static HasJsonNodeRoundingModeMapper instance() {
        return new HasJsonNodeRoundingModeMapper();
    }

    private HasJsonNodeRoundingModeMapper() {
        super();
    }

    @Override
    Class<RoundingMode> type() {
        return RoundingMode.class;
    }

    @Override
    RoundingMode fromJsonNodeNull() {
        return null;
    }

    @Override
    RoundingMode fromJsonNodeNonNull(final JsonNode node) {
        return RoundingMode.valueOf(node.stringValueOrFail());
    }

    @Override
    JsonNode toJsonNodeNonNull(final RoundingMode mode) {
        return JsonNode.string(mode.name());
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("rounding-mode");
}
