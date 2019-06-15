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
package walkingkooka.tree.json.parser;

import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Optional;

/**
 * Base class for all JsonNode symbol parser tokens.
 */
abstract class JsonNodeSymbolParserToken extends JsonNodeLeafParserToken<String> {

    JsonNodeSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public final Optional<JsonNodeParserToken> withoutSymbols() {
        return Optional.empty();
    }

    @Override
    public final boolean isBoolean() {
        return false;
    }

    @Override
    public final boolean isNull() {
        return false;
    }

    @Override
    public final boolean isNumber() {
        return false;
    }

    @Override
    public final boolean isString() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return true;
    }

    @Override
    public final boolean isNoise() {
        return true;
    }

    @Override
    final JsonNode toJsonNodeOrNull() {
        return null;
    }

    @Override
    final void addJsonNode(final List<JsonNode> children) {
        // skip whitespace
    }
}
